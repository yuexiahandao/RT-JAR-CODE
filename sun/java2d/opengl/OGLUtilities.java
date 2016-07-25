/*     */ package sun.java2d.opengl;
/*     */ 
/*     */ import java.awt.Graphics;
/*     */ import java.awt.GraphicsConfiguration;
/*     */ import java.awt.Rectangle;
/*     */ import sun.java2d.SunGraphics2D;
/*     */ import sun.java2d.SurfaceData;
/*     */ import sun.java2d.pipe.Region;
/*     */ 
/*     */ class OGLUtilities
/*     */ {
/*     */   public static final int UNDEFINED = 0;
/*     */   public static final int WINDOW = 1;
/*     */   public static final int PBUFFER = 2;
/*     */   public static final int TEXTURE = 3;
/*     */   public static final int FLIP_BACKBUFFER = 4;
/*     */   public static final int FBOBJECT = 5;
/*     */ 
/*     */   public static boolean isQueueFlusherThread()
/*     */   {
/*  66 */     return OGLRenderQueue.isQueueFlusherThread();
/*     */   }
/*     */ 
/*     */   public static boolean invokeWithOGLContextCurrent(Graphics paramGraphics, Runnable paramRunnable)
/*     */   {
/*  96 */     OGLRenderQueue localOGLRenderQueue = OGLRenderQueue.getInstance();
/*  97 */     localOGLRenderQueue.lock();
/*     */     try {
/*  99 */       if (paramGraphics != null) {
/* 100 */         if (!(paramGraphics instanceof SunGraphics2D)) {
/* 101 */           return false;
/*     */         }
/* 103 */         SurfaceData localSurfaceData = ((SunGraphics2D)paramGraphics).surfaceData;
/* 104 */         if (!(localSurfaceData instanceof OGLSurfaceData)) {
/* 105 */           return false;
/*     */         }
/*     */ 
/* 109 */         OGLContext.validateContext((OGLSurfaceData)localSurfaceData);
/*     */       }
/*     */ 
/* 113 */       localOGLRenderQueue.flushAndInvokeNow(paramRunnable);
/*     */ 
/* 117 */       OGLContext.invalidateCurrentContext();
/*     */     } finally {
/* 119 */       localOGLRenderQueue.unlock();
/*     */     }
/*     */ 
/* 122 */     return true;
/*     */   }
/*     */ 
/*     */   public static boolean invokeWithOGLSharedContextCurrent(GraphicsConfiguration paramGraphicsConfiguration, Runnable paramRunnable)
/*     */   {
/* 152 */     if (!(paramGraphicsConfiguration instanceof OGLGraphicsConfig)) {
/* 153 */       return false;
/*     */     }
/*     */ 
/* 156 */     OGLRenderQueue localOGLRenderQueue = OGLRenderQueue.getInstance();
/* 157 */     localOGLRenderQueue.lock();
/*     */     try
/*     */     {
/* 160 */       OGLContext.setScratchSurface((OGLGraphicsConfig)paramGraphicsConfiguration);
/*     */ 
/* 163 */       localOGLRenderQueue.flushAndInvokeNow(paramRunnable);
/*     */ 
/* 167 */       OGLContext.invalidateCurrentContext();
/*     */     } finally {
/* 169 */       localOGLRenderQueue.unlock();
/*     */     }
/*     */ 
/* 172 */     return true;
/*     */   }
/*     */ 
/*     */   public static Rectangle getOGLViewport(Graphics paramGraphics, int paramInt1, int paramInt2)
/*     */   {
/* 199 */     if (!(paramGraphics instanceof SunGraphics2D)) {
/* 200 */       return null;
/*     */     }
/*     */ 
/* 203 */     SunGraphics2D localSunGraphics2D = (SunGraphics2D)paramGraphics;
/* 204 */     SurfaceData localSurfaceData = localSunGraphics2D.surfaceData;
/*     */ 
/* 209 */     int i = localSunGraphics2D.transX;
/* 210 */     int j = localSunGraphics2D.transY;
/*     */ 
/* 215 */     Rectangle localRectangle = localSurfaceData.getBounds();
/* 216 */     int k = i;
/* 217 */     int m = localRectangle.height - (j + paramInt2);
/*     */ 
/* 219 */     return new Rectangle(k, m, paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   public static Rectangle getOGLScissorBox(Graphics paramGraphics)
/*     */   {
/* 240 */     if (!(paramGraphics instanceof SunGraphics2D)) {
/* 241 */       return null;
/*     */     }
/*     */ 
/* 244 */     SunGraphics2D localSunGraphics2D = (SunGraphics2D)paramGraphics;
/* 245 */     SurfaceData localSurfaceData = localSunGraphics2D.surfaceData;
/* 246 */     Region localRegion = localSunGraphics2D.getCompClip();
/* 247 */     if (!localRegion.isRectangular())
/*     */     {
/* 251 */       return null;
/*     */     }
/*     */ 
/* 256 */     int i = localRegion.getLoX();
/* 257 */     int j = localRegion.getLoY();
/*     */ 
/* 260 */     int k = localRegion.getWidth();
/* 261 */     int m = localRegion.getHeight();
/*     */ 
/* 265 */     Rectangle localRectangle = localSurfaceData.getBounds();
/* 266 */     int n = i;
/* 267 */     int i1 = localRectangle.height - (j + m);
/*     */ 
/* 269 */     return new Rectangle(n, i1, k, m);
/*     */   }
/*     */ 
/*     */   public static Object getOGLSurfaceIdentifier(Graphics paramGraphics)
/*     */   {
/* 285 */     if (!(paramGraphics instanceof SunGraphics2D)) {
/* 286 */       return null;
/*     */     }
/* 288 */     return ((SunGraphics2D)paramGraphics).surfaceData;
/*     */   }
/*     */ 
/*     */   public static int getOGLSurfaceType(Graphics paramGraphics)
/*     */   {
/* 304 */     if (!(paramGraphics instanceof SunGraphics2D)) {
/* 305 */       return 0;
/*     */     }
/* 307 */     SurfaceData localSurfaceData = ((SunGraphics2D)paramGraphics).surfaceData;
/* 308 */     if (!(localSurfaceData instanceof OGLSurfaceData)) {
/* 309 */       return 0;
/*     */     }
/* 311 */     return ((OGLSurfaceData)localSurfaceData).getType();
/*     */   }
/*     */ 
/*     */   public static int getOGLTextureType(Graphics paramGraphics)
/*     */   {
/* 329 */     if (!(paramGraphics instanceof SunGraphics2D)) {
/* 330 */       return 0;
/*     */     }
/* 332 */     SurfaceData localSurfaceData = ((SunGraphics2D)paramGraphics).surfaceData;
/* 333 */     if (!(localSurfaceData instanceof OGLSurfaceData)) {
/* 334 */       return 0;
/*     */     }
/* 336 */     return ((OGLSurfaceData)localSurfaceData).getTextureTarget();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.java2d.opengl.OGLUtilities
 * JD-Core Version:    0.6.2
 */