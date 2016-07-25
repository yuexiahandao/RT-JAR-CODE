/*     */ package sun.net.spi;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.Proxy;
/*     */ import java.net.Proxy.Type;
/*     */ import java.net.ProxySelector;
/*     */ import java.net.SocketAddress;
/*     */ import java.net.URI;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.StringTokenizer;
/*     */ import sun.misc.REException;
/*     */ import sun.misc.RegexpPool;
/*     */ import sun.net.NetProperties;
/*     */ import sun.net.SocksProxy;
/*     */ import sun.security.action.LoadLibraryAction;
/*     */ 
/*     */ public class DefaultProxySelector extends ProxySelector
/*     */ {
/*  74 */   static final String[][] props = { { "http", "http.proxy", "proxy", "socksProxy" }, { "https", "https.proxy", "proxy", "socksProxy" }, { "ftp", "ftp.proxy", "ftpProxy", "proxy", "socksProxy" }, { "gopher", "gopherProxy", "socksProxy" }, { "socket", "socksProxy" } };
/*     */   private static final String SOCKS_PROXY_VERSION = "socksProxyVersion";
/*  87 */   private static boolean hasSystemProxies = false;
/*     */ 
/*     */   public List<Proxy> select(URI paramURI)
/*     */   {
/* 140 */     if (paramURI == null) {
/* 141 */       throw new IllegalArgumentException("URI can't be null.");
/*     */     }
/* 143 */     String str1 = paramURI.getScheme();
/* 144 */     Object localObject1 = paramURI.getHost();
/*     */ 
/* 146 */     if (localObject1 == null)
/*     */     {
/* 154 */       localObject2 = paramURI.getAuthority();
/* 155 */       if (localObject2 != null)
/*     */       {
/* 157 */         int i = ((String)localObject2).indexOf('@');
/* 158 */         if (i >= 0) {
/* 159 */           localObject2 = ((String)localObject2).substring(i + 1);
/*     */         }
/* 161 */         i = ((String)localObject2).lastIndexOf(':');
/* 162 */         if (i >= 0) {
/* 163 */           localObject2 = ((String)localObject2).substring(0, i);
/*     */         }
/* 165 */         localObject1 = localObject2;
/*     */       }
/*     */     }
/*     */ 
/* 169 */     if ((str1 == null) || (localObject1 == null)) {
/* 170 */       throw new IllegalArgumentException("protocol = " + str1 + " host = " + (String)localObject1);
/*     */     }
/* 172 */     Object localObject2 = new ArrayList(1);
/*     */ 
/* 174 */     NonProxyInfo localNonProxyInfo1 = null;
/*     */ 
/* 176 */     if ("http".equalsIgnoreCase(str1))
/* 177 */       localNonProxyInfo1 = NonProxyInfo.httpNonProxyInfo;
/* 178 */     else if ("https".equalsIgnoreCase(str1))
/*     */     {
/* 181 */       localNonProxyInfo1 = NonProxyInfo.httpNonProxyInfo;
/* 182 */     } else if ("ftp".equalsIgnoreCase(str1)) {
/* 183 */       localNonProxyInfo1 = NonProxyInfo.ftpNonProxyInfo;
/*     */     }
/*     */ 
/* 189 */     final String str2 = str1;
/* 190 */     final NonProxyInfo localNonProxyInfo2 = localNonProxyInfo1;
/* 191 */     final String str3 = ((String)localObject1).toLowerCase();
/*     */ 
/* 199 */     Proxy localProxy = (Proxy)AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public Proxy run()
/*     */       {
/* 203 */         String str1 = null;
/* 204 */         int j = 0;
/* 205 */         String str2 = null;
/* 206 */         InetSocketAddress localInetSocketAddress = null;
/*     */ 
/* 209 */         for (int i = 0; i < DefaultProxySelector.props.length; i++) {
/* 210 */           if (DefaultProxySelector.props[i][0].equalsIgnoreCase(str2)) {
/* 211 */             for (Object localObject1 = 1; localObject1 < DefaultProxySelector.props[i].length; localObject1++)
/*     */             {
/* 216 */               str1 = NetProperties.get(DefaultProxySelector.props[i][localObject1] + "Host");
/* 217 */               if ((str1 != null) && (str1.length() != 0))
/*     */                 break;
/*     */             }
/*     */             Object localObject3;
/* 220 */             if ((str1 == null) || (str1.length() == 0))
/*     */             {
/* 227 */               if (DefaultProxySelector.hasSystemProxies)
/*     */               {
/*     */                 String str3;
/* 229 */                 if (str2.equalsIgnoreCase("socket"))
/* 230 */                   str3 = "socks";
/*     */                 else
/* 232 */                   str3 = str2;
/* 233 */                 localObject3 = DefaultProxySelector.this.getSystemProxy(str3, str3);
/* 234 */                 if (localObject3 != null) {
/* 235 */                   return localObject3;
/*     */                 }
/*     */               }
/* 238 */               return Proxy.NO_PROXY;
/*     */             }
/*     */ 
/* 242 */             if (localNonProxyInfo2 != null) {
/* 243 */               str2 = NetProperties.get(localNonProxyInfo2.property);
/* 244 */               synchronized (localNonProxyInfo2) {
/* 245 */                 if (str2 == null) {
/* 246 */                   if (localNonProxyInfo2.defaultVal != null) {
/* 247 */                     str2 = localNonProxyInfo2.defaultVal;
/*     */                   } else {
/* 249 */                     localNonProxyInfo2.hostsSource = null;
/* 250 */                     localNonProxyInfo2.hostsPool = null;
/*     */                   }
/* 252 */                 } else if (str2.length() != 0)
/*     */                 {
/* 256 */                   str2 = str2 + "|localhost|127.*|[::1]|0.0.0.0|[::0]";
/*     */                 }
/*     */ 
/* 259 */                 if ((str2 != null) && 
/* 260 */                   (!str2.equals(localNonProxyInfo2.hostsSource))) {
/* 261 */                   localObject3 = new RegexpPool();
/* 262 */                   StringTokenizer localStringTokenizer = new StringTokenizer(str2, "|", false);
/*     */                   try {
/* 264 */                     while (localStringTokenizer.hasMoreTokens())
/* 265 */                       ((RegexpPool)localObject3).add(localStringTokenizer.nextToken().toLowerCase(), Boolean.TRUE);
/*     */                   }
/*     */                   catch (REException localREException) {
/*     */                   }
/* 269 */                   localNonProxyInfo2.hostsPool = ((RegexpPool)localObject3);
/* 270 */                   localNonProxyInfo2.hostsSource = str2;
/*     */                 }
/*     */ 
/* 273 */                 if ((localNonProxyInfo2.hostsPool != null) && (localNonProxyInfo2.hostsPool.match(str3) != null))
/*     */                 {
/* 275 */                   return Proxy.NO_PROXY;
/*     */                 }
/*     */               }
/*     */ 
/*     */             }
/*     */ 
/* 281 */             j = NetProperties.getInteger(DefaultProxySelector.props[i][localObject1] + "Port", 0).intValue();
/* 282 */             if ((j == 0) && (localObject1 < DefaultProxySelector.props[i].length - 1))
/*     */             {
/* 286 */               for (Object localObject2 = 1; localObject2 < DefaultProxySelector.props[i].length - 1; localObject2++) {
/* 287 */                 if ((localObject2 != localObject1) && (j == 0)) {
/* 288 */                   j = NetProperties.getInteger(DefaultProxySelector.props[i][localObject2] + "Port", 0).intValue();
/*     */                 }
/*     */               }
/*     */             }
/*     */ 
/* 293 */             if (j == 0) {
/* 294 */               if (localObject1 == DefaultProxySelector.props[i].length - 1)
/* 295 */                 j = DefaultProxySelector.this.defaultPort("socket");
/*     */               else {
/* 297 */                 j = DefaultProxySelector.this.defaultPort(str2);
/*     */               }
/*     */ 
/*     */             }
/*     */ 
/* 302 */             localInetSocketAddress = InetSocketAddress.createUnresolved(str1, j);
/*     */ 
/* 304 */             if (localObject1 == DefaultProxySelector.props[i].length - 1) {
/* 305 */               int k = NetProperties.getInteger("socksProxyVersion", 5).intValue();
/* 306 */               return SocksProxy.create(localInetSocketAddress, k);
/*     */             }
/* 308 */             return new Proxy(Proxy.Type.HTTP, localInetSocketAddress);
/*     */           }
/*     */         }
/*     */ 
/* 312 */         return Proxy.NO_PROXY;
/*     */       }
/*     */     });
/* 315 */     ((List)localObject2).add(localProxy);
/*     */ 
/* 321 */     return localObject2;
/*     */   }
/*     */ 
/*     */   public void connectFailed(URI paramURI, SocketAddress paramSocketAddress, IOException paramIOException) {
/* 325 */     if ((paramURI == null) || (paramSocketAddress == null) || (paramIOException == null))
/* 326 */       throw new IllegalArgumentException("Arguments can't be null.");
/*     */   }
/*     */ 
/*     */   private int defaultPort(String paramString)
/*     */   {
/* 333 */     if ("http".equalsIgnoreCase(paramString))
/* 334 */       return 80;
/* 335 */     if ("https".equalsIgnoreCase(paramString))
/* 336 */       return 443;
/* 337 */     if ("ftp".equalsIgnoreCase(paramString))
/* 338 */       return 80;
/* 339 */     if ("socket".equalsIgnoreCase(paramString))
/* 340 */       return 1080;
/* 341 */     if ("gopher".equalsIgnoreCase(paramString)) {
/* 342 */       return 80;
/*     */     }
/* 344 */     return -1;
/*     */   }
/*     */ 
/*     */   private static native boolean init();
/*     */ 
/*     */   private synchronized native Proxy getSystemProxy(String paramString1, String paramString2);
/*     */ 
/*     */   static
/*     */   {
/*  91 */     Boolean localBoolean = (Boolean)AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public Boolean run() {
/*  94 */         return NetProperties.getBoolean("java.net.useSystemProxies");
/*     */       }
/*     */     });
/*  96 */     if ((localBoolean != null) && (localBoolean.booleanValue())) {
/*  97 */       AccessController.doPrivileged(new LoadLibraryAction("net"));
/*     */ 
/*  99 */       hasSystemProxies = init();
/*     */     }
/*     */   }
/*     */ 
/*     */   static class NonProxyInfo
/*     */   {
/*     */     static final String defStringVal = "localhost|127.*|[::1]|0.0.0.0|[::0]";
/*     */     String hostsSource;
/*     */     RegexpPool hostsPool;
/*     */     final String property;
/*     */     final String defaultVal;
/* 120 */     static NonProxyInfo ftpNonProxyInfo = new NonProxyInfo("ftp.nonProxyHosts", null, null, "localhost|127.*|[::1]|0.0.0.0|[::0]");
/* 121 */     static NonProxyInfo httpNonProxyInfo = new NonProxyInfo("http.nonProxyHosts", null, null, "localhost|127.*|[::1]|0.0.0.0|[::0]");
/*     */ 
/*     */     NonProxyInfo(String paramString1, String paramString2, RegexpPool paramRegexpPool, String paramString3) {
/* 124 */       this.property = paramString1;
/* 125 */       this.hostsSource = paramString2;
/* 126 */       this.hostsPool = paramRegexpPool;
/* 127 */       this.defaultVal = paramString3;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.net.spi.DefaultProxySelector
 * JD-Core Version:    0.6.2
 */