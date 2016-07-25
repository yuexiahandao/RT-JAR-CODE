/*    */ package sun.awt;
/*    */ 
/*    */ import java.awt.AWTEvent;
/*    */ import java.awt.ActiveEvent;
/*    */ 
/*    */ public class ModalityEvent extends AWTEvent
/*    */   implements ActiveEvent
/*    */ {
/*    */   public static final int MODALITY_PUSHED = 1300;
/*    */   public static final int MODALITY_POPPED = 1301;
/*    */   private ModalityListener listener;
/*    */ 
/*    */   public ModalityEvent(Object paramObject, ModalityListener paramModalityListener, int paramInt)
/*    */   {
/* 41 */     super(paramObject, paramInt);
/* 42 */     this.listener = paramModalityListener;
/*    */   }
/*    */ 
/*    */   public void dispatch() {
/* 46 */     switch (getID()) {
/*    */     case 1300:
/* 48 */       this.listener.modalityPushed(this);
/* 49 */       break;
/*    */     case 1301:
/* 52 */       this.listener.modalityPopped(this);
/* 53 */       break;
/*    */     default:
/* 56 */       throw new Error("Invalid event id.");
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.ModalityEvent
 * JD-Core Version:    0.6.2
 */