/*      */ package javax.swing.plaf.basic;
/*      */ 
/*      */ import java.awt.BasicStroke;
/*      */ import java.awt.Color;
/*      */ import java.awt.Component.BaselineResizeBehavior;
/*      */ import java.awt.Dimension;
/*      */ import java.awt.Font;
/*      */ import java.awt.FontMetrics;
/*      */ import java.awt.Graphics;
/*      */ import java.awt.Graphics2D;
/*      */ import java.awt.Insets;
/*      */ import java.awt.Point;
/*      */ import java.awt.Rectangle;
/*      */ import java.awt.event.ActionEvent;
/*      */ import java.awt.event.ActionListener;
/*      */ import java.awt.event.HierarchyEvent;
/*      */ import java.awt.event.HierarchyListener;
/*      */ import java.awt.geom.AffineTransform;
/*      */ import java.beans.PropertyChangeEvent;
/*      */ import java.beans.PropertyChangeListener;
/*      */ import javax.swing.BoundedRangeModel;
/*      */ import javax.swing.JComponent;
/*      */ import javax.swing.JProgressBar;
/*      */ import javax.swing.LookAndFeel;
/*      */ import javax.swing.SwingUtilities;
/*      */ import javax.swing.Timer;
/*      */ import javax.swing.UIManager;
/*      */ import javax.swing.event.ChangeEvent;
/*      */ import javax.swing.event.ChangeListener;
/*      */ import javax.swing.plaf.ComponentUI;
/*      */ import javax.swing.plaf.ProgressBarUI;
/*      */ import sun.swing.DefaultLookup;
/*      */ import sun.swing.SwingUtilities2;
/*      */ 
/*      */ public class BasicProgressBarUI extends ProgressBarUI
/*      */ {
/*      */   private int cachedPercent;
/*      */   private int cellLength;
/*      */   private int cellSpacing;
/*      */   private Color selectionForeground;
/*      */   private Color selectionBackground;
/*      */   private Animator animator;
/*      */   protected JProgressBar progressBar;
/*      */   protected ChangeListener changeListener;
/*      */   private Handler handler;
/*      */   private int animationIndex;
/*      */   private int numFrames;
/*      */   private int repaintInterval;
/*      */   private int cycleTime;
/*   97 */   private static boolean ADJUSTTIMER = true;
/*      */   protected Rectangle boxRect;
/*      */   private Rectangle nextPaintRect;
/*      */   private Rectangle componentInnards;
/*      */   private Rectangle oldComponentInnards;
/*      */   private double delta;
/*      */   private int maxPosition;
/*      */ 
/*      */   public BasicProgressBarUI()
/*      */   {
/*   68 */     this.animationIndex = 0;
/*      */ 
/*  126 */     this.delta = 0.0D;
/*      */ 
/*  128 */     this.maxPosition = 0;
/*      */   }
/*      */ 
/*      */   public static ComponentUI createUI(JComponent paramJComponent) {
/*  132 */     return new BasicProgressBarUI();
/*      */   }
/*      */ 
/*      */   public void installUI(JComponent paramJComponent) {
/*  136 */     this.progressBar = ((JProgressBar)paramJComponent);
/*  137 */     installDefaults();
/*  138 */     installListeners();
/*  139 */     if (this.progressBar.isIndeterminate())
/*  140 */       initIndeterminateValues();
/*      */   }
/*      */ 
/*      */   public void uninstallUI(JComponent paramJComponent)
/*      */   {
/*  145 */     if (this.progressBar.isIndeterminate()) {
/*  146 */       cleanUpIndeterminateValues();
/*      */     }
/*  148 */     uninstallDefaults();
/*  149 */     uninstallListeners();
/*  150 */     this.progressBar = null;
/*      */   }
/*      */ 
/*      */   protected void installDefaults() {
/*  154 */     LookAndFeel.installProperty(this.progressBar, "opaque", Boolean.TRUE);
/*  155 */     LookAndFeel.installBorder(this.progressBar, "ProgressBar.border");
/*  156 */     LookAndFeel.installColorsAndFont(this.progressBar, "ProgressBar.background", "ProgressBar.foreground", "ProgressBar.font");
/*      */ 
/*  160 */     this.cellLength = UIManager.getInt("ProgressBar.cellLength");
/*  161 */     if (this.cellLength == 0) this.cellLength = 1;
/*  162 */     this.cellSpacing = UIManager.getInt("ProgressBar.cellSpacing");
/*  163 */     this.selectionForeground = UIManager.getColor("ProgressBar.selectionForeground");
/*  164 */     this.selectionBackground = UIManager.getColor("ProgressBar.selectionBackground");
/*      */   }
/*      */ 
/*      */   protected void uninstallDefaults() {
/*  168 */     LookAndFeel.uninstallBorder(this.progressBar);
/*      */   }
/*      */ 
/*      */   protected void installListeners()
/*      */   {
/*  173 */     this.changeListener = getHandler();
/*  174 */     this.progressBar.addChangeListener(this.changeListener);
/*      */ 
/*  177 */     this.progressBar.addPropertyChangeListener(getHandler());
/*      */   }
/*      */ 
/*      */   private Handler getHandler() {
/*  181 */     if (this.handler == null) {
/*  182 */       this.handler = new Handler(null);
/*      */     }
/*  184 */     return this.handler;
/*      */   }
/*      */ 
/*      */   protected void startAnimationTimer()
/*      */   {
/*  205 */     if (this.animator == null) {
/*  206 */       this.animator = new Animator(null);
/*      */     }
/*      */ 
/*  209 */     this.animator.start(getRepaintInterval());
/*      */   }
/*      */ 
/*      */   protected void stopAnimationTimer()
/*      */   {
/*  228 */     if (this.animator != null)
/*  229 */       this.animator.stop();
/*      */   }
/*      */ 
/*      */   protected void uninstallListeners()
/*      */   {
/*  237 */     this.progressBar.removeChangeListener(this.changeListener);
/*  238 */     this.progressBar.removePropertyChangeListener(getHandler());
/*  239 */     this.handler = null;
/*      */   }
/*      */ 
/*      */   public int getBaseline(JComponent paramJComponent, int paramInt1, int paramInt2)
/*      */   {
/*  252 */     super.getBaseline(paramJComponent, paramInt1, paramInt2);
/*  253 */     if ((this.progressBar.isStringPainted()) && (this.progressBar.getOrientation() == 0))
/*      */     {
/*  255 */       FontMetrics localFontMetrics = this.progressBar.getFontMetrics(this.progressBar.getFont());
/*      */ 
/*  257 */       Insets localInsets = this.progressBar.getInsets();
/*  258 */       int i = localInsets.top;
/*  259 */       paramInt2 = paramInt2 - localInsets.top - localInsets.bottom;
/*  260 */       return i + (paramInt2 + localFontMetrics.getAscent() - localFontMetrics.getLeading() - localFontMetrics.getDescent()) / 2;
/*      */     }
/*      */ 
/*  264 */     return -1;
/*      */   }
/*      */ 
/*      */   public Component.BaselineResizeBehavior getBaselineResizeBehavior(JComponent paramJComponent)
/*      */   {
/*  277 */     super.getBaselineResizeBehavior(paramJComponent);
/*  278 */     if ((this.progressBar.isStringPainted()) && (this.progressBar.getOrientation() == 0))
/*      */     {
/*  280 */       return Component.BaselineResizeBehavior.CENTER_OFFSET;
/*      */     }
/*  282 */     return Component.BaselineResizeBehavior.OTHER;
/*      */   }
/*      */ 
/*      */   protected Dimension getPreferredInnerHorizontal()
/*      */   {
/*  295 */     Dimension localDimension = (Dimension)DefaultLookup.get(this.progressBar, this, "ProgressBar.horizontalSize");
/*      */ 
/*  297 */     if (localDimension == null) {
/*  298 */       localDimension = new Dimension(146, 12);
/*      */     }
/*  300 */     return localDimension;
/*      */   }
/*      */ 
/*      */   protected Dimension getPreferredInnerVertical() {
/*  304 */     Dimension localDimension = (Dimension)DefaultLookup.get(this.progressBar, this, "ProgressBar.verticalSize");
/*      */ 
/*  306 */     if (localDimension == null) {
/*  307 */       localDimension = new Dimension(12, 146);
/*      */     }
/*  309 */     return localDimension;
/*      */   }
/*      */ 
/*      */   protected Color getSelectionForeground()
/*      */   {
/*  317 */     return this.selectionForeground;
/*      */   }
/*      */ 
/*      */   protected Color getSelectionBackground()
/*      */   {
/*  325 */     return this.selectionBackground;
/*      */   }
/*      */ 
/*      */   private int getCachedPercent() {
/*  329 */     return this.cachedPercent;
/*      */   }
/*      */ 
/*      */   private void setCachedPercent(int paramInt) {
/*  333 */     this.cachedPercent = paramInt;
/*      */   }
/*      */ 
/*      */   protected int getCellLength()
/*      */   {
/*  348 */     if (this.progressBar.isStringPainted()) {
/*  349 */       return 1;
/*      */     }
/*  351 */     return this.cellLength;
/*      */   }
/*      */ 
/*      */   protected void setCellLength(int paramInt)
/*      */   {
/*  356 */     this.cellLength = paramInt;
/*      */   }
/*      */ 
/*      */   protected int getCellSpacing()
/*      */   {
/*  370 */     if (this.progressBar.isStringPainted()) {
/*  371 */       return 0;
/*      */     }
/*  373 */     return this.cellSpacing;
/*      */   }
/*      */ 
/*      */   protected void setCellSpacing(int paramInt)
/*      */   {
/*  378 */     this.cellSpacing = paramInt;
/*      */   }
/*      */ 
/*      */   protected int getAmountFull(Insets paramInsets, int paramInt1, int paramInt2)
/*      */   {
/*  389 */     int i = 0;
/*  390 */     BoundedRangeModel localBoundedRangeModel = this.progressBar.getModel();
/*      */ 
/*  392 */     if (localBoundedRangeModel.getMaximum() - localBoundedRangeModel.getMinimum() != 0) {
/*  393 */       if (this.progressBar.getOrientation() == 0) {
/*  394 */         i = (int)Math.round(paramInt1 * this.progressBar.getPercentComplete());
/*      */       }
/*      */       else {
/*  397 */         i = (int)Math.round(paramInt2 * this.progressBar.getPercentComplete());
/*      */       }
/*      */     }
/*      */ 
/*  401 */     return i;
/*      */   }
/*      */ 
/*      */   public void paint(Graphics paramGraphics, JComponent paramJComponent)
/*      */   {
/*  409 */     if (this.progressBar.isIndeterminate())
/*  410 */       paintIndeterminate(paramGraphics, paramJComponent);
/*      */     else
/*  412 */       paintDeterminate(paramGraphics, paramJComponent);
/*      */   }
/*      */ 
/*      */   protected Rectangle getBox(Rectangle paramRectangle)
/*      */   {
/*  441 */     int i = getAnimationIndex();
/*  442 */     int j = this.numFrames / 2;
/*      */ 
/*  444 */     if ((sizeChanged()) || (this.delta == 0.0D) || (this.maxPosition == 0.0D)) {
/*  445 */       updateSizes();
/*      */     }
/*      */ 
/*  448 */     paramRectangle = getGenericBox(paramRectangle);
/*      */ 
/*  450 */     if (paramRectangle == null) {
/*  451 */       return null;
/*      */     }
/*  453 */     if (j <= 0) {
/*  454 */       return null;
/*      */     }
/*      */ 
/*  458 */     if (this.progressBar.getOrientation() == 0) {
/*  459 */       if (i < j) {
/*  460 */         paramRectangle.x = (this.componentInnards.x + (int)Math.round(this.delta * i));
/*      */       }
/*      */       else {
/*  463 */         paramRectangle.x = (this.maxPosition - (int)Math.round(this.delta * (i - j)));
/*      */       }
/*      */ 
/*      */     }
/*  468 */     else if (i < j) {
/*  469 */       paramRectangle.y = (this.componentInnards.y + (int)Math.round(this.delta * i));
/*      */     }
/*      */     else {
/*  472 */       paramRectangle.y = (this.maxPosition - (int)Math.round(this.delta * (i - j)));
/*      */     }
/*      */ 
/*  477 */     return paramRectangle;
/*      */   }
/*      */ 
/*      */   private void updateSizes()
/*      */   {
/*  485 */     int i = 0;
/*      */ 
/*  487 */     if (this.progressBar.getOrientation() == 0) {
/*  488 */       i = getBoxLength(this.componentInnards.width, this.componentInnards.height);
/*      */ 
/*  490 */       this.maxPosition = (this.componentInnards.x + this.componentInnards.width - i);
/*      */     }
/*      */     else
/*      */     {
/*  494 */       i = getBoxLength(this.componentInnards.height, this.componentInnards.width);
/*      */ 
/*  496 */       this.maxPosition = (this.componentInnards.y + this.componentInnards.height - i);
/*      */     }
/*      */ 
/*  501 */     this.delta = (2.0D * this.maxPosition / this.numFrames);
/*      */   }
/*      */ 
/*      */   private Rectangle getGenericBox(Rectangle paramRectangle)
/*      */   {
/*  508 */     if (paramRectangle == null) {
/*  509 */       paramRectangle = new Rectangle();
/*      */     }
/*      */ 
/*  512 */     if (this.progressBar.getOrientation() == 0) {
/*  513 */       paramRectangle.width = getBoxLength(this.componentInnards.width, this.componentInnards.height);
/*      */ 
/*  515 */       if (paramRectangle.width < 0) {
/*  516 */         paramRectangle = null;
/*      */       } else {
/*  518 */         paramRectangle.height = this.componentInnards.height;
/*  519 */         paramRectangle.y = this.componentInnards.y;
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/*  524 */       paramRectangle.height = getBoxLength(this.componentInnards.height, this.componentInnards.width);
/*      */ 
/*  526 */       if (paramRectangle.height < 0) {
/*  527 */         paramRectangle = null;
/*      */       } else {
/*  529 */         paramRectangle.width = this.componentInnards.width;
/*  530 */         paramRectangle.x = this.componentInnards.x;
/*      */       }
/*      */     }
/*      */ 
/*  534 */     return paramRectangle;
/*      */   }
/*      */ 
/*      */   protected int getBoxLength(int paramInt1, int paramInt2)
/*      */   {
/*  571 */     return (int)Math.round(paramInt1 / 6.0D);
/*      */   }
/*      */ 
/*      */   protected void paintIndeterminate(Graphics paramGraphics, JComponent paramJComponent)
/*      */   {
/*  585 */     if (!(paramGraphics instanceof Graphics2D)) {
/*  586 */       return;
/*      */     }
/*      */ 
/*  589 */     Insets localInsets = this.progressBar.getInsets();
/*  590 */     int i = this.progressBar.getWidth() - (localInsets.right + localInsets.left);
/*  591 */     int j = this.progressBar.getHeight() - (localInsets.top + localInsets.bottom);
/*      */ 
/*  593 */     if ((i <= 0) || (j <= 0)) {
/*  594 */       return;
/*      */     }
/*      */ 
/*  597 */     Graphics2D localGraphics2D = (Graphics2D)paramGraphics;
/*      */ 
/*  600 */     this.boxRect = getBox(this.boxRect);
/*  601 */     if (this.boxRect != null) {
/*  602 */       localGraphics2D.setColor(this.progressBar.getForeground());
/*  603 */       localGraphics2D.fillRect(this.boxRect.x, this.boxRect.y, this.boxRect.width, this.boxRect.height);
/*      */     }
/*      */ 
/*  608 */     if (this.progressBar.isStringPainted())
/*  609 */       if (this.progressBar.getOrientation() == 0) {
/*  610 */         paintString(localGraphics2D, localInsets.left, localInsets.top, i, j, this.boxRect.x, this.boxRect.width, localInsets);
/*      */       }
/*      */       else
/*      */       {
/*  615 */         paintString(localGraphics2D, localInsets.left, localInsets.top, i, j, this.boxRect.y, this.boxRect.height, localInsets);
/*      */       }
/*      */   }
/*      */ 
/*      */   protected void paintDeterminate(Graphics paramGraphics, JComponent paramJComponent)
/*      */   {
/*  636 */     if (!(paramGraphics instanceof Graphics2D)) {
/*  637 */       return;
/*      */     }
/*      */ 
/*  640 */     Insets localInsets = this.progressBar.getInsets();
/*  641 */     int i = this.progressBar.getWidth() - (localInsets.right + localInsets.left);
/*  642 */     int j = this.progressBar.getHeight() - (localInsets.top + localInsets.bottom);
/*      */ 
/*  644 */     if ((i <= 0) || (j <= 0)) {
/*  645 */       return;
/*      */     }
/*      */ 
/*  648 */     int k = getCellLength();
/*  649 */     int m = getCellSpacing();
/*      */ 
/*  651 */     int n = getAmountFull(localInsets, i, j);
/*      */ 
/*  653 */     Graphics2D localGraphics2D = (Graphics2D)paramGraphics;
/*  654 */     localGraphics2D.setColor(this.progressBar.getForeground());
/*      */ 
/*  656 */     if (this.progressBar.getOrientation() == 0)
/*      */     {
/*  658 */       if ((m == 0) && (n > 0))
/*      */       {
/*  660 */         localGraphics2D.setStroke(new BasicStroke(j, 0, 2));
/*      */       }
/*      */       else
/*      */       {
/*  664 */         localGraphics2D.setStroke(new BasicStroke(j, 0, 2, 0.0F, new float[] { k, m }, 0.0F));
/*      */       }
/*      */ 
/*  669 */       if (BasicGraphicsUtils.isLeftToRight(paramJComponent)) {
/*  670 */         localGraphics2D.drawLine(localInsets.left, j / 2 + localInsets.top, n + localInsets.left, j / 2 + localInsets.top);
/*      */       }
/*      */       else {
/*  673 */         localGraphics2D.drawLine(i + localInsets.left, j / 2 + localInsets.top, i + localInsets.left - n, j / 2 + localInsets.top);
/*      */       }
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/*  681 */       if ((m == 0) && (n > 0))
/*      */       {
/*  683 */         localGraphics2D.setStroke(new BasicStroke(i, 0, 2));
/*      */       }
/*      */       else
/*      */       {
/*  687 */         localGraphics2D.setStroke(new BasicStroke(i, 0, 2, 0.0F, new float[] { k, m }, 0.0F));
/*      */       }
/*      */ 
/*  692 */       localGraphics2D.drawLine(i / 2 + localInsets.left, localInsets.top + j, i / 2 + localInsets.left, localInsets.top + j - n);
/*      */     }
/*      */ 
/*  699 */     if (this.progressBar.isStringPainted())
/*  700 */       paintString(paramGraphics, localInsets.left, localInsets.top, i, j, n, localInsets);
/*      */   }
/*      */ 
/*      */   protected void paintString(Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, Insets paramInsets)
/*      */   {
/*  710 */     if (this.progressBar.getOrientation() == 0) {
/*  711 */       if (BasicGraphicsUtils.isLeftToRight(this.progressBar)) {
/*  712 */         if (this.progressBar.isIndeterminate()) {
/*  713 */           this.boxRect = getBox(this.boxRect);
/*  714 */           paintString(paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, this.boxRect.x, this.boxRect.width, paramInsets);
/*      */         }
/*      */         else {
/*  717 */           paintString(paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, paramInt1, paramInt5, paramInsets);
/*      */         }
/*      */       }
/*      */       else {
/*  721 */         paintString(paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, paramInt1 + paramInt3 - paramInt5, paramInt5, paramInsets);
/*      */       }
/*      */ 
/*      */     }
/*  726 */     else if (this.progressBar.isIndeterminate()) {
/*  727 */       this.boxRect = getBox(this.boxRect);
/*  728 */       paintString(paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, this.boxRect.y, this.boxRect.height, paramInsets);
/*      */     }
/*      */     else {
/*  731 */       paintString(paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, paramInt2 + paramInt4 - paramInt5, paramInt5, paramInsets);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void paintString(Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, Insets paramInsets)
/*      */   {
/*  753 */     if (!(paramGraphics instanceof Graphics2D)) {
/*  754 */       return;
/*      */     }
/*      */ 
/*  757 */     Graphics2D localGraphics2D = (Graphics2D)paramGraphics;
/*  758 */     String str = this.progressBar.getString();
/*  759 */     localGraphics2D.setFont(this.progressBar.getFont());
/*  760 */     Point localPoint = getStringPlacement(localGraphics2D, str, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */ 
/*  762 */     Rectangle localRectangle = localGraphics2D.getClipBounds();
/*      */ 
/*  764 */     if (this.progressBar.getOrientation() == 0) {
/*  765 */       localGraphics2D.setColor(getSelectionBackground());
/*  766 */       SwingUtilities2.drawString(this.progressBar, localGraphics2D, str, localPoint.x, localPoint.y);
/*      */ 
/*  768 */       localGraphics2D.setColor(getSelectionForeground());
/*  769 */       localGraphics2D.clipRect(paramInt5, paramInt2, paramInt6, paramInt4);
/*  770 */       SwingUtilities2.drawString(this.progressBar, localGraphics2D, str, localPoint.x, localPoint.y);
/*      */     }
/*      */     else {
/*  773 */       localGraphics2D.setColor(getSelectionBackground());
/*  774 */       AffineTransform localAffineTransform = AffineTransform.getRotateInstance(1.570796326794897D);
/*      */ 
/*  776 */       localGraphics2D.setFont(this.progressBar.getFont().deriveFont(localAffineTransform));
/*  777 */       localPoint = getStringPlacement(localGraphics2D, str, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */ 
/*  779 */       SwingUtilities2.drawString(this.progressBar, localGraphics2D, str, localPoint.x, localPoint.y);
/*      */ 
/*  781 */       localGraphics2D.setColor(getSelectionForeground());
/*  782 */       localGraphics2D.clipRect(paramInt1, paramInt5, paramInt3, paramInt6);
/*  783 */       SwingUtilities2.drawString(this.progressBar, localGraphics2D, str, localPoint.x, localPoint.y);
/*      */     }
/*      */ 
/*  786 */     localGraphics2D.setClip(localRectangle);
/*      */   }
/*      */ 
/*      */   protected Point getStringPlacement(Graphics paramGraphics, String paramString, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/*  799 */     FontMetrics localFontMetrics = SwingUtilities2.getFontMetrics(this.progressBar, paramGraphics, this.progressBar.getFont());
/*      */ 
/*  801 */     int i = SwingUtilities2.stringWidth(this.progressBar, localFontMetrics, paramString);
/*      */ 
/*  804 */     if (this.progressBar.getOrientation() == 0) {
/*  805 */       return new Point(paramInt1 + Math.round(paramInt3 / 2 - i / 2), paramInt2 + (paramInt4 + localFontMetrics.getAscent() - localFontMetrics.getLeading() - localFontMetrics.getDescent()) / 2);
/*      */     }
/*      */ 
/*  811 */     return new Point(paramInt1 + (paramInt3 - localFontMetrics.getAscent() + localFontMetrics.getLeading() + localFontMetrics.getDescent()) / 2, paramInt2 + Math.round(paramInt4 / 2 - i / 2));
/*      */   }
/*      */ 
/*      */   public Dimension getPreferredSize(JComponent paramJComponent)
/*      */   {
/*  820 */     Insets localInsets = this.progressBar.getInsets();
/*  821 */     FontMetrics localFontMetrics = this.progressBar.getFontMetrics(this.progressBar.getFont());
/*      */     Dimension localDimension;
/*      */     String str;
/*      */     int i;
/*      */     int j;
/*  824 */     if (this.progressBar.getOrientation() == 0) {
/*  825 */       localDimension = new Dimension(getPreferredInnerHorizontal());
/*      */ 
/*  827 */       if (this.progressBar.isStringPainted())
/*      */       {
/*  829 */         str = this.progressBar.getString();
/*  830 */         i = SwingUtilities2.stringWidth(this.progressBar, localFontMetrics, str);
/*      */ 
/*  832 */         if (i > localDimension.width) {
/*  833 */           localDimension.width = i;
/*      */         }
/*      */ 
/*  840 */         j = localFontMetrics.getHeight() + localFontMetrics.getDescent();
/*      */ 
/*  842 */         if (j > localDimension.height)
/*  843 */           localDimension.height = j;
/*      */       }
/*      */     }
/*      */     else {
/*  847 */       localDimension = new Dimension(getPreferredInnerVertical());
/*      */ 
/*  849 */       if (this.progressBar.isStringPainted()) {
/*  850 */         str = this.progressBar.getString();
/*  851 */         i = localFontMetrics.getHeight() + localFontMetrics.getDescent();
/*      */ 
/*  853 */         if (i > localDimension.width) {
/*  854 */           localDimension.width = i;
/*      */         }
/*      */ 
/*  857 */         j = SwingUtilities2.stringWidth(this.progressBar, localFontMetrics, str);
/*      */ 
/*  859 */         if (j > localDimension.height) {
/*  860 */           localDimension.height = j;
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  865 */     localDimension.width += localInsets.left + localInsets.right;
/*  866 */     localDimension.height += localInsets.top + localInsets.bottom;
/*  867 */     return localDimension;
/*      */   }
/*      */ 
/*      */   public Dimension getMinimumSize(JComponent paramJComponent)
/*      */   {
/*  875 */     Dimension localDimension = getPreferredSize(this.progressBar);
/*  876 */     if (this.progressBar.getOrientation() == 0)
/*  877 */       localDimension.width = 10;
/*      */     else {
/*  879 */       localDimension.height = 10;
/*      */     }
/*  881 */     return localDimension;
/*      */   }
/*      */ 
/*      */   public Dimension getMaximumSize(JComponent paramJComponent) {
/*  885 */     Dimension localDimension = getPreferredSize(this.progressBar);
/*  886 */     if (this.progressBar.getOrientation() == 0)
/*  887 */       localDimension.width = 32767;
/*      */     else {
/*  889 */       localDimension.height = 32767;
/*      */     }
/*  891 */     return localDimension;
/*      */   }
/*      */ 
/*      */   protected int getAnimationIndex()
/*      */   {
/*  900 */     return this.animationIndex;
/*      */   }
/*      */ 
/*      */   protected final int getFrameCount()
/*      */   {
/*  913 */     return this.numFrames;
/*      */   }
/*      */ 
/*      */   protected void setAnimationIndex(int paramInt)
/*      */   {
/*  932 */     if (this.animationIndex != paramInt) {
/*  933 */       if (sizeChanged()) {
/*  934 */         this.animationIndex = paramInt;
/*  935 */         this.maxPosition = 0;
/*  936 */         this.delta = 0.0D;
/*  937 */         this.progressBar.repaint();
/*  938 */         return;
/*      */       }
/*      */ 
/*  942 */       this.nextPaintRect = getBox(this.nextPaintRect);
/*      */ 
/*  945 */       this.animationIndex = paramInt;
/*      */ 
/*  948 */       if (this.nextPaintRect != null) {
/*  949 */         this.boxRect = getBox(this.boxRect);
/*  950 */         if (this.boxRect != null)
/*  951 */           this.nextPaintRect.add(this.boxRect);
/*      */       }
/*      */     }
/*      */     else {
/*  955 */       return;
/*      */     }
/*      */ 
/*  958 */     if (this.nextPaintRect != null)
/*  959 */       this.progressBar.repaint(this.nextPaintRect);
/*      */     else
/*  961 */       this.progressBar.repaint();
/*      */   }
/*      */ 
/*      */   private boolean sizeChanged()
/*      */   {
/*  966 */     if ((this.oldComponentInnards == null) || (this.componentInnards == null)) {
/*  967 */       return true;
/*      */     }
/*      */ 
/*  970 */     this.oldComponentInnards.setRect(this.componentInnards);
/*  971 */     this.componentInnards = SwingUtilities.calculateInnerArea(this.progressBar, this.componentInnards);
/*      */ 
/*  973 */     return !this.oldComponentInnards.equals(this.componentInnards);
/*      */   }
/*      */ 
/*      */   protected void incrementAnimationIndex()
/*      */   {
/*  997 */     int i = getAnimationIndex() + 1;
/*      */ 
/*  999 */     if (i < this.numFrames)
/* 1000 */       setAnimationIndex(i);
/*      */     else
/* 1002 */       setAnimationIndex(0);
/*      */   }
/*      */ 
/*      */   private int getRepaintInterval()
/*      */   {
/* 1021 */     return this.repaintInterval;
/*      */   }
/*      */ 
/*      */   private int initRepaintInterval() {
/* 1025 */     this.repaintInterval = DefaultLookup.getInt(this.progressBar, this, "ProgressBar.repaintInterval", 50);
/*      */ 
/* 1027 */     return this.repaintInterval;
/*      */   }
/*      */ 
/*      */   private int getCycleTime()
/*      */   {
/* 1045 */     return this.cycleTime;
/*      */   }
/*      */ 
/*      */   private int initCycleTime() {
/* 1049 */     this.cycleTime = DefaultLookup.getInt(this.progressBar, this, "ProgressBar.cycleTime", 3000);
/*      */ 
/* 1051 */     return this.cycleTime;
/*      */   }
/*      */ 
/*      */   private void initIndeterminateDefaults()
/*      */   {
/* 1057 */     initRepaintInterval();
/* 1058 */     initCycleTime();
/*      */ 
/* 1061 */     if (this.repaintInterval <= 0) {
/* 1062 */       this.repaintInterval = 100;
/*      */     }
/*      */ 
/* 1066 */     if (this.repaintInterval > this.cycleTime) {
/* 1067 */       this.cycleTime = (this.repaintInterval * 20);
/*      */     }
/*      */     else {
/* 1070 */       int i = (int)Math.ceil(this.cycleTime / (this.repaintInterval * 2.0D));
/*      */ 
/* 1073 */       this.cycleTime = (this.repaintInterval * i * 2);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void initIndeterminateValues()
/*      */   {
/* 1084 */     initIndeterminateDefaults();
/*      */ 
/* 1086 */     this.numFrames = (this.cycleTime / this.repaintInterval);
/* 1087 */     initAnimationIndex();
/*      */ 
/* 1089 */     this.boxRect = new Rectangle();
/* 1090 */     this.nextPaintRect = new Rectangle();
/* 1091 */     this.componentInnards = new Rectangle();
/* 1092 */     this.oldComponentInnards = new Rectangle();
/*      */ 
/* 1096 */     this.progressBar.addHierarchyListener(getHandler());
/*      */ 
/* 1099 */     if (this.progressBar.isDisplayable())
/* 1100 */       startAnimationTimer();
/*      */   }
/*      */ 
/*      */   private void cleanUpIndeterminateValues()
/*      */   {
/* 1107 */     if (this.progressBar.isDisplayable()) {
/* 1108 */       stopAnimationTimer();
/*      */     }
/*      */ 
/* 1111 */     this.cycleTime = (this.repaintInterval = 0);
/* 1112 */     this.numFrames = (this.animationIndex = 0);
/* 1113 */     this.maxPosition = 0;
/* 1114 */     this.delta = 0.0D;
/*      */ 
/* 1116 */     this.boxRect = (this.nextPaintRect = null);
/* 1117 */     this.componentInnards = (this.oldComponentInnards = null);
/*      */ 
/* 1119 */     this.progressBar.removeHierarchyListener(getHandler());
/*      */   }
/*      */ 
/*      */   private void initAnimationIndex()
/*      */   {
/* 1125 */     if ((this.progressBar.getOrientation() == 0) && (BasicGraphicsUtils.isLeftToRight(this.progressBar)))
/*      */     {
/* 1129 */       setAnimationIndex(0);
/*      */     }
/*      */     else
/* 1132 */       setAnimationIndex(this.numFrames / 2);
/*      */   }
/*      */ 
/*      */   private class Animator
/*      */     implements ActionListener
/*      */   {
/*      */     private Timer timer;
/*      */     private long previousDelay;
/*      */     private int interval;
/*      */     private long lastCall;
/* 1151 */     private int MINIMUM_DELAY = 5;
/*      */ 
/*      */     private Animator()
/*      */     {
/*      */     }
/*      */ 
/*      */     private void start(int paramInt) {
/* 1158 */       this.previousDelay = paramInt;
/* 1159 */       this.lastCall = 0L;
/*      */ 
/* 1161 */       if (this.timer == null)
/* 1162 */         this.timer = new Timer(paramInt, this);
/*      */       else {
/* 1164 */         this.timer.setDelay(paramInt);
/*      */       }
/*      */ 
/* 1167 */       if (BasicProgressBarUI.ADJUSTTIMER) {
/* 1168 */         this.timer.setRepeats(false);
/* 1169 */         this.timer.setCoalesce(false);
/*      */       }
/*      */ 
/* 1172 */       this.timer.start();
/*      */     }
/*      */ 
/*      */     private void stop()
/*      */     {
/* 1179 */       this.timer.stop();
/*      */     }
/*      */ 
/*      */     public void actionPerformed(ActionEvent paramActionEvent)
/*      */     {
/* 1186 */       if (BasicProgressBarUI.ADJUSTTIMER) {
/* 1187 */         long l = System.currentTimeMillis();
/*      */ 
/* 1189 */         if (this.lastCall > 0L)
/*      */         {
/* 1195 */           int i = (int)(this.previousDelay - l + this.lastCall + BasicProgressBarUI.this.getRepaintInterval());
/*      */ 
/* 1198 */           if (i < this.MINIMUM_DELAY) {
/* 1199 */             i = this.MINIMUM_DELAY;
/*      */           }
/* 1201 */           this.timer.setInitialDelay(i);
/* 1202 */           this.previousDelay = i;
/*      */         }
/* 1204 */         this.timer.start();
/* 1205 */         this.lastCall = l;
/*      */       }
/*      */ 
/* 1208 */       BasicProgressBarUI.this.incrementAnimationIndex();
/*      */     }
/*      */   }
/*      */ 
/*      */   public class ChangeHandler
/*      */     implements ChangeListener
/*      */   {
/*      */     public ChangeHandler()
/*      */     {
/*      */     }
/*      */ 
/*      */     public void stateChanged(ChangeEvent paramChangeEvent)
/*      */     {
/* 1223 */       BasicProgressBarUI.this.getHandler().stateChanged(paramChangeEvent);
/*      */     }
/*      */   }
/*      */ 
/*      */   private class Handler implements ChangeListener, PropertyChangeListener, HierarchyListener {
/*      */     private Handler() {
/*      */     }
/*      */ 
/* 1231 */     public void stateChanged(ChangeEvent paramChangeEvent) { BoundedRangeModel localBoundedRangeModel = BasicProgressBarUI.this.progressBar.getModel();
/* 1232 */       int i = localBoundedRangeModel.getMaximum() - localBoundedRangeModel.getMinimum();
/*      */ 
/* 1234 */       int k = BasicProgressBarUI.this.getCachedPercent();
/*      */       int j;
/* 1236 */       if (i > 0)
/* 1237 */         j = (int)(100L * localBoundedRangeModel.getValue() / i);
/*      */       else {
/* 1239 */         j = 0;
/*      */       }
/*      */ 
/* 1242 */       if (j != k) {
/* 1243 */         BasicProgressBarUI.this.setCachedPercent(j);
/* 1244 */         BasicProgressBarUI.this.progressBar.repaint();
/*      */       }
/*      */     }
/*      */ 
/*      */     public void propertyChange(PropertyChangeEvent paramPropertyChangeEvent)
/*      */     {
/* 1250 */       String str = paramPropertyChangeEvent.getPropertyName();
/* 1251 */       if ("indeterminate" == str) {
/* 1252 */         if (BasicProgressBarUI.this.progressBar.isIndeterminate()) {
/* 1253 */           BasicProgressBarUI.this.initIndeterminateValues();
/*      */         }
/*      */         else {
/* 1256 */           BasicProgressBarUI.this.cleanUpIndeterminateValues();
/*      */         }
/* 1258 */         BasicProgressBarUI.this.progressBar.repaint();
/*      */       }
/*      */     }
/*      */ 
/*      */     public void hierarchyChanged(HierarchyEvent paramHierarchyEvent)
/*      */     {
/* 1264 */       if (((paramHierarchyEvent.getChangeFlags() & 0x2) != 0L) && 
/* 1265 */         (BasicProgressBarUI.this.progressBar.isIndeterminate()))
/* 1266 */         if (BasicProgressBarUI.this.progressBar.isDisplayable())
/* 1267 */           BasicProgressBarUI.this.startAnimationTimer();
/*      */         else
/* 1269 */           BasicProgressBarUI.this.stopAnimationTimer();
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.basic.BasicProgressBarUI
 * JD-Core Version:    0.6.2
 */