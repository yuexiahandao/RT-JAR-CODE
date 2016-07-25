/*     */ package com.sun.jmx.remote.internal;
/*     */ 
/*     */ import com.sun.jmx.remote.util.ClassLogger;
/*     */ 
/*     */ public abstract class ServerCommunicatorAdmin
/*     */ {
/*     */   private long timestamp;
/* 218 */   private final int[] lock = new int[0];
/* 219 */   private int currentJobs = 0;
/*     */   private long timeout;
/* 224 */   private boolean terminated = false;
/*     */ 
/* 226 */   private static final ClassLogger logger = new ClassLogger("javax.management.remote.misc", "ServerCommunicatorAdmin");
/*     */ 
/* 229 */   private static final ClassLogger timelogger = new ClassLogger("javax.management.remote.timeout", "ServerCommunicatorAdmin");
/*     */ 
/*     */   public ServerCommunicatorAdmin(long paramLong)
/*     */   {
/*  34 */     if (logger.traceOn()) {
/*  35 */       logger.trace("Constructor", "Creates a new ServerCommunicatorAdmin object with the timeout " + paramLong);
/*     */     }
/*     */ 
/*  40 */     this.timeout = paramLong;
/*     */ 
/*  42 */     this.timestamp = 0L;
/*  43 */     if (paramLong < 9223372036854775807L) {
/*  44 */       Timeout localTimeout = new Timeout(null);
/*  45 */       Thread localThread = new Thread(localTimeout);
/*  46 */       localThread.setName("JMX server connection timeout " + localThread.getId());
/*     */ 
/*  49 */       localThread.setDaemon(true);
/*  50 */       localThread.start();
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean reqIncoming()
/*     */   {
/*  64 */     if (logger.traceOn()) {
/*  65 */       logger.trace("reqIncoming", "Receive a new request.");
/*     */     }
/*     */ 
/*  68 */     synchronized (this.lock) {
/*  69 */       if (this.terminated) {
/*  70 */         logger.warning("reqIncoming", "The server has decided to close this client connection.");
/*     */       }
/*     */ 
/*  74 */       this.currentJobs += 1;
/*     */ 
/*  76 */       return this.terminated;
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean rspOutgoing()
/*     */   {
/*  87 */     if (logger.traceOn()) {
/*  88 */       logger.trace("reqIncoming", "Finish a request.");
/*     */     }
/*     */ 
/*  91 */     synchronized (this.lock) {
/*  92 */       if (--this.currentJobs == 0) {
/*  93 */         this.timestamp = System.currentTimeMillis();
/*  94 */         logtime("Admin: Timestamp=", this.timestamp);
/*     */ 
/*  96 */         this.lock.notify();
/*     */       }
/*  98 */       return this.terminated;
/*     */     }
/*     */   }
/*     */ 
/*     */   protected abstract void doStop();
/*     */ 
/*     */   public void terminate()
/*     */   {
/* 112 */     if (logger.traceOn()) {
/* 113 */       logger.trace("terminate", "terminate the ServerCommunicatorAdmin object.");
/*     */     }
/*     */ 
/* 117 */     synchronized (this.lock) {
/* 118 */       if (this.terminated) {
/* 119 */         return;
/*     */       }
/*     */ 
/* 122 */       this.terminated = true;
/*     */ 
/* 125 */       this.lock.notify();
/*     */     }
/*     */   }
/*     */ 
/*     */   private void logtime(String paramString, long paramLong)
/*     */   {
/* 210 */     timelogger.trace("synchro", paramString + paramLong);
/*     */   }
/*     */ 
/*     */   private class Timeout
/*     */     implements Runnable
/*     */   {
/*     */     private Timeout()
/*     */     {
/*     */     }
/*     */ 
/*     */     public void run()
/*     */     {
/* 134 */       int i = 0;
/*     */ 
/* 136 */       synchronized (ServerCommunicatorAdmin.this.lock) {
/* 137 */         if (ServerCommunicatorAdmin.this.timestamp == 0L) ServerCommunicatorAdmin.this.timestamp = System.currentTimeMillis();
/* 138 */         ServerCommunicatorAdmin.this.logtime("Admin: timeout=", ServerCommunicatorAdmin.this.timeout);
/* 139 */         ServerCommunicatorAdmin.this.logtime("Admin: Timestamp=", ServerCommunicatorAdmin.this.timestamp);
/*     */ 
/* 141 */         while (!ServerCommunicatorAdmin.this.terminated) {
/*     */           try
/*     */           {
/* 144 */             while ((!ServerCommunicatorAdmin.this.terminated) && (ServerCommunicatorAdmin.this.currentJobs != 0)) {
/* 145 */               if (ServerCommunicatorAdmin.logger.traceOn()) {
/* 146 */                 ServerCommunicatorAdmin.logger.trace("Timeout-run", "Waiting without timeout.");
/*     */               }
/*     */ 
/* 150 */               ServerCommunicatorAdmin.this.lock.wait();
/*     */             }
/*     */ 
/* 153 */             if (ServerCommunicatorAdmin.this.terminated) return;
/*     */ 
/* 155 */             long l1 = ServerCommunicatorAdmin.this.timeout - (System.currentTimeMillis() - ServerCommunicatorAdmin.this.timestamp);
/*     */ 
/* 158 */             ServerCommunicatorAdmin.this.logtime("Admin: remaining timeout=", l1);
/*     */ 
/* 160 */             if (l1 > 0L)
/*     */             {
/* 162 */               if (ServerCommunicatorAdmin.logger.traceOn()) {
/* 163 */                 ServerCommunicatorAdmin.logger.trace("Timeout-run", "Waiting with timeout: " + l1 + " ms remaining");
/*     */               }
/*     */ 
/* 168 */               ServerCommunicatorAdmin.this.lock.wait(l1);
/*     */             }
/*     */ 
/* 171 */             if (ServerCommunicatorAdmin.this.currentJobs <= 0)
/*     */             {
/* 173 */               long l2 = System.currentTimeMillis() - ServerCommunicatorAdmin.this.timestamp;
/*     */ 
/* 175 */               ServerCommunicatorAdmin.this.logtime("Admin: elapsed=", l2);
/*     */ 
/* 177 */               if ((!ServerCommunicatorAdmin.this.terminated) && (l2 > ServerCommunicatorAdmin.this.timeout)) {
/* 178 */                 if (ServerCommunicatorAdmin.logger.traceOn()) {
/* 179 */                   ServerCommunicatorAdmin.logger.trace("Timeout-run", "timeout elapsed");
/*     */                 }
/*     */ 
/* 182 */                 ServerCommunicatorAdmin.this.logtime("Admin: timeout elapsed! " + l2 + ">", ServerCommunicatorAdmin.this.timeout);
/*     */ 
/* 185 */                 ServerCommunicatorAdmin.this.terminated = true;
/*     */ 
/* 187 */                 i = 1;
/*     */               }
/*     */             }
/*     */           } catch (InterruptedException localInterruptedException) {
/* 191 */             ServerCommunicatorAdmin.logger.warning("Timeout-run", "Unexpected Exception: " + localInterruptedException);
/*     */ 
/* 193 */             ServerCommunicatorAdmin.logger.debug("Timeout-run", localInterruptedException);
/* 194 */             return;
/*     */           }
/*     */         }
/*     */       }
/*     */ 
/* 199 */       if (i != 0) {
/* 200 */         if (ServerCommunicatorAdmin.logger.traceOn()) {
/* 201 */           ServerCommunicatorAdmin.logger.trace("Timeout-run", "Call the doStop.");
/*     */         }
/*     */ 
/* 204 */         ServerCommunicatorAdmin.this.doStop();
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.remote.internal.ServerCommunicatorAdmin
 * JD-Core Version:    0.6.2
 */