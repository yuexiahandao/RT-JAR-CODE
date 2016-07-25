/*    */ package sun.net.www.protocol.gopher;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.net.InetSocketAddress;
/*    */ import java.net.Proxy;
/*    */ import java.net.Proxy.Type;
/*    */ import java.net.URL;
/*    */ import java.net.URLConnection;
/*    */ import java.net.URLStreamHandler;
/*    */ import sun.net.www.protocol.http.HttpURLConnection;
/*    */ 
/*    */ public class Handler extends URLStreamHandler
/*    */ {
/*    */   protected int getDefaultPort()
/*    */   {
/* 46 */     return 70;
/*    */   }
/*    */ 
/*    */   public URLConnection openConnection(URL paramURL) throws IOException
/*    */   {
/* 51 */     return openConnection(paramURL, null);
/*    */   }
/*    */ 
/*    */   public URLConnection openConnection(URL paramURL, Proxy paramProxy)
/*    */     throws IOException
/*    */   {
/* 60 */     if ((paramProxy == null) && (GopherClient.getUseGopherProxy())) {
/* 61 */       String str = GopherClient.getGopherProxyHost();
/* 62 */       if (str != null) {
/* 63 */         InetSocketAddress localInetSocketAddress = InetSocketAddress.createUnresolved(str, GopherClient.getGopherProxyPort());
/*    */ 
/* 65 */         paramProxy = new Proxy(Proxy.Type.HTTP, localInetSocketAddress);
/*    */       }
/*    */     }
/* 68 */     if (paramProxy != null) {
/* 69 */       return new HttpURLConnection(paramURL, paramProxy);
/*    */     }
/*    */ 
/* 72 */     return new GopherURLConnection(paramURL);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.net.www.protocol.gopher.Handler
 * JD-Core Version:    0.6.2
 */