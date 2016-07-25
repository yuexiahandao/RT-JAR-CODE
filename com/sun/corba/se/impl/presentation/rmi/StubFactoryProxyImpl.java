/*    */ package com.sun.corba.se.impl.presentation.rmi;
/*    */ 
/*    */ import com.sun.corba.se.spi.orbutil.proxy.InvocationHandlerFactory;
/*    */ import com.sun.corba.se.spi.orbutil.proxy.LinkedInvocationHandler;
/*    */ import com.sun.corba.se.spi.presentation.rmi.DynamicStub;
/*    */ import com.sun.corba.se.spi.presentation.rmi.PresentationManager.ClassData;
/*    */ import java.lang.reflect.Proxy;
/*    */ import org.omg.CORBA.Object;
/*    */ 
/*    */ public class StubFactoryProxyImpl extends StubFactoryDynamicBase
/*    */ {
/*    */   public StubFactoryProxyImpl(PresentationManager.ClassData paramClassData, ClassLoader paramClassLoader)
/*    */   {
/* 41 */     super(paramClassData, paramClassLoader);
/*    */   }
/*    */ 
/*    */   public Object makeStub()
/*    */   {
/* 48 */     InvocationHandlerFactory localInvocationHandlerFactory = this.classData.getInvocationHandlerFactory();
/* 49 */     LinkedInvocationHandler localLinkedInvocationHandler = (LinkedInvocationHandler)localInvocationHandlerFactory.getInvocationHandler();
/*    */ 
/* 51 */     Class[] arrayOfClass = localInvocationHandlerFactory.getProxyInterfaces();
/* 52 */     DynamicStub localDynamicStub = (DynamicStub)Proxy.newProxyInstance(this.loader, arrayOfClass, localLinkedInvocationHandler);
/*    */ 
/* 54 */     localLinkedInvocationHandler.setProxy((Proxy)localDynamicStub);
/* 55 */     return localDynamicStub;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.presentation.rmi.StubFactoryProxyImpl
 * JD-Core Version:    0.6.2
 */