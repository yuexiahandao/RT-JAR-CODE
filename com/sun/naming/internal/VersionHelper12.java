/*     */ package com.sun.naming.internal;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.net.URLClassLoader;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.security.PrivilegedActionException;
/*     */ import java.security.PrivilegedExceptionAction;
/*     */ import java.util.Enumeration;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Properties;
/*     */ import javax.naming.NamingEnumeration;
/*     */ 
/*     */ final class VersionHelper12 extends VersionHelper
/*     */ {
/*     */   public Class loadClass(String paramString)
/*     */     throws ClassNotFoundException
/*     */   {
/*  61 */     return loadClass(paramString, getContextClassLoader());
/*     */   }
/*     */ 
/*     */   Class loadClass(String paramString, ClassLoader paramClassLoader)
/*     */     throws ClassNotFoundException
/*     */   {
/*  72 */     Class localClass = Class.forName(paramString, true, paramClassLoader);
/*  73 */     return localClass;
/*     */   }
/*     */ 
/*     */   public Class loadClass(String paramString1, String paramString2)
/*     */     throws ClassNotFoundException, MalformedURLException
/*     */   {
/*  83 */     ClassLoader localClassLoader = getContextClassLoader();
/*  84 */     URLClassLoader localURLClassLoader = URLClassLoader.newInstance(getUrlArray(paramString2), localClassLoader);
/*     */ 
/*  87 */     return loadClass(paramString1, localURLClassLoader);
/*     */   }
/*     */ 
/*     */   String getJndiProperty(final int paramInt) {
/*  91 */     return (String)AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public Object run() {
/*     */         try {
/*  95 */           return System.getProperty(VersionHelper.PROPS[paramInt]); } catch (SecurityException localSecurityException) {
/*     */         }
/*  97 */         return null;
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   String[] getJndiProperties()
/*     */   {
/* 105 */     Properties localProperties = (Properties)AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public Object run() {
/*     */         try {
/* 109 */           return System.getProperties(); } catch (SecurityException localSecurityException) {
/*     */         }
/* 111 */         return null;
/*     */       }
/*     */     });
/* 116 */     if (localProperties == null) {
/* 117 */       return null;
/*     */     }
/* 119 */     String[] arrayOfString = new String[PROPS.length];
/* 120 */     for (int i = 0; i < PROPS.length; i++) {
/* 121 */       arrayOfString[i] = localProperties.getProperty(PROPS[i]);
/*     */     }
/* 123 */     return arrayOfString;
/*     */   }
/*     */ 
/*     */   InputStream getResourceAsStream(final Class paramClass, final String paramString) {
/* 127 */     return (InputStream)AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public Object run() {
/* 130 */         return paramClass.getResourceAsStream(paramString);
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   InputStream getJavaHomeLibStream(final String paramString)
/*     */   {
/* 137 */     return (InputStream)AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public Object run() {
/*     */         try {
/* 141 */           String str1 = System.getProperty("java.home");
/* 142 */           if (str1 == null) {
/* 143 */             return null;
/*     */           }
/* 145 */           String str2 = str1 + File.separator + "lib" + File.separator + paramString;
/*     */ 
/* 147 */           return new FileInputStream(str2); } catch (Exception localException) {
/*     */         }
/* 149 */         return null;
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   NamingEnumeration getResources(final ClassLoader paramClassLoader, final String paramString)
/*     */     throws IOException
/*     */   {
/*     */     Enumeration localEnumeration;
/*     */     try
/*     */     {
/* 161 */       localEnumeration = (Enumeration)AccessController.doPrivileged(new PrivilegedExceptionAction()
/*     */       {
/*     */         public Object run() throws IOException {
/* 164 */           return paramClassLoader == null ? ClassLoader.getSystemResources(paramString) : paramClassLoader.getResources(paramString);
/*     */         }
/*     */ 
/*     */       });
/*     */     }
/*     */     catch (PrivilegedActionException localPrivilegedActionException)
/*     */     {
/* 171 */       throw ((IOException)localPrivilegedActionException.getException());
/*     */     }
/* 173 */     return new InputStreamEnumeration(localEnumeration);
/*     */   }
/*     */ 
/*     */   ClassLoader getContextClassLoader()
/*     */   {
/* 185 */     return (ClassLoader)AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public Object run() {
/* 188 */         ClassLoader localClassLoader = Thread.currentThread().getContextClassLoader();
/*     */ 
/* 190 */         if (localClassLoader == null)
/*     */         {
/* 192 */           localClassLoader = ClassLoader.getSystemClassLoader();
/*     */         }
/*     */ 
/* 195 */         return localClassLoader;
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   class InputStreamEnumeration
/*     */     implements NamingEnumeration
/*     */   {
/*     */     private final Enumeration urls;
/* 212 */     private Object nextElement = null;
/*     */ 
/*     */     InputStreamEnumeration(Enumeration arg2)
/*     */     {
/*     */       Object localObject;
/* 215 */       this.urls = localObject;
/*     */     }
/*     */ 
/*     */     private Object getNextElement()
/*     */     {
/* 223 */       return AccessController.doPrivileged(new PrivilegedAction()
/*     */       {
/*     */         public Object run() {
/* 226 */           while (VersionHelper12.InputStreamEnumeration.this.urls.hasMoreElements())
/*     */             try {
/* 228 */               return ((URL)VersionHelper12.InputStreamEnumeration.this.urls.nextElement()).openStream();
/*     */             }
/*     */             catch (IOException localIOException)
/*     */             {
/*     */             }
/* 233 */           return null;
/*     */         }
/*     */       });
/*     */     }
/*     */ 
/*     */     public boolean hasMore()
/*     */     {
/* 240 */       if (this.nextElement != null) {
/* 241 */         return true;
/*     */       }
/* 243 */       this.nextElement = getNextElement();
/* 244 */       return this.nextElement != null;
/*     */     }
/*     */ 
/*     */     public boolean hasMoreElements() {
/* 248 */       return hasMore();
/*     */     }
/*     */ 
/*     */     public Object next() {
/* 252 */       if (hasMore()) {
/* 253 */         Object localObject = this.nextElement;
/* 254 */         this.nextElement = null;
/* 255 */         return localObject;
/*     */       }
/* 257 */       throw new NoSuchElementException();
/*     */     }
/*     */ 
/*     */     public Object nextElement()
/*     */     {
/* 262 */       return next();
/*     */     }
/*     */ 
/*     */     public void close()
/*     */     {
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.naming.internal.VersionHelper12
 * JD-Core Version:    0.6.2
 */