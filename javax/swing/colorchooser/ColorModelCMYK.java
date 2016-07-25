/*    */ package javax.swing.colorchooser;
/*    */ 
/*    */ final class ColorModelCMYK extends ColorModel
/*    */ {
/*    */   ColorModelCMYK()
/*    */   {
/* 31 */     super("cmyk", new String[] { "Cyan", "Magenta", "Yellow", "Black", "Alpha" });
/*    */   }
/*    */ 
/*    */   void setColor(int paramInt, float[] paramArrayOfFloat)
/*    */   {
/* 36 */     super.setColor(paramInt, paramArrayOfFloat);
/* 37 */     paramArrayOfFloat[4] = paramArrayOfFloat[3];
/* 38 */     RGBtoCMYK(paramArrayOfFloat, paramArrayOfFloat);
/*    */   }
/*    */ 
/*    */   int getColor(float[] paramArrayOfFloat)
/*    */   {
/* 43 */     CMYKtoRGB(paramArrayOfFloat, paramArrayOfFloat);
/* 44 */     paramArrayOfFloat[3] = paramArrayOfFloat[4];
/* 45 */     return super.getColor(paramArrayOfFloat);
/*    */   }
/*    */ 
/*    */   private static float[] CMYKtoRGB(float[] paramArrayOfFloat1, float[] paramArrayOfFloat2)
/*    */   {
/* 58 */     if (paramArrayOfFloat2 == null) {
/* 59 */       paramArrayOfFloat2 = new float[3];
/*    */     }
/* 61 */     paramArrayOfFloat2[0] = (1.0F + paramArrayOfFloat1[0] * paramArrayOfFloat1[3] - paramArrayOfFloat1[3] - paramArrayOfFloat1[0]);
/* 62 */     paramArrayOfFloat2[1] = (1.0F + paramArrayOfFloat1[1] * paramArrayOfFloat1[3] - paramArrayOfFloat1[3] - paramArrayOfFloat1[1]);
/* 63 */     paramArrayOfFloat2[2] = (1.0F + paramArrayOfFloat1[2] * paramArrayOfFloat1[3] - paramArrayOfFloat1[3] - paramArrayOfFloat1[2]);
/* 64 */     return paramArrayOfFloat2;
/*    */   }
/*    */ 
/*    */   private static float[] RGBtoCMYK(float[] paramArrayOfFloat1, float[] paramArrayOfFloat2)
/*    */   {
/* 77 */     if (paramArrayOfFloat2 == null) {
/* 78 */       paramArrayOfFloat2 = new float[4];
/*    */     }
/* 80 */     float f = ColorModelHSL.max(paramArrayOfFloat1[0], paramArrayOfFloat1[1], paramArrayOfFloat1[2]);
/* 81 */     if (f > 0.0F) {
/* 82 */       paramArrayOfFloat2[0] = (1.0F - paramArrayOfFloat1[0] / f);
/* 83 */       paramArrayOfFloat2[1] = (1.0F - paramArrayOfFloat1[1] / f);
/* 84 */       paramArrayOfFloat2[2] = (1.0F - paramArrayOfFloat1[2] / f);
/*    */     }
/*    */     else {
/* 87 */       paramArrayOfFloat2[0] = 0.0F;
/* 88 */       paramArrayOfFloat2[1] = 0.0F;
/* 89 */       paramArrayOfFloat2[2] = 0.0F;
/*    */     }
/* 91 */     paramArrayOfFloat2[3] = (1.0F - f);
/* 92 */     return paramArrayOfFloat2;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.colorchooser.ColorModelCMYK
 * JD-Core Version:    0.6.2
 */