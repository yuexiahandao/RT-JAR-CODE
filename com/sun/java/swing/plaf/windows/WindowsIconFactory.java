/*     */ package com.sun.java.swing.plaf.windows;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Graphics2D;
/*     */ import java.io.Serializable;
/*     */ import javax.swing.AbstractButton;
/*     */ import javax.swing.ButtonModel;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JCheckBox;
/*     */ import javax.swing.JCheckBoxMenuItem;
/*     */ import javax.swing.JInternalFrame;
/*     */ import javax.swing.JMenu;
/*     */ import javax.swing.JMenuItem;
/*     */ import javax.swing.JRadioButtonMenuItem;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.UIDefaults;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.plaf.UIResource;
/*     */ import sun.swing.MenuItemCheckIconFactory;
/*     */ 
/*     */ public class WindowsIconFactory
/*     */   implements Serializable
/*     */ {
/*     */   private static Icon frame_closeIcon;
/*     */   private static Icon frame_iconifyIcon;
/*     */   private static Icon frame_maxIcon;
/*     */   private static Icon frame_minIcon;
/*     */   private static Icon frame_resizeIcon;
/*     */   private static Icon checkBoxIcon;
/*     */   private static Icon radioButtonIcon;
/*     */   private static Icon checkBoxMenuItemIcon;
/*     */   private static Icon radioButtonMenuItemIcon;
/*     */   private static Icon menuItemCheckIcon;
/*     */   private static Icon menuItemArrowIcon;
/*     */   private static Icon menuArrowIcon;
/*     */   private static VistaMenuItemCheckIconFactory menuItemCheckIconFactory;
/*     */ 
/*     */   public static Icon getMenuItemCheckIcon()
/*     */   {
/*  71 */     if (menuItemCheckIcon == null) {
/*  72 */       menuItemCheckIcon = new MenuItemCheckIcon(null);
/*     */     }
/*  74 */     return menuItemCheckIcon;
/*     */   }
/*     */ 
/*     */   public static Icon getMenuItemArrowIcon() {
/*  78 */     if (menuItemArrowIcon == null) {
/*  79 */       menuItemArrowIcon = new MenuItemArrowIcon(null);
/*     */     }
/*  81 */     return menuItemArrowIcon;
/*     */   }
/*     */ 
/*     */   public static Icon getMenuArrowIcon() {
/*  85 */     if (menuArrowIcon == null) {
/*  86 */       menuArrowIcon = new MenuArrowIcon(null);
/*     */     }
/*  88 */     return menuArrowIcon;
/*     */   }
/*     */ 
/*     */   public static Icon getCheckBoxIcon() {
/*  92 */     if (checkBoxIcon == null) {
/*  93 */       checkBoxIcon = new CheckBoxIcon(null);
/*     */     }
/*  95 */     return checkBoxIcon;
/*     */   }
/*     */ 
/*     */   public static Icon getRadioButtonIcon() {
/*  99 */     if (radioButtonIcon == null) {
/* 100 */       radioButtonIcon = new RadioButtonIcon(null);
/*     */     }
/* 102 */     return radioButtonIcon;
/*     */   }
/*     */ 
/*     */   public static Icon getCheckBoxMenuItemIcon() {
/* 106 */     if (checkBoxMenuItemIcon == null) {
/* 107 */       checkBoxMenuItemIcon = new CheckBoxMenuItemIcon(null);
/*     */     }
/* 109 */     return checkBoxMenuItemIcon;
/*     */   }
/*     */ 
/*     */   public static Icon getRadioButtonMenuItemIcon() {
/* 113 */     if (radioButtonMenuItemIcon == null) {
/* 114 */       radioButtonMenuItemIcon = new RadioButtonMenuItemIcon(null);
/*     */     }
/* 116 */     return radioButtonMenuItemIcon;
/*     */   }
/*     */ 
/*     */   static synchronized VistaMenuItemCheckIconFactory getMenuItemCheckIconFactory()
/*     */   {
/* 121 */     if (menuItemCheckIconFactory == null) {
/* 122 */       menuItemCheckIconFactory = new VistaMenuItemCheckIconFactory();
/*     */     }
/*     */ 
/* 125 */     return menuItemCheckIconFactory;
/*     */   }
/*     */ 
/*     */   public static Icon createFrameCloseIcon() {
/* 129 */     if (frame_closeIcon == null) {
/* 130 */       frame_closeIcon = new FrameButtonIcon(TMSchema.Part.WP_CLOSEBUTTON, null);
/*     */     }
/* 132 */     return frame_closeIcon;
/*     */   }
/*     */ 
/*     */   public static Icon createFrameIconifyIcon() {
/* 136 */     if (frame_iconifyIcon == null) {
/* 137 */       frame_iconifyIcon = new FrameButtonIcon(TMSchema.Part.WP_MINBUTTON, null);
/*     */     }
/* 139 */     return frame_iconifyIcon;
/*     */   }
/*     */ 
/*     */   public static Icon createFrameMaximizeIcon() {
/* 143 */     if (frame_maxIcon == null) {
/* 144 */       frame_maxIcon = new FrameButtonIcon(TMSchema.Part.WP_MAXBUTTON, null);
/*     */     }
/* 146 */     return frame_maxIcon;
/*     */   }
/*     */ 
/*     */   public static Icon createFrameMinimizeIcon() {
/* 150 */     if (frame_minIcon == null) {
/* 151 */       frame_minIcon = new FrameButtonIcon(TMSchema.Part.WP_RESTOREBUTTON, null);
/*     */     }
/* 153 */     return frame_minIcon;
/*     */   }
/*     */ 
/*     */   public static Icon createFrameResizeIcon() {
/* 157 */     if (frame_resizeIcon == null)
/* 158 */       frame_resizeIcon = new ResizeIcon(null);
/* 159 */     return frame_resizeIcon;
/*     */   }
/*     */ 
/*     */   private static class CheckBoxIcon
/*     */     implements Icon, Serializable
/*     */   {
/*     */     static final int csize = 13;
/*     */ 
/*     */     public void paintIcon(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2)
/*     */     {
/* 322 */       JCheckBox localJCheckBox = (JCheckBox)paramComponent;
/* 323 */       ButtonModel localButtonModel = localJCheckBox.getModel();
/* 324 */       XPStyle localXPStyle = XPStyle.getXP();
/*     */ 
/* 326 */       if (localXPStyle != null)
/*     */       {
/*     */         TMSchema.State localState;
/* 328 */         if (localButtonModel.isSelected()) {
/* 329 */           localState = TMSchema.State.CHECKEDNORMAL;
/* 330 */           if (!localButtonModel.isEnabled())
/* 331 */             localState = TMSchema.State.CHECKEDDISABLED;
/* 332 */           else if ((localButtonModel.isPressed()) && (localButtonModel.isArmed()))
/* 333 */             localState = TMSchema.State.CHECKEDPRESSED;
/* 334 */           else if (localButtonModel.isRollover())
/* 335 */             localState = TMSchema.State.CHECKEDHOT;
/*     */         }
/*     */         else {
/* 338 */           localState = TMSchema.State.UNCHECKEDNORMAL;
/* 339 */           if (!localButtonModel.isEnabled())
/* 340 */             localState = TMSchema.State.UNCHECKEDDISABLED;
/* 341 */           else if ((localButtonModel.isPressed()) && (localButtonModel.isArmed()))
/* 342 */             localState = TMSchema.State.UNCHECKEDPRESSED;
/* 343 */           else if (localButtonModel.isRollover()) {
/* 344 */             localState = TMSchema.State.UNCHECKEDHOT;
/*     */           }
/*     */         }
/* 347 */         TMSchema.Part localPart = TMSchema.Part.BP_CHECKBOX;
/* 348 */         localXPStyle.getSkin(paramComponent, localPart).paintSkin(paramGraphics, paramInt1, paramInt2, localState);
/*     */       }
/*     */       else {
/* 351 */         if (!localJCheckBox.isBorderPaintedFlat())
/*     */         {
/* 353 */           paramGraphics.setColor(UIManager.getColor("CheckBox.shadow"));
/* 354 */           paramGraphics.drawLine(paramInt1, paramInt2, paramInt1 + 11, paramInt2);
/* 355 */           paramGraphics.drawLine(paramInt1, paramInt2 + 1, paramInt1, paramInt2 + 11);
/*     */ 
/* 358 */           paramGraphics.setColor(UIManager.getColor("CheckBox.highlight"));
/* 359 */           paramGraphics.drawLine(paramInt1 + 12, paramInt2, paramInt1 + 12, paramInt2 + 12);
/* 360 */           paramGraphics.drawLine(paramInt1, paramInt2 + 12, paramInt1 + 11, paramInt2 + 12);
/*     */ 
/* 363 */           paramGraphics.setColor(UIManager.getColor("CheckBox.darkShadow"));
/* 364 */           paramGraphics.drawLine(paramInt1 + 1, paramInt2 + 1, paramInt1 + 10, paramInt2 + 1);
/* 365 */           paramGraphics.drawLine(paramInt1 + 1, paramInt2 + 2, paramInt1 + 1, paramInt2 + 10);
/*     */ 
/* 368 */           paramGraphics.setColor(UIManager.getColor("CheckBox.light"));
/* 369 */           paramGraphics.drawLine(paramInt1 + 1, paramInt2 + 11, paramInt1 + 11, paramInt2 + 11);
/* 370 */           paramGraphics.drawLine(paramInt1 + 11, paramInt2 + 1, paramInt1 + 11, paramInt2 + 10);
/*     */ 
/* 373 */           if (((localButtonModel.isPressed()) && (localButtonModel.isArmed())) || (!localButtonModel.isEnabled()))
/* 374 */             paramGraphics.setColor(UIManager.getColor("CheckBox.background"));
/*     */           else {
/* 376 */             paramGraphics.setColor(UIManager.getColor("CheckBox.interiorBackground"));
/*     */           }
/* 378 */           paramGraphics.fillRect(paramInt1 + 2, paramInt2 + 2, 9, 9);
/*     */         } else {
/* 380 */           paramGraphics.setColor(UIManager.getColor("CheckBox.shadow"));
/* 381 */           paramGraphics.drawRect(paramInt1 + 1, paramInt2 + 1, 10, 10);
/*     */ 
/* 383 */           if (((localButtonModel.isPressed()) && (localButtonModel.isArmed())) || (!localButtonModel.isEnabled()))
/* 384 */             paramGraphics.setColor(UIManager.getColor("CheckBox.background"));
/*     */           else {
/* 386 */             paramGraphics.setColor(UIManager.getColor("CheckBox.interiorBackground"));
/*     */           }
/* 388 */           paramGraphics.fillRect(paramInt1 + 2, paramInt2 + 2, 9, 9);
/*     */         }
/*     */ 
/* 391 */         if (localButtonModel.isEnabled())
/* 392 */           paramGraphics.setColor(UIManager.getColor("CheckBox.foreground"));
/*     */         else {
/* 394 */           paramGraphics.setColor(UIManager.getColor("CheckBox.shadow"));
/*     */         }
/*     */ 
/* 398 */         if (localButtonModel.isSelected()) {
/* 399 */           paramGraphics.drawLine(paramInt1 + 9, paramInt2 + 3, paramInt1 + 9, paramInt2 + 3);
/* 400 */           paramGraphics.drawLine(paramInt1 + 8, paramInt2 + 4, paramInt1 + 9, paramInt2 + 4);
/* 401 */           paramGraphics.drawLine(paramInt1 + 7, paramInt2 + 5, paramInt1 + 9, paramInt2 + 5);
/* 402 */           paramGraphics.drawLine(paramInt1 + 6, paramInt2 + 6, paramInt1 + 8, paramInt2 + 6);
/* 403 */           paramGraphics.drawLine(paramInt1 + 3, paramInt2 + 7, paramInt1 + 7, paramInt2 + 7);
/* 404 */           paramGraphics.drawLine(paramInt1 + 4, paramInt2 + 8, paramInt1 + 6, paramInt2 + 8);
/* 405 */           paramGraphics.drawLine(paramInt1 + 5, paramInt2 + 9, paramInt1 + 5, paramInt2 + 9);
/* 406 */           paramGraphics.drawLine(paramInt1 + 3, paramInt2 + 5, paramInt1 + 3, paramInt2 + 5);
/* 407 */           paramGraphics.drawLine(paramInt1 + 3, paramInt2 + 6, paramInt1 + 4, paramInt2 + 6);
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/*     */     public int getIconWidth() {
/* 413 */       XPStyle localXPStyle = XPStyle.getXP();
/* 414 */       if (localXPStyle != null) {
/* 415 */         return localXPStyle.getSkin(null, TMSchema.Part.BP_CHECKBOX).getWidth();
/*     */       }
/* 417 */       return 13;
/*     */     }
/*     */ 
/*     */     public int getIconHeight()
/*     */     {
/* 422 */       XPStyle localXPStyle = XPStyle.getXP();
/* 423 */       if (localXPStyle != null) {
/* 424 */         return localXPStyle.getSkin(null, TMSchema.Part.BP_CHECKBOX).getHeight();
/*     */       }
/* 426 */       return 13;
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class CheckBoxMenuItemIcon
/*     */     implements Icon, UIResource, Serializable
/*     */   {
/*     */     public void paintIcon(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2)
/*     */     {
/* 548 */       AbstractButton localAbstractButton = (AbstractButton)paramComponent;
/* 549 */       ButtonModel localButtonModel = localAbstractButton.getModel();
/* 550 */       boolean bool = localButtonModel.isSelected();
/* 551 */       if (bool) {
/* 552 */         paramInt2 -= getIconHeight() / 2;
/* 553 */         paramGraphics.drawLine(paramInt1 + 9, paramInt2 + 3, paramInt1 + 9, paramInt2 + 3);
/* 554 */         paramGraphics.drawLine(paramInt1 + 8, paramInt2 + 4, paramInt1 + 9, paramInt2 + 4);
/* 555 */         paramGraphics.drawLine(paramInt1 + 7, paramInt2 + 5, paramInt1 + 9, paramInt2 + 5);
/* 556 */         paramGraphics.drawLine(paramInt1 + 6, paramInt2 + 6, paramInt1 + 8, paramInt2 + 6);
/* 557 */         paramGraphics.drawLine(paramInt1 + 3, paramInt2 + 7, paramInt1 + 7, paramInt2 + 7);
/* 558 */         paramGraphics.drawLine(paramInt1 + 4, paramInt2 + 8, paramInt1 + 6, paramInt2 + 8);
/* 559 */         paramGraphics.drawLine(paramInt1 + 5, paramInt2 + 9, paramInt1 + 5, paramInt2 + 9);
/* 560 */         paramGraphics.drawLine(paramInt1 + 3, paramInt2 + 5, paramInt1 + 3, paramInt2 + 5);
/* 561 */         paramGraphics.drawLine(paramInt1 + 3, paramInt2 + 6, paramInt1 + 4, paramInt2 + 6);
/*     */       }
/*     */     }
/* 564 */     public int getIconWidth() { return 9; } 
/* 565 */     public int getIconHeight() { return 9; }
/*     */ 
/*     */   }
/*     */ 
/*     */   private static class FrameButtonIcon
/*     */     implements Icon, Serializable
/*     */   {
/*     */     private TMSchema.Part part;
/*     */ 
/*     */     private FrameButtonIcon(TMSchema.Part paramPart)
/*     */     {
/* 167 */       this.part = paramPart;
/*     */     }
/*     */ 
/*     */     public void paintIcon(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2) {
/* 171 */       int i = getIconWidth();
/* 172 */       int j = getIconHeight();
/*     */ 
/* 174 */       XPStyle localXPStyle = XPStyle.getXP();
/*     */       int i2;
/* 175 */       if (localXPStyle != null) {
/* 176 */         XPStyle.Skin localSkin = localXPStyle.getSkin(paramComponent, this.part);
/* 177 */         JButton localJButton = (JButton)paramComponent;
/* 178 */         ButtonModel localButtonModel = localJButton.getModel();
/*     */ 
/* 181 */         JInternalFrame localJInternalFrame = (JInternalFrame)SwingUtilities.getAncestorOfClass(JInternalFrame.class, localJButton);
/*     */ 
/* 183 */         i2 = (localJInternalFrame != null) && (localJInternalFrame.isSelected()) ? 1 : 0;
/*     */         TMSchema.State localState;
/* 186 */         if (i2 != 0) {
/* 187 */           if (!localButtonModel.isEnabled())
/* 188 */             localState = TMSchema.State.DISABLED;
/* 189 */           else if ((localButtonModel.isArmed()) && (localButtonModel.isPressed()))
/* 190 */             localState = TMSchema.State.PUSHED;
/* 191 */           else if (localButtonModel.isRollover())
/* 192 */             localState = TMSchema.State.HOT;
/*     */           else {
/* 194 */             localState = TMSchema.State.NORMAL;
/*     */           }
/*     */         }
/* 197 */         else if (!localButtonModel.isEnabled())
/* 198 */           localState = TMSchema.State.INACTIVEDISABLED;
/* 199 */         else if ((localButtonModel.isArmed()) && (localButtonModel.isPressed()))
/* 200 */           localState = TMSchema.State.INACTIVEPUSHED;
/* 201 */         else if (localButtonModel.isRollover())
/* 202 */           localState = TMSchema.State.INACTIVEHOT;
/*     */         else {
/* 204 */           localState = TMSchema.State.INACTIVENORMAL;
/*     */         }
/*     */ 
/* 207 */         localSkin.paintSkin(paramGraphics, 0, 0, i, j, localState);
/*     */       } else {
/* 209 */         paramGraphics.setColor(Color.black);
/* 210 */         int k = i / 12 + 2;
/* 211 */         int m = j / 5;
/* 212 */         int n = j - m * 2 - 1;
/* 213 */         int i1 = i * 3 / 4 - 3;
/* 214 */         i2 = Math.max(j / 8, 2);
/* 215 */         int i3 = Math.max(i / 15, 1);
/* 216 */         if (this.part == TMSchema.Part.WP_CLOSEBUTTON)
/*     */         {
/* 218 */           int i4;
/* 218 */           if (i > 47) i4 = 6;
/* 219 */           else if (i > 37) i4 = 5;
/* 220 */           else if (i > 26) i4 = 4;
/* 221 */           else if (i > 16) i4 = 3;
/* 222 */           else if (i > 12) i4 = 2; else
/* 223 */             i4 = 1;
/* 224 */           m = j / 12 + 2;
/* 225 */           if (i4 == 1) {
/* 226 */             if (i1 % 2 == 1) { k++; i1++; }
/* 227 */             paramGraphics.drawLine(k, m, k + i1 - 2, m + i1 - 2);
/* 228 */             paramGraphics.drawLine(k + i1 - 2, m, k, m + i1 - 2);
/* 229 */           } else if (i4 == 2) {
/* 230 */             if (i1 > 6) { k++; i1--; }
/* 231 */             paramGraphics.drawLine(k, m, k + i1 - 2, m + i1 - 2);
/* 232 */             paramGraphics.drawLine(k + i1 - 2, m, k, m + i1 - 2);
/* 233 */             paramGraphics.drawLine(k + 1, m, k + i1 - 1, m + i1 - 2);
/* 234 */             paramGraphics.drawLine(k + i1 - 1, m, k + 1, m + i1 - 2);
/*     */           } else {
/* 236 */             k += 2; m++; i1 -= 2;
/* 237 */             paramGraphics.drawLine(k, m, k + i1 - 1, m + i1 - 1);
/* 238 */             paramGraphics.drawLine(k + i1 - 1, m, k, m + i1 - 1);
/* 239 */             paramGraphics.drawLine(k + 1, m, k + i1 - 1, m + i1 - 2);
/* 240 */             paramGraphics.drawLine(k + i1 - 2, m, k, m + i1 - 2);
/* 241 */             paramGraphics.drawLine(k, m + 1, k + i1 - 2, m + i1 - 1);
/* 242 */             paramGraphics.drawLine(k + i1 - 1, m + 1, k + 1, m + i1 - 1);
/* 243 */             for (int i5 = 4; i5 <= i4; i5++) {
/* 244 */               paramGraphics.drawLine(k + i5 - 2, m, k + i1 - 1, m + i1 - i5 + 1);
/* 245 */               paramGraphics.drawLine(k, m + i5 - 2, k + i1 - i5 + 1, m + i1 - 1);
/* 246 */               paramGraphics.drawLine(k + i1 - i5 + 1, m, k, m + i1 - i5 + 1);
/* 247 */               paramGraphics.drawLine(k + i1 - 1, m + i5 - 2, k + i5 - 2, m + i1 - 1);
/*     */             }
/*     */           }
/* 250 */         } else if (this.part == TMSchema.Part.WP_MINBUTTON) {
/* 251 */           paramGraphics.fillRect(k, m + n - i2, i1 - i1 / 3, i2);
/* 252 */         } else if (this.part == TMSchema.Part.WP_MAXBUTTON) {
/* 253 */           paramGraphics.fillRect(k, m, i1, i2);
/* 254 */           paramGraphics.fillRect(k, m, i3, n);
/* 255 */           paramGraphics.fillRect(k + i1 - i3, m, i3, n);
/* 256 */           paramGraphics.fillRect(k, m + n - i3, i1, i3);
/* 257 */         } else if (this.part == TMSchema.Part.WP_RESTOREBUTTON) {
/* 258 */           paramGraphics.fillRect(k + i1 / 3, m, i1 - i1 / 3, i2);
/* 259 */           paramGraphics.fillRect(k + i1 / 3, m, i3, n / 3);
/* 260 */           paramGraphics.fillRect(k + i1 - i3, m, i3, n - n / 3);
/* 261 */           paramGraphics.fillRect(k + i1 - i1 / 3, m + n - n / 3 - i3, i1 / 3, i3);
/*     */ 
/* 263 */           paramGraphics.fillRect(k, m + n / 3, i1 - i1 / 3, i2);
/* 264 */           paramGraphics.fillRect(k, m + n / 3, i3, n - n / 3);
/* 265 */           paramGraphics.fillRect(k + i1 - i1 / 3 - i3, m + n / 3, i3, n - n / 3);
/* 266 */           paramGraphics.fillRect(k, m + n - i3, i1 - i1 / 3, i3);
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/*     */     public int getIconWidth()
/*     */     {
/*     */       int i;
/* 273 */       if (XPStyle.getXP() != null)
/*     */       {
/* 277 */         i = UIManager.getInt("InternalFrame.titleButtonHeight") - 2;
/* 278 */         Dimension localDimension = XPStyle.getPartSize(TMSchema.Part.WP_CLOSEBUTTON, TMSchema.State.NORMAL);
/* 279 */         if ((localDimension != null) && (localDimension.width != 0) && (localDimension.height != 0))
/* 280 */           i = (int)(i * localDimension.width / localDimension.height);
/*     */       }
/*     */       else {
/* 283 */         i = UIManager.getInt("InternalFrame.titleButtonWidth") - 2;
/*     */       }
/* 285 */       if (XPStyle.getXP() != null) {
/* 286 */         i -= 2;
/*     */       }
/* 288 */       return i;
/*     */     }
/*     */ 
/*     */     public int getIconHeight() {
/* 292 */       int i = UIManager.getInt("InternalFrame.titleButtonHeight") - 4;
/* 293 */       return i;
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class MenuArrowIcon
/*     */     implements Icon, UIResource, Serializable
/*     */   {
/*     */     public void paintIcon(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2)
/*     */     {
/* 616 */       XPStyle localXPStyle = XPStyle.getXP();
/* 617 */       if (WindowsMenuItemUI.isVistaPainting(localXPStyle)) {
/* 618 */         TMSchema.State localState = TMSchema.State.NORMAL;
/* 619 */         if ((paramComponent instanceof JMenuItem)) {
/* 620 */           localState = ((JMenuItem)paramComponent).getModel().isEnabled() ? TMSchema.State.NORMAL : TMSchema.State.DISABLED;
/*     */         }
/*     */ 
/* 623 */         XPStyle.Skin localSkin = localXPStyle.getSkin(paramComponent, TMSchema.Part.MP_POPUPSUBMENU);
/* 624 */         if (WindowsGraphicsUtils.isLeftToRight(paramComponent)) {
/* 625 */           localSkin.paintSkin(paramGraphics, paramInt1, paramInt2, localState);
/*     */         } else {
/* 627 */           Graphics2D localGraphics2D = (Graphics2D)paramGraphics.create();
/* 628 */           localGraphics2D.translate(paramInt1 + localSkin.getWidth(), paramInt2);
/* 629 */           localGraphics2D.scale(-1.0D, 1.0D);
/* 630 */           localSkin.paintSkin(localGraphics2D, 0, 0, localState);
/* 631 */           localGraphics2D.dispose();
/*     */         }
/*     */       } else {
/* 634 */         paramGraphics.translate(paramInt1, paramInt2);
/* 635 */         if (WindowsGraphicsUtils.isLeftToRight(paramComponent)) {
/* 636 */           paramGraphics.drawLine(0, 0, 0, 7);
/* 637 */           paramGraphics.drawLine(1, 1, 1, 6);
/* 638 */           paramGraphics.drawLine(2, 2, 2, 5);
/* 639 */           paramGraphics.drawLine(3, 3, 3, 4);
/*     */         } else {
/* 641 */           paramGraphics.drawLine(4, 0, 4, 7);
/* 642 */           paramGraphics.drawLine(3, 1, 3, 6);
/* 643 */           paramGraphics.drawLine(2, 2, 2, 5);
/* 644 */           paramGraphics.drawLine(1, 3, 1, 4);
/*     */         }
/* 646 */         paramGraphics.translate(-paramInt1, -paramInt2);
/*     */       }
/*     */     }
/*     */ 
/* 650 */     public int getIconWidth() { XPStyle localXPStyle = XPStyle.getXP();
/* 651 */       if (WindowsMenuItemUI.isVistaPainting(localXPStyle)) {
/* 652 */         XPStyle.Skin localSkin = localXPStyle.getSkin(null, TMSchema.Part.MP_POPUPSUBMENU);
/* 653 */         return localSkin.getWidth();
/*     */       }
/* 655 */       return 4; }
/*     */ 
/*     */     public int getIconHeight()
/*     */     {
/* 659 */       XPStyle localXPStyle = XPStyle.getXP();
/* 660 */       if (WindowsMenuItemUI.isVistaPainting(localXPStyle)) {
/* 661 */         XPStyle.Skin localSkin = localXPStyle.getSkin(null, TMSchema.Part.MP_POPUPSUBMENU);
/* 662 */         return localSkin.getHeight();
/*     */       }
/* 664 */       return 8;
/*     */     }
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
/* 609 */       return 4; } 
/* 610 */     public int getIconHeight() { return 8; }
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
/* 595 */       return 9; } 
/* 596 */     public int getIconHeight() { return 9; }
/*     */ 
/*     */   }
/*     */ 
/*     */   private static class RadioButtonIcon
/*     */     implements Icon, UIResource, Serializable
/*     */   {
/*     */     public void paintIcon(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2)
/*     */     {
/* 434 */       AbstractButton localAbstractButton = (AbstractButton)paramComponent;
/* 435 */       ButtonModel localButtonModel = localAbstractButton.getModel();
/* 436 */       XPStyle localXPStyle = XPStyle.getXP();
/*     */ 
/* 438 */       if (localXPStyle != null) {
/* 439 */         TMSchema.Part localPart = TMSchema.Part.BP_RADIOBUTTON;
/* 440 */         XPStyle.Skin localSkin = localXPStyle.getSkin(localAbstractButton, localPart);
/*     */ 
/* 442 */         int i = 0;
/*     */         TMSchema.State localState;
/* 443 */         if (localButtonModel.isSelected()) {
/* 444 */           localState = TMSchema.State.CHECKEDNORMAL;
/* 445 */           if (!localButtonModel.isEnabled())
/* 446 */             localState = TMSchema.State.CHECKEDDISABLED;
/* 447 */           else if ((localButtonModel.isPressed()) && (localButtonModel.isArmed()))
/* 448 */             localState = TMSchema.State.CHECKEDPRESSED;
/* 449 */           else if (localButtonModel.isRollover())
/* 450 */             localState = TMSchema.State.CHECKEDHOT;
/*     */         }
/*     */         else {
/* 453 */           localState = TMSchema.State.UNCHECKEDNORMAL;
/* 454 */           if (!localButtonModel.isEnabled())
/* 455 */             localState = TMSchema.State.UNCHECKEDDISABLED;
/* 456 */           else if ((localButtonModel.isPressed()) && (localButtonModel.isArmed()))
/* 457 */             localState = TMSchema.State.UNCHECKEDPRESSED;
/* 458 */           else if (localButtonModel.isRollover()) {
/* 459 */             localState = TMSchema.State.UNCHECKEDHOT;
/*     */           }
/*     */         }
/* 462 */         localSkin.paintSkin(paramGraphics, paramInt1, paramInt2, localState);
/*     */       }
/*     */       else {
/* 465 */         if (((localButtonModel.isPressed()) && (localButtonModel.isArmed())) || (!localButtonModel.isEnabled()))
/* 466 */           paramGraphics.setColor(UIManager.getColor("RadioButton.background"));
/*     */         else {
/* 468 */           paramGraphics.setColor(UIManager.getColor("RadioButton.interiorBackground"));
/*     */         }
/* 470 */         paramGraphics.fillRect(paramInt1 + 2, paramInt2 + 2, 8, 8);
/*     */ 
/* 474 */         paramGraphics.setColor(UIManager.getColor("RadioButton.shadow"));
/* 475 */         paramGraphics.drawLine(paramInt1 + 4, paramInt2 + 0, paramInt1 + 7, paramInt2 + 0);
/* 476 */         paramGraphics.drawLine(paramInt1 + 2, paramInt2 + 1, paramInt1 + 3, paramInt2 + 1);
/* 477 */         paramGraphics.drawLine(paramInt1 + 8, paramInt2 + 1, paramInt1 + 9, paramInt2 + 1);
/* 478 */         paramGraphics.drawLine(paramInt1 + 1, paramInt2 + 2, paramInt1 + 1, paramInt2 + 3);
/* 479 */         paramGraphics.drawLine(paramInt1 + 0, paramInt2 + 4, paramInt1 + 0, paramInt2 + 7);
/* 480 */         paramGraphics.drawLine(paramInt1 + 1, paramInt2 + 8, paramInt1 + 1, paramInt2 + 9);
/*     */ 
/* 483 */         paramGraphics.setColor(UIManager.getColor("RadioButton.highlight"));
/* 484 */         paramGraphics.drawLine(paramInt1 + 2, paramInt2 + 10, paramInt1 + 3, paramInt2 + 10);
/* 485 */         paramGraphics.drawLine(paramInt1 + 4, paramInt2 + 11, paramInt1 + 7, paramInt2 + 11);
/* 486 */         paramGraphics.drawLine(paramInt1 + 8, paramInt2 + 10, paramInt1 + 9, paramInt2 + 10);
/* 487 */         paramGraphics.drawLine(paramInt1 + 10, paramInt2 + 9, paramInt1 + 10, paramInt2 + 8);
/* 488 */         paramGraphics.drawLine(paramInt1 + 11, paramInt2 + 7, paramInt1 + 11, paramInt2 + 4);
/* 489 */         paramGraphics.drawLine(paramInt1 + 10, paramInt2 + 3, paramInt1 + 10, paramInt2 + 2);
/*     */ 
/* 493 */         paramGraphics.setColor(UIManager.getColor("RadioButton.darkShadow"));
/* 494 */         paramGraphics.drawLine(paramInt1 + 4, paramInt2 + 1, paramInt1 + 7, paramInt2 + 1);
/* 495 */         paramGraphics.drawLine(paramInt1 + 2, paramInt2 + 2, paramInt1 + 3, paramInt2 + 2);
/* 496 */         paramGraphics.drawLine(paramInt1 + 8, paramInt2 + 2, paramInt1 + 9, paramInt2 + 2);
/* 497 */         paramGraphics.drawLine(paramInt1 + 2, paramInt2 + 3, paramInt1 + 2, paramInt2 + 3);
/* 498 */         paramGraphics.drawLine(paramInt1 + 1, paramInt2 + 4, paramInt1 + 1, paramInt2 + 7);
/* 499 */         paramGraphics.drawLine(paramInt1 + 2, paramInt2 + 8, paramInt1 + 2, paramInt2 + 8);
/*     */ 
/* 503 */         paramGraphics.setColor(UIManager.getColor("RadioButton.light"));
/* 504 */         paramGraphics.drawLine(paramInt1 + 2, paramInt2 + 9, paramInt1 + 3, paramInt2 + 9);
/* 505 */         paramGraphics.drawLine(paramInt1 + 4, paramInt2 + 10, paramInt1 + 7, paramInt2 + 10);
/* 506 */         paramGraphics.drawLine(paramInt1 + 8, paramInt2 + 9, paramInt1 + 9, paramInt2 + 9);
/* 507 */         paramGraphics.drawLine(paramInt1 + 9, paramInt2 + 8, paramInt1 + 9, paramInt2 + 8);
/* 508 */         paramGraphics.drawLine(paramInt1 + 10, paramInt2 + 7, paramInt1 + 10, paramInt2 + 4);
/* 509 */         paramGraphics.drawLine(paramInt1 + 9, paramInt2 + 3, paramInt1 + 9, paramInt2 + 3);
/*     */ 
/* 513 */         if (localButtonModel.isSelected()) {
/* 514 */           if (localButtonModel.isEnabled())
/* 515 */             paramGraphics.setColor(UIManager.getColor("RadioButton.foreground"));
/*     */           else {
/* 517 */             paramGraphics.setColor(UIManager.getColor("RadioButton.shadow"));
/*     */           }
/* 519 */           paramGraphics.fillRect(paramInt1 + 4, paramInt2 + 5, 4, 2);
/* 520 */           paramGraphics.fillRect(paramInt1 + 5, paramInt2 + 4, 2, 4);
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/*     */     public int getIconWidth() {
/* 526 */       XPStyle localXPStyle = XPStyle.getXP();
/* 527 */       if (localXPStyle != null) {
/* 528 */         return localXPStyle.getSkin(null, TMSchema.Part.BP_RADIOBUTTON).getWidth();
/*     */       }
/* 530 */       return 13;
/*     */     }
/*     */ 
/*     */     public int getIconHeight()
/*     */     {
/* 535 */       XPStyle localXPStyle = XPStyle.getXP();
/* 536 */       if (localXPStyle != null) {
/* 537 */         return localXPStyle.getSkin(null, TMSchema.Part.BP_RADIOBUTTON).getHeight();
/*     */       }
/* 539 */       return 13;
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class RadioButtonMenuItemIcon
/*     */     implements Icon, UIResource, Serializable
/*     */   {
/*     */     public void paintIcon(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2)
/*     */     {
/* 573 */       AbstractButton localAbstractButton = (AbstractButton)paramComponent;
/* 574 */       ButtonModel localButtonModel = localAbstractButton.getModel();
/* 575 */       if (localAbstractButton.isSelected() == true)
/* 576 */         paramGraphics.fillRoundRect(paramInt1 + 3, paramInt2 + 3, getIconWidth() - 6, getIconHeight() - 6, 4, 4);
/*     */     }
/*     */ 
/*     */     public int getIconWidth() {
/* 580 */       return 12; } 
/* 581 */     public int getIconHeight() { return 12; }
/*     */ 
/*     */   }
/*     */ 
/*     */   private static class ResizeIcon
/*     */     implements Icon, Serializable
/*     */   {
/*     */     public void paintIcon(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2)
/*     */     {
/* 301 */       paramGraphics.setColor(UIManager.getColor("InternalFrame.resizeIconHighlight"));
/* 302 */       paramGraphics.drawLine(0, 11, 11, 0);
/* 303 */       paramGraphics.drawLine(4, 11, 11, 4);
/* 304 */       paramGraphics.drawLine(8, 11, 11, 8);
/*     */ 
/* 306 */       paramGraphics.setColor(UIManager.getColor("InternalFrame.resizeIconShadow"));
/* 307 */       paramGraphics.drawLine(1, 11, 11, 1);
/* 308 */       paramGraphics.drawLine(2, 11, 11, 2);
/* 309 */       paramGraphics.drawLine(5, 11, 11, 5);
/* 310 */       paramGraphics.drawLine(6, 11, 11, 6);
/* 311 */       paramGraphics.drawLine(9, 11, 11, 9);
/* 312 */       paramGraphics.drawLine(10, 11, 11, 10);
/*     */     }
/* 314 */     public int getIconWidth() { return 13; } 
/* 315 */     public int getIconHeight() { return 13; }
/*     */ 
/*     */   }
/*     */ 
/*     */   static class VistaMenuItemCheckIconFactory
/*     */     implements MenuItemCheckIconFactory
/*     */   {
/*     */     private static final int OFFSET = 3;
/*     */ 
/*     */     public Icon getIcon(JMenuItem paramJMenuItem)
/*     */     {
/* 674 */       return new VistaMenuItemCheckIcon(paramJMenuItem);
/*     */     }
/*     */ 
/*     */     public boolean isCompatible(Object paramObject, String paramString) {
/* 678 */       return ((paramObject instanceof VistaMenuItemCheckIcon)) && (((VistaMenuItemCheckIcon)paramObject).type == getType(paramString));
/*     */     }
/*     */ 
/*     */     public Icon getIcon(String paramString)
/*     */     {
/* 683 */       return new VistaMenuItemCheckIcon(paramString);
/*     */     }
/*     */ 
/*     */     static int getIconWidth() {
/* 687 */       XPStyle localXPStyle = XPStyle.getXP();
/* 688 */       return (localXPStyle != null ? localXPStyle.getSkin(null, TMSchema.Part.MP_POPUPCHECK).getWidth() : 16) + 6;
/*     */     }
/*     */ 
/*     */     private static Class<? extends JMenuItem> getType(Component paramComponent)
/*     */     {
/* 693 */       Object localObject = null;
/* 694 */       if ((paramComponent instanceof JCheckBoxMenuItem))
/* 695 */         localObject = JCheckBoxMenuItem.class;
/* 696 */       else if ((paramComponent instanceof JRadioButtonMenuItem))
/* 697 */         localObject = JRadioButtonMenuItem.class;
/* 698 */       else if ((paramComponent instanceof JMenu))
/* 699 */         localObject = JMenu.class;
/* 700 */       else if ((paramComponent instanceof JMenuItem)) {
/* 701 */         localObject = JMenuItem.class;
/*     */       }
/* 703 */       return localObject;
/*     */     }
/*     */ 
/*     */     private static Class<? extends JMenuItem> getType(String paramString) {
/* 707 */       Object localObject = null;
/* 708 */       if (paramString == "CheckBoxMenuItem")
/* 709 */         localObject = JCheckBoxMenuItem.class;
/* 710 */       else if (paramString == "RadioButtonMenuItem")
/* 711 */         localObject = JRadioButtonMenuItem.class;
/* 712 */       else if (paramString == "Menu")
/* 713 */         localObject = JMenu.class;
/* 714 */       else if (paramString == "MenuItem") {
/* 715 */         localObject = JMenuItem.class;
/*     */       }
/*     */       else {
/* 718 */         localObject = JMenuItem.class;
/*     */       }
/* 720 */       return localObject;
/*     */     }
/*     */ 
/*     */     private static class VistaMenuItemCheckIcon
/*     */       implements Icon, UIResource, Serializable
/*     */     {
/*     */       private final JMenuItem menuItem;
/*     */       private final Class<? extends JMenuItem> type;
/*     */ 
/*     */       VistaMenuItemCheckIcon(JMenuItem paramJMenuItem)
/*     */       {
/* 735 */         this.type = WindowsIconFactory.VistaMenuItemCheckIconFactory.getType(paramJMenuItem);
/* 736 */         this.menuItem = paramJMenuItem;
/*     */       }
/*     */       VistaMenuItemCheckIcon(String paramString) {
/* 739 */         this.type = WindowsIconFactory.VistaMenuItemCheckIconFactory.getType(paramString);
/* 740 */         this.menuItem = null;
/*     */       }
/*     */ 
/*     */       public int getIconHeight() {
/* 744 */         Icon localIcon1 = getLaFIcon();
/* 745 */         if (localIcon1 != null) {
/* 746 */           return localIcon1.getIconHeight();
/*     */         }
/* 748 */         Icon localIcon2 = getIcon();
/* 749 */         int i = 0;
/* 750 */         if (localIcon2 != null) {
/* 751 */           i = localIcon2.getIconHeight();
/*     */         } else {
/* 753 */           XPStyle localXPStyle = XPStyle.getXP();
/* 754 */           if (localXPStyle != null) {
/* 755 */             XPStyle.Skin localSkin = localXPStyle.getSkin(null, TMSchema.Part.MP_POPUPCHECK);
/* 756 */             i = localSkin.getHeight();
/*     */           } else {
/* 758 */             i = 16;
/*     */           }
/*     */         }
/* 761 */         i += 6;
/* 762 */         return i;
/*     */       }
/*     */ 
/*     */       public int getIconWidth() {
/* 766 */         Icon localIcon1 = getLaFIcon();
/* 767 */         if (localIcon1 != null) {
/* 768 */           return localIcon1.getIconWidth();
/*     */         }
/* 770 */         Icon localIcon2 = getIcon();
/* 771 */         int i = 0;
/* 772 */         if (localIcon2 != null)
/* 773 */           i = localIcon2.getIconWidth() + 6;
/*     */         else {
/* 775 */           i = WindowsIconFactory.VistaMenuItemCheckIconFactory.getIconWidth();
/*     */         }
/* 777 */         return i;
/*     */       }
/*     */ 
/*     */       public void paintIcon(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2) {
/* 781 */         Icon localIcon1 = getLaFIcon();
/* 782 */         if (localIcon1 != null) {
/* 783 */           localIcon1.paintIcon(paramComponent, paramGraphics, paramInt1, paramInt2);
/* 784 */           return;
/*     */         }
/* 786 */         assert ((this.menuItem == null) || (paramComponent == this.menuItem));
/* 787 */         Icon localIcon2 = getIcon();
/* 788 */         if ((this.type == JCheckBoxMenuItem.class) || (this.type == JRadioButtonMenuItem.class))
/*     */         {
/* 790 */           AbstractButton localAbstractButton = (AbstractButton)paramComponent;
/* 791 */           if (localAbstractButton.isSelected()) {
/* 792 */             TMSchema.Part localPart1 = TMSchema.Part.MP_POPUPCHECKBACKGROUND;
/* 793 */             TMSchema.Part localPart2 = TMSchema.Part.MP_POPUPCHECK;
/*     */             TMSchema.State localState1;
/*     */             TMSchema.State localState2;
/* 796 */             if (isEnabled(paramComponent, null)) {
/* 797 */               localState1 = localIcon2 != null ? TMSchema.State.BITMAP : TMSchema.State.NORMAL;
/*     */ 
/* 799 */               localState2 = this.type == JRadioButtonMenuItem.class ? TMSchema.State.BULLETNORMAL : TMSchema.State.CHECKMARKNORMAL;
/*     */             }
/*     */             else
/*     */             {
/* 803 */               localState1 = TMSchema.State.DISABLEDPUSHED;
/* 804 */               localState2 = this.type == JRadioButtonMenuItem.class ? TMSchema.State.BULLETDISABLED : TMSchema.State.CHECKMARKDISABLED;
/*     */             }
/*     */ 
/* 809 */             XPStyle localXPStyle = XPStyle.getXP();
/* 810 */             if (localXPStyle != null)
/*     */             {
/* 812 */               XPStyle.Skin localSkin = localXPStyle.getSkin(paramComponent, localPart1);
/* 813 */               localSkin.paintSkin(paramGraphics, paramInt1, paramInt2, getIconWidth(), getIconHeight(), localState1);
/*     */ 
/* 815 */               if (localIcon2 == null) {
/* 816 */                 localSkin = localXPStyle.getSkin(paramComponent, localPart2);
/* 817 */                 localSkin.paintSkin(paramGraphics, paramInt1 + 3, paramInt2 + 3, localState2);
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/* 822 */         if (localIcon2 != null)
/* 823 */           localIcon2.paintIcon(paramComponent, paramGraphics, paramInt1 + 3, paramInt2 + 3);
/*     */       }
/*     */ 
/*     */       private static WindowsMenuItemUIAccessor getAccessor(JMenuItem paramJMenuItem)
/*     */       {
/* 828 */         WindowsMenuItemUIAccessor localWindowsMenuItemUIAccessor = null;
/* 829 */         Object localObject = paramJMenuItem != null ? paramJMenuItem.getUI() : null;
/*     */ 
/* 831 */         if ((localObject instanceof WindowsMenuItemUI))
/* 832 */           localWindowsMenuItemUIAccessor = ((WindowsMenuItemUI)localObject).accessor;
/* 833 */         else if ((localObject instanceof WindowsMenuUI))
/* 834 */           localWindowsMenuItemUIAccessor = ((WindowsMenuUI)localObject).accessor;
/* 835 */         else if ((localObject instanceof WindowsCheckBoxMenuItemUI))
/* 836 */           localWindowsMenuItemUIAccessor = ((WindowsCheckBoxMenuItemUI)localObject).accessor;
/* 837 */         else if ((localObject instanceof WindowsRadioButtonMenuItemUI)) {
/* 838 */           localWindowsMenuItemUIAccessor = ((WindowsRadioButtonMenuItemUI)localObject).accessor;
/*     */         }
/* 840 */         return localWindowsMenuItemUIAccessor;
/*     */       }
/*     */ 
/*     */       private static boolean isEnabled(Component paramComponent, TMSchema.State paramState) {
/* 844 */         if ((paramState == null) && ((paramComponent instanceof JMenuItem))) {
/* 845 */           WindowsMenuItemUIAccessor localWindowsMenuItemUIAccessor = getAccessor((JMenuItem)paramComponent);
/*     */ 
/* 847 */           if (localWindowsMenuItemUIAccessor != null) {
/* 848 */             paramState = localWindowsMenuItemUIAccessor.getState((JMenuItem)paramComponent);
/*     */           }
/*     */         }
/* 851 */         if (paramState == null) {
/* 852 */           if (paramComponent != null) {
/* 853 */             return paramComponent.isEnabled();
/*     */           }
/* 855 */           return true;
/*     */         }
/*     */ 
/* 858 */         return (paramState != TMSchema.State.DISABLED) && (paramState != TMSchema.State.DISABLEDHOT) && (paramState != TMSchema.State.DISABLEDPUSHED);
/*     */       }
/*     */ 
/*     */       private Icon getIcon()
/*     */       {
/* 864 */         Icon localIcon = null;
/* 865 */         if (this.menuItem == null) {
/* 866 */           return localIcon;
/*     */         }
/* 868 */         WindowsMenuItemUIAccessor localWindowsMenuItemUIAccessor = getAccessor(this.menuItem);
/*     */ 
/* 870 */         Object localObject = localWindowsMenuItemUIAccessor != null ? localWindowsMenuItemUIAccessor.getState(this.menuItem) : null;
/*     */ 
/* 872 */         if (isEnabled(this.menuItem, null)) {
/* 873 */           if (localObject == TMSchema.State.PUSHED)
/* 874 */             localIcon = this.menuItem.getPressedIcon();
/*     */           else
/* 876 */             localIcon = this.menuItem.getIcon();
/*     */         }
/*     */         else {
/* 879 */           localIcon = this.menuItem.getDisabledIcon();
/*     */         }
/* 881 */         return localIcon;
/*     */       }
/*     */ 
/*     */       private Icon getLaFIcon()
/*     */       {
/* 891 */         Icon localIcon = (Serializable)UIManager.getDefaults().get(typeToString(this.type));
/* 892 */         if (((localIcon instanceof VistaMenuItemCheckIcon)) && (((VistaMenuItemCheckIcon)localIcon).type == this.type))
/*     */         {
/* 894 */           localIcon = null;
/*     */         }
/* 896 */         return localIcon;
/*     */       }
/*     */ 
/*     */       private static String typeToString(Class<? extends JMenuItem> paramClass)
/*     */       {
/* 901 */         assert ((paramClass == JMenuItem.class) || (paramClass == JMenu.class) || (paramClass == JCheckBoxMenuItem.class) || (paramClass == JRadioButtonMenuItem.class));
/*     */ 
/* 905 */         StringBuilder localStringBuilder = new StringBuilder(paramClass.getName());
/*     */ 
/* 907 */         localStringBuilder.delete(0, localStringBuilder.lastIndexOf("J") + 1);
/* 908 */         localStringBuilder.append(".checkIcon");
/* 909 */         return localStringBuilder.toString();
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.java.swing.plaf.windows.WindowsIconFactory
 * JD-Core Version:    0.6.2
 */