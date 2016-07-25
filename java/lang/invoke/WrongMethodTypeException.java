/*    */ package java.lang.invoke;
/*    */ 
/*    */ public class WrongMethodTypeException extends RuntimeException
/*    */ {
/*    */   private static final long serialVersionUID = 292L;
/*    */ 
/*    */   public WrongMethodTypeException()
/*    */   {
/*    */   }
/*    */ 
/*    */   public WrongMethodTypeException(String paramString)
/*    */   {
/* 60 */     super(paramString);
/*    */   }
/*    */ 
/*    */   WrongMethodTypeException(String paramString, Throwable paramThrowable)
/*    */   {
/* 72 */     super(paramString, paramThrowable);
/*    */   }
/*    */ 
/*    */   WrongMethodTypeException(Throwable paramThrowable)
/*    */   {
/* 83 */     super(paramThrowable);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.lang.invoke.WrongMethodTypeException
 * JD-Core Version:    0.6.2
 */