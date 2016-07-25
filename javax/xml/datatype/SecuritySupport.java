/*     */ package javax.xml.datatype;
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
/*     */   {
/*  44 */     return (ClassLoader)AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public Object run() {
/*  47 */         ClassLoader cl = null;
/*     */         try {
/*  49 */           cl = Thread.currentThread().getContextClassLoader(); } catch (SecurityException ex) {
/*     */         }
/*  51 */         return cl;
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   String getSystemProperty(final String propName) {
/*  57 */     return (String)AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public Object run() {
/*  60 */         return System.getProperty(propName);
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   FileInputStream getFileInputStream(final File file) throws FileNotFoundException
/*     */   {
/*     */     try
/*     */     {
/*  69 */       return (FileInputStream)AccessController.doPrivileged(new PrivilegedExceptionAction()
/*     */       {
/*     */         public Object run() throws FileNotFoundException {
/*  72 */           return new FileInputStream(file);
/*     */         } } );
/*     */     }
/*     */     catch (PrivilegedActionException e) {
/*  76 */       throw ((FileNotFoundException)e.getException());
/*     */     }
/*     */   }
/*     */ 
/*     */   InputStream getResourceAsStream(final ClassLoader cl, final String name)
/*     */   {
/*  83 */     return (InputStream)AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public Object run()
/*     */       {
/*     */         InputStream ris;
/*     */         InputStream ris;
/*  87 */         if (cl == null)
/*  88 */           ris = Object.class.getResourceAsStream(name);
/*     */         else {
/*  90 */           ris = cl.getResourceAsStream(name);
/*     */         }
/*  92 */         return ris;
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   boolean doesFileExist(final File f) {
/*  98 */     return ((Boolean)AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public Object run() {
/* 101 */         return new Boolean(f.exists());
/*     */       }
/*     */     })).booleanValue();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.datatype.SecuritySupport
 * JD-Core Version:    0.6.2
 */