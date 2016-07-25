/*     */ package sun.java2d.pipe;
/*     */ 
/*     */ import java.awt.AlphaComposite;
/*     */ import java.awt.Composite;
/*     */ import java.awt.CompositeContext;
/*     */ import java.awt.Paint;
/*     */ import java.awt.PaintContext;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.RenderingHints;
/*     */ import java.awt.Shape;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.awt.image.ColorModel;
/*     */ import java.awt.image.Raster;
/*     */ import java.awt.image.WritableRaster;
/*     */ import sun.awt.image.BufImgSurfaceData;
/*     */ import sun.java2d.SunGraphics2D;
/*     */ import sun.java2d.SurfaceData;
/*     */ import sun.java2d.loops.Blit;
/*     */ import sun.java2d.loops.CompositeType;
/*     */ import sun.java2d.loops.MaskBlit;
/*     */ 
/*     */ public class GeneralCompositePipe
/*     */   implements CompositePipe
/*     */ {
/*     */   public Object startSequence(SunGraphics2D paramSunGraphics2D, Shape paramShape, Rectangle paramRectangle, int[] paramArrayOfInt)
/*     */   {
/*  64 */     RenderingHints localRenderingHints = paramSunGraphics2D.getRenderingHints();
/*  65 */     ColorModel localColorModel = paramSunGraphics2D.getDeviceColorModel();
/*  66 */     PaintContext localPaintContext = paramSunGraphics2D.paint.createContext(localColorModel, paramRectangle, paramShape.getBounds2D(), paramSunGraphics2D.cloneTransform(), localRenderingHints);
/*     */ 
/*  70 */     CompositeContext localCompositeContext = paramSunGraphics2D.composite.createContext(localPaintContext.getColorModel(), localColorModel, localRenderingHints);
/*     */ 
/*  73 */     return new TileContext(paramSunGraphics2D, localPaintContext, localCompositeContext, localColorModel);
/*     */   }
/*     */ 
/*     */   public boolean needTile(Object paramObject, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
/*  77 */     return true;
/*     */   }
/*     */ 
/*     */   public void renderPathTile(Object paramObject, byte[] paramArrayOfByte, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
/*     */   {
/*  87 */     TileContext localTileContext = (TileContext)paramObject;
/*  88 */     PaintContext localPaintContext = localTileContext.paintCtxt;
/*  89 */     CompositeContext localCompositeContext = localTileContext.compCtxt;
/*  90 */     SunGraphics2D localSunGraphics2D = localTileContext.sunG2D;
/*     */ 
/*  92 */     Raster localRaster1 = localPaintContext.getRaster(paramInt3, paramInt4, paramInt5, paramInt6);
/*  93 */     ColorModel localColorModel1 = localPaintContext.getColorModel();
/*     */ 
/*  99 */     SurfaceData localSurfaceData1 = localSunGraphics2D.getSurfaceData();
/* 100 */     Raster localRaster2 = localSurfaceData1.getRaster(paramInt3, paramInt4, paramInt5, paramInt6);
/*     */     WritableRaster localWritableRaster;
/*     */     Object localObject1;
/* 101 */     if (((localRaster2 instanceof WritableRaster)) && (paramArrayOfByte == null)) {
/* 102 */       localWritableRaster = (WritableRaster)localRaster2;
/* 103 */       localWritableRaster = localWritableRaster.createWritableChild(paramInt3, paramInt4, paramInt5, paramInt6, 0, 0, null);
/* 104 */       localObject1 = localWritableRaster;
/*     */     } else {
/* 106 */       localObject1 = localRaster2.createChild(paramInt3, paramInt4, paramInt5, paramInt6, 0, 0, null);
/* 107 */       localWritableRaster = ((Raster)localObject1).createCompatibleWritableRaster();
/*     */     }
/*     */ 
/* 110 */     localCompositeContext.compose(localRaster1, (Raster)localObject1, localWritableRaster);
/*     */ 
/* 112 */     if ((localRaster2 != localWritableRaster) && (localWritableRaster.getParent() != localRaster2))
/* 113 */       if (((localRaster2 instanceof WritableRaster)) && (paramArrayOfByte == null)) {
/* 114 */         ((WritableRaster)localRaster2).setDataElements(paramInt3, paramInt4, localWritableRaster);
/*     */       } else {
/* 116 */         ColorModel localColorModel2 = localSunGraphics2D.getDeviceColorModel();
/* 117 */         BufferedImage localBufferedImage = new BufferedImage(localColorModel2, localWritableRaster, localColorModel2.isAlphaPremultiplied(), null);
/*     */ 
/* 121 */         SurfaceData localSurfaceData2 = BufImgSurfaceData.createData(localBufferedImage);
/*     */         Object localObject2;
/* 122 */         if (paramArrayOfByte == null) {
/* 123 */           localObject2 = Blit.getFromCache(localSurfaceData2.getSurfaceType(), CompositeType.SrcNoEa, localSurfaceData1.getSurfaceType());
/*     */ 
/* 126 */           ((Blit)localObject2).Blit(localSurfaceData2, localSurfaceData1, AlphaComposite.Src, null, 0, 0, paramInt3, paramInt4, paramInt5, paramInt6);
/*     */         }
/*     */         else {
/* 129 */           localObject2 = MaskBlit.getFromCache(localSurfaceData2.getSurfaceType(), CompositeType.SrcNoEa, localSurfaceData1.getSurfaceType());
/*     */ 
/* 132 */           ((MaskBlit)localObject2).MaskBlit(localSurfaceData2, localSurfaceData1, AlphaComposite.Src, null, 0, 0, paramInt3, paramInt4, paramInt5, paramInt6, paramArrayOfByte, paramInt1, paramInt2);
/*     */         }
/*     */       }
/*     */   }
/*     */ 
/*     */   public void skipTile(Object paramObject, int paramInt1, int paramInt2)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void endSequence(Object paramObject)
/*     */   {
/* 145 */     TileContext localTileContext = (TileContext)paramObject;
/* 146 */     if (localTileContext.paintCtxt != null) {
/* 147 */       localTileContext.paintCtxt.dispose();
/*     */     }
/* 149 */     if (localTileContext.compCtxt != null)
/* 150 */       localTileContext.compCtxt.dispose();
/*     */   }
/*     */ 
/*     */   class TileContext
/*     */   {
/*     */     SunGraphics2D sunG2D;
/*     */     PaintContext paintCtxt;
/*     */     CompositeContext compCtxt;
/*     */     ColorModel compModel;
/*     */     Object pipeState;
/*     */ 
/*     */     public TileContext(SunGraphics2D paramPaintContext, PaintContext paramCompositeContext, CompositeContext paramColorModel, ColorModel arg5)
/*     */     {
/*  55 */       this.sunG2D = paramPaintContext;
/*  56 */       this.paintCtxt = paramCompositeContext;
/*  57 */       this.compCtxt = paramColorModel;
/*     */       Object localObject;
/*  58 */       this.compModel = localObject;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.java2d.pipe.GeneralCompositePipe
 * JD-Core Version:    0.6.2
 */