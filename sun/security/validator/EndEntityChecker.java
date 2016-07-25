/*     */ package sun.security.validator;
/*     */ 
/*     */ import java.security.cert.CertificateException;
/*     */ import java.security.cert.X509Certificate;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ 
/*     */ class EndEntityChecker
/*     */ {
/*     */   private static final String OID_EXTENDED_KEY_USAGE = "2.5.29.37";
/*     */   private static final String OID_EKU_TLS_SERVER = "1.3.6.1.5.5.7.3.1";
/*     */   private static final String OID_EKU_TLS_CLIENT = "1.3.6.1.5.5.7.3.2";
/*     */   private static final String OID_EKU_CODE_SIGNING = "1.3.6.1.5.5.7.3.3";
/*     */   private static final String OID_EKU_TIME_STAMPING = "1.3.6.1.5.5.7.3.8";
/*     */   private static final String OID_EKU_ANY_USAGE = "2.5.29.37.0";
/*     */   private static final String OID_EKU_NS_SGC = "2.16.840.1.113730.4.1";
/*     */   private static final String OID_EKU_MS_SGC = "1.3.6.1.4.1.311.10.3.3";
/*     */   private static final String OID_SUBJECT_ALT_NAME = "2.5.29.17";
/*     */   private static final String NSCT_SSL_CLIENT = "ssl_client";
/*     */   private static final String NSCT_SSL_SERVER = "ssl_server";
/*     */   private static final String NSCT_CODE_SIGNING = "object_signing";
/*     */   private static final int KU_SIGNATURE = 0;
/*     */   private static final int KU_KEY_ENCIPHERMENT = 2;
/*     */   private static final int KU_KEY_AGREEMENT = 4;
/* 108 */   private static final Collection<String> KU_SERVER_SIGNATURE = Arrays.asList(new String[] { "DHE_DSS", "DHE_RSA", "ECDHE_ECDSA", "ECDHE_RSA", "RSA_EXPORT", "UNKNOWN" });
/*     */ 
/* 113 */   private static final Collection<String> KU_SERVER_ENCRYPTION = Arrays.asList(new String[] { "RSA" });
/*     */ 
/* 117 */   private static final Collection<String> KU_SERVER_KEY_AGREEMENT = Arrays.asList(new String[] { "DH_DSS", "DH_RSA", "ECDH_ECDSA", "ECDH_RSA" });
/*     */   private final String variant;
/*     */   private final String type;
/*     */ 
/*     */   private EndEntityChecker(String paramString1, String paramString2)
/*     */   {
/* 127 */     this.type = paramString1;
/* 128 */     this.variant = paramString2;
/*     */   }
/*     */ 
/*     */   static EndEntityChecker getInstance(String paramString1, String paramString2) {
/* 132 */     return new EndEntityChecker(paramString1, paramString2);
/*     */   }
/*     */ 
/*     */   void check(X509Certificate paramX509Certificate, Object paramObject) throws CertificateException
/*     */   {
/* 137 */     if (this.variant.equals("generic"))
/*     */     {
/* 139 */       return;
/* 140 */     }if (this.variant.equals("tls server"))
/* 141 */       checkTLSServer(paramX509Certificate, (String)paramObject);
/* 142 */     else if (this.variant.equals("tls client"))
/* 143 */       checkTLSClient(paramX509Certificate);
/* 144 */     else if (this.variant.equals("code signing"))
/* 145 */       checkCodeSigning(paramX509Certificate);
/* 146 */     else if (this.variant.equals("jce signing"))
/* 147 */       checkCodeSigning(paramX509Certificate);
/* 148 */     else if (this.variant.equals("plugin code signing"))
/* 149 */       checkCodeSigning(paramX509Certificate);
/* 150 */     else if (this.variant.equals("tsa server"))
/* 151 */       checkTSAServer(paramX509Certificate);
/*     */     else
/* 153 */       throw new CertificateException("Unknown variant: " + this.variant);
/*     */   }
/*     */ 
/*     */   private Set<String> getCriticalExtensions(X509Certificate paramX509Certificate)
/*     */   {
/* 162 */     Set localSet = paramX509Certificate.getCriticalExtensionOIDs();
/* 163 */     if (localSet == null) {
/* 164 */       localSet = Collections.emptySet();
/*     */     }
/* 166 */     return localSet;
/*     */   }
/*     */ 
/*     */   private void checkRemainingExtensions(Set<String> paramSet)
/*     */     throws CertificateException
/*     */   {
/* 176 */     paramSet.remove("2.5.29.19");
/*     */ 
/* 182 */     paramSet.remove("2.5.29.17");
/*     */ 
/* 184 */     if (!paramSet.isEmpty())
/* 185 */       throw new CertificateException("Certificate contains unsupported critical extensions: " + paramSet);
/*     */   }
/*     */ 
/*     */   private boolean checkEKU(X509Certificate paramX509Certificate, Set<String> paramSet, String paramString)
/*     */     throws CertificateException
/*     */   {
/* 196 */     List localList = paramX509Certificate.getExtendedKeyUsage();
/* 197 */     if (localList == null) {
/* 198 */       return true;
/*     */     }
/* 200 */     return (localList.contains(paramString)) || (localList.contains("2.5.29.37.0"));
/*     */   }
/*     */ 
/*     */   private boolean checkKeyUsage(X509Certificate paramX509Certificate, int paramInt)
/*     */     throws CertificateException
/*     */   {
/* 210 */     boolean[] arrayOfBoolean = paramX509Certificate.getKeyUsage();
/* 211 */     if (arrayOfBoolean == null) {
/* 212 */       return true;
/*     */     }
/* 214 */     return (arrayOfBoolean.length > paramInt) && (arrayOfBoolean[paramInt] != 0);
/*     */   }
/*     */ 
/*     */   private void checkTLSClient(X509Certificate paramX509Certificate)
/*     */     throws CertificateException
/*     */   {
/* 224 */     Set localSet = getCriticalExtensions(paramX509Certificate);
/*     */ 
/* 226 */     if (!checkKeyUsage(paramX509Certificate, 0)) {
/* 227 */       throw new ValidatorException("KeyUsage does not allow digital signatures", ValidatorException.T_EE_EXTENSIONS, paramX509Certificate);
/*     */     }
/*     */ 
/* 232 */     if (!checkEKU(paramX509Certificate, localSet, "1.3.6.1.5.5.7.3.2")) {
/* 233 */       throw new ValidatorException("Extended key usage does not permit use for TLS client authentication", ValidatorException.T_EE_EXTENSIONS, paramX509Certificate);
/*     */     }
/*     */ 
/* 238 */     if (!SimpleValidator.getNetscapeCertTypeBit(paramX509Certificate, "ssl_client")) {
/* 239 */       throw new ValidatorException("Netscape cert type does not permit use for SSL client", ValidatorException.T_EE_EXTENSIONS, paramX509Certificate);
/*     */     }
/*     */ 
/* 245 */     localSet.remove("2.5.29.15");
/* 246 */     localSet.remove("2.5.29.37");
/* 247 */     localSet.remove("2.16.840.1.113730.1.1");
/*     */ 
/* 249 */     checkRemainingExtensions(localSet);
/*     */   }
/*     */ 
/*     */   private void checkTLSServer(X509Certificate paramX509Certificate, String paramString)
/*     */     throws CertificateException
/*     */   {
/* 260 */     Set localSet = getCriticalExtensions(paramX509Certificate);
/*     */ 
/* 262 */     if (KU_SERVER_ENCRYPTION.contains(paramString)) {
/* 263 */       if (!checkKeyUsage(paramX509Certificate, 2)) {
/* 264 */         throw new ValidatorException("KeyUsage does not allow key encipherment", ValidatorException.T_EE_EXTENSIONS, paramX509Certificate);
/*     */       }
/*     */ 
/*     */     }
/* 268 */     else if (KU_SERVER_SIGNATURE.contains(paramString)) {
/* 269 */       if (!checkKeyUsage(paramX509Certificate, 0)) {
/* 270 */         throw new ValidatorException("KeyUsage does not allow digital signatures", ValidatorException.T_EE_EXTENSIONS, paramX509Certificate);
/*     */       }
/*     */ 
/*     */     }
/* 274 */     else if (KU_SERVER_KEY_AGREEMENT.contains(paramString)) {
/* 275 */       if (!checkKeyUsage(paramX509Certificate, 4)) {
/* 276 */         throw new ValidatorException("KeyUsage does not allow key agreement", ValidatorException.T_EE_EXTENSIONS, paramX509Certificate);
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 281 */       throw new CertificateException("Unknown authType: " + paramString);
/*     */     }
/*     */ 
/* 284 */     if (!checkEKU(paramX509Certificate, localSet, "1.3.6.1.5.5.7.3.1"))
/*     */     {
/* 287 */       if ((!checkEKU(paramX509Certificate, localSet, "1.3.6.1.4.1.311.10.3.3")) && (!checkEKU(paramX509Certificate, localSet, "2.16.840.1.113730.4.1")))
/*     */       {
/* 289 */         throw new ValidatorException("Extended key usage does not permit use for TLS server authentication", ValidatorException.T_EE_EXTENSIONS, paramX509Certificate);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 296 */     if (!SimpleValidator.getNetscapeCertTypeBit(paramX509Certificate, "ssl_server")) {
/* 297 */       throw new ValidatorException("Netscape cert type does not permit use for SSL server", ValidatorException.T_EE_EXTENSIONS, paramX509Certificate);
/*     */     }
/*     */ 
/* 303 */     localSet.remove("2.5.29.15");
/* 304 */     localSet.remove("2.5.29.37");
/* 305 */     localSet.remove("2.16.840.1.113730.1.1");
/*     */ 
/* 307 */     checkRemainingExtensions(localSet);
/*     */   }
/*     */ 
/*     */   private void checkCodeSigning(X509Certificate paramX509Certificate)
/*     */     throws CertificateException
/*     */   {
/* 316 */     Set localSet = getCriticalExtensions(paramX509Certificate);
/*     */ 
/* 318 */     if (!checkKeyUsage(paramX509Certificate, 0)) {
/* 319 */       throw new ValidatorException("KeyUsage does not allow digital signatures", ValidatorException.T_EE_EXTENSIONS, paramX509Certificate);
/*     */     }
/*     */ 
/* 324 */     if (!checkEKU(paramX509Certificate, localSet, "1.3.6.1.5.5.7.3.3")) {
/* 325 */       throw new ValidatorException("Extended key usage does not permit use for code signing", ValidatorException.T_EE_EXTENSIONS, paramX509Certificate);
/*     */     }
/*     */ 
/* 332 */     if (!this.variant.equals("jce signing")) {
/* 333 */       if (!SimpleValidator.getNetscapeCertTypeBit(paramX509Certificate, "object_signing")) {
/* 334 */         throw new ValidatorException("Netscape cert type does not permit use for code signing", ValidatorException.T_EE_EXTENSIONS, paramX509Certificate);
/*     */       }
/*     */ 
/* 338 */       localSet.remove("2.16.840.1.113730.1.1");
/*     */     }
/*     */ 
/* 342 */     localSet.remove("2.5.29.15");
/* 343 */     localSet.remove("2.5.29.37");
/*     */ 
/* 345 */     checkRemainingExtensions(localSet);
/*     */   }
/*     */ 
/*     */   private void checkTSAServer(X509Certificate paramX509Certificate)
/*     */     throws CertificateException
/*     */   {
/* 355 */     Set localSet = getCriticalExtensions(paramX509Certificate);
/*     */ 
/* 357 */     if (!checkKeyUsage(paramX509Certificate, 0)) {
/* 358 */       throw new ValidatorException("KeyUsage does not allow digital signatures", ValidatorException.T_EE_EXTENSIONS, paramX509Certificate);
/*     */     }
/*     */ 
/* 363 */     if (paramX509Certificate.getExtendedKeyUsage() == null) {
/* 364 */       throw new ValidatorException("Certificate does not contain an extended key usage extension required for a TSA server", ValidatorException.T_EE_EXTENSIONS, paramX509Certificate);
/*     */     }
/*     */ 
/* 370 */     if (!checkEKU(paramX509Certificate, localSet, "1.3.6.1.5.5.7.3.8")) {
/* 371 */       throw new ValidatorException("Extended key usage does not permit use for TSA server", ValidatorException.T_EE_EXTENSIONS, paramX509Certificate);
/*     */     }
/*     */ 
/* 377 */     localSet.remove("2.5.29.15");
/* 378 */     localSet.remove("2.5.29.37");
/*     */ 
/* 380 */     checkRemainingExtensions(localSet);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.validator.EndEntityChecker
 * JD-Core Version:    0.6.2
 */