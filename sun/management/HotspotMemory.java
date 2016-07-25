/*    */ package sun.management;
/*    */ 
/*    */ import java.util.List;
/*    */ import sun.management.counter.Counter;
/*    */ 
/*    */ class HotspotMemory
/*    */   implements HotspotMemoryMBean
/*    */ {
/*    */   private VMManagement jvm;
/*    */   private static final String JAVA_GC = "java.gc.";
/*    */   private static final String COM_SUN_GC = "com.sun.gc.";
/*    */   private static final String SUN_GC = "sun.gc.";
/*    */   private static final String GC_COUNTER_NAME_PATTERN = "java.gc.|com.sun.gc.|sun.gc.";
/*    */ 
/*    */   HotspotMemory(VMManagement paramVMManagement)
/*    */   {
/* 47 */     this.jvm = paramVMManagement;
/*    */   }
/*    */ 
/*    */   public List<Counter> getInternalMemoryCounters()
/*    */   {
/* 58 */     return this.jvm.getInternalCounters("java.gc.|com.sun.gc.|sun.gc.");
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.management.HotspotMemory
 * JD-Core Version:    0.6.2
 */