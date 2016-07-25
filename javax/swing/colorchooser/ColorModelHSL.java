/*     */ package javax.swing.colorchooser;
/*     */ 
/*     */ final class ColorModelHSL extends ColorModel
/*     */ {
/*     */   ColorModelHSL()
/*     */   {
/*  31 */     super("hsl", new String[] { "Hue", "Saturation", "Lightness", "Transparency" });
/*     */   }
/*     */ 
/*     */   void setColor(int paramInt, float[] paramArrayOfFloat)
/*     */   {
/*  36 */     super.setColor(paramInt, paramArrayOfFloat);
/*  37 */     RGBtoHSL(paramArrayOfFloat, paramArrayOfFloat);
/*  38 */     paramArrayOfFloat[3] = (1.0F - paramArrayOfFloat[3]);
/*     */   }
/*     */ 
/*     */   int getColor(float[] paramArrayOfFloat)
/*     */   {
/*  43 */     paramArrayOfFloat[3] = (1.0F - paramArrayOfFloat[3]);
/*  44 */     HSLtoRGB(paramArrayOfFloat, paramArrayOfFloat);
/*  45 */     return super.getColor(paramArrayOfFloat);
/*     */   }
/*     */ 
/*     */   int getMaximum(int paramInt)
/*     */   {
/*  50 */     return paramInt == 0 ? 360 : 100;
/*     */   }
/*     */ 
/*     */   float getDefault(int paramInt)
/*     */   {
/*  55 */     return paramInt == 2 ? 0.5F : paramInt == 0 ? -1.0F : 1.0F;
/*     */   }
/*     */ 
/*     */   private static float[] HSLtoRGB(float[] paramArrayOfFloat1, float[] paramArrayOfFloat2)
/*     */   {
/*  68 */     if (paramArrayOfFloat2 == null) {
/*  69 */       paramArrayOfFloat2 = new float[3];
/*     */     }
/*  71 */     float f1 = paramArrayOfFloat1[0];
/*  72 */     float f2 = paramArrayOfFloat1[1];
/*  73 */     float f3 = paramArrayOfFloat1[2];
/*     */ 
/*  75 */     if (f2 > 0.0F) {
/*  76 */       f1 = f1 < 1.0F ? f1 * 6.0F : 0.0F;
/*  77 */       float f4 = f3 + f2 * (f3 > 0.5F ? 1.0F - f3 : f3);
/*  78 */       float f5 = 2.0F * f3 - f4;
/*  79 */       paramArrayOfFloat2[0] = normalize(f4, f5, f1 < 4.0F ? f1 + 2.0F : f1 - 4.0F);
/*  80 */       paramArrayOfFloat2[1] = normalize(f4, f5, f1);
/*  81 */       paramArrayOfFloat2[2] = normalize(f4, f5, f1 < 2.0F ? f1 + 4.0F : f1 - 2.0F);
/*     */     }
/*     */     else {
/*  84 */       paramArrayOfFloat2[0] = f3;
/*  85 */       paramArrayOfFloat2[1] = f3;
/*  86 */       paramArrayOfFloat2[2] = f3;
/*     */     }
/*  88 */     return paramArrayOfFloat2;
/*     */   }
/*     */ 
/*     */   private static float[] RGBtoHSL(float[] paramArrayOfFloat1, float[] paramArrayOfFloat2)
/*     */   {
/* 101 */     if (paramArrayOfFloat2 == null) {
/* 102 */       paramArrayOfFloat2 = new float[3];
/*     */     }
/* 104 */     float f1 = max(paramArrayOfFloat1[0], paramArrayOfFloat1[1], paramArrayOfFloat1[2]);
/* 105 */     float f2 = min(paramArrayOfFloat1[0], paramArrayOfFloat1[1], paramArrayOfFloat1[2]);
/*     */ 
/* 107 */     float f3 = f1 + f2;
/* 108 */     float f4 = f1 - f2;
/* 109 */     if (f4 > 0.0F) {
/* 110 */       f4 /= (f3 > 1.0F ? 2.0F - f3 : f3);
/*     */     }
/*     */ 
/* 114 */     paramArrayOfFloat2[0] = getHue(paramArrayOfFloat1[0], paramArrayOfFloat1[1], paramArrayOfFloat1[2], f1, f2);
/* 115 */     paramArrayOfFloat2[1] = f4;
/* 116 */     paramArrayOfFloat2[2] = (f3 / 2.0F);
/* 117 */     return paramArrayOfFloat2;
/*     */   }
/*     */ 
/*     */   static float min(float paramFloat1, float paramFloat2, float paramFloat3)
/*     */   {
/* 129 */     float f = paramFloat1 < paramFloat2 ? paramFloat1 : paramFloat2;
/* 130 */     return f < paramFloat3 ? f : paramFloat3;
/*     */   }
/*     */ 
/*     */   static float max(float paramFloat1, float paramFloat2, float paramFloat3)
/*     */   {
/* 142 */     float f = paramFloat1 > paramFloat2 ? paramFloat1 : paramFloat2;
/* 143 */     return f > paramFloat3 ? f : paramFloat3;
/*     */   }
/*     */ 
/*     */   static float getHue(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5)
/*     */   {
/* 157 */     float f = paramFloat4 - paramFloat5;
/* 158 */     if (f > 0.0F) {
/* 159 */       if (paramFloat4 == paramFloat1) {
/* 160 */         f = (paramFloat2 - paramFloat3) / f;
/* 161 */         if (f < 0.0F) {
/* 162 */           f += 6.0F;
/*     */         }
/*     */       }
/* 165 */       else if (paramFloat4 == paramFloat2) {
/* 166 */         f = 2.0F + (paramFloat3 - paramFloat1) / f;
/*     */       }
/*     */       else {
/* 169 */         f = 4.0F + (paramFloat1 - paramFloat2) / f;
/*     */       }
/* 171 */       f /= 6.0F;
/*     */     }
/* 173 */     return f;
/*     */   }
/*     */ 
/*     */   private static float normalize(float paramFloat1, float paramFloat2, float paramFloat3) {
/* 177 */     if (paramFloat3 < 1.0F) {
/* 178 */       return paramFloat2 + (paramFloat1 - paramFloat2) * paramFloat3;
/*     */     }
/* 180 */     if (paramFloat3 < 3.0F) {
/* 181 */       return paramFloat1;
/*     */     }
/* 183 */     if (paramFloat3 < 4.0F) {
/* 184 */       return paramFloat2 + (paramFloat1 - paramFloat2) * (4.0F - paramFloat3);
/*     */     }
/* 186 */     return paramFloat2;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.colorchooser.ColorModelHSL
 * JD-Core Version:    0.6.2
 */