/*     */ package sun.print;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Font;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Image;
/*     */ import java.awt.Shape;
/*     */ import java.awt.font.FontRenderContext;
/*     */ import java.awt.font.TextLayout;
/*     */ import java.awt.geom.AffineTransform;
/*     */ import java.awt.geom.Area;
/*     */ import java.awt.geom.Line2D.Float;
/*     */ import java.awt.geom.PathIterator;
/*     */ import java.awt.geom.Point2D.Float;
/*     */ import java.awt.geom.Rectangle2D;
/*     */ import java.awt.geom.Rectangle2D.Float;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.awt.print.PageFormat;
/*     */ import java.awt.print.Printable;
/*     */ import java.awt.print.PrinterException;
/*     */ import java.awt.print.PrinterJob;
/*     */ import sun.awt.image.ByteComponentRaster;
/*     */ 
/*     */ class PSPathGraphics extends PathGraphics
/*     */ {
/*     */   private static final int DEFAULT_USER_RES = 72;
/*     */ 
/*     */   PSPathGraphics(Graphics2D paramGraphics2D, PrinterJob paramPrinterJob, Printable paramPrintable, PageFormat paramPageFormat, int paramInt, boolean paramBoolean)
/*     */   {
/*  71 */     super(paramGraphics2D, paramPrinterJob, paramPrintable, paramPageFormat, paramInt, paramBoolean);
/*     */   }
/*     */ 
/*     */   public Graphics create()
/*     */   {
/*  83 */     return new PSPathGraphics((Graphics2D)getDelegate().create(), getPrinterJob(), getPrintable(), getPageFormat(), getPageIndex(), canDoRedraws());
/*     */   }
/*     */ 
/*     */   public void fill(Shape paramShape, Color paramColor)
/*     */   {
/*  98 */     deviceFill(paramShape.getPathIterator(new AffineTransform()), paramColor);
/*     */   }
/*     */ 
/*     */   public void drawString(String paramString, int paramInt1, int paramInt2)
/*     */   {
/* 114 */     drawString(paramString, paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   public void drawString(String paramString, float paramFloat1, float paramFloat2)
/*     */   {
/* 140 */     drawString(paramString, paramFloat1, paramFloat2, getFont(), getFontRenderContext(), 0.0F);
/*     */   }
/*     */ 
/*     */   protected boolean canDrawStringToWidth()
/*     */   {
/* 145 */     return true;
/*     */   }
/*     */ 
/*     */   protected int platformFontCount(Font paramFont, String paramString) {
/* 149 */     PSPrinterJob localPSPrinterJob = (PSPrinterJob)getPrinterJob();
/* 150 */     return localPSPrinterJob.platformFontCount(paramFont, paramString);
/*     */   }
/*     */ 
/*     */   protected void drawString(String paramString, float paramFloat1, float paramFloat2, Font paramFont, FontRenderContext paramFontRenderContext, float paramFloat3)
/*     */   {
/* 155 */     if (paramString.length() == 0) {
/* 156 */       return;
/*     */     }
/*     */ 
/* 167 */     if ((paramFont.hasLayoutAttributes()) && (!this.printingGlyphVector)) {
/* 168 */       localObject = new TextLayout(paramString, paramFont, paramFontRenderContext);
/* 169 */       ((TextLayout)localObject).draw(this, paramFloat1, paramFloat2);
/* 170 */       return;
/*     */     }
/*     */ 
/* 173 */     Object localObject = getFont();
/* 174 */     if (!((Font)localObject).equals(paramFont))
/* 175 */       setFont(paramFont);
/*     */     else {
/* 177 */       localObject = null;
/*     */     }
/*     */ 
/* 180 */     boolean bool1 = false;
/*     */ 
/* 182 */     float f1 = 0.0F; float f2 = 0.0F;
/* 183 */     boolean bool2 = getFont().isTransformed();
/*     */ 
/* 185 */     if (bool2) {
/* 186 */       AffineTransform localAffineTransform = getFont().getTransform();
/* 187 */       int j = localAffineTransform.getType();
/*     */ 
/* 192 */       if (j == 1) {
/* 193 */         f1 = (float)localAffineTransform.getTranslateX();
/* 194 */         f2 = (float)localAffineTransform.getTranslateY();
/* 195 */         if (Math.abs(f1) < 1.E-005D) f1 = 0.0F;
/* 196 */         if (Math.abs(f2) < 1.E-005D) f2 = 0.0F;
/* 197 */         bool2 = false;
/*     */       }
/*     */     }
/*     */ 
/* 201 */     int i = !bool2 ? 1 : 0;
/*     */ 
/* 203 */     if ((!PSPrinterJob.shapeTextProp) && (i != 0))
/*     */     {
/* 205 */       PSPrinterJob localPSPrinterJob = (PSPrinterJob)getPrinterJob();
/* 206 */       if (localPSPrinterJob.setFont(getFont()))
/*     */       {
/*     */         try
/*     */         {
/* 216 */           localPSPrinterJob.setColor((Color)getPaint());
/*     */         } catch (ClassCastException localClassCastException) {
/* 218 */           if (localObject != null) {
/* 219 */             setFont((Font)localObject);
/*     */           }
/* 221 */           throw new IllegalArgumentException("Expected a Color instance");
/*     */         }
/*     */ 
/* 225 */         localPSPrinterJob.setTransform(getTransform());
/* 226 */         localPSPrinterJob.setClip(getClip());
/*     */ 
/* 228 */         bool1 = localPSPrinterJob.textOut(this, paramString, paramFloat1 + f1, paramFloat2 + f2, paramFont, paramFontRenderContext, paramFloat3);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 237 */     if (!bool1) {
/* 238 */       if (localObject != null) {
/* 239 */         setFont((Font)localObject);
/* 240 */         localObject = null;
/*     */       }
/* 242 */       super.drawString(paramString, paramFloat1, paramFloat2, paramFont, paramFontRenderContext, paramFloat3);
/*     */     }
/*     */ 
/* 245 */     if (localObject != null)
/* 246 */       setFont((Font)localObject);
/*     */   }
/*     */ 
/*     */   protected boolean drawImageToPlatform(Image paramImage, AffineTransform paramAffineTransform, Color paramColor, int paramInt1, int paramInt2, int paramInt3, int paramInt4, boolean paramBoolean)
/*     */   {
/* 285 */     BufferedImage localBufferedImage = getBufferedImage(paramImage);
/* 286 */     if (localBufferedImage == null) {
/* 287 */       return true;
/*     */     }
/*     */ 
/* 290 */     PSPrinterJob localPSPrinterJob = (PSPrinterJob)getPrinterJob();
/*     */ 
/* 298 */     AffineTransform localAffineTransform1 = getTransform();
/* 299 */     if (paramAffineTransform == null) {
/* 300 */       paramAffineTransform = new AffineTransform();
/*     */     }
/* 302 */     localAffineTransform1.concatenate(paramAffineTransform);
/*     */ 
/* 320 */     double[] arrayOfDouble = new double[6];
/* 321 */     localAffineTransform1.getMatrix(arrayOfDouble);
/*     */ 
/* 332 */     Point2D.Float localFloat1 = new Point2D.Float(1.0F, 0.0F);
/* 333 */     Point2D.Float localFloat2 = new Point2D.Float(0.0F, 1.0F);
/* 334 */     localAffineTransform1.deltaTransform(localFloat1, localFloat1);
/* 335 */     localAffineTransform1.deltaTransform(localFloat2, localFloat2);
/*     */ 
/* 337 */     Point2D.Float localFloat3 = new Point2D.Float(0.0F, 0.0F);
/* 338 */     double d1 = localFloat1.distance(localFloat3);
/* 339 */     double d2 = localFloat2.distance(localFloat3);
/*     */ 
/* 341 */     double d3 = localPSPrinterJob.getXRes();
/* 342 */     double d4 = localPSPrinterJob.getYRes();
/* 343 */     double d5 = d3 / 72.0D;
/* 344 */     double d6 = d4 / 72.0D;
/*     */ 
/* 347 */     int i = localAffineTransform1.getType();
/* 348 */     int j = (i & 0x30) != 0 ? 1 : 0;
/*     */ 
/* 351 */     if (j != 0) {
/* 352 */       if (d1 > d5) d1 = d5;
/* 353 */       if (d2 > d6) d2 = d6;
/*     */ 
/*     */     }
/*     */ 
/* 359 */     if ((d1 != 0.0D) && (d2 != 0.0D))
/*     */     {
/* 363 */       AffineTransform localAffineTransform2 = new AffineTransform(arrayOfDouble[0] / d1, arrayOfDouble[1] / d2, arrayOfDouble[2] / d1, arrayOfDouble[3] / d2, arrayOfDouble[4] / d1, arrayOfDouble[5] / d2);
/*     */ 
/* 391 */       Rectangle2D.Float localFloat4 = new Rectangle2D.Float(paramInt1, paramInt2, paramInt3, paramInt4);
/*     */ 
/* 395 */       Shape localShape1 = localAffineTransform2.createTransformedShape(localFloat4);
/* 396 */       Rectangle2D localRectangle2D = localShape1.getBounds2D();
/*     */ 
/* 402 */       localRectangle2D.setRect(localRectangle2D.getX(), localRectangle2D.getY(), localRectangle2D.getWidth() + 0.001D, localRectangle2D.getHeight() + 0.001D);
/*     */ 
/* 406 */       int k = (int)localRectangle2D.getWidth();
/* 407 */       int m = (int)localRectangle2D.getHeight();
/*     */ 
/* 409 */       if ((k > 0) && (m > 0))
/*     */       {
/* 425 */         int n = 1;
/* 426 */         if ((!paramBoolean) && (hasTransparentPixels(localBufferedImage))) {
/* 427 */           n = 0;
/* 428 */           if (isBitmaskTransparency(localBufferedImage)) {
/* 429 */             if (paramColor == null) {
/* 430 */               if (drawBitmaskImage(localBufferedImage, paramAffineTransform, paramColor, paramInt1, paramInt2, paramInt3, paramInt4))
/*     */               {
/* 434 */                 return true;
/*     */               }
/* 436 */             } else if (paramColor.getTransparency() == 1)
/*     */             {
/* 438 */               n = 1;
/*     */             }
/*     */           }
/* 441 */           if (!canDoRedraws()) {
/* 442 */             n = 1;
/*     */           }
/*     */ 
/*     */         }
/*     */         else
/*     */         {
/* 448 */           paramColor = null;
/*     */         }
/*     */ 
/* 452 */         if (((paramInt1 + paramInt3 > localBufferedImage.getWidth(null)) || (paramInt2 + paramInt4 > localBufferedImage.getHeight(null))) && (canDoRedraws()))
/*     */         {
/* 455 */           n = 0;
/*     */         }
/* 457 */         if (n == 0)
/*     */         {
/* 459 */           localAffineTransform1.getMatrix(arrayOfDouble);
/* 460 */           localObject1 = new AffineTransform(arrayOfDouble[0] / d5, arrayOfDouble[1] / d6, arrayOfDouble[2] / d5, arrayOfDouble[3] / d6, arrayOfDouble[4] / d5, arrayOfDouble[5] / d6);
/*     */ 
/* 469 */           localObject2 = new Rectangle2D.Float(paramInt1, paramInt2, paramInt3, paramInt4);
/*     */ 
/* 472 */           localShape2 = localAffineTransform1.createTransformedShape((Shape)localObject2);
/*     */ 
/* 475 */           localObject3 = localShape2.getBounds2D();
/*     */ 
/* 477 */           ((Rectangle2D)localObject3).setRect(((Rectangle2D)localObject3).getX(), ((Rectangle2D)localObject3).getY(), ((Rectangle2D)localObject3).getWidth() + 0.001D, ((Rectangle2D)localObject3).getHeight() + 0.001D);
/*     */ 
/* 486 */           int i1 = (int)((Rectangle2D)localObject3).getWidth();
/* 487 */           int i2 = (int)((Rectangle2D)localObject3).getHeight();
/* 488 */           int i3 = i1 * i2 * 3;
/* 489 */           int i4 = 8388608;
/* 490 */           double d7 = d3 < d4 ? d3 : d4;
/* 491 */           int i5 = (int)d7;
/* 492 */           double d8 = 1.0D;
/*     */ 
/* 494 */           double d9 = i1 / k;
/* 495 */           double d10 = i2 / m;
/* 496 */           double d11 = d9 > d10 ? d10 : d9;
/* 497 */           int i6 = (int)(i5 / d11);
/* 498 */           if (i6 < 72) i6 = 72;
/*     */ 
/* 500 */           while ((i3 > i4) && (i5 > i6)) {
/* 501 */             d8 *= 2.0D;
/* 502 */             i5 /= 2;
/* 503 */             i3 /= 4;
/*     */           }
/* 505 */           if (i5 < i6) {
/* 506 */             d8 = d7 / i6;
/*     */           }
/*     */ 
/* 509 */           ((Rectangle2D)localObject3).setRect(((Rectangle2D)localObject3).getX() / d8, ((Rectangle2D)localObject3).getY() / d8, ((Rectangle2D)localObject3).getWidth() / d8, ((Rectangle2D)localObject3).getHeight() / d8);
/*     */ 
/* 523 */           localPSPrinterJob.saveState(getTransform(), getClip(), (Rectangle2D)localObject3, d8, d8);
/*     */ 
/* 525 */           return true;
/*     */         }
/*     */ 
/* 537 */         Object localObject1 = new BufferedImage((int)localRectangle2D.getWidth(), (int)localRectangle2D.getHeight(), 5);
/*     */ 
/* 545 */         Object localObject2 = ((BufferedImage)localObject1).createGraphics();
/* 546 */         ((Graphics2D)localObject2).clipRect(0, 0, ((BufferedImage)localObject1).getWidth(), ((BufferedImage)localObject1).getHeight());
/*     */ 
/* 550 */         ((Graphics2D)localObject2).translate(-localRectangle2D.getX(), -localRectangle2D.getY());
/*     */ 
/* 552 */         ((Graphics2D)localObject2).transform(localAffineTransform2);
/*     */ 
/* 557 */         if (paramColor == null) {
/* 558 */           paramColor = Color.white;
/*     */         }
/*     */ 
/* 562 */         ((Graphics2D)localObject2).drawImage(localBufferedImage, paramInt1, paramInt2, paramInt1 + paramInt3, paramInt2 + paramInt4, paramInt1, paramInt2, paramInt1 + paramInt3, paramInt2 + paramInt4, paramColor, null);
/*     */ 
/* 577 */         Shape localShape2 = getClip();
/* 578 */         Object localObject3 = getTransform().createTransformedShape(localShape2);
/*     */ 
/* 580 */         AffineTransform localAffineTransform3 = AffineTransform.getScaleInstance(d1, d2);
/*     */ 
/* 582 */         Shape localShape3 = localAffineTransform3.createTransformedShape(localShape1);
/* 583 */         Area localArea1 = new Area(localShape3);
/* 584 */         Area localArea2 = new Area((Shape)localObject3);
/* 585 */         localArea1.intersect(localArea2);
/* 586 */         localPSPrinterJob.setClip(localArea1);
/*     */ 
/* 595 */         Rectangle2D.Float localFloat5 = new Rectangle2D.Float((float)(localRectangle2D.getX() * d1), (float)(localRectangle2D.getY() * d2), (float)(localRectangle2D.getWidth() * d1), (float)(localRectangle2D.getHeight() * d2));
/*     */ 
/* 606 */         ByteComponentRaster localByteComponentRaster = (ByteComponentRaster)((BufferedImage)localObject1).getRaster();
/*     */ 
/* 609 */         localPSPrinterJob.drawImageBGR(localByteComponentRaster.getDataStorage(), localFloat5.x, localFloat5.y, (float)Math.rint(localFloat5.width + 0.5D), (float)Math.rint(localFloat5.height + 0.5D), 0.0F, 0.0F, ((BufferedImage)localObject1).getWidth(), ((BufferedImage)localObject1).getHeight(), ((BufferedImage)localObject1).getWidth(), ((BufferedImage)localObject1).getHeight());
/*     */ 
/* 618 */         localPSPrinterJob.setClip(getTransform().createTransformedShape(localShape2));
/*     */ 
/* 622 */         ((Graphics2D)localObject2).dispose();
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 628 */     return true;
/*     */   }
/*     */ 
/*     */   public void redrawRegion(Rectangle2D paramRectangle2D, double paramDouble1, double paramDouble2, Shape paramShape, AffineTransform paramAffineTransform)
/*     */     throws PrinterException
/*     */   {
/* 642 */     PSPrinterJob localPSPrinterJob = (PSPrinterJob)getPrinterJob();
/* 643 */     Printable localPrintable = getPrintable();
/* 644 */     PageFormat localPageFormat = getPageFormat();
/* 645 */     int i = getPageIndex();
/*     */ 
/* 650 */     BufferedImage localBufferedImage = new BufferedImage((int)paramRectangle2D.getWidth(), (int)paramRectangle2D.getHeight(), 5);
/*     */ 
/* 661 */     Graphics2D localGraphics2D = localBufferedImage.createGraphics();
/* 662 */     ProxyGraphics2D localProxyGraphics2D = new ProxyGraphics2D(localGraphics2D, localPSPrinterJob);
/* 663 */     localProxyGraphics2D.setColor(Color.white);
/* 664 */     localProxyGraphics2D.fillRect(0, 0, localBufferedImage.getWidth(), localBufferedImage.getHeight());
/* 665 */     localProxyGraphics2D.clipRect(0, 0, localBufferedImage.getWidth(), localBufferedImage.getHeight());
/*     */ 
/* 667 */     localProxyGraphics2D.translate(-paramRectangle2D.getX(), -paramRectangle2D.getY());
/*     */ 
/* 671 */     float f1 = (float)(localPSPrinterJob.getXRes() / paramDouble1);
/* 672 */     float f2 = (float)(localPSPrinterJob.getYRes() / paramDouble2);
/*     */ 
/* 678 */     localProxyGraphics2D.scale(f1 / 72.0F, f2 / 72.0F);
/*     */ 
/* 680 */     localProxyGraphics2D.translate(-localPSPrinterJob.getPhysicalPrintableX(localPageFormat.getPaper()) / localPSPrinterJob.getXRes() * 72.0D, -localPSPrinterJob.getPhysicalPrintableY(localPageFormat.getPaper()) / localPSPrinterJob.getYRes() * 72.0D);
/*     */ 
/* 686 */     localProxyGraphics2D.transform(new AffineTransform(getPageFormat().getMatrix()));
/*     */ 
/* 688 */     localProxyGraphics2D.setPaint(Color.black);
/*     */ 
/* 690 */     localPrintable.print(localProxyGraphics2D, localPageFormat, i);
/*     */ 
/* 692 */     localGraphics2D.dispose();
/*     */ 
/* 697 */     localPSPrinterJob.setClip(paramAffineTransform.createTransformedShape(paramShape));
/*     */ 
/* 707 */     Rectangle2D.Float localFloat = new Rectangle2D.Float((float)(paramRectangle2D.getX() * paramDouble1), (float)(paramRectangle2D.getY() * paramDouble2), (float)(paramRectangle2D.getWidth() * paramDouble1), (float)(paramRectangle2D.getHeight() * paramDouble2));
/*     */ 
/* 718 */     ByteComponentRaster localByteComponentRaster = (ByteComponentRaster)localBufferedImage.getRaster();
/*     */ 
/* 720 */     localPSPrinterJob.drawImageBGR(localByteComponentRaster.getDataStorage(), localFloat.x, localFloat.y, localFloat.width, localFloat.height, 0.0F, 0.0F, localBufferedImage.getWidth(), localBufferedImage.getHeight(), localBufferedImage.getWidth(), localBufferedImage.getHeight());
/*     */   }
/*     */ 
/*     */   protected void deviceFill(PathIterator paramPathIterator, Color paramColor)
/*     */   {
/* 739 */     PSPrinterJob localPSPrinterJob = (PSPrinterJob)getPrinterJob();
/* 740 */     localPSPrinterJob.deviceFill(paramPathIterator, paramColor, getTransform(), getClip());
/*     */   }
/*     */ 
/*     */   protected void deviceFrameRect(int paramInt1, int paramInt2, int paramInt3, int paramInt4, Color paramColor)
/*     */   {
/* 750 */     draw(new Rectangle2D.Float(paramInt1, paramInt2, paramInt3, paramInt4));
/*     */   }
/*     */ 
/*     */   protected void deviceDrawLine(int paramInt1, int paramInt2, int paramInt3, int paramInt4, Color paramColor)
/*     */   {
/* 760 */     draw(new Line2D.Float(paramInt1, paramInt2, paramInt3, paramInt4));
/*     */   }
/*     */ 
/*     */   protected void deviceFillRect(int paramInt1, int paramInt2, int paramInt3, int paramInt4, Color paramColor)
/*     */   {
/* 768 */     fill(new Rectangle2D.Float(paramInt1, paramInt2, paramInt3, paramInt4));
/*     */   }
/*     */ 
/*     */   protected void deviceClip(PathIterator paramPathIterator)
/*     */   {
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.print.PSPathGraphics
 * JD-Core Version:    0.6.2
 */