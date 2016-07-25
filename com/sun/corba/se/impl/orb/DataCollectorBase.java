/*     */ package com.sun.corba.se.impl.orb;
/*     */ 
/*     */ import com.sun.corba.se.impl.orbutil.GetPropertyAction;
/*     */ import com.sun.corba.se.spi.orb.DataCollector;
/*     */ import com.sun.corba.se.spi.orb.PropertyParser;
/*     */ import java.applet.Applet;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.Enumeration;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.Properties;
/*     */ import java.util.Set;
/*     */ import java.util.StringTokenizer;
/*     */ 
/*     */ public abstract class DataCollectorBase
/*     */   implements DataCollector
/*     */ {
/*     */   private PropertyParser parser;
/*     */   private Set propertyNames;
/*     */   private Set propertyPrefixes;
/*     */   private Set URLPropertyNames;
/*     */   protected String localHostName;
/*     */   protected String configurationHostName;
/*     */   private boolean setParserCalled;
/*     */   private Properties originalProps;
/*     */   private Properties resultProps;
/*     */ 
/*     */   public DataCollectorBase(Properties paramProperties, String paramString1, String paramString2)
/*     */   {
/*  72 */     this.URLPropertyNames = new HashSet();
/*  73 */     this.URLPropertyNames.add("org.omg.CORBA.ORBInitialServices");
/*     */ 
/*  75 */     this.propertyNames = new HashSet();
/*     */ 
/*  80 */     this.propertyNames.add("org.omg.CORBA.ORBInitRef");
/*     */ 
/*  82 */     this.propertyPrefixes = new HashSet();
/*     */ 
/*  84 */     this.originalProps = paramProperties;
/*  85 */     this.localHostName = paramString1;
/*  86 */     this.configurationHostName = paramString2;
/*  87 */     this.setParserCalled = false;
/*  88 */     this.resultProps = new Properties();
/*     */   }
/*     */ 
/*     */   public boolean initialHostIsLocal()
/*     */   {
/*  97 */     checkSetParserCalled();
/*  98 */     return this.localHostName.equals(this.resultProps.getProperty("org.omg.CORBA.ORBInitialHost"));
/*     */   }
/*     */ 
/*     */   public void setParser(PropertyParser paramPropertyParser)
/*     */   {
/* 104 */     Iterator localIterator = paramPropertyParser.iterator();
/* 105 */     while (localIterator.hasNext()) {
/* 106 */       ParserAction localParserAction = (ParserAction)localIterator.next();
/* 107 */       if (localParserAction.isPrefix())
/* 108 */         this.propertyPrefixes.add(localParserAction.getPropertyName());
/*     */       else {
/* 110 */         this.propertyNames.add(localParserAction.getPropertyName());
/*     */       }
/*     */     }
/* 113 */     collect();
/* 114 */     this.setParserCalled = true;
/*     */   }
/*     */ 
/*     */   public Properties getProperties()
/*     */   {
/* 119 */     checkSetParserCalled();
/* 120 */     return this.resultProps;
/*     */   }
/*     */ 
/*     */   public abstract boolean isApplet();
/*     */ 
/*     */   protected abstract void collect();
/*     */ 
/*     */   protected void checkPropertyDefaults()
/*     */   {
/* 142 */     String str1 = this.resultProps.getProperty("org.omg.CORBA.ORBInitialHost");
/*     */ 
/* 145 */     if ((str1 == null) || (str1.equals(""))) {
/* 146 */       setProperty("org.omg.CORBA.ORBInitialHost", this.configurationHostName);
/*     */     }
/*     */ 
/* 149 */     String str2 = this.resultProps.getProperty("com.sun.CORBA.ORBServerHost");
/*     */ 
/* 152 */     if ((str2 == null) || (str2.equals("")) || (str2.equals("0.0.0.0")) || (str2.equals("::")) || (str2.toLowerCase().equals("::ffff:0.0.0.0")))
/*     */     {
/* 158 */       setProperty("com.sun.CORBA.ORBServerHost", this.localHostName);
/*     */ 
/* 160 */       setProperty("com.sun.CORBA.INTERNAL USE ONLY: listen on all interfaces", "com.sun.CORBA.INTERNAL USE ONLY: listen on all interfaces");
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void findPropertiesFromArgs(String[] paramArrayOfString)
/*     */   {
/* 167 */     if (paramArrayOfString == null) {
/* 168 */       return;
/*     */     }
/*     */ 
/* 176 */     for (int i = 0; i < paramArrayOfString.length; i++) {
/* 177 */       String str2 = null;
/* 178 */       String str1 = null;
/*     */ 
/* 180 */       if ((paramArrayOfString[i] != null) && (paramArrayOfString[i].startsWith("-ORB"))) {
/* 181 */         String str3 = paramArrayOfString[i].substring(1);
/* 182 */         str1 = findMatchingPropertyName(this.propertyNames, str3);
/*     */ 
/* 184 */         if ((str1 != null) && 
/* 185 */           (i + 1 < paramArrayOfString.length) && (paramArrayOfString[(i + 1)] != null)) {
/* 186 */           str2 = paramArrayOfString[(++i)];
/*     */         }
/*     */       }
/*     */ 
/* 190 */       if (str2 != null)
/* 191 */         setProperty(str1, str2);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void findPropertiesFromApplet(final Applet paramApplet)
/*     */   {
/* 200 */     if (paramApplet == null) {
/* 201 */       return;
/*     */     }
/* 203 */     PropertyCallback local1 = new PropertyCallback() {
/*     */       public String get(String paramAnonymousString) {
/* 205 */         return paramApplet.getParameter(paramAnonymousString);
/*     */       }
/*     */     };
/* 209 */     findPropertiesByName(this.propertyNames.iterator(), local1);
/*     */ 
/* 217 */     PropertyCallback local2 = new PropertyCallback() {
/*     */       public String get(String paramAnonymousString) {
/* 219 */         String str = DataCollectorBase.this.resultProps.getProperty(paramAnonymousString);
/* 220 */         if (str == null)
/* 221 */           return null;
/*     */         try
/*     */         {
/* 224 */           URL localURL = new URL(paramApplet.getDocumentBase(), str);
/* 225 */           return localURL.toExternalForm();
/*     */         }
/*     */         catch (MalformedURLException localMalformedURLException) {
/*     */         }
/* 229 */         return str;
/*     */       }
/*     */     };
/* 234 */     findPropertiesByName(this.URLPropertyNames.iterator(), local2);
/*     */   }
/*     */ 
/*     */   private void doProperties(final Properties paramProperties)
/*     */   {
/* 240 */     PropertyCallback local3 = new PropertyCallback() {
/*     */       public String get(String paramAnonymousString) {
/* 242 */         return paramProperties.getProperty(paramAnonymousString);
/*     */       }
/*     */     };
/* 246 */     findPropertiesByName(this.propertyNames.iterator(), local3);
/*     */ 
/* 248 */     findPropertiesByPrefix(this.propertyPrefixes, makeIterator(paramProperties.propertyNames()), local3);
/*     */   }
/*     */ 
/*     */   protected void findPropertiesFromFile()
/*     */   {
/* 254 */     Properties localProperties = getFileProperties();
/* 255 */     if (localProperties == null) {
/* 256 */       return;
/*     */     }
/* 258 */     doProperties(localProperties);
/*     */   }
/*     */ 
/*     */   protected void findPropertiesFromProperties()
/*     */   {
/* 263 */     if (this.originalProps == null) {
/* 264 */       return;
/*     */     }
/* 266 */     doProperties(this.originalProps);
/*     */   }
/*     */ 
/*     */   protected void findPropertiesFromSystem()
/*     */   {
/* 278 */     Set localSet1 = getCORBAPrefixes(this.propertyNames);
/* 279 */     Set localSet2 = getCORBAPrefixes(this.propertyPrefixes);
/*     */ 
/* 281 */     PropertyCallback local4 = new PropertyCallback() {
/*     */       public String get(String paramAnonymousString) {
/* 283 */         return DataCollectorBase.getSystemProperty(paramAnonymousString);
/*     */       }
/*     */     };
/* 287 */     findPropertiesByName(localSet1.iterator(), local4);
/*     */ 
/* 289 */     findPropertiesByPrefix(localSet2, getSystemPropertyNames(), local4);
/*     */   }
/*     */ 
/*     */   private void setProperty(String paramString1, String paramString2)
/*     */   {
/* 302 */     if (paramString1.equals("org.omg.CORBA.ORBInitRef"))
/*     */     {
/* 304 */       StringTokenizer localStringTokenizer = new StringTokenizer(paramString2, "=");
/* 305 */       if (localStringTokenizer.countTokens() != 2) {
/* 306 */         throw new IllegalArgumentException();
/*     */       }
/* 308 */       String str1 = localStringTokenizer.nextToken();
/* 309 */       String str2 = localStringTokenizer.nextToken();
/*     */ 
/* 311 */       this.resultProps.setProperty(paramString1 + "." + str1, str2);
/*     */     } else {
/* 313 */       this.resultProps.setProperty(paramString1, paramString2);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void checkSetParserCalled()
/*     */   {
/* 319 */     if (!this.setParserCalled)
/* 320 */       throw new IllegalStateException("setParser not called.");
/*     */   }
/*     */ 
/*     */   private void findPropertiesByPrefix(Set paramSet, Iterator paramIterator, PropertyCallback paramPropertyCallback)
/*     */   {
/* 329 */     while (paramIterator.hasNext()) {
/* 330 */       String str1 = (String)paramIterator.next();
/* 331 */       Iterator localIterator = paramSet.iterator();
/* 332 */       while (localIterator.hasNext()) {
/* 333 */         String str2 = (String)localIterator.next();
/* 334 */         if (str1.startsWith(str2)) {
/* 335 */           String str3 = paramPropertyCallback.get(str1);
/*     */ 
/* 339 */           setProperty(str1, str3);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private void findPropertiesByName(Iterator paramIterator, PropertyCallback paramPropertyCallback)
/*     */   {
/* 351 */     while (paramIterator.hasNext()) {
/* 352 */       String str1 = (String)paramIterator.next();
/* 353 */       String str2 = paramPropertyCallback.get(str1);
/* 354 */       if (str2 != null)
/* 355 */         setProperty(str1, str2);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static String getSystemProperty(String paramString)
/*     */   {
/* 361 */     return (String)AccessController.doPrivileged(new GetPropertyAction(paramString));
/*     */   }
/*     */ 
/*     */   private String findMatchingPropertyName(Set paramSet, String paramString)
/*     */   {
/* 370 */     Iterator localIterator = paramSet.iterator();
/* 371 */     while (localIterator.hasNext()) {
/* 372 */       String str = (String)localIterator.next();
/* 373 */       if (str.endsWith(paramString)) {
/* 374 */         return str;
/*     */       }
/*     */     }
/* 377 */     return null;
/*     */   }
/*     */ 
/*     */   private static Iterator makeIterator(Enumeration paramEnumeration)
/*     */   {
/* 382 */     return new Iterator() {
/* 383 */       public boolean hasNext() { return this.val$enumeration.hasMoreElements(); } 
/* 384 */       public Object next() { return this.val$enumeration.nextElement(); } 
/* 385 */       public void remove() { throw new UnsupportedOperationException(); }
/*     */ 
/*     */     };
/*     */   }
/*     */ 
/*     */   private static Iterator getSystemPropertyNames()
/*     */   {
/* 393 */     Enumeration localEnumeration = (Enumeration)AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public Object run()
/*     */       {
/* 397 */         return System.getProperties().propertyNames();
/*     */       }
/*     */     });
/* 402 */     return makeIterator(localEnumeration);
/*     */   }
/*     */ 
/*     */   private void getPropertiesFromFile(Properties paramProperties, String paramString)
/*     */   {
/*     */     try {
/* 408 */       File localFile = new File(paramString);
/* 409 */       if (!localFile.exists()) {
/* 410 */         return;
/*     */       }
/* 412 */       FileInputStream localFileInputStream = new FileInputStream(localFile);
/*     */       try
/*     */       {
/* 415 */         paramProperties.load(localFileInputStream);
/*     */       } finally {
/* 417 */         localFileInputStream.close();
/*     */       }
/*     */     }
/*     */     catch (Exception localException)
/*     */     {
/*     */     }
/*     */   }
/*     */ 
/*     */   private Properties getFileProperties()
/*     */   {
/* 428 */     Properties localProperties1 = new Properties();
/*     */ 
/* 430 */     String str1 = getSystemProperty("java.home");
/* 431 */     String str2 = str1 + File.separator + "lib" + File.separator + "orb.properties";
/*     */ 
/* 434 */     getPropertiesFromFile(localProperties1, str2);
/*     */ 
/* 436 */     Properties localProperties2 = new Properties(localProperties1);
/*     */ 
/* 438 */     String str3 = getSystemProperty("user.home");
/* 439 */     str2 = str3 + File.separator + "orb.properties";
/*     */ 
/* 441 */     getPropertiesFromFile(localProperties2, str2);
/* 442 */     return localProperties2;
/*     */   }
/*     */ 
/*     */   private boolean hasCORBAPrefix(String paramString)
/*     */   {
/* 447 */     return (paramString.startsWith("org.omg.")) || (paramString.startsWith("com.sun.CORBA.")) || (paramString.startsWith("com.sun.corba.")) || (paramString.startsWith("com.sun.corba.se."));
/*     */   }
/*     */ 
/*     */   private Set getCORBAPrefixes(Set paramSet)
/*     */   {
/* 457 */     HashSet localHashSet = new HashSet();
/* 458 */     Iterator localIterator = paramSet.iterator();
/* 459 */     while (localIterator.hasNext()) {
/* 460 */       String str = (String)localIterator.next();
/* 461 */       if (hasCORBAPrefix(str)) {
/* 462 */         localHashSet.add(str);
/*     */       }
/*     */     }
/* 465 */     return localHashSet;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.orb.DataCollectorBase
 * JD-Core Version:    0.6.2
 */