/*     */ package com.sun.security.sasl.ntlm;
/*     */ 
/*     */ import com.sun.security.ntlm.Client;
/*     */ import com.sun.security.ntlm.NTLMException;
/*     */ import java.io.IOException;
/*     */ import java.net.InetAddress;
/*     */ import java.net.UnknownHostException;
/*     */ import java.util.Map;
/*     */ import java.util.Random;
/*     */ import javax.security.auth.callback.Callback;
/*     */ import javax.security.auth.callback.CallbackHandler;
/*     */ import javax.security.auth.callback.NameCallback;
/*     */ import javax.security.auth.callback.PasswordCallback;
/*     */ import javax.security.auth.callback.UnsupportedCallbackException;
/*     */ import javax.security.sasl.RealmCallback;
/*     */ import javax.security.sasl.SaslClient;
/*     */ import javax.security.sasl.SaslException;
/*     */ 
/*     */ final class NTLMClient
/*     */   implements SaslClient
/*     */ {
/*     */   private static final String NTLM_VERSION = "com.sun.security.sasl.ntlm.version";
/*     */   private static final String NTLM_RANDOM = "com.sun.security.sasl.ntlm.random";
/*     */   private static final String NTLM_DOMAIN = "com.sun.security.sasl.ntlm.domain";
/*     */   private static final String NTLM_HOSTNAME = "com.sun.security.sasl.ntlm.hostname";
/*     */   private final Client client;
/*     */   private final String mech;
/*     */   private final Random random;
/* 102 */   private int step = 0;
/*     */ 
/*     */   NTLMClient(String paramString1, String paramString2, String paramString3, String paramString4, Map paramMap, CallbackHandler paramCallbackHandler)
/*     */     throws SaslException
/*     */   {
/* 116 */     this.mech = paramString1;
/* 117 */     String str1 = null;
/* 118 */     Random localRandom = null;
/* 119 */     String str2 = null;
/*     */ 
/* 121 */     if (paramMap != null) {
/* 122 */       localObject = (String)paramMap.get("javax.security.sasl.qop");
/* 123 */       if ((localObject != null) && (!((String)localObject).equals("auth"))) {
/* 124 */         throw new SaslException("NTLM only support auth");
/*     */       }
/* 126 */       str1 = (String)paramMap.get("com.sun.security.sasl.ntlm.version");
/* 127 */       localRandom = (Random)paramMap.get("com.sun.security.sasl.ntlm.random");
/* 128 */       str2 = (String)paramMap.get("com.sun.security.sasl.ntlm.hostname");
/*     */     }
/* 130 */     this.random = (localRandom != null ? localRandom : new Random());
/*     */ 
/* 132 */     if (str1 == null) {
/* 133 */       str1 = System.getProperty("ntlm.version");
/*     */     }
/*     */ 
/* 136 */     Object localObject = (paramString4 != null) && (!paramString4.isEmpty()) ? new RealmCallback("Realm: ", paramString4) : new RealmCallback("Realm: ");
/*     */ 
/* 139 */     NameCallback localNameCallback = (paramString2 != null) && (!paramString2.isEmpty()) ? new NameCallback("User name: ", paramString2) : new NameCallback("User name: ");
/*     */ 
/* 142 */     PasswordCallback localPasswordCallback = new PasswordCallback("Password: ", false);
/*     */     try
/*     */     {
/* 146 */       paramCallbackHandler.handle(new Callback[] { localObject, localNameCallback, localPasswordCallback });
/*     */     } catch (UnsupportedCallbackException localUnsupportedCallbackException) {
/* 148 */       throw new SaslException("NTLM: Cannot perform callback to acquire realm, username or password", localUnsupportedCallbackException);
/*     */     }
/*     */     catch (IOException localIOException) {
/* 151 */       throw new SaslException("NTLM: Error acquiring realm, username or password", localIOException);
/*     */     }
/*     */ 
/* 155 */     if (str2 == null)
/*     */       try {
/* 157 */         str2 = InetAddress.getLocalHost().getCanonicalHostName();
/*     */       } catch (UnknownHostException localUnknownHostException) {
/* 159 */         str2 = "localhost";
/*     */       }
/*     */     try
/*     */     {
/* 163 */       this.client = new Client(str1, str2, localNameCallback.getName(), ((RealmCallback)localObject).getText(), localPasswordCallback.getPassword());
/*     */     }
/*     */     catch (NTLMException localNTLMException)
/*     */     {
/* 168 */       throw new SaslException("NTLM: client creation failure", localNTLMException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public String getMechanismName()
/*     */   {
/* 175 */     return this.mech;
/*     */   }
/*     */ 
/*     */   public boolean isComplete()
/*     */   {
/* 180 */     return this.step >= 2;
/*     */   }
/*     */ 
/*     */   public byte[] unwrap(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */     throws SaslException
/*     */   {
/* 186 */     throw new IllegalStateException("Not supported.");
/*     */   }
/*     */ 
/*     */   public byte[] wrap(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */     throws SaslException
/*     */   {
/* 192 */     throw new IllegalStateException("Not supported.");
/*     */   }
/*     */ 
/*     */   public Object getNegotiatedProperty(String paramString)
/*     */   {
/* 197 */     if (!isComplete()) {
/* 198 */       throw new IllegalStateException("authentication not complete");
/*     */     }
/* 200 */     if (paramString.equals("javax.security.sasl.qop"))
/* 201 */       return "auth";
/* 202 */     if (paramString.equals("com.sun.security.sasl.ntlm.domain")) {
/* 203 */       return this.client.getDomain();
/*     */     }
/* 205 */     return null;
/*     */   }
/*     */ 
/*     */   public void dispose()
/*     */     throws SaslException
/*     */   {
/* 211 */     this.client.dispose();
/*     */   }
/*     */ 
/*     */   public boolean hasInitialResponse()
/*     */   {
/* 216 */     return true;
/*     */   }
/*     */ 
/*     */   public byte[] evaluateChallenge(byte[] paramArrayOfByte) throws SaslException
/*     */   {
/* 221 */     this.step += 1;
/* 222 */     if (this.step == 1)
/* 223 */       return this.client.type1();
/*     */     try
/*     */     {
/* 226 */       byte[] arrayOfByte = new byte[8];
/* 227 */       this.random.nextBytes(arrayOfByte);
/* 228 */       return this.client.type3(paramArrayOfByte, arrayOfByte);
/*     */     } catch (NTLMException localNTLMException) {
/* 230 */       throw new SaslException("Type3 creation failed", localNTLMException);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.security.sasl.ntlm.NTLMClient
 * JD-Core Version:    0.6.2
 */