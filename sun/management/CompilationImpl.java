/*    */ package sun.management;
/*    */ 
/*    */ import java.lang.management.CompilationMXBean;
/*    */ import javax.management.ObjectName;
/*    */ 
/*    */ class CompilationImpl
/*    */   implements CompilationMXBean
/*    */ {
/*    */   private final VMManagement jvm;
/*    */   private final String name;
/*    */ 
/*    */   CompilationImpl(VMManagement paramVMManagement)
/*    */   {
/* 48 */     this.jvm = paramVMManagement;
/* 49 */     this.name = this.jvm.getCompilerName();
/* 50 */     if (this.name == null)
/* 51 */       throw new AssertionError("Null compiler name");
/*    */   }
/*    */ 
/*    */   public String getName()
/*    */   {
/* 56 */     return this.name;
/*    */   }
/*    */ 
/*    */   public boolean isCompilationTimeMonitoringSupported() {
/* 60 */     return this.jvm.isCompilationTimeMonitoringSupported();
/*    */   }
/*    */ 
/*    */   public long getTotalCompilationTime() {
/* 64 */     if (!isCompilationTimeMonitoringSupported()) {
/* 65 */       throw new UnsupportedOperationException("Compilation time monitoring is not supported.");
/*    */     }
/*    */ 
/* 69 */     return this.jvm.getTotalCompileTime();
/*    */   }
/*    */ 
/*    */   public ObjectName getObjectName() {
/* 73 */     return Util.newObjectName("java.lang:type=Compilation");
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.management.CompilationImpl
 * JD-Core Version:    0.6.2
 */