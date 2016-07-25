/*     */ package sun.security.x509;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.security.PublicKey;
/*     */ import java.util.Enumeration;
/*     */ import sun.security.util.DerInputStream;
/*     */ import sun.security.util.DerOutputStream;
/*     */ import sun.security.util.DerValue;
/*     */ 
/*     */ public class CertificateX509Key
/*     */   implements CertAttrSet<String>
/*     */ {
/*     */   public static final String IDENT = "x509.info.key";
/*     */   public static final String NAME = "key";
/*     */   public static final String KEY = "value";
/*     */   private PublicKey key;
/*     */ 
/*     */   public CertificateX509Key(PublicKey paramPublicKey)
/*     */   {
/*  64 */     this.key = paramPublicKey;
/*     */   }
/*     */ 
/*     */   public CertificateX509Key(DerInputStream paramDerInputStream)
/*     */     throws IOException
/*     */   {
/*  74 */     DerValue localDerValue = paramDerInputStream.getDerValue();
/*  75 */     this.key = X509Key.parse(localDerValue);
/*     */   }
/*     */ 
/*     */   public CertificateX509Key(InputStream paramInputStream)
/*     */     throws IOException
/*     */   {
/*  85 */     DerValue localDerValue = new DerValue(paramInputStream);
/*  86 */     this.key = X509Key.parse(localDerValue);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/*  93 */     if (this.key == null) return "";
/*  94 */     return this.key.toString();
/*     */   }
/*     */ 
/*     */   public void encode(OutputStream paramOutputStream)
/*     */     throws IOException
/*     */   {
/* 104 */     DerOutputStream localDerOutputStream = new DerOutputStream();
/* 105 */     localDerOutputStream.write(this.key.getEncoded());
/*     */ 
/* 107 */     paramOutputStream.write(localDerOutputStream.toByteArray());
/*     */   }
/*     */ 
/*     */   public void set(String paramString, Object paramObject)
/*     */     throws IOException
/*     */   {
/* 114 */     if (paramString.equalsIgnoreCase("value"))
/* 115 */       this.key = ((PublicKey)paramObject);
/*     */     else
/* 117 */       throw new IOException("Attribute name not recognized by CertAttrSet: CertificateX509Key.");
/*     */   }
/*     */ 
/*     */   public Object get(String paramString)
/*     */     throws IOException
/*     */   {
/* 126 */     if (paramString.equalsIgnoreCase("value")) {
/* 127 */       return this.key;
/*     */     }
/* 129 */     throw new IOException("Attribute name not recognized by CertAttrSet: CertificateX509Key.");
/*     */   }
/*     */ 
/*     */   public void delete(String paramString)
/*     */     throws IOException
/*     */   {
/* 138 */     if (paramString.equalsIgnoreCase("value"))
/* 139 */       this.key = null;
/*     */     else
/* 141 */       throw new IOException("Attribute name not recognized by CertAttrSet: CertificateX509Key.");
/*     */   }
/*     */ 
/*     */   public Enumeration<String> getElements()
/*     */   {
/* 151 */     AttributeNameEnumeration localAttributeNameEnumeration = new AttributeNameEnumeration();
/* 152 */     localAttributeNameEnumeration.addElement("value");
/*     */ 
/* 154 */     return localAttributeNameEnumeration.elements();
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/* 161 */     return "key";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.x509.CertificateX509Key
 * JD-Core Version:    0.6.2
 */