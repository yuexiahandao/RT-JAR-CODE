/*     */ package javax.activation;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.util.Map;
/*     */ import java.util.WeakHashMap;
/*     */ 
/*     */ public abstract class FileTypeMap
/*     */ {
/*  52 */   private static FileTypeMap defaultMap = null;
/*  53 */   private static Map<ClassLoader, FileTypeMap> map = new WeakHashMap();
/*     */ 
/*     */   public abstract String getContentType(File paramFile);
/*     */ 
/*     */   public abstract String getContentType(String paramString);
/*     */ 
/*     */   public static synchronized void setDefaultFileTypeMap(FileTypeMap fileTypeMap)
/*     */   {
/*  90 */     SecurityManager security = System.getSecurityManager();
/*  91 */     if (security != null) {
/*     */       try
/*     */       {
/*  94 */         security.checkSetFactory();
/*     */       }
/*     */       catch (SecurityException ex)
/*     */       {
/*  99 */         if ((FileTypeMap.class.getClassLoader() == null) || (FileTypeMap.class.getClassLoader() != fileTypeMap.getClass().getClassLoader()))
/*     */         {
/* 102 */           throw ex;
/*     */         }
/*     */       }
/*     */     }
/* 106 */     map.remove(SecuritySupport.getContextClassLoader());
/* 107 */     defaultMap = fileTypeMap;
/*     */   }
/*     */ 
/*     */   public static synchronized FileTypeMap getDefaultFileTypeMap()
/*     */   {
/* 120 */     if (defaultMap != null) {
/* 121 */       return defaultMap;
/*     */     }
/*     */ 
/* 124 */     ClassLoader tccl = SecuritySupport.getContextClassLoader();
/* 125 */     FileTypeMap def = (FileTypeMap)map.get(tccl);
/* 126 */     if (def == null) {
/* 127 */       def = new MimetypesFileTypeMap();
/* 128 */       map.put(tccl, def);
/*     */     }
/* 130 */     return def;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.activation.FileTypeMap
 * JD-Core Version:    0.6.2
 */