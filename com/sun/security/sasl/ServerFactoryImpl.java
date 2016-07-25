/*    */ package com.sun.security.sasl;
/*    */ 
/*    */ import com.sun.security.sasl.util.PolicyUtils;
/*    */ import java.util.Map;
/*    */ import javax.security.auth.callback.CallbackHandler;
/*    */ import javax.security.sasl.SaslException;
/*    */ import javax.security.sasl.SaslServer;
/*    */ import javax.security.sasl.SaslServerFactory;
/*    */ 
/*    */ public final class ServerFactoryImpl
/*    */   implements SaslServerFactory
/*    */ {
/* 44 */   private static final String[] myMechs = { "CRAM-MD5" };
/*    */ 
/* 48 */   private static final int[] mechPolicies = { 17 };
/*    */   private static final int CRAMMD5 = 0;
/*    */ 
/*    */   public SaslServer createSaslServer(String paramString1, String paramString2, String paramString3, Map<String, ?> paramMap, CallbackHandler paramCallbackHandler)
/*    */     throws SaslException
/*    */   {
/* 63 */     if ((paramString1.equals(myMechs[0])) && (PolicyUtils.checkPolicy(mechPolicies[0], paramMap)))
/*    */     {
/* 66 */       if (paramCallbackHandler == null) {
/* 67 */         throw new SaslException("Callback handler with support for AuthorizeCallback required");
/*    */       }
/*    */ 
/* 70 */       return new CramMD5Server(paramString2, paramString3, paramMap, paramCallbackHandler);
/*    */     }
/* 72 */     return null;
/*    */   }
/*    */ 
/*    */   public String[] getMechanismNames(Map<String, ?> paramMap) {
/* 76 */     return PolicyUtils.filterMechs(myMechs, mechPolicies, paramMap);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.security.sasl.ServerFactoryImpl
 * JD-Core Version:    0.6.2
 */