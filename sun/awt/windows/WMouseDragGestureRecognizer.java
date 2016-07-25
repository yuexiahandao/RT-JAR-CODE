/*     */ package sun.awt.windows;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.Point;
/*     */ import java.awt.dnd.DragGestureListener;
/*     */ import java.awt.dnd.DragSource;
/*     */ import java.awt.dnd.MouseDragGestureRecognizer;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.util.ArrayList;
/*     */ import sun.awt.dnd.SunDragSourceContextPeer;
/*     */ 
/*     */ class WMouseDragGestureRecognizer extends MouseDragGestureRecognizer
/*     */ {
/*     */   private static final long serialVersionUID = -3527844310018033570L;
/*     */   protected static int motionThreshold;
/*     */   protected static final int ButtonMask = 7168;
/*     */ 
/*     */   protected WMouseDragGestureRecognizer(DragSource paramDragSource, Component paramComponent, int paramInt, DragGestureListener paramDragGestureListener)
/*     */   {
/*  83 */     super(paramDragSource, paramComponent, paramInt, paramDragGestureListener);
/*     */   }
/*     */ 
/*     */   protected WMouseDragGestureRecognizer(DragSource paramDragSource, Component paramComponent, int paramInt)
/*     */   {
/*  95 */     this(paramDragSource, paramComponent, paramInt, null);
/*     */   }
/*     */ 
/*     */   protected WMouseDragGestureRecognizer(DragSource paramDragSource, Component paramComponent)
/*     */   {
/* 106 */     this(paramDragSource, paramComponent, 0);
/*     */   }
/*     */ 
/*     */   protected WMouseDragGestureRecognizer(DragSource paramDragSource)
/*     */   {
/* 116 */     this(paramDragSource, null);
/*     */   }
/*     */ 
/*     */   protected int mapDragOperationFromModifiers(MouseEvent paramMouseEvent)
/*     */   {
/* 124 */     int i = paramMouseEvent.getModifiersEx();
/* 125 */     int j = i & 0x1C00;
/*     */ 
/* 128 */     if ((j != 1024) && (j != 2048) && (j != 4096))
/*     */     {
/* 131 */       return 0;
/*     */     }
/*     */ 
/* 134 */     return SunDragSourceContextPeer.convertModifiersToDropAction(i, getSourceActions());
/*     */   }
/*     */ 
/*     */   public void mouseClicked(MouseEvent paramMouseEvent)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void mousePressed(MouseEvent paramMouseEvent)
/*     */   {
/* 152 */     this.events.clear();
/*     */ 
/* 154 */     if (mapDragOperationFromModifiers(paramMouseEvent) != 0) {
/*     */       try {
/* 156 */         motionThreshold = DragSource.getDragThreshold();
/*     */       } catch (Exception localException) {
/* 158 */         motionThreshold = 5;
/*     */       }
/* 160 */       appendEvent(paramMouseEvent);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void mouseReleased(MouseEvent paramMouseEvent)
/*     */   {
/* 169 */     this.events.clear();
/*     */   }
/*     */ 
/*     */   public void mouseEntered(MouseEvent paramMouseEvent)
/*     */   {
/* 177 */     this.events.clear();
/*     */   }
/*     */ 
/*     */   public void mouseExited(MouseEvent paramMouseEvent)
/*     */   {
/* 186 */     if (!this.events.isEmpty()) {
/* 187 */       int i = mapDragOperationFromModifiers(paramMouseEvent);
/*     */ 
/* 189 */       if (i == 0)
/* 190 */         this.events.clear();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void mouseDragged(MouseEvent paramMouseEvent)
/*     */   {
/* 200 */     if (!this.events.isEmpty()) {
/* 201 */       int i = mapDragOperationFromModifiers(paramMouseEvent);
/*     */ 
/* 203 */       if (i == 0) {
/* 204 */         return;
/*     */       }
/*     */ 
/* 207 */       MouseEvent localMouseEvent = (MouseEvent)this.events.get(0);
/*     */ 
/* 210 */       Point localPoint1 = localMouseEvent.getPoint();
/* 211 */       Point localPoint2 = paramMouseEvent.getPoint();
/*     */ 
/* 213 */       int j = Math.abs(localPoint1.x - localPoint2.x);
/* 214 */       int k = Math.abs(localPoint1.y - localPoint2.y);
/*     */ 
/* 216 */       if ((j > motionThreshold) || (k > motionThreshold))
/* 217 */         fireDragGestureRecognized(i, ((MouseEvent)getTriggerEvent()).getPoint());
/*     */       else
/* 219 */         appendEvent(paramMouseEvent);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void mouseMoved(MouseEvent paramMouseEvent)
/*     */   {
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.windows.WMouseDragGestureRecognizer
 * JD-Core Version:    0.6.2
 */