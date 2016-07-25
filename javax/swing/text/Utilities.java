/*      */ package javax.swing.text;
/*      */ 
/*      */ import java.awt.Component;
/*      */ import java.awt.ComponentOrientation;
/*      */ import java.awt.Container;
/*      */ import java.awt.FontMetrics;
/*      */ import java.awt.Graphics;
/*      */ import java.awt.Graphics2D;
/*      */ import java.awt.Rectangle;
/*      */ import java.awt.Shape;
/*      */ import java.awt.font.TextAttribute;
/*      */ import java.text.AttributedCharacterIterator;
/*      */ import java.text.AttributedString;
/*      */ import java.text.BreakIterator;
/*      */ import javax.swing.JComponent;
/*      */ import sun.swing.SwingUtilities2;
/*      */ 
/*      */ public class Utilities
/*      */ {
/*      */   static JComponent getJComponent(View paramView)
/*      */   {
/*   58 */     if (paramView != null) {
/*   59 */       Container localContainer = paramView.getContainer();
/*   60 */       if ((localContainer instanceof JComponent)) {
/*   61 */         return (JComponent)localContainer;
/*      */       }
/*      */     }
/*   64 */     return null;
/*      */   }
/*      */ 
/*      */   public static final int drawTabbedText(Segment paramSegment, int paramInt1, int paramInt2, Graphics paramGraphics, TabExpander paramTabExpander, int paramInt3)
/*      */   {
/*   84 */     return drawTabbedText(null, paramSegment, paramInt1, paramInt2, paramGraphics, paramTabExpander, paramInt3);
/*      */   }
/*      */ 
/*      */   static final int drawTabbedText(View paramView, Segment paramSegment, int paramInt1, int paramInt2, Graphics paramGraphics, TabExpander paramTabExpander, int paramInt3)
/*      */   {
/*  106 */     return drawTabbedText(paramView, paramSegment, paramInt1, paramInt2, paramGraphics, paramTabExpander, paramInt3, null);
/*      */   }
/*      */ 
/*      */   static final int drawTabbedText(View paramView, Segment paramSegment, int paramInt1, int paramInt2, Graphics paramGraphics, TabExpander paramTabExpander, int paramInt3, int[] paramArrayOfInt)
/*      */   {
/*  120 */     JComponent localJComponent = getJComponent(paramView);
/*  121 */     FontMetrics localFontMetrics = SwingUtilities2.getFontMetrics(localJComponent, paramGraphics);
/*  122 */     int i = paramInt1;
/*  123 */     char[] arrayOfChar = paramSegment.array;
/*  124 */     View localView1 = paramSegment.offset;
/*  125 */     int j = 0;
/*  126 */     int k = paramSegment.offset;
/*  127 */     int m = 0;
/*  128 */     View localView2 = -1;
/*  129 */     View localView3 = 0;
/*  130 */     View localView4 = 0;
/*  131 */     if (paramArrayOfInt != null) {
/*  132 */       localView5 = -paramInt3 + localView1;
/*  133 */       localView6 = null;
/*  134 */       if ((paramView != null) && ((localView6 = paramView.getParent()) != null))
/*      */       {
/*  136 */         localView5 += localView6.getStartOffset();
/*      */       }
/*  138 */       m = paramArrayOfInt[0];
/*      */ 
/*  140 */       localView2 = paramArrayOfInt[1] + localView5;
/*      */ 
/*  142 */       localView3 = paramArrayOfInt[2] + localView5;
/*      */ 
/*  144 */       localView4 = paramArrayOfInt[3] + localView5;
/*      */     }
/*      */ 
/*  147 */     View localView5 = paramSegment.offset + paramSegment.count;
/*  148 */     for (View localView6 = localView1; localView6 < localView5; localView6++) {
/*  149 */       if ((arrayOfChar[localView6] == '\t') || (((m != 0) || (localView6 <= localView2)) && (arrayOfChar[localView6] == ' ') && (localView3 <= localView6) && (localView6 <= localView4)))
/*      */       {
/*  155 */         if (j > 0) {
/*  156 */           i = SwingUtilities2.drawChars(localJComponent, paramGraphics, arrayOfChar, k, j, paramInt1, paramInt2);
/*      */ 
/*  158 */           j = 0;
/*      */         }
/*  160 */         k = localView6 + 1;
/*  161 */         if (arrayOfChar[localView6] == '\t') {
/*  162 */           if (paramTabExpander != null)
/*  163 */             i = (int)paramTabExpander.nextTabStop(i, paramInt3 + localView6 - localView1);
/*      */           else
/*  165 */             i += localFontMetrics.charWidth(' ');
/*      */         }
/*  167 */         else if (arrayOfChar[localView6] == ' ') {
/*  168 */           i += localFontMetrics.charWidth(' ') + m;
/*  169 */           if (localView6 <= localView2) {
/*  170 */             i++;
/*      */           }
/*      */         }
/*  173 */         paramInt1 = i;
/*  174 */       } else if ((arrayOfChar[localView6] == '\n') || (arrayOfChar[localView6] == '\r')) {
/*  175 */         if (j > 0) {
/*  176 */           i = SwingUtilities2.drawChars(localJComponent, paramGraphics, arrayOfChar, k, j, paramInt1, paramInt2);
/*      */ 
/*  178 */           j = 0;
/*      */         }
/*  180 */         k = localView6 + 1;
/*  181 */         paramInt1 = i;
/*      */       } else {
/*  183 */         j++;
/*      */       }
/*      */     }
/*  186 */     if (j > 0) {
/*  187 */       i = SwingUtilities2.drawChars(localJComponent, paramGraphics, arrayOfChar, k, j, paramInt1, paramInt2);
/*      */     }
/*      */ 
/*  190 */     return i;
/*      */   }
/*      */ 
/*      */   public static final int getTabbedTextWidth(Segment paramSegment, FontMetrics paramFontMetrics, int paramInt1, TabExpander paramTabExpander, int paramInt2)
/*      */   {
/*  208 */     return getTabbedTextWidth(null, paramSegment, paramFontMetrics, paramInt1, paramTabExpander, paramInt2, null);
/*      */   }
/*      */ 
/*      */   static final int getTabbedTextWidth(View paramView, Segment paramSegment, FontMetrics paramFontMetrics, int paramInt1, TabExpander paramTabExpander, int paramInt2, int[] paramArrayOfInt)
/*      */   {
/*  222 */     int i = paramInt1;
/*  223 */     char[] arrayOfChar = paramSegment.array;
/*  224 */     int j = paramSegment.offset;
/*  225 */     int k = paramSegment.offset + paramSegment.count;
/*  226 */     int m = 0;
/*  227 */     int n = 0;
/*  228 */     int i1 = -1;
/*  229 */     int i2 = 0;
/*  230 */     int i3 = 0;
/*  231 */     if (paramArrayOfInt != null) {
/*  232 */       i4 = -paramInt2 + j;
/*  233 */       View localView = null;
/*  234 */       if ((paramView != null) && ((localView = paramView.getParent()) != null))
/*      */       {
/*  236 */         i4 += localView.getStartOffset();
/*      */       }
/*  238 */       n = paramArrayOfInt[0];
/*      */ 
/*  240 */       i1 = paramArrayOfInt[1] + i4;
/*      */ 
/*  242 */       i2 = paramArrayOfInt[2] + i4;
/*      */ 
/*  244 */       i3 = paramArrayOfInt[3] + i4;
/*      */     }
/*      */ 
/*  248 */     for (int i4 = j; i4 < k; i4++) {
/*  249 */       if ((arrayOfChar[i4] == '\t') || (((n != 0) || (i4 <= i1)) && (arrayOfChar[i4] == ' ') && (i2 <= i4) && (i4 <= i3)))
/*      */       {
/*  255 */         i += paramFontMetrics.charsWidth(arrayOfChar, i4 - m, m);
/*  256 */         m = 0;
/*  257 */         if (arrayOfChar[i4] == '\t') {
/*  258 */           if (paramTabExpander != null) {
/*  259 */             i = (int)paramTabExpander.nextTabStop(i, paramInt2 + i4 - j);
/*      */           }
/*      */           else
/*  262 */             i += paramFontMetrics.charWidth(' ');
/*      */         }
/*  264 */         else if (arrayOfChar[i4] == ' ') {
/*  265 */           i += paramFontMetrics.charWidth(' ') + n;
/*  266 */           if (i4 <= i1)
/*  267 */             i++;
/*      */         }
/*      */       }
/*  270 */       else if (arrayOfChar[i4] == '\n')
/*      */       {
/*  273 */         i += paramFontMetrics.charsWidth(arrayOfChar, i4 - m, m);
/*  274 */         m = 0;
/*      */       } else {
/*  276 */         m++;
/*      */       }
/*      */     }
/*  279 */     i += paramFontMetrics.charsWidth(arrayOfChar, k - m, m);
/*  280 */     return i - paramInt1;
/*      */   }
/*      */ 
/*      */   public static final int getTabbedTextOffset(Segment paramSegment, FontMetrics paramFontMetrics, int paramInt1, int paramInt2, TabExpander paramTabExpander, int paramInt3)
/*      */   {
/*  303 */     return getTabbedTextOffset(paramSegment, paramFontMetrics, paramInt1, paramInt2, paramTabExpander, paramInt3, true);
/*      */   }
/*      */ 
/*      */   static final int getTabbedTextOffset(View paramView, Segment paramSegment, FontMetrics paramFontMetrics, int paramInt1, int paramInt2, TabExpander paramTabExpander, int paramInt3, int[] paramArrayOfInt)
/*      */   {
/*  310 */     return getTabbedTextOffset(paramView, paramSegment, paramFontMetrics, paramInt1, paramInt2, paramTabExpander, paramInt3, true, paramArrayOfInt);
/*      */   }
/*      */ 
/*      */   public static final int getTabbedTextOffset(Segment paramSegment, FontMetrics paramFontMetrics, int paramInt1, int paramInt2, TabExpander paramTabExpander, int paramInt3, boolean paramBoolean)
/*      */   {
/*  319 */     return getTabbedTextOffset(null, paramSegment, paramFontMetrics, paramInt1, paramInt2, paramTabExpander, paramInt3, paramBoolean, null);
/*      */   }
/*      */ 
/*      */   static final int getTabbedTextOffset(View paramView, Segment paramSegment, FontMetrics paramFontMetrics, int paramInt1, int paramInt2, TabExpander paramTabExpander, int paramInt3, boolean paramBoolean, int[] paramArrayOfInt)
/*      */   {
/*  336 */     if (paramInt1 >= paramInt2)
/*      */     {
/*  338 */       return 0;
/*      */     }
/*  340 */     int i = paramInt1;
/*      */ 
/*  343 */     char[] arrayOfChar = paramSegment.array;
/*  344 */     int j = paramSegment.offset;
/*  345 */     int k = paramSegment.count;
/*  346 */     int m = 0;
/*  347 */     int n = -1;
/*  348 */     int i1 = 0;
/*  349 */     int i2 = 0;
/*  350 */     if (paramArrayOfInt != null) {
/*  351 */       i3 = -paramInt3 + j;
/*  352 */       View localView = null;
/*  353 */       if ((paramView != null) && ((localView = paramView.getParent()) != null))
/*      */       {
/*  355 */         i3 += localView.getStartOffset();
/*      */       }
/*  357 */       m = paramArrayOfInt[0];
/*      */ 
/*  359 */       n = paramArrayOfInt[1] + i3;
/*      */ 
/*  361 */       i1 = paramArrayOfInt[2] + i3;
/*      */ 
/*  363 */       i2 = paramArrayOfInt[3] + i3;
/*      */     }
/*      */ 
/*  366 */     int i3 = paramSegment.offset + paramSegment.count;
/*  367 */     for (int i4 = paramSegment.offset; i4 < i3; i4++) {
/*  368 */       if ((arrayOfChar[i4] == '\t') || (((m != 0) || (i4 <= n)) && (arrayOfChar[i4] == ' ') && (i1 <= i4) && (i4 <= i2)))
/*      */       {
/*  374 */         if (arrayOfChar[i4] == '\t') {
/*  375 */           if (paramTabExpander != null) {
/*  376 */             i = (int)paramTabExpander.nextTabStop(i, paramInt3 + i4 - j);
/*      */           }
/*      */           else
/*  379 */             i += paramFontMetrics.charWidth(' ');
/*      */         }
/*  381 */         else if (arrayOfChar[i4] == ' ') {
/*  382 */           i += paramFontMetrics.charWidth(' ') + m;
/*  383 */           if (i4 <= n)
/*  384 */             i++;
/*      */         }
/*      */       }
/*      */       else {
/*  388 */         i += paramFontMetrics.charWidth(arrayOfChar[i4]);
/*      */       }
/*  390 */       if (paramInt2 < i)
/*      */       {
/*      */         int i5;
/*  397 */         if (paramBoolean) {
/*  398 */           i5 = i4 + 1 - j;
/*      */ 
/*  400 */           int i6 = paramFontMetrics.charsWidth(arrayOfChar, j, i5);
/*  401 */           int i7 = paramInt2 - paramInt1;
/*      */ 
/*  403 */           if (i7 < i6)
/*  404 */             while (i5 > 0) {
/*  405 */               int i8 = i5 > 1 ? paramFontMetrics.charsWidth(arrayOfChar, j, i5 - 1) : 0;
/*      */ 
/*  407 */               if (i7 >= i8) {
/*  408 */                 if (i7 - i8 >= i6 - i7) break;
/*  409 */                 i5--; break;
/*      */               }
/*      */ 
/*  415 */               i6 = i8;
/*  416 */               i5--;
/*      */             }
/*      */         }
/*      */         else {
/*  420 */           i5 = i4 - j;
/*      */ 
/*  422 */           while ((i5 > 0) && (paramFontMetrics.charsWidth(arrayOfChar, j, i5) > paramInt2 - paramInt1)) {
/*  423 */             i5--;
/*      */           }
/*      */         }
/*      */ 
/*  427 */         return i5;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  432 */     return k;
/*      */   }
/*      */ 
/*      */   public static final int getBreakLocation(Segment paramSegment, FontMetrics paramFontMetrics, int paramInt1, int paramInt2, TabExpander paramTabExpander, int paramInt3)
/*      */   {
/*  452 */     char[] arrayOfChar = paramSegment.array;
/*  453 */     int i = paramSegment.offset;
/*  454 */     int j = paramSegment.count;
/*  455 */     int k = getTabbedTextOffset(paramSegment, paramFontMetrics, paramInt1, paramInt2, paramTabExpander, paramInt3, false);
/*      */ 
/*  458 */     if (k >= j - 1) {
/*  459 */       return j;
/*      */     }
/*      */ 
/*  462 */     for (int m = i + k; m >= i; m--) {
/*  463 */       int n = arrayOfChar[m];
/*  464 */       if (n < 256)
/*      */       {
/*  466 */         if (Character.isWhitespace(n)) {
/*  467 */           k = m - i + 1;
/*  468 */           break;
/*      */         }
/*      */       }
/*      */       else {
/*  472 */         BreakIterator localBreakIterator = BreakIterator.getLineInstance();
/*  473 */         localBreakIterator.setText(paramSegment);
/*  474 */         int i1 = localBreakIterator.preceding(m + 1);
/*  475 */         if (i1 <= i) break;
/*  476 */         k = i1 - i; break;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  481 */     return k;
/*      */   }
/*      */ 
/*      */   public static final int getRowStart(JTextComponent paramJTextComponent, int paramInt)
/*      */     throws BadLocationException
/*      */   {
/*  497 */     Rectangle localRectangle = paramJTextComponent.modelToView(paramInt);
/*  498 */     if (localRectangle == null) {
/*  499 */       return -1;
/*      */     }
/*  501 */     int i = paramInt;
/*  502 */     int j = localRectangle.y;
/*  503 */     while ((localRectangle != null) && (j == localRectangle.y))
/*      */     {
/*  505 */       if (localRectangle.height != 0) {
/*  506 */         paramInt = i;
/*      */       }
/*  508 */       i--;
/*  509 */       localRectangle = i >= 0 ? paramJTextComponent.modelToView(i) : null;
/*      */     }
/*  511 */     return paramInt;
/*      */   }
/*      */ 
/*      */   public static final int getRowEnd(JTextComponent paramJTextComponent, int paramInt)
/*      */     throws BadLocationException
/*      */   {
/*  527 */     Rectangle localRectangle = paramJTextComponent.modelToView(paramInt);
/*  528 */     if (localRectangle == null) {
/*  529 */       return -1;
/*      */     }
/*  531 */     int i = paramJTextComponent.getDocument().getLength();
/*  532 */     int j = paramInt;
/*  533 */     int k = localRectangle.y;
/*  534 */     while ((localRectangle != null) && (k == localRectangle.y))
/*      */     {
/*  536 */       if (localRectangle.height != 0) {
/*  537 */         paramInt = j;
/*      */       }
/*  539 */       j++;
/*  540 */       localRectangle = j <= i ? paramJTextComponent.modelToView(j) : null;
/*      */     }
/*  542 */     return paramInt;
/*      */   }
/*      */ 
/*      */   public static final int getPositionAbove(JTextComponent paramJTextComponent, int paramInt1, int paramInt2)
/*      */     throws BadLocationException
/*      */   {
/*  559 */     int i = getRowStart(paramJTextComponent, paramInt1) - 1;
/*  560 */     if (i < 0) {
/*  561 */       return -1;
/*      */     }
/*  563 */     int j = 2147483647;
/*  564 */     int k = 0;
/*  565 */     Rectangle localRectangle = null;
/*  566 */     if (i >= 0) {
/*  567 */       localRectangle = paramJTextComponent.modelToView(i);
/*  568 */       k = localRectangle.y;
/*      */     }
/*  570 */     while ((localRectangle != null) && (k == localRectangle.y)) {
/*  571 */       int m = Math.abs(localRectangle.x - paramInt2);
/*  572 */       if (m < j) {
/*  573 */         paramInt1 = i;
/*  574 */         j = m;
/*      */       }
/*  576 */       i--;
/*  577 */       localRectangle = i >= 0 ? paramJTextComponent.modelToView(i) : null;
/*      */     }
/*  579 */     return paramInt1;
/*      */   }
/*      */ 
/*      */   public static final int getPositionBelow(JTextComponent paramJTextComponent, int paramInt1, int paramInt2)
/*      */     throws BadLocationException
/*      */   {
/*  596 */     int i = getRowEnd(paramJTextComponent, paramInt1) + 1;
/*  597 */     if (i <= 0) {
/*  598 */       return -1;
/*      */     }
/*  600 */     int j = 2147483647;
/*  601 */     int k = paramJTextComponent.getDocument().getLength();
/*  602 */     int m = 0;
/*  603 */     Rectangle localRectangle = null;
/*  604 */     if (i <= k) {
/*  605 */       localRectangle = paramJTextComponent.modelToView(i);
/*  606 */       m = localRectangle.y;
/*      */     }
/*  608 */     while ((localRectangle != null) && (m == localRectangle.y)) {
/*  609 */       int n = Math.abs(paramInt2 - localRectangle.x);
/*  610 */       if (n < j) {
/*  611 */         paramInt1 = i;
/*  612 */         j = n;
/*      */       }
/*  614 */       i++;
/*  615 */       localRectangle = i <= k ? paramJTextComponent.modelToView(i) : null;
/*      */     }
/*  617 */     return paramInt1;
/*      */   }
/*      */ 
/*      */   public static final int getWordStart(JTextComponent paramJTextComponent, int paramInt)
/*      */     throws BadLocationException
/*      */   {
/*  630 */     Document localDocument = paramJTextComponent.getDocument();
/*  631 */     Element localElement = getParagraphElement(paramJTextComponent, paramInt);
/*  632 */     if (localElement == null) {
/*  633 */       throw new BadLocationException("No word at " + paramInt, paramInt);
/*      */     }
/*  635 */     int i = localElement.getStartOffset();
/*  636 */     int j = Math.min(localElement.getEndOffset(), localDocument.getLength());
/*      */ 
/*  638 */     Segment localSegment = SegmentCache.getSharedSegment();
/*  639 */     localDocument.getText(i, j - i, localSegment);
/*  640 */     if (localSegment.count > 0) {
/*  641 */       BreakIterator localBreakIterator = BreakIterator.getWordInstance(paramJTextComponent.getLocale());
/*  642 */       localBreakIterator.setText(localSegment);
/*  643 */       int k = localSegment.offset + paramInt - i;
/*  644 */       if (k >= localBreakIterator.last()) {
/*  645 */         k = localBreakIterator.last() - 1;
/*      */       }
/*  647 */       localBreakIterator.following(k);
/*  648 */       paramInt = i + localBreakIterator.previous() - localSegment.offset;
/*      */     }
/*  650 */     SegmentCache.releaseSharedSegment(localSegment);
/*  651 */     return paramInt;
/*      */   }
/*      */ 
/*      */   public static final int getWordEnd(JTextComponent paramJTextComponent, int paramInt)
/*      */     throws BadLocationException
/*      */   {
/*  664 */     Document localDocument = paramJTextComponent.getDocument();
/*  665 */     Element localElement = getParagraphElement(paramJTextComponent, paramInt);
/*  666 */     if (localElement == null) {
/*  667 */       throw new BadLocationException("No word at " + paramInt, paramInt);
/*      */     }
/*  669 */     int i = localElement.getStartOffset();
/*  670 */     int j = Math.min(localElement.getEndOffset(), localDocument.getLength());
/*      */ 
/*  672 */     Segment localSegment = SegmentCache.getSharedSegment();
/*  673 */     localDocument.getText(i, j - i, localSegment);
/*  674 */     if (localSegment.count > 0) {
/*  675 */       BreakIterator localBreakIterator = BreakIterator.getWordInstance(paramJTextComponent.getLocale());
/*  676 */       localBreakIterator.setText(localSegment);
/*  677 */       int k = paramInt - i + localSegment.offset;
/*  678 */       if (k >= localBreakIterator.last()) {
/*  679 */         k = localBreakIterator.last() - 1;
/*      */       }
/*  681 */       paramInt = i + localBreakIterator.following(k) - localSegment.offset;
/*      */     }
/*  683 */     SegmentCache.releaseSharedSegment(localSegment);
/*  684 */     return paramInt;
/*      */   }
/*      */ 
/*      */   public static final int getNextWord(JTextComponent paramJTextComponent, int paramInt)
/*      */     throws BadLocationException
/*      */   {
/*  698 */     Element localElement = getParagraphElement(paramJTextComponent, paramInt);
/*  699 */     for (int i = getNextWordInParagraph(paramJTextComponent, localElement, paramInt, false); 
/*  700 */       i == -1; 
/*  701 */       i = getNextWordInParagraph(paramJTextComponent, localElement, paramInt, true))
/*      */     {
/*  704 */       paramInt = localElement.getEndOffset();
/*  705 */       localElement = getParagraphElement(paramJTextComponent, paramInt);
/*      */     }
/*  707 */     return i;
/*      */   }
/*      */ 
/*      */   static int getNextWordInParagraph(JTextComponent paramJTextComponent, Element paramElement, int paramInt, boolean paramBoolean)
/*      */     throws BadLocationException
/*      */   {
/*  718 */     if (paramElement == null) {
/*  719 */       throw new BadLocationException("No more words", paramInt);
/*      */     }
/*  721 */     Document localDocument = paramElement.getDocument();
/*  722 */     int i = paramElement.getStartOffset();
/*  723 */     int j = Math.min(paramElement.getEndOffset(), localDocument.getLength());
/*  724 */     if ((paramInt >= j) || (paramInt < i)) {
/*  725 */       throw new BadLocationException("No more words", paramInt);
/*      */     }
/*  727 */     Segment localSegment = SegmentCache.getSharedSegment();
/*  728 */     localDocument.getText(i, j - i, localSegment);
/*  729 */     BreakIterator localBreakIterator = BreakIterator.getWordInstance(paramJTextComponent.getLocale());
/*  730 */     localBreakIterator.setText(localSegment);
/*  731 */     if ((paramBoolean) && (localBreakIterator.first() == localSegment.offset + paramInt - i) && (!Character.isWhitespace(localSegment.array[localBreakIterator.first()])))
/*      */     {
/*  734 */       return paramInt;
/*      */     }
/*  736 */     int k = localBreakIterator.following(localSegment.offset + paramInt - i);
/*  737 */     if ((k == -1) || (k >= localSegment.offset + localSegment.count))
/*      */     {
/*  740 */       return -1;
/*      */     }
/*      */ 
/*  745 */     char c = localSegment.array[k];
/*  746 */     if (!Character.isWhitespace(c)) {
/*  747 */       return i + k - localSegment.offset;
/*      */     }
/*      */ 
/*  753 */     k = localBreakIterator.next();
/*  754 */     if (k != -1) {
/*  755 */       paramInt = i + k - localSegment.offset;
/*  756 */       if (paramInt != j) {
/*  757 */         return paramInt;
/*      */       }
/*      */     }
/*  760 */     SegmentCache.releaseSharedSegment(localSegment);
/*  761 */     return -1;
/*      */   }
/*      */ 
/*      */   public static final int getPreviousWord(JTextComponent paramJTextComponent, int paramInt)
/*      */     throws BadLocationException
/*      */   {
/*  776 */     Element localElement = getParagraphElement(paramJTextComponent, paramInt);
/*  777 */     for (int i = getPrevWordInParagraph(paramJTextComponent, localElement, paramInt); 
/*  778 */       i == -1; 
/*  779 */       i = getPrevWordInParagraph(paramJTextComponent, localElement, paramInt))
/*      */     {
/*  782 */       paramInt = localElement.getStartOffset() - 1;
/*  783 */       localElement = getParagraphElement(paramJTextComponent, paramInt);
/*      */     }
/*  785 */     return i;
/*      */   }
/*      */ 
/*      */   static int getPrevWordInParagraph(JTextComponent paramJTextComponent, Element paramElement, int paramInt)
/*      */     throws BadLocationException
/*      */   {
/*  796 */     if (paramElement == null) {
/*  797 */       throw new BadLocationException("No more words", paramInt);
/*      */     }
/*  799 */     Document localDocument = paramElement.getDocument();
/*  800 */     int i = paramElement.getStartOffset();
/*  801 */     int j = paramElement.getEndOffset();
/*  802 */     if ((paramInt > j) || (paramInt < i)) {
/*  803 */       throw new BadLocationException("No more words", paramInt);
/*      */     }
/*  805 */     Segment localSegment = SegmentCache.getSharedSegment();
/*  806 */     localDocument.getText(i, j - i, localSegment);
/*  807 */     BreakIterator localBreakIterator = BreakIterator.getWordInstance(paramJTextComponent.getLocale());
/*  808 */     localBreakIterator.setText(localSegment);
/*  809 */     if (localBreakIterator.following(localSegment.offset + paramInt - i) == -1) {
/*  810 */       localBreakIterator.last();
/*      */     }
/*  812 */     int k = localBreakIterator.previous();
/*  813 */     if (k == localSegment.offset + paramInt - i) {
/*  814 */       k = localBreakIterator.previous();
/*      */     }
/*      */ 
/*  817 */     if (k == -1)
/*      */     {
/*  819 */       return -1;
/*      */     }
/*      */ 
/*  824 */     char c = localSegment.array[k];
/*  825 */     if (!Character.isWhitespace(c)) {
/*  826 */       return i + k - localSegment.offset;
/*      */     }
/*      */ 
/*  832 */     k = localBreakIterator.previous();
/*  833 */     if (k != -1) {
/*  834 */       return i + k - localSegment.offset;
/*      */     }
/*  836 */     SegmentCache.releaseSharedSegment(localSegment);
/*  837 */     return -1;
/*      */   }
/*      */ 
/*      */   public static final Element getParagraphElement(JTextComponent paramJTextComponent, int paramInt)
/*      */   {
/*  848 */     Document localDocument = paramJTextComponent.getDocument();
/*  849 */     if ((localDocument instanceof StyledDocument)) {
/*  850 */       return ((StyledDocument)localDocument).getParagraphElement(paramInt);
/*      */     }
/*  852 */     Element localElement1 = localDocument.getDefaultRootElement();
/*  853 */     int i = localElement1.getElementIndex(paramInt);
/*  854 */     Element localElement2 = localElement1.getElement(i);
/*  855 */     if ((paramInt >= localElement2.getStartOffset()) && (paramInt < localElement2.getEndOffset())) {
/*  856 */       return localElement2;
/*      */     }
/*  858 */     return null;
/*      */   }
/*      */ 
/*      */   static boolean isComposedTextElement(Document paramDocument, int paramInt) {
/*  862 */     Element localElement = paramDocument.getDefaultRootElement();
/*  863 */     while (!localElement.isLeaf()) {
/*  864 */       localElement = localElement.getElement(localElement.getElementIndex(paramInt));
/*      */     }
/*  866 */     return isComposedTextElement(localElement);
/*      */   }
/*      */ 
/*      */   static boolean isComposedTextElement(Element paramElement) {
/*  870 */     AttributeSet localAttributeSet = paramElement.getAttributes();
/*  871 */     return isComposedTextAttributeDefined(localAttributeSet);
/*      */   }
/*      */ 
/*      */   static boolean isComposedTextAttributeDefined(AttributeSet paramAttributeSet) {
/*  875 */     return (paramAttributeSet != null) && (paramAttributeSet.isDefined(StyleConstants.ComposedTextAttribute));
/*      */   }
/*      */ 
/*      */   static int drawComposedText(View paramView, AttributeSet paramAttributeSet, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     throws BadLocationException
/*      */   {
/*  894 */     Graphics2D localGraphics2D = (Graphics2D)paramGraphics;
/*  895 */     AttributedString localAttributedString = (AttributedString)paramAttributeSet.getAttribute(StyleConstants.ComposedTextAttribute);
/*      */ 
/*  897 */     localAttributedString.addAttribute(TextAttribute.FONT, paramGraphics.getFont());
/*      */ 
/*  899 */     if (paramInt3 >= paramInt4) {
/*  900 */       return paramInt1;
/*      */     }
/*  902 */     AttributedCharacterIterator localAttributedCharacterIterator = localAttributedString.getIterator(null, paramInt3, paramInt4);
/*  903 */     return paramInt1 + (int)SwingUtilities2.drawString(getJComponent(paramView), localGraphics2D, localAttributedCharacterIterator, paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   static void paintComposedText(Graphics paramGraphics, Rectangle paramRectangle, GlyphView paramGlyphView)
/*      */   {
/*  911 */     if ((paramGraphics instanceof Graphics2D)) {
/*  912 */       Graphics2D localGraphics2D = (Graphics2D)paramGraphics;
/*  913 */       int i = paramGlyphView.getStartOffset();
/*  914 */       int j = paramGlyphView.getEndOffset();
/*  915 */       AttributeSet localAttributeSet = paramGlyphView.getElement().getAttributes();
/*  916 */       AttributedString localAttributedString = (AttributedString)localAttributeSet.getAttribute(StyleConstants.ComposedTextAttribute);
/*      */ 
/*  918 */       int k = paramGlyphView.getElement().getStartOffset();
/*  919 */       int m = paramRectangle.y + paramRectangle.height - (int)paramGlyphView.getGlyphPainter().getDescent(paramGlyphView);
/*  920 */       int n = paramRectangle.x;
/*      */ 
/*  923 */       localAttributedString.addAttribute(TextAttribute.FONT, paramGlyphView.getFont());
/*  924 */       localAttributedString.addAttribute(TextAttribute.FOREGROUND, paramGlyphView.getForeground());
/*  925 */       if (StyleConstants.isBold(paramGlyphView.getAttributes())) {
/*  926 */         localAttributedString.addAttribute(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD);
/*      */       }
/*  928 */       if (StyleConstants.isItalic(paramGlyphView.getAttributes())) {
/*  929 */         localAttributedString.addAttribute(TextAttribute.POSTURE, TextAttribute.POSTURE_OBLIQUE);
/*      */       }
/*  931 */       if (paramGlyphView.isUnderline()) {
/*  932 */         localAttributedString.addAttribute(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
/*      */       }
/*  934 */       if (paramGlyphView.isStrikeThrough()) {
/*  935 */         localAttributedString.addAttribute(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON);
/*      */       }
/*  937 */       if (paramGlyphView.isSuperscript()) {
/*  938 */         localAttributedString.addAttribute(TextAttribute.SUPERSCRIPT, TextAttribute.SUPERSCRIPT_SUPER);
/*      */       }
/*  940 */       if (paramGlyphView.isSubscript()) {
/*  941 */         localAttributedString.addAttribute(TextAttribute.SUPERSCRIPT, TextAttribute.SUPERSCRIPT_SUB);
/*      */       }
/*      */ 
/*  945 */       AttributedCharacterIterator localAttributedCharacterIterator = localAttributedString.getIterator(null, i - k, j - k);
/*  946 */       SwingUtilities2.drawString(getJComponent(paramGlyphView), localGraphics2D, localAttributedCharacterIterator, n, m);
/*      */     }
/*      */   }
/*      */ 
/*      */   static boolean isLeftToRight(Component paramComponent)
/*      */   {
/*  956 */     return paramComponent.getComponentOrientation().isLeftToRight();
/*      */   }
/*      */ 
/*      */   static int getNextVisualPositionFrom(View paramView, int paramInt1, Position.Bias paramBias, Shape paramShape, int paramInt2, Position.Bias[] paramArrayOfBias)
/*      */     throws BadLocationException
/*      */   {
/*  996 */     if (paramView.getViewCount() == 0)
/*      */     {
/*  998 */       return paramInt1;
/*      */     }
/* 1000 */     int i = (paramInt2 == 1) || (paramInt2 == 7) ? 1 : 0;
/*      */     int k;
/*      */     Object localObject;
/*      */     int j;
/* 1003 */     if (paramInt1 == -1)
/*      */     {
/* 1005 */       k = i != 0 ? paramView.getViewCount() - 1 : 0;
/* 1006 */       View localView = paramView.getView(k);
/* 1007 */       localObject = paramView.getChildAllocation(k, paramShape);
/* 1008 */       j = localView.getNextVisualPositionFrom(paramInt1, paramBias, (Shape)localObject, paramInt2, paramArrayOfBias);
/*      */ 
/* 1010 */       if ((j == -1) && (i == 0) && (paramView.getViewCount() > 1))
/*      */       {
/* 1014 */         localView = paramView.getView(1);
/* 1015 */         localObject = paramView.getChildAllocation(1, paramShape);
/* 1016 */         j = localView.getNextVisualPositionFrom(-1, paramArrayOfBias[0], (Shape)localObject, paramInt2, paramArrayOfBias);
/*      */       }
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/* 1022 */       k = i != 0 ? -1 : 1;
/*      */       int m;
/* 1024 */       if ((paramBias == Position.Bias.Backward) && (paramInt1 > 0)) {
/* 1025 */         m = paramView.getViewIndex(paramInt1 - 1, Position.Bias.Forward);
/*      */       }
/*      */       else {
/* 1028 */         m = paramView.getViewIndex(paramInt1, Position.Bias.Forward);
/*      */       }
/* 1030 */       localObject = paramView.getView(m);
/* 1031 */       Shape localShape = paramView.getChildAllocation(m, paramShape);
/* 1032 */       j = ((View)localObject).getNextVisualPositionFrom(paramInt1, paramBias, localShape, paramInt2, paramArrayOfBias);
/*      */ 
/* 1034 */       if (((paramInt2 == 3) || (paramInt2 == 7)) && ((paramView instanceof CompositeView)) && (((CompositeView)paramView).flipEastAndWestAtEnds(paramInt1, paramBias)))
/*      */       {
/* 1038 */         k *= -1;
/*      */       }
/* 1040 */       m += k;
/* 1041 */       if ((j == -1) && (m >= 0) && (m < paramView.getViewCount()))
/*      */       {
/* 1043 */         localObject = paramView.getView(m);
/* 1044 */         localShape = paramView.getChildAllocation(m, paramShape);
/* 1045 */         j = ((View)localObject).getNextVisualPositionFrom(-1, paramBias, localShape, paramInt2, paramArrayOfBias);
/*      */ 
/* 1050 */         if ((j == paramInt1) && (paramArrayOfBias[0] != paramBias)) {
/* 1051 */           return getNextVisualPositionFrom(paramView, paramInt1, paramArrayOfBias[0], paramShape, paramInt2, paramArrayOfBias);
/*      */         }
/*      */ 
/*      */       }
/* 1056 */       else if ((j != -1) && (paramArrayOfBias[0] != paramBias) && (((k == 1) && (((View)localObject).getEndOffset() == j)) || ((k == -1) && (((View)localObject).getStartOffset() == j) && (m >= 0) && (m < paramView.getViewCount()))))
/*      */       {
/* 1063 */         localObject = paramView.getView(m);
/* 1064 */         localShape = paramView.getChildAllocation(m, paramShape);
/* 1065 */         Position.Bias localBias = paramArrayOfBias[0];
/* 1066 */         int n = ((View)localObject).getNextVisualPositionFrom(-1, paramBias, localShape, paramInt2, paramArrayOfBias);
/*      */ 
/* 1068 */         if (paramArrayOfBias[0] == paramBias) {
/* 1069 */           j = n;
/*      */         }
/*      */         else {
/* 1072 */           paramArrayOfBias[0] = localBias;
/*      */         }
/*      */       }
/*      */     }
/* 1076 */     return j;
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.text.Utilities
 * JD-Core Version:    0.6.2
 */