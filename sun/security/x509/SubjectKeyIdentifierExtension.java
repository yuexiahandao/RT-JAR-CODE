/*     */ package sun.security.x509;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.util.Enumeration;
/*     */ import sun.security.util.DerOutputStream;
/*     */ import sun.security.util.DerValue;
/*     */ 
/*     */ public class SubjectKeyIdentifierExtension extends Extension
/*     */   implements CertAttrSet<String>
/*     */ {
/*     */   public static final String IDENT = "x509.info.extensions.SubjectKeyIdentifier";
/*     */   public static final String NAME = "SubjectKeyIdentifier";
/*     */   public static final String KEY_ID = "key_id";
/*  70 */   private KeyIdentifier id = null;
/*     */ 
/*     */   private void encodeThis() throws IOException
/*     */   {
/*  74 */     if (this.id == null) {
/*  75 */       this.extensionValue = null;
/*  76 */       return;
/*     */     }
/*  78 */     DerOutputStream localDerOutputStream = new DerOutputStream();
/*  79 */     this.id.encode(localDerOutputStream);
/*  80 */     this.extensionValue = localDerOutputStream.toByteArray();
/*     */   }
/*     */ 
/*     */   public SubjectKeyIdentifierExtension(byte[] paramArrayOfByte)
/*     */     throws IOException
/*     */   {
/*  90 */     this.id = new KeyIdentifier(paramArrayOfByte);
/*     */ 
/*  92 */     this.extensionId = PKIXExtensions.SubjectKey_Id;
/*  93 */     this.critical = false;
/*  94 */     encodeThis();
/*     */   }
/*     */ 
/*     */   public SubjectKeyIdentifierExtension(Boolean paramBoolean, Object paramObject)
/*     */     throws IOException
/*     */   {
/* 107 */     this.extensionId = PKIXExtensions.SubjectKey_Id;
/* 108 */     this.critical = paramBoolean.booleanValue();
/* 109 */     this.extensionValue = ((byte[])paramObject);
/* 110 */     DerValue localDerValue = new DerValue(this.extensionValue);
/* 111 */     this.id = new KeyIdentifier(localDerValue);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 118 */     return super.toString() + "SubjectKeyIdentifier [\n" + String.valueOf(this.id) + "]\n";
/*     */   }
/*     */ 
/*     */   public void encode(OutputStream paramOutputStream)
/*     */     throws IOException
/*     */   {
/* 129 */     DerOutputStream localDerOutputStream = new DerOutputStream();
/* 130 */     if (this.extensionValue == null) {
/* 131 */       this.extensionId = PKIXExtensions.SubjectKey_Id;
/* 132 */       this.critical = false;
/* 133 */       encodeThis();
/*     */     }
/* 135 */     super.encode(localDerOutputStream);
/* 136 */     paramOutputStream.write(localDerOutputStream.toByteArray());
/*     */   }
/*     */ 
/*     */   public void set(String paramString, Object paramObject)
/*     */     throws IOException
/*     */   {
/* 143 */     if (paramString.equalsIgnoreCase("key_id")) {
/* 144 */       if (!(paramObject instanceof KeyIdentifier)) {
/* 145 */         throw new IOException("Attribute value should be of type KeyIdentifier.");
/*     */       }
/*     */ 
/* 148 */       this.id = ((KeyIdentifier)paramObject);
/*     */     } else {
/* 150 */       throw new IOException("Attribute name not recognized by CertAttrSet:SubjectKeyIdentifierExtension.");
/*     */     }
/*     */ 
/* 153 */     encodeThis();
/*     */   }
/*     */ 
/*     */   public Object get(String paramString)
/*     */     throws IOException
/*     */   {
/* 160 */     if (paramString.equalsIgnoreCase("key_id")) {
/* 161 */       return this.id;
/*     */     }
/* 163 */     throw new IOException("Attribute name not recognized by CertAttrSet:SubjectKeyIdentifierExtension.");
/*     */   }
/*     */ 
/*     */   public void delete(String paramString)
/*     */     throws IOException
/*     */   {
/* 172 */     if (paramString.equalsIgnoreCase("key_id"))
/* 173 */       this.id = null;
/*     */     else {
/* 175 */       throw new IOException("Attribute name not recognized by CertAttrSet:SubjectKeyIdentifierExtension.");
/*     */     }
/*     */ 
/* 178 */     encodeThis();
/*     */   }
/*     */ 
/*     */   public Enumeration<String> getElements()
/*     */   {
/* 186 */     AttributeNameEnumeration localAttributeNameEnumeration = new AttributeNameEnumeration();
/* 187 */     localAttributeNameEnumeration.addElement("key_id");
/*     */ 
/* 189 */     return localAttributeNameEnumeration.elements();
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/* 196 */     return "SubjectKeyIdentifier";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.x509.SubjectKeyIdentifierExtension
 * JD-Core Version:    0.6.2
 */