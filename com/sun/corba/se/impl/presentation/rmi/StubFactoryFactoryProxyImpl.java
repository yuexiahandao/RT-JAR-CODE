/*    */ package com.sun.corba.se.impl.presentation.rmi;
/*    */ 
/*    */ import com.sun.corba.se.spi.presentation.rmi.PresentationManager;
/*    */ import com.sun.corba.se.spi.presentation.rmi.PresentationManager.ClassData;
/*    */ import com.sun.corba.se.spi.presentation.rmi.PresentationManager.StubFactory;
/*    */ import java.security.AccessController;
/*    */ import java.security.PrivilegedAction;
/*    */ 
/*    */ public class StubFactoryFactoryProxyImpl extends StubFactoryFactoryDynamicBase
/*    */ {
/*    */   public PresentationManager.StubFactory makeDynamicStubFactory(PresentationManager paramPresentationManager, final PresentationManager.ClassData paramClassData, final ClassLoader paramClassLoader)
/*    */   {
/* 39 */     return (PresentationManager.StubFactory)AccessController.doPrivileged(new PrivilegedAction()
/*    */     {
/*    */       public StubFactoryProxyImpl run()
/*    */       {
/* 43 */         return new StubFactoryProxyImpl(paramClassData, paramClassLoader);
/*    */       }
/*    */     });
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.presentation.rmi.StubFactoryFactoryProxyImpl
 * JD-Core Version:    0.6.2
 */