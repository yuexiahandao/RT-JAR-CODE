/*     */ package sun.misc;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.security.PrivilegedActionException;
/*     */ import java.security.PrivilegedExceptionAction;
/*     */ import java.util.Enumeration;
/*     */ import java.util.StringTokenizer;
/*     */ import java.util.Vector;
/*     */ import java.util.jar.Attributes;
/*     */ import java.util.jar.Attributes.Name;
/*     */ import java.util.jar.JarFile;
/*     */ import java.util.jar.Manifest;
/*     */ import sun.net.www.ParseUtil;
/*     */ import sun.security.action.GetPropertyAction;
/*     */ 
/*     */ public class ExtensionDependency
/*     */ {
/*     */   private static Vector providers;
/*     */   static final boolean DEBUG = false;
/*     */ 
/*     */   public static synchronized void addExtensionInstallationProvider(ExtensionInstallationProvider paramExtensionInstallationProvider)
/*     */   {
/*  85 */     if (providers == null) {
/*  86 */       providers = new Vector();
/*     */     }
/*  88 */     providers.add(paramExtensionInstallationProvider);
/*     */   }
/*     */ 
/*     */   public static synchronized void removeExtensionInstallationProvider(ExtensionInstallationProvider paramExtensionInstallationProvider)
/*     */   {
/*  99 */     providers.remove(paramExtensionInstallationProvider);
/*     */   }
/*     */ 
/*     */   public static boolean checkExtensionsDependencies(JarFile paramJarFile)
/*     */   {
/* 110 */     if (providers == null)
/*     */     {
/* 113 */       return true;
/*     */     }
/*     */     try
/*     */     {
/* 117 */       ExtensionDependency localExtensionDependency = new ExtensionDependency();
/* 118 */       return localExtensionDependency.checkExtensions(paramJarFile);
/*     */     } catch (ExtensionInstallationException localExtensionInstallationException) {
/* 120 */       debug(localExtensionInstallationException.getMessage());
/*     */     }
/* 122 */     return false;
/*     */   }
/*     */ 
/*     */   protected boolean checkExtensions(JarFile paramJarFile)
/*     */     throws ExtensionInstallationException
/*     */   {
/*     */     Manifest localManifest;
/*     */     try
/*     */     {
/* 134 */       localManifest = paramJarFile.getManifest();
/*     */     } catch (IOException localIOException) {
/* 136 */       return false;
/*     */     }
/*     */ 
/* 139 */     if (localManifest == null)
/*     */     {
/* 142 */       return true;
/*     */     }
/*     */ 
/* 145 */     boolean bool = true;
/* 146 */     Attributes localAttributes = localManifest.getMainAttributes();
/* 147 */     if (localAttributes != null)
/*     */     {
/* 149 */       String str1 = localAttributes.getValue(Attributes.Name.EXTENSION_LIST);
/* 150 */       if (str1 != null) {
/* 151 */         StringTokenizer localStringTokenizer = new StringTokenizer(str1);
/*     */ 
/* 153 */         while (localStringTokenizer.hasMoreTokens()) {
/* 154 */           String str2 = localStringTokenizer.nextToken();
/* 155 */           debug("The file " + paramJarFile.getName() + " appears to depend on " + str2);
/*     */ 
/* 158 */           String str3 = str2 + "-" + Attributes.Name.EXTENSION_NAME.toString();
/*     */ 
/* 160 */           if (localAttributes.getValue(str3) == null) {
/* 161 */             debug("The jar file " + paramJarFile.getName() + " appers to depend on " + str2 + " but does not define the " + str3 + " attribute in its manifest ");
/*     */           }
/* 167 */           else if (!checkExtension(str2, localAttributes)) {
/* 168 */             debug("Failed installing " + str2);
/* 169 */             bool = false;
/*     */           }
/*     */         }
/*     */       }
/*     */       else {
/* 174 */         debug("No dependencies for " + paramJarFile.getName());
/*     */       }
/*     */     }
/* 177 */     return bool;
/*     */   }
/*     */ 
/*     */   protected synchronized boolean checkExtension(String paramString, Attributes paramAttributes)
/*     */     throws ExtensionInstallationException
/*     */   {
/* 194 */     debug("Checking extension " + paramString);
/* 195 */     if (checkExtensionAgainstInstalled(paramString, paramAttributes)) {
/* 196 */       return true;
/*     */     }
/* 198 */     debug("Extension not currently installed ");
/* 199 */     ExtensionInfo localExtensionInfo = new ExtensionInfo(paramString, paramAttributes);
/* 200 */     return installExtension(localExtensionInfo, null);
/*     */   }
/*     */ 
/*     */   boolean checkExtensionAgainstInstalled(String paramString, Attributes paramAttributes)
/*     */     throws ExtensionInstallationException
/*     */   {
/* 217 */     File localFile = checkExtensionExists(paramString);
/*     */ 
/* 219 */     if (localFile != null)
/*     */     {
/*     */       try {
/* 222 */         if (checkExtensionAgainst(paramString, paramAttributes, localFile))
/* 223 */           return true;
/*     */       } catch (FileNotFoundException localFileNotFoundException1) {
/* 225 */         debugException(localFileNotFoundException1);
/*     */       } catch (IOException localIOException1) {
/* 227 */         debugException(localIOException1);
/*     */       }
/* 229 */       return false;
/*     */     }
/*     */ 
/*     */     File[] arrayOfFile;
/*     */     try
/*     */     {
/* 240 */       arrayOfFile = getInstalledExtensions();
/*     */     } catch (IOException localIOException2) {
/* 242 */       debugException(localIOException2);
/* 243 */       return false;
/*     */     }
/*     */ 
/* 246 */     for (int i = 0; i < arrayOfFile.length; i++) {
/*     */       try {
/* 248 */         if (checkExtensionAgainst(paramString, paramAttributes, arrayOfFile[i]))
/* 249 */           return true;
/*     */       } catch (FileNotFoundException localFileNotFoundException2) {
/* 251 */         debugException(localFileNotFoundException2);
/*     */       } catch (IOException localIOException3) {
/* 253 */         debugException(localIOException3);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 258 */     return false;
/*     */   }
/*     */ 
/*     */   protected boolean checkExtensionAgainst(String paramString, Attributes paramAttributes, final File paramFile)
/*     */     throws IOException, FileNotFoundException, ExtensionInstallationException
/*     */   {
/* 281 */     debug("Checking extension " + paramString + " against " + paramFile.getName());
/*     */     Manifest localManifest;
/*     */     try
/*     */     {
/* 287 */       localManifest = (Manifest)AccessController.doPrivileged(new PrivilegedExceptionAction()
/*     */       {
/*     */         public Manifest run() throws IOException, FileNotFoundException
/*     */         {
/* 291 */           if (!paramFile.exists())
/* 292 */             throw new FileNotFoundException(paramFile.getName());
/* 293 */           JarFile localJarFile = new JarFile(paramFile);
/* 294 */           return localJarFile.getManifest();
/*     */         } } );
/*     */     }
/*     */     catch (PrivilegedActionException localPrivilegedActionException) {
/* 298 */       if ((localPrivilegedActionException.getException() instanceof FileNotFoundException))
/* 299 */         throw ((FileNotFoundException)localPrivilegedActionException.getException());
/* 300 */       throw ((IOException)localPrivilegedActionException.getException());
/*     */     }
/*     */ 
/* 304 */     ExtensionInfo localExtensionInfo1 = new ExtensionInfo(paramString, paramAttributes);
/* 305 */     debug("Requested Extension : " + localExtensionInfo1);
/*     */ 
/* 307 */     int i = 4;
/* 308 */     ExtensionInfo localExtensionInfo2 = null;
/*     */ 
/* 310 */     if (localManifest != null) {
/* 311 */       Attributes localAttributes = localManifest.getMainAttributes();
/* 312 */       if (localAttributes != null) {
/* 313 */         localExtensionInfo2 = new ExtensionInfo(null, localAttributes);
/* 314 */         debug("Extension Installed " + localExtensionInfo2);
/* 315 */         i = localExtensionInfo2.isCompatibleWith(localExtensionInfo1);
/* 316 */         switch (i) {
/*     */         case 0:
/* 318 */           debug("Extensions are compatible");
/* 319 */           return true;
/*     */         case 4:
/* 322 */           debug("Extensions are incompatible");
/* 323 */           return false;
/*     */         }
/*     */ 
/* 327 */         debug("Extensions require an upgrade or vendor switch");
/* 328 */         return installExtension(localExtensionInfo1, localExtensionInfo2);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 333 */     return false;
/*     */   }
/*     */ 
/*     */   protected boolean installExtension(ExtensionInfo paramExtensionInfo1, ExtensionInfo paramExtensionInfo2)
/*     */     throws ExtensionInstallationException
/*     */   {
/*     */     Vector localVector;
/* 353 */     synchronized (providers) {
/* 354 */       localVector = (Vector)providers.clone();
/*     */     }
/* 356 */     for (??? = localVector.elements(); ((Enumeration)???).hasMoreElements(); ) {
/* 357 */       ExtensionInstallationProvider localExtensionInstallationProvider = (ExtensionInstallationProvider)((Enumeration)???).nextElement();
/*     */ 
/* 360 */       if (localExtensionInstallationProvider != null)
/*     */       {
/* 362 */         if (localExtensionInstallationProvider.installExtension(paramExtensionInfo1, paramExtensionInfo2)) {
/* 363 */           debug(paramExtensionInfo1.name + " installation successful");
/* 364 */           Launcher.ExtClassLoader localExtClassLoader = (Launcher.ExtClassLoader)Launcher.getLauncher().getClassLoader().getParent();
/*     */ 
/* 366 */           addNewExtensionsToClassLoader(localExtClassLoader);
/* 367 */           return true;
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 373 */     debug(paramExtensionInfo1.name + " installation failed");
/* 374 */     return false;
/*     */   }
/*     */ 
/*     */   private File checkExtensionExists(String paramString)
/*     */   {
/* 390 */     final String str = paramString;
/* 391 */     final String[] arrayOfString = { ".jar", ".zip" };
/*     */ 
/* 393 */     return (File)AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public File run()
/*     */       {
/*     */         try {
/* 398 */           File[] arrayOfFile = ExtensionDependency.access$000();
/*     */ 
/* 402 */           for (int i = 0; i < arrayOfFile.length; i++) {
/* 403 */             for (int j = 0; j < arrayOfString.length; j++)
/*     */             {
/*     */               File localFile;
/* 404 */               if (str.toLowerCase().endsWith(arrayOfString[j]))
/* 405 */                 localFile = new File(arrayOfFile[i], str);
/*     */               else {
/* 407 */                 localFile = new File(arrayOfFile[i], str + arrayOfString[j]);
/*     */               }
/* 409 */               ExtensionDependency.debug("checkExtensionExists:fileName " + localFile.getName());
/* 410 */               if (localFile.exists()) {
/* 411 */                 return localFile;
/*     */               }
/*     */             }
/*     */           }
/* 415 */           return null;
/*     */         }
/*     */         catch (Exception localException) {
/* 418 */           ExtensionDependency.this.debugException(localException);
/* 419 */         }return null;
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   private static File[] getExtDirs()
/*     */   {
/* 431 */     String str = (String)AccessController.doPrivileged(new GetPropertyAction("java.ext.dirs"));
/*     */     File[] arrayOfFile;
/* 435 */     if (str != null) {
/* 436 */       StringTokenizer localStringTokenizer = new StringTokenizer(str, File.pathSeparator);
/*     */ 
/* 438 */       int i = localStringTokenizer.countTokens();
/* 439 */       debug("getExtDirs count " + i);
/* 440 */       arrayOfFile = new File[i];
/* 441 */       for (int j = 0; j < i; j++) {
/* 442 */         arrayOfFile[j] = new File(localStringTokenizer.nextToken());
/* 443 */         debug("getExtDirs dirs[" + j + "] " + arrayOfFile[j]);
/*     */       }
/*     */     } else {
/* 446 */       arrayOfFile = new File[0];
/* 447 */       debug("getExtDirs dirs " + arrayOfFile);
/*     */     }
/* 449 */     debug("getExtDirs dirs.length " + arrayOfFile.length);
/* 450 */     return arrayOfFile;
/*     */   }
/*     */ 
/*     */   private static File[] getExtFiles(File[] paramArrayOfFile)
/*     */     throws IOException
/*     */   {
/* 462 */     Vector localVector = new Vector();
/* 463 */     for (int i = 0; i < paramArrayOfFile.length; i++) {
/* 464 */       String[] arrayOfString = paramArrayOfFile[i].list(new JarFilter());
/* 465 */       if (arrayOfString != null) {
/* 466 */         debug("getExtFiles files.length " + arrayOfString.length);
/* 467 */         for (int j = 0; j < arrayOfString.length; j++) {
/* 468 */           File localFile = new File(paramArrayOfFile[i], arrayOfString[j]);
/* 469 */           localVector.add(localFile);
/* 470 */           debug("getExtFiles f[" + j + "] " + localFile);
/*     */         }
/*     */       }
/*     */     }
/* 474 */     File[] arrayOfFile = new File[localVector.size()];
/* 475 */     localVector.copyInto(arrayOfFile);
/* 476 */     debug("getExtFiles ua.length " + arrayOfFile.length);
/* 477 */     return arrayOfFile;
/*     */   }
/*     */ 
/*     */   private File[] getInstalledExtensions()
/*     */     throws IOException
/*     */   {
/* 486 */     return (File[])AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public File[] run() {
/*     */         try {
/* 490 */           return ExtensionDependency.getExtFiles(ExtensionDependency.access$000());
/*     */         } catch (IOException localIOException) {
/* 492 */           ExtensionDependency.debug("Cannot get list of installed extensions");
/* 493 */           ExtensionDependency.this.debugException(localIOException);
/* 494 */         }return new File[0];
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   private Boolean addNewExtensionsToClassLoader(Launcher.ExtClassLoader paramExtClassLoader)
/*     */   {
/*     */     try
/*     */     {
/* 511 */       File[] arrayOfFile = getInstalledExtensions();
/* 512 */       for (int i = 0; i < arrayOfFile.length; i++) {
/* 513 */         final File localFile = arrayOfFile[i];
/* 514 */         URL localURL = (URL)AccessController.doPrivileged(new PrivilegedAction()
/*     */         {
/*     */           public URL run() {
/*     */             try {
/* 518 */               return ParseUtil.fileToEncodedURL(localFile);
/*     */             } catch (MalformedURLException localMalformedURLException) {
/* 520 */               ExtensionDependency.this.debugException(localMalformedURLException);
/* 521 */             }return null;
/*     */           }
/*     */         });
/* 525 */         if (localURL != null) {
/* 526 */           URL[] arrayOfURL = paramExtClassLoader.getURLs();
/* 527 */           int j = 0;
/* 528 */           for (int k = 0; k < arrayOfURL.length; k++) {
/* 529 */             debug("URL[" + k + "] is " + arrayOfURL[k] + " looking for " + localURL);
/*     */ 
/* 531 */             if (arrayOfURL[k].toString().compareToIgnoreCase(localURL.toString()) == 0)
/*     */             {
/* 533 */               j = 1;
/* 534 */               debug("Found !");
/*     */             }
/*     */           }
/* 537 */           if (j == 0) {
/* 538 */             debug("Not Found ! adding to the classloader " + localURL);
/*     */ 
/* 540 */             paramExtClassLoader.addExtURL(localURL);
/*     */           }
/*     */         }
/*     */       }
/*     */     } catch (MalformedURLException localMalformedURLException) {
/* 545 */       localMalformedURLException.printStackTrace();
/*     */     } catch (IOException localIOException) {
/* 547 */       localIOException.printStackTrace();
/*     */     }
/*     */ 
/* 550 */     return Boolean.TRUE;
/*     */   }
/*     */ 
/*     */   private static void debug(String paramString)
/*     */   {
/*     */   }
/*     */ 
/*     */   private void debugException(Throwable paramThrowable)
/*     */   {
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.misc.ExtensionDependency
 * JD-Core Version:    0.6.2
 */