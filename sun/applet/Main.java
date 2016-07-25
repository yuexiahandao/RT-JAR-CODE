/*     */ package sun.applet;
/*     */ 
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Properties;
/*     */ import java.util.Vector;
/*     */ import sun.net.www.ParseUtil;
/*     */ 
/*     */ public class Main
/*     */ {
/*  69 */   static File theUserPropertiesFile = new File(localFile, ".appletviewer");
/*     */ 
/*  54 */   static final String[][] avDefaultUserProps = { { "http.proxyHost", "" }, { "http.proxyPort", "80" }, { "package.restrict.access.sun", "true" } };
/*     */ 
/*  73 */   private static AppletMessageHandler amh = new AppletMessageHandler("appletviewer");
/*     */   private boolean debugFlag;
/*     */   private boolean helpFlag;
/*     */   private String encoding;
/*     */   private boolean noSecurityFlag;
/*  82 */   private static boolean cmdLineTestFlag = false;
/*     */ 
/*  87 */   private static Vector urlList = new Vector(1);
/*     */ 
/*  91 */   public static final String theVersion = System.getProperty("java.version");
/*     */ 
/*     */   public Main()
/*     */   {
/*  78 */     this.debugFlag = false;
/*  79 */     this.helpFlag = false;
/*  80 */     this.encoding = null;
/*  81 */     this.noSecurityFlag = false;
/*     */   }
/*     */ 
/*     */   public static void main(String[] paramArrayOfString)
/*     */   {
/*  97 */     Main localMain = new Main();
/*  98 */     int i = localMain.run(paramArrayOfString);
/*     */ 
/* 103 */     if ((i != 0) || (cmdLineTestFlag))
/* 104 */       System.exit(i);
/*     */   }
/*     */ 
/*     */   private int run(String[] paramArrayOfString)
/*     */   {
/*     */     try {
/* 110 */       if (paramArrayOfString.length == 0) {
/* 111 */         usage();
/* 112 */         return 0;
/*     */       }
/* 114 */       for (i = 0; i < paramArrayOfString.length; ) {
/* 115 */         int k = decodeArg(paramArrayOfString, i);
/* 116 */         if (k == 0) {
/* 117 */           throw new ParseException(lookup("main.err.unrecognizedarg", paramArrayOfString[i]));
/*     */         }
/*     */ 
/* 120 */         i += k;
/*     */       }
/*     */     }
/*     */     catch (ParseException localParseException)
/*     */     {
/*     */       int i;
/* 123 */       System.err.println(localParseException.getMessage());
/* 124 */       return 1;
/*     */     }
/*     */ 
/* 128 */     if (this.helpFlag) {
/* 129 */       usage();
/* 130 */       return 0;
/*     */     }
/*     */ 
/* 133 */     if (urlList.size() == 0) {
/* 134 */       System.err.println(lookup("main.err.inputfile"));
/* 135 */       return 1;
/*     */     }
/*     */ 
/* 138 */     if (this.debugFlag)
/*     */     {
/* 143 */       return invokeDebugger(paramArrayOfString);
/*     */     }
/*     */ 
/* 147 */     if ((!this.noSecurityFlag) && (System.getSecurityManager() == null)) {
/* 148 */       init();
/*     */     }
/*     */ 
/* 151 */     for (int j = 0; j < urlList.size(); j++)
/*     */     {
/*     */       try
/*     */       {
/* 156 */         AppletViewer.parse((URL)urlList.elementAt(j), this.encoding);
/*     */       } catch (IOException localIOException) {
/* 158 */         System.err.println(lookup("main.err.io", localIOException.getMessage()));
/* 159 */         return 1;
/*     */       }
/*     */     }
/* 162 */     return 0;
/*     */   }
/*     */ 
/*     */   private static void usage() {
/* 166 */     System.out.println(lookup("usage"));
/*     */   }
/*     */ 
/*     */   private int decodeArg(String[] paramArrayOfString, int paramInt)
/*     */     throws Main.ParseException
/*     */   {
/* 182 */     String str = paramArrayOfString[paramInt];
/* 183 */     int i = paramArrayOfString.length;
/*     */ 
/* 185 */     if (("-help".equalsIgnoreCase(str)) || ("-?".equals(str))) {
/* 186 */       this.helpFlag = true;
/* 187 */       return 1;
/* 188 */     }if (("-encoding".equals(str)) && (paramInt < i - 1)) {
/* 189 */       if (this.encoding != null)
/* 190 */         throw new ParseException(lookup("main.err.dupoption", str));
/* 191 */       this.encoding = paramArrayOfString[(++paramInt)];
/* 192 */       return 2;
/* 193 */     }if ("-debug".equals(str)) {
/* 194 */       this.debugFlag = true;
/* 195 */       return 1;
/* 196 */     }if ("-Xnosecurity".equals(str))
/*     */     {
/* 201 */       System.err.println();
/* 202 */       System.err.println(lookup("main.warn.nosecmgr"));
/* 203 */       System.err.println();
/*     */ 
/* 205 */       this.noSecurityFlag = true;
/* 206 */       return 1;
/* 207 */     }if ("-XcmdLineTest".equals(str))
/*     */     {
/* 211 */       cmdLineTestFlag = true;
/* 212 */       return 1;
/* 213 */     }if (str.startsWith("-")) {
/* 214 */       throw new ParseException(lookup("main.err.unsupportedopt", str));
/*     */     }
/*     */ 
/* 217 */     URL localURL = parseURL(str);
/* 218 */     if (localURL != null) {
/* 219 */       urlList.addElement(localURL);
/* 220 */       return 1;
/*     */     }
/*     */ 
/* 223 */     return 0;
/*     */   }
/*     */ 
/*     */   private URL parseURL(String paramString)
/*     */     throws Main.ParseException
/*     */   {
/* 238 */     URL localURL = null;
/*     */ 
/* 240 */     String str1 = "file:";
/*     */     try
/*     */     {
/* 243 */       if (paramString.indexOf(':') <= 1)
/*     */       {
/* 246 */         localURL = ParseUtil.fileToEncodedURL(new File(paramString));
/* 247 */       } else if ((paramString.startsWith(str1)) && (paramString.length() != str1.length()) && (!new File(paramString.substring(str1.length())).isAbsolute()))
/*     */       {
/* 254 */         String str2 = ParseUtil.fileToEncodedURL(new File(System.getProperty("user.dir"))).getPath() + paramString.substring(str1.length());
/*     */ 
/* 256 */         localURL = new URL("file", "", str2);
/*     */       }
/*     */       else {
/* 259 */         localURL = new URL(paramString);
/*     */       }
/*     */     } catch (MalformedURLException localMalformedURLException) {
/* 262 */       throw new ParseException(lookup("main.err.badurl", paramString, localMalformedURLException.getMessage()));
/*     */     }
/*     */ 
/* 266 */     return localURL;
/*     */   }
/*     */ 
/*     */   private int invokeDebugger(String[] paramArrayOfString)
/*     */   {
/* 278 */     String[] arrayOfString = new String[paramArrayOfString.length + 1];
/* 279 */     int i = 0;
/*     */ 
/* 287 */     String str = System.getProperty("java.home") + File.separator + "phony";
/*     */ 
/* 289 */     arrayOfString[(i++)] = ("-Djava.class.path=" + str);
/*     */ 
/* 292 */     arrayOfString[(i++)] = "sun.applet.Main";
/*     */ 
/* 296 */     for (int j = 0; j < paramArrayOfString.length; j++) {
/* 297 */       if (!"-debug".equals(paramArrayOfString[j])) {
/* 298 */         arrayOfString[(i++)] = paramArrayOfString[j];
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 310 */       Class localClass = Class.forName("com.sun.tools.example.debug.tty.TTY", true, ClassLoader.getSystemClassLoader());
/*     */ 
/* 312 */       Method localMethod = localClass.getDeclaredMethod("main", new Class[] { [Ljava.lang.String.class });
/*     */ 
/* 314 */       localMethod.invoke(null, new Object[] { arrayOfString });
/*     */     } catch (ClassNotFoundException localClassNotFoundException) {
/* 316 */       System.err.println(lookup("main.debug.cantfinddebug"));
/* 317 */       return 1;
/*     */     } catch (NoSuchMethodException localNoSuchMethodException) {
/* 319 */       System.err.println(lookup("main.debug.cantfindmain"));
/* 320 */       return 1;
/*     */     } catch (InvocationTargetException localInvocationTargetException) {
/* 322 */       System.err.println(lookup("main.debug.exceptionindebug"));
/* 323 */       return 1;
/*     */     } catch (IllegalAccessException localIllegalAccessException) {
/* 325 */       System.err.println(lookup("main.debug.cantaccess"));
/* 326 */       return 1;
/*     */     }
/* 328 */     return 0;
/*     */   }
/*     */ 
/*     */   private void init()
/*     */   {
/* 333 */     Properties localProperties1 = getAVProps();
/*     */ 
/* 340 */     localProperties1.put("browser", "sun.applet.AppletViewer");
/* 341 */     localProperties1.put("browser.version", "1.06");
/* 342 */     localProperties1.put("browser.vendor", "Oracle Corporation");
/* 343 */     localProperties1.put("http.agent", "Java(tm) 2 SDK, Standard Edition v" + theVersion);
/*     */ 
/* 347 */     localProperties1.put("package.restrict.definition.java", "true");
/* 348 */     localProperties1.put("package.restrict.definition.sun", "true");
/*     */ 
/* 356 */     localProperties1.put("java.version.applet", "true");
/* 357 */     localProperties1.put("java.vendor.applet", "true");
/* 358 */     localProperties1.put("java.vendor.url.applet", "true");
/* 359 */     localProperties1.put("java.class.version.applet", "true");
/* 360 */     localProperties1.put("os.name.applet", "true");
/* 361 */     localProperties1.put("os.version.applet", "true");
/* 362 */     localProperties1.put("os.arch.applet", "true");
/* 363 */     localProperties1.put("file.separator.applet", "true");
/* 364 */     localProperties1.put("path.separator.applet", "true");
/* 365 */     localProperties1.put("line.separator.applet", "true");
/*     */ 
/* 369 */     Properties localProperties2 = System.getProperties();
/* 370 */     for (Enumeration localEnumeration = localProperties2.propertyNames(); localEnumeration.hasMoreElements(); ) {
/* 371 */       String str1 = (String)localEnumeration.nextElement();
/* 372 */       String str2 = localProperties2.getProperty(str1);
/*     */       String str3;
/* 374 */       if ((str3 = (String)localProperties1.setProperty(str1, str2)) != null) {
/* 375 */         System.err.println(lookup("main.warn.prop.overwrite", str1, str3, str2));
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 380 */     System.setProperties(localProperties1);
/*     */ 
/* 383 */     if (!this.noSecurityFlag)
/* 384 */       System.setSecurityManager(new AppletSecurity());
/*     */     else
/* 386 */       System.err.println(lookup("main.nosecmgr"));
/*     */   }
/*     */ 
/*     */   private Properties getAVProps()
/*     */   {
/* 403 */     Properties localProperties = new Properties();
/*     */ 
/* 405 */     File localFile1 = theUserPropertiesFile;
/* 406 */     if (localFile1.exists())
/*     */     {
/* 408 */       if (localFile1.canRead())
/*     */       {
/* 410 */         localProperties = getAVProps(localFile1);
/*     */       }
/*     */       else {
/* 413 */         System.err.println(lookup("main.warn.cantreadprops", localFile1.toString()));
/*     */ 
/* 415 */         localProperties = setDefaultAVProps();
/*     */       }
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/* 421 */       File localFile2 = new File(System.getProperty("user.home"));
/* 422 */       File localFile3 = new File(localFile2, ".hotjava");
/* 423 */       localFile3 = new File(localFile3, "properties");
/* 424 */       if (localFile3.exists())
/*     */       {
/* 426 */         localProperties = getAVProps(localFile3);
/*     */       }
/*     */       else {
/* 429 */         System.err.println(lookup("main.warn.cantreadprops", localFile3.toString()));
/*     */ 
/* 431 */         localProperties = setDefaultAVProps();
/*     */       }
/*     */ 
/*     */       try
/*     */       {
/* 436 */         FileOutputStream localFileOutputStream = new FileOutputStream(localFile1);
/* 437 */         localProperties.store(localFileOutputStream, lookup("main.prop.store"));
/* 438 */         localFileOutputStream.close();
/*     */       } catch (IOException localIOException) {
/* 440 */         System.err.println(lookup("main.err.prop.cantsave", localFile1.toString()));
/*     */       }
/*     */     }
/*     */ 
/* 444 */     return localProperties;
/*     */   }
/*     */ 
/*     */   private Properties setDefaultAVProps()
/*     */   {
/* 454 */     Properties localProperties = new Properties();
/* 455 */     for (int i = 0; i < avDefaultUserProps.length; i++) {
/* 456 */       localProperties.setProperty(avDefaultUserProps[i][0], avDefaultUserProps[i][1]);
/*     */     }
/*     */ 
/* 459 */     return localProperties;
/*     */   }
/*     */ 
/*     */   private Properties getAVProps(File paramFile)
/*     */   {
/* 471 */     Properties localProperties1 = new Properties();
/*     */ 
/* 474 */     Properties localProperties2 = new Properties();
/*     */     try {
/* 476 */       FileInputStream localFileInputStream = new FileInputStream(paramFile);
/* 477 */       localProperties2.load(new BufferedInputStream(localFileInputStream));
/* 478 */       localFileInputStream.close();
/*     */     } catch (IOException localIOException) {
/* 480 */       System.err.println(lookup("main.err.prop.cantread", paramFile.toString()));
/*     */     }
/*     */ 
/* 485 */     for (int i = 0; i < avDefaultUserProps.length; i++) {
/* 486 */       String str = localProperties2.getProperty(avDefaultUserProps[i][0]);
/* 487 */       if (str != null)
/*     */       {
/* 489 */         localProperties1.setProperty(avDefaultUserProps[i][0], str);
/*     */       }
/*     */       else {
/* 492 */         localProperties1.setProperty(avDefaultUserProps[i][0], avDefaultUserProps[i][1]);
/*     */       }
/*     */     }
/*     */ 
/* 496 */     return localProperties1;
/*     */   }
/*     */ 
/*     */   private static String lookup(String paramString)
/*     */   {
/* 504 */     return amh.getMessage(paramString);
/*     */   }
/*     */ 
/*     */   private static String lookup(String paramString1, String paramString2) {
/* 508 */     return amh.getMessage(paramString1, paramString2);
/*     */   }
/*     */ 
/*     */   private static String lookup(String paramString1, String paramString2, String paramString3) {
/* 512 */     return amh.getMessage(paramString1, paramString2, paramString3);
/*     */   }
/*     */ 
/*     */   private static String lookup(String paramString1, String paramString2, String paramString3, String paramString4)
/*     */   {
/* 517 */     return amh.getMessage(paramString1, paramString2, paramString3, paramString4);
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  65 */     File localFile = new File(System.getProperty("user.home"));
/*     */ 
/*  67 */     localFile.canWrite();
/*     */   }
/*     */ 
/*     */   class ParseException extends RuntimeException
/*     */   {
/* 531 */     Throwable t = null;
/*     */ 
/*     */     public ParseException(String arg2)
/*     */     {
/* 523 */       super();
/*     */     }
/*     */ 
/*     */     public ParseException(Throwable arg2) {
/* 527 */       super();
/* 528 */       this.t = localObject;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.applet.Main
 * JD-Core Version:    0.6.2
 */