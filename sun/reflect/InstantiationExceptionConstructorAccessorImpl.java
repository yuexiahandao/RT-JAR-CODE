/*    */ package sun.reflect;
/*    */ 
/*    */ import java.lang.reflect.InvocationTargetException;
/*    */ 
/*    */ class InstantiationExceptionConstructorAccessorImpl extends ConstructorAccessorImpl
/*    */ {
/*    */   private String message;
/*    */ 
/*    */   InstantiationExceptionConstructorAccessorImpl(String paramString)
/*    */   {
/* 39 */     this.message = paramString;
/*    */   }
/*    */ 
/*    */   public Object newInstance(Object[] paramArrayOfObject)
/*    */     throws InstantiationException, IllegalArgumentException, InvocationTargetException
/*    */   {
/* 47 */     if (this.message == null) {
/* 48 */       throw new InstantiationException();
/*    */     }
/* 50 */     throw new InstantiationException(this.message);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.reflect.InstantiationExceptionConstructorAccessorImpl
 * JD-Core Version:    0.6.2
 */