/*    */ package sun.java2d.d3d;
/*    */ 
/*    */ import java.awt.Composite;
/*    */ import sun.java2d.SurfaceData;
/*    */ import sun.java2d.loops.CompositeType;
/*    */ import sun.java2d.loops.GraphicsPrimitive;
/*    */ import sun.java2d.loops.GraphicsPrimitiveMgr;
/*    */ import sun.java2d.loops.SurfaceType;
/*    */ import sun.java2d.pipe.BufferedMaskBlit;
/*    */ import sun.java2d.pipe.Region;
/*    */ 
/*    */ class D3DMaskBlit extends BufferedMaskBlit
/*    */ {
/*    */   static void register()
/*    */   {
/* 42 */     GraphicsPrimitive[] arrayOfGraphicsPrimitive = { new D3DMaskBlit(SurfaceType.IntArgb, CompositeType.SrcOver), new D3DMaskBlit(SurfaceType.IntArgbPre, CompositeType.SrcOver), new D3DMaskBlit(SurfaceType.IntRgb, CompositeType.SrcOver), new D3DMaskBlit(SurfaceType.IntRgb, CompositeType.SrcNoEa), new D3DMaskBlit(SurfaceType.IntBgr, CompositeType.SrcOver), new D3DMaskBlit(SurfaceType.IntBgr, CompositeType.SrcNoEa) };
/*    */ 
/* 50 */     GraphicsPrimitiveMgr.register(arrayOfGraphicsPrimitive);
/*    */   }
/*    */ 
/*    */   private D3DMaskBlit(SurfaceType paramSurfaceType, CompositeType paramCompositeType)
/*    */   {
/* 56 */     super(D3DRenderQueue.getInstance(), paramSurfaceType, paramCompositeType, D3DSurfaceData.D3DSurface);
/*    */   }
/*    */ 
/*    */   protected void validateContext(SurfaceData paramSurfaceData, Composite paramComposite, Region paramRegion)
/*    */   {
/* 64 */     D3DSurfaceData localD3DSurfaceData = (D3DSurfaceData)paramSurfaceData;
/* 65 */     D3DContext.validateContext(localD3DSurfaceData, localD3DSurfaceData, paramRegion, paramComposite, null, null, null, 0);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.java2d.d3d.D3DMaskBlit
 * JD-Core Version:    0.6.2
 */