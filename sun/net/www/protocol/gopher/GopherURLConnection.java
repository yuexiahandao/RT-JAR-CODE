/*    */ package sun.net.www.protocol.gopher;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.net.SocketPermission;
/*    */ import java.net.URL;
/*    */ import java.security.Permission;
/*    */ import sun.net.www.URLConnection;
/*    */ 
/*    */ class GopherURLConnection extends URLConnection
/*    */ {
/*    */   Permission permission;
/*    */ 
/*    */   GopherURLConnection(URL paramURL)
/*    */   {
/* 81 */     super(paramURL);
/*    */   }
/*    */ 
/*    */   public void connect() throws IOException {
/*    */   }
/*    */ 
/*    */   public InputStream getInputStream() throws IOException {
/* 88 */     return new GopherClient(this).openStream(this.url);
/*    */   }
/*    */ 
/*    */   public Permission getPermission() {
/* 92 */     if (this.permission == null) {
/* 93 */       int i = this.url.getPort();
/* 94 */       i = i < 0 ? 70 : i;
/* 95 */       String str = this.url.getHost() + ":" + this.url.getPort();
/* 96 */       this.permission = new SocketPermission(str, "connect");
/*    */     }
/* 98 */     return this.permission;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.net.www.protocol.gopher.GopherURLConnection
 * JD-Core Version:    0.6.2
 */