/*     */ package sun.security.pkcs;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import sun.misc.HexDumpEncoder;
/*     */ import sun.security.util.DerInputStream;
/*     */ import sun.security.util.DerValue;
/*     */ import sun.security.x509.GeneralNames;
/*     */ import sun.security.x509.SerialNumber;
/*     */ 
/*     */ class ESSCertId
/*     */ {
/*     */   private static volatile HexDumpEncoder hexDumper;
/*     */   private byte[] certHash;
/*     */   private GeneralNames issuer;
/*     */   private SerialNumber serialNumber;
/*     */ 
/*     */   ESSCertId(DerValue paramDerValue)
/*     */     throws IOException
/*     */   {
/* 141 */     this.certHash = paramDerValue.data.getDerValue().toByteArray();
/*     */ 
/* 144 */     if (paramDerValue.data.available() > 0) {
/* 145 */       DerValue localDerValue = paramDerValue.data.getDerValue();
/*     */ 
/* 147 */       this.issuer = new GeneralNames(localDerValue.data.getDerValue());
/*     */ 
/* 149 */       this.serialNumber = new SerialNumber(localDerValue.data.getDerValue());
/*     */     }
/*     */   }
/*     */ 
/*     */   public String toString() {
/* 154 */     StringBuffer localStringBuffer = new StringBuffer();
/* 155 */     localStringBuffer.append("[\n\tCertificate hash (SHA-1):\n");
/* 156 */     if (hexDumper == null) {
/* 157 */       hexDumper = new HexDumpEncoder();
/*     */     }
/* 159 */     localStringBuffer.append(hexDumper.encode(this.certHash));
/* 160 */     if ((this.issuer != null) && (this.serialNumber != null)) {
/* 161 */       localStringBuffer.append("\n\tIssuer: " + this.issuer + "\n");
/* 162 */       localStringBuffer.append("\t" + this.serialNumber);
/*     */     }
/* 164 */     localStringBuffer.append("\n]");
/* 165 */     return localStringBuffer.toString();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.pkcs.ESSCertId
 * JD-Core Version:    0.6.2
 */