/*     */ package sun.security.util;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.util.StringTokenizer;
/*     */ 
/*     */ public class PathList
/*     */ {
/*     */   public static String appendPath(String paramString1, String paramString2)
/*     */   {
/*  49 */     if ((paramString1 == null) || (paramString1.length() == 0))
/*  50 */       return paramString2;
/*  51 */     if ((paramString2 == null) || (paramString2.length() == 0)) {
/*  52 */       return paramString1;
/*     */     }
/*  54 */     return paramString1 + File.pathSeparator + paramString2;
/*     */   }
/*     */ 
/*     */   public static URL[] pathToURLs(String paramString)
/*     */   {
/*  66 */     StringTokenizer localStringTokenizer = new StringTokenizer(paramString, File.pathSeparator);
/*  67 */     Object localObject1 = new URL[localStringTokenizer.countTokens()];
/*  68 */     int i = 0;
/*     */     Object localObject2;
/*  69 */     while (localStringTokenizer.hasMoreTokens()) {
/*  70 */       localObject2 = fileToURL(new File(localStringTokenizer.nextToken()));
/*  71 */       if (localObject2 != null) {
/*  72 */         localObject1[(i++)] = localObject2;
/*     */       }
/*     */     }
/*  75 */     if (localObject1.length != i) {
/*  76 */       localObject2 = new URL[i];
/*  77 */       System.arraycopy(localObject1, 0, localObject2, 0, i);
/*  78 */       localObject1 = localObject2;
/*     */     }
/*  80 */     return localObject1;
/*     */   }
/*     */ 
/*     */   private static URL fileToURL(File paramFile)
/*     */   {
/*     */     try
/*     */     {
/*  93 */       str = paramFile.getCanonicalPath();
/*     */     } catch (IOException localIOException) {
/*  95 */       str = paramFile.getAbsolutePath();
/*     */     }
/*  97 */     String str = str.replace(File.separatorChar, '/');
/*  98 */     if (!str.startsWith("/")) {
/*  99 */       str = "/" + str;
/*     */     }
/*     */ 
/* 102 */     if (!paramFile.isFile())
/* 103 */       str = str + "/";
/*     */     try
/*     */     {
/* 106 */       return new URL("file", "", str); } catch (MalformedURLException localMalformedURLException) {
/*     */     }
/* 108 */     throw new IllegalArgumentException("file");
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.util.PathList
 * JD-Core Version:    0.6.2
 */