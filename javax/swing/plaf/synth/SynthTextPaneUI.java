/*     */ package javax.swing.plaf.synth;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Font;
/*     */ import java.awt.Graphics;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.text.JTextComponent;
/*     */ import javax.swing.text.Style;
/*     */ import javax.swing.text.StyleConstants;
/*     */ import javax.swing.text.StyledDocument;
/*     */ 
/*     */ public class SynthTextPaneUI extends SynthEditorPaneUI
/*     */ {
/*     */   public static ComponentUI createUI(JComponent paramJComponent)
/*     */   {
/*  59 */     return new SynthTextPaneUI();
/*     */   }
/*     */ 
/*     */   protected String getPropertyPrefix()
/*     */   {
/*  71 */     return "TextPane";
/*     */   }
/*     */ 
/*     */   public void installUI(JComponent paramJComponent)
/*     */   {
/* 100 */     super.installUI(paramJComponent);
/* 101 */     updateForeground(paramJComponent.getForeground());
/* 102 */     updateFont(paramJComponent.getFont());
/*     */   }
/*     */ 
/*     */   protected void propertyChange(PropertyChangeEvent paramPropertyChangeEvent)
/*     */   {
/* 118 */     super.propertyChange(paramPropertyChangeEvent);
/*     */ 
/* 120 */     String str = paramPropertyChangeEvent.getPropertyName();
/*     */ 
/* 122 */     if (str.equals("foreground")) {
/* 123 */       updateForeground((Color)paramPropertyChangeEvent.getNewValue());
/* 124 */     } else if (str.equals("font")) {
/* 125 */       updateFont((Font)paramPropertyChangeEvent.getNewValue());
/* 126 */     } else if (str.equals("document")) {
/* 127 */       JTextComponent localJTextComponent = getComponent();
/* 128 */       updateForeground(localJTextComponent.getForeground());
/* 129 */       updateFont(localJTextComponent.getFont());
/*     */     }
/*     */   }
/*     */ 
/*     */   private void updateForeground(Color paramColor)
/*     */   {
/* 140 */     StyledDocument localStyledDocument = (StyledDocument)getComponent().getDocument();
/* 141 */     Style localStyle = localStyledDocument.getStyle("default");
/*     */ 
/* 143 */     if (localStyle == null) {
/* 144 */       return;
/*     */     }
/*     */ 
/* 147 */     if (paramColor == null)
/* 148 */       localStyle.removeAttribute(StyleConstants.Foreground);
/*     */     else
/* 150 */       StyleConstants.setForeground(localStyle, paramColor);
/*     */   }
/*     */ 
/*     */   private void updateFont(Font paramFont)
/*     */   {
/* 161 */     StyledDocument localStyledDocument = (StyledDocument)getComponent().getDocument();
/* 162 */     Style localStyle = localStyledDocument.getStyle("default");
/*     */ 
/* 164 */     if (localStyle == null) {
/* 165 */       return;
/*     */     }
/*     */ 
/* 168 */     if (paramFont == null) {
/* 169 */       localStyle.removeAttribute(StyleConstants.FontFamily);
/* 170 */       localStyle.removeAttribute(StyleConstants.FontSize);
/* 171 */       localStyle.removeAttribute(StyleConstants.Bold);
/* 172 */       localStyle.removeAttribute(StyleConstants.Italic);
/*     */     } else {
/* 174 */       StyleConstants.setFontFamily(localStyle, paramFont.getName());
/* 175 */       StyleConstants.setFontSize(localStyle, paramFont.getSize());
/* 176 */       StyleConstants.setBold(localStyle, paramFont.isBold());
/* 177 */       StyleConstants.setItalic(localStyle, paramFont.isItalic());
/*     */     }
/*     */   }
/*     */ 
/*     */   void paintBackground(SynthContext paramSynthContext, Graphics paramGraphics, JComponent paramJComponent)
/*     */   {
/* 183 */     paramSynthContext.getPainter().paintTextPaneBackground(paramSynthContext, paramGraphics, 0, 0, paramJComponent.getWidth(), paramJComponent.getHeight());
/*     */   }
/*     */ 
/*     */   public void paintBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/* 193 */     paramSynthContext.getPainter().paintTextPaneBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.synth.SynthTextPaneUI
 * JD-Core Version:    0.6.2
 */