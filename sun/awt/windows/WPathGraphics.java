/*      */ package sun.awt.windows;
/*      */ 
/*      */ import java.awt.BasicStroke;
/*      */ import java.awt.Color;
/*      */ import java.awt.Font;
/*      */ import java.awt.Graphics;
/*      */ import java.awt.Graphics2D;
/*      */ import java.awt.Image;
/*      */ import java.awt.Shape;
/*      */ import java.awt.Stroke;
/*      */ import java.awt.font.FontRenderContext;
/*      */ import java.awt.font.GlyphVector;
/*      */ import java.awt.font.TextLayout;
/*      */ import java.awt.geom.AffineTransform;
/*      */ import java.awt.geom.Line2D.Float;
/*      */ import java.awt.geom.NoninvertibleTransformException;
/*      */ import java.awt.geom.PathIterator;
/*      */ import java.awt.geom.Point2D;
/*      */ import java.awt.geom.Point2D.Double;
/*      */ import java.awt.geom.Point2D.Float;
/*      */ import java.awt.geom.Rectangle2D;
/*      */ import java.awt.geom.Rectangle2D.Float;
/*      */ import java.awt.image.BufferedImage;
/*      */ import java.awt.image.ColorModel;
/*      */ import java.awt.image.ComponentSampleModel;
/*      */ import java.awt.image.IndexColorModel;
/*      */ import java.awt.image.MultiPixelPackedSampleModel;
/*      */ import java.awt.image.SampleModel;
/*      */ import java.awt.image.WritableRaster;
/*      */ import java.awt.print.PageFormat;
/*      */ import java.awt.print.Printable;
/*      */ import java.awt.print.PrinterException;
/*      */ import java.awt.print.PrinterJob;
/*      */ import java.security.AccessController;
/*      */ import java.util.Arrays;
/*      */ import sun.awt.image.ByteComponentRaster;
/*      */ import sun.awt.image.BytePackedRaster;
/*      */ import sun.font.CharToGlyphMapper;
/*      */ import sun.font.CompositeFont;
/*      */ import sun.font.Font2D;
/*      */ import sun.font.FontUtilities;
/*      */ import sun.font.PhysicalFont;
/*      */ import sun.font.TrueTypeFont;
/*      */ import sun.print.PathGraphics;
/*      */ import sun.print.ProxyGraphics2D;
/*      */ import sun.security.action.GetPropertyAction;
/*      */ 
/*      */ class WPathGraphics extends PathGraphics
/*      */ {
/*      */   private static final int DEFAULT_USER_RES = 72;
/*      */   private static final float MIN_DEVICE_LINEWIDTH = 1.2F;
/*      */   private static final float MAX_THINLINE_INCHES = 0.014F;
/*   93 */   private static boolean useGDITextLayout = true;
/*   94 */   private static boolean preferGDITextLayout = false;
/*      */ 
/*      */   WPathGraphics(Graphics2D paramGraphics2D, PrinterJob paramPrinterJob, Printable paramPrintable, PageFormat paramPageFormat, int paramInt, boolean paramBoolean)
/*      */   {
/*  115 */     super(paramGraphics2D, paramPrinterJob, paramPrintable, paramPageFormat, paramInt, paramBoolean);
/*      */   }
/*      */ 
/*      */   public Graphics create()
/*      */   {
/*  128 */     return new WPathGraphics((Graphics2D)getDelegate().create(), getPrinterJob(), getPrintable(), getPageFormat(), getPageIndex(), canDoRedraws());
/*      */   }
/*      */ 
/*      */   public void draw(Shape paramShape)
/*      */   {
/*  153 */     Stroke localStroke = getStroke();
/*      */ 
/*  160 */     if ((localStroke instanceof BasicStroke))
/*      */     {
/*  162 */       BasicStroke localBasicStroke2 = null;
/*      */ 
/*  170 */       BasicStroke localBasicStroke1 = (BasicStroke)localStroke;
/*  171 */       float f2 = localBasicStroke1.getLineWidth();
/*  172 */       Point2D.Float localFloat1 = new Point2D.Float(f2, f2);
/*      */ 
/*  180 */       AffineTransform localAffineTransform1 = getTransform();
/*  181 */       localAffineTransform1.deltaTransform(localFloat1, localFloat1);
/*  182 */       float f1 = Math.min(Math.abs(localFloat1.x), Math.abs(localFloat1.y));
/*      */ 
/*  189 */       if (f1 < 1.2F)
/*      */       {
/*  191 */         Point2D.Float localFloat2 = new Point2D.Float(1.2F, 1.2F);
/*      */         try
/*      */         {
/*  202 */           AffineTransform localAffineTransform2 = localAffineTransform1.createInverse();
/*  203 */           localAffineTransform2.deltaTransform(localFloat2, localFloat2);
/*      */ 
/*  205 */           float f3 = Math.max(Math.abs(localFloat2.x), Math.abs(localFloat2.y));
/*      */ 
/*  212 */           localBasicStroke2 = new BasicStroke(f3, localBasicStroke1.getEndCap(), localBasicStroke1.getLineJoin(), localBasicStroke1.getMiterLimit(), localBasicStroke1.getDashArray(), localBasicStroke1.getDashPhase());
/*      */ 
/*  218 */           setStroke(localBasicStroke2);
/*      */         }
/*      */         catch (NoninvertibleTransformException localNoninvertibleTransformException)
/*      */         {
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  228 */       super.draw(paramShape);
/*      */ 
/*  234 */       if (localBasicStroke2 != null) {
/*  235 */         setStroke(localBasicStroke1);
/*      */       }
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/*  242 */       super.draw(paramShape);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void drawString(String paramString, int paramInt1, int paramInt2)
/*      */   {
/*  260 */     drawString(paramString, paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   public void drawString(String paramString, float paramFloat1, float paramFloat2)
/*      */   {
/*  265 */     drawString(paramString, paramFloat1, paramFloat2, getFont(), getFontRenderContext(), 0.0F);
/*      */   }
/*      */ 
/*      */   protected int platformFontCount(Font paramFont, String paramString)
/*      */   {
/*  283 */     AffineTransform localAffineTransform1 = getTransform();
/*  284 */     AffineTransform localAffineTransform2 = new AffineTransform(localAffineTransform1);
/*  285 */     localAffineTransform2.concatenate(getFont().getTransform());
/*  286 */     int i = localAffineTransform2.getType();
/*      */ 
/*  289 */     int j = (i != 32) && ((i & 0x40) == 0) ? 1 : 0;
/*      */ 
/*  294 */     if (j == 0) {
/*  295 */       return 0;
/*      */     }
/*      */ 
/*  305 */     Font2D localFont2D = FontUtilities.getFont2D(paramFont);
/*  306 */     if (((localFont2D instanceof CompositeFont)) || ((localFont2D instanceof TrueTypeFont)))
/*      */     {
/*  308 */       return 1;
/*      */     }
/*  310 */     return 0;
/*      */   }
/*      */ 
/*      */   private static boolean isXP()
/*      */   {
/*  315 */     String str = System.getProperty("os.version");
/*  316 */     if (str != null) {
/*  317 */       Float localFloat = Float.valueOf(str);
/*  318 */       return localFloat.floatValue() >= 5.1F;
/*      */     }
/*  320 */     return false;
/*      */   }
/*      */ 
/*      */   private boolean strNeedsTextLayout(String paramString, Font paramFont)
/*      */   {
/*  330 */     char[] arrayOfChar = paramString.toCharArray();
/*  331 */     boolean bool = FontUtilities.isComplexText(arrayOfChar, 0, arrayOfChar.length);
/*  332 */     if (!bool)
/*  333 */       return false;
/*  334 */     if (!useGDITextLayout) {
/*  335 */       return true;
/*      */     }
/*  337 */     if ((preferGDITextLayout) || ((isXP()) && (FontUtilities.textLayoutIsCompatible(paramFont))))
/*      */     {
/*  339 */       return false;
/*      */     }
/*  341 */     return true;
/*      */   }
/*      */ 
/*      */   private int getAngle(Point2D.Double paramDouble)
/*      */   {
/*  351 */     double d = Math.toDegrees(Math.atan2(paramDouble.y, paramDouble.x));
/*  352 */     if (d < 0.0D) {
/*  353 */       d += 360.0D;
/*      */     }
/*      */ 
/*  361 */     if (d != 0.0D) {
/*  362 */       d = 360.0D - d;
/*      */     }
/*  364 */     return (int)Math.round(d * 10.0D);
/*      */   }
/*      */ 
/*      */   private float getAwScale(double paramDouble1, double paramDouble2)
/*      */   {
/*  369 */     float f = (float)(paramDouble1 / paramDouble2);
/*      */ 
/*  371 */     if ((f > 0.999F) && (f < 1.001F)) {
/*  372 */       f = 1.0F;
/*      */     }
/*  374 */     return f;
/*      */   }
/*      */ 
/*      */   public void drawString(String paramString, float paramFloat1, float paramFloat2, Font paramFont, FontRenderContext paramFontRenderContext, float paramFloat3)
/*      */   {
/*  402 */     if (paramString.length() == 0) {
/*  403 */       return;
/*      */     }
/*      */ 
/*  406 */     if (WPrinterJob.shapeTextProp) {
/*  407 */       super.drawString(paramString, paramFloat1, paramFloat2, paramFont, paramFontRenderContext, paramFloat3);
/*  408 */       return;
/*      */     }
/*      */ 
/*  422 */     boolean bool = strNeedsTextLayout(paramString, paramFont);
/*  423 */     if (((paramFont.hasLayoutAttributes()) || (bool)) && (!this.printingGlyphVector))
/*      */     {
/*  425 */       localObject = new TextLayout(paramString, paramFont, paramFontRenderContext);
/*  426 */       ((TextLayout)localObject).draw(this, paramFloat1, paramFloat2);
/*  427 */       return;
/*  428 */     }if (bool) {
/*  429 */       super.drawString(paramString, paramFloat1, paramFloat2, paramFont, paramFontRenderContext, paramFloat3);
/*  430 */       return;
/*      */     }
/*      */ 
/*  433 */     Object localObject = getTransform();
/*  434 */     AffineTransform localAffineTransform1 = new AffineTransform((AffineTransform)localObject);
/*  435 */     localAffineTransform1.concatenate(paramFont.getTransform());
/*  436 */     int i = localAffineTransform1.getType();
/*      */ 
/*  443 */     int j = (i != 32) && ((i & 0x40) == 0) ? 1 : 0;
/*      */ 
/*  448 */     WPrinterJob localWPrinterJob = (WPrinterJob)getPrinterJob();
/*      */     try {
/*  450 */       localWPrinterJob.setTextColor((Color)getPaint());
/*      */     } catch (ClassCastException localClassCastException) {
/*  452 */       j = 0;
/*      */     }
/*      */ 
/*  455 */     if (j == 0) {
/*  456 */       super.drawString(paramString, paramFloat1, paramFloat2, paramFont, paramFontRenderContext, paramFloat3);
/*  457 */       return;
/*      */     }
/*      */ 
/*  468 */     Point2D.Float localFloat1 = new Point2D.Float(paramFloat1, paramFloat2);
/*  469 */     Point2D.Float localFloat2 = new Point2D.Float();
/*      */ 
/*  474 */     if (paramFont.isTransformed()) {
/*  475 */       AffineTransform localAffineTransform2 = paramFont.getTransform();
/*  476 */       float f2 = (float)localAffineTransform2.getTranslateX();
/*  477 */       float f3 = (float)localAffineTransform2.getTranslateY();
/*  478 */       if (Math.abs(f2) < 1.E-005D) f2 = 0.0F;
/*  479 */       if (Math.abs(f3) < 1.E-005D) f3 = 0.0F;
/*  480 */       localFloat1.x += f2; localFloat1.y += f3;
/*      */     }
/*  482 */     ((AffineTransform)localObject).transform(localFloat1, localFloat2);
/*      */ 
/*  484 */     if (getClip() != null) {
/*  485 */       deviceClip(getClip().getPathIterator((AffineTransform)localObject));
/*      */     }
/*      */ 
/*  495 */     float f1 = paramFont.getSize2D();
/*      */ 
/*  497 */     Point2D.Double localDouble1 = new Point2D.Double(0.0D, 1.0D);
/*  498 */     localAffineTransform1.deltaTransform(localDouble1, localDouble1);
/*  499 */     double d1 = Math.sqrt(localDouble1.x * localDouble1.x + localDouble1.y * localDouble1.y);
/*  500 */     float f4 = (float)(f1 * d1);
/*      */ 
/*  502 */     Point2D.Double localDouble2 = new Point2D.Double(1.0D, 0.0D);
/*  503 */     localAffineTransform1.deltaTransform(localDouble2, localDouble2);
/*  504 */     double d2 = Math.sqrt(localDouble2.x * localDouble2.x + localDouble2.y * localDouble2.y);
/*  505 */     float f5 = (float)(f1 * d2);
/*      */ 
/*  507 */     float f6 = getAwScale(d2, d1);
/*  508 */     int k = getAngle(localDouble2);
/*      */ 
/*  510 */     Font2D localFont2D = FontUtilities.getFont2D(paramFont);
/*  511 */     if ((localFont2D instanceof TrueTypeFont)) {
/*  512 */       textOut(paramString, paramFont, (TrueTypeFont)localFont2D, paramFontRenderContext, f4, k, f6, (AffineTransform)localObject, d2, paramFloat1, paramFloat2, localFloat2.x, localFloat2.y, paramFloat3);
/*      */     }
/*  516 */     else if ((localFont2D instanceof CompositeFont))
/*      */     {
/*  524 */       CompositeFont localCompositeFont = (CompositeFont)localFont2D;
/*  525 */       float f7 = paramFloat1; float f8 = paramFloat2;
/*  526 */       float f9 = localFloat2.x; float f10 = localFloat2.y;
/*  527 */       char[] arrayOfChar = paramString.toCharArray();
/*  528 */       int m = arrayOfChar.length;
/*  529 */       int[] arrayOfInt = new int[m];
/*  530 */       localCompositeFont.getMapper().charsToGlyphs(m, arrayOfChar, arrayOfInt);
/*      */ 
/*  532 */       int n = 0; int i1 = 0; int i2 = 0;
/*  533 */       while (i1 < m)
/*      */       {
/*  535 */         n = i1;
/*  536 */         i2 = arrayOfInt[n] >>> 24;
/*      */ 
/*  538 */         while ((i1 < m) && (arrayOfInt[i1] >>> 24 == i2)) {
/*  539 */           i1++;
/*      */         }
/*  541 */         String str = new String(arrayOfChar, n, i1 - n);
/*  542 */         PhysicalFont localPhysicalFont = localCompositeFont.getSlotFont(i2);
/*  543 */         textOut(str, paramFont, localPhysicalFont, paramFontRenderContext, f4, k, f6, (AffineTransform)localObject, d2, f7, f8, f9, f10, 0.0F);
/*      */ 
/*  547 */         Rectangle2D localRectangle2D = paramFont.getStringBounds(str, paramFontRenderContext);
/*  548 */         float f11 = (float)localRectangle2D.getWidth();
/*  549 */         f7 += f11;
/*  550 */         localFloat1.x += f11;
/*  551 */         ((AffineTransform)localObject).transform(localFloat1, localFloat2);
/*  552 */         f9 = localFloat2.x;
/*  553 */         f10 = localFloat2.y;
/*      */       }
/*      */     } else {
/*  556 */       super.drawString(paramString, paramFloat1, paramFloat2, paramFont, paramFontRenderContext, paramFloat3);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected boolean printGlyphVector(GlyphVector paramGlyphVector, float paramFloat1, float paramFloat2)
/*      */   {
/*  569 */     if ((paramGlyphVector.getLayoutFlags() & 0x1) != 0) {
/*  570 */       return false;
/*      */     }
/*      */ 
/*  573 */     AffineTransform localAffineTransform1 = getTransform();
/*  574 */     AffineTransform localAffineTransform2 = new AffineTransform(localAffineTransform1);
/*  575 */     Font localFont = paramGlyphVector.getFont();
/*  576 */     localAffineTransform2.concatenate(localFont.getTransform());
/*  577 */     int i = localAffineTransform2.getType();
/*      */ 
/*  584 */     int j = (i != 32) && ((i & 0x40) == 0) ? 1 : 0;
/*      */ 
/*  588 */     WPrinterJob localWPrinterJob = (WPrinterJob)getPrinterJob();
/*      */     try {
/*  590 */       localWPrinterJob.setTextColor((Color)getPaint());
/*      */     } catch (ClassCastException localClassCastException) {
/*  592 */       j = 0;
/*      */     }
/*      */ 
/*  595 */     if ((WPrinterJob.shapeTextProp) || (j == 0)) {
/*  596 */       return false;
/*      */     }
/*      */ 
/*  601 */     Point2D.Float localFloat1 = new Point2D.Float(paramFloat1, paramFloat2);
/*  602 */     Point2D.Float localFloat2 = new Point2D.Float();
/*      */ 
/*  607 */     if (localFont.isTransformed()) {
/*  608 */       AffineTransform localAffineTransform3 = localFont.getTransform();
/*  609 */       float f2 = (float)localAffineTransform3.getTranslateX();
/*  610 */       float f3 = (float)localAffineTransform3.getTranslateY();
/*  611 */       if (Math.abs(f2) < 1.E-005D) f2 = 0.0F;
/*  612 */       if (Math.abs(f3) < 1.E-005D) f3 = 0.0F;
/*  613 */       localFloat1.x += f2; localFloat1.y += f3;
/*      */     }
/*  615 */     localAffineTransform1.transform(localFloat1, localFloat2);
/*      */ 
/*  617 */     if (getClip() != null) {
/*  618 */       deviceClip(getClip().getPathIterator(localAffineTransform1));
/*      */     }
/*      */ 
/*  628 */     float f1 = localFont.getSize2D();
/*      */ 
/*  630 */     Point2D.Double localDouble1 = new Point2D.Double(0.0D, 1.0D);
/*  631 */     localAffineTransform2.deltaTransform(localDouble1, localDouble1);
/*  632 */     double d1 = Math.sqrt(localDouble1.x * localDouble1.x + localDouble1.y * localDouble1.y);
/*  633 */     float f4 = (float)(f1 * d1);
/*      */ 
/*  635 */     Point2D.Double localDouble2 = new Point2D.Double(1.0D, 0.0D);
/*  636 */     localAffineTransform2.deltaTransform(localDouble2, localDouble2);
/*  637 */     double d2 = Math.sqrt(localDouble2.x * localDouble2.x + localDouble2.y * localDouble2.y);
/*  638 */     float f5 = (float)(f1 * d2);
/*      */ 
/*  640 */     float f6 = getAwScale(d2, d1);
/*  641 */     int k = getAngle(localDouble2);
/*      */ 
/*  643 */     int m = paramGlyphVector.getNumGlyphs();
/*  644 */     Object localObject1 = paramGlyphVector.getGlyphCodes(0, m, null);
/*  645 */     Object localObject2 = paramGlyphVector.getGlyphPositions(0, m, null);
/*      */ 
/*  658 */     int n = 0;
/*  659 */     for (int i1 = 0; i1 < m; i1++)
/*  660 */       if ((localObject1[i1] & 0xFFFF) >= 65534)
/*      */       {
/*  662 */         n++;
/*      */       }
/*      */     int i3;
/*  665 */     if (n > 0) {
/*  666 */       i1 = m - n;
/*  667 */       localObject3 = new int[i1];
/*  668 */       localObject4 = new float[i1 * 2];
/*  669 */       int i2 = 0;
/*  670 */       for (i3 = 0; i3 < m; i3++) {
/*  671 */         if ((localObject1[i3] & 0xFFFF) < 65534)
/*      */         {
/*  673 */           localObject3[i2] = localObject1[i3];
/*  674 */           localObject4[(i2 * 2)] = localObject2[(i3 * 2)];
/*  675 */           localObject4[(i2 * 2 + 1)] = localObject2[(i3 * 2 + 1)];
/*  676 */           i2++;
/*      */         }
/*      */       }
/*  679 */       m = i1;
/*  680 */       localObject1 = localObject3;
/*  681 */       localObject2 = localObject4;
/*      */     }
/*      */ 
/*  699 */     AffineTransform localAffineTransform4 = new AffineTransform(localAffineTransform1);
/*      */ 
/*  701 */     localAffineTransform4.rotate(k * 3.141592653589793D / 1800.0D);
/*  702 */     Object localObject3 = new float[localObject2.length];
/*      */ 
/*  704 */     localAffineTransform4.transform((float[])localObject2, 0, (float[])localObject3, 0, localObject2.length / 2);
/*      */ 
/*  708 */     Object localObject4 = FontUtilities.getFont2D(localFont);
/*      */     Object localObject5;
/*  709 */     if ((localObject4 instanceof TrueTypeFont)) {
/*  710 */       localObject5 = ((Font2D)localObject4).getFamilyName(null);
/*  711 */       i3 = localFont.getStyle() | ((Font2D)localObject4).getStyle();
/*  712 */       if (!localWPrinterJob.setFont((String)localObject5, f4, i3, k, f6))
/*      */       {
/*  714 */         return false;
/*      */       }
/*  716 */       localWPrinterJob.glyphsOut((int[])localObject1, localFloat2.x, localFloat2.y, (float[])localObject3);
/*      */     }
/*  718 */     else if ((localObject4 instanceof CompositeFont))
/*      */     {
/*  726 */       localObject5 = (CompositeFont)localObject4;
/*  727 */       float f7 = paramFloat1; float f8 = paramFloat2;
/*  728 */       float f9 = localFloat2.x; float f10 = localFloat2.y;
/*      */ 
/*  730 */       int i4 = 0; int i5 = 0; int i6 = 0;
/*  731 */       while (i5 < m)
/*      */       {
/*  733 */         i4 = i5;
/*  734 */         i6 = localObject1[i4] >>> 24;
/*      */ 
/*  736 */         while ((i5 < m) && (localObject1[i5] >>> 24 == i6)) {
/*  737 */           i5++;
/*      */         }
/*      */ 
/*  745 */         PhysicalFont localPhysicalFont = ((CompositeFont)localObject5).getSlotFont(i6);
/*  746 */         if (!(localPhysicalFont instanceof TrueTypeFont)) {
/*  747 */           return false;
/*      */         }
/*  749 */         String str = localPhysicalFont.getFamilyName(null);
/*  750 */         int i7 = localFont.getStyle() | localPhysicalFont.getStyle();
/*  751 */         if (!localWPrinterJob.setFont(str, f4, i7, k, f6))
/*      */         {
/*  753 */           return false;
/*      */         }
/*      */ 
/*  756 */         int[] arrayOfInt = Arrays.copyOfRange((int[])localObject1, i4, i5);
/*  757 */         float[] arrayOfFloat = Arrays.copyOfRange((float[])localObject3, i4 * 2, i5 * 2);
/*      */ 
/*  759 */         if (i4 != 0) {
/*  760 */           Point2D.Float localFloat3 = new Point2D.Float(paramFloat1 + localObject2[(i4 * 2)], paramFloat2 + localObject2[(i4 * 2 + 1)]);
/*      */ 
/*  763 */           localAffineTransform1.transform(localFloat3, localFloat3);
/*  764 */           f9 = localFloat3.x;
/*  765 */           f10 = localFloat3.y;
/*      */         }
/*  767 */         localWPrinterJob.glyphsOut(arrayOfInt, f9, f10, arrayOfFloat);
/*      */       }
/*      */     } else {
/*  770 */       return false;
/*      */     }
/*  772 */     return true;
/*      */   }
/*      */ 
/*      */   private void textOut(String paramString, Font paramFont, PhysicalFont paramPhysicalFont, FontRenderContext paramFontRenderContext, float paramFloat1, int paramInt, float paramFloat2, AffineTransform paramAffineTransform, double paramDouble, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6, float paramFloat7)
/*      */   {
/*  784 */     String str = paramPhysicalFont.getFamilyName(null);
/*  785 */     int i = paramFont.getStyle() | paramPhysicalFont.getStyle();
/*  786 */     WPrinterJob localWPrinterJob = (WPrinterJob)getPrinterJob();
/*  787 */     boolean bool = localWPrinterJob.setFont(str, paramFloat1, i, paramInt, paramFloat2);
/*      */ 
/*  789 */     if (!bool) {
/*  790 */       super.drawString(paramString, paramFloat3, paramFloat4, paramFont, paramFontRenderContext, paramFloat7);
/*  791 */       return;
/*      */     }
/*      */ 
/*  794 */     Object localObject = null;
/*  795 */     if (!okGDIMetrics(paramString, paramFont, paramFontRenderContext, paramDouble))
/*      */     {
/*  803 */       paramString = localWPrinterJob.removeControlChars(paramString);
/*  804 */       char[] arrayOfChar = paramString.toCharArray();
/*  805 */       int j = arrayOfChar.length;
/*  806 */       GlyphVector localGlyphVector = null;
/*  807 */       if (!FontUtilities.isComplexText(arrayOfChar, 0, j)) {
/*  808 */         localGlyphVector = paramFont.createGlyphVector(paramFontRenderContext, paramString);
/*      */       }
/*  810 */       if (localGlyphVector == null) {
/*  811 */         super.drawString(paramString, paramFloat3, paramFloat4, paramFont, paramFontRenderContext, paramFloat7);
/*  812 */         return;
/*      */       }
/*  814 */       localObject = localGlyphVector.getGlyphPositions(0, j, null);
/*  815 */       Point2D localPoint2D = localGlyphVector.getGlyphPosition(localGlyphVector.getNumGlyphs());
/*      */ 
/*  820 */       AffineTransform localAffineTransform = new AffineTransform(paramAffineTransform);
/*      */ 
/*  822 */       localAffineTransform.rotate(paramInt * 3.141592653589793D / 1800.0D);
/*  823 */       float[] arrayOfFloat = new float[localObject.length];
/*      */ 
/*  825 */       localAffineTransform.transform((float[])localObject, 0, arrayOfFloat, 0, localObject.length / 2);
/*      */ 
/*  828 */       localObject = arrayOfFloat;
/*      */     }
/*  830 */     localWPrinterJob.textOut(paramString, paramFloat5, paramFloat6, (float[])localObject);
/*      */   }
/*      */ 
/*      */   private boolean okGDIMetrics(String paramString, Font paramFont, FontRenderContext paramFontRenderContext, double paramDouble)
/*      */   {
/*  848 */     Rectangle2D localRectangle2D = paramFont.getStringBounds(paramString, paramFontRenderContext);
/*  849 */     double d1 = localRectangle2D.getWidth();
/*  850 */     d1 = Math.round(d1 * paramDouble);
/*  851 */     int i = ((WPrinterJob)getPrinterJob()).getGDIAdvance(paramString);
/*  852 */     if ((d1 > 0.0D) && (i > 0)) {
/*  853 */       double d2 = Math.abs(i - d1);
/*  854 */       double d3 = i / d1;
/*  855 */       if (d3 < 1.0D) {
/*  856 */         d3 = 1.0D / d3;
/*      */       }
/*  858 */       return (d2 <= 1.0D) || (d3 < 1.002D);
/*      */     }
/*  860 */     return true;
/*      */   }
/*      */ 
/*      */   protected boolean drawImageToPlatform(Image paramImage, AffineTransform paramAffineTransform, Color paramColor, int paramInt1, int paramInt2, int paramInt3, int paramInt4, boolean paramBoolean)
/*      */   {
/*  897 */     BufferedImage localBufferedImage1 = getBufferedImage(paramImage);
/*  898 */     if (localBufferedImage1 == null) {
/*  899 */       return true;
/*      */     }
/*      */ 
/*  902 */     WPrinterJob localWPrinterJob = (WPrinterJob)getPrinterJob();
/*      */ 
/*  910 */     AffineTransform localAffineTransform1 = getTransform();
/*  911 */     if (paramAffineTransform == null) {
/*  912 */       paramAffineTransform = new AffineTransform();
/*      */     }
/*  914 */     localAffineTransform1.concatenate(paramAffineTransform);
/*      */ 
/*  934 */     double[] arrayOfDouble = new double[6];
/*  935 */     localAffineTransform1.getMatrix(arrayOfDouble);
/*      */ 
/*  945 */     Point2D.Float localFloat1 = new Point2D.Float(1.0F, 0.0F);
/*  946 */     Point2D.Float localFloat2 = new Point2D.Float(0.0F, 1.0F);
/*  947 */     localAffineTransform1.deltaTransform(localFloat1, localFloat1);
/*  948 */     localAffineTransform1.deltaTransform(localFloat2, localFloat2);
/*      */ 
/*  950 */     Point2D.Float localFloat3 = new Point2D.Float(0.0F, 0.0F);
/*  951 */     double d1 = localFloat1.distance(localFloat3);
/*  952 */     double d2 = localFloat2.distance(localFloat3);
/*      */ 
/*  954 */     double d3 = localWPrinterJob.getXRes();
/*  955 */     double d4 = localWPrinterJob.getYRes();
/*  956 */     double d5 = d3 / 72.0D;
/*  957 */     double d6 = d4 / 72.0D;
/*      */ 
/*  960 */     int i = localAffineTransform1.getType();
/*  961 */     int j = (i & 0x30) != 0 ? 1 : 0;
/*      */ 
/*  964 */     if (j != 0) {
/*  965 */       if (d1 > d5) d1 = d5;
/*  966 */       if (d2 > d6) d2 = d6;
/*      */ 
/*      */     }
/*      */ 
/*  972 */     if ((d1 != 0.0D) && (d2 != 0.0D))
/*      */     {
/*  976 */       AffineTransform localAffineTransform2 = new AffineTransform(arrayOfDouble[0] / d1, arrayOfDouble[1] / d2, arrayOfDouble[2] / d1, arrayOfDouble[3] / d2, arrayOfDouble[4] / d1, arrayOfDouble[5] / d2);
/*      */ 
/* 1004 */       Rectangle2D.Float localFloat = new Rectangle2D.Float(paramInt1, paramInt2, paramInt3, paramInt4);
/*      */ 
/* 1008 */       Shape localShape1 = localAffineTransform2.createTransformedShape(localFloat);
/* 1009 */       Rectangle2D localRectangle2D1 = localShape1.getBounds2D();
/*      */ 
/* 1015 */       localRectangle2D1.setRect(localRectangle2D1.getX(), localRectangle2D1.getY(), localRectangle2D1.getWidth() + 0.001D, localRectangle2D1.getHeight() + 0.001D);
/*      */ 
/* 1019 */       int k = (int)localRectangle2D1.getWidth();
/* 1020 */       int m = (int)localRectangle2D1.getHeight();
/*      */ 
/* 1022 */       if ((k > 0) && (m > 0))
/*      */       {
/* 1040 */         int n = 1;
/* 1041 */         if ((!paramBoolean) && (hasTransparentPixels(localBufferedImage1))) {
/* 1042 */           n = 0;
/* 1043 */           if (isBitmaskTransparency(localBufferedImage1)) {
/* 1044 */             if (paramColor == null) {
/* 1045 */               if (drawBitmaskImage(localBufferedImage1, paramAffineTransform, paramColor, paramInt1, paramInt2, paramInt3, paramInt4))
/*      */               {
/* 1049 */                 return true;
/*      */               }
/* 1051 */             } else if (paramColor.getTransparency() == 1)
/*      */             {
/* 1053 */               n = 1;
/*      */             }
/*      */           }
/* 1056 */           if (!canDoRedraws()) {
/* 1057 */             n = 1;
/*      */           }
/*      */ 
/*      */         }
/*      */         else
/*      */         {
/* 1063 */           paramColor = null;
/*      */         }
/*      */ 
/* 1067 */         if (((paramInt1 + paramInt3 > localBufferedImage1.getWidth(null)) || (paramInt2 + paramInt4 > localBufferedImage1.getHeight(null))) && (canDoRedraws()))
/*      */         {
/* 1070 */           n = 0;
/*      */         }
/*      */         int i5;
/*      */         int i7;
/* 1072 */         if (n == 0)
/*      */         {
/* 1074 */           localAffineTransform1.getMatrix(arrayOfDouble);
/* 1075 */           AffineTransform localAffineTransform3 = new AffineTransform(arrayOfDouble[0] / d5, arrayOfDouble[1] / d6, arrayOfDouble[2] / d5, arrayOfDouble[3] / d6, arrayOfDouble[4] / d5, arrayOfDouble[5] / d6);
/*      */ 
/* 1084 */           localObject1 = new Rectangle2D.Float(paramInt1, paramInt2, paramInt3, paramInt4);
/*      */ 
/* 1087 */           localObject2 = localAffineTransform1.createTransformedShape((Shape)localObject1);
/*      */ 
/* 1090 */           Rectangle2D localRectangle2D2 = ((Shape)localObject2).getBounds2D();
/*      */ 
/* 1092 */           localRectangle2D2.setRect(localRectangle2D2.getX(), localRectangle2D2.getY(), localRectangle2D2.getWidth() + 0.001D, localRectangle2D2.getHeight() + 0.001D);
/*      */ 
/* 1101 */           int i3 = (int)localRectangle2D2.getWidth();
/* 1102 */           i5 = (int)localRectangle2D2.getHeight();
/* 1103 */           i7 = i3 * i5 * 3;
/* 1104 */           i8 = 8388608;
/* 1105 */           double d7 = d3 < d4 ? d3 : d4;
/* 1106 */           int i9 = (int)d7;
/* 1107 */           double d8 = 1.0D;
/*      */ 
/* 1109 */           double d9 = i3 / k;
/* 1110 */           double d10 = i5 / m;
/* 1111 */           double d11 = d9 > d10 ? d10 : d9;
/* 1112 */           int i13 = (int)(i9 / d11);
/* 1113 */           if (i13 < 72) i13 = 72;
/*      */ 
/* 1115 */           while ((i7 > i8) && (i9 > i13)) {
/* 1116 */             d8 *= 2.0D;
/* 1117 */             i9 /= 2;
/* 1118 */             i7 /= 4;
/*      */           }
/* 1120 */           if (i9 < i13) {
/* 1121 */             d8 = d7 / i13;
/*      */           }
/*      */ 
/* 1124 */           localRectangle2D2.setRect(localRectangle2D2.getX() / d8, localRectangle2D2.getY() / d8, localRectangle2D2.getWidth() / d8, localRectangle2D2.getHeight() / d8);
/*      */ 
/* 1138 */           localWPrinterJob.saveState(getTransform(), getClip(), localRectangle2D2, d8, d8);
/*      */ 
/* 1140 */           return true;
/*      */         }
/*      */ 
/* 1153 */         int i1 = 5;
/* 1154 */         Object localObject1 = null;
/*      */ 
/* 1156 */         Object localObject2 = localBufferedImage1.getColorModel();
/* 1157 */         int i2 = localBufferedImage1.getType();
/* 1158 */         if (((localObject2 instanceof IndexColorModel)) && (((ColorModel)localObject2).getPixelSize() <= 8) && ((i2 == 12) || (i2 == 13)))
/*      */         {
/* 1162 */           localObject1 = (IndexColorModel)localObject2;
/* 1163 */           i1 = i2;
/*      */ 
/* 1167 */           if ((i2 == 12) && (((ColorModel)localObject2).getPixelSize() == 2))
/*      */           {
/* 1170 */             int[] arrayOfInt = new int[16];
/* 1171 */             ((IndexColorModel)localObject1).getRGBs(arrayOfInt);
/* 1172 */             i5 = ((IndexColorModel)localObject1).getTransparency() != 1 ? 1 : 0;
/*      */ 
/* 1174 */             i7 = ((IndexColorModel)localObject1).getTransparentPixel();
/*      */ 
/* 1176 */             localObject1 = new IndexColorModel(4, 16, arrayOfInt, 0, i5, i7, 0);
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/* 1183 */         int i4 = (int)localRectangle2D1.getWidth();
/* 1184 */         int i6 = (int)localRectangle2D1.getHeight();
/* 1185 */         BufferedImage localBufferedImage2 = null;
/*      */ 
/* 1212 */         int i8 = 1;
/* 1213 */         if (i8 != 0) {
/* 1214 */           if (localObject1 == null)
/* 1215 */             localBufferedImage2 = new BufferedImage(i4, i6, i1);
/*      */           else {
/* 1217 */             localBufferedImage2 = new BufferedImage(i4, i6, i1, (IndexColorModel)localObject1);
/*      */           }
/*      */ 
/* 1224 */           localObject3 = localBufferedImage2.createGraphics();
/* 1225 */           ((Graphics2D)localObject3).clipRect(0, 0, localBufferedImage2.getWidth(), localBufferedImage2.getHeight());
/*      */ 
/* 1229 */           ((Graphics2D)localObject3).translate(-localRectangle2D1.getX(), -localRectangle2D1.getY());
/*      */ 
/* 1231 */           ((Graphics2D)localObject3).transform(localAffineTransform2);
/*      */ 
/* 1236 */           if (paramColor == null) {
/* 1237 */             paramColor = Color.white;
/*      */           }
/*      */ 
/* 1240 */           ((Graphics2D)localObject3).drawImage(localBufferedImage1, paramInt1, paramInt2, paramInt1 + paramInt3, paramInt2 + paramInt4, paramInt1, paramInt2, paramInt1 + paramInt3, paramInt2 + paramInt4, paramColor, null);
/*      */ 
/* 1248 */           ((Graphics2D)localObject3).dispose();
/*      */         } else {
/* 1250 */           localBufferedImage2 = localBufferedImage1;
/*      */         }
/*      */ 
/* 1260 */         Object localObject3 = new Rectangle2D.Float((float)(localRectangle2D1.getX() * d1), (float)(localRectangle2D1.getY() * d2), (float)(localRectangle2D1.getWidth() * d1), (float)(localRectangle2D1.getHeight() * d2));
/*      */ 
/* 1270 */         WritableRaster localWritableRaster = localBufferedImage2.getRaster();
/*      */         byte[] arrayOfByte;
/* 1272 */         if ((localWritableRaster instanceof ByteComponentRaster))
/* 1273 */           arrayOfByte = ((ByteComponentRaster)localWritableRaster).getDataStorage();
/* 1274 */         else if ((localWritableRaster instanceof BytePackedRaster))
/* 1275 */           arrayOfByte = ((BytePackedRaster)localWritableRaster).getDataStorage();
/*      */         else {
/* 1277 */           return false;
/*      */         }
/*      */ 
/* 1280 */         int i10 = 24;
/* 1281 */         SampleModel localSampleModel = localBufferedImage2.getSampleModel();
/*      */         Object localObject4;
/* 1282 */         if ((localSampleModel instanceof ComponentSampleModel)) {
/* 1283 */           localObject4 = (ComponentSampleModel)localSampleModel;
/* 1284 */           i10 = ((ComponentSampleModel)localObject4).getPixelStride() * 8;
/* 1285 */         } else if ((localSampleModel instanceof MultiPixelPackedSampleModel)) {
/* 1286 */           localObject4 = (MultiPixelPackedSampleModel)localSampleModel;
/*      */ 
/* 1288 */           i10 = ((MultiPixelPackedSampleModel)localObject4).getPixelBitStride();
/*      */         }
/* 1290 */         else if (localObject1 != null) {
/* 1291 */           int i11 = localBufferedImage2.getWidth();
/* 1292 */           int i12 = localBufferedImage2.getHeight();
/* 1293 */           if ((i11 > 0) && (i12 > 0)) {
/* 1294 */             i10 = arrayOfByte.length * 8 / i11 / i12;
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/* 1306 */         Shape localShape2 = getClip();
/* 1307 */         clip(paramAffineTransform.createTransformedShape(localFloat));
/* 1308 */         deviceClip(getClip().getPathIterator(getTransform()));
/*      */ 
/* 1310 */         localWPrinterJob.drawDIBImage(arrayOfByte, ((Rectangle2D.Float)localObject3).x, ((Rectangle2D.Float)localObject3).y, (float)Math.rint(((Rectangle2D.Float)localObject3).width + 0.5D), (float)Math.rint(((Rectangle2D.Float)localObject3).height + 0.5D), 0.0F, 0.0F, localBufferedImage2.getWidth(), localBufferedImage2.getHeight(), i10, (IndexColorModel)localObject1);
/*      */ 
/* 1318 */         setClip(localShape2);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1323 */     return true;
/*      */   }
/*      */ 
/*      */   public void redrawRegion(Rectangle2D paramRectangle2D, double paramDouble1, double paramDouble2, Shape paramShape, AffineTransform paramAffineTransform)
/*      */     throws PrinterException
/*      */   {
/* 1334 */     WPrinterJob localWPrinterJob = (WPrinterJob)getPrinterJob();
/* 1335 */     Printable localPrintable = getPrintable();
/* 1336 */     PageFormat localPageFormat = getPageFormat();
/* 1337 */     int i = getPageIndex();
/*      */ 
/* 1342 */     BufferedImage localBufferedImage = new BufferedImage((int)paramRectangle2D.getWidth(), (int)paramRectangle2D.getHeight(), 5);
/*      */ 
/* 1353 */     Graphics2D localGraphics2D = localBufferedImage.createGraphics();
/* 1354 */     ProxyGraphics2D localProxyGraphics2D = new ProxyGraphics2D(localGraphics2D, localWPrinterJob);
/* 1355 */     localProxyGraphics2D.setColor(Color.white);
/* 1356 */     localProxyGraphics2D.fillRect(0, 0, localBufferedImage.getWidth(), localBufferedImage.getHeight());
/* 1357 */     localProxyGraphics2D.clipRect(0, 0, localBufferedImage.getWidth(), localBufferedImage.getHeight());
/*      */ 
/* 1359 */     localProxyGraphics2D.translate(-paramRectangle2D.getX(), -paramRectangle2D.getY());
/*      */ 
/* 1363 */     float f1 = (float)(localWPrinterJob.getXRes() / paramDouble1);
/* 1364 */     float f2 = (float)(localWPrinterJob.getYRes() / paramDouble2);
/*      */ 
/* 1370 */     localProxyGraphics2D.scale(f1 / 72.0F, f2 / 72.0F);
/*      */ 
/* 1373 */     localProxyGraphics2D.translate(-localWPrinterJob.getPhysicalPrintableX(localPageFormat.getPaper()) / localWPrinterJob.getXRes() * 72.0D, -localWPrinterJob.getPhysicalPrintableY(localPageFormat.getPaper()) / localWPrinterJob.getYRes() * 72.0D);
/*      */ 
/* 1379 */     localProxyGraphics2D.transform(new AffineTransform(getPageFormat().getMatrix()));
/* 1380 */     localProxyGraphics2D.setPaint(Color.black);
/*      */ 
/* 1382 */     localPrintable.print(localProxyGraphics2D, localPageFormat, i);
/*      */ 
/* 1384 */     localGraphics2D.dispose();
/*      */ 
/* 1393 */     deviceClip(paramShape.getPathIterator(paramAffineTransform));
/*      */ 
/* 1402 */     Rectangle2D.Float localFloat = new Rectangle2D.Float((float)(paramRectangle2D.getX() * paramDouble1), (float)(paramRectangle2D.getY() * paramDouble2), (float)(paramRectangle2D.getWidth() * paramDouble1), (float)(paramRectangle2D.getHeight() * paramDouble2));
/*      */ 
/* 1412 */     ByteComponentRaster localByteComponentRaster = (ByteComponentRaster)localBufferedImage.getRaster();
/*      */ 
/* 1415 */     localWPrinterJob.drawImage3ByteBGR(localByteComponentRaster.getDataStorage(), localFloat.x, localFloat.y, localFloat.width, localFloat.height, 0.0F, 0.0F, localBufferedImage.getWidth(), localBufferedImage.getHeight());
/*      */   }
/*      */ 
/*      */   protected void deviceFill(PathIterator paramPathIterator, Color paramColor)
/*      */   {
/* 1431 */     WPrinterJob localWPrinterJob = (WPrinterJob)getPrinterJob();
/*      */ 
/* 1433 */     convertToWPath(paramPathIterator);
/* 1434 */     localWPrinterJob.selectSolidBrush(paramColor);
/* 1435 */     localWPrinterJob.fillPath();
/*      */   }
/*      */ 
/*      */   protected void deviceClip(PathIterator paramPathIterator)
/*      */   {
/* 1445 */     WPrinterJob localWPrinterJob = (WPrinterJob)getPrinterJob();
/*      */ 
/* 1447 */     convertToWPath(paramPathIterator);
/* 1448 */     localWPrinterJob.selectClipPath();
/*      */   }
/*      */ 
/*      */   protected void deviceFrameRect(int paramInt1, int paramInt2, int paramInt3, int paramInt4, Color paramColor)
/*      */   {
/* 1457 */     AffineTransform localAffineTransform = getTransform();
/*      */ 
/* 1460 */     int i = localAffineTransform.getType();
/* 1461 */     int j = (i & 0x30) != 0 ? 1 : 0;
/*      */ 
/* 1465 */     if (j != 0) {
/* 1466 */       draw(new Rectangle2D.Float(paramInt1, paramInt2, paramInt3, paramInt4));
/* 1467 */       return;
/*      */     }
/*      */ 
/* 1470 */     Stroke localStroke = getStroke();
/*      */ 
/* 1472 */     if ((localStroke instanceof BasicStroke)) {
/* 1473 */       BasicStroke localBasicStroke = (BasicStroke)localStroke;
/*      */ 
/* 1475 */       int k = localBasicStroke.getEndCap();
/* 1476 */       int m = localBasicStroke.getLineJoin();
/*      */ 
/* 1482 */       if ((k == 2) && (m == 0) && (localBasicStroke.getMiterLimit() == 10.0F))
/*      */       {
/* 1486 */         float f1 = localBasicStroke.getLineWidth();
/* 1487 */         Point2D.Float localFloat1 = new Point2D.Float(f1, f1);
/*      */ 
/* 1490 */         localAffineTransform.deltaTransform(localFloat1, localFloat1);
/* 1491 */         float f2 = Math.min(Math.abs(localFloat1.x), Math.abs(localFloat1.y));
/*      */ 
/* 1495 */         Point2D.Float localFloat2 = new Point2D.Float(paramInt1, paramInt2);
/* 1496 */         localAffineTransform.transform(localFloat2, localFloat2);
/*      */ 
/* 1499 */         Point2D.Float localFloat3 = new Point2D.Float(paramInt1 + paramInt3, paramInt2 + paramInt4);
/*      */ 
/* 1501 */         localAffineTransform.transform(localFloat3, localFloat3);
/*      */ 
/* 1503 */         float f3 = (float)(localFloat3.getX() - localFloat2.getX());
/* 1504 */         float f4 = (float)(localFloat3.getY() - localFloat2.getY());
/*      */ 
/* 1506 */         WPrinterJob localWPrinterJob = (WPrinterJob)getPrinterJob();
/*      */ 
/* 1509 */         if (localWPrinterJob.selectStylePen(k, m, f2, paramColor) == true)
/*      */         {
/* 1511 */           localWPrinterJob.frameRect((float)localFloat2.getX(), (float)localFloat2.getY(), f3, f4);
/*      */         }
/*      */         else
/*      */         {
/* 1517 */           double d = Math.min(localWPrinterJob.getXRes(), localWPrinterJob.getYRes());
/*      */ 
/* 1520 */           if (f2 / d < 0.01400000043213368D)
/*      */           {
/* 1522 */             localWPrinterJob.selectPen(f2, paramColor);
/* 1523 */             localWPrinterJob.frameRect((float)localFloat2.getX(), (float)localFloat2.getY(), f3, f4);
/*      */           }
/*      */           else
/*      */           {
/* 1527 */             draw(new Rectangle2D.Float(paramInt1, paramInt2, paramInt3, paramInt4));
/*      */           }
/*      */         }
/*      */       }
/*      */       else {
/* 1532 */         draw(new Rectangle2D.Float(paramInt1, paramInt2, paramInt3, paramInt4));
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void deviceFillRect(int paramInt1, int paramInt2, int paramInt3, int paramInt4, Color paramColor)
/*      */   {
/* 1548 */     AffineTransform localAffineTransform = getTransform();
/*      */ 
/* 1551 */     int i = localAffineTransform.getType();
/* 1552 */     int j = (i & 0x30) != 0 ? 1 : 0;
/*      */ 
/* 1555 */     if (j != 0) {
/* 1556 */       fill(new Rectangle2D.Float(paramInt1, paramInt2, paramInt3, paramInt4));
/* 1557 */       return;
/*      */     }
/*      */ 
/* 1560 */     Point2D.Float localFloat1 = new Point2D.Float(paramInt1, paramInt2);
/* 1561 */     localAffineTransform.transform(localFloat1, localFloat1);
/*      */ 
/* 1563 */     Point2D.Float localFloat2 = new Point2D.Float(paramInt1 + paramInt3, paramInt2 + paramInt4);
/* 1564 */     localAffineTransform.transform(localFloat2, localFloat2);
/*      */ 
/* 1566 */     float f1 = (float)(localFloat2.getX() - localFloat1.getX());
/* 1567 */     float f2 = (float)(localFloat2.getY() - localFloat1.getY());
/*      */ 
/* 1569 */     WPrinterJob localWPrinterJob = (WPrinterJob)getPrinterJob();
/* 1570 */     localWPrinterJob.fillRect((float)localFloat1.getX(), (float)localFloat1.getY(), f1, f2, paramColor);
/*      */   }
/*      */ 
/*      */   protected void deviceDrawLine(int paramInt1, int paramInt2, int paramInt3, int paramInt4, Color paramColor)
/*      */   {
/* 1581 */     Stroke localStroke = getStroke();
/*      */ 
/* 1583 */     if ((localStroke instanceof BasicStroke)) {
/* 1584 */       BasicStroke localBasicStroke = (BasicStroke)localStroke;
/*      */ 
/* 1586 */       if (localBasicStroke.getDashArray() != null) {
/* 1587 */         draw(new Line2D.Float(paramInt1, paramInt2, paramInt3, paramInt4));
/* 1588 */         return;
/*      */       }
/*      */ 
/* 1591 */       float f1 = localBasicStroke.getLineWidth();
/* 1592 */       Point2D.Float localFloat1 = new Point2D.Float(f1, f1);
/*      */ 
/* 1594 */       AffineTransform localAffineTransform = getTransform();
/* 1595 */       localAffineTransform.deltaTransform(localFloat1, localFloat1);
/*      */ 
/* 1597 */       float f2 = Math.min(Math.abs(localFloat1.x), Math.abs(localFloat1.y));
/*      */ 
/* 1600 */       Point2D.Float localFloat2 = new Point2D.Float(paramInt1, paramInt2);
/* 1601 */       localAffineTransform.transform(localFloat2, localFloat2);
/*      */ 
/* 1603 */       Point2D.Float localFloat3 = new Point2D.Float(paramInt3, paramInt4);
/* 1604 */       localAffineTransform.transform(localFloat3, localFloat3);
/*      */ 
/* 1606 */       int i = localBasicStroke.getEndCap();
/* 1607 */       int j = localBasicStroke.getLineJoin();
/*      */ 
/* 1610 */       if ((localFloat3.getX() == localFloat2.getX()) && (localFloat3.getY() == localFloat2.getY()))
/*      */       {
/* 1616 */         i = 1;
/*      */       }
/*      */ 
/* 1620 */       WPrinterJob localWPrinterJob = (WPrinterJob)getPrinterJob();
/*      */ 
/* 1623 */       if (localWPrinterJob.selectStylePen(i, j, f2, paramColor))
/*      */       {
/* 1625 */         localWPrinterJob.moveTo((float)localFloat2.getX(), (float)localFloat2.getY());
/*      */ 
/* 1627 */         localWPrinterJob.lineTo((float)localFloat3.getX(), (float)localFloat3.getY());
/*      */       }
/*      */       else
/*      */       {
/* 1638 */         double d = Math.min(localWPrinterJob.getXRes(), localWPrinterJob.getYRes());
/*      */ 
/* 1641 */         if ((i == 1) || (((paramInt1 == paramInt3) || (paramInt2 == paramInt4)) && (f2 / d < 0.01400000043213368D)))
/*      */         {
/* 1645 */           localWPrinterJob.selectPen(f2, paramColor);
/* 1646 */           localWPrinterJob.moveTo((float)localFloat2.getX(), (float)localFloat2.getY());
/*      */ 
/* 1648 */           localWPrinterJob.lineTo((float)localFloat3.getX(), (float)localFloat3.getY());
/*      */         }
/*      */         else
/*      */         {
/* 1652 */           draw(new Line2D.Float(paramInt1, paramInt2, paramInt3, paramInt4));
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private void convertToWPath(PathIterator paramPathIterator)
/*      */   {
/* 1666 */     float[] arrayOfFloat = new float[6];
/*      */ 
/* 1669 */     WPrinterJob localWPrinterJob = (WPrinterJob)getPrinterJob();
/*      */     int j;
/* 1675 */     if (paramPathIterator.getWindingRule() == 0)
/* 1676 */       j = 1;
/*      */     else {
/* 1678 */       j = 2;
/*      */     }
/* 1680 */     localWPrinterJob.setPolyFillMode(j);
/*      */ 
/* 1682 */     localWPrinterJob.beginPath();
/*      */ 
/* 1684 */     while (!paramPathIterator.isDone()) {
/* 1685 */       int i = paramPathIterator.currentSegment(arrayOfFloat);
/*      */ 
/* 1687 */       switch (i) {
/*      */       case 0:
/* 1689 */         localWPrinterJob.moveTo(arrayOfFloat[0], arrayOfFloat[1]);
/* 1690 */         break;
/*      */       case 1:
/* 1693 */         localWPrinterJob.lineTo(arrayOfFloat[0], arrayOfFloat[1]);
/* 1694 */         break;
/*      */       case 2:
/* 1699 */         int k = localWPrinterJob.getPenX();
/* 1700 */         int m = localWPrinterJob.getPenY();
/* 1701 */         float f1 = k + (arrayOfFloat[0] - k) * 2.0F / 3.0F;
/* 1702 */         float f2 = m + (arrayOfFloat[1] - m) * 2.0F / 3.0F;
/* 1703 */         float f3 = arrayOfFloat[2] - (arrayOfFloat[2] - arrayOfFloat[0]) * 2.0F / 3.0F;
/* 1704 */         float f4 = arrayOfFloat[3] - (arrayOfFloat[3] - arrayOfFloat[1]) * 2.0F / 3.0F;
/* 1705 */         localWPrinterJob.polyBezierTo(f1, f2, f3, f4, arrayOfFloat[2], arrayOfFloat[3]);
/*      */ 
/* 1708 */         break;
/*      */       case 3:
/* 1711 */         localWPrinterJob.polyBezierTo(arrayOfFloat[0], arrayOfFloat[1], arrayOfFloat[2], arrayOfFloat[3], arrayOfFloat[4], arrayOfFloat[5]);
/*      */ 
/* 1714 */         break;
/*      */       case 4:
/* 1717 */         localWPrinterJob.closeFigure();
/*      */       }
/*      */ 
/* 1722 */       paramPathIterator.next();
/*      */     }
/*      */ 
/* 1725 */     localWPrinterJob.endPath();
/*      */   }
/*      */ 
/*      */   static
/*      */   {
/*   96 */     String str = (String)AccessController.doPrivileged(new GetPropertyAction("sun.java2d.print.enableGDITextLayout"));
/*      */ 
/*  101 */     if (str != null) {
/*  102 */       useGDITextLayout = Boolean.getBoolean(str);
/*  103 */       if ((!useGDITextLayout) && 
/*  104 */         (str.equalsIgnoreCase("prefer"))) {
/*  105 */         useGDITextLayout = true;
/*  106 */         preferGDITextLayout = true;
/*      */       }
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.windows.WPathGraphics
 * JD-Core Version:    0.6.2
 */