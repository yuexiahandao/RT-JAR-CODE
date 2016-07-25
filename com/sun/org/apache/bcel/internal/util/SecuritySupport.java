/*     */ package com.sun.org.apache.bcel.internal.util;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.FilenameFilter;
/*     */ import java.io.InputStream;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.security.PrivilegedActionException;
/*     */ import java.security.PrivilegedExceptionAction;
/*     */ import java.util.ListResourceBundle;
/*     */ import java.util.Locale;
/*     */ import java.util.MissingResourceException;
/*     */ import java.util.ResourceBundle;
/*     */ 
/*     */ public final class SecuritySupport
/*     */ {
/*  46 */   private static final SecuritySupport securitySupport = new SecuritySupport();
/*     */ 
/*     */   public static SecuritySupport getInstance()
/*     */   {
/*  52 */     return securitySupport;
/*     */   }
/*     */ 
/*     */   static ClassLoader getContextClassLoader() {
/*  56 */     return (ClassLoader)AccessController.doPrivileged(new PrivilegedAction() {
/*     */       public Object run() {
/*  58 */         ClassLoader cl = null;
/*     */         try {
/*  60 */           cl = Thread.currentThread().getContextClassLoader();
/*     */         } catch (SecurityException ex) {
/*     */         }
/*  63 */         return cl;
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   static ClassLoader getSystemClassLoader() {
/*  69 */     return (ClassLoader)AccessController.doPrivileged(new PrivilegedAction() {
/*     */       public Object run() {
/*  71 */         ClassLoader cl = null;
/*     */         try {
/*  73 */           cl = ClassLoader.getSystemClassLoader();
/*     */         } catch (SecurityException ex) {
/*     */         }
/*  76 */         return cl;
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   static ClassLoader getParentClassLoader(ClassLoader cl) {
/*  82 */     return (ClassLoader)AccessController.doPrivileged(new PrivilegedAction() {
/*     */       public Object run() {
/*  84 */         ClassLoader parent = null;
/*     */         try {
/*  86 */           parent = this.val$cl.getParent();
/*     */         }
/*     */         catch (SecurityException ex)
/*     */         {
/*     */         }
/*     */ 
/*  92 */         return parent == this.val$cl ? null : parent;
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   public static String getSystemProperty(String propName) {
/*  98 */     return (String)AccessController.doPrivileged(new PrivilegedAction() {
/*     */       public Object run() {
/* 100 */         return System.getProperty(this.val$propName);
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   static FileInputStream getFileInputStream(File file) throws FileNotFoundException
/*     */   {
/*     */     try {
/* 108 */       return (FileInputStream)AccessController.doPrivileged(new PrivilegedExceptionAction() {
/*     */         public Object run() throws FileNotFoundException {
/* 110 */           return new FileInputStream(this.val$file);
/*     */         } } );
/*     */     }
/*     */     catch (PrivilegedActionException e) {
/* 114 */       throw ((FileNotFoundException)e.getException());
/*     */     }
/*     */   }
/*     */ 
/*     */   public static InputStream getResourceAsStream(String name)
/*     */   {
/* 123 */     if (System.getSecurityManager() != null) {
/* 124 */       return getResourceAsStream(null, name);
/*     */     }
/* 126 */     return getResourceAsStream(findClassLoader(), name);
/*     */   }
/*     */ 
/*     */   public static InputStream getResourceAsStream(ClassLoader cl, final String name)
/*     */   {
/* 132 */     return (InputStream)AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public Object run()
/*     */       {
/*     */         InputStream ris;
/*     */         InputStream ris;
/* 135 */         if (this.val$cl == null)
/* 136 */           ris = Object.class.getResourceAsStream("/" + name);
/*     */         else {
/* 138 */           ris = this.val$cl.getResourceAsStream(name);
/*     */         }
/* 140 */         return ris;
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   public static ListResourceBundle getResourceBundle(String bundle)
/*     */   {
/* 154 */     return getResourceBundle(bundle, Locale.getDefault());
/*     */   }
/*     */ 
/*     */   public static ListResourceBundle getResourceBundle(String bundle, final Locale locale)
/*     */   {
/* 167 */     return (ListResourceBundle)AccessController.doPrivileged(new PrivilegedAction() {
/*     */       public ListResourceBundle run() {
/*     */         try {
/* 170 */           return (ListResourceBundle)ResourceBundle.getBundle(this.val$bundle, locale);
/*     */         } catch (MissingResourceException e) {
/*     */           try {
/* 173 */             return (ListResourceBundle)ResourceBundle.getBundle(this.val$bundle, new Locale("en", "US")); } catch (MissingResourceException e2) {  }
/*     */         }
/* 175 */         throw new MissingResourceException("Could not load any resource bundle by " + this.val$bundle, this.val$bundle, "");
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   public static String[] getFileList(File f, final FilenameFilter filter)
/*     */   {
/* 184 */     return (String[])AccessController.doPrivileged(new PrivilegedAction() {
/*     */       public Object run() {
/* 186 */         return this.val$f.list(filter);
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   public static boolean getFileExists(File f) {
/* 192 */     return ((Boolean)AccessController.doPrivileged(new PrivilegedAction() {
/*     */       public Object run() {
/* 194 */         return this.val$f.exists() ? Boolean.TRUE : Boolean.FALSE;
/*     */       }
/*     */     })).booleanValue();
/*     */   }
/*     */ 
/*     */   static long getLastModified(File f)
/*     */   {
/* 200 */     return ((Long)AccessController.doPrivileged(new PrivilegedAction() {
/*     */       public Object run() {
/* 202 */         return new Long(this.val$f.lastModified());
/*     */       }
/*     */     })).longValue();
/*     */   }
/*     */ 
/*     */   public static ClassLoader findClassLoader()
/*     */   {
/* 213 */     if (System.getSecurityManager() != null)
/*     */     {
/* 215 */       return null;
/*     */     }
/* 217 */     return SecuritySupport.class.getClassLoader();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.util.SecuritySupport
 * JD-Core Version:    0.6.2
 */