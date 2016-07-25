/*     */ package sun.misc;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ 
/*     */ public class Version
/*     */ {
/*     */   private static final String launcher_name = "java";
/*     */   private static final String java_version = "1.7.0_79";
/*     */   private static final String java_runtime_name = "Java(TM) SE Runtime Environment";
/*     */   private static final String java_runtime_version = "1.7.0_79-b15";
/*  54 */   private static boolean versionsInitialized = false;
/*  55 */   private static int jvm_major_version = 0;
/*  56 */   private static int jvm_minor_version = 0;
/*  57 */   private static int jvm_micro_version = 0;
/*  58 */   private static int jvm_update_version = 0;
/*  59 */   private static int jvm_build_number = 0;
/*  60 */   private static String jvm_special_version = null;
/*  61 */   private static int jdk_major_version = 0;
/*  62 */   private static int jdk_minor_version = 0;
/*  63 */   private static int jdk_micro_version = 0;
/*  64 */   private static int jdk_update_version = 0;
/*  65 */   private static int jdk_build_number = 0;
/*  66 */   private static String jdk_special_version = null;
/*     */   private static boolean jvmVersionInfoAvailable;
/*     */ 
/*     */   public static void init()
/*     */   {
/*  49 */     System.setProperty("java.version", "1.7.0_79");
/*  50 */     System.setProperty("java.runtime.version", "1.7.0_79-b15");
/*  51 */     System.setProperty("java.runtime.name", "Java(TM) SE Runtime Environment");
/*     */   }
/*     */ 
/*     */   public static void print()
/*     */   {
/*  74 */     print(System.err);
/*     */   }
/*     */ 
/*     */   public static void println()
/*     */   {
/*  82 */     print(System.err);
/*  83 */     System.err.println();
/*     */   }
/*     */ 
/*     */   public static void print(PrintStream paramPrintStream)
/*     */   {
/*  90 */     int i = 0;
/*     */ 
/*  93 */     String str1 = System.getProperty("java.awt.headless");
/*  94 */     if ((str1 != null) && (str1.equalsIgnoreCase("true"))) {
/*  95 */       i = 1;
/*     */     }
/*     */ 
/*  99 */     paramPrintStream.println("java version \"1.7.0_79\"");
/*     */ 
/* 103 */     paramPrintStream.print("Java(TM) SE Runtime Environment (build 1.7.0_79-b15");
/*     */ 
/* 105 */     if (("Java(TM) SE Runtime Environment".indexOf("Embedded") != -1) && (i != 0))
/*     */     {
/* 107 */       paramPrintStream.print(", headless");
/*     */     }
/* 109 */     paramPrintStream.println(')');
/*     */ 
/* 112 */     String str2 = System.getProperty("java.vm.name");
/* 113 */     String str3 = System.getProperty("java.vm.version");
/* 114 */     String str4 = System.getProperty("java.vm.info");
/* 115 */     paramPrintStream.println(str2 + " (build " + str3 + ", " + str4 + ")");
/*     */   }
/*     */ 
/*     */   public static synchronized int jvmMajorVersion()
/*     */   {
/* 128 */     if (!versionsInitialized) {
/* 129 */       initVersions();
/*     */     }
/* 131 */     return jvm_major_version;
/*     */   }
/*     */ 
/*     */   public static synchronized int jvmMinorVersion()
/*     */   {
/* 141 */     if (!versionsInitialized) {
/* 142 */       initVersions();
/*     */     }
/* 144 */     return jvm_minor_version;
/*     */   }
/*     */ 
/*     */   public static synchronized int jvmMicroVersion()
/*     */   {
/* 155 */     if (!versionsInitialized) {
/* 156 */       initVersions();
/*     */     }
/* 158 */     return jvm_micro_version;
/*     */   }
/*     */ 
/*     */   public static synchronized int jvmUpdateVersion()
/*     */   {
/* 167 */     if (!versionsInitialized) {
/* 168 */       initVersions();
/*     */     }
/* 170 */     return jvm_update_version;
/*     */   }
/*     */ 
/*     */   public static synchronized String jvmSpecialVersion() {
/* 174 */     if (!versionsInitialized) {
/* 175 */       initVersions();
/*     */     }
/* 177 */     if (jvm_special_version == null) {
/* 178 */       jvm_special_version = getJvmSpecialVersion();
/*     */     }
/* 180 */     return jvm_special_version;
/*     */   }
/*     */ 
/*     */   public static native String getJvmSpecialVersion();
/*     */ 
/*     */   public static synchronized int jvmBuildNumber()
/*     */   {
/* 190 */     if (!versionsInitialized) {
/* 191 */       initVersions();
/*     */     }
/* 193 */     return jvm_build_number;
/*     */   }
/*     */ 
/*     */   public static synchronized int jdkMajorVersion()
/*     */   {
/* 202 */     if (!versionsInitialized) {
/* 203 */       initVersions();
/*     */     }
/* 205 */     return jdk_major_version;
/*     */   }
/*     */ 
/*     */   public static synchronized int jdkMinorVersion()
/*     */   {
/* 213 */     if (!versionsInitialized) {
/* 214 */       initVersions();
/*     */     }
/* 216 */     return jdk_minor_version;
/*     */   }
/*     */ 
/*     */   public static synchronized int jdkMicroVersion()
/*     */   {
/* 224 */     if (!versionsInitialized) {
/* 225 */       initVersions();
/*     */     }
/* 227 */     return jdk_micro_version;
/*     */   }
/*     */ 
/*     */   public static synchronized int jdkUpdateVersion()
/*     */   {
/* 236 */     if (!versionsInitialized) {
/* 237 */       initVersions();
/*     */     }
/* 239 */     return jdk_update_version;
/*     */   }
/*     */ 
/*     */   public static synchronized String jdkSpecialVersion() {
/* 243 */     if (!versionsInitialized) {
/* 244 */       initVersions();
/*     */     }
/* 246 */     if (jdk_special_version == null) {
/* 247 */       jdk_special_version = getJdkSpecialVersion();
/*     */     }
/* 249 */     return jdk_special_version;
/*     */   }
/*     */ 
/*     */   public static native String getJdkSpecialVersion();
/*     */ 
/*     */   public static synchronized int jdkBuildNumber()
/*     */   {
/* 259 */     if (!versionsInitialized) {
/* 260 */       initVersions();
/*     */     }
/* 262 */     return jdk_build_number;
/*     */   }
/*     */ 
/*     */   private static synchronized void initVersions()
/*     */   {
/* 268 */     if (versionsInitialized) {
/* 269 */       return;
/*     */     }
/* 271 */     jvmVersionInfoAvailable = getJvmVersionInfo();
/* 272 */     if (!jvmVersionInfoAvailable)
/*     */     {
/* 277 */       Object localObject = System.getProperty("java.vm.version");
/* 278 */       if ((((CharSequence)localObject).length() >= 5) && (Character.isDigit(((CharSequence)localObject).charAt(0))) && (((CharSequence)localObject).charAt(1) == '.') && (Character.isDigit(((CharSequence)localObject).charAt(2))) && (((CharSequence)localObject).charAt(3) == '.') && (Character.isDigit(((CharSequence)localObject).charAt(4))))
/*     */       {
/* 282 */         jvm_major_version = Character.digit(((CharSequence)localObject).charAt(0), 10);
/* 283 */         jvm_minor_version = Character.digit(((CharSequence)localObject).charAt(2), 10);
/* 284 */         jvm_micro_version = Character.digit(((CharSequence)localObject).charAt(4), 10);
/* 285 */         localObject = ((CharSequence)localObject).subSequence(5, ((CharSequence)localObject).length());
/* 286 */         if ((((CharSequence)localObject).charAt(0) == '_') && (((CharSequence)localObject).length() >= 3) && (Character.isDigit(((CharSequence)localObject).charAt(1))) && (Character.isDigit(((CharSequence)localObject).charAt(2))))
/*     */         {
/* 289 */           int i = 3;
/*     */           try {
/* 291 */             String str1 = ((CharSequence)localObject).subSequence(1, 3).toString();
/* 292 */             jvm_update_version = Integer.valueOf(str1).intValue();
/* 293 */             if (((CharSequence)localObject).length() >= 4) {
/* 294 */               char c = ((CharSequence)localObject).charAt(3);
/* 295 */               if ((c >= 'a') && (c <= 'z')) {
/* 296 */                 jvm_special_version = Character.toString(c);
/* 297 */                 i++;
/*     */               }
/*     */             }
/*     */           }
/*     */           catch (NumberFormatException localNumberFormatException) {
/* 302 */             return;
/*     */           }
/* 304 */           localObject = ((CharSequence)localObject).subSequence(i, ((CharSequence)localObject).length());
/*     */         }
/* 306 */         if (((CharSequence)localObject).charAt(0) == '-')
/*     */         {
/* 310 */           localObject = ((CharSequence)localObject).subSequence(1, ((CharSequence)localObject).length());
/* 311 */           String[] arrayOfString1 = ((CharSequence)localObject).toString().split("-");
/* 312 */           for (String str2 : arrayOfString1) {
/* 313 */             if ((str2.charAt(0) == 'b') && (str2.length() == 3) && (Character.isDigit(str2.charAt(1))) && (Character.isDigit(str2.charAt(2))))
/*     */             {
/* 316 */               jvm_build_number = Integer.valueOf(str2.substring(1, 3)).intValue();
/*     */ 
/* 318 */               break;
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 324 */     getJdkVersionInfo();
/* 325 */     versionsInitialized = true;
/*     */   }
/*     */ 
/*     */   private static native boolean getJvmVersionInfo();
/*     */ 
/*     */   private static native void getJdkVersionInfo();
/*     */ 
/*     */   static
/*     */   {
/*  45 */     init();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.misc.Version
 * JD-Core Version:    0.6.2
 */