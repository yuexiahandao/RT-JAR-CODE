/*     */ package sun.security.x509;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.util.Enumeration;
/*     */ import sun.security.util.DerInputStream;
/*     */ import sun.security.util.DerOutputStream;
/*     */ import sun.security.util.DerValue;
/*     */ import sun.security.util.ObjectIdentifier;
/*     */ 
/*     */ public class CertificateAlgorithmId
/*     */   implements CertAttrSet<String>
/*     */ {
/*     */   private AlgorithmId algId;
/*     */   public static final String IDENT = "x509.info.algorithmID";
/*     */   public static final String NAME = "algorithmID";
/*     */   public static final String ALGORITHM = "algorithm";
/*     */ 
/*     */   public CertificateAlgorithmId(AlgorithmId paramAlgorithmId)
/*     */   {
/*  68 */     this.algId = paramAlgorithmId;
/*     */   }
/*     */ 
/*     */   public CertificateAlgorithmId(DerInputStream paramDerInputStream)
/*     */     throws IOException
/*     */   {
/*  78 */     DerValue localDerValue = paramDerInputStream.getDerValue();
/*  79 */     this.algId = AlgorithmId.parse(localDerValue);
/*     */   }
/*     */ 
/*     */   public CertificateAlgorithmId(InputStream paramInputStream)
/*     */     throws IOException
/*     */   {
/*  89 */     DerValue localDerValue = new DerValue(paramInputStream);
/*  90 */     this.algId = AlgorithmId.parse(localDerValue);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/*  97 */     if (this.algId == null) return "";
/*  98 */     return this.algId.toString() + ", OID = " + this.algId.getOID().toString() + "\n";
/*     */   }
/*     */ 
/*     */   public void encode(OutputStream paramOutputStream)
/*     */     throws IOException
/*     */   {
/* 109 */     DerOutputStream localDerOutputStream = new DerOutputStream();
/* 110 */     this.algId.encode(localDerOutputStream);
/*     */ 
/* 112 */     paramOutputStream.write(localDerOutputStream.toByteArray());
/*     */   }
/*     */ 
/*     */   public void set(String paramString, Object paramObject)
/*     */     throws IOException
/*     */   {
/* 119 */     if (!(paramObject instanceof AlgorithmId)) {
/* 120 */       throw new IOException("Attribute must be of type AlgorithmId.");
/*     */     }
/* 122 */     if (paramString.equalsIgnoreCase("algorithm"))
/* 123 */       this.algId = ((AlgorithmId)paramObject);
/*     */     else
/* 125 */       throw new IOException("Attribute name not recognized by CertAttrSet:CertificateAlgorithmId.");
/*     */   }
/*     */ 
/*     */   public Object get(String paramString)
/*     */     throws IOException
/*     */   {
/* 134 */     if (paramString.equalsIgnoreCase("algorithm")) {
/* 135 */       return this.algId;
/*     */     }
/* 137 */     throw new IOException("Attribute name not recognized by CertAttrSet:CertificateAlgorithmId.");
/*     */   }
/*     */ 
/*     */   public void delete(String paramString)
/*     */     throws IOException
/*     */   {
/* 146 */     if (paramString.equalsIgnoreCase("algorithm"))
/* 147 */       this.algId = null;
/*     */     else
/* 149 */       throw new IOException("Attribute name not recognized by CertAttrSet:CertificateAlgorithmId.");
/*     */   }
/*     */ 
/*     */   public Enumeration<String> getElements()
/*     */   {
/* 159 */     AttributeNameEnumeration localAttributeNameEnumeration = new AttributeNameEnumeration();
/* 160 */     localAttributeNameEnumeration.addElement("algorithm");
/* 161 */     return localAttributeNameEnumeration.elements();
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/* 168 */     return "algorithmID";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.x509.CertificateAlgorithmId
 * JD-Core Version:    0.6.2
 */