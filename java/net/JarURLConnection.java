/*     */ package java.net;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.security.cert.Certificate;
/*     */ import java.util.jar.Attributes;
/*     */ import java.util.jar.JarEntry;
/*     */ import java.util.jar.JarFile;
/*     */ import java.util.jar.Manifest;
/*     */ import sun.net.www.ParseUtil;
/*     */ 
/*     */ public abstract class JarURLConnection extends URLConnection
/*     */ {
/*     */   private URL jarFileURL;
/*     */   private String entryName;
/*     */   protected URLConnection jarFileURLConnection;
/*     */ 
/*     */   protected JarURLConnection(URL paramURL)
/*     */     throws MalformedURLException
/*     */   {
/* 161 */     super(paramURL);
/* 162 */     parseSpecs(paramURL);
/*     */   }
/*     */ 
/*     */   private void parseSpecs(URL paramURL)
/*     */     throws MalformedURLException
/*     */   {
/* 169 */     String str = paramURL.getFile();
/*     */ 
/* 171 */     int i = str.indexOf("!/");
/*     */ 
/* 175 */     if (i == -1) {
/* 176 */       throw new MalformedURLException("no !/ found in url spec:" + str);
/*     */     }
/*     */ 
/* 179 */     this.jarFileURL = new URL(str.substring(0, i++));
/* 180 */     this.entryName = null;
/*     */ 
/* 183 */     i++; if (i != str.length()) {
/* 184 */       this.entryName = str.substring(i, str.length());
/* 185 */       this.entryName = ParseUtil.decode(this.entryName);
/*     */     }
/*     */   }
/*     */ 
/*     */   public URL getJarFileURL()
/*     */   {
/* 195 */     return this.jarFileURL;
/*     */   }
/*     */ 
/*     */   public String getEntryName()
/*     */   {
/* 206 */     return this.entryName;
/*     */   }
/*     */ 
/*     */   public abstract JarFile getJarFile()
/*     */     throws IOException;
/*     */ 
/*     */   public Manifest getManifest()
/*     */     throws IOException
/*     */   {
/* 235 */     return getJarFile().getManifest();
/*     */   }
/*     */ 
/*     */   public JarEntry getJarEntry()
/*     */     throws IOException
/*     */   {
/* 253 */     return getJarFile().getJarEntry(this.entryName);
/*     */   }
/*     */ 
/*     */   public Attributes getAttributes()
/*     */     throws IOException
/*     */   {
/* 269 */     JarEntry localJarEntry = getJarEntry();
/* 270 */     return localJarEntry != null ? localJarEntry.getAttributes() : null;
/*     */   }
/*     */ 
/*     */   public Attributes getMainAttributes()
/*     */     throws IOException
/*     */   {
/* 287 */     Manifest localManifest = getManifest();
/* 288 */     return localManifest != null ? localManifest.getMainAttributes() : null;
/*     */   }
/*     */ 
/*     */   public Certificate[] getCertificates()
/*     */     throws IOException
/*     */   {
/* 310 */     JarEntry localJarEntry = getJarEntry();
/* 311 */     return localJarEntry != null ? localJarEntry.getCertificates() : null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.net.JarURLConnection
 * JD-Core Version:    0.6.2
 */