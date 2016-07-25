/*    */ package com.sun.corba.se.spi.orbutil.proxy;
/*    */ 
/*    */ import com.sun.corba.se.impl.presentation.rmi.DynamicAccessPermission;
/*    */ import java.lang.reflect.InvocationHandler;
/*    */ import java.lang.reflect.InvocationTargetException;
/*    */ import java.lang.reflect.Method;
/*    */ 
/*    */ public abstract class DelegateInvocationHandlerImpl
/*    */ {
/*    */   public static InvocationHandler create(Object paramObject)
/*    */   {
/* 45 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 46 */     if (localSecurityManager != null) {
/* 47 */       localSecurityManager.checkPermission(new DynamicAccessPermission("access"));
/*    */     }
/* 49 */     return new InvocationHandler()
/*    */     {
/*    */       public Object invoke(Object paramAnonymousObject, Method paramAnonymousMethod, Object[] paramAnonymousArrayOfObject)
/*    */         throws Throwable
/*    */       {
/*    */         try
/*    */         {
/* 56 */           return paramAnonymousMethod.invoke(this.val$delegate, paramAnonymousArrayOfObject);
/*    */         }
/*    */         catch (InvocationTargetException localInvocationTargetException)
/*    */         {
/* 60 */           throw localInvocationTargetException.getCause();
/*    */         }
/*    */       }
/*    */     };
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.orbutil.proxy.DelegateInvocationHandlerImpl
 * JD-Core Version:    0.6.2
 */