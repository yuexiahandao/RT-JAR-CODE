/*     */ package java.awt;
/*     */ 
/*     */ import java.awt.event.AdjustmentEvent;
/*     */ import java.awt.event.AdjustmentListener;
/*     */ import java.awt.peer.ScrollPanePeer;
/*     */ import java.io.Serializable;
/*     */ 
/*     */ class PeerFixer
/*     */   implements AdjustmentListener, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 7051237413532574756L;
/*     */   private ScrollPane scroller;
/*     */ 
/*     */   PeerFixer(ScrollPane paramScrollPane)
/*     */   {
/* 821 */     this.scroller = paramScrollPane;
/*     */   }
/*     */ 
/*     */   public void adjustmentValueChanged(AdjustmentEvent paramAdjustmentEvent)
/*     */   {
/* 828 */     Adjustable localAdjustable = paramAdjustmentEvent.getAdjustable();
/* 829 */     int i = paramAdjustmentEvent.getValue();
/* 830 */     ScrollPanePeer localScrollPanePeer = (ScrollPanePeer)this.scroller.peer;
/* 831 */     if (localScrollPanePeer != null) {
/* 832 */       localScrollPanePeer.setValue(localAdjustable, i);
/*     */     }
/*     */ 
/* 835 */     Component localComponent = this.scroller.getComponent(0);
/* 836 */     switch (localAdjustable.getOrientation()) {
/*     */     case 1:
/* 838 */       localComponent.move(localComponent.getLocation().x, -i);
/* 839 */       break;
/*     */     case 0:
/* 841 */       localComponent.move(-i, localComponent.getLocation().y);
/* 842 */       break;
/*     */     default:
/* 844 */       throw new IllegalArgumentException("Illegal adjustable orientation");
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.PeerFixer
 * JD-Core Version:    0.6.2
 */