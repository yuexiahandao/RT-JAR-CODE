/*     */ package javax.swing.plaf.synth;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Insets;
/*     */ import java.awt.event.FocusEvent;
/*     */ import java.awt.event.FocusListener;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.UIResource;
/*     */ import javax.swing.plaf.basic.BasicTextFieldUI;
/*     */ import javax.swing.text.Caret;
/*     */ import javax.swing.text.JTextComponent;
/*     */ 
/*     */ public class SynthTextFieldUI extends BasicTextFieldUI
/*     */   implements SynthUI
/*     */ {
/*     */   private Handler handler;
/*     */   private SynthStyle style;
/*     */ 
/*     */   public SynthTextFieldUI()
/*     */   {
/*  54 */     this.handler = new Handler(null);
/*     */   }
/*     */ 
/*     */   public static ComponentUI createUI(JComponent paramJComponent)
/*     */   {
/*  64 */     return new SynthTextFieldUI();
/*     */   }
/*     */ 
/*     */   private void updateStyle(JTextComponent paramJTextComponent) {
/*  68 */     SynthContext localSynthContext = getContext(paramJTextComponent, 1);
/*  69 */     SynthStyle localSynthStyle = this.style;
/*     */ 
/*  71 */     this.style = SynthLookAndFeel.updateStyle(localSynthContext, this);
/*     */ 
/*  73 */     if (this.style != localSynthStyle) {
/*  74 */       updateStyle(paramJTextComponent, localSynthContext, getPropertyPrefix());
/*     */ 
/*  76 */       if (localSynthStyle != null) {
/*  77 */         uninstallKeyboardActions();
/*  78 */         installKeyboardActions();
/*     */       }
/*     */     }
/*  81 */     localSynthContext.dispose();
/*     */   }
/*     */ 
/*     */   static void updateStyle(JTextComponent paramJTextComponent, SynthContext paramSynthContext, String paramString)
/*     */   {
/*  86 */     SynthStyle localSynthStyle = paramSynthContext.getStyle();
/*     */ 
/*  88 */     Color localColor1 = paramJTextComponent.getCaretColor();
/*  89 */     if ((localColor1 == null) || ((localColor1 instanceof UIResource))) {
/*  90 */       paramJTextComponent.setCaretColor((Color)localSynthStyle.get(paramSynthContext, paramString + ".caretForeground"));
/*     */     }
/*     */ 
/*  94 */     Color localColor2 = paramJTextComponent.getForeground();
/*  95 */     if ((localColor2 == null) || ((localColor2 instanceof UIResource))) {
/*  96 */       localColor2 = localSynthStyle.getColorForState(paramSynthContext, ColorType.TEXT_FOREGROUND);
/*  97 */       if (localColor2 != null) {
/*  98 */         paramJTextComponent.setForeground(localColor2);
/*     */       }
/*     */     }
/*     */ 
/* 102 */     Object localObject1 = localSynthStyle.get(paramSynthContext, paramString + ".caretAspectRatio");
/* 103 */     if ((localObject1 instanceof Number)) {
/* 104 */       paramJTextComponent.putClientProperty("caretAspectRatio", localObject1);
/*     */     }
/*     */ 
/* 107 */     paramSynthContext.setComponentState(768);
/*     */ 
/* 109 */     Color localColor3 = paramJTextComponent.getSelectionColor();
/* 110 */     if ((localColor3 == null) || ((localColor3 instanceof UIResource))) {
/* 111 */       paramJTextComponent.setSelectionColor(localSynthStyle.getColor(paramSynthContext, ColorType.TEXT_BACKGROUND));
/*     */     }
/*     */ 
/* 115 */     Color localColor4 = paramJTextComponent.getSelectedTextColor();
/* 116 */     if ((localColor4 == null) || ((localColor4 instanceof UIResource))) {
/* 117 */       paramJTextComponent.setSelectedTextColor(localSynthStyle.getColor(paramSynthContext, ColorType.TEXT_FOREGROUND));
/*     */     }
/*     */ 
/* 121 */     paramSynthContext.setComponentState(8);
/*     */ 
/* 123 */     Color localColor5 = paramJTextComponent.getDisabledTextColor();
/* 124 */     if ((localColor5 == null) || ((localColor5 instanceof UIResource))) {
/* 125 */       paramJTextComponent.setDisabledTextColor(localSynthStyle.getColor(paramSynthContext, ColorType.TEXT_FOREGROUND));
/*     */     }
/*     */ 
/* 129 */     Insets localInsets = paramJTextComponent.getMargin();
/* 130 */     if ((localInsets == null) || ((localInsets instanceof UIResource))) {
/* 131 */       localInsets = (Insets)localSynthStyle.get(paramSynthContext, paramString + ".margin");
/*     */ 
/* 133 */       if (localInsets == null)
/*     */       {
/* 135 */         localInsets = SynthLookAndFeel.EMPTY_UIRESOURCE_INSETS;
/*     */       }
/* 137 */       paramJTextComponent.setMargin(localInsets);
/*     */     }
/*     */ 
/* 140 */     Caret localCaret = paramJTextComponent.getCaret();
/* 141 */     if ((localCaret instanceof UIResource)) {
/* 142 */       Object localObject2 = localSynthStyle.get(paramSynthContext, paramString + ".caretBlinkRate");
/* 143 */       if ((localObject2 != null) && ((localObject2 instanceof Integer))) {
/* 144 */         Integer localInteger = (Integer)localObject2;
/* 145 */         localCaret.setBlinkRate(localInteger.intValue());
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public SynthContext getContext(JComponent paramJComponent)
/*     */   {
/* 155 */     return getContext(paramJComponent, SynthLookAndFeel.getComponentState(paramJComponent));
/*     */   }
/*     */ 
/*     */   private SynthContext getContext(JComponent paramJComponent, int paramInt) {
/* 159 */     return SynthContext.getContext(SynthContext.class, paramJComponent, SynthLookAndFeel.getRegion(paramJComponent), this.style, paramInt);
/*     */   }
/*     */ 
/*     */   public void update(Graphics paramGraphics, JComponent paramJComponent)
/*     */   {
/* 177 */     SynthContext localSynthContext = getContext(paramJComponent);
/*     */ 
/* 179 */     SynthLookAndFeel.update(localSynthContext, paramGraphics);
/* 180 */     paintBackground(localSynthContext, paramGraphics, paramJComponent);
/* 181 */     paint(localSynthContext, paramGraphics);
/* 182 */     localSynthContext.dispose();
/*     */   }
/*     */ 
/*     */   protected void paint(SynthContext paramSynthContext, Graphics paramGraphics)
/*     */   {
/* 198 */     super.paint(paramGraphics, getComponent());
/*     */   }
/*     */ 
/*     */   void paintBackground(SynthContext paramSynthContext, Graphics paramGraphics, JComponent paramJComponent) {
/* 202 */     paramSynthContext.getPainter().paintTextFieldBackground(paramSynthContext, paramGraphics, 0, 0, paramJComponent.getWidth(), paramJComponent.getHeight());
/*     */   }
/*     */ 
/*     */   public void paintBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/* 212 */     paramSynthContext.getPainter().paintTextFieldBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*     */   }
/*     */ 
/*     */   protected void paintBackground(Graphics paramGraphics)
/*     */   {
/*     */   }
/*     */ 
/*     */   protected void propertyChange(PropertyChangeEvent paramPropertyChangeEvent)
/*     */   {
/* 237 */     if (SynthLookAndFeel.shouldUpdateStyle(paramPropertyChangeEvent)) {
/* 238 */       updateStyle((JTextComponent)paramPropertyChangeEvent.getSource());
/*     */     }
/* 240 */     super.propertyChange(paramPropertyChangeEvent);
/*     */   }
/*     */ 
/*     */   protected void installDefaults()
/*     */   {
/* 249 */     super.installDefaults();
/* 250 */     updateStyle(getComponent());
/* 251 */     getComponent().addFocusListener(this.handler);
/*     */   }
/*     */ 
/*     */   protected void uninstallDefaults()
/*     */   {
/* 259 */     SynthContext localSynthContext = getContext(getComponent(), 1);
/*     */ 
/* 261 */     getComponent().putClientProperty("caretAspectRatio", null);
/* 262 */     getComponent().removeFocusListener(this.handler);
/*     */ 
/* 264 */     this.style.uninstallDefaults(localSynthContext);
/* 265 */     localSynthContext.dispose();
/* 266 */     this.style = null;
/* 267 */     super.uninstallDefaults();
/*     */   }
/*     */   private final class Handler implements FocusListener {
/*     */     private Handler() {
/*     */     }
/* 272 */     public void focusGained(FocusEvent paramFocusEvent) { SynthTextFieldUI.this.getComponent().repaint(); }
/*     */ 
/*     */     public void focusLost(FocusEvent paramFocusEvent)
/*     */     {
/* 276 */       SynthTextFieldUI.this.getComponent().repaint();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.synth.SynthTextFieldUI
 * JD-Core Version:    0.6.2
 */