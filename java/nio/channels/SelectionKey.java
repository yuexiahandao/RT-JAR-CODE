/*     */ package java.nio.channels;
/*     */ 
/*     */ import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
/*     */ 
/*     */ public abstract class SelectionKey
/*     */ {
/*     */   public static final int OP_READ = 1;
/*     */   public static final int OP_WRITE = 4;
/*     */   public static final int OP_CONNECT = 8;
/*     */   public static final int OP_ACCEPT = 16;
/* 366 */   private volatile Object attachment = null;
/*     */ 
/* 369 */   private static final AtomicReferenceFieldUpdater<SelectionKey, Object> attachmentUpdater = AtomicReferenceFieldUpdater.newUpdater(SelectionKey.class, Object.class, "attachment");
/*     */ 
/*     */   public abstract SelectableChannel channel();
/*     */ 
/*     */   public abstract Selector selector();
/*     */ 
/*     */   public abstract boolean isValid();
/*     */ 
/*     */   public abstract void cancel();
/*     */ 
/*     */   public abstract int interestOps();
/*     */ 
/*     */   public abstract SelectionKey interestOps(int paramInt);
/*     */ 
/*     */   public abstract int readyOps();
/*     */ 
/*     */   public final boolean isReadable()
/*     */   {
/* 289 */     return (readyOps() & 0x1) != 0;
/*     */   }
/*     */ 
/*     */   public final boolean isWritable()
/*     */   {
/* 312 */     return (readyOps() & 0x4) != 0;
/*     */   }
/*     */ 
/*     */   public final boolean isConnectable()
/*     */   {
/* 336 */     return (readyOps() & 0x8) != 0;
/*     */   }
/*     */ 
/*     */   public final boolean isAcceptable()
/*     */   {
/* 360 */     return (readyOps() & 0x10) != 0;
/*     */   }
/*     */ 
/*     */   public final Object attach(Object paramObject)
/*     */   {
/* 388 */     return attachmentUpdater.getAndSet(this, paramObject);
/*     */   }
/*     */ 
/*     */   public final Object attachment()
/*     */   {
/* 398 */     return this.attachment;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.nio.channels.SelectionKey
 * JD-Core Version:    0.6.2
 */