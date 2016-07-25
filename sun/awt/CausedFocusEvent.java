/*    */ package sun.awt;
/*    */ 
/*    */ import java.awt.Component;
/*    */ import java.awt.event.FocusEvent;
/*    */ 
/*    */ public class CausedFocusEvent extends FocusEvent
/*    */ {
/*    */   private final Cause cause;
/*    */ 
/*    */   public Cause getCause()
/*    */   {
/* 59 */     return this.cause;
/*    */   }
/*    */ 
/*    */   public String toString() {
/* 63 */     return "java.awt.FocusEvent[" + super.paramString() + ",cause=" + this.cause + "] on " + getSource();
/*    */   }
/*    */ 
/*    */   public CausedFocusEvent(Component paramComponent1, int paramInt, boolean paramBoolean, Component paramComponent2, Cause paramCause)
/*    */   {
/* 68 */     super(paramComponent1, paramInt, paramBoolean, paramComponent2);
/* 69 */     if (paramCause == null) {
/* 70 */       paramCause = Cause.UNKNOWN;
/*    */     }
/* 72 */     this.cause = paramCause;
/*    */   }
/*    */ 
/*    */   public static FocusEvent retarget(FocusEvent paramFocusEvent, Component paramComponent)
/*    */   {
/* 83 */     if (paramFocusEvent == null) return null;
/*    */ 
/* 85 */     return new CausedFocusEvent(paramComponent, paramFocusEvent.getID(), paramFocusEvent.isTemporary(), paramFocusEvent.getOppositeComponent(), (paramFocusEvent instanceof CausedFocusEvent) ? ((CausedFocusEvent)paramFocusEvent).getCause() : Cause.RETARGETED);
/*    */   }
/*    */ 
/*    */   public static enum Cause
/*    */   {
/* 40 */     UNKNOWN, 
/* 41 */     MOUSE_EVENT, 
/* 42 */     TRAVERSAL, 
/* 43 */     TRAVERSAL_UP, 
/* 44 */     TRAVERSAL_DOWN, 
/* 45 */     TRAVERSAL_FORWARD, 
/* 46 */     TRAVERSAL_BACKWARD, 
/* 47 */     MANUAL_REQUEST, 
/* 48 */     AUTOMATIC_TRAVERSE, 
/* 49 */     ROLLBACK, 
/* 50 */     NATIVE_SYSTEM, 
/* 51 */     ACTIVATION, 
/* 52 */     CLEAR_GLOBAL_FOCUS_OWNER, 
/* 53 */     RETARGETED;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.CausedFocusEvent
 * JD-Core Version:    0.6.2
 */