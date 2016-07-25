/*     */ package java.awt.dnd;
/*     */ 
/*     */ import java.awt.Point;
/*     */ import java.util.EventObject;
/*     */ 
/*     */ public class DragSourceEvent extends EventObject
/*     */ {
/*     */   private static final long serialVersionUID = -763287114604032641L;
/*     */   private final boolean locationSpecified;
/*     */   private final int x;
/*     */   private final int y;
/*     */ 
/*     */   public DragSourceEvent(DragSourceContext paramDragSourceContext)
/*     */   {
/* 105 */     super(paramDragSourceContext);
/* 106 */     this.locationSpecified = false;
/* 107 */     this.x = 0;
/* 108 */     this.y = 0;
/*     */   }
/*     */ 
/*     */   public DragSourceEvent(DragSourceContext paramDragSourceContext, int paramInt1, int paramInt2)
/*     */   {
/* 125 */     super(paramDragSourceContext);
/* 126 */     this.locationSpecified = true;
/* 127 */     this.x = paramInt1;
/* 128 */     this.y = paramInt2;
/*     */   }
/*     */ 
/*     */   public DragSourceContext getDragSourceContext()
/*     */   {
/* 139 */     return (DragSourceContext)getSource();
/*     */   }
/*     */ 
/*     */   public Point getLocation()
/*     */   {
/* 153 */     if (this.locationSpecified) {
/* 154 */       return new Point(this.x, this.y);
/*     */     }
/* 156 */     return null;
/*     */   }
/*     */ 
/*     */   public int getX()
/*     */   {
/* 170 */     return this.x;
/*     */   }
/*     */ 
/*     */   public int getY()
/*     */   {
/* 183 */     return this.y;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.dnd.DragSourceEvent
 * JD-Core Version:    0.6.2
 */