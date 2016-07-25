/*    */ package java.awt;
/*    */ 
/*    */ public class HeadlessException extends UnsupportedOperationException
/*    */ {
/*    */   private static final long serialVersionUID = 167183644944358563L;
/*    */ 
/*    */   public HeadlessException()
/*    */   {
/*    */   }
/*    */ 
/*    */   public HeadlessException(String paramString)
/*    */   {
/* 43 */     super(paramString);
/*    */   }
/*    */   public String getMessage() {
/* 46 */     String str1 = super.getMessage();
/* 47 */     String str2 = GraphicsEnvironment.getHeadlessMessage();
/*    */ 
/* 49 */     if (str1 == null)
/* 50 */       return str2;
/* 51 */     if (str2 == null) {
/* 52 */       return str1;
/*    */     }
/* 54 */     return str1 + str2;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.HeadlessException
 * JD-Core Version:    0.6.2
 */