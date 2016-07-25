/*     */ package sun.swing.text;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.ComponentOrientation;
/*     */ import java.awt.Container;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Font;
/*     */ import java.awt.FontMetrics;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Insets;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.font.FontRenderContext;
/*     */ import java.awt.print.PageFormat;
/*     */ import java.awt.print.Printable;
/*     */ import java.awt.print.PrinterException;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.text.MessageFormat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.ExecutionException;
/*     */ import java.util.concurrent.FutureTask;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import javax.swing.BorderFactory;
/*     */ import javax.swing.CellRendererPane;
/*     */ import javax.swing.JEditorPane;
/*     */ import javax.swing.JTextArea;
/*     */ import javax.swing.JTextField;
/*     */ import javax.swing.JTextPane;
/*     */ import javax.swing.JViewport;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.border.Border;
/*     */ import javax.swing.border.TitledBorder;
/*     */ import javax.swing.text.AbstractDocument;
/*     */ import javax.swing.text.BadLocationException;
/*     */ import javax.swing.text.Document;
/*     */ import javax.swing.text.EditorKit;
/*     */ import javax.swing.text.JTextComponent;
/*     */ import javax.swing.text.html.HTML.Tag;
/*     */ import javax.swing.text.html.HTMLDocument;
/*     */ import javax.swing.text.html.HTMLDocument.Iterator;
/*     */ import sun.font.FontDesignMetrics;
/*     */ import sun.swing.text.html.FrameEditorPaneTag;
/*     */ 
/*     */ public class TextComponentPrintable
/*     */   implements CountingPrintable
/*     */ {
/*     */   private static final int LIST_SIZE = 1000;
/* 103 */   private boolean isLayouted = false;
/*     */   private final JTextComponent textComponentToPrint;
/* 113 */   private final AtomicReference<FontRenderContext> frc = new AtomicReference(null);
/*     */   private final JTextComponent printShell;
/*     */   private final MessageFormat headerFormat;
/*     */   private final MessageFormat footerFormat;
/*     */   private static final float HEADER_FONT_SIZE = 18.0F;
/*     */   private static final float FOOTER_FONT_SIZE = 12.0F;
/*     */   private final Font headerFont;
/*     */   private final Font footerFont;
/*     */   private final List<IntegerSegment> rowsMetrics;
/*     */   private final List<IntegerSegment> pagesMetrics;
/* 564 */   private boolean needReadLock = false;
/*     */ 
/*     */   public static Printable getPrintable(JTextComponent paramJTextComponent, MessageFormat paramMessageFormat1, MessageFormat paramMessageFormat2)
/*     */   {
/* 161 */     if (((paramJTextComponent instanceof JEditorPane)) && (isFrameSetDocument(paramJTextComponent.getDocument())))
/*     */     {
/* 165 */       List localList = getFrames((JEditorPane)paramJTextComponent);
/* 166 */       ArrayList localArrayList = new ArrayList();
/*     */ 
/* 168 */       for (JEditorPane localJEditorPane : localList) {
/* 169 */         localArrayList.add((CountingPrintable)getPrintable(localJEditorPane, paramMessageFormat1, paramMessageFormat2));
/*     */       }
/*     */ 
/* 172 */       return new CompoundPrintable(localArrayList);
/*     */     }
/* 174 */     return new TextComponentPrintable(paramJTextComponent, paramMessageFormat1, paramMessageFormat2);
/*     */   }
/*     */ 
/*     */   private static boolean isFrameSetDocument(Document paramDocument)
/*     */   {
/* 187 */     boolean bool = false;
/* 188 */     if ((paramDocument instanceof HTMLDocument)) {
/* 189 */       HTMLDocument localHTMLDocument = (HTMLDocument)paramDocument;
/* 190 */       if (localHTMLDocument.getIterator(HTML.Tag.FRAME).isValid()) {
/* 191 */         bool = true;
/*     */       }
/*     */     }
/* 194 */     return bool;
/*     */   }
/*     */ 
/*     */   private static List<JEditorPane> getFrames(JEditorPane paramJEditorPane)
/*     */   {
/* 206 */     ArrayList localArrayList = new ArrayList();
/* 207 */     getFrames(paramJEditorPane, localArrayList);
/* 208 */     if (localArrayList.size() == 0)
/*     */     {
/* 211 */       createFrames(paramJEditorPane);
/* 212 */       getFrames(paramJEditorPane, localArrayList);
/*     */     }
/* 214 */     return localArrayList;
/*     */   }
/*     */ 
/*     */   private static void getFrames(Container paramContainer, List<JEditorPane> paramList)
/*     */   {
/* 227 */     for (Component localComponent : paramContainer.getComponents())
/* 228 */       if (((localComponent instanceof FrameEditorPaneTag)) && ((localComponent instanceof JEditorPane)))
/*     */       {
/* 230 */         paramList.add((JEditorPane)localComponent);
/*     */       }
/* 232 */       else if ((localComponent instanceof Container))
/* 233 */         getFrames((Container)localComponent, paramList);
/*     */   }
/*     */ 
/*     */   private static void createFrames(JEditorPane paramJEditorPane)
/*     */   {
/* 245 */     Runnable local1 = new Runnable()
/*     */     {
/*     */       public void run()
/*     */       {
/* 250 */         CellRendererPane localCellRendererPane = new CellRendererPane();
/* 251 */         localCellRendererPane.add(this.val$editor);
/*     */ 
/* 254 */         localCellRendererPane.setSize(500, 500);
/*     */       }
/*     */     };
/* 257 */     if (SwingUtilities.isEventDispatchThread())
/* 258 */       local1.run();
/*     */     else
/*     */       try {
/* 261 */         SwingUtilities.invokeAndWait(local1);
/*     */       } catch (Exception localException) {
/* 263 */         if ((localException instanceof RuntimeException)) {
/* 264 */           throw ((RuntimeException)localException);
/*     */         }
/* 266 */         throw new RuntimeException(localException);
/*     */       }
/*     */   }
/*     */ 
/*     */   private TextComponentPrintable(JTextComponent paramJTextComponent, MessageFormat paramMessageFormat1, MessageFormat paramMessageFormat2)
/*     */   {
/* 283 */     this.textComponentToPrint = paramJTextComponent;
/* 284 */     this.headerFormat = paramMessageFormat1;
/* 285 */     this.footerFormat = paramMessageFormat2;
/* 286 */     this.headerFont = paramJTextComponent.getFont().deriveFont(1, 18.0F);
/*     */ 
/* 288 */     this.footerFont = paramJTextComponent.getFont().deriveFont(0, 12.0F);
/*     */ 
/* 290 */     this.pagesMetrics = Collections.synchronizedList(new ArrayList());
/*     */ 
/* 292 */     this.rowsMetrics = new ArrayList(1000);
/* 293 */     this.printShell = createPrintShell(paramJTextComponent);
/*     */   }
/*     */ 
/*     */   private JTextComponent createPrintShell(final JTextComponent paramJTextComponent)
/*     */   {
/* 308 */     if (SwingUtilities.isEventDispatchThread()) {
/* 309 */       return createPrintShellOnEDT(paramJTextComponent);
/*     */     }
/* 311 */     FutureTask localFutureTask = new FutureTask(new Callable()
/*     */     {
/*     */       public JTextComponent call() throws Exception
/*     */       {
/* 315 */         return TextComponentPrintable.this.createPrintShellOnEDT(paramJTextComponent);
/*     */       }
/*     */     });
/* 318 */     SwingUtilities.invokeLater(localFutureTask);
/*     */     try {
/* 320 */       return (JTextComponent)localFutureTask.get();
/*     */     } catch (InterruptedException localInterruptedException) {
/* 322 */       throw new RuntimeException(localInterruptedException);
/*     */     } catch (ExecutionException localExecutionException) {
/* 324 */       Throwable localThrowable = localExecutionException.getCause();
/* 325 */       if ((localThrowable instanceof Error)) {
/* 326 */         throw ((Error)localThrowable);
/*     */       }
/* 328 */       if ((localThrowable instanceof RuntimeException)) {
/* 329 */         throw ((RuntimeException)localThrowable);
/*     */       }
/* 331 */       throw new AssertionError(localThrowable);
/*     */     }
/*     */   }
/*     */ 
/*     */   private JTextComponent createPrintShellOnEDT(final JTextComponent paramJTextComponent) {
/* 336 */     assert (SwingUtilities.isEventDispatchThread());
/*     */ 
/* 338 */     Object localObject = null;
/* 339 */     if ((paramJTextComponent instanceof JTextField)) {
/* 340 */       localObject = new JTextField()
/*     */       {
/*     */         public FontMetrics getFontMetrics(Font paramAnonymousFont)
/*     */         {
/* 348 */           return TextComponentPrintable.this.frc.get() == null ? super.getFontMetrics(paramAnonymousFont) : FontDesignMetrics.getMetrics(paramAnonymousFont, (FontRenderContext)TextComponentPrintable.this.frc.get());
/*     */         }
/*     */ 
/*     */       };
/*     */     }
/* 353 */     else if ((paramJTextComponent instanceof JTextArea)) {
/* 354 */       localObject = new JTextArea()
/*     */       {
/*     */         public FontMetrics getFontMetrics(Font paramAnonymousFont)
/*     */         {
/* 364 */           return TextComponentPrintable.this.frc.get() == null ? super.getFontMetrics(paramAnonymousFont) : FontDesignMetrics.getMetrics(paramAnonymousFont, (FontRenderContext)TextComponentPrintable.this.frc.get());
/*     */         }
/*     */ 
/*     */       };
/*     */     }
/* 369 */     else if ((paramJTextComponent instanceof JTextPane)) {
/* 370 */       localObject = new JTextPane()
/*     */       {
/*     */         public FontMetrics getFontMetrics(Font paramAnonymousFont)
/*     */         {
/* 374 */           return TextComponentPrintable.this.frc.get() == null ? super.getFontMetrics(paramAnonymousFont) : FontDesignMetrics.getMetrics(paramAnonymousFont, (FontRenderContext)TextComponentPrintable.this.frc.get());
/*     */         }
/*     */ 
/*     */         public EditorKit getEditorKit()
/*     */         {
/* 380 */           if (getDocument() == paramJTextComponent.getDocument()) {
/* 381 */             return ((JTextPane)paramJTextComponent).getEditorKit();
/*     */           }
/* 383 */           return super.getEditorKit();
/*     */         }
/*     */       };
/*     */     }
/* 387 */     else if ((paramJTextComponent instanceof JEditorPane)) {
/* 388 */       localObject = new JEditorPane()
/*     */       {
/*     */         public FontMetrics getFontMetrics(Font paramAnonymousFont)
/*     */         {
/* 392 */           return TextComponentPrintable.this.frc.get() == null ? super.getFontMetrics(paramAnonymousFont) : FontDesignMetrics.getMetrics(paramAnonymousFont, (FontRenderContext)TextComponentPrintable.this.frc.get());
/*     */         }
/*     */ 
/*     */         public EditorKit getEditorKit()
/*     */         {
/* 398 */           if (getDocument() == paramJTextComponent.getDocument()) {
/* 399 */             return ((JEditorPane)paramJTextComponent).getEditorKit();
/*     */           }
/* 401 */           return super.getEditorKit();
/*     */         }
/*     */ 
/*     */       };
/*     */     }
/*     */ 
/* 407 */     ((JTextComponent)localObject).setBorder(null);
/*     */ 
/* 410 */     ((JTextComponent)localObject).setOpaque(paramJTextComponent.isOpaque());
/* 411 */     ((JTextComponent)localObject).setEditable(paramJTextComponent.isEditable());
/* 412 */     ((JTextComponent)localObject).setEnabled(paramJTextComponent.isEnabled());
/* 413 */     ((JTextComponent)localObject).setFont(paramJTextComponent.getFont());
/* 414 */     ((JTextComponent)localObject).setBackground(paramJTextComponent.getBackground());
/* 415 */     ((JTextComponent)localObject).setForeground(paramJTextComponent.getForeground());
/* 416 */     ((JTextComponent)localObject).setComponentOrientation(paramJTextComponent.getComponentOrientation());
/*     */ 
/* 419 */     if ((localObject instanceof JEditorPane)) {
/* 420 */       ((JTextComponent)localObject).putClientProperty("JEditorPane.honorDisplayProperties", paramJTextComponent.getClientProperty("JEditorPane.honorDisplayProperties"));
/*     */ 
/* 423 */       ((JTextComponent)localObject).putClientProperty("JEditorPane.w3cLengthUnits", paramJTextComponent.getClientProperty("JEditorPane.w3cLengthUnits"));
/*     */ 
/* 425 */       ((JTextComponent)localObject).putClientProperty("charset", paramJTextComponent.getClientProperty("charset"));
/*     */     }
/*     */ 
/* 428 */     ((JTextComponent)localObject).setDocument(paramJTextComponent.getDocument());
/* 429 */     return localObject;
/*     */   }
/*     */ 
/*     */   public int getNumberOfPages()
/*     */   {
/* 443 */     return this.pagesMetrics.size();
/*     */   }
/*     */ 
/*     */   public int print(final Graphics paramGraphics, final PageFormat paramPageFormat, final int paramInt)
/*     */     throws PrinterException
/*     */   {
/* 458 */     if (!this.isLayouted) {
/* 459 */       if ((paramGraphics instanceof Graphics2D)) {
/* 460 */         this.frc.set(((Graphics2D)paramGraphics).getFontRenderContext());
/*     */       }
/* 462 */       layout((int)Math.floor(paramPageFormat.getImageableWidth()));
/* 463 */       calculateRowsMetrics();
/*     */     }
/*     */     int i;
/* 466 */     if (!SwingUtilities.isEventDispatchThread()) {
/* 467 */       Callable local7 = new Callable() {
/*     */         public Integer call() throws Exception {
/* 469 */           return Integer.valueOf(TextComponentPrintable.this.printOnEDT(paramGraphics, paramPageFormat, paramInt));
/*     */         }
/*     */       };
/* 472 */       FutureTask localFutureTask = new FutureTask(local7);
/*     */ 
/* 474 */       SwingUtilities.invokeLater(localFutureTask);
/*     */       try {
/* 476 */         i = ((Integer)localFutureTask.get()).intValue();
/*     */       } catch (InterruptedException localInterruptedException) {
/* 478 */         throw new RuntimeException(localInterruptedException);
/*     */       } catch (ExecutionException localExecutionException) {
/* 480 */         Throwable localThrowable = localExecutionException.getCause();
/* 481 */         if ((localThrowable instanceof PrinterException))
/* 482 */           throw ((PrinterException)localThrowable);
/* 483 */         if ((localThrowable instanceof RuntimeException))
/* 484 */           throw ((RuntimeException)localThrowable);
/* 485 */         if ((localThrowable instanceof Error)) {
/* 486 */           throw ((Error)localThrowable);
/*     */         }
/* 488 */         throw new RuntimeException(localThrowable);
/*     */       }
/*     */     }
/*     */     else {
/* 492 */       i = printOnEDT(paramGraphics, paramPageFormat, paramInt);
/*     */     }
/* 494 */     return i;
/*     */   }
/*     */ 
/*     */   private int printOnEDT(Graphics paramGraphics, PageFormat paramPageFormat, int paramInt)
/*     */     throws PrinterException
/*     */   {
/* 507 */     assert (SwingUtilities.isEventDispatchThread());
/* 508 */     Object localObject1 = BorderFactory.createEmptyBorder();
/*     */ 
/* 510 */     if ((this.headerFormat != null) || (this.footerFormat != null))
/*     */     {
/* 512 */       localObject2 = new Object[] { Integer.valueOf(paramInt + 1) };
/* 513 */       if (this.headerFormat != null) {
/* 514 */         localObject1 = new TitledBorder((Border)localObject1, this.headerFormat.format(localObject2), 2, 1, this.headerFont, this.printShell.getForeground());
/*     */       }
/*     */ 
/* 519 */       if (this.footerFormat != null) {
/* 520 */         localObject1 = new TitledBorder((Border)localObject1, this.footerFormat.format(localObject2), 2, 6, this.footerFont, this.printShell.getForeground());
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 526 */     Object localObject2 = ((Border)localObject1).getBorderInsets(this.printShell);
/* 527 */     updatePagesMetrics(paramInt, (int)Math.floor(paramPageFormat.getImageableHeight()) - ((Insets)localObject2).top - ((Insets)localObject2).bottom);
/*     */ 
/* 531 */     if (this.pagesMetrics.size() <= paramInt) {
/* 532 */       return 1;
/*     */     }
/*     */ 
/* 535 */     Graphics2D localGraphics2D = (Graphics2D)paramGraphics.create();
/*     */ 
/* 537 */     localGraphics2D.translate(paramPageFormat.getImageableX(), paramPageFormat.getImageableY());
/* 538 */     ((Border)localObject1).paintBorder(this.printShell, localGraphics2D, 0, 0, (int)Math.floor(paramPageFormat.getImageableWidth()), (int)Math.floor(paramPageFormat.getImageableHeight()));
/*     */ 
/* 542 */     localGraphics2D.translate(0, ((Insets)localObject2).top);
/*     */ 
/* 544 */     Rectangle localRectangle = new Rectangle(0, 0, (int)paramPageFormat.getWidth(), ((IntegerSegment)this.pagesMetrics.get(paramInt)).end - ((IntegerSegment)this.pagesMetrics.get(paramInt)).start + 1);
/*     */ 
/* 549 */     localGraphics2D.clip(localRectangle);
/* 550 */     int i = 0;
/* 551 */     if (ComponentOrientation.RIGHT_TO_LEFT == this.printShell.getComponentOrientation())
/*     */     {
/* 553 */       i = (int)paramPageFormat.getImageableWidth() - this.printShell.getWidth();
/*     */     }
/* 555 */     localGraphics2D.translate(i, -((IntegerSegment)this.pagesMetrics.get(paramInt)).start);
/* 556 */     this.printShell.print(localGraphics2D);
/*     */ 
/* 558 */     localGraphics2D.dispose();
/*     */ 
/* 560 */     return 0;
/*     */   }
/*     */ 
/*     */   private void releaseReadLock()
/*     */   {
/* 572 */     assert (!SwingUtilities.isEventDispatchThread());
/* 573 */     Document localDocument = this.textComponentToPrint.getDocument();
/* 574 */     if ((localDocument instanceof AbstractDocument))
/*     */       try {
/* 576 */         ((AbstractDocument)localDocument).readUnlock();
/* 577 */         this.needReadLock = true;
/*     */       }
/*     */       catch (Error localError)
/*     */       {
/*     */       }
/*     */   }
/*     */ 
/*     */   private void acquireReadLock()
/*     */   {
/* 592 */     assert (!SwingUtilities.isEventDispatchThread());
/* 593 */     if (this.needReadLock)
/*     */     {
/*     */       try
/*     */       {
/* 600 */         SwingUtilities.invokeAndWait(new Runnable() {
/*     */           public void run() {
/*     */           } } );
/*     */       }
/*     */       catch (InterruptedException localInterruptedException) {
/*     */       }
/*     */       catch (InvocationTargetException localInvocationTargetException) {
/*     */       }
/* 608 */       Document localDocument = this.textComponentToPrint.getDocument();
/* 609 */       ((AbstractDocument)localDocument).readLock();
/* 610 */       this.needReadLock = false;
/*     */     }
/*     */   }
/*     */ 
/*     */   private void layout(final int paramInt)
/*     */   {
/* 631 */     if (!SwingUtilities.isEventDispatchThread()) {
/* 632 */       Callable local9 = new Callable() {
/*     */         public Object call() throws Exception {
/* 634 */           TextComponentPrintable.this.layoutOnEDT(paramInt);
/* 635 */           return null;
/*     */         }
/*     */       };
/* 638 */       FutureTask localFutureTask = new FutureTask(local9);
/*     */ 
/* 645 */       releaseReadLock();
/* 646 */       SwingUtilities.invokeLater(localFutureTask);
/*     */       try {
/* 648 */         localFutureTask.get();
/*     */       } catch (InterruptedException localInterruptedException) {
/* 650 */         throw new RuntimeException(localInterruptedException);
/*     */       } catch (ExecutionException localExecutionException) {
/* 652 */         Throwable localThrowable = localExecutionException.getCause();
/* 653 */         if ((localThrowable instanceof RuntimeException))
/* 654 */           throw ((RuntimeException)localThrowable);
/* 655 */         if ((localThrowable instanceof Error)) {
/* 656 */           throw ((Error)localThrowable);
/*     */         }
/* 658 */         throw new RuntimeException(localThrowable);
/*     */       }
/*     */       finally {
/* 661 */         acquireReadLock();
/*     */       }
/*     */     } else {
/* 664 */       layoutOnEDT(paramInt);
/*     */     }
/*     */ 
/* 667 */     this.isLayouted = true;
/*     */   }
/*     */ 
/*     */   private void layoutOnEDT(int paramInt)
/*     */   {
/* 676 */     assert (SwingUtilities.isEventDispatchThread());
/*     */ 
/* 681 */     CellRendererPane localCellRendererPane = new CellRendererPane();
/*     */ 
/* 685 */     JViewport localJViewport = new JViewport();
/* 686 */     localJViewport.setBorder(null);
/* 687 */     Dimension localDimension = new Dimension(paramInt, 2147482647);
/*     */ 
/* 691 */     if ((this.printShell instanceof JTextField)) {
/* 692 */       localDimension = new Dimension(localDimension.width, this.printShell.getPreferredSize().height);
/*     */     }
/*     */ 
/* 695 */     this.printShell.setSize(localDimension);
/* 696 */     localJViewport.setComponentOrientation(this.printShell.getComponentOrientation());
/* 697 */     localJViewport.setSize(localDimension);
/* 698 */     localJViewport.add(this.printShell);
/* 699 */     localCellRendererPane.add(localJViewport);
/*     */   }
/*     */ 
/*     */   private void updatePagesMetrics(int paramInt1, int paramInt2)
/*     */   {
/* 711 */     while ((paramInt1 >= this.pagesMetrics.size()) && (!this.rowsMetrics.isEmpty()))
/*     */     {
/* 713 */       int i = this.pagesMetrics.size() - 1;
/* 714 */       int j = i >= 0 ? ((IntegerSegment)this.pagesMetrics.get(i)).end + 1 : 0;
/*     */ 
/* 718 */       int k = 0;
/*     */ 
/* 720 */       while ((k < this.rowsMetrics.size()) && (((IntegerSegment)this.rowsMetrics.get(k)).end - j + 1 <= paramInt2))
/*     */       {
/* 722 */         k++;
/*     */       }
/* 724 */       if (k == 0)
/*     */       {
/* 727 */         this.pagesMetrics.add(new IntegerSegment(j, j + paramInt2 - 1));
/*     */       }
/*     */       else {
/* 730 */         k--;
/* 731 */         this.pagesMetrics.add(new IntegerSegment(j, ((IntegerSegment)this.rowsMetrics.get(k)).end));
/*     */ 
/* 733 */         for (int m = 0; m <= k; m++)
/* 734 */           this.rowsMetrics.remove(0);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private void calculateRowsMetrics()
/*     */   {
/* 749 */     int i = this.printShell.getDocument().getLength();
/* 750 */     ArrayList localArrayList = new ArrayList(1000);
/*     */ 
/* 752 */     int j = 0; int k = -1; for (int m = -1; j < i; 
/* 753 */       j++) {
/*     */       try {
/* 755 */         Rectangle localRectangle = this.printShell.modelToView(j);
/* 756 */         if (localRectangle != null) {
/* 757 */           int n = (int)localRectangle.getY();
/* 758 */           int i1 = (int)localRectangle.getHeight();
/* 759 */           if ((i1 != 0) && ((n != k) || (i1 != m)))
/*     */           {
/* 766 */             k = n;
/* 767 */             m = i1;
/* 768 */             localArrayList.add(new IntegerSegment(n, n + i1 - 1));
/*     */           }
/*     */         }
/*     */       } catch (BadLocationException localBadLocationException) {
/* 772 */         if (!$assertionsDisabled) throw new AssertionError();
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 778 */     Collections.sort(localArrayList);
/* 779 */     j = -2147483648;
/* 780 */     k = -2147483648;
/* 781 */     for (IntegerSegment localIntegerSegment : localArrayList) {
/* 782 */       if (k < localIntegerSegment.start) {
/* 783 */         if (k != -2147483648) {
/* 784 */           this.rowsMetrics.add(new IntegerSegment(j, k));
/*     */         }
/* 786 */         j = localIntegerSegment.start;
/* 787 */         k = localIntegerSegment.end;
/*     */       } else {
/* 789 */         k = localIntegerSegment.end;
/*     */       }
/*     */     }
/* 792 */     if (k != -2147483648)
/* 793 */       this.rowsMetrics.add(new IntegerSegment(j, k));
/*     */   }
/*     */ 
/*     */   private static class IntegerSegment
/*     */     implements Comparable<IntegerSegment>
/*     */   {
/*     */     final int start;
/*     */     final int end;
/*     */ 
/*     */     IntegerSegment(int paramInt1, int paramInt2)
/*     */     {
/* 807 */       this.start = paramInt1;
/* 808 */       this.end = paramInt2;
/*     */     }
/*     */ 
/*     */     public int compareTo(IntegerSegment paramIntegerSegment) {
/* 812 */       int i = this.start - paramIntegerSegment.start;
/* 813 */       return i != 0 ? i : this.end - paramIntegerSegment.end;
/*     */     }
/*     */ 
/*     */     public boolean equals(Object paramObject)
/*     */     {
/* 818 */       if ((paramObject instanceof IntegerSegment)) {
/* 819 */         return compareTo((IntegerSegment)paramObject) == 0;
/*     */       }
/* 821 */       return false;
/*     */     }
/*     */ 
/*     */     public int hashCode()
/*     */     {
/* 828 */       int i = 17;
/* 829 */       i = 37 * i + this.start;
/* 830 */       i = 37 * i + this.end;
/* 831 */       return i;
/*     */     }
/*     */ 
/*     */     public String toString()
/*     */     {
/* 836 */       return "IntegerSegment [" + this.start + ", " + this.end + "]";
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.swing.text.TextComponentPrintable
 * JD-Core Version:    0.6.2
 */