/*      */ package sun.font;
/*      */ 
/*      */ import java.awt.Font;
/*      */ import java.awt.Point;
/*      */ import java.awt.Rectangle;
/*      */ import java.awt.RenderingHints;
/*      */ import java.awt.Shape;
/*      */ import java.awt.font.FontRenderContext;
/*      */ import java.awt.font.GlyphJustificationInfo;
/*      */ import java.awt.font.GlyphMetrics;
/*      */ import java.awt.font.GlyphVector;
/*      */ import java.awt.font.LineMetrics;
/*      */ import java.awt.geom.AffineTransform;
/*      */ import java.awt.geom.GeneralPath;
/*      */ import java.awt.geom.NoninvertibleTransformException;
/*      */ import java.awt.geom.PathIterator;
/*      */ import java.awt.geom.Point2D;
/*      */ import java.awt.geom.Point2D.Float;
/*      */ import java.awt.geom.Rectangle2D;
/*      */ import java.awt.geom.Rectangle2D.Float;
/*      */ import java.lang.ref.SoftReference;
/*      */ import java.text.CharacterIterator;
/*      */ import sun.java2d.loops.FontInfo;
/*      */ 
/*      */ public class StandardGlyphVector extends GlyphVector
/*      */ {
/*      */   private Font font;
/*      */   private FontRenderContext frc;
/*      */   private int[] glyphs;
/*      */   private int[] userGlyphs;
/*      */   private float[] positions;
/*      */   private int[] charIndices;
/*      */   private int flags;
/*      */   private static final int UNINITIALIZED_FLAGS = -1;
/*      */   private GlyphTransformInfo gti;
/*      */   private AffineTransform ftx;
/*      */   private AffineTransform dtx;
/*      */   private AffineTransform invdtx;
/*      */   private AffineTransform frctx;
/*      */   private Font2D font2D;
/*      */   private SoftReference fsref;
/*      */   private SoftReference lbcacheRef;
/*      */   private SoftReference vbcacheRef;
/*      */   public static final int FLAG_USES_VERTICAL_BASELINE = 128;
/*      */   public static final int FLAG_USES_VERTICAL_METRICS = 256;
/*      */   public static final int FLAG_USES_ALTERNATE_ORIENTATION = 512;
/*      */ 
/*      */   public StandardGlyphVector(Font paramFont, String paramString, FontRenderContext paramFontRenderContext)
/*      */   {
/*  163 */     init(paramFont, paramString.toCharArray(), 0, paramString.length(), paramFontRenderContext, -1);
/*      */   }
/*      */ 
/*      */   public StandardGlyphVector(Font paramFont, char[] paramArrayOfChar, FontRenderContext paramFontRenderContext) {
/*  167 */     init(paramFont, paramArrayOfChar, 0, paramArrayOfChar.length, paramFontRenderContext, -1);
/*      */   }
/*      */ 
/*      */   public StandardGlyphVector(Font paramFont, char[] paramArrayOfChar, int paramInt1, int paramInt2, FontRenderContext paramFontRenderContext)
/*      */   {
/*  172 */     init(paramFont, paramArrayOfChar, paramInt1, paramInt2, paramFontRenderContext, -1);
/*      */   }
/*      */ 
/*      */   private float getTracking(Font paramFont) {
/*  176 */     if (paramFont.hasLayoutAttributes()) {
/*  177 */       AttributeValues localAttributeValues = ((AttributeMap)paramFont.getAttributes()).getValues();
/*  178 */       return localAttributeValues.getTracking();
/*      */     }
/*  180 */     return 0.0F;
/*      */   }
/*      */ 
/*      */   public StandardGlyphVector(Font paramFont, FontRenderContext paramFontRenderContext, int[] paramArrayOfInt1, float[] paramArrayOfFloat, int[] paramArrayOfInt2, int paramInt)
/*      */   {
/*  186 */     initGlyphVector(paramFont, paramFontRenderContext, paramArrayOfInt1, paramArrayOfFloat, paramArrayOfInt2, paramInt);
/*      */ 
/*  189 */     float f1 = getTracking(paramFont);
/*  190 */     if (f1 != 0.0F) {
/*  191 */       f1 *= paramFont.getSize2D();
/*  192 */       Point2D.Float localFloat = new Point2D.Float(f1, 0.0F);
/*  193 */       if (paramFont.isTransformed()) {
/*  194 */         localObject = paramFont.getTransform();
/*  195 */         ((AffineTransform)localObject).deltaTransform(localFloat, localFloat);
/*      */       }
/*      */ 
/*  200 */       Object localObject = FontUtilities.getFont2D(paramFont);
/*  201 */       FontStrike localFontStrike = ((Font2D)localObject).getStrike(paramFont, paramFontRenderContext);
/*      */ 
/*  203 */       float[] arrayOfFloat = { localFloat.x, localFloat.y };
/*  204 */       for (int i = 0; i < arrayOfFloat.length; i++) {
/*  205 */         float f2 = arrayOfFloat[i];
/*  206 */         if (f2 != 0.0F) {
/*  207 */           float f3 = 0.0F;
/*  208 */           int j = i; for (int k = 0; k < paramArrayOfInt1.length; j += 2) {
/*  209 */             if (localFontStrike.getGlyphAdvance(paramArrayOfInt1[(k++)]) != 0.0F) {
/*  210 */               paramArrayOfFloat[j] += f3;
/*  211 */               f3 += f2;
/*      */             }
/*      */           }
/*  214 */           paramArrayOfFloat[(paramArrayOfFloat.length - 2 + i)] += f3;
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public void initGlyphVector(Font paramFont, FontRenderContext paramFontRenderContext, int[] paramArrayOfInt1, float[] paramArrayOfFloat, int[] paramArrayOfInt2, int paramInt)
/*      */   {
/*  222 */     this.font = paramFont;
/*  223 */     this.frc = paramFontRenderContext;
/*  224 */     this.glyphs = paramArrayOfInt1;
/*  225 */     this.userGlyphs = paramArrayOfInt1;
/*  226 */     this.positions = paramArrayOfFloat;
/*  227 */     this.charIndices = paramArrayOfInt2;
/*  228 */     this.flags = paramInt;
/*      */ 
/*  230 */     initFontData();
/*      */   }
/*      */ 
/*      */   public StandardGlyphVector(Font paramFont, CharacterIterator paramCharacterIterator, FontRenderContext paramFontRenderContext) {
/*  234 */     int i = paramCharacterIterator.getBeginIndex();
/*  235 */     char[] arrayOfChar = new char[paramCharacterIterator.getEndIndex() - i];
/*  236 */     for (int j = paramCharacterIterator.first(); 
/*  237 */       j != 65535; 
/*  238 */       j = paramCharacterIterator.next()) {
/*  239 */       arrayOfChar[(paramCharacterIterator.getIndex() - i)] = j;
/*      */     }
/*  241 */     init(paramFont, arrayOfChar, 0, arrayOfChar.length, paramFontRenderContext, -1);
/*      */   }
/*      */ 
/*      */   public StandardGlyphVector(Font paramFont, int[] paramArrayOfInt, FontRenderContext paramFontRenderContext)
/*      */   {
/*  247 */     this.font = paramFont;
/*  248 */     this.frc = paramFontRenderContext;
/*  249 */     this.flags = -1;
/*      */ 
/*  251 */     initFontData();
/*  252 */     this.userGlyphs = paramArrayOfInt;
/*  253 */     this.glyphs = getValidatedGlyphs(this.userGlyphs);
/*      */   }
/*      */ 
/*      */   public static StandardGlyphVector getStandardGV(GlyphVector paramGlyphVector, FontInfo paramFontInfo)
/*      */   {
/*  268 */     if (paramFontInfo.aaHint == 2) {
/*  269 */       Object localObject = paramGlyphVector.getFontRenderContext().getAntiAliasingHint();
/*  270 */       if ((localObject != RenderingHints.VALUE_TEXT_ANTIALIAS_ON) && (localObject != RenderingHints.VALUE_TEXT_ANTIALIAS_GASP))
/*      */       {
/*  273 */         FontRenderContext localFontRenderContext = paramGlyphVector.getFontRenderContext();
/*  274 */         localFontRenderContext = new FontRenderContext(localFontRenderContext.getTransform(), RenderingHints.VALUE_TEXT_ANTIALIAS_ON, localFontRenderContext.getFractionalMetricsHint());
/*      */ 
/*  277 */         return new StandardGlyphVector(paramGlyphVector, localFontRenderContext);
/*      */       }
/*      */     }
/*  280 */     if ((paramGlyphVector instanceof StandardGlyphVector)) {
/*  281 */       return (StandardGlyphVector)paramGlyphVector;
/*      */     }
/*  283 */     return new StandardGlyphVector(paramGlyphVector, paramGlyphVector.getFontRenderContext());
/*      */   }
/*      */ 
/*      */   public Font getFont()
/*      */   {
/*  291 */     return this.font;
/*      */   }
/*      */ 
/*      */   public FontRenderContext getFontRenderContext() {
/*  295 */     return this.frc;
/*      */   }
/*      */ 
/*      */   public void performDefaultLayout() {
/*  299 */     this.positions = null;
/*  300 */     if (getTracking(this.font) == 0.0F)
/*  301 */       clearFlags(2);
/*      */   }
/*      */ 
/*      */   public int getNumGlyphs()
/*      */   {
/*  306 */     return this.glyphs.length;
/*      */   }
/*      */ 
/*      */   public int getGlyphCode(int paramInt) {
/*  310 */     return this.userGlyphs[paramInt];
/*      */   }
/*      */ 
/*      */   public int[] getGlyphCodes(int paramInt1, int paramInt2, int[] paramArrayOfInt) {
/*  314 */     if (paramInt2 < 0) {
/*  315 */       throw new IllegalArgumentException("count = " + paramInt2);
/*      */     }
/*  317 */     if (paramInt1 < 0) {
/*  318 */       throw new IndexOutOfBoundsException("start = " + paramInt1);
/*      */     }
/*  320 */     if (paramInt1 > this.glyphs.length - paramInt2) {
/*  321 */       throw new IndexOutOfBoundsException("start + count = " + (paramInt1 + paramInt2));
/*      */     }
/*      */ 
/*  324 */     if (paramArrayOfInt == null) {
/*  325 */       paramArrayOfInt = new int[paramInt2];
/*      */     }
/*      */ 
/*  329 */     for (int i = 0; i < paramInt2; i++) {
/*  330 */       paramArrayOfInt[i] = this.userGlyphs[(i + paramInt1)];
/*      */     }
/*      */ 
/*  333 */     return paramArrayOfInt;
/*      */   }
/*      */ 
/*      */   public int getGlyphCharIndex(int paramInt) {
/*  337 */     if ((paramInt < 0) && (paramInt >= this.glyphs.length)) {
/*  338 */       throw new IndexOutOfBoundsException("" + paramInt);
/*      */     }
/*  340 */     if (this.charIndices == null) {
/*  341 */       if ((getLayoutFlags() & 0x4) != 0) {
/*  342 */         return this.glyphs.length - 1 - paramInt;
/*      */       }
/*  344 */       return paramInt;
/*      */     }
/*  346 */     return this.charIndices[paramInt];
/*      */   }
/*      */ 
/*      */   public int[] getGlyphCharIndices(int paramInt1, int paramInt2, int[] paramArrayOfInt) {
/*  350 */     if ((paramInt1 < 0) || (paramInt2 < 0) || (paramInt2 > this.glyphs.length - paramInt1)) {
/*  351 */       throw new IndexOutOfBoundsException("" + paramInt1 + ", " + paramInt2);
/*      */     }
/*  353 */     if (paramArrayOfInt == null)
/*  354 */       paramArrayOfInt = new int[paramInt2];
/*      */     int i;
/*  356 */     if (this.charIndices == null)
/*      */     {
/*      */       int j;
/*  357 */       if ((getLayoutFlags() & 0x4) != 0) {
/*  358 */         i = 0; for (j = this.glyphs.length - 1 - paramInt1; 
/*  359 */           i < paramInt2; j--) {
/*  360 */           paramArrayOfInt[i] = j;
/*      */ 
/*  359 */           i++;
/*      */         }
/*      */       }
/*      */       else {
/*  363 */         i = 0; for (j = paramInt1; i < paramInt2; j++) {
/*  364 */           paramArrayOfInt[i] = j;
/*      */ 
/*  363 */           i++;
/*      */         }
/*      */       }
/*      */     }
/*      */     else {
/*  368 */       for (i = 0; i < paramInt2; i++) {
/*  369 */         paramArrayOfInt[i] = this.charIndices[(i + paramInt1)];
/*      */       }
/*      */     }
/*  372 */     return paramArrayOfInt;
/*      */   }
/*      */ 
/*      */   public Rectangle2D getLogicalBounds()
/*      */   {
/*  379 */     setFRCTX();
/*  380 */     initPositions();
/*      */ 
/*  382 */     LineMetrics localLineMetrics = this.font.getLineMetrics("", this.frc);
/*      */ 
/*  386 */     float f1 = 0.0F;
/*  387 */     float f2 = -localLineMetrics.getAscent();
/*  388 */     float f3 = 0.0F;
/*  389 */     float f4 = localLineMetrics.getDescent() + localLineMetrics.getLeading();
/*  390 */     if (this.glyphs.length > 0) {
/*  391 */       f3 = this.positions[(this.positions.length - 2)];
/*      */     }
/*      */ 
/*  394 */     return new Rectangle2D.Float(f1, f2, f3 - f1, f4 - f2);
/*      */   }
/*      */ 
/*      */   public Rectangle2D getVisualBounds()
/*      */   {
/*  399 */     Object localObject = null;
/*  400 */     for (int i = 0; i < this.glyphs.length; i++) {
/*  401 */       Rectangle2D localRectangle2D = getGlyphVisualBounds(i).getBounds2D();
/*  402 */       if (!localRectangle2D.isEmpty()) {
/*  403 */         if (localObject == null)
/*  404 */           localObject = localRectangle2D;
/*      */         else {
/*  406 */           Rectangle2D.union((Rectangle2D)localObject, localRectangle2D, (Rectangle2D)localObject);
/*      */         }
/*      */       }
/*      */     }
/*  410 */     if (localObject == null) {
/*  411 */       localObject = new Rectangle2D.Float(0.0F, 0.0F, 0.0F, 0.0F);
/*      */     }
/*  413 */     return localObject;
/*      */   }
/*      */ 
/*      */   public Rectangle getPixelBounds(FontRenderContext paramFontRenderContext, float paramFloat1, float paramFloat2)
/*      */   {
/*  419 */     return getGlyphsPixelBounds(paramFontRenderContext, paramFloat1, paramFloat2, 0, this.glyphs.length);
/*      */   }
/*      */ 
/*      */   public Shape getOutline() {
/*  423 */     return getGlyphsOutline(0, this.glyphs.length, 0.0F, 0.0F);
/*      */   }
/*      */ 
/*      */   public Shape getOutline(float paramFloat1, float paramFloat2) {
/*  427 */     return getGlyphsOutline(0, this.glyphs.length, paramFloat1, paramFloat2);
/*      */   }
/*      */ 
/*      */   public Shape getGlyphOutline(int paramInt)
/*      */   {
/*  432 */     return getGlyphsOutline(paramInt, 1, 0.0F, 0.0F);
/*      */   }
/*      */ 
/*      */   public Shape getGlyphOutline(int paramInt, float paramFloat1, float paramFloat2)
/*      */   {
/*  437 */     return getGlyphsOutline(paramInt, 1, paramFloat1, paramFloat2);
/*      */   }
/*      */ 
/*      */   public Point2D getGlyphPosition(int paramInt) {
/*  441 */     initPositions();
/*      */ 
/*  443 */     paramInt *= 2;
/*  444 */     return new Point2D.Float(this.positions[paramInt], this.positions[(paramInt + 1)]);
/*      */   }
/*      */ 
/*      */   public void setGlyphPosition(int paramInt, Point2D paramPoint2D) {
/*  448 */     initPositions();
/*      */ 
/*  450 */     int i = paramInt << 1;
/*  451 */     this.positions[i] = ((float)paramPoint2D.getX());
/*  452 */     this.positions[(i + 1)] = ((float)paramPoint2D.getY());
/*      */ 
/*  454 */     clearCaches(paramInt);
/*  455 */     addFlags(2);
/*      */   }
/*      */ 
/*      */   public AffineTransform getGlyphTransform(int paramInt) {
/*  459 */     if ((paramInt < 0) || (paramInt >= this.glyphs.length)) {
/*  460 */       throw new IndexOutOfBoundsException("ix = " + paramInt);
/*      */     }
/*  462 */     if (this.gti != null) {
/*  463 */       return this.gti.getGlyphTransform(paramInt);
/*      */     }
/*  465 */     return null;
/*      */   }
/*      */ 
/*      */   public void setGlyphTransform(int paramInt, AffineTransform paramAffineTransform) {
/*  469 */     if ((paramInt < 0) || (paramInt >= this.glyphs.length)) {
/*  470 */       throw new IndexOutOfBoundsException("ix = " + paramInt);
/*      */     }
/*      */ 
/*  473 */     if (this.gti == null) {
/*  474 */       if ((paramAffineTransform == null) || (paramAffineTransform.isIdentity())) {
/*  475 */         return;
/*      */       }
/*  477 */       this.gti = new GlyphTransformInfo(this);
/*      */     }
/*  479 */     this.gti.setGlyphTransform(paramInt, paramAffineTransform);
/*  480 */     if (this.gti.transformCount() == 0)
/*  481 */       this.gti = null;
/*      */   }
/*      */ 
/*      */   public int getLayoutFlags()
/*      */   {
/*  486 */     if (this.flags == -1) {
/*  487 */       this.flags = 0;
/*      */ 
/*  489 */       if ((this.charIndices != null) && (this.glyphs.length > 1)) {
/*  490 */         int i = 1;
/*  491 */         int j = 1;
/*      */ 
/*  493 */         int k = this.charIndices.length;
/*  494 */         for (int m = 0; (m < this.charIndices.length) && ((i != 0) || (j != 0)); m++) {
/*  495 */           int n = this.charIndices[m];
/*      */ 
/*  497 */           i = (i != 0) && (n == m) ? 1 : 0;
/*  498 */           j = (j != 0) && (n == --k) ? 1 : 0;
/*      */         }
/*      */ 
/*  501 */         if (j != 0) this.flags |= 4;
/*  502 */         if ((j == 0) && (i == 0)) this.flags |= 8;
/*      */       }
/*      */     }
/*      */ 
/*  506 */     return this.flags;
/*      */   }
/*      */ 
/*      */   public float[] getGlyphPositions(int paramInt1, int paramInt2, float[] paramArrayOfFloat) {
/*  510 */     if (paramInt2 < 0) {
/*  511 */       throw new IllegalArgumentException("count = " + paramInt2);
/*      */     }
/*  513 */     if (paramInt1 < 0) {
/*  514 */       throw new IndexOutOfBoundsException("start = " + paramInt1);
/*      */     }
/*  516 */     if (paramInt1 > this.glyphs.length + 1 - paramInt2) {
/*  517 */       throw new IndexOutOfBoundsException("start + count = " + (paramInt1 + paramInt2));
/*      */     }
/*      */ 
/*  520 */     return internalGetGlyphPositions(paramInt1, paramInt2, 0, paramArrayOfFloat);
/*      */   }
/*      */ 
/*      */   public Shape getGlyphLogicalBounds(int paramInt) {
/*  524 */     if ((paramInt < 0) || (paramInt >= this.glyphs.length))
/*  525 */       throw new IndexOutOfBoundsException("ix = " + paramInt);
/*      */     Shape[] arrayOfShape;
/*  529 */     if ((this.lbcacheRef == null) || ((arrayOfShape = (Shape[])this.lbcacheRef.get()) == null)) {
/*  530 */       arrayOfShape = new Shape[this.glyphs.length];
/*  531 */       this.lbcacheRef = new SoftReference(arrayOfShape);
/*      */     }
/*      */ 
/*  534 */     Object localObject = arrayOfShape[paramInt];
/*  535 */     if (localObject == null) {
/*  536 */       setFRCTX();
/*  537 */       initPositions();
/*      */ 
/*  545 */       ADL localADL = new ADL();
/*  546 */       GlyphStrike localGlyphStrike = getGlyphStrike(paramInt);
/*  547 */       localGlyphStrike.getADL(localADL);
/*      */ 
/*  549 */       Point2D.Float localFloat = localGlyphStrike.strike.getGlyphMetrics(this.glyphs[paramInt]);
/*      */ 
/*  551 */       float f1 = localFloat.x;
/*  552 */       float f2 = localFloat.y;
/*  553 */       float f3 = localADL.descentX + localADL.leadingX + localADL.ascentX;
/*  554 */       float f4 = localADL.descentY + localADL.leadingY + localADL.ascentY;
/*  555 */       float f5 = this.positions[(paramInt * 2)] + localGlyphStrike.dx - localADL.ascentX;
/*  556 */       float f6 = this.positions[(paramInt * 2 + 1)] + localGlyphStrike.dy - localADL.ascentY;
/*      */ 
/*  558 */       GeneralPath localGeneralPath = new GeneralPath();
/*  559 */       localGeneralPath.moveTo(f5, f6);
/*  560 */       localGeneralPath.lineTo(f5 + f1, f6 + f2);
/*  561 */       localGeneralPath.lineTo(f5 + f1 + f3, f6 + f2 + f4);
/*  562 */       localGeneralPath.lineTo(f5 + f3, f6 + f4);
/*  563 */       localGeneralPath.closePath();
/*      */ 
/*  565 */       localObject = new DelegatingShape(localGeneralPath);
/*  566 */       arrayOfShape[paramInt] = localObject;
/*      */     }
/*      */ 
/*  569 */     return localObject;
/*      */   }
/*      */ 
/*      */   public Shape getGlyphVisualBounds(int paramInt)
/*      */   {
/*  574 */     if ((paramInt < 0) || (paramInt >= this.glyphs.length))
/*  575 */       throw new IndexOutOfBoundsException("ix = " + paramInt);
/*      */     Shape[] arrayOfShape;
/*  579 */     if ((this.vbcacheRef == null) || ((arrayOfShape = (Shape[])this.vbcacheRef.get()) == null)) {
/*  580 */       arrayOfShape = new Shape[this.glyphs.length];
/*  581 */       this.vbcacheRef = new SoftReference(arrayOfShape);
/*      */     }
/*      */ 
/*  584 */     Object localObject = arrayOfShape[paramInt];
/*  585 */     if (localObject == null) {
/*  586 */       localObject = new DelegatingShape(getGlyphOutlineBounds(paramInt));
/*  587 */       arrayOfShape[paramInt] = localObject;
/*      */     }
/*      */ 
/*  590 */     return localObject;
/*      */   }
/*      */ 
/*      */   public Rectangle getGlyphPixelBounds(int paramInt, FontRenderContext paramFontRenderContext, float paramFloat1, float paramFloat2)
/*      */   {
/*  595 */     return getGlyphsPixelBounds(paramFontRenderContext, paramFloat1, paramFloat2, paramInt, 1);
/*      */   }
/*      */ 
/*      */   public GlyphMetrics getGlyphMetrics(int paramInt) {
/*  599 */     if ((paramInt < 0) || (paramInt >= this.glyphs.length)) {
/*  600 */       throw new IndexOutOfBoundsException("ix = " + paramInt);
/*      */     }
/*      */ 
/*  603 */     Rectangle2D localRectangle2D = getGlyphVisualBounds(paramInt).getBounds2D();
/*  604 */     Point2D localPoint2D = getGlyphPosition(paramInt);
/*  605 */     localRectangle2D.setRect(localRectangle2D.getMinX() - localPoint2D.getX(), localRectangle2D.getMinY() - localPoint2D.getY(), localRectangle2D.getWidth(), localRectangle2D.getHeight());
/*      */ 
/*  609 */     Point2D.Float localFloat = getGlyphStrike(paramInt).strike.getGlyphMetrics(this.glyphs[paramInt]);
/*      */ 
/*  611 */     GlyphMetrics localGlyphMetrics = new GlyphMetrics(true, localFloat.x, localFloat.y, localRectangle2D, (byte)0);
/*      */ 
/*  614 */     return localGlyphMetrics;
/*      */   }
/*      */ 
/*      */   public GlyphJustificationInfo getGlyphJustificationInfo(int paramInt) {
/*  618 */     if ((paramInt < 0) || (paramInt >= this.glyphs.length)) {
/*  619 */       throw new IndexOutOfBoundsException("ix = " + paramInt);
/*      */     }
/*      */ 
/*  627 */     return null;
/*      */   }
/*      */ 
/*      */   public boolean equals(GlyphVector paramGlyphVector) {
/*  631 */     if (this == paramGlyphVector) {
/*  632 */       return true;
/*      */     }
/*  634 */     if (paramGlyphVector == null) {
/*  635 */       return false;
/*      */     }
/*      */     try
/*      */     {
/*  639 */       StandardGlyphVector localStandardGlyphVector = (StandardGlyphVector)paramGlyphVector;
/*      */ 
/*  641 */       if (this.glyphs.length != localStandardGlyphVector.glyphs.length) {
/*  642 */         return false;
/*      */       }
/*      */ 
/*  645 */       for (int i = 0; i < this.glyphs.length; i++) {
/*  646 */         if (this.glyphs[i] != localStandardGlyphVector.glyphs[i]) {
/*  647 */           return false;
/*      */         }
/*      */       }
/*      */ 
/*  651 */       if (!this.font.equals(localStandardGlyphVector.font)) {
/*  652 */         return false;
/*      */       }
/*      */ 
/*  655 */       if (!this.frc.equals(localStandardGlyphVector.frc)) {
/*  656 */         return false;
/*      */       }
/*      */ 
/*  659 */       if ((localStandardGlyphVector.positions == null ? 1 : 0) != (this.positions == null ? 1 : 0)) {
/*  660 */         if (this.positions == null)
/*  661 */           initPositions();
/*      */         else {
/*  663 */           localStandardGlyphVector.initPositions();
/*      */         }
/*      */       }
/*      */ 
/*  667 */       if (this.positions != null) {
/*  668 */         for (i = 0; i < this.positions.length; i++) {
/*  669 */           if (this.positions[i] != localStandardGlyphVector.positions[i]) {
/*  670 */             return false;
/*      */           }
/*      */         }
/*      */       }
/*      */ 
/*  675 */       if (this.gti == null) {
/*  676 */         return localStandardGlyphVector.gti == null;
/*      */       }
/*  678 */       return this.gti.equals(localStandardGlyphVector.gti);
/*      */     }
/*      */     catch (ClassCastException localClassCastException)
/*      */     {
/*      */     }
/*      */ 
/*  684 */     return false;
/*      */   }
/*      */ 
/*      */   public int hashCode()
/*      */   {
/*  693 */     return this.font.hashCode() ^ this.glyphs.length;
/*      */   }
/*      */ 
/*      */   public boolean equals(Object paramObject)
/*      */   {
/*      */     try
/*      */     {
/*  703 */       return equals((GlyphVector)paramObject);
/*      */     } catch (ClassCastException localClassCastException) {
/*      */     }
/*  706 */     return false;
/*      */   }
/*      */ 
/*      */   public StandardGlyphVector copy()
/*      */   {
/*  714 */     return (StandardGlyphVector)clone();
/*      */   }
/*      */ 
/*      */   public Object clone()
/*      */   {
/*      */     try
/*      */     {
/*  725 */       StandardGlyphVector localStandardGlyphVector = (StandardGlyphVector)super.clone();
/*      */ 
/*  727 */       localStandardGlyphVector.clearCaches();
/*      */ 
/*  729 */       if (this.positions != null) {
/*  730 */         localStandardGlyphVector.positions = ((float[])this.positions.clone());
/*      */       }
/*      */ 
/*  733 */       if (this.gti != null) {
/*  734 */         localStandardGlyphVector.gti = new GlyphTransformInfo(localStandardGlyphVector, this.gti);
/*      */       }
/*      */ 
/*  737 */       return localStandardGlyphVector;
/*      */     }
/*      */     catch (CloneNotSupportedException localCloneNotSupportedException)
/*      */     {
/*      */     }
/*  742 */     return this;
/*      */   }
/*      */ 
/*      */   public void setGlyphPositions(float[] paramArrayOfFloat, int paramInt1, int paramInt2, int paramInt3)
/*      */   {
/*  755 */     if (paramInt3 < 0) {
/*  756 */       throw new IllegalArgumentException("count = " + paramInt3);
/*      */     }
/*      */ 
/*  759 */     initPositions();
/*  760 */     int i = paramInt2 * 2; int j = i + paramInt3 * 2; for (int k = paramInt1; i < j; k++) {
/*  761 */       this.positions[i] = paramArrayOfFloat[k];
/*      */ 
/*  760 */       i++;
/*      */     }
/*      */ 
/*  764 */     clearCaches();
/*  765 */     addFlags(2);
/*      */   }
/*      */ 
/*      */   public void setGlyphPositions(float[] paramArrayOfFloat)
/*      */   {
/*  773 */     int i = this.glyphs.length * 2 + 2;
/*  774 */     if (paramArrayOfFloat.length != i) {
/*  775 */       throw new IllegalArgumentException("srcPositions.length != " + i);
/*      */     }
/*      */ 
/*  778 */     this.positions = ((float[])paramArrayOfFloat.clone());
/*      */ 
/*  780 */     clearCaches();
/*  781 */     addFlags(2);
/*      */   }
/*      */ 
/*      */   public float[] getGlyphPositions(float[] paramArrayOfFloat)
/*      */   {
/*  790 */     return internalGetGlyphPositions(0, this.glyphs.length + 1, 0, paramArrayOfFloat);
/*      */   }
/*      */ 
/*      */   public AffineTransform[] getGlyphTransforms(int paramInt1, int paramInt2, AffineTransform[] paramArrayOfAffineTransform)
/*      */   {
/*  801 */     if ((paramInt1 < 0) || (paramInt2 < 0) || (paramInt1 + paramInt2 > this.glyphs.length)) {
/*  802 */       throw new IllegalArgumentException("start: " + paramInt1 + " count: " + paramInt2);
/*      */     }
/*      */ 
/*  805 */     if (this.gti == null) {
/*  806 */       return null;
/*      */     }
/*      */ 
/*  809 */     if (paramArrayOfAffineTransform == null) {
/*  810 */       paramArrayOfAffineTransform = new AffineTransform[paramInt2];
/*      */     }
/*      */ 
/*  813 */     for (int i = 0; i < paramInt2; paramInt1++) {
/*  814 */       paramArrayOfAffineTransform[i] = this.gti.getGlyphTransform(paramInt1);
/*      */ 
/*  813 */       i++;
/*      */     }
/*      */ 
/*  817 */     return paramArrayOfAffineTransform;
/*      */   }
/*      */ 
/*      */   public AffineTransform[] getGlyphTransforms()
/*      */   {
/*  824 */     return getGlyphTransforms(0, this.glyphs.length, null);
/*      */   }
/*      */ 
/*      */   public void setGlyphTransforms(AffineTransform[] paramArrayOfAffineTransform, int paramInt1, int paramInt2, int paramInt3)
/*      */   {
/*  833 */     int i = paramInt2; for (int j = paramInt2 + paramInt3; i < j; i++)
/*  834 */       setGlyphTransform(i, paramArrayOfAffineTransform[(paramInt1 + i)]);
/*      */   }
/*      */ 
/*      */   public void setGlyphTransforms(AffineTransform[] paramArrayOfAffineTransform)
/*      */   {
/*  842 */     setGlyphTransforms(paramArrayOfAffineTransform, 0, 0, this.glyphs.length);
/*      */   }
/*      */ 
/*      */   public float[] getGlyphInfo()
/*      */   {
/*  849 */     setFRCTX();
/*  850 */     initPositions();
/*  851 */     float[] arrayOfFloat = new float[this.glyphs.length * 8];
/*  852 */     int i = 0; for (int j = 0; i < this.glyphs.length; j += 8) {
/*  853 */       float f1 = this.positions[(i * 2)];
/*  854 */       float f2 = this.positions[(i * 2 + 1)];
/*  855 */       arrayOfFloat[j] = f1;
/*  856 */       arrayOfFloat[(j + 1)] = f2;
/*      */ 
/*  858 */       int k = this.glyphs[i];
/*  859 */       GlyphStrike localGlyphStrike = getGlyphStrike(i);
/*  860 */       Point2D.Float localFloat = localGlyphStrike.strike.getGlyphMetrics(k);
/*  861 */       arrayOfFloat[(j + 2)] = localFloat.x;
/*  862 */       arrayOfFloat[(j + 3)] = localFloat.y;
/*      */ 
/*  864 */       Rectangle2D localRectangle2D = getGlyphVisualBounds(i).getBounds2D();
/*  865 */       arrayOfFloat[(j + 4)] = ((float)localRectangle2D.getMinX());
/*  866 */       arrayOfFloat[(j + 5)] = ((float)localRectangle2D.getMinY());
/*  867 */       arrayOfFloat[(j + 6)] = ((float)localRectangle2D.getWidth());
/*  868 */       arrayOfFloat[(j + 7)] = ((float)localRectangle2D.getHeight());
/*      */ 
/*  852 */       i++;
/*      */     }
/*      */ 
/*  870 */     return arrayOfFloat;
/*      */   }
/*      */ 
/*      */   public void pixellate(FontRenderContext paramFontRenderContext, Point2D paramPoint2D, Point paramPoint)
/*      */   {
/*  877 */     if (paramFontRenderContext == null) {
/*  878 */       paramFontRenderContext = this.frc;
/*      */     }
/*      */ 
/*  883 */     AffineTransform localAffineTransform = paramFontRenderContext.getTransform();
/*  884 */     localAffineTransform.transform(paramPoint2D, paramPoint2D);
/*  885 */     paramPoint.x = ((int)paramPoint2D.getX());
/*  886 */     paramPoint.y = ((int)paramPoint2D.getY());
/*  887 */     paramPoint2D.setLocation(paramPoint.x, paramPoint.y);
/*      */     try {
/*  889 */       localAffineTransform.inverseTransform(paramPoint2D, paramPoint2D);
/*      */     }
/*      */     catch (NoninvertibleTransformException localNoninvertibleTransformException) {
/*  892 */       throw new IllegalArgumentException("must be able to invert frc transform");
/*      */     }
/*      */   }
/*      */ 
/*      */   boolean needsPositions(double[] paramArrayOfDouble)
/*      */   {
/*  905 */     return (this.gti != null) || ((getLayoutFlags() & 0x2) != 0) || (!matchTX(paramArrayOfDouble, this.frctx));
/*      */   }
/*      */ 
/*      */   Object setupGlyphImages(long[] paramArrayOfLong, float[] paramArrayOfFloat, double[] paramArrayOfDouble)
/*      */   {
/*  930 */     initPositions();
/*  931 */     setRenderTransform(paramArrayOfDouble);
/*      */ 
/*  933 */     if (this.gti != null) {
/*  934 */       return this.gti.setupGlyphImages(paramArrayOfLong, paramArrayOfFloat, this.dtx);
/*      */     }
/*      */ 
/*  937 */     GlyphStrike localGlyphStrike = getDefaultStrike();
/*  938 */     localGlyphStrike.strike.getGlyphImagePtrs(this.glyphs, paramArrayOfLong, this.glyphs.length);
/*      */ 
/*  940 */     if (paramArrayOfFloat != null) {
/*  941 */       if (this.dtx.isIdentity())
/*  942 */         System.arraycopy(this.positions, 0, paramArrayOfFloat, 0, this.glyphs.length * 2);
/*      */       else {
/*  944 */         this.dtx.transform(this.positions, 0, paramArrayOfFloat, 0, this.glyphs.length);
/*      */       }
/*      */     }
/*      */ 
/*  948 */     return localGlyphStrike;
/*      */   }
/*      */ 
/*      */   private static boolean matchTX(double[] paramArrayOfDouble, AffineTransform paramAffineTransform)
/*      */   {
/*  962 */     return (paramArrayOfDouble[0] == paramAffineTransform.getScaleX()) && (paramArrayOfDouble[1] == paramAffineTransform.getShearY()) && (paramArrayOfDouble[2] == paramAffineTransform.getShearX()) && (paramArrayOfDouble[3] == paramAffineTransform.getScaleY());
/*      */   }
/*      */ 
/*      */   private static AffineTransform getNonTranslateTX(AffineTransform paramAffineTransform)
/*      */   {
/*  971 */     if ((paramAffineTransform.getTranslateX() != 0.0D) || (paramAffineTransform.getTranslateY() != 0.0D)) {
/*  972 */       paramAffineTransform = new AffineTransform(paramAffineTransform.getScaleX(), paramAffineTransform.getShearY(), paramAffineTransform.getShearX(), paramAffineTransform.getScaleY(), 0.0D, 0.0D);
/*      */     }
/*      */ 
/*  976 */     return paramAffineTransform;
/*      */   }
/*      */ 
/*      */   private static boolean equalNonTranslateTX(AffineTransform paramAffineTransform1, AffineTransform paramAffineTransform2) {
/*  980 */     return (paramAffineTransform1.getScaleX() == paramAffineTransform2.getScaleX()) && (paramAffineTransform1.getShearY() == paramAffineTransform2.getShearY()) && (paramAffineTransform1.getShearX() == paramAffineTransform2.getShearX()) && (paramAffineTransform1.getScaleY() == paramAffineTransform2.getScaleY());
/*      */   }
/*      */ 
/*      */   private void setRenderTransform(double[] paramArrayOfDouble)
/*      */   {
/*  988 */     assert (paramArrayOfDouble.length == 4);
/*  989 */     if (!matchTX(paramArrayOfDouble, this.dtx))
/*  990 */       resetDTX(new AffineTransform(paramArrayOfDouble));
/*      */   }
/*      */ 
/*      */   private final void setDTX(AffineTransform paramAffineTransform)
/*      */   {
/*  996 */     if (!equalNonTranslateTX(this.dtx, paramAffineTransform))
/*  997 */       resetDTX(getNonTranslateTX(paramAffineTransform));
/*      */   }
/*      */ 
/*      */   private final void setFRCTX()
/*      */   {
/* 1003 */     if (!equalNonTranslateTX(this.frctx, this.dtx))
/* 1004 */       resetDTX(getNonTranslateTX(this.frctx));
/*      */   }
/*      */ 
/*      */   private final void resetDTX(AffineTransform paramAffineTransform)
/*      */   {
/* 1014 */     this.fsref = null;
/* 1015 */     this.dtx = paramAffineTransform;
/* 1016 */     this.invdtx = null;
/* 1017 */     if (!this.dtx.isIdentity()) {
/*      */       try {
/* 1019 */         this.invdtx = this.dtx.createInverse();
/*      */       }
/*      */       catch (NoninvertibleTransformException localNoninvertibleTransformException)
/*      */       {
/*      */       }
/*      */     }
/* 1025 */     if (this.gti != null)
/* 1026 */       this.gti.strikesRef = null;
/*      */   }
/*      */ 
/*      */   private StandardGlyphVector(GlyphVector paramGlyphVector, FontRenderContext paramFontRenderContext)
/*      */   {
/* 1037 */     this.font = paramGlyphVector.getFont();
/* 1038 */     this.frc = paramFontRenderContext;
/* 1039 */     initFontData();
/*      */ 
/* 1041 */     int i = paramGlyphVector.getNumGlyphs();
/* 1042 */     this.userGlyphs = paramGlyphVector.getGlyphCodes(0, i, null);
/* 1043 */     if ((paramGlyphVector instanceof StandardGlyphVector))
/*      */     {
/* 1050 */       this.glyphs = this.userGlyphs;
/*      */     }
/* 1052 */     else this.glyphs = getValidatedGlyphs(this.userGlyphs);
/*      */ 
/* 1054 */     this.flags = (paramGlyphVector.getLayoutFlags() & 0xF);
/*      */ 
/* 1056 */     if ((this.flags & 0x2) != 0) {
/* 1057 */       this.positions = paramGlyphVector.getGlyphPositions(0, i + 1, null);
/*      */     }
/*      */ 
/* 1060 */     if ((this.flags & 0x8) != 0) {
/* 1061 */       this.charIndices = paramGlyphVector.getGlyphCharIndices(0, i, null);
/*      */     }
/*      */ 
/* 1064 */     if ((this.flags & 0x1) != 0) {
/* 1065 */       AffineTransform[] arrayOfAffineTransform = new AffineTransform[i];
/* 1066 */       for (int j = 0; j < i; j++) {
/* 1067 */         arrayOfAffineTransform[j] = paramGlyphVector.getGlyphTransform(j);
/*      */       }
/*      */ 
/* 1070 */       setGlyphTransforms(arrayOfAffineTransform);
/*      */     }
/*      */   }
/*      */ 
/*      */   int[] getValidatedGlyphs(int[] paramArrayOfInt)
/*      */   {
/* 1080 */     int i = paramArrayOfInt.length;
/* 1081 */     int[] arrayOfInt = new int[i];
/* 1082 */     for (int j = 0; j < i; j++) {
/* 1083 */       if ((paramArrayOfInt[j] == 65534) || (paramArrayOfInt[j] == 65535))
/* 1084 */         arrayOfInt[j] = paramArrayOfInt[j];
/*      */       else {
/* 1086 */         arrayOfInt[j] = this.font2D.getValidatedGlyphCode(paramArrayOfInt[j]);
/*      */       }
/*      */     }
/* 1089 */     return arrayOfInt;
/*      */   }
/*      */ 
/*      */   private void init(Font paramFont, char[] paramArrayOfChar, int paramInt1, int paramInt2, FontRenderContext paramFontRenderContext, int paramInt3)
/*      */   {
/* 1096 */     if ((paramInt1 < 0) || (paramInt2 < 0) || (paramInt1 + paramInt2 > paramArrayOfChar.length)) {
/* 1097 */       throw new ArrayIndexOutOfBoundsException("start or count out of bounds");
/*      */     }
/*      */ 
/* 1100 */     this.font = paramFont;
/* 1101 */     this.frc = paramFontRenderContext;
/* 1102 */     this.flags = paramInt3;
/*      */ 
/* 1104 */     if (getTracking(paramFont) != 0.0F) {
/* 1105 */       addFlags(2);
/*      */     }
/*      */ 
/* 1109 */     if (paramInt1 != 0) {
/* 1110 */       char[] arrayOfChar = new char[paramInt2];
/* 1111 */       System.arraycopy(paramArrayOfChar, paramInt1, arrayOfChar, 0, paramInt2);
/* 1112 */       paramArrayOfChar = arrayOfChar;
/*      */     }
/*      */ 
/* 1115 */     initFontData();
/*      */ 
/* 1119 */     this.glyphs = new int[paramInt2];
/*      */ 
/* 1121 */     this.userGlyphs = this.glyphs;
/* 1122 */     this.font2D.getMapper().charsToGlyphs(paramInt2, paramArrayOfChar, this.glyphs);
/*      */   }
/*      */ 
/*      */   private void initFontData() {
/* 1126 */     this.font2D = FontUtilities.getFont2D(this.font);
/* 1127 */     float f = this.font.getSize2D();
/* 1128 */     if (this.font.isTransformed()) {
/* 1129 */       this.ftx = this.font.getTransform();
/* 1130 */       if ((this.ftx.getTranslateX() != 0.0D) || (this.ftx.getTranslateY() != 0.0D)) {
/* 1131 */         addFlags(2);
/*      */       }
/* 1133 */       this.ftx.setTransform(this.ftx.getScaleX(), this.ftx.getShearY(), this.ftx.getShearX(), this.ftx.getScaleY(), 0.0D, 0.0D);
/* 1134 */       this.ftx.scale(f, f);
/*      */     } else {
/* 1136 */       this.ftx = AffineTransform.getScaleInstance(f, f);
/*      */     }
/*      */ 
/* 1139 */     this.frctx = this.frc.getTransform();
/* 1140 */     resetDTX(getNonTranslateTX(this.frctx));
/*      */   }
/*      */ 
/*      */   private float[] internalGetGlyphPositions(int paramInt1, int paramInt2, int paramInt3, float[] paramArrayOfFloat)
/*      */   {
/* 1157 */     if (paramArrayOfFloat == null) {
/* 1158 */       paramArrayOfFloat = new float[paramInt3 + paramInt2 * 2];
/*      */     }
/*      */ 
/* 1161 */     initPositions();
/*      */ 
/* 1164 */     int i = paramInt3; int j = paramInt3 + paramInt2 * 2; for (int k = paramInt1 * 2; i < j; k++) {
/* 1165 */       paramArrayOfFloat[i] = this.positions[k];
/*      */ 
/* 1164 */       i++;
/*      */     }
/*      */ 
/* 1168 */     return paramArrayOfFloat;
/*      */   }
/*      */ 
/*      */   private Rectangle2D getGlyphOutlineBounds(int paramInt) {
/* 1172 */     setFRCTX();
/* 1173 */     initPositions();
/* 1174 */     return getGlyphStrike(paramInt).getGlyphOutlineBounds(this.glyphs[paramInt], this.positions[(paramInt * 2)], this.positions[(paramInt * 2 + 1)]);
/*      */   }
/*      */ 
/*      */   private Shape getGlyphsOutline(int paramInt1, int paramInt2, float paramFloat1, float paramFloat2)
/*      */   {
/* 1181 */     setFRCTX();
/* 1182 */     initPositions();
/*      */ 
/* 1184 */     GeneralPath localGeneralPath = new GeneralPath(1);
/* 1185 */     int i = paramInt1; int j = paramInt1 + paramInt2; for (int k = paramInt1 * 2; i < j; k += 2) {
/* 1186 */       float f1 = paramFloat1 + this.positions[k];
/* 1187 */       float f2 = paramFloat2 + this.positions[(k + 1)];
/*      */ 
/* 1189 */       getGlyphStrike(i).appendGlyphOutline(this.glyphs[i], localGeneralPath, f1, f2);
/*      */ 
/* 1185 */       i++;
/*      */     }
/*      */ 
/* 1192 */     return localGeneralPath;
/*      */   }
/*      */ 
/*      */   private Rectangle getGlyphsPixelBounds(FontRenderContext paramFontRenderContext, float paramFloat1, float paramFloat2, int paramInt1, int paramInt2) {
/* 1196 */     initPositions();
/*      */ 
/* 1198 */     AffineTransform localAffineTransform = null;
/* 1199 */     if ((paramFontRenderContext == null) || (paramFontRenderContext.equals(this.frc)))
/* 1200 */       localAffineTransform = this.frctx;
/*      */     else {
/* 1202 */       localAffineTransform = paramFontRenderContext.getTransform();
/*      */     }
/* 1204 */     setDTX(localAffineTransform);
/*      */ 
/* 1206 */     if (this.gti != null) {
/* 1207 */       return this.gti.getGlyphsPixelBounds(localAffineTransform, paramFloat1, paramFloat2, paramInt1, paramInt2);
/*      */     }
/*      */ 
/* 1210 */     FontStrike localFontStrike = getDefaultStrike().strike;
/* 1211 */     Rectangle localRectangle1 = null;
/* 1212 */     Rectangle localRectangle2 = new Rectangle();
/* 1213 */     Point2D.Float localFloat = new Point2D.Float();
/* 1214 */     int i = paramInt1 * 2;
/*      */     while (true) { paramInt2--; if (paramInt2 < 0) break;
/* 1216 */       localFloat.x = (paramFloat1 + this.positions[(i++)]);
/* 1217 */       localFloat.y = (paramFloat2 + this.positions[(i++)]);
/* 1218 */       localAffineTransform.transform(localFloat, localFloat);
/* 1219 */       localFontStrike.getGlyphImageBounds(this.glyphs[(paramInt1++)], localFloat, localRectangle2);
/* 1220 */       if (!localRectangle2.isEmpty()) {
/* 1221 */         if (localRectangle1 == null)
/* 1222 */           localRectangle1 = new Rectangle(localRectangle2);
/*      */         else {
/* 1224 */           localRectangle1.add(localRectangle2);
/*      */         }
/*      */       }
/*      */     }
/* 1228 */     return localRectangle1 != null ? localRectangle1 : localRectangle2;
/*      */   }
/*      */ 
/*      */   private void clearCaches(int paramInt)
/*      */   {
/*      */     Shape[] arrayOfShape;
/* 1232 */     if (this.lbcacheRef != null) {
/* 1233 */       arrayOfShape = (Shape[])this.lbcacheRef.get();
/* 1234 */       if (arrayOfShape != null) {
/* 1235 */         arrayOfShape[paramInt] = null;
/*      */       }
/*      */     }
/*      */ 
/* 1239 */     if (this.vbcacheRef != null) {
/* 1240 */       arrayOfShape = (Shape[])this.vbcacheRef.get();
/* 1241 */       if (arrayOfShape != null)
/* 1242 */         arrayOfShape[paramInt] = null;
/*      */     }
/*      */   }
/*      */ 
/*      */   private void clearCaches()
/*      */   {
/* 1248 */     this.lbcacheRef = null;
/* 1249 */     this.vbcacheRef = null;
/*      */   }
/*      */ 
/*      */   private void initPositions()
/*      */   {
/* 1296 */     if (this.positions == null) {
/* 1297 */       setFRCTX();
/*      */ 
/* 1299 */       this.positions = new float[this.glyphs.length * 2 + 2];
/*      */ 
/* 1301 */       Point2D.Float localFloat1 = null;
/* 1302 */       float f = getTracking(this.font);
/* 1303 */       if (f != 0.0F) {
/* 1304 */         f *= this.font.getSize2D();
/* 1305 */         localFloat1 = new Point2D.Float(f, 0.0F);
/*      */       }
/*      */ 
/* 1308 */       Point2D.Float localFloat2 = new Point2D.Float(0.0F, 0.0F);
/* 1309 */       if (this.font.isTransformed()) {
/* 1310 */         AffineTransform localAffineTransform = this.font.getTransform();
/* 1311 */         localAffineTransform.transform(localFloat2, localFloat2);
/* 1312 */         this.positions[0] = localFloat2.x;
/* 1313 */         this.positions[1] = localFloat2.y;
/*      */ 
/* 1315 */         if (localFloat1 != null) {
/* 1316 */           localAffineTransform.deltaTransform(localFloat1, localFloat1);
/*      */         }
/*      */       }
/* 1319 */       int i = 0; for (int j = 2; i < this.glyphs.length; j += 2) {
/* 1320 */         getGlyphStrike(i).addDefaultGlyphAdvance(this.glyphs[i], localFloat2);
/* 1321 */         if (localFloat1 != null) {
/* 1322 */           localFloat2.x += localFloat1.x;
/* 1323 */           localFloat2.y += localFloat1.y;
/*      */         }
/* 1325 */         this.positions[j] = localFloat2.x;
/* 1326 */         this.positions[(j + 1)] = localFloat2.y;
/*      */ 
/* 1319 */         i++;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private void addFlags(int paramInt)
/*      */   {
/* 1335 */     this.flags = (getLayoutFlags() | paramInt);
/*      */   }
/*      */ 
/*      */   private void clearFlags(int paramInt)
/*      */   {
/* 1342 */     this.flags = (getLayoutFlags() & (paramInt ^ 0xFFFFFFFF));
/*      */   }
/*      */ 
/*      */   private GlyphStrike getGlyphStrike(int paramInt)
/*      */   {
/* 1349 */     if (this.gti == null) {
/* 1350 */       return getDefaultStrike();
/*      */     }
/* 1352 */     return this.gti.getStrike(paramInt);
/*      */   }
/*      */ 
/*      */   private GlyphStrike getDefaultStrike()
/*      */   {
/* 1358 */     GlyphStrike localGlyphStrike = null;
/* 1359 */     if (this.fsref != null) {
/* 1360 */       localGlyphStrike = (GlyphStrike)this.fsref.get();
/*      */     }
/* 1362 */     if (localGlyphStrike == null) {
/* 1363 */       localGlyphStrike = GlyphStrike.create(this, this.dtx, null);
/* 1364 */       this.fsref = new SoftReference(localGlyphStrike);
/*      */     }
/* 1366 */     return localGlyphStrike;
/*      */   }
/*      */ 
/*      */   public String toString()
/*      */   {
/* 1830 */     return appendString(null).toString();
/*      */   }
/*      */ 
/*      */   StringBuffer appendString(StringBuffer paramStringBuffer) {
/* 1834 */     if (paramStringBuffer == null)
/* 1835 */       paramStringBuffer = new StringBuffer();
/*      */     try
/*      */     {
/* 1838 */       paramStringBuffer.append("SGV{font: ");
/* 1839 */       paramStringBuffer.append(this.font.toString());
/* 1840 */       paramStringBuffer.append(", frc: ");
/* 1841 */       paramStringBuffer.append(this.frc.toString());
/* 1842 */       paramStringBuffer.append(", glyphs: (");
/* 1843 */       paramStringBuffer.append(this.glyphs.length);
/* 1844 */       paramStringBuffer.append(")[");
/* 1845 */       for (int i = 0; i < this.glyphs.length; i++) {
/* 1846 */         if (i > 0) {
/* 1847 */           paramStringBuffer.append(", ");
/*      */         }
/* 1849 */         paramStringBuffer.append(Integer.toHexString(this.glyphs[i]));
/*      */       }
/* 1851 */       paramStringBuffer.append("]");
/* 1852 */       if (this.positions != null) {
/* 1853 */         paramStringBuffer.append(", positions: (");
/* 1854 */         paramStringBuffer.append(this.positions.length);
/* 1855 */         paramStringBuffer.append(")[");
/* 1856 */         for (i = 0; i < this.positions.length; i += 2) {
/* 1857 */           if (i > 0) {
/* 1858 */             paramStringBuffer.append(", ");
/*      */           }
/* 1860 */           paramStringBuffer.append(this.positions[i]);
/* 1861 */           paramStringBuffer.append("@");
/* 1862 */           paramStringBuffer.append(this.positions[(i + 1)]);
/*      */         }
/* 1864 */         paramStringBuffer.append("]");
/*      */       }
/* 1866 */       if (this.charIndices != null) {
/* 1867 */         paramStringBuffer.append(", indices: (");
/* 1868 */         paramStringBuffer.append(this.charIndices.length);
/* 1869 */         paramStringBuffer.append(")[");
/* 1870 */         for (i = 0; i < this.charIndices.length; i++) {
/* 1871 */           if (i > 0) {
/* 1872 */             paramStringBuffer.append(", ");
/*      */           }
/* 1874 */           paramStringBuffer.append(this.charIndices[i]);
/*      */         }
/* 1876 */         paramStringBuffer.append("]");
/*      */       }
/* 1878 */       paramStringBuffer.append(", flags:");
/* 1879 */       if (getLayoutFlags() == 0) {
/* 1880 */         paramStringBuffer.append(" default");
/*      */       } else {
/* 1882 */         if ((this.flags & 0x1) != 0) {
/* 1883 */           paramStringBuffer.append(" tx");
/*      */         }
/* 1885 */         if ((this.flags & 0x2) != 0) {
/* 1886 */           paramStringBuffer.append(" pos");
/*      */         }
/* 1888 */         if ((this.flags & 0x4) != 0) {
/* 1889 */           paramStringBuffer.append(" rtl");
/*      */         }
/* 1891 */         if ((this.flags & 0x8) != 0)
/* 1892 */           paramStringBuffer.append(" complex");
/*      */       }
/*      */     }
/*      */     catch (Exception localException)
/*      */     {
/* 1897 */       paramStringBuffer.append(" " + localException.getMessage());
/*      */     }
/* 1899 */     paramStringBuffer.append("}");
/*      */ 
/* 1901 */     return paramStringBuffer; } 
/*      */   static class ADL { public float ascentX;
/*      */     public float ascentY;
/*      */     public float descentX;
/*      */     public float descentY;
/*      */     public float leadingX;
/*      */     public float leadingY;
/*      */ 
/* 1913 */     public String toString() { return toStringBuffer(null).toString(); }
/*      */ 
/*      */     protected StringBuffer toStringBuffer(StringBuffer paramStringBuffer)
/*      */     {
/* 1917 */       if (paramStringBuffer == null) {
/* 1918 */         paramStringBuffer = new StringBuffer();
/*      */       }
/* 1920 */       paramStringBuffer.append("ax: ");
/* 1921 */       paramStringBuffer.append(this.ascentX);
/* 1922 */       paramStringBuffer.append(" ay: ");
/* 1923 */       paramStringBuffer.append(this.ascentY);
/* 1924 */       paramStringBuffer.append(" dx: ");
/* 1925 */       paramStringBuffer.append(this.descentX);
/* 1926 */       paramStringBuffer.append(" dy: ");
/* 1927 */       paramStringBuffer.append(this.descentY);
/* 1928 */       paramStringBuffer.append(" lx: ");
/* 1929 */       paramStringBuffer.append(this.leadingX);
/* 1930 */       paramStringBuffer.append(" ly: ");
/* 1931 */       paramStringBuffer.append(this.leadingY);
/*      */ 
/* 1933 */       return paramStringBuffer;
/*      */     }
/*      */   }
/*      */ 
/*      */   public static final class GlyphStrike
/*      */   {
/*      */     StandardGlyphVector sgv;
/*      */     FontStrike strike;
/*      */     float dx;
/*      */     float dy;
/*      */ 
/*      */     static GlyphStrike create(StandardGlyphVector paramStandardGlyphVector, AffineTransform paramAffineTransform1, AffineTransform paramAffineTransform2)
/*      */     {
/* 1701 */       float f1 = 0.0F;
/* 1702 */       float f2 = 0.0F;
/*      */ 
/* 1704 */       AffineTransform localAffineTransform = paramStandardGlyphVector.ftx;
/* 1705 */       if ((!paramAffineTransform1.isIdentity()) || (paramAffineTransform2 != null)) {
/* 1706 */         localAffineTransform = new AffineTransform(paramStandardGlyphVector.ftx);
/* 1707 */         if (paramAffineTransform2 != null) {
/* 1708 */           localAffineTransform.preConcatenate(paramAffineTransform2);
/* 1709 */           f1 = (float)localAffineTransform.getTranslateX();
/* 1710 */           f2 = (float)localAffineTransform.getTranslateY();
/*      */         }
/* 1712 */         if (!paramAffineTransform1.isIdentity()) {
/* 1713 */           localAffineTransform.preConcatenate(paramAffineTransform1);
/*      */         }
/*      */       }
/*      */ 
/* 1717 */       int i = 1;
/* 1718 */       Object localObject = paramStandardGlyphVector.frc.getAntiAliasingHint();
/* 1719 */       if (localObject == RenderingHints.VALUE_TEXT_ANTIALIAS_GASP)
/*      */       {
/* 1724 */         if ((!localAffineTransform.isIdentity()) && ((localAffineTransform.getType() & 0xFFFFFFFE) != 0))
/*      */         {
/* 1726 */           double d1 = localAffineTransform.getShearX();
/* 1727 */           if (d1 != 0.0D) {
/* 1728 */             double d2 = localAffineTransform.getScaleY();
/* 1729 */             i = (int)Math.sqrt(d1 * d1 + d2 * d2);
/*      */           }
/*      */           else {
/* 1732 */             i = (int)Math.abs(localAffineTransform.getScaleY());
/*      */           }
/*      */         }
/*      */       }
/* 1736 */       int j = FontStrikeDesc.getAAHintIntVal(localObject, paramStandardGlyphVector.font2D, i);
/* 1737 */       int k = FontStrikeDesc.getFMHintIntVal(paramStandardGlyphVector.frc.getFractionalMetricsHint());
/*      */ 
/* 1739 */       FontStrikeDesc localFontStrikeDesc = new FontStrikeDesc(paramAffineTransform1, localAffineTransform, paramStandardGlyphVector.font.getStyle(), j, k);
/*      */ 
/* 1745 */       FontStrike localFontStrike = paramStandardGlyphVector.font2D.handle.font2D.getStrike(localFontStrikeDesc);
/*      */ 
/* 1747 */       return new GlyphStrike(paramStandardGlyphVector, localFontStrike, f1, f2);
/*      */     }
/*      */ 
/*      */     private GlyphStrike(StandardGlyphVector paramStandardGlyphVector, FontStrike paramFontStrike, float paramFloat1, float paramFloat2) {
/* 1751 */       this.sgv = paramStandardGlyphVector;
/* 1752 */       this.strike = paramFontStrike;
/* 1753 */       this.dx = paramFloat1;
/* 1754 */       this.dy = paramFloat2;
/*      */     }
/*      */ 
/*      */     void getADL(StandardGlyphVector.ADL paramADL) {
/* 1758 */       StrikeMetrics localStrikeMetrics = this.strike.getFontMetrics();
/* 1759 */       Point2D.Float localFloat = null;
/* 1760 */       if (this.sgv.font.isTransformed()) {
/* 1761 */         localFloat = new Point2D.Float();
/* 1762 */         localFloat.x = ((float)this.sgv.font.getTransform().getTranslateX());
/* 1763 */         localFloat.y = ((float)this.sgv.font.getTransform().getTranslateY());
/*      */       }
/*      */ 
/* 1766 */       paramADL.ascentX = (-localStrikeMetrics.ascentX);
/* 1767 */       paramADL.ascentY = (-localStrikeMetrics.ascentY);
/* 1768 */       paramADL.descentX = localStrikeMetrics.descentX;
/* 1769 */       paramADL.descentY = localStrikeMetrics.descentY;
/* 1770 */       paramADL.leadingX = localStrikeMetrics.leadingX;
/* 1771 */       paramADL.leadingY = localStrikeMetrics.leadingY;
/*      */     }
/*      */ 
/*      */     void getGlyphPosition(int paramInt1, int paramInt2, float[] paramArrayOfFloat1, float[] paramArrayOfFloat2) {
/* 1775 */       paramArrayOfFloat1[paramInt2] += this.dx;
/* 1776 */       paramInt2++;
/* 1777 */       paramArrayOfFloat1[paramInt2] += this.dy;
/*      */     }
/*      */ 
/*      */     void addDefaultGlyphAdvance(int paramInt, Point2D.Float paramFloat)
/*      */     {
/* 1783 */       Point2D.Float localFloat = this.strike.getGlyphMetrics(paramInt);
/* 1784 */       paramFloat.x += localFloat.x + this.dx;
/* 1785 */       paramFloat.y += localFloat.y + this.dy;
/*      */     }
/*      */ 
/*      */     Rectangle2D getGlyphOutlineBounds(int paramInt, float paramFloat1, float paramFloat2) {
/* 1789 */       Object localObject = null;
/* 1790 */       if (this.sgv.invdtx == null) {
/* 1791 */         localObject = new Rectangle2D.Float();
/* 1792 */         ((Rectangle2D)localObject).setRect(this.strike.getGlyphOutlineBounds(paramInt));
/*      */       } else {
/* 1794 */         GeneralPath localGeneralPath = this.strike.getGlyphOutline(paramInt, 0.0F, 0.0F);
/* 1795 */         localGeneralPath.transform(this.sgv.invdtx);
/* 1796 */         localObject = localGeneralPath.getBounds2D();
/*      */       }
/*      */ 
/* 1806 */       if (!((Rectangle2D)localObject).isEmpty()) {
/* 1807 */         ((Rectangle2D)localObject).setRect(((Rectangle2D)localObject).getMinX() + paramFloat1 + this.dx, ((Rectangle2D)localObject).getMinY() + paramFloat2 + this.dy, ((Rectangle2D)localObject).getWidth(), ((Rectangle2D)localObject).getHeight());
/*      */       }
/*      */ 
/* 1811 */       return localObject;
/*      */     }
/*      */ 
/*      */     void appendGlyphOutline(int paramInt, GeneralPath paramGeneralPath, float paramFloat1, float paramFloat2)
/*      */     {
/* 1816 */       GeneralPath localGeneralPath = null;
/* 1817 */       if (this.sgv.invdtx == null) {
/* 1818 */         localGeneralPath = this.strike.getGlyphOutline(paramInt, paramFloat1 + this.dx, paramFloat2 + this.dy);
/*      */       } else {
/* 1820 */         localGeneralPath = this.strike.getGlyphOutline(paramInt, 0.0F, 0.0F);
/* 1821 */         localGeneralPath.transform(this.sgv.invdtx);
/* 1822 */         localGeneralPath.transform(AffineTransform.getTranslateInstance(paramFloat1 + this.dx, paramFloat2 + this.dy));
/*      */       }
/* 1824 */       PathIterator localPathIterator = localGeneralPath.getPathIterator(null);
/* 1825 */       paramGeneralPath.append(localPathIterator, false);
/*      */     }
/*      */   }
/*      */ 
/*      */   static final class GlyphTransformInfo
/*      */   {
/*      */     StandardGlyphVector sgv;
/*      */     int[] indices;
/*      */     double[] transforms;
/*      */     SoftReference strikesRef;
/*      */     boolean haveAllStrikes;
/*      */ 
/*      */     GlyphTransformInfo(StandardGlyphVector paramStandardGlyphVector)
/*      */     {
/* 1387 */       this.sgv = paramStandardGlyphVector;
/*      */     }
/*      */ 
/*      */     GlyphTransformInfo(StandardGlyphVector paramStandardGlyphVector, GlyphTransformInfo paramGlyphTransformInfo)
/*      */     {
/* 1392 */       this.sgv = paramStandardGlyphVector;
/*      */ 
/* 1394 */       this.indices = (paramGlyphTransformInfo.indices == null ? null : (int[])paramGlyphTransformInfo.indices.clone());
/* 1395 */       this.transforms = (paramGlyphTransformInfo.transforms == null ? null : (double[])paramGlyphTransformInfo.transforms.clone());
/* 1396 */       this.strikesRef = null;
/*      */     }
/*      */ 
/*      */     public boolean equals(GlyphTransformInfo paramGlyphTransformInfo)
/*      */     {
/* 1401 */       if (paramGlyphTransformInfo == null) {
/* 1402 */         return false;
/*      */       }
/* 1404 */       if (paramGlyphTransformInfo == this) {
/* 1405 */         return true;
/*      */       }
/* 1407 */       if (this.indices.length != paramGlyphTransformInfo.indices.length) {
/* 1408 */         return false;
/*      */       }
/* 1410 */       if (this.transforms.length != paramGlyphTransformInfo.transforms.length) {
/* 1411 */         return false;
/*      */       }
/*      */ 
/* 1418 */       for (int i = 0; i < this.indices.length; i++) {
/* 1419 */         int j = this.indices[i];
/* 1420 */         int k = paramGlyphTransformInfo.indices[i];
/* 1421 */         if ((j == 0 ? 1 : 0) != (k == 0 ? 1 : 0)) {
/* 1422 */           return false;
/*      */         }
/* 1424 */         if (j != 0) {
/* 1425 */           j *= 6;
/* 1426 */           k *= 6;
/* 1427 */           for (int m = 6; m > 0; m--) {
/* 1428 */             if (this.indices[(--j)] != paramGlyphTransformInfo.indices[(--k)]) {
/* 1429 */               return false;
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/* 1434 */       return true;
/*      */     }
/*      */ 
/*      */     void setGlyphTransform(int paramInt, AffineTransform paramAffineTransform)
/*      */     {
/* 1450 */       double[] arrayOfDouble1 = new double[6];
/* 1451 */       int i = 1;
/* 1452 */       if ((paramAffineTransform == null) || (paramAffineTransform.isIdentity()))
/*      */       {
/*      */         double tmp24_23 = 1.0D; arrayOfDouble1[3] = tmp24_23; arrayOfDouble1[0] = tmp24_23;
/*      */       }
/*      */       else {
/* 1457 */         i = 0;
/* 1458 */         paramAffineTransform.getMatrix(arrayOfDouble1);
/*      */       }
/*      */ 
/* 1461 */       if (this.indices == null) {
/* 1462 */         if (i != 0) {
/* 1463 */           return;
/*      */         }
/*      */ 
/* 1466 */         this.indices = new int[this.sgv.glyphs.length];
/* 1467 */         this.indices[paramInt] = 1;
/* 1468 */         this.transforms = arrayOfDouble1;
/*      */       } else {
/* 1470 */         int j = 0;
/* 1471 */         int k = -1;
/*      */         int n;
/* 1472 */         if (i != 0) {
/* 1473 */           k = 0;
/*      */         } else {
/* 1475 */           j = 1;
/*      */ 
/* 1478 */           label156: for (m = 0; m < this.transforms.length; m += 6) {
/* 1479 */             for (n = 0; n < 6; n++) {
/* 1480 */               if (this.transforms[(m + n)] != arrayOfDouble1[n]) {
/*      */                 break label156;
/*      */               }
/*      */             }
/* 1484 */             j = 0;
/* 1485 */             break;
/*      */           }
/* 1487 */           k = m / 6 + 1;
/*      */         }
/*      */ 
/* 1491 */         int m = this.indices[paramInt];
/* 1492 */         if (k != m)
/*      */         {
/* 1494 */           n = 0;
/* 1495 */           if (m != 0) {
/* 1496 */             n = 1;
/* 1497 */             for (int i1 = 0; i1 < this.indices.length; i1++) {
/* 1498 */               if ((this.indices[i1] == m) && (i1 != paramInt)) {
/* 1499 */                 n = 0;
/* 1500 */                 break;
/*      */               }
/*      */             }
/*      */           }
/*      */ 
/* 1505 */           if ((n != 0) && (j != 0)) {
/* 1506 */             k = m;
/* 1507 */             System.arraycopy(arrayOfDouble1, 0, this.transforms, (k - 1) * 6, 6);
/*      */           }
/*      */           else
/*      */           {
/*      */             double[] arrayOfDouble2;
/* 1508 */             if (n != 0) {
/* 1509 */               if (this.transforms.length == 6) {
/* 1510 */                 this.indices = null;
/* 1511 */                 this.transforms = null;
/*      */ 
/* 1513 */                 this.sgv.clearCaches(paramInt);
/* 1514 */                 this.sgv.clearFlags(1);
/* 1515 */                 this.strikesRef = null;
/*      */ 
/* 1517 */                 return;
/*      */               }
/*      */ 
/* 1520 */               arrayOfDouble2 = new double[this.transforms.length - 6];
/* 1521 */               System.arraycopy(this.transforms, 0, arrayOfDouble2, 0, (m - 1) * 6);
/* 1522 */               System.arraycopy(this.transforms, m * 6, arrayOfDouble2, (m - 1) * 6, this.transforms.length - m * 6);
/*      */ 
/* 1524 */               this.transforms = arrayOfDouble2;
/*      */ 
/* 1527 */               for (int i2 = 0; i2 < this.indices.length; i2++) {
/* 1528 */                 if (this.indices[i2] > m) {
/* 1529 */                   this.indices[i2] -= 1;
/*      */                 }
/*      */               }
/* 1532 */               if (k > m)
/* 1533 */                 k--;
/*      */             }
/* 1535 */             else if (j != 0) {
/* 1536 */               arrayOfDouble2 = new double[this.transforms.length + 6];
/* 1537 */               System.arraycopy(this.transforms, 0, arrayOfDouble2, 0, this.transforms.length);
/* 1538 */               System.arraycopy(arrayOfDouble1, 0, arrayOfDouble2, this.transforms.length, 6);
/* 1539 */               this.transforms = arrayOfDouble2;
/*      */             }
/*      */           }
/* 1542 */           this.indices[paramInt] = k;
/*      */         }
/*      */       }
/*      */ 
/* 1546 */       this.sgv.clearCaches(paramInt);
/* 1547 */       this.sgv.addFlags(1);
/* 1548 */       this.strikesRef = null;
/*      */     }
/*      */ 
/*      */     AffineTransform getGlyphTransform(int paramInt)
/*      */     {
/* 1553 */       int i = this.indices[paramInt];
/* 1554 */       if (i == 0) {
/* 1555 */         return null;
/*      */       }
/*      */ 
/* 1558 */       int j = (i - 1) * 6;
/* 1559 */       return new AffineTransform(this.transforms[(j + 0)], this.transforms[(j + 1)], this.transforms[(j + 2)], this.transforms[(j + 3)], this.transforms[(j + 4)], this.transforms[(j + 5)]);
/*      */     }
/*      */ 
/*      */     int transformCount()
/*      */     {
/* 1568 */       if (this.transforms == null) {
/* 1569 */         return 0;
/*      */       }
/* 1571 */       return this.transforms.length / 6;
/*      */     }
/*      */ 
/*      */     Object setupGlyphImages(long[] paramArrayOfLong, float[] paramArrayOfFloat, AffineTransform paramAffineTransform)
/*      */     {
/* 1592 */       int i = this.sgv.glyphs.length;
/*      */ 
/* 1594 */       StandardGlyphVector.GlyphStrike[] arrayOfGlyphStrike = getAllStrikes();
/* 1595 */       for (int j = 0; j < i; j++) {
/* 1596 */         StandardGlyphVector.GlyphStrike localGlyphStrike = arrayOfGlyphStrike[this.indices[j]];
/* 1597 */         int k = this.sgv.glyphs[j];
/* 1598 */         paramArrayOfLong[j] = localGlyphStrike.strike.getGlyphImagePtr(k);
/*      */ 
/* 1600 */         localGlyphStrike.getGlyphPosition(k, j * 2, this.sgv.positions, paramArrayOfFloat);
/*      */       }
/* 1602 */       paramAffineTransform.transform(paramArrayOfFloat, 0, paramArrayOfFloat, 0, i);
/*      */ 
/* 1604 */       return arrayOfGlyphStrike;
/*      */     }
/*      */ 
/*      */     Rectangle getGlyphsPixelBounds(AffineTransform paramAffineTransform, float paramFloat1, float paramFloat2, int paramInt1, int paramInt2) {
/* 1608 */       Rectangle localRectangle1 = null;
/* 1609 */       Rectangle localRectangle2 = new Rectangle();
/* 1610 */       Point2D.Float localFloat = new Point2D.Float();
/* 1611 */       int i = paramInt1 * 2;
/*      */       while (true) { paramInt2--; if (paramInt2 < 0) break;
/* 1613 */         StandardGlyphVector.GlyphStrike localGlyphStrike = getStrike(paramInt1);
/* 1614 */         localFloat.x = (paramFloat1 + this.sgv.positions[(i++)] + localGlyphStrike.dx);
/* 1615 */         localFloat.y = (paramFloat2 + this.sgv.positions[(i++)] + localGlyphStrike.dy);
/* 1616 */         paramAffineTransform.transform(localFloat, localFloat);
/* 1617 */         localGlyphStrike.strike.getGlyphImageBounds(this.sgv.glyphs[(paramInt1++)], localFloat, localRectangle2);
/* 1618 */         if (!localRectangle2.isEmpty()) {
/* 1619 */           if (localRectangle1 == null)
/* 1620 */             localRectangle1 = new Rectangle(localRectangle2);
/*      */           else {
/* 1622 */             localRectangle1.add(localRectangle2);
/*      */           }
/*      */         }
/*      */       }
/* 1626 */       return localRectangle1 != null ? localRectangle1 : localRectangle2;
/*      */     }
/*      */ 
/*      */     StandardGlyphVector.GlyphStrike getStrike(int paramInt) {
/* 1630 */       if (this.indices != null) {
/* 1631 */         StandardGlyphVector.GlyphStrike[] arrayOfGlyphStrike = getStrikeArray();
/* 1632 */         return getStrikeAtIndex(arrayOfGlyphStrike, this.indices[paramInt]);
/*      */       }
/* 1634 */       return this.sgv.getDefaultStrike();
/*      */     }
/*      */ 
/*      */     private StandardGlyphVector.GlyphStrike[] getAllStrikes() {
/* 1638 */       if (this.indices == null) {
/* 1639 */         return null;
/*      */       }
/*      */ 
/* 1642 */       StandardGlyphVector.GlyphStrike[] arrayOfGlyphStrike = getStrikeArray();
/* 1643 */       if (!this.haveAllStrikes) {
/* 1644 */         for (int i = 0; i < arrayOfGlyphStrike.length; i++) {
/* 1645 */           getStrikeAtIndex(arrayOfGlyphStrike, i);
/*      */         }
/* 1647 */         this.haveAllStrikes = true;
/*      */       }
/*      */ 
/* 1650 */       return arrayOfGlyphStrike;
/*      */     }
/*      */ 
/*      */     private StandardGlyphVector.GlyphStrike[] getStrikeArray() {
/* 1654 */       StandardGlyphVector.GlyphStrike[] arrayOfGlyphStrike = null;
/* 1655 */       if (this.strikesRef != null) {
/* 1656 */         arrayOfGlyphStrike = (StandardGlyphVector.GlyphStrike[])this.strikesRef.get();
/*      */       }
/* 1658 */       if (arrayOfGlyphStrike == null) {
/* 1659 */         this.haveAllStrikes = false;
/* 1660 */         arrayOfGlyphStrike = new StandardGlyphVector.GlyphStrike[transformCount() + 1];
/* 1661 */         this.strikesRef = new SoftReference(arrayOfGlyphStrike);
/*      */       }
/*      */ 
/* 1664 */       return arrayOfGlyphStrike;
/*      */     }
/*      */ 
/*      */     private StandardGlyphVector.GlyphStrike getStrikeAtIndex(StandardGlyphVector.GlyphStrike[] paramArrayOfGlyphStrike, int paramInt) {
/* 1668 */       StandardGlyphVector.GlyphStrike localGlyphStrike = paramArrayOfGlyphStrike[paramInt];
/* 1669 */       if (localGlyphStrike == null) {
/* 1670 */         if (paramInt == 0) {
/* 1671 */           localGlyphStrike = this.sgv.getDefaultStrike();
/*      */         } else {
/* 1673 */           int i = (paramInt - 1) * 6;
/* 1674 */           AffineTransform localAffineTransform = new AffineTransform(this.transforms[i], this.transforms[(i + 1)], this.transforms[(i + 2)], this.transforms[(i + 3)], this.transforms[(i + 4)], this.transforms[(i + 5)]);
/*      */ 
/* 1681 */           localGlyphStrike = StandardGlyphVector.GlyphStrike.create(this.sgv, this.sgv.dtx, localAffineTransform);
/*      */         }
/* 1683 */         paramArrayOfGlyphStrike[paramInt] = localGlyphStrike;
/*      */       }
/* 1685 */       return localGlyphStrike;
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.font.StandardGlyphVector
 * JD-Core Version:    0.6.2
 */