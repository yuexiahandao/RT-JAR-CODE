/*     */ package java.security.cert;
/*     */ 
/*     */ import java.io.InputStream;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ 
/*     */ public abstract class CertificateFactorySpi
/*     */ {
/*     */   public abstract Certificate engineGenerateCertificate(InputStream paramInputStream)
/*     */     throws CertificateException;
/*     */ 
/*     */   public CertPath engineGenerateCertPath(InputStream paramInputStream)
/*     */     throws CertificateException
/*     */   {
/* 127 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public CertPath engineGenerateCertPath(InputStream paramInputStream, String paramString)
/*     */     throws CertificateException
/*     */   {
/* 152 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public CertPath engineGenerateCertPath(List<? extends Certificate> paramList)
/*     */     throws CertificateException
/*     */   {
/* 179 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public Iterator<String> engineGetCertPathEncodings()
/*     */   {
/* 205 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public abstract Collection<? extends Certificate> engineGenerateCertificates(InputStream paramInputStream)
/*     */     throws CertificateException;
/*     */ 
/*     */   public abstract CRL engineGenerateCRL(InputStream paramInputStream)
/*     */     throws CRLException;
/*     */ 
/*     */   public abstract Collection<? extends CRL> engineGenerateCRLs(InputStream paramInputStream)
/*     */     throws CRLException;
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.security.cert.CertificateFactorySpi
 * JD-Core Version:    0.6.2
 */