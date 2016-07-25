/*     */ package java.awt.font;
/*     */ 
/*     */ import java.awt.Font;
/*     */ import java.io.PrintStream;
/*     */ import java.text.AttributedCharacterIterator;
/*     */ import java.text.Bidi;
/*     */ import java.text.BreakIterator;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Map;
/*     */ import sun.font.AttributeValues;
/*     */ import sun.font.BidiUtils;
/*     */ import sun.font.TextLabelFactory;
/*     */ import sun.font.TextLineComponent;
/*     */ 
/*     */ public final class TextMeasurer
/*     */   implements Cloneable
/*     */ {
/* 100 */   private static float EST_LINES = 2.1F;
/*     */   private FontRenderContext fFrc;
/*     */   private int fStart;
/*     */   private char[] fChars;
/*     */   private Bidi fBidi;
/*     */   private byte[] fLevels;
/*     */   private TextLineComponent[] fComponents;
/*     */   private int fComponentStart;
/*     */   private int fComponentLimit;
/*     */   private boolean haveLayoutWindow;
/* 143 */   private BreakIterator fLineBreak = null;
/* 144 */   private CharArrayIterator charIter = null;
/* 145 */   int layoutCount = 0;
/* 146 */   int layoutCharCount = 0;
/*     */   private StyledParagraph fParagraph;
/*     */   private boolean fIsDirectionLTR;
/*     */   private byte fBaseline;
/*     */   private float[] fBaselineOffsets;
/* 155 */   private float fJustifyRatio = 1.0F;
/*     */ 
/* 624 */   private int formattedChars = 0;
/* 625 */   private static boolean wantStats = false;
/* 626 */   private boolean collectStats = false;
/*     */ 
/*     */   public TextMeasurer(AttributedCharacterIterator paramAttributedCharacterIterator, FontRenderContext paramFontRenderContext)
/*     */   {
/* 166 */     this.fFrc = paramFontRenderContext;
/* 167 */     initAll(paramAttributedCharacterIterator);
/*     */   }
/*     */ 
/*     */   protected Object clone() {
/*     */     TextMeasurer localTextMeasurer;
/*     */     try {
/* 173 */       localTextMeasurer = (TextMeasurer)super.clone();
/*     */     }
/*     */     catch (CloneNotSupportedException localCloneNotSupportedException) {
/* 176 */       throw new Error();
/*     */     }
/* 178 */     if (this.fComponents != null) {
/* 179 */       localTextMeasurer.fComponents = ((TextLineComponent[])this.fComponents.clone());
/*     */     }
/* 181 */     return localTextMeasurer;
/*     */   }
/*     */ 
/*     */   private void invalidateComponents() {
/* 185 */     this.fComponentStart = (this.fComponentLimit = this.fChars.length);
/* 186 */     this.fComponents = null;
/* 187 */     this.haveLayoutWindow = false;
/*     */   }
/*     */ 
/*     */   private void initAll(AttributedCharacterIterator paramAttributedCharacterIterator)
/*     */   {
/* 196 */     this.fStart = paramAttributedCharacterIterator.getBeginIndex();
/*     */ 
/* 199 */     this.fChars = new char[paramAttributedCharacterIterator.getEndIndex() - this.fStart];
/*     */ 
/* 201 */     int i = 0;
/* 202 */     for (int j = paramAttributedCharacterIterator.first(); j != 65535; j = paramAttributedCharacterIterator.next()) {
/* 203 */       this.fChars[(i++)] = j;
/*     */     }
/*     */ 
/* 206 */     paramAttributedCharacterIterator.first();
/*     */ 
/* 208 */     this.fBidi = new Bidi(paramAttributedCharacterIterator);
/* 209 */     if (this.fBidi.isLeftToRight()) {
/* 210 */       this.fBidi = null;
/*     */     }
/*     */ 
/* 213 */     paramAttributedCharacterIterator.first();
/* 214 */     Map localMap = paramAttributedCharacterIterator.getAttributes();
/* 215 */     NumericShaper localNumericShaper = AttributeValues.getNumericShaping(localMap);
/* 216 */     if (localNumericShaper != null) {
/* 217 */       localNumericShaper.shape(this.fChars, 0, this.fChars.length);
/*     */     }
/*     */ 
/* 220 */     this.fParagraph = new StyledParagraph(paramAttributedCharacterIterator, this.fChars);
/*     */ 
/* 228 */     this.fJustifyRatio = AttributeValues.getJustification(localMap);
/*     */ 
/* 230 */     boolean bool = TextLine.advanceToFirstFont(paramAttributedCharacterIterator);
/*     */     Object localObject;
/*     */     LineMetrics localLineMetrics;
/* 232 */     if (bool) {
/* 233 */       localObject = TextLine.getFontAtCurrentPos(paramAttributedCharacterIterator);
/* 234 */       int k = paramAttributedCharacterIterator.getIndex() - paramAttributedCharacterIterator.getBeginIndex();
/* 235 */       localLineMetrics = ((Font)localObject).getLineMetrics(this.fChars, k, k + 1, this.fFrc);
/* 236 */       this.fBaseline = ((byte)localLineMetrics.getBaselineIndex());
/* 237 */       this.fBaselineOffsets = localLineMetrics.getBaselineOffsets();
/*     */     }
/*     */     else
/*     */     {
/* 243 */       localObject = (GraphicAttribute)localMap.get(TextAttribute.CHAR_REPLACEMENT);
/*     */ 
/* 245 */       this.fBaseline = TextLayout.getBaselineFromGraphic((GraphicAttribute)localObject);
/* 246 */       Font localFont = new Font(new Hashtable(5, 0.9F));
/* 247 */       localLineMetrics = localFont.getLineMetrics(" ", 0, 1, this.fFrc);
/* 248 */       this.fBaselineOffsets = localLineMetrics.getBaselineOffsets();
/*     */     }
/* 250 */     this.fBaselineOffsets = TextLine.getNormalizedOffsets(this.fBaselineOffsets, this.fBaseline);
/*     */ 
/* 253 */     invalidateComponents();
/*     */   }
/*     */ 
/*     */   private void generateComponents(int paramInt1, int paramInt2)
/*     */   {
/* 262 */     if (this.collectStats) {
/* 263 */       this.formattedChars += paramInt2 - paramInt1;
/*     */     }
/* 265 */     int i = 0;
/* 266 */     TextLabelFactory localTextLabelFactory = new TextLabelFactory(this.fFrc, this.fChars, this.fBidi, i);
/*     */ 
/* 268 */     int[] arrayOfInt1 = null;
/*     */ 
/* 270 */     if (this.fBidi != null) {
/* 271 */       this.fLevels = BidiUtils.getLevels(this.fBidi);
/* 272 */       int[] arrayOfInt2 = BidiUtils.createVisualToLogicalMap(this.fLevels);
/* 273 */       arrayOfInt1 = BidiUtils.createInverseMap(arrayOfInt2);
/* 274 */       this.fIsDirectionLTR = this.fBidi.baseIsLeftToRight();
/*     */     }
/*     */     else {
/* 277 */       this.fLevels = null;
/* 278 */       this.fIsDirectionLTR = true;
/*     */     }
/*     */     try
/*     */     {
/* 282 */       this.fComponents = TextLine.getComponents(this.fParagraph, this.fChars, paramInt1, paramInt2, arrayOfInt1, this.fLevels, localTextLabelFactory);
/*     */     }
/*     */     catch (IllegalArgumentException localIllegalArgumentException)
/*     */     {
/* 286 */       System.out.println("startingAt=" + paramInt1 + "; endingAt=" + paramInt2);
/* 287 */       System.out.println("fComponentLimit=" + this.fComponentLimit);
/* 288 */       throw localIllegalArgumentException;
/*     */     }
/*     */ 
/* 291 */     this.fComponentStart = paramInt1;
/* 292 */     this.fComponentLimit = paramInt2;
/*     */   }
/*     */ 
/*     */   private int calcLineBreak(int paramInt, float paramFloat)
/*     */   {
/* 302 */     int i = paramInt;
/* 303 */     float f = paramFloat;
/*     */ 
/* 306 */     int k = this.fComponentStart;
/*     */ 
/* 308 */     for (int j = 0; j < this.fComponents.length; j++) {
/* 309 */       int m = k + this.fComponents[j].getNumCharacters();
/* 310 */       if (m > i)
/*     */       {
/*     */         break;
/*     */       }
/* 314 */       k = m;
/*     */     }
/*     */ 
/* 320 */     for (; j < this.fComponents.length; j++)
/*     */     {
/* 322 */       TextLineComponent localTextLineComponent = this.fComponents[j];
/* 323 */       int n = localTextLineComponent.getNumCharacters();
/*     */ 
/* 325 */       int i1 = localTextLineComponent.getLineBreakIndex(i - k, f);
/* 326 */       if ((i1 == n) && (j < this.fComponents.length)) {
/* 327 */         f -= localTextLineComponent.getAdvanceBetween(i - k, i1);
/* 328 */         k += n;
/* 329 */         i = k;
/*     */       }
/*     */       else {
/* 332 */         return k + i1;
/*     */       }
/*     */     }
/*     */ 
/* 336 */     if (this.fComponentLimit < this.fChars.length)
/*     */     {
/* 342 */       generateComponents(paramInt, this.fChars.length);
/* 343 */       return calcLineBreak(paramInt, paramFloat);
/*     */     }
/*     */ 
/* 346 */     return this.fChars.length;
/*     */   }
/*     */ 
/*     */   private int trailingCdWhitespaceStart(int paramInt1, int paramInt2)
/*     */   {
/* 360 */     if (this.fLevels != null)
/*     */     {
/* 362 */       int i = (byte)(this.fIsDirectionLTR ? 0 : 1);
/* 363 */       int j = paramInt2;
/*     */       do { j--; if (j < paramInt1) break; }
/* 364 */       while ((this.fLevels[j] % 2 != i) && (Character.getDirectionality(this.fChars[j]) == 12));
/*     */ 
/* 366 */       j++; return j;
/*     */     }
/*     */ 
/* 371 */     return paramInt1;
/*     */   }
/*     */ 
/*     */   private TextLineComponent[] makeComponentsOnRange(int paramInt1, int paramInt2)
/*     */   {
/* 381 */     int i = trailingCdWhitespaceStart(paramInt1, paramInt2);
/*     */ 
/* 384 */     int k = this.fComponentStart;
/*     */ 
/* 386 */     for (int j = 0; j < this.fComponents.length; j++) {
/* 387 */       m = k + this.fComponents[j].getNumCharacters();
/* 388 */       if (m > paramInt1)
/*     */       {
/*     */         break;
/*     */       }
/* 392 */       k = m;
/*     */     }
/*     */ 
/* 400 */     int n = 0;
/* 401 */     int i1 = k;
/* 402 */     int i2 = j;
/*     */     int i4;
/* 403 */     for (int i3 = 1; i3 != 0; i2++) {
/* 404 */       i4 = i1 + this.fComponents[i2].getNumCharacters();
/* 405 */       if ((i > Math.max(i1, paramInt1)) && (i < Math.min(i4, paramInt2)))
/*     */       {
/* 407 */         n = 1;
/*     */       }
/* 409 */       if (i4 >= paramInt2) {
/* 410 */         i3 = 0;
/*     */       }
/*     */       else {
/* 413 */         i1 = i4;
/*     */       }
/*     */     }
/* 416 */     int m = i2 - j;
/* 417 */     if (n != 0) {
/* 418 */       m++;
/*     */     }
/*     */ 
/* 422 */     TextLineComponent[] arrayOfTextLineComponent = new TextLineComponent[m];
/* 423 */     i1 = 0;
/* 424 */     i2 = paramInt1;
/*     */ 
/* 426 */     i3 = i;
/*     */ 
/* 429 */     if (i3 == paramInt1) {
/* 430 */       i4 = this.fIsDirectionLTR ? 0 : 1;
/*     */ 
/* 432 */       i3 = paramInt2;
/*     */     }
/*     */     else {
/* 435 */       i4 = 2;
/*     */     }
/*     */ 
/* 438 */     while (i2 < paramInt2)
/*     */     {
/* 440 */       int i5 = this.fComponents[j].getNumCharacters();
/* 441 */       int i6 = k + i5;
/*     */ 
/* 443 */       int i7 = Math.max(i2, k);
/* 444 */       int i8 = Math.min(i3, i6);
/*     */ 
/* 446 */       arrayOfTextLineComponent[(i1++)] = this.fComponents[j].getSubset(i7 - k, i8 - k, i4);
/*     */ 
/* 450 */       i2 += i8 - i7;
/* 451 */       if (i2 == i3) {
/* 452 */         i3 = paramInt2;
/* 453 */         i4 = this.fIsDirectionLTR ? 0 : 1;
/*     */       }
/*     */ 
/* 456 */       if (i2 == i6) {
/* 457 */         j++;
/* 458 */         k = i6;
/*     */       }
/*     */     }
/*     */ 
/* 462 */     return arrayOfTextLineComponent;
/*     */   }
/*     */ 
/*     */   private TextLine makeTextLineOnRange(int paramInt1, int paramInt2)
/*     */   {
/* 467 */     int[] arrayOfInt1 = null;
/* 468 */     byte[] arrayOfByte = null;
/*     */ 
/* 470 */     if (this.fBidi != null) {
/* 471 */       localObject = this.fBidi.createLineBidi(paramInt1, paramInt2);
/* 472 */       arrayOfByte = BidiUtils.getLevels((Bidi)localObject);
/* 473 */       int[] arrayOfInt2 = BidiUtils.createVisualToLogicalMap(arrayOfByte);
/* 474 */       arrayOfInt1 = BidiUtils.createInverseMap(arrayOfInt2);
/*     */     }
/*     */ 
/* 477 */     Object localObject = makeComponentsOnRange(paramInt1, paramInt2);
/*     */ 
/* 479 */     return new TextLine(this.fFrc, (TextLineComponent[])localObject, this.fBaselineOffsets, this.fChars, paramInt1, paramInt2, arrayOfInt1, arrayOfByte, this.fIsDirectionLTR);
/*     */   }
/*     */ 
/*     */   private void ensureComponents(int paramInt1, int paramInt2)
/*     */   {
/* 493 */     if ((paramInt1 < this.fComponentStart) || (paramInt2 > this.fComponentLimit))
/* 494 */       generateComponents(paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   private void makeLayoutWindow(int paramInt)
/*     */   {
/* 500 */     int i = paramInt;
/* 501 */     int j = this.fChars.length;
/*     */ 
/* 504 */     if ((this.layoutCount > 0) && (!this.haveLayoutWindow)) {
/* 505 */       float f = Math.max(this.layoutCharCount / this.layoutCount, 1);
/* 506 */       j = Math.min(paramInt + (int)(f * EST_LINES), this.fChars.length);
/*     */     }
/*     */ 
/* 509 */     if ((paramInt > 0) || (j < this.fChars.length)) {
/* 510 */       if (this.charIter == null) {
/* 511 */         this.charIter = new CharArrayIterator(this.fChars);
/*     */       }
/*     */       else {
/* 514 */         this.charIter.reset(this.fChars);
/*     */       }
/* 516 */       if (this.fLineBreak == null) {
/* 517 */         this.fLineBreak = BreakIterator.getLineInstance();
/*     */       }
/* 519 */       this.fLineBreak.setText(this.charIter);
/* 520 */       if ((paramInt > 0) && 
/* 521 */         (!this.fLineBreak.isBoundary(paramInt))) {
/* 522 */         i = this.fLineBreak.preceding(paramInt);
/*     */       }
/*     */ 
/* 525 */       if ((j < this.fChars.length) && 
/* 526 */         (!this.fLineBreak.isBoundary(j))) {
/* 527 */         j = this.fLineBreak.following(j);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 532 */     ensureComponents(i, j);
/* 533 */     this.haveLayoutWindow = true;
/*     */   }
/*     */ 
/*     */   public int getLineBreakIndex(int paramInt, float paramFloat)
/*     */   {
/* 553 */     int i = paramInt - this.fStart;
/*     */ 
/* 555 */     if ((!this.haveLayoutWindow) || (i < this.fComponentStart) || (i >= this.fComponentLimit))
/*     */     {
/* 558 */       makeLayoutWindow(i);
/*     */     }
/*     */ 
/* 561 */     return calcLineBreak(i, paramFloat) + this.fStart;
/*     */   }
/*     */ 
/*     */   public float getAdvanceBetween(int paramInt1, int paramInt2)
/*     */   {
/* 582 */     int i = paramInt1 - this.fStart;
/* 583 */     int j = paramInt2 - this.fStart;
/*     */ 
/* 585 */     ensureComponents(i, j);
/* 586 */     TextLine localTextLine = makeTextLineOnRange(i, j);
/* 587 */     return localTextLine.getMetrics().advance;
/*     */   }
/*     */ 
/*     */   public TextLayout getLayout(int paramInt1, int paramInt2)
/*     */   {
/* 607 */     int i = paramInt1 - this.fStart;
/* 608 */     int j = paramInt2 - this.fStart;
/*     */ 
/* 610 */     ensureComponents(i, j);
/* 611 */     TextLine localTextLine = makeTextLineOnRange(i, j);
/*     */ 
/* 613 */     if (j < this.fChars.length) {
/* 614 */       this.layoutCharCount += paramInt2 - paramInt1;
/* 615 */       this.layoutCount += 1;
/*     */     }
/*     */ 
/* 618 */     return new TextLayout(localTextLine, this.fBaseline, this.fBaselineOffsets, this.fJustifyRatio);
/*     */   }
/*     */ 
/*     */   private void printStats()
/*     */   {
/* 629 */     System.out.println("formattedChars: " + this.formattedChars);
/*     */ 
/* 631 */     this.collectStats = false;
/*     */   }
/*     */ 
/*     */   public void insertChar(AttributedCharacterIterator paramAttributedCharacterIterator, int paramInt)
/*     */   {
/* 658 */     if (this.collectStats) {
/* 659 */       printStats();
/*     */     }
/* 661 */     if (wantStats) {
/* 662 */       this.collectStats = true;
/*     */     }
/*     */ 
/* 665 */     this.fStart = paramAttributedCharacterIterator.getBeginIndex();
/* 666 */     int i = paramAttributedCharacterIterator.getEndIndex();
/* 667 */     if (i - this.fStart != this.fChars.length + 1) {
/* 668 */       initAll(paramAttributedCharacterIterator);
/*     */     }
/*     */ 
/* 671 */     char[] arrayOfChar = new char[i - this.fStart];
/* 672 */     int j = paramInt - this.fStart;
/* 673 */     System.arraycopy(this.fChars, 0, arrayOfChar, 0, j);
/*     */ 
/* 675 */     int k = paramAttributedCharacterIterator.setIndex(paramInt);
/* 676 */     arrayOfChar[j] = k;
/* 677 */     System.arraycopy(this.fChars, j, arrayOfChar, j + 1, i - paramInt - 1);
/*     */ 
/* 682 */     this.fChars = arrayOfChar;
/*     */ 
/* 684 */     if ((this.fBidi != null) || (Bidi.requiresBidi(arrayOfChar, j, j + 1)) || (paramAttributedCharacterIterator.getAttribute(TextAttribute.BIDI_EMBEDDING) != null))
/*     */     {
/* 687 */       this.fBidi = new Bidi(paramAttributedCharacterIterator);
/* 688 */       if (this.fBidi.isLeftToRight()) {
/* 689 */         this.fBidi = null;
/*     */       }
/*     */     }
/*     */ 
/* 693 */     this.fParagraph = StyledParagraph.insertChar(paramAttributedCharacterIterator, this.fChars, paramInt, this.fParagraph);
/*     */ 
/* 697 */     invalidateComponents();
/*     */   }
/*     */ 
/*     */   public void deleteChar(AttributedCharacterIterator paramAttributedCharacterIterator, int paramInt)
/*     */   {
/* 724 */     this.fStart = paramAttributedCharacterIterator.getBeginIndex();
/* 725 */     int i = paramAttributedCharacterIterator.getEndIndex();
/* 726 */     if (i - this.fStart != this.fChars.length - 1) {
/* 727 */       initAll(paramAttributedCharacterIterator);
/*     */     }
/*     */ 
/* 730 */     char[] arrayOfChar = new char[i - this.fStart];
/* 731 */     int j = paramInt - this.fStart;
/*     */ 
/* 733 */     System.arraycopy(this.fChars, 0, arrayOfChar, 0, paramInt - this.fStart);
/* 734 */     System.arraycopy(this.fChars, j + 1, arrayOfChar, j, i - paramInt);
/* 735 */     this.fChars = arrayOfChar;
/*     */ 
/* 737 */     if (this.fBidi != null) {
/* 738 */       this.fBidi = new Bidi(paramAttributedCharacterIterator);
/* 739 */       if (this.fBidi.isLeftToRight()) {
/* 740 */         this.fBidi = null;
/*     */       }
/*     */     }
/*     */ 
/* 744 */     this.fParagraph = StyledParagraph.deleteChar(paramAttributedCharacterIterator, this.fChars, paramInt, this.fParagraph);
/*     */ 
/* 748 */     invalidateComponents();
/*     */   }
/*     */ 
/*     */   char[] getChars()
/*     */   {
/* 757 */     return this.fChars;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.font.TextMeasurer
 * JD-Core Version:    0.6.2
 */