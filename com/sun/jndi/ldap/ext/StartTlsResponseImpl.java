/*     */ package com.sun.jndi.ldap.ext;
/*     */ 
/*     */ import com.sun.jndi.ldap.Connection;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.security.Principal;
/*     */ import java.security.cert.Certificate;
/*     */ import java.security.cert.CertificateException;
/*     */ import java.security.cert.X509Certificate;
/*     */ import javax.naming.ldap.StartTlsResponse;
/*     */ import javax.net.ssl.HostnameVerifier;
/*     */ import javax.net.ssl.SSLPeerUnverifiedException;
/*     */ import javax.net.ssl.SSLSession;
/*     */ import javax.net.ssl.SSLSocket;
/*     */ import javax.net.ssl.SSLSocketFactory;
/*     */ import sun.security.util.HostnameChecker;
/*     */ 
/*     */ public final class StartTlsResponseImpl extends StartTlsResponse
/*     */ {
/*     */   private static final boolean debug = false;
/*     */   private static final int DNSNAME_TYPE = 2;
/*  85 */   private transient String hostname = null;
/*     */ 
/*  90 */   private transient Connection ldapConnection = null;
/*     */ 
/*  95 */   private transient InputStream originalInputStream = null;
/*     */ 
/* 100 */   private transient OutputStream originalOutputStream = null;
/*     */ 
/* 105 */   private transient SSLSocket sslSocket = null;
/*     */ 
/* 110 */   private transient SSLSocketFactory defaultFactory = null;
/* 111 */   private transient SSLSocketFactory currentFactory = null;
/*     */ 
/* 116 */   private transient String[] suites = null;
/*     */ 
/* 121 */   private transient HostnameVerifier verifier = null;
/*     */ 
/* 126 */   private transient boolean isClosed = true;
/*     */   private static final long serialVersionUID = -1126624615143411328L;
/*     */ 
/*     */   public void setEnabledCipherSuites(String[] paramArrayOfString)
/*     */   {
/* 147 */     this.suites = paramArrayOfString;
/*     */   }
/*     */ 
/*     */   public void setHostnameVerifier(HostnameVerifier paramHostnameVerifier)
/*     */   {
/* 162 */     this.verifier = paramHostnameVerifier;
/*     */   }
/*     */ 
/*     */   public SSLSession negotiate()
/*     */     throws IOException
/*     */   {
/* 178 */     return negotiate(null);
/*     */   }
/*     */ 
/*     */   public SSLSession negotiate(SSLSocketFactory paramSSLSocketFactory)
/*     */     throws IOException
/*     */   {
/* 213 */     if ((this.isClosed) && (this.sslSocket != null)) {
/* 214 */       throw new IOException("TLS connection is closed.");
/*     */     }
/*     */ 
/* 217 */     if (paramSSLSocketFactory == null) {
/* 218 */       paramSSLSocketFactory = getDefaultFactory();
/*     */     }
/*     */ 
/* 225 */     SSLSession localSSLSession = startHandshake(paramSSLSocketFactory).getSession();
/*     */ 
/* 231 */     Object localObject = null;
/*     */     try {
/* 233 */       if (verify(this.hostname, localSSLSession)) {
/* 234 */         this.isClosed = false;
/* 235 */         return localSSLSession;
/*     */       }
/*     */     }
/*     */     catch (SSLPeerUnverifiedException localSSLPeerUnverifiedException) {
/* 239 */       localObject = localSSLPeerUnverifiedException;
/*     */     }
/* 241 */     if ((this.verifier != null) && (this.verifier.verify(this.hostname, localSSLSession)))
/*     */     {
/* 243 */       this.isClosed = false;
/* 244 */       return localSSLSession;
/*     */     }
/*     */ 
/* 248 */     close();
/* 249 */     localSSLSession.invalidate();
/* 250 */     if (localObject == null) {
/* 251 */       localObject = new SSLPeerUnverifiedException("hostname of the server '" + this.hostname + "' does not match the hostname in the " + "server's certificate.");
/*     */     }
/*     */ 
/* 256 */     throw ((Throwable)localObject);
/*     */   }
/*     */ 
/*     */   public void close()
/*     */     throws IOException
/*     */   {
/* 268 */     if (this.isClosed) {
/* 269 */       return;
/*     */     }
/*     */ 
/* 278 */     this.ldapConnection.replaceStreams(this.originalInputStream, this.originalOutputStream);
/*     */ 
/* 284 */     this.sslSocket.close();
/*     */ 
/* 286 */     this.isClosed = true;
/*     */   }
/*     */ 
/*     */   public void setConnection(Connection paramConnection, String paramString)
/*     */   {
/* 298 */     this.ldapConnection = paramConnection;
/* 299 */     this.hostname = (paramString != null ? paramString : paramConnection.host);
/* 300 */     this.originalInputStream = paramConnection.inStream;
/* 301 */     this.originalOutputStream = paramConnection.outStream;
/*     */   }
/*     */ 
/*     */   private SSLSocketFactory getDefaultFactory()
/*     */     throws IOException
/*     */   {
/* 312 */     if (this.defaultFactory != null) {
/* 313 */       return this.defaultFactory;
/*     */     }
/*     */ 
/* 316 */     return this.defaultFactory = (SSLSocketFactory)SSLSocketFactory.getDefault();
/*     */   }
/*     */ 
/*     */   private SSLSocket startHandshake(SSLSocketFactory paramSSLSocketFactory)
/*     */     throws IOException
/*     */   {
/* 331 */     if (this.ldapConnection == null) {
/* 332 */       throw new IllegalStateException("LDAP connection has not been set. TLS requires an existing LDAP connection.");
/*     */     }
/*     */ 
/* 336 */     if (paramSSLSocketFactory != this.currentFactory)
/*     */     {
/* 338 */       this.sslSocket = ((SSLSocket)paramSSLSocketFactory.createSocket(this.ldapConnection.sock, this.ldapConnection.host, this.ldapConnection.port, false));
/*     */ 
/* 340 */       this.currentFactory = paramSSLSocketFactory;
/*     */     }
/*     */ 
/* 347 */     if (this.suites != null) {
/* 348 */       this.sslSocket.setEnabledCipherSuites(this.suites);
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 361 */       this.sslSocket.startHandshake();
/*     */ 
/* 368 */       this.ldapConnection.replaceStreams(this.sslSocket.getInputStream(), this.sslSocket.getOutputStream());
/*     */     }
/*     */     catch (IOException localIOException)
/*     */     {
/* 380 */       this.sslSocket.close();
/* 381 */       this.isClosed = true;
/* 382 */       throw localIOException;
/*     */     }
/*     */ 
/* 385 */     return this.sslSocket;
/*     */   }
/*     */ 
/*     */   private boolean verify(String paramString, SSLSession paramSSLSession)
/*     */     throws SSLPeerUnverifiedException
/*     */   {
/* 405 */     Certificate[] arrayOfCertificate = null;
/*     */ 
/* 408 */     if ((paramString != null) && (paramString.startsWith("[")) && (paramString.endsWith("]")))
/*     */     {
/* 410 */       paramString = paramString.substring(1, paramString.length() - 1);
/*     */     }
/*     */     try {
/* 413 */       HostnameChecker localHostnameChecker = HostnameChecker.getInstance((byte)2);
/*     */ 
/* 416 */       if (paramSSLSession.getCipherSuite().startsWith("TLS_KRB5")) {
/* 417 */         localObject = getPeerPrincipal(paramSSLSession);
/* 418 */         if (!HostnameChecker.match(paramString, (Principal)localObject)) {
/* 419 */           throw new SSLPeerUnverifiedException("hostname of the kerberos principal:" + localObject + " does not match the hostname:" + paramString);
/*     */         }
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/* 426 */         arrayOfCertificate = paramSSLSession.getPeerCertificates();
/*     */ 
/* 428 */         if ((arrayOfCertificate[0] instanceof X509Certificate))
/* 429 */           localObject = (X509Certificate)arrayOfCertificate[0];
/*     */         else {
/* 431 */           throw new SSLPeerUnverifiedException("Received a non X509Certificate from the server");
/*     */         }
/*     */ 
/* 434 */         localHostnameChecker.match(paramString, (X509Certificate)localObject);
/*     */       }
/*     */ 
/* 438 */       return true;
/*     */     }
/*     */     catch (SSLPeerUnverifiedException localSSLPeerUnverifiedException)
/*     */     {
/* 445 */       Object localObject = paramSSLSession.getCipherSuite();
/* 446 */       if ((localObject != null) && (((String)localObject).indexOf("_anon_") != -1)) {
/* 447 */         return true;
/*     */       }
/* 449 */       throw localSSLPeerUnverifiedException;
/*     */     }
/*     */     catch (CertificateException localCertificateException)
/*     */     {
/* 455 */       throw ((SSLPeerUnverifiedException)new SSLPeerUnverifiedException("hostname of the server '" + paramString + "' does not match the hostname in the " + "server's certificate.").initCause(localCertificateException));
/*     */     }
/*     */   }
/*     */ 
/*     */   private static Principal getPeerPrincipal(SSLSession paramSSLSession)
/*     */     throws SSLPeerUnverifiedException
/*     */   {
/*     */     Principal localPrincipal;
/*     */     try
/*     */     {
/* 470 */       localPrincipal = paramSSLSession.getPeerPrincipal();
/*     */     }
/*     */     catch (AbstractMethodError localAbstractMethodError)
/*     */     {
/* 474 */       localPrincipal = null;
/*     */     }
/* 476 */     return localPrincipal;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jndi.ldap.ext.StartTlsResponseImpl
 * JD-Core Version:    0.6.2
 */