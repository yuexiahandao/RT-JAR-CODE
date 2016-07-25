/*    */ package sun.reflect.misc;
/*    */ 
/*    */ import java.lang.reflect.InvocationTargetException;
/*    */ import java.lang.reflect.Method;
/*    */ import java.security.AccessController;
/*    */ 
/*    */ class Trampoline
/*    */ {
/*    */   private static void ensureInvocableMethod(Method paramMethod)
/*    */     throws InvocationTargetException
/*    */   {
/* 63 */     Class localClass = paramMethod.getDeclaringClass();
/* 64 */     if ((localClass.equals(AccessController.class)) || (localClass.equals(Method.class)) || (localClass.getName().startsWith("java.lang.invoke.")))
/*    */     {
/* 67 */       throw new InvocationTargetException(new UnsupportedOperationException("invocation not supported"));
/*    */     }
/*    */   }
/*    */ 
/*    */   private static Object invoke(Method paramMethod, Object paramObject, Object[] paramArrayOfObject)
/*    */     throws InvocationTargetException, IllegalAccessException
/*    */   {
/* 74 */     ensureInvocableMethod(paramMethod);
/* 75 */     return paramMethod.invoke(paramObject, paramArrayOfObject);
/*    */   }
/*    */ 
/*    */   static
/*    */   {
/* 54 */     if (Trampoline.class.getClassLoader() == null)
/* 55 */       throw new Error("Trampoline must not be defined by the bootstrap classloader");
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.reflect.misc.Trampoline
 * JD-Core Version:    0.6.2
 */