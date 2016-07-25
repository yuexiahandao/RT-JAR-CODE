/*    */ package sun.rmi.transport.proxy;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.net.ServerSocket;
/*    */ import java.net.Socket;
/*    */ import java.rmi.server.RMISocketFactory;
/*    */ 
/*    */ public class RMIDirectSocketFactory extends RMISocketFactory
/*    */ {
/*    */   public Socket createSocket(String paramString, int paramInt)
/*    */     throws IOException
/*    */   {
/* 40 */     return new Socket(paramString, paramInt);
/*    */   }
/*    */ 
/*    */   public ServerSocket createServerSocket(int paramInt) throws IOException
/*    */   {
/* 45 */     return new ServerSocket(paramInt);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.rmi.transport.proxy.RMIDirectSocketFactory
 * JD-Core Version:    0.6.2
 */