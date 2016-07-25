/*     */ package java.rmi;
/*     */ 
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.rmi.registry.LocateRegistry;
/*     */ import java.rmi.registry.Registry;
/*     */ 
/*     */ public final class Naming
/*     */ {
/*     */   public static Remote lookup(String paramString)
/*     */     throws NotBoundException, MalformedURLException, RemoteException
/*     */   {
/*  96 */     ParsedNamingURL localParsedNamingURL = parseURL(paramString);
/*  97 */     Registry localRegistry = getRegistry(localParsedNamingURL);
/*     */ 
/*  99 */     if (localParsedNamingURL.name == null)
/* 100 */       return localRegistry;
/* 101 */     return localRegistry.lookup(localParsedNamingURL.name);
/*     */   }
/*     */ 
/*     */   public static void bind(String paramString, Remote paramRemote)
/*     */     throws AlreadyBoundException, MalformedURLException, RemoteException
/*     */   {
/* 122 */     ParsedNamingURL localParsedNamingURL = parseURL(paramString);
/* 123 */     Registry localRegistry = getRegistry(localParsedNamingURL);
/*     */ 
/* 125 */     if (paramRemote == null) {
/* 126 */       throw new NullPointerException("cannot bind to null");
/*     */     }
/* 128 */     localRegistry.bind(localParsedNamingURL.name, paramRemote);
/*     */   }
/*     */ 
/*     */   public static void unbind(String paramString)
/*     */     throws RemoteException, NotBoundException, MalformedURLException
/*     */   {
/* 149 */     ParsedNamingURL localParsedNamingURL = parseURL(paramString);
/* 150 */     Registry localRegistry = getRegistry(localParsedNamingURL);
/*     */ 
/* 152 */     localRegistry.unbind(localParsedNamingURL.name);
/*     */   }
/*     */ 
/*     */   public static void rebind(String paramString, Remote paramRemote)
/*     */     throws RemoteException, MalformedURLException
/*     */   {
/* 171 */     ParsedNamingURL localParsedNamingURL = parseURL(paramString);
/* 172 */     Registry localRegistry = getRegistry(localParsedNamingURL);
/*     */ 
/* 174 */     if (paramRemote == null) {
/* 175 */       throw new NullPointerException("cannot bind to null");
/*     */     }
/* 177 */     localRegistry.rebind(localParsedNamingURL.name, paramRemote);
/*     */   }
/*     */ 
/*     */   public static String[] list(String paramString)
/*     */     throws RemoteException, MalformedURLException
/*     */   {
/* 198 */     ParsedNamingURL localParsedNamingURL = parseURL(paramString);
/* 199 */     Registry localRegistry = getRegistry(localParsedNamingURL);
/*     */ 
/* 201 */     String str = "";
/* 202 */     if ((localParsedNamingURL.port > 0) || (!localParsedNamingURL.host.equals("")))
/* 203 */       str = str + "//" + localParsedNamingURL.host;
/* 204 */     if (localParsedNamingURL.port > 0)
/* 205 */       str = str + ":" + localParsedNamingURL.port;
/* 206 */     str = str + "/";
/*     */ 
/* 208 */     String[] arrayOfString = localRegistry.list();
/* 209 */     for (int i = 0; i < arrayOfString.length; i++) {
/* 210 */       arrayOfString[i] = (str + arrayOfString[i]);
/*     */     }
/* 212 */     return arrayOfString;
/*     */   }
/*     */ 
/*     */   private static Registry getRegistry(ParsedNamingURL paramParsedNamingURL)
/*     */     throws RemoteException
/*     */   {
/* 221 */     return LocateRegistry.getRegistry(paramParsedNamingURL.host, paramParsedNamingURL.port);
/*     */   }
/*     */ 
/*     */   private static ParsedNamingURL parseURL(String paramString)
/*     */     throws MalformedURLException
/*     */   {
/*     */     try
/*     */     {
/* 237 */       return intParseURL(paramString);
/*     */     }
/*     */     catch (URISyntaxException localURISyntaxException1)
/*     */     {
/* 243 */       MalformedURLException localMalformedURLException1 = new MalformedURLException("invalid URL String: " + paramString);
/*     */ 
/* 245 */       localMalformedURLException1.initCause(localURISyntaxException1);
/* 246 */       int i = paramString.indexOf(':');
/* 247 */       int j = paramString.indexOf("//:");
/* 248 */       if (j < 0) {
/* 249 */         throw localMalformedURLException1;
/*     */       }
/* 251 */       if ((j == 0) || ((i > 0) && (j == i + 1)))
/*     */       {
/* 254 */         int k = j + 2;
/* 255 */         String str = paramString.substring(0, k) + "localhost" + paramString.substring(k);
/*     */         try
/*     */         {
/* 259 */           return intParseURL(str);
/*     */         } catch (URISyntaxException localURISyntaxException2) {
/* 261 */           throw localMalformedURLException1;
/*     */         } catch (MalformedURLException localMalformedURLException2) {
/* 263 */           throw localMalformedURLException2;
/*     */         }
/*     */       }
/* 266 */       throw localMalformedURLException1;
/*     */     }
/*     */   }
/*     */ 
/*     */   private static ParsedNamingURL intParseURL(String paramString)
/*     */     throws MalformedURLException, URISyntaxException
/*     */   {
/* 273 */     URI localURI = new URI(paramString);
/* 274 */     if (localURI.isOpaque()) {
/* 275 */       throw new MalformedURLException("not a hierarchical URL: " + paramString);
/*     */     }
/*     */ 
/* 278 */     if (localURI.getFragment() != null) {
/* 279 */       throw new MalformedURLException("invalid character, '#', in URL name: " + paramString);
/*     */     }
/* 281 */     if (localURI.getQuery() != null) {
/* 282 */       throw new MalformedURLException("invalid character, '?', in URL name: " + paramString);
/*     */     }
/* 284 */     if (localURI.getUserInfo() != null) {
/* 285 */       throw new MalformedURLException("invalid character, '@', in URL host: " + paramString);
/*     */     }
/*     */ 
/* 288 */     String str1 = localURI.getScheme();
/* 289 */     if ((str1 != null) && (!str1.equals("rmi"))) {
/* 290 */       throw new MalformedURLException("invalid URL scheme: " + paramString);
/*     */     }
/*     */ 
/* 293 */     String str2 = localURI.getPath();
/* 294 */     if (str2 != null) {
/* 295 */       if (str2.startsWith("/")) {
/* 296 */         str2 = str2.substring(1);
/*     */       }
/* 298 */       if (str2.length() == 0) {
/* 299 */         str2 = null;
/*     */       }
/*     */     }
/*     */ 
/* 303 */     String str3 = localURI.getHost();
/* 304 */     if (str3 == null) {
/* 305 */       str3 = "";
/*     */       try
/*     */       {
/* 313 */         localURI.parseServerAuthority();
/*     */       }
/*     */       catch (URISyntaxException localURISyntaxException1) {
/* 316 */         String str4 = localURI.getAuthority();
/* 317 */         if ((str4 != null) && (str4.startsWith(":")))
/*     */         {
/* 319 */           str4 = "localhost" + str4;
/*     */           try {
/* 321 */             localURI = new URI(null, str4, null, null, null);
/*     */ 
/* 324 */             localURI.parseServerAuthority();
/*     */           } catch (URISyntaxException localURISyntaxException2) {
/* 326 */             throw new MalformedURLException("invalid authority: " + paramString);
/*     */           }
/*     */         }
/*     */         else {
/* 330 */           throw new MalformedURLException("invalid authority: " + paramString);
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 335 */     int i = localURI.getPort();
/* 336 */     if (i == -1) {
/* 337 */       i = 1099;
/*     */     }
/* 339 */     return new ParsedNamingURL(str3, i, str2);
/*     */   }
/*     */ 
/*     */   private static class ParsedNamingURL
/*     */   {
/*     */     String host;
/*     */     int port;
/*     */     String name;
/*     */ 
/*     */     ParsedNamingURL(String paramString1, int paramInt, String paramString2) {
/* 351 */       this.host = paramString1;
/* 352 */       this.port = paramInt;
/* 353 */       this.name = paramString2;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.rmi.Naming
 * JD-Core Version:    0.6.2
 */