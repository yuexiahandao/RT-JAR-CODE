/*    */ package sun.management;
/*    */ 
/*    */ import java.util.List;
/*    */ import sun.management.counter.Counter;
/*    */ 
/*    */ class HotspotRuntime
/*    */   implements HotspotRuntimeMBean
/*    */ {
/*    */   private VMManagement jvm;
/*    */   private static final String JAVA_RT = "java.rt.";
/*    */   private static final String COM_SUN_RT = "com.sun.rt.";
/*    */   private static final String SUN_RT = "sun.rt.";
/*    */   private static final String JAVA_PROPERTY = "java.property.";
/*    */   private static final String COM_SUN_PROPERTY = "com.sun.property.";
/*    */   private static final String SUN_PROPERTY = "sun.property.";
/*    */   private static final String RT_COUNTER_NAME_PATTERN = "java.rt.|com.sun.rt.|sun.rt.|java.property.|com.sun.property.|sun.property.";
/*    */ 
/*    */   HotspotRuntime(VMManagement paramVMManagement)
/*    */   {
/* 48 */     this.jvm = paramVMManagement;
/*    */   }
/*    */ 
/*    */   public long getSafepointCount() {
/* 52 */     return this.jvm.getSafepointCount();
/*    */   }
/*    */ 
/*    */   public long getTotalSafepointTime() {
/* 56 */     return this.jvm.getTotalSafepointTime();
/*    */   }
/*    */ 
/*    */   public long getSafepointSyncTime() {
/* 60 */     return this.jvm.getSafepointSyncTime();
/*    */   }
/*    */ 
/*    */   public List<Counter> getInternalRuntimeCounters()
/*    */   {
/* 75 */     return this.jvm.getInternalCounters("java.rt.|com.sun.rt.|sun.rt.|java.property.|com.sun.property.|sun.property.");
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.management.HotspotRuntime
 * JD-Core Version:    0.6.2
 */