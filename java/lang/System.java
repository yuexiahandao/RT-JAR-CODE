
package java.lang;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Console;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.channels.Channel;
import java.nio.channels.spi.SelectorProvider;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.ProtectionDomain;
import java.util.Map;
import java.util.Properties;
import java.util.PropertyPermission;

import sun.misc.JavaIOAccess;
import sun.misc.JavaLangAccess;
import sun.misc.SharedSecrets;
import sun.misc.VM;
import sun.misc.Version;
import sun.nio.ch.Interruptible;
import sun.reflect.CallerSensitive;
import sun.reflect.ConstantPool;
import sun.reflect.Reflection;
import sun.reflect.annotation.AnnotationType;
import sun.security.util.SecurityConstants;


public final class System {
    public static final InputStream in = null;

    public static final PrintStream out = null;

    public static final PrintStream err = null;

    private static volatile SecurityManager security = null;

    private static volatile Console cons = null;
    private static Properties props;
    private static String lineSeparator;


    private static native void registerNatives();


    public static void setIn(InputStream paramInputStream) {

        checkIO();

        setIn0(paramInputStream);

    }


    public static void setOut(PrintStream paramPrintStream) {

        checkIO();

        setOut0(paramPrintStream);

    }


    public static void setErr(PrintStream paramPrintStream) {

        checkIO();

        setErr0(paramPrintStream);

    }


    public static Console console() {

        if (cons == null) {

            synchronized (System.class) {

                cons = SharedSecrets.getJavaIOAccess().console();

            }

        }

        return cons;

    }


    public static Channel inheritedChannel()
            throws IOException {

        return SelectorProvider.provider().inheritedChannel();

    }


    private static void checkIO() {

        SecurityManager localSecurityManager = getSecurityManager();

        if (localSecurityManager != null)
            localSecurityManager.checkPermission(new RuntimePermission("setIO"));

    }


    private static native void setIn0(InputStream paramInputStream);


    private static native void setOut0(PrintStream paramPrintStream);


    private static native void setErr0(PrintStream paramPrintStream);


    public static void setSecurityManager(SecurityManager paramSecurityManager) {

        try {

            paramSecurityManager.checkPackageAccess("java.lang");

        } catch (Exception localException) {

        }

        setSecurityManager0(paramSecurityManager);

    }


    private static synchronized void setSecurityManager0(SecurityManager paramSecurityManager) {

        SecurityManager localSecurityManager = getSecurityManager();

        if (localSecurityManager != null) {

            localSecurityManager.checkPermission(new RuntimePermission("setSecurityManager"));

        }


        if ((paramSecurityManager != null) && (paramSecurityManager.getClass().getClassLoader() != null)) {

            AccessController.doPrivileged(new PrivilegedAction() {

                public Object run() {

                    this.val$s.getClass().getProtectionDomain().implies(SecurityConstants.ALL_PERMISSION);


                    return null;

                }

            });

        }


        security = paramSecurityManager;

    }


    public static SecurityManager getSecurityManager() {

        return security;

    }


    public static native long currentTimeMillis();


    public static native long nanoTime();


    public static native void arraycopy(Object paramObject1, int paramInt1, Object paramObject2, int paramInt2, int paramInt3);


    public static native int identityHashCode(Object paramObject);


    private static native Properties initProperties(Properties paramProperties);


    public static Properties getProperties() {

        SecurityManager localSecurityManager = getSecurityManager();

        if (localSecurityManager != null) {

            localSecurityManager.checkPropertiesAccess();

        }


        return props;

    }


    public static String lineSeparator() {

        return lineSeparator;

    }


    public static void setProperties(Properties paramProperties) {

        SecurityManager localSecurityManager = getSecurityManager();

        if (localSecurityManager != null) {

            localSecurityManager.checkPropertiesAccess();

        }

        if (paramProperties == null) {

            paramProperties = new Properties();

            initProperties(paramProperties);

        }

        props = paramProperties;

    }


    public static String getProperty(String paramString) {

        checkKey(paramString);

        SecurityManager localSecurityManager = getSecurityManager();

        if (localSecurityManager != null) {

            localSecurityManager.checkPropertyAccess(paramString);

        }


        return props.getProperty(paramString);

    }


    public static String getProperty(String paramString1, String paramString2) {

        checkKey(paramString1);

        SecurityManager localSecurityManager = getSecurityManager();

        if (localSecurityManager != null) {

            localSecurityManager.checkPropertyAccess(paramString1);

        }


        return props.getProperty(paramString1, paramString2);

    }


    public static String setProperty(String paramString1, String paramString2) {

        checkKey(paramString1);

        SecurityManager localSecurityManager = getSecurityManager();

        if (localSecurityManager != null) {

            localSecurityManager.checkPermission(new PropertyPermission(paramString1, "write"));

        }


        return (String) props.setProperty(paramString1, paramString2);

    }


    public static String clearProperty(String paramString) {

        checkKey(paramString);

        SecurityManager localSecurityManager = getSecurityManager();

        if (localSecurityManager != null) {

            localSecurityManager.checkPermission(new PropertyPermission(paramString, "write"));

        }


        return (String) props.remove(paramString);

    }


