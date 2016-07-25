/*    */ package com.sun.corba.se.impl.presentation.rmi;
/*    */ 
/*    */ import com.sun.corba.se.spi.presentation.rmi.PresentationManager.ClassData;
/*    */ import com.sun.corba.se.spi.presentation.rmi.PresentationManager.StubFactory;
/*    */ import com.sun.corba.se.spi.presentation.rmi.StubAdapter;
/*    */ 
/*    */ public abstract class StubFactoryBase
/*    */   implements PresentationManager.StubFactory
/*    */ {
/* 42 */   private String[] typeIds = null;
/*    */   protected final PresentationManager.ClassData classData;
/*    */ 
/*    */   protected StubFactoryBase(PresentationManager.ClassData paramClassData)
/*    */   {
/* 48 */     this.classData = paramClassData;
/*    */   }
/*    */ 
/*    */   public synchronized String[] getTypeIds()
/*    */   {
/* 53 */     if (this.typeIds == null) {
/* 54 */       if (this.classData == null) {
/* 55 */         org.omg.CORBA.Object localObject = makeStub();
/* 56 */         this.typeIds = StubAdapter.getTypeIds(localObject);
/*    */       } else {
/* 58 */         this.typeIds = this.classData.getTypeIds();
/*    */       }
/*    */     }
/*    */ 
/* 62 */     return this.typeIds;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.presentation.rmi.StubFactoryBase
 * JD-Core Version:    0.6.2
 */