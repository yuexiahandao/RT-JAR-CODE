/*     */ package sun.java2d.pipe;
/*     */ 
/*     */ import java.awt.BasicStroke;
/*     */ import java.awt.Shape;
/*     */ import java.awt.geom.AffineTransform;
/*     */ import java.awt.geom.Line2D;
/*     */ import java.awt.geom.Rectangle2D;
/*     */ import sun.java2d.SunGraphics2D;
/*     */ 
/*     */ public class PixelToParallelogramConverter extends PixelToShapeConverter
/*     */   implements ShapeDrawPipe
/*     */ {
/*     */   ParallelogramPipe outrenderer;
/*     */   double minPenSize;
/*     */   double normPosition;
/*     */   double normRoundingBias;
/*     */   boolean adjustfill;
/*     */ 
/*     */   public PixelToParallelogramConverter(ShapeDrawPipe paramShapeDrawPipe, ParallelogramPipe paramParallelogramPipe, double paramDouble1, double paramDouble2, boolean paramBoolean)
/*     */   {
/*  69 */     super(paramShapeDrawPipe);
/*  70 */     this.outrenderer = paramParallelogramPipe;
/*  71 */     this.minPenSize = paramDouble1;
/*  72 */     this.normPosition = paramDouble2;
/*  73 */     this.normRoundingBias = (0.5D - paramDouble2);
/*  74 */     this.adjustfill = paramBoolean;
/*     */   }
/*     */ 
/*     */   public void drawLine(SunGraphics2D paramSunGraphics2D, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/*  80 */     if (!drawGeneralLine(paramSunGraphics2D, paramInt1, paramInt2, paramInt3, paramInt4))
/*  81 */       super.drawLine(paramSunGraphics2D, paramInt1, paramInt2, paramInt3, paramInt4);
/*     */   }
/*     */ 
/*     */   public void drawRect(SunGraphics2D paramSunGraphics2D, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/*  88 */     if ((paramInt3 >= 0) && (paramInt4 >= 0)) {
/*  89 */       if (paramSunGraphics2D.strokeState < 3) {
/*  90 */         BasicStroke localBasicStroke = (BasicStroke)paramSunGraphics2D.stroke;
/*  91 */         if ((paramInt3 > 0) && (paramInt4 > 0)) {
/*  92 */           if ((localBasicStroke.getLineJoin() == 0) && (localBasicStroke.getDashArray() == null))
/*     */           {
/*  95 */             double d = localBasicStroke.getLineWidth();
/*  96 */             drawRectangle(paramSunGraphics2D, paramInt1, paramInt2, paramInt3, paramInt4, d);
/*     */           }
/*     */ 
/*     */         }
/*     */         else
/*     */         {
/* 103 */           drawLine(paramSunGraphics2D, paramInt1, paramInt2, paramInt1 + paramInt3, paramInt2 + paramInt4);
/* 104 */           return;
/*     */         }
/*     */       }
/* 107 */       super.drawRect(paramSunGraphics2D, paramInt1, paramInt2, paramInt3, paramInt4);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void fillRect(SunGraphics2D paramSunGraphics2D, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/* 114 */     if ((paramInt3 > 0) && (paramInt4 > 0))
/* 115 */       fillRectangle(paramSunGraphics2D, paramInt1, paramInt2, paramInt3, paramInt4);
/*     */   }
/*     */ 
/*     */   public void draw(SunGraphics2D paramSunGraphics2D, Shape paramShape)
/*     */   {
/* 120 */     if (paramSunGraphics2D.strokeState < 3) {
/* 121 */       BasicStroke localBasicStroke = (BasicStroke)paramSunGraphics2D.stroke;
/*     */       Object localObject;
/* 122 */       if ((paramShape instanceof Rectangle2D)) {
/* 123 */         if ((localBasicStroke.getLineJoin() == 0) && (localBasicStroke.getDashArray() == null))
/*     */         {
/* 126 */           localObject = (Rectangle2D)paramShape;
/* 127 */           double d1 = ((Rectangle2D)localObject).getWidth();
/* 128 */           double d2 = ((Rectangle2D)localObject).getHeight();
/* 129 */           double d3 = ((Rectangle2D)localObject).getX();
/* 130 */           double d4 = ((Rectangle2D)localObject).getY();
/* 131 */           if ((d1 >= 0.0D) && (d2 >= 0.0D)) {
/* 132 */             double d5 = localBasicStroke.getLineWidth();
/* 133 */             drawRectangle(paramSunGraphics2D, d3, d4, d1, d2, d5);
/*     */           }
/*     */         }
/*     */       }
/* 137 */       else if ((paramShape instanceof Line2D)) {
/* 138 */         localObject = (Line2D)paramShape;
/* 139 */         if (drawGeneralLine(paramSunGraphics2D, ((Line2D)localObject).getX1(), ((Line2D)localObject).getY1(), ((Line2D)localObject).getX2(), ((Line2D)localObject).getY2()))
/*     */         {
/* 143 */           return;
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 148 */     this.outpipe.draw(paramSunGraphics2D, paramShape);
/*     */   }
/*     */ 
/*     */   public void fill(SunGraphics2D paramSunGraphics2D, Shape paramShape) {
/* 152 */     if ((paramShape instanceof Rectangle2D)) {
/* 153 */       Rectangle2D localRectangle2D = (Rectangle2D)paramShape;
/* 154 */       double d1 = localRectangle2D.getWidth();
/* 155 */       double d2 = localRectangle2D.getHeight();
/* 156 */       if ((d1 > 0.0D) && (d2 > 0.0D)) {
/* 157 */         double d3 = localRectangle2D.getX();
/* 158 */         double d4 = localRectangle2D.getY();
/* 159 */         fillRectangle(paramSunGraphics2D, d3, d4, d1, d2);
/*     */       }
/* 161 */       return;
/*     */     }
/*     */ 
/* 164 */     this.outpipe.fill(paramSunGraphics2D, paramShape);
/*     */   }
/*     */ 
/*     */   static double len(double paramDouble1, double paramDouble2) {
/* 168 */     return paramDouble2 == 0.0D ? Math.abs(paramDouble1) : paramDouble1 == 0.0D ? Math.abs(paramDouble2) : Math.sqrt(paramDouble1 * paramDouble1 + paramDouble2 * paramDouble2);
/*     */   }
/*     */ 
/*     */   double normalize(double paramDouble)
/*     */   {
/* 174 */     return Math.floor(paramDouble + this.normRoundingBias) + this.normPosition;
/*     */   }
/*     */ 
/*     */   public boolean drawGeneralLine(SunGraphics2D paramSunGraphics2D, double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4)
/*     */   {
/* 181 */     if ((paramSunGraphics2D.strokeState == 3) || (paramSunGraphics2D.strokeState == 1))
/*     */     {
/* 184 */       return false;
/*     */     }
/* 186 */     BasicStroke localBasicStroke = (BasicStroke)paramSunGraphics2D.stroke;
/* 187 */     int i = localBasicStroke.getEndCap();
/* 188 */     if ((i == 1) || (localBasicStroke.getDashArray() != null))
/*     */     {
/* 192 */       return false;
/*     */     }
/* 194 */     double d1 = localBasicStroke.getLineWidth();
/*     */ 
/* 197 */     double d2 = paramDouble3 - paramDouble1;
/* 198 */     double d3 = paramDouble4 - paramDouble2;
/*     */     double d4;
/*     */     double d5;
/*     */     double d6;
/*     */     double d7;
/* 200 */     switch (paramSunGraphics2D.transformState)
/*     */     {
/*     */     case 3:
/*     */     case 4:
/* 204 */       double[] arrayOfDouble1 = { paramDouble1, paramDouble2, paramDouble3, paramDouble4 };
/* 205 */       paramSunGraphics2D.transform.transform(arrayOfDouble1, 0, arrayOfDouble1, 0, 2);
/* 206 */       d4 = arrayOfDouble1[0];
/* 207 */       d5 = arrayOfDouble1[1];
/* 208 */       d6 = arrayOfDouble1[2];
/* 209 */       d7 = arrayOfDouble1[3];
/*     */ 
/* 211 */       break;
/*     */     case 1:
/*     */     case 2:
/* 215 */       double d8 = paramSunGraphics2D.transform.getTranslateX();
/* 216 */       double d10 = paramSunGraphics2D.transform.getTranslateY();
/* 217 */       d4 = paramDouble1 + d8;
/* 218 */       d5 = paramDouble2 + d10;
/* 219 */       d6 = paramDouble3 + d8;
/* 220 */       d7 = paramDouble4 + d10;
/*     */ 
/* 222 */       break;
/*     */     case 0:
/* 224 */       d4 = paramDouble1;
/* 225 */       d5 = paramDouble2;
/* 226 */       d6 = paramDouble3;
/* 227 */       d7 = paramDouble4;
/* 228 */       break;
/*     */     default:
/* 230 */       throw new InternalError("unknown TRANSFORM state...");
/*     */     }
/* 232 */     if (paramSunGraphics2D.strokeHint != 2) {
/* 233 */       if ((paramSunGraphics2D.strokeState == 0) && ((this.outrenderer instanceof PixelDrawPipe)))
/*     */       {
/* 238 */         int j = (int)Math.floor(d4 - paramSunGraphics2D.transX);
/* 239 */         int k = (int)Math.floor(d5 - paramSunGraphics2D.transY);
/* 240 */         int m = (int)Math.floor(d6 - paramSunGraphics2D.transX);
/* 241 */         int n = (int)Math.floor(d7 - paramSunGraphics2D.transY);
/* 242 */         ((PixelDrawPipe)this.outrenderer).drawLine(paramSunGraphics2D, j, k, m, n);
/* 243 */         return true;
/*     */       }
/* 245 */       d4 = normalize(d4);
/* 246 */       d5 = normalize(d5);
/* 247 */       d6 = normalize(d6);
/* 248 */       d7 = normalize(d7);
/*     */     }
/* 250 */     if (paramSunGraphics2D.transformState >= 3)
/*     */     {
/* 254 */       d9 = len(d2, d3);
/* 255 */       if (d9 == 0.0D) {
/* 256 */         d2 = d9 = 1.0D;
/*     */       }
/*     */ 
/* 260 */       double[] arrayOfDouble2 = { d3 / d9, -d2 / d9 };
/* 261 */       paramSunGraphics2D.transform.deltaTransform(arrayOfDouble2, 0, arrayOfDouble2, 0, 1);
/* 262 */       d1 *= len(arrayOfDouble2[0], arrayOfDouble2[1]);
/*     */     }
/* 264 */     d1 = Math.max(d1, this.minPenSize);
/* 265 */     d2 = d6 - d4;
/* 266 */     d3 = d7 - d5;
/* 267 */     double d9 = len(d2, d3);
/*     */     double d11;
/*     */     double d12;
/* 269 */     if (d9 == 0.0D) {
/* 270 */       if (i == 0) {
/* 271 */         return true;
/*     */       }
/* 273 */       d11 = d1;
/* 274 */       d12 = 0.0D;
/*     */     } else {
/* 276 */       d11 = d1 * d2 / d9;
/* 277 */       d12 = d1 * d3 / d9;
/*     */     }
/* 279 */     double d13 = d4 + d12 / 2.0D;
/* 280 */     double d14 = d5 - d11 / 2.0D;
/* 281 */     if (i == 2) {
/* 282 */       d13 -= d11 / 2.0D;
/* 283 */       d14 -= d12 / 2.0D;
/* 284 */       d2 += d11;
/* 285 */       d3 += d12;
/*     */     }
/* 287 */     this.outrenderer.fillParallelogram(paramSunGraphics2D, paramDouble1, paramDouble2, paramDouble3, paramDouble4, d13, d14, -d12, d11, d2, d3);
/*     */ 
/* 289 */     return true;
/*     */   }
/*     */ 
/*     */   public void fillRectangle(SunGraphics2D paramSunGraphics2D, double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4)
/*     */   {
/* 298 */     AffineTransform localAffineTransform = paramSunGraphics2D.transform;
/* 299 */     double d3 = localAffineTransform.getScaleX();
/* 300 */     double d4 = localAffineTransform.getShearY();
/* 301 */     double d5 = localAffineTransform.getShearX();
/* 302 */     double d6 = localAffineTransform.getScaleY();
/* 303 */     double d1 = paramDouble1 * d3 + paramDouble2 * d5 + localAffineTransform.getTranslateX();
/* 304 */     double d2 = paramDouble1 * d4 + paramDouble2 * d6 + localAffineTransform.getTranslateY();
/* 305 */     d3 *= paramDouble3;
/* 306 */     d4 *= paramDouble3;
/* 307 */     d5 *= paramDouble4;
/* 308 */     d6 *= paramDouble4;
/* 309 */     if ((this.adjustfill) && (paramSunGraphics2D.strokeState < 3) && (paramSunGraphics2D.strokeHint != 2))
/*     */     {
/* 313 */       double d7 = normalize(d1);
/* 314 */       double d8 = normalize(d2);
/* 315 */       d3 = normalize(d1 + d3) - d7;
/* 316 */       d4 = normalize(d2 + d4) - d8;
/* 317 */       d5 = normalize(d1 + d5) - d7;
/* 318 */       d6 = normalize(d2 + d6) - d8;
/* 319 */       d1 = d7;
/* 320 */       d2 = d8;
/*     */     }
/* 322 */     this.outrenderer.fillParallelogram(paramSunGraphics2D, paramDouble1, paramDouble2, paramDouble1 + paramDouble3, paramDouble2 + paramDouble4, d1, d2, d3, d4, d5, d6);
/*     */   }
/*     */ 
/*     */   public void drawRectangle(SunGraphics2D paramSunGraphics2D, double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5)
/*     */   {
/* 334 */     AffineTransform localAffineTransform = paramSunGraphics2D.transform;
/* 335 */     double d3 = localAffineTransform.getScaleX();
/* 336 */     double d4 = localAffineTransform.getShearY();
/* 337 */     double d5 = localAffineTransform.getShearX();
/* 338 */     double d6 = localAffineTransform.getScaleY();
/* 339 */     double d1 = paramDouble1 * d3 + paramDouble2 * d5 + localAffineTransform.getTranslateX();
/* 340 */     double d2 = paramDouble1 * d4 + paramDouble2 * d6 + localAffineTransform.getTranslateY();
/*     */ 
/* 343 */     double d7 = len(d3, d4) * paramDouble5;
/* 344 */     double d8 = len(d5, d6) * paramDouble5;
/* 345 */     d3 *= paramDouble3;
/* 346 */     d4 *= paramDouble3;
/* 347 */     d5 *= paramDouble4;
/* 348 */     d6 *= paramDouble4;
/* 349 */     if ((paramSunGraphics2D.strokeState < 3) && (paramSunGraphics2D.strokeHint != 2))
/*     */     {
/* 352 */       d9 = normalize(d1);
/* 353 */       d10 = normalize(d2);
/* 354 */       d3 = normalize(d1 + d3) - d9;
/* 355 */       d4 = normalize(d2 + d4) - d10;
/* 356 */       d5 = normalize(d1 + d5) - d9;
/* 357 */       d6 = normalize(d2 + d6) - d10;
/* 358 */       d1 = d9;
/* 359 */       d2 = d10;
/*     */     }
/* 361 */     d7 = Math.max(d7, this.minPenSize);
/* 362 */     d8 = Math.max(d8, this.minPenSize);
/* 363 */     double d9 = len(d3, d4);
/* 364 */     double d10 = len(d5, d6);
/* 365 */     if ((d7 >= d9) || (d8 >= d10))
/*     */     {
/* 369 */       fillOuterParallelogram(paramSunGraphics2D, paramDouble1, paramDouble2, paramDouble1 + paramDouble3, paramDouble2 + paramDouble4, d1, d2, d3, d4, d5, d6, d9, d10, d7, d8);
/*     */     }
/*     */     else
/*     */     {
/* 374 */       this.outrenderer.drawParallelogram(paramSunGraphics2D, paramDouble1, paramDouble2, paramDouble1 + paramDouble3, paramDouble2 + paramDouble4, d1, d2, d3, d4, d5, d6, d7 / d9, d8 / d10);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void fillOuterParallelogram(SunGraphics2D paramSunGraphics2D, double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5, double paramDouble6, double paramDouble7, double paramDouble8, double paramDouble9, double paramDouble10, double paramDouble11, double paramDouble12, double paramDouble13, double paramDouble14)
/*     */   {
/* 397 */     double d1 = paramDouble7 / paramDouble11;
/* 398 */     double d2 = paramDouble8 / paramDouble11;
/* 399 */     double d3 = paramDouble9 / paramDouble12;
/* 400 */     double d4 = paramDouble10 / paramDouble12;
/* 401 */     if (paramDouble11 == 0.0D)
/*     */     {
/* 403 */       if (paramDouble12 == 0.0D)
/*     */       {
/* 405 */         d3 = 0.0D;
/* 406 */         d4 = 1.0D;
/*     */       }
/* 408 */       d1 = d4;
/* 409 */       d2 = -d3;
/* 410 */     } else if (paramDouble12 == 0.0D)
/*     */     {
/* 412 */       d3 = d2;
/* 413 */       d4 = -d1;
/*     */     }
/* 415 */     d1 *= paramDouble13;
/* 416 */     d2 *= paramDouble13;
/* 417 */     d3 *= paramDouble14;
/* 418 */     d4 *= paramDouble14;
/* 419 */     paramDouble5 -= (d1 + d3) / 2.0D;
/* 420 */     paramDouble6 -= (d2 + d4) / 2.0D;
/* 421 */     paramDouble7 += d1;
/* 422 */     paramDouble8 += d2;
/* 423 */     paramDouble9 += d3;
/* 424 */     paramDouble10 += d4;
/*     */ 
/* 426 */     this.outrenderer.fillParallelogram(paramSunGraphics2D, paramDouble1, paramDouble2, paramDouble3, paramDouble4, paramDouble5, paramDouble6, paramDouble7, paramDouble8, paramDouble9, paramDouble10);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.java2d.pipe.PixelToParallelogramConverter
 * JD-Core Version:    0.6.2
 */