/*     */ package com.sun.imageio.plugins.common;
/*     */ 
/*     */ import java.awt.Point;
/*     */ import java.awt.Rectangle;
/*     */ import java.io.IOException;
/*     */ import javax.imageio.stream.ImageInputStream;
/*     */ 
/*     */ public class ReaderUtil
/*     */ {
/*     */   private static void computeUpdatedPixels(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, int paramInt9, int[] paramArrayOfInt, int paramInt10)
/*     */   {
/*  84 */     int i = 0;
/*  85 */     int j = -1;
/*  86 */     int k = -1;
/*  87 */     int m = -1;
/*     */ 
/*  89 */     for (int n = 0; n < paramInt8; n++) {
/*  90 */       int i1 = paramInt7 + n * paramInt9;
/*  91 */       if (i1 >= paramInt1)
/*     */       {
/*  94 */         if ((i1 - paramInt1) % paramInt6 == 0)
/*     */         {
/*  97 */           if (i1 >= paramInt1 + paramInt2)
/*     */           {
/*     */             break;
/*     */           }
/* 101 */           int i2 = paramInt3 + (i1 - paramInt1) / paramInt6;
/*     */ 
/* 103 */           if (i2 >= paramInt4)
/*     */           {
/* 106 */             if (i2 > paramInt5)
/*     */             {
/*     */               break;
/*     */             }
/* 110 */             if (i == 0) {
/* 111 */               j = i2;
/* 112 */               i = 1;
/* 113 */             } else if (k == -1) {
/* 114 */               k = i2;
/*     */             }
/* 116 */             m = i2;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 119 */     paramArrayOfInt[paramInt10] = j;
/*     */ 
/* 122 */     if (i == 0)
/* 123 */       paramArrayOfInt[(paramInt10 + 2)] = 0;
/*     */     else {
/* 125 */       paramArrayOfInt[(paramInt10 + 2)] = (m - j + 1);
/*     */     }
/*     */ 
/* 129 */     paramArrayOfInt[(paramInt10 + 4)] = Math.max(k - j, 1);
/*     */   }
/*     */ 
/*     */   public static int[] computeUpdatedPixels(Rectangle paramRectangle, Point paramPoint, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, int paramInt9, int paramInt10, int paramInt11, int paramInt12)
/*     */   {
/* 190 */     int[] arrayOfInt = new int[6];
/* 191 */     computeUpdatedPixels(paramRectangle.x, paramRectangle.width, paramPoint.x, paramInt1, paramInt3, paramInt5, paramInt7, paramInt9, paramInt11, arrayOfInt, 0);
/*     */ 
/* 196 */     computeUpdatedPixels(paramRectangle.y, paramRectangle.height, paramPoint.y, paramInt2, paramInt4, paramInt6, paramInt8, paramInt10, paramInt12, arrayOfInt, 1);
/*     */ 
/* 201 */     return arrayOfInt;
/*     */   }
/*     */ 
/*     */   public static int readMultiByteInteger(ImageInputStream paramImageInputStream)
/*     */     throws IOException
/*     */   {
/* 207 */     int i = paramImageInputStream.readByte();
/* 208 */     int j = i & 0x7F;
/* 209 */     while ((i & 0x80) == 128) {
/* 210 */       j <<= 7;
/* 211 */       i = paramImageInputStream.readByte();
/* 212 */       j |= i & 0x7F;
/*     */     }
/* 214 */     return j;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.imageio.plugins.common.ReaderUtil
 * JD-Core Version:    0.6.2
 */