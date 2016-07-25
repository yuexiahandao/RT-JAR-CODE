/*     */ package sun.security.x509;
/*     */ 
/*     */ @Deprecated
/*     */ public class CertException extends SecurityException
/*     */ {
/*     */   private static final long serialVersionUID = 6930793039696446142L;
/*     */   public static final int verf_INVALID_SIG = 1;
/*     */   public static final int verf_INVALID_REVOKED = 2;
/*     */   public static final int verf_INVALID_NOTBEFORE = 3;
/*     */   public static final int verf_INVALID_EXPIRED = 4;
/*     */   public static final int verf_CA_UNTRUSTED = 5;
/*     */   public static final int verf_CHAIN_LENGTH = 6;
/*     */   public static final int verf_PARSE_ERROR = 7;
/*     */   public static final int err_CONSTRUCTION = 8;
/*     */   public static final int err_INVALID_PUBLIC_KEY = 9;
/*     */   public static final int err_INVALID_VERSION = 10;
/*     */   public static final int err_INVALID_FORMAT = 11;
/*     */   public static final int err_ENCODING = 12;
/*     */   private int verfCode;
/*     */   private String moreData;
/*     */ 
/*     */   public CertException(int paramInt, String paramString)
/*     */   {
/*  95 */     this.verfCode = paramInt;
/*  96 */     this.moreData = paramString;
/*     */   }
/*     */ 
/*     */   public CertException(int paramInt)
/*     */   {
/* 105 */     this.verfCode = paramInt;
/*     */   }
/*     */ 
/*     */   public int getVerfCode()
/*     */   {
/* 111 */     return this.verfCode;
/*     */   }
/*     */ 
/*     */   public String getMoreData()
/*     */   {
/* 117 */     return this.moreData;
/*     */   }
/*     */ 
/*     */   public String getVerfDescription()
/*     */   {
/* 125 */     switch (this.verfCode) {
/*     */     case 1:
/* 127 */       return "The signature in the certificate is not valid.";
/*     */     case 2:
/* 129 */       return "The certificate has been revoked.";
/*     */     case 3:
/* 131 */       return "The certificate is not yet valid.";
/*     */     case 4:
/* 133 */       return "The certificate has expired.";
/*     */     case 5:
/* 135 */       return "The Authority which issued the certificate is not trusted.";
/*     */     case 6:
/* 137 */       return "The certificate path to a trusted authority is too long.";
/*     */     case 7:
/* 139 */       return "The certificate could not be parsed.";
/*     */     case 8:
/* 141 */       return "There was an error when constructing the certificate.";
/*     */     case 9:
/* 143 */       return "The public key was not in the correct format.";
/*     */     case 10:
/* 145 */       return "The certificate has an invalid version number.";
/*     */     case 11:
/* 147 */       return "The certificate has an invalid format.";
/*     */     case 12:
/* 149 */       return "Problem encountered while encoding the data.";
/*     */     }
/*     */ 
/* 152 */     return "Unknown code:  " + this.verfCode;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 161 */     return "[Certificate Exception: " + getMessage() + "]";
/*     */   }
/*     */ 
/*     */   public String getMessage()
/*     */   {
/* 169 */     return getVerfDescription() + (this.moreData != null ? "\n  (" + this.moreData + ")" : "");
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.x509.CertException
 * JD-Core Version:    0.6.2
 */