/*     */ package sun.applet;
/*     */ 
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.File;
/*     */ import java.io.FilePermission;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.net.HttpURLConnection;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.net.URLClassLoader;
/*     */ import java.net.URLConnection;
/*     */ import java.security.AccessControlContext;
/*     */ import java.security.AccessController;
/*     */ import java.security.CodeSource;
/*     */ import java.security.Permission;
/*     */ import java.security.PermissionCollection;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.security.PrivilegedActionException;
/*     */ import java.security.PrivilegedExceptionAction;
/*     */ import java.security.cert.Certificate;
/*     */ import java.util.Enumeration;
/*     */ import java.util.HashMap;
/*     */ import java.util.NoSuchElementException;
/*     */ import sun.awt.AppContext;
/*     */ import sun.misc.IOUtils;
/*     */ import sun.net.www.ParseUtil;
/*     */ 
/*     */ public class AppletClassLoader extends URLClassLoader
/*     */ {
/*     */   private URL base;
/*     */   private CodeSource codesource;
/*     */   private AccessControlContext acc;
/*  67 */   private boolean exceptionStatus = false;
/*     */ 
/*  69 */   private final Object threadGroupSynchronizer = new Object();
/*  70 */   private final Object grabReleaseSynchronizer = new Object();
/*     */ 
/*  72 */   private boolean codebaseLookup = true;
/*  73 */   private volatile boolean allowRecursiveDirectoryRead = true;
/*     */ 
/* 343 */   private Object syncResourceAsStream = new Object();
/* 344 */   private Object syncResourceAsStreamFromJar = new Object();
/*     */ 
/* 347 */   private boolean resourceAsStreamInCall = false;
/* 348 */   private boolean resourceAsStreamFromJarInCall = false;
/*     */   private AppletThreadGroup threadGroup;
/*     */   private AppContext appContext;
/* 690 */   int usageCount = 0;
/*     */ 
/* 772 */   private HashMap jdk11AppletInfo = new HashMap();
/* 773 */   private HashMap jdk12AppletInfo = new HashMap();
/*     */ 
/* 825 */   private static AppletMessageHandler mh = new AppletMessageHandler("appletclassloader");
/*     */ 
/*     */   protected AppletClassLoader(URL paramURL)
/*     */   {
/*  79 */     super(new URL[0]);
/*  80 */     this.base = paramURL;
/*  81 */     this.codesource = new CodeSource(paramURL, (Certificate[])null);
/*     */ 
/*  83 */     this.acc = AccessController.getContext();
/*     */   }
/*     */ 
/*     */   public void disableRecursiveDirectoryRead() {
/*  87 */     this.allowRecursiveDirectoryRead = false;
/*     */   }
/*     */ 
/*     */   void setCodebaseLookup(boolean paramBoolean)
/*     */   {
/*  95 */     this.codebaseLookup = paramBoolean;
/*     */   }
/*     */ 
/*     */   URL getBaseURL()
/*     */   {
/* 102 */     return this.base;
/*     */   }
/*     */ 
/*     */   public URL[] getURLs()
/*     */   {
/* 109 */     URL[] arrayOfURL1 = super.getURLs();
/* 110 */     URL[] arrayOfURL2 = new URL[arrayOfURL1.length + 1];
/* 111 */     System.arraycopy(arrayOfURL1, 0, arrayOfURL2, 0, arrayOfURL1.length);
/* 112 */     arrayOfURL2[(arrayOfURL2.length - 1)] = this.base;
/* 113 */     return arrayOfURL2;
/*     */   }
/*     */ 
/*     */   protected void addJar(String paramString)
/*     */     throws IOException
/*     */   {
/*     */     URL localURL;
/*     */     try
/*     */     {
/* 124 */       localURL = new URL(this.base, paramString);
/*     */     } catch (MalformedURLException localMalformedURLException) {
/* 126 */       throw new IllegalArgumentException("name");
/*     */     }
/* 128 */     addURL(localURL);
/*     */   }
/*     */ 
/*     */   public synchronized Class loadClass(String paramString, boolean paramBoolean)
/*     */     throws ClassNotFoundException
/*     */   {
/* 145 */     int i = paramString.lastIndexOf('.');
/* 146 */     if (i != -1) {
/* 147 */       SecurityManager localSecurityManager = System.getSecurityManager();
/* 148 */       if (localSecurityManager != null)
/* 149 */         localSecurityManager.checkPackageAccess(paramString.substring(0, i));
/*     */     }
/*     */     try {
/* 152 */       return super.loadClass(paramString, paramBoolean);
/*     */     }
/*     */     catch (ClassNotFoundException localClassNotFoundException) {
/* 155 */       throw localClassNotFoundException;
/*     */     }
/*     */     catch (RuntimeException localRuntimeException) {
/* 158 */       throw localRuntimeException;
/*     */     }
/*     */     catch (Error localError) {
/* 161 */       throw localError;
/*     */     }
/*     */   }
/*     */ 
/*     */   protected Class findClass(String paramString)
/*     */     throws ClassNotFoundException
/*     */   {
/* 171 */     int i = paramString.indexOf(";");
/* 172 */     String str1 = "";
/* 173 */     if (i != -1) {
/* 174 */       str1 = paramString.substring(i, paramString.length());
/* 175 */       paramString = paramString.substring(0, i);
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 180 */       return super.findClass(paramString);
/*     */     }
/*     */     catch (ClassNotFoundException localClassNotFoundException)
/*     */     {
/* 188 */       if (!this.codebaseLookup) {
/* 189 */         throw new ClassNotFoundException(paramString);
/*     */       }
/*     */ 
/* 192 */       String str2 = ParseUtil.encodePath(paramString.replace('.', '/'), false);
/* 193 */       final String str3 = str2 + ".class" + str1;
/*     */       try {
/* 195 */         byte[] arrayOfByte = (byte[])AccessController.doPrivileged(new PrivilegedExceptionAction()
/*     */         {
/*     */           public Object run() throws IOException {
/*     */             try {
/* 199 */               URL localURL = new URL(AppletClassLoader.this.base, str3);
/*     */ 
/* 202 */               if ((AppletClassLoader.this.base.getProtocol().equals(localURL.getProtocol())) && (AppletClassLoader.this.base.getHost().equals(localURL.getHost())) && (AppletClassLoader.this.base.getPort() == localURL.getPort()))
/*     */               {
/* 205 */                 return AppletClassLoader.getBytes(localURL);
/*     */               }
/*     */ 
/* 208 */               return null;
/*     */             } catch (Exception localException) {
/*     */             }
/* 211 */             return null;
/*     */           }
/*     */         }
/*     */         , this.acc);
/*     */ 
/* 216 */         if (arrayOfByte != null) {
/* 217 */           return defineClass(paramString, arrayOfByte, 0, arrayOfByte.length, this.codesource);
/*     */         }
/* 219 */         throw new ClassNotFoundException(paramString);
/*     */       }
/*     */       catch (PrivilegedActionException localPrivilegedActionException) {
/* 222 */         throw new ClassNotFoundException(paramString, localPrivilegedActionException.getException());
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   protected PermissionCollection getPermissions(CodeSource paramCodeSource)
/*     */   {
/* 246 */     PermissionCollection localPermissionCollection = super.getPermissions(paramCodeSource);
/*     */ 
/* 248 */     URL localURL = paramCodeSource.getLocation();
/*     */ 
/* 250 */     String str1 = null;
/*     */     Permission localPermission1;
/*     */     try
/*     */     {
/* 254 */       localPermission1 = localURL.openConnection().getPermission();
/*     */     } catch (IOException localIOException1) {
/* 256 */       localPermission1 = null;
/*     */     }
/*     */ 
/* 259 */     if ((localPermission1 instanceof FilePermission)) {
/* 260 */       str1 = localPermission1.getName();
/* 261 */     } else if ((localPermission1 == null) && (localURL.getProtocol().equals("file"))) {
/* 262 */       str1 = localURL.getFile().replace('/', File.separatorChar);
/* 263 */       str1 = ParseUtil.decode(str1);
/*     */     }
/*     */ 
/* 266 */     if (str1 != null) {
/* 267 */       String str2 = str1;
/* 268 */       if (!str1.endsWith(File.separator)) {
/* 269 */         int i = str1.lastIndexOf(File.separatorChar);
/* 270 */         if (i != -1) {
/* 271 */           str1 = str1.substring(0, i + 1) + "-";
/* 272 */           localPermissionCollection.add(new FilePermission(str1, "read"));
/*     */         }
/*     */       }
/*     */ 
/* 276 */       File localFile = new File(str2);
/* 277 */       boolean bool = localFile.isDirectory();
/*     */ 
/* 282 */       if ((this.allowRecursiveDirectoryRead) && ((bool) || (str2.toLowerCase().endsWith(".jar")) || (str2.toLowerCase().endsWith(".zip"))))
/*     */       {
/*     */         Permission localPermission2;
/*     */         try
/*     */         {
/* 288 */           localPermission2 = this.base.openConnection().getPermission();
/*     */         } catch (IOException localIOException2) {
/* 290 */           localPermission2 = null;
/*     */         }
/*     */         String str3;
/* 292 */         if ((localPermission2 instanceof FilePermission)) {
/* 293 */           str3 = localPermission2.getName();
/* 294 */           if (str3.endsWith(File.separator)) {
/* 295 */             str3 = str3 + "-";
/*     */           }
/* 297 */           localPermissionCollection.add(new FilePermission(str3, "read"));
/*     */         }
/* 299 */         else if ((localPermission2 == null) && (this.base.getProtocol().equals("file"))) {
/* 300 */           str3 = this.base.getFile().replace('/', File.separatorChar);
/* 301 */           str3 = ParseUtil.decode(str3);
/* 302 */           if (str3.endsWith(File.separator)) {
/* 303 */             str3 = str3 + "-";
/*     */           }
/* 305 */           localPermissionCollection.add(new FilePermission(str3, "read"));
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 310 */     return localPermissionCollection;
/*     */   }
/*     */ 
/*     */   private static byte[] getBytes(URL paramURL)
/*     */     throws IOException
/*     */   {
/* 317 */     URLConnection localURLConnection = paramURL.openConnection();
/* 318 */     if ((localURLConnection instanceof HttpURLConnection)) {
/* 319 */       HttpURLConnection localHttpURLConnection = (HttpURLConnection)localURLConnection;
/* 320 */       int j = localHttpURLConnection.getResponseCode();
/* 321 */       if (j >= 400) {
/* 322 */         throw new IOException("open HTTP connection failed.");
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 325 */     int i = localURLConnection.getContentLength();
/*     */ 
/* 331 */     BufferedInputStream localBufferedInputStream = new BufferedInputStream(localURLConnection.getInputStream());
/*     */     byte[] arrayOfByte;
/*     */     try
/*     */     {
/* 335 */       arrayOfByte = IOUtils.readFully(localBufferedInputStream, i, true);
/*     */     } finally {
/* 337 */       localBufferedInputStream.close();
/*     */     }
/* 339 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */   public InputStream getResourceAsStream(String paramString)
/*     */   {
/* 364 */     if (paramString == null) {
/* 365 */       throw new NullPointerException("name");
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 370 */       InputStream localInputStream = null;
/*     */ 
/* 380 */       synchronized (this.syncResourceAsStream)
/*     */       {
/* 382 */         this.resourceAsStreamInCall = true;
/*     */ 
/* 385 */         localInputStream = super.getResourceAsStream(paramString);
/*     */ 
/* 387 */         this.resourceAsStreamInCall = false;
/*     */       }
/*     */ 
/* 392 */       if ((this.codebaseLookup == true) && (localInputStream == null))
/*     */       {
/* 396 */         ??? = new URL(this.base, ParseUtil.encodePath(paramString, false));
/* 397 */       }return ((URL)???).openStream();
/*     */     }
/*     */     catch (Exception localException)
/*     */     {
/*     */     }
/*     */ 
/* 404 */     return null;
/*     */   }
/*     */ 
/*     */   public InputStream getResourceAsStreamFromJar(String paramString)
/*     */   {
/* 423 */     if (paramString == null) {
/* 424 */       throw new NullPointerException("name");
/*     */     }
/*     */     try
/*     */     {
/* 428 */       InputStream localInputStream = null;
/* 429 */       synchronized (this.syncResourceAsStreamFromJar) {
/* 430 */         this.resourceAsStreamFromJarInCall = true;
/*     */ 
/* 432 */         localInputStream = super.getResourceAsStream(paramString);
/* 433 */         this.resourceAsStreamFromJarInCall = false;
/*     */       }
/*     */ 
/* 436 */       return localInputStream; } catch (Exception localException) {
/*     */     }
/* 438 */     return null;
/*     */   }
/*     */ 
/*     */   public URL findResource(String paramString)
/*     */   {
/* 449 */     URL localURL = super.findResource(paramString);
/*     */ 
/* 453 */     if (paramString.startsWith("META-INF/")) {
/* 454 */       return localURL;
/*     */     }
/*     */ 
/* 458 */     if (!this.codebaseLookup) {
/* 459 */       return localURL;
/*     */     }
/* 461 */     if (localURL == null)
/*     */     {
/* 465 */       boolean bool1 = false;
/* 466 */       synchronized (this.syncResourceAsStreamFromJar) {
/* 467 */         bool1 = this.resourceAsStreamFromJarInCall;
/*     */       }
/*     */ 
/* 470 */       if (bool1) {
/* 471 */         return null;
/*     */       }
/*     */ 
/* 479 */       boolean bool2 = false;
/*     */ 
/* 481 */       synchronized (this.syncResourceAsStream)
/*     */       {
/* 483 */         bool2 = this.resourceAsStreamInCall;
/*     */       }
/*     */ 
/* 490 */       if (!bool2)
/*     */       {
/*     */         try
/*     */         {
/* 494 */           localURL = new URL(this.base, ParseUtil.encodePath(paramString, false));
/*     */ 
/* 496 */           if (!resourceExists(localURL))
/* 497 */             localURL = null;
/*     */         }
/*     */         catch (Exception localException) {
/* 500 */           localURL = null;
/*     */         }
/*     */       }
/*     */     }
/* 504 */     return localURL;
/*     */   }
/*     */ 
/*     */   private boolean resourceExists(URL paramURL)
/*     */   {
/* 518 */     boolean bool = true;
/*     */     try {
/* 520 */       URLConnection localURLConnection = paramURL.openConnection();
/*     */       Object localObject;
/* 521 */       if ((localURLConnection instanceof HttpURLConnection)) {
/* 522 */         localObject = (HttpURLConnection)localURLConnection;
/*     */ 
/* 526 */         ((HttpURLConnection)localObject).setRequestMethod("HEAD");
/*     */ 
/* 528 */         int i = ((HttpURLConnection)localObject).getResponseCode();
/* 529 */         if (i == 200) {
/* 530 */           return true;
/*     */         }
/* 532 */         if (i >= 400) {
/* 533 */           return false;
/*     */         }
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/* 544 */         localObject = localURLConnection.getInputStream();
/* 545 */         ((InputStream)localObject).close();
/*     */       }
/*     */     } catch (Exception localException) {
/* 548 */       bool = false;
/*     */     }
/* 550 */     return bool;
/*     */   }
/*     */ 
/*     */   public Enumeration findResources(String paramString)
/*     */     throws IOException
/*     */   {
/* 560 */     final Enumeration localEnumeration = super.findResources(paramString);
/*     */ 
/* 564 */     if (paramString.startsWith("META-INF/")) {
/* 565 */       return localEnumeration;
/*     */     }
/*     */ 
/* 569 */     if (!this.codebaseLookup) {
/* 570 */       return localEnumeration;
/*     */     }
/* 572 */     URL localURL1 = new URL(this.base, ParseUtil.encodePath(paramString, false));
/* 573 */     if (!resourceExists(localURL1)) {
/* 574 */       localURL1 = null;
/*     */     }
/*     */ 
/* 577 */     final URL localURL2 = localURL1;
/* 578 */     return new Enumeration() {
/*     */       private boolean done;
/*     */ 
/* 581 */       public Object nextElement() { if (!this.done) {
/* 582 */           if (localEnumeration.hasMoreElements()) {
/* 583 */             return localEnumeration.nextElement();
/*     */           }
/* 585 */           this.done = true;
/* 586 */           if (localURL2 != null) {
/* 587 */             return localURL2;
/*     */           }
/*     */         }
/* 590 */         throw new NoSuchElementException(); }
/*     */ 
/*     */       public boolean hasMoreElements() {
/* 593 */         return (!this.done) && ((localEnumeration.hasMoreElements()) || (localURL2 != null));
/*     */       }
/*     */     };
/*     */   }
/*     */ 
/*     */   Class loadCode(String paramString)
/*     */     throws ClassNotFoundException
/*     */   {
/* 605 */     paramString = paramString.replace('/', '.');
/* 606 */     paramString = paramString.replace(File.separatorChar, '.');
/*     */ 
/* 609 */     String str1 = null;
/* 610 */     int i = paramString.indexOf(";");
/* 611 */     if (i != -1) {
/* 612 */       str1 = paramString.substring(i, paramString.length());
/* 613 */       paramString = paramString.substring(0, i);
/*     */     }
/*     */ 
/* 617 */     String str2 = paramString;
/*     */ 
/* 619 */     if ((paramString.endsWith(".class")) || (paramString.endsWith(".java")))
/* 620 */       paramString = paramString.substring(0, paramString.lastIndexOf('.'));
/*     */     try
/*     */     {
/* 623 */       if (str1 != null)
/* 624 */         paramString = paramString + str1;
/* 625 */       return loadClass(paramString);
/*     */     }
/*     */     catch (ClassNotFoundException localClassNotFoundException)
/*     */     {
/* 630 */       if (str1 != null)
/* 631 */         str2 = str2 + str1;
/*     */     }
/* 633 */     return loadClass(str2);
/*     */   }
/*     */ 
/*     */   public ThreadGroup getThreadGroup()
/*     */   {
/* 646 */     synchronized (this.threadGroupSynchronizer) {
/* 647 */       if ((this.threadGroup == null) || (this.threadGroup.isDestroyed())) {
/* 648 */         AccessController.doPrivileged(new PrivilegedAction() {
/*     */           public Object run() {
/* 650 */             AppletClassLoader.this.threadGroup = new AppletThreadGroup(AppletClassLoader.this.base + "-threadGroup");
/*     */ 
/* 657 */             AppContextCreator localAppContextCreator = new AppContextCreator(AppletClassLoader.this.threadGroup);
/*     */ 
/* 664 */             localAppContextCreator.setContextClassLoader(AppletClassLoader.this);
/*     */ 
/* 666 */             localAppContextCreator.start();
/*     */             try {
/* 668 */               synchronized (localAppContextCreator.syncObject) {
/* 669 */                 while (!localAppContextCreator.created)
/* 670 */                   localAppContextCreator.syncObject.wait();
/*     */               }
/*     */             } catch (InterruptedException localInterruptedException) {
/*     */             }
/* 674 */             AppletClassLoader.this.appContext = localAppContextCreator.appContext;
/* 675 */             return null;
/*     */           }
/*     */         });
/*     */       }
/* 679 */       return this.threadGroup;
/*     */     }
/*     */   }
/*     */ 
/*     */   public AppContext getAppContext()
/*     */   {
/* 687 */     return this.appContext;
/*     */   }
/*     */ 
/*     */   public void grab()
/*     */   {
/* 697 */     synchronized (this.grabReleaseSynchronizer) {
/* 698 */       this.usageCount += 1;
/*     */     }
/* 700 */     getThreadGroup();
/*     */   }
/*     */ 
/*     */   protected void setExceptionStatus()
/*     */   {
/* 705 */     this.exceptionStatus = true;
/*     */   }
/*     */ 
/*     */   public boolean getExceptionStatus()
/*     */   {
/* 710 */     return this.exceptionStatus;
/*     */   }
/*     */ 
/*     */   protected void release()
/*     */   {
/* 727 */     AppContext localAppContext = null;
/*     */ 
/* 729 */     synchronized (this.grabReleaseSynchronizer) {
/* 730 */       if (this.usageCount > 1)
/* 731 */         this.usageCount -= 1;
/*     */       else {
/* 733 */         synchronized (this.threadGroupSynchronizer) {
/* 734 */           localAppContext = resetAppContext();
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 741 */     if (localAppContext != null)
/*     */       try {
/* 743 */         localAppContext.dispose();
/*     */       }
/*     */       catch (IllegalThreadStateException localIllegalThreadStateException)
/*     */       {
/*     */       }
/*     */   }
/*     */ 
/*     */   protected AppContext resetAppContext()
/*     */   {
/* 758 */     AppContext localAppContext = null;
/*     */ 
/* 760 */     synchronized (this.threadGroupSynchronizer)
/*     */     {
/* 762 */       localAppContext = this.appContext;
/* 763 */       this.usageCount = 0;
/* 764 */       this.appContext = null;
/* 765 */       this.threadGroup = null;
/*     */     }
/* 767 */     return localAppContext;
/*     */   }
/*     */ 
/*     */   void setJDK11Target(Class paramClass, boolean paramBoolean)
/*     */   {
/* 784 */     this.jdk11AppletInfo.put(paramClass.toString(), Boolean.valueOf(paramBoolean));
/*     */   }
/*     */ 
/*     */   void setJDK12Target(Class paramClass, boolean paramBoolean)
/*     */   {
/* 796 */     this.jdk12AppletInfo.put(paramClass.toString(), Boolean.valueOf(paramBoolean));
/*     */   }
/*     */ 
/*     */   Boolean isJDK11Target(Class paramClass)
/*     */   {
/* 809 */     return (Boolean)this.jdk11AppletInfo.get(paramClass.toString());
/*     */   }
/*     */ 
/*     */   Boolean isJDK12Target(Class paramClass)
/*     */   {
/* 822 */     return (Boolean)this.jdk12AppletInfo.get(paramClass.toString());
/*     */   }
/*     */ 
/*     */   private static void printError(String paramString, Throwable paramThrowable)
/*     */   {
/* 832 */     String str = null;
/* 833 */     if (paramThrowable == null)
/* 834 */       str = mh.getMessage("filenotfound", paramString);
/* 835 */     else if ((paramThrowable instanceof IOException))
/* 836 */       str = mh.getMessage("fileioexception", paramString);
/* 837 */     else if ((paramThrowable instanceof ClassFormatError))
/* 838 */       str = mh.getMessage("fileformat", paramString);
/* 839 */     else if ((paramThrowable instanceof ThreadDeath))
/* 840 */       str = mh.getMessage("filedeath", paramString);
/* 841 */     else if ((paramThrowable instanceof Error)) {
/* 842 */       str = mh.getMessage("fileerror", paramThrowable.toString(), paramString);
/*     */     }
/* 844 */     if (str != null)
/* 845 */       System.err.println(str);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.applet.AppletClassLoader
 * JD-Core Version:    0.6.2
 */