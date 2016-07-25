/*    */ package sun.management.jdp;
/*    */ 
/*    */ public abstract class JdpGenericPacket
/*    */   implements JdpPacket
/*    */ {
/*    */   private static final int MAGIC = -1056969150;
/*    */   private static final short PROTOCOL_VERSION = 1;
/*    */ 
/*    */   public static void checkMagic(int paramInt)
/*    */     throws JdpException
/*    */   {
/* 63 */     if (paramInt != -1056969150)
/* 64 */       throw new JdpException("Invalid JDP magic header: " + paramInt);
/*    */   }
/*    */ 
/*    */   public static void checkVersion(short paramShort)
/*    */     throws JdpException
/*    */   {
/* 77 */     if (paramShort > 1)
/* 78 */       throw new JdpException("Unsupported protocol version: " + paramShort);
/*    */   }
/*    */ 
/*    */   public static int getMagic()
/*    */   {
/* 87 */     return -1056969150;
/*    */   }
/*    */ 
/*    */   public static short getVersion()
/*    */   {
/* 95 */     return 1;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.management.jdp.JdpGenericPacket
 * JD-Core Version:    0.6.2
 */