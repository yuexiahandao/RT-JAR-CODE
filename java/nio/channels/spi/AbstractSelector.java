/*     */ package java.nio.channels.spi;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.channels.SelectionKey;
/*     */ import java.nio.channels.Selector;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import sun.nio.ch.Interruptible;
/*     */ 
/*     */ public abstract class AbstractSelector extends Selector
/*     */ {
/*  73 */   private AtomicBoolean selectorOpen = new AtomicBoolean(true);
/*     */   private final SelectorProvider provider;
/*  85 */   private final Set<SelectionKey> cancelledKeys = new HashSet();
/*     */ 
/* 191 */   private Interruptible interruptor = null;
/*     */ 
/*     */   protected AbstractSelector(SelectorProvider paramSelectorProvider)
/*     */   {
/*  82 */     this.provider = paramSelectorProvider;
/*     */   }
/*     */ 
/*     */   void cancel(SelectionKey paramSelectionKey)
/*     */   {
/*  88 */     synchronized (this.cancelledKeys) {
/*  89 */       this.cancelledKeys.add(paramSelectionKey);
/*     */     }
/*     */   }
/*     */ 
/*     */   public final void close()
/*     */     throws IOException
/*     */   {
/* 105 */     boolean bool = this.selectorOpen.getAndSet(false);
/* 106 */     if (!bool)
/* 107 */       return;
/* 108 */     implCloseSelector();
/*     */   }
/*     */ 
/*     */   protected abstract void implCloseSelector()
/*     */     throws IOException;
/*     */ 
/*     */   public final boolean isOpen()
/*     */   {
/* 130 */     return this.selectorOpen.get();
/*     */   }
/*     */ 
/*     */   public final SelectorProvider provider()
/*     */   {
/* 139 */     return this.provider;
/*     */   }
/*     */ 
/*     */   protected final Set<SelectionKey> cancelledKeys()
/*     */   {
/* 150 */     return this.cancelledKeys;
/*     */   }
/*     */ 
/*     */   protected abstract SelectionKey register(AbstractSelectableChannel paramAbstractSelectableChannel, int paramInt, Object paramObject);
/*     */ 
/*     */   protected final void deregister(AbstractSelectionKey paramAbstractSelectionKey)
/*     */   {
/* 185 */     ((AbstractSelectableChannel)paramAbstractSelectionKey.channel()).removeKey(paramAbstractSelectionKey);
/*     */   }
/*     */ 
/*     */   protected final void begin()
/*     */   {
/* 207 */     if (this.interruptor == null)
/* 208 */       this.interruptor = new Interruptible() {
/*     */         public void interrupt(Thread paramAnonymousThread) {
/* 210 */           AbstractSelector.this.wakeup();
/*     */         }
/*     */       };
/* 213 */     AbstractInterruptibleChannel.blockedOn(this.interruptor);
/* 214 */     Thread localThread = Thread.currentThread();
/* 215 */     if (localThread.isInterrupted())
/* 216 */       this.interruptor.interrupt(localThread);
/*     */   }
/*     */ 
/*     */   protected final void end()
/*     */   {
/* 228 */     AbstractInterruptibleChannel.blockedOn(null);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.nio.channels.spi.AbstractSelector
 * JD-Core Version:    0.6.2
 */