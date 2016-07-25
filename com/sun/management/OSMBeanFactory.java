/*    */ package com.sun.management;
/*    */ 
/*    */ import java.lang.management.OperatingSystemMXBean;
/*    */ import sun.management.VMManagement;
/*    */ 
/*    */ public class OSMBeanFactory
/*    */ {
/* 42 */   private static OperatingSystem osMBean = null;
/*    */ 
/*    */   public static synchronized OperatingSystemMXBean getOperatingSystemMXBean(VMManagement paramVMManagement)
/*    */   {
/* 47 */     if (osMBean == null) {
/* 48 */       osMBean = new OperatingSystem(paramVMManagement);
/*    */     }
/* 50 */     return osMBean;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.management.OSMBeanFactory
 * JD-Core Version:    0.6.2
 */