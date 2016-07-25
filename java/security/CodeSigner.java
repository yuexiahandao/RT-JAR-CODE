/*     */ package java.security;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.Serializable;
/*     */ import java.security.cert.CertPath;
/*     */ import java.util.List;
/*     */ 
/*     */ public final class CodeSigner
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 6819288105193937581L;
/*     */   private CertPath signerCertPath;
/*     */   private Timestamp timestamp;
/*  60 */   private transient int myhash = -1;
/*     */ 
/*     */   public CodeSigner(CertPath paramCertPath, Timestamp paramTimestamp)
/*     */   {
/*  74 */     if (paramCertPath == null) {
/*  75 */       throw new NullPointerException();
/*     */     }
/*  77 */     this.signerCertPath = paramCertPath;
/*  78 */     this.timestamp = paramTimestamp;
/*     */   }
/*     */ 
/*     */   public CertPath getSignerCertPath()
/*     */   {
/*  87 */     return this.signerCertPath;
/*     */   }
/*     */ 
/*     */   public Timestamp getTimestamp()
/*     */   {
/*  96 */     return this.timestamp;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 107 */     if (this.myhash == -1) {
/* 108 */       if (this.timestamp == null)
/* 109 */         this.myhash = this.signerCertPath.hashCode();
/*     */       else {
/* 111 */         this.myhash = (this.signerCertPath.hashCode() + this.timestamp.hashCode());
/*     */       }
/*     */     }
/* 114 */     return this.myhash;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 128 */     if ((paramObject == null) || (!(paramObject instanceof CodeSigner))) {
/* 129 */       return false;
/*     */     }
/* 131 */     CodeSigner localCodeSigner = (CodeSigner)paramObject;
/*     */ 
/* 133 */     if (this == localCodeSigner) {
/* 134 */       return true;
/*     */     }
/* 136 */     Timestamp localTimestamp = localCodeSigner.getTimestamp();
/* 137 */     if (this.timestamp == null) {
/* 138 */       if (localTimestamp != null) {
/* 139 */         return false;
/*     */       }
/*     */     }
/* 142 */     else if ((localTimestamp == null) || (!this.timestamp.equals(localTimestamp)))
/*     */     {
/* 144 */       return false;
/*     */     }
/*     */ 
/* 147 */     return this.signerCertPath.equals(localCodeSigner.getSignerCertPath());
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 157 */     StringBuffer localStringBuffer = new StringBuffer();
/* 158 */     localStringBuffer.append("(");
/* 159 */     localStringBuffer.append("Signer: " + this.signerCertPath.getCertificates().get(0));
/* 160 */     if (this.timestamp != null) {
/* 161 */       localStringBuffer.append("timestamp: " + this.timestamp);
/*     */     }
/* 163 */     localStringBuffer.append(")");
/* 164 */     return localStringBuffer.toString();
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream)
/*     */     throws IOException, ClassNotFoundException
/*     */   {
/* 170 */     paramObjectInputStream.defaultReadObject();
/* 171 */     this.myhash = -1;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.security.CodeSigner
 * JD-Core Version:    0.6.2
 */