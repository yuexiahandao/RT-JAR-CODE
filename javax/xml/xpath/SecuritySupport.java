/*     */ package javax.xml.xpath;
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
/*  45 */     return (ClassLoader)AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public Object run() {
/*  48 */         ClassLoader cl = null;
/*     */         try {
/*  50 */           cl = Thread.currentThread().getContextClassLoader(); } catch (SecurityException ex) {
/*     */         }
/*  52 */         return cl;
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   String getSystemProperty(final String propName) {
/*  58 */     return (String)AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public Object run() {
/*  61 */         return System.getProperty(propName);
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   FileInputStream getFileInputStream(final File file) throws FileNotFoundException
/*     */   {
/*     */     try
/*     */     {
/*  70 */       return (FileInputStream)AccessController.doPrivileged(new PrivilegedExceptionAction()
/*     */       {
/*     */         public Object run() throws FileNotFoundException {
/*  73 */           return new FileInputStream(file);
/*     */         } } );
/*     */     }
/*     */     catch (PrivilegedActionException e) {
/*  77 */       throw ((FileNotFoundException)e.getException());
/*     */     }
/*     */   }
/*     */ 
/*     */   InputStream getURLInputStream(final URL url) throws IOException
/*     */   {
/*     */     try
/*     */     {
/*  85 */       return (InputStream)AccessController.doPrivileged(new PrivilegedExceptionAction()
/*     */       {
/*     */         public Object run() throws IOException {
/*  88 */           return url.openStream();
/*     */         } } );
/*     */     }
/*     */     catch (PrivilegedActionException e) {
/*  92 */       throw ((IOException)e.getException());
/*     */     }
/*     */   }
/*     */ 
/*     */   URL getResourceAsURL(final ClassLoader cl, final String name)
/*     */   {
/*  99 */     return (URL)AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public Object run()
/*     */       {
/*     */         URL url;
/*     */         URL url;
/* 103 */         if (cl == null)
/* 104 */           url = Object.class.getResource(name);
/*     */         else {
/* 106 */           url = cl.getResource(name);
/*     */         }
/* 108 */         return url;
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   Enumeration getResources(final ClassLoader cl, final String name) throws IOException
/*     */   {
/*     */     try
/*     */     {
/* 117 */       return (Enumeration)AccessController.doPrivileged(new PrivilegedExceptionAction()
/*     */       {
/*     */         public Object run()
/*     */           throws IOException
/*     */         {
/*     */           Enumeration enumeration;
/*     */           Enumeration enumeration;
/* 121 */           if (cl == null)
/* 122 */             enumeration = ClassLoader.getSystemResources(name);
/*     */           else {
/* 124 */             enumeration = cl.getResources(name);
/*     */           }
/* 126 */           return enumeration;
/*     */         } } );
/*     */     }
/*     */     catch (PrivilegedActionException e) {
/* 130 */       throw ((IOException)e.getException());
/*     */     }
/*     */   }
/*     */ 
/*     */   InputStream getResourceAsStream(final ClassLoader cl, final String name)
/*     */   {
/* 137 */     return (InputStream)AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public Object run()
/*     */       {
/*     */         InputStream ris;
/*     */         InputStream ris;
/* 141 */         if (cl == null)
/* 142 */           ris = Object.class.getResourceAsStream(name);
/*     */         else {
/* 144 */           ris = cl.getResourceAsStream(name);
/*     */         }
/* 146 */         return ris;
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   boolean doesFileExist(final File f) {
/* 152 */     return ((Boolean)AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public Object run() {
/* 155 */         return new Boolean(f.exists());
/*     */       }
/*     */     })).booleanValue();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.xpath.SecuritySupport
 * JD-Core Version:    0.6.2
 */