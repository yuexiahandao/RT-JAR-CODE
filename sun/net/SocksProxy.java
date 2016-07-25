/*    */ package sun.net;
/*    */ 
/*    */ import java.net.Proxy;
/*    */ import java.net.Proxy.Type;
/*    */ import java.net.SocketAddress;
/*    */ 
/*    */ public final class SocksProxy extends Proxy
/*    */ {
/*    */   private final int version;
/*    */ 
/*    */   private SocksProxy(SocketAddress paramSocketAddress, int paramInt)
/*    */   {
/* 38 */     super(Proxy.Type.SOCKS, paramSocketAddress);
/* 39 */     this.version = paramInt;
/*    */   }
/*    */ 
/*    */   public static SocksProxy create(SocketAddress paramSocketAddress, int paramInt) {
/* 43 */     return new SocksProxy(paramSocketAddress, paramInt);
/*    */   }
/*    */ 
/*    */   public int protocolVersion() {
/* 47 */     return this.version;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.net.SocksProxy
 * JD-Core Version:    0.6.2
 */