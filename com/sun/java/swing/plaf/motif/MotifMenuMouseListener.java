/*    */ package com.sun.java.swing.plaf.motif;
/*    */ 
/*    */ import java.awt.event.MouseAdapter;
/*    */ import java.awt.event.MouseEvent;
/*    */ import javax.swing.MenuSelectionManager;
/*    */ 
/*    */ class MotifMenuMouseListener extends MouseAdapter
/*    */ {
/*    */   public void mousePressed(MouseEvent paramMouseEvent)
/*    */   {
/* 37 */     MenuSelectionManager.defaultManager().processMouseEvent(paramMouseEvent);
/*    */   }
/*    */   public void mouseReleased(MouseEvent paramMouseEvent) {
/* 40 */     MenuSelectionManager.defaultManager().processMouseEvent(paramMouseEvent);
/*    */   }
/*    */   public void mouseEntered(MouseEvent paramMouseEvent) {
/* 43 */     MenuSelectionManager.defaultManager().processMouseEvent(paramMouseEvent);
/*    */   }
/*    */   public void mouseExited(MouseEvent paramMouseEvent) {
/* 46 */     MenuSelectionManager.defaultManager().processMouseEvent(paramMouseEvent);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.java.swing.plaf.motif.MotifMenuMouseListener
 * JD-Core Version:    0.6.2
 */