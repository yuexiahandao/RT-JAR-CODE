/*    */ package com.sun.net.ssl.internal.www.protocol.https;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.net.Proxy;
/*    */ import java.net.URL;
/*    */ import java.net.URLConnection;
/*    */ 
/*    */ public class Handler extends sun.net.www.protocol.https.Handler
/*    */ {
/*    */   public Handler()
/*    */   {
/*    */   }
/*    */ 
/*    */   public Handler(String paramString, int paramInt)
/*    */   {
/* 45 */     super(paramString, paramInt);
/*    */   }
/*    */ 
/*    */   protected URLConnection openConnection(URL paramURL) throws IOException {
/* 49 */     return openConnection(paramURL, (Proxy)null);
/*    */   }
/*    */ 
/*    */   protected URLConnection openConnection(URL paramURL, Proxy paramProxy) throws IOException {
/* 53 */     return new HttpsURLConnectionOldImpl(paramURL, paramProxy, this);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.net.ssl.internal.www.protocol.https.Handler
 * JD-Core Version:    0.6.2
 */