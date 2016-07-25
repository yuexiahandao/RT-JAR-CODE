/*     */ package com.sun.java.swing.plaf.motif;
/*     */ 
/*     */ import java.awt.Point;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.io.Serializable;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JMenuItem;
/*     */ import javax.swing.LookAndFeel;
/*     */ import javax.swing.MenuSelectionManager;
/*     */ import javax.swing.event.ChangeEvent;
/*     */ import javax.swing.event.ChangeListener;
/*     */ import javax.swing.event.MouseInputListener;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.basic.BasicRadioButtonMenuItemUI;
/*     */ 
/*     */ public class MotifRadioButtonMenuItemUI extends BasicRadioButtonMenuItemUI
/*     */ {
/*     */   protected ChangeListener changeListener;
/*     */ 
/*     */   public static ComponentUI createUI(JComponent paramJComponent)
/*     */   {
/*  56 */     return new MotifRadioButtonMenuItemUI();
/*     */   }
/*     */ 
/*     */   protected void installListeners() {
/*  60 */     super.installListeners();
/*  61 */     this.changeListener = createChangeListener(this.menuItem);
/*  62 */     this.menuItem.addChangeListener(this.changeListener);
/*     */   }
/*     */ 
/*     */   protected void uninstallListeners() {
/*  66 */     super.uninstallListeners();
/*  67 */     this.menuItem.removeChangeListener(this.changeListener);
/*     */   }
/*     */ 
/*     */   protected ChangeListener createChangeListener(JComponent paramJComponent) {
/*  71 */     return new ChangeHandler();
/*     */   }
/*     */ 
/*     */   protected MouseInputListener createMouseInputListener(JComponent paramJComponent)
/*     */   {
/*  82 */     return new MouseInputHandler();
/*     */   }
/*     */ 
/*     */   protected class ChangeHandler
/*     */     implements ChangeListener, Serializable
/*     */   {
/*     */     protected ChangeHandler()
/*     */     {
/*     */     }
/*     */ 
/*     */     public void stateChanged(ChangeEvent paramChangeEvent)
/*     */     {
/*  76 */       JMenuItem localJMenuItem = (JMenuItem)paramChangeEvent.getSource();
/*  77 */       LookAndFeel.installProperty(localJMenuItem, "borderPainted", Boolean.valueOf(localJMenuItem.isArmed()));
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
/*  89 */       MenuSelectionManager localMenuSelectionManager = MenuSelectionManager.defaultManager();
/*  90 */       localMenuSelectionManager.setSelectedPath(MotifRadioButtonMenuItemUI.this.getPath());
/*     */     }
/*     */     public void mouseReleased(MouseEvent paramMouseEvent) {
/*  93 */       MenuSelectionManager localMenuSelectionManager = MenuSelectionManager.defaultManager();
/*     */ 
/*  95 */       JMenuItem localJMenuItem = (JMenuItem)paramMouseEvent.getComponent();
/*  96 */       Point localPoint = paramMouseEvent.getPoint();
/*  97 */       if ((localPoint.x >= 0) && (localPoint.x < localJMenuItem.getWidth()) && (localPoint.y >= 0) && (localPoint.y < localJMenuItem.getHeight()))
/*     */       {
/*  99 */         localMenuSelectionManager.clearSelectedPath();
/* 100 */         localJMenuItem.doClick(0);
/*     */       } else {
/* 102 */         localMenuSelectionManager.processMouseEvent(paramMouseEvent);
/*     */       }
/*     */     }
/*     */     public void mouseEntered(MouseEvent paramMouseEvent) {
/*     */     }
/*     */     public void mouseExited(MouseEvent paramMouseEvent) {  }
/*     */ 
/* 108 */     public void mouseDragged(MouseEvent paramMouseEvent) { MenuSelectionManager.defaultManager().processMouseEvent(paramMouseEvent); }
/*     */ 
/*     */ 
/*     */     public void mouseMoved(MouseEvent paramMouseEvent)
/*     */     {
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.java.swing.plaf.motif.MotifRadioButtonMenuItemUI
 * JD-Core Version:    0.6.2
 */