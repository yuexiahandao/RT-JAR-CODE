/*     */ package sun.security.x509;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.util.Enumeration;
/*     */ import sun.security.util.DerOutputStream;
/*     */ import sun.security.util.DerValue;
/*     */ 
/*     */ public class CertificateIssuerExtension extends Extension
/*     */   implements CertAttrSet<String>
/*     */ {
/*     */   public static final String NAME = "CertificateIssuer";
/*     */   public static final String ISSUER = "issuer";
/*     */   private GeneralNames names;
/*     */ 
/*     */   private void encodeThis()
/*     */     throws IOException
/*     */   {
/*  78 */     if ((this.names == null) || (this.names.isEmpty())) {
/*  79 */       this.extensionValue = null;
/*  80 */       return;
/*     */     }
/*  82 */     DerOutputStream localDerOutputStream = new DerOutputStream();
/*  83 */     this.names.encode(localDerOutputStream);
/*  84 */     this.extensionValue = localDerOutputStream.toByteArray();
/*     */   }
/*     */ 
/*     */   public CertificateIssuerExtension(GeneralNames paramGeneralNames)
/*     */     throws IOException
/*     */   {
/*  95 */     this.extensionId = PKIXExtensions.CertificateIssuer_Id;
/*  96 */     this.critical = true;
/*  97 */     this.names = paramGeneralNames;
/*  98 */     encodeThis();
/*     */   }
/*     */ 
/*     */   public CertificateIssuerExtension(Boolean paramBoolean, Object paramObject)
/*     */     throws IOException
/*     */   {
/* 112 */     this.extensionId = PKIXExtensions.CertificateIssuer_Id;
/* 113 */     this.critical = paramBoolean.booleanValue();
/*     */ 
/* 115 */     this.extensionValue = ((byte[])paramObject);
/* 116 */     DerValue localDerValue = new DerValue(this.extensionValue);
/* 117 */     this.names = new GeneralNames(localDerValue);
/*     */   }
/*     */ 
/*     */   public void set(String paramString, Object paramObject)
/*     */     throws IOException
/*     */   {
/* 126 */     if (paramString.equalsIgnoreCase("issuer")) {
/* 127 */       if (!(paramObject instanceof GeneralNames)) {
/* 128 */         throw new IOException("Attribute value must be of type GeneralNames");
/*     */       }
/*     */ 
/* 131 */       this.names = ((GeneralNames)paramObject);
/*     */     } else {
/* 133 */       throw new IOException("Attribute name not recognized by CertAttrSet:CertificateIssuer");
/*     */     }
/*     */ 
/* 136 */     encodeThis();
/*     */   }
/*     */ 
/*     */   public Object get(String paramString)
/*     */     throws IOException
/*     */   {
/* 145 */     if (paramString.equalsIgnoreCase("issuer")) {
/* 146 */       return this.names;
/*     */     }
/* 148 */     throw new IOException("Attribute name not recognized by CertAttrSet:CertificateIssuer");
/*     */   }
/*     */ 
/*     */   public void delete(String paramString)
/*     */     throws IOException
/*     */   {
/* 159 */     if (paramString.equalsIgnoreCase("issuer"))
/* 160 */       this.names = null;
/*     */     else {
/* 162 */       throw new IOException("Attribute name not recognized by CertAttrSet:CertificateIssuer");
/*     */     }
/*     */ 
/* 165 */     encodeThis();
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 172 */     return super.toString() + "Certificate Issuer [\n" + String.valueOf(this.names) + "]\n";
/*     */   }
/*     */ 
/*     */   public void encode(OutputStream paramOutputStream)
/*     */     throws IOException
/*     */   {
/* 183 */     DerOutputStream localDerOutputStream = new DerOutputStream();
/* 184 */     if (this.extensionValue == null) {
/* 185 */       this.extensionId = PKIXExtensions.CertificateIssuer_Id;
/* 186 */       this.critical = true;
/* 187 */       encodeThis();
/*     */     }
/* 189 */     super.encode(localDerOutputStream);
/* 190 */     paramOutputStream.write(localDerOutputStream.toByteArray());
/*     */   }
/*     */ 
/*     */   public Enumeration<String> getElements()
/*     */   {
/* 198 */     AttributeNameEnumeration localAttributeNameEnumeration = new AttributeNameEnumeration();
/* 199 */     localAttributeNameEnumeration.addElement("issuer");
/* 200 */     return localAttributeNameEnumeration.elements();
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/* 207 */     return "CertificateIssuer";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.x509.CertificateIssuerExtension
 * JD-Core Version:    0.6.2
 */