/*     */ package com.sun.security.sasl;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.util.Map;
/*     */ import java.util.Random;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.security.auth.callback.Callback;
/*     */ import javax.security.auth.callback.CallbackHandler;
/*     */ import javax.security.auth.callback.NameCallback;
/*     */ import javax.security.auth.callback.PasswordCallback;
/*     */ import javax.security.auth.callback.UnsupportedCallbackException;
/*     */ import javax.security.sasl.AuthorizeCallback;
/*     */ import javax.security.sasl.SaslException;
/*     */ import javax.security.sasl.SaslServer;
/*     */ 
/*     */ final class CramMD5Server extends CramMD5Base
/*     */   implements SaslServer
/*     */ {
/*     */   private String fqdn;
/*  57 */   private byte[] challengeData = null;
/*     */   private String authzid;
/*     */   private CallbackHandler cbh;
/*     */ 
/*     */   CramMD5Server(String paramString1, String paramString2, Map paramMap, CallbackHandler paramCallbackHandler)
/*     */     throws SaslException
/*     */   {
/*  73 */     if (paramString2 == null) {
/*  74 */       throw new SaslException("CRAM-MD5: fully qualified server name must be specified");
/*     */     }
/*     */ 
/*  78 */     this.fqdn = paramString2;
/*  79 */     this.cbh = paramCallbackHandler;
/*     */   }
/*     */ 
/*     */   public byte[] evaluateResponse(byte[] paramArrayOfByte)
/*     */     throws SaslException
/*     */   {
/* 100 */     if (this.completed) {
/* 101 */       throw new IllegalStateException("CRAM-MD5 authentication already completed");
/*     */     }
/*     */ 
/* 105 */     if (this.aborted) {
/* 106 */       throw new IllegalStateException("CRAM-MD5 authentication previously aborted due to error");
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 111 */       if (this.challengeData == null) {
/* 112 */         if (paramArrayOfByte.length != 0) {
/* 113 */           this.aborted = true;
/* 114 */           throw new SaslException("CRAM-MD5 does not expect any initial response");
/*     */         }
/*     */ 
/* 119 */         Random localRandom = new Random();
/* 120 */         long l1 = localRandom.nextLong();
/* 121 */         long l2 = System.currentTimeMillis();
/*     */ 
/* 123 */         localObject = new StringBuffer();
/* 124 */         ((StringBuffer)localObject).append('<');
/* 125 */         ((StringBuffer)localObject).append(l1);
/* 126 */         ((StringBuffer)localObject).append('.');
/* 127 */         ((StringBuffer)localObject).append(l2);
/* 128 */         ((StringBuffer)localObject).append('@');
/* 129 */         ((StringBuffer)localObject).append(this.fqdn);
/* 130 */         ((StringBuffer)localObject).append('>');
/* 131 */         String str2 = ((StringBuffer)localObject).toString();
/*     */ 
/* 133 */         logger.log(Level.FINE, "CRAMSRV01:Generated challenge: {0}", str2);
/*     */ 
/* 136 */         this.challengeData = str2.getBytes("UTF8");
/* 137 */         return (byte[])this.challengeData.clone();
/*     */       }
/*     */ 
/* 141 */       if (logger.isLoggable(Level.FINE)) {
/* 142 */         logger.log(Level.FINE, "CRAMSRV02:Received response: {0}", new String(paramArrayOfByte, "UTF8"));
/*     */       }
/*     */ 
/* 148 */       int i = 0;
/* 149 */       for (int j = 0; j < paramArrayOfByte.length; j++) {
/* 150 */         if (paramArrayOfByte[j] == 32) {
/* 151 */           i = j;
/* 152 */           break;
/*     */         }
/*     */       }
/* 155 */       if (i == 0) {
/* 156 */         this.aborted = true;
/* 157 */         throw new SaslException("CRAM-MD5: Invalid response; space missing");
/*     */       }
/*     */ 
/* 160 */       String str1 = new String(paramArrayOfByte, 0, i, "UTF8");
/*     */ 
/* 162 */       logger.log(Level.FINE, "CRAMSRV03:Extracted username: {0}", str1);
/*     */ 
/* 166 */       NameCallback localNameCallback = new NameCallback("CRAM-MD5 authentication ID: ", str1);
/*     */ 
/* 168 */       PasswordCallback localPasswordCallback = new PasswordCallback("CRAM-MD5 password: ", false);
/*     */ 
/* 170 */       this.cbh.handle(new Callback[] { localNameCallback, localPasswordCallback });
/* 171 */       char[] arrayOfChar = localPasswordCallback.getPassword();
/* 172 */       if ((arrayOfChar == null) || (arrayOfChar.length == 0))
/*     */       {
/* 174 */         this.aborted = true;
/* 175 */         throw new SaslException("CRAM-MD5: username not found: " + str1);
/*     */       }
/*     */ 
/* 178 */       localPasswordCallback.clearPassword();
/* 179 */       Object localObject = new String(arrayOfChar);
/* 180 */       for (int k = 0; k < arrayOfChar.length; k++) {
/* 181 */         arrayOfChar[k] = '\000';
/*     */       }
/* 183 */       this.pw = ((String)localObject).getBytes("UTF8");
/*     */ 
/* 187 */       String str3 = HMAC_MD5(this.pw, this.challengeData);
/*     */ 
/* 189 */       logger.log(Level.FINE, "CRAMSRV04:Expecting digest: {0}", str3);
/*     */ 
/* 193 */       clearPassword();
/*     */ 
/* 196 */       byte[] arrayOfByte = str3.getBytes("UTF8");
/* 197 */       int m = paramArrayOfByte.length - i - 1;
/* 198 */       if (arrayOfByte.length != m) {
/* 199 */         this.aborted = true;
/* 200 */         throw new SaslException("Invalid response");
/*     */       }
/* 202 */       int n = 0;
/* 203 */       for (int i1 = i + 1; i1 < paramArrayOfByte.length; i1++) {
/* 204 */         if (arrayOfByte[(n++)] != paramArrayOfByte[i1]) {
/* 205 */           this.aborted = true;
/* 206 */           throw new SaslException("Invalid response");
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 211 */       AuthorizeCallback localAuthorizeCallback = new AuthorizeCallback(str1, str1);
/* 212 */       this.cbh.handle(new Callback[] { localAuthorizeCallback });
/* 213 */       if (localAuthorizeCallback.isAuthorized()) {
/* 214 */         this.authzid = localAuthorizeCallback.getAuthorizedID();
/*     */       }
/*     */       else {
/* 217 */         this.aborted = true;
/* 218 */         throw new SaslException("CRAM-MD5: user not authorized: " + str1);
/*     */       }
/*     */ 
/* 222 */       logger.log(Level.FINE, "CRAMSRV05:Authorization id: {0}", this.authzid);
/*     */ 
/* 225 */       this.completed = true;
/* 226 */       return null;
/*     */     }
/*     */     catch (UnsupportedEncodingException localUnsupportedEncodingException) {
/* 229 */       this.aborted = true;
/* 230 */       throw new SaslException("UTF8 not available on platform", localUnsupportedEncodingException);
/*     */     } catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {
/* 232 */       this.aborted = true;
/* 233 */       throw new SaslException("MD5 algorithm not available on platform", localNoSuchAlgorithmException);
/*     */     } catch (UnsupportedCallbackException localUnsupportedCallbackException) {
/* 235 */       this.aborted = true;
/* 236 */       throw new SaslException("CRAM-MD5 authentication failed", localUnsupportedCallbackException);
/*     */     } catch (SaslException localSaslException) {
/* 238 */       throw localSaslException;
/*     */     } catch (IOException localIOException) {
/* 240 */       this.aborted = true;
/* 241 */       throw new SaslException("CRAM-MD5 authentication failed", localIOException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public String getAuthorizationID() {
/* 246 */     if (this.completed) {
/* 247 */       return this.authzid;
/*     */     }
/* 249 */     throw new IllegalStateException("CRAM-MD5 authentication not completed");
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.security.sasl.CramMD5Server
 * JD-Core Version:    0.6.2
 */