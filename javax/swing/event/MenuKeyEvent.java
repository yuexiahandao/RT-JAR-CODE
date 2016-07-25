/*    */ package javax.swing.event;
/*    */ 
/*    */ import java.awt.Component;
/*    */ import java.awt.event.KeyEvent;
/*    */ import javax.swing.MenuElement;
/*    */ import javax.swing.MenuSelectionManager;
/*    */ 
/*    */ public class MenuKeyEvent extends KeyEvent
/*    */ {
/*    */   private MenuElement[] path;
/*    */   private MenuSelectionManager manager;
/*    */ 
/*    */   public MenuKeyEvent(Component paramComponent, int paramInt1, long paramLong, int paramInt2, int paramInt3, char paramChar, MenuElement[] paramArrayOfMenuElement, MenuSelectionManager paramMenuSelectionManager)
/*    */   {
/* 74 */     super(paramComponent, paramInt1, paramLong, paramInt2, paramInt3, paramChar);
/* 75 */     this.path = paramArrayOfMenuElement;
/* 76 */     this.manager = paramMenuSelectionManager;
/*    */   }
/*    */ 
/*    */   public MenuElement[] getPath()
/*    */   {
/* 85 */     return this.path;
/*    */   }
/*    */ 
/*    */   public MenuSelectionManager getMenuSelectionManager()
/*    */   {
/* 94 */     return this.manager;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.event.MenuKeyEvent
 * JD-Core Version:    0.6.2
 */