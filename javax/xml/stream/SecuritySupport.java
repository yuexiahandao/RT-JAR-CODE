/*     */ package javax.xml.stream;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.InputStream;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.security.PrivilegedActionException;
/*     */ import java.security.PrivilegedExceptionAction;
/*     */ 
/*     */ class SecuritySupport
/*     */ {
/*     */   ClassLoader getContextClassLoader()
/*     */     throws SecurityException
/*     */   {
/*  44 */     return (ClassLoader)AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public Object run() {
/*  47 */         ClassLoader cl = null;
/*     */ 
/*  49 */         cl = Thread.currentThread().getContextClassLoader();
/*     */ 
/*  52 */         if (cl == null) {
/*  53 */           cl = ClassLoader.getSystemClassLoader();
/*     */         }
/*  55 */         return cl;
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   String getSystemProperty(final String propName) {
/*  61 */     return (String)AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public Object run() {
/*  64 */         return System.getProperty(propName);
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   FileInputStream getFileInputStream(final File file) throws FileNotFoundException
/*     */   {
/*     */     try
/*     */     {
/*  73 */       return (FileInputStream)AccessController.doPrivileged(new PrivilegedExceptionAction()
/*     */       {
/*     */         public Object run() throws FileNotFoundException {
/*  76 */           return new FileInputStream(file);
/*     */         } } );
/*     */     }
/*     */     catch (PrivilegedActionException e) {
/*  80 */       throw ((FileNotFoundException)e.getException());
/*     */     }
/*     */   }
/*     */ 
/*     */   InputStream getResourceAsStream(final ClassLoader cl, final String name)
/*     */   {
/*  87 */     return (InputStream)AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public Object run()
/*     */       {
/*     */         InputStream ris;
/*     */         InputStream ris;
/*  91 */         if (cl == null)
/*  92 */           ris = Object.class.getResourceAsStream(name);
/*     */         else {
/*  94 */           ris = cl.getResourceAsStream(name);
/*     */         }
/*  96 */         return ris;
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   boolean doesFileExist(final File f) {
/* 102 */     return ((Boolean)AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public Object run() {
/* 105 */         return new Boolean(f.exists());
/*     */       }
/*     */     })).booleanValue();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.stream.SecuritySupport
 * JD-Core Version:    0.6.2
 */