/*     */ package java.awt.dnd;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.awt.event.MouseListener;
/*     */ import java.awt.event.MouseMotionListener;
/*     */ 
/*     */ public abstract class MouseDragGestureRecognizer extends DragGestureRecognizer
/*     */   implements MouseListener, MouseMotionListener
/*     */ {
/*     */   private static final long serialVersionUID = 6220099344182281120L;
/*     */ 
/*     */   protected MouseDragGestureRecognizer(DragSource paramDragSource, Component paramComponent, int paramInt, DragGestureListener paramDragGestureListener)
/*     */   {
/*  86 */     super(paramDragSource, paramComponent, paramInt, paramDragGestureListener);
/*     */   }
/*     */ 
/*     */   protected MouseDragGestureRecognizer(DragSource paramDragSource, Component paramComponent, int paramInt)
/*     */   {
/* 102 */     this(paramDragSource, paramComponent, paramInt, null);
/*     */   }
/*     */ 
/*     */   protected MouseDragGestureRecognizer(DragSource paramDragSource, Component paramComponent)
/*     */   {
/* 116 */     this(paramDragSource, paramComponent, 0);
/*     */   }
/*     */ 
/*     */   protected MouseDragGestureRecognizer(DragSource paramDragSource)
/*     */   {
/* 127 */     this(paramDragSource, null);
/*     */   }
/*     */ 
/*     */   protected void registerListeners()
/*     */   {
/* 135 */     this.component.addMouseListener(this);
/* 136 */     this.component.addMouseMotionListener(this);
/*     */   }
/*     */ 
/*     */   protected void unregisterListeners()
/*     */   {
/* 147 */     this.component.removeMouseListener(this);
/* 148 */     this.component.removeMouseMotionListener(this);
/*     */   }
/*     */ 
/*     */   public void mouseClicked(MouseEvent paramMouseEvent)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void mousePressed(MouseEvent paramMouseEvent)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void mouseReleased(MouseEvent paramMouseEvent)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void mouseEntered(MouseEvent paramMouseEvent)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void mouseExited(MouseEvent paramMouseEvent)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void mouseDragged(MouseEvent paramMouseEvent)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void mouseMoved(MouseEvent paramMouseEvent)
/*     */   {
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.dnd.MouseDragGestureRecognizer
 * JD-Core Version:    0.6.2
 */