/*    */ package sun.management.jdp;
/*    */ 
/*    */ import java.io.ByteArrayOutputStream;
/*    */ import java.io.DataOutputStream;
/*    */ import java.io.IOException;
/*    */ 
/*    */ public final class JdpPacketWriter
/*    */ {
/*    */   private final ByteArrayOutputStream baos;
/*    */   private final DataOutputStream pkt;
/*    */ 
/*    */   public JdpPacketWriter()
/*    */     throws IOException
/*    */   {
/* 48 */     this.baos = new ByteArrayOutputStream();
/* 49 */     this.pkt = new DataOutputStream(this.baos);
/*    */ 
/* 51 */     this.pkt.writeInt(JdpGenericPacket.getMagic());
/* 52 */     this.pkt.writeShort(JdpGenericPacket.getVersion());
/*    */   }
/*    */ 
/*    */   public void addEntry(String paramString)
/*    */     throws IOException
/*    */   {
/* 68 */     this.pkt.writeUTF(paramString);
/*    */   }
/*    */ 
/*    */   public void addEntry(String paramString1, String paramString2)
/*    */     throws IOException
/*    */   {
/* 84 */     if (paramString2 != null) {
/* 85 */       addEntry(paramString1);
/* 86 */       addEntry(paramString2);
/*    */     }
/*    */   }
/*    */ 
/*    */   public byte[] getPacketBytes()
/*    */   {
/* 96 */     return this.baos.toByteArray();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.management.jdp.JdpPacketWriter
 * JD-Core Version:    0.6.2
 */