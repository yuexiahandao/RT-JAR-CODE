/*    */ package com.sun.corba.se.spi.presentation.rmi;
/*    */ 
/*    */ import com.sun.corba.se.impl.presentation.rmi.StubFactoryFactoryProxyImpl;
/*    */ import com.sun.corba.se.impl.presentation.rmi.StubFactoryFactoryStaticImpl;
/*    */ import com.sun.corba.se.impl.presentation.rmi.StubFactoryStaticImpl;
/*    */ 
/*    */ public abstract class PresentationDefaults
/*    */ {
/* 38 */   private static StubFactoryFactoryStaticImpl staticImpl = null;
/*    */ 
/*    */   public static synchronized PresentationManager.StubFactoryFactory getStaticStubFactoryFactory()
/*    */   {
/* 45 */     if (staticImpl == null) {
/* 46 */       staticImpl = new StubFactoryFactoryStaticImpl();
/*    */     }
/* 48 */     return staticImpl;
/*    */   }
/*    */ 
/*    */   public static PresentationManager.StubFactoryFactory getProxyStubFactoryFactory()
/*    */   {
/* 54 */     return new StubFactoryFactoryProxyImpl();
/*    */   }
/*    */ 
/*    */   public static PresentationManager.StubFactory makeStaticStubFactory(Class paramClass)
/*    */   {
/* 60 */     return new StubFactoryStaticImpl(paramClass);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.presentation.rmi.PresentationDefaults
 * JD-Core Version:    0.6.2
 */