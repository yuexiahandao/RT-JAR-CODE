/*     */ package java.security.cert;
/*     */ 
/*     */ import java.security.InvalidAlgorithmParameterException;
/*     */ import java.security.KeyStore;
/*     */ import java.security.KeyStoreException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Date;
/*     */ import java.util.Enumeration;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class PKIXParameters
/*     */   implements CertPathParameters
/*     */ {
/*     */   private Set<TrustAnchor> unmodTrustAnchors;
/*     */   private Date date;
/*     */   private List<PKIXCertPathChecker> certPathCheckers;
/*     */   private String sigProvider;
/*  92 */   private boolean revocationEnabled = true;
/*     */   private Set<String> unmodInitialPolicies;
/*  94 */   private boolean explicitPolicyRequired = false;
/*  95 */   private boolean policyMappingInhibited = false;
/*  96 */   private boolean anyPolicyInhibited = false;
/*  97 */   private boolean policyQualifiersRejected = true;
/*     */   private List<CertStore> certStores;
/*     */   private CertSelector certSelector;
/*     */ 
/*     */   public PKIXParameters(Set<TrustAnchor> paramSet)
/*     */     throws InvalidAlgorithmParameterException
/*     */   {
/* 120 */     setTrustAnchors(paramSet);
/*     */ 
/* 122 */     this.unmodInitialPolicies = Collections.emptySet();
/* 123 */     this.certPathCheckers = new ArrayList();
/* 124 */     this.certStores = new ArrayList();
/*     */   }
/*     */ 
/*     */   public PKIXParameters(KeyStore paramKeyStore)
/*     */     throws KeyStoreException, InvalidAlgorithmParameterException
/*     */   {
/* 144 */     if (paramKeyStore == null) {
/* 145 */       throw new NullPointerException("the keystore parameter must be non-null");
/*     */     }
/* 147 */     HashSet localHashSet = new HashSet();
/* 148 */     Enumeration localEnumeration = paramKeyStore.aliases();
/* 149 */     while (localEnumeration.hasMoreElements()) {
/* 150 */       String str = (String)localEnumeration.nextElement();
/* 151 */       if (paramKeyStore.isCertificateEntry(str)) {
/* 152 */         Certificate localCertificate = paramKeyStore.getCertificate(str);
/* 153 */         if ((localCertificate instanceof X509Certificate))
/* 154 */           localHashSet.add(new TrustAnchor((X509Certificate)localCertificate, null));
/*     */       }
/*     */     }
/* 157 */     setTrustAnchors(localHashSet);
/* 158 */     this.unmodInitialPolicies = Collections.emptySet();
/* 159 */     this.certPathCheckers = new ArrayList();
/* 160 */     this.certStores = new ArrayList();
/*     */   }
/*     */ 
/*     */   public Set<TrustAnchor> getTrustAnchors()
/*     */   {
/* 173 */     return this.unmodTrustAnchors;
/*     */   }
/*     */ 
/*     */   public void setTrustAnchors(Set<TrustAnchor> paramSet)
/*     */     throws InvalidAlgorithmParameterException
/*     */   {
/* 195 */     if (paramSet == null) {
/* 196 */       throw new NullPointerException("the trustAnchors parameters must be non-null");
/*     */     }
/*     */ 
/* 199 */     if (paramSet.isEmpty()) {
/* 200 */       throw new InvalidAlgorithmParameterException("the trustAnchors parameter must be non-empty");
/*     */     }
/*     */ 
/* 203 */     for (Iterator localIterator = paramSet.iterator(); localIterator.hasNext(); ) {
/* 204 */       if (!(localIterator.next() instanceof TrustAnchor)) {
/* 205 */         throw new ClassCastException("all elements of set must be of type java.security.cert.TrustAnchor");
/*     */       }
/*     */     }
/*     */ 
/* 209 */     this.unmodTrustAnchors = Collections.unmodifiableSet(new HashSet(paramSet));
/*     */   }
/*     */ 
/*     */   public Set<String> getInitialPolicies()
/*     */   {
/* 228 */     return this.unmodInitialPolicies;
/*     */   }
/*     */ 
/*     */   public void setInitialPolicies(Set<String> paramSet)
/*     */   {
/* 251 */     if (paramSet != null) {
/* 252 */       Iterator localIterator = paramSet.iterator();
/* 253 */       while (localIterator.hasNext()) {
/* 254 */         if (!(localIterator.next() instanceof String)) {
/* 255 */           throw new ClassCastException("all elements of set must be of type java.lang.String");
/*     */         }
/*     */       }
/* 258 */       this.unmodInitialPolicies = Collections.unmodifiableSet(new HashSet(paramSet));
/*     */     }
/*     */     else {
/* 261 */       this.unmodInitialPolicies = Collections.emptySet();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setCertStores(List<CertStore> paramList)
/*     */   {
/* 282 */     if (paramList == null) {
/* 283 */       this.certStores = new ArrayList();
/*     */     } else {
/* 285 */       for (Iterator localIterator = paramList.iterator(); localIterator.hasNext(); ) {
/* 286 */         if (!(localIterator.next() instanceof CertStore)) {
/* 287 */           throw new ClassCastException("all elements of list must be of type java.security.cert.CertStore");
/*     */         }
/*     */       }
/*     */ 
/* 291 */       this.certStores = new ArrayList(paramList);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void addCertStore(CertStore paramCertStore)
/*     */   {
/* 303 */     if (paramCertStore != null)
/* 304 */       this.certStores.add(paramCertStore);
/*     */   }
/*     */ 
/*     */   public List<CertStore> getCertStores()
/*     */   {
/* 318 */     return Collections.unmodifiableList(new ArrayList(this.certStores));
/*     */   }
/*     */ 
/*     */   public void setRevocationEnabled(boolean paramBoolean)
/*     */   {
/* 341 */     this.revocationEnabled = paramBoolean;
/*     */   }
/*     */ 
/*     */   public boolean isRevocationEnabled()
/*     */   {
/* 355 */     return this.revocationEnabled;
/*     */   }
/*     */ 
/*     */   public void setExplicitPolicyRequired(boolean paramBoolean)
/*     */   {
/* 367 */     this.explicitPolicyRequired = paramBoolean;
/*     */   }
/*     */ 
/*     */   public boolean isExplicitPolicyRequired()
/*     */   {
/* 379 */     return this.explicitPolicyRequired;
/*     */   }
/*     */ 
/*     */   public void setPolicyMappingInhibited(boolean paramBoolean)
/*     */   {
/* 391 */     this.policyMappingInhibited = paramBoolean;
/*     */   }
/*     */ 
/*     */   public boolean isPolicyMappingInhibited()
/*     */   {
/* 402 */     return this.policyMappingInhibited;
/*     */   }
/*     */ 
/*     */   public void setAnyPolicyInhibited(boolean paramBoolean)
/*     */   {
/* 415 */     this.anyPolicyInhibited = paramBoolean;
/*     */   }
/*     */ 
/*     */   public boolean isAnyPolicyInhibited()
/*     */   {
/* 426 */     return this.anyPolicyInhibited;
/*     */   }
/*     */ 
/*     */   public void setPolicyQualifiersRejected(boolean paramBoolean)
/*     */   {
/* 453 */     this.policyQualifiersRejected = paramBoolean;
/*     */   }
/*     */ 
/*     */   public boolean getPolicyQualifiersRejected()
/*     */   {
/* 471 */     return this.policyQualifiersRejected;
/*     */   }
/*     */ 
/*     */   public Date getDate()
/*     */   {
/* 485 */     if (this.date == null) {
/* 486 */       return null;
/*     */     }
/* 488 */     return (Date)this.date.clone();
/*     */   }
/*     */ 
/*     */   public void setDate(Date paramDate)
/*     */   {
/* 503 */     if (paramDate != null)
/* 504 */       this.date = ((Date)paramDate.clone());
/*     */     else
/* 506 */       paramDate = null;
/*     */   }
/*     */ 
/*     */   public void setCertPathCheckers(List<PKIXCertPathChecker> paramList)
/*     */   {
/* 546 */     if (paramList != null) {
/* 547 */       ArrayList localArrayList = new ArrayList();
/*     */ 
/* 549 */       for (PKIXCertPathChecker localPKIXCertPathChecker : paramList) {
/* 550 */         localArrayList.add((PKIXCertPathChecker)localPKIXCertPathChecker.clone());
/*     */       }
/* 552 */       this.certPathCheckers = localArrayList;
/*     */     } else {
/* 554 */       this.certPathCheckers = new ArrayList();
/*     */     }
/*     */   }
/*     */ 
/*     */   public List<PKIXCertPathChecker> getCertPathCheckers()
/*     */   {
/* 570 */     ArrayList localArrayList = new ArrayList();
/* 571 */     for (PKIXCertPathChecker localPKIXCertPathChecker : this.certPathCheckers) {
/* 572 */       localArrayList.add((PKIXCertPathChecker)localPKIXCertPathChecker.clone());
/*     */     }
/* 574 */     return Collections.unmodifiableList(localArrayList);
/*     */   }
/*     */ 
/*     */   public void addCertPathChecker(PKIXCertPathChecker paramPKIXCertPathChecker)
/*     */   {
/* 589 */     if (paramPKIXCertPathChecker != null)
/* 590 */       this.certPathCheckers.add((PKIXCertPathChecker)paramPKIXCertPathChecker.clone());
/*     */   }
/*     */ 
/*     */   public String getSigProvider()
/*     */   {
/* 602 */     return this.sigProvider;
/*     */   }
/*     */ 
/*     */   public void setSigProvider(String paramString)
/*     */   {
/* 615 */     this.sigProvider = paramString;
/*     */   }
/*     */ 
/*     */   public CertSelector getTargetCertConstraints()
/*     */   {
/* 631 */     if (this.certSelector != null) {
/* 632 */       return (CertSelector)this.certSelector.clone();
/*     */     }
/* 634 */     return null;
/*     */   }
/*     */ 
/*     */   public void setTargetCertConstraints(CertSelector paramCertSelector)
/*     */   {
/* 652 */     if (paramCertSelector != null)
/* 653 */       this.certSelector = ((CertSelector)paramCertSelector.clone());
/*     */     else
/* 655 */       this.certSelector = null;
/*     */   }
/*     */ 
/*     */   public Object clone()
/*     */   {
/*     */     try
/*     */     {
/* 666 */       PKIXParameters localPKIXParameters = (PKIXParameters)super.clone();
/*     */ 
/* 669 */       if (this.certStores != null) {
/* 670 */         localPKIXParameters.certStores = new ArrayList(this.certStores);
/*     */       }
/* 672 */       if (this.certPathCheckers != null) {
/* 673 */         localPKIXParameters.certPathCheckers = new ArrayList(this.certPathCheckers.size());
/*     */ 
/* 675 */         for (PKIXCertPathChecker localPKIXCertPathChecker : this.certPathCheckers) {
/* 676 */           localPKIXParameters.certPathCheckers.add((PKIXCertPathChecker)localPKIXCertPathChecker.clone());
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 683 */       return localPKIXParameters;
/*     */     }
/*     */     catch (CloneNotSupportedException localCloneNotSupportedException) {
/* 686 */       throw new InternalError(localCloneNotSupportedException.toString());
/*     */     }
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 696 */     StringBuffer localStringBuffer = new StringBuffer();
/* 697 */     localStringBuffer.append("[\n");
/*     */ 
/* 700 */     if (this.unmodTrustAnchors != null) {
/* 701 */       localStringBuffer.append("  Trust Anchors: " + this.unmodTrustAnchors.toString() + "\n");
/*     */     }
/*     */ 
/* 706 */     if (this.unmodInitialPolicies != null) {
/* 707 */       if (this.unmodInitialPolicies.isEmpty())
/* 708 */         localStringBuffer.append("  Initial Policy OIDs: any\n");
/*     */       else {
/* 710 */         localStringBuffer.append("  Initial Policy OIDs: [" + this.unmodInitialPolicies.toString() + "]\n");
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 716 */     localStringBuffer.append("  Validity Date: " + String.valueOf(this.date) + "\n");
/* 717 */     localStringBuffer.append("  Signature Provider: " + String.valueOf(this.sigProvider) + "\n");
/* 718 */     localStringBuffer.append("  Default Revocation Enabled: " + this.revocationEnabled + "\n");
/* 719 */     localStringBuffer.append("  Explicit Policy Required: " + this.explicitPolicyRequired + "\n");
/* 720 */     localStringBuffer.append("  Policy Mapping Inhibited: " + this.policyMappingInhibited + "\n");
/* 721 */     localStringBuffer.append("  Any Policy Inhibited: " + this.anyPolicyInhibited + "\n");
/* 722 */     localStringBuffer.append("  Policy Qualifiers Rejected: " + this.policyQualifiersRejected + "\n");
/*     */ 
/* 725 */     localStringBuffer.append("  Target Cert Constraints: " + String.valueOf(this.certSelector) + "\n");
/*     */ 
/* 728 */     if (this.certPathCheckers != null) {
/* 729 */       localStringBuffer.append("  Certification Path Checkers: [" + this.certPathCheckers.toString() + "]\n");
/*     */     }
/* 731 */     if (this.certStores != null)
/* 732 */       localStringBuffer.append("  CertStores: [" + this.certStores.toString() + "]\n");
/* 733 */     localStringBuffer.append("]");
/* 734 */     return localStringBuffer.toString();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.security.cert.PKIXParameters
 * JD-Core Version:    0.6.2
 */