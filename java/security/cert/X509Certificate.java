/*     */ package java.security.cert;
/*     */ 
/*     */ import java.math.BigInteger;
/*     */ import java.security.Principal;
/*     */ import java.util.Collection;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ import javax.security.auth.x500.X500Principal;
/*     */ import sun.security.x509.X509CertImpl;
/*     */ 
/*     */ public abstract class X509Certificate extends Certificate
/*     */   implements X509Extension
/*     */ {
/*     */   private static final long serialVersionUID = -2491127588187038216L;
/*     */   private transient X500Principal subjectX500Principal;
/*     */   private transient X500Principal issuerX500Principal;
/*     */ 
/*     */   protected X509Certificate()
/*     */   {
/* 123 */     super("X.509");
/*     */   }
/*     */ 
/*     */   public abstract void checkValidity()
/*     */     throws CertificateExpiredException, CertificateNotYetValidException;
/*     */ 
/*     */   public abstract void checkValidity(Date paramDate)
/*     */     throws CertificateExpiredException, CertificateNotYetValidException;
/*     */ 
/*     */   public abstract int getVersion();
/*     */ 
/*     */   public abstract BigInteger getSerialNumber();
/*     */ 
/*     */   public abstract Principal getIssuerDN();
/*     */ 
/*     */   public X500Principal getIssuerX500Principal()
/*     */   {
/* 251 */     if (this.issuerX500Principal == null) {
/* 252 */       this.issuerX500Principal = X509CertImpl.getIssuerX500Principal(this);
/*     */     }
/* 254 */     return this.issuerX500Principal;
/*     */   }
/*     */ 
/*     */   public abstract Principal getSubjectDN();
/*     */ 
/*     */   public X500Principal getSubjectX500Principal()
/*     */   {
/* 294 */     if (this.subjectX500Principal == null) {
/* 295 */       this.subjectX500Principal = X509CertImpl.getSubjectX500Principal(this);
/*     */     }
/* 297 */     return this.subjectX500Principal;
/*     */   }
/*     */ 
/*     */   public abstract Date getNotBefore();
/*     */ 
/*     */   public abstract Date getNotAfter();
/*     */ 
/*     */   public abstract byte[] getTBSCertificate()
/*     */     throws CertificateEncodingException;
/*     */ 
/*     */   public abstract byte[] getSignature();
/*     */ 
/*     */   public abstract String getSigAlgName();
/*     */ 
/*     */   public abstract String getSigAlgOID();
/*     */ 
/*     */   public abstract byte[] getSigAlgParams();
/*     */ 
/*     */   public abstract boolean[] getIssuerUniqueID();
/*     */ 
/*     */   public abstract boolean[] getSubjectUniqueID();
/*     */ 
/*     */   public abstract boolean[] getKeyUsage();
/*     */ 
/*     */   public List<String> getExtendedKeyUsage()
/*     */     throws CertificateParsingException
/*     */   {
/* 508 */     return X509CertImpl.getExtendedKeyUsage(this);
/*     */   }
/*     */ 
/*     */   public abstract int getBasicConstraints();
/*     */ 
/*     */   public Collection<List<?>> getSubjectAlternativeNames()
/*     */     throws CertificateParsingException
/*     */   {
/* 605 */     return X509CertImpl.getSubjectAlternativeNames(this);
/*     */   }
/*     */ 
/*     */   public Collection<List<?>> getIssuerAlternativeNames()
/*     */     throws CertificateParsingException
/*     */   {
/* 647 */     return X509CertImpl.getIssuerAlternativeNames(this);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.security.cert.X509Certificate
 * JD-Core Version:    0.6.2
 */