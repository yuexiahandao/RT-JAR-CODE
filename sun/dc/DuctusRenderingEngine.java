/*     */ package sun.dc;
/*     */ 
/*     */ import java.awt.BasicStroke;
/*     */ import java.awt.Shape;
/*     */ import java.awt.geom.AffineTransform;
/*     */ import java.awt.geom.Path2D.Float;
/*     */ import java.awt.geom.PathIterator;
/*     */ import java.io.PrintStream;
/*     */ import sun.awt.geom.PathConsumer2D;
/*     */ import sun.dc.path.FastPathProducer;
/*     */ import sun.dc.path.PathConsumer;
/*     */ import sun.dc.path.PathException;
/*     */ import sun.dc.pr.PRException;
/*     */ import sun.dc.pr.PathDasher;
/*     */ import sun.dc.pr.PathStroker;
/*     */ import sun.dc.pr.Rasterizer;
/*     */ import sun.java2d.pipe.AATileGenerator;
/*     */ import sun.java2d.pipe.Region;
/*     */ import sun.java2d.pipe.RenderingEngine;
/*     */ 
/*     */ public class DuctusRenderingEngine extends RenderingEngine
/*     */ {
/*     */   static final float PenUnits = 0.01F;
/*     */   static final int MinPenUnits = 100;
/*     */   static final int MinPenUnitsAA = 20;
/*     */   static final float MinPenSizeAA = 0.2F;
/*     */   static final float UPPER_BND = 1.701412E+038F;
/*     */   static final float LOWER_BND = -1.701412E+038F;
/*  56 */   private static final int[] RasterizerCaps = { 30, 10, 20 };
/*     */ 
/*  60 */   private static final int[] RasterizerCorners = { 50, 10, 40 };
/*     */   private static Rasterizer theRasterizer;
/*     */ 
/*     */   static float[] getTransformMatrix(AffineTransform paramAffineTransform)
/*     */   {
/*  65 */     float[] arrayOfFloat = new float[4];
/*  66 */     double[] arrayOfDouble = new double[6];
/*  67 */     paramAffineTransform.getMatrix(arrayOfDouble);
/*  68 */     for (int i = 0; i < 4; i++) {
/*  69 */       arrayOfFloat[i] = ((float)arrayOfDouble[i]);
/*     */     }
/*  71 */     return arrayOfFloat;
/*     */   }
/*     */ 
/*     */   public Shape createStrokedShape(Shape paramShape, float paramFloat1, int paramInt1, int paramInt2, float paramFloat2, float[] paramArrayOfFloat, float paramFloat3)
/*     */   {
/*  86 */     FillAdapter localFillAdapter = new FillAdapter();
/*  87 */     PathStroker localPathStroker = new PathStroker(localFillAdapter);
/*  88 */     PathDasher localPathDasher = null;
/*     */     try
/*     */     {
/*  93 */       localPathStroker.setPenDiameter(paramFloat1);
/*  94 */       localPathStroker.setPenT4(null);
/*  95 */       localPathStroker.setCaps(RasterizerCaps[paramInt1]);
/*  96 */       localPathStroker.setCorners(RasterizerCorners[paramInt2], paramFloat2);
/*     */       Object localObject1;
/*  97 */       if (paramArrayOfFloat != null) {
/*  98 */         localPathDasher = new PathDasher(localPathStroker);
/*  99 */         localPathDasher.setDash(paramArrayOfFloat, paramFloat3);
/* 100 */         localPathDasher.setDashT4(null);
/* 101 */         localObject1 = localPathDasher;
/*     */       } else {
/* 103 */         localObject1 = localPathStroker;
/*     */       }
/*     */ 
/* 106 */       feedConsumer((PathConsumer)localObject1, paramShape.getPathIterator(null));
/*     */     } finally {
/* 108 */       localPathStroker.dispose();
/* 109 */       if (localPathDasher != null) {
/* 110 */         localPathDasher.dispose();
/*     */       }
/*     */     }
/*     */ 
/* 114 */     return localFillAdapter.getShape();
/*     */   }
/*     */ 
/*     */   public void strokeTo(Shape paramShape, AffineTransform paramAffineTransform, BasicStroke paramBasicStroke, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, PathConsumer2D paramPathConsumer2D)
/*     */   {
/* 129 */     PathStroker localPathStroker = new PathStroker(paramPathConsumer2D);
/* 130 */     Object localObject1 = localPathStroker;
/*     */ 
/* 132 */     float[] arrayOfFloat1 = null;
/* 133 */     if (!paramBoolean1) {
/* 134 */       localPathStroker.setPenDiameter(paramBasicStroke.getLineWidth());
/* 135 */       if (paramAffineTransform != null) {
/* 136 */         arrayOfFloat1 = getTransformMatrix(paramAffineTransform);
/*     */       }
/* 138 */       localPathStroker.setPenT4(arrayOfFloat1);
/* 139 */       localPathStroker.setPenFitting(0.01F, 100);
/*     */     }
/* 141 */     localPathStroker.setCaps(RasterizerCaps[paramBasicStroke.getEndCap()]);
/* 142 */     localPathStroker.setCorners(RasterizerCorners[paramBasicStroke.getLineJoin()], paramBasicStroke.getMiterLimit());
/*     */ 
/* 144 */     float[] arrayOfFloat2 = paramBasicStroke.getDashArray();
/*     */     Object localObject2;
/* 145 */     if (arrayOfFloat2 != null) {
/* 146 */       localObject2 = new PathDasher(localPathStroker);
/* 147 */       ((PathDasher)localObject2).setDash(arrayOfFloat2, paramBasicStroke.getDashPhase());
/* 148 */       if ((paramAffineTransform != null) && (arrayOfFloat1 == null)) {
/* 149 */         arrayOfFloat1 = getTransformMatrix(paramAffineTransform);
/*     */       }
/* 151 */       ((PathDasher)localObject2).setDashT4(arrayOfFloat1);
/* 152 */       localObject1 = localObject2;
/*     */     }
/*     */     try
/*     */     {
/* 156 */       localObject2 = paramShape.getPathIterator(paramAffineTransform);
/*     */ 
/* 158 */       feedConsumer((PathIterator)localObject2, (PathConsumer)localObject1, paramBoolean2, 0.25F);
/*     */ 
/* 163 */       while ((localObject1 != null) && (localObject1 != paramPathConsumer2D)) {
/* 164 */         localObject2 = ((PathConsumer)localObject1).getConsumer();
/* 165 */         ((PathConsumer)localObject1).dispose();
/* 166 */         localObject1 = localObject2;
/*     */       }
/*     */     }
/*     */     catch (PathException localPathException)
/*     */     {
/* 160 */       throw new InternalError("Unable to Stroke shape (" + localPathException.getMessage() + ")");
/*     */     }
/*     */     finally {
/* 163 */       while ((localObject1 != null) && (localObject1 != paramPathConsumer2D)) {
/* 164 */         PathConsumer localPathConsumer = ((PathConsumer)localObject1).getConsumer();
/* 165 */         ((PathConsumer)localObject1).dispose();
/* 166 */         localObject1 = localPathConsumer;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public static void feedConsumer(PathIterator paramPathIterator, PathConsumer paramPathConsumer, boolean paramBoolean, float paramFloat)
/*     */     throws PathException
/*     */   {
/* 178 */     paramPathConsumer.beginPath();
/* 179 */     int i = 0;
/* 180 */     int j = 0;
/* 181 */     int k = 0;
/* 182 */     float f1 = 0.0F;
/* 183 */     float f2 = 0.0F;
/* 184 */     float[] arrayOfFloat = new float[6];
/* 185 */     float f3 = 0.5F - paramFloat;
/* 186 */     float f4 = 0.0F;
/* 187 */     float f5 = 0.0F;
/*     */ 
/* 189 */     while (!paramPathIterator.isDone()) {
/* 190 */       int m = paramPathIterator.currentSegment(arrayOfFloat);
/* 191 */       if (i == 1) {
/* 192 */         i = 0;
/* 193 */         if (m != 0)
/*     */         {
/* 195 */           paramPathConsumer.beginSubpath(f1, f2);
/* 196 */           k = 1;
/*     */         }
/*     */       }
/* 199 */       if (paramBoolean)
/*     */       {
/*     */         int n;
/* 201 */         switch (m) {
/*     */         case 3:
/* 203 */           n = 4;
/* 204 */           break;
/*     */         case 2:
/* 206 */           n = 2;
/* 207 */           break;
/*     */         case 0:
/*     */         case 1:
/* 210 */           n = 0;
/* 211 */           break;
/*     */         case 4:
/*     */         default:
/* 214 */           n = -1;
/*     */         }
/*     */ 
/* 217 */         if (n >= 0) {
/* 218 */           float f6 = arrayOfFloat[n];
/* 219 */           float f7 = arrayOfFloat[(n + 1)];
/* 220 */           float f8 = (float)Math.floor(f6 + f3) + paramFloat;
/* 221 */           float f9 = (float)Math.floor(f7 + f3) + paramFloat;
/* 222 */           arrayOfFloat[n] = f8;
/* 223 */           arrayOfFloat[(n + 1)] = f9;
/* 224 */           f8 -= f6;
/* 225 */           f9 -= f7;
/* 226 */           switch (m) {
/*     */           case 3:
/* 228 */             arrayOfFloat[0] += f4;
/* 229 */             arrayOfFloat[1] += f5;
/* 230 */             arrayOfFloat[2] += f8;
/* 231 */             arrayOfFloat[3] += f9;
/* 232 */             break;
/*     */           case 2:
/* 234 */             arrayOfFloat[0] += (f8 + f4) / 2.0F;
/* 235 */             arrayOfFloat[1] += (f9 + f5) / 2.0F;
/* 236 */             break;
/*     */           case 0:
/*     */           case 1:
/*     */           case 4:
/*     */           }
/*     */ 
/* 242 */           f4 = f8;
/* 243 */           f5 = f9;
/*     */         }
/*     */       }
/* 246 */       switch (m)
/*     */       {
/*     */       case 0:
/* 254 */         if ((arrayOfFloat[0] < 1.701412E+038F) && (arrayOfFloat[0] > -1.701412E+038F) && (arrayOfFloat[1] < 1.701412E+038F) && (arrayOfFloat[1] > -1.701412E+038F))
/*     */         {
/* 257 */           f1 = arrayOfFloat[0];
/* 258 */           f2 = arrayOfFloat[1];
/* 259 */           paramPathConsumer.beginSubpath(f1, f2);
/* 260 */           k = 1;
/* 261 */           j = 0;
/*     */         } else {
/* 263 */           j = 1;
/*     */         }
/* 265 */         break;
/*     */       case 1:
/* 273 */         if ((arrayOfFloat[0] < 1.701412E+038F) && (arrayOfFloat[0] > -1.701412E+038F) && (arrayOfFloat[1] < 1.701412E+038F) && (arrayOfFloat[1] > -1.701412E+038F))
/*     */         {
/* 276 */           if (j != 0) {
/* 277 */             paramPathConsumer.beginSubpath(arrayOfFloat[0], arrayOfFloat[1]);
/* 278 */             k = 1;
/* 279 */             j = 0;
/*     */           } else {
/* 281 */             paramPathConsumer.appendLine(arrayOfFloat[0], arrayOfFloat[1]); }  } break;
/*     */       case 2:
/* 295 */         if ((arrayOfFloat[2] < 1.701412E+038F) && (arrayOfFloat[2] > -1.701412E+038F) && (arrayOfFloat[3] < 1.701412E+038F) && (arrayOfFloat[3] > -1.701412E+038F))
/*     */         {
/* 298 */           if (j != 0) {
/* 299 */             paramPathConsumer.beginSubpath(arrayOfFloat[2], arrayOfFloat[3]);
/* 300 */             k = 1;
/* 301 */             j = 0;
/*     */           }
/* 303 */           else if ((arrayOfFloat[0] < 1.701412E+038F) && (arrayOfFloat[0] > -1.701412E+038F) && (arrayOfFloat[1] < 1.701412E+038F) && (arrayOfFloat[1] > -1.701412E+038F))
/*     */           {
/* 306 */             paramPathConsumer.appendQuadratic(arrayOfFloat[0], arrayOfFloat[1], arrayOfFloat[2], arrayOfFloat[3]);
/*     */           }
/*     */           else {
/* 309 */             paramPathConsumer.appendLine(arrayOfFloat[2], arrayOfFloat[3]); }  } break;
/*     */       case 3:
/* 324 */         if ((arrayOfFloat[4] < 1.701412E+038F) && (arrayOfFloat[4] > -1.701412E+038F) && (arrayOfFloat[5] < 1.701412E+038F) && (arrayOfFloat[5] > -1.701412E+038F))
/*     */         {
/* 327 */           if (j != 0) {
/* 328 */             paramPathConsumer.beginSubpath(arrayOfFloat[4], arrayOfFloat[5]);
/* 329 */             k = 1;
/* 330 */             j = 0;
/*     */           }
/* 332 */           else if ((arrayOfFloat[0] < 1.701412E+038F) && (arrayOfFloat[0] > -1.701412E+038F) && (arrayOfFloat[1] < 1.701412E+038F) && (arrayOfFloat[1] > -1.701412E+038F) && (arrayOfFloat[2] < 1.701412E+038F) && (arrayOfFloat[2] > -1.701412E+038F) && (arrayOfFloat[3] < 1.701412E+038F) && (arrayOfFloat[3] > -1.701412E+038F))
/*     */           {
/* 337 */             paramPathConsumer.appendCubic(arrayOfFloat[0], arrayOfFloat[1], arrayOfFloat[2], arrayOfFloat[3], arrayOfFloat[4], arrayOfFloat[5]);
/*     */           }
/*     */           else
/*     */           {
/* 341 */             paramPathConsumer.appendLine(arrayOfFloat[4], arrayOfFloat[5]); }  } break;
/*     */       case 4:
/* 347 */         if (k != 0) {
/* 348 */           paramPathConsumer.closedSubpath();
/* 349 */           k = 0;
/* 350 */           i = 1;
/*     */         }
/*     */         break;
/*     */       }
/* 354 */       paramPathIterator.next();
/*     */     }
/*     */ 
/* 357 */     paramPathConsumer.endPath();
/*     */   }
/*     */ 
/*     */   public static synchronized Rasterizer getRasterizer()
/*     */   {
/* 363 */     Rasterizer localRasterizer = theRasterizer;
/* 364 */     if (localRasterizer == null)
/* 365 */       localRasterizer = new Rasterizer();
/*     */     else {
/* 367 */       theRasterizer = null;
/*     */     }
/* 369 */     return localRasterizer;
/*     */   }
/*     */ 
/*     */   public static synchronized void dropRasterizer(Rasterizer paramRasterizer) {
/* 373 */     paramRasterizer.reset();
/* 374 */     theRasterizer = paramRasterizer;
/*     */   }
/*     */ 
/*     */   public float getMinimumAAPenSize()
/*     */   {
/* 382 */     return 0.2F;
/*     */   }
/*     */ 
/*     */   public AATileGenerator getAATileGenerator(Shape paramShape, AffineTransform paramAffineTransform, Region paramRegion, BasicStroke paramBasicStroke, boolean paramBoolean1, boolean paramBoolean2, int[] paramArrayOfInt)
/*     */   {
/* 397 */     Rasterizer localRasterizer = getRasterizer();
/* 398 */     PathIterator localPathIterator = paramShape.getPathIterator(paramAffineTransform);
/*     */ 
/* 400 */     if (paramBasicStroke != null) {
/* 401 */       float[] arrayOfFloat1 = null;
/* 402 */       localRasterizer.setUsage(3);
/* 403 */       if (paramBoolean1) {
/* 404 */         localRasterizer.setPenDiameter(0.2F);
/*     */       } else {
/* 406 */         localRasterizer.setPenDiameter(paramBasicStroke.getLineWidth());
/* 407 */         if (paramAffineTransform != null) {
/* 408 */           arrayOfFloat1 = getTransformMatrix(paramAffineTransform);
/* 409 */           localRasterizer.setPenT4(arrayOfFloat1);
/*     */         }
/* 411 */         localRasterizer.setPenFitting(0.01F, 20);
/*     */       }
/* 413 */       localRasterizer.setCaps(RasterizerCaps[paramBasicStroke.getEndCap()]);
/* 414 */       localRasterizer.setCorners(RasterizerCorners[paramBasicStroke.getLineJoin()], paramBasicStroke.getMiterLimit());
/*     */ 
/* 416 */       float[] arrayOfFloat2 = paramBasicStroke.getDashArray();
/* 417 */       if (arrayOfFloat2 != null) {
/* 418 */         localRasterizer.setDash(arrayOfFloat2, paramBasicStroke.getDashPhase());
/* 419 */         if ((paramAffineTransform != null) && (arrayOfFloat1 == null)) {
/* 420 */           arrayOfFloat1 = getTransformMatrix(paramAffineTransform);
/*     */         }
/* 422 */         localRasterizer.setDashT4(arrayOfFloat1);
/*     */       }
/*     */     } else {
/* 425 */       localRasterizer.setUsage(localPathIterator.getWindingRule() == 0 ? 1 : 2);
/*     */     }
/*     */ 
/* 430 */     localRasterizer.beginPath();
/*     */ 
/* 432 */     int i = 0;
/* 433 */     int j = 0;
/* 434 */     int k = 0;
/* 435 */     float f1 = 0.0F;
/* 436 */     float f2 = 0.0F;
/* 437 */     float[] arrayOfFloat3 = new float[6];
/* 438 */     float f3 = 0.0F;
/* 439 */     float f4 = 0.0F;
/*     */ 
/* 441 */     while (!localPathIterator.isDone()) {
/* 442 */       int m = localPathIterator.currentSegment(arrayOfFloat3);
/* 443 */       if (i == 1) {
/* 444 */         i = 0;
/* 445 */         if (m != 0)
/*     */         {
/* 447 */           localRasterizer.beginSubpath(f1, f2);
/* 448 */           k = 1;
/*     */         }
/*     */       }
/* 451 */       if (paramBoolean2)
/*     */       {
/*     */         int n;
/* 453 */         switch (m) {
/*     */         case 3:
/* 455 */           n = 4;
/* 456 */           break;
/*     */         case 2:
/* 458 */           n = 2;
/* 459 */           break;
/*     */         case 0:
/*     */         case 1:
/* 462 */           n = 0;
/* 463 */           break;
/*     */         case 4:
/*     */         default:
/* 466 */           n = -1;
/*     */         }
/*     */ 
/* 469 */         if (n >= 0) {
/* 470 */           float f5 = arrayOfFloat3[n];
/* 471 */           float f6 = arrayOfFloat3[(n + 1)];
/* 472 */           float f7 = (float)Math.floor(f5) + 0.5F;
/* 473 */           float f8 = (float)Math.floor(f6) + 0.5F;
/* 474 */           arrayOfFloat3[n] = f7;
/* 475 */           arrayOfFloat3[(n + 1)] = f8;
/* 476 */           f7 -= f5;
/* 477 */           f8 -= f6;
/* 478 */           switch (m) {
/*     */           case 3:
/* 480 */             arrayOfFloat3[0] += f3;
/* 481 */             arrayOfFloat3[1] += f4;
/* 482 */             arrayOfFloat3[2] += f7;
/* 483 */             arrayOfFloat3[3] += f8;
/* 484 */             break;
/*     */           case 2:
/* 486 */             arrayOfFloat3[0] += (f7 + f3) / 2.0F;
/* 487 */             arrayOfFloat3[1] += (f8 + f4) / 2.0F;
/* 488 */             break;
/*     */           case 0:
/*     */           case 1:
/*     */           case 4:
/*     */           }
/*     */ 
/* 494 */           f3 = f7;
/* 495 */           f4 = f8;
/*     */         }
/*     */       }
/* 498 */       switch (m)
/*     */       {
/*     */       case 0:
/* 507 */         if ((arrayOfFloat3[0] < 1.701412E+038F) && (arrayOfFloat3[0] > -1.701412E+038F) && (arrayOfFloat3[1] < 1.701412E+038F) && (arrayOfFloat3[1] > -1.701412E+038F))
/*     */         {
/* 510 */           f1 = arrayOfFloat3[0];
/* 511 */           f2 = arrayOfFloat3[1];
/* 512 */           localRasterizer.beginSubpath(f1, f2);
/* 513 */           k = 1;
/* 514 */           j = 0;
/*     */         } else {
/* 516 */           j = 1;
/*     */         }
/* 518 */         break;
/*     */       case 1:
/* 527 */         if ((arrayOfFloat3[0] < 1.701412E+038F) && (arrayOfFloat3[0] > -1.701412E+038F) && (arrayOfFloat3[1] < 1.701412E+038F) && (arrayOfFloat3[1] > -1.701412E+038F))
/*     */         {
/* 530 */           if (j != 0) {
/* 531 */             localRasterizer.beginSubpath(arrayOfFloat3[0], arrayOfFloat3[1]);
/* 532 */             k = 1;
/* 533 */             j = 0;
/*     */           } else {
/* 535 */             localRasterizer.appendLine(arrayOfFloat3[0], arrayOfFloat3[1]); }  } break;
/*     */       case 2:
/* 550 */         if ((arrayOfFloat3[2] < 1.701412E+038F) && (arrayOfFloat3[2] > -1.701412E+038F) && (arrayOfFloat3[3] < 1.701412E+038F) && (arrayOfFloat3[3] > -1.701412E+038F))
/*     */         {
/* 553 */           if (j != 0) {
/* 554 */             localRasterizer.beginSubpath(arrayOfFloat3[2], arrayOfFloat3[3]);
/* 555 */             k = 1;
/* 556 */             j = 0;
/*     */           }
/* 558 */           else if ((arrayOfFloat3[0] < 1.701412E+038F) && (arrayOfFloat3[0] > -1.701412E+038F) && (arrayOfFloat3[1] < 1.701412E+038F) && (arrayOfFloat3[1] > -1.701412E+038F))
/*     */           {
/* 561 */             localRasterizer.appendQuadratic(arrayOfFloat3[0], arrayOfFloat3[1], arrayOfFloat3[2], arrayOfFloat3[3]);
/*     */           }
/*     */           else {
/* 564 */             localRasterizer.appendLine(arrayOfFloat3[2], arrayOfFloat3[3]); }  } break;
/*     */       case 3:
/* 580 */         if ((arrayOfFloat3[4] < 1.701412E+038F) && (arrayOfFloat3[4] > -1.701412E+038F) && (arrayOfFloat3[5] < 1.701412E+038F) && (arrayOfFloat3[5] > -1.701412E+038F))
/*     */         {
/* 583 */           if (j != 0) {
/* 584 */             localRasterizer.beginSubpath(arrayOfFloat3[4], arrayOfFloat3[5]);
/* 585 */             k = 1;
/* 586 */             j = 0;
/*     */           }
/* 588 */           else if ((arrayOfFloat3[0] < 1.701412E+038F) && (arrayOfFloat3[0] > -1.701412E+038F) && (arrayOfFloat3[1] < 1.701412E+038F) && (arrayOfFloat3[1] > -1.701412E+038F) && (arrayOfFloat3[2] < 1.701412E+038F) && (arrayOfFloat3[2] > -1.701412E+038F) && (arrayOfFloat3[3] < 1.701412E+038F) && (arrayOfFloat3[3] > -1.701412E+038F))
/*     */           {
/* 593 */             localRasterizer.appendCubic(arrayOfFloat3[0], arrayOfFloat3[1], arrayOfFloat3[2], arrayOfFloat3[3], arrayOfFloat3[4], arrayOfFloat3[5]);
/*     */           }
/*     */           else
/*     */           {
/* 597 */             localRasterizer.appendLine(arrayOfFloat3[4], arrayOfFloat3[5]); }  } break;
/*     */       case 4:
/* 603 */         if (k != 0) {
/* 604 */           localRasterizer.closedSubpath();
/* 605 */           k = 0;
/* 606 */           i = 1;
/*     */         }
/*     */         break;
/*     */       }
/* 610 */       localPathIterator.next();
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 615 */       localRasterizer.endPath();
/* 616 */       localRasterizer.getAlphaBox(paramArrayOfInt);
/* 617 */       paramRegion.clipBoxToBounds(paramArrayOfInt);
/* 618 */       if ((paramArrayOfInt[0] >= paramArrayOfInt[2]) || (paramArrayOfInt[1] >= paramArrayOfInt[3])) {
/* 619 */         dropRasterizer(localRasterizer);
/* 620 */         return null;
/*     */       }
/* 622 */       localRasterizer.setOutputArea(paramArrayOfInt[0], paramArrayOfInt[1], paramArrayOfInt[2] - paramArrayOfInt[0], paramArrayOfInt[3] - paramArrayOfInt[1]);
/*     */     }
/*     */     catch (PRException localPRException)
/*     */     {
/* 632 */       System.err.println("DuctusRenderingEngine.getAATileGenerator: " + localPRException);
/*     */     }
/*     */ 
/* 635 */     return localRasterizer;
/*     */   }
/*     */ 
/*     */   public AATileGenerator getAATileGenerator(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5, double paramDouble6, double paramDouble7, double paramDouble8, Region paramRegion, int[] paramArrayOfInt)
/*     */   {
/* 651 */     int i = (paramDouble7 > 0.0D) && (paramDouble8 > 0.0D) ? 1 : 0;
/*     */     double d1;
/*     */     double d2;
/*     */     double d3;
/*     */     double d4;
/* 653 */     if (i != 0) {
/* 654 */       d1 = paramDouble3 * paramDouble7;
/* 655 */       d2 = paramDouble4 * paramDouble7;
/* 656 */       d3 = paramDouble5 * paramDouble8;
/* 657 */       d4 = paramDouble6 * paramDouble8;
/* 658 */       paramDouble1 -= (d1 + d3) / 2.0D;
/* 659 */       paramDouble2 -= (d2 + d4) / 2.0D;
/* 660 */       paramDouble3 += d1;
/* 661 */       paramDouble4 += d2;
/* 662 */       paramDouble5 += d3;
/* 663 */       paramDouble6 += d4;
/* 664 */       if ((paramDouble7 > 1.0D) && (paramDouble8 > 1.0D))
/*     */       {
/* 666 */         i = 0;
/*     */       }
/*     */     } else {
/* 669 */       d1 = d2 = d3 = d4 = 0.0D;
/*     */     }
/*     */ 
/* 672 */     Rasterizer localRasterizer = getRasterizer();
/*     */ 
/* 674 */     localRasterizer.setUsage(1);
/*     */ 
/* 676 */     localRasterizer.beginPath();
/* 677 */     localRasterizer.beginSubpath((float)paramDouble1, (float)paramDouble2);
/* 678 */     localRasterizer.appendLine((float)(paramDouble1 + paramDouble3), (float)(paramDouble2 + paramDouble4));
/* 679 */     localRasterizer.appendLine((float)(paramDouble1 + paramDouble3 + paramDouble5), (float)(paramDouble2 + paramDouble4 + paramDouble6));
/* 680 */     localRasterizer.appendLine((float)(paramDouble1 + paramDouble5), (float)(paramDouble2 + paramDouble6));
/* 681 */     localRasterizer.closedSubpath();
/* 682 */     if (i != 0) {
/* 683 */       paramDouble1 += d1 + d3;
/* 684 */       paramDouble2 += d2 + d4;
/* 685 */       paramDouble3 -= 2.0D * d1;
/* 686 */       paramDouble4 -= 2.0D * d2;
/* 687 */       paramDouble5 -= 2.0D * d3;
/* 688 */       paramDouble6 -= 2.0D * d4;
/* 689 */       localRasterizer.beginSubpath((float)paramDouble1, (float)paramDouble2);
/* 690 */       localRasterizer.appendLine((float)(paramDouble1 + paramDouble3), (float)(paramDouble2 + paramDouble4));
/* 691 */       localRasterizer.appendLine((float)(paramDouble1 + paramDouble3 + paramDouble5), (float)(paramDouble2 + paramDouble4 + paramDouble6));
/* 692 */       localRasterizer.appendLine((float)(paramDouble1 + paramDouble5), (float)(paramDouble2 + paramDouble6));
/* 693 */       localRasterizer.closedSubpath();
/*     */     }
/*     */     try
/*     */     {
/* 697 */       localRasterizer.endPath();
/* 698 */       localRasterizer.getAlphaBox(paramArrayOfInt);
/* 699 */       paramRegion.clipBoxToBounds(paramArrayOfInt);
/* 700 */       if ((paramArrayOfInt[0] >= paramArrayOfInt[2]) || (paramArrayOfInt[1] >= paramArrayOfInt[3])) {
/* 701 */         dropRasterizer(localRasterizer);
/* 702 */         return null;
/*     */       }
/* 704 */       localRasterizer.setOutputArea(paramArrayOfInt[0], paramArrayOfInt[1], paramArrayOfInt[2] - paramArrayOfInt[0], paramArrayOfInt[3] - paramArrayOfInt[1]);
/*     */     }
/*     */     catch (PRException localPRException)
/*     */     {
/* 714 */       System.err.println("DuctusRenderingEngine.getAATileGenerator: " + localPRException);
/*     */     }
/*     */ 
/* 717 */     return localRasterizer;
/*     */   }
/*     */ 
/*     */   private void feedConsumer(PathConsumer paramPathConsumer, PathIterator paramPathIterator) {
/*     */     try {
/* 722 */       paramPathConsumer.beginPath();
/* 723 */       int i = 0;
/* 724 */       float f1 = 0.0F;
/* 725 */       float f2 = 0.0F;
/* 726 */       float[] arrayOfFloat = new float[6];
/*     */ 
/* 728 */       while (!paramPathIterator.isDone()) {
/* 729 */         int j = paramPathIterator.currentSegment(arrayOfFloat);
/* 730 */         if (i == 1) {
/* 731 */           i = 0;
/* 732 */           if (j != 0)
/*     */           {
/* 734 */             paramPathConsumer.beginSubpath(f1, f2);
/*     */           }
/*     */         }
/* 737 */         switch (j) {
/*     */         case 0:
/* 739 */           f1 = arrayOfFloat[0];
/* 740 */           f2 = arrayOfFloat[1];
/* 741 */           paramPathConsumer.beginSubpath(arrayOfFloat[0], arrayOfFloat[1]);
/* 742 */           break;
/*     */         case 1:
/* 744 */           paramPathConsumer.appendLine(arrayOfFloat[0], arrayOfFloat[1]);
/* 745 */           break;
/*     */         case 2:
/* 747 */           paramPathConsumer.appendQuadratic(arrayOfFloat[0], arrayOfFloat[1], arrayOfFloat[2], arrayOfFloat[3]);
/*     */ 
/* 749 */           break;
/*     */         case 3:
/* 751 */           paramPathConsumer.appendCubic(arrayOfFloat[0], arrayOfFloat[1], arrayOfFloat[2], arrayOfFloat[3], arrayOfFloat[4], arrayOfFloat[5]);
/*     */ 
/* 754 */           break;
/*     */         case 4:
/* 756 */           paramPathConsumer.closedSubpath();
/* 757 */           i = 1;
/*     */         }
/*     */ 
/* 760 */         paramPathIterator.next();
/*     */       }
/*     */ 
/* 763 */       paramPathConsumer.endPath();
/*     */     } catch (PathException localPathException) {
/* 765 */       throw new InternalError("Unable to Stroke shape (" + localPathException.getMessage() + ")");
/*     */     }
/*     */   }
/*     */ 
/*     */   private class FillAdapter implements PathConsumer
/*     */   {
/*     */     boolean closed;
/*     */     Path2D.Float path;
/*     */ 
/*     */     public FillAdapter()
/*     */     {
/* 777 */       this.path = new Path2D.Float(1);
/*     */     }
/*     */ 
/*     */     public Shape getShape() {
/* 781 */       return this.path;
/*     */     }
/*     */ 
/*     */     public void dispose() {
/*     */     }
/*     */ 
/*     */     public PathConsumer getConsumer() {
/* 788 */       return null;
/*     */     }
/*     */     public void beginPath() {
/*     */     }
/*     */ 
/*     */     public void beginSubpath(float paramFloat1, float paramFloat2) {
/* 794 */       if (this.closed) {
/* 795 */         this.path.closePath();
/* 796 */         this.closed = false;
/*     */       }
/* 798 */       this.path.moveTo(paramFloat1, paramFloat2);
/*     */     }
/*     */ 
/*     */     public void appendLine(float paramFloat1, float paramFloat2) {
/* 802 */       this.path.lineTo(paramFloat1, paramFloat2);
/*     */     }
/*     */ 
/*     */     public void appendQuadratic(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4) {
/* 806 */       this.path.quadTo(paramFloat1, paramFloat2, paramFloat3, paramFloat4);
/*     */     }
/*     */ 
/*     */     public void appendCubic(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6)
/*     */     {
/* 812 */       this.path.curveTo(paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloat5, paramFloat6);
/*     */     }
/*     */ 
/*     */     public void closedSubpath() {
/* 816 */       this.closed = true;
/*     */     }
/*     */ 
/*     */     public void endPath() {
/* 820 */       if (this.closed) {
/* 821 */         this.path.closePath();
/* 822 */         this.closed = false;
/*     */       }
/*     */     }
/*     */ 
/*     */     public void useProxy(FastPathProducer paramFastPathProducer)
/*     */       throws PathException
/*     */     {
/* 829 */       paramFastPathProducer.sendTo(this);
/*     */     }
/*     */ 
/*     */     public long getCPathConsumer() {
/* 833 */       return 0L;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.dc.DuctusRenderingEngine
 * JD-Core Version:    0.6.2
 */