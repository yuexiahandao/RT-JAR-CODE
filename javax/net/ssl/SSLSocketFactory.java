/*     */ package javax.net.ssl;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.net.Socket;
/*     */ import java.security.AccessController;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.security.Security;
/*     */ import java.util.Locale;
/*     */ import javax.net.SocketFactory;
/*     */ import sun.security.action.GetPropertyAction;
/*     */ 
/*     */ public abstract class SSLSocketFactory extends SocketFactory
/*     */ {
/*     */   private static SSLSocketFactory theFactory;
/*     */   private static boolean propertyChecked;
/*  56 */   static final boolean DEBUG = (str.contains("all")) || (str.contains("ssl"));
/*     */ 
/*     */   private static void log(String paramString)
/*     */   {
/*  60 */     if (DEBUG)
/*  61 */       System.out.println(paramString);
/*     */   }
/*     */ 
/*     */   public static synchronized SocketFactory getDefault()
/*     */   {
/*  88 */     if (theFactory != null) {
/*  89 */       return theFactory;
/*     */     }
/*     */ 
/*  92 */     if (!propertyChecked) {
/*  93 */       propertyChecked = true;
/*  94 */       String str = getSecurityProperty("ssl.SocketFactory.provider");
/*  95 */       if (str != null) {
/*  96 */         log("setting up default SSLSocketFactory");
/*     */         try {
/*  98 */           Class localClass = null;
/*     */           try {
/* 100 */             localClass = Class.forName(str);
/*     */           } catch (ClassNotFoundException localClassNotFoundException) {
/* 102 */             ClassLoader localClassLoader = ClassLoader.getSystemClassLoader();
/* 103 */             if (localClassLoader != null) {
/* 104 */               localClass = localClassLoader.loadClass(str);
/*     */             }
/*     */           }
/* 107 */           log("class " + str + " is loaded");
/* 108 */           SSLSocketFactory localSSLSocketFactory = (SSLSocketFactory)localClass.newInstance();
/* 109 */           log("instantiated an instance of class " + str);
/* 110 */           theFactory = localSSLSocketFactory;
/* 111 */           return localSSLSocketFactory;
/*     */         } catch (Exception localException) {
/* 113 */           log("SSLSocketFactory instantiation failed: " + localException.toString());
/* 114 */           theFactory = new DefaultSSLSocketFactory(localException);
/* 115 */           return theFactory;
/*     */         }
/*     */       }
/*     */     }
/*     */     try
/*     */     {
/* 121 */       return SSLContext.getDefault().getSocketFactory();
/*     */     } catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {
/* 123 */       return new DefaultSSLSocketFactory(localNoSuchAlgorithmException);
/*     */     }
/*     */   }
/*     */ 
/*     */   static String getSecurityProperty(String paramString) {
/* 128 */     return (String)AccessController.doPrivileged(new PrivilegedAction() {
/*     */       public String run() {
/* 130 */         String str = Security.getProperty(this.val$name);
/* 131 */         if (str != null) {
/* 132 */           str = str.trim();
/* 133 */           if (str.length() == 0) {
/* 134 */             str = null;
/*     */           }
/*     */         }
/* 137 */         return str;
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   public abstract String[] getDefaultCipherSuites();
/*     */ 
/*     */   public abstract String[] getSupportedCipherSuites();
/*     */ 
/*     */   public abstract Socket createSocket(Socket paramSocket, String paramString, int paramInt, boolean paramBoolean)
/*     */     throws IOException;
/*     */ 
/*     */   static
/*     */   {
/*  53 */     String str = ((String)AccessController.doPrivileged(new GetPropertyAction("javax.net.debug", ""))).toLowerCase(Locale.ENGLISH);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.net.ssl.SSLSocketFactory
 * JD-Core Version:    0.6.2
 */