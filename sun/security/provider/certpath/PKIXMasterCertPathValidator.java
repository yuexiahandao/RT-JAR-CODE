/*     */ package sun.security.provider.certpath;
/*     */ 
/*     */ import java.security.cert.CertPath;
/*     */ import java.security.cert.CertPathValidatorException;
/*     */ import java.security.cert.CertPathValidatorException.BasicReason;
/*     */ import java.security.cert.PKIXCertPathChecker;
/*     */ import java.security.cert.PKIXReason;
/*     */ import java.security.cert.X509Certificate;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import sun.security.util.Debug;
/*     */ 
/*     */ class PKIXMasterCertPathValidator
/*     */ {
/*  51 */   private static final Debug debug = Debug.getInstance("certpath");
/*     */   private List<PKIXCertPathChecker> certPathCheckers;
/*     */ 
/*     */   PKIXMasterCertPathValidator(List<PKIXCertPathChecker> paramList)
/*     */   {
/*  61 */     this.certPathCheckers = paramList;
/*     */   }
/*     */ 
/*     */   void validate(CertPath paramCertPath, List<X509Certificate> paramList)
/*     */     throws CertPathValidatorException
/*     */   {
/*  85 */     int i = paramList.size();
/*     */ 
/*  87 */     if (debug != null) {
/*  88 */       debug.println("--------------------------------------------------------------");
/*     */ 
/*  90 */       debug.println("Executing PKIX certification path validation algorithm.");
/*     */     }
/*     */ 
/*  94 */     for (int j = 0; j < i; j++)
/*     */     {
/* 103 */       if (debug != null) {
/* 104 */         debug.println("Checking cert" + (j + 1) + " ...");
/*     */       }
/* 106 */       X509Certificate localX509Certificate = (X509Certificate)paramList.get(j);
/* 107 */       Set localSet = localX509Certificate.getCriticalExtensionOIDs();
/*     */ 
/* 109 */       if (localSet == null) {
/* 110 */         localSet = Collections.emptySet();
/*     */       }
/*     */ 
/* 113 */       if ((debug != null) && (!localSet.isEmpty())) {
/* 114 */         debug.println("Set of critical extensions:");
/* 115 */         for (localObject = localSet.iterator(); ((Iterator)localObject).hasNext(); ) { String str = (String)((Iterator)localObject).next();
/* 116 */           debug.println(str);
/*     */         }
/*     */       }
/*     */ 
/* 120 */       Object localObject = null;
/* 121 */       for (int k = 0; k < this.certPathCheckers.size(); k++)
/*     */       {
/* 123 */         PKIXCertPathChecker localPKIXCertPathChecker = (PKIXCertPathChecker)this.certPathCheckers.get(k);
/* 124 */         if (debug != null) {
/* 125 */           debug.println("-Using checker" + (k + 1) + " ... [" + localPKIXCertPathChecker.getClass().getName() + "]");
/*     */         }
/*     */ 
/* 129 */         if (j == 0)
/* 130 */           localPKIXCertPathChecker.init(false);
/*     */         try
/*     */         {
/* 133 */           localPKIXCertPathChecker.check(localX509Certificate, localSet);
/*     */ 
/* 136 */           if (isRevocationCheck(localPKIXCertPathChecker, k, this.certPathCheckers)) {
/* 137 */             if (debug != null) {
/* 138 */               debug.println("-checker" + (k + 1) + " validation succeeded");
/*     */             }
/*     */ 
/* 141 */             k++;
/* 142 */             continue;
/*     */           }
/*     */ 
/*     */         }
/*     */         catch (CertPathValidatorException localCertPathValidatorException1)
/*     */         {
/* 148 */           if ((localObject != null) && ((localPKIXCertPathChecker instanceof CrlRevocationChecker)))
/*     */           {
/* 150 */             if (localCertPathValidatorException1.getReason() == CertPathValidatorException.BasicReason.REVOKED) {
/* 151 */               throw localCertPathValidatorException1;
/*     */             }
/* 153 */             throw ((Throwable)localObject);
/*     */           }
/*     */ 
/* 159 */           CertPathValidatorException localCertPathValidatorException2 = new CertPathValidatorException(localCertPathValidatorException1.getMessage(), localCertPathValidatorException1.getCause(), paramCertPath, i - (j + 1), localCertPathValidatorException1.getReason());
/*     */ 
/* 165 */           if (localCertPathValidatorException1.getReason() == CertPathValidatorException.BasicReason.REVOKED) {
/* 166 */             throw localCertPathValidatorException2;
/*     */           }
/*     */ 
/* 169 */           if (!isRevocationCheck(localPKIXCertPathChecker, k, this.certPathCheckers))
/*     */           {
/* 171 */             throw localCertPathValidatorException2;
/*     */           }
/*     */ 
/* 175 */           localObject = localCertPathValidatorException2;
/*     */ 
/* 178 */           if (debug != null) {
/* 179 */             debug.println(localCertPathValidatorException1.getMessage());
/* 180 */             debug.println("preparing to failover (from OCSP to CRLs)");
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/* 185 */         if (debug != null) {
/* 186 */           debug.println("-checker" + (k + 1) + " validation succeeded");
/*     */         }
/*     */       }
/* 189 */       if (debug != null)
/* 190 */         debug.println("checking for unresolvedCritExts");
/* 191 */       if (!localSet.isEmpty()) {
/* 192 */         throw new CertPathValidatorException("unrecognized critical extension(s)", null, paramCertPath, i - (j + 1), PKIXReason.UNRECOGNIZED_CRIT_EXT);
/*     */       }
/*     */ 
/* 197 */       if (debug != null) {
/* 198 */         debug.println("\ncert" + (j + 1) + " validation succeeded.\n");
/*     */       }
/*     */     }
/* 201 */     if (debug != null) {
/* 202 */       debug.println("Cert path validation succeeded. (PKIX validation algorithm)");
/*     */ 
/* 204 */       debug.println("--------------------------------------------------------------");
/*     */     }
/*     */   }
/*     */ 
/*     */   private static boolean isRevocationCheck(PKIXCertPathChecker paramPKIXCertPathChecker, int paramInt, List<PKIXCertPathChecker> paramList)
/*     */   {
/* 217 */     if (((paramPKIXCertPathChecker instanceof OCSPChecker)) && (paramInt + 1 < paramList.size())) {
/* 218 */       PKIXCertPathChecker localPKIXCertPathChecker = (PKIXCertPathChecker)paramList.get(paramInt + 1);
/* 219 */       if ((localPKIXCertPathChecker instanceof CrlRevocationChecker)) {
/* 220 */         return true;
/*     */       }
/*     */     }
/* 223 */     return false;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.provider.certpath.PKIXMasterCertPathValidator
 * JD-Core Version:    0.6.2
 */