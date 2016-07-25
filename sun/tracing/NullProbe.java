/*    */ package sun.tracing;
/*    */ 
/*    */ class NullProbe extends ProbeSkeleton
/*    */ {
/*    */   public NullProbe(Class<?>[] paramArrayOfClass)
/*    */   {
/* 74 */     super(paramArrayOfClass);
/*    */   }
/*    */ 
/*    */   public boolean isEnabled() {
/* 78 */     return false;
/*    */   }
/*    */ 
/*    */   public void uncheckedTrigger(Object[] paramArrayOfObject)
/*    */   {
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.tracing.NullProbe
 * JD-Core Version:    0.6.2
 */