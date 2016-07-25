/*    */ package com.sun.net.ssl.internal.www.protocol.https;
/*    */ 
/*    */ import com.sun.net.ssl.HttpsURLConnection;
/*    */ import java.io.IOException;
/*    */ import java.net.Proxy;
/*    */ import java.net.URL;
/*    */ import javax.net.ssl.HostnameVerifier;
/*    */ import javax.net.ssl.SSLSocketFactory;
/*    */ import sun.net.www.protocol.http.Handler;
/*    */ import sun.net.www.protocol.https.AbstractDelegateHttpsURLConnection;
/*    */ 
/*    */ public class DelegateHttpsURLConnection extends AbstractDelegateHttpsURLConnection
/*    */ {
/*    */   public HttpsURLConnection httpsURLConnection;
/*    */ 
/*    */   DelegateHttpsURLConnection(URL paramURL, Handler paramHandler, HttpsURLConnection paramHttpsURLConnection)
/*    */     throws IOException
/*    */   {
/* 71 */     this(paramURL, null, paramHandler, paramHttpsURLConnection);
/*    */   }
/*    */ 
/*    */   DelegateHttpsURLConnection(URL paramURL, Proxy paramProxy, Handler paramHandler, HttpsURLConnection paramHttpsURLConnection)
/*    */     throws IOException
/*    */   {
/* 78 */     super(paramURL, paramProxy, paramHandler);
/* 79 */     this.httpsURLConnection = paramHttpsURLConnection;
/*    */   }
/*    */ 
/*    */   protected SSLSocketFactory getSSLSocketFactory() {
/* 83 */     return this.httpsURLConnection.getSSLSocketFactory();
/*    */   }
/*    */ 
/*    */   protected HostnameVerifier getHostnameVerifier()
/*    */   {
/* 88 */     return new VerifierWrapper(this.httpsURLConnection.getHostnameVerifier());
/*    */   }
/*    */ 
/*    */   protected void dispose()
/*    */     throws Throwable
/*    */   {
/* 96 */     super.finalize();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.net.ssl.internal.www.protocol.https.DelegateHttpsURLConnection
 * JD-Core Version:    0.6.2
 */