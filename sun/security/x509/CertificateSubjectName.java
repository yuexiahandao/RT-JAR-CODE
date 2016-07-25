/*     */ package sun.security.x509;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.util.Enumeration;
/*     */ import javax.security.auth.x500.X500Principal;
/*     */ import sun.security.util.DerInputStream;
/*     */ import sun.security.util.DerOutputStream;
/*     */ import sun.security.util.DerValue;
/*     */ 
/*     */ public class CertificateSubjectName
/*     */   implements CertAttrSet<String>
/*     */ {
/*     */   public static final String IDENT = "x509.info.subject";
/*     */   public static final String NAME = "subject";
/*     */   public static final String DN_NAME = "dname";
/*     */   public static final String DN_PRINCIPAL = "x500principal";
/*     */   private X500Name dnName;
/*     */   private X500Principal dnPrincipal;
/*     */ 
/*     */   public CertificateSubjectName(X500Name paramX500Name)
/*     */   {
/*  72 */     this.dnName = paramX500Name;
/*     */   }
/*     */ 
/*     */   public CertificateSubjectName(DerInputStream paramDerInputStream)
/*     */     throws IOException
/*     */   {
/*  82 */     this.dnName = new X500Name(paramDerInputStream);
/*     */   }
/*     */ 
/*     */   public CertificateSubjectName(InputStream paramInputStream)
/*     */     throws IOException
/*     */   {
/*  92 */     DerValue localDerValue = new DerValue(paramInputStream);
/*  93 */     this.dnName = new X500Name(localDerValue);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 100 */     if (this.dnName == null) return "";
/* 101 */     return this.dnName.toString();
/*     */   }
/*     */ 
/*     */   public void encode(OutputStream paramOutputStream)
/*     */     throws IOException
/*     */   {
/* 111 */     DerOutputStream localDerOutputStream = new DerOutputStream();
/* 112 */     this.dnName.encode(localDerOutputStream);
/*     */ 
/* 114 */     paramOutputStream.write(localDerOutputStream.toByteArray());
/*     */   }
/*     */ 
/*     */   public void set(String paramString, Object paramObject)
/*     */     throws IOException
/*     */   {
/* 121 */     if (!(paramObject instanceof X500Name)) {
/* 122 */       throw new IOException("Attribute must be of type X500Name.");
/*     */     }
/* 124 */     if (paramString.equalsIgnoreCase("dname")) {
/* 125 */       this.dnName = ((X500Name)paramObject);
/* 126 */       this.dnPrincipal = null;
/*     */     } else {
/* 128 */       throw new IOException("Attribute name not recognized by CertAttrSet:CertificateSubjectName.");
/*     */     }
/*     */   }
/*     */ 
/*     */   public Object get(String paramString)
/*     */     throws IOException
/*     */   {
/* 137 */     if (paramString.equalsIgnoreCase("dname"))
/* 138 */       return this.dnName;
/* 139 */     if (paramString.equalsIgnoreCase("x500principal")) {
/* 140 */       if ((this.dnPrincipal == null) && (this.dnName != null)) {
/* 141 */         this.dnPrincipal = this.dnName.asX500Principal();
/*     */       }
/* 143 */       return this.dnPrincipal;
/*     */     }
/* 145 */     throw new IOException("Attribute name not recognized by CertAttrSet:CertificateSubjectName.");
/*     */   }
/*     */ 
/*     */   public void delete(String paramString)
/*     */     throws IOException
/*     */   {
/* 154 */     if (paramString.equalsIgnoreCase("dname")) {
/* 155 */       this.dnName = null;
/* 156 */       this.dnPrincipal = null;
/*     */     } else {
/* 158 */       throw new IOException("Attribute name not recognized by CertAttrSet:CertificateSubjectName.");
/*     */     }
/*     */   }
/*     */ 
/*     */   public Enumeration<String> getElements()
/*     */   {
/* 168 */     AttributeNameEnumeration localAttributeNameEnumeration = new AttributeNameEnumeration();
/* 169 */     localAttributeNameEnumeration.addElement("dname");
/*     */ 
/* 171 */     return localAttributeNameEnumeration.elements();
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/* 178 */     return "subject";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.x509.CertificateSubjectName
 * JD-Core Version:    0.6.2
 */