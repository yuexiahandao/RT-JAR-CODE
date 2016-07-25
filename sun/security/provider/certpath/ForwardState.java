/*     */ package sun.security.provider.certpath;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.security.PublicKey;
/*     */ import java.security.cert.CertPathValidatorException;
/*     */ import java.security.cert.CertificateException;
/*     */ import java.security.cert.PKIXCertPathChecker;
/*     */ import java.security.cert.X509Certificate;
/*     */ import java.security.interfaces.DSAPublicKey;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
/*     */ import javax.security.auth.x500.X500Principal;
/*     */ import sun.security.util.Debug;
/*     */ import sun.security.x509.GeneralName;
/*     */ import sun.security.x509.GeneralNameInterface;
/*     */ import sun.security.x509.GeneralNames;
/*     */ import sun.security.x509.SubjectAlternativeNameExtension;
/*     */ import sun.security.x509.X500Name;
/*     */ import sun.security.x509.X509CertImpl;
/*     */ 
/*     */ class ForwardState
/*     */   implements State
/*     */ {
/*  59 */   private static final Debug debug = Debug.getInstance("certpath");
/*     */   X500Principal issuerDN;
/*     */   X509CertImpl cert;
/*     */   HashSet<GeneralNameInterface> subjectNamesTraversed;
/*     */   int traversedCACerts;
/*  77 */   private boolean init = true;
/*     */   public CrlRevocationChecker crlChecker;
/*     */   UntrustedChecker untrustedChecker;
/*     */   ArrayList<PKIXCertPathChecker> forwardCheckers;
/*  91 */   boolean keyParamsNeededFlag = false;
/*     */ 
/*     */   public boolean isInitial()
/*     */   {
/* 100 */     return this.init;
/*     */   }
/*     */ 
/*     */   public boolean keyParamsNeeded()
/*     */   {
/* 111 */     return this.keyParamsNeededFlag;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 118 */     StringBuffer localStringBuffer = new StringBuffer();
/*     */     try {
/* 120 */       localStringBuffer.append("State [");
/* 121 */       localStringBuffer.append("\n  issuerDN of last cert: " + this.issuerDN);
/* 122 */       localStringBuffer.append("\n  traversedCACerts: " + this.traversedCACerts);
/* 123 */       localStringBuffer.append("\n  init: " + String.valueOf(this.init));
/* 124 */       localStringBuffer.append("\n  keyParamsNeeded: " + String.valueOf(this.keyParamsNeededFlag));
/*     */ 
/* 126 */       localStringBuffer.append("\n  subjectNamesTraversed: \n" + this.subjectNamesTraversed);
/* 127 */       localStringBuffer.append("]\n");
/*     */     } catch (Exception localException) {
/* 129 */       if (debug != null) {
/* 130 */         debug.println("ForwardState.toString() unexpected exception");
/* 131 */         localException.printStackTrace();
/*     */       }
/*     */     }
/* 134 */     return localStringBuffer.toString();
/*     */   }
/*     */ 
/*     */   public void initState(List<PKIXCertPathChecker> paramList)
/*     */     throws CertPathValidatorException
/*     */   {
/* 145 */     this.subjectNamesTraversed = new HashSet();
/* 146 */     this.traversedCACerts = 0;
/*     */ 
/* 152 */     this.forwardCheckers = new ArrayList();
/* 153 */     if (paramList != null) {
/* 154 */       for (PKIXCertPathChecker localPKIXCertPathChecker : paramList) {
/* 155 */         if (localPKIXCertPathChecker.isForwardCheckingSupported()) {
/* 156 */           localPKIXCertPathChecker.init(true);
/* 157 */           this.forwardCheckers.add(localPKIXCertPathChecker);
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 162 */     this.init = true;
/*     */   }
/*     */ 
/*     */   public void updateState(X509Certificate paramX509Certificate)
/*     */     throws CertificateException, IOException, CertPathValidatorException
/*     */   {
/* 173 */     if (paramX509Certificate == null) {
/* 174 */       return;
/*     */     }
/* 176 */     X509CertImpl localX509CertImpl = X509CertImpl.toImpl(paramX509Certificate);
/*     */ 
/* 179 */     PublicKey localPublicKey = localX509CertImpl.getPublicKey();
/* 180 */     if (((localPublicKey instanceof DSAPublicKey)) && (((DSAPublicKey)localPublicKey).getParams() == null))
/*     */     {
/* 182 */       this.keyParamsNeededFlag = true;
/*     */     }
/*     */ 
/* 186 */     this.cert = localX509CertImpl;
/*     */ 
/* 189 */     this.issuerDN = paramX509Certificate.getIssuerX500Principal();
/*     */ 
/* 191 */     if (!X509CertImpl.isSelfIssued(paramX509Certificate))
/*     */     {
/* 197 */       if ((!this.init) && (paramX509Certificate.getBasicConstraints() != -1)) {
/* 198 */         this.traversedCACerts += 1;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 204 */     if ((this.init) || (!X509CertImpl.isSelfIssued(paramX509Certificate))) {
/* 205 */       X500Principal localX500Principal = paramX509Certificate.getSubjectX500Principal();
/* 206 */       this.subjectNamesTraversed.add(X500Name.asX500Name(localX500Principal));
/*     */       try
/*     */       {
/* 209 */         SubjectAlternativeNameExtension localSubjectAlternativeNameExtension = localX509CertImpl.getSubjectAlternativeNameExtension();
/*     */ 
/* 211 */         if (localSubjectAlternativeNameExtension != null) {
/* 212 */           GeneralNames localGeneralNames = (GeneralNames)localSubjectAlternativeNameExtension.get("subject_name");
/*     */ 
/* 214 */           Iterator localIterator = localGeneralNames.iterator();
/* 215 */           while (localIterator.hasNext()) {
/* 216 */             GeneralNameInterface localGeneralNameInterface = ((GeneralName)localIterator.next()).getName();
/* 217 */             this.subjectNamesTraversed.add(localGeneralNameInterface);
/*     */           }
/*     */         }
/*     */       } catch (Exception localException) {
/* 221 */         if (debug != null) {
/* 222 */           debug.println("ForwardState.updateState() unexpected exception");
/*     */ 
/* 224 */           localException.printStackTrace();
/*     */         }
/* 226 */         throw new CertPathValidatorException(localException);
/*     */       }
/*     */     }
/*     */ 
/* 230 */     this.init = false;
/*     */   }
/*     */ 
/*     */   public Object clone()
/*     */   {
/*     */     try
/*     */     {
/* 244 */       ForwardState localForwardState = (ForwardState)super.clone();
/*     */ 
/* 247 */       localForwardState.forwardCheckers = ((ArrayList)this.forwardCheckers.clone());
/*     */ 
/* 249 */       ListIterator localListIterator = localForwardState.forwardCheckers.listIterator();
/*     */ 
/* 251 */       while (localListIterator.hasNext()) {
/* 252 */         PKIXCertPathChecker localPKIXCertPathChecker = (PKIXCertPathChecker)localListIterator.next();
/* 253 */         if ((localPKIXCertPathChecker instanceof Cloneable)) {
/* 254 */           localListIterator.set((PKIXCertPathChecker)localPKIXCertPathChecker.clone());
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 263 */       localForwardState.subjectNamesTraversed = ((HashSet)this.subjectNamesTraversed.clone());
/*     */ 
/* 265 */       return localForwardState;
/*     */     } catch (CloneNotSupportedException localCloneNotSupportedException) {
/* 267 */       throw new InternalError(localCloneNotSupportedException.toString());
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.provider.certpath.ForwardState
 * JD-Core Version:    0.6.2
 */