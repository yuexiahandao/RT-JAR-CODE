/*     */ package sun.security.x509;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.security.cert.CertificateException;
/*     */ import java.security.cert.CertificateExpiredException;
/*     */ import java.security.cert.CertificateNotYetValidException;
/*     */ import java.security.cert.CertificateParsingException;
/*     */ import java.util.Date;
/*     */ import java.util.Enumeration;
/*     */ import sun.security.util.DerInputStream;
/*     */ import sun.security.util.DerOutputStream;
/*     */ import sun.security.util.DerValue;
/*     */ 
/*     */ public class PrivateKeyUsageExtension extends Extension
/*     */   implements CertAttrSet<String>
/*     */ {
/*     */   public static final String IDENT = "x509.info.extensions.PrivateKeyUsage";
/*     */   public static final String NAME = "PrivateKeyUsage";
/*     */   public static final String NOT_BEFORE = "not_before";
/*     */   public static final String NOT_AFTER = "not_after";
/*     */   private static final byte TAG_BEFORE = 0;
/*     */   private static final byte TAG_AFTER = 1;
/*  79 */   private Date notBefore = null;
/*  80 */   private Date notAfter = null;
/*     */ 
/*     */   private void encodeThis() throws IOException
/*     */   {
/*  84 */     if ((this.notBefore == null) && (this.notAfter == null)) {
/*  85 */       this.extensionValue = null;
/*  86 */       return;
/*     */     }
/*  88 */     DerOutputStream localDerOutputStream1 = new DerOutputStream();
/*     */ 
/*  90 */     DerOutputStream localDerOutputStream2 = new DerOutputStream();
/*     */     DerOutputStream localDerOutputStream3;
/*  91 */     if (this.notBefore != null) {
/*  92 */       localDerOutputStream3 = new DerOutputStream();
/*  93 */       localDerOutputStream3.putGeneralizedTime(this.notBefore);
/*  94 */       localDerOutputStream2.writeImplicit(DerValue.createTag((byte)-128, false, (byte)0), localDerOutputStream3);
/*     */     }
/*     */ 
/*  97 */     if (this.notAfter != null) {
/*  98 */       localDerOutputStream3 = new DerOutputStream();
/*  99 */       localDerOutputStream3.putGeneralizedTime(this.notAfter);
/* 100 */       localDerOutputStream2.writeImplicit(DerValue.createTag((byte)-128, false, (byte)1), localDerOutputStream3);
/*     */     }
/*     */ 
/* 103 */     localDerOutputStream1.write((byte)48, localDerOutputStream2);
/* 104 */     this.extensionValue = localDerOutputStream1.toByteArray();
/*     */   }
/*     */ 
/*     */   public PrivateKeyUsageExtension(Date paramDate1, Date paramDate2)
/*     */     throws IOException
/*     */   {
/* 117 */     this.notBefore = paramDate1;
/* 118 */     this.notAfter = paramDate2;
/*     */ 
/* 120 */     this.extensionId = PKIXExtensions.PrivateKeyUsage_Id;
/* 121 */     this.critical = false;
/* 122 */     encodeThis();
/*     */   }
/*     */ 
/*     */   public PrivateKeyUsageExtension(Boolean paramBoolean, Object paramObject)
/*     */     throws CertificateException, IOException
/*     */   {
/* 136 */     this.extensionId = PKIXExtensions.PrivateKeyUsage_Id;
/* 137 */     this.critical = paramBoolean.booleanValue();
/*     */ 
/* 139 */     this.extensionValue = ((byte[])paramObject);
/* 140 */     DerInputStream localDerInputStream = new DerInputStream(this.extensionValue);
/* 141 */     DerValue[] arrayOfDerValue = localDerInputStream.getSequence(2);
/*     */ 
/* 146 */     for (int i = 0; i < arrayOfDerValue.length; i++) {
/* 147 */       DerValue localDerValue = arrayOfDerValue[i];
/*     */ 
/* 149 */       if ((localDerValue.isContextSpecific((byte)0)) && (!localDerValue.isConstructed()))
/*     */       {
/* 151 */         if (this.notBefore != null) {
/* 152 */           throw new CertificateParsingException("Duplicate notBefore in PrivateKeyUsage.");
/*     */         }
/*     */ 
/* 155 */         localDerValue.resetTag((byte)24);
/* 156 */         localDerInputStream = new DerInputStream(localDerValue.toByteArray());
/* 157 */         this.notBefore = localDerInputStream.getGeneralizedTime();
/*     */       }
/* 159 */       else if ((localDerValue.isContextSpecific((byte)1)) && (!localDerValue.isConstructed()))
/*     */       {
/* 161 */         if (this.notAfter != null) {
/* 162 */           throw new CertificateParsingException("Duplicate notAfter in PrivateKeyUsage.");
/*     */         }
/*     */ 
/* 165 */         localDerValue.resetTag((byte)24);
/* 166 */         localDerInputStream = new DerInputStream(localDerValue.toByteArray());
/* 167 */         this.notAfter = localDerInputStream.getGeneralizedTime();
/*     */       } else {
/* 169 */         throw new IOException("Invalid encoding of PrivateKeyUsageExtension");
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 178 */     return super.toString() + "PrivateKeyUsage: [\n" + (this.notBefore == null ? "" : new StringBuilder().append("From: ").append(this.notBefore.toString()).append(", ").toString()) + (this.notAfter == null ? "" : new StringBuilder().append("To: ").append(this.notAfter.toString()).toString()) + "]\n";
/*     */   }
/*     */ 
/*     */   public void valid()
/*     */     throws CertificateNotYetValidException, CertificateExpiredException
/*     */   {
/* 194 */     Date localDate = new Date();
/* 195 */     valid(localDate);
/*     */   }
/*     */ 
/*     */   public void valid(Date paramDate)
/*     */     throws CertificateNotYetValidException, CertificateExpiredException
/*     */   {
/* 214 */     if (this.notBefore.after(paramDate)) {
/* 215 */       throw new CertificateNotYetValidException("NotBefore: " + this.notBefore.toString());
/*     */     }
/*     */ 
/* 218 */     if (this.notAfter.before(paramDate))
/* 219 */       throw new CertificateExpiredException("NotAfter: " + this.notAfter.toString());
/*     */   }
/*     */ 
/*     */   public void encode(OutputStream paramOutputStream)
/*     */     throws IOException
/*     */   {
/* 231 */     DerOutputStream localDerOutputStream = new DerOutputStream();
/* 232 */     if (this.extensionValue == null) {
/* 233 */       this.extensionId = PKIXExtensions.PrivateKeyUsage_Id;
/* 234 */       this.critical = false;
/* 235 */       encodeThis();
/*     */     }
/* 237 */     super.encode(localDerOutputStream);
/* 238 */     paramOutputStream.write(localDerOutputStream.toByteArray());
/*     */   }
/*     */ 
/*     */   public void set(String paramString, Object paramObject)
/*     */     throws CertificateException, IOException
/*     */   {
/* 247 */     if (!(paramObject instanceof Date)) {
/* 248 */       throw new CertificateException("Attribute must be of type Date.");
/*     */     }
/* 250 */     if (paramString.equalsIgnoreCase("not_before"))
/* 251 */       this.notBefore = ((Date)paramObject);
/* 252 */     else if (paramString.equalsIgnoreCase("not_after"))
/* 253 */       this.notAfter = ((Date)paramObject);
/*     */     else {
/* 255 */       throw new CertificateException("Attribute name not recognized by CertAttrSet:PrivateKeyUsage.");
/*     */     }
/*     */ 
/* 258 */     encodeThis();
/*     */   }
/*     */ 
/*     */   public Object get(String paramString)
/*     */     throws CertificateException
/*     */   {
/* 266 */     if (paramString.equalsIgnoreCase("not_before"))
/* 267 */       return new Date(this.notBefore.getTime());
/* 268 */     if (paramString.equalsIgnoreCase("not_after")) {
/* 269 */       return new Date(this.notAfter.getTime());
/*     */     }
/* 271 */     throw new CertificateException("Attribute name not recognized by CertAttrSet:PrivateKeyUsage.");
/*     */   }
/*     */ 
/*     */   public void delete(String paramString)
/*     */     throws CertificateException, IOException
/*     */   {
/* 281 */     if (paramString.equalsIgnoreCase("not_before"))
/* 282 */       this.notBefore = null;
/* 283 */     else if (paramString.equalsIgnoreCase("not_after"))
/* 284 */       this.notAfter = null;
/*     */     else {
/* 286 */       throw new CertificateException("Attribute name not recognized by CertAttrSet:PrivateKeyUsage.");
/*     */     }
/*     */ 
/* 289 */     encodeThis();
/*     */   }
/*     */ 
/*     */   public Enumeration<String> getElements()
/*     */   {
/* 297 */     AttributeNameEnumeration localAttributeNameEnumeration = new AttributeNameEnumeration();
/* 298 */     localAttributeNameEnumeration.addElement("not_before");
/* 299 */     localAttributeNameEnumeration.addElement("not_after");
/*     */ 
/* 301 */     return localAttributeNameEnumeration.elements();
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/* 308 */     return "PrivateKeyUsage";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.x509.PrivateKeyUsageExtension
 * JD-Core Version:    0.6.2
 */