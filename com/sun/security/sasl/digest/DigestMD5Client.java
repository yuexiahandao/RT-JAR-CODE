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
/*     */ import javax.security.sasl.RealmCallback;
/*     */ import javax.security.sasl.RealmChoiceCallback;
/*     */ import javax.security.sasl.SaslClient;
/*     */ import javax.security.sasl.SaslException;
/*     */ 
/*     */ final class DigestMD5Client extends DigestMD5Base
/*     */   implements SaslClient
/*     */ {
/* 104 */   private static final String MY_CLASS_NAME = DigestMD5Client.class.getName();
/*     */   private static final String CIPHER_PROPERTY = "com.sun.security.sasl.digest.cipher";
/* 111 */   private static final String[] DIRECTIVE_KEY = { "realm", "qop", "algorithm", "nonce", "maxbuf", "charset", "cipher", "rspauth", "stale" };
/*     */   private static final int REALM = 0;
/*     */   private static final int QOP = 1;
/*     */   private static final int ALGORITHM = 2;
/*     */   private static final int NONCE = 3;
/*     */   private static final int MAXBUF = 4;
/*     */   private static final int CHARSET = 5;
/*     */   private static final int CIPHER = 6;
/*     */   private static final int RESPONSE_AUTH = 7;
/*     */   private static final int STALE = 8;
/*     */   private int nonceCount;
/*     */   private String specifiedCipher;
/*     */   private byte[] cnonce;
/*     */   private String username;
/*     */   private char[] passwd;
/*     */   private byte[] authzidBytes;
/*     */ 
/*     */   DigestMD5Client(String paramString1, String paramString2, String paramString3, Map paramMap, CallbackHandler paramCallbackHandler)
/*     */     throws SaslException
/*     */   {
/* 158 */     super(paramMap, MY_CLASS_NAME, 2, paramString2 + "/" + paramString3, paramCallbackHandler);
/*     */ 
/* 161 */     if (paramString1 != null) {
/* 162 */       this.authzid = paramString1;
/*     */       try {
/* 164 */         this.authzidBytes = paramString1.getBytes("UTF8");
/*     */       }
/*     */       catch (UnsupportedEncodingException localUnsupportedEncodingException) {
/* 167 */         throw new SaslException("DIGEST-MD5: Error encoding authzid value into UTF-8", localUnsupportedEncodingException);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 172 */     if (paramMap != null) {
/* 173 */       this.specifiedCipher = ((String)paramMap.get("com.sun.security.sasl.digest.cipher"));
/*     */ 
/* 175 */       logger.log(Level.FINE, "DIGEST60:Explicitly specified cipher: {0}", this.specifiedCipher);
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean hasInitialResponse()
/*     */   {
/* 186 */     return false;
/*     */   }
/*     */ 
/*     */   public byte[] evaluateChallenge(byte[] paramArrayOfByte)
/*     */     throws SaslException
/*     */   {
/* 206 */     if (paramArrayOfByte.length > 2048)
/* 207 */       throw new SaslException("DIGEST-MD5: Invalid digest-challenge length. Got:  " + paramArrayOfByte.length + " Expected < " + 2048);
/*     */     byte[][] arrayOfByte;
/* 215 */     switch (this.step)
/*     */     {
/*     */     case 2:
/* 220 */       ArrayList localArrayList = new ArrayList(3);
/* 221 */       arrayOfByte = parseDirectives(paramArrayOfByte, DIRECTIVE_KEY, localArrayList, 0);
/*     */       try
/*     */       {
/* 225 */         processChallenge(arrayOfByte, localArrayList);
/* 226 */         checkQopSupport(arrayOfByte[1], arrayOfByte[6]);
/* 227 */         this.step += 1;
/* 228 */         return generateClientResponse(arrayOfByte[5]);
/*     */       } catch (SaslException localSaslException) {
/* 230 */         this.step = 0;
/* 231 */         clearPassword();
/* 232 */         throw localSaslException;
/*     */       } catch (IOException localIOException) {
/* 234 */         this.step = 0;
/* 235 */         clearPassword();
/* 236 */         throw new SaslException("DIGEST-MD5: Error generating digest response-value", localIOException);
/*     */       }
/*     */ 
/*     */     case 3:
/*     */       try
/*     */       {
/* 244 */         arrayOfByte = parseDirectives(paramArrayOfByte, DIRECTIVE_KEY, null, 0);
/*     */ 
/* 246 */         validateResponseValue(arrayOfByte[7]);
/*     */ 
/* 250 */         if ((this.integrity) && (this.privacy))
/* 251 */           this.secCtx = new DigestMD5Base.DigestPrivacy(this, true);
/* 252 */         else if (this.integrity) {
/* 253 */           this.secCtx = new DigestMD5Base.DigestIntegrity(this, true);
/*     */         }
/*     */ 
/* 256 */         return null;
/*     */       } finally {
/* 258 */         clearPassword();
/* 259 */         this.step = 0;
/* 260 */         this.completed = true;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 265 */     throw new SaslException("DIGEST-MD5: Client at illegal state");
/*     */   }
/*     */ 
/*     */   private void processChallenge(byte[][] paramArrayOfByte, List<byte[]> paramList)
/*     */     throws SaslException, UnsupportedEncodingException
/*     */   {
/* 283 */     if (paramArrayOfByte[5] != null) {
/* 284 */       if (!"utf-8".equals(new String(paramArrayOfByte[5], this.encoding))) {
/* 285 */         throw new SaslException("DIGEST-MD5: digest-challenge format violation. Unrecognised charset value: " + new String(paramArrayOfByte[5]));
/*     */       }
/*     */ 
/* 289 */       this.encoding = "UTF8";
/* 290 */       this.useUTF8 = true;
/*     */     }
/*     */ 
/* 295 */     if (paramArrayOfByte[2] == null) {
/* 296 */       throw new SaslException("DIGEST-MD5: Digest-challenge format violation: algorithm directive missing");
/*     */     }
/* 298 */     if (!"md5-sess".equals(new String(paramArrayOfByte[2], this.encoding))) {
/* 299 */       throw new SaslException("DIGEST-MD5: Digest-challenge format violation. Invalid value for 'algorithm' directive: " + paramArrayOfByte[2]);
/*     */     }
/*     */ 
/* 305 */     if (paramArrayOfByte[3] == null) {
/* 306 */       throw new SaslException("DIGEST-MD5: Digest-challenge format violation: nonce directive missing");
/*     */     }
/*     */ 
/* 309 */     this.nonce = paramArrayOfByte[3];
/*     */     try
/*     */     {
/* 314 */       String[] arrayOfString = null;
/*     */ 
/* 316 */       if (paramArrayOfByte[0] != null) {
/* 317 */         if ((paramList == null) || (paramList.size() <= 1))
/*     */         {
/* 319 */           this.negotiatedRealm = new String(paramArrayOfByte[0], this.encoding);
/*     */         } else {
/* 321 */           arrayOfString = new String[paramList.size()];
/* 322 */           for (int j = 0; j < arrayOfString.length; j++) {
/* 323 */             arrayOfString[j] = new String((byte[])paramList.get(j), this.encoding);
/*     */           }
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 329 */       NameCallback localNameCallback = this.authzid == null ? new NameCallback("DIGEST-MD5 authentication ID: ") : new NameCallback("DIGEST-MD5 authentication ID: ", this.authzid);
/*     */ 
/* 332 */       PasswordCallback localPasswordCallback = new PasswordCallback("DIGEST-MD5 password: ", false);
/*     */       Object localObject;
/* 335 */       if (arrayOfString == null)
/*     */       {
/* 338 */         localObject = this.negotiatedRealm == null ? new RealmCallback("DIGEST-MD5 realm: ") : new RealmCallback("DIGEST-MD5 realm: ", this.negotiatedRealm);
/*     */ 
/* 342 */         this.cbh.handle(new Callback[] { localObject, localNameCallback, localPasswordCallback });
/*     */ 
/* 345 */         this.negotiatedRealm = ((RealmCallback)localObject).getText();
/* 346 */         if (this.negotiatedRealm == null)
/* 347 */           this.negotiatedRealm = "";
/*     */       }
/*     */       else {
/* 350 */         localObject = new RealmChoiceCallback("DIGEST-MD5 realm: ", arrayOfString, 0, false);
/*     */ 
/* 354 */         this.cbh.handle(new Callback[] { localObject, localNameCallback, localPasswordCallback });
/*     */ 
/* 357 */         this.negotiatedRealm = arrayOfString[localObject.getSelectedIndexes()[0]];
/*     */       }
/*     */ 
/* 360 */       this.passwd = localPasswordCallback.getPassword();
/* 361 */       localPasswordCallback.clearPassword();
/* 362 */       this.username = localNameCallback.getName();
/*     */     }
/*     */     catch (UnsupportedCallbackException localUnsupportedCallbackException) {
/* 365 */       throw new SaslException("DIGEST-MD5: Cannot perform callback to acquire realm, authentication ID or password", localUnsupportedCallbackException);
/*     */     }
/*     */     catch (IOException localIOException)
/*     */     {
/* 369 */       throw new SaslException("DIGEST-MD5: Error acquiring realm, authentication ID or password", localIOException);
/*     */     }
/*     */ 
/* 373 */     if ((this.username == null) || (this.passwd == null)) {
/* 374 */       throw new SaslException("DIGEST-MD5: authentication ID and password must be specified");
/*     */     }
/*     */ 
/* 379 */     int i = paramArrayOfByte[4] == null ? 65536 : Integer.parseInt(new String(paramArrayOfByte[4], this.encoding));
/*     */ 
/* 382 */     this.sendMaxBufSize = (this.sendMaxBufSize == 0 ? i : Math.min(this.sendMaxBufSize, i));
/*     */   }
/*     */ 
/*     */   private void checkQopSupport(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
/*     */     throws IOException
/*     */   {
/*     */     String str;
/* 400 */     if (paramArrayOfByte1 == null)
/* 401 */       str = "auth";
/*     */     else {
/* 403 */       str = new String(paramArrayOfByte1, this.encoding);
/*     */     }
/*     */ 
/* 407 */     String[] arrayOfString = new String[3];
/* 408 */     byte[] arrayOfByte = parseQop(str, arrayOfString, true);
/*     */ 
/* 410 */     byte b = combineMasks(arrayOfByte);
/*     */ 
/* 412 */     switch (findPreferredMask(b, this.qop)) {
/*     */     case 0:
/* 414 */       throw new SaslException("DIGEST-MD5: No common protection layer between client and server");
/*     */     case 1:
/* 418 */       this.negotiatedQop = "auth";
/*     */ 
/* 420 */       break;
/*     */     case 2:
/* 423 */       this.negotiatedQop = "auth-int";
/* 424 */       this.integrity = true;
/* 425 */       this.rawSendSize = (this.sendMaxBufSize - 16);
/* 426 */       break;
/*     */     case 4:
/* 429 */       this.negotiatedQop = "auth-conf";
/* 430 */       this.privacy = (this.integrity = 1);
/* 431 */       this.rawSendSize = (this.sendMaxBufSize - 26);
/* 432 */       checkStrengthSupport(paramArrayOfByte2);
/*     */     case 3:
/*     */     }
/*     */ 
/* 436 */     if (logger.isLoggable(Level.FINE))
/* 437 */       logger.log(Level.FINE, "DIGEST61:Raw send size: {0}", new Integer(this.rawSendSize));
/*     */   }
/*     */ 
/*     */   private void checkStrengthSupport(byte[] paramArrayOfByte)
/*     */     throws IOException
/*     */   {
/* 456 */     if (paramArrayOfByte == null) {
/* 457 */       throw new SaslException("DIGEST-MD5: server did not specify cipher to use for 'auth-conf'");
/*     */     }
/*     */ 
/* 462 */     String str1 = new String(paramArrayOfByte, this.encoding);
/* 463 */     StringTokenizer localStringTokenizer = new StringTokenizer(str1, ", \t\n");
/* 464 */     int i = localStringTokenizer.countTokens();
/* 465 */     String str2 = null;
/* 466 */     byte[] arrayOfByte1 = { 0, 0, 0, 0, 0 };
/*     */ 
/* 471 */     String[] arrayOfString = new String[arrayOfByte1.length];
/*     */ 
/* 474 */     for (int j = 0; j < i; j++) {
/* 475 */       str2 = localStringTokenizer.nextToken();
/* 476 */       for (k = 0; k < CIPHER_TOKENS.length; k++) {
/* 477 */         if (str2.equals(CIPHER_TOKENS[k]))
/*     */         {
/*     */           int tmp126_124 = k;
/*     */           byte[] tmp126_122 = arrayOfByte1; tmp126_122[tmp126_124] = ((byte)(tmp126_122[tmp126_124] | CIPHER_MASKS[k]));
/* 479 */           arrayOfString[k] = str2;
/* 480 */           logger.log(Level.FINE, "DIGEST62:Server supports {0}", str2);
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 486 */     byte[] arrayOfByte2 = getPlatformCiphers();
/*     */ 
/* 489 */     int k = 0;
/* 490 */     for (int m = 0; m < arrayOfByte1.length; m++)
/*     */     {
/*     */       int tmp192_190 = m;
/*     */       byte[] tmp192_188 = arrayOfByte1; tmp192_188[tmp192_190] = ((byte)(tmp192_188[tmp192_190] & arrayOfByte2[m]));
/* 492 */       k = (byte)(k | arrayOfByte1[m]);
/*     */     }
/*     */ 
/* 495 */     if (k == 0) {
/* 496 */       throw new SaslException("DIGEST-MD5: Client supports none of these cipher suites: " + str1);
/*     */     }
/*     */ 
/* 504 */     this.negotiatedCipher = findCipherAndStrength(arrayOfByte1, arrayOfString);
/*     */ 
/* 506 */     if (this.negotiatedCipher == null) {
/* 507 */       throw new SaslException("DIGEST-MD5: Unable to negotiate a strength level for 'auth-conf'");
/*     */     }
/*     */ 
/* 510 */     logger.log(Level.FINE, "DIGEST63:Cipher suite: {0}", this.negotiatedCipher);
/*     */   }
/*     */ 
/*     */   private String findCipherAndStrength(byte[] paramArrayOfByte, String[] paramArrayOfString)
/*     */   {
/* 525 */     for (int j = 0; j < this.strength.length; j++)
/*     */     {
/*     */       int i;
/* 526 */       if ((i = this.strength[j]) != 0) {
/* 527 */         for (int k = 0; k < paramArrayOfByte.length; k++)
/*     */         {
/* 532 */           if ((i == paramArrayOfByte[k]) && ((this.specifiedCipher == null) || (this.specifiedCipher.equals(paramArrayOfString[k]))))
/*     */           {
/* 535 */             switch (i) {
/*     */             case 4:
/* 537 */               this.negotiatedStrength = "high";
/* 538 */               break;
/*     */             case 2:
/* 540 */               this.negotiatedStrength = "medium";
/* 541 */               break;
/*     */             case 1:
/* 543 */               this.negotiatedStrength = "low";
/*     */             case 3:
/*     */             }
/*     */ 
/* 547 */             return paramArrayOfString[k];
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 553 */     return null;
/*     */   }
/*     */ 
/*     */   private byte[] generateClientResponse(byte[] paramArrayOfByte)
/*     */     throws IOException
/*     */   {
/* 572 */     ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
/*     */ 
/* 574 */     if (this.useUTF8) {
/* 575 */       localByteArrayOutputStream.write("charset=".getBytes(this.encoding));
/* 576 */       localByteArrayOutputStream.write(paramArrayOfByte);
/* 577 */       localByteArrayOutputStream.write(44);
/*     */     }
/*     */ 
/* 580 */     localByteArrayOutputStream.write(("username=\"" + quotedStringValue(this.username) + "\",").getBytes(this.encoding));
/*     */ 
/* 583 */     if (this.negotiatedRealm.length() > 0) {
/* 584 */       localByteArrayOutputStream.write(("realm=\"" + quotedStringValue(this.negotiatedRealm) + "\",").getBytes(this.encoding));
/*     */     }
/*     */ 
/* 588 */     localByteArrayOutputStream.write("nonce=\"".getBytes(this.encoding));
/* 589 */     writeQuotedStringValue(localByteArrayOutputStream, this.nonce);
/* 590 */     localByteArrayOutputStream.write(34);
/* 591 */     localByteArrayOutputStream.write(44);
/*     */ 
/* 593 */     this.nonceCount = getNonceCount(this.nonce);
/* 594 */     localByteArrayOutputStream.write(("nc=" + nonceCountToHex(this.nonceCount) + ",").getBytes(this.encoding));
/*     */ 
/* 597 */     this.cnonce = generateNonce();
/* 598 */     localByteArrayOutputStream.write("cnonce=\"".getBytes(this.encoding));
/* 599 */     writeQuotedStringValue(localByteArrayOutputStream, this.cnonce);
/* 600 */     localByteArrayOutputStream.write("\",".getBytes(this.encoding));
/* 601 */     localByteArrayOutputStream.write(("digest-uri=\"" + this.digestUri + "\",").getBytes(this.encoding));
/*     */ 
/* 603 */     localByteArrayOutputStream.write("maxbuf=".getBytes(this.encoding));
/* 604 */     localByteArrayOutputStream.write(String.valueOf(this.recvMaxBufSize).getBytes(this.encoding));
/* 605 */     localByteArrayOutputStream.write(44);
/*     */     try
/*     */     {
/* 608 */       localByteArrayOutputStream.write("response=".getBytes(this.encoding));
/* 609 */       localByteArrayOutputStream.write(generateResponseValue("AUTHENTICATE", this.digestUri, this.negotiatedQop, this.username, this.negotiatedRealm, this.passwd, this.nonce, this.cnonce, this.nonceCount, this.authzidBytes));
/*     */ 
/* 613 */       localByteArrayOutputStream.write(44);
/*     */     } catch (Exception localException) {
/* 615 */       throw new SaslException("DIGEST-MD5: Error generating response value", localException);
/*     */     }
/*     */ 
/* 619 */     localByteArrayOutputStream.write(("qop=" + this.negotiatedQop).getBytes(this.encoding));
/*     */ 
/* 621 */     if (this.negotiatedCipher != null) {
/* 622 */       localByteArrayOutputStream.write((",cipher=\"" + this.negotiatedCipher + "\"").getBytes(this.encoding));
/*     */     }
/*     */ 
/* 625 */     if (this.authzidBytes != null) {
/* 626 */       localByteArrayOutputStream.write(",authzid=\"".getBytes(this.encoding));
/* 627 */       writeQuotedStringValue(localByteArrayOutputStream, this.authzidBytes);
/* 628 */       localByteArrayOutputStream.write("\"".getBytes(this.encoding));
/*     */     }
/*     */ 
/* 631 */     if (localByteArrayOutputStream.size() > 4096) {
/* 632 */       throw new SaslException("DIGEST-MD5: digest-response size too large. Length: " + localByteArrayOutputStream.size());
/*     */     }
/*     */ 
/* 635 */     return localByteArrayOutputStream.toByteArray();
/*     */   }
/*     */ 
/*     */   private void validateResponseValue(byte[] paramArrayOfByte)
/*     */     throws SaslException
/*     */   {
/* 653 */     if (paramArrayOfByte == null) {
/* 654 */       throw new SaslException("DIGEST-MD5: Authenication failed. Expecting 'rspauth' authentication success message");
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 659 */       byte[] arrayOfByte = generateResponseValue("", this.digestUri, this.negotiatedQop, this.username, this.negotiatedRealm, this.passwd, this.nonce, this.cnonce, this.nonceCount, this.authzidBytes);
/*     */ 
/* 662 */       if (!Arrays.equals(arrayOfByte, paramArrayOfByte))
/*     */       {
/* 664 */         throw new SaslException("Server's rspauth value does not match what client expects");
/*     */       }
/*     */     }
/*     */     catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {
/* 668 */       throw new SaslException("Problem generating response value for verification", localNoSuchAlgorithmException);
/*     */     }
/*     */     catch (IOException localIOException) {
/* 671 */       throw new SaslException("Problem generating response value for verification", localIOException);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static int getNonceCount(byte[] paramArrayOfByte)
/*     */   {
/* 685 */     return 1;
/*     */   }
/*     */ 
/*     */   private void clearPassword() {
/* 689 */     if (this.passwd != null) {
/* 690 */       for (int i = 0; i < this.passwd.length; i++) {
/* 691 */         this.passwd[i] = '\000';
/*     */       }
/* 693 */       this.passwd = null;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.security.sasl.digest.DigestMD5Client
 * JD-Core Version:    0.6.2
 */