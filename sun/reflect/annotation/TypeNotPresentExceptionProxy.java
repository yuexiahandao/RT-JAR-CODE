/*    */ package sun.reflect.annotation;
/*    */ 
/*    */ public class TypeNotPresentExceptionProxy extends ExceptionProxy
/*    */ {
/*    */   private static final long serialVersionUID = 5565925172427947573L;
/*    */   String typeName;
/*    */   Throwable cause;
/*    */ 
/*    */   public TypeNotPresentExceptionProxy(String paramString, Throwable paramThrowable)
/*    */   {
/* 41 */     this.typeName = paramString;
/* 42 */     this.cause = paramThrowable;
/*    */   }
/*    */ 
/*    */   protected RuntimeException generateException() {
/* 46 */     return new TypeNotPresentException(this.typeName, this.cause);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.reflect.annotation.TypeNotPresentExceptionProxy
 * JD-Core Version:    0.6.2
 */