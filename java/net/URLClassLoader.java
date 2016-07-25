/*     */ package java.net;
/*     */ 
/*     */ import java.io.Closeable;
/*     */ import java.io.File;
/*     */ import java.io.FilePermission;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.security.AccessControlContext;
/*     */ import java.security.AccessController;
/*     */ import java.security.CodeSigner;
/*     */ import java.security.CodeSource;
/*     */ import java.security.Permission;
/*     */ import java.security.PermissionCollection;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.security.PrivilegedActionException;
/*     */ import java.security.PrivilegedExceptionAction;
/*     */ import java.security.SecureClassLoader;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Set;
/*     */ import java.util.WeakHashMap;
/*     */ import java.util.jar.Attributes;
/*     */ import java.util.jar.Attributes.Name;
/*     */ import java.util.jar.JarFile;
/*     */ import java.util.jar.Manifest;
/*     */ import sun.misc.JavaNetAccess;
/*     */ import sun.misc.PerfCounter;
/*     */ import sun.misc.Resource;
/*     */ import sun.misc.SharedSecrets;
/*     */ import sun.misc.URLClassPath;
/*     */ import sun.net.www.ParseUtil;
/*     */ import sun.net.www.protocol.file.FileURLConnection;
/*     */ 
/*     */ public class URLClassLoader extends SecureClassLoader
/*     */   implements Closeable
/*     */ {
/*     */   private final URLClassPath ucp;
/*     */   private final AccessControlContext acc;
/* 207 */   private WeakHashMap<Closeable, Void> closeables = new WeakHashMap();
/*     */ 
/*     */   public URLClassLoader(URL[] paramArrayOfURL, ClassLoader paramClassLoader)
/*     */   {
/*  97 */     super(paramClassLoader);
/*     */ 
/*  99 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 100 */     if (localSecurityManager != null) {
/* 101 */       localSecurityManager.checkCreateClassLoader();
/*     */     }
/* 103 */     this.ucp = new URLClassPath(paramArrayOfURL);
/* 104 */     this.acc = AccessController.getContext();
/*     */   }
/*     */ 
/*     */   URLClassLoader(URL[] paramArrayOfURL, ClassLoader paramClassLoader, AccessControlContext paramAccessControlContext)
/*     */   {
/* 109 */     super(paramClassLoader);
/*     */ 
/* 111 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 112 */     if (localSecurityManager != null) {
/* 113 */       localSecurityManager.checkCreateClassLoader();
/*     */     }
/* 115 */     this.ucp = new URLClassPath(paramArrayOfURL);
/* 116 */     this.acc = paramAccessControlContext;
/*     */   }
/*     */ 
/*     */   public URLClassLoader(URL[] paramArrayOfURL)
/*     */   {
/* 142 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 143 */     if (localSecurityManager != null) {
/* 144 */       localSecurityManager.checkCreateClassLoader();
/*     */     }
/* 146 */     this.ucp = new URLClassPath(paramArrayOfURL);
/* 147 */     this.acc = AccessController.getContext();
/*     */   }
/*     */ 
/*     */   URLClassLoader(URL[] paramArrayOfURL, AccessControlContext paramAccessControlContext)
/*     */   {
/* 153 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 154 */     if (localSecurityManager != null) {
/* 155 */       localSecurityManager.checkCreateClassLoader();
/*     */     }
/* 157 */     this.ucp = new URLClassPath(paramArrayOfURL);
/* 158 */     this.acc = paramAccessControlContext;
/*     */   }
/*     */ 
/*     */   public URLClassLoader(URL[] paramArrayOfURL, ClassLoader paramClassLoader, URLStreamHandlerFactory paramURLStreamHandlerFactory)
/*     */   {
/* 183 */     super(paramClassLoader);
/*     */ 
/* 185 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 186 */     if (localSecurityManager != null) {
/* 187 */       localSecurityManager.checkCreateClassLoader();
/*     */     }
/* 189 */     this.ucp = new URLClassPath(paramArrayOfURL, paramURLStreamHandlerFactory);
/* 190 */     this.acc = AccessController.getContext();
/*     */   }
/*     */ 
/*     */   public InputStream getResourceAsStream(String paramString)
/*     */   {
/* 227 */     URL localURL = getResource(paramString);
/*     */     try {
/* 229 */       if (localURL == null) {
/* 230 */         return null;
/*     */       }
/* 232 */       URLConnection localURLConnection = localURL.openConnection();
/* 233 */       InputStream localInputStream = localURLConnection.getInputStream();
/* 234 */       if ((localURLConnection instanceof JarURLConnection)) {
/* 235 */         JarURLConnection localJarURLConnection = (JarURLConnection)localURLConnection;
/* 236 */         JarFile localJarFile = localJarURLConnection.getJarFile();
/* 237 */         synchronized (this.closeables) {
/* 238 */           if (!this.closeables.containsKey(localJarFile))
/* 239 */             this.closeables.put(localJarFile, null);
/*     */         }
/*     */       }
/* 242 */       else if ((localURLConnection instanceof FileURLConnection)) {
/* 243 */         synchronized (this.closeables) {
/* 244 */           this.closeables.put(localInputStream, null);
/*     */         }
/*     */       }
/* 247 */       return localInputStream; } catch (IOException localIOException) {
/*     */     }
/* 249 */     return null;
/*     */   }
/*     */ 
/*     */   public void close()
/*     */     throws IOException
/*     */   {
/* 282 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 283 */     if (localSecurityManager != null) {
/* 284 */       localSecurityManager.checkPermission(new RuntimePermission("closeClassLoader"));
/*     */     }
/* 286 */     List localList = this.ucp.closeLoaders();
/*     */     Object localObject2;
/* 290 */     synchronized (this.closeables) {
/* 291 */       localObject1 = this.closeables.keySet();
/* 292 */       for (localObject2 = ((Set)localObject1).iterator(); ((Iterator)localObject2).hasNext(); ) { Closeable localCloseable = (Closeable)((Iterator)localObject2).next();
/*     */         try {
/* 294 */           localCloseable.close();
/*     */         } catch (IOException localIOException) {
/* 296 */           localList.add(localIOException);
/*     */         }
/*     */       }
/* 299 */       this.closeables.clear();
/*     */     }
/*     */ 
/* 302 */     if (localList.isEmpty()) {
/* 303 */       return;
/*     */     }
/*     */ 
/* 306 */     ??? = (IOException)localList.remove(0);
/*     */ 
/* 310 */     for (Object localObject1 = localList.iterator(); ((Iterator)localObject1).hasNext(); ) { localObject2 = (IOException)((Iterator)localObject1).next();
/* 311 */       ((IOException)???).addSuppressed((Throwable)localObject2);
/*     */     }
/* 313 */     throw ((Throwable)???);
/*     */   }
/*     */ 
/*     */   protected void addURL(URL paramURL)
/*     */   {
/* 327 */     this.ucp.addURL(paramURL);
/*     */   }
/*     */ 
/*     */   public URL[] getURLs()
/*     */   {
/* 337 */     return this.ucp.getURLs();
/*     */   }
/*     */ 
/*     */   protected Class<?> findClass(final String paramString)
/*     */     throws ClassNotFoundException
/*     */   {
/*     */     try
/*     */     {
/* 354 */       return (Class)AccessController.doPrivileged(new PrivilegedExceptionAction()
/*     */       {
/*     */         public Class run() throws ClassNotFoundException {
/* 357 */           String str = paramString.replace('.', '/').concat(".class");
/* 358 */           Resource localResource = URLClassLoader.this.ucp.getResource(str, false);
/* 359 */           if (localResource != null) {
/*     */             try {
/* 361 */               return URLClassLoader.this.defineClass(paramString, localResource);
/*     */             } catch (IOException localIOException) {
/* 363 */               throw new ClassNotFoundException(paramString, localIOException);
/*     */             }
/*     */           }
/* 366 */           throw new ClassNotFoundException(paramString);
/*     */         }
/*     */       }
/*     */       , this.acc);
/*     */     }
/*     */     catch (PrivilegedActionException localPrivilegedActionException)
/*     */     {
/* 371 */       throw ((ClassNotFoundException)localPrivilegedActionException.getException());
/*     */     }
/*     */   }
/*     */ 
/*     */   private Package getAndVerifyPackage(String paramString, Manifest paramManifest, URL paramURL)
/*     */   {
/* 382 */     Package localPackage = getPackage(paramString);
/* 383 */     if (localPackage != null)
/*     */     {
/* 385 */       if (localPackage.isSealed())
/*     */       {
/* 387 */         if (!localPackage.isSealed(paramURL)) {
/* 388 */           throw new SecurityException("sealing violation: package " + paramString + " is sealed");
/*     */         }
/*     */ 
/*     */       }
/* 394 */       else if ((paramManifest != null) && (isSealed(paramString, paramManifest))) {
/* 395 */         throw new SecurityException("sealing violation: can't seal package " + paramString + ": already loaded");
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 401 */     return localPackage;
/*     */   }
/*     */ 
/*     */   private Class defineClass(String paramString, Resource paramResource)
/*     */     throws IOException
/*     */   {
/* 410 */     long l = System.nanoTime();
/* 411 */     int i = paramString.lastIndexOf('.');
/* 412 */     URL localURL = paramResource.getCodeSourceURL();
/* 413 */     if (i != -1) {
/* 414 */       localObject1 = paramString.substring(0, i);
/*     */ 
/* 416 */       localObject2 = paramResource.getManifest();
/* 417 */       if (getAndVerifyPackage((String)localObject1, (Manifest)localObject2, localURL) == null) {
/*     */         try {
/* 419 */           if (localObject2 != null)
/* 420 */             definePackage((String)localObject1, (Manifest)localObject2, localURL);
/*     */           else {
/* 422 */             definePackage((String)localObject1, null, null, null, null, null, null, null);
/*     */           }
/*     */         }
/*     */         catch (IllegalArgumentException localIllegalArgumentException)
/*     */         {
/* 427 */           if (getAndVerifyPackage((String)localObject1, (Manifest)localObject2, localURL) == null)
/*     */           {
/* 429 */             throw new AssertionError("Cannot find package " + (String)localObject1);
/*     */           }
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 436 */     Object localObject1 = paramResource.getByteBuffer();
/* 437 */     if (localObject1 != null)
/*     */     {
/* 439 */       localObject2 = paramResource.getCodeSigners();
/* 440 */       localObject3 = new CodeSource(localURL, (CodeSigner[])localObject2);
/* 441 */       PerfCounter.getReadClassBytesTime().addElapsedTimeFrom(l);
/* 442 */       return defineClass(paramString, (ByteBuffer)localObject1, (CodeSource)localObject3);
/*     */     }
/* 444 */     Object localObject2 = paramResource.getBytes();
/*     */ 
/* 446 */     Object localObject3 = paramResource.getCodeSigners();
/* 447 */     CodeSource localCodeSource = new CodeSource(localURL, (CodeSigner[])localObject3);
/* 448 */     PerfCounter.getReadClassBytesTime().addElapsedTimeFrom(l);
/* 449 */     return defineClass(paramString, (byte[])localObject2, 0, localObject2.length, localCodeSource);
/*     */   }
/*     */ 
/*     */   protected Package definePackage(String paramString, Manifest paramManifest, URL paramURL)
/*     */     throws IllegalArgumentException
/*     */   {
/* 471 */     String str1 = paramString.replace('.', '/').concat("/");
/* 472 */     String str2 = null; String str3 = null; String str4 = null;
/* 473 */     String str5 = null; String str6 = null; String str7 = null;
/* 474 */     String str8 = null;
/* 475 */     URL localURL = null;
/*     */ 
/* 477 */     Attributes localAttributes = paramManifest.getAttributes(str1);
/* 478 */     if (localAttributes != null) {
/* 479 */       str2 = localAttributes.getValue(Attributes.Name.SPECIFICATION_TITLE);
/* 480 */       str3 = localAttributes.getValue(Attributes.Name.SPECIFICATION_VERSION);
/* 481 */       str4 = localAttributes.getValue(Attributes.Name.SPECIFICATION_VENDOR);
/* 482 */       str5 = localAttributes.getValue(Attributes.Name.IMPLEMENTATION_TITLE);
/* 483 */       str6 = localAttributes.getValue(Attributes.Name.IMPLEMENTATION_VERSION);
/* 484 */       str7 = localAttributes.getValue(Attributes.Name.IMPLEMENTATION_VENDOR);
/* 485 */       str8 = localAttributes.getValue(Attributes.Name.SEALED);
/*     */     }
/* 487 */     localAttributes = paramManifest.getMainAttributes();
/* 488 */     if (localAttributes != null) {
/* 489 */       if (str2 == null) {
/* 490 */         str2 = localAttributes.getValue(Attributes.Name.SPECIFICATION_TITLE);
/*     */       }
/* 492 */       if (str3 == null) {
/* 493 */         str3 = localAttributes.getValue(Attributes.Name.SPECIFICATION_VERSION);
/*     */       }
/* 495 */       if (str4 == null) {
/* 496 */         str4 = localAttributes.getValue(Attributes.Name.SPECIFICATION_VENDOR);
/*     */       }
/* 498 */       if (str5 == null) {
/* 499 */         str5 = localAttributes.getValue(Attributes.Name.IMPLEMENTATION_TITLE);
/*     */       }
/* 501 */       if (str6 == null) {
/* 502 */         str6 = localAttributes.getValue(Attributes.Name.IMPLEMENTATION_VERSION);
/*     */       }
/* 504 */       if (str7 == null) {
/* 505 */         str7 = localAttributes.getValue(Attributes.Name.IMPLEMENTATION_VENDOR);
/*     */       }
/* 507 */       if (str8 == null) {
/* 508 */         str8 = localAttributes.getValue(Attributes.Name.SEALED);
/*     */       }
/*     */     }
/* 511 */     if ("true".equalsIgnoreCase(str8)) {
/* 512 */       localURL = paramURL;
/*     */     }
/* 514 */     return definePackage(paramString, str2, str3, str4, str5, str6, str7, localURL);
/*     */   }
/*     */ 
/*     */   private boolean isSealed(String paramString, Manifest paramManifest)
/*     */   {
/* 523 */     String str1 = paramString.replace('.', '/').concat("/");
/* 524 */     Attributes localAttributes = paramManifest.getAttributes(str1);
/* 525 */     String str2 = null;
/* 526 */     if (localAttributes != null) {
/* 527 */       str2 = localAttributes.getValue(Attributes.Name.SEALED);
/*     */     }
/* 529 */     if ((str2 == null) && 
/* 530 */       ((localAttributes = paramManifest.getMainAttributes()) != null)) {
/* 531 */       str2 = localAttributes.getValue(Attributes.Name.SEALED);
/*     */     }
/*     */ 
/* 534 */     return "true".equalsIgnoreCase(str2);
/*     */   }
/*     */ 
/*     */   public URL findResource(final String paramString)
/*     */   {
/* 548 */     URL localURL = (URL)AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public URL run() {
/* 551 */         return URLClassLoader.this.ucp.findResource(paramString, true);
/*     */       }
/*     */     }
/*     */     , this.acc);
/*     */ 
/* 555 */     return localURL != null ? this.ucp.checkURL(localURL) : null;
/*     */   }
/*     */ 
/*     */   public Enumeration<URL> findResources(String paramString)
/*     */     throws IOException
/*     */   {
/* 570 */     final Enumeration localEnumeration = this.ucp.findResources(paramString, true);
/*     */ 
/* 572 */     return new Enumeration() {
/* 573 */       private URL url = null;
/*     */ 
/*     */       private boolean next() {
/* 576 */         if (this.url != null)
/* 577 */           return true;
/*     */         do
/*     */         {
/* 580 */           URL localURL = (URL)AccessController.doPrivileged(new PrivilegedAction()
/*     */           {
/*     */             public URL run() {
/* 583 */               if (!URLClassLoader.3.this.val$e.hasMoreElements())
/* 584 */                 return null;
/* 585 */               return (URL)URLClassLoader.3.this.val$e.nextElement();
/*     */             }
/*     */           }
/*     */           , URLClassLoader.this.acc);
/*     */ 
/* 588 */           if (localURL == null)
/*     */             break;
/* 590 */           this.url = URLClassLoader.this.ucp.checkURL(localURL);
/* 591 */         }while (this.url == null);
/* 592 */         return this.url != null;
/*     */       }
/*     */ 
/*     */       public URL nextElement() {
/* 596 */         if (!next()) {
/* 597 */           throw new NoSuchElementException();
/*     */         }
/* 599 */         URL localURL = this.url;
/* 600 */         this.url = null;
/* 601 */         return localURL;
/*     */       }
/*     */ 
/*     */       public boolean hasMoreElements() {
/* 605 */         return next();
/*     */       }
/*     */     };
/*     */   }
/*     */ 
/*     */   protected PermissionCollection getPermissions(CodeSource paramCodeSource)
/*     */   {
/* 635 */     PermissionCollection localPermissionCollection = super.getPermissions(paramCodeSource);
/*     */ 
/* 637 */     URL localURL = paramCodeSource.getLocation();
/*     */     URLConnection localURLConnection;
/*     */     Object localObject1;
/*     */     try
/*     */     {
/* 643 */       localURLConnection = localURL.openConnection();
/* 644 */       localObject1 = localURLConnection.getPermission();
/*     */     } catch (IOException localIOException) {
/* 646 */       localObject1 = null;
/* 647 */       localURLConnection = null;
/*     */     }
/*     */     Object localObject2;
/*     */     Object localObject3;
/* 650 */     if ((localObject1 instanceof FilePermission))
/*     */     {
/* 654 */       localObject2 = ((Permission)localObject1).getName();
/* 655 */       if (((String)localObject2).endsWith(File.separator)) {
/* 656 */         localObject2 = (String)localObject2 + "-";
/* 657 */         localObject1 = new FilePermission((String)localObject2, "read");
/*     */       }
/* 659 */     } else if ((localObject1 == null) && (localURL.getProtocol().equals("file"))) {
/* 660 */       localObject2 = localURL.getFile().replace('/', File.separatorChar);
/* 661 */       localObject2 = ParseUtil.decode((String)localObject2);
/* 662 */       if (((String)localObject2).endsWith(File.separator))
/* 663 */         localObject2 = (String)localObject2 + "-";
/* 664 */       localObject1 = new FilePermission((String)localObject2, "read");
/*     */     }
/*     */     else
/*     */     {
/* 671 */       localObject2 = localURL;
/* 672 */       if ((localURLConnection instanceof JarURLConnection)) {
/* 673 */         localObject2 = ((JarURLConnection)localURLConnection).getJarFileURL();
/*     */       }
/* 675 */       localObject3 = ((URL)localObject2).getHost();
/* 676 */       if ((localObject3 != null) && (((String)localObject3).length() > 0)) {
/* 677 */         localObject1 = new SocketPermission((String)localObject3, "connect,accept");
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 684 */     if (localObject1 != null) {
/* 685 */       localObject2 = System.getSecurityManager();
/* 686 */       if (localObject2 != null) {
/* 687 */         localObject3 = localObject1;
/* 688 */         AccessController.doPrivileged(new PrivilegedAction() {
/*     */           public Void run() throws SecurityException {
/* 690 */             this.val$sm.checkPermission(this.val$fp);
/* 691 */             return null;
/*     */           }
/*     */         }
/*     */         , this.acc);
/*     */       }
/*     */ 
/* 695 */       localPermissionCollection.add((Permission)localObject1);
/*     */     }
/* 697 */     return localPermissionCollection;
/*     */   }
/*     */ 
/*     */   public static URLClassLoader newInstance(URL[] paramArrayOfURL, final ClassLoader paramClassLoader)
/*     */   {
/* 715 */     final AccessControlContext localAccessControlContext = AccessController.getContext();
/*     */ 
/* 717 */     URLClassLoader localURLClassLoader = (URLClassLoader)AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public URLClassLoader run() {
/* 720 */         return new FactoryURLClassLoader(this.val$urls, paramClassLoader, localAccessControlContext);
/*     */       }
/*     */     });
/* 723 */     return localURLClassLoader;
/*     */   }
/*     */ 
/*     */   public static URLClassLoader newInstance(URL[] paramArrayOfURL)
/*     */   {
/* 739 */     final AccessControlContext localAccessControlContext = AccessController.getContext();
/*     */ 
/* 741 */     URLClassLoader localURLClassLoader = (URLClassLoader)AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public URLClassLoader run() {
/* 744 */         return new FactoryURLClassLoader(this.val$urls, localAccessControlContext);
/*     */       }
/*     */     });
/* 747 */     return localURLClassLoader;
/*     */   }
/*     */ 
/*     */   static {
/* 751 */     SharedSecrets.setJavaNetAccess(new JavaNetAccess()
/*     */     {
/*     */       public URLClassPath getURLClassPath(URLClassLoader paramAnonymousURLClassLoader) {
/* 754 */         return paramAnonymousURLClassLoader.ucp;
/*     */       }
/*     */     });
/* 758 */     ClassLoader.registerAsParallelCapable();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.net.URLClassLoader
 * JD-Core Version:    0.6.2
 */