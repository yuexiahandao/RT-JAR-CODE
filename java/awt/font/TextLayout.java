/*      */ package java.awt.font;
/*      */ 
/*      */ import java.awt.Font;
/*      */ import java.awt.Graphics2D;
/*      */ import java.awt.Rectangle;
/*      */ import java.awt.Shape;
/*      */ import java.awt.geom.AffineTransform;
/*      */ import java.awt.geom.GeneralPath;
/*      */ import java.awt.geom.Point2D;
/*      */ import java.awt.geom.Point2D.Float;
/*      */ import java.awt.geom.Rectangle2D;
/*      */ import java.awt.geom.Rectangle2D.Float;
/*      */ import java.text.AttributedCharacterIterator;
/*      */ import java.text.AttributedCharacterIterator.Attribute;
/*      */ import java.text.AttributedString;
/*      */ import java.util.Map;
/*      */ import sun.font.AttributeValues;
/*      */ import sun.font.CoreMetrics;
/*      */ import sun.font.FontResolver;
/*      */ import sun.font.GraphicComponent;
/*      */ import sun.font.LayoutPathImpl;
/*      */ import sun.text.CodePointIterator;
/*      */ 
/*      */ public final class TextLayout
/*      */   implements Cloneable
/*      */ {
/*      */   private int characterCount;
/*  241 */   private boolean isVerticalLine = false;
/*      */   private byte baseline;
/*      */   private float[] baselineOffsets;
/*      */   private TextLine textLine;
/*  248 */   private TextLine.TextLineMetrics lineMetrics = null;
/*      */   private float visibleAdvance;
/*      */   private int hashCodeCache;
/*  257 */   private boolean cacheIsValid = false;
/*      */   private float justifyRatio;
/*      */   private static final float ALREADY_JUSTIFIED = -53.900002F;
/*      */   private static float dx;
/*      */   private static float dy;
/*  285 */   private Rectangle2D naturalBounds = null;
/*      */ 
/*  291 */   private Rectangle2D boundsRect = null;
/*      */ 
/*  297 */   private boolean caretsInLigaturesAreAllowed = false;
/*      */ 
/*  352 */   public static final CaretPolicy DEFAULT_CARET_POLICY = new CaretPolicy();
/*      */ 
/*      */   public TextLayout(String paramString, Font paramFont, FontRenderContext paramFontRenderContext)
/*      */   {
/*  373 */     if (paramFont == null) {
/*  374 */       throw new IllegalArgumentException("Null font passed to TextLayout constructor.");
/*      */     }
/*      */ 
/*  377 */     if (paramString == null) {
/*  378 */       throw new IllegalArgumentException("Null string passed to TextLayout constructor.");
/*      */     }
/*      */ 
/*  381 */     if (paramString.length() == 0) {
/*  382 */       throw new IllegalArgumentException("Zero length string passed to TextLayout constructor.");
/*      */     }
/*      */ 
/*  385 */     Map localMap = null;
/*  386 */     if (paramFont.hasLayoutAttributes()) {
/*  387 */       localMap = paramFont.getAttributes();
/*      */     }
/*      */ 
/*  390 */     char[] arrayOfChar = paramString.toCharArray();
/*  391 */     if (sameBaselineUpTo(paramFont, arrayOfChar, 0, arrayOfChar.length) == arrayOfChar.length) {
/*  392 */       fastInit(arrayOfChar, paramFont, localMap, paramFontRenderContext);
/*      */     } else {
/*  394 */       AttributedString localAttributedString = localMap == null ? new AttributedString(paramString) : new AttributedString(paramString, localMap);
/*      */ 
/*  397 */       localAttributedString.addAttribute(TextAttribute.FONT, paramFont);
/*  398 */       standardInit(localAttributedString.getIterator(), arrayOfChar, paramFontRenderContext);
/*      */     }
/*      */   }
/*      */ 
/*      */   public TextLayout(String paramString, Map<? extends AttributedCharacterIterator.Attribute, ?> paramMap, FontRenderContext paramFontRenderContext)
/*      */   {
/*  422 */     if (paramString == null) {
/*  423 */       throw new IllegalArgumentException("Null string passed to TextLayout constructor.");
/*      */     }
/*      */ 
/*  426 */     if (paramMap == null) {
/*  427 */       throw new IllegalArgumentException("Null map passed to TextLayout constructor.");
/*      */     }
/*      */ 
/*  430 */     if (paramString.length() == 0) {
/*  431 */       throw new IllegalArgumentException("Zero length string passed to TextLayout constructor.");
/*      */     }
/*      */ 
/*  434 */     char[] arrayOfChar = paramString.toCharArray();
/*  435 */     Font localFont = singleFont(arrayOfChar, 0, arrayOfChar.length, paramMap);
/*  436 */     if (localFont != null) {
/*  437 */       fastInit(arrayOfChar, localFont, paramMap, paramFontRenderContext);
/*      */     } else {
/*  439 */       AttributedString localAttributedString = new AttributedString(paramString, paramMap);
/*  440 */       standardInit(localAttributedString.getIterator(), arrayOfChar, paramFontRenderContext);
/*      */     }
/*      */   }
/*      */ 
/*      */   private static Font singleFont(char[] paramArrayOfChar, int paramInt1, int paramInt2, Map paramMap)
/*      */   {
/*  456 */     if (paramMap.get(TextAttribute.CHAR_REPLACEMENT) != null) {
/*  457 */       return null;
/*      */     }
/*      */ 
/*  460 */     Font localFont = null;
/*      */     try {
/*  462 */       localFont = (Font)paramMap.get(TextAttribute.FONT);
/*      */     }
/*      */     catch (ClassCastException localClassCastException) {
/*      */     }
/*  466 */     if (localFont == null) {
/*  467 */       if (paramMap.get(TextAttribute.FAMILY) != null) {
/*  468 */         localFont = Font.getFont(paramMap);
/*  469 */         if (localFont.canDisplayUpTo(paramArrayOfChar, paramInt1, paramInt2) != -1)
/*  470 */           return null;
/*      */       }
/*      */       else {
/*  473 */         FontResolver localFontResolver = FontResolver.getInstance();
/*  474 */         CodePointIterator localCodePointIterator = CodePointIterator.create(paramArrayOfChar, paramInt1, paramInt2);
/*  475 */         int i = localFontResolver.nextFontRunIndex(localCodePointIterator);
/*  476 */         if (localCodePointIterator.charIndex() == paramInt2) {
/*  477 */           localFont = localFontResolver.getFont(i, paramMap);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  482 */     if (sameBaselineUpTo(localFont, paramArrayOfChar, paramInt1, paramInt2) != paramInt2) {
/*  483 */       return null;
/*      */     }
/*      */ 
/*  486 */     return localFont;
/*      */   }
/*      */ 
/*      */   public TextLayout(AttributedCharacterIterator paramAttributedCharacterIterator, FontRenderContext paramFontRenderContext)
/*      */   {
/*  505 */     if (paramAttributedCharacterIterator == null) {
/*  506 */       throw new IllegalArgumentException("Null iterator passed to TextLayout constructor.");
/*      */     }
/*      */ 
/*  509 */     int i = paramAttributedCharacterIterator.getBeginIndex();
/*  510 */     int j = paramAttributedCharacterIterator.getEndIndex();
/*  511 */     if (i == j) {
/*  512 */       throw new IllegalArgumentException("Zero length iterator passed to TextLayout constructor.");
/*      */     }
/*      */ 
/*  515 */     int k = j - i;
/*  516 */     paramAttributedCharacterIterator.first();
/*  517 */     char[] arrayOfChar = new char[k];
/*  518 */     int m = 0;
/*  519 */     for (int n = paramAttributedCharacterIterator.first(); n != 65535; n = paramAttributedCharacterIterator.next()) {
/*  520 */       arrayOfChar[(m++)] = n;
/*      */     }
/*      */ 
/*  523 */     paramAttributedCharacterIterator.first();
/*  524 */     if (paramAttributedCharacterIterator.getRunLimit() == j)
/*      */     {
/*  526 */       Map localMap = paramAttributedCharacterIterator.getAttributes();
/*  527 */       Font localFont = singleFont(arrayOfChar, 0, k, localMap);
/*  528 */       if (localFont != null) {
/*  529 */         fastInit(arrayOfChar, localFont, localMap, paramFontRenderContext);
/*  530 */         return;
/*      */       }
/*      */     }
/*      */ 
/*  534 */     standardInit(paramAttributedCharacterIterator, arrayOfChar, paramFontRenderContext);
/*      */   }
/*      */ 
/*      */   TextLayout(TextLine paramTextLine, byte paramByte, float[] paramArrayOfFloat, float paramFloat)
/*      */   {
/*  554 */     this.characterCount = paramTextLine.characterCount();
/*  555 */     this.baseline = paramByte;
/*  556 */     this.baselineOffsets = paramArrayOfFloat;
/*  557 */     this.textLine = paramTextLine;
/*  558 */     this.justifyRatio = paramFloat;
/*      */   }
/*      */ 
/*      */   private void paragraphInit(byte paramByte, CoreMetrics paramCoreMetrics, Map paramMap, char[] paramArrayOfChar)
/*      */   {
/*  566 */     this.baseline = paramByte;
/*      */ 
/*  569 */     this.baselineOffsets = TextLine.getNormalizedOffsets(paramCoreMetrics.baselineOffsets, this.baseline);
/*      */ 
/*  571 */     this.justifyRatio = AttributeValues.getJustification(paramMap);
/*  572 */     NumericShaper localNumericShaper = AttributeValues.getNumericShaping(paramMap);
/*  573 */     if (localNumericShaper != null)
/*  574 */       localNumericShaper.shape(paramArrayOfChar, 0, paramArrayOfChar.length);
/*      */   }
/*      */ 
/*      */   private void fastInit(char[] paramArrayOfChar, Font paramFont, Map paramMap, FontRenderContext paramFontRenderContext)
/*      */   {
/*  587 */     this.isVerticalLine = false;
/*      */ 
/*  589 */     LineMetrics localLineMetrics = paramFont.getLineMetrics(paramArrayOfChar, 0, paramArrayOfChar.length, paramFontRenderContext);
/*  590 */     CoreMetrics localCoreMetrics = CoreMetrics.get(localLineMetrics);
/*  591 */     byte b = (byte)localCoreMetrics.baselineIndex;
/*      */ 
/*  593 */     if (paramMap == null) {
/*  594 */       this.baseline = b;
/*  595 */       this.baselineOffsets = localCoreMetrics.baselineOffsets;
/*  596 */       this.justifyRatio = 1.0F;
/*      */     } else {
/*  598 */       paragraphInit(b, localCoreMetrics, paramMap, paramArrayOfChar);
/*      */     }
/*      */ 
/*  601 */     this.characterCount = paramArrayOfChar.length;
/*      */ 
/*  603 */     this.textLine = TextLine.fastCreateTextLine(paramFontRenderContext, paramArrayOfChar, paramFont, localCoreMetrics, paramMap);
/*      */   }
/*      */ 
/*      */   private void standardInit(AttributedCharacterIterator paramAttributedCharacterIterator, char[] paramArrayOfChar, FontRenderContext paramFontRenderContext)
/*      */   {
/*  613 */     this.characterCount = paramArrayOfChar.length;
/*      */ 
/*  622 */     Map localMap = paramAttributedCharacterIterator.getAttributes();
/*      */ 
/*  624 */     boolean bool = TextLine.advanceToFirstFont(paramAttributedCharacterIterator);
/*      */     Object localObject1;
/*      */     int i;
/*      */     Object localObject2;
/*  626 */     if (bool) {
/*  627 */       localObject1 = TextLine.getFontAtCurrentPos(paramAttributedCharacterIterator);
/*  628 */       i = paramAttributedCharacterIterator.getIndex() - paramAttributedCharacterIterator.getBeginIndex();
/*  629 */       localObject2 = ((Font)localObject1).getLineMetrics(paramArrayOfChar, i, i + 1, paramFontRenderContext);
/*  630 */       CoreMetrics localCoreMetrics = CoreMetrics.get((LineMetrics)localObject2);
/*  631 */       paragraphInit((byte)localCoreMetrics.baselineIndex, localCoreMetrics, localMap, paramArrayOfChar);
/*      */     }
/*      */     else
/*      */     {
/*  637 */       localObject1 = (GraphicAttribute)localMap.get(TextAttribute.CHAR_REPLACEMENT);
/*      */ 
/*  639 */       i = getBaselineFromGraphic((GraphicAttribute)localObject1);
/*  640 */       localObject2 = GraphicComponent.createCoreMetrics((GraphicAttribute)localObject1);
/*  641 */       paragraphInit(i, (CoreMetrics)localObject2, localMap, paramArrayOfChar);
/*      */     }
/*      */ 
/*  645 */     this.textLine = TextLine.standardCreateTextLine(paramFontRenderContext, paramAttributedCharacterIterator, paramArrayOfChar, this.baselineOffsets);
/*      */   }
/*      */ 
/*      */   private void ensureCache()
/*      */   {
/*  654 */     if (!this.cacheIsValid)
/*  655 */       buildCache();
/*      */   }
/*      */ 
/*      */   private void buildCache()
/*      */   {
/*  660 */     this.lineMetrics = this.textLine.getMetrics();
/*      */     int i;
/*      */     int j;
/*  663 */     if (this.textLine.isDirectionLTR())
/*      */     {
/*  665 */       i = this.characterCount - 1;
/*  666 */       while (i != -1) {
/*  667 */         j = this.textLine.visualToLogical(i);
/*  668 */         if (!this.textLine.isCharSpace(j))
/*      */         {
/*      */           break;
/*      */         }
/*  672 */         i--;
/*      */       }
/*      */ 
/*  675 */       if (i == this.characterCount - 1) {
/*  676 */         this.visibleAdvance = this.lineMetrics.advance;
/*      */       }
/*  678 */       else if (i == -1) {
/*  679 */         this.visibleAdvance = 0.0F;
/*      */       }
/*      */       else {
/*  682 */         j = this.textLine.visualToLogical(i);
/*  683 */         this.visibleAdvance = (this.textLine.getCharLinePosition(j) + this.textLine.getCharAdvance(j));
/*      */       }
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/*  689 */       i = 0;
/*  690 */       while (i != this.characterCount) {
/*  691 */         j = this.textLine.visualToLogical(i);
/*  692 */         if (!this.textLine.isCharSpace(j))
/*      */         {
/*      */           break;
/*      */         }
/*  696 */         i++;
/*      */       }
/*      */ 
/*  699 */       if (i == this.characterCount) {
/*  700 */         this.visibleAdvance = 0.0F;
/*      */       }
/*  702 */       else if (i == 0) {
/*  703 */         this.visibleAdvance = this.lineMetrics.advance;
/*      */       }
/*      */       else {
/*  706 */         j = this.textLine.visualToLogical(i);
/*  707 */         float f = this.textLine.getCharLinePosition(j);
/*  708 */         this.visibleAdvance = (this.lineMetrics.advance - f);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  713 */     this.naturalBounds = null;
/*  714 */     this.boundsRect = null;
/*      */ 
/*  717 */     this.hashCodeCache = 0;
/*      */ 
/*  719 */     this.cacheIsValid = true;
/*      */   }
/*      */ 
/*      */   private Rectangle2D getNaturalBounds()
/*      */   {
/*  727 */     ensureCache();
/*      */ 
/*  729 */     if (this.naturalBounds == null) {
/*  730 */       this.naturalBounds = this.textLine.getItalicBounds();
/*      */     }
/*      */ 
/*  733 */     return this.naturalBounds;
/*      */   }
/*      */ 
/*      */   protected Object clone()
/*      */   {
/*      */     try
/*      */     {
/*  753 */       return super.clone();
/*      */     } catch (CloneNotSupportedException localCloneNotSupportedException) {
/*      */     }
/*  756 */     throw new InternalError();
/*      */   }
/*      */ 
/*      */   private void checkTextHit(TextHitInfo paramTextHitInfo)
/*      */   {
/*  765 */     if (paramTextHitInfo == null) {
/*  766 */       throw new IllegalArgumentException("TextHitInfo is null.");
/*      */     }
/*      */ 
/*  769 */     if ((paramTextHitInfo.getInsertionIndex() < 0) || (paramTextHitInfo.getInsertionIndex() > this.characterCount))
/*      */     {
/*  771 */       throw new IllegalArgumentException("TextHitInfo is out of range");
/*      */     }
/*      */   }
/*      */ 
/*      */   public TextLayout getJustifiedLayout(float paramFloat)
/*      */   {
/*  792 */     if (paramFloat <= 0.0F) {
/*  793 */       throw new IllegalArgumentException("justificationWidth <= 0 passed to TextLayout.getJustifiedLayout()");
/*      */     }
/*      */ 
/*  796 */     if (this.justifyRatio == -53.900002F) {
/*  797 */       throw new Error("Can't justify again.");
/*      */     }
/*      */ 
/*  800 */     ensureCache();
/*      */ 
/*  803 */     int i = this.characterCount;
/*  804 */     while ((i > 0) && (this.textLine.isCharWhitespace(i - 1))) {
/*  805 */       i--;
/*      */     }
/*      */ 
/*  808 */     TextLine localTextLine = this.textLine.getJustifiedLine(paramFloat, this.justifyRatio, 0, i);
/*  809 */     if (localTextLine != null) {
/*  810 */       return new TextLayout(localTextLine, this.baseline, this.baselineOffsets, -53.900002F);
/*      */     }
/*      */ 
/*  813 */     return this;
/*      */   }
/*      */ 
/*      */   protected void handleJustify(float paramFloat)
/*      */   {
/*      */   }
/*      */ 
/*      */   public byte getBaseline()
/*      */   {
/*  853 */     return this.baseline;
/*      */   }
/*      */ 
/*      */   public float[] getBaselineOffsets()
/*      */   {
/*  872 */     float[] arrayOfFloat = new float[this.baselineOffsets.length];
/*  873 */     System.arraycopy(this.baselineOffsets, 0, arrayOfFloat, 0, arrayOfFloat.length);
/*  874 */     return arrayOfFloat;
/*      */   }
/*      */ 
/*      */   public float getAdvance()
/*      */   {
/*  885 */     ensureCache();
/*  886 */     return this.lineMetrics.advance;
/*      */   }
/*      */ 
/*      */   public float getVisibleAdvance()
/*      */   {
/*  897 */     ensureCache();
/*  898 */     return this.visibleAdvance;
/*      */   }
/*      */ 
/*      */   public float getAscent()
/*      */   {
/*  913 */     ensureCache();
/*  914 */     return this.lineMetrics.ascent;
/*      */   }
/*      */ 
/*      */   public float getDescent()
/*      */   {
/*  928 */     ensureCache();
/*  929 */     return this.lineMetrics.descent;
/*      */   }
/*      */ 
/*      */   public float getLeading()
/*      */   {
/*  954 */     ensureCache();
/*  955 */     return this.lineMetrics.leading;
/*      */   }
/*      */ 
/*      */   public Rectangle2D getBounds()
/*      */   {
/*  969 */     ensureCache();
/*      */ 
/*  971 */     if (this.boundsRect == null) {
/*  972 */       localObject = this.textLine.getVisualBounds();
/*  973 */       if ((dx != 0.0F) || (dy != 0.0F)) {
/*  974 */         ((Rectangle2D)localObject).setRect(((Rectangle2D)localObject).getX() - dx, ((Rectangle2D)localObject).getY() - dy, ((Rectangle2D)localObject).getWidth(), ((Rectangle2D)localObject).getHeight());
/*      */       }
/*      */ 
/*  979 */       this.boundsRect = ((Rectangle2D)localObject);
/*      */     }
/*      */ 
/*  982 */     Object localObject = new Rectangle2D.Float();
/*  983 */     ((Rectangle2D)localObject).setRect(this.boundsRect);
/*      */ 
/*  985 */     return localObject;
/*      */   }
/*      */ 
/*      */   public Rectangle getPixelBounds(FontRenderContext paramFontRenderContext, float paramFloat1, float paramFloat2)
/*      */   {
/* 1005 */     return this.textLine.getPixelBounds(paramFontRenderContext, paramFloat1, paramFloat2);
/*      */   }
/*      */ 
/*      */   public boolean isLeftToRight()
/*      */   {
/* 1025 */     return this.textLine.isDirectionLTR();
/*      */   }
/*      */ 
/*      */   public boolean isVertical()
/*      */   {
/* 1034 */     return this.isVerticalLine;
/*      */   }
/*      */ 
/*      */   public int getCharacterCount()
/*      */   {
/* 1043 */     return this.characterCount;
/*      */   }
/*      */ 
/*      */   private float[] getCaretInfo(int paramInt, Rectangle2D paramRectangle2D, float[] paramArrayOfFloat)
/*      */   {
/*      */     float f8;
/*      */     float f2;
/*      */     float f1;
/*      */     float f4;
/*      */     float f3;
/* 1123 */     if ((paramInt == 0) || (paramInt == this.characterCount))
/*      */     {
/*      */       int j;
/*      */       float f5;
/* 1127 */       if (paramInt == this.characterCount) {
/* 1128 */         j = this.textLine.visualToLogical(this.characterCount - 1);
/* 1129 */         f5 = this.textLine.getCharLinePosition(j) + this.textLine.getCharAdvance(j);
/*      */       }
/*      */       else
/*      */       {
/* 1133 */         j = this.textLine.visualToLogical(paramInt);
/* 1134 */         f5 = this.textLine.getCharLinePosition(j);
/*      */       }
/* 1136 */       f8 = this.textLine.getCharAngle(j);
/* 1137 */       float f9 = this.textLine.getCharShift(j);
/* 1138 */       f5 += f8 * f9;
/* 1139 */       f1 = f2 = f5 + f8 * this.textLine.getCharAscent(j);
/* 1140 */       f3 = f4 = f5 - f8 * this.textLine.getCharDescent(j);
/*      */     }
/*      */     else
/*      */     {
/* 1145 */       int i = this.textLine.visualToLogical(paramInt - 1);
/* 1146 */       f7 = this.textLine.getCharAngle(i);
/* 1147 */       f8 = this.textLine.getCharLinePosition(i) + this.textLine.getCharAdvance(i);
/*      */ 
/* 1149 */       if (f7 != 0.0F) {
/* 1150 */         f8 += f7 * this.textLine.getCharShift(i);
/* 1151 */         f1 = f8 + f7 * this.textLine.getCharAscent(i);
/* 1152 */         f3 = f8 - f7 * this.textLine.getCharDescent(i);
/*      */       }
/*      */       else {
/* 1155 */         f1 = f3 = f8;
/*      */       }
/*      */ 
/* 1159 */       i = this.textLine.visualToLogical(paramInt);
/* 1160 */       f7 = this.textLine.getCharAngle(i);
/* 1161 */       f8 = this.textLine.getCharLinePosition(i);
/* 1162 */       if (f7 != 0.0F) {
/* 1163 */         f8 += f7 * this.textLine.getCharShift(i);
/* 1164 */         f2 = f8 + f7 * this.textLine.getCharAscent(i);
/* 1165 */         f4 = f8 - f7 * this.textLine.getCharDescent(i);
/*      */       }
/*      */       else {
/* 1168 */         f2 = f4 = f8;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1173 */     float f6 = (f1 + f2) / 2.0F;
/* 1174 */     float f7 = (f3 + f4) / 2.0F;
/*      */ 
/* 1176 */     if (paramArrayOfFloat == null) {
/* 1177 */       paramArrayOfFloat = new float[2];
/*      */     }
/*      */ 
/* 1180 */     if (this.isVerticalLine) {
/* 1181 */       paramArrayOfFloat[1] = ((float)((f6 - f7) / paramRectangle2D.getWidth()));
/* 1182 */       paramArrayOfFloat[0] = ((float)(f6 + paramArrayOfFloat[1] * paramRectangle2D.getX()));
/*      */     }
/*      */     else {
/* 1185 */       paramArrayOfFloat[1] = ((float)((f6 - f7) / paramRectangle2D.getHeight()));
/* 1186 */       paramArrayOfFloat[0] = ((float)(f7 + paramArrayOfFloat[1] * paramRectangle2D.getMaxY()));
/*      */     }
/*      */ 
/* 1189 */     return paramArrayOfFloat;
/*      */   }
/*      */ 
/*      */   public float[] getCaretInfo(TextHitInfo paramTextHitInfo, Rectangle2D paramRectangle2D)
/*      */   {
/* 1210 */     ensureCache();
/* 1211 */     checkTextHit(paramTextHitInfo);
/*      */ 
/* 1213 */     return getCaretInfoTestInternal(paramTextHitInfo, paramRectangle2D);
/*      */   }
/*      */ 
/*      */   private float[] getCaretInfoTestInternal(TextHitInfo paramTextHitInfo, Rectangle2D paramRectangle2D)
/*      */   {
/* 1224 */     ensureCache();
/* 1225 */     checkTextHit(paramTextHitInfo);
/*      */ 
/* 1227 */     float[] arrayOfFloat = new float[6];
/*      */ 
/* 1230 */     getCaretInfo(hitToCaret(paramTextHitInfo), paramRectangle2D, arrayOfFloat);
/*      */ 
/* 1235 */     int i = paramTextHitInfo.getCharIndex();
/* 1236 */     boolean bool1 = paramTextHitInfo.isLeadingEdge();
/* 1237 */     boolean bool2 = this.textLine.isDirectionLTR();
/* 1238 */     int j = !isVertical() ? 1 : 0;
/*      */     Object localObject;
/*      */     double d1;
/*      */     double d5;
/*      */     double d3;
/*      */     double d4;
/*      */     double d6;
/* 1240 */     if ((i == -1) || (i == this.characterCount))
/*      */     {
/* 1243 */       localObject = this.textLine.getMetrics();
/* 1244 */       int k = bool2 == (i == -1) ? 1 : 0;
/* 1245 */       d1 = 0.0D;
/* 1246 */       if (j != 0) {
/* 1247 */         d3 = d5 = k != 0 ? 0.0D : ((TextLine.TextLineMetrics)localObject).advance;
/* 1248 */         d4 = -((TextLine.TextLineMetrics)localObject).ascent;
/* 1249 */         d6 = ((TextLine.TextLineMetrics)localObject).descent;
/*      */       } else {
/* 1251 */         d4 = d6 = k != 0 ? 0.0D : ((TextLine.TextLineMetrics)localObject).advance;
/* 1252 */         d3 = ((TextLine.TextLineMetrics)localObject).descent;
/* 1253 */         d5 = ((TextLine.TextLineMetrics)localObject).ascent;
/*      */       }
/*      */     } else {
/* 1256 */       localObject = this.textLine.getCoreMetricsAt(i);
/* 1257 */       d1 = ((CoreMetrics)localObject).italicAngle;
/* 1258 */       double d2 = this.textLine.getCharLinePosition(i, bool1);
/* 1259 */       if (((CoreMetrics)localObject).baselineIndex < 0)
/*      */       {
/* 1261 */         TextLine.TextLineMetrics localTextLineMetrics = this.textLine.getMetrics();
/* 1262 */         if (j != 0) {
/* 1263 */           d3 = d5 = d2;
/* 1264 */           if (((CoreMetrics)localObject).baselineIndex == -1) {
/* 1265 */             d4 = -localTextLineMetrics.ascent;
/* 1266 */             d6 = d4 + ((CoreMetrics)localObject).height;
/*      */           } else {
/* 1268 */             d6 = localTextLineMetrics.descent;
/* 1269 */             d4 = d6 - ((CoreMetrics)localObject).height;
/*      */           }
/*      */         } else {
/* 1272 */           d4 = d6 = d2;
/* 1273 */           d3 = localTextLineMetrics.descent;
/* 1274 */           d5 = localTextLineMetrics.ascent;
/*      */         }
/*      */       }
/*      */       else {
/* 1278 */         float f = this.baselineOffsets[localObject.baselineIndex];
/* 1279 */         if (j != 0) {
/* 1280 */           d2 += d1 * ((CoreMetrics)localObject).ssOffset;
/* 1281 */           d3 = d2 + d1 * ((CoreMetrics)localObject).ascent;
/* 1282 */           d5 = d2 - d1 * ((CoreMetrics)localObject).descent;
/* 1283 */           d4 = f - ((CoreMetrics)localObject).ascent;
/* 1284 */           d6 = f + ((CoreMetrics)localObject).descent;
/*      */         } else {
/* 1286 */           d2 -= d1 * ((CoreMetrics)localObject).ssOffset;
/* 1287 */           d4 = d2 + d1 * ((CoreMetrics)localObject).ascent;
/* 1288 */           d6 = d2 - d1 * ((CoreMetrics)localObject).descent;
/* 1289 */           d3 = f + ((CoreMetrics)localObject).ascent;
/* 1290 */           d5 = f + ((CoreMetrics)localObject).descent;
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 1295 */     arrayOfFloat[2] = ((float)d3);
/* 1296 */     arrayOfFloat[3] = ((float)d4);
/* 1297 */     arrayOfFloat[4] = ((float)d5);
/* 1298 */     arrayOfFloat[5] = ((float)d6);
/*      */ 
/* 1300 */     return arrayOfFloat;
/*      */   }
/*      */ 
/*      */   public float[] getCaretInfo(TextHitInfo paramTextHitInfo)
/*      */   {
/* 1313 */     return getCaretInfo(paramTextHitInfo, getNaturalBounds());
/*      */   }
/*      */ 
/*      */   private int hitToCaret(TextHitInfo paramTextHitInfo)
/*      */   {
/* 1326 */     int i = paramTextHitInfo.getCharIndex();
/*      */ 
/* 1328 */     if (i < 0)
/* 1329 */       return this.textLine.isDirectionLTR() ? 0 : this.characterCount;
/* 1330 */     if (i >= this.characterCount) {
/* 1331 */       return this.textLine.isDirectionLTR() ? this.characterCount : 0;
/*      */     }
/*      */ 
/* 1334 */     int j = this.textLine.logicalToVisual(i);
/*      */ 
/* 1336 */     if (paramTextHitInfo.isLeadingEdge() != this.textLine.isCharLTR(i)) {
/* 1337 */       j++;
/*      */     }
/*      */ 
/* 1340 */     return j;
/*      */   }
/*      */ 
/*      */   private TextHitInfo caretToHit(int paramInt)
/*      */   {
/* 1353 */     if ((paramInt == 0) || (paramInt == this.characterCount))
/*      */     {
/* 1355 */       if ((paramInt == this.characterCount) == this.textLine.isDirectionLTR()) {
/* 1356 */         return TextHitInfo.leading(this.characterCount);
/*      */       }
/*      */ 
/* 1359 */       return TextHitInfo.trailing(-1);
/*      */     }
/*      */ 
/* 1364 */     int i = this.textLine.visualToLogical(paramInt);
/* 1365 */     boolean bool = this.textLine.isCharLTR(i);
/*      */ 
/* 1367 */     return bool ? TextHitInfo.leading(i) : TextHitInfo.trailing(i);
/*      */   }
/*      */ 
/*      */   private boolean caretIsValid(int paramInt)
/*      */   {
/* 1374 */     if ((paramInt == this.characterCount) || (paramInt == 0)) {
/* 1375 */       return true;
/*      */     }
/*      */ 
/* 1378 */     int i = this.textLine.visualToLogical(paramInt);
/*      */ 
/* 1380 */     if (!this.textLine.isCharLTR(i)) {
/* 1381 */       i = this.textLine.visualToLogical(paramInt - 1);
/* 1382 */       if (this.textLine.isCharLTR(i)) {
/* 1383 */         return true;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1390 */     return this.textLine.caretAtOffsetIsValid(i);
/*      */   }
/*      */ 
/*      */   public TextHitInfo getNextRightHit(TextHitInfo paramTextHitInfo)
/*      */   {
/* 1403 */     ensureCache();
/* 1404 */     checkTextHit(paramTextHitInfo);
/*      */ 
/* 1406 */     int i = hitToCaret(paramTextHitInfo);
/*      */ 
/* 1408 */     if (i == this.characterCount) {
/* 1409 */       return null;
/*      */     }
/*      */     do
/*      */     {
/* 1413 */       i++;
/* 1414 */     }while (!caretIsValid(i));
/*      */ 
/* 1416 */     return caretToHit(i);
/*      */   }
/*      */ 
/*      */   public TextHitInfo getNextRightHit(int paramInt, CaretPolicy paramCaretPolicy)
/*      */   {
/* 1435 */     if ((paramInt < 0) || (paramInt > this.characterCount)) {
/* 1436 */       throw new IllegalArgumentException("Offset out of bounds in TextLayout.getNextRightHit()");
/*      */     }
/*      */ 
/* 1439 */     if (paramCaretPolicy == null) {
/* 1440 */       throw new IllegalArgumentException("Null CaretPolicy passed to TextLayout.getNextRightHit()");
/*      */     }
/*      */ 
/* 1443 */     TextHitInfo localTextHitInfo1 = TextHitInfo.afterOffset(paramInt);
/* 1444 */     TextHitInfo localTextHitInfo2 = localTextHitInfo1.getOtherHit();
/*      */ 
/* 1446 */     TextHitInfo localTextHitInfo3 = getNextRightHit(paramCaretPolicy.getStrongCaret(localTextHitInfo1, localTextHitInfo2, this));
/*      */ 
/* 1448 */     if (localTextHitInfo3 != null) {
/* 1449 */       TextHitInfo localTextHitInfo4 = getVisualOtherHit(localTextHitInfo3);
/* 1450 */       return paramCaretPolicy.getStrongCaret(localTextHitInfo4, localTextHitInfo3, this);
/*      */     }
/*      */ 
/* 1453 */     return null;
/*      */   }
/*      */ 
/*      */   public TextHitInfo getNextRightHit(int paramInt)
/*      */   {
/* 1472 */     return getNextRightHit(paramInt, DEFAULT_CARET_POLICY);
/*      */   }
/*      */ 
/*      */   public TextHitInfo getNextLeftHit(TextHitInfo paramTextHitInfo)
/*      */   {
/* 1485 */     ensureCache();
/* 1486 */     checkTextHit(paramTextHitInfo);
/*      */ 
/* 1488 */     int i = hitToCaret(paramTextHitInfo);
/*      */ 
/* 1490 */     if (i == 0) {
/* 1491 */       return null;
/*      */     }
/*      */     do
/*      */     {
/* 1495 */       i--;
/* 1496 */     }while (!caretIsValid(i));
/*      */ 
/* 1498 */     return caretToHit(i);
/*      */   }
/*      */ 
/*      */   public TextHitInfo getNextLeftHit(int paramInt, CaretPolicy paramCaretPolicy)
/*      */   {
/* 1517 */     if (paramCaretPolicy == null) {
/* 1518 */       throw new IllegalArgumentException("Null CaretPolicy passed to TextLayout.getNextLeftHit()");
/*      */     }
/*      */ 
/* 1521 */     if ((paramInt < 0) || (paramInt > this.characterCount)) {
/* 1522 */       throw new IllegalArgumentException("Offset out of bounds in TextLayout.getNextLeftHit()");
/*      */     }
/*      */ 
/* 1525 */     TextHitInfo localTextHitInfo1 = TextHitInfo.afterOffset(paramInt);
/* 1526 */     TextHitInfo localTextHitInfo2 = localTextHitInfo1.getOtherHit();
/*      */ 
/* 1528 */     TextHitInfo localTextHitInfo3 = getNextLeftHit(paramCaretPolicy.getStrongCaret(localTextHitInfo1, localTextHitInfo2, this));
/*      */ 
/* 1530 */     if (localTextHitInfo3 != null) {
/* 1531 */       TextHitInfo localTextHitInfo4 = getVisualOtherHit(localTextHitInfo3);
/* 1532 */       return paramCaretPolicy.getStrongCaret(localTextHitInfo4, localTextHitInfo3, this);
/*      */     }
/*      */ 
/* 1535 */     return null;
/*      */   }
/*      */ 
/*      */   public TextHitInfo getNextLeftHit(int paramInt)
/*      */   {
/* 1554 */     return getNextLeftHit(paramInt, DEFAULT_CARET_POLICY);
/*      */   }
/*      */ 
/*      */   public TextHitInfo getVisualOtherHit(TextHitInfo paramTextHitInfo)
/*      */   {
/* 1565 */     ensureCache();
/* 1566 */     checkTextHit(paramTextHitInfo);
/*      */ 
/* 1568 */     int i = paramTextHitInfo.getCharIndex();
/*      */     int k;
/*      */     int j;
/*      */     boolean bool;
/* 1573 */     if ((i == -1) || (i == this.characterCount))
/*      */     {
/* 1576 */       if (this.textLine.isDirectionLTR() == (i == -1)) {
/* 1577 */         k = 0;
/*      */       }
/*      */       else {
/* 1580 */         k = this.characterCount - 1;
/*      */       }
/*      */ 
/* 1583 */       j = this.textLine.visualToLogical(k);
/*      */ 
/* 1585 */       if (this.textLine.isDirectionLTR() == (i == -1))
/*      */       {
/* 1587 */         bool = this.textLine.isCharLTR(j);
/*      */       }
/*      */       else
/*      */       {
/* 1591 */         bool = !this.textLine.isCharLTR(j);
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/* 1596 */       k = this.textLine.logicalToVisual(i);
/*      */       int m;
/* 1599 */       if (this.textLine.isCharLTR(i) == paramTextHitInfo.isLeadingEdge()) {
/* 1600 */         k--;
/* 1601 */         m = 0;
/*      */       }
/*      */       else {
/* 1604 */         k++;
/* 1605 */         m = 1;
/*      */       }
/*      */ 
/* 1608 */       if ((k > -1) && (k < this.characterCount)) {
/* 1609 */         j = this.textLine.visualToLogical(k);
/* 1610 */         bool = m == this.textLine.isCharLTR(j);
/*      */       }
/*      */       else {
/* 1613 */         j = m == this.textLine.isDirectionLTR() ? this.characterCount : -1;
/*      */ 
/* 1615 */         bool = j == this.characterCount;
/*      */       }
/*      */     }
/*      */ 
/* 1619 */     return bool ? TextHitInfo.leading(j) : TextHitInfo.trailing(j);
/*      */   }
/*      */ 
/*      */   private double[] getCaretPath(TextHitInfo paramTextHitInfo, Rectangle2D paramRectangle2D)
/*      */   {
/* 1624 */     float[] arrayOfFloat = getCaretInfo(paramTextHitInfo, paramRectangle2D);
/* 1625 */     return new double[] { arrayOfFloat[2], arrayOfFloat[3], arrayOfFloat[4], arrayOfFloat[5] };
/*      */   }
/*      */ 
/*      */   private double[] getCaretPath(int paramInt, Rectangle2D paramRectangle2D, boolean paramBoolean)
/*      */   {
/* 1640 */     float[] arrayOfFloat = getCaretInfo(paramInt, paramRectangle2D, null);
/*      */ 
/* 1642 */     double d1 = arrayOfFloat[0];
/* 1643 */     double d2 = arrayOfFloat[1];
/*      */ 
/* 1646 */     double d7 = -3141.5900000000001D; double d8 = -2.7D;
/*      */ 
/* 1648 */     double d9 = paramRectangle2D.getX();
/* 1649 */     double d10 = d9 + paramRectangle2D.getWidth();
/* 1650 */     double d11 = paramRectangle2D.getY();
/* 1651 */     double d12 = d11 + paramRectangle2D.getHeight();
/*      */ 
/* 1653 */     int i = 0;
/*      */     double d3;
/*      */     double d5;
/*      */     double d4;
/*      */     double d6;
/* 1655 */     if (this.isVerticalLine)
/*      */     {
/* 1657 */       if (d2 >= 0.0D) {
/* 1658 */         d3 = d9;
/* 1659 */         d5 = d10;
/*      */       }
/*      */       else {
/* 1662 */         d5 = d9;
/* 1663 */         d3 = d10;
/*      */       }
/*      */ 
/* 1666 */       d4 = d1 + d3 * d2;
/* 1667 */       d6 = d1 + d5 * d2;
/*      */ 
/* 1671 */       if (paramBoolean) {
/* 1672 */         if (d4 < d11) {
/* 1673 */           if ((d2 <= 0.0D) || (d6 <= d11)) {
/* 1674 */             d4 = d6 = d11;
/*      */           }
/*      */           else {
/* 1677 */             i = 1;
/* 1678 */             d4 = d11;
/* 1679 */             d8 = d11;
/* 1680 */             d7 = d5 + (d11 - d6) / d2;
/* 1681 */             if (d6 > d12) {
/* 1682 */               d6 = d12;
/*      */             }
/*      */           }
/*      */         }
/* 1686 */         else if (d6 > d12) {
/* 1687 */           if ((d2 >= 0.0D) || (d4 >= d12)) {
/* 1688 */             d4 = d6 = d12;
/*      */           }
/*      */           else {
/* 1691 */             i = 1;
/* 1692 */             d6 = d12;
/* 1693 */             d8 = d12;
/* 1694 */             d7 = d3 + (d12 - d5) / d2;
/*      */           }
/*      */         }
/*      */       }
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/* 1702 */       if (d2 >= 0.0D) {
/* 1703 */         d4 = d12;
/* 1704 */         d6 = d11;
/*      */       }
/*      */       else {
/* 1707 */         d6 = d12;
/* 1708 */         d4 = d11;
/*      */       }
/*      */ 
/* 1711 */       d3 = d1 - d4 * d2;
/* 1712 */       d5 = d1 - d6 * d2;
/*      */ 
/* 1716 */       if (paramBoolean) {
/* 1717 */         if (d3 < d9) {
/* 1718 */           if ((d2 <= 0.0D) || (d5 <= d9)) {
/* 1719 */             d3 = d5 = d9;
/*      */           }
/*      */           else {
/* 1722 */             i = 1;
/* 1723 */             d3 = d9;
/* 1724 */             d7 = d9;
/* 1725 */             d8 = d6 - (d9 - d5) / d2;
/* 1726 */             if (d5 > d10) {
/* 1727 */               d5 = d10;
/*      */             }
/*      */           }
/*      */         }
/* 1731 */         else if (d5 > d10) {
/* 1732 */           if ((d2 >= 0.0D) || (d3 >= d10)) {
/* 1733 */             d3 = d5 = d10;
/*      */           }
/*      */           else {
/* 1736 */             i = 1;
/* 1737 */             d5 = d10;
/* 1738 */             d7 = d10;
/* 1739 */             d8 = d4 - (d10 - d3) / d2;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 1745 */     return new double[] { d3, d4, d5, i != 0 ? new double[] { d3, d4, d7, d8, d5, d6 } : d6 };
/*      */   }
/*      */ 
/*      */   private static GeneralPath pathToShape(double[] paramArrayOfDouble, boolean paramBoolean, LayoutPathImpl paramLayoutPathImpl)
/*      */   {
/* 1752 */     GeneralPath localGeneralPath = new GeneralPath(0, paramArrayOfDouble.length);
/* 1753 */     localGeneralPath.moveTo((float)paramArrayOfDouble[0], (float)paramArrayOfDouble[1]);
/* 1754 */     for (int i = 2; i < paramArrayOfDouble.length; i += 2) {
/* 1755 */       localGeneralPath.lineTo((float)paramArrayOfDouble[i], (float)paramArrayOfDouble[(i + 1)]);
/*      */     }
/* 1757 */     if (paramBoolean) {
/* 1758 */       localGeneralPath.closePath();
/*      */     }
/*      */ 
/* 1761 */     if (paramLayoutPathImpl != null) {
/* 1762 */       localGeneralPath = (GeneralPath)paramLayoutPathImpl.mapShape(localGeneralPath);
/*      */     }
/* 1764 */     return localGeneralPath;
/*      */   }
/*      */ 
/*      */   public Shape getCaretShape(TextHitInfo paramTextHitInfo, Rectangle2D paramRectangle2D)
/*      */   {
/* 1778 */     ensureCache();
/* 1779 */     checkTextHit(paramTextHitInfo);
/*      */ 
/* 1781 */     if (paramRectangle2D == null) {
/* 1782 */       throw new IllegalArgumentException("Null Rectangle2D passed to TextLayout.getCaret()");
/*      */     }
/*      */ 
/* 1785 */     return pathToShape(getCaretPath(paramTextHitInfo, paramRectangle2D), false, this.textLine.getLayoutPath());
/*      */   }
/*      */ 
/*      */   public Shape getCaretShape(TextHitInfo paramTextHitInfo)
/*      */   {
/* 1797 */     return getCaretShape(paramTextHitInfo, getNaturalBounds());
/*      */   }
/*      */ 
/*      */   private final TextHitInfo getStrongHit(TextHitInfo paramTextHitInfo1, TextHitInfo paramTextHitInfo2)
/*      */   {
/* 1814 */     int i = getCharacterLevel(paramTextHitInfo1.getCharIndex());
/* 1815 */     int j = getCharacterLevel(paramTextHitInfo2.getCharIndex());
/*      */ 
/* 1817 */     if (i == j) {
/* 1818 */       if ((paramTextHitInfo2.isLeadingEdge()) && (!paramTextHitInfo1.isLeadingEdge())) {
/* 1819 */         return paramTextHitInfo2;
/*      */       }
/*      */ 
/* 1822 */       return paramTextHitInfo1;
/*      */     }
/*      */ 
/* 1826 */     return i < j ? paramTextHitInfo1 : paramTextHitInfo2;
/*      */   }
/*      */ 
/*      */   public byte getCharacterLevel(int paramInt)
/*      */   {
/* 1840 */     if ((paramInt < -1) || (paramInt > this.characterCount)) {
/* 1841 */       throw new IllegalArgumentException("Index is out of range in getCharacterLevel.");
/*      */     }
/*      */ 
/* 1844 */     ensureCache();
/* 1845 */     if ((paramInt == -1) || (paramInt == this.characterCount)) {
/* 1846 */       return (byte)(this.textLine.isDirectionLTR() ? 0 : 1);
/*      */     }
/*      */ 
/* 1849 */     return this.textLine.getCharLevel(paramInt);
/*      */   }
/*      */ 
/*      */   public Shape[] getCaretShapes(int paramInt, Rectangle2D paramRectangle2D, CaretPolicy paramCaretPolicy)
/*      */   {
/* 1865 */     ensureCache();
/*      */ 
/* 1867 */     if ((paramInt < 0) || (paramInt > this.characterCount)) {
/* 1868 */       throw new IllegalArgumentException("Offset out of bounds in TextLayout.getCaretShapes()");
/*      */     }
/*      */ 
/* 1871 */     if (paramRectangle2D == null) {
/* 1872 */       throw new IllegalArgumentException("Null Rectangle2D passed to TextLayout.getCaretShapes()");
/*      */     }
/*      */ 
/* 1875 */     if (paramCaretPolicy == null) {
/* 1876 */       throw new IllegalArgumentException("Null CaretPolicy passed to TextLayout.getCaretShapes()");
/*      */     }
/*      */ 
/* 1879 */     Shape[] arrayOfShape = new Shape[2];
/*      */ 
/* 1881 */     TextHitInfo localTextHitInfo1 = TextHitInfo.afterOffset(paramInt);
/*      */ 
/* 1883 */     int i = hitToCaret(localTextHitInfo1);
/*      */ 
/* 1885 */     LayoutPathImpl localLayoutPathImpl = this.textLine.getLayoutPath();
/* 1886 */     GeneralPath localGeneralPath1 = pathToShape(getCaretPath(localTextHitInfo1, paramRectangle2D), false, localLayoutPathImpl);
/* 1887 */     TextHitInfo localTextHitInfo2 = localTextHitInfo1.getOtherHit();
/* 1888 */     int j = hitToCaret(localTextHitInfo2);
/*      */ 
/* 1890 */     if (i == j) {
/* 1891 */       arrayOfShape[0] = localGeneralPath1;
/*      */     }
/*      */     else {
/* 1894 */       GeneralPath localGeneralPath2 = pathToShape(getCaretPath(localTextHitInfo2, paramRectangle2D), false, localLayoutPathImpl);
/*      */ 
/* 1896 */       TextHitInfo localTextHitInfo3 = paramCaretPolicy.getStrongCaret(localTextHitInfo1, localTextHitInfo2, this);
/* 1897 */       boolean bool = localTextHitInfo3.equals(localTextHitInfo1);
/*      */ 
/* 1899 */       if (bool) {
/* 1900 */         arrayOfShape[0] = localGeneralPath1;
/* 1901 */         arrayOfShape[1] = localGeneralPath2;
/*      */       }
/*      */       else {
/* 1904 */         arrayOfShape[0] = localGeneralPath2;
/* 1905 */         arrayOfShape[1] = localGeneralPath1;
/*      */       }
/*      */     }
/*      */ 
/* 1909 */     return arrayOfShape;
/*      */   }
/*      */ 
/*      */   public Shape[] getCaretShapes(int paramInt, Rectangle2D paramRectangle2D)
/*      */   {
/* 1925 */     return getCaretShapes(paramInt, paramRectangle2D, DEFAULT_CARET_POLICY);
/*      */   }
/*      */ 
/*      */   public Shape[] getCaretShapes(int paramInt)
/*      */   {
/* 1940 */     return getCaretShapes(paramInt, getNaturalBounds(), DEFAULT_CARET_POLICY);
/*      */   }
/*      */ 
/*      */   private GeneralPath boundingShape(double[] paramArrayOfDouble1, double[] paramArrayOfDouble2)
/*      */   {
/* 1955 */     GeneralPath localGeneralPath = pathToShape(paramArrayOfDouble1, false, null);
/*      */     int i;
/* 1959 */     if (this.isVerticalLine) {
/* 1960 */       i = (paramArrayOfDouble1[1] > paramArrayOfDouble1[(paramArrayOfDouble1.length - 1)] ? 1 : 0) == (paramArrayOfDouble2[1] > paramArrayOfDouble2[(paramArrayOfDouble2.length - 1)] ? 1 : 0) ? 1 : 0;
/*      */     }
/*      */     else
/*      */     {
/* 1964 */       i = (paramArrayOfDouble1[0] > paramArrayOfDouble1[(paramArrayOfDouble1.length - 2)] ? 1 : 0) == (paramArrayOfDouble2[0] > paramArrayOfDouble2[(paramArrayOfDouble2.length - 2)] ? 1 : 0) ? 1 : 0;
/*      */     }
/*      */     int j;
/*      */     int k;
/*      */     int m;
/* 1972 */     if (i != 0) {
/* 1973 */       j = paramArrayOfDouble2.length - 2;
/* 1974 */       k = -2;
/* 1975 */       m = -2;
/*      */     }
/*      */     else {
/* 1978 */       j = 0;
/* 1979 */       k = paramArrayOfDouble2.length;
/* 1980 */       m = 2;
/*      */     }
/*      */ 
/* 1983 */     for (int n = j; n != k; n += m) {
/* 1984 */       localGeneralPath.lineTo((float)paramArrayOfDouble2[n], (float)paramArrayOfDouble2[(n + 1)]);
/*      */     }
/*      */ 
/* 1987 */     localGeneralPath.closePath();
/*      */ 
/* 1989 */     return localGeneralPath;
/*      */   }
/*      */ 
/*      */   private GeneralPath caretBoundingShape(int paramInt1, int paramInt2, Rectangle2D paramRectangle2D)
/*      */   {
/* 1998 */     if (paramInt1 > paramInt2) {
/* 1999 */       int i = paramInt1;
/* 2000 */       paramInt1 = paramInt2;
/* 2001 */       paramInt2 = i;
/*      */     }
/*      */ 
/* 2004 */     return boundingShape(getCaretPath(paramInt1, paramRectangle2D, true), getCaretPath(paramInt2, paramRectangle2D, true));
/*      */   }
/*      */ 
/*      */   private GeneralPath leftShape(Rectangle2D paramRectangle2D)
/*      */   {
/*      */     double[] arrayOfDouble1;
/* 2016 */     if (this.isVerticalLine) {
/* 2017 */       arrayOfDouble1 = new double[] { paramRectangle2D.getX(), paramRectangle2D.getY(), paramRectangle2D.getX() + paramRectangle2D.getWidth(), paramRectangle2D.getY() };
/*      */     }
/*      */     else
/*      */     {
/* 2021 */       arrayOfDouble1 = new double[] { paramRectangle2D.getX(), paramRectangle2D.getY() + paramRectangle2D.getHeight(), paramRectangle2D.getX(), paramRectangle2D.getY() };
/*      */     }
/*      */ 
/* 2026 */     double[] arrayOfDouble2 = getCaretPath(0, paramRectangle2D, true);
/*      */ 
/* 2028 */     return boundingShape(arrayOfDouble1, arrayOfDouble2);
/*      */   }
/*      */ 
/*      */   private GeneralPath rightShape(Rectangle2D paramRectangle2D)
/*      */   {
/*      */     double[] arrayOfDouble1;
/* 2037 */     if (this.isVerticalLine) {
/* 2038 */       arrayOfDouble1 = new double[] { paramRectangle2D.getX(), paramRectangle2D.getY() + paramRectangle2D.getHeight(), paramRectangle2D.getX() + paramRectangle2D.getWidth(), paramRectangle2D.getY() + paramRectangle2D.getHeight() };
/*      */     }
/*      */     else
/*      */     {
/* 2045 */       arrayOfDouble1 = new double[] { paramRectangle2D.getX() + paramRectangle2D.getWidth(), paramRectangle2D.getY() + paramRectangle2D.getHeight(), paramRectangle2D.getX() + paramRectangle2D.getWidth(), paramRectangle2D.getY() };
/*      */     }
/*      */ 
/* 2053 */     double[] arrayOfDouble2 = getCaretPath(this.characterCount, paramRectangle2D, true);
/*      */ 
/* 2055 */     return boundingShape(arrayOfDouble2, arrayOfDouble1);
/*      */   }
/*      */ 
/*      */   public int[] getLogicalRangesForVisualSelection(TextHitInfo paramTextHitInfo1, TextHitInfo paramTextHitInfo2)
/*      */   {
/* 2069 */     ensureCache();
/*      */ 
/* 2071 */     checkTextHit(paramTextHitInfo1);
/* 2072 */     checkTextHit(paramTextHitInfo2);
/*      */ 
/* 2076 */     boolean[] arrayOfBoolean = new boolean[this.characterCount];
/*      */ 
/* 2078 */     int i = hitToCaret(paramTextHitInfo1);
/* 2079 */     int j = hitToCaret(paramTextHitInfo2);
/*      */ 
/* 2081 */     if (i > j) {
/* 2082 */       k = i;
/* 2083 */       i = j;
/* 2084 */       j = k;
/*      */     }
/*      */ 
/* 2094 */     if (i < j) {
/* 2095 */       k = i;
/* 2096 */       while (k < j) {
/* 2097 */         arrayOfBoolean[this.textLine.visualToLogical(k)] = true;
/* 2098 */         k++;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2106 */     int k = 0;
/* 2107 */     int m = 0;
/* 2108 */     for (int n = 0; n < this.characterCount; n++) {
/* 2109 */       if (arrayOfBoolean[n] != m) {
/* 2110 */         m = m == 0 ? 1 : 0;
/* 2111 */         if (m != 0) {
/* 2112 */           k++;
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 2117 */     int[] arrayOfInt = new int[k * 2];
/* 2118 */     k = 0;
/* 2119 */     m = 0;
/* 2120 */     for (int i1 = 0; i1 < this.characterCount; i1++) {
/* 2121 */       if (arrayOfBoolean[i1] != m) {
/* 2122 */         arrayOfInt[(k++)] = i1;
/* 2123 */         m = m == 0 ? 1 : 0;
/*      */       }
/*      */     }
/* 2126 */     if (m != 0) {
/* 2127 */       arrayOfInt[(k++)] = this.characterCount;
/*      */     }
/*      */ 
/* 2130 */     return arrayOfInt;
/*      */   }
/*      */ 
/*      */   public Shape getVisualHighlightShape(TextHitInfo paramTextHitInfo1, TextHitInfo paramTextHitInfo2, Rectangle2D paramRectangle2D)
/*      */   {
/* 2173 */     ensureCache();
/*      */ 
/* 2175 */     checkTextHit(paramTextHitInfo1);
/* 2176 */     checkTextHit(paramTextHitInfo2);
/*      */ 
/* 2178 */     if (paramRectangle2D == null) {
/* 2179 */       throw new IllegalArgumentException("Null Rectangle2D passed to TextLayout.getVisualHighlightShape()");
/*      */     }
/*      */ 
/* 2182 */     GeneralPath localGeneralPath = new GeneralPath(0);
/*      */ 
/* 2184 */     int i = hitToCaret(paramTextHitInfo1);
/* 2185 */     int j = hitToCaret(paramTextHitInfo2);
/*      */ 
/* 2187 */     localGeneralPath.append(caretBoundingShape(i, j, paramRectangle2D), false);
/*      */ 
/* 2190 */     if ((i == 0) || (j == 0)) {
/* 2191 */       localObject = leftShape(paramRectangle2D);
/* 2192 */       if (!((GeneralPath)localObject).getBounds().isEmpty()) {
/* 2193 */         localGeneralPath.append((Shape)localObject, false);
/*      */       }
/*      */     }
/* 2196 */     if ((i == this.characterCount) || (j == this.characterCount)) {
/* 2197 */       localObject = rightShape(paramRectangle2D);
/* 2198 */       if (!((GeneralPath)localObject).getBounds().isEmpty()) {
/* 2199 */         localGeneralPath.append((Shape)localObject, false);
/*      */       }
/*      */     }
/*      */ 
/* 2203 */     Object localObject = this.textLine.getLayoutPath();
/* 2204 */     if (localObject != null) {
/* 2205 */       localGeneralPath = (GeneralPath)((LayoutPathImpl)localObject).mapShape(localGeneralPath);
/*      */     }
/*      */ 
/* 2208 */     return localGeneralPath;
/*      */   }
/*      */ 
/*      */   public Shape getVisualHighlightShape(TextHitInfo paramTextHitInfo1, TextHitInfo paramTextHitInfo2)
/*      */   {
/* 2223 */     return getVisualHighlightShape(paramTextHitInfo1, paramTextHitInfo2, getNaturalBounds());
/*      */   }
/*      */ 
/*      */   public Shape getLogicalHighlightShape(int paramInt1, int paramInt2, Rectangle2D paramRectangle2D)
/*      */   {
/* 2268 */     if (paramRectangle2D == null) {
/* 2269 */       throw new IllegalArgumentException("Null Rectangle2D passed to TextLayout.getLogicalHighlightShape()");
/*      */     }
/*      */ 
/* 2272 */     ensureCache();
/*      */ 
/* 2274 */     if (paramInt1 > paramInt2) {
/* 2275 */       int i = paramInt1;
/* 2276 */       paramInt1 = paramInt2;
/* 2277 */       paramInt2 = i;
/*      */     }
/*      */ 
/* 2280 */     if ((paramInt1 < 0) || (paramInt2 > this.characterCount)) {
/* 2281 */       throw new IllegalArgumentException("Range is invalid in TextLayout.getLogicalHighlightShape()");
/*      */     }
/*      */ 
/* 2284 */     GeneralPath localGeneralPath = new GeneralPath(0);
/*      */ 
/* 2286 */     Object localObject1 = new int[10];
/* 2287 */     int j = 0;
/*      */ 
/* 2289 */     if (paramInt1 < paramInt2) {
/* 2290 */       k = paramInt1;
/*      */       do {
/* 2292 */         localObject1[(j++)] = hitToCaret(TextHitInfo.leading(k));
/* 2293 */         boolean bool = this.textLine.isCharLTR(k);
/*      */         do
/*      */         {
/* 2296 */           k++;
/* 2297 */         }while ((k < paramInt2) && (this.textLine.isCharLTR(k) == bool));
/*      */ 
/* 2299 */         int m = k;
/* 2300 */         localObject1[(j++)] = hitToCaret(TextHitInfo.trailing(m - 1));
/*      */ 
/* 2302 */         if (j == localObject1.length) {
/* 2303 */           int[] arrayOfInt = new int[localObject1.length + 10];
/* 2304 */           System.arraycopy(localObject1, 0, arrayOfInt, 0, j);
/* 2305 */           localObject1 = arrayOfInt;
/*      */         }
/*      */       }
/* 2307 */       while (k < paramInt2);
/*      */     }
/*      */     else {
/* 2310 */       j = 2;
/*      */       int tmp213_210 = hitToCaret(TextHitInfo.leading(paramInt1)); localObject1[1] = tmp213_210; localObject1[0] = tmp213_210;
/*      */     }
/*      */ 
/* 2316 */     for (int k = 0; k < j; k += 2) {
/* 2317 */       localGeneralPath.append(caretBoundingShape(localObject1[k], localObject1[(k + 1)], paramRectangle2D), false);
/*      */     }
/*      */ 
/* 2321 */     if (paramInt1 != paramInt2) {
/* 2322 */       if (((this.textLine.isDirectionLTR()) && (paramInt1 == 0)) || ((!this.textLine.isDirectionLTR()) && (paramInt2 == this.characterCount)))
/*      */       {
/* 2324 */         localObject2 = leftShape(paramRectangle2D);
/* 2325 */         if (!((GeneralPath)localObject2).getBounds().isEmpty()) {
/* 2326 */           localGeneralPath.append((Shape)localObject2, false);
/*      */         }
/*      */       }
/*      */ 
/* 2330 */       if (((this.textLine.isDirectionLTR()) && (paramInt2 == this.characterCount)) || ((!this.textLine.isDirectionLTR()) && (paramInt1 == 0)))
/*      */       {
/* 2333 */         localObject2 = rightShape(paramRectangle2D);
/* 2334 */         if (!((GeneralPath)localObject2).getBounds().isEmpty()) {
/* 2335 */           localGeneralPath.append((Shape)localObject2, false);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 2340 */     Object localObject2 = this.textLine.getLayoutPath();
/* 2341 */     if (localObject2 != null) {
/* 2342 */       localGeneralPath = (GeneralPath)((LayoutPathImpl)localObject2).mapShape(localGeneralPath);
/*      */     }
/* 2344 */     return localGeneralPath;
/*      */   }
/*      */ 
/*      */   public Shape getLogicalHighlightShape(int paramInt1, int paramInt2)
/*      */   {
/* 2363 */     return getLogicalHighlightShape(paramInt1, paramInt2, getNaturalBounds());
/*      */   }
/*      */ 
/*      */   public Shape getBlackBoxBounds(int paramInt1, int paramInt2)
/*      */   {
/* 2378 */     ensureCache();
/*      */ 
/* 2380 */     if (paramInt1 > paramInt2) {
/* 2381 */       int i = paramInt1;
/* 2382 */       paramInt1 = paramInt2;
/* 2383 */       paramInt2 = i;
/*      */     }
/*      */ 
/* 2386 */     if ((paramInt1 < 0) || (paramInt2 > this.characterCount)) {
/* 2387 */       throw new IllegalArgumentException("Invalid range passed to TextLayout.getBlackBoxBounds()");
/*      */     }
/*      */ 
/* 2395 */     GeneralPath localGeneralPath = new GeneralPath(1);
/*      */ 
/* 2397 */     if (paramInt1 < this.characterCount) {
/* 2398 */       for (int j = paramInt1; 
/* 2399 */         j < paramInt2; 
/* 2400 */         j++)
/*      */       {
/* 2402 */         Rectangle2D localRectangle2D = this.textLine.getCharBounds(j);
/* 2403 */         if (!localRectangle2D.isEmpty()) {
/* 2404 */           localGeneralPath.append(localRectangle2D, false);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 2409 */     if ((dx != 0.0F) || (dy != 0.0F)) {
/* 2410 */       localObject = AffineTransform.getTranslateInstance(dx, dy);
/* 2411 */       localGeneralPath = (GeneralPath)((AffineTransform)localObject).createTransformedShape(localGeneralPath);
/*      */     }
/* 2413 */     Object localObject = this.textLine.getLayoutPath();
/* 2414 */     if (localObject != null) {
/* 2415 */       localGeneralPath = (GeneralPath)((LayoutPathImpl)localObject).mapShape(localGeneralPath);
/*      */     }
/*      */ 
/* 2419 */     return localGeneralPath;
/*      */   }
/*      */ 
/*      */   private float caretToPointDistance(float[] paramArrayOfFloat, float paramFloat1, float paramFloat2)
/*      */   {
/* 2432 */     float f1 = this.isVerticalLine ? paramFloat2 : paramFloat1;
/* 2433 */     float f2 = this.isVerticalLine ? -paramFloat1 : paramFloat2;
/*      */ 
/* 2435 */     return f1 - paramArrayOfFloat[0] + f2 * paramArrayOfFloat[1];
/*      */   }
/*      */ 
/*      */   public TextHitInfo hitTestChar(float paramFloat1, float paramFloat2, Rectangle2D paramRectangle2D)
/*      */   {
/* 2459 */     LayoutPathImpl localLayoutPathImpl = this.textLine.getLayoutPath();
/* 2460 */     boolean bool = false;
/* 2461 */     if (localLayoutPathImpl != null) {
/* 2462 */       Point2D.Float localFloat = new Point2D.Float(paramFloat1, paramFloat2);
/* 2463 */       bool = localLayoutPathImpl.pointToPath(localFloat, localFloat);
/* 2464 */       paramFloat1 = localFloat.x;
/* 2465 */       paramFloat2 = localFloat.y;
/*      */     }
/*      */ 
/* 2468 */     if (isVertical()) {
/* 2469 */       if (paramFloat2 < paramRectangle2D.getMinY())
/* 2470 */         return TextHitInfo.leading(0);
/* 2471 */       if (paramFloat2 >= paramRectangle2D.getMaxY())
/* 2472 */         return TextHitInfo.trailing(this.characterCount - 1);
/*      */     }
/*      */     else {
/* 2475 */       if (paramFloat1 < paramRectangle2D.getMinX())
/* 2476 */         return isLeftToRight() ? TextHitInfo.leading(0) : TextHitInfo.trailing(this.characterCount - 1);
/* 2477 */       if (paramFloat1 >= paramRectangle2D.getMaxX()) {
/* 2478 */         return isLeftToRight() ? TextHitInfo.trailing(this.characterCount - 1) : TextHitInfo.leading(0);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2492 */     double d1 = 1.7976931348623157E+308D;
/* 2493 */     int i = 0;
/* 2494 */     int j = -1;
/* 2495 */     Object localObject = null;
/* 2496 */     float f1 = 0.0F; float f2 = 0.0F; float f3 = 0.0F; float f4 = 0.0F; float f5 = 0.0F; float f6 = 0.0F;
/*      */ 
/* 2498 */     for (int k = 0; k < this.characterCount; k++)
/* 2499 */       if (this.textLine.caretAtOffsetIsValid(k))
/*      */       {
/* 2502 */         if (j == -1) {
/* 2503 */           j = k;
/*      */         }
/* 2505 */         CoreMetrics localCoreMetrics = this.textLine.getCoreMetricsAt(k);
/* 2506 */         if (localCoreMetrics != localObject) {
/* 2507 */           localObject = localCoreMetrics;
/*      */ 
/* 2509 */           if (localCoreMetrics.baselineIndex == -1)
/* 2510 */             f4 = -(this.textLine.getMetrics().ascent - localCoreMetrics.ascent) + localCoreMetrics.ssOffset;
/* 2511 */           else if (localCoreMetrics.baselineIndex == -2)
/* 2512 */             f4 = this.textLine.getMetrics().descent - localCoreMetrics.descent + localCoreMetrics.ssOffset;
/*      */           else {
/* 2514 */             f4 = localCoreMetrics.effectiveBaselineOffset(this.baselineOffsets) + localCoreMetrics.ssOffset;
/*      */           }
/* 2516 */           f7 = (localCoreMetrics.descent - localCoreMetrics.ascent) / 2.0F - f4;
/* 2517 */           f5 = f7 * localCoreMetrics.italicAngle;
/* 2518 */           f4 += f7;
/* 2519 */           f6 = (f4 - paramFloat2) * (f4 - paramFloat2);
/*      */         }
/* 2521 */         float f7 = this.textLine.getCharXPosition(k);
/* 2522 */         float f8 = this.textLine.getCharAdvance(k);
/* 2523 */         float f9 = f8 / 2.0F;
/* 2524 */         f7 += f9 - f5;
/*      */ 
/* 2527 */         double d2 = Math.sqrt(4.0F * (f7 - paramFloat1) * (f7 - paramFloat1) + f6);
/* 2528 */         if (d2 < d1) {
/* 2529 */           d1 = d2;
/* 2530 */           i = k;
/* 2531 */           j = -1;
/* 2532 */           f1 = f7; f2 = f4; f3 = localCoreMetrics.italicAngle;
/*      */         }
/*      */       }
/* 2535 */     k = paramFloat1 < f1 - (paramFloat2 - f2) * f3 ? 1 : 0;
/* 2536 */     int m = this.textLine.isCharLTR(i) == k ? 1 : 0;
/* 2537 */     if (j == -1) {
/* 2538 */       j = this.characterCount;
/*      */     }
/* 2540 */     TextHitInfo localTextHitInfo = m != 0 ? TextHitInfo.leading(i) : TextHitInfo.trailing(j - 1);
/*      */ 
/* 2542 */     return localTextHitInfo;
/*      */   }
/*      */ 
/*      */   public TextHitInfo hitTestChar(float paramFloat1, float paramFloat2)
/*      */   {
/* 2559 */     return hitTestChar(paramFloat1, paramFloat2, getNaturalBounds());
/*      */   }
/*      */ 
/*      */   public int hashCode()
/*      */   {
/* 2567 */     if (this.hashCodeCache == 0) {
/* 2568 */       ensureCache();
/* 2569 */       this.hashCodeCache = this.textLine.hashCode();
/*      */     }
/* 2571 */     return this.hashCodeCache;
/*      */   }
/*      */ 
/*      */   public boolean equals(Object paramObject)
/*      */   {
/* 2584 */     return ((paramObject instanceof TextLayout)) && (equals((TextLayout)paramObject));
/*      */   }
/*      */ 
/*      */   public boolean equals(TextLayout paramTextLayout)
/*      */   {
/* 2598 */     if (paramTextLayout == null) {
/* 2599 */       return false;
/*      */     }
/* 2601 */     if (paramTextLayout == this) {
/* 2602 */       return true;
/*      */     }
/*      */ 
/* 2605 */     ensureCache();
/* 2606 */     return this.textLine.equals(paramTextLayout.textLine);
/*      */   }
/*      */ 
/*      */   public String toString()
/*      */   {
/* 2615 */     ensureCache();
/* 2616 */     return this.textLine.toString();
/*      */   }
/*      */ 
/*      */   public void draw(Graphics2D paramGraphics2D, float paramFloat1, float paramFloat2)
/*      */   {
/* 2634 */     if (paramGraphics2D == null) {
/* 2635 */       throw new IllegalArgumentException("Null Graphics2D passed to TextLayout.draw()");
/*      */     }
/*      */ 
/* 2638 */     this.textLine.draw(paramGraphics2D, paramFloat1 - dx, paramFloat2 - dy);
/*      */   }
/*      */ 
/*      */   TextLine getTextLineForTesting()
/*      */   {
/* 2646 */     return this.textLine;
/*      */   }
/*      */ 
/*      */   private static int sameBaselineUpTo(Font paramFont, char[] paramArrayOfChar, int paramInt1, int paramInt2)
/*      */   {
/* 2658 */     return paramInt2;
/*      */   }
/*      */ 
/*      */   static byte getBaselineFromGraphic(GraphicAttribute paramGraphicAttribute)
/*      */   {
/* 2670 */     byte b = (byte)paramGraphicAttribute.getAlignment();
/*      */ 
/* 2672 */     if ((b == -2) || (b == -1))
/*      */     {
/* 2675 */       return 0;
/*      */     }
/*      */ 
/* 2678 */     return b;
/*      */   }
/*      */ 
/*      */   public Shape getOutline(AffineTransform paramAffineTransform)
/*      */   {
/* 2691 */     ensureCache();
/* 2692 */     Shape localShape = this.textLine.getOutline(paramAffineTransform);
/* 2693 */     LayoutPathImpl localLayoutPathImpl = this.textLine.getLayoutPath();
/* 2694 */     if (localLayoutPathImpl != null) {
/* 2695 */       localShape = localLayoutPathImpl.mapShape(localShape);
/*      */     }
/* 2697 */     return localShape;
/*      */   }
/*      */ 
/*      */   public LayoutPath getLayoutPath()
/*      */   {
/* 2707 */     return this.textLine.getLayoutPath();
/*      */   }
/*      */ 
/*      */   public void hitToPoint(TextHitInfo paramTextHitInfo, Point2D paramPoint2D)
/*      */   {
/* 2726 */     if ((paramTextHitInfo == null) || (paramPoint2D == null)) {
/* 2727 */       throw new NullPointerException((paramTextHitInfo == null ? "hit" : "point") + " can't be null");
/*      */     }
/*      */ 
/* 2730 */     ensureCache();
/* 2731 */     checkTextHit(paramTextHitInfo);
/*      */ 
/* 2733 */     float f1 = 0.0F;
/* 2734 */     float f2 = 0.0F;
/*      */ 
/* 2736 */     int i = paramTextHitInfo.getCharIndex();
/* 2737 */     boolean bool1 = paramTextHitInfo.isLeadingEdge();
/*      */     boolean bool2;
/* 2739 */     if ((i == -1) || (i == this.textLine.characterCount())) {
/* 2740 */       bool2 = this.textLine.isDirectionLTR();
/* 2741 */       f1 = bool2 == (i == -1) ? 0.0F : this.lineMetrics.advance;
/*      */     } else {
/* 2743 */       bool2 = this.textLine.isCharLTR(i);
/* 2744 */       f1 = this.textLine.getCharLinePosition(i, bool1);
/* 2745 */       f2 = this.textLine.getCharYPosition(i);
/*      */     }
/* 2747 */     paramPoint2D.setLocation(f1, f2);
/* 2748 */     LayoutPathImpl localLayoutPathImpl = this.textLine.getLayoutPath();
/* 2749 */     if (localLayoutPathImpl != null)
/* 2750 */       localLayoutPathImpl.pathToPoint(paramPoint2D, bool2 != bool1, paramPoint2D);
/*      */   }
/*      */ 
/*      */   public static class CaretPolicy
/*      */   {
/*      */     public TextHitInfo getStrongCaret(TextHitInfo paramTextHitInfo1, TextHitInfo paramTextHitInfo2, TextLayout paramTextLayout)
/*      */     {
/*  340 */       return paramTextLayout.getStrongHit(paramTextHitInfo1, paramTextHitInfo2);
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.font.TextLayout
 * JD-Core Version:    0.6.2
 */