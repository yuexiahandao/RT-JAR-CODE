/*     */ package javax.swing.plaf.nimbus;
/*     */ 
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.lang.ref.SoftReference;
/*     */ import sun.awt.AppContext;
/*     */ 
/*     */ abstract class Effect
/*     */ {
/*     */   abstract EffectType getEffectType();
/*     */ 
/*     */   abstract float getOpacity();
/*     */ 
/*     */   abstract BufferedImage applyEffect(BufferedImage paramBufferedImage1, BufferedImage paramBufferedImage2, int paramInt1, int paramInt2);
/*     */ 
/*     */   protected static ArrayCache getArrayCache()
/*     */   {
/*  79 */     ArrayCache localArrayCache = (ArrayCache)AppContext.getAppContext().get(ArrayCache.class);
/*  80 */     if (localArrayCache == null) {
/*  81 */       localArrayCache = new ArrayCache();
/*  82 */       AppContext.getAppContext().put(ArrayCache.class, localArrayCache);
/*     */     }
/*  84 */     return localArrayCache;
/*     */   }
/*     */ 
/*     */   protected static class ArrayCache {
/*  88 */     private SoftReference<int[]> tmpIntArray = null;
/*  89 */     private SoftReference<byte[]> tmpByteArray1 = null;
/*  90 */     private SoftReference<byte[]> tmpByteArray2 = null;
/*  91 */     private SoftReference<byte[]> tmpByteArray3 = null;
/*     */ 
/*     */     protected int[] getTmpIntArray(int paramInt)
/*     */     {
/*     */       int[] arrayOfInt;
/*  95 */       if ((this.tmpIntArray == null) || ((arrayOfInt = (int[])this.tmpIntArray.get()) == null) || (arrayOfInt.length < paramInt))
/*     */       {
/*  97 */         arrayOfInt = new int[paramInt];
/*  98 */         this.tmpIntArray = new SoftReference(arrayOfInt);
/*     */       }
/* 100 */       return arrayOfInt;
/*     */     }
/*     */ 
/*     */     protected byte[] getTmpByteArray1(int paramInt)
/*     */     {
/*     */       byte[] arrayOfByte;
/* 105 */       if ((this.tmpByteArray1 == null) || ((arrayOfByte = (byte[])this.tmpByteArray1.get()) == null) || (arrayOfByte.length < paramInt))
/*     */       {
/* 107 */         arrayOfByte = new byte[paramInt];
/* 108 */         this.tmpByteArray1 = new SoftReference(arrayOfByte);
/*     */       }
/* 110 */       return arrayOfByte;
/*     */     }
/*     */ 
/*     */     protected byte[] getTmpByteArray2(int paramInt)
/*     */     {
/*     */       byte[] arrayOfByte;
/* 115 */       if ((this.tmpByteArray2 == null) || ((arrayOfByte = (byte[])this.tmpByteArray2.get()) == null) || (arrayOfByte.length < paramInt))
/*     */       {
/* 117 */         arrayOfByte = new byte[paramInt];
/* 118 */         this.tmpByteArray2 = new SoftReference(arrayOfByte);
/*     */       }
/* 120 */       return arrayOfByte;
/*     */     }
/*     */ 
/*     */     protected byte[] getTmpByteArray3(int paramInt)
/*     */     {
/*     */       byte[] arrayOfByte;
/* 125 */       if ((this.tmpByteArray3 == null) || ((arrayOfByte = (byte[])this.tmpByteArray3.get()) == null) || (arrayOfByte.length < paramInt))
/*     */       {
/* 127 */         arrayOfByte = new byte[paramInt];
/* 128 */         this.tmpByteArray3 = new SoftReference(arrayOfByte);
/*     */       }
/* 130 */       return arrayOfByte;
/*     */     }
/*     */   }
/*     */ 
/*     */   static enum EffectType
/*     */   {
/*  39 */     UNDER, BLENDED, OVER;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.nimbus.Effect
 * JD-Core Version:    0.6.2
 */