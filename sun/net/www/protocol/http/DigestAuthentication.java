/*     */ package sun.net.www.protocol.http;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.Serializable;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.net.PasswordAuthentication;
/*     */ import java.net.ProtocolException;
/*     */ import java.net.URL;
/*     */ import java.security.MessageDigest;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.util.Arrays;
/*     */ import java.util.Random;
/*     */ import java.util.StringTokenizer;
/*     */ import sun.net.www.HeaderParser;
/*     */ 
/*     */ class DigestAuthentication extends AuthenticationInfo
/*     */ {
/*     */   private static final long serialVersionUID = 100L;
/*     */   private String authMethod;
/*     */   Parameters params;
/* 479 */   private static final char[] charArray = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
/*     */ 
/* 484 */   private static final String[] zeroPad = { "00000000", "0000000", "000000", "00000", "0000", "000", "00", "0" };
/*     */ 
/*     */   public DigestAuthentication(boolean paramBoolean, URL paramURL, String paramString1, String paramString2, PasswordAuthentication paramPasswordAuthentication, Parameters paramParameters)
/*     */   {
/* 178 */     super(paramBoolean ? 'p' : 's', AuthScheme.DIGEST, paramURL, paramString1);
/*     */ 
/* 182 */     this.authMethod = paramString2;
/* 183 */     this.pw = paramPasswordAuthentication;
/* 184 */     this.params = paramParameters;
/*     */   }
/*     */ 
/*     */   public DigestAuthentication(boolean paramBoolean, String paramString1, int paramInt, String paramString2, String paramString3, PasswordAuthentication paramPasswordAuthentication, Parameters paramParameters)
/*     */   {
/* 190 */     super(paramBoolean ? 'p' : 's', AuthScheme.DIGEST, paramString1, paramInt, paramString2);
/*     */ 
/* 195 */     this.authMethod = paramString3;
/* 196 */     this.pw = paramPasswordAuthentication;
/* 197 */     this.params = paramParameters;
/*     */   }
/*     */ 
/*     */   public boolean supportsPreemptiveAuthorization()
/*     */   {
/* 205 */     return true;
/*     */   }
/*     */ 
/*     */   public String getHeaderValue(URL paramURL, String paramString)
/*     */   {
/* 224 */     return getHeaderValueImpl(paramURL.getFile(), paramString);
/*     */   }
/*     */ 
/*     */   String getHeaderValue(String paramString1, String paramString2)
/*     */   {
/* 242 */     return getHeaderValueImpl(paramString1, paramString2);
/*     */   }
/*     */ 
/*     */   public boolean isAuthorizationStale(String paramString)
/*     */   {
/* 255 */     HeaderParser localHeaderParser = new HeaderParser(paramString);
/* 256 */     String str1 = localHeaderParser.findValue("stale");
/* 257 */     if ((str1 == null) || (!str1.equals("true")))
/* 258 */       return false;
/* 259 */     String str2 = localHeaderParser.findValue("nonce");
/* 260 */     if ((str2 == null) || ("".equals(str2))) {
/* 261 */       return false;
/*     */     }
/* 263 */     this.params.setNonce(str2);
/* 264 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean setHeaders(HttpURLConnection paramHttpURLConnection, HeaderParser paramHeaderParser, String paramString)
/*     */   {
/* 276 */     this.params.setNonce(paramHeaderParser.findValue("nonce"));
/* 277 */     this.params.setOpaque(paramHeaderParser.findValue("opaque"));
/* 278 */     this.params.setQop(paramHeaderParser.findValue("qop"));
/*     */ 
/* 280 */     String str1 = "";
/*     */     String str2;
/* 282 */     if ((this.type == 'p') && (paramHttpURLConnection.tunnelState() == HttpURLConnection.TunnelState.SETUP))
/*     */     {
/* 284 */       str1 = HttpURLConnection.connectRequestURI(paramHttpURLConnection.getURL());
/* 285 */       str2 = HttpURLConnection.HTTP_CONNECT;
/*     */     } else {
/*     */       try {
/* 288 */         str1 = paramHttpURLConnection.getRequestURI(); } catch (IOException localIOException) {
/*     */       }
/* 290 */       str2 = paramHttpURLConnection.getMethod();
/*     */     }
/*     */ 
/* 293 */     if ((this.params.nonce == null) || (this.authMethod == null) || (this.pw == null) || (this.realm == null)) {
/* 294 */       return false;
/*     */     }
/* 296 */     if (this.authMethod.length() >= 1)
/*     */     {
/* 300 */       this.authMethod = (Character.toUpperCase(this.authMethod.charAt(0)) + this.authMethod.substring(1).toLowerCase());
/*     */     }
/*     */ 
/* 303 */     String str3 = paramHeaderParser.findValue("algorithm");
/* 304 */     if ((str3 == null) || ("".equals(str3))) {
/* 305 */       str3 = "MD5";
/*     */     }
/* 307 */     this.params.setAlgorithm(str3);
/*     */ 
/* 313 */     if (this.params.authQop()) {
/* 314 */       this.params.setNewCnonce();
/*     */     }
/*     */ 
/* 317 */     String str4 = getHeaderValueImpl(str1, str2);
/* 318 */     if (str4 != null) {
/* 319 */       paramHttpURLConnection.setAuthenticationProperty(getHeaderName(), str4);
/* 320 */       return true;
/*     */     }
/* 322 */     return false;
/*     */   }
/*     */ 
/*     */   private String getHeaderValueImpl(String paramString1, String paramString2)
/*     */   {
/* 331 */     char[] arrayOfChar = this.pw.getPassword();
/* 332 */     boolean bool = this.params.authQop();
/* 333 */     String str2 = this.params.getOpaque();
/* 334 */     String str3 = this.params.getCnonce();
/* 335 */     String str4 = this.params.getNonce();
/* 336 */     String str5 = this.params.getAlgorithm();
/* 337 */     this.params.incrementNC();
/* 338 */     int i = this.params.getNCCount();
/* 339 */     String str6 = null;
/*     */ 
/* 341 */     if (i != -1) {
/* 342 */       str6 = Integer.toHexString(i).toLowerCase();
/* 343 */       int j = str6.length();
/* 344 */       if (j < 8)
/* 345 */         str6 = zeroPad[j] + str6;
/*     */     }
/*     */     String str1;
/*     */     try {
/* 349 */       str1 = computeDigest(true, this.pw.getUserName(), arrayOfChar, this.realm, paramString2, paramString1, str4, str3, str6);
/*     */     }
/*     */     catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {
/* 352 */       return null;
/*     */     }
/*     */ 
/* 355 */     String str7 = "\"";
/* 356 */     if (bool) {
/* 357 */       str7 = "\", nc=" + str6;
/*     */     }
/*     */ 
/* 360 */     String str8 = this.authMethod + " username=\"" + this.pw.getUserName() + "\", realm=\"" + this.realm + "\", nonce=\"" + str4 + str7 + ", uri=\"" + paramString1 + "\", response=\"" + str1 + "\", algorithm=\"" + str5;
/*     */ 
/* 368 */     if (str2 != null) {
/* 369 */       str8 = str8 + "\", opaque=\"" + str2;
/*     */     }
/* 371 */     if (str3 != null) {
/* 372 */       str8 = str8 + "\", cnonce=\"" + str3;
/*     */     }
/* 374 */     if (bool) {
/* 375 */       str8 = str8 + "\", qop=\"auth";
/*     */     }
/* 377 */     str8 = str8 + "\"";
/* 378 */     return str8;
/*     */   }
/*     */ 
/*     */   public void checkResponse(String paramString1, String paramString2, URL paramURL) throws IOException
/*     */   {
/* 383 */     checkResponse(paramString1, paramString2, paramURL.getFile());
/*     */   }
/*     */ 
/*     */   public void checkResponse(String paramString1, String paramString2, String paramString3) throws IOException
/*     */   {
/* 388 */     char[] arrayOfChar = this.pw.getPassword();
/* 389 */     String str1 = this.pw.getUserName();
/* 390 */     boolean bool = this.params.authQop();
/* 391 */     String str2 = this.params.getOpaque();
/* 392 */     String str3 = this.params.cnonce;
/* 393 */     String str4 = this.params.getNonce();
/* 394 */     String str5 = this.params.getAlgorithm();
/* 395 */     int i = this.params.getNCCount();
/* 396 */     String str6 = null;
/*     */ 
/* 398 */     if (paramString1 == null) {
/* 399 */       throw new ProtocolException("No authentication information in response");
/*     */     }
/*     */ 
/* 402 */     if (i != -1) {
/* 403 */       str6 = Integer.toHexString(i).toUpperCase();
/* 404 */       int j = str6.length();
/* 405 */       if (j < 8)
/* 406 */         str6 = zeroPad[j] + str6;
/*     */     }
/*     */     try {
/* 409 */       String str7 = computeDigest(false, str1, arrayOfChar, this.realm, paramString2, paramString3, str4, str3, str6);
/*     */ 
/* 411 */       HeaderParser localHeaderParser = new HeaderParser(paramString1);
/* 412 */       String str8 = localHeaderParser.findValue("rspauth");
/* 413 */       if (str8 == null) {
/* 414 */         throw new ProtocolException("No digest in response");
/*     */       }
/* 416 */       if (!str8.equals(str7)) {
/* 417 */         throw new ProtocolException("Response digest invalid");
/*     */       }
/*     */ 
/* 420 */       String str9 = localHeaderParser.findValue("nextnonce");
/* 421 */       if ((str9 != null) && (!"".equals(str9)))
/* 422 */         this.params.setNonce(str9);
/*     */     }
/*     */     catch (NoSuchAlgorithmException localNoSuchAlgorithmException)
/*     */     {
/* 426 */       throw new ProtocolException("Unsupported algorithm in response");
/*     */     }
/*     */   }
/*     */ 
/*     */   private String computeDigest(boolean paramBoolean, String paramString1, char[] paramArrayOfChar, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6, String paramString7)
/*     */     throws NoSuchAlgorithmException
/*     */   {
/* 439 */     String str3 = this.params.getAlgorithm();
/* 440 */     boolean bool = str3.equalsIgnoreCase("MD5-sess");
/*     */ 
/* 442 */     MessageDigest localMessageDigest = MessageDigest.getInstance(bool ? "MD5" : str3);
/*     */     String str2;
/*     */     String str4;
/*     */     String str1;
/* 444 */     if (bool) {
/* 445 */       if ((str2 = this.params.getCachedHA1()) == null) {
/* 446 */         str4 = paramString1 + ":" + paramString2 + ":";
/* 447 */         str5 = encode(str4, paramArrayOfChar, localMessageDigest);
/* 448 */         str1 = str5 + ":" + paramString5 + ":" + paramString6;
/* 449 */         str2 = encode(str1, null, localMessageDigest);
/* 450 */         this.params.setCachedHA1(str2);
/*     */       }
/*     */     } else {
/* 453 */       str1 = paramString1 + ":" + paramString2 + ":";
/* 454 */       str2 = encode(str1, paramArrayOfChar, localMessageDigest);
/*     */     }
/*     */ 
/* 458 */     if (paramBoolean)
/* 459 */       str4 = paramString3 + ":" + paramString4;
/*     */     else {
/* 461 */       str4 = ":" + paramString4;
/*     */     }
/* 463 */     String str5 = encode(str4, null, localMessageDigest);
/*     */     String str6;
/* 466 */     if (this.params.authQop()) {
/* 467 */       str6 = str2 + ":" + paramString5 + ":" + paramString7 + ":" + paramString6 + ":auth:" + str5;
/*     */     }
/*     */     else
/*     */     {
/* 471 */       str6 = str2 + ":" + paramString5 + ":" + str5;
/*     */     }
/*     */ 
/* 475 */     String str7 = encode(str6, null, localMessageDigest);
/* 476 */     return str7;
/*     */   }
/*     */ 
/*     */   private String encode(String paramString, char[] paramArrayOfChar, MessageDigest paramMessageDigest)
/*     */   {
/*     */     try
/*     */     {
/* 491 */       paramMessageDigest.update(paramString.getBytes("ISO-8859-1"));
/*     */     } catch (UnsupportedEncodingException localUnsupportedEncodingException) {
/* 493 */       if (!$assertionsDisabled) throw new AssertionError();
/*     */     }
/* 495 */     if (paramArrayOfChar != null) {
/* 496 */       arrayOfByte = new byte[paramArrayOfChar.length];
/* 497 */       for (int i = 0; i < paramArrayOfChar.length; i++)
/* 498 */         arrayOfByte[i] = ((byte)paramArrayOfChar[i]);
/* 499 */       paramMessageDigest.update(arrayOfByte);
/* 500 */       Arrays.fill(arrayOfByte, (byte)0);
/*     */     }
/* 502 */     byte[] arrayOfByte = paramMessageDigest.digest();
/*     */ 
/* 504 */     StringBuffer localStringBuffer = new StringBuffer(arrayOfByte.length * 2);
/* 505 */     for (int j = 0; j < arrayOfByte.length; j++) {
/* 506 */       int k = arrayOfByte[j] >>> 4 & 0xF;
/* 507 */       localStringBuffer.append(charArray[k]);
/* 508 */       k = arrayOfByte[j] & 0xF;
/* 509 */       localStringBuffer.append(charArray[k]);
/*     */     }
/* 511 */     return localStringBuffer.toString();
/*     */   }
/*     */ 
/*     */   static class Parameters
/*     */     implements Serializable
/*     */   {
/*     */     private static final long serialVersionUID = -3584543755194526252L;
/*     */     private boolean serverQop;
/*     */     private String opaque;
/*     */     private String cnonce;
/*     */     private String nonce;
/*     */     private String algorithm;
/*  66 */     private int NCcount = 0;
/*     */     private String cachedHA1;
/*  72 */     private boolean redoCachedHA1 = true;
/*     */     private static final int cnonceRepeat = 5;
/*     */     private static final int cnoncelen = 40;
/*  81 */     private static Random random = new Random();
/*     */ 
/* 103 */     int cnonce_count = 0;
/*     */ 
/*     */     Parameters()
/*     */     {
/*  85 */       this.serverQop = false;
/*  86 */       this.opaque = null;
/*  87 */       this.algorithm = null;
/*  88 */       this.cachedHA1 = null;
/*  89 */       this.nonce = null;
/*  90 */       setNewCnonce();
/*     */     }
/*     */ 
/*     */     boolean authQop() {
/*  94 */       return this.serverQop;
/*     */     }
/*     */     synchronized void incrementNC() {
/*  97 */       this.NCcount += 1;
/*     */     }
/*     */     synchronized int getNCCount() {
/* 100 */       return this.NCcount;
/*     */     }
/*     */ 
/*     */     synchronized String getCnonce()
/*     */     {
/* 107 */       if (this.cnonce_count >= 5) {
/* 108 */         setNewCnonce();
/*     */       }
/* 110 */       this.cnonce_count += 1;
/* 111 */       return this.cnonce;
/*     */     }
/*     */     synchronized void setNewCnonce() {
/* 114 */       byte[] arrayOfByte = new byte[20];
/* 115 */       char[] arrayOfChar = new char[40];
/* 116 */       random.nextBytes(arrayOfByte);
/* 117 */       for (int i = 0; i < 20; i++) {
/* 118 */         int j = arrayOfByte[i] + 128;
/* 119 */         arrayOfChar[(i * 2)] = ((char)(65 + j / 16));
/* 120 */         arrayOfChar[(i * 2 + 1)] = ((char)(65 + j % 16));
/*     */       }
/* 122 */       this.cnonce = new String(arrayOfChar, 0, 40);
/* 123 */       this.cnonce_count = 0;
/* 124 */       this.redoCachedHA1 = true;
/*     */     }
/*     */ 
/*     */     synchronized void setQop(String paramString) {
/* 128 */       if (paramString != null) {
/* 129 */         StringTokenizer localStringTokenizer = new StringTokenizer(paramString, " ");
/* 130 */         while (localStringTokenizer.hasMoreTokens()) {
/* 131 */           if (localStringTokenizer.nextToken().equalsIgnoreCase("auth")) {
/* 132 */             this.serverQop = true;
/* 133 */             return;
/*     */           }
/*     */         }
/*     */       }
/* 137 */       this.serverQop = false;
/*     */     }
/*     */     synchronized String getOpaque() {
/* 140 */       return this.opaque; } 
/* 141 */     synchronized void setOpaque(String paramString) { this.opaque = paramString; } 
/*     */     synchronized String getNonce() {
/* 143 */       return this.nonce;
/*     */     }
/*     */     synchronized void setNonce(String paramString) {
/* 146 */       if (!paramString.equals(this.nonce)) {
/* 147 */         this.nonce = paramString;
/* 148 */         this.NCcount = 0;
/* 149 */         this.redoCachedHA1 = true;
/*     */       }
/*     */     }
/*     */ 
/*     */     synchronized String getCachedHA1() {
/* 154 */       if (this.redoCachedHA1) {
/* 155 */         return null;
/*     */       }
/* 157 */       return this.cachedHA1;
/*     */     }
/*     */ 
/*     */     synchronized void setCachedHA1(String paramString)
/*     */     {
/* 162 */       this.cachedHA1 = paramString;
/* 163 */       this.redoCachedHA1 = false;
/*     */     }
/*     */     synchronized String getAlgorithm() {
/* 166 */       return this.algorithm; } 
/* 167 */     synchronized void setAlgorithm(String paramString) { this.algorithm = paramString; }
/*     */ 
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.net.www.protocol.http.DigestAuthentication
 * JD-Core Version:    0.6.2
 */