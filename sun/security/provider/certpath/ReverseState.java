/*     */ package sun.security.provider.certpath;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.security.PublicKey;
/*     */ import java.security.cert.CertPathValidatorException;
/*     */ import java.security.cert.CertificateException;
/*     */ import java.security.cert.PKIXCertPathChecker;
/*     */ import java.security.cert.TrustAnchor;
/*     */ import java.security.cert.X509Certificate;
/*     */ import java.security.interfaces.DSAPublicKey;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
/*     */ import java.util.Set;
/*     */ import javax.security.auth.x500.X500Principal;
/*     */ import sun.security.util.Debug;
/*     */ import sun.security.x509.NameConstraintsExtension;
/*     */ import sun.security.x509.SubjectKeyIdentifierExtension;
/*     */ import sun.security.x509.X509CertImpl;
/*     */ 
/*     */ class ReverseState
/*     */   implements State
/*     */ {
/*  59 */   private static final Debug debug = Debug.getInstance("certpath");
/*     */   X500Principal subjectDN;
/*     */   PublicKey pubKey;
/*     */   SubjectKeyIdentifierExtension subjKeyId;
/*     */   NameConstraintsExtension nc;
/*     */   int explicitPolicy;
/*     */   int policyMapping;
/*     */   int inhibitAnyPolicy;
/*     */   int certIndex;
/*     */   PolicyNodeImpl rootNode;
/*     */   int remainingCACerts;
/*     */   ArrayList<PKIXCertPathChecker> userCheckers;
/*  94 */   private boolean init = true;
/*     */   public CrlRevocationChecker crlChecker;
/*     */   AlgorithmChecker algorithmChecker;
/*     */   UntrustedChecker untrustedChecker;
/*     */   TrustAnchor trustAnchor;
/* 111 */   public boolean crlSign = true;
/*     */ 
/*     */   public boolean isInitial()
/*     */   {
/* 120 */     return this.init;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 127 */     StringBuffer localStringBuffer = new StringBuffer();
/*     */     try {
/* 129 */       localStringBuffer.append("State [");
/* 130 */       localStringBuffer.append("\n  subjectDN of last cert: " + this.subjectDN);
/* 131 */       localStringBuffer.append("\n  subjectKeyIdentifier: " + String.valueOf(this.subjKeyId));
/* 132 */       localStringBuffer.append("\n  nameConstraints: " + String.valueOf(this.nc));
/* 133 */       localStringBuffer.append("\n  certIndex: " + this.certIndex);
/* 134 */       localStringBuffer.append("\n  explicitPolicy: " + this.explicitPolicy);
/* 135 */       localStringBuffer.append("\n  policyMapping:  " + this.policyMapping);
/* 136 */       localStringBuffer.append("\n  inhibitAnyPolicy:  " + this.inhibitAnyPolicy);
/* 137 */       localStringBuffer.append("\n  rootNode: " + this.rootNode);
/* 138 */       localStringBuffer.append("\n  remainingCACerts: " + this.remainingCACerts);
/* 139 */       localStringBuffer.append("\n  crlSign: " + this.crlSign);
/* 140 */       localStringBuffer.append("\n  init: " + this.init);
/* 141 */       localStringBuffer.append("\n]\n");
/*     */     } catch (Exception localException) {
/* 143 */       if (debug != null) {
/* 144 */         debug.println("ReverseState.toString() unexpected exception");
/* 145 */         localException.printStackTrace();
/*     */       }
/*     */     }
/* 148 */     return localStringBuffer.toString();
/*     */   }
/*     */ 
/*     */   public void initState(int paramInt, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, List<PKIXCertPathChecker> paramList)
/*     */     throws CertPathValidatorException
/*     */   {
/* 171 */     this.remainingCACerts = (paramInt == -1 ? 2147483647 : paramInt);
/*     */ 
/* 174 */     if (paramBoolean1) {
/* 175 */       this.explicitPolicy = 0;
/*     */     }
/*     */     else
/*     */     {
/* 180 */       this.explicitPolicy = (paramInt == -1 ? paramInt : paramInt + 2);
/*     */     }
/*     */ 
/* 186 */     if (paramBoolean2)
/* 187 */       this.policyMapping = 0;
/*     */     else {
/* 189 */       this.policyMapping = (paramInt == -1 ? paramInt : paramInt + 2);
/*     */     }
/*     */ 
/* 195 */     if (paramBoolean3)
/* 196 */       this.inhibitAnyPolicy = 0;
/*     */     else {
/* 198 */       this.inhibitAnyPolicy = (paramInt == -1 ? paramInt : paramInt + 2);
/*     */     }
/*     */ 
/* 204 */     this.certIndex = 1;
/*     */ 
/* 207 */     HashSet localHashSet = new HashSet(1);
/* 208 */     localHashSet.add("2.5.29.32.0");
/*     */ 
/* 210 */     this.rootNode = new PolicyNodeImpl(null, "2.5.29.32.0", null, false, localHashSet, false);
/*     */ 
/* 216 */     if (paramList != null)
/*     */     {
/* 218 */       this.userCheckers = new ArrayList(paramList);
/*     */ 
/* 220 */       for (PKIXCertPathChecker localPKIXCertPathChecker : paramList)
/* 221 */         localPKIXCertPathChecker.init(false);
/*     */     }
/*     */     else {
/* 224 */       this.userCheckers = new ArrayList();
/*     */     }
/*     */ 
/* 228 */     this.crlSign = true;
/*     */ 
/* 230 */     this.init = true;
/*     */   }
/*     */ 
/*     */   public void updateState(TrustAnchor paramTrustAnchor)
/*     */     throws CertificateException, IOException, CertPathValidatorException
/*     */   {
/* 241 */     this.trustAnchor = paramTrustAnchor;
/* 242 */     X509Certificate localX509Certificate = paramTrustAnchor.getTrustedCert();
/* 243 */     if (localX509Certificate != null) {
/* 244 */       updateState(localX509Certificate);
/*     */     } else {
/* 246 */       localObject = paramTrustAnchor.getCA();
/* 247 */       updateState(paramTrustAnchor.getCAPublicKey(), (X500Principal)localObject);
/*     */     }
/*     */ 
/* 252 */     for (Object localObject = this.userCheckers.iterator(); ((Iterator)localObject).hasNext(); ) { PKIXCertPathChecker localPKIXCertPathChecker = (PKIXCertPathChecker)((Iterator)localObject).next();
/* 253 */       if ((localPKIXCertPathChecker instanceof AlgorithmChecker)) {
/* 254 */         ((AlgorithmChecker)localPKIXCertPathChecker).trySetTrustAnchor(paramTrustAnchor);
/*     */       }
/*     */     }
/*     */ 
/* 258 */     this.init = false;
/*     */   }
/*     */ 
/*     */   private void updateState(PublicKey paramPublicKey, X500Principal paramX500Principal)
/*     */   {
/* 271 */     this.subjectDN = paramX500Principal;
/*     */ 
/* 274 */     this.pubKey = paramPublicKey;
/*     */   }
/*     */ 
/*     */   public void updateState(X509Certificate paramX509Certificate)
/*     */     throws CertificateException, IOException, CertPathValidatorException
/*     */   {
/* 285 */     if (paramX509Certificate == null) {
/* 286 */       return;
/*     */     }
/*     */ 
/* 290 */     this.subjectDN = paramX509Certificate.getSubjectX500Principal();
/*     */ 
/* 293 */     X509CertImpl localX509CertImpl = X509CertImpl.toImpl(paramX509Certificate);
/* 294 */     PublicKey localPublicKey = paramX509Certificate.getPublicKey();
/* 295 */     if (((localPublicKey instanceof DSAPublicKey)) && (((DSAPublicKey)localPublicKey).getParams() == null))
/*     */     {
/* 297 */       localPublicKey = BasicChecker.makeInheritedParamsKey(localPublicKey, this.pubKey);
/*     */     }
/*     */ 
/* 301 */     this.pubKey = localPublicKey;
/*     */ 
/* 307 */     if (this.init) {
/* 308 */       this.init = false;
/* 309 */       return;
/*     */     }
/*     */ 
/* 313 */     this.subjKeyId = localX509CertImpl.getSubjectKeyIdentifierExtension();
/*     */ 
/* 316 */     this.crlSign = CrlRevocationChecker.certCanSignCrl(paramX509Certificate);
/*     */ 
/* 319 */     if (this.nc != null) {
/* 320 */       this.nc.merge(localX509CertImpl.getNameConstraintsExtension());
/*     */     } else {
/* 322 */       this.nc = localX509CertImpl.getNameConstraintsExtension();
/* 323 */       if (this.nc != null)
/*     */       {
/* 327 */         this.nc = ((NameConstraintsExtension)this.nc.clone());
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 332 */     this.explicitPolicy = PolicyChecker.mergeExplicitPolicy(this.explicitPolicy, localX509CertImpl, false);
/*     */ 
/* 334 */     this.policyMapping = PolicyChecker.mergePolicyMapping(this.policyMapping, localX509CertImpl);
/*     */ 
/* 336 */     this.inhibitAnyPolicy = PolicyChecker.mergeInhibitAnyPolicy(this.inhibitAnyPolicy, localX509CertImpl);
/*     */ 
/* 338 */     this.certIndex += 1;
/*     */ 
/* 343 */     this.remainingCACerts = ConstraintsChecker.mergeBasicConstraints(paramX509Certificate, this.remainingCACerts);
/*     */ 
/* 346 */     this.init = false;
/*     */   }
/*     */ 
/*     */   public boolean keyParamsNeeded()
/*     */   {
/* 359 */     return false;
/*     */   }
/*     */ 
/*     */   public Object clone()
/*     */   {
/*     */     try
/*     */     {
/* 373 */       ReverseState localReverseState = (ReverseState)super.clone();
/*     */ 
/* 376 */       localReverseState.userCheckers = ((ArrayList)this.userCheckers.clone());
/*     */ 
/* 378 */       ListIterator localListIterator = localReverseState.userCheckers.listIterator();
/*     */ 
/* 380 */       while (localListIterator.hasNext()) {
/* 381 */         PKIXCertPathChecker localPKIXCertPathChecker = (PKIXCertPathChecker)localListIterator.next();
/* 382 */         if ((localPKIXCertPathChecker instanceof Cloneable)) {
/* 383 */           localListIterator.set((PKIXCertPathChecker)localPKIXCertPathChecker.clone());
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 388 */       if (this.nc != null) {
/* 389 */         localReverseState.nc = ((NameConstraintsExtension)this.nc.clone());
/*     */       }
/*     */ 
/* 393 */       if (this.rootNode != null) {
/* 394 */         localReverseState.rootNode = this.rootNode.copyTree();
/*     */       }
/*     */ 
/* 397 */       return localReverseState;
/*     */     } catch (CloneNotSupportedException localCloneNotSupportedException) {
/* 399 */       throw new InternalError(localCloneNotSupportedException.toString());
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.provider.certpath.ReverseState
 * JD-Core Version:    0.6.2
 */