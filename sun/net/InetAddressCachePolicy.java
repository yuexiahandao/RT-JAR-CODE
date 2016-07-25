/*     */ package sun.net;
/*     */ 
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.security.Security;
/*     */ import sun.security.action.GetIntegerAction;
/*     */ 
/*     */ public final class InetAddressCachePolicy
/*     */ {
/*     */   private static final String cachePolicyProp = "networkaddress.cache.ttl";
/*     */   private static final String cachePolicyPropFallback = "sun.net.inetaddr.ttl";
/*     */   private static final String negativeCachePolicyProp = "networkaddress.cache.negative.ttl";
/*     */   private static final String negativeCachePolicyPropFallback = "sun.net.inetaddr.negative.ttl";
/*     */   public static final int FOREVER = -1;
/*     */   public static final int NEVER = 0;
/*     */   public static final int DEFAULT_POSITIVE = 30;
/*  59 */   private static int cachePolicy = -1;
/*     */ 
/*  69 */   private static int negativeCachePolicy = 0;
/*     */   private static boolean propertySet;
/*     */   private static boolean propertyNegativeSet;
/*     */ 
/*     */   public static synchronized int get()
/*     */   {
/* 157 */     return cachePolicy;
/*     */   }
/*     */ 
/*     */   public static synchronized int getNegative() {
/* 161 */     return negativeCachePolicy;
/*     */   }
/*     */ 
/*     */   public static synchronized void setIfNotSet(int paramInt)
/*     */   {
/* 177 */     if (!propertySet) {
/* 178 */       checkValue(paramInt, cachePolicy);
/* 179 */       cachePolicy = paramInt;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static synchronized void setNegativeIfNotSet(int paramInt)
/*     */   {
/* 196 */     if (!propertyNegativeSet)
/*     */     {
/* 200 */       negativeCachePolicy = paramInt;
/*     */     }
/*     */   }
/*     */ 
/*     */   private static void checkValue(int paramInt1, int paramInt2)
/*     */   {
/* 210 */     if (paramInt1 == -1) {
/* 211 */       return;
/*     */     }
/* 213 */     if ((paramInt2 == -1) || (paramInt1 < paramInt2) || (paramInt1 < -1))
/*     */     {
/* 217 */       throw new SecurityException("can't make InetAddress cache more lax");
/*     */     }
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  87 */     Integer localInteger = null;
/*     */     try
/*     */     {
/*  90 */       localInteger = new Integer((String)AccessController.doPrivileged(new PrivilegedAction()
/*     */       {
/*     */         public String run()
/*     */         {
/*  94 */           return Security.getProperty("networkaddress.cache.ttl");
/*     */         }
/*     */       }));
/*     */     }
/*     */     catch (NumberFormatException localNumberFormatException1) {
/*     */     }
/* 100 */     if (localInteger != null) {
/* 101 */       cachePolicy = localInteger.intValue();
/* 102 */       if (cachePolicy < 0) {
/* 103 */         cachePolicy = -1;
/*     */       }
/* 105 */       propertySet = true;
/*     */     } else {
/* 107 */       localInteger = (Integer)AccessController.doPrivileged(new GetIntegerAction("sun.net.inetaddr.ttl"));
/*     */ 
/* 109 */       if (localInteger != null) {
/* 110 */         cachePolicy = localInteger.intValue();
/* 111 */         if (cachePolicy < 0) {
/* 112 */           cachePolicy = -1;
/*     */         }
/* 114 */         propertySet = true;
/*     */       }
/* 119 */       else if (System.getSecurityManager() == null) {
/* 120 */         cachePolicy = 30;
/*     */       }
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 126 */       localInteger = new Integer((String)AccessController.doPrivileged(new PrivilegedAction()
/*     */       {
/*     */         public String run()
/*     */         {
/* 130 */           return Security.getProperty("networkaddress.cache.negative.ttl");
/*     */         }
/*     */       }));
/*     */     }
/*     */     catch (NumberFormatException localNumberFormatException2)
/*     */     {
/*     */     }
/* 137 */     if (localInteger != null) {
/* 138 */       negativeCachePolicy = localInteger.intValue();
/* 139 */       if (negativeCachePolicy < 0) {
/* 140 */         negativeCachePolicy = -1;
/*     */       }
/* 142 */       propertyNegativeSet = true;
/*     */     } else {
/* 144 */       localInteger = (Integer)AccessController.doPrivileged(new GetIntegerAction("sun.net.inetaddr.negative.ttl"));
/*     */ 
/* 146 */       if (localInteger != null) {
/* 147 */         negativeCachePolicy = localInteger.intValue();
/* 148 */         if (negativeCachePolicy < 0) {
/* 149 */           negativeCachePolicy = -1;
/*     */         }
/* 151 */         propertyNegativeSet = true;
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.net.InetAddressCachePolicy
 * JD-Core Version:    0.6.2
 */