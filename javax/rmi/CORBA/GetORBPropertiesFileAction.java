/*     */ package javax.rmi.CORBA;
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
/*  50 */   private boolean debug = false;
/*     */ 
/*     */   private String getSystemProperty(final String paramString)
/*     */   {
/*  58 */     String str = (String)AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public Object run() {
/*  61 */         return System.getProperty(paramString);
/*     */       }
/*     */     });
/*  66 */     return str;
/*     */   }
/*     */ 
/*     */   private void getPropertiesFromFile(Properties paramProperties, String paramString)
/*     */   {
/*     */     try {
/*  72 */       File localFile = new File(paramString);
/*  73 */       if (!localFile.exists()) {
/*  74 */         return;
/*     */       }
/*  76 */       FileInputStream localFileInputStream = new FileInputStream(localFile);
/*     */       try
/*     */       {
/*  79 */         paramProperties.load(localFileInputStream);
/*     */       } finally {
/*  81 */         localFileInputStream.close();
/*     */       }
/*     */     } catch (Exception localException) {
/*  84 */       if (this.debug)
/*  85 */         System.out.println("ORB properties file " + paramString + " not found: " + localException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public Object run()
/*     */   {
/*  92 */     Properties localProperties1 = new Properties();
/*     */ 
/*  94 */     String str1 = getSystemProperty("java.home");
/*  95 */     String str2 = str1 + File.separator + "lib" + File.separator + "orb.properties";
/*     */ 
/*  98 */     getPropertiesFromFile(localProperties1, str2);
/*     */ 
/* 100 */     Properties localProperties2 = new Properties(localProperties1);
/*     */ 
/* 102 */     String str3 = getSystemProperty("user.home");
/* 103 */     str2 = str3 + File.separator + "orb.properties";
/*     */ 
/* 105 */     getPropertiesFromFile(localProperties2, str2);
/* 106 */     return localProperties2;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.rmi.CORBA.GetORBPropertiesFileAction
 * JD-Core Version:    0.6.2
 */