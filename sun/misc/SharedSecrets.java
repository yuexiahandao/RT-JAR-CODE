/*     */ package sun.misc;
/*     */ 
/*     */ import java.io.Console;
/*     */ import java.io.FileDescriptor;
/*     */ import java.net.HttpCookie;
/*     */ import java.nio.ByteOrder;
/*     */ import java.security.AccessController;
/*     */ import java.security.ProtectionDomain;
/*     */ import java.util.jar.JarFile;
/*     */ import java.util.zip.Adler32;
/*     */ import java.util.zip.ZipFile;
/*     */ import javax.security.auth.kerberos.KeyTab;
/*     */ 
/*     */ public class SharedSecrets
/*     */ {
/*  47 */   private static final Unsafe unsafe = Unsafe.getUnsafe();
/*     */   private static JavaUtilJarAccess javaUtilJarAccess;
/*     */   private static JavaLangAccess javaLangAccess;
/*     */   private static JavaIOAccess javaIOAccess;
/*     */   private static JavaNetAccess javaNetAccess;
/*     */   private static JavaNetHttpCookieAccess javaNetHttpCookieAccess;
/*     */   private static JavaNioAccess javaNioAccess;
/*     */   private static JavaIOFileDescriptorAccess javaIOFileDescriptorAccess;
/*     */   private static JavaSecurityProtectionDomainAccess javaSecurityProtectionDomainAccess;
/*     */   private static JavaSecurityAccess javaSecurityAccess;
/*     */   private static JavaxSecurityAuthKerberosAccess javaxSecurityAuthKerberosAccess;
/*     */   private static JavaUtilZipAccess javaUtilZipAccess;
/*     */   private static JavaUtilZipFileAccess javaUtilZipFileAccess;
/*     */   private static JavaAWTAccess javaAWTAccess;
/*     */ 
/*     */   public static JavaUtilJarAccess javaUtilJarAccess()
/*     */   {
/*  63 */     if (javaUtilJarAccess == null)
/*     */     {
/*  66 */       unsafe.ensureClassInitialized(JarFile.class);
/*     */     }
/*  68 */     return javaUtilJarAccess;
/*     */   }
/*     */ 
/*     */   public static void setJavaUtilJarAccess(JavaUtilJarAccess paramJavaUtilJarAccess) {
/*  72 */     javaUtilJarAccess = paramJavaUtilJarAccess;
/*     */   }
/*     */ 
/*     */   public static void setJavaLangAccess(JavaLangAccess paramJavaLangAccess) {
/*  76 */     javaLangAccess = paramJavaLangAccess;
/*     */   }
/*     */ 
/*     */   public static JavaLangAccess getJavaLangAccess() {
/*  80 */     return javaLangAccess;
/*     */   }
/*     */ 
/*     */   public static void setJavaNetAccess(JavaNetAccess paramJavaNetAccess) {
/*  84 */     javaNetAccess = paramJavaNetAccess;
/*     */   }
/*     */ 
/*     */   public static JavaNetAccess getJavaNetAccess() {
/*  88 */     return javaNetAccess;
/*     */   }
/*     */ 
/*     */   public static void setJavaNetHttpCookieAccess(JavaNetHttpCookieAccess paramJavaNetHttpCookieAccess) {
/*  92 */     javaNetHttpCookieAccess = paramJavaNetHttpCookieAccess;
/*     */   }
/*     */ 
/*     */   public static JavaNetHttpCookieAccess getJavaNetHttpCookieAccess() {
/*  96 */     if (javaNetHttpCookieAccess == null)
/*  97 */       unsafe.ensureClassInitialized(HttpCookie.class);
/*  98 */     return javaNetHttpCookieAccess;
/*     */   }
/*     */ 
/*     */   public static void setJavaNioAccess(JavaNioAccess paramJavaNioAccess) {
/* 102 */     javaNioAccess = paramJavaNioAccess;
/*     */   }
/*     */ 
/*     */   public static JavaNioAccess getJavaNioAccess() {
/* 106 */     if (javaNioAccess == null)
/*     */     {
/* 110 */       unsafe.ensureClassInitialized(ByteOrder.class);
/*     */     }
/* 112 */     return javaNioAccess;
/*     */   }
/*     */ 
/*     */   public static void setJavaIOAccess(JavaIOAccess paramJavaIOAccess) {
/* 116 */     javaIOAccess = paramJavaIOAccess;
/*     */   }
/*     */ 
/*     */   public static JavaIOAccess getJavaIOAccess() {
/* 120 */     if (javaIOAccess == null) {
/* 121 */       unsafe.ensureClassInitialized(Console.class);
/*     */     }
/* 123 */     return javaIOAccess;
/*     */   }
/*     */ 
/*     */   public static void setJavaIOFileDescriptorAccess(JavaIOFileDescriptorAccess paramJavaIOFileDescriptorAccess) {
/* 127 */     javaIOFileDescriptorAccess = paramJavaIOFileDescriptorAccess;
/*     */   }
/*     */ 
/*     */   public static JavaIOFileDescriptorAccess getJavaIOFileDescriptorAccess() {
/* 131 */     if (javaIOFileDescriptorAccess == null) {
/* 132 */       unsafe.ensureClassInitialized(FileDescriptor.class);
/*     */     }
/* 134 */     return javaIOFileDescriptorAccess;
/*     */   }
/*     */ 
/*     */   public static void setJavaSecurityProtectionDomainAccess(JavaSecurityProtectionDomainAccess paramJavaSecurityProtectionDomainAccess)
/*     */   {
/* 139 */     javaSecurityProtectionDomainAccess = paramJavaSecurityProtectionDomainAccess;
/*     */   }
/*     */ 
/*     */   public static JavaSecurityProtectionDomainAccess getJavaSecurityProtectionDomainAccess()
/*     */   {
/* 144 */     if (javaSecurityProtectionDomainAccess == null)
/* 145 */       unsafe.ensureClassInitialized(ProtectionDomain.class);
/* 146 */     return javaSecurityProtectionDomainAccess;
/*     */   }
/*     */ 
/*     */   public static void setJavaSecurityAccess(JavaSecurityAccess paramJavaSecurityAccess) {
/* 150 */     javaSecurityAccess = paramJavaSecurityAccess;
/*     */   }
/*     */ 
/*     */   public static JavaSecurityAccess getJavaSecurityAccess() {
/* 154 */     if (javaSecurityAccess == null) {
/* 155 */       unsafe.ensureClassInitialized(AccessController.class);
/*     */     }
/* 157 */     return javaSecurityAccess;
/*     */   }
/*     */ 
/*     */   public static void setJavaxSecurityAuthKerberosAccess(JavaxSecurityAuthKerberosAccess paramJavaxSecurityAuthKerberosAccess)
/*     */   {
/* 162 */     javaxSecurityAuthKerberosAccess = paramJavaxSecurityAuthKerberosAccess;
/*     */   }
/*     */ 
/*     */   public static JavaxSecurityAuthKerberosAccess getJavaxSecurityAuthKerberosAccess()
/*     */   {
/* 167 */     if (javaxSecurityAuthKerberosAccess == null)
/* 168 */       unsafe.ensureClassInitialized(KeyTab.class);
/* 169 */     return javaxSecurityAuthKerberosAccess;
/*     */   }
/*     */ 
/*     */   public static void setJavaUtilZipAccess(JavaUtilZipAccess paramJavaUtilZipAccess) {
/* 173 */     javaUtilZipAccess = paramJavaUtilZipAccess;
/*     */   }
/*     */ 
/*     */   public static JavaUtilZipAccess getJavaUtilZipAccess() {
/* 177 */     if (javaUtilZipAccess == null) {
/* 178 */       unsafe.ensureClassInitialized(Adler32.class);
/*     */     }
/* 180 */     return javaUtilZipAccess;
/*     */   }
/*     */ 
/*     */   public static JavaUtilZipFileAccess getJavaUtilZipFileAccess() {
/* 184 */     if (javaUtilZipFileAccess == null)
/* 185 */       unsafe.ensureClassInitialized(ZipFile.class);
/* 186 */     return javaUtilZipFileAccess;
/*     */   }
/*     */ 
/*     */   public static void setJavaUtilZipFileAccess(JavaUtilZipFileAccess paramJavaUtilZipFileAccess) {
/* 190 */     javaUtilZipFileAccess = paramJavaUtilZipFileAccess;
/*     */   }
/*     */ 
/*     */   public static void setJavaAWTAccess(JavaAWTAccess paramJavaAWTAccess) {
/* 194 */     javaAWTAccess = paramJavaAWTAccess;
/*     */   }
/*     */ 
/*     */   public static JavaAWTAccess getJavaAWTAccess()
/*     */   {
/* 200 */     return javaAWTAccess;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.misc.SharedSecrets
 * JD-Core Version:    0.6.2
 */