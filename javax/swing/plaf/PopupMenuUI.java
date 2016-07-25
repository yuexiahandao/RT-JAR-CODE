/*    */ package javax.swing.plaf;
/*    */ 
/*    */ import java.awt.event.MouseEvent;
/*    */ import javax.swing.JPopupMenu;
/*    */ import javax.swing.Popup;
/*    */ import javax.swing.PopupFactory;
/*    */ 
/*    */ public abstract class PopupMenuUI extends ComponentUI
/*    */ {
/*    */   public boolean isPopupTrigger(MouseEvent paramMouseEvent)
/*    */   {
/* 45 */     return paramMouseEvent.isPopupTrigger();
/*    */   }
/*    */ 
/*    */   public Popup getPopup(JPopupMenu paramJPopupMenu, int paramInt1, int paramInt2)
/*    */   {
/* 59 */     PopupFactory localPopupFactory = PopupFactory.getSharedInstance();
/*    */ 
/* 61 */     return localPopupFactory.getPopup(paramJPopupMenu.getInvoker(), paramJPopupMenu, paramInt1, paramInt2);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.PopupMenuUI
 * JD-Core Version:    0.6.2
 */