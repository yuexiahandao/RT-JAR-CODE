/*    */ package java.lang;
/*    */ 
/*    */ public class TypeNotPresentException extends RuntimeException
/*    */ {
/*    */   private static final long serialVersionUID = -5101214195716534496L;
/*    */   private String typeName;
/*    */ 
/*    */   public TypeNotPresentException(String paramString, Throwable paramThrowable)
/*    */   {
/* 60 */     super("Type " + paramString + " not present", paramThrowable);
/* 61 */     this.typeName = paramString;
/*    */   }
/*    */ 
/*    */   public String typeName()
/*    */   {
/* 69 */     return this.typeName;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.lang.TypeNotPresentException
 * JD-Core Version:    0.6.2
 */