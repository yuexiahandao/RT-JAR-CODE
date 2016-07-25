/*     */ package sun.awt.image;
/*     */ 
/*     */ import java.io.FilePermission;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.HttpURLConnection;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.SocketPermission;
/*     */ import java.net.URL;
/*     */ import java.net.URLConnection;
/*     */ import java.security.Permission;
/*     */ 
/*     */ public class URLImageSource extends InputStreamImageSource
/*     */ {
/*     */   URL url;
/*     */   URLConnection conn;
/*     */   String actualHost;
/*     */   int actualPort;
/*     */ 
/*     */   public URLImageSource(URL paramURL)
/*     */   {
/*  42 */     SecurityManager localSecurityManager = System.getSecurityManager();
/*  43 */     if (localSecurityManager != null) {
/*     */       try {
/*  45 */         Permission localPermission = paramURL.openConnection().getPermission();
/*     */ 
/*  47 */         if (localPermission != null)
/*     */           try {
/*  49 */             localSecurityManager.checkPermission(localPermission);
/*     */           }
/*     */           catch (SecurityException localSecurityException)
/*     */           {
/*  53 */             if (((localPermission instanceof FilePermission)) && (localPermission.getActions().indexOf("read") != -1))
/*     */             {
/*  55 */               localSecurityManager.checkRead(localPermission.getName());
/*  56 */             } else if (((localPermission instanceof SocketPermission)) && (localPermission.getActions().indexOf("connect") != -1))
/*     */             {
/*  59 */               localSecurityManager.checkConnect(paramURL.getHost(), paramURL.getPort());
/*     */             }
/*  61 */             else throw localSecurityException;
/*     */           }
/*     */       }
/*     */       catch (IOException localIOException)
/*     */       {
/*  66 */         localSecurityManager.checkConnect(paramURL.getHost(), paramURL.getPort());
/*     */       }
/*     */     }
/*  69 */     this.url = paramURL;
/*     */   }
/*     */ 
/*     */   public URLImageSource(String paramString) throws MalformedURLException
/*     */   {
/*  74 */     this(new URL(null, paramString));
/*     */   }
/*     */ 
/*     */   public URLImageSource(URL paramURL, URLConnection paramURLConnection) {
/*  78 */     this(paramURL);
/*  79 */     this.conn = paramURLConnection;
/*     */   }
/*     */ 
/*     */   public URLImageSource(URLConnection paramURLConnection) {
/*  83 */     this(paramURLConnection.getURL(), paramURLConnection);
/*     */   }
/*     */ 
/*     */   final boolean checkSecurity(Object paramObject, boolean paramBoolean)
/*     */   {
/*  93 */     if (this.actualHost != null) {
/*     */       try {
/*  95 */         SecurityManager localSecurityManager = System.getSecurityManager();
/*  96 */         if (localSecurityManager != null)
/*  97 */           localSecurityManager.checkConnect(this.actualHost, this.actualPort, paramObject);
/*     */       }
/*     */       catch (SecurityException localSecurityException) {
/* 100 */         if (!paramBoolean) {
/* 101 */           throw localSecurityException;
/*     */         }
/* 103 */         return false;
/*     */       }
/*     */     }
/* 106 */     return true;
/*     */   }
/*     */ 
/*     */   private synchronized URLConnection getConnection()
/*     */     throws IOException
/*     */   {
/*     */     URLConnection localURLConnection;
/* 111 */     if (this.conn != null) {
/* 112 */       localURLConnection = this.conn;
/* 113 */       this.conn = null;
/*     */     } else {
/* 115 */       localURLConnection = this.url.openConnection();
/*     */     }
/* 117 */     return localURLConnection;
/*     */   }
/*     */ 
/*     */   protected ImageDecoder getDecoder() {
/* 121 */     InputStream localInputStream = null;
/* 122 */     String str = null;
/* 123 */     URLConnection localURLConnection = null;
/*     */     try {
/* 125 */       localURLConnection = getConnection();
/* 126 */       localInputStream = localURLConnection.getInputStream();
/* 127 */       str = localURLConnection.getContentType();
/* 128 */       URL localURL = localURLConnection.getURL();
/* 129 */       if ((localURL != this.url) && ((!localURL.getHost().equals(this.url.getHost())) || (localURL.getPort() != this.url.getPort())))
/*     */       {
/* 136 */         if ((this.actualHost != null) && ((!this.actualHost.equals(localURL.getHost())) || (this.actualPort != localURL.getPort())))
/*     */         {
/* 139 */           throw new SecurityException("image moved!");
/*     */         }
/* 141 */         this.actualHost = localURL.getHost();
/* 142 */         this.actualPort = localURL.getPort();
/*     */       }
/*     */     } catch (IOException localIOException1) {
/* 145 */       if (localInputStream != null)
/*     */         try {
/* 147 */           localInputStream.close();
/*     */         } catch (IOException localIOException2) {
/*     */         }
/* 150 */       else if ((localURLConnection instanceof HttpURLConnection)) {
/* 151 */         ((HttpURLConnection)localURLConnection).disconnect();
/*     */       }
/* 153 */       return null;
/*     */     }
/*     */ 
/* 156 */     ImageDecoder localImageDecoder = decoderForType(localInputStream, str);
/* 157 */     if (localImageDecoder == null) {
/* 158 */       localImageDecoder = getDecoder(localInputStream);
/*     */     }
/*     */ 
/* 161 */     if (localImageDecoder == null)
/*     */     {
/* 163 */       if (localInputStream != null)
/*     */         try {
/* 165 */           localInputStream.close();
/*     */         } catch (IOException localIOException3) {
/*     */         }
/* 168 */       else if ((localURLConnection instanceof HttpURLConnection)) {
/* 169 */         ((HttpURLConnection)localURLConnection).disconnect();
/*     */       }
/*     */     }
/* 172 */     return localImageDecoder;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.image.URLImageSource
 * JD-Core Version:    0.6.2
 */