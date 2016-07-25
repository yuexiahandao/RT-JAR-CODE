/*     */ package sun.security.x509;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.util.Enumeration;
/*     */ import sun.security.util.DerOutputStream;
/*     */ import sun.security.util.DerValue;
/*     */ 
/*     */ public class SubjectAlternativeNameExtension extends Extension
/*     */   implements CertAttrSet<String>
/*     */ {
/*     */   public static final String IDENT = "x509.info.extensions.SubjectAlternativeName";
/*     */   public static final String NAME = "SubjectAlternativeName";
/*     */   public static final String SUBJECT_NAME = "subject_name";
/*  70 */   GeneralNames names = null;
/*     */ 
/*     */   private void encodeThis() throws IOException
/*     */   {
/*  74 */     if ((this.names == null) || (this.names.isEmpty())) {
/*  75 */       this.extensionValue = null;
/*  76 */       return;
/*     */     }
/*  78 */     DerOutputStream localDerOutputStream = new DerOutputStream();
/*  79 */     this.names.encode(localDerOutputStream);
/*  80 */     this.extensionValue = localDerOutputStream.toByteArray();
/*     */   }
/*     */ 
/*     */   public SubjectAlternativeNameExtension(GeneralNames paramGeneralNames)
/*     */     throws IOException
/*     */   {
/*  92 */     this(Boolean.FALSE, paramGeneralNames);
/*     */   }
/*     */ 
/*     */   public SubjectAlternativeNameExtension(Boolean paramBoolean, GeneralNames paramGeneralNames)
/*     */     throws IOException
/*     */   {
/* 105 */     this.names = paramGeneralNames;
/* 106 */     this.extensionId = PKIXExtensions.SubjectAlternativeName_Id;
/* 107 */     this.critical = paramBoolean.booleanValue();
/* 108 */     encodeThis();
/*     */   }
/*     */ 
/*     */   public SubjectAlternativeNameExtension()
/*     */   {
/* 116 */     this.extensionId = PKIXExtensions.SubjectAlternativeName_Id;
/* 117 */     this.critical = false;
/* 118 */     this.names = new GeneralNames();
/*     */   }
/*     */ 
/*     */   public SubjectAlternativeNameExtension(Boolean paramBoolean, Object paramObject)
/*     */     throws IOException
/*     */   {
/* 131 */     this.extensionId = PKIXExtensions.SubjectAlternativeName_Id;
/* 132 */     this.critical = paramBoolean.booleanValue();
/*     */ 
/* 134 */     this.extensionValue = ((byte[])paramObject);
/* 135 */     DerValue localDerValue = new DerValue(this.extensionValue);
/* 136 */     if (localDerValue.data == null) {
/* 137 */       this.names = new GeneralNames();
/* 138 */       return;
/*     */     }
/*     */ 
/* 141 */     this.names = new GeneralNames(localDerValue);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 149 */     String str = super.toString() + "SubjectAlternativeName [\n";
/* 150 */     if (this.names == null)
/* 151 */       str = str + "  null\n";
/*     */     else {
/* 153 */       for (GeneralName localGeneralName : this.names.names()) {
/* 154 */         str = str + "  " + localGeneralName + "\n";
/*     */       }
/*     */     }
/* 157 */     str = str + "]\n";
/* 158 */     return str;
/*     */   }
/*     */ 
/*     */   public void encode(OutputStream paramOutputStream)
/*     */     throws IOException
/*     */   {
/* 168 */     DerOutputStream localDerOutputStream = new DerOutputStream();
/* 169 */     if (this.extensionValue == null) {
/* 170 */       this.extensionId = PKIXExtensions.SubjectAlternativeName_Id;
/* 171 */       this.critical = false;
/* 172 */       encodeThis();
/*     */     }
/* 174 */     super.encode(localDerOutputStream);
/* 175 */     paramOutputStream.write(localDerOutputStream.toByteArray());
/*     */   }
/*     */ 
/*     */   public void set(String paramString, Object paramObject)
/*     */     throws IOException
/*     */   {
/* 182 */     if (paramString.equalsIgnoreCase("subject_name")) {
/* 183 */       if (!(paramObject instanceof GeneralNames)) {
/* 184 */         throw new IOException("Attribute value should be of type GeneralNames.");
/*     */       }
/*     */ 
/* 187 */       this.names = ((GeneralNames)paramObject);
/*     */     } else {
/* 189 */       throw new IOException("Attribute name not recognized by CertAttrSet:SubjectAlternativeName.");
/*     */     }
/*     */ 
/* 192 */     encodeThis();
/*     */   }
/*     */ 
/*     */   public Object get(String paramString)
/*     */     throws IOException
/*     */   {
/* 199 */     if (paramString.equalsIgnoreCase("subject_name")) {
/* 200 */       return this.names;
/*     */     }
/* 202 */     throw new IOException("Attribute name not recognized by CertAttrSet:SubjectAlternativeName.");
/*     */   }
/*     */ 
/*     */   public void delete(String paramString)
/*     */     throws IOException
/*     */   {
/* 211 */     if (paramString.equalsIgnoreCase("subject_name"))
/* 212 */       this.names = null;
/*     */     else {
/* 214 */       throw new IOException("Attribute name not recognized by CertAttrSet:SubjectAlternativeName.");
/*     */     }
/*     */ 
/* 217 */     encodeThis();
/*     */   }
/*     */ 
/*     */   public Enumeration<String> getElements()
/*     */   {
/* 225 */     AttributeNameEnumeration localAttributeNameEnumeration = new AttributeNameEnumeration();
/* 226 */     localAttributeNameEnumeration.addElement("subject_name");
/*     */ 
/* 228 */     return localAttributeNameEnumeration.elements();
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/* 235 */     return "SubjectAlternativeName";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.x509.SubjectAlternativeNameExtension
 * JD-Core Version:    0.6.2
 */