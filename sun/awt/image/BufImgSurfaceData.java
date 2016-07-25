/*     */ package sun.awt.image;
/*     */ 
/*     */ import java.awt.GraphicsConfiguration;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.awt.image.ColorModel;
/*     */ import java.awt.image.DataBuffer;
/*     */ import java.awt.image.DirectColorModel;
/*     */ import java.awt.image.IndexColorModel;
/*     */ import java.awt.image.Raster;
/*     */ import java.awt.image.SampleModel;
/*     */ import java.awt.image.WritableRaster;
/*     */ import sun.java2d.SunGraphics2D;
/*     */ import sun.java2d.SurfaceData;
/*     */ import sun.java2d.loops.CompositeType;
/*     */ import sun.java2d.loops.RenderLoops;
/*     */ import sun.java2d.loops.SurfaceType;
/*     */ 
/*     */ public class BufImgSurfaceData extends SurfaceData
/*     */ {
/*     */   BufferedImage bufImg;
/*     */   private BufferedImageGraphicsConfig graphicsConfig;
/*     */   RenderLoops solidloops;
/*     */   private static final int DCM_RGBX_RED_MASK = -16777216;
/*     */   private static final int DCM_RGBX_GREEN_MASK = 16711680;
/*     */   private static final int DCM_RGBX_BLUE_MASK = 65280;
/*     */   private static final int DCM_555X_RED_MASK = 63488;
/*     */   private static final int DCM_555X_GREEN_MASK = 1984;
/*     */   private static final int DCM_555X_BLUE_MASK = 62;
/*     */   private static final int DCM_4444_RED_MASK = 3840;
/*     */   private static final int DCM_4444_GREEN_MASK = 240;
/*     */   private static final int DCM_4444_BLUE_MASK = 15;
/*     */   private static final int DCM_4444_ALPHA_MASK = 61440;
/*     */   private static final int DCM_ARGBBM_ALPHA_MASK = 16777216;
/*     */   private static final int DCM_ARGBBM_RED_MASK = 16711680;
/*     */   private static final int DCM_ARGBBM_GREEN_MASK = 65280;
/*     */   private static final int DCM_ARGBBM_BLUE_MASK = 255;
/*     */   private static final int CACHE_SIZE = 5;
/* 368 */   private static RenderLoops[] loopcache = new RenderLoops[5];
/* 369 */   private static SurfaceType[] typecache = new SurfaceType[5];
/*     */ 
/*     */   private static native void initIDs(Class paramClass1, Class paramClass2);
/*     */ 
/*     */   public static SurfaceData createData(BufferedImage paramBufferedImage)
/*     */   {
/*  75 */     if (paramBufferedImage == null) {
/*  76 */       throw new NullPointerException("BufferedImage cannot be null");
/*     */     }
/*     */ 
/*  79 */     ColorModel localColorModel = paramBufferedImage.getColorModel();
/*  80 */     int i = paramBufferedImage.getType();
/*     */     Object localObject1;
/*     */     Object localObject2;
/*  82 */     switch (i) {
/*     */     case 4:
/*  84 */       localObject1 = createDataIC(paramBufferedImage, SurfaceType.IntBgr);
/*  85 */       break;
/*     */     case 1:
/*  87 */       localObject1 = createDataIC(paramBufferedImage, SurfaceType.IntRgb);
/*  88 */       break;
/*     */     case 2:
/*  90 */       localObject1 = createDataIC(paramBufferedImage, SurfaceType.IntArgb);
/*  91 */       break;
/*     */     case 3:
/*  93 */       localObject1 = createDataIC(paramBufferedImage, SurfaceType.IntArgbPre);
/*  94 */       break;
/*     */     case 5:
/*  96 */       localObject1 = createDataBC(paramBufferedImage, SurfaceType.ThreeByteBgr, 2);
/*  97 */       break;
/*     */     case 6:
/*  99 */       localObject1 = createDataBC(paramBufferedImage, SurfaceType.FourByteAbgr, 3);
/* 100 */       break;
/*     */     case 7:
/* 102 */       localObject1 = createDataBC(paramBufferedImage, SurfaceType.FourByteAbgrPre, 3);
/* 103 */       break;
/*     */     case 8:
/* 105 */       localObject1 = createDataSC(paramBufferedImage, SurfaceType.Ushort565Rgb, null);
/* 106 */       break;
/*     */     case 9:
/* 108 */       localObject1 = createDataSC(paramBufferedImage, SurfaceType.Ushort555Rgb, null);
/* 109 */       break;
/*     */     case 13:
/* 113 */       switch (localColorModel.getTransparency()) {
/*     */       case 1:
/* 115 */         if (isOpaqueGray((IndexColorModel)localColorModel))
/* 116 */           localObject2 = SurfaceType.Index8Gray;
/*     */         else {
/* 118 */           localObject2 = SurfaceType.ByteIndexedOpaque;
/*     */         }
/* 120 */         break;
/*     */       case 2:
/* 122 */         localObject2 = SurfaceType.ByteIndexedBm;
/* 123 */         break;
/*     */       case 3:
/* 125 */         localObject2 = SurfaceType.ByteIndexed;
/* 126 */         break;
/*     */       default:
/* 128 */         throw new InternalError("Unrecognized transparency");
/*     */       }
/* 130 */       localObject1 = createDataBC(paramBufferedImage, (SurfaceType)localObject2, 0);
/*     */ 
/* 132 */       break;
/*     */     case 10:
/* 134 */       localObject1 = createDataBC(paramBufferedImage, SurfaceType.ByteGray, 0);
/* 135 */       break;
/*     */     case 11:
/* 137 */       localObject1 = createDataSC(paramBufferedImage, SurfaceType.UshortGray, null);
/* 138 */       break;
/*     */     case 12:
/* 142 */       SampleModel localSampleModel = paramBufferedImage.getRaster().getSampleModel();
/* 143 */       switch (localSampleModel.getSampleSize(0)) {
/*     */       case 1:
/* 145 */         localObject2 = SurfaceType.ByteBinary1Bit;
/* 146 */         break;
/*     */       case 2:
/* 148 */         localObject2 = SurfaceType.ByteBinary2Bit;
/* 149 */         break;
/*     */       case 4:
/* 151 */         localObject2 = SurfaceType.ByteBinary4Bit;
/* 152 */         break;
/*     */       case 3:
/*     */       default:
/* 154 */         throw new InternalError("Unrecognized pixel size");
/*     */       }
/* 156 */       localObject1 = createDataBP(paramBufferedImage, (SurfaceType)localObject2);
/*     */ 
/* 158 */       break;
/*     */     case 0:
/*     */     default:
/* 162 */       localObject2 = paramBufferedImage.getRaster();
/* 163 */       int j = ((Raster)localObject2).getNumBands();
/*     */       SurfaceType localSurfaceType;
/*     */       Object localObject3;
/*     */       int m;
/*     */       int n;
/*     */       int i1;
/* 164 */       if (((localObject2 instanceof IntegerComponentRaster)) && (((Raster)localObject2).getNumDataElements() == 1) && (((IntegerComponentRaster)localObject2).getPixelStride() == 1))
/*     */       {
/* 168 */         localSurfaceType = SurfaceType.AnyInt;
/* 169 */         if ((localColorModel instanceof DirectColorModel)) {
/* 170 */           localObject3 = (DirectColorModel)localColorModel;
/* 171 */           int k = ((DirectColorModel)localObject3).getAlphaMask();
/* 172 */           m = ((DirectColorModel)localObject3).getRedMask();
/* 173 */           n = ((DirectColorModel)localObject3).getGreenMask();
/* 174 */           i1 = ((DirectColorModel)localObject3).getBlueMask();
/* 175 */           if ((j == 3) && (k == 0) && (m == -16777216) && (n == 16711680) && (i1 == 65280))
/*     */           {
/* 181 */             localSurfaceType = SurfaceType.IntRgbx;
/* 182 */           } else if ((j == 4) && (k == 16777216) && (m == 16711680) && (n == 65280) && (i1 == 255))
/*     */           {
/* 188 */             localSurfaceType = SurfaceType.IntArgbBm;
/*     */           }
/* 190 */           else localSurfaceType = SurfaceType.AnyDcm;
/*     */         }
/*     */ 
/* 193 */         localObject1 = createDataIC(paramBufferedImage, localSurfaceType);
/*     */       }
/* 195 */       else if (((localObject2 instanceof ShortComponentRaster)) && (((Raster)localObject2).getNumDataElements() == 1) && (((ShortComponentRaster)localObject2).getPixelStride() == 1))
/*     */       {
/* 199 */         localSurfaceType = SurfaceType.AnyShort;
/* 200 */         localObject3 = null;
/* 201 */         if ((localColorModel instanceof DirectColorModel)) {
/* 202 */           DirectColorModel localDirectColorModel = (DirectColorModel)localColorModel;
/* 203 */           m = localDirectColorModel.getAlphaMask();
/* 204 */           n = localDirectColorModel.getRedMask();
/* 205 */           i1 = localDirectColorModel.getGreenMask();
/* 206 */           int i2 = localDirectColorModel.getBlueMask();
/* 207 */           if ((j == 3) && (m == 0) && (n == 63488) && (i1 == 1984) && (i2 == 62))
/*     */           {
/* 213 */             localSurfaceType = SurfaceType.Ushort555Rgbx;
/*     */           }
/* 215 */           else if ((j == 4) && (m == 61440) && (n == 3840) && (i1 == 240) && (i2 == 15))
/*     */           {
/* 221 */             localSurfaceType = SurfaceType.Ushort4444Argb;
/*     */           }
/* 223 */         } else if ((localColorModel instanceof IndexColorModel)) {
/* 224 */           localObject3 = (IndexColorModel)localColorModel;
/* 225 */           if (((IndexColorModel)localObject3).getPixelSize() == 12) {
/* 226 */             if (isOpaqueGray((IndexColorModel)localObject3))
/* 227 */               localSurfaceType = SurfaceType.Index12Gray;
/*     */             else
/* 229 */               localSurfaceType = SurfaceType.UshortIndexed;
/*     */           }
/*     */           else {
/* 232 */             localObject3 = null;
/*     */           }
/*     */         }
/* 235 */         localObject1 = createDataSC(paramBufferedImage, localSurfaceType, (IndexColorModel)localObject3);
/*     */       }
/*     */       else {
/* 238 */         localObject1 = new BufImgSurfaceData(((Raster)localObject2).getDataBuffer(), paramBufferedImage, SurfaceType.Custom);
/*     */       }
/*     */       break;
/*     */     }
/*     */ 
/* 243 */     ((BufImgSurfaceData)localObject1).initSolidLoops();
/* 244 */     return localObject1;
/*     */   }
/*     */ 
/*     */   public static SurfaceData createData(Raster paramRaster, ColorModel paramColorModel) {
/* 248 */     throw new InternalError("SurfaceData not implemented for Raster/CM");
/*     */   }
/*     */ 
/*     */   public static SurfaceData createDataIC(BufferedImage paramBufferedImage, SurfaceType paramSurfaceType)
/*     */   {
/* 253 */     IntegerComponentRaster localIntegerComponentRaster = (IntegerComponentRaster)paramBufferedImage.getRaster();
/*     */ 
/* 255 */     BufImgSurfaceData localBufImgSurfaceData = new BufImgSurfaceData(localIntegerComponentRaster.getDataBuffer(), paramBufferedImage, paramSurfaceType);
/*     */ 
/* 257 */     localBufImgSurfaceData.initRaster(localIntegerComponentRaster.getDataStorage(), localIntegerComponentRaster.getDataOffset(0) * 4, 0, localIntegerComponentRaster.getWidth(), localIntegerComponentRaster.getHeight(), localIntegerComponentRaster.getPixelStride() * 4, localIntegerComponentRaster.getScanlineStride() * 4, null);
/*     */ 
/* 264 */     return localBufImgSurfaceData;
/*     */   }
/*     */ 
/*     */   public static SurfaceData createDataSC(BufferedImage paramBufferedImage, SurfaceType paramSurfaceType, IndexColorModel paramIndexColorModel)
/*     */   {
/* 270 */     ShortComponentRaster localShortComponentRaster = (ShortComponentRaster)paramBufferedImage.getRaster();
/*     */ 
/* 272 */     BufImgSurfaceData localBufImgSurfaceData = new BufImgSurfaceData(localShortComponentRaster.getDataBuffer(), paramBufferedImage, paramSurfaceType);
/*     */ 
/* 274 */     localBufImgSurfaceData.initRaster(localShortComponentRaster.getDataStorage(), localShortComponentRaster.getDataOffset(0) * 2, 0, localShortComponentRaster.getWidth(), localShortComponentRaster.getHeight(), localShortComponentRaster.getPixelStride() * 2, localShortComponentRaster.getScanlineStride() * 2, paramIndexColorModel);
/*     */ 
/* 281 */     return localBufImgSurfaceData;
/*     */   }
/*     */ 
/*     */   public static SurfaceData createDataBC(BufferedImage paramBufferedImage, SurfaceType paramSurfaceType, int paramInt)
/*     */   {
/* 287 */     ByteComponentRaster localByteComponentRaster = (ByteComponentRaster)paramBufferedImage.getRaster();
/*     */ 
/* 289 */     BufImgSurfaceData localBufImgSurfaceData = new BufImgSurfaceData(localByteComponentRaster.getDataBuffer(), paramBufferedImage, paramSurfaceType);
/*     */ 
/* 291 */     ColorModel localColorModel = paramBufferedImage.getColorModel();
/* 292 */     IndexColorModel localIndexColorModel = (localColorModel instanceof IndexColorModel) ? (IndexColorModel)localColorModel : null;
/*     */ 
/* 295 */     localBufImgSurfaceData.initRaster(localByteComponentRaster.getDataStorage(), localByteComponentRaster.getDataOffset(paramInt), 0, localByteComponentRaster.getWidth(), localByteComponentRaster.getHeight(), localByteComponentRaster.getPixelStride(), localByteComponentRaster.getScanlineStride(), localIndexColorModel);
/*     */ 
/* 302 */     return localBufImgSurfaceData;
/*     */   }
/*     */ 
/*     */   public static SurfaceData createDataBP(BufferedImage paramBufferedImage, SurfaceType paramSurfaceType)
/*     */   {
/* 307 */     BytePackedRaster localBytePackedRaster = (BytePackedRaster)paramBufferedImage.getRaster();
/*     */ 
/* 309 */     BufImgSurfaceData localBufImgSurfaceData = new BufImgSurfaceData(localBytePackedRaster.getDataBuffer(), paramBufferedImage, paramSurfaceType);
/*     */ 
/* 311 */     ColorModel localColorModel = paramBufferedImage.getColorModel();
/* 312 */     IndexColorModel localIndexColorModel = (localColorModel instanceof IndexColorModel) ? (IndexColorModel)localColorModel : null;
/*     */ 
/* 315 */     localBufImgSurfaceData.initRaster(localBytePackedRaster.getDataStorage(), localBytePackedRaster.getDataBitOffset() / 8, localBytePackedRaster.getDataBitOffset() & 0x7, localBytePackedRaster.getWidth(), localBytePackedRaster.getHeight(), 0, localBytePackedRaster.getScanlineStride(), localIndexColorModel);
/*     */ 
/* 323 */     return localBufImgSurfaceData;
/*     */   }
/*     */ 
/*     */   public RenderLoops getRenderLoops(SunGraphics2D paramSunGraphics2D) {
/* 327 */     if ((paramSunGraphics2D.paintState <= 1) && (paramSunGraphics2D.compositeState <= 0))
/*     */     {
/* 330 */       return this.solidloops;
/*     */     }
/* 332 */     return super.getRenderLoops(paramSunGraphics2D);
/*     */   }
/*     */ 
/*     */   public Raster getRaster(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
/* 336 */     return this.bufImg.getRaster();
/*     */   }
/*     */ 
/*     */   protected native void initRaster(Object paramObject, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, IndexColorModel paramIndexColorModel);
/*     */ 
/*     */   public BufImgSurfaceData(DataBuffer paramDataBuffer, BufferedImage paramBufferedImage, SurfaceType paramSurfaceType)
/*     */   {
/* 354 */     super(SunWritableRaster.stealTrackable(paramDataBuffer), paramSurfaceType, paramBufferedImage.getColorModel());
/*     */ 
/* 356 */     this.bufImg = paramBufferedImage;
/*     */   }
/*     */ 
/*     */   protected BufImgSurfaceData(SurfaceType paramSurfaceType, ColorModel paramColorModel) {
/* 360 */     super(paramSurfaceType, paramColorModel);
/*     */   }
/*     */ 
/*     */   public void initSolidLoops() {
/* 364 */     this.solidloops = getSolidLoops(getSurfaceType());
/*     */   }
/*     */ 
/*     */   public static synchronized RenderLoops getSolidLoops(SurfaceType paramSurfaceType)
/*     */   {
/* 371 */     for (int i = 4; i >= 0; i--) {
/* 372 */       SurfaceType localSurfaceType = typecache[i];
/* 373 */       if (localSurfaceType == paramSurfaceType)
/* 374 */         return loopcache[i];
/* 375 */       if (localSurfaceType == null) {
/*     */         break;
/*     */       }
/*     */     }
/* 379 */     RenderLoops localRenderLoops = makeRenderLoops(SurfaceType.OpaqueColor, CompositeType.SrcNoEa, paramSurfaceType);
/*     */ 
/* 382 */     System.arraycopy(loopcache, 1, loopcache, 0, 4);
/* 383 */     System.arraycopy(typecache, 1, typecache, 0, 4);
/* 384 */     loopcache[4] = localRenderLoops;
/* 385 */     typecache[4] = paramSurfaceType;
/* 386 */     return localRenderLoops;
/*     */   }
/*     */ 
/*     */   public SurfaceData getReplacement()
/*     */   {
/* 392 */     return restoreContents(this.bufImg);
/*     */   }
/*     */ 
/*     */   public synchronized GraphicsConfiguration getDeviceConfiguration() {
/* 396 */     if (this.graphicsConfig == null) {
/* 397 */       this.graphicsConfig = BufferedImageGraphicsConfig.getConfig(this.bufImg);
/*     */     }
/* 399 */     return this.graphicsConfig;
/*     */   }
/*     */ 
/*     */   public Rectangle getBounds() {
/* 403 */     return new Rectangle(this.bufImg.getWidth(), this.bufImg.getHeight());
/*     */   }
/*     */ 
/*     */   protected void checkCustomComposite()
/*     */   {
/*     */   }
/*     */ 
/*     */   private static native void freeNativeICMData(long paramLong);
/*     */ 
/*     */   public Object getDestination()
/*     */   {
/* 417 */     return this.bufImg;
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  71 */     initIDs(IndexColorModel.class, ICMColorData.class);
/*     */   }
/*     */ 
/*     */   public static final class ICMColorData
/*     */   {
/* 421 */     private long pData = 0L;
/*     */ 
/*     */     private ICMColorData(long paramLong) {
/* 424 */       this.pData = paramLong;
/*     */     }
/*     */ 
/*     */     public void finalize() {
/* 428 */       if (this.pData != 0L) {
/* 429 */         BufImgSurfaceData.freeNativeICMData(this.pData);
/* 430 */         this.pData = 0L;
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.image.BufImgSurfaceData
 * JD-Core Version:    0.6.2
 */