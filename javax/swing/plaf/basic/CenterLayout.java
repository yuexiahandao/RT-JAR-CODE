/*    */ package javax.swing.plaf.basic;
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
/*    */ 
/* 47 */       return new Dimension(localDimension.width + localInsets.left + localInsets.right, localDimension.height + localInsets.top + localInsets.bottom);
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
/* 60 */     if (paramContainer.getComponentCount() > 0) {
/* 61 */       Component localComponent = paramContainer.getComponent(0);
/* 62 */       Dimension localDimension = localComponent.getPreferredSize();
/* 63 */       int i = paramContainer.getWidth();
/* 64 */       int j = paramContainer.getHeight();
/* 65 */       Insets localInsets = paramContainer.getInsets();
/*    */ 
/* 67 */       i -= localInsets.left + localInsets.right;
/*    */ 
/* 69 */       j -= localInsets.top + localInsets.bottom;
/*    */ 
/* 72 */       int k = (i - localDimension.width) / 2 + localInsets.left;
/*    */ 
/* 74 */       int m = (j - localDimension.height) / 2 + localInsets.top;
/*    */ 
/* 77 */       localComponent.setBounds(k, m, localDimension.width, localDimension.height);
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.basic.CenterLayout
 * JD-Core Version:    0.6.2
 */