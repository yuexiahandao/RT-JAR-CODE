/*     */ package com.sun.java.swing.plaf.motif;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Graphics;
/*     */ import java.io.Serializable;
/*     */ import javax.swing.AbstractButton;
/*     */ import javax.swing.ButtonModel;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.JCheckBox;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.plaf.UIResource;
/*     */ 
/*     */ public class MotifIconFactory
/*     */   implements Serializable
/*     */ {
/*     */   private static Icon checkBoxIcon;
/*     */   private static Icon radioButtonIcon;
/*     */   private static Icon menuItemCheckIcon;
/*     */   private static Icon menuItemArrowIcon;
/*     */   private static Icon menuArrowIcon;
/*     */ 
/*     */   public static Icon getMenuItemCheckIcon()
/*     */   {
/*  62 */     return null;
/*     */   }
/*     */ 
/*     */   public static Icon getMenuItemArrowIcon() {
/*  66 */     if (menuItemArrowIcon == null) {
/*  67 */       menuItemArrowIcon = new MenuItemArrowIcon(null);
/*     */     }
/*  69 */     return menuItemArrowIcon;
/*     */   }
/*     */ 
/*     */   public static Icon getMenuArrowIcon() {
/*  73 */     if (menuArrowIcon == null) {
/*  74 */       menuArrowIcon = new MenuArrowIcon(null);
/*     */     }
/*  76 */     return menuArrowIcon;
/*     */   }
/*     */ 
/*     */   public static Icon getCheckBoxIcon() {
/*  80 */     if (checkBoxIcon == null) {
/*  81 */       checkBoxIcon = new CheckBoxIcon(null);
/*     */     }
/*  83 */     return checkBoxIcon;
/*     */   }
/*     */ 
/*     */   public static Icon getRadioButtonIcon() {
/*  87 */     if (radioButtonIcon == null) {
/*  88 */       radioButtonIcon = new RadioButtonIcon(null);
/*     */     }
/*  90 */     return radioButtonIcon;
/*     */   }
/*     */ 
/*     */   private static class CheckBoxIcon
/*     */     implements Icon, UIResource, Serializable
/*     */   {
/*     */     static final int csize = 13;
/*  96 */     private Color control = UIManager.getColor("control");
/*  97 */     private Color foreground = UIManager.getColor("CheckBox.foreground");
/*  98 */     private Color shadow = UIManager.getColor("controlShadow");
/*  99 */     private Color highlight = UIManager.getColor("controlHighlight");
/* 100 */     private Color lightShadow = UIManager.getColor("controlLightShadow");
/*     */ 
/*     */     public void paintIcon(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2) {
/* 103 */       AbstractButton localAbstractButton = (AbstractButton)paramComponent;
/* 104 */       ButtonModel localButtonModel = localAbstractButton.getModel();
/*     */ 
/* 106 */       boolean bool1 = false;
/*     */ 
/* 108 */       if ((localAbstractButton instanceof JCheckBox)) {
/* 109 */         bool1 = ((JCheckBox)localAbstractButton).isBorderPaintedFlat();
/*     */       }
/*     */ 
/* 112 */       boolean bool2 = localButtonModel.isPressed();
/* 113 */       boolean bool3 = localButtonModel.isArmed();
/* 114 */       boolean bool4 = localButtonModel.isEnabled();
/* 115 */       boolean bool5 = localButtonModel.isSelected();
/*     */ 
/* 122 */       int i = ((bool2) && (!bool3) && (bool5)) || ((bool2) && (bool3) && (!bool5)) ? 1 : 0;
/*     */ 
/* 128 */       int j = ((bool2) && (!bool3) && (!bool5)) || ((bool2) && (bool3) && (bool5)) ? 1 : 0;
/*     */ 
/* 135 */       int k = ((!bool2) && (bool3) && (bool5)) || ((!bool2) && (!bool3) && (bool5)) ? 1 : 0;
/*     */ 
/* 143 */       if (bool1) {
/* 144 */         paramGraphics.setColor(this.shadow);
/* 145 */         paramGraphics.drawRect(paramInt1 + 2, paramInt2, 12, 12);
/* 146 */         if ((j != 0) || (i != 0)) {
/* 147 */           paramGraphics.setColor(this.control);
/* 148 */           paramGraphics.fillRect(paramInt1 + 3, paramInt2 + 1, 11, 11);
/*     */         }
/*     */       }
/*     */ 
/* 152 */       if (i != 0)
/*     */       {
/* 154 */         drawCheckBezel(paramGraphics, paramInt1, paramInt2, 13, true, false, false, bool1);
/* 155 */       } else if (j != 0)
/*     */       {
/* 157 */         drawCheckBezel(paramGraphics, paramInt1, paramInt2, 13, true, true, false, bool1);
/* 158 */       } else if (k != 0)
/*     */       {
/* 160 */         drawCheckBezel(paramGraphics, paramInt1, paramInt2, 13, false, false, true, bool1);
/* 161 */       } else if (!bool1)
/*     */       {
/* 163 */         drawCheckBezelOut(paramGraphics, paramInt1, paramInt2, 13);
/*     */       }
/*     */     }
/*     */ 
/*     */     public int getIconWidth() {
/* 168 */       return 13;
/*     */     }
/*     */ 
/*     */     public int getIconHeight() {
/* 172 */       return 13;
/*     */     }
/*     */ 
/*     */     public void drawCheckBezelOut(Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3) {
/* 176 */       Color localColor1 = UIManager.getColor("controlShadow");
/*     */ 
/* 178 */       int i = paramInt3;
/* 179 */       int j = paramInt3;
/* 180 */       Color localColor2 = paramGraphics.getColor();
/*     */ 
/* 182 */       paramGraphics.translate(paramInt1, paramInt2);
/* 183 */       paramGraphics.setColor(this.highlight);
/* 184 */       paramGraphics.drawLine(0, 0, 0, j - 1);
/* 185 */       paramGraphics.drawLine(1, 0, i - 1, 0);
/*     */ 
/* 187 */       paramGraphics.setColor(this.shadow);
/* 188 */       paramGraphics.drawLine(1, j - 1, i - 1, j - 1);
/* 189 */       paramGraphics.drawLine(i - 1, j - 1, i - 1, 1);
/* 190 */       paramGraphics.translate(-paramInt1, -paramInt2);
/* 191 */       paramGraphics.setColor(localColor2);
/*     */     }
/*     */ 
/*     */     public void drawCheckBezel(Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4)
/*     */     {
/* 199 */       Color localColor = paramGraphics.getColor();
/* 200 */       paramGraphics.translate(paramInt1, paramInt2);
/*     */ 
/* 204 */       if (!paramBoolean4) {
/* 205 */         if (paramBoolean2) {
/* 206 */           paramGraphics.setColor(this.control);
/* 207 */           paramGraphics.fillRect(1, 1, paramInt3 - 2, paramInt3 - 2);
/* 208 */           paramGraphics.setColor(this.shadow);
/*     */         } else {
/* 210 */           paramGraphics.setColor(this.lightShadow);
/* 211 */           paramGraphics.fillRect(0, 0, paramInt3, paramInt3);
/* 212 */           paramGraphics.setColor(this.highlight);
/*     */         }
/*     */ 
/* 215 */         paramGraphics.drawLine(1, paramInt3 - 1, paramInt3 - 2, paramInt3 - 1);
/* 216 */         if (paramBoolean1) {
/* 217 */           paramGraphics.drawLine(2, paramInt3 - 2, paramInt3 - 3, paramInt3 - 2);
/* 218 */           paramGraphics.drawLine(paramInt3 - 2, 2, paramInt3 - 2, paramInt3 - 1);
/* 219 */           if (paramBoolean2)
/* 220 */             paramGraphics.setColor(this.highlight);
/*     */           else {
/* 222 */             paramGraphics.setColor(this.shadow);
/*     */           }
/* 224 */           paramGraphics.drawLine(1, 2, 1, paramInt3 - 2);
/* 225 */           paramGraphics.drawLine(1, 1, paramInt3 - 3, 1);
/* 226 */           if (paramBoolean2)
/* 227 */             paramGraphics.setColor(this.shadow);
/*     */           else {
/* 229 */             paramGraphics.setColor(this.highlight);
/*     */           }
/*     */         }
/*     */ 
/* 233 */         paramGraphics.drawLine(paramInt3 - 1, 1, paramInt3 - 1, paramInt3 - 1);
/*     */ 
/* 236 */         if (paramBoolean2)
/* 237 */           paramGraphics.setColor(this.highlight);
/*     */         else {
/* 239 */           paramGraphics.setColor(this.shadow);
/*     */         }
/* 241 */         paramGraphics.drawLine(0, 1, 0, paramInt3 - 1);
/*     */ 
/* 244 */         paramGraphics.drawLine(0, 0, paramInt3 - 1, 0);
/*     */       }
/*     */ 
/* 247 */       if (paramBoolean3)
/*     */       {
/* 249 */         paramGraphics.setColor(this.foreground);
/* 250 */         paramGraphics.drawLine(paramInt3 - 2, 1, paramInt3 - 2, 2);
/* 251 */         paramGraphics.drawLine(paramInt3 - 3, 2, paramInt3 - 3, 3);
/* 252 */         paramGraphics.drawLine(paramInt3 - 4, 3, paramInt3 - 4, 4);
/* 253 */         paramGraphics.drawLine(paramInt3 - 5, 4, paramInt3 - 5, 6);
/* 254 */         paramGraphics.drawLine(paramInt3 - 6, 5, paramInt3 - 6, 8);
/* 255 */         paramGraphics.drawLine(paramInt3 - 7, 6, paramInt3 - 7, 10);
/* 256 */         paramGraphics.drawLine(paramInt3 - 8, 7, paramInt3 - 8, 10);
/* 257 */         paramGraphics.drawLine(paramInt3 - 9, 6, paramInt3 - 9, 9);
/* 258 */         paramGraphics.drawLine(paramInt3 - 10, 5, paramInt3 - 10, 8);
/* 259 */         paramGraphics.drawLine(paramInt3 - 11, 5, paramInt3 - 11, 7);
/* 260 */         paramGraphics.drawLine(paramInt3 - 12, 6, paramInt3 - 12, 6);
/*     */       }
/* 262 */       paramGraphics.translate(-paramInt1, -paramInt2);
/* 263 */       paramGraphics.setColor(localColor);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class MenuArrowIcon
/*     */     implements Icon, UIResource, Serializable
/*     */   {
/* 379 */     private Color focus = UIManager.getColor("windowBorder");
/* 380 */     private Color shadow = UIManager.getColor("controlShadow");
/* 381 */     private Color highlight = UIManager.getColor("controlHighlight");
/*     */ 
/*     */     public void paintIcon(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2) {
/* 384 */       AbstractButton localAbstractButton = (AbstractButton)paramComponent;
/* 385 */       ButtonModel localButtonModel = localAbstractButton.getModel();
/*     */ 
/* 389 */       int i = getIconWidth();
/* 390 */       int j = getIconHeight();
/*     */ 
/* 392 */       Color localColor = paramGraphics.getColor();
/*     */ 
/* 394 */       if (localButtonModel.isSelected()) {
/* 395 */         if (MotifGraphicsUtils.isLeftToRight(paramComponent)) {
/* 396 */           paramGraphics.setColor(this.shadow);
/* 397 */           paramGraphics.fillRect(paramInt1 + 1, paramInt2 + 1, 2, j);
/* 398 */           paramGraphics.drawLine(paramInt1 + 4, paramInt2 + 2, paramInt1 + 4, paramInt2 + 2);
/* 399 */           paramGraphics.drawLine(paramInt1 + 6, paramInt2 + 3, paramInt1 + 6, paramInt2 + 3);
/* 400 */           paramGraphics.drawLine(paramInt1 + 8, paramInt2 + 4, paramInt1 + 8, paramInt2 + 5);
/* 401 */           paramGraphics.setColor(this.focus);
/* 402 */           paramGraphics.fillRect(paramInt1 + 2, paramInt2 + 2, 2, j - 2);
/* 403 */           paramGraphics.fillRect(paramInt1 + 4, paramInt2 + 3, 2, j - 4);
/* 404 */           paramGraphics.fillRect(paramInt1 + 6, paramInt2 + 4, 2, j - 6);
/* 405 */           paramGraphics.setColor(this.highlight);
/* 406 */           paramGraphics.drawLine(paramInt1 + 2, paramInt2 + j, paramInt1 + 2, paramInt2 + j);
/* 407 */           paramGraphics.drawLine(paramInt1 + 4, paramInt2 + j - 1, paramInt1 + 4, paramInt2 + j - 1);
/* 408 */           paramGraphics.drawLine(paramInt1 + 6, paramInt2 + j - 2, paramInt1 + 6, paramInt2 + j - 2);
/* 409 */           paramGraphics.drawLine(paramInt1 + 8, paramInt2 + j - 4, paramInt1 + 8, paramInt2 + j - 3);
/*     */         } else {
/* 411 */           paramGraphics.setColor(this.highlight);
/* 412 */           paramGraphics.fillRect(paramInt1 + 7, paramInt2 + 1, 2, 10);
/* 413 */           paramGraphics.drawLine(paramInt1 + 5, paramInt2 + 9, paramInt1 + 5, paramInt2 + 9);
/* 414 */           paramGraphics.drawLine(paramInt1 + 3, paramInt2 + 8, paramInt1 + 3, paramInt2 + 8);
/* 415 */           paramGraphics.drawLine(paramInt1 + 1, paramInt2 + 6, paramInt1 + 1, paramInt2 + 7);
/* 416 */           paramGraphics.setColor(this.focus);
/* 417 */           paramGraphics.fillRect(paramInt1 + 6, paramInt2 + 2, 2, 8);
/* 418 */           paramGraphics.fillRect(paramInt1 + 4, paramInt2 + 3, 2, 6);
/* 419 */           paramGraphics.fillRect(paramInt1 + 2, paramInt2 + 4, 2, 4);
/* 420 */           paramGraphics.setColor(this.shadow);
/* 421 */           paramGraphics.drawLine(paramInt1 + 1, paramInt2 + 4, paramInt1 + 1, paramInt2 + 5);
/* 422 */           paramGraphics.drawLine(paramInt1 + 3, paramInt2 + 3, paramInt1 + 3, paramInt2 + 3);
/* 423 */           paramGraphics.drawLine(paramInt1 + 5, paramInt2 + 2, paramInt1 + 5, paramInt2 + 2);
/* 424 */           paramGraphics.drawLine(paramInt1 + 7, paramInt2 + 1, paramInt1 + 7, paramInt2 + 1);
/*     */         }
/*     */       }
/* 427 */       else if (MotifGraphicsUtils.isLeftToRight(paramComponent)) {
/* 428 */         paramGraphics.setColor(this.highlight);
/* 429 */         paramGraphics.drawLine(paramInt1 + 1, paramInt2 + 1, paramInt1 + 1, paramInt2 + j);
/* 430 */         paramGraphics.drawLine(paramInt1 + 2, paramInt2 + 1, paramInt1 + 2, paramInt2 + j - 2);
/* 431 */         paramGraphics.fillRect(paramInt1 + 3, paramInt2 + 2, 2, 2);
/* 432 */         paramGraphics.fillRect(paramInt1 + 5, paramInt2 + 3, 2, 2);
/* 433 */         paramGraphics.fillRect(paramInt1 + 7, paramInt2 + 4, 2, 2);
/* 434 */         paramGraphics.setColor(this.shadow);
/* 435 */         paramGraphics.drawLine(paramInt1 + 2, paramInt2 + j - 1, paramInt1 + 2, paramInt2 + j);
/* 436 */         paramGraphics.fillRect(paramInt1 + 3, paramInt2 + j - 2, 2, 2);
/* 437 */         paramGraphics.fillRect(paramInt1 + 5, paramInt2 + j - 3, 2, 2);
/* 438 */         paramGraphics.fillRect(paramInt1 + 7, paramInt2 + j - 4, 2, 2);
/* 439 */         paramGraphics.setColor(localColor);
/*     */       } else {
/* 441 */         paramGraphics.setColor(this.highlight);
/* 442 */         paramGraphics.fillRect(paramInt1 + 1, paramInt2 + 4, 2, 2);
/* 443 */         paramGraphics.fillRect(paramInt1 + 3, paramInt2 + 3, 2, 2);
/* 444 */         paramGraphics.fillRect(paramInt1 + 5, paramInt2 + 2, 2, 2);
/* 445 */         paramGraphics.drawLine(paramInt1 + 7, paramInt2 + 1, paramInt1 + 7, paramInt2 + 2);
/* 446 */         paramGraphics.setColor(this.shadow);
/* 447 */         paramGraphics.fillRect(paramInt1 + 1, paramInt2 + j - 4, 2, 2);
/* 448 */         paramGraphics.fillRect(paramInt1 + 3, paramInt2 + j - 3, 2, 2);
/* 449 */         paramGraphics.fillRect(paramInt1 + 5, paramInt2 + j - 2, 2, 2);
/* 450 */         paramGraphics.drawLine(paramInt1 + 7, paramInt2 + 3, paramInt1 + 7, paramInt2 + j);
/* 451 */         paramGraphics.drawLine(paramInt1 + 8, paramInt2 + 1, paramInt1 + 8, paramInt2 + j);
/* 452 */         paramGraphics.setColor(localColor);
/*     */       }
/*     */     }
/*     */ 
/*     */     public int getIconWidth() {
/* 457 */       return 10; } 
/* 458 */     public int getIconHeight() { return 10; }
/*     */ 
/*     */   }
/*     */ 
/*     */   private static class MenuItemArrowIcon
/*     */     implements Icon, UIResource, Serializable
/*     */   {
/*     */     public void paintIcon(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2)
/*     */     {
/*     */     }
/*     */ 
/*     */     public int getIconWidth()
/*     */     {
/* 373 */       return 0; } 
/* 374 */     public int getIconHeight() { return 0; }
/*     */ 
/*     */   }
/*     */ 
/*     */   private static class MenuItemCheckIcon
/*     */     implements Icon, UIResource, Serializable
/*     */   {
/*     */     public void paintIcon(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2)
/*     */     {
/*     */     }
/*     */ 
/*     */     public int getIconWidth()
/*     */     {
/* 363 */       return 0; } 
/* 364 */     public int getIconHeight() { return 0; }
/*     */ 
/*     */   }
/*     */ 
/*     */   private static class RadioButtonIcon
/*     */     implements Icon, UIResource, Serializable
/*     */   {
/* 268 */     private Color dot = UIManager.getColor("activeCaptionBorder");
/* 269 */     private Color highlight = UIManager.getColor("controlHighlight");
/* 270 */     private Color shadow = UIManager.getColor("controlShadow");
/*     */ 
/*     */     public void paintIcon(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2)
/*     */     {
/* 274 */       AbstractButton localAbstractButton = (AbstractButton)paramComponent;
/* 275 */       ButtonModel localButtonModel = localAbstractButton.getModel();
/*     */ 
/* 277 */       int i = getIconWidth();
/* 278 */       int j = getIconHeight();
/*     */ 
/* 280 */       boolean bool1 = localButtonModel.isPressed();
/* 281 */       boolean bool2 = localButtonModel.isArmed();
/* 282 */       boolean bool3 = localButtonModel.isEnabled();
/* 283 */       boolean bool4 = localButtonModel.isSelected();
/*     */ 
/* 285 */       int k = ((bool1) && (!bool2) && (bool4)) || ((bool1) && (bool2) && (!bool4)) || ((!bool1) && (bool2) && (bool4)) || ((!bool1) && (!bool2) && (bool4)) ? 1 : 0;
/*     */ 
/* 299 */       if (k != 0) {
/* 300 */         paramGraphics.setColor(this.shadow);
/* 301 */         paramGraphics.drawLine(paramInt1 + 5, paramInt2 + 0, paramInt1 + 8, paramInt2 + 0);
/* 302 */         paramGraphics.drawLine(paramInt1 + 3, paramInt2 + 1, paramInt1 + 4, paramInt2 + 1);
/* 303 */         paramGraphics.drawLine(paramInt1 + 9, paramInt2 + 1, paramInt1 + 9, paramInt2 + 1);
/* 304 */         paramGraphics.drawLine(paramInt1 + 2, paramInt2 + 2, paramInt1 + 2, paramInt2 + 2);
/* 305 */         paramGraphics.drawLine(paramInt1 + 1, paramInt2 + 3, paramInt1 + 1, paramInt2 + 3);
/* 306 */         paramGraphics.drawLine(paramInt1, paramInt2 + 4, paramInt1, paramInt2 + 9);
/* 307 */         paramGraphics.drawLine(paramInt1 + 1, paramInt2 + 10, paramInt1 + 1, paramInt2 + 10);
/* 308 */         paramGraphics.drawLine(paramInt1 + 2, paramInt2 + 11, paramInt1 + 2, paramInt2 + 11);
/* 309 */         paramGraphics.setColor(this.highlight);
/* 310 */         paramGraphics.drawLine(paramInt1 + 3, paramInt2 + 12, paramInt1 + 4, paramInt2 + 12);
/* 311 */         paramGraphics.drawLine(paramInt1 + 5, paramInt2 + 13, paramInt1 + 8, paramInt2 + 13);
/* 312 */         paramGraphics.drawLine(paramInt1 + 9, paramInt2 + 12, paramInt1 + 10, paramInt2 + 12);
/* 313 */         paramGraphics.drawLine(paramInt1 + 11, paramInt2 + 11, paramInt1 + 11, paramInt2 + 11);
/* 314 */         paramGraphics.drawLine(paramInt1 + 12, paramInt2 + 10, paramInt1 + 12, paramInt2 + 10);
/* 315 */         paramGraphics.drawLine(paramInt1 + 13, paramInt2 + 9, paramInt1 + 13, paramInt2 + 4);
/* 316 */         paramGraphics.drawLine(paramInt1 + 12, paramInt2 + 3, paramInt1 + 12, paramInt2 + 3);
/* 317 */         paramGraphics.drawLine(paramInt1 + 11, paramInt2 + 2, paramInt1 + 11, paramInt2 + 2);
/* 318 */         paramGraphics.drawLine(paramInt1 + 10, paramInt2 + 1, paramInt1 + 10, paramInt2 + 1);
/* 319 */         paramGraphics.setColor(this.dot);
/* 320 */         paramGraphics.fillRect(paramInt1 + 4, paramInt2 + 5, 6, 4);
/* 321 */         paramGraphics.drawLine(paramInt1 + 5, paramInt2 + 4, paramInt1 + 8, paramInt2 + 4);
/* 322 */         paramGraphics.drawLine(paramInt1 + 5, paramInt2 + 9, paramInt1 + 8, paramInt2 + 9);
/*     */       }
/*     */       else {
/* 325 */         paramGraphics.setColor(this.highlight);
/* 326 */         paramGraphics.drawLine(paramInt1 + 5, paramInt2 + 0, paramInt1 + 8, paramInt2 + 0);
/* 327 */         paramGraphics.drawLine(paramInt1 + 3, paramInt2 + 1, paramInt1 + 4, paramInt2 + 1);
/* 328 */         paramGraphics.drawLine(paramInt1 + 9, paramInt2 + 1, paramInt1 + 9, paramInt2 + 1);
/* 329 */         paramGraphics.drawLine(paramInt1 + 2, paramInt2 + 2, paramInt1 + 2, paramInt2 + 2);
/* 330 */         paramGraphics.drawLine(paramInt1 + 1, paramInt2 + 3, paramInt1 + 1, paramInt2 + 3);
/* 331 */         paramGraphics.drawLine(paramInt1, paramInt2 + 4, paramInt1, paramInt2 + 9);
/* 332 */         paramGraphics.drawLine(paramInt1 + 1, paramInt2 + 10, paramInt1 + 1, paramInt2 + 10);
/* 333 */         paramGraphics.drawLine(paramInt1 + 2, paramInt2 + 11, paramInt1 + 2, paramInt2 + 11);
/*     */ 
/* 335 */         paramGraphics.setColor(this.shadow);
/* 336 */         paramGraphics.drawLine(paramInt1 + 3, paramInt2 + 12, paramInt1 + 4, paramInt2 + 12);
/* 337 */         paramGraphics.drawLine(paramInt1 + 5, paramInt2 + 13, paramInt1 + 8, paramInt2 + 13);
/* 338 */         paramGraphics.drawLine(paramInt1 + 9, paramInt2 + 12, paramInt1 + 10, paramInt2 + 12);
/* 339 */         paramGraphics.drawLine(paramInt1 + 11, paramInt2 + 11, paramInt1 + 11, paramInt2 + 11);
/* 340 */         paramGraphics.drawLine(paramInt1 + 12, paramInt2 + 10, paramInt1 + 12, paramInt2 + 10);
/* 341 */         paramGraphics.drawLine(paramInt1 + 13, paramInt2 + 9, paramInt1 + 13, paramInt2 + 4);
/* 342 */         paramGraphics.drawLine(paramInt1 + 12, paramInt2 + 3, paramInt1 + 12, paramInt2 + 3);
/* 343 */         paramGraphics.drawLine(paramInt1 + 11, paramInt2 + 2, paramInt1 + 11, paramInt2 + 2);
/* 344 */         paramGraphics.drawLine(paramInt1 + 10, paramInt2 + 1, paramInt1 + 10, paramInt2 + 1);
/*     */       }
/*     */     }
/*     */ 
/*     */     public int getIconWidth()
/*     */     {
/* 350 */       return 14;
/*     */     }
/*     */ 
/*     */     public int getIconHeight() {
/* 354 */       return 14;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.java.swing.plaf.motif.MotifIconFactory
 * JD-Core Version:    0.6.2
 */