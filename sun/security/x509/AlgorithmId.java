/*     */ package sun.security.x509;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.security.AlgorithmParameters;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.Provider;
/*     */ import java.security.Security;
/*     */ import java.util.Enumeration;
/*     */ import java.util.HashMap;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import sun.security.ec.ECKeyFactory;
/*     */ import sun.security.util.DerEncoder;
/*     */ import sun.security.util.DerInputStream;
/*     */ import sun.security.util.DerOutputStream;
/*     */ import sun.security.util.DerValue;
/*     */ import sun.security.util.ObjectIdentifier;
/*     */ 
/*     */ public class AlgorithmId
/*     */   implements Serializable, DerEncoder
/*     */ {
/*     */   private static final long serialVersionUID = 7205873507486557157L;
/*     */   private ObjectIdentifier algid;
/*     */   private AlgorithmParameters algParams;
/*  70 */   private boolean constructedFromDer = true;
/*     */   protected DerValue params;
/* 595 */   private static boolean initOidTable = false;
/*     */   private static Map<String, ObjectIdentifier> oidTable;
/*     */   private static final Map<ObjectIdentifier, String> nameTable;
/* 609 */   public static final ObjectIdentifier MD2_oid = ObjectIdentifier.newInternal(new int[] { 1, 2, 840, 113549, 2, 2 });
/*     */ 
/* 616 */   public static final ObjectIdentifier MD5_oid = ObjectIdentifier.newInternal(new int[] { 1, 2, 840, 113549, 2, 5 });
/*     */ 
/* 625 */   public static final ObjectIdentifier SHA_oid = ObjectIdentifier.newInternal(new int[] { 1, 3, 14, 3, 2, 26 });
/*     */ 
/* 628 */   public static final ObjectIdentifier SHA256_oid = ObjectIdentifier.newInternal(new int[] { 2, 16, 840, 1, 101, 3, 4, 2, 1 });
/*     */ 
/* 631 */   public static final ObjectIdentifier SHA384_oid = ObjectIdentifier.newInternal(new int[] { 2, 16, 840, 1, 101, 3, 4, 2, 2 });
/*     */ 
/* 634 */   public static final ObjectIdentifier SHA512_oid = ObjectIdentifier.newInternal(new int[] { 2, 16, 840, 1, 101, 3, 4, 2, 3 });
/*     */ 
/* 640 */   private static final int[] DH_data = { 1, 2, 840, 113549, 1, 3, 1 };
/* 641 */   private static final int[] DH_PKIX_data = { 1, 2, 840, 10046, 2, 1 };
/* 642 */   private static final int[] DSA_OIW_data = { 1, 3, 14, 3, 2, 12 };
/* 643 */   private static final int[] DSA_PKIX_data = { 1, 2, 840, 10040, 4, 1 };
/* 644 */   private static final int[] RSA_data = { 2, 5, 8, 1, 1 };
/* 645 */   private static final int[] RSAEncryption_data = { 1, 2, 840, 113549, 1, 1, 1 };
/*     */   public static final ObjectIdentifier DH_oid;
/*     */   public static final ObjectIdentifier DH_PKIX_oid;
/*     */   public static final ObjectIdentifier DSA_oid;
/*     */   public static final ObjectIdentifier DSA_OIW_oid;
/* 652 */   public static final ObjectIdentifier EC_oid = oid(new int[] { 1, 2, 840, 10045, 2, 1 });
/*     */   public static final ObjectIdentifier RSA_oid;
/*     */   public static final ObjectIdentifier RSAEncryption_oid;
/* 659 */   private static final int[] md2WithRSAEncryption_data = { 1, 2, 840, 113549, 1, 1, 2 };
/*     */ 
/* 661 */   private static final int[] md5WithRSAEncryption_data = { 1, 2, 840, 113549, 1, 1, 4 };
/*     */ 
/* 663 */   private static final int[] sha1WithRSAEncryption_data = { 1, 2, 840, 113549, 1, 1, 5 };
/*     */ 
/* 665 */   private static final int[] sha1WithRSAEncryption_OIW_data = { 1, 3, 14, 3, 2, 29 };
/*     */ 
/* 667 */   private static final int[] sha256WithRSAEncryption_data = { 1, 2, 840, 113549, 1, 1, 11 };
/*     */ 
/* 669 */   private static final int[] sha384WithRSAEncryption_data = { 1, 2, 840, 113549, 1, 1, 12 };
/*     */ 
/* 671 */   private static final int[] sha512WithRSAEncryption_data = { 1, 2, 840, 113549, 1, 1, 13 };
/*     */ 
/* 673 */   private static final int[] shaWithDSA_OIW_data = { 1, 3, 14, 3, 2, 13 };
/*     */ 
/* 675 */   private static final int[] sha1WithDSA_OIW_data = { 1, 3, 14, 3, 2, 27 };
/*     */ 
/* 677 */   private static final int[] dsaWithSHA1_PKIX_data = { 1, 2, 840, 10040, 4, 3 };
/*     */   public static final ObjectIdentifier md2WithRSAEncryption_oid;
/*     */   public static final ObjectIdentifier md5WithRSAEncryption_oid;
/*     */   public static final ObjectIdentifier sha1WithRSAEncryption_oid;
/*     */   public static final ObjectIdentifier sha1WithRSAEncryption_OIW_oid;
/*     */   public static final ObjectIdentifier sha256WithRSAEncryption_oid;
/*     */   public static final ObjectIdentifier sha384WithRSAEncryption_oid;
/*     */   public static final ObjectIdentifier sha512WithRSAEncryption_oid;
/*     */   public static final ObjectIdentifier shaWithDSA_OIW_oid;
/*     */   public static final ObjectIdentifier sha1WithDSA_OIW_oid;
/*     */   public static final ObjectIdentifier sha1WithDSA_oid;
/* 691 */   public static final ObjectIdentifier sha1WithECDSA_oid = oid(new int[] { 1, 2, 840, 10045, 4, 1 });
/*     */ 
/* 693 */   public static final ObjectIdentifier sha224WithECDSA_oid = oid(new int[] { 1, 2, 840, 10045, 4, 3, 1 });
/*     */ 
/* 695 */   public static final ObjectIdentifier sha256WithECDSA_oid = oid(new int[] { 1, 2, 840, 10045, 4, 3, 2 });
/*     */ 
/* 697 */   public static final ObjectIdentifier sha384WithECDSA_oid = oid(new int[] { 1, 2, 840, 10045, 4, 3, 3 });
/*     */ 
/* 699 */   public static final ObjectIdentifier sha512WithECDSA_oid = oid(new int[] { 1, 2, 840, 10045, 4, 3, 4 });
/*     */ 
/* 701 */   public static final ObjectIdentifier specifiedWithECDSA_oid = oid(new int[] { 1, 2, 840, 10045, 4, 3 });
/*     */ 
/* 708 */   public static final ObjectIdentifier pbeWithMD5AndDES_oid = ObjectIdentifier.newInternal(new int[] { 1, 2, 840, 113549, 1, 5, 3 });
/*     */ 
/* 710 */   public static final ObjectIdentifier pbeWithMD5AndRC2_oid = ObjectIdentifier.newInternal(new int[] { 1, 2, 840, 113549, 1, 5, 6 });
/*     */ 
/* 712 */   public static final ObjectIdentifier pbeWithSHA1AndDES_oid = ObjectIdentifier.newInternal(new int[] { 1, 2, 840, 113549, 1, 5, 10 });
/*     */ 
/* 714 */   public static final ObjectIdentifier pbeWithSHA1AndRC2_oid = ObjectIdentifier.newInternal(new int[] { 1, 2, 840, 113549, 1, 5, 11 });
/*     */ 
/* 716 */   public static ObjectIdentifier pbeWithSHA1AndDESede_oid = ObjectIdentifier.newInternal(new int[] { 1, 2, 840, 113549, 1, 12, 1, 3 });
/*     */ 
/* 718 */   public static ObjectIdentifier pbeWithSHA1AndRC2_40_oid = ObjectIdentifier.newInternal(new int[] { 1, 2, 840, 113549, 1, 12, 1, 6 });
/*     */ 
/*     */   @Deprecated
/*     */   public AlgorithmId()
/*     */   {
/*     */   }
/*     */ 
/*     */   public AlgorithmId(ObjectIdentifier paramObjectIdentifier)
/*     */   {
/*  94 */     this.algid = paramObjectIdentifier;
/*     */   }
/*     */ 
/*     */   public AlgorithmId(ObjectIdentifier paramObjectIdentifier, AlgorithmParameters paramAlgorithmParameters)
/*     */   {
/* 104 */     this.algid = paramObjectIdentifier;
/* 105 */     this.algParams = paramAlgorithmParameters;
/* 106 */     this.constructedFromDer = false;
/*     */   }
/*     */ 
/*     */   private AlgorithmId(ObjectIdentifier paramObjectIdentifier, DerValue paramDerValue) throws IOException
/*     */   {
/* 111 */     this.algid = paramObjectIdentifier;
/* 112 */     this.params = paramDerValue;
/* 113 */     if (this.params != null)
/* 114 */       decodeParams();
/*     */   }
/*     */ 
/*     */   protected void decodeParams() throws IOException
/*     */   {
/* 119 */     String str = this.algid.toString();
/*     */     try {
/* 121 */       this.algParams = AlgorithmParameters.getInstance(str);
/*     */     }
/*     */     catch (NoSuchAlgorithmException localNoSuchAlgorithmException1)
/*     */     {
/*     */       try
/*     */       {
/* 127 */         this.algParams = AlgorithmParameters.getInstance(str, ECKeyFactory.ecInternalProvider);
/*     */       }
/*     */       catch (NoSuchAlgorithmException localNoSuchAlgorithmException2)
/*     */       {
/* 134 */         this.algParams = null;
/* 135 */         return;
/*     */       }
/*     */     }
/*     */ 
/* 139 */     this.algParams.init(this.params.toByteArray());
/*     */   }
/*     */ 
/*     */   public final void encode(DerOutputStream paramDerOutputStream)
/*     */     throws IOException
/*     */   {
/* 146 */     derEncode(paramDerOutputStream);
/*     */   }
/*     */ 
/*     */   public void derEncode(OutputStream paramOutputStream)
/*     */     throws IOException
/*     */   {
/* 159 */     DerOutputStream localDerOutputStream1 = new DerOutputStream();
/* 160 */     DerOutputStream localDerOutputStream2 = new DerOutputStream();
/*     */ 
/* 162 */     localDerOutputStream1.putOID(this.algid);
/*     */ 
/* 164 */     if (!this.constructedFromDer) {
/* 165 */       if (this.algParams != null)
/* 166 */         this.params = new DerValue(this.algParams.getEncoded());
/*     */       else {
/* 168 */         this.params = null;
/*     */       }
/*     */     }
/* 171 */     if (this.params == null)
/*     */     {
/* 197 */       localDerOutputStream1.putNull();
/*     */     }
/* 199 */     else localDerOutputStream1.putDerValue(this.params);
/*     */ 
/* 201 */     localDerOutputStream2.write((byte)48, localDerOutputStream1);
/* 202 */     paramOutputStream.write(localDerOutputStream2.toByteArray());
/*     */   }
/*     */ 
/*     */   public final byte[] encode()
/*     */     throws IOException
/*     */   {
/* 210 */     DerOutputStream localDerOutputStream = new DerOutputStream();
/* 211 */     derEncode(localDerOutputStream);
/* 212 */     return localDerOutputStream.toByteArray();
/*     */   }
/*     */ 
/*     */   public final ObjectIdentifier getOID()
/*     */   {
/* 223 */     return this.algid;
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/* 235 */     String str1 = (String)nameTable.get(this.algid);
/* 236 */     if (str1 != null) {
/* 237 */       return str1;
/*     */     }
/* 239 */     if ((this.params != null) && (this.algid.equals(specifiedWithECDSA_oid)))
/*     */       try {
/* 241 */         AlgorithmId localAlgorithmId = parse(new DerValue(getEncodedParams()));
/*     */ 
/* 243 */         String str2 = localAlgorithmId.getName();
/* 244 */         if (str2.equals("SHA")) {
/* 245 */           str2 = "SHA1";
/*     */         }
/* 247 */         str1 = str2 + "withECDSA";
/*     */       }
/*     */       catch (IOException localIOException)
/*     */       {
/*     */       }
/* 252 */     return str1 == null ? this.algid.toString() : str1;
/*     */   }
/*     */ 
/*     */   public AlgorithmParameters getParameters() {
/* 256 */     return this.algParams;
/*     */   }
/*     */ 
/*     */   public byte[] getEncodedParams()
/*     */     throws IOException
/*     */   {
/* 266 */     return this.params == null ? null : this.params.toByteArray();
/*     */   }
/*     */ 
/*     */   public boolean equals(AlgorithmId paramAlgorithmId)
/*     */   {
/* 274 */     boolean bool = this.params == null ? false : paramAlgorithmId.params == null ? true : this.params.equals(paramAlgorithmId.params);
/*     */ 
/* 276 */     return (this.algid.equals(paramAlgorithmId.algid)) && (bool);
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 287 */     if (this == paramObject) {
/* 288 */       return true;
/*     */     }
/* 290 */     if ((paramObject instanceof AlgorithmId))
/* 291 */       return equals((AlgorithmId)paramObject);
/* 292 */     if ((paramObject instanceof ObjectIdentifier)) {
/* 293 */       return equals((ObjectIdentifier)paramObject);
/*     */     }
/* 295 */     return false;
/*     */   }
/*     */ 
/*     */   public final boolean equals(ObjectIdentifier paramObjectIdentifier)
/*     */   {
/* 304 */     return this.algid.equals(paramObjectIdentifier);
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 313 */     StringBuilder localStringBuilder = new StringBuilder();
/* 314 */     localStringBuilder.append(this.algid.toString());
/* 315 */     localStringBuilder.append(paramsToString());
/* 316 */     return localStringBuilder.toString().hashCode();
/*     */   }
/*     */ 
/*     */   protected String paramsToString()
/*     */   {
/* 324 */     if (this.params == null)
/* 325 */       return "";
/* 326 */     if (this.algParams != null) {
/* 327 */       return this.algParams.toString();
/*     */     }
/* 329 */     return ", params unparsed";
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 337 */     return getName() + paramsToString();
/*     */   }
/*     */ 
/*     */   public static AlgorithmId parse(DerValue paramDerValue)
/*     */     throws IOException
/*     */   {
/* 354 */     if (paramDerValue.tag != 48) {
/* 355 */       throw new IOException("algid parse error, not a sequence");
/*     */     }
/*     */ 
/* 363 */     DerInputStream localDerInputStream = paramDerValue.toDerInputStream();
/*     */ 
/* 365 */     ObjectIdentifier localObjectIdentifier = localDerInputStream.getOID();
/*     */     DerValue localDerValue;
/* 366 */     if (localDerInputStream.available() == 0) {
/* 367 */       localDerValue = null;
/*     */     } else {
/* 369 */       localDerValue = localDerInputStream.getDerValue();
/* 370 */       if (localDerValue.tag == 5) {
/* 371 */         if (localDerValue.length() != 0) {
/* 372 */           throw new IOException("invalid NULL");
/*     */         }
/* 374 */         localDerValue = null;
/*     */       }
/* 376 */       if (localDerInputStream.available() != 0) {
/* 377 */         throw new IOException("Invalid AlgorithmIdentifier: extra data");
/*     */       }
/*     */     }
/*     */ 
/* 381 */     return new AlgorithmId(localObjectIdentifier, localDerValue);
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public static AlgorithmId getAlgorithmId(String paramString)
/*     */     throws NoSuchAlgorithmException
/*     */   {
/* 395 */     return get(paramString);
/*     */   }
/*     */ 
/*     */   public static AlgorithmId get(String paramString)
/*     */     throws NoSuchAlgorithmException
/*     */   {
/*     */     ObjectIdentifier localObjectIdentifier;
/*     */     try
/*     */     {
/* 409 */       localObjectIdentifier = algOID(paramString);
/*     */     } catch (IOException localIOException) {
/* 411 */       throw new NoSuchAlgorithmException("Invalid ObjectIdentifier " + paramString);
/*     */     }
/*     */ 
/* 415 */     if (localObjectIdentifier == null) {
/* 416 */       throw new NoSuchAlgorithmException("unrecognized algorithm name: " + paramString);
/*     */     }
/*     */ 
/* 419 */     return new AlgorithmId(localObjectIdentifier);
/*     */   }
/*     */ 
/*     */   public static AlgorithmId get(AlgorithmParameters paramAlgorithmParameters)
/*     */     throws NoSuchAlgorithmException
/*     */   {
/* 432 */     String str = paramAlgorithmParameters.getAlgorithm();
/*     */     ObjectIdentifier localObjectIdentifier;
/*     */     try
/*     */     {
/* 434 */       localObjectIdentifier = algOID(str);
/*     */     } catch (IOException localIOException) {
/* 436 */       throw new NoSuchAlgorithmException("Invalid ObjectIdentifier " + str);
/*     */     }
/*     */ 
/* 439 */     if (localObjectIdentifier == null) {
/* 440 */       throw new NoSuchAlgorithmException("unrecognized algorithm name: " + str);
/*     */     }
/*     */ 
/* 443 */     return new AlgorithmId(localObjectIdentifier, paramAlgorithmParameters);
/*     */   }
/*     */ 
/*     */   private static ObjectIdentifier algOID(String paramString)
/*     */     throws IOException
/*     */   {
/* 460 */     if (paramString.indexOf('.') != -1) {
/* 461 */       if (paramString.startsWith("OID.")) {
/* 462 */         return new ObjectIdentifier(paramString.substring("OID.".length()));
/*     */       }
/* 464 */       return new ObjectIdentifier(paramString);
/*     */     }
/*     */ 
/* 469 */     if (paramString.equalsIgnoreCase("MD5")) {
/* 470 */       return MD5_oid;
/*     */     }
/* 472 */     if (paramString.equalsIgnoreCase("MD2")) {
/* 473 */       return MD2_oid;
/*     */     }
/* 475 */     if ((paramString.equalsIgnoreCase("SHA")) || (paramString.equalsIgnoreCase("SHA1")) || (paramString.equalsIgnoreCase("SHA-1")))
/*     */     {
/* 477 */       return SHA_oid;
/*     */     }
/* 479 */     if ((paramString.equalsIgnoreCase("SHA-256")) || (paramString.equalsIgnoreCase("SHA256")))
/*     */     {
/* 481 */       return SHA256_oid;
/*     */     }
/* 483 */     if ((paramString.equalsIgnoreCase("SHA-384")) || (paramString.equalsIgnoreCase("SHA384")))
/*     */     {
/* 485 */       return SHA384_oid;
/*     */     }
/* 487 */     if ((paramString.equalsIgnoreCase("SHA-512")) || (paramString.equalsIgnoreCase("SHA512")))
/*     */     {
/* 489 */       return SHA512_oid;
/*     */     }
/*     */ 
/* 494 */     if (paramString.equalsIgnoreCase("RSA")) {
/* 495 */       return RSAEncryption_oid;
/*     */     }
/* 497 */     if ((paramString.equalsIgnoreCase("Diffie-Hellman")) || (paramString.equalsIgnoreCase("DH")))
/*     */     {
/* 499 */       return DH_oid;
/*     */     }
/* 501 */     if (paramString.equalsIgnoreCase("DSA")) {
/* 502 */       return DSA_oid;
/*     */     }
/* 504 */     if (paramString.equalsIgnoreCase("EC")) {
/* 505 */       return EC_oid;
/*     */     }
/*     */ 
/* 509 */     if ((paramString.equalsIgnoreCase("MD5withRSA")) || (paramString.equalsIgnoreCase("MD5/RSA")))
/*     */     {
/* 511 */       return md5WithRSAEncryption_oid;
/*     */     }
/* 513 */     if ((paramString.equalsIgnoreCase("MD2withRSA")) || (paramString.equalsIgnoreCase("MD2/RSA")))
/*     */     {
/* 515 */       return md2WithRSAEncryption_oid;
/*     */     }
/* 517 */     if ((paramString.equalsIgnoreCase("SHAwithDSA")) || (paramString.equalsIgnoreCase("SHA1withDSA")) || (paramString.equalsIgnoreCase("SHA/DSA")) || (paramString.equalsIgnoreCase("SHA1/DSA")) || (paramString.equalsIgnoreCase("DSAWithSHA1")) || (paramString.equalsIgnoreCase("DSS")) || (paramString.equalsIgnoreCase("SHA-1/DSA")))
/*     */     {
/* 524 */       return sha1WithDSA_oid;
/*     */     }
/* 526 */     if ((paramString.equalsIgnoreCase("SHA1WithRSA")) || (paramString.equalsIgnoreCase("SHA1/RSA")))
/*     */     {
/* 528 */       return sha1WithRSAEncryption_oid;
/*     */     }
/* 530 */     if ((paramString.equalsIgnoreCase("SHA1withECDSA")) || (paramString.equalsIgnoreCase("ECDSA")))
/*     */     {
/* 532 */       return sha1WithECDSA_oid;
/*     */     }
/* 534 */     if (paramString.equalsIgnoreCase("SHA224withECDSA")) {
/* 535 */       return sha224WithECDSA_oid;
/*     */     }
/* 537 */     if (paramString.equalsIgnoreCase("SHA256withECDSA")) {
/* 538 */       return sha256WithECDSA_oid;
/*     */     }
/* 540 */     if (paramString.equalsIgnoreCase("SHA384withECDSA")) {
/* 541 */       return sha384WithECDSA_oid;
/*     */     }
/* 543 */     if (paramString.equalsIgnoreCase("SHA512withECDSA")) {
/* 544 */       return sha512WithECDSA_oid;
/*     */     }
/*     */ 
/* 550 */     if (!initOidTable) {
/* 551 */       Provider[] arrayOfProvider = Security.getProviders();
/* 552 */       for (int i = 0; i < arrayOfProvider.length; i++) {
/* 553 */         Enumeration localEnumeration = arrayOfProvider[i].keys();
/* 554 */         while (localEnumeration.hasMoreElements()) {
/* 555 */           String str2 = (String)localEnumeration.nextElement();
/* 556 */           String str3 = str2.toUpperCase(Locale.ENGLISH);
/*     */           int j;
/* 558 */           if ((str3.startsWith("ALG.ALIAS")) && ((j = str3.indexOf("OID.", 0)) != -1))
/*     */           {
/* 560 */             j += "OID.".length();
/* 561 */             if (j == str2.length())
/*     */             {
/*     */               break;
/*     */             }
/* 565 */             if (oidTable == null) {
/* 566 */               oidTable = new HashMap();
/*     */             }
/* 568 */             String str1 = str2.substring(j);
/* 569 */             String str4 = arrayOfProvider[i].getProperty(str2);
/* 570 */             if (str4 != null) {
/* 571 */               str4 = str4.toUpperCase(Locale.ENGLISH);
/*     */             }
/* 573 */             if ((str4 != null) && (oidTable.get(str4) == null))
/*     */             {
/* 575 */               oidTable.put(str4, new ObjectIdentifier(str1));
/*     */             }
/*     */           }
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 582 */       if (oidTable == null) {
/* 583 */         oidTable = new HashMap(1);
/*     */       }
/* 585 */       initOidTable = true;
/*     */     }
/*     */ 
/* 588 */     return (ObjectIdentifier)oidTable.get(paramString.toUpperCase(Locale.ENGLISH));
/*     */   }
/*     */ 
/*     */   private static ObjectIdentifier oid(int[] paramArrayOfInt) {
/* 592 */     return ObjectIdentifier.newInternal(paramArrayOfInt);
/*     */   }
/*     */ 
/*     */   public static String makeSigAlg(String paramString1, String paramString2)
/*     */   {
/* 900 */     paramString1 = paramString1.replace("-", "").toUpperCase(Locale.ENGLISH);
/* 901 */     if (paramString1.equalsIgnoreCase("SHA")) paramString1 = "SHA1";
/*     */ 
/* 903 */     paramString2 = paramString2.toUpperCase(Locale.ENGLISH);
/* 904 */     if (paramString2.equals("EC")) paramString2 = "ECDSA";
/*     */ 
/* 906 */     return paramString1 + "with" + paramString2;
/*     */   }
/*     */ 
/*     */   public static String getEncAlgFromSigAlg(String paramString)
/*     */   {
/* 914 */     paramString = paramString.toUpperCase(Locale.ENGLISH);
/* 915 */     int i = paramString.indexOf("WITH");
/* 916 */     String str = null;
/* 917 */     if (i > 0) {
/* 918 */       int j = paramString.indexOf("AND", i + 4);
/* 919 */       if (j > 0)
/* 920 */         str = paramString.substring(i + 4, j);
/*     */       else {
/* 922 */         str = paramString.substring(i + 4);
/*     */       }
/* 924 */       if (str.equalsIgnoreCase("ECDSA")) {
/* 925 */         str = "EC";
/*     */       }
/*     */     }
/* 928 */     return str;
/*     */   }
/*     */ 
/*     */   public static String getDigAlgFromSigAlg(String paramString)
/*     */   {
/* 936 */     paramString = paramString.toUpperCase(Locale.ENGLISH);
/* 937 */     int i = paramString.indexOf("WITH");
/* 938 */     if (i > 0) {
/* 939 */       return paramString.substring(0, i);
/*     */     }
/* 941 */     return null;
/*     */   }
/*     */ 
/*     */   public static String getStandardDigestName(String paramString)
/*     */   {
/* 946 */     if (paramString.equals("SHA"))
/* 947 */       return "SHA-1";
/* 948 */     if (paramString.equals("SHA224"))
/* 949 */       return "SHA-224";
/* 950 */     if (paramString.equals("SHA256"))
/* 951 */       return "SHA-256";
/* 952 */     if (paramString.equals("SHA384"))
/* 953 */       return "SHA-384";
/* 954 */     if (paramString.equals("SHA512")) {
/* 955 */       return "SHA-512";
/*     */     }
/* 957 */     return paramString;
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/* 736 */     DH_oid = ObjectIdentifier.newInternal(DH_data);
/*     */ 
/* 743 */     DH_PKIX_oid = ObjectIdentifier.newInternal(DH_PKIX_data);
/*     */ 
/* 753 */     DSA_OIW_oid = ObjectIdentifier.newInternal(DSA_OIW_data);
/*     */ 
/* 762 */     DSA_oid = ObjectIdentifier.newInternal(DSA_PKIX_data);
/*     */ 
/* 770 */     RSA_oid = ObjectIdentifier.newInternal(RSA_data);
/*     */ 
/* 777 */     RSAEncryption_oid = ObjectIdentifier.newInternal(RSAEncryption_data);
/*     */ 
/* 785 */     md2WithRSAEncryption_oid = ObjectIdentifier.newInternal(md2WithRSAEncryption_data);
/*     */ 
/* 793 */     md5WithRSAEncryption_oid = ObjectIdentifier.newInternal(md5WithRSAEncryption_data);
/*     */ 
/* 801 */     sha1WithRSAEncryption_oid = ObjectIdentifier.newInternal(sha1WithRSAEncryption_data);
/*     */ 
/* 809 */     sha1WithRSAEncryption_OIW_oid = ObjectIdentifier.newInternal(sha1WithRSAEncryption_OIW_data);
/*     */ 
/* 817 */     sha256WithRSAEncryption_oid = ObjectIdentifier.newInternal(sha256WithRSAEncryption_data);
/*     */ 
/* 825 */     sha384WithRSAEncryption_oid = ObjectIdentifier.newInternal(sha384WithRSAEncryption_data);
/*     */ 
/* 833 */     sha512WithRSAEncryption_oid = ObjectIdentifier.newInternal(sha512WithRSAEncryption_data);
/*     */ 
/* 842 */     shaWithDSA_OIW_oid = ObjectIdentifier.newInternal(shaWithDSA_OIW_data);
/*     */ 
/* 849 */     sha1WithDSA_OIW_oid = ObjectIdentifier.newInternal(sha1WithDSA_OIW_data);
/*     */ 
/* 856 */     sha1WithDSA_oid = ObjectIdentifier.newInternal(dsaWithSHA1_PKIX_data);
/*     */ 
/* 858 */     nameTable = new HashMap();
/* 859 */     nameTable.put(MD5_oid, "MD5");
/* 860 */     nameTable.put(MD2_oid, "MD2");
/* 861 */     nameTable.put(SHA_oid, "SHA");
/* 862 */     nameTable.put(SHA256_oid, "SHA256");
/* 863 */     nameTable.put(SHA384_oid, "SHA384");
/* 864 */     nameTable.put(SHA512_oid, "SHA512");
/* 865 */     nameTable.put(RSAEncryption_oid, "RSA");
/* 866 */     nameTable.put(RSA_oid, "RSA");
/* 867 */     nameTable.put(DH_oid, "Diffie-Hellman");
/* 868 */     nameTable.put(DH_PKIX_oid, "Diffie-Hellman");
/* 869 */     nameTable.put(DSA_oid, "DSA");
/* 870 */     nameTable.put(DSA_OIW_oid, "DSA");
/* 871 */     nameTable.put(EC_oid, "EC");
/* 872 */     nameTable.put(sha1WithECDSA_oid, "SHA1withECDSA");
/* 873 */     nameTable.put(sha224WithECDSA_oid, "SHA224withECDSA");
/* 874 */     nameTable.put(sha256WithECDSA_oid, "SHA256withECDSA");
/* 875 */     nameTable.put(sha384WithECDSA_oid, "SHA384withECDSA");
/* 876 */     nameTable.put(sha512WithECDSA_oid, "SHA512withECDSA");
/* 877 */     nameTable.put(md5WithRSAEncryption_oid, "MD5withRSA");
/* 878 */     nameTable.put(md2WithRSAEncryption_oid, "MD2withRSA");
/* 879 */     nameTable.put(sha1WithDSA_oid, "SHA1withDSA");
/* 880 */     nameTable.put(sha1WithDSA_OIW_oid, "SHA1withDSA");
/* 881 */     nameTable.put(shaWithDSA_OIW_oid, "SHA1withDSA");
/* 882 */     nameTable.put(sha1WithRSAEncryption_oid, "SHA1withRSA");
/* 883 */     nameTable.put(sha1WithRSAEncryption_OIW_oid, "SHA1withRSA");
/* 884 */     nameTable.put(sha256WithRSAEncryption_oid, "SHA256withRSA");
/* 885 */     nameTable.put(sha384WithRSAEncryption_oid, "SHA384withRSA");
/* 886 */     nameTable.put(sha512WithRSAEncryption_oid, "SHA512withRSA");
/* 887 */     nameTable.put(pbeWithMD5AndDES_oid, "PBEWithMD5AndDES");
/* 888 */     nameTable.put(pbeWithMD5AndRC2_oid, "PBEWithMD5AndRC2");
/* 889 */     nameTable.put(pbeWithSHA1AndDES_oid, "PBEWithSHA1AndDES");
/* 890 */     nameTable.put(pbeWithSHA1AndRC2_oid, "PBEWithSHA1AndRC2");
/* 891 */     nameTable.put(pbeWithSHA1AndDESede_oid, "PBEWithSHA1AndDESede");
/* 892 */     nameTable.put(pbeWithSHA1AndRC2_40_oid, "PBEWithSHA1AndRC2_40");
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.x509.AlgorithmId
 * JD-Core Version:    0.6.2
 */