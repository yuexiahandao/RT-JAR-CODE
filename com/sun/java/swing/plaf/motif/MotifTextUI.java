/*     */ package com.sun.java.swing.plaf.motif;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.event.FocusEvent;
/*     */ import javax.swing.KeyStroke;
/*     */ import javax.swing.plaf.TextUI;
/*     */ import javax.swing.plaf.UIResource;
/*     */ import javax.swing.text.BadLocationException;
/*     */ import javax.swing.text.Caret;
/*     */ import javax.swing.text.DefaultCaret;
/*     */ import javax.swing.text.JTextComponent;
/*     */ import javax.swing.text.JTextComponent.KeyBinding;
/*     */ 
/*     */ public class MotifTextUI
/*     */ {
/* 156 */   static final JTextComponent.KeyBinding[] defaultBindings = { new JTextComponent.KeyBinding(KeyStroke.getKeyStroke(155, 2), "copy-to-clipboard"), new JTextComponent.KeyBinding(KeyStroke.getKeyStroke(155, 1), "paste-from-clipboard"), new JTextComponent.KeyBinding(KeyStroke.getKeyStroke(127, 1), "cut-to-clipboard"), new JTextComponent.KeyBinding(KeyStroke.getKeyStroke(37, 1), "selection-backward"), new JTextComponent.KeyBinding(KeyStroke.getKeyStroke(39, 1), "selection-forward") };
/*     */ 
/*     */   public static Caret createCaret()
/*     */   {
/*  56 */     return new MotifCaret();
/*     */   }
/*     */ 
/*     */   public static class MotifCaret extends DefaultCaret
/*     */     implements UIResource
/*     */   {
/*     */     static final int IBeamOverhang = 2;
/*     */ 
/*     */     public void focusGained(FocusEvent paramFocusEvent)
/*     */     {
/*  81 */       super.focusGained(paramFocusEvent);
/*  82 */       getComponent().repaint();
/*     */     }
/*     */ 
/*     */     public void focusLost(FocusEvent paramFocusEvent)
/*     */     {
/*  94 */       super.focusLost(paramFocusEvent);
/*  95 */       getComponent().repaint();
/*     */     }
/*     */ 
/*     */     protected void damage(Rectangle paramRectangle)
/*     */     {
/* 107 */       if (paramRectangle != null) {
/* 108 */         this.x = (paramRectangle.x - 2 - 1);
/* 109 */         this.y = paramRectangle.y;
/* 110 */         this.width = (paramRectangle.width + 4 + 3);
/* 111 */         this.height = paramRectangle.height;
/* 112 */         repaint();
/*     */       }
/*     */     }
/*     */ 
/*     */     public void paint(Graphics paramGraphics)
/*     */     {
/* 127 */       if (isVisible())
/*     */         try {
/* 129 */           JTextComponent localJTextComponent = getComponent();
/* 130 */           Color localColor = localJTextComponent.hasFocus() ? localJTextComponent.getCaretColor() : localJTextComponent.getDisabledTextColor();
/*     */ 
/* 132 */           TextUI localTextUI = localJTextComponent.getUI();
/* 133 */           int i = getDot();
/* 134 */           Rectangle localRectangle = localTextUI.modelToView(localJTextComponent, i);
/* 135 */           int j = localRectangle.x - 2;
/* 136 */           int k = localRectangle.x + 2;
/* 137 */           int m = localRectangle.y + 1;
/* 138 */           int n = localRectangle.y + localRectangle.height - 2;
/* 139 */           paramGraphics.setColor(localColor);
/* 140 */           paramGraphics.drawLine(localRectangle.x, m, localRectangle.x, n);
/* 141 */           paramGraphics.drawLine(j, m, k, m);
/* 142 */           paramGraphics.drawLine(j, n, k, n);
/*     */         }
/*     */         catch (BadLocationException localBadLocationException)
/*     */         {
/*     */         }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.java.swing.plaf.motif.MotifTextUI
 * JD-Core Version:    0.6.2
 */