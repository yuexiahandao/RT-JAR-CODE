/*     */ package java.net;
/*     */ 
/*     */ import java.security.AccessController;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Enumeration;
/*     */ import java.util.List;
/*     */ import java.util.NoSuchElementException;
/*     */ import sun.security.action.LoadLibraryAction;
/*     */ 
/*     */ public final class NetworkInterface
/*     */ {
/*     */   private String name;
/*     */   private String displayName;
/*     */   private int index;
/*     */   private InetAddress[] addrs;
/*     */   private InterfaceAddress[] bindings;
/*     */   private NetworkInterface[] childs;
/*  50 */   private NetworkInterface parent = null;
/*  51 */   private boolean virtual = false;
/*     */   private static final NetworkInterface defaultInterface;
/*     */   private static final int defaultIndex;
/*     */ 
/*     */   NetworkInterface()
/*     */   {
/*     */   }
/*     */ 
/*     */   NetworkInterface(String paramString, int paramInt, InetAddress[] paramArrayOfInetAddress)
/*     */   {
/*  76 */     this.name = paramString;
/*  77 */     this.index = paramInt;
/*  78 */     this.addrs = paramArrayOfInetAddress;
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/*  87 */     return this.name;
/*     */   }
/*     */ 
/*     */   public Enumeration<InetAddress> getInetAddresses()
/*     */   {
/* 145 */     return new Enumeration()
/*     */     {
/* 107 */       private int i = 0; private int count = 0;
/*     */       private InetAddress[] local_addrs;
/*     */ 
/*     */       public InetAddress nextElement()
/*     */       {
/* 134 */         if (this.i < this.count) {
/* 135 */           return this.local_addrs[(this.i++)];
/*     */         }
/* 137 */         throw new NoSuchElementException();
/*     */       }
/*     */ 
/*     */       public boolean hasMoreElements()
/*     */       {
/* 142 */         return this.i < this.count;
/*     */       }
/*     */     };
/*     */   }
/*     */ 
/*     */   public List<InterfaceAddress> getInterfaceAddresses()
/*     */   {
/* 163 */     ArrayList localArrayList = new ArrayList(1);
/* 164 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 165 */     for (int i = 0; i < this.bindings.length; i++)
/*     */       try {
/* 167 */         if (localSecurityManager != null) {
/* 168 */           localSecurityManager.checkConnect(this.bindings[i].getAddress().getHostAddress(), -1);
/*     */         }
/* 170 */         localArrayList.add(this.bindings[i]);
/*     */       } catch (SecurityException localSecurityException) {
/*     */       }
/* 173 */     return localArrayList;
/*     */   }
/*     */ 
/*     */   public Enumeration<NetworkInterface> getSubInterfaces()
/*     */   {
/* 206 */     return new Enumeration()
/*     */     {
/* 189 */       private int i = 0;
/*     */ 
/*     */       public NetworkInterface nextElement()
/*     */       {
/* 195 */         if (this.i < NetworkInterface.this.childs.length) {
/* 196 */           return NetworkInterface.this.childs[(this.i++)];
/*     */         }
/* 198 */         throw new NoSuchElementException();
/*     */       }
/*     */ 
/*     */       public boolean hasMoreElements()
/*     */       {
/* 203 */         return this.i < NetworkInterface.this.childs.length;
/*     */       }
/*     */     };
/*     */   }
/*     */ 
/*     */   public NetworkInterface getParent()
/*     */   {
/* 219 */     return this.parent;
/*     */   }
/*     */ 
/*     */   public int getIndex()
/*     */   {
/* 234 */     return this.index;
/*     */   }
/*     */ 
/*     */   public String getDisplayName()
/*     */   {
/* 247 */     return "".equals(this.displayName) ? null : this.displayName;
/*     */   }
/*     */ 
/*     */   public static NetworkInterface getByName(String paramString)
/*     */     throws SocketException
/*     */   {
/* 267 */     if (paramString == null)
/* 268 */       throw new NullPointerException();
/* 269 */     return getByName0(paramString);
/*     */   }
/*     */ 
/*     */   public static NetworkInterface getByIndex(int paramInt)
/*     */     throws SocketException
/*     */   {
/* 284 */     if (paramInt < 0)
/* 285 */       throw new IllegalArgumentException("Interface index can't be negative");
/* 286 */     return getByIndex0(paramInt);
/*     */   }
/*     */ 
/*     */   public static NetworkInterface getByInetAddress(InetAddress paramInetAddress)
/*     */     throws SocketException
/*     */   {
/* 312 */     if (paramInetAddress == null) {
/* 313 */       throw new NullPointerException();
/*     */     }
/* 315 */     if ((!(paramInetAddress instanceof Inet4Address)) && (!(paramInetAddress instanceof Inet6Address))) {
/* 316 */       throw new IllegalArgumentException("invalid address type");
/*     */     }
/* 318 */     return getByInetAddress0(paramInetAddress);
/*     */   }
/*     */ 
/*     */   public static Enumeration<NetworkInterface> getNetworkInterfaces()
/*     */     throws SocketException
/*     */   {
/* 334 */     NetworkInterface[] arrayOfNetworkInterface = getAll();
/*     */ 
/* 337 */     if (arrayOfNetworkInterface == null) {
/* 338 */       return null;
/*     */     }
/* 340 */     return new Enumeration() {
/* 341 */       private int i = 0;
/*     */ 
/* 343 */       public NetworkInterface nextElement() { if ((this.val$netifs != null) && (this.i < this.val$netifs.length)) {
/* 344 */           NetworkInterface localNetworkInterface = this.val$netifs[(this.i++)];
/* 345 */           return localNetworkInterface;
/*     */         }
/* 347 */         throw new NoSuchElementException();
/*     */       }
/*     */ 
/*     */       public boolean hasMoreElements()
/*     */       {
/* 352 */         return (this.val$netifs != null) && (this.i < this.val$netifs.length);
/*     */       }
/*     */     };
/*     */   }
/*     */ 
/*     */   private static native NetworkInterface[] getAll()
/*     */     throws SocketException;
/*     */ 
/*     */   private static native NetworkInterface getByName0(String paramString)
/*     */     throws SocketException;
/*     */ 
/*     */   private static native NetworkInterface getByIndex0(int paramInt)
/*     */     throws SocketException;
/*     */ 
/*     */   private static native NetworkInterface getByInetAddress0(InetAddress paramInetAddress)
/*     */     throws SocketException;
/*     */ 
/*     */   public boolean isUp()
/*     */     throws SocketException
/*     */   {
/* 378 */     return isUp0(this.name, this.index);
/*     */   }
/*     */ 
/*     */   public boolean isLoopback()
/*     */     throws SocketException
/*     */   {
/* 390 */     return isLoopback0(this.name, this.index);
/*     */   }
/*     */ 
/*     */   public boolean isPointToPoint()
/*     */     throws SocketException
/*     */   {
/* 405 */     return isP2P0(this.name, this.index);
/*     */   }
/*     */ 
/*     */   public boolean supportsMulticast()
/*     */     throws SocketException
/*     */   {
/* 417 */     return supportsMulticast0(this.name, this.index);
/*     */   }
/*     */ 
/*     */   public byte[] getHardwareAddress()
/*     */     throws SocketException
/*     */   {
/* 435 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 436 */     if (localSecurityManager != null) {
/*     */       try {
/* 438 */         localSecurityManager.checkPermission(new NetPermission("getNetworkInformation"));
/*     */       } catch (SecurityException localSecurityException) {
/* 440 */         if (!getInetAddresses().hasMoreElements())
/*     */         {
/* 442 */           return null;
/*     */         }
/*     */       }
/*     */     }
/* 446 */     for (InetAddress localInetAddress : this.addrs) {
/* 447 */       if ((localInetAddress instanceof Inet4Address)) {
/* 448 */         return getMacAddr0(((Inet4Address)localInetAddress).getAddress(), this.name, this.index);
/*     */       }
/*     */     }
/* 451 */     return getMacAddr0(null, this.name, this.index);
/*     */   }
/*     */ 
/*     */   public int getMTU()
/*     */     throws SocketException
/*     */   {
/* 462 */     return getMTU0(this.name, this.index);
/*     */   }
/*     */ 
/*     */   public boolean isVirtual()
/*     */   {
/* 479 */     return this.virtual;
/*     */   }
/*     */ 
/*     */   private static native boolean isUp0(String paramString, int paramInt)
/*     */     throws SocketException;
/*     */ 
/*     */   private static native boolean isLoopback0(String paramString, int paramInt)
/*     */     throws SocketException;
/*     */ 
/*     */   private static native boolean supportsMulticast0(String paramString, int paramInt)
/*     */     throws SocketException;
/*     */ 
/*     */   private static native boolean isP2P0(String paramString, int paramInt)
/*     */     throws SocketException;
/*     */ 
/*     */   private static native byte[] getMacAddr0(byte[] paramArrayOfByte, String paramString, int paramInt)
/*     */     throws SocketException;
/*     */ 
/*     */   private static native int getMTU0(String paramString, int paramInt)
/*     */     throws SocketException;
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 504 */     if (!(paramObject instanceof NetworkInterface)) {
/* 505 */       return false;
/*     */     }
/* 507 */     NetworkInterface localNetworkInterface = (NetworkInterface)paramObject;
/* 508 */     if (this.name != null) {
/* 509 */       if (!this.name.equals(localNetworkInterface.name)) {
/* 510 */         return false;
/*     */       }
/*     */     }
/* 513 */     else if (localNetworkInterface.name != null) {
/* 514 */       return false;
/*     */     }
/*     */ 
/* 518 */     if (this.addrs == null)
/* 519 */       return localNetworkInterface.addrs == null;
/* 520 */     if (localNetworkInterface.addrs == null) {
/* 521 */       return false;
/*     */     }
/*     */ 
/* 526 */     if (this.addrs.length != localNetworkInterface.addrs.length) {
/* 527 */       return false;
/*     */     }
/*     */ 
/* 530 */     InetAddress[] arrayOfInetAddress = localNetworkInterface.addrs;
/* 531 */     int i = arrayOfInetAddress.length;
/*     */ 
/* 533 */     for (int j = 0; j < i; j++) {
/* 534 */       int k = 0;
/* 535 */       for (int m = 0; m < i; m++) {
/* 536 */         if (this.addrs[j].equals(arrayOfInetAddress[m])) {
/* 537 */           k = 1;
/* 538 */           break;
/*     */         }
/*     */       }
/* 541 */       if (k == 0) {
/* 542 */         return false;
/*     */       }
/*     */     }
/* 545 */     return true;
/*     */   }
/*     */ 
/*     */   public int hashCode() {
/* 549 */     return this.name == null ? 0 : this.name.hashCode();
/*     */   }
/*     */ 
/*     */   public String toString() {
/* 553 */     String str = "name:";
/* 554 */     str = str + (this.name == null ? "null" : this.name);
/* 555 */     if (this.displayName != null) {
/* 556 */       str = str + " (" + this.displayName + ")";
/*     */     }
/* 558 */     return str;
/*     */   }
/*     */ 
/*     */   private static native void init();
/*     */ 
/*     */   static NetworkInterface getDefault()
/*     */   {
/* 569 */     return defaultInterface;
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  56 */     AccessController.doPrivileged(new LoadLibraryAction("net"));
/*  57 */     init();
/*  58 */     defaultInterface = DefaultInterface.getDefault();
/*  59 */     if (defaultInterface != null)
/*  60 */       defaultIndex = defaultInterface.getIndex();
/*     */     else
/*  62 */       defaultIndex = 0;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.net.NetworkInterface
 * JD-Core Version:    0.6.2
 */