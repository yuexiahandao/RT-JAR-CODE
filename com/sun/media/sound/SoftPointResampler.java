/*    */ package com.sun.media.sound;
/*    */ 
/*    */ public final class SoftPointResampler extends SoftAbstractResampler
/*    */ {
/*    */   public int getPadding()
/*    */   {
/* 35 */     return 100;
/*    */   }
/*    */ 
/*    */   public void interpolate(float[] paramArrayOfFloat1, float[] paramArrayOfFloat2, float paramFloat1, float[] paramArrayOfFloat3, float paramFloat2, float[] paramArrayOfFloat4, int[] paramArrayOfInt, int paramInt)
/*    */   {
/* 41 */     float f1 = paramArrayOfFloat3[0];
/* 42 */     float f2 = paramArrayOfFloat2[0];
/* 43 */     int i = paramArrayOfInt[0];
/* 44 */     float f3 = paramFloat1;
/* 45 */     float f4 = paramInt;
/* 46 */     if (paramFloat2 == 0.0F) {
/* 47 */       while ((f2 < f3) && (i < f4)) {
/* 48 */         paramArrayOfFloat4[(i++)] = paramArrayOfFloat1[((int)f2)];
/* 49 */         f2 += f1;
/*    */       }
/*    */     }
/* 52 */     while ((f2 < f3) && (i < f4)) {
/* 53 */       paramArrayOfFloat4[(i++)] = paramArrayOfFloat1[((int)f2)];
/* 54 */       f2 += f1;
/* 55 */       f1 += paramFloat2;
/*    */     }
/*    */ 
/* 58 */     paramArrayOfFloat2[0] = f2;
/* 59 */     paramArrayOfInt[0] = i;
/* 60 */     paramArrayOfFloat3[0] = f1;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.media.sound.SoftPointResampler
 * JD-Core Version:    0.6.2
 */