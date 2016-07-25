/*      */ package sun.misc;
/*      */ 
/*      */ import java.io.Closeable;
/*      */ import java.io.File;
/*      */ import java.io.FileInputStream;
/*      */ import java.io.FileNotFoundException;
/*      */ import java.io.FilePermission;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.PrintStream;
/*      */ import java.net.HttpURLConnection;
/*      */ import java.net.JarURLConnection;
/*      */ import java.net.MalformedURLException;
/*      */ import java.net.SocketPermission;
/*      */ import java.net.URL;
/*      */ import java.net.URLConnection;
/*      */ import java.net.URLStreamHandler;
/*      */ import java.net.URLStreamHandlerFactory;
/*      */ import java.security.AccessControlException;
/*      */ import java.security.AccessController;
/*      */ import java.security.CodeSigner;
/*      */ import java.security.Permission;
/*      */ import java.security.PrivilegedActionException;
/*      */ import java.security.PrivilegedExceptionAction;
/*      */ import java.security.cert.Certificate;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collections;
/*      */ import java.util.Enumeration;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.LinkedList;
/*      */ import java.util.List;
/*      */ import java.util.NoSuchElementException;
/*      */ import java.util.Set;
/*      */ import java.util.Stack;
/*      */ import java.util.StringTokenizer;
/*      */ import java.util.jar.Attributes;
/*      */ import java.util.jar.Attributes.Name;
/*      */ import java.util.jar.JarEntry;
/*      */ import java.util.jar.JarFile;
/*      */ import java.util.jar.Manifest;
/*      */ import java.util.zip.ZipEntry;
/*      */ import sun.net.util.URLUtil;
/*      */ import sun.net.www.ParseUtil;
/*      */ import sun.security.action.GetPropertyAction;
/*      */ 
/*      */ public class URLClassPath
/*      */ {
/*      */   static final String USER_AGENT_JAVA_VERSION = "UA-Java-Version";
/*   69 */   static final String JAVA_VERSION = (String)AccessController.doPrivileged(new GetPropertyAction("java.version"));
/*      */ 
/*   71 */   private static final boolean DEBUG = AccessController.doPrivileged(new GetPropertyAction("sun.misc.URLClassPath.debug")) != null;
/*      */ 
/*   75 */   private static final boolean DISABLE_JAR_CHECKING = (str.equals("true")) || (str.equals(""));
/*      */ 
/*   79 */   private ArrayList<URL> path = new ArrayList();
/*      */ 
/*   82 */   Stack<URL> urls = new Stack();
/*      */ 
/*   85 */   ArrayList<Loader> loaders = new ArrayList();
/*      */ 
/*   88 */   HashMap<String, Loader> lmap = new HashMap();
/*      */   private URLStreamHandler jarHandler;
/*   94 */   private boolean closed = false;
/*      */ 
/*      */   public URLClassPath(URL[] paramArrayOfURL, URLStreamHandlerFactory paramURLStreamHandlerFactory)
/*      */   {
/*  107 */     for (int i = 0; i < paramArrayOfURL.length; i++) {
/*  108 */       this.path.add(paramArrayOfURL[i]);
/*      */     }
/*  110 */     push(paramArrayOfURL);
/*  111 */     if (paramURLStreamHandlerFactory != null)
/*  112 */       this.jarHandler = paramURLStreamHandlerFactory.createURLStreamHandler("jar");
/*      */   }
/*      */ 
/*      */   public URLClassPath(URL[] paramArrayOfURL)
/*      */   {
/*  117 */     this(paramArrayOfURL, null);
/*      */   }
/*      */ 
/*      */   public synchronized List<IOException> closeLoaders() {
/*  121 */     if (this.closed) {
/*  122 */       return Collections.emptyList();
/*      */     }
/*  124 */     LinkedList localLinkedList = new LinkedList();
/*  125 */     for (Loader localLoader : this.loaders) {
/*      */       try {
/*  127 */         localLoader.close();
/*      */       } catch (IOException localIOException) {
/*  129 */         localLinkedList.add(localIOException);
/*      */       }
/*      */     }
/*  132 */     this.closed = true;
/*  133 */     return localLinkedList;
/*      */   }
/*      */ 
/*      */   public synchronized void addURL(URL paramURL)
/*      */   {
/*  144 */     if (this.closed)
/*  145 */       return;
/*  146 */     synchronized (this.urls) {
/*  147 */       if ((paramURL == null) || (this.path.contains(paramURL))) {
/*  148 */         return;
/*      */       }
/*  150 */       this.urls.add(0, paramURL);
/*  151 */       this.path.add(paramURL);
/*      */     }
/*      */   }
/*      */ 
/*      */   public URL[] getURLs()
/*      */   {
/*  159 */     synchronized (this.urls) {
/*  160 */       return (URL[])this.path.toArray(new URL[this.path.size()]);
/*      */     }
/*      */   }
/*      */ 
/*      */   public URL findResource(String paramString, boolean paramBoolean)
/*      */   {
/*      */     Loader localLoader;
/*  175 */     for (int i = 0; (localLoader = getLoader(i)) != null; i++) {
/*  176 */       URL localURL = localLoader.findResource(paramString, paramBoolean);
/*  177 */       if (localURL != null) {
/*  178 */         return localURL;
/*      */       }
/*      */     }
/*  181 */     return null;
/*      */   }
/*      */ 
/*      */   public Resource getResource(String paramString, boolean paramBoolean)
/*      */   {
/*  193 */     if (DEBUG)
/*  194 */       System.err.println("URLClassPath.getResource(\"" + paramString + "\")");
/*      */     Loader localLoader;
/*  198 */     for (int i = 0; (localLoader = getLoader(i)) != null; i++) {
/*  199 */       Resource localResource = localLoader.getResource(paramString, paramBoolean);
/*  200 */       if (localResource != null) {
/*  201 */         return localResource;
/*      */       }
/*      */     }
/*  204 */     return null;
/*      */   }
/*      */ 
/*      */   public Enumeration<URL> findResources(final String paramString, final boolean paramBoolean)
/*      */   {
/*  216 */     return new Enumeration() {
/*  217 */       private int index = 0;
/*  218 */       private URL url = null;
/*      */ 
/*      */       private boolean next() {
/*  221 */         if (this.url != null)
/*  222 */           return true;
/*      */         URLClassPath.Loader localLoader;
/*  225 */         while ((localLoader = URLClassPath.this.getLoader(this.index++)) != null) {
/*  226 */           this.url = localLoader.findResource(paramString, paramBoolean);
/*  227 */           if (this.url != null) {
/*  228 */             return true;
/*      */           }
/*      */         }
/*  231 */         return false;
/*      */       }
/*      */ 
/*      */       public boolean hasMoreElements()
/*      */       {
/*  236 */         return next();
/*      */       }
/*      */ 
/*      */       public URL nextElement() {
/*  240 */         if (!next()) {
/*  241 */           throw new NoSuchElementException();
/*      */         }
/*  243 */         URL localURL = this.url;
/*  244 */         this.url = null;
/*  245 */         return localURL;
/*      */       }
/*      */     };
/*      */   }
/*      */ 
/*      */   public Resource getResource(String paramString) {
/*  251 */     return getResource(paramString, true);
/*      */   }
/*      */ 
/*      */   public Enumeration<Resource> getResources(final String paramString, final boolean paramBoolean)
/*      */   {
/*  263 */     return new Enumeration() {
/*  264 */       private int index = 0;
/*  265 */       private Resource res = null;
/*      */ 
/*      */       private boolean next() {
/*  268 */         if (this.res != null)
/*  269 */           return true;
/*      */         URLClassPath.Loader localLoader;
/*  272 */         while ((localLoader = URLClassPath.this.getLoader(this.index++)) != null) {
/*  273 */           this.res = localLoader.getResource(paramString, paramBoolean);
/*  274 */           if (this.res != null) {
/*  275 */             return true;
/*      */           }
/*      */         }
/*  278 */         return false;
/*      */       }
/*      */ 
/*      */       public boolean hasMoreElements()
/*      */       {
/*  283 */         return next();
/*      */       }
/*      */ 
/*      */       public Resource nextElement() {
/*  287 */         if (!next()) {
/*  288 */           throw new NoSuchElementException();
/*      */         }
/*  290 */         Resource localResource = this.res;
/*  291 */         this.res = null;
/*  292 */         return localResource;
/*      */       }
/*      */     };
/*      */   }
/*      */ 
/*      */   public Enumeration<Resource> getResources(String paramString) {
/*  298 */     return getResources(paramString, true);
/*      */   }
/*      */ 
/*      */   private synchronized Loader getLoader(int paramInt)
/*      */   {
/*  307 */     if (this.closed) {
/*  308 */       return null;
/*      */     }
/*      */ 
/*  312 */     while (this.loaders.size() < paramInt + 1)
/*      */     {
/*      */       URL localURL;
/*  315 */       synchronized (this.urls) {
/*  316 */         if (this.urls.empty()) {
/*  317 */           return null;
/*      */         }
/*  319 */         localURL = (URL)this.urls.pop();
/*      */       }
/*      */ 
/*  325 */       ??? = URLUtil.urlNoFragString(localURL);
/*  326 */       if (!this.lmap.containsKey(???))
/*      */       {
/*      */         Loader localLoader;
/*      */         try
/*      */         {
/*  332 */           localLoader = getLoader(localURL);
/*      */ 
/*  335 */           URL[] arrayOfURL = localLoader.getClassPath();
/*  336 */           if (arrayOfURL != null)
/*  337 */             push(arrayOfURL);
/*      */         }
/*      */         catch (IOException localIOException) {
/*      */         }
/*  341 */         continue;
/*      */ 
/*  344 */         this.loaders.add(localLoader);
/*  345 */         this.lmap.put(???, localLoader);
/*      */       }
/*      */     }
/*  347 */     return (Loader)this.loaders.get(paramInt);
/*      */   }
/*      */ 
/*      */   private Loader getLoader(final URL paramURL)
/*      */     throws IOException
/*      */   {
/*      */     try
/*      */     {
/*  355 */       return (Loader)AccessController.doPrivileged(new PrivilegedExceptionAction()
/*      */       {
/*      */         public URLClassPath.Loader run() throws IOException {
/*  358 */           String str = paramURL.getFile();
/*  359 */           if ((str != null) && (str.endsWith("/"))) {
/*  360 */             if ("file".equals(paramURL.getProtocol())) {
/*  361 */               return new URLClassPath.FileLoader(paramURL);
/*      */             }
/*  363 */             return new URLClassPath.Loader(paramURL);
/*      */           }
/*      */ 
/*  366 */           return new URLClassPath.JarLoader(paramURL, URLClassPath.this.jarHandler, URLClassPath.this.lmap);
/*      */         }
/*      */       });
/*      */     }
/*      */     catch (PrivilegedActionException localPrivilegedActionException) {
/*  371 */       throw ((IOException)localPrivilegedActionException.getException());
/*      */     }
/*      */   }
/*      */ 
/*      */   private void push(URL[] paramArrayOfURL)
/*      */   {
/*  379 */     synchronized (this.urls) {
/*  380 */       for (int i = paramArrayOfURL.length - 1; i >= 0; i--)
/*  381 */         this.urls.push(paramArrayOfURL[i]);
/*      */     }
/*      */   }
/*      */ 
/*      */   public static URL[] pathToURLs(String paramString)
/*      */   {
/*  393 */     StringTokenizer localStringTokenizer = new StringTokenizer(paramString, File.pathSeparator);
/*  394 */     Object localObject1 = new URL[localStringTokenizer.countTokens()];
/*  395 */     int i = 0;
/*      */     Object localObject2;
/*  396 */     while (localStringTokenizer.hasMoreTokens()) {
/*  397 */       localObject2 = new File(localStringTokenizer.nextToken());
/*      */       try {
/*  399 */         localObject2 = new File(((File)localObject2).getCanonicalPath());
/*      */       }
/*      */       catch (IOException localIOException1) {
/*      */       }
/*      */       try {
/*  404 */         localObject1[(i++)] = ParseUtil.fileToEncodedURL((File)localObject2);
/*      */       } catch (IOException localIOException2) {
/*      */       }
/*      */     }
/*  408 */     if (localObject1.length != i) {
/*  409 */       localObject2 = new URL[i];
/*  410 */       System.arraycopy(localObject1, 0, localObject2, 0, i);
/*  411 */       localObject1 = localObject2;
/*      */     }
/*  413 */     return localObject1;
/*      */   }
/*      */ 
/*      */   public URL checkURL(URL paramURL)
/*      */   {
/*      */     try
/*      */     {
/*  423 */       check(paramURL);
/*      */     } catch (Exception localException) {
/*  425 */       return null;
/*      */     }
/*      */ 
/*  428 */     return paramURL;
/*      */   }
/*      */ 
/*      */   static void check(URL paramURL)
/*      */     throws IOException
/*      */   {
/*  437 */     SecurityManager localSecurityManager = System.getSecurityManager();
/*  438 */     if (localSecurityManager != null) {
/*  439 */       URLConnection localURLConnection = paramURL.openConnection();
/*  440 */       Permission localPermission = localURLConnection.getPermission();
/*  441 */       if (localPermission != null)
/*      */         try {
/*  443 */           localSecurityManager.checkPermission(localPermission);
/*      */         }
/*      */         catch (SecurityException localSecurityException)
/*      */         {
/*  447 */           if (((localPermission instanceof FilePermission)) && (localPermission.getActions().indexOf("read") != -1))
/*      */           {
/*  449 */             localSecurityManager.checkRead(localPermission.getName());
/*  450 */           } else if (((localPermission instanceof SocketPermission)) && (localPermission.getActions().indexOf("connect") != -1))
/*      */           {
/*  453 */             URL localURL = paramURL;
/*  454 */             if ((localURLConnection instanceof JarURLConnection)) {
/*  455 */               localURL = ((JarURLConnection)localURLConnection).getJarFileURL();
/*      */             }
/*  457 */             localSecurityManager.checkConnect(localURL.getHost(), localURL.getPort());
/*      */           }
/*      */           else {
/*  460 */             throw localSecurityException;
/*      */           }
/*      */         }
/*      */     }
/*      */   }
/*      */ 
/*      */   static
/*      */   {
/*   73 */     String str = (String)AccessController.doPrivileged(new GetPropertyAction("sun.misc.URLClassPath.disableJarChecking"));
/*      */   }
/*      */ 
/*      */   private static class FileLoader extends URLClassPath.Loader
/*      */   {
/*      */     private File dir;
/*      */ 
/*      */     FileLoader(URL paramURL)
/*      */       throws IOException
/*      */     {
/* 1034 */       super();
/* 1035 */       if (!"file".equals(paramURL.getProtocol())) {
/* 1036 */         throw new IllegalArgumentException("url");
/*      */       }
/* 1038 */       String str = paramURL.getFile().replace('/', File.separatorChar);
/* 1039 */       str = ParseUtil.decode(str);
/* 1040 */       this.dir = new File(str).getCanonicalFile();
/*      */     }
/*      */ 
/*      */     URL findResource(String paramString, boolean paramBoolean)
/*      */     {
/* 1047 */       Resource localResource = getResource(paramString, paramBoolean);
/* 1048 */       if (localResource != null) {
/* 1049 */         return localResource.getURL();
/*      */       }
/* 1051 */       return null;
/*      */     }
/*      */ 
/*      */     Resource getResource(final String paramString, boolean paramBoolean)
/*      */     {
/*      */       try {
/* 1057 */         URL localURL2 = new URL(getBaseURL(), ".");
/* 1058 */         final URL localURL1 = new URL(getBaseURL(), ParseUtil.encodePath(paramString, false));
/*      */ 
/* 1060 */         if (!localURL1.getFile().startsWith(localURL2.getFile()))
/*      */         {
/* 1062 */           return null;
/*      */         }
/*      */ 
/* 1065 */         if (paramBoolean)
/* 1066 */           URLClassPath.check(localURL1);
/*      */         final File localFile;
/* 1069 */         if (paramString.indexOf("..") != -1) {
/* 1070 */           localFile = new File(this.dir, paramString.replace('/', File.separatorChar)).getCanonicalFile();
/*      */ 
/* 1072 */           if (!localFile.getPath().startsWith(this.dir.getPath()))
/*      */           {
/* 1074 */             return null;
/*      */           }
/*      */         } else {
/* 1077 */           localFile = new File(this.dir, paramString.replace('/', File.separatorChar));
/*      */         }
/*      */ 
/* 1080 */         if (localFile.exists())
/* 1081 */           return new Resource() {
/* 1082 */             public String getName() { return paramString; } 
/* 1083 */             public URL getURL() { return localURL1; } 
/* 1084 */             public URL getCodeSourceURL() { return URLClassPath.FileLoader.this.getBaseURL(); } 
/*      */             public InputStream getInputStream() throws IOException {
/* 1086 */               return new FileInputStream(localFile);
/*      */             }
/* 1088 */             public int getContentLength() throws IOException { return (int)localFile.length(); }
/*      */           };
/*      */       }
/*      */       catch (Exception localException) {
/* 1092 */         return null;
/*      */       }
/* 1094 */       return null;
/*      */     }
/*      */   }
/*      */ 
/*      */   static class JarLoader extends URLClassPath.Loader
/*      */   {
/*      */     private JarFile jar;
/*      */     private URL csu;
/*      */     private JarIndex index;
/*      */     private MetaIndex metaIndex;
/*      */     private URLStreamHandler handler;
/*      */     private HashMap<String, URLClassPath.Loader> lmap;
/*  598 */     private boolean closed = false;
/*  599 */     private static final JavaUtilZipFileAccess zipAccess = SharedSecrets.getJavaUtilZipFileAccess();
/*      */ 
/*      */     JarLoader(URL paramURL, URLStreamHandler paramURLStreamHandler, HashMap<String, URLClassPath.Loader> paramHashMap)
/*      */       throws IOException
/*      */     {
/*  610 */       super();
/*  611 */       this.csu = paramURL;
/*  612 */       this.handler = paramURLStreamHandler;
/*  613 */       this.lmap = paramHashMap;
/*      */ 
/*  615 */       if (!isOptimizable(paramURL)) {
/*  616 */         ensureOpen();
/*      */       } else {
/*  618 */         String str = paramURL.getFile();
/*  619 */         if (str != null) {
/*  620 */           str = ParseUtil.decode(str);
/*  621 */           File localFile = new File(str);
/*  622 */           this.metaIndex = MetaIndex.forJar(localFile);
/*      */ 
/*  629 */           if ((this.metaIndex != null) && (!localFile.exists())) {
/*  630 */             this.metaIndex = null;
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*  637 */         if (this.metaIndex == null)
/*  638 */           ensureOpen();
/*      */       }
/*      */     }
/*      */ 
/*      */     public void close()
/*      */       throws IOException
/*      */     {
/*  646 */       if (!this.closed) {
/*  647 */         this.closed = true;
/*      */ 
/*  649 */         ensureOpen();
/*  650 */         this.jar.close();
/*      */       }
/*      */     }
/*      */ 
/*      */     JarFile getJarFile() {
/*  655 */       return this.jar;
/*      */     }
/*      */ 
/*      */     private boolean isOptimizable(URL paramURL) {
/*  659 */       return "file".equals(paramURL.getProtocol());
/*      */     }
/*      */ 
/*      */     private void ensureOpen() throws IOException {
/*  663 */       if (this.jar == null)
/*      */         try {
/*  665 */           AccessController.doPrivileged(new PrivilegedExceptionAction()
/*      */           {
/*      */             public Void run() throws IOException {
/*  668 */               if (URLClassPath.DEBUG) {
/*  669 */                 System.err.println("Opening " + URLClassPath.JarLoader.this.csu);
/*  670 */                 Thread.dumpStack();
/*      */               }
/*      */ 
/*  673 */               URLClassPath.JarLoader.this.jar = URLClassPath.JarLoader.this.getJarFile(URLClassPath.JarLoader.this.csu);
/*  674 */               URLClassPath.JarLoader.this.index = JarIndex.getJarIndex(URLClassPath.JarLoader.this.jar, URLClassPath.JarLoader.this.metaIndex);
/*  675 */               if (URLClassPath.JarLoader.this.index != null) {
/*  676 */                 String[] arrayOfString = URLClassPath.JarLoader.this.index.getJarFiles();
/*      */ 
/*  682 */                 for (int i = 0; i < arrayOfString.length; i++) {
/*      */                   try {
/*  684 */                     URL localURL = new URL(URLClassPath.JarLoader.this.csu, arrayOfString[i]);
/*      */ 
/*  686 */                     String str = URLUtil.urlNoFragString(localURL);
/*  687 */                     if (!URLClassPath.JarLoader.this.lmap.containsKey(str))
/*  688 */                       URLClassPath.JarLoader.this.lmap.put(str, null);
/*      */                   }
/*      */                   catch (MalformedURLException localMalformedURLException)
/*      */                   {
/*      */                   }
/*      */                 }
/*      */               }
/*  695 */               return null;
/*      */             }
/*      */           });
/*      */         }
/*      */         catch (PrivilegedActionException localPrivilegedActionException) {
/*  700 */           throw ((IOException)localPrivilegedActionException.getException());
/*      */         }
/*      */     }
/*      */ 
/*      */     static JarFile checkJar(JarFile paramJarFile)
/*      */       throws IOException
/*      */     {
/*  707 */       if ((System.getSecurityManager() != null) && (!URLClassPath.DISABLE_JAR_CHECKING) && (!zipAccess.startsWithLocHeader(paramJarFile)))
/*      */       {
/*  709 */         IOException localIOException1 = new IOException("Invalid Jar file");
/*      */         try {
/*  711 */           paramJarFile.close();
/*      */         } catch (IOException localIOException2) {
/*  713 */           localIOException1.addSuppressed(localIOException2);
/*      */         }
/*  715 */         throw localIOException1;
/*      */       }
/*      */ 
/*  718 */       return paramJarFile;
/*      */     }
/*      */ 
/*      */     private JarFile getJarFile(URL paramURL) throws IOException
/*      */     {
/*  723 */       if (isOptimizable(paramURL)) {
/*  724 */         localObject = new FileURLMapper(paramURL);
/*  725 */         if (!((FileURLMapper)localObject).exists()) {
/*  726 */           throw new FileNotFoundException(((FileURLMapper)localObject).getPath());
/*      */         }
/*  728 */         return checkJar(new JarFile(((FileURLMapper)localObject).getPath()));
/*      */       }
/*  730 */       Object localObject = getBaseURL().openConnection();
/*  731 */       ((URLConnection)localObject).setRequestProperty("UA-Java-Version", URLClassPath.JAVA_VERSION);
/*  732 */       JarFile localJarFile = ((JarURLConnection)localObject).getJarFile();
/*  733 */       return checkJar(localJarFile);
/*      */     }
/*      */ 
/*      */     JarIndex getIndex()
/*      */     {
/*      */       try
/*      */       {
/*  741 */         ensureOpen();
/*      */       } catch (IOException localIOException) {
/*  743 */         throw ((InternalError)new InternalError().initCause(localIOException));
/*      */       }
/*  745 */       return this.index;
/*      */     }
/*      */ 
/*      */     Resource checkResource(final String paramString, boolean paramBoolean, final JarEntry paramJarEntry)
/*      */     {
/*      */       final URL localURL;
/*      */       try
/*      */       {
/*  757 */         localURL = new URL(getBaseURL(), ParseUtil.encodePath(paramString, false));
/*  758 */         if (paramBoolean)
/*  759 */           URLClassPath.check(localURL);
/*      */       }
/*      */       catch (MalformedURLException localMalformedURLException) {
/*  762 */         return null;
/*      */       }
/*      */       catch (IOException localIOException) {
/*  765 */         return null;
/*      */       } catch (AccessControlException localAccessControlException) {
/*  767 */         return null;
/*      */       }
/*      */ 
/*  770 */       return new Resource() {
/*  771 */         public String getName() { return paramString; } 
/*  772 */         public URL getURL() { return localURL; } 
/*  773 */         public URL getCodeSourceURL() { return URLClassPath.JarLoader.this.csu; } 
/*      */         public InputStream getInputStream() throws IOException {
/*  775 */           return URLClassPath.JarLoader.this.jar.getInputStream(paramJarEntry);
/*      */         }
/*  777 */         public int getContentLength() { return (int)paramJarEntry.getSize(); } 
/*      */         public Manifest getManifest() throws IOException {
/*  779 */           return URLClassPath.JarLoader.this.jar.getManifest();
/*      */         }
/*  781 */         public Certificate[] getCertificates() { return paramJarEntry.getCertificates(); } 
/*      */         public CodeSigner[] getCodeSigners() {
/*  783 */           return paramJarEntry.getCodeSigners();
/*      */         }
/*      */       };
/*      */     }
/*      */ 
/*      */     boolean validIndex(String paramString)
/*      */     {
/*  793 */       String str1 = paramString;
/*      */       int i;
/*  795 */       if ((i = paramString.lastIndexOf("/")) != -1) {
/*  796 */         str1 = paramString.substring(0, i);
/*      */       }
/*      */ 
/*  801 */       Enumeration localEnumeration = this.jar.entries();
/*  802 */       while (localEnumeration.hasMoreElements()) {
/*  803 */         ZipEntry localZipEntry = (ZipEntry)localEnumeration.nextElement();
/*  804 */         String str2 = localZipEntry.getName();
/*  805 */         if ((i = str2.lastIndexOf("/")) != -1)
/*  806 */           str2 = str2.substring(0, i);
/*  807 */         if (str2.equals(str1)) {
/*  808 */           return true;
/*      */         }
/*      */       }
/*  811 */       return false;
/*      */     }
/*      */ 
/*      */     URL findResource(String paramString, boolean paramBoolean)
/*      */     {
/*  818 */       Resource localResource = getResource(paramString, paramBoolean);
/*  819 */       if (localResource != null) {
/*  820 */         return localResource.getURL();
/*      */       }
/*  822 */       return null;
/*      */     }
/*      */ 
/*      */     Resource getResource(String paramString, boolean paramBoolean)
/*      */     {
/*  829 */       if ((this.metaIndex != null) && 
/*  830 */         (!this.metaIndex.mayContain(paramString))) {
/*  831 */         return null;
/*      */       }
/*      */ 
/*      */       try
/*      */       {
/*  836 */         ensureOpen();
/*      */       } catch (IOException localIOException) {
/*  838 */         throw ((InternalError)new InternalError().initCause(localIOException));
/*      */       }
/*  840 */       JarEntry localJarEntry = this.jar.getJarEntry(paramString);
/*  841 */       if (localJarEntry != null) {
/*  842 */         return checkResource(paramString, paramBoolean, localJarEntry);
/*      */       }
/*  844 */       if (this.index == null) {
/*  845 */         return null;
/*      */       }
/*  847 */       HashSet localHashSet = new HashSet();
/*  848 */       return getResource(paramString, paramBoolean, localHashSet);
/*      */     }
/*      */ 
/*      */     Resource getResource(String paramString, boolean paramBoolean, Set<String> paramSet)
/*      */     {
/*  863 */       int i = 0;
/*  864 */       int j = 0;
/*  865 */       LinkedList localLinkedList = null;
/*      */ 
/*  870 */       if ((localLinkedList = this.index.get(paramString)) == null)
/*  871 */         return null;
/*      */       do
/*      */       {
/*  874 */         Object[] arrayOfObject = localLinkedList.toArray();
/*  875 */         int k = localLinkedList.size();
/*      */ 
/*  877 */         while (j < k) { String str1 = (String)arrayOfObject[(j++)];
/*      */           final URL localURL;
/*      */           JarLoader localJarLoader;
/*      */           try {
/*  883 */             localURL = new URL(this.csu, str1);
/*  884 */             String str2 = URLUtil.urlNoFragString(localURL);
/*  885 */             if ((localJarLoader = (JarLoader)this.lmap.get(str2)) == null)
/*      */             {
/*  889 */               localJarLoader = (JarLoader)AccessController.doPrivileged(new PrivilegedExceptionAction()
/*      */               {
/*      */                 public URLClassPath.JarLoader run() throws IOException {
/*  892 */                   return new URLClassPath.JarLoader(localURL, URLClassPath.JarLoader.this.handler, URLClassPath.JarLoader.this.lmap);
/*      */                 }
/*      */               });
/*  901 */               JarIndex localJarIndex = localJarLoader.getIndex();
/*  902 */               if (localJarIndex != null) {
/*  903 */                 int n = str1.lastIndexOf("/");
/*  904 */                 localJarIndex.merge(this.index, n == -1 ? null : str1.substring(0, n + 1));
/*      */               }
/*      */ 
/*  909 */               this.lmap.put(str2, localJarLoader);
/*      */             }
/*      */           } catch (PrivilegedActionException localPrivilegedActionException) {
/*  912 */             continue; } catch (MalformedURLException localMalformedURLException) {
/*      */           }
/*  914 */           continue;
/*      */ 
/*  921 */           int m = !paramSet.add(URLUtil.urlNoFragString(localURL)) ? 1 : 0;
/*  922 */           if (m == 0) {
/*      */             try {
/*  924 */               localJarLoader.ensureOpen();
/*      */             } catch (IOException localIOException) {
/*  926 */               throw ((InternalError)new InternalError().initCause(localIOException));
/*      */             }
/*  928 */             JarEntry localJarEntry = localJarLoader.jar.getJarEntry(paramString);
/*  929 */             if (localJarEntry != null) {
/*  930 */               return localJarLoader.checkResource(paramString, paramBoolean, localJarEntry);
/*      */             }
/*      */ 
/*  937 */             if (!localJarLoader.validIndex(paramString))
/*      */             {
/*  939 */               throw new InvalidJarIndexException("Invalid index");
/*      */             }
/*      */ 
/*      */           }
/*      */ 
/*  948 */           if ((m == 0) && (localJarLoader != this) && (localJarLoader.getIndex() != null))
/*      */           {
/*      */             Resource localResource;
/*  955 */             if ((localResource = localJarLoader.getResource(paramString, paramBoolean, paramSet)) != null)
/*      */             {
/*  957 */               return localResource;
/*      */             }
/*      */           }
/*      */         }
/*      */ 
/*  962 */         localLinkedList = this.index.get(paramString);
/*      */       }
/*      */ 
/*  965 */       while (j < localLinkedList.size());
/*  966 */       return null;
/*      */     }
/*      */ 
/*      */     URL[] getClassPath()
/*      */       throws IOException
/*      */     {
/*  974 */       if (this.index != null) {
/*  975 */         return null;
/*      */       }
/*      */ 
/*  978 */       if (this.metaIndex != null) {
/*  979 */         return null;
/*      */       }
/*      */ 
/*  982 */       ensureOpen();
/*  983 */       parseExtensionsDependencies();
/*  984 */       if (SharedSecrets.javaUtilJarAccess().jarFileHasClassPathAttribute(this.jar)) {
/*  985 */         Manifest localManifest = this.jar.getManifest();
/*  986 */         if (localManifest != null) {
/*  987 */           Attributes localAttributes = localManifest.getMainAttributes();
/*  988 */           if (localAttributes != null) {
/*  989 */             String str = localAttributes.getValue(Attributes.Name.CLASS_PATH);
/*  990 */             if (str != null) {
/*  991 */               return parseClassPath(this.csu, str);
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*  996 */       return null;
/*      */     }
/*      */ 
/*      */     private void parseExtensionsDependencies()
/*      */       throws IOException
/*      */     {
/* 1003 */       ExtensionDependency.checkExtensionsDependencies(this.jar);
/*      */     }
/*      */ 
/*      */     private URL[] parseClassPath(URL paramURL, String paramString)
/*      */       throws MalformedURLException
/*      */     {
/* 1013 */       StringTokenizer localStringTokenizer = new StringTokenizer(paramString);
/* 1014 */       URL[] arrayOfURL = new URL[localStringTokenizer.countTokens()];
/* 1015 */       int i = 0;
/* 1016 */       while (localStringTokenizer.hasMoreTokens()) {
/* 1017 */         String str = localStringTokenizer.nextToken();
/* 1018 */         arrayOfURL[i] = new URL(paramURL, str);
/* 1019 */         i++;
/*      */       }
/* 1021 */       return arrayOfURL;
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class Loader
/*      */     implements Closeable
/*      */   {
/*      */     private final URL base;
/*      */     private JarFile jarfile;
/*      */ 
/*      */     Loader(URL paramURL)
/*      */     {
/*  479 */       this.base = paramURL;
/*      */     }
/*      */ 
/*      */     URL getBaseURL()
/*      */     {
/*  486 */       return this.base;
/*      */     }
/*      */ 
/*      */     URL findResource(String paramString, boolean paramBoolean) {
/*      */       URL localURL;
/*      */       try {
/*  492 */         localURL = new URL(this.base, ParseUtil.encodePath(paramString, false));
/*      */       } catch (MalformedURLException localMalformedURLException) {
/*  494 */         throw new IllegalArgumentException("name");
/*      */       }
/*      */       try
/*      */       {
/*  498 */         if (paramBoolean) {
/*  499 */           URLClassPath.check(localURL);
/*      */         }
/*      */ 
/*  506 */         URLConnection localURLConnection = localURL.openConnection();
/*      */         Object localObject;
/*  507 */         if ((localURLConnection instanceof HttpURLConnection)) {
/*  508 */           localObject = (HttpURLConnection)localURLConnection;
/*  509 */           ((HttpURLConnection)localObject).setRequestMethod("HEAD");
/*  510 */           if (((HttpURLConnection)localObject).getResponseCode() >= 400)
/*  511 */             return null;
/*      */         }
/*      */         else
/*      */         {
/*  515 */           localObject = localURL.openStream();
/*  516 */           ((InputStream)localObject).close();
/*      */         }
/*  518 */         return localURL; } catch (Exception localException) {
/*      */       }
/*  520 */       return null;
/*      */     }
/*      */ 
/*      */     Resource getResource(final String paramString, boolean paramBoolean)
/*      */     {
/*      */       final URL localURL;
/*      */       try {
/*  527 */         localURL = new URL(this.base, ParseUtil.encodePath(paramString, false));
/*      */       } catch (MalformedURLException localMalformedURLException) {
/*  529 */         throw new IllegalArgumentException("name");
/*      */       }
/*      */       final URLConnection localURLConnection;
/*      */       try {
/*  533 */         if (paramBoolean) {
/*  534 */           URLClassPath.check(localURL);
/*      */         }
/*  536 */         localURLConnection = localURL.openConnection();
/*  537 */         InputStream localInputStream = localURLConnection.getInputStream();
/*  538 */         if ((localURLConnection instanceof JarURLConnection))
/*      */         {
/*  542 */           JarURLConnection localJarURLConnection = (JarURLConnection)localURLConnection;
/*  543 */           this.jarfile = URLClassPath.JarLoader.checkJar(localJarURLConnection.getJarFile());
/*      */         }
/*      */       } catch (Exception localException) {
/*  546 */         return null;
/*      */       }
/*  548 */       return new Resource() {
/*  549 */         public String getName() { return paramString; } 
/*  550 */         public URL getURL() { return localURL; } 
/*  551 */         public URL getCodeSourceURL() { return URLClassPath.Loader.this.base; } 
/*      */         public InputStream getInputStream() throws IOException {
/*  553 */           return localURLConnection.getInputStream();
/*      */         }
/*      */         public int getContentLength() throws IOException {
/*  556 */           return localURLConnection.getContentLength();
/*      */         }
/*      */       };
/*      */     }
/*      */ 
/*      */     Resource getResource(String paramString)
/*      */     {
/*  567 */       return getResource(paramString, true);
/*      */     }
/*      */ 
/*      */     public void close()
/*      */       throws IOException
/*      */     {
/*  575 */       if (this.jarfile != null)
/*  576 */         this.jarfile.close();
/*      */     }
/*      */ 
/*      */     URL[] getClassPath()
/*      */       throws IOException
/*      */     {
/*  584 */       return null;
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.misc.URLClassPath
 * JD-Core Version:    0.6.2
 */