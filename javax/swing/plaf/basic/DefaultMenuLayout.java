/*    */ package javax.swing.plaf.basic;
/*    */ 
/*    */ import java.awt.Container;
/*    */ import java.awt.Dimension;
/*    */ import javax.swing.BoxLayout;
/*    */ import javax.swing.JPopupMenu;
/*    */ import javax.swing.plaf.UIResource;
/*    */ import sun.swing.MenuItemLayoutHelper;
/*    */ 
/*    */ public class DefaultMenuLayout extends BoxLayout
/*    */   implements UIResource
/*    */ {
/*    */   public DefaultMenuLayout(Container paramContainer, int paramInt)
/*    */   {
/* 45 */     super(paramContainer, paramInt);
/*    */   }
/*    */ 
/*    */   public Dimension preferredLayoutSize(Container paramContainer) {
/* 49 */     if ((paramContainer instanceof JPopupMenu)) {
/* 50 */       JPopupMenu localJPopupMenu = (JPopupMenu)paramContainer;
/* 51 */       MenuItemLayoutHelper.clearUsedClientProperties(localJPopupMenu);
/* 52 */       if (localJPopupMenu.getComponentCount() == 0) {
/* 53 */         return new Dimension(0, 0);
/*    */       }
/*    */ 
/*    */     }
/*    */ 
/* 58 */     super.invalidateLayout(paramContainer);
/*    */ 
/* 60 */     return super.preferredLayoutSize(paramContainer);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.basic.DefaultMenuLayout
 * JD-Core Version:    0.6.2
 */