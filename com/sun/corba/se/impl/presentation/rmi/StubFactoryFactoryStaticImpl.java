/*     */ package com.sun.corba.se.impl.presentation.rmi;
/*     */ 
/*     */ import com.sun.corba.se.impl.logging.ORBUtilSystemException;
/*     */ import com.sun.corba.se.impl.util.PackagePrefixChecker;
/*     */ import com.sun.corba.se.impl.util.Utility;
/*     */ import com.sun.corba.se.spi.presentation.rmi.PresentationManager.StubFactory;
/*     */ import javax.rmi.CORBA.Tie;
/*     */ import javax.rmi.CORBA.Util;
/*     */ import org.omg.CORBA.CompletionStatus;
/*     */ 
/*     */ public class StubFactoryFactoryStaticImpl extends StubFactoryFactoryBase
/*     */ {
/*  45 */   private ORBUtilSystemException wrapper = ORBUtilSystemException.get("rpc.presentation");
/*     */ 
/*     */   public PresentationManager.StubFactory createStubFactory(String paramString1, boolean paramBoolean, String paramString2, Class paramClass, ClassLoader paramClassLoader)
/*     */   {
/*  52 */     String str1 = null;
/*     */ 
/*  54 */     if (paramBoolean)
/*  55 */       str1 = Utility.idlStubName(paramString1);
/*     */     else {
/*  57 */       str1 = Utility.stubNameForCompiler(paramString1);
/*     */     }
/*  59 */     ClassLoader localClassLoader1 = paramClass == null ? paramClassLoader : paramClass.getClassLoader();
/*     */ 
/*  70 */     String str2 = str1;
/*  71 */     String str3 = str1;
/*     */ 
/*  73 */     if (PackagePrefixChecker.hasOffendingPrefix(str1))
/*  74 */       str2 = PackagePrefixChecker.packagePrefix() + str1;
/*     */     else {
/*  76 */       str3 = PackagePrefixChecker.packagePrefix() + str1;
/*     */     }
/*  78 */     Class localClass = null;
/*     */     try
/*     */     {
/*  81 */       localClass = Util.loadClass(str2, paramString2, localClassLoader1);
/*     */     }
/*     */     catch (ClassNotFoundException localClassNotFoundException1)
/*     */     {
/*  85 */       this.wrapper.classNotFound1(CompletionStatus.COMPLETED_MAYBE, localClassNotFoundException1, str2);
/*     */       try
/*     */       {
/*  88 */         localClass = Util.loadClass(str3, paramString2, localClassLoader1);
/*     */       }
/*     */       catch (ClassNotFoundException localClassNotFoundException2) {
/*  91 */         throw this.wrapper.classNotFound2(CompletionStatus.COMPLETED_MAYBE, localClassNotFoundException2, str3);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 100 */     if ((localClass == null) || ((paramClass != null) && (!paramClass.isAssignableFrom(localClass)))) {
/*     */       try
/*     */       {
/* 103 */         ClassLoader localClassLoader2 = Thread.currentThread().getContextClassLoader();
/* 104 */         if (localClassLoader2 == null) {
/* 105 */           localClassLoader2 = ClassLoader.getSystemClassLoader();
/*     */         }
/* 107 */         localClass = localClassLoader2.loadClass(paramString1);
/*     */       }
/*     */       catch (Exception localException) {
/* 110 */         IllegalStateException localIllegalStateException = new IllegalStateException("Could not load class " + str1);
/*     */ 
/* 112 */         localIllegalStateException.initCause(localException);
/* 113 */         throw localIllegalStateException;
/*     */       }
/*     */     }
/*     */ 
/* 117 */     return new StubFactoryStaticImpl(localClass);
/*     */   }
/*     */ 
/*     */   public Tie getTie(Class paramClass)
/*     */   {
/* 122 */     Class localClass = null;
/* 123 */     String str = Utility.tieName(paramClass.getName());
/*     */     try
/*     */     {
/* 130 */       localClass = Utility.loadClassForClass(str, Util.getCodebase(paramClass), null, paramClass, paramClass.getClassLoader());
/*     */ 
/* 132 */       return (Tie)localClass.newInstance();
/*     */     } catch (Exception localException1) {
/* 134 */       localClass = Utility.loadClassForClass(PackagePrefixChecker.packagePrefix() + str, Util.getCodebase(paramClass), null, paramClass, paramClass.getClassLoader());
/*     */ 
/* 137 */       return (Tie)localClass.newInstance();
/*     */     } catch (Exception localException2) {
/*     */     }
/* 140 */     return null;
/*     */   }
/*     */ 
/*     */   public boolean createsDynamicStubs()
/*     */   {
/* 147 */     return false;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.presentation.rmi.StubFactoryFactoryStaticImpl
 * JD-Core Version:    0.6.2
 */