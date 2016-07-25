/*     */ package sun.font;
/*     */ 
/*     */ import java.awt.GraphicsConfiguration;
/*     */ import java.awt.GraphicsDevice;
/*     */ import java.awt.GraphicsEnvironment;
/*     */ import java.lang.ref.Reference;
/*     */ import java.lang.ref.ReferenceQueue;
/*     */ import java.lang.ref.SoftReference;
/*     */ import java.lang.ref.WeakReference;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.ArrayList;
/*     */ import sun.java2d.Disposer;
/*     */ import sun.java2d.pipe.BufferedContext;
/*     */ import sun.java2d.pipe.RenderQueue;
/*     */ import sun.java2d.pipe.hw.AccelGraphicsConfig;
/*     */ import sun.misc.Unsafe;
/*     */ 
/*     */ public final class StrikeCache
/*     */ {
/*  66 */   static final Unsafe unsafe = Unsafe.getUnsafe();
/*     */ 
/*  68 */   static ReferenceQueue refQueue = Disposer.getQueue();
/*     */ 
/*  70 */   static ArrayList<GlyphDisposedListener> disposeListeners = new ArrayList(1);
/*     */ 
/*  95 */   static int MINSTRIKES = 8;
/*  96 */   static int recentStrikeIndex = 0;
/*     */   static FontStrike[] recentStrikes;
/*     */   static boolean cacheRefTypeWeak;
/*     */   static int nativeAddressSize;
/*     */   static int glyphInfoSize;
/*     */   static int xAdvanceOffset;
/*     */   static int yAdvanceOffset;
/*     */   static int boundsOffset;
/*     */   static int widthOffset;
/*     */   static int heightOffset;
/*     */   static int rowBytesOffset;
/*     */   static int topLeftXOffset;
/*     */   static int topLeftYOffset;
/*     */   static int pixelDataOffset;
/*     */   static int cacheCellOffset;
/*     */   static int managedOffset;
/*     */   static long invisibleGlyphPtr;
/*     */ 
/*     */   static native void getGlyphCacheDescription(long[] paramArrayOfLong);
/*     */ 
/*     */   static void refStrike(FontStrike paramFontStrike)
/*     */   {
/* 196 */     int i = recentStrikeIndex;
/* 197 */     recentStrikes[i] = paramFontStrike;
/* 198 */     i++;
/* 199 */     if (i == MINSTRIKES) {
/* 200 */       i = 0;
/*     */     }
/* 202 */     recentStrikeIndex = i;
/*     */   }
/*     */ 
/*     */   private static final void doDispose(FontStrikeDisposer paramFontStrikeDisposer) {
/* 206 */     if (paramFontStrikeDisposer.intGlyphImages != null) {
/* 207 */       freeCachedIntMemory(paramFontStrikeDisposer.intGlyphImages, paramFontStrikeDisposer.pScalerContext);
/*     */     }
/* 209 */     else if (paramFontStrikeDisposer.longGlyphImages != null) {
/* 210 */       freeCachedLongMemory(paramFontStrikeDisposer.longGlyphImages, paramFontStrikeDisposer.pScalerContext);
/*     */     }
/*     */     else
/*     */     {
/*     */       int i;
/* 212 */       if (paramFontStrikeDisposer.segIntGlyphImages != null)
/*     */       {
/* 217 */         for (i = 0; i < paramFontStrikeDisposer.segIntGlyphImages.length; i++) {
/* 218 */           if (paramFontStrikeDisposer.segIntGlyphImages[i] != null) {
/* 219 */             freeCachedIntMemory(paramFontStrikeDisposer.segIntGlyphImages[i], paramFontStrikeDisposer.pScalerContext);
/*     */ 
/* 222 */             paramFontStrikeDisposer.pScalerContext = 0L;
/* 223 */             paramFontStrikeDisposer.segIntGlyphImages[i] = null;
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/* 229 */         if (paramFontStrikeDisposer.pScalerContext != 0L)
/* 230 */           freeCachedIntMemory(new int[0], paramFontStrikeDisposer.pScalerContext);
/*     */       }
/* 232 */       else if (paramFontStrikeDisposer.segLongGlyphImages != null) {
/* 233 */         for (i = 0; i < paramFontStrikeDisposer.segLongGlyphImages.length; i++) {
/* 234 */           if (paramFontStrikeDisposer.segLongGlyphImages[i] != null) {
/* 235 */             freeCachedLongMemory(paramFontStrikeDisposer.segLongGlyphImages[i], paramFontStrikeDisposer.pScalerContext);
/*     */ 
/* 237 */             paramFontStrikeDisposer.pScalerContext = 0L;
/* 238 */             paramFontStrikeDisposer.segLongGlyphImages[i] = null;
/*     */           }
/*     */         }
/* 241 */         if (paramFontStrikeDisposer.pScalerContext != 0L)
/* 242 */           freeCachedLongMemory(new long[0], paramFontStrikeDisposer.pScalerContext);
/*     */       }
/* 244 */       else if (paramFontStrikeDisposer.pScalerContext != 0L)
/*     */       {
/* 249 */         if (longAddresses())
/* 250 */           freeCachedLongMemory(new long[0], paramFontStrikeDisposer.pScalerContext);
/*     */         else
/* 252 */           freeCachedIntMemory(new int[0], paramFontStrikeDisposer.pScalerContext);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private static boolean longAddresses() {
/* 258 */     return nativeAddressSize == 8;
/*     */   }
/*     */ 
/*     */   static void disposeStrike(FontStrikeDisposer paramFontStrikeDisposer)
/*     */   {
/* 275 */     if (Disposer.pollingQueue) {
/* 276 */       doDispose(paramFontStrikeDisposer);
/* 277 */       return;
/*     */     }
/*     */ 
/* 280 */     RenderQueue localRenderQueue = null;
/* 281 */     GraphicsEnvironment localGraphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
/*     */ 
/* 283 */     if (!GraphicsEnvironment.isHeadless()) {
/* 284 */       GraphicsConfiguration localGraphicsConfiguration = localGraphicsEnvironment.getDefaultScreenDevice().getDefaultConfiguration();
/*     */ 
/* 286 */       if ((localGraphicsConfiguration instanceof AccelGraphicsConfig)) {
/* 287 */         AccelGraphicsConfig localAccelGraphicsConfig = (AccelGraphicsConfig)localGraphicsConfiguration;
/* 288 */         BufferedContext localBufferedContext = localAccelGraphicsConfig.getContext();
/* 289 */         if (localBufferedContext != null) {
/* 290 */           localRenderQueue = localBufferedContext.getRenderQueue();
/*     */         }
/*     */       }
/*     */     }
/* 294 */     if (localRenderQueue != null) {
/* 295 */       localRenderQueue.lock();
/*     */       try {
/* 297 */         localRenderQueue.flushAndInvokeNow(new Runnable() {
/*     */           public void run() {
/* 299 */             StrikeCache.doDispose(this.val$disposer);
/* 300 */             Disposer.pollRemove();
/*     */           } } );
/*     */       }
/*     */       finally {
/* 304 */         localRenderQueue.unlock();
/*     */       }
/*     */     } else {
/* 307 */       doDispose(paramFontStrikeDisposer); } 
/*     */   }
/*     */   static native void freeIntPointer(int paramInt);
/*     */ 
/*     */   static native void freeLongPointer(long paramLong);
/*     */ 
/*     */   private static native void freeIntMemory(int[] paramArrayOfInt, long paramLong);
/*     */ 
/*     */   private static native void freeLongMemory(long[] paramArrayOfLong, long paramLong);
/*     */ 
/* 317 */   private static void freeCachedIntMemory(int[] paramArrayOfInt, long paramLong) { synchronized (disposeListeners) {
/* 318 */       if (disposeListeners.size() > 0) {
/* 319 */         ArrayList localArrayList = null;
/*     */ 
/* 321 */         for (int i = 0; i < paramArrayOfInt.length; i++) {
/* 322 */           if ((paramArrayOfInt[i] != 0) && (unsafe.getByte(paramArrayOfInt[i] + managedOffset) == 0))
/*     */           {
/* 324 */             if (localArrayList == null) {
/* 325 */               localArrayList = new ArrayList();
/*     */             }
/* 327 */             localArrayList.add(Long.valueOf(paramArrayOfInt[i]));
/*     */           }
/*     */         }
/*     */ 
/* 331 */         if (localArrayList != null)
/*     */         {
/* 334 */           notifyDisposeListeners(localArrayList);
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 339 */     freeIntMemory(paramArrayOfInt, paramLong); }
/*     */ 
/*     */   private static void freeCachedLongMemory(long[] paramArrayOfLong, long paramLong)
/*     */   {
/* 343 */     synchronized (disposeListeners) {
/* 344 */       if (disposeListeners.size() > 0) {
/* 345 */         ArrayList localArrayList = null;
/*     */ 
/* 347 */         for (int i = 0; i < paramArrayOfLong.length; i++) {
/* 348 */           if ((paramArrayOfLong[i] != 0L) && (unsafe.getByte(paramArrayOfLong[i] + managedOffset) == 0))
/*     */           {
/* 351 */             if (localArrayList == null) {
/* 352 */               localArrayList = new ArrayList();
/*     */             }
/* 354 */             localArrayList.add(Long.valueOf(paramArrayOfLong[i]));
/*     */           }
/*     */         }
/*     */ 
/* 358 */         if (localArrayList != null)
/*     */         {
/* 361 */           notifyDisposeListeners(localArrayList);
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 366 */     freeLongMemory(paramArrayOfLong, paramLong);
/*     */   }
/*     */ 
/*     */   public static void addGlyphDisposedListener(GlyphDisposedListener paramGlyphDisposedListener) {
/* 370 */     synchronized (disposeListeners) {
/* 371 */       disposeListeners.add(paramGlyphDisposedListener);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static void notifyDisposeListeners(ArrayList<Long> paramArrayList) {
/* 376 */     for (GlyphDisposedListener localGlyphDisposedListener : disposeListeners)
/* 377 */       localGlyphDisposedListener.glyphDisposed(paramArrayList);
/*     */   }
/*     */ 
/*     */   public static Reference getStrikeRef(FontStrike paramFontStrike)
/*     */   {
/* 382 */     return getStrikeRef(paramFontStrike, cacheRefTypeWeak);
/*     */   }
/*     */ 
/*     */   public static Reference getStrikeRef(FontStrike paramFontStrike, boolean paramBoolean)
/*     */   {
/* 393 */     if (paramFontStrike.disposer == null) {
/* 394 */       if (paramBoolean) {
/* 395 */         return new WeakReference(paramFontStrike);
/*     */       }
/* 397 */       return new SoftReference(paramFontStrike);
/*     */     }
/*     */ 
/* 401 */     if (paramBoolean) {
/* 402 */       return new WeakDisposerRef(paramFontStrike);
/*     */     }
/* 404 */     return new SoftDisposerRef(paramFontStrike);
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/* 138 */     long[] arrayOfLong = new long[13];
/* 139 */     getGlyphCacheDescription(arrayOfLong);
/*     */ 
/* 142 */     nativeAddressSize = (int)arrayOfLong[0];
/* 143 */     glyphInfoSize = (int)arrayOfLong[1];
/* 144 */     xAdvanceOffset = (int)arrayOfLong[2];
/* 145 */     yAdvanceOffset = (int)arrayOfLong[3];
/* 146 */     widthOffset = (int)arrayOfLong[4];
/* 147 */     heightOffset = (int)arrayOfLong[5];
/* 148 */     rowBytesOffset = (int)arrayOfLong[6];
/* 149 */     topLeftXOffset = (int)arrayOfLong[7];
/* 150 */     topLeftYOffset = (int)arrayOfLong[8];
/* 151 */     pixelDataOffset = (int)arrayOfLong[9];
/* 152 */     invisibleGlyphPtr = arrayOfLong[10];
/* 153 */     cacheCellOffset = (int)arrayOfLong[11];
/* 154 */     managedOffset = (int)arrayOfLong[12];
/*     */ 
/* 156 */     if (nativeAddressSize < 4) {
/* 157 */       throw new InternalError("Unexpected address size for font data: " + nativeAddressSize);
/*     */     }
/*     */ 
/* 161 */     AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public Object run()
/*     */       {
/* 171 */         String str1 = System.getProperty("sun.java2d.font.reftype", "soft");
/*     */ 
/* 173 */         StrikeCache.cacheRefTypeWeak = str1.equals("weak");
/*     */ 
/* 175 */         String str2 = System.getProperty("sun.java2d.font.minstrikes");
/*     */ 
/* 177 */         if (str2 != null) {
/*     */           try {
/* 179 */             StrikeCache.MINSTRIKES = Integer.parseInt(str2);
/* 180 */             if (StrikeCache.MINSTRIKES <= 0)
/* 181 */               StrikeCache.MINSTRIKES = 1;
/*     */           }
/*     */           catch (NumberFormatException localNumberFormatException)
/*     */           {
/*     */           }
/*     */         }
/* 187 */         StrikeCache.recentStrikes = new FontStrike[StrikeCache.MINSTRIKES];
/*     */ 
/* 189 */         return null;
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   static abstract interface DisposableStrike
/*     */   {
/*     */     public abstract FontStrikeDisposer getDisposer();
/*     */   }
/*     */ 
/*     */   static class SoftDisposerRef extends SoftReference
/*     */     implements StrikeCache.DisposableStrike
/*     */   {
/*     */     private FontStrikeDisposer disposer;
/*     */ 
/*     */     public FontStrikeDisposer getDisposer()
/*     */     {
/* 418 */       return this.disposer;
/*     */     }
/*     */ 
/*     */     SoftDisposerRef(FontStrike paramFontStrike) {
/* 422 */       super(StrikeCache.refQueue);
/* 423 */       this.disposer = paramFontStrike.disposer;
/* 424 */       Disposer.addReference(this, this.disposer);
/*     */     }
/*     */   }
/*     */ 
/*     */   static class WeakDisposerRef extends WeakReference implements StrikeCache.DisposableStrike
/*     */   {
/*     */     private FontStrikeDisposer disposer;
/*     */ 
/*     */     public FontStrikeDisposer getDisposer()
/*     */     {
/* 434 */       return this.disposer;
/*     */     }
/*     */ 
/*     */     WeakDisposerRef(FontStrike paramFontStrike) {
/* 438 */       super(StrikeCache.refQueue);
/* 439 */       this.disposer = paramFontStrike.disposer;
/* 440 */       Disposer.addReference(this, this.disposer);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.font.StrikeCache
 * JD-Core Version:    0.6.2
 */