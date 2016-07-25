/*    */ package com.sun.corba.se.impl.presentation.rmi;
/*    */ 
/*    */ import com.sun.corba.se.spi.presentation.rmi.PresentationManager.ClassData;
/*    */ import java.io.SerializablePermission;
/*    */ import org.omg.CORBA.Object;
/*    */ 
/*    */ public abstract class StubFactoryDynamicBase extends StubFactoryBase
/*    */ {
/*    */   protected final ClassLoader loader;
/*    */ 
/*    */   private static Void checkPermission()
/*    */   {
/* 43 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 44 */     if (localSecurityManager != null) {
/* 45 */       localSecurityManager.checkPermission(new SerializablePermission("enableSubclassImplementation"));
/*    */     }
/*    */ 
/* 48 */     return null;
/*    */   }
/*    */ 
/*    */   private StubFactoryDynamicBase(Void paramVoid, PresentationManager.ClassData paramClassData, ClassLoader paramClassLoader)
/*    */   {
/* 53 */     super(paramClassData);
/*    */ 
/* 56 */     if (paramClassLoader == null) {
/* 57 */       ClassLoader localClassLoader = Thread.currentThread().getContextClassLoader();
/* 58 */       if (localClassLoader == null)
/* 59 */         localClassLoader = ClassLoader.getSystemClassLoader();
/* 60 */       this.loader = localClassLoader;
/*    */     } else {
/* 62 */       this.loader = paramClassLoader;
/*    */     }
/*    */   }
/*    */ 
/*    */   public StubFactoryDynamicBase(PresentationManager.ClassData paramClassData, ClassLoader paramClassLoader)
/*    */   {
/* 69 */     this(checkPermission(), paramClassData, paramClassLoader);
/*    */   }
/*    */ 
/*    */   public abstract Object makeStub();
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.presentation.rmi.StubFactoryDynamicBase
 * JD-Core Version:    0.6.2
 */