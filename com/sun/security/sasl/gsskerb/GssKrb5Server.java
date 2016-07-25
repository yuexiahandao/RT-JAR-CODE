/*     */ package com.sun.security.sasl.gsskerb;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.util.Map;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.security.auth.callback.Callback;
/*     */ import javax.security.auth.callback.CallbackHandler;
/*     */ import javax.security.auth.callback.UnsupportedCallbackException;
/*     */ import javax.security.sasl.AuthorizeCallback;
/*     */ import javax.security.sasl.SaslException;
/*     */ import javax.security.sasl.SaslServer;
/*     */ import org.ietf.jgss.GSSContext;
/*     */ import org.ietf.jgss.GSSCredential;
/*     */ import org.ietf.jgss.GSSException;
/*     */ import org.ietf.jgss.GSSManager;
/*     */ import org.ietf.jgss.GSSName;
/*     */ import org.ietf.jgss.MessageProp;
/*     */ 
/*     */ final class GssKrb5Server extends GssKrb5Base
/*     */   implements SaslServer
/*     */ {
/*  67 */   private static final String MY_CLASS_NAME = GssKrb5Server.class.getName();
/*     */ 
/*  69 */   private int handshakeStage = 0;
/*     */   private String peer;
/*     */   private String authzid;
/*     */   private CallbackHandler cbh;
/*     */ 
/*     */   GssKrb5Server(String paramString1, String paramString2, Map paramMap, CallbackHandler paramCallbackHandler)
/*     */     throws SaslException
/*     */   {
/*  82 */     super(paramMap, MY_CLASS_NAME);
/*     */ 
/*  84 */     this.cbh = paramCallbackHandler;
/*  85 */     String str = paramString1 + "@" + paramString2;
/*     */ 
/*  87 */     logger.log(Level.FINE, "KRB5SRV01:Using service name: {0}", str);
/*     */     try
/*     */     {
/*  90 */       GSSManager localGSSManager = GSSManager.getInstance();
/*     */ 
/*  93 */       GSSName localGSSName = localGSSManager.createName(str, GSSName.NT_HOSTBASED_SERVICE, KRB5_OID);
/*     */ 
/*  96 */       GSSCredential localGSSCredential = localGSSManager.createCredential(localGSSName, 2147483647, KRB5_OID, 2);
/*     */ 
/* 101 */       this.secCtx = localGSSManager.createContext(localGSSCredential);
/*     */ 
/* 103 */       if ((this.allQop & 0x2) != 0)
/*     */       {
/* 105 */         this.secCtx.requestInteg(true);
/*     */       }
/*     */ 
/* 108 */       if ((this.allQop & 0x4) != 0)
/*     */       {
/* 110 */         this.secCtx.requestConf(true);
/*     */       }
/*     */     } catch (GSSException localGSSException) {
/* 113 */       throw new SaslException("Failure to initialize security context", localGSSException);
/*     */     }
/* 115 */     logger.log(Level.FINE, "KRB5SRV02:Initialization complete");
/*     */   }
/*     */ 
/*     */   public byte[] evaluateResponse(byte[] paramArrayOfByte)
/*     */     throws SaslException
/*     */   {
/* 134 */     if (this.completed) {
/* 135 */       throw new SaslException("SASL authentication already complete");
/*     */     }
/*     */ 
/* 139 */     if (logger.isLoggable(Level.FINER)) {
/* 140 */       traceOutput(MY_CLASS_NAME, "evaluateResponse", "KRB5SRV03:Response [raw]:", paramArrayOfByte);
/*     */     }
/*     */ 
/* 144 */     switch (this.handshakeStage) {
/*     */     case 1:
/* 146 */       return doHandshake1(paramArrayOfByte);
/*     */     case 2:
/* 149 */       return doHandshake2(paramArrayOfByte);
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 155 */       byte[] arrayOfByte = this.secCtx.acceptSecContext(paramArrayOfByte, 0, paramArrayOfByte.length);
/*     */ 
/* 158 */       if (logger.isLoggable(Level.FINER)) {
/* 159 */         traceOutput(MY_CLASS_NAME, "evaluateResponse", "KRB5SRV04:Challenge: [after acceptSecCtx]", arrayOfByte);
/*     */       }
/*     */ 
/* 163 */       if (this.secCtx.isEstablished()) {
/* 164 */         this.handshakeStage = 1;
/*     */ 
/* 166 */         this.peer = this.secCtx.getSrcName().toString();
/*     */ 
/* 168 */         logger.log(Level.FINE, "KRB5SRV05:Peer name is : {0}", this.peer);
/*     */ 
/* 170 */         if (arrayOfByte == null) {
/* 171 */           return doHandshake1(EMPTY);
/*     */         }
/*     */       }
/*     */ 
/* 175 */       return arrayOfByte;
/*     */     } catch (GSSException localGSSException) {
/* 177 */       throw new SaslException("GSS initiate failed", localGSSException);
/*     */     }
/*     */   }
/*     */ 
/*     */   private byte[] doHandshake1(byte[] paramArrayOfByte)
/*     */     throws SaslException
/*     */   {
/*     */     try
/*     */     {
/* 186 */       if ((paramArrayOfByte != null) && (paramArrayOfByte.length > 0)) {
/* 187 */         throw new SaslException("Handshake expecting no response data from server");
/*     */       }
/*     */ 
/* 195 */       byte[] arrayOfByte1 = new byte[4];
/* 196 */       arrayOfByte1[0] = this.allQop;
/* 197 */       intToNetworkByteOrder(this.recvMaxBufSize, arrayOfByte1, 1, 3);
/*     */ 
/* 199 */       if (logger.isLoggable(Level.FINE)) {
/* 200 */         logger.log(Level.FINE, "KRB5SRV06:Supported protections: {0}; recv max buf size: {1}", new Object[] { new Byte(this.allQop), new Integer(this.recvMaxBufSize) });
/*     */       }
/*     */ 
/* 206 */       this.handshakeStage = 2;
/*     */ 
/* 208 */       if (logger.isLoggable(Level.FINER)) {
/* 209 */         traceOutput(MY_CLASS_NAME, "doHandshake1", "KRB5SRV07:Challenge [raw]", arrayOfByte1);
/*     */       }
/*     */ 
/* 213 */       byte[] arrayOfByte2 = this.secCtx.wrap(arrayOfByte1, 0, arrayOfByte1.length, new MessageProp(0, false));
/*     */ 
/* 216 */       if (logger.isLoggable(Level.FINER)) {
/* 217 */         traceOutput(MY_CLASS_NAME, "doHandshake1", "KRB5SRV08:Challenge [after wrap]", arrayOfByte2);
/*     */       }
/*     */ 
/* 220 */       return arrayOfByte2;
/*     */     }
/*     */     catch (GSSException localGSSException) {
/* 223 */       throw new SaslException("Problem wrapping handshake1", localGSSException);
/*     */     }
/*     */   }
/*     */ 
/*     */   private byte[] doHandshake2(byte[] paramArrayOfByte) throws SaslException
/*     */   {
/*     */     try
/*     */     {
/* 231 */       byte[] arrayOfByte = this.secCtx.unwrap(paramArrayOfByte, 0, paramArrayOfByte.length, new MessageProp(0, false));
/*     */ 
/* 234 */       if (logger.isLoggable(Level.FINER)) {
/* 235 */         traceOutput(MY_CLASS_NAME, "doHandshake2", "KRB5SRV09:Response [after unwrap]", arrayOfByte);
/*     */       }
/*     */ 
/* 240 */       byte b = arrayOfByte[0];
/* 241 */       if ((b & this.allQop) == 0) {
/* 242 */         throw new SaslException("Client selected unsupported protection: " + b);
/*     */       }
/*     */ 
/* 245 */       if ((b & 0x4) != 0) {
/* 246 */         this.privacy = true;
/* 247 */         this.integrity = true;
/* 248 */       } else if ((b & 0x2) != 0) {
/* 249 */         this.integrity = true;
/*     */       }
/*     */ 
/* 255 */       int i = networkByteOrderToInt(arrayOfByte, 1, 3);
/*     */ 
/* 259 */       this.sendMaxBufSize = (this.sendMaxBufSize == 0 ? i : Math.min(this.sendMaxBufSize, i));
/*     */ 
/* 263 */       this.rawSendSize = this.secCtx.getWrapSizeLimit(0, this.privacy, this.sendMaxBufSize);
/*     */ 
/* 266 */       if (logger.isLoggable(Level.FINE)) {
/* 267 */         logger.log(Level.FINE, "KRB5SRV10:Selected protection: {0}; privacy: {1}; integrity: {2}", new Object[] { new Byte(b), Boolean.valueOf(this.privacy), Boolean.valueOf(this.integrity) });
/*     */ 
/* 272 */         logger.log(Level.FINE, "KRB5SRV11:Client max recv size: {0}; server max send size: {1}; rawSendSize: {2}", new Object[] { new Integer(i), new Integer(this.sendMaxBufSize), new Integer(this.rawSendSize) });
/*     */       }
/*     */ 
/* 280 */       if (arrayOfByte.length > 4)
/*     */         try {
/* 282 */           this.authzid = new String(arrayOfByte, 4, arrayOfByte.length - 4, "UTF-8");
/*     */         }
/*     */         catch (UnsupportedEncodingException localUnsupportedEncodingException) {
/* 285 */           throw new SaslException("Cannot decode authzid", localUnsupportedEncodingException);
/*     */         }
/*     */       else {
/* 288 */         this.authzid = this.peer;
/*     */       }
/* 290 */       logger.log(Level.FINE, "KRB5SRV12:Authzid: {0}", this.authzid);
/*     */ 
/* 292 */       AuthorizeCallback localAuthorizeCallback = new AuthorizeCallback(this.peer, this.authzid);
/*     */ 
/* 295 */       this.cbh.handle(new Callback[] { localAuthorizeCallback });
/* 296 */       if (localAuthorizeCallback.isAuthorized()) {
/* 297 */         this.authzid = localAuthorizeCallback.getAuthorizedID();
/* 298 */         this.completed = true;
/*     */       }
/*     */       else {
/* 301 */         throw new SaslException(this.peer + " is not authorized to connect as " + this.authzid);
/*     */       }
/*     */ 
/* 305 */       return null;
/*     */     } catch (GSSException localGSSException) {
/* 307 */       throw new SaslException("Final handshake step failed", localGSSException);
/*     */     } catch (IOException localIOException) {
/* 309 */       throw new SaslException("Problem with callback handler", localIOException);
/*     */     } catch (UnsupportedCallbackException localUnsupportedCallbackException) {
/* 311 */       throw new SaslException("Problem with callback handler", localUnsupportedCallbackException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public String getAuthorizationID() {
/* 316 */     if (this.completed) {
/* 317 */       return this.authzid;
/*     */     }
/* 319 */     throw new IllegalStateException("Authentication incomplete");
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.security.sasl.gsskerb.GssKrb5Server
 * JD-Core Version:    0.6.2
 */