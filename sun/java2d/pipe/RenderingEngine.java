/*     */ package sun.java2d.pipe;
/*     */ 
/*     */ import java.awt.BasicStroke;
/*     */ import java.awt.Shape;
/*     */ import java.awt.geom.AffineTransform;
/*     */ import java.awt.geom.PathIterator;
/*     */ import java.io.PrintStream;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.ServiceLoader;
/*     */ import sun.awt.geom.PathConsumer2D;
/*     */ import sun.security.action.GetPropertyAction;
/*     */ 
/*     */ public abstract class RenderingEngine
/*     */ {
/*     */   private static RenderingEngine reImpl;
/*     */ 
/*     */   public static synchronized RenderingEngine getInstance()
/*     */   {
/* 116 */     if (reImpl != null) {
/* 117 */       return reImpl;
/*     */     }
/*     */ 
/* 120 */     reImpl = (RenderingEngine)AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public Object run()
/*     */       {
/* 124 */         String str = System.getProperty("sun.java2d.renderer", "sun.dc.DuctusRenderingEngine");
/*     */ 
/* 126 */         if (str.equals("sun.dc.DuctusRenderingEngine"))
/*     */           try {
/* 128 */             Class localClass = Class.forName("sun.dc.DuctusRenderingEngine");
/* 129 */             return localClass.newInstance();
/*     */           }
/*     */           catch (ClassNotFoundException localClassNotFoundException)
/*     */           {
/*     */           }
/*     */           catch (IllegalAccessException localIllegalAccessException)
/*     */           {
/*     */           }
/*     */           catch (InstantiationException localInstantiationException) {
/*     */           }
/* 139 */         ServiceLoader localServiceLoader = ServiceLoader.loadInstalled(RenderingEngine.class);
/*     */ 
/* 142 */         Object localObject = null;
/*     */ 
/* 144 */         for (RenderingEngine localRenderingEngine : localServiceLoader) {
/* 145 */           localObject = localRenderingEngine;
/* 146 */           if (localRenderingEngine.getClass().getName().equals(str)) {
/*     */             break;
/*     */           }
/*     */         }
/* 150 */         return localObject;
/*     */       }
/*     */     });
/* 154 */     if (reImpl == null) {
/* 155 */       throw new InternalError("No RenderingEngine module found");
/*     */     }
/*     */ 
/* 158 */     GetPropertyAction localGetPropertyAction = new GetPropertyAction("sun.java2d.renderer.trace");
/*     */ 
/* 160 */     String str = (String)AccessController.doPrivileged(localGetPropertyAction);
/* 161 */     if (str != null) {
/* 162 */       reImpl = new Tracer(reImpl);
/*     */     }
/*     */ 
/* 165 */     return reImpl;
/*     */   }
/*     */ 
/*     */   public abstract Shape createStrokedShape(Shape paramShape, float paramFloat1, int paramInt1, int paramInt2, float paramFloat2, float[] paramArrayOfFloat, float paramFloat3);
/*     */ 
/*     */   public abstract void strokeTo(Shape paramShape, AffineTransform paramAffineTransform, BasicStroke paramBasicStroke, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, PathConsumer2D paramPathConsumer2D);
/*     */ 
/*     */   public abstract AATileGenerator getAATileGenerator(Shape paramShape, AffineTransform paramAffineTransform, Region paramRegion, BasicStroke paramBasicStroke, boolean paramBoolean1, boolean paramBoolean2, int[] paramArrayOfInt);
/*     */ 
/*     */   public abstract AATileGenerator getAATileGenerator(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5, double paramDouble6, double paramDouble7, double paramDouble8, Region paramRegion, int[] paramArrayOfInt);
/*     */ 
/*     */   public abstract float getMinimumAAPenSize();
/*     */ 
/*     */   public static void feedConsumer(PathIterator paramPathIterator, PathConsumer2D paramPathConsumer2D)
/*     */   {
/* 363 */     float[] arrayOfFloat = new float[6];
/* 364 */     while (!paramPathIterator.isDone()) {
/* 365 */       switch (paramPathIterator.currentSegment(arrayOfFloat)) {
/*     */       case 0:
/* 367 */         paramPathConsumer2D.moveTo(arrayOfFloat[0], arrayOfFloat[1]);
/* 368 */         break;
/*     */       case 1:
/* 370 */         paramPathConsumer2D.lineTo(arrayOfFloat[0], arrayOfFloat[1]);
/* 371 */         break;
/*     */       case 2:
/* 373 */         paramPathConsumer2D.quadTo(arrayOfFloat[0], arrayOfFloat[1], arrayOfFloat[2], arrayOfFloat[3]);
/*     */ 
/* 375 */         break;
/*     */       case 3:
/* 377 */         paramPathConsumer2D.curveTo(arrayOfFloat[0], arrayOfFloat[1], arrayOfFloat[2], arrayOfFloat[3], arrayOfFloat[4], arrayOfFloat[5]);
/*     */ 
/* 380 */         break;
/*     */       case 4:
/* 382 */         paramPathConsumer2D.closePath();
/*     */       }
/*     */ 
/* 385 */       paramPathIterator.next();
/*     */     }
/*     */   }
/*     */ 
/*     */   static class Tracer extends RenderingEngine {
/*     */     RenderingEngine target;
/*     */     String name;
/*     */ 
/* 394 */     public Tracer(RenderingEngine paramRenderingEngine) { this.target = paramRenderingEngine;
/* 395 */       this.name = paramRenderingEngine.getClass().getName();
/*     */     }
/*     */ 
/*     */     public Shape createStrokedShape(Shape paramShape, float paramFloat1, int paramInt1, int paramInt2, float paramFloat2, float[] paramArrayOfFloat, float paramFloat3)
/*     */     {
/* 406 */       System.out.println(this.name + ".createStrokedShape(" + paramShape.getClass().getName() + ", " + "width = " + paramFloat1 + ", " + "caps = " + paramInt1 + ", " + "join = " + paramInt2 + ", " + "miter = " + paramFloat2 + ", " + "dashes = " + paramArrayOfFloat + ", " + "dashphase = " + paramFloat3 + ")");
/*     */ 
/* 414 */       return this.target.createStrokedShape(paramShape, paramFloat1, paramInt1, paramInt2, paramFloat2, paramArrayOfFloat, paramFloat3);
/*     */     }
/*     */ 
/*     */     public void strokeTo(Shape paramShape, AffineTransform paramAffineTransform, BasicStroke paramBasicStroke, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, PathConsumer2D paramPathConsumer2D)
/*     */     {
/* 427 */       System.out.println(this.name + ".strokeTo(" + paramShape.getClass().getName() + ", " + paramAffineTransform + ", " + paramBasicStroke + ", " + (paramBoolean1 ? "thin" : "wide") + ", " + (paramBoolean2 ? "normalized" : "pure") + ", " + (paramBoolean3 ? "AA" : "non-AA") + ", " + paramPathConsumer2D.getClass().getName() + ")");
/*     */ 
/* 435 */       this.target.strokeTo(paramShape, paramAffineTransform, paramBasicStroke, paramBoolean1, paramBoolean2, paramBoolean3, paramPathConsumer2D);
/*     */     }
/*     */ 
/*     */     public float getMinimumAAPenSize() {
/* 439 */       System.out.println(this.name + ".getMinimumAAPenSize()");
/* 440 */       return this.target.getMinimumAAPenSize();
/*     */     }
/*     */ 
/*     */     public AATileGenerator getAATileGenerator(Shape paramShape, AffineTransform paramAffineTransform, Region paramRegion, BasicStroke paramBasicStroke, boolean paramBoolean1, boolean paramBoolean2, int[] paramArrayOfInt)
/*     */     {
/* 451 */       System.out.println(this.name + ".getAATileGenerator(" + paramShape.getClass().getName() + ", " + paramAffineTransform + ", " + paramRegion + ", " + paramBasicStroke + ", " + (paramBoolean1 ? "thin" : "wide") + ", " + (paramBoolean2 ? "normalized" : "pure") + ")");
/*     */ 
/* 458 */       return this.target.getAATileGenerator(paramShape, paramAffineTransform, paramRegion, paramBasicStroke, paramBoolean1, paramBoolean2, paramArrayOfInt);
/*     */     }
/*     */ 
/*     */     public AATileGenerator getAATileGenerator(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5, double paramDouble6, double paramDouble7, double paramDouble8, Region paramRegion, int[] paramArrayOfInt)
/*     */     {
/* 469 */       System.out.println(this.name + ".getAATileGenerator(" + paramDouble1 + ", " + paramDouble2 + ", " + paramDouble3 + ", " + paramDouble4 + ", " + paramDouble5 + ", " + paramDouble6 + ", " + paramDouble7 + ", " + paramDouble8 + ", " + paramRegion + ")");
/*     */ 
/* 475 */       return this.target.getAATileGenerator(paramDouble1, paramDouble2, paramDouble3, paramDouble4, paramDouble5, paramDouble6, paramDouble7, paramDouble8, paramRegion, paramArrayOfInt);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.java2d.pipe.RenderingEngine
 * JD-Core Version:    0.6.2
 */