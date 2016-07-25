/*     */ package javax.net.ssl;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import javax.net.ServerSocketFactory;
/*     */ 
/*     */ public abstract class SSLServerSocketFactory extends ServerSocketFactory
/*     */ {
/*     */   private static SSLServerSocketFactory theFactory;
/*     */   private static boolean propertyChecked;
/*     */ 
/*     */   private static void log(String paramString)
/*     */   {
/*  52 */     if (SSLSocketFactory.DEBUG)
/*  53 */       System.out.println(paramString);
/*     */   }
/*     */ 
/*     */   public static synchronized ServerSocketFactory getDefault()
/*     */   {
/*  79 */     if (theFactory != null) {
/*  80 */       return theFactory;
/*     */     }
/*     */ 
/*  83 */     if (!propertyChecked) {
/*  84 */       propertyChecked = true;
/*  85 */       String str = SSLSocketFactory.getSecurityProperty("ssl.ServerSocketFactory.provider");
/*     */ 
/*  87 */       if (str != null) {
/*  88 */         log("setting up default SSLServerSocketFactory");
/*     */         try {
/*  90 */           Class localClass = null;
/*     */           try {
/*  92 */             localClass = Class.forName(str);
/*     */           } catch (ClassNotFoundException localClassNotFoundException) {
/*  94 */             ClassLoader localClassLoader = ClassLoader.getSystemClassLoader();
/*  95 */             if (localClassLoader != null) {
/*  96 */               localClass = localClassLoader.loadClass(str);
/*     */             }
/*     */           }
/*  99 */           log("class " + str + " is loaded");
/* 100 */           SSLServerSocketFactory localSSLServerSocketFactory = (SSLServerSocketFactory)localClass.newInstance();
/* 101 */           log("instantiated an instance of class " + str);
/* 102 */           theFactory = localSSLServerSocketFactory;
/* 103 */           return localSSLServerSocketFactory;
/*     */         } catch (Exception localException) {
/* 105 */           log("SSLServerSocketFactory instantiation failed: " + localException);
/* 106 */           theFactory = new DefaultSSLServerSocketFactory(localException);
/* 107 */           return theFactory;
/*     */         }
/*     */       }
/*     */     }
/*     */     try
/*     */     {
/* 113 */       return SSLContext.getDefault().getServerSocketFactory();
/*     */     } catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {
/* 115 */       return new DefaultSSLServerSocketFactory(localNoSuchAlgorithmException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public abstract String[] getDefaultCipherSuites();
/*     */ 
/*     */   public abstract String[] getSupportedCipherSuites();
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.net.ssl.SSLServerSocketFactory
 * JD-Core Version:    0.6.2
 */