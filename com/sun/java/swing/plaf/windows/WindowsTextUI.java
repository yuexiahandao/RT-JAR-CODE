/*     */ package com.sun.java.swing.plaf.windows;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.Shape;
/*     */ import javax.swing.plaf.TextUI;
/*     */ import javax.swing.plaf.UIResource;
/*     */ import javax.swing.plaf.basic.BasicTextUI;
/*     */ import javax.swing.text.BadLocationException;
/*     */ import javax.swing.text.Caret;
/*     */ import javax.swing.text.DefaultCaret;
/*     */ import javax.swing.text.DefaultHighlighter.DefaultHighlightPainter;
/*     */ import javax.swing.text.Highlighter.HighlightPainter;
/*     */ import javax.swing.text.JTextComponent;
/*     */ import javax.swing.text.LayeredHighlighter.LayerPainter;
/*     */ import javax.swing.text.Position.Bias;
/*     */ import javax.swing.text.View;
/*     */ 
/*     */ public abstract class WindowsTextUI extends BasicTextUI
/*     */ {
/*  62 */   static LayeredHighlighter.LayerPainter WindowsPainter = new WindowsHighlightPainter(null);
/*     */ 
/*     */   protected Caret createCaret()
/*     */   {
/*  58 */     return new WindowsCaret();
/*     */   }
/*     */ 
/*     */   static class WindowsCaret extends DefaultCaret
/*     */     implements UIResource
/*     */   {
/*     */     protected Highlighter.HighlightPainter getSelectionPainter()
/*     */     {
/*  73 */       return WindowsTextUI.WindowsPainter;
/*     */     }
/*     */   }
/*     */ 
/*     */   static class WindowsHighlightPainter extends DefaultHighlighter.DefaultHighlightPainter
/*     */   {
/*     */     WindowsHighlightPainter(Color paramColor)
/*     */     {
/*  81 */       super();
/*     */     }
/*     */ 
/*     */     public void paint(Graphics paramGraphics, int paramInt1, int paramInt2, Shape paramShape, JTextComponent paramJTextComponent)
/*     */     {
/*  96 */       Rectangle localRectangle1 = paramShape.getBounds();
/*     */       try
/*     */       {
/*  99 */         TextUI localTextUI = paramJTextComponent.getUI();
/* 100 */         Rectangle localRectangle2 = localTextUI.modelToView(paramJTextComponent, paramInt1);
/* 101 */         Rectangle localRectangle3 = localTextUI.modelToView(paramJTextComponent, paramInt2);
/*     */ 
/* 104 */         Color localColor = getColor();
/*     */ 
/* 106 */         if (localColor == null) {
/* 107 */           paramGraphics.setColor(paramJTextComponent.getSelectionColor());
/*     */         }
/*     */         else {
/* 110 */           paramGraphics.setColor(localColor);
/*     */         }
/* 112 */         int i = 0;
/* 113 */         int j = 0;
/* 114 */         if (paramJTextComponent.isEditable()) {
/* 115 */           int k = paramJTextComponent.getCaretPosition();
/* 116 */           i = paramInt1 == k ? 1 : 0;
/* 117 */           j = paramInt2 == k ? 1 : 0;
/*     */         }
/* 119 */         if (localRectangle2.y == localRectangle3.y)
/*     */         {
/* 121 */           Rectangle localRectangle4 = localRectangle2.union(localRectangle3);
/* 122 */           if (localRectangle4.width > 0) {
/* 123 */             if (i != 0) {
/* 124 */               localRectangle4.x += 1;
/* 125 */               localRectangle4.width -= 1;
/*     */             }
/* 127 */             else if (j != 0) {
/* 128 */               localRectangle4.width -= 1;
/*     */             }
/*     */           }
/* 131 */           paramGraphics.fillRect(localRectangle4.x, localRectangle4.y, localRectangle4.width, localRectangle4.height);
/*     */         }
/*     */         else {
/* 134 */           int m = localRectangle1.x + localRectangle1.width - localRectangle2.x;
/* 135 */           if ((i != 0) && (m > 0)) {
/* 136 */             localRectangle2.x += 1;
/* 137 */             m--;
/*     */           }
/* 139 */           paramGraphics.fillRect(localRectangle2.x, localRectangle2.y, m, localRectangle2.height);
/* 140 */           if (localRectangle2.y + localRectangle2.height != localRectangle3.y) {
/* 141 */             paramGraphics.fillRect(localRectangle1.x, localRectangle2.y + localRectangle2.height, localRectangle1.width, localRectangle3.y - (localRectangle2.y + localRectangle2.height));
/*     */           }
/*     */ 
/* 144 */           if ((j != 0) && (localRectangle3.x > localRectangle1.x)) {
/* 145 */             localRectangle3.x -= 1;
/*     */           }
/* 147 */           paramGraphics.fillRect(localRectangle1.x, localRectangle3.y, localRectangle3.x - localRectangle1.x, localRectangle3.height);
/*     */         }
/*     */       }
/*     */       catch (BadLocationException localBadLocationException)
/*     */       {
/*     */       }
/*     */     }
/*     */ 
/*     */     public Shape paintLayer(Graphics paramGraphics, int paramInt1, int paramInt2, Shape paramShape, JTextComponent paramJTextComponent, View paramView)
/*     */     {
/* 169 */       Color localColor = getColor();
/*     */ 
/* 171 */       if (localColor == null) {
/* 172 */         paramGraphics.setColor(paramJTextComponent.getSelectionColor());
/*     */       }
/*     */       else {
/* 175 */         paramGraphics.setColor(localColor);
/*     */       }
/* 177 */       int i = 0;
/* 178 */       int j = 0;
/* 179 */       if (paramJTextComponent.isEditable()) {
/* 180 */         int k = paramJTextComponent.getCaretPosition();
/* 181 */         i = paramInt1 == k ? 1 : 0;
/* 182 */         j = paramInt2 == k ? 1 : 0;
/*     */       }
/*     */       Object localObject;
/* 184 */       if ((paramInt1 == paramView.getStartOffset()) && (paramInt2 == paramView.getEndOffset()))
/*     */       {
/* 188 */         if ((paramShape instanceof Rectangle)) {
/* 189 */           localObject = (Rectangle)paramShape;
/*     */         }
/*     */         else {
/* 192 */           localObject = paramShape.getBounds();
/*     */         }
/* 194 */         if ((i != 0) && (((Rectangle)localObject).width > 0)) {
/* 195 */           paramGraphics.fillRect(((Rectangle)localObject).x + 1, ((Rectangle)localObject).y, ((Rectangle)localObject).width - 1, ((Rectangle)localObject).height);
/*     */         }
/* 198 */         else if ((j != 0) && (((Rectangle)localObject).width > 0)) {
/* 199 */           paramGraphics.fillRect(((Rectangle)localObject).x, ((Rectangle)localObject).y, ((Rectangle)localObject).width - 1, ((Rectangle)localObject).height);
/*     */         }
/*     */         else
/*     */         {
/* 203 */           paramGraphics.fillRect(((Rectangle)localObject).x, ((Rectangle)localObject).y, ((Rectangle)localObject).width, ((Rectangle)localObject).height);
/*     */         }
/* 205 */         return localObject;
/*     */       }
/*     */ 
/*     */       try
/*     */       {
/* 211 */         localObject = paramView.modelToView(paramInt1, Position.Bias.Forward, paramInt2, Position.Bias.Backward, paramShape);
/*     */ 
/* 214 */         Rectangle localRectangle = (localObject instanceof Rectangle) ? (Rectangle)localObject : ((Shape)localObject).getBounds();
/*     */ 
/* 216 */         if ((i != 0) && (localRectangle.width > 0)) {
/* 217 */           paramGraphics.fillRect(localRectangle.x + 1, localRectangle.y, localRectangle.width - 1, localRectangle.height);
/*     */         }
/* 219 */         else if ((j != 0) && (localRectangle.width > 0)) {
/* 220 */           paramGraphics.fillRect(localRectangle.x, localRectangle.y, localRectangle.width - 1, localRectangle.height);
/*     */         }
/*     */         else {
/* 223 */           paramGraphics.fillRect(localRectangle.x, localRectangle.y, localRectangle.width, localRectangle.height);
/*     */         }
/* 225 */         return localRectangle;
/*     */       }
/*     */       catch (BadLocationException localBadLocationException)
/*     */       {
/*     */       }
/*     */ 
/* 231 */       return null;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.java.swing.plaf.windows.WindowsTextUI
 * JD-Core Version:    0.6.2
 */