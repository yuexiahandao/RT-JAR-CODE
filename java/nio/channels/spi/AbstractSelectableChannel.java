/*     */ package java.nio.channels.spi;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.channels.ClosedChannelException;
/*     */ import java.nio.channels.IllegalBlockingModeException;
/*     */ import java.nio.channels.SelectableChannel;
/*     */ import java.nio.channels.SelectionKey;
/*     */ import java.nio.channels.Selector;
/*     */ 
/*     */ public abstract class AbstractSelectableChannel extends SelectableChannel
/*     */ {
/*     */   private final SelectorProvider provider;
/*  61 */   private SelectionKey[] keys = null;
/*  62 */   private int keyCount = 0;
/*     */ 
/*  65 */   private final Object keyLock = new Object();
/*     */ 
/*  68 */   private final Object regLock = new Object();
/*     */ 
/*  71 */   boolean blocking = true;
/*     */ 
/*     */   protected AbstractSelectableChannel(SelectorProvider paramSelectorProvider)
/*     */   {
/*  77 */     this.provider = paramSelectorProvider;
/*     */   }
/*     */ 
/*     */   public final SelectorProvider provider()
/*     */   {
/*  86 */     return this.provider;
/*     */   }
/*     */ 
/*     */   private void addKey(SelectionKey paramSelectionKey)
/*     */   {
/*  93 */     assert (Thread.holdsLock(this.keyLock));
/*  94 */     int i = 0;
/*  95 */     if ((this.keys != null) && (this.keyCount < this.keys.length))
/*     */     {
/*  97 */       for (i = 0; (i < this.keys.length) && 
/*  98 */         (this.keys[i] != null); ) { i++; continue;
/*     */ 
/* 100 */         if (this.keys == null) {
/* 101 */           this.keys = new SelectionKey[3];
/*     */         }
/*     */         else {
/* 104 */           int j = this.keys.length * 2;
/* 105 */           SelectionKey[] arrayOfSelectionKey = new SelectionKey[j];
/* 106 */           for (i = 0; i < this.keys.length; i++)
/* 107 */             arrayOfSelectionKey[i] = this.keys[i];
/* 108 */           this.keys = arrayOfSelectionKey;
/* 109 */           i = this.keyCount;
/*     */         } } 
/*     */     }
/* 111 */     this.keys[i] = paramSelectionKey;
/* 112 */     this.keyCount += 1;
/*     */   }
/*     */ 
/*     */   private SelectionKey findKey(Selector paramSelector) {
/* 116 */     synchronized (this.keyLock) {
/* 117 */       if (this.keys == null)
/* 118 */         return null;
/* 119 */       for (int i = 0; i < this.keys.length; i++)
/* 120 */         if ((this.keys[i] != null) && (this.keys[i].selector() == paramSelector))
/* 121 */           return this.keys[i];
/* 122 */       return null;
/*     */     }
/*     */   }
/*     */ 
/*     */   void removeKey(SelectionKey paramSelectionKey) {
/* 127 */     synchronized (this.keyLock) {
/* 128 */       for (int i = 0; i < this.keys.length; i++)
/* 129 */         if (this.keys[i] == paramSelectionKey) {
/* 130 */           this.keys[i] = null;
/* 131 */           this.keyCount -= 1;
/*     */         }
/* 133 */       ((AbstractSelectionKey)paramSelectionKey).invalidate();
/*     */     }
/*     */   }
/*     */ 
/*     */   private boolean haveValidKeys() {
/* 138 */     synchronized (this.keyLock) {
/* 139 */       if (this.keyCount == 0)
/* 140 */         return false;
/* 141 */       for (int i = 0; i < this.keys.length; i++) {
/* 142 */         if ((this.keys[i] != null) && (this.keys[i].isValid()))
/* 143 */           return true;
/*     */       }
/* 145 */       return false;
/*     */     }
/*     */   }
/*     */ 
/*     */   public final boolean isRegistered()
/*     */   {
/* 153 */     synchronized (this.keyLock) {
/* 154 */       return this.keyCount != 0;
/*     */     }
/*     */   }
/*     */ 
/*     */   public final SelectionKey keyFor(Selector paramSelector) {
/* 159 */     return findKey(paramSelector);
/*     */   }
/*     */ 
/*     */   public final SelectionKey register(Selector paramSelector, int paramInt, Object paramObject)
/*     */     throws ClosedChannelException
/*     */   {
/* 192 */     synchronized (this.regLock) {
/* 193 */       if (!isOpen())
/* 194 */         throw new ClosedChannelException();
/* 195 */       if ((paramInt & (validOps() ^ 0xFFFFFFFF)) != 0)
/* 196 */         throw new IllegalArgumentException();
/* 197 */       if (this.blocking)
/* 198 */         throw new IllegalBlockingModeException();
/* 199 */       SelectionKey localSelectionKey = findKey(paramSelector);
/* 200 */       if (localSelectionKey != null) {
/* 201 */         localSelectionKey.interestOps(paramInt);
/* 202 */         localSelectionKey.attach(paramObject);
/*     */       }
/* 204 */       if (localSelectionKey == null)
/*     */       {
/* 206 */         synchronized (this.keyLock) {
/* 207 */           if (!isOpen())
/* 208 */             throw new ClosedChannelException();
/* 209 */           localSelectionKey = ((AbstractSelector)paramSelector).register(this, paramInt, paramObject);
/* 210 */           addKey(localSelectionKey);
/*     */         }
/*     */       }
/* 213 */       return localSelectionKey;
/*     */     }
/*     */   }
/*     */ 
/*     */   protected final void implCloseChannel()
/*     */     throws IOException
/*     */   {
/* 231 */     implCloseSelectableChannel();
/* 232 */     synchronized (this.keyLock) {
/* 233 */       int i = this.keys == null ? 0 : this.keys.length;
/* 234 */       for (int j = 0; j < i; j++) {
/* 235 */         SelectionKey localSelectionKey = this.keys[j];
/* 236 */         if (localSelectionKey != null)
/* 237 */           localSelectionKey.cancel();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   protected abstract void implCloseSelectableChannel()
/*     */     throws IOException;
/*     */ 
/*     */   public final boolean isBlocking()
/*     */   {
/* 261 */     synchronized (this.regLock) {
/* 262 */       return this.blocking;
/*     */     }
/*     */   }
/*     */ 
/*     */   public final Object blockingLock() {
/* 267 */     return this.regLock;
/*     */   }
/*     */ 
/*     */   public final SelectableChannel configureBlocking(boolean paramBoolean)
/*     */     throws IOException
/*     */   {
/* 281 */     synchronized (this.regLock) {
/* 282 */       if (!isOpen())
/* 283 */         throw new ClosedChannelException();
/* 284 */       if (this.blocking == paramBoolean)
/* 285 */         return this;
/* 286 */       if ((paramBoolean) && (haveValidKeys()))
/* 287 */         throw new IllegalBlockingModeException();
/* 288 */       implConfigureBlocking(paramBoolean);
/* 289 */       this.blocking = paramBoolean;
/*     */     }
/* 291 */     return this;
/*     */   }
/*     */ 
/*     */   protected abstract void implConfigureBlocking(boolean paramBoolean)
/*     */     throws IOException;
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.nio.channels.spi.AbstractSelectableChannel
 * JD-Core Version:    0.6.2
 */