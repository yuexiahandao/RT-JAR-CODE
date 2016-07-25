/*     */ package sun.security.provider;
/*     */ 
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.security.Security;
/*     */ import java.util.Map;
/*     */ 
/*     */ final class SunEntries
/*     */ {
/*     */   private static final String PROP_EGD = "java.security.egd";
/*     */   private static final String PROP_RNDSOURCE = "securerandom.source";
/*     */   static final String URL_DEV_RANDOM = "file:/dev/random";
/*     */   static final String URL_DEV_URANDOM = "file:/dev/urandom";
/* 261 */   private static final String seedSource = (String)AccessController.doPrivileged(new PrivilegedAction()
/*     */   {
/*     */     public String run()
/*     */     {
/* 265 */       String str = System.getProperty("java.security.egd", "");
/* 266 */       if (str.length() != 0) {
/* 267 */         return str;
/*     */       }
/* 269 */       str = Security.getProperty("securerandom.source");
/* 270 */       if (str == null) {
/* 271 */         return "";
/*     */       }
/* 273 */       return str;
/*     */     }
/*     */   });
/*     */ 
/*     */   static void putEntries(Map<Object, Object> paramMap)
/*     */   {
/*  86 */     boolean bool1 = NativePRNG.isAvailable();
/*  87 */     boolean bool2 = seedSource.equals("file:/dev/urandom");
/*  88 */     if ((bool1) && (bool2)) {
/*  89 */       paramMap.put("SecureRandom.NativePRNG", "sun.security.provider.NativePRNG");
/*     */     }
/*     */ 
/*  92 */     paramMap.put("SecureRandom.SHA1PRNG", "sun.security.provider.SecureRandom");
/*     */ 
/*  94 */     if ((bool1) && (!bool2)) {
/*  95 */       paramMap.put("SecureRandom.NativePRNG", "sun.security.provider.NativePRNG");
/*     */     }
/*     */ 
/* 102 */     paramMap.put("Signature.SHA1withDSA", "sun.security.provider.DSA$SHA1withDSA");
/* 103 */     paramMap.put("Signature.NONEwithDSA", "sun.security.provider.DSA$RawDSA");
/* 104 */     paramMap.put("Alg.Alias.Signature.RawDSA", "NONEwithDSA");
/*     */ 
/* 106 */     String str = "java.security.interfaces.DSAPublicKey|java.security.interfaces.DSAPrivateKey";
/*     */ 
/* 108 */     paramMap.put("Signature.SHA1withDSA SupportedKeyClasses", str);
/* 109 */     paramMap.put("Signature.NONEwithDSA SupportedKeyClasses", str);
/*     */ 
/* 111 */     paramMap.put("Alg.Alias.Signature.DSA", "SHA1withDSA");
/* 112 */     paramMap.put("Alg.Alias.Signature.DSS", "SHA1withDSA");
/* 113 */     paramMap.put("Alg.Alias.Signature.SHA/DSA", "SHA1withDSA");
/* 114 */     paramMap.put("Alg.Alias.Signature.SHA-1/DSA", "SHA1withDSA");
/* 115 */     paramMap.put("Alg.Alias.Signature.SHA1/DSA", "SHA1withDSA");
/* 116 */     paramMap.put("Alg.Alias.Signature.SHAwithDSA", "SHA1withDSA");
/* 117 */     paramMap.put("Alg.Alias.Signature.DSAWithSHA1", "SHA1withDSA");
/* 118 */     paramMap.put("Alg.Alias.Signature.OID.1.2.840.10040.4.3", "SHA1withDSA");
/*     */ 
/* 120 */     paramMap.put("Alg.Alias.Signature.1.2.840.10040.4.3", "SHA1withDSA");
/* 121 */     paramMap.put("Alg.Alias.Signature.1.3.14.3.2.13", "SHA1withDSA");
/* 122 */     paramMap.put("Alg.Alias.Signature.1.3.14.3.2.27", "SHA1withDSA");
/*     */ 
/* 127 */     paramMap.put("KeyPairGenerator.DSA", "sun.security.provider.DSAKeyPairGenerator");
/*     */ 
/* 129 */     paramMap.put("Alg.Alias.KeyPairGenerator.OID.1.2.840.10040.4.1", "DSA");
/* 130 */     paramMap.put("Alg.Alias.KeyPairGenerator.1.2.840.10040.4.1", "DSA");
/* 131 */     paramMap.put("Alg.Alias.KeyPairGenerator.1.3.14.3.2.12", "DSA");
/*     */ 
/* 136 */     paramMap.put("MessageDigest.MD2", "sun.security.provider.MD2");
/* 137 */     paramMap.put("MessageDigest.MD5", "sun.security.provider.MD5");
/* 138 */     paramMap.put("MessageDigest.SHA", "sun.security.provider.SHA");
/*     */ 
/* 140 */     paramMap.put("Alg.Alias.MessageDigest.SHA-1", "SHA");
/* 141 */     paramMap.put("Alg.Alias.MessageDigest.SHA1", "SHA");
/*     */ 
/* 143 */     paramMap.put("MessageDigest.SHA-256", "sun.security.provider.SHA2");
/* 144 */     paramMap.put("MessageDigest.SHA-384", "sun.security.provider.SHA5$SHA384");
/* 145 */     paramMap.put("MessageDigest.SHA-512", "sun.security.provider.SHA5$SHA512");
/*     */ 
/* 150 */     paramMap.put("AlgorithmParameterGenerator.DSA", "sun.security.provider.DSAParameterGenerator");
/*     */ 
/* 156 */     paramMap.put("AlgorithmParameters.DSA", "sun.security.provider.DSAParameters");
/*     */ 
/* 158 */     paramMap.put("Alg.Alias.AlgorithmParameters.1.3.14.3.2.12", "DSA");
/* 159 */     paramMap.put("Alg.Alias.AlgorithmParameters.1.2.840.10040.4.1", "DSA");
/*     */ 
/* 164 */     paramMap.put("KeyFactory.DSA", "sun.security.provider.DSAKeyFactory");
/* 165 */     paramMap.put("Alg.Alias.KeyFactory.1.3.14.3.2.12", "DSA");
/* 166 */     paramMap.put("Alg.Alias.KeyFactory.1.2.840.10040.4.1", "DSA");
/*     */ 
/* 171 */     paramMap.put("CertificateFactory.X.509", "sun.security.provider.X509Factory");
/*     */ 
/* 173 */     paramMap.put("Alg.Alias.CertificateFactory.X509", "X.509");
/*     */ 
/* 178 */     paramMap.put("KeyStore.JKS", "sun.security.provider.JavaKeyStore$JKS");
/* 179 */     paramMap.put("KeyStore.CaseExactJKS", "sun.security.provider.JavaKeyStore$CaseExactJKS");
/*     */ 
/* 185 */     paramMap.put("Policy.JavaPolicy", "sun.security.provider.PolicySpiFile");
/*     */ 
/* 190 */     paramMap.put("Configuration.JavaLoginConfig", "sun.security.provider.ConfigSpiFile");
/*     */ 
/* 196 */     paramMap.put("CertPathBuilder.PKIX", "sun.security.provider.certpath.SunCertPathBuilder");
/*     */ 
/* 198 */     paramMap.put("CertPathBuilder.PKIX ValidationAlgorithm", "RFC3280");
/*     */ 
/* 204 */     paramMap.put("CertPathValidator.PKIX", "sun.security.provider.certpath.PKIXCertPathValidator");
/*     */ 
/* 206 */     paramMap.put("CertPathValidator.PKIX ValidationAlgorithm", "RFC3280");
/*     */ 
/* 212 */     paramMap.put("CertStore.LDAP", "sun.security.provider.certpath.ldap.LDAPCertStore");
/*     */ 
/* 214 */     paramMap.put("CertStore.LDAP LDAPSchema", "RFC2587");
/* 215 */     paramMap.put("CertStore.Collection", "sun.security.provider.certpath.CollectionCertStore");
/*     */ 
/* 217 */     paramMap.put("CertStore.com.sun.security.IndexedCollection", "sun.security.provider.certpath.IndexedCollectionCertStore");
/*     */ 
/* 223 */     paramMap.put("Signature.SHA1withDSA KeySize", "1024");
/* 224 */     paramMap.put("KeyPairGenerator.DSA KeySize", "1024");
/* 225 */     paramMap.put("AlgorithmParameterGenerator.DSA KeySize", "1024");
/*     */ 
/* 230 */     paramMap.put("Signature.SHA1withDSA ImplementedIn", "Software");
/* 231 */     paramMap.put("KeyPairGenerator.DSA ImplementedIn", "Software");
/* 232 */     paramMap.put("MessageDigest.MD5 ImplementedIn", "Software");
/* 233 */     paramMap.put("MessageDigest.SHA ImplementedIn", "Software");
/* 234 */     paramMap.put("AlgorithmParameterGenerator.DSA ImplementedIn", "Software");
/*     */ 
/* 236 */     paramMap.put("AlgorithmParameters.DSA ImplementedIn", "Software");
/* 237 */     paramMap.put("KeyFactory.DSA ImplementedIn", "Software");
/* 238 */     paramMap.put("SecureRandom.SHA1PRNG ImplementedIn", "Software");
/* 239 */     paramMap.put("CertificateFactory.X.509 ImplementedIn", "Software");
/* 240 */     paramMap.put("KeyStore.JKS ImplementedIn", "Software");
/* 241 */     paramMap.put("CertPathValidator.PKIX ImplementedIn", "Software");
/* 242 */     paramMap.put("CertPathBuilder.PKIX ImplementedIn", "Software");
/* 243 */     paramMap.put("CertStore.LDAP ImplementedIn", "Software");
/* 244 */     paramMap.put("CertStore.Collection ImplementedIn", "Software");
/* 245 */     paramMap.put("CertStore.com.sun.security.IndexedCollection ImplementedIn", "Software");
/*     */   }
/*     */ 
/*     */   static String getSeedSource()
/*     */   {
/* 279 */     return seedSource;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.provider.SunEntries
 * JD-Core Version:    0.6.2
 */