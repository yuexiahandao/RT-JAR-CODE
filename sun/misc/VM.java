package sun.misc;

import java.util.Properties;

// 具体的源码查看：http://hg.openjdk.java.net/jdk7u/jdk7u/jdk/file/tip/src/share/classes/sun/misc/VM.java

/**
 * 这个类应该是抽象出来的JVM操作和实体类
 *
 * 用于检查VM是否启动
 * 设置系统的属性
 * 初始化环境变量
 */
public class VM {
    /**
     * 下面的方法都是本地方法，指示出VM在低内存情况下有选择地暂停某些线程。他们本质上是危险的，不能在本地线程上实现。
     * 我们在JDK1.2中删除它们。骨架任然保留，使现有的使用这些方法的应用任然可以工作。
     */
    // 是否暂停
    private static boolean suspended = false;

    @Deprecated
    // 绿色状态
    public static final int STATE_GREEN = 1;

    @Deprecated
    // 黄色状态
    public static final int STATE_YELLOW = 2;

    @Deprecated
    // 红色状态
    public static final int STATE_RED = 3;
    // 是否启动了
    private static volatile boolean booted = false;
    // 新建一个对象锁
    private static final Object lock = new Object();

    private static long directMemory = 67108864L;
    private static boolean pageAlignDirectMemory;
    private static boolean defaultAllowArraySyntax = false;
    private static boolean allowArraySyntax = defaultAllowArraySyntax;

    private static boolean allowGetCallerClass = true;

    private static final Properties savedProps = new Properties();

    private static volatile int finalRefCount = 0;

    private static volatile int peakFinalRefCount = 0;
    private static final int JVMTI_THREAD_STATE_ALIVE = 1;
    private static final int JVMTI_THREAD_STATE_TERMINATED = 2;
    private static final int JVMTI_THREAD_STATE_RUNNABLE = 4;
    private static final int JVMTI_THREAD_STATE_BLOCKED_ON_MONITOR_ENTER = 1024;
    private static final int JVMTI_THREAD_STATE_WAITING_INDEFINITELY = 16;
    private static final int JVMTI_THREAD_STATE_WAITING_WITH_TIMEOUT = 32;

    @Deprecated
    // 线程是否暂停
    public static boolean threadsSuspended() {
        return suspended;
    }

    /**
     * 运行线程暂停
     * @param paramThreadGroup：线程组
     * @param paramBoolean： bool参数
     * @return
     */
    public static boolean allowThreadSuspension(ThreadGroup paramThreadGroup, boolean paramBoolean) {
        // 调用线程组来处理
        return paramThreadGroup.allowThreadSuspension(paramBoolean);
    }

    @Deprecated
    public static boolean suspendThreads() {
        suspended = true;
        return true;
    }

    @Deprecated
    public static void unsuspendThreads() {
        suspended = false;
    }

    @Deprecated
    public static void unsuspendSomeThreads() {
    }

    @Deprecated
    public static final int getState() {
        return 1;
    }

    @Deprecated
    public static void registerVMNotification(VMNotification paramVMNotification) {
    }

    @Deprecated
    public static void asChange(int paramInt1, int paramInt2) {
    }

    @Deprecated
    public static void asChange_otherthread(int paramInt1, int paramInt2) {
    }

    public static void booted() {
        synchronized (lock) {
            booted = true;
            lock.notifyAll();
        }
    }

    public static boolean isBooted() {
        return booted;
    }

    public static void awaitBooted()
            throws InterruptedException {
        synchronized (lock) {
            while (!booted)
                lock.wait();
        }
    }

    public static long maxDirectMemory() {
        return directMemory;
    }

    public static boolean isDirectMemoryPageAligned() {
        return pageAlignDirectMemory;
    }

    public static boolean allowArraySyntax() {
        return allowArraySyntax;
    }

    public static boolean allowGetCallerClass() {
        return allowGetCallerClass;
    }

    public static String getSavedProperty(String paramString) {
        if (savedProps.isEmpty()) {
            throw new IllegalStateException("Should be non-empty if initialized");
        }
        return savedProps.getProperty(paramString);
    }

    public static void saveAndRemoveProperties(Properties paramProperties) {
        if (booted) {
            throw new IllegalStateException("System initialization has completed");
        }
        savedProps.putAll(paramProperties);

        String str = (String) paramProperties.remove("sun.nio.MaxDirectMemorySize");
        if (str != null) {
            if (str.equals("-1")) {
                directMemory = Runtime.getRuntime().maxMemory();
            } else {
                long l = Long.parseLong(str);
                if (l > -1L) {
                    directMemory = l;
                }
            }
        }

        str = (String) paramProperties.remove("sun.nio.PageAlignDirectMemory");
        if ("true".equals(str)) {
            pageAlignDirectMemory = true;
        }

        str = paramProperties.getProperty("sun.lang.ClassLoader.allowArraySyntax");
        allowArraySyntax = str == null ? defaultAllowArraySyntax : Boolean.parseBoolean(str);

        str = paramProperties.getProperty("jdk.reflect.allowGetCallerClass");
        allowGetCallerClass = (str == null) || (str.isEmpty()) || (Boolean.parseBoolean(str)) || (Boolean.valueOf(paramProperties.getProperty("jdk.logging.allowStackWalkSearch")).booleanValue());

        paramProperties.remove("java.lang.Integer.IntegerCache.high");

        paramProperties.remove("sun.zip.disableMemoryMapping");

        paramProperties.remove("sun.java.launcher.diag");
    }

    public static void initializeOSEnvironment() {
        if (!booted)
            OSEnvironment.initialize();
    }

    public static int getFinalRefCount() {
        return finalRefCount;
    }

    public static int getPeakFinalRefCount() {
        return peakFinalRefCount;
    }

    public static void addFinalRefCount(int paramInt) {
        finalRefCount += paramInt;
        if (finalRefCount > peakFinalRefCount)
            peakFinalRefCount = finalRefCount;
    }

    public static Thread.State toThreadState(int paramInt) {
        if ((paramInt & 0x4) != 0)
            return Thread.State.RUNNABLE;
        if ((paramInt & 0x400) != 0)
            return Thread.State.BLOCKED;
        if ((paramInt & 0x10) != 0)
            return Thread.State.WAITING;
        if ((paramInt & 0x20) != 0)
            return Thread.State.TIMED_WAITING;
        if ((paramInt & 0x2) != 0)
            return Thread.State.TERMINATED;
        if ((paramInt & 0x1) == 0) {
            return Thread.State.NEW;
        }
        return Thread.State.RUNNABLE;
    }

    public static native ClassLoader latestUserDefinedLoader();

    private static native void initialize();

    static {
        initialize();
    }
}