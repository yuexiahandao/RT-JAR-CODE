/*     */ package sun.nio.ch;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.SocketException;
/*     */ import java.nio.channels.ClosedSelectorException;
/*     */ import java.nio.channels.IllegalSelectorException;
/*     */ import java.nio.channels.SelectionKey;
/*     */ import java.nio.channels.Selector;
/*     */ import java.nio.channels.spi.AbstractSelectableChannel;
/*     */ import java.nio.channels.spi.AbstractSelector;
/*     */ import java.nio.channels.spi.SelectorProvider;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.Set;
/*     */ 
/*     */ abstract class SelectorImpl extends AbstractSelector
/*     */ {
/*     */   protected Set<SelectionKey> selectedKeys;
/*     */   protected HashSet<SelectionKey> keys;
/*     */   private Set<SelectionKey> publicKeys;
/*     */   private Set<SelectionKey> publicSelectedKeys;
/*     */ 
/*     */   protected SelectorImpl(SelectorProvider paramSelectorProvider)
/*     */   {
/*  55 */     super(paramSelectorProvider);
/*  56 */     this.keys = new HashSet();
/*  57 */     this.selectedKeys = new HashSet();
/*  58 */     if (Util.atBugLevel("1.4")) {
/*  59 */       this.publicKeys = this.keys;
/*  60 */       this.publicSelectedKeys = this.selectedKeys;
/*     */     } else {
/*  62 */       this.publicKeys = Collections.unmodifiableSet(this.keys);
/*  63 */       this.publicSelectedKeys = Util.ungrowableSet(this.selectedKeys);
/*     */     }
/*     */   }
/*     */ 
/*     */   public Set<SelectionKey> keys() {
/*  68 */     if ((!isOpen()) && (!Util.atBugLevel("1.4")))
/*  69 */       throw new ClosedSelectorException();
/*  70 */     return this.publicKeys;
/*     */   }
/*     */ 
/*     */   public Set<SelectionKey> selectedKeys() {
/*  74 */     if ((!isOpen()) && (!Util.atBugLevel("1.4")))
/*  75 */       throw new ClosedSelectorException();
/*  76 */     return this.publicSelectedKeys;
/*     */   }
/*     */ 
/*     */   protected abstract int doSelect(long paramLong) throws IOException;
/*     */ 
/*     */   private int lockAndDoSelect(long paramLong) throws IOException {
/*  82 */     synchronized (this) {
/*  83 */       if (!isOpen())
/*  84 */         throw new ClosedSelectorException();
/*  85 */       synchronized (this.publicKeys) {
/*  86 */         synchronized (this.publicSelectedKeys) {
/*     */         }
/*  88 */         localObject1 = finally;
/*  88 */         throw localObject1;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public int select(long paramLong)
/*     */     throws IOException
/*     */   {
/*  96 */     if (paramLong < 0L)
/*  97 */       throw new IllegalArgumentException("Negative timeout");
/*  98 */     return lockAndDoSelect(paramLong == 0L ? -1L : paramLong);
/*     */   }
/*     */ 
/*     */   public int select() throws IOException {
/* 102 */     return select(0L);
/*     */   }
/*     */ 
/*     */   public int selectNow() throws IOException {
/* 106 */     return lockAndDoSelect(0L);
/*     */   }
/*     */ 
/*     */   public void implCloseSelector() throws IOException {
/* 110 */     wakeup();
/* 111 */     synchronized (this) {
/* 112 */       synchronized (this.publicKeys) {
/* 113 */         synchronized (this.publicSelectedKeys) {
/* 114 */           implClose();
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   protected abstract void implClose() throws IOException;
/*     */ 
/*     */   void putEventOps(SelectionKeyImpl paramSelectionKeyImpl, int paramInt)
/*     */   {
/*     */   }
/*     */ 
/*     */   protected final SelectionKey register(AbstractSelectableChannel paramAbstractSelectableChannel, int paramInt, Object paramObject)
/*     */   {
/* 128 */     if (!(paramAbstractSelectableChannel instanceof SelChImpl))
/* 129 */       throw new IllegalSelectorException();
/* 130 */     SelectionKeyImpl localSelectionKeyImpl = new SelectionKeyImpl((SelChImpl)paramAbstractSelectableChannel, this);
/* 131 */     localSelectionKeyImpl.attach(paramObject);
/* 132 */     synchronized (this.publicKeys) {
/* 133 */       implRegister(localSelectionKeyImpl);
/*     */     }
/* 135 */     localSelectionKeyImpl.interestOps(paramInt);
/* 136 */     return localSelectionKeyImpl;
/*     */   }
/*     */ 
/*     */   protected abstract void implRegister(SelectionKeyImpl paramSelectionKeyImpl);
/*     */ 
/*     */   void processDeregisterQueue() throws IOException
/*     */   {
/* 143 */     Set localSet = cancelledKeys();
/* 144 */     synchronized (localSet) {
/* 145 */       if (!localSet.isEmpty()) {
/* 146 */         Iterator localIterator = localSet.iterator();
/* 147 */         while (localIterator.hasNext()) {
/* 148 */           SelectionKeyImpl localSelectionKeyImpl = (SelectionKeyImpl)localIterator.next();
/*     */           try {
/* 150 */             implDereg(localSelectionKeyImpl);
/*     */           } catch (SocketException localSocketException) {
/* 152 */             IOException localIOException = new IOException("Error deregistering key");
/*     */ 
/* 154 */             localIOException.initCause(localSocketException);
/* 155 */             throw localIOException;
/*     */           } finally {
/* 157 */             localIterator.remove();
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   protected abstract void implDereg(SelectionKeyImpl paramSelectionKeyImpl)
/*     */     throws IOException;
/*     */ 
/*     */   public abstract Selector wakeup();
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.nio.ch.SelectorImpl
 * JD-Core Version:    0.6.2
 */