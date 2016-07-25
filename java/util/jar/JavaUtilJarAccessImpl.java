/*    */ package java.util.jar;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.net.URL;
/*    */ import java.security.CodeSource;
/*    */ import java.util.Enumeration;
/*    */ import java.util.List;
/*    */ import sun.misc.JavaUtilJarAccess;
/*    */ 
/*    */ class JavaUtilJarAccessImpl
/*    */   implements JavaUtilJarAccess
/*    */ {
/*    */   public boolean jarFileHasClassPathAttribute(JarFile paramJarFile)
/*    */     throws IOException
/*    */   {
/* 37 */     return paramJarFile.hasClassPathAttribute();
/*    */   }
/*    */ 
/*    */   public CodeSource[] getCodeSources(JarFile paramJarFile, URL paramURL) {
/* 41 */     return paramJarFile.getCodeSources(paramURL);
/*    */   }
/*    */ 
/*    */   public CodeSource getCodeSource(JarFile paramJarFile, URL paramURL, String paramString) {
/* 45 */     return paramJarFile.getCodeSource(paramURL, paramString);
/*    */   }
/*    */ 
/*    */   public Enumeration<String> entryNames(JarFile paramJarFile, CodeSource[] paramArrayOfCodeSource) {
/* 49 */     return paramJarFile.entryNames(paramArrayOfCodeSource);
/*    */   }
/*    */ 
/*    */   public Enumeration<JarEntry> entries2(JarFile paramJarFile) {
/* 53 */     return paramJarFile.entries2();
/*    */   }
/*    */ 
/*    */   public void setEagerValidation(JarFile paramJarFile, boolean paramBoolean) {
/* 57 */     paramJarFile.setEagerValidation(paramBoolean);
/*    */   }
/*    */ 
/*    */   public List getManifestDigests(JarFile paramJarFile) {
/* 61 */     return paramJarFile.getManifestDigests();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.jar.JavaUtilJarAccessImpl
 * JD-Core Version:    0.6.2
 */