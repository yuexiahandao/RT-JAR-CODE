/*     */ package sun.java2d.opengl;
/*     */ 
/*     */ import java.awt.Composite;
/*     */ import java.awt.geom.AffineTransform;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.awt.image.BufferedImageOp;
/*     */ import sun.java2d.SurfaceData;
/*     */ import sun.java2d.loops.CompositeType;
/*     */ import sun.java2d.loops.GraphicsPrimitive;
/*     */ import sun.java2d.loops.GraphicsPrimitiveMgr;
/*     */ import sun.java2d.loops.SurfaceType;
/*     */ import sun.java2d.pipe.Region;
/*     */ import sun.java2d.pipe.RenderBuffer;
/*     */ import sun.java2d.pipe.RenderQueue;
/*     */ 
/*     */ class OGLBlitLoops
/*     */ {
/*     */   private static final int OFFSET_SRCTYPE = 16;
/*     */   private static final int OFFSET_HINT = 8;
/*     */   private static final int OFFSET_TEXTURE = 3;
/*     */   private static final int OFFSET_RTT = 2;
/*     */   private static final int OFFSET_XFORM = 1;
/*     */   private static final int OFFSET_ISOBLIT = 0;
/*     */ 
/*     */   static void register()
/*     */   {
/*  52 */     OGLSwToSurfaceBlit localOGLSwToSurfaceBlit = new OGLSwToSurfaceBlit(SurfaceType.IntArgbPre, 1);
/*     */ 
/*  55 */     OGLSwToTextureBlit localOGLSwToTextureBlit = new OGLSwToTextureBlit(SurfaceType.IntArgbPre, 1);
/*     */ 
/*  59 */     GraphicsPrimitive[] arrayOfGraphicsPrimitive = { new OGLSurfaceToSurfaceBlit(), new OGLSurfaceToSurfaceScale(), new OGLSurfaceToSurfaceTransform(), new OGLRTTSurfaceToSurfaceBlit(), new OGLRTTSurfaceToSurfaceScale(), new OGLRTTSurfaceToSurfaceTransform(), new OGLSurfaceToSwBlit(SurfaceType.IntArgb, 0), localOGLSwToSurfaceBlit, new OGLSwToSurfaceBlit(SurfaceType.IntRgb, 2), new OGLSwToSurfaceBlit(SurfaceType.IntRgbx, 3), new OGLSwToSurfaceBlit(SurfaceType.IntBgr, 4), new OGLSwToSurfaceBlit(SurfaceType.IntBgrx, 5), new OGLSwToSurfaceBlit(SurfaceType.ThreeByteBgr, 11), new OGLSwToSurfaceBlit(SurfaceType.Ushort565Rgb, 6), new OGLSwToSurfaceBlit(SurfaceType.Ushort555Rgb, 7), new OGLSwToSurfaceBlit(SurfaceType.Ushort555Rgbx, 8), new OGLSwToSurfaceBlit(SurfaceType.ByteGray, 9), new OGLSwToSurfaceBlit(SurfaceType.UshortGray, 10), new OGLGeneralBlit(OGLSurfaceData.OpenGLSurface, CompositeType.AnyAlpha, localOGLSwToSurfaceBlit), new OGLAnyCompositeBlit(OGLSurfaceData.OpenGLSurface), new OGLSwToSurfaceScale(SurfaceType.IntRgb, 2), new OGLSwToSurfaceScale(SurfaceType.IntRgbx, 3), new OGLSwToSurfaceScale(SurfaceType.IntBgr, 4), new OGLSwToSurfaceScale(SurfaceType.IntBgrx, 5), new OGLSwToSurfaceScale(SurfaceType.ThreeByteBgr, 11), new OGLSwToSurfaceScale(SurfaceType.Ushort565Rgb, 6), new OGLSwToSurfaceScale(SurfaceType.Ushort555Rgb, 7), new OGLSwToSurfaceScale(SurfaceType.Ushort555Rgbx, 8), new OGLSwToSurfaceScale(SurfaceType.ByteGray, 9), new OGLSwToSurfaceScale(SurfaceType.UshortGray, 10), new OGLSwToSurfaceScale(SurfaceType.IntArgbPre, 1), new OGLSwToSurfaceTransform(SurfaceType.IntRgb, 2), new OGLSwToSurfaceTransform(SurfaceType.IntRgbx, 3), new OGLSwToSurfaceTransform(SurfaceType.IntBgr, 4), new OGLSwToSurfaceTransform(SurfaceType.IntBgrx, 5), new OGLSwToSurfaceTransform(SurfaceType.ThreeByteBgr, 11), new OGLSwToSurfaceTransform(SurfaceType.Ushort565Rgb, 6), new OGLSwToSurfaceTransform(SurfaceType.Ushort555Rgb, 7), new OGLSwToSurfaceTransform(SurfaceType.Ushort555Rgbx, 8), new OGLSwToSurfaceTransform(SurfaceType.ByteGray, 9), new OGLSwToSurfaceTransform(SurfaceType.UshortGray, 10), new OGLSwToSurfaceTransform(SurfaceType.IntArgbPre, 1), new OGLTextureToSurfaceBlit(), new OGLTextureToSurfaceScale(), new OGLTextureToSurfaceTransform(), localOGLSwToTextureBlit, new OGLSwToTextureBlit(SurfaceType.IntRgb, 2), new OGLSwToTextureBlit(SurfaceType.IntRgbx, 3), new OGLSwToTextureBlit(SurfaceType.IntBgr, 4), new OGLSwToTextureBlit(SurfaceType.IntBgrx, 5), new OGLSwToTextureBlit(SurfaceType.ThreeByteBgr, 11), new OGLSwToTextureBlit(SurfaceType.Ushort565Rgb, 6), new OGLSwToTextureBlit(SurfaceType.Ushort555Rgb, 7), new OGLSwToTextureBlit(SurfaceType.Ushort555Rgbx, 8), new OGLSwToTextureBlit(SurfaceType.ByteGray, 9), new OGLSwToTextureBlit(SurfaceType.UshortGray, 10), new OGLGeneralBlit(OGLSurfaceData.OpenGLTexture, CompositeType.SrcNoEa, localOGLSwToTextureBlit), new OGLAnyCompositeBlit(OGLSurfaceData.OpenGLTexture) };
/*     */ 
/* 182 */     GraphicsPrimitiveMgr.register(arrayOfGraphicsPrimitive);
/*     */   }
/*     */ 
/*     */   private static int createPackedParams(boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4, int paramInt1, int paramInt2)
/*     */   {
/* 205 */     return paramInt2 << 16 | paramInt1 << 8 | (paramBoolean2 ? 1 : 0) << 3 | (paramBoolean3 ? 1 : 0) << 2 | (paramBoolean4 ? 1 : 0) << 1 | (paramBoolean1 ? 1 : 0) << 0;
/*     */   }
/*     */ 
/*     */   private static void enqueueBlit(RenderQueue paramRenderQueue, SurfaceData paramSurfaceData1, SurfaceData paramSurfaceData2, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4)
/*     */   {
/* 227 */     RenderBuffer localRenderBuffer = paramRenderQueue.getBuffer();
/* 228 */     paramRenderQueue.ensureCapacityAndAlignment(72, 24);
/* 229 */     localRenderBuffer.putInt(31);
/* 230 */     localRenderBuffer.putInt(paramInt1);
/* 231 */     localRenderBuffer.putInt(paramInt2).putInt(paramInt3);
/* 232 */     localRenderBuffer.putInt(paramInt4).putInt(paramInt5);
/* 233 */     localRenderBuffer.putDouble(paramDouble1).putDouble(paramDouble2);
/* 234 */     localRenderBuffer.putDouble(paramDouble3).putDouble(paramDouble4);
/* 235 */     localRenderBuffer.putLong(paramSurfaceData1.getNativeOps());
/* 236 */     localRenderBuffer.putLong(paramSurfaceData2.getNativeOps());
/*     */   }
/*     */ 
/*     */   static void Blit(SurfaceData paramSurfaceData1, SurfaceData paramSurfaceData2, Composite paramComposite, Region paramRegion, AffineTransform paramAffineTransform, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, int paramInt6, boolean paramBoolean)
/*     */   {
/* 248 */     int i = 0;
/* 249 */     if (paramSurfaceData1.getTransparency() == 1) {
/* 250 */       i |= 1;
/*     */     }
/*     */ 
/* 253 */     OGLRenderQueue localOGLRenderQueue = OGLRenderQueue.getInstance();
/* 254 */     localOGLRenderQueue.lock();
/*     */     try
/*     */     {
/* 259 */       localOGLRenderQueue.addReference(paramSurfaceData1);
/*     */ 
/* 261 */       OGLSurfaceData localOGLSurfaceData = (OGLSurfaceData)paramSurfaceData2;
/* 262 */       if (paramBoolean)
/*     */       {
/* 265 */         OGLGraphicsConfig localOGLGraphicsConfig = localOGLSurfaceData.getOGLGraphicsConfig();
/* 266 */         OGLContext.setScratchSurface(localOGLGraphicsConfig);
/*     */       } else {
/* 268 */         OGLContext.validateContext(localOGLSurfaceData, localOGLSurfaceData, paramRegion, paramComposite, paramAffineTransform, null, null, i);
/*     */       }
/*     */ 
/* 273 */       int j = createPackedParams(false, paramBoolean, false, paramAffineTransform != null, paramInt1, paramInt6);
/*     */ 
/* 276 */       enqueueBlit(localOGLRenderQueue, paramSurfaceData1, paramSurfaceData2, j, paramInt2, paramInt3, paramInt4, paramInt5, paramDouble1, paramDouble2, paramDouble3, paramDouble4);
/*     */ 
/* 283 */       localOGLRenderQueue.flushNow();
/*     */     } finally {
/* 285 */       localOGLRenderQueue.unlock();
/*     */     }
/*     */   }
/*     */ 
/*     */   static void IsoBlit(SurfaceData paramSurfaceData1, SurfaceData paramSurfaceData2, BufferedImage paramBufferedImage, BufferedImageOp paramBufferedImageOp, Composite paramComposite, Region paramRegion, AffineTransform paramAffineTransform, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, boolean paramBoolean)
/*     */   {
/* 305 */     int i = 0;
/* 306 */     if (paramSurfaceData1.getTransparency() == 1) {
/* 307 */       i |= 1;
/*     */     }
/*     */ 
/* 310 */     OGLRenderQueue localOGLRenderQueue = OGLRenderQueue.getInstance();
/* 311 */     localOGLRenderQueue.lock();
/*     */     try {
/* 313 */       OGLSurfaceData localOGLSurfaceData1 = (OGLSurfaceData)paramSurfaceData1;
/* 314 */       OGLSurfaceData localOGLSurfaceData2 = (OGLSurfaceData)paramSurfaceData2;
/* 315 */       int j = localOGLSurfaceData1.getType();
/*     */       boolean bool;
/*     */       OGLSurfaceData localOGLSurfaceData3;
/* 318 */       if (j == 3)
/*     */       {
/* 322 */         bool = false;
/* 323 */         localOGLSurfaceData3 = localOGLSurfaceData2;
/*     */       }
/*     */       else
/*     */       {
/* 328 */         bool = true;
/* 329 */         if (j == 5)
/* 330 */           localOGLSurfaceData3 = localOGLSurfaceData2;
/*     */         else {
/* 332 */           localOGLSurfaceData3 = localOGLSurfaceData1;
/*     */         }
/*     */       }
/*     */ 
/* 336 */       OGLContext.validateContext(localOGLSurfaceData3, localOGLSurfaceData2, paramRegion, paramComposite, paramAffineTransform, null, null, i);
/*     */ 
/* 340 */       if (paramBufferedImageOp != null) {
/* 341 */         OGLBufImgOps.enableBufImgOp(localOGLRenderQueue, localOGLSurfaceData1, paramBufferedImage, paramBufferedImageOp);
/*     */       }
/*     */ 
/* 344 */       int k = createPackedParams(true, paramBoolean, bool, paramAffineTransform != null, paramInt1, 0);
/*     */ 
/* 347 */       enqueueBlit(localOGLRenderQueue, paramSurfaceData1, paramSurfaceData2, k, paramInt2, paramInt3, paramInt4, paramInt5, paramDouble1, paramDouble2, paramDouble3, paramDouble4);
/*     */ 
/* 352 */       if (paramBufferedImageOp != null) {
/* 353 */         OGLBufImgOps.disableBufImgOp(localOGLRenderQueue, paramBufferedImageOp);
/*     */       }
/*     */ 
/* 356 */       if ((bool) && (localOGLSurfaceData2.isOnScreen()))
/*     */       {
/* 360 */         localOGLRenderQueue.flushNow();
/*     */       }
/*     */     } finally {
/* 363 */       localOGLRenderQueue.unlock();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.java2d.opengl.OGLBlitLoops
 * JD-Core Version:    0.6.2
 */