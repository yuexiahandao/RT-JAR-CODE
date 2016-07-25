/*    */ package javax.swing.colorchooser;
/*    */ 
/*    */ import java.awt.Component;
/*    */ import java.awt.Container;
/*    */ import java.awt.Dimension;
/*    */ import java.awt.Insets;
/*    */ import java.awt.LayoutManager;
/*    */ import java.io.Serializable;
/*    */ 
/*    */ class CenterLayout
/*    */   implements LayoutManager, Serializable
/*    */ {
/*    */   public void addLayoutComponent(String paramString, Component paramComponent)
/*    */   {
/*    */   }
/*    */ 
/*    */   public void removeLayoutComponent(Component paramComponent)
/*    */   {
/*    */   }
/*    */ 
/*    */   public Dimension preferredLayoutSize(Container paramContainer)
/*    */   {
/* 42 */     Component localComponent = paramContainer.getComponent(0);
/* 43 */     if (localComponent != null) {
/* 44 */       Dimension localDimension = localComponent.getPreferredSize();
/* 45 */       Insets localInsets = paramContainer.getInsets();
/* 46 */       localDimension.width += localInsets.left + localInsets.right;
/* 47 */       localDimension.height += localInsets.top + localInsets.bottom;
/* 48 */       return localDimension;
/*    */     }
/*    */ 
/* 51 */     return new Dimension(0, 0);
/*    */   }
/*    */ 
/*    */   public Dimension minimumLayoutSize(Container paramContainer)
/*    */   {
/* 56 */     return preferredLayoutSize(paramContainer);
/*    */   }
/*    */ 
/*    */   public void layoutContainer(Container paramContainer) {
/*    */     try {
/* 61 */       Component localComponent = paramContainer.getComponent(0);
/*    */ 
/* 63 */       localComponent.setSize(localComponent.getPreferredSize());
/* 64 */       Dimension localDimension1 = localComponent.getSize();
/* 65 */       Dimension localDimension2 = paramContainer.getSize();
/* 66 */       Insets localInsets = paramContainer.getInsets();
/* 67 */       localDimension2.width -= localInsets.left + localInsets.right;
/* 68 */       localDimension2.height -= localInsets.top + localInsets.bottom;
/* 69 */       int i = localDimension2.width / 2 - localDimension1.width / 2;
/* 70 */       int j = localDimension2.height / 2 - localDimension1.height / 2;
/* 71 */       i += localInsets.left;
/* 72 */       j += localInsets.top;
/*    */ 
/* 74 */       localComponent.setBounds(i, j, localDimension1.width, localDimension1.height);
/*    */     }
/*    */     catch (Exception localException)
/*    */     {
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.colorchooser.CenterLayout
 * JD-Core Version:    0.6.2
 */