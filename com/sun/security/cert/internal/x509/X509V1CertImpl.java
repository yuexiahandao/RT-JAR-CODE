/*     */ package com.sun.security.cert.internal.x509;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.math.BigInteger;
/*     */ import java.security.InvalidKeyException;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.NoSuchProviderException;
/*     */ import java.security.Principal;
/*     */ import java.security.PublicKey;
/*     */ import java.security.SignatureException;
/*     */ import java.security.cert.CertificateFactory;
/*     */ import java.util.Date;
/*     */ 
/*     */ public class X509V1CertImpl extends javax.security.cert.X509Certificate
/*     */   implements Serializable
/*     */ {
/*     */   static final long serialVersionUID = -2048442350420423405L;
/*     */   private java.security.cert.X509Certificate wrappedCert;
/*     */ 
/*     */   private static synchronized CertificateFactory getFactory()
/*     */     throws java.security.cert.CertificateException
/*     */   {
/*  58 */     return CertificateFactory.getInstance("X.509");
/*     */   }
/*     */ 
/*     */   public X509V1CertImpl()
/*     */   {
/*     */   }
/*     */ 
/*     */   public X509V1CertImpl(byte[] paramArrayOfByte)
/*     */     throws javax.security.cert.CertificateException
/*     */   {
/*     */     try
/*     */     {
/*  83 */       ByteArrayInputStream localByteArrayInputStream = new ByteArrayInputStream(paramArrayOfByte);
/*  84 */       this.wrappedCert = ((java.security.cert.X509Certificate)getFactory().generateCertificate(localByteArrayInputStream));
/*     */     }
/*     */     catch (java.security.cert.CertificateException localCertificateException) {
/*  87 */       throw new javax.security.cert.CertificateException(localCertificateException.getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   public X509V1CertImpl(InputStream paramInputStream)
/*     */     throws javax.security.cert.CertificateException
/*     */   {
/*     */     try
/*     */     {
/* 100 */       this.wrappedCert = ((java.security.cert.X509Certificate)getFactory().generateCertificate(paramInputStream));
/*     */     }
/*     */     catch (java.security.cert.CertificateException localCertificateException) {
/* 103 */       throw new javax.security.cert.CertificateException(localCertificateException.getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   public byte[] getEncoded()
/*     */     throws javax.security.cert.CertificateEncodingException
/*     */   {
/*     */     try
/*     */     {
/* 115 */       return this.wrappedCert.getEncoded();
/*     */     } catch (java.security.cert.CertificateEncodingException localCertificateEncodingException) {
/* 117 */       throw new javax.security.cert.CertificateEncodingException(localCertificateEncodingException.getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   public void verify(PublicKey paramPublicKey)
/*     */     throws javax.security.cert.CertificateException, NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException, SignatureException
/*     */   {
/*     */     try
/*     */     {
/* 135 */       this.wrappedCert.verify(paramPublicKey);
/*     */     } catch (java.security.cert.CertificateException localCertificateException) {
/* 137 */       throw new javax.security.cert.CertificateException(localCertificateException.getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   public void verify(PublicKey paramPublicKey, String paramString)
/*     */     throws javax.security.cert.CertificateException, NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException, SignatureException
/*     */   {
/*     */     try
/*     */     {
/* 156 */       this.wrappedCert.verify(paramPublicKey, paramString);
/*     */     } catch (java.security.cert.CertificateException localCertificateException) {
/* 158 */       throw new javax.security.cert.CertificateException(localCertificateException.getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   public void checkValidity()
/*     */     throws javax.security.cert.CertificateExpiredException, javax.security.cert.CertificateNotYetValidException
/*     */   {
/* 168 */     checkValidity(new Date());
/*     */   }
/*     */ 
/*     */   public void checkValidity(Date paramDate)
/*     */     throws javax.security.cert.CertificateExpiredException, javax.security.cert.CertificateNotYetValidException
/*     */   {
/*     */     try
/*     */     {
/* 182 */       this.wrappedCert.checkValidity(paramDate);
/*     */     } catch (java.security.cert.CertificateNotYetValidException localCertificateNotYetValidException) {
/* 184 */       throw new javax.security.cert.CertificateNotYetValidException(localCertificateNotYetValidException.getMessage());
/*     */     } catch (java.security.cert.CertificateExpiredException localCertificateExpiredException) {
/* 186 */       throw new javax.security.cert.CertificateExpiredException(localCertificateExpiredException.getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 198 */     return this.wrappedCert.toString();
/*     */   }
/*     */ 
/*     */   public PublicKey getPublicKey()
/*     */   {
/* 207 */     PublicKey localPublicKey = this.wrappedCert.getPublicKey();
/* 208 */     return localPublicKey;
/*     */   }
/*     */ 
/*     */   public int getVersion()
/*     */   {
/* 217 */     return this.wrappedCert.getVersion() - 1;
/*     */   }
/*     */ 
/*     */   public BigInteger getSerialNumber()
/*     */   {
/* 226 */     return this.wrappedCert.getSerialNumber();
/*     */   }
/*     */ 
/*     */   public Principal getSubjectDN()
/*     */   {
/* 236 */     return this.wrappedCert.getSubjectDN();
/*     */   }
/*     */ 
/*     */   public Principal getIssuerDN()
/*     */   {
/* 246 */     return this.wrappedCert.getIssuerDN();
/*     */   }
/*     */ 
/*     */   public Date getNotBefore()
/*     */   {
/* 256 */     return this.wrappedCert.getNotBefore();
/*     */   }
/*     */ 
/*     */   public Date getNotAfter()
/*     */   {
/* 266 */     return this.wrappedCert.getNotAfter();
/*     */   }
/*     */ 
/*     */   public String getSigAlgName()
/*     */   {
/* 278 */     return this.wrappedCert.getSigAlgName();
/*     */   }
/*     */ 
/*     */   public String getSigAlgOID()
/*     */   {
/* 289 */     return this.wrappedCert.getSigAlgOID();
/*     */   }
/*     */ 
/*     */   public byte[] getSigAlgParams()
/*     */   {
/* 301 */     return this.wrappedCert.getSigAlgParams();
/*     */   }
/*     */ 
/*     */   private synchronized void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException
/*     */   {
/*     */     try {
/* 307 */       paramObjectOutputStream.write(getEncoded());
/*     */     } catch (javax.security.cert.CertificateEncodingException localCertificateEncodingException) {
/* 309 */       throw new IOException("getEncoded failed: " + localCertificateEncodingException.getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   private synchronized void readObject(ObjectInputStream paramObjectInputStream) throws IOException
/*     */   {
/*     */     try {
/* 316 */       this.wrappedCert = ((java.security.cert.X509Certificate)getFactory().generateCertificate(paramObjectInputStream));
/*     */     }
/*     */     catch (java.security.cert.CertificateException localCertificateException) {
/* 319 */       throw new IOException("generateCertificate failed: " + localCertificateException.getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   public java.security.cert.X509Certificate getX509Certificate() {
/* 324 */     return this.wrappedCert;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.security.cert.internal.x509.X509V1CertImpl
 * JD-Core Version:    0.6.2
 */