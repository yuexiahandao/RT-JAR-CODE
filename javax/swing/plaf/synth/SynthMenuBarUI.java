/*     */ package javax.swing.plaf.synth;
/*     */ 
/*     */ import java.awt.Graphics;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JMenuBar;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.UIResource;
/*     */ import javax.swing.plaf.basic.BasicMenuBarUI;
/*     */ 
/*     */ public class SynthMenuBarUI extends BasicMenuBarUI
/*     */   implements PropertyChangeListener, SynthUI
/*     */ {
/*     */   private SynthStyle style;
/*     */ 
/*     */   public static ComponentUI createUI(JComponent paramJComponent)
/*     */   {
/*  52 */     return new SynthMenuBarUI();
/*     */   }
/*     */ 
/*     */   protected void installDefaults()
/*     */   {
/*  60 */     if ((this.menuBar.getLayout() == null) || ((this.menuBar.getLayout() instanceof UIResource)))
/*     */     {
/*  62 */       this.menuBar.setLayout(new SynthMenuLayout(this.menuBar, 2));
/*     */     }
/*  64 */     updateStyle(this.menuBar);
/*     */   }
/*     */ 
/*     */   protected void installListeners()
/*     */   {
/*  72 */     super.installListeners();
/*  73 */     this.menuBar.addPropertyChangeListener(this);
/*     */   }
/*     */ 
/*     */   private void updateStyle(JMenuBar paramJMenuBar) {
/*  77 */     SynthContext localSynthContext = getContext(paramJMenuBar, 1);
/*  78 */     SynthStyle localSynthStyle = this.style;
/*  79 */     this.style = SynthLookAndFeel.updateStyle(localSynthContext, this);
/*  80 */     if ((this.style != localSynthStyle) && 
/*  81 */       (localSynthStyle != null)) {
/*  82 */       uninstallKeyboardActions();
/*  83 */       installKeyboardActions();
/*     */     }
/*     */ 
/*  86 */     localSynthContext.dispose();
/*     */   }
/*     */ 
/*     */   protected void uninstallDefaults()
/*     */   {
/*  94 */     SynthContext localSynthContext = getContext(this.menuBar, 1);
/*     */ 
/*  96 */     this.style.uninstallDefaults(localSynthContext);
/*  97 */     localSynthContext.dispose();
/*  98 */     this.style = null;
/*     */   }
/*     */ 
/*     */   protected void uninstallListeners()
/*     */   {
/* 106 */     super.uninstallListeners();
/* 107 */     this.menuBar.removePropertyChangeListener(this);
/*     */   }
/*     */ 
/*     */   public SynthContext getContext(JComponent paramJComponent)
/*     */   {
/* 115 */     return getContext(paramJComponent, getComponentState(paramJComponent));
/*     */   }
/*     */ 
/*     */   private SynthContext getContext(JComponent paramJComponent, int paramInt) {
/* 119 */     return SynthContext.getContext(SynthContext.class, paramJComponent, SynthLookAndFeel.getRegion(paramJComponent), this.style, paramInt);
/*     */   }
/*     */ 
/*     */   private int getComponentState(JComponent paramJComponent)
/*     */   {
/* 124 */     return SynthLookAndFeel.getComponentState(paramJComponent);
/*     */   }
/*     */ 
/*     */   public void update(Graphics paramGraphics, JComponent paramJComponent)
/*     */   {
/* 141 */     SynthContext localSynthContext = getContext(paramJComponent);
/*     */ 
/* 143 */     SynthLookAndFeel.update(localSynthContext, paramGraphics);
/* 144 */     localSynthContext.getPainter().paintMenuBarBackground(localSynthContext, paramGraphics, 0, 0, paramJComponent.getWidth(), paramJComponent.getHeight());
/*     */ 
/* 146 */     paint(localSynthContext, paramGraphics);
/* 147 */     localSynthContext.dispose();
/*     */   }
/*     */ 
/*     */   public void paint(Graphics paramGraphics, JComponent paramJComponent)
/*     */   {
/* 161 */     SynthContext localSynthContext = getContext(paramJComponent);
/*     */ 
/* 163 */     paint(localSynthContext, paramGraphics);
/* 164 */     localSynthContext.dispose();
/*     */   }
/*     */ 
/*     */   protected void paint(SynthContext paramSynthContext, Graphics paramGraphics)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void paintBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/* 183 */     paramSynthContext.getPainter().paintMenuBarBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*     */   }
/*     */ 
/*     */   public void propertyChange(PropertyChangeEvent paramPropertyChangeEvent)
/*     */   {
/* 191 */     if (SynthLookAndFeel.shouldUpdateStyle(paramPropertyChangeEvent))
/* 192 */       updateStyle((JMenuBar)paramPropertyChangeEvent.getSource());
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.synth.SynthMenuBarUI
 * JD-Core Version:    0.6.2
 */