    private static void checkKey(String paramString) {

        if (paramString == null) {

            throw new NullPointerException("key can't be null");

        }

        if (paramString.equals(""))
            throw new IllegalArgumentException("key can't be empty");

    }


    public static String getenv(String paramString) {

        SecurityManager localSecurityManager = getSecurityManager();

        if (localSecurityManager != null) {

            localSecurityManager.checkPermission(new RuntimePermission("getenv." + paramString));

        }


        return ProcessEnvironment.getenv(paramString);

    }


    public static Map<String, String> getenv() {

        SecurityManager localSecurityManager = getSecurityManager();

        if (localSecurityManager != null) {

            localSecurityManager.checkPermission(new RuntimePermission("getenv.*"));

        }


        return ProcessEnvironment.getenv();

    }


    public static void exit(int paramInt) {

        Runtime.getRuntime().exit(paramInt);

    }


    public static void gc() {

        Runtime.getRuntime().gc();

    }


    public static void runFinalization() {

        Runtime.getRuntime().runFinalization();

    }


    @Deprecated
    public static void runFinalizersOnExit(boolean paramBoolean) {

        Runtime.getRuntime();
        Runtime.runFinalizersOnExit(paramBoolean);

    }


    @CallerSensitive
    public static void load(String paramString) {

        Runtime.getRuntime().load0(Reflection.getCallerClass(), paramString);

    }


    @CallerSensitive
    public static void loadLibrary(String paramString) {

        Runtime.getRuntime().loadLibrary0(Reflection.getCallerClass(), paramString);

    }


    public static native String mapLibraryName(String paramString);


    private static void initializeSystemClass() {

        props = new Properties();

        initProperties(props);


        VM.saveAndRemoveProperties(props);


        lineSeparator = props.getProperty("line.separator");

        Version.init();


        FileInputStream localFileInputStream = new FileInputStream(FileDescriptor.in);

        FileOutputStream localFileOutputStream1 = new FileOutputStream(FileDescriptor.out);

        FileOutputStream localFileOutputStream2 = new FileOutputStream(FileDescriptor.err);

        setIn0(new BufferedInputStream(localFileInputStream));

        setOut0(new PrintStream(new BufferedOutputStream(localFileOutputStream1, 128), true));

        setErr0(new PrintStream(new BufferedOutputStream(localFileOutputStream2, 128), true));


        loadLibrary("zip");


        Terminator.setup();


        VM.initializeOSEnvironment();


        Thread localThread = Thread.currentThread();

        localThread.getThreadGroup().add(localThread);


        setJavaLangAccess();


        VM.booted();

    }


    private static void setJavaLangAccess() {

        SharedSecrets.setJavaLangAccess(new JavaLangAccess() {

            public ConstantPool getConstantPool(Class paramAnonymousClass) {

                return paramAnonymousClass.getConstantPool();

            }


            public boolean casAnnotationType(Class<?> paramAnonymousClass, AnnotationType paramAnonymousAnnotationType1, AnnotationType paramAnonymousAnnotationType2) {

                return paramAnonymousClass.casAnnotationType(paramAnonymousAnnotationType1, paramAnonymousAnnotationType2);

            }


            public AnnotationType getAnnotationType(Class paramAnonymousClass) {

                return paramAnonymousClass.getAnnotationType();

            }


            public byte[] getRawClassAnnotations(Class<?> paramAnonymousClass) {

                return paramAnonymousClass.getRawAnnotations();

            }


            public <E extends Enum<E>> E[] getEnumConstantsShared(Class<E> paramAnonymousClass) {

                return (Enum[]) paramAnonymousClass.getEnumConstantsShared();

            }


            public void blockedOn(Thread paramAnonymousThread, Interruptible paramAnonymousInterruptible) {

                paramAnonymousThread.blockedOn(paramAnonymousInterruptible);

            }


            public void registerShutdownHook(int paramAnonymousInt, boolean paramAnonymousBoolean, Runnable paramAnonymousRunnable) {

                Shutdown.add(paramAnonymousInt, paramAnonymousBoolean, paramAnonymousRunnable);

            }


            public int getStackTraceDepth(Throwable paramAnonymousThrowable) {

                return paramAnonymousThrowable.getStackTraceDepth();

            }


            public StackTraceElement getStackTraceElement(Throwable paramAnonymousThrowable, int paramAnonymousInt) {

                return paramAnonymousThrowable.getStackTraceElement(paramAnonymousInt);

            }


            public int getStringHash32(String paramAnonymousString) {

                return paramAnonymousString.hash32();

            }


            public Thread newThreadWithAcc(Runnable paramAnonymousRunnable, AccessControlContext paramAnonymousAccessControlContext) {

                return new Thread(paramAnonymousRunnable, paramAnonymousAccessControlContext);

            }


            public void invokeFinalize(Object paramAnonymousObject) throws Throwable {

                paramAnonymousObject.finalize();

            }

        });

    }


    static {

        registerNatives();

    }

}
