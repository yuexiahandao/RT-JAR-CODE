/*    */ package sun.awt;
/*    */ 
/*    */ import java.awt.AWTEvent;
/*    */ import java.awt.Component;
/*    */ 
/*    */ public class UngrabEvent extends AWTEvent
/*    */ {
/*    */   private static final int UNGRAB_EVENT_ID = 1998;
/*    */ 
/*    */   public UngrabEvent(Component paramComponent)
/*    */   {
/* 46 */     super(paramComponent, 1998);
/*    */   }
/*    */ 
/*    */   public String toString() {
/* 50 */     return "sun.awt.UngrabEvent[" + getSource() + "]";
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.UngrabEvent
 * JD-Core Version:    0.6.2
 */