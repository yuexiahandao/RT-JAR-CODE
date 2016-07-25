/*     */ package com.sun.java.swing.plaf.windows;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.event.MouseEvent;
/*     */ import javax.swing.ButtonModel;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JMenu;
/*     */ import javax.swing.JMenuBar;
/*     */ import javax.swing.JMenuItem;
/*     */ import javax.swing.MenuElement;
/*     */ import javax.swing.UIDefaults;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.event.MouseInputListener;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.basic.BasicMenuUI;
/*     */ import javax.swing.plaf.basic.BasicMenuUI.MouseInputHandler;
/*     */ 
/*     */ public class WindowsMenuUI extends BasicMenuUI
/*     */ {
/*     */   protected Integer menuBarHeight;
/*     */   protected boolean hotTrackingOn;
/*     */   final WindowsMenuItemUIAccessor accessor;
/*     */ 
/*     */   public WindowsMenuUI()
/*     */   {
/*  53 */     this.accessor = new WindowsMenuItemUIAccessor()
/*     */     {
/*     */       public JMenuItem getMenuItem()
/*     */       {
/*  57 */         return WindowsMenuUI.this.menuItem;
/*     */       }
/*     */ 
/*     */       public TMSchema.State getState(JMenuItem paramAnonymousJMenuItem) {
/*  61 */         Object localObject1 = paramAnonymousJMenuItem.isEnabled() ? TMSchema.State.NORMAL : TMSchema.State.DISABLED;
/*     */ 
/*  63 */         ButtonModel localButtonModel = paramAnonymousJMenuItem.getModel();
/*  64 */         if ((localButtonModel.isArmed()) || (localButtonModel.isSelected())) {
/*  65 */           localObject1 = paramAnonymousJMenuItem.isEnabled() ? TMSchema.State.PUSHED : TMSchema.State.DISABLEDPUSHED;
/*     */         }
/*  67 */         else if ((localButtonModel.isRollover()) && (((JMenu)paramAnonymousJMenuItem).isTopLevelMenu()))
/*     */         {
/*  73 */           Object localObject2 = localObject1;
/*  74 */           localObject1 = paramAnonymousJMenuItem.isEnabled() ? TMSchema.State.HOT : TMSchema.State.DISABLEDHOT;
/*     */ 
/*  77 */           for (MenuElement localMenuElement : ((JMenuBar)paramAnonymousJMenuItem.getParent()).getSubElements()) {
/*  78 */             if (((JMenuItem)localMenuElement).isSelected()) {
/*  79 */               localObject1 = localObject2;
/*  80 */               break;
/*     */             }
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/*  86 */         if (!((JMenu)paramAnonymousJMenuItem).isTopLevelMenu()) {
/*  87 */           if (localObject1 == TMSchema.State.PUSHED)
/*  88 */             localObject1 = TMSchema.State.HOT;
/*  89 */           else if (localObject1 == TMSchema.State.DISABLEDPUSHED) {
/*  90 */             localObject1 = TMSchema.State.DISABLEDHOT;
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/*  97 */         if ((((JMenu)paramAnonymousJMenuItem).isTopLevelMenu()) && (WindowsMenuItemUI.isVistaPainting()) && 
/*  98 */           (!WindowsMenuBarUI.isActive(paramAnonymousJMenuItem))) {
/*  99 */           localObject1 = TMSchema.State.DISABLED;
/*     */         }
/*     */ 
/* 102 */         return localObject1;
/*     */       }
/*     */ 
/*     */       public TMSchema.Part getPart(JMenuItem paramAnonymousJMenuItem) {
/* 106 */         return ((JMenu)paramAnonymousJMenuItem).isTopLevelMenu() ? TMSchema.Part.MP_BARITEM : TMSchema.Part.MP_POPUPITEM;
/*     */       } } ;
/*     */   }
/*     */ 
/*     */   public static ComponentUI createUI(JComponent paramJComponent) {
/* 111 */     return new WindowsMenuUI();
/*     */   }
/*     */ 
/*     */   protected void installDefaults() {
/* 115 */     super.installDefaults();
/* 116 */     if (!WindowsLookAndFeel.isClassicWindows()) {
/* 117 */       this.menuItem.setRolloverEnabled(true);
/*     */     }
/*     */ 
/* 120 */     this.menuBarHeight = Integer.valueOf(UIManager.getInt("MenuBar.height"));
/*     */ 
/* 122 */     Object localObject = UIManager.get("MenuBar.rolloverEnabled");
/* 123 */     this.hotTrackingOn = ((localObject instanceof Boolean) ? ((Boolean)localObject).booleanValue() : true);
/*     */   }
/*     */ 
/*     */   protected void paintBackground(Graphics paramGraphics, JMenuItem paramJMenuItem, Color paramColor)
/*     */   {
/* 131 */     if (WindowsMenuItemUI.isVistaPainting()) {
/* 132 */       WindowsMenuItemUI.paintBackground(this.accessor, paramGraphics, paramJMenuItem, paramColor);
/* 133 */       return;
/*     */     }
/*     */ 
/* 136 */     JMenu localJMenu = (JMenu)paramJMenuItem;
/* 137 */     ButtonModel localButtonModel = localJMenu.getModel();
/*     */ 
/* 141 */     if ((WindowsLookAndFeel.isClassicWindows()) || (!localJMenu.isTopLevelMenu()) || ((XPStyle.getXP() != null) && ((localButtonModel.isArmed()) || (localButtonModel.isSelected()))))
/*     */     {
/* 145 */       super.paintBackground(paramGraphics, localJMenu, paramColor);
/* 146 */       return;
/*     */     }
/*     */ 
/* 149 */     Color localColor1 = paramGraphics.getColor();
/* 150 */     int i = localJMenu.getWidth();
/* 151 */     int j = localJMenu.getHeight();
/*     */ 
/* 153 */     UIDefaults localUIDefaults = UIManager.getLookAndFeelDefaults();
/* 154 */     Color localColor2 = localUIDefaults.getColor("controlLtHighlight");
/* 155 */     Color localColor3 = localUIDefaults.getColor("controlShadow");
/*     */ 
/* 157 */     paramGraphics.setColor(localJMenu.getBackground());
/* 158 */     paramGraphics.fillRect(0, 0, i, j);
/*     */ 
/* 160 */     if (localJMenu.isOpaque()) {
/* 161 */       if ((localButtonModel.isArmed()) || (localButtonModel.isSelected()))
/*     */       {
/* 163 */         paramGraphics.setColor(localColor3);
/* 164 */         paramGraphics.drawLine(0, 0, i - 1, 0);
/* 165 */         paramGraphics.drawLine(0, 0, 0, j - 2);
/*     */ 
/* 167 */         paramGraphics.setColor(localColor2);
/* 168 */         paramGraphics.drawLine(i - 1, 0, i - 1, j - 2);
/* 169 */         paramGraphics.drawLine(0, j - 2, i - 1, j - 2);
/* 170 */       } else if ((localButtonModel.isRollover()) && (localButtonModel.isEnabled()))
/*     */       {
/* 172 */         int k = 0;
/* 173 */         MenuElement[] arrayOfMenuElement = ((JMenuBar)localJMenu.getParent()).getSubElements();
/* 174 */         for (int m = 0; m < arrayOfMenuElement.length; m++) {
/* 175 */           if (((JMenuItem)arrayOfMenuElement[m]).isSelected()) {
/* 176 */             k = 1;
/* 177 */             break;
/*     */           }
/*     */         }
/* 180 */         if (k == 0) {
/* 181 */           if (XPStyle.getXP() != null) {
/* 182 */             paramGraphics.setColor(this.selectionBackground);
/* 183 */             paramGraphics.fillRect(0, 0, i, j);
/*     */           }
/*     */           else {
/* 186 */             paramGraphics.setColor(localColor2);
/* 187 */             paramGraphics.drawLine(0, 0, i - 1, 0);
/* 188 */             paramGraphics.drawLine(0, 0, 0, j - 2);
/*     */ 
/* 190 */             paramGraphics.setColor(localColor3);
/* 191 */             paramGraphics.drawLine(i - 1, 0, i - 1, j - 2);
/* 192 */             paramGraphics.drawLine(0, j - 2, i - 1, j - 2);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 197 */     paramGraphics.setColor(localColor1);
/*     */   }
/*     */ 
/*     */   protected void paintText(Graphics paramGraphics, JMenuItem paramJMenuItem, Rectangle paramRectangle, String paramString)
/*     */   {
/* 211 */     if (WindowsMenuItemUI.isVistaPainting()) {
/* 212 */       WindowsMenuItemUI.paintText(this.accessor, paramGraphics, paramJMenuItem, paramRectangle, paramString);
/* 213 */       return;
/*     */     }
/* 215 */     JMenu localJMenu = (JMenu)paramJMenuItem;
/* 216 */     ButtonModel localButtonModel = paramJMenuItem.getModel();
/* 217 */     Color localColor = paramGraphics.getColor();
/*     */ 
/* 220 */     boolean bool = localButtonModel.isRollover();
/* 221 */     if ((bool) && (localJMenu.isTopLevelMenu())) {
/* 222 */       MenuElement[] arrayOfMenuElement = ((JMenuBar)localJMenu.getParent()).getSubElements();
/* 223 */       for (int i = 0; i < arrayOfMenuElement.length; i++) {
/* 224 */         if (((JMenuItem)arrayOfMenuElement[i]).isSelected()) {
/* 225 */           bool = false;
/* 226 */           break;
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 231 */     if (((localButtonModel.isSelected()) && ((WindowsLookAndFeel.isClassicWindows()) || (!localJMenu.isTopLevelMenu()))) || ((XPStyle.getXP() != null) && ((bool) || (localButtonModel.isArmed()) || (localButtonModel.isSelected()))))
/*     */     {
/* 236 */       paramGraphics.setColor(this.selectionForeground);
/*     */     }
/*     */ 
/* 239 */     WindowsGraphicsUtils.paintText(paramGraphics, paramJMenuItem, paramRectangle, paramString, 0);
/*     */ 
/* 241 */     paramGraphics.setColor(localColor);
/*     */   }
/*     */ 
/*     */   protected MouseInputListener createMouseInputListener(JComponent paramJComponent) {
/* 245 */     return new WindowsMouseInputHandler();
/*     */   }
/*     */ 
/*     */   protected Dimension getPreferredMenuItemSize(JComponent paramJComponent, Icon paramIcon1, Icon paramIcon2, int paramInt)
/*     */   {
/* 281 */     Dimension localDimension = super.getPreferredMenuItemSize(paramJComponent, paramIcon1, paramIcon2, paramInt);
/*     */ 
/* 287 */     if (((paramJComponent instanceof JMenu)) && (((JMenu)paramJComponent).isTopLevelMenu()) && (this.menuBarHeight != null) && (localDimension.height < this.menuBarHeight.intValue()))
/*     */     {
/* 290 */       localDimension.height = this.menuBarHeight.intValue();
/*     */     }
/*     */ 
/* 293 */     return localDimension;
/*     */   }
/*     */ 
/*     */   protected class WindowsMouseInputHandler extends BasicMenuUI.MouseInputHandler
/*     */   {
/*     */     protected WindowsMouseInputHandler()
/*     */     {
/* 253 */       super();
/*     */     }
/* 255 */     public void mouseEntered(MouseEvent paramMouseEvent) { super.mouseEntered(paramMouseEvent);
/*     */ 
/* 257 */       JMenu localJMenu = (JMenu)paramMouseEvent.getSource();
/* 258 */       if ((WindowsMenuUI.this.hotTrackingOn) && (localJMenu.isTopLevelMenu()) && (localJMenu.isRolloverEnabled())) {
/* 259 */         localJMenu.getModel().setRollover(true);
/* 260 */         WindowsMenuUI.this.menuItem.repaint();
/*     */       } }
/*     */ 
/*     */     public void mouseExited(MouseEvent paramMouseEvent)
/*     */     {
/* 265 */       super.mouseExited(paramMouseEvent);
/*     */ 
/* 267 */       JMenu localJMenu = (JMenu)paramMouseEvent.getSource();
/* 268 */       ButtonModel localButtonModel = localJMenu.getModel();
/* 269 */       if (localJMenu.isRolloverEnabled()) {
/* 270 */         localButtonModel.setRollover(false);
/* 271 */         WindowsMenuUI.this.menuItem.repaint();
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.java.swing.plaf.windows.WindowsMenuUI
 * JD-Core Version:    0.6.2
 */