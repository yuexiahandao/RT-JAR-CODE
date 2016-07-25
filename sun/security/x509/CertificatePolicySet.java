/*     */ package sun.security.x509;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Vector;
/*     */ import sun.security.util.DerInputStream;
/*     */ import sun.security.util.DerOutputStream;
/*     */ import sun.security.util.DerValue;
/*     */ 
/*     */ public class CertificatePolicySet
/*     */ {
/*     */   private final Vector<CertificatePolicyId> ids;
/*     */ 
/*     */   public CertificatePolicySet(Vector<CertificatePolicyId> paramVector)
/*     */   {
/*  51 */     this.ids = paramVector;
/*     */   }
/*     */ 
/*     */   public CertificatePolicySet(DerInputStream paramDerInputStream)
/*     */     throws IOException
/*     */   {
/*  61 */     this.ids = new Vector();
/*  62 */     DerValue[] arrayOfDerValue = paramDerInputStream.getSequence(5);
/*     */ 
/*  64 */     for (int i = 0; i < arrayOfDerValue.length; i++) {
/*  65 */       CertificatePolicyId localCertificatePolicyId = new CertificatePolicyId(arrayOfDerValue[i]);
/*  66 */       this.ids.addElement(localCertificatePolicyId);
/*     */     }
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/*  74 */     String str = "CertificatePolicySet:[\n" + this.ids.toString() + "]\n";
/*     */ 
/*  78 */     return str;
/*     */   }
/*     */ 
/*     */   public void encode(DerOutputStream paramDerOutputStream)
/*     */     throws IOException
/*     */   {
/*  87 */     DerOutputStream localDerOutputStream = new DerOutputStream();
/*     */ 
/*  89 */     for (int i = 0; i < this.ids.size(); i++) {
/*  90 */       ((CertificatePolicyId)this.ids.elementAt(i)).encode(localDerOutputStream);
/*     */     }
/*  92 */     paramDerOutputStream.write((byte)48, localDerOutputStream);
/*     */   }
/*     */ 
/*     */   public List<CertificatePolicyId> getCertPolicyIds()
/*     */   {
/* 102 */     return Collections.unmodifiableList(this.ids);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.x509.CertificatePolicySet
 * JD-Core Version:    0.6.2
 */