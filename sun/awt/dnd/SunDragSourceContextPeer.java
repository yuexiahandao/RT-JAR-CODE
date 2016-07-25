/*     */ package sun.awt.dnd;
/*     */ 
/*     */ import java.awt.AWTEvent;
/*     */ import java.awt.Component;
/*     */ import java.awt.Cursor;
/*     */ import java.awt.EventQueue;
/*     */ import java.awt.Image;
/*     */ import java.awt.Point;
/*     */ import java.awt.datatransfer.Transferable;
/*     */ import java.awt.dnd.DragGestureEvent;
/*     */ import java.awt.dnd.DragSource;
/*     */ import java.awt.dnd.DragSourceContext;
/*     */ import java.awt.dnd.DragSourceDragEvent;
/*     */ import java.awt.dnd.DragSourceDropEvent;
/*     */ import java.awt.dnd.DragSourceEvent;
/*     */ import java.awt.dnd.InvalidDnDOperationException;
/*     */ import java.awt.dnd.peer.DragSourceContextPeer;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.util.Map;
/*     */ import java.util.SortedMap;
/*     */ import sun.awt.SunToolkit;
/*     */ import sun.awt.datatransfer.DataTransferer;
/*     */ 
/*     */ public abstract class SunDragSourceContextPeer
/*     */   implements DragSourceContextPeer
/*     */ {
/*     */   private DragGestureEvent trigger;
/*     */   private Component component;
/*     */   private Cursor cursor;
/*     */   private Image dragImage;
/*     */   private Point dragImageOffset;
/*     */   private long nativeCtxt;
/*     */   private DragSourceContext dragSourceContext;
/*     */   private int sourceActions;
/*  76 */   private static boolean dragDropInProgress = false;
/*  77 */   private static boolean discardingMouseEvents = false;
/*     */   protected static final int DISPATCH_ENTER = 1;
/*     */   protected static final int DISPATCH_MOTION = 2;
/*     */   protected static final int DISPATCH_CHANGED = 3;
/*     */   protected static final int DISPATCH_EXIT = 4;
/*     */   protected static final int DISPATCH_FINISH = 5;
/*     */   protected static final int DISPATCH_MOUSE_MOVED = 6;
/*     */ 
/*     */   public SunDragSourceContextPeer(DragGestureEvent paramDragGestureEvent)
/*     */   {
/*  95 */     this.trigger = paramDragGestureEvent;
/*  96 */     if (this.trigger != null)
/*  97 */       this.component = this.trigger.getComponent();
/*     */     else
/*  99 */       this.component = null;
/*     */   }
/*     */ 
/*     */   public void startSecondaryEventLoop()
/*     */   {
/*     */   }
/*     */ 
/*     */   public void quitSecondaryEventLoop()
/*     */   {
/*     */   }
/*     */ 
/*     */   public void startDrag(DragSourceContext paramDragSourceContext, Cursor paramCursor, Image paramImage, Point paramPoint)
/*     */     throws InvalidDnDOperationException
/*     */   {
/* 118 */     if (getTrigger().getTriggerEvent() == null) {
/* 119 */       throw new InvalidDnDOperationException("DragGestureEvent has a null trigger");
/*     */     }
/*     */ 
/* 122 */     this.dragSourceContext = paramDragSourceContext;
/* 123 */     this.cursor = paramCursor;
/* 124 */     this.sourceActions = getDragSourceContext().getSourceActions();
/* 125 */     this.dragImage = paramImage;
/* 126 */     this.dragImageOffset = paramPoint;
/*     */ 
/* 128 */     Transferable localTransferable = getDragSourceContext().getTransferable();
/* 129 */     SortedMap localSortedMap = DataTransferer.getInstance().getFormatsForTransferable(localTransferable, DataTransferer.adaptFlavorMap(getTrigger().getDragSource().getFlavorMap()));
/*     */ 
/* 132 */     DataTransferer.getInstance(); long[] arrayOfLong = DataTransferer.keysToLongArray(localSortedMap);
/*     */ 
/* 134 */     startDrag(localTransferable, arrayOfLong, localSortedMap);
/*     */ 
/* 140 */     discardingMouseEvents = true;
/* 141 */     EventQueue.invokeLater(new Runnable() {
/*     */       public void run() {
/* 143 */         SunDragSourceContextPeer.access$002(false);
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   protected abstract void startDrag(Transferable paramTransferable, long[] paramArrayOfLong, Map paramMap);
/*     */ 
/*     */   public void setCursor(Cursor paramCursor)
/*     */     throws InvalidDnDOperationException
/*     */   {
/* 156 */     synchronized (this) {
/* 157 */       if ((this.cursor == null) || (!this.cursor.equals(paramCursor))) {
/* 158 */         this.cursor = paramCursor;
/*     */ 
/* 161 */         setNativeCursor(getNativeContext(), paramCursor, paramCursor != null ? paramCursor.getType() : 0);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public Cursor getCursor()
/*     */   {
/* 172 */     return this.cursor;
/*     */   }
/*     */ 
/*     */   public Image getDragImage()
/*     */   {
/* 182 */     return this.dragImage;
/*     */   }
/*     */ 
/*     */   public Point getDragImageOffset()
/*     */   {
/* 194 */     if (this.dragImageOffset == null) {
/* 195 */       return new Point(0, 0);
/*     */     }
/* 197 */     return new Point(this.dragImageOffset);
/*     */   }
/*     */ 
/*     */   protected abstract void setNativeCursor(long paramLong, Cursor paramCursor, int paramInt);
/*     */ 
/*     */   protected synchronized void setTrigger(DragGestureEvent paramDragGestureEvent)
/*     */   {
/* 209 */     this.trigger = paramDragGestureEvent;
/* 210 */     if (this.trigger != null)
/* 211 */       this.component = this.trigger.getComponent();
/*     */     else
/* 213 */       this.component = null;
/*     */   }
/*     */ 
/*     */   protected DragGestureEvent getTrigger()
/*     */   {
/* 218 */     return this.trigger;
/*     */   }
/*     */ 
/*     */   protected Component getComponent() {
/* 222 */     return this.component;
/*     */   }
/*     */ 
/*     */   protected synchronized void setNativeContext(long paramLong) {
/* 226 */     this.nativeCtxt = paramLong;
/*     */   }
/*     */ 
/*     */   protected synchronized long getNativeContext() {
/* 230 */     return this.nativeCtxt;
/*     */   }
/*     */ 
/*     */   protected DragSourceContext getDragSourceContext() {
/* 234 */     return this.dragSourceContext;
/*     */   }
/*     */ 
/*     */   public void transferablesFlavorsChanged()
/*     */   {
/*     */   }
/*     */ 
/*     */   protected final void postDragSourceDragEvent(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*     */   {
/* 256 */     int i = convertModifiersToDropAction(paramInt2, this.sourceActions);
/*     */ 
/* 260 */     DragSourceDragEvent localDragSourceDragEvent = new DragSourceDragEvent(getDragSourceContext(), i, paramInt1 & this.sourceActions, paramInt2, paramInt3, paramInt4);
/*     */ 
/* 265 */     EventDispatcher localEventDispatcher = new EventDispatcher(paramInt5, localDragSourceDragEvent);
/*     */ 
/* 267 */     SunToolkit.invokeLaterOnAppContext(SunToolkit.targetToAppContext(getComponent()), localEventDispatcher);
/*     */ 
/* 270 */     startSecondaryEventLoop();
/*     */   }
/*     */ 
/*     */   protected void dragEnter(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/* 280 */     postDragSourceDragEvent(paramInt1, paramInt2, paramInt3, paramInt4, 1);
/*     */   }
/*     */ 
/*     */   private void dragMotion(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/* 290 */     postDragSourceDragEvent(paramInt1, paramInt2, paramInt3, paramInt4, 2);
/*     */   }
/*     */ 
/*     */   private void operationChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/* 300 */     postDragSourceDragEvent(paramInt1, paramInt2, paramInt3, paramInt4, 3);
/*     */   }
/*     */ 
/*     */   protected final void dragExit(int paramInt1, int paramInt2)
/*     */   {
/* 308 */     DragSourceEvent localDragSourceEvent = new DragSourceEvent(getDragSourceContext(), paramInt1, paramInt2);
/*     */ 
/* 310 */     EventDispatcher localEventDispatcher = new EventDispatcher(4, localDragSourceEvent);
/*     */ 
/* 313 */     SunToolkit.invokeLaterOnAppContext(SunToolkit.targetToAppContext(getComponent()), localEventDispatcher);
/*     */ 
/* 316 */     startSecondaryEventLoop();
/*     */   }
/*     */ 
/*     */   private void dragMouseMoved(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/* 326 */     postDragSourceDragEvent(paramInt1, paramInt2, paramInt3, paramInt4, 6);
/*     */   }
/*     */ 
/*     */   protected final void dragDropFinished(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3)
/*     */   {
/* 337 */     DragSourceDropEvent localDragSourceDropEvent = new DragSourceDropEvent(getDragSourceContext(), paramInt1 & this.sourceActions, paramBoolean, paramInt2, paramInt3);
/*     */ 
/* 341 */     EventDispatcher localEventDispatcher = new EventDispatcher(5, localDragSourceDropEvent);
/*     */ 
/* 344 */     SunToolkit.invokeLaterOnAppContext(SunToolkit.targetToAppContext(getComponent()), localEventDispatcher);
/*     */ 
/* 347 */     startSecondaryEventLoop();
/* 348 */     setNativeContext(0L);
/* 349 */     this.dragImage = null;
/* 350 */     this.dragImageOffset = null;
/*     */   }
/*     */ 
/*     */   public static void setDragDropInProgress(boolean paramBoolean) throws InvalidDnDOperationException
/*     */   {
/* 355 */     synchronized (SunDragSourceContextPeer.class) {
/* 356 */       if (dragDropInProgress == paramBoolean) {
/* 357 */         throw new InvalidDnDOperationException(getExceptionMessage(paramBoolean));
/*     */       }
/* 359 */       dragDropInProgress = paramBoolean;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static boolean checkEvent(AWTEvent paramAWTEvent)
/*     */   {
/* 368 */     if ((discardingMouseEvents) && ((paramAWTEvent instanceof MouseEvent))) {
/* 369 */       MouseEvent localMouseEvent = (MouseEvent)paramAWTEvent;
/* 370 */       if (!(localMouseEvent instanceof SunDropTargetEvent)) {
/* 371 */         return false;
/*     */       }
/*     */     }
/* 374 */     return true;
/*     */   }
/*     */ 
/*     */   public static void checkDragDropInProgress() throws InvalidDnDOperationException
/*     */   {
/* 379 */     if (dragDropInProgress)
/* 380 */       throw new InvalidDnDOperationException(getExceptionMessage(true));
/*     */   }
/*     */ 
/*     */   private static String getExceptionMessage(boolean paramBoolean)
/*     */   {
/* 385 */     return paramBoolean ? "Drag and drop in progress" : "No drag in progress";
/*     */   }
/*     */ 
/*     */   public static int convertModifiersToDropAction(int paramInt1, int paramInt2)
/*     */   {
/* 390 */     int i = 0;
/*     */ 
/* 403 */     switch (paramInt1 & 0xC0)
/*     */     {
/*     */     case 192:
/* 406 */       i = 1073741824; break;
/*     */     case 128:
/* 408 */       i = 1; break;
/*     */     case 64:
/* 410 */       i = 2; break;
/*     */     default:
/* 412 */       if ((paramInt2 & 0x2) != 0)
/* 413 */         i = 2;
/* 414 */       else if ((paramInt2 & 0x1) != 0)
/* 415 */         i = 1;
/* 416 */       else if ((paramInt2 & 0x40000000) != 0) {
/* 417 */         i = 1073741824;
/*     */       }
/*     */       break;
/*     */     }
/* 421 */     return i & paramInt2;
/*     */   }
/*     */ 
/*     */   private void cleanup() {
/* 425 */     this.trigger = null;
/* 426 */     this.component = null;
/* 427 */     this.cursor = null;
/* 428 */     this.dragSourceContext = null;
/* 429 */     SunDropTargetContextPeer.setCurrentJVMLocalSourceTransferable(null);
/* 430 */     setDragDropInProgress(false);
/*     */   }
/*     */ 
/*     */   private class EventDispatcher
/*     */     implements Runnable
/*     */   {
/*     */     private final int dispatchType;
/*     */     private final DragSourceEvent event;
/*     */ 
/*     */     EventDispatcher(int paramDragSourceEvent, DragSourceEvent arg3)
/*     */     {
/*     */       Object localObject;
/* 440 */       switch (paramDragSourceEvent) {
/*     */       case 1:
/*     */       case 2:
/*     */       case 3:
/*     */       case 6:
/* 445 */         if (!(localObject instanceof DragSourceDragEvent)) {
/* 446 */           throw new IllegalArgumentException("Event: " + localObject);
/*     */         }
/*     */         break;
/*     */       case 4:
/* 450 */         break;
/*     */       case 5:
/* 452 */         if (!(localObject instanceof DragSourceDropEvent)) {
/* 453 */           throw new IllegalArgumentException("Event: " + localObject);
/*     */         }
/*     */         break;
/*     */       default:
/* 457 */         throw new IllegalArgumentException("Dispatch type: " + paramDragSourceEvent);
/*     */       }
/*     */ 
/* 461 */       this.dispatchType = paramDragSourceEvent;
/* 462 */       this.event = localObject;
/*     */     }
/*     */ 
/*     */     public void run() {
/* 466 */       DragSourceContext localDragSourceContext = SunDragSourceContextPeer.this.getDragSourceContext();
/*     */       try
/*     */       {
/* 469 */         switch (this.dispatchType) {
/*     */         case 1:
/* 471 */           localDragSourceContext.dragEnter((DragSourceDragEvent)this.event);
/* 472 */           break;
/*     */         case 2:
/* 474 */           localDragSourceContext.dragOver((DragSourceDragEvent)this.event);
/* 475 */           break;
/*     */         case 3:
/* 477 */           localDragSourceContext.dropActionChanged((DragSourceDragEvent)this.event);
/* 478 */           break;
/*     */         case 4:
/* 480 */           localDragSourceContext.dragExit(this.event);
/* 481 */           break;
/*     */         case 6:
/* 483 */           localDragSourceContext.dragMouseMoved((DragSourceDragEvent)this.event);
/* 484 */           break;
/*     */         case 5:
/*     */           try {
/* 487 */             localDragSourceContext.dragDropEnd((DragSourceDropEvent)this.event);
/*     */           } finally {
/* 489 */             SunDragSourceContextPeer.this.cleanup();
/*     */           }
/* 491 */           break;
/*     */         default:
/* 493 */           throw new IllegalStateException("Dispatch type: " + this.dispatchType);
/*     */         }
/*     */       }
/*     */       finally {
/* 497 */         SunDragSourceContextPeer.this.quitSecondaryEventLoop();
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.dnd.SunDragSourceContextPeer
 * JD-Core Version:    0.6.2
 */