/*     */ package org.xml.sax.helpers;
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
/*  42 */     return (ClassLoader)AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public Object run() {
/*  45 */         ClassLoader cl = null;
/*     */ 
/*  47 */         cl = Thread.currentThread().getContextClassLoader();
/*     */ 
/*  50 */         if (cl == null) {
/*  51 */           cl = ClassLoader.getSystemClassLoader();
/*     */         }
/*  53 */         return cl;
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   String getSystemProperty(final String propName) {
/*  59 */     return (String)AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public Object run() {
/*  62 */         return System.getProperty(propName);
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   FileInputStream getFileInputStream(final File file) throws FileNotFoundException
/*     */   {
/*     */     try
/*     */     {
/*  71 */       return (FileInputStream)AccessController.doPrivileged(new PrivilegedExceptionAction()
/*     */       {
/*     */         public Object run() throws FileNotFoundException {
/*  74 */           return new FileInputStream(file);
/*     */         } } );
/*     */     }
/*     */     catch (PrivilegedActionException e) {
/*  78 */       throw ((FileNotFoundException)e.getException());
/*     */     }
/*     */   }
/*     */ 
/*     */   InputStream getResourceAsStream(final ClassLoader cl, final String name)
/*     */   {
/*  85 */     return (InputStream)AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public Object run()
/*     */       {
/*     */         InputStream ris;
/*     */         InputStream ris;
/*  89 */         if (cl == null)
/*  90 */           ris = Object.class.getResourceAsStream(name);
/*     */         else {
/*  92 */           ris = cl.getResourceAsStream(name);
/*     */         }
/*  94 */         return ris;
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   boolean doesFileExist(final File f) {
/* 100 */     return ((Boolean)AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public Object run() {
/* 103 */         return new Boolean(f.exists());
/*     */       }
/*     */     })).booleanValue();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.xml.sax.helpers.SecuritySupport
 * JD-Core Version:    0.6.2
 */