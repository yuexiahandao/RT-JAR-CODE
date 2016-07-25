/*     */ package java.security.cert;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.NotSerializableException;
/*     */ import java.io.ObjectStreamException;
/*     */ import java.io.Serializable;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ 
/*     */ public abstract class CertPath
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 6068470306649138683L;
/*     */   private String type;
/*     */ 
/*     */   protected CertPath(String paramString)
/*     */   {
/* 136 */     this.type = paramString;
/*     */   }
/*     */ 
/*     */   public String getType()
/*     */   {
/* 149 */     return this.type;
/*     */   }
/*     */ 
/*     */   public abstract Iterator<String> getEncodings();
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 179 */     if (this == paramObject) {
/* 180 */       return true;
/*     */     }
/* 182 */     if (!(paramObject instanceof CertPath)) {
/* 183 */       return false;
/*     */     }
/* 185 */     CertPath localCertPath = (CertPath)paramObject;
/* 186 */     if (!localCertPath.getType().equals(this.type)) {
/* 187 */       return false;
/*     */     }
/* 189 */     List localList1 = getCertificates();
/* 190 */     List localList2 = localCertPath.getCertificates();
/* 191 */     return localList1.equals(localList2);
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 210 */     int i = this.type.hashCode();
/* 211 */     i = 31 * i + getCertificates().hashCode();
/* 212 */     return i;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 223 */     StringBuffer localStringBuffer = new StringBuffer();
/* 224 */     Iterator localIterator = getCertificates().iterator();
/*     */ 
/* 227 */     localStringBuffer.append("\n" + this.type + " Cert Path: length = " + getCertificates().size() + ".\n");
/*     */ 
/* 229 */     localStringBuffer.append("[\n");
/* 230 */     int i = 1;
/* 231 */     while (localIterator.hasNext()) {
/* 232 */       localStringBuffer.append("=========================================================Certificate " + i + " start.\n");
/*     */ 
/* 234 */       Certificate localCertificate = (Certificate)localIterator.next();
/* 235 */       localStringBuffer.append(localCertificate.toString());
/* 236 */       localStringBuffer.append("\n=========================================================Certificate " + i + " end.\n\n\n");
/*     */ 
/* 238 */       i++;
/*     */     }
/*     */ 
/* 241 */     localStringBuffer.append("\n]");
/* 242 */     return localStringBuffer.toString();
/*     */   }
/*     */ 
/*     */   public abstract byte[] getEncoded()
/*     */     throws CertificateEncodingException;
/*     */ 
/*     */   public abstract byte[] getEncoded(String paramString)
/*     */     throws CertificateEncodingException;
/*     */ 
/*     */   public abstract List<? extends Certificate> getCertificates();
/*     */ 
/*     */   protected Object writeReplace()
/*     */     throws ObjectStreamException
/*     */   {
/*     */     try
/*     */     {
/* 287 */       return new CertPathRep(this.type, getEncoded());
/*     */     } catch (CertificateException localCertificateException) {
/* 289 */       NotSerializableException localNotSerializableException = new NotSerializableException("java.security.cert.CertPath: " + this.type);
/*     */ 
/* 292 */       localNotSerializableException.initCause(localCertificateException);
/* 293 */       throw localNotSerializableException;
/*     */     }
/*     */   }
/*     */ 
/*     */   protected static class CertPathRep
/*     */     implements Serializable
/*     */   {
/*     */     private static final long serialVersionUID = 3015633072427920915L;
/*     */     private String type;
/*     */     private byte[] data;
/*     */ 
/*     */     protected CertPathRep(String paramString, byte[] paramArrayOfByte)
/*     */     {
/* 318 */       this.type = paramString;
/* 319 */       this.data = paramArrayOfByte;
/*     */     }
/*     */ 
/*     */     protected Object readResolve()
/*     */       throws ObjectStreamException
/*     */     {
/*     */       try
/*     */       {
/* 332 */         CertificateFactory localCertificateFactory = CertificateFactory.getInstance(this.type);
/* 333 */         return localCertificateFactory.generateCertPath(new ByteArrayInputStream(this.data));
/*     */       } catch (CertificateException localCertificateException) {
/* 335 */         NotSerializableException localNotSerializableException = new NotSerializableException("java.security.cert.CertPath: " + this.type);
/*     */ 
/* 338 */         localNotSerializableException.initCause(localCertificateException);
/* 339 */         throw localNotSerializableException;
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.security.cert.CertPath
 * JD-Core Version:    0.6.2
 */