/*     */ package sun.launcher;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.math.BigDecimal;
/*     */ import java.math.RoundingMode;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.file.DirectoryStream;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.text.MessageFormat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Locale.Category;
/*     */ import java.util.Properties;
/*     */ import java.util.ResourceBundle;
/*     */ import java.util.Set;
/*     */ import java.util.TreeSet;
/*     */ import java.util.jar.Attributes;
/*     */ import java.util.jar.JarFile;
/*     */ import java.util.jar.Manifest;
/*     */ import sun.misc.VM;
/*     */ 
/*     */ public enum LauncherHelper
/*     */ {
/*  70 */   INSTANCE;
/*     */ 
/*     */   private static final String MAIN_CLASS = "Main-Class";
/*  73 */   private static StringBuilder outBuf = new StringBuilder();
/*     */   private static final String INDENT = "    ";
/*     */   private static final String VM_SETTINGS = "VM settings:";
/*     */   private static final String PROP_SETTINGS = "Property settings:";
/*     */   private static final String LOCALE_SETTINGS = "Locale settings:";
/*     */   private static final String diagprop = "sun.java.launcher.diag";
/*  82 */   static final boolean trace = VM.getSavedProperty("sun.java.launcher.diag") != null;
/*     */   private static final String defaultBundleName = "sun.launcher.resources.launcher";
/*     */   private static final int LM_UNKNOWN = 0;
/*     */   private static final int LM_CLASS = 1;
/*     */   private static final int LM_JAR = 2;
/*     */   private static final String encprop = "sun.jnu.encoding";
/* 514 */   private static String encoding = null;
/* 515 */   private static boolean isCharsetSupported = false;
/*     */ 
/*     */   static void showSettings(boolean paramBoolean1, String paramString, long paramLong1, long paramLong2, long paramLong3, boolean paramBoolean2)
/*     */   {
/* 117 */     PrintStream localPrintStream = paramBoolean1 ? System.err : System.out;
/* 118 */     String[] arrayOfString = paramString.split(":");
/* 119 */     String str1 = (arrayOfString.length > 1) && (arrayOfString[1] != null) ? arrayOfString[1].trim() : "all";
/*     */ 
/* 122 */     switch (str1) {
/*     */     case "vm":
/* 124 */       printVmSettings(localPrintStream, paramLong1, paramLong2, paramLong3, paramBoolean2);
/*     */ 
/* 126 */       break;
/*     */     case "properties":
/* 128 */       printProperties(localPrintStream);
/* 129 */       break;
/*     */     case "locale":
/* 131 */       printLocale(localPrintStream);
/* 132 */       break;
/*     */     default:
/* 134 */       printVmSettings(localPrintStream, paramLong1, paramLong2, paramLong3, paramBoolean2);
/*     */ 
/* 136 */       printProperties(localPrintStream);
/* 137 */       printLocale(localPrintStream);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static void printVmSettings(PrintStream paramPrintStream, long paramLong1, long paramLong2, long paramLong3, boolean paramBoolean)
/*     */   {
/* 149 */     paramPrintStream.println("VM settings:");
/* 150 */     if (paramLong3 != 0L) {
/* 151 */       paramPrintStream.println("    Stack Size: " + SizePrefix.scaleValue(paramLong3));
/*     */     }
/*     */ 
/* 154 */     if (paramLong1 != 0L) {
/* 155 */       paramPrintStream.println("    Min. Heap Size: " + SizePrefix.scaleValue(paramLong1));
/*     */     }
/*     */ 
/* 158 */     if (paramLong2 != 0L) {
/* 159 */       paramPrintStream.println("    Max. Heap Size: " + SizePrefix.scaleValue(paramLong2));
/*     */     }
/*     */     else {
/* 162 */       paramPrintStream.println("    Max. Heap Size (Estimated): " + SizePrefix.scaleValue(Runtime.getRuntime().maxMemory()));
/*     */     }
/*     */ 
/* 165 */     paramPrintStream.println("    Ergonomics Machine Class: " + (paramBoolean ? "server" : "client"));
/*     */ 
/* 167 */     paramPrintStream.println("    Using VM: " + System.getProperty("java.vm.name"));
/*     */ 
/* 169 */     paramPrintStream.println();
/*     */   }
/*     */ 
/*     */   private static void printProperties(PrintStream paramPrintStream)
/*     */   {
/* 176 */     Properties localProperties = System.getProperties();
/* 177 */     paramPrintStream.println("Property settings:");
/* 178 */     ArrayList localArrayList = new ArrayList();
/* 179 */     localArrayList.addAll(localProperties.stringPropertyNames());
/* 180 */     Collections.sort(localArrayList);
/* 181 */     for (String str : localArrayList) {
/* 182 */       printPropertyValue(paramPrintStream, str, localProperties.getProperty(str));
/*     */     }
/* 184 */     paramPrintStream.println();
/*     */   }
/*     */ 
/*     */   private static boolean isPath(String paramString) {
/* 188 */     return (paramString.endsWith(".dirs")) || (paramString.endsWith(".path"));
/*     */   }
/*     */ 
/*     */   private static void printPropertyValue(PrintStream paramPrintStream, String paramString1, String paramString2)
/*     */   {
/* 193 */     paramPrintStream.print("    " + paramString1 + " = ");
/*     */     int k;
/* 194 */     if (paramString1.equals("line.separator")) {
/* 195 */       for (k : paramString2.getBytes()) {
/* 196 */         switch (k) {
/*     */         case 13:
/* 198 */           paramPrintStream.print("\\r ");
/* 199 */           break;
/*     */         case 10:
/* 201 */           paramPrintStream.print("\\n ");
/* 202 */           break;
/*     */         default:
/* 206 */           paramPrintStream.printf("0x%02X", new Object[] { Integer.valueOf(k & 0xFF) });
/*     */         }
/*     */       }
/*     */ 
/* 210 */       paramPrintStream.println();
/* 211 */       return;
/*     */     }
/* 213 */     if (!isPath(paramString1)) {
/* 214 */       paramPrintStream.println(paramString2);
/* 215 */       return;
/*     */     }
/* 217 */     ??? = paramString2.split(System.getProperty("path.separator"));
/* 218 */     ??? = 1;
/* 219 */     for (String str : ???)
/* 220 */       if (??? != 0) {
/* 221 */         paramPrintStream.println(str);
/* 222 */         ??? = 0;
/*     */       } else {
/* 224 */         paramPrintStream.println("        " + str);
/*     */       }
/*     */   }
/*     */ 
/*     */   private static void printLocale(PrintStream paramPrintStream)
/*     */   {
/* 233 */     Locale localLocale = Locale.getDefault();
/* 234 */     paramPrintStream.println("Locale settings:");
/* 235 */     paramPrintStream.println("    default locale = " + localLocale.getDisplayLanguage());
/*     */ 
/* 237 */     paramPrintStream.println("    default display locale = " + Locale.getDefault(Locale.Category.DISPLAY).getDisplayName());
/*     */ 
/* 239 */     paramPrintStream.println("    default format locale = " + Locale.getDefault(Locale.Category.FORMAT).getDisplayName());
/*     */ 
/* 241 */     printLocales(paramPrintStream);
/* 242 */     paramPrintStream.println();
/*     */   }
/*     */ 
/*     */   private static void printLocales(PrintStream paramPrintStream) {
/* 246 */     Locale[] arrayOfLocale = Locale.getAvailableLocales();
/* 247 */     int i = arrayOfLocale == null ? 0 : arrayOfLocale.length;
/* 248 */     if (i < 1) {
/* 249 */       return;
/*     */     }
/*     */ 
/* 253 */     TreeSet localTreeSet = new TreeSet();
/*     */     String str;
/* 254 */     for (str : arrayOfLocale) {
/* 255 */       localTreeSet.add(str.toString());
/*     */     }
/*     */ 
/* 258 */     paramPrintStream.print("    available locales = ");
/* 259 */     ??? = localTreeSet.iterator();
/* 260 */     ??? = i - 1;
/* 261 */     for (??? = 0; ((Iterator)???).hasNext(); ???++) {
/* 262 */       str = (String)((Iterator)???).next();
/* 263 */       paramPrintStream.print(str);
/* 264 */       if (??? != ???) {
/* 265 */         paramPrintStream.print(", ");
/*     */       }
/*     */ 
/* 268 */       if ((??? + 1) % 8 == 0) {
/* 269 */         paramPrintStream.println();
/* 270 */         paramPrintStream.print("        ");
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private static String getLocalizedMessage(String paramString, Object[] paramArrayOfObject)
/*     */   {
/* 316 */     String str = ResourceBundleHolder.RB.getString(paramString);
/* 317 */     return paramArrayOfObject != null ? MessageFormat.format(str, paramArrayOfObject) : str;
/*     */   }
/*     */ 
/*     */   static void initHelpMessage(String paramString)
/*     */   {
/* 328 */     outBuf = outBuf.append(getLocalizedMessage("java.launcher.opt.header", new Object[] { paramString == null ? "java" : paramString }));
/*     */ 
/* 330 */     outBuf = outBuf.append(getLocalizedMessage("java.launcher.opt.datamodel", new Object[] { Integer.valueOf(32) }));
/*     */ 
/* 332 */     outBuf = outBuf.append(getLocalizedMessage("java.launcher.opt.datamodel", new Object[] { Integer.valueOf(64) }));
/*     */   }
/*     */ 
/*     */   static void appendVmSelectMessage(String paramString1, String paramString2)
/*     */   {
/* 341 */     outBuf = outBuf.append(getLocalizedMessage("java.launcher.opt.vmselect", new Object[] { paramString1, paramString2 }));
/*     */   }
/*     */ 
/*     */   static void appendVmSynonymMessage(String paramString1, String paramString2)
/*     */   {
/* 350 */     outBuf = outBuf.append(getLocalizedMessage("java.launcher.opt.hotspot", new Object[] { paramString1, paramString2 }));
/*     */   }
/*     */ 
/*     */   static void appendVmErgoMessage(boolean paramBoolean, String paramString)
/*     */   {
/* 359 */     outBuf = outBuf.append(getLocalizedMessage("java.launcher.ergo.message1", new Object[] { paramString }));
/*     */ 
/* 361 */     outBuf = paramBoolean ? outBuf.append(",\n" + getLocalizedMessage("java.launcher.ergo.message2", new Object[0]) + "\n\n") : outBuf.append(".\n\n");
/*     */   }
/*     */ 
/*     */   static void printHelpMessage(boolean paramBoolean)
/*     */   {
/* 373 */     PrintStream localPrintStream = paramBoolean ? System.err : System.out;
/* 374 */     outBuf = outBuf.append(getLocalizedMessage("java.launcher.opt.footer", new Object[] { File.pathSeparator }));
/*     */ 
/* 376 */     localPrintStream.println(outBuf.toString());
/*     */   }
/*     */ 
/*     */   static void printXUsageMessage(boolean paramBoolean)
/*     */   {
/* 383 */     PrintStream localPrintStream = paramBoolean ? System.err : System.out;
/* 384 */     localPrintStream.println(getLocalizedMessage("java.launcher.X.usage", new Object[] { File.pathSeparator }));
/*     */ 
/* 386 */     if (System.getProperty("os.name").contains("OS X"))
/* 387 */       localPrintStream.println(getLocalizedMessage("java.launcher.X.macosx.usage", new Object[] { File.pathSeparator }));
/*     */   }
/*     */ 
/*     */   static String getMainClassFromJar(PrintStream paramPrintStream, String paramString)
/*     */   {
/*     */     try
/*     */     {
/* 394 */       JarFile localJarFile = null;
/*     */       try {
/* 396 */         localJarFile = new JarFile(paramString);
/* 397 */         Manifest localManifest = localJarFile.getManifest();
/* 398 */         if (localManifest == null) {
/* 399 */           abort(paramPrintStream, null, "java.launcher.jar.error2", new Object[] { paramString });
/*     */         }
/* 401 */         Attributes localAttributes = localManifest.getMainAttributes();
/* 402 */         if (localAttributes == null) {
/* 403 */           abort(paramPrintStream, null, "java.launcher.jar.error3", new Object[] { paramString });
/*     */         }
/* 405 */         String str1 = localAttributes.getValue("Main-Class");
/* 406 */         if (str1 == null) {
/* 407 */           abort(paramPrintStream, null, "java.launcher.jar.error3", new Object[] { paramString });
/*     */         }
/* 409 */         return str1.trim();
/*     */       } finally {
/* 411 */         if (localJarFile != null)
/* 412 */           localJarFile.close();
/*     */       }
/*     */     }
/*     */     catch (IOException localIOException) {
/* 416 */       abort(paramPrintStream, localIOException, "java.launcher.jar.error1", new Object[] { paramString });
/*     */     }
/* 418 */     return null;
/*     */   }
/*     */ 
/*     */   static void abort(PrintStream paramPrintStream, Throwable paramThrowable, String paramString, Object[] paramArrayOfObject)
/*     */   {
/* 430 */     if (paramString != null) {
/* 431 */       paramPrintStream.println(getLocalizedMessage(paramString, paramArrayOfObject));
/*     */     }
/* 433 */     if (trace) {
/* 434 */       if (paramThrowable != null)
/* 435 */         paramThrowable.printStackTrace();
/*     */       else {
/* 437 */         Thread.dumpStack();
/*     */       }
/*     */     }
/* 440 */     System.exit(1);
/*     */   }
/*     */ 
/*     */   public static Class<?> checkAndLoadMain(boolean paramBoolean, int paramInt, String paramString)
/*     */   {
/* 464 */     PrintStream localPrintStream = paramBoolean ? System.err : System.out;
/* 465 */     ClassLoader localClassLoader = ClassLoader.getSystemClassLoader();
/*     */ 
/* 467 */     String str = null;
/* 468 */     switch (paramInt) {
/*     */     case 1:
/* 470 */       str = paramString;
/* 471 */       break;
/*     */     case 2:
/* 473 */       str = getMainClassFromJar(localPrintStream, paramString);
/* 474 */       break;
/*     */     default:
/* 477 */       throw new InternalError("" + paramInt + ": Unknown launch mode");
/*     */     }
/* 479 */     str = str.replace('/', '.');
/* 480 */     Class localClass = null;
/*     */     try {
/* 482 */       localClass = localClassLoader.loadClass(str);
/*     */     } catch (ClassNotFoundException localClassNotFoundException) {
/* 484 */       abort(localPrintStream, localClassNotFoundException, "java.launcher.cls.error1", new Object[] { str });
/*     */     }
/* 486 */     getMainMethod(localPrintStream, localClass);
/* 487 */     return localClass;
/*     */   }
/*     */ 
/*     */   static Method getMainMethod(PrintStream paramPrintStream, Class<?> paramClass) {
/* 491 */     String str = paramClass.getName();
/* 492 */     Method localMethod = null;
/*     */     try {
/* 494 */       localMethod = paramClass.getMethod("main", new Class[] { [Ljava.lang.String.class });
/*     */     } catch (NoSuchMethodException localNoSuchMethodException) {
/* 496 */       abort(paramPrintStream, null, "java.launcher.cls.error4", new Object[] { str });
/*     */     }
/*     */ 
/* 503 */     int i = localMethod.getModifiers();
/* 504 */     if (!Modifier.isStatic(i)) {
/* 505 */       abort(paramPrintStream, null, "java.launcher.cls.error2", new Object[] { "static", str });
/*     */     }
/* 507 */     if (localMethod.getReturnType() != Void.TYPE) {
/* 508 */       abort(paramPrintStream, null, "java.launcher.cls.error3", new Object[] { str });
/*     */     }
/* 510 */     return localMethod;
/*     */   }
/*     */ 
/*     */   static String makePlatformString(boolean paramBoolean, byte[] paramArrayOfByte)
/*     */   {
/* 522 */     PrintStream localPrintStream = paramBoolean ? System.err : System.out;
/* 523 */     if (encoding == null) {
/* 524 */       encoding = System.getProperty("sun.jnu.encoding");
/* 525 */       isCharsetSupported = Charset.isSupported(encoding);
/*     */     }
/*     */     try {
/* 528 */       return isCharsetSupported ? new String(paramArrayOfByte, encoding) : new String(paramArrayOfByte);
/*     */     }
/*     */     catch (UnsupportedEncodingException localUnsupportedEncodingException)
/*     */     {
/* 533 */       abort(localPrintStream, localUnsupportedEncodingException, null, new Object[0]);
/*     */     }
/* 535 */     return null;
/*     */   }
/*     */ 
/*     */   static String[] expandArgs(String[] paramArrayOfString) {
/* 539 */     ArrayList localArrayList = new ArrayList();
/* 540 */     for (String str : paramArrayOfString) {
/* 541 */       localArrayList.add(new StdArg(str));
/*     */     }
/* 543 */     return expandArgs(localArrayList);
/*     */   }
/*     */ 
/*     */   static String[] expandArgs(List<StdArg> paramList) {
/* 547 */     ArrayList localArrayList = new ArrayList();
/* 548 */     if (trace) {
/* 549 */       System.err.println("Incoming arguments:");
/*     */     }
/* 551 */     for (Object localObject1 = paramList.iterator(); ((Iterator)localObject1).hasNext(); ) { localObject2 = (StdArg)((Iterator)localObject1).next();
/* 552 */       if (trace) {
/* 553 */         System.err.println(localObject2);
/*     */       }
/* 555 */       if (((StdArg)localObject2).needsExpansion) {
/* 556 */         File localFile1 = new File(((StdArg)localObject2).arg);
/* 557 */         File localFile2 = localFile1.getParentFile();
/* 558 */         str = localFile1.getName();
/* 559 */         if (localFile2 == null)
/* 560 */           localFile2 = new File(".");
/*     */         try {
/* 562 */           DirectoryStream localDirectoryStream = Files.newDirectoryStream(localFile2.toPath(), str); Object localObject3 = null;
/*     */           try {
/* 564 */             int k = 0;
/* 565 */             for (Path localPath : localDirectoryStream) {
/* 566 */               localArrayList.add(localPath.normalize().toString());
/* 567 */               k++;
/*     */             }
/* 569 */             if (k == 0)
/* 570 */               localArrayList.add(((StdArg)localObject2).arg);
/*     */           }
/*     */           catch (Throwable localThrowable2)
/*     */           {
/* 562 */             localObject3 = localThrowable2; throw localThrowable2;
/*     */           }
/*     */           finally
/*     */           {
/* 572 */             if (localDirectoryStream != null) if (localObject3 != null) try { localDirectoryStream.close(); } catch (Throwable localThrowable3) { localObject3.addSuppressed(localThrowable3); } else localDirectoryStream.close();  
/*     */           } } catch (Exception localException) { localArrayList.add(((StdArg)localObject2).arg);
/* 574 */           if (trace) {
/* 575 */             System.err.println("Warning: passing argument as-is " + localObject2);
/* 576 */             System.err.print(localException);
/*     */           } }
/*     */       }
/*     */       else {
/* 580 */         localArrayList.add(((StdArg)localObject2).arg);
/*     */       }
/*     */     }
/*     */     Object localObject2;
/*     */     String str;
/* 583 */     localObject1 = new String[localArrayList.size()];
/* 584 */     localArrayList.toArray((Object[])localObject1);
/*     */ 
/* 586 */     if (trace) {
/* 587 */       System.err.println("Expanded arguments:");
/* 588 */       for (str : localObject1) {
/* 589 */         System.err.println(str);
/*     */       }
/*     */     }
/* 592 */     return localObject1;
/*     */   }
/*     */ 
/*     */   private static class ResourceBundleHolder
/*     */   {
/*  87 */     private static final ResourceBundle RB = ResourceBundle.getBundle("sun.launcher.resources.launcher");
/*     */   }
/*     */ 
/*     */   private static enum SizePrefix
/*     */   {
/* 277 */     KILO(1024L, "K"), 
/* 278 */     MEGA(1048576L, "M"), 
/* 279 */     GIGA(1073741824L, "G"), 
/* 280 */     TERA(1099511627776L, "T");
/*     */ 
/*     */     long size;
/*     */     String abbrev;
/*     */ 
/* 285 */     private SizePrefix(long paramLong, String paramString) { this.size = paramLong;
/* 286 */       this.abbrev = paramString; }
/*     */ 
/*     */     private static String scale(long paramLong, SizePrefix paramSizePrefix)
/*     */     {
/* 290 */       return BigDecimal.valueOf(paramLong).divide(BigDecimal.valueOf(paramSizePrefix.size), 2, RoundingMode.HALF_EVEN).toPlainString() + paramSizePrefix.abbrev;
/*     */     }
/*     */ 
/*     */     static String scaleValue(long paramLong)
/*     */     {
/* 299 */       if (paramLong < MEGA.size)
/* 300 */         return scale(paramLong, KILO);
/* 301 */       if (paramLong < GIGA.size)
/* 302 */         return scale(paramLong, MEGA);
/* 303 */       if (paramLong < TERA.size) {
/* 304 */         return scale(paramLong, GIGA);
/*     */       }
/* 306 */       return scale(paramLong, TERA);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class StdArg
/*     */   {
/*     */     final String arg;
/*     */     final boolean needsExpansion;
/*     */ 
/*     */     StdArg(String paramString, boolean paramBoolean)
/*     */     {
/* 600 */       this.arg = paramString;
/* 601 */       this.needsExpansion = paramBoolean;
/*     */     }
/*     */ 
/*     */     StdArg(String paramString)
/*     */     {
/* 607 */       this.arg = paramString.substring(1);
/* 608 */       this.needsExpansion = (paramString.charAt(0) == 'T');
/*     */     }
/*     */     public String toString() {
/* 611 */       return "StdArg{arg=" + this.arg + ", needsExpansion=" + this.needsExpansion + '}';
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.launcher.LauncherHelper
 * JD-Core Version:    0.6.2
 */