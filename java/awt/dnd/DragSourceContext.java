/*     */ package java.awt.dnd;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.Cursor;
/*     */ import java.awt.Image;
/*     */ import java.awt.Point;
/*     */ import java.awt.datatransfer.DataFlavor;
/*     */ import java.awt.datatransfer.Transferable;
/*     */ import java.awt.datatransfer.UnsupportedFlavorException;
/*     */ import java.awt.dnd.peer.DragSourceContextPeer;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.TooManyListenersException;
/*     */ 
/*     */ public class DragSourceContext
/*     */   implements DragSourceListener, DragSourceMotionListener, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -115407898692194719L;
/*     */   protected static final int DEFAULT = 0;
/*     */   protected static final int ENTER = 1;
/*     */   protected static final int OVER = 2;
/*     */   protected static final int CHANGED = 3;
/*     */   private static Transferable emptyTransferable;
/*     */   private transient DragSourceContextPeer peer;
/*     */   private DragGestureEvent trigger;
/*     */   private Cursor cursor;
/*     */   private transient Transferable transferable;
/*     */   private transient DragSourceListener listener;
/*     */   private boolean useCustomCursor;
/*     */   private final int sourceActions;
/*     */ 
/*     */   public DragSourceContext(DragSourceContextPeer paramDragSourceContextPeer, DragGestureEvent paramDragGestureEvent, Cursor paramCursor, Image paramImage, Point paramPoint, Transferable paramTransferable, DragSourceListener paramDragSourceListener)
/*     */   {
/* 185 */     if (paramDragSourceContextPeer == null) {
/* 186 */       throw new NullPointerException("DragSourceContextPeer");
/*     */     }
/*     */ 
/* 189 */     if (paramDragGestureEvent == null) {
/* 190 */       throw new NullPointerException("Trigger");
/*     */     }
/*     */ 
/* 193 */     if (paramDragGestureEvent.getDragSource() == null) {
/* 194 */       throw new IllegalArgumentException("DragSource");
/*     */     }
/*     */ 
/* 197 */     if (paramDragGestureEvent.getComponent() == null) {
/* 198 */       throw new IllegalArgumentException("Component");
/*     */     }
/*     */ 
/* 201 */     if (paramDragGestureEvent.getSourceAsDragGestureRecognizer().getSourceActions() == 0)
/*     */     {
/* 203 */       throw new IllegalArgumentException("source actions");
/*     */     }
/*     */ 
/* 206 */     if (paramDragGestureEvent.getDragAction() == 0) {
/* 207 */       throw new IllegalArgumentException("no drag action");
/*     */     }
/*     */ 
/* 210 */     if (paramTransferable == null) {
/* 211 */       throw new NullPointerException("Transferable");
/*     */     }
/*     */ 
/* 214 */     if ((paramImage != null) && (paramPoint == null)) {
/* 215 */       throw new NullPointerException("offset");
/*     */     }
/*     */ 
/* 218 */     this.peer = paramDragSourceContextPeer;
/* 219 */     this.trigger = paramDragGestureEvent;
/* 220 */     this.cursor = paramCursor;
/* 221 */     this.transferable = paramTransferable;
/* 222 */     this.listener = paramDragSourceListener;
/* 223 */     this.sourceActions = paramDragGestureEvent.getSourceAsDragGestureRecognizer().getSourceActions();
/*     */ 
/* 226 */     this.useCustomCursor = (paramCursor != null);
/*     */ 
/* 228 */     updateCurrentCursor(paramDragGestureEvent.getDragAction(), getSourceActions(), 0);
/*     */   }
/*     */ 
/*     */   public DragSource getDragSource()
/*     */   {
/* 239 */     return this.trigger.getDragSource();
/*     */   }
/*     */ 
/*     */   public Component getComponent()
/*     */   {
/* 248 */     return this.trigger.getComponent();
/*     */   }
/*     */ 
/*     */   public DragGestureEvent getTrigger()
/*     */   {
/* 257 */     return this.trigger;
/*     */   }
/*     */ 
/*     */   public int getSourceActions()
/*     */   {
/* 267 */     return this.sourceActions;
/*     */   }
/*     */ 
/*     */   public synchronized void setCursor(Cursor paramCursor)
/*     */   {
/* 285 */     this.useCustomCursor = (paramCursor != null);
/* 286 */     setCursorImpl(paramCursor);
/*     */   }
/*     */ 
/*     */   public Cursor getCursor()
/*     */   {
/* 295 */     return this.cursor;
/*     */   }
/*     */ 
/*     */   public synchronized void addDragSourceListener(DragSourceListener paramDragSourceListener)
/*     */     throws TooManyListenersException
/*     */   {
/* 312 */     if (paramDragSourceListener == null) return;
/*     */ 
/* 314 */     if (equals(paramDragSourceListener)) throw new IllegalArgumentException("DragSourceContext may not be its own listener");
/*     */ 
/* 316 */     if (this.listener != null) {
/* 317 */       throw new TooManyListenersException();
/*     */     }
/* 319 */     this.listener = paramDragSourceListener;
/*     */   }
/*     */ 
/*     */   public synchronized void removeDragSourceListener(DragSourceListener paramDragSourceListener)
/*     */   {
/* 332 */     if ((this.listener != null) && (this.listener.equals(paramDragSourceListener)))
/* 333 */       this.listener = null;
/*     */     else
/* 335 */       throw new IllegalArgumentException();
/*     */   }
/*     */ 
/*     */   public void transferablesFlavorsChanged()
/*     */   {
/* 344 */     if (this.peer != null) this.peer.transferablesFlavorsChanged();
/*     */   }
/*     */ 
/*     */   public void dragEnter(DragSourceDragEvent paramDragSourceDragEvent)
/*     */   {
/* 357 */     DragSourceListener localDragSourceListener = this.listener;
/* 358 */     if (localDragSourceListener != null) {
/* 359 */       localDragSourceListener.dragEnter(paramDragSourceDragEvent);
/*     */     }
/* 361 */     getDragSource().processDragEnter(paramDragSourceDragEvent);
/*     */ 
/* 363 */     updateCurrentCursor(getSourceActions(), paramDragSourceDragEvent.getTargetActions(), 1);
/*     */   }
/*     */ 
/*     */   public void dragOver(DragSourceDragEvent paramDragSourceDragEvent)
/*     */   {
/* 376 */     DragSourceListener localDragSourceListener = this.listener;
/* 377 */     if (localDragSourceListener != null) {
/* 378 */       localDragSourceListener.dragOver(paramDragSourceDragEvent);
/*     */     }
/* 380 */     getDragSource().processDragOver(paramDragSourceDragEvent);
/*     */ 
/* 382 */     updateCurrentCursor(getSourceActions(), paramDragSourceDragEvent.getTargetActions(), 2);
/*     */   }
/*     */ 
/*     */   public void dragExit(DragSourceEvent paramDragSourceEvent)
/*     */   {
/* 395 */     DragSourceListener localDragSourceListener = this.listener;
/* 396 */     if (localDragSourceListener != null) {
/* 397 */       localDragSourceListener.dragExit(paramDragSourceEvent);
/*     */     }
/* 399 */     getDragSource().processDragExit(paramDragSourceEvent);
/*     */ 
/* 401 */     updateCurrentCursor(0, 0, 0);
/*     */   }
/*     */ 
/*     */   public void dropActionChanged(DragSourceDragEvent paramDragSourceDragEvent)
/*     */   {
/* 414 */     DragSourceListener localDragSourceListener = this.listener;
/* 415 */     if (localDragSourceListener != null) {
/* 416 */       localDragSourceListener.dropActionChanged(paramDragSourceDragEvent);
/*     */     }
/* 418 */     getDragSource().processDropActionChanged(paramDragSourceDragEvent);
/*     */ 
/* 420 */     updateCurrentCursor(getSourceActions(), paramDragSourceDragEvent.getTargetActions(), 3);
/*     */   }
/*     */ 
/*     */   public void dragDropEnd(DragSourceDropEvent paramDragSourceDropEvent)
/*     */   {
/* 433 */     DragSourceListener localDragSourceListener = this.listener;
/* 434 */     if (localDragSourceListener != null) {
/* 435 */       localDragSourceListener.dragDropEnd(paramDragSourceDropEvent);
/*     */     }
/* 437 */     getDragSource().processDragDropEnd(paramDragSourceDropEvent);
/*     */   }
/*     */ 
/*     */   public void dragMouseMoved(DragSourceDragEvent paramDragSourceDragEvent)
/*     */   {
/* 451 */     getDragSource().processDragMouseMoved(paramDragSourceDragEvent);
/*     */   }
/*     */ 
/*     */   public Transferable getTransferable()
/*     */   {
/* 460 */     return this.transferable;
/*     */   }
/*     */ 
/*     */   protected synchronized void updateCurrentCursor(int paramInt1, int paramInt2, int paramInt3)
/*     */   {
/* 480 */     if (this.useCustomCursor) {
/* 481 */       return;
/*     */     }
/*     */ 
/* 486 */     Cursor localCursor = null;
/*     */ 
/* 488 */     switch (paramInt3) {
/*     */     default:
/* 490 */       paramInt2 = 0;
/*     */     case 1:
/*     */     case 2:
/*     */     case 3:
/* 494 */     }int i = paramInt1 & paramInt2;
/*     */ 
/* 496 */     if (i == 0) {
/* 497 */       if ((paramInt1 & 0x40000000) == 1073741824)
/* 498 */         localCursor = DragSource.DefaultLinkNoDrop;
/* 499 */       else if ((paramInt1 & 0x2) == 2)
/* 500 */         localCursor = DragSource.DefaultMoveNoDrop;
/*     */       else
/* 502 */         localCursor = DragSource.DefaultCopyNoDrop;
/*     */     }
/* 504 */     else if ((i & 0x40000000) == 1073741824)
/* 505 */       localCursor = DragSource.DefaultLinkDrop;
/* 506 */     else if ((i & 0x2) == 2)
/* 507 */       localCursor = DragSource.DefaultMoveDrop;
/*     */     else {
/* 509 */       localCursor = DragSource.DefaultCopyDrop;
/*     */     }
/*     */ 
/* 513 */     setCursorImpl(localCursor);
/*     */   }
/*     */ 
/*     */   private void setCursorImpl(Cursor paramCursor) {
/* 517 */     if ((this.cursor == null) || (!this.cursor.equals(paramCursor))) {
/* 518 */       this.cursor = paramCursor;
/* 519 */       if (this.peer != null) this.peer.setCursor(this.cursor);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*     */     throws IOException
/*     */   {
/* 542 */     paramObjectOutputStream.defaultWriteObject();
/*     */ 
/* 544 */     paramObjectOutputStream.writeObject(SerializationTester.test(this.transferable) ? this.transferable : null);
/*     */ 
/* 546 */     paramObjectOutputStream.writeObject(SerializationTester.test(this.listener) ? this.listener : null);
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream)
/*     */     throws ClassNotFoundException, IOException
/*     */   {
/* 565 */     paramObjectInputStream.defaultReadObject();
/*     */ 
/* 567 */     this.transferable = ((Transferable)paramObjectInputStream.readObject());
/* 568 */     this.listener = ((Serializable)paramObjectInputStream.readObject());
/*     */ 
/* 571 */     if (this.transferable == null) {
/* 572 */       if (emptyTransferable == null) {
/* 573 */         emptyTransferable = new Transferable() {
/*     */           public DataFlavor[] getTransferDataFlavors() {
/* 575 */             return new DataFlavor[0];
/*     */           }
/*     */ 
/*     */           public boolean isDataFlavorSupported(DataFlavor paramAnonymousDataFlavor) {
/* 579 */             return false;
/*     */           }
/*     */ 
/*     */           public Object getTransferData(DataFlavor paramAnonymousDataFlavor) throws UnsupportedFlavorException
/*     */           {
/* 584 */             throw new UnsupportedFlavorException(paramAnonymousDataFlavor);
/*     */           }
/*     */         };
/*     */       }
/* 588 */       this.transferable = emptyTransferable;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.dnd.DragSourceContext
 * JD-Core Version:    0.6.2
 */