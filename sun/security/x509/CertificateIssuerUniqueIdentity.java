/*     */ package sun.security.x509;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.util.Enumeration;
/*     */ import sun.security.util.DerInputStream;
/*     */ import sun.security.util.DerOutputStream;
/*     */ import sun.security.util.DerValue;
/*     */ 
/*     */ public class CertificateIssuerUniqueIdentity
/*     */   implements CertAttrSet<String>
/*     */ {
/*     */   private UniqueIdentity id;
/*     */   public static final String IDENT = "x509.info.issuerID";
/*     */   public static final String NAME = "issuerID";
/*     */   public static final String ID = "id";
/*     */ 
/*     */   public CertificateIssuerUniqueIdentity(UniqueIdentity paramUniqueIdentity)
/*     */   {
/*  63 */     this.id = paramUniqueIdentity;
/*     */   }
/*     */ 
/*     */   public CertificateIssuerUniqueIdentity(DerInputStream paramDerInputStream)
/*     */     throws IOException
/*     */   {
/*  74 */     this.id = new UniqueIdentity(paramDerInputStream);
/*     */   }
/*     */ 
/*     */   public CertificateIssuerUniqueIdentity(InputStream paramInputStream)
/*     */     throws IOException
/*     */   {
/*  85 */     DerValue localDerValue = new DerValue(paramInputStream);
/*  86 */     this.id = new UniqueIdentity(localDerValue);
/*     */   }
/*     */ 
/*     */   public CertificateIssuerUniqueIdentity(DerValue paramDerValue)
/*     */     throws IOException
/*     */   {
/*  97 */     this.id = new UniqueIdentity(paramDerValue);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 104 */     if (this.id == null) return "";
/* 105 */     return this.id.toString();
/*     */   }
/*     */ 
/*     */   public void encode(OutputStream paramOutputStream)
/*     */     throws IOException
/*     */   {
/* 115 */     DerOutputStream localDerOutputStream = new DerOutputStream();
/* 116 */     this.id.encode(localDerOutputStream, DerValue.createTag((byte)-128, false, (byte)1));
/*     */ 
/* 118 */     paramOutputStream.write(localDerOutputStream.toByteArray());
/*     */   }
/*     */ 
/*     */   public void set(String paramString, Object paramObject)
/*     */     throws IOException
/*     */   {
/* 125 */     if (!(paramObject instanceof UniqueIdentity)) {
/* 126 */       throw new IOException("Attribute must be of type UniqueIdentity.");
/*     */     }
/* 128 */     if (paramString.equalsIgnoreCase("id"))
/* 129 */       this.id = ((UniqueIdentity)paramObject);
/*     */     else
/* 131 */       throw new IOException("Attribute name not recognized by CertAttrSet: CertificateIssuerUniqueIdentity.");
/*     */   }
/*     */ 
/*     */   public Object get(String paramString)
/*     */     throws IOException
/*     */   {
/* 140 */     if (paramString.equalsIgnoreCase("id")) {
/* 141 */       return this.id;
/*     */     }
/* 143 */     throw new IOException("Attribute name not recognized by CertAttrSet: CertificateIssuerUniqueIdentity.");
/*     */   }
/*     */ 
/*     */   public void delete(String paramString)
/*     */     throws IOException
/*     */   {
/* 152 */     if (paramString.equalsIgnoreCase("id"))
/* 153 */       this.id = null;
/*     */     else
/* 155 */       throw new IOException("Attribute name not recognized by CertAttrSet: CertificateIssuerUniqueIdentity.");
/*     */   }
/*     */ 
/*     */   public Enumeration<String> getElements()
/*     */   {
/* 165 */     AttributeNameEnumeration localAttributeNameEnumeration = new AttributeNameEnumeration();
/* 166 */     localAttributeNameEnumeration.addElement("id");
/*     */ 
/* 168 */     return localAttributeNameEnumeration.elements();
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/* 175 */     return "issuerID";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.x509.CertificateIssuerUniqueIdentity
 * JD-Core Version:    0.6.2
 */