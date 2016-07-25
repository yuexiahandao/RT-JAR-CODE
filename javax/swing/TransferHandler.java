/*      */ package javax.swing;
/*      */ 
/*      */ import java.awt.Component;
/*      */ import java.awt.GraphicsEnvironment;
/*      */ import java.awt.Image;
/*      */ import java.awt.Insets;
/*      */ import java.awt.Point;
/*      */ import java.awt.Rectangle;
/*      */ import java.awt.Toolkit;
/*      */ import java.awt.datatransfer.Clipboard;
/*      */ import java.awt.datatransfer.DataFlavor;
/*      */ import java.awt.datatransfer.Transferable;
/*      */ import java.awt.datatransfer.UnsupportedFlavorException;
/*      */ import java.awt.dnd.DragGestureEvent;
/*      */ import java.awt.dnd.DragGestureListener;
/*      */ import java.awt.dnd.DragGestureRecognizer;
/*      */ import java.awt.dnd.DragSource;
/*      */ import java.awt.dnd.DragSourceContext;
/*      */ import java.awt.dnd.DragSourceDragEvent;
/*      */ import java.awt.dnd.DragSourceDropEvent;
/*      */ import java.awt.dnd.DragSourceEvent;
/*      */ import java.awt.dnd.DragSourceListener;
/*      */ import java.awt.dnd.DropTarget;
/*      */ import java.awt.dnd.DropTargetContext;
/*      */ import java.awt.dnd.DropTargetDragEvent;
/*      */ import java.awt.dnd.DropTargetDropEvent;
/*      */ import java.awt.dnd.DropTargetEvent;
/*      */ import java.awt.dnd.DropTargetListener;
/*      */ import java.awt.event.ActionEvent;
/*      */ import java.awt.event.ActionListener;
/*      */ import java.awt.event.InputEvent;
/*      */ import java.awt.event.MouseEvent;
/*      */ import java.beans.BeanInfo;
/*      */ import java.beans.IntrospectionException;
/*      */ import java.beans.Introspector;
/*      */ import java.beans.PropertyDescriptor;
/*      */ import java.io.IOException;
/*      */ import java.io.PrintStream;
/*      */ import java.io.Serializable;
/*      */ import java.lang.reflect.Method;
/*      */ import java.security.AccessControlContext;
/*      */ import java.security.AccessController;
/*      */ import java.security.PrivilegedAction;
/*      */ import java.util.TooManyListenersException;
/*      */ import javax.swing.event.EventListenerList;
/*      */ import javax.swing.plaf.UIResource;
/*      */ import javax.swing.text.JTextComponent;
/*      */ import sun.awt.AWTAccessor;
/*      */ import sun.awt.AWTAccessor.AWTEventAccessor;
/*      */ import sun.awt.AWTAccessor.ComponentAccessor;
/*      */ import sun.awt.AppContext;
/*      */ import sun.awt.SunToolkit;
/*      */ import sun.misc.JavaSecurityAccess;
/*      */ import sun.misc.SharedSecrets;
/*      */ import sun.reflect.misc.MethodUtil;
/*      */ import sun.swing.SwingAccessor;
/*      */ import sun.swing.SwingAccessor.JTextComponentAccessor;
/*      */ import sun.swing.SwingUtilities2;
/*      */ import sun.swing.UIAction;
/*      */ 
/*      */ public class TransferHandler
/*      */   implements Serializable
/*      */ {
/*      */   public static final int NONE = 0;
/*      */   public static final int COPY = 1;
/*      */   public static final int MOVE = 2;
/*      */   public static final int COPY_OR_MOVE = 3;
/*      */   public static final int LINK = 1073741824;
/*      */   private Image dragImage;
/*      */   private Point dragImageOffset;
/*      */   private String propertyName;
/* 1108 */   private static SwingDragGestureRecognizer recognizer = null;
/*      */ 
/* 1680 */   static final Action cutAction = new TransferAction("cut");
/* 1681 */   static final Action copyAction = new TransferAction("copy");
/* 1682 */   static final Action pasteAction = new TransferAction("paste");
/*      */ 
/*      */   public static Action getCutAction()
/*      */   {
/*  592 */     return cutAction;
/*      */   }
/*      */ 
/*      */   public static Action getCopyAction()
/*      */   {
/*  604 */     return copyAction;
/*      */   }
/*      */ 
/*      */   public static Action getPasteAction()
/*      */   {
/*  616 */     return pasteAction;
/*      */   }
/*      */ 
/*      */   public TransferHandler(String paramString)
/*      */   {
/*  630 */     this.propertyName = paramString;
/*      */   }
/*      */ 
/*      */   protected TransferHandler()
/*      */   {
/*  637 */     this(null);
/*      */   }
/*      */ 
/*      */   public void setDragImage(Image paramImage)
/*      */   {
/*  663 */     this.dragImage = paramImage;
/*      */   }
/*      */ 
/*      */   public Image getDragImage()
/*      */   {
/*  673 */     return this.dragImage;
/*      */   }
/*      */ 
/*      */   public void setDragImageOffset(Point paramPoint)
/*      */   {
/*  685 */     this.dragImageOffset = new Point(paramPoint);
/*      */   }
/*      */ 
/*      */   public Point getDragImageOffset()
/*      */   {
/*  697 */     if (this.dragImageOffset == null) {
/*  698 */       return new Point(0, 0);
/*      */     }
/*  700 */     return new Point(this.dragImageOffset);
/*      */   }
/*      */ 
/*      */   public void exportAsDrag(JComponent paramJComponent, InputEvent paramInputEvent, int paramInt)
/*      */   {
/*  728 */     int i = getSourceActions(paramJComponent);
/*      */ 
/*  731 */     if ((!(paramInputEvent instanceof MouseEvent)) || ((paramInt != 1) && (paramInt != 2) && (paramInt != 1073741824)) || ((i & paramInt) == 0))
/*      */     {
/*  737 */       paramInt = 0;
/*      */     }
/*      */ 
/*  740 */     if ((paramInt != 0) && (!GraphicsEnvironment.isHeadless())) {
/*  741 */       if (recognizer == null) {
/*  742 */         recognizer = new SwingDragGestureRecognizer(new DragHandler(null));
/*      */       }
/*  744 */       recognizer.gestured(paramJComponent, (MouseEvent)paramInputEvent, i, paramInt);
/*      */     } else {
/*  746 */       exportDone(paramJComponent, null, 0);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void exportToClipboard(JComponent paramJComponent, Clipboard paramClipboard, int paramInt)
/*      */     throws IllegalStateException
/*      */   {
/*  781 */     if (((paramInt == 1) || (paramInt == 2)) && ((getSourceActions(paramJComponent) & paramInt) != 0))
/*      */     {
/*  784 */       Transferable localTransferable = createTransferable(paramJComponent);
/*  785 */       if (localTransferable != null) {
/*      */         try {
/*  787 */           paramClipboard.setContents(localTransferable, null);
/*  788 */           exportDone(paramJComponent, localTransferable, paramInt);
/*  789 */           return;
/*      */         } catch (IllegalStateException localIllegalStateException) {
/*  791 */           exportDone(paramJComponent, localTransferable, 0);
/*  792 */           throw localIllegalStateException;
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  797 */     exportDone(paramJComponent, null, 0);
/*      */   }
/*      */ 
/*      */   public boolean importData(TransferSupport paramTransferSupport)
/*      */   {
/*  826 */     return (paramTransferSupport.getComponent() instanceof JComponent) ? importData((JComponent)paramTransferSupport.getComponent(), paramTransferSupport.getTransferable()) : false;
/*      */   }
/*      */ 
/*      */   public boolean importData(JComponent paramJComponent, Transferable paramTransferable)
/*      */   {
/*  851 */     PropertyDescriptor localPropertyDescriptor = getPropertyDescriptor(paramJComponent);
/*  852 */     if (localPropertyDescriptor != null) {
/*  853 */       Method localMethod = localPropertyDescriptor.getWriteMethod();
/*  854 */       if (localMethod == null)
/*      */       {
/*  856 */         return false;
/*      */       }
/*  858 */       Class[] arrayOfClass = localMethod.getParameterTypes();
/*  859 */       if (arrayOfClass.length != 1)
/*      */       {
/*  861 */         return false;
/*      */       }
/*  863 */       DataFlavor localDataFlavor = getPropertyDataFlavor(arrayOfClass[0], paramTransferable.getTransferDataFlavors());
/*  864 */       if (localDataFlavor != null) {
/*      */         try {
/*  866 */           Object localObject = paramTransferable.getTransferData(localDataFlavor);
/*  867 */           Object[] arrayOfObject = { localObject };
/*  868 */           MethodUtil.invoke(localMethod, paramJComponent, arrayOfObject);
/*  869 */           return true;
/*      */         } catch (Exception localException) {
/*  871 */           System.err.println("Invocation failed");
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  876 */     return false;
/*      */   }
/*      */ 
/*      */   public boolean canImport(TransferSupport paramTransferSupport)
/*      */   {
/*  925 */     return (paramTransferSupport.getComponent() instanceof JComponent) ? canImport((JComponent)paramTransferSupport.getComponent(), paramTransferSupport.getDataFlavors()) : false;
/*      */   }
/*      */ 
/*      */   public boolean canImport(JComponent paramJComponent, DataFlavor[] paramArrayOfDataFlavor)
/*      */   {
/*  949 */     PropertyDescriptor localPropertyDescriptor = getPropertyDescriptor(paramJComponent);
/*  950 */     if (localPropertyDescriptor != null) {
/*  951 */       Method localMethod = localPropertyDescriptor.getWriteMethod();
/*  952 */       if (localMethod == null)
/*      */       {
/*  954 */         return false;
/*      */       }
/*  956 */       Class[] arrayOfClass = localMethod.getParameterTypes();
/*  957 */       if (arrayOfClass.length != 1)
/*      */       {
/*  959 */         return false;
/*      */       }
/*  961 */       DataFlavor localDataFlavor = getPropertyDataFlavor(arrayOfClass[0], paramArrayOfDataFlavor);
/*  962 */       if (localDataFlavor != null) {
/*  963 */         return true;
/*      */       }
/*      */     }
/*  966 */     return false;
/*      */   }
/*      */ 
/*      */   public int getSourceActions(JComponent paramJComponent)
/*      */   {
/*  984 */     PropertyDescriptor localPropertyDescriptor = getPropertyDescriptor(paramJComponent);
/*  985 */     if (localPropertyDescriptor != null) {
/*  986 */       return 1;
/*      */     }
/*  988 */     return 0;
/*      */   }
/*      */ 
/*      */   public Icon getVisualRepresentation(Transferable paramTransferable)
/*      */   {
/* 1013 */     return null;
/*      */   }
/*      */ 
/*      */   protected Transferable createTransferable(JComponent paramJComponent)
/*      */   {
/* 1030 */     PropertyDescriptor localPropertyDescriptor = getPropertyDescriptor(paramJComponent);
/* 1031 */     if (localPropertyDescriptor != null) {
/* 1032 */       return new PropertyTransferable(localPropertyDescriptor, paramJComponent);
/*      */     }
/* 1034 */     return null;
/*      */   }
/*      */ 
/*      */   protected void exportDone(JComponent paramJComponent, Transferable paramTransferable, int paramInt)
/*      */   {
/*      */   }
/*      */ 
/*      */   private PropertyDescriptor getPropertyDescriptor(JComponent paramJComponent)
/*      */   {
/* 1060 */     if (this.propertyName == null) {
/* 1061 */       return null;
/* 1063 */     }Class localClass = paramJComponent.getClass();
/*      */     BeanInfo localBeanInfo;
/*      */     try {
/* 1066 */       localBeanInfo = Introspector.getBeanInfo(localClass);
/*      */     } catch (IntrospectionException localIntrospectionException) {
/* 1068 */       return null;
/*      */     }
/* 1070 */     PropertyDescriptor[] arrayOfPropertyDescriptor = localBeanInfo.getPropertyDescriptors();
/* 1071 */     for (int i = 0; i < arrayOfPropertyDescriptor.length; i++) {
/* 1072 */       if (this.propertyName.equals(arrayOfPropertyDescriptor[i].getName())) {
/* 1073 */         Method localMethod = arrayOfPropertyDescriptor[i].getReadMethod();
/*      */ 
/* 1075 */         if (localMethod != null) {
/* 1076 */           Class[] arrayOfClass = localMethod.getParameterTypes();
/*      */ 
/* 1078 */           if ((arrayOfClass == null) || (arrayOfClass.length == 0))
/*      */           {
/* 1080 */             return arrayOfPropertyDescriptor[i];
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/* 1085 */     return null;
/*      */   }
/*      */ 
/*      */   private DataFlavor getPropertyDataFlavor(Class<?> paramClass, DataFlavor[] paramArrayOfDataFlavor)
/*      */   {
/* 1094 */     for (int i = 0; i < paramArrayOfDataFlavor.length; i++) {
/* 1095 */       DataFlavor localDataFlavor = paramArrayOfDataFlavor[i];
/* 1096 */       if (("application".equals(localDataFlavor.getPrimaryType())) && ("x-java-jvm-local-objectref".equals(localDataFlavor.getSubType())) && (paramClass.isAssignableFrom(localDataFlavor.getRepresentationClass())))
/*      */       {
/* 1100 */         return localDataFlavor;
/*      */       }
/*      */     }
/* 1103 */     return null;
/*      */   }
/*      */ 
/*      */   private static DropTargetListener getDropTargetListener()
/*      */   {
/* 1111 */     synchronized (DropHandler.class) {
/* 1112 */       DropHandler localDropHandler = (DropHandler)AppContext.getAppContext().get(DropHandler.class);
/*      */ 
/* 1115 */       if (localDropHandler == null) {
/* 1116 */         localDropHandler = new DropHandler(null);
/* 1117 */         AppContext.getAppContext().put(DropHandler.class, localDropHandler);
/*      */       }
/*      */ 
/* 1120 */       return localDropHandler;
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class DragHandler
/*      */     implements DragGestureListener, DragSourceListener
/*      */   {
/*      */     private boolean scrolls;
/*      */ 
/*      */     public void dragGestureRecognized(DragGestureEvent paramDragGestureEvent)
/*      */     {
/* 1591 */       JComponent localJComponent = (JComponent)paramDragGestureEvent.getComponent();
/* 1592 */       TransferHandler localTransferHandler = localJComponent.getTransferHandler();
/* 1593 */       Transferable localTransferable = localTransferHandler.createTransferable(localJComponent);
/* 1594 */       if (localTransferable != null) {
/* 1595 */         this.scrolls = localJComponent.getAutoscrolls();
/* 1596 */         localJComponent.setAutoscrolls(false);
/*      */         try {
/* 1598 */           Image localImage = localTransferHandler.getDragImage();
/* 1599 */           if (localImage == null)
/* 1600 */             paramDragGestureEvent.startDrag(null, localTransferable, this);
/*      */           else {
/* 1602 */             paramDragGestureEvent.startDrag(null, localImage, localTransferHandler.getDragImageOffset(), localTransferable, this);
/*      */           }
/* 1604 */           return;
/*      */         } catch (RuntimeException localRuntimeException) {
/* 1606 */           localJComponent.setAutoscrolls(this.scrolls);
/*      */         }
/*      */       }
/*      */ 
/* 1610 */       localTransferHandler.exportDone(localJComponent, localTransferable, 0);
/*      */     }
/*      */ 
/*      */     public void dragEnter(DragSourceDragEvent paramDragSourceDragEvent)
/*      */     {
/*      */     }
/*      */ 
/*      */     public void dragOver(DragSourceDragEvent paramDragSourceDragEvent)
/*      */     {
/*      */     }
/*      */ 
/*      */     public void dragExit(DragSourceEvent paramDragSourceEvent)
/*      */     {
/*      */     }
/*      */ 
/*      */     public void dragDropEnd(DragSourceDropEvent paramDragSourceDropEvent)
/*      */     {
/* 1637 */       DragSourceContext localDragSourceContext = paramDragSourceDropEvent.getDragSourceContext();
/* 1638 */       JComponent localJComponent = (JComponent)localDragSourceContext.getComponent();
/* 1639 */       if (paramDragSourceDropEvent.getDropSuccess())
/* 1640 */         localJComponent.getTransferHandler().exportDone(localJComponent, localDragSourceContext.getTransferable(), paramDragSourceDropEvent.getDropAction());
/*      */       else {
/* 1642 */         localJComponent.getTransferHandler().exportDone(localJComponent, localDragSourceContext.getTransferable(), 0);
/*      */       }
/* 1644 */       localJComponent.setAutoscrolls(this.scrolls);
/*      */     }
/*      */ 
/*      */     public void dropActionChanged(DragSourceDragEvent paramDragSourceDragEvent)
/*      */     {
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class DropHandler
/*      */     implements DropTargetListener, Serializable, ActionListener
/*      */   {
/*      */     private Timer timer;
/*      */     private Point lastPosition;
/* 1306 */     private Rectangle outer = new Rectangle();
/* 1307 */     private Rectangle inner = new Rectangle();
/* 1308 */     private int hysteresis = 10;
/*      */     private Component component;
/*      */     private Object state;
/* 1312 */     private TransferHandler.TransferSupport support = new TransferHandler.TransferSupport(null, (DropTargetEvent)null, null);
/*      */     private static final int AUTOSCROLL_INSET = 10;
/*      */ 
/*      */     private void updateAutoscrollRegion(JComponent paramJComponent)
/*      */     {
/* 1331 */       Rectangle localRectangle = paramJComponent.getVisibleRect();
/* 1332 */       this.outer.setBounds(localRectangle.x, localRectangle.y, localRectangle.width, localRectangle.height);
/*      */ 
/* 1335 */       Insets localInsets = new Insets(0, 0, 0, 0);
/* 1336 */       if ((paramJComponent instanceof Scrollable)) {
/* 1337 */         int i = 20;
/*      */ 
/* 1339 */         if (localRectangle.width >= i) {
/* 1340 */           localInsets.left = (localInsets.right = 10);
/*      */         }
/*      */ 
/* 1343 */         if (localRectangle.height >= i) {
/* 1344 */           localInsets.top = (localInsets.bottom = 10);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1349 */       this.inner.setBounds(localRectangle.x + localInsets.left, localRectangle.y + localInsets.top, localRectangle.width - (localInsets.left + localInsets.right), localRectangle.height - (localInsets.top + localInsets.bottom));
/*      */     }
/*      */ 
/*      */     private void autoscroll(JComponent paramJComponent, Point paramPoint)
/*      */     {
/* 1362 */       if ((paramJComponent instanceof Scrollable)) {
/* 1363 */         Scrollable localScrollable = (Scrollable)paramJComponent;
/*      */         int i;
/*      */         Rectangle localRectangle;
/* 1364 */         if (paramPoint.y < this.inner.y)
/*      */         {
/* 1366 */           i = localScrollable.getScrollableUnitIncrement(this.outer, 1, -1);
/* 1367 */           localRectangle = new Rectangle(this.inner.x, this.outer.y - i, this.inner.width, i);
/* 1368 */           paramJComponent.scrollRectToVisible(localRectangle);
/* 1369 */         } else if (paramPoint.y > this.inner.y + this.inner.height)
/*      */         {
/* 1371 */           i = localScrollable.getScrollableUnitIncrement(this.outer, 1, 1);
/* 1372 */           localRectangle = new Rectangle(this.inner.x, this.outer.y + this.outer.height, this.inner.width, i);
/* 1373 */           paramJComponent.scrollRectToVisible(localRectangle);
/*      */         }
/*      */ 
/* 1376 */         if (paramPoint.x < this.inner.x)
/*      */         {
/* 1378 */           i = localScrollable.getScrollableUnitIncrement(this.outer, 0, -1);
/* 1379 */           localRectangle = new Rectangle(this.outer.x - i, this.inner.y, i, this.inner.height);
/* 1380 */           paramJComponent.scrollRectToVisible(localRectangle);
/* 1381 */         } else if (paramPoint.x > this.inner.x + this.inner.width)
/*      */         {
/* 1383 */           i = localScrollable.getScrollableUnitIncrement(this.outer, 0, 1);
/* 1384 */           localRectangle = new Rectangle(this.outer.x + this.outer.width, this.inner.y, i, this.inner.height);
/* 1385 */           paramJComponent.scrollRectToVisible(localRectangle);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     private void initPropertiesIfNecessary()
/*      */     {
/* 1395 */       if (this.timer == null) {
/* 1396 */         Toolkit localToolkit = Toolkit.getDefaultToolkit();
/*      */ 
/* 1399 */         Integer localInteger = (Integer)localToolkit.getDesktopProperty("DnD.Autoscroll.interval");
/*      */ 
/* 1402 */         this.timer = new Timer(localInteger == null ? 100 : localInteger.intValue(), this);
/*      */ 
/* 1404 */         localInteger = (Integer)localToolkit.getDesktopProperty("DnD.Autoscroll.initialDelay");
/*      */ 
/* 1407 */         this.timer.setInitialDelay(localInteger == null ? 100 : localInteger.intValue());
/*      */ 
/* 1409 */         localInteger = (Integer)localToolkit.getDesktopProperty("DnD.Autoscroll.cursorHysteresis");
/*      */ 
/* 1412 */         if (localInteger != null)
/* 1413 */           this.hysteresis = localInteger.intValue();
/*      */       }
/*      */     }
/*      */ 
/*      */     public void actionPerformed(ActionEvent paramActionEvent)
/*      */     {
/* 1425 */       updateAutoscrollRegion((JComponent)this.component);
/* 1426 */       if ((this.outer.contains(this.lastPosition)) && (!this.inner.contains(this.lastPosition)))
/* 1427 */         autoscroll((JComponent)this.component, this.lastPosition);
/*      */     }
/*      */ 
/*      */     private void setComponentDropLocation(TransferHandler.TransferSupport paramTransferSupport, boolean paramBoolean)
/*      */     {
/* 1436 */       TransferHandler.DropLocation localDropLocation = paramTransferSupport == null ? null : paramTransferSupport.getDropLocation();
/*      */ 
/* 1440 */       if (SunToolkit.isInstanceOf(this.component, "javax.swing.text.JTextComponent")) {
/* 1441 */         this.state = SwingAccessor.getJTextComponentAccessor().setDropLocation((JTextComponent)this.component, localDropLocation, this.state, paramBoolean);
/*      */       }
/* 1443 */       else if ((this.component instanceof JComponent))
/* 1444 */         this.state = ((JComponent)this.component).setDropLocation(localDropLocation, this.state, paramBoolean);
/*      */     }
/*      */ 
/*      */     private void handleDrag(DropTargetDragEvent paramDropTargetDragEvent)
/*      */     {
/* 1449 */       TransferHandler localTransferHandler = ((TransferHandler.HasGetTransferHandler)this.component).getTransferHandler();
/*      */ 
/* 1452 */       if (localTransferHandler == null) {
/* 1453 */         paramDropTargetDragEvent.rejectDrag();
/* 1454 */         setComponentDropLocation(null, false);
/* 1455 */         return;
/*      */       }
/*      */ 
/* 1458 */       TransferHandler.TransferSupport.access$400(this.support, this.component, paramDropTargetDragEvent);
/* 1459 */       boolean bool1 = localTransferHandler.canImport(this.support);
/*      */ 
/* 1461 */       if (bool1)
/* 1462 */         paramDropTargetDragEvent.acceptDrag(this.support.getDropAction());
/*      */       else {
/* 1464 */         paramDropTargetDragEvent.rejectDrag();
/*      */       }
/*      */ 
/* 1467 */       boolean bool2 = TransferHandler.TransferSupport.access$500(this.support) ? TransferHandler.TransferSupport.access$600(this.support) : bool1;
/*      */ 
/* 1471 */       setComponentDropLocation(bool2 ? this.support : null, false);
/*      */     }
/*      */ 
/*      */     public void dragEnter(DropTargetDragEvent paramDropTargetDragEvent) {
/* 1475 */       this.state = null;
/* 1476 */       this.component = paramDropTargetDragEvent.getDropTargetContext().getComponent();
/*      */ 
/* 1478 */       handleDrag(paramDropTargetDragEvent);
/*      */ 
/* 1480 */       if ((this.component instanceof JComponent)) {
/* 1481 */         this.lastPosition = paramDropTargetDragEvent.getLocation();
/* 1482 */         updateAutoscrollRegion((JComponent)this.component);
/* 1483 */         initPropertiesIfNecessary();
/*      */       }
/*      */     }
/*      */ 
/*      */     public void dragOver(DropTargetDragEvent paramDropTargetDragEvent) {
/* 1488 */       handleDrag(paramDropTargetDragEvent);
/*      */ 
/* 1490 */       if (!(this.component instanceof JComponent)) {
/* 1491 */         return;
/*      */       }
/*      */ 
/* 1494 */       Point localPoint = paramDropTargetDragEvent.getLocation();
/*      */ 
/* 1496 */       if ((Math.abs(localPoint.x - this.lastPosition.x) > this.hysteresis) || (Math.abs(localPoint.y - this.lastPosition.y) > this.hysteresis))
/*      */       {
/* 1499 */         if (this.timer.isRunning()) this.timer.stop();
/*      */       }
/* 1501 */       else if (!this.timer.isRunning()) this.timer.start();
/*      */ 
/* 1504 */       this.lastPosition = localPoint;
/*      */     }
/*      */ 
/*      */     public void dragExit(DropTargetEvent paramDropTargetEvent) {
/* 1508 */       cleanup(false);
/*      */     }
/*      */ 
/*      */     public void drop(DropTargetDropEvent paramDropTargetDropEvent) {
/* 1512 */       TransferHandler localTransferHandler = ((TransferHandler.HasGetTransferHandler)this.component).getTransferHandler();
/*      */ 
/* 1515 */       if (localTransferHandler == null) {
/* 1516 */         paramDropTargetDropEvent.rejectDrop();
/* 1517 */         cleanup(false);
/* 1518 */         return;
/*      */       }
/*      */ 
/* 1521 */       TransferHandler.TransferSupport.access$400(this.support, this.component, paramDropTargetDropEvent);
/* 1522 */       boolean bool1 = localTransferHandler.canImport(this.support);
/*      */ 
/* 1524 */       if (bool1) {
/* 1525 */         paramDropTargetDropEvent.acceptDrop(this.support.getDropAction());
/*      */ 
/* 1527 */         boolean bool2 = TransferHandler.TransferSupport.access$500(this.support) ? TransferHandler.TransferSupport.access$600(this.support) : bool1;
/*      */ 
/* 1531 */         setComponentDropLocation(bool2 ? this.support : null, false);
/*      */         boolean bool3;
/*      */         try
/*      */         {
/* 1536 */           bool3 = localTransferHandler.importData(this.support);
/*      */         } catch (RuntimeException localRuntimeException) {
/* 1538 */           bool3 = false;
/*      */         }
/*      */ 
/* 1541 */         paramDropTargetDropEvent.dropComplete(bool3);
/* 1542 */         cleanup(bool3);
/*      */       } else {
/* 1544 */         paramDropTargetDropEvent.rejectDrop();
/* 1545 */         cleanup(false);
/*      */       }
/*      */     }
/*      */ 
/*      */     public void dropActionChanged(DropTargetDragEvent paramDropTargetDragEvent)
/*      */     {
/* 1554 */       if (this.component == null) {
/* 1555 */         return;
/*      */       }
/*      */ 
/* 1558 */       handleDrag(paramDropTargetDragEvent);
/*      */     }
/*      */ 
/*      */     private void cleanup(boolean paramBoolean) {
/* 1562 */       setComponentDropLocation(null, paramBoolean);
/* 1563 */       if ((this.component instanceof JComponent)) {
/* 1564 */         ((JComponent)this.component).dndDone();
/*      */       }
/*      */ 
/* 1567 */       if (this.timer != null) {
/* 1568 */         this.timer.stop();
/*      */       }
/*      */ 
/* 1571 */       this.state = null;
/* 1572 */       this.component = null;
/* 1573 */       this.lastPosition = null;
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class DropLocation
/*      */   {
/*      */     private final Point dropPoint;
/*      */ 
/*      */     protected DropLocation(Point paramPoint)
/*      */     {
/*  163 */       if (paramPoint == null) {
/*  164 */         throw new IllegalArgumentException("Point cannot be null");
/*      */       }
/*      */ 
/*  167 */       this.dropPoint = new Point(paramPoint);
/*      */     }
/*      */ 
/*      */     public final Point getDropPoint()
/*      */     {
/*  177 */       return new Point(this.dropPoint);
/*      */     }
/*      */ 
/*      */     public String toString()
/*      */     {
/*  189 */       return getClass().getName() + "[dropPoint=" + this.dropPoint + "]";
/*      */     }
/*      */   }
/*      */ 
/*      */   static abstract interface HasGetTransferHandler
/*      */   {
/*      */     public abstract TransferHandler getTransferHandler();
/*      */   }
/*      */ 
/*      */   static class PropertyTransferable
/*      */     implements Transferable
/*      */   {
/*      */     JComponent component;
/*      */     PropertyDescriptor property;
/*      */ 
/*      */     PropertyTransferable(PropertyDescriptor paramPropertyDescriptor, JComponent paramJComponent)
/*      */     {
/* 1127 */       this.property = paramPropertyDescriptor;
/* 1128 */       this.component = paramJComponent;
/*      */     }
/*      */ 
/*      */     public DataFlavor[] getTransferDataFlavors()
/*      */     {
/* 1140 */       DataFlavor[] arrayOfDataFlavor = new DataFlavor[1];
/* 1141 */       Class localClass = this.property.getPropertyType();
/* 1142 */       String str = "application/x-java-jvm-local-objectref;class=" + localClass.getName();
/*      */       try {
/* 1144 */         arrayOfDataFlavor[0] = new DataFlavor(str);
/*      */       } catch (ClassNotFoundException localClassNotFoundException) {
/* 1146 */         arrayOfDataFlavor = new DataFlavor[0];
/*      */       }
/* 1148 */       return arrayOfDataFlavor;
/*      */     }
/*      */ 
/*      */     public boolean isDataFlavorSupported(DataFlavor paramDataFlavor)
/*      */     {
/* 1159 */       Class localClass = this.property.getPropertyType();
/* 1160 */       if (("application".equals(paramDataFlavor.getPrimaryType())) && ("x-java-jvm-local-objectref".equals(paramDataFlavor.getSubType())) && (paramDataFlavor.getRepresentationClass().isAssignableFrom(localClass)))
/*      */       {
/* 1164 */         return true;
/*      */       }
/* 1166 */       return false;
/*      */     }
/*      */ 
/*      */     public Object getTransferData(DataFlavor paramDataFlavor)
/*      */       throws UnsupportedFlavorException, IOException
/*      */     {
/* 1181 */       if (!isDataFlavorSupported(paramDataFlavor)) {
/* 1182 */         throw new UnsupportedFlavorException(paramDataFlavor);
/*      */       }
/* 1184 */       Method localMethod = this.property.getReadMethod();
/* 1185 */       Object localObject = null;
/*      */       try {
/* 1187 */         localObject = MethodUtil.invoke(localMethod, this.component, (Object[])null);
/*      */       } catch (Exception localException) {
/* 1189 */         throw new IOException("Property read failed: " + this.property.getName());
/*      */       }
/* 1191 */       return localObject;
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class SwingDragGestureRecognizer extends DragGestureRecognizer
/*      */   {
/*      */     SwingDragGestureRecognizer(DragGestureListener paramDragGestureListener)
/*      */     {
/* 1654 */       super(null, 0, paramDragGestureListener);
/*      */     }
/*      */ 
/*      */     void gestured(JComponent paramJComponent, MouseEvent paramMouseEvent, int paramInt1, int paramInt2) {
/* 1658 */       setComponent(paramJComponent);
/* 1659 */       setSourceActions(paramInt1);
/* 1660 */       appendEvent(paramMouseEvent);
/* 1661 */       fireDragGestureRecognized(paramInt2, paramMouseEvent.getPoint());
/*      */     }
/*      */ 
/*      */     protected void registerListeners()
/*      */     {
/*      */     }
/*      */ 
/*      */     protected void unregisterListeners()
/*      */     {
/*      */     }
/*      */   }
/*      */ 
/*      */   static class SwingDropTarget extends DropTarget
/*      */     implements UIResource
/*      */   {
/*      */     private EventListenerList listenerList;
/*      */ 
/*      */     SwingDropTarget(Component paramComponent)
/*      */     {
/* 1211 */       super(1073741827, null);
/*      */       try
/*      */       {
/* 1215 */         super.addDropTargetListener(TransferHandler.access$200());
/*      */       }
/*      */       catch (TooManyListenersException localTooManyListenersException)
/*      */       {
/*      */       }
/*      */     }
/*      */ 
/*      */     public void addDropTargetListener(DropTargetListener paramDropTargetListener) throws TooManyListenersException {
/* 1223 */       if (this.listenerList == null) {
/* 1224 */         this.listenerList = new EventListenerList();
/*      */       }
/* 1226 */       this.listenerList.add(DropTargetListener.class, paramDropTargetListener);
/*      */     }
/*      */ 
/*      */     public void removeDropTargetListener(DropTargetListener paramDropTargetListener) {
/* 1230 */       if (this.listenerList != null)
/* 1231 */         this.listenerList.remove(DropTargetListener.class, paramDropTargetListener);
/*      */     }
/*      */ 
/*      */     public void dragEnter(DropTargetDragEvent paramDropTargetDragEvent)
/*      */     {
/* 1238 */       super.dragEnter(paramDropTargetDragEvent);
/* 1239 */       if (this.listenerList != null) {
/* 1240 */         Object[] arrayOfObject = this.listenerList.getListenerList();
/* 1241 */         for (int i = arrayOfObject.length - 2; i >= 0; i -= 2)
/* 1242 */           if (arrayOfObject[i] == DropTargetListener.class)
/* 1243 */             ((DropTargetListener)arrayOfObject[(i + 1)]).dragEnter(paramDropTargetDragEvent);
/*      */       }
/*      */     }
/*      */ 
/*      */     public void dragOver(DropTargetDragEvent paramDropTargetDragEvent)
/*      */     {
/* 1250 */       super.dragOver(paramDropTargetDragEvent);
/* 1251 */       if (this.listenerList != null) {
/* 1252 */         Object[] arrayOfObject = this.listenerList.getListenerList();
/* 1253 */         for (int i = arrayOfObject.length - 2; i >= 0; i -= 2)
/* 1254 */           if (arrayOfObject[i] == DropTargetListener.class)
/* 1255 */             ((DropTargetListener)arrayOfObject[(i + 1)]).dragOver(paramDropTargetDragEvent);
/*      */       }
/*      */     }
/*      */ 
/*      */     public void dragExit(DropTargetEvent paramDropTargetEvent)
/*      */     {
/* 1262 */       super.dragExit(paramDropTargetEvent);
/* 1263 */       if (this.listenerList != null) {
/* 1264 */         Object[] arrayOfObject = this.listenerList.getListenerList();
/* 1265 */         for (int i = arrayOfObject.length - 2; i >= 0; i -= 2)
/* 1266 */           if (arrayOfObject[i] == DropTargetListener.class)
/* 1267 */             ((DropTargetListener)arrayOfObject[(i + 1)]).dragExit(paramDropTargetEvent);
/*      */       }
/*      */     }
/*      */ 
/*      */     public void drop(DropTargetDropEvent paramDropTargetDropEvent)
/*      */     {
/* 1274 */       super.drop(paramDropTargetDropEvent);
/* 1275 */       if (this.listenerList != null) {
/* 1276 */         Object[] arrayOfObject = this.listenerList.getListenerList();
/* 1277 */         for (int i = arrayOfObject.length - 2; i >= 0; i -= 2)
/* 1278 */           if (arrayOfObject[i] == DropTargetListener.class)
/* 1279 */             ((DropTargetListener)arrayOfObject[(i + 1)]).drop(paramDropTargetDropEvent);
/*      */       }
/*      */     }
/*      */ 
/*      */     public void dropActionChanged(DropTargetDragEvent paramDropTargetDragEvent)
/*      */     {
/* 1286 */       super.dropActionChanged(paramDropTargetDragEvent);
/* 1287 */       if (this.listenerList != null) {
/* 1288 */         Object[] arrayOfObject = this.listenerList.getListenerList();
/* 1289 */         for (int i = arrayOfObject.length - 2; i >= 0; i -= 2)
/* 1290 */           if (arrayOfObject[i] == DropTargetListener.class)
/* 1291 */             ((DropTargetListener)arrayOfObject[(i + 1)]).dropActionChanged(paramDropTargetDragEvent);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   static class TransferAction extends UIAction
/*      */     implements UIResource
/*      */   {
/* 1699 */     private static final JavaSecurityAccess javaSecurityAccess = SharedSecrets.getJavaSecurityAccess();
/*      */ 
/* 1784 */     private static Object SandboxClipboardKey = new Object();
/*      */ 
/*      */     TransferAction(String paramString)
/*      */     {
/* 1687 */       super();
/*      */     }
/*      */ 
/*      */     public boolean isEnabled(Object paramObject) {
/* 1691 */       if (((paramObject instanceof JComponent)) && (((JComponent)paramObject).getTransferHandler() == null))
/*      */       {
/* 1693 */         return false;
/*      */       }
/*      */ 
/* 1696 */       return true;
/*      */     }
/*      */ 
/*      */     public void actionPerformed(final ActionEvent paramActionEvent)
/*      */     {
/* 1703 */       Object localObject = paramActionEvent.getSource();
/*      */ 
/* 1705 */       final PrivilegedAction local1 = new PrivilegedAction() {
/*      */         public Void run() {
/* 1707 */           TransferHandler.TransferAction.this.actionPerformedImpl(paramActionEvent);
/* 1708 */           return null;
/*      */         }
/*      */       };
/* 1712 */       AccessControlContext localAccessControlContext1 = AccessController.getContext();
/* 1713 */       AccessControlContext localAccessControlContext2 = AWTAccessor.getComponentAccessor().getAccessControlContext((Component)localObject);
/* 1714 */       final AccessControlContext localAccessControlContext3 = AWTAccessor.getAWTEventAccessor().getAccessControlContext(paramActionEvent);
/*      */ 
/* 1716 */       if (localAccessControlContext2 == null)
/* 1717 */         javaSecurityAccess.doIntersectionPrivilege(local1, localAccessControlContext1, localAccessControlContext3);
/*      */       else
/* 1719 */         javaSecurityAccess.doIntersectionPrivilege(new PrivilegedAction()
/*      */         {
/*      */           public Void run() {
/* 1722 */             TransferHandler.TransferAction.javaSecurityAccess.doIntersectionPrivilege(local1, localAccessControlContext3);
/* 1723 */             return null;
/*      */           }
/*      */         }
/*      */         , localAccessControlContext1, localAccessControlContext2);
/*      */     }
/*      */ 
/*      */     private void actionPerformedImpl(ActionEvent paramActionEvent)
/*      */     {
/* 1730 */       Object localObject = paramActionEvent.getSource();
/* 1731 */       if ((localObject instanceof JComponent)) {
/* 1732 */         JComponent localJComponent = (JComponent)localObject;
/* 1733 */         TransferHandler localTransferHandler = localJComponent.getTransferHandler();
/* 1734 */         Clipboard localClipboard = getClipboard(localJComponent);
/* 1735 */         String str = (String)getValue("Name");
/*      */ 
/* 1737 */         Transferable localTransferable = null;
/*      */         try
/*      */         {
/* 1741 */           if ((localClipboard != null) && (localTransferHandler != null) && (str != null)) {
/* 1742 */             if ("cut".equals(str))
/* 1743 */               localTransferHandler.exportToClipboard(localJComponent, localClipboard, 2);
/* 1744 */             else if ("copy".equals(str))
/* 1745 */               localTransferHandler.exportToClipboard(localJComponent, localClipboard, 1);
/* 1746 */             else if ("paste".equals(str))
/* 1747 */               localTransferable = localClipboard.getContents(null);
/*      */           }
/*      */         }
/*      */         catch (IllegalStateException localIllegalStateException)
/*      */         {
/* 1752 */           UIManager.getLookAndFeel().provideErrorFeedback(localJComponent);
/* 1753 */           return;
/*      */         }
/*      */ 
/* 1757 */         if (localTransferable != null)
/* 1758 */           localTransferHandler.importData(new TransferHandler.TransferSupport(localJComponent, localTransferable));
/*      */       }
/*      */     }
/*      */ 
/*      */     private Clipboard getClipboard(JComponent paramJComponent)
/*      */     {
/* 1767 */       if (SwingUtilities2.canAccessSystemClipboard()) {
/* 1768 */         return paramJComponent.getToolkit().getSystemClipboard();
/*      */       }
/* 1770 */       Clipboard localClipboard = (Clipboard)AppContext.getAppContext().get(SandboxClipboardKey);
/*      */ 
/* 1772 */       if (localClipboard == null) {
/* 1773 */         localClipboard = new Clipboard("Sandboxed Component Clipboard");
/* 1774 */         AppContext.getAppContext().put(SandboxClipboardKey, localClipboard);
/*      */       }
/*      */ 
/* 1777 */       return localClipboard;
/*      */     }
/*      */   }
/*      */ 
/*      */   public static final class TransferSupport
/*      */   {
/*      */     private boolean isDrop;
/*      */     private Component component;
/*      */     private boolean showDropLocationIsSet;
/*      */     private boolean showDropLocation;
/*  220 */     private int dropAction = -1;
/*      */     private Object source;
/*      */     private TransferHandler.DropLocation dropLocation;
/*      */ 
/*      */     private TransferSupport(Component paramComponent, DropTargetEvent paramDropTargetEvent)
/*      */     {
/*  241 */       this.isDrop = true;
/*  242 */       setDNDVariables(paramComponent, paramDropTargetEvent);
/*      */     }
/*      */ 
/*      */     public TransferSupport(Component paramComponent, Transferable paramTransferable)
/*      */     {
/*  256 */       if (paramComponent == null) {
/*  257 */         throw new NullPointerException("component is null");
/*      */       }
/*      */ 
/*  260 */       if (paramTransferable == null) {
/*  261 */         throw new NullPointerException("transferable is null");
/*      */       }
/*      */ 
/*  264 */       this.isDrop = false;
/*  265 */       this.component = paramComponent;
/*  266 */       this.source = paramTransferable;
/*      */     }
/*      */ 
/*      */     private void setDNDVariables(Component paramComponent, DropTargetEvent paramDropTargetEvent)
/*      */     {
/*  278 */       assert (this.isDrop);
/*      */ 
/*  280 */       this.component = paramComponent;
/*  281 */       this.source = paramDropTargetEvent;
/*  282 */       this.dropLocation = null;
/*  283 */       this.dropAction = -1;
/*  284 */       this.showDropLocationIsSet = false;
/*      */ 
/*  286 */       if (this.source == null) {
/*  287 */         return;
/*      */       }
/*      */ 
/*  290 */       assert (((this.source instanceof DropTargetDragEvent)) || ((this.source instanceof DropTargetDropEvent)));
/*      */ 
/*  293 */       Point localPoint = (this.source instanceof DropTargetDragEvent) ? ((DropTargetDragEvent)this.source).getLocation() : ((DropTargetDropEvent)this.source).getLocation();
/*      */ 
/*  297 */       if (SunToolkit.isInstanceOf(paramComponent, "javax.swing.text.JTextComponent")) {
/*  298 */         this.dropLocation = SwingAccessor.getJTextComponentAccessor().dropLocationForPoint((JTextComponent)paramComponent, localPoint);
/*      */       }
/*  300 */       else if ((paramComponent instanceof JComponent))
/*  301 */         this.dropLocation = ((JComponent)paramComponent).dropLocationForPoint(localPoint);
/*      */     }
/*      */ 
/*      */     public boolean isDrop()
/*      */     {
/*  319 */       return this.isDrop;
/*      */     }
/*      */ 
/*      */     public Component getComponent()
/*      */     {
/*  328 */       return this.component;
/*      */     }
/*      */ 
/*      */     private void assureIsDrop()
/*      */     {
/*  338 */       if (!this.isDrop)
/*  339 */         throw new IllegalStateException("Not a drop");
/*      */     }
/*      */ 
/*      */     public TransferHandler.DropLocation getDropLocation()
/*      */     {
/*  360 */       assureIsDrop();
/*      */ 
/*  362 */       if (this.dropLocation == null)
/*      */       {
/*  367 */         Point localPoint = (this.source instanceof DropTargetDragEvent) ? ((DropTargetDragEvent)this.source).getLocation() : ((DropTargetDropEvent)this.source).getLocation();
/*      */ 
/*  371 */         this.dropLocation = new TransferHandler.DropLocation(localPoint);
/*      */       }
/*      */ 
/*  374 */       return this.dropLocation;
/*      */     }
/*      */ 
/*      */     public void setShowDropLocation(boolean paramBoolean)
/*      */     {
/*  396 */       assureIsDrop();
/*      */ 
/*  398 */       this.showDropLocation = paramBoolean;
/*  399 */       this.showDropLocationIsSet = true;
/*      */     }
/*      */ 
/*      */     public void setDropAction(int paramInt)
/*      */     {
/*  422 */       assureIsDrop();
/*      */ 
/*  424 */       int i = paramInt & getSourceDropActions();
/*      */ 
/*  426 */       if ((i != 1) && (i != 2) && (i != 1073741824)) {
/*  427 */         throw new IllegalArgumentException("unsupported drop action: " + paramInt);
/*      */       }
/*      */ 
/*  430 */       this.dropAction = paramInt;
/*      */     }
/*      */ 
/*      */     public int getDropAction()
/*      */     {
/*  456 */       return this.dropAction == -1 ? getUserDropAction() : this.dropAction;
/*      */     }
/*      */ 
/*      */     public int getUserDropAction()
/*      */     {
/*  484 */       assureIsDrop();
/*      */ 
/*  486 */       return (this.source instanceof DropTargetDragEvent) ? ((DropTargetDragEvent)this.source).getDropAction() : ((DropTargetDropEvent)this.source).getDropAction();
/*      */     }
/*      */ 
/*      */     public int getSourceDropActions()
/*      */     {
/*  517 */       assureIsDrop();
/*      */ 
/*  519 */       return (this.source instanceof DropTargetDragEvent) ? ((DropTargetDragEvent)this.source).getSourceActions() : ((DropTargetDropEvent)this.source).getSourceActions();
/*      */     }
/*      */ 
/*      */     public DataFlavor[] getDataFlavors()
/*      */     {
/*  530 */       if (this.isDrop) {
/*  531 */         if ((this.source instanceof DropTargetDragEvent)) {
/*  532 */           return ((DropTargetDragEvent)this.source).getCurrentDataFlavors();
/*      */         }
/*  534 */         return ((DropTargetDropEvent)this.source).getCurrentDataFlavors();
/*      */       }
/*      */ 
/*  538 */       return ((Transferable)this.source).getTransferDataFlavors();
/*      */     }
/*      */ 
/*      */     public boolean isDataFlavorSupported(DataFlavor paramDataFlavor)
/*      */     {
/*  548 */       if (this.isDrop) {
/*  549 */         if ((this.source instanceof DropTargetDragEvent)) {
/*  550 */           return ((DropTargetDragEvent)this.source).isDataFlavorSupported(paramDataFlavor);
/*      */         }
/*  552 */         return ((DropTargetDropEvent)this.source).isDataFlavorSupported(paramDataFlavor);
/*      */       }
/*      */ 
/*  556 */       return ((Transferable)this.source).isDataFlavorSupported(paramDataFlavor);
/*      */     }
/*      */ 
/*      */     public Transferable getTransferable()
/*      */     {
/*  570 */       if (this.isDrop) {
/*  571 */         if ((this.source instanceof DropTargetDragEvent)) {
/*  572 */           return ((DropTargetDragEvent)this.source).getTransferable();
/*      */         }
/*  574 */         return ((DropTargetDropEvent)this.source).getTransferable();
/*      */       }
/*      */ 
/*  578 */       return (Transferable)this.source;
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.TransferHandler
 * JD-Core Version:    0.6.2
 */