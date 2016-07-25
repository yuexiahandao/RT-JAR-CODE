/*    */ package com.sun.corba.se.impl.presentation.rmi;
/*    */ 
/*    */ import com.sun.corba.se.impl.logging.ORBUtilSystemException;
/*    */ import com.sun.corba.se.spi.orb.ORB;
/*    */ import com.sun.corba.se.spi.presentation.rmi.PresentationManager;
/*    */ import com.sun.corba.se.spi.presentation.rmi.PresentationManager.ClassData;
/*    */ import com.sun.corba.se.spi.presentation.rmi.PresentationManager.StubFactory;
/*    */ import com.sun.corba.se.spi.presentation.rmi.PresentationManager.StubFactoryFactory;
/*    */ import java.rmi.Remote;
/*    */ import javax.rmi.CORBA.Tie;
/*    */ import javax.rmi.CORBA.Util;
/*    */ import org.omg.CORBA.CompletionStatus;
/*    */ import org.omg.CORBA.portable.IDLEntity;
/*    */ 
/*    */ public abstract class StubFactoryFactoryDynamicBase extends StubFactoryFactoryBase
/*    */ {
/*    */   protected final ORBUtilSystemException wrapper;
/*    */ 
/*    */   public StubFactoryFactoryDynamicBase()
/*    */   {
/* 53 */     this.wrapper = ORBUtilSystemException.get("rpc.presentation");
/*    */   }
/*    */ 
/*    */   public PresentationManager.StubFactory createStubFactory(String paramString1, boolean paramBoolean, String paramString2, Class paramClass, ClassLoader paramClassLoader)
/*    */   {
/* 61 */     Class localClass = null;
/*    */     try
/*    */     {
/* 64 */       localClass = Util.loadClass(paramString1, paramString2, paramClassLoader);
/*    */     } catch (ClassNotFoundException localClassNotFoundException) {
/* 66 */       throw this.wrapper.classNotFound3(CompletionStatus.COMPLETED_MAYBE, localClassNotFoundException, paramString1);
/*    */     }
/*    */ 
/* 70 */     PresentationManager localPresentationManager = ORB.getPresentationManager();
/*    */ 
/* 72 */     if ((IDLEntity.class.isAssignableFrom(localClass)) && (!Remote.class.isAssignableFrom(localClass)))
/*    */     {
/* 75 */       localObject = localPresentationManager.getStubFactoryFactory(false);
/*    */ 
/* 77 */       PresentationManager.StubFactory localStubFactory = ((PresentationManager.StubFactoryFactory)localObject).createStubFactory(paramString1, true, paramString2, paramClass, paramClassLoader);
/*    */ 
/* 80 */       return localStubFactory;
/*    */     }
/* 82 */     Object localObject = localPresentationManager.getClassData(localClass);
/* 83 */     return makeDynamicStubFactory(localPresentationManager, (PresentationManager.ClassData)localObject, paramClassLoader);
/*    */   }
/*    */ 
/*    */   public abstract PresentationManager.StubFactory makeDynamicStubFactory(PresentationManager paramPresentationManager, PresentationManager.ClassData paramClassData, ClassLoader paramClassLoader);
/*    */ 
/*    */   public Tie getTie(Class paramClass)
/*    */   {
/* 93 */     PresentationManager localPresentationManager = ORB.getPresentationManager();
/* 94 */     return new ReflectiveTie(localPresentationManager, this.wrapper);
/*    */   }
/*    */ 
/*    */   public boolean createsDynamicStubs()
/*    */   {
/* 99 */     return true;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.presentation.rmi.StubFactoryFactoryDynamicBase
 * JD-Core Version:    0.6.2
 */