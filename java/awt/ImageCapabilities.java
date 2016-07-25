/*    */ package java.awt;
/*    */ 
/*    */ public class ImageCapabilities
/*    */   implements Cloneable
/*    */ {
/* 35 */   private boolean accelerated = false;
/*    */ 
/*    */   public ImageCapabilities(boolean paramBoolean)
/*    */   {
/* 42 */     this.accelerated = paramBoolean;
/*    */   }
/*    */ 
/*    */   public boolean isAccelerated()
/*    */   {
/* 55 */     return this.accelerated;
/*    */   }
/*    */ 
/*    */   public boolean isTrueVolatile()
/*    */   {
/* 66 */     return false;
/*    */   }
/*    */ 
/*    */   public Object clone()
/*    */   {
/*    */     try
/*    */     {
/* 74 */       return super.clone();
/*    */     } catch (CloneNotSupportedException localCloneNotSupportedException) {
/*    */     }
/* 77 */     throw new InternalError();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.ImageCapabilities
 * JD-Core Version:    0.6.2
 */