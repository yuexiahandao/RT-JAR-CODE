/*     */ package sun.dc.pr;
/*     */ 
/*     */ import java.security.AccessController;
/*     */ import sun.awt.geom.PathConsumer2D;
/*     */ import sun.dc.path.FastPathProducer;
/*     */ import sun.dc.path.PathConsumer;
/*     */ import sun.dc.path.PathError;
/*     */ import sun.dc.path.PathException;
/*     */ import sun.security.action.LoadLibraryAction;
/*     */ 
/*     */ public class PathStroker
/*     */   implements PathConsumer
/*     */ {
/*     */   public static final int ROUND = 10;
/*     */   public static final int SQUARE = 20;
/*     */   public static final int BUTT = 30;
/*     */   public static final int BEVEL = 40;
/*     */   public static final int MITER = 50;
/*     */   private PathConsumer dest;
/*     */   private PathConsumer2D dest2D;
/*     */   private long cData;
/*     */ 
/*     */   public PathStroker(PathConsumer paramPathConsumer)
/*     */   {
/*  60 */     this.dest = paramPathConsumer;
/*  61 */     cInitialize(paramPathConsumer);
/*  62 */     reset();
/*     */   }
/*     */   public PathStroker(PathConsumer2D paramPathConsumer2D) {
/*  65 */     this.dest2D = paramPathConsumer2D;
/*  66 */     cInitialize2D(paramPathConsumer2D);
/*  67 */     reset();
/*     */   }
/*     */   public native void dispose();
/*     */ 
/*  71 */   protected static void classFinalize() throws Throwable { cClassFinalize(); }
/*     */ 
/*     */   public PathConsumer getConsumer()
/*     */   {
/*  75 */     return this.dest;
/*     */   }
/*     */ 
/*     */   public native void setPenDiameter(float paramFloat)
/*     */     throws PRError;
/*     */ 
/*     */   public native void setPenT4(float[] paramArrayOfFloat)
/*     */     throws PRError;
/*     */ 
/*     */   public native void setPenFitting(float paramFloat, int paramInt)
/*     */     throws PRError;
/*     */ 
/*     */   public native void setCaps(int paramInt)
/*     */     throws PRError;
/*     */ 
/*     */   public native void setCorners(int paramInt, float paramFloat)
/*     */     throws PRError;
/*     */ 
/*     */   public native void setOutputT6(float[] paramArrayOfFloat)
/*     */     throws PRError;
/*     */ 
/*     */   public native void setOutputConsumer(PathConsumer paramPathConsumer)
/*     */     throws PRError;
/*     */ 
/*     */   public native void reset();
/*     */ 
/*     */   public native void beginPath()
/*     */     throws PathError;
/*     */ 
/*     */   public native void beginSubpath(float paramFloat1, float paramFloat2)
/*     */     throws PathError;
/*     */ 
/*     */   public native void appendLine(float paramFloat1, float paramFloat2)
/*     */     throws PathError;
/*     */ 
/*     */   public native void appendQuadratic(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
/*     */     throws PathError;
/*     */ 
/*     */   public native void appendCubic(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6)
/*     */     throws PathError;
/*     */ 
/*     */   public native void closedSubpath()
/*     */     throws PathError;
/*     */ 
/*     */   public native void endPath()
/*     */     throws PathError, PathException;
/*     */ 
/*     */   public void useProxy(FastPathProducer paramFastPathProducer)
/*     */     throws PathError, PathException
/*     */   {
/* 326 */     paramFastPathProducer.sendTo(this);
/*     */   }
/*     */   public native long getCPathConsumer();
/*     */ 
/*     */   private static native void cClassInitialize();
/*     */ 
/*     */   private static native void cClassFinalize();
/*     */ 
/*     */   private native void cInitialize(PathConsumer paramPathConsumer);
/*     */ 
/*     */   private native void cInitialize2D(PathConsumer2D paramPathConsumer2D);
/*     */ 
/*     */   static {
/* 339 */     AccessController.doPrivileged(new LoadLibraryAction("dcpr"));
/*     */ 
/* 341 */     cClassInitialize();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.dc.pr.PathStroker
 * JD-Core Version:    0.6.2
 */