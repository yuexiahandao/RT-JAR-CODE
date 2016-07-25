/*     */ package javax.net.ssl;
/*     */ 
/*     */ import java.security.Principal;
/*     */ import java.security.cert.Certificate;
/*     */ import java.util.EventObject;
/*     */ 
/*     */ public class HandshakeCompletedEvent extends EventObject
/*     */ {
/*     */   private static final long serialVersionUID = 7914963744257769778L;
/*     */   private transient SSLSession session;
/*     */ 
/*     */   public HandshakeCompletedEvent(SSLSocket paramSSLSocket, SSLSession paramSSLSession)
/*     */   {
/*  65 */     super(paramSSLSocket);
/*  66 */     this.session = paramSSLSession;
/*     */   }
/*     */ 
/*     */   public SSLSession getSession()
/*     */   {
/*  77 */     return this.session;
/*     */   }
/*     */ 
/*     */   public String getCipherSuite()
/*     */   {
/*  90 */     return this.session.getCipherSuite();
/*     */   }
/*     */ 
/*     */   public Certificate[] getLocalCertificates()
/*     */   {
/* 114 */     return this.session.getLocalCertificates();
/*     */   }
/*     */ 
/*     */   public Certificate[] getPeerCertificates()
/*     */     throws SSLPeerUnverifiedException
/*     */   {
/* 134 */     return this.session.getPeerCertificates();
/*     */   }
/*     */ 
/*     */   public javax.security.cert.X509Certificate[] getPeerCertificateChain()
/*     */     throws SSLPeerUnverifiedException
/*     */   {
/* 160 */     return this.session.getPeerCertificateChain();
/*     */   }
/*     */ 
/*     */   public Principal getPeerPrincipal()
/*     */     throws SSLPeerUnverifiedException
/*     */   {
/*     */     Object localObject;
/*     */     try
/*     */     {
/* 184 */       localObject = this.session.getPeerPrincipal();
/*     */     }
/*     */     catch (AbstractMethodError localAbstractMethodError)
/*     */     {
/* 188 */       Certificate[] arrayOfCertificate = getPeerCertificates();
/* 189 */       localObject = ((java.security.cert.X509Certificate)arrayOfCertificate[0]).getSubjectX500Principal();
/*     */     }
/*     */ 
/* 192 */     return localObject;
/*     */   }
/*     */ 
/*     */   public Principal getLocalPrincipal()
/*     */   {
/*     */     Object localObject;
/*     */     try
/*     */     {
/* 212 */       localObject = this.session.getLocalPrincipal();
/*     */     } catch (AbstractMethodError localAbstractMethodError) {
/* 214 */       localObject = null;
/*     */ 
/* 217 */       Certificate[] arrayOfCertificate = getLocalCertificates();
/* 218 */       if (arrayOfCertificate != null) {
/* 219 */         localObject = ((java.security.cert.X509Certificate)arrayOfCertificate[0]).getSubjectX500Principal();
/*     */       }
/*     */     }
/*     */ 
/* 223 */     return localObject;
/*     */   }
/*     */ 
/*     */   public SSLSocket getSocket()
/*     */   {
/* 235 */     return (SSLSocket)getSource();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.net.ssl.HandshakeCompletedEvent
 * JD-Core Version:    0.6.2
 */