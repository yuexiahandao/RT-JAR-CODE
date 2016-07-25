/*     */ package sun.java2d;
/*     */ 
/*     */ public abstract interface StateTrackable
/*     */ {
/*     */   public abstract State getState();
/*     */ 
/*     */   public abstract StateTracker getStateTracker();
/*     */ 
/*     */   public static enum State
/*     */   {
/* 165 */     IMMUTABLE, 
/*     */ 
/* 175 */     STABLE, 
/*     */ 
/* 189 */     DYNAMIC, 
/*     */ 
/* 198 */     UNTRACKABLE;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.java2d.StateTrackable
 * JD-Core Version:    0.6.2
 */