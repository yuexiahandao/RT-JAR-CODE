/*     */ package java.awt.image;
/*     */ 
/*     */ public abstract class RGBImageFilter extends ImageFilter
/*     */ {
/*     */   protected ColorModel origmodel;
/*     */   protected ColorModel newmodel;
/*     */   protected boolean canFilterIndexColorModel;
/*     */ 
/*     */   public void setColorModel(ColorModel paramColorModel)
/*     */   {
/* 115 */     if ((this.canFilterIndexColorModel) && ((paramColorModel instanceof IndexColorModel))) {
/* 116 */       IndexColorModel localIndexColorModel = filterIndexColorModel((IndexColorModel)paramColorModel);
/* 117 */       substituteColorModel(paramColorModel, localIndexColorModel);
/* 118 */       this.consumer.setColorModel(localIndexColorModel);
/*     */     } else {
/* 120 */       this.consumer.setColorModel(ColorModel.getRGBdefault());
/*     */     }
/*     */   }
/*     */ 
/*     */   public void substituteColorModel(ColorModel paramColorModel1, ColorModel paramColorModel2)
/*     */   {
/* 133 */     this.origmodel = paramColorModel1;
/* 134 */     this.newmodel = paramColorModel2;
/*     */   }
/*     */ 
/*     */   public IndexColorModel filterIndexColorModel(IndexColorModel paramIndexColorModel)
/*     */   {
/* 148 */     int i = paramIndexColorModel.getMapSize();
/* 149 */     byte[] arrayOfByte1 = new byte[i];
/* 150 */     byte[] arrayOfByte2 = new byte[i];
/* 151 */     byte[] arrayOfByte3 = new byte[i];
/* 152 */     byte[] arrayOfByte4 = new byte[i];
/* 153 */     paramIndexColorModel.getReds(arrayOfByte1);
/* 154 */     paramIndexColorModel.getGreens(arrayOfByte2);
/* 155 */     paramIndexColorModel.getBlues(arrayOfByte3);
/* 156 */     paramIndexColorModel.getAlphas(arrayOfByte4);
/* 157 */     int j = paramIndexColorModel.getTransparentPixel();
/* 158 */     int k = 0;
/* 159 */     for (int m = 0; m < i; m++) {
/* 160 */       int n = filterRGB(-1, -1, paramIndexColorModel.getRGB(m));
/* 161 */       arrayOfByte4[m] = ((byte)(n >> 24));
/* 162 */       if ((arrayOfByte4[m] != -1) && (m != j)) {
/* 163 */         k = 1;
/*     */       }
/* 165 */       arrayOfByte1[m] = ((byte)(n >> 16));
/* 166 */       arrayOfByte2[m] = ((byte)(n >> 8));
/* 167 */       arrayOfByte3[m] = ((byte)(n >> 0));
/*     */     }
/* 169 */     if (k != 0) {
/* 170 */       return new IndexColorModel(paramIndexColorModel.getPixelSize(), i, arrayOfByte1, arrayOfByte2, arrayOfByte3, arrayOfByte4);
/*     */     }
/*     */ 
/* 173 */     return new IndexColorModel(paramIndexColorModel.getPixelSize(), i, arrayOfByte1, arrayOfByte2, arrayOfByte3, j);
/*     */   }
/*     */ 
/*     */   public void filterRGBPixels(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int[] paramArrayOfInt, int paramInt5, int paramInt6)
/*     */   {
/* 196 */     int i = paramInt5;
/* 197 */     for (int j = 0; j < paramInt4; j++) {
/* 198 */       for (int k = 0; k < paramInt3; k++) {
/* 199 */         paramArrayOfInt[i] = filterRGB(paramInt1 + k, paramInt2 + j, paramArrayOfInt[i]);
/* 200 */         i++;
/*     */       }
/* 202 */       i += paramInt6 - paramInt3;
/*     */     }
/* 204 */     this.consumer.setPixels(paramInt1, paramInt2, paramInt3, paramInt4, ColorModel.getRGBdefault(), paramArrayOfInt, paramInt5, paramInt6);
/*     */   }
/*     */ 
/*     */   public void setPixels(int paramInt1, int paramInt2, int paramInt3, int paramInt4, ColorModel paramColorModel, byte[] paramArrayOfByte, int paramInt5, int paramInt6)
/*     */   {
/* 227 */     if (paramColorModel == this.origmodel) {
/* 228 */       this.consumer.setPixels(paramInt1, paramInt2, paramInt3, paramInt4, this.newmodel, paramArrayOfByte, paramInt5, paramInt6);
/*     */     } else {
/* 230 */       int[] arrayOfInt = new int[paramInt3];
/* 231 */       int i = paramInt5;
/* 232 */       for (int j = 0; j < paramInt4; j++) {
/* 233 */         for (int k = 0; k < paramInt3; k++) {
/* 234 */           arrayOfInt[k] = paramColorModel.getRGB(paramArrayOfByte[i] & 0xFF);
/* 235 */           i++;
/*     */         }
/* 237 */         i += paramInt6 - paramInt3;
/* 238 */         filterRGBPixels(paramInt1, paramInt2 + j, paramInt3, 1, arrayOfInt, 0, paramInt3);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setPixels(int paramInt1, int paramInt2, int paramInt3, int paramInt4, ColorModel paramColorModel, int[] paramArrayOfInt, int paramInt5, int paramInt6)
/*     */   {
/* 264 */     if (paramColorModel == this.origmodel) {
/* 265 */       this.consumer.setPixels(paramInt1, paramInt2, paramInt3, paramInt4, this.newmodel, paramArrayOfInt, paramInt5, paramInt6);
/*     */     } else {
/* 267 */       int[] arrayOfInt = new int[paramInt3];
/* 268 */       int i = paramInt5;
/* 269 */       for (int j = 0; j < paramInt4; j++) {
/* 270 */         for (int k = 0; k < paramInt3; k++) {
/* 271 */           arrayOfInt[k] = paramColorModel.getRGB(paramArrayOfInt[i]);
/* 272 */           i++;
/*     */         }
/* 274 */         i += paramInt6 - paramInt3;
/* 275 */         filterRGBPixels(paramInt1, paramInt2 + j, paramInt3, 1, arrayOfInt, 0, paramInt3);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public abstract int filterRGB(int paramInt1, int paramInt2, int paramInt3);
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.image.RGBImageFilter
 * JD-Core Version:    0.6.2
 */