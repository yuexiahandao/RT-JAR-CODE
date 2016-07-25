/*     */ package com.sun.security.sasl.util;
/*     */ 
/*     */ import java.util.Map;
/*     */ 
/*     */ public final class PolicyUtils
/*     */ {
/*     */   public static final int NOPLAINTEXT = 1;
/*     */   public static final int NOACTIVE = 2;
/*     */   public static final int NODICTIONARY = 4;
/*     */   public static final int FORWARD_SECRECY = 8;
/*     */   public static final int NOANONYMOUS = 16;
/*     */   public static final int PASS_CREDENTIALS = 512;
/*     */ 
/*     */   public static boolean checkPolicy(int paramInt, Map paramMap)
/*     */   {
/*  57 */     if (paramMap == null) {
/*  58 */       return true;
/*     */     }
/*     */ 
/*  61 */     if (("true".equalsIgnoreCase((String)paramMap.get("javax.security.sasl.policy.noplaintext"))) && ((paramInt & 0x1) == 0))
/*     */     {
/*  63 */       return false;
/*     */     }
/*  65 */     if (("true".equalsIgnoreCase((String)paramMap.get("javax.security.sasl.policy.noactive"))) && ((paramInt & 0x2) == 0))
/*     */     {
/*  67 */       return false;
/*     */     }
/*  69 */     if (("true".equalsIgnoreCase((String)paramMap.get("javax.security.sasl.policy.nodictionary"))) && ((paramInt & 0x4) == 0))
/*     */     {
/*  71 */       return false;
/*     */     }
/*  73 */     if (("true".equalsIgnoreCase((String)paramMap.get("javax.security.sasl.policy.noanonymous"))) && ((paramInt & 0x10) == 0))
/*     */     {
/*  75 */       return false;
/*     */     }
/*  77 */     if (("true".equalsIgnoreCase((String)paramMap.get("javax.security.sasl.policy.forward"))) && ((paramInt & 0x8) == 0))
/*     */     {
/*  79 */       return false;
/*     */     }
/*  81 */     if (("true".equalsIgnoreCase((String)paramMap.get("javax.security.sasl.policy.credentials"))) && ((paramInt & 0x200) == 0))
/*     */     {
/*  83 */       return false;
/*     */     }
/*     */ 
/*  86 */     return true;
/*     */   }
/*     */ 
/*     */   public static String[] filterMechs(String[] paramArrayOfString, int[] paramArrayOfInt, Map paramMap)
/*     */   {
/*  97 */     if (paramMap == null) {
/*  98 */       return (String[])paramArrayOfString.clone();
/*     */     }
/*     */ 
/* 101 */     boolean[] arrayOfBoolean = new boolean[paramArrayOfString.length];
/* 102 */     int i = 0;
/* 103 */     for (int j = 0; j < paramArrayOfString.length; j++) {
/* 104 */       if ((arrayOfBoolean[j] = checkPolicy(paramArrayOfInt[j], paramMap))) {
/* 105 */         i++;
/*     */       }
/*     */     }
/* 108 */     String[] arrayOfString = new String[i];
/* 109 */     int k = 0; for (int m = 0; k < paramArrayOfString.length; k++) {
/* 110 */       if (arrayOfBoolean[k] != 0) {
/* 111 */         arrayOfString[(m++)] = paramArrayOfString[k];
/*     */       }
/*     */     }
/*     */ 
/* 115 */     return arrayOfString;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.security.sasl.util.PolicyUtils
 * JD-Core Version:    0.6.2
 */