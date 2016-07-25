/*     */ package sun.net.www.protocol.jar;
/*     */ 
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.FilterInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.net.URLConnection;
/*     */ import java.security.Permission;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.jar.JarEntry;
/*     */ import java.util.jar.JarFile;
/*     */ 
/*     */ public class JarURLConnection extends java.net.JarURLConnection
/*     */ {
/*     */   private static final boolean debug = false;
/*  54 */   private static final JarFileFactory factory = JarFileFactory.getInstance();
/*     */   private URL jarFileURL;
/*     */   private Permission permission;
/*     */   private URLConnection jarFileURLConnection;
/*     */   private String entryName;
/*     */   private JarEntry jarEntry;
/*     */   private JarFile jarFile;
/*     */   private String contentType;
/*     */ 
/*     */   public JarURLConnection(URL paramURL, Handler paramHandler)
/*     */     throws MalformedURLException, IOException
/*     */   {
/*  81 */     super(paramURL);
/*     */ 
/*  83 */     this.jarFileURL = getJarFileURL();
/*  84 */     this.jarFileURLConnection = this.jarFileURL.openConnection();
/*  85 */     this.entryName = getEntryName();
/*     */   }
/*     */ 
/*     */   public JarFile getJarFile() throws IOException {
/*  89 */     connect();
/*  90 */     return this.jarFile;
/*     */   }
/*     */ 
/*     */   public JarEntry getJarEntry() throws IOException {
/*  94 */     connect();
/*  95 */     return this.jarEntry;
/*     */   }
/*     */ 
/*     */   public Permission getPermission() throws IOException {
/*  99 */     return this.jarFileURLConnection.getPermission();
/*     */   }
/*     */ 
/*     */   public void connect()
/*     */     throws IOException
/*     */   {
/* 120 */     if (!this.connected)
/*     */     {
/* 122 */       this.jarFile = factory.get(getJarFileURL(), getUseCaches());
/*     */ 
/* 127 */       if (getUseCaches()) {
/* 128 */         this.jarFileURLConnection = factory.getConnection(this.jarFile);
/*     */       }
/*     */ 
/* 131 */       if (this.entryName != null) {
/* 132 */         this.jarEntry = ((JarEntry)this.jarFile.getEntry(this.entryName));
/* 133 */         if (this.jarEntry == null) {
/*     */           try {
/* 135 */             if (!getUseCaches())
/* 136 */               this.jarFile.close();
/*     */           }
/*     */           catch (Exception localException) {
/*     */           }
/* 140 */           throw new FileNotFoundException("JAR entry " + this.entryName + " not found in " + this.jarFile.getName());
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 145 */       this.connected = true;
/*     */     }
/*     */   }
/*     */ 
/*     */   public InputStream getInputStream() throws IOException {
/* 150 */     connect();
/*     */ 
/* 152 */     JarURLInputStream localJarURLInputStream = null;
/*     */ 
/* 154 */     if (this.entryName == null) {
/* 155 */       throw new IOException("no entry name specified");
/*     */     }
/* 157 */     if (this.jarEntry == null) {
/* 158 */       throw new FileNotFoundException("JAR entry " + this.entryName + " not found in " + this.jarFile.getName());
/*     */     }
/*     */ 
/* 162 */     localJarURLInputStream = new JarURLInputStream(this.jarFile.getInputStream(this.jarEntry));
/*     */ 
/* 164 */     return localJarURLInputStream;
/*     */   }
/*     */ 
/*     */   public int getContentLength() {
/* 168 */     long l = getContentLengthLong();
/* 169 */     if (l > 2147483647L)
/* 170 */       return -1;
/* 171 */     return (int)l;
/*     */   }
/*     */ 
/*     */   public long getContentLengthLong() {
/* 175 */     long l = -1L;
/*     */     try {
/* 177 */       connect();
/* 178 */       if (this.jarEntry == null)
/*     */       {
/* 180 */         l = this.jarFileURLConnection.getContentLengthLong();
/*     */       }
/*     */       else
/* 183 */         l = getJarEntry().getSize();
/*     */     }
/*     */     catch (IOException localIOException) {
/*     */     }
/* 187 */     return l;
/*     */   }
/*     */ 
/*     */   public Object getContent() throws IOException {
/* 191 */     Object localObject = null;
/*     */ 
/* 193 */     connect();
/* 194 */     if (this.entryName == null)
/* 195 */       localObject = this.jarFile;
/*     */     else {
/* 197 */       localObject = super.getContent();
/*     */     }
/* 199 */     return localObject;
/*     */   }
/*     */ 
/*     */   public String getContentType() {
/* 203 */     if (this.contentType == null) {
/* 204 */       if (this.entryName == null)
/* 205 */         this.contentType = "x-java/jar";
/*     */       else
/*     */         try {
/* 208 */           connect();
/* 209 */           InputStream localInputStream = this.jarFile.getInputStream(this.jarEntry);
/* 210 */           this.contentType = guessContentTypeFromStream(new BufferedInputStream(localInputStream));
/*     */ 
/* 212 */           localInputStream.close();
/*     */         }
/*     */         catch (IOException localIOException)
/*     */         {
/*     */         }
/* 217 */       if (this.contentType == null) {
/* 218 */         this.contentType = guessContentTypeFromName(this.entryName);
/*     */       }
/* 220 */       if (this.contentType == null) {
/* 221 */         this.contentType = "content/unknown";
/*     */       }
/*     */     }
/* 224 */     return this.contentType;
/*     */   }
/*     */ 
/*     */   public String getHeaderField(String paramString) {
/* 228 */     return this.jarFileURLConnection.getHeaderField(paramString);
/*     */   }
/*     */ 
/*     */   public void setRequestProperty(String paramString1, String paramString2)
/*     */   {
/* 239 */     this.jarFileURLConnection.setRequestProperty(paramString1, paramString2);
/*     */   }
/*     */ 
/*     */   public String getRequestProperty(String paramString)
/*     */   {
/* 250 */     return this.jarFileURLConnection.getRequestProperty(paramString);
/*     */   }
/*     */ 
/*     */   public void addRequestProperty(String paramString1, String paramString2)
/*     */   {
/* 263 */     this.jarFileURLConnection.addRequestProperty(paramString1, paramString2);
/*     */   }
/*     */ 
/*     */   public Map<String, List<String>> getRequestProperties()
/*     */   {
/* 277 */     return this.jarFileURLConnection.getRequestProperties();
/*     */   }
/*     */ 
/*     */   public void setAllowUserInteraction(boolean paramBoolean)
/*     */   {
/* 288 */     this.jarFileURLConnection.setAllowUserInteraction(paramBoolean);
/*     */   }
/*     */ 
/*     */   public boolean getAllowUserInteraction()
/*     */   {
/* 300 */     return this.jarFileURLConnection.getAllowUserInteraction();
/*     */   }
/*     */ 
/*     */   public void setUseCaches(boolean paramBoolean)
/*     */   {
/* 322 */     this.jarFileURLConnection.setUseCaches(paramBoolean);
/*     */   }
/*     */ 
/*     */   public boolean getUseCaches()
/*     */   {
/* 334 */     return this.jarFileURLConnection.getUseCaches();
/*     */   }
/*     */ 
/*     */   public void setIfModifiedSince(long paramLong)
/*     */   {
/* 345 */     this.jarFileURLConnection.setIfModifiedSince(paramLong);
/*     */   }
/*     */ 
/*     */   public void setDefaultUseCaches(boolean paramBoolean)
/*     */   {
/* 356 */     this.jarFileURLConnection.setDefaultUseCaches(paramBoolean);
/*     */   }
/*     */ 
/*     */   public boolean getDefaultUseCaches()
/*     */   {
/* 372 */     return this.jarFileURLConnection.getDefaultUseCaches();
/*     */   }
/*     */ 
/*     */   class JarURLInputStream extends FilterInputStream
/*     */   {
/*     */     JarURLInputStream(InputStream arg2)
/*     */     {
/* 104 */       super();
/*     */     }
/*     */     public void close() throws IOException {
/*     */       try {
/* 108 */         super.close();
/*     */       } finally {
/* 110 */         if (!JarURLConnection.this.getUseCaches())
/* 111 */           JarURLConnection.this.jarFile.close();
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.net.www.protocol.jar.JarURLConnection
 * JD-Core Version:    0.6.2
 */