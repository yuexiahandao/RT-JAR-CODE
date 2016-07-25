/*     */ package sun.security.krb5.internal;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.DatagramPacket;
/*     */ import java.net.DatagramSocket;
/*     */ import java.net.InetAddress;
/*     */ import java.net.SocketException;
/*     */ import java.net.UnknownHostException;
/*     */ 
/*     */ class UDPClient extends NetClient
/*     */ {
/*     */   InetAddress iaddr;
/*     */   int iport;
/* 183 */   int bufSize = 65507;
/*     */   DatagramSocket dgSocket;
/*     */   DatagramPacket dgPacketIn;
/*     */ 
/*     */   UDPClient(String paramString, int paramInt1, int paramInt2)
/*     */     throws UnknownHostException, SocketException
/*     */   {
/* 189 */     this.iaddr = InetAddress.getByName(paramString);
/* 190 */     this.iport = paramInt1;
/* 191 */     this.dgSocket = new DatagramSocket();
/* 192 */     this.dgSocket.setSoTimeout(paramInt2);
/*     */   }
/*     */ 
/*     */   public void send(byte[] paramArrayOfByte) throws IOException
/*     */   {
/* 197 */     DatagramPacket localDatagramPacket = new DatagramPacket(paramArrayOfByte, paramArrayOfByte.length, this.iaddr, this.iport);
/*     */ 
/* 199 */     this.dgSocket.send(localDatagramPacket);
/*     */   }
/*     */ 
/*     */   public byte[] receive() throws IOException
/*     */   {
/* 204 */     byte[] arrayOfByte1 = new byte[this.bufSize];
/* 205 */     this.dgPacketIn = new DatagramPacket(arrayOfByte1, arrayOfByte1.length);
/*     */     try {
/* 207 */       this.dgSocket.receive(this.dgPacketIn);
/*     */     }
/*     */     catch (SocketException localSocketException) {
/* 210 */       this.dgSocket.receive(this.dgPacketIn);
/*     */     }
/* 212 */     byte[] arrayOfByte2 = new byte[this.dgPacketIn.getLength()];
/* 213 */     System.arraycopy(this.dgPacketIn.getData(), 0, arrayOfByte2, 0, this.dgPacketIn.getLength());
/*     */ 
/* 215 */     return arrayOfByte2;
/*     */   }
/*     */ 
/*     */   public void close()
/*     */   {
/* 220 */     this.dgSocket.close();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.krb5.internal.UDPClient
 * JD-Core Version:    0.6.2
 */