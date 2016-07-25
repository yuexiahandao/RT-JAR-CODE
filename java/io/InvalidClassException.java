/*    */ package java.io;
/*    */ 
/*    */ public class InvalidClassException extends ObjectStreamException
/*    */ {
/*    */   private static final long serialVersionUID = -4333316296251054416L;
/*    */   public String classname;
/*    */ 
/*    */   public InvalidClassException(String paramString)
/*    */   {
/* 58 */     super(paramString);
/*    */   }
/*    */ 
/*    */   public InvalidClassException(String paramString1, String paramString2)
/*    */   {
/* 68 */     super(paramString2);
/* 69 */     this.classname = paramString1;
/*    */   }
/*    */ 
/*    */   public String getMessage()
/*    */   {
/* 76 */     if (this.classname == null) {
/* 77 */       return super.getMessage();
/*    */     }
/* 79 */     return this.classname + "; " + super.getMessage();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.io.InvalidClassException
 * JD-Core Version:    0.6.2
 */