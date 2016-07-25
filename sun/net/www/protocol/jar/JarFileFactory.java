/*     */ package sun.net.www.protocol.jar;
/*     */ 
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.FilePermission;
/*     */ import java.io.IOException;
/*     */ import java.net.SocketPermission;
/*     */ import java.net.URL;
/*     */ import java.net.URLConnection;
/*     */ import java.security.Permission;
/*     */ import java.util.HashMap;
/*     */ import java.util.jar.JarFile;
/*     */ import sun.net.util.URLUtil;
/*     */ 
/*     */ class JarFileFactory
/*     */   implements URLJarFile.URLJarFileCloseController
/*     */ {
/*  46 */   private static final HashMap<String, JarFile> fileCache = new HashMap();
/*     */ 
/*  49 */   private static final HashMap<JarFile, URL> urlCache = new HashMap();
/*     */ 
/*  51 */   private static final JarFileFactory instance = new JarFileFactory();
/*     */ 
/*     */   public static JarFileFactory getInstance()
/*     */   {
/*  56 */     return instance;
/*     */   }
/*     */ 
/*     */   URLConnection getConnection(JarFile paramJarFile)
/*     */     throws IOException
/*     */   {
/*     */     URL localURL;
/*  61 */     synchronized (instance) {
/*  62 */       localURL = (URL)urlCache.get(paramJarFile);
/*     */     }
/*  64 */     if (localURL != null) {
/*  65 */       return localURL.openConnection();
/*     */     }
/*  67 */     return null;
/*     */   }
/*     */ 
/*     */   public JarFile get(URL paramURL) throws IOException {
/*  71 */     return get(paramURL, true);
/*     */   }
/*     */ 
/*     */   JarFile get(URL paramURL, boolean paramBoolean)
/*     */     throws IOException
/*     */   {
/*     */     Object localObject1;
/*  75 */     if (paramURL.getProtocol().equalsIgnoreCase("file"))
/*     */     {
/*  78 */       localObject1 = paramURL.getHost();
/*  79 */       if ((localObject1 != null) && (!((String)localObject1).equals("")) && (!((String)localObject1).equalsIgnoreCase("localhost")))
/*     */       {
/*  82 */         paramURL = new URL("file", "", "//" + (String)localObject1 + paramURL.getPath());
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/*  89 */     if (paramBoolean) {
/*  90 */       synchronized (instance) {
/*  91 */         localObject1 = getCachedJarFile(paramURL);
/*     */       }
/*  93 */       if (localObject1 == null) {
/*  94 */         JarFile localJarFile = URLJarFile.getJarFile(paramURL, this);
/*  95 */         synchronized (instance) {
/*  96 */           localObject1 = getCachedJarFile(paramURL);
/*  97 */           if (localObject1 == null) {
/*  98 */             fileCache.put(URLUtil.urlNoFragString(paramURL), localJarFile);
/*  99 */             urlCache.put(localJarFile, paramURL);
/* 100 */             localObject1 = localJarFile;
/*     */           }
/* 102 */           else if (localJarFile != null) {
/* 103 */             localJarFile.close();
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     else {
/* 109 */       localObject1 = URLJarFile.getJarFile(paramURL, this);
/*     */     }
/* 111 */     if (localObject1 == null) {
/* 112 */       throw new FileNotFoundException(paramURL.toString());
/*     */     }
/* 114 */     return localObject1;
/*     */   }
/*     */ 
/*     */   public void close(JarFile paramJarFile)
/*     */   {
/* 123 */     synchronized (instance) {
/* 124 */       URL localURL = (URL)urlCache.remove(paramJarFile);
/* 125 */       if (localURL != null)
/* 126 */         fileCache.remove(URLUtil.urlNoFragString(localURL));
/*     */     }
/*     */   }
/*     */ 
/*     */   private JarFile getCachedJarFile(URL paramURL) {
/* 131 */     assert (Thread.holdsLock(instance));
/* 132 */     JarFile localJarFile = (JarFile)fileCache.get(URLUtil.urlNoFragString(paramURL));
/*     */ 
/* 135 */     if (localJarFile != null) {
/* 136 */       Permission localPermission = getPermission(localJarFile);
/* 137 */       if (localPermission != null) {
/* 138 */         SecurityManager localSecurityManager = System.getSecurityManager();
/* 139 */         if (localSecurityManager != null) {
/*     */           try {
/* 141 */             localSecurityManager.checkPermission(localPermission);
/*     */           }
/*     */           catch (SecurityException localSecurityException)
/*     */           {
/* 145 */             if (((localPermission instanceof FilePermission)) && (localPermission.getActions().indexOf("read") != -1))
/*     */             {
/* 147 */               localSecurityManager.checkRead(localPermission.getName());
/* 148 */             } else if (((localPermission instanceof SocketPermission)) && (localPermission.getActions().indexOf("connect") != -1))
/*     */             {
/* 151 */               localSecurityManager.checkConnect(paramURL.getHost(), paramURL.getPort());
/*     */             }
/* 153 */             else throw localSecurityException;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 159 */     return localJarFile;
/*     */   }
/*     */ 
/*     */   private Permission getPermission(JarFile paramJarFile) {
/*     */     try {
/* 164 */       URLConnection localURLConnection = getConnection(paramJarFile);
/* 165 */       if (localURLConnection != null)
/* 166 */         return localURLConnection.getPermission();
/*     */     }
/*     */     catch (IOException localIOException)
/*     */     {
/*     */     }
/* 171 */     return null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.net.www.protocol.jar.JarFileFactory
 * JD-Core Version:    0.6.2
 */