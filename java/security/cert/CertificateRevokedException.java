/*     */ package java.security.cert;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.util.Collections;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import javax.security.auth.x500.X500Principal;
/*     */ import sun.security.util.ObjectIdentifier;
/*     */ import sun.security.x509.InvalidityDateExtension;
/*     */ 
/*     */ public class CertificateRevokedException extends CertificateException
/*     */ {
/*     */   private static final long serialVersionUID = 7839996631571608627L;
/*     */   private Date revocationDate;
/*     */   private final CRLReason reason;
/*     */   private final X500Principal authority;
/*     */   private transient Map<String, Extension> extensions;
/*     */ 
/*     */   public CertificateRevokedException(Date paramDate, CRLReason paramCRLReason, X500Principal paramX500Principal, Map<String, Extension> paramMap)
/*     */   {
/*  91 */     if ((paramDate == null) || (paramCRLReason == null) || (paramX500Principal == null) || (paramMap == null))
/*     */     {
/*  93 */       throw new NullPointerException();
/*     */     }
/*  95 */     this.revocationDate = new Date(paramDate.getTime());
/*  96 */     this.reason = paramCRLReason;
/*  97 */     this.authority = paramX500Principal;
/*     */ 
/*  99 */     this.extensions = Collections.checkedMap(new HashMap(), String.class, Extension.class);
/*     */ 
/* 101 */     this.extensions.putAll(paramMap);
/*     */   }
/*     */ 
/*     */   public Date getRevocationDate()
/*     */   {
/* 112 */     return (Date)this.revocationDate.clone();
/*     */   }
/*     */ 
/*     */   public CRLReason getRevocationReason()
/*     */   {
/* 121 */     return this.reason;
/*     */   }
/*     */ 
/*     */   public X500Principal getAuthorityName()
/*     */   {
/* 132 */     return this.authority;
/*     */   }
/*     */ 
/*     */   public Date getInvalidityDate()
/*     */   {
/* 149 */     Extension localExtension = (Extension)getExtensions().get("2.5.29.24");
/* 150 */     if (localExtension == null)
/* 151 */       return null;
/*     */     try
/*     */     {
/* 154 */       Date localDate = (Date)InvalidityDateExtension.toImpl(localExtension).get("DATE");
/*     */ 
/* 156 */       return new Date(localDate.getTime()); } catch (IOException localIOException) {
/*     */     }
/* 158 */     return null;
/*     */   }
/*     */ 
/*     */   public Map<String, Extension> getExtensions()
/*     */   {
/* 173 */     return Collections.unmodifiableMap(this.extensions);
/*     */   }
/*     */ 
/*     */   public String getMessage()
/*     */   {
/* 178 */     return "Certificate has been revoked, reason: " + this.reason + ", revocation date: " + this.revocationDate + ", authority: " + this.authority + ", extension OIDs: " + this.extensions.keySet();
/*     */   }
/*     */ 
/*     */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*     */     throws IOException
/*     */   {
/* 196 */     paramObjectOutputStream.defaultWriteObject();
/*     */ 
/* 199 */     paramObjectOutputStream.writeInt(this.extensions.size());
/*     */ 
/* 206 */     for (Map.Entry localEntry : this.extensions.entrySet()) {
/* 207 */       Extension localExtension = (Extension)localEntry.getValue();
/* 208 */       paramObjectOutputStream.writeObject(localExtension.getId());
/* 209 */       paramObjectOutputStream.writeBoolean(localExtension.isCritical());
/* 210 */       byte[] arrayOfByte = localExtension.getValue();
/* 211 */       paramObjectOutputStream.writeInt(arrayOfByte.length);
/* 212 */       paramObjectOutputStream.write(arrayOfByte);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream)
/*     */     throws IOException, ClassNotFoundException
/*     */   {
/* 223 */     paramObjectInputStream.defaultReadObject();
/*     */ 
/* 226 */     this.revocationDate = new Date(this.revocationDate.getTime());
/*     */ 
/* 230 */     int i = paramObjectInputStream.readInt();
/* 231 */     if (i == 0)
/* 232 */       this.extensions = Collections.emptyMap();
/*     */     else {
/* 234 */       this.extensions = new HashMap(i);
/*     */     }
/*     */ 
/* 238 */     for (int j = 0; j < i; j++) {
/* 239 */       String str = (String)paramObjectInputStream.readObject();
/* 240 */       boolean bool = paramObjectInputStream.readBoolean();
/* 241 */       int k = paramObjectInputStream.readInt();
/* 242 */       byte[] arrayOfByte = new byte[k];
/* 243 */       paramObjectInputStream.readFully(arrayOfByte);
/* 244 */       sun.security.x509.Extension localExtension = sun.security.x509.Extension.newExtension(new ObjectIdentifier(str), bool, arrayOfByte);
/*     */ 
/* 246 */       this.extensions.put(str, localExtension);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.security.cert.CertificateRevokedException
 * JD-Core Version:    0.6.2
 */