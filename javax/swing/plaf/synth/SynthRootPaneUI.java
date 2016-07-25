/*     */ package javax.swing.plaf.synth;
/*     */ 
/*     */ import java.awt.Graphics;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JRootPane;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.basic.BasicRootPaneUI;
/*     */ 
/*     */ public class SynthRootPaneUI extends BasicRootPaneUI
/*     */   implements SynthUI
/*     */ {
/*     */   private SynthStyle style;
/*     */ 
/*     */   public static ComponentUI createUI(JComponent paramJComponent)
/*     */   {
/*  51 */     return new SynthRootPaneUI();
/*     */   }
/*     */ 
/*     */   protected void installDefaults(JRootPane paramJRootPane)
/*     */   {
/*  59 */     updateStyle(paramJRootPane);
/*     */   }
/*     */ 
/*     */   protected void uninstallDefaults(JRootPane paramJRootPane)
/*     */   {
/*  67 */     SynthContext localSynthContext = getContext(paramJRootPane, 1);
/*     */ 
/*  69 */     this.style.uninstallDefaults(localSynthContext);
/*  70 */     localSynthContext.dispose();
/*  71 */     this.style = null;
/*     */   }
/*     */ 
/*     */   public SynthContext getContext(JComponent paramJComponent)
/*     */   {
/*  79 */     return getContext(paramJComponent, getComponentState(paramJComponent));
/*     */   }
/*     */ 
/*     */   private SynthContext getContext(JComponent paramJComponent, int paramInt) {
/*  83 */     return SynthContext.getContext(SynthContext.class, paramJComponent, SynthLookAndFeel.getRegion(paramJComponent), this.style, paramInt);
/*     */   }
/*     */ 
/*     */   private int getComponentState(JComponent paramJComponent)
/*     */   {
/*  88 */     return SynthLookAndFeel.getComponentState(paramJComponent);
/*     */   }
/*     */ 
/*     */   private void updateStyle(JComponent paramJComponent) {
/*  92 */     SynthContext localSynthContext = getContext(paramJComponent, 1);
/*  93 */     SynthStyle localSynthStyle = this.style;
/*  94 */     this.style = SynthLookAndFeel.updateStyle(localSynthContext, this);
/*  95 */     if ((this.style != localSynthStyle) && 
/*  96 */       (localSynthStyle != null)) {
/*  97 */       uninstallKeyboardActions((JRootPane)paramJComponent);
/*  98 */       installKeyboardActions((JRootPane)paramJComponent);
/*     */     }
/*     */ 
/* 101 */     localSynthContext.dispose();
/*     */   }
/*     */ 
/*     */   public void update(Graphics paramGraphics, JComponent paramJComponent)
/*     */   {
/* 118 */     SynthContext localSynthContext = getContext(paramJComponent);
/*     */ 
/* 120 */     SynthLookAndFeel.update(localSynthContext, paramGraphics);
/* 121 */     localSynthContext.getPainter().paintRootPaneBackground(localSynthContext, paramGraphics, 0, 0, paramJComponent.getWidth(), paramJComponent.getHeight());
/*     */ 
/* 123 */     paint(localSynthContext, paramGraphics);
/* 124 */     localSynthContext.dispose();
/*     */   }
/*     */ 
/*     */   public void paint(Graphics paramGraphics, JComponent paramJComponent)
/*     */   {
/* 138 */     SynthContext localSynthContext = getContext(paramJComponent);
/*     */ 
/* 140 */     paint(localSynthContext, paramGraphics);
/* 141 */     localSynthContext.dispose();
/*     */   }
/*     */ 
/*     */   protected void paint(SynthContext paramSynthContext, Graphics paramGraphics)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void paintBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/* 160 */     paramSynthContext.getPainter().paintRootPaneBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*     */   }
/*     */ 
/*     */   public void propertyChange(PropertyChangeEvent paramPropertyChangeEvent)
/*     */   {
/* 170 */     if (SynthLookAndFeel.shouldUpdateStyle(paramPropertyChangeEvent)) {
/* 171 */       updateStyle((JRootPane)paramPropertyChangeEvent.getSource());
/*     */     }
/* 173 */     super.propertyChange(paramPropertyChangeEvent);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.synth.SynthRootPaneUI
 * JD-Core Version:    0.6.2
 */