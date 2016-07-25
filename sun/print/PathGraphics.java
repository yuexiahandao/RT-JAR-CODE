/*      */ package sun.print;
/*      */ 
/*      */ import java.awt.Color;
/*      */ import java.awt.Font;
/*      */ import java.awt.Graphics2D;
/*      */ import java.awt.Image;
/*      */ import java.awt.Paint;
/*      */ import java.awt.Polygon;
/*      */ import java.awt.Shape;
/*      */ import java.awt.Stroke;
/*      */ import java.awt.font.FontRenderContext;
/*      */ import java.awt.font.GlyphVector;
/*      */ import java.awt.font.TextAttribute;
/*      */ import java.awt.font.TextLayout;
/*      */ import java.awt.geom.AffineTransform;
/*      */ import java.awt.geom.Arc2D.Float;
/*      */ import java.awt.geom.Ellipse2D.Float;
/*      */ import java.awt.geom.Line2D.Float;
/*      */ import java.awt.geom.PathIterator;
/*      */ import java.awt.geom.Point2D;
/*      */ import java.awt.geom.Rectangle2D;
/*      */ import java.awt.geom.Rectangle2D.Float;
/*      */ import java.awt.geom.RoundRectangle2D.Float;
/*      */ import java.awt.image.BufferedImage;
/*      */ import java.awt.image.BufferedImageOp;
/*      */ import java.awt.image.ColorModel;
/*      */ import java.awt.image.DataBuffer;
/*      */ import java.awt.image.DataBufferInt;
/*      */ import java.awt.image.ImageObserver;
/*      */ import java.awt.image.IndexColorModel;
/*      */ import java.awt.image.Raster;
/*      */ import java.awt.image.RenderedImage;
/*      */ import java.awt.image.SampleModel;
/*      */ import java.awt.image.SinglePixelPackedSampleModel;
/*      */ import java.awt.image.VolatileImage;
/*      */ import java.awt.image.WritableRaster;
/*      */ import java.awt.print.PageFormat;
/*      */ import java.awt.print.Printable;
/*      */ import java.awt.print.PrinterException;
/*      */ import java.awt.print.PrinterJob;
/*      */ import java.lang.ref.SoftReference;
/*      */ import java.text.AttributedCharacterIterator;
/*      */ import java.util.Hashtable;
/*      */ import java.util.Map;
/*      */ import sun.awt.image.SunWritableRaster;
/*      */ import sun.awt.image.ToolkitImage;
/*      */ import sun.font.CompositeFont;
/*      */ import sun.font.Font2D;
/*      */ import sun.font.Font2DHandle;
/*      */ import sun.font.FontUtilities;
/*      */ import sun.font.PhysicalFont;
/*      */ 
/*      */ public abstract class PathGraphics extends ProxyGraphics2D
/*      */ {
/*      */   private Printable mPainter;
/*      */   private PageFormat mPageFormat;
/*      */   private int mPageIndex;
/*      */   private boolean mCanRedraw;
/*      */   protected boolean printingGlyphVector;
/*  660 */   protected static SoftReference<Hashtable<Font2DHandle, Object>> fontMapRef = new SoftReference(null);
/*      */ 
/*      */   protected PathGraphics(Graphics2D paramGraphics2D, PrinterJob paramPrinterJob, Printable paramPrintable, PageFormat paramPageFormat, int paramInt, boolean paramBoolean)
/*      */   {
/*   97 */     super(paramGraphics2D, paramPrinterJob);
/*      */ 
/*   99 */     this.mPainter = paramPrintable;
/*  100 */     this.mPageFormat = paramPageFormat;
/*  101 */     this.mPageIndex = paramInt;
/*  102 */     this.mCanRedraw = paramBoolean;
/*      */   }
/*      */ 
/*      */   protected Printable getPrintable()
/*      */   {
/*  110 */     return this.mPainter;
/*      */   }
/*      */ 
/*      */   protected PageFormat getPageFormat()
/*      */   {
/*  118 */     return this.mPageFormat;
/*      */   }
/*      */ 
/*      */   protected int getPageIndex()
/*      */   {
/*  125 */     return this.mPageIndex;
/*      */   }
/*      */ 
/*      */   public boolean canDoRedraws()
/*      */   {
/*  136 */     return this.mCanRedraw;
/*      */   }
/*      */ 
/*      */   public abstract void redrawRegion(Rectangle2D paramRectangle2D, double paramDouble1, double paramDouble2, Shape paramShape, AffineTransform paramAffineTransform)
/*      */     throws PrinterException;
/*      */ 
/*      */   public void drawLine(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/*  160 */     Paint localPaint = getPaint();
/*      */     try
/*      */     {
/*  163 */       AffineTransform localAffineTransform = getTransform();
/*  164 */       if (getClip() != null) {
/*  165 */         deviceClip(getClip().getPathIterator(localAffineTransform));
/*      */       }
/*      */ 
/*  168 */       deviceDrawLine(paramInt1, paramInt2, paramInt3, paramInt4, (Color)localPaint);
/*      */     }
/*      */     catch (ClassCastException localClassCastException) {
/*  171 */       throw new IllegalArgumentException("Expected a Color instance");
/*      */     }
/*      */   }
/*      */ 
/*      */   public void drawRect(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/*  194 */     Paint localPaint = getPaint();
/*      */     try
/*      */     {
/*  197 */       AffineTransform localAffineTransform = getTransform();
/*  198 */       if (getClip() != null) {
/*  199 */         deviceClip(getClip().getPathIterator(localAffineTransform));
/*      */       }
/*      */ 
/*  202 */       deviceFrameRect(paramInt1, paramInt2, paramInt3, paramInt4, (Color)localPaint);
/*      */     }
/*      */     catch (ClassCastException localClassCastException) {
/*  205 */       throw new IllegalArgumentException("Expected a Color instance");
/*      */     }
/*      */   }
/*      */ 
/*      */   public void fillRect(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/*  231 */     Paint localPaint = getPaint();
/*      */     try
/*      */     {
/*  234 */       AffineTransform localAffineTransform = getTransform();
/*  235 */       if (getClip() != null) {
/*  236 */         deviceClip(getClip().getPathIterator(localAffineTransform));
/*      */       }
/*      */ 
/*  239 */       deviceFillRect(paramInt1, paramInt2, paramInt3, paramInt4, (Color)localPaint);
/*      */     }
/*      */     catch (ClassCastException localClassCastException) {
/*  242 */       throw new IllegalArgumentException("Expected a Color instance");
/*      */     }
/*      */   }
/*      */ 
/*      */   public void clearRect(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/*  267 */     fill(new Rectangle2D.Float(paramInt1, paramInt2, paramInt3, paramInt4), getBackground());
/*      */   }
/*      */ 
/*      */   public void drawRoundRect(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
/*      */   {
/*  289 */     draw(new RoundRectangle2D.Float(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6));
/*      */   }
/*      */ 
/*      */   public void fillRoundRect(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
/*      */   {
/*  314 */     fill(new RoundRectangle2D.Float(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6));
/*      */   }
/*      */ 
/*      */   public void drawOval(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/*  338 */     draw(new Ellipse2D.Float(paramInt1, paramInt2, paramInt3, paramInt4));
/*      */   }
/*      */ 
/*      */   public void fillOval(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/*  354 */     fill(new Ellipse2D.Float(paramInt1, paramInt2, paramInt3, paramInt4));
/*      */   }
/*      */ 
/*      */   public void drawArc(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
/*      */   {
/*  396 */     draw(new Arc2D.Float(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, 0));
/*      */   }
/*      */ 
/*      */   public void fillArc(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
/*      */   {
/*  441 */     fill(new Arc2D.Float(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, 2));
/*      */   }
/*      */ 
/*      */   public void drawPolyline(int[] paramArrayOfInt1, int[] paramArrayOfInt2, int paramInt)
/*      */   {
/*  465 */     if (paramInt > 0) {
/*  466 */       float f1 = paramArrayOfInt1[0];
/*  467 */       float f2 = paramArrayOfInt2[0];
/*  468 */       for (int i = 1; i < paramInt; i++) {
/*  469 */         float f3 = paramArrayOfInt1[i];
/*  470 */         float f4 = paramArrayOfInt2[i];
/*  471 */         draw(new Line2D.Float(f1, f2, f3, f4));
/*  472 */         f1 = f3;
/*  473 */         f2 = f4;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public void drawPolygon(int[] paramArrayOfInt1, int[] paramArrayOfInt2, int paramInt)
/*      */   {
/*  502 */     draw(new Polygon(paramArrayOfInt1, paramArrayOfInt2, paramInt));
/*      */   }
/*      */ 
/*      */   public void drawPolygon(Polygon paramPolygon)
/*      */   {
/*  513 */     draw(paramPolygon);
/*      */   }
/*      */ 
/*      */   public void fillPolygon(int[] paramArrayOfInt1, int[] paramArrayOfInt2, int paramInt)
/*      */   {
/*  539 */     fill(new Polygon(paramArrayOfInt1, paramArrayOfInt2, paramInt));
/*      */   }
/*      */ 
/*      */   public void fillPolygon(Polygon paramPolygon)
/*      */   {
/*  554 */     fill(paramPolygon);
/*      */   }
/*      */ 
/*      */   public void drawString(String paramString, int paramInt1, int paramInt2)
/*      */   {
/*  570 */     drawString(paramString, paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   public void drawString(String paramString, float paramFloat1, float paramFloat2) {
/*  574 */     if (paramString.length() == 0) {
/*  575 */       return;
/*      */     }
/*  577 */     TextLayout localTextLayout = new TextLayout(paramString, getFont(), getFontRenderContext());
/*      */ 
/*  579 */     localTextLayout.draw(this, paramFloat1, paramFloat2);
/*      */   }
/*      */ 
/*      */   protected void drawString(String paramString, float paramFloat1, float paramFloat2, Font paramFont, FontRenderContext paramFontRenderContext, float paramFloat3)
/*      */   {
/*  584 */     TextLayout localTextLayout = new TextLayout(paramString, paramFont, paramFontRenderContext);
/*      */ 
/*  586 */     Shape localShape = localTextLayout.getOutline(AffineTransform.getTranslateInstance(paramFloat1, paramFloat2));
/*      */ 
/*  588 */     fill(localShape);
/*      */   }
/*      */ 
/*      */   public void drawString(AttributedCharacterIterator paramAttributedCharacterIterator, int paramInt1, int paramInt2)
/*      */   {
/*  605 */     drawString(paramAttributedCharacterIterator, paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   public void drawString(AttributedCharacterIterator paramAttributedCharacterIterator, float paramFloat1, float paramFloat2) {
/*  609 */     if (paramAttributedCharacterIterator == null) {
/*  610 */       throw new NullPointerException("attributedcharacteriterator is null");
/*      */     }
/*      */ 
/*  613 */     TextLayout localTextLayout = new TextLayout(paramAttributedCharacterIterator, getFontRenderContext());
/*      */ 
/*  615 */     localTextLayout.draw(this, paramFloat1, paramFloat2);
/*      */   }
/*      */ 
/*      */   public void drawGlyphVector(GlyphVector paramGlyphVector, float paramFloat1, float paramFloat2)
/*      */   {
/*  642 */     if (this.printingGlyphVector) {
/*  643 */       assert (!this.printingGlyphVector);
/*  644 */       fill(paramGlyphVector.getOutline(paramFloat1, paramFloat2));
/*  645 */       return;
/*      */     }
/*      */     try
/*      */     {
/*  649 */       this.printingGlyphVector = true;
/*  650 */       if ((RasterPrinterJob.shapeTextProp) || (!printedSimpleGlyphVector(paramGlyphVector, paramFloat1, paramFloat2)))
/*      */       {
/*  652 */         fill(paramGlyphVector.getOutline(paramFloat1, paramFloat2));
/*      */       }
/*      */     } finally {
/*  655 */       this.printingGlyphVector = false;
/*      */     }
/*      */   }
/*      */ 
/*      */   protected int platformFontCount(Font paramFont, String paramString)
/*      */   {
/*  663 */     return 0;
/*      */   }
/*      */ 
/*      */   protected boolean printGlyphVector(GlyphVector paramGlyphVector, float paramFloat1, float paramFloat2)
/*      */   {
/*  672 */     return false;
/*      */   }
/*      */ 
/*      */   boolean printedSimpleGlyphVector(GlyphVector paramGlyphVector, float paramFloat1, float paramFloat2)
/*      */   {
/*  690 */     int i = paramGlyphVector.getLayoutFlags();
/*      */ 
/*  698 */     if ((i != 0) && (i != 2)) {
/*  699 */       return printGlyphVector(paramGlyphVector, paramFloat1, paramFloat2);
/*      */     }
/*      */ 
/*  702 */     Font localFont = paramGlyphVector.getFont();
/*  703 */     Font2D localFont2D = FontUtilities.getFont2D(localFont);
/*  704 */     if (localFont2D.handle.font2D != localFont2D)
/*      */     {
/*  706 */       return false;
/*      */     }
/*      */     Hashtable localHashtable;
/*  709 */     synchronized (PathGraphics.class) {
/*  710 */       localHashtable = (Hashtable)fontMapRef.get();
/*  711 */       if (localHashtable == null) {
/*  712 */         localHashtable = new Hashtable();
/*  713 */         fontMapRef = new SoftReference(localHashtable);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  718 */     int j = paramGlyphVector.getNumGlyphs();
/*  719 */     int[] arrayOfInt1 = paramGlyphVector.getGlyphCodes(0, j, null);
/*      */ 
/*  721 */     char[] arrayOfChar1 = null;
/*  722 */     Object localObject2 = (char[][])null;
/*  723 */     CompositeFont localCompositeFont = null;
/*      */     int k;
/*      */     int m;
/*  726 */     synchronized (localHashtable) {
/*  727 */       if ((localFont2D instanceof CompositeFont)) {
/*  728 */         localCompositeFont = (CompositeFont)localFont2D;
/*  729 */         k = localCompositeFont.getNumSlots();
/*  730 */         localObject2 = (char[][])localHashtable.get(localFont2D.handle);
/*  731 */         if (localObject2 == null) {
/*  732 */           localObject2 = new char[k][];
/*  733 */           localHashtable.put(localFont2D.handle, localObject2);
/*      */         }
/*  735 */         for (m = 0; m < j; m++) {
/*  736 */           int n = arrayOfInt1[m] >>> 24;
/*  737 */           if (n >= k) {
/*  738 */             return false;
/*      */           }
/*  740 */           if (localObject2[n] == null) {
/*  741 */             PhysicalFont localPhysicalFont = localCompositeFont.getSlotFont(n);
/*  742 */             char[] arrayOfChar2 = (char[])localHashtable.get(localPhysicalFont.handle);
/*  743 */             if (arrayOfChar2 == null) {
/*  744 */               arrayOfChar2 = getGlyphToCharMapForFont(localPhysicalFont);
/*      */             }
/*  746 */             localObject2[n] = arrayOfChar2;
/*      */           }
/*      */         }
/*      */       } else {
/*  750 */         arrayOfChar1 = (char[])localHashtable.get(localFont2D.handle);
/*  751 */         if (arrayOfChar1 == null) {
/*  752 */           arrayOfChar1 = getGlyphToCharMapForFont(localFont2D);
/*  753 */           localHashtable.put(localFont2D.handle, arrayOfChar1);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  758 */     ??? = new char[j];
/*  759 */     if (localCompositeFont != null) {
/*  760 */       for (k = 0; k < j; k++) {
/*  761 */         m = arrayOfInt1[k];
/*  762 */         Object localObject3 = localObject2[(m >>> 24)];
/*  763 */         m &= 16777215;
/*  764 */         if (localObject3 == null) {
/*  765 */           return false;
/*      */         }
/*      */ 
/*  783 */         if (m == 65535) {
/*  784 */           i2 = 10; } else {
/*  785 */           if ((m < 0) || (m >= localObject3.length)) {
/*  786 */             return false;
/*      */           }
/*  788 */           i2 = localObject3[m];
/*      */         }
/*  790 */         if (i2 != 65535)
/*  791 */           ???[k] = i2;
/*      */         else
/*  793 */           return false;
/*      */       }
/*      */     }
/*      */     else {
/*  797 */       for (k = 0; k < j; k++) {
/*  798 */         m = arrayOfInt1[k];
/*      */         int i1;
/*  800 */         if (m == 65535) {
/*  801 */           i1 = 10; } else {
/*  802 */           if ((m < 0) || (m >= arrayOfChar1.length)) {
/*  803 */             return false;
/*      */           }
/*  805 */           i1 = arrayOfChar1[m];
/*      */         }
/*  807 */         if (i1 != 65535)
/*  808 */           ???[k] = i1;
/*      */         else {
/*  810 */           return false;
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  815 */     FontRenderContext localFontRenderContext1 = paramGlyphVector.getFontRenderContext();
/*  816 */     GlyphVector localGlyphVector = localFont.createGlyphVector(localFontRenderContext1, (char[])???);
/*  817 */     if (localGlyphVector.getNumGlyphs() != j) {
/*  818 */       return printGlyphVector(paramGlyphVector, paramFloat1, paramFloat2);
/*      */     }
/*  820 */     int[] arrayOfInt2 = localGlyphVector.getGlyphCodes(0, j, null);
/*      */ 
/*  824 */     for (int i2 = 0; i2 < j; i2++) {
/*  825 */       if (arrayOfInt1[i2] != arrayOfInt2[i2]) {
/*  826 */         return printGlyphVector(paramGlyphVector, paramFloat1, paramFloat2);
/*      */       }
/*      */     }
/*      */ 
/*  830 */     FontRenderContext localFontRenderContext2 = getFontRenderContext();
/*  831 */     boolean bool = localFontRenderContext1.equals(localFontRenderContext2);
/*      */ 
/*  835 */     if ((!bool) && (localFontRenderContext1.usesFractionalMetrics() == localFontRenderContext2.usesFractionalMetrics()))
/*      */     {
/*  837 */       localObject5 = localFontRenderContext1.getTransform();
/*  838 */       AffineTransform localAffineTransform = getTransform();
/*  839 */       localObject6 = new double[4];
/*  840 */       double[] arrayOfDouble = new double[4];
/*  841 */       ((AffineTransform)localObject5).getMatrix((double[])localObject6);
/*  842 */       localAffineTransform.getMatrix(arrayOfDouble);
/*  843 */       bool = true;
/*  844 */       for (int i5 = 0; i5 < 4; i5++) {
/*  845 */         if (localObject6[i5] != arrayOfDouble[i5]) {
/*  846 */           bool = false;
/*  847 */           break;
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  852 */     Object localObject5 = new String((char[])???, 0, j);
/*  853 */     int i3 = platformFontCount(localFont, (String)localObject5);
/*  854 */     if (i3 == 0) {
/*  855 */       return false;
/*      */     }
/*      */ 
/*  858 */     Object localObject6 = paramGlyphVector.getGlyphPositions(0, j, null);
/*  859 */     int i4 = ((i & 0x2) == 0) || (samePositions(localGlyphVector, arrayOfInt2, arrayOfInt1, (float[])localObject6)) ? 1 : 0;
/*      */ 
/*  879 */     Point2D localPoint2D = paramGlyphVector.getGlyphPosition(j);
/*  880 */     float f1 = (float)localPoint2D.getX();
/*  881 */     int i6 = 0;
/*      */     Object localObject7;
/*  882 */     if ((localFont.hasLayoutAttributes()) && (this.printingGlyphVector) && (i4 != 0))
/*      */     {
/*  898 */       Map localMap = localFont.getAttributes();
/*  899 */       localObject7 = localMap.get(TextAttribute.TRACKING);
/*  900 */       int i8 = (localObject7 != null) && ((localObject7 instanceof Number)) && (((Number)localObject7).floatValue() != 0.0F) ? 1 : 0;
/*      */ 
/*  903 */       if (i8 != 0) {
/*  904 */         i4 = 0;
/*      */       } else {
/*  906 */         Rectangle2D localRectangle2D = localFont.getStringBounds((String)localObject5, localFontRenderContext1);
/*  907 */         float f2 = (float)localRectangle2D.getWidth();
/*  908 */         if (Math.abs(f2 - f1) > 1.E-005D) {
/*  909 */           i6 = 1;
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  914 */     if ((bool) && (i4 != 0) && (i6 == 0)) {
/*  915 */       drawString((String)localObject5, paramFloat1, paramFloat2, localFont, localFontRenderContext1, 0.0F);
/*  916 */       return true;
/*      */     }
/*      */ 
/*  928 */     if ((i3 == 1) && (canDrawStringToWidth()) && (i4 != 0)) {
/*  929 */       drawString((String)localObject5, paramFloat1, paramFloat2, localFont, localFontRenderContext1, f1);
/*  930 */       return true;
/*      */     }
/*      */ 
/*  938 */     if (FontUtilities.isComplexText((char[])???, 0, ???.length)) {
/*  939 */       return printGlyphVector(paramGlyphVector, paramFloat1, paramFloat2);
/*      */     }
/*      */ 
/*  953 */     if ((j > 10) && (printGlyphVector(paramGlyphVector, paramFloat1, paramFloat2))) {
/*  954 */       return true;
/*      */     }
/*      */ 
/*  957 */     for (int i7 = 0; i7 < j; i7++) {
/*  958 */       localObject7 = new String((char[])???, i7, 1);
/*  959 */       drawString((String)localObject7, paramFloat1 + localObject6[(i7 * 2)], paramFloat2 + localObject6[(i7 * 2 + 1)], localFont, localFontRenderContext1, 0.0F);
/*      */     }
/*      */ 
/*  962 */     return true;
/*      */   }
/*      */ 
/*      */   private boolean samePositions(GlyphVector paramGlyphVector, int[] paramArrayOfInt1, int[] paramArrayOfInt2, float[] paramArrayOfFloat)
/*      */   {
/*  976 */     int i = paramGlyphVector.getNumGlyphs();
/*  977 */     float[] arrayOfFloat = paramGlyphVector.getGlyphPositions(0, i, null);
/*      */ 
/*  980 */     if ((i != paramArrayOfInt1.length) || (paramArrayOfInt2.length != paramArrayOfInt1.length) || (paramArrayOfFloat.length != arrayOfFloat.length))
/*      */     {
/*  983 */       return false;
/*      */     }
/*      */ 
/*  986 */     for (int j = 0; j < i; j++) {
/*  987 */       if ((paramArrayOfInt1[j] != paramArrayOfInt2[j]) || (arrayOfFloat[j] != paramArrayOfFloat[j])) {
/*  988 */         return false;
/*      */       }
/*      */     }
/*  991 */     return true;
/*      */   }
/*      */ 
/*      */   protected boolean canDrawStringToWidth() {
/*  995 */     return false;
/*      */   }
/*      */ 
/*      */   private static char[] getGlyphToCharMapForFont(Font2D paramFont2D)
/*      */   {
/* 1008 */     int i = paramFont2D.getNumGlyphs();
/* 1009 */     int j = paramFont2D.getMissingGlyphCode();
/* 1010 */     char[] arrayOfChar = new char[i];
/*      */ 
/* 1013 */     for (int m = 0; m < i; m++) {
/* 1014 */       arrayOfChar[m] = 65535;
/*      */     }
/*      */ 
/* 1022 */     for (m = 0; m < 65535; m = (char)(m + 1))
/* 1023 */       if ((m < 55296) || (m > 57343))
/*      */       {
/* 1027 */         int k = paramFont2D.charToGlyph(m);
/* 1028 */         if ((k != j) && (k >= 0) && (k < i) && (arrayOfChar[k] == 65535))
/*      */         {
/* 1032 */           arrayOfChar[k] = m;
/*      */         }
/*      */       }
/* 1035 */     return arrayOfChar;
/*      */   }
/*      */ 
/*      */   public void draw(Shape paramShape)
/*      */   {
/* 1054 */     fill(getStroke().createStrokedShape(paramShape));
/*      */   }
/*      */ 
/*      */   public void fill(Shape paramShape)
/*      */   {
/* 1070 */     Paint localPaint = getPaint();
/*      */     try
/*      */     {
/* 1073 */       fill(paramShape, (Color)localPaint);
/*      */     }
/*      */     catch (ClassCastException localClassCastException)
/*      */     {
/* 1082 */       throw new IllegalArgumentException("Expected a Color instance");
/*      */     }
/*      */   }
/*      */ 
/*      */   public void fill(Shape paramShape, Color paramColor) {
/* 1087 */     AffineTransform localAffineTransform = getTransform();
/*      */ 
/* 1089 */     if (getClip() != null) {
/* 1090 */       deviceClip(getClip().getPathIterator(localAffineTransform));
/*      */     }
/* 1092 */     deviceFill(paramShape.getPathIterator(localAffineTransform), paramColor);
/*      */   }
/*      */ 
/*      */   protected abstract void deviceFill(PathIterator paramPathIterator, Color paramColor);
/*      */ 
/*      */   protected abstract void deviceClip(PathIterator paramPathIterator);
/*      */ 
/*      */   protected abstract void deviceFrameRect(int paramInt1, int paramInt2, int paramInt3, int paramInt4, Color paramColor);
/*      */ 
/*      */   protected abstract void deviceDrawLine(int paramInt1, int paramInt2, int paramInt3, int paramInt4, Color paramColor);
/*      */ 
/*      */   protected abstract void deviceFillRect(int paramInt1, int paramInt2, int paramInt3, int paramInt4, Color paramColor);
/*      */ 
/*      */   protected BufferedImage getBufferedImage(Image paramImage)
/*      */   {
/* 1131 */     if ((paramImage instanceof BufferedImage))
/*      */     {
/* 1133 */       return (BufferedImage)paramImage;
/* 1134 */     }if ((paramImage instanceof ToolkitImage))
/*      */     {
/* 1138 */       return ((ToolkitImage)paramImage).getBufferedImage();
/* 1139 */     }if ((paramImage instanceof VolatileImage))
/*      */     {
/* 1142 */       return ((VolatileImage)paramImage).getSnapshot();
/*      */     }
/*      */ 
/* 1149 */     return null;
/*      */   }
/*      */ 
/*      */   protected boolean hasTransparentPixels(BufferedImage paramBufferedImage)
/*      */   {
/* 1161 */     ColorModel localColorModel = paramBufferedImage.getColorModel();
/* 1162 */     boolean bool = localColorModel == null;
/*      */ 
/* 1174 */     if ((bool) && (paramBufferedImage != null) && (
/* 1175 */       (paramBufferedImage.getType() == 2) || (paramBufferedImage.getType() == 3)))
/*      */     {
/* 1177 */       DataBuffer localDataBuffer = paramBufferedImage.getRaster().getDataBuffer();
/* 1178 */       SampleModel localSampleModel = paramBufferedImage.getRaster().getSampleModel();
/* 1179 */       if (((localDataBuffer instanceof DataBufferInt)) && ((localSampleModel instanceof SinglePixelPackedSampleModel)))
/*      */       {
/* 1181 */         SinglePixelPackedSampleModel localSinglePixelPackedSampleModel = (SinglePixelPackedSampleModel)localSampleModel;
/*      */ 
/* 1184 */         int[] arrayOfInt = SunWritableRaster.stealData((DataBufferInt)localDataBuffer, 0);
/*      */ 
/* 1186 */         int i = paramBufferedImage.getMinX();
/* 1187 */         int j = paramBufferedImage.getMinY();
/* 1188 */         int k = paramBufferedImage.getWidth();
/* 1189 */         int m = paramBufferedImage.getHeight();
/* 1190 */         int n = localSinglePixelPackedSampleModel.getScanlineStride();
/* 1191 */         int i1 = 0;
/* 1192 */         for (int i2 = j; i2 < j + m; i2++) {
/* 1193 */           int i3 = i2 * n;
/* 1194 */           for (int i4 = i; i4 < i + k; i4++) {
/* 1195 */             if ((arrayOfInt[(i3 + i4)] & 0xFF000000) != -16777216) {
/* 1196 */               i1 = 1;
/* 1197 */               break;
/*      */             }
/*      */           }
/* 1200 */           if (i1 != 0) {
/*      */             break;
/*      */           }
/*      */         }
/* 1204 */         if (i1 == 0) {
/* 1205 */           bool = false;
/*      */         }
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1211 */     return bool;
/*      */   }
/*      */ 
/*      */   protected boolean isBitmaskTransparency(BufferedImage paramBufferedImage) {
/* 1215 */     ColorModel localColorModel = paramBufferedImage.getColorModel();
/* 1216 */     return (localColorModel != null) && (localColorModel.getTransparency() == 2);
/*      */   }
/*      */ 
/*      */   protected boolean drawBitmaskImage(BufferedImage paramBufferedImage, AffineTransform paramAffineTransform, Color paramColor, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/* 1230 */     ColorModel localColorModel = paramBufferedImage.getColorModel();
/*      */ 
/* 1234 */     if (!(localColorModel instanceof IndexColorModel)) {
/* 1235 */       return false;
/*      */     }
/* 1237 */     IndexColorModel localIndexColorModel = (IndexColorModel)localColorModel;
/*      */ 
/* 1240 */     if (localColorModel.getTransparency() != 2) {
/* 1241 */       return false;
/*      */     }
/*      */ 
/* 1246 */     if ((paramColor != null) && (paramColor.getAlpha() < 128)) {
/* 1247 */       return false;
/*      */     }
/*      */ 
/* 1250 */     if ((paramAffineTransform.getType() & 0xFFFFFFF4) != 0)
/*      */     {
/* 1255 */       return false;
/*      */     }
/*      */ 
/* 1258 */     if ((getTransform().getType() & 0xFFFFFFF4) != 0)
/*      */     {
/* 1263 */       return false;
/*      */     }
/*      */ 
/* 1266 */     BufferedImage localBufferedImage = null;
/* 1267 */     WritableRaster localWritableRaster = paramBufferedImage.getRaster();
/* 1268 */     int i = localIndexColorModel.getTransparentPixel();
/* 1269 */     byte[] arrayOfByte = new byte[localIndexColorModel.getMapSize()];
/* 1270 */     localIndexColorModel.getAlphas(arrayOfByte);
/* 1271 */     if (i >= 0) {
/* 1272 */       arrayOfByte[i] = 0;
/*      */     }
/*      */ 
/* 1279 */     int j = localWritableRaster.getWidth();
/* 1280 */     int k = localWritableRaster.getHeight();
/* 1281 */     if ((paramInt1 > j) || (paramInt2 > k))
/* 1282 */       return false;
/*      */     int m;
/*      */     int i1;
/* 1285 */     if (paramInt1 + paramInt3 > j) {
/* 1286 */       m = j;
/* 1287 */       i1 = m - paramInt1;
/*      */     } else {
/* 1289 */       m = paramInt1 + paramInt3;
/* 1290 */       i1 = paramInt3;
/*      */     }
/*      */     int n;
/*      */     int i2;
/* 1292 */     if (paramInt2 + paramInt4 > k) {
/* 1293 */       n = k;
/* 1294 */       i2 = n - paramInt2;
/*      */     } else {
/* 1296 */       n = paramInt2 + paramInt4;
/* 1297 */       i2 = paramInt4;
/*      */     }
/* 1299 */     int[] arrayOfInt = new int[i1];
/* 1300 */     for (int i3 = paramInt2; i3 < n; i3++) {
/* 1301 */       int i4 = -1;
/* 1302 */       localWritableRaster.getPixels(paramInt1, i3, i1, 1, arrayOfInt);
/* 1303 */       for (int i5 = paramInt1; i5 < m; i5++) {
/* 1304 */         if (arrayOfByte[arrayOfInt[(i5 - paramInt1)]] == 0) {
/* 1305 */           if (i4 >= 0) {
/* 1306 */             localBufferedImage = paramBufferedImage.getSubimage(i4, i3, i5 - i4, 1);
/*      */ 
/* 1308 */             paramAffineTransform.translate(i4, i3);
/* 1309 */             drawImageToPlatform(localBufferedImage, paramAffineTransform, paramColor, 0, 0, i5 - i4, 1, true);
/*      */ 
/* 1311 */             paramAffineTransform.translate(-i4, -i3);
/* 1312 */             i4 = -1;
/*      */           }
/* 1314 */         } else if (i4 < 0) {
/* 1315 */           i4 = i5;
/*      */         }
/*      */       }
/* 1318 */       if (i4 >= 0) {
/* 1319 */         localBufferedImage = paramBufferedImage.getSubimage(i4, i3, m - i4, 1);
/*      */ 
/* 1321 */         paramAffineTransform.translate(i4, i3);
/* 1322 */         drawImageToPlatform(localBufferedImage, paramAffineTransform, paramColor, 0, 0, m - i4, 1, true);
/*      */ 
/* 1324 */         paramAffineTransform.translate(-i4, -i3);
/*      */       }
/*      */     }
/* 1327 */     return true;
/*      */   }
/*      */ 
/*      */   protected abstract boolean drawImageToPlatform(Image paramImage, AffineTransform paramAffineTransform, Color paramColor, int paramInt1, int paramInt2, int paramInt3, int paramInt4, boolean paramBoolean);
/*      */ 
/*      */   public boolean drawImage(Image paramImage, int paramInt1, int paramInt2, ImageObserver paramImageObserver)
/*      */   {
/* 1396 */     return drawImage(paramImage, paramInt1, paramInt2, null, paramImageObserver);
/*      */   }
/*      */ 
/*      */   public boolean drawImage(Image paramImage, int paramInt1, int paramInt2, int paramInt3, int paramInt4, ImageObserver paramImageObserver)
/*      */   {
/* 1437 */     return drawImage(paramImage, paramInt1, paramInt2, paramInt3, paramInt4, null, paramImageObserver);
/*      */   }
/*      */ 
/*      */   public boolean drawImage(Image paramImage, int paramInt1, int paramInt2, Color paramColor, ImageObserver paramImageObserver)
/*      */   {
/* 1481 */     if (paramImage == null) {
/* 1482 */       return true;
/*      */     }
/*      */ 
/* 1486 */     int i = paramImage.getWidth(null);
/* 1487 */     int j = paramImage.getHeight(null);
/*      */     boolean bool;
/* 1489 */     if ((i < 0) || (j < 0))
/* 1490 */       bool = false;
/*      */     else {
/* 1492 */       bool = drawImage(paramImage, paramInt1, paramInt2, i, j, paramColor, paramImageObserver);
/*      */     }
/*      */ 
/* 1495 */     return bool;
/*      */   }
/*      */ 
/*      */   public boolean drawImage(Image paramImage, int paramInt1, int paramInt2, int paramInt3, int paramInt4, Color paramColor, ImageObserver paramImageObserver)
/*      */   {
/* 1543 */     if (paramImage == null) {
/* 1544 */       return true;
/*      */     }
/*      */ 
/* 1548 */     int i = paramImage.getWidth(null);
/* 1549 */     int j = paramImage.getHeight(null);
/*      */     boolean bool;
/* 1551 */     if ((i < 0) || (j < 0))
/* 1552 */       bool = false;
/*      */     else {
/* 1554 */       bool = drawImage(paramImage, paramInt1, paramInt2, paramInt1 + paramInt3, paramInt2 + paramInt4, 0, 0, i, j, paramImageObserver);
/*      */     }
/*      */ 
/* 1560 */     return bool;
/*      */   }
/*      */ 
/*      */   public boolean drawImage(Image paramImage, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, ImageObserver paramImageObserver)
/*      */   {
/* 1615 */     return drawImage(paramImage, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramInt7, paramInt8, null, paramImageObserver);
/*      */   }
/*      */ 
/*      */   public boolean drawImage(Image paramImage, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, Color paramColor, ImageObserver paramImageObserver)
/*      */   {
/* 1681 */     if (paramImage == null) {
/* 1682 */       return true;
/*      */     }
/* 1684 */     int i = paramImage.getWidth(null);
/* 1685 */     int j = paramImage.getHeight(null);
/*      */ 
/* 1687 */     if ((i < 0) || (j < 0)) {
/* 1688 */       return true;
/*      */     }
/*      */ 
/* 1691 */     int k = paramInt7 - paramInt5;
/* 1692 */     int m = paramInt8 - paramInt6;
/*      */ 
/* 1700 */     float f1 = (paramInt3 - paramInt1) / k;
/* 1701 */     float f2 = (paramInt4 - paramInt2) / m;
/* 1702 */     AffineTransform localAffineTransform = new AffineTransform(f1, 0.0F, 0.0F, f2, paramInt1 - paramInt5 * f1, paramInt2 - paramInt6 * f2);
/*      */ 
/* 1714 */     int n = 0;
/* 1715 */     if (paramInt7 < paramInt5) {
/* 1716 */       n = paramInt5;
/* 1717 */       paramInt5 = paramInt7;
/* 1718 */       paramInt7 = n;
/*      */     }
/* 1720 */     if (paramInt8 < paramInt6) {
/* 1721 */       n = paramInt6;
/* 1722 */       paramInt6 = paramInt8;
/* 1723 */       paramInt8 = n;
/*      */     }
/*      */ 
/* 1729 */     if (paramInt5 < 0)
/* 1730 */       paramInt5 = 0;
/* 1731 */     else if (paramInt5 > i) {
/* 1732 */       paramInt5 = i;
/*      */     }
/* 1734 */     if (paramInt7 < 0)
/* 1735 */       paramInt7 = 0;
/* 1736 */     else if (paramInt7 > i) {
/* 1737 */       paramInt7 = i;
/*      */     }
/* 1739 */     if (paramInt6 < 0)
/* 1740 */       paramInt6 = 0;
/* 1741 */     else if (paramInt6 > j) {
/* 1742 */       paramInt6 = j;
/*      */     }
/* 1744 */     if (paramInt8 < 0)
/* 1745 */       paramInt8 = 0;
/* 1746 */     else if (paramInt8 > j) {
/* 1747 */       paramInt8 = j;
/*      */     }
/*      */ 
/* 1750 */     k = paramInt7 - paramInt5;
/* 1751 */     m = paramInt8 - paramInt6;
/*      */ 
/* 1753 */     if ((k <= 0) || (m <= 0)) {
/* 1754 */       return true;
/*      */     }
/*      */ 
/* 1757 */     return drawImageToPlatform(paramImage, localAffineTransform, paramColor, paramInt5, paramInt6, k, m, false);
/*      */   }
/*      */ 
/*      */   public boolean drawImage(Image paramImage, AffineTransform paramAffineTransform, ImageObserver paramImageObserver)
/*      */   {
/* 1788 */     if (paramImage == null) {
/* 1789 */       return true;
/*      */     }
/*      */ 
/* 1793 */     int i = paramImage.getWidth(null);
/* 1794 */     int j = paramImage.getHeight(null);
/*      */     boolean bool;
/* 1796 */     if ((i < 0) || (j < 0))
/* 1797 */       bool = false;
/*      */     else {
/* 1799 */       bool = drawImageToPlatform(paramImage, paramAffineTransform, null, 0, 0, i, j, false);
/*      */     }
/*      */ 
/* 1803 */     return bool;
/*      */   }
/*      */ 
/*      */   public void drawImage(BufferedImage paramBufferedImage, BufferedImageOp paramBufferedImageOp, int paramInt1, int paramInt2)
/*      */   {
/* 1829 */     if (paramBufferedImage == null) {
/* 1830 */       return;
/*      */     }
/*      */ 
/* 1833 */     int i = paramBufferedImage.getWidth(null);
/* 1834 */     int j = paramBufferedImage.getHeight(null);
/*      */ 
/* 1836 */     if (paramBufferedImageOp != null) {
/* 1837 */       paramBufferedImage = paramBufferedImageOp.filter(paramBufferedImage, null);
/*      */     }
/* 1839 */     if ((i <= 0) || (j <= 0)) {
/* 1840 */       return;
/*      */     }
/* 1842 */     AffineTransform localAffineTransform = new AffineTransform(1.0F, 0.0F, 0.0F, 1.0F, paramInt1, paramInt2);
/* 1843 */     drawImageToPlatform(paramBufferedImage, localAffineTransform, null, 0, 0, i, j, false);
/*      */   }
/*      */ 
/*      */   public void drawRenderedImage(RenderedImage paramRenderedImage, AffineTransform paramAffineTransform)
/*      */   {
/* 1871 */     if (paramRenderedImage == null) {
/* 1872 */       return;
/*      */     }
/*      */ 
/* 1875 */     BufferedImage localBufferedImage = null;
/* 1876 */     int i = paramRenderedImage.getWidth();
/* 1877 */     int j = paramRenderedImage.getHeight();
/*      */ 
/* 1879 */     if ((i <= 0) || (j <= 0)) {
/* 1880 */       return;
/*      */     }
/*      */ 
/* 1883 */     if ((paramRenderedImage instanceof BufferedImage)) {
/* 1884 */       localBufferedImage = (BufferedImage)paramRenderedImage;
/*      */     } else {
/* 1886 */       localBufferedImage = new BufferedImage(i, j, 2);
/*      */ 
/* 1888 */       Graphics2D localGraphics2D = localBufferedImage.createGraphics();
/* 1889 */       localGraphics2D.drawRenderedImage(paramRenderedImage, paramAffineTransform);
/*      */     }
/*      */ 
/* 1892 */     drawImageToPlatform(localBufferedImage, paramAffineTransform, null, 0, 0, i, j, false);
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.print.PathGraphics
 * JD-Core Version:    0.6.2
 */