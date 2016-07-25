/*     */ package com.sun.java.swing.plaf.windows;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.ComponentOrientation;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Insets;
/*     */ import java.awt.Window;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JPopupMenu;
/*     */ import javax.swing.JRootPane;
/*     */ import javax.swing.MenuElement;
/*     */ import javax.swing.MenuSelectionManager;
/*     */ import javax.swing.Popup;
/*     */ import javax.swing.PopupFactory;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.event.ChangeEvent;
/*     */ import javax.swing.event.ChangeListener;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.basic.BasicPopupMenuUI;
/*     */ import sun.swing.StringUIClientPropertyKey;
/*     */ import sun.swing.SwingUtilities2;
/*     */ 
/*     */ public class WindowsPopupMenuUI extends BasicPopupMenuUI
/*     */ {
/*  61 */   static MnemonicListener mnemonicListener = null;
/*  62 */   static final Object GUTTER_OFFSET_KEY = new StringUIClientPropertyKey("GUTTER_OFFSET_KEY");
/*     */ 
/*     */   public static ComponentUI createUI(JComponent paramJComponent)
/*     */   {
/*  66 */     return new WindowsPopupMenuUI();
/*     */   }
/*     */ 
/*     */   public void installListeners() {
/*  70 */     super.installListeners();
/*  71 */     if ((!UIManager.getBoolean("Button.showMnemonics")) && (mnemonicListener == null))
/*     */     {
/*  74 */       mnemonicListener = new MnemonicListener();
/*  75 */       MenuSelectionManager.defaultManager().addChangeListener(mnemonicListener);
/*     */     }
/*     */   }
/*     */ 
/*     */   public Popup getPopup(JPopupMenu paramJPopupMenu, int paramInt1, int paramInt2)
/*     */   {
/*  91 */     PopupFactory localPopupFactory = PopupFactory.getSharedInstance();
/*  92 */     return localPopupFactory.getPopup(paramJPopupMenu.getInvoker(), paramJPopupMenu, paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   static int getTextOffset(JComponent paramJComponent)
/*     */   {
/* 126 */     int i = -1;
/* 127 */     Object localObject = paramJComponent.getClientProperty(SwingUtilities2.BASICMENUITEMUI_MAX_TEXT_OFFSET);
/*     */ 
/* 129 */     if ((localObject instanceof Integer))
/*     */     {
/* 134 */       i = ((Integer)localObject).intValue();
/* 135 */       int j = 0;
/* 136 */       Component localComponent = paramJComponent.getComponent(0);
/* 137 */       if (localComponent != null) {
/* 138 */         j = localComponent.getX();
/*     */       }
/* 140 */       i += j;
/*     */     }
/* 142 */     return i;
/*     */   }
/*     */ 
/*     */   static int getSpanBeforeGutter()
/*     */   {
/* 151 */     return 3;
/*     */   }
/*     */ 
/*     */   static int getSpanAfterGutter()
/*     */   {
/* 160 */     return 3;
/*     */   }
/*     */ 
/*     */   static int getGutterWidth()
/*     */   {
/* 169 */     int i = 2;
/* 170 */     XPStyle localXPStyle = XPStyle.getXP();
/* 171 */     if (localXPStyle != null) {
/* 172 */       XPStyle.Skin localSkin = localXPStyle.getSkin(null, TMSchema.Part.MP_POPUPGUTTER);
/* 173 */       i = localSkin.getWidth();
/*     */     }
/* 175 */     return i;
/*     */   }
/*     */ 
/*     */   private static boolean isLeftToRight(JComponent paramJComponent)
/*     */   {
/* 187 */     boolean bool = true;
/* 188 */     for (int i = paramJComponent.getComponentCount() - 1; (i >= 0) && (bool); i--) {
/* 189 */       bool = paramJComponent.getComponent(i).getComponentOrientation().isLeftToRight();
/*     */     }
/*     */ 
/* 192 */     return bool;
/*     */   }
/*     */ 
/*     */   public void paint(Graphics paramGraphics, JComponent paramJComponent)
/*     */   {
/* 197 */     XPStyle localXPStyle = XPStyle.getXP();
/* 198 */     if (WindowsMenuItemUI.isVistaPainting(localXPStyle)) {
/* 199 */       XPStyle.Skin localSkin = localXPStyle.getSkin(paramJComponent, TMSchema.Part.MP_POPUPBACKGROUND);
/* 200 */       localSkin.paintSkin(paramGraphics, 0, 0, paramJComponent.getWidth(), paramJComponent.getHeight(), TMSchema.State.NORMAL);
/* 201 */       int i = getTextOffset(paramJComponent);
/* 202 */       if ((i >= 0) && (isLeftToRight(paramJComponent)))
/*     */       {
/* 205 */         localSkin = localXPStyle.getSkin(paramJComponent, TMSchema.Part.MP_POPUPGUTTER);
/* 206 */         int j = getGutterWidth();
/* 207 */         int k = i - getSpanAfterGutter() - j;
/*     */ 
/* 209 */         paramJComponent.putClientProperty(GUTTER_OFFSET_KEY, Integer.valueOf(k));
/*     */ 
/* 211 */         Insets localInsets = paramJComponent.getInsets();
/* 212 */         localSkin.paintSkin(paramGraphics, k, localInsets.top, j, paramJComponent.getHeight() - localInsets.bottom - localInsets.top, TMSchema.State.NORMAL);
/*     */       }
/* 216 */       else if (paramJComponent.getClientProperty(GUTTER_OFFSET_KEY) != null) {
/* 217 */         paramJComponent.putClientProperty(GUTTER_OFFSET_KEY, null);
/*     */       }
/*     */     }
/*     */     else {
/* 221 */       super.paint(paramGraphics, paramJComponent);
/*     */     }
/*     */   }
/*     */ 
/*     */   static class MnemonicListener
/*     */     implements ChangeListener
/*     */   {
/*  96 */     JRootPane repaintRoot = null;
/*     */ 
/*     */     public void stateChanged(ChangeEvent paramChangeEvent) {
/*  99 */       MenuSelectionManager localMenuSelectionManager = (MenuSelectionManager)paramChangeEvent.getSource();
/* 100 */       MenuElement[] arrayOfMenuElement = localMenuSelectionManager.getSelectedPath();
/*     */       Object localObject;
/* 101 */       if (arrayOfMenuElement.length == 0) {
/* 102 */         if (!WindowsLookAndFeel.isMnemonicHidden())
/*     */         {
/* 104 */           WindowsLookAndFeel.setMnemonicHidden(true);
/* 105 */           if (this.repaintRoot != null) {
/* 106 */             localObject = SwingUtilities.getWindowAncestor(this.repaintRoot);
/*     */ 
/* 108 */             WindowsGraphicsUtils.repaintMnemonicsInWindow((Window)localObject);
/*     */           }
/*     */         }
/*     */       } else {
/* 112 */         localObject = (Component)arrayOfMenuElement[0];
/* 113 */         if ((localObject instanceof JPopupMenu)) localObject = ((JPopupMenu)localObject).getInvoker();
/* 114 */         this.repaintRoot = SwingUtilities.getRootPane((Component)localObject);
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.java.swing.plaf.windows.WindowsPopupMenuUI
 * JD-Core Version:    0.6.2
 */