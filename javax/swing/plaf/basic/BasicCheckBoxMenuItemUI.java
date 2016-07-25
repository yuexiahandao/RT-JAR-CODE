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
/*    */ public class BasicCheckBoxMenuItemUI extends BasicMenuItemUI
/*    */ {
/*    */   public static ComponentUI createUI(JComponent paramJComponent)
/*    */   {
/* 46 */     return new BasicCheckBoxMenuItemUI();
/*    */   }
/*    */ 
/*    */   protected String getPropertyPrefix() {
/* 50 */     return "CheckBoxMenuItem";
/*    */   }
/*    */ 
/*    */   public void processMouseEvent(JMenuItem paramJMenuItem, MouseEvent paramMouseEvent, MenuElement[] paramArrayOfMenuElement, MenuSelectionManager paramMenuSelectionManager) {
/* 54 */     Point localPoint = paramMouseEvent.getPoint();
/* 55 */     if ((localPoint.x >= 0) && (localPoint.x < paramJMenuItem.getWidth()) && (localPoint.y >= 0) && (localPoint.y < paramJMenuItem.getHeight()))
/*    */     {
/* 57 */       if (paramMouseEvent.getID() == 502) {
/* 58 */         paramMenuSelectionManager.clearSelectedPath();
/* 59 */         paramJMenuItem.doClick(0);
/*    */       } else {
/* 61 */         paramMenuSelectionManager.setSelectedPath(paramArrayOfMenuElement);
/*    */       } } else if (paramJMenuItem.getModel().isArmed()) {
/* 63 */       MenuElement[] arrayOfMenuElement = new MenuElement[paramArrayOfMenuElement.length - 1];
/*    */ 
/* 65 */       int i = 0; for (int j = paramArrayOfMenuElement.length - 1; i < j; i++)
/* 66 */         arrayOfMenuElement[i] = paramArrayOfMenuElement[i];
/* 67 */       paramMenuSelectionManager.setSelectedPath(arrayOfMenuElement);
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.basic.BasicCheckBoxMenuItemUI
 * JD-Core Version:    0.6.2
 */