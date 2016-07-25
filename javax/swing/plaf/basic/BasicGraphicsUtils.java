/*     */ package javax.swing.plaf.basic;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.ComponentOrientation;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Font;
/*     */ import java.awt.FontMetrics;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Insets;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.Toolkit;
/*     */ import java.awt.event.InputEvent;
/*     */ import javax.swing.AbstractButton;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.SwingUtilities;
/*     */ import sun.swing.SwingUtilities2;
/*     */ 
/*     */ public class BasicGraphicsUtils
/*     */ {
/*  50 */   private static final Insets GROOVE_INSETS = new Insets(2, 2, 2, 2);
/*  51 */   private static final Insets ETCHED_INSETS = new Insets(2, 2, 2, 2);
/*     */ 
/*     */   public static void drawEtchedRect(Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, Color paramColor1, Color paramColor2, Color paramColor3, Color paramColor4)
/*     */   {
/*  57 */     Color localColor = paramGraphics.getColor();
/*  58 */     paramGraphics.translate(paramInt1, paramInt2);
/*     */ 
/*  60 */     paramGraphics.setColor(paramColor1);
/*  61 */     paramGraphics.drawLine(0, 0, paramInt3 - 1, 0);
/*  62 */     paramGraphics.drawLine(0, 1, 0, paramInt4 - 2);
/*     */ 
/*  64 */     paramGraphics.setColor(paramColor2);
/*  65 */     paramGraphics.drawLine(1, 1, paramInt3 - 3, 1);
/*  66 */     paramGraphics.drawLine(1, 2, 1, paramInt4 - 3);
/*     */ 
/*  68 */     paramGraphics.setColor(paramColor4);
/*  69 */     paramGraphics.drawLine(paramInt3 - 1, 0, paramInt3 - 1, paramInt4 - 1);
/*  70 */     paramGraphics.drawLine(0, paramInt4 - 1, paramInt3 - 1, paramInt4 - 1);
/*     */ 
/*  72 */     paramGraphics.setColor(paramColor3);
/*  73 */     paramGraphics.drawLine(paramInt3 - 2, 1, paramInt3 - 2, paramInt4 - 3);
/*  74 */     paramGraphics.drawLine(1, paramInt4 - 2, paramInt3 - 2, paramInt4 - 2);
/*     */ 
/*  76 */     paramGraphics.translate(-paramInt1, -paramInt2);
/*  77 */     paramGraphics.setColor(localColor);
/*     */   }
/*     */ 
/*     */   public static Insets getEtchedInsets()
/*     */   {
/*  88 */     return ETCHED_INSETS;
/*     */   }
/*     */ 
/*     */   public static void drawGroove(Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, Color paramColor1, Color paramColor2)
/*     */   {
/*  95 */     Color localColor = paramGraphics.getColor();
/*  96 */     paramGraphics.translate(paramInt1, paramInt2);
/*     */ 
/*  98 */     paramGraphics.setColor(paramColor1);
/*  99 */     paramGraphics.drawRect(0, 0, paramInt3 - 2, paramInt4 - 2);
/*     */ 
/* 101 */     paramGraphics.setColor(paramColor2);
/* 102 */     paramGraphics.drawLine(1, paramInt4 - 3, 1, 1);
/* 103 */     paramGraphics.drawLine(1, 1, paramInt3 - 3, 1);
/*     */ 
/* 105 */     paramGraphics.drawLine(0, paramInt4 - 1, paramInt3 - 1, paramInt4 - 1);
/* 106 */     paramGraphics.drawLine(paramInt3 - 1, paramInt4 - 1, paramInt3 - 1, 0);
/*     */ 
/* 108 */     paramGraphics.translate(-paramInt1, -paramInt2);
/* 109 */     paramGraphics.setColor(localColor);
/*     */   }
/*     */ 
/*     */   public static Insets getGrooveInsets()
/*     */   {
/* 119 */     return GROOVE_INSETS;
/*     */   }
/*     */ 
/*     */   public static void drawBezel(Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, boolean paramBoolean1, boolean paramBoolean2, Color paramColor1, Color paramColor2, Color paramColor3, Color paramColor4)
/*     */   {
/* 128 */     Color localColor = paramGraphics.getColor();
/* 129 */     paramGraphics.translate(paramInt1, paramInt2);
/*     */ 
/* 131 */     if ((paramBoolean1) && (paramBoolean2)) {
/* 132 */       paramGraphics.setColor(paramColor2);
/* 133 */       paramGraphics.drawRect(0, 0, paramInt3 - 1, paramInt4 - 1);
/* 134 */       paramGraphics.setColor(paramColor1);
/* 135 */       paramGraphics.drawRect(1, 1, paramInt3 - 3, paramInt4 - 3);
/* 136 */     } else if (paramBoolean1) {
/* 137 */       drawLoweredBezel(paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, paramColor1, paramColor2, paramColor3, paramColor4);
/*     */     }
/* 139 */     else if (paramBoolean2) {
/* 140 */       paramGraphics.setColor(paramColor2);
/* 141 */       paramGraphics.drawRect(0, 0, paramInt3 - 1, paramInt4 - 1);
/*     */ 
/* 143 */       paramGraphics.setColor(paramColor4);
/* 144 */       paramGraphics.drawLine(1, 1, 1, paramInt4 - 3);
/* 145 */       paramGraphics.drawLine(2, 1, paramInt3 - 3, 1);
/*     */ 
/* 147 */       paramGraphics.setColor(paramColor3);
/* 148 */       paramGraphics.drawLine(2, 2, 2, paramInt4 - 4);
/* 149 */       paramGraphics.drawLine(3, 2, paramInt3 - 4, 2);
/*     */ 
/* 151 */       paramGraphics.setColor(paramColor1);
/* 152 */       paramGraphics.drawLine(2, paramInt4 - 3, paramInt3 - 3, paramInt4 - 3);
/* 153 */       paramGraphics.drawLine(paramInt3 - 3, 2, paramInt3 - 3, paramInt4 - 4);
/*     */ 
/* 155 */       paramGraphics.setColor(paramColor2);
/* 156 */       paramGraphics.drawLine(1, paramInt4 - 2, paramInt3 - 2, paramInt4 - 2);
/* 157 */       paramGraphics.drawLine(paramInt3 - 2, paramInt4 - 2, paramInt3 - 2, 1);
/*     */     } else {
/* 159 */       paramGraphics.setColor(paramColor4);
/* 160 */       paramGraphics.drawLine(0, 0, 0, paramInt4 - 1);
/* 161 */       paramGraphics.drawLine(1, 0, paramInt3 - 2, 0);
/*     */ 
/* 163 */       paramGraphics.setColor(paramColor3);
/* 164 */       paramGraphics.drawLine(1, 1, 1, paramInt4 - 3);
/* 165 */       paramGraphics.drawLine(2, 1, paramInt3 - 3, 1);
/*     */ 
/* 167 */       paramGraphics.setColor(paramColor1);
/* 168 */       paramGraphics.drawLine(1, paramInt4 - 2, paramInt3 - 2, paramInt4 - 2);
/* 169 */       paramGraphics.drawLine(paramInt3 - 2, 1, paramInt3 - 2, paramInt4 - 3);
/*     */ 
/* 171 */       paramGraphics.setColor(paramColor2);
/* 172 */       paramGraphics.drawLine(0, paramInt4 - 1, paramInt3 - 1, paramInt4 - 1);
/* 173 */       paramGraphics.drawLine(paramInt3 - 1, paramInt4 - 1, paramInt3 - 1, 0);
/*     */     }
/* 175 */     paramGraphics.translate(-paramInt1, -paramInt2);
/* 176 */     paramGraphics.setColor(localColor);
/*     */   }
/*     */ 
/*     */   public static void drawLoweredBezel(Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, Color paramColor1, Color paramColor2, Color paramColor3, Color paramColor4)
/*     */   {
/* 182 */     paramGraphics.setColor(paramColor2);
/* 183 */     paramGraphics.drawLine(0, 0, 0, paramInt4 - 1);
/* 184 */     paramGraphics.drawLine(1, 0, paramInt3 - 2, 0);
/*     */ 
/* 186 */     paramGraphics.setColor(paramColor1);
/* 187 */     paramGraphics.drawLine(1, 1, 1, paramInt4 - 2);
/* 188 */     paramGraphics.drawLine(1, 1, paramInt3 - 3, 1);
/*     */ 
/* 190 */     paramGraphics.setColor(paramColor4);
/* 191 */     paramGraphics.drawLine(0, paramInt4 - 1, paramInt3 - 1, paramInt4 - 1);
/* 192 */     paramGraphics.drawLine(paramInt3 - 1, paramInt4 - 1, paramInt3 - 1, 0);
/*     */ 
/* 194 */     paramGraphics.setColor(paramColor3);
/* 195 */     paramGraphics.drawLine(1, paramInt4 - 2, paramInt3 - 2, paramInt4 - 2);
/* 196 */     paramGraphics.drawLine(paramInt3 - 2, paramInt4 - 2, paramInt3 - 2, 1);
/*     */   }
/*     */ 
/*     */   public static void drawString(Graphics paramGraphics, String paramString, int paramInt1, int paramInt2, int paramInt3)
/*     */   {
/* 207 */     int i = -1;
/*     */ 
/* 209 */     if (paramInt1 != 0) {
/* 210 */       int j = Character.toUpperCase((char)paramInt1);
/* 211 */       int k = Character.toLowerCase((char)paramInt1);
/* 212 */       int m = paramString.indexOf(j);
/* 213 */       int n = paramString.indexOf(k);
/*     */ 
/* 215 */       if (m == -1) {
/* 216 */         i = n;
/*     */       }
/* 218 */       else if (n == -1) {
/* 219 */         i = m;
/*     */       }
/*     */       else {
/* 222 */         i = n < m ? n : m;
/*     */       }
/*     */     }
/* 225 */     drawStringUnderlineCharAt(paramGraphics, paramString, i, paramInt2, paramInt3);
/*     */   }
/*     */ 
/*     */   public static void drawStringUnderlineCharAt(Graphics paramGraphics, String paramString, int paramInt1, int paramInt2, int paramInt3)
/*     */   {
/* 246 */     SwingUtilities2.drawStringUnderlineCharAt(null, paramGraphics, paramString, paramInt1, paramInt2, paramInt3);
/*     */   }
/*     */ 
/*     */   public static void drawDashedRect(Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/* 254 */     for (int i = paramInt1; i < paramInt1 + paramInt3; i += 2) {
/* 255 */       paramGraphics.fillRect(i, paramInt2, 1, 1);
/* 256 */       paramGraphics.fillRect(i, paramInt2 + paramInt4 - 1, 1, 1);
/*     */     }
/*     */ 
/* 260 */     for (int j = paramInt2; j < paramInt2 + paramInt4; j += 2) {
/* 261 */       paramGraphics.fillRect(paramInt1, j, 1, 1);
/* 262 */       paramGraphics.fillRect(paramInt1 + paramInt3 - 1, j, 1, 1);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static Dimension getPreferredButtonSize(AbstractButton paramAbstractButton, int paramInt)
/*     */   {
/* 268 */     if (paramAbstractButton.getComponentCount() > 0) {
/* 269 */       return null;
/*     */     }
/*     */ 
/* 272 */     Icon localIcon = paramAbstractButton.getIcon();
/* 273 */     String str = paramAbstractButton.getText();
/*     */ 
/* 275 */     Font localFont = paramAbstractButton.getFont();
/* 276 */     FontMetrics localFontMetrics = paramAbstractButton.getFontMetrics(localFont);
/*     */ 
/* 278 */     Rectangle localRectangle1 = new Rectangle();
/* 279 */     Rectangle localRectangle2 = new Rectangle();
/* 280 */     Rectangle localRectangle3 = new Rectangle(32767, 32767);
/*     */ 
/* 282 */     SwingUtilities.layoutCompoundLabel(paramAbstractButton, localFontMetrics, str, localIcon, paramAbstractButton.getVerticalAlignment(), paramAbstractButton.getHorizontalAlignment(), paramAbstractButton.getVerticalTextPosition(), paramAbstractButton.getHorizontalTextPosition(), localRectangle3, localRectangle1, localRectangle2, str == null ? 0 : paramInt);
/*     */ 
/* 293 */     Rectangle localRectangle4 = localRectangle1.union(localRectangle2);
/*     */ 
/* 295 */     Insets localInsets = paramAbstractButton.getInsets();
/* 296 */     localRectangle4.width += localInsets.left + localInsets.right;
/* 297 */     localRectangle4.height += localInsets.top + localInsets.bottom;
/*     */ 
/* 299 */     return localRectangle4.getSize();
/*     */   }
/*     */ 
/*     */   static boolean isLeftToRight(Component paramComponent)
/*     */   {
/* 307 */     return paramComponent.getComponentOrientation().isLeftToRight();
/*     */   }
/*     */ 
/*     */   static boolean isMenuShortcutKeyDown(InputEvent paramInputEvent) {
/* 311 */     return (paramInputEvent.getModifiers() & Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()) != 0;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.basic.BasicGraphicsUtils
 * JD-Core Version:    0.6.2
 */