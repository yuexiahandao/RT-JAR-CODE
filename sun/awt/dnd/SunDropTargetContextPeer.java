/*     */ package sun.awt.dnd;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.Point;
/*     */ import java.awt.datatransfer.DataFlavor;
/*     */ import java.awt.datatransfer.Transferable;
/*     */ import java.awt.datatransfer.UnsupportedFlavorException;
/*     */ import java.awt.dnd.DropTarget;
/*     */ import java.awt.dnd.DropTargetContext;
/*     */ import java.awt.dnd.DropTargetDragEvent;
/*     */ import java.awt.dnd.DropTargetDropEvent;
/*     */ import java.awt.dnd.DropTargetEvent;
/*     */ import java.awt.dnd.DropTargetListener;
/*     */ import java.awt.dnd.InvalidDnDOperationException;
/*     */ import java.awt.dnd.peer.DropTargetContextPeer;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import sun.awt.AppContext;
/*     */ import sun.awt.SunToolkit;
/*     */ import sun.awt.datatransfer.DataTransferer;
/*     */ import sun.awt.datatransfer.ToolkitThreadBlockedHandler;
/*     */ import sun.util.logging.PlatformLogger;
/*     */ 
/*     */ public abstract class SunDropTargetContextPeer
/*     */   implements DropTargetContextPeer, Transferable
/*     */ {
/*     */   public static final boolean DISPATCH_SYNC = true;
/*     */   private DropTarget currentDT;
/*     */   private DropTargetContext currentDTC;
/*     */   private long[] currentT;
/*     */   private int currentA;
/*     */   private int currentSA;
/*     */   private int currentDA;
/*     */   private int previousDA;
/*     */   private long nativeDragContext;
/*     */   private Transferable local;
/*  92 */   private boolean dragRejected = false;
/*     */ 
/*  94 */   protected int dropStatus = 0;
/*  95 */   protected boolean dropComplete = false;
/*     */ 
/* 100 */   boolean dropInProcess = false;
/*     */ 
/* 106 */   protected static final Object _globalLock = new Object();
/*     */ 
/* 108 */   private static final PlatformLogger dndLog = PlatformLogger.getLogger("sun.awt.dnd.SunDropTargetContextPeer");
/*     */ 
/* 114 */   protected static Transferable currentJVMLocalSourceTransferable = null;
/*     */   protected static final int STATUS_NONE = 0;
/*     */   protected static final int STATUS_WAIT = 1;
/*     */   protected static final int STATUS_ACCEPT = 2;
/*     */   protected static final int STATUS_REJECT = -1;
/*     */ 
/*     */   public static void setCurrentJVMLocalSourceTransferable(Transferable paramTransferable)
/*     */     throws InvalidDnDOperationException
/*     */   {
/* 117 */     synchronized (_globalLock) {
/* 118 */       if ((paramTransferable != null) && (currentJVMLocalSourceTransferable != null)) {
/* 119 */         throw new InvalidDnDOperationException();
/*     */       }
/* 121 */       currentJVMLocalSourceTransferable = paramTransferable;
/*     */     }
/*     */   }
/*     */ 
/*     */   private static Transferable getJVMLocalSourceTransferable()
/*     */   {
/* 131 */     return currentJVMLocalSourceTransferable;
/*     */   }
/*     */ 
/*     */   public DropTarget getDropTarget()
/*     */   {
/* 155 */     return this.currentDT;
/*     */   }
/*     */ 
/*     */   public synchronized void setTargetActions(int paramInt)
/*     */   {
/* 162 */     this.currentA = (paramInt & 0x40000003);
/*     */   }
/*     */ 
/*     */   public int getTargetActions()
/*     */   {
/* 171 */     return this.currentA;
/*     */   }
/*     */ 
/*     */   public Transferable getTransferable()
/*     */   {
/* 179 */     return this;
/*     */   }
/*     */ 
/*     */   public DataFlavor[] getTransferDataFlavors()
/*     */   {
/* 189 */     Transferable localTransferable = this.local;
/*     */ 
/* 191 */     if (localTransferable != null) {
/* 192 */       return localTransferable.getTransferDataFlavors();
/*     */     }
/* 194 */     return DataTransferer.getInstance().getFlavorsForFormatsAsArray(this.currentT, DataTransferer.adaptFlavorMap(this.currentDT.getFlavorMap()));
/*     */   }
/*     */ 
/*     */   public boolean isDataFlavorSupported(DataFlavor paramDataFlavor)
/*     */   {
/* 205 */     Transferable localTransferable = this.local;
/*     */ 
/* 207 */     if (localTransferable != null) {
/* 208 */       return localTransferable.isDataFlavorSupported(paramDataFlavor);
/*     */     }
/* 210 */     return DataTransferer.getInstance().getFlavorsForFormats(this.currentT, DataTransferer.adaptFlavorMap(this.currentDT.getFlavorMap())).containsKey(paramDataFlavor);
/*     */   }
/*     */ 
/*     */   public Object getTransferData(DataFlavor paramDataFlavor)
/*     */     throws UnsupportedFlavorException, IOException, InvalidDnDOperationException
/*     */   {
/* 226 */     SecurityManager localSecurityManager = System.getSecurityManager();
/*     */     try {
/* 228 */       if ((!this.dropInProcess) && (localSecurityManager != null))
/* 229 */         localSecurityManager.checkSystemClipboardAccess();
/*     */     }
/*     */     catch (Exception localException) {
/* 232 */       localObject1 = Thread.currentThread();
/* 233 */       ((Thread)localObject1).getUncaughtExceptionHandler().uncaughtException((Thread)localObject1, localException);
/* 234 */       return null;
/*     */     }
/*     */ 
/* 237 */     Long localLong = null;
/* 238 */     Object localObject1 = this.local;
/*     */ 
/* 240 */     if (localObject1 != null)
/* 241 */       return ((Transferable)localObject1).getTransferData(paramDataFlavor);
/* 242 */     if (paramDataFlavor.isMimeTypeEqual("application/x-java-jvm-local-objectref"))
/*     */     {
/* 248 */       return null;
/*     */     }
/*     */ 
/* 251 */     if ((this.dropStatus != 2) || (this.dropComplete)) {
/* 252 */       throw new InvalidDnDOperationException("No drop current");
/*     */     }
/*     */ 
/* 255 */     Map localMap = DataTransferer.getInstance().getFlavorsForFormats(this.currentT, DataTransferer.adaptFlavorMap(this.currentDT.getFlavorMap()));
/*     */ 
/* 259 */     localLong = (Long)localMap.get(paramDataFlavor);
/* 260 */     if (localLong == null) {
/* 261 */       throw new UnsupportedFlavorException(paramDataFlavor);
/*     */     }
/*     */ 
/* 264 */     if ((paramDataFlavor.isRepresentationClassRemote()) && (this.currentDA != 1073741824))
/*     */     {
/* 266 */       throw new InvalidDnDOperationException("only ACTION_LINK is permissable for transfer of java.rmi.Remote objects");
/*     */     }
/*     */ 
/* 269 */     long l = localLong.longValue();
/* 270 */     Object localObject2 = getNativeData(l);
/*     */ 
/* 272 */     if ((localObject2 instanceof byte[]))
/*     */       try {
/* 274 */         return DataTransferer.getInstance().translateBytes((byte[])localObject2, paramDataFlavor, l, this);
/*     */       }
/*     */       catch (IOException localIOException1) {
/* 277 */         throw new InvalidDnDOperationException(localIOException1.getMessage());
/*     */       }
/* 279 */     if ((localObject2 instanceof InputStream)) {
/*     */       try {
/* 281 */         return DataTransferer.getInstance().translateStream((InputStream)localObject2, paramDataFlavor, l, this);
/*     */       }
/*     */       catch (IOException localIOException2) {
/* 284 */         throw new InvalidDnDOperationException(localIOException2.getMessage());
/*     */       }
/*     */     }
/* 287 */     throw new IOException("no native data was transfered");
/*     */   }
/*     */ 
/*     */   protected abstract Object getNativeData(long paramLong)
/*     */     throws IOException;
/*     */ 
/*     */   public boolean isTransferableJVMLocal()
/*     */   {
/* 298 */     return (this.local != null) || (getJVMLocalSourceTransferable() != null);
/*     */   }
/*     */ 
/*     */   private int handleEnterMessage(Component paramComponent, int paramInt1, int paramInt2, int paramInt3, int paramInt4, long[] paramArrayOfLong, long paramLong)
/*     */   {
/* 306 */     return postDropTargetEvent(paramComponent, paramInt1, paramInt2, paramInt3, paramInt4, paramArrayOfLong, paramLong, 504, true);
/*     */   }
/*     */ 
/*     */   protected void processEnterMessage(SunDropTargetEvent paramSunDropTargetEvent)
/*     */   {
/* 317 */     Component localComponent = (Component)paramSunDropTargetEvent.getSource();
/* 318 */     DropTarget localDropTarget = localComponent.getDropTarget();
/* 319 */     Point localPoint = paramSunDropTargetEvent.getPoint();
/*     */ 
/* 321 */     this.local = getJVMLocalSourceTransferable();
/*     */ 
/* 323 */     if (this.currentDTC != null) {
/* 324 */       this.currentDTC.removeNotify();
/* 325 */       this.currentDTC = null;
/*     */     }
/*     */ 
/* 328 */     if ((localComponent.isShowing()) && (localDropTarget != null) && (localDropTarget.isActive())) {
/* 329 */       this.currentDT = localDropTarget;
/* 330 */       this.currentDTC = this.currentDT.getDropTargetContext();
/*     */ 
/* 332 */       this.currentDTC.addNotify(this);
/*     */ 
/* 334 */       this.currentA = localDropTarget.getDefaultActions();
/*     */       try
/*     */       {
/* 337 */         localDropTarget.dragEnter(new DropTargetDragEvent(this.currentDTC, localPoint, this.currentDA, this.currentSA));
/*     */       }
/*     */       catch (Exception localException)
/*     */       {
/* 342 */         localException.printStackTrace();
/* 343 */         this.currentDA = 0;
/*     */       }
/*     */     } else {
/* 346 */       this.currentDT = null;
/* 347 */       this.currentDTC = null;
/* 348 */       this.currentDA = 0;
/* 349 */       this.currentSA = 0;
/* 350 */       this.currentA = 0;
/*     */     }
/*     */   }
/*     */ 
/*     */   private void handleExitMessage(Component paramComponent, long paramLong)
/*     */   {
/* 365 */     postDropTargetEvent(paramComponent, 0, 0, 0, 0, null, paramLong, 505, true);
/*     */   }
/*     */ 
/*     */   protected void processExitMessage(SunDropTargetEvent paramSunDropTargetEvent)
/*     */   {
/* 376 */     Component localComponent = (Component)paramSunDropTargetEvent.getSource();
/* 377 */     DropTarget localDropTarget = localComponent.getDropTarget();
/* 378 */     DropTargetContext localDropTargetContext = null;
/*     */ 
/* 380 */     if (localDropTarget == null) {
/* 381 */       this.currentDT = null;
/* 382 */       this.currentT = null;
/*     */ 
/* 384 */       if (this.currentDTC != null) {
/* 385 */         this.currentDTC.removeNotify();
/*     */       }
/*     */ 
/* 388 */       this.currentDTC = null;
/*     */ 
/* 390 */       return;
/*     */     }
/*     */ 
/* 393 */     if (localDropTarget != this.currentDT)
/*     */     {
/* 395 */       if (this.currentDTC != null) {
/* 396 */         this.currentDTC.removeNotify();
/*     */       }
/*     */ 
/* 399 */       this.currentDT = localDropTarget;
/* 400 */       this.currentDTC = localDropTarget.getDropTargetContext();
/*     */ 
/* 402 */       this.currentDTC.addNotify(this);
/*     */     }
/*     */ 
/* 405 */     localDropTargetContext = this.currentDTC;
/*     */ 
/* 407 */     if (localDropTarget.isActive()) try {
/* 408 */         localDropTarget.dragExit(new DropTargetEvent(localDropTargetContext));
/*     */       } catch (Exception localException) {
/* 410 */         localException.printStackTrace();
/*     */       } finally {
/* 412 */         this.currentA = 0;
/* 413 */         this.currentSA = 0;
/* 414 */         this.currentDA = 0;
/* 415 */         this.currentDT = null;
/* 416 */         this.currentT = null;
/*     */ 
/* 418 */         this.currentDTC.removeNotify();
/* 419 */         this.currentDTC = null;
/*     */ 
/* 421 */         this.local = null;
/*     */ 
/* 423 */         this.dragRejected = false;
/*     */       }
/*     */   }
/*     */ 
/*     */   private int handleMotionMessage(Component paramComponent, int paramInt1, int paramInt2, int paramInt3, int paramInt4, long[] paramArrayOfLong, long paramLong)
/*     */   {
/* 432 */     return postDropTargetEvent(paramComponent, paramInt1, paramInt2, paramInt3, paramInt4, paramArrayOfLong, paramLong, 506, true);
/*     */   }
/*     */ 
/*     */   protected void processMotionMessage(SunDropTargetEvent paramSunDropTargetEvent, boolean paramBoolean)
/*     */   {
/* 444 */     Component localComponent = (Component)paramSunDropTargetEvent.getSource();
/* 445 */     Point localPoint = paramSunDropTargetEvent.getPoint();
/* 446 */     int i = paramSunDropTargetEvent.getID();
/* 447 */     DropTarget localDropTarget1 = localComponent.getDropTarget();
/* 448 */     DropTargetContext localDropTargetContext = null;
/*     */ 
/* 450 */     if ((localComponent.isShowing()) && (localDropTarget1 != null) && (localDropTarget1.isActive())) {
/* 451 */       if (this.currentDT != localDropTarget1) {
/* 452 */         if (this.currentDTC != null) {
/* 453 */           this.currentDTC.removeNotify();
/*     */         }
/*     */ 
/* 456 */         this.currentDT = localDropTarget1;
/* 457 */         this.currentDTC = null;
/*     */       }
/*     */ 
/* 460 */       localDropTargetContext = this.currentDT.getDropTargetContext();
/* 461 */       if (localDropTargetContext != this.currentDTC) {
/* 462 */         if (this.currentDTC != null) {
/* 463 */           this.currentDTC.removeNotify();
/*     */         }
/*     */ 
/* 466 */         this.currentDTC = localDropTargetContext;
/* 467 */         this.currentDTC.addNotify(this);
/*     */       }
/*     */ 
/* 470 */       this.currentA = this.currentDT.getDefaultActions();
/*     */       try
/*     */       {
/* 473 */         DropTargetDragEvent localDropTargetDragEvent = new DropTargetDragEvent(localDropTargetContext, localPoint, this.currentDA, this.currentSA);
/*     */ 
/* 477 */         DropTarget localDropTarget2 = localDropTarget1;
/* 478 */         if (paramBoolean)
/* 479 */           localDropTarget2.dropActionChanged(localDropTargetDragEvent);
/*     */         else {
/* 481 */           localDropTarget2.dragOver(localDropTargetDragEvent);
/*     */         }
/*     */ 
/* 484 */         if (this.dragRejected)
/* 485 */           this.currentDA = 0;
/*     */       }
/*     */       catch (Exception localException) {
/* 488 */         localException.printStackTrace();
/* 489 */         this.currentDA = 0;
/*     */       }
/*     */     } else {
/* 492 */       this.currentDA = 0;
/*     */     }
/*     */   }
/*     */ 
/*     */   private void handleDropMessage(Component paramComponent, int paramInt1, int paramInt2, int paramInt3, int paramInt4, long[] paramArrayOfLong, long paramLong)
/*     */   {
/* 505 */     postDropTargetEvent(paramComponent, paramInt1, paramInt2, paramInt3, paramInt4, paramArrayOfLong, paramLong, 502, false);
/*     */   }
/*     */ 
/*     */   protected void processDropMessage(SunDropTargetEvent paramSunDropTargetEvent)
/*     */   {
/* 516 */     Component localComponent = (Component)paramSunDropTargetEvent.getSource();
/* 517 */     Point localPoint = paramSunDropTargetEvent.getPoint();
/* 518 */     DropTarget localDropTarget = localComponent.getDropTarget();
/*     */ 
/* 520 */     this.dropStatus = 1;
/* 521 */     this.dropComplete = false;
/*     */ 
/* 523 */     if ((localComponent.isShowing()) && (localDropTarget != null) && (localDropTarget.isActive())) {
/* 524 */       DropTargetContext localDropTargetContext = localDropTarget.getDropTargetContext();
/*     */ 
/* 526 */       this.currentDT = localDropTarget;
/*     */ 
/* 528 */       if (this.currentDTC != null) {
/* 529 */         this.currentDTC.removeNotify();
/*     */       }
/*     */ 
/* 532 */       this.currentDTC = localDropTargetContext;
/* 533 */       this.currentDTC.addNotify(this);
/* 534 */       this.currentA = localDropTarget.getDefaultActions();
/*     */ 
/* 536 */       synchronized (_globalLock) {
/* 537 */         if ((this.local = getJVMLocalSourceTransferable()) != null) {
/* 538 */           setCurrentJVMLocalSourceTransferable(null);
/*     */         }
/*     */       }
/* 541 */       this.dropInProcess = true;
/*     */       try
/*     */       {
/* 544 */         localDropTarget.drop(new DropTargetDropEvent(localDropTargetContext, localPoint, this.currentDA, this.currentSA, this.local != null));
/*     */       }
/*     */       finally
/*     */       {
/* 550 */         if (this.dropStatus == 1)
/* 551 */           rejectDrop();
/* 552 */         else if (!this.dropComplete) {
/* 553 */           dropComplete(false);
/*     */         }
/* 555 */         this.dropInProcess = false;
/*     */       }
/*     */     } else {
/* 558 */       rejectDrop();
/*     */     }
/*     */   }
/*     */ 
/*     */   protected int postDropTargetEvent(Component paramComponent, int paramInt1, int paramInt2, int paramInt3, int paramInt4, long[] paramArrayOfLong, long paramLong, int paramInt5, boolean paramBoolean)
/*     */   {
/* 570 */     AppContext localAppContext = SunToolkit.targetToAppContext(paramComponent);
/*     */ 
/* 572 */     EventDispatcher localEventDispatcher = new EventDispatcher(this, paramInt3, paramInt4, paramArrayOfLong, paramLong, paramBoolean);
/*     */ 
/* 576 */     SunDropTargetEvent localSunDropTargetEvent = new SunDropTargetEvent(paramComponent, paramInt5, paramInt1, paramInt2, localEventDispatcher);
/*     */ 
/* 579 */     if (paramBoolean == true) {
/* 580 */       DataTransferer.getInstance().getToolkitThreadBlockedHandler().lock();
/*     */     }
/*     */ 
/* 584 */     SunToolkit.postEvent(localAppContext, localSunDropTargetEvent);
/*     */ 
/* 586 */     eventPosted(localSunDropTargetEvent);
/*     */ 
/* 588 */     if (paramBoolean == true) {
/* 589 */       while (!localEventDispatcher.isDone()) {
/* 590 */         DataTransferer.getInstance().getToolkitThreadBlockedHandler().enter();
/*     */       }
/*     */ 
/* 593 */       DataTransferer.getInstance().getToolkitThreadBlockedHandler().unlock();
/*     */ 
/* 596 */       return localEventDispatcher.getReturnValue();
/*     */     }
/* 598 */     return 0;
/*     */   }
/*     */ 
/*     */   public synchronized void acceptDrag(int paramInt)
/*     */   {
/* 607 */     if (this.currentDT == null) {
/* 608 */       throw new InvalidDnDOperationException("No Drag pending");
/*     */     }
/* 610 */     this.currentDA = mapOperation(paramInt);
/* 611 */     if (this.currentDA != 0)
/* 612 */       this.dragRejected = false;
/*     */   }
/*     */ 
/*     */   public synchronized void rejectDrag()
/*     */   {
/* 621 */     if (this.currentDT == null) {
/* 622 */       throw new InvalidDnDOperationException("No Drag pending");
/*     */     }
/* 624 */     this.currentDA = 0;
/* 625 */     this.dragRejected = true;
/*     */   }
/*     */ 
/*     */   public synchronized void acceptDrop(int paramInt)
/*     */   {
/* 633 */     if (paramInt == 0) {
/* 634 */       throw new IllegalArgumentException("invalid acceptDrop() action");
/*     */     }
/* 636 */     if (this.dropStatus != 1) {
/* 637 */       throw new InvalidDnDOperationException("invalid acceptDrop()");
/*     */     }
/*     */ 
/* 640 */     this.currentDA = (this.currentA = mapOperation(paramInt & this.currentSA));
/*     */ 
/* 642 */     this.dropStatus = 2;
/* 643 */     this.dropComplete = false;
/*     */   }
/*     */ 
/*     */   public synchronized void rejectDrop()
/*     */   {
/* 651 */     if (this.dropStatus != 1) {
/* 652 */       throw new InvalidDnDOperationException("invalid rejectDrop()");
/*     */     }
/* 654 */     this.dropStatus = -1;
/*     */ 
/* 661 */     this.currentDA = 0;
/* 662 */     dropComplete(false);
/*     */   }
/*     */ 
/*     */   private int mapOperation(int paramInt)
/*     */   {
/* 670 */     int[] arrayOfInt = { 2, 1, 1073741824 };
/*     */ 
/* 675 */     int i = 0;
/*     */ 
/* 677 */     for (int j = 0; j < arrayOfInt.length; j++) {
/* 678 */       if ((paramInt & arrayOfInt[j]) == arrayOfInt[j]) {
/* 679 */         i = arrayOfInt[j];
/* 680 */         break;
/*     */       }
/*     */     }
/*     */ 
/* 684 */     return i;
/*     */   }
/*     */ 
/*     */   public synchronized void dropComplete(boolean paramBoolean)
/*     */   {
/* 692 */     if (this.dropStatus == 0) {
/* 693 */       throw new InvalidDnDOperationException("No Drop pending");
/*     */     }
/*     */ 
/* 696 */     if (this.currentDTC != null) this.currentDTC.removeNotify();
/*     */ 
/* 698 */     this.currentDT = null;
/* 699 */     this.currentDTC = null;
/* 700 */     this.currentT = null;
/* 701 */     this.currentA = 0;
/*     */ 
/* 703 */     synchronized (_globalLock) {
/* 704 */       currentJVMLocalSourceTransferable = null;
/*     */     }
/*     */ 
/* 707 */     this.dropStatus = 0;
/* 708 */     this.dropComplete = true;
/*     */     try
/*     */     {
/* 711 */       doDropDone(paramBoolean, this.currentDA, this.local != null);
/*     */     } finally {
/* 713 */       this.currentDA = 0;
/*     */ 
/* 716 */       this.nativeDragContext = 0L;
/*     */     }
/*     */   }
/*     */ 
/*     */   protected abstract void doDropDone(boolean paramBoolean1, int paramInt, boolean paramBoolean2);
/*     */ 
/*     */   protected synchronized long getNativeDragContext()
/*     */   {
/* 724 */     return this.nativeDragContext;
/*     */   }
/*     */ 
/*     */   protected void eventPosted(SunDropTargetEvent paramSunDropTargetEvent)
/*     */   {
/*     */   }
/*     */ 
/*     */   protected void eventProcessed(SunDropTargetEvent paramSunDropTargetEvent, int paramInt, boolean paramBoolean)
/*     */   {
/*     */   }
/*     */ 
/*     */   protected static class EventDispatcher
/*     */   {
/*     */     private final SunDropTargetContextPeer peer;
/*     */     private final int dropAction;
/*     */     private final int actions;
/*     */     private final long[] formats;
/*     */     private long nativeCtxt;
/*     */     private final boolean dispatchType;
/* 742 */     private boolean dispatcherDone = false;
/*     */ 
/* 745 */     private int returnValue = 0;
/*     */ 
/* 747 */     private final HashSet eventSet = new HashSet(3);
/*     */ 
/* 749 */     static final ToolkitThreadBlockedHandler handler = DataTransferer.getInstance().getToolkitThreadBlockedHandler();
/*     */ 
/*     */     EventDispatcher(SunDropTargetContextPeer paramSunDropTargetContextPeer, int paramInt1, int paramInt2, long[] paramArrayOfLong, long paramLong, boolean paramBoolean)
/*     */     {
/* 759 */       this.peer = paramSunDropTargetContextPeer;
/* 760 */       this.nativeCtxt = paramLong;
/* 761 */       this.dropAction = paramInt1;
/* 762 */       this.actions = paramInt2;
/* 763 */       this.formats = (null == paramArrayOfLong ? null : Arrays.copyOf(paramArrayOfLong, paramArrayOfLong.length));
/*     */ 
/* 765 */       this.dispatchType = paramBoolean;
/*     */     }
/*     */ 
/*     */     void dispatchEvent(SunDropTargetEvent paramSunDropTargetEvent) {
/* 769 */       int i = paramSunDropTargetEvent.getID();
/*     */ 
/* 771 */       switch (i) {
/*     */       case 504:
/* 773 */         dispatchEnterEvent(paramSunDropTargetEvent);
/* 774 */         break;
/*     */       case 506:
/* 776 */         dispatchMotionEvent(paramSunDropTargetEvent);
/* 777 */         break;
/*     */       case 505:
/* 779 */         dispatchExitEvent(paramSunDropTargetEvent);
/* 780 */         break;
/*     */       case 502:
/* 782 */         dispatchDropEvent(paramSunDropTargetEvent);
/* 783 */         break;
/*     */       case 503:
/*     */       default:
/* 785 */         throw new InvalidDnDOperationException();
/*     */       }
/*     */     }
/*     */ 
/*     */     private void dispatchEnterEvent(SunDropTargetEvent paramSunDropTargetEvent) {
/* 790 */       synchronized (this.peer)
/*     */       {
/* 793 */         this.peer.previousDA = this.dropAction;
/*     */ 
/* 796 */         this.peer.nativeDragContext = this.nativeCtxt;
/* 797 */         this.peer.currentT = this.formats;
/* 798 */         this.peer.currentSA = this.actions;
/* 799 */         this.peer.currentDA = this.dropAction;
/*     */ 
/* 801 */         this.peer.dropStatus = 2;
/* 802 */         this.peer.dropComplete = false;
/*     */         try
/*     */         {
/* 805 */           this.peer.processEnterMessage(paramSunDropTargetEvent);
/*     */         } finally {
/* 807 */           this.peer.dropStatus = 0;
/*     */         }
/*     */ 
/* 810 */         setReturnValue(this.peer.currentDA);
/*     */       }
/*     */     }
/*     */ 
/*     */     private void dispatchMotionEvent(SunDropTargetEvent paramSunDropTargetEvent) {
/* 815 */       synchronized (this.peer)
/*     */       {
/* 817 */         boolean bool = this.peer.previousDA != this.dropAction;
/* 818 */         this.peer.previousDA = this.dropAction;
/*     */ 
/* 821 */         this.peer.nativeDragContext = this.nativeCtxt;
/* 822 */         this.peer.currentT = this.formats;
/* 823 */         this.peer.currentSA = this.actions;
/* 824 */         this.peer.currentDA = this.dropAction;
/*     */ 
/* 826 */         this.peer.dropStatus = 2;
/* 827 */         this.peer.dropComplete = false;
/*     */         try
/*     */         {
/* 830 */           this.peer.processMotionMessage(paramSunDropTargetEvent, bool);
/*     */         } finally {
/* 832 */           this.peer.dropStatus = 0;
/*     */         }
/*     */ 
/* 835 */         setReturnValue(this.peer.currentDA);
/*     */       }
/*     */     }
/*     */ 
/*     */     private void dispatchExitEvent(SunDropTargetEvent paramSunDropTargetEvent) {
/* 840 */       synchronized (this.peer)
/*     */       {
/* 843 */         this.peer.nativeDragContext = this.nativeCtxt;
/*     */ 
/* 845 */         this.peer.processExitMessage(paramSunDropTargetEvent);
/*     */       }
/*     */     }
/*     */ 
/*     */     private void dispatchDropEvent(SunDropTargetEvent paramSunDropTargetEvent) {
/* 850 */       synchronized (this.peer)
/*     */       {
/* 853 */         this.peer.nativeDragContext = this.nativeCtxt;
/* 854 */         this.peer.currentT = this.formats;
/* 855 */         this.peer.currentSA = this.actions;
/* 856 */         this.peer.currentDA = this.dropAction;
/*     */ 
/* 858 */         this.peer.processDropMessage(paramSunDropTargetEvent);
/*     */       }
/*     */     }
/*     */ 
/*     */     void setReturnValue(int paramInt) {
/* 863 */       this.returnValue = paramInt;
/*     */     }
/*     */ 
/*     */     int getReturnValue() {
/* 867 */       return this.returnValue;
/*     */     }
/*     */ 
/*     */     boolean isDone() {
/* 871 */       return this.eventSet.isEmpty();
/*     */     }
/*     */ 
/*     */     void registerEvent(SunDropTargetEvent paramSunDropTargetEvent) {
/* 875 */       handler.lock();
/* 876 */       if ((!this.eventSet.add(paramSunDropTargetEvent)) && (SunDropTargetContextPeer.dndLog.isLoggable(500))) {
/* 877 */         SunDropTargetContextPeer.dndLog.fine("Event is already registered: " + paramSunDropTargetEvent);
/*     */       }
/* 879 */       handler.unlock();
/*     */     }
/*     */ 
/*     */     void unregisterEvent(SunDropTargetEvent paramSunDropTargetEvent) {
/* 883 */       handler.lock();
/*     */       try {
/* 885 */         if (!this.eventSet.remove(paramSunDropTargetEvent))
/*     */         {
/*     */           return;
/*     */         }
/* 889 */         if (this.eventSet.isEmpty()) {
/* 890 */           if ((!this.dispatcherDone) && (this.dispatchType == true)) {
/* 891 */             handler.exit();
/*     */           }
/* 893 */           this.dispatcherDone = true;
/*     */         }
/*     */       } finally {
/* 896 */         handler.unlock();
/*     */       }
/*     */       try
/*     */       {
/* 900 */         this.peer.eventProcessed(paramSunDropTargetEvent, this.returnValue, this.dispatcherDone);
/*     */       }
/*     */       finally
/*     */       {
/* 906 */         if (this.dispatcherDone) {
/* 907 */           this.nativeCtxt = 0L;
/*     */ 
/* 909 */           this.peer.nativeDragContext = 0L;
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/*     */     public void unregisterAllEvents()
/*     */     {
/* 916 */       Object[] arrayOfObject = null;
/* 917 */       handler.lock();
/*     */       try {
/* 919 */         arrayOfObject = this.eventSet.toArray();
/*     */       } finally {
/* 921 */         handler.unlock();
/*     */       }
/*     */ 
/* 924 */       if (arrayOfObject != null)
/* 925 */         for (int i = 0; i < arrayOfObject.length; i++)
/* 926 */           unregisterEvent((SunDropTargetEvent)arrayOfObject[i]);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.dnd.SunDropTargetContextPeer
 * JD-Core Version:    0.6.2
 */