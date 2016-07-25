/*    */ package javax.swing.plaf.basic;
/*    */ 
/*    */ import java.awt.Point;
/*    */ import java.awt.event.MouseEvent;
/*    */ import javax.swing.ButtonModel;
/*    */ import javax.swing.JComponent;
/*    */ import javax.swing.JMenuItem;
/*    */ import javax.swing.MenuElement;
/*    */ import javax.swing.MenuSelectionManager;
/*    */ import javax.swing.plaf.ComponentUI;
/*    */ 
/*    */ public class BasicRadioButtonMenuItemUI extends BasicMenuItemUI
/*    */ {
/*    */   public static ComponentUI createUI(JComponent paramJComponent)
/*    */   {
/* 43 */     return new BasicRadioButtonMenuItemUI();
/*    */   }
/*    */ 
/*    */   protected String getPropertyPrefix() {
/* 47 */     return "RadioButtonMenuItem";
/*    */   }
/*    */ 
/*    */   public void processMouseEvent(JMenuItem paramJMenuItem, MouseEvent paramMouseEvent, MenuElement[] paramArrayOfMenuElement, MenuSelectionManager paramMenuSelectionManager) {
/* 51 */     Point localPoint = paramMouseEvent.getPoint();
/* 52 */     if ((localPoint.x >= 0) && (localPoint.x < paramJMenuItem.getWidth()) && (localPoint.y >= 0) && (localPoint.y < paramJMenuItem.getHeight()))
/*    */     {
/* 54 */       if (paramMouseEvent.getID() == 502) {
/* 55 */         paramMenuSelectionManager.clearSelectedPath();
/* 56 */         paramJMenuItem.doClick(0);
/* 57 */         paramJMenuItem.setArmed(false);
/*    */       } else {
/* 59 */         paramMenuSelectionManager.setSelectedPath(paramArrayOfMenuElement);
/*    */       } } else if (paramJMenuItem.getModel().isArmed()) {
/* 61 */       MenuElement[] arrayOfMenuElement = new MenuElement[paramArrayOfMenuElement.length - 1];
/*    */ 
/* 63 */       int i = 0; for (int j = paramArrayOfMenuElement.length - 1; i < j; i++)
/* 64 */         arrayOfMenuElement[i] = paramArrayOfMenuElement[i];
/* 65 */       paramMenuSelectionManager.setSelectedPath(arrayOfMenuElement);
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.basic.BasicRadioButtonMenuItemUI
 * JD-Core Version:    0.6.2
 */