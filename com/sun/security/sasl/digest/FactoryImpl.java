/*     */ package com.sun.security.sasl.digest;
/*     */ 
/*     */ import com.sun.security.sasl.util.PolicyUtils;
/*     */ import java.util.Map;
/*     */ import javax.security.auth.callback.CallbackHandler;
/*     */ import javax.security.sasl.SaslClient;
/*     */ import javax.security.sasl.SaslClientFactory;
/*     */ import javax.security.sasl.SaslException;
/*     */ import javax.security.sasl.SaslServer;
/*     */ import javax.security.sasl.SaslServerFactory;
/*     */ 
/*     */ public final class FactoryImpl
/*     */   implements SaslClientFactory, SaslServerFactory
/*     */ {
/*  47 */   private static final String[] myMechs = { "DIGEST-MD5" };
/*     */   private static final int DIGEST_MD5 = 0;
/*  49 */   private static final int[] mechPolicies = { 17 };
/*     */ 
/*     */   public SaslClient createSaslClient(String[] paramArrayOfString, String paramString1, String paramString2, String paramString3, Map<String, ?> paramMap, CallbackHandler paramCallbackHandler)
/*     */     throws SaslException
/*     */   {
/*  70 */     for (int i = 0; i < paramArrayOfString.length; i++) {
/*  71 */       if ((paramArrayOfString[i].equals(myMechs[0])) && (PolicyUtils.checkPolicy(mechPolicies[0], paramMap)))
/*     */       {
/*  74 */         if (paramCallbackHandler == null) {
/*  75 */           throw new SaslException("Callback handler with support for RealmChoiceCallback, RealmCallback, NameCallback, and PasswordCallback required");
/*     */         }
/*     */ 
/*  81 */         return new DigestMD5Client(paramString1, paramString2, paramString3, paramMap, paramCallbackHandler);
/*     */       }
/*     */     }
/*     */ 
/*  85 */     return null;
/*     */   }
/*     */ 
/*     */   public SaslServer createSaslServer(String paramString1, String paramString2, String paramString3, Map<String, ?> paramMap, CallbackHandler paramCallbackHandler)
/*     */     throws SaslException
/*     */   {
/*  99 */     if ((paramString1.equals(myMechs[0])) && (PolicyUtils.checkPolicy(mechPolicies[0], paramMap)))
/*     */     {
/* 102 */       if (paramCallbackHandler == null) {
/* 103 */         throw new SaslException("Callback handler with support for AuthorizeCallback, RealmCallback, NameCallback, and PasswordCallback required");
/*     */       }
/*     */ 
/* 109 */       return new DigestMD5Server(paramString2, paramString3, paramMap, paramCallbackHandler);
/*     */     }
/* 111 */     return null;
/*     */   }
/*     */ 
/*     */   public String[] getMechanismNames(Map<String, ?> paramMap)
/*     */   {
/* 121 */     return PolicyUtils.filterMechs(myMechs, mechPolicies, paramMap);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.security.sasl.digest.FactoryImpl
 * JD-Core Version:    0.6.2
 */