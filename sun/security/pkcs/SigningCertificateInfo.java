/*     */ package sun.security.pkcs;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import sun.security.util.DerInputStream;
/*     */ import sun.security.util.DerValue;
/*     */ 
/*     */ public class SigningCertificateInfo
/*     */ {
/*  86 */   private byte[] ber = null;
/*     */ 
/*  88 */   private ESSCertId[] certId = null;
/*     */ 
/*     */   public SigningCertificateInfo(byte[] paramArrayOfByte) throws IOException {
/*  91 */     parse(paramArrayOfByte);
/*     */   }
/*     */ 
/*     */   public String toString() {
/*  95 */     StringBuffer localStringBuffer = new StringBuffer();
/*  96 */     localStringBuffer.append("[\n");
/*  97 */     for (int i = 0; i < this.certId.length; i++) {
/*  98 */       localStringBuffer.append(this.certId[i].toString());
/*     */     }
/*     */ 
/* 101 */     localStringBuffer.append("\n]");
/*     */ 
/* 103 */     return localStringBuffer.toString();
/*     */   }
/*     */ 
/*     */   public void parse(byte[] paramArrayOfByte)
/*     */     throws IOException
/*     */   {
/* 109 */     DerValue localDerValue = new DerValue(paramArrayOfByte);
/* 110 */     if (localDerValue.tag != 48) {
/* 111 */       throw new IOException("Bad encoding for signingCertificate");
/*     */     }
/*     */ 
/* 115 */     DerValue[] arrayOfDerValue1 = localDerValue.data.getSequence(1);
/* 116 */     this.certId = new ESSCertId[arrayOfDerValue1.length];
/* 117 */     for (int i = 0; i < arrayOfDerValue1.length; i++) {
/* 118 */       this.certId[i] = new ESSCertId(arrayOfDerValue1[i]);
/*     */     }
/*     */ 
/* 122 */     if (localDerValue.data.available() > 0) {
/* 123 */       DerValue[] arrayOfDerValue2 = localDerValue.data.getSequence(1);
/* 124 */       for (int j = 0; j < arrayOfDerValue2.length; j++);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.pkcs.SigningCertificateInfo
 * JD-Core Version:    0.6.2
 */