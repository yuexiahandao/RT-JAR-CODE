/*    */ package javax.swing.plaf;
/*    */ 
/*    */ import java.awt.Component;
/*    */ import java.awt.Graphics;
/*    */ import java.io.Serializable;
/*    */ import javax.swing.Icon;
/*    */ 
/*    */ public class IconUIResource
/*    */   implements Icon, UIResource, Serializable
/*    */ {
/*    */   private Icon delegate;
/*    */ 
/*    */   public IconUIResource(Icon paramIcon)
/*    */   {
/* 65 */     if (paramIcon == null) {
/* 66 */       throw new IllegalArgumentException("null delegate icon argument");
/*    */     }
/* 68 */     this.delegate = paramIcon;
/*    */   }
/*    */ 
/*    */   public void paintIcon(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2) {
/* 72 */     this.delegate.paintIcon(paramComponent, paramGraphics, paramInt1, paramInt2);
/*    */   }
/*    */ 
/*    */   public int getIconWidth() {
/* 76 */     return this.delegate.getIconWidth();
/*    */   }
/*    */ 
/*    */   public int getIconHeight() {
/* 80 */     return this.delegate.getIconHeight();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.IconUIResource
 * JD-Core Version:    0.6.2
 */