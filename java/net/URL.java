/*      */ package java.net;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.ObjectOutputStream;
/*      */ import java.io.Serializable;
/*      */ import java.security.AccessController;
/*      */ import java.security.PrivilegedAction;
/*      */ import java.util.Hashtable;
/*      */ import java.util.StringTokenizer;
/*      */ import sun.net.ApplicationProxy;
/*      */ import sun.security.action.GetPropertyAction;
/*      */ import sun.security.util.SecurityConstants;
/*      */ 
/*      */ public final class URL
/*      */   implements Serializable
/*      */ {
/*      */   static final long serialVersionUID = -7627629688361524110L;
/*      */   private static final String protocolPathProp = "java.protocol.handler.pkgs";
/*      */   private String protocol;
/*      */   private String host;
/*  169 */   private int port = -1;
/*      */   private String file;
/*      */   private transient String query;
/*      */   private String authority;
/*      */   private transient String path;
/*      */   private transient String userInfo;
/*      */   private String ref;
/*      */   transient InetAddress hostAddress;
/*      */   transient URLStreamHandler handler;
/*  219 */   private int hashCode = -1;
/*      */   static URLStreamHandlerFactory factory;
/* 1118 */   static Hashtable handlers = new Hashtable();
/* 1119 */   private static Object streamHandlerLock = new Object();
/*      */   private static final String GOPHER = "gopher";
/*      */   private static final String ENABLE_GOPHER_PROP = "jdk.net.registerGopherProtocol";
/* 1124 */   private static final boolean enableGopher = ((Boolean)AccessController.doPrivileged(new PrivilegedAction()
/*      */   {
/*      */     public Boolean run()
/*      */     {
/* 1128 */       String str = System.getProperty("jdk.net.registerGopherProtocol");
/* 1129 */       return Boolean.valueOf(str != null);
/*      */     }
/*      */   })).booleanValue();
/*      */   private static final String JDK_PACKAGE_PREFIX = "sun.net.www.protocol";
/*      */ 
/*      */   public URL(String paramString1, String paramString2, int paramInt, String paramString3)
/*      */     throws MalformedURLException
/*      */   {
/*  302 */     this(paramString1, paramString2, paramInt, paramString3, null);
/*      */   }
/*      */ 
/*      */   public URL(String paramString1, String paramString2, String paramString3)
/*      */     throws MalformedURLException
/*      */   {
/*  325 */     this(paramString1, paramString2, -1, paramString3);
/*      */   }
/*      */ 
/*      */   public URL(String paramString1, String paramString2, int paramInt, String paramString3, URLStreamHandler paramURLStreamHandler)
/*      */     throws MalformedURLException
/*      */   {
/*  369 */     if (paramURLStreamHandler != null) {
/*  370 */       localObject = System.getSecurityManager();
/*  371 */       if (localObject != null)
/*      */       {
/*  373 */         checkSpecifyHandler((SecurityManager)localObject);
/*      */       }
/*      */     }
/*      */ 
/*  377 */     paramString1 = paramString1.toLowerCase();
/*  378 */     this.protocol = paramString1;
/*  379 */     if (paramString2 != null)
/*      */     {
/*  385 */       if ((paramString2.indexOf(':') >= 0) && (!paramString2.startsWith("["))) {
/*  386 */         paramString2 = "[" + paramString2 + "]";
/*      */       }
/*  388 */       this.host = paramString2;
/*      */ 
/*  390 */       if (paramInt < -1) {
/*  391 */         throw new MalformedURLException("Invalid port number :" + paramInt);
/*      */       }
/*      */ 
/*  394 */       this.port = paramInt;
/*  395 */       this.authority = (paramString2 + ":" + paramInt);
/*      */     }
/*      */ 
/*  398 */     Object localObject = new Parts(paramString3);
/*  399 */     this.path = ((Parts)localObject).getPath();
/*  400 */     this.query = ((Parts)localObject).getQuery();
/*      */ 
/*  402 */     if (this.query != null)
/*  403 */       this.file = (this.path + "?" + this.query);
/*      */     else {
/*  405 */       this.file = this.path;
/*      */     }
/*  407 */     this.ref = ((Parts)localObject).getRef();
/*      */ 
/*  411 */     if ((paramURLStreamHandler == null) && ((paramURLStreamHandler = getURLStreamHandler(paramString1)) == null))
/*      */     {
/*  413 */       throw new MalformedURLException("unknown protocol: " + paramString1);
/*      */     }
/*  415 */     this.handler = paramURLStreamHandler;
/*      */   }
/*      */ 
/*      */   public URL(String paramString)
/*      */     throws MalformedURLException
/*      */   {
/*  431 */     this(null, paramString);
/*      */   }
/*      */ 
/*      */   public URL(URL paramURL, String paramString)
/*      */     throws MalformedURLException
/*      */   {
/*  482 */     this(paramURL, paramString, null);
/*      */   }
/*      */ 
/*      */   public URL(URL paramURL, String paramString, URLStreamHandler paramURLStreamHandler)
/*      */     throws MalformedURLException
/*      */   {
/*  508 */     String str = paramString;
/*      */ 
/*  510 */     int m = 0;
/*  511 */     Object localObject1 = null;
/*  512 */     int n = 0;
/*  513 */     int i1 = 0;
/*      */     Object localObject2;
/*  516 */     if (paramURLStreamHandler != null) {
/*  517 */       localObject2 = System.getSecurityManager();
/*  518 */       if (localObject2 != null) {
/*  519 */         checkSpecifyHandler((SecurityManager)localObject2);
/*      */       }
/*      */     }
/*      */     try
/*      */     {
/*  524 */       int j = paramString.length();
/*  525 */       while ((j > 0) && (paramString.charAt(j - 1) <= ' ')) {
/*  526 */         j--;
/*      */       }
/*  528 */       while ((m < j) && (paramString.charAt(m) <= ' ')) {
/*  529 */         m++;
/*      */       }
/*      */ 
/*  532 */       if (paramString.regionMatches(true, m, "url:", 0, 4)) {
/*  533 */         m += 4;
/*      */       }
/*  535 */       if ((m < paramString.length()) && (paramString.charAt(m) == '#'))
/*      */       {
/*  540 */         n = 1;
/*      */       }
/*      */       int k;
/*  542 */       for (int i = m; (n == 0) && (i < j) && ((k = paramString.charAt(i)) != '/'); 
/*  543 */         i++) {
/*  544 */         if (k == 58)
/*      */         {
/*  546 */           localObject2 = paramString.substring(m, i).toLowerCase();
/*  547 */           if (!isValidProtocol((String)localObject2)) break;
/*  548 */           localObject1 = localObject2;
/*  549 */           m = i + 1; break;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  556 */       this.protocol = localObject1;
/*  557 */       if ((paramURL != null) && ((localObject1 == null) || (localObject1.equalsIgnoreCase(paramURL.protocol))))
/*      */       {
/*  561 */         if (paramURLStreamHandler == null) {
/*  562 */           paramURLStreamHandler = paramURL.handler;
/*      */         }
/*      */ 
/*  569 */         if ((paramURL.path != null) && (paramURL.path.startsWith("/"))) {
/*  570 */           localObject1 = null;
/*      */         }
/*  572 */         if (localObject1 == null) {
/*  573 */           this.protocol = paramURL.protocol;
/*  574 */           this.authority = paramURL.authority;
/*  575 */           this.userInfo = paramURL.userInfo;
/*  576 */           this.host = paramURL.host;
/*  577 */           this.port = paramURL.port;
/*  578 */           this.file = paramURL.file;
/*  579 */           this.path = paramURL.path;
/*  580 */           i1 = 1;
/*      */         }
/*      */       }
/*      */ 
/*  584 */       if (this.protocol == null) {
/*  585 */         throw new MalformedURLException("no protocol: " + str);
/*      */       }
/*      */ 
/*  590 */       if ((paramURLStreamHandler == null) && ((paramURLStreamHandler = getURLStreamHandler(this.protocol)) == null))
/*      */       {
/*  592 */         throw new MalformedURLException("unknown protocol: " + this.protocol);
/*      */       }
/*      */ 
/*  595 */       this.handler = paramURLStreamHandler;
/*      */ 
/*  597 */       i = paramString.indexOf('#', m);
/*  598 */       if (i >= 0) {
/*  599 */         this.ref = paramString.substring(i + 1, j);
/*  600 */         j = i;
/*      */       }
/*      */ 
/*  607 */       if ((i1 != 0) && (m == j)) {
/*  608 */         this.query = paramURL.query;
/*  609 */         if (this.ref == null) {
/*  610 */           this.ref = paramURL.ref;
/*      */         }
/*      */       }
/*      */ 
/*  614 */       paramURLStreamHandler.parseURL(this, paramString, m, j);
/*      */     }
/*      */     catch (MalformedURLException localMalformedURLException1) {
/*  617 */       throw localMalformedURLException1;
/*      */     } catch (Exception localException) {
/*  619 */       MalformedURLException localMalformedURLException2 = new MalformedURLException(localException.getMessage());
/*  620 */       localMalformedURLException2.initCause(localException);
/*  621 */       throw localMalformedURLException2;
/*      */     }
/*      */   }
/*      */ 
/*      */   private boolean isValidProtocol(String paramString)
/*      */   {
/*  629 */     int i = paramString.length();
/*  630 */     if (i < 1)
/*  631 */       return false;
/*  632 */     char c = paramString.charAt(0);
/*  633 */     if (!Character.isLetter(c))
/*  634 */       return false;
/*  635 */     for (int j = 1; j < i; j++) {
/*  636 */       c = paramString.charAt(j);
/*  637 */       if ((!Character.isLetterOrDigit(c)) && (c != '.') && (c != '+') && (c != '-'))
/*      */       {
/*  639 */         return false;
/*      */       }
/*      */     }
/*  642 */     return true;
/*      */   }
/*      */ 
/*      */   private void checkSpecifyHandler(SecurityManager paramSecurityManager)
/*      */   {
/*  649 */     paramSecurityManager.checkPermission(SecurityConstants.SPECIFY_HANDLER_PERMISSION);
/*      */   }
/*      */ 
/*      */   protected void set(String paramString1, String paramString2, int paramInt, String paramString3, String paramString4)
/*      */   {
/*  665 */     synchronized (this) {
/*  666 */       this.protocol = paramString1;
/*  667 */       this.host = paramString2;
/*  668 */       this.authority = (paramString2 + ":" + paramInt);
/*  669 */       this.port = paramInt;
/*  670 */       this.file = paramString3;
/*  671 */       this.ref = paramString4;
/*      */ 
/*  674 */       this.hashCode = -1;
/*  675 */       this.hostAddress = null;
/*  676 */       int i = paramString3.lastIndexOf('?');
/*  677 */       if (i != -1) {
/*  678 */         this.query = paramString3.substring(i + 1);
/*  679 */         this.path = paramString3.substring(0, i);
/*      */       } else {
/*  681 */         this.path = paramString3;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void set(String paramString1, String paramString2, int paramInt, String paramString3, String paramString4, String paramString5, String paramString6, String paramString7)
/*      */   {
/*  703 */     synchronized (this) {
/*  704 */       this.protocol = paramString1;
/*  705 */       this.host = paramString2;
/*  706 */       this.port = paramInt;
/*  707 */       this.file = (paramString5 + "?" + paramString6);
/*  708 */       this.userInfo = paramString4;
/*  709 */       this.path = paramString5;
/*  710 */       this.ref = paramString7;
/*      */ 
/*  713 */       this.hashCode = -1;
/*  714 */       this.hostAddress = null;
/*  715 */       this.query = paramString6;
/*  716 */       this.authority = paramString3;
/*      */     }
/*      */   }
/*      */ 
/*      */   public String getQuery()
/*      */   {
/*  728 */     return this.query;
/*      */   }
/*      */ 
/*      */   public String getPath()
/*      */   {
/*  739 */     return this.path;
/*      */   }
/*      */ 
/*      */   public String getUserInfo()
/*      */   {
/*  750 */     return this.userInfo;
/*      */   }
/*      */ 
/*      */   public String getAuthority()
/*      */   {
/*  760 */     return this.authority;
/*      */   }
/*      */ 
/*      */   public int getPort()
/*      */   {
/*  769 */     return this.port;
/*      */   }
/*      */ 
/*      */   public int getDefaultPort()
/*      */   {
/*  782 */     return this.handler.getDefaultPort();
/*      */   }
/*      */ 
/*      */   public String getProtocol()
/*      */   {
/*  791 */     return this.protocol;
/*      */   }
/*      */ 
/*      */   public String getHost()
/*      */   {
/*  803 */     return this.host;
/*      */   }
/*      */ 
/*      */   public String getFile()
/*      */   {
/*  818 */     return this.file;
/*      */   }
/*      */ 
/*      */   public String getRef()
/*      */   {
/*  829 */     return this.ref;
/*      */   }
/*      */ 
/*      */   public boolean equals(Object paramObject)
/*      */   {
/*  858 */     if (!(paramObject instanceof URL))
/*  859 */       return false;
/*  860 */     URL localURL = (URL)paramObject;
/*      */ 
/*  862 */     return this.handler.equals(this, localURL);
/*      */   }
/*      */ 
/*      */   public synchronized int hashCode()
/*      */   {
/*  874 */     if (this.hashCode != -1) {
/*  875 */       return this.hashCode;
/*      */     }
/*  877 */     this.hashCode = this.handler.hashCode(this);
/*  878 */     return this.hashCode;
/*      */   }
/*      */ 
/*      */   public boolean sameFile(URL paramURL)
/*      */   {
/*  893 */     return this.handler.sameFile(this, paramURL);
/*      */   }
/*      */ 
/*      */   public String toString()
/*      */   {
/*  907 */     return toExternalForm();
/*      */   }
/*      */ 
/*      */   public String toExternalForm()
/*      */   {
/*  921 */     return this.handler.toExternalForm(this);
/*      */   }
/*      */ 
/*      */   public URI toURI()
/*      */     throws URISyntaxException
/*      */   {
/*  938 */     return new URI(toString());
/*      */   }
/*      */ 
/*      */   public URLConnection openConnection()
/*      */     throws IOException
/*      */   {
/*  971 */     return this.handler.openConnection(this);
/*      */   }
/*      */ 
/*      */   public URLConnection openConnection(Proxy paramProxy)
/*      */     throws IOException
/*      */   {
/* 1005 */     if (paramProxy == null) {
/* 1006 */       throw new IllegalArgumentException("proxy can not be null");
/*      */     }
/*      */ 
/* 1010 */     ApplicationProxy localApplicationProxy = paramProxy == Proxy.NO_PROXY ? Proxy.NO_PROXY : ApplicationProxy.create(paramProxy);
/* 1011 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 1012 */     if ((localApplicationProxy.type() != Proxy.Type.DIRECT) && (localSecurityManager != null)) {
/* 1013 */       InetSocketAddress localInetSocketAddress = (InetSocketAddress)localApplicationProxy.address();
/* 1014 */       if (localInetSocketAddress.isUnresolved())
/* 1015 */         localSecurityManager.checkConnect(localInetSocketAddress.getHostName(), localInetSocketAddress.getPort());
/*      */       else {
/* 1017 */         localSecurityManager.checkConnect(localInetSocketAddress.getAddress().getHostAddress(), localInetSocketAddress.getPort());
/*      */       }
/*      */     }
/* 1020 */     return this.handler.openConnection(this, localApplicationProxy);
/*      */   }
/*      */ 
/*      */   public final InputStream openStream()
/*      */     throws IOException
/*      */   {
/* 1037 */     return openConnection().getInputStream();
/*      */   }
/*      */ 
/*      */   public final Object getContent()
/*      */     throws IOException
/*      */   {
/* 1051 */     return openConnection().getContent();
/*      */   }
/*      */ 
/*      */   public final Object getContent(Class[] paramArrayOfClass)
/*      */     throws IOException
/*      */   {
/* 1070 */     return openConnection().getContent(paramArrayOfClass);
/*      */   }
/*      */ 
/*      */   public static void setURLStreamHandlerFactory(URLStreamHandlerFactory paramURLStreamHandlerFactory)
/*      */   {
/* 1102 */     synchronized (streamHandlerLock) {
/* 1103 */       if (factory != null) {
/* 1104 */         throw new Error("factory already defined");
/*      */       }
/* 1106 */       SecurityManager localSecurityManager = System.getSecurityManager();
/* 1107 */       if (localSecurityManager != null) {
/* 1108 */         localSecurityManager.checkSetFactory();
/*      */       }
/* 1110 */       handlers.clear();
/* 1111 */       factory = paramURLStreamHandlerFactory;
/*      */     }
/*      */   }
/*      */ 
/*      */   static URLStreamHandler getURLStreamHandler(String paramString)
/*      */   {
/* 1143 */     Object localObject1 = (URLStreamHandler)handlers.get(paramString);
/* 1144 */     if (localObject1 == null)
/*      */     {
/* 1146 */       int i = 0;
/*      */ 
/* 1149 */       if (factory != null) {
/* 1150 */         localObject1 = factory.createURLStreamHandler(paramString);
/* 1151 */         i = 1;
/*      */       }
/*      */       Object localObject2;
/* 1155 */       if (localObject1 == null) {
/* 1156 */         String str1 = null;
/*      */ 
/* 1158 */         str1 = (String)AccessController.doPrivileged(new GetPropertyAction("java.protocol.handler.pkgs", ""));
/*      */ 
/* 1162 */         if (str1 != "") {
/* 1163 */           str1 = str1 + "|";
/*      */         }
/*      */ 
/* 1168 */         str1 = str1 + "sun.net.www.protocol";
/*      */ 
/* 1170 */         localObject2 = new StringTokenizer(str1, "|");
/*      */ 
/* 1173 */         while ((localObject1 == null) && (((StringTokenizer)localObject2).hasMoreTokens()))
/*      */         {
/* 1176 */           String str2 = ((StringTokenizer)localObject2).nextToken().trim();
/*      */ 
/* 1181 */           if ((!paramString.equalsIgnoreCase("gopher")) || (!str2.equals("sun.net.www.protocol")) || (enableGopher))
/*      */           {
/*      */             try
/*      */             {
/* 1187 */               String str3 = str2 + "." + paramString + ".Handler";
/*      */ 
/* 1189 */               Class localClass = null;
/*      */               try {
/* 1191 */                 localClass = Class.forName(str3);
/*      */               } catch (ClassNotFoundException localClassNotFoundException) {
/* 1193 */                 ClassLoader localClassLoader = ClassLoader.getSystemClassLoader();
/* 1194 */                 if (localClassLoader != null) {
/* 1195 */                   localClass = localClassLoader.loadClass(str3);
/*      */                 }
/*      */               }
/* 1198 */               if (localClass != null) {
/* 1199 */                 localObject1 = (URLStreamHandler)localClass.newInstance();
/*      */               }
/*      */             }
/*      */             catch (Exception localException)
/*      */             {
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/* 1208 */       synchronized (streamHandlerLock)
/*      */       {
/* 1210 */         localObject2 = null;
/*      */ 
/* 1214 */         localObject2 = (URLStreamHandler)handlers.get(paramString);
/*      */ 
/* 1216 */         if (localObject2 != null) {
/* 1217 */           return localObject2;
/*      */         }
/*      */ 
/* 1222 */         if ((i == 0) && (factory != null)) {
/* 1223 */           localObject2 = factory.createURLStreamHandler(paramString);
/*      */         }
/*      */ 
/* 1226 */         if (localObject2 != null)
/*      */         {
/* 1230 */           localObject1 = localObject2;
/*      */         }
/*      */ 
/* 1234 */         if (localObject1 != null) {
/* 1235 */           handlers.put(paramString, localObject1);
/*      */         }
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1241 */     return localObject1;
/*      */   }
/*      */ 
/*      */   private synchronized void writeObject(ObjectOutputStream paramObjectOutputStream)
/*      */     throws IOException
/*      */   {
/* 1258 */     paramObjectOutputStream.defaultWriteObject();
/*      */   }
/*      */ 
/*      */   private synchronized void readObject(ObjectInputStream paramObjectInputStream)
/*      */     throws IOException, ClassNotFoundException
/*      */   {
/* 1269 */     paramObjectInputStream.defaultReadObject();
/* 1270 */     if ((this.handler = getURLStreamHandler(this.protocol)) == null)
/* 1271 */       throw new IOException("unknown protocol: " + this.protocol);
/*      */     int i;
/* 1275 */     if ((this.authority == null) && (((this.host != null) && (this.host.length() > 0)) || (this.port != -1)))
/*      */     {
/* 1277 */       if (this.host == null)
/* 1278 */         this.host = "";
/* 1279 */       this.authority = (this.host + ":" + this.port);
/*      */ 
/* 1282 */       i = this.host.lastIndexOf('@');
/* 1283 */       if (i != -1) {
/* 1284 */         this.userInfo = this.host.substring(0, i);
/* 1285 */         this.host = this.host.substring(i + 1);
/*      */       }
/* 1287 */     } else if (this.authority != null)
/*      */     {
/* 1289 */       i = this.authority.indexOf('@');
/* 1290 */       if (i != -1) {
/* 1291 */         this.userInfo = this.authority.substring(0, i);
/*      */       }
/*      */     }
/*      */ 
/* 1295 */     this.path = null;
/* 1296 */     this.query = null;
/* 1297 */     if (this.file != null)
/*      */     {
/* 1299 */       i = this.file.lastIndexOf('?');
/* 1300 */       if (i != -1) {
/* 1301 */         this.query = this.file.substring(i + 1);
/* 1302 */         this.path = this.file.substring(0, i);
/*      */       } else {
/* 1304 */         this.path = this.file;
/*      */       }
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.net.URL
 * JD-Core Version:    0.6.2
 */