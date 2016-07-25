/*    */ package sun.management;
/*    */ 
/*    */ import java.lang.management.GarbageCollectorMXBean;
/*    */ import java.lang.management.MemoryManagerMXBean;
/*    */ import java.lang.management.MemoryPoolMXBean;
/*    */ 
/*    */ class ManagementFactory
/*    */ {
/*    */   private static MemoryPoolMXBean createMemoryPool(String paramString, boolean paramBoolean, long paramLong1, long paramLong2)
/*    */   {
/* 42 */     return new MemoryPoolImpl(paramString, paramBoolean, paramLong1, paramLong2);
/*    */   }
/*    */ 
/*    */   private static MemoryManagerMXBean createMemoryManager(String paramString) {
/* 46 */     return new MemoryManagerImpl(paramString);
/*    */   }
/*    */ 
/*    */   private static GarbageCollectorMXBean createGarbageCollector(String paramString1, String paramString2)
/*    */   {
/* 53 */     return new GarbageCollectorImpl(paramString1);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.management.ManagementFactory
 * JD-Core Version:    0.6.2
 */