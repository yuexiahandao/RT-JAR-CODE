/*      */ package javax.swing.plaf.synth;
/*      */ 
/*      */ import java.awt.Dimension;
/*      */ import java.awt.FontMetrics;
/*      */ import java.awt.Graphics;
/*      */ import java.awt.Insets;
/*      */ import java.awt.Point;
/*      */ import java.awt.Rectangle;
/*      */ import java.awt.event.MouseEvent;
/*      */ import java.beans.PropertyChangeEvent;
/*      */ import java.beans.PropertyChangeListener;
/*      */ import java.util.Dictionary;
/*      */ import java.util.Enumeration;
/*      */ import javax.swing.JComponent;
/*      */ import javax.swing.JSlider;
/*      */ import javax.swing.plaf.ComponentUI;
/*      */ import javax.swing.plaf.basic.BasicSliderUI;
/*      */ import javax.swing.plaf.basic.BasicSliderUI.TrackListener;
/*      */ import sun.swing.SwingUtilities2;
/*      */ 
/*      */ public class SynthSliderUI extends BasicSliderUI
/*      */   implements PropertyChangeListener, SynthUI
/*      */ {
/*   53 */   private Rectangle valueRect = new Rectangle();
/*      */   private boolean paintValue;
/*      */   private Dimension lastSize;
/*      */   private int trackHeight;
/*      */   private int trackBorder;
/*      */   private int thumbWidth;
/*      */   private int thumbHeight;
/*      */   private SynthStyle style;
/*      */   private SynthStyle sliderTrackStyle;
/*      */   private SynthStyle sliderThumbStyle;
/*      */   private transient boolean thumbActive;
/*      */   private transient boolean thumbPressed;
/*      */ 
/*      */   public static ComponentUI createUI(JComponent paramJComponent)
/*      */   {
/*   93 */     return new SynthSliderUI((JSlider)paramJComponent);
/*      */   }
/*      */ 
/*      */   protected SynthSliderUI(JSlider paramJSlider) {
/*   97 */     super(paramJSlider);
/*      */   }
/*      */ 
/*      */   protected void installDefaults(JSlider paramJSlider)
/*      */   {
/*  105 */     updateStyle(paramJSlider);
/*      */   }
/*      */ 
/*      */   protected void uninstallDefaults(JSlider paramJSlider)
/*      */   {
/*  113 */     SynthContext localSynthContext = getContext(paramJSlider, 1);
/*  114 */     this.style.uninstallDefaults(localSynthContext);
/*  115 */     localSynthContext.dispose();
/*  116 */     this.style = null;
/*      */ 
/*  118 */     localSynthContext = getContext(paramJSlider, Region.SLIDER_TRACK, 1);
/*  119 */     this.sliderTrackStyle.uninstallDefaults(localSynthContext);
/*  120 */     localSynthContext.dispose();
/*  121 */     this.sliderTrackStyle = null;
/*      */ 
/*  123 */     localSynthContext = getContext(paramJSlider, Region.SLIDER_THUMB, 1);
/*  124 */     this.sliderThumbStyle.uninstallDefaults(localSynthContext);
/*  125 */     localSynthContext.dispose();
/*  126 */     this.sliderThumbStyle = null;
/*      */   }
/*      */ 
/*      */   protected void installListeners(JSlider paramJSlider)
/*      */   {
/*  134 */     super.installListeners(paramJSlider);
/*  135 */     paramJSlider.addPropertyChangeListener(this);
/*      */   }
/*      */ 
/*      */   protected void uninstallListeners(JSlider paramJSlider)
/*      */   {
/*  143 */     paramJSlider.removePropertyChangeListener(this);
/*  144 */     super.uninstallListeners(paramJSlider);
/*      */   }
/*      */ 
/*      */   private void updateStyle(JSlider paramJSlider) {
/*  148 */     SynthContext localSynthContext = getContext(paramJSlider, 1);
/*  149 */     SynthStyle localSynthStyle = this.style;
/*  150 */     this.style = SynthLookAndFeel.updateStyle(localSynthContext, this);
/*      */ 
/*  152 */     if (this.style != localSynthStyle) {
/*  153 */       this.thumbWidth = this.style.getInt(localSynthContext, "Slider.thumbWidth", 30);
/*      */ 
/*  156 */       this.thumbHeight = this.style.getInt(localSynthContext, "Slider.thumbHeight", 14);
/*      */ 
/*  162 */       String str = (String)this.slider.getClientProperty("JComponent.sizeVariant");
/*      */ 
/*  164 */       if (str != null) {
/*  165 */         if ("large".equals(str)) {
/*  166 */           this.thumbWidth = ((int)(this.thumbWidth * 1.15D));
/*  167 */           this.thumbHeight = ((int)(this.thumbHeight * 1.15D));
/*  168 */         } else if ("small".equals(str)) {
/*  169 */           this.thumbWidth = ((int)(this.thumbWidth * 0.857D));
/*  170 */           this.thumbHeight = ((int)(this.thumbHeight * 0.857D));
/*  171 */         } else if ("mini".equals(str)) {
/*  172 */           this.thumbWidth = ((int)(this.thumbWidth * 0.784D));
/*  173 */           this.thumbHeight = ((int)(this.thumbHeight * 0.784D));
/*      */         }
/*      */       }
/*      */ 
/*  177 */       this.trackBorder = this.style.getInt(localSynthContext, "Slider.trackBorder", 1);
/*      */ 
/*  180 */       this.trackHeight = (this.thumbHeight + this.trackBorder * 2);
/*      */ 
/*  182 */       this.paintValue = this.style.getBoolean(localSynthContext, "Slider.paintValue", true);
/*      */ 
/*  184 */       if (localSynthStyle != null) {
/*  185 */         uninstallKeyboardActions(paramJSlider);
/*  186 */         installKeyboardActions(paramJSlider);
/*      */       }
/*      */     }
/*  189 */     localSynthContext.dispose();
/*      */ 
/*  191 */     localSynthContext = getContext(paramJSlider, Region.SLIDER_TRACK, 1);
/*  192 */     this.sliderTrackStyle = SynthLookAndFeel.updateStyle(localSynthContext, this);
/*      */ 
/*  194 */     localSynthContext.dispose();
/*      */ 
/*  196 */     localSynthContext = getContext(paramJSlider, Region.SLIDER_THUMB, 1);
/*  197 */     this.sliderThumbStyle = SynthLookAndFeel.updateStyle(localSynthContext, this);
/*      */ 
/*  199 */     localSynthContext.dispose();
/*      */   }
/*      */ 
/*      */   protected BasicSliderUI.TrackListener createTrackListener(JSlider paramJSlider)
/*      */   {
/*  207 */     return new SynthTrackListener(null);
/*      */   }
/*      */ 
/*      */   private void updateThumbState(int paramInt1, int paramInt2) {
/*  211 */     setThumbActive(this.thumbRect.contains(paramInt1, paramInt2));
/*      */   }
/*      */ 
/*      */   private void updateThumbState(int paramInt1, int paramInt2, boolean paramBoolean) {
/*  215 */     updateThumbState(paramInt1, paramInt2);
/*  216 */     setThumbPressed(paramBoolean);
/*      */   }
/*      */ 
/*      */   private void setThumbActive(boolean paramBoolean) {
/*  220 */     if (this.thumbActive != paramBoolean) {
/*  221 */       this.thumbActive = paramBoolean;
/*  222 */       this.slider.repaint(this.thumbRect);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void setThumbPressed(boolean paramBoolean) {
/*  227 */     if (this.thumbPressed != paramBoolean) {
/*  228 */       this.thumbPressed = paramBoolean;
/*  229 */       this.slider.repaint(this.thumbRect);
/*      */     }
/*      */   }
/*      */ 
/*      */   public int getBaseline(JComponent paramJComponent, int paramInt1, int paramInt2)
/*      */   {
/*  238 */     if (paramJComponent == null) {
/*  239 */       throw new NullPointerException("Component must be non-null");
/*      */     }
/*  241 */     if ((paramInt1 < 0) || (paramInt2 < 0)) {
/*  242 */       throw new IllegalArgumentException("Width and height must be >= 0");
/*      */     }
/*      */ 
/*  245 */     if ((this.slider.getPaintLabels()) && (labelsHaveSameBaselines()))
/*      */     {
/*  247 */       Insets localInsets = new Insets(0, 0, 0, 0);
/*  248 */       SynthContext localSynthContext1 = getContext(this.slider, Region.SLIDER_TRACK);
/*      */ 
/*  250 */       this.style.getInsets(localSynthContext1, localInsets);
/*  251 */       localSynthContext1.dispose();
/*      */       int j;
/*      */       int k;
/*      */       int i1;
/*  252 */       if (this.slider.getOrientation() == 0) {
/*  253 */         int i = 0;
/*  254 */         if (this.paintValue) {
/*  255 */           SynthContext localSynthContext2 = getContext(this.slider);
/*  256 */           i = localSynthContext2.getStyle().getGraphicsUtils(localSynthContext2).getMaximumCharHeight(localSynthContext2);
/*      */ 
/*  258 */           localSynthContext2.dispose();
/*      */         }
/*  260 */         j = 0;
/*  261 */         if (this.slider.getPaintTicks()) {
/*  262 */           j = getTickLength();
/*      */         }
/*  264 */         k = getHeightOfTallestLabel();
/*  265 */         int m = i + this.trackHeight + localInsets.top + localInsets.bottom + j + k + 4;
/*      */ 
/*  268 */         i1 = paramInt2 / 2 - m / 2;
/*  269 */         i1 += i + 2;
/*  270 */         i1 += this.trackHeight + localInsets.top + localInsets.bottom;
/*  271 */         i1 += j + 2;
/*  272 */         JComponent localJComponent1 = (JComponent)this.slider.getLabelTable().elements().nextElement();
/*  273 */         Dimension localDimension1 = localJComponent1.getPreferredSize();
/*  274 */         return i1 + localJComponent1.getBaseline(localDimension1.width, localDimension1.height);
/*      */       }
/*      */ 
/*  277 */       Integer localInteger = this.slider.getInverted() ? getLowestValue() : getHighestValue();
/*      */ 
/*  279 */       if (localInteger != null) {
/*  280 */         j = this.insetCache.top;
/*  281 */         k = 0;
/*  282 */         if (this.paintValue) {
/*  283 */           SynthContext localSynthContext3 = getContext(this.slider);
/*  284 */           k = localSynthContext3.getStyle().getGraphicsUtils(localSynthContext3).getMaximumCharHeight(localSynthContext3);
/*      */ 
/*  286 */           localSynthContext3.dispose();
/*      */         }
/*  288 */         int n = paramInt2 - this.insetCache.top - this.insetCache.bottom;
/*      */ 
/*  290 */         i1 = j + k;
/*  291 */         int i2 = n - k;
/*  292 */         int i3 = yPositionForValue(localInteger.intValue(), i1, i2);
/*      */ 
/*  294 */         JComponent localJComponent2 = (JComponent)this.slider.getLabelTable().get(localInteger);
/*  295 */         Dimension localDimension2 = localJComponent2.getPreferredSize();
/*  296 */         return i3 - localDimension2.height / 2 + localJComponent2.getBaseline(localDimension2.width, localDimension2.height);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  301 */     return -1;
/*      */   }
/*      */ 
/*      */   public Dimension getPreferredSize(JComponent paramJComponent)
/*      */   {
/*  309 */     recalculateIfInsetsChanged();
/*  310 */     Dimension localDimension = new Dimension(this.contentRect.width, this.contentRect.height);
/*  311 */     if (this.slider.getOrientation() == 1)
/*  312 */       localDimension.height = 200;
/*      */     else {
/*  314 */       localDimension.width = 200;
/*      */     }
/*  316 */     Insets localInsets = this.slider.getInsets();
/*  317 */     localDimension.width += localInsets.left + localInsets.right;
/*  318 */     localDimension.height += localInsets.top + localInsets.bottom;
/*  319 */     return localDimension;
/*      */   }
/*      */ 
/*      */   public Dimension getMinimumSize(JComponent paramJComponent)
/*      */   {
/*  327 */     recalculateIfInsetsChanged();
/*  328 */     Dimension localDimension = new Dimension(this.contentRect.width, this.contentRect.height);
/*  329 */     if (this.slider.getOrientation() == 1)
/*  330 */       localDimension.height = (this.thumbRect.height + this.insetCache.top + this.insetCache.bottom);
/*      */     else {
/*  332 */       localDimension.width = (this.thumbRect.width + this.insetCache.left + this.insetCache.right);
/*      */     }
/*  334 */     return localDimension;
/*      */   }
/*      */ 
/*      */   protected void calculateGeometry()
/*      */   {
/*  342 */     calculateThumbSize();
/*  343 */     layout();
/*  344 */     calculateThumbLocation();
/*      */   }
/*      */ 
/*      */   protected void layout()
/*      */   {
/*  351 */     SynthContext localSynthContext1 = getContext(this.slider);
/*  352 */     SynthGraphicsUtils localSynthGraphicsUtils = this.style.getGraphicsUtils(localSynthContext1);
/*      */ 
/*  355 */     Insets localInsets = new Insets(0, 0, 0, 0);
/*  356 */     SynthContext localSynthContext2 = getContext(this.slider, Region.SLIDER_TRACK);
/*  357 */     this.style.getInsets(localSynthContext2, localInsets);
/*  358 */     localSynthContext2.dispose();
/*      */     int k;
/*      */     int m;
/*      */     int n;
/*      */     int j;
/*  360 */     if (this.slider.getOrientation() == 0)
/*      */     {
/*  363 */       this.valueRect.height = 0;
/*  364 */       if (this.paintValue) {
/*  365 */         this.valueRect.height = localSynthGraphicsUtils.getMaximumCharHeight(localSynthContext1);
/*      */       }
/*      */ 
/*  369 */       this.trackRect.height = this.trackHeight;
/*      */ 
/*  371 */       this.tickRect.height = 0;
/*  372 */       if (this.slider.getPaintTicks()) {
/*  373 */         this.tickRect.height = getTickLength();
/*      */       }
/*      */ 
/*  376 */       this.labelRect.height = 0;
/*  377 */       if (this.slider.getPaintLabels()) {
/*  378 */         this.labelRect.height = getHeightOfTallestLabel();
/*      */       }
/*      */ 
/*  381 */       this.contentRect.height = (this.valueRect.height + this.trackRect.height + localInsets.top + localInsets.bottom + this.tickRect.height + this.labelRect.height + 4);
/*      */ 
/*  384 */       this.contentRect.width = (this.slider.getWidth() - this.insetCache.left - this.insetCache.right);
/*      */ 
/*  388 */       int i = 0;
/*  389 */       if (this.slider.getPaintLabels())
/*      */       {
/*  392 */         this.trackRect.x = this.insetCache.left;
/*  393 */         this.trackRect.width = this.contentRect.width;
/*      */ 
/*  395 */         Dictionary localDictionary = this.slider.getLabelTable();
/*  396 */         if (localDictionary != null) {
/*  397 */           k = this.slider.getMinimum();
/*  398 */           m = this.slider.getMaximum();
/*      */ 
/*  403 */           n = 2147483647;
/*  404 */           int i1 = -2147483648;
/*  405 */           Enumeration localEnumeration = localDictionary.keys();
/*  406 */           while (localEnumeration.hasMoreElements()) {
/*  407 */             int i2 = ((Integer)localEnumeration.nextElement()).intValue();
/*  408 */             if ((i2 >= k) && (i2 < n)) {
/*  409 */               n = i2;
/*      */             }
/*  411 */             if ((i2 <= m) && (i2 > i1)) {
/*  412 */               i1 = i2;
/*      */             }
/*      */ 
/*      */           }
/*      */ 
/*  417 */           i = getPadForLabel(n);
/*  418 */           i = Math.max(i, getPadForLabel(i1));
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  423 */       this.valueRect.x = (this.trackRect.x = this.tickRect.x = this.labelRect.x = this.insetCache.left + i);
/*      */ 
/*  425 */       this.valueRect.width = (this.trackRect.width = this.tickRect.width = this.labelRect.width = this.contentRect.width - i * 2);
/*      */ 
/*  428 */       j = this.slider.getHeight() / 2 - this.contentRect.height / 2;
/*      */ 
/*  430 */       this.valueRect.y = j;
/*  431 */       j += this.valueRect.height + 2;
/*      */ 
/*  433 */       this.trackRect.y = (j + localInsets.top);
/*  434 */       j += this.trackRect.height + localInsets.top + localInsets.bottom;
/*      */ 
/*  436 */       this.tickRect.y = j;
/*  437 */       j += this.tickRect.height + 2;
/*      */ 
/*  439 */       this.labelRect.y = j;
/*  440 */       j += this.labelRect.height;
/*      */     }
/*      */     else
/*      */     {
/*  444 */       this.trackRect.width = this.trackHeight;
/*      */ 
/*  446 */       this.tickRect.width = 0;
/*  447 */       if (this.slider.getPaintTicks()) {
/*  448 */         this.tickRect.width = getTickLength();
/*      */       }
/*      */ 
/*  451 */       this.labelRect.width = 0;
/*  452 */       if (this.slider.getPaintLabels()) {
/*  453 */         this.labelRect.width = getWidthOfWidestLabel();
/*      */       }
/*      */ 
/*  456 */       this.valueRect.y = this.insetCache.top;
/*  457 */       this.valueRect.height = 0;
/*  458 */       if (this.paintValue) {
/*  459 */         this.valueRect.height = localSynthGraphicsUtils.getMaximumCharHeight(localSynthContext1);
/*      */       }
/*      */ 
/*  464 */       FontMetrics localFontMetrics = this.slider.getFontMetrics(this.slider.getFont());
/*  465 */       this.valueRect.width = Math.max(localSynthGraphicsUtils.computeStringWidth(localSynthContext1, this.slider.getFont(), localFontMetrics, "" + this.slider.getMaximum()), localSynthGraphicsUtils.computeStringWidth(localSynthContext1, this.slider.getFont(), localFontMetrics, "" + this.slider.getMinimum()));
/*      */ 
/*  471 */       j = this.valueRect.width / 2;
/*  472 */       k = localInsets.left + this.trackRect.width / 2;
/*  473 */       m = this.trackRect.width / 2 + localInsets.right + this.tickRect.width + this.labelRect.width;
/*      */ 
/*  475 */       this.contentRect.width = (Math.max(k, j) + Math.max(m, j) + 2 + this.insetCache.left + this.insetCache.right);
/*      */ 
/*  477 */       this.contentRect.height = (this.slider.getHeight() - this.insetCache.top - this.insetCache.bottom);
/*      */ 
/*  481 */       this.trackRect.y = (this.tickRect.y = this.labelRect.y = this.valueRect.y + this.valueRect.height);
/*      */ 
/*  483 */       this.trackRect.height = (this.tickRect.height = this.labelRect.height = this.contentRect.height - this.valueRect.height);
/*      */ 
/*  486 */       n = this.slider.getWidth() / 2 - this.contentRect.width / 2;
/*  487 */       if (SynthLookAndFeel.isLeftToRight(this.slider)) {
/*  488 */         if (j > k) {
/*  489 */           n += j - k;
/*      */         }
/*  491 */         this.trackRect.x = (n + localInsets.left);
/*      */ 
/*  493 */         n += localInsets.left + this.trackRect.width + localInsets.right;
/*  494 */         this.tickRect.x = n;
/*  495 */         this.labelRect.x = (n + this.tickRect.width + 2);
/*      */       } else {
/*  497 */         if (j > m) {
/*  498 */           n += j - m;
/*      */         }
/*  500 */         this.labelRect.x = n;
/*      */ 
/*  502 */         n += this.labelRect.width + 2;
/*  503 */         this.tickRect.x = n;
/*  504 */         this.trackRect.x = (n + this.tickRect.width + localInsets.left);
/*      */       }
/*      */     }
/*  507 */     localSynthContext1.dispose();
/*  508 */     this.lastSize = this.slider.getSize();
/*      */   }
/*      */ 
/*      */   private int getPadForLabel(int paramInt)
/*      */   {
/*  518 */     int i = 0;
/*      */ 
/*  520 */     JComponent localJComponent = (JComponent)this.slider.getLabelTable().get(Integer.valueOf(paramInt));
/*  521 */     if (localJComponent != null) {
/*  522 */       int j = xPositionForValue(paramInt);
/*  523 */       int k = localJComponent.getPreferredSize().width / 2;
/*  524 */       if (j - k < this.insetCache.left) {
/*  525 */         i = Math.max(i, this.insetCache.left - (j - k));
/*      */       }
/*      */ 
/*  528 */       if (j + k > this.slider.getWidth() - this.insetCache.right) {
/*  529 */         i = Math.max(i, j + k - (this.slider.getWidth() - this.insetCache.right));
/*      */       }
/*      */     }
/*      */ 
/*  533 */     return i;
/*      */   }
/*      */ 
/*      */   protected void calculateThumbLocation()
/*      */   {
/*  541 */     super.calculateThumbLocation();
/*  542 */     if (this.slider.getOrientation() == 0)
/*  543 */       this.thumbRect.y += this.trackBorder;
/*      */     else {
/*  545 */       this.thumbRect.x += this.trackBorder;
/*      */     }
/*  547 */     Point localPoint = this.slider.getMousePosition();
/*  548 */     if (localPoint != null)
/*  549 */       updateThumbState(localPoint.x, localPoint.y);
/*      */   }
/*      */ 
/*      */   public void setThumbLocation(int paramInt1, int paramInt2)
/*      */   {
/*  558 */     super.setThumbLocation(paramInt1, paramInt2);
/*      */ 
/*  561 */     this.slider.repaint(this.valueRect.x, this.valueRect.y, this.valueRect.width, this.valueRect.height);
/*      */ 
/*  563 */     setThumbActive(false);
/*      */   }
/*      */ 
/*      */   protected int xPositionForValue(int paramInt)
/*      */   {
/*  571 */     int i = this.slider.getMinimum();
/*  572 */     int j = this.slider.getMaximum();
/*  573 */     int k = this.trackRect.x + this.thumbRect.width / 2 + this.trackBorder;
/*  574 */     int m = this.trackRect.x + this.trackRect.width - this.thumbRect.width / 2 - this.trackBorder;
/*      */ 
/*  576 */     int n = m - k;
/*  577 */     double d1 = j - i;
/*  578 */     double d2 = n / d1;
/*      */ 
/*  581 */     if (!drawInverted()) {
/*  582 */       i1 = k;
/*  583 */       i1 = (int)(i1 + Math.round(d2 * (paramInt - i)));
/*      */     } else {
/*  585 */       i1 = m;
/*  586 */       i1 = (int)(i1 - Math.round(d2 * (paramInt - i)));
/*      */     }
/*      */ 
/*  589 */     int i1 = Math.max(k, i1);
/*  590 */     i1 = Math.min(m, i1);
/*      */ 
/*  592 */     return i1;
/*      */   }
/*      */ 
/*      */   protected int yPositionForValue(int paramInt1, int paramInt2, int paramInt3)
/*      */   {
/*  600 */     int i = this.slider.getMinimum();
/*  601 */     int j = this.slider.getMaximum();
/*  602 */     int k = paramInt2 + this.thumbRect.height / 2 + this.trackBorder;
/*  603 */     int m = paramInt2 + paramInt3 - this.thumbRect.height / 2 - this.trackBorder;
/*      */ 
/*  605 */     int n = m - k;
/*  606 */     double d1 = j - i;
/*  607 */     double d2 = n / d1;
/*      */ 
/*  610 */     if (!drawInverted()) {
/*  611 */       i1 = k;
/*  612 */       i1 = (int)(i1 + Math.round(d2 * (j - paramInt1)));
/*      */     } else {
/*  614 */       i1 = k;
/*  615 */       i1 = (int)(i1 + Math.round(d2 * (paramInt1 - i)));
/*      */     }
/*      */ 
/*  618 */     int i1 = Math.max(k, i1);
/*  619 */     i1 = Math.min(m, i1);
/*      */ 
/*  621 */     return i1;
/*      */   }
/*      */ 
/*      */   public int valueForYPosition(int paramInt)
/*      */   {
/*  630 */     int j = this.slider.getMinimum();
/*  631 */     int k = this.slider.getMaximum();
/*  632 */     int m = this.trackRect.y + this.thumbRect.height / 2 + this.trackBorder;
/*  633 */     int n = this.trackRect.y + this.trackRect.height - this.thumbRect.height / 2 - this.trackBorder;
/*      */ 
/*  635 */     int i1 = n - m;
/*      */     int i;
/*  637 */     if (paramInt <= m) {
/*  638 */       i = drawInverted() ? j : k;
/*  639 */     } else if (paramInt >= n) {
/*  640 */       i = drawInverted() ? k : j;
/*      */     } else {
/*  642 */       int i2 = paramInt - m;
/*  643 */       double d1 = k - j;
/*  644 */       double d2 = d1 / i1;
/*  645 */       int i3 = (int)Math.round(i2 * d2);
/*      */ 
/*  647 */       i = drawInverted() ? j + i3 : k - i3;
/*      */     }
/*      */ 
/*  650 */     return i;
/*      */   }
/*      */ 
/*      */   public int valueForXPosition(int paramInt)
/*      */   {
/*  659 */     int j = this.slider.getMinimum();
/*  660 */     int k = this.slider.getMaximum();
/*  661 */     int m = this.trackRect.x + this.thumbRect.width / 2 + this.trackBorder;
/*  662 */     int n = this.trackRect.x + this.trackRect.width - this.thumbRect.width / 2 - this.trackBorder;
/*      */ 
/*  664 */     int i1 = n - m;
/*      */     int i;
/*  666 */     if (paramInt <= m) {
/*  667 */       i = drawInverted() ? k : j;
/*  668 */     } else if (paramInt >= n) {
/*  669 */       i = drawInverted() ? j : k;
/*      */     } else {
/*  671 */       int i2 = paramInt - m;
/*  672 */       double d1 = k - j;
/*  673 */       double d2 = d1 / i1;
/*  674 */       int i3 = (int)Math.round(i2 * d2);
/*      */ 
/*  676 */       i = drawInverted() ? k - i3 : j + i3;
/*      */     }
/*      */ 
/*  679 */     return i;
/*      */   }
/*      */ 
/*      */   protected Dimension getThumbSize()
/*      */   {
/*  687 */     Dimension localDimension = new Dimension();
/*      */ 
/*  689 */     if (this.slider.getOrientation() == 1) {
/*  690 */       localDimension.width = this.thumbHeight;
/*  691 */       localDimension.height = this.thumbWidth;
/*      */     } else {
/*  693 */       localDimension.width = this.thumbWidth;
/*  694 */       localDimension.height = this.thumbHeight;
/*      */     }
/*  696 */     return localDimension;
/*      */   }
/*      */ 
/*      */   protected void recalculateIfInsetsChanged()
/*      */   {
/*  704 */     SynthContext localSynthContext = getContext(this.slider);
/*  705 */     Insets localInsets1 = this.style.getInsets(localSynthContext, null);
/*  706 */     Insets localInsets2 = this.slider.getInsets();
/*  707 */     localInsets1.left += localInsets2.left; localInsets1.right += localInsets2.right;
/*  708 */     localInsets1.top += localInsets2.top; localInsets1.bottom += localInsets2.bottom;
/*  709 */     if (!localInsets1.equals(this.insetCache)) {
/*  710 */       this.insetCache = localInsets1;
/*  711 */       calculateGeometry();
/*      */     }
/*  713 */     localSynthContext.dispose();
/*      */   }
/*      */ 
/*      */   public SynthContext getContext(JComponent paramJComponent)
/*      */   {
/*  721 */     return getContext(paramJComponent, SynthLookAndFeel.getComponentState(paramJComponent));
/*      */   }
/*      */ 
/*      */   private SynthContext getContext(JComponent paramJComponent, int paramInt) {
/*  725 */     return SynthContext.getContext(SynthContext.class, paramJComponent, SynthLookAndFeel.getRegion(paramJComponent), this.style, paramInt);
/*      */   }
/*      */ 
/*      */   private SynthContext getContext(JComponent paramJComponent, Region paramRegion)
/*      */   {
/*  730 */     return getContext(paramJComponent, paramRegion, getComponentState(paramJComponent, paramRegion));
/*      */   }
/*      */ 
/*      */   private SynthContext getContext(JComponent paramJComponent, Region paramRegion, int paramInt) {
/*  734 */     SynthStyle localSynthStyle = null;
/*  735 */     SynthContext localSynthContext = SynthContext.class;
/*      */ 
/*  737 */     if (paramRegion == Region.SLIDER_TRACK)
/*  738 */       localSynthStyle = this.sliderTrackStyle;
/*  739 */     else if (paramRegion == Region.SLIDER_THUMB) {
/*  740 */       localSynthStyle = this.sliderThumbStyle;
/*      */     }
/*  742 */     return SynthContext.getContext(localSynthContext, paramJComponent, paramRegion, localSynthStyle, paramInt);
/*      */   }
/*      */ 
/*      */   private int getComponentState(JComponent paramJComponent, Region paramRegion) {
/*  746 */     if ((paramRegion == Region.SLIDER_THUMB) && (this.thumbActive) && (paramJComponent.isEnabled())) {
/*  747 */       int i = this.thumbPressed ? 4 : 2;
/*  748 */       if (paramJComponent.isFocusOwner()) i |= 256;
/*  749 */       return i;
/*      */     }
/*  751 */     return SynthLookAndFeel.getComponentState(paramJComponent);
/*      */   }
/*      */ 
/*      */   public void update(Graphics paramGraphics, JComponent paramJComponent)
/*      */   {
/*  768 */     SynthContext localSynthContext = getContext(paramJComponent);
/*  769 */     SynthLookAndFeel.update(localSynthContext, paramGraphics);
/*  770 */     localSynthContext.getPainter().paintSliderBackground(localSynthContext, paramGraphics, 0, 0, paramJComponent.getWidth(), paramJComponent.getHeight(), this.slider.getOrientation());
/*      */ 
/*  773 */     paint(localSynthContext, paramGraphics);
/*  774 */     localSynthContext.dispose();
/*      */   }
/*      */ 
/*      */   public void paint(Graphics paramGraphics, JComponent paramJComponent)
/*      */   {
/*  788 */     SynthContext localSynthContext = getContext(paramJComponent);
/*  789 */     paint(localSynthContext, paramGraphics);
/*  790 */     localSynthContext.dispose();
/*      */   }
/*      */ 
/*      */   protected void paint(SynthContext paramSynthContext, Graphics paramGraphics)
/*      */   {
/*  801 */     recalculateIfInsetsChanged();
/*  802 */     recalculateIfOrientationChanged();
/*  803 */     Rectangle localRectangle = paramGraphics.getClipBounds();
/*      */ 
/*  805 */     if ((this.lastSize == null) || (!this.lastSize.equals(this.slider.getSize())))
/*  806 */       calculateGeometry();
/*      */     Object localObject;
/*  809 */     if (this.paintValue) {
/*  810 */       localObject = SwingUtilities2.getFontMetrics(this.slider, paramGraphics);
/*  811 */       int i = paramSynthContext.getStyle().getGraphicsUtils(paramSynthContext).computeStringWidth(paramSynthContext, paramGraphics.getFont(), (FontMetrics)localObject, "" + this.slider.getValue());
/*      */ 
/*  814 */       this.valueRect.x = (this.thumbRect.x + (this.thumbRect.width - i) / 2);
/*      */ 
/*  818 */       if (this.slider.getOrientation() == 0) {
/*  819 */         if (this.valueRect.x + i > this.insetCache.left + this.contentRect.width) {
/*  820 */           this.valueRect.x = (this.insetCache.left + this.contentRect.width - i);
/*      */         }
/*  822 */         this.valueRect.x = Math.max(this.valueRect.x, 0);
/*      */       }
/*      */ 
/*  825 */       paramGraphics.setColor(paramSynthContext.getStyle().getColor(paramSynthContext, ColorType.TEXT_FOREGROUND));
/*      */ 
/*  827 */       paramSynthContext.getStyle().getGraphicsUtils(paramSynthContext).paintText(paramSynthContext, paramGraphics, "" + this.slider.getValue(), this.valueRect.x, this.valueRect.y, -1);
/*      */     }
/*      */ 
/*  832 */     if ((this.slider.getPaintTrack()) && (localRectangle.intersects(this.trackRect))) {
/*  833 */       localObject = getContext(this.slider, Region.SLIDER_TRACK);
/*  834 */       paintTrack((SynthContext)localObject, paramGraphics, this.trackRect);
/*  835 */       ((SynthContext)localObject).dispose();
/*      */     }
/*      */ 
/*  838 */     if (localRectangle.intersects(this.thumbRect)) {
/*  839 */       localObject = getContext(this.slider, Region.SLIDER_THUMB);
/*  840 */       paintThumb((SynthContext)localObject, paramGraphics, this.thumbRect);
/*  841 */       ((SynthContext)localObject).dispose();
/*      */     }
/*      */ 
/*  844 */     if ((this.slider.getPaintTicks()) && (localRectangle.intersects(this.tickRect))) {
/*  845 */       paintTicks(paramGraphics);
/*      */     }
/*      */ 
/*  848 */     if ((this.slider.getPaintLabels()) && (localRectangle.intersects(this.labelRect)))
/*  849 */       paintLabels(paramGraphics);
/*      */   }
/*      */ 
/*      */   public void paintBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/*  859 */     paramSynthContext.getPainter().paintSliderBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, this.slider.getOrientation());
/*      */   }
/*      */ 
/*      */   protected void paintThumb(SynthContext paramSynthContext, Graphics paramGraphics, Rectangle paramRectangle)
/*      */   {
/*  872 */     int i = this.slider.getOrientation();
/*  873 */     SynthLookAndFeel.updateSubregion(paramSynthContext, paramGraphics, paramRectangle);
/*  874 */     paramSynthContext.getPainter().paintSliderThumbBackground(paramSynthContext, paramGraphics, paramRectangle.x, paramRectangle.y, paramRectangle.width, paramRectangle.height, i);
/*      */ 
/*  877 */     paramSynthContext.getPainter().paintSliderThumbBorder(paramSynthContext, paramGraphics, paramRectangle.x, paramRectangle.y, paramRectangle.width, paramRectangle.height, i);
/*      */   }
/*      */ 
/*      */   protected void paintTrack(SynthContext paramSynthContext, Graphics paramGraphics, Rectangle paramRectangle)
/*      */   {
/*  891 */     int i = this.slider.getOrientation();
/*  892 */     SynthLookAndFeel.updateSubregion(paramSynthContext, paramGraphics, paramRectangle);
/*  893 */     paramSynthContext.getPainter().paintSliderTrackBackground(paramSynthContext, paramGraphics, paramRectangle.x, paramRectangle.y, paramRectangle.width, paramRectangle.height, i);
/*      */ 
/*  896 */     paramSynthContext.getPainter().paintSliderTrackBorder(paramSynthContext, paramGraphics, paramRectangle.x, paramRectangle.y, paramRectangle.width, paramRectangle.height, i);
/*      */   }
/*      */ 
/*      */   public void propertyChange(PropertyChangeEvent paramPropertyChangeEvent)
/*      */   {
/*  906 */     if (SynthLookAndFeel.shouldUpdateStyle(paramPropertyChangeEvent))
/*  907 */       updateStyle((JSlider)paramPropertyChangeEvent.getSource());
/*      */   }
/*      */ 
/*      */   private class SynthTrackListener extends BasicSliderUI.TrackListener
/*      */   {
/*      */     private SynthTrackListener()
/*      */     {
/*  917 */       super();
/*      */     }
/*      */     public void mouseExited(MouseEvent paramMouseEvent) {
/*  920 */       SynthSliderUI.this.setThumbActive(false);
/*      */     }
/*      */ 
/*      */     public void mousePressed(MouseEvent paramMouseEvent) {
/*  924 */       super.mousePressed(paramMouseEvent);
/*  925 */       SynthSliderUI.this.setThumbPressed(SynthSliderUI.this.thumbRect.contains(paramMouseEvent.getX(), paramMouseEvent.getY()));
/*      */     }
/*      */ 
/*      */     public void mouseReleased(MouseEvent paramMouseEvent) {
/*  929 */       super.mouseReleased(paramMouseEvent);
/*  930 */       SynthSliderUI.this.updateThumbState(paramMouseEvent.getX(), paramMouseEvent.getY(), false);
/*      */     }
/*      */ 
/*      */     public void mouseDragged(MouseEvent paramMouseEvent)
/*      */     {
/*  936 */       if (!SynthSliderUI.this.slider.isEnabled()) {
/*  937 */         return;
/*      */       }
/*      */ 
/*  940 */       this.currentMouseX = paramMouseEvent.getX();
/*  941 */       this.currentMouseY = paramMouseEvent.getY();
/*      */ 
/*  943 */       if (!SynthSliderUI.this.isDragging()) {
/*  944 */         return;
/*      */       }
/*      */ 
/*  947 */       SynthSliderUI.this.slider.setValueIsAdjusting(true);
/*      */       int i;
/*  949 */       switch (SynthSliderUI.this.slider.getOrientation()) {
/*      */       case 1:
/*  951 */         int j = SynthSliderUI.this.thumbRect.height / 2;
/*  952 */         int k = paramMouseEvent.getY() - this.offset;
/*  953 */         int m = SynthSliderUI.this.trackRect.y;
/*  954 */         int n = SynthSliderUI.this.trackRect.y + SynthSliderUI.this.trackRect.height - j - SynthSliderUI.this.trackBorder;
/*      */ 
/*  956 */         int i1 = SynthSliderUI.this.yPositionForValue(SynthSliderUI.this.slider.getMaximum() - SynthSliderUI.this.slider.getExtent());
/*      */ 
/*  959 */         if (SynthSliderUI.this.drawInverted()) {
/*  960 */           n = i1;
/*  961 */           m += j;
/*      */         } else {
/*  963 */           m = i1;
/*      */         }
/*  965 */         k = Math.max(k, m - j);
/*  966 */         k = Math.min(k, n - j);
/*      */ 
/*  968 */         SynthSliderUI.this.setThumbLocation(SynthSliderUI.this.thumbRect.x, k);
/*      */ 
/*  970 */         i = k + j;
/*  971 */         SynthSliderUI.this.slider.setValue(SynthSliderUI.this.valueForYPosition(i));
/*  972 */         break;
/*      */       case 0:
/*  974 */         int i2 = SynthSliderUI.this.thumbRect.width / 2;
/*  975 */         int i3 = paramMouseEvent.getX() - this.offset;
/*  976 */         int i4 = SynthSliderUI.this.trackRect.x + i2 + SynthSliderUI.this.trackBorder;
/*  977 */         int i5 = SynthSliderUI.this.trackRect.x + SynthSliderUI.this.trackRect.width - i2 - SynthSliderUI.this.trackBorder;
/*      */ 
/*  979 */         int i6 = SynthSliderUI.this.xPositionForValue(SynthSliderUI.this.slider.getMaximum() - SynthSliderUI.this.slider.getExtent());
/*      */ 
/*  982 */         if (SynthSliderUI.this.drawInverted())
/*  983 */           i4 = i6;
/*      */         else {
/*  985 */           i5 = i6;
/*      */         }
/*  987 */         i3 = Math.max(i3, i4 - i2);
/*  988 */         i3 = Math.min(i3, i5 - i2);
/*      */ 
/*  990 */         SynthSliderUI.this.setThumbLocation(i3, SynthSliderUI.this.thumbRect.y);
/*      */ 
/*  992 */         i = i3 + i2;
/*  993 */         SynthSliderUI.this.slider.setValue(SynthSliderUI.this.valueForXPosition(i));
/*  994 */         break;
/*      */       default:
/*  996 */         return;
/*      */       }
/*      */ 
/*  999 */       if (SynthSliderUI.this.slider.getValueIsAdjusting())
/* 1000 */         SynthSliderUI.this.setThumbActive(true);
/*      */     }
/*      */ 
/*      */     public void mouseMoved(MouseEvent paramMouseEvent)
/*      */     {
/* 1005 */       SynthSliderUI.this.updateThumbState(paramMouseEvent.getX(), paramMouseEvent.getY());
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.synth.SynthSliderUI
 * JD-Core Version:    0.6.2
 */