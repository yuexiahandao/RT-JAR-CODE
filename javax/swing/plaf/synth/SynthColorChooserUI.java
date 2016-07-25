/*     */ package javax.swing.plaf.synth;
/*     */ 
/*     */ import java.awt.Graphics;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import javax.swing.JColorChooser;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.colorchooser.AbstractColorChooserPanel;
/*     */ import javax.swing.colorchooser.ColorChooserComponentFactory;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.basic.BasicColorChooserUI;
/*     */ 
/*     */ public class SynthColorChooserUI extends BasicColorChooserUI
/*     */   implements PropertyChangeListener, SynthUI
/*     */ {
/*     */   private SynthStyle style;
/*     */ 
/*     */   public static ComponentUI createUI(JComponent paramJComponent)
/*     */   {
/*  57 */     return new SynthColorChooserUI();
/*     */   }
/*     */ 
/*     */   protected AbstractColorChooserPanel[] createDefaultChoosers()
/*     */   {
/*  65 */     SynthContext localSynthContext = getContext(this.chooser, 1);
/*  66 */     AbstractColorChooserPanel[] arrayOfAbstractColorChooserPanel = (AbstractColorChooserPanel[])localSynthContext.getStyle().get(localSynthContext, "ColorChooser.panels");
/*     */ 
/*  68 */     localSynthContext.dispose();
/*     */ 
/*  70 */     if (arrayOfAbstractColorChooserPanel == null) {
/*  71 */       arrayOfAbstractColorChooserPanel = ColorChooserComponentFactory.getDefaultChooserPanels();
/*     */     }
/*  73 */     return arrayOfAbstractColorChooserPanel;
/*     */   }
/*     */ 
/*     */   protected void installDefaults()
/*     */   {
/*  81 */     super.installDefaults();
/*  82 */     updateStyle(this.chooser);
/*     */   }
/*     */ 
/*     */   private void updateStyle(JComponent paramJComponent) {
/*  86 */     SynthContext localSynthContext = getContext(paramJComponent, 1);
/*  87 */     this.style = SynthLookAndFeel.updateStyle(localSynthContext, this);
/*  88 */     localSynthContext.dispose();
/*     */   }
/*     */ 
/*     */   protected void uninstallDefaults()
/*     */   {
/*  96 */     SynthContext localSynthContext = getContext(this.chooser, 1);
/*     */ 
/*  98 */     this.style.uninstallDefaults(localSynthContext);
/*  99 */     localSynthContext.dispose();
/* 100 */     this.style = null;
/* 101 */     super.uninstallDefaults();
/*     */   }
/*     */ 
/*     */   protected void installListeners()
/*     */   {
/* 109 */     super.installListeners();
/* 110 */     this.chooser.addPropertyChangeListener(this);
/*     */   }
/*     */ 
/*     */   protected void uninstallListeners()
/*     */   {
/* 118 */     this.chooser.removePropertyChangeListener(this);
/* 119 */     super.uninstallListeners();
/*     */   }
/*     */ 
/*     */   public SynthContext getContext(JComponent paramJComponent)
/*     */   {
/* 127 */     return getContext(paramJComponent, getComponentState(paramJComponent));
/*     */   }
/*     */ 
/*     */   private SynthContext getContext(JComponent paramJComponent, int paramInt) {
/* 131 */     return SynthContext.getContext(SynthContext.class, paramJComponent, SynthLookAndFeel.getRegion(paramJComponent), this.style, paramInt);
/*     */   }
/*     */ 
/*     */   private int getComponentState(JComponent paramJComponent)
/*     */   {
/* 136 */     return SynthLookAndFeel.getComponentState(paramJComponent);
/*     */   }
/*     */ 
/*     */   public void update(Graphics paramGraphics, JComponent paramJComponent)
/*     */   {
/* 153 */     SynthContext localSynthContext = getContext(paramJComponent);
/*     */ 
/* 155 */     SynthLookAndFeel.update(localSynthContext, paramGraphics);
/* 156 */     localSynthContext.getPainter().paintColorChooserBackground(localSynthContext, paramGraphics, 0, 0, paramJComponent.getWidth(), paramJComponent.getHeight());
/*     */ 
/* 158 */     paint(localSynthContext, paramGraphics);
/* 159 */     localSynthContext.dispose();
/*     */   }
/*     */ 
/*     */   public void paint(Graphics paramGraphics, JComponent paramJComponent)
/*     */   {
/* 173 */     SynthContext localSynthContext = getContext(paramJComponent);
/*     */ 
/* 175 */     paint(localSynthContext, paramGraphics);
/* 176 */     localSynthContext.dispose();
/*     */   }
/*     */ 
/*     */   protected void paint(SynthContext paramSynthContext, Graphics paramGraphics)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void paintBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/* 196 */     paramSynthContext.getPainter().paintColorChooserBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*     */   }
/*     */ 
/*     */   public void propertyChange(PropertyChangeEvent paramPropertyChangeEvent)
/*     */   {
/* 204 */     if (SynthLookAndFeel.shouldUpdateStyle(paramPropertyChangeEvent))
/* 205 */       updateStyle((JColorChooser)paramPropertyChangeEvent.getSource());
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.synth.SynthColorChooserUI
 * JD-Core Version:    0.6.2
 */