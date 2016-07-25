/*     */ package com.sun.org.apache.xml.internal.security.keys.storage.implementations;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.security.keys.content.x509.XMLX509SKI;
/*     */ import com.sun.org.apache.xml.internal.security.keys.storage.StorageResolverException;
/*     */ import com.sun.org.apache.xml.internal.security.keys.storage.StorageResolverSpi;
/*     */ import com.sun.org.apache.xml.internal.security.utils.Base64;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.math.BigInteger;
/*     */ import java.security.Principal;
/*     */ import java.security.cert.CertificateException;
/*     */ import java.security.cert.CertificateExpiredException;
/*     */ import java.security.cert.CertificateFactory;
/*     */ import java.security.cert.CertificateNotYetValidException;
/*     */ import java.security.cert.X509Certificate;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ 
/*     */ public class CertsInFilesystemDirectoryResolver extends StorageResolverSpi
/*     */ {
/*  49 */   static Logger log = Logger.getLogger(CertsInFilesystemDirectoryResolver.class.getName());
/*     */ 
/*  54 */   String _merlinsCertificatesDir = null;
/*     */ 
/*  57 */   private List _certs = new ArrayList();
/*     */ 
/*  60 */   Iterator _iterator = null;
/*     */ 
/*     */   public CertsInFilesystemDirectoryResolver(String paramString)
/*     */     throws StorageResolverException
/*     */   {
/*  71 */     this._merlinsCertificatesDir = paramString;
/*     */ 
/*  73 */     readCertsFromHarddrive();
/*     */ 
/*  75 */     this._iterator = new FilesystemIterator(this._certs);
/*     */   }
/*     */ 
/*     */   private void readCertsFromHarddrive()
/*     */     throws StorageResolverException
/*     */   {
/*  85 */     File localFile1 = new File(this._merlinsCertificatesDir);
/*  86 */     ArrayList localArrayList = new ArrayList();
/*  87 */     String[] arrayOfString = localFile1.list();
/*     */ 
/*  89 */     for (int i = 0; i < arrayOfString.length; i++) {
/*  90 */       String str1 = arrayOfString[i];
/*     */ 
/*  92 */       if (str1.endsWith(".crt")) {
/*  93 */         localArrayList.add(arrayOfString[i]);
/*     */       }
/*     */     }
/*     */ 
/*  97 */     CertificateFactory localCertificateFactory = null;
/*     */     try
/*     */     {
/* 100 */       localCertificateFactory = CertificateFactory.getInstance("X.509");
/*     */     } catch (CertificateException localCertificateException1) {
/* 102 */       throw new StorageResolverException("empty", localCertificateException1);
/*     */     }
/*     */ 
/* 105 */     if (localCertificateFactory == null) {
/* 106 */       throw new StorageResolverException("empty");
/*     */     }
/*     */ 
/* 109 */     for (int j = 0; j < localArrayList.size(); j++) {
/* 110 */       String str2 = localFile1.getAbsolutePath() + File.separator + (String)localArrayList.get(j);
/*     */ 
/* 112 */       File localFile2 = new File(str2);
/* 113 */       int k = 0;
/* 114 */       String str3 = null;
/*     */       try
/*     */       {
/* 117 */         FileInputStream localFileInputStream = new FileInputStream(localFile2);
/* 118 */         X509Certificate localX509Certificate = (X509Certificate)localCertificateFactory.generateCertificate(localFileInputStream);
/*     */ 
/* 121 */         localFileInputStream.close();
/*     */ 
/* 124 */         localX509Certificate.checkValidity();
/* 125 */         this._certs.add(localX509Certificate);
/*     */ 
/* 127 */         str3 = localX509Certificate.getSubjectDN().getName();
/* 128 */         k = 1;
/*     */       } catch (FileNotFoundException localFileNotFoundException) {
/* 130 */         log.log(Level.FINE, "Could not add certificate from file " + str2, localFileNotFoundException);
/*     */       } catch (IOException localIOException) {
/* 132 */         log.log(Level.FINE, "Could not add certificate from file " + str2, localIOException);
/*     */       } catch (CertificateNotYetValidException localCertificateNotYetValidException) {
/* 134 */         log.log(Level.FINE, "Could not add certificate from file " + str2, localCertificateNotYetValidException);
/*     */       } catch (CertificateExpiredException localCertificateExpiredException) {
/* 136 */         log.log(Level.FINE, "Could not add certificate from file " + str2, localCertificateExpiredException);
/*     */       } catch (CertificateException localCertificateException2) {
/* 138 */         log.log(Level.FINE, "Could not add certificate from file " + str2, localCertificateException2);
/*     */       }
/*     */ 
/* 141 */       if ((k != 0) && 
/* 142 */         (log.isLoggable(Level.FINE)))
/* 143 */         log.log(Level.FINE, "Added certificate: " + str3);
/*     */     }
/*     */   }
/*     */ 
/*     */   public Iterator getIterator()
/*     */   {
/* 150 */     return this._iterator;
/*     */   }
/*     */ 
/*     */   public static void main(String[] paramArrayOfString)
/*     */     throws Exception
/*     */   {
/* 205 */     CertsInFilesystemDirectoryResolver localCertsInFilesystemDirectoryResolver = new CertsInFilesystemDirectoryResolver("data/ie/baltimore/merlin-examples/merlin-xmldsig-eighteen/certs");
/*     */ 
/* 209 */     for (Iterator localIterator = localCertsInFilesystemDirectoryResolver.getIterator(); localIterator.hasNext(); ) {
/* 210 */       X509Certificate localX509Certificate = (X509Certificate)localIterator.next();
/* 211 */       byte[] arrayOfByte = XMLX509SKI.getSKIBytesFromCert(localX509Certificate);
/*     */ 
/* 215 */       System.out.println();
/* 216 */       System.out.println("Base64(SKI())=                 \"" + Base64.encode(arrayOfByte) + "\"");
/*     */ 
/* 218 */       System.out.println("cert.getSerialNumber()=        \"" + localX509Certificate.getSerialNumber().toString() + "\"");
/*     */ 
/* 220 */       System.out.println("cert.getSubjectDN().getName()= \"" + localX509Certificate.getSubjectDN().getName() + "\"");
/*     */ 
/* 222 */       System.out.println("cert.getIssuerDN().getName()=  \"" + localX509Certificate.getIssuerDN().getName() + "\"");
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class FilesystemIterator
/*     */     implements Iterator
/*     */   {
/* 162 */     List _certs = null;
/*     */     int _i;
/*     */ 
/*     */     public FilesystemIterator(List paramList)
/*     */     {
/* 173 */       this._certs = paramList;
/* 174 */       this._i = 0;
/*     */     }
/*     */ 
/*     */     public boolean hasNext()
/*     */     {
/* 179 */       return this._i < this._certs.size();
/*     */     }
/*     */ 
/*     */     public Object next()
/*     */     {
/* 184 */       return this._certs.get(this._i++);
/*     */     }
/*     */ 
/*     */     public void remove()
/*     */     {
/* 192 */       throw new UnsupportedOperationException("Can't remove keys from KeyStore");
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.security.keys.storage.implementations.CertsInFilesystemDirectoryResolver
 * JD-Core Version:    0.6.2
 */