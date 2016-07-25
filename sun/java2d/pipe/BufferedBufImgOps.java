/*     */ package sun.java2d.pipe;
/*     */ 
/*     */ import java.awt.color.ColorSpace;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.awt.image.BufferedImageOp;
/*     */ import java.awt.image.ByteLookupTable;
/*     */ import java.awt.image.ColorModel;
/*     */ import java.awt.image.ConvolveOp;
/*     */ import java.awt.image.IndexColorModel;
/*     */ import java.awt.image.Kernel;
/*     */ import java.awt.image.LookupOp;
/*     */ import java.awt.image.LookupTable;
/*     */ import java.awt.image.RescaleOp;
/*     */ import java.awt.image.ShortLookupTable;
/*     */ import sun.java2d.SurfaceData;
/*     */ 
/*     */ public class BufferedBufImgOps
/*     */ {
/*     */   public static void enableBufImgOp(RenderQueue paramRenderQueue, SurfaceData paramSurfaceData, BufferedImage paramBufferedImage, BufferedImageOp paramBufferedImageOp)
/*     */   {
/*  52 */     if ((paramBufferedImageOp instanceof ConvolveOp))
/*  53 */       enableConvolveOp(paramRenderQueue, paramSurfaceData, (ConvolveOp)paramBufferedImageOp);
/*  54 */     else if ((paramBufferedImageOp instanceof RescaleOp))
/*  55 */       enableRescaleOp(paramRenderQueue, paramSurfaceData, paramBufferedImage, (RescaleOp)paramBufferedImageOp);
/*  56 */     else if ((paramBufferedImageOp instanceof LookupOp))
/*  57 */       enableLookupOp(paramRenderQueue, paramSurfaceData, paramBufferedImage, (LookupOp)paramBufferedImageOp);
/*     */     else
/*  59 */       throw new InternalError("Unknown BufferedImageOp");
/*     */   }
/*     */ 
/*     */   public static void disableBufImgOp(RenderQueue paramRenderQueue, BufferedImageOp paramBufferedImageOp)
/*     */   {
/*  64 */     if ((paramBufferedImageOp instanceof ConvolveOp))
/*  65 */       disableConvolveOp(paramRenderQueue);
/*  66 */     else if ((paramBufferedImageOp instanceof RescaleOp))
/*  67 */       disableRescaleOp(paramRenderQueue);
/*  68 */     else if ((paramBufferedImageOp instanceof LookupOp))
/*  69 */       disableLookupOp(paramRenderQueue);
/*     */     else
/*  71 */       throw new InternalError("Unknown BufferedImageOp");
/*     */   }
/*     */ 
/*     */   public static boolean isConvolveOpValid(ConvolveOp paramConvolveOp)
/*     */   {
/*  78 */     Kernel localKernel = paramConvolveOp.getKernel();
/*  79 */     int i = localKernel.getWidth();
/*  80 */     int j = localKernel.getHeight();
/*     */ 
/*  84 */     if (((i != 3) || (j != 3)) && ((i != 5) || (j != 5))) {
/*  85 */       return false;
/*     */     }
/*  87 */     return true;
/*     */   }
/*     */ 
/*     */   private static void enableConvolveOp(RenderQueue paramRenderQueue, SurfaceData paramSurfaceData, ConvolveOp paramConvolveOp)
/*     */   {
/*  95 */     int i = paramConvolveOp.getEdgeCondition() == 0 ? 1 : 0;
/*     */ 
/*  97 */     Kernel localKernel = paramConvolveOp.getKernel();
/*  98 */     int j = localKernel.getWidth();
/*  99 */     int k = localKernel.getHeight();
/* 100 */     int m = j * k;
/* 101 */     int n = 4;
/* 102 */     int i1 = 24 + m * n;
/*     */ 
/* 104 */     RenderBuffer localRenderBuffer = paramRenderQueue.getBuffer();
/* 105 */     paramRenderQueue.ensureCapacityAndAlignment(i1, 4);
/* 106 */     localRenderBuffer.putInt(120);
/* 107 */     localRenderBuffer.putLong(paramSurfaceData.getNativeOps());
/* 108 */     localRenderBuffer.putInt(i != 0 ? 1 : 0);
/* 109 */     localRenderBuffer.putInt(j);
/* 110 */     localRenderBuffer.putInt(k);
/* 111 */     localRenderBuffer.put(localKernel.getKernelData(null));
/*     */   }
/*     */ 
/*     */   private static void disableConvolveOp(RenderQueue paramRenderQueue)
/*     */   {
/* 116 */     RenderBuffer localRenderBuffer = paramRenderQueue.getBuffer();
/* 117 */     paramRenderQueue.ensureCapacity(4);
/* 118 */     localRenderBuffer.putInt(121);
/*     */   }
/*     */ 
/*     */   public static boolean isRescaleOpValid(RescaleOp paramRescaleOp, BufferedImage paramBufferedImage)
/*     */   {
/* 126 */     int i = paramRescaleOp.getNumFactors();
/* 127 */     ColorModel localColorModel = paramBufferedImage.getColorModel();
/*     */ 
/* 129 */     if ((localColorModel instanceof IndexColorModel)) {
/* 130 */       throw new IllegalArgumentException("Rescaling cannot be performed on an indexed image");
/*     */     }
/*     */ 
/* 134 */     if ((i != 1) && (i != localColorModel.getNumColorComponents()) && (i != localColorModel.getNumComponents()))
/*     */     {
/* 138 */       throw new IllegalArgumentException("Number of scaling constants does not equal the number of of color or color/alpha  components");
/*     */     }
/*     */ 
/* 144 */     int j = localColorModel.getColorSpace().getType();
/* 145 */     if ((j != 5) && (j != 6))
/*     */     {
/* 149 */       return false;
/*     */     }
/*     */ 
/* 152 */     if ((i == 2) || (i > 4))
/*     */     {
/* 154 */       return false;
/*     */     }
/*     */ 
/* 157 */     return true;
/*     */   }
/*     */ 
/*     */   private static void enableRescaleOp(RenderQueue paramRenderQueue, SurfaceData paramSurfaceData, BufferedImage paramBufferedImage, RescaleOp paramRescaleOp)
/*     */   {
/* 166 */     ColorModel localColorModel = paramBufferedImage.getColorModel();
/* 167 */     int i = (localColorModel.hasAlpha()) && (localColorModel.isAlphaPremultiplied()) ? 1 : 0;
/*     */ 
/* 186 */     int j = paramRescaleOp.getNumFactors();
/* 187 */     float[] arrayOfFloat1 = paramRescaleOp.getScaleFactors(null);
/* 188 */     float[] arrayOfFloat2 = paramRescaleOp.getOffsets(null);
/*     */     float[] arrayOfFloat3;
/*     */     float[] arrayOfFloat4;
/* 195 */     if (j == 1) {
/* 196 */       arrayOfFloat3 = new float[4];
/* 197 */       arrayOfFloat4 = new float[4];
/* 198 */       for (k = 0; k < 3; k++) {
/* 199 */         arrayOfFloat3[k] = arrayOfFloat1[0];
/* 200 */         arrayOfFloat4[k] = arrayOfFloat2[0];
/*     */       }
/*     */ 
/* 203 */       arrayOfFloat3[3] = 1.0F;
/* 204 */       arrayOfFloat4[3] = 0.0F;
/* 205 */     } else if (j == 3) {
/* 206 */       arrayOfFloat3 = new float[4];
/* 207 */       arrayOfFloat4 = new float[4];
/* 208 */       for (k = 0; k < 3; k++) {
/* 209 */         arrayOfFloat3[k] = arrayOfFloat1[k];
/* 210 */         arrayOfFloat4[k] = arrayOfFloat2[k];
/*     */       }
/*     */ 
/* 213 */       arrayOfFloat3[3] = 1.0F;
/* 214 */       arrayOfFloat4[3] = 0.0F;
/*     */     } else {
/* 216 */       arrayOfFloat3 = arrayOfFloat1;
/* 217 */       arrayOfFloat4 = arrayOfFloat2;
/*     */     }
/*     */     int n;
/* 224 */     if (localColorModel.getNumComponents() == 1)
/*     */     {
/* 226 */       k = localColorModel.getComponentSize(0);
/* 227 */       m = (1 << k) - 1;
/* 228 */       for (n = 0; n < 3; n++)
/* 229 */         arrayOfFloat4[n] /= m;
/*     */     }
/*     */     else
/*     */     {
/* 233 */       for (k = 0; k < localColorModel.getNumComponents(); k++) {
/* 234 */         m = localColorModel.getComponentSize(k);
/* 235 */         n = (1 << m) - 1;
/* 236 */         arrayOfFloat4[k] /= n;
/*     */       }
/*     */     }
/*     */ 
/* 240 */     int k = 4;
/* 241 */     int m = 16 + 4 * k * 2;
/*     */ 
/* 243 */     RenderBuffer localRenderBuffer = paramRenderQueue.getBuffer();
/* 244 */     paramRenderQueue.ensureCapacityAndAlignment(m, 4);
/* 245 */     localRenderBuffer.putInt(122);
/* 246 */     localRenderBuffer.putLong(paramSurfaceData.getNativeOps());
/* 247 */     localRenderBuffer.putInt(i != 0 ? 1 : 0);
/* 248 */     localRenderBuffer.put(arrayOfFloat3);
/* 249 */     localRenderBuffer.put(arrayOfFloat4);
/*     */   }
/*     */ 
/*     */   private static void disableRescaleOp(RenderQueue paramRenderQueue)
/*     */   {
/* 254 */     RenderBuffer localRenderBuffer = paramRenderQueue.getBuffer();
/* 255 */     paramRenderQueue.ensureCapacity(4);
/* 256 */     localRenderBuffer.putInt(123);
/*     */   }
/*     */ 
/*     */   public static boolean isLookupOpValid(LookupOp paramLookupOp, BufferedImage paramBufferedImage)
/*     */   {
/* 264 */     LookupTable localLookupTable = paramLookupOp.getTable();
/* 265 */     int i = localLookupTable.getNumComponents();
/* 266 */     ColorModel localColorModel = paramBufferedImage.getColorModel();
/*     */ 
/* 268 */     if ((localColorModel instanceof IndexColorModel)) {
/* 269 */       throw new IllegalArgumentException("LookupOp cannot be performed on an indexed image");
/*     */     }
/*     */ 
/* 273 */     if ((i != 1) && (i != localColorModel.getNumComponents()) && (i != localColorModel.getNumColorComponents()))
/*     */     {
/* 277 */       throw new IllegalArgumentException("Number of arrays in the  lookup table (" + i + ") is not compatible with" + " the src image: " + paramBufferedImage);
/*     */     }
/*     */ 
/* 284 */     int j = localColorModel.getColorSpace().getType();
/* 285 */     if ((j != 5) && (j != 6))
/*     */     {
/* 289 */       return false;
/*     */     }
/*     */ 
/* 292 */     if ((i == 2) || (i > 4))
/*     */     {
/* 294 */       return false;
/*     */     }
/*     */     Object localObject;
/*     */     int k;
/* 302 */     if ((localLookupTable instanceof ByteLookupTable)) {
/* 303 */       localObject = ((ByteLookupTable)localLookupTable).getTable();
/* 304 */       for (k = 1; k < localObject.length; k++)
/* 305 */         if ((localObject[k].length > 256) || (localObject[k].length != localObject[(k - 1)].length))
/*     */         {
/* 308 */           return false;
/*     */         }
/*     */     }
/* 311 */     else if ((localLookupTable instanceof ShortLookupTable)) {
/* 312 */       localObject = ((ShortLookupTable)localLookupTable).getTable();
/* 313 */       for (k = 1; k < localObject.length; k++)
/* 314 */         if ((localObject[k].length > 256) || (localObject[k].length != localObject[(k - 1)].length))
/*     */         {
/* 317 */           return false;
/*     */         }
/*     */     }
/*     */     else {
/* 321 */       return false;
/*     */     }
/*     */ 
/* 324 */     return true;
/*     */   }
/*     */ 
/*     */   private static void enableLookupOp(RenderQueue paramRenderQueue, SurfaceData paramSurfaceData, BufferedImage paramBufferedImage, LookupOp paramLookupOp)
/*     */   {
/* 333 */     int i = (paramBufferedImage.getColorModel().hasAlpha()) && (paramBufferedImage.isAlphaPremultiplied()) ? 1 : 0;
/*     */ 
/* 337 */     LookupTable localLookupTable = paramLookupOp.getTable();
/* 338 */     int j = localLookupTable.getNumComponents();
/* 339 */     int k = localLookupTable.getOffset();
/*     */     Object localObject1;
/*     */     int m;
/*     */     int n;
/*     */     int i1;
/* 344 */     if ((localLookupTable instanceof ShortLookupTable)) {
/* 345 */       localObject1 = ((ShortLookupTable)localLookupTable).getTable();
/* 346 */       m = localObject1[0].length;
/* 347 */       n = 2;
/* 348 */       i1 = 1;
/*     */     } else {
/* 350 */       localObject1 = ((ByteLookupTable)localLookupTable).getTable();
/* 351 */       m = localObject1[0].length;
/* 352 */       n = 1;
/* 353 */       i1 = 0;
/*     */     }
/*     */ 
/* 357 */     int i2 = j * m * n;
/* 358 */     int i3 = i2 + 3 & 0xFFFFFFFC;
/* 359 */     int i4 = i3 - i2;
/* 360 */     int i5 = 32 + i3;
/*     */ 
/* 362 */     RenderBuffer localRenderBuffer = paramRenderQueue.getBuffer();
/* 363 */     paramRenderQueue.ensureCapacityAndAlignment(i5, 4);
/* 364 */     localRenderBuffer.putInt(124);
/* 365 */     localRenderBuffer.putLong(paramSurfaceData.getNativeOps());
/* 366 */     localRenderBuffer.putInt(i != 0 ? 1 : 0);
/* 367 */     localRenderBuffer.putInt(i1 != 0 ? 1 : 0);
/* 368 */     localRenderBuffer.putInt(j);
/* 369 */     localRenderBuffer.putInt(m);
/* 370 */     localRenderBuffer.putInt(k);
/*     */     Object localObject2;
/*     */     int i6;
/* 371 */     if (i1 != 0) {
/* 372 */       localObject2 = ((ShortLookupTable)localLookupTable).getTable();
/* 373 */       for (i6 = 0; i6 < j; i6++)
/* 374 */         localRenderBuffer.put(localObject2[i6]);
/*     */     }
/*     */     else {
/* 377 */       localObject2 = ((ByteLookupTable)localLookupTable).getTable();
/* 378 */       for (i6 = 0; i6 < j; i6++) {
/* 379 */         localRenderBuffer.put(localObject2[i6]);
/*     */       }
/*     */     }
/* 382 */     if (i4 != 0)
/* 383 */       localRenderBuffer.position(localRenderBuffer.position() + i4);
/*     */   }
/*     */ 
/*     */   private static void disableLookupOp(RenderQueue paramRenderQueue)
/*     */   {
/* 389 */     RenderBuffer localRenderBuffer = paramRenderQueue.getBuffer();
/* 390 */     paramRenderQueue.ensureCapacity(4);
/* 391 */     localRenderBuffer.putInt(125);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.java2d.pipe.BufferedBufImgOps
 * JD-Core Version:    0.6.2
 */