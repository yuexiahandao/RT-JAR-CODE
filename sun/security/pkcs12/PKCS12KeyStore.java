/*      */ package sun.security.pkcs12;
/*      */ 
/*      */ import java.io.ByteArrayInputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.OutputStream;
/*      */ import java.io.UnsupportedEncodingException;
/*      */ import java.security.AlgorithmParameters;
/*      */ import java.security.Key;
/*      */ import java.security.KeyFactory;
/*      */ import java.security.KeyStoreException;
/*      */ import java.security.KeyStoreSpi;
/*      */ import java.security.MessageDigest;
/*      */ import java.security.NoSuchAlgorithmException;
/*      */ import java.security.PrivateKey;
/*      */ import java.security.SecureRandom;
/*      */ import java.security.UnrecoverableKeyException;
/*      */ import java.security.cert.Certificate;
/*      */ import java.security.cert.CertificateException;
/*      */ import java.security.cert.CertificateFactory;
/*      */ import java.security.cert.X509Certificate;
/*      */ import java.security.spec.AlgorithmParameterSpec;
/*      */ import java.security.spec.KeySpec;
/*      */ import java.security.spec.PKCS8EncodedKeySpec;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Date;
/*      */ import java.util.Enumeration;
/*      */ import java.util.HashSet;
/*      */ import java.util.Hashtable;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedHashMap;
/*      */ import java.util.Set;
/*      */ import javax.crypto.Cipher;
/*      */ import javax.crypto.Mac;
/*      */ import javax.crypto.SecretKey;
/*      */ import javax.crypto.SecretKeyFactory;
/*      */ import javax.crypto.spec.PBEKeySpec;
/*      */ import javax.crypto.spec.PBEParameterSpec;
/*      */ import javax.security.auth.x500.X500Principal;
/*      */ import sun.security.pkcs.ContentInfo;
/*      */ import sun.security.pkcs.EncryptedPrivateKeyInfo;
/*      */ import sun.security.util.Debug;
/*      */ import sun.security.util.DerInputStream;
/*      */ import sun.security.util.DerOutputStream;
/*      */ import sun.security.util.DerValue;
/*      */ import sun.security.util.ObjectIdentifier;
/*      */ import sun.security.x509.AlgorithmId;
/*      */ 
/*      */ public final class PKCS12KeyStore extends KeyStoreSpi
/*      */ {
/*      */   public static final int VERSION_3 = 3;
/*  126 */   private static final Debug debug = Debug.getInstance("pkcs12");
/*      */ 
/*  128 */   private static final int[] keyBag = { 1, 2, 840, 113549, 1, 12, 10, 1, 2 };
/*  129 */   private static final int[] certBag = { 1, 2, 840, 113549, 1, 12, 10, 1, 3 };
/*      */ 
/*  131 */   private static final int[] pkcs9Name = { 1, 2, 840, 113549, 1, 9, 20 };
/*  132 */   private static final int[] pkcs9KeyId = { 1, 2, 840, 113549, 1, 9, 21 };
/*      */ 
/*  134 */   private static final int[] pkcs9certType = { 1, 2, 840, 113549, 1, 9, 22, 1 };
/*      */ 
/*  136 */   private static final int[] pbeWithSHAAnd40BitRC2CBC = { 1, 2, 840, 113549, 1, 12, 1, 6 };
/*      */ 
/*  138 */   private static final int[] pbeWithSHAAnd3KeyTripleDESCBC = { 1, 2, 840, 113549, 1, 12, 1, 3 };
/*      */   private static ObjectIdentifier PKCS8ShroudedKeyBag_OID;
/*      */   private static ObjectIdentifier CertBag_OID;
/*      */   private static ObjectIdentifier PKCS9FriendlyName_OID;
/*      */   private static ObjectIdentifier PKCS9LocalKeyId_OID;
/*      */   private static ObjectIdentifier PKCS9CertType_OID;
/*      */   private static ObjectIdentifier pbeWithSHAAnd40BitRC2CBC_OID;
/*      */   private static ObjectIdentifier pbeWithSHAAnd3KeyTripleDESCBC_OID;
/*  149 */   private int counter = 0;
/*      */   private static final int iterationCount = 1024;
/*      */   private static final int SALT_LEN = 20;
/*  156 */   private int privateKeyCount = 0;
/*      */   private SecureRandom random;
/*  202 */   private Hashtable<String, KeyEntry> entries = new Hashtable();
/*      */ 
/*  205 */   private ArrayList<KeyEntry> keyList = new ArrayList();
/*  206 */   private LinkedHashMap<X500Principal, X509Certificate> certsMap = new LinkedHashMap();
/*      */ 
/*  208 */   private ArrayList<CertEntry> certEntries = new ArrayList();
/*      */ 
/*      */   public Key engineGetKey(String paramString, char[] paramArrayOfChar)
/*      */     throws NoSuchAlgorithmException, UnrecoverableKeyException
/*      */   {
/*  228 */     KeyEntry localKeyEntry = (KeyEntry)this.entries.get(paramString.toLowerCase());
/*  229 */     PrivateKey localPrivateKey = null;
/*      */ 
/*  231 */     if (localKeyEntry == null) {
/*  232 */       return null; } 
/*      */ byte[] arrayOfByte1 = localKeyEntry.protectedPrivKey;
/*      */     byte[] arrayOfByte2;
/*      */     Object localObject1;
/*      */     Object localObject3;
/*      */     ObjectIdentifier localObjectIdentifier;
/*      */     AlgorithmParameters localAlgorithmParameters;
/*      */     try { EncryptedPrivateKeyInfo localEncryptedPrivateKeyInfo = new EncryptedPrivateKeyInfo(arrayOfByte1);
/*      */ 
/*  245 */       arrayOfByte2 = localEncryptedPrivateKeyInfo.getEncryptedData();
/*      */ 
/*  248 */       localObject1 = new DerValue(localEncryptedPrivateKeyInfo.getAlgorithm().encode());
/*  249 */       localObject3 = ((DerValue)localObject1).toDerInputStream();
/*  250 */       localObjectIdentifier = ((DerInputStream)localObject3).getOID();
/*  251 */       localAlgorithmParameters = parseAlgParameters((DerInputStream)localObject3);
/*      */     } catch (IOException localIOException)
/*      */     {
/*  254 */       localObject1 = new UnrecoverableKeyException("Private key not stored as PKCS#8 EncryptedPrivateKeyInfo: " + localIOException);
/*      */ 
/*  257 */       ((UnrecoverableKeyException)localObject1).initCause(localIOException);
/*  258 */       throw ((Throwable)localObject1);
/*      */     }
/*      */ 
/*      */     try
/*      */     {
/*      */       byte[] arrayOfByte3;
/*      */       try
/*      */       {
/*  266 */         localObject1 = getPBEKey(paramArrayOfChar);
/*  267 */         localObject3 = Cipher.getInstance(localObjectIdentifier.toString());
/*  268 */         ((Cipher)localObject3).init(2, (Key)localObject1, localAlgorithmParameters);
/*  269 */         arrayOfByte3 = ((Cipher)localObject3).doFinal(arrayOfByte2);
/*      */       }
/*      */       catch (Exception localException2) {
/*  272 */         while (paramArrayOfChar.length == 0)
/*      */         {
/*  275 */           paramArrayOfChar = new char[1];
/*      */         }
/*      */ 
/*  278 */         throw localException2;
/*      */       }
/*      */ 
/*  282 */       localObject2 = new PKCS8EncodedKeySpec(arrayOfByte3);
/*      */ 
/*  288 */       localObject3 = new DerValue(arrayOfByte3);
/*  289 */       DerInputStream localDerInputStream = ((DerValue)localObject3).toDerInputStream();
/*  290 */       int i = localDerInputStream.getInteger();
/*  291 */       DerValue[] arrayOfDerValue = localDerInputStream.getSequence(2);
/*  292 */       AlgorithmId localAlgorithmId = new AlgorithmId(arrayOfDerValue[0].getOID());
/*  293 */       String str = localAlgorithmId.getName();
/*      */ 
/*  295 */       KeyFactory localKeyFactory = KeyFactory.getInstance(str);
/*  296 */       localPrivateKey = localKeyFactory.generatePrivate((KeySpec)localObject2);
/*      */     } catch (Exception localException1) {
/*  298 */       Object localObject2 = new UnrecoverableKeyException("Get Key failed: " + localException1.getMessage());
/*      */ 
/*  301 */       ((UnrecoverableKeyException)localObject2).initCause(localException1);
/*  302 */       throw ((Throwable)localObject2);
/*      */     }
/*  304 */     return localPrivateKey;
/*      */   }
/*      */ 
/*      */   public Certificate[] engineGetCertificateChain(String paramString)
/*      */   {
/*  319 */     KeyEntry localKeyEntry = (KeyEntry)this.entries.get(paramString.toLowerCase());
/*  320 */     if (localKeyEntry != null) {
/*  321 */       if (localKeyEntry.chain == null) {
/*  322 */         return null;
/*      */       }
/*  324 */       return (Certificate[])localKeyEntry.chain.clone();
/*      */     }
/*      */ 
/*  327 */     return null;
/*      */   }
/*      */ 
/*      */   public Certificate engineGetCertificate(String paramString)
/*      */   {
/*  347 */     KeyEntry localKeyEntry = (KeyEntry)this.entries.get(paramString.toLowerCase());
/*  348 */     if (localKeyEntry != null) {
/*  349 */       if (localKeyEntry.chain == null) {
/*  350 */         return null;
/*      */       }
/*  352 */       return localKeyEntry.chain[0];
/*      */     }
/*      */ 
/*  355 */     return null;
/*      */   }
/*      */ 
/*      */   public Date engineGetCreationDate(String paramString)
/*      */   {
/*  368 */     KeyEntry localKeyEntry = (KeyEntry)this.entries.get(paramString.toLowerCase());
/*  369 */     if (localKeyEntry != null) {
/*  370 */       return new Date(localKeyEntry.date.getTime());
/*      */     }
/*  372 */     return null;
/*      */   }
/*      */ 
/*      */   public synchronized void engineSetKeyEntry(String paramString, Key paramKey, char[] paramArrayOfChar, Certificate[] paramArrayOfCertificate)
/*      */     throws KeyStoreException
/*      */   {
/*      */     try
/*      */     {
/*  403 */       KeyEntry localKeyEntry = new KeyEntry(null);
/*  404 */       localKeyEntry.date = new Date();
/*      */ 
/*  406 */       if ((paramKey instanceof PrivateKey)) {
/*  407 */         if ((paramKey.getFormat().equals("PKCS#8")) || (paramKey.getFormat().equals("PKCS8")))
/*      */         {
/*  410 */           localKeyEntry.protectedPrivKey = encryptPrivateKey(paramKey.getEncoded(), paramArrayOfChar);
/*      */         }
/*      */         else {
/*  413 */           throw new KeyStoreException("Private key is not encodedas PKCS#8");
/*      */         }
/*      */       }
/*      */       else {
/*  417 */         throw new KeyStoreException("Key is not a PrivateKey");
/*      */       }
/*      */ 
/*  421 */       if (paramArrayOfCertificate != null)
/*      */       {
/*  423 */         if ((paramArrayOfCertificate.length > 1) && (!validateChain(paramArrayOfCertificate))) {
/*  424 */           throw new KeyStoreException("Certificate chain is not validate");
/*      */         }
/*  426 */         localKeyEntry.chain = ((Certificate[])paramArrayOfCertificate.clone());
/*      */       }
/*      */ 
/*  430 */       localKeyEntry.keyId = ("Time " + localKeyEntry.date.getTime()).getBytes("UTF8");
/*      */ 
/*  432 */       localKeyEntry.alias = paramString.toLowerCase();
/*      */ 
/*  435 */       this.entries.put(paramString.toLowerCase(), localKeyEntry);
/*      */     } catch (Exception localException) {
/*  437 */       KeyStoreException localKeyStoreException = new KeyStoreException("Key protection  algorithm not found: " + localException);
/*      */ 
/*  439 */       localKeyStoreException.initCause(localException);
/*  440 */       throw localKeyStoreException;
/*      */     }
/*      */   }
/*      */ 
/*      */   public synchronized void engineSetKeyEntry(String paramString, byte[] paramArrayOfByte, Certificate[] paramArrayOfCertificate)
/*      */     throws KeyStoreException
/*      */   {
/*      */     try
/*      */     {
/*  474 */       new EncryptedPrivateKeyInfo(paramArrayOfByte);
/*      */     } catch (IOException localIOException) {
/*  476 */       KeyStoreException localKeyStoreException = new KeyStoreException("Private key is not stored as PKCS#8 EncryptedPrivateKeyInfo: " + localIOException);
/*      */ 
/*  478 */       localKeyStoreException.initCause(localIOException);
/*  479 */       throw localKeyStoreException;
/*      */     }
/*      */ 
/*  482 */     KeyEntry localKeyEntry = new KeyEntry(null);
/*  483 */     localKeyEntry.date = new Date();
/*      */     try
/*      */     {
/*  487 */       localKeyEntry.keyId = ("Time " + localKeyEntry.date.getTime()).getBytes("UTF8");
/*      */     }
/*      */     catch (UnsupportedEncodingException localUnsupportedEncodingException)
/*      */     {
/*      */     }
/*  492 */     localKeyEntry.alias = paramString.toLowerCase();
/*      */ 
/*  494 */     localKeyEntry.protectedPrivKey = ((byte[])paramArrayOfByte.clone());
/*  495 */     if (paramArrayOfCertificate != null)
/*      */     {
/*  497 */       if ((paramArrayOfCertificate.length > 1) && (!validateChain(paramArrayOfCertificate))) {
/*  498 */         throw new KeyStoreException("Certificate chain is not valid");
/*      */       }
/*      */ 
/*  501 */       localKeyEntry.chain = ((Certificate[])paramArrayOfCertificate.clone());
/*      */     }
/*      */ 
/*  505 */     this.entries.put(paramString.toLowerCase(), localKeyEntry);
/*      */   }
/*      */ 
/*      */   private byte[] getSalt()
/*      */   {
/*  515 */     byte[] arrayOfByte = new byte[20];
/*  516 */     if (this.random == null) {
/*  517 */       this.random = new SecureRandom();
/*      */     }
/*  519 */     this.random.nextBytes(arrayOfByte);
/*  520 */     return arrayOfByte;
/*      */   }
/*      */ 
/*      */   private AlgorithmParameters getAlgorithmParameters(String paramString)
/*      */     throws IOException
/*      */   {
/*  529 */     AlgorithmParameters localAlgorithmParameters = null;
/*      */ 
/*  532 */     PBEParameterSpec localPBEParameterSpec = new PBEParameterSpec(getSalt(), 1024);
/*      */     try
/*      */     {
/*  535 */       localAlgorithmParameters = AlgorithmParameters.getInstance(paramString);
/*  536 */       localAlgorithmParameters.init(localPBEParameterSpec);
/*      */     } catch (Exception localException) {
/*  538 */       IOException localIOException = new IOException("getAlgorithmParameters failed: " + localException.getMessage());
/*      */ 
/*  541 */       localIOException.initCause(localException);
/*  542 */       throw localIOException;
/*      */     }
/*  544 */     return localAlgorithmParameters;
/*      */   }
/*      */ 
/*      */   private AlgorithmParameters parseAlgParameters(DerInputStream paramDerInputStream)
/*      */     throws IOException
/*      */   {
/*  553 */     AlgorithmParameters localAlgorithmParameters = null;
/*      */     try
/*      */     {
/*      */       DerValue localDerValue;
/*  556 */       if (paramDerInputStream.available() == 0) {
/*  557 */         localDerValue = null;
/*      */       } else {
/*  559 */         localDerValue = paramDerInputStream.getDerValue();
/*  560 */         if (localDerValue.tag == 5) {
/*  561 */           localDerValue = null;
/*      */         }
/*      */       }
/*  564 */       if (localDerValue != null) {
/*  565 */         localAlgorithmParameters = AlgorithmParameters.getInstance("PBE");
/*  566 */         localAlgorithmParameters.init(localDerValue.toByteArray());
/*      */       }
/*      */     } catch (Exception localException) {
/*  569 */       IOException localIOException = new IOException("parseAlgParameters failed: " + localException.getMessage());
/*      */ 
/*  572 */       localIOException.initCause(localException);
/*  573 */       throw localIOException;
/*      */     }
/*  575 */     return localAlgorithmParameters;
/*      */   }
/*      */ 
/*      */   private SecretKey getPBEKey(char[] paramArrayOfChar)
/*      */     throws IOException
/*      */   {
/*  583 */     SecretKey localSecretKey = null;
/*      */     try
/*      */     {
/*  586 */       PBEKeySpec localPBEKeySpec = new PBEKeySpec(paramArrayOfChar);
/*  587 */       localObject = SecretKeyFactory.getInstance("PBE");
/*  588 */       localSecretKey = ((SecretKeyFactory)localObject).generateSecret(localPBEKeySpec);
/*      */     } catch (Exception localException) {
/*  590 */       Object localObject = new IOException("getSecretKey failed: " + localException.getMessage());
/*      */ 
/*  592 */       ((IOException)localObject).initCause(localException);
/*  593 */       throw ((Throwable)localObject);
/*      */     }
/*  595 */     return localSecretKey;
/*      */   }
/*      */ 
/*      */   private byte[] encryptPrivateKey(byte[] paramArrayOfByte, char[] paramArrayOfChar)
/*      */     throws IOException, NoSuchAlgorithmException, UnrecoverableKeyException
/*      */   {
/*  610 */     byte[] arrayOfByte1 = null;
/*      */     try
/*      */     {
/*  614 */       AlgorithmParameters localAlgorithmParameters = getAlgorithmParameters("PBEWithSHA1AndDESede");
/*      */ 
/*  618 */       localObject = getPBEKey(paramArrayOfChar);
/*  619 */       Cipher localCipher = Cipher.getInstance("PBEWithSHA1AndDESede");
/*  620 */       localCipher.init(1, (Key)localObject, localAlgorithmParameters);
/*  621 */       byte[] arrayOfByte2 = localCipher.doFinal(paramArrayOfByte);
/*      */ 
/*  625 */       AlgorithmId localAlgorithmId = new AlgorithmId(pbeWithSHAAnd3KeyTripleDESCBC_OID, localAlgorithmParameters);
/*      */ 
/*  627 */       EncryptedPrivateKeyInfo localEncryptedPrivateKeyInfo = new EncryptedPrivateKeyInfo(localAlgorithmId, arrayOfByte2);
/*      */ 
/*  629 */       arrayOfByte1 = localEncryptedPrivateKeyInfo.getEncoded();
/*      */     } catch (Exception localException) {
/*  631 */       Object localObject = new UnrecoverableKeyException("Encrypt Private Key failed: " + localException.getMessage());
/*      */ 
/*  634 */       ((UnrecoverableKeyException)localObject).initCause(localException);
/*  635 */       throw ((Throwable)localObject);
/*      */     }
/*      */ 
/*  638 */     return arrayOfByte1;
/*      */   }
/*      */ 
/*      */   public synchronized void engineSetCertificateEntry(String paramString, Certificate paramCertificate)
/*      */     throws KeyStoreException
/*      */   {
/*  658 */     KeyEntry localKeyEntry = (KeyEntry)this.entries.get(paramString.toLowerCase());
/*  659 */     if (localKeyEntry != null) {
/*  660 */       throw new KeyStoreException("Cannot overwrite own certificate");
/*      */     }
/*  662 */     throw new KeyStoreException("TrustedCertEntry not supported");
/*      */   }
/*      */ 
/*      */   public synchronized void engineDeleteEntry(String paramString)
/*      */     throws KeyStoreException
/*      */   {
/*  675 */     this.entries.remove(paramString.toLowerCase());
/*      */   }
/*      */ 
/*      */   public Enumeration<String> engineAliases()
/*      */   {
/*  684 */     return this.entries.keys();
/*      */   }
/*      */ 
/*      */   public boolean engineContainsAlias(String paramString)
/*      */   {
/*  695 */     return this.entries.containsKey(paramString.toLowerCase());
/*      */   }
/*      */ 
/*      */   public int engineSize()
/*      */   {
/*  704 */     return this.entries.size();
/*      */   }
/*      */ 
/*      */   public boolean engineIsKeyEntry(String paramString)
/*      */   {
/*  715 */     KeyEntry localKeyEntry = (KeyEntry)this.entries.get(paramString.toLowerCase());
/*  716 */     if (localKeyEntry != null) {
/*  717 */       return true;
/*      */     }
/*  719 */     return false;
/*      */   }
/*      */ 
/*      */   public boolean engineIsCertificateEntry(String paramString)
/*      */   {
/*  732 */     return false;
/*      */   }
/*      */ 
/*      */   public String engineGetCertificateAlias(Certificate paramCertificate)
/*      */   {
/*  752 */     Certificate localCertificate = null;
/*      */ 
/*  754 */     for (Enumeration localEnumeration = this.entries.keys(); localEnumeration.hasMoreElements(); ) {
/*  755 */       String str = (String)localEnumeration.nextElement();
/*  756 */       KeyEntry localKeyEntry = (KeyEntry)this.entries.get(str);
/*  757 */       if (localKeyEntry.chain != null) {
/*  758 */         localCertificate = localKeyEntry.chain[0];
/*      */       }
/*  760 */       if (localCertificate.equals(paramCertificate)) {
/*  761 */         return str;
/*      */       }
/*      */     }
/*  764 */     return null;
/*      */   }
/*      */ 
/*      */   public synchronized void engineStore(OutputStream paramOutputStream, char[] paramArrayOfChar)
/*      */     throws IOException, NoSuchAlgorithmException, CertificateException
/*      */   {
/*  784 */     if (paramArrayOfChar == null) {
/*  785 */       throw new IllegalArgumentException("password can't be null");
/*      */     }
/*      */ 
/*  789 */     DerOutputStream localDerOutputStream1 = new DerOutputStream();
/*      */ 
/*  792 */     DerOutputStream localDerOutputStream2 = new DerOutputStream();
/*  793 */     localDerOutputStream2.putInteger(3);
/*  794 */     byte[] arrayOfByte1 = localDerOutputStream2.toByteArray();
/*  795 */     localDerOutputStream1.write(arrayOfByte1);
/*      */ 
/*  798 */     DerOutputStream localDerOutputStream3 = new DerOutputStream();
/*      */ 
/*  801 */     DerOutputStream localDerOutputStream4 = new DerOutputStream();
/*      */ 
/*  804 */     byte[] arrayOfByte2 = createSafeContent();
/*  805 */     ContentInfo localContentInfo1 = new ContentInfo(arrayOfByte2);
/*  806 */     localContentInfo1.encode(localDerOutputStream4);
/*      */ 
/*  809 */     byte[] arrayOfByte3 = createEncryptedData(paramArrayOfChar);
/*  810 */     ContentInfo localContentInfo2 = new ContentInfo(ContentInfo.ENCRYPTED_DATA_OID, new DerValue(arrayOfByte3));
/*      */ 
/*  813 */     localContentInfo2.encode(localDerOutputStream4);
/*      */ 
/*  816 */     DerOutputStream localDerOutputStream5 = new DerOutputStream();
/*  817 */     localDerOutputStream5.write((byte)48, localDerOutputStream4);
/*  818 */     byte[] arrayOfByte4 = localDerOutputStream5.toByteArray();
/*      */ 
/*  821 */     ContentInfo localContentInfo3 = new ContentInfo(arrayOfByte4);
/*  822 */     localContentInfo3.encode(localDerOutputStream3);
/*  823 */     byte[] arrayOfByte5 = localDerOutputStream3.toByteArray();
/*  824 */     localDerOutputStream1.write(arrayOfByte5);
/*      */ 
/*  827 */     byte[] arrayOfByte6 = calculateMac(paramArrayOfChar, arrayOfByte4);
/*  828 */     localDerOutputStream1.write(arrayOfByte6);
/*      */ 
/*  831 */     DerOutputStream localDerOutputStream6 = new DerOutputStream();
/*  832 */     localDerOutputStream6.write((byte)48, localDerOutputStream1);
/*  833 */     byte[] arrayOfByte7 = localDerOutputStream6.toByteArray();
/*  834 */     paramOutputStream.write(arrayOfByte7);
/*  835 */     paramOutputStream.flush();
/*      */   }
/*      */ 
/*      */   private byte[] generateHash(byte[] paramArrayOfByte)
/*      */     throws IOException
/*      */   {
/*  844 */     byte[] arrayOfByte = null;
/*      */     try
/*      */     {
/*  847 */       MessageDigest localMessageDigest = MessageDigest.getInstance("SHA1");
/*  848 */       localMessageDigest.update(paramArrayOfByte);
/*  849 */       arrayOfByte = localMessageDigest.digest();
/*      */     } catch (Exception localException) {
/*  851 */       IOException localIOException = new IOException("generateHash failed: " + localException);
/*  852 */       localIOException.initCause(localException);
/*  853 */       throw localIOException;
/*      */     }
/*  855 */     return arrayOfByte;
/*      */   }
/*      */ 
/*      */   private byte[] calculateMac(char[] paramArrayOfChar, byte[] paramArrayOfByte)
/*      */     throws IOException
/*      */   {
/*  868 */     byte[] arrayOfByte1 = null;
/*  869 */     String str = "SHA1";
/*      */     try
/*      */     {
/*  873 */       byte[] arrayOfByte2 = getSalt();
/*      */ 
/*  876 */       localObject = Mac.getInstance("HmacPBESHA1");
/*  877 */       PBEParameterSpec localPBEParameterSpec = new PBEParameterSpec(arrayOfByte2, 1024);
/*      */ 
/*  879 */       SecretKey localSecretKey = getPBEKey(paramArrayOfChar);
/*  880 */       ((Mac)localObject).init(localSecretKey, localPBEParameterSpec);
/*  881 */       ((Mac)localObject).update(paramArrayOfByte);
/*  882 */       byte[] arrayOfByte3 = ((Mac)localObject).doFinal();
/*      */ 
/*  885 */       MacData localMacData = new MacData(str, arrayOfByte3, arrayOfByte2, 1024);
/*      */ 
/*  887 */       DerOutputStream localDerOutputStream = new DerOutputStream();
/*  888 */       localDerOutputStream.write(localMacData.getEncoded());
/*  889 */       arrayOfByte1 = localDerOutputStream.toByteArray();
/*      */     } catch (Exception localException) {
/*  891 */       Object localObject = new IOException("calculateMac failed: " + localException);
/*  892 */       ((IOException)localObject).initCause(localException);
/*  893 */       throw ((Throwable)localObject);
/*      */     }
/*  895 */     return arrayOfByte1;
/*      */   }
/*      */ 
/*      */   private boolean validateChain(Certificate[] paramArrayOfCertificate)
/*      */   {
/*  904 */     for (int i = 0; i < paramArrayOfCertificate.length - 1; i++) {
/*  905 */       X500Principal localX500Principal1 = ((X509Certificate)paramArrayOfCertificate[i]).getIssuerX500Principal();
/*      */ 
/*  907 */       X500Principal localX500Principal2 = ((X509Certificate)paramArrayOfCertificate[(i + 1)]).getSubjectX500Principal();
/*      */ 
/*  909 */       if (!localX500Principal1.equals(localX500Principal2)) {
/*  910 */         return false;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  916 */     HashSet localHashSet = new HashSet(Arrays.asList(paramArrayOfCertificate));
/*  917 */     return localHashSet.size() == paramArrayOfCertificate.length;
/*      */   }
/*      */ 
/*      */   private byte[] getBagAttributes(String paramString, byte[] paramArrayOfByte)
/*      */     throws IOException
/*      */   {
/*  957 */     byte[] arrayOfByte1 = null;
/*  958 */     byte[] arrayOfByte2 = null;
/*      */ 
/*  961 */     if ((paramString == null) && (paramArrayOfByte == null)) {
/*  962 */       return null;
/*      */     }
/*      */ 
/*  966 */     DerOutputStream localDerOutputStream1 = new DerOutputStream();
/*      */     DerOutputStream localDerOutputStream3;
/*      */     DerOutputStream localDerOutputStream4;
/*  969 */     if (paramString != null) {
/*  970 */       localDerOutputStream2 = new DerOutputStream();
/*  971 */       localDerOutputStream2.putOID(PKCS9FriendlyName_OID);
/*  972 */       localDerOutputStream3 = new DerOutputStream();
/*  973 */       localDerOutputStream4 = new DerOutputStream();
/*  974 */       localDerOutputStream3.putBMPString(paramString);
/*  975 */       localDerOutputStream2.write((byte)49, localDerOutputStream3);
/*  976 */       localDerOutputStream4.write((byte)48, localDerOutputStream2);
/*  977 */       arrayOfByte2 = localDerOutputStream4.toByteArray();
/*      */     }
/*      */ 
/*  981 */     if (paramArrayOfByte != null) {
/*  982 */       localDerOutputStream2 = new DerOutputStream();
/*  983 */       localDerOutputStream2.putOID(PKCS9LocalKeyId_OID);
/*  984 */       localDerOutputStream3 = new DerOutputStream();
/*  985 */       localDerOutputStream4 = new DerOutputStream();
/*  986 */       localDerOutputStream3.putOctetString(paramArrayOfByte);
/*  987 */       localDerOutputStream2.write((byte)49, localDerOutputStream3);
/*  988 */       localDerOutputStream4.write((byte)48, localDerOutputStream2);
/*  989 */       arrayOfByte1 = localDerOutputStream4.toByteArray();
/*      */     }
/*      */ 
/*  992 */     DerOutputStream localDerOutputStream2 = new DerOutputStream();
/*  993 */     if (arrayOfByte2 != null) {
/*  994 */       localDerOutputStream2.write(arrayOfByte2);
/*      */     }
/*  996 */     if (arrayOfByte1 != null) {
/*  997 */       localDerOutputStream2.write(arrayOfByte1);
/*      */     }
/*  999 */     localDerOutputStream1.write((byte)49, localDerOutputStream2);
/* 1000 */     return localDerOutputStream1.toByteArray();
/*      */   }
/*      */ 
/*      */   private byte[] createEncryptedData(char[] paramArrayOfChar)
/*      */     throws CertificateException, IOException
/*      */   {
/* 1013 */     DerOutputStream localDerOutputStream1 = new DerOutputStream();
/* 1014 */     for (Object localObject1 = this.entries.keys(); ((Enumeration)localObject1).hasMoreElements(); )
/*      */     {
/* 1016 */       localObject2 = (String)((Enumeration)localObject1).nextElement();
/* 1017 */       localObject3 = (KeyEntry)this.entries.get(localObject2);
/*      */       int i;
/* 1021 */       if (((KeyEntry)localObject3).chain == null)
/* 1022 */         i = 0;
/*      */       else {
/* 1024 */         i = ((KeyEntry)localObject3).chain.length;
/*      */       }
/*      */ 
/* 1027 */       for (int j = 0; j < i; j++)
/*      */       {
/* 1029 */         DerOutputStream localDerOutputStream4 = new DerOutputStream();
/* 1030 */         localDerOutputStream4.putOID(CertBag_OID);
/*      */ 
/* 1033 */         DerOutputStream localDerOutputStream5 = new DerOutputStream();
/* 1034 */         localDerOutputStream5.putOID(PKCS9CertType_OID);
/*      */ 
/* 1037 */         DerOutputStream localDerOutputStream6 = new DerOutputStream();
/* 1038 */         X509Certificate localX509Certificate = (X509Certificate)localObject3.chain[j];
/* 1039 */         localDerOutputStream6.putOctetString(localX509Certificate.getEncoded());
/* 1040 */         localDerOutputStream5.write(DerValue.createTag((byte)-128, true, (byte)0), localDerOutputStream6);
/*      */ 
/* 1044 */         DerOutputStream localDerOutputStream7 = new DerOutputStream();
/* 1045 */         localDerOutputStream7.write((byte)48, localDerOutputStream5);
/* 1046 */         byte[] arrayOfByte1 = localDerOutputStream7.toByteArray();
/*      */ 
/* 1049 */         DerOutputStream localDerOutputStream8 = new DerOutputStream();
/* 1050 */         localDerOutputStream8.write(arrayOfByte1);
/*      */ 
/* 1052 */         localDerOutputStream4.write(DerValue.createTag((byte)-128, true, (byte)0), localDerOutputStream8);
/*      */ 
/* 1058 */         byte[] arrayOfByte2 = null;
/* 1059 */         if (j == 0)
/*      */         {
/* 1061 */           arrayOfByte2 = getBagAttributes(((KeyEntry)localObject3).alias, ((KeyEntry)localObject3).keyId);
/*      */         }
/*      */         else
/*      */         {
/* 1069 */           arrayOfByte2 = getBagAttributes(localX509Certificate.getSubjectX500Principal().getName(), null);
/*      */         }
/*      */ 
/* 1072 */         if (arrayOfByte2 != null) {
/* 1073 */           localDerOutputStream4.write(arrayOfByte2);
/*      */         }
/*      */ 
/* 1077 */         localDerOutputStream1.write((byte)48, localDerOutputStream4);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1082 */     localObject1 = new DerOutputStream();
/* 1083 */     ((DerOutputStream)localObject1).write((byte)48, localDerOutputStream1);
/* 1084 */     Object localObject2 = ((DerOutputStream)localObject1).toByteArray();
/*      */ 
/* 1087 */     Object localObject3 = encryptContent((byte[])localObject2, paramArrayOfChar);
/*      */ 
/* 1090 */     DerOutputStream localDerOutputStream2 = new DerOutputStream();
/* 1091 */     DerOutputStream localDerOutputStream3 = new DerOutputStream();
/* 1092 */     localDerOutputStream2.putInteger(0);
/* 1093 */     localDerOutputStream2.write((byte[])localObject3);
/* 1094 */     localDerOutputStream3.write((byte)48, localDerOutputStream2);
/* 1095 */     return localDerOutputStream3.toByteArray();
/*      */   }
/*      */ 
/*      */   private byte[] createSafeContent()
/*      */     throws CertificateException, IOException
/*      */   {
/* 1107 */     DerOutputStream localDerOutputStream1 = new DerOutputStream();
/* 1108 */     for (Object localObject = this.entries.keys(); ((Enumeration)localObject).hasMoreElements(); )
/*      */     {
/* 1110 */       String str = (String)((Enumeration)localObject).nextElement();
/* 1111 */       KeyEntry localKeyEntry = (KeyEntry)this.entries.get(str);
/*      */ 
/* 1114 */       DerOutputStream localDerOutputStream2 = new DerOutputStream();
/* 1115 */       localDerOutputStream2.putOID(PKCS8ShroudedKeyBag_OID);
/*      */ 
/* 1118 */       byte[] arrayOfByte1 = localKeyEntry.protectedPrivKey;
/* 1119 */       EncryptedPrivateKeyInfo localEncryptedPrivateKeyInfo = null;
/*      */       try {
/* 1121 */         localEncryptedPrivateKeyInfo = new EncryptedPrivateKeyInfo(arrayOfByte1);
/*      */       } catch (IOException localIOException) {
/* 1123 */         throw new IOException("Private key not stored as PKCS#8 EncryptedPrivateKeyInfo" + localIOException.getMessage());
/*      */       }
/*      */ 
/* 1128 */       DerOutputStream localDerOutputStream3 = new DerOutputStream();
/* 1129 */       localDerOutputStream3.write(localEncryptedPrivateKeyInfo.getEncoded());
/* 1130 */       localDerOutputStream2.write(DerValue.createTag((byte)-128, true, (byte)0), localDerOutputStream3);
/*      */ 
/* 1134 */       byte[] arrayOfByte2 = getBagAttributes(str, localKeyEntry.keyId);
/* 1135 */       localDerOutputStream2.write(arrayOfByte2);
/*      */ 
/* 1138 */       localDerOutputStream1.write((byte)48, localDerOutputStream2);
/*      */     }
/*      */ 
/* 1142 */     localObject = new DerOutputStream();
/* 1143 */     ((DerOutputStream)localObject).write((byte)48, localDerOutputStream1);
/* 1144 */     return ((DerOutputStream)localObject).toByteArray();
/*      */   }
/*      */ 
/*      */   private byte[] encryptContent(byte[] paramArrayOfByte, char[] paramArrayOfChar)
/*      */     throws IOException
/*      */   {
/* 1160 */     byte[] arrayOfByte1 = null;
/*      */ 
/* 1163 */     AlgorithmParameters localAlgorithmParameters = getAlgorithmParameters("PBEWithSHA1AndRC2_40");
/*      */ 
/* 1165 */     DerOutputStream localDerOutputStream1 = new DerOutputStream();
/* 1166 */     AlgorithmId localAlgorithmId = new AlgorithmId(pbeWithSHAAnd40BitRC2CBC_OID, localAlgorithmParameters);
/*      */ 
/* 1168 */     localAlgorithmId.encode(localDerOutputStream1);
/* 1169 */     byte[] arrayOfByte2 = localDerOutputStream1.toByteArray();
/*      */     try
/*      */     {
/* 1173 */       SecretKey localSecretKey = getPBEKey(paramArrayOfChar);
/* 1174 */       localObject = Cipher.getInstance("PBEWithSHA1AndRC2_40");
/* 1175 */       ((Cipher)localObject).init(1, localSecretKey, localAlgorithmParameters);
/* 1176 */       arrayOfByte1 = ((Cipher)localObject).doFinal(paramArrayOfByte);
/*      */     }
/*      */     catch (Exception localException) {
/* 1179 */       localObject = new IOException("Failed to encrypt safe contents entry: " + localException);
/*      */ 
/* 1181 */       ((IOException)localObject).initCause(localException);
/* 1182 */       throw ((Throwable)localObject);
/*      */     }
/*      */ 
/* 1186 */     DerOutputStream localDerOutputStream2 = new DerOutputStream();
/* 1187 */     localDerOutputStream2.putOID(ContentInfo.DATA_OID);
/* 1188 */     localDerOutputStream2.write(arrayOfByte2);
/*      */ 
/* 1191 */     Object localObject = new DerOutputStream();
/* 1192 */     ((DerOutputStream)localObject).putOctetString(arrayOfByte1);
/* 1193 */     localDerOutputStream2.writeImplicit(DerValue.createTag((byte)-128, false, (byte)0), (DerOutputStream)localObject);
/*      */ 
/* 1197 */     DerOutputStream localDerOutputStream3 = new DerOutputStream();
/* 1198 */     localDerOutputStream3.write((byte)48, localDerOutputStream2);
/* 1199 */     return localDerOutputStream3.toByteArray();
/*      */   }
/*      */ 
/*      */   public synchronized void engineLoad(InputStream paramInputStream, char[] paramArrayOfChar)
/*      */     throws IOException, NoSuchAlgorithmException, CertificateException
/*      */   {
/* 1223 */     Object localObject1 = null;
/* 1224 */     Object localObject2 = null;
/* 1225 */     Object localObject3 = null;
/*      */ 
/* 1227 */     if (paramInputStream == null) {
/* 1228 */       return;
/*      */     }
/*      */ 
/* 1231 */     this.counter = 0;
/*      */ 
/* 1233 */     DerValue localDerValue = new DerValue(paramInputStream);
/* 1234 */     DerInputStream localDerInputStream1 = localDerValue.toDerInputStream();
/* 1235 */     int i = localDerInputStream1.getInteger();
/*      */ 
/* 1237 */     if (i != 3) {
/* 1238 */       throw new IOException("PKCS12 keystore not in version 3 format");
/*      */     }
/*      */ 
/* 1241 */     this.entries.clear();
/*      */ 
/* 1247 */     ContentInfo localContentInfo = new ContentInfo(localDerInputStream1);
/* 1248 */     ObjectIdentifier localObjectIdentifier1 = localContentInfo.getContentType();
/*      */     byte[] arrayOfByte;
/* 1250 */     if (localObjectIdentifier1.equals(ContentInfo.DATA_OID))
/* 1251 */       arrayOfByte = localContentInfo.getData();
/*      */     else {
/* 1253 */       throw new IOException("public key protected PKCS12 not supported");
/*      */     }
/*      */ 
/* 1256 */     DerInputStream localDerInputStream2 = new DerInputStream(arrayOfByte);
/* 1257 */     DerValue[] arrayOfDerValue1 = localDerInputStream2.getSequence(2);
/* 1258 */     int j = arrayOfDerValue1.length;
/*      */ 
/* 1261 */     this.privateKeyCount = 0;
/*      */     Object localObject8;
/*      */     Object localObject7;
/*      */     Object localObject6;
/*      */     Object localObject5;
/*      */     Object localObject9;
/* 1266 */     for (int k = 0; k < j; k++)
/*      */     {
/* 1270 */       localObject8 = null;
/*      */ 
/* 1272 */       localObject7 = new DerInputStream(arrayOfDerValue1[k].toByteArray());
/* 1273 */       localObject6 = new ContentInfo((DerInputStream)localObject7);
/* 1274 */       localObjectIdentifier1 = ((ContentInfo)localObject6).getContentType();
/* 1275 */       localObject5 = null;
/* 1276 */       if (localObjectIdentifier1.equals(ContentInfo.DATA_OID)) {
/* 1277 */         localObject5 = ((ContentInfo)localObject6).getData();
/* 1278 */       } else if (localObjectIdentifier1.equals(ContentInfo.ENCRYPTED_DATA_OID)) {
/* 1279 */         if (paramArrayOfChar == null) {
/*      */           continue;
/*      */         }
/* 1282 */         localObject9 = ((ContentInfo)localObject6).getContent().toDerInputStream();
/*      */ 
/* 1284 */         int n = ((DerInputStream)localObject9).getInteger();
/* 1285 */         DerValue[] arrayOfDerValue2 = ((DerInputStream)localObject9).getSequence(2);
/* 1286 */         ObjectIdentifier localObjectIdentifier2 = arrayOfDerValue2[0].getOID();
/* 1287 */         localObject8 = arrayOfDerValue2[1].toByteArray();
/* 1288 */         if (!arrayOfDerValue2[2].isContextSpecific((byte)0)) {
/* 1289 */           throw new IOException("encrypted content not present!");
/*      */         }
/* 1291 */         byte b = 4;
/* 1292 */         if (arrayOfDerValue2[2].isConstructed())
/* 1293 */           b = (byte)(b | 0x20);
/* 1294 */         arrayOfDerValue2[2].resetTag(b);
/* 1295 */         localObject5 = arrayOfDerValue2[2].getOctetString();
/*      */ 
/* 1298 */         DerInputStream localDerInputStream3 = arrayOfDerValue2[1].toDerInputStream();
/* 1299 */         ObjectIdentifier localObjectIdentifier3 = localDerInputStream3.getOID();
/* 1300 */         AlgorithmParameters localAlgorithmParameters = parseAlgParameters(localDerInputStream3);
/*      */         try
/*      */         {
/* 1305 */           SecretKey localSecretKey = getPBEKey(paramArrayOfChar);
/* 1306 */           Cipher localCipher = Cipher.getInstance(localObjectIdentifier3.toString());
/* 1307 */           localCipher.init(2, localSecretKey, localAlgorithmParameters);
/* 1308 */           localObject5 = localCipher.doFinal((byte[])localObject5);
/*      */         }
/*      */         catch (Exception localException2) {
/* 1311 */           while (paramArrayOfChar.length == 0)
/*      */           {
/* 1314 */             paramArrayOfChar = new char[1];
/*      */           }
/*      */ 
/* 1317 */           throw new IOException("failed to decrypt safe contents entry: " + localException2, localException2);
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/* 1322 */         throw new IOException("public key protected PKCS12 not supported");
/*      */       }
/*      */ 
/* 1325 */       localObject9 = new DerInputStream((byte[])localObject5);
/* 1326 */       loadSafeContents((DerInputStream)localObject9, paramArrayOfChar);
/*      */     }
/*      */ 
/* 1330 */     if ((paramArrayOfChar != null) && (localDerInputStream1.available() > 0)) {
/* 1331 */       localObject4 = new MacData(localDerInputStream1);
/*      */       try {
/* 1333 */         localObject5 = ((MacData)localObject4).getDigestAlgName().toUpperCase();
/* 1334 */         if ((((String)localObject5).equals("SHA")) || (((String)localObject5).equals("SHA1")) || (((String)localObject5).equals("SHA-1")))
/*      */         {
/* 1337 */           localObject5 = "SHA1";
/*      */         }
/*      */ 
/* 1341 */         localObject6 = Mac.getInstance("HmacPBE" + (String)localObject5);
/* 1342 */         localObject7 = new PBEParameterSpec(((MacData)localObject4).getSalt(), ((MacData)localObject4).getIterations());
/*      */ 
/* 1345 */         localObject8 = getPBEKey(paramArrayOfChar);
/* 1346 */         ((Mac)localObject6).init((Key)localObject8, (AlgorithmParameterSpec)localObject7);
/* 1347 */         ((Mac)localObject6).update(arrayOfByte);
/* 1348 */         localObject9 = ((Mac)localObject6).doFinal();
/*      */ 
/* 1350 */         if (!Arrays.equals(((MacData)localObject4).getDigest(), (byte[])localObject9))
/* 1351 */           throw new SecurityException("Failed PKCS12 integrity checking");
/*      */       }
/*      */       catch (Exception localException1)
/*      */       {
/* 1355 */         localObject6 = new IOException("Integrity check failed: " + localException1);
/*      */ 
/* 1357 */         ((IOException)localObject6).initCause(localException1);
/* 1358 */         throw ((Throwable)localObject6);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1365 */     Object localObject4 = (KeyEntry[])this.keyList.toArray(new KeyEntry[this.keyList.size()]);
/* 1366 */     for (int m = 0; m < localObject4.length; m++) {
/* 1367 */       localObject6 = localObject4[m];
/* 1368 */       if (((KeyEntry)localObject6).keyId != null) {
/* 1369 */         localObject7 = new ArrayList();
/*      */ 
/* 1371 */         localObject8 = findMatchedCertificate((KeyEntry)localObject6);
/*      */ 
/* 1374 */         while (localObject8 != null)
/*      */         {
/* 1376 */           if (!((ArrayList)localObject7).isEmpty()) {
/* 1377 */             for (localObject9 = ((ArrayList)localObject7).iterator(); ((Iterator)localObject9).hasNext(); ) { X509Certificate localX509Certificate = (X509Certificate)((Iterator)localObject9).next();
/* 1378 */               if (((X509Certificate)localObject8).equals(localX509Certificate)) {
/* 1379 */                 if (debug == null) break label899;
/* 1380 */                 debug.println("Loop detected in certificate chain. Skip adding repeated cert to chain. Subject: " + ((X509Certificate)localObject8).getSubjectX500Principal().toString()); break label899;
/*      */               }
/*      */ 
/*      */             }
/*      */ 
/*      */           }
/*      */ 
/* 1390 */           ((ArrayList)localObject7).add(localObject8);
/* 1391 */           localObject9 = ((X509Certificate)localObject8).getIssuerX500Principal();
/* 1392 */           if (((X500Principal)localObject9).equals(((X509Certificate)localObject8).getSubjectX500Principal())) {
/*      */             break;
/*      */           }
/* 1395 */           localObject8 = (X509Certificate)this.certsMap.get(localObject9);
/*      */         }
/*      */ 
/* 1398 */         label899: if (((ArrayList)localObject7).size() > 0)
/* 1399 */           ((KeyEntry)localObject6).chain = ((Certificate[])((ArrayList)localObject7).toArray(new Certificate[((ArrayList)localObject7).size()]));
/*      */       }
/*      */     }
/* 1402 */     this.certEntries.clear();
/* 1403 */     this.certsMap.clear();
/* 1404 */     this.keyList.clear();
/*      */   }
/*      */ 
/*      */   private X509Certificate findMatchedCertificate(KeyEntry paramKeyEntry)
/*      */   {
/* 1413 */     Object localObject1 = null;
/* 1414 */     Object localObject2 = null;
/* 1415 */     for (CertEntry localCertEntry : this.certEntries) {
/* 1416 */       if (Arrays.equals(paramKeyEntry.keyId, localCertEntry.keyId)) {
/* 1417 */         localObject1 = localCertEntry;
/* 1418 */         if (paramKeyEntry.alias.equalsIgnoreCase(localCertEntry.alias))
/*      */         {
/* 1420 */           return localCertEntry.cert;
/*      */         }
/* 1422 */       } else if (paramKeyEntry.alias.equalsIgnoreCase(localCertEntry.alias)) {
/* 1423 */         localObject2 = localCertEntry;
/*      */       }
/*      */     }
/*      */ 
/* 1427 */     if (localObject1 != null) return localObject1.cert;
/* 1428 */     if (localObject2 != null) return localObject2.cert;
/* 1429 */     return null;
/*      */   }
/*      */ 
/*      */   private void loadSafeContents(DerInputStream paramDerInputStream, char[] paramArrayOfChar)
/*      */     throws IOException, NoSuchAlgorithmException, CertificateException
/*      */   {
/* 1435 */     DerValue[] arrayOfDerValue1 = paramDerInputStream.getSequence(2);
/* 1436 */     int i = arrayOfDerValue1.length;
/*      */ 
/* 1441 */     for (int j = 0; j < i; j++)
/*      */     {
/* 1445 */       Object localObject1 = null;
/*      */ 
/* 1447 */       DerInputStream localDerInputStream1 = arrayOfDerValue1[j].toDerInputStream();
/* 1448 */       ObjectIdentifier localObjectIdentifier1 = localDerInputStream1.getOID();
/* 1449 */       DerValue localDerValue1 = localDerInputStream1.getDerValue();
/* 1450 */       if (!localDerValue1.isContextSpecific((byte)0)) {
/* 1451 */         throw new IOException("unsupported PKCS12 bag value type " + localDerValue1.tag);
/*      */       }
/*      */ 
/* 1454 */       localDerValue1 = localDerValue1.data.getDerValue();
/*      */       Object localObject2;
/*      */       Object localObject5;
/*      */       Object localObject6;
/* 1455 */       if (localObjectIdentifier1.equals(PKCS8ShroudedKeyBag_OID)) {
/* 1456 */         localObject2 = new KeyEntry(null);
/* 1457 */         ((KeyEntry)localObject2).protectedPrivKey = localDerValue1.toByteArray();
/* 1458 */         localObject1 = localObject2;
/* 1459 */         this.privateKeyCount += 1;
/* 1460 */       } else if (localObjectIdentifier1.equals(CertBag_OID)) {
/* 1461 */         localObject2 = new DerInputStream(localDerValue1.toByteArray());
/* 1462 */         DerValue[] arrayOfDerValue2 = ((DerInputStream)localObject2).getSequence(2);
/* 1463 */         localObject3 = arrayOfDerValue2[0].getOID();
/* 1464 */         if (!arrayOfDerValue2[1].isContextSpecific((byte)0)) {
/* 1465 */           throw new IOException("unsupported PKCS12 cert value type " + arrayOfDerValue2[1].tag);
/*      */         }
/*      */ 
/* 1468 */         DerValue localDerValue2 = arrayOfDerValue2[1].data.getDerValue();
/* 1469 */         localObject5 = CertificateFactory.getInstance("X509");
/*      */ 
/* 1471 */         localObject6 = (X509Certificate)((CertificateFactory)localObject5).generateCertificate(new ByteArrayInputStream(localDerValue2.getOctetString()));
/*      */ 
/* 1473 */         localObject1 = localObject6;
/*      */       }
/*      */ 
/*      */       try
/*      */       {
/* 1480 */         localObject2 = localDerInputStream1.getSet(2);
/*      */       }
/*      */       catch (IOException localIOException1)
/*      */       {
/* 1485 */         localObject2 = null;
/*      */       }
/*      */ 
/* 1488 */       String str = null;
/* 1489 */       Object localObject3 = null;
/*      */ 
/* 1491 */       if (localObject2 != null)
/* 1492 */         for (int k = 0; k < localObject2.length; k++) { localObject5 = new DerInputStream(localObject2[k].toByteArray());
/*      */ 
/* 1495 */           localObject6 = ((DerInputStream)localObject5).getSequence(2);
/* 1496 */           ObjectIdentifier localObjectIdentifier2 = localObject6[0].getOID();
/* 1497 */           DerInputStream localDerInputStream2 = new DerInputStream(localObject6[1].toByteArray());
/*      */           DerValue[] arrayOfDerValue3;
/*      */           try { arrayOfDerValue3 = localDerInputStream2.getSet(1);
/*      */           } catch (IOException localIOException2) {
/* 1503 */             throw new IOException("Attribute " + localObjectIdentifier2 + " should have a value " + localIOException2.getMessage());
/*      */           }
/*      */ 
/* 1506 */           if (localObjectIdentifier2.equals(PKCS9FriendlyName_OID))
/* 1507 */             str = arrayOfDerValue3[0].getBMPString();
/* 1508 */           else if (localObjectIdentifier2.equals(PKCS9LocalKeyId_OID))
/* 1509 */             localObject3 = arrayOfDerValue3[0].getOctetString();
/*      */         }
/*      */       Object localObject4;
/* 1525 */       if ((localObject1 instanceof KeyEntry)) {
/* 1526 */         localObject4 = (KeyEntry)localObject1;
/* 1527 */         if (localObject3 == null)
/*      */         {
/* 1532 */           if (this.privateKeyCount == 1) {
/* 1533 */             localObject3 = "01".getBytes("UTF8");
/*      */           }
/*      */         }
/*      */         else
/*      */         {
/* 1538 */           ((KeyEntry)localObject4).keyId = ((byte[])localObject3);
/*      */ 
/* 1540 */           localObject5 = new String((byte[])localObject3, "UTF8");
/* 1541 */           localObject6 = null;
/* 1542 */           if (((String)localObject5).startsWith("Time ")) {
/*      */             try {
/* 1544 */               localObject6 = new Date(Long.parseLong(((String)localObject5).substring(5)));
/*      */             }
/*      */             catch (Exception localException) {
/* 1547 */               localObject6 = null;
/*      */             }
/*      */           }
/* 1550 */           if (localObject6 == null) {
/* 1551 */             localObject6 = new Date();
/*      */           }
/* 1553 */           ((KeyEntry)localObject4).date = ((Date)localObject6);
/* 1554 */           this.keyList.add(localObject4);
/* 1555 */           if (str == null)
/* 1556 */             str = getUnfriendlyName();
/* 1557 */           ((KeyEntry)localObject4).alias = str;
/* 1558 */           this.entries.put(str.toLowerCase(), localObject4);
/*      */         } } else if ((localObject1 instanceof X509Certificate)) {
/* 1560 */         localObject4 = (X509Certificate)localObject1;
/*      */ 
/* 1565 */         if ((localObject3 == null) && (this.privateKeyCount == 1))
/*      */         {
/* 1567 */           if (j == 0) {
/* 1568 */             localObject3 = "01".getBytes("UTF8");
/*      */           }
/*      */         }
/* 1571 */         this.certEntries.add(new CertEntry((X509Certificate)localObject4, (byte[])localObject3, str));
/* 1572 */         localObject5 = ((X509Certificate)localObject4).getSubjectX500Principal();
/* 1573 */         if ((localObject5 != null) && 
/* 1574 */           (!this.certsMap.containsKey(localObject5)))
/* 1575 */           this.certsMap.put(localObject5, localObject4);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private String getUnfriendlyName()
/*      */   {
/* 1583 */     this.counter += 1;
/* 1584 */     return String.valueOf(this.counter);
/*      */   }
/*      */ 
/*      */   static
/*      */   {
/*      */     try
/*      */     {
/*  163 */       PKCS8ShroudedKeyBag_OID = new ObjectIdentifier(keyBag);
/*  164 */       CertBag_OID = new ObjectIdentifier(certBag);
/*  165 */       PKCS9FriendlyName_OID = new ObjectIdentifier(pkcs9Name);
/*  166 */       PKCS9LocalKeyId_OID = new ObjectIdentifier(pkcs9KeyId);
/*  167 */       PKCS9CertType_OID = new ObjectIdentifier(pkcs9certType);
/*  168 */       pbeWithSHAAnd40BitRC2CBC_OID = new ObjectIdentifier(pbeWithSHAAnd40BitRC2CBC);
/*      */ 
/*  170 */       pbeWithSHAAnd3KeyTripleDESCBC_OID = new ObjectIdentifier(pbeWithSHAAnd3KeyTripleDESCBC);
/*      */     }
/*      */     catch (IOException localIOException)
/*      */     {
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class CertEntry
/*      */   {
/*      */     final X509Certificate cert;
/*      */     final byte[] keyId;
/*      */     final String alias;
/*      */ 
/*      */     CertEntry(X509Certificate paramX509Certificate, byte[] paramArrayOfByte, String paramString)
/*      */     {
/*  192 */       this.cert = paramX509Certificate;
/*  193 */       this.keyId = paramArrayOfByte;
/*  194 */       this.alias = paramString;
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class KeyEntry
/*      */   {
/*      */     Date date;
/*      */     byte[] protectedPrivKey;
/*      */     Certificate[] chain;
/*      */     byte[] keyId;
/*      */     String alias;
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.pkcs12.PKCS12KeyStore
 * JD-Core Version:    0.6.2
 */