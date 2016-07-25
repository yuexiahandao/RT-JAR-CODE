/*     */ package javax.swing.plaf.synth;
/*     */ 
/*     */ import java.awt.Graphics;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.ViewportUI;
/*     */ 
/*     */ public class SynthViewportUI extends ViewportUI
/*     */   implements PropertyChangeListener, SynthUI
/*     */ {
/*     */   private SynthStyle style;
/*     */ 
/*     */   public static ComponentUI createUI(JComponent paramJComponent)
/*     */   {
/*  51 */     return new SynthViewportUI();
/*     */   }
/*     */ 
/*     */   public void installUI(JComponent paramJComponent)
/*     */   {
/*  59 */     super.installUI(paramJComponent);
/*  60 */     installDefaults(paramJComponent);
/*  61 */     installListeners(paramJComponent);
/*     */   }
/*     */ 
/*     */   public void uninstallUI(JComponent paramJComponent)
/*     */   {
/*  69 */     super.uninstallUI(paramJComponent);
/*  70 */     uninstallListeners(paramJComponent);
/*  71 */     uninstallDefaults(paramJComponent);
/*     */   }
/*     */ 
/*     */   protected void installDefaults(JComponent paramJComponent)
/*     */   {
/*  80 */     updateStyle(paramJComponent);
/*     */   }
/*     */ 
/*     */   private void updateStyle(JComponent paramJComponent) {
/*  84 */     SynthContext localSynthContext = getContext(paramJComponent, 1);
/*     */ 
/*  90 */     SynthStyle localSynthStyle1 = SynthLookAndFeel.getStyle(localSynthContext.getComponent(), localSynthContext.getRegion());
/*     */ 
/*  92 */     SynthStyle localSynthStyle2 = localSynthContext.getStyle();
/*     */ 
/*  94 */     if (localSynthStyle1 != localSynthStyle2) {
/*  95 */       if (localSynthStyle2 != null) {
/*  96 */         localSynthStyle2.uninstallDefaults(localSynthContext);
/*     */       }
/*  98 */       localSynthContext.setStyle(localSynthStyle1);
/*  99 */       localSynthStyle1.installDefaults(localSynthContext);
/*     */     }
/* 101 */     this.style = localSynthStyle1;
/* 102 */     localSynthContext.dispose();
/*     */   }
/*     */ 
/*     */   protected void installListeners(JComponent paramJComponent)
/*     */   {
/* 111 */     paramJComponent.addPropertyChangeListener(this);
/*     */   }
/*     */ 
/*     */   protected void uninstallListeners(JComponent paramJComponent)
/*     */   {
/* 120 */     paramJComponent.removePropertyChangeListener(this);
/*     */   }
/*     */ 
/*     */   protected void uninstallDefaults(JComponent paramJComponent)
/*     */   {
/* 129 */     SynthContext localSynthContext = getContext(paramJComponent, 1);
/* 130 */     this.style.uninstallDefaults(localSynthContext);
/* 131 */     localSynthContext.dispose();
/* 132 */     this.style = null;
/*     */   }
/*     */ 
/*     */   public SynthContext getContext(JComponent paramJComponent)
/*     */   {
/* 140 */     return getContext(paramJComponent, SynthLookAndFeel.getComponentState(paramJComponent));
/*     */   }
/*     */ 
/*     */   private SynthContext getContext(JComponent paramJComponent, int paramInt) {
/* 144 */     return SynthContext.getContext(SynthContext.class, paramJComponent, getRegion(paramJComponent), this.style, paramInt);
/*     */   }
/*     */ 
/*     */   private Region getRegion(JComponent paramJComponent)
/*     */   {
/* 149 */     return SynthLookAndFeel.getRegion(paramJComponent);
/*     */   }
/*     */ 
/*     */   public void update(Graphics paramGraphics, JComponent paramJComponent)
/*     */   {
/* 166 */     SynthContext localSynthContext = getContext(paramJComponent);
/*     */ 
/* 168 */     SynthLookAndFeel.update(localSynthContext, paramGraphics);
/* 169 */     localSynthContext.getPainter().paintViewportBackground(localSynthContext, paramGraphics, 0, 0, paramJComponent.getWidth(), paramJComponent.getHeight());
/*     */ 
/* 171 */     paint(localSynthContext, paramGraphics);
/* 172 */     localSynthContext.dispose();
/*     */   }
/*     */ 
/*     */   public void paintBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void paint(Graphics paramGraphics, JComponent paramJComponent)
/*     */   {
/* 203 */     SynthContext localSynthContext = getContext(paramJComponent);
/*     */ 
/* 205 */     paint(localSynthContext, paramGraphics);
/* 206 */     localSynthContext.dispose();
/*     */   }
/*     */ 
/*     */   protected void paint(SynthContext paramSynthContext, Graphics paramGraphics)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void propertyChange(PropertyChangeEvent paramPropertyChangeEvent)
/*     */   {
/* 224 */     if (SynthLookAndFeel.shouldUpdateStyle(paramPropertyChangeEvent))
/* 225 */       updateStyle((JComponent)paramPropertyChangeEvent.getSource());
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.synth.SynthViewportUI
 * JD-Core Version:    0.6.2
 */