/*     */ package sun.misc;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.net.URLClassLoader;
/*     */ import java.net.URLStreamHandler;
/*     */ import java.net.URLStreamHandlerFactory;
/*     */ import java.security.AccessControlContext;
/*     */ import java.security.AccessController;
/*     */ import java.security.CodeSource;
/*     */ import java.security.PermissionCollection;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.security.PrivilegedActionException;
/*     */ import java.security.PrivilegedExceptionAction;
/*     */ import java.security.ProtectionDomain;
/*     */ import java.security.cert.Certificate;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import java.util.StringTokenizer;
/*     */ import java.util.Vector;
/*     */ import sun.net.www.ParseUtil;
/*     */ 
/*     */ public class Launcher
/*     */ {
/*  56 */   private static URLStreamHandlerFactory factory = new Factory(null);
/*  57 */   private static Launcher launcher = new Launcher();
/*  58 */   private static String bootClassPath = System.getProperty("sun.boot.class.path");
/*     */   private ClassLoader loader;
/*     */   private static URLStreamHandler fileHandler;
/*     */ 
/*     */   public static Launcher getLauncher()
/*     */   {
/*  62 */     return launcher;
/*     */   }
/*     */ 
/*     */   public Launcher()
/*     */   {
/*     */     ExtClassLoader localExtClassLoader;
/*     */     try
/*     */     {
/*  71 */       localExtClassLoader = ExtClassLoader.getExtClassLoader();
/*     */     } catch (IOException localIOException1) {
/*  73 */       throw new InternalError("Could not create extension class loader");
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/*  79 */       this.loader = AppClassLoader.getAppClassLoader(localExtClassLoader);
/*     */     } catch (IOException localIOException2) {
/*  81 */       throw new InternalError("Could not create application class loader");
/*     */     }
/*     */ 
/*  86 */     Thread.currentThread().setContextClassLoader(this.loader);
/*     */ 
/*  89 */     String str = System.getProperty("java.security.manager");
/*  90 */     if (str != null) {
/*  91 */       SecurityManager localSecurityManager = null;
/*  92 */       if (("".equals(str)) || ("default".equals(str)))
/*  93 */         localSecurityManager = new SecurityManager();
/*     */       else
/*     */         try {
/*  96 */           localSecurityManager = (SecurityManager)this.loader.loadClass(str).newInstance();
/*     */         } catch (IllegalAccessException localIllegalAccessException) {
/*     */         } catch (InstantiationException localInstantiationException) {
/*     */         } catch (ClassNotFoundException localClassNotFoundException) {
/*     */         }
/*     */         catch (ClassCastException localClassCastException) {
/*     */         }
/* 103 */       if (localSecurityManager != null)
/* 104 */         System.setSecurityManager(localSecurityManager);
/*     */       else
/* 106 */         throw new InternalError("Could not create SecurityManager: " + str);
/*     */     }
/*     */   }
/*     */ 
/*     */   public ClassLoader getClassLoader()
/*     */   {
/* 116 */     return this.loader;
/*     */   }
/*     */ 
/*     */   public static URLClassPath getBootstrapClassPath()
/*     */   {
/* 393 */     return BootClassPathHolder.bcp;
/*     */   }
/*     */ 
/*     */   private static URL[] pathToURLs(File[] paramArrayOfFile) {
/* 397 */     URL[] arrayOfURL = new URL[paramArrayOfFile.length];
/* 398 */     for (int i = 0; i < paramArrayOfFile.length; i++) {
/* 399 */       arrayOfURL[i] = getFileURL(paramArrayOfFile[i]);
/*     */     }
/*     */ 
/* 405 */     return arrayOfURL;
/*     */   }
/*     */ 
/*     */   private static File[] getClassPath(String paramString)
/*     */   {
/*     */     Object localObject;
/* 410 */     if (paramString != null) {
/* 411 */       int i = 0; int j = 1;
/* 412 */       int k = 0; int m = 0;
/*     */ 
/* 414 */       while ((k = paramString.indexOf(File.pathSeparator, m)) != -1) {
/* 415 */         j++;
/* 416 */         m = k + 1;
/*     */       }
/* 418 */       localObject = new File[j];
/* 419 */       m = k = 0;
/*     */ 
/* 421 */       while ((k = paramString.indexOf(File.pathSeparator, m)) != -1) {
/* 422 */         if (k - m > 0) {
/* 423 */           localObject[(i++)] = new File(paramString.substring(m, k));
/*     */         }
/*     */         else {
/* 426 */           localObject[(i++)] = new File(".");
/*     */         }
/* 428 */         m = k + 1;
/*     */       }
/*     */ 
/* 431 */       if (m < paramString.length())
/* 432 */         localObject[(i++)] = new File(paramString.substring(m));
/*     */       else {
/* 434 */         localObject[(i++)] = new File(".");
/*     */       }
/*     */ 
/* 437 */       if (i != j) {
/* 438 */         File[] arrayOfFile = new File[i];
/* 439 */         System.arraycopy(localObject, 0, arrayOfFile, 0, i);
/* 440 */         localObject = arrayOfFile;
/*     */       }
/*     */     } else {
/* 443 */       localObject = new File[0];
/*     */     }
/*     */ 
/* 449 */     return localObject;
/*     */   }
/*     */ 
/*     */   static URL getFileURL(File paramFile)
/*     */   {
/*     */     try
/*     */     {
/* 456 */       paramFile = paramFile.getCanonicalFile();
/*     */     } catch (IOException localIOException) {
/*     */     }
/*     */     try {
/* 460 */       return ParseUtil.fileToEncodedURL(paramFile);
/*     */     } catch (MalformedURLException localMalformedURLException) {
/*     */     }
/* 463 */     throw new InternalError();
/*     */   }
/*     */ 
/*     */   static class AppClassLoader extends URLClassLoader
/*     */   {
/*     */     public static ClassLoader getAppClassLoader(final ClassLoader paramClassLoader)
/*     */       throws IOException
/*     */     {
/* 268 */       String str = System.getProperty("java.class.path");
/* 269 */       final File[] arrayOfFile = str == null ? new File[0] : Launcher.getClassPath(str);
/*     */ 
/* 278 */       return (ClassLoader)AccessController.doPrivileged(new PrivilegedAction()
/*     */       {
/*     */         public Launcher.AppClassLoader run() {
/* 281 */           URL[] arrayOfURL = this.val$s == null ? new URL[0] : Launcher.pathToURLs(arrayOfFile);
/*     */ 
/* 283 */           return new Launcher.AppClassLoader(arrayOfURL, paramClassLoader);
/*     */         }
/*     */       });
/*     */     }
/*     */ 
/*     */     AppClassLoader(URL[] paramArrayOfURL, ClassLoader paramClassLoader)
/*     */     {
/* 292 */       super(paramClassLoader, Launcher.factory);
/*     */     }
/*     */ 
/*     */     public Class loadClass(String paramString, boolean paramBoolean)
/*     */       throws ClassNotFoundException
/*     */     {
/* 301 */       int i = paramString.lastIndexOf('.');
/* 302 */       if (i != -1) {
/* 303 */         SecurityManager localSecurityManager = System.getSecurityManager();
/* 304 */         if (localSecurityManager != null) {
/* 305 */           localSecurityManager.checkPackageAccess(paramString.substring(0, i));
/*     */         }
/*     */       }
/* 308 */       return super.loadClass(paramString, paramBoolean);
/*     */     }
/*     */ 
/*     */     protected PermissionCollection getPermissions(CodeSource paramCodeSource)
/*     */     {
/* 316 */       PermissionCollection localPermissionCollection = super.getPermissions(paramCodeSource);
/* 317 */       localPermissionCollection.add(new RuntimePermission("exitVM"));
/* 318 */       return localPermissionCollection;
/*     */     }
/*     */ 
/*     */     private void appendToClassPathForInstrumentation(String paramString)
/*     */     {
/* 328 */       assert (Thread.holdsLock(this));
/*     */ 
/* 331 */       super.addURL(Launcher.getFileURL(new File(paramString)));
/*     */     }
/*     */ 
/*     */     private static AccessControlContext getContext(File[] paramArrayOfFile)
/*     */       throws MalformedURLException
/*     */     {
/* 344 */       PathPermissions localPathPermissions = new PathPermissions(paramArrayOfFile);
/*     */ 
/* 347 */       ProtectionDomain localProtectionDomain = new ProtectionDomain(new CodeSource(localPathPermissions.getCodeBase(), (Certificate[])null), localPathPermissions);
/*     */ 
/* 352 */       AccessControlContext localAccessControlContext = new AccessControlContext(new ProtectionDomain[] { localProtectionDomain });
/*     */ 
/* 355 */       return localAccessControlContext;
/*     */     }
/*     */ 
/*     */     static
/*     */     {
/* 262 */       ClassLoader.registerAsParallelCapable();
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class BootClassPathHolder
/*     */   {
/* 388 */     static final URLClassPath bcp = new URLClassPath(arrayOfURL, Launcher.factory);
/*     */ 
/*     */     static
/*     */     {
/*     */       URL[] arrayOfURL;
/* 363 */       if (Launcher.bootClassPath != null) {
/* 364 */         arrayOfURL = (URL[])AccessController.doPrivileged(new PrivilegedAction()
/*     */         {
/*     */           public URL[] run() {
/* 367 */             File[] arrayOfFile = Launcher.getClassPath(Launcher.bootClassPath);
/* 368 */             int i = arrayOfFile.length;
/* 369 */             HashSet localHashSet = new HashSet();
/* 370 */             for (int j = 0; j < i; j++) {
/* 371 */               File localFile = arrayOfFile[j];
/*     */ 
/* 374 */               if (!localFile.isDirectory()) {
/* 375 */                 localFile = localFile.getParentFile();
/*     */               }
/* 377 */               if ((localFile != null) && (localHashSet.add(localFile))) {
/* 378 */                 MetaIndex.registerDirectory(localFile);
/*     */               }
/*     */             }
/* 381 */             return Launcher.pathToURLs(arrayOfFile);
/*     */           }
/*     */         });
/*     */       }
/*     */       else
/* 386 */         arrayOfURL = new URL[0];
/*     */     }
/*     */   }
/*     */ 
/*     */   static class ExtClassLoader extends URLClassLoader
/*     */   {
/*     */     public static ExtClassLoader getExtClassLoader()
/*     */       throws IOException
/*     */     {
/* 134 */       File[] arrayOfFile = getExtDirs();
/*     */       try
/*     */       {
/* 141 */         return (ExtClassLoader)AccessController.doPrivileged(new PrivilegedExceptionAction()
/*     */         {
/*     */           public Launcher.ExtClassLoader run() throws IOException {
/* 144 */             int i = this.val$dirs.length;
/* 145 */             for (int j = 0; j < i; j++) {
/* 146 */               MetaIndex.registerDirectory(this.val$dirs[j]);
/*     */             }
/* 148 */             return new Launcher.ExtClassLoader(this.val$dirs);
/*     */           } } );
/*     */       }
/*     */       catch (PrivilegedActionException localPrivilegedActionException) {
/* 152 */         throw ((IOException)localPrivilegedActionException.getException());
/*     */       }
/*     */     }
/*     */ 
/*     */     void addExtURL(URL paramURL) {
/* 157 */       super.addURL(paramURL);
/*     */     }
/*     */ 
/*     */     public ExtClassLoader(File[] paramArrayOfFile)
/*     */       throws IOException
/*     */     {
/* 164 */       super(null, Launcher.factory);
/*     */     }
/*     */ 
/*     */     private static File[] getExtDirs() {
/* 168 */       String str = System.getProperty("java.ext.dirs");
/*     */       File[] arrayOfFile;
/* 170 */       if (str != null) {
/* 171 */         StringTokenizer localStringTokenizer = new StringTokenizer(str, File.pathSeparator);
/*     */ 
/* 173 */         int i = localStringTokenizer.countTokens();
/* 174 */         arrayOfFile = new File[i];
/* 175 */         for (int j = 0; j < i; j++)
/* 176 */           arrayOfFile[j] = new File(localStringTokenizer.nextToken());
/*     */       }
/*     */       else {
/* 179 */         arrayOfFile = new File[0];
/*     */       }
/* 181 */       return arrayOfFile;
/*     */     }
/*     */ 
/*     */     private static URL[] getExtURLs(File[] paramArrayOfFile) throws IOException {
/* 185 */       Vector localVector = new Vector();
/* 186 */       for (int i = 0; i < paramArrayOfFile.length; i++) {
/* 187 */         String[] arrayOfString = paramArrayOfFile[i].list();
/* 188 */         if (arrayOfString != null) {
/* 189 */           for (int j = 0; j < arrayOfString.length; j++) {
/* 190 */             if (!arrayOfString[j].equals("meta-index")) {
/* 191 */               File localFile = new File(paramArrayOfFile[i], arrayOfString[j]);
/* 192 */               localVector.add(Launcher.getFileURL(localFile));
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/* 197 */       URL[] arrayOfURL = new URL[localVector.size()];
/* 198 */       localVector.copyInto(arrayOfURL);
/* 199 */       return arrayOfURL;
/*     */     }
/*     */ 
/*     */     public String findLibrary(String paramString)
/*     */     {
/* 210 */       paramString = System.mapLibraryName(paramString);
/* 211 */       URL[] arrayOfURL = super.getURLs();
/* 212 */       Object localObject = null;
/* 213 */       for (int i = 0; i < arrayOfURL.length; i++)
/*     */       {
/* 215 */         File localFile1 = new File(arrayOfURL[i].getPath()).getParentFile();
/* 216 */         if ((localFile1 != null) && (!localFile1.equals(localObject)))
/*     */         {
/* 219 */           String str = VM.getSavedProperty("os.arch");
/* 220 */           if (str != null) {
/* 221 */             localFile2 = new File(new File(localFile1, str), paramString);
/* 222 */             if (localFile2.exists()) {
/* 223 */               return localFile2.getAbsolutePath();
/*     */             }
/*     */           }
/*     */ 
/* 227 */           File localFile2 = new File(localFile1, paramString);
/* 228 */           if (localFile2.exists()) {
/* 229 */             return localFile2.getAbsolutePath();
/*     */           }
/*     */         }
/* 232 */         localObject = localFile1;
/*     */       }
/* 234 */       return null;
/*     */     }
/*     */ 
/*     */     private static AccessControlContext getContext(File[] paramArrayOfFile)
/*     */       throws IOException
/*     */     {
/* 240 */       PathPermissions localPathPermissions = new PathPermissions(paramArrayOfFile);
/*     */ 
/* 243 */       ProtectionDomain localProtectionDomain = new ProtectionDomain(new CodeSource(localPathPermissions.getCodeBase(), (Certificate[])null), localPathPermissions);
/*     */ 
/* 248 */       AccessControlContext localAccessControlContext = new AccessControlContext(new ProtectionDomain[] { localProtectionDomain });
/*     */ 
/* 251 */       return localAccessControlContext;
/*     */     }
/*     */ 
/*     */     static
/*     */     {
/* 125 */       ClassLoader.registerAsParallelCapable();
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class Factory
/*     */     implements URLStreamHandlerFactory
/*     */   {
/* 471 */     private static String PREFIX = "sun.net.www.protocol";
/*     */ 
/*     */     public URLStreamHandler createURLStreamHandler(String paramString) {
/* 474 */       String str = PREFIX + "." + paramString + ".Handler";
/*     */       try {
/* 476 */         Class localClass = Class.forName(str);
/* 477 */         return (URLStreamHandler)localClass.newInstance();
/*     */       } catch (ClassNotFoundException localClassNotFoundException) {
/* 479 */         localClassNotFoundException.printStackTrace();
/*     */       } catch (InstantiationException localInstantiationException) {
/* 481 */         localInstantiationException.printStackTrace();
/*     */       } catch (IllegalAccessException localIllegalAccessException) {
/* 483 */         localIllegalAccessException.printStackTrace();
/*     */       }
/* 485 */       throw new InternalError("could not load " + paramString + "system protocol handler");
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.misc.Launcher
 * JD-Core Version:    0.6.2
 */