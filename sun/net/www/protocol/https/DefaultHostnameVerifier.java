/*    */ package sun.net.www.protocol.https;
/*    */ 
/*    */ import javax.net.ssl.HostnameVerifier;
/*    */ import javax.net.ssl.SSLSession;
/*    */ 
/*    */ public final class DefaultHostnameVerifier
/*    */   implements HostnameVerifier
/*    */ {
/*    */   public boolean verify(String paramString, SSLSession paramSSLSession)
/*    */   {
/* 43 */     return false;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.net.www.protocol.https.DefaultHostnameVerifier
 * JD-Core Version:    0.6.2
 */