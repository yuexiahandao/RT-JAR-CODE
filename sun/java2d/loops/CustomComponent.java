/*     */ package sun.java2d.loops;
/*     */ 
/*     */ import java.awt.Rectangle;
/*     */ import sun.java2d.SurfaceData;
/*     */ import sun.java2d.pipe.Region;
/*     */ 
/*     */ public final class CustomComponent
/*     */ {
/*     */   public static void register()
/*     */   {
/*  53 */     CustomComponent localCustomComponent = CustomComponent.class;
/*  54 */     GraphicsPrimitive[] arrayOfGraphicsPrimitive = { new GraphicsPrimitiveProxy(localCustomComponent, "OpaqueCopyAnyToArgb", Blit.methodSignature, Blit.primTypeID, SurfaceType.Any, CompositeType.SrcNoEa, SurfaceType.IntArgb), new GraphicsPrimitiveProxy(localCustomComponent, "OpaqueCopyArgbToAny", Blit.methodSignature, Blit.primTypeID, SurfaceType.IntArgb, CompositeType.SrcNoEa, SurfaceType.Any), new GraphicsPrimitiveProxy(localCustomComponent, "XorCopyArgbToAny", Blit.methodSignature, Blit.primTypeID, SurfaceType.IntArgb, CompositeType.Xor, SurfaceType.Any) };
/*     */ 
/*  74 */     GraphicsPrimitiveMgr.register(arrayOfGraphicsPrimitive);
/*     */   }
/*     */ 
/*     */   public static Region getRegionOfInterest(SurfaceData paramSurfaceData1, SurfaceData paramSurfaceData2, Region paramRegion, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
/*     */   {
/*  92 */     Region localRegion = Region.getInstanceXYWH(paramInt3, paramInt4, paramInt5, paramInt6);
/*  93 */     localRegion = localRegion.getIntersection(paramSurfaceData2.getBounds());
/*  94 */     Rectangle localRectangle = paramSurfaceData1.getBounds();
/*     */ 
/*  96 */     localRectangle.translate(paramInt3 - paramInt1, paramInt4 - paramInt2);
/*  97 */     localRegion = localRegion.getIntersection(localRectangle);
/*  98 */     if (paramRegion != null)
/*     */     {
/* 100 */       localRegion = localRegion.getIntersection(paramRegion);
/*     */     }
/* 102 */     return localRegion;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.java2d.loops.CustomComponent
 * JD-Core Version:    0.6.2
 */