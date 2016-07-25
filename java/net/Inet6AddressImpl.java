/*     */ package java.net;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Enumeration;
/*     */ 
/*     */ class Inet6AddressImpl
/*     */   implements InetAddressImpl
/*     */ {
/*     */   private InetAddress anyLocalAddress;
/*     */   private InetAddress loopbackAddress;
/*     */ 
/*     */   public native String getLocalHostName()
/*     */     throws UnknownHostException;
/*     */ 
/*     */   public native InetAddress[] lookupAllHostAddr(String paramString)
/*     */     throws UnknownHostException;
/*     */ 
/*     */   public native String getHostByAddr(byte[] paramArrayOfByte)
/*     */     throws UnknownHostException;
/*     */ 
/*     */   private native boolean isReachable0(byte[] paramArrayOfByte1, int paramInt1, int paramInt2, byte[] paramArrayOfByte2, int paramInt3, int paramInt4)
/*     */     throws IOException;
/*     */ 
/*     */   public boolean isReachable(InetAddress paramInetAddress, int paramInt1, NetworkInterface paramNetworkInterface, int paramInt2)
/*     */     throws IOException
/*     */   {
/*  47 */     byte[] arrayOfByte = null;
/*  48 */     int i = -1;
/*  49 */     int j = -1;
/*  50 */     if (paramNetworkInterface != null)
/*     */     {
/*  57 */       Enumeration localEnumeration = paramNetworkInterface.getInetAddresses();
/*  58 */       InetAddress localInetAddress = null;
/*  59 */       while (localEnumeration.hasMoreElements()) {
/*  60 */         localInetAddress = (InetAddress)localEnumeration.nextElement();
/*  61 */         if (localInetAddress.getClass().isInstance(paramInetAddress)) {
/*  62 */           arrayOfByte = localInetAddress.getAddress();
/*  63 */           if ((localInetAddress instanceof Inet6Address)) {
/*  64 */             j = ((Inet6Address)localInetAddress).getScopeId();
/*     */           }
/*     */         }
/*     */       }
/*     */ 
/*  69 */       if (arrayOfByte == null)
/*     */       {
/*  72 */         return false;
/*     */       }
/*     */     }
/*  75 */     if ((paramInetAddress instanceof Inet6Address))
/*  76 */       i = ((Inet6Address)paramInetAddress).getScopeId();
/*  77 */     return isReachable0(paramInetAddress.getAddress(), i, paramInt1, arrayOfByte, paramInt2, j);
/*     */   }
/*     */ 
/*     */   public synchronized InetAddress anyLocalAddress() {
/*  81 */     if (this.anyLocalAddress == null) {
/*  82 */       if (InetAddress.preferIPv6Address) {
/*  83 */         this.anyLocalAddress = new Inet6Address();
/*  84 */         this.anyLocalAddress.holder().hostName = "::";
/*     */       } else {
/*  86 */         this.anyLocalAddress = new Inet4AddressImpl().anyLocalAddress();
/*     */       }
/*     */     }
/*  89 */     return this.anyLocalAddress;
/*     */   }
/*     */ 
/*     */   public synchronized InetAddress loopbackAddress() {
/*  93 */     if (this.loopbackAddress == null) {
/*  94 */       if (InetAddress.preferIPv6Address) {
/*  95 */         byte[] arrayOfByte = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 };
/*     */ 
/*  98 */         this.loopbackAddress = new Inet6Address("localhost", arrayOfByte);
/*     */       } else {
/* 100 */         this.loopbackAddress = new Inet4AddressImpl().loopbackAddress();
/*     */       }
/*     */     }
/* 103 */     return this.loopbackAddress;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.net.Inet6AddressImpl
 * JD-Core Version:    0.6.2
 */