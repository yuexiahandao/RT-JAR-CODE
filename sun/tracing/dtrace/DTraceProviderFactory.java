/*     */ package sun.tracing.dtrace;
/*     */ 
/*     */ import com.sun.tracing.Provider;
/*     */ import com.sun.tracing.ProviderFactory;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ 
/*     */ public final class DTraceProviderFactory extends ProviderFactory
/*     */ {
/*     */   public <T extends Provider> T createProvider(Class<T> paramClass)
/*     */   {
/*  79 */     DTraceProvider localDTraceProvider = new DTraceProvider(paramClass);
/*  80 */     Provider localProvider = localDTraceProvider.newProxyInstance();
/*  81 */     localDTraceProvider.setProxy(localProvider);
/*  82 */     localDTraceProvider.init();
/*  83 */     new Activation(localDTraceProvider.getModuleName(), new DTraceProvider[] { localDTraceProvider });
/*  84 */     return localProvider;
/*     */   }
/*     */ 
/*     */   public Map<Class<? extends Provider>, Provider> createProviders(Set<Class<? extends Provider>> paramSet, String paramString)
/*     */   {
/* 121 */     HashMap localHashMap = new HashMap();
/*     */ 
/* 123 */     HashSet localHashSet = new HashSet();
/* 124 */     for (Class localClass : paramSet) {
/* 125 */       DTraceProvider localDTraceProvider = new DTraceProvider(localClass);
/* 126 */       localHashSet.add(localDTraceProvider);
/* 127 */       localHashMap.put(localClass, localDTraceProvider.newProxyInstance());
/*     */     }
/* 129 */     new Activation(paramString, (DTraceProvider[])localHashSet.toArray(new DTraceProvider[0]));
/* 130 */     return localHashMap;
/*     */   }
/*     */ 
/*     */   public static boolean isSupported()
/*     */   {
/*     */     try
/*     */     {
/* 145 */       SecurityManager localSecurityManager = System.getSecurityManager();
/* 146 */       if (localSecurityManager != null) {
/* 147 */         RuntimePermission localRuntimePermission = new RuntimePermission("com.sun.tracing.dtrace.createProvider");
/*     */ 
/* 149 */         localSecurityManager.checkPermission(localRuntimePermission);
/*     */       }
/* 151 */       return JVM.isSupported(); } catch (SecurityException localSecurityException) {
/*     */     }
/* 153 */     return false;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.tracing.dtrace.DTraceProviderFactory
 * JD-Core Version:    0.6.2
 */