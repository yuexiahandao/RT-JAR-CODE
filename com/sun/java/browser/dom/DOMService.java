/*    */ package com.sun.java.browser.dom;
/*    */ 
/*    */ import java.security.AccessController;
/*    */ import sun.security.action.GetPropertyAction;
/*    */ 
/*    */ public abstract class DOMService
/*    */ {
/*    */   public static DOMService getService(Object paramObject)
/*    */     throws DOMUnsupportedException
/*    */   {
/*    */     try
/*    */     {
/* 46 */       String str = (String)AccessController.doPrivileged(new GetPropertyAction("com.sun.java.browser.dom.DOMServiceProvider"));
/*    */ 
/* 49 */       DOMService.class; Class localClass = Class.forName("sun.plugin.dom.DOMService");
/*    */ 
/* 51 */       return (DOMService)localClass.newInstance();
/*    */     }
/*    */     catch (Throwable localThrowable)
/*    */     {
/* 55 */       throw new DOMUnsupportedException(localThrowable.toString());
/*    */     }
/*    */   }
/*    */ 
/*    */   public abstract Object invokeAndWait(DOMAction paramDOMAction)
/*    */     throws DOMAccessException;
/*    */ 
/*    */   public abstract void invokeLater(DOMAction paramDOMAction);
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.java.browser.dom.DOMService
 * JD-Core Version:    0.6.2
 */