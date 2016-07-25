/*     */ package com.sun.tracing;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.lang.reflect.Field;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedActionException;
/*     */ import java.security.PrivilegedExceptionAction;
/*     */ import java.util.HashSet;
/*     */ import sun.security.action.GetPropertyAction;
/*     */ import sun.tracing.MultiplexProviderFactory;
/*     */ import sun.tracing.NullProviderFactory;
/*     */ import sun.tracing.PrintStreamProviderFactory;
/*     */ import sun.tracing.dtrace.DTraceProviderFactory;
/*     */ 
/*     */ public abstract class ProviderFactory
/*     */ {
/*     */   public abstract <T extends Provider> T createProvider(Class<T> paramClass);
/*     */ 
/*     */   public static ProviderFactory getDefaultFactory()
/*     */   {
/*  55 */     HashSet localHashSet = new HashSet();
/*     */ 
/*  58 */     String str1 = (String)AccessController.doPrivileged(new GetPropertyAction("com.sun.tracing.dtrace"));
/*     */ 
/*  61 */     if (((str1 == null) || (!str1.equals("disable"))) && (DTraceProviderFactory.isSupported()))
/*     */     {
/*  63 */       localHashSet.add(new DTraceProviderFactory());
/*     */     }
/*     */ 
/*  67 */     str1 = (String)AccessController.doPrivileged(new GetPropertyAction("sun.tracing.stream"));
/*     */ 
/*  69 */     if (str1 != null) {
/*  70 */       for (String str2 : str1.split(",")) {
/*  71 */         PrintStream localPrintStream = getPrintStreamFromSpec(str2);
/*  72 */         if (localPrintStream != null) {
/*  73 */           localHashSet.add(new PrintStreamProviderFactory(localPrintStream));
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/*  80 */     if (localHashSet.size() == 0)
/*  81 */       return new NullProviderFactory();
/*  82 */     if (localHashSet.size() == 1) {
/*  83 */       return ((ProviderFactory[])localHashSet.toArray(new ProviderFactory[1]))[0];
/*     */     }
/*  85 */     return new MultiplexProviderFactory(localHashSet);
/*     */   }
/*     */ 
/*     */   private static PrintStream getPrintStreamFromSpec(final String paramString)
/*     */   {
/*     */     try
/*     */     {
/*  95 */       final int i = paramString.lastIndexOf('.');
/*  96 */       Class localClass = Class.forName(paramString.substring(0, i));
/*     */ 
/*  98 */       Field localField = (Field)AccessController.doPrivileged(new PrivilegedExceptionAction() {
/*     */         public Field run() throws NoSuchFieldException {
/* 100 */           return this.val$cls.getField(paramString.substring(i + 1));
/*     */         }
/*     */       });
/* 104 */       return (PrintStream)localField.get(null);
/*     */     } catch (ClassNotFoundException localClassNotFoundException) {
/* 106 */       throw new AssertionError(localClassNotFoundException);
/*     */     } catch (IllegalAccessException localIllegalAccessException) {
/* 108 */       throw new AssertionError(localIllegalAccessException);
/*     */     } catch (PrivilegedActionException localPrivilegedActionException) {
/* 110 */       throw new AssertionError(localPrivilegedActionException);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.tracing.ProviderFactory
 * JD-Core Version:    0.6.2
 */