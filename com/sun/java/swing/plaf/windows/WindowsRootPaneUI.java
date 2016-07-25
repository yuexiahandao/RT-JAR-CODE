/*     */ package com.sun.java.swing.plaf.windows;
/*     */ 
/*     */ import java.awt.KeyEventPostProcessor;
/*     */ import java.awt.Toolkit;
/*     */ import java.awt.Window;
/*     */ import java.awt.event.KeyEvent;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JFrame;
/*     */ import javax.swing.JMenuBar;
/*     */ import javax.swing.JRootPane;
/*     */ import javax.swing.MenuElement;
/*     */ import javax.swing.MenuSelectionManager;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.basic.BasicRootPaneUI;
/*     */ import javax.swing.plaf.basic.ComboPopup;
/*     */ import sun.awt.SunToolkit;
/*     */ 
/*     */ public class WindowsRootPaneUI extends BasicRootPaneUI
/*     */ {
/*  71 */   private static final WindowsRootPaneUI windowsRootPaneUI = new WindowsRootPaneUI();
/*  72 */   static final AltProcessor altProcessor = new AltProcessor();
/*     */ 
/*     */   public static ComponentUI createUI(JComponent paramJComponent) {
/*  75 */     return windowsRootPaneUI;
/*     */   }
/*     */ 
/*     */   static class AltProcessor implements KeyEventPostProcessor {
/*  79 */     static boolean altKeyPressed = false;
/*  80 */     static boolean menuCanceledOnPress = false;
/*  81 */     static JRootPane root = null;
/*  82 */     static Window winAncestor = null;
/*     */ 
/*     */     void altPressed(KeyEvent paramKeyEvent) {
/*  85 */       MenuSelectionManager localMenuSelectionManager = MenuSelectionManager.defaultManager();
/*     */ 
/*  87 */       MenuElement[] arrayOfMenuElement = localMenuSelectionManager.getSelectedPath();
/*  88 */       if ((arrayOfMenuElement.length > 0) && (!(arrayOfMenuElement[0] instanceof ComboPopup))) {
/*  89 */         localMenuSelectionManager.clearSelectedPath();
/*  90 */         menuCanceledOnPress = true;
/*  91 */         paramKeyEvent.consume();
/*  92 */       } else if (arrayOfMenuElement.length > 0) {
/*  93 */         menuCanceledOnPress = false;
/*  94 */         WindowsLookAndFeel.setMnemonicHidden(false);
/*  95 */         WindowsGraphicsUtils.repaintMnemonicsInWindow(winAncestor);
/*  96 */         paramKeyEvent.consume();
/*     */       } else {
/*  98 */         menuCanceledOnPress = false;
/*  99 */         WindowsLookAndFeel.setMnemonicHidden(false);
/* 100 */         WindowsGraphicsUtils.repaintMnemonicsInWindow(winAncestor);
/* 101 */         JMenuBar localJMenuBar = root != null ? root.getJMenuBar() : null;
/* 102 */         if ((localJMenuBar == null) && ((winAncestor instanceof JFrame))) {
/* 103 */           localJMenuBar = ((JFrame)winAncestor).getJMenuBar();
/*     */         }
/* 105 */         Object localObject = localJMenuBar != null ? localJMenuBar.getMenu(0) : null;
/* 106 */         if (localObject != null)
/* 107 */           paramKeyEvent.consume();
/*     */       }
/*     */     }
/*     */ 
/*     */     void altReleased(KeyEvent paramKeyEvent)
/*     */     {
/* 113 */       if (menuCanceledOnPress) {
/* 114 */         WindowsLookAndFeel.setMnemonicHidden(true);
/* 115 */         WindowsGraphicsUtils.repaintMnemonicsInWindow(winAncestor);
/* 116 */         return;
/*     */       }
/*     */ 
/* 119 */       MenuSelectionManager localMenuSelectionManager = MenuSelectionManager.defaultManager();
/*     */ 
/* 121 */       if (localMenuSelectionManager.getSelectedPath().length == 0)
/*     */       {
/* 124 */         JMenuBar localJMenuBar = root != null ? root.getJMenuBar() : null;
/* 125 */         if ((localJMenuBar == null) && ((winAncestor instanceof JFrame))) {
/* 126 */           localJMenuBar = ((JFrame)winAncestor).getJMenuBar();
/*     */         }
/* 128 */         Object localObject = localJMenuBar != null ? localJMenuBar.getMenu(0) : null;
/*     */ 
/* 136 */         int i = 0;
/* 137 */         Toolkit localToolkit = Toolkit.getDefaultToolkit();
/* 138 */         if ((localToolkit instanceof SunToolkit)) {
/* 139 */           i = paramKeyEvent.getWhen() <= ((SunToolkit)localToolkit).getWindowDeactivationTime(winAncestor) ? 1 : 0;
/*     */         }
/*     */ 
/* 142 */         if ((localObject != null) && (i == 0)) {
/* 143 */           MenuElement[] arrayOfMenuElement = new MenuElement[2];
/* 144 */           arrayOfMenuElement[0] = localJMenuBar;
/* 145 */           arrayOfMenuElement[1] = localObject;
/* 146 */           localMenuSelectionManager.setSelectedPath(arrayOfMenuElement);
/* 147 */         } else if (!WindowsLookAndFeel.isMnemonicHidden()) {
/* 148 */           WindowsLookAndFeel.setMnemonicHidden(true);
/* 149 */           WindowsGraphicsUtils.repaintMnemonicsInWindow(winAncestor);
/*     */         }
/*     */       }
/* 152 */       else if ((localMenuSelectionManager.getSelectedPath()[0] instanceof ComboPopup)) {
/* 153 */         WindowsLookAndFeel.setMnemonicHidden(true);
/* 154 */         WindowsGraphicsUtils.repaintMnemonicsInWindow(winAncestor);
/*     */       }
/*     */     }
/*     */ 
/*     */     public boolean postProcessKeyEvent(KeyEvent paramKeyEvent)
/*     */     {
/* 161 */       if (paramKeyEvent.isConsumed())
/*     */       {
/* 163 */         return false;
/*     */       }
/* 165 */       if (paramKeyEvent.getKeyCode() == 18) {
/* 166 */         root = SwingUtilities.getRootPane(paramKeyEvent.getComponent());
/* 167 */         winAncestor = root == null ? null : SwingUtilities.getWindowAncestor(root);
/*     */ 
/* 170 */         if (paramKeyEvent.getID() == 401) {
/* 171 */           if (!altKeyPressed) {
/* 172 */             altPressed(paramKeyEvent);
/*     */           }
/* 174 */           altKeyPressed = true;
/* 175 */           return true;
/* 176 */         }if (paramKeyEvent.getID() == 402) {
/* 177 */           if (altKeyPressed) {
/* 178 */             altReleased(paramKeyEvent);
/*     */           } else {
/* 180 */             MenuSelectionManager localMenuSelectionManager = MenuSelectionManager.defaultManager();
/*     */ 
/* 182 */             MenuElement[] arrayOfMenuElement = localMenuSelectionManager.getSelectedPath();
/* 183 */             if (arrayOfMenuElement.length <= 0) {
/* 184 */               WindowsLookAndFeel.setMnemonicHidden(true);
/* 185 */               WindowsGraphicsUtils.repaintMnemonicsInWindow(winAncestor);
/*     */             }
/*     */           }
/* 188 */           altKeyPressed = false;
/*     */         }
/* 190 */         root = null;
/* 191 */         winAncestor = null;
/*     */       } else {
/* 193 */         altKeyPressed = false;
/*     */       }
/* 195 */       return false;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.java.swing.plaf.windows.WindowsRootPaneUI
 * JD-Core Version:    0.6.2
 */