/*     */ package javax.swing.plaf.synth;
/*     */ 
/*     */ import java.awt.Canvas;
/*     */ import java.awt.Component;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Shape;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JSplitPane;
/*     */ import javax.swing.KeyStroke;
/*     */ import javax.swing.LookAndFeel;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.basic.BasicSplitPaneDivider;
/*     */ import javax.swing.plaf.basic.BasicSplitPaneUI;
/*     */ 
/*     */ public class SynthSplitPaneUI extends BasicSplitPaneUI
/*     */   implements PropertyChangeListener, SynthUI
/*     */ {
/*     */   private static Set<KeyStroke> managingFocusForwardTraversalKeys;
/*     */   private static Set<KeyStroke> managingFocusBackwardTraversalKeys;
/*     */   private SynthStyle style;
/*     */   private SynthStyle dividerStyle;
/*     */ 
/*     */   public static ComponentUI createUI(JComponent paramJComponent)
/*     */   {
/*  76 */     return new SynthSplitPaneUI();
/*     */   }
/*     */ 
/*     */   protected void installDefaults()
/*     */   {
/*  84 */     updateStyle(this.splitPane);
/*     */ 
/*  86 */     setOrientation(this.splitPane.getOrientation());
/*  87 */     setContinuousLayout(this.splitPane.isContinuousLayout());
/*     */ 
/*  89 */     resetLayoutManager();
/*     */ 
/*  93 */     if (this.nonContinuousLayoutDivider == null) {
/*  94 */       setNonContinuousLayoutDivider(createDefaultNonContinuousLayoutDivider(), true);
/*     */     }
/*     */     else
/*     */     {
/*  98 */       setNonContinuousLayoutDivider(this.nonContinuousLayoutDivider, true);
/*     */     }
/*     */ 
/* 102 */     if (managingFocusForwardTraversalKeys == null) {
/* 103 */       managingFocusForwardTraversalKeys = new HashSet();
/* 104 */       managingFocusForwardTraversalKeys.add(KeyStroke.getKeyStroke(9, 0));
/*     */     }
/*     */ 
/* 107 */     this.splitPane.setFocusTraversalKeys(0, managingFocusForwardTraversalKeys);
/*     */ 
/* 110 */     if (managingFocusBackwardTraversalKeys == null) {
/* 111 */       managingFocusBackwardTraversalKeys = new HashSet();
/* 112 */       managingFocusBackwardTraversalKeys.add(KeyStroke.getKeyStroke(9, 1));
/*     */     }
/*     */ 
/* 115 */     this.splitPane.setFocusTraversalKeys(1, managingFocusBackwardTraversalKeys);
/*     */   }
/*     */ 
/*     */   private void updateStyle(JSplitPane paramJSplitPane)
/*     */   {
/* 120 */     SynthContext localSynthContext = getContext(paramJSplitPane, Region.SPLIT_PANE_DIVIDER, 1);
/*     */ 
/* 122 */     SynthStyle localSynthStyle1 = this.dividerStyle;
/* 123 */     this.dividerStyle = SynthLookAndFeel.updateStyle(localSynthContext, this);
/* 124 */     localSynthContext.dispose();
/*     */ 
/* 126 */     localSynthContext = getContext(paramJSplitPane, 1);
/* 127 */     SynthStyle localSynthStyle2 = this.style;
/*     */ 
/* 129 */     this.style = SynthLookAndFeel.updateStyle(localSynthContext, this);
/*     */ 
/* 131 */     if (this.style != localSynthStyle2) {
/* 132 */       Object localObject = this.style.get(localSynthContext, "SplitPane.size");
/* 133 */       if (localObject == null) {
/* 134 */         localObject = Integer.valueOf(6);
/*     */       }
/* 136 */       LookAndFeel.installProperty(paramJSplitPane, "dividerSize", localObject);
/*     */ 
/* 138 */       localObject = this.style.get(localSynthContext, "SplitPane.oneTouchExpandable");
/* 139 */       if (localObject != null) {
/* 140 */         LookAndFeel.installProperty(paramJSplitPane, "oneTouchExpandable", localObject);
/*     */       }
/*     */ 
/* 143 */       if (this.divider != null) {
/* 144 */         paramJSplitPane.remove(this.divider);
/* 145 */         this.divider.setDividerSize(paramJSplitPane.getDividerSize());
/*     */       }
/* 147 */       if (localSynthStyle2 != null) {
/* 148 */         uninstallKeyboardActions();
/* 149 */         installKeyboardActions();
/*     */       }
/*     */     }
/* 152 */     if ((this.style != localSynthStyle2) || (this.dividerStyle != localSynthStyle1))
/*     */     {
/* 155 */       if (this.divider != null) {
/* 156 */         paramJSplitPane.remove(this.divider);
/*     */       }
/* 158 */       this.divider = createDefaultDivider();
/* 159 */       this.divider.setBasicSplitPaneUI(this);
/* 160 */       paramJSplitPane.add(this.divider, "divider");
/*     */     }
/* 162 */     localSynthContext.dispose();
/*     */   }
/*     */ 
/*     */   protected void installListeners()
/*     */   {
/* 170 */     super.installListeners();
/* 171 */     this.splitPane.addPropertyChangeListener(this);
/*     */   }
/*     */ 
/*     */   protected void uninstallDefaults()
/*     */   {
/* 179 */     SynthContext localSynthContext = getContext(this.splitPane, 1);
/*     */ 
/* 181 */     this.style.uninstallDefaults(localSynthContext);
/* 182 */     localSynthContext.dispose();
/* 183 */     this.style = null;
/*     */ 
/* 185 */     localSynthContext = getContext(this.splitPane, Region.SPLIT_PANE_DIVIDER, 1);
/* 186 */     this.dividerStyle.uninstallDefaults(localSynthContext);
/* 187 */     localSynthContext.dispose();
/* 188 */     this.dividerStyle = null;
/*     */ 
/* 190 */     super.uninstallDefaults();
/*     */   }
/*     */ 
/*     */   protected void uninstallListeners()
/*     */   {
/* 199 */     super.uninstallListeners();
/* 200 */     this.splitPane.removePropertyChangeListener(this);
/*     */   }
/*     */ 
/*     */   public SynthContext getContext(JComponent paramJComponent)
/*     */   {
/* 208 */     return getContext(paramJComponent, SynthLookAndFeel.getComponentState(paramJComponent));
/*     */   }
/*     */ 
/*     */   private SynthContext getContext(JComponent paramJComponent, int paramInt) {
/* 212 */     return SynthContext.getContext(SynthContext.class, paramJComponent, SynthLookAndFeel.getRegion(paramJComponent), this.style, paramInt);
/*     */   }
/*     */ 
/*     */   SynthContext getContext(JComponent paramJComponent, Region paramRegion)
/*     */   {
/* 217 */     return getContext(paramJComponent, paramRegion, getComponentState(paramJComponent, paramRegion));
/*     */   }
/*     */ 
/*     */   private SynthContext getContext(JComponent paramJComponent, Region paramRegion, int paramInt) {
/* 221 */     if (paramRegion == Region.SPLIT_PANE_DIVIDER) {
/* 222 */       return SynthContext.getContext(SynthContext.class, paramJComponent, paramRegion, this.dividerStyle, paramInt);
/*     */     }
/*     */ 
/* 225 */     return SynthContext.getContext(SynthContext.class, paramJComponent, paramRegion, this.style, paramInt);
/*     */   }
/*     */ 
/*     */   private int getComponentState(JComponent paramJComponent, Region paramRegion)
/*     */   {
/* 230 */     int i = SynthLookAndFeel.getComponentState(paramJComponent);
/*     */ 
/* 232 */     if (this.divider.isMouseOver()) {
/* 233 */       i |= 2;
/*     */     }
/* 235 */     return i;
/*     */   }
/*     */ 
/*     */   public void propertyChange(PropertyChangeEvent paramPropertyChangeEvent)
/*     */   {
/* 243 */     if (SynthLookAndFeel.shouldUpdateStyle(paramPropertyChangeEvent))
/* 244 */       updateStyle((JSplitPane)paramPropertyChangeEvent.getSource());
/*     */   }
/*     */ 
/*     */   public BasicSplitPaneDivider createDefaultDivider()
/*     */   {
/* 253 */     SynthSplitPaneDivider localSynthSplitPaneDivider = new SynthSplitPaneDivider(this);
/*     */ 
/* 255 */     localSynthSplitPaneDivider.setDividerSize(this.splitPane.getDividerSize());
/* 256 */     return localSynthSplitPaneDivider;
/*     */   }
/*     */ 
/*     */   protected Component createDefaultNonContinuousLayoutDivider()
/*     */   {
/* 264 */     return new Canvas() {
/*     */       public void paint(Graphics paramAnonymousGraphics) {
/* 266 */         SynthSplitPaneUI.this.paintDragDivider(paramAnonymousGraphics, 0, 0, getWidth(), getHeight());
/*     */       }
/*     */     };
/*     */   }
/*     */ 
/*     */   public void update(Graphics paramGraphics, JComponent paramJComponent)
/*     */   {
/* 285 */     SynthContext localSynthContext = getContext(paramJComponent);
/*     */ 
/* 287 */     SynthLookAndFeel.update(localSynthContext, paramGraphics);
/* 288 */     localSynthContext.getPainter().paintSplitPaneBackground(localSynthContext, paramGraphics, 0, 0, paramJComponent.getWidth(), paramJComponent.getHeight());
/*     */ 
/* 290 */     paint(localSynthContext, paramGraphics);
/* 291 */     localSynthContext.dispose();
/*     */   }
/*     */ 
/*     */   public void paint(Graphics paramGraphics, JComponent paramJComponent)
/*     */   {
/* 305 */     SynthContext localSynthContext = getContext(paramJComponent);
/*     */ 
/* 307 */     paint(localSynthContext, paramGraphics);
/* 308 */     localSynthContext.dispose();
/*     */   }
/*     */ 
/*     */   protected void paint(SynthContext paramSynthContext, Graphics paramGraphics)
/*     */   {
/* 321 */     super.paint(paramGraphics, this.splitPane);
/*     */   }
/*     */ 
/*     */   public void paintBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/* 330 */     paramSynthContext.getPainter().paintSplitPaneBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*     */   }
/*     */ 
/*     */   private void paintDragDivider(Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
/* 334 */     SynthContext localSynthContext = getContext(this.splitPane, Region.SPLIT_PANE_DIVIDER);
/* 335 */     localSynthContext.setComponentState((localSynthContext.getComponentState() | 0x2) ^ 0x2 | 0x4);
/*     */ 
/* 337 */     Shape localShape = paramGraphics.getClip();
/* 338 */     paramGraphics.clipRect(paramInt1, paramInt2, paramInt3, paramInt4);
/* 339 */     localSynthContext.getPainter().paintSplitPaneDragDivider(localSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, this.splitPane.getOrientation());
/*     */ 
/* 341 */     paramGraphics.setClip(localShape);
/* 342 */     localSynthContext.dispose();
/*     */   }
/*     */ 
/*     */   public void finishedPaintingChildren(JSplitPane paramJSplitPane, Graphics paramGraphics)
/*     */   {
/* 350 */     if ((paramJSplitPane == this.splitPane) && (getLastDragLocation() != -1) && (!isContinuousLayout()) && (!this.draggingHW))
/*     */     {
/* 352 */       if (paramJSplitPane.getOrientation() == 1) {
/* 353 */         paintDragDivider(paramGraphics, getLastDragLocation(), 0, this.dividerSize - 1, this.splitPane.getHeight() - 1);
/*     */       }
/*     */       else
/* 356 */         paintDragDivider(paramGraphics, 0, getLastDragLocation(), this.splitPane.getWidth() - 1, this.dividerSize - 1);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.synth.SynthSplitPaneUI
 * JD-Core Version:    0.6.2
 */