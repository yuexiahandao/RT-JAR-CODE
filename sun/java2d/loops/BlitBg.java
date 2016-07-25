/*     */ package sun.java2d.loops;
/*     */ 
/*     */ import java.awt.AlphaComposite;
/*     */ import java.awt.Color;
/*     */ import java.awt.Composite;
/*     */ import java.awt.Font;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.awt.image.ColorModel;
/*     */ import java.awt.image.WritableRaster;
/*     */ import java.io.PrintStream;
/*     */ import sun.awt.image.BufImgSurfaceData;
/*     */ import sun.java2d.SunGraphics2D;
/*     */ import sun.java2d.SurfaceData;
/*     */ import sun.java2d.pipe.Region;
/*     */ 
/*     */ public class BlitBg extends GraphicsPrimitive
/*     */ {
/*  57 */   public static final String methodSignature = "BlitBg(...)".toString();
/*     */ 
/*  59 */   public static final int primTypeID = makePrimTypeID();
/*     */ 
/*  61 */   private static RenderCache blitcache = new RenderCache(20);
/*     */ 
/*     */   public static BlitBg locate(SurfaceType paramSurfaceType1, CompositeType paramCompositeType, SurfaceType paramSurfaceType2)
/*     */   {
/*  67 */     return (BlitBg)GraphicsPrimitiveMgr.locate(primTypeID, paramSurfaceType1, paramCompositeType, paramSurfaceType2);
/*     */   }
/*     */ 
/*     */   public static BlitBg getFromCache(SurfaceType paramSurfaceType1, CompositeType paramCompositeType, SurfaceType paramSurfaceType2)
/*     */   {
/*  76 */     Object localObject = blitcache.get(paramSurfaceType1, paramCompositeType, paramSurfaceType2);
/*  77 */     if (localObject != null) {
/*  78 */       return (BlitBg)localObject;
/*     */     }
/*  80 */     BlitBg localBlitBg = locate(paramSurfaceType1, paramCompositeType, paramSurfaceType2);
/*  81 */     if (localBlitBg == null) {
/*  82 */       System.out.println("blitbg loop not found for:");
/*  83 */       System.out.println("src:  " + paramSurfaceType1);
/*  84 */       System.out.println("comp: " + paramCompositeType);
/*  85 */       System.out.println("dst:  " + paramSurfaceType2);
/*     */     } else {
/*  87 */       blitcache.put(paramSurfaceType1, paramCompositeType, paramSurfaceType2, localBlitBg);
/*     */     }
/*  89 */     return localBlitBg;
/*     */   }
/*     */ 
/*     */   protected BlitBg(SurfaceType paramSurfaceType1, CompositeType paramCompositeType, SurfaceType paramSurfaceType2)
/*     */   {
/*  96 */     super(methodSignature, primTypeID, paramSurfaceType1, paramCompositeType, paramSurfaceType2);
/*     */   }
/*     */ 
/*     */   public BlitBg(long paramLong, SurfaceType paramSurfaceType1, CompositeType paramCompositeType, SurfaceType paramSurfaceType2)
/*     */   {
/* 104 */     super(paramLong, methodSignature, primTypeID, paramSurfaceType1, paramCompositeType, paramSurfaceType2);
/*     */   }
/*     */ 
/*     */   public native void BlitBg(SurfaceData paramSurfaceData1, SurfaceData paramSurfaceData2, Composite paramComposite, Region paramRegion, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7);
/*     */ 
/*     */   public GraphicsPrimitive makePrimitive(SurfaceType paramSurfaceType1, CompositeType paramCompositeType, SurfaceType paramSurfaceType2)
/*     */   {
/* 131 */     return new General(paramSurfaceType1, paramCompositeType, paramSurfaceType2);
/*     */   }
/*     */ 
/*     */   public GraphicsPrimitive traceWrap()
/*     */   {
/* 188 */     return new TraceBlitBg(this);
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/* 118 */     GraphicsPrimitiveMgr.registerGeneral(new BlitBg(null, null, null));
/*     */   }
/*     */ 
/*     */   private static class General extends BlitBg
/*     */   {
/*     */     CompositeType compositeType;
/* 184 */     private static Font defaultFont = new Font("Dialog", 0, 12);
/*     */ 
/*     */     public General(SurfaceType paramSurfaceType1, CompositeType paramCompositeType, SurfaceType paramSurfaceType2)
/*     */     {
/* 141 */       super(paramCompositeType, paramSurfaceType2);
/* 142 */       this.compositeType = paramCompositeType;
/*     */     }
/*     */ 
/*     */     public void BlitBg(SurfaceData paramSurfaceData1, SurfaceData paramSurfaceData2, Composite paramComposite, Region paramRegion, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7)
/*     */     {
/* 155 */       ColorModel localColorModel = paramSurfaceData2.getColorModel();
/* 156 */       boolean bool1 = paramInt1 >>> 24 != 255;
/* 157 */       if ((!localColorModel.hasAlpha()) && (bool1)) {
/* 158 */         localColorModel = ColorModel.getRGBdefault();
/*     */       }
/* 160 */       WritableRaster localWritableRaster = localColorModel.createCompatibleWritableRaster(paramInt6, paramInt7);
/*     */ 
/* 162 */       boolean bool2 = localColorModel.isAlphaPremultiplied();
/* 163 */       BufferedImage localBufferedImage = new BufferedImage(localColorModel, localWritableRaster, bool2, null);
/*     */ 
/* 165 */       SurfaceData localSurfaceData = BufImgSurfaceData.createData(localBufferedImage);
/* 166 */       Color localColor = new Color(paramInt1, bool1);
/* 167 */       SunGraphics2D localSunGraphics2D = new SunGraphics2D(localSurfaceData, localColor, localColor, defaultFont);
/*     */ 
/* 169 */       FillRect localFillRect = FillRect.locate(SurfaceType.AnyColor, CompositeType.SrcNoEa, localSurfaceData.getSurfaceType());
/*     */ 
/* 172 */       Blit localBlit1 = Blit.getFromCache(paramSurfaceData1.getSurfaceType(), CompositeType.SrcOverNoEa, localSurfaceData.getSurfaceType());
/*     */ 
/* 175 */       Blit localBlit2 = Blit.getFromCache(localSurfaceData.getSurfaceType(), this.compositeType, paramSurfaceData2.getSurfaceType());
/*     */ 
/* 177 */       localFillRect.FillRect(localSunGraphics2D, localSurfaceData, 0, 0, paramInt6, paramInt7);
/* 178 */       localBlit1.Blit(paramSurfaceData1, localSurfaceData, AlphaComposite.SrcOver, null, paramInt2, paramInt3, 0, 0, paramInt6, paramInt7);
/*     */ 
/* 180 */       localBlit2.Blit(localSurfaceData, paramSurfaceData2, paramComposite, paramRegion, 0, 0, paramInt4, paramInt5, paramInt6, paramInt7);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class TraceBlitBg extends BlitBg
/*     */   {
/*     */     BlitBg target;
/*     */ 
/*     */     public TraceBlitBg(BlitBg paramBlitBg)
/*     */     {
/* 195 */       super(paramBlitBg.getCompositeType(), paramBlitBg.getDestType());
/*     */ 
/* 198 */       this.target = paramBlitBg;
/*     */     }
/*     */ 
/*     */     public GraphicsPrimitive traceWrap() {
/* 202 */       return this;
/*     */     }
/*     */ 
/*     */     public void BlitBg(SurfaceData paramSurfaceData1, SurfaceData paramSurfaceData2, Composite paramComposite, Region paramRegion, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7)
/*     */     {
/* 212 */       tracePrimitive(this.target);
/* 213 */       this.target.BlitBg(paramSurfaceData1, paramSurfaceData2, paramComposite, paramRegion, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramInt7);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.java2d.loops.BlitBg
 * JD-Core Version:    0.6.2
 */