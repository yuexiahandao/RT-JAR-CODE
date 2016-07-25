/*     */ package sun.security.x509;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.security.cert.CertificateExpiredException;
/*     */ import java.security.cert.CertificateNotYetValidException;
/*     */ import java.util.Date;
/*     */ import java.util.Enumeration;
/*     */ import sun.security.util.DerInputStream;
/*     */ import sun.security.util.DerOutputStream;
/*     */ import sun.security.util.DerValue;
/*     */ 
/*     */ public class CertificateValidity
/*     */   implements CertAttrSet<String>
/*     */ {
/*     */   public static final String IDENT = "x509.info.validity";
/*     */   public static final String NAME = "validity";
/*     */   public static final String NOT_BEFORE = "notBefore";
/*     */   public static final String NOT_AFTER = "notAfter";
/*     */   private static final long YR_2050 = 2524636800000L;
/*     */   private Date notBefore;
/*     */   private Date notAfter;
/*     */ 
/*     */   private Date getNotBefore()
/*     */   {
/*  62 */     return new Date(this.notBefore.getTime());
/*     */   }
/*     */ 
/*     */   private Date getNotAfter()
/*     */   {
/*  67 */     return new Date(this.notAfter.getTime());
/*     */   }
/*     */ 
/*     */   private void construct(DerValue paramDerValue) throws IOException
/*     */   {
/*  72 */     if (paramDerValue.tag != 48) {
/*  73 */       throw new IOException("Invalid encoded CertificateValidity, starting sequence tag missing.");
/*     */     }
/*     */ 
/*  77 */     if (paramDerValue.data.available() == 0) {
/*  78 */       throw new IOException("No data encoded for CertificateValidity");
/*     */     }
/*  80 */     DerInputStream localDerInputStream = new DerInputStream(paramDerValue.toByteArray());
/*  81 */     DerValue[] arrayOfDerValue = localDerInputStream.getSequence(2);
/*  82 */     if (arrayOfDerValue.length != 2) {
/*  83 */       throw new IOException("Invalid encoding for CertificateValidity");
/*     */     }
/*  85 */     if (arrayOfDerValue[0].tag == 23)
/*  86 */       this.notBefore = paramDerValue.data.getUTCTime();
/*  87 */     else if (arrayOfDerValue[0].tag == 24)
/*  88 */       this.notBefore = paramDerValue.data.getGeneralizedTime();
/*     */     else {
/*  90 */       throw new IOException("Invalid encoding for CertificateValidity");
/*     */     }
/*     */ 
/*  93 */     if (arrayOfDerValue[1].tag == 23)
/*  94 */       this.notAfter = paramDerValue.data.getUTCTime();
/*  95 */     else if (arrayOfDerValue[1].tag == 24)
/*  96 */       this.notAfter = paramDerValue.data.getGeneralizedTime();
/*     */     else
/*  98 */       throw new IOException("Invalid encoding for CertificateValidity");
/*     */   }
/*     */ 
/*     */   public CertificateValidity()
/*     */   {
/*     */   }
/*     */ 
/*     */   public CertificateValidity(Date paramDate1, Date paramDate2)
/*     */   {
/* 116 */     this.notBefore = paramDate1;
/* 117 */     this.notAfter = paramDate2;
/*     */   }
/*     */ 
/*     */   public CertificateValidity(DerInputStream paramDerInputStream)
/*     */     throws IOException
/*     */   {
/* 127 */     DerValue localDerValue = paramDerInputStream.getDerValue();
/* 128 */     construct(localDerValue);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 135 */     if ((this.notBefore == null) || (this.notAfter == null))
/* 136 */       return "";
/* 137 */     return "Validity: [From: " + this.notBefore.toString() + ",\n               To: " + this.notAfter.toString() + "]";
/*     */   }
/*     */ 
/*     */   public void encode(OutputStream paramOutputStream)
/*     */     throws IOException
/*     */   {
/* 151 */     if ((this.notBefore == null) || (this.notAfter == null)) {
/* 152 */       throw new IOException("CertAttrSet:CertificateValidity: null values to encode.\n");
/*     */     }
/*     */ 
/* 155 */     DerOutputStream localDerOutputStream1 = new DerOutputStream();
/*     */ 
/* 157 */     if (this.notBefore.getTime() < 2524636800000L)
/* 158 */       localDerOutputStream1.putUTCTime(this.notBefore);
/*     */     else {
/* 160 */       localDerOutputStream1.putGeneralizedTime(this.notBefore);
/*     */     }
/* 162 */     if (this.notAfter.getTime() < 2524636800000L)
/* 163 */       localDerOutputStream1.putUTCTime(this.notAfter);
/*     */     else {
/* 165 */       localDerOutputStream1.putGeneralizedTime(this.notAfter);
/*     */     }
/* 167 */     DerOutputStream localDerOutputStream2 = new DerOutputStream();
/* 168 */     localDerOutputStream2.write((byte)48, localDerOutputStream1);
/*     */ 
/* 170 */     paramOutputStream.write(localDerOutputStream2.toByteArray());
/*     */   }
/*     */ 
/*     */   public void set(String paramString, Object paramObject)
/*     */     throws IOException
/*     */   {
/* 177 */     if (!(paramObject instanceof Date)) {
/* 178 */       throw new IOException("Attribute must be of type Date.");
/*     */     }
/* 180 */     if (paramString.equalsIgnoreCase("notBefore"))
/* 181 */       this.notBefore = ((Date)paramObject);
/* 182 */     else if (paramString.equalsIgnoreCase("notAfter"))
/* 183 */       this.notAfter = ((Date)paramObject);
/*     */     else
/* 185 */       throw new IOException("Attribute name not recognized by CertAttrSet: CertificateValidity.");
/*     */   }
/*     */ 
/*     */   public Object get(String paramString)
/*     */     throws IOException
/*     */   {
/* 194 */     if (paramString.equalsIgnoreCase("notBefore"))
/* 195 */       return getNotBefore();
/* 196 */     if (paramString.equalsIgnoreCase("notAfter")) {
/* 197 */       return getNotAfter();
/*     */     }
/* 199 */     throw new IOException("Attribute name not recognized by CertAttrSet: CertificateValidity.");
/*     */   }
/*     */ 
/*     */   public void delete(String paramString)
/*     */     throws IOException
/*     */   {
/* 208 */     if (paramString.equalsIgnoreCase("notBefore"))
/* 209 */       this.notBefore = null;
/* 210 */     else if (paramString.equalsIgnoreCase("notAfter"))
/* 211 */       this.notAfter = null;
/*     */     else
/* 213 */       throw new IOException("Attribute name not recognized by CertAttrSet: CertificateValidity.");
/*     */   }
/*     */ 
/*     */   public Enumeration<String> getElements()
/*     */   {
/* 223 */     AttributeNameEnumeration localAttributeNameEnumeration = new AttributeNameEnumeration();
/* 224 */     localAttributeNameEnumeration.addElement("notBefore");
/* 225 */     localAttributeNameEnumeration.addElement("notAfter");
/*     */ 
/* 227 */     return localAttributeNameEnumeration.elements();
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/* 234 */     return "validity";
/*     */   }
/*     */ 
/*     */   public void valid()
/*     */     throws CertificateNotYetValidException, CertificateExpiredException
/*     */   {
/* 246 */     Date localDate = new Date();
/* 247 */     valid(localDate);
/*     */   }
/*     */ 
/*     */   public void valid(Date paramDate)
/*     */     throws CertificateNotYetValidException, CertificateExpiredException
/*     */   {
/* 268 */     if (this.notBefore.after(paramDate)) {
/* 269 */       throw new CertificateNotYetValidException("NotBefore: " + this.notBefore.toString());
/*     */     }
/*     */ 
/* 272 */     if (this.notAfter.before(paramDate))
/* 273 */       throw new CertificateExpiredException("NotAfter: " + this.notAfter.toString());
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.x509.CertificateValidity
 * JD-Core Version:    0.6.2
 */