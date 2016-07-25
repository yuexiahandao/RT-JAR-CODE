/*     */ package java.net;
/*     */ 
/*     */ import java.io.ObjectStreamException;
/*     */ 
/*     */ public final class Inet4Address extends InetAddress
/*     */ {
/*     */   static final int INADDRSZ = 4;
/*     */   private static final long serialVersionUID = 3286316764910316507L;
/*     */   private static final int loopback = 2130706433;
/*     */ 
/*     */   Inet4Address()
/*     */   {
/* 105 */     holder().hostName = null;
/* 106 */     holder().address = 0;
/* 107 */     holder().family = 1;
/*     */   }
/*     */ 
/*     */   Inet4Address(String paramString, byte[] paramArrayOfByte) {
/* 111 */     holder().hostName = paramString;
/* 112 */     holder().family = 1;
/* 113 */     if ((paramArrayOfByte != null) && 
/* 114 */       (paramArrayOfByte.length == 4)) {
/* 115 */       int i = paramArrayOfByte[3] & 0xFF;
/* 116 */       i |= paramArrayOfByte[2] << 8 & 0xFF00;
/* 117 */       i |= paramArrayOfByte[1] << 16 & 0xFF0000;
/* 118 */       i |= paramArrayOfByte[0] << 24 & 0xFF000000;
/* 119 */       holder().address = i;
/*     */     }
/*     */   }
/*     */ 
/*     */   Inet4Address(String paramString, int paramInt) {
/* 124 */     holder().hostName = paramString;
/* 125 */     holder().family = 1;
/* 126 */     holder().address = paramInt;
/*     */   }
/*     */ 
/*     */   private Object writeReplace()
/*     */     throws ObjectStreamException
/*     */   {
/* 139 */     InetAddress localInetAddress = new InetAddress();
/* 140 */     localInetAddress.holder().hostName = holder().getHostName();
/* 141 */     localInetAddress.holder().address = holder().getAddress();
/*     */ 
/* 149 */     localInetAddress.holder().family = 2;
/*     */ 
/* 151 */     return localInetAddress;
/*     */   }
/*     */ 
/*     */   public boolean isMulticastAddress()
/*     */   {
/* 163 */     return (holder().getAddress() & 0xF0000000) == -536870912;
/*     */   }
/*     */ 
/*     */   public boolean isAnyLocalAddress()
/*     */   {
/* 173 */     return holder().getAddress() == 0;
/*     */   }
/*     */ 
/*     */   public boolean isLoopbackAddress()
/*     */   {
/* 186 */     byte[] arrayOfByte = getAddress();
/* 187 */     return arrayOfByte[0] == 127;
/*     */   }
/*     */ 
/*     */   public boolean isLinkLocalAddress()
/*     */   {
/* 202 */     int i = holder().getAddress();
/* 203 */     return ((i >>> 24 & 0xFF) == 169) && ((i >>> 16 & 0xFF) == 254);
/*     */   }
/*     */ 
/*     */   public boolean isSiteLocalAddress()
/*     */   {
/* 219 */     int i = holder().getAddress();
/* 220 */     return ((i >>> 24 & 0xFF) == 10) || (((i >>> 24 & 0xFF) == 172) && ((i >>> 16 & 0xF0) == 16)) || (((i >>> 24 & 0xFF) == 192) && ((i >>> 16 & 0xFF) == 168));
/*     */   }
/*     */ 
/*     */   public boolean isMCGlobal()
/*     */   {
/* 237 */     byte[] arrayOfByte = getAddress();
/* 238 */     return ((arrayOfByte[0] & 0xFF) >= 224) && ((arrayOfByte[0] & 0xFF) <= 238) && (((arrayOfByte[0] & 0xFF) != 224) || (arrayOfByte[1] != 0) || (arrayOfByte[2] != 0));
/*     */   }
/*     */ 
/*     */   public boolean isMCNodeLocal()
/*     */   {
/* 253 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean isMCLinkLocal()
/*     */   {
/* 266 */     int i = holder().getAddress();
/* 267 */     return ((i >>> 24 & 0xFF) == 224) && ((i >>> 16 & 0xFF) == 0) && ((i >>> 8 & 0xFF) == 0);
/*     */   }
/*     */ 
/*     */   public boolean isMCSiteLocal()
/*     */   {
/* 282 */     int i = holder().getAddress();
/* 283 */     return ((i >>> 24 & 0xFF) == 239) && ((i >>> 16 & 0xFF) == 255);
/*     */   }
/*     */ 
/*     */   public boolean isMCOrgLocal()
/*     */   {
/* 298 */     int i = holder().getAddress();
/* 299 */     return ((i >>> 24 & 0xFF) == 239) && ((i >>> 16 & 0xFF) >= 192) && ((i >>> 16 & 0xFF) <= 195);
/*     */   }
/*     */ 
/*     */   public byte[] getAddress()
/*     */   {
/* 312 */     int i = holder().getAddress();
/* 313 */     byte[] arrayOfByte = new byte[4];
/*     */ 
/* 315 */     arrayOfByte[0] = ((byte)(i >>> 24 & 0xFF));
/* 316 */     arrayOfByte[1] = ((byte)(i >>> 16 & 0xFF));
/* 317 */     arrayOfByte[2] = ((byte)(i >>> 8 & 0xFF));
/* 318 */     arrayOfByte[3] = ((byte)(i & 0xFF));
/* 319 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */   public String getHostAddress()
/*     */   {
/* 329 */     return numericToTextFormat(getAddress());
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 338 */     return holder().getAddress();
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 358 */     return (paramObject != null) && ((paramObject instanceof Inet4Address)) && (((InetAddress)paramObject).holder().getAddress() == holder().getAddress());
/*     */   }
/*     */ 
/*     */   static String numericToTextFormat(byte[] paramArrayOfByte)
/*     */   {
/* 374 */     return (paramArrayOfByte[0] & 0xFF) + "." + (paramArrayOfByte[1] & 0xFF) + "." + (paramArrayOfByte[2] & 0xFF) + "." + (paramArrayOfByte[3] & 0xFF);
/*     */   }
/*     */ 
/*     */   private static native void init();
/*     */ 
/*     */   static
/*     */   {
/* 100 */     init();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.net.Inet4Address
 * JD-Core Version:    0.6.2
 */