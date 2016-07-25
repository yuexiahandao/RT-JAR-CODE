/*     */ package com.sun.java.swing.plaf.motif;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.ComponentOrientation;
/*     */ import java.awt.Container;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Font;
/*     */ import java.awt.FontMetrics;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Insets;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.event.KeyEvent;
/*     */ import javax.swing.ButtonModel;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JMenu;
/*     */ import javax.swing.JMenuBar;
/*     */ import javax.swing.JMenuItem;
/*     */ import javax.swing.KeyStroke;
/*     */ import javax.swing.SwingConstants;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.text.View;
/*     */ import sun.swing.SwingUtilities2;
/*     */ 
/*     */ public class MotifGraphicsUtils
/*     */   implements SwingConstants
/*     */ {
/*     */   private static final String MAX_ACC_WIDTH = "maxAccWidth";
/*     */ 
/*     */   static void drawPoint(Graphics paramGraphics, int paramInt1, int paramInt2)
/*     */   {
/*  58 */     paramGraphics.drawLine(paramInt1, paramInt2, paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   public static void drawGroove(Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, Color paramColor1, Color paramColor2)
/*     */   {
/*  68 */     Color localColor = paramGraphics.getColor();
/*  69 */     paramGraphics.translate(paramInt1, paramInt2);
/*     */ 
/*  71 */     paramGraphics.setColor(paramColor1);
/*  72 */     paramGraphics.drawRect(0, 0, paramInt3 - 2, paramInt4 - 2);
/*     */ 
/*  74 */     paramGraphics.setColor(paramColor2);
/*  75 */     paramGraphics.drawLine(1, paramInt4 - 3, 1, 1);
/*  76 */     paramGraphics.drawLine(1, 1, paramInt3 - 3, 1);
/*     */ 
/*  78 */     paramGraphics.drawLine(0, paramInt4 - 1, paramInt3 - 1, paramInt4 - 1);
/*  79 */     paramGraphics.drawLine(paramInt3 - 1, paramInt4 - 1, paramInt3 - 1, 0);
/*     */ 
/*  81 */     paramGraphics.translate(-paramInt1, -paramInt2);
/*  82 */     paramGraphics.setColor(localColor);
/*     */   }
/*     */ 
/*     */   public static void drawStringInRect(Graphics paramGraphics, String paramString, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*     */   {
/*  96 */     drawStringInRect(null, paramGraphics, paramString, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
/*     */   }
/*     */ 
/*     */   static void drawStringInRect(JComponent paramJComponent, Graphics paramGraphics, String paramString, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*     */   {
/* 105 */     if (paramGraphics.getFont() == null)
/*     */     {
/* 107 */       return;
/*     */     }
/* 109 */     FontMetrics localFontMetrics = SwingUtilities2.getFontMetrics(paramJComponent, paramGraphics);
/* 110 */     if (localFontMetrics == null)
/*     */       return;
/*     */     int i;
/*     */     int j;
/* 115 */     if (paramInt5 == 0) {
/* 116 */       i = SwingUtilities2.stringWidth(paramJComponent, localFontMetrics, paramString);
/* 117 */       if (i > paramInt3) {
/* 118 */         i = paramInt3;
/*     */       }
/* 120 */       j = paramInt1 + (paramInt3 - i) / 2;
/* 121 */     } else if (paramInt5 == 4) {
/* 122 */       i = SwingUtilities2.stringWidth(paramJComponent, localFontMetrics, paramString);
/* 123 */       if (i > paramInt3) {
/* 124 */         i = paramInt3;
/*     */       }
/* 126 */       j = paramInt1 + paramInt3 - i;
/*     */     } else {
/* 128 */       j = paramInt1;
/*     */     }
/*     */ 
/* 131 */     int m = (paramInt4 - localFontMetrics.getAscent() - localFontMetrics.getDescent()) / 2;
/* 132 */     if (m < 0) {
/* 133 */       m = 0;
/*     */     }
/*     */ 
/* 136 */     int k = paramInt2 + paramInt4 - m - localFontMetrics.getDescent();
/*     */ 
/* 138 */     SwingUtilities2.drawString(paramJComponent, paramGraphics, paramString, j, k);
/*     */   }
/*     */ 
/*     */   public static void paintMenuItem(Graphics paramGraphics, JComponent paramJComponent, Icon paramIcon1, Icon paramIcon2, Color paramColor1, Color paramColor2, int paramInt)
/*     */   {
/* 152 */     JMenuItem localJMenuItem = (JMenuItem)paramJComponent;
/* 153 */     ButtonModel localButtonModel = localJMenuItem.getModel();
/*     */ 
/* 155 */     Dimension localDimension = localJMenuItem.getSize();
/* 156 */     Insets localInsets = paramJComponent.getInsets();
/*     */ 
/* 158 */     Rectangle localRectangle1 = new Rectangle(localDimension);
/*     */ 
/* 160 */     localRectangle1.x += localInsets.left;
/* 161 */     localRectangle1.y += localInsets.top;
/* 162 */     localRectangle1.width -= localInsets.right + localRectangle1.x;
/* 163 */     localRectangle1.height -= localInsets.bottom + localRectangle1.y;
/*     */ 
/* 165 */     Rectangle localRectangle2 = new Rectangle();
/* 166 */     Rectangle localRectangle3 = new Rectangle();
/* 167 */     Rectangle localRectangle4 = new Rectangle();
/* 168 */     Rectangle localRectangle5 = new Rectangle();
/* 169 */     Rectangle localRectangle6 = new Rectangle();
/*     */ 
/* 171 */     Font localFont1 = paramGraphics.getFont();
/* 172 */     Font localFont2 = paramJComponent.getFont();
/* 173 */     paramGraphics.setFont(localFont2);
/* 174 */     FontMetrics localFontMetrics1 = SwingUtilities2.getFontMetrics(paramJComponent, paramGraphics, localFont2);
/* 175 */     FontMetrics localFontMetrics2 = SwingUtilities2.getFontMetrics(paramJComponent, paramGraphics, UIManager.getFont("MenuItem.acceleratorFont"));
/*     */ 
/* 178 */     if (paramJComponent.isOpaque()) {
/* 179 */       if ((localButtonModel.isArmed()) || (((paramJComponent instanceof JMenu)) && (localButtonModel.isSelected())))
/* 180 */         paramGraphics.setColor(paramColor1);
/*     */       else {
/* 182 */         paramGraphics.setColor(paramJComponent.getBackground());
/*     */       }
/* 184 */       paramGraphics.fillRect(0, 0, localDimension.width, localDimension.height);
/*     */     }
/*     */ 
/* 188 */     KeyStroke localKeyStroke = localJMenuItem.getAccelerator();
/* 189 */     String str1 = "";
/* 190 */     if (localKeyStroke != null) {
/* 191 */       int i = localKeyStroke.getModifiers();
/* 192 */       if (i > 0) {
/* 193 */         str1 = KeyEvent.getKeyModifiersText(i);
/* 194 */         str1 = str1 + "+";
/*     */       }
/* 196 */       str1 = str1 + KeyEvent.getKeyText(localKeyStroke.getKeyCode());
/*     */     }
/*     */ 
/* 200 */     String str2 = layoutMenuItem(paramJComponent, localFontMetrics1, localJMenuItem.getText(), localFontMetrics2, str1, localJMenuItem.getIcon(), paramIcon1, paramIcon2, localJMenuItem.getVerticalAlignment(), localJMenuItem.getHorizontalAlignment(), localJMenuItem.getVerticalTextPosition(), localJMenuItem.getHorizontalTextPosition(), localRectangle1, localRectangle2, localRectangle3, localRectangle4, localRectangle5, localRectangle6, localJMenuItem.getText() == null ? 0 : paramInt, paramInt);
/*     */ 
/* 216 */     Color localColor = paramGraphics.getColor();
/* 217 */     if (paramIcon1 != null) {
/* 218 */       if ((localButtonModel.isArmed()) || (((paramJComponent instanceof JMenu)) && (localButtonModel.isSelected())))
/* 219 */         paramGraphics.setColor(paramColor2);
/* 220 */       paramIcon1.paintIcon(paramJComponent, paramGraphics, localRectangle5.x, localRectangle5.y);
/* 221 */       paramGraphics.setColor(localColor);
/*     */     }
/*     */     Object localObject;
/* 225 */     if (localJMenuItem.getIcon() != null)
/*     */     {
/* 227 */       if (!localButtonModel.isEnabled()) {
/* 228 */         localObject = localJMenuItem.getDisabledIcon();
/* 229 */       } else if ((localButtonModel.isPressed()) && (localButtonModel.isArmed())) {
/* 230 */         localObject = localJMenuItem.getPressedIcon();
/* 231 */         if (localObject == null)
/*     */         {
/* 233 */           localObject = localJMenuItem.getIcon();
/*     */         }
/*     */       } else {
/* 236 */         localObject = localJMenuItem.getIcon();
/*     */       }
/*     */ 
/* 239 */       if (localObject != null) {
/* 240 */         ((Icon)localObject).paintIcon(paramJComponent, paramGraphics, localRectangle2.x, localRectangle2.y);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 245 */     if ((str2 != null) && (!str2.equals("")))
/*     */     {
/* 248 */       localObject = (View)paramJComponent.getClientProperty("html");
/* 249 */       if (localObject != null) {
/* 250 */         ((View)localObject).paint(paramGraphics, localRectangle3);
/*     */       } else {
/* 252 */         int k = localJMenuItem.getDisplayedMnemonicIndex();
/*     */ 
/* 254 */         if (!localButtonModel.isEnabled())
/*     */         {
/* 256 */           paramGraphics.setColor(localJMenuItem.getBackground().brighter());
/* 257 */           SwingUtilities2.drawStringUnderlineCharAt(localJMenuItem, paramGraphics, str2, k, localRectangle3.x, localRectangle3.y + localFontMetrics2.getAscent());
/*     */ 
/* 260 */           paramGraphics.setColor(localJMenuItem.getBackground().darker());
/* 261 */           SwingUtilities2.drawStringUnderlineCharAt(localJMenuItem, paramGraphics, str2, k, localRectangle3.x - 1, localRectangle3.y + localFontMetrics2.getAscent() - 1);
/*     */         }
/*     */         else
/*     */         {
/* 267 */           if ((localButtonModel.isArmed()) || (((paramJComponent instanceof JMenu)) && (localButtonModel.isSelected())))
/* 268 */             paramGraphics.setColor(paramColor2);
/*     */           else {
/* 270 */             paramGraphics.setColor(localJMenuItem.getForeground());
/*     */           }
/* 272 */           SwingUtilities2.drawStringUnderlineCharAt(localJMenuItem, paramGraphics, str2, k, localRectangle3.x, localRectangle3.y + localFontMetrics1.getAscent());
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 281 */     if ((str1 != null) && (!str1.equals("")))
/*     */     {
/* 284 */       int j = 0;
/* 285 */       Container localContainer = localJMenuItem.getParent();
/* 286 */       if ((localContainer != null) && ((localContainer instanceof JComponent))) {
/* 287 */         JComponent localJComponent = (JComponent)localContainer;
/* 288 */         Integer localInteger = (Integer)localJComponent.getClientProperty("maxAccWidth");
/* 289 */         int m = localInteger != null ? localInteger.intValue() : localRectangle4.width;
/*     */ 
/* 293 */         j = m - localRectangle4.width;
/*     */       }
/*     */ 
/* 296 */       paramGraphics.setFont(UIManager.getFont("MenuItem.acceleratorFont"));
/* 297 */       if (!localButtonModel.isEnabled())
/*     */       {
/* 299 */         paramGraphics.setColor(localJMenuItem.getBackground().brighter());
/* 300 */         SwingUtilities2.drawString(paramJComponent, paramGraphics, str1, localRectangle4.x - j, localRectangle4.y + localFontMetrics1.getAscent());
/*     */ 
/* 302 */         paramGraphics.setColor(localJMenuItem.getBackground().darker());
/* 303 */         SwingUtilities2.drawString(paramJComponent, paramGraphics, str1, localRectangle4.x - j - 1, localRectangle4.y + localFontMetrics1.getAscent() - 1);
/*     */       }
/*     */       else
/*     */       {
/* 307 */         if ((localButtonModel.isArmed()) || (((paramJComponent instanceof JMenu)) && (localButtonModel.isSelected())))
/*     */         {
/* 309 */           paramGraphics.setColor(paramColor2);
/*     */         }
/* 311 */         else paramGraphics.setColor(localJMenuItem.getForeground());
/*     */ 
/* 313 */         SwingUtilities2.drawString(paramJComponent, paramGraphics, str1, localRectangle4.x - j, localRectangle4.y + localFontMetrics2.getAscent());
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 320 */     if (paramIcon2 != null) {
/* 321 */       if ((localButtonModel.isArmed()) || (((paramJComponent instanceof JMenu)) && (localButtonModel.isSelected())))
/* 322 */         paramGraphics.setColor(paramColor2);
/* 323 */       if (!(localJMenuItem.getParent() instanceof JMenuBar)) {
/* 324 */         paramIcon2.paintIcon(paramJComponent, paramGraphics, localRectangle6.x, localRectangle6.y);
/*     */       }
/*     */     }
/* 327 */     paramGraphics.setColor(localColor);
/* 328 */     paramGraphics.setFont(localFont1);
/*     */   }
/*     */ 
/*     */   private static String layoutMenuItem(JComponent paramJComponent, FontMetrics paramFontMetrics1, String paramString1, FontMetrics paramFontMetrics2, String paramString2, Icon paramIcon1, Icon paramIcon2, Icon paramIcon3, int paramInt1, int paramInt2, int paramInt3, int paramInt4, Rectangle paramRectangle1, Rectangle paramRectangle2, Rectangle paramRectangle3, Rectangle paramRectangle4, Rectangle paramRectangle5, Rectangle paramRectangle6, int paramInt5, int paramInt6)
/*     */   {
/* 363 */     SwingUtilities.layoutCompoundLabel(paramJComponent, paramFontMetrics1, paramString1, paramIcon1, paramInt1, paramInt2, paramInt3, paramInt4, paramRectangle1, paramRectangle2, paramRectangle3, paramInt5);
/*     */ 
/* 380 */     if ((paramString2 == null) || (paramString2.equals(""))) {
/* 381 */       paramRectangle4.width = (paramRectangle4.height = 0);
/* 382 */       paramString2 = "";
/*     */     }
/*     */     else {
/* 385 */       paramRectangle4.width = SwingUtilities2.stringWidth(paramJComponent, paramFontMetrics2, paramString2);
/*     */ 
/* 387 */       paramRectangle4.height = paramFontMetrics2.getHeight();
/*     */     }
/*     */ 
/* 393 */     if (paramIcon2 != null) {
/* 394 */       paramRectangle5.width = paramIcon2.getIconWidth();
/* 395 */       paramRectangle5.height = paramIcon2.getIconHeight();
/*     */     }
/*     */     else {
/* 398 */       paramRectangle5.width = (paramRectangle5.height = 0);
/*     */     }
/*     */ 
/* 404 */     if (paramIcon3 != null) {
/* 405 */       paramRectangle6.width = paramIcon3.getIconWidth();
/* 406 */       paramRectangle6.height = paramIcon3.getIconHeight();
/*     */     }
/*     */     else {
/* 409 */       paramRectangle6.width = (paramRectangle6.height = 0);
/*     */     }
/*     */ 
/* 413 */     Rectangle localRectangle = paramRectangle2.union(paramRectangle3);
/* 414 */     if (isLeftToRight(paramJComponent)) {
/* 415 */       paramRectangle3.x += paramRectangle5.width + paramInt6;
/* 416 */       paramRectangle2.x += paramRectangle5.width + paramInt6;
/*     */ 
/* 419 */       paramRectangle4.x = (paramRectangle1.x + paramRectangle1.width - paramRectangle6.width - paramInt6 - paramRectangle4.width);
/*     */ 
/* 423 */       paramRectangle5.x = paramRectangle1.x;
/* 424 */       paramRectangle6.x = (paramRectangle1.x + paramRectangle1.width - paramInt6 - paramRectangle6.width);
/*     */     }
/*     */     else {
/* 427 */       paramRectangle3.x -= paramRectangle5.width + paramInt6;
/* 428 */       paramRectangle2.x -= paramRectangle5.width + paramInt6;
/*     */ 
/* 431 */       paramRectangle4.x = (paramRectangle1.x + paramRectangle6.width + paramInt6);
/*     */ 
/* 434 */       paramRectangle5.x = (paramRectangle1.x + paramRectangle1.width - paramRectangle5.width);
/* 435 */       paramRectangle1.x += paramInt6;
/*     */     }
/*     */ 
/* 440 */     paramRectangle4.y = (localRectangle.y + localRectangle.height / 2 - paramRectangle4.height / 2);
/* 441 */     paramRectangle6.y = (localRectangle.y + localRectangle.height / 2 - paramRectangle6.height / 2);
/* 442 */     paramRectangle5.y = (localRectangle.y + localRectangle.height / 2 - paramRectangle5.height / 2);
/*     */ 
/* 448 */     return paramString1;
/*     */   }
/*     */ 
/*     */   private static void drawMenuBezel(Graphics paramGraphics, Color paramColor, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/* 456 */     paramGraphics.setColor(paramColor);
/* 457 */     paramGraphics.fillRect(paramInt1, paramInt2, paramInt3, paramInt4);
/*     */ 
/* 459 */     paramGraphics.setColor(paramColor.brighter().brighter());
/* 460 */     paramGraphics.drawLine(paramInt1 + 1, paramInt2 + paramInt4 - 1, paramInt1 + paramInt3 - 1, paramInt2 + paramInt4 - 1);
/* 461 */     paramGraphics.drawLine(paramInt1 + paramInt3 - 1, paramInt2 + paramInt4 - 2, paramInt1 + paramInt3 - 1, paramInt2 + 1);
/*     */ 
/* 463 */     paramGraphics.setColor(paramColor.darker().darker());
/* 464 */     paramGraphics.drawLine(paramInt1, paramInt2, paramInt1 + paramInt3 - 2, paramInt2);
/* 465 */     paramGraphics.drawLine(paramInt1, paramInt2 + 1, paramInt1, paramInt2 + paramInt4 - 2);
/*     */   }
/*     */ 
/*     */   static boolean isLeftToRight(Component paramComponent)
/*     */   {
/* 474 */     return paramComponent.getComponentOrientation().isLeftToRight();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.java.swing.plaf.motif.MotifGraphicsUtils
 * JD-Core Version:    0.6.2
 */