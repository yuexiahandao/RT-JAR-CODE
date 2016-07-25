/*    */ package sun.management;
/*    */ 
/*    */ import java.lang.management.ClassLoadingMXBean;
/*    */ import javax.management.ObjectName;
/*    */ 
/*    */ class ClassLoadingImpl
/*    */   implements ClassLoadingMXBean
/*    */ {
/*    */   private final VMManagement jvm;
/*    */ 
/*    */   ClassLoadingImpl(VMManagement paramVMManagement)
/*    */   {
/* 47 */     this.jvm = paramVMManagement;
/*    */   }
/*    */ 
/*    */   public long getTotalLoadedClassCount() {
/* 51 */     return this.jvm.getTotalClassCount();
/*    */   }
/*    */ 
/*    */   public int getLoadedClassCount() {
/* 55 */     return this.jvm.getLoadedClassCount();
/*    */   }
/*    */ 
/*    */   public long getUnloadedClassCount() {
/* 59 */     return this.jvm.getUnloadedClassCount();
/*    */   }
/*    */ 
/*    */   public boolean isVerbose() {
/* 63 */     return this.jvm.getVerboseClass();
/*    */   }
/*    */ 
/*    */   public void setVerbose(boolean paramBoolean) {
/* 67 */     Util.checkControlAccess();
/*    */ 
/* 69 */     setVerboseClass(paramBoolean);
/*    */   }
/*    */   static native void setVerboseClass(boolean paramBoolean);
/*    */ 
/*    */   public ObjectName getObjectName() {
/* 74 */     return Util.newObjectName("java.lang:type=ClassLoading");
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.management.ClassLoadingImpl
 * JD-Core Version:    0.6.2
 */