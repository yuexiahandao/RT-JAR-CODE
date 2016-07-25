/*     */ package sun.java2d.loops;
/*     */ 
/*     */ import java.awt.Composite;
/*     */ import java.awt.CompositeContext;
/*     */ import java.awt.RenderingHints;
/*     */ import java.awt.image.ColorModel;
/*     */ import java.awt.image.Raster;
/*     */ import java.awt.image.WritableRaster;
/*     */ import java.io.PrintStream;
/*     */ import java.lang.ref.WeakReference;
/*     */ import sun.java2d.SurfaceData;
/*     */ import sun.java2d.pipe.Region;
/*     */ import sun.java2d.pipe.SpanIterator;
/*     */ 
/*     */ public class Blit extends GraphicsPrimitive
/*     */ {
/*  53 */   public static final String methodSignature = "Blit(...)".toString();
/*     */ 
/*  55 */   public static final int primTypeID = makePrimTypeID();
/*     */ 
/*  57 */   private static RenderCache blitcache = new RenderCache(20);
/*     */ 
/*     */   public static Blit locate(SurfaceType paramSurfaceType1, CompositeType paramCompositeType, SurfaceType paramSurfaceType2)
/*     */   {
/*  63 */     return (Blit)GraphicsPrimitiveMgr.locate(primTypeID, paramSurfaceType1, paramCompositeType, paramSurfaceType2);
/*     */   }
/*     */ 
/*     */   public static Blit getFromCache(SurfaceType paramSurfaceType1, CompositeType paramCompositeType, SurfaceType paramSurfaceType2)
/*     */   {
/*  72 */     Object localObject = blitcache.get(paramSurfaceType1, paramCompositeType, paramSurfaceType2);
/*  73 */     if (localObject != null) {
/*  74 */       return (Blit)localObject;
/*     */     }
/*     */ 
/*  77 */     Blit localBlit = locate(paramSurfaceType1, paramCompositeType, paramSurfaceType2);
/*  78 */     if (localBlit == null) {
/*  79 */       System.out.println("blit loop not found for:");
/*  80 */       System.out.println("src:  " + paramSurfaceType1);
/*  81 */       System.out.println("comp: " + paramCompositeType);
/*  82 */       System.out.println("dst:  " + paramSurfaceType2);
/*     */     } else {
/*  84 */       blitcache.put(paramSurfaceType1, paramCompositeType, paramSurfaceType2, localBlit);
/*     */     }
/*  86 */     return localBlit;
/*     */   }
/*     */ 
/*     */   protected Blit(SurfaceType paramSurfaceType1, CompositeType paramCompositeType, SurfaceType paramSurfaceType2)
/*     */   {
/*  93 */     super(methodSignature, primTypeID, paramSurfaceType1, paramCompositeType, paramSurfaceType2);
/*     */   }
/*     */ 
/*     */   public Blit(long paramLong, SurfaceType paramSurfaceType1, CompositeType paramCompositeType, SurfaceType paramSurfaceType2)
/*     */   {
/* 101 */     super(paramLong, methodSignature, primTypeID, paramSurfaceType1, paramCompositeType, paramSurfaceType2);
/*     */   }
/*     */ 
/*     */   public native void Blit(SurfaceData paramSurfaceData1, SurfaceData paramSurfaceData2, Composite paramComposite, Region paramRegion, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6);
/*     */ 
/*     */   public GraphicsPrimitive makePrimitive(SurfaceType paramSurfaceType1, CompositeType paramCompositeType, SurfaceType paramSurfaceType2)
/*     */   {
/* 128 */     if (paramCompositeType.isDerivedFrom(CompositeType.Xor)) {
/* 129 */       GeneralXorBlit localGeneralXorBlit = new GeneralXorBlit(paramSurfaceType1, paramCompositeType, paramSurfaceType2);
/*     */ 
/* 132 */       setupGeneralBinaryOp(localGeneralXorBlit);
/* 133 */       return localGeneralXorBlit;
/* 134 */     }if (paramCompositeType.isDerivedFrom(CompositeType.AnyAlpha)) {
/* 135 */       return new GeneralMaskBlit(paramSurfaceType1, paramCompositeType, paramSurfaceType2);
/*     */     }
/* 137 */     return AnyBlit.instance;
/*     */   }
/*     */ 
/*     */   public GraphicsPrimitive traceWrap()
/*     */   {
/* 305 */     return new TraceBlit(this);
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/* 114 */     GraphicsPrimitiveMgr.registerGeneral(new Blit(null, null, null));
/*     */   }
/*     */ 
/*     */   private static class AnyBlit extends Blit
/*     */   {
/* 142 */     public static AnyBlit instance = new AnyBlit();
/*     */ 
/*     */     public AnyBlit() {
/* 145 */       super(CompositeType.Any, SurfaceType.Any);
/*     */     }
/*     */ 
/*     */     public void Blit(SurfaceData paramSurfaceData1, SurfaceData paramSurfaceData2, Composite paramComposite, Region paramRegion, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
/*     */     {
/* 156 */       ColorModel localColorModel1 = paramSurfaceData1.getColorModel();
/* 157 */       ColorModel localColorModel2 = paramSurfaceData2.getColorModel();
/*     */ 
/* 159 */       CompositeContext localCompositeContext = paramComposite.createContext(localColorModel1, localColorModel2, new RenderingHints(null));
/*     */ 
/* 161 */       Raster localRaster1 = paramSurfaceData1.getRaster(paramInt1, paramInt2, paramInt5, paramInt6);
/* 162 */       WritableRaster localWritableRaster1 = (WritableRaster)paramSurfaceData2.getRaster(paramInt3, paramInt4, paramInt5, paramInt6);
/*     */ 
/* 165 */       if (paramRegion == null) {
/* 166 */         paramRegion = Region.getInstanceXYWH(paramInt3, paramInt4, paramInt5, paramInt6);
/*     */       }
/* 168 */       int[] arrayOfInt = { paramInt3, paramInt4, paramInt3 + paramInt5, paramInt4 + paramInt6 };
/* 169 */       SpanIterator localSpanIterator = paramRegion.getSpanIterator(arrayOfInt);
/* 170 */       paramInt1 -= paramInt3;
/* 171 */       paramInt2 -= paramInt4;
/* 172 */       while (localSpanIterator.nextSpan(arrayOfInt)) {
/* 173 */         int i = arrayOfInt[2] - arrayOfInt[0];
/* 174 */         int j = arrayOfInt[3] - arrayOfInt[1];
/* 175 */         Raster localRaster2 = localRaster1.createChild(paramInt1 + arrayOfInt[0], paramInt2 + arrayOfInt[1], i, j, 0, 0, null);
/*     */ 
/* 177 */         WritableRaster localWritableRaster2 = localWritableRaster1.createWritableChild(arrayOfInt[0], arrayOfInt[1], i, j, 0, 0, null);
/*     */ 
/* 179 */         localCompositeContext.compose(localRaster2, localWritableRaster2, localWritableRaster2);
/*     */       }
/* 181 */       localCompositeContext.dispose();
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class GeneralMaskBlit extends Blit
/*     */   {
/*     */     MaskBlit performop;
/*     */ 
/*     */     public GeneralMaskBlit(SurfaceType paramSurfaceType1, CompositeType paramCompositeType, SurfaceType paramSurfaceType2)
/*     */     {
/* 192 */       super(paramCompositeType, paramSurfaceType2);
/* 193 */       this.performop = MaskBlit.locate(paramSurfaceType1, paramCompositeType, paramSurfaceType2);
/*     */     }
/*     */ 
/*     */     public void Blit(SurfaceData paramSurfaceData1, SurfaceData paramSurfaceData2, Composite paramComposite, Region paramRegion, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
/*     */     {
/* 204 */       this.performop.MaskBlit(paramSurfaceData1, paramSurfaceData2, paramComposite, paramRegion, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, null, 0, 0);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class GeneralXorBlit extends Blit
/*     */     implements GraphicsPrimitive.GeneralBinaryOp
/*     */   {
/*     */     Blit convertsrc;
/*     */     Blit convertdst;
/*     */     Blit performop;
/*     */     Blit convertresult;
/*     */     WeakReference srcTmp;
/*     */     WeakReference dstTmp;
/*     */ 
/*     */     public GeneralXorBlit(SurfaceType paramSurfaceType1, CompositeType paramCompositeType, SurfaceType paramSurfaceType2)
/*     */     {
/* 227 */       super(paramCompositeType, paramSurfaceType2);
/*     */     }
/*     */ 
/*     */     public void setPrimitives(Blit paramBlit1, Blit paramBlit2, GraphicsPrimitive paramGraphicsPrimitive, Blit paramBlit3)
/*     */     {
/* 235 */       this.convertsrc = paramBlit1;
/* 236 */       this.convertdst = paramBlit2;
/* 237 */       this.performop = ((Blit)paramGraphicsPrimitive);
/* 238 */       this.convertresult = paramBlit3;
/*     */     }
/*     */ 
/*     */     public synchronized void Blit(SurfaceData paramSurfaceData1, SurfaceData paramSurfaceData2, Composite paramComposite, Region paramRegion, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
/*     */     {
/*     */       SurfaceData localSurfaceData1;
/*     */       int i;
/*     */       int j;
/*     */       SurfaceData localSurfaceData3;
/* 253 */       if (this.convertsrc == null) {
/* 254 */         localSurfaceData1 = paramSurfaceData1;
/* 255 */         i = paramInt1;
/* 256 */         j = paramInt2;
/*     */       } else {
/* 258 */         localSurfaceData3 = null;
/* 259 */         if (this.srcTmp != null) {
/* 260 */           localSurfaceData3 = (SurfaceData)this.srcTmp.get();
/*     */         }
/* 262 */         localSurfaceData1 = convertFrom(this.convertsrc, paramSurfaceData1, paramInt1, paramInt2, paramInt5, paramInt6, localSurfaceData3);
/*     */ 
/* 264 */         i = 0;
/* 265 */         j = 0;
/* 266 */         if (localSurfaceData1 != localSurfaceData3)
/* 267 */           this.srcTmp = new WeakReference(localSurfaceData1);
/*     */       }
/*     */       SurfaceData localSurfaceData2;
/*     */       int k;
/*     */       int m;
/*     */       Region localRegion;
/* 271 */       if (this.convertdst == null) {
/* 272 */         localSurfaceData2 = paramSurfaceData2;
/* 273 */         k = paramInt3;
/* 274 */         m = paramInt4;
/* 275 */         localRegion = paramRegion;
/*     */       }
/*     */       else {
/* 278 */         localSurfaceData3 = null;
/* 279 */         if (this.dstTmp != null) {
/* 280 */           localSurfaceData3 = (SurfaceData)this.dstTmp.get();
/*     */         }
/* 282 */         localSurfaceData2 = convertFrom(this.convertdst, paramSurfaceData2, paramInt3, paramInt4, paramInt5, paramInt6, localSurfaceData3);
/*     */ 
/* 284 */         k = 0;
/* 285 */         m = 0;
/* 286 */         localRegion = null;
/* 287 */         if (localSurfaceData2 != localSurfaceData3) {
/* 288 */           this.dstTmp = new WeakReference(localSurfaceData2);
/*     */         }
/*     */       }
/*     */ 
/* 292 */       this.performop.Blit(localSurfaceData1, localSurfaceData2, paramComposite, localRegion, i, j, k, m, paramInt5, paramInt6);
/*     */ 
/* 296 */       if (this.convertresult != null)
/*     */       {
/* 298 */         convertTo(this.convertresult, localSurfaceData2, paramSurfaceData2, paramRegion, paramInt3, paramInt4, paramInt5, paramInt6);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class TraceBlit extends Blit
/*     */   {
/*     */     Blit target;
/*     */ 
/*     */     public TraceBlit(Blit paramBlit)
/*     */     {
/* 312 */       super(paramBlit.getCompositeType(), paramBlit.getDestType());
/*     */ 
/* 315 */       this.target = paramBlit;
/*     */     }
/*     */ 
/*     */     public GraphicsPrimitive traceWrap() {
/* 319 */       return this;
/*     */     }
/*     */ 
/*     */     public void Blit(SurfaceData paramSurfaceData1, SurfaceData paramSurfaceData2, Composite paramComposite, Region paramRegion, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
/*     */     {
/* 327 */       tracePrimitive(this.target);
/* 328 */       this.target.Blit(paramSurfaceData1, paramSurfaceData2, paramComposite, paramRegion, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.java2d.loops.Blit
 * JD-Core Version:    0.6.2
 */