/*    */ package com.sun.media.sound;
/*    */ 
/*    */ public final class SoftCubicResampler extends SoftAbstractResampler
/*    */ {
/*    */   public int getPadding()
/*    */   {
/* 35 */     return 3;
/*    */   }
/*    */ 
/*    */   public void interpolate(float[] paramArrayOfFloat1, float[] paramArrayOfFloat2, float paramFloat1, float[] paramArrayOfFloat3, float paramFloat2, float[] paramArrayOfFloat4, int[] paramArrayOfInt, int paramInt)
/*    */   {
/* 41 */     float f1 = paramArrayOfFloat3[0];
/* 42 */     float f2 = paramArrayOfFloat2[0];
/* 43 */     int i = paramArrayOfInt[0];
/* 44 */     float f3 = paramFloat1;
/* 45 */     int j = paramInt;
/*    */     int k;
/*    */     float f4;
/*    */     float f5;
/*    */     float f6;
/*    */     float f7;
/*    */     float f8;
/*    */     float f9;
/*    */     float f10;
/*    */     float f11;
/*    */     float f12;
/* 46 */     if (paramFloat2 == 0.0F) {
/* 47 */       while ((f2 < f3) && (i < j)) {
/* 48 */         k = (int)f2;
/* 49 */         f4 = f2 - k;
/* 50 */         f5 = paramArrayOfFloat1[(k - 1)];
/* 51 */         f6 = paramArrayOfFloat1[k];
/* 52 */         f7 = paramArrayOfFloat1[(k + 1)];
/* 53 */         f8 = paramArrayOfFloat1[(k + 2)];
/* 54 */         f9 = f8 - f7 + f6 - f5;
/* 55 */         f10 = f5 - f6 - f9;
/* 56 */         f11 = f7 - f5;
/* 57 */         f12 = f6;
/*    */ 
/* 60 */         paramArrayOfFloat4[(i++)] = (((f9 * f4 + f10) * f4 + f11) * f4 + f12);
/* 61 */         f2 += f1;
/*    */       }
/*    */     }
/* 64 */     while ((f2 < f3) && (i < j)) {
/* 65 */       k = (int)f2;
/* 66 */       f4 = f2 - k;
/* 67 */       f5 = paramArrayOfFloat1[(k - 1)];
/* 68 */       f6 = paramArrayOfFloat1[k];
/* 69 */       f7 = paramArrayOfFloat1[(k + 1)];
/* 70 */       f8 = paramArrayOfFloat1[(k + 2)];
/* 71 */       f9 = f8 - f7 + f6 - f5;
/* 72 */       f10 = f5 - f6 - f9;
/* 73 */       f11 = f7 - f5;
/* 74 */       f12 = f6;
/*    */ 
/* 77 */       paramArrayOfFloat4[(i++)] = (((f9 * f4 + f10) * f4 + f11) * f4 + f12);
/* 78 */       f2 += f1;
/* 79 */       f1 += paramFloat2;
/*    */     }
/*    */ 
/* 82 */     paramArrayOfFloat2[0] = f2;
/* 83 */     paramArrayOfInt[0] = i;
/* 84 */     paramArrayOfFloat3[0] = f1;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.media.sound.SoftCubicResampler
 * JD-Core Version:    0.6.2
 */