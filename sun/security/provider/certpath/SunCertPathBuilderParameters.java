/*     */ package sun.security.provider.certpath;
/*     */ 
/*     */ import java.security.InvalidAlgorithmParameterException;
/*     */ import java.security.KeyStore;
/*     */ import java.security.KeyStoreException;
/*     */ import java.security.cert.CertSelector;
/*     */ import java.security.cert.PKIXBuilderParameters;
/*     */ import java.security.cert.TrustAnchor;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class SunCertPathBuilderParameters extends PKIXBuilderParameters
/*     */ {
/*  52 */   private boolean buildForward = true;
/*     */ 
/*     */   public SunCertPathBuilderParameters(Set<TrustAnchor> paramSet, CertSelector paramCertSelector)
/*     */     throws InvalidAlgorithmParameterException
/*     */   {
/*  71 */     super(paramSet, paramCertSelector);
/*  72 */     setBuildForward(true);
/*     */   }
/*     */ 
/*     */   public SunCertPathBuilderParameters(KeyStore paramKeyStore, CertSelector paramCertSelector)
/*     */     throws KeyStoreException, InvalidAlgorithmParameterException
/*     */   {
/*  93 */     super(paramKeyStore, paramCertSelector);
/*  94 */     setBuildForward(true);
/*     */   }
/*     */ 
/*     */   public boolean getBuildForward()
/*     */   {
/* 103 */     return this.buildForward;
/*     */   }
/*     */ 
/*     */   public void setBuildForward(boolean paramBoolean)
/*     */   {
/* 115 */     this.buildForward = paramBoolean;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 124 */     StringBuffer localStringBuffer = new StringBuffer();
/* 125 */     localStringBuffer.append("[\n");
/* 126 */     localStringBuffer.append(super.toString());
/* 127 */     localStringBuffer.append("  Build Forward Flag: " + String.valueOf(this.buildForward) + "\n");
/* 128 */     localStringBuffer.append("]\n");
/* 129 */     return localStringBuffer.toString();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.provider.certpath.SunCertPathBuilderParameters
 * JD-Core Version:    0.6.2
 */