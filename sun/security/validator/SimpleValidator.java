/*     */ package sun.security.validator;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.security.AlgorithmConstraints;
/*     */ import java.security.GeneralSecurityException;
/*     */ import java.security.PublicKey;
/*     */ import java.security.cert.CertPathValidatorException;
/*     */ import java.security.cert.CertificateException;
/*     */ import java.security.cert.TrustAnchor;
/*     */ import java.security.cert.X509Certificate;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.security.auth.x500.X500Principal;
/*     */ import sun.security.provider.certpath.AlgorithmChecker;
/*     */ import sun.security.provider.certpath.UntrustedChecker;
/*     */ import sun.security.util.BitArray;
/*     */ import sun.security.util.DerInputStream;
/*     */ import sun.security.util.DerValue;
/*     */ import sun.security.util.ObjectIdentifier;
/*     */ import sun.security.x509.NetscapeCertTypeExtension;
/*     */ import sun.security.x509.X509CertImpl;
/*     */ 
/*     */ public final class SimpleValidator extends Validator
/*     */ {
/*     */   static final String OID_BASIC_CONSTRAINTS = "2.5.29.19";
/*     */   static final String OID_NETSCAPE_CERT_TYPE = "2.16.840.1.113730.1.1";
/*     */   static final String OID_KEY_USAGE = "2.5.29.15";
/*     */   static final String OID_EXTENDED_KEY_USAGE = "2.5.29.37";
/*     */   static final String OID_EKU_ANY_USAGE = "2.5.29.37.0";
/*  73 */   static final ObjectIdentifier OBJID_NETSCAPE_CERT_TYPE = NetscapeCertTypeExtension.NetscapeCertType_Id;
/*     */   private static final String NSCT_SSL_CA = "ssl_ca";
/*     */   private static final String NSCT_CODE_SIGNING_CA = "object_signing_ca";
/*     */   private final Map<X500Principal, List<X509Certificate>> trustedX500Principals;
/*     */   private final Collection<X509Certificate> trustedCerts;
/*     */ 
/*     */   SimpleValidator(String paramString, Collection<X509Certificate> paramCollection)
/*     */   {
/*  98 */     super("Simple", paramString);
/*  99 */     this.trustedCerts = paramCollection;
/* 100 */     this.trustedX500Principals = new HashMap();
/*     */ 
/* 102 */     for (X509Certificate localX509Certificate : paramCollection) {
/* 103 */       X500Principal localX500Principal = localX509Certificate.getSubjectX500Principal();
/* 104 */       Object localObject = (List)this.trustedX500Principals.get(localX500Principal);
/* 105 */       if (localObject == null)
/*     */       {
/* 108 */         localObject = new ArrayList(2);
/* 109 */         this.trustedX500Principals.put(localX500Principal, localObject);
/*     */       }
/* 111 */       ((List)localObject).add(localX509Certificate);
/*     */     }
/*     */   }
/*     */ 
/*     */   public Collection<X509Certificate> getTrustedCertificates() {
/* 116 */     return this.trustedCerts;
/*     */   }
/*     */ 
/*     */   X509Certificate[] engineValidate(X509Certificate[] paramArrayOfX509Certificate, Collection<X509Certificate> paramCollection, AlgorithmConstraints paramAlgorithmConstraints, Object paramObject)
/*     */     throws CertificateException
/*     */   {
/* 128 */     if ((paramArrayOfX509Certificate == null) || (paramArrayOfX509Certificate.length == 0)) {
/* 129 */       throw new CertificateException("null or zero-length certificate chain");
/*     */     }
/*     */ 
/* 134 */     paramArrayOfX509Certificate = buildTrustedChain(paramArrayOfX509Certificate);
/*     */ 
/* 136 */     Date localDate = this.validationDate;
/* 137 */     if (localDate == null) {
/* 138 */       localDate = new Date();
/*     */     }
/*     */ 
/* 142 */     UntrustedChecker localUntrustedChecker = new UntrustedChecker();
/*     */ 
/* 145 */     TrustAnchor localTrustAnchor = new TrustAnchor(paramArrayOfX509Certificate[(paramArrayOfX509Certificate.length - 1)], null);
/* 146 */     AlgorithmChecker localAlgorithmChecker1 = new AlgorithmChecker(localTrustAnchor);
/*     */ 
/* 149 */     AlgorithmChecker localAlgorithmChecker2 = null;
/* 150 */     if (paramAlgorithmConstraints != null) {
/* 151 */       localAlgorithmChecker2 = new AlgorithmChecker(localTrustAnchor, paramAlgorithmConstraints);
/*     */     }
/*     */ 
/* 156 */     int i = paramArrayOfX509Certificate.length - 1;
/* 157 */     for (int j = paramArrayOfX509Certificate.length - 2; j >= 0; j--) {
/* 158 */       X509Certificate localX509Certificate1 = paramArrayOfX509Certificate[(j + 1)];
/* 159 */       X509Certificate localX509Certificate2 = paramArrayOfX509Certificate[j];
/*     */       try
/*     */       {
/* 165 */         localUntrustedChecker.check(localX509Certificate2, Collections.emptySet());
/*     */       } catch (CertPathValidatorException localCertPathValidatorException1) {
/* 167 */         throw new ValidatorException("Untrusted certificate: " + localX509Certificate2.getSubjectX500Principal(), ValidatorException.T_UNTRUSTED_CERT, localX509Certificate2, localCertPathValidatorException1);
/*     */       }
/*     */ 
/*     */       try
/*     */       {
/* 176 */         localAlgorithmChecker1.check(localX509Certificate2, Collections.emptySet());
/* 177 */         if (localAlgorithmChecker2 != null)
/* 178 */           localAlgorithmChecker2.check(localX509Certificate2, Collections.emptySet());
/*     */       }
/*     */       catch (CertPathValidatorException localCertPathValidatorException2) {
/* 181 */         throw new ValidatorException(ValidatorException.T_ALGORITHM_DISABLED, localX509Certificate2, localCertPathValidatorException2);
/*     */       }
/*     */ 
/* 186 */       if ((!this.variant.equals("code signing")) && (!this.variant.equals("jce signing")))
/*     */       {
/* 188 */         localX509Certificate2.checkValidity(localDate);
/*     */       }
/*     */ 
/* 192 */       if (!localX509Certificate2.getIssuerX500Principal().equals(localX509Certificate1.getSubjectX500Principal()))
/*     */       {
/* 194 */         throw new ValidatorException(ValidatorException.T_NAME_CHAINING, localX509Certificate2);
/*     */       }
/*     */ 
/*     */       try
/*     */       {
/* 200 */         localX509Certificate2.verify(localX509Certificate1.getPublicKey());
/*     */       } catch (GeneralSecurityException localGeneralSecurityException) {
/* 202 */         throw new ValidatorException(ValidatorException.T_SIGNATURE_ERROR, localX509Certificate2, localGeneralSecurityException);
/*     */       }
/*     */ 
/* 207 */       if (j != 0) {
/* 208 */         i = checkExtensions(localX509Certificate2, i);
/*     */       }
/*     */     }
/*     */ 
/* 212 */     return paramArrayOfX509Certificate;
/*     */   }
/*     */ 
/*     */   private int checkExtensions(X509Certificate paramX509Certificate, int paramInt) throws CertificateException
/*     */   {
/* 217 */     Set localSet = paramX509Certificate.getCriticalExtensionOIDs();
/* 218 */     if (localSet == null) {
/* 219 */       localSet = Collections.emptySet();
/*     */     }
/*     */ 
/* 223 */     int i = checkBasicConstraints(paramX509Certificate, localSet, paramInt);
/*     */ 
/* 227 */     checkKeyUsage(paramX509Certificate, localSet);
/*     */ 
/* 230 */     checkNetscapeCertType(paramX509Certificate, localSet);
/*     */ 
/* 232 */     if (!localSet.isEmpty()) {
/* 233 */       throw new ValidatorException("Certificate contains unknown critical extensions: " + localSet, ValidatorException.T_CA_EXTENSIONS, paramX509Certificate);
/*     */     }
/*     */ 
/* 238 */     return i;
/*     */   }
/*     */ 
/*     */   private void checkNetscapeCertType(X509Certificate paramX509Certificate, Set<String> paramSet) throws CertificateException
/*     */   {
/* 243 */     if (!this.variant.equals("generic"))
/*     */     {
/* 245 */       if ((this.variant.equals("tls client")) || (this.variant.equals("tls server")))
/*     */       {
/* 247 */         if (!getNetscapeCertTypeBit(paramX509Certificate, "ssl_ca")) {
/* 248 */           throw new ValidatorException("Invalid Netscape CertType extension for SSL CA certificate", ValidatorException.T_CA_EXTENSIONS, paramX509Certificate);
/*     */         }
/*     */ 
/* 253 */         paramSet.remove("2.16.840.1.113730.1.1");
/* 254 */       } else if ((this.variant.equals("code signing")) || (this.variant.equals("jce signing")))
/*     */       {
/* 256 */         if (!getNetscapeCertTypeBit(paramX509Certificate, "object_signing_ca")) {
/* 257 */           throw new ValidatorException("Invalid Netscape CertType extension for code signing CA certificate", ValidatorException.T_CA_EXTENSIONS, paramX509Certificate);
/*     */         }
/*     */ 
/* 262 */         paramSet.remove("2.16.840.1.113730.1.1");
/*     */       } else {
/* 264 */         throw new CertificateException("Unknown variant " + this.variant);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   static boolean getNetscapeCertTypeBit(X509Certificate paramX509Certificate, String paramString)
/*     */   {
/*     */     try
/*     */     {
/*     */       Object localObject2;
/*     */       NetscapeCertTypeExtension localNetscapeCertTypeExtension;
/* 275 */       if ((paramX509Certificate instanceof X509CertImpl)) {
/* 276 */         localObject1 = (X509CertImpl)paramX509Certificate;
/* 277 */         localObject2 = OBJID_NETSCAPE_CERT_TYPE;
/* 278 */         localNetscapeCertTypeExtension = (NetscapeCertTypeExtension)((X509CertImpl)localObject1).getExtension((ObjectIdentifier)localObject2);
/* 279 */         if (localNetscapeCertTypeExtension == null)
/* 280 */           return true;
/*     */       }
/*     */       else {
/* 283 */         localObject1 = paramX509Certificate.getExtensionValue("2.16.840.1.113730.1.1");
/* 284 */         if (localObject1 == null) {
/* 285 */           return true;
/*     */         }
/* 287 */         localObject2 = new DerInputStream((byte[])localObject1);
/* 288 */         byte[] arrayOfByte = ((DerInputStream)localObject2).getOctetString();
/* 289 */         arrayOfByte = new DerValue(arrayOfByte).getUnalignedBitString().toByteArray();
/*     */ 
/* 291 */         localNetscapeCertTypeExtension = new NetscapeCertTypeExtension(arrayOfByte);
/*     */       }
/* 293 */       Object localObject1 = (Boolean)localNetscapeCertTypeExtension.get(paramString);
/* 294 */       return ((Boolean)localObject1).booleanValue(); } catch (IOException localIOException) {
/*     */     }
/* 296 */     return false;
/*     */   }
/*     */ 
/*     */   private int checkBasicConstraints(X509Certificate paramX509Certificate, Set<String> paramSet, int paramInt)
/*     */     throws CertificateException
/*     */   {
/* 303 */     paramSet.remove("2.5.29.19");
/* 304 */     int i = paramX509Certificate.getBasicConstraints();
/*     */ 
/* 306 */     if (i < 0) {
/* 307 */       throw new ValidatorException("End user tried to act as a CA", ValidatorException.T_CA_EXTENSIONS, paramX509Certificate);
/*     */     }
/*     */ 
/* 313 */     if (!X509CertImpl.isSelfIssued(paramX509Certificate)) {
/* 314 */       if (paramInt <= 0) {
/* 315 */         throw new ValidatorException("Violated path length constraints", ValidatorException.T_CA_EXTENSIONS, paramX509Certificate);
/*     */       }
/*     */ 
/* 319 */       paramInt--;
/*     */     }
/*     */ 
/* 322 */     if (paramInt > i) {
/* 323 */       paramInt = i;
/*     */     }
/*     */ 
/* 326 */     return paramInt;
/*     */   }
/*     */ 
/*     */   private void checkKeyUsage(X509Certificate paramX509Certificate, Set<String> paramSet)
/*     */     throws CertificateException
/*     */   {
/* 336 */     paramSet.remove("2.5.29.15");
/*     */ 
/* 338 */     paramSet.remove("2.5.29.37");
/*     */ 
/* 341 */     boolean[] arrayOfBoolean = paramX509Certificate.getKeyUsage();
/* 342 */     if (arrayOfBoolean != null)
/*     */     {
/* 344 */       if ((arrayOfBoolean.length < 6) || (arrayOfBoolean[5] == 0))
/* 345 */         throw new ValidatorException("Wrong key usage: expected keyCertSign", ValidatorException.T_CA_EXTENSIONS, paramX509Certificate);
/*     */     }
/*     */   }
/*     */ 
/*     */   private X509Certificate[] buildTrustedChain(X509Certificate[] paramArrayOfX509Certificate)
/*     */     throws CertificateException
/*     */   {
/* 359 */     ArrayList localArrayList = new ArrayList(paramArrayOfX509Certificate.length);
/*     */ 
/* 362 */     for (int i = 0; i < paramArrayOfX509Certificate.length; i++) {
/* 363 */       localObject1 = paramArrayOfX509Certificate[i];
/* 364 */       localObject2 = getTrustedCertificate((X509Certificate)localObject1);
/* 365 */       if (localObject2 != null) {
/* 366 */         localArrayList.add(localObject2);
/* 367 */         return (X509Certificate[])localArrayList.toArray(CHAIN0);
/*     */       }
/* 369 */       localArrayList.add(localObject1);
/*     */     }
/*     */ 
/* 373 */     X509Certificate localX509Certificate1 = paramArrayOfX509Certificate[(paramArrayOfX509Certificate.length - 1)];
/* 374 */     Object localObject1 = localX509Certificate1.getSubjectX500Principal();
/* 375 */     Object localObject2 = localX509Certificate1.getIssuerX500Principal();
/* 376 */     List localList = (List)this.trustedX500Principals.get(localObject2);
/* 377 */     if (localList != null) {
/* 378 */       X509Certificate localX509Certificate2 = (X509Certificate)localList.iterator().next();
/* 379 */       localArrayList.add(localX509Certificate2);
/* 380 */       return (X509Certificate[])localArrayList.toArray(CHAIN0);
/*     */     }
/*     */ 
/* 384 */     throw new ValidatorException(ValidatorException.T_NO_TRUST_ANCHOR);
/*     */   }
/*     */ 
/*     */   private X509Certificate getTrustedCertificate(X509Certificate paramX509Certificate)
/*     */   {
/* 394 */     X500Principal localX500Principal1 = paramX509Certificate.getSubjectX500Principal();
/* 395 */     List localList = (List)this.trustedX500Principals.get(localX500Principal1);
/* 396 */     if (localList == null) {
/* 397 */       return null;
/*     */     }
/*     */ 
/* 400 */     X500Principal localX500Principal2 = paramX509Certificate.getIssuerX500Principal();
/* 401 */     PublicKey localPublicKey = paramX509Certificate.getPublicKey();
/*     */ 
/* 403 */     for (X509Certificate localX509Certificate : localList) {
/* 404 */       if (localX509Certificate.equals(paramX509Certificate)) {
/* 405 */         return paramX509Certificate;
/*     */       }
/* 407 */       if ((localX509Certificate.getIssuerX500Principal().equals(localX500Principal2)) && 
/* 410 */         (localX509Certificate.getPublicKey().equals(localPublicKey)))
/*     */       {
/* 415 */         return localX509Certificate;
/*     */       }
/*     */     }
/* 417 */     return null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.validator.SimpleValidator
 * JD-Core Version:    0.6.2
 */