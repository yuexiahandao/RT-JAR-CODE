/*     */ package sun.java2d;
/*     */ 
/*     */ import java.awt.GraphicsConfiguration;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.image.ColorModel;
/*     */ import java.awt.image.Raster;
/*     */ import sun.java2d.loops.SurfaceType;
/*     */ import sun.java2d.pipe.NullPipe;
/*     */ 
/*     */ public class NullSurfaceData extends SurfaceData
/*     */ {
/*  43 */   public static final SurfaceData theInstance = new NullSurfaceData();
/*     */ 
/*  66 */   private static final NullPipe nullpipe = new NullPipe();
/*     */ 
/*     */   private NullSurfaceData()
/*     */   {
/*  46 */     super(StateTrackable.State.IMMUTABLE, SurfaceType.Any, ColorModel.getRGBdefault());
/*     */   }
/*     */ 
/*     */   public void invalidate()
/*     */   {
/*     */   }
/*     */ 
/*     */   public SurfaceData getReplacement()
/*     */   {
/*  63 */     return this;
/*     */   }
/*     */ 
/*     */   public void validatePipe(SunGraphics2D paramSunGraphics2D)
/*     */   {
/*  69 */     paramSunGraphics2D.drawpipe = nullpipe;
/*  70 */     paramSunGraphics2D.fillpipe = nullpipe;
/*  71 */     paramSunGraphics2D.shapepipe = nullpipe;
/*  72 */     paramSunGraphics2D.textpipe = nullpipe;
/*  73 */     paramSunGraphics2D.imagepipe = nullpipe;
/*     */   }
/*     */ 
/*     */   public GraphicsConfiguration getDeviceConfiguration() {
/*  77 */     return null;
/*     */   }
/*     */ 
/*     */   public Raster getRaster(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/*  92 */     throw new InvalidPipeException("should be NOP");
/*     */   }
/*     */ 
/*     */   public boolean useTightBBoxes()
/*     */   {
/* 107 */     return false;
/*     */   }
/*     */ 
/*     */   public int pixelFor(int paramInt)
/*     */   {
/* 115 */     return paramInt;
/*     */   }
/*     */ 
/*     */   public int rgbFor(int paramInt)
/*     */   {
/* 123 */     return paramInt;
/*     */   }
/*     */ 
/*     */   public Rectangle getBounds()
/*     */   {
/* 130 */     return new Rectangle();
/*     */   }
/*     */ 
/*     */   protected void checkCustomComposite()
/*     */   {
/*     */   }
/*     */ 
/*     */   public boolean copyArea(SunGraphics2D paramSunGraphics2D, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
/*     */   {
/* 150 */     return true;
/*     */   }
/*     */ 
/*     */   public Object getDestination()
/*     */   {
/* 157 */     return null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.java2d.NullSurfaceData
 * JD-Core Version:    0.6.2
 */