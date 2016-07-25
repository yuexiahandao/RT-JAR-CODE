/*     */ package javax.swing.plaf.synth;
/*     */ 
/*     */ import java.awt.Graphics;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.basic.BasicPanelUI;
/*     */ 
/*     */ public class SynthPanelUI extends BasicPanelUI
/*     */   implements PropertyChangeListener, SynthUI
/*     */ {
/*     */   private SynthStyle style;
/*     */ 
/*     */   public static ComponentUI createUI(JComponent paramJComponent)
/*     */   {
/*  52 */     return new SynthPanelUI();
/*     */   }
/*     */ 
/*     */   public void installUI(JComponent paramJComponent)
/*     */   {
/*  60 */     JPanel localJPanel = (JPanel)paramJComponent;
/*     */ 
/*  62 */     super.installUI(paramJComponent);
/*  63 */     installListeners(localJPanel);
/*     */   }
/*     */ 
/*     */   public void uninstallUI(JComponent paramJComponent)
/*     */   {
/*  71 */     JPanel localJPanel = (JPanel)paramJComponent;
/*     */ 
/*  73 */     uninstallListeners(localJPanel);
/*  74 */     super.uninstallUI(paramJComponent);
/*     */   }
/*     */ 
/*     */   protected void installListeners(JPanel paramJPanel)
/*     */   {
/*  83 */     paramJPanel.addPropertyChangeListener(this);
/*     */   }
/*     */ 
/*     */   protected void uninstallListeners(JPanel paramJPanel)
/*     */   {
/*  92 */     paramJPanel.removePropertyChangeListener(this);
/*     */   }
/*     */ 
/*     */   protected void installDefaults(JPanel paramJPanel)
/*     */   {
/* 100 */     updateStyle(paramJPanel);
/*     */   }
/*     */ 
/*     */   protected void uninstallDefaults(JPanel paramJPanel)
/*     */   {
/* 108 */     SynthContext localSynthContext = getContext(paramJPanel, 1);
/*     */ 
/* 110 */     this.style.uninstallDefaults(localSynthContext);
/* 111 */     localSynthContext.dispose();
/* 112 */     this.style = null;
/*     */   }
/*     */ 
/*     */   private void updateStyle(JPanel paramJPanel) {
/* 116 */     SynthContext localSynthContext = getContext(paramJPanel, 1);
/* 117 */     this.style = SynthLookAndFeel.updateStyle(localSynthContext, this);
/* 118 */     localSynthContext.dispose();
/*     */   }
/*     */ 
/*     */   public SynthContext getContext(JComponent paramJComponent)
/*     */   {
/* 126 */     return getContext(paramJComponent, getComponentState(paramJComponent));
/*     */   }
/*     */ 
/*     */   private SynthContext getContext(JComponent paramJComponent, int paramInt) {
/* 130 */     return SynthContext.getContext(SynthContext.class, paramJComponent, SynthLookAndFeel.getRegion(paramJComponent), this.style, paramInt);
/*     */   }
/*     */ 
/*     */   private int getComponentState(JComponent paramJComponent)
/*     */   {
/* 135 */     return SynthLookAndFeel.getComponentState(paramJComponent);
/*     */   }
/*     */ 
/*     */   public void update(Graphics paramGraphics, JComponent paramJComponent)
/*     */   {
/* 152 */     SynthContext localSynthContext = getContext(paramJComponent);
/*     */ 
/* 154 */     SynthLookAndFeel.update(localSynthContext, paramGraphics);
/* 155 */     localSynthContext.getPainter().paintPanelBackground(localSynthContext, paramGraphics, 0, 0, paramJComponent.getWidth(), paramJComponent.getHeight());
/*     */ 
/* 157 */     paint(localSynthContext, paramGraphics);
/* 158 */     localSynthContext.dispose();
/*     */   }
/*     */ 
/*     */   public void paint(Graphics paramGraphics, JComponent paramJComponent)
/*     */   {
/* 172 */     SynthContext localSynthContext = getContext(paramJComponent);
/*     */ 
/* 174 */     paint(localSynthContext, paramGraphics);
/* 175 */     localSynthContext.dispose();
/*     */   }
/*     */ 
/*     */   protected void paint(SynthContext paramSynthContext, Graphics paramGraphics)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void paintBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/* 195 */     paramSynthContext.getPainter().paintPanelBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*     */   }
/*     */ 
/*     */   public void propertyChange(PropertyChangeEvent paramPropertyChangeEvent)
/*     */   {
/* 203 */     if (SynthLookAndFeel.shouldUpdateStyle(paramPropertyChangeEvent))
/* 204 */       updateStyle((JPanel)paramPropertyChangeEvent.getSource());
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.synth.SynthPanelUI
 * JD-Core Version:    0.6.2
 */