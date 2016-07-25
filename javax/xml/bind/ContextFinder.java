/*     */ package javax.xml.bind;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.net.URL;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import java.util.StringTokenizer;
/*     */ import java.util.logging.ConsoleHandler;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ 
/*     */ class ContextFinder
/*     */ {
/*  61 */   private static final Logger logger = Logger.getLogger("javax.xml.bind");
/*     */   private static final String PLATFORM_DEFAULT_FACTORY_CLASS = "com.sun.xml.internal.bind.v2.ContextFactory";
/*     */ 
/*     */   private static void handleInvocationTargetException(InvocationTargetException x)
/*     */     throws JAXBException
/*     */   {
/*  87 */     Throwable t = x.getTargetException();
/*  88 */     if (t != null) {
/*  89 */       if ((t instanceof JAXBException))
/*     */       {
/*  91 */         throw ((JAXBException)t);
/*  92 */       }if ((t instanceof RuntimeException))
/*     */       {
/*  94 */         throw ((RuntimeException)t);
/*  95 */       }if ((t instanceof Error))
/*  96 */         throw ((Error)t);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static JAXBException handleClassCastException(Class originalType, Class targetType)
/*     */   {
/* 113 */     URL targetTypeURL = which(targetType);
/*     */ 
/* 115 */     return new JAXBException(Messages.format("JAXBContext.IllegalCast", originalType.getClassLoader().getResource("javax/xml/bind/JAXBContext.class"), targetTypeURL));
/*     */   }
/*     */ 
/*     */   static JAXBContext newInstance(String contextPath, String className, ClassLoader classLoader, Map properties)
/*     */     throws JAXBException
/*     */   {
/*     */     try
/*     */     {
/* 131 */       Class spFactory = safeLoadClass(className, classLoader);
/* 132 */       return newInstance(contextPath, spFactory, classLoader, properties);
/*     */     } catch (ClassNotFoundException x) {
/* 134 */       throw new JAXBException(Messages.format("ContextFinder.ProviderNotFound", className), x);
/*     */     }
/*     */     catch (RuntimeException x)
/*     */     {
/* 140 */       throw x;
/*     */     }
/*     */     catch (Exception x)
/*     */     {
/* 146 */       throw new JAXBException(Messages.format("ContextFinder.CouldNotInstantiate", className, x), x);
/*     */     }
/*     */   }
/*     */ 
/*     */   static JAXBContext newInstance(String contextPath, Class spFactory, ClassLoader classLoader, Map properties)
/*     */     throws JAXBException
/*     */   {
/*     */     try
/*     */     {
/* 165 */       Object context = null;
/*     */       try
/*     */       {
/* 170 */         Method m = spFactory.getMethod("createContext", new Class[] { String.class, ClassLoader.class, Map.class });
/*     */ 
/* 172 */         context = m.invoke(null, new Object[] { contextPath, classLoader, properties });
/*     */       }
/*     */       catch (NoSuchMethodException e)
/*     */       {
/*     */       }
/* 177 */       if (context == null)
/*     */       {
/* 180 */         Method m = spFactory.getMethod("createContext", new Class[] { String.class, ClassLoader.class });
/*     */ 
/* 182 */         context = m.invoke(null, new Object[] { contextPath, classLoader });
/*     */       }
/*     */ 
/* 185 */       if (!(context instanceof JAXBContext))
/*     */       {
/* 187 */         throw handleClassCastException(context.getClass(), JAXBContext.class);
/*     */       }
/* 189 */       return (JAXBContext)context;
/*     */     } catch (InvocationTargetException x) {
/* 191 */       handleInvocationTargetException(x);
/*     */ 
/* 194 */       Throwable e = x;
/* 195 */       if (x.getTargetException() != null) {
/* 196 */         e = x.getTargetException();
/*     */       }
/* 198 */       throw new JAXBException(Messages.format("ContextFinder.CouldNotInstantiate", spFactory, e), e);
/*     */     }
/*     */     catch (RuntimeException x)
/*     */     {
/* 202 */       throw x;
/*     */     }
/*     */     catch (Exception x)
/*     */     {
/* 208 */       throw new JAXBException(Messages.format("ContextFinder.CouldNotInstantiate", spFactory, x), x);
/*     */     }
/*     */   }
/*     */ 
/*     */   static JAXBContext newInstance(Class[] classes, Map properties, String className)
/*     */     throws JAXBException
/*     */   {
/* 222 */     ClassLoader cl = Thread.currentThread().getContextClassLoader();
/*     */     Class spi;
/*     */     try
/*     */     {
/* 225 */       spi = safeLoadClass(className, cl);
/*     */     } catch (ClassNotFoundException e) {
/* 227 */       throw new JAXBException(e);
/*     */     }
/*     */ 
/* 230 */     if (logger.isLoggable(Level.FINE))
/*     */     {
/* 232 */       logger.log(Level.FINE, "loaded {0} from {1}", new Object[] { className, which(spi) });
/*     */     }
/*     */ 
/* 235 */     return newInstance(classes, properties, spi);
/*     */   }
/*     */ 
/*     */   static JAXBContext newInstance(Class[] classes, Map properties, Class spFactory) throws JAXBException
/*     */   {
/*     */     Method m;
/*     */     try
/*     */     {
/* 243 */       m = spFactory.getMethod("createContext", new Class[] { [Ljava.lang.Class.class, Map.class });
/*     */     } catch (NoSuchMethodException e) {
/* 245 */       throw new JAXBException(e);
/*     */     }
/*     */     try {
/* 248 */       Object context = m.invoke(null, new Object[] { classes, properties });
/* 249 */       if (!(context instanceof JAXBContext))
/*     */       {
/* 251 */         throw handleClassCastException(context.getClass(), JAXBContext.class);
/*     */       }
/* 253 */       return (JAXBContext)context;
/*     */     } catch (IllegalAccessException e) {
/* 255 */       throw new JAXBException(e);
/*     */     } catch (InvocationTargetException e) {
/* 257 */       handleInvocationTargetException(e);
/*     */ 
/* 259 */       Throwable x = e;
/* 260 */       if (e.getTargetException() != null) {
/* 261 */         x = e.getTargetException();
/*     */       }
/* 263 */       throw new JAXBException(x);
/*     */     }
/*     */   }
/*     */ 
/*     */   static JAXBContext find(String factoryId, String contextPath, ClassLoader classLoader, Map properties)
/*     */     throws JAXBException
/*     */   {
/* 271 */     String jaxbContextFQCN = JAXBContext.class.getName();
/*     */ 
/* 275 */     StringTokenizer packages = new StringTokenizer(contextPath, ":");
/*     */ 
/* 278 */     if (!packages.hasMoreTokens())
/*     */     {
/* 280 */       throw new JAXBException(Messages.format("ContextFinder.NoPackageInContextPath"));
/*     */     }
/*     */ 
/* 283 */     logger.fine("Searching jaxb.properties");
/*     */ 
/* 285 */     while (packages.hasMoreTokens()) {
/* 286 */       String packageName = packages.nextToken(":").replace('.', '/');
/*     */ 
/* 288 */       StringBuilder propFileName = new StringBuilder().append(packageName).append("/jaxb.properties");
/*     */ 
/* 290 */       Properties props = loadJAXBProperties(classLoader, propFileName.toString());
/* 291 */       if (props != null) {
/* 292 */         if (props.containsKey(factoryId)) {
/* 293 */           String factoryClassName = props.getProperty(factoryId);
/* 294 */           return newInstance(contextPath, factoryClassName, classLoader, properties);
/*     */         }
/* 296 */         throw new JAXBException(Messages.format("ContextFinder.MissingProperty", packageName, factoryId));
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 301 */     logger.fine("Searching the system property");
/*     */ 
/* 304 */     String factoryClassName = (String)AccessController.doPrivileged(new GetPropertyAction("javax.xml.bind.context.factory"));
/* 305 */     if (factoryClassName != null) {
/* 306 */       return newInstance(contextPath, factoryClassName, classLoader, properties);
/*     */     }
/* 308 */     factoryClassName = (String)AccessController.doPrivileged(new GetPropertyAction(jaxbContextFQCN));
/* 309 */     if (factoryClassName != null) {
/* 310 */       return newInstance(contextPath, factoryClassName, classLoader, properties);
/*     */     }
/*     */ 
/* 314 */     if (Thread.currentThread().getContextClassLoader() == classLoader) {
/* 315 */       Class factory = lookupUsingOSGiServiceLoader("javax.xml.bind.JAXBContext");
/* 316 */       if (factory != null) {
/* 317 */         logger.fine("OSGi environment detected");
/* 318 */         return newInstance(contextPath, factory, classLoader, properties);
/*     */       }
/*     */     }
/*     */ 
/* 322 */     logger.fine("Searching META-INF/services");
/*     */     try
/*     */     {
/* 326 */       StringBuilder resource = new StringBuilder().append("META-INF/services/").append(jaxbContextFQCN);
/* 327 */       InputStream resourceStream = classLoader.getResourceAsStream(resource.toString());
/*     */ 
/* 330 */       if (resourceStream != null) {
/* 331 */         BufferedReader r = new BufferedReader(new InputStreamReader(resourceStream, "UTF-8"));
/* 332 */         factoryClassName = r.readLine().trim();
/* 333 */         r.close();
/* 334 */         return newInstance(contextPath, factoryClassName, classLoader, properties);
/*     */       }
/* 336 */       logger.log(Level.FINE, "Unable to load:{0}", resource.toString());
/*     */     }
/*     */     catch (UnsupportedEncodingException e)
/*     */     {
/* 340 */       throw new JAXBException(e);
/*     */     } catch (IOException e) {
/* 342 */       throw new JAXBException(e);
/*     */     }
/*     */ 
/* 346 */     logger.fine("Trying to create the platform default provider");
/* 347 */     return newInstance(contextPath, "com.sun.xml.internal.bind.v2.ContextFactory", classLoader, properties);
/*     */   }
/*     */ 
/*     */   static JAXBContext find(Class[] classes, Map properties) throws JAXBException
/*     */   {
/* 352 */     String jaxbContextFQCN = JAXBContext.class.getName();
/*     */ 
/* 356 */     for (Class c : classes)
/*     */     {
/* 358 */       ClassLoader classLoader = (ClassLoader)AccessController.doPrivileged(new PrivilegedAction() {
/*     */         public ClassLoader run() {
/* 360 */           return this.val$c.getClassLoader();
/*     */         }
/*     */       });
/* 363 */       Package pkg = c.getPackage();
/* 364 */       if (pkg != null)
/*     */       {
/* 366 */         String packageName = pkg.getName().replace('.', '/');
/*     */ 
/* 375 */         String resourceName = packageName + "/jaxb.properties";
/* 376 */         logger.log(Level.FINE, "Trying to locate {0}", resourceName);
/* 377 */         Properties props = loadJAXBProperties(classLoader, resourceName);
/* 378 */         if (props == null) {
/* 379 */           logger.fine("  not found");
/*     */         } else {
/* 381 */           logger.fine("  found");
/* 382 */           if (props.containsKey("javax.xml.bind.context.factory"))
/*     */           {
/* 384 */             String factoryClassName = props.getProperty("javax.xml.bind.context.factory").trim();
/* 385 */             return newInstance(classes, properties, factoryClassName);
/*     */           }
/* 387 */           throw new JAXBException(Messages.format("ContextFinder.MissingProperty", packageName, "javax.xml.bind.context.factory"));
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 393 */     logger.log(Level.FINE, "Checking system property {0}", "javax.xml.bind.context.factory");
/* 394 */     String factoryClassName = (String)AccessController.doPrivileged(new GetPropertyAction("javax.xml.bind.context.factory"));
/* 395 */     if (factoryClassName != null) {
/* 396 */       logger.log(Level.FINE, "  found {0}", factoryClassName);
/* 397 */       return newInstance(classes, properties, factoryClassName);
/*     */     }
/* 399 */     logger.fine("  not found");
/* 400 */     logger.log(Level.FINE, "Checking system property {0}", jaxbContextFQCN);
/* 401 */     factoryClassName = (String)AccessController.doPrivileged(new GetPropertyAction(jaxbContextFQCN));
/* 402 */     if (factoryClassName != null) {
/* 403 */       logger.log(Level.FINE, "  found {0}", factoryClassName);
/* 404 */       return newInstance(classes, properties, factoryClassName);
/*     */     }
/* 406 */     logger.fine("  not found");
/*     */ 
/* 410 */     Class factory = lookupUsingOSGiServiceLoader("javax.xml.bind.JAXBContext");
/* 411 */     if (factory != null) {
/* 412 */       logger.fine("OSGi environment detected");
/* 413 */       return newInstance(classes, properties, factory);
/*     */     }
/*     */ 
/* 417 */     logger.fine("Checking META-INF/services");
/*     */     try
/*     */     {
/* 420 */       String resource = "META-INF/services/" + jaxbContextFQCN;
/* 421 */       ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
/*     */       URL resourceURL;
/*     */       URL resourceURL;
/* 423 */       if (classLoader == null)
/* 424 */         resourceURL = ClassLoader.getSystemResource(resource);
/*     */       else {
/* 426 */         resourceURL = classLoader.getResource(resource);
/*     */       }
/* 428 */       if (resourceURL != null) {
/* 429 */         logger.log(Level.FINE, "Reading {0}", resourceURL);
/* 430 */         BufferedReader r = new BufferedReader(new InputStreamReader(resourceURL.openStream(), "UTF-8"));
/* 431 */         factoryClassName = r.readLine().trim();
/* 432 */         return newInstance(classes, properties, factoryClassName);
/*     */       }
/* 434 */       logger.log(Level.FINE, "Unable to find: {0}", resource);
/*     */     }
/*     */     catch (UnsupportedEncodingException e)
/*     */     {
/* 438 */       throw new JAXBException(e);
/*     */     } catch (IOException e) {
/* 440 */       throw new JAXBException(e);
/*     */     }
/*     */ 
/* 444 */     logger.fine("Trying to create the platform default provider");
/* 445 */     return newInstance(classes, properties, "com.sun.xml.internal.bind.v2.ContextFactory");
/*     */   }
/*     */ 
/*     */   private static Class lookupUsingOSGiServiceLoader(String factoryId)
/*     */   {
/*     */     try {
/* 451 */       Class serviceClass = Class.forName(factoryId);
/* 452 */       Class target = Class.forName("com.sun.org.glassfish.hk2.osgiresourcelocator.ServiceLoader");
/* 453 */       Method m = target.getMethod("lookupProviderClasses", new Class[] { Class.class });
/* 454 */       Iterator iter = ((Iterable)m.invoke(null, new Object[] { serviceClass })).iterator();
/* 455 */       return iter.hasNext() ? (Class)iter.next() : null;
/*     */     } catch (Exception e) {
/* 457 */       logger.log(Level.FINE, "Unable to find from OSGi: {0}", factoryId);
/* 458 */     }return null;
/*     */   }
/*     */ 
/*     */   private static Properties loadJAXBProperties(ClassLoader classLoader, String propFileName)
/*     */     throws JAXBException
/*     */   {
/* 466 */     Properties props = null;
/*     */     try
/*     */     {
/*     */       URL url;
/*     */       URL url;
/* 470 */       if (classLoader == null)
/* 471 */         url = ClassLoader.getSystemResource(propFileName);
/*     */       else {
/* 473 */         url = classLoader.getResource(propFileName);
/*     */       }
/* 475 */       if (url != null) {
/* 476 */         logger.log(Level.FINE, "loading props from {0}", url);
/* 477 */         props = new Properties();
/* 478 */         InputStream is = url.openStream();
/* 479 */         props.load(is);
/* 480 */         is.close();
/*     */       }
/*     */     } catch (IOException ioe) {
/* 483 */       logger.log(Level.FINE, "Unable to load " + propFileName, ioe);
/* 484 */       throw new JAXBException(ioe.toString(), ioe);
/*     */     }
/*     */ 
/* 487 */     return props;
/*     */   }
/*     */ 
/*     */   static URL which(Class clazz, ClassLoader loader)
/*     */   {
/* 505 */     String classnameAsResource = clazz.getName().replace('.', '/') + ".class";
/*     */ 
/* 507 */     if (loader == null) {
/* 508 */       loader = ClassLoader.getSystemClassLoader();
/*     */     }
/*     */ 
/* 511 */     return loader.getResource(classnameAsResource);
/*     */   }
/*     */ 
/*     */   static URL which(Class clazz)
/*     */   {
/* 527 */     return which(clazz, clazz.getClassLoader());
/*     */   }
/*     */ 
/*     */   private static Class safeLoadClass(String className, ClassLoader classLoader)
/*     */     throws ClassNotFoundException
/*     */   {
/* 549 */     logger.log(Level.FINE, "Trying to load {0}", className);
/*     */     try
/*     */     {
/* 552 */       SecurityManager s = System.getSecurityManager();
/* 553 */       if (s != null) {
/* 554 */         int i = className.lastIndexOf('.');
/* 555 */         if (i != -1) {
/* 556 */           s.checkPackageAccess(className.substring(0, i));
/*     */         }
/*     */       }
/*     */ 
/* 560 */       if (classLoader == null) {
/* 561 */         return Class.forName(className);
/*     */       }
/* 563 */       return classLoader.loadClass(className);
/*     */     }
/*     */     catch (SecurityException se)
/*     */     {
/* 567 */       if ("com.sun.xml.internal.bind.v2.ContextFactory".equals(className)) {
/* 568 */         return Class.forName(className);
/*     */       }
/* 570 */       throw se;
/*     */     }
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*     */     try
/*     */     {
/*  63 */       if (AccessController.doPrivileged(new GetPropertyAction("jaxb.debug")) != null)
/*     */       {
/*  66 */         logger.setUseParentHandlers(false);
/*  67 */         logger.setLevel(Level.ALL);
/*  68 */         ConsoleHandler handler = new ConsoleHandler();
/*  69 */         handler.setLevel(Level.ALL);
/*  70 */         logger.addHandler(handler);
/*     */       }
/*     */     }
/*     */     catch (Throwable t)
/*     */     {
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.bind.ContextFinder
 * JD-Core Version:    0.6.2
 */