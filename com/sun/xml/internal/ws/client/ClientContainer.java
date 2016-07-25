/*    */ package com.sun.xml.internal.ws.client;
/*    */ 
/*    */ import com.sun.xml.internal.ws.api.ResourceLoader;
/*    */ import com.sun.xml.internal.ws.api.server.Container;
/*    */ import java.net.MalformedURLException;
/*    */ import java.net.URL;
/*    */ 
/*    */ final class ClientContainer extends Container
/*    */ {
/* 39 */   private final ResourceLoader loader = new ResourceLoader() {
/*    */     public URL getResource(String resource) throws MalformedURLException {
/* 41 */       ClassLoader cl = Thread.currentThread().getContextClassLoader();
/* 42 */       if (cl == null) {
/* 43 */         cl = getClass().getClassLoader();
/*    */       }
/* 45 */       return cl.getResource("META-INF/" + resource);
/*    */     }
/* 39 */   };
/*    */ 
/*    */   public <T> T getSPI(Class<T> spiType)
/*    */   {
/* 50 */     if (spiType == ResourceLoader.class) {
/* 51 */       return spiType.cast(this.loader);
/*    */     }
/* 53 */     return null;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.client.ClientContainer
 * JD-Core Version:    0.6.2
 */