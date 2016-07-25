/*    */ package sun.java2d;
/*    */ 
/*    */ import java.awt.GraphicsConfiguration;
/*    */ import sun.awt.image.BufImgVolatileSurfaceManager;
/*    */ import sun.awt.image.SunVolatileImage;
/*    */ import sun.awt.image.VolatileSurfaceManager;
/*    */ import sun.java2d.d3d.D3DGraphicsConfig;
/*    */ import sun.java2d.d3d.D3DVolatileSurfaceManager;
/*    */ import sun.java2d.opengl.WGLGraphicsConfig;
/*    */ import sun.java2d.opengl.WGLVolatileSurfaceManager;
/*    */ 
/*    */ public class WindowsSurfaceManagerFactory extends SurfaceManagerFactory
/*    */ {
/*    */   public VolatileSurfaceManager createVolatileManager(SunVolatileImage paramSunVolatileImage, Object paramObject)
/*    */   {
/* 55 */     GraphicsConfiguration localGraphicsConfiguration = paramSunVolatileImage.getGraphicsConfig();
/* 56 */     if ((localGraphicsConfiguration instanceof D3DGraphicsConfig))
/* 57 */       return new D3DVolatileSurfaceManager(paramSunVolatileImage, paramObject);
/* 58 */     if ((localGraphicsConfiguration instanceof WGLGraphicsConfig)) {
/* 59 */       return new WGLVolatileSurfaceManager(paramSunVolatileImage, paramObject);
/*    */     }
/* 61 */     return new BufImgVolatileSurfaceManager(paramSunVolatileImage, paramObject);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.java2d.WindowsSurfaceManagerFactory
 * JD-Core Version:    0.6.2
 */