/*     */ package sun.management;
/*     */ 
/*     */ import com.sun.management.ThreadMXBean;
/*     */ import java.lang.management.ThreadInfo;
/*     */ import java.util.Arrays;
/*     */ import javax.management.ObjectName;
/*     */ 
/*     */ class ThreadImpl
/*     */   implements ThreadMXBean
/*     */ {
/*     */   private final VMManagement jvm;
/*  46 */   private boolean contentionMonitoringEnabled = false;
/*     */   private boolean cpuTimeEnabled;
/*     */   private boolean allocatedMemoryEnabled;
/*     */ 
/*     */   ThreadImpl(VMManagement paramVMManagement)
/*     */   {
/*  54 */     this.jvm = paramVMManagement;
/*  55 */     this.cpuTimeEnabled = this.jvm.isThreadCpuTimeEnabled();
/*  56 */     this.allocatedMemoryEnabled = this.jvm.isThreadAllocatedMemoryEnabled();
/*     */   }
/*     */ 
/*     */   public int getThreadCount() {
/*  60 */     return this.jvm.getLiveThreadCount();
/*     */   }
/*     */ 
/*     */   public int getPeakThreadCount() {
/*  64 */     return this.jvm.getPeakThreadCount();
/*     */   }
/*     */ 
/*     */   public long getTotalStartedThreadCount() {
/*  68 */     return this.jvm.getTotalThreadCount();
/*     */   }
/*     */ 
/*     */   public int getDaemonThreadCount() {
/*  72 */     return this.jvm.getDaemonThreadCount();
/*     */   }
/*     */ 
/*     */   public boolean isThreadContentionMonitoringSupported() {
/*  76 */     return this.jvm.isThreadContentionMonitoringSupported();
/*     */   }
/*     */ 
/*     */   public synchronized boolean isThreadContentionMonitoringEnabled() {
/*  80 */     if (!isThreadContentionMonitoringSupported()) {
/*  81 */       throw new UnsupportedOperationException("Thread contention monitoring is not supported.");
/*     */     }
/*     */ 
/*  84 */     return this.contentionMonitoringEnabled;
/*     */   }
/*     */ 
/*     */   public boolean isThreadCpuTimeSupported() {
/*  88 */     return this.jvm.isOtherThreadCpuTimeSupported();
/*     */   }
/*     */ 
/*     */   public boolean isCurrentThreadCpuTimeSupported() {
/*  92 */     return this.jvm.isCurrentThreadCpuTimeSupported();
/*     */   }
/*     */ 
/*     */   public boolean isThreadAllocatedMemorySupported() {
/*  96 */     return this.jvm.isThreadAllocatedMemorySupported();
/*     */   }
/*     */ 
/*     */   public boolean isThreadCpuTimeEnabled() {
/* 100 */     if ((!isThreadCpuTimeSupported()) && (!isCurrentThreadCpuTimeSupported()))
/*     */     {
/* 102 */       throw new UnsupportedOperationException("Thread CPU time measurement is not supported");
/*     */     }
/*     */ 
/* 105 */     return this.cpuTimeEnabled;
/*     */   }
/*     */ 
/*     */   public boolean isThreadAllocatedMemoryEnabled() {
/* 109 */     if (!isThreadAllocatedMemorySupported()) {
/* 110 */       throw new UnsupportedOperationException("Thread allocated memory measurement is not supported");
/*     */     }
/*     */ 
/* 113 */     return this.allocatedMemoryEnabled;
/*     */   }
/*     */ 
/*     */   public long[] getAllThreadIds() {
/* 117 */     Util.checkMonitorAccess();
/*     */ 
/* 119 */     Thread[] arrayOfThread = getThreads();
/* 120 */     int i = arrayOfThread.length;
/* 121 */     long[] arrayOfLong = new long[i];
/* 122 */     for (int j = 0; j < i; j++) {
/* 123 */       Thread localThread = arrayOfThread[j];
/* 124 */       arrayOfLong[j] = localThread.getId();
/*     */     }
/* 126 */     return arrayOfLong;
/*     */   }
/*     */ 
/*     */   public ThreadInfo getThreadInfo(long paramLong) {
/* 130 */     long[] arrayOfLong = new long[1];
/* 131 */     arrayOfLong[0] = paramLong;
/* 132 */     ThreadInfo[] arrayOfThreadInfo = getThreadInfo(arrayOfLong, 0);
/* 133 */     return arrayOfThreadInfo[0];
/*     */   }
/*     */ 
/*     */   public ThreadInfo getThreadInfo(long paramLong, int paramInt) {
/* 137 */     long[] arrayOfLong = new long[1];
/* 138 */     arrayOfLong[0] = paramLong;
/* 139 */     ThreadInfo[] arrayOfThreadInfo = getThreadInfo(arrayOfLong, paramInt);
/* 140 */     return arrayOfThreadInfo[0];
/*     */   }
/*     */ 
/*     */   public ThreadInfo[] getThreadInfo(long[] paramArrayOfLong) {
/* 144 */     return getThreadInfo(paramArrayOfLong, 0);
/*     */   }
/*     */ 
/*     */   private void verifyThreadIds(long[] paramArrayOfLong) {
/* 148 */     if (paramArrayOfLong == null) {
/* 149 */       throw new NullPointerException("Null ids parameter.");
/*     */     }
/*     */ 
/* 152 */     for (int i = 0; i < paramArrayOfLong.length; i++)
/* 153 */       if (paramArrayOfLong[i] <= 0L)
/* 154 */         throw new IllegalArgumentException("Invalid thread ID parameter: " + paramArrayOfLong[i]);
/*     */   }
/*     */ 
/*     */   public ThreadInfo[] getThreadInfo(long[] paramArrayOfLong, int paramInt)
/*     */   {
/* 161 */     verifyThreadIds(paramArrayOfLong);
/*     */ 
/* 163 */     if (paramInt < 0) {
/* 164 */       throw new IllegalArgumentException("Invalid maxDepth parameter: " + paramInt);
/*     */     }
/*     */ 
/* 168 */     Util.checkMonitorAccess();
/*     */ 
/* 170 */     ThreadInfo[] arrayOfThreadInfo = new ThreadInfo[paramArrayOfLong.length];
/* 171 */     if (paramInt == 2147483647)
/* 172 */       getThreadInfo1(paramArrayOfLong, -1, arrayOfThreadInfo);
/*     */     else {
/* 174 */       getThreadInfo1(paramArrayOfLong, paramInt, arrayOfThreadInfo);
/*     */     }
/* 176 */     return arrayOfThreadInfo;
/*     */   }
/*     */ 
/*     */   public void setThreadContentionMonitoringEnabled(boolean paramBoolean) {
/* 180 */     if (!isThreadContentionMonitoringSupported()) {
/* 181 */       throw new UnsupportedOperationException("Thread contention monitoring is not supported");
/*     */     }
/*     */ 
/* 185 */     Util.checkControlAccess();
/*     */ 
/* 187 */     synchronized (this) {
/* 188 */       if (this.contentionMonitoringEnabled != paramBoolean) {
/* 189 */         if (paramBoolean)
/*     */         {
/* 192 */           resetContentionTimes0(0L);
/*     */         }
/*     */ 
/* 196 */         setThreadContentionMonitoringEnabled0(paramBoolean);
/*     */ 
/* 198 */         this.contentionMonitoringEnabled = paramBoolean;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private boolean verifyCurrentThreadCpuTime()
/*     */   {
/* 205 */     if (!isCurrentThreadCpuTimeSupported()) {
/* 206 */       throw new UnsupportedOperationException("Current thread CPU time measurement is not supported.");
/*     */     }
/*     */ 
/* 209 */     return isThreadCpuTimeEnabled();
/*     */   }
/*     */ 
/*     */   public long getCurrentThreadCpuTime() {
/* 213 */     if (verifyCurrentThreadCpuTime()) {
/* 214 */       return getThreadTotalCpuTime0(0L);
/*     */     }
/* 216 */     return -1L;
/*     */   }
/*     */ 
/*     */   public long getThreadCpuTime(long paramLong) {
/* 220 */     long[] arrayOfLong1 = new long[1];
/* 221 */     arrayOfLong1[0] = paramLong;
/* 222 */     long[] arrayOfLong2 = getThreadCpuTime(arrayOfLong1);
/* 223 */     return arrayOfLong2[0];
/*     */   }
/*     */ 
/*     */   private boolean verifyThreadCpuTime(long[] paramArrayOfLong) {
/* 227 */     verifyThreadIds(paramArrayOfLong);
/*     */ 
/* 230 */     if ((!isThreadCpuTimeSupported()) && (!isCurrentThreadCpuTimeSupported()))
/*     */     {
/* 232 */       throw new UnsupportedOperationException("Thread CPU time measurement is not supported.");
/*     */     }
/*     */ 
/* 236 */     if (!isThreadCpuTimeSupported())
/*     */     {
/* 238 */       for (int i = 0; i < paramArrayOfLong.length; i++) {
/* 239 */         if (paramArrayOfLong[i] != Thread.currentThread().getId()) {
/* 240 */           throw new UnsupportedOperationException("Thread CPU time measurement is only supported for the current thread.");
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 247 */     return isThreadCpuTimeEnabled();
/*     */   }
/*     */ 
/*     */   public long[] getThreadCpuTime(long[] paramArrayOfLong) {
/* 251 */     boolean bool = verifyThreadCpuTime(paramArrayOfLong);
/*     */ 
/* 253 */     int i = paramArrayOfLong.length;
/* 254 */     long[] arrayOfLong = new long[i];
/* 255 */     Arrays.fill(arrayOfLong, -1L);
/*     */ 
/* 257 */     if (bool) {
/* 258 */       if (i == 1) {
/* 259 */         long l = paramArrayOfLong[0];
/* 260 */         if (l == Thread.currentThread().getId()) {
/* 261 */           l = 0L;
/*     */         }
/* 263 */         arrayOfLong[0] = getThreadTotalCpuTime0(l);
/*     */       } else {
/* 265 */         getThreadTotalCpuTime1(paramArrayOfLong, arrayOfLong);
/*     */       }
/*     */     }
/* 268 */     return arrayOfLong;
/*     */   }
/*     */ 
/*     */   public long getCurrentThreadUserTime() {
/* 272 */     if (verifyCurrentThreadCpuTime()) {
/* 273 */       return getThreadUserCpuTime0(0L);
/*     */     }
/* 275 */     return -1L;
/*     */   }
/*     */ 
/*     */   public long getThreadUserTime(long paramLong) {
/* 279 */     long[] arrayOfLong1 = new long[1];
/* 280 */     arrayOfLong1[0] = paramLong;
/* 281 */     long[] arrayOfLong2 = getThreadUserTime(arrayOfLong1);
/* 282 */     return arrayOfLong2[0];
/*     */   }
/*     */ 
/*     */   public long[] getThreadUserTime(long[] paramArrayOfLong) {
/* 286 */     boolean bool = verifyThreadCpuTime(paramArrayOfLong);
/*     */ 
/* 288 */     int i = paramArrayOfLong.length;
/* 289 */     long[] arrayOfLong = new long[i];
/* 290 */     Arrays.fill(arrayOfLong, -1L);
/*     */ 
/* 292 */     if (bool) {
/* 293 */       if (i == 1) {
/* 294 */         long l = paramArrayOfLong[0];
/* 295 */         if (l == Thread.currentThread().getId()) {
/* 296 */           l = 0L;
/*     */         }
/* 298 */         arrayOfLong[0] = getThreadUserCpuTime0(l);
/*     */       } else {
/* 300 */         getThreadUserCpuTime1(paramArrayOfLong, arrayOfLong);
/*     */       }
/*     */     }
/* 303 */     return arrayOfLong;
/*     */   }
/*     */ 
/*     */   public void setThreadCpuTimeEnabled(boolean paramBoolean) {
/* 307 */     if ((!isThreadCpuTimeSupported()) && (!isCurrentThreadCpuTimeSupported()))
/*     */     {
/* 309 */       throw new UnsupportedOperationException("Thread CPU time measurement is not supported");
/*     */     }
/*     */ 
/* 313 */     Util.checkControlAccess();
/* 314 */     synchronized (this) {
/* 315 */       if (this.cpuTimeEnabled != paramBoolean)
/*     */       {
/* 317 */         setThreadCpuTimeEnabled0(paramBoolean);
/* 318 */         this.cpuTimeEnabled = paramBoolean;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public long getThreadAllocatedBytes(long paramLong) {
/* 324 */     long[] arrayOfLong1 = new long[1];
/* 325 */     arrayOfLong1[0] = paramLong;
/* 326 */     long[] arrayOfLong2 = getThreadAllocatedBytes(arrayOfLong1);
/* 327 */     return arrayOfLong2[0];
/*     */   }
/*     */ 
/*     */   private boolean verifyThreadAllocatedMemory(long[] paramArrayOfLong) {
/* 331 */     verifyThreadIds(paramArrayOfLong);
/*     */ 
/* 334 */     if (!isThreadAllocatedMemorySupported()) {
/* 335 */       throw new UnsupportedOperationException("Thread allocated memory measurement is not supported.");
/*     */     }
/*     */ 
/* 339 */     return isThreadAllocatedMemoryEnabled();
/*     */   }
/*     */ 
/*     */   public long[] getThreadAllocatedBytes(long[] paramArrayOfLong) {
/* 343 */     boolean bool = verifyThreadAllocatedMemory(paramArrayOfLong);
/*     */ 
/* 345 */     long[] arrayOfLong = new long[paramArrayOfLong.length];
/* 346 */     Arrays.fill(arrayOfLong, -1L);
/*     */ 
/* 348 */     if (bool) {
/* 349 */       getThreadAllocatedMemory1(paramArrayOfLong, arrayOfLong);
/*     */     }
/* 351 */     return arrayOfLong;
/*     */   }
/*     */ 
/*     */   public void setThreadAllocatedMemoryEnabled(boolean paramBoolean) {
/* 355 */     if (!isThreadAllocatedMemorySupported()) {
/* 356 */       throw new UnsupportedOperationException("Thread allocated memory measurement is not supported.");
/*     */     }
/*     */ 
/* 360 */     Util.checkControlAccess();
/* 361 */     synchronized (this) {
/* 362 */       if (this.allocatedMemoryEnabled != paramBoolean)
/*     */       {
/* 364 */         setThreadAllocatedMemoryEnabled0(paramBoolean);
/* 365 */         this.allocatedMemoryEnabled = paramBoolean;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public long[] findMonitorDeadlockedThreads() {
/* 371 */     Util.checkMonitorAccess();
/*     */ 
/* 373 */     Thread[] arrayOfThread = findMonitorDeadlockedThreads0();
/* 374 */     if (arrayOfThread == null) {
/* 375 */       return null;
/*     */     }
/*     */ 
/* 378 */     long[] arrayOfLong = new long[arrayOfThread.length];
/* 379 */     for (int i = 0; i < arrayOfThread.length; i++) {
/* 380 */       Thread localThread = arrayOfThread[i];
/* 381 */       arrayOfLong[i] = localThread.getId();
/*     */     }
/* 383 */     return arrayOfLong;
/*     */   }
/*     */ 
/*     */   public long[] findDeadlockedThreads() {
/* 387 */     if (!isSynchronizerUsageSupported()) {
/* 388 */       throw new UnsupportedOperationException("Monitoring of Synchronizer Usage is not supported.");
/*     */     }
/*     */ 
/* 392 */     Util.checkMonitorAccess();
/*     */ 
/* 394 */     Thread[] arrayOfThread = findDeadlockedThreads0();
/* 395 */     if (arrayOfThread == null) {
/* 396 */       return null;
/*     */     }
/*     */ 
/* 399 */     long[] arrayOfLong = new long[arrayOfThread.length];
/* 400 */     for (int i = 0; i < arrayOfThread.length; i++) {
/* 401 */       Thread localThread = arrayOfThread[i];
/* 402 */       arrayOfLong[i] = localThread.getId();
/*     */     }
/* 404 */     return arrayOfLong;
/*     */   }
/*     */ 
/*     */   public void resetPeakThreadCount() {
/* 408 */     Util.checkControlAccess();
/* 409 */     resetPeakThreadCount0();
/*     */   }
/*     */ 
/*     */   public boolean isObjectMonitorUsageSupported() {
/* 413 */     return this.jvm.isObjectMonitorUsageSupported();
/*     */   }
/*     */ 
/*     */   public boolean isSynchronizerUsageSupported() {
/* 417 */     return this.jvm.isSynchronizerUsageSupported();
/*     */   }
/*     */ 
/*     */   private void verifyDumpThreads(boolean paramBoolean1, boolean paramBoolean2)
/*     */   {
/* 422 */     if ((paramBoolean1) && (!isObjectMonitorUsageSupported())) {
/* 423 */       throw new UnsupportedOperationException("Monitoring of Object Monitor Usage is not supported.");
/*     */     }
/*     */ 
/* 427 */     if ((paramBoolean2) && (!isSynchronizerUsageSupported())) {
/* 428 */       throw new UnsupportedOperationException("Monitoring of Synchronizer Usage is not supported.");
/*     */     }
/*     */ 
/* 432 */     Util.checkMonitorAccess();
/*     */   }
/*     */ 
/*     */   public ThreadInfo[] getThreadInfo(long[] paramArrayOfLong, boolean paramBoolean1, boolean paramBoolean2)
/*     */   {
/* 438 */     verifyThreadIds(paramArrayOfLong);
/* 439 */     verifyDumpThreads(paramBoolean1, paramBoolean2);
/* 440 */     return dumpThreads0(paramArrayOfLong, paramBoolean1, paramBoolean2);
/*     */   }
/*     */ 
/*     */   public ThreadInfo[] dumpAllThreads(boolean paramBoolean1, boolean paramBoolean2)
/*     */   {
/* 445 */     verifyDumpThreads(paramBoolean1, paramBoolean2);
/* 446 */     return dumpThreads0(null, paramBoolean1, paramBoolean2); } 
/*     */   private static native Thread[] getThreads();
/*     */ 
/*     */   private static native void getThreadInfo1(long[] paramArrayOfLong, int paramInt, ThreadInfo[] paramArrayOfThreadInfo);
/*     */ 
/*     */   private static native long getThreadTotalCpuTime0(long paramLong);
/*     */ 
/*     */   private static native void getThreadTotalCpuTime1(long[] paramArrayOfLong1, long[] paramArrayOfLong2);
/*     */ 
/*     */   private static native long getThreadUserCpuTime0(long paramLong);
/*     */ 
/*     */   private static native void getThreadUserCpuTime1(long[] paramArrayOfLong1, long[] paramArrayOfLong2);
/*     */ 
/*     */   private static native void getThreadAllocatedMemory1(long[] paramArrayOfLong1, long[] paramArrayOfLong2);
/*     */ 
/*     */   private static native void setThreadCpuTimeEnabled0(boolean paramBoolean);
/*     */ 
/*     */   private static native void setThreadAllocatedMemoryEnabled0(boolean paramBoolean);
/*     */ 
/*     */   private static native void setThreadContentionMonitoringEnabled0(boolean paramBoolean);
/*     */ 
/*     */   private static native Thread[] findMonitorDeadlockedThreads0();
/*     */ 
/*     */   private static native Thread[] findDeadlockedThreads0();
/*     */ 
/*     */   private static native void resetPeakThreadCount0();
/*     */ 
/*     */   private static native ThreadInfo[] dumpThreads0(long[] paramArrayOfLong, boolean paramBoolean1, boolean paramBoolean2);
/*     */ 
/*     */   private static native void resetContentionTimes0(long paramLong);
/*     */ 
/* 473 */   public ObjectName getObjectName() { return Util.newObjectName("java.lang:type=Threading"); }
/*     */ 
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.management.ThreadImpl
 * JD-Core Version:    0.6.2
 */