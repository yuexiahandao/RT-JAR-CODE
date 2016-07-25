/*     */ package javax.swing.plaf.synth;
/*     */ 
/*     */ import java.awt.ComponentOrientation;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Insets;
/*     */ import java.awt.Rectangle;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JScrollBar;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.UIResource;
/*     */ import javax.swing.plaf.basic.BasicScrollBarUI;
/*     */ 
/*     */ public class SynthScrollBarUI extends BasicScrollBarUI
/*     */   implements PropertyChangeListener, SynthUI
/*     */ {
/*     */   private SynthStyle style;
/*     */   private SynthStyle thumbStyle;
/*     */   private SynthStyle trackStyle;
/*     */   private boolean validMinimumThumbSize;
/*     */ 
/*     */   public static ComponentUI createUI(JComponent paramJComponent)
/*     */   {
/*  52 */     return new SynthScrollBarUI();
/*     */   }
/*     */ 
/*     */   protected void installDefaults()
/*     */   {
/*  60 */     super.installDefaults();
/*  61 */     this.trackHighlight = 0;
/*  62 */     if ((this.scrollbar.getLayout() == null) || ((this.scrollbar.getLayout() instanceof UIResource)))
/*     */     {
/*  64 */       this.scrollbar.setLayout(this);
/*     */     }
/*  66 */     configureScrollBarColors();
/*  67 */     updateStyle(this.scrollbar);
/*     */   }
/*     */ 
/*     */   protected void configureScrollBarColors()
/*     */   {
/*     */   }
/*     */ 
/*     */   private void updateStyle(JScrollBar paramJScrollBar)
/*     */   {
/*  78 */     SynthStyle localSynthStyle = this.style;
/*  79 */     SynthContext localSynthContext = getContext(paramJScrollBar, 1);
/*  80 */     this.style = SynthLookAndFeel.updateStyle(localSynthContext, this);
/*  81 */     if (this.style != localSynthStyle) {
/*  82 */       this.scrollBarWidth = this.style.getInt(localSynthContext, "ScrollBar.thumbHeight", 14);
/*  83 */       this.minimumThumbSize = ((Dimension)this.style.get(localSynthContext, "ScrollBar.minimumThumbSize"));
/*     */ 
/*  85 */       if (this.minimumThumbSize == null) {
/*  86 */         this.minimumThumbSize = new Dimension();
/*  87 */         this.validMinimumThumbSize = false;
/*     */       }
/*     */       else {
/*  90 */         this.validMinimumThumbSize = true;
/*     */       }
/*  92 */       this.maximumThumbSize = ((Dimension)this.style.get(localSynthContext, "ScrollBar.maximumThumbSize"));
/*     */ 
/*  94 */       if (this.maximumThumbSize == null) {
/*  95 */         this.maximumThumbSize = new Dimension(4096, 4097);
/*     */       }
/*     */ 
/*  98 */       this.incrGap = this.style.getInt(localSynthContext, "ScrollBar.incrementButtonGap", 0);
/*  99 */       this.decrGap = this.style.getInt(localSynthContext, "ScrollBar.decrementButtonGap", 0);
/*     */ 
/* 104 */       String str = (String)this.scrollbar.getClientProperty("JComponent.sizeVariant");
/*     */ 
/* 106 */       if (str != null) {
/* 107 */         if ("large".equals(str)) {
/* 108 */           this.scrollBarWidth = ((int)(this.scrollBarWidth * 1.15D));
/* 109 */           this.incrGap = ((int)(this.incrGap * 1.15D));
/* 110 */           this.decrGap = ((int)(this.decrGap * 1.15D));
/* 111 */         } else if ("small".equals(str)) {
/* 112 */           this.scrollBarWidth = ((int)(this.scrollBarWidth * 0.857D));
/* 113 */           this.incrGap = ((int)(this.incrGap * 0.857D));
/* 114 */           this.decrGap = ((int)(this.decrGap * 0.857D));
/* 115 */         } else if ("mini".equals(str)) {
/* 116 */           this.scrollBarWidth = ((int)(this.scrollBarWidth * 0.714D));
/* 117 */           this.incrGap = ((int)(this.incrGap * 0.714D));
/* 118 */           this.decrGap = ((int)(this.decrGap * 0.714D));
/*     */         }
/*     */       }
/*     */ 
/* 122 */       if (localSynthStyle != null) {
/* 123 */         uninstallKeyboardActions();
/* 124 */         installKeyboardActions();
/*     */       }
/*     */     }
/* 127 */     localSynthContext.dispose();
/*     */ 
/* 129 */     localSynthContext = getContext(paramJScrollBar, Region.SCROLL_BAR_TRACK, 1);
/* 130 */     this.trackStyle = SynthLookAndFeel.updateStyle(localSynthContext, this);
/* 131 */     localSynthContext.dispose();
/*     */ 
/* 133 */     localSynthContext = getContext(paramJScrollBar, Region.SCROLL_BAR_THUMB, 1);
/* 134 */     this.thumbStyle = SynthLookAndFeel.updateStyle(localSynthContext, this);
/* 135 */     localSynthContext.dispose();
/*     */   }
/*     */ 
/*     */   protected void installListeners()
/*     */   {
/* 143 */     super.installListeners();
/* 144 */     this.scrollbar.addPropertyChangeListener(this);
/*     */   }
/*     */ 
/*     */   protected void uninstallListeners()
/*     */   {
/* 152 */     super.uninstallListeners();
/* 153 */     this.scrollbar.removePropertyChangeListener(this);
/*     */   }
/*     */ 
/*     */   protected void uninstallDefaults()
/*     */   {
/* 161 */     SynthContext localSynthContext = getContext(this.scrollbar, 1);
/* 162 */     this.style.uninstallDefaults(localSynthContext);
/* 163 */     localSynthContext.dispose();
/* 164 */     this.style = null;
/*     */ 
/* 166 */     localSynthContext = getContext(this.scrollbar, Region.SCROLL_BAR_TRACK, 1);
/* 167 */     this.trackStyle.uninstallDefaults(localSynthContext);
/* 168 */     localSynthContext.dispose();
/* 169 */     this.trackStyle = null;
/*     */ 
/* 171 */     localSynthContext = getContext(this.scrollbar, Region.SCROLL_BAR_THUMB, 1);
/* 172 */     this.thumbStyle.uninstallDefaults(localSynthContext);
/* 173 */     localSynthContext.dispose();
/* 174 */     this.thumbStyle = null;
/*     */ 
/* 176 */     super.uninstallDefaults();
/*     */   }
/*     */ 
/*     */   public SynthContext getContext(JComponent paramJComponent)
/*     */   {
/* 184 */     return getContext(paramJComponent, SynthLookAndFeel.getComponentState(paramJComponent));
/*     */   }
/*     */ 
/*     */   private SynthContext getContext(JComponent paramJComponent, int paramInt) {
/* 188 */     return SynthContext.getContext(SynthContext.class, paramJComponent, SynthLookAndFeel.getRegion(paramJComponent), this.style, paramInt);
/*     */   }
/*     */ 
/*     */   private SynthContext getContext(JComponent paramJComponent, Region paramRegion)
/*     */   {
/* 193 */     return getContext(paramJComponent, paramRegion, getComponentState(paramJComponent, paramRegion));
/*     */   }
/*     */ 
/*     */   private SynthContext getContext(JComponent paramJComponent, Region paramRegion, int paramInt) {
/* 197 */     SynthStyle localSynthStyle = this.trackStyle;
/*     */ 
/* 199 */     if (paramRegion == Region.SCROLL_BAR_THUMB) {
/* 200 */       localSynthStyle = this.thumbStyle;
/*     */     }
/* 202 */     return SynthContext.getContext(SynthContext.class, paramJComponent, paramRegion, localSynthStyle, paramInt);
/*     */   }
/*     */ 
/*     */   private int getComponentState(JComponent paramJComponent, Region paramRegion)
/*     */   {
/* 207 */     if ((paramRegion == Region.SCROLL_BAR_THUMB) && (isThumbRollover()) && (paramJComponent.isEnabled()))
/*     */     {
/* 209 */       return 2;
/*     */     }
/* 211 */     return SynthLookAndFeel.getComponentState(paramJComponent);
/*     */   }
/*     */ 
/*     */   public boolean getSupportsAbsolutePositioning()
/*     */   {
/* 219 */     SynthContext localSynthContext = getContext(this.scrollbar);
/* 220 */     boolean bool = this.style.getBoolean(localSynthContext, "ScrollBar.allowsAbsolutePositioning", false);
/*     */ 
/* 222 */     localSynthContext.dispose();
/* 223 */     return bool;
/*     */   }
/*     */ 
/*     */   public void update(Graphics paramGraphics, JComponent paramJComponent)
/*     */   {
/* 240 */     SynthContext localSynthContext = getContext(paramJComponent);
/*     */ 
/* 242 */     SynthLookAndFeel.update(localSynthContext, paramGraphics);
/* 243 */     localSynthContext.getPainter().paintScrollBarBackground(localSynthContext, paramGraphics, 0, 0, paramJComponent.getWidth(), paramJComponent.getHeight(), this.scrollbar.getOrientation());
/*     */ 
/* 246 */     paint(localSynthContext, paramGraphics);
/* 247 */     localSynthContext.dispose();
/*     */   }
/*     */ 
/*     */   public void paint(Graphics paramGraphics, JComponent paramJComponent)
/*     */   {
/* 261 */     SynthContext localSynthContext = getContext(paramJComponent);
/*     */ 
/* 263 */     paint(localSynthContext, paramGraphics);
/* 264 */     localSynthContext.dispose();
/*     */   }
/*     */ 
/*     */   protected void paint(SynthContext paramSynthContext, Graphics paramGraphics)
/*     */   {
/* 275 */     SynthContext localSynthContext = getContext(this.scrollbar, Region.SCROLL_BAR_TRACK);
/*     */ 
/* 277 */     paintTrack(localSynthContext, paramGraphics, getTrackBounds());
/* 278 */     localSynthContext.dispose();
/*     */ 
/* 280 */     localSynthContext = getContext(this.scrollbar, Region.SCROLL_BAR_THUMB);
/* 281 */     paintThumb(localSynthContext, paramGraphics, getThumbBounds());
/* 282 */     localSynthContext.dispose();
/*     */   }
/*     */ 
/*     */   public void paintBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/* 291 */     paramSynthContext.getPainter().paintScrollBarBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, this.scrollbar.getOrientation());
/*     */   }
/*     */ 
/*     */   protected void paintTrack(SynthContext paramSynthContext, Graphics paramGraphics, Rectangle paramRectangle)
/*     */   {
/* 304 */     SynthLookAndFeel.updateSubregion(paramSynthContext, paramGraphics, paramRectangle);
/* 305 */     paramSynthContext.getPainter().paintScrollBarTrackBackground(paramSynthContext, paramGraphics, paramRectangle.x, paramRectangle.y, paramRectangle.width, paramRectangle.height, this.scrollbar.getOrientation());
/*     */ 
/* 308 */     paramSynthContext.getPainter().paintScrollBarTrackBorder(paramSynthContext, paramGraphics, paramRectangle.x, paramRectangle.y, paramRectangle.width, paramRectangle.height, this.scrollbar.getOrientation());
/*     */   }
/*     */ 
/*     */   protected void paintThumb(SynthContext paramSynthContext, Graphics paramGraphics, Rectangle paramRectangle)
/*     */   {
/* 322 */     SynthLookAndFeel.updateSubregion(paramSynthContext, paramGraphics, paramRectangle);
/* 323 */     int i = this.scrollbar.getOrientation();
/* 324 */     paramSynthContext.getPainter().paintScrollBarThumbBackground(paramSynthContext, paramGraphics, paramRectangle.x, paramRectangle.y, paramRectangle.width, paramRectangle.height, i);
/*     */ 
/* 327 */     paramSynthContext.getPainter().paintScrollBarThumbBorder(paramSynthContext, paramGraphics, paramRectangle.x, paramRectangle.y, paramRectangle.width, paramRectangle.height, i);
/*     */   }
/*     */ 
/*     */   public Dimension getPreferredSize(JComponent paramJComponent)
/*     */   {
/* 350 */     Insets localInsets = paramJComponent.getInsets();
/* 351 */     return this.scrollbar.getOrientation() == 1 ? new Dimension(this.scrollBarWidth + localInsets.left + localInsets.right, 48) : new Dimension(48, this.scrollBarWidth + localInsets.top + localInsets.bottom);
/*     */   }
/*     */ 
/*     */   protected Dimension getMinimumThumbSize()
/*     */   {
/* 361 */     if (!this.validMinimumThumbSize) {
/* 362 */       if (this.scrollbar.getOrientation() == 1) {
/* 363 */         this.minimumThumbSize.width = this.scrollBarWidth;
/* 364 */         this.minimumThumbSize.height = 7;
/*     */       } else {
/* 366 */         this.minimumThumbSize.width = 7;
/* 367 */         this.minimumThumbSize.height = this.scrollBarWidth;
/*     */       }
/*     */     }
/* 370 */     return this.minimumThumbSize;
/*     */   }
/*     */ 
/*     */   protected JButton createDecreaseButton(int paramInt)
/*     */   {
/* 378 */     SynthArrowButton local1 = new SynthArrowButton(paramInt)
/*     */     {
/*     */       public boolean contains(int paramAnonymousInt1, int paramAnonymousInt2) {
/* 381 */         if (SynthScrollBarUI.this.decrGap < 0) {
/* 382 */           int i = getWidth();
/* 383 */           int j = getHeight();
/* 384 */           if (SynthScrollBarUI.this.scrollbar.getOrientation() == 1)
/*     */           {
/* 387 */             j += SynthScrollBarUI.this.decrGap;
/*     */           }
/*     */           else
/*     */           {
/* 391 */             i += SynthScrollBarUI.this.decrGap;
/*     */           }
/* 393 */           return (paramAnonymousInt1 >= 0) && (paramAnonymousInt1 < i) && (paramAnonymousInt2 >= 0) && (paramAnonymousInt2 < j);
/*     */         }
/* 395 */         return super.contains(paramAnonymousInt1, paramAnonymousInt2);
/*     */       }
/*     */     };
/* 398 */     local1.setName("ScrollBar.button");
/* 399 */     return local1;
/*     */   }
/*     */ 
/*     */   protected JButton createIncreaseButton(int paramInt)
/*     */   {
/* 407 */     SynthArrowButton local2 = new SynthArrowButton(paramInt)
/*     */     {
/*     */       public boolean contains(int paramAnonymousInt1, int paramAnonymousInt2) {
/* 410 */         if (SynthScrollBarUI.this.incrGap < 0) {
/* 411 */           int i = getWidth();
/* 412 */           int j = getHeight();
/* 413 */           if (SynthScrollBarUI.this.scrollbar.getOrientation() == 1)
/*     */           {
/* 416 */             j += SynthScrollBarUI.this.incrGap;
/* 417 */             paramAnonymousInt2 += SynthScrollBarUI.this.incrGap;
/*     */           }
/*     */           else
/*     */           {
/* 421 */             i += SynthScrollBarUI.this.incrGap;
/* 422 */             paramAnonymousInt1 += SynthScrollBarUI.this.incrGap;
/*     */           }
/* 424 */           return (paramAnonymousInt1 >= 0) && (paramAnonymousInt1 < i) && (paramAnonymousInt2 >= 0) && (paramAnonymousInt2 < j);
/*     */         }
/* 426 */         return super.contains(paramAnonymousInt1, paramAnonymousInt2);
/*     */       }
/*     */     };
/* 429 */     local2.setName("ScrollBar.button");
/* 430 */     return local2;
/*     */   }
/*     */ 
/*     */   protected void setThumbRollover(boolean paramBoolean)
/*     */   {
/* 438 */     if (isThumbRollover() != paramBoolean) {
/* 439 */       this.scrollbar.repaint(getThumbBounds());
/* 440 */       super.setThumbRollover(paramBoolean);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void updateButtonDirections() {
/* 445 */     int i = this.scrollbar.getOrientation();
/* 446 */     if (this.scrollbar.getComponentOrientation().isLeftToRight()) {
/* 447 */       ((SynthArrowButton)this.incrButton).setDirection(i == 0 ? 3 : 5);
/*     */ 
/* 449 */       ((SynthArrowButton)this.decrButton).setDirection(i == 0 ? 7 : 1);
/*     */     }
/*     */     else
/*     */     {
/* 453 */       ((SynthArrowButton)this.incrButton).setDirection(i == 0 ? 7 : 5);
/*     */ 
/* 455 */       ((SynthArrowButton)this.decrButton).setDirection(i == 0 ? 3 : 1);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void propertyChange(PropertyChangeEvent paramPropertyChangeEvent)
/*     */   {
/* 464 */     String str = paramPropertyChangeEvent.getPropertyName();
/*     */ 
/* 466 */     if (SynthLookAndFeel.shouldUpdateStyle(paramPropertyChangeEvent)) {
/* 467 */       updateStyle((JScrollBar)paramPropertyChangeEvent.getSource());
/*     */     }
/*     */ 
/* 470 */     if ("orientation" == str) {
/* 471 */       updateButtonDirections();
/*     */     }
/* 473 */     else if ("componentOrientation" == str)
/* 474 */       updateButtonDirections();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.synth.SynthScrollBarUI
 * JD-Core Version:    0.6.2
 */