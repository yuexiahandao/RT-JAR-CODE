/*     */ package java.awt.dnd;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.Point;
/*     */ import java.awt.event.InputEvent;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.TooManyListenersException;
/*     */ 
/*     */ public abstract class DragGestureRecognizer
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 8996673345831063337L;
/*     */   protected DragSource dragSource;
/*     */   protected Component component;
/*     */   protected transient DragGestureListener dragGestureListener;
/*     */   protected int sourceActions;
/* 465 */   protected ArrayList<InputEvent> events = new ArrayList(1);
/*     */ 
/*     */   protected DragGestureRecognizer(DragSource paramDragSource, Component paramComponent, int paramInt, DragGestureListener paramDragGestureListener)
/*     */   {
/* 124 */     if (paramDragSource == null) throw new IllegalArgumentException("null DragSource");
/*     */ 
/* 126 */     this.dragSource = paramDragSource;
/* 127 */     this.component = paramComponent;
/* 128 */     this.sourceActions = (paramInt & 0x40000003);
/*     */     try
/*     */     {
/* 131 */       if (paramDragGestureListener != null) addDragGestureListener(paramDragGestureListener);
/*     */     }
/*     */     catch (TooManyListenersException localTooManyListenersException)
/*     */     {
/*     */     }
/*     */   }
/*     */ 
/*     */   protected DragGestureRecognizer(DragSource paramDragSource, Component paramComponent, int paramInt)
/*     */   {
/* 165 */     this(paramDragSource, paramComponent, paramInt, null);
/*     */   }
/*     */ 
/*     */   protected DragGestureRecognizer(DragSource paramDragSource, Component paramComponent)
/*     */   {
/* 193 */     this(paramDragSource, paramComponent, 0);
/*     */   }
/*     */ 
/*     */   protected DragGestureRecognizer(DragSource paramDragSource)
/*     */   {
/* 210 */     this(paramDragSource, null);
/*     */   }
/*     */ 
/*     */   protected abstract void registerListeners();
/*     */ 
/*     */   protected abstract void unregisterListeners();
/*     */ 
/*     */   public DragSource getDragSource()
/*     */   {
/* 238 */     return this.dragSource;
/*     */   }
/*     */ 
/*     */   public synchronized Component getComponent()
/*     */   {
/* 250 */     return this.component;
/*     */   }
/*     */ 
/*     */   public synchronized void setComponent(Component paramComponent)
/*     */   {
/* 262 */     if ((this.component != null) && (this.dragGestureListener != null)) {
/* 263 */       unregisterListeners();
/*     */     }
/* 265 */     this.component = paramComponent;
/*     */ 
/* 267 */     if ((this.component != null) && (this.dragGestureListener != null))
/* 268 */       registerListeners();
/*     */   }
/*     */ 
/*     */   public synchronized int getSourceActions()
/*     */   {
/* 279 */     return this.sourceActions;
/*     */   }
/*     */ 
/*     */   public synchronized void setSourceActions(int paramInt)
/*     */   {
/* 289 */     this.sourceActions = (paramInt & 0x40000003);
/*     */   }
/*     */ 
/*     */   public InputEvent getTriggerEvent()
/*     */   {
/* 300 */     return this.events.isEmpty() ? null : (InputEvent)this.events.get(0);
/*     */   }
/*     */ 
/*     */   public void resetRecognizer()
/*     */   {
/* 307 */     this.events.clear();
/*     */   }
/*     */ 
/*     */   public synchronized void addDragGestureListener(DragGestureListener paramDragGestureListener)
/*     */     throws TooManyListenersException
/*     */   {
/* 320 */     if (this.dragGestureListener != null) {
/* 321 */       throw new TooManyListenersException();
/*     */     }
/* 323 */     this.dragGestureListener = paramDragGestureListener;
/*     */ 
/* 325 */     if (this.component != null) registerListeners();
/*     */   }
/*     */ 
/*     */   public synchronized void removeDragGestureListener(DragGestureListener paramDragGestureListener)
/*     */   {
/* 340 */     if ((this.dragGestureListener == null) || (!this.dragGestureListener.equals(paramDragGestureListener))) {
/* 341 */       throw new IllegalArgumentException();
/*     */     }
/* 343 */     this.dragGestureListener = null;
/*     */ 
/* 345 */     if (this.component != null) unregisterListeners();
/*     */   }
/*     */ 
/*     */   protected synchronized void fireDragGestureRecognized(int paramInt, Point paramPoint)
/*     */   {
/*     */     try
/*     */     {
/* 358 */       if (this.dragGestureListener != null)
/* 359 */         this.dragGestureListener.dragGestureRecognized(new DragGestureEvent(this, paramInt, paramPoint, this.events));
/*     */     }
/*     */     finally {
/* 362 */       this.events.clear();
/*     */     }
/*     */   }
/*     */ 
/*     */   protected synchronized void appendEvent(InputEvent paramInputEvent)
/*     */   {
/* 385 */     this.events.add(paramInputEvent);
/*     */   }
/*     */ 
/*     */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*     */     throws IOException
/*     */   {
/* 400 */     paramObjectOutputStream.defaultWriteObject();
/*     */ 
/* 402 */     paramObjectOutputStream.writeObject(SerializationTester.test(this.dragGestureListener) ? this.dragGestureListener : null);
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream)
/*     */     throws ClassNotFoundException, IOException
/*     */   {
/* 417 */     paramObjectInputStream.defaultReadObject();
/*     */ 
/* 419 */     this.dragGestureListener = ((DragGestureListener)paramObjectInputStream.readObject());
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.dnd.DragGestureRecognizer
 * JD-Core Version:    0.6.2
 */