/*     */ package javax.swing;
/*     */ 
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Font;
/*     */ import java.awt.FontMetrics;
/*     */ import java.awt.Insets;
/*     */ import java.awt.Rectangle;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectOutputStream;
/*     */ import javax.accessibility.AccessibleContext;
/*     */ import javax.accessibility.AccessibleState;
/*     */ import javax.accessibility.AccessibleStateSet;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.text.AbstractDocument;
/*     */ import javax.swing.text.BadLocationException;
/*     */ import javax.swing.text.Document;
/*     */ import javax.swing.text.Element;
/*     */ import javax.swing.text.JTextComponent;
/*     */ import javax.swing.text.JTextComponent.AccessibleJTextComponent;
/*     */ import javax.swing.text.PlainDocument;
/*     */ 
/*     */ public class JTextArea extends JTextComponent
/*     */ {
/*     */   private static final String uiClassID = "TextAreaUI";
/*     */   private int rows;
/*     */   private int columns;
/*     */   private int columnWidth;
/*     */   private int rowHeight;
/*     */   private boolean wrap;
/*     */   private boolean word;
/*     */ 
/*     */   public JTextArea()
/*     */   {
/* 140 */     this(null, null, 0, 0);
/*     */   }
/*     */ 
/*     */   public JTextArea(String paramString)
/*     */   {
/* 150 */     this(null, paramString, 0, 0);
/*     */   }
/*     */ 
/*     */   public JTextArea(int paramInt1, int paramInt2)
/*     */   {
/* 164 */     this(null, null, paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   public JTextArea(String paramString, int paramInt1, int paramInt2)
/*     */   {
/* 178 */     this(null, paramString, paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   public JTextArea(Document paramDocument)
/*     */   {
/* 188 */     this(paramDocument, null, 0, 0);
/*     */   }
/*     */ 
/*     */   public JTextArea(Document paramDocument, String paramString, int paramInt1, int paramInt2)
/*     */   {
/* 205 */     this.rows = paramInt1;
/* 206 */     this.columns = paramInt2;
/* 207 */     if (paramDocument == null) {
/* 208 */       paramDocument = createDefaultModel();
/*     */     }
/* 210 */     setDocument(paramDocument);
/* 211 */     if (paramString != null) {
/* 212 */       setText(paramString);
/* 213 */       select(0, 0);
/*     */     }
/* 215 */     if (paramInt1 < 0) {
/* 216 */       throw new IllegalArgumentException("rows: " + paramInt1);
/*     */     }
/* 218 */     if (paramInt2 < 0) {
/* 219 */       throw new IllegalArgumentException("columns: " + paramInt2);
/*     */     }
/* 221 */     LookAndFeel.installProperty(this, "focusTraversalKeysForward", JComponent.getManagingFocusForwardTraversalKeys());
/*     */ 
/* 225 */     LookAndFeel.installProperty(this, "focusTraversalKeysBackward", JComponent.getManagingFocusBackwardTraversalKeys());
/*     */   }
/*     */ 
/*     */   public String getUIClassID()
/*     */   {
/* 239 */     return "TextAreaUI";
/*     */   }
/*     */ 
/*     */   protected Document createDefaultModel()
/*     */   {
/* 250 */     return new PlainDocument();
/*     */   }
/*     */ 
/*     */   public void setTabSize(int paramInt)
/*     */   {
/* 267 */     Document localDocument = getDocument();
/* 268 */     if (localDocument != null) {
/* 269 */       int i = getTabSize();
/* 270 */       localDocument.putProperty("tabSize", Integer.valueOf(paramInt));
/* 271 */       firePropertyChange("tabSize", i, paramInt);
/*     */     }
/*     */   }
/*     */ 
/*     */   public int getTabSize()
/*     */   {
/* 282 */     int i = 8;
/* 283 */     Document localDocument = getDocument();
/* 284 */     if (localDocument != null) {
/* 285 */       Integer localInteger = (Integer)localDocument.getProperty("tabSize");
/* 286 */       if (localInteger != null) {
/* 287 */         i = localInteger.intValue();
/*     */       }
/*     */     }
/* 290 */     return i;
/*     */   }
/*     */ 
/*     */   public void setLineWrap(boolean paramBoolean)
/*     */   {
/* 309 */     boolean bool = this.wrap;
/* 310 */     this.wrap = paramBoolean;
/* 311 */     firePropertyChange("lineWrap", bool, paramBoolean);
/*     */   }
/*     */ 
/*     */   public boolean getLineWrap()
/*     */   {
/* 323 */     return this.wrap;
/*     */   }
/*     */ 
/*     */   public void setWrapStyleWord(boolean paramBoolean)
/*     */   {
/* 343 */     boolean bool = this.word;
/* 344 */     this.word = paramBoolean;
/* 345 */     firePropertyChange("wrapStyleWord", bool, paramBoolean);
/*     */   }
/*     */ 
/*     */   public boolean getWrapStyleWord()
/*     */   {
/* 360 */     return this.word;
/*     */   }
/*     */ 
/*     */   public int getLineOfOffset(int paramInt)
/*     */     throws BadLocationException
/*     */   {
/* 373 */     Document localDocument = getDocument();
/* 374 */     if (paramInt < 0)
/* 375 */       throw new BadLocationException("Can't translate offset to line", -1);
/* 376 */     if (paramInt > localDocument.getLength()) {
/* 377 */       throw new BadLocationException("Can't translate offset to line", localDocument.getLength() + 1);
/*     */     }
/* 379 */     Element localElement = getDocument().getDefaultRootElement();
/* 380 */     return localElement.getElementIndex(paramInt);
/*     */   }
/*     */ 
/*     */   public int getLineCount()
/*     */   {
/* 390 */     Element localElement = getDocument().getDefaultRootElement();
/* 391 */     return localElement.getElementCount();
/*     */   }
/*     */ 
/*     */   public int getLineStartOffset(int paramInt)
/*     */     throws BadLocationException
/*     */   {
/* 405 */     int i = getLineCount();
/* 406 */     if (paramInt < 0)
/* 407 */       throw new BadLocationException("Negative line", -1);
/* 408 */     if (paramInt >= i) {
/* 409 */       throw new BadLocationException("No such line", getDocument().getLength() + 1);
/*     */     }
/* 411 */     Element localElement1 = getDocument().getDefaultRootElement();
/* 412 */     Element localElement2 = localElement1.getElement(paramInt);
/* 413 */     return localElement2.getStartOffset();
/*     */   }
/*     */ 
/*     */   public int getLineEndOffset(int paramInt)
/*     */     throws BadLocationException
/*     */   {
/* 428 */     int i = getLineCount();
/* 429 */     if (paramInt < 0)
/* 430 */       throw new BadLocationException("Negative line", -1);
/* 431 */     if (paramInt >= i) {
/* 432 */       throw new BadLocationException("No such line", getDocument().getLength() + 1);
/*     */     }
/* 434 */     Element localElement1 = getDocument().getDefaultRootElement();
/* 435 */     Element localElement2 = localElement1.getElement(paramInt);
/* 436 */     int j = localElement2.getEndOffset();
/*     */ 
/* 438 */     return paramInt == i - 1 ? j - 1 : j;
/*     */   }
/*     */ 
/*     */   public void insert(String paramString, int paramInt)
/*     */   {
/* 456 */     Document localDocument = getDocument();
/* 457 */     if (localDocument != null)
/*     */       try {
/* 459 */         localDocument.insertString(paramInt, paramString, null);
/*     */       } catch (BadLocationException localBadLocationException) {
/* 461 */         throw new IllegalArgumentException(localBadLocationException.getMessage());
/*     */       }
/*     */   }
/*     */ 
/*     */   public void append(String paramString)
/*     */   {
/* 474 */     Document localDocument = getDocument();
/* 475 */     if (localDocument != null)
/*     */       try {
/* 477 */         localDocument.insertString(localDocument.getLength(), paramString, null);
/*     */       }
/*     */       catch (BadLocationException localBadLocationException)
/*     */       {
/*     */       }
/*     */   }
/*     */ 
/*     */   public void replaceRange(String paramString, int paramInt1, int paramInt2)
/*     */   {
/* 497 */     if (paramInt2 < paramInt1) {
/* 498 */       throw new IllegalArgumentException("end before start");
/*     */     }
/* 500 */     Document localDocument = getDocument();
/* 501 */     if (localDocument != null)
/*     */       try {
/* 503 */         if ((localDocument instanceof AbstractDocument)) {
/* 504 */           ((AbstractDocument)localDocument).replace(paramInt1, paramInt2 - paramInt1, paramString, null);
/*     */         }
/*     */         else
/*     */         {
/* 508 */           localDocument.remove(paramInt1, paramInt2 - paramInt1);
/* 509 */           localDocument.insertString(paramInt1, paramString, null);
/*     */         }
/*     */       } catch (BadLocationException localBadLocationException) {
/* 512 */         throw new IllegalArgumentException(localBadLocationException.getMessage());
/*     */       }
/*     */   }
/*     */ 
/*     */   public int getRows()
/*     */   {
/* 523 */     return this.rows;
/*     */   }
/*     */ 
/*     */   public void setRows(int paramInt)
/*     */   {
/* 537 */     int i = this.rows;
/* 538 */     if (paramInt < 0) {
/* 539 */       throw new IllegalArgumentException("rows less than zero.");
/*     */     }
/* 541 */     if (paramInt != i) {
/* 542 */       this.rows = paramInt;
/* 543 */       invalidate();
/*     */     }
/*     */   }
/*     */ 
/*     */   protected int getRowHeight()
/*     */   {
/* 554 */     if (this.rowHeight == 0) {
/* 555 */       FontMetrics localFontMetrics = getFontMetrics(getFont());
/* 556 */       this.rowHeight = localFontMetrics.getHeight();
/*     */     }
/* 558 */     return this.rowHeight;
/*     */   }
/*     */ 
/*     */   public int getColumns()
/*     */   {
/* 567 */     return this.columns;
/*     */   }
/*     */ 
/*     */   public void setColumns(int paramInt)
/*     */   {
/* 581 */     int i = this.columns;
/* 582 */     if (paramInt < 0) {
/* 583 */       throw new IllegalArgumentException("columns less than zero.");
/*     */     }
/* 585 */     if (paramInt != i) {
/* 586 */       this.columns = paramInt;
/* 587 */       invalidate();
/*     */     }
/*     */   }
/*     */ 
/*     */   protected int getColumnWidth()
/*     */   {
/* 602 */     if (this.columnWidth == 0) {
/* 603 */       FontMetrics localFontMetrics = getFontMetrics(getFont());
/* 604 */       this.columnWidth = localFontMetrics.charWidth('m');
/*     */     }
/* 606 */     return this.columnWidth;
/*     */   }
/*     */ 
/*     */   public Dimension getPreferredSize()
/*     */   {
/* 619 */     Dimension localDimension = super.getPreferredSize();
/* 620 */     localDimension = localDimension == null ? new Dimension(400, 400) : localDimension;
/* 621 */     Insets localInsets = getInsets();
/*     */ 
/* 623 */     if (this.columns != 0) {
/* 624 */       localDimension.width = Math.max(localDimension.width, this.columns * getColumnWidth() + localInsets.left + localInsets.right);
/*     */     }
/*     */ 
/* 627 */     if (this.rows != 0) {
/* 628 */       localDimension.height = Math.max(localDimension.height, this.rows * getRowHeight() + localInsets.top + localInsets.bottom);
/*     */     }
/*     */ 
/* 631 */     return localDimension;
/*     */   }
/*     */ 
/*     */   public void setFont(Font paramFont)
/*     */   {
/* 641 */     super.setFont(paramFont);
/* 642 */     this.rowHeight = 0;
/* 643 */     this.columnWidth = 0;
/*     */   }
/*     */ 
/*     */   protected String paramString()
/*     */   {
/* 657 */     String str1 = this.wrap ? "true" : "false";
/*     */ 
/* 659 */     String str2 = this.word ? "true" : "false";
/*     */ 
/* 662 */     return super.paramString() + ",colums=" + this.columns + ",columWidth=" + this.columnWidth + ",rows=" + this.rows + ",rowHeight=" + this.rowHeight + ",word=" + str2 + ",wrap=" + str1;
/*     */   }
/*     */ 
/*     */   public boolean getScrollableTracksViewportWidth()
/*     */   {
/* 683 */     return this.wrap ? true : super.getScrollableTracksViewportWidth();
/*     */   }
/*     */ 
/*     */   public Dimension getPreferredScrollableViewportSize()
/*     */   {
/* 696 */     Dimension localDimension = super.getPreferredScrollableViewportSize();
/* 697 */     localDimension = localDimension == null ? new Dimension(400, 400) : localDimension;
/* 698 */     Insets localInsets = getInsets();
/*     */ 
/* 700 */     localDimension.width = (this.columns == 0 ? localDimension.width : this.columns * getColumnWidth() + localInsets.left + localInsets.right);
/*     */ 
/* 702 */     localDimension.height = (this.rows == 0 ? localDimension.height : this.rows * getRowHeight() + localInsets.top + localInsets.bottom);
/*     */ 
/* 704 */     return localDimension;
/*     */   }
/*     */ 
/*     */   public int getScrollableUnitIncrement(Rectangle paramRectangle, int paramInt1, int paramInt2)
/*     */   {
/* 729 */     switch (paramInt1) {
/*     */     case 1:
/* 731 */       return getRowHeight();
/*     */     case 0:
/* 733 */       return getColumnWidth();
/*     */     }
/* 735 */     throw new IllegalArgumentException("Invalid orientation: " + paramInt1);
/*     */   }
/*     */ 
/*     */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*     */     throws IOException
/*     */   {
/* 744 */     paramObjectOutputStream.defaultWriteObject();
/* 745 */     if (getUIClassID().equals("TextAreaUI")) {
/* 746 */       byte b = JComponent.getWriteObjCounter(this);
/* 747 */       b = (byte)(b - 1); JComponent.setWriteObjCounter(this, b);
/* 748 */       if ((b == 0) && (this.ui != null))
/* 749 */         this.ui.installUI(this);
/*     */     }
/*     */   }
/*     */ 
/*     */   public AccessibleContext getAccessibleContext()
/*     */   {
/* 769 */     if (this.accessibleContext == null) {
/* 770 */       this.accessibleContext = new AccessibleJTextArea();
/*     */     }
/* 772 */     return this.accessibleContext;
/*     */   }
/*     */ 
/*     */   protected class AccessibleJTextArea extends JTextComponent.AccessibleJTextComponent
/*     */   {
/*     */     protected AccessibleJTextArea()
/*     */     {
/* 790 */       super();
/*     */     }
/*     */ 
/*     */     public AccessibleStateSet getAccessibleStateSet()
/*     */     {
/* 800 */       AccessibleStateSet localAccessibleStateSet = super.getAccessibleStateSet();
/* 801 */       localAccessibleStateSet.add(AccessibleState.MULTI_LINE);
/* 802 */       return localAccessibleStateSet;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.JTextArea
 * JD-Core Version:    0.6.2
 */