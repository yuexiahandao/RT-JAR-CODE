/*      */ package java.lang;
/*      */ 
/*      */ import java.io.BufferedInputStream;
/*      */ import java.io.BufferedOutputStream;
/*      */ import java.io.Console;
/*      */ import java.io.FileDescriptor;
/*      */ import java.io.FileInputStream;
/*      */ import java.io.FileOutputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.PrintStream;
/*      */ import java.nio.channels.Channel;
/*      */ import java.nio.channels.spi.SelectorProvider;
/*      */ import java.security.AccessControlContext;
/*      */ import java.security.AccessController;
/*      */ import java.security.PrivilegedAction;
/*      */ import java.security.ProtectionDomain;
/*      */ import java.util.Map;
/*      */ import java.util.Properties;
/*      */ import java.util.PropertyPermission;
/*      */ import sun.misc.JavaIOAccess;
/*      */ import sun.misc.JavaLangAccess;
/*      */ import sun.misc.SharedSecrets;
/*      */ import sun.misc.VM;
/*      */ import sun.misc.Version;
/*      */ import sun.nio.ch.Interruptible;
/*      */ import sun.reflect.CallerSensitive;
/*      */ import sun.reflect.ConstantPool;
/*      */ import sun.reflect.Reflection;
/*      */ import sun.reflect.annotation.AnnotationType;
/*      */ import sun.security.util.SecurityConstants;
/*      */ 
/*      */ public final class System
/*      */ {
/*   80 */   public static final InputStream in = null;
/*      */ 
/*  107 */   public static final PrintStream out = null;
/*      */ 
/*  121 */   public static final PrintStream err = null;
/*      */ 
/*  125 */   private static volatile SecurityManager security = null;
/*      */ 
/*  200 */   private static volatile Console cons = null;
/*      */   private static Properties props;
/*      */   private static String lineSeparator;
/*      */ 
/*      */   private static native void registerNatives();
/*      */ 
/*      */   public static void setIn(InputStream paramInputStream)
/*      */   {
/*  148 */     checkIO();
/*  149 */     setIn0(paramInputStream);
/*      */   }
/*      */ 
/*      */   public static void setOut(PrintStream paramPrintStream)
/*      */   {
/*  172 */     checkIO();
/*  173 */     setOut0(paramPrintStream);
/*      */   }
/*      */ 
/*      */   public static void setErr(PrintStream paramPrintStream)
/*      */   {
/*  196 */     checkIO();
/*  197 */     setErr0(paramPrintStream);
/*      */   }
/*      */ 
/*      */   public static Console console()
/*      */   {
/*  210 */     if (cons == null) {
/*  211 */       synchronized (System.class) {
/*  212 */         cons = SharedSecrets.getJavaIOAccess().console();
/*      */       }
/*      */     }
/*  215 */     return cons;
/*      */   }
/*      */ 
/*      */   public static Channel inheritedChannel()
/*      */     throws IOException
/*      */   {
/*  244 */     return SelectorProvider.provider().inheritedChannel();
/*      */   }
/*      */ 
/*      */   private static void checkIO() {
/*  248 */     SecurityManager localSecurityManager = getSecurityManager();
/*  249 */     if (localSecurityManager != null)
/*  250 */       localSecurityManager.checkPermission(new RuntimePermission("setIO"));
/*      */   }
/*      */ 
/*      */   private static native void setIn0(InputStream paramInputStream);
/*      */ 
/*      */   private static native void setOut0(PrintStream paramPrintStream);
/*      */ 
/*      */   private static native void setErr0(PrintStream paramPrintStream);
/*      */ 
/*      */   public static void setSecurityManager(SecurityManager paramSecurityManager)
/*      */   {
/*      */     try
/*      */     {
/*  284 */       paramSecurityManager.checkPackageAccess("java.lang");
/*      */     }
/*      */     catch (Exception localException) {
/*      */     }
/*  288 */     setSecurityManager0(paramSecurityManager);
/*      */   }
/*      */ 
/*      */   private static synchronized void setSecurityManager0(SecurityManager paramSecurityManager)
/*      */   {
/*  293 */     SecurityManager localSecurityManager = getSecurityManager();
/*  294 */     if (localSecurityManager != null)
/*      */     {
/*  297 */       localSecurityManager.checkPermission(new RuntimePermission("setSecurityManager"));
/*      */     }
/*      */ 
/*  301 */     if ((paramSecurityManager != null) && (paramSecurityManager.getClass().getClassLoader() != null))
/*      */     {
/*  310 */       AccessController.doPrivileged(new PrivilegedAction() {
/*      */         public Object run() {
/*  312 */           this.val$s.getClass().getProtectionDomain().implies(SecurityConstants.ALL_PERMISSION);
/*      */ 
/*  314 */           return null;
/*      */         }
/*      */       });
/*      */     }
/*      */ 
/*  319 */     security = paramSecurityManager;
/*      */   }
/*      */ 
/*      */   public static SecurityManager getSecurityManager()
/*      */   {
/*  331 */     return security;
/*      */   }
/*      */ 
/*      */   public static native long currentTimeMillis();
/*      */ 
/*      */   public static native long nanoTime();
/*      */ 
/*      */   public static native void arraycopy(Object paramObject1, int paramInt1, Object paramObject2, int paramInt2, int paramInt3);
/*      */ 
/*      */   public static native int identityHashCode(Object paramObject);
/*      */ 
/*      */   private static native Properties initProperties(Properties paramProperties);
/*      */ 
/*      */   public static Properties getProperties()
/*      */   {
/*  622 */     SecurityManager localSecurityManager = getSecurityManager();
/*  623 */     if (localSecurityManager != null) {
/*  624 */       localSecurityManager.checkPropertiesAccess();
/*      */     }
/*      */ 
/*  627 */     return props;
/*      */   }
/*      */ 
/*      */   public static String lineSeparator()
/*      */   {
/*  639 */     return lineSeparator;
/*      */   }
/*      */ 
/*      */   public static void setProperties(Properties paramProperties)
/*      */   {
/*  667 */     SecurityManager localSecurityManager = getSecurityManager();
/*  668 */     if (localSecurityManager != null) {
/*  669 */       localSecurityManager.checkPropertiesAccess();
/*      */     }
/*  671 */     if (paramProperties == null) {
/*  672 */       paramProperties = new Properties();
/*  673 */       initProperties(paramProperties);
/*      */     }
/*  675 */     props = paramProperties;
/*      */   }
/*      */ 
/*      */   public static String getProperty(String paramString)
/*      */   {
/*  705 */     checkKey(paramString);
/*  706 */     SecurityManager localSecurityManager = getSecurityManager();
/*  707 */     if (localSecurityManager != null) {
/*  708 */       localSecurityManager.checkPropertyAccess(paramString);
/*      */     }
/*      */ 
/*  711 */     return props.getProperty(paramString);
/*      */   }
/*      */ 
/*      */   public static String getProperty(String paramString1, String paramString2)
/*      */   {
/*  741 */     checkKey(paramString1);
/*  742 */     SecurityManager localSecurityManager = getSecurityManager();
/*  743 */     if (localSecurityManager != null) {
/*  744 */       localSecurityManager.checkPropertyAccess(paramString1);
/*      */     }
/*      */ 
/*  747 */     return props.getProperty(paramString1, paramString2);
/*      */   }
/*      */ 
/*      */   public static String setProperty(String paramString1, String paramString2)
/*      */   {
/*  780 */     checkKey(paramString1);
/*  781 */     SecurityManager localSecurityManager = getSecurityManager();
/*  782 */     if (localSecurityManager != null) {
/*  783 */       localSecurityManager.checkPermission(new PropertyPermission(paramString1, "write"));
/*      */     }
/*      */ 
/*  787 */     return (String)props.setProperty(paramString1, paramString2);
/*      */   }
/*      */ 
/*      */   public static String clearProperty(String paramString)
/*      */   {
/*  818 */     checkKey(paramString);
/*  819 */     SecurityManager localSecurityManager = getSecurityManager();
/*  820 */     if (localSecurityManager != null) {
/*  821 */       localSecurityManager.checkPermission(new PropertyPermission(paramString, "write"));
/*      */     }
/*      */ 
/*  824 */     return (String)props.remove(paramString);
/*      */   }
/*      */ 
/*      */   private static void checkKey(String paramString) {
/*  828 */     if (paramString == null) {
/*  829 */       throw new NullPointerException("key can't be null");
/*      */     }
/*  831 */     if (paramString.equals(""))
/*  832 */       throw new IllegalArgumentException("key can't be empty");
/*      */   }
/*      */ 
/*      */   public static String getenv(String paramString)
/*      */   {
/*  883 */     SecurityManager localSecurityManager = getSecurityManager();
/*  884 */     if (localSecurityManager != null) {
/*  885 */       localSecurityManager.checkPermission(new RuntimePermission("getenv." + paramString));
/*      */     }
/*      */ 
/*  888 */     return ProcessEnvironment.getenv(paramString);
/*      */   }
/*      */ 
/*      */   public static Map<String, String> getenv()
/*      */   {
/*  933 */     SecurityManager localSecurityManager = getSecurityManager();
/*  934 */     if (localSecurityManager != null) {
/*  935 */       localSecurityManager.checkPermission(new RuntimePermission("getenv.*"));
/*      */     }
/*      */ 
/*  938 */     return ProcessEnvironment.getenv();
/*      */   }
/*      */ 
/*      */   public static void exit(int paramInt)
/*      */   {
/*  962 */     Runtime.getRuntime().exit(paramInt);
/*      */   }
/*      */ 
/*      */   public static void gc()
/*      */   {
/*  984 */     Runtime.getRuntime().gc();
/*      */   }
/*      */ 
/*      */   public static void runFinalization()
/*      */   {
/* 1006 */     Runtime.getRuntime().runFinalization();
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   public static void runFinalizersOnExit(boolean paramBoolean)
/*      */   {
/* 1036 */     Runtime.getRuntime(); Runtime.runFinalizersOnExit(paramBoolean);
/*      */   }
/*      */ 
/*      */   @CallerSensitive
/*      */   public static void load(String paramString)
/*      */   {
/* 1062 */     Runtime.getRuntime().load0(Reflection.getCallerClass(), paramString);
/*      */   }
/*      */ 
/*      */   @CallerSensitive
/*      */   public static void loadLibrary(String paramString)
/*      */   {
/* 1088 */     Runtime.getRuntime().loadLibrary0(Reflection.getCallerClass(), paramString);
/*      */   }
/*      */ 
/*      */   public static native String mapLibraryName(String paramString);
/*      */ 
/*      */   private static void initializeSystemClass()
/*      */   {
/* 1118 */     props = new Properties();
/* 1119 */     initProperties(props);
/*      */ 
/* 1135 */     VM.saveAndRemoveProperties(props);
/*      */ 
/* 1138 */     lineSeparator = props.getProperty("line.separator");
/* 1139 */     Version.init();
/*      */ 
/* 1141 */     FileInputStream localFileInputStream = new FileInputStream(FileDescriptor.in);
/* 1142 */     FileOutputStream localFileOutputStream1 = new FileOutputStream(FileDescriptor.out);
/* 1143 */     FileOutputStream localFileOutputStream2 = new FileOutputStream(FileDescriptor.err);
/* 1144 */     setIn0(new BufferedInputStream(localFileInputStream));
/* 1145 */     setOut0(new PrintStream(new BufferedOutputStream(localFileOutputStream1, 128), true));
/* 1146 */     setErr0(new PrintStream(new BufferedOutputStream(localFileOutputStream2, 128), true));
/*      */ 
/* 1149 */     loadLibrary("zip");
/*      */ 
/* 1152 */     Terminator.setup();
/*      */ 
/* 1158 */     VM.initializeOSEnvironment();
/*      */ 
/* 1162 */     Thread localThread = Thread.currentThread();
/* 1163 */     localThread.getThreadGroup().add(localThread);
/*      */ 
/* 1166 */     setJavaLangAccess();
/*      */ 
/* 1172 */     VM.booted();
/*      */   }
/*      */ 
/*      */   private static void setJavaLangAccess()
/*      */   {
/* 1177 */     SharedSecrets.setJavaLangAccess(new JavaLangAccess() {
/*      */       public ConstantPool getConstantPool(Class paramAnonymousClass) {
/* 1179 */         return paramAnonymousClass.getConstantPool();
/*      */       }
/*      */       public boolean casAnnotationType(Class<?> paramAnonymousClass, AnnotationType paramAnonymousAnnotationType1, AnnotationType paramAnonymousAnnotationType2) {
/* 1182 */         return paramAnonymousClass.casAnnotationType(paramAnonymousAnnotationType1, paramAnonymousAnnotationType2);
/*      */       }
/*      */       public AnnotationType getAnnotationType(Class paramAnonymousClass) {
/* 1185 */         return paramAnonymousClass.getAnnotationType();
/*      */       }
/*      */       public byte[] getRawClassAnnotations(Class<?> paramAnonymousClass) {
/* 1188 */         return paramAnonymousClass.getRawAnnotations();
/*      */       }
/*      */ 
/*      */       public <E extends Enum<E>> E[] getEnumConstantsShared(Class<E> paramAnonymousClass) {
/* 1192 */         return (Enum[])paramAnonymousClass.getEnumConstantsShared();
/*      */       }
/*      */       public void blockedOn(Thread paramAnonymousThread, Interruptible paramAnonymousInterruptible) {
/* 1195 */         paramAnonymousThread.blockedOn(paramAnonymousInterruptible);
/*      */       }
/*      */       public void registerShutdownHook(int paramAnonymousInt, boolean paramAnonymousBoolean, Runnable paramAnonymousRunnable) {
/* 1198 */         Shutdown.add(paramAnonymousInt, paramAnonymousBoolean, paramAnonymousRunnable);
/*      */       }
/*      */       public int getStackTraceDepth(Throwable paramAnonymousThrowable) {
/* 1201 */         return paramAnonymousThrowable.getStackTraceDepth();
/*      */       }
/*      */       public StackTraceElement getStackTraceElement(Throwable paramAnonymousThrowable, int paramAnonymousInt) {
/* 1204 */         return paramAnonymousThrowable.getStackTraceElement(paramAnonymousInt);
/*      */       }
/*      */       public int getStringHash32(String paramAnonymousString) {
/* 1207 */         return paramAnonymousString.hash32();
/*      */       }
/*      */       public Thread newThreadWithAcc(Runnable paramAnonymousRunnable, AccessControlContext paramAnonymousAccessControlContext) {
/* 1210 */         return new Thread(paramAnonymousRunnable, paramAnonymousAccessControlContext);
/*      */       }
/*      */       public void invokeFinalize(Object paramAnonymousObject) throws Throwable {
/* 1213 */         paramAnonymousObject.finalize();
/*      */       }
/*      */     });
/*      */   }
/*      */ 
/*      */   static
/*      */   {
/*   67 */     registerNatives();
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.lang.System
 * JD-Core Version:    0.6.2
 */