/*    */ package java.util;
/*    */ 
/*    */ public class MissingFormatArgumentException extends IllegalFormatException
/*    */ {
/*    */   private static final long serialVersionUID = 19190115L;
/*    */   private String s;
/*    */ 
/*    */   public MissingFormatArgumentException(String paramString)
/*    */   {
/* 53 */     if (paramString == null)
/* 54 */       throw new NullPointerException();
/* 55 */     this.s = paramString;
/*    */   }
/*    */ 
/*    */   public String getFormatSpecifier()
/*    */   {
/* 64 */     return this.s;
/*    */   }
/*    */ 
/*    */   public String getMessage() {
/* 68 */     return "Format specifier '" + this.s + "'";
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.MissingFormatArgumentException
 * JD-Core Version:    0.6.2
 */