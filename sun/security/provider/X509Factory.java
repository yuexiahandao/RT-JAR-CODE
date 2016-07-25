/*     */ package sun.security.provider;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.security.cert.CRL;
/*     */ import java.security.cert.CRLException;
/*     */ import java.security.cert.CertPath;
/*     */ import java.security.cert.Certificate;
/*     */ import java.security.cert.CertificateException;
/*     */ import java.security.cert.CertificateFactorySpi;
/*     */ import java.security.cert.X509CRL;
/*     */ import java.security.cert.X509Certificate;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import sun.misc.BASE64Decoder;
/*     */ import sun.security.pkcs.PKCS7;
/*     */ import sun.security.pkcs.ParsingException;
/*     */ import sun.security.provider.certpath.X509CertPath;
/*     */ import sun.security.provider.certpath.X509CertificatePair;
/*     */ import sun.security.util.Cache;
/*     */ import sun.security.util.Cache.EqualByteArray;
/*     */ import sun.security.x509.X509CRLImpl;
/*     */ import sun.security.x509.X509CertImpl;
/*     */ 
/*     */ public class X509Factory extends CertificateFactorySpi
/*     */ {
/*     */   public static final String BEGIN_CERT = "-----BEGIN CERTIFICATE-----";
/*     */   public static final String END_CERT = "-----END CERTIFICATE-----";
/*     */   private static final int ENC_MAX_LENGTH = 4194304;
/*  67 */   private static final Cache certCache = Cache.newSoftMemoryCache(750);
/*  68 */   private static final Cache crlCache = Cache.newSoftMemoryCache(750);
/*     */ 
/*     */   public Certificate engineGenerateCertificate(InputStream paramInputStream)
/*     */     throws CertificateException
/*     */   {
/*  84 */     if (paramInputStream == null)
/*     */     {
/*  86 */       certCache.clear();
/*  87 */       X509CertificatePair.clearCache();
/*  88 */       throw new CertificateException("Missing input stream");
/*     */     }
/*     */     try {
/*  91 */       byte[] arrayOfByte = readOneBlock(paramInputStream);
/*  92 */       if (arrayOfByte != null) {
/*  93 */         X509CertImpl localX509CertImpl = (X509CertImpl)getFromCache(certCache, arrayOfByte);
/*  94 */         if (localX509CertImpl != null) {
/*  95 */           return localX509CertImpl;
/*     */         }
/*  97 */         localX509CertImpl = new X509CertImpl(arrayOfByte);
/*  98 */         addToCache(certCache, localX509CertImpl.getEncodedInternal(), localX509CertImpl);
/*  99 */         return localX509CertImpl;
/*     */       }
/* 101 */       throw new IOException("Empty input");
/*     */     }
/*     */     catch (IOException localIOException) {
/* 104 */       throw ((CertificateException)new CertificateException("Could not parse certificate: " + localIOException.toString()).initCause(localIOException));
/*     */     }
/*     */   }
/*     */ 
/*     */   private static int readFully(InputStream paramInputStream, ByteArrayOutputStream paramByteArrayOutputStream, int paramInt)
/*     */     throws IOException
/*     */   {
/* 115 */     int i = 0;
/* 116 */     byte[] arrayOfByte = new byte[2048];
/* 117 */     while (paramInt > 0) {
/* 118 */       int j = paramInputStream.read(arrayOfByte, 0, paramInt < 2048 ? paramInt : 2048);
/* 119 */       if (j <= 0) {
/*     */         break;
/*     */       }
/* 122 */       paramByteArrayOutputStream.write(arrayOfByte, 0, j);
/* 123 */       i += j;
/* 124 */       paramInt -= j;
/*     */     }
/* 126 */     return i;
/*     */   }
/*     */ 
/*     */   public static synchronized X509CertImpl intern(X509Certificate paramX509Certificate)
/*     */     throws CertificateException
/*     */   {
/* 144 */     if (paramX509Certificate == null) {
/* 145 */       return null;
/*     */     }
/* 147 */     boolean bool = paramX509Certificate instanceof X509CertImpl;
/*     */     byte[] arrayOfByte;
/* 149 */     if (bool)
/* 150 */       arrayOfByte = ((X509CertImpl)paramX509Certificate).getEncodedInternal();
/*     */     else {
/* 152 */       arrayOfByte = paramX509Certificate.getEncoded();
/*     */     }
/* 154 */     X509CertImpl localX509CertImpl = (X509CertImpl)getFromCache(certCache, arrayOfByte);
/* 155 */     if (localX509CertImpl != null) {
/* 156 */       return localX509CertImpl;
/*     */     }
/* 158 */     if (bool) {
/* 159 */       localX509CertImpl = (X509CertImpl)paramX509Certificate;
/*     */     } else {
/* 161 */       localX509CertImpl = new X509CertImpl(arrayOfByte);
/* 162 */       arrayOfByte = localX509CertImpl.getEncodedInternal();
/*     */     }
/* 164 */     addToCache(certCache, arrayOfByte, localX509CertImpl);
/* 165 */     return localX509CertImpl;
/*     */   }
/*     */ 
/*     */   public static synchronized X509CRLImpl intern(X509CRL paramX509CRL)
/*     */     throws CRLException
/*     */   {
/* 174 */     if (paramX509CRL == null) {
/* 175 */       return null;
/*     */     }
/* 177 */     boolean bool = paramX509CRL instanceof X509CRLImpl;
/*     */     byte[] arrayOfByte;
/* 179 */     if (bool)
/* 180 */       arrayOfByte = ((X509CRLImpl)paramX509CRL).getEncodedInternal();
/*     */     else {
/* 182 */       arrayOfByte = paramX509CRL.getEncoded();
/*     */     }
/* 184 */     X509CRLImpl localX509CRLImpl = (X509CRLImpl)getFromCache(crlCache, arrayOfByte);
/* 185 */     if (localX509CRLImpl != null) {
/* 186 */       return localX509CRLImpl;
/*     */     }
/* 188 */     if (bool) {
/* 189 */       localX509CRLImpl = (X509CRLImpl)paramX509CRL;
/*     */     } else {
/* 191 */       localX509CRLImpl = new X509CRLImpl(arrayOfByte);
/* 192 */       arrayOfByte = localX509CRLImpl.getEncodedInternal();
/*     */     }
/* 194 */     addToCache(crlCache, arrayOfByte, localX509CRLImpl);
/* 195 */     return localX509CRLImpl;
/*     */   }
/*     */ 
/*     */   private static synchronized Object getFromCache(Cache paramCache, byte[] paramArrayOfByte)
/*     */   {
/* 203 */     Cache.EqualByteArray localEqualByteArray = new Cache.EqualByteArray(paramArrayOfByte);
/* 204 */     Object localObject = paramCache.get(localEqualByteArray);
/* 205 */     return localObject;
/*     */   }
/*     */ 
/*     */   private static synchronized void addToCache(Cache paramCache, byte[] paramArrayOfByte, Object paramObject)
/*     */   {
/* 213 */     if (paramArrayOfByte.length > 4194304) {
/* 214 */       return;
/*     */     }
/* 216 */     Cache.EqualByteArray localEqualByteArray = new Cache.EqualByteArray(paramArrayOfByte);
/* 217 */     paramCache.put(localEqualByteArray, paramObject);
/*     */   }
/*     */ 
/*     */   public CertPath engineGenerateCertPath(InputStream paramInputStream)
/*     */     throws CertificateException
/*     */   {
/* 234 */     if (paramInputStream == null)
/* 235 */       throw new CertificateException("Missing input stream");
/*     */     try
/*     */     {
/* 238 */       byte[] arrayOfByte = readOneBlock(paramInputStream);
/* 239 */       if (arrayOfByte != null) {
/* 240 */         return new X509CertPath(new ByteArrayInputStream(arrayOfByte));
/*     */       }
/* 242 */       throw new IOException("Empty input");
/*     */     }
/*     */     catch (IOException localIOException) {
/* 245 */       throw new CertificateException(localIOException.getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   public CertPath engineGenerateCertPath(InputStream paramInputStream, String paramString)
/*     */     throws CertificateException
/*     */   {
/* 265 */     if (paramInputStream == null)
/* 266 */       throw new CertificateException("Missing input stream");
/*     */     try
/*     */     {
/* 269 */       byte[] arrayOfByte = readOneBlock(paramInputStream);
/* 270 */       if (arrayOfByte != null) {
/* 271 */         return new X509CertPath(new ByteArrayInputStream(arrayOfByte), paramString);
/*     */       }
/* 273 */       throw new IOException("Empty input");
/*     */     }
/*     */     catch (IOException localIOException) {
/* 276 */       throw new CertificateException(localIOException.getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   public CertPath engineGenerateCertPath(List<? extends Certificate> paramList)
/*     */     throws CertificateException
/*     */   {
/* 298 */     return new X509CertPath(paramList);
/*     */   }
/*     */ 
/*     */   public Iterator<String> engineGetCertPathEncodings()
/*     */   {
/* 314 */     return X509CertPath.getEncodingsStatic();
/*     */   }
/*     */ 
/*     */   public Collection<? extends Certificate> engineGenerateCertificates(InputStream paramInputStream)
/*     */     throws CertificateException
/*     */   {
/* 331 */     if (paramInputStream == null)
/* 332 */       throw new CertificateException("Missing input stream");
/*     */     try
/*     */     {
/* 335 */       return parseX509orPKCS7Cert(paramInputStream);
/*     */     } catch (IOException localIOException) {
/* 337 */       throw new CertificateException(localIOException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public CRL engineGenerateCRL(InputStream paramInputStream)
/*     */     throws CRLException
/*     */   {
/* 356 */     if (paramInputStream == null)
/*     */     {
/* 358 */       crlCache.clear();
/* 359 */       throw new CRLException("Missing input stream");
/*     */     }
/*     */     try {
/* 362 */       byte[] arrayOfByte = readOneBlock(paramInputStream);
/* 363 */       if (arrayOfByte != null) {
/* 364 */         X509CRLImpl localX509CRLImpl = (X509CRLImpl)getFromCache(crlCache, arrayOfByte);
/* 365 */         if (localX509CRLImpl != null) {
/* 366 */           return localX509CRLImpl;
/*     */         }
/* 368 */         localX509CRLImpl = new X509CRLImpl(arrayOfByte);
/* 369 */         addToCache(crlCache, localX509CRLImpl.getEncodedInternal(), localX509CRLImpl);
/* 370 */         return localX509CRLImpl;
/*     */       }
/* 372 */       throw new IOException("Empty input");
/*     */     }
/*     */     catch (IOException localIOException) {
/* 375 */       throw new CRLException(localIOException.getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   public Collection<? extends CRL> engineGenerateCRLs(InputStream paramInputStream)
/*     */     throws CRLException
/*     */   {
/* 393 */     if (paramInputStream == null)
/* 394 */       throw new CRLException("Missing input stream");
/*     */     try
/*     */     {
/* 397 */       return parseX509orPKCS7CRL(paramInputStream);
/*     */     } catch (IOException localIOException) {
/* 399 */       throw new CRLException(localIOException.getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   private Collection<? extends Certificate> parseX509orPKCS7Cert(InputStream paramInputStream)
/*     */     throws CertificateException, IOException
/*     */   {
/* 412 */     ArrayList localArrayList = new ArrayList();
/* 413 */     byte[] arrayOfByte = readOneBlock(paramInputStream);
/* 414 */     if (arrayOfByte == null)
/* 415 */       return new ArrayList(0);
/*     */     try
/*     */     {
/* 418 */       PKCS7 localPKCS7 = new PKCS7(arrayOfByte);
/* 419 */       X509Certificate[] arrayOfX509Certificate = localPKCS7.getCertificates();
/*     */ 
/* 421 */       if (arrayOfX509Certificate != null) {
/* 422 */         return Arrays.asList(arrayOfX509Certificate);
/*     */       }
/*     */ 
/* 425 */       return new ArrayList(0);
/*     */     }
/*     */     catch (ParsingException localParsingException) {
/* 428 */       while (arrayOfByte != null) {
/* 429 */         localArrayList.add(new X509CertImpl(arrayOfByte));
/* 430 */         arrayOfByte = readOneBlock(paramInputStream);
/*     */       }
/*     */     }
/* 433 */     return localArrayList;
/*     */   }
/*     */ 
/*     */   private Collection<? extends CRL> parseX509orPKCS7CRL(InputStream paramInputStream)
/*     */     throws CRLException, IOException
/*     */   {
/* 445 */     ArrayList localArrayList = new ArrayList();
/* 446 */     byte[] arrayOfByte = readOneBlock(paramInputStream);
/* 447 */     if (arrayOfByte == null)
/* 448 */       return new ArrayList(0);
/*     */     try
/*     */     {
/* 451 */       PKCS7 localPKCS7 = new PKCS7(arrayOfByte);
/* 452 */       X509CRL[] arrayOfX509CRL = localPKCS7.getCRLs();
/*     */ 
/* 454 */       if (arrayOfX509CRL != null) {
/* 455 */         return Arrays.asList(arrayOfX509CRL);
/*     */       }
/*     */ 
/* 458 */       return new ArrayList(0);
/*     */     }
/*     */     catch (ParsingException localParsingException) {
/* 461 */       while (arrayOfByte != null) {
/* 462 */         localArrayList.add(new X509CRLImpl(arrayOfByte));
/* 463 */         arrayOfByte = readOneBlock(paramInputStream);
/*     */       }
/*     */     }
/* 466 */     return localArrayList;
/*     */   }
/*     */ 
/*     */   private static byte[] readOneBlock(InputStream paramInputStream)
/*     */     throws IOException
/*     */   {
/* 484 */     int i = paramInputStream.read();
/* 485 */     if (i == -1) {
/* 486 */       return null;
/*     */     }
/* 488 */     if (i == 48) {
/* 489 */       localObject = new ByteArrayOutputStream(2048);
/* 490 */       ((ByteArrayOutputStream)localObject).write(i);
/* 491 */       readBERInternal(paramInputStream, (ByteArrayOutputStream)localObject, i);
/* 492 */       return ((ByteArrayOutputStream)localObject).toByteArray(); } 
/*     */ Object localObject = new char[2048];
/* 496 */     int j = 0;
/*     */ 
/* 499 */     int k = i == 45 ? 1 : 0;
/* 500 */     int m = i == 45 ? -1 : i;
/*     */     int n;
/*     */     while (true) { n = paramInputStream.read();
/* 503 */       if (n == -1)
/*     */       {
/* 506 */         return null;
/*     */       }
/* 508 */       if (n == 45) {
/* 509 */         k++;
/*     */       } else {
/* 511 */         k = 0;
/* 512 */         m = n;
/*     */       }
/* 514 */       if ((k == 5) && ((m == -1) || (m == 13) || (m == 10))) {
/*     */         break;
/*     */       }
/*     */  }
/*     */ 
/* 521 */     StringBuffer localStringBuffer1 = new StringBuffer("-----");
/*     */     int i1;
/*     */     while (true) {
/* 523 */       i1 = paramInputStream.read();
/* 524 */       if (i1 == -1) {
/* 525 */         throw new IOException("Incomplete data");
/*     */       }
/* 527 */       if (i1 == 10) {
/* 528 */         n = 10;
/* 529 */         break;
/*     */       }
/* 531 */       if (i1 == 13) {
/* 532 */         i1 = paramInputStream.read();
/* 533 */         if (i1 == -1) {
/* 534 */           throw new IOException("Incomplete data");
/*     */         }
/* 536 */         if (i1 == 10) {
/* 537 */           n = 10; break;
/*     */         }
/* 539 */         n = 13;
/* 540 */         localObject[(j++)] = ((char)i1);
/*     */ 
/* 542 */         break;
/*     */       }
/* 544 */       localStringBuffer1.append((char)i1);
/*     */     }
/*     */ 
/*     */     while (true)
/*     */     {
/* 549 */       i1 = paramInputStream.read();
/* 550 */       if (i1 == -1) {
/* 551 */         throw new IOException("Incomplete data");
/*     */       }
/* 553 */       if (i1 == 45) break;
/* 554 */       localObject[(j++)] = ((char)i1);
/* 555 */       if (j >= localObject.length) {
/* 556 */         localObject = Arrays.copyOf((char[])localObject, localObject.length + 1024);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 564 */     StringBuffer localStringBuffer2 = new StringBuffer("-");
/*     */     while (true) {
/* 566 */       int i2 = paramInputStream.read();
/*     */ 
/* 569 */       if ((i2 == -1) || (i2 == n) || (i2 == 10)) {
/*     */         break;
/*     */       }
/* 572 */       if (i2 != 13) localStringBuffer2.append((char)i2);
/*     */     }
/*     */ 
/* 575 */     checkHeaderFooter(localStringBuffer1.toString(), localStringBuffer2.toString());
/*     */ 
/* 577 */     BASE64Decoder localBASE64Decoder = new BASE64Decoder();
/* 578 */     return localBASE64Decoder.decodeBuffer(new String((char[])localObject, 0, j));
/*     */   }
/*     */ 
/*     */   private static void checkHeaderFooter(String paramString1, String paramString2)
/*     */     throws IOException
/*     */   {
/* 584 */     if ((paramString1.length() < 16) || (!paramString1.startsWith("-----BEGIN ")) || (!paramString1.endsWith("-----")))
/*     */     {
/* 586 */       throw new IOException("Illegal header: " + paramString1);
/*     */     }
/* 588 */     if ((paramString2.length() < 14) || (!paramString2.startsWith("-----END ")) || (!paramString2.endsWith("-----")))
/*     */     {
/* 590 */       throw new IOException("Illegal footer: " + paramString2);
/*     */     }
/* 592 */     String str1 = paramString1.substring(11, paramString1.length() - 5);
/* 593 */     String str2 = paramString2.substring(9, paramString2.length() - 5);
/* 594 */     if (!str1.equals(str2))
/* 595 */       throw new IOException("Header and footer do not match: " + paramString1 + " " + paramString2);
/*     */   }
/*     */ 
/*     */   private static int readBERInternal(InputStream paramInputStream, ByteArrayOutputStream paramByteArrayOutputStream, int paramInt)
/*     */     throws IOException
/*     */   {
/* 613 */     if (paramInt == -1) {
/* 614 */       paramInt = paramInputStream.read();
/* 615 */       if (paramInt == -1) {
/* 616 */         throw new IOException("BER/DER tag info absent");
/*     */       }
/* 618 */       if ((paramInt & 0x1F) == 31) {
/* 619 */         throw new IOException("Multi octets tag not supported");
/*     */       }
/* 621 */       paramByteArrayOutputStream.write(paramInt);
/*     */     }
/*     */ 
/* 624 */     int i = paramInputStream.read();
/* 625 */     if (i == -1) {
/* 626 */       throw new IOException("BER/DER length info ansent");
/*     */     }
/* 628 */     paramByteArrayOutputStream.write(i);
/*     */     int k;
/* 632 */     if (i == 128) {
/* 633 */       if ((paramInt & 0x20) != 32) {
/* 634 */         throw new IOException("Non constructed encoding must have definite length");
/*     */       }
/*     */       while (true)
/*     */       {
/* 638 */         k = readBERInternal(paramInputStream, paramByteArrayOutputStream, -1);
/* 639 */         if (k == 0)
/*     */           break;
/*     */       }
/*     */     }
/*     */     int j;
/* 644 */     if (i < 128) {
/* 645 */       j = i;
/* 646 */     } else if (i == 129) {
/* 647 */       j = paramInputStream.read();
/* 648 */       if (j == -1) {
/* 649 */         throw new IOException("Incomplete BER/DER length info");
/*     */       }
/* 651 */       paramByteArrayOutputStream.write(j);
/*     */     }
/*     */     else
/*     */     {
/*     */       int m;
/* 652 */       if (i == 130) {
/* 653 */         k = paramInputStream.read();
/* 654 */         m = paramInputStream.read();
/* 655 */         if (m == -1) {
/* 656 */           throw new IOException("Incomplete BER/DER length info");
/*     */         }
/* 658 */         paramByteArrayOutputStream.write(k);
/* 659 */         paramByteArrayOutputStream.write(m);
/* 660 */         j = k << 8 | m;
/*     */       }
/*     */       else
/*     */       {
/*     */         int n;
/* 661 */         if (i == 131) {
/* 662 */           k = paramInputStream.read();
/* 663 */           m = paramInputStream.read();
/* 664 */           n = paramInputStream.read();
/* 665 */           if (n == -1) {
/* 666 */             throw new IOException("Incomplete BER/DER length info");
/*     */           }
/* 668 */           paramByteArrayOutputStream.write(k);
/* 669 */           paramByteArrayOutputStream.write(m);
/* 670 */           paramByteArrayOutputStream.write(n);
/* 671 */           j = k << 16 | m << 8 | n;
/* 672 */         } else if (i == 132) {
/* 673 */           k = paramInputStream.read();
/* 674 */           m = paramInputStream.read();
/* 675 */           n = paramInputStream.read();
/* 676 */           int i1 = paramInputStream.read();
/* 677 */           if (i1 == -1) {
/* 678 */             throw new IOException("Incomplete BER/DER length info");
/*     */           }
/* 680 */           if (k > 127) {
/* 681 */             throw new IOException("Invalid BER/DER data (a little huge?)");
/*     */           }
/* 683 */           paramByteArrayOutputStream.write(k);
/* 684 */           paramByteArrayOutputStream.write(m);
/* 685 */           paramByteArrayOutputStream.write(n);
/* 686 */           paramByteArrayOutputStream.write(i1);
/* 687 */           j = k << 24 | m << 16 | n << 8 | i1;
/*     */         }
/*     */         else {
/* 690 */           throw new IOException("Invalid BER/DER data (too huge?)");
/*     */         }
/*     */       }
/*     */     }
/* 692 */     if (readFully(paramInputStream, paramByteArrayOutputStream, j) != j) {
/* 693 */       throw new IOException("Incomplete BER/DER data");
/*     */     }
/*     */ 
/* 696 */     return paramInt;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.provider.X509Factory
 * JD-Core Version:    0.6.2
 */