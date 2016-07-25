/*     */ package com.sun.jmx.snmp.daemon;
/*     */ 
/*     */ import com.sun.jmx.defaults.JmxProperties;
/*     */ import java.util.logging.Logger;
/*     */ import javax.management.MBeanServer;
/*     */ import javax.management.ObjectName;
/*     */ 
/*     */ abstract class ClientHandler
/*     */   implements Runnable
/*     */ {
/* 122 */   protected CommunicatorServer adaptorServer = null;
/* 123 */   protected int requestId = -1;
/* 124 */   protected MBeanServer mbs = null;
/* 125 */   protected ObjectName objectName = null;
/* 126 */   protected Thread thread = null;
/* 127 */   protected boolean interruptCalled = false;
/* 128 */   protected String dbgTag = null;
/*     */ 
/*     */   public ClientHandler(CommunicatorServer paramCommunicatorServer, int paramInt, MBeanServer paramMBeanServer, ObjectName paramObjectName)
/*     */   {
/*  53 */     this.adaptorServer = paramCommunicatorServer;
/*  54 */     this.requestId = paramInt;
/*  55 */     this.mbs = paramMBeanServer;
/*  56 */     this.objectName = paramObjectName;
/*  57 */     this.interruptCalled = false;
/*  58 */     this.dbgTag = makeDebugTag();
/*     */ 
/*  61 */     this.thread = createThread(this);
/*     */   }
/*     */ 
/*     */   Thread createThread(Runnable paramRunnable)
/*     */   {
/*  71 */     return new Thread(this);
/*     */   }
/*     */ 
/*     */   public void interrupt() {
/*  75 */     JmxProperties.SNMP_ADAPTOR_LOGGER.entering(this.dbgTag, "interrupt");
/*  76 */     this.interruptCalled = true;
/*  77 */     if (this.thread != null) {
/*  78 */       this.thread.interrupt();
/*     */     }
/*  80 */     JmxProperties.SNMP_ADAPTOR_LOGGER.exiting(this.dbgTag, "interrupt");
/*     */   }
/*     */ 
/*     */   public void join()
/*     */   {
/*  85 */     if (this.thread != null)
/*     */       try {
/*  87 */         this.thread.join();
/*     */       }
/*     */       catch (InterruptedException localInterruptedException)
/*     */       {
/*     */       }
/*     */   }
/*     */ 
/*     */   public void run()
/*     */   {
/*     */     try
/*     */     {
/* 100 */       this.adaptorServer.notifyClientHandlerCreated(this);
/*     */ 
/* 105 */       doRun();
/*     */     }
/*     */     finally
/*     */     {
/* 113 */       this.adaptorServer.notifyClientHandlerDeleted(this);
/*     */     }
/*     */   }
/*     */ 
/*     */   public abstract void doRun();
/*     */ 
/*     */   protected String makeDebugTag()
/*     */   {
/* 131 */     return "ClientHandler[" + this.adaptorServer.getProtocol() + ":" + this.adaptorServer.getPort() + "][" + this.requestId + "]";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.snmp.daemon.ClientHandler
 * JD-Core Version:    0.6.2
 */