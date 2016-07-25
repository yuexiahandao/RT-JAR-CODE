/*     */ package com.sun.security.sasl;
/*     */ 
/*     */ import com.sun.security.sasl.util.PolicyUtils;
/*     */ import java.io.IOException;
/*     */ import java.util.Map;
/*     */ import javax.security.auth.callback.Callback;
/*     */ import javax.security.auth.callback.CallbackHandler;
/*     */ import javax.security.auth.callback.NameCallback;
/*     */ import javax.security.auth.callback.PasswordCallback;
/*     */ import javax.security.auth.callback.UnsupportedCallbackException;
/*     */ import javax.security.sasl.SaslClient;
/*     */ import javax.security.sasl.SaslClientFactory;
/*     */ import javax.security.sasl.SaslException;
/*     */ 
/*     */ public final class ClientFactoryImpl
/*     */   implements SaslClientFactory
/*     */ {
/*  50 */   private static final String[] myMechs = { "EXTERNAL", "CRAM-MD5", "PLAIN" };
/*     */ 
/*  56 */   private static final int[] mechPolicies = { 7, 17, 16 };
/*     */   private static final int EXTERNAL = 0;
/*     */   private static final int CRAMMD5 = 1;
/*     */   private static final int PLAIN = 2;
/*     */ 
/*     */   public SaslClient createSaslClient(String[] paramArrayOfString, String paramString1, String paramString2, String paramString3, Map<String, ?> paramMap, CallbackHandler paramCallbackHandler)
/*     */     throws SaslException
/*     */   {
/*  77 */     for (int i = 0; i < paramArrayOfString.length; i++) {
/*  78 */       if ((paramArrayOfString[i].equals(myMechs[0])) && (PolicyUtils.checkPolicy(mechPolicies[0], paramMap)))
/*     */       {
/*  80 */         return new ExternalClient(paramString1);
/*     */       }
/*     */       Object[] arrayOfObject;
/*  82 */       if ((paramArrayOfString[i].equals(myMechs[1])) && (PolicyUtils.checkPolicy(mechPolicies[1], paramMap)))
/*     */       {
/*  85 */         arrayOfObject = getUserInfo("CRAM-MD5", paramString1, paramCallbackHandler);
/*     */ 
/*  88 */         return new CramMD5Client((String)arrayOfObject[0], (byte[])arrayOfObject[1]);
/*     */       }
/*     */ 
/*  91 */       if ((paramArrayOfString[i].equals(myMechs[2])) && (PolicyUtils.checkPolicy(mechPolicies[2], paramMap)))
/*     */       {
/*  94 */         arrayOfObject = getUserInfo("PLAIN", paramString1, paramCallbackHandler);
/*     */ 
/*  97 */         return new PlainClient(paramString1, (String)arrayOfObject[0], (byte[])arrayOfObject[1]);
/*     */       }
/*     */     }
/*     */ 
/* 101 */     return null;
/*     */   }
/*     */ 
/*     */   public String[] getMechanismNames(Map<String, ?> paramMap) {
/* 105 */     return PolicyUtils.filterMechs(myMechs, mechPolicies, paramMap);
/*     */   }
/*     */ 
/*     */   private Object[] getUserInfo(String paramString1, String paramString2, CallbackHandler paramCallbackHandler)
/*     */     throws SaslException
/*     */   {
/* 122 */     if (paramCallbackHandler == null) {
/* 123 */       throw new SaslException("Callback handler to get username/password required");
/*     */     }
/*     */     try
/*     */     {
/* 127 */       String str1 = paramString1 + " authentication id: ";
/* 128 */       String str2 = paramString1 + " password: ";
/*     */ 
/* 130 */       NameCallback localNameCallback = paramString2 == null ? new NameCallback(str1) : new NameCallback(str1, paramString2);
/*     */ 
/* 134 */       PasswordCallback localPasswordCallback = new PasswordCallback(str2, false);
/*     */ 
/* 136 */       paramCallbackHandler.handle(new Callback[] { localNameCallback, localPasswordCallback });
/*     */ 
/* 138 */       char[] arrayOfChar = localPasswordCallback.getPassword();
/*     */       byte[] arrayOfByte;
/* 143 */       if (arrayOfChar != null) {
/* 144 */         arrayOfByte = new String(arrayOfChar).getBytes("UTF8");
/* 145 */         localPasswordCallback.clearPassword();
/*     */       } else {
/* 147 */         arrayOfByte = null;
/*     */       }
/*     */ 
/* 150 */       String str3 = localNameCallback.getName();
/*     */ 
/* 152 */       return new Object[] { str3, arrayOfByte };
/*     */     }
/*     */     catch (IOException localIOException) {
/* 155 */       throw new SaslException("Cannot get password", localIOException);
/*     */     } catch (UnsupportedCallbackException localUnsupportedCallbackException) {
/* 157 */       throw new SaslException("Cannot get userid/password", localUnsupportedCallbackException);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.security.sasl.ClientFactoryImpl
 * JD-Core Version:    0.6.2
 */