/*     */ package java.awt.dnd;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.Cursor;
/*     */ import java.awt.GraphicsEnvironment;
/*     */ import java.awt.HeadlessException;
/*     */ import java.awt.Image;
/*     */ import java.awt.Point;
/*     */ import java.awt.Toolkit;
/*     */ import java.awt.datatransfer.FlavorMap;
/*     */ import java.awt.datatransfer.SystemFlavorMap;
/*     */ import java.awt.datatransfer.Transferable;
/*     */ import java.awt.dnd.peer.DragSourceContextPeer;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.security.AccessController;
/*     */ import java.util.EventListener;
/*     */ import sun.awt.dnd.SunDragSourceContextPeer;
/*     */ import sun.security.action.GetIntegerAction;
/*     */ 
/*     */ public class DragSource
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 6236096958971414066L;
/* 147 */   public static final Cursor DefaultCopyDrop = load("DnD.Cursor.CopyDrop");
/*     */ 
/* 157 */   public static final Cursor DefaultMoveDrop = load("DnD.Cursor.MoveDrop");
/*     */ 
/* 167 */   public static final Cursor DefaultLinkDrop = load("DnD.Cursor.LinkDrop");
/*     */ 
/* 177 */   public static final Cursor DefaultCopyNoDrop = load("DnD.Cursor.CopyNoDrop");
/*     */ 
/* 187 */   public static final Cursor DefaultMoveNoDrop = load("DnD.Cursor.MoveNoDrop");
/*     */ 
/* 197 */   public static final Cursor DefaultLinkNoDrop = load("DnD.Cursor.LinkNoDrop");
/*     */ 
/* 200 */   private static final DragSource dflt = GraphicsEnvironment.isHeadless() ? null : new DragSource();
/*     */   static final String dragSourceListenerK = "dragSourceL";
/*     */   static final String dragSourceMotionListenerK = "dragSourceMotionL";
/* 917 */   private transient FlavorMap flavorMap = SystemFlavorMap.getDefaultFlavorMap();
/*     */   private transient DragSourceListener listener;
/*     */   private transient DragSourceMotionListener motionListener;
/*     */ 
/*     */   private static Cursor load(String paramString)
/*     */   {
/* 126 */     if (GraphicsEnvironment.isHeadless()) {
/* 127 */       return null;
/*     */     }
/*     */     try
/*     */     {
/* 131 */       return (Cursor)Toolkit.getDefaultToolkit().getDesktopProperty(paramString);
/*     */     } catch (Exception localException) {
/* 133 */       localException.printStackTrace();
/*     */ 
/* 135 */       throw new RuntimeException("failed to load system cursor: " + paramString + " : " + localException.getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   public static DragSource getDefaultDragSource()
/*     */   {
/* 219 */     if (GraphicsEnvironment.isHeadless()) {
/* 220 */       throw new HeadlessException();
/*     */     }
/* 222 */     return dflt;
/*     */   }
/*     */ 
/*     */   public static boolean isDragImageSupported()
/*     */   {
/* 236 */     Toolkit localToolkit = Toolkit.getDefaultToolkit();
/*     */     try
/*     */     {
/* 241 */       Boolean localBoolean = (Boolean)Toolkit.getDefaultToolkit().getDesktopProperty("DnD.isDragImageSupported");
/*     */ 
/* 243 */       return localBoolean.booleanValue(); } catch (Exception localException) {
/*     */     }
/* 245 */     return false;
/*     */   }
/*     */ 
/*     */   public DragSource()
/*     */     throws HeadlessException
/*     */   {
/* 257 */     if (GraphicsEnvironment.isHeadless())
/* 258 */       throw new HeadlessException();
/*     */   }
/*     */ 
/*     */   public void startDrag(DragGestureEvent paramDragGestureEvent, Cursor paramCursor, Image paramImage, Point paramPoint, Transferable paramTransferable, DragSourceListener paramDragSourceListener, FlavorMap paramFlavorMap)
/*     */     throws InvalidDnDOperationException
/*     */   {
/* 301 */     SunDragSourceContextPeer.setDragDropInProgress(true);
/*     */     try
/*     */     {
/* 304 */       if (paramFlavorMap != null) this.flavorMap = paramFlavorMap;
/*     */ 
/* 306 */       DragSourceContextPeer localDragSourceContextPeer = Toolkit.getDefaultToolkit().createDragSourceContextPeer(paramDragGestureEvent);
/*     */ 
/* 308 */       DragSourceContext localDragSourceContext = createDragSourceContext(localDragSourceContextPeer, paramDragGestureEvent, paramCursor, paramImage, paramPoint, paramTransferable, paramDragSourceListener);
/*     */ 
/* 317 */       if (localDragSourceContext == null) {
/* 318 */         throw new InvalidDnDOperationException();
/*     */       }
/*     */ 
/* 321 */       localDragSourceContextPeer.startDrag(localDragSourceContext, localDragSourceContext.getCursor(), paramImage, paramPoint);
/*     */     } catch (RuntimeException localRuntimeException) {
/* 323 */       SunDragSourceContextPeer.setDragDropInProgress(false);
/* 324 */       throw localRuntimeException;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void startDrag(DragGestureEvent paramDragGestureEvent, Cursor paramCursor, Transferable paramTransferable, DragSourceListener paramDragSourceListener, FlavorMap paramFlavorMap)
/*     */     throws InvalidDnDOperationException
/*     */   {
/* 358 */     startDrag(paramDragGestureEvent, paramCursor, null, null, paramTransferable, paramDragSourceListener, paramFlavorMap);
/*     */   }
/*     */ 
/*     */   public void startDrag(DragGestureEvent paramDragGestureEvent, Cursor paramCursor, Image paramImage, Point paramPoint, Transferable paramTransferable, DragSourceListener paramDragSourceListener)
/*     */     throws InvalidDnDOperationException
/*     */   {
/* 396 */     startDrag(paramDragGestureEvent, paramCursor, paramImage, paramPoint, paramTransferable, paramDragSourceListener, null);
/*     */   }
/*     */ 
/*     */   public void startDrag(DragGestureEvent paramDragGestureEvent, Cursor paramCursor, Transferable paramTransferable, DragSourceListener paramDragSourceListener)
/*     */     throws InvalidDnDOperationException
/*     */   {
/* 426 */     startDrag(paramDragGestureEvent, paramCursor, null, null, paramTransferable, paramDragSourceListener, null);
/*     */   }
/*     */ 
/*     */   protected DragSourceContext createDragSourceContext(DragSourceContextPeer paramDragSourceContextPeer, DragGestureEvent paramDragGestureEvent, Cursor paramCursor, Image paramImage, Point paramPoint, Transferable paramTransferable, DragSourceListener paramDragSourceListener)
/*     */   {
/* 477 */     return new DragSourceContext(paramDragSourceContextPeer, paramDragGestureEvent, paramCursor, paramImage, paramPoint, paramTransferable, paramDragSourceListener);
/*     */   }
/*     */ 
/*     */   public FlavorMap getFlavorMap()
/*     */   {
/* 487 */     return this.flavorMap;
/*     */   }
/*     */ 
/*     */   public <T extends DragGestureRecognizer> T createDragGestureRecognizer(Class<T> paramClass, Component paramComponent, int paramInt, DragGestureListener paramDragGestureListener)
/*     */   {
/* 515 */     return Toolkit.getDefaultToolkit().createDragGestureRecognizer(paramClass, this, paramComponent, paramInt, paramDragGestureListener);
/*     */   }
/*     */ 
/*     */   public DragGestureRecognizer createDefaultDragGestureRecognizer(Component paramComponent, int paramInt, DragGestureListener paramDragGestureListener)
/*     */   {
/* 543 */     return Toolkit.getDefaultToolkit().createDragGestureRecognizer(MouseDragGestureRecognizer.class, this, paramComponent, paramInt, paramDragGestureListener);
/*     */   }
/*     */ 
/*     */   public void addDragSourceListener(DragSourceListener paramDragSourceListener)
/*     */   {
/* 560 */     if (paramDragSourceListener != null)
/* 561 */       synchronized (this) {
/* 562 */         this.listener = DnDEventMulticaster.add(this.listener, paramDragSourceListener);
/*     */       }
/*     */   }
/*     */ 
/*     */   public void removeDragSourceListener(DragSourceListener paramDragSourceListener)
/*     */   {
/* 583 */     if (paramDragSourceListener != null)
/* 584 */       synchronized (this) {
/* 585 */         this.listener = DnDEventMulticaster.remove(this.listener, paramDragSourceListener);
/*     */       }
/*     */   }
/*     */ 
/*     */   public DragSourceListener[] getDragSourceListeners()
/*     */   {
/* 603 */     return (DragSourceListener[])getListeners(DragSourceListener.class);
/*     */   }
/*     */ 
/*     */   public void addDragSourceMotionListener(DragSourceMotionListener paramDragSourceMotionListener)
/*     */   {
/* 620 */     if (paramDragSourceMotionListener != null)
/* 621 */       synchronized (this) {
/* 622 */         this.motionListener = DnDEventMulticaster.add(this.motionListener, paramDragSourceMotionListener);
/*     */       }
/*     */   }
/*     */ 
/*     */   public void removeDragSourceMotionListener(DragSourceMotionListener paramDragSourceMotionListener)
/*     */   {
/* 643 */     if (paramDragSourceMotionListener != null)
/* 644 */       synchronized (this) {
/* 645 */         this.motionListener = DnDEventMulticaster.remove(this.motionListener, paramDragSourceMotionListener);
/*     */       }
/*     */   }
/*     */ 
/*     */   public DragSourceMotionListener[] getDragSourceMotionListeners()
/*     */   {
/* 663 */     return (DragSourceMotionListener[])getListeners(DragSourceMotionListener.class);
/*     */   }
/*     */ 
/*     */   public <T extends EventListener> T[] getListeners(Class<T> paramClass)
/*     */   {
/* 689 */     Object localObject = null;
/* 690 */     if (paramClass == DragSourceListener.class)
/* 691 */       localObject = this.listener;
/* 692 */     else if (paramClass == DragSourceMotionListener.class) {
/* 693 */       localObject = this.motionListener;
/*     */     }
/* 695 */     return DnDEventMulticaster.getListeners((EventListener)localObject, paramClass);
/*     */   }
/*     */ 
/*     */   void processDragEnter(DragSourceDragEvent paramDragSourceDragEvent)
/*     */   {
/* 707 */     DragSourceListener localDragSourceListener = this.listener;
/* 708 */     if (localDragSourceListener != null)
/* 709 */       localDragSourceListener.dragEnter(paramDragSourceDragEvent);
/*     */   }
/*     */ 
/*     */   void processDragOver(DragSourceDragEvent paramDragSourceDragEvent)
/*     */   {
/* 722 */     DragSourceListener localDragSourceListener = this.listener;
/* 723 */     if (localDragSourceListener != null)
/* 724 */       localDragSourceListener.dragOver(paramDragSourceDragEvent);
/*     */   }
/*     */ 
/*     */   void processDropActionChanged(DragSourceDragEvent paramDragSourceDragEvent)
/*     */   {
/* 737 */     DragSourceListener localDragSourceListener = this.listener;
/* 738 */     if (localDragSourceListener != null)
/* 739 */       localDragSourceListener.dropActionChanged(paramDragSourceDragEvent);
/*     */   }
/*     */ 
/*     */   void processDragExit(DragSourceEvent paramDragSourceEvent)
/*     */   {
/* 752 */     DragSourceListener localDragSourceListener = this.listener;
/* 753 */     if (localDragSourceListener != null)
/* 754 */       localDragSourceListener.dragExit(paramDragSourceEvent);
/*     */   }
/*     */ 
/*     */   void processDragDropEnd(DragSourceDropEvent paramDragSourceDropEvent)
/*     */   {
/* 767 */     DragSourceListener localDragSourceListener = this.listener;
/* 768 */     if (localDragSourceListener != null)
/* 769 */       localDragSourceListener.dragDropEnd(paramDragSourceDropEvent);
/*     */   }
/*     */ 
/*     */   void processDragMouseMoved(DragSourceDragEvent paramDragSourceDragEvent)
/*     */   {
/* 782 */     DragSourceMotionListener localDragSourceMotionListener = this.motionListener;
/* 783 */     if (localDragSourceMotionListener != null)
/* 784 */       localDragSourceMotionListener.dragMouseMoved(paramDragSourceDragEvent);
/*     */   }
/*     */ 
/*     */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*     */     throws IOException
/*     */   {
/* 820 */     paramObjectOutputStream.defaultWriteObject();
/*     */ 
/* 822 */     paramObjectOutputStream.writeObject(SerializationTester.test(this.flavorMap) ? this.flavorMap : null);
/*     */ 
/* 824 */     DnDEventMulticaster.save(paramObjectOutputStream, "dragSourceL", this.listener);
/* 825 */     DnDEventMulticaster.save(paramObjectOutputStream, "dragSourceMotionL", this.motionListener);
/* 826 */     paramObjectOutputStream.writeObject(null);
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream)
/*     */     throws ClassNotFoundException, IOException
/*     */   {
/* 856 */     paramObjectInputStream.defaultReadObject();
/*     */ 
/* 859 */     this.flavorMap = ((FlavorMap)paramObjectInputStream.readObject());
/*     */ 
/* 862 */     if (this.flavorMap == null)
/* 863 */       this.flavorMap = SystemFlavorMap.getDefaultFlavorMap();
/*     */     Object localObject;
/* 867 */     while (null != (localObject = paramObjectInputStream.readObject())) {
/* 868 */       String str = ((String)localObject).intern();
/*     */ 
/* 870 */       if ("dragSourceL" == str)
/* 871 */         addDragSourceListener((DragSourceListener)paramObjectInputStream.readObject());
/* 872 */       else if ("dragSourceMotionL" == str) {
/* 873 */         addDragSourceMotionListener((DragSourceMotionListener)paramObjectInputStream.readObject());
/*     */       }
/*     */       else
/*     */       {
/* 877 */         paramObjectInputStream.readObject();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public static int getDragThreshold()
/*     */   {
/* 899 */     int i = ((Integer)AccessController.doPrivileged(new GetIntegerAction("awt.dnd.drag.threshold", 0))).intValue();
/*     */ 
/* 901 */     if (i > 0) {
/* 902 */       return i;
/*     */     }
/* 904 */     Integer localInteger = (Integer)Toolkit.getDefaultToolkit().getDesktopProperty("DnD.gestureMotionThreshold");
/*     */ 
/* 906 */     if (localInteger != null) {
/* 907 */       return localInteger.intValue();
/*     */     }
/*     */ 
/* 910 */     return 5;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.dnd.DragSource
 * JD-Core Version:    0.6.2
 */