/*     */ package sun.nio.fs;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.spi.FileTypeDetector;
/*     */ import java.util.Locale;
/*     */ 
/*     */ public abstract class AbstractFileTypeDetector extends FileTypeDetector
/*     */ {
/*     */   private static final String TSPECIALS = "()<>@,;:/[]?=\\\"";
/*     */ 
/*     */   public final String probeContentType(Path paramPath)
/*     */     throws IOException
/*     */   {
/*  50 */     if (paramPath == null)
/*  51 */       throw new NullPointerException("'file' is null");
/*  52 */     String str = implProbeContentType(paramPath);
/*  53 */     return str == null ? null : parse(str);
/*     */   }
/*     */ 
/*     */   protected abstract String implProbeContentType(Path paramPath)
/*     */     throws IOException;
/*     */ 
/*     */   private static String parse(String paramString)
/*     */   {
/*  67 */     int i = paramString.indexOf('/');
/*  68 */     int j = paramString.indexOf(';');
/*  69 */     if (i < 0)
/*  70 */       return null;
/*  71 */     String str1 = paramString.substring(0, i).trim().toLowerCase(Locale.ENGLISH);
/*  72 */     if (!isValidToken(str1))
/*  73 */       return null;
/*  74 */     String str2 = j < 0 ? paramString.substring(i + 1) : paramString.substring(i + 1, j);
/*     */ 
/*  76 */     str2 = str2.trim().toLowerCase(Locale.ENGLISH);
/*  77 */     if (!isValidToken(str2))
/*  78 */       return null;
/*  79 */     StringBuilder localStringBuilder = new StringBuilder(str1.length() + str2.length() + 1);
/*  80 */     localStringBuilder.append(str1);
/*  81 */     localStringBuilder.append('/');
/*  82 */     localStringBuilder.append(str2);
/*  83 */     return localStringBuilder.toString();
/*     */   }
/*     */ 
/*     */   private static boolean isTokenChar(char paramChar)
/*     */   {
/*  95 */     return (paramChar > ' ') && (paramChar < '') && ("()<>@,;:/[]?=\\\"".indexOf(paramChar) < 0);
/*     */   }
/*     */ 
/*     */   private static boolean isValidToken(String paramString)
/*     */   {
/* 102 */     int i = paramString.length();
/* 103 */     if (i == 0)
/* 104 */       return false;
/* 105 */     for (int j = 0; j < i; j++) {
/* 106 */       if (!isTokenChar(paramString.charAt(j)))
/* 107 */         return false;
/*     */     }
/* 109 */     return true;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.nio.fs.AbstractFileTypeDetector
 * JD-Core Version:    0.6.2
 */