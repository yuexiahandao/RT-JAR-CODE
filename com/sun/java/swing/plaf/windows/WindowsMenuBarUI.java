/*     */ package com.sun.java.swing.plaf.windows;
/*     */ 
/*     */ import java.awt.Container;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Window;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.HierarchyEvent;
/*     */ import java.awt.event.HierarchyListener;
/*     */ import java.awt.event.WindowAdapter;
/*     */ import java.awt.event.WindowEvent;
/*     */ import java.awt.event.WindowListener;
/*     */ import javax.swing.AbstractAction;
/*     */ import javax.swing.ActionMap;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JMenu;
/*     */ import javax.swing.JMenuBar;
/*     */ import javax.swing.JRootPane;
/*     */ import javax.swing.MenuElement;
/*     */ import javax.swing.MenuSelectionManager;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.plaf.ActionMapUIResource;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.basic.BasicMenuBarUI;
/*     */ 
/*     */ public class WindowsMenuBarUI extends BasicMenuBarUI
/*     */ {
/*     */   private WindowListener windowListener;
/*     */   private HierarchyListener hierarchyListener;
/*     */   private Window window;
/*     */ 
/*     */   public WindowsMenuBarUI()
/*     */   {
/*  58 */     this.windowListener = null;
/*  59 */     this.hierarchyListener = null;
/*  60 */     this.window = null;
/*     */   }
/*     */   public static ComponentUI createUI(JComponent paramJComponent) {
/*  63 */     return new WindowsMenuBarUI();
/*     */   }
/*     */ 
/*     */   protected void uninstallListeners()
/*     */   {
/*  68 */     uninstallWindowListener();
/*  69 */     if (this.hierarchyListener != null) {
/*  70 */       this.menuBar.removeHierarchyListener(this.hierarchyListener);
/*  71 */       this.hierarchyListener = null;
/*     */     }
/*  73 */     super.uninstallListeners();
/*     */   }
/*     */   private void installWindowListener() {
/*  76 */     if (this.windowListener == null) {
/*  77 */       Container localContainer = this.menuBar.getTopLevelAncestor();
/*  78 */       if ((localContainer instanceof Window)) {
/*  79 */         this.window = ((Window)localContainer);
/*  80 */         this.windowListener = new WindowAdapter()
/*     */         {
/*     */           public void windowActivated(WindowEvent paramAnonymousWindowEvent) {
/*  83 */             WindowsMenuBarUI.this.menuBar.repaint();
/*     */           }
/*     */ 
/*     */           public void windowDeactivated(WindowEvent paramAnonymousWindowEvent) {
/*  87 */             WindowsMenuBarUI.this.menuBar.repaint();
/*     */           }
/*     */         };
/*  90 */         ((Window)localContainer).addWindowListener(this.windowListener);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*  95 */   private void uninstallWindowListener() { if ((this.windowListener != null) && (this.window != null)) {
/*  96 */       this.window.removeWindowListener(this.windowListener);
/*     */     }
/*  98 */     this.window = null;
/*  99 */     this.windowListener = null; }
/*     */ 
/*     */   protected void installListeners()
/*     */   {
/* 103 */     if (WindowsLookAndFeel.isOnVista()) {
/* 104 */       installWindowListener();
/* 105 */       this.hierarchyListener = new HierarchyListener()
/*     */       {
/*     */         public void hierarchyChanged(HierarchyEvent paramAnonymousHierarchyEvent) {
/* 108 */           if ((paramAnonymousHierarchyEvent.getChangeFlags() & 0x2) != 0L)
/*     */           {
/* 110 */             if (WindowsMenuBarUI.this.menuBar.isDisplayable())
/* 111 */               WindowsMenuBarUI.this.installWindowListener();
/*     */             else
/* 113 */               WindowsMenuBarUI.this.uninstallWindowListener();
/*     */           }
/*     */         }
/*     */       };
/* 118 */       this.menuBar.addHierarchyListener(this.hierarchyListener);
/*     */     }
/* 120 */     super.installListeners();
/*     */   }
/*     */ 
/*     */   protected void installKeyboardActions() {
/* 124 */     super.installKeyboardActions();
/* 125 */     Object localObject = SwingUtilities.getUIActionMap(this.menuBar);
/* 126 */     if (localObject == null) {
/* 127 */       localObject = new ActionMapUIResource();
/* 128 */       SwingUtilities.replaceUIActionMap(this.menuBar, (ActionMap)localObject);
/*     */     }
/* 130 */     ((ActionMap)localObject).put("takeFocus", new TakeFocus(null));
/*     */   }
/*     */ 
/*     */   public void paint(Graphics paramGraphics, JComponent paramJComponent)
/*     */   {
/* 158 */     XPStyle localXPStyle = XPStyle.getXP();
/* 159 */     if (WindowsMenuItemUI.isVistaPainting(localXPStyle))
/*     */     {
/* 161 */       XPStyle.Skin localSkin = localXPStyle.getSkin(paramJComponent, TMSchema.Part.MP_BARBACKGROUND);
/* 162 */       int i = paramJComponent.getWidth();
/* 163 */       int j = paramJComponent.getHeight();
/* 164 */       TMSchema.State localState = isActive(paramJComponent) ? TMSchema.State.ACTIVE : TMSchema.State.INACTIVE;
/* 165 */       localSkin.paintSkin(paramGraphics, 0, 0, i, j, localState);
/*     */     } else {
/* 167 */       super.paint(paramGraphics, paramJComponent);
/*     */     }
/*     */   }
/*     */ 
/*     */   static boolean isActive(JComponent paramJComponent)
/*     */   {
/* 177 */     JRootPane localJRootPane = paramJComponent.getRootPane();
/* 178 */     if (localJRootPane != null) {
/* 179 */       Container localContainer = localJRootPane.getParent();
/* 180 */       if ((localContainer instanceof Window)) {
/* 181 */         return ((Window)localContainer).isActive();
/*     */       }
/*     */     }
/* 184 */     return true;
/*     */   }
/*     */ 
/*     */   private static class TakeFocus extends AbstractAction
/*     */   {
/*     */     public void actionPerformed(ActionEvent paramActionEvent)
/*     */     {
/* 139 */       JMenuBar localJMenuBar = (JMenuBar)paramActionEvent.getSource();
/* 140 */       JMenu localJMenu = localJMenuBar.getMenu(0);
/* 141 */       if (localJMenu != null) {
/* 142 */         MenuSelectionManager localMenuSelectionManager = MenuSelectionManager.defaultManager();
/*     */ 
/* 144 */         MenuElement[] arrayOfMenuElement = new MenuElement[2];
/* 145 */         arrayOfMenuElement[0] = localJMenuBar;
/* 146 */         arrayOfMenuElement[1] = localJMenu;
/* 147 */         localMenuSelectionManager.setSelectedPath(arrayOfMenuElement);
/*     */ 
/* 150 */         WindowsLookAndFeel.setMnemonicHidden(false);
/* 151 */         WindowsLookAndFeel.repaintRootPane(localJMenuBar);
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.java.swing.plaf.windows.WindowsMenuBarUI
 * JD-Core Version:    0.6.2
 */