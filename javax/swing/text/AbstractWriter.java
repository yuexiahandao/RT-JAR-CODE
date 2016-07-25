/*     */ package javax.swing.text;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.Writer;
/*     */ import java.util.Enumeration;
/*     */ 
/*     */ public abstract class AbstractWriter
/*     */ {
/*     */   private ElementIterator it;
/*     */   private Writer out;
/*  46 */   private int indentLevel = 0;
/*  47 */   private int indentSpace = 2;
/*  48 */   private Document doc = null;
/*  49 */   private int maxLineLength = 100;
/*  50 */   private int currLength = 0;
/*  51 */   private int startOffset = 0;
/*  52 */   private int endOffset = 0;
/*     */ 
/*  56 */   private int offsetIndent = 0;
/*     */   private String lineSeparator;
/*     */   private boolean canWrapLines;
/*     */   private boolean isLineEmpty;
/*     */   private char[] indentChars;
/*     */   private char[] tempChars;
/*     */   private char[] newlineChars;
/*     */   private Segment segment;
/*     */   protected static final char NEWLINE = '\n';
/*     */ 
/*     */   protected AbstractWriter(Writer paramWriter, Document paramDocument)
/*     */   {
/* 118 */     this(paramWriter, paramDocument, 0, paramDocument.getLength());
/*     */   }
/*     */ 
/*     */   protected AbstractWriter(Writer paramWriter, Document paramDocument, int paramInt1, int paramInt2)
/*     */   {
/* 133 */     this.doc = paramDocument;
/* 134 */     this.it = new ElementIterator(paramDocument.getDefaultRootElement());
/* 135 */     this.out = paramWriter;
/* 136 */     this.startOffset = paramInt1;
/* 137 */     this.endOffset = (paramInt1 + paramInt2);
/* 138 */     Object localObject = paramDocument.getProperty("__EndOfLine__");
/*     */ 
/* 140 */     if ((localObject instanceof String)) {
/* 141 */       setLineSeparator((String)localObject);
/*     */     }
/*     */     else {
/* 144 */       String str = null;
/*     */       try {
/* 146 */         str = System.getProperty("line.separator"); } catch (SecurityException localSecurityException) {
/*     */       }
/* 148 */       if (str == null)
/*     */       {
/* 151 */         str = "\n";
/*     */       }
/* 153 */       setLineSeparator(str);
/*     */     }
/* 155 */     this.canWrapLines = true;
/*     */   }
/*     */ 
/*     */   protected AbstractWriter(Writer paramWriter, Element paramElement)
/*     */   {
/* 167 */     this(paramWriter, paramElement, 0, paramElement.getEndOffset());
/*     */   }
/*     */ 
/*     */   protected AbstractWriter(Writer paramWriter, Element paramElement, int paramInt1, int paramInt2)
/*     */   {
/* 182 */     this.doc = paramElement.getDocument();
/* 183 */     this.it = new ElementIterator(paramElement);
/* 184 */     this.out = paramWriter;
/* 185 */     this.startOffset = paramInt1;
/* 186 */     this.endOffset = (paramInt1 + paramInt2);
/* 187 */     this.canWrapLines = true;
/*     */   }
/*     */ 
/*     */   public int getStartOffset()
/*     */   {
/* 196 */     return this.startOffset;
/*     */   }
/*     */ 
/*     */   public int getEndOffset()
/*     */   {
/* 205 */     return this.endOffset;
/*     */   }
/*     */ 
/*     */   protected ElementIterator getElementIterator()
/*     */   {
/* 214 */     return this.it;
/*     */   }
/*     */ 
/*     */   protected Writer getWriter()
/*     */   {
/* 223 */     return this.out;
/*     */   }
/*     */ 
/*     */   protected Document getDocument()
/*     */   {
/* 232 */     return this.doc;
/*     */   }
/*     */ 
/*     */   protected boolean inRange(Element paramElement)
/*     */   {
/* 247 */     int i = getStartOffset();
/* 248 */     int j = getEndOffset();
/* 249 */     if (((paramElement.getStartOffset() >= i) && (paramElement.getStartOffset() < j)) || ((i >= paramElement.getStartOffset()) && (i < paramElement.getEndOffset())))
/*     */     {
/* 253 */       return true;
/*     */     }
/* 255 */     return false;
/*     */   }
/*     */ 
/*     */   protected abstract void write()
/*     */     throws IOException, BadLocationException;
/*     */ 
/*     */   protected String getText(Element paramElement)
/*     */     throws BadLocationException
/*     */   {
/* 278 */     return this.doc.getText(paramElement.getStartOffset(), paramElement.getEndOffset() - paramElement.getStartOffset());
/*     */   }
/*     */ 
/*     */   protected void text(Element paramElement)
/*     */     throws BadLocationException, IOException
/*     */   {
/* 295 */     int i = Math.max(getStartOffset(), paramElement.getStartOffset());
/* 296 */     int j = Math.min(getEndOffset(), paramElement.getEndOffset());
/* 297 */     if (i < j) {
/* 298 */       if (this.segment == null) {
/* 299 */         this.segment = new Segment();
/*     */       }
/* 301 */       getDocument().getText(i, j - i, this.segment);
/* 302 */       if (this.segment.count > 0)
/* 303 */         write(this.segment.array, this.segment.offset, this.segment.count);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void setLineLength(int paramInt)
/*     */   {
/* 315 */     this.maxLineLength = paramInt;
/*     */   }
/*     */ 
/*     */   protected int getLineLength()
/*     */   {
/* 324 */     return this.maxLineLength;
/*     */   }
/*     */ 
/*     */   protected void setCurrentLineLength(int paramInt)
/*     */   {
/* 333 */     this.currLength = paramInt;
/* 334 */     this.isLineEmpty = (this.currLength == 0);
/*     */   }
/*     */ 
/*     */   protected int getCurrentLineLength()
/*     */   {
/* 343 */     return this.currLength;
/*     */   }
/*     */ 
/*     */   protected boolean isLineEmpty()
/*     */   {
/* 354 */     return this.isLineEmpty;
/*     */   }
/*     */ 
/*     */   protected void setCanWrapLines(boolean paramBoolean)
/*     */   {
/* 365 */     this.canWrapLines = paramBoolean;
/*     */   }
/*     */ 
/*     */   protected boolean getCanWrapLines()
/*     */   {
/* 375 */     return this.canWrapLines;
/*     */   }
/*     */ 
/*     */   protected void setIndentSpace(int paramInt)
/*     */   {
/* 386 */     this.indentSpace = paramInt;
/*     */   }
/*     */ 
/*     */   protected int getIndentSpace()
/*     */   {
/* 395 */     return this.indentSpace;
/*     */   }
/*     */ 
/*     */   public void setLineSeparator(String paramString)
/*     */   {
/* 406 */     this.lineSeparator = paramString;
/*     */   }
/*     */ 
/*     */   public String getLineSeparator()
/*     */   {
/* 415 */     return this.lineSeparator;
/*     */   }
/*     */ 
/*     */   protected void incrIndent()
/*     */   {
/* 425 */     if (this.offsetIndent > 0) {
/* 426 */       this.offsetIndent += 1;
/*     */     }
/* 429 */     else if (++this.indentLevel * getIndentSpace() >= getLineLength()) {
/* 430 */       this.offsetIndent += 1;
/* 431 */       this.indentLevel -= 1;
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void decrIndent()
/*     */   {
/* 440 */     if (this.offsetIndent > 0) {
/* 441 */       this.offsetIndent -= 1;
/*     */     }
/*     */     else
/* 444 */       this.indentLevel -= 1;
/*     */   }
/*     */ 
/*     */   protected int getIndentLevel()
/*     */   {
/* 456 */     return this.indentLevel;
/*     */   }
/*     */ 
/*     */   protected void indent()
/*     */     throws IOException
/*     */   {
/* 468 */     int i = getIndentLevel() * getIndentSpace();
/* 469 */     if ((this.indentChars == null) || (i > this.indentChars.length)) {
/* 470 */       this.indentChars = new char[i];
/* 471 */       for (j = 0; j < i; j++) {
/* 472 */         this.indentChars[j] = ' ';
/*     */       }
/*     */     }
/* 475 */     int j = getCurrentLineLength();
/* 476 */     boolean bool = isLineEmpty();
/* 477 */     output(this.indentChars, 0, i);
/* 478 */     if ((bool) && (j == 0))
/* 479 */       this.isLineEmpty = true;
/*     */   }
/*     */ 
/*     */   protected void write(char paramChar)
/*     */     throws IOException
/*     */   {
/* 491 */     if (this.tempChars == null) {
/* 492 */       this.tempChars = new char[''];
/*     */     }
/* 494 */     this.tempChars[0] = paramChar;
/* 495 */     write(this.tempChars, 0, 1);
/*     */   }
/*     */ 
/*     */   protected void write(String paramString)
/*     */     throws IOException
/*     */   {
/* 506 */     if (paramString == null) {
/* 507 */       return;
/*     */     }
/* 509 */     int i = paramString.length();
/* 510 */     if ((this.tempChars == null) || (this.tempChars.length < i)) {
/* 511 */       this.tempChars = new char[i];
/*     */     }
/* 513 */     paramString.getChars(0, i, this.tempChars, 0);
/* 514 */     write(this.tempChars, 0, i);
/*     */   }
/*     */ 
/*     */   protected void writeLineSeparator()
/*     */     throws IOException
/*     */   {
/* 524 */     String str = getLineSeparator();
/* 525 */     int i = str.length();
/* 526 */     if ((this.newlineChars == null) || (this.newlineChars.length < i)) {
/* 527 */       this.newlineChars = new char[i];
/*     */     }
/* 529 */     str.getChars(0, i, this.newlineChars, 0);
/* 530 */     output(this.newlineChars, 0, i);
/* 531 */     setCurrentLineLength(0);
/*     */   }
/*     */ 
/*     */   protected void write(char[] paramArrayOfChar, int paramInt1, int paramInt2)
/*     */     throws IOException
/*     */   {
/*     */     int i;
/*     */     int j;
/*     */     int k;
/* 549 */     if (!getCanWrapLines())
/*     */     {
/* 552 */       i = paramInt1;
/* 553 */       j = paramInt1 + paramInt2;
/* 554 */       k = indexOf(paramArrayOfChar, '\n', paramInt1, j);
/* 555 */       while (k != -1) {
/* 556 */         if (k > i) {
/* 557 */           output(paramArrayOfChar, i, k - i);
/*     */         }
/* 559 */         writeLineSeparator();
/* 560 */         i = k + 1;
/* 561 */         k = indexOf(paramArrayOfChar, '\n', i, j);
/*     */       }
/* 563 */       if (i < j) {
/* 564 */         output(paramArrayOfChar, i, j - i);
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 569 */       i = paramInt1;
/* 570 */       j = paramInt1 + paramInt2;
/* 571 */       k = getCurrentLineLength();
/* 572 */       int m = getLineLength();
/*     */ 
/* 574 */       while (i < j) {
/* 575 */         int n = indexOf(paramArrayOfChar, '\n', i, j);
/*     */ 
/* 577 */         int i1 = 0;
/* 578 */         int i2 = 0;
/*     */ 
/* 580 */         k = getCurrentLineLength();
/* 581 */         if ((n != -1) && (k + (n - i) < m))
/*     */         {
/* 583 */           if (n > i) {
/* 584 */             output(paramArrayOfChar, i, n - i);
/*     */           }
/* 586 */           i = n + 1;
/* 587 */           i2 = 1;
/*     */         }
/* 589 */         else if ((n == -1) && (k + (j - i) < m))
/*     */         {
/* 591 */           if (j > i) {
/* 592 */             output(paramArrayOfChar, i, j - i);
/*     */           }
/* 594 */           i = j;
/*     */         }
/*     */         else
/*     */         {
/* 600 */           int i3 = -1;
/* 601 */           int i4 = Math.min(j - i, m - k - 1);
/*     */ 
/* 603 */           int i5 = 0;
/* 604 */           while (i5 < i4) {
/* 605 */             if (Character.isWhitespace(paramArrayOfChar[(i5 + i)]))
/*     */             {
/* 607 */               i3 = i5;
/*     */             }
/* 609 */             i5++;
/*     */           }
/* 611 */           if (i3 != -1)
/*     */           {
/* 613 */             i3 += i + 1;
/* 614 */             output(paramArrayOfChar, i, i3 - i);
/* 615 */             i = i3;
/* 616 */             i1 = 1;
/*     */           }
/*     */           else
/*     */           {
/* 625 */             i5 = Math.max(0, i4);
/* 626 */             i4 = j - i;
/* 627 */             while (i5 < i4) {
/* 628 */               if (Character.isWhitespace(paramArrayOfChar[(i5 + i)]))
/*     */               {
/* 630 */                 i3 = i5;
/* 631 */                 break;
/*     */               }
/* 633 */               i5++;
/*     */             }
/* 635 */             if (i3 == -1) {
/* 636 */               output(paramArrayOfChar, i, j - i);
/* 637 */               i3 = j;
/*     */             }
/*     */             else {
/* 640 */               i3 += i;
/* 641 */               if (paramArrayOfChar[i3] == '\n') {
/* 642 */                 output(paramArrayOfChar, i, i3++ - i);
/*     */ 
/* 644 */                 i2 = 1;
/*     */               }
/*     */               else {
/* 647 */                 output(paramArrayOfChar, i, ++i3 - i);
/*     */ 
/* 649 */                 i1 = 1;
/*     */               }
/*     */             }
/* 652 */             i = i3;
/*     */           }
/*     */         }
/* 655 */         if ((i2 != 0) || (i1 != 0) || (i < j)) {
/* 656 */           writeLineSeparator();
/* 657 */           if ((i < j) || (i2 == 0))
/* 658 */             indent();
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void writeAttributes(AttributeSet paramAttributeSet)
/*     */     throws IOException
/*     */   {
/* 674 */     Enumeration localEnumeration = paramAttributeSet.getAttributeNames();
/* 675 */     while (localEnumeration.hasMoreElements()) {
/* 676 */       Object localObject = localEnumeration.nextElement();
/* 677 */       write(" " + localObject + "=" + paramAttributeSet.getAttribute(localObject));
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void output(char[] paramArrayOfChar, int paramInt1, int paramInt2)
/*     */     throws IOException
/*     */   {
/* 696 */     getWriter().write(paramArrayOfChar, paramInt1, paramInt2);
/* 697 */     setCurrentLineLength(getCurrentLineLength() + paramInt2);
/*     */   }
/*     */ 
/*     */   private int indexOf(char[] paramArrayOfChar, char paramChar, int paramInt1, int paramInt2)
/*     */   {
/* 705 */     while (paramInt1 < paramInt2) {
/* 706 */       if (paramArrayOfChar[paramInt1] == paramChar) {
/* 707 */         return paramInt1;
/*     */       }
/* 709 */       paramInt1++;
/*     */     }
/* 711 */     return -1;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.text.AbstractWriter
 * JD-Core Version:    0.6.2
 */