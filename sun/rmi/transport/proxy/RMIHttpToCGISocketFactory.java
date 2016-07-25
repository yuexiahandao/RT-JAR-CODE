/*    */ package sun.rmi.transport.proxy;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.net.ServerSocket;
/*    */ import java.net.Socket;
/*    */ import java.net.URL;
/*    */ import java.rmi.server.RMISocketFactory;
/*    */ 
/*    */ public class RMIHttpToCGISocketFactory extends RMISocketFactory
/*    */ {
/*    */   public Socket createSocket(String paramString, int paramInt)
/*    */     throws IOException
/*    */   {
/* 45 */     return new HttpSendSocket(paramString, paramInt, new URL("http", paramString, "/cgi-bin/java-rmi.cgi?forward=" + paramInt));
/*    */   }
/*    */ 
/*    */   public ServerSocket createServerSocket(int paramInt)
/*    */     throws IOException
/*    */   {
/* 53 */     return new HttpAwareServerSocket(paramInt);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.rmi.transport.proxy.RMIHttpToCGISocketFactory
 * JD-Core Version:    0.6.2
 */