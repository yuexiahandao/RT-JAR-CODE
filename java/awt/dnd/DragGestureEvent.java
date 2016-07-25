/*     */ package java.awt.dnd;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.Cursor;
/*     */ import java.awt.Image;
/*     */ import java.awt.Point;
/*     */ import java.awt.datatransfer.Transferable;
/*     */ import java.awt.event.InputEvent;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectInputStream.GetField;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.util.Collections;
/*     */ import java.util.EventObject;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ 
/*     */ public class DragGestureEvent extends EventObject
/*     */ {
/*     */   private static final long serialVersionUID = 9080172649166731306L;
/*     */   private transient List events;
/*     */   private DragSource dragSource;
/*     */   private Component component;
/*     */   private Point origin;
/*     */   private int action;
/*     */ 
/*     */   public DragGestureEvent(DragGestureRecognizer paramDragGestureRecognizer, int paramInt, Point paramPoint, List<? extends InputEvent> paramList)
/*     */   {
/* 102 */     super(paramDragGestureRecognizer);
/*     */ 
/* 104 */     if ((this.component = paramDragGestureRecognizer.getComponent()) == null)
/* 105 */       throw new IllegalArgumentException("null component");
/* 106 */     if ((this.dragSource = paramDragGestureRecognizer.getDragSource()) == null) {
/* 107 */       throw new IllegalArgumentException("null DragSource");
/*     */     }
/* 109 */     if ((paramList == null) || (paramList.isEmpty())) {
/* 110 */       throw new IllegalArgumentException("null or empty list of events");
/*     */     }
/* 112 */     if ((paramInt != 1) && (paramInt != 2) && (paramInt != 1073741824))
/*     */     {
/* 115 */       throw new IllegalArgumentException("bad action");
/*     */     }
/* 117 */     if (paramPoint == null) throw new IllegalArgumentException("null origin");
/*     */ 
/* 119 */     this.events = paramList;
/* 120 */     this.action = paramInt;
/* 121 */     this.origin = paramPoint;
/*     */   }
/*     */ 
/*     */   public DragGestureRecognizer getSourceAsDragGestureRecognizer()
/*     */   {
/* 131 */     return (DragGestureRecognizer)getSource();
/*     */   }
/*     */ 
/*     */   public Component getComponent()
/*     */   {
/* 141 */     return this.component;
/*     */   }
/*     */ 
/*     */   public DragSource getDragSource()
/*     */   {
/* 149 */     return this.dragSource;
/*     */   }
/*     */ 
/*     */   public Point getDragOrigin()
/*     */   {
/* 159 */     return this.origin;
/*     */   }
/*     */ 
/*     */   public Iterator<InputEvent> iterator()
/*     */   {
/* 169 */     return this.events.iterator();
/*     */   }
/*     */ 
/*     */   public Object[] toArray()
/*     */   {
/* 178 */     return this.events.toArray();
/*     */   }
/*     */ 
/*     */   public Object[] toArray(Object[] paramArrayOfObject)
/*     */   {
/* 188 */     return this.events.toArray(paramArrayOfObject);
/*     */   }
/*     */ 
/*     */   public int getDragAction()
/*     */   {
/* 197 */     return this.action;
/*     */   }
/*     */ 
/*     */   public InputEvent getTriggerEvent()
/*     */   {
/* 206 */     return getSourceAsDragGestureRecognizer().getTriggerEvent();
/*     */   }
/*     */ 
/*     */   public void startDrag(Cursor paramCursor, Transferable paramTransferable)
/*     */     throws InvalidDnDOperationException
/*     */   {
/* 237 */     this.dragSource.startDrag(this, paramCursor, paramTransferable, null);
/*     */   }
/*     */ 
/*     */   public void startDrag(Cursor paramCursor, Transferable paramTransferable, DragSourceListener paramDragSourceListener)
/*     */     throws InvalidDnDOperationException
/*     */   {
/* 262 */     this.dragSource.startDrag(this, paramCursor, paramTransferable, paramDragSourceListener);
/*     */   }
/*     */ 
/*     */   public void startDrag(Cursor paramCursor, Image paramImage, Point paramPoint, Transferable paramTransferable, DragSourceListener paramDragSourceListener)
/*     */     throws InvalidDnDOperationException
/*     */   {
/* 291 */     this.dragSource.startDrag(this, paramCursor, paramImage, paramPoint, paramTransferable, paramDragSourceListener);
/*     */   }
/*     */ 
/*     */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*     */     throws IOException
/*     */   {
/* 308 */     paramObjectOutputStream.defaultWriteObject();
/*     */ 
/* 310 */     paramObjectOutputStream.writeObject(SerializationTester.test(this.events) ? this.events : null);
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream)
/*     */     throws ClassNotFoundException, IOException
/*     */   {
/* 330 */     ObjectInputStream.GetField localGetField = paramObjectInputStream.readFields();
/*     */ 
/* 332 */     this.dragSource = ((DragSource)localGetField.get("dragSource", null));
/* 333 */     this.component = ((Component)localGetField.get("component", null));
/* 334 */     this.origin = ((Point)localGetField.get("origin", null));
/* 335 */     this.action = localGetField.get("action", 0);
/*     */     try
/*     */     {
/* 339 */       this.events = ((List)localGetField.get("events", null));
/*     */     }
/*     */     catch (IllegalArgumentException localIllegalArgumentException) {
/* 342 */       this.events = ((List)paramObjectInputStream.readObject());
/*     */     }
/*     */ 
/* 346 */     if (this.events == null)
/* 347 */       this.events = Collections.EMPTY_LIST;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.dnd.DragGestureEvent
 * JD-Core Version:    0.6.2
 */