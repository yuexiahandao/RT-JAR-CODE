/*    */ package javax.swing.event;
/*    */ 
/*    */ import java.util.EventObject;
/*    */ 
/*    */ public abstract class CaretEvent extends EventObject
/*    */ {
/*    */   public CaretEvent(Object paramObject)
/*    */   {
/* 53 */     super(paramObject);
/*    */   }
/*    */ 
/*    */   public abstract int getDot();
/*    */ 
/*    */   public abstract int getMark();
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.event.CaretEvent
 * JD-Core Version:    0.6.2
 */