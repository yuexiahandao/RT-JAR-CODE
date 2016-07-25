/*     */ package sun.java2d.loops;
/*     */ 
/*     */ import java.awt.Composite;
/*     */ import java.io.PrintStream;
/*     */ import java.lang.ref.WeakReference;
/*     */ import sun.java2d.SurfaceData;
/*     */ import sun.java2d.pipe.Region;
/*     */ 
/*     */ public class MaskBlit extends GraphicsPrimitive
/*     */ {
/*  51 */   public static final String methodSignature = "MaskBlit(...)".toString();
/*     */ 
/*  53 */   public static final int primTypeID = makePrimTypeID();
/*     */ 
/*  55 */   private static RenderCache blitcache = new RenderCache(20);
/*     */ 
/*     */   public static MaskBlit locate(SurfaceType paramSurfaceType1, CompositeType paramCompositeType, SurfaceType paramSurfaceType2)
/*     */   {
/*  61 */     return (MaskBlit)GraphicsPrimitiveMgr.locate(primTypeID, paramSurfaceType1, paramCompositeType, paramSurfaceType2);
/*     */   }
/*     */ 
/*     */   public static MaskBlit getFromCache(SurfaceType paramSurfaceType1, CompositeType paramCompositeType, SurfaceType paramSurfaceType2)
/*     */   {
/*  70 */     Object localObject = blitcache.get(paramSurfaceType1, paramCompositeType, paramSurfaceType2);
/*  71 */     if (localObject != null) {
/*  72 */       return (MaskBlit)localObject;
/*     */     }
/*  74 */     MaskBlit localMaskBlit = locate(paramSurfaceType1, paramCompositeType, paramSurfaceType2);
/*  75 */     if (localMaskBlit == null) {
/*  76 */       System.out.println("mask blit loop not found for:");
/*  77 */       System.out.println("src:  " + paramSurfaceType1);
/*  78 */       System.out.println("comp: " + paramCompositeType);
/*  79 */       System.out.println("dst:  " + paramSurfaceType2);
/*     */     } else {
/*  81 */       blitcache.put(paramSurfaceType1, paramCompositeType, paramSurfaceType2, localMaskBlit);
/*     */     }
/*  83 */     return localMaskBlit;
/*     */   }
/*     */ 
/*     */   protected MaskBlit(SurfaceType paramSurfaceType1, CompositeType paramCompositeType, SurfaceType paramSurfaceType2)
/*     */   {
/*  90 */     super(methodSignature, primTypeID, paramSurfaceType1, paramCompositeType, paramSurfaceType2);
/*     */   }
/*     */ 
/*     */   public MaskBlit(long paramLong, SurfaceType paramSurfaceType1, CompositeType paramCompositeType, SurfaceType paramSurfaceType2)
/*     */   {
/*  98 */     super(paramLong, methodSignature, primTypeID, paramSurfaceType1, paramCompositeType, paramSurfaceType2);
/*     */   }
/*     */ 
/*     */   public native void MaskBlit(SurfaceData paramSurfaceData1, SurfaceData paramSurfaceData2, Composite paramComposite, Region paramRegion, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, byte[] paramArrayOfByte, int paramInt7, int paramInt8);
/*     */ 
/*     */   public GraphicsPrimitive makePrimitive(SurfaceType paramSurfaceType1, CompositeType paramCompositeType, SurfaceType paramSurfaceType2)
/*     */   {
/* 127 */     if (CompositeType.Xor.equals(paramCompositeType)) {
/* 128 */       throw new InternalError("Cannot construct MaskBlit for XOR mode");
/*     */     }
/*     */ 
/* 132 */     General localGeneral = new General(paramSurfaceType1, paramCompositeType, paramSurfaceType2);
/* 133 */     setupGeneralBinaryOp(localGeneral);
/* 134 */     return localGeneral;
/*     */   }
/*     */ 
/*     */   public GraphicsPrimitive traceWrap()
/*     */   {
/* 232 */     return new TraceMaskBlit(this);
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/* 112 */     GraphicsPrimitiveMgr.registerGeneral(new MaskBlit(null, null, null));
/*     */   }
/*     */ 
/*     */   private static class General extends MaskBlit
/*     */     implements GraphicsPrimitive.GeneralBinaryOp
/*     */   {
/*     */     Blit convertsrc;
/*     */     Blit convertdst;
/*     */     MaskBlit performop;
/*     */     Blit convertresult;
/*     */     WeakReference srcTmp;
/*     */     WeakReference dstTmp;
/*     */ 
/*     */     public General(SurfaceType paramSurfaceType1, CompositeType paramCompositeType, SurfaceType paramSurfaceType2)
/*     */     {
/* 153 */       super(paramCompositeType, paramSurfaceType2);
/*     */     }
/*     */ 
/*     */     public void setPrimitives(Blit paramBlit1, Blit paramBlit2, GraphicsPrimitive paramGraphicsPrimitive, Blit paramBlit3)
/*     */     {
/* 161 */       this.convertsrc = paramBlit1;
/* 162 */       this.convertdst = paramBlit2;
/* 163 */       this.performop = ((MaskBlit)paramGraphicsPrimitive);
/* 164 */       this.convertresult = paramBlit3;
/*     */     }
/*     */ 
/*     */     public synchronized void MaskBlit(SurfaceData paramSurfaceData1, SurfaceData paramSurfaceData2, Composite paramComposite, Region paramRegion, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, byte[] paramArrayOfByte, int paramInt7, int paramInt8)
/*     */     {
/*     */       SurfaceData localSurfaceData1;
/*     */       int i;
/*     */       int j;
/*     */       SurfaceData localSurfaceData3;
/* 180 */       if (this.convertsrc == null) {
/* 181 */         localSurfaceData1 = paramSurfaceData1;
/* 182 */         i = paramInt1;
/* 183 */         j = paramInt2;
/*     */       } else {
/* 185 */         localSurfaceData3 = null;
/* 186 */         if (this.srcTmp != null) {
/* 187 */           localSurfaceData3 = (SurfaceData)this.srcTmp.get();
/*     */         }
/* 189 */         localSurfaceData1 = convertFrom(this.convertsrc, paramSurfaceData1, paramInt1, paramInt2, paramInt5, paramInt6, localSurfaceData3);
/*     */ 
/* 191 */         i = 0;
/* 192 */         j = 0;
/* 193 */         if (localSurfaceData1 != localSurfaceData3)
/* 194 */           this.srcTmp = new WeakReference(localSurfaceData1);
/*     */       }
/*     */       SurfaceData localSurfaceData2;
/*     */       int k;
/*     */       int m;
/*     */       Region localRegion;
/* 198 */       if (this.convertdst == null) {
/* 199 */         localSurfaceData2 = paramSurfaceData2;
/* 200 */         k = paramInt3;
/* 201 */         m = paramInt4;
/* 202 */         localRegion = paramRegion;
/*     */       }
/*     */       else {
/* 205 */         localSurfaceData3 = null;
/* 206 */         if (this.dstTmp != null) {
/* 207 */           localSurfaceData3 = (SurfaceData)this.dstTmp.get();
/*     */         }
/* 209 */         localSurfaceData2 = convertFrom(this.convertdst, paramSurfaceData2, paramInt3, paramInt4, paramInt5, paramInt6, localSurfaceData3);
/*     */ 
/* 211 */         k = 0;
/* 212 */         m = 0;
/* 213 */         localRegion = null;
/* 214 */         if (localSurfaceData2 != localSurfaceData3) {
/* 215 */           this.dstTmp = new WeakReference(localSurfaceData2);
/*     */         }
/*     */       }
/*     */ 
/* 219 */       this.performop.MaskBlit(localSurfaceData1, localSurfaceData2, paramComposite, localRegion, i, j, k, m, paramInt5, paramInt6, paramArrayOfByte, paramInt7, paramInt8);
/*     */ 
/* 223 */       if (this.convertresult != null)
/*     */       {
/* 225 */         convertTo(this.convertresult, localSurfaceData2, paramSurfaceData2, paramRegion, paramInt3, paramInt4, paramInt5, paramInt6);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class TraceMaskBlit extends MaskBlit
/*     */   {
/*     */     MaskBlit target;
/*     */ 
/*     */     public TraceMaskBlit(MaskBlit paramMaskBlit)
/*     */     {
/* 241 */       super(paramMaskBlit.getSourceType(), paramMaskBlit.getCompositeType(), paramMaskBlit.getDestType());
/*     */ 
/* 245 */       this.target = paramMaskBlit;
/*     */     }
/*     */ 
/*     */     public GraphicsPrimitive traceWrap() {
/* 249 */       return this;
/*     */     }
/*     */ 
/*     */     public void MaskBlit(SurfaceData paramSurfaceData1, SurfaceData paramSurfaceData2, Composite paramComposite, Region paramRegion, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, byte[] paramArrayOfByte, int paramInt7, int paramInt8)
/*     */     {
/* 258 */       tracePrimitive(this.target);
/* 259 */       this.target.MaskBlit(paramSurfaceData1, paramSurfaceData2, paramComposite, paramRegion, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramArrayOfByte, paramInt7, paramInt8);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.java2d.loops.MaskBlit
 * JD-Core Version:    0.6.2
 */