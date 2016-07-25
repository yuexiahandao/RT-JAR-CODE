/*     */ package java.security;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.Serializable;
/*     */ import java.security.cert.CertPath;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ 
/*     */ public final class Timestamp
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -5502683707821851294L;
/*     */   private Date timestamp;
/*     */   private CertPath signerCertPath;
/*  66 */   private transient int myhash = -1;
/*     */ 
/*     */   public Timestamp(Date paramDate, CertPath paramCertPath)
/*     */   {
/*  76 */     if ((paramDate == null) || (paramCertPath == null)) {
/*  77 */       throw new NullPointerException();
/*     */     }
/*  79 */     this.timestamp = new Date(paramDate.getTime());
/*  80 */     this.signerCertPath = paramCertPath;
/*     */   }
/*     */ 
/*     */   public Date getTimestamp()
/*     */   {
/*  89 */     return new Date(this.timestamp.getTime());
/*     */   }
/*     */ 
/*     */   public CertPath getSignerCertPath()
/*     */   {
/*  98 */     return this.signerCertPath;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 109 */     if (this.myhash == -1) {
/* 110 */       this.myhash = (this.timestamp.hashCode() + this.signerCertPath.hashCode());
/*     */     }
/* 112 */     return this.myhash;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 125 */     if ((paramObject == null) || (!(paramObject instanceof Timestamp))) {
/* 126 */       return false;
/*     */     }
/* 128 */     Timestamp localTimestamp = (Timestamp)paramObject;
/*     */ 
/* 130 */     if (this == localTimestamp) {
/* 131 */       return true;
/*     */     }
/* 133 */     return (this.timestamp.equals(localTimestamp.getTimestamp())) && (this.signerCertPath.equals(localTimestamp.getSignerCertPath()));
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 144 */     StringBuffer localStringBuffer = new StringBuffer();
/* 145 */     localStringBuffer.append("(");
/* 146 */     localStringBuffer.append("timestamp: " + this.timestamp);
/* 147 */     List localList = this.signerCertPath.getCertificates();
/* 148 */     if (!localList.isEmpty())
/* 149 */       localStringBuffer.append("TSA: " + localList.get(0));
/*     */     else {
/* 151 */       localStringBuffer.append("TSA: <empty>");
/*     */     }
/* 153 */     localStringBuffer.append(")");
/* 154 */     return localStringBuffer.toString();
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream)
/*     */     throws IOException, ClassNotFoundException
/*     */   {
/* 160 */     paramObjectInputStream.defaultReadObject();
/* 161 */     this.myhash = -1;
/* 162 */     this.timestamp = new Date(this.timestamp.getTime());
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.security.Timestamp
 * JD-Core Version:    0.6.2
 */