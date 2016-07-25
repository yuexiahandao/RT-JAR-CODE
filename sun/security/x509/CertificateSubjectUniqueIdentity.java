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
/*     */ public class CertificateSubjectUniqueIdentity
/*     */   implements CertAttrSet<String>
/*     */ {
/*     */   public static final String IDENT = "x509.info.subjectID";
/*     */   public static final String NAME = "subjectID";
/*     */   public static final String ID = "id";
/*     */   private UniqueIdentity id;
/*     */ 
/*     */   public CertificateSubjectUniqueIdentity(UniqueIdentity paramUniqueIdentity)
/*     */   {
/*  62 */     this.id = paramUniqueIdentity;
/*     */   }
/*     */ 
/*     */   public CertificateSubjectUniqueIdentity(DerInputStream paramDerInputStream)
/*     */     throws IOException
/*     */   {
/*  73 */     this.id = new UniqueIdentity(paramDerInputStream);
/*     */   }
/*     */ 
/*     */   public CertificateSubjectUniqueIdentity(InputStream paramInputStream)
/*     */     throws IOException
/*     */   {
/*  84 */     DerValue localDerValue = new DerValue(paramInputStream);
/*  85 */     this.id = new UniqueIdentity(localDerValue);
/*     */   }
/*     */ 
/*     */   public CertificateSubjectUniqueIdentity(DerValue paramDerValue)
/*     */     throws IOException
/*     */   {
/*  96 */     this.id = new UniqueIdentity(paramDerValue);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 103 */     if (this.id == null) return "";
/* 104 */     return this.id.toString();
/*     */   }
/*     */ 
/*     */   public void encode(OutputStream paramOutputStream)
/*     */     throws IOException
/*     */   {
/* 114 */     DerOutputStream localDerOutputStream = new DerOutputStream();
/* 115 */     this.id.encode(localDerOutputStream, DerValue.createTag((byte)-128, false, (byte)2));
/*     */ 
/* 117 */     paramOutputStream.write(localDerOutputStream.toByteArray());
/*     */   }
/*     */ 
/*     */   public void set(String paramString, Object paramObject)
/*     */     throws IOException
/*     */   {
/* 124 */     if (!(paramObject instanceof UniqueIdentity)) {
/* 125 */       throw new IOException("Attribute must be of type UniqueIdentity.");
/*     */     }
/* 127 */     if (paramString.equalsIgnoreCase("id"))
/* 128 */       this.id = ((UniqueIdentity)paramObject);
/*     */     else
/* 130 */       throw new IOException("Attribute name not recognized by CertAttrSet: CertificateSubjectUniqueIdentity.");
/*     */   }
/*     */ 
/*     */   public Object get(String paramString)
/*     */     throws IOException
/*     */   {
/* 139 */     if (paramString.equalsIgnoreCase("id")) {
/* 140 */       return this.id;
/*     */     }
/* 142 */     throw new IOException("Attribute name not recognized by CertAttrSet: CertificateSubjectUniqueIdentity.");
/*     */   }
/*     */ 
/*     */   public void delete(String paramString)
/*     */     throws IOException
/*     */   {
/* 151 */     if (paramString.equalsIgnoreCase("id"))
/* 152 */       this.id = null;
/*     */     else
/* 154 */       throw new IOException("Attribute name not recognized by CertAttrSet: CertificateSubjectUniqueIdentity.");
/*     */   }
/*     */ 
/*     */   public Enumeration<String> getElements()
/*     */   {
/* 164 */     AttributeNameEnumeration localAttributeNameEnumeration = new AttributeNameEnumeration();
/* 165 */     localAttributeNameEnumeration.addElement("id");
/*     */ 
/* 167 */     return localAttributeNameEnumeration.elements();
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/* 174 */     return "subjectID";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.x509.CertificateSubjectUniqueIdentity
 * JD-Core Version:    0.6.2
 */