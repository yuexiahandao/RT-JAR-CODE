/*     */ package javax.xml.validation;
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
/*     */ class SchemaFactoryFinder
/*     */ {
/*  52 */   private static boolean debug = false;
/*     */ 
/*  56 */   private static SecuritySupport ss = new SecuritySupport();
/*     */   private static final String DEFAULT_PACKAGE = "com.sun.org.apache.xerces.internal";
/*  61 */   private static Properties cacheProps = new Properties();
/*     */ 
/*  66 */   private static volatile boolean firstTime = true;
/*     */   private final ClassLoader classLoader;
/* 525 */   private static final Class SERVICE_CLASS = SchemaFactory.class;
/* 526 */   private static final String SERVICE_ID = "META-INF/services/" + SERVICE_CLASS.getName();
/*     */ 
/*     */   private static void debugPrintln(String msg)
/*     */   {
/*  83 */     if (debug)
/*  84 */       System.err.println("JAXP: " + msg);
/*     */   }
/*     */ 
/*     */   public SchemaFactoryFinder(ClassLoader loader)
/*     */   {
/* 105 */     this.classLoader = loader;
/* 106 */     if (debug)
/* 107 */       debugDisplayClassLoader();
/*     */   }
/*     */ 
/*     */   private void debugDisplayClassLoader()
/*     */   {
/*     */     try {
/* 113 */       if (this.classLoader == ss.getContextClassLoader()) {
/* 114 */         debugPrintln("using thread context class loader (" + this.classLoader + ") for search");
/* 115 */         return;
/*     */       }
/*     */     }
/*     */     catch (Throwable _)
/*     */     {
/*     */     }
/* 121 */     if (this.classLoader == ClassLoader.getSystemClassLoader()) {
/* 122 */       debugPrintln("using system class loader (" + this.classLoader + ") for search");
/* 123 */       return;
/*     */     }
/*     */ 
/* 126 */     debugPrintln("using class loader (" + this.classLoader + ") for search");
/*     */   }
/*     */ 
/*     */   public SchemaFactory newFactory(String schemaLanguage)
/*     */   {
/* 143 */     if (schemaLanguage == null) throw new NullPointerException();
/* 144 */     SchemaFactory f = _newFactory(schemaLanguage);
/* 145 */     if (f != null)
/* 146 */       debugPrintln("factory '" + f.getClass().getName() + "' was found for " + schemaLanguage);
/*     */     else {
/* 148 */       debugPrintln("unable to find a factory for " + schemaLanguage);
/*     */     }
/* 150 */     return f;
/*     */   }
/*     */ 
/*     */   private SchemaFactory _newFactory(String schemaLanguage)
/*     */   {
/* 163 */     String propertyName = SERVICE_CLASS.getName() + ":" + schemaLanguage;
/*     */     try
/*     */     {
/* 167 */       debugPrintln("Looking up system property '" + propertyName + "'");
/* 168 */       String r = ss.getSystemProperty(propertyName);
/* 169 */       if (r != null) {
/* 170 */         debugPrintln("The value is '" + r + "'");
/* 171 */         SchemaFactory sf = createInstance(r, true);
/* 172 */         if (sf != null) return sf; 
/*     */       }
/* 174 */       else { debugPrintln("The property is undefined."); }
/*     */     } catch (Throwable t) {
/* 176 */       if (debug) {
/* 177 */         debugPrintln("failed to look up system property '" + propertyName + "'");
/* 178 */         t.printStackTrace();
/*     */       }
/*     */     }
/*     */ 
/* 182 */     String javah = ss.getSystemProperty("java.home");
/* 183 */     String configFile = javah + File.separator + "lib" + File.separator + "jaxp.properties";
/*     */ 
/* 186 */     String factoryClassName = null;
/*     */     try
/*     */     {
/* 190 */       if (firstTime) {
/* 191 */         synchronized (cacheProps) {
/* 192 */           if (firstTime) {
/* 193 */             File f = new File(configFile);
/* 194 */             firstTime = false;
/* 195 */             if (ss.doesFileExist(f)) {
/* 196 */               debugPrintln("Read properties file " + f);
/* 197 */               cacheProps.load(ss.getFileInputStream(f));
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/* 202 */       factoryClassName = cacheProps.getProperty(propertyName);
/* 203 */       debugPrintln("found " + factoryClassName + " in $java.home/jaxp.properties");
/*     */ 
/* 205 */       if (factoryClassName != null) {
/* 206 */         SchemaFactory sf = createInstance(factoryClassName, true);
/* 207 */         if (sf != null)
/* 208 */           return sf;
/*     */       }
/*     */     }
/*     */     catch (Exception ex) {
/* 212 */       if (debug) {
/* 213 */         ex.printStackTrace();
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 218 */     Iterator sitr = createServiceFileIterator();
/* 219 */     while (sitr.hasNext()) {
/* 220 */       URL resource = (URL)sitr.next();
/* 221 */       debugPrintln("looking into " + resource);
/*     */       try {
/* 223 */         SchemaFactory sf = loadFromService(schemaLanguage, resource.toExternalForm(), ss.getURLInputStream(resource));
/*     */ 
/* 225 */         if (sf != null) return sf; 
/*     */       }
/* 227 */       catch (IOException e) { if (debug) {
/* 228 */           debugPrintln("failed to read " + resource);
/* 229 */           e.printStackTrace();
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 235 */     if (schemaLanguage.equals("http://www.w3.org/2001/XMLSchema")) {
/* 236 */       debugPrintln("attempting to use the platform default XML Schema validator");
/* 237 */       return createInstance("com.sun.org.apache.xerces.internal.jaxp.validation.XMLSchemaFactory", true);
/*     */     }
/*     */ 
/* 240 */     debugPrintln("all things were tried, but none was found. bailing out.");
/* 241 */     return null;
/*     */   }
/*     */ 
/*     */   private Class createClass(String className)
/*     */   {
/* 252 */     boolean internal = false;
/* 253 */     if ((System.getSecurityManager() != null) && 
/* 254 */       (className != null) && (className.startsWith("com.sun.org.apache.xerces.internal")))
/* 255 */       internal = true;
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
/*     */   SchemaFactory createInstance(String className)
/*     */   {
/* 283 */     return createInstance(className, false);
/*     */   }
/*     */ 
/*     */   SchemaFactory createInstance(String className, boolean useServicesMechanism) {
/* 287 */     SchemaFactory schemaFactory = null;
/*     */ 
/* 289 */     debugPrintln("createInstance(" + className + ")");
/*     */ 
/* 292 */     Class clazz = createClass(className);
/* 293 */     if (clazz == null) {
/* 294 */       debugPrintln("failed to getClass(" + className + ")");
/* 295 */       return null;
/*     */     }
/* 297 */     debugPrintln("loaded " + className + " from " + which(clazz));
/*     */     try
/*     */     {
/* 301 */       if (!useServicesMechanism) {
/* 302 */         schemaFactory = (SchemaFactory)newInstanceNoServiceLoader(clazz);
/*     */       }
/* 304 */       if (schemaFactory == null)
/* 305 */         schemaFactory = (SchemaFactory)clazz.newInstance();
/*     */     }
/*     */     catch (ClassCastException classCastException) {
/* 308 */       debugPrintln("could not instantiate " + clazz.getName());
/* 309 */       if (debug) {
/* 310 */         classCastException.printStackTrace();
/*     */       }
/* 312 */       return null;
/*     */     } catch (IllegalAccessException illegalAccessException) {
/* 314 */       debugPrintln("could not instantiate " + clazz.getName());
/* 315 */       if (debug) {
/* 316 */         illegalAccessException.printStackTrace();
/*     */       }
/* 318 */       return null;
/*     */     } catch (InstantiationException instantiationException) {
/* 320 */       debugPrintln("could not instantiate " + clazz.getName());
/* 321 */       if (debug) {
/* 322 */         instantiationException.printStackTrace();
/*     */       }
/* 324 */       return null;
/*     */     }
/*     */ 
/* 327 */     return schemaFactory;
/*     */   }
/*     */ 
/*     */   private static Object newInstanceNoServiceLoader(Class<?> providerClass)
/*     */   {
/* 337 */     if (System.getSecurityManager() == null)
/* 338 */       return null;
/*     */     try
/*     */     {
/* 341 */       Method creationMethod = providerClass.getDeclaredMethod("newXMLSchemaFactoryNoServiceLoader", new Class[0]);
/*     */ 
/* 345 */       return creationMethod.invoke(null, (Object[])null);
/*     */     } catch (NoSuchMethodException exc) {
/* 347 */       return null; } catch (Exception exc) {
/*     */     }
/* 349 */     return null;
/*     */   }
/*     */ 
/*     */   private SchemaFactory loadFromProperty(String keyName, String resourceName, InputStream in)
/*     */     throws IOException
/*     */   {
/* 377 */     debugPrintln("Reading " + resourceName);
/*     */ 
/* 379 */     Properties props = new Properties();
/* 380 */     props.load(in);
/* 381 */     in.close();
/* 382 */     String factoryClassName = props.getProperty(keyName);
/* 383 */     if (factoryClassName != null) {
/* 384 */       debugPrintln("found " + keyName + " = " + factoryClassName);
/* 385 */       return createInstance(factoryClassName);
/*     */     }
/* 387 */     debugPrintln(keyName + " is not in the property file");
/* 388 */     return null;
/*     */   }
/*     */ 
/*     */   private SchemaFactory loadFromService(String schemaLanguage, String inputName, InputStream in)
/*     */     throws IOException
/*     */   {
/* 411 */     SchemaFactory schemaFactory = null;
/* 412 */     Class[] stringClassArray = { "".getClass() };
/* 413 */     Object[] schemaLanguageObjectArray = { schemaLanguage };
/* 414 */     String isSchemaLanguageSupportedMethod = "isSchemaLanguageSupported";
/*     */ 
/* 416 */     debugPrintln("Reading " + inputName);
/*     */ 
/* 419 */     BufferedReader configFile = new BufferedReader(new InputStreamReader(in));
/* 420 */     String line = null;
/* 421 */     while ((line = configFile.readLine()) != null)
/*     */     {
/* 423 */       int comment = line.indexOf("#");
/* 424 */       switch (comment) { case -1:
/* 425 */         break;
/*     */       case 0:
/* 426 */         line = ""; break;
/*     */       default:
/* 427 */         line = line.substring(0, comment);
/*     */       }
/*     */ 
/* 431 */       line = line.trim();
/*     */ 
/* 434 */       if (line.length() != 0)
/*     */       {
/* 439 */         Class clazz = createClass(line);
/* 440 */         if (clazz != null)
/*     */         {
/*     */           try
/*     */           {
/* 446 */             schemaFactory = (SchemaFactory)clazz.newInstance();
/*     */           } catch (ClassCastException classCastExcpetion) {
/* 448 */             schemaFactory = null;
/* 449 */             continue;
/*     */           } catch (InstantiationException instantiationException) {
/* 451 */             schemaFactory = null;
/* 452 */             continue;
/*     */           } catch (IllegalAccessException illegalAccessException) {
/* 454 */             schemaFactory = null;
/* 455 */           }continue;
/*     */           try
/*     */           {
/* 460 */             Method isSchemaLanguageSupported = clazz.getMethod("isSchemaLanguageSupported", stringClassArray);
/* 461 */             Boolean supported = (Boolean)isSchemaLanguageSupported.invoke(schemaFactory, schemaLanguageObjectArray);
/* 462 */             if (supported.booleanValue())
/* 463 */               break;
/*     */           }
/*     */           catch (NoSuchMethodException noSuchMethodException)
/*     */           {
/*     */           }
/*     */           catch (IllegalAccessException illegalAccessException) {
/*     */           }
/*     */           catch (InvocationTargetException invocationTargetException) {
/*     */           }
/* 472 */           schemaFactory = null;
/*     */         }
/*     */       }
/*     */     }
/* 476 */     configFile.close();
/*     */ 
/* 479 */     return schemaFactory; } 
/*     */   private Iterator createServiceFileIterator() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield 419	javax/xml/validation/SchemaFactoryFinder:classLoader	Ljava/lang/ClassLoader;
/*     */     //   4: ifnonnull +12 -> 16
/*     */     //   7: new 297	javax/xml/validation/SchemaFactoryFinder$1
/*     */     //   10: dup
/*     */     //   11: aload_0
/*     */     //   12: invokespecial 479	javax/xml/validation/SchemaFactoryFinder$1:<init>	(Ljavax/xml/validation/SchemaFactoryFinder;)V
/*     */     //   15: areturn
/*     */     //   16: getstatic 422	javax/xml/validation/SchemaFactoryFinder:ss	Ljavax/xml/validation/SecuritySupport;
/*     */     //   19: aload_0
/*     */     //   20: getfield 419	javax/xml/validation/SchemaFactoryFinder:classLoader	Ljava/lang/ClassLoader;
/*     */     //   23: getstatic 420	javax/xml/validation/SchemaFactoryFinder:SERVICE_ID	Ljava/lang/String;
/*     */     //   26: invokevirtual 488	javax/xml/validation/SecuritySupport:getResources	(Ljava/lang/ClassLoader;Ljava/lang/String;)Ljava/util/Enumeration;
/*     */     //   29: astore_1
/*     */     //   30: aload_1
/*     */     //   31: invokeinterface 489 1 0
/*     */     //   36: ifne +32 -> 68
/*     */     //   39: new 285	java/lang/StringBuilder
/*     */     //   42: dup
/*     */     //   43: invokespecial 454	java/lang/StringBuilder:<init>	()V
/*     */     //   46: ldc 42
/*     */     //   48: invokevirtual 457	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   51: getstatic 420	javax/xml/validation/SchemaFactoryFinder:SERVICE_ID	Ljava/lang/String;
/*     */     //   54: invokevirtual 457	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   57: ldc 3
/*     */     //   59: invokevirtual 457	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   62: invokevirtual 455	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*     */     //   65: invokestatic 469	javax/xml/validation/SchemaFactoryFinder:debugPrintln	(Ljava/lang/String;)V
/*     */     //   68: new 298	javax/xml/validation/SchemaFactoryFinder$2
/*     */     //   71: dup
/*     */     //   72: aload_0
/*     */     //   73: aload_1
/*     */     //   74: invokespecial 480	javax/xml/validation/SchemaFactoryFinder$2:<init>	(Ljavax/xml/validation/SchemaFactoryFinder;Ljava/util/Enumeration;)V
/*     */     //   77: areturn
/*     */     //   78: astore_1
/*     */     //   79: new 285	java/lang/StringBuilder
/*     */     //   82: dup
/*     */     //   83: invokespecial 454	java/lang/StringBuilder:<init>	()V
/*     */     //   86: ldc 28
/*     */     //   88: invokevirtual 457	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   91: getstatic 420	javax/xml/validation/SchemaFactoryFinder:SERVICE_ID	Ljava/lang/String;
/*     */     //   94: invokevirtual 457	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   97: invokevirtual 455	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*     */     //   100: invokestatic 469	javax/xml/validation/SchemaFactoryFinder:debugPrintln	(Ljava/lang/String;)V
/*     */     //   103: getstatic 416	javax/xml/validation/SchemaFactoryFinder:debug	Z
/*     */     //   106: ifeq +7 -> 113
/*     */     //   109: aload_1
/*     */     //   110: invokevirtual 427	java/io/IOException:printStackTrace	()V
/*     */     //   113: new 291	java/util/ArrayList
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
/*  71 */       debug = ss.getSystemProperty("jaxp.debug") != null;
/*     */     } catch (Exception _) {
/*  73 */       debug = false;
/*     */     }
/*     */   }
/*     */ 
/*     */   private static abstract class SingleIterator
/*     */     implements Iterator
/*     */   {
/* 355 */     private boolean seen = false;
/*     */ 
/* 357 */     public final void remove() { throw new UnsupportedOperationException(); } 
/* 358 */     public final boolean hasNext() { return !this.seen; } 
/*     */     public final Object next() {
/* 360 */       if (this.seen) throw new NoSuchElementException();
/* 361 */       this.seen = true;
/* 362 */       return value();
/*     */     }
/*     */ 
/*     */     protected abstract Object value();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.validation.SchemaFactoryFinder
 * JD-Core Version:    0.6.2
 */