/*     */ package com.sun.corba.se.spi.extension;
/*     */ 
/*     */ import org.omg.CORBA.LocalObject;
/*     */ import org.omg.CORBA.Policy;
/*     */ 
/*     */ public class ServantCachingPolicy extends LocalObject
/*     */   implements Policy
/*     */ {
/*     */   public static final int NO_SERVANT_CACHING = 0;
/*     */   public static final int FULL_SEMANTICS = 1;
/*     */   public static final int INFO_ONLY_SEMANTICS = 2;
/*     */   public static final int MINIMAL_SEMANTICS = 3;
/*  81 */   private static ServantCachingPolicy policy = null;
/*  82 */   private static ServantCachingPolicy infoOnlyPolicy = null;
/*  83 */   private static ServantCachingPolicy minimalPolicy = null;
/*     */   private int type;
/*     */ 
/*     */   public String typeToName()
/*     */   {
/*  89 */     switch (this.type) {
/*     */     case 1:
/*  91 */       return "FULL";
/*     */     case 2:
/*  93 */       return "INFO_ONLY";
/*     */     case 3:
/*  95 */       return "MINIMAL";
/*     */     }
/*  97 */     return "UNKNOWN(" + this.type + ")";
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 103 */     return "ServantCachingPolicy[" + typeToName() + "]";
/*     */   }
/*     */ 
/*     */   private ServantCachingPolicy(int paramInt)
/*     */   {
/* 108 */     this.type = paramInt;
/*     */   }
/*     */ 
/*     */   public int getType()
/*     */   {
/* 113 */     return this.type;
/*     */   }
/*     */ 
/*     */   public static synchronized ServantCachingPolicy getPolicy()
/*     */   {
/* 120 */     return getFullPolicy();
/*     */   }
/*     */ 
/*     */   public static synchronized ServantCachingPolicy getFullPolicy()
/*     */   {
/* 125 */     if (policy == null) {
/* 126 */       policy = new ServantCachingPolicy(1);
/*     */     }
/* 128 */     return policy;
/*     */   }
/*     */ 
/*     */   public static synchronized ServantCachingPolicy getInfoOnlyPolicy()
/*     */   {
/* 133 */     if (infoOnlyPolicy == null) {
/* 134 */       infoOnlyPolicy = new ServantCachingPolicy(2);
/*     */     }
/* 136 */     return infoOnlyPolicy;
/*     */   }
/*     */ 
/*     */   public static synchronized ServantCachingPolicy getMinimalPolicy()
/*     */   {
/* 141 */     if (minimalPolicy == null) {
/* 142 */       minimalPolicy = new ServantCachingPolicy(3);
/*     */     }
/* 144 */     return minimalPolicy;
/*     */   }
/*     */ 
/*     */   public int policy_type()
/*     */   {
/* 149 */     return 1398079488;
/*     */   }
/*     */ 
/*     */   public Policy copy()
/*     */   {
/* 154 */     return this;
/*     */   }
/*     */ 
/*     */   public void destroy()
/*     */   {
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.extension.ServantCachingPolicy
 * JD-Core Version:    0.6.2
 */