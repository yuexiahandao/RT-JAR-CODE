/*    */ package java.util;
/*    */ 
/*    */ public class MissingFormatWidthException extends IllegalFormatException
/*    */ {
/*    */   private static final long serialVersionUID = 15560123L;
/*    */   private String s;
/*    */ 
/*    */   public MissingFormatWidthException(String paramString)
/*    */   {
/* 51 */     if (paramString == null)
/* 52 */       throw new NullPointerException();
/* 53 */     this.s = paramString;
/*    */   }
/*    */ 
/*    */   public String getFormatSpecifier()
/*    */   {
/* 62 */     return this.s;
/*    */   }
/*    */ 
/*    */   public String getMessage() {
/* 66 */     return this.s;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.MissingFormatWidthException
 * JD-Core Version:    0.6.2
 */