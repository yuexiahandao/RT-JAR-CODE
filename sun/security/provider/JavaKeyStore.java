/*     */ package sun.security.provider;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.security.DigestInputStream;
/*     */ import java.security.DigestOutputStream;
/*     */ import java.security.Key;
/*     */ import java.security.KeyStoreException;
/*     */ import java.security.KeyStoreSpi;
/*     */ import java.security.MessageDigest;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.PrivateKey;
/*     */ import java.security.UnrecoverableKeyException;
/*     */ import java.security.cert.Certificate;
/*     */ import java.security.cert.CertificateException;
/*     */ import java.security.cert.CertificateFactory;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Date;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Hashtable;
/*     */ import java.util.List;
/*     */ import sun.misc.IOUtils;
/*     */ import sun.security.pkcs.EncryptedPrivateKeyInfo;
/*     */ 
/*     */ abstract class JavaKeyStore extends KeyStoreSpi
/*     */ {
/*     */   private static final int MAGIC = -17957139;
/*     */   private static final int VERSION_1 = 1;
/*     */   private static final int VERSION_2 = 2;
/*     */   private final Hashtable<String, Object> entries;
/*     */ 
/*     */   JavaKeyStore()
/*     */   {
/*  92 */     this.entries = new Hashtable();
/*     */   }
/*     */ 
/*     */   abstract String convertAlias(String paramString);
/*     */ 
/*     */   public Key engineGetKey(String paramString, char[] paramArrayOfChar)
/*     */     throws NoSuchAlgorithmException, UnrecoverableKeyException
/*     */   {
/* 118 */     Object localObject = this.entries.get(convertAlias(paramString));
/*     */ 
/* 120 */     if ((localObject == null) || (!(localObject instanceof KeyEntry))) {
/* 121 */       return null;
/*     */     }
/* 123 */     if (paramArrayOfChar == null) {
/* 124 */       throw new UnrecoverableKeyException("Password must not be null");
/* 127 */     }
/*     */ KeyProtector localKeyProtector = new KeyProtector(paramArrayOfChar);
/* 128 */     byte[] arrayOfByte = ((KeyEntry)localObject).protectedPrivKey;
/*     */     EncryptedPrivateKeyInfo localEncryptedPrivateKeyInfo;
/*     */     try {
/* 132 */       localEncryptedPrivateKeyInfo = new EncryptedPrivateKeyInfo(arrayOfByte);
/*     */     } catch (IOException localIOException) {
/* 134 */       throw new UnrecoverableKeyException("Private key not stored as PKCS #8 EncryptedPrivateKeyInfo");
/*     */     }
/*     */ 
/* 138 */     return localKeyProtector.recover(localEncryptedPrivateKeyInfo);
/*     */   }
/*     */ 
/*     */   public Certificate[] engineGetCertificateChain(String paramString)
/*     */   {
/* 153 */     Object localObject = this.entries.get(convertAlias(paramString));
/*     */ 
/* 155 */     if ((localObject != null) && ((localObject instanceof KeyEntry))) {
/* 156 */       if (((KeyEntry)localObject).chain == null) {
/* 157 */         return null;
/*     */       }
/* 159 */       return (Certificate[])((KeyEntry)localObject).chain.clone();
/*     */     }
/*     */ 
/* 162 */     return null;
/*     */   }
/*     */ 
/*     */   public Certificate engineGetCertificate(String paramString)
/*     */   {
/* 182 */     Object localObject = this.entries.get(convertAlias(paramString));
/*     */ 
/* 184 */     if (localObject != null) {
/* 185 */       if ((localObject instanceof TrustedCertEntry)) {
/* 186 */         return ((TrustedCertEntry)localObject).cert;
/*     */       }
/* 188 */       if (((KeyEntry)localObject).chain == null) {
/* 189 */         return null;
/*     */       }
/* 191 */       return ((KeyEntry)localObject).chain[0];
/*     */     }
/*     */ 
/* 195 */     return null;
/*     */   }
/*     */ 
/*     */   public Date engineGetCreationDate(String paramString)
/*     */   {
/* 208 */     Object localObject = this.entries.get(convertAlias(paramString));
/*     */ 
/* 210 */     if (localObject != null) {
/* 211 */       if ((localObject instanceof TrustedCertEntry)) {
/* 212 */         return new Date(((TrustedCertEntry)localObject).date.getTime());
/*     */       }
/* 214 */       return new Date(((KeyEntry)localObject).date.getTime());
/*     */     }
/*     */ 
/* 217 */     return null;
/*     */   }
/*     */ 
/*     */   public void engineSetKeyEntry(String paramString, Key paramKey, char[] paramArrayOfChar, Certificate[] paramArrayOfCertificate)
/*     */     throws KeyStoreException
/*     */   {
/* 247 */     KeyProtector localKeyProtector = null;
/*     */ 
/* 249 */     if (!(paramKey instanceof PrivateKey))
/* 250 */       throw new KeyStoreException("Cannot store non-PrivateKeys");
/*     */     try
/*     */     {
/* 253 */       synchronized (this.entries) {
/* 254 */         KeyEntry localKeyEntry = new KeyEntry(null);
/* 255 */         localKeyEntry.date = new Date();
/*     */ 
/* 258 */         localKeyProtector = new KeyProtector(paramArrayOfChar);
/* 259 */         localKeyEntry.protectedPrivKey = localKeyProtector.protect(paramKey);
/*     */ 
/* 262 */         if ((paramArrayOfCertificate != null) && (paramArrayOfCertificate.length != 0))
/*     */         {
/* 264 */           localKeyEntry.chain = ((Certificate[])paramArrayOfCertificate.clone());
/*     */         }
/* 266 */         else localKeyEntry.chain = null;
/*     */ 
/* 269 */         this.entries.put(convertAlias(paramString), localKeyEntry);
/*     */       }
/*     */     } catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {
/* 272 */       throw new KeyStoreException("Key protection algorithm not found");
/*     */     } finally {
/* 274 */       localKeyProtector = null;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void engineSetKeyEntry(String paramString, byte[] paramArrayOfByte, Certificate[] paramArrayOfCertificate)
/*     */     throws KeyStoreException
/*     */   {
/* 305 */     synchronized (this.entries)
/*     */     {
/*     */       try
/*     */       {
/* 309 */         new EncryptedPrivateKeyInfo(paramArrayOfByte);
/*     */       } catch (IOException localIOException) {
/* 311 */         throw new KeyStoreException("key is not encoded as EncryptedPrivateKeyInfo");
/*     */       }
/*     */ 
/* 315 */       KeyEntry localKeyEntry = new KeyEntry(null);
/* 316 */       localKeyEntry.date = new Date();
/*     */ 
/* 318 */       localKeyEntry.protectedPrivKey = ((byte[])paramArrayOfByte.clone());
/* 319 */       if ((paramArrayOfCertificate != null) && (paramArrayOfCertificate.length != 0))
/*     */       {
/* 321 */         localKeyEntry.chain = ((Certificate[])paramArrayOfCertificate.clone());
/*     */       }
/* 323 */       else localKeyEntry.chain = null;
/*     */ 
/* 326 */       this.entries.put(convertAlias(paramString), localKeyEntry);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void engineSetCertificateEntry(String paramString, Certificate paramCertificate)
/*     */     throws KeyStoreException
/*     */   {
/* 347 */     synchronized (this.entries)
/*     */     {
/* 349 */       Object localObject1 = this.entries.get(convertAlias(paramString));
/* 350 */       if ((localObject1 != null) && ((localObject1 instanceof KeyEntry))) {
/* 351 */         throw new KeyStoreException("Cannot overwrite own certificate");
/*     */       }
/*     */ 
/* 355 */       TrustedCertEntry localTrustedCertEntry = new TrustedCertEntry(null);
/* 356 */       localTrustedCertEntry.cert = paramCertificate;
/* 357 */       localTrustedCertEntry.date = new Date();
/* 358 */       this.entries.put(convertAlias(paramString), localTrustedCertEntry);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void engineDeleteEntry(String paramString)
/*     */     throws KeyStoreException
/*     */   {
/* 372 */     synchronized (this.entries) {
/* 373 */       this.entries.remove(convertAlias(paramString));
/*     */     }
/*     */   }
/*     */ 
/*     */   public Enumeration<String> engineAliases()
/*     */   {
/* 383 */     return this.entries.keys();
/*     */   }
/*     */ 
/*     */   public boolean engineContainsAlias(String paramString)
/*     */   {
/* 394 */     return this.entries.containsKey(convertAlias(paramString));
/*     */   }
/*     */ 
/*     */   public int engineSize()
/*     */   {
/* 403 */     return this.entries.size();
/*     */   }
/*     */ 
/*     */   public boolean engineIsKeyEntry(String paramString)
/*     */   {
/* 414 */     Object localObject = this.entries.get(convertAlias(paramString));
/* 415 */     if ((localObject != null) && ((localObject instanceof KeyEntry))) {
/* 416 */       return true;
/*     */     }
/* 418 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean engineIsCertificateEntry(String paramString)
/*     */   {
/* 430 */     Object localObject = this.entries.get(convertAlias(paramString));
/* 431 */     if ((localObject != null) && ((localObject instanceof TrustedCertEntry))) {
/* 432 */       return true;
/*     */     }
/* 434 */     return false;
/*     */   }
/*     */ 
/*     */   public String engineGetCertificateAlias(Certificate paramCertificate)
/*     */   {
/* 457 */     for (Enumeration localEnumeration = this.entries.keys(); localEnumeration.hasMoreElements(); ) {
/* 458 */       String str = (String)localEnumeration.nextElement();
/* 459 */       Object localObject = this.entries.get(str);
/*     */       Certificate localCertificate;
/* 460 */       if ((localObject instanceof TrustedCertEntry)) {
/* 461 */         localCertificate = ((TrustedCertEntry)localObject).cert; } else {
/* 462 */         if (((KeyEntry)localObject).chain == null) continue;
/* 463 */         localCertificate = ((KeyEntry)localObject).chain[0];
/*     */       }
/*     */ 
/* 467 */       if (localCertificate.equals(paramCertificate)) {
/* 468 */         return str;
/*     */       }
/*     */     }
/* 471 */     return null;
/*     */   }
/*     */ 
/*     */   public void engineStore(OutputStream paramOutputStream, char[] paramArrayOfChar)
/*     */     throws IOException, NoSuchAlgorithmException, CertificateException
/*     */   {
/* 490 */     synchronized (this.entries)
/*     */     {
/* 524 */       if (paramArrayOfChar == null) {
/* 525 */         throw new IllegalArgumentException("password can't be null");
/*     */       }
/*     */ 
/* 530 */       MessageDigest localMessageDigest = getPreKeyedHash(paramArrayOfChar);
/* 531 */       DataOutputStream localDataOutputStream = new DataOutputStream(new DigestOutputStream(paramOutputStream, localMessageDigest));
/*     */ 
/* 534 */       localDataOutputStream.writeInt(-17957139);
/*     */ 
/* 536 */       localDataOutputStream.writeInt(2);
/*     */ 
/* 538 */       localDataOutputStream.writeInt(this.entries.size());
/*     */ 
/* 540 */       for (Object localObject1 = this.entries.keys(); ((Enumeration)localObject1).hasMoreElements(); )
/*     */       {
/* 542 */         String str = (String)((Enumeration)localObject1).nextElement();
/* 543 */         Object localObject2 = this.entries.get(str);
/*     */         byte[] arrayOfByte;
/* 545 */         if ((localObject2 instanceof KeyEntry))
/*     */         {
/* 548 */           localDataOutputStream.writeInt(1);
/*     */ 
/* 551 */           localDataOutputStream.writeUTF(str);
/*     */ 
/* 554 */           localDataOutputStream.writeLong(((KeyEntry)localObject2).date.getTime());
/*     */ 
/* 557 */           localDataOutputStream.writeInt(((KeyEntry)localObject2).protectedPrivKey.length);
/* 558 */           localDataOutputStream.write(((KeyEntry)localObject2).protectedPrivKey);
/*     */           int i;
/* 562 */           if (((KeyEntry)localObject2).chain == null)
/* 563 */             i = 0;
/*     */           else {
/* 565 */             i = ((KeyEntry)localObject2).chain.length;
/*     */           }
/* 567 */           localDataOutputStream.writeInt(i);
/* 568 */           for (int j = 0; j < i; j++) {
/* 569 */             arrayOfByte = ((KeyEntry)localObject2).chain[j].getEncoded();
/* 570 */             localDataOutputStream.writeUTF(((KeyEntry)localObject2).chain[j].getType());
/* 571 */             localDataOutputStream.writeInt(arrayOfByte.length);
/* 572 */             localDataOutputStream.write(arrayOfByte);
/*     */           }
/*     */         }
/*     */         else
/*     */         {
/* 577 */           localDataOutputStream.writeInt(2);
/*     */ 
/* 580 */           localDataOutputStream.writeUTF(str);
/*     */ 
/* 583 */           localDataOutputStream.writeLong(((TrustedCertEntry)localObject2).date.getTime());
/*     */ 
/* 586 */           arrayOfByte = ((TrustedCertEntry)localObject2).cert.getEncoded();
/* 587 */           localDataOutputStream.writeUTF(((TrustedCertEntry)localObject2).cert.getType());
/* 588 */           localDataOutputStream.writeInt(arrayOfByte.length);
/* 589 */           localDataOutputStream.write(arrayOfByte);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 598 */       localObject1 = localMessageDigest.digest();
/*     */ 
/* 600 */       localDataOutputStream.write((byte[])localObject1);
/* 601 */       localDataOutputStream.flush();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void engineLoad(InputStream paramInputStream, char[] paramArrayOfChar)
/*     */     throws IOException, NoSuchAlgorithmException, CertificateException
/*     */   {
/* 625 */     synchronized (this.entries)
/*     */     {
/* 627 */       MessageDigest localMessageDigest = null;
/* 628 */       CertificateFactory localCertificateFactory = null;
/* 629 */       Hashtable localHashtable = null;
/* 630 */       ByteArrayInputStream localByteArrayInputStream = null;
/* 631 */       byte[] arrayOfByte1 = null;
/*     */ 
/* 633 */       if (paramInputStream == null)
/*     */         return;
/*     */       DataInputStream localDataInputStream;
/* 636 */       if (paramArrayOfChar != null) {
/* 637 */         localMessageDigest = getPreKeyedHash(paramArrayOfChar);
/* 638 */         localDataInputStream = new DataInputStream(new DigestInputStream(paramInputStream, localMessageDigest));
/*     */       } else {
/* 640 */         localDataInputStream = new DataInputStream(paramInputStream);
/*     */       }
/*     */ 
/* 645 */       int i = localDataInputStream.readInt();
/* 646 */       int j = localDataInputStream.readInt();
/*     */ 
/* 648 */       if ((i != -17957139) || ((j != 1) && (j != 2)))
/*     */       {
/* 650 */         throw new IOException("Invalid keystore format");
/*     */       }
/*     */ 
/* 653 */       if (j == 1) {
/* 654 */         localCertificateFactory = CertificateFactory.getInstance("X509");
/*     */       }
/*     */       else {
/* 657 */         localHashtable = new Hashtable(3);
/*     */       }
/*     */ 
/* 660 */       this.entries.clear();
/* 661 */       int k = localDataInputStream.readInt();
/*     */       Object localObject1;
/* 663 */       for (int m = 0; m < k; m++)
/*     */       {
/* 667 */         int n = localDataInputStream.readInt();
/*     */         String str1;
/* 669 */         if (n == 1)
/*     */         {
/* 671 */           localObject1 = new KeyEntry(null);
/*     */ 
/* 674 */           str1 = localDataInputStream.readUTF();
/*     */ 
/* 677 */           ((KeyEntry)localObject1).date = new Date(localDataInputStream.readLong());
/*     */ 
/* 680 */           ((KeyEntry)localObject1).protectedPrivKey = IOUtils.readFully(localDataInputStream, localDataInputStream.readInt(), true);
/*     */ 
/* 684 */           int i2 = localDataInputStream.readInt();
/* 685 */           if (i2 > 0) {
/* 686 */             ArrayList localArrayList = new ArrayList(i2 > 10 ? 10 : i2);
/*     */ 
/* 688 */             for (int i3 = 0; i3 < i2; i3++) {
/* 689 */               if (j == 2)
/*     */               {
/* 693 */                 String str3 = localDataInputStream.readUTF();
/* 694 */                 if (localHashtable.containsKey(str3))
/*     */                 {
/* 696 */                   localCertificateFactory = (CertificateFactory)localHashtable.get(str3);
/*     */                 }
/*     */                 else {
/* 699 */                   localCertificateFactory = CertificateFactory.getInstance(str3);
/*     */ 
/* 702 */                   localHashtable.put(str3, localCertificateFactory);
/*     */                 }
/*     */               }
/*     */ 
/* 706 */               arrayOfByte1 = IOUtils.readFully(localDataInputStream, localDataInputStream.readInt(), true);
/* 707 */               localByteArrayInputStream = new ByteArrayInputStream(arrayOfByte1);
/* 708 */               localArrayList.add(localCertificateFactory.generateCertificate(localByteArrayInputStream));
/* 709 */               localByteArrayInputStream.close();
/*     */             }
/*     */ 
/* 712 */             ((KeyEntry)localObject1).chain = ((Certificate[])localArrayList.toArray(new Certificate[i2]));
/*     */           }
/*     */ 
/* 716 */           this.entries.put(str1, localObject1);
/*     */         }
/* 718 */         else if (n == 2)
/*     */         {
/* 720 */           localObject1 = new TrustedCertEntry(null);
/*     */ 
/* 723 */           str1 = localDataInputStream.readUTF();
/*     */ 
/* 726 */           ((TrustedCertEntry)localObject1).date = new Date(localDataInputStream.readLong());
/*     */ 
/* 729 */           if (j == 2)
/*     */           {
/* 733 */             String str2 = localDataInputStream.readUTF();
/* 734 */             if (localHashtable.containsKey(str2))
/*     */             {
/* 736 */               localCertificateFactory = (CertificateFactory)localHashtable.get(str2);
/*     */             }
/*     */             else {
/* 739 */               localCertificateFactory = CertificateFactory.getInstance(str2);
/*     */ 
/* 742 */               localHashtable.put(str2, localCertificateFactory);
/*     */             }
/*     */           }
/* 745 */           arrayOfByte1 = IOUtils.readFully(localDataInputStream, localDataInputStream.readInt(), true);
/* 746 */           localByteArrayInputStream = new ByteArrayInputStream(arrayOfByte1);
/* 747 */           ((TrustedCertEntry)localObject1).cert = localCertificateFactory.generateCertificate(localByteArrayInputStream);
/* 748 */           localByteArrayInputStream.close();
/*     */ 
/* 751 */           this.entries.put(str1, localObject1);
/*     */         }
/*     */         else {
/* 754 */           throw new IOException("Unrecognized keystore entry");
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 763 */       if (paramArrayOfChar != null)
/*     */       {
/* 765 */         byte[] arrayOfByte2 = localMessageDigest.digest();
/* 766 */         byte[] arrayOfByte3 = new byte[arrayOfByte2.length];
/* 767 */         localDataInputStream.readFully(arrayOfByte3);
/* 768 */         for (int i1 = 0; i1 < arrayOfByte2.length; i1++)
/* 769 */           if (arrayOfByte2[i1] != arrayOfByte3[i1]) {
/* 770 */             localObject1 = new UnrecoverableKeyException("Password verification failed");
/*     */ 
/* 772 */             throw ((IOException)new IOException("Keystore was tampered with, or password was incorrect").initCause((Throwable)localObject1));
/*     */           }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private MessageDigest getPreKeyedHash(char[] paramArrayOfChar)
/*     */     throws NoSuchAlgorithmException, UnsupportedEncodingException
/*     */   {
/* 790 */     MessageDigest localMessageDigest = MessageDigest.getInstance("SHA");
/* 791 */     byte[] arrayOfByte = new byte[paramArrayOfChar.length * 2];
/* 792 */     int i = 0; for (int j = 0; i < paramArrayOfChar.length; i++) {
/* 793 */       arrayOfByte[(j++)] = ((byte)(paramArrayOfChar[i] >> '\b'));
/* 794 */       arrayOfByte[(j++)] = ((byte)paramArrayOfChar[i]);
/*     */     }
/* 796 */     localMessageDigest.update(arrayOfByte);
/* 797 */     for (i = 0; i < arrayOfByte.length; i++)
/* 798 */       arrayOfByte[i] = 0;
/* 799 */     localMessageDigest.update("Mighty Aphrodite".getBytes("UTF8"));
/* 800 */     return localMessageDigest;
/*     */   }
/*     */ 
/*     */   public static final class CaseExactJKS extends JavaKeyStore
/*     */   {
/*     */     String convertAlias(String paramString)
/*     */     {
/*  64 */       return paramString;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static final class JKS extends JavaKeyStore
/*     */   {
/*     */     String convertAlias(String paramString)
/*     */     {
/*  57 */       return paramString.toLowerCase();
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class KeyEntry
/*     */   {
/*     */     Date date;
/*     */     byte[] protectedPrivKey;
/*     */     Certificate[] chain;
/*     */   }
/*     */ 
/*     */   private static class TrustedCertEntry
/*     */   {
/*     */     Date date;
/*     */     Certificate cert;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.provider.JavaKeyStore
 * JD-Core Version:    0.6.2
 */