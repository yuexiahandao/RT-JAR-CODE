/*    */ package com.sun.management;
/*    */ 
/*    */ import sun.management.OperatingSystemImpl;
/*    */ import sun.management.VMManagement;
/*    */ 
/*    */ class OperatingSystem extends OperatingSystemImpl
/*    */   implements OperatingSystemMXBean
/*    */ {
/* 43 */   private static Object psapiLock = new Object();
/*    */ 
/*    */   OperatingSystem(VMManagement paramVMManagement) {
/* 46 */     super(paramVMManagement);
/*    */   }
/*    */ 
/*    */   public long getCommittedVirtualMemorySize() {
/* 50 */     synchronized (psapiLock) {
/* 51 */       return getCommittedVirtualMemorySize0(); }  } 
/*    */   private native long getCommittedVirtualMemorySize0();
/*    */ 
/*    */   public native long getTotalSwapSpaceSize();
/*    */ 
/*    */   public native long getFreeSwapSpaceSize();
/*    */ 
/*    */   public native long getProcessCpuTime();
/*    */ 
/*    */   public native long getFreePhysicalMemorySize();
/*    */ 
/*    */   public native long getTotalPhysicalMemorySize();
/*    */ 
/*    */   public native double getSystemCpuLoad();
/*    */ 
/*    */   public native double getProcessCpuLoad();
/*    */ 
/*    */   private static native void initialize();
/*    */ 
/* 65 */   static { initialize(); }
/*    */ 
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.management.OperatingSystem
 * JD-Core Version:    0.6.2
 */