/*     */ package sun.security.provider.certpath;
/*     */ 
/*     */ import java.math.BigInteger;
/*     */ import java.security.cert.Certificate;
/*     */ import java.security.cert.CertificateException;
/*     */ import java.security.cert.X509Certificate;
/*     */ import java.util.Date;
/*     */ import sun.security.util.Debug;
/*     */ import sun.security.x509.AuthorityKeyIdentifierExtension;
/*     */ import sun.security.x509.KeyIdentifier;
/*     */ import sun.security.x509.SubjectKeyIdentifierExtension;
/*     */ import sun.security.x509.X509CertImpl;
/*     */ 
/*     */ public class Vertex
/*     */ {
/*  52 */   private static final Debug debug = Debug.getInstance("certpath");
/*     */   private Certificate cert;
/*     */   private int index;
/*     */   private Throwable throwable;
/*     */ 
/*     */   Vertex(Certificate paramCertificate)
/*     */   {
/*  64 */     this.cert = paramCertificate;
/*  65 */     this.index = -1;
/*     */   }
/*     */ 
/*     */   public Certificate getCertificate()
/*     */   {
/*  74 */     return this.cert;
/*     */   }
/*     */ 
/*     */   public int getIndex()
/*     */   {
/*  85 */     return this.index;
/*     */   }
/*     */ 
/*     */   void setIndex(int paramInt)
/*     */   {
/*  96 */     this.index = paramInt;
/*     */   }
/*     */ 
/*     */   public Throwable getThrowable()
/*     */   {
/* 106 */     return this.throwable;
/*     */   }
/*     */ 
/*     */   void setThrowable(Throwable paramThrowable)
/*     */   {
/* 116 */     this.throwable = paramThrowable;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 125 */     return certToString() + throwableToString() + indexToString();
/*     */   }
/*     */ 
/*     */   public String certToString()
/*     */   {
/* 135 */     String str = "";
/* 136 */     if ((this.cert == null) || (!(this.cert instanceof X509Certificate))) {
/* 137 */       return "Cert:       Not an X509Certificate\n";
/*     */     }
/* 139 */     X509CertImpl localX509CertImpl = null;
/*     */     try {
/* 141 */       localX509CertImpl = X509CertImpl.toImpl((X509Certificate)this.cert);
/*     */     } catch (CertificateException localCertificateException) {
/* 143 */       if (debug != null) {
/* 144 */         debug.println("Vertex.certToString() unexpected exception");
/* 145 */         localCertificateException.printStackTrace();
/*     */       }
/* 147 */       return str;
/*     */     }
/*     */ 
/* 150 */     str = "Issuer:     " + localX509CertImpl.getIssuerX500Principal() + "\n";
/* 151 */     str = str + "Subject:    " + localX509CertImpl.getSubjectX500Principal() + "\n";
/* 152 */     str = str + "SerialNum:  " + localX509CertImpl.getSerialNumber().toString(16) + "\n";
/* 153 */     str = str + "Expires:    " + localX509CertImpl.getNotAfter().toString() + "\n";
/* 154 */     boolean[] arrayOfBoolean1 = localX509CertImpl.getIssuerUniqueID();
/* 155 */     if (arrayOfBoolean1 != null) {
/* 156 */       str = str + "IssuerUID:  ";
/* 157 */       for (int i = 0; i < arrayOfBoolean1.length; i++) {
/* 158 */         str = str + (arrayOfBoolean1[i] != 0 ? 1 : 0);
/*     */       }
/* 160 */       str = str + "\n";
/*     */     }
/* 162 */     boolean[] arrayOfBoolean2 = localX509CertImpl.getSubjectUniqueID();
/* 163 */     if (arrayOfBoolean2 != null) {
/* 164 */       str = str + "SubjectUID: ";
/* 165 */       for (int j = 0; j < arrayOfBoolean2.length; j++) {
/* 166 */         str = str + (arrayOfBoolean2[j] != 0 ? 1 : 0);
/*     */       }
/* 168 */       str = str + "\n";
/*     */     }
/* 170 */     SubjectKeyIdentifierExtension localSubjectKeyIdentifierExtension = null;
/*     */     try {
/* 172 */       localSubjectKeyIdentifierExtension = localX509CertImpl.getSubjectKeyIdentifierExtension();
/* 173 */       if (localSubjectKeyIdentifierExtension != null) {
/* 174 */         KeyIdentifier localKeyIdentifier1 = (KeyIdentifier)localSubjectKeyIdentifierExtension.get("key_id");
/* 175 */         str = str + "SubjKeyID:  " + localKeyIdentifier1.toString();
/*     */       }
/*     */     } catch (Exception localException1) {
/* 178 */       if (debug != null) {
/* 179 */         debug.println("Vertex.certToString() unexpected exception");
/* 180 */         localException1.printStackTrace();
/*     */       }
/*     */     }
/* 183 */     AuthorityKeyIdentifierExtension localAuthorityKeyIdentifierExtension = null;
/*     */     try {
/* 185 */       localAuthorityKeyIdentifierExtension = localX509CertImpl.getAuthorityKeyIdentifierExtension();
/* 186 */       if (localAuthorityKeyIdentifierExtension != null) {
/* 187 */         KeyIdentifier localKeyIdentifier2 = (KeyIdentifier)localAuthorityKeyIdentifierExtension.get("key_id");
/* 188 */         str = str + "AuthKeyID:  " + localKeyIdentifier2.toString();
/*     */       }
/*     */     } catch (Exception localException2) {
/* 191 */       if (debug != null) {
/* 192 */         debug.println("Vertex.certToString() 2 unexpected exception");
/* 193 */         localException2.printStackTrace();
/*     */       }
/*     */     }
/* 196 */     return str;
/*     */   }
/*     */ 
/*     */   public String throwableToString()
/*     */   {
/* 206 */     String str = "Exception:  ";
/* 207 */     if (this.throwable != null)
/* 208 */       str = str + this.throwable.toString();
/*     */     else
/* 210 */       str = str + "null";
/* 211 */     str = str + "\n";
/* 212 */     return str;
/*     */   }
/*     */ 
/*     */   public String moreToString()
/*     */   {
/* 223 */     String str = "Last cert?  ";
/* 224 */     str = str + (this.index == -1 ? "Yes" : "No");
/* 225 */     str = str + "\n";
/* 226 */     return str;
/*     */   }
/*     */ 
/*     */   public String indexToString()
/*     */   {
/* 236 */     String str = "Index:      " + this.index + "\n";
/* 237 */     return str;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.provider.certpath.Vertex
 * JD-Core Version:    0.6.2
 */