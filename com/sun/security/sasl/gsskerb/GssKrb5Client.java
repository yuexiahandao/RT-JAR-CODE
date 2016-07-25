/*     */ package com.sun.security.sasl.gsskerb;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Map;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.security.auth.callback.CallbackHandler;
/*     */ import javax.security.sasl.SaslClient;
/*     */ import javax.security.sasl.SaslException;
/*     */ import org.ietf.jgss.GSSContext;
/*     */ import org.ietf.jgss.GSSCredential;
/*     */ import org.ietf.jgss.GSSException;
/*     */ import org.ietf.jgss.GSSManager;
/*     */ import org.ietf.jgss.GSSName;
/*     */ import org.ietf.jgss.MessageProp;
/*     */ 
/*     */ final class GssKrb5Client extends GssKrb5Base
/*     */   implements SaslClient
/*     */ {
/*  84 */   private static final String MY_CLASS_NAME = GssKrb5Client.class.getName();
/*     */ 
/*  86 */   private boolean finalHandshake = false;
/*  87 */   private boolean mutual = false;
/*     */   private byte[] authzID;
/*     */ 
/*     */   GssKrb5Client(String paramString1, String paramString2, String paramString3, Map paramMap, CallbackHandler paramCallbackHandler)
/*     */     throws SaslException
/*     */   {
/*  98 */     super(paramMap, MY_CLASS_NAME);
/*     */ 
/* 100 */     String str = paramString2 + "@" + paramString3;
/* 101 */     logger.log(Level.FINE, "KRB5CLNT01:Requesting service name: {0}", str);
/*     */     try
/*     */     {
/* 105 */       GSSManager localGSSManager = GSSManager.getInstance();
/*     */ 
/* 108 */       GSSName localGSSName = localGSSManager.createName(str, GSSName.NT_HOSTBASED_SERVICE, KRB5_OID);
/*     */ 
/* 112 */       GSSCredential localGSSCredential = null;
/*     */       Object localObject;
/* 113 */       if (paramMap != null) {
/* 114 */         localObject = paramMap.get("javax.security.sasl.credentials");
/* 115 */         if ((localObject != null) && ((localObject instanceof GSSCredential))) {
/* 116 */           localGSSCredential = (GSSCredential)localObject;
/* 117 */           logger.log(Level.FINE, "KRB5CLNT01:Using the credentials supplied in javax.security.sasl.credentials");
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 124 */       this.secCtx = localGSSManager.createContext(localGSSName, KRB5_OID, localGSSCredential, 2147483647);
/*     */ 
/* 130 */       if (localGSSCredential != null) {
/* 131 */         this.secCtx.requestCredDeleg(true);
/*     */       }
/*     */ 
/* 135 */       if (paramMap != null)
/*     */       {
/* 137 */         localObject = (String)paramMap.get("javax.security.sasl.server.authentication");
/* 138 */         if (localObject != null) {
/* 139 */           this.mutual = "true".equalsIgnoreCase((String)localObject);
/*     */         }
/*     */       }
/* 142 */       this.secCtx.requestMutualAuth(this.mutual);
/*     */ 
/* 146 */       this.secCtx.requestConf(true);
/* 147 */       this.secCtx.requestInteg(true);
/*     */     }
/*     */     catch (GSSException localGSSException) {
/* 150 */       throw new SaslException("Failure to initialize security context", localGSSException);
/*     */     }
/*     */ 
/* 153 */     if ((paramString1 != null) && (paramString1.length() > 0))
/*     */       try {
/* 155 */         this.authzID = paramString1.getBytes("UTF8");
/*     */       } catch (IOException localIOException) {
/* 157 */         throw new SaslException("Cannot encode authorization ID", localIOException);
/*     */       }
/*     */   }
/*     */ 
/*     */   public boolean hasInitialResponse()
/*     */   {
/* 163 */     return true;
/*     */   }
/*     */ 
/*     */   public byte[] evaluateChallenge(byte[] paramArrayOfByte)
/*     */     throws SaslException
/*     */   {
/* 181 */     if (this.completed) {
/* 182 */       throw new IllegalStateException("GSSAPI authentication already complete");
/*     */     }
/*     */ 
/* 186 */     if (this.finalHandshake) {
/* 187 */       return doFinalHandshake(paramArrayOfByte);
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 193 */       byte[] arrayOfByte = this.secCtx.initSecContext(paramArrayOfByte, 0, paramArrayOfByte.length);
/*     */ 
/* 195 */       if (logger.isLoggable(Level.FINER)) {
/* 196 */         traceOutput(MY_CLASS_NAME, "evaluteChallenge", "KRB5CLNT02:Challenge: [raw]", paramArrayOfByte);
/*     */ 
/* 198 */         traceOutput(MY_CLASS_NAME, "evaluateChallenge", "KRB5CLNT03:Response: [after initSecCtx]", arrayOfByte);
/*     */       }
/*     */ 
/* 202 */       if (this.secCtx.isEstablished()) {
/* 203 */         this.finalHandshake = true;
/* 204 */         if (arrayOfByte == null)
/*     */         {
/* 206 */           return EMPTY;
/*     */         }
/*     */       }
/*     */ 
/* 210 */       return arrayOfByte;
/*     */     } catch (GSSException localGSSException) {
/* 212 */       throw new SaslException("GSS initiate failed", localGSSException);
/*     */     }
/*     */   }
/*     */ 
/*     */   private byte[] doFinalHandshake(byte[] paramArrayOfByte)
/*     */     throws SaslException
/*     */   {
/*     */     try
/*     */     {
/* 222 */       if (logger.isLoggable(Level.FINER)) {
/* 223 */         traceOutput(MY_CLASS_NAME, "doFinalHandshake", "KRB5CLNT04:Challenge [raw]:", paramArrayOfByte);
/*     */       }
/*     */ 
/* 227 */       if (paramArrayOfByte.length == 0)
/*     */       {
/* 229 */         return EMPTY;
/*     */       }
/*     */ 
/* 234 */       byte[] arrayOfByte1 = this.secCtx.unwrap(paramArrayOfByte, 0, paramArrayOfByte.length, new MessageProp(0, false));
/*     */ 
/* 239 */       if (logger.isLoggable(Level.FINE)) {
/* 240 */         if (logger.isLoggable(Level.FINER)) {
/* 241 */           traceOutput(MY_CLASS_NAME, "doFinalHandshake", "KRB5CLNT05:Challenge [unwrapped]:", arrayOfByte1);
/*     */         }
/*     */ 
/* 244 */         logger.log(Level.FINE, "KRB5CLNT06:Server protections: {0}", new Byte(arrayOfByte1[0]));
/*     */       }
/*     */ 
/* 250 */       byte b = findPreferredMask(arrayOfByte1[0], this.qop);
/* 251 */       if (b == 0) {
/* 252 */         throw new SaslException("No common protection layer between client and server");
/*     */       }
/*     */ 
/* 256 */       if ((b & 0x4) != 0) {
/* 257 */         this.privacy = true;
/* 258 */         this.integrity = true;
/* 259 */       } else if ((b & 0x2) != 0) {
/* 260 */         this.integrity = true;
/*     */       }
/*     */ 
/* 265 */       int i = networkByteOrderToInt(arrayOfByte1, 1, 3);
/*     */ 
/* 269 */       this.sendMaxBufSize = (this.sendMaxBufSize == 0 ? i : Math.min(this.sendMaxBufSize, i));
/*     */ 
/* 273 */       this.rawSendSize = this.secCtx.getWrapSizeLimit(0, this.privacy, this.sendMaxBufSize);
/*     */ 
/* 276 */       if (logger.isLoggable(Level.FINE)) {
/* 277 */         logger.log(Level.FINE, "KRB5CLNT07:Client max recv size: {0}; server max recv size: {1}; rawSendSize: {2}", new Object[] { new Integer(this.recvMaxBufSize), new Integer(i), new Integer(this.rawSendSize) });
/*     */       }
/*     */ 
/* 286 */       int j = 4;
/* 287 */       if (this.authzID != null) {
/* 288 */         j += this.authzID.length;
/*     */       }
/*     */ 
/* 291 */       byte[] arrayOfByte2 = new byte[j];
/* 292 */       arrayOfByte2[0] = b;
/*     */ 
/* 294 */       if (logger.isLoggable(Level.FINE)) {
/* 295 */         logger.log(Level.FINE, "KRB5CLNT08:Selected protection: {0}; privacy: {1}; integrity: {2}", new Object[] { new Byte(b), Boolean.valueOf(this.privacy), Boolean.valueOf(this.integrity) });
/*     */       }
/*     */ 
/* 302 */       intToNetworkByteOrder(this.recvMaxBufSize, arrayOfByte2, 1, 3);
/* 303 */       if (this.authzID != null)
/*     */       {
/* 305 */         System.arraycopy(this.authzID, 0, arrayOfByte2, 4, this.authzID.length);
/* 306 */         logger.log(Level.FINE, "KRB5CLNT09:Authzid: {0}", this.authzID);
/*     */       }
/*     */ 
/* 309 */       if (logger.isLoggable(Level.FINER)) {
/* 310 */         traceOutput(MY_CLASS_NAME, "doFinalHandshake", "KRB5CLNT10:Response [raw]", arrayOfByte2);
/*     */       }
/*     */ 
/* 314 */       arrayOfByte1 = this.secCtx.wrap(arrayOfByte2, 0, arrayOfByte2.length, new MessageProp(0, false));
/*     */ 
/* 318 */       if (logger.isLoggable(Level.FINER)) {
/* 319 */         traceOutput(MY_CLASS_NAME, "doFinalHandshake", "KRB5CLNT11:Response [after wrap]", arrayOfByte1);
/*     */       }
/*     */ 
/* 323 */       this.completed = true;
/*     */ 
/* 325 */       return arrayOfByte1;
/*     */     } catch (GSSException localGSSException) {
/* 327 */       throw new SaslException("Final handshake failed", localGSSException);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.security.sasl.gsskerb.GssKrb5Client
 * JD-Core Version:    0.6.2
 */