/*     */ package javax.swing.plaf.synth;
/*     */ 
/*     */ import java.awt.Graphics;
/*     */ import java.awt.event.FocusEvent;
/*     */ import java.awt.event.FocusListener;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.basic.BasicTextAreaUI;
/*     */ import javax.swing.text.JTextComponent;
/*     */ 
/*     */ public class SynthTextAreaUI extends BasicTextAreaUI
/*     */   implements SynthUI
/*     */ {
/*     */   private Handler handler;
/*     */   private SynthStyle style;
/*     */ 
/*     */   public SynthTextAreaUI()
/*     */   {
/*  55 */     this.handler = new Handler(null);
/*     */   }
/*     */ 
/*     */   public static ComponentUI createUI(JComponent paramJComponent)
/*     */   {
/*  65 */     return new SynthTextAreaUI();
/*     */   }
/*     */ 
/*     */   protected void installDefaults()
/*     */   {
/*  74 */     super.installDefaults();
/*  75 */     updateStyle(getComponent());
/*  76 */     getComponent().addFocusListener(this.handler);
/*     */   }
/*     */ 
/*     */   protected void uninstallDefaults()
/*     */   {
/*  84 */     SynthContext localSynthContext = getContext(getComponent(), 1);
/*     */ 
/*  86 */     getComponent().putClientProperty("caretAspectRatio", null);
/*  87 */     getComponent().removeFocusListener(this.handler);
/*     */ 
/*  89 */     this.style.uninstallDefaults(localSynthContext);
/*  90 */     localSynthContext.dispose();
/*  91 */     this.style = null;
/*  92 */     super.uninstallDefaults();
/*     */   }
/*     */ 
/*     */   private void updateStyle(JTextComponent paramJTextComponent) {
/*  96 */     SynthContext localSynthContext = getContext(paramJTextComponent, 1);
/*  97 */     SynthStyle localSynthStyle = this.style;
/*     */ 
/*  99 */     this.style = SynthLookAndFeel.updateStyle(localSynthContext, this);
/*     */ 
/* 101 */     if (this.style != localSynthStyle) {
/* 102 */       SynthTextFieldUI.updateStyle(paramJTextComponent, localSynthContext, getPropertyPrefix());
/*     */ 
/* 104 */       if (localSynthStyle != null) {
/* 105 */         uninstallKeyboardActions();
/* 106 */         installKeyboardActions();
/*     */       }
/*     */     }
/* 109 */     localSynthContext.dispose();
/*     */   }
/*     */ 
/*     */   public SynthContext getContext(JComponent paramJComponent)
/*     */   {
/* 117 */     return getContext(paramJComponent, SynthLookAndFeel.getComponentState(paramJComponent));
/*     */   }
/*     */ 
/*     */   private SynthContext getContext(JComponent paramJComponent, int paramInt) {
/* 121 */     return SynthContext.getContext(SynthContext.class, paramJComponent, SynthLookAndFeel.getRegion(paramJComponent), this.style, paramInt);
/*     */   }
/*     */ 
/*     */   public void update(Graphics paramGraphics, JComponent paramJComponent)
/*     */   {
/* 139 */     SynthContext localSynthContext = getContext(paramJComponent);
/*     */ 
/* 141 */     SynthLookAndFeel.update(localSynthContext, paramGraphics);
/* 142 */     localSynthContext.getPainter().paintTextAreaBackground(localSynthContext, paramGraphics, 0, 0, paramJComponent.getWidth(), paramJComponent.getHeight());
/*     */ 
/* 144 */     paint(localSynthContext, paramGraphics);
/* 145 */     localSynthContext.dispose();
/*     */   }
/*     */ 
/*     */   protected void paint(SynthContext paramSynthContext, Graphics paramGraphics)
/*     */   {
/* 156 */     super.paint(paramGraphics, getComponent());
/*     */   }
/*     */ 
/*     */   protected void paintBackground(Graphics paramGraphics)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void paintBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/* 175 */     paramSynthContext.getPainter().paintTextAreaBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*     */   }
/*     */ 
/*     */   protected void propertyChange(PropertyChangeEvent paramPropertyChangeEvent)
/*     */   {
/* 190 */     if (SynthLookAndFeel.shouldUpdateStyle(paramPropertyChangeEvent)) {
/* 191 */       updateStyle((JTextComponent)paramPropertyChangeEvent.getSource());
/*     */     }
/* 193 */     super.propertyChange(paramPropertyChangeEvent);
/*     */   }
/*     */   private final class Handler implements FocusListener {
/*     */     private Handler() {
/*     */     }
/* 198 */     public void focusGained(FocusEvent paramFocusEvent) { SynthTextAreaUI.this.getComponent().repaint(); }
/*     */ 
/*     */     public void focusLost(FocusEvent paramFocusEvent)
/*     */     {
/* 202 */       SynthTextAreaUI.this.getComponent().repaint();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.synth.SynthTextAreaUI
 * JD-Core Version:    0.6.2
 */