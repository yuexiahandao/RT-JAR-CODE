/*     */ package java.nio.channels;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.channels.spi.AbstractInterruptibleChannel;
/*     */ import java.nio.channels.spi.SelectorProvider;
/*     */ 
/*     */ public abstract class SelectableChannel extends AbstractInterruptibleChannel
/*     */   implements Channel
/*     */ {
/*     */   public abstract SelectorProvider provider();
/*     */ 
/*     */   public abstract int validOps();
/*     */ 
/*     */   public abstract boolean isRegistered();
/*     */ 
/*     */   public abstract SelectionKey keyFor(Selector paramSelector);
/*     */ 
/*     */   public abstract SelectionKey register(Selector paramSelector, int paramInt, Object paramObject)
/*     */     throws ClosedChannelException;
/*     */ 
/*     */   public final SelectionKey register(Selector paramSelector, int paramInt)
/*     */     throws ClosedChannelException
/*     */   {
/* 277 */     return register(paramSelector, paramInt, null);
/*     */   }
/*     */ 
/*     */   public abstract SelectableChannel configureBlocking(boolean paramBoolean)
/*     */     throws IOException;
/*     */ 
/*     */   public abstract boolean isBlocking();
/*     */ 
/*     */   public abstract Object blockingLock();
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.nio.channels.SelectableChannel
 * JD-Core Version:    0.6.2
 */