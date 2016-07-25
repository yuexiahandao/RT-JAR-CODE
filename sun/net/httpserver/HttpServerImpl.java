/*    */ package sun.net.httpserver;
/*    */ 
/*    */ import com.sun.net.httpserver.HttpContext;
/*    */ import com.sun.net.httpserver.HttpHandler;
/*    */ import com.sun.net.httpserver.HttpServer;
/*    */ import java.io.IOException;
/*    */ import java.net.InetSocketAddress;
/*    */ import java.util.concurrent.Executor;
/*    */ 
/*    */ public class HttpServerImpl extends HttpServer
/*    */ {
/*    */   ServerImpl server;
/*    */ 
/*    */   HttpServerImpl()
/*    */     throws IOException
/*    */   {
/* 44 */     this(new InetSocketAddress(80), 0);
/*    */   }
/*    */ 
/*    */   HttpServerImpl(InetSocketAddress paramInetSocketAddress, int paramInt)
/*    */     throws IOException
/*    */   {
/* 50 */     this.server = new ServerImpl(this, "http", paramInetSocketAddress, paramInt);
/*    */   }
/*    */ 
/*    */   public void bind(InetSocketAddress paramInetSocketAddress, int paramInt) throws IOException {
/* 54 */     this.server.bind(paramInetSocketAddress, paramInt);
/*    */   }
/*    */ 
/*    */   public void start() {
/* 58 */     this.server.start();
/*    */   }
/*    */ 
/*    */   public void setExecutor(Executor paramExecutor) {
/* 62 */     this.server.setExecutor(paramExecutor);
/*    */   }
/*    */ 
/*    */   public Executor getExecutor() {
/* 66 */     return this.server.getExecutor();
/*    */   }
/*    */ 
/*    */   public void stop(int paramInt) {
/* 70 */     this.server.stop(paramInt);
/*    */   }
/*    */ 
/*    */   public HttpContextImpl createContext(String paramString, HttpHandler paramHttpHandler) {
/* 74 */     return this.server.createContext(paramString, paramHttpHandler);
/*    */   }
/*    */ 
/*    */   public HttpContextImpl createContext(String paramString) {
/* 78 */     return this.server.createContext(paramString);
/*    */   }
/*    */ 
/*    */   public void removeContext(String paramString) throws IllegalArgumentException {
/* 82 */     this.server.removeContext(paramString);
/*    */   }
/*    */ 
/*    */   public void removeContext(HttpContext paramHttpContext) throws IllegalArgumentException {
/* 86 */     this.server.removeContext(paramHttpContext);
/*    */   }
/*    */ 
/*    */   public InetSocketAddress getAddress() {
/* 90 */     return this.server.getAddress();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.net.httpserver.HttpServerImpl
 * JD-Core Version:    0.6.2
 */