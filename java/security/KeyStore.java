/*      */ package java.security;
/*      */ 
/*      */ import java.io.File;
/*      */ import java.io.FileInputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.OutputStream;
/*      */ import java.security.cert.Certificate;
/*      */ import java.security.cert.CertificateException;
/*      */ import java.security.cert.X509Certificate;
/*      */ import java.util.Arrays;
/*      */ import java.util.Date;
/*      */ import java.util.Enumeration;
/*      */ import javax.crypto.SecretKey;
/*      */ import javax.security.auth.DestroyFailedException;
/*      */ import javax.security.auth.Destroyable;
/*      */ import javax.security.auth.callback.Callback;
/*      */ import javax.security.auth.callback.CallbackHandler;
/*      */ import javax.security.auth.callback.PasswordCallback;
/*      */ 
/*      */ public class KeyStore
/*      */ {
/*      */   private static final String KEYSTORE_TYPE = "keystore.type";
/*      */   private String type;
/*      */   private Provider provider;
/*      */   private KeyStoreSpi keyStoreSpi;
/*  210 */   private boolean initialized = false;
/*      */ 
/*      */   protected KeyStore(KeyStoreSpi paramKeyStoreSpi, Provider paramProvider, String paramString)
/*      */   {
/*  578 */     this.keyStoreSpi = paramKeyStoreSpi;
/*  579 */     this.provider = paramProvider;
/*  580 */     this.type = paramString;
/*      */   }
/*      */ 
/*      */   public static KeyStore getInstance(String paramString)
/*      */     throws KeyStoreException
/*      */   {
/*      */     try
/*      */     {
/*  613 */       Object[] arrayOfObject = Security.getImpl(paramString, "KeyStore", (String)null);
/*  614 */       return new KeyStore((KeyStoreSpi)arrayOfObject[0], (Provider)arrayOfObject[1], paramString);
/*      */     } catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {
/*  616 */       throw new KeyStoreException(paramString + " not found", localNoSuchAlgorithmException);
/*      */     } catch (NoSuchProviderException localNoSuchProviderException) {
/*  618 */       throw new KeyStoreException(paramString + " not found", localNoSuchProviderException);
/*      */     }
/*      */   }
/*      */ 
/*      */   public static KeyStore getInstance(String paramString1, String paramString2)
/*      */     throws KeyStoreException, NoSuchProviderException
/*      */   {
/*  658 */     if ((paramString2 == null) || (paramString2.length() == 0))
/*  659 */       throw new IllegalArgumentException("missing provider");
/*      */     try {
/*  661 */       Object[] arrayOfObject = Security.getImpl(paramString1, "KeyStore", paramString2);
/*  662 */       return new KeyStore((KeyStoreSpi)arrayOfObject[0], (Provider)arrayOfObject[1], paramString1);
/*      */     } catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {
/*  664 */       throw new KeyStoreException(paramString1 + " not found", localNoSuchAlgorithmException);
/*      */     }
/*      */   }
/*      */ 
/*      */   public static KeyStore getInstance(String paramString, Provider paramProvider)
/*      */     throws KeyStoreException
/*      */   {
/*  699 */     if (paramProvider == null)
/*  700 */       throw new IllegalArgumentException("missing provider");
/*      */     try {
/*  702 */       Object[] arrayOfObject = Security.getImpl(paramString, "KeyStore", paramProvider);
/*  703 */       return new KeyStore((KeyStoreSpi)arrayOfObject[0], (Provider)arrayOfObject[1], paramString);
/*      */     } catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {
/*  705 */       throw new KeyStoreException(paramString + " not found", localNoSuchAlgorithmException);
/*      */     }
/*      */   }
/*      */ 
/*      */   public static final String getDefaultType()
/*      */   {
/*  734 */     String str = (String)AccessController.doPrivileged(new PrivilegedAction() {
/*      */       public String run() {
/*  736 */         return Security.getProperty("keystore.type");
/*      */       }
/*      */     });
/*  739 */     if (str == null) {
/*  740 */       str = "jks";
/*      */     }
/*  742 */     return str;
/*      */   }
/*      */ 
/*      */   public final Provider getProvider()
/*      */   {
/*  752 */     return this.provider;
/*      */   }
/*      */ 
/*      */   public final String getType()
/*      */   {
/*  762 */     return this.type;
/*      */   }
/*      */ 
/*      */   public final Key getKey(String paramString, char[] paramArrayOfChar)
/*      */     throws KeyStoreException, NoSuchAlgorithmException, UnrecoverableKeyException
/*      */   {
/*  789 */     if (!this.initialized) {
/*  790 */       throw new KeyStoreException("Uninitialized keystore");
/*      */     }
/*  792 */     return this.keyStoreSpi.engineGetKey(paramString, paramArrayOfChar);
/*      */   }
/*      */ 
/*      */   public final Certificate[] getCertificateChain(String paramString)
/*      */     throws KeyStoreException
/*      */   {
/*  814 */     if (!this.initialized) {
/*  815 */       throw new KeyStoreException("Uninitialized keystore");
/*      */     }
/*  817 */     return this.keyStoreSpi.engineGetCertificateChain(paramString);
/*      */   }
/*      */ 
/*      */   public final Certificate getCertificate(String paramString)
/*      */     throws KeyStoreException
/*      */   {
/*  847 */     if (!this.initialized) {
/*  848 */       throw new KeyStoreException("Uninitialized keystore");
/*      */     }
/*  850 */     return this.keyStoreSpi.engineGetCertificate(paramString);
/*      */   }
/*      */ 
/*      */   public final Date getCreationDate(String paramString)
/*      */     throws KeyStoreException
/*      */   {
/*  867 */     if (!this.initialized) {
/*  868 */       throw new KeyStoreException("Uninitialized keystore");
/*      */     }
/*  870 */     return this.keyStoreSpi.engineGetCreationDate(paramString);
/*      */   }
/*      */ 
/*      */   public final void setKeyEntry(String paramString, Key paramKey, char[] paramArrayOfChar, Certificate[] paramArrayOfCertificate)
/*      */     throws KeyStoreException
/*      */   {
/*  900 */     if (!this.initialized) {
/*  901 */       throw new KeyStoreException("Uninitialized keystore");
/*      */     }
/*  903 */     if (((paramKey instanceof PrivateKey)) && ((paramArrayOfCertificate == null) || (paramArrayOfCertificate.length == 0)))
/*      */     {
/*  905 */       throw new IllegalArgumentException("Private key must be accompanied by certificate chain");
/*      */     }
/*      */ 
/*  909 */     this.keyStoreSpi.engineSetKeyEntry(paramString, paramKey, paramArrayOfChar, paramArrayOfCertificate);
/*      */   }
/*      */ 
/*      */   public final void setKeyEntry(String paramString, byte[] paramArrayOfByte, Certificate[] paramArrayOfCertificate)
/*      */     throws KeyStoreException
/*      */   {
/*  940 */     if (!this.initialized) {
/*  941 */       throw new KeyStoreException("Uninitialized keystore");
/*      */     }
/*  943 */     this.keyStoreSpi.engineSetKeyEntry(paramString, paramArrayOfByte, paramArrayOfCertificate);
/*      */   }
/*      */ 
/*      */   public final void setCertificateEntry(String paramString, Certificate paramCertificate)
/*      */     throws KeyStoreException
/*      */   {
/*  967 */     if (!this.initialized) {
/*  968 */       throw new KeyStoreException("Uninitialized keystore");
/*      */     }
/*  970 */     this.keyStoreSpi.engineSetCertificateEntry(paramString, paramCertificate);
/*      */   }
/*      */ 
/*      */   public final void deleteEntry(String paramString)
/*      */     throws KeyStoreException
/*      */   {
/*  984 */     if (!this.initialized) {
/*  985 */       throw new KeyStoreException("Uninitialized keystore");
/*      */     }
/*  987 */     this.keyStoreSpi.engineDeleteEntry(paramString);
/*      */   }
/*      */ 
/*      */   public final Enumeration<String> aliases()
/*      */     throws KeyStoreException
/*      */   {
/* 1001 */     if (!this.initialized) {
/* 1002 */       throw new KeyStoreException("Uninitialized keystore");
/*      */     }
/* 1004 */     return this.keyStoreSpi.engineAliases();
/*      */   }
/*      */ 
/*      */   public final boolean containsAlias(String paramString)
/*      */     throws KeyStoreException
/*      */   {
/* 1020 */     if (!this.initialized) {
/* 1021 */       throw new KeyStoreException("Uninitialized keystore");
/*      */     }
/* 1023 */     return this.keyStoreSpi.engineContainsAlias(paramString);
/*      */   }
/*      */ 
/*      */   public final int size()
/*      */     throws KeyStoreException
/*      */   {
/* 1037 */     if (!this.initialized) {
/* 1038 */       throw new KeyStoreException("Uninitialized keystore");
/*      */     }
/* 1040 */     return this.keyStoreSpi.engineSize();
/*      */   }
/*      */ 
/*      */   public final boolean isKeyEntry(String paramString)
/*      */     throws KeyStoreException
/*      */   {
/* 1060 */     if (!this.initialized) {
/* 1061 */       throw new KeyStoreException("Uninitialized keystore");
/*      */     }
/* 1063 */     return this.keyStoreSpi.engineIsKeyEntry(paramString);
/*      */   }
/*      */ 
/*      */   public final boolean isCertificateEntry(String paramString)
/*      */     throws KeyStoreException
/*      */   {
/* 1083 */     if (!this.initialized) {
/* 1084 */       throw new KeyStoreException("Uninitialized keystore");
/*      */     }
/* 1086 */     return this.keyStoreSpi.engineIsCertificateEntry(paramString);
/*      */   }
/*      */ 
/*      */   public final String getCertificateAlias(Certificate paramCertificate)
/*      */     throws KeyStoreException
/*      */   {
/* 1118 */     if (!this.initialized) {
/* 1119 */       throw new KeyStoreException("Uninitialized keystore");
/*      */     }
/* 1121 */     return this.keyStoreSpi.engineGetCertificateAlias(paramCertificate);
/*      */   }
/*      */ 
/*      */   public final void store(OutputStream paramOutputStream, char[] paramArrayOfChar)
/*      */     throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException
/*      */   {
/* 1143 */     if (!this.initialized) {
/* 1144 */       throw new KeyStoreException("Uninitialized keystore");
/*      */     }
/* 1146 */     this.keyStoreSpi.engineStore(paramOutputStream, paramArrayOfChar);
/*      */   }
/*      */ 
/*      */   public final void store(LoadStoreParameter paramLoadStoreParameter)
/*      */     throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException
/*      */   {
/* 1172 */     if (!this.initialized) {
/* 1173 */       throw new KeyStoreException("Uninitialized keystore");
/*      */     }
/* 1175 */     this.keyStoreSpi.engineStore(paramLoadStoreParameter);
/*      */   }
/*      */ 
/*      */   public final void load(InputStream paramInputStream, char[] paramArrayOfChar)
/*      */     throws IOException, NoSuchAlgorithmException, CertificateException
/*      */   {
/* 1214 */     this.keyStoreSpi.engineLoad(paramInputStream, paramArrayOfChar);
/* 1215 */     this.initialized = true;
/*      */   }
/*      */ 
/*      */   public final void load(LoadStoreParameter paramLoadStoreParameter)
/*      */     throws IOException, NoSuchAlgorithmException, CertificateException
/*      */   {
/* 1248 */     this.keyStoreSpi.engineLoad(paramLoadStoreParameter);
/* 1249 */     this.initialized = true;
/*      */   }
/*      */ 
/*      */   public final Entry getEntry(String paramString, ProtectionParameter paramProtectionParameter)
/*      */     throws NoSuchAlgorithmException, UnrecoverableEntryException, KeyStoreException
/*      */   {
/* 1284 */     if (paramString == null) {
/* 1285 */       throw new NullPointerException("invalid null input");
/*      */     }
/* 1287 */     if (!this.initialized) {
/* 1288 */       throw new KeyStoreException("Uninitialized keystore");
/*      */     }
/* 1290 */     return this.keyStoreSpi.engineGetEntry(paramString, paramProtectionParameter);
/*      */   }
/*      */ 
/*      */   public final void setEntry(String paramString, Entry paramEntry, ProtectionParameter paramProtectionParameter)
/*      */     throws KeyStoreException
/*      */   {
/* 1320 */     if ((paramString == null) || (paramEntry == null)) {
/* 1321 */       throw new NullPointerException("invalid null input");
/*      */     }
/* 1323 */     if (!this.initialized) {
/* 1324 */       throw new KeyStoreException("Uninitialized keystore");
/*      */     }
/* 1326 */     this.keyStoreSpi.engineSetEntry(paramString, paramEntry, paramProtectionParameter);
/*      */   }
/*      */ 
/*      */   public final boolean entryInstanceOf(String paramString, Class<? extends Entry> paramClass)
/*      */     throws KeyStoreException
/*      */   {
/* 1355 */     if ((paramString == null) || (paramClass == null)) {
/* 1356 */       throw new NullPointerException("invalid null input");
/*      */     }
/* 1358 */     if (!this.initialized) {
/* 1359 */       throw new KeyStoreException("Uninitialized keystore");
/*      */     }
/* 1361 */     return this.keyStoreSpi.engineEntryInstanceOf(paramString, paramClass);
/*      */   }
/*      */ 
/*      */   public static abstract class Builder
/*      */   {
/*      */     static final int MAX_CALLBACK_TRIES = 3;
/*      */ 
/*      */     public abstract KeyStore getKeyStore()
/*      */       throws KeyStoreException;
/*      */ 
/*      */     public abstract KeyStore.ProtectionParameter getProtectionParameter(String paramString)
/*      */       throws KeyStoreException;
/*      */ 
/*      */     public static Builder newInstance(KeyStore paramKeyStore, final KeyStore.ProtectionParameter paramProtectionParameter)
/*      */     {
/* 1439 */       if ((paramKeyStore == null) || (paramProtectionParameter == null)) {
/* 1440 */         throw new NullPointerException();
/*      */       }
/* 1442 */       if (!paramKeyStore.initialized) {
/* 1443 */         throw new IllegalArgumentException("KeyStore not initialized");
/*      */       }
/* 1445 */       return new Builder() {
/*      */         private volatile boolean getCalled;
/*      */ 
/*      */         public KeyStore getKeyStore() {
/* 1449 */           this.getCalled = true;
/* 1450 */           return this.val$keyStore;
/*      */         }
/*      */ 
/*      */         public KeyStore.ProtectionParameter getProtectionParameter(String paramAnonymousString)
/*      */         {
/* 1455 */           if (paramAnonymousString == null) {
/* 1456 */             throw new NullPointerException();
/*      */           }
/* 1458 */           if (!this.getCalled) {
/* 1459 */             throw new IllegalStateException("getKeyStore() must be called first");
/*      */           }
/*      */ 
/* 1462 */           return paramProtectionParameter;
/*      */         }
/*      */       };
/*      */     }
/*      */ 
/*      */     public static Builder newInstance(String paramString, Provider paramProvider, File paramFile, KeyStore.ProtectionParameter paramProtectionParameter)
/*      */     {
/* 1512 */       if ((paramString == null) || (paramFile == null) || (paramProtectionParameter == null)) {
/* 1513 */         throw new NullPointerException();
/*      */       }
/* 1515 */       if ((!(paramProtectionParameter instanceof KeyStore.PasswordProtection)) && (!(paramProtectionParameter instanceof KeyStore.CallbackHandlerProtection)))
/*      */       {
/* 1517 */         throw new IllegalArgumentException("Protection must be PasswordProtection or CallbackHandlerProtection");
/*      */       }
/*      */ 
/* 1521 */       if (!paramFile.isFile()) {
/* 1522 */         throw new IllegalArgumentException("File does not exist or it does not refer to a normal file: " + paramFile);
/*      */       }
/*      */ 
/* 1526 */       return new FileBuilder(paramString, paramProvider, paramFile, paramProtectionParameter, AccessController.getContext());
/*      */     }
/*      */ 
/*      */     public static Builder newInstance(final String paramString, Provider paramProvider, final KeyStore.ProtectionParameter paramProtectionParameter)
/*      */     {
/* 1677 */       if ((paramString == null) || (paramProtectionParameter == null)) {
/* 1678 */         throw new NullPointerException();
/*      */       }
/* 1680 */       final AccessControlContext localAccessControlContext = AccessController.getContext();
/* 1681 */       return new Builder()
/*      */       {
/*      */         private volatile boolean getCalled;
/*      */         private IOException oldException;
/* 1685 */         private final PrivilegedExceptionAction<KeyStore> action = new PrivilegedExceptionAction()
/*      */         {
/*      */           public KeyStore run()
/*      */             throws Exception
/*      */           {
/*      */             KeyStore localKeyStore;
/* 1690 */             if (KeyStore.Builder.2.this.val$provider == null)
/* 1691 */               localKeyStore = KeyStore.getInstance(KeyStore.Builder.2.this.val$type);
/*      */             else {
/* 1693 */               localKeyStore = KeyStore.getInstance(KeyStore.Builder.2.this.val$type, KeyStore.Builder.2.this.val$provider);
/*      */             }
/* 1695 */             KeyStore.SimpleLoadStoreParameter localSimpleLoadStoreParameter = new KeyStore.SimpleLoadStoreParameter(KeyStore.Builder.2.this.val$protection);
/* 1696 */             if (!(KeyStore.Builder.2.this.val$protection instanceof KeyStore.CallbackHandlerProtection)) {
/* 1697 */               localKeyStore.load(localSimpleLoadStoreParameter);
/*      */             }
/*      */             else
/*      */             {
/* 1701 */               int i = 0;
/*      */               while (true) {
/* 1703 */                 i++;
/*      */                 try {
/* 1705 */                   localKeyStore.load(localSimpleLoadStoreParameter);
/*      */                 }
/*      */                 catch (IOException localIOException) {
/* 1708 */                   if ((localIOException.getCause() instanceof UnrecoverableKeyException)) {
/* 1709 */                     if (i >= 3)
/*      */                     {
/* 1712 */                       KeyStore.Builder.2.this.oldException = localIOException;
/*      */                     }
/*      */                   }
/* 1715 */                   else throw localIOException;
/*      */                 }
/*      */               }
/*      */             }
/* 1719 */             KeyStore.Builder.2.this.getCalled = true;
/* 1720 */             return localKeyStore;
/*      */           }
/* 1685 */         };
/*      */ 
/*      */         public synchronized KeyStore getKeyStore()
/*      */           throws KeyStoreException
/*      */         {
/* 1726 */           if (this.oldException != null) {
/* 1727 */             throw new KeyStoreException("Previous KeyStore instantiation failed", this.oldException);
/*      */           }
/*      */ 
/*      */           try
/*      */           {
/* 1732 */             return (KeyStore)AccessController.doPrivileged(this.action, localAccessControlContext);
/*      */           } catch (PrivilegedActionException localPrivilegedActionException) {
/* 1734 */             Throwable localThrowable = localPrivilegedActionException.getCause();
/* 1735 */             throw new KeyStoreException("KeyStore instantiation failed", localThrowable);
/*      */           }
/*      */         }
/*      */ 
/*      */         public KeyStore.ProtectionParameter getProtectionParameter(String paramAnonymousString)
/*      */         {
/* 1742 */           if (paramAnonymousString == null) {
/* 1743 */             throw new NullPointerException();
/*      */           }
/* 1745 */           if (!this.getCalled) {
/* 1746 */             throw new IllegalStateException("getKeyStore() must be called first");
/*      */           }
/*      */ 
/* 1749 */           return paramProtectionParameter;
/*      */         }
/*      */       };
/*      */     }
/*      */ 
/*      */     private static final class FileBuilder extends KeyStore.Builder
/*      */     {
/*      */       private final String type;
/*      */       private final Provider provider;
/*      */       private final File file;
/*      */       private KeyStore.ProtectionParameter protection;
/*      */       private KeyStore.ProtectionParameter keyProtection;
/*      */       private final AccessControlContext context;
/*      */       private KeyStore keyStore;
/*      */       private Throwable oldException;
/*      */ 
/*      */       FileBuilder(String paramString, Provider paramProvider, File paramFile, KeyStore.ProtectionParameter paramProtectionParameter, AccessControlContext paramAccessControlContext)
/*      */       {
/* 1546 */         this.type = paramString;
/* 1547 */         this.provider = paramProvider;
/* 1548 */         this.file = paramFile;
/* 1549 */         this.protection = paramProtectionParameter;
/* 1550 */         this.context = paramAccessControlContext;
/*      */       }
/*      */ 
/*      */       public synchronized KeyStore getKeyStore() throws KeyStoreException
/*      */       {
/* 1555 */         if (this.keyStore != null) {
/* 1556 */           return this.keyStore;
/*      */         }
/* 1558 */         if (this.oldException != null) {
/* 1559 */           throw new KeyStoreException("Previous KeyStore instantiation failed", this.oldException);
/*      */         }
/*      */ 
/* 1563 */         PrivilegedExceptionAction local1 = new PrivilegedExceptionAction()
/*      */         {
/*      */           public KeyStore run() throws Exception {
/* 1566 */             if (!(KeyStore.Builder.FileBuilder.this.protection instanceof KeyStore.CallbackHandlerProtection)) {
/* 1567 */               return run0();
/*      */             }
/*      */ 
/* 1571 */             int i = 0;
/*      */             do {
/* 1573 */               i++;
/*      */               try {
/* 1575 */                 return run0();
/*      */               } catch (IOException localIOException) {
/* 1577 */                 if (i >= 3) break;  }  } while ((localIOException.getCause() instanceof UnrecoverableKeyException));
/*      */ 
/* 1581 */             throw localIOException;
/*      */           }
/*      */ 
/*      */           public KeyStore run0()
/*      */             throws Exception
/*      */           {
/*      */             KeyStore localKeyStore;
/* 1587 */             if (KeyStore.Builder.FileBuilder.this.provider == null)
/* 1588 */               localKeyStore = KeyStore.getInstance(KeyStore.Builder.FileBuilder.this.type);
/*      */             else {
/* 1590 */               localKeyStore = KeyStore.getInstance(KeyStore.Builder.FileBuilder.this.type, KeyStore.Builder.FileBuilder.this.provider);
/*      */             }
/* 1592 */             FileInputStream localFileInputStream = null;
/* 1593 */             char[] arrayOfChar = null;
/*      */             try {
/* 1595 */               localFileInputStream = new FileInputStream(KeyStore.Builder.FileBuilder.this.file);
/*      */               Object localObject1;
/* 1596 */               if ((KeyStore.Builder.FileBuilder.this.protection instanceof KeyStore.PasswordProtection)) {
/* 1597 */                 arrayOfChar = ((KeyStore.PasswordProtection)KeyStore.Builder.FileBuilder.this.protection).getPassword();
/*      */ 
/* 1599 */                 KeyStore.Builder.FileBuilder.this.keyProtection = KeyStore.Builder.FileBuilder.this.protection;
/*      */               } else {
/* 1601 */                 localObject1 = ((KeyStore.CallbackHandlerProtection)KeyStore.Builder.FileBuilder.this.protection).getCallbackHandler();
/*      */ 
/* 1604 */                 PasswordCallback localPasswordCallback = new PasswordCallback("Password for keystore " + KeyStore.Builder.FileBuilder.this.file.getName(), false);
/*      */ 
/* 1607 */                 ((CallbackHandler)localObject1).handle(new Callback[] { localPasswordCallback });
/* 1608 */                 arrayOfChar = localPasswordCallback.getPassword();
/* 1609 */                 if (arrayOfChar == null) {
/* 1610 */                   throw new KeyStoreException("No password provided");
/*      */                 }
/*      */ 
/* 1613 */                 localPasswordCallback.clearPassword();
/* 1614 */                 KeyStore.Builder.FileBuilder.this.keyProtection = new KeyStore.PasswordProtection(arrayOfChar);
/*      */               }
/* 1616 */               localKeyStore.load(localFileInputStream, arrayOfChar);
/* 1617 */               return localKeyStore;
/*      */             } finally {
/* 1619 */               if (localFileInputStream != null)
/* 1620 */                 localFileInputStream.close();
/*      */             }
/*      */           }
/*      */         };
/*      */         try
/*      */         {
/* 1626 */           this.keyStore = ((KeyStore)AccessController.doPrivileged(local1, this.context));
/* 1627 */           return this.keyStore;
/*      */         } catch (PrivilegedActionException localPrivilegedActionException) {
/* 1629 */           this.oldException = localPrivilegedActionException.getCause();
/* 1630 */         }throw new KeyStoreException("KeyStore instantiation failed", this.oldException);
/*      */       }
/*      */ 
/*      */       public synchronized KeyStore.ProtectionParameter getProtectionParameter(String paramString)
/*      */       {
/* 1637 */         if (paramString == null) {
/* 1638 */           throw new NullPointerException();
/*      */         }
/* 1640 */         if (this.keyStore == null) {
/* 1641 */           throw new IllegalStateException("getKeyStore() must be called first");
/*      */         }
/*      */ 
/* 1644 */         return this.keyProtection;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class CallbackHandlerProtection
/*      */     implements KeyStore.ProtectionParameter
/*      */   {
/*      */     private final CallbackHandler handler;
/*      */ 
/*      */     public CallbackHandlerProtection(CallbackHandler paramCallbackHandler)
/*      */     {
/*  329 */       if (paramCallbackHandler == null) {
/*  330 */         throw new NullPointerException("handler must not be null");
/*      */       }
/*  332 */       this.handler = paramCallbackHandler;
/*      */     }
/*      */ 
/*      */     public CallbackHandler getCallbackHandler()
/*      */     {
/*  341 */       return this.handler;
/*      */     }
/*      */   }
/*      */ 
/*      */   public static abstract interface Entry
/*      */   {
/*      */   }
/*      */ 
/*      */   public static abstract interface LoadStoreParameter
/*      */   {
/*      */     public abstract KeyStore.ProtectionParameter getProtectionParameter();
/*      */   }
/*      */ 
/*      */   public static class PasswordProtection
/*      */     implements KeyStore.ProtectionParameter, Destroyable
/*      */   {
/*      */     private final char[] password;
/*  253 */     private volatile boolean destroyed = false;
/*      */ 
/*      */     public PasswordProtection(char[] paramArrayOfChar)
/*      */     {
/*  264 */       this.password = (paramArrayOfChar == null ? null : (char[])paramArrayOfChar.clone());
/*      */     }
/*      */ 
/*      */     public synchronized char[] getPassword()
/*      */     {
/*  281 */       if (this.destroyed) {
/*  282 */         throw new IllegalStateException("password has been cleared");
/*      */       }
/*  284 */       return this.password;
/*      */     }
/*      */ 
/*      */     public synchronized void destroy()
/*      */       throws DestroyFailedException
/*      */     {
/*  295 */       this.destroyed = true;
/*  296 */       if (this.password != null)
/*  297 */         Arrays.fill(this.password, ' ');
/*      */     }
/*      */ 
/*      */     public synchronized boolean isDestroyed()
/*      */     {
/*  307 */       return this.destroyed;
/*      */     }
/*      */   }
/*      */ 
/*      */   public static final class PrivateKeyEntry
/*      */     implements KeyStore.Entry
/*      */   {
/*      */     private final PrivateKey privKey;
/*      */     private final Certificate[] chain;
/*      */ 
/*      */     public PrivateKeyEntry(PrivateKey paramPrivateKey, Certificate[] paramArrayOfCertificate)
/*      */     {
/*  389 */       if ((paramPrivateKey == null) || (paramArrayOfCertificate == null)) {
/*  390 */         throw new NullPointerException("invalid null input");
/*      */       }
/*  392 */       if (paramArrayOfCertificate.length == 0) {
/*  393 */         throw new IllegalArgumentException("invalid zero-length input chain");
/*      */       }
/*      */ 
/*  397 */       Certificate[] arrayOfCertificate = (Certificate[])paramArrayOfCertificate.clone();
/*  398 */       String str = arrayOfCertificate[0].getType();
/*  399 */       for (int i = 1; i < arrayOfCertificate.length; i++) {
/*  400 */         if (!str.equals(arrayOfCertificate[i].getType())) {
/*  401 */           throw new IllegalArgumentException("chain does not contain certificates of the same type");
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  406 */       if (!paramPrivateKey.getAlgorithm().equals(arrayOfCertificate[0].getPublicKey().getAlgorithm()))
/*      */       {
/*  408 */         throw new IllegalArgumentException("private key algorithm does not match algorithm of public key in end entity certificate (at index 0)");
/*      */       }
/*      */ 
/*  413 */       this.privKey = paramPrivateKey;
/*      */ 
/*  415 */       if (((arrayOfCertificate[0] instanceof X509Certificate)) && (!(arrayOfCertificate instanceof X509Certificate[])))
/*      */       {
/*  418 */         this.chain = new X509Certificate[arrayOfCertificate.length];
/*  419 */         System.arraycopy(arrayOfCertificate, 0, this.chain, 0, arrayOfCertificate.length);
/*      */       }
/*      */       else {
/*  422 */         this.chain = arrayOfCertificate;
/*      */       }
/*      */     }
/*      */ 
/*      */     public PrivateKey getPrivateKey()
/*      */     {
/*  432 */       return this.privKey;
/*      */     }
/*      */ 
/*      */     public Certificate[] getCertificateChain()
/*      */     {
/*  447 */       return (Certificate[])this.chain.clone();
/*      */     }
/*      */ 
/*      */     public Certificate getCertificate()
/*      */     {
/*  461 */       return this.chain[0];
/*      */     }
/*      */ 
/*      */     public String toString()
/*      */     {
/*  469 */       StringBuilder localStringBuilder = new StringBuilder();
/*  470 */       localStringBuilder.append("Private key entry and certificate chain with " + this.chain.length + " elements:\r\n");
/*      */ 
/*  472 */       for (Certificate localCertificate : this.chain) {
/*  473 */         localStringBuilder.append(localCertificate);
/*  474 */         localStringBuilder.append("\r\n");
/*      */       }
/*  476 */       return localStringBuilder.toString();
/*      */     }
/*      */   }
/*      */ 
/*      */   public static abstract interface ProtectionParameter
/*      */   {
/*      */   }
/*      */ 
/*      */   public static final class SecretKeyEntry
/*      */     implements KeyStore.Entry
/*      */   {
/*      */     private final SecretKey sKey;
/*      */ 
/*      */     public SecretKeyEntry(SecretKey paramSecretKey)
/*      */     {
/*  500 */       if (paramSecretKey == null) {
/*  501 */         throw new NullPointerException("invalid null input");
/*      */       }
/*  503 */       this.sKey = paramSecretKey;
/*      */     }
/*      */ 
/*      */     public SecretKey getSecretKey()
/*      */     {
/*  512 */       return this.sKey;
/*      */     }
/*      */ 
/*      */     public String toString()
/*      */     {
/*  520 */       return "Secret key entry with algorithm " + this.sKey.getAlgorithm();
/*      */     }
/*      */   }
/*      */ 
/*      */   static class SimpleLoadStoreParameter
/*      */     implements KeyStore.LoadStoreParameter
/*      */   {
/*      */     private final KeyStore.ProtectionParameter protection;
/*      */ 
/*      */     SimpleLoadStoreParameter(KeyStore.ProtectionParameter paramProtectionParameter)
/*      */     {
/* 1761 */       this.protection = paramProtectionParameter;
/*      */     }
/*      */ 
/*      */     public KeyStore.ProtectionParameter getProtectionParameter() {
/* 1765 */       return this.protection;
/*      */     }
/*      */   }
/*      */ 
/*      */   public static final class TrustedCertificateEntry
/*      */     implements KeyStore.Entry
/*      */   {
/*      */     private final Certificate cert;
/*      */ 
/*      */     public TrustedCertificateEntry(Certificate paramCertificate)
/*      */     {
/*  544 */       if (paramCertificate == null) {
/*  545 */         throw new NullPointerException("invalid null input");
/*      */       }
/*  547 */       this.cert = paramCertificate;
/*      */     }
/*      */ 
/*      */     public Certificate getTrustedCertificate()
/*      */     {
/*  556 */       return this.cert;
/*      */     }
/*      */ 
/*      */     public String toString()
/*      */     {
/*  564 */       return "Trusted certificate entry:\r\n" + this.cert.toString();
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.security.KeyStore
 * JD-Core Version:    0.6.2
 */