/*     */ package javax.swing.text;
/*     */ 
/*     */ import java.awt.Container;
/*     */ import java.awt.FontMetrics;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.Shape;
/*     */ import javax.swing.JPasswordField;
/*     */ import sun.swing.SwingUtilities2;
/*     */ 
/*     */ public class PasswordView extends FieldView
/*     */ {
/* 235 */   static char[] ONE = new char[1];
/*     */ 
/*     */   public PasswordView(Element paramElement)
/*     */   {
/*  49 */     super(paramElement);
/*     */   }
/*     */ 
/*     */   protected int drawUnselectedText(Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */     throws BadLocationException
/*     */   {
/*  68 */     Container localContainer = getContainer();
/*  69 */     if ((localContainer instanceof JPasswordField)) {
/*  70 */       JPasswordField localJPasswordField = (JPasswordField)localContainer;
/*  71 */       if (!localJPasswordField.echoCharIsSet()) {
/*  72 */         return super.drawUnselectedText(paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*     */       }
/*  74 */       if (localJPasswordField.isEnabled()) {
/*  75 */         paramGraphics.setColor(localJPasswordField.getForeground());
/*     */       }
/*     */       else {
/*  78 */         paramGraphics.setColor(localJPasswordField.getDisabledTextColor());
/*     */       }
/*  80 */       char c = localJPasswordField.getEchoChar();
/*  81 */       int i = paramInt4 - paramInt3;
/*  82 */       for (int j = 0; j < i; j++) {
/*  83 */         paramInt1 = drawEchoCharacter(paramGraphics, paramInt1, paramInt2, c);
/*     */       }
/*     */     }
/*  86 */     return paramInt1;
/*     */   }
/*     */ 
/*     */   protected int drawSelectedText(Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */     throws BadLocationException
/*     */   {
/* 106 */     paramGraphics.setColor(this.selected);
/* 107 */     Container localContainer = getContainer();
/* 108 */     if ((localContainer instanceof JPasswordField)) {
/* 109 */       JPasswordField localJPasswordField = (JPasswordField)localContainer;
/* 110 */       if (!localJPasswordField.echoCharIsSet()) {
/* 111 */         return super.drawSelectedText(paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*     */       }
/* 113 */       char c = localJPasswordField.getEchoChar();
/* 114 */       int i = paramInt4 - paramInt3;
/* 115 */       for (int j = 0; j < i; j++) {
/* 116 */         paramInt1 = drawEchoCharacter(paramGraphics, paramInt1, paramInt2, c);
/*     */       }
/*     */     }
/* 119 */     return paramInt1;
/*     */   }
/*     */ 
/*     */   protected int drawEchoCharacter(Graphics paramGraphics, int paramInt1, int paramInt2, char paramChar)
/*     */   {
/* 135 */     ONE[0] = paramChar;
/* 136 */     SwingUtilities2.drawChars(Utilities.getJComponent(this), paramGraphics, ONE, 0, 1, paramInt1, paramInt2);
/*     */ 
/* 138 */     return paramInt1 + paramGraphics.getFontMetrics().charWidth(paramChar);
/*     */   }
/*     */ 
/*     */   public Shape modelToView(int paramInt, Shape paramShape, Position.Bias paramBias)
/*     */     throws BadLocationException
/*     */   {
/* 153 */     Container localContainer = getContainer();
/* 154 */     if ((localContainer instanceof JPasswordField)) {
/* 155 */       JPasswordField localJPasswordField = (JPasswordField)localContainer;
/* 156 */       if (!localJPasswordField.echoCharIsSet()) {
/* 157 */         return super.modelToView(paramInt, paramShape, paramBias);
/*     */       }
/* 159 */       char c = localJPasswordField.getEchoChar();
/* 160 */       FontMetrics localFontMetrics = localJPasswordField.getFontMetrics(localJPasswordField.getFont());
/*     */ 
/* 162 */       Rectangle localRectangle = adjustAllocation(paramShape).getBounds();
/* 163 */       int i = (paramInt - getStartOffset()) * localFontMetrics.charWidth(c);
/* 164 */       localRectangle.x += i;
/* 165 */       localRectangle.width = 1;
/* 166 */       return localRectangle;
/*     */     }
/* 168 */     return null;
/*     */   }
/*     */ 
/*     */   public int viewToModel(float paramFloat1, float paramFloat2, Shape paramShape, Position.Bias[] paramArrayOfBias)
/*     */   {
/* 183 */     paramArrayOfBias[0] = Position.Bias.Forward;
/* 184 */     int i = 0;
/* 185 */     Container localContainer = getContainer();
/* 186 */     if ((localContainer instanceof JPasswordField)) {
/* 187 */       JPasswordField localJPasswordField = (JPasswordField)localContainer;
/* 188 */       if (!localJPasswordField.echoCharIsSet()) {
/* 189 */         return super.viewToModel(paramFloat1, paramFloat2, paramShape, paramArrayOfBias);
/*     */       }
/* 191 */       char c = localJPasswordField.getEchoChar();
/* 192 */       int j = localJPasswordField.getFontMetrics(localJPasswordField.getFont()).charWidth(c);
/* 193 */       paramShape = adjustAllocation(paramShape);
/* 194 */       Rectangle localRectangle = (paramShape instanceof Rectangle) ? (Rectangle)paramShape : paramShape.getBounds();
/*     */ 
/* 196 */       i = j > 0 ? ((int)paramFloat1 - localRectangle.x) / j : 2147483647;
/*     */ 
/* 198 */       if (i < 0) {
/* 199 */         i = 0;
/*     */       }
/* 201 */       else if (i > getStartOffset() + getDocument().getLength()) {
/* 202 */         i = getDocument().getLength() - getStartOffset();
/*     */       }
/*     */     }
/* 205 */     return getStartOffset() + i;
/*     */   }
/*     */ 
/*     */   public float getPreferredSpan(int paramInt)
/*     */   {
/* 219 */     switch (paramInt) {
/*     */     case 0:
/* 221 */       Container localContainer = getContainer();
/* 222 */       if ((localContainer instanceof JPasswordField)) {
/* 223 */         JPasswordField localJPasswordField = (JPasswordField)localContainer;
/* 224 */         if (localJPasswordField.echoCharIsSet()) {
/* 225 */           char c = localJPasswordField.getEchoChar();
/* 226 */           FontMetrics localFontMetrics = localJPasswordField.getFontMetrics(localJPasswordField.getFont());
/* 227 */           Document localDocument = getDocument();
/* 228 */           return localFontMetrics.charWidth(c) * getDocument().getLength();
/*     */         }
/*     */       }
/*     */       break;
/*     */     }
/* 232 */     return super.getPreferredSpan(paramInt);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.text.PasswordView
 * JD-Core Version:    0.6.2
 */