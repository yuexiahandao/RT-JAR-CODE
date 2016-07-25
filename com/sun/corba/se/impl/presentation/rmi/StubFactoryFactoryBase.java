/*    */ package com.sun.corba.se.impl.presentation.rmi;
/*    */ 
/*    */ import com.sun.corba.se.impl.util.Utility;
/*    */ import com.sun.corba.se.spi.presentation.rmi.PresentationManager.StubFactoryFactory;
/*    */ 
/*    */ public abstract class StubFactoryFactoryBase
/*    */   implements PresentationManager.StubFactoryFactory
/*    */ {
/*    */   public String getStubName(String paramString)
/*    */   {
/* 42 */     return Utility.stubName(paramString);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.presentation.rmi.StubFactoryFactoryBase
 * JD-Core Version:    0.6.2
 */