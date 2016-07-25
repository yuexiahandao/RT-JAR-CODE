/*     */ package sun.security.x509;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.math.BigInteger;
/*     */ import java.util.Enumeration;
/*     */ import sun.security.util.DerInputStream;
/*     */ import sun.security.util.DerOutputStream;
/*     */ import sun.security.util.DerValue;
/*     */ 
/*     */ public class CertificateSerialNumber
/*     */   implements CertAttrSet<String>
/*     */ {
/*     */   public static final String IDENT = "x509.info.serialNumber";
/*     */   public static final String NAME = "serialNumber";
/*     */   public static final String NUMBER = "number";
/*     */   private SerialNumber serial;
/*     */ 
/*     */   public CertificateSerialNumber(BigInteger paramBigInteger)
/*     */   {
/*  63 */     this.serial = new SerialNumber(paramBigInteger);
/*     */   }
/*     */ 
/*     */   public CertificateSerialNumber(int paramInt)
/*     */   {
/*  72 */     this.serial = new SerialNumber(paramInt);
/*     */   }
/*     */ 
/*     */   public CertificateSerialNumber(DerInputStream paramDerInputStream)
/*     */     throws IOException
/*     */   {
/*  82 */     this.serial = new SerialNumber(paramDerInputStream);
/*     */   }
/*     */ 
/*     */   public CertificateSerialNumber(InputStream paramInputStream)
/*     */     throws IOException
/*     */   {
/*  92 */     this.serial = new SerialNumber(paramInputStream);
/*     */   }
/*     */ 
/*     */   public CertificateSerialNumber(DerValue paramDerValue)
/*     */     throws IOException
/*     */   {
/* 102 */     this.serial = new SerialNumber(paramDerValue);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 109 */     if (this.serial == null) return "";
/* 110 */     return this.serial.toString();
/*     */   }
/*     */ 
/*     */   public void encode(OutputStream paramOutputStream)
/*     */     throws IOException
/*     */   {
/* 120 */     DerOutputStream localDerOutputStream = new DerOutputStream();
/* 121 */     this.serial.encode(localDerOutputStream);
/*     */ 
/* 123 */     paramOutputStream.write(localDerOutputStream.toByteArray());
/*     */   }
/*     */ 
/*     */   public void set(String paramString, Object paramObject)
/*     */     throws IOException
/*     */   {
/* 130 */     if (!(paramObject instanceof SerialNumber)) {
/* 131 */       throw new IOException("Attribute must be of type SerialNumber.");
/*     */     }
/* 133 */     if (paramString.equalsIgnoreCase("number"))
/* 134 */       this.serial = ((SerialNumber)paramObject);
/*     */     else
/* 136 */       throw new IOException("Attribute name not recognized by CertAttrSet:CertificateSerialNumber.");
/*     */   }
/*     */ 
/*     */   public Object get(String paramString)
/*     */     throws IOException
/*     */   {
/* 145 */     if (paramString.equalsIgnoreCase("number")) {
/* 146 */       return this.serial;
/*     */     }
/* 148 */     throw new IOException("Attribute name not recognized by CertAttrSet:CertificateSerialNumber.");
/*     */   }
/*     */ 
/*     */   public void delete(String paramString)
/*     */     throws IOException
/*     */   {
/* 157 */     if (paramString.equalsIgnoreCase("number"))
/* 158 */       this.serial = null;
/*     */     else
/* 160 */       throw new IOException("Attribute name not recognized by CertAttrSet:CertificateSerialNumber.");
/*     */   }
/*     */ 
/*     */   public Enumeration<String> getElements()
/*     */   {
/* 170 */     AttributeNameEnumeration localAttributeNameEnumeration = new AttributeNameEnumeration();
/* 171 */     localAttributeNameEnumeration.addElement("number");
/*     */ 
/* 173 */     return localAttributeNameEnumeration.elements();
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/* 180 */     return "serialNumber";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.x509.CertificateSerialNumber
 * JD-Core Version:    0.6.2
 */