/*    */ package javax.net;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.net.InetAddress;
/*    */ import java.net.ServerSocket;
/*    */ import java.net.SocketException;
/*    */ 
/*    */ public abstract class ServerSocketFactory
/*    */ {
/*    */   private static ServerSocketFactory theFactory;
/*    */ 
/*    */   public static ServerSocketFactory getDefault()
/*    */   {
/* 72 */     synchronized (ServerSocketFactory.class) {
/* 73 */       if (theFactory == null)
/*    */       {
/* 80 */         theFactory = new DefaultServerSocketFactory();
/*    */       }
/*    */     }
/*    */ 
/* 84 */     return theFactory;
/*    */   }
/*    */ 
/*    */   public ServerSocket createServerSocket()
/*    */     throws IOException
/*    */   {
/* 99 */     throw new SocketException("Unbound server sockets not implemented");
/*    */   }
/*    */ 
/*    */   public abstract ServerSocket createServerSocket(int paramInt)
/*    */     throws IOException;
/*    */ 
/*    */   public abstract ServerSocket createServerSocket(int paramInt1, int paramInt2)
/*    */     throws IOException;
/*    */ 
/*    */   public abstract ServerSocket createServerSocket(int paramInt1, int paramInt2, InetAddress paramInetAddress)
/*    */     throws IOException;
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.net.ServerSocketFactory
 * JD-Core Version:    0.6.2
 */