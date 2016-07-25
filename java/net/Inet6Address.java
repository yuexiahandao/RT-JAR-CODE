/*     */ package java.net;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InvalidObjectException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectInputStream.GetField;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.ObjectOutputStream.PutField;
/*     */ import java.io.ObjectStreamField;
/*     */ import java.util.Enumeration;
/*     */ import sun.misc.Unsafe;
/*     */ 
/*     */ public final class Inet6Address extends InetAddress
/*     */ {
/*     */   static final int INADDRSZ = 16;
/* 174 */   private transient int cached_scope_id = 0;
/*     */   private final transient Inet6AddressHolder holder6;
/*     */   private static final long serialVersionUID = 6880410070516793377L;
/*     */   private static final ObjectStreamField[] serialPersistentFields;
/*     */   private static final long FIELDS_OFFSET;
/*     */   private static final Unsafe UNSAFE;
/*     */   private static final int INT16SZ = 2;
/*     */ 
/*     */   Inet6Address()
/*     */   {
/* 378 */     this.holder.init(null, 2);
/* 379 */     this.holder6 = new Inet6AddressHolder(null);
/*     */   }
/*     */ 
/*     */   Inet6Address(String paramString, byte[] paramArrayOfByte, int paramInt)
/*     */   {
/* 386 */     this.holder.init(paramString, 2);
/* 387 */     this.holder6 = new Inet6AddressHolder(null);
/* 388 */     this.holder6.init(paramArrayOfByte, paramInt);
/*     */   }
/*     */ 
/*     */   Inet6Address(String paramString, byte[] paramArrayOfByte) {
/* 392 */     this.holder6 = new Inet6AddressHolder(null);
/*     */     try {
/* 394 */       initif(paramString, paramArrayOfByte, null); } catch (UnknownHostException localUnknownHostException) {
/*     */     }
/*     */   }
/*     */ 
/*     */   Inet6Address(String paramString, byte[] paramArrayOfByte, NetworkInterface paramNetworkInterface) throws UnknownHostException {
/* 399 */     this.holder6 = new Inet6AddressHolder(null);
/* 400 */     initif(paramString, paramArrayOfByte, paramNetworkInterface);
/*     */   }
/*     */ 
/*     */   Inet6Address(String paramString1, byte[] paramArrayOfByte, String paramString2) throws UnknownHostException {
/* 404 */     this.holder6 = new Inet6AddressHolder(null);
/* 405 */     initstr(paramString1, paramArrayOfByte, paramString2);
/*     */   }
/*     */ 
/*     */   public static Inet6Address getByAddress(String paramString, byte[] paramArrayOfByte, NetworkInterface paramNetworkInterface)
/*     */     throws UnknownHostException
/*     */   {
/* 429 */     if ((paramString != null) && (paramString.length() > 0) && (paramString.charAt(0) == '[') && 
/* 430 */       (paramString.charAt(paramString.length() - 1) == ']')) {
/* 431 */       paramString = paramString.substring(1, paramString.length() - 1);
/*     */     }
/*     */ 
/* 434 */     if ((paramArrayOfByte != null) && 
/* 435 */       (paramArrayOfByte.length == 16)) {
/* 436 */       return new Inet6Address(paramString, paramArrayOfByte, paramNetworkInterface);
/*     */     }
/*     */ 
/* 439 */     throw new UnknownHostException("addr is of illegal length");
/*     */   }
/*     */ 
/*     */   public static Inet6Address getByAddress(String paramString, byte[] paramArrayOfByte, int paramInt)
/*     */     throws UnknownHostException
/*     */   {
/* 460 */     if ((paramString != null) && (paramString.length() > 0) && (paramString.charAt(0) == '[') && 
/* 461 */       (paramString.charAt(paramString.length() - 1) == ']')) {
/* 462 */       paramString = paramString.substring(1, paramString.length() - 1);
/*     */     }
/*     */ 
/* 465 */     if ((paramArrayOfByte != null) && 
/* 466 */       (paramArrayOfByte.length == 16)) {
/* 467 */       return new Inet6Address(paramString, paramArrayOfByte, paramInt);
/*     */     }
/*     */ 
/* 470 */     throw new UnknownHostException("addr is of illegal length");
/*     */   }
/*     */ 
/*     */   private void initstr(String paramString1, byte[] paramArrayOfByte, String paramString2) throws UnknownHostException {
/*     */     try {
/* 475 */       NetworkInterface localNetworkInterface = NetworkInterface.getByName(paramString2);
/* 476 */       if (localNetworkInterface == null) {
/* 477 */         throw new UnknownHostException("no such interface " + paramString2);
/*     */       }
/* 479 */       initif(paramString1, paramArrayOfByte, localNetworkInterface);
/*     */     } catch (SocketException localSocketException) {
/* 481 */       throw new UnknownHostException("SocketException thrown" + paramString2);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void initif(String paramString, byte[] paramArrayOfByte, NetworkInterface paramNetworkInterface) throws UnknownHostException {
/* 486 */     int i = -1;
/* 487 */     this.holder6.init(paramArrayOfByte, paramNetworkInterface);
/*     */ 
/* 489 */     if (paramArrayOfByte.length == 16) {
/* 490 */       i = 2;
/*     */     }
/* 492 */     this.holder.init(paramString, i);
/*     */   }
/*     */ 
/*     */   private static boolean differentLocalAddressTypes(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
/*     */   {
/* 503 */     if ((isLinkLocalAddress(paramArrayOfByte1)) && (!isLinkLocalAddress(paramArrayOfByte2)))
/*     */     {
/* 505 */       return false;
/*     */     }
/* 507 */     if ((isSiteLocalAddress(paramArrayOfByte1)) && (!isSiteLocalAddress(paramArrayOfByte2)))
/*     */     {
/* 509 */       return false;
/*     */     }
/* 511 */     return true;
/*     */   }
/*     */ 
/*     */   private static int deriveNumericScope(byte[] paramArrayOfByte, NetworkInterface paramNetworkInterface) throws UnknownHostException {
/* 515 */     Enumeration localEnumeration = paramNetworkInterface.getInetAddresses();
/* 516 */     while (localEnumeration.hasMoreElements()) {
/* 517 */       InetAddress localInetAddress = (InetAddress)localEnumeration.nextElement();
/* 518 */       if ((localInetAddress instanceof Inet6Address))
/*     */       {
/* 521 */         Inet6Address localInet6Address = (Inet6Address)localInetAddress;
/*     */ 
/* 523 */         if (differentLocalAddressTypes(paramArrayOfByte, localInet6Address.getAddress()))
/*     */         {
/* 528 */           return localInet6Address.getScopeId();
/*     */         }
/*     */       }
/*     */     }
/* 530 */     throw new UnknownHostException("no scope_id found");
/*     */   }
/*     */ 
/*     */   private int deriveNumericScope(String paramString) throws UnknownHostException {
/*     */     Enumeration localEnumeration;
/*     */     try {
/* 536 */       localEnumeration = NetworkInterface.getNetworkInterfaces();
/*     */     } catch (SocketException localSocketException) {
/* 538 */       throw new UnknownHostException("could not enumerate local network interfaces");
/*     */     }
/* 540 */     while (localEnumeration.hasMoreElements()) {
/* 541 */       NetworkInterface localNetworkInterface = (NetworkInterface)localEnumeration.nextElement();
/* 542 */       if (localNetworkInterface.getName().equals(paramString)) {
/* 543 */         return deriveNumericScope(this.holder6.ipaddress, localNetworkInterface);
/*     */       }
/*     */     }
/* 546 */     throw new UnknownHostException("No matching address found for interface : " + paramString);
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream)
/*     */     throws IOException, ClassNotFoundException
/*     */   {
/* 586 */     NetworkInterface localNetworkInterface = null;
/*     */ 
/* 588 */     if (getClass().getClassLoader() != null) {
/* 589 */       throw new SecurityException("invalid address type");
/*     */     }
/*     */ 
/* 592 */     ObjectInputStream.GetField localGetField = paramObjectInputStream.readFields();
/* 593 */     byte[] arrayOfByte = (byte[])localGetField.get("ipaddress", null);
/* 594 */     int i = localGetField.get("scope_id", -1);
/* 595 */     boolean bool1 = localGetField.get("scope_id_set", false);
/* 596 */     boolean bool2 = localGetField.get("scope_ifname_set", false);
/* 597 */     String str = (String)localGetField.get("ifname", null);
/*     */ 
/* 599 */     if ((str != null) && (!"".equals(str))) {
/*     */       try {
/* 601 */         localNetworkInterface = NetworkInterface.getByName(str);
/* 602 */         if (localNetworkInterface == null)
/*     */         {
/* 605 */           bool1 = false;
/* 606 */           bool2 = false;
/* 607 */           i = 0;
/*     */         } else {
/*     */           try {
/* 610 */             i = deriveNumericScope(arrayOfByte, localNetworkInterface);
/*     */           }
/*     */           catch (UnknownHostException localUnknownHostException)
/*     */           {
/*     */           }
/*     */         }
/*     */       }
/*     */       catch (SocketException localSocketException)
/*     */       {
/*     */       }
/*     */     }
/*     */ 
/* 622 */     arrayOfByte = (byte[])arrayOfByte.clone();
/*     */ 
/* 625 */     if (arrayOfByte.length != 16) {
/* 626 */       throw new InvalidObjectException("invalid address length: " + arrayOfByte.length);
/*     */     }
/*     */ 
/* 630 */     if (this.holder.getFamily() != 2) {
/* 631 */       throw new InvalidObjectException("invalid address family type");
/*     */     }
/*     */ 
/* 634 */     Inet6AddressHolder localInet6AddressHolder = new Inet6AddressHolder(arrayOfByte, i, bool1, localNetworkInterface, bool2, null);
/*     */ 
/* 638 */     UNSAFE.putObject(this, FIELDS_OFFSET, localInet6AddressHolder);
/*     */   }
/*     */ 
/*     */   private synchronized void writeObject(ObjectOutputStream paramObjectOutputStream)
/*     */     throws IOException
/*     */   {
/* 649 */     String str = null;
/*     */ 
/* 651 */     if (this.holder6.scope_ifname_set) {
/* 652 */       str = this.holder6.scope_ifname.getName();
/*     */     }
/* 654 */     ObjectOutputStream.PutField localPutField = paramObjectOutputStream.putFields();
/* 655 */     localPutField.put("ipaddress", this.holder6.ipaddress);
/* 656 */     localPutField.put("scope_id", this.holder6.scope_id);
/* 657 */     localPutField.put("scope_id_set", this.holder6.scope_id_set);
/* 658 */     localPutField.put("scope_ifname_set", this.holder6.scope_ifname_set);
/* 659 */     localPutField.put("ifname", str);
/* 660 */     paramObjectOutputStream.writeFields();
/*     */   }
/*     */ 
/*     */   public boolean isMulticastAddress()
/*     */   {
/* 674 */     return this.holder6.isMulticastAddress();
/*     */   }
/*     */ 
/*     */   public boolean isAnyLocalAddress()
/*     */   {
/* 685 */     return this.holder6.isAnyLocalAddress();
/*     */   }
/*     */ 
/*     */   public boolean isLoopbackAddress()
/*     */   {
/* 697 */     return this.holder6.isLoopbackAddress();
/*     */   }
/*     */ 
/*     */   public boolean isLinkLocalAddress()
/*     */   {
/* 709 */     return this.holder6.isLinkLocalAddress();
/*     */   }
/*     */ 
/*     */   static boolean isLinkLocalAddress(byte[] paramArrayOfByte)
/*     */   {
/* 714 */     return ((paramArrayOfByte[0] & 0xFF) == 254) && ((paramArrayOfByte[1] & 0xC0) == 128);
/*     */   }
/*     */ 
/*     */   public boolean isSiteLocalAddress()
/*     */   {
/* 727 */     return this.holder6.isSiteLocalAddress();
/*     */   }
/*     */ 
/*     */   static boolean isSiteLocalAddress(byte[] paramArrayOfByte)
/*     */   {
/* 732 */     return ((paramArrayOfByte[0] & 0xFF) == 254) && ((paramArrayOfByte[1] & 0xC0) == 192);
/*     */   }
/*     */ 
/*     */   public boolean isMCGlobal()
/*     */   {
/* 746 */     return this.holder6.isMCGlobal();
/*     */   }
/*     */ 
/*     */   public boolean isMCNodeLocal()
/*     */   {
/* 759 */     return this.holder6.isMCNodeLocal();
/*     */   }
/*     */ 
/*     */   public boolean isMCLinkLocal()
/*     */   {
/* 772 */     return this.holder6.isMCLinkLocal();
/*     */   }
/*     */ 
/*     */   public boolean isMCSiteLocal()
/*     */   {
/* 785 */     return this.holder6.isMCSiteLocal();
/*     */   }
/*     */ 
/*     */   public boolean isMCOrgLocal()
/*     */   {
/* 799 */     return this.holder6.isMCOrgLocal();
/*     */   }
/*     */ 
/*     */   public byte[] getAddress()
/*     */   {
/* 810 */     return (byte[])this.holder6.ipaddress.clone();
/*     */   }
/*     */ 
/*     */   public int getScopeId()
/*     */   {
/* 821 */     return this.holder6.scope_id;
/*     */   }
/*     */ 
/*     */   public NetworkInterface getScopedInterface()
/*     */   {
/* 832 */     return this.holder6.scope_ifname;
/*     */   }
/*     */ 
/*     */   public String getHostAddress()
/*     */   {
/* 845 */     return this.holder6.getHostAddress();
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 855 */     return this.holder6.hashCode();
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 876 */     if ((paramObject == null) || (!(paramObject instanceof Inet6Address)))
/*     */     {
/* 878 */       return false;
/*     */     }
/* 880 */     Inet6Address localInet6Address = (Inet6Address)paramObject;
/*     */ 
/* 882 */     return this.holder6.equals(localInet6Address.holder6);
/*     */   }
/*     */ 
/*     */   public boolean isIPv4CompatibleAddress()
/*     */   {
/* 894 */     return this.holder6.isIPv4CompatibleAddress();
/*     */   }
/*     */ 
/*     */   static String numericToTextFormat(byte[] paramArrayOfByte)
/*     */   {
/* 909 */     StringBuffer localStringBuffer = new StringBuffer(39);
/* 910 */     for (int i = 0; i < 8; i++) {
/* 911 */       localStringBuffer.append(Integer.toHexString(paramArrayOfByte[(i << 1)] << 8 & 0xFF00 | paramArrayOfByte[((i << 1) + 1)] & 0xFF));
/*     */ 
/* 913 */       if (i < 7) {
/* 914 */         localStringBuffer.append(":");
/*     */       }
/*     */     }
/* 917 */     return localStringBuffer.toString();
/*     */   }
/*     */ 
/*     */   private static native void init();
/*     */ 
/*     */   static
/*     */   {
/* 373 */     init();
/*     */ 
/* 557 */     serialPersistentFields = new ObjectStreamField[] { new ObjectStreamField("ipaddress", [B.class), new ObjectStreamField("scope_id", Integer.TYPE), new ObjectStreamField("scope_id_set", Boolean.TYPE), new ObjectStreamField("scope_ifname_set", Boolean.TYPE), new ObjectStreamField("ifname", String.class) };
/*     */     try
/*     */     {
/* 570 */       Unsafe localUnsafe = Unsafe.getUnsafe();
/* 571 */       FIELDS_OFFSET = localUnsafe.objectFieldOffset(Inet6Address.class.getDeclaredField("holder6"));
/*     */ 
/* 573 */       UNSAFE = localUnsafe;
/*     */     } catch (ReflectiveOperationException localReflectiveOperationException) {
/* 575 */       throw new Error(localReflectiveOperationException);
/*     */     }
/*     */   }
/*     */ 
/*     */   private class Inet6AddressHolder
/*     */   {
/*     */     byte[] ipaddress;
/* 202 */     int scope_id = 0;
/*     */ 
/* 208 */     boolean scope_id_set = false;
/*     */ 
/* 214 */     NetworkInterface scope_ifname = null;
/*     */ 
/* 220 */     boolean scope_ifname_set = false;
/*     */ 
/*     */     private Inet6AddressHolder()
/*     */     {
/* 179 */       this.ipaddress = new byte[16];
/*     */     }
/*     */ 
/*     */     private Inet6AddressHolder(byte[] paramInt, int paramBoolean1, boolean paramNetworkInterface, NetworkInterface paramBoolean2, boolean arg6)
/*     */     {
/* 186 */       this.ipaddress = paramInt;
/* 187 */       this.scope_id = paramBoolean1;
/* 188 */       this.scope_id_set = paramNetworkInterface;
/*     */       boolean bool;
/* 189 */       this.scope_ifname_set = bool;
/* 190 */       this.scope_ifname = paramBoolean2;
/*     */     }
/*     */ 
/*     */     void setAddr(byte[] paramArrayOfByte)
/*     */     {
/* 223 */       if (paramArrayOfByte.length == 16)
/* 224 */         System.arraycopy(paramArrayOfByte, 0, this.ipaddress, 0, 16);
/*     */     }
/*     */ 
/*     */     void init(byte[] paramArrayOfByte, int paramInt)
/*     */     {
/* 229 */       setAddr(paramArrayOfByte);
/*     */ 
/* 231 */       if (paramInt >= 0) {
/* 232 */         this.scope_id = paramInt;
/* 233 */         this.scope_id_set = true;
/*     */       }
/*     */     }
/*     */ 
/*     */     void init(byte[] paramArrayOfByte, NetworkInterface paramNetworkInterface)
/*     */       throws UnknownHostException
/*     */     {
/* 240 */       setAddr(paramArrayOfByte);
/*     */ 
/* 242 */       if (paramNetworkInterface != null) {
/* 243 */         this.scope_id = Inet6Address.deriveNumericScope(this.ipaddress, paramNetworkInterface);
/* 244 */         this.scope_id_set = true;
/* 245 */         this.scope_ifname = paramNetworkInterface;
/* 246 */         this.scope_ifname_set = true;
/*     */       }
/*     */     }
/*     */ 
/*     */     String getHostAddress() {
/* 251 */       String str = Inet6Address.numericToTextFormat(this.ipaddress);
/* 252 */       if (this.scope_ifname_set)
/* 253 */         str = str + "%" + this.scope_ifname.getName();
/* 254 */       else if (this.scope_id_set) {
/* 255 */         str = str + "%" + this.scope_id;
/*     */       }
/* 257 */       return str;
/*     */     }
/*     */ 
/*     */     public boolean equals(Object paramObject) {
/* 261 */       if (!(paramObject instanceof Inet6AddressHolder)) {
/* 262 */         return false;
/*     */       }
/* 264 */       Inet6AddressHolder localInet6AddressHolder = (Inet6AddressHolder)paramObject;
/*     */ 
/* 266 */       for (int i = 0; i < 16; i++) {
/* 267 */         if (this.ipaddress[i] != localInet6AddressHolder.ipaddress[i]) {
/* 268 */           return false;
/*     */         }
/*     */       }
/* 271 */       return true;
/*     */     }
/*     */ 
/*     */     public int hashCode() {
/* 275 */       if (this.ipaddress != null)
/*     */       {
/* 277 */         int i = 0;
/* 278 */         int j = 0;
/* 279 */         while (j < 16) {
/* 280 */           int k = 0;
/* 281 */           int m = 0;
/* 282 */           while ((k < 4) && (j < 16)) {
/* 283 */             m = (m << 8) + this.ipaddress[j];
/* 284 */             k++;
/* 285 */             j++;
/*     */           }
/* 287 */           i += m;
/*     */         }
/* 289 */         return i;
/*     */       }
/*     */ 
/* 292 */       return 0;
/*     */     }
/*     */ 
/*     */     boolean isIPv4CompatibleAddress()
/*     */     {
/* 297 */       if ((this.ipaddress[0] == 0) && (this.ipaddress[1] == 0) && (this.ipaddress[2] == 0) && (this.ipaddress[3] == 0) && (this.ipaddress[4] == 0) && (this.ipaddress[5] == 0) && (this.ipaddress[6] == 0) && (this.ipaddress[7] == 0) && (this.ipaddress[8] == 0) && (this.ipaddress[9] == 0) && (this.ipaddress[10] == 0) && (this.ipaddress[11] == 0))
/*     */       {
/* 303 */         return true;
/*     */       }
/* 305 */       return false;
/*     */     }
/*     */ 
/*     */     boolean isMulticastAddress() {
/* 309 */       return (this.ipaddress[0] & 0xFF) == 255;
/*     */     }
/*     */ 
/*     */     boolean isAnyLocalAddress() {
/* 313 */       int i = 0;
/* 314 */       for (int j = 0; j < 16; j++) {
/* 315 */         i = (byte)(i | this.ipaddress[j]);
/*     */       }
/* 317 */       return i == 0;
/*     */     }
/*     */ 
/*     */     boolean isLoopbackAddress() {
/* 321 */       int i = 0;
/* 322 */       for (int j = 0; j < 15; j++) {
/* 323 */         i = (byte)(i | this.ipaddress[j]);
/*     */       }
/* 325 */       return (i == 0) && (this.ipaddress[15] == 1);
/*     */     }
/*     */ 
/*     */     boolean isLinkLocalAddress() {
/* 329 */       return ((this.ipaddress[0] & 0xFF) == 254) && ((this.ipaddress[1] & 0xC0) == 128);
/*     */     }
/*     */ 
/*     */     boolean isSiteLocalAddress()
/*     */     {
/* 335 */       return ((this.ipaddress[0] & 0xFF) == 254) && ((this.ipaddress[1] & 0xC0) == 192);
/*     */     }
/*     */ 
/*     */     boolean isMCGlobal()
/*     */     {
/* 340 */       return ((this.ipaddress[0] & 0xFF) == 255) && ((this.ipaddress[1] & 0xF) == 14);
/*     */     }
/*     */ 
/*     */     boolean isMCNodeLocal()
/*     */     {
/* 345 */       return ((this.ipaddress[0] & 0xFF) == 255) && ((this.ipaddress[1] & 0xF) == 1);
/*     */     }
/*     */ 
/*     */     boolean isMCLinkLocal()
/*     */     {
/* 350 */       return ((this.ipaddress[0] & 0xFF) == 255) && ((this.ipaddress[1] & 0xF) == 2);
/*     */     }
/*     */ 
/*     */     boolean isMCSiteLocal()
/*     */     {
/* 355 */       return ((this.ipaddress[0] & 0xFF) == 255) && ((this.ipaddress[1] & 0xF) == 5);
/*     */     }
/*     */ 
/*     */     boolean isMCOrgLocal()
/*     */     {
/* 360 */       return ((this.ipaddress[0] & 0xFF) == 255) && ((this.ipaddress[1] & 0xF) == 8);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.net.Inet6Address
 * JD-Core Version:    0.6.2
 */