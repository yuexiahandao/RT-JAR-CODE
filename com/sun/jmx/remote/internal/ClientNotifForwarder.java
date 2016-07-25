/*     */ package com.sun.jmx.remote.internal;
/*     */ 
/*     */ import com.sun.jmx.remote.util.ClassLogger;
/*     */ import com.sun.jmx.remote.util.EnvHelp;
/*     */ import java.io.IOException;
/*     */ import java.io.NotSerializableException;
/*     */ import java.security.AccessControlContext;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.Executor;
/*     */ import javax.management.InstanceNotFoundException;
/*     */ import javax.management.ListenerNotFoundException;
/*     */ import javax.management.MBeanServerNotification;
/*     */ import javax.management.Notification;
/*     */ import javax.management.NotificationFilter;
/*     */ import javax.management.NotificationListener;
/*     */ import javax.management.ObjectName;
/*     */ import javax.management.remote.NotificationResult;
/*     */ import javax.management.remote.TargetedNotification;
/*     */ import javax.security.auth.Subject;
/*     */ 
/*     */ public abstract class ClientNotifForwarder
/*     */ {
/*     */   private final AccessControlContext acc;
/*     */   private static int threadId;
/*     */   private final ClassLoader defaultClassLoader;
/*     */   private final Executor executor;
/* 859 */   private final Map<Integer, ClientListenerInfo> infoList = new HashMap();
/*     */ 
/* 863 */   private long clientSequenceNumber = -1L;
/*     */   private final int maxNotifications;
/*     */   private final long timeout;
/* 866 */   private Integer mbeanRemovedNotifID = null;
/*     */   private Thread currentFetchThread;
/*     */   private static final int STARTING = 0;
/*     */   private static final int STARTED = 1;
/*     */   private static final int STOPPING = 2;
/*     */   private static final int STOPPED = 3;
/*     */   private static final int TERMINATED = 4;
/* 896 */   private int state = 3;
/*     */ 
/* 905 */   private boolean beingReconnected = false;
/*     */ 
/* 907 */   private static final ClassLogger logger = new ClassLogger("javax.management.remote.misc", "ClientNotifForwarder");
/*     */ 
/*     */   public ClientNotifForwarder(Map paramMap)
/*     */   {
/*  61 */     this(null, paramMap);
/*     */   }
/*     */ 
/*     */   public ClientNotifForwarder(ClassLoader paramClassLoader, Map<String, ?> paramMap)
/*     */   {
/* 122 */     this.maxNotifications = EnvHelp.getMaxFetchNotifNumber(paramMap);
/* 123 */     this.timeout = EnvHelp.getFetchTimeout(paramMap);
/*     */ 
/* 129 */     Object localObject = (Executor)paramMap.get("jmx.remote.x.fetch.notifications.executor");
/*     */ 
/* 131 */     if (localObject == null)
/* 132 */       localObject = new LinearExecutor(null);
/* 133 */     else if (logger.traceOn()) {
/* 134 */       logger.trace("ClientNotifForwarder", "executor is " + localObject);
/*     */     }
/* 136 */     this.defaultClassLoader = paramClassLoader;
/* 137 */     this.executor = ((Executor)localObject);
/* 138 */     this.acc = AccessController.getContext();
/*     */   }
/*     */ 
/*     */   protected abstract NotificationResult fetchNotifs(long paramLong1, int paramInt, long paramLong2)
/*     */     throws IOException, ClassNotFoundException;
/*     */ 
/*     */   protected abstract Integer addListenerForMBeanRemovedNotif()
/*     */     throws IOException, InstanceNotFoundException;
/*     */ 
/*     */   protected abstract void removeListenerForMBeanRemovedNotif(Integer paramInteger)
/*     */     throws IOException, InstanceNotFoundException, ListenerNotFoundException;
/*     */ 
/*     */   protected abstract void lostNotifs(String paramString, long paramLong);
/*     */ 
/*     */   public synchronized void addNotificationListener(Integer paramInteger, ObjectName paramObjectName, NotificationListener paramNotificationListener, NotificationFilter paramNotificationFilter, Object paramObject, Subject paramSubject)
/*     */     throws IOException, InstanceNotFoundException
/*     */   {
/* 170 */     if (logger.traceOn()) {
/* 171 */       logger.trace("addNotificationListener", "Add the listener " + paramNotificationListener + " at " + paramObjectName);
/*     */     }
/*     */ 
/* 175 */     this.infoList.put(paramInteger, new ClientListenerInfo(paramInteger, paramObjectName, paramNotificationListener, paramNotificationFilter, paramObject, paramSubject));
/*     */ 
/* 184 */     init(false);
/*     */   }
/*     */ 
/*     */   public synchronized Integer[] removeNotificationListener(ObjectName paramObjectName, NotificationListener paramNotificationListener)
/*     */     throws ListenerNotFoundException, IOException
/*     */   {
/* 192 */     beforeRemove();
/*     */ 
/* 194 */     if (logger.traceOn()) {
/* 195 */       logger.trace("removeNotificationListener", "Remove the listener " + paramNotificationListener + " from " + paramObjectName);
/*     */     }
/*     */ 
/* 199 */     ArrayList localArrayList1 = new ArrayList();
/* 200 */     ArrayList localArrayList2 = new ArrayList(this.infoList.values());
/*     */ 
/* 202 */     for (int i = localArrayList2.size() - 1; i >= 0; i--) {
/* 203 */       ClientListenerInfo localClientListenerInfo = (ClientListenerInfo)localArrayList2.get(i);
/*     */ 
/* 205 */       if (localClientListenerInfo.sameAs(paramObjectName, paramNotificationListener)) {
/* 206 */         localArrayList1.add(localClientListenerInfo.getListenerID());
/*     */ 
/* 208 */         this.infoList.remove(localClientListenerInfo.getListenerID());
/*     */       }
/*     */     }
/*     */ 
/* 212 */     if (localArrayList1.isEmpty()) {
/* 213 */       throw new ListenerNotFoundException("Listener not found");
/*     */     }
/* 215 */     return (Integer[])localArrayList1.toArray(new Integer[0]);
/*     */   }
/*     */ 
/*     */   public synchronized Integer removeNotificationListener(ObjectName paramObjectName, NotificationListener paramNotificationListener, NotificationFilter paramNotificationFilter, Object paramObject)
/*     */     throws ListenerNotFoundException, IOException
/*     */   {
/* 225 */     if (logger.traceOn()) {
/* 226 */       logger.trace("removeNotificationListener", "Remove the listener " + paramNotificationListener + " from " + paramObjectName);
/*     */     }
/*     */ 
/* 230 */     beforeRemove();
/*     */ 
/* 232 */     Integer localInteger = null;
/*     */ 
/* 234 */     ArrayList localArrayList = new ArrayList(this.infoList.values());
/*     */ 
/* 236 */     for (int i = localArrayList.size() - 1; i >= 0; i--) {
/* 237 */       ClientListenerInfo localClientListenerInfo = (ClientListenerInfo)localArrayList.get(i);
/* 238 */       if (localClientListenerInfo.sameAs(paramObjectName, paramNotificationListener, paramNotificationFilter, paramObject)) {
/* 239 */         localInteger = localClientListenerInfo.getListenerID();
/*     */ 
/* 241 */         this.infoList.remove(localInteger);
/*     */ 
/* 243 */         break;
/*     */       }
/*     */     }
/*     */ 
/* 247 */     if (localInteger == null) {
/* 248 */       throw new ListenerNotFoundException("Listener not found");
/*     */     }
/* 250 */     return localInteger;
/*     */   }
/*     */ 
/*     */   public synchronized Integer[] removeNotificationListener(ObjectName paramObjectName) {
/* 254 */     if (logger.traceOn()) {
/* 255 */       logger.trace("removeNotificationListener", "Remove all listeners registered at " + paramObjectName);
/*     */     }
/*     */ 
/* 259 */     ArrayList localArrayList1 = new ArrayList();
/*     */ 
/* 261 */     ArrayList localArrayList2 = new ArrayList(this.infoList.values());
/*     */ 
/* 263 */     for (int i = localArrayList2.size() - 1; i >= 0; i--) {
/* 264 */       ClientListenerInfo localClientListenerInfo = (ClientListenerInfo)localArrayList2.get(i);
/* 265 */       if (localClientListenerInfo.sameAs(paramObjectName)) {
/* 266 */         localArrayList1.add(localClientListenerInfo.getListenerID());
/*     */ 
/* 268 */         this.infoList.remove(localClientListenerInfo.getListenerID());
/*     */       }
/*     */     }
/*     */ 
/* 272 */     return (Integer[])localArrayList1.toArray(new Integer[0]);
/*     */   }
/*     */ 
/*     */   public synchronized ClientListenerInfo[] preReconnection()
/*     */     throws IOException
/*     */   {
/* 287 */     if ((this.state == 4) || (this.beingReconnected)) {
/* 288 */       throw new IOException("Illegal state.");
/*     */     }
/*     */ 
/* 291 */     ClientListenerInfo[] arrayOfClientListenerInfo = (ClientListenerInfo[])this.infoList.values().toArray(new ClientListenerInfo[0]);
/*     */ 
/* 295 */     this.beingReconnected = true;
/*     */ 
/* 297 */     this.infoList.clear();
/*     */ 
/* 299 */     return arrayOfClientListenerInfo;
/*     */   }
/*     */ 
/*     */   public synchronized void postReconnection(ClientListenerInfo[] paramArrayOfClientListenerInfo)
/*     */     throws IOException
/*     */   {
/* 310 */     if (this.state == 4) {
/* 311 */       return;
/*     */     }
/*     */ 
/* 314 */     while (this.state == 2) {
/*     */       try {
/* 316 */         wait();
/*     */       } catch (InterruptedException localInterruptedException1) {
/* 318 */         IOException localIOException1 = new IOException(localInterruptedException1.toString());
/* 319 */         EnvHelp.initCause(localIOException1, localInterruptedException1);
/* 320 */         throw localIOException1;
/*     */       }
/*     */     }
/*     */ 
/* 324 */     boolean bool = logger.traceOn();
/* 325 */     int i = paramArrayOfClientListenerInfo.length;
/*     */ 
/* 327 */     for (int j = 0; j < i; j++) {
/* 328 */       if (bool) {
/* 329 */         logger.trace("addNotificationListeners", "Add a listener at " + paramArrayOfClientListenerInfo[j].getListenerID());
/*     */       }
/*     */ 
/* 334 */       this.infoList.put(paramArrayOfClientListenerInfo[j].getListenerID(), paramArrayOfClientListenerInfo[j]);
/*     */     }
/*     */ 
/* 337 */     this.beingReconnected = false;
/* 338 */     notifyAll();
/*     */ 
/* 340 */     if ((this.currentFetchThread == Thread.currentThread()) || (this.state == 0) || (this.state == 1))
/*     */     {
/*     */       try
/*     */       {
/* 344 */         this.mbeanRemovedNotifID = addListenerForMBeanRemovedNotif();
/*     */       }
/*     */       catch (Exception localException)
/*     */       {
/* 350 */         if (logger.traceOn())
/* 351 */           logger.trace("init", "Failed to register a listener to the mbean server: the client will not do clean when an MBean is unregistered", localException);
/*     */       }
/*     */     }
/*     */     else {
/* 355 */       while (this.state == 2) {
/*     */         try {
/* 357 */           wait();
/*     */         } catch (InterruptedException localInterruptedException2) {
/* 359 */           IOException localIOException2 = new IOException(localInterruptedException2.toString());
/* 360 */           EnvHelp.initCause(localIOException2, localInterruptedException2);
/* 361 */           throw localIOException2;
/*     */         }
/*     */       }
/*     */ 
/* 365 */       if (paramArrayOfClientListenerInfo.length > 0)
/* 366 */         init(true);
/* 367 */       else if (this.infoList.size() > 0)
/* 368 */         init(false);
/*     */     }
/*     */   }
/*     */ 
/*     */   public synchronized void terminate()
/*     */   {
/* 374 */     if (this.state == 4) {
/* 375 */       return;
/*     */     }
/*     */ 
/* 378 */     if (logger.traceOn()) {
/* 379 */       logger.trace("terminate", "Terminating...");
/*     */     }
/*     */ 
/* 382 */     if (this.state == 1) {
/* 383 */       this.infoList.clear();
/*     */     }
/*     */ 
/* 386 */     setState(4);
/*     */   }
/*     */ 
/*     */   private synchronized void setState(int paramInt)
/*     */   {
/* 734 */     if (this.state == 4) {
/* 735 */       return;
/*     */     }
/*     */ 
/* 738 */     this.state = paramInt;
/* 739 */     notifyAll();
/*     */   }
/*     */ 
/*     */   private synchronized void init(boolean paramBoolean)
/*     */     throws IOException
/*     */   {
/* 751 */     switch (this.state) {
/*     */     case 1:
/* 753 */       return;
/*     */     case 0:
/* 755 */       return;
/*     */     case 4:
/* 757 */       throw new IOException("The ClientNotifForwarder has been terminated.");
/*     */     case 2:
/* 759 */       if (this.beingReconnected == true)
/*     */       {
/* 761 */         return;
/*     */       }
/*     */ 
/* 764 */       while (this.state == 2) {
/*     */         try {
/* 766 */           wait();
/*     */         } catch (InterruptedException localInterruptedException) {
/* 768 */           IOException localIOException = new IOException(localInterruptedException.toString());
/* 769 */           EnvHelp.initCause(localIOException, localInterruptedException);
/*     */ 
/* 771 */           throw localIOException;
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 777 */       init(paramBoolean);
/*     */ 
/* 779 */       return;
/*     */     case 3:
/* 781 */       if (this.beingReconnected == true)
/*     */       {
/* 783 */         return;
/*     */       }
/*     */ 
/* 786 */       if (logger.traceOn()) {
/* 787 */         logger.trace("init", "Initializing...");
/*     */       }
/*     */ 
/* 791 */       if (!paramBoolean) {
/*     */         try {
/* 793 */           NotificationResult localNotificationResult = fetchNotifs(-1L, 0, 0L);
/* 794 */           this.clientSequenceNumber = localNotificationResult.getNextSequenceNumber();
/*     */         }
/*     */         catch (ClassNotFoundException localClassNotFoundException) {
/* 797 */           logger.warning("init", "Impossible exception: " + localClassNotFoundException);
/* 798 */           logger.debug("init", localClassNotFoundException);
/*     */         }
/*     */       }
/*     */ 
/*     */       try
/*     */       {
/* 804 */         this.mbeanRemovedNotifID = addListenerForMBeanRemovedNotif();
/*     */       }
/*     */       catch (Exception localException)
/*     */       {
/* 810 */         if (logger.traceOn()) {
/* 811 */           logger.trace("init", "Failed to register a listener to the mbean server: the client will not do clean when an MBean is unregistered", localException);
/*     */         }
/*     */       }
/*     */ 
/* 815 */       setState(0);
/*     */ 
/* 818 */       this.executor.execute(new NotifFetcher(null));
/*     */ 
/* 820 */       return;
/*     */     }
/*     */ 
/* 823 */     throw new IOException("Unknown state.");
/*     */   }
/*     */ 
/*     */   private synchronized void beforeRemove()
/*     */     throws IOException
/*     */   {
/* 832 */     while (this.beingReconnected) {
/* 833 */       if (this.state == 4) {
/* 834 */         throw new IOException("Terminated.");
/*     */       }
/*     */       try
/*     */       {
/* 838 */         wait();
/*     */       } catch (InterruptedException localInterruptedException) {
/* 840 */         IOException localIOException = new IOException(localInterruptedException.toString());
/* 841 */         EnvHelp.initCause(localIOException, localInterruptedException);
/*     */ 
/* 843 */         throw localIOException;
/*     */       }
/*     */     }
/*     */ 
/* 847 */     if (this.state == 4)
/* 848 */       throw new IOException("Terminated.");
/*     */   }
/*     */ 
/*     */   private static class LinearExecutor
/*     */     implements Executor
/*     */   {
/*     */     private Runnable command;
/*     */     private Thread thread;
/*     */ 
/*     */     public synchronized void execute(Runnable paramRunnable)
/*     */     {
/*  88 */       if (this.command != null)
/*  89 */         throw new IllegalArgumentException("More than one command");
/*  90 */       this.command = paramRunnable;
/*  91 */       if (this.thread == null) {
/*  92 */         this.thread = new Thread()
/*     */         {
/*     */           public void run()
/*     */           {
/*     */             while (true)
/*     */             {
/*     */               Runnable localRunnable;
/*  98 */               synchronized (ClientNotifForwarder.LinearExecutor.this) {
/*  99 */                 if (ClientNotifForwarder.LinearExecutor.this.command == null) {
/* 100 */                   ClientNotifForwarder.LinearExecutor.this.thread = null;
/* 101 */                   return;
/*     */                 }
/* 103 */                 localRunnable = ClientNotifForwarder.LinearExecutor.this.command;
/* 104 */                 ClientNotifForwarder.LinearExecutor.this.command = null;
/*     */               }
/*     */ 
/* 107 */               localRunnable.run();
/*     */             }
/*     */           }
/*     */         };
/* 111 */         this.thread.setDaemon(true);
/* 112 */         this.thread.setName("ClientNotifForwarder-" + ClientNotifForwarder.access$204());
/* 113 */         this.thread.start();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private class NotifFetcher
/*     */     implements Runnable
/*     */   {
/* 397 */     private volatile boolean alreadyLogged = false;
/*     */ 
/*     */     private NotifFetcher() {  } 
/* 400 */     private void logOnce(String paramString, SecurityException paramSecurityException) { if (this.alreadyLogged) return;
/*     */ 
/* 402 */       ClientNotifForwarder.logger.config("setContextClassLoader", paramString);
/* 403 */       if (paramSecurityException != null) ClientNotifForwarder.logger.fine("setContextClassLoader", paramSecurityException);
/* 404 */       this.alreadyLogged = true;
/*     */     }
/*     */ 
/*     */     private final ClassLoader setContextClassLoader(final ClassLoader paramClassLoader)
/*     */     {
/* 409 */       AccessControlContext localAccessControlContext = ClientNotifForwarder.this.acc;
/*     */ 
/* 412 */       if (localAccessControlContext == null) {
/* 413 */         logOnce("AccessControlContext must not be null.", null);
/* 414 */         throw new SecurityException("AccessControlContext must not be null");
/*     */       }
/* 416 */       return (ClassLoader)AccessController.doPrivileged(new PrivilegedAction()
/*     */       {
/*     */         public ClassLoader run()
/*     */         {
/*     */           try
/*     */           {
/* 422 */             ClassLoader localClassLoader = Thread.currentThread().getContextClassLoader();
/*     */ 
/* 426 */             if (paramClassLoader == localClassLoader) return localClassLoader;
/*     */ 
/* 430 */             Thread.currentThread().setContextClassLoader(paramClassLoader);
/* 431 */             return localClassLoader;
/*     */           } catch (SecurityException localSecurityException) {
/* 433 */             ClientNotifForwarder.NotifFetcher.this.logOnce("Permission to set ContextClassLoader missing. Notifications will not be dispatched. Please check your Java policy configuration: " + localSecurityException, localSecurityException);
/*     */ 
/* 437 */             throw localSecurityException;
/*     */           }
/*     */         }
/*     */       }
/*     */       , localAccessControlContext);
/*     */     }
/*     */ 
/*     */     public void run()
/*     */     {
/*     */       ClassLoader localClassLoader;
/* 445 */       if (ClientNotifForwarder.this.defaultClassLoader != null)
/* 446 */         localClassLoader = setContextClassLoader(ClientNotifForwarder.this.defaultClassLoader);
/*     */       else
/* 448 */         localClassLoader = null;
/*     */       try
/*     */       {
/* 451 */         doRun();
/*     */       } finally {
/* 453 */         if (ClientNotifForwarder.this.defaultClassLoader != null)
/* 454 */           setContextClassLoader(localClassLoader);
/*     */       }
/*     */     }
/*     */ 
/*     */     private void doRun()
/*     */     {
/* 460 */       synchronized (ClientNotifForwarder.this) {
/* 461 */         ClientNotifForwarder.this.currentFetchThread = Thread.currentThread();
/*     */ 
/* 463 */         if (ClientNotifForwarder.this.state == 0) {
/* 464 */           ClientNotifForwarder.this.setState(1);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 469 */       ??? = null;
/* 470 */       if ((!shouldStop()) && (( = fetchNotifs()) != null))
/*     */       {
/* 473 */         TargetedNotification[] arrayOfTargetedNotification = ((NotificationResult)???).getTargetedNotifications();
/*     */ 
/* 475 */         Object localObject2 = arrayOfTargetedNotification.length;
/*     */ 
/* 479 */         long l = 0L;
/*     */         HashMap localHashMap;
/*     */         Object localObject3;
/*     */         Integer localInteger1;
/* 481 */         synchronized (ClientNotifForwarder.this)
/*     */         {
/* 484 */           if (ClientNotifForwarder.this.clientSequenceNumber >= 0L) {
/* 485 */             l = ((NotificationResult)???).getEarliestSequenceNumber() - ClientNotifForwarder.this.clientSequenceNumber;
/*     */           }
/*     */ 
/* 489 */           ClientNotifForwarder.this.clientSequenceNumber = ((NotificationResult)???).getNextSequenceNumber();
/*     */ 
/* 491 */           localHashMap = new HashMap();
/*     */ 
/* 493 */           for (localObject3 = 0; localObject3 < localObject2; localObject3++) {
/* 494 */             TargetedNotification localTargetedNotification = arrayOfTargetedNotification[localObject3];
/* 495 */             Integer localInteger2 = localTargetedNotification.getListenerID();
/*     */             Object localObject4;
/* 498 */             if (!localInteger2.equals(ClientNotifForwarder.this.mbeanRemovedNotifID)) {
/* 499 */               localObject4 = (ClientListenerInfo)ClientNotifForwarder.this.infoList.get(localInteger2);
/* 500 */               if (localObject4 != null)
/* 501 */                 localHashMap.put(localInteger2, localObject4);
/*     */             }
/*     */             else
/*     */             {
/* 505 */               localObject4 = localTargetedNotification.getNotification();
/*     */ 
/* 508 */               if (((localObject4 instanceof MBeanServerNotification)) && (((Notification)localObject4).getType().equals("JMX.mbean.unregistered")))
/*     */               {
/* 511 */                 MBeanServerNotification localMBeanServerNotification = (MBeanServerNotification)localObject4;
/*     */ 
/* 513 */                 ObjectName localObjectName = localMBeanServerNotification.getMBeanName();
/*     */ 
/* 515 */                 ClientNotifForwarder.this.removeNotificationListener(localObjectName);
/*     */               }
/*     */             }
/*     */           }
/* 518 */           localInteger1 = ClientNotifForwarder.this.mbeanRemovedNotifID;
/*     */         }
/*     */ 
/* 521 */         if (l > 0L) {
/* 522 */           ??? = "May have lost up to " + l + " notification" + (l == 1L ? "" : "s");
/*     */ 
/* 525 */           ClientNotifForwarder.this.lostNotifs(???, l);
/* 526 */           ClientNotifForwarder.logger.trace("NotifFetcher.run", ???);
/*     */         }
/*     */ 
/* 530 */         for (??? = 0; ??? < localObject2; ???++) {
/* 531 */           localObject3 = arrayOfTargetedNotification[???];
/* 532 */           dispatchNotification((TargetedNotification)localObject3, localInteger1, localHashMap);
/*     */         }
/*     */       }
/*     */ 
/* 536 */       synchronized (ClientNotifForwarder.this) {
/* 537 */         ClientNotifForwarder.this.currentFetchThread = null;
/*     */       }
/*     */ 
/* 540 */       if ((??? == null) || (shouldStop()))
/*     */       {
/* 542 */         ClientNotifForwarder.this.setState(3);
/*     */         try
/*     */         {
/* 545 */           ClientNotifForwarder.this.removeListenerForMBeanRemovedNotif(ClientNotifForwarder.this.mbeanRemovedNotifID);
/*     */         } catch (Exception localException) {
/* 547 */           if (ClientNotifForwarder.logger.traceOn())
/* 548 */             ClientNotifForwarder.logger.trace("NotifFetcher-run", "removeListenerForMBeanRemovedNotif", localException);
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/* 553 */         ClientNotifForwarder.this.executor.execute(this);
/*     */       }
/*     */     }
/*     */ 
/*     */     void dispatchNotification(TargetedNotification paramTargetedNotification, Integer paramInteger, Map<Integer, ClientListenerInfo> paramMap)
/*     */     {
/* 560 */       Notification localNotification = paramTargetedNotification.getNotification();
/* 561 */       Integer localInteger = paramTargetedNotification.getListenerID();
/*     */ 
/* 563 */       if (localInteger.equals(paramInteger)) return;
/* 564 */       ClientListenerInfo localClientListenerInfo = (ClientListenerInfo)paramMap.get(localInteger);
/*     */ 
/* 566 */       if (localClientListenerInfo == null) {
/* 567 */         ClientNotifForwarder.logger.trace("NotifFetcher.dispatch", "Listener ID not in map");
/*     */ 
/* 569 */         return;
/*     */       }
/*     */ 
/* 572 */       NotificationListener localNotificationListener = localClientListenerInfo.getListener();
/* 573 */       Object localObject = localClientListenerInfo.getHandback();
/*     */       try {
/* 575 */         localNotificationListener.handleNotification(localNotification, localObject);
/*     */       }
/*     */       catch (RuntimeException localRuntimeException)
/*     */       {
/* 580 */         ClientNotifForwarder.logger.trace("NotifFetcher-run", "Failed to forward a notification to a listener", localRuntimeException);
/*     */       }
/*     */     }
/*     */ 
/*     */     private NotificationResult fetchNotifs()
/*     */     {
/*     */       try {
/* 587 */         NotificationResult localNotificationResult = ClientNotifForwarder.this.fetchNotifs(ClientNotifForwarder.this.clientSequenceNumber, ClientNotifForwarder.this.maxNotifications, ClientNotifForwarder.this.timeout);
/*     */ 
/* 591 */         if (ClientNotifForwarder.logger.traceOn()) {
/* 592 */           ClientNotifForwarder.logger.trace("NotifFetcher-run", "Got notifications from the server: " + localNotificationResult);
/*     */         }
/*     */ 
/* 596 */         return localNotificationResult;
/*     */       } catch (ClassNotFoundException localClassNotFoundException) {
/* 598 */         ClientNotifForwarder.logger.trace("NotifFetcher.fetchNotifs", localClassNotFoundException);
/* 599 */         return fetchOneNotif();
/*     */       } catch (NotSerializableException localNotSerializableException) {
/* 601 */         ClientNotifForwarder.logger.trace("NotifFetcher.fetchNotifs", localNotSerializableException);
/* 602 */         return fetchOneNotif();
/*     */       } catch (IOException localIOException) {
/* 604 */         if (!shouldStop()) {
/* 605 */           ClientNotifForwarder.logger.error("NotifFetcher-run", "Failed to fetch notification, stopping thread. Error is: " + localIOException, localIOException);
/*     */ 
/* 608 */           ClientNotifForwarder.logger.debug("NotifFetcher-run", localIOException);
/*     */         }
/*     */       }
/*     */ 
/* 612 */       return null;
/*     */     }
/*     */ 
/*     */     private NotificationResult fetchOneNotif()
/*     */     {
/* 635 */       ClientNotifForwarder localClientNotifForwarder = ClientNotifForwarder.this;
/*     */ 
/* 637 */       long l1 = ClientNotifForwarder.this.clientSequenceNumber;
/*     */ 
/* 639 */       int i = 0;
/*     */ 
/* 641 */       NotificationResult localNotificationResult = null;
/* 642 */       long l2 = -1L;
/*     */       Object localObject;
/* 644 */       while ((localNotificationResult == null) && (!shouldStop()))
/*     */       {
/*     */         try
/*     */         {
/* 649 */           localObject = localClientNotifForwarder.fetchNotifs(l1, 0, 0L);
/*     */         } catch (ClassNotFoundException localClassNotFoundException) {
/* 651 */           ClientNotifForwarder.logger.warning("NotifFetcher.fetchOneNotif", "Impossible exception: " + localClassNotFoundException);
/*     */ 
/* 653 */           ClientNotifForwarder.logger.debug("NotifFetcher.fetchOneNotif", localClassNotFoundException);
/* 654 */           return null;
/*     */         } catch (IOException localIOException) {
/* 656 */           if (!shouldStop())
/* 657 */             ClientNotifForwarder.logger.trace("NotifFetcher.fetchOneNotif", localIOException);
/* 658 */           return null;
/*     */         }
/*     */ 
/* 661 */         if (shouldStop()) {
/* 662 */           return null;
/*     */         }
/* 664 */         l1 = ((NotificationResult)localObject).getNextSequenceNumber();
/* 665 */         if (l2 < 0L) {
/* 666 */           l2 = ((NotificationResult)localObject).getEarliestSequenceNumber();
/*     */         }
/*     */         try
/*     */         {
/* 670 */           localNotificationResult = localClientNotifForwarder.fetchNotifs(l1, 1, 0L);
/*     */         } catch (Exception localException) {
/* 672 */           if (((localException instanceof ClassNotFoundException)) || ((localException instanceof NotSerializableException)))
/*     */           {
/* 674 */             ClientNotifForwarder.logger.warning("NotifFetcher.fetchOneNotif", "Failed to deserialize a notification: " + localException.toString());
/*     */ 
/* 676 */             if (ClientNotifForwarder.logger.traceOn()) {
/* 677 */               ClientNotifForwarder.logger.trace("NotifFetcher.fetchOneNotif", "Failed to deserialize a notification.", localException);
/*     */             }
/*     */ 
/* 681 */             i++;
/* 682 */             l1 += 1L;
/*     */           } else {
/* 684 */             if (!shouldStop())
/* 685 */               ClientNotifForwarder.logger.trace("NotifFetcher.fetchOneNotif", localException);
/* 686 */             return null;
/*     */           }
/*     */         }
/*     */       }
/*     */ 
/* 691 */       if (i > 0) {
/* 692 */         localObject = "Dropped " + i + " notification" + (i == 1 ? "" : "s") + " because classes were missing locally";
/*     */ 
/* 696 */         ClientNotifForwarder.this.lostNotifs((String)localObject, i);
/*     */ 
/* 703 */         if (localNotificationResult != null) {
/* 704 */           localNotificationResult = new NotificationResult(l2, localNotificationResult.getNextSequenceNumber(), localNotificationResult.getTargetedNotifications());
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 710 */       return localNotificationResult;
/*     */     }
/*     */ 
/*     */     private boolean shouldStop() {
/* 714 */       synchronized (ClientNotifForwarder.this) {
/* 715 */         if (ClientNotifForwarder.this.state != 1)
/* 716 */           return true;
/* 717 */         if (ClientNotifForwarder.this.infoList.size() == 0)
/*     */         {
/* 719 */           ClientNotifForwarder.this.setState(2);
/*     */ 
/* 721 */           return true;
/*     */         }
/*     */ 
/* 724 */         return false;
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.remote.internal.ClientNotifForwarder
 * JD-Core Version:    0.6.2
 */