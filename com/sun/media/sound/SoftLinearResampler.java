/*    */ package com.sun.media.sound;
/*    */ 
/*    */ public final class SoftLinearResampler extends SoftAbstractResampler
/*    */ {
/*    */   public int getPadding()
/*    */   {
/* 35 */     return 2;
/*    */   }
/*    */ 
/*    */   public void interpolate(float[] paramArrayOfFloat1, float[] paramArrayOfFloat2, float paramFloat1, float[] paramArrayOfFloat3, float paramFloat2, float[] paramArrayOfFloat4, int[] paramArrayOfInt, int paramInt)
/*    */   {
/* 42 */     float f1 = paramArrayOfFloat3[0];
/* 43 */     float f2 = paramArrayOfFloat2[0];
/* 44 */     int i = paramArrayOfInt[0];
/* 45 */     float f3 = paramFloat1;
/* 46 */     int j = paramInt;
/*    */     int k;
/*    */     float f4;
/*    */     float f5;
/* 47 */     if (paramFloat2 == 0.0F) {
/* 48 */       while ((f2 < f3) && (i < j)) {
/* 49 */         k = (int)f2;
/* 50 */         f4 = f2 - k;
/* 51 */         f5 = paramArrayOfFloat1[k];
/* 52 */         paramArrayOfFloat4[(i++)] = (f5 + (paramArrayOfFloat1[(k + 1)] - f5) * f4);
/* 53 */         f2 += f1;
/*    */       }
/*    */     }
/* 56 */     while ((f2 < f3) && (i < j)) {
/* 57 */       k = (int)f2;
/* 58 */       f4 = f2 - k;
/* 59 */       f5 = paramArrayOfFloat1[k];
/* 60 */       paramArrayOfFloat4[(i++)] = (f5 + (paramArrayOfFloat1[(k + 1)] - f5) * f4);
/* 61 */       f2 += f1;
/* 62 */       f1 += paramFloat2;
/*    */     }
/*    */ 
/* 65 */     paramArrayOfFloat2[0] = f2;
/* 66 */     paramArrayOfInt[0] = i;
/* 67 */     paramArrayOfFloat3[0] = f1;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.media.sound.SoftLinearResampler
 * JD-Core Version:    0.6.2
 */