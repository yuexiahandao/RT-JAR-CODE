/*    */ package sun.java2d.d3d;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import sun.java2d.InvalidPipeException;
/*    */ import sun.java2d.SurfaceData;
/*    */ import sun.java2d.SurfaceDataProxy;
/*    */ import sun.java2d.loops.CompositeType;
/*    */ 
/*    */ public class D3DSurfaceDataProxy extends SurfaceDataProxy
/*    */ {
/*    */   D3DGraphicsConfig d3dgc;
/*    */   int transparency;
/*    */ 
/*    */   public static SurfaceDataProxy createProxy(SurfaceData paramSurfaceData, D3DGraphicsConfig paramD3DGraphicsConfig)
/*    */   {
/* 46 */     if ((paramSurfaceData instanceof D3DSurfaceData))
/*    */     {
/* 49 */       return UNCACHED;
/*    */     }
/*    */ 
/* 52 */     return new D3DSurfaceDataProxy(paramD3DGraphicsConfig, paramSurfaceData.getTransparency());
/*    */   }
/*    */ 
/*    */   public D3DSurfaceDataProxy(D3DGraphicsConfig paramD3DGraphicsConfig, int paramInt)
/*    */   {
/* 59 */     this.d3dgc = paramD3DGraphicsConfig;
/* 60 */     this.transparency = paramInt;
/*    */ 
/* 63 */     activateDisplayListener();
/*    */   }
/*    */ 
/*    */   public SurfaceData validateSurfaceData(SurfaceData paramSurfaceData1, SurfaceData paramSurfaceData2, int paramInt1, int paramInt2)
/*    */   {
/* 71 */     if ((paramSurfaceData2 == null) || (paramSurfaceData2.isSurfaceLost())) {
/*    */       try {
/* 73 */         paramSurfaceData2 = this.d3dgc.createManagedSurface(paramInt1, paramInt2, this.transparency);
/*    */       } catch (InvalidPipeException localInvalidPipeException) {
/* 75 */         this.d3dgc.getD3DDevice(); if (!D3DGraphicsDevice.isD3DAvailable()) {
/* 76 */           invalidate();
/* 77 */           flush();
/* 78 */           return null;
/*    */         }
/*    */       }
/*    */     }
/* 82 */     return paramSurfaceData2;
/*    */   }
/*    */ 
/*    */   public boolean isSupportedOperation(SurfaceData paramSurfaceData, int paramInt, CompositeType paramCompositeType, Color paramColor)
/*    */   {
/* 91 */     return (paramColor == null) || (this.transparency == 1);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.java2d.d3d.D3DSurfaceDataProxy
 * JD-Core Version:    0.6.2
 */