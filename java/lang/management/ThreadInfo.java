/*     */
package java.lang.management;
/*     */ 
/*     */

import javax.management.openmbean.CompositeData;
/*     */ import sun.management.ManagementFactoryHelper;
/*     */ import sun.management.ThreadInfoCompositeData;

/*     */
/*     */ public class ThreadInfo
/*     */ {
    /*     */   private String threadName;
    /*     */   private long threadId;
    /*     */   private long blockedTime;
    /*     */   private long blockedCount;
    /*     */   private long waitedTime;
    /*     */   private long waitedCount;
    /*     */   private LockInfo lock;
    /*     */   private String lockName;
    /*     */   private long lockOwnerId;
    /*     */   private String lockOwnerName;
    /*     */   private boolean inNative;
    /*     */   private boolean suspended;
    /*     */   private Thread.State threadState;
    /*     */   private StackTraceElement[] stackTrace;
    /*     */   private MonitorInfo[] lockedMonitors;
    /*     */   private LockInfo[] lockedSynchronizers;
    /* 110 */   private static MonitorInfo[] EMPTY_MONITORS = new MonitorInfo[0];
    /* 111 */   private static LockInfo[] EMPTY_SYNCS = new LockInfo[0];
    /*     */   private static final int MAX_FRAMES = 8;
    /* 837 */   private static final StackTraceElement[] NO_STACK_TRACE = new StackTraceElement[0];

    /*     */
/*     */
    private ThreadInfo(Thread paramThread1, int paramInt, Object paramObject, Thread paramThread2, long paramLong1, long paramLong2, long paramLong3, long paramLong4, StackTraceElement[] paramArrayOfStackTraceElement)
/*     */ {
/* 130 */
        initialize(paramThread1, paramInt, paramObject, paramThread2, paramLong1, paramLong2, paramLong3, paramLong4, paramArrayOfStackTraceElement, EMPTY_MONITORS, EMPTY_SYNCS);
/*     */
    }

    /*     */
/*     */
    private ThreadInfo(Thread paramThread1, int paramInt, Object paramObject, Thread paramThread2, long paramLong1, long paramLong2, long paramLong3, long paramLong4, StackTraceElement[] paramArrayOfStackTraceElement, Object[] paramArrayOfObject1, int[] paramArrayOfInt, Object[] paramArrayOfObject2)
/*     */ {
/* 160 */
        int i = paramArrayOfObject1 == null ? 0 : paramArrayOfObject1.length;
/*     */
        MonitorInfo[] arrayOfMonitorInfo;
/*     */
        Object localObject1;
/* 162 */
        if (i == 0) {
/* 163 */
            arrayOfMonitorInfo = EMPTY_MONITORS;
/*     */
        } else {
/* 165 */
            arrayOfMonitorInfo = new MonitorInfo[i];
/* 166 */
            for (j = 0; j < i; j++) {
/* 167 */
                localObject1 = paramArrayOfObject1[j];
/* 168 */
                String str1 = localObject1.getClass().getName();
/* 169 */
                int m = System.identityHashCode(localObject1);
/* 170 */
                int n = paramArrayOfInt[j];
/* 171 */
                StackTraceElement localStackTraceElement = n >= 0 ? paramArrayOfStackTraceElement[n] : null;
/*     */ 
/* 173 */
                arrayOfMonitorInfo[j] = new MonitorInfo(str1, m, n, localStackTraceElement);
/*     */
            }
/*     */ 
/*     */
        }
/*     */ 
/* 180 */
        int j = paramArrayOfObject2 == null ? 0 : paramArrayOfObject2.length;
/*     */ 
/* 182 */
        if (j == 0) {
/* 183 */
            localObject1 = EMPTY_SYNCS;
/*     */
        } else {
/* 185 */
            localObject1 = new LockInfo[j];
/* 186 */
            for (int k = 0; k < j; k++) {
/* 187 */
                Object localObject2 = paramArrayOfObject2[k];
/* 188 */
                String str2 = localObject2.getClass().getName();
/* 189 */
                int i1 = System.identityHashCode(localObject2);
/* 190 */
                localObject1[k] = new LockInfo(str2, i1);
/*     */
            }
/*     */ 
/*     */
        }
/*     */ 
/* 195 */
        initialize(paramThread1, paramInt, paramObject, paramThread2, paramLong1, paramLong2, paramLong3, paramLong4, paramArrayOfStackTraceElement, arrayOfMonitorInfo, (LockInfo[]) localObject1);
/*     */
    }

    /*     */
/*     */
    private void initialize(Thread paramThread1, int paramInt, Object paramObject, Thread paramThread2, long paramLong1, long paramLong2, long paramLong3, long paramLong4, StackTraceElement[] paramArrayOfStackTraceElement, MonitorInfo[] paramArrayOfMonitorInfo, LockInfo[] paramArrayOfLockInfo)
/*     */ {
/* 222 */
        this.threadId = paramThread1.getId();
/* 223 */
        this.threadName = paramThread1.getName();
/* 224 */
        this.threadState = ManagementFactoryHelper.toThreadState(paramInt);
/* 225 */
        this.suspended = ManagementFactoryHelper.isThreadSuspended(paramInt);
/* 226 */
        this.inNative = ManagementFactoryHelper.isThreadRunningNative(paramInt);
/* 227 */
        this.blockedCount = paramLong1;
/* 228 */
        this.blockedTime = paramLong2;
/* 229 */
        this.waitedCount = paramLong3;
/* 230 */
        this.waitedTime = paramLong4;
/*     */ 
/* 232 */
        if (paramObject == null) {
/* 233 */
            this.lock = null;
/* 234 */
            this.lockName = null;
/*     */
        } else {
/* 236 */
            this.lock = new LockInfo(paramObject);
/* 237 */
            this.lockName = (this.lock.getClassName() + '@' + Integer.toHexString(this.lock.getIdentityHashCode()));
/*     */
        }
/*     */ 
/* 241 */
        if (paramThread2 == null) {
/* 242 */
            this.lockOwnerId = -1L;
/* 243 */
            this.lockOwnerName = null;
/*     */
        } else {
/* 245 */
            this.lockOwnerId = paramThread2.getId();
/* 246 */
            this.lockOwnerName = paramThread2.getName();
/*     */
        }
/* 248 */
        if (paramArrayOfStackTraceElement == null)
/* 249 */ this.stackTrace = NO_STACK_TRACE;
/*     */
        else {
/* 251 */
            this.stackTrace = paramArrayOfStackTraceElement;
/*     */
        }
/* 253 */
        this.lockedMonitors = paramArrayOfMonitorInfo;
/* 254 */
        this.lockedSynchronizers = paramArrayOfLockInfo;
/*     */
    }

    /*     */
/*     */
    private ThreadInfo(CompositeData paramCompositeData)
/*     */ {
/* 262 */
        ThreadInfoCompositeData localThreadInfoCompositeData = ThreadInfoCompositeData.getInstance(paramCompositeData);
/*     */ 
/* 264 */
        this.threadId = localThreadInfoCompositeData.threadId();
/* 265 */
        this.threadName = localThreadInfoCompositeData.threadName();
/* 266 */
        this.blockedTime = localThreadInfoCompositeData.blockedTime();
/* 267 */
        this.blockedCount = localThreadInfoCompositeData.blockedCount();
/* 268 */
        this.waitedTime = localThreadInfoCompositeData.waitedTime();
/* 269 */
        this.waitedCount = localThreadInfoCompositeData.waitedCount();
/* 270 */
        this.lockName = localThreadInfoCompositeData.lockName();
/* 271 */
        this.lockOwnerId = localThreadInfoCompositeData.lockOwnerId();
/* 272 */
        this.lockOwnerName = localThreadInfoCompositeData.lockOwnerName();
/* 273 */
        this.threadState = localThreadInfoCompositeData.threadState();
/* 274 */
        this.suspended = localThreadInfoCompositeData.suspended();
/* 275 */
        this.inNative = localThreadInfoCompositeData.inNative();
/* 276 */
        this.stackTrace = localThreadInfoCompositeData.stackTrace();
/*     */ 
/* 279 */
        if (localThreadInfoCompositeData.isCurrentVersion()) {
/* 280 */
            this.lock = localThreadInfoCompositeData.lockInfo();
/* 281 */
            this.lockedMonitors = localThreadInfoCompositeData.lockedMonitors();
/* 282 */
            this.lockedSynchronizers = localThreadInfoCompositeData.lockedSynchronizers();
/*     */
        }
/*     */
        else
/*     */ {
/* 287 */
            if (this.lockName != null) {
/* 288 */
                String[] arrayOfString = this.lockName.split("@");
/* 289 */
                if (arrayOfString.length == 2) {
/* 290 */
                    int i = Integer.parseInt(arrayOfString[1], 16);
/* 291 */
                    this.lock = new LockInfo(arrayOfString[0], i);
/*     */
                } else {
/* 293 */
                    assert (arrayOfString.length == 2);
/* 294 */
                    this.lock = null;
/*     */
                }
/*     */
            } else {
/* 297 */
                this.lock = null;
/*     */
            }
/* 299 */
            this.lockedMonitors = EMPTY_MONITORS;
/* 300 */
            this.lockedSynchronizers = EMPTY_SYNCS;
/*     */
        }
/*     */
    }

    /*     */
/*     */
    public long getThreadId()
/*     */ {
/* 310 */
        return this.threadId;
/*     */
    }

    /*     */
/*     */
    public String getThreadName()
/*     */ {
/* 319 */
        return this.threadName;
/*     */
    }

    /*     */
/*     */
    public Thread.State getThreadState()
/*     */ {
/* 328 */
        return this.threadState;
/*     */
    }

    /*     */
/*     */
    public long getBlockedTime()
/*     */ {
/* 357 */
        return this.blockedTime;
/*     */
    }

    /*     */
/*     */
    public long getBlockedCount()
/*     */ {
/* 371 */
        return this.blockedCount;
/*     */
    }

    /*     */
/*     */
    public long getWaitedTime()
/*     */ {
/* 402 */
        return this.waitedTime;
/*     */
    }

    /*     */
/*     */
    public long getWaitedCount()
/*     */ {
/* 417 */
        return this.waitedCount;
/*     */
    }

    /*     */
/*     */
    public LockInfo getLockInfo()
/*     */ {
/* 458 */
        return this.lock;
/*     */
    }

    /*     */
/*     */
    public String getLockName()
/*     */ {
/* 481 */
        return this.lockName;
/*     */
    }

    /*     */
/*     */
    public long getLockOwnerId()
/*     */ {
/* 499 */
        return this.lockOwnerId;
/*     */
    }

    /*     */
/*     */
    public String getLockOwnerName()
/*     */ {
/* 517 */
        return this.lockOwnerName;
/*     */
    }

    /*     */
/*     */
    public StackTraceElement[] getStackTrace()
/*     */ {
/* 540 */
        return this.stackTrace;
/*     */
    }

    /*     */
/*     */
    public boolean isSuspended()
/*     */ {
/* 552 */
        return this.suspended;
/*     */
    }

    /*     */
/*     */
    public boolean isInNative()
/*     */ {
/* 566 */
        return this.inNative;
/*     */
    }

    /*     */
/*     */
    public String toString()
/*     */ {
/* 581 */
        StringBuilder localStringBuilder = new StringBuilder("\"" + getThreadName() + "\"" + " Id=" + getThreadId() + " " + getThreadState());
/*     */ 
/* 584 */
        if (getLockName() != null) {
/* 585 */
            localStringBuilder.append(" on " + getLockName());
/*     */
        }
/* 587 */
        if (getLockOwnerName() != null) {
/* 588 */
            localStringBuilder.append(" owned by \"" + getLockOwnerName() + "\" Id=" + getLockOwnerId());
/*     */
        }
/*     */ 
/* 591 */
        if (isSuspended()) {
/* 592 */
            localStringBuilder.append(" (suspended)");
/*     */
        }
/* 594 */
        if (isInNative()) {
/* 595 */
            localStringBuilder.append(" (in native)");
/*     */
        }
/* 597 */
        localStringBuilder.append('\n');
/*     */
        Object localObject2;
/*     */
        Object localObject3;
/* 598 */
        for (int i = 0;
/* 599 */       (i < this.stackTrace.length) && (i < 8); i++) {
/* 600 */
            localObject1 = this.stackTrace[i];
/* 601 */
            localStringBuilder.append("\tat " + ((StackTraceElement) localObject1).toString());
/* 602 */
            localStringBuilder.append('\n');
/* 603 */
            if ((i == 0) && (getLockInfo() != null)) {
/* 604 */
                localObject2 = getThreadState();
/* 605 */
                switch (1. $SwitchMap$java$lang$Thread$State[localObject2.ordinal()]){
/*     */
                    case 1:
/* 607 */
                        localStringBuilder.append("\t-  blocked on " + getLockInfo());
/* 608 */
                        localStringBuilder.append('\n');
/* 609 */
                        break;
/*     */
                    case 2:
/* 611 */
                        localStringBuilder.append("\t-  waiting on " + getLockInfo());
/* 612 */
                        localStringBuilder.append('\n');
/* 613 */
                        break;
/*     */
                    case 3:
/* 615 */
                        localStringBuilder.append("\t-  waiting on " + getLockInfo());
/* 616 */
                        localStringBuilder.append('\n');
/* 617 */
                        break;
/*     */
                }
/*     */ 
/*     */
            }
/*     */ 
/* 622 */
            for (localObject3:
                 this.lockedMonitors) {
/* 623 */
                if (localObject3.getLockedStackDepth() == i) {
/* 624 */
                    localStringBuilder.append("\t-  locked " + localObject3);
/* 625 */
                    localStringBuilder.append('\n');
/*     */
                }
/*     */
            }
/*     */
        }
/* 629 */
        if (i < this.stackTrace.length) {
/* 630 */
            localStringBuilder.append("\t...");
/* 631 */
            localStringBuilder.append('\n');
/*     */
        }
/*     */ 
/* 634 */
        Object localObject1 = getLockedSynchronizers();
/* 635 */
        if (localObject1.length > 0) {
/* 636 */
            localStringBuilder.append("\n\tNumber of locked synchronizers = " + localObject1.length);
/* 637 */
            localStringBuilder.append('\n');
/* 638 */
            for (localObject3:
                 localObject1) {
/* 639 */
                localStringBuilder.append("\t- " + localObject3);
/* 640 */
                localStringBuilder.append('\n');
/*     */
            }
/*     */
        }
/* 643 */
        localStringBuilder.append('\n');
/* 644 */
        return localStringBuilder.toString();
/*     */
    }

    /*     */
/*     */
    public static ThreadInfo from(CompositeData paramCompositeData)
/*     */ {
/* 791 */
        if (paramCompositeData == null) {
/* 792 */
            return null;
/*     */
        }
/*     */ 
/* 795 */
        if ((paramCompositeData instanceof ThreadInfoCompositeData)) {
/* 796 */
            return ((ThreadInfoCompositeData) paramCompositeData).getThreadInfo();
/*     */
        }
/* 798 */
        return new ThreadInfo(paramCompositeData);
/*     */
    }

    /*     */
/*     */
    public MonitorInfo[] getLockedMonitors()
/*     */ {
/* 816 */
        return this.lockedMonitors;
/*     */
    }

    /*     */
/*     */
    public LockInfo[] getLockedSynchronizers()
/*     */ {
/* 834 */
        return this.lockedSynchronizers;
/*     */
    }
/*     */
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.lang.management.ThreadInfo
 * JD-Core Version:    0.6.2
 */