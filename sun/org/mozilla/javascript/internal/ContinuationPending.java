/*    */ package sun.org.mozilla.javascript.internal;
/*    */ 
/*    */ public class ContinuationPending extends RuntimeException
/*    */ {
/*    */   private static final long serialVersionUID = 4956008116771118856L;
/*    */   private NativeContinuation continuationState;
/*    */   private Object applicationState;
/*    */ 
/*    */   ContinuationPending(NativeContinuation paramNativeContinuation)
/*    */   {
/* 64 */     this.continuationState = paramNativeContinuation;
/*    */   }
/*    */ 
/*    */   public Object getContinuation()
/*    */   {
/* 74 */     return this.continuationState;
/*    */   }
/*    */ 
/*    */   NativeContinuation getContinuationState()
/*    */   {
/* 81 */     return this.continuationState;
/*    */   }
/*    */ 
/*    */   public void setApplicationState(Object paramObject)
/*    */   {
/* 90 */     this.applicationState = paramObject;
/*    */   }
/*    */ 
/*    */   public Object getApplicationState()
/*    */   {
/* 97 */     return this.applicationState;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.ContinuationPending
 * JD-Core Version:    0.6.2
 */