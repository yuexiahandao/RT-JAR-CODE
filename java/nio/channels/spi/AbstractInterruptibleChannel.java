/*     */ package java.nio.channels.spi;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.channels.AsynchronousCloseException;
/*     */ import java.nio.channels.Channel;
/*     */ import java.nio.channels.ClosedByInterruptException;
/*     */ import java.nio.channels.InterruptibleChannel;
/*     */ import sun.misc.JavaLangAccess;
/*     */ import sun.misc.SharedSecrets;
/*     */ import sun.nio.ch.Interruptible;
/*     */ 
/*     */ public abstract class AbstractInterruptibleChannel
/*     */   implements Channel, InterruptibleChannel
/*     */ {
/*  91 */   private final Object closeLock = new Object();
/*  92 */   private volatile boolean open = true;
/*     */   private Interruptible interruptor;
/*     */   private volatile Thread interrupted;
/*     */ 
/*     */   public final void close()
/*     */     throws IOException
/*     */   {
/* 111 */     synchronized (this.closeLock) {
/* 112 */       if (!this.open)
/* 113 */         return;
/* 114 */       this.open = false;
/* 115 */       implCloseChannel();
/*     */     }
/*     */   }
/*     */ 
/*     */   protected abstract void implCloseChannel()
/*     */     throws IOException;
/*     */ 
/*     */   public final boolean isOpen()
/*     */   {
/* 138 */     return this.open;
/*     */   }
/*     */ 
/*     */   protected final void begin()
/*     */   {
/* 156 */     if (this.interruptor == null)
/* 157 */       this.interruptor = new Interruptible() {
/*     */         public void interrupt(Thread paramAnonymousThread) {
/* 159 */           synchronized (AbstractInterruptibleChannel.this.closeLock) {
/* 160 */             if (!AbstractInterruptibleChannel.this.open)
/* 161 */               return;
/* 162 */             AbstractInterruptibleChannel.this.open = false;
/* 163 */             AbstractInterruptibleChannel.this.interrupted = paramAnonymousThread;
/*     */             try {
/* 165 */               AbstractInterruptibleChannel.this.implCloseChannel();
/*     */             } catch (IOException localIOException) {
/*     */             }
/*     */           }
/*     */         } } ;
/* 170 */     blockedOn(this.interruptor);
/* 171 */     Thread localThread = Thread.currentThread();
/* 172 */     if (localThread.isInterrupted())
/* 173 */       this.interruptor.interrupt(localThread);
/*     */   }
/*     */ 
/*     */   protected final void end(boolean paramBoolean)
/*     */     throws AsynchronousCloseException
/*     */   {
/* 198 */     blockedOn(null);
/* 199 */     Thread localThread = this.interrupted;
/* 200 */     if ((localThread != null) && (localThread == Thread.currentThread())) {
/* 201 */       localThread = null;
/* 202 */       throw new ClosedByInterruptException();
/*     */     }
/* 204 */     if ((!paramBoolean) && (!this.open))
/* 205 */       throw new AsynchronousCloseException();
/*     */   }
/*     */ 
/*     */   static void blockedOn(Interruptible paramInterruptible)
/*     */   {
/* 211 */     SharedSecrets.getJavaLangAccess().blockedOn(Thread.currentThread(), paramInterruptible);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.nio.channels.spi.AbstractInterruptibleChannel
 * JD-Core Version:    0.6.2
 */