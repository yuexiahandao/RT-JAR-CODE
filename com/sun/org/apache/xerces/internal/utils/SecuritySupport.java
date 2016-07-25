/*     */ package com.sun.org.apache.xerces.internal.utils;
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
/*     */ import java.util.Locale;
/*     */ import java.util.MissingResourceException;
/*     */ import java.util.Properties;
/*     */ import java.util.PropertyResourceBundle;
/*     */ import java.util.ResourceBundle;
/*     */ 
/*     */ public final class SecuritySupport
/*     */ {
/*  47 */   private static final SecuritySupport securitySupport = new SecuritySupport();
/*     */ 
/* 331 */   static final Properties cacheProps = new Properties();
/*     */ 
/* 336 */   static volatile boolean firstTime = true;
/*     */ 
/*     */   public static SecuritySupport getInstance()
/*     */   {
/*  53 */     return securitySupport;
/*     */   }
/*     */ 
/*     */   static ClassLoader getContextClassLoader() {
/*  57 */     return (ClassLoader)AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public Object run() {
/*  60 */         ClassLoader cl = null;
/*     */         try {
/*  62 */           cl = Thread.currentThread().getContextClassLoader(); } catch (SecurityException ex) {
/*     */         }
/*  64 */         return cl;
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   static ClassLoader getSystemClassLoader() {
/*  70 */     return (ClassLoader)AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public Object run() {
/*  73 */         ClassLoader cl = null;
/*     */         try {
/*  75 */           cl = ClassLoader.getSystemClassLoader(); } catch (SecurityException ex) {
/*     */         }
/*  77 */         return cl;
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   static ClassLoader getParentClassLoader(ClassLoader cl) {
/*  83 */     return (ClassLoader)AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public Object run() {
/*  86 */         ClassLoader parent = null;
/*     */         try {
/*  88 */           parent = this.val$cl.getParent();
/*     */         }
/*     */         catch (SecurityException ex)
/*     */         {
/*     */         }
/*  93 */         return parent == this.val$cl ? null : parent;
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   public static String getSystemProperty(String propName) {
/*  99 */     return (String)AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public Object run() {
/* 102 */         return System.getProperty(this.val$propName);
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   static FileInputStream getFileInputStream(File file) throws FileNotFoundException
/*     */   {
/*     */     try
/*     */     {
/* 111 */       return (FileInputStream)AccessController.doPrivileged(new PrivilegedExceptionAction()
/*     */       {
/*     */         public Object run() throws FileNotFoundException {
/* 114 */           return new FileInputStream(this.val$file);
/*     */         } } );
/*     */     }
/*     */     catch (PrivilegedActionException e) {
/* 118 */       throw ((FileNotFoundException)e.getException());
/*     */     }
/*     */   }
/*     */ 
/*     */   public static InputStream getResourceAsStream(String name)
/*     */   {
/* 126 */     if (System.getSecurityManager() != null) {
/* 127 */       return getResourceAsStream(null, name);
/*     */     }
/* 129 */     return getResourceAsStream(ObjectFactory.findClassLoader(), name);
/*     */   }
/*     */ 
/*     */   public static InputStream getResourceAsStream(ClassLoader cl, final String name)
/*     */   {
/* 136 */     return (InputStream)AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public Object run()
/*     */       {
/*     */         InputStream ris;
/*     */         InputStream ris;
/* 140 */         if (this.val$cl == null)
/* 141 */           ris = Object.class.getResourceAsStream("/" + name);
/*     */         else {
/* 143 */           ris = this.val$cl.getResourceAsStream(name);
/*     */         }
/* 145 */         return ris;
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   public static ResourceBundle getResourceBundle(String bundle)
/*     */   {
/* 156 */     return getResourceBundle(bundle, Locale.getDefault());
/*     */   }
/*     */ 
/*     */   public static ResourceBundle getResourceBundle(String bundle, final Locale locale)
/*     */   {
/* 166 */     return (ResourceBundle)AccessController.doPrivileged(new PrivilegedAction() {
/*     */       public ResourceBundle run() {
/*     */         try {
/* 169 */           return PropertyResourceBundle.getBundle(this.val$bundle, locale);
/*     */         } catch (MissingResourceException e) {
/*     */           try {
/* 172 */             return PropertyResourceBundle.getBundle(this.val$bundle, new Locale("en", "US")); } catch (MissingResourceException e2) {  }
/*     */         }
/* 174 */         throw new MissingResourceException("Could not load any resource bundle by " + this.val$bundle, this.val$bundle, "");
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   static boolean getFileExists(File f)
/*     */   {
/* 183 */     return ((Boolean)AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public Object run() {
/* 186 */         return this.val$f.exists() ? Boolean.TRUE : Boolean.FALSE;
/*     */       }
/*     */     })).booleanValue();
/*     */   }
/*     */ 
/*     */   static long getLastModified(File f)
/*     */   {
/* 192 */     return ((Long)AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public Object run() {
/* 195 */         return new Long(this.val$f.lastModified());
/*     */       }
/*     */     })).longValue();
/*     */   }
/*     */ 
/*     */   public static String sanitizePath(String uri)
/*     */   {
/* 207 */     if (uri == null) {
/* 208 */       return "";
/*     */     }
/* 210 */     int i = uri.lastIndexOf("/");
/* 211 */     if (i > 0) {
/* 212 */       return uri.substring(i + 1, uri.length());
/*     */     }
/* 214 */     return uri;
/*     */   }
/*     */ 
/*     */   public static String checkAccess(String systemId, String allowedProtocols, String accessAny)
/*     */     throws IOException
/*     */   {
/* 226 */     if ((systemId == null) || ((allowedProtocols != null) && (allowedProtocols.equalsIgnoreCase(accessAny))))
/*     */     {
/* 228 */       return null;
/*     */     }
/*     */     String protocol;
/*     */     String protocol;
/* 232 */     if (systemId.indexOf(":") == -1) {
/* 233 */       protocol = "file";
/*     */     } else {
/* 235 */       URL url = new URL(systemId);
/* 236 */       protocol = url.getProtocol();
/* 237 */       if (protocol.equalsIgnoreCase("jar")) {
/* 238 */         String path = url.getPath();
/* 239 */         protocol = path.substring(0, path.indexOf(":"));
/*     */       }
/*     */     }
/*     */ 
/* 243 */     if (isProtocolAllowed(protocol, allowedProtocols))
/*     */     {
/* 245 */       return null;
/*     */     }
/* 247 */     return protocol;
/*     */   }
/*     */ 
/*     */   private static boolean isProtocolAllowed(String protocol, String allowedProtocols)
/*     */   {
/* 260 */     if (allowedProtocols == null) {
/* 261 */       return false;
/*     */     }
/* 263 */     String[] temp = allowedProtocols.split(",");
/* 264 */     for (String t : temp) {
/* 265 */       t = t.trim();
/* 266 */       if (t.equalsIgnoreCase(protocol)) {
/* 267 */         return true;
/*     */       }
/*     */     }
/* 270 */     return false;
/*     */   }
/*     */ 
/*     */   public static String getJAXPSystemProperty(String sysPropertyId)
/*     */   {
/* 281 */     String accessExternal = getSystemProperty(sysPropertyId);
/* 282 */     if (accessExternal == null) {
/* 283 */       accessExternal = readJAXPProperty(sysPropertyId);
/*     */     }
/* 285 */     return accessExternal;
/*     */   }
/*     */ 
/*     */   static String readJAXPProperty(String propertyId)
/*     */   {
/* 296 */     String value = null;
/* 297 */     InputStream is = null;
/*     */     try {
/* 299 */       if (firstTime) {
/* 300 */         synchronized (cacheProps) {
/* 301 */           if (firstTime) {
/* 302 */             String configFile = getSystemProperty("java.home") + File.separator + "lib" + File.separator + "jaxp.properties";
/*     */ 
/* 304 */             File f = new File(configFile);
/* 305 */             if (getFileExists(f)) {
/* 306 */               is = getFileInputStream(f);
/* 307 */               cacheProps.load(is);
/*     */             }
/* 309 */             firstTime = false;
/*     */           }
/*     */         }
/*     */       }
/* 313 */       value = cacheProps.getProperty(propertyId);
/*     */     }
/*     */     catch (Exception ex) {
/*     */     }
/*     */     finally {
/* 318 */       if (is != null)
/*     */         try {
/* 320 */           is.close();
/*     */         }
/*     */         catch (IOException ex) {
/*     */         }
/*     */     }
/* 325 */     return value;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.utils.SecuritySupport
 * JD-Core Version:    0.6.2
 */