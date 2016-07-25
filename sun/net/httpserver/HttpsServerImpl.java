/*    */ package sun.net.httpserver;
/*    */ 
/*    */ import com.sun.net.httpserver.HttpContext;
/*    */ import com.sun.net.httpserver.HttpHandler;
/*    */ import com.sun.net.httpserver.HttpsConfigurator;
/*    */ import com.sun.net.httpserver.HttpsServer;
/*    */ import java.io.IOException;
/*    */ import java.net.InetSocketAddress;
/*    */ import java.util.concurrent.Executor;
/*    */ 
/*    */ public class HttpsServerImpl extends HttpsServer
/*    */ {
/*    */   ServerImpl server;
/*    */ 
/*    */   HttpsServerImpl()
/*    */     throws IOException
/*    */   {
/* 44 */     this(new InetSocketAddress(443), 0);
/*    */   }
/*    */ 
/*    */   HttpsServerImpl(InetSocketAddress paramInetSocketAddress, int paramInt)
/*    */     throws IOException
/*    */   {
/* 50 */     this.server = new ServerImpl(this, "https", paramInetSocketAddress, paramInt);
/*    */   }
/*    */ 
/*    */   public void setHttpsConfigurator(HttpsConfigurator paramHttpsConfigurator) {
/* 54 */     this.server.setHttpsConfigurator(paramHttpsConfigurator);
/*    */   }
/*    */ 
/*    */   public HttpsConfigurator getHttpsConfigurator() {
/* 58 */     return this.server.getHttpsConfigurator();
/*    */   }
/*    */ 
/*    */   public void bind(InetSocketAddress paramInetSocketAddress, int paramInt) throws IOException {
/* 62 */     this.server.bind(paramInetSocketAddress, paramInt);
/*    */   }
/*    */ 
/*    */   public void start() {
/* 66 */     this.server.start();
/*    */   }
/*    */ 
/*    */   public void setExecutor(Executor paramExecutor) {
/* 70 */     this.server.setExecutor(paramExecutor);
/*    */   }
/*    */ 
/*    */   public Executor getExecutor() {
/* 74 */     return this.server.getExecutor();
/*    */   }
/*    */ 
/*    */   public void stop(int paramInt) {
/* 78 */     this.server.stop(paramInt);
/*    */   }
/*    */ 
/*    */   public HttpContextImpl createContext(String paramString, HttpHandler paramHttpHandler) {
/* 82 */     return this.server.createContext(paramString, paramHttpHandler);
/*    */   }
/*    */ 
/*    */   public HttpContextImpl createContext(String paramString) {
/* 86 */     return this.server.createContext(paramString);
/*    */   }
/*    */ 
/*    */   public void removeContext(String paramString) throws IllegalArgumentException {
/* 90 */     this.server.removeContext(paramString);
/*    */   }
/*    */ 
/*    */   public void removeContext(HttpContext paramHttpContext) throws IllegalArgumentException {
/* 94 */     this.server.removeContext(paramHttpContext);
/*    */   }
/*    */ 
/*    */   public InetSocketAddress getAddress() {
/* 98 */     return this.server.getAddress();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.net.httpserver.HttpsServerImpl
 * JD-Core Version:    0.6.2
 */