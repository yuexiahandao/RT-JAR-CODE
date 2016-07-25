/*    */ package sun.management;
/*    */ 
/*    */ import java.lang.management.OperatingSystemMXBean;
/*    */ import javax.management.ObjectName;
/*    */ import sun.misc.Unsafe;
/*    */ 
/*    */ public class OperatingSystemImpl
/*    */   implements OperatingSystemMXBean
/*    */ {
/*    */   private final VMManagement jvm;
/* 67 */   private static final Unsafe unsafe = Unsafe.getUnsafe();
/* 68 */   private double[] loadavg = new double[1];
/*    */ 
/*    */   protected OperatingSystemImpl(VMManagement paramVMManagement)
/*    */   {
/* 48 */     this.jvm = paramVMManagement;
/*    */   }
/*    */ 
/*    */   public String getName() {
/* 52 */     return this.jvm.getOsName();
/*    */   }
/*    */ 
/*    */   public String getArch() {
/* 56 */     return this.jvm.getOsArch();
/*    */   }
/*    */ 
/*    */   public String getVersion() {
/* 60 */     return this.jvm.getOsVersion();
/*    */   }
/*    */ 
/*    */   public int getAvailableProcessors() {
/* 64 */     return this.jvm.getAvailableProcessors();
/*    */   }
/*    */ 
/*    */   public double getSystemLoadAverage()
/*    */   {
/* 70 */     if (unsafe.getLoadAverage(this.loadavg, 1) == 1) {
/* 71 */       return this.loadavg[0];
/*    */     }
/* 73 */     return -1.0D;
/*    */   }
/*    */ 
/*    */   public ObjectName getObjectName() {
/* 77 */     return Util.newObjectName("java.lang:type=OperatingSystem");
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.management.OperatingSystemImpl
 * JD-Core Version:    0.6.2
 */