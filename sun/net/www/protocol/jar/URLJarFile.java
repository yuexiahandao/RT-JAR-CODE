/*     */ package sun.net.www.protocol.jar;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.URL;
/*     */ import java.net.URLConnection;
/*     */ import java.nio.file.CopyOption;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.StandardCopyOption;
/*     */ import java.nio.file.attribute.FileAttribute;
/*     */ import java.security.AccessController;
/*     */ import java.security.CodeSigner;
/*     */ import java.security.PrivilegedActionException;
/*     */ import java.security.PrivilegedExceptionAction;
/*     */ import java.security.cert.Certificate;
/*     */ import java.util.Map;
/*     */ import java.util.jar.Attributes;
/*     */ import java.util.jar.JarEntry;
/*     */ import java.util.jar.JarFile;
/*     */ import java.util.jar.Manifest;
/*     */ import java.util.zip.ZipEntry;
/*     */ import sun.net.www.ParseUtil;
/*     */ 
/*     */ public class URLJarFile extends JarFile
/*     */ {
/*  52 */   private static URLJarFileCallBack callback = null;
/*     */ 
/*  55 */   private URLJarFileCloseController closeController = null;
/*     */ 
/*  57 */   private static int BUF_SIZE = 2048;
/*     */   private Manifest superMan;
/*     */   private Attributes superAttr;
/*     */   private Map<String, Attributes> superEntries;
/*     */ 
/*     */   static JarFile getJarFile(URL paramURL)
/*     */     throws IOException
/*     */   {
/*  64 */     return getJarFile(paramURL, null);
/*     */   }
/*     */ 
/*     */   static JarFile getJarFile(URL paramURL, URLJarFileCloseController paramURLJarFileCloseController) throws IOException {
/*  68 */     if (isFileURL(paramURL)) {
/*  69 */       return new URLJarFile(paramURL, paramURLJarFileCloseController);
/*     */     }
/*  71 */     return retrieve(paramURL, paramURLJarFileCloseController);
/*     */   }
/*     */ 
/*     */   public URLJarFile(File paramFile)
/*     */     throws IOException
/*     */   {
/*  80 */     this(paramFile, null);
/*     */   }
/*     */ 
/*     */   public URLJarFile(File paramFile, URLJarFileCloseController paramURLJarFileCloseController)
/*     */     throws IOException
/*     */   {
/*  88 */     super(paramFile, true, 5);
/*  89 */     this.closeController = paramURLJarFileCloseController;
/*     */   }
/*     */ 
/*     */   private URLJarFile(URL paramURL, URLJarFileCloseController paramURLJarFileCloseController) throws IOException {
/*  93 */     super(ParseUtil.decode(paramURL.getFile()));
/*  94 */     this.closeController = paramURLJarFileCloseController;
/*     */   }
/*     */ 
/*     */   private static boolean isFileURL(URL paramURL) {
/*  98 */     if (paramURL.getProtocol().equalsIgnoreCase("file"))
/*     */     {
/* 103 */       String str = paramURL.getHost();
/* 104 */       if ((str == null) || (str.equals("")) || (str.equals("~")) || (str.equalsIgnoreCase("localhost")))
/*     */       {
/* 106 */         return true;
/*     */       }
/*     */     }
/* 108 */     return false;
/*     */   }
/*     */ 
/*     */   protected void finalize()
/*     */     throws IOException
/*     */   {
/* 115 */     close();
/*     */   }
/*     */ 
/*     */   public ZipEntry getEntry(String paramString)
/*     */   {
/* 128 */     ZipEntry localZipEntry = super.getEntry(paramString);
/* 129 */     if (localZipEntry != null) {
/* 130 */       if ((localZipEntry instanceof JarEntry)) {
/* 131 */         return new URLJarFileEntry((JarEntry)localZipEntry);
/*     */       }
/* 133 */       throw new InternalError(super.getClass() + " returned unexpected entry type " + localZipEntry.getClass());
/*     */     }
/*     */ 
/* 137 */     return null;
/*     */   }
/*     */ 
/*     */   public Manifest getManifest() throws IOException
/*     */   {
/* 142 */     if (!isSuperMan()) {
/* 143 */       return null;
/*     */     }
/*     */ 
/* 146 */     Manifest localManifest = new Manifest();
/* 147 */     Attributes localAttributes1 = localManifest.getMainAttributes();
/* 148 */     localAttributes1.putAll((Map)this.superAttr.clone());
/*     */     Map localMap;
/* 151 */     if (this.superEntries != null) {
/* 152 */       localMap = localManifest.getEntries();
/* 153 */       for (String str : this.superEntries.keySet()) {
/* 154 */         Attributes localAttributes2 = (Attributes)this.superEntries.get(str);
/* 155 */         localMap.put(str, (Attributes)localAttributes2.clone());
/*     */       }
/*     */     }
/*     */ 
/* 159 */     return localManifest;
/*     */   }
/*     */ 
/*     */   public void close() throws IOException
/*     */   {
/* 164 */     if (this.closeController != null) {
/* 165 */       this.closeController.close(this);
/*     */     }
/* 167 */     super.close();
/*     */   }
/*     */ 
/*     */   private synchronized boolean isSuperMan()
/*     */     throws IOException
/*     */   {
/* 173 */     if (this.superMan == null) {
/* 174 */       this.superMan = super.getManifest();
/*     */     }
/*     */ 
/* 177 */     if (this.superMan != null) {
/* 178 */       this.superAttr = this.superMan.getMainAttributes();
/* 179 */       this.superEntries = this.superMan.getEntries();
/* 180 */       return true;
/*     */     }
/* 182 */     return false;
/*     */   }
/*     */ 
/*     */   private static JarFile retrieve(URL paramURL)
/*     */     throws IOException
/*     */   {
/* 190 */     return retrieve(paramURL, null);
/*     */   }
/*     */ 
/*     */   private static JarFile retrieve(URL paramURL, final URLJarFileCloseController paramURLJarFileCloseController)
/*     */     throws IOException
/*     */   {
/* 203 */     if (callback != null)
/*     */     {
/* 205 */       return callback.retrieve(paramURL);
/*     */     }
/*     */ 
/* 211 */     JarFile localJarFile = null;
/*     */     try
/*     */     {
/* 214 */       InputStream localInputStream = paramURL.openConnection().getInputStream(); Object localObject1 = null;
/*     */       try { localJarFile = (JarFile)AccessController.doPrivileged(new PrivilegedExceptionAction()
/*     */         {
/*     */           public JarFile run() throws IOException {
/* 218 */             Path localPath = Files.createTempFile("jar_cache", null, new FileAttribute[0]);
/*     */             try {
/* 220 */               Files.copy(this.val$in, localPath, new CopyOption[] { StandardCopyOption.REPLACE_EXISTING });
/* 221 */               URLJarFile localURLJarFile = new URLJarFile(localPath.toFile(), paramURLJarFileCloseController);
/* 222 */               localPath.toFile().deleteOnExit();
/* 223 */               return localURLJarFile;
/*     */             } catch (Throwable localThrowable) {
/*     */               try {
/* 226 */                 Files.delete(localPath);
/*     */               } catch (IOException localIOException) {
/* 228 */                 localThrowable.addSuppressed(localIOException);
/*     */               }
/* 230 */               throw localThrowable;
/*     */             }
/*     */           }
/*     */         });
/*     */       }
/*     */       catch (Throwable localThrowable2)
/*     */       {
/* 214 */         localObject1 = localThrowable2; throw localThrowable2;
/*     */       }
/*     */       finally
/*     */       {
/* 234 */         if (localInputStream != null) if (localObject1 != null) try { localInputStream.close(); } catch (Throwable localThrowable3) { localObject1.addSuppressed(localThrowable3); } else localInputStream.close();  
/*     */       } } catch (PrivilegedActionException localPrivilegedActionException) { throw ((IOException)localPrivilegedActionException.getException()); }
/*     */ 
/*     */ 
/* 238 */     return localJarFile;
/*     */   }
/*     */ 
/*     */   public static void setCallBack(URLJarFileCallBack paramURLJarFileCallBack)
/*     */   {
/* 248 */     callback = paramURLJarFileCallBack;
/*     */   }
/*     */   public static abstract interface URLJarFileCloseController {
/*     */     public abstract void close(JarFile paramJarFile);
/*     */   }
/*     */   private class URLJarFileEntry extends JarEntry {
/*     */     private JarEntry je;
/*     */ 
/* 256 */     URLJarFileEntry(JarEntry arg2) { super();
/* 257 */       this.je = localJarEntry; }
/*     */ 
/*     */     public Attributes getAttributes() throws IOException
/*     */     {
/* 261 */       if (URLJarFile.this.isSuperMan()) {
/* 262 */         Map localMap = URLJarFile.this.superEntries;
/* 263 */         if (localMap != null) {
/* 264 */           Attributes localAttributes = (Attributes)localMap.get(getName());
/* 265 */           if (localAttributes != null)
/* 266 */             return (Attributes)localAttributes.clone();
/*     */         }
/*     */       }
/* 269 */       return null;
/*     */     }
/*     */ 
/*     */     public Certificate[] getCertificates() {
/* 273 */       Certificate[] arrayOfCertificate = this.je.getCertificates();
/* 274 */       return arrayOfCertificate == null ? null : (Certificate[])arrayOfCertificate.clone();
/*     */     }
/*     */ 
/*     */     public CodeSigner[] getCodeSigners() {
/* 278 */       CodeSigner[] arrayOfCodeSigner = this.je.getCodeSigners();
/* 279 */       return arrayOfCodeSigner == null ? null : (CodeSigner[])arrayOfCodeSigner.clone();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.net.www.protocol.jar.URLJarFile
 * JD-Core Version:    0.6.2
 */