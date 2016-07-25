/*    */ package com.sun.security.sasl.gsskerb;
/*    */ 
/*    */ import com.sun.security.sasl.util.PolicyUtils;
/*    */ import java.util.Map;
/*    */ import javax.security.auth.callback.CallbackHandler;
/*    */ import javax.security.sasl.SaslClient;
/*    */ import javax.security.sasl.SaslClientFactory;
/*    */ import javax.security.sasl.SaslException;
/*    */ import javax.security.sasl.SaslServer;
/*    */ import javax.security.sasl.SaslServerFactory;
/*    */ 
/*    */ public final class FactoryImpl
/*    */   implements SaslClientFactory, SaslServerFactory
/*    */ {
/* 41 */   private static final String[] myMechs = { "GSSAPI" };
/*    */ 
/* 44 */   private static final int[] mechPolicies = { 19 };
/*    */   private static final int GSS_KERB_V5 = 0;
/*    */ 
/*    */   public SaslClient createSaslClient(String[] paramArrayOfString, String paramString1, String paramString2, String paramString3, Map<String, ?> paramMap, CallbackHandler paramCallbackHandler)
/*    */     throws SaslException
/*    */   {
/* 60 */     for (int i = 0; i < paramArrayOfString.length; i++) {
/* 61 */       if ((paramArrayOfString[i].equals(myMechs[0])) && (PolicyUtils.checkPolicy(mechPolicies[0], paramMap)))
/*    */       {
/* 63 */         return new GssKrb5Client(paramString1, paramString2, paramString3, paramMap, paramCallbackHandler);
/*    */       }
/*    */ 
/*    */     }
/*    */ 
/* 71 */     return null;
/*    */   }
/*    */ 
/*    */   public SaslServer createSaslServer(String paramString1, String paramString2, String paramString3, Map<String, ?> paramMap, CallbackHandler paramCallbackHandler)
/*    */     throws SaslException
/*    */   {
/* 79 */     if ((paramString1.equals(myMechs[0])) && (PolicyUtils.checkPolicy(mechPolicies[0], paramMap)))
/*    */     {
/* 81 */       if (paramCallbackHandler == null) {
/* 82 */         throw new SaslException("Callback handler with support for AuthorizeCallback required");
/*    */       }
/*    */ 
/* 85 */       return new GssKrb5Server(paramString2, paramString3, paramMap, paramCallbackHandler);
/*    */     }
/*    */ 
/* 91 */     return null;
/*    */   }
/*    */ 
/*    */   public String[] getMechanismNames(Map<String, ?> paramMap) {
/* 95 */     return PolicyUtils.filterMechs(myMechs, mechPolicies, paramMap);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.security.sasl.gsskerb.FactoryImpl
 * JD-Core Version:    0.6.2
 */