/*     */ package com.sun.java.swing.plaf.windows;
/*     */ 
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Insets;
/*     */ import java.awt.Rectangle;
/*     */ import javax.swing.BoundedRangeModel;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JTextField;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.TextUI;
/*     */ import javax.swing.plaf.UIResource;
/*     */ import javax.swing.plaf.basic.BasicTextFieldUI;
/*     */ import javax.swing.text.BadLocationException;
/*     */ import javax.swing.text.Caret;
/*     */ import javax.swing.text.DefaultCaret;
/*     */ import javax.swing.text.Highlighter.HighlightPainter;
/*     */ import javax.swing.text.Position.Bias;
/*     */ 
/*     */ public class WindowsTextFieldUI extends BasicTextFieldUI
/*     */ {
/*     */   public static ComponentUI createUI(JComponent paramJComponent)
/*     */   {
/*  75 */     return new WindowsTextFieldUI();
/*     */   }
/*     */ 
/*     */   protected void paintBackground(Graphics paramGraphics)
/*     */   {
/*  87 */     super.paintBackground(paramGraphics);
/*     */   }
/*     */ 
/*     */   protected Caret createCaret()
/*     */   {
/*  96 */     return new WindowsFieldCaret();
/*     */   }
/*     */ 
/*     */   static class WindowsFieldCaret extends DefaultCaret
/*     */     implements UIResource
/*     */   {
/*     */     protected void adjustVisibility(Rectangle paramRectangle)
/*     */     {
/* 116 */       SwingUtilities.invokeLater(new SafeScroller(paramRectangle));
/*     */     }
/*     */ 
/*     */     protected Highlighter.HighlightPainter getSelectionPainter()
/*     */     {
/* 125 */       return WindowsTextUI.WindowsPainter;
/*     */     }
/*     */ 
/*     */     private class SafeScroller
/*     */       implements Runnable
/*     */     {
/*     */       private Rectangle r;
/*     */ 
/*     */       SafeScroller(Rectangle arg2)
/*     */       {
/*     */         Object localObject;
/* 131 */         this.r = localObject;
/*     */       }
/*     */ 
/*     */       public void run() {
/* 135 */         JTextField localJTextField = (JTextField)WindowsTextFieldUI.WindowsFieldCaret.this.getComponent();
/* 136 */         if (localJTextField != null) {
/* 137 */           TextUI localTextUI = localJTextField.getUI();
/* 138 */           int i = WindowsTextFieldUI.WindowsFieldCaret.this.getDot();
/*     */ 
/* 140 */           Position.Bias localBias = Position.Bias.Forward;
/* 141 */           Rectangle localRectangle1 = null;
/*     */           try {
/* 143 */             localRectangle1 = localTextUI.modelToView(localJTextField, i, localBias);
/*     */           } catch (BadLocationException localBadLocationException1) {
/*     */           }
/* 146 */           Insets localInsets = localJTextField.getInsets();
/* 147 */           BoundedRangeModel localBoundedRangeModel = localJTextField.getHorizontalVisibility();
/* 148 */           int j = this.r.x + localBoundedRangeModel.getValue() - localInsets.left;
/* 149 */           int k = localBoundedRangeModel.getExtent() / 4;
/* 150 */           if (this.r.x < localInsets.left)
/* 151 */             localBoundedRangeModel.setValue(j - k);
/* 152 */           else if (this.r.x + this.r.width > localInsets.left + localBoundedRangeModel.getExtent()) {
/* 153 */             localBoundedRangeModel.setValue(j - 3 * k);
/*     */           }
/*     */ 
/* 159 */           if (localRectangle1 != null)
/*     */             try
/*     */             {
/* 162 */               Rectangle localRectangle2 = localTextUI.modelToView(localJTextField, i, localBias);
/* 163 */               if ((localRectangle2 != null) && (!localRectangle2.equals(localRectangle1)))
/* 164 */                 WindowsTextFieldUI.WindowsFieldCaret.this.damage(localRectangle2);
/*     */             }
/*     */             catch (BadLocationException localBadLocationException2)
/*     */             {
/*     */             }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.java.swing.plaf.windows.WindowsTextFieldUI
 * JD-Core Version:    0.6.2
 */