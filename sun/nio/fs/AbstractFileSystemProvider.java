/*     */ package sun.nio.fs;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.file.LinkOption;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.spi.FileSystemProvider;
/*     */ import java.util.Map;
/*     */ 
/*     */ abstract class AbstractFileSystemProvider extends FileSystemProvider
/*     */ {
/*     */   private static String[] split(String paramString)
/*     */   {
/*  46 */     String[] arrayOfString = new String[2];
/*  47 */     int i = paramString.indexOf(':');
/*  48 */     if (i == -1) {
/*  49 */       arrayOfString[0] = "basic";
/*  50 */       arrayOfString[1] = paramString;
/*     */     } else {
/*  52 */       arrayOfString[0] = paramString.substring(0, i++);
/*  53 */       arrayOfString[1] = (i == paramString.length() ? "" : paramString.substring(i));
/*     */     }
/*  55 */     return arrayOfString;
/*     */   }
/*     */ 
/*     */   abstract DynamicFileAttributeView getFileAttributeView(Path paramPath, String paramString, LinkOption[] paramArrayOfLinkOption);
/*     */ 
/*     */   public final void setAttribute(Path paramPath, String paramString, Object paramObject, LinkOption[] paramArrayOfLinkOption)
/*     */     throws IOException
/*     */   {
/*  73 */     String[] arrayOfString = split(paramString);
/*  74 */     if (arrayOfString[0].length() == 0)
/*  75 */       throw new IllegalArgumentException(paramString);
/*  76 */     DynamicFileAttributeView localDynamicFileAttributeView = getFileAttributeView(paramPath, arrayOfString[0], paramArrayOfLinkOption);
/*  77 */     if (localDynamicFileAttributeView == null)
/*  78 */       throw new UnsupportedOperationException("View '" + arrayOfString[0] + "' not available");
/*  79 */     localDynamicFileAttributeView.setAttribute(arrayOfString[1], paramObject);
/*     */   }
/*     */ 
/*     */   public final Map<String, Object> readAttributes(Path paramPath, String paramString, LinkOption[] paramArrayOfLinkOption)
/*     */     throws IOException
/*     */   {
/*  86 */     String[] arrayOfString = split(paramString);
/*  87 */     if (arrayOfString[0].length() == 0)
/*  88 */       throw new IllegalArgumentException(paramString);
/*  89 */     DynamicFileAttributeView localDynamicFileAttributeView = getFileAttributeView(paramPath, arrayOfString[0], paramArrayOfLinkOption);
/*  90 */     if (localDynamicFileAttributeView == null)
/*  91 */       throw new UnsupportedOperationException("View '" + arrayOfString[0] + "' not available");
/*  92 */     return localDynamicFileAttributeView.readAttributes(arrayOfString[1].split(","));
/*     */   }
/*     */ 
/*     */   abstract boolean implDelete(Path paramPath, boolean paramBoolean)
/*     */     throws IOException;
/*     */ 
/*     */   public final void delete(Path paramPath)
/*     */     throws IOException
/*     */   {
/* 103 */     implDelete(paramPath, true);
/*     */   }
/*     */ 
/*     */   public final boolean deleteIfExists(Path paramPath) throws IOException
/*     */   {
/* 108 */     return implDelete(paramPath, false);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.nio.fs.AbstractFileSystemProvider
 * JD-Core Version:    0.6.2
 */