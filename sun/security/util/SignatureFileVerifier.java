/*     */ package sun.security.util;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.IOException;
/*     */ import java.security.CodeSigner;
/*     */ import java.security.MessageDigest;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.SignatureException;
/*     */ import java.security.Timestamp;
/*     */ import java.security.cert.CertPath;
/*     */ import java.security.cert.CertificateException;
/*     */ import java.security.cert.CertificateFactory;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import java.util.jar.Attributes;
/*     */ import java.util.jar.Attributes.Name;
/*     */ import java.util.jar.JarException;
/*     */ import java.util.jar.Manifest;
/*     */ import sun.misc.BASE64Decoder;
/*     */ import sun.security.jca.Providers;
/*     */ import sun.security.pkcs.ContentInfo;
/*     */ import sun.security.pkcs.PKCS7;
/*     */ import sun.security.pkcs.PKCS9Attribute;
/*     */ import sun.security.pkcs.PKCS9Attributes;
/*     */ import sun.security.pkcs.SignerInfo;
/*     */ import sun.security.timestamp.TimestampToken;
/*     */ import sun.security.x509.AlgorithmId;
/*     */ 
/*     */ public class SignatureFileVerifier
/*     */ {
/*  47 */   private static final Debug debug = Debug.getInstance("jar");
/*     */   private ArrayList<CodeSigner[]> signerCache;
/*  52 */   private static final String ATTR_DIGEST = "-DIGEST-Manifest-Main-Attributes".toUpperCase(Locale.ENGLISH);
/*     */   private PKCS7 block;
/*     */   private byte[] sfBytes;
/*     */   private String name;
/*     */   private ManifestDigester md;
/*     */   private HashMap<String, MessageDigest> createdDigests;
/*  74 */   private boolean workaround = false;
/*     */ 
/*  77 */   private CertificateFactory certificateFactory = null;
/*     */ 
/* 635 */   private static final char[] hexc = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
/*     */ 
/*     */   public SignatureFileVerifier(ArrayList<CodeSigner[]> paramArrayList, ManifestDigester paramManifestDigester, String paramString, byte[] paramArrayOfByte)
/*     */     throws IOException, CertificateException
/*     */   {
/*  94 */     Object localObject1 = null;
/*     */     try {
/*  96 */       localObject1 = Providers.startJarVerification();
/*  97 */       this.block = new PKCS7(paramArrayOfByte);
/*  98 */       this.sfBytes = this.block.getContentInfo().getData();
/*  99 */       this.certificateFactory = CertificateFactory.getInstance("X509");
/*     */     } finally {
/* 101 */       Providers.stopJarVerification(localObject1);
/*     */     }
/* 103 */     this.name = paramString.substring(0, paramString.lastIndexOf(".")).toUpperCase(Locale.ENGLISH);
/*     */ 
/* 105 */     this.md = paramManifestDigester;
/* 106 */     this.signerCache = paramArrayList;
/*     */   }
/*     */ 
/*     */   public boolean needSignatureFileBytes()
/*     */   {
/* 115 */     return this.sfBytes == null;
/*     */   }
/*     */ 
/*     */   public boolean needSignatureFile(String paramString)
/*     */   {
/* 127 */     return this.name.equalsIgnoreCase(paramString);
/*     */   }
/*     */ 
/*     */   public void setSignatureFile(byte[] paramArrayOfByte)
/*     */   {
/* 136 */     this.sfBytes = paramArrayOfByte;
/*     */   }
/*     */ 
/*     */   public static boolean isBlockOrSF(String paramString)
/*     */   {
/* 150 */     if ((paramString.endsWith(".SF")) || (paramString.endsWith(".DSA")) || (paramString.endsWith(".RSA")) || (paramString.endsWith(".EC")))
/*     */     {
/* 152 */       return true;
/*     */     }
/* 154 */     return false;
/*     */   }
/*     */ 
/*     */   public static boolean isSigningRelated(String paramString)
/*     */   {
/* 168 */     paramString = paramString.toUpperCase(Locale.ENGLISH);
/* 169 */     if (!paramString.startsWith("META-INF/")) {
/* 170 */       return false;
/*     */     }
/* 172 */     paramString = paramString.substring(9);
/* 173 */     if (paramString.indexOf('/') != -1) {
/* 174 */       return false;
/*     */     }
/* 176 */     if ((isBlockOrSF(paramString)) || (paramString.equals("MANIFEST.MF")))
/* 177 */       return true;
/* 178 */     if (paramString.startsWith("SIG-"))
/*     */     {
/* 182 */       int i = paramString.lastIndexOf('.');
/* 183 */       if (i != -1) {
/* 184 */         String str = paramString.substring(i + 1);
/*     */ 
/* 186 */         if ((str.length() > 3) || (str.length() < 1)) {
/* 187 */           return false;
/*     */         }
/*     */ 
/* 190 */         for (int j = 0; j < str.length(); j++) {
/* 191 */           int k = str.charAt(j);
/*     */ 
/* 193 */           if (((k < 65) || (k > 90)) && ((k < 48) || (k > 57))) {
/* 194 */             return false;
/*     */           }
/*     */         }
/*     */       }
/* 198 */       return true;
/*     */     }
/* 200 */     return false;
/*     */   }
/*     */ 
/*     */   private MessageDigest getDigest(String paramString)
/*     */   {
/* 207 */     if (this.createdDigests == null) {
/* 208 */       this.createdDigests = new HashMap();
/*     */     }
/* 210 */     MessageDigest localMessageDigest = (MessageDigest)this.createdDigests.get(paramString);
/*     */ 
/* 212 */     if (localMessageDigest == null)
/*     */       try {
/* 214 */         localMessageDigest = MessageDigest.getInstance(paramString);
/* 215 */         this.createdDigests.put(paramString, localMessageDigest);
/*     */       }
/*     */       catch (NoSuchAlgorithmException localNoSuchAlgorithmException)
/*     */       {
/*     */       }
/* 220 */     return localMessageDigest;
/*     */   }
/*     */ 
/*     */   public void process(Hashtable<String, CodeSigner[]> paramHashtable, List paramList)
/*     */     throws IOException, SignatureException, NoSuchAlgorithmException, JarException, CertificateException
/*     */   {
/* 237 */     Object localObject1 = null;
/*     */     try {
/* 239 */       localObject1 = Providers.startJarVerification();
/* 240 */       processImpl(paramHashtable, paramList);
/*     */     } finally {
/* 242 */       Providers.stopJarVerification(localObject1);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void processImpl(Hashtable<String, CodeSigner[]> paramHashtable, List paramList)
/*     */     throws IOException, SignatureException, NoSuchAlgorithmException, JarException, CertificateException
/*     */   {
/* 252 */     Manifest localManifest = new Manifest();
/* 253 */     localManifest.read(new ByteArrayInputStream(this.sfBytes));
/*     */ 
/* 255 */     String str1 = localManifest.getMainAttributes().getValue(Attributes.Name.SIGNATURE_VERSION);
/*     */ 
/* 258 */     if ((str1 == null) || (!str1.equalsIgnoreCase("1.0")))
/*     */     {
/* 261 */       return;
/*     */     }
/*     */ 
/* 264 */     SignerInfo[] arrayOfSignerInfo = this.block.verify(this.sfBytes);
/*     */ 
/* 266 */     if (arrayOfSignerInfo == null) {
/* 267 */       throw new SecurityException("cannot verify signature block file " + this.name);
/*     */     }
/*     */ 
/* 271 */     BASE64Decoder localBASE64Decoder = new BASE64Decoder();
/*     */ 
/* 273 */     CodeSigner[] arrayOfCodeSigner = getSigners(arrayOfSignerInfo, this.block);
/*     */ 
/* 276 */     if (arrayOfCodeSigner == null) {
/* 277 */       return;
/*     */     }
/* 279 */     Iterator localIterator = localManifest.getEntries().entrySet().iterator();
/*     */ 
/* 283 */     boolean bool = verifyManifestHash(localManifest, this.md, localBASE64Decoder, paramList);
/*     */ 
/* 286 */     if ((!bool) && (!verifyManifestMainAttrs(localManifest, this.md, localBASE64Decoder))) {
/* 287 */       throw new SecurityException("Invalid signature file digest for Manifest main attributes");
/*     */     }
/*     */ 
/* 292 */     while (localIterator.hasNext())
/*     */     {
/* 294 */       Map.Entry localEntry = (Map.Entry)localIterator.next();
/* 295 */       String str2 = (String)localEntry.getKey();
/*     */ 
/* 297 */       if ((bool) || (verifySection((Attributes)localEntry.getValue(), str2, this.md, localBASE64Decoder)))
/*     */       {
/* 300 */         if (str2.startsWith("./")) {
/* 301 */           str2 = str2.substring(2);
/*     */         }
/* 303 */         if (str2.startsWith("/")) {
/* 304 */           str2 = str2.substring(1);
/*     */         }
/* 306 */         updateSigners(arrayOfCodeSigner, paramHashtable, str2);
/*     */ 
/* 308 */         if (debug != null) {
/* 309 */           debug.println("processSignature signed name = " + str2);
/*     */         }
/*     */       }
/* 312 */       else if (debug != null) {
/* 313 */         debug.println("processSignature unsigned name = " + str2);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 318 */     updateSigners(arrayOfCodeSigner, paramHashtable, "META-INF/MANIFEST.MF");
/*     */   }
/*     */ 
/*     */   private boolean verifyManifestHash(Manifest paramManifest, ManifestDigester paramManifestDigester, BASE64Decoder paramBASE64Decoder, List paramList)
/*     */     throws IOException
/*     */   {
/* 330 */     Attributes localAttributes = paramManifest.getMainAttributes();
/* 331 */     boolean bool = false;
/*     */ 
/* 334 */     for (Map.Entry localEntry : localAttributes.entrySet())
/*     */     {
/* 336 */       String str1 = localEntry.getKey().toString();
/*     */ 
/* 338 */       if (str1.toUpperCase(Locale.ENGLISH).endsWith("-DIGEST-MANIFEST"))
/*     */       {
/* 340 */         String str2 = str1.substring(0, str1.length() - 16);
/*     */ 
/* 342 */         paramList.add(str1);
/* 343 */         paramList.add(localEntry.getValue());
/* 344 */         MessageDigest localMessageDigest = getDigest(str2);
/* 345 */         if (localMessageDigest != null) {
/* 346 */           byte[] arrayOfByte1 = paramManifestDigester.manifestDigest(localMessageDigest);
/* 347 */           byte[] arrayOfByte2 = paramBASE64Decoder.decodeBuffer((String)localEntry.getValue());
/*     */ 
/* 350 */           if (debug != null) {
/* 351 */             debug.println("Signature File: Manifest digest " + localMessageDigest.getAlgorithm());
/*     */ 
/* 353 */             debug.println("  sigfile  " + toHex(arrayOfByte2));
/* 354 */             debug.println("  computed " + toHex(arrayOfByte1));
/* 355 */             debug.println();
/*     */           }
/*     */ 
/* 358 */           if (MessageDigest.isEqual(arrayOfByte1, arrayOfByte2))
/*     */           {
/* 360 */             bool = true;
/*     */           }
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 367 */     return bool;
/*     */   }
/*     */ 
/*     */   private boolean verifyManifestMainAttrs(Manifest paramManifest, ManifestDigester paramManifestDigester, BASE64Decoder paramBASE64Decoder)
/*     */     throws IOException
/*     */   {
/* 375 */     Attributes localAttributes = paramManifest.getMainAttributes();
/* 376 */     boolean bool = true;
/*     */ 
/* 380 */     for (Map.Entry localEntry : localAttributes.entrySet()) {
/* 381 */       String str1 = localEntry.getKey().toString();
/*     */ 
/* 383 */       if (str1.toUpperCase(Locale.ENGLISH).endsWith(ATTR_DIGEST)) {
/* 384 */         String str2 = str1.substring(0, str1.length() - ATTR_DIGEST.length());
/*     */ 
/* 387 */         MessageDigest localMessageDigest = getDigest(str2);
/* 388 */         if (localMessageDigest != null) {
/* 389 */           ManifestDigester.Entry localEntry1 = paramManifestDigester.get("Manifest-Main-Attributes", false);
/*     */ 
/* 391 */           byte[] arrayOfByte1 = localEntry1.digest(localMessageDigest);
/* 392 */           byte[] arrayOfByte2 = paramBASE64Decoder.decodeBuffer((String)localEntry.getValue());
/*     */ 
/* 395 */           if (debug != null) {
/* 396 */             debug.println("Signature File: Manifest Main Attributes digest " + localMessageDigest.getAlgorithm());
/*     */ 
/* 399 */             debug.println("  sigfile  " + toHex(arrayOfByte2));
/* 400 */             debug.println("  computed " + toHex(arrayOfByte1));
/* 401 */             debug.println();
/*     */           }
/*     */ 
/* 404 */           if (!MessageDigest.isEqual(arrayOfByte1, arrayOfByte2))
/*     */           {
/* 409 */             bool = false;
/* 410 */             if (debug == null) break;
/* 411 */             debug.println("Verification of Manifest main attributes failed");
/*     */ 
/* 413 */             debug.println(); break;
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 424 */     return bool;
/*     */   }
/*     */ 
/*     */   private boolean verifySection(Attributes paramAttributes, String paramString, ManifestDigester paramManifestDigester, BASE64Decoder paramBASE64Decoder)
/*     */     throws IOException
/*     */   {
/* 442 */     boolean bool = false;
/* 443 */     ManifestDigester.Entry localEntry = paramManifestDigester.get(paramString, this.block.isOldStyle());
/*     */ 
/* 445 */     if (localEntry == null) {
/* 446 */       throw new SecurityException("no manifiest section for signature file entry " + paramString);
/*     */     }
/*     */ 
/* 450 */     if (paramAttributes != null)
/*     */     {
/* 456 */       for (Map.Entry localEntry1 : paramAttributes.entrySet()) {
/* 457 */         String str1 = localEntry1.getKey().toString();
/*     */ 
/* 459 */         if (str1.toUpperCase(Locale.ENGLISH).endsWith("-DIGEST"))
/*     */         {
/* 461 */           String str2 = str1.substring(0, str1.length() - 7);
/*     */ 
/* 463 */           MessageDigest localMessageDigest = getDigest(str2);
/*     */ 
/* 465 */           if (localMessageDigest != null) {
/* 466 */             int i = 0;
/*     */ 
/* 468 */             byte[] arrayOfByte1 = paramBASE64Decoder.decodeBuffer((String)localEntry1.getValue());
/*     */             byte[] arrayOfByte2;
/* 471 */             if (this.workaround)
/* 472 */               arrayOfByte2 = localEntry.digestWorkaround(localMessageDigest);
/*     */             else {
/* 474 */               arrayOfByte2 = localEntry.digest(localMessageDigest);
/*     */             }
/*     */ 
/* 477 */             if (debug != null) {
/* 478 */               debug.println("Signature Block File: " + paramString + " digest=" + localMessageDigest.getAlgorithm());
/*     */ 
/* 480 */               debug.println("  expected " + toHex(arrayOfByte1));
/* 481 */               debug.println("  computed " + toHex(arrayOfByte2));
/* 482 */               debug.println();
/*     */             }
/*     */ 
/* 485 */             if (MessageDigest.isEqual(arrayOfByte2, arrayOfByte1)) {
/* 486 */               bool = true;
/* 487 */               i = 1;
/*     */             }
/* 490 */             else if (!this.workaround) {
/* 491 */               arrayOfByte2 = localEntry.digestWorkaround(localMessageDigest);
/* 492 */               if (MessageDigest.isEqual(arrayOfByte2, arrayOfByte1)) {
/* 493 */                 if (debug != null) {
/* 494 */                   debug.println("  re-computed " + toHex(arrayOfByte2));
/* 495 */                   debug.println();
/*     */                 }
/* 497 */                 this.workaround = true;
/* 498 */                 bool = true;
/* 499 */                 i = 1;
/*     */               }
/*     */             }
/*     */ 
/* 503 */             if (i == 0) {
/* 504 */               throw new SecurityException("invalid " + localMessageDigest.getAlgorithm() + " signature file digest for " + paramString);
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 512 */     return bool;
/*     */   }
/*     */ 
/*     */   private CodeSigner[] getSigners(SignerInfo[] paramArrayOfSignerInfo, PKCS7 paramPKCS7)
/*     */     throws IOException, NoSuchAlgorithmException, SignatureException, CertificateException
/*     */   {
/* 524 */     ArrayList localArrayList1 = null;
/*     */ 
/* 526 */     for (int i = 0; i < paramArrayOfSignerInfo.length; i++)
/*     */     {
/* 528 */       SignerInfo localSignerInfo = paramArrayOfSignerInfo[i];
/* 529 */       ArrayList localArrayList2 = localSignerInfo.getCertificateChain(paramPKCS7);
/* 530 */       CertPath localCertPath = this.certificateFactory.generateCertPath(localArrayList2);
/* 531 */       if (localArrayList1 == null) {
/* 532 */         localArrayList1 = new ArrayList();
/*     */       }
/*     */ 
/* 535 */       localArrayList1.add(new CodeSigner(localCertPath, getTimestamp(localSignerInfo)));
/*     */ 
/* 537 */       if (debug != null) {
/* 538 */         debug.println("Signature Block Certificate: " + localArrayList2.get(0));
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 543 */     if (localArrayList1 != null) {
/* 544 */       return (CodeSigner[])localArrayList1.toArray(new CodeSigner[localArrayList1.size()]);
/*     */     }
/* 546 */     return null;
/*     */   }
/*     */ 
/*     */   private Timestamp getTimestamp(SignerInfo paramSignerInfo)
/*     */     throws IOException, NoSuchAlgorithmException, SignatureException, CertificateException
/*     */   {
/* 574 */     Timestamp localTimestamp = null;
/*     */ 
/* 577 */     PKCS9Attributes localPKCS9Attributes = paramSignerInfo.getUnauthenticatedAttributes();
/* 578 */     if (localPKCS9Attributes != null) {
/* 579 */       PKCS9Attribute localPKCS9Attribute = localPKCS9Attributes.getAttribute("signatureTimestampToken");
/*     */ 
/* 581 */       if (localPKCS9Attribute != null) {
/* 582 */         PKCS7 localPKCS7 = new PKCS7((byte[])localPKCS9Attribute.getValue());
/*     */ 
/* 585 */         byte[] arrayOfByte = localPKCS7.getContentInfo().getData();
/*     */ 
/* 589 */         SignerInfo[] arrayOfSignerInfo = localPKCS7.verify(arrayOfByte);
/*     */ 
/* 592 */         ArrayList localArrayList = arrayOfSignerInfo[0].getCertificateChain(localPKCS7);
/*     */ 
/* 594 */         CertPath localCertPath = this.certificateFactory.generateCertPath(localArrayList);
/*     */ 
/* 596 */         TimestampToken localTimestampToken = new TimestampToken(arrayOfByte);
/*     */ 
/* 599 */         verifyTimestamp(localTimestampToken, paramSignerInfo.getEncryptedDigest());
/*     */ 
/* 601 */         localTimestamp = new Timestamp(localTimestampToken.getDate(), localCertPath);
/*     */       }
/*     */     }
/*     */ 
/* 605 */     return localTimestamp;
/*     */   }
/*     */ 
/*     */   private void verifyTimestamp(TimestampToken paramTimestampToken, byte[] paramArrayOfByte)
/*     */     throws NoSuchAlgorithmException, SignatureException
/*     */   {
/* 616 */     MessageDigest localMessageDigest = MessageDigest.getInstance(AlgorithmId.getStandardDigestName(paramTimestampToken.getHashAlgorithm().getName()));
/*     */ 
/* 620 */     if (!Arrays.equals(paramTimestampToken.getHashedMessage(), localMessageDigest.digest(paramArrayOfByte))) {
/* 621 */       throw new SignatureException("Signature timestamp (#" + paramTimestampToken.getSerialNumber() + ") generated on " + paramTimestampToken.getDate() + " is inapplicable");
/*     */     }
/*     */ 
/* 626 */     if (debug != null) {
/* 627 */       debug.println();
/* 628 */       debug.println("Detected signature timestamp (#" + paramTimestampToken.getSerialNumber() + ") generated on " + paramTimestampToken.getDate());
/*     */ 
/* 630 */       debug.println();
/*     */     }
/*     */   }
/*     */ 
/*     */   static String toHex(byte[] paramArrayOfByte)
/*     */   {
/* 645 */     StringBuffer localStringBuffer = new StringBuffer(paramArrayOfByte.length * 2);
/*     */ 
/* 647 */     for (int i = 0; i < paramArrayOfByte.length; i++) {
/* 648 */       localStringBuffer.append(hexc[(paramArrayOfByte[i] >> 4 & 0xF)]);
/* 649 */       localStringBuffer.append(hexc[(paramArrayOfByte[i] & 0xF)]);
/*     */     }
/* 651 */     return localStringBuffer.toString();
/*     */   }
/*     */ 
/*     */   static boolean contains(CodeSigner[] paramArrayOfCodeSigner, CodeSigner paramCodeSigner)
/*     */   {
/* 657 */     for (int i = 0; i < paramArrayOfCodeSigner.length; i++) {
/* 658 */       if (paramArrayOfCodeSigner[i].equals(paramCodeSigner))
/* 659 */         return true;
/*     */     }
/* 661 */     return false;
/*     */   }
/*     */ 
/*     */   static boolean isSubSet(CodeSigner[] paramArrayOfCodeSigner1, CodeSigner[] paramArrayOfCodeSigner2)
/*     */   {
/* 668 */     if (paramArrayOfCodeSigner2 == paramArrayOfCodeSigner1) {
/* 669 */       return true;
/*     */     }
/*     */ 
/* 672 */     for (int i = 0; i < paramArrayOfCodeSigner1.length; i++) {
/* 673 */       if (!contains(paramArrayOfCodeSigner2, paramArrayOfCodeSigner1[i]))
/* 674 */         return false;
/*     */     }
/* 676 */     return true;
/*     */   }
/*     */ 
/*     */   static boolean matches(CodeSigner[] paramArrayOfCodeSigner1, CodeSigner[] paramArrayOfCodeSigner2, CodeSigner[] paramArrayOfCodeSigner3)
/*     */   {
/* 688 */     if ((paramArrayOfCodeSigner2 == null) && (paramArrayOfCodeSigner1 == paramArrayOfCodeSigner3)) {
/* 689 */       return true;
/*     */     }
/*     */ 
/* 694 */     if ((paramArrayOfCodeSigner2 != null) && (!isSubSet(paramArrayOfCodeSigner2, paramArrayOfCodeSigner1))) {
/* 695 */       return false;
/*     */     }
/*     */ 
/* 698 */     if (!isSubSet(paramArrayOfCodeSigner3, paramArrayOfCodeSigner1)) {
/* 699 */       return false;
/*     */     }
/*     */ 
/* 705 */     for (int i = 0; i < paramArrayOfCodeSigner1.length; i++) {
/* 706 */       int j = ((paramArrayOfCodeSigner2 != null) && (contains(paramArrayOfCodeSigner2, paramArrayOfCodeSigner1[i]))) || (contains(paramArrayOfCodeSigner3, paramArrayOfCodeSigner1[i])) ? 1 : 0;
/*     */ 
/* 709 */       if (j == 0)
/* 710 */         return false;
/*     */     }
/* 712 */     return true;
/*     */   }
/*     */ 
/*     */   void updateSigners(CodeSigner[] paramArrayOfCodeSigner, Hashtable<String, CodeSigner[]> paramHashtable, String paramString)
/*     */   {
/* 718 */     CodeSigner[] arrayOfCodeSigner1 = (CodeSigner[])paramHashtable.get(paramString);
/*     */     CodeSigner[] arrayOfCodeSigner2;
/* 725 */     for (int i = this.signerCache.size() - 1; i != -1; i--) {
/* 726 */       arrayOfCodeSigner2 = (CodeSigner[])this.signerCache.get(i);
/* 727 */       if (matches(arrayOfCodeSigner2, arrayOfCodeSigner1, paramArrayOfCodeSigner)) {
/* 728 */         paramHashtable.put(paramString, arrayOfCodeSigner2);
/* 729 */         return;
/*     */       }
/*     */     }
/*     */ 
/* 733 */     if (arrayOfCodeSigner1 == null) {
/* 734 */       arrayOfCodeSigner2 = paramArrayOfCodeSigner;
/*     */     } else {
/* 736 */       arrayOfCodeSigner2 = new CodeSigner[arrayOfCodeSigner1.length + paramArrayOfCodeSigner.length];
/*     */ 
/* 738 */       System.arraycopy(arrayOfCodeSigner1, 0, arrayOfCodeSigner2, 0, arrayOfCodeSigner1.length);
/*     */ 
/* 740 */       System.arraycopy(paramArrayOfCodeSigner, 0, arrayOfCodeSigner2, arrayOfCodeSigner1.length, paramArrayOfCodeSigner.length);
/*     */     }
/*     */ 
/* 743 */     this.signerCache.add(arrayOfCodeSigner2);
/* 744 */     paramHashtable.put(paramString, arrayOfCodeSigner2);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.util.SignatureFileVerifier
 * JD-Core Version:    0.6.2
 */