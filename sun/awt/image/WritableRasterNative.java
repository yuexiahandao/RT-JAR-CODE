/*     */ package sun.awt.image;
/*     */ 
/*     */ import java.awt.Point;
/*     */ import java.awt.image.ColorModel;
/*     */ import java.awt.image.DataBuffer;
/*     */ import java.awt.image.DirectColorModel;
/*     */ import java.awt.image.PixelInterleavedSampleModel;
/*     */ import java.awt.image.SampleModel;
/*     */ import java.awt.image.SinglePixelPackedSampleModel;
/*     */ import java.awt.image.WritableRaster;
/*     */ import sun.java2d.SurfaceData;
/*     */ 
/*     */ public class WritableRasterNative extends WritableRaster
/*     */ {
/*     */   public static WritableRasterNative createNativeRaster(SampleModel paramSampleModel, DataBuffer paramDataBuffer)
/*     */   {
/*  58 */     return new WritableRasterNative(paramSampleModel, paramDataBuffer);
/*     */   }
/*     */ 
/*     */   protected WritableRasterNative(SampleModel paramSampleModel, DataBuffer paramDataBuffer) {
/*  62 */     super(paramSampleModel, paramDataBuffer, new Point(0, 0));
/*     */   }
/*     */ 
/*     */   public static WritableRasterNative createNativeRaster(ColorModel paramColorModel, SurfaceData paramSurfaceData, int paramInt1, int paramInt2)
/*     */   {
/*  70 */     Object localObject1 = null;
/*  71 */     int i = 0;
/*  72 */     int j = paramInt1;
/*     */     int[] arrayOfInt;
/*     */     DirectColorModel localDirectColorModel;
/*  74 */     switch (paramColorModel.getPixelSize())
/*     */     {
/*     */     case 8:
/*     */     case 12:
/*  78 */       if (paramColorModel.getPixelSize() == 8)
/*  79 */         i = 0;
/*     */       else {
/*  81 */         i = 1;
/*     */       }
/*  83 */       localObject2 = new int[1];
/*  84 */       localObject2[0] = 0;
/*  85 */       localObject1 = new PixelInterleavedSampleModel(i, paramInt1, paramInt2, 1, j, (int[])localObject2);
/*     */ 
/*  89 */       break;
/*     */     case 15:
/*     */     case 16:
/*  94 */       i = 1;
/*  95 */       arrayOfInt = new int[3];
/*  96 */       localDirectColorModel = (DirectColorModel)paramColorModel;
/*  97 */       arrayOfInt[0] = localDirectColorModel.getRedMask();
/*  98 */       arrayOfInt[1] = localDirectColorModel.getGreenMask();
/*  99 */       arrayOfInt[2] = localDirectColorModel.getBlueMask();
/* 100 */       localObject1 = new SinglePixelPackedSampleModel(i, paramInt1, paramInt2, j, arrayOfInt);
/*     */ 
/* 103 */       break;
/*     */     case 24:
/*     */     case 32:
/* 106 */       i = 3;
/* 107 */       arrayOfInt = new int[3];
/* 108 */       localDirectColorModel = (DirectColorModel)paramColorModel;
/* 109 */       arrayOfInt[0] = localDirectColorModel.getRedMask();
/* 110 */       arrayOfInt[1] = localDirectColorModel.getGreenMask();
/* 111 */       arrayOfInt[2] = localDirectColorModel.getBlueMask();
/* 112 */       localObject1 = new SinglePixelPackedSampleModel(i, paramInt1, paramInt2, j, arrayOfInt);
/*     */ 
/* 115 */       break;
/*     */     default:
/* 117 */       throw new InternalError("Unsupported depth " + paramColorModel.getPixelSize());
/*     */     }
/*     */ 
/* 121 */     Object localObject2 = new DataBufferNative(paramSurfaceData, i, paramInt1, paramInt2);
/*     */ 
/* 123 */     return new WritableRasterNative((SampleModel)localObject1, (DataBuffer)localObject2);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.image.WritableRasterNative
 * JD-Core Version:    0.6.2
 */