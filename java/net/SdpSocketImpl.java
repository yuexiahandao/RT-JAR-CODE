/*    */ package java.net;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import sun.net.sdp.SdpSupport;
/*    */ 
/*    */ class SdpSocketImpl extends PlainSocketImpl
/*    */ {
/*    */   protected void create(boolean paramBoolean)
/*    */     throws IOException
/*    */   {
/* 41 */     if (!paramBoolean)
/* 42 */       throw new UnsupportedOperationException("Must be a stream socket");
/* 43 */     this.fd = SdpSupport.createSocket();
/* 44 */     if (this.socket != null)
/* 45 */       this.socket.setCreated();
/* 46 */     if (this.serverSocket != null)
/* 47 */       this.serverSocket.setCreated();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.net.SdpSocketImpl
 * JD-Core Version:    0.6.2
 */