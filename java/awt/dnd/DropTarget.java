/*     */ package java.awt.dnd;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.GraphicsEnvironment;
/*     */ import java.awt.HeadlessException;
/*     */ import java.awt.Insets;
/*     */ import java.awt.Point;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.Toolkit;
/*     */ import java.awt.datatransfer.FlavorMap;
/*     */ import java.awt.datatransfer.SystemFlavorMap;
/*     */ import java.awt.dnd.peer.DropTargetPeer;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.awt.peer.ComponentPeer;
/*     */ import java.awt.peer.LightweightPeer;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectInputStream.GetField;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.TooManyListenersException;
/*     */ import javax.swing.Timer;
/*     */ 
/*     */ public class DropTarget
/*     */   implements DropTargetListener, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -6283860791671019047L;
/* 805 */   private DropTargetContext dropTargetContext = createDropTargetContext();
/*     */   private Component component;
/*     */   private transient ComponentPeer componentPeer;
/*     */   private transient ComponentPeer nativePeer;
/* 832 */   int actions = 3;
/*     */ 
/* 839 */   boolean active = true;
/*     */   private transient DropTargetAutoScroller autoScroller;
/*     */   private transient DropTargetListener dtListener;
/*     */   private transient FlavorMap flavorMap;
/*     */ 
/*     */   public DropTarget(Component paramComponent, int paramInt, DropTargetListener paramDropTargetListener, boolean paramBoolean, FlavorMap paramFlavorMap)
/*     */     throws HeadlessException
/*     */   {
/*  94 */     if (GraphicsEnvironment.isHeadless()) {
/*  95 */       throw new HeadlessException();
/*     */     }
/*     */ 
/*  98 */     this.component = paramComponent;
/*     */ 
/* 100 */     setDefaultActions(paramInt);
/*     */ 
/* 102 */     if (paramDropTargetListener != null) try {
/* 103 */         addDropTargetListener(paramDropTargetListener);
/*     */       }
/*     */       catch (TooManyListenersException localTooManyListenersException)
/*     */       {
/*     */       }
/* 108 */     if (paramComponent != null) {
/* 109 */       paramComponent.setDropTarget(this);
/* 110 */       setActive(paramBoolean);
/*     */     }
/*     */ 
/* 113 */     if (paramFlavorMap != null)
/* 114 */       this.flavorMap = paramFlavorMap;
/*     */     else
/* 116 */       this.flavorMap = SystemFlavorMap.getDefaultFlavorMap();
/*     */   }
/*     */ 
/*     */   public DropTarget(Component paramComponent, int paramInt, DropTargetListener paramDropTargetListener, boolean paramBoolean)
/*     */     throws HeadlessException
/*     */   {
/* 141 */     this(paramComponent, paramInt, paramDropTargetListener, paramBoolean, null);
/*     */   }
/*     */ 
/*     */   public DropTarget()
/*     */     throws HeadlessException
/*     */   {
/* 151 */     this(null, 3, null, true, null);
/*     */   }
/*     */ 
/*     */   public DropTarget(Component paramComponent, DropTargetListener paramDropTargetListener)
/*     */     throws HeadlessException
/*     */   {
/* 169 */     this(paramComponent, 3, paramDropTargetListener, true, null);
/*     */   }
/*     */ 
/*     */   public DropTarget(Component paramComponent, int paramInt, DropTargetListener paramDropTargetListener)
/*     */     throws HeadlessException
/*     */   {
/* 189 */     this(paramComponent, paramInt, paramDropTargetListener, true);
/*     */   }
/*     */ 
/*     */   public synchronized void setComponent(Component paramComponent)
/*     */   {
/* 204 */     if ((this.component == paramComponent) || ((this.component != null) && (this.component.equals(paramComponent)))) {
/* 205 */       return;
/*     */     }
/*     */ 
/* 208 */     ComponentPeer localComponentPeer = null;
/*     */     Component localComponent;
/* 210 */     if ((localComponent = this.component) != null) {
/* 211 */       clearAutoscroll();
/*     */ 
/* 213 */       this.component = null;
/*     */ 
/* 215 */       if (this.componentPeer != null) {
/* 216 */         localComponentPeer = this.componentPeer;
/* 217 */         removeNotify(this.componentPeer);
/*     */       }
/*     */ 
/* 220 */       localComponent.setDropTarget(null);
/*     */     }
/*     */ 
/* 224 */     if ((this.component = paramComponent) != null) try {
/* 225 */         paramComponent.setDropTarget(this);
/*     */       } catch (Exception localException) {
/* 227 */         if (localComponent != null) {
/* 228 */           localComponent.setDropTarget(this);
/* 229 */           addNotify(localComponentPeer);
/*     */         }
/*     */       }
/*     */   }
/*     */ 
/*     */   public synchronized Component getComponent()
/*     */   {
/* 242 */     return this.component;
/*     */   }
/*     */ 
/*     */   public void setDefaultActions(int paramInt)
/*     */   {
/* 254 */     getDropTargetContext().setTargetActions(paramInt & 0x40000003);
/*     */   }
/*     */ 
/*     */   void doSetDefaultActions(int paramInt)
/*     */   {
/* 262 */     this.actions = paramInt;
/*     */   }
/*     */ 
/*     */   public int getDefaultActions()
/*     */   {
/* 273 */     return this.actions;
/*     */   }
/*     */ 
/*     */   public synchronized void setActive(boolean paramBoolean)
/*     */   {
/* 284 */     if (paramBoolean != this.active) {
/* 285 */       this.active = paramBoolean;
/*     */     }
/*     */ 
/* 288 */     if (!this.active) clearAutoscroll();
/*     */   }
/*     */ 
/*     */   public boolean isActive()
/*     */   {
/* 300 */     return this.active;
/*     */   }
/*     */ 
/*     */   public synchronized void addDropTargetListener(DropTargetListener paramDropTargetListener)
/*     */     throws TooManyListenersException
/*     */   {
/* 314 */     if (paramDropTargetListener == null) return;
/*     */ 
/* 316 */     if (equals(paramDropTargetListener)) throw new IllegalArgumentException("DropTarget may not be its own Listener");
/*     */ 
/* 318 */     if (this.dtListener == null)
/* 319 */       this.dtListener = paramDropTargetListener;
/*     */     else
/* 321 */       throw new TooManyListenersException();
/*     */   }
/*     */ 
/*     */   public synchronized void removeDropTargetListener(DropTargetListener paramDropTargetListener)
/*     */   {
/* 331 */     if ((paramDropTargetListener != null) && (this.dtListener != null))
/* 332 */       if (this.dtListener.equals(paramDropTargetListener))
/* 333 */         this.dtListener = null;
/*     */       else
/* 335 */         throw new IllegalArgumentException("listener mismatch");
/*     */   }
/*     */ 
/*     */   public synchronized void dragEnter(DropTargetDragEvent paramDropTargetDragEvent)
/*     */   {
/* 354 */     if (!this.active) return;
/*     */ 
/* 356 */     if (this.dtListener != null)
/* 357 */       this.dtListener.dragEnter(paramDropTargetDragEvent);
/*     */     else {
/* 359 */       paramDropTargetDragEvent.getDropTargetContext().setTargetActions(0);
/*     */     }
/* 361 */     initializeAutoscrolling(paramDropTargetDragEvent.getLocation());
/*     */   }
/*     */ 
/*     */   public synchronized void dragOver(DropTargetDragEvent paramDropTargetDragEvent)
/*     */   {
/* 379 */     if (!this.active) return;
/*     */ 
/* 381 */     if ((this.dtListener != null) && (this.active)) this.dtListener.dragOver(paramDropTargetDragEvent);
/*     */ 
/* 383 */     updateAutoscroll(paramDropTargetDragEvent.getLocation());
/*     */   }
/*     */ 
/*     */   public synchronized void dropActionChanged(DropTargetDragEvent paramDropTargetDragEvent)
/*     */   {
/* 401 */     if (!this.active) return;
/*     */ 
/* 403 */     if (this.dtListener != null) this.dtListener.dropActionChanged(paramDropTargetDragEvent);
/*     */ 
/* 405 */     updateAutoscroll(paramDropTargetDragEvent.getLocation());
/*     */   }
/*     */ 
/*     */   public synchronized void dragExit(DropTargetEvent paramDropTargetEvent)
/*     */   {
/* 424 */     if (!this.active) return;
/*     */ 
/* 426 */     if ((this.dtListener != null) && (this.active)) this.dtListener.dragExit(paramDropTargetEvent);
/*     */ 
/* 428 */     clearAutoscroll();
/*     */   }
/*     */ 
/*     */   public synchronized void drop(DropTargetDropEvent paramDropTargetDropEvent)
/*     */   {
/* 447 */     clearAutoscroll();
/*     */ 
/* 449 */     if ((this.dtListener != null) && (this.active))
/* 450 */       this.dtListener.drop(paramDropTargetDropEvent);
/*     */     else
/* 452 */       paramDropTargetDropEvent.rejectDrop();
/*     */   }
/*     */ 
/*     */   public FlavorMap getFlavorMap()
/*     */   {
/* 466 */     return this.flavorMap;
/*     */   }
/*     */ 
/*     */   public void setFlavorMap(FlavorMap paramFlavorMap)
/*     */   {
/* 477 */     this.flavorMap = (paramFlavorMap == null ? SystemFlavorMap.getDefaultFlavorMap() : paramFlavorMap);
/*     */   }
/*     */ 
/*     */   public void addNotify(ComponentPeer paramComponentPeer)
/*     */   {
/* 498 */     if (paramComponentPeer == this.componentPeer) return;
/*     */ 
/* 500 */     this.componentPeer = paramComponentPeer;
/*     */ 
/* 502 */     for (Object localObject = this.component; 
/* 503 */       (localObject != null) && ((paramComponentPeer instanceof LightweightPeer)); localObject = ((Component)localObject).getParent()) {
/* 504 */       paramComponentPeer = ((Component)localObject).getPeer();
/*     */     }
/*     */ 
/* 507 */     if ((paramComponentPeer instanceof DropTargetPeer)) {
/* 508 */       this.nativePeer = paramComponentPeer;
/* 509 */       ((DropTargetPeer)paramComponentPeer).addDropTarget(this);
/*     */     } else {
/* 511 */       this.nativePeer = null;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void removeNotify(ComponentPeer paramComponentPeer)
/*     */   {
/* 532 */     if (this.nativePeer != null) {
/* 533 */       ((DropTargetPeer)this.nativePeer).removeDropTarget(this);
/*     */     }
/* 535 */     this.componentPeer = (this.nativePeer = null);
/*     */   }
/*     */ 
/*     */   public DropTargetContext getDropTargetContext()
/*     */   {
/* 546 */     return this.dropTargetContext;
/*     */   }
/*     */ 
/*     */   protected DropTargetContext createDropTargetContext()
/*     */   {
/* 561 */     return new DropTargetContext(this);
/*     */   }
/*     */ 
/*     */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*     */     throws IOException
/*     */   {
/* 576 */     paramObjectOutputStream.defaultWriteObject();
/*     */ 
/* 578 */     paramObjectOutputStream.writeObject(SerializationTester.test(this.dtListener) ? this.dtListener : null);
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream)
/*     */     throws ClassNotFoundException, IOException
/*     */   {
/* 597 */     ObjectInputStream.GetField localGetField = paramObjectInputStream.readFields();
/*     */     try
/*     */     {
/* 600 */       this.dropTargetContext = ((DropTargetContext)localGetField.get("dropTargetContext", null));
/*     */     }
/*     */     catch (IllegalArgumentException localIllegalArgumentException1)
/*     */     {
/*     */     }
/* 605 */     if (this.dropTargetContext == null) {
/* 606 */       this.dropTargetContext = createDropTargetContext();
/*     */     }
/*     */ 
/* 609 */     this.component = ((Component)localGetField.get("component", null));
/* 610 */     this.actions = localGetField.get("actions", 3);
/* 611 */     this.active = localGetField.get("active", true);
/*     */     try
/*     */     {
/* 615 */       this.dtListener = ((Serializable)localGetField.get("dtListener", null));
/*     */     }
/*     */     catch (IllegalArgumentException localIllegalArgumentException2) {
/* 618 */       this.dtListener = ((Serializable)paramObjectInputStream.readObject());
/*     */     }
/*     */   }
/*     */ 
/*     */   protected DropTargetAutoScroller createDropTargetAutoScroller(Component paramComponent, Point paramPoint)
/*     */   {
/* 764 */     return new DropTargetAutoScroller(paramComponent, paramPoint);
/*     */   }
/*     */ 
/*     */   protected void initializeAutoscrolling(Point paramPoint)
/*     */   {
/* 774 */     if ((this.component == null) || (!(this.component instanceof Autoscroll))) return;
/*     */ 
/* 776 */     this.autoScroller = createDropTargetAutoScroller(this.component, paramPoint);
/*     */   }
/*     */ 
/*     */   protected void updateAutoscroll(Point paramPoint)
/*     */   {
/* 786 */     if (this.autoScroller != null) this.autoScroller.updateLocation(paramPoint);
/*     */   }
/*     */ 
/*     */   protected void clearAutoscroll()
/*     */   {
/* 794 */     if (this.autoScroller != null) {
/* 795 */       this.autoScroller.stop();
/* 796 */       this.autoScroller = null;
/*     */     }
/*     */   }
/*     */ 
/*     */   protected static class DropTargetAutoScroller
/*     */     implements ActionListener
/*     */   {
/*     */     private Component component;
/*     */     private Autoscroll autoScroll;
/*     */     private Timer timer;
/*     */     private Point locn;
/*     */     private Point prev;
/* 748 */     private Rectangle outer = new Rectangle();
/* 749 */     private Rectangle inner = new Rectangle();
/*     */ 
/* 751 */     private int hysteresis = 10;
/*     */ 
/*     */     protected DropTargetAutoScroller(Component paramComponent, Point paramPoint)
/*     */     {
/* 640 */       this.component = paramComponent;
/* 641 */       this.autoScroll = ((Autoscroll)this.component);
/*     */ 
/* 643 */       Toolkit localToolkit = Toolkit.getDefaultToolkit();
/*     */ 
/* 645 */       Integer localInteger1 = Integer.valueOf(100);
/* 646 */       Integer localInteger2 = Integer.valueOf(100);
/*     */       try
/*     */       {
/* 649 */         localInteger1 = (Integer)localToolkit.getDesktopProperty("DnD.Autoscroll.initialDelay");
/*     */       }
/*     */       catch (Exception localException1)
/*     */       {
/*     */       }
/*     */       try {
/* 655 */         localInteger2 = (Integer)localToolkit.getDesktopProperty("DnD.Autoscroll.interval");
/*     */       }
/*     */       catch (Exception localException2)
/*     */       {
/*     */       }
/* 660 */       this.timer = new Timer(localInteger2.intValue(), this);
/*     */ 
/* 662 */       this.timer.setCoalesce(true);
/* 663 */       this.timer.setInitialDelay(localInteger1.intValue());
/*     */ 
/* 665 */       this.locn = paramPoint;
/* 666 */       this.prev = paramPoint;
/*     */       try
/*     */       {
/* 669 */         this.hysteresis = ((Integer)localToolkit.getDesktopProperty("DnD.Autoscroll.cursorHysteresis")).intValue();
/*     */       }
/*     */       catch (Exception localException3)
/*     */       {
/*     */       }
/* 674 */       this.timer.start();
/*     */     }
/*     */ 
/*     */     private void updateRegion()
/*     */     {
/* 682 */       Insets localInsets = this.autoScroll.getAutoscrollInsets();
/* 683 */       Dimension localDimension = this.component.getSize();
/*     */ 
/* 685 */       if ((localDimension.width != this.outer.width) || (localDimension.height != this.outer.height)) {
/* 686 */         this.outer.reshape(0, 0, localDimension.width, localDimension.height);
/*     */       }
/* 688 */       if ((this.inner.x != localInsets.left) || (this.inner.y != localInsets.top)) {
/* 689 */         this.inner.setLocation(localInsets.left, localInsets.top);
/*     */       }
/* 691 */       int i = localDimension.width - (localInsets.left + localInsets.right);
/* 692 */       int j = localDimension.height - (localInsets.top + localInsets.bottom);
/*     */ 
/* 694 */       if ((i != this.inner.width) || (j != this.inner.height))
/* 695 */         this.inner.setSize(i, j);
/*     */     }
/*     */ 
/*     */     protected synchronized void updateLocation(Point paramPoint)
/*     */     {
/* 706 */       this.prev = this.locn;
/* 707 */       this.locn = paramPoint;
/*     */ 
/* 709 */       if ((Math.abs(this.locn.x - this.prev.x) > this.hysteresis) || (Math.abs(this.locn.y - this.prev.y) > this.hysteresis))
/*     */       {
/* 711 */         if (this.timer.isRunning()) this.timer.stop();
/*     */       }
/* 713 */       else if (!this.timer.isRunning()) this.timer.start();
/*     */     }
/*     */ 
/*     */     protected void stop()
/*     */     {
/* 721 */       this.timer.stop();
/*     */     }
/*     */ 
/*     */     public synchronized void actionPerformed(ActionEvent paramActionEvent)
/*     */     {
/* 730 */       updateRegion();
/*     */ 
/* 732 */       if ((this.outer.contains(this.locn)) && (!this.inner.contains(this.locn)))
/* 733 */         this.autoScroll.autoscroll(this.locn);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.dnd.DropTarget
 * JD-Core Version:    0.6.2
 */