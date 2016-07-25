/*     */ package sun.security.x509;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.security.cert.CertificateException;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import sun.security.util.ObjectIdentifier;
/*     */ 
/*     */ public class OIDMap
/*     */ {
/*     */   private static final String ROOT = "x509.info.extensions";
/*     */   private static final String AUTH_KEY_IDENTIFIER = "x509.info.extensions.AuthorityKeyIdentifier";
/*     */   private static final String SUB_KEY_IDENTIFIER = "x509.info.extensions.SubjectKeyIdentifier";
/*     */   private static final String KEY_USAGE = "x509.info.extensions.KeyUsage";
/*     */   private static final String PRIVATE_KEY_USAGE = "x509.info.extensions.PrivateKeyUsage";
/*     */   private static final String POLICY_MAPPINGS = "x509.info.extensions.PolicyMappings";
/*     */   private static final String SUB_ALT_NAME = "x509.info.extensions.SubjectAlternativeName";
/*     */   private static final String ISSUER_ALT_NAME = "x509.info.extensions.IssuerAlternativeName";
/*     */   private static final String BASIC_CONSTRAINTS = "x509.info.extensions.BasicConstraints";
/*     */   private static final String NAME_CONSTRAINTS = "x509.info.extensions.NameConstraints";
/*     */   private static final String POLICY_CONSTRAINTS = "x509.info.extensions.PolicyConstraints";
/*     */   private static final String CRL_NUMBER = "x509.info.extensions.CRLNumber";
/*     */   private static final String CRL_REASON = "x509.info.extensions.CRLReasonCode";
/*     */   private static final String NETSCAPE_CERT = "x509.info.extensions.NetscapeCertType";
/*     */   private static final String CERT_POLICIES = "x509.info.extensions.CertificatePolicies";
/*     */   private static final String EXT_KEY_USAGE = "x509.info.extensions.ExtendedKeyUsage";
/*     */   private static final String INHIBIT_ANY_POLICY = "x509.info.extensions.InhibitAnyPolicy";
/*     */   private static final String CRL_DIST_POINTS = "x509.info.extensions.CRLDistributionPoints";
/*     */   private static final String CERT_ISSUER = "x509.info.extensions.CertificateIssuer";
/*     */   private static final String SUBJECT_INFO_ACCESS = "x509.info.extensions.SubjectInfoAccess";
/*     */   private static final String AUTH_INFO_ACCESS = "x509.info.extensions.AuthorityInfoAccess";
/*     */   private static final String ISSUING_DIST_POINT = "x509.info.extensions.IssuingDistributionPoint";
/*     */   private static final String DELTA_CRL_INDICATOR = "x509.info.extensions.DeltaCRLIndicator";
/*     */   private static final String FRESHEST_CRL = "x509.info.extensions.FreshestCRL";
/*     */   private static final String OCSPNOCHECK = "x509.info.extensions.OCSPNoCheck";
/* 106 */   private static final int[] NetscapeCertType_data = { 2, 16, 840, 1, 113730, 1, 1 };
/*     */ 
/* 116 */   private static final Map<ObjectIdentifier, OIDInfo> oidMap = new HashMap();
/* 117 */   private static final Map<String, OIDInfo> nameMap = new HashMap();
/*     */ 
/*     */   private static void addInternal(String paramString1, ObjectIdentifier paramObjectIdentifier, String paramString2)
/*     */   {
/* 176 */     OIDInfo localOIDInfo = new OIDInfo(paramString1, paramObjectIdentifier, paramString2);
/* 177 */     oidMap.put(paramObjectIdentifier, localOIDInfo);
/* 178 */     nameMap.put(paramString1, localOIDInfo);
/*     */   }
/*     */ 
/*     */   public static void addAttribute(String paramString1, String paramString2, Class paramClass)
/*     */     throws CertificateException
/*     */   {
/*     */     ObjectIdentifier localObjectIdentifier;
/*     */     try
/*     */     {
/* 235 */       localObjectIdentifier = new ObjectIdentifier(paramString2);
/*     */     } catch (IOException localIOException) {
/* 237 */       throw new CertificateException("Invalid Object identifier: " + paramString2);
/*     */     }
/*     */ 
/* 240 */     OIDInfo localOIDInfo = new OIDInfo(paramString1, localObjectIdentifier, paramClass);
/* 241 */     if (oidMap.put(localObjectIdentifier, localOIDInfo) != null) {
/* 242 */       throw new CertificateException("Object identifier already exists: " + paramString2);
/*     */     }
/*     */ 
/* 245 */     if (nameMap.put(paramString1, localOIDInfo) != null)
/* 246 */       throw new CertificateException("Name already exists: " + paramString1);
/*     */   }
/*     */ 
/*     */   public static String getName(ObjectIdentifier paramObjectIdentifier)
/*     */   {
/* 258 */     OIDInfo localOIDInfo = (OIDInfo)oidMap.get(paramObjectIdentifier);
/* 259 */     return localOIDInfo == null ? null : localOIDInfo.name;
/*     */   }
/*     */ 
/*     */   public static ObjectIdentifier getOID(String paramString)
/*     */   {
/* 270 */     OIDInfo localOIDInfo = (OIDInfo)nameMap.get(paramString);
/* 271 */     return localOIDInfo == null ? null : localOIDInfo.oid;
/*     */   }
/*     */ 
/*     */   public static Class getClass(String paramString)
/*     */     throws CertificateException
/*     */   {
/* 281 */     OIDInfo localOIDInfo = (OIDInfo)nameMap.get(paramString);
/* 282 */     return localOIDInfo == null ? null : localOIDInfo.getClazz();
/*     */   }
/*     */ 
/*     */   public static Class getClass(ObjectIdentifier paramObjectIdentifier)
/*     */     throws CertificateException
/*     */   {
/* 293 */     OIDInfo localOIDInfo = (OIDInfo)oidMap.get(paramObjectIdentifier);
/* 294 */     return localOIDInfo == null ? null : localOIDInfo.getClazz();
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/* 118 */     addInternal("x509.info.extensions.SubjectKeyIdentifier", PKIXExtensions.SubjectKey_Id, "sun.security.x509.SubjectKeyIdentifierExtension");
/*     */ 
/* 120 */     addInternal("x509.info.extensions.KeyUsage", PKIXExtensions.KeyUsage_Id, "sun.security.x509.KeyUsageExtension");
/*     */ 
/* 122 */     addInternal("x509.info.extensions.PrivateKeyUsage", PKIXExtensions.PrivateKeyUsage_Id, "sun.security.x509.PrivateKeyUsageExtension");
/*     */ 
/* 124 */     addInternal("x509.info.extensions.SubjectAlternativeName", PKIXExtensions.SubjectAlternativeName_Id, "sun.security.x509.SubjectAlternativeNameExtension");
/*     */ 
/* 126 */     addInternal("x509.info.extensions.IssuerAlternativeName", PKIXExtensions.IssuerAlternativeName_Id, "sun.security.x509.IssuerAlternativeNameExtension");
/*     */ 
/* 128 */     addInternal("x509.info.extensions.BasicConstraints", PKIXExtensions.BasicConstraints_Id, "sun.security.x509.BasicConstraintsExtension");
/*     */ 
/* 130 */     addInternal("x509.info.extensions.CRLNumber", PKIXExtensions.CRLNumber_Id, "sun.security.x509.CRLNumberExtension");
/*     */ 
/* 132 */     addInternal("x509.info.extensions.CRLReasonCode", PKIXExtensions.ReasonCode_Id, "sun.security.x509.CRLReasonCodeExtension");
/*     */ 
/* 134 */     addInternal("x509.info.extensions.NameConstraints", PKIXExtensions.NameConstraints_Id, "sun.security.x509.NameConstraintsExtension");
/*     */ 
/* 136 */     addInternal("x509.info.extensions.PolicyMappings", PKIXExtensions.PolicyMappings_Id, "sun.security.x509.PolicyMappingsExtension");
/*     */ 
/* 138 */     addInternal("x509.info.extensions.AuthorityKeyIdentifier", PKIXExtensions.AuthorityKey_Id, "sun.security.x509.AuthorityKeyIdentifierExtension");
/*     */ 
/* 140 */     addInternal("x509.info.extensions.PolicyConstraints", PKIXExtensions.PolicyConstraints_Id, "sun.security.x509.PolicyConstraintsExtension");
/*     */ 
/* 142 */     addInternal("x509.info.extensions.NetscapeCertType", ObjectIdentifier.newInternal(new int[] { 2, 16, 840, 1, 113730, 1, 1 }), "sun.security.x509.NetscapeCertTypeExtension");
/*     */ 
/* 145 */     addInternal("x509.info.extensions.CertificatePolicies", PKIXExtensions.CertificatePolicies_Id, "sun.security.x509.CertificatePoliciesExtension");
/*     */ 
/* 147 */     addInternal("x509.info.extensions.ExtendedKeyUsage", PKIXExtensions.ExtendedKeyUsage_Id, "sun.security.x509.ExtendedKeyUsageExtension");
/*     */ 
/* 149 */     addInternal("x509.info.extensions.InhibitAnyPolicy", PKIXExtensions.InhibitAnyPolicy_Id, "sun.security.x509.InhibitAnyPolicyExtension");
/*     */ 
/* 151 */     addInternal("x509.info.extensions.CRLDistributionPoints", PKIXExtensions.CRLDistributionPoints_Id, "sun.security.x509.CRLDistributionPointsExtension");
/*     */ 
/* 153 */     addInternal("x509.info.extensions.CertificateIssuer", PKIXExtensions.CertificateIssuer_Id, "sun.security.x509.CertificateIssuerExtension");
/*     */ 
/* 155 */     addInternal("x509.info.extensions.SubjectInfoAccess", PKIXExtensions.SubjectInfoAccess_Id, "sun.security.x509.SubjectInfoAccessExtension");
/*     */ 
/* 157 */     addInternal("x509.info.extensions.AuthorityInfoAccess", PKIXExtensions.AuthInfoAccess_Id, "sun.security.x509.AuthorityInfoAccessExtension");
/*     */ 
/* 159 */     addInternal("x509.info.extensions.IssuingDistributionPoint", PKIXExtensions.IssuingDistributionPoint_Id, "sun.security.x509.IssuingDistributionPointExtension");
/*     */ 
/* 162 */     addInternal("x509.info.extensions.DeltaCRLIndicator", PKIXExtensions.DeltaCRLIndicator_Id, "sun.security.x509.DeltaCRLIndicatorExtension");
/*     */ 
/* 164 */     addInternal("x509.info.extensions.FreshestCRL", PKIXExtensions.FreshestCRL_Id, "sun.security.x509.FreshestCRLExtension");
/*     */ 
/* 166 */     addInternal("x509.info.extensions.OCSPNoCheck", PKIXExtensions.OCSPNoCheck_Id, "sun.security.x509.OCSPNoCheckExtension");
/*     */   }
/*     */ 
/*     */   private static class OIDInfo
/*     */   {
/*     */     final ObjectIdentifier oid;
/*     */     final String name;
/*     */     final String className;
/*     */     private volatile Class clazz;
/*     */ 
/*     */     OIDInfo(String paramString1, ObjectIdentifier paramObjectIdentifier, String paramString2)
/*     */     {
/* 192 */       this.name = paramString1;
/* 193 */       this.oid = paramObjectIdentifier;
/* 194 */       this.className = paramString2;
/*     */     }
/*     */ 
/*     */     OIDInfo(String paramString, ObjectIdentifier paramObjectIdentifier, Class paramClass) {
/* 198 */       this.name = paramString;
/* 199 */       this.oid = paramObjectIdentifier;
/* 200 */       this.className = paramClass.getName();
/* 201 */       this.clazz = paramClass;
/*     */     }
/*     */ 
/*     */     Class getClazz()
/*     */       throws CertificateException
/*     */     {
/*     */       try
/*     */       {
/* 209 */         Class localClass = this.clazz;
/* 210 */         if (localClass == null) {
/* 211 */           localClass = Class.forName(this.className);
/* 212 */           this.clazz = localClass;
/*     */         }
/* 214 */         return localClass;
/*     */       } catch (ClassNotFoundException localClassNotFoundException) {
/* 216 */         throw ((CertificateException)new CertificateException("Could not load class: " + localClassNotFoundException).initCause(localClassNotFoundException));
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.x509.OIDMap
 * JD-Core Version:    0.6.2
 */