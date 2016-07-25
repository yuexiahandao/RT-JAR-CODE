/*     */ package sun.security.jca;
/*     */ 
/*     */ import java.security.Provider;
/*     */ import sun.security.util.Debug;
/*     */ 
/*     */ public class Providers
/*     */ {
/*  42 */   private static final ThreadLocal<ProviderList> threadLists = new InheritableThreadLocal();
/*     */   private static volatile int threadListsUsed;
/*  57 */   private static volatile ProviderList providerList = ProviderList.fromSecurityProperties();
/*     */   private static final String BACKUP_PROVIDER_CLASSNAME = "sun.security.provider.VerificationProvider";
/*  86 */   private static final String[] jarVerificationProviders = { "sun.security.provider.Sun", "sun.security.rsa.SunRsaSign", "sun.security.ec.SunEC", "sun.security.provider.VerificationProvider" };
/*     */ 
/*     */   public static Provider getSunProvider()
/*     */   {
/*     */     try
/*     */     {
/* 100 */       Class localClass1 = Class.forName(jarVerificationProviders[0]);
/* 101 */       return (Provider)localClass1.newInstance();
/*     */     } catch (Exception localException1) {
/*     */       try {
/* 104 */         Class localClass2 = Class.forName("sun.security.provider.VerificationProvider");
/* 105 */         return (Provider)localClass2.newInstance(); } catch (Exception localException2) {
/*     */       }
/* 107 */       throw new RuntimeException("Sun provider not found", localException1);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static Object startJarVerification()
/*     */   {
/* 119 */     ProviderList localProviderList1 = getProviderList();
/* 120 */     ProviderList localProviderList2 = localProviderList1.getJarList(jarVerificationProviders);
/*     */ 
/* 122 */     return beginThreadProviderList(localProviderList2);
/*     */   }
/*     */ 
/*     */   public static void stopJarVerification(Object paramObject)
/*     */   {
/* 130 */     endThreadProviderList((ProviderList)paramObject);
/*     */   }
/*     */ 
/*     */   public static ProviderList getProviderList()
/*     */   {
/* 138 */     ProviderList localProviderList = getThreadProviderList();
/* 139 */     if (localProviderList == null) {
/* 140 */       localProviderList = getSystemProviderList();
/*     */     }
/* 142 */     return localProviderList;
/*     */   }
/*     */ 
/*     */   public static void setProviderList(ProviderList paramProviderList)
/*     */   {
/* 150 */     if (getThreadProviderList() == null)
/* 151 */       setSystemProviderList(paramProviderList);
/*     */     else
/* 153 */       changeThreadProviderList(paramProviderList);
/*     */   }
/*     */ 
/*     */   public static ProviderList getFullProviderList()
/*     */   {
/* 164 */     synchronized (Providers.class) {
/* 165 */       localObject1 = getThreadProviderList();
/* 166 */       if (localObject1 != null) {
/* 167 */         ProviderList localProviderList = ((ProviderList)localObject1).removeInvalid();
/* 168 */         if (localProviderList != localObject1) {
/* 169 */           changeThreadProviderList(localProviderList);
/* 170 */           localObject1 = localProviderList;
/*     */         }
/* 172 */         return localObject1;
/*     */       }
/*     */     }
/* 175 */     Object localObject1 = getSystemProviderList();
/* 176 */     ??? = ((ProviderList)localObject1).removeInvalid();
/* 177 */     if (??? != localObject1) {
/* 178 */       setSystemProviderList((ProviderList)???);
/* 179 */       localObject1 = ???;
/*     */     }
/* 181 */     return localObject1;
/*     */   }
/*     */ 
/*     */   private static ProviderList getSystemProviderList() {
/* 185 */     return providerList;
/*     */   }
/*     */ 
/*     */   private static void setSystemProviderList(ProviderList paramProviderList) {
/* 189 */     providerList = paramProviderList;
/*     */   }
/*     */ 
/*     */   public static ProviderList getThreadProviderList()
/*     */   {
/* 195 */     if (threadListsUsed == 0) {
/* 196 */       return null;
/*     */     }
/* 198 */     return (ProviderList)threadLists.get();
/*     */   }
/*     */ 
/*     */   private static void changeThreadProviderList(ProviderList paramProviderList)
/*     */   {
/* 205 */     threadLists.set(paramProviderList);
/*     */   }
/*     */ 
/*     */   public static synchronized ProviderList beginThreadProviderList(ProviderList paramProviderList)
/*     */   {
/* 225 */     if (ProviderList.debug != null) {
/* 226 */       ProviderList.debug.println("ThreadLocal providers: " + paramProviderList);
/*     */     }
/* 228 */     ProviderList localProviderList = (ProviderList)threadLists.get();
/* 229 */     threadListsUsed += 1;
/* 230 */     threadLists.set(paramProviderList);
/* 231 */     return localProviderList;
/*     */   }
/*     */ 
/*     */   public static synchronized void endThreadProviderList(ProviderList paramProviderList) {
/* 235 */     if (paramProviderList == null) {
/* 236 */       if (ProviderList.debug != null) {
/* 237 */         ProviderList.debug.println("Disabling ThreadLocal providers");
/*     */       }
/* 239 */       threadLists.remove();
/*     */     } else {
/* 241 */       if (ProviderList.debug != null) {
/* 242 */         ProviderList.debug.println("Restoring previous ThreadLocal providers: " + paramProviderList);
/*     */       }
/*     */ 
/* 245 */       threadLists.set(paramProviderList);
/*     */     }
/* 247 */     threadListsUsed -= 1;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.jca.Providers
 * JD-Core Version:    0.6.2
 */