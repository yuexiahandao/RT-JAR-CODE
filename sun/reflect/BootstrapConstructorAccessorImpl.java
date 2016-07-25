/*    */ package sun.reflect;
/*    */ 
/*    */ import java.lang.reflect.Constructor;
/*    */ import java.lang.reflect.InvocationTargetException;
/*    */ import sun.misc.Unsafe;
/*    */ 
/*    */ class BootstrapConstructorAccessorImpl extends ConstructorAccessorImpl
/*    */ {
/*    */   private Constructor constructor;
/*    */ 
/*    */   BootstrapConstructorAccessorImpl(Constructor paramConstructor)
/*    */   {
/* 38 */     this.constructor = paramConstructor;
/*    */   }
/*    */ 
/*    */   public Object newInstance(Object[] paramArrayOfObject) throws IllegalArgumentException, InvocationTargetException
/*    */   {
/*    */     try
/*    */     {
/* 45 */       return UnsafeFieldAccessorImpl.unsafe.allocateInstance(this.constructor.getDeclaringClass());
/*    */     }
/*    */     catch (InstantiationException localInstantiationException) {
/* 48 */       throw new InvocationTargetException(localInstantiationException);
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.reflect.BootstrapConstructorAccessorImpl
 * JD-Core Version:    0.6.2
 */