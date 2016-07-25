/*     */ package javax.xml.validation;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.URL;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.security.PrivilegedActionException;
/*     */ import java.security.PrivilegedExceptionAction;
/*     */ import java.util.Enumeration;
/*     */ 
/*     */ class SecuritySupport
/*     */ {
/*     */   ClassLoader getContextClassLoader()
/*     */   {
/*  46 */     return (ClassLoader)AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public Object run() {
/*  49 */         ClassLoader cl = null;
/*     */ 
/*  51 */         cl = Thread.currentThread().getContextClassLoader();
/*     */ 
/*  53 */         if (cl == null)
/*  54 */           cl = ClassLoader.getSystemClassLoader();
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
/*     */   InputStream getURLInputStream(final URL url) throws IOException
/*     */   {
/*     */     try
/*     */     {
/*  88 */       return (InputStream)AccessController.doPrivileged(new PrivilegedExceptionAction()
/*     */       {
/*     */         public Object run() throws IOException {
/*  91 */           return url.openStream();
/*     */         } } );
/*     */     }
/*     */     catch (PrivilegedActionException e) {
/*  95 */       throw ((IOException)e.getException());
/*     */     }
/*     */   }
/*     */ 
/*     */   URL getResourceAsURL(final ClassLoader cl, final String name)
/*     */   {
/* 102 */     return (URL)AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public Object run()
/*     */       {
/*     */         URL url;
/*     */         URL url;
/* 106 */         if (cl == null)
/* 107 */           url = Object.class.getResource(name);
/*     */         else {
/* 109 */           url = cl.getResource(name);
/*     */         }
/* 111 */         return url;
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   Enumeration getResources(final ClassLoader cl, final String name) throws IOException
/*     */   {
/*     */     try
/*     */     {
/* 120 */       return (Enumeration)AccessController.doPrivileged(new PrivilegedExceptionAction()
/*     */       {
/*     */         public Object run()
/*     */           throws IOException
/*     */         {
/*     */           Enumeration enumeration;
/*     */           Enumeration enumeration;
/* 124 */           if (cl == null)
/* 125 */             enumeration = ClassLoader.getSystemResources(name);
/*     */           else {
/* 127 */             enumeration = cl.getResources(name);
/*     */           }
/* 129 */           return enumeration;
/*     */         } } );
/*     */     }
/*     */     catch (PrivilegedActionException e) {
/* 133 */       throw ((IOException)e.getException());
/*     */     }
/*     */   }
/*     */ 
/*     */   InputStream getResourceAsStream(final ClassLoader cl, final String name)
/*     */   {
/* 140 */     return (InputStream)AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public Object run()
/*     */       {
/*     */         InputStream ris;
/*     */         InputStream ris;
/* 144 */         if (cl == null)
/* 145 */           ris = Object.class.getResourceAsStream(name);
/*     */         else {
/* 147 */           ris = cl.getResourceAsStream(name);
/*     */         }
/* 149 */         return ris;
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   boolean doesFileExist(final File f) {
/* 155 */     return ((Boolean)AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public Object run() {
/* 158 */         return new Boolean(f.exists());
/*     */       }
/*     */     })).booleanValue();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.validation.SecuritySupport
 * JD-Core Version:    0.6.2
 */