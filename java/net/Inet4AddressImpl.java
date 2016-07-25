/*    */ package java.net;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.util.Enumeration;
/*    */ 
/*    */ class Inet4AddressImpl
/*    */   implements InetAddressImpl
/*    */ {
/*    */   private InetAddress anyLocalAddress;
/*    */   private InetAddress loopbackAddress;
/*    */ 
/*    */   public native String getLocalHostName()
/*    */     throws UnknownHostException;
/*    */ 
/*    */   public native InetAddress[] lookupAllHostAddr(String paramString)
/*    */     throws UnknownHostException;
/*    */ 
/*    */   public native String getHostByAddr(byte[] paramArrayOfByte)
/*    */     throws UnknownHostException;
/*    */ 
/*    */   private native boolean isReachable0(byte[] paramArrayOfByte1, int paramInt1, byte[] paramArrayOfByte2, int paramInt2)
/*    */     throws IOException;
/*    */ 
/*    */   public synchronized InetAddress anyLocalAddress()
/*    */   {
/* 41 */     if (this.anyLocalAddress == null) {
/* 42 */       this.anyLocalAddress = new Inet4Address();
/* 43 */       this.anyLocalAddress.holder().hostName = "0.0.0.0";
/*    */     }
/* 45 */     return this.anyLocalAddress;
/*    */   }
/*    */ 
/*    */   public synchronized InetAddress loopbackAddress() {
/* 49 */     if (this.loopbackAddress == null) {
/* 50 */       byte[] arrayOfByte = { 127, 0, 0, 1 };
/* 51 */       this.loopbackAddress = new Inet4Address("localhost", arrayOfByte);
/*    */     }
/* 53 */     return this.loopbackAddress;
/*    */   }
/*    */ 
/*    */   public boolean isReachable(InetAddress paramInetAddress, int paramInt1, NetworkInterface paramNetworkInterface, int paramInt2) throws IOException {
/* 57 */     byte[] arrayOfByte = null;
/* 58 */     if (paramNetworkInterface != null)
/*    */     {
/* 62 */       Enumeration localEnumeration = paramNetworkInterface.getInetAddresses();
/* 63 */       InetAddress localInetAddress = null;
/* 64 */       while ((!(localInetAddress instanceof Inet4Address)) && (localEnumeration.hasMoreElements()))
/*    */       {
/* 66 */         localInetAddress = (InetAddress)localEnumeration.nextElement();
/* 67 */       }if ((localInetAddress instanceof Inet4Address))
/* 68 */         arrayOfByte = localInetAddress.getAddress();
/*    */     }
/* 70 */     return isReachable0(paramInetAddress.getAddress(), paramInt1, arrayOfByte, paramInt2);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.net.Inet4AddressImpl
 * JD-Core Version:    0.6.2
 */