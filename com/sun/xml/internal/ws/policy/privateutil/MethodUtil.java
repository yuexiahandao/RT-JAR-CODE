/*    */ package com.sun.xml.internal.ws.policy.privateutil;
/*    */ 
/*    */ import java.lang.reflect.InvocationTargetException;
/*    */ import java.lang.reflect.Method;
/*    */ import java.util.logging.Level;
/*    */ import java.util.logging.Logger;
/*    */ 
/*    */ class MethodUtil
/*    */ {
/* 39 */   private static final Logger LOGGER = Logger.getLogger(MethodUtil.class.getName());
/*    */ 
/* 56 */   private static final Method INVOKE_METHOD = method;
/*    */ 
/*    */   static Object invoke(Object target, Method method, Object[] args) throws IllegalAccessException, InvocationTargetException
/*    */   {
/* 60 */     if (INVOKE_METHOD != null)
/*    */     {
/* 62 */       if (LOGGER.isLoggable(Level.FINE))
/* 63 */         LOGGER.log(Level.FINE, "Invoking method using sun.reflect.misc.MethodUtil");
/*    */       try
/*    */       {
/* 66 */         return INVOKE_METHOD.invoke(null, new Object[] { method, target, args });
/*    */       }
/*    */       catch (InvocationTargetException ite) {
/* 69 */         throw unwrapException(ite);
/*    */       }
/*    */     }
/*    */ 
/* 73 */     if (LOGGER.isLoggable(Level.FINE)) {
/* 74 */       LOGGER.log(Level.FINE, "Invoking method directly, probably non-Oracle JVM");
/*    */     }
/* 76 */     return method.invoke(target, args);
/*    */   }
/*    */ 
/*    */   private static InvocationTargetException unwrapException(InvocationTargetException ite)
/*    */   {
/* 81 */     Throwable targetException = ite.getTargetException();
/* 82 */     if ((targetException != null) && ((targetException instanceof InvocationTargetException))) {
/* 83 */       if (LOGGER.isLoggable(Level.FINE)) {
/* 84 */         LOGGER.log(Level.FINE, "Unwrapping invocation target exception");
/*    */       }
/* 86 */       return (InvocationTargetException)targetException;
/*    */     }
/* 88 */     return ite;
/*    */   }
/*    */ 
/*    */   static
/*    */   {
/*    */     Method method;
/*    */     try
/*    */     {
/* 45 */       Class clazz = Class.forName("sun.reflect.misc.MethodUtil");
/* 46 */       method = clazz.getMethod("invoke", new Class[] { Method.class, Object.class, [Ljava.lang.Object.class });
/* 47 */       if (LOGGER.isLoggable(Level.FINE))
/* 48 */         LOGGER.log(Level.FINE, "Class sun.reflect.misc.MethodUtil found; it will be used to invoke methods.");
/*    */     }
/*    */     catch (Throwable t) {
/* 51 */       method = null;
/* 52 */       if (LOGGER.isLoggable(Level.FINE))
/* 53 */         LOGGER.log(Level.FINE, "Class sun.reflect.misc.MethodUtil not found, probably non-Oracle JVM");
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.policy.privateutil.MethodUtil
 * JD-Core Version:    0.6.2
 */