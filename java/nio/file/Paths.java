/*     */ package java.nio.file;
/*     */ 
/*     */ import java.net.URI;
/*     */ import java.nio.file.spi.FileSystemProvider;
/*     */ 
/*     */ public final class Paths
/*     */ {
/*     */   public static Path get(String paramString, String[] paramArrayOfString)
/*     */   {
/*  84 */     return FileSystems.getDefault().getPath(paramString, paramArrayOfString);
/*     */   }
/*     */ 
/*     */   public static Path get(URI paramURI)
/*     */   {
/* 132 */     String str = paramURI.getScheme();
/* 133 */     if (str == null) {
/* 134 */       throw new IllegalArgumentException("Missing scheme");
/*     */     }
/*     */ 
/* 137 */     if (str.equalsIgnoreCase("file")) {
/* 138 */       return FileSystems.getDefault().provider().getPath(paramURI);
/*     */     }
/*     */ 
/* 141 */     for (FileSystemProvider localFileSystemProvider : FileSystemProvider.installedProviders()) {
/* 142 */       if (localFileSystemProvider.getScheme().equalsIgnoreCase(str)) {
/* 143 */         return localFileSystemProvider.getPath(paramURI);
/*     */       }
/*     */     }
/*     */ 
/* 147 */     throw new FileSystemNotFoundException("Provider \"" + str + "\" not installed");
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.nio.file.Paths
 * JD-Core Version:    0.6.2
 */