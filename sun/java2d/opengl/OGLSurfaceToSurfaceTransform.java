/*     */ package sun.java2d.opengl;
/*     */ 
/*     */ import java.awt.Composite;
/*     */ import java.awt.geom.AffineTransform;
/*     */ import sun.java2d.SurfaceData;
/*     */ import sun.java2d.loops.CompositeType;
/*     */ import sun.java2d.loops.TransformBlit;
/*     */ import sun.java2d.pipe.Region;
/*     */ 
/*     */ class OGLSurfaceToSurfaceTransform extends TransformBlit
/*     */ {
/*     */   OGLSurfaceToSurfaceTransform()
/*     */   {
/* 418 */     super(OGLSurfaceData.OpenGLSurface, CompositeType.AnyAlpha, OGLSurfaceData.OpenGLSurface);
/*     */   }
/*     */ 
/*     */   public void Transform(SurfaceData paramSurfaceData1, SurfaceData paramSurfaceData2, Composite paramComposite, Region paramRegion, AffineTransform paramAffineTransform, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7)
/*     */   {
/* 429 */     OGLBlitLoops.IsoBlit(paramSurfaceData1, paramSurfaceData2, null, null, paramComposite, paramRegion, paramAffineTransform, paramInt1, paramInt2, paramInt3, paramInt2 + paramInt6, paramInt3 + paramInt7, paramInt4, paramInt5, paramInt4 + paramInt6, paramInt5 + paramInt7, false);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.java2d.opengl.OGLSurfaceToSurfaceTransform
 * JD-Core Version:    0.6.2
 */