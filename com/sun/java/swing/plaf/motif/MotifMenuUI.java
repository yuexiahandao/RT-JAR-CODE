/*     */ package com.sun.java.swing.plaf.motif;
/*     */ 
/*     */ import java.awt.Point;
/*     */ import java.awt.event.MouseEvent;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JMenu;
/*     */ import javax.swing.JMenuBar;
/*     */ import javax.swing.JMenuItem;
/*     */ import javax.swing.JPopupMenu;
/*     */ import javax.swing.MenuElement;
/*     */ import javax.swing.MenuSelectionManager;
/*     */ import javax.swing.event.ChangeEvent;
/*     */ import javax.swing.event.ChangeListener;
/*     */ import javax.swing.event.MouseInputListener;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.basic.BasicMenuUI;
/*     */ import javax.swing.plaf.basic.BasicMenuUI.ChangeHandler;
/*     */ 
/*     */ public class MotifMenuUI extends BasicMenuUI
/*     */ {
/*     */   public static ComponentUI createUI(JComponent paramJComponent)
/*     */   {
/*  49 */     return new MotifMenuUI();
/*     */   }
/*     */ 
/*     */   protected ChangeListener createChangeListener(JComponent paramJComponent)
/*     */   {
/*  66 */     return new MotifChangeHandler((JMenu)paramJComponent, this);
/*     */   }
/*     */ 
/*     */   private boolean popupIsOpen(JMenu paramJMenu, MenuElement[] paramArrayOfMenuElement)
/*     */   {
/*  71 */     JPopupMenu localJPopupMenu = paramJMenu.getPopupMenu();
/*     */ 
/*  73 */     for (int i = paramArrayOfMenuElement.length - 1; i >= 0; i--) {
/*  74 */       if (paramArrayOfMenuElement[i].getComponent() == localJPopupMenu)
/*  75 */         return true;
/*     */     }
/*  77 */     return false;
/*     */   }
/*     */ 
/*     */   protected MouseInputListener createMouseInputListener(JComponent paramJComponent) {
/*  81 */     return new MouseInputHandler();
/*     */   }
/*     */ 
/*     */   public class MotifChangeHandler extends BasicMenuUI.ChangeHandler {
/*     */     public MotifChangeHandler(JMenu paramMotifMenuUI, MotifMenuUI arg3) {
/*  86 */       super(paramMotifMenuUI, localBasicMenuUI);
/*     */     }
/*     */ 
/*     */     public void stateChanged(ChangeEvent paramChangeEvent)
/*     */     {
/*  91 */       JMenuItem localJMenuItem = (JMenuItem)paramChangeEvent.getSource();
/*  92 */       if ((localJMenuItem.isArmed()) || (localJMenuItem.isSelected())) {
/*  93 */         localJMenuItem.setBorderPainted(true);
/*     */       }
/*     */       else {
/*  96 */         localJMenuItem.setBorderPainted(false);
/*     */       }
/*     */ 
/*  99 */       super.stateChanged(paramChangeEvent);
/*     */     }
/*     */   }
/*     */   protected class MouseInputHandler implements MouseInputListener {
/*     */     protected MouseInputHandler() {
/*     */     }
/*     */     public void mouseClicked(MouseEvent paramMouseEvent) {  }
/*     */ 
/* 106 */     public void mousePressed(MouseEvent paramMouseEvent) { MenuSelectionManager localMenuSelectionManager = MenuSelectionManager.defaultManager();
/* 107 */       JMenu localJMenu = (JMenu)paramMouseEvent.getComponent();
/* 108 */       if (localJMenu.isEnabled())
/*     */       {
/*     */         MenuElement[] arrayOfMenuElement;
/* 109 */         if (localJMenu.isTopLevelMenu()) {
/* 110 */           if (localJMenu.isSelected()) {
/* 111 */             localMenuSelectionManager.clearSelectedPath();
/*     */           } else {
/* 113 */             localObject = localJMenu.getParent();
/* 114 */             if ((localObject != null) && ((localObject instanceof JMenuBar))) {
/* 115 */               arrayOfMenuElement = new MenuElement[2];
/* 116 */               arrayOfMenuElement[0] = ((MenuElement)localObject);
/* 117 */               arrayOfMenuElement[1] = localJMenu;
/* 118 */               localMenuSelectionManager.setSelectedPath(arrayOfMenuElement);
/*     */             }
/*     */           }
/*     */         }
/*     */ 
/* 123 */         Object localObject = MotifMenuUI.this.getPath();
/* 124 */         if (localObject.length > 0) {
/* 125 */           arrayOfMenuElement = new MenuElement[localObject.length + 1];
/* 126 */           System.arraycopy(localObject, 0, arrayOfMenuElement, 0, localObject.length);
/* 127 */           arrayOfMenuElement[localObject.length] = localJMenu.getPopupMenu();
/* 128 */           localMenuSelectionManager.setSelectedPath(arrayOfMenuElement);
/*     */         }
/*     */       } }
/*     */ 
/*     */     public void mouseReleased(MouseEvent paramMouseEvent)
/*     */     {
/* 134 */       MenuSelectionManager localMenuSelectionManager = MenuSelectionManager.defaultManager();
/*     */ 
/* 136 */       JMenuItem localJMenuItem = (JMenuItem)paramMouseEvent.getComponent();
/* 137 */       Point localPoint = paramMouseEvent.getPoint();
/* 138 */       if ((localPoint.x < 0) || (localPoint.x >= localJMenuItem.getWidth()) || (localPoint.y < 0) || (localPoint.y >= localJMenuItem.getHeight()))
/*     */       {
/* 140 */         localMenuSelectionManager.processMouseEvent(paramMouseEvent);
/*     */       }
/*     */     }
/*     */     public void mouseEntered(MouseEvent paramMouseEvent) {
/*     */     }
/*     */     public void mouseExited(MouseEvent paramMouseEvent) {  }
/*     */ 
/* 146 */     public void mouseDragged(MouseEvent paramMouseEvent) { MenuSelectionManager.defaultManager().processMouseEvent(paramMouseEvent); }
/*     */ 
/*     */ 
/*     */     public void mouseMoved(MouseEvent paramMouseEvent)
/*     */     {
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.java.swing.plaf.motif.MotifMenuUI
 * JD-Core Version:    0.6.2
 */