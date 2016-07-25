/*     */ package javax.rmi;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.Properties;
/*     */ 
/*     */ class GetORBPropertiesFileAction
/*     */   implements PrivilegedAction
/*     */ {
/* 235 */   private boolean debug = false;
/*     */ 
/*     */   private String getSystemProperty(final String paramString)
/*     */   {
/* 243 */     String str = (String)AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public Object run() {
/* 246 */         return System.getProperty(paramString);
/*     */       }
/*     */     });
/* 251 */     return str;
/*     */   }
/*     */ 
/*     */   private void getPropertiesFromFile(Properties paramProperties, String paramString)
/*     */   {
/*     */     try {
/* 257 */       File localFile = new File(paramString);
/* 258 */       if (!localFile.exists()) {
/* 259 */         return;
/*     */       }
/* 261 */       FileInputStream localFileInputStream = new FileInputStream(localFile);
/*     */       try
/*     */       {
/* 264 */         paramProperties.load(localFileInputStream);
/*     */       } finally {
/* 266 */         localFileInputStream.close();
/*     */       }
/*     */     } catch (Exception localException) {
/* 269 */       if (this.debug)
/* 270 */         System.out.println("ORB properties file " + paramString + " not found: " + localException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public Object run()
/*     */   {
/* 277 */     Properties localProperties1 = new Properties();
/*     */ 
/* 279 */     String str1 = getSystemProperty("java.home");
/* 280 */     String str2 = str1 + File.separator + "lib" + File.separator + "orb.properties";
/*     */ 
/* 283 */     getPropertiesFromFile(localProperties1, str2);
/*     */ 
/* 285 */     Properties localProperties2 = new Properties(localProperties1);
/*     */ 
/* 287 */     String str3 = getSystemProperty("user.home");
/* 288 */     str2 = str3 + File.separator + "orb.properties";
/*     */ 
/* 290 */     getPropertiesFromFile(localProperties2, str2);
/* 291 */     return localProperties2;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.rmi.GetORBPropertiesFileAction
 * JD-Core Version:    0.6.2
 */