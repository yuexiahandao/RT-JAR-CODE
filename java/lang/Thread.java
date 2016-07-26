/*      */
package java.lang;
/*      */ 
/*      */

import java.lang.ref.Reference;
/*      */ import java.lang.ref.ReferenceQueue;
/*      */ import java.lang.ref.WeakReference;
/*      */ import java.security.AccessControlContext;
/*      */ import java.security.AccessController;
/*      */ import java.security.PrivilegedAction;
/*      */ import java.util.HashMap;
/*      */ import java.util.Map;
/*      */ import java.util.concurrent.ConcurrentHashMap;
/*      */ import java.util.concurrent.ConcurrentMap;
/*      */ import sun.misc.VM;
/*      */ import sun.nio.ch.Interruptible;
/*      */ import sun.reflect.CallerSensitive;
/*      */ import sun.reflect.Reflection;
/*      */ import sun.security.util.SecurityConstants;

/*      */
/*      */ public class Thread
/*      */ implements Runnable
/*      */ {
    /*      */   private char[] name;
    /*      */   private int priority;
    /*      */   private Thread threadQ;
    /*      */   private long eetop;
    /*      */   private boolean single_step;
    /*  157 */   private boolean daemon = false;
    /*      */
/*  160 */   private boolean stillborn = false;
    /*      */   private Runnable target;
    /*      */   private ThreadGroup group;
    /*      */   private ClassLoader contextClassLoader;
    /*      */   private AccessControlContext inheritedAccessControlContext;
    /*      */   private static int threadInitNumber;
    /*  182 */ ThreadLocal.ThreadLocalMap threadLocals = null;
    /*      */
/*  188 */ ThreadLocal.ThreadLocalMap inheritableThreadLocals = null;
    /*      */   private long stackSize;
    /*      */   private long nativeParkEventPointer;
    /*      */   private long tid;
    /*      */   private static long threadSeqNumber;
    /*  214 */   private volatile int threadStatus = 0;
    /*      */   volatile Object parkBlocker;
    /*      */   private volatile Interruptible blocker;
    /*  234 */   private final Object blockerLock = new Object();
    /*      */   public static final int MIN_PRIORITY = 1;
    /*      */   public static final int NORM_PRIORITY = 5;
    /*      */   public static final int MAX_PRIORITY = 10;
    /* 1527 */   private static final StackTraceElement[] EMPTY_STACK_TRACE = new StackTraceElement[0];
    /*      */
/* 1653 */   private static final RuntimePermission SUBCLASS_IMPLEMENTATION_PERMISSION = new RuntimePermission("enableContextClassLoaderOverride");
    /*      */   private volatile UncaughtExceptionHandler uncaughtExceptionHandler;
    /*      */   private static volatile UncaughtExceptionHandler defaultUncaughtExceptionHandler;

    /*      */
/*      */
    private static native void registerNatives();

    /*      */
/*      */
    private static synchronized int nextThreadNum()
/*      */ {
/*  177 */
        return threadInitNumber++;
/*      */
    }

    /*      */
/*      */
    private static synchronized long nextThreadID()
/*      */ {
/*  218 */
        return ++threadSeqNumber;
/*      */
    }

    /*      */
/*      */   void blockedOn(Interruptible paramInterruptible)
/*      */ {
/*  239 */
        synchronized (this.blockerLock) {
/*  240 */
            this.blocker = paramInterruptible;
/*      */
        }
/*      */
    }

    /*      */
/*      */
    public static native Thread currentThread();

    /*      */
/*      */
    public static native void yield();

    /*      */
/*      */
    public static native void sleep(long paramLong)
/*      */     throws InterruptedException;

    /*      */
/*      */
    public static void sleep(long paramLong, int paramInt)
/*      */     throws InterruptedException
/*      */ {
/*  327 */
        if (paramLong < 0L) {
/*  328 */
            throw new IllegalArgumentException("timeout value is negative");
/*      */
        }
/*      */ 
/*  331 */
        if ((paramInt < 0) || (paramInt > 999999)) {
/*  332 */
            throw new IllegalArgumentException("nanosecond timeout value out of range");
/*      */
        }
/*      */ 
/*  336 */
        if ((paramInt >= 500000) || ((paramInt != 0) && (paramLong == 0L))) {
/*  337 */
            paramLong += 1L;
/*      */
        }
/*      */ 
/*  340 */
        sleep(paramLong);
/*      */
    }

    /*      */
/*      */
    private void init(ThreadGroup paramThreadGroup, Runnable paramRunnable, String paramString, long paramLong)
/*      */ {
/*  349 */
        init(paramThreadGroup, paramRunnable, paramString, paramLong, null);
/*      */
    }

    /*      */
/*      */
    private void init(ThreadGroup paramThreadGroup, Runnable paramRunnable, String paramString, long paramLong, AccessControlContext paramAccessControlContext)
/*      */ {
/*  365 */
        if (paramString == null) {
/*  366 */
            throw new NullPointerException("name cannot be null");
/*      */
        }
/*      */ 
/*  369 */
        this.name = paramString.toCharArray();
/*      */ 
/*  371 */
        Thread localThread = currentThread();
/*  372 */
        SecurityManager localSecurityManager = System.getSecurityManager();
/*  373 */
        if (paramThreadGroup == null)
/*      */ {
/*  378 */
            if (localSecurityManager != null) {
/*  379 */
                paramThreadGroup = localSecurityManager.getThreadGroup();
/*      */
            }
/*      */ 
/*  384 */
            if (paramThreadGroup == null) {
/*  385 */
                paramThreadGroup = localThread.getThreadGroup();
/*      */
            }
/*      */ 
/*      */
        }
/*      */ 
/*  391 */
        paramThreadGroup.checkAccess();
/*      */ 
/*  396 */
        if ((localSecurityManager != null) &&
/*  397 */       (isCCLOverridden(getClass()))) {
/*  398 */
            localSecurityManager.checkPermission(SUBCLASS_IMPLEMENTATION_PERMISSION);
/*      */
        }
/*      */ 
/*  402 */
        paramThreadGroup.addUnstarted();
/*      */ 
/*  404 */
        this.group = paramThreadGroup;
/*  405 */
        this.daemon = localThread.isDaemon();
/*  406 */
        this.priority = localThread.getPriority();
/*  407 */
        if ((localSecurityManager == null) || (isCCLOverridden(localThread.getClass())))
/*  408 */ this.contextClassLoader = localThread.getContextClassLoader();
/*      */
        else
/*  410 */       this.contextClassLoader = localThread.contextClassLoader;
/*  411 */
        this.inheritedAccessControlContext = (paramAccessControlContext != null ? paramAccessControlContext : AccessController.getContext());
/*      */ 
/*  413 */
        this.target = paramRunnable;
/*  414 */
        setPriority(this.priority);
/*  415 */
        if (localThread.inheritableThreadLocals != null) {
/*  416 */
            this.inheritableThreadLocals = ThreadLocal.createInheritedMap(localThread.inheritableThreadLocals);
/*      */
        }
/*      */ 
/*  419 */
        this.stackSize = paramLong;
/*      */ 
/*  422 */
        this.tid = nextThreadID();
/*      */
    }

    /*      */
/*      */
    protected Object clone()
/*      */     throws CloneNotSupportedException
/*      */ {
/*  434 */
        throw new CloneNotSupportedException();
/*      */
    }

    /*      */
/*      */
    public Thread()
/*      */ {
/*  445 */
        init(null, null, "Thread-" + nextThreadNum(), 0L);
/*      */
    }

    /*      */
/*      */
    public Thread(Runnable paramRunnable)
/*      */ {
/*  461 */
        init(null, paramRunnable, "Thread-" + nextThreadNum(), 0L);
/*      */
    }

    /*      */
/*      */   Thread(Runnable paramRunnable, AccessControlContext paramAccessControlContext)
/*      */ {
/*  469 */
        init(null, paramRunnable, "Thread-" + nextThreadNum(), 0L, paramAccessControlContext);
/*      */
    }

    /*      */
/*      */
    public Thread(ThreadGroup paramThreadGroup, Runnable paramRunnable)
/*      */ {
/*  496 */
        init(paramThreadGroup, paramRunnable, "Thread-" + nextThreadNum(), 0L);
/*      */
    }

    /*      */
/*      */
    public Thread(String paramString)
/*      */ {
/*  508 */
        init(null, null, paramString, 0L);
/*      */
    }

    /*      */
/*      */
    public Thread(ThreadGroup paramThreadGroup, String paramString)
/*      */ {
/*  532 */
        init(paramThreadGroup, null, paramString, 0L);
/*      */
    }

    /*      */
/*      */
    public Thread(Runnable paramRunnable, String paramString)
/*      */ {
/*  548 */
        init(null, paramRunnable, paramString, 0L);
/*      */
    }

    /*      */
/*      */
    public Thread(ThreadGroup paramThreadGroup, Runnable paramRunnable, String paramString)
/*      */ {
/*  596 */
        init(paramThreadGroup, paramRunnable, paramString, 0L);
/*      */
    }

    /*      */
/*      */
    public Thread(ThreadGroup paramThreadGroup, Runnable paramRunnable, String paramString, long paramLong)
/*      */ {
/*  675 */
        init(paramThreadGroup, paramRunnable, paramString, paramLong);
/*      */
    }

    /*      */
/*      */
    public synchronized void start()
/*      */ {
/*  704 */
        if (this.threadStatus != 0) {
/*  705 */
            throw new IllegalThreadStateException();
/*      */
        }
/*      */ 
/*  710 */
        this.group.add(this);
/*      */ 
/*  712 */
        int i = 0;
/*      */
        try {
/*  714 */
            start0();
/*  715 */
            i = 1;
/*      */
        } finally {
/*      */
            try {
/*  718 */
                if (i == 0)
/*  719 */ this.group.threadStartFailed(this);
/*      */
            }
/*      */ catch (Throwable localThrowable2)
/*      */ {
/*      */
            }
/*      */
        }
/*      */
    }

    /*      */
/*      */
    private native void start0();

    /*      */
/*      */
    public void run()
/*      */ {
/*  744 */
        if (this.target != null)
/*  745 */ this.target.run();
/*      */
    }

    /*      */
/*      */
    private void exit()
/*      */ {
/*  754 */
        if (this.group != null) {
/*  755 */
            this.group.threadTerminated(this);
/*  756 */
            this.group = null;
/*      */
        }
/*      */ 
/*  759 */
        this.target = null;
/*      */ 
/*  761 */
        this.threadLocals = null;
/*  762 */
        this.inheritableThreadLocals = null;
/*  763 */
        this.inheritedAccessControlContext = null;
/*  764 */
        this.blocker = null;
/*  765 */
        this.uncaughtExceptionHandler = null;
/*      */
    }

    /*      */
/*      */
    @Deprecated
/*      */ public final void stop()
/*      */ {
/*  836 */
        stop(new ThreadDeath());
/*      */
    }

    /*      */
/*      */
    @Deprecated
/*      */ public final synchronized void stop(Throwable paramThrowable)
/*      */ {
/*  890 */
        if (paramThrowable == null) {
/*  891 */
            throw new NullPointerException();
/*      */
        }
/*  893 */
        SecurityManager localSecurityManager = System.getSecurityManager();
/*  894 */
        if (localSecurityManager != null) {
/*  895 */
            checkAccess();
/*  896 */
            if ((this != currentThread()) || (!(paramThrowable instanceof ThreadDeath)))
/*      */ {
/*  898 */
                localSecurityManager.checkPermission(SecurityConstants.STOP_THREAD_PERMISSION);
/*      */
            }
/*      */ 
/*      */
        }
/*      */ 
/*  903 */
        if (this.threadStatus != 0) {
/*  904 */
            resume();
/*      */
        }
/*      */ 
/*  908 */
        stop0(paramThrowable);
/*      */
    }

    /*      */
/*      */
    public void interrupt()
/*      */ {
/*  951 */
        if (this != currentThread()) {
/*  952 */
            checkAccess();
/*      */
        }
/*  954 */
        synchronized (this.blockerLock) {
/*  955 */
            Interruptible localInterruptible = this.blocker;
/*  956 */
            if (localInterruptible != null) {
/*  957 */
                interrupt0();
/*  958 */
                localInterruptible.interrupt(this);
/*  959 */
                return;
/*      */
            }
/*      */
        }
/*  962 */
        interrupt0();
/*      */
    }

    /*      */
/*      */
    public static boolean interrupted()
/*      */ {
/*  983 */
        return currentThread().isInterrupted(true);
/*      */
    }

    /*      */
/*      */
    public boolean isInterrupted()
/*      */ {
/* 1000 */
        return isInterrupted(false);
/*      */
    }

    /*      */
/*      */
    private native boolean isInterrupted(boolean paramBoolean);

    /*      */
/*      */
    @Deprecated
/*      */ public void destroy()
/*      */ {
/* 1029 */
        throw new NoSuchMethodError();
/*      */
    }

    /*      */
/*      */
    public final native boolean isAlive();

    /*      */
/*      */
    @Deprecated
/*      */ public final void suspend()
/*      */ {
/* 1067 */
        checkAccess();
/* 1068 */
        suspend0();
/*      */
    }

    /*      */
/*      */
    @Deprecated
/*      */ public final void resume()
/*      */ {
/* 1093 */
        checkAccess();
/* 1094 */
        resume0();
/*      */
    }

    /*      */
/*      */
    public final void setPriority(int paramInt)
/*      */ {
/* 1123 */
        checkAccess();
/* 1124 */
        if ((paramInt > 10) || (paramInt < 1))
/* 1125 */ throw new IllegalArgumentException();
/*      */
        ThreadGroup localThreadGroup;
/* 1127 */
        if ((localThreadGroup = getThreadGroup()) != null) {
/* 1128 */
            if (paramInt > localThreadGroup.getMaxPriority()) {
/* 1129 */
                paramInt = localThreadGroup.getMaxPriority();
/*      */
            }
/* 1131 */
            setPriority0(this.priority = paramInt);
/*      */
        }
/*      */
    }

    /*      */
/*      */
    public final int getPriority()
/*      */ {
/* 1142 */
        return this.priority;
/*      */
    }

    /*      */
/*      */
    public final void setName(String paramString)
/*      */ {
/* 1160 */
        checkAccess();
/* 1161 */
        this.name = paramString.toCharArray();
/*      */
    }

    /*      */
/*      */
    public final String getName()
/*      */ {
/* 1171 */
        return String.valueOf(this.name);
/*      */
    }

    /*      */
/*      */
    public final ThreadGroup getThreadGroup()
/*      */ {
/* 1182 */
        return this.group;
/*      */
    }

    /*      */
/*      */
    public static int activeCount()
/*      */ {
/* 1202 */
        return currentThread().getThreadGroup().activeCount();
/*      */
    }

    /*      */
/*      */
    public static int enumerate(Thread[] paramArrayOfThread)
/*      */ {
/* 1232 */
        return currentThread().getThreadGroup().enumerate(paramArrayOfThread);
/*      */
    }

    /*      */
/*      */
    @Deprecated
/*      */ public native int countStackFrames();

    /*      */
/*      */
    public final synchronized void join(long paramLong)
/*      */     throws InterruptedException
/*      */ {
/* 1272 */
        long l1 = System.currentTimeMillis();
/* 1273 */
        long l2 = 0L;
/*      */ 
/* 1275 */
        if (paramLong < 0L) {
/* 1276 */
            throw new IllegalArgumentException("timeout value is negative");
/*      */
        }
/*      */ 
/* 1279 */
        if (paramLong == 0L) {
/* 1280 */
            while (isAlive()) {
/* 1281 */
                wait(0L);
/*      */
            }
/*      */
        }
/* 1284 */
        while (isAlive()) {
/* 1285 */
            long l3 = paramLong - l2;
/* 1286 */
            if (l3 <= 0L) {
/*      */
                break;
/*      */
            }
/* 1289 */
            wait(l3);
/* 1290 */
            l2 = System.currentTimeMillis() - l1;
/*      */
        }
/*      */
    }

    /*      */
/*      */
    public final synchronized void join(long paramLong, int paramInt)
/*      */     throws InterruptedException
/*      */ {
/* 1323 */
        if (paramLong < 0L) {
/* 1324 */
            throw new IllegalArgumentException("timeout value is negative");
/*      */
        }
/*      */ 
/* 1327 */
        if ((paramInt < 0) || (paramInt > 999999)) {
/* 1328 */
            throw new IllegalArgumentException("nanosecond timeout value out of range");
/*      */
        }
/*      */ 
/* 1332 */
        if ((paramInt >= 500000) || ((paramInt != 0) && (paramLong == 0L))) {
/* 1333 */
            paramLong += 1L;
/*      */
        }
/*      */ 
/* 1336 */
        join(paramLong);
/*      */
    }

    /*      */
/*      */
    public final void join()
/*      */     throws InterruptedException
/*      */ {
/* 1355 */
        join(0L);
/*      */
    }

    /*      */
/*      */
    public static void dumpStack()
/*      */ {
/* 1365 */
        new Exception("Stack trace").printStackTrace();
/*      */
    }

    /*      */
/*      */
    public final void setDaemon(boolean paramBoolean)
/*      */ {
/* 1386 */
        checkAccess();
/* 1387 */
        if (isAlive()) {
/* 1388 */
            throw new IllegalThreadStateException();
/*      */
        }
/* 1390 */
        this.daemon = paramBoolean;
/*      */
    }

    /*      */
/*      */
    public final boolean isDaemon()
/*      */ {
/* 1401 */
        return this.daemon;
/*      */
    }

    /*      */
/*      */
    public final void checkAccess()
/*      */ {
/* 1417 */
        SecurityManager localSecurityManager = System.getSecurityManager();
/* 1418 */
        if (localSecurityManager != null)
/* 1419 */ localSecurityManager.checkAccess(this);
/*      */
    }

    /*      */
/*      */
    public String toString()
/*      */ {
/* 1430 */
        ThreadGroup localThreadGroup = getThreadGroup();
/* 1431 */
        if (localThreadGroup != null) {
/* 1432 */
            return "Thread[" + getName() + "," + getPriority() + "," + localThreadGroup.getName() + "]";
/*      */
        }
/*      */ 
/* 1435 */
        return "Thread[" + getName() + "," + getPriority() + "," + "" + "]";
/*      */
    }

    /*      */
/*      */
    @CallerSensitive
/*      */ public ClassLoader getContextClassLoader()
/*      */ {
/* 1468 */
        if (this.contextClassLoader == null) {
/* 1469 */
            return null;
/*      */
        }
/* 1471 */
        SecurityManager localSecurityManager = System.getSecurityManager();
/* 1472 */
        if (localSecurityManager != null) {
/* 1473 */
            ClassLoader.checkClassLoaderPermission(this.contextClassLoader, Reflection.getCallerClass());
/*      */
        }
/*      */ 
/* 1476 */
        return this.contextClassLoader;
/*      */
    }

    /*      */
/*      */
    public void setContextClassLoader(ClassLoader paramClassLoader)
/*      */ {
/* 1502 */
        SecurityManager localSecurityManager = System.getSecurityManager();
/* 1503 */
        if (localSecurityManager != null) {
/* 1504 */
            localSecurityManager.checkPermission(new RuntimePermission("setContextClassLoader"));
/*      */
        }
/* 1506 */
        this.contextClassLoader = paramClassLoader;
/*      */
    }

    /*      */
/*      */
    public static native boolean holdsLock(Object paramObject);

    /*      */
/*      */
    public StackTraceElement[] getStackTrace()
/*      */ {
/* 1567 */
        if (this != currentThread())
/*      */ {
/* 1569 */
            SecurityManager localSecurityManager = System.getSecurityManager();
/* 1570 */
            if (localSecurityManager != null) {
/* 1571 */
                localSecurityManager.checkPermission(SecurityConstants.GET_STACK_TRACE_PERMISSION);
/*      */
            }
/*      */ 
/* 1576 */
            if (!isAlive()) {
/* 1577 */
                return EMPTY_STACK_TRACE;
/*      */
            }
/* 1579 */
            StackTraceElement[][] arrayOfStackTraceElement = dumpThreads(new Thread[]{this});
/* 1580 */
            StackTraceElement[] arrayOfStackTraceElement1 = arrayOfStackTraceElement[0];
/*      */ 
/* 1583 */
            if (arrayOfStackTraceElement1 == null) {
/* 1584 */
                arrayOfStackTraceElement1 = EMPTY_STACK_TRACE;
/*      */
            }
/* 1586 */
            return arrayOfStackTraceElement1;
/*      */
        }
/*      */ 
/* 1589 */
        return new Exception().getStackTrace();
/*      */
    }

    /*      */
/*      */
    public static Map<Thread, StackTraceElement[]> getAllStackTraces()
/*      */ {
/* 1630 */
        SecurityManager localSecurityManager = System.getSecurityManager();
/* 1631 */
        if (localSecurityManager != null) {
/* 1632 */
            localSecurityManager.checkPermission(SecurityConstants.GET_STACK_TRACE_PERMISSION);
/*      */ 
/* 1634 */
            localSecurityManager.checkPermission(SecurityConstants.MODIFY_THREADGROUP_PERMISSION);
/*      */
        }
/*      */ 
/* 1639 */
        Thread[] arrayOfThread = getThreads();
/* 1640 */
        StackTraceElement[][] arrayOfStackTraceElement = dumpThreads(arrayOfThread);
/* 1641 */
        HashMap localHashMap = new HashMap(arrayOfThread.length);
/* 1642 */
        for (int i = 0; i < arrayOfThread.length; i++) {
/* 1643 */
            StackTraceElement[] arrayOfStackTraceElement1 = arrayOfStackTraceElement[i];
/* 1644 */
            if (arrayOfStackTraceElement1 != null) {
/* 1645 */
                localHashMap.put(arrayOfThread[i], arrayOfStackTraceElement1);
/*      */
            }
/*      */
        }
/*      */ 
/* 1649 */
        return localHashMap;
/*      */
    }

    /*      */
/*      */
    private static boolean isCCLOverridden(Class paramClass)
/*      */ {
/* 1676 */
        if (paramClass == Thread.class) {
/* 1677 */
            return false;
/*      */
        }
/* 1679 */
        processQueue(Caches.subclassAuditsQueue, Caches.subclassAudits);
/* 1680 */
        WeakClassKey localWeakClassKey = new WeakClassKey(paramClass, Caches.subclassAuditsQueue);
/* 1681 */
        Boolean localBoolean = (Boolean) Caches.subclassAudits.get(localWeakClassKey);
/* 1682 */
        if (localBoolean == null) {
/* 1683 */
            localBoolean = Boolean.valueOf(auditSubclass(paramClass));
/* 1684 */
            Caches.subclassAudits.putIfAbsent(localWeakClassKey, localBoolean);
/*      */
        }
/*      */ 
/* 1687 */
        return localBoolean.booleanValue();
/*      */
    }

    /*      */
/*      */
    private static boolean auditSubclass(Class paramClass)
/*      */ {
/* 1696 */
        Boolean localBoolean = (Boolean) AccessController.doPrivileged(new PrivilegedAction()
/*      */ {
            /*      */
            public Boolean run() {
/* 1699 */
                for (Class localClass = this.val$subcl;
/* 1700 */           localClass != Thread.class; 
/* 1701 */           localClass = localClass.getSuperclass())
/*      */
                    try
/*      */ {
/* 1704 */
                        localClass.getDeclaredMethod("getContextClassLoader", new Class[0]);
/* 1705 */
                        return Boolean.TRUE;
/*      */
                    }
/*      */ catch (NoSuchMethodException localNoSuchMethodException1) {
/*      */
                        try {
/* 1709 */
                            Class[] arrayOfClass = {ClassLoader.class};
/* 1710 */
                            localClass.getDeclaredMethod("setContextClassLoader", arrayOfClass);
/* 1711 */
                            return Boolean.TRUE;
/*      */
                        } catch (NoSuchMethodException localNoSuchMethodException2) {
/*      */
                        }
/*      */
                    }
/* 1715 */
                return Boolean.FALSE;
/*      */
            }
/*      */
        });
/* 1719 */
        return localBoolean.booleanValue();
/*      */
    }

    /*      */
/*      */
    private static native StackTraceElement[][] dumpThreads(Thread[] paramArrayOfThread);

    /*      */
/*      */
    private static native Thread[] getThreads();

    /*      */
/*      */
    public long getId()
/*      */ {
/* 1735 */
        return this.tid;
/*      */
    }

    /*      */
/*      */
    public State getState()
/*      */ {
/* 1847 */
        return VM.toThreadState(this.threadStatus);
/*      */
    }

    /*      */
/*      */
    public static void setDefaultUncaughtExceptionHandler(UncaughtExceptionHandler paramUncaughtExceptionHandler)
/*      */ {
/* 1927 */
        SecurityManager localSecurityManager = System.getSecurityManager();
/* 1928 */
        if (localSecurityManager != null) {
/* 1929 */
            localSecurityManager.checkPermission(new RuntimePermission("setDefaultUncaughtExceptionHandler"));
/*      */
        }
/*      */ 
/* 1934 */
        defaultUncaughtExceptionHandler = paramUncaughtExceptionHandler;
/*      */
    }

    /*      */
/*      */
    public static UncaughtExceptionHandler getDefaultUncaughtExceptionHandler()
/*      */ {
/* 1945 */
        return defaultUncaughtExceptionHandler;
/*      */
    }

    /*      */
/*      */
    public UncaughtExceptionHandler getUncaughtExceptionHandler()
/*      */ {
/* 1957 */
        return this.uncaughtExceptionHandler != null ? this.uncaughtExceptionHandler : this.group;
/*      */
    }

    /*      */
/*      */
    public void setUncaughtExceptionHandler(UncaughtExceptionHandler paramUncaughtExceptionHandler)
/*      */ {
/* 1977 */
        checkAccess();
/* 1978 */
        this.uncaughtExceptionHandler = paramUncaughtExceptionHandler;
/*      */
    }

    /*      */
/*      */
    private void dispatchUncaughtException(Throwable paramThrowable)
/*      */ {
/* 1986 */
        getUncaughtExceptionHandler().uncaughtException(this, paramThrowable);
/*      */
    }

    /*      */
/*      */
    static void processQueue(ReferenceQueue<Class<?>> paramReferenceQueue, ConcurrentMap<? extends WeakReference<Class<?>>, ?> paramConcurrentMap)
/*      */ {
/*      */
        Reference localReference;
/* 1998 */
        while ((localReference = paramReferenceQueue.poll()) != null)
/* 1999 */ paramConcurrentMap.remove(localReference);
/*      */
    }

    /*      */
/*      */
    private native void setPriority0(int paramInt);

    /*      */
/*      */
    private native void stop0(Object paramObject);

    /*      */
/*      */
    private native void suspend0();

    /*      */
/*      */
    private native void resume0();

    /*      */
/*      */
    private native void interrupt0();

    /*      */
/*      */
    private native void setNativeName(String paramString);

    /*      */
/*      */   static
/*      */ {
/*  145 */
        registerNatives();
/*      */
    }

    /*      */
/*      */   private static class Caches
/*      */ {
        /* 1661 */     static final ConcurrentMap<Thread.WeakClassKey, Boolean> subclassAudits = new ConcurrentHashMap();
        /*      */
/* 1665 */     static final ReferenceQueue<Class<?>> subclassAuditsQueue = new ReferenceQueue();
/*      */
    }

    /*      */
/*      */   public static enum State
/*      */ {
        /* 1776 */     NEW,
        /*      */
/* 1784 */     RUNNABLE,
        /*      */
/* 1793 */     BLOCKED,
        /*      */
/* 1814 */     WAITING,
        /*      */
/* 1828 */     TIMED_WAITING,
        /*      */
/* 1834 */     TERMINATED;
/*      */
    }

    /*      */
/*      */   public static abstract interface UncaughtExceptionHandler
/*      */ {
        /*      */
        public abstract void uncaughtException(Thread paramThread, Throwable paramThrowable);
/*      */
    }

    /*      */
/*      */   static class WeakClassKey extends WeakReference<Class<?>>
/*      */ {
        /*      */     private final int hash;

        /*      */
/*      */     WeakClassKey(Class<?> paramClass, ReferenceQueue<Class<?>> paramReferenceQueue)
/*      */ {
/* 2018 */
            super(paramReferenceQueue);
/* 2019 */
            this.hash = System.identityHashCode(paramClass);
/*      */
        }

        /*      */
/*      */
        public int hashCode()
/*      */ {
/* 2027 */
            return this.hash;
/*      */
        }

        /*      */
/*      */
        public boolean equals(Object paramObject)
/*      */ {
/* 2038 */
            if (paramObject == this) {
/* 2039 */
                return true;
/*      */
            }
/* 2041 */
            if ((paramObject instanceof WeakClassKey)) {
/* 2042 */
                Object localObject = get();
/* 2043 */
                return (localObject != null) && (localObject == ((WeakClassKey) paramObject).get());
/*      */
            }
/*      */ 
/* 2046 */
            return false;
/*      */
        }
/*      */
    }
/*      */
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.lang.Thread
 * JD-Core Version:    0.6.2
 */