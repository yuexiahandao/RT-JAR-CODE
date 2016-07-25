/*     */ package javax.swing.colorchooser;
/*     */ 
/*     */ final class ColorModelHSV extends ColorModel
/*     */ {
/*     */   ColorModelHSV()
/*     */   {
/*  31 */     super("hsv", new String[] { "Hue", "Saturation", "Value", "Transparency" });
/*     */   }
/*     */ 
/*     */   void setColor(int paramInt, float[] paramArrayOfFloat)
/*     */   {
/*  36 */     super.setColor(paramInt, paramArrayOfFloat);
/*  37 */     RGBtoHSV(paramArrayOfFloat, paramArrayOfFloat);
/*  38 */     paramArrayOfFloat[3] = (1.0F - paramArrayOfFloat[3]);
/*     */   }
/*     */ 
/*     */   int getColor(float[] paramArrayOfFloat)
/*     */   {
/*  43 */     paramArrayOfFloat[3] = (1.0F - paramArrayOfFloat[3]);
/*  44 */     HSVtoRGB(paramArrayOfFloat, paramArrayOfFloat);
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
/*  55 */     return paramInt == 0 ? -1.0F : 1.0F;
/*     */   }
/*     */ 
/*     */   private static float[] HSVtoRGB(float[] paramArrayOfFloat1, float[] paramArrayOfFloat2)
/*     */   {
/*  68 */     if (paramArrayOfFloat2 == null) {
/*  69 */       paramArrayOfFloat2 = new float[3];
/*     */     }
/*  71 */     float f1 = paramArrayOfFloat1[0];
/*  72 */     float f2 = paramArrayOfFloat1[1];
/*  73 */     float f3 = paramArrayOfFloat1[2];
/*     */ 
/*  75 */     paramArrayOfFloat2[0] = f3;
/*  76 */     paramArrayOfFloat2[1] = f3;
/*  77 */     paramArrayOfFloat2[2] = f3;
/*     */ 
/*  79 */     if (f2 > 0.0F) {
/*  80 */       f1 = f1 < 1.0F ? f1 * 6.0F : 0.0F;
/*  81 */       int i = (int)f1;
/*  82 */       float f4 = f1 - i;
/*  83 */       switch (i) {
/*     */       case 0:
/*  85 */         paramArrayOfFloat2[1] *= (1.0F - f2 * (1.0F - f4));
/*  86 */         paramArrayOfFloat2[2] *= (1.0F - f2);
/*  87 */         break;
/*     */       case 1:
/*  89 */         paramArrayOfFloat2[0] *= (1.0F - f2 * f4);
/*  90 */         paramArrayOfFloat2[2] *= (1.0F - f2);
/*  91 */         break;
/*     */       case 2:
/*  93 */         paramArrayOfFloat2[0] *= (1.0F - f2);
/*  94 */         paramArrayOfFloat2[2] *= (1.0F - f2 * (1.0F - f4));
/*  95 */         break;
/*     */       case 3:
/*  97 */         paramArrayOfFloat2[0] *= (1.0F - f2);
/*  98 */         paramArrayOfFloat2[1] *= (1.0F - f2 * f4);
/*  99 */         break;
/*     */       case 4:
/* 101 */         paramArrayOfFloat2[0] *= (1.0F - f2 * (1.0F - f4));
/* 102 */         paramArrayOfFloat2[1] *= (1.0F - f2);
/* 103 */         break;
/*     */       case 5:
/* 105 */         paramArrayOfFloat2[1] *= (1.0F - f2);
/* 106 */         paramArrayOfFloat2[2] *= (1.0F - f2 * f4);
/*     */       }
/*     */     }
/*     */ 
/* 110 */     return paramArrayOfFloat2;
/*     */   }
/*     */ 
/*     */   private static float[] RGBtoHSV(float[] paramArrayOfFloat1, float[] paramArrayOfFloat2)
/*     */   {
/* 123 */     if (paramArrayOfFloat2 == null) {
/* 124 */       paramArrayOfFloat2 = new float[3];
/*     */     }
/* 126 */     float f1 = ColorModelHSL.max(paramArrayOfFloat1[0], paramArrayOfFloat1[1], paramArrayOfFloat1[2]);
/* 127 */     float f2 = ColorModelHSL.min(paramArrayOfFloat1[0], paramArrayOfFloat1[1], paramArrayOfFloat1[2]);
/*     */ 
/* 129 */     float f3 = f1 - f2;
/* 130 */     if (f3 > 0.0F) {
/* 131 */       f3 /= f1;
/*     */     }
/* 133 */     paramArrayOfFloat2[0] = ColorModelHSL.getHue(paramArrayOfFloat1[0], paramArrayOfFloat1[1], paramArrayOfFloat1[2], f1, f2);
/* 134 */     paramArrayOfFloat2[1] = f3;
/* 135 */     paramArrayOfFloat2[2] = f1;
/* 136 */     return paramArrayOfFloat2;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.colorchooser.ColorModelHSV
 * JD-Core Version:    0.6.2
 */