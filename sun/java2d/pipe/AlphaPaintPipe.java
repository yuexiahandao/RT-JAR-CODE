/*     */ package sun.java2d.pipe;
/*     */ 
/*     */ import java.awt.Paint;
/*     */ import java.awt.PaintContext;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.Shape;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.awt.image.ColorModel;
/*     */ import java.awt.image.Raster;
/*     */ import java.awt.image.WritableRaster;
/*     */ import java.lang.ref.WeakReference;
/*     */ import sun.awt.image.BufImgSurfaceData;
/*     */ import sun.java2d.SunGraphics2D;
/*     */ import sun.java2d.SurfaceData;
/*     */ import sun.java2d.loops.Blit;
/*     */ import sun.java2d.loops.CompositeType;
/*     */ import sun.java2d.loops.MaskBlit;
/*     */ 
/*     */ public class AlphaPaintPipe
/*     */   implements CompositePipe
/*     */ {
/*     */   static WeakReference cachedLastRaster;
/*     */   static WeakReference cachedLastColorModel;
/*     */   static WeakReference cachedLastData;
/*     */   private static final int TILE_SIZE = 32;
/*     */ 
/*     */   public Object startSequence(SunGraphics2D paramSunGraphics2D, Shape paramShape, Rectangle paramRectangle, int[] paramArrayOfInt)
/*     */   {
/*  83 */     PaintContext localPaintContext = paramSunGraphics2D.paint.createContext(paramSunGraphics2D.getDeviceColorModel(), paramRectangle, paramShape.getBounds2D(), paramSunGraphics2D.cloneTransform(), paramSunGraphics2D.getRenderingHints());
/*     */ 
/*  89 */     return new TileContext(paramSunGraphics2D, localPaintContext);
/*     */   }
/*     */ 
/*     */   public boolean needTile(Object paramObject, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
/*  93 */     return true;
/*     */   }
/*     */ 
/*     */   public void renderPathTile(Object paramObject, byte[] paramArrayOfByte, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
/*     */   {
/* 101 */     TileContext localTileContext = (TileContext)paramObject;
/* 102 */     PaintContext localPaintContext = localTileContext.paintCtxt;
/* 103 */     SunGraphics2D localSunGraphics2D = localTileContext.sunG2D;
/* 104 */     SurfaceData localSurfaceData1 = localTileContext.dstData;
/* 105 */     SurfaceData localSurfaceData2 = null;
/* 106 */     Object localObject1 = null;
/* 107 */     if ((localTileContext.lastData != null) && (localTileContext.lastRaster != null)) {
/* 108 */       localSurfaceData2 = (SurfaceData)localTileContext.lastData.get();
/* 109 */       localObject1 = (Raster)localTileContext.lastRaster.get();
/* 110 */       if ((localSurfaceData2 == null) || (localObject1 == null)) {
/* 111 */         localSurfaceData2 = null;
/* 112 */         localObject1 = null;
/*     */       }
/*     */     }
/* 115 */     ColorModel localColorModel = localTileContext.paintModel;
/*     */ 
/* 117 */     for (int i = 0; i < paramInt6; i += 32) {
/* 118 */       int j = paramInt4 + i;
/* 119 */       int k = Math.min(paramInt6 - i, 32);
/* 120 */       for (int m = 0; m < paramInt5; m += 32) {
/* 121 */         int n = paramInt3 + m;
/* 122 */         int i1 = Math.min(paramInt5 - m, 32);
/*     */ 
/* 124 */         Raster localRaster = localPaintContext.getRaster(n, j, i1, k);
/* 125 */         if ((localRaster.getMinX() != 0) || (localRaster.getMinY() != 0))
/* 126 */           localRaster = localRaster.createTranslatedChild(0, 0);
/*     */         Object localObject2;
/* 128 */         if (localObject1 != localRaster) {
/* 129 */           localObject1 = localRaster;
/* 130 */           localTileContext.lastRaster = new WeakReference(localObject1);
/*     */ 
/* 132 */           localObject2 = new BufferedImage(localColorModel, (WritableRaster)localRaster, localColorModel.isAlphaPremultiplied(), null);
/*     */ 
/* 137 */           localSurfaceData2 = BufImgSurfaceData.createData((BufferedImage)localObject2);
/* 138 */           localTileContext.lastData = new WeakReference(localSurfaceData2);
/* 139 */           localTileContext.lastMask = null;
/* 140 */           localTileContext.lastBlit = null;
/*     */         }
/*     */ 
/* 143 */         if (paramArrayOfByte == null) {
/* 144 */           if (localTileContext.lastBlit == null) {
/* 145 */             localObject2 = localSunGraphics2D.imageComp;
/* 146 */             if ((CompositeType.SrcOverNoEa.equals(localObject2)) && (localColorModel.getTransparency() == 1))
/*     */             {
/* 149 */               localObject2 = CompositeType.SrcNoEa;
/*     */             }
/* 151 */             localTileContext.lastBlit = Blit.getFromCache(localSurfaceData2.getSurfaceType(), (CompositeType)localObject2, localSurfaceData1.getSurfaceType());
/*     */           }
/*     */ 
/* 156 */           localTileContext.lastBlit.Blit(localSurfaceData2, localSurfaceData1, localSunGraphics2D.composite, null, 0, 0, n, j, i1, k);
/*     */         }
/*     */         else
/*     */         {
/* 160 */           if (localTileContext.lastMask == null) {
/* 161 */             localObject2 = localSunGraphics2D.imageComp;
/* 162 */             if ((CompositeType.SrcOverNoEa.equals(localObject2)) && (localColorModel.getTransparency() == 1))
/*     */             {
/* 165 */               localObject2 = CompositeType.SrcNoEa;
/*     */             }
/* 167 */             localTileContext.lastMask = MaskBlit.getFromCache(localSurfaceData2.getSurfaceType(), (CompositeType)localObject2, localSurfaceData1.getSurfaceType());
/*     */           }
/*     */ 
/* 173 */           int i2 = paramInt1 + i * paramInt2 + m;
/* 174 */           localTileContext.lastMask.MaskBlit(localSurfaceData2, localSurfaceData1, localSunGraphics2D.composite, null, 0, 0, n, j, i1, k, paramArrayOfByte, i2, paramInt2);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void skipTile(Object paramObject, int paramInt1, int paramInt2)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void endSequence(Object paramObject)
/*     */   {
/* 188 */     TileContext localTileContext = (TileContext)paramObject;
/* 189 */     if (localTileContext.paintCtxt != null) {
/* 190 */       localTileContext.paintCtxt.dispose();
/*     */     }
/* 192 */     synchronized (AlphaPaintPipe.class) {
/* 193 */       if (localTileContext.lastData != null) {
/* 194 */         cachedLastRaster = localTileContext.lastRaster;
/* 195 */         if ((cachedLastColorModel == null) || (cachedLastColorModel.get() != localTileContext.paintModel))
/*     */         {
/* 199 */           cachedLastColorModel = new WeakReference(localTileContext.paintModel);
/*     */         }
/*     */ 
/* 202 */         cachedLastData = localTileContext.lastData;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   static class TileContext
/*     */   {
/*     */     SunGraphics2D sunG2D;
/*     */     PaintContext paintCtxt;
/*     */     ColorModel paintModel;
/*     */     WeakReference lastRaster;
/*     */     WeakReference lastData;
/*     */     MaskBlit lastMask;
/*     */     Blit lastBlit;
/*     */     SurfaceData dstData;
/*     */ 
/*     */     public TileContext(SunGraphics2D paramSunGraphics2D, PaintContext paramPaintContext)
/*     */     {
/*  66 */       this.sunG2D = paramSunGraphics2D;
/*  67 */       this.paintCtxt = paramPaintContext;
/*  68 */       this.paintModel = paramPaintContext.getColorModel();
/*  69 */       this.dstData = paramSunGraphics2D.getSurfaceData();
/*  70 */       synchronized (AlphaPaintPipe.class) {
/*  71 */         if ((AlphaPaintPipe.cachedLastColorModel != null) && (AlphaPaintPipe.cachedLastColorModel.get() == this.paintModel))
/*     */         {
/*  74 */           this.lastRaster = AlphaPaintPipe.cachedLastRaster;
/*  75 */           this.lastData = AlphaPaintPipe.cachedLastData;
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.java2d.pipe.AlphaPaintPipe
 * JD-Core Version:    0.6.2
 */