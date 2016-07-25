/*     */ package java.security.cert;
/*     */ 
/*     */ import java.security.InvalidAlgorithmParameterException;
/*     */ import java.security.InvalidParameterException;
/*     */ import java.security.KeyStore;
/*     */ import java.security.KeyStoreException;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class PKIXBuilderParameters extends PKIXParameters
/*     */ {
/*  80 */   private int maxPathLength = 5;
/*     */ 
/*     */   public PKIXBuilderParameters(Set<TrustAnchor> paramSet, CertSelector paramCertSelector)
/*     */     throws InvalidAlgorithmParameterException
/*     */   {
/* 104 */     super(paramSet);
/* 105 */     setTargetCertConstraints(paramCertSelector);
/*     */   }
/*     */ 
/*     */   public PKIXBuilderParameters(KeyStore paramKeyStore, CertSelector paramCertSelector)
/*     */     throws KeyStoreException, InvalidAlgorithmParameterException
/*     */   {
/* 130 */     super(paramKeyStore);
/* 131 */     setTargetCertConstraints(paramCertSelector);
/*     */   }
/*     */ 
/*     */   public void setMaxPathLength(int paramInt)
/*     */   {
/* 165 */     if (paramInt < -1) {
/* 166 */       throw new InvalidParameterException("the maximum path length parameter can not be less than -1");
/*     */     }
/*     */ 
/* 169 */     this.maxPathLength = paramInt;
/*     */   }
/*     */ 
/*     */   public int getMaxPathLength()
/*     */   {
/* 183 */     return this.maxPathLength;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 192 */     StringBuffer localStringBuffer = new StringBuffer();
/* 193 */     localStringBuffer.append("[\n");
/* 194 */     localStringBuffer.append(super.toString());
/* 195 */     localStringBuffer.append("  Maximum Path Length: " + this.maxPathLength + "\n");
/* 196 */     localStringBuffer.append("]\n");
/* 197 */     return localStringBuffer.toString();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.security.cert.PKIXBuilderParameters
 * JD-Core Version:    0.6.2
 */