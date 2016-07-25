/*     */ package javax.activation;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.URL;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.security.PrivilegedActionException;
/*     */ import java.security.PrivilegedExceptionAction;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Enumeration;
/*     */ import java.util.List;
/*     */ 
/*     */ class SecuritySupport
/*     */ {
/*     */   public static ClassLoader getContextClassLoader()
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
/*     */   public static InputStream getResourceAsStream(Class c, final String name) throws IOException
/*     */   {
/*     */     try {
/*  60 */       return (InputStream)AccessController.doPrivileged(new PrivilegedExceptionAction()
/*     */       {
/*     */         public Object run() throws IOException {
/*  63 */           return this.val$c.getResourceAsStream(name);
/*     */         } } );
/*     */     }
/*     */     catch (PrivilegedActionException e) {
/*  67 */       throw ((IOException)e.getException());
/*     */     }
/*     */   }
/*     */ 
/*     */   public static URL[] getResources(ClassLoader cl, final String name) {
/*  72 */     return (URL[])AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public Object run() {
/*  75 */         URL[] ret = null;
/*     */         try {
/*  77 */           List v = new ArrayList();
/*  78 */           Enumeration e = this.val$cl.getResources(name);
/*  79 */           while ((e != null) && (e.hasMoreElements())) {
/*  80 */             URL url = (URL)e.nextElement();
/*  81 */             if (url != null)
/*  82 */               v.add(url);
/*     */           }
/*  84 */           if (v.size() > 0) {
/*  85 */             ret = new URL[v.size()];
/*  86 */             ret = (URL[])v.toArray(ret);
/*     */           }
/*     */         } catch (IOException ioex) {
/*     */         } catch (SecurityException ex) {  }
/*     */ 
/*  90 */         return ret;
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   public static URL[] getSystemResources(String name) {
/*  96 */     return (URL[])AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public Object run() {
/*  99 */         URL[] ret = null;
/*     */         try {
/* 101 */           List v = new ArrayList();
/* 102 */           Enumeration e = ClassLoader.getSystemResources(this.val$name);
/* 103 */           while ((e != null) && (e.hasMoreElements())) {
/* 104 */             URL url = (URL)e.nextElement();
/* 105 */             if (url != null)
/* 106 */               v.add(url);
/*     */           }
/* 108 */           if (v.size() > 0) {
/* 109 */             ret = new URL[v.size()];
/* 110 */             ret = (URL[])v.toArray(ret);
/*     */           }
/*     */         } catch (IOException ioex) {
/*     */         } catch (SecurityException ex) {  }
/*     */ 
/* 114 */         return ret;
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   public static InputStream openStream(URL url) throws IOException {
/*     */     try {
/* 121 */       return (InputStream)AccessController.doPrivileged(new PrivilegedExceptionAction()
/*     */       {
/*     */         public Object run() throws IOException {
/* 124 */           return this.val$url.openStream();
/*     */         } } );
/*     */     }
/*     */     catch (PrivilegedActionException e) {
/* 128 */       throw ((IOException)e.getException());
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.activation.SecuritySupport
 * JD-Core Version:    0.6.2
 */