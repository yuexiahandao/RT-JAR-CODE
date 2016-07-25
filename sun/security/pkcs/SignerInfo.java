/*     */ package sun.security.pkcs;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.math.BigInteger;
/*     */ import java.security.InvalidKeyException;
/*     */ import java.security.MessageDigest;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.Principal;
/*     */ import java.security.PublicKey;
/*     */ import java.security.Signature;
/*     */ import java.security.SignatureException;
/*     */ import java.security.cert.X509Certificate;
/*     */ import java.util.ArrayList;
/*     */ import sun.misc.HexDumpEncoder;
/*     */ import sun.security.util.Debug;
/*     */ import sun.security.util.DerEncoder;
/*     */ import sun.security.util.DerInputStream;
/*     */ import sun.security.util.DerOutputStream;
/*     */ import sun.security.util.DerValue;
/*     */ import sun.security.util.ObjectIdentifier;
/*     */ import sun.security.x509.AlgorithmId;
/*     */ import sun.security.x509.KeyUsageExtension;
/*     */ import sun.security.x509.X500Name;
/*     */ 
/*     */ public class SignerInfo
/*     */   implements DerEncoder
/*     */ {
/*     */   BigInteger version;
/*     */   X500Name issuerName;
/*     */   BigInteger certificateSerialNumber;
/*     */   AlgorithmId digestAlgorithmId;
/*     */   AlgorithmId digestEncryptionAlgorithmId;
/*     */   byte[] encryptedDigest;
/*     */   PKCS9Attributes authenticatedAttributes;
/*     */   PKCS9Attributes unauthenticatedAttributes;
/*     */ 
/*     */   public SignerInfo(X500Name paramX500Name, BigInteger paramBigInteger, AlgorithmId paramAlgorithmId1, AlgorithmId paramAlgorithmId2, byte[] paramArrayOfByte)
/*     */   {
/*  64 */     this.version = BigInteger.ONE;
/*  65 */     this.issuerName = paramX500Name;
/*  66 */     this.certificateSerialNumber = paramBigInteger;
/*  67 */     this.digestAlgorithmId = paramAlgorithmId1;
/*  68 */     this.digestEncryptionAlgorithmId = paramAlgorithmId2;
/*  69 */     this.encryptedDigest = paramArrayOfByte;
/*     */   }
/*     */ 
/*     */   public SignerInfo(X500Name paramX500Name, BigInteger paramBigInteger, AlgorithmId paramAlgorithmId1, PKCS9Attributes paramPKCS9Attributes1, AlgorithmId paramAlgorithmId2, byte[] paramArrayOfByte, PKCS9Attributes paramPKCS9Attributes2)
/*     */   {
/*  79 */     this.version = BigInteger.ONE;
/*  80 */     this.issuerName = paramX500Name;
/*  81 */     this.certificateSerialNumber = paramBigInteger;
/*  82 */     this.digestAlgorithmId = paramAlgorithmId1;
/*  83 */     this.authenticatedAttributes = paramPKCS9Attributes1;
/*  84 */     this.digestEncryptionAlgorithmId = paramAlgorithmId2;
/*  85 */     this.encryptedDigest = paramArrayOfByte;
/*  86 */     this.unauthenticatedAttributes = paramPKCS9Attributes2;
/*     */   }
/*     */ 
/*     */   public SignerInfo(DerInputStream paramDerInputStream)
/*     */     throws IOException, ParsingException
/*     */   {
/*  95 */     this(paramDerInputStream, false);
/*     */   }
/*     */ 
/*     */   public SignerInfo(DerInputStream paramDerInputStream, boolean paramBoolean)
/*     */     throws IOException, ParsingException
/*     */   {
/* 112 */     this.version = paramDerInputStream.getBigInteger();
/*     */ 
/* 115 */     DerValue[] arrayOfDerValue = paramDerInputStream.getSequence(2);
/* 116 */     byte[] arrayOfByte = arrayOfDerValue[0].toByteArray();
/* 117 */     this.issuerName = new X500Name(new DerValue((byte)48, arrayOfByte));
/*     */ 
/* 119 */     this.certificateSerialNumber = arrayOfDerValue[1].getBigInteger();
/*     */ 
/* 122 */     DerValue localDerValue = paramDerInputStream.getDerValue();
/*     */ 
/* 124 */     this.digestAlgorithmId = AlgorithmId.parse(localDerValue);
/*     */ 
/* 127 */     if (paramBoolean)
/*     */     {
/* 130 */       paramDerInputStream.getSet(0);
/*     */     }
/* 134 */     else if ((byte)paramDerInputStream.peekByte() == -96) {
/* 135 */       this.authenticatedAttributes = new PKCS9Attributes(paramDerInputStream);
/*     */     }
/*     */ 
/* 141 */     localDerValue = paramDerInputStream.getDerValue();
/*     */ 
/* 143 */     this.digestEncryptionAlgorithmId = AlgorithmId.parse(localDerValue);
/*     */ 
/* 146 */     this.encryptedDigest = paramDerInputStream.getOctetString();
/*     */ 
/* 149 */     if (paramBoolean)
/*     */     {
/* 152 */       paramDerInputStream.getSet(0);
/*     */     }
/* 156 */     else if ((paramDerInputStream.available() != 0) && ((byte)paramDerInputStream.peekByte() == -95))
/*     */     {
/* 158 */       this.unauthenticatedAttributes = new PKCS9Attributes(paramDerInputStream, true);
/*     */     }
/*     */ 
/* 164 */     if (paramDerInputStream.available() != 0)
/* 165 */       throw new ParsingException("extra data at the end");
/*     */   }
/*     */ 
/*     */   public void encode(DerOutputStream paramDerOutputStream)
/*     */     throws IOException
/*     */   {
/* 171 */     derEncode(paramDerOutputStream);
/*     */   }
/*     */ 
/*     */   public void derEncode(OutputStream paramOutputStream)
/*     */     throws IOException
/*     */   {
/* 184 */     DerOutputStream localDerOutputStream1 = new DerOutputStream();
/* 185 */     localDerOutputStream1.putInteger(this.version);
/* 186 */     DerOutputStream localDerOutputStream2 = new DerOutputStream();
/* 187 */     this.issuerName.encode(localDerOutputStream2);
/* 188 */     localDerOutputStream2.putInteger(this.certificateSerialNumber);
/* 189 */     localDerOutputStream1.write((byte)48, localDerOutputStream2);
/*     */ 
/* 191 */     this.digestAlgorithmId.encode(localDerOutputStream1);
/*     */ 
/* 194 */     if (this.authenticatedAttributes != null) {
/* 195 */       this.authenticatedAttributes.encode((byte)-96, localDerOutputStream1);
/*     */     }
/* 197 */     this.digestEncryptionAlgorithmId.encode(localDerOutputStream1);
/*     */ 
/* 199 */     localDerOutputStream1.putOctetString(this.encryptedDigest);
/*     */ 
/* 202 */     if (this.unauthenticatedAttributes != null) {
/* 203 */       this.unauthenticatedAttributes.encode((byte)-95, localDerOutputStream1);
/*     */     }
/* 205 */     DerOutputStream localDerOutputStream3 = new DerOutputStream();
/* 206 */     localDerOutputStream3.write((byte)48, localDerOutputStream1);
/*     */ 
/* 208 */     paramOutputStream.write(localDerOutputStream3.toByteArray());
/*     */   }
/*     */ 
/*     */   public X509Certificate getCertificate(PKCS7 paramPKCS7)
/*     */     throws IOException
/*     */   {
/* 219 */     return paramPKCS7.getCertificate(this.certificateSerialNumber, this.issuerName);
/*     */   }
/*     */ 
/*     */   public ArrayList<X509Certificate> getCertificateChain(PKCS7 paramPKCS7)
/*     */     throws IOException
/*     */   {
/* 229 */     X509Certificate localX509Certificate1 = paramPKCS7.getCertificate(this.certificateSerialNumber, this.issuerName);
/* 230 */     if (localX509Certificate1 == null) {
/* 231 */       return null;
/*     */     }
/* 233 */     ArrayList localArrayList = new ArrayList();
/* 234 */     localArrayList.add(localX509Certificate1);
/*     */ 
/* 236 */     X509Certificate[] arrayOfX509Certificate = paramPKCS7.getCertificates();
/* 237 */     if ((arrayOfX509Certificate == null) || (localX509Certificate1.getSubjectDN().equals(localX509Certificate1.getIssuerDN())))
/*     */     {
/* 239 */       return localArrayList;
/*     */     }
/*     */ 
/* 242 */     Principal localPrincipal = localX509Certificate1.getIssuerDN();
/* 243 */     int i = 0;
/*     */     while (true) {
/* 245 */       int j = 0;
/* 246 */       int k = i;
/* 247 */       while (k < arrayOfX509Certificate.length) {
/* 248 */         if (localPrincipal.equals(arrayOfX509Certificate[k].getSubjectDN()))
/*     */         {
/* 250 */           localArrayList.add(arrayOfX509Certificate[k]);
/*     */ 
/* 253 */           if (arrayOfX509Certificate[k].getSubjectDN().equals(arrayOfX509Certificate[k].getIssuerDN()))
/*     */           {
/* 255 */             i = arrayOfX509Certificate.length;
/*     */           } else {
/* 257 */             localPrincipal = arrayOfX509Certificate[k].getIssuerDN();
/* 258 */             X509Certificate localX509Certificate2 = arrayOfX509Certificate[i];
/* 259 */             arrayOfX509Certificate[i] = arrayOfX509Certificate[k];
/* 260 */             arrayOfX509Certificate[k] = localX509Certificate2;
/* 261 */             i++;
/*     */           }
/* 263 */           j = 1;
/* 264 */           break;
/*     */         }
/* 266 */         k++;
/*     */       }
/*     */ 
/* 269 */       if (j == 0) {
/*     */         break;
/*     */       }
/*     */     }
/* 273 */     return localArrayList;
/*     */   }
/*     */ 
/*     */   SignerInfo verify(PKCS7 paramPKCS7, byte[] paramArrayOfByte)
/*     */     throws NoSuchAlgorithmException, SignatureException
/*     */   {
/*     */     try
/*     */     {
/* 283 */       ContentInfo localContentInfo = paramPKCS7.getContentInfo();
/* 284 */       if (paramArrayOfByte == null) {
/* 285 */         paramArrayOfByte = localContentInfo.getContentBytes();
/*     */       }
/*     */ 
/* 288 */       String str = getDigestAlgorithmId().getName();
/*     */       byte[] arrayOfByte;
/* 294 */       if (this.authenticatedAttributes == null) {
/* 295 */         arrayOfByte = paramArrayOfByte;
/*     */       }
/*     */       else
/*     */       {
/* 299 */         localObject1 = (ObjectIdentifier)this.authenticatedAttributes.getAttributeValue(PKCS9Attribute.CONTENT_TYPE_OID);
/*     */ 
/* 302 */         if ((localObject1 == null) || (!((ObjectIdentifier)localObject1).equals(localContentInfo.contentType)))
/*     */         {
/* 304 */           return null;
/*     */         }
/*     */ 
/* 307 */         localObject2 = (byte[])this.authenticatedAttributes.getAttributeValue(PKCS9Attribute.MESSAGE_DIGEST_OID);
/*     */ 
/* 311 */         if (localObject2 == null) {
/* 312 */           return null;
/*     */         }
/* 314 */         localObject3 = MessageDigest.getInstance(AlgorithmId.getStandardDigestName(str));
/*     */ 
/* 316 */         localObject4 = ((MessageDigest)localObject3).digest(paramArrayOfByte);
/*     */ 
/* 318 */         if (localObject2.length != localObject4.length)
/* 319 */           return null;
/* 320 */         for (int i = 0; i < localObject2.length; i++) {
/* 321 */           if (localObject2[i] != localObject4[i]) {
/* 322 */             return null;
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/* 331 */         arrayOfByte = this.authenticatedAttributes.getDerEncoding();
/*     */       }
/*     */ 
/* 336 */       Object localObject1 = getDigestEncryptionAlgorithmId().getName();
/*     */ 
/* 341 */       Object localObject2 = AlgorithmId.getEncAlgFromSigAlg((String)localObject1);
/* 342 */       if (localObject2 != null) localObject1 = localObject2;
/* 343 */       Object localObject3 = AlgorithmId.makeSigAlg(str, (String)localObject1);
/*     */ 
/* 346 */       Object localObject4 = Signature.getInstance((String)localObject3);
/* 347 */       X509Certificate localX509Certificate = getCertificate(paramPKCS7);
/*     */ 
/* 349 */       if (localX509Certificate == null) {
/* 350 */         return null;
/*     */       }
/* 352 */       if (localX509Certificate.hasUnsupportedCriticalExtension()) {
/* 353 */         throw new SignatureException("Certificate has unsupported critical extension(s)");
/*     */       }
/*     */ 
/* 361 */       boolean[] arrayOfBoolean = localX509Certificate.getKeyUsage();
/* 362 */       if (arrayOfBoolean != null)
/*     */       {
/*     */         try
/*     */         {
/* 369 */           localObject5 = new KeyUsageExtension(arrayOfBoolean);
/*     */         } catch (IOException localIOException2) {
/* 371 */           throw new SignatureException("Failed to parse keyUsage extension");
/*     */         }
/*     */ 
/* 375 */         boolean bool1 = ((Boolean)((KeyUsageExtension)localObject5).get("digital_signature")).booleanValue();
/*     */ 
/* 378 */         boolean bool2 = ((Boolean)((KeyUsageExtension)localObject5).get("non_repudiation")).booleanValue();
/*     */ 
/* 381 */         if ((!bool1) && (!bool2)) {
/* 382 */           throw new SignatureException("Key usage restricted: cannot be used for digital signatures");
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 388 */       Object localObject5 = localX509Certificate.getPublicKey();
/* 389 */       ((Signature)localObject4).initVerify((PublicKey)localObject5);
/*     */ 
/* 391 */       ((Signature)localObject4).update(arrayOfByte);
/*     */ 
/* 393 */       if (((Signature)localObject4).verify(this.encryptedDigest))
/* 394 */         return this;
/*     */     }
/*     */     catch (IOException localIOException1)
/*     */     {
/* 398 */       throw new SignatureException("IO error verifying signature:\n" + localIOException1.getMessage());
/*     */     }
/*     */     catch (InvalidKeyException localInvalidKeyException)
/*     */     {
/* 402 */       throw new SignatureException("InvalidKey: " + localInvalidKeyException.getMessage());
/*     */     }
/*     */ 
/* 405 */     return null;
/*     */   }
/*     */ 
/*     */   SignerInfo verify(PKCS7 paramPKCS7)
/*     */     throws NoSuchAlgorithmException, SignatureException
/*     */   {
/* 411 */     return verify(paramPKCS7, null);
/*     */   }
/*     */ 
/*     */   public BigInteger getVersion()
/*     */   {
/* 416 */     return this.version;
/*     */   }
/*     */ 
/*     */   public X500Name getIssuerName() {
/* 420 */     return this.issuerName;
/*     */   }
/*     */ 
/*     */   public BigInteger getCertificateSerialNumber() {
/* 424 */     return this.certificateSerialNumber;
/*     */   }
/*     */ 
/*     */   public AlgorithmId getDigestAlgorithmId() {
/* 428 */     return this.digestAlgorithmId;
/*     */   }
/*     */ 
/*     */   public PKCS9Attributes getAuthenticatedAttributes() {
/* 432 */     return this.authenticatedAttributes;
/*     */   }
/*     */ 
/*     */   public AlgorithmId getDigestEncryptionAlgorithmId() {
/* 436 */     return this.digestEncryptionAlgorithmId;
/*     */   }
/*     */ 
/*     */   public byte[] getEncryptedDigest() {
/* 440 */     return this.encryptedDigest;
/*     */   }
/*     */ 
/*     */   public PKCS9Attributes getUnauthenticatedAttributes() {
/* 444 */     return this.unauthenticatedAttributes;
/*     */   }
/*     */ 
/*     */   public String toString() {
/* 448 */     HexDumpEncoder localHexDumpEncoder = new HexDumpEncoder();
/*     */ 
/* 450 */     String str = "";
/*     */ 
/* 452 */     str = str + "Signer Info for (issuer): " + this.issuerName + "\n";
/* 453 */     str = str + "\tversion: " + Debug.toHexString(this.version) + "\n";
/* 454 */     str = str + "\tcertificateSerialNumber: " + Debug.toHexString(this.certificateSerialNumber) + "\n";
/*     */ 
/* 456 */     str = str + "\tdigestAlgorithmId: " + this.digestAlgorithmId + "\n";
/* 457 */     if (this.authenticatedAttributes != null) {
/* 458 */       str = str + "\tauthenticatedAttributes: " + this.authenticatedAttributes + "\n";
/*     */     }
/*     */ 
/* 461 */     str = str + "\tdigestEncryptionAlgorithmId: " + this.digestEncryptionAlgorithmId + "\n";
/*     */ 
/* 464 */     str = str + "\tencryptedDigest: \n" + localHexDumpEncoder.encodeBuffer(this.encryptedDigest) + "\n";
/*     */ 
/* 466 */     if (this.unauthenticatedAttributes != null) {
/* 467 */       str = str + "\tunauthenticatedAttributes: " + this.unauthenticatedAttributes + "\n";
/*     */     }
/*     */ 
/* 470 */     return str;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.pkcs.SignerInfo
 * JD-Core Version:    0.6.2
 */