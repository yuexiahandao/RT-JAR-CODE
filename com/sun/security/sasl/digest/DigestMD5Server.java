/*     */ package com.sun.security.sasl.digest;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.StringTokenizer;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.security.auth.callback.Callback;
/*     */ import javax.security.auth.callback.CallbackHandler;
/*     */ import javax.security.auth.callback.NameCallback;
/*     */ import javax.security.auth.callback.PasswordCallback;
/*     */ import javax.security.auth.callback.UnsupportedCallbackException;
/*     */ import javax.security.sasl.AuthorizeCallback;
/*     */ import javax.security.sasl.RealmCallback;
/*     */ import javax.security.sasl.SaslException;
/*     */ import javax.security.sasl.SaslServer;
/*     */ 
/*     */ final class DigestMD5Server extends DigestMD5Base
/*     */   implements SaslServer
/*     */ {
/*  94 */   private static final String MY_CLASS_NAME = DigestMD5Server.class.getName();
/*     */   private static final String UTF8_DIRECTIVE = "charset=utf-8,";
/*     */   private static final String ALGORITHM_DIRECTIVE = "algorithm=md5-sess";
/*     */   private static final int NONCE_COUNT_VALUE = 1;
/*     */   private static final String UTF8_PROPERTY = "com.sun.security.sasl.digest.utf8";
/*     */   private static final String REALM_PROPERTY = "com.sun.security.sasl.digest.realm";
/* 114 */   private static final String[] DIRECTIVE_KEY = { "username", "realm", "nonce", "cnonce", "nonce-count", "qop", "digest-uri", "response", "maxbuf", "charset", "cipher", "authzid", "auth-param" };
/*     */   private static final int USERNAME = 0;
/*     */   private static final int REALM = 1;
/*     */   private static final int NONCE = 2;
/*     */   private static final int CNONCE = 3;
/*     */   private static final int NONCE_COUNT = 4;
/*     */   private static final int QOP = 5;
/*     */   private static final int DIGEST_URI = 6;
/*     */   private static final int RESPONSE = 7;
/*     */   private static final int MAXBUF = 8;
/*     */   private static final int CHARSET = 9;
/*     */   private static final int CIPHER = 10;
/*     */   private static final int AUTHZID = 11;
/*     */   private static final int AUTH_PARAM = 12;
/*     */   private String specifiedQops;
/*     */   private byte[] myCiphers;
/*     */   private List<String> serverRealms;
/*     */ 
/*     */   DigestMD5Server(String paramString1, String paramString2, Map paramMap, CallbackHandler paramCallbackHandler)
/*     */     throws SaslException
/*     */   {
/* 152 */     super(paramMap, MY_CLASS_NAME, 1, paramString1 + "/" + paramString2, paramCallbackHandler);
/*     */ 
/* 154 */     this.serverRealms = new ArrayList();
/*     */ 
/* 156 */     this.useUTF8 = true;
/*     */ 
/* 158 */     if (paramMap != null) {
/* 159 */       this.specifiedQops = ((String)paramMap.get("javax.security.sasl.qop"));
/* 160 */       if ("false".equals((String)paramMap.get("com.sun.security.sasl.digest.utf8"))) {
/* 161 */         this.useUTF8 = false;
/* 162 */         logger.log(Level.FINE, "DIGEST80:Server supports ISO-Latin-1");
/*     */       }
/*     */ 
/* 165 */       String str1 = (String)paramMap.get("com.sun.security.sasl.digest.realm");
/* 166 */       if (str1 != null) {
/* 167 */         StringTokenizer localStringTokenizer = new StringTokenizer(str1, ", \t\n");
/* 168 */         int i = localStringTokenizer.countTokens();
/* 169 */         String str2 = null;
/* 170 */         for (int j = 0; j < i; j++) {
/* 171 */           str2 = localStringTokenizer.nextToken();
/* 172 */           logger.log(Level.FINE, "DIGEST81:Server supports realm {0}", str2);
/*     */ 
/* 174 */           this.serverRealms.add(str2);
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 179 */     this.encoding = (this.useUTF8 ? "UTF8" : "8859_1");
/*     */ 
/* 182 */     if (this.serverRealms.size() == 0)
/* 183 */       this.serverRealms.add(paramString2);
/*     */   }
/*     */ 
/*     */   public byte[] evaluateResponse(byte[] paramArrayOfByte) throws SaslException
/*     */   {
/* 188 */     if (paramArrayOfByte.length > 4096)
/* 189 */       throw new SaslException("DIGEST-MD5: Invalid digest response length. Got:  " + paramArrayOfByte.length + " Expected < " + 4096);
/*     */     byte[] arrayOfByte;
/* 195 */     switch (this.step) {
/*     */     case 1:
/* 197 */       if (paramArrayOfByte.length != 0) {
/* 198 */         throw new SaslException("DIGEST-MD5 must not have an initial response");
/*     */       }
/*     */ 
/* 203 */       String str = null;
/* 204 */       if ((this.allQop & 0x4) != 0) {
/* 205 */         this.myCiphers = getPlatformCiphers();
/* 206 */         StringBuffer localStringBuffer = new StringBuffer();
/*     */ 
/* 210 */         for (int i = 0; i < CIPHER_TOKENS.length; i++) {
/* 211 */           if (this.myCiphers[i] != 0) {
/* 212 */             if (localStringBuffer.length() > 0) {
/* 213 */               localStringBuffer.append(',');
/*     */             }
/* 215 */             localStringBuffer.append(CIPHER_TOKENS[i]);
/*     */           }
/*     */         }
/* 218 */         str = localStringBuffer.toString();
/*     */       }
/*     */       try
/*     */       {
/* 222 */         arrayOfByte = generateChallenge(this.serverRealms, this.specifiedQops, str);
/*     */ 
/* 225 */         this.step = 3;
/* 226 */         return arrayOfByte;
/*     */       } catch (UnsupportedEncodingException localUnsupportedEncodingException1) {
/* 228 */         throw new SaslException("DIGEST-MD5: Error encoding challenge", localUnsupportedEncodingException1);
/*     */       }
/*     */       catch (IOException localIOException) {
/* 231 */         throw new SaslException("DIGEST-MD5: Error generating challenge", localIOException);
/*     */       }
/*     */ 
/*     */     case 3:
/*     */       try
/*     */       {
/* 242 */         byte[][] arrayOfByte1 = parseDirectives(paramArrayOfByte, DIRECTIVE_KEY, null, 1);
/*     */ 
/* 244 */         arrayOfByte = validateClientResponse(arrayOfByte1);
/*     */       } catch (SaslException localSaslException) {
/* 246 */         throw localSaslException;
/*     */       } catch (UnsupportedEncodingException localUnsupportedEncodingException2) {
/* 248 */         throw new SaslException("DIGEST-MD5: Error validating client response", localUnsupportedEncodingException2);
/*     */       }
/*     */       finally {
/* 251 */         this.step = 0;
/*     */       }
/*     */ 
/* 254 */       this.completed = true;
/*     */ 
/* 257 */       if ((this.integrity) && (this.privacy))
/* 258 */         this.secCtx = new DigestMD5Base.DigestPrivacy(this, false);
/* 259 */       else if (this.integrity) {
/* 260 */         this.secCtx = new DigestMD5Base.DigestIntegrity(this, false);
/*     */       }
/*     */ 
/* 263 */       return arrayOfByte;
/*     */     }
/*     */ 
/* 267 */     throw new SaslException("DIGEST-MD5: Server at illegal state");
/*     */   }
/*     */ 
/*     */   private byte[] generateChallenge(List<String> paramList, String paramString1, String paramString2)
/*     */     throws UnsupportedEncodingException, IOException
/*     */   {
/* 297 */     ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
/*     */ 
/* 300 */     for (int i = 0; (paramList != null) && (i < paramList.size()); i++) {
/* 301 */       localByteArrayOutputStream.write("realm=\"".getBytes(this.encoding));
/* 302 */       writeQuotedStringValue(localByteArrayOutputStream, ((String)paramList.get(i)).getBytes(this.encoding));
/* 303 */       localByteArrayOutputStream.write(34);
/* 304 */       localByteArrayOutputStream.write(44);
/*     */     }
/*     */ 
/* 308 */     localByteArrayOutputStream.write("nonce=\"".getBytes(this.encoding));
/* 309 */     this.nonce = generateNonce();
/* 310 */     writeQuotedStringValue(localByteArrayOutputStream, this.nonce);
/* 311 */     localByteArrayOutputStream.write(34);
/* 312 */     localByteArrayOutputStream.write(44);
/*     */ 
/* 316 */     if (paramString1 != null) {
/* 317 */       localByteArrayOutputStream.write("qop=\"".getBytes(this.encoding));
/*     */ 
/* 319 */       writeQuotedStringValue(localByteArrayOutputStream, paramString1.getBytes(this.encoding));
/* 320 */       localByteArrayOutputStream.write(34);
/* 321 */       localByteArrayOutputStream.write(44);
/*     */     }
/*     */ 
/* 325 */     if (this.recvMaxBufSize != 65536) {
/* 326 */       localByteArrayOutputStream.write(("maxbuf=\"" + this.recvMaxBufSize + "\",").getBytes(this.encoding));
/*     */     }
/*     */ 
/* 330 */     if (this.useUTF8) {
/* 331 */       localByteArrayOutputStream.write("charset=utf-8,".getBytes(this.encoding));
/*     */     }
/*     */ 
/* 334 */     if (paramString2 != null) {
/* 335 */       localByteArrayOutputStream.write("cipher=\"".getBytes(this.encoding));
/*     */ 
/* 337 */       writeQuotedStringValue(localByteArrayOutputStream, paramString2.getBytes(this.encoding));
/* 338 */       localByteArrayOutputStream.write(34);
/* 339 */       localByteArrayOutputStream.write(44);
/*     */     }
/*     */ 
/* 343 */     localByteArrayOutputStream.write("algorithm=md5-sess".getBytes(this.encoding));
/*     */ 
/* 345 */     return localByteArrayOutputStream.toByteArray();
/*     */   }
/*     */ 
/*     */   private byte[] validateClientResponse(byte[][] paramArrayOfByte)
/*     */     throws SaslException, UnsupportedEncodingException
/*     */   {
/* 390 */     if (paramArrayOfByte[9] != null)
/*     */     {
/* 393 */       if ((!this.useUTF8) || (!"utf-8".equals(new String(paramArrayOfByte[9], this.encoding))))
/*     */       {
/* 395 */         throw new SaslException("DIGEST-MD5: digest response format violation. Incompatible charset value: " + new String(paramArrayOfByte[9]));
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 402 */     int i = paramArrayOfByte[8] == null ? 65536 : Integer.parseInt(new String(paramArrayOfByte[8], this.encoding));
/*     */ 
/* 408 */     this.sendMaxBufSize = (this.sendMaxBufSize == 0 ? i : Math.min(this.sendMaxBufSize, i));
/*     */     String str1;
/* 413 */     if (paramArrayOfByte[0] != null) {
/* 414 */       str1 = new String(paramArrayOfByte[0], this.encoding);
/* 415 */       logger.log(Level.FINE, "DIGEST82:Username: {0}", str1);
/*     */     } else {
/* 417 */       throw new SaslException("DIGEST-MD5: digest response format violation. Missing username.");
/*     */     }
/*     */ 
/* 422 */     this.negotiatedRealm = (paramArrayOfByte[1] != null ? new String(paramArrayOfByte[1], this.encoding) : "");
/*     */ 
/* 424 */     logger.log(Level.FINE, "DIGEST83:Client negotiated realm: {0}", this.negotiatedRealm);
/*     */ 
/* 427 */     if (!this.serverRealms.contains(this.negotiatedRealm))
/*     */     {
/* 430 */       throw new SaslException("DIGEST-MD5: digest response format violation. Nonexistent realm: " + this.negotiatedRealm);
/*     */     }
/*     */ 
/* 436 */     if (paramArrayOfByte[2] == null) {
/* 437 */       throw new SaslException("DIGEST-MD5: digest response format violation. Missing nonce.");
/*     */     }
/*     */ 
/* 440 */     byte[] arrayOfByte1 = paramArrayOfByte[2];
/* 441 */     if (!Arrays.equals(arrayOfByte1, this.nonce)) {
/* 442 */       throw new SaslException("DIGEST-MD5: digest response format violation. Mismatched nonce.");
/*     */     }
/*     */ 
/* 447 */     if (paramArrayOfByte[3] == null) {
/* 448 */       throw new SaslException("DIGEST-MD5: digest response format violation. Missing cnonce.");
/*     */     }
/*     */ 
/* 451 */     byte[] arrayOfByte2 = paramArrayOfByte[3];
/*     */ 
/* 454 */     if ((paramArrayOfByte[4] != null) && (1 != Integer.parseInt(new String(paramArrayOfByte[4], this.encoding), 16)))
/*     */     {
/* 457 */       throw new SaslException("DIGEST-MD5: digest response format violation. Nonce count does not match: " + new String(paramArrayOfByte[4]));
/*     */     }
/*     */ 
/* 463 */     this.negotiatedQop = (paramArrayOfByte[5] != null ? new String(paramArrayOfByte[5], this.encoding) : "auth");
/*     */ 
/* 466 */     logger.log(Level.FINE, "DIGEST84:Client negotiated qop: {0}", this.negotiatedQop);
/*     */     int j;
/* 471 */     if (this.negotiatedQop.equals("auth")) {
/* 472 */       j = 1;
/* 473 */     } else if (this.negotiatedQop.equals("auth-int")) {
/* 474 */       j = 2;
/* 475 */       this.integrity = true;
/* 476 */       this.rawSendSize = (this.sendMaxBufSize - 16);
/* 477 */     } else if (this.negotiatedQop.equals("auth-conf")) {
/* 478 */       j = 4;
/* 479 */       this.integrity = (this.privacy = 1);
/* 480 */       this.rawSendSize = (this.sendMaxBufSize - 26);
/*     */     } else {
/* 482 */       throw new SaslException("DIGEST-MD5: digest response format violation. Invalid QOP: " + this.negotiatedQop);
/*     */     }
/*     */ 
/* 485 */     if ((j & this.allQop) == 0) {
/* 486 */       throw new SaslException("DIGEST-MD5: server does not support  qop: " + this.negotiatedQop);
/*     */     }
/*     */ 
/* 490 */     if (this.privacy) {
/* 491 */       this.negotiatedCipher = (paramArrayOfByte[10] != null ? new String(paramArrayOfByte[10], this.encoding) : null);
/*     */ 
/* 493 */       if (this.negotiatedCipher == null) {
/* 494 */         throw new SaslException("DIGEST-MD5: digest response format violation. No cipher specified.");
/*     */       }
/*     */ 
/* 498 */       int k = -1;
/* 499 */       logger.log(Level.FINE, "DIGEST85:Client negotiated cipher: {0}", this.negotiatedCipher);
/*     */ 
/* 503 */       for (int m = 0; m < CIPHER_TOKENS.length; m++) {
/* 504 */         if ((this.negotiatedCipher.equals(CIPHER_TOKENS[m])) && (this.myCiphers[m] != 0))
/*     */         {
/* 506 */           k = m;
/* 507 */           break;
/*     */         }
/*     */       }
/* 510 */       if (k == -1) {
/* 511 */         throw new SaslException("DIGEST-MD5: server does not support cipher: " + this.negotiatedCipher);
/*     */       }
/*     */ 
/* 515 */       if ((CIPHER_MASKS[k] & 0x4) != 0)
/* 516 */         this.negotiatedStrength = "high";
/* 517 */       else if ((CIPHER_MASKS[k] & 0x2) != 0) {
/* 518 */         this.negotiatedStrength = "medium";
/*     */       }
/*     */       else {
/* 521 */         this.negotiatedStrength = "low";
/*     */       }
/*     */ 
/* 524 */       logger.log(Level.FINE, "DIGEST86:Negotiated strength: {0}", this.negotiatedStrength);
/*     */     }
/*     */ 
/* 529 */     Object localObject1 = paramArrayOfByte[6] != null ? new String(paramArrayOfByte[6], this.encoding) : null;
/*     */ 
/* 532 */     if (localObject1 != null) {
/* 533 */       logger.log(Level.FINE, "DIGEST87:digest URI: {0}", localObject1);
/*     */     }
/*     */ 
/* 545 */     if (this.digestUri.equalsIgnoreCase(localObject1))
/* 546 */       this.digestUri = localObject1;
/*     */     else {
/* 548 */       throw new SaslException("DIGEST-MD5: digest response format violation. Mismatched URI: " + localObject1 + "; expecting: " + this.digestUri);
/*     */     }
/*     */ 
/* 554 */     byte[] arrayOfByte3 = paramArrayOfByte[7];
/* 555 */     if (arrayOfByte3 == null)
/* 556 */       throw new SaslException("DIGEST-MD5: digest response format  violation. Missing response.");
/*     */     byte[] arrayOfByte4;
/* 562 */     String str2 = (arrayOfByte4 = paramArrayOfByte[11]) != null ? new String(arrayOfByte4, this.encoding) : str1;
/*     */ 
/* 565 */     if (arrayOfByte4 != null) {
/* 566 */       logger.log(Level.FINE, "DIGEST88:Authzid: {0}", new String(arrayOfByte4));
/*     */     }
/*     */ 
/*     */     char[] arrayOfChar;
/*     */     try
/*     */     {
/* 576 */       RealmCallback localRealmCallback = new RealmCallback("DIGEST-MD5 realm: ", this.negotiatedRealm);
/*     */ 
/* 578 */       NameCallback localNameCallback = new NameCallback("DIGEST-MD5 authentication ID: ", str1);
/*     */ 
/* 582 */       PasswordCallback localPasswordCallback = new PasswordCallback("DIGEST-MD5 password: ", false);
/*     */ 
/* 585 */       this.cbh.handle(new Callback[] { localRealmCallback, localNameCallback, localPasswordCallback });
/* 586 */       arrayOfChar = localPasswordCallback.getPassword();
/* 587 */       localPasswordCallback.clearPassword();
/*     */     }
/*     */     catch (UnsupportedCallbackException localUnsupportedCallbackException1) {
/* 590 */       throw new SaslException("DIGEST-MD5: Cannot perform callback to acquire password", localUnsupportedCallbackException1);
/*     */     }
/*     */     catch (IOException localIOException1)
/*     */     {
/* 594 */       throw new SaslException("DIGEST-MD5: IO error acquiring password", localIOException1);
/*     */     }
/*     */ 
/* 598 */     if (arrayOfChar == null) {
/* 599 */       throw new SaslException("DIGEST-MD5: cannot acquire password for " + str1 + " in realm : " + this.negotiatedRealm);
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/*     */       try
/*     */       {
/* 609 */         arrayOfByte5 = generateResponseValue("AUTHENTICATE", this.digestUri, this.negotiatedQop, str1, this.negotiatedRealm, arrayOfChar, this.nonce, arrayOfByte2, 1, arrayOfByte4);
/*     */       }
/*     */       catch (NoSuchAlgorithmException localNoSuchAlgorithmException)
/*     */       {
/* 615 */         throw new SaslException("DIGEST-MD5: problem duplicating client response", localNoSuchAlgorithmException);
/*     */       }
/*     */     }
/*     */     catch (IOException localIOException2)
/*     */     {
/*     */       byte[] arrayOfByte5;
/* 618 */       throw new SaslException("DIGEST-MD5: problem duplicating client response", localIOException2);
/*     */ 
/* 622 */       if (!Arrays.equals(arrayOfByte3, arrayOfByte5)) {
/* 623 */         throw new SaslException("DIGEST-MD5: digest response format violation. Mismatched response.");
/*     */       }
/*     */ 
/*     */       try
/*     */       {
/* 629 */         AuthorizeCallback localAuthorizeCallback = new AuthorizeCallback(str1, str2);
/*     */ 
/* 631 */         this.cbh.handle(new Callback[] { localAuthorizeCallback });
/*     */ 
/* 633 */         if (localAuthorizeCallback.isAuthorized())
/* 634 */           this.authzid = localAuthorizeCallback.getAuthorizedID();
/*     */         else
/* 636 */           throw new SaslException("DIGEST-MD5: " + str1 + " is not authorized to act as " + str2);
/*     */       }
/*     */       catch (SaslException localSaslException)
/*     */       {
/* 640 */         throw localSaslException;
/*     */       } catch (UnsupportedCallbackException localUnsupportedCallbackException2) {
/* 642 */         throw new SaslException("DIGEST-MD5: Cannot perform callback to check authzid", localUnsupportedCallbackException2);
/*     */       }
/*     */       catch (IOException localIOException3) {
/* 645 */         throw new SaslException("DIGEST-MD5: IO error checking authzid", localIOException3);
/*     */       }
/*     */       int n;
/* 649 */       return generateResponseAuth(str1, arrayOfChar, arrayOfByte2, 1, arrayOfByte4);
/*     */     }
/*     */     finally
/*     */     {
/* 653 */       for (int i1 = 0; i1 < arrayOfChar.length; i1++)
/* 654 */         arrayOfChar[i1] = '\000';
/*     */     }
/*     */   }
/*     */ 
/*     */   private byte[] generateResponseAuth(String paramString, char[] paramArrayOfChar, byte[] paramArrayOfByte1, int paramInt, byte[] paramArrayOfByte2)
/*     */     throws SaslException
/*     */   {
/*     */     try
/*     */     {
/* 679 */       byte[] arrayOfByte1 = generateResponseValue("", this.digestUri, this.negotiatedQop, paramString, this.negotiatedRealm, paramArrayOfChar, this.nonce, paramArrayOfByte1, paramInt, paramArrayOfByte2);
/*     */ 
/* 683 */       byte[] arrayOfByte2 = new byte[arrayOfByte1.length + 8];
/* 684 */       System.arraycopy("rspauth=".getBytes(this.encoding), 0, arrayOfByte2, 0, 8);
/* 685 */       System.arraycopy(arrayOfByte1, 0, arrayOfByte2, 8, arrayOfByte1.length);
/*     */ 
/* 688 */       return arrayOfByte2;
/*     */     }
/*     */     catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {
/* 691 */       throw new SaslException("DIGEST-MD5: problem generating response", localNoSuchAlgorithmException);
/*     */     } catch (IOException localIOException) {
/* 693 */       throw new SaslException("DIGEST-MD5: problem generating response", localIOException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public String getAuthorizationID() {
/* 698 */     if (this.completed) {
/* 699 */       return this.authzid;
/*     */     }
/* 701 */     throw new IllegalStateException("DIGEST-MD5 server negotiation not complete");
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.security.sasl.digest.DigestMD5Server
 * JD-Core Version:    0.6.2
 */