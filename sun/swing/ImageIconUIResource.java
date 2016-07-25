/*    */ package sun.swing;
/*    */ 
/*    */ import java.awt.Image;
/*    */ import javax.swing.ImageIcon;
/*    */ import javax.swing.plaf.UIResource;
/*    */ 
/*    */ public class ImageIconUIResource extends ImageIcon
/*    */   implements UIResource
/*    */ {
/*    */   public ImageIconUIResource(byte[] paramArrayOfByte)
/*    */   {
/* 47 */     super(paramArrayOfByte);
/*    */   }
/*    */ 
/*    */   public ImageIconUIResource(Image paramImage)
/*    */   {
/* 57 */     super(paramImage);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.swing.ImageIconUIResource
 * JD-Core Version:    0.6.2
 */