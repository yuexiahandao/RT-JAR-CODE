/*     */ package com.sun.java.swing.plaf.motif;
/*     */ 
/*     */ import java.awt.Point;
/*     */ import java.awt.event.MouseEvent;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JMenuItem;
/*     */ import javax.swing.LookAndFeel;
/*     */ import javax.swing.MenuSelectionManager;
/*     */ import javax.swing.event.ChangeEvent;
/*     */ import javax.swing.event.ChangeListener;
/*     */ import javax.swing.event.MouseInputListener;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.basic.BasicCheckBoxMenuItemUI;
/*     */ 
/*     */ public class MotifCheckBoxMenuItemUI extends BasicCheckBoxMenuItemUI
/*     */ {
/*     */   protected ChangeListener changeListener;
/*     */ 
/*     */   public static ComponentUI createUI(JComponent paramJComponent)
/*     */   {
/*  50 */     return new MotifCheckBoxMenuItemUI();
/*     */   }
/*     */ 
/*     */   protected void installListeners() {
/*  54 */     super.installListeners();
/*  55 */     this.changeListener = createChangeListener(this.menuItem);
/*  56 */     this.menuItem.addChangeListener(this.changeListener);
/*     */   }
/*     */ 
/*     */   protected void uninstallListeners() {
/*  60 */     super.uninstallListeners();
/*  61 */     this.menuItem.removeChangeListener(this.changeListener);
/*     */   }
/*     */ 
/*     */   protected ChangeListener createChangeListener(JComponent paramJComponent) {
/*  65 */     return new ChangeHandler();
/*     */   }
/*     */ 
/*     */   protected MouseInputListener createMouseInputListener(JComponent paramJComponent)
/*     */   {
/*  76 */     return new MouseInputHandler();
/*     */   }
/*     */ 
/*     */   protected class ChangeHandler
/*     */     implements ChangeListener
/*     */   {
/*     */     protected ChangeHandler()
/*     */     {
/*     */     }
/*     */ 
/*     */     public void stateChanged(ChangeEvent paramChangeEvent)
/*     */     {
/*  70 */       JMenuItem localJMenuItem = (JMenuItem)paramChangeEvent.getSource();
/*  71 */       LookAndFeel.installProperty(localJMenuItem, "borderPainted", Boolean.valueOf(localJMenuItem.isArmed()));
/*     */     }
/*     */   }
/*     */ 
/*     */   protected class MouseInputHandler implements MouseInputListener {
/*     */     protected MouseInputHandler() {
/*     */     }
/*     */ 
/*     */     public void mouseClicked(MouseEvent paramMouseEvent) {
/*     */     }
/*     */ 
/*     */     public void mousePressed(MouseEvent paramMouseEvent) {
/*  83 */       MenuSelectionManager localMenuSelectionManager = MenuSelectionManager.defaultManager();
/*  84 */       localMenuSelectionManager.setSelectedPath(MotifCheckBoxMenuItemUI.this.getPath());
/*     */     }
/*     */     public void mouseReleased(MouseEvent paramMouseEvent) {
/*  87 */       MenuSelectionManager localMenuSelectionManager = MenuSelectionManager.defaultManager();
/*     */ 
/*  89 */       JMenuItem localJMenuItem = (JMenuItem)paramMouseEvent.getComponent();
/*  90 */       Point localPoint = paramMouseEvent.getPoint();
/*  91 */       if ((localPoint.x >= 0) && (localPoint.x < localJMenuItem.getWidth()) && (localPoint.y >= 0) && (localPoint.y < localJMenuItem.getHeight()))
/*     */       {
/*  93 */         localMenuSelectionManager.clearSelectedPath();
/*  94 */         localJMenuItem.doClick(0);
/*     */       } else {
/*  96 */         localMenuSelectionManager.processMouseEvent(paramMouseEvent);
/*     */       }
/*     */     }
/*     */     public void mouseEntered(MouseEvent paramMouseEvent) {
/*     */     }
/*     */     public void mouseExited(MouseEvent paramMouseEvent) {  }
/*     */ 
/* 102 */     public void mouseDragged(MouseEvent paramMouseEvent) { MenuSelectionManager.defaultManager().processMouseEvent(paramMouseEvent); }
/*     */ 
/*     */ 
/*     */     public void mouseMoved(MouseEvent paramMouseEvent)
/*     */     {
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.java.swing.plaf.motif.MotifCheckBoxMenuItemUI
 * JD-Core Version:    0.6.2
 */