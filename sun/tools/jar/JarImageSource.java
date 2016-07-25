/*    */ package sun.tools.jar;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.net.JarURLConnection;
/*    */ import java.net.URL;
/*    */ import java.util.jar.JarEntry;
/*    */ import java.util.jar.JarFile;
/*    */ import sun.awt.image.ImageDecoder;
/*    */ import sun.awt.image.URLImageSource;
/*    */ 
/*    */ public class JarImageSource extends URLImageSource
/*    */ {
/*    */   String mimeType;
/* 41 */   String entryName = null;
/*    */   URL url;
/*    */ 
/*    */   public JarImageSource(URL paramURL, String paramString)
/*    */   {
/* 49 */     super(paramURL);
/* 50 */     this.url = paramURL;
/* 51 */     this.mimeType = paramString;
/*    */   }
/*    */ 
/*    */   public JarImageSource(URL paramURL, String paramString1, String paramString2)
/*    */   {
/* 59 */     this(paramURL, paramString2);
/* 60 */     this.entryName = paramString1;
/*    */   }
/*    */ 
/*    */   protected ImageDecoder getDecoder() {
/* 64 */     InputStream localInputStream = null;
/*    */     try {
/* 66 */       JarURLConnection localJarURLConnection = (JarURLConnection)this.url.openConnection();
/* 67 */       JarFile localJarFile = localJarURLConnection.getJarFile();
/* 68 */       JarEntry localJarEntry = localJarURLConnection.getJarEntry();
/*    */ 
/* 70 */       if ((this.entryName != null) && (localJarEntry == null)) {
/* 71 */         localJarEntry = localJarFile.getJarEntry(this.entryName);
/*    */       }
/* 73 */       if ((localJarEntry == null) || ((localJarEntry != null) && (this.entryName != null) && (!this.entryName.equals(localJarEntry.getName()))))
/*    */       {
/* 75 */         return null;
/*    */       }
/* 77 */       localInputStream = localJarFile.getInputStream(localJarEntry);
/*    */     } catch (IOException localIOException) {
/* 79 */       return null;
/*    */     }
/*    */ 
/* 82 */     ImageDecoder localImageDecoder = decoderForType(localInputStream, this.mimeType);
/* 83 */     if (localImageDecoder == null) {
/* 84 */       localImageDecoder = getDecoder(localInputStream);
/*    */     }
/* 86 */     return localImageDecoder;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.tools.jar.JarImageSource
 * JD-Core Version:    0.6.2
 */