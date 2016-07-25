/*      */ package java.net;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.ObjectInputStream.GetField;
/*      */ import java.io.ObjectOutputStream;
/*      */ import java.io.ObjectOutputStream.PutField;
/*      */ import java.io.ObjectStreamException;
/*      */ import java.io.ObjectStreamField;
/*      */ import java.io.PrintStream;
/*      */ import java.io.Serializable;
/*      */ import java.security.AccessController;
/*      */ import java.security.PrivilegedActionException;
/*      */ import java.security.PrivilegedExceptionAction;
/*      */ import java.util.ArrayList;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedHashMap;
/*      */ import java.util.LinkedList;
/*      */ import java.util.List;
/*      */ import java.util.Set;
/*      */ import sun.misc.Service;
/*      */ import sun.misc.Unsafe;
/*      */ import sun.net.InetAddressCachePolicy;
/*      */ import sun.net.spi.nameservice.NameService;
/*      */ import sun.net.spi.nameservice.NameServiceDescriptor;
/*      */ import sun.net.util.IPAddressUtil;
/*      */ import sun.security.action.GetBooleanAction;
/*      */ import sun.security.action.GetPropertyAction;
/*      */ import sun.security.action.LoadLibraryAction;
/*      */ 
/*      */ public class InetAddress
/*      */   implements Serializable
/*      */ {
/*      */   static final int IPv4 = 1;
/*      */   static final int IPv6 = 2;
/*      */   static transient boolean preferIPv6Address;
/*      */   final transient InetAddressHolder holder;
/*      */   private static List<NameService> nameServices;
/*  259 */   private transient String canonicalHostName = null;
/*      */   private static final long serialVersionUID = 3286316764910316507L;
/*      */   private static Cache addressCache;
/*      */   private static Cache negativeCache;
/*      */   private static boolean addressCacheInit;
/*      */   static InetAddress[] unknown_array;
/*      */   static InetAddressImpl impl;
/*      */   private static final HashMap<String, Void> lookupTable;
/*      */   private static InetAddress cachedLocalHost;
/*      */   private static long cacheTime;
/*      */   private static final long maxCacheTime = 5000L;
/*      */   private static final Object cacheLock;
/*      */   private static final long FIELDS_OFFSET;
/*      */   private static final Unsafe UNSAFE;
/* 1590 */   private static final ObjectStreamField[] serialPersistentFields = { new ObjectStreamField("hostName", String.class), new ObjectStreamField("address", Integer.TYPE), new ObjectStreamField("family", Integer.TYPE) };
/*      */ 
/*      */   InetAddressHolder holder()
/*      */   {
/*  252 */     return this.holder;
/*      */   }
/*      */ 
/*      */   InetAddress()
/*      */   {
/*  281 */     this.holder = new InetAddressHolder();
/*      */   }
/*      */ 
/*      */   private Object readResolve()
/*      */     throws ObjectStreamException
/*      */   {
/*  294 */     return new Inet4Address(holder().getHostName(), holder().getAddress());
/*      */   }
/*      */ 
/*      */   public boolean isMulticastAddress()
/*      */   {
/*  305 */     return false;
/*      */   }
/*      */ 
/*      */   public boolean isAnyLocalAddress()
/*      */   {
/*  315 */     return false;
/*      */   }
/*      */ 
/*      */   public boolean isLoopbackAddress()
/*      */   {
/*  326 */     return false;
/*      */   }
/*      */ 
/*      */   public boolean isLinkLocalAddress()
/*      */   {
/*  337 */     return false;
/*      */   }
/*      */ 
/*      */   public boolean isSiteLocalAddress()
/*      */   {
/*  348 */     return false;
/*      */   }
/*      */ 
/*      */   public boolean isMCGlobal()
/*      */   {
/*  360 */     return false;
/*      */   }
/*      */ 
/*      */   public boolean isMCNodeLocal()
/*      */   {
/*  372 */     return false;
/*      */   }
/*      */ 
/*      */   public boolean isMCLinkLocal()
/*      */   {
/*  384 */     return false;
/*      */   }
/*      */ 
/*      */   public boolean isMCSiteLocal()
/*      */   {
/*  396 */     return false;
/*      */   }
/*      */ 
/*      */   public boolean isMCOrgLocal()
/*      */   {
/*  409 */     return false;
/*      */   }
/*      */ 
/*      */   public boolean isReachable(int paramInt)
/*      */     throws IOException
/*      */   {
/*  434 */     return isReachable(null, 0, paramInt);
/*      */   }
/*      */ 
/*      */   public boolean isReachable(NetworkInterface paramNetworkInterface, int paramInt1, int paramInt2)
/*      */     throws IOException
/*      */   {
/*  470 */     if (paramInt1 < 0)
/*  471 */       throw new IllegalArgumentException("ttl can't be negative");
/*  472 */     if (paramInt2 < 0) {
/*  473 */       throw new IllegalArgumentException("timeout can't be negative");
/*      */     }
/*  475 */     return impl.isReachable(this, paramInt2, paramNetworkInterface, paramInt1);
/*      */   }
/*      */ 
/*      */   public String getHostName()
/*      */   {
/*  504 */     return getHostName(true);
/*      */   }
/*      */ 
/*      */   String getHostName(boolean paramBoolean)
/*      */   {
/*  531 */     if (holder().getHostName() == null) {
/*  532 */       holder().hostName = getHostFromNameService(this, paramBoolean);
/*      */     }
/*  534 */     return holder().getHostName();
/*      */   }
/*      */ 
/*      */   public String getCanonicalHostName()
/*      */   {
/*  559 */     if (this.canonicalHostName == null) {
/*  560 */       this.canonicalHostName = getHostFromNameService(this, true);
/*      */     }
/*      */ 
/*  563 */     return this.canonicalHostName;
/*      */   }
/*      */ 
/*      */   private static String getHostFromNameService(InetAddress paramInetAddress, boolean paramBoolean)
/*      */   {
/*  586 */     String str = null;
/*  587 */     for (NameService localNameService : nameServices) {
/*      */       try
/*      */       {
/*  590 */         str = localNameService.getHostByAddr(paramInetAddress.getAddress());
/*      */ 
/*  595 */         if (paramBoolean) {
/*  596 */           localObject = System.getSecurityManager();
/*  597 */           if (localObject != null) {
/*  598 */             ((SecurityManager)localObject).checkConnect(str, -1);
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*  607 */         Object localObject = getAllByName0(str, paramBoolean);
/*  608 */         boolean bool = false;
/*      */ 
/*  610 */         if (localObject != null) {
/*  611 */           for (int i = 0; (!bool) && (i < localObject.length); i++) {
/*  612 */             bool = paramInetAddress.equals(localObject[i]);
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*  617 */         if (!bool) {
/*  618 */           return paramInetAddress.getHostAddress();
/*      */         }
/*      */ 
/*      */       }
/*      */       catch (SecurityException localSecurityException)
/*      */       {
/*  625 */         str = paramInetAddress.getHostAddress();
/*      */       }
/*      */       catch (UnknownHostException localUnknownHostException) {
/*  628 */         str = paramInetAddress.getHostAddress();
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  633 */     return str;
/*      */   }
/*      */ 
/*      */   public byte[] getAddress()
/*      */   {
/*  644 */     return null;
/*      */   }
/*      */ 
/*      */   public String getHostAddress()
/*      */   {
/*  654 */     return null;
/*      */   }
/*      */ 
/*      */   public int hashCode()
/*      */   {
/*  663 */     return -1;
/*      */   }
/*      */ 
/*      */   public boolean equals(Object paramObject)
/*      */   {
/*  683 */     return false;
/*      */   }
/*      */ 
/*      */   public String toString()
/*      */   {
/*  697 */     String str = holder().getHostName();
/*  698 */     return (str != null ? str : "") + "/" + getHostAddress();
/*      */   }
/*      */ 
/*      */   private static void cacheInitIfNeeded()
/*      */   {
/*  835 */     assert (Thread.holdsLock(addressCache));
/*  836 */     if (addressCacheInit) {
/*  837 */       return;
/*      */     }
/*  839 */     unknown_array = new InetAddress[1];
/*  840 */     unknown_array[0] = impl.anyLocalAddress();
/*      */ 
/*  842 */     addressCache.put(impl.anyLocalAddress().getHostName(), unknown_array);
/*      */ 
/*  845 */     addressCacheInit = true;
/*      */   }
/*      */ 
/*      */   private static void cacheAddresses(String paramString, InetAddress[] paramArrayOfInetAddress, boolean paramBoolean)
/*      */   {
/*  854 */     paramString = paramString.toLowerCase();
/*  855 */     synchronized (addressCache) {
/*  856 */       cacheInitIfNeeded();
/*  857 */       if (paramBoolean)
/*  858 */         addressCache.put(paramString, paramArrayOfInetAddress);
/*      */       else
/*  860 */         negativeCache.put(paramString, paramArrayOfInetAddress);
/*      */     }
/*      */   }
/*      */ 
/*      */   private static InetAddress[] getCachedAddresses(String paramString)
/*      */   {
/*  870 */     paramString = paramString.toLowerCase();
/*      */ 
/*  874 */     synchronized (addressCache) {
/*  875 */       cacheInitIfNeeded();
/*      */ 
/*  877 */       CacheEntry localCacheEntry = addressCache.get(paramString);
/*  878 */       if (localCacheEntry == null) {
/*  879 */         localCacheEntry = negativeCache.get(paramString);
/*      */       }
/*      */ 
/*  882 */       if (localCacheEntry != null) {
/*  883 */         return localCacheEntry.addresses;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  888 */     return null;
/*      */   }
/*      */ 
/*      */   private static NameService createNSProvider(String paramString) {
/*  892 */     if (paramString == null) {
/*  893 */       return null;
/*      */     }
/*  895 */     Object localObject = null;
/*  896 */     if (paramString.equals("default"))
/*      */     {
/*  898 */       localObject = new NameService()
/*      */       {
/*      */         public InetAddress[] lookupAllHostAddr(String paramAnonymousString) throws UnknownHostException {
/*  901 */           return InetAddress.impl.lookupAllHostAddr(paramAnonymousString);
/*      */         }
/*      */ 
/*      */         public String getHostByAddr(byte[] paramAnonymousArrayOfByte) throws UnknownHostException {
/*  905 */           return InetAddress.impl.getHostByAddr(paramAnonymousArrayOfByte);
/*      */         } } ;
/*      */     }
/*      */     else {
/*  909 */       String str = paramString;
/*      */       try {
/*  911 */         localObject = (NameService)AccessController.doPrivileged(new PrivilegedExceptionAction()
/*      */         {
/*      */           public NameService run() {
/*  914 */             Iterator localIterator = Service.providers(NameServiceDescriptor.class);
/*  915 */             while (localIterator.hasNext()) {
/*  916 */               NameServiceDescriptor localNameServiceDescriptor = (NameServiceDescriptor)localIterator.next();
/*      */ 
/*  918 */               if (this.val$providerName.equalsIgnoreCase(localNameServiceDescriptor.getType() + "," + localNameServiceDescriptor.getProviderName()))
/*      */               {
/*      */                 try
/*      */                 {
/*  922 */                   return localNameServiceDescriptor.createNameService();
/*      */                 } catch (Exception localException) {
/*  924 */                   localException.printStackTrace();
/*  925 */                   System.err.println("Cannot create name service:" + this.val$providerName + ": " + localException);
/*      */                 }
/*      */ 
/*      */               }
/*      */ 
/*      */             }
/*      */ 
/*  932 */             return null;
/*      */           }
/*      */         });
/*      */       }
/*      */       catch (PrivilegedActionException localPrivilegedActionException)
/*      */       {
/*      */       }
/*      */     }
/*  940 */     return localObject;
/*      */   }
/*      */ 
/*      */   public static InetAddress getByAddress(String paramString, byte[] paramArrayOfByte)
/*      */     throws UnknownHostException
/*      */   {
/*  996 */     if ((paramString != null) && (paramString.length() > 0) && (paramString.charAt(0) == '[') && 
/*  997 */       (paramString.charAt(paramString.length() - 1) == ']')) {
/*  998 */       paramString = paramString.substring(1, paramString.length() - 1);
/*      */     }
/*      */ 
/* 1001 */     if (paramArrayOfByte != null) {
/* 1002 */       if (paramArrayOfByte.length == 4)
/* 1003 */         return new Inet4Address(paramString, paramArrayOfByte);
/* 1004 */       if (paramArrayOfByte.length == 16) {
/* 1005 */         byte[] arrayOfByte = IPAddressUtil.convertFromIPv4MappedAddress(paramArrayOfByte);
/*      */ 
/* 1007 */         if (arrayOfByte != null) {
/* 1008 */           return new Inet4Address(paramString, arrayOfByte);
/*      */         }
/* 1010 */         return new Inet6Address(paramString, paramArrayOfByte);
/*      */       }
/*      */     }
/*      */ 
/* 1014 */     throw new UnknownHostException("addr is of illegal length");
/*      */   }
/*      */ 
/*      */   public static InetAddress getByName(String paramString)
/*      */     throws UnknownHostException
/*      */   {
/* 1048 */     return getAllByName(paramString)[0];
/*      */   }
/*      */ 
/*      */   private static InetAddress getByName(String paramString, InetAddress paramInetAddress)
/*      */     throws UnknownHostException
/*      */   {
/* 1054 */     return getAllByName(paramString, paramInetAddress)[0];
/*      */   }
/*      */ 
/*      */   public static InetAddress[] getAllByName(String paramString)
/*      */     throws UnknownHostException
/*      */   {
/* 1098 */     return getAllByName(paramString, null);
/*      */   }
/*      */ 
/*      */   private static InetAddress[] getAllByName(String paramString, InetAddress paramInetAddress)
/*      */     throws UnknownHostException
/*      */   {
/* 1104 */     if ((paramString == null) || (paramString.length() == 0)) {
/* 1105 */       InetAddress[] arrayOfInetAddress1 = new InetAddress[1];
/* 1106 */       arrayOfInetAddress1[0] = impl.loopbackAddress();
/* 1107 */       return arrayOfInetAddress1;
/*      */     }
/*      */ 
/* 1110 */     int i = 0;
/* 1111 */     if (paramString.charAt(0) == '[')
/*      */     {
/* 1113 */       if ((paramString.length() > 2) && (paramString.charAt(paramString.length() - 1) == ']')) {
/* 1114 */         paramString = paramString.substring(1, paramString.length() - 1);
/* 1115 */         i = 1;
/*      */       }
/*      */       else {
/* 1118 */         throw new UnknownHostException(paramString + ": invalid IPv6 address");
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1123 */     if ((Character.digit(paramString.charAt(0), 16) != -1) || (paramString.charAt(0) == ':'))
/*      */     {
/* 1125 */       byte[] arrayOfByte = null;
/* 1126 */       int j = -1;
/* 1127 */       String str = null;
/*      */ 
/* 1129 */       arrayOfByte = IPAddressUtil.textToNumericFormatV4(paramString);
/* 1130 */       if (arrayOfByte == null)
/*      */       {
/*      */         int k;
/* 1134 */         if ((k = paramString.indexOf("%")) != -1) {
/* 1135 */           j = checkNumericZone(paramString);
/* 1136 */           if (j == -1) {
/* 1137 */             str = paramString.substring(k + 1);
/*      */           }
/*      */         }
/* 1140 */         if (((arrayOfByte = IPAddressUtil.textToNumericFormatV6(paramString)) == null) && (paramString.contains(":")))
/* 1141 */           throw new UnknownHostException(paramString + ": invalid IPv6 address");
/*      */       }
/* 1143 */       else if (i != 0)
/*      */       {
/* 1145 */         throw new UnknownHostException("[" + paramString + "]");
/*      */       }
/* 1147 */       InetAddress[] arrayOfInetAddress2 = new InetAddress[1];
/* 1148 */       if (arrayOfByte != null) {
/* 1149 */         if (arrayOfByte.length == 4) {
/* 1150 */           arrayOfInetAddress2[0] = new Inet4Address(null, arrayOfByte);
/*      */         }
/* 1152 */         else if (str != null)
/* 1153 */           arrayOfInetAddress2[0] = new Inet6Address(null, arrayOfByte, str);
/*      */         else {
/* 1155 */           arrayOfInetAddress2[0] = new Inet6Address(null, arrayOfByte, j);
/*      */         }
/*      */ 
/* 1158 */         return arrayOfInetAddress2;
/*      */       }
/* 1160 */     } else if (i != 0)
/*      */     {
/* 1162 */       throw new UnknownHostException("[" + paramString + "]");
/*      */     }
/* 1164 */     return getAllByName0(paramString, paramInetAddress, true);
/*      */   }
/*      */ 
/*      */   public static InetAddress getLoopbackAddress()
/*      */   {
/* 1179 */     return impl.loopbackAddress();
/*      */   }
/*      */ 
/*      */   private static int checkNumericZone(String paramString)
/*      */     throws UnknownHostException
/*      */   {
/* 1191 */     int i = paramString.indexOf('%');
/* 1192 */     int j = paramString.length();
/* 1193 */     int m = 0;
/* 1194 */     if (i == -1) {
/* 1195 */       return -1;
/*      */     }
/* 1197 */     for (int n = i + 1; n < j; n++) {
/* 1198 */       char c = paramString.charAt(n);
/* 1199 */       if (c == ']') {
/* 1200 */         if (n != i + 1)
/*      */           break;
/* 1202 */         return -1;
/*      */       }
/*      */       int k;
/* 1206 */       if ((k = Character.digit(c, 10)) < 0) {
/* 1207 */         return -1;
/*      */       }
/* 1209 */       m = m * 10 + k;
/*      */     }
/* 1211 */     return m;
/*      */   }
/*      */ 
/*      */   private static InetAddress[] getAllByName0(String paramString)
/*      */     throws UnknownHostException
/*      */   {
/* 1217 */     return getAllByName0(paramString, true);
/*      */   }
/*      */ 
/*      */   static InetAddress[] getAllByName0(String paramString, boolean paramBoolean)
/*      */     throws UnknownHostException
/*      */   {
/* 1225 */     return getAllByName0(paramString, null, paramBoolean);
/*      */   }
/*      */ 
/*      */   private static InetAddress[] getAllByName0(String paramString, InetAddress paramInetAddress, boolean paramBoolean)
/*      */     throws UnknownHostException
/*      */   {
/* 1237 */     if (paramBoolean) {
/* 1238 */       localObject = System.getSecurityManager();
/* 1239 */       if (localObject != null) {
/* 1240 */         ((SecurityManager)localObject).checkConnect(paramString, -1);
/*      */       }
/*      */     }
/*      */ 
/* 1244 */     Object localObject = getCachedAddresses(paramString);
/*      */ 
/* 1247 */     if (localObject == null) {
/* 1248 */       localObject = getAddressesFromNameService(paramString, paramInetAddress);
/*      */     }
/*      */ 
/* 1251 */     if (localObject == unknown_array) {
/* 1252 */       throw new UnknownHostException(paramString);
/*      */     }
/* 1254 */     return (InetAddress[])((InetAddress[])localObject).clone();
/*      */   }
/*      */ 
/*      */   private static InetAddress[] getAddressesFromNameService(String paramString, InetAddress paramInetAddress)
/*      */     throws UnknownHostException
/*      */   {
/* 1260 */     Object localObject1 = null;
/* 1261 */     boolean bool = false;
/* 1262 */     Object localObject2 = null;
/*      */ 
/* 1282 */     if ((localObject1 = checkLookupTable(paramString)) == null)
/*      */     {
/*      */       try
/*      */       {
/* 1287 */         for (Iterator localIterator = nameServices.iterator(); localIterator.hasNext(); ) { localNameService = (NameService)localIterator.next();
/*      */           try
/*      */           {
/* 1295 */             localObject1 = localNameService.lookupAllHostAddr(paramString);
/* 1296 */             bool = true;
/*      */           }
/*      */           catch (UnknownHostException localUnknownHostException) {
/* 1299 */             if (paramString.equalsIgnoreCase("localhost")) {
/* 1300 */               InetAddress[] arrayOfInetAddress = { impl.loopbackAddress() };
/* 1301 */               localObject1 = arrayOfInetAddress;
/* 1302 */               bool = true;
/* 1303 */               break;
/*      */             }
/*      */ 
/* 1306 */             localObject1 = unknown_array;
/* 1307 */             bool = false;
/* 1308 */             localObject2 = localUnknownHostException;
/*      */           }
/*      */         }
/*      */         NameService localNameService;
/* 1314 */         if ((paramInetAddress != null) && (localObject1.length > 1) && (!localObject1[0].equals(paramInetAddress)))
/*      */         {
/* 1316 */           int i = 1;
/* 1317 */           while ((i < localObject1.length) && 
/* 1318 */             (!localObject1[i].equals(paramInetAddress))) {
/* 1317 */             i++;
/*      */           }
/*      */ 
/* 1323 */           if (i < localObject1.length) {
/* 1324 */             Object localObject3 = paramInetAddress;
/* 1325 */             for (int j = 0; j < i; j++) {
/* 1326 */               localNameService = localObject1[j];
/* 1327 */               localObject1[j] = localObject3;
/* 1328 */               localObject3 = localNameService;
/*      */             }
/* 1330 */             localObject1[i] = localObject3;
/*      */           }
/*      */         }
/*      */ 
/* 1334 */         cacheAddresses(paramString, (InetAddress[])localObject1, bool);
/*      */ 
/* 1336 */         if ((!bool) && (localObject2 != null)) {
/* 1337 */           throw localObject2;
/*      */         }
/*      */       }
/*      */       finally
/*      */       {
/* 1342 */         updateLookupTable(paramString);
/*      */       }
/*      */     }
/*      */ 
/* 1346 */     return localObject1;
/*      */   }
/*      */ 
/*      */   private static InetAddress[] checkLookupTable(String paramString)
/*      */   {
/* 1351 */     synchronized (lookupTable)
/*      */     {
/* 1355 */       if (!lookupTable.containsKey(paramString)) {
/* 1356 */         lookupTable.put(paramString, null);
/* 1357 */         return null;
/*      */       }
/*      */ 
/* 1363 */       while (lookupTable.containsKey(paramString)) {
/*      */         try {
/* 1365 */           lookupTable.wait();
/*      */         }
/*      */         catch (InterruptedException localInterruptedException)
/*      */         {
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1375 */     ??? = getCachedAddresses(paramString);
/* 1376 */     if (??? == null) {
/* 1377 */       synchronized (lookupTable) {
/* 1378 */         lookupTable.put(paramString, null);
/* 1379 */         return null;
/*      */       }
/*      */     }
/*      */ 
/* 1383 */     return ???;
/*      */   }
/*      */ 
/*      */   private static void updateLookupTable(String paramString) {
/* 1387 */     synchronized (lookupTable) {
/* 1388 */       lookupTable.remove(paramString);
/* 1389 */       lookupTable.notifyAll();
/*      */     }
/*      */   }
/*      */ 
/*      */   public static InetAddress getByAddress(byte[] paramArrayOfByte)
/*      */     throws UnknownHostException
/*      */   {
/* 1411 */     return getByAddress(null, paramArrayOfByte);
/*      */   }
/*      */ 
/*      */   public static InetAddress getLocalHost()
/*      */     throws UnknownHostException
/*      */   {
/* 1444 */     SecurityManager localSecurityManager = System.getSecurityManager();
/*      */     try {
/* 1446 */       String str = impl.getLocalHostName();
/*      */ 
/* 1448 */       if (localSecurityManager != null) {
/* 1449 */         localSecurityManager.checkConnect(str, -1);
/*      */       }
/*      */ 
/* 1452 */       if (str.equals("localhost")) {
/* 1453 */         return impl.loopbackAddress();
/*      */       }
/*      */ 
/* 1456 */       InetAddress localInetAddress = null;
/* 1457 */       synchronized (cacheLock) {
/* 1458 */         long l = System.currentTimeMillis();
/* 1459 */         if (cachedLocalHost != null) {
/* 1460 */           if (l - cacheTime < 5000L)
/* 1461 */             localInetAddress = cachedLocalHost;
/*      */           else {
/* 1463 */             cachedLocalHost = null;
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/* 1468 */         if (localInetAddress == null) {
/*      */           InetAddress[] arrayOfInetAddress;
/*      */           try {
/* 1471 */             arrayOfInetAddress = getAddressesFromNameService(str, null);
/*      */           }
/*      */           catch (UnknownHostException localUnknownHostException1)
/*      */           {
/* 1475 */             UnknownHostException localUnknownHostException2 = new UnknownHostException(str + ": " + localUnknownHostException1.getMessage());
/*      */ 
/* 1478 */             localUnknownHostException2.initCause(localUnknownHostException1);
/* 1479 */             throw localUnknownHostException2;
/*      */           }
/* 1481 */           cachedLocalHost = arrayOfInetAddress[0];
/* 1482 */           cacheTime = l;
/* 1483 */           localInetAddress = arrayOfInetAddress[0];
/*      */         }
/*      */       }
/* 1486 */       return localInetAddress; } catch (SecurityException localSecurityException) {
/*      */     }
/* 1488 */     return impl.loopbackAddress();
/*      */   }
/*      */ 
/*      */   private static native void init();
/*      */ 
/*      */   static InetAddress anyLocalAddress()
/*      */   {
/* 1503 */     return impl.anyLocalAddress();
/*      */   }
/*      */ 
/*      */   static InetAddressImpl loadImpl(String paramString)
/*      */   {
/* 1510 */     Object localObject = null;
/*      */ 
/* 1519 */     String str = (String)AccessController.doPrivileged(new GetPropertyAction("impl.prefix", ""));
/*      */     try
/*      */     {
/* 1522 */       localObject = Class.forName("java.net." + str + paramString).newInstance();
/*      */     } catch (ClassNotFoundException localClassNotFoundException) {
/* 1524 */       System.err.println("Class not found: java.net." + str + paramString + ":\ncheck impl.prefix property " + "in your properties file.");
/*      */     }
/*      */     catch (InstantiationException localInstantiationException)
/*      */     {
/* 1528 */       System.err.println("Could not instantiate: java.net." + str + paramString + ":\ncheck impl.prefix property " + "in your properties file.");
/*      */     }
/*      */     catch (IllegalAccessException localIllegalAccessException)
/*      */     {
/* 1532 */       System.err.println("Cannot access class: java.net." + str + paramString + ":\ncheck impl.prefix property " + "in your properties file.");
/*      */     }
/*      */ 
/* 1537 */     if (localObject == null) {
/*      */       try {
/* 1539 */         localObject = Class.forName(paramString).newInstance();
/*      */       } catch (Exception localException) {
/* 1541 */         throw new Error("System property impl.prefix incorrect");
/*      */       }
/*      */     }
/*      */ 
/* 1545 */     return (InetAddressImpl)localObject;
/*      */   }
/*      */ 
/*      */   private void readObjectNoData(ObjectInputStream paramObjectInputStream) throws IOException, ClassNotFoundException
/*      */   {
/* 1550 */     if (getClass().getClassLoader() != null)
/* 1551 */       throw new SecurityException("invalid address type");
/*      */   }
/*      */ 
/*      */   private void readObject(ObjectInputStream paramObjectInputStream)
/*      */     throws IOException, ClassNotFoundException
/*      */   {
/* 1572 */     if (getClass().getClassLoader() != null) {
/* 1573 */       throw new SecurityException("invalid address type");
/*      */     }
/* 1575 */     ObjectInputStream.GetField localGetField = paramObjectInputStream.readFields();
/* 1576 */     String str = (String)localGetField.get("hostName", null);
/* 1577 */     int i = localGetField.get("address", 0);
/* 1578 */     int j = localGetField.get("family", 0);
/* 1579 */     InetAddressHolder localInetAddressHolder = new InetAddressHolder(str, i, j);
/* 1580 */     UNSAFE.putObject(this, FIELDS_OFFSET, localInetAddressHolder);
/*      */   }
/*      */ 
/*      */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*      */     throws IOException
/*      */   {
/* 1598 */     if (getClass().getClassLoader() != null) {
/* 1599 */       throw new SecurityException("invalid address type");
/*      */     }
/* 1601 */     ObjectOutputStream.PutField localPutField = paramObjectOutputStream.putFields();
/* 1602 */     localPutField.put("hostName", holder().hostName);
/* 1603 */     localPutField.put("address", holder().address);
/* 1604 */     localPutField.put("family", holder().family);
/* 1605 */     paramObjectOutputStream.writeFields();
/*      */   }
/*      */ 
/*      */   static
/*      */   {
/*  204 */     preferIPv6Address = false;
/*      */ 
/*  256 */     nameServices = null;
/*      */ 
/*  268 */     preferIPv6Address = ((Boolean)AccessController.doPrivileged(new GetBooleanAction("java.net.preferIPv6Addresses"))).booleanValue();
/*      */ 
/*  270 */     AccessController.doPrivileged(new LoadLibraryAction("net"));
/*  271 */     init();
/*      */ 
/*  705 */     addressCache = new Cache(InetAddress.Cache.Type.Positive);
/*      */ 
/*  707 */     negativeCache = new Cache(InetAddress.Cache.Type.Negative);
/*      */ 
/*  709 */     addressCacheInit = false;
/*      */ 
/*  715 */     lookupTable = new HashMap();
/*      */ 
/*  945 */     impl = InetAddressImplFactory.create();
/*      */ 
/*  948 */     Object localObject = null;
/*  949 */     String str = "sun.net.spi.nameservice.provider.";
/*  950 */     int i = 1;
/*  951 */     nameServices = new ArrayList();
/*  952 */     localObject = (String)AccessController.doPrivileged(new GetPropertyAction(str + i));
/*      */     NameService localNameService;
/*  954 */     while (localObject != null) {
/*  955 */       localNameService = createNSProvider((String)localObject);
/*  956 */       if (localNameService != null) {
/*  957 */         nameServices.add(localNameService);
/*      */       }
/*  959 */       i++;
/*  960 */       localObject = (String)AccessController.doPrivileged(new GetPropertyAction(str + i));
/*      */     }
/*      */ 
/*  966 */     if (nameServices.size() == 0) {
/*  967 */       localNameService = createNSProvider("default");
/*  968 */       nameServices.add(localNameService);
/*      */     }
/*      */ 
/* 1414 */     cachedLocalHost = null;
/* 1415 */     cacheTime = 0L;
/*      */ 
/* 1417 */     cacheLock = new Object();
/*      */     try
/*      */     {
/* 1560 */       localObject = Unsafe.getUnsafe();
/* 1561 */       FIELDS_OFFSET = ((Unsafe)localObject).objectFieldOffset(InetAddress.class.getDeclaredField("holder"));
/*      */ 
/* 1564 */       UNSAFE = (Unsafe)localObject;
/*      */     } catch (ReflectiveOperationException localReflectiveOperationException) {
/* 1566 */       throw new Error(localReflectiveOperationException);
/*      */     }
/*      */   }
/*      */ 
/*      */   static final class Cache
/*      */   {
/*      */     private LinkedHashMap<String, InetAddress.CacheEntry> cache;
/*      */     private Type type;
/*      */ 
/*      */     public Cache(Type paramType)
/*      */     {
/*  745 */       this.type = paramType;
/*  746 */       this.cache = new LinkedHashMap();
/*      */     }
/*      */ 
/*      */     private int getPolicy() {
/*  750 */       if (this.type == Type.Positive) {
/*  751 */         return InetAddressCachePolicy.get();
/*      */       }
/*  753 */       return InetAddressCachePolicy.getNegative();
/*      */     }
/*      */ 
/*      */     public Cache put(String paramString, InetAddress[] paramArrayOfInetAddress)
/*      */     {
/*  763 */       int i = getPolicy();
/*  764 */       if (i == 0)
/*  765 */         return this;
/*      */       Iterator localIterator;
/*      */       String str;
/*  770 */       if (i != -1)
/*      */       {
/*  774 */         LinkedList localLinkedList = new LinkedList();
/*  775 */         long l2 = System.currentTimeMillis();
/*  776 */         for (localIterator = this.cache.keySet().iterator(); localIterator.hasNext(); ) { str = (String)localIterator.next();
/*  777 */           InetAddress.CacheEntry localCacheEntry2 = (InetAddress.CacheEntry)this.cache.get(str);
/*      */ 
/*  779 */           if ((localCacheEntry2.expiration < 0L) || (localCacheEntry2.expiration >= l2)) break;
/*  780 */           localLinkedList.add(str);
/*      */         }
/*      */ 
/*  786 */         for (localIterator = localLinkedList.iterator(); localIterator.hasNext(); ) { str = (String)localIterator.next();
/*  787 */           this.cache.remove(str);
/*      */         }
/*      */       }
/*      */       long l1;
/*  796 */       if (i == -1)
/*  797 */         l1 = -1L;
/*      */       else {
/*  799 */         l1 = System.currentTimeMillis() + i * 1000;
/*      */       }
/*  801 */       InetAddress.CacheEntry localCacheEntry1 = new InetAddress.CacheEntry(paramArrayOfInetAddress, l1);
/*  802 */       this.cache.put(paramString, localCacheEntry1);
/*  803 */       return this;
/*      */     }
/*      */ 
/*      */     public InetAddress.CacheEntry get(String paramString)
/*      */     {
/*  811 */       int i = getPolicy();
/*  812 */       if (i == 0) {
/*  813 */         return null;
/*      */       }
/*  815 */       InetAddress.CacheEntry localCacheEntry = (InetAddress.CacheEntry)this.cache.get(paramString);
/*      */ 
/*  818 */       if ((localCacheEntry != null) && (i != -1) && 
/*  819 */         (localCacheEntry.expiration >= 0L) && (localCacheEntry.expiration < System.currentTimeMillis()))
/*      */       {
/*  821 */         this.cache.remove(paramString);
/*  822 */         localCacheEntry = null;
/*      */       }
/*      */ 
/*  826 */       return localCacheEntry;
/*      */     }
/*      */ 
/*      */     static enum Type
/*      */     {
/*  739 */       Positive, Negative;
/*      */     }
/*      */   }
/*      */ 
/*      */   static final class CacheEntry
/*      */   {
/*      */     InetAddress[] addresses;
/*      */     long expiration;
/*      */ 
/*      */     CacheEntry(InetAddress[] paramArrayOfInetAddress, long paramLong)
/*      */     {
/*  723 */       this.addresses = paramArrayOfInetAddress;
/*  724 */       this.expiration = paramLong;
/*      */     }
/*      */   }
/*      */ 
/*      */   static class InetAddressHolder
/*      */   {
/*      */     String hostName;
/*      */     int address;
/*      */     int family;
/*      */ 
/*      */     InetAddressHolder()
/*      */     {
/*      */     }
/*      */ 
/*      */     InetAddressHolder(String paramString, int paramInt1, int paramInt2)
/*      */     {
/*  211 */       this.hostName = paramString;
/*  212 */       this.address = paramInt1;
/*  213 */       this.family = paramInt2;
/*      */     }
/*      */ 
/*      */     void init(String paramString, int paramInt) {
/*  217 */       this.hostName = paramString;
/*  218 */       if (paramInt != -1)
/*  219 */         this.family = paramInt;
/*      */     }
/*      */ 
/*      */     String getHostName()
/*      */     {
/*  226 */       return this.hostName;
/*      */     }
/*      */ 
/*      */     int getAddress()
/*      */     {
/*  235 */       return this.address;
/*      */     }
/*      */ 
/*      */     int getFamily()
/*      */     {
/*  245 */       return this.family;
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.net.InetAddress
 * JD-Core Version:    0.6.2
 */