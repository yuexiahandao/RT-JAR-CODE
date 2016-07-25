/*     */ package sun.security.pkcs;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.math.BigInteger;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.Principal;
/*     */ import java.security.SignatureException;
/*     */ import java.security.cert.CRLException;
/*     */ import java.security.cert.CertificateException;
/*     */ import java.security.cert.CertificateFactory;
/*     */ import java.security.cert.X509CRL;
/*     */ import java.security.cert.X509Certificate;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import java.util.Vector;
/*     */ import sun.security.util.Debug;
/*     */ import sun.security.util.DerEncoder;
/*     */ import sun.security.util.DerInputStream;
/*     */ import sun.security.util.DerOutputStream;
/*     */ import sun.security.util.DerValue;
/*     */ import sun.security.util.ObjectIdentifier;
/*     */ import sun.security.x509.AlgorithmId;
/*     */ import sun.security.x509.X500Name;
/*     */ import sun.security.x509.X509CRLImpl;
/*     */ import sun.security.x509.X509CertImpl;
/*     */ import sun.security.x509.X509CertInfo;
/*     */ 
/*     */ public class PKCS7
/*     */ {
/*     */   private ObjectIdentifier contentType;
/*  60 */   private BigInteger version = null;
/*  61 */   private AlgorithmId[] digestAlgorithmIds = null;
/*  62 */   private ContentInfo contentInfo = null;
/*  63 */   private X509Certificate[] certificates = null;
/*  64 */   private X509CRL[] crls = null;
/*  65 */   private SignerInfo[] signerInfos = null;
/*     */ 
/*  67 */   private boolean oldStyle = false;
/*     */   private Principal[] certIssuerNames;
/*     */ 
/*     */   public PKCS7(InputStream paramInputStream)
/*     */     throws ParsingException, IOException
/*     */   {
/*  80 */     DataInputStream localDataInputStream = new DataInputStream(paramInputStream);
/*  81 */     byte[] arrayOfByte = new byte[localDataInputStream.available()];
/*  82 */     localDataInputStream.readFully(arrayOfByte);
/*     */ 
/*  84 */     parse(new DerInputStream(arrayOfByte));
/*     */   }
/*     */ 
/*     */   public PKCS7(DerInputStream paramDerInputStream)
/*     */     throws ParsingException
/*     */   {
/*  95 */     parse(paramDerInputStream);
/*     */   }
/*     */ 
/*     */   public PKCS7(byte[] paramArrayOfByte)
/*     */     throws ParsingException
/*     */   {
/*     */     try
/*     */     {
/* 107 */       DerInputStream localDerInputStream = new DerInputStream(paramArrayOfByte);
/* 108 */       parse(localDerInputStream);
/*     */     } catch (IOException localIOException) {
/* 110 */       ParsingException localParsingException = new ParsingException("Unable to parse the encoded bytes");
/*     */ 
/* 112 */       localParsingException.initCause(localIOException);
/* 113 */       throw localParsingException;
/*     */     }
/*     */   }
/*     */ 
/*     */   private void parse(DerInputStream paramDerInputStream)
/*     */     throws ParsingException
/*     */   {
/*     */     try
/*     */     {
/* 124 */       paramDerInputStream.mark(paramDerInputStream.available());
/*     */ 
/* 126 */       parse(paramDerInputStream, false);
/*     */     } catch (IOException localIOException1) {
/*     */       try {
/* 129 */         paramDerInputStream.reset();
/*     */ 
/* 131 */         parse(paramDerInputStream, true);
/* 132 */         this.oldStyle = true;
/*     */       } catch (IOException localIOException2) {
/* 134 */         ParsingException localParsingException = new ParsingException(localIOException2.getMessage());
/*     */ 
/* 136 */         localParsingException.initCause(localIOException1);
/* 137 */         localParsingException.addSuppressed(localIOException2);
/* 138 */         throw localParsingException;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private void parse(DerInputStream paramDerInputStream, boolean paramBoolean)
/*     */     throws IOException
/*     */   {
/* 153 */     this.contentInfo = new ContentInfo(paramDerInputStream, paramBoolean);
/* 154 */     this.contentType = this.contentInfo.contentType;
/* 155 */     DerValue localDerValue = this.contentInfo.getContent();
/*     */ 
/* 157 */     if (this.contentType.equals(ContentInfo.SIGNED_DATA_OID))
/* 158 */       parseSignedData(localDerValue);
/* 159 */     else if (this.contentType.equals(ContentInfo.OLD_SIGNED_DATA_OID))
/*     */     {
/* 161 */       parseOldSignedData(localDerValue);
/* 162 */     } else if (this.contentType.equals(ContentInfo.NETSCAPE_CERT_SEQUENCE_OID))
/* 163 */       parseNetscapeCertChain(localDerValue);
/*     */     else
/* 165 */       throw new ParsingException("content type " + this.contentType + " not supported.");
/*     */   }
/*     */ 
/*     */   public PKCS7(AlgorithmId[] paramArrayOfAlgorithmId, ContentInfo paramContentInfo, X509Certificate[] paramArrayOfX509Certificate, X509CRL[] paramArrayOfX509CRL, SignerInfo[] paramArrayOfSignerInfo)
/*     */   {
/* 185 */     this.version = BigInteger.ONE;
/* 186 */     this.digestAlgorithmIds = paramArrayOfAlgorithmId;
/* 187 */     this.contentInfo = paramContentInfo;
/* 188 */     this.certificates = paramArrayOfX509Certificate;
/* 189 */     this.crls = paramArrayOfX509CRL;
/* 190 */     this.signerInfos = paramArrayOfSignerInfo;
/*     */   }
/*     */ 
/*     */   public PKCS7(AlgorithmId[] paramArrayOfAlgorithmId, ContentInfo paramContentInfo, X509Certificate[] paramArrayOfX509Certificate, SignerInfo[] paramArrayOfSignerInfo)
/*     */   {
/* 197 */     this(paramArrayOfAlgorithmId, paramContentInfo, paramArrayOfX509Certificate, null, paramArrayOfSignerInfo);
/*     */   }
/*     */ 
/*     */   private void parseNetscapeCertChain(DerValue paramDerValue) throws ParsingException, IOException
/*     */   {
/* 202 */     DerInputStream localDerInputStream = new DerInputStream(paramDerValue.toByteArray());
/* 203 */     DerValue[] arrayOfDerValue = localDerInputStream.getSequence(2);
/* 204 */     this.certificates = new X509Certificate[arrayOfDerValue.length];
/*     */ 
/* 206 */     CertificateFactory localCertificateFactory = null;
/*     */     try {
/* 208 */       localCertificateFactory = CertificateFactory.getInstance("X.509");
/*     */     }
/*     */     catch (CertificateException localCertificateException1)
/*     */     {
/*     */     }
/* 213 */     for (int i = 0; i < arrayOfDerValue.length; i++) {
/* 214 */       ByteArrayInputStream localByteArrayInputStream = null;
/*     */       try {
/* 216 */         if (localCertificateFactory == null) {
/* 217 */           this.certificates[i] = new X509CertImpl(arrayOfDerValue[i]);
/*     */         } else {
/* 219 */           byte[] arrayOfByte = arrayOfDerValue[i].toByteArray();
/* 220 */           localByteArrayInputStream = new ByteArrayInputStream(arrayOfByte);
/* 221 */           this.certificates[i] = ((X509Certificate)localCertificateFactory.generateCertificate(localByteArrayInputStream));
/*     */ 
/* 223 */           localByteArrayInputStream.close();
/* 224 */           localByteArrayInputStream = null;
/*     */         }
/*     */       } catch (CertificateException localCertificateException2) {
/* 227 */         localParsingException = new ParsingException(localCertificateException2.getMessage());
/* 228 */         localParsingException.initCause(localCertificateException2);
/* 229 */         throw localParsingException;
/*     */       } catch (IOException localIOException) {
/* 231 */         ParsingException localParsingException = new ParsingException(localIOException.getMessage());
/* 232 */         localParsingException.initCause(localIOException);
/* 233 */         throw localParsingException;
/*     */       } finally {
/* 235 */         if (localByteArrayInputStream != null)
/* 236 */           localByteArrayInputStream.close();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private void parseSignedData(DerValue paramDerValue)
/*     */     throws ParsingException, IOException
/*     */   {
/* 244 */     DerInputStream localDerInputStream = paramDerValue.toDerInputStream();
/*     */ 
/* 247 */     this.version = localDerInputStream.getBigInteger();
/*     */ 
/* 250 */     DerValue[] arrayOfDerValue1 = localDerInputStream.getSet(1);
/* 251 */     int i = arrayOfDerValue1.length;
/* 252 */     this.digestAlgorithmIds = new AlgorithmId[i];
/*     */     try {
/* 254 */       for (int j = 0; j < i; j++) {
/* 255 */         localObject1 = arrayOfDerValue1[j];
/* 256 */         this.digestAlgorithmIds[j] = AlgorithmId.parse((DerValue)localObject1);
/*     */       }
/*     */     }
/*     */     catch (IOException localIOException1) {
/* 260 */       Object localObject1 = new ParsingException("Error parsing digest AlgorithmId IDs: " + localIOException1.getMessage());
/*     */ 
/* 263 */       ((ParsingException)localObject1).initCause(localIOException1);
/* 264 */       throw ((Throwable)localObject1);
/*     */     }
/*     */ 
/* 267 */     this.contentInfo = new ContentInfo(localDerInputStream);
/*     */ 
/* 269 */     CertificateFactory localCertificateFactory = null;
/*     */     try {
/* 271 */       localCertificateFactory = CertificateFactory.getInstance("X.509");
/*     */     }
/*     */     catch (CertificateException localCertificateException1)
/*     */     {
/*     */     }
/*     */     Object localObject3;
/* 280 */     if ((byte)localDerInputStream.peekByte() == -96) {
/* 281 */       arrayOfDerValue2 = localDerInputStream.getSet(2, true);
/*     */ 
/* 283 */       i = arrayOfDerValue2.length;
/* 284 */       this.certificates = new X509Certificate[i];
/* 285 */       k = 0;
/*     */ 
/* 287 */       for (int m = 0; m < i; m++) {
/* 288 */         localObject3 = null;
/*     */         try {
/* 290 */           int n = arrayOfDerValue2[m].getTag();
/*     */ 
/* 293 */           if (n == 48) {
/* 294 */             if (localCertificateFactory == null) {
/* 295 */               this.certificates[k] = new X509CertImpl(arrayOfDerValue2[m]);
/*     */             } else {
/* 297 */               localObject4 = arrayOfDerValue2[m].toByteArray();
/* 298 */               localObject3 = new ByteArrayInputStream((byte[])localObject4);
/* 299 */               this.certificates[k] = ((X509Certificate)localCertificateFactory.generateCertificate((InputStream)localObject3));
/*     */ 
/* 301 */               ((ByteArrayInputStream)localObject3).close();
/* 302 */               localObject3 = null;
/*     */             }
/* 304 */             k++;
/*     */           }
/*     */         } catch (CertificateException localCertificateException2) {
/* 307 */           localObject4 = new ParsingException(localCertificateException2.getMessage());
/* 308 */           ((ParsingException)localObject4).initCause(localCertificateException2);
/* 309 */           throw ((Throwable)localObject4);
/*     */         } catch (IOException localIOException2) {
/* 311 */           Object localObject4 = new ParsingException(localIOException2.getMessage());
/* 312 */           ((ParsingException)localObject4).initCause(localIOException2);
/* 313 */           throw ((Throwable)localObject4);
/*     */         } finally {
/* 315 */           if (localObject3 != null)
/* 316 */             ((ByteArrayInputStream)localObject3).close();
/*     */         }
/*     */       }
/* 319 */       if (k != i)
/* 320 */         this.certificates = ((X509Certificate[])Arrays.copyOf(this.certificates, k));
/*     */     }
/*     */     Object localObject2;
/* 325 */     if ((byte)localDerInputStream.peekByte() == -95) {
/* 326 */       arrayOfDerValue2 = localDerInputStream.getSet(1, true);
/*     */ 
/* 328 */       i = arrayOfDerValue2.length;
/* 329 */       this.crls = new X509CRL[i];
/*     */ 
/* 331 */       for (k = 0; k < i; k++) {
/* 332 */         localObject2 = null;
/*     */         try {
/* 334 */           if (localCertificateFactory == null) {
/* 335 */             this.crls[k] = new X509CRLImpl(arrayOfDerValue2[k]);
/*     */           } else {
/* 337 */             localObject3 = arrayOfDerValue2[k].toByteArray();
/* 338 */             localObject2 = new ByteArrayInputStream((byte[])localObject3);
/* 339 */             this.crls[k] = ((X509CRL)localCertificateFactory.generateCRL((InputStream)localObject2));
/* 340 */             ((ByteArrayInputStream)localObject2).close();
/* 341 */             localObject2 = null;
/*     */           }
/*     */         } catch (CRLException localCRLException) {
/* 344 */           ParsingException localParsingException = new ParsingException(localCRLException.getMessage());
/*     */ 
/* 346 */           localParsingException.initCause(localCRLException);
/* 347 */           throw localParsingException;
/*     */         } finally {
/* 349 */           if (localObject2 != null) {
/* 350 */             ((ByteArrayInputStream)localObject2).close();
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 356 */     DerValue[] arrayOfDerValue2 = localDerInputStream.getSet(1);
/*     */ 
/* 358 */     i = arrayOfDerValue2.length;
/* 359 */     this.signerInfos = new SignerInfo[i];
/*     */ 
/* 361 */     for (int k = 0; k < i; k++) {
/* 362 */       localObject2 = arrayOfDerValue2[k].toDerInputStream();
/* 363 */       this.signerInfos[k] = new SignerInfo((DerInputStream)localObject2);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void parseOldSignedData(DerValue paramDerValue)
/*     */     throws ParsingException, IOException
/*     */   {
/* 374 */     DerInputStream localDerInputStream1 = paramDerValue.toDerInputStream();
/*     */ 
/* 377 */     this.version = localDerInputStream1.getBigInteger();
/*     */ 
/* 380 */     DerValue[] arrayOfDerValue1 = localDerInputStream1.getSet(1);
/* 381 */     int i = arrayOfDerValue1.length;
/*     */ 
/* 383 */     this.digestAlgorithmIds = new AlgorithmId[i];
/*     */     try {
/* 385 */       for (int j = 0; j < i; j++) {
/* 386 */         DerValue localDerValue = arrayOfDerValue1[j];
/* 387 */         this.digestAlgorithmIds[j] = AlgorithmId.parse(localDerValue);
/*     */       }
/*     */     } catch (IOException localIOException1) {
/* 390 */       throw new ParsingException("Error parsing digest AlgorithmId IDs");
/*     */     }
/*     */ 
/* 394 */     this.contentInfo = new ContentInfo(localDerInputStream1, true);
/*     */ 
/* 397 */     CertificateFactory localCertificateFactory = null;
/*     */     try {
/* 399 */       localCertificateFactory = CertificateFactory.getInstance("X.509");
/*     */     }
/*     */     catch (CertificateException localCertificateException1) {
/*     */     }
/* 403 */     DerValue[] arrayOfDerValue2 = localDerInputStream1.getSet(2);
/* 404 */     i = arrayOfDerValue2.length;
/* 405 */     this.certificates = new X509Certificate[i];
/*     */ 
/* 407 */     for (int k = 0; k < i; k++) {
/* 408 */       ByteArrayInputStream localByteArrayInputStream = null;
/*     */       try {
/* 410 */         if (localCertificateFactory == null) {
/* 411 */           this.certificates[k] = new X509CertImpl(arrayOfDerValue2[k]);
/*     */         } else {
/* 413 */           byte[] arrayOfByte = arrayOfDerValue2[k].toByteArray();
/* 414 */           localByteArrayInputStream = new ByteArrayInputStream(arrayOfByte);
/* 415 */           this.certificates[k] = ((X509Certificate)localCertificateFactory.generateCertificate(localByteArrayInputStream));
/*     */ 
/* 417 */           localByteArrayInputStream.close();
/* 418 */           localByteArrayInputStream = null;
/*     */         }
/*     */       } catch (CertificateException localCertificateException2) {
/* 421 */         localParsingException = new ParsingException(localCertificateException2.getMessage());
/* 422 */         localParsingException.initCause(localCertificateException2);
/* 423 */         throw localParsingException;
/*     */       } catch (IOException localIOException2) {
/* 425 */         ParsingException localParsingException = new ParsingException(localIOException2.getMessage());
/* 426 */         localParsingException.initCause(localIOException2);
/* 427 */         throw localParsingException;
/*     */       } finally {
/* 429 */         if (localByteArrayInputStream != null) {
/* 430 */           localByteArrayInputStream.close();
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 435 */     localDerInputStream1.getSet(0);
/*     */ 
/* 438 */     DerValue[] arrayOfDerValue3 = localDerInputStream1.getSet(1);
/* 439 */     i = arrayOfDerValue3.length;
/* 440 */     this.signerInfos = new SignerInfo[i];
/* 441 */     for (int m = 0; m < i; m++) {
/* 442 */       DerInputStream localDerInputStream2 = arrayOfDerValue3[m].toDerInputStream();
/* 443 */       this.signerInfos[m] = new SignerInfo(localDerInputStream2, true);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void encodeSignedData(OutputStream paramOutputStream)
/*     */     throws IOException
/*     */   {
/* 454 */     DerOutputStream localDerOutputStream = new DerOutputStream();
/* 455 */     encodeSignedData(localDerOutputStream);
/* 456 */     paramOutputStream.write(localDerOutputStream.toByteArray());
/*     */   }
/*     */ 
/*     */   public void encodeSignedData(DerOutputStream paramDerOutputStream)
/*     */     throws IOException
/*     */   {
/* 468 */     DerOutputStream localDerOutputStream = new DerOutputStream();
/*     */ 
/* 471 */     localDerOutputStream.putInteger(this.version);
/*     */ 
/* 474 */     localDerOutputStream.putOrderedSetOf((byte)49, this.digestAlgorithmIds);
/*     */ 
/* 477 */     this.contentInfo.encode(localDerOutputStream);
/*     */ 
/* 480 */     if ((this.certificates != null) && (this.certificates.length != 0))
/*     */     {
/* 482 */       localObject1 = new X509CertImpl[this.certificates.length];
/* 483 */       for (int i = 0; i < this.certificates.length; i++) {
/* 484 */         if ((this.certificates[i] instanceof X509CertImpl))
/* 485 */           localObject1[i] = ((X509CertImpl)this.certificates[i]);
/*     */         else {
/*     */           try {
/* 488 */             byte[] arrayOfByte1 = this.certificates[i].getEncoded();
/* 489 */             localObject1[i] = new X509CertImpl(arrayOfByte1);
/*     */           } catch (CertificateException localCertificateException) {
/* 491 */             IOException localIOException1 = new IOException(localCertificateException.getMessage());
/* 492 */             localIOException1.initCause(localCertificateException);
/* 493 */             throw localIOException1;
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 500 */       localDerOutputStream.putOrderedSetOf((byte)-96, (DerEncoder[])localObject1);
/*     */     }
/*     */ 
/* 504 */     if ((this.crls != null) && (this.crls.length != 0))
/*     */     {
/* 506 */       localObject1 = new HashSet(this.crls.length);
/* 507 */       for (Object localObject3 : this.crls) {
/* 508 */         if ((localObject3 instanceof X509CRLImpl))
/* 509 */           ((Set)localObject1).add((X509CRLImpl)localObject3);
/*     */         else {
/*     */           try {
/* 512 */             byte[] arrayOfByte2 = localObject3.getEncoded();
/* 513 */             ((Set)localObject1).add(new X509CRLImpl(arrayOfByte2));
/*     */           } catch (CRLException localCRLException) {
/* 515 */             IOException localIOException2 = new IOException(localCRLException.getMessage());
/* 516 */             localIOException2.initCause(localCRLException);
/* 517 */             throw localIOException2;
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 524 */       localDerOutputStream.putOrderedSetOf((byte)-95, (DerEncoder[])((Set)localObject1).toArray(new X509CRLImpl[((Set)localObject1).size()]));
/*     */     }
/*     */ 
/* 529 */     localDerOutputStream.putOrderedSetOf((byte)49, this.signerInfos);
/*     */ 
/* 532 */     Object localObject1 = new DerValue((byte)48, localDerOutputStream.toByteArray());
/*     */ 
/* 536 */     ??? = new ContentInfo(ContentInfo.SIGNED_DATA_OID, (DerValue)localObject1);
/*     */ 
/* 540 */     ((ContentInfo)???).encode(paramDerOutputStream);
/*     */   }
/*     */ 
/*     */   public SignerInfo verify(SignerInfo paramSignerInfo, byte[] paramArrayOfByte)
/*     */     throws NoSuchAlgorithmException, SignatureException
/*     */   {
/* 554 */     return paramSignerInfo.verify(this, paramArrayOfByte);
/*     */   }
/*     */ 
/*     */   public SignerInfo[] verify(byte[] paramArrayOfByte)
/*     */     throws NoSuchAlgorithmException, SignatureException
/*     */   {
/* 568 */     Vector localVector = new Vector();
/* 569 */     for (int i = 0; i < this.signerInfos.length; i++)
/*     */     {
/* 571 */       SignerInfo localSignerInfo = verify(this.signerInfos[i], paramArrayOfByte);
/* 572 */       if (localSignerInfo != null) {
/* 573 */         localVector.addElement(localSignerInfo);
/*     */       }
/*     */     }
/* 576 */     if (localVector.size() != 0)
/*     */     {
/* 578 */       SignerInfo[] arrayOfSignerInfo = new SignerInfo[localVector.size()];
/* 579 */       localVector.copyInto(arrayOfSignerInfo);
/* 580 */       return arrayOfSignerInfo;
/*     */     }
/* 582 */     return null;
/*     */   }
/*     */ 
/*     */   public SignerInfo[] verify()
/*     */     throws NoSuchAlgorithmException, SignatureException
/*     */   {
/* 593 */     return verify(null);
/*     */   }
/*     */ 
/*     */   public BigInteger getVersion()
/*     */   {
/* 602 */     return this.version;
/*     */   }
/*     */ 
/*     */   public AlgorithmId[] getDigestAlgorithmIds()
/*     */   {
/* 611 */     return this.digestAlgorithmIds;
/*     */   }
/*     */ 
/*     */   public ContentInfo getContentInfo()
/*     */   {
/* 618 */     return this.contentInfo;
/*     */   }
/*     */ 
/*     */   public X509Certificate[] getCertificates()
/*     */   {
/* 627 */     if (this.certificates != null) {
/* 628 */       return (X509Certificate[])this.certificates.clone();
/*     */     }
/* 630 */     return null;
/*     */   }
/*     */ 
/*     */   public X509CRL[] getCRLs()
/*     */   {
/* 639 */     if (this.crls != null) {
/* 640 */       return (X509CRL[])this.crls.clone();
/*     */     }
/* 642 */     return null;
/*     */   }
/*     */ 
/*     */   public SignerInfo[] getSignerInfos()
/*     */   {
/* 651 */     return this.signerInfos;
/*     */   }
/*     */ 
/*     */   public X509Certificate getCertificate(BigInteger paramBigInteger, X500Name paramX500Name)
/*     */   {
/* 663 */     if (this.certificates != null) {
/* 664 */       if (this.certIssuerNames == null)
/* 665 */         populateCertIssuerNames();
/* 666 */       for (int i = 0; i < this.certificates.length; i++) {
/* 667 */         X509Certificate localX509Certificate = this.certificates[i];
/* 668 */         BigInteger localBigInteger = localX509Certificate.getSerialNumber();
/* 669 */         if ((paramBigInteger.equals(localBigInteger)) && (paramX500Name.equals(this.certIssuerNames[i])))
/*     */         {
/* 672 */           return localX509Certificate;
/*     */         }
/*     */       }
/*     */     }
/* 676 */     return null;
/*     */   }
/*     */ 
/*     */   private void populateCertIssuerNames()
/*     */   {
/* 684 */     if (this.certificates == null) {
/* 685 */       return;
/*     */     }
/* 687 */     this.certIssuerNames = new Principal[this.certificates.length];
/* 688 */     for (int i = 0; i < this.certificates.length; i++) {
/* 689 */       X509Certificate localX509Certificate = this.certificates[i];
/* 690 */       Principal localPrincipal = localX509Certificate.getIssuerDN();
/* 691 */       if (!(localPrincipal instanceof X500Name))
/*     */       {
/*     */         try
/*     */         {
/* 697 */           X509CertInfo localX509CertInfo = new X509CertInfo(localX509Certificate.getTBSCertificate());
/*     */ 
/* 699 */           localPrincipal = (Principal)localX509CertInfo.get("issuer.dname");
/*     */         }
/*     */         catch (Exception localException)
/*     */         {
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 707 */       this.certIssuerNames[i] = localPrincipal;
/*     */     }
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 715 */     String str = "";
/*     */ 
/* 717 */     str = str + this.contentInfo + "\n";
/* 718 */     if (this.version != null)
/* 719 */       str = str + "PKCS7 :: version: " + Debug.toHexString(this.version) + "\n";
/*     */     int i;
/* 720 */     if (this.digestAlgorithmIds != null) {
/* 721 */       str = str + "PKCS7 :: digest AlgorithmIds: \n";
/* 722 */       for (i = 0; i < this.digestAlgorithmIds.length; i++)
/* 723 */         str = str + "\t" + this.digestAlgorithmIds[i] + "\n";
/*     */     }
/* 725 */     if (this.certificates != null) {
/* 726 */       str = str + "PKCS7 :: certificates: \n";
/* 727 */       for (i = 0; i < this.certificates.length; i++)
/* 728 */         str = str + "\t" + i + ".   " + this.certificates[i] + "\n";
/*     */     }
/* 730 */     if (this.crls != null) {
/* 731 */       str = str + "PKCS7 :: crls: \n";
/* 732 */       for (i = 0; i < this.crls.length; i++)
/* 733 */         str = str + "\t" + i + ".   " + this.crls[i] + "\n";
/*     */     }
/* 735 */     if (this.signerInfos != null) {
/* 736 */       str = str + "PKCS7 :: signer infos: \n";
/* 737 */       for (i = 0; i < this.signerInfos.length; i++)
/* 738 */         str = str + "\t" + i + ".  " + this.signerInfos[i] + "\n";
/*     */     }
/* 740 */     return str;
/*     */   }
/*     */ 
/*     */   public boolean isOldStyle()
/*     */   {
/* 748 */     return this.oldStyle;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.pkcs.PKCS7
 * JD-Core Version:    0.6.2
 */