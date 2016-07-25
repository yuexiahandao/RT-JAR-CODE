/*     */ package com.sun.net.ssl.internal.www.protocol.https;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.security.Principal;
/*     */ import java.security.cert.Certificate;
/*     */ import java.security.cert.CertificateException;
/*     */ import java.security.cert.X509Certificate;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import javax.net.ssl.SSLPeerUnverifiedException;
/*     */ import javax.net.ssl.SSLSession;
/*     */ import sun.security.util.DerValue;
/*     */ import sun.security.util.HostnameChecker;
/*     */ import sun.security.x509.X500Name;
/*     */ 
/*     */ class VerifierWrapper
/*     */   implements javax.net.ssl.HostnameVerifier
/*     */ {
/*     */   private com.sun.net.ssl.HostnameVerifier verifier;
/*     */ 
/*     */   VerifierWrapper(com.sun.net.ssl.HostnameVerifier paramHostnameVerifier)
/*     */   {
/* 105 */     this.verifier = paramHostnameVerifier;
/*     */   }
/*     */ 
/*     */   public boolean verify(String paramString, SSLSession paramSSLSession)
/*     */   {
/*     */     try
/*     */     {
/*     */       String str;
/* 118 */       if (paramSSLSession.getCipherSuite().startsWith("TLS_KRB5")) {
/* 119 */         str = HostnameChecker.getServerName(getPeerPrincipal(paramSSLSession));
/*     */       }
/*     */       else
/*     */       {
/* 123 */         Certificate[] arrayOfCertificate = paramSSLSession.getPeerCertificates();
/* 124 */         if ((arrayOfCertificate == null) || (arrayOfCertificate.length == 0)) {
/* 125 */           return false;
/*     */         }
/* 127 */         if (!(arrayOfCertificate[0] instanceof X509Certificate)) {
/* 128 */           return false;
/*     */         }
/* 130 */         X509Certificate localX509Certificate = (X509Certificate)arrayOfCertificate[0];
/* 131 */         str = getServername(localX509Certificate);
/*     */       }
/* 133 */       if (str == null) {
/* 134 */         return false;
/*     */       }
/* 136 */       return this.verifier.verify(paramString, str); } catch (SSLPeerUnverifiedException localSSLPeerUnverifiedException) {
/*     */     }
/* 138 */     return false;
/*     */   }
/*     */ 
/*     */   private Principal getPeerPrincipal(SSLSession paramSSLSession)
/*     */     throws SSLPeerUnverifiedException
/*     */   {
/*     */     Principal localPrincipal;
/*     */     try
/*     */     {
/* 150 */       localPrincipal = paramSSLSession.getPeerPrincipal();
/*     */     }
/*     */     catch (AbstractMethodError localAbstractMethodError)
/*     */     {
/* 154 */       localPrincipal = null;
/*     */     }
/* 156 */     return localPrincipal;
/*     */   }
/*     */ 
/*     */   private static String getServername(X509Certificate paramX509Certificate)
/*     */   {
/*     */     try
/*     */     {
/* 168 */       Collection localCollection = paramX509Certificate.getSubjectAlternativeNames();
/* 169 */       if (localCollection != null)
/* 170 */         for (localObject1 = localCollection.iterator(); ((Iterator)localObject1).hasNext(); ) {
/* 171 */           localObject2 = (List)((Iterator)localObject1).next();
/* 172 */           if (((Integer)((List)localObject2).get(0)).intValue() == 2)
/*     */           {
/* 174 */             return (String)((List)localObject2).get(1);
/*     */           }
/*     */         }
/*     */       String str;
/* 181 */       Object localObject1 = HostnameChecker.getSubjectX500Name(paramX509Certificate);
/*     */ 
/* 183 */       Object localObject2 = ((X500Name)localObject1).findMostSpecificAttribute(X500Name.commonName_oid);
/*     */ 
/* 185 */       if (localObject2 != null)
/*     */         try {
/* 187 */           return ((DerValue)localObject2).getAsString();
/*     */         }
/*     */         catch (IOException localIOException)
/*     */         {
/*     */         }
/*     */     }
/*     */     catch (CertificateException localCertificateException)
/*     */     {
/*     */     }
/* 196 */     return null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.net.ssl.internal.www.protocol.https.VerifierWrapper
 * JD-Core Version:    0.6.2
 */