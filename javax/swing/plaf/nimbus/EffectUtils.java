/*     */ package javax.swing.plaf.nimbus;
/*     */ 
/*     */ import java.awt.AlphaComposite;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.GraphicsConfiguration;
/*     */ import java.awt.GraphicsDevice;
/*     */ import java.awt.GraphicsEnvironment;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.awt.image.ColorModel;
/*     */ import java.awt.image.Raster;
/*     */ import java.awt.image.WritableRaster;
/*     */ 
/*     */ class EffectUtils
/*     */ {
/*     */   static void clearImage(BufferedImage paramBufferedImage)
/*     */   {
/*  50 */     Graphics2D localGraphics2D = paramBufferedImage.createGraphics();
/*  51 */     localGraphics2D.setComposite(AlphaComposite.Clear);
/*  52 */     localGraphics2D.fillRect(0, 0, paramBufferedImage.getWidth(), paramBufferedImage.getHeight());
/*  53 */     localGraphics2D.dispose();
/*     */   }
/*     */ 
/*     */   static BufferedImage gaussianBlur(BufferedImage paramBufferedImage1, BufferedImage paramBufferedImage2, int paramInt)
/*     */   {
/*  68 */     int i = paramBufferedImage1.getWidth();
/*  69 */     int j = paramBufferedImage1.getHeight();
/*  70 */     if ((paramBufferedImage2 == null) || (paramBufferedImage2.getWidth() != i) || (paramBufferedImage2.getHeight() != j) || (paramBufferedImage1.getType() != paramBufferedImage2.getType())) {
/*  71 */       paramBufferedImage2 = createColorModelCompatibleImage(paramBufferedImage1);
/*     */     }
/*  73 */     float[] arrayOfFloat = createGaussianKernel(paramInt);
/*     */     Object localObject1;
/*     */     Object localObject2;
/*  74 */     if (paramBufferedImage1.getType() == 2) {
/*  75 */       localObject1 = new int[i * j];
/*  76 */       localObject2 = new int[i * j];
/*  77 */       getPixels(paramBufferedImage1, 0, 0, i, j, (int[])localObject1);
/*     */ 
/*  79 */       blur((int[])localObject1, (int[])localObject2, i, j, arrayOfFloat, paramInt);
/*     */ 
/*  82 */       blur((int[])localObject2, (int[])localObject1, j, i, arrayOfFloat, paramInt);
/*     */ 
/*  84 */       setPixels(paramBufferedImage2, 0, 0, i, j, (int[])localObject1);
/*  85 */     } else if (paramBufferedImage1.getType() == 10) {
/*  86 */       localObject1 = new byte[i * j];
/*  87 */       localObject2 = new byte[i * j];
/*  88 */       getPixels(paramBufferedImage1, 0, 0, i, j, (byte[])localObject1);
/*     */ 
/*  90 */       blur((byte[])localObject1, (byte[])localObject2, i, j, arrayOfFloat, paramInt);
/*     */ 
/*  93 */       blur((byte[])localObject2, (byte[])localObject1, j, i, arrayOfFloat, paramInt);
/*     */ 
/*  95 */       setPixels(paramBufferedImage2, 0, 0, i, j, (byte[])localObject1);
/*     */     } else {
/*  97 */       throw new IllegalArgumentException("EffectUtils.gaussianBlur() src image is not a supported type, type=[" + paramBufferedImage1.getType() + "]");
/*     */     }
/*     */ 
/* 100 */     return paramBufferedImage2;
/*     */   }
/*     */ 
/*     */   private static void blur(int[] paramArrayOfInt1, int[] paramArrayOfInt2, int paramInt1, int paramInt2, float[] paramArrayOfFloat, int paramInt3)
/*     */   {
/* 129 */     for (int n = 0; n < paramInt2; n++) {
/* 130 */       int i1 = n;
/* 131 */       int i2 = n * paramInt1;
/*     */ 
/* 133 */       for (int i3 = 0; i3 < paramInt1; i3++)
/*     */       {
/*     */         float f4;
/*     */         float f3;
/*     */         float f2;
/* 134 */         float f1 = f2 = f3 = f4 = 0.0F;
/*     */ 
/* 136 */         for (int i4 = -paramInt3; i4 <= paramInt3; i4++) {
/* 137 */           int i5 = i3 + i4;
/* 138 */           if ((i5 < 0) || (i5 >= paramInt1)) {
/* 139 */             i5 = (i3 + paramInt1) % paramInt1;
/*     */           }
/*     */ 
/* 142 */           int i6 = paramArrayOfInt1[(i2 + i5)];
/* 143 */           float f5 = paramArrayOfFloat[(paramInt3 + i4)];
/*     */ 
/* 145 */           f1 += f5 * (i6 >> 24 & 0xFF);
/* 146 */           f2 += f5 * (i6 >> 16 & 0xFF);
/* 147 */           f3 += f5 * (i6 >> 8 & 0xFF);
/* 148 */           f4 += f5 * (i6 & 0xFF);
/*     */         }
/*     */ 
/* 151 */         int i = (int)(f1 + 0.5F);
/* 152 */         int j = (int)(f2 + 0.5F);
/* 153 */         int k = (int)(f3 + 0.5F);
/* 154 */         int m = (int)(f4 + 0.5F);
/*     */ 
/* 156 */         paramArrayOfInt2[i1] = ((i > 255 ? 'ÿ' : i) << 24 | (j > 255 ? 'ÿ' : j) << 16 | (k > 255 ? 'ÿ' : k) << 8 | (m > 255 ? 'ÿ' : m));
/*     */ 
/* 160 */         i1 += paramInt2;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   static void blur(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, int paramInt1, int paramInt2, float[] paramArrayOfFloat, int paramInt3)
/*     */   {
/* 183 */     for (int j = 0; j < paramInt2; j++) {
/* 184 */       int k = j;
/* 185 */       int m = j * paramInt1;
/* 186 */       for (int n = 0; n < paramInt1; n++) {
/* 187 */         float f1 = 0.0F;
/* 188 */         for (int i1 = -paramInt3; i1 <= paramInt3; i1++) {
/* 189 */           int i2 = n + i1;
/*     */ 
/* 192 */           if ((i2 < 0) || (i2 >= paramInt1)) {
/* 193 */             i2 = (n + paramInt1) % paramInt1;
/*     */           }
/* 195 */           int i3 = paramArrayOfByte1[(m + i2)] & 0xFF;
/* 196 */           float f2 = paramArrayOfFloat[(paramInt3 + i1)];
/* 197 */           f1 += f2 * i3;
/*     */         }
/* 199 */         int i = (int)(f1 + 0.5F);
/* 200 */         paramArrayOfByte2[k] = ((byte)(i > 255 ? 'ÿ' : i));
/* 201 */         k += paramInt2;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   static float[] createGaussianKernel(int paramInt) {
/* 207 */     if (paramInt < 1) {
/* 208 */       throw new IllegalArgumentException("Radius must be >= 1");
/*     */     }
/*     */ 
/* 211 */     float[] arrayOfFloat = new float[paramInt * 2 + 1];
/*     */ 
/* 213 */     float f1 = paramInt / 3.0F;
/* 214 */     float f2 = 2.0F * f1 * f1;
/* 215 */     float f3 = (float)Math.sqrt(f2 * 3.141592653589793D);
/* 216 */     float f4 = 0.0F;
/*     */ 
/* 218 */     for (int i = -paramInt; i <= paramInt; i++) {
/* 219 */       float f5 = i * i;
/* 220 */       int j = i + paramInt;
/* 221 */       arrayOfFloat[j] = ((float)Math.exp(-f5 / f2) / f3);
/* 222 */       f4 += arrayOfFloat[j];
/*     */     }
/*     */ 
/* 225 */     for (i = 0; i < arrayOfFloat.length; i++) {
/* 226 */       arrayOfFloat[i] /= f4;
/*     */     }
/*     */ 
/* 229 */     return arrayOfFloat;
/*     */   }
/*     */ 
/*     */   static byte[] getPixels(BufferedImage paramBufferedImage, int paramInt1, int paramInt2, int paramInt3, int paramInt4, byte[] paramArrayOfByte)
/*     */   {
/* 252 */     if ((paramInt3 == 0) || (paramInt4 == 0)) {
/* 253 */       return new byte[0];
/*     */     }
/*     */ 
/* 256 */     if (paramArrayOfByte == null)
/* 257 */       paramArrayOfByte = new byte[paramInt3 * paramInt4];
/* 258 */     else if (paramArrayOfByte.length < paramInt3 * paramInt4) {
/* 259 */       throw new IllegalArgumentException("pixels array must have a length >= w*h");
/*     */     }
/*     */ 
/* 262 */     int i = paramBufferedImage.getType();
/* 263 */     if (i == 10) {
/* 264 */       WritableRaster localWritableRaster = paramBufferedImage.getRaster();
/* 265 */       return (byte[])localWritableRaster.getDataElements(paramInt1, paramInt2, paramInt3, paramInt4, paramArrayOfByte);
/*     */     }
/* 267 */     throw new IllegalArgumentException("Only type BYTE_GRAY is supported");
/*     */   }
/*     */ 
/*     */   static void setPixels(BufferedImage paramBufferedImage, int paramInt1, int paramInt2, int paramInt3, int paramInt4, byte[] paramArrayOfByte)
/*     */   {
/* 286 */     if ((paramArrayOfByte == null) || (paramInt3 == 0) || (paramInt4 == 0))
/* 287 */       return;
/* 288 */     if (paramArrayOfByte.length < paramInt3 * paramInt4) {
/* 289 */       throw new IllegalArgumentException("pixels array must have a length >= w*h");
/*     */     }
/* 291 */     int i = paramBufferedImage.getType();
/* 292 */     if (i == 10) {
/* 293 */       WritableRaster localWritableRaster = paramBufferedImage.getRaster();
/* 294 */       localWritableRaster.setDataElements(paramInt1, paramInt2, paramInt3, paramInt4, paramArrayOfByte);
/*     */     } else {
/* 296 */       throw new IllegalArgumentException("Only type BYTE_GRAY is supported");
/*     */     }
/*     */   }
/*     */ 
/*     */   public static int[] getPixels(BufferedImage paramBufferedImage, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int[] paramArrayOfInt)
/*     */   {
/* 320 */     if ((paramInt3 == 0) || (paramInt4 == 0)) {
/* 321 */       return new int[0];
/*     */     }
/*     */ 
/* 324 */     if (paramArrayOfInt == null)
/* 325 */       paramArrayOfInt = new int[paramInt3 * paramInt4];
/* 326 */     else if (paramArrayOfInt.length < paramInt3 * paramInt4) {
/* 327 */       throw new IllegalArgumentException("pixels array must have a length >= w*h");
/*     */     }
/*     */ 
/* 331 */     int i = paramBufferedImage.getType();
/* 332 */     if ((i == 2) || (i == 1))
/*     */     {
/* 334 */       WritableRaster localWritableRaster = paramBufferedImage.getRaster();
/* 335 */       return (int[])localWritableRaster.getDataElements(paramInt1, paramInt2, paramInt3, paramInt4, paramArrayOfInt);
/*     */     }
/*     */ 
/* 339 */     return paramBufferedImage.getRGB(paramInt1, paramInt2, paramInt3, paramInt4, paramArrayOfInt, 0, paramInt3);
/*     */   }
/*     */ 
/*     */   public static void setPixels(BufferedImage paramBufferedImage, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int[] paramArrayOfInt)
/*     */   {
/* 359 */     if ((paramArrayOfInt == null) || (paramInt3 == 0) || (paramInt4 == 0))
/* 360 */       return;
/* 361 */     if (paramArrayOfInt.length < paramInt3 * paramInt4) {
/* 362 */       throw new IllegalArgumentException("pixels array must have a length >= w*h");
/*     */     }
/*     */ 
/* 366 */     int i = paramBufferedImage.getType();
/* 367 */     if ((i == 2) || (i == 1))
/*     */     {
/* 369 */       WritableRaster localWritableRaster = paramBufferedImage.getRaster();
/* 370 */       localWritableRaster.setDataElements(paramInt1, paramInt2, paramInt3, paramInt4, paramArrayOfInt);
/*     */     }
/*     */     else {
/* 373 */       paramBufferedImage.setRGB(paramInt1, paramInt2, paramInt3, paramInt4, paramArrayOfInt, 0, paramInt3);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static BufferedImage createColorModelCompatibleImage(BufferedImage paramBufferedImage)
/*     */   {
/* 389 */     ColorModel localColorModel = paramBufferedImage.getColorModel();
/* 390 */     return new BufferedImage(localColorModel, localColorModel.createCompatibleWritableRaster(paramBufferedImage.getWidth(), paramBufferedImage.getHeight()), localColorModel.isAlphaPremultiplied(), null);
/*     */   }
/*     */ 
/*     */   public static BufferedImage createCompatibleTranslucentImage(int paramInt1, int paramInt2)
/*     */   {
/* 410 */     return isHeadless() ? new BufferedImage(paramInt1, paramInt2, 2) : getGraphicsConfiguration().createCompatibleImage(paramInt1, paramInt2, 3);
/*     */   }
/*     */ 
/*     */   private static boolean isHeadless()
/*     */   {
/* 417 */     return GraphicsEnvironment.isHeadless();
/*     */   }
/*     */ 
/*     */   private static GraphicsConfiguration getGraphicsConfiguration()
/*     */   {
/* 422 */     return GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.nimbus.EffectUtils
 * JD-Core Version:    0.6.2
 */