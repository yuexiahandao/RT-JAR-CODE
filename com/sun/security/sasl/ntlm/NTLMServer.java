/*     */ package com.sun.security.sasl.ntlm;
/*     */ 
/*     */ import com.sun.security.ntlm.NTLMException;
/*     */ import com.sun.security.ntlm.Server;
/*     */ import java.io.IOException;
/*     */ import java.util.Map;
/*     */ import java.util.Random;
/*     */ import javax.security.auth.callback.Callback;
/*     */ import javax.security.auth.callback.CallbackHandler;
/*     */ import javax.security.auth.callback.NameCallback;
/*     */ import javax.security.auth.callback.PasswordCallback;
/*     */ import javax.security.auth.callback.UnsupportedCallbackException;
/*     */ import javax.security.sasl.RealmCallback;
/*     */ import javax.security.sasl.SaslException;
/*     */ import javax.security.sasl.SaslServer;
/*     */ 
/*     */ final class NTLMServer
/*     */   implements SaslServer
/*     */ {
/*     */   private static final String NTLM_VERSION = "com.sun.security.sasl.ntlm.version";
/*     */   private static final String NTLM_DOMAIN = "com.sun.security.sasl.ntlm.domain";
/*     */   private static final String NTLM_HOSTNAME = "com.sun.security.sasl.ntlm.hostname";
/*     */   private static final String NTLM_RANDOM = "com.sun.security.sasl.ntlm.random";
/*     */   private final Random random;
/*     */   private final Server server;
/*     */   private byte[] nonce;
/*  98 */   private int step = 0;
/*     */   private String authzId;
/*     */   private final String mech;
/*     */   private String hostname;
/*     */ 
/*     */   NTLMServer(String paramString1, String paramString2, String paramString3, Map paramMap, final CallbackHandler paramCallbackHandler)
/*     */     throws SaslException
/*     */   {
/* 115 */     this.mech = paramString1;
/* 116 */     String str1 = null;
/* 117 */     String str2 = null;
/* 118 */     Random localRandom = null;
/*     */ 
/* 120 */     if (paramMap != null) {
/* 121 */       str2 = (String)paramMap.get("com.sun.security.sasl.ntlm.domain");
/* 122 */       str1 = (String)paramMap.get("com.sun.security.sasl.ntlm.version");
/* 123 */       localRandom = (Random)paramMap.get("com.sun.security.sasl.ntlm.random");
/*     */     }
/* 125 */     this.random = (localRandom != null ? localRandom : new Random());
/*     */ 
/* 127 */     if (str1 == null) {
/* 128 */       str1 = System.getProperty("ntlm.version");
/*     */     }
/* 130 */     if (str2 == null) {
/* 131 */       str2 = paramString3;
/*     */     }
/* 133 */     if (str2 == null) {
/* 134 */       throw new SaslException("Domain must be provided as the serverName argument or in props");
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 139 */       this.server = new Server(str1, str2) {
/*     */         public char[] getPassword(String paramAnonymousString1, String paramAnonymousString2) {
/*     */           try {
/* 142 */             RealmCallback localRealmCallback = new RealmCallback("Domain: ", paramAnonymousString1);
/*     */ 
/* 144 */             NameCallback localNameCallback = new NameCallback("Name: ", paramAnonymousString2);
/*     */ 
/* 146 */             PasswordCallback localPasswordCallback = new PasswordCallback("Password: ", false);
/*     */ 
/* 148 */             paramCallbackHandler.handle(new Callback[] { localRealmCallback, localNameCallback, localPasswordCallback });
/* 149 */             char[] arrayOfChar = localPasswordCallback.getPassword();
/* 150 */             localPasswordCallback.clearPassword();
/* 151 */             return arrayOfChar;
/*     */           } catch (IOException localIOException) {
/* 153 */             return null; } catch (UnsupportedCallbackException localUnsupportedCallbackException) {
/*     */           }
/* 155 */           return null;
/*     */         }
/*     */       };
/*     */     }
/*     */     catch (NTLMException localNTLMException) {
/* 160 */       throw new SaslException("NTLM: server creation failure", localNTLMException);
/*     */     }
/*     */ 
/* 163 */     this.nonce = new byte[8];
/*     */   }
/*     */ 
/*     */   public String getMechanismName()
/*     */   {
/* 168 */     return this.mech;
/*     */   }
/*     */ 
/*     */   public byte[] evaluateResponse(byte[] paramArrayOfByte) throws SaslException
/*     */   {
/*     */     try {
/* 174 */       this.step += 1;
/* 175 */       if (this.step == 1) {
/* 176 */         this.random.nextBytes(this.nonce);
/* 177 */         return this.server.type2(paramArrayOfByte, this.nonce);
/*     */       }
/* 179 */       String[] arrayOfString = this.server.verify(paramArrayOfByte, this.nonce);
/* 180 */       this.authzId = arrayOfString[0];
/* 181 */       this.hostname = arrayOfString[1];
/* 182 */       return null;
/*     */     }
/*     */     catch (NTLMException localNTLMException) {
/* 185 */       throw new SaslException("NTLM: generate response failure", localNTLMException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean isComplete()
/*     */   {
/* 191 */     return this.step >= 2;
/*     */   }
/*     */ 
/*     */   public String getAuthorizationID()
/*     */   {
/* 196 */     if (!isComplete()) {
/* 197 */       throw new IllegalStateException("authentication not complete");
/*     */     }
/* 199 */     return this.authzId;
/*     */   }
/*     */ 
/*     */   public byte[] unwrap(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */     throws SaslException
/*     */   {
/* 205 */     throw new IllegalStateException("Not supported yet.");
/*     */   }
/*     */ 
/*     */   public byte[] wrap(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */     throws SaslException
/*     */   {
/* 211 */     throw new IllegalStateException("Not supported yet.");
/*     */   }
/*     */ 
/*     */   public Object getNegotiatedProperty(String paramString)
/*     */   {
/* 216 */     if (!isComplete()) {
/* 217 */       throw new IllegalStateException("authentication not complete");
/*     */     }
/* 219 */     if (paramString.equals("javax.security.sasl.qop"))
/* 220 */       return "auth";
/* 221 */     if (paramString.equals("com.sun.security.sasl.ntlm.hostname")) {
/* 222 */       return this.hostname;
/*     */     }
/* 224 */     return null;
/*     */   }
/*     */ 
/*     */   public void dispose()
/*     */     throws SaslException
/*     */   {
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.security.sasl.ntlm.NTLMServer
 * JD-Core Version:    0.6.2
 */