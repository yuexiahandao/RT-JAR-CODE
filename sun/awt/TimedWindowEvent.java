/*    */ package sun.awt;
/*    */ 
/*    */ import java.awt.Window;
/*    */ import java.awt.event.WindowEvent;
/*    */ 
/*    */ public class TimedWindowEvent extends WindowEvent
/*    */ {
/*    */   private long time;
/*    */ 
/*    */   public long getWhen()
/*    */   {
/* 36 */     return this.time;
/*    */   }
/*    */ 
/*    */   public TimedWindowEvent(Window paramWindow1, int paramInt, Window paramWindow2, long paramLong) {
/* 40 */     super(paramWindow1, paramInt, paramWindow2);
/* 41 */     this.time = paramLong;
/*    */   }
/*    */ 
/*    */   public TimedWindowEvent(Window paramWindow1, int paramInt1, Window paramWindow2, int paramInt2, int paramInt3, long paramLong)
/*    */   {
/* 47 */     super(paramWindow1, paramInt1, paramWindow2, paramInt2, paramInt3);
/* 48 */     this.time = paramLong;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.TimedWindowEvent
 * JD-Core Version:    0.6.2
 */