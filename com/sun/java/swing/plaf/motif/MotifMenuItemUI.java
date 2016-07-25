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
/*     */ import javax.swing.plaf.basic.BasicMenuItemUI;
/*     */ 
/*     */ public class MotifMenuItemUI extends BasicMenuItemUI
/*     */ {
/*     */   protected ChangeListener changeListener;
/*     */ 
/*     */   public static ComponentUI createUI(JComponent paramJComponent)
/*     */   {
/*  51 */     return new MotifMenuItemUI();
/*     */   }
/*     */ 
/*     */   protected void installListeners() {
/*  55 */     super.installListeners();
/*  56 */     this.changeListener = createChangeListener(this.menuItem);
/*  57 */     this.menuItem.addChangeListener(this.changeListener);
/*     */   }
/*     */ 
/*     */   protected void uninstallListeners() {
/*  61 */     super.uninstallListeners();
/*  62 */     this.menuItem.removeChangeListener(this.changeListener);
/*     */   }
/*     */ 
/*     */   protected ChangeListener createChangeListener(JComponent paramJComponent) {
/*  66 */     return new ChangeHandler();
/*     */   }
/*     */ 
/*     */   protected MouseInputListener createMouseInputListener(JComponent paramJComponent) {
/*  70 */     return new MouseInputHandler();
/*     */   }
/*     */   protected class ChangeHandler implements ChangeListener {
/*     */     protected ChangeHandler() {
/*     */     }
/*     */     public void stateChanged(ChangeEvent paramChangeEvent) {
/*  76 */       JMenuItem localJMenuItem = (JMenuItem)paramChangeEvent.getSource();
/*  77 */       LookAndFeel.installProperty(localJMenuItem, "borderPainted", Boolean.valueOf((localJMenuItem.isArmed()) || (localJMenuItem.isSelected())));
/*     */     }
/*     */   }
/*     */   protected class MouseInputHandler implements MouseInputListener {
/*     */     protected MouseInputHandler() {
/*     */     }
/*     */     public void mouseClicked(MouseEvent paramMouseEvent) {
/*     */     }
/*  85 */     public void mousePressed(MouseEvent paramMouseEvent) { MenuSelectionManager localMenuSelectionManager = MenuSelectionManager.defaultManager();
/*  86 */       localMenuSelectionManager.setSelectedPath(MotifMenuItemUI.this.getPath()); }
/*     */ 
/*     */     public void mouseReleased(MouseEvent paramMouseEvent) {
/*  89 */       MenuSelectionManager localMenuSelectionManager = MenuSelectionManager.defaultManager();
/*     */ 
/*  91 */       JMenuItem localJMenuItem = (JMenuItem)paramMouseEvent.getComponent();
/*  92 */       Point localPoint = paramMouseEvent.getPoint();
/*  93 */       if ((localPoint.x >= 0) && (localPoint.x < localJMenuItem.getWidth()) && (localPoint.y >= 0) && (localPoint.y < localJMenuItem.getHeight()))
/*     */       {
/*  95 */         localMenuSelectionManager.clearSelectedPath();
/*  96 */         localJMenuItem.doClick(0);
/*     */       } else {
/*  98 */         localMenuSelectionManager.processMouseEvent(paramMouseEvent);
/*     */       }
/*     */     }
/*     */     public void mouseEntered(MouseEvent paramMouseEvent) {
/*     */     }
/*     */     public void mouseExited(MouseEvent paramMouseEvent) {  }
/*     */ 
/* 104 */     public void mouseDragged(MouseEvent paramMouseEvent) { MenuSelectionManager.defaultManager().processMouseEvent(paramMouseEvent); }
/*     */ 
/*     */ 
/*     */     public void mouseMoved(MouseEvent paramMouseEvent)
/*     */     {
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.java.swing.plaf.motif.MotifMenuItemUI
 * JD-Core Version:    0.6.2
 */