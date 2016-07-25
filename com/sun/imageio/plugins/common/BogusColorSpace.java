/*     */ package com.sun.imageio.plugins.common;
/*     */ 
/*     */ import java.awt.color.ColorSpace;
/*     */ 
/*     */ public class BogusColorSpace extends ColorSpace
/*     */ {
/*     */   private static int getType(int paramInt)
/*     */   {
/*  44 */     if (paramInt < 1)
/*  45 */       throw new IllegalArgumentException("numComponents < 1!");
/*     */     int i;
/*  49 */     switch (paramInt) {
/*     */     case 1:
/*  51 */       i = 6;
/*  52 */       break;
/*     */     default:
/*  57 */       i = paramInt + 10;
/*     */     }
/*     */ 
/*  60 */     return i;
/*     */   }
/*     */ 
/*     */   public BogusColorSpace(int paramInt)
/*     */   {
/*  72 */     super(getType(paramInt), paramInt);
/*     */   }
/*     */ 
/*     */   public float[] toRGB(float[] paramArrayOfFloat)
/*     */   {
/*  82 */     if (paramArrayOfFloat.length < getNumComponents()) {
/*  83 */       throw new ArrayIndexOutOfBoundsException("colorvalue.length < getNumComponents()");
/*     */     }
/*     */ 
/*  87 */     float[] arrayOfFloat = new float[3];
/*     */ 
/*  89 */     System.arraycopy(paramArrayOfFloat, 0, arrayOfFloat, 0, Math.min(3, getNumComponents()));
/*     */ 
/*  92 */     return paramArrayOfFloat;
/*     */   }
/*     */ 
/*     */   public float[] fromRGB(float[] paramArrayOfFloat) {
/*  96 */     if (paramArrayOfFloat.length < 3) {
/*  97 */       throw new ArrayIndexOutOfBoundsException("rgbvalue.length < 3");
/*     */     }
/*     */ 
/* 101 */     float[] arrayOfFloat = new float[getNumComponents()];
/*     */ 
/* 103 */     System.arraycopy(paramArrayOfFloat, 0, arrayOfFloat, 0, Math.min(3, arrayOfFloat.length));
/*     */ 
/* 106 */     return paramArrayOfFloat;
/*     */   }
/*     */ 
/*     */   public float[] toCIEXYZ(float[] paramArrayOfFloat) {
/* 110 */     if (paramArrayOfFloat.length < getNumComponents()) {
/* 111 */       throw new ArrayIndexOutOfBoundsException("colorvalue.length < getNumComponents()");
/*     */     }
/*     */ 
/* 115 */     float[] arrayOfFloat = new float[3];
/*     */ 
/* 117 */     System.arraycopy(paramArrayOfFloat, 0, arrayOfFloat, 0, Math.min(3, getNumComponents()));
/*     */ 
/* 120 */     return paramArrayOfFloat;
/*     */   }
/*     */ 
/*     */   public float[] fromCIEXYZ(float[] paramArrayOfFloat) {
/* 124 */     if (paramArrayOfFloat.length < 3) {
/* 125 */       throw new ArrayIndexOutOfBoundsException("xyzvalue.length < 3");
/*     */     }
/*     */ 
/* 129 */     float[] arrayOfFloat = new float[getNumComponents()];
/*     */ 
/* 131 */     System.arraycopy(paramArrayOfFloat, 0, arrayOfFloat, 0, Math.min(3, arrayOfFloat.length));
/*     */ 
/* 134 */     return paramArrayOfFloat;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.imageio.plugins.common.BogusColorSpace
 * JD-Core Version:    0.6.2
 */