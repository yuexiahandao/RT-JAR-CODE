/*     */ package sun.java2d.loops;
/*     */ 
/*     */ import java.awt.Composite;
/*     */ import java.awt.image.BufferedImage;
/*     */ import sun.awt.image.BufImgSurfaceData;
/*     */ import sun.java2d.SunGraphics2D;
/*     */ import sun.java2d.SurfaceData;
/*     */ import sun.java2d.pipe.Region;
/*     */ 
/*     */ public class MaskFill extends GraphicsPrimitive
/*     */ {
/*  53 */   public static final String methodSignature = "MaskFill(...)".toString();
/*  54 */   public static final String fillPgramSignature = "FillAAPgram(...)".toString();
/*     */ 
/*  56 */   public static final String drawPgramSignature = "DrawAAPgram(...)".toString();
/*     */ 
/*  59 */   public static final int primTypeID = makePrimTypeID();
/*     */ 
/*  61 */   private static RenderCache fillcache = new RenderCache(10);
/*     */ 
/*     */   public static MaskFill locate(SurfaceType paramSurfaceType1, CompositeType paramCompositeType, SurfaceType paramSurfaceType2)
/*     */   {
/*  67 */     return (MaskFill)GraphicsPrimitiveMgr.locate(primTypeID, paramSurfaceType1, paramCompositeType, paramSurfaceType2);
/*     */   }
/*     */ 
/*     */   public static MaskFill locatePrim(SurfaceType paramSurfaceType1, CompositeType paramCompositeType, SurfaceType paramSurfaceType2)
/*     */   {
/*  76 */     return (MaskFill)GraphicsPrimitiveMgr.locatePrim(primTypeID, paramSurfaceType1, paramCompositeType, paramSurfaceType2);
/*     */   }
/*     */ 
/*     */   public static MaskFill getFromCache(SurfaceType paramSurfaceType1, CompositeType paramCompositeType, SurfaceType paramSurfaceType2)
/*     */   {
/*  89 */     Object localObject = fillcache.get(paramSurfaceType1, paramCompositeType, paramSurfaceType2);
/*  90 */     if (localObject != null) {
/*  91 */       return (MaskFill)localObject;
/*     */     }
/*  93 */     MaskFill localMaskFill = locatePrim(paramSurfaceType1, paramCompositeType, paramSurfaceType2);
/*  94 */     if (localMaskFill != null) {
/*  95 */       fillcache.put(paramSurfaceType1, paramCompositeType, paramSurfaceType2, localMaskFill);
/*     */     }
/*  97 */     return localMaskFill;
/*     */   }
/*     */ 
/*     */   protected MaskFill(String paramString, SurfaceType paramSurfaceType1, CompositeType paramCompositeType, SurfaceType paramSurfaceType2)
/*     */   {
/* 105 */     super(paramString, primTypeID, paramSurfaceType1, paramCompositeType, paramSurfaceType2);
/*     */   }
/*     */ 
/*     */   protected MaskFill(SurfaceType paramSurfaceType1, CompositeType paramCompositeType, SurfaceType paramSurfaceType2)
/*     */   {
/* 112 */     super(methodSignature, primTypeID, paramSurfaceType1, paramCompositeType, paramSurfaceType2);
/*     */   }
/*     */ 
/*     */   public MaskFill(long paramLong, SurfaceType paramSurfaceType1, CompositeType paramCompositeType, SurfaceType paramSurfaceType2)
/*     */   {
/* 120 */     super(paramLong, methodSignature, primTypeID, paramSurfaceType1, paramCompositeType, paramSurfaceType2);
/*     */   }
/*     */ 
/*     */   public native void MaskFill(SunGraphics2D paramSunGraphics2D, SurfaceData paramSurfaceData, Composite paramComposite, int paramInt1, int paramInt2, int paramInt3, int paramInt4, byte[] paramArrayOfByte, int paramInt5, int paramInt6);
/*     */ 
/*     */   public native void FillAAPgram(SunGraphics2D paramSunGraphics2D, SurfaceData paramSurfaceData, Composite paramComposite, double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5, double paramDouble6);
/*     */ 
/*     */   public native void DrawAAPgram(SunGraphics2D paramSunGraphics2D, SurfaceData paramSurfaceData, Composite paramComposite, double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5, double paramDouble6, double paramDouble7, double paramDouble8);
/*     */ 
/*     */   public boolean canDoParallelograms()
/*     */   {
/* 145 */     return getNativePrim() != 0L;
/*     */   }
/*     */ 
/*     */   public GraphicsPrimitive makePrimitive(SurfaceType paramSurfaceType1, CompositeType paramCompositeType, SurfaceType paramSurfaceType2)
/*     */   {
/* 156 */     if ((SurfaceType.OpaqueColor.equals(paramSurfaceType1)) || (SurfaceType.AnyColor.equals(paramSurfaceType1)))
/*     */     {
/* 159 */       if (CompositeType.Xor.equals(paramCompositeType)) {
/* 160 */         throw new InternalError("Cannot construct MaskFill for XOR mode");
/*     */       }
/*     */ 
/* 163 */       return new General(paramSurfaceType1, paramCompositeType, paramSurfaceType2);
/*     */     }
/*     */ 
/* 166 */     throw new InternalError("MaskFill can only fill with colors");
/*     */   }
/*     */ 
/*     */   public GraphicsPrimitive traceWrap()
/*     */   {
/* 213 */     return new TraceMaskFill(this);
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/* 149 */     GraphicsPrimitiveMgr.registerGeneral(new MaskFill(null, null, null));
/*     */   }
/*     */ 
/*     */   private static class General extends MaskFill
/*     */   {
/*     */     FillRect fillop;
/*     */     MaskBlit maskop;
/*     */ 
/*     */     public General(SurfaceType paramSurfaceType1, CompositeType paramCompositeType, SurfaceType paramSurfaceType2)
/*     */     {
/* 178 */       super(paramCompositeType, paramSurfaceType2);
/* 179 */       this.fillop = FillRect.locate(paramSurfaceType1, CompositeType.SrcNoEa, SurfaceType.IntArgb);
/*     */ 
/* 182 */       this.maskop = MaskBlit.locate(SurfaceType.IntArgb, paramCompositeType, paramSurfaceType2);
/*     */     }
/*     */ 
/*     */     public void MaskFill(SunGraphics2D paramSunGraphics2D, SurfaceData paramSurfaceData, Composite paramComposite, int paramInt1, int paramInt2, int paramInt3, int paramInt4, byte[] paramArrayOfByte, int paramInt5, int paramInt6)
/*     */     {
/* 191 */       BufferedImage localBufferedImage = new BufferedImage(paramInt3, paramInt4, 2);
/*     */ 
/* 193 */       SurfaceData localSurfaceData = BufImgSurfaceData.createData(localBufferedImage);
/*     */ 
/* 198 */       Region localRegion = paramSunGraphics2D.clipRegion;
/* 199 */       paramSunGraphics2D.clipRegion = null;
/* 200 */       int i = paramSunGraphics2D.pixel;
/* 201 */       paramSunGraphics2D.pixel = localSurfaceData.pixelFor(paramSunGraphics2D.getColor());
/* 202 */       this.fillop.FillRect(paramSunGraphics2D, localSurfaceData, 0, 0, paramInt3, paramInt4);
/* 203 */       paramSunGraphics2D.pixel = i;
/* 204 */       paramSunGraphics2D.clipRegion = localRegion;
/*     */ 
/* 206 */       this.maskop.MaskBlit(localSurfaceData, paramSurfaceData, paramComposite, null, 0, 0, paramInt1, paramInt2, paramInt3, paramInt4, paramArrayOfByte, paramInt5, paramInt6);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class TraceMaskFill extends MaskFill
/*     */   {
/*     */     MaskFill target;
/*     */     MaskFill fillPgramTarget;
/*     */     MaskFill drawPgramTarget;
/*     */ 
/*     */     public TraceMaskFill(MaskFill paramMaskFill)
/*     */     {
/* 222 */       super(paramMaskFill.getCompositeType(), paramMaskFill.getDestType());
/*     */ 
/* 225 */       this.target = paramMaskFill;
/* 226 */       this.fillPgramTarget = new MaskFill(fillPgramSignature, paramMaskFill.getSourceType(), paramMaskFill.getCompositeType(), paramMaskFill.getDestType());
/*     */ 
/* 230 */       this.drawPgramTarget = new MaskFill(drawPgramSignature, paramMaskFill.getSourceType(), paramMaskFill.getCompositeType(), paramMaskFill.getDestType());
/*     */     }
/*     */ 
/*     */     public GraphicsPrimitive traceWrap()
/*     */     {
/* 237 */       return this;
/*     */     }
/*     */ 
/*     */     public void MaskFill(SunGraphics2D paramSunGraphics2D, SurfaceData paramSurfaceData, Composite paramComposite, int paramInt1, int paramInt2, int paramInt3, int paramInt4, byte[] paramArrayOfByte, int paramInt5, int paramInt6)
/*     */     {
/* 245 */       tracePrimitive(this.target);
/* 246 */       this.target.MaskFill(paramSunGraphics2D, paramSurfaceData, paramComposite, paramInt1, paramInt2, paramInt3, paramInt4, paramArrayOfByte, paramInt5, paramInt6);
/*     */     }
/*     */ 
/*     */     public void FillAAPgram(SunGraphics2D paramSunGraphics2D, SurfaceData paramSurfaceData, Composite paramComposite, double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5, double paramDouble6)
/*     */     {
/* 256 */       tracePrimitive(this.fillPgramTarget);
/* 257 */       this.target.FillAAPgram(paramSunGraphics2D, paramSurfaceData, paramComposite, paramDouble1, paramDouble2, paramDouble3, paramDouble4, paramDouble5, paramDouble6);
/*     */     }
/*     */ 
/*     */     public void DrawAAPgram(SunGraphics2D paramSunGraphics2D, SurfaceData paramSurfaceData, Composite paramComposite, double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5, double paramDouble6, double paramDouble7, double paramDouble8)
/*     */     {
/* 268 */       tracePrimitive(this.drawPgramTarget);
/* 269 */       this.target.DrawAAPgram(paramSunGraphics2D, paramSurfaceData, paramComposite, paramDouble1, paramDouble2, paramDouble3, paramDouble4, paramDouble5, paramDouble6, paramDouble7, paramDouble8);
/*     */     }
/*     */ 
/*     */     public boolean canDoParallelograms()
/*     */     {
/* 274 */       return this.target.canDoParallelograms();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.java2d.loops.MaskFill
 * JD-Core Version:    0.6.2
 */