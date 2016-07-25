/*    */ package com.sun.corba.se.spi.orbutil.proxy;
/*    */ 
/*    */ import com.sun.corba.se.impl.logging.ORBUtilSystemException;
/*    */ import com.sun.corba.se.impl.presentation.rmi.DynamicAccessPermission;
/*    */ import java.lang.reflect.InvocationHandler;
/*    */ import java.lang.reflect.Method;
/*    */ import java.util.LinkedHashMap;
/*    */ import java.util.Map;
/*    */ 
/*    */ public class CompositeInvocationHandlerImpl
/*    */   implements CompositeInvocationHandler
/*    */ {
/* 44 */   private Map classToInvocationHandler = new LinkedHashMap();
/* 45 */   private InvocationHandler defaultHandler = null;
/*    */ 
/* 85 */   private static final DynamicAccessPermission perm = new DynamicAccessPermission("access");
/*    */   private static final long serialVersionUID = 4571178305984833743L;
/*    */ 
/*    */   public void addInvocationHandler(Class paramClass, InvocationHandler paramInvocationHandler)
/*    */   {
/* 50 */     checkAccess();
/* 51 */     this.classToInvocationHandler.put(paramClass, paramInvocationHandler);
/*    */   }
/*    */ 
/*    */   public void setDefaultHandler(InvocationHandler paramInvocationHandler)
/*    */   {
/* 56 */     checkAccess();
/* 57 */     this.defaultHandler = paramInvocationHandler;
/*    */   }
/*    */ 
/*    */   public Object invoke(Object paramObject, Method paramMethod, Object[] paramArrayOfObject)
/*    */     throws Throwable
/*    */   {
/* 65 */     Class localClass = paramMethod.getDeclaringClass();
/* 66 */     InvocationHandler localInvocationHandler = (InvocationHandler)this.classToInvocationHandler.get(localClass);
/*    */ 
/* 69 */     if (localInvocationHandler == null) {
/* 70 */       if (this.defaultHandler != null) {
/* 71 */         localInvocationHandler = this.defaultHandler;
/*    */       } else {
/* 73 */         ORBUtilSystemException localORBUtilSystemException = ORBUtilSystemException.get("util");
/*    */ 
/* 75 */         throw localORBUtilSystemException.noInvocationHandler("\"" + paramMethod.toString() + "\"");
/*    */       }
/*    */ 
/*    */     }
/*    */ 
/* 82 */     return localInvocationHandler.invoke(paramObject, paramMethod, paramArrayOfObject);
/*    */   }
/*    */ 
/*    */   private void checkAccess()
/*    */   {
/* 87 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 88 */     if (localSecurityManager != null)
/* 89 */       localSecurityManager.checkPermission(perm);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.orbutil.proxy.CompositeInvocationHandlerImpl
 * JD-Core Version:    0.6.2
 */