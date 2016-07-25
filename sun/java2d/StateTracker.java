/*    */ package sun.java2d;
/*    */ 
/*    */ public abstract interface StateTracker
/*    */ {
/* 72 */   public static final StateTracker ALWAYS_CURRENT = new StateTracker() {
/*    */     public boolean isCurrent() {
/* 74 */       return true;
/*    */     }
/* 72 */   };
/*    */ 
/* 89 */   public static final StateTracker NEVER_CURRENT = new StateTracker() {
/*    */     public boolean isCurrent() {
/* 91 */       return false;
/*    */     }
/* 89 */   };
/*    */ 
/*    */   public abstract boolean isCurrent();
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.java2d.StateTracker
 * JD-Core Version:    0.6.2
 */