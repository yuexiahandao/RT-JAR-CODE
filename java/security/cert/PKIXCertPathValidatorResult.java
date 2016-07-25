/*     */ package java.security.cert;
/*     */ 
/*     */ import java.security.PublicKey;
/*     */ 
/*     */ public class PKIXCertPathValidatorResult
/*     */   implements CertPathValidatorResult
/*     */ {
/*     */   private TrustAnchor trustAnchor;
/*     */   private PolicyNode policyTree;
/*     */   private PublicKey subjectPublicKey;
/*     */ 
/*     */   public PKIXCertPathValidatorResult(TrustAnchor paramTrustAnchor, PolicyNode paramPolicyNode, PublicKey paramPublicKey)
/*     */   {
/*  79 */     if (paramPublicKey == null)
/*  80 */       throw new NullPointerException("subjectPublicKey must be non-null");
/*  81 */     if (paramTrustAnchor == null)
/*  82 */       throw new NullPointerException("trustAnchor must be non-null");
/*  83 */     this.trustAnchor = paramTrustAnchor;
/*  84 */     this.policyTree = paramPolicyNode;
/*  85 */     this.subjectPublicKey = paramPublicKey;
/*     */   }
/*     */ 
/*     */   public TrustAnchor getTrustAnchor()
/*     */   {
/*  95 */     return this.trustAnchor;
/*     */   }
/*     */ 
/*     */   public PolicyNode getPolicyTree()
/*     */   {
/* 116 */     return this.policyTree;
/*     */   }
/*     */ 
/*     */   public PublicKey getPublicKey()
/*     */   {
/* 126 */     return this.subjectPublicKey;
/*     */   }
/*     */ 
/*     */   public Object clone()
/*     */   {
/*     */     try
/*     */     {
/* 136 */       return super.clone();
/*     */     }
/*     */     catch (CloneNotSupportedException localCloneNotSupportedException) {
/* 139 */       throw new InternalError(localCloneNotSupportedException.toString());
/*     */     }
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 151 */     StringBuffer localStringBuffer = new StringBuffer();
/* 152 */     localStringBuffer.append("PKIXCertPathValidatorResult: [\n");
/* 153 */     localStringBuffer.append("  Trust Anchor: " + this.trustAnchor.toString() + "\n");
/* 154 */     localStringBuffer.append("  Policy Tree: " + String.valueOf(this.policyTree) + "\n");
/* 155 */     localStringBuffer.append("  Subject Public Key: " + this.subjectPublicKey + "\n");
/* 156 */     localStringBuffer.append("]");
/* 157 */     return localStringBuffer.toString();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.security.cert.PKIXCertPathValidatorResult
 * JD-Core Version:    0.6.2
 */