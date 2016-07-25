/*    */ package sun.management;
/*    */ 
/*    */ import java.util.Collections;
/*    */ import java.util.HashMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import sun.management.counter.Counter;
/*    */ 
/*    */ class HotspotThread
/*    */   implements HotspotThreadMBean
/*    */ {
/*    */   private VMManagement jvm;
/*    */   private static final String JAVA_THREADS = "java.threads.";
/*    */   private static final String COM_SUN_THREADS = "com.sun.threads.";
/*    */   private static final String SUN_THREADS = "sun.threads.";
/*    */   private static final String THREADS_COUNTER_NAME_PATTERN = "java.threads.|com.sun.threads.|sun.threads.";
/*    */ 
/*    */   HotspotThread(VMManagement paramVMManagement)
/*    */   {
/* 48 */     this.jvm = paramVMManagement;
/*    */   }
/*    */ 
/*    */   public native int getInternalThreadCount();
/*    */ 
/*    */   public Map<String, Long> getInternalThreadCpuTimes() {
/* 54 */     int i = getInternalThreadCount();
/* 55 */     if (i == 0) {
/* 56 */       return Collections.emptyMap();
/*    */     }
/* 58 */     String[] arrayOfString = new String[i];
/* 59 */     long[] arrayOfLong = new long[i];
/* 60 */     int j = getInternalThreadTimes0(arrayOfString, arrayOfLong);
/* 61 */     HashMap localHashMap = new HashMap(j);
/* 62 */     for (int k = 0; k < j; k++) {
/* 63 */       localHashMap.put(arrayOfString[k], new Long(arrayOfLong[k]));
/*    */     }
/* 65 */     return localHashMap;
/*    */   }
/*    */ 
/*    */   public native int getInternalThreadTimes0(String[] paramArrayOfString, long[] paramArrayOfLong);
/*    */ 
/*    */   public List<Counter> getInternalThreadingCounters()
/*    */   {
/* 77 */     return this.jvm.getInternalCounters("java.threads.|com.sun.threads.|sun.threads.");
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.management.HotspotThread
 * JD-Core Version:    0.6.2
 */