/*    */ package com.sun.xml.internal.ws.server;
/*    */ 
/*    */ import java.lang.reflect.InvocationTargetException;
/*    */ import java.lang.reflect.Method;
/*    */ import java.util.logging.Level;
/*    */ import java.util.logging.Logger;
/*    */ 
/*    */ class MethodUtil
/*    */ {
/* 41 */   private static final Logger LOGGER = Logger.getLogger(MethodUtil.class.getName());
/*    */ 
/* 58 */   private static final Method INVOKE_METHOD = method;
/*    */ 
/*    */   static Object invoke(Object target, Method method, Object[] args) throws IllegalAccessException, InvocationTargetException
/*    */   {
/* 62 */     if (INVOKE_METHOD != null)
/*    */     {
/* 64 */       if (LOGGER.isLoggable(Level.FINE))
/* 65 */         LOGGER.log(Level.FINE, "Invoking method using sun.reflect.misc.MethodUtil");
/*    */       try
/*    */       {
/* 68 */         return INVOKE_METHOD.invoke(null, new Object[] { method, target, args });
/*    */       }
/*    */       catch (InvocationTargetException ite) {
/* 71 */         throw unwrapException(ite);
/*    */       }
/*    */     }
/*    */ 
/* 75 */     if (LOGGER.isLoggable(Level.FINE)) {
/* 76 */       LOGGER.log(Level.FINE, "Invoking method directly, probably non-Oracle JVM");
/*    */     }
/* 78 */     return method.invoke(target, args);
/*    */   }
/*    */ 
/*    */   private static InvocationTargetException unwrapException(InvocationTargetException ite)
/*    */   {
/* 83 */     Throwable targetException = ite.getTargetException();
/* 84 */     if ((targetException != null) && ((targetException instanceof InvocationTargetException))) {
/* 85 */       if (LOGGER.isLoggable(Level.FINE)) {
/* 86 */         LOGGER.log(Level.FINE, "Unwrapping invocation target exception");
/*    */       }
/* 88 */       return (InvocationTargetException)targetException;
/*    */     }
/* 90 */     return ite;
/*    */   }
/*    */ 
/*    */   static
/*    */   {
/*    */     Method method;
/*    */     try
/*    */     {
/* 47 */       Class clazz = Class.forName("sun.reflect.misc.MethodUtil");
/* 48 */       method = clazz.getMethod("invoke", new Class[] { Method.class, Object.class, [Ljava.lang.Object.class });
/* 49 */       if (LOGGER.isLoggable(Level.FINE))
/* 50 */         LOGGER.log(Level.FINE, "Class sun.reflect.misc.MethodUtil found; it will be used to invoke methods.");
/*    */     }
/*    */     catch (Throwable t) {
/* 53 */       method = null;
/* 54 */       if (LOGGER.isLoggable(Level.FINE))
/* 55 */         LOGGER.log(Level.FINE, "Class sun.reflect.misc.MethodUtil not found, probably non-Oracle JVM");
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.server.MethodUtil
 * JD-Core Version:    0.6.2
 */