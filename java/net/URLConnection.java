/*      */ package java.net;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.OutputStream;
/*      */ import java.security.AccessController;
/*      */ import java.security.Permission;
/*      */ import java.util.Collections;
/*      */ import java.util.Date;
/*      */ import java.util.Hashtable;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.StringTokenizer;
/*      */ import sun.net.www.MessageHeader;
/*      */ import sun.net.www.MimeTable;
/*      */ import sun.security.action.GetPropertyAction;
/*      */ import sun.security.util.SecurityConstants;
/*      */ 
/*      */ public abstract class URLConnection
/*      */ {
/*      */   protected URL url;
/*  199 */   protected boolean doInput = true;
/*      */ 
/*  214 */   protected boolean doOutput = false;
/*      */ 
/*  216 */   private static boolean defaultAllowUserInteraction = false;
/*      */ 
/*  235 */   protected boolean allowUserInteraction = defaultAllowUserInteraction;
/*      */ 
/*  237 */   private static boolean defaultUseCaches = true;
/*      */ 
/*  254 */   protected boolean useCaches = defaultUseCaches;
/*      */ 
/*  274 */   protected long ifModifiedSince = 0L;
/*      */ 
/*  281 */   protected boolean connected = false;
/*      */   private int connectTimeout;
/*      */   private int readTimeout;
/*      */   private MessageHeader requests;
/*      */   private static FileNameMap fileNameMap;
/*  302 */   private static boolean fileNameMapLoaded = false;
/*      */   static ContentHandlerFactory factory;
/* 1239 */   private static Hashtable handlers = new Hashtable();
/*      */   private static final String contentClassPrefix = "sun.net.www.content";
/*      */   private static final String contentPathProp = "java.content.handler.pkgs";
/*      */ 
/*      */   public static synchronized FileNameMap getFileNameMap()
/*      */   {
/*  316 */     if ((fileNameMap == null) && (!fileNameMapLoaded)) {
/*  317 */       fileNameMap = MimeTable.loadTable();
/*  318 */       fileNameMapLoaded = true;
/*      */     }
/*      */ 
/*  321 */     return new FileNameMap() {
/*  322 */       private FileNameMap map = URLConnection.fileNameMap;
/*      */ 
/*  324 */       public String getContentTypeFor(String paramAnonymousString) { return this.map.getContentTypeFor(paramAnonymousString); }
/*      */ 
/*      */     };
/*      */   }
/*      */ 
/*      */   public static void setFileNameMap(FileNameMap paramFileNameMap)
/*      */   {
/*  345 */     SecurityManager localSecurityManager = System.getSecurityManager();
/*  346 */     if (localSecurityManager != null) localSecurityManager.checkSetFactory();
/*  347 */     fileNameMap = paramFileNameMap;
/*      */   }
/*      */ 
/*      */   public abstract void connect()
/*      */     throws IOException;
/*      */ 
/*      */   public void setConnectTimeout(int paramInt)
/*      */   {
/*  397 */     if (paramInt < 0) {
/*  398 */       throw new IllegalArgumentException("timeout can not be negative");
/*      */     }
/*  400 */     this.connectTimeout = paramInt;
/*      */   }
/*      */ 
/*      */   public int getConnectTimeout()
/*      */   {
/*  416 */     return this.connectTimeout;
/*      */   }
/*      */ 
/*      */   public void setReadTimeout(int paramInt)
/*      */   {
/*  440 */     if (paramInt < 0) {
/*  441 */       throw new IllegalArgumentException("timeout can not be negative");
/*      */     }
/*  443 */     this.readTimeout = paramInt;
/*      */   }
/*      */ 
/*      */   public int getReadTimeout()
/*      */   {
/*  458 */     return this.readTimeout;
/*      */   }
/*      */ 
/*      */   protected URLConnection(URL paramURL)
/*      */   {
/*  468 */     this.url = paramURL;
/*      */   }
/*      */ 
/*      */   public URL getURL()
/*      */   {
/*  480 */     return this.url;
/*      */   }
/*      */ 
/*      */   public int getContentLength()
/*      */   {
/*  495 */     long l = getContentLengthLong();
/*  496 */     if (l > 2147483647L)
/*  497 */       return -1;
/*  498 */     return (int)l;
/*      */   }
/*      */ 
/*      */   public long getContentLengthLong()
/*      */   {
/*  511 */     return getHeaderFieldLong("content-length", -1L);
/*      */   }
/*      */ 
/*      */   public String getContentType()
/*      */   {
/*  522 */     return getHeaderField("content-type");
/*      */   }
/*      */ 
/*      */   public String getContentEncoding()
/*      */   {
/*  533 */     return getHeaderField("content-encoding");
/*      */   }
/*      */ 
/*      */   public long getExpiration()
/*      */   {
/*  545 */     return getHeaderFieldDate("expires", 0L);
/*      */   }
/*      */ 
/*      */   public long getDate()
/*      */   {
/*  557 */     return getHeaderFieldDate("date", 0L);
/*      */   }
/*      */ 
/*      */   public long getLastModified()
/*      */   {
/*  569 */     return getHeaderFieldDate("last-modified", 0L);
/*      */   }
/*      */ 
/*      */   public String getHeaderField(String paramString)
/*      */   {
/*  584 */     return null;
/*      */   }
/*      */ 
/*      */   public Map<String, List<String>> getHeaderFields()
/*      */   {
/*  598 */     return Collections.EMPTY_MAP;
/*      */   }
/*      */ 
/*      */   public int getHeaderFieldInt(String paramString, int paramInt)
/*      */   {
/*  616 */     String str = getHeaderField(paramString);
/*      */     try {
/*  618 */       return Integer.parseInt(str); } catch (Exception localException) {
/*      */     }
/*  620 */     return paramInt;
/*      */   }
/*      */ 
/*      */   public long getHeaderFieldLong(String paramString, long paramLong)
/*      */   {
/*  639 */     String str = getHeaderField(paramString);
/*      */     try {
/*  641 */       return Long.parseLong(str); } catch (Exception localException) {
/*      */     }
/*  643 */     return paramLong;
/*      */   }
/*      */ 
/*      */   public long getHeaderFieldDate(String paramString, long paramLong)
/*      */   {
/*  663 */     String str = getHeaderField(paramString);
/*      */     try {
/*  665 */       return Date.parse(str); } catch (Exception localException) {
/*      */     }
/*  667 */     return paramLong;
/*      */   }
/*      */ 
/*      */   public String getHeaderFieldKey(int paramInt)
/*      */   {
/*  680 */     return null;
/*      */   }
/*      */ 
/*      */   public String getHeaderField(int paramInt)
/*      */   {
/*  698 */     return null;
/*      */   }
/*      */ 
/*      */   public Object getContent()
/*      */     throws IOException
/*      */   {
/*  748 */     getInputStream();
/*  749 */     return getContentHandler().getContent(this);
/*      */   }
/*      */ 
/*      */   public Object getContent(Class[] paramArrayOfClass)
/*      */     throws IOException
/*      */   {
/*  776 */     getInputStream();
/*  777 */     return getContentHandler().getContent(this, paramArrayOfClass);
/*      */   }
/*      */ 
/*      */   public Permission getPermission()
/*      */     throws IOException
/*      */   {
/*  820 */     return SecurityConstants.ALL_PERMISSION;
/*      */   }
/*      */ 
/*      */   public InputStream getInputStream()
/*      */     throws IOException
/*      */   {
/*  839 */     throw new UnknownServiceException("protocol doesn't support input");
/*      */   }
/*      */ 
/*      */   public OutputStream getOutputStream()
/*      */     throws IOException
/*      */   {
/*  852 */     throw new UnknownServiceException("protocol doesn't support output");
/*      */   }
/*      */ 
/*      */   public String toString()
/*      */   {
/*  861 */     return getClass().getName() + ":" + this.url;
/*      */   }
/*      */ 
/*      */   public void setDoInput(boolean paramBoolean)
/*      */   {
/*  878 */     if (this.connected)
/*  879 */       throw new IllegalStateException("Already connected");
/*  880 */     this.doInput = paramBoolean;
/*      */   }
/*      */ 
/*      */   public boolean getDoInput()
/*      */   {
/*  892 */     return this.doInput;
/*      */   }
/*      */ 
/*      */   public void setDoOutput(boolean paramBoolean)
/*      */   {
/*  908 */     if (this.connected)
/*  909 */       throw new IllegalStateException("Already connected");
/*  910 */     this.doOutput = paramBoolean;
/*      */   }
/*      */ 
/*      */   public boolean getDoOutput()
/*      */   {
/*  922 */     return this.doOutput;
/*      */   }
/*      */ 
/*      */   public void setAllowUserInteraction(boolean paramBoolean)
/*      */   {
/*  934 */     if (this.connected)
/*  935 */       throw new IllegalStateException("Already connected");
/*  936 */     this.allowUserInteraction = paramBoolean;
/*      */   }
/*      */ 
/*      */   public boolean getAllowUserInteraction()
/*      */   {
/*  948 */     return this.allowUserInteraction;
/*      */   }
/*      */ 
/*      */   public static void setDefaultAllowUserInteraction(boolean paramBoolean)
/*      */   {
/*  960 */     defaultAllowUserInteraction = paramBoolean;
/*      */   }
/*      */ 
/*      */   public static boolean getDefaultAllowUserInteraction()
/*      */   {
/*  976 */     return defaultAllowUserInteraction;
/*      */   }
/*      */ 
/*      */   public void setUseCaches(boolean paramBoolean)
/*      */   {
/*  997 */     if (this.connected)
/*  998 */       throw new IllegalStateException("Already connected");
/*  999 */     this.useCaches = paramBoolean;
/*      */   }
/*      */ 
/*      */   public boolean getUseCaches()
/*      */   {
/* 1011 */     return this.useCaches;
/*      */   }
/*      */ 
/*      */   public void setIfModifiedSince(long paramLong)
/*      */   {
/* 1023 */     if (this.connected)
/* 1024 */       throw new IllegalStateException("Already connected");
/* 1025 */     this.ifModifiedSince = paramLong;
/*      */   }
/*      */ 
/*      */   public long getIfModifiedSince()
/*      */   {
/* 1035 */     return this.ifModifiedSince;
/*      */   }
/*      */ 
/*      */   public boolean getDefaultUseCaches()
/*      */   {
/* 1051 */     return defaultUseCaches;
/*      */   }
/*      */ 
/*      */   public void setDefaultUseCaches(boolean paramBoolean)
/*      */   {
/* 1062 */     defaultUseCaches = paramBoolean;
/*      */   }
/*      */ 
/*      */   public void setRequestProperty(String paramString1, String paramString2)
/*      */   {
/* 1082 */     if (this.connected)
/* 1083 */       throw new IllegalStateException("Already connected");
/* 1084 */     if (paramString1 == null) {
/* 1085 */       throw new NullPointerException("key is null");
/*      */     }
/* 1087 */     if (this.requests == null) {
/* 1088 */       this.requests = new MessageHeader();
/*      */     }
/* 1090 */     this.requests.set(paramString1, paramString2);
/*      */   }
/*      */ 
/*      */   public void addRequestProperty(String paramString1, String paramString2)
/*      */   {
/* 1107 */     if (this.connected)
/* 1108 */       throw new IllegalStateException("Already connected");
/* 1109 */     if (paramString1 == null) {
/* 1110 */       throw new NullPointerException("key is null");
/*      */     }
/* 1112 */     if (this.requests == null) {
/* 1113 */       this.requests = new MessageHeader();
/*      */     }
/* 1115 */     this.requests.add(paramString1, paramString2);
/*      */   }
/*      */ 
/*      */   public String getRequestProperty(String paramString)
/*      */   {
/* 1130 */     if (this.connected) {
/* 1131 */       throw new IllegalStateException("Already connected");
/*      */     }
/* 1133 */     if (this.requests == null) {
/* 1134 */       return null;
/*      */     }
/* 1136 */     return this.requests.findValue(paramString);
/*      */   }
/*      */ 
/*      */   public Map<String, List<String>> getRequestProperties()
/*      */   {
/* 1152 */     if (this.connected) {
/* 1153 */       throw new IllegalStateException("Already connected");
/*      */     }
/* 1155 */     if (this.requests == null) {
/* 1156 */       return Collections.EMPTY_MAP;
/*      */     }
/* 1158 */     return this.requests.getHeaders(null);
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   public static void setDefaultRequestProperty(String paramString1, String paramString2)
/*      */   {
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   public static String getDefaultRequestProperty(String paramString)
/*      */   {
/* 1200 */     return null;
/*      */   }
/*      */ 
/*      */   public static synchronized void setContentHandlerFactory(ContentHandlerFactory paramContentHandlerFactory)
/*      */   {
/* 1229 */     if (factory != null) {
/* 1230 */       throw new Error("factory already defined");
/*      */     }
/* 1232 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 1233 */     if (localSecurityManager != null) {
/* 1234 */       localSecurityManager.checkSetFactory();
/*      */     }
/* 1236 */     factory = paramContentHandlerFactory;
/*      */   }
/*      */ 
/*      */   synchronized ContentHandler getContentHandler()
/*      */     throws UnknownServiceException
/*      */   {
/* 1248 */     String str = stripOffParameters(getContentType());
/* 1249 */     ContentHandler localContentHandler = null;
/* 1250 */     if (str == null)
/* 1251 */       throw new UnknownServiceException("no content-type");
/*      */     try {
/* 1253 */       localContentHandler = (ContentHandler)handlers.get(str);
/* 1254 */       if (localContentHandler != null)
/* 1255 */         return localContentHandler;
/*      */     }
/*      */     catch (Exception localException1) {
/*      */     }
/* 1259 */     if (factory != null)
/* 1260 */       localContentHandler = factory.createContentHandler(str);
/* 1261 */     if (localContentHandler == null) {
/*      */       try {
/* 1263 */         localContentHandler = lookupContentHandlerClassFor(str);
/*      */       } catch (Exception localException2) {
/* 1265 */         localException2.printStackTrace();
/* 1266 */         localContentHandler = UnknownContentHandler.INSTANCE;
/*      */       }
/* 1268 */       handlers.put(str, localContentHandler);
/*      */     }
/* 1270 */     return localContentHandler;
/*      */   }
/*      */ 
/*      */   private String stripOffParameters(String paramString)
/*      */   {
/* 1280 */     if (paramString == null)
/* 1281 */       return null;
/* 1282 */     int i = paramString.indexOf(';');
/*      */ 
/* 1284 */     if (i > 0) {
/* 1285 */       return paramString.substring(0, i);
/*      */     }
/* 1287 */     return paramString;
/*      */   }
/*      */ 
/*      */   private ContentHandler lookupContentHandlerClassFor(String paramString)
/*      */     throws InstantiationException, IllegalAccessException, ClassNotFoundException
/*      */   {
/* 1307 */     String str1 = typeToPackageName(paramString);
/*      */ 
/* 1309 */     String str2 = getContentHandlerPkgPrefixes();
/*      */ 
/* 1311 */     StringTokenizer localStringTokenizer = new StringTokenizer(str2, "|");
/*      */ 
/* 1314 */     while (localStringTokenizer.hasMoreTokens()) {
/* 1315 */       String str3 = localStringTokenizer.nextToken().trim();
/*      */       try
/*      */       {
/* 1318 */         String str4 = str3 + "." + str1;
/* 1319 */         Class localClass = null;
/*      */         try {
/* 1321 */           localClass = Class.forName(str4);
/*      */         } catch (ClassNotFoundException localClassNotFoundException) {
/* 1323 */           ClassLoader localClassLoader = ClassLoader.getSystemClassLoader();
/* 1324 */           if (localClassLoader != null) {
/* 1325 */             localClass = localClassLoader.loadClass(str4);
/*      */           }
/*      */         }
/* 1328 */         if (localClass != null) {
/* 1329 */           return (ContentHandler)localClass.newInstance();
/*      */         }
/*      */       }
/*      */       catch (Exception localException)
/*      */       {
/*      */       }
/*      */     }
/*      */ 
/* 1337 */     return UnknownContentHandler.INSTANCE;
/*      */   }
/*      */ 
/*      */   private String typeToPackageName(String paramString)
/*      */   {
/* 1347 */     paramString = paramString.toLowerCase();
/* 1348 */     int i = paramString.length();
/* 1349 */     char[] arrayOfChar = new char[i];
/* 1350 */     paramString.getChars(0, i, arrayOfChar, 0);
/* 1351 */     for (int j = 0; j < i; j++) {
/* 1352 */       int k = arrayOfChar[j];
/* 1353 */       if (k == 47)
/* 1354 */         arrayOfChar[j] = '.';
/* 1355 */       else if (((65 > k) || (k > 90)) && ((97 > k) || (k > 122)) && ((48 > k) || (k > 57)))
/*      */       {
/* 1358 */         arrayOfChar[j] = '_';
/*      */       }
/*      */     }
/* 1361 */     return new String(arrayOfChar);
/*      */   }
/*      */ 
/*      */   private String getContentHandlerPkgPrefixes()
/*      */   {
/* 1373 */     String str = (String)AccessController.doPrivileged(new GetPropertyAction("java.content.handler.pkgs", ""));
/*      */ 
/* 1376 */     if (str != "") {
/* 1377 */       str = str + "|";
/*      */     }
/*      */ 
/* 1380 */     return str + "sun.net.www.content";
/*      */   }
/*      */ 
/*      */   public static String guessContentTypeFromName(String paramString)
/*      */   {
/* 1395 */     return getFileNameMap().getContentTypeFor(paramString);
/*      */   }
/*      */ 
/*      */   public static String guessContentTypeFromStream(InputStream paramInputStream)
/*      */     throws IOException
/*      */   {
/* 1422 */     if (!paramInputStream.markSupported()) {
/* 1423 */       return null;
/*      */     }
/* 1425 */     paramInputStream.mark(16);
/* 1426 */     int i = paramInputStream.read();
/* 1427 */     int j = paramInputStream.read();
/* 1428 */     int k = paramInputStream.read();
/* 1429 */     int m = paramInputStream.read();
/* 1430 */     int n = paramInputStream.read();
/* 1431 */     int i1 = paramInputStream.read();
/* 1432 */     int i2 = paramInputStream.read();
/* 1433 */     int i3 = paramInputStream.read();
/* 1434 */     int i4 = paramInputStream.read();
/* 1435 */     int i5 = paramInputStream.read();
/* 1436 */     int i6 = paramInputStream.read();
/* 1437 */     int i7 = paramInputStream.read();
/* 1438 */     int i8 = paramInputStream.read();
/* 1439 */     int i9 = paramInputStream.read();
/* 1440 */     int i10 = paramInputStream.read();
/* 1441 */     int i11 = paramInputStream.read();
/* 1442 */     paramInputStream.reset();
/*      */ 
/* 1444 */     if ((i == 202) && (j == 254) && (k == 186) && (m == 190)) {
/* 1445 */       return "application/java-vm";
/*      */     }
/*      */ 
/* 1448 */     if ((i == 172) && (j == 237))
/*      */     {
/* 1450 */       return "application/x-java-serialized-object";
/*      */     }
/*      */ 
/* 1453 */     if (i == 60) {
/* 1454 */       if ((j == 33) || ((j == 104) && (((k == 116) && (m == 109) && (n == 108)) || ((k == 101) && (m == 97) && (n == 100)))) || ((j == 98) && (k == 111) && (m == 100) && (n == 121)) || ((j == 72) && (((k == 84) && (m == 77) && (n == 76)) || ((k == 69) && (m == 65) && (n == 68)))) || ((j == 66) && (k == 79) && (m == 68) && (n == 89)))
/*      */       {
/* 1461 */         return "text/html";
/*      */       }
/*      */ 
/* 1464 */       if ((j == 63) && (k == 120) && (m == 109) && (n == 108) && (i1 == 32)) {
/* 1465 */         return "application/xml";
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1470 */     if ((i == 239) && (j == 187) && (k == 191) && 
/* 1471 */       (m == 60) && (n == 63) && (i1 == 120)) {
/* 1472 */       return "application/xml";
/*      */     }
/*      */ 
/* 1477 */     if ((i == 254) && (j == 255) && 
/* 1478 */       (k == 0) && (m == 60) && (n == 0) && (i1 == 63) && (i2 == 0) && (i3 == 120))
/*      */     {
/* 1480 */       return "application/xml";
/*      */     }
/*      */ 
/* 1484 */     if ((i == 255) && (j == 254) && 
/* 1485 */       (k == 60) && (m == 0) && (n == 63) && (i1 == 0) && (i2 == 120) && (i3 == 0))
/*      */     {
/* 1487 */       return "application/xml";
/*      */     }
/*      */ 
/* 1492 */     if ((i == 0) && (j == 0) && (k == 254) && (m == 255) && 
/* 1493 */       (n == 0) && (i1 == 0) && (i2 == 0) && (i3 == 60) && (i4 == 0) && (i5 == 0) && (i6 == 0) && (i7 == 63) && (i8 == 0) && (i9 == 0) && (i10 == 0) && (i11 == 120))
/*      */     {
/* 1496 */       return "application/xml";
/*      */     }
/*      */ 
/* 1500 */     if ((i == 255) && (j == 254) && (k == 0) && (m == 0) && 
/* 1501 */       (n == 60) && (i1 == 0) && (i2 == 0) && (i3 == 0) && (i4 == 63) && (i5 == 0) && (i6 == 0) && (i7 == 0) && (i8 == 120) && (i9 == 0) && (i10 == 0) && (i11 == 0))
/*      */     {
/* 1504 */       return "application/xml";
/*      */     }
/*      */ 
/* 1508 */     if ((i == 71) && (j == 73) && (k == 70) && (m == 56)) {
/* 1509 */       return "image/gif";
/*      */     }
/*      */ 
/* 1512 */     if ((i == 35) && (j == 100) && (k == 101) && (m == 102)) {
/* 1513 */       return "image/x-bitmap";
/*      */     }
/*      */ 
/* 1516 */     if ((i == 33) && (j == 32) && (k == 88) && (m == 80) && (n == 77) && (i1 == 50))
/*      */     {
/* 1518 */       return "image/x-pixmap";
/*      */     }
/*      */ 
/* 1521 */     if ((i == 137) && (j == 80) && (k == 78) && (m == 71) && (n == 13) && (i1 == 10) && (i2 == 26) && (i3 == 10))
/*      */     {
/* 1524 */       return "image/png";
/*      */     }
/*      */ 
/* 1527 */     if ((i == 255) && (j == 216) && (k == 255)) {
/* 1528 */       if (m == 224) {
/* 1529 */         return "image/jpeg";
/*      */       }
/*      */ 
/* 1538 */       if ((m == 225) && (i2 == 69) && (i3 == 120) && (i4 == 105) && (i5 == 102) && (i6 == 0))
/*      */       {
/* 1541 */         return "image/jpeg";
/*      */       }
/*      */ 
/* 1544 */       if (m == 238) {
/* 1545 */         return "image/jpg";
/*      */       }
/*      */     }
/*      */ 
/* 1549 */     if ((i == 208) && (j == 207) && (k == 17) && (m == 224) && (n == 161) && (i1 == 177) && (i2 == 26) && (i3 == 225))
/*      */     {
/* 1556 */       if (checkfpx(paramInputStream)) {
/* 1557 */         return "image/vnd.fpx";
/*      */       }
/*      */     }
/*      */ 
/* 1561 */     if ((i == 46) && (j == 115) && (k == 110) && (m == 100)) {
/* 1562 */       return "audio/basic";
/*      */     }
/*      */ 
/* 1565 */     if ((i == 100) && (j == 110) && (k == 115) && (m == 46)) {
/* 1566 */       return "audio/basic";
/*      */     }
/*      */ 
/* 1569 */     if ((i == 82) && (j == 73) && (k == 70) && (m == 70))
/*      */     {
/* 1573 */       return "audio/x-wav";
/*      */     }
/* 1575 */     return null;
/*      */   }
/*      */ 
/*      */   private static boolean checkfpx(InputStream paramInputStream)
/*      */     throws IOException
/*      */   {
/* 1614 */     paramInputStream.mark(256);
/*      */ 
/* 1618 */     long l1 = 28L;
/*      */     long l2;
/* 1621 */     if ((l2 = skipForward(paramInputStream, l1)) < l1) {
/* 1622 */       paramInputStream.reset();
/* 1623 */       return false;
/*      */     }
/*      */ 
/* 1626 */     int[] arrayOfInt = new int[16];
/* 1627 */     if (readBytes(arrayOfInt, 2, paramInputStream) < 0) {
/* 1628 */       paramInputStream.reset();
/* 1629 */       return false;
/*      */     }
/*      */ 
/* 1632 */     int i = arrayOfInt[0];
/*      */ 
/* 1634 */     l2 += 2L;
/*      */ 
/* 1636 */     if (readBytes(arrayOfInt, 2, paramInputStream) < 0) {
/* 1637 */       paramInputStream.reset();
/* 1638 */       return false;
/*      */     }
/*      */     int j;
/* 1641 */     if (i == 254) {
/* 1642 */       j = arrayOfInt[0];
/* 1643 */       j += (arrayOfInt[1] << 8);
/*      */     }
/*      */     else {
/* 1646 */       j = arrayOfInt[0] << 8;
/* 1647 */       j += arrayOfInt[1];
/*      */     }
/*      */ 
/* 1650 */     l2 += 2L;
/* 1651 */     l1 = 48L - l2;
/* 1652 */     long l3 = 0L;
/* 1653 */     if ((l3 = skipForward(paramInputStream, l1)) < l1) {
/* 1654 */       paramInputStream.reset();
/* 1655 */       return false;
/*      */     }
/* 1657 */     l2 += l3;
/*      */ 
/* 1659 */     if (readBytes(arrayOfInt, 4, paramInputStream) < 0) {
/* 1660 */       paramInputStream.reset();
/* 1661 */       return false;
/*      */     }
/*      */     int k;
/* 1665 */     if (i == 254) {
/* 1666 */       k = arrayOfInt[0];
/* 1667 */       k += (arrayOfInt[1] << 8);
/* 1668 */       k += (arrayOfInt[2] << 16);
/* 1669 */       k += (arrayOfInt[3] << 24);
/*      */     } else {
/* 1671 */       k = arrayOfInt[0] << 24;
/* 1672 */       k += (arrayOfInt[1] << 16);
/* 1673 */       k += (arrayOfInt[2] << 8);
/* 1674 */       k += arrayOfInt[3];
/*      */     }
/* 1676 */     l2 += 4L;
/* 1677 */     paramInputStream.reset();
/*      */ 
/* 1679 */     l1 = 512L + (1 << j) * k + 80L;
/*      */ 
/* 1682 */     if (l1 < 0L) {
/* 1683 */       return false;
/*      */     }
/*      */ 
/* 1692 */     paramInputStream.mark((int)l1 + 48);
/*      */ 
/* 1694 */     if (skipForward(paramInputStream, l1) < l1) {
/* 1695 */       paramInputStream.reset();
/* 1696 */       return false;
/*      */     }
/*      */ 
/* 1711 */     if (readBytes(arrayOfInt, 16, paramInputStream) < 0) {
/* 1712 */       paramInputStream.reset();
/* 1713 */       return false;
/*      */     }
/*      */ 
/* 1717 */     if ((i == 254) && (arrayOfInt[0] == 0) && (arrayOfInt[2] == 97) && (arrayOfInt[3] == 86) && (arrayOfInt[4] == 84) && (arrayOfInt[5] == 193) && (arrayOfInt[6] == 206) && (arrayOfInt[7] == 17) && (arrayOfInt[8] == 133) && (arrayOfInt[9] == 83) && (arrayOfInt[10] == 0) && (arrayOfInt[11] == 170) && (arrayOfInt[12] == 0) && (arrayOfInt[13] == 161) && (arrayOfInt[14] == 249) && (arrayOfInt[15] == 91))
/*      */     {
/* 1723 */       paramInputStream.reset();
/* 1724 */       return true;
/*      */     }
/*      */ 
/* 1728 */     if ((arrayOfInt[3] == 0) && (arrayOfInt[1] == 97) && (arrayOfInt[0] == 86) && (arrayOfInt[5] == 84) && (arrayOfInt[4] == 193) && (arrayOfInt[7] == 206) && (arrayOfInt[6] == 17) && (arrayOfInt[8] == 133) && (arrayOfInt[9] == 83) && (arrayOfInt[10] == 0) && (arrayOfInt[11] == 170) && (arrayOfInt[12] == 0) && (arrayOfInt[13] == 161) && (arrayOfInt[14] == 249) && (arrayOfInt[15] == 91))
/*      */     {
/* 1733 */       paramInputStream.reset();
/* 1734 */       return true;
/*      */     }
/* 1736 */     paramInputStream.reset();
/* 1737 */     return false;
/*      */   }
/*      */ 
/*      */   private static int readBytes(int[] paramArrayOfInt, int paramInt, InputStream paramInputStream)
/*      */     throws IOException
/*      */   {
/* 1748 */     byte[] arrayOfByte = new byte[paramInt];
/* 1749 */     if (paramInputStream.read(arrayOfByte, 0, paramInt) < paramInt) {
/* 1750 */       return -1;
/*      */     }
/*      */ 
/* 1754 */     for (int i = 0; i < paramInt; i++) {
/* 1755 */       arrayOfByte[i] &= 255;
/*      */     }
/* 1757 */     return 0;
/*      */   }
/*      */ 
/*      */   private static long skipForward(InputStream paramInputStream, long paramLong)
/*      */     throws IOException
/*      */   {
/* 1769 */     long l1 = 0L;
/* 1770 */     long l2 = 0L;
/*      */ 
/* 1772 */     while (l2 != paramLong) {
/* 1773 */       l1 = paramInputStream.skip(paramLong - l2);
/*      */ 
/* 1776 */       if (l1 <= 0L) {
/* 1777 */         if (paramInputStream.read() == -1) {
/* 1778 */           return l2;
/*      */         }
/* 1780 */         l2 += 1L;
/*      */       }
/*      */ 
/* 1783 */       l2 += l1;
/*      */     }
/* 1785 */     return l2;
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.net.URLConnection
 * JD-Core Version:    0.6.2
 */