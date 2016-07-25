/*     */ package com.sun.org.apache.xerces.internal.xinclude;
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
/*     */ final class SecuritySupport
/*     */ {
/*  41 */   private static final SecuritySupport securitySupport = new SecuritySupport();
/*     */ 
/*     */   static SecuritySupport getInstance()
/*     */   {
/*  47 */     return securitySupport;
/*     */   }
/*     */ 
/*     */   ClassLoader getContextClassLoader() {
/*  51 */     return (ClassLoader)AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public Object run() {
/*  54 */         ClassLoader cl = null;
/*     */         try {
/*  56 */           cl = Thread.currentThread().getContextClassLoader(); } catch (SecurityException ex) {
/*     */         }
/*  58 */         return cl;
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   ClassLoader getSystemClassLoader() {
/*  64 */     return (ClassLoader)AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public Object run() {
/*  67 */         ClassLoader cl = null;
/*     */         try {
/*  69 */           cl = ClassLoader.getSystemClassLoader(); } catch (SecurityException ex) {
/*     */         }
/*  71 */         return cl;
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   ClassLoader getParentClassLoader(final ClassLoader cl) {
/*  77 */     return (ClassLoader)AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public Object run() {
/*  80 */         ClassLoader parent = null;
/*     */         try {
/*  82 */           parent = cl.getParent();
/*     */         }
/*     */         catch (SecurityException ex)
/*     */         {
/*     */         }
/*  87 */         return parent == cl ? null : parent;
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   String getSystemProperty(final String propName) {
/*  93 */     return (String)AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public Object run() {
/*  96 */         return System.getProperty(propName);
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   FileInputStream getFileInputStream(final File file) throws FileNotFoundException
/*     */   {
/*     */     try
/*     */     {
/* 105 */       return (FileInputStream)AccessController.doPrivileged(new PrivilegedExceptionAction()
/*     */       {
/*     */         public Object run() throws FileNotFoundException {
/* 108 */           return new FileInputStream(file);
/*     */         } } );
/*     */     }
/*     */     catch (PrivilegedActionException e) {
/* 112 */       throw ((FileNotFoundException)e.getException());
/*     */     }
/*     */   }
/*     */ 
/*     */   InputStream getResourceAsStream(final ClassLoader cl, final String name)
/*     */   {
/* 119 */     return (InputStream)AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public Object run()
/*     */       {
/*     */         InputStream ris;
/*     */         InputStream ris;
/* 123 */         if (cl == null)
/* 124 */           ris = ClassLoader.getSystemResourceAsStream(name);
/*     */         else {
/* 126 */           ris = cl.getResourceAsStream(name);
/*     */         }
/* 128 */         return ris;
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   boolean getFileExists(final File f) {
/* 134 */     return ((Boolean)AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public Object run() {
/* 137 */         return new Boolean(f.exists());
/*     */       }
/*     */     })).booleanValue();
/*     */   }
/*     */ 
/*     */   long getLastModified(final File f)
/*     */   {
/* 143 */     return ((Long)AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public Object run() {
/* 146 */         return new Long(f.lastModified());
/*     */       }
/*     */     })).longValue();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.xinclude.SecuritySupport
 * JD-Core Version:    0.6.2
 */