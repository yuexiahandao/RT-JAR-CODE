/*    */ package com.sun.corba.se.impl.presentation.rmi;
/*    */ 
/*    */ import org.omg.CORBA.Object;
/*    */ 
/*    */ public class StubFactoryStaticImpl extends StubFactoryBase
/*    */ {
/*    */   private Class stubClass;
/*    */ 
/*    */   public StubFactoryStaticImpl(Class paramClass)
/*    */   {
/* 38 */     super(null);
/* 39 */     this.stubClass = paramClass;
/*    */   }
/*    */ 
/*    */   public Object makeStub()
/*    */   {
/* 44 */     Object localObject = null;
/*    */     try {
/* 46 */       localObject = (Object)this.stubClass.newInstance();
/*    */     } catch (InstantiationException localInstantiationException) {
/* 48 */       throw new RuntimeException(localInstantiationException);
/*    */     } catch (IllegalAccessException localIllegalAccessException) {
/* 50 */       throw new RuntimeException(localIllegalAccessException);
/*    */     }
/* 52 */     return localObject;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.presentation.rmi.StubFactoryStaticImpl
 * JD-Core Version:    0.6.2
 */