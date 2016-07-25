/*      */ package com.sun.jmx.snmp.daemon;
/*      */ 
/*      */ import com.sun.jmx.defaults.JmxProperties;
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.net.InetAddress;
/*      */ import java.util.NoSuchElementException;
/*      */ import java.util.Vector;
/*      */ import java.util.logging.Level;
/*      */ import java.util.logging.Logger;
/*      */ import javax.management.AttributeChangeNotification;
/*      */ import javax.management.ListenerNotFoundException;
/*      */ import javax.management.MBeanNotificationInfo;
/*      */ import javax.management.MBeanRegistration;
/*      */ import javax.management.MBeanServer;
/*      */ import javax.management.MBeanServerFactory;
/*      */ import javax.management.NotificationBroadcaster;
/*      */ import javax.management.NotificationBroadcasterSupport;
/*      */ import javax.management.NotificationFilter;
/*      */ import javax.management.NotificationListener;
/*      */ import javax.management.ObjectName;
/*      */ import javax.management.loading.ClassLoaderRepository;
/*      */ import javax.management.remote.MBeanServerForwarder;
/*      */ 
/*      */ public abstract class CommunicatorServer
/*      */   implements Runnable, MBeanRegistration, NotificationBroadcaster, CommunicatorServerMBean
/*      */ {
/*      */   public static final int ONLINE = 0;
/*      */   public static final int OFFLINE = 1;
/*      */   public static final int STOPPING = 2;
/*      */   public static final int STARTING = 3;
/*      */   public static final int SNMP_TYPE = 4;
/*  179 */   volatile transient int state = 1;
/*      */   ObjectName objectName;
/*      */   MBeanServer topMBS;
/*      */   MBeanServer bottomMBS;
/*  192 */   transient String dbgTag = null;
/*      */ 
/*  199 */   int maxActiveClientCount = 1;
/*      */ 
/*  203 */   transient int servedClientCount = 0;
/*      */ 
/*  209 */   String host = null;
/*      */ 
/*  215 */   int port = -1;
/*      */ 
/*  225 */   private transient Object stateLock = new Object();
/*      */ 
/*  227 */   private transient Vector<ClientHandler> clientHandlerVector = new Vector();
/*      */ 
/*  230 */   private transient Thread fatherThread = Thread.currentThread();
/*  231 */   private transient Thread mainThread = null;
/*      */ 
/*  233 */   private volatile boolean stopRequested = false;
/*  234 */   private boolean interrupted = false;
/*  235 */   private transient Exception startException = null;
/*      */ 
/*  238 */   private transient long notifCount = 0L;
/*  239 */   private transient NotificationBroadcasterSupport notifBroadcaster = new NotificationBroadcasterSupport();
/*      */ 
/*  241 */   private transient MBeanNotificationInfo[] notifInfos = null;
/*      */ 
/*      */   public CommunicatorServer(int paramInt)
/*      */     throws IllegalArgumentException
/*      */   {
/*  255 */     switch (paramInt)
/*      */     {
/*      */     case 4:
/*  258 */       break;
/*      */     default:
/*  260 */       throw new IllegalArgumentException("Invalid connector Type");
/*      */     }
/*  262 */     this.dbgTag = makeDebugTag();
/*      */   }
/*      */ 
/*      */   protected Thread createMainThread() {
/*  266 */     return new Thread(this, makeThreadName());
/*      */   }
/*      */ 
/*      */   public void start(long paramLong)
/*      */     throws CommunicationException, InterruptedException
/*      */   {
/*      */     int i;
/*  290 */     synchronized (this.stateLock) {
/*  291 */       if (this.state == 2)
/*      */       {
/*  294 */         waitState(1, 60000L);
/*      */       }
/*  296 */       i = this.state == 1 ? 1 : 0;
/*  297 */       if (i != 0) {
/*  298 */         changeState(3);
/*  299 */         this.stopRequested = false;
/*  300 */         this.interrupted = false;
/*  301 */         this.startException = null;
/*      */       }
/*      */     }
/*      */ 
/*  305 */     if (i == 0) {
/*  306 */       if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINER)) {
/*  307 */         JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINER, this.dbgTag, "start", "Connector is not OFFLINE");
/*      */       }
/*      */ 
/*  310 */       return;
/*      */     }
/*      */ 
/*  313 */     if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINER)) {
/*  314 */       JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINER, this.dbgTag, "start", "--> Start connector ");
/*      */     }
/*      */ 
/*  318 */     this.mainThread = createMainThread();
/*      */ 
/*  320 */     this.mainThread.start();
/*      */ 
/*  322 */     if (paramLong > 0L) waitForStart(paramLong);
/*      */   }
/*      */ 
/*      */   public void start()
/*      */   {
/*      */     try
/*      */     {
/*  333 */       start(0L);
/*      */     }
/*      */     catch (InterruptedException localInterruptedException) {
/*  336 */       if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINER))
/*  337 */         JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINER, this.dbgTag, "start", "interrupted", localInterruptedException);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void stop()
/*      */   {
/*  350 */     synchronized (this.stateLock) {
/*  351 */       if ((this.state == 1) || (this.state == 2)) {
/*  352 */         if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINER)) {
/*  353 */           JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINER, this.dbgTag, "stop", "Connector is not ONLINE");
/*      */         }
/*      */ 
/*  356 */         return;
/*      */       }
/*  358 */       changeState(2);
/*      */ 
/*  362 */       if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINER)) {
/*  363 */         JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINER, this.dbgTag, "stop", "Interrupt main thread");
/*      */       }
/*      */ 
/*  366 */       this.stopRequested = true;
/*  367 */       if (!this.interrupted) {
/*  368 */         this.interrupted = true;
/*  369 */         this.mainThread.interrupt();
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  376 */     if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINER)) {
/*  377 */       JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINER, this.dbgTag, "stop", "terminateAllClient");
/*      */     }
/*      */ 
/*  380 */     terminateAllClient();
/*      */ 
/*  385 */     synchronized (this.stateLock) {
/*  386 */       if (this.state == 3)
/*  387 */         changeState(1);
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean isActive()
/*      */   {
/*  397 */     synchronized (this.stateLock) {
/*  398 */       return this.state == 0;
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean waitState(int paramInt, long paramLong)
/*      */   {
/*  435 */     if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINER)) {
/*  436 */       JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINER, this.dbgTag, "waitState", paramInt + "(0on,1off,2st) TO=" + paramLong + " ; current state = " + getStateString());
/*      */     }
/*      */ 
/*  441 */     long l1 = 0L;
/*  442 */     if (paramLong > 0L) {
/*  443 */       l1 = System.currentTimeMillis() + paramLong;
/*      */     }
/*  445 */     synchronized (this.stateLock) {
/*  446 */       while (this.state != paramInt) {
/*  447 */         if (paramLong < 0L) {
/*  448 */           if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINER)) {
/*  449 */             JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINER, this.dbgTag, "waitState", "timeOut < 0, return without wait");
/*      */           }
/*      */ 
/*  452 */           return false;
/*      */         }
/*      */         try {
/*  455 */           if (paramLong > 0L) {
/*  456 */             long l2 = l1 - System.currentTimeMillis();
/*  457 */             if (l2 <= 0L) {
/*  458 */               if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINER)) {
/*  459 */                 JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINER, this.dbgTag, "waitState", "timed out");
/*      */               }
/*      */ 
/*  462 */               return false;
/*      */             }
/*  464 */             this.stateLock.wait(l2);
/*      */           } else {
/*  466 */             this.stateLock.wait();
/*      */           }
/*      */         } catch (InterruptedException localInterruptedException) {
/*  469 */           if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINER)) {
/*  470 */             JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINER, this.dbgTag, "waitState", "wait interrupted");
/*      */           }
/*      */ 
/*  473 */           return this.state == paramInt;
/*      */         }
/*      */       }
/*      */ 
/*  477 */       if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINER)) {
/*  478 */         JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINER, this.dbgTag, "waitState", "returning in desired state");
/*      */       }
/*      */ 
/*  481 */       return true;
/*      */     }
/*      */   }
/*      */ 
/*      */   private void waitForStart(long paramLong)
/*      */     throws CommunicationException, InterruptedException
/*      */   {
/*  504 */     if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINER)) {
/*  505 */       JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINER, this.dbgTag, "waitForStart", "Timeout=" + paramLong + " ; current state = " + getStateString());
/*      */     }
/*      */ 
/*  510 */     long l1 = System.currentTimeMillis();
/*      */ 
/*  512 */     synchronized (this.stateLock) {
/*  513 */       while (this.state == 3)
/*      */       {
/*  516 */         long l2 = System.currentTimeMillis() - l1;
/*      */ 
/*  523 */         long l3 = paramLong - l2;
/*      */ 
/*  527 */         if (l3 < 0L) {
/*  528 */           if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINER)) {
/*  529 */             JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINER, this.dbgTag, "waitForStart", "timeout < 0, return without wait");
/*      */           }
/*      */ 
/*  532 */           throw new InterruptedException("Timeout expired");
/*      */         }
/*      */ 
/*      */         try
/*      */         {
/*  540 */           this.stateLock.wait(l3);
/*      */         } catch (InterruptedException localInterruptedException) {
/*  542 */           if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINER)) {
/*  543 */             JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINER, this.dbgTag, "waitForStart", "wait interrupted");
/*      */           }
/*      */ 
/*  551 */           if (this.state != 0) throw localInterruptedException;
/*      */ 
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  557 */       if (this.state == 0)
/*      */       {
/*  560 */         if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINER)) {
/*  561 */           JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINER, this.dbgTag, "waitForStart", "started");
/*      */         }
/*      */ 
/*  564 */         return;
/*  565 */       }if ((this.startException instanceof CommunicationException))
/*      */       {
/*  569 */         throw ((CommunicationException)this.startException);
/*  570 */       }if ((this.startException instanceof InterruptedException))
/*      */       {
/*  574 */         throw ((InterruptedException)this.startException);
/*  575 */       }if (this.startException != null)
/*      */       {
/*  579 */         throw new CommunicationException(this.startException, "Failed to start: " + this.startException);
/*      */       }
/*      */ 
/*  586 */       throw new CommunicationException("Failed to start: state is " + getStringForState(this.state));
/*      */     }
/*      */   }
/*      */ 
/*      */   public int getState()
/*      */   {
/*  599 */     synchronized (this.stateLock) {
/*  600 */       return this.state;
/*      */     }
/*      */   }
/*      */ 
/*      */   public String getStateString()
/*      */   {
/*  611 */     return getStringForState(this.state);
/*      */   }
/*      */ 
/*      */   public String getHost()
/*      */   {
/*      */     try
/*      */     {
/*  621 */       this.host = InetAddress.getLocalHost().getHostName();
/*      */     } catch (Exception localException) {
/*  623 */       this.host = "Unknown host";
/*      */     }
/*  625 */     return this.host;
/*      */   }
/*      */ 
/*      */   public int getPort()
/*      */   {
/*  634 */     synchronized (this.stateLock) {
/*  635 */       return this.port;
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setPort(int paramInt)
/*      */     throws IllegalStateException
/*      */   {
/*  649 */     synchronized (this.stateLock) {
/*  650 */       if ((this.state == 0) || (this.state == 3)) {
/*  651 */         throw new IllegalStateException("Stop server before carrying out this operation");
/*      */       }
/*  653 */       this.port = paramInt;
/*  654 */       this.dbgTag = makeDebugTag();
/*      */     }
/*      */   }
/*      */ 
/*      */   public abstract String getProtocol();
/*      */ 
/*      */   int getServedClientCount()
/*      */   {
/*  674 */     return this.servedClientCount;
/*      */   }
/*      */ 
/*      */   int getActiveClientCount()
/*      */   {
/*  685 */     int i = this.clientHandlerVector.size();
/*  686 */     return i;
/*      */   }
/*      */ 
/*      */   int getMaxActiveClientCount()
/*      */   {
/*  698 */     return this.maxActiveClientCount;
/*      */   }
/*      */ 
/*      */   void setMaxActiveClientCount(int paramInt)
/*      */     throws IllegalStateException
/*      */   {
/*  712 */     synchronized (this.stateLock) {
/*  713 */       if ((this.state == 0) || (this.state == 3)) {
/*  714 */         throw new IllegalStateException("Stop server before carrying out this operation");
/*      */       }
/*      */ 
/*  717 */       this.maxActiveClientCount = paramInt;
/*      */     }
/*      */   }
/*      */ 
/*      */   void notifyClientHandlerCreated(ClientHandler paramClientHandler)
/*      */   {
/*  725 */     this.clientHandlerVector.addElement(paramClientHandler);
/*      */   }
/*      */ 
/*      */   synchronized void notifyClientHandlerDeleted(ClientHandler paramClientHandler)
/*      */   {
/*  732 */     this.clientHandlerVector.removeElement(paramClientHandler);
/*  733 */     notifyAll();
/*      */   }
/*      */ 
/*      */   protected int getBindTries()
/*      */   {
/*  741 */     return 50;
/*      */   }
/*      */ 
/*      */   protected long getBindSleepTime()
/*      */   {
/*  749 */     return 100L;
/*      */   }
/*      */ 
/*      */   public void run()
/*      */   {
/*  764 */     int i = 0;
/*  765 */     int j = 0;
/*      */     try
/*      */     {
/*  773 */       int k = getBindTries();
/*  774 */       long l = getBindSleepTime();
/*  775 */       while ((i < k) && (j == 0))
/*      */       {
/*      */         try
/*      */         {
/*  779 */           doBind();
/*  780 */           j = 1;
/*      */         } catch (CommunicationException localCommunicationException) {
/*  782 */           i++;
/*      */           try {
/*  784 */             Thread.sleep(l);
/*      */           } catch (InterruptedException localInterruptedException) {
/*  786 */             throw localInterruptedException;
/*      */           }
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  792 */       if (j == 0)
/*      */       {
/*  795 */         doBind();
/*      */       }
/*      */     }
/*      */     catch (Exception localException1) {
/*  799 */       if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST)) {
/*  800 */         JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, this.dbgTag, "run", "Got unexpected exception", localException1);
/*      */       }
/*      */ 
/*  803 */       synchronized (this.stateLock) {
/*  804 */         this.startException = localException1;
/*  805 */         changeState(1);
/*      */       }
/*  807 */       if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINER)) {
/*  808 */         JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINER, this.dbgTag, "run", "State is OFFLINE");
/*      */       }
/*      */ 
/*  811 */       doError(localException1);
/*  812 */       return;
/*      */     }
/*      */ 
/*      */     try
/*      */     {
/*  819 */       changeState(0);
/*  820 */       if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINER)) {
/*  821 */         JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINER, this.dbgTag, "run", "State is ONLINE");
/*      */       }
/*      */ 
/*  828 */       while (!this.stopRequested) {
/*  829 */         this.servedClientCount += 1;
/*  830 */         doReceive();
/*  831 */         waitIfTooManyClients();
/*  832 */         doProcess();
/*      */       }
/*  834 */       if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINER)) {
/*  835 */         JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINER, this.dbgTag, "run", "Stop has been requested");
/*      */       }
/*      */     }
/*      */     catch (InterruptedException localException1)
/*      */     {
/*  840 */       if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST)) {
/*  841 */         JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, this.dbgTag, "run", "Interrupt caught");
/*      */       }
/*      */ 
/*  844 */       changeState(2);
/*      */     } catch (Exception localException1) {
/*  846 */       if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST)) {
/*  847 */         JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, this.dbgTag, "run", "Got unexpected exception", ???);
/*      */       }
/*      */ 
/*  850 */       changeState(2);
/*      */     } finally {
/*  852 */       synchronized (this.stateLock) {
/*  853 */         this.interrupted = true;
/*  854 */         Thread.currentThread(); Thread.interrupted();
/*      */       }
/*      */ 
/*      */       try
/*      */       {
/*  861 */         doUnbind();
/*  862 */         waitClientTermination();
/*  863 */         changeState(1);
/*  864 */         if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINER))
/*  865 */           JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINER, this.dbgTag, "run", "State is OFFLINE");
/*      */       }
/*      */       catch (Exception localException3)
/*      */       {
/*  869 */         if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST)) {
/*  870 */           JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, this.dbgTag, "run", "Got unexpected exception", localException3);
/*      */         }
/*      */ 
/*  873 */         changeState(1);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   protected abstract void doError(Exception paramException)
/*      */     throws CommunicationException;
/*      */ 
/*      */   protected abstract void doBind()
/*      */     throws CommunicationException, InterruptedException;
/*      */ 
/*      */   protected abstract void doReceive()
/*      */     throws CommunicationException, InterruptedException;
/*      */ 
/*      */   protected abstract void doProcess()
/*      */     throws CommunicationException, InterruptedException;
/*      */ 
/*      */   protected abstract void doUnbind()
/*      */     throws CommunicationException, InterruptedException;
/*      */ 
/*      */   public synchronized MBeanServer getMBeanServer()
/*      */   {
/*  941 */     return this.topMBS;
/*      */   }
/*      */ 
/*      */   public synchronized void setMBeanServer(MBeanServer paramMBeanServer)
/*      */     throws IllegalArgumentException, IllegalStateException
/*      */   {
/*  964 */     synchronized (this.stateLock) {
/*  965 */       if ((this.state == 0) || (this.state == 3)) {
/*  966 */         throw new IllegalStateException("Stop server before carrying out this operation");
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  973 */     Vector localVector = new Vector();
/*  974 */     for (MBeanServer localMBeanServer = paramMBeanServer; 
/*  975 */       localMBeanServer != this.bottomMBS; 
/*  976 */       localMBeanServer = ((MBeanServerForwarder)localMBeanServer).getMBeanServer()) {
/*  977 */       if (!(localMBeanServer instanceof MBeanServerForwarder))
/*  978 */         throw new IllegalArgumentException("MBeanServer argument must be MBean server where this server is registered, or an MBeanServerForwarder leading to that server");
/*  979 */       if (localVector.contains(localMBeanServer)) {
/*  980 */         throw new IllegalArgumentException("MBeanServerForwarder loop");
/*      */       }
/*  982 */       localVector.addElement(localMBeanServer);
/*      */     }
/*  984 */     this.topMBS = paramMBeanServer;
/*      */   }
/*      */ 
/*      */   ObjectName getObjectName()
/*      */   {
/*  994 */     return this.objectName;
/*      */   }
/*      */ 
/*      */   void changeState(int paramInt)
/*      */   {
/*      */     int i;
/* 1002 */     synchronized (this.stateLock) {
/* 1003 */       if (this.state == paramInt)
/* 1004 */         return;
/* 1005 */       i = this.state;
/* 1006 */       this.state = paramInt;
/* 1007 */       this.stateLock.notifyAll();
/*      */     }
/* 1009 */     sendStateChangeNotification(i, paramInt);
/*      */   }
/*      */ 
/*      */   String makeDebugTag()
/*      */   {
/* 1016 */     return "CommunicatorServer[" + getProtocol() + ":" + getPort() + "]";
/*      */   }
/*      */ 
/*      */   String makeThreadName()
/*      */   {
/*      */     String str;
/* 1025 */     if (this.objectName == null)
/* 1026 */       str = "CommunicatorServer";
/*      */     else {
/* 1028 */       str = this.objectName.toString();
/*      */     }
/* 1030 */     return str;
/*      */   }
/*      */ 
/*      */   private synchronized void waitIfTooManyClients()
/*      */     throws InterruptedException
/*      */   {
/* 1040 */     while (getActiveClientCount() >= this.maxActiveClientCount) {
/* 1041 */       if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINER)) {
/* 1042 */         JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINER, this.dbgTag, "waitIfTooManyClients", "Waiting for a client to terminate");
/*      */       }
/*      */ 
/* 1045 */       wait();
/*      */     }
/*      */   }
/*      */ 
/*      */   private void waitClientTermination()
/*      */   {
/* 1053 */     int i = this.clientHandlerVector.size();
/* 1054 */     if ((JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINER)) && 
/* 1055 */       (i >= 1)) {
/* 1056 */       JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINER, this.dbgTag, "waitClientTermination", "waiting for " + i + " clients to terminate");
/*      */     }
/*      */ 
/* 1074 */     while (!this.clientHandlerVector.isEmpty()) {
/*      */       try {
/* 1076 */         ((ClientHandler)this.clientHandlerVector.firstElement()).join();
/*      */       } catch (NoSuchElementException localNoSuchElementException) {
/* 1078 */         if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINER)) {
/* 1079 */           JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINER, this.dbgTag, "waitClientTermination", "No elements left", localNoSuchElementException);
/*      */         }
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1085 */     if ((JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINER)) && 
/* 1086 */       (i >= 1))
/* 1087 */       JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINER, this.dbgTag, "waitClientTermination", "Ok, let's go...");
/*      */   }
/*      */ 
/*      */   private void terminateAllClient()
/*      */   {
/* 1097 */     int i = this.clientHandlerVector.size();
/* 1098 */     if ((JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINER)) && 
/* 1099 */       (i >= 1)) {
/* 1100 */       JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINER, this.dbgTag, "terminateAllClient", "Interrupting " + i + " clients");
/*      */     }
/*      */ 
/* 1122 */     ClientHandler[] arrayOfClientHandler1 = (ClientHandler[])this.clientHandlerVector.toArray(new ClientHandler[0]);
/*      */ 
/* 1124 */     for (ClientHandler localClientHandler : arrayOfClientHandler1)
/*      */       try {
/* 1126 */         localClientHandler.interrupt();
/*      */       } catch (Exception localException) {
/* 1128 */         if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINER))
/* 1129 */           JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINER, this.dbgTag, "terminateAllClient", "Failed to interrupt pending request. Ignore the exception.", localException);
/*      */       }
/*      */   }
/*      */ 
/*      */   private void readObject(ObjectInputStream paramObjectInputStream)
/*      */     throws IOException, ClassNotFoundException
/*      */   {
/* 1146 */     paramObjectInputStream.defaultReadObject();
/*      */ 
/* 1152 */     this.stateLock = new Object();
/* 1153 */     this.state = 1;
/* 1154 */     this.stopRequested = false;
/* 1155 */     this.servedClientCount = 0;
/* 1156 */     this.clientHandlerVector = new Vector();
/* 1157 */     this.fatherThread = Thread.currentThread();
/* 1158 */     this.mainThread = null;
/* 1159 */     this.notifCount = 0L;
/* 1160 */     this.notifInfos = null;
/* 1161 */     this.notifBroadcaster = new NotificationBroadcasterSupport();
/* 1162 */     this.dbgTag = makeDebugTag();
/*      */   }
/*      */ 
/*      */   public void addNotificationListener(NotificationListener paramNotificationListener, NotificationFilter paramNotificationFilter, Object paramObject)
/*      */     throws IllegalArgumentException
/*      */   {
/* 1192 */     if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST)) {
/* 1193 */       JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, this.dbgTag, "addNotificationListener", "Adding listener " + paramNotificationListener + " with filter " + paramNotificationFilter + " and handback " + paramObject);
/*      */     }
/*      */ 
/* 1197 */     this.notifBroadcaster.addNotificationListener(paramNotificationListener, paramNotificationFilter, paramObject);
/*      */   }
/*      */ 
/*      */   public void removeNotificationListener(NotificationListener paramNotificationListener)
/*      */     throws ListenerNotFoundException
/*      */   {
/* 1213 */     if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST)) {
/* 1214 */       JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, this.dbgTag, "removeNotificationListener", "Removing listener " + paramNotificationListener);
/*      */     }
/*      */ 
/* 1217 */     this.notifBroadcaster.removeNotificationListener(paramNotificationListener);
/*      */   }
/*      */ 
/*      */   public MBeanNotificationInfo[] getNotificationInfo()
/*      */   {
/* 1232 */     if (this.notifInfos == null) {
/* 1233 */       this.notifInfos = new MBeanNotificationInfo[1];
/* 1234 */       String[] arrayOfString = { "jmx.attribute.change" };
/*      */ 
/* 1236 */       this.notifInfos[0] = new MBeanNotificationInfo(arrayOfString, AttributeChangeNotification.class.getName(), "Sent to notify that the value of the State attribute of this CommunicatorServer instance has changed.");
/*      */     }
/*      */ 
/* 1242 */     return this.notifInfos;
/*      */   }
/*      */ 
/*      */   private void sendStateChangeNotification(int paramInt1, int paramInt2)
/*      */   {
/* 1250 */     String str1 = getStringForState(paramInt1);
/* 1251 */     String str2 = getStringForState(paramInt2);
/* 1252 */     String str3 = this.dbgTag + " The value of attribute State has changed from " + paramInt1 + " (" + str1 + ") to " + paramInt2 + " (" + str2 + ").";
/*      */ 
/* 1258 */     this.notifCount += 1L;
/* 1259 */     AttributeChangeNotification localAttributeChangeNotification = new AttributeChangeNotification(this, this.notifCount, System.currentTimeMillis(), str3, "State", "int", new Integer(paramInt1), new Integer(paramInt2));
/*      */ 
/* 1268 */     if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST)) {
/* 1269 */       JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, this.dbgTag, "sendStateChangeNotification", "Sending AttributeChangeNotification #" + this.notifCount + " with message: " + str3);
/*      */     }
/*      */ 
/* 1273 */     this.notifBroadcaster.sendNotification(localAttributeChangeNotification);
/*      */   }
/*      */ 
/*      */   private static String getStringForState(int paramInt)
/*      */   {
/* 1280 */     switch (paramInt) { case 0:
/* 1281 */       return "ONLINE";
/*      */     case 3:
/* 1282 */       return "STARTING";
/*      */     case 1:
/* 1283 */       return "OFFLINE";
/*      */     case 2:
/* 1284 */       return "STOPPING"; }
/* 1285 */     return "UNDEFINED";
/*      */   }
/*      */ 
/*      */   public ObjectName preRegister(MBeanServer paramMBeanServer, ObjectName paramObjectName)
/*      */     throws Exception
/*      */   {
/* 1309 */     this.objectName = paramObjectName;
/* 1310 */     synchronized (this) {
/* 1311 */       if (this.bottomMBS != null) {
/* 1312 */         throw new IllegalArgumentException("connector already registered in an MBean server");
/*      */       }
/*      */ 
/* 1316 */       this.topMBS = (this.bottomMBS = paramMBeanServer);
/*      */     }
/* 1318 */     this.dbgTag = makeDebugTag();
/* 1319 */     return paramObjectName;
/*      */   }
/*      */ 
/*      */   public void postRegister(Boolean paramBoolean)
/*      */   {
/* 1329 */     if (!paramBoolean.booleanValue())
/* 1330 */       synchronized (this) {
/* 1331 */         this.topMBS = (this.bottomMBS = null);
/*      */       }
/*      */   }
/*      */ 
/*      */   public void preDeregister()
/*      */     throws Exception
/*      */   {
/* 1344 */     synchronized (this) {
/* 1345 */       this.topMBS = (this.bottomMBS = null);
/*      */     }
/* 1347 */     this.objectName = null;
/* 1348 */     int i = getState();
/* 1349 */     if ((i == 0) || (i == 3))
/* 1350 */       stop();
/*      */   }
/*      */ 
/*      */   public void postDeregister()
/*      */   {
/*      */   }
/*      */ 
/*      */   Class loadClass(String paramString)
/*      */     throws ClassNotFoundException
/*      */   {
/*      */     try
/*      */     {
/* 1366 */       return Class.forName(paramString);
/*      */     } catch (ClassNotFoundException localClassNotFoundException) {
/* 1368 */       ClassLoaderRepository localClassLoaderRepository = MBeanServerFactory.getClassLoaderRepository(this.bottomMBS);
/*      */ 
/* 1370 */       if (localClassLoaderRepository == null) throw new ClassNotFoundException(paramString);
/* 1371 */       return localClassLoaderRepository.loadClass(paramString);
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.snmp.daemon.CommunicatorServer
 * JD-Core Version:    0.6.2
 */