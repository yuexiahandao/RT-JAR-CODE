/*     */ package javax.xml.xpath;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.PrintStream;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.net.URL;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Iterator;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Properties;
/*     */ 
/*     */ class XPathFactoryFinder
/*     */ {
/*     */   private static final String DEFAULT_PACKAGE = "com.sun.org.apache.xpath.internal";
/*  53 */   private static SecuritySupport ss = new SecuritySupport();
/*     */ 
/*  55 */   private static boolean debug = false;
/*     */ 
/*  68 */   private static Properties cacheProps = new Properties();
/*     */ 
/*  73 */   private static volatile boolean firstTime = true;
/*     */   private final ClassLoader classLoader;
/* 525 */   private static final Class SERVICE_CLASS = XPathFactory.class;
/* 526 */   private static final String SERVICE_ID = "META-INF/services/" + SERVICE_CLASS.getName();
/*     */ 
/*     */   private static void debugPrintln(String msg)
/*     */   {
/*  81 */     if (debug)
/*  82 */       System.err.println("JAXP: " + msg);
/*     */   }
/*     */ 
/*     */   public XPathFactoryFinder(ClassLoader loader)
/*     */   {
/* 103 */     this.classLoader = loader;
/* 104 */     if (debug)
/* 105 */       debugDisplayClassLoader();
/*     */   }
/*     */ 
/*     */   private void debugDisplayClassLoader()
/*     */   {
/*     */     try {
/* 111 */       if (this.classLoader == ss.getContextClassLoader()) {
/* 112 */         debugPrintln("using thread context class loader (" + this.classLoader + ") for search");
/* 113 */         return;
/*     */       }
/*     */     }
/*     */     catch (Throwable _)
/*     */     {
/*     */     }
/* 119 */     if (this.classLoader == ClassLoader.getSystemClassLoader()) {
/* 120 */       debugPrintln("using system class loader (" + this.classLoader + ") for search");
/* 121 */       return;
/*     */     }
/*     */ 
/* 124 */     debugPrintln("using class loader (" + this.classLoader + ") for search");
/*     */   }
/*     */ 
/*     */   public XPathFactory newFactory(String uri)
/*     */   {
/* 140 */     if (uri == null) throw new NullPointerException();
/* 141 */     XPathFactory f = _newFactory(uri);
/* 142 */     if (f != null)
/* 143 */       debugPrintln("factory '" + f.getClass().getName() + "' was found for " + uri);
/*     */     else {
/* 145 */       debugPrintln("unable to find a factory for " + uri);
/*     */     }
/* 147 */     return f;
/*     */   }
/*     */ 
/*     */   private XPathFactory _newFactory(String uri)
/*     */   {
/* 160 */     String propertyName = SERVICE_CLASS.getName() + ":" + uri;
/*     */     try
/*     */     {
/* 164 */       debugPrintln("Looking up system property '" + propertyName + "'");
/* 165 */       String r = ss.getSystemProperty(propertyName);
/* 166 */       if (r != null) {
/* 167 */         debugPrintln("The value is '" + r + "'");
/* 168 */         XPathFactory xpathFactory = createInstance(r, true);
/* 169 */         if (xpathFactory != null) return xpathFactory; 
/*     */       }
/* 171 */       else { debugPrintln("The property is undefined."); }
/*     */     } catch (Throwable t) {
/* 173 */       if (debug) {
/* 174 */         debugPrintln("failed to look up system property '" + propertyName + "'");
/* 175 */         t.printStackTrace();
/*     */       }
/*     */     }
/*     */ 
/* 179 */     String javah = ss.getSystemProperty("java.home");
/* 180 */     String configFile = javah + File.separator + "lib" + File.separator + "jaxp.properties";
/*     */ 
/* 183 */     String factoryClassName = null;
/*     */     try
/*     */     {
/* 187 */       if (firstTime) {
/* 188 */         synchronized (cacheProps) {
/* 189 */           if (firstTime) {
/* 190 */             File f = new File(configFile);
/* 191 */             firstTime = false;
/* 192 */             if (ss.doesFileExist(f)) {
/* 193 */               debugPrintln("Read properties file " + f);
/* 194 */               cacheProps.load(ss.getFileInputStream(f));
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/* 199 */       factoryClassName = cacheProps.getProperty(propertyName);
/* 200 */       debugPrintln("found " + factoryClassName + " in $java.home/jaxp.properties");
/*     */ 
/* 202 */       if (factoryClassName != null) {
/* 203 */         XPathFactory xpathFactory = createInstance(factoryClassName, true);
/* 204 */         if (xpathFactory != null)
/* 205 */           return xpathFactory;
/*     */       }
/*     */     }
/*     */     catch (Exception ex) {
/* 209 */       if (debug) {
/* 210 */         ex.printStackTrace();
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 215 */     Iterator sitr = createServiceFileIterator();
/* 216 */     while (sitr.hasNext()) {
/* 217 */       URL resource = (URL)sitr.next();
/* 218 */       debugPrintln("looking into " + resource);
/*     */       try {
/* 220 */         XPathFactory xpathFactory = loadFromService(uri, resource.toExternalForm(), ss.getURLInputStream(resource));
/*     */ 
/* 222 */         if (xpathFactory != null)
/* 223 */           return xpathFactory;
/*     */       }
/*     */       catch (IOException e) {
/* 226 */         if (debug) {
/* 227 */           debugPrintln("failed to read " + resource);
/* 228 */           e.printStackTrace();
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 234 */     if (uri.equals("http://java.sun.com/jaxp/xpath/dom")) {
/* 235 */       debugPrintln("attempting to use the platform default W3C DOM XPath lib");
/* 236 */       return createInstance("com.sun.org.apache.xpath.internal.jaxp.XPathFactoryImpl", true);
/*     */     }
/*     */ 
/* 239 */     debugPrintln("all things were tried, but none was found. bailing out.");
/* 240 */     return null;
/*     */   }
/*     */ 
/*     */   private Class createClass(String className)
/*     */   {
/* 251 */     boolean internal = false;
/* 252 */     if ((System.getSecurityManager() != null) && 
/* 253 */       (className != null) && (className.startsWith("com.sun.org.apache.xpath.internal")))
/* 254 */       internal = true;
/*     */     Class clazz;
/*     */     try
/*     */     {
/*     */       Class clazz;
/* 260 */       if ((this.classLoader != null) && (!internal))
/* 261 */         clazz = this.classLoader.loadClass(className);
/*     */       else
/* 263 */         clazz = Class.forName(className);
/*     */     }
/*     */     catch (Throwable t) {
/* 266 */       if (debug) t.printStackTrace();
/* 267 */       return null;
/*     */     }
/*     */ 
/* 270 */     return clazz;
/*     */   }
/*     */ 
/*     */   XPathFactory createInstance(String className)
/*     */   {
/* 283 */     return createInstance(className, false);
/*     */   }
/*     */   XPathFactory createInstance(String className, boolean useServicesMechanism) {
/* 286 */     XPathFactory xPathFactory = null;
/*     */ 
/* 288 */     debugPrintln("createInstance(" + className + ")");
/*     */ 
/* 291 */     Class clazz = createClass(className);
/* 292 */     if (clazz == null) {
/* 293 */       debugPrintln("failed to getClass(" + className + ")");
/* 294 */       return null;
/*     */     }
/* 296 */     debugPrintln("loaded " + className + " from " + which(clazz));
/*     */     try
/*     */     {
/* 300 */       if (!useServicesMechanism) {
/* 301 */         xPathFactory = (XPathFactory)newInstanceNoServiceLoader(clazz);
/*     */       }
/* 303 */       if (xPathFactory == null)
/* 304 */         xPathFactory = (XPathFactory)clazz.newInstance();
/*     */     }
/*     */     catch (ClassCastException classCastException) {
/* 307 */       debugPrintln("could not instantiate " + clazz.getName());
/* 308 */       if (debug) {
/* 309 */         classCastException.printStackTrace();
/*     */       }
/* 311 */       return null;
/*     */     } catch (IllegalAccessException illegalAccessException) {
/* 313 */       debugPrintln("could not instantiate " + clazz.getName());
/* 314 */       if (debug) {
/* 315 */         illegalAccessException.printStackTrace();
/*     */       }
/* 317 */       return null;
/*     */     } catch (InstantiationException instantiationException) {
/* 319 */       debugPrintln("could not instantiate " + clazz.getName());
/* 320 */       if (debug) {
/* 321 */         instantiationException.printStackTrace();
/*     */       }
/* 323 */       return null;
/*     */     }
/*     */ 
/* 326 */     return xPathFactory;
/*     */   }
/*     */ 
/*     */   private static Object newInstanceNoServiceLoader(Class<?> providerClass)
/*     */   {
/* 336 */     if (System.getSecurityManager() == null)
/* 337 */       return null;
/*     */     try
/*     */     {
/* 340 */       Method creationMethod = providerClass.getDeclaredMethod("newXPathFactoryNoServiceLoader", new Class[0]);
/*     */ 
/* 344 */       return creationMethod.invoke(null, (Object[])null);
/*     */     } catch (NoSuchMethodException exc) {
/* 346 */       return null; } catch (Exception exc) {
/*     */     }
/* 348 */     return null;
/*     */   }
/*     */ 
/*     */   private XPathFactory loadFromService(String objectModel, String inputName, InputStream in)
/*     */     throws IOException
/*     */   {
/* 371 */     XPathFactory xPathFactory = null;
/* 372 */     Class[] stringClassArray = { "".getClass() };
/* 373 */     Object[] objectModelObjectArray = { objectModel };
/* 374 */     String isObjectModelSupportedMethod = "isObjectModelSupported";
/*     */ 
/* 376 */     debugPrintln("Reading " + inputName);
/*     */ 
/* 379 */     BufferedReader configFile = new BufferedReader(new InputStreamReader(in));
/* 380 */     String line = null;
/* 381 */     while ((line = configFile.readLine()) != null)
/*     */     {
/* 383 */       int comment = line.indexOf("#");
/* 384 */       switch (comment) { case -1:
/* 385 */         break;
/*     */       case 0:
/* 386 */         line = ""; break;
/*     */       default:
/* 387 */         line = line.substring(0, comment);
/*     */       }
/*     */ 
/* 391 */       line = line.trim();
/*     */ 
/* 394 */       if (line.length() != 0)
/*     */       {
/* 399 */         Class clazz = createClass(line);
/* 400 */         if (clazz != null)
/*     */         {
/*     */           try
/*     */           {
/* 406 */             xPathFactory = (XPathFactory)clazz.newInstance();
/*     */           } catch (ClassCastException classCastExcpetion) {
/* 408 */             xPathFactory = null;
/* 409 */             continue;
/*     */           } catch (InstantiationException instantiationException) {
/* 411 */             xPathFactory = null;
/* 412 */             continue;
/*     */           } catch (IllegalAccessException illegalAccessException) {
/* 414 */             xPathFactory = null;
/* 415 */           }continue;
/*     */           try
/*     */           {
/* 420 */             Method isObjectModelSupported = clazz.getMethod("isObjectModelSupported", stringClassArray);
/* 421 */             Boolean supported = (Boolean)isObjectModelSupported.invoke(xPathFactory, objectModelObjectArray);
/* 422 */             if (supported.booleanValue())
/* 423 */               break;
/*     */           }
/*     */           catch (NoSuchMethodException noSuchMethodException)
/*     */           {
/*     */           }
/*     */           catch (IllegalAccessException illegalAccessException)
/*     */           {
/*     */           }
/*     */           catch (InvocationTargetException invocationTargetException) {
/*     */           }
/* 433 */           xPathFactory = null;
/*     */         }
/*     */       }
/*     */     }
/* 437 */     configFile.close();
/*     */ 
/* 440 */     return xPathFactory;
/*     */   }
/*     */ 
/*     */   private XPathFactory loadFromProperty(String keyName, String resourceName, InputStream in)
/*     */     throws IOException
/*     */   {
/* 467 */     debugPrintln("Reading " + resourceName);
/*     */ 
/* 469 */     Properties props = new Properties();
/* 470 */     props.load(in);
/* 471 */     in.close();
/* 472 */     String factoryClassName = props.getProperty(keyName);
/* 473 */     if (factoryClassName != null) {
/* 474 */       debugPrintln("found " + keyName + " = " + factoryClassName);
/* 475 */       return createInstance(factoryClassName, true);
/*     */     }
/* 477 */     debugPrintln(keyName + " is not in the property file");
/* 478 */     return null; } 
/*     */   private Iterator createServiceFileIterator() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield 419	javax/xml/xpath/XPathFactoryFinder:classLoader	Ljava/lang/ClassLoader;
/*     */     //   4: ifnonnull +12 -> 16
/*     */     //   7: new 299	javax/xml/xpath/XPathFactoryFinder$1
/*     */     //   10: dup
/*     */     //   11: aload_0
/*     */     //   12: invokespecial 486	javax/xml/xpath/XPathFactoryFinder$1:<init>	(Ljavax/xml/xpath/XPathFactoryFinder;)V
/*     */     //   15: areturn
/*     */     //   16: getstatic 422	javax/xml/xpath/XPathFactoryFinder:ss	Ljavax/xml/xpath/SecuritySupport;
/*     */     //   19: aload_0
/*     */     //   20: getfield 419	javax/xml/xpath/XPathFactoryFinder:classLoader	Ljava/lang/ClassLoader;
/*     */     //   23: getstatic 420	javax/xml/xpath/XPathFactoryFinder:SERVICE_ID	Ljava/lang/String;
/*     */     //   26: invokevirtual 475	javax/xml/xpath/SecuritySupport:getResources	(Ljava/lang/ClassLoader;Ljava/lang/String;)Ljava/util/Enumeration;
/*     */     //   29: astore_1
/*     */     //   30: aload_1
/*     */     //   31: invokeinterface 488 1 0
/*     */     //   36: ifne +32 -> 68
/*     */     //   39: new 286	java/lang/StringBuilder
/*     */     //   42: dup
/*     */     //   43: invokespecial 454	java/lang/StringBuilder:<init>	()V
/*     */     //   46: ldc 42
/*     */     //   48: invokevirtual 457	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   51: getstatic 420	javax/xml/xpath/XPathFactoryFinder:SERVICE_ID	Ljava/lang/String;
/*     */     //   54: invokevirtual 457	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   57: ldc 3
/*     */     //   59: invokevirtual 457	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   62: invokevirtual 455	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*     */     //   65: invokestatic 477	javax/xml/xpath/XPathFactoryFinder:debugPrintln	(Ljava/lang/String;)V
/*     */     //   68: new 300	javax/xml/xpath/XPathFactoryFinder$2
/*     */     //   71: dup
/*     */     //   72: aload_0
/*     */     //   73: aload_1
/*     */     //   74: invokespecial 487	javax/xml/xpath/XPathFactoryFinder$2:<init>	(Ljavax/xml/xpath/XPathFactoryFinder;Ljava/util/Enumeration;)V
/*     */     //   77: areturn
/*     */     //   78: astore_1
/*     */     //   79: new 286	java/lang/StringBuilder
/*     */     //   82: dup
/*     */     //   83: invokespecial 454	java/lang/StringBuilder:<init>	()V
/*     */     //   86: ldc 28
/*     */     //   88: invokevirtual 457	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   91: getstatic 420	javax/xml/xpath/XPathFactoryFinder:SERVICE_ID	Ljava/lang/String;
/*     */     //   94: invokevirtual 457	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   97: invokevirtual 455	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*     */     //   100: invokestatic 477	javax/xml/xpath/XPathFactoryFinder:debugPrintln	(Ljava/lang/String;)V
/*     */     //   103: getstatic 416	javax/xml/xpath/XPathFactoryFinder:debug	Z
/*     */     //   106: ifeq +7 -> 113
/*     */     //   109: aload_1
/*     */     //   110: invokevirtual 427	java/io/IOException:printStackTrace	()V
/*     */     //   113: new 292	java/util/ArrayList
/*     */     //   116: dup
/*     */     //   117: invokespecial 463	java/util/ArrayList:<init>	()V
/*     */     //   120: invokevirtual 464	java/util/ArrayList:iterator	()Ljava/util/Iterator;
/*     */     //   123: areturn
/*     */     //
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   16	77	78	java/io/IOException } 
/* 531 */   private static String which(Class clazz) { return which(clazz.getName(), clazz.getClassLoader()); }
/*     */ 
/*     */ 
/*     */   private static String which(String classname, ClassLoader loader)
/*     */   {
/* 544 */     String classnameAsResource = classname.replace('.', '/') + ".class";
/*     */ 
/* 546 */     if (loader == null) loader = ClassLoader.getSystemClassLoader();
/*     */ 
/* 549 */     URL it = ss.getResourceAsURL(loader, classnameAsResource);
/* 550 */     if (it != null) {
/* 551 */       return it.toString();
/*     */     }
/* 553 */     return null;
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*     */     try
/*     */     {
/*  59 */       debug = ss.getSystemProperty("jaxp.debug") != null;
/*     */     } catch (Exception _) {
/*  61 */       debug = false;
/*     */     }
/*     */   }
/*     */ 
/*     */   private static abstract class SingleIterator
/*     */     implements Iterator
/*     */   {
/* 445 */     private boolean seen = false;
/*     */ 
/* 447 */     public final void remove() { throw new UnsupportedOperationException(); } 
/* 448 */     public final boolean hasNext() { return !this.seen; } 
/*     */     public final Object next() {
/* 450 */       if (this.seen) throw new NoSuchElementException();
/* 451 */       this.seen = true;
/* 452 */       return value();
/*     */     }
/*     */ 
/*     */     protected abstract Object value();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.xpath.XPathFactoryFinder
 * JD-Core Version:    0.6.2
 */