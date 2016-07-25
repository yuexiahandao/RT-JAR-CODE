/*    */ package sun.awt;
/*    */ 
/*    */ import java.awt.event.InvocationEvent;
/*    */ 
/*    */ public class PeerEvent extends InvocationEvent
/*    */ {
/*    */   public static final long PRIORITY_EVENT = 1L;
/*    */   public static final long ULTIMATE_PRIORITY_EVENT = 2L;
/*    */   public static final long LOW_PRIORITY_EVENT = 4L;
/*    */   private long flags;
/*    */ 
/*    */   public PeerEvent(Object paramObject, Runnable paramRunnable, long paramLong)
/*    */   {
/* 38 */     this(paramObject, paramRunnable, null, false, paramLong);
/*    */   }
/*    */ 
/*    */   public PeerEvent(Object paramObject1, Runnable paramRunnable, Object paramObject2, boolean paramBoolean, long paramLong)
/*    */   {
/* 43 */     super(paramObject1, paramRunnable, paramObject2, paramBoolean);
/* 44 */     this.flags = paramLong;
/*    */   }
/*    */ 
/*    */   public long getFlags() {
/* 48 */     return this.flags;
/*    */   }
/*    */ 
/*    */   public PeerEvent coalesceEvents(PeerEvent paramPeerEvent) {
/* 52 */     return null;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.PeerEvent
 * JD-Core Version:    0.6.2
 */