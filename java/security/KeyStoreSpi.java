/*     */ package java.security;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.security.cert.Certificate;
/*     */ import java.security.cert.CertificateException;
/*     */ import java.util.Date;
/*     */ import java.util.Enumeration;
/*     */ import javax.crypto.SecretKey;
/*     */ import javax.security.auth.callback.Callback;
/*     */ import javax.security.auth.callback.CallbackHandler;
/*     */ import javax.security.auth.callback.PasswordCallback;
/*     */ import javax.security.auth.callback.UnsupportedCallbackException;
/*     */ 
/*     */ public abstract class KeyStoreSpi
/*     */ {
/*     */   public abstract Key engineGetKey(String paramString, char[] paramArrayOfChar)
/*     */     throws NoSuchAlgorithmException, UnrecoverableKeyException;
/*     */ 
/*     */   public abstract Certificate[] engineGetCertificateChain(String paramString);
/*     */ 
/*     */   public abstract Certificate engineGetCertificate(String paramString);
/*     */ 
/*     */   public abstract Date engineGetCreationDate(String paramString);
/*     */ 
/*     */   public abstract void engineSetKeyEntry(String paramString, Key paramKey, char[] paramArrayOfChar, Certificate[] paramArrayOfCertificate)
/*     */     throws KeyStoreException;
/*     */ 
/*     */   public abstract void engineSetKeyEntry(String paramString, byte[] paramArrayOfByte, Certificate[] paramArrayOfCertificate)
/*     */     throws KeyStoreException;
/*     */ 
/*     */   public abstract void engineSetCertificateEntry(String paramString, Certificate paramCertificate)
/*     */     throws KeyStoreException;
/*     */ 
/*     */   public abstract void engineDeleteEntry(String paramString)
/*     */     throws KeyStoreException;
/*     */ 
/*     */   public abstract Enumeration<String> engineAliases();
/*     */ 
/*     */   public abstract boolean engineContainsAlias(String paramString);
/*     */ 
/*     */   public abstract int engineSize();
/*     */ 
/*     */   public abstract boolean engineIsKeyEntry(String paramString);
/*     */ 
/*     */   public abstract boolean engineIsCertificateEntry(String paramString);
/*     */ 
/*     */   public abstract String engineGetCertificateAlias(Certificate paramCertificate);
/*     */ 
/*     */   public abstract void engineStore(OutputStream paramOutputStream, char[] paramArrayOfChar)
/*     */     throws IOException, NoSuchAlgorithmException, CertificateException;
/*     */ 
/*     */   public void engineStore(KeyStore.LoadStoreParameter paramLoadStoreParameter)
/*     */     throws IOException, NoSuchAlgorithmException, CertificateException
/*     */   {
/* 320 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public abstract void engineLoad(InputStream paramInputStream, char[] paramArrayOfChar)
/*     */     throws IOException, NoSuchAlgorithmException, CertificateException;
/*     */ 
/*     */   public void engineLoad(KeyStore.LoadStoreParameter paramLoadStoreParameter)
/*     */     throws IOException, NoSuchAlgorithmException, CertificateException
/*     */   {
/* 383 */     if (paramLoadStoreParameter == null) {
/* 384 */       engineLoad((InputStream)null, (char[])null);
/* 385 */       return;
/*     */     }
/*     */ 
/* 388 */     if ((paramLoadStoreParameter instanceof KeyStore.SimpleLoadStoreParameter)) {
/* 389 */       KeyStore.ProtectionParameter localProtectionParameter = paramLoadStoreParameter.getProtectionParameter();
/*     */       char[] arrayOfChar;
/* 391 */       if ((localProtectionParameter instanceof KeyStore.PasswordProtection)) {
/* 392 */         arrayOfChar = ((KeyStore.PasswordProtection)localProtectionParameter).getPassword();
/* 393 */       } else if ((localProtectionParameter instanceof KeyStore.CallbackHandlerProtection)) {
/* 394 */         CallbackHandler localCallbackHandler = ((KeyStore.CallbackHandlerProtection)localProtectionParameter).getCallbackHandler();
/*     */ 
/* 396 */         PasswordCallback localPasswordCallback = new PasswordCallback("Password: ", false);
/*     */         try
/*     */         {
/* 399 */           localCallbackHandler.handle(new Callback[] { localPasswordCallback });
/*     */         } catch (UnsupportedCallbackException localUnsupportedCallbackException) {
/* 401 */           throw new NoSuchAlgorithmException("Could not obtain password", localUnsupportedCallbackException);
/*     */         }
/*     */ 
/* 404 */         arrayOfChar = localPasswordCallback.getPassword();
/* 405 */         localPasswordCallback.clearPassword();
/* 406 */         if (arrayOfChar == null)
/* 407 */           throw new NoSuchAlgorithmException("No password provided");
/*     */       }
/*     */       else
/*     */       {
/* 411 */         throw new NoSuchAlgorithmException("ProtectionParameter must be PasswordProtection or CallbackHandlerProtection");
/*     */       }
/*     */ 
/* 414 */       engineLoad(null, arrayOfChar);
/* 415 */       return;
/*     */     }
/*     */ 
/* 418 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public KeyStore.Entry engineGetEntry(String paramString, KeyStore.ProtectionParameter paramProtectionParameter)
/*     */     throws KeyStoreException, NoSuchAlgorithmException, UnrecoverableEntryException
/*     */   {
/* 450 */     if (!engineContainsAlias(paramString)) {
/* 451 */       return null;
/*     */     }
/*     */ 
/* 454 */     if (paramProtectionParameter == null) {
/* 455 */       if (engineIsCertificateEntry(paramString)) {
/* 456 */         return new KeyStore.TrustedCertificateEntry(engineGetCertificate(paramString));
/*     */       }
/*     */ 
/* 459 */       throw new UnrecoverableKeyException("requested entry requires a password");
/*     */     }
/*     */ 
/* 464 */     if ((paramProtectionParameter instanceof KeyStore.PasswordProtection)) {
/* 465 */       if (engineIsCertificateEntry(paramString)) {
/* 466 */         throw new UnsupportedOperationException("trusted certificate entries are not password-protected");
/*     */       }
/* 468 */       if (engineIsKeyEntry(paramString)) {
/* 469 */         KeyStore.PasswordProtection localPasswordProtection = (KeyStore.PasswordProtection)paramProtectionParameter;
/*     */ 
/* 471 */         char[] arrayOfChar = localPasswordProtection.getPassword();
/*     */ 
/* 473 */         Key localKey = engineGetKey(paramString, arrayOfChar);
/* 474 */         if ((localKey instanceof PrivateKey)) {
/* 475 */           Certificate[] arrayOfCertificate = engineGetCertificateChain(paramString);
/* 476 */           return new KeyStore.PrivateKeyEntry((PrivateKey)localKey, arrayOfCertificate);
/* 477 */         }if ((localKey instanceof SecretKey)) {
/* 478 */           return new KeyStore.SecretKeyEntry((SecretKey)localKey);
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 483 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public void engineSetEntry(String paramString, KeyStore.Entry paramEntry, KeyStore.ProtectionParameter paramProtectionParameter)
/*     */     throws KeyStoreException
/*     */   {
/* 509 */     if ((paramProtectionParameter != null) && (!(paramProtectionParameter instanceof KeyStore.PasswordProtection)))
/*     */     {
/* 511 */       throw new KeyStoreException("unsupported protection parameter");
/*     */     }
/* 513 */     KeyStore.PasswordProtection localPasswordProtection = null;
/* 514 */     if (paramProtectionParameter != null) {
/* 515 */       localPasswordProtection = (KeyStore.PasswordProtection)paramProtectionParameter;
/*     */     }
/*     */ 
/* 519 */     if ((paramEntry instanceof KeyStore.TrustedCertificateEntry)) {
/* 520 */       if ((paramProtectionParameter != null) && (localPasswordProtection.getPassword() != null))
/*     */       {
/* 522 */         throw new KeyStoreException("trusted certificate entries are not password-protected");
/*     */       }
/*     */ 
/* 525 */       KeyStore.TrustedCertificateEntry localTrustedCertificateEntry = (KeyStore.TrustedCertificateEntry)paramEntry;
/*     */ 
/* 527 */       engineSetCertificateEntry(paramString, localTrustedCertificateEntry.getTrustedCertificate());
/* 528 */       return;
/*     */     }
/* 530 */     if ((paramEntry instanceof KeyStore.PrivateKeyEntry)) {
/* 531 */       if ((localPasswordProtection == null) || (localPasswordProtection.getPassword() == null))
/*     */       {
/* 533 */         throw new KeyStoreException("non-null password required to create PrivateKeyEntry");
/*     */       }
/*     */ 
/* 536 */       engineSetKeyEntry(paramString, ((KeyStore.PrivateKeyEntry)paramEntry).getPrivateKey(), localPasswordProtection.getPassword(), ((KeyStore.PrivateKeyEntry)paramEntry).getCertificateChain());
/*     */ 
/* 541 */       return;
/*     */     }
/* 543 */     if ((paramEntry instanceof KeyStore.SecretKeyEntry)) {
/* 544 */       if ((localPasswordProtection == null) || (localPasswordProtection.getPassword() == null))
/*     */       {
/* 546 */         throw new KeyStoreException("non-null password required to create SecretKeyEntry");
/*     */       }
/*     */ 
/* 549 */       engineSetKeyEntry(paramString, ((KeyStore.SecretKeyEntry)paramEntry).getSecretKey(), localPasswordProtection.getPassword(), (Certificate[])null);
/*     */ 
/* 554 */       return;
/*     */     }
/*     */ 
/* 558 */     throw new KeyStoreException("unsupported entry type: " + paramEntry.getClass().getName());
/*     */   }
/*     */ 
/*     */   public boolean engineEntryInstanceOf(String paramString, Class<? extends KeyStore.Entry> paramClass)
/*     */   {
/* 580 */     if (paramClass == KeyStore.TrustedCertificateEntry.class) {
/* 581 */       return engineIsCertificateEntry(paramString);
/*     */     }
/* 583 */     if (paramClass == KeyStore.PrivateKeyEntry.class) {
/* 584 */       return (engineIsKeyEntry(paramString)) && (engineGetCertificate(paramString) != null);
/*     */     }
/*     */ 
/* 587 */     if (paramClass == KeyStore.SecretKeyEntry.class) {
/* 588 */       return (engineIsKeyEntry(paramString)) && (engineGetCertificate(paramString) == null);
/*     */     }
/*     */ 
/* 591 */     return false;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.security.KeyStoreSpi
 * JD-Core Version:    0.6.2
 */