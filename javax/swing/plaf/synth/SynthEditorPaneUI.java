/*     */ package javax.swing.plaf.synth;
/*     */ 
/*     */ import java.awt.Graphics;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.basic.BasicEditorPaneUI;
/*     */ import javax.swing.text.JTextComponent;
/*     */ 
/*     */ public class SynthEditorPaneUI extends BasicEditorPaneUI
/*     */   implements SynthUI
/*     */ {
/*     */   private SynthStyle style;
/*  48 */   private Boolean localTrue = Boolean.TRUE;
/*     */ 
/*     */   public static ComponentUI createUI(JComponent paramJComponent)
/*     */   {
/*  57 */     return new SynthEditorPaneUI();
/*     */   }
/*     */ 
/*     */   protected void installDefaults()
/*     */   {
/*  66 */     super.installDefaults();
/*  67 */     JTextComponent localJTextComponent = getComponent();
/*  68 */     Object localObject = localJTextComponent.getClientProperty("JEditorPane.honorDisplayProperties");
/*     */ 
/*  70 */     if (localObject == null) {
/*  71 */       localJTextComponent.putClientProperty("JEditorPane.honorDisplayProperties", this.localTrue);
/*     */     }
/*  73 */     updateStyle(getComponent());
/*     */   }
/*     */ 
/*     */   protected void uninstallDefaults()
/*     */   {
/*  81 */     SynthContext localSynthContext = getContext(getComponent(), 1);
/*  82 */     JTextComponent localJTextComponent = getComponent();
/*  83 */     localJTextComponent.putClientProperty("caretAspectRatio", null);
/*     */ 
/*  85 */     this.style.uninstallDefaults(localSynthContext);
/*  86 */     localSynthContext.dispose();
/*  87 */     this.style = null;
/*     */ 
/*  89 */     Object localObject = localJTextComponent.getClientProperty("JEditorPane.honorDisplayProperties");
/*     */ 
/*  91 */     if (localObject == this.localTrue) {
/*  92 */       localJTextComponent.putClientProperty("JEditorPane.honorDisplayProperties", Boolean.FALSE);
/*     */     }
/*     */ 
/*  95 */     super.uninstallDefaults();
/*     */   }
/*     */ 
/*     */   protected void propertyChange(PropertyChangeEvent paramPropertyChangeEvent)
/*     */   {
/* 110 */     if (SynthLookAndFeel.shouldUpdateStyle(paramPropertyChangeEvent)) {
/* 111 */       updateStyle((JTextComponent)paramPropertyChangeEvent.getSource());
/*     */     }
/* 113 */     super.propertyChange(paramPropertyChangeEvent);
/*     */   }
/*     */ 
/*     */   private void updateStyle(JTextComponent paramJTextComponent) {
/* 117 */     SynthContext localSynthContext = getContext(paramJTextComponent, 1);
/* 118 */     SynthStyle localSynthStyle = this.style;
/*     */ 
/* 120 */     this.style = SynthLookAndFeel.updateStyle(localSynthContext, this);
/*     */ 
/* 122 */     if (this.style != localSynthStyle) {
/* 123 */       SynthTextFieldUI.updateStyle(paramJTextComponent, localSynthContext, getPropertyPrefix());
/*     */ 
/* 125 */       if (localSynthStyle != null) {
/* 126 */         uninstallKeyboardActions();
/* 127 */         installKeyboardActions();
/*     */       }
/*     */     }
/* 130 */     localSynthContext.dispose();
/*     */   }
/*     */ 
/*     */   public SynthContext getContext(JComponent paramJComponent)
/*     */   {
/* 138 */     return getContext(paramJComponent, getComponentState(paramJComponent));
/*     */   }
/*     */ 
/*     */   private SynthContext getContext(JComponent paramJComponent, int paramInt) {
/* 142 */     return SynthContext.getContext(SynthContext.class, paramJComponent, SynthLookAndFeel.getRegion(paramJComponent), this.style, paramInt);
/*     */   }
/*     */ 
/*     */   private int getComponentState(JComponent paramJComponent)
/*     */   {
/* 147 */     return SynthLookAndFeel.getComponentState(paramJComponent);
/*     */   }
/*     */ 
/*     */   public void update(Graphics paramGraphics, JComponent paramJComponent)
/*     */   {
/* 164 */     SynthContext localSynthContext = getContext(paramJComponent);
/*     */ 
/* 166 */     SynthLookAndFeel.update(localSynthContext, paramGraphics);
/* 167 */     paintBackground(localSynthContext, paramGraphics, paramJComponent);
/* 168 */     paint(localSynthContext, paramGraphics);
/* 169 */     localSynthContext.dispose();
/*     */   }
/*     */ 
/*     */   protected void paint(SynthContext paramSynthContext, Graphics paramGraphics)
/*     */   {
/* 180 */     super.paint(paramGraphics, getComponent());
/*     */   }
/*     */ 
/*     */   protected void paintBackground(Graphics paramGraphics)
/*     */   {
/*     */   }
/*     */ 
/*     */   void paintBackground(SynthContext paramSynthContext, Graphics paramGraphics, JComponent paramJComponent)
/*     */   {
/* 192 */     paramSynthContext.getPainter().paintEditorPaneBackground(paramSynthContext, paramGraphics, 0, 0, paramJComponent.getWidth(), paramJComponent.getHeight());
/*     */   }
/*     */ 
/*     */   public void paintBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/* 202 */     paramSynthContext.getPainter().paintEditorPaneBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.synth.SynthEditorPaneUI
 * JD-Core Version:    0.6.2
 */