/*     */ package sun.management;
/*     */ 
/*     */ import java.lang.management.LockInfo;
/*     */ import java.lang.management.MonitorInfo;
/*     */ import java.lang.management.ThreadInfo;
/*     */ import java.util.Set;
/*     */ import javax.management.openmbean.CompositeData;
/*     */ import javax.management.openmbean.CompositeDataSupport;
/*     */ import javax.management.openmbean.CompositeType;
/*     */ import javax.management.openmbean.OpenDataException;
/*     */ import javax.management.openmbean.OpenType;
/*     */ 
/*     */ public class ThreadInfoCompositeData extends LazyCompositeData
/*     */ {
/*     */   private final ThreadInfo threadInfo;
/*     */   private final CompositeData cdata;
/*     */   private final boolean currentVersion;
/*     */   private static final String THREAD_ID = "threadId";
/*     */   private static final String THREAD_NAME = "threadName";
/*     */   private static final String THREAD_STATE = "threadState";
/*     */   private static final String BLOCKED_TIME = "blockedTime";
/*     */   private static final String BLOCKED_COUNT = "blockedCount";
/*     */   private static final String WAITED_TIME = "waitedTime";
/*     */   private static final String WAITED_COUNT = "waitedCount";
/*     */   private static final String LOCK_INFO = "lockInfo";
/*     */   private static final String LOCK_NAME = "lockName";
/*     */   private static final String LOCK_OWNER_ID = "lockOwnerId";
/*     */   private static final String LOCK_OWNER_NAME = "lockOwnerName";
/*     */   private static final String STACK_TRACE = "stackTrace";
/*     */   private static final String SUSPENDED = "suspended";
/*     */   private static final String IN_NATIVE = "inNative";
/*     */   private static final String LOCKED_MONITORS = "lockedMonitors";
/*     */   private static final String LOCKED_SYNCS = "lockedSynchronizers";
/* 151 */   private static final String[] threadInfoItemNames = { "threadId", "threadName", "threadState", "blockedTime", "blockedCount", "waitedTime", "waitedCount", "lockInfo", "lockName", "lockOwnerId", "lockOwnerName", "stackTrace", "suspended", "inNative", "lockedMonitors", "lockedSynchronizers" };
/*     */ 
/* 171 */   private static final String[] threadInfoV6Attributes = { "lockInfo", "lockedMonitors", "lockedSynchronizers" };
/*     */   private static final CompositeType threadInfoCompositeType;
/*     */   private static final CompositeType threadInfoV5CompositeType;
/* 224 */   private static final CompositeType lockInfoCompositeType = ((CompositeData)localObject2).getCompositeType();
/*     */   private static final long serialVersionUID = 2464378539119753175L;
/*     */ 
/*     */   private ThreadInfoCompositeData(ThreadInfo paramThreadInfo)
/*     */   {
/*  48 */     this.threadInfo = paramThreadInfo;
/*  49 */     this.currentVersion = true;
/*  50 */     this.cdata = null;
/*     */   }
/*     */ 
/*     */   private ThreadInfoCompositeData(CompositeData paramCompositeData) {
/*  54 */     this.threadInfo = null;
/*  55 */     this.currentVersion = isCurrentVersion(paramCompositeData);
/*  56 */     this.cdata = paramCompositeData;
/*     */   }
/*     */ 
/*     */   public ThreadInfo getThreadInfo() {
/*  60 */     return this.threadInfo;
/*     */   }
/*     */ 
/*     */   public boolean isCurrentVersion() {
/*  64 */     return this.currentVersion;
/*     */   }
/*     */ 
/*     */   public static ThreadInfoCompositeData getInstance(CompositeData paramCompositeData) {
/*  68 */     validateCompositeData(paramCompositeData);
/*  69 */     return new ThreadInfoCompositeData(paramCompositeData);
/*     */   }
/*     */ 
/*     */   public static CompositeData toCompositeData(ThreadInfo paramThreadInfo) {
/*  73 */     ThreadInfoCompositeData localThreadInfoCompositeData = new ThreadInfoCompositeData(paramThreadInfo);
/*  74 */     return localThreadInfoCompositeData.getCompositeData();
/*     */   }
/*     */ 
/*     */   protected CompositeData getCompositeData()
/*     */   {
/*  79 */     StackTraceElement[] arrayOfStackTraceElement = this.threadInfo.getStackTrace();
/*  80 */     CompositeData[] arrayOfCompositeData1 = new CompositeData[arrayOfStackTraceElement.length];
/*     */ 
/*  82 */     for (int i = 0; i < arrayOfStackTraceElement.length; i++) {
/*  83 */       localObject = arrayOfStackTraceElement[i];
/*  84 */       arrayOfCompositeData1[i] = StackTraceElementCompositeData.toCompositeData((StackTraceElement)localObject);
/*     */     }
/*     */ 
/*  88 */     LockDataConverter localLockDataConverter = LockDataConverter.newLockDataConverter(this.threadInfo);
/*  89 */     Object localObject = localLockDataConverter.toLockInfoCompositeData();
/*  90 */     CompositeData[] arrayOfCompositeData2 = localLockDataConverter.toLockedSynchronizersCompositeData();
/*     */ 
/*  93 */     MonitorInfo[] arrayOfMonitorInfo = this.threadInfo.getLockedMonitors();
/*  94 */     CompositeData[] arrayOfCompositeData3 = new CompositeData[arrayOfMonitorInfo.length];
/*     */ 
/*  96 */     for (int j = 0; j < arrayOfMonitorInfo.length; j++) {
/*  97 */       MonitorInfo localMonitorInfo = arrayOfMonitorInfo[j];
/*  98 */       arrayOfCompositeData3[j] = MonitorInfoCompositeData.toCompositeData(localMonitorInfo);
/*     */     }
/*     */ 
/* 104 */     Object[] arrayOfObject = { new Long(this.threadInfo.getThreadId()), this.threadInfo.getThreadName(), this.threadInfo.getThreadState().name(), new Long(this.threadInfo.getBlockedTime()), new Long(this.threadInfo.getBlockedCount()), new Long(this.threadInfo.getWaitedTime()), new Long(this.threadInfo.getWaitedCount()), localObject, this.threadInfo.getLockName(), new Long(this.threadInfo.getLockOwnerId()), this.threadInfo.getLockOwnerName(), arrayOfCompositeData1, new Boolean(this.threadInfo.isSuspended()), new Boolean(this.threadInfo.isInNative()), arrayOfCompositeData3, arrayOfCompositeData2 };
/*     */     try
/*     */     {
/* 124 */       return new CompositeDataSupport(threadInfoCompositeType, threadInfoItemNames, arrayOfObject);
/*     */     }
/*     */     catch (OpenDataException localOpenDataException)
/*     */     {
/* 129 */       throw new AssertionError(localOpenDataException);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static boolean isV5Attribute(String paramString)
/*     */   {
/* 228 */     for (String str : threadInfoV6Attributes) {
/* 229 */       if (paramString.equals(str)) {
/* 230 */         return false;
/*     */       }
/*     */     }
/* 233 */     return true;
/*     */   }
/*     */ 
/*     */   public static boolean isCurrentVersion(CompositeData paramCompositeData) {
/* 237 */     if (paramCompositeData == null) {
/* 238 */       throw new NullPointerException("Null CompositeData");
/*     */     }
/*     */ 
/* 241 */     return isTypeMatched(threadInfoCompositeType, paramCompositeData.getCompositeType());
/*     */   }
/*     */ 
/*     */   public long threadId() {
/* 245 */     return getLong(this.cdata, "threadId");
/*     */   }
/*     */ 
/*     */   public String threadName()
/*     */   {
/* 251 */     String str = getString(this.cdata, "threadName");
/* 252 */     if (str == null) {
/* 253 */       throw new IllegalArgumentException("Invalid composite data: Attribute threadName has null value");
/*     */     }
/*     */ 
/* 256 */     return str;
/*     */   }
/*     */ 
/*     */   public Thread.State threadState() {
/* 260 */     return Thread.State.valueOf(getString(this.cdata, "threadState"));
/*     */   }
/*     */ 
/*     */   public long blockedTime() {
/* 264 */     return getLong(this.cdata, "blockedTime");
/*     */   }
/*     */ 
/*     */   public long blockedCount() {
/* 268 */     return getLong(this.cdata, "blockedCount");
/*     */   }
/*     */ 
/*     */   public long waitedTime() {
/* 272 */     return getLong(this.cdata, "waitedTime");
/*     */   }
/*     */ 
/*     */   public long waitedCount() {
/* 276 */     return getLong(this.cdata, "waitedCount");
/*     */   }
/*     */ 
/*     */   public String lockName()
/*     */   {
/* 282 */     return getString(this.cdata, "lockName");
/*     */   }
/*     */ 
/*     */   public long lockOwnerId() {
/* 286 */     return getLong(this.cdata, "lockOwnerId");
/*     */   }
/*     */ 
/*     */   public String lockOwnerName() {
/* 290 */     return getString(this.cdata, "lockOwnerName");
/*     */   }
/*     */ 
/*     */   public boolean suspended() {
/* 294 */     return getBoolean(this.cdata, "suspended");
/*     */   }
/*     */ 
/*     */   public boolean inNative() {
/* 298 */     return getBoolean(this.cdata, "inNative");
/*     */   }
/*     */ 
/*     */   public StackTraceElement[] stackTrace() {
/* 302 */     CompositeData[] arrayOfCompositeData = (CompositeData[])this.cdata.get("stackTrace");
/*     */ 
/* 307 */     StackTraceElement[] arrayOfStackTraceElement = new StackTraceElement[arrayOfCompositeData.length];
/*     */ 
/* 309 */     for (int i = 0; i < arrayOfCompositeData.length; i++) {
/* 310 */       CompositeData localCompositeData = arrayOfCompositeData[i];
/* 311 */       arrayOfStackTraceElement[i] = StackTraceElementCompositeData.from(localCompositeData);
/*     */     }
/* 313 */     return arrayOfStackTraceElement;
/*     */   }
/*     */ 
/*     */   public LockInfo lockInfo()
/*     */   {
/* 318 */     LockDataConverter localLockDataConverter = LockDataConverter.newLockDataConverter();
/* 319 */     CompositeData localCompositeData = (CompositeData)this.cdata.get("lockInfo");
/* 320 */     return localLockDataConverter.toLockInfo(localCompositeData);
/*     */   }
/*     */ 
/*     */   public MonitorInfo[] lockedMonitors() {
/* 324 */     CompositeData[] arrayOfCompositeData = (CompositeData[])this.cdata.get("lockedMonitors");
/*     */ 
/* 329 */     MonitorInfo[] arrayOfMonitorInfo = new MonitorInfo[arrayOfCompositeData.length];
/*     */ 
/* 331 */     for (int i = 0; i < arrayOfCompositeData.length; i++) {
/* 332 */       CompositeData localCompositeData = arrayOfCompositeData[i];
/* 333 */       arrayOfMonitorInfo[i] = MonitorInfo.from(localCompositeData);
/*     */     }
/* 335 */     return arrayOfMonitorInfo;
/*     */   }
/*     */ 
/*     */   public LockInfo[] lockedSynchronizers() {
/* 339 */     LockDataConverter localLockDataConverter = LockDataConverter.newLockDataConverter();
/* 340 */     CompositeData[] arrayOfCompositeData = (CompositeData[])this.cdata.get("lockedSynchronizers");
/*     */ 
/* 345 */     return localLockDataConverter.toLockedSynchronizers(arrayOfCompositeData);
/*     */   }
/*     */ 
/*     */   public static void validateCompositeData(CompositeData paramCompositeData)
/*     */   {
/* 353 */     if (paramCompositeData == null) {
/* 354 */       throw new NullPointerException("Null CompositeData");
/*     */     }
/*     */ 
/* 357 */     CompositeType localCompositeType = paramCompositeData.getCompositeType();
/* 358 */     int i = 1;
/* 359 */     if (!isTypeMatched(threadInfoCompositeType, localCompositeType)) {
/* 360 */       i = 0;
/*     */ 
/* 362 */       if (!isTypeMatched(threadInfoV5CompositeType, localCompositeType)) {
/* 363 */         throw new IllegalArgumentException("Unexpected composite type for ThreadInfo");
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 368 */     CompositeData[] arrayOfCompositeData1 = (CompositeData[])paramCompositeData.get("stackTrace");
/*     */ 
/* 370 */     if (arrayOfCompositeData1 == null) {
/* 371 */       throw new IllegalArgumentException("StackTraceElement[] is missing");
/*     */     }
/*     */ 
/* 374 */     if (arrayOfCompositeData1.length > 0) {
/* 375 */       StackTraceElementCompositeData.validateCompositeData(arrayOfCompositeData1[0]);
/*     */     }
/*     */ 
/* 379 */     if (i != 0) {
/* 380 */       CompositeData localCompositeData = (CompositeData)paramCompositeData.get("lockInfo");
/* 381 */       if ((localCompositeData != null) && 
/* 382 */         (!isTypeMatched(lockInfoCompositeType, localCompositeData.getCompositeType())))
/*     */       {
/* 384 */         throw new IllegalArgumentException("Unexpected composite type for \"lockInfo\" attribute.");
/*     */       }
/*     */ 
/* 390 */       CompositeData[] arrayOfCompositeData2 = (CompositeData[])paramCompositeData.get("lockedMonitors");
/* 391 */       if (arrayOfCompositeData2 == null) {
/* 392 */         throw new IllegalArgumentException("MonitorInfo[] is null");
/*     */       }
/* 394 */       if (arrayOfCompositeData2.length > 0) {
/* 395 */         MonitorInfoCompositeData.validateCompositeData(arrayOfCompositeData2[0]);
/*     */       }
/*     */ 
/* 398 */       CompositeData[] arrayOfCompositeData3 = (CompositeData[])paramCompositeData.get("lockedSynchronizers");
/* 399 */       if (arrayOfCompositeData3 == null) {
/* 400 */         throw new IllegalArgumentException("LockInfo[] is null");
/*     */       }
/* 402 */       if ((arrayOfCompositeData3.length > 0) && 
/* 403 */         (!isTypeMatched(lockInfoCompositeType, arrayOfCompositeData3[0].getCompositeType())))
/*     */       {
/* 405 */         throw new IllegalArgumentException("Unexpected composite type for \"lockedSynchronizers\" attribute.");
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*     */     try
/*     */     {
/* 184 */       threadInfoCompositeType = (CompositeType)MappedMXBeanType.toOpenType(ThreadInfo.class);
/*     */ 
/* 187 */       String[] arrayOfString1 = (String[])threadInfoCompositeType.keySet().toArray(new String[0]);
/*     */ 
/* 189 */       int i = threadInfoItemNames.length - threadInfoV6Attributes.length;
/*     */ 
/* 191 */       localObject2 = new String[i];
/* 192 */       String[] arrayOfString2 = new String[i];
/* 193 */       OpenType[] arrayOfOpenType = new OpenType[i];
/* 194 */       int j = 0;
/* 195 */       for (String str : arrayOfString1) {
/* 196 */         if (isV5Attribute(str)) {
/* 197 */           localObject2[j] = str;
/* 198 */           arrayOfString2[j] = threadInfoCompositeType.getDescription(str);
/* 199 */           arrayOfOpenType[j] = threadInfoCompositeType.getType(str);
/* 200 */           j++;
/*     */         }
/*     */       }
/*     */ 
/* 204 */       threadInfoV5CompositeType = new CompositeType("java.lang.management.ThreadInfo", "J2SE 5.0 java.lang.management.ThreadInfo", (String[])localObject2, arrayOfString2, arrayOfOpenType);
/*     */     }
/*     */     catch (OpenDataException localOpenDataException)
/*     */     {
/* 212 */       throw new AssertionError(localOpenDataException);
/*     */     }
/*     */ 
/* 220 */     Object localObject1 = new Object();
/* 221 */     LockInfo localLockInfo = new LockInfo(localObject1.getClass().getName(), System.identityHashCode(localObject1));
/*     */ 
/* 223 */     Object localObject2 = LockDataConverter.toLockInfoCompositeData(localLockInfo);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.management.ThreadInfoCompositeData
 * JD-Core Version:    0.6.2
 */