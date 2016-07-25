/*    */ package sun.management;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ 
/*    */ public class CompilerThreadStat
/*    */   implements Serializable
/*    */ {
/*    */   private String name;
/*    */   private long taskCount;
/*    */   private long compileTime;
/*    */   private MethodInfo lastMethod;
/*    */   private static final long serialVersionUID = 6992337162326171013L;
/*    */ 
/*    */   CompilerThreadStat(String paramString, long paramLong1, long paramLong2, MethodInfo paramMethodInfo)
/*    */   {
/* 37 */     this.name = paramString;
/* 38 */     this.taskCount = paramLong1;
/* 39 */     this.compileTime = paramLong2;
/* 40 */     this.lastMethod = paramMethodInfo;
/*    */   }
/*    */ 
/*    */   public String getName()
/*    */   {
/* 50 */     return this.name;
/*    */   }
/*    */ 
/*    */   public long getCompileTaskCount()
/*    */   {
/* 60 */     return this.taskCount;
/*    */   }
/*    */ 
/*    */   public long getCompileTime()
/*    */   {
/* 70 */     return this.compileTime;
/*    */   }
/*    */ 
/*    */   public MethodInfo getLastCompiledMethodInfo()
/*    */   {
/* 81 */     return this.lastMethod;
/*    */   }
/*    */ 
/*    */   public String toString() {
/* 85 */     return getName() + " compileTasks = " + getCompileTaskCount() + " compileTime = " + getCompileTime();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.management.CompilerThreadStat
 * JD-Core Version:    0.6.2
 */