/*     */ package sun.security.x509;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import sun.security.util.DerOutputStream;
/*     */ import sun.security.util.DerValue;
/*     */ import sun.security.util.ObjectIdentifier;
/*     */ 
/*     */ public class CertificatePolicyId
/*     */ {
/*     */   private ObjectIdentifier id;
/*     */ 
/*     */   public CertificatePolicyId(ObjectIdentifier paramObjectIdentifier)
/*     */   {
/*  47 */     this.id = paramObjectIdentifier;
/*     */   }
/*     */ 
/*     */   public CertificatePolicyId(DerValue paramDerValue)
/*     */     throws IOException
/*     */   {
/*  56 */     this.id = paramDerValue.getOID();
/*     */   }
/*     */ 
/*     */   public ObjectIdentifier getIdentifier()
/*     */   {
/*  63 */     return this.id;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/*  70 */     String str = "CertificatePolicyId: [" + this.id.toString() + "]\n";
/*     */ 
/*  74 */     return str;
/*     */   }
/*     */ 
/*     */   public void encode(DerOutputStream paramDerOutputStream)
/*     */     throws IOException
/*     */   {
/*  84 */     paramDerOutputStream.putOID(this.id);
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/*  95 */     if ((paramObject instanceof CertificatePolicyId)) {
/*  96 */       return this.id.equals(((CertificatePolicyId)paramObject).getIdentifier());
/*     */     }
/*  98 */     return false;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 107 */     return this.id.hashCode();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.x509.CertificatePolicyId
 * JD-Core Version:    0.6.2
 */