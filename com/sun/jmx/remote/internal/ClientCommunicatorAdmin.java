/*     */ package com.sun.jmx.remote.internal;
/*     */ 
/*     */ import com.sun.jmx.remote.util.ClassLogger;
/*     */ import com.sun.jmx.remote.util.EnvHelp;
/*     */ import java.io.IOException;
/*     */ import java.io.InterruptedIOException;
/*     */ 
/*     */ public abstract class ClientCommunicatorAdmin
/*     */ {
/*  35 */   private static volatile long threadNo = 1L;
/*     */   private final Checker checker;
/*     */   private long period;
/*     */   private static final int CONNECTED = 0;
/*     */   private static final int RE_CONNECTING = 1;
/*     */   private static final int FAILED = 2;
/*     */   private static final int TERMINATED = 3;
/* 245 */   private int state = 0;
/*     */ 
/* 247 */   private final int[] lock = new int[0];
/*     */ 
/* 249 */   private static final ClassLogger logger = new ClassLogger("javax.management.remote.misc", "ClientCommunicatorAdmin");
/*     */ 
/*     */   public ClientCommunicatorAdmin(long paramLong)
/*     */   {
/*  38 */     this.period = paramLong;
/*     */ 
/*  40 */     if (paramLong > 0L) {
/*  41 */       this.checker = new Checker(null);
/*     */ 
/*  43 */       Thread localThread = new Thread(this.checker, "JMX client heartbeat " + ++threadNo);
/*  44 */       localThread.setDaemon(true);
/*  45 */       localThread.start();
/*     */     } else {
/*  47 */       this.checker = null;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void gotIOException(IOException paramIOException)
/*     */     throws IOException
/*     */   {
/*  54 */     restart(paramIOException);
/*     */   }
/*     */ 
/*     */   protected abstract void checkConnection()
/*     */     throws IOException;
/*     */ 
/*     */   protected abstract void doStart()
/*     */     throws IOException;
/*     */ 
/*     */   protected abstract void doStop();
/*     */ 
/*     */   public void terminate()
/*     */   {
/*  76 */     synchronized (this.lock) {
/*  77 */       if (this.state == 3) {
/*  78 */         return;
/*     */       }
/*     */ 
/*  81 */       this.state = 3;
/*     */ 
/*  83 */       this.lock.notifyAll();
/*     */ 
/*  85 */       if (this.checker != null)
/*  86 */         this.checker.stop();
/*     */     }
/*     */   }
/*     */ 
/*     */   private void restart(IOException paramIOException) throws IOException
/*     */   {
/*  92 */     synchronized (this.lock) {
/*  93 */       if (this.state == 3)
/*  94 */         throw new IOException("The client has been closed.");
/*  95 */       if (this.state == 2)
/*  96 */         throw paramIOException;
/*  97 */       if (this.state == 1)
/*     */       {
/* 100 */         while (this.state == 1) {
/*     */           try {
/* 102 */             this.lock.wait();
/*     */           }
/*     */           catch (InterruptedException localInterruptedException) {
/* 105 */             InterruptedIOException localInterruptedIOException = new InterruptedIOException(localInterruptedException.toString());
/* 106 */             EnvHelp.initCause(localInterruptedIOException, localInterruptedException);
/*     */ 
/* 108 */             throw localInterruptedIOException;
/*     */           }
/*     */         }
/*     */ 
/* 112 */         if (this.state == 3)
/* 113 */           throw new IOException("The client has been closed.");
/* 114 */         if (this.state != 0)
/*     */         {
/* 116 */           throw paramIOException;
/*     */         }
/*     */       } else {
/* 119 */         this.state = 1;
/* 120 */         this.lock.notifyAll();
/*     */       }
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 126 */       doStart();
/* 127 */       synchronized (this.lock) {
/* 128 */         if (this.state == 3) {
/* 129 */           throw new IOException("The client has been closed.");
/*     */         }
/*     */ 
/* 132 */         this.state = 0;
/*     */ 
/* 134 */         this.lock.notifyAll();
/*     */       }
/*     */ 
/* 137 */       return;
/*     */     } catch (Exception localException1) {
/* 139 */       logger.warning("restart", "Failed to restart: " + localException1);
/* 140 */       logger.debug("restart", localException1);
/*     */ 
/* 142 */       synchronized (this.lock) {
/* 143 */         if (this.state == 3) {
/* 144 */           throw new IOException("The client has been closed.");
/*     */         }
/*     */ 
/* 147 */         this.state = 2;
/*     */ 
/* 149 */         this.lock.notifyAll();
/*     */       }
/*     */       try
/*     */       {
/* 153 */         doStop();
/*     */       }
/*     */       catch (Exception localException2)
/*     */       {
/*     */       }
/*     */ 
/* 159 */       terminate();
/*     */     }
/* 161 */     throw paramIOException;
/*     */   }
/*     */   private class Checker implements Runnable {
/*     */     private Thread myThread;
/*     */ 
/*     */     private Checker() {
/*     */     }
/*     */ 
/*     */     public void run() {
/* 170 */       this.myThread = Thread.currentThread();
/*     */ 
/* 172 */       while ((ClientCommunicatorAdmin.this.state != 3) && (!this.myThread.isInterrupted())) {
/*     */         try {
/* 174 */           Thread.sleep(ClientCommunicatorAdmin.this.period);
/*     */         }
/*     */         catch (InterruptedException localInterruptedException)
/*     */         {
/*     */         }
/*     */ 
/* 180 */         if ((ClientCommunicatorAdmin.this.state == 3) || (this.myThread.isInterrupted())) {
/*     */           break;
/*     */         }
/*     */         try
/*     */         {
/* 185 */           ClientCommunicatorAdmin.this.checkConnection();
/*     */         } catch (Exception localException1) {
/* 187 */           synchronized (ClientCommunicatorAdmin.this.lock) {
/* 188 */             if ((ClientCommunicatorAdmin.this.state == 3) || (this.myThread.isInterrupted())) {
/* 189 */               break;
/*     */             }
/*     */           }
/*     */ 
/* 193 */           Exception localException2 = (Exception)EnvHelp.getCause(localException1);
/*     */ 
/* 195 */           if (((localException2 instanceof IOException)) && (!(localException2 instanceof InterruptedIOException)))
/*     */           {
/*     */             try {
/* 198 */               ClientCommunicatorAdmin.this.restart((IOException)localException2);
/*     */             } catch (Exception localException3) {
/* 200 */               ClientCommunicatorAdmin.logger.warning("Checker-run", "Failed to check connection: " + localException2);
/*     */ 
/* 202 */               ClientCommunicatorAdmin.logger.warning("Checker-run", "stopping");
/* 203 */               ClientCommunicatorAdmin.logger.debug("Checker-run", localException2);
/*     */ 
/* 205 */               break;
/*     */             }
/*     */           } else {
/* 208 */             ClientCommunicatorAdmin.logger.warning("Checker-run", "Failed to check the connection: " + localException2);
/*     */ 
/* 210 */             ClientCommunicatorAdmin.logger.debug("Checker-run", localException2);
/*     */ 
/* 214 */             break;
/*     */           }
/*     */         }
/*     */       }
/*     */ 
/* 219 */       if (ClientCommunicatorAdmin.logger.traceOn())
/* 220 */         ClientCommunicatorAdmin.logger.trace("Checker-run", "Finished.");
/*     */     }
/*     */ 
/*     */     private void stop()
/*     */     {
/* 225 */       if ((this.myThread != null) && (this.myThread != Thread.currentThread()))
/* 226 */         this.myThread.interrupt();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.remote.internal.ClientCommunicatorAdmin
 * JD-Core Version:    0.6.2
 */