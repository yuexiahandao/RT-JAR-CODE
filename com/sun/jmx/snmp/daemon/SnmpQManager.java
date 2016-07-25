/*     */ package com.sun.jmx.snmp.daemon;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.Vector;
/*     */ 
/*     */ final class SnmpQManager
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 2163709017015248264L;
/*     */   private SendQ newq;
/*     */   private WaitQ waitq;
/*  31 */   private ThreadGroup queueThreadGroup = null;
/*  32 */   private Thread requestQThread = null;
/*  33 */   private Thread timerQThread = null;
/*     */ 
/*     */   SnmpQManager()
/*     */   {
/*  39 */     this.newq = new SendQ(20, 5);
/*  40 */     this.waitq = new WaitQ(20, 5);
/*     */ 
/*  42 */     this.queueThreadGroup = new ThreadGroup("Qmanager Thread Group");
/*     */ 
/*  45 */     startQThreads();
/*     */   }
/*     */ 
/*     */   public void startQThreads() {
/*  49 */     if ((this.timerQThread == null) || (!this.timerQThread.isAlive())) {
/*  50 */       this.timerQThread = new SnmpTimerServer(this.queueThreadGroup, this);
/*     */     }
/*  52 */     if ((this.requestQThread == null) || (!this.requestQThread.isAlive()))
/*  53 */       this.requestQThread = new SnmpSendServer(this.queueThreadGroup, this);
/*     */   }
/*     */ 
/*     */   public void stopQThreads()
/*     */   {
/*  59 */     ((SnmpTimerServer)this.timerQThread).isBeingDestroyed = true;
/*  60 */     this.waitq.isBeingDestroyed = true;
/*  61 */     ((SnmpSendServer)this.requestQThread).isBeingDestroyed = true;
/*  62 */     this.newq.isBeingDestroyed = true;
/*     */ 
/*  64 */     if ((this.timerQThread != null) && (this.timerQThread.isAlive() == true)) {
/*  65 */       ((SnmpTimerServer)this.timerQThread).stopTimerServer();
/*     */     }
/*  67 */     this.waitq = null;
/*  68 */     this.timerQThread = null;
/*     */ 
/*  70 */     if ((this.requestQThread != null) && (this.requestQThread.isAlive() == true)) {
/*  71 */       ((SnmpSendServer)this.requestQThread).stopSendServer();
/*     */     }
/*  73 */     this.newq = null;
/*  74 */     this.requestQThread = null;
/*     */   }
/*     */ 
/*     */   public void addRequest(SnmpInformRequest paramSnmpInformRequest) {
/*  78 */     this.newq.addRequest(paramSnmpInformRequest);
/*     */   }
/*     */ 
/*     */   public void addWaiting(SnmpInformRequest paramSnmpInformRequest)
/*     */   {
/*  83 */     this.waitq.addWaiting(paramSnmpInformRequest);
/*     */   }
/*     */ 
/*     */   public Vector getAllOutstandingRequest(long paramLong)
/*     */   {
/*  88 */     return this.newq.getAllOutstandingRequest(paramLong);
/*     */   }
/*     */ 
/*     */   public SnmpInformRequest getTimeoutRequests() {
/*  92 */     return this.waitq.getTimeoutRequests();
/*     */   }
/*     */ 
/*     */   public void removeRequest(SnmpInformRequest paramSnmpInformRequest) {
/*  96 */     this.newq.removeElement(paramSnmpInformRequest);
/*  97 */     this.waitq.removeElement(paramSnmpInformRequest);
/*     */   }
/*     */ 
/*     */   public SnmpInformRequest removeRequest(long paramLong) {
/* 101 */     SnmpInformRequest localSnmpInformRequest = null;
/*     */ 
/* 103 */     if ((localSnmpInformRequest = this.newq.removeRequest(paramLong)) == null) {
/* 104 */       localSnmpInformRequest = this.waitq.removeRequest(paramLong);
/*     */     }
/* 106 */     return localSnmpInformRequest;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.snmp.daemon.SnmpQManager
 * JD-Core Version:    0.6.2
 */