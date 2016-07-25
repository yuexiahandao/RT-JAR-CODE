/*     */ package sun.security.provider.certpath;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.security.cert.Certificate;
/*     */ import java.security.cert.CertificateException;
/*     */ import java.security.cert.X509CertSelector;
/*     */ import java.security.cert.X509Certificate;
/*     */ import java.util.Date;
/*     */ import sun.security.util.DerOutputStream;
/*     */ import sun.security.x509.AuthorityKeyIdentifierExtension;
/*     */ import sun.security.x509.KeyIdentifier;
/*     */ import sun.security.x509.SerialNumber;
/*     */ 
/*     */ class AdaptableX509CertSelector extends X509CertSelector
/*     */ {
/*     */   private Date startDate;
/*     */   private Date endDate;
/*  55 */   private boolean isSKIDSensitive = false;
/*     */ 
/*  58 */   private boolean isSNSensitive = false;
/*     */ 
/*     */   void setValidityPeriod(Date paramDate1, Date paramDate2)
/*     */   {
/*  84 */     this.startDate = paramDate1;
/*  85 */     this.endDate = paramDate2;
/*     */   }
/*     */ 
/*     */   void parseAuthorityKeyIdentifierExtension(AuthorityKeyIdentifierExtension paramAuthorityKeyIdentifierExtension)
/*     */     throws IOException
/*     */   {
/* 103 */     if (paramAuthorityKeyIdentifierExtension != null) {
/* 104 */       KeyIdentifier localKeyIdentifier = (KeyIdentifier)paramAuthorityKeyIdentifierExtension.get("key_id");
/* 105 */       if (localKeyIdentifier != null)
/*     */       {
/* 107 */         if ((this.isSKIDSensitive) || (getSubjectKeyIdentifier() == null)) {
/* 108 */           localObject = new DerOutputStream();
/* 109 */           ((DerOutputStream)localObject).putOctetString(localKeyIdentifier.getIdentifier());
/* 110 */           super.setSubjectKeyIdentifier(((DerOutputStream)localObject).toByteArray());
/*     */ 
/* 112 */           this.isSKIDSensitive = true;
/*     */         }
/*     */       }
/*     */ 
/* 116 */       Object localObject = (SerialNumber)paramAuthorityKeyIdentifierExtension.get("serial_number");
/*     */ 
/* 118 */       if (localObject != null)
/*     */       {
/* 120 */         if ((this.isSNSensitive) || (getSerialNumber() == null)) {
/* 121 */           super.setSerialNumber(((SerialNumber)localObject).getNumber());
/* 122 */           this.isSNSensitive = true;
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean match(Certificate paramCertificate)
/*     */   {
/* 140 */     if (!(paramCertificate instanceof X509Certificate)) {
/* 141 */       return false;
/*     */     }
/*     */ 
/* 144 */     X509Certificate localX509Certificate = (X509Certificate)paramCertificate;
/* 145 */     int i = localX509Certificate.getVersion();
/*     */ 
/* 148 */     if (i < 3) {
/* 149 */       if (this.startDate != null) {
/*     */         try {
/* 151 */           localX509Certificate.checkValidity(this.startDate);
/*     */         } catch (CertificateException localCertificateException1) {
/* 153 */           return false;
/*     */         }
/*     */       }
/*     */ 
/* 157 */       if (this.endDate != null) {
/*     */         try {
/* 159 */           localX509Certificate.checkValidity(this.endDate);
/*     */         } catch (CertificateException localCertificateException2) {
/* 161 */           return false;
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 167 */     if ((this.isSKIDSensitive) && ((i < 3) || (localX509Certificate.getExtensionValue("2.5.29.14") == null)))
/*     */     {
/* 169 */       setSubjectKeyIdentifier(null);
/*     */     }
/*     */ 
/* 181 */     if ((this.isSNSensitive) && (i < 3)) {
/* 182 */       setSerialNumber(null);
/*     */     }
/*     */ 
/* 185 */     return super.match(paramCertificate);
/*     */   }
/*     */ 
/*     */   public Object clone()
/*     */   {
/* 190 */     AdaptableX509CertSelector localAdaptableX509CertSelector = (AdaptableX509CertSelector)super.clone();
/*     */ 
/* 192 */     if (this.startDate != null) {
/* 193 */       localAdaptableX509CertSelector.startDate = ((Date)this.startDate.clone());
/*     */     }
/*     */ 
/* 196 */     if (this.endDate != null) {
/* 197 */       localAdaptableX509CertSelector.endDate = ((Date)this.endDate.clone());
/*     */     }
/*     */ 
/* 200 */     return localAdaptableX509CertSelector;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.provider.certpath.AdaptableX509CertSelector
 * JD-Core Version:    0.6.2
 */