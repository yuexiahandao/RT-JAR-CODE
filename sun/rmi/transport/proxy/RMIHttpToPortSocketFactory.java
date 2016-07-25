/*    */ package sun.rmi.transport.proxy;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.net.ServerSocket;
/*    */ import java.net.Socket;
/*    */ import java.net.URL;
/*    */ import java.rmi.server.RMISocketFactory;
/*    */ 
/*    */ public class RMIHttpToPortSocketFactory extends RMISocketFactory
/*    */ {
/*    */   public Socket createSocket(String paramString, int paramInt)
/*    */     throws IOException
/*    */   {
/* 44 */     return new HttpSendSocket(paramString, paramInt, new URL("http", paramString, paramInt, "/"));
/*    */   }
/*    */ 
/*    */   public ServerSocket createServerSocket(int paramInt)
/*    */     throws IOException
/*    */   {
/* 51 */     return new HttpAwareServerSocket(paramInt);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.rmi.transport.proxy.RMIHttpToPortSocketFactory
 * JD-Core Version:    0.6.2
 */