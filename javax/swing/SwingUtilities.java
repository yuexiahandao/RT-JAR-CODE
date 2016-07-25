/*      */ package javax.swing;
/*      */ 
/*      */ import java.applet.Applet;
/*      */ import java.awt.Component;
/*      */ import java.awt.ComponentOrientation;
/*      */ import java.awt.Container;
/*      */ import java.awt.EventQueue;
/*      */ import java.awt.FontMetrics;
/*      */ import java.awt.Frame;
/*      */ import java.awt.Graphics;
/*      */ import java.awt.GraphicsEnvironment;
/*      */ import java.awt.HeadlessException;
/*      */ import java.awt.IllegalComponentStateException;
/*      */ import java.awt.Image;
/*      */ import java.awt.Insets;
/*      */ import java.awt.KeyboardFocusManager;
/*      */ import java.awt.Point;
/*      */ import java.awt.Rectangle;
/*      */ import java.awt.Toolkit;
/*      */ import java.awt.Window;
/*      */ import java.awt.dnd.DropTarget;
/*      */ import java.awt.event.ActionEvent;
/*      */ import java.awt.event.KeyEvent;
/*      */ import java.awt.event.MouseEvent;
/*      */ import java.awt.event.MouseWheelEvent;
/*      */ import java.awt.event.WindowEvent;
/*      */ import java.awt.event.WindowListener;
/*      */ import java.lang.reflect.InvocationTargetException;
/*      */ import java.security.AccessController;
/*      */ import javax.accessibility.Accessible;
/*      */ import javax.accessibility.AccessibleComponent;
/*      */ import javax.accessibility.AccessibleContext;
/*      */ import javax.accessibility.AccessibleStateSet;
/*      */ import javax.swing.event.MenuDragMouseEvent;
/*      */ import javax.swing.plaf.UIResource;
/*      */ import javax.swing.text.View;
/*      */ import sun.awt.AppContext;
/*      */ import sun.reflect.misc.ReflectUtil;
/*      */ import sun.security.action.GetPropertyAction;
/*      */ import sun.swing.SwingUtilities2;
/*      */ import sun.swing.UIAction;
/*      */ 
/*      */ public class SwingUtilities
/*      */   implements SwingConstants
/*      */ {
/*   56 */   private static boolean canAccessEventQueue = false;
/*   57 */   private static boolean eventQueueTested = false;
/*      */   private static boolean suppressDropSupport;
/*      */   private static boolean checkedSuppressDropSupport;
/* 1754 */   private static final Object sharedOwnerFrameKey = new StringBuffer("SwingUtilities.sharedOwnerFrame");
/*      */ 
/*      */   private static boolean getSuppressDropTarget()
/*      */   {
/*   77 */     if (!checkedSuppressDropSupport) {
/*   78 */       suppressDropSupport = Boolean.valueOf((String)AccessController.doPrivileged(new GetPropertyAction("suppressSwingDropSupport"))).booleanValue();
/*      */ 
/*   81 */       checkedSuppressDropSupport = true;
/*      */     }
/*   83 */     return suppressDropSupport;
/*      */   }
/*      */ 
/*      */   static void installSwingDropTargetAsNecessary(Component paramComponent, TransferHandler paramTransferHandler)
/*      */   {
/*   93 */     if (!getSuppressDropTarget()) {
/*   94 */       DropTarget localDropTarget = paramComponent.getDropTarget();
/*   95 */       if ((localDropTarget == null) || ((localDropTarget instanceof UIResource)))
/*   96 */         if (paramTransferHandler == null)
/*   97 */           paramComponent.setDropTarget(null);
/*   98 */         else if (!GraphicsEnvironment.isHeadless())
/*   99 */           paramComponent.setDropTarget(new TransferHandler.SwingDropTarget(paramComponent));
/*      */     }
/*      */   }
/*      */ 
/*      */   public static final boolean isRectangleContainingRectangle(Rectangle paramRectangle1, Rectangle paramRectangle2)
/*      */   {
/*  109 */     return (paramRectangle2.x >= paramRectangle1.x) && (paramRectangle2.x + paramRectangle2.width <= paramRectangle1.x + paramRectangle1.width) && (paramRectangle2.y >= paramRectangle1.y) && (paramRectangle2.y + paramRectangle2.height <= paramRectangle1.y + paramRectangle1.height);
/*      */   }
/*      */ 
/*      */   public static Rectangle getLocalBounds(Component paramComponent)
/*      */   {
/*  117 */     Rectangle localRectangle = new Rectangle(paramComponent.getBounds());
/*  118 */     localRectangle.x = (localRectangle.y = 0);
/*  119 */     return localRectangle;
/*      */   }
/*      */ 
/*      */   public static Window getWindowAncestor(Component paramComponent)
/*      */   {
/*  135 */     for (Container localContainer = paramComponent.getParent(); localContainer != null; localContainer = localContainer.getParent()) {
/*  136 */       if ((localContainer instanceof Window)) {
/*  137 */         return (Window)localContainer;
/*      */       }
/*      */     }
/*  140 */     return null;
/*      */   }
/*      */ 
/*      */   static Point convertScreenLocationToParent(Container paramContainer, int paramInt1, int paramInt2)
/*      */   {
/*  148 */     for (Container localContainer = paramContainer; localContainer != null; localContainer = localContainer.getParent()) {
/*  149 */       if ((localContainer instanceof Window)) {
/*  150 */         Point localPoint = new Point(paramInt1, paramInt2);
/*      */ 
/*  152 */         convertPointFromScreen(localPoint, paramContainer);
/*  153 */         return localPoint;
/*      */       }
/*      */     }
/*  156 */     throw new Error("convertScreenLocationToParent: no window ancestor");
/*      */   }
/*      */ 
/*      */   public static Point convertPoint(Component paramComponent1, Point paramPoint, Component paramComponent2)
/*      */   {
/*  172 */     if ((paramComponent1 == null) && (paramComponent2 == null))
/*  173 */       return paramPoint;
/*  174 */     if (paramComponent1 == null) {
/*  175 */       paramComponent1 = getWindowAncestor(paramComponent2);
/*  176 */       if (paramComponent1 == null)
/*  177 */         throw new Error("Source component not connected to component tree hierarchy");
/*      */     }
/*  179 */     Point localPoint = new Point(paramPoint);
/*  180 */     convertPointToScreen(localPoint, paramComponent1);
/*  181 */     if (paramComponent2 == null) {
/*  182 */       paramComponent2 = getWindowAncestor(paramComponent1);
/*  183 */       if (paramComponent2 == null)
/*  184 */         throw new Error("Destination component not connected to component tree hierarchy");
/*      */     }
/*  186 */     convertPointFromScreen(localPoint, paramComponent2);
/*  187 */     return localPoint;
/*      */   }
/*      */ 
/*      */   public static Point convertPoint(Component paramComponent1, int paramInt1, int paramInt2, Component paramComponent2)
/*      */   {
/*  201 */     Point localPoint = new Point(paramInt1, paramInt2);
/*  202 */     return convertPoint(paramComponent1, localPoint, paramComponent2);
/*      */   }
/*      */ 
/*      */   public static Rectangle convertRectangle(Component paramComponent1, Rectangle paramRectangle, Component paramComponent2)
/*      */   {
/*  216 */     Point localPoint = new Point(paramRectangle.x, paramRectangle.y);
/*  217 */     localPoint = convertPoint(paramComponent1, localPoint, paramComponent2);
/*  218 */     return new Rectangle(localPoint.x, localPoint.y, paramRectangle.width, paramRectangle.height);
/*      */   }
/*      */ 
/*      */   public static Container getAncestorOfClass(Class<?> paramClass, Component paramComponent)
/*      */   {
/*  228 */     if ((paramComponent == null) || (paramClass == null)) {
/*  229 */       return null;
/*      */     }
/*  231 */     Container localContainer = paramComponent.getParent();
/*  232 */     while ((localContainer != null) && (!paramClass.isInstance(localContainer)))
/*  233 */       localContainer = localContainer.getParent();
/*  234 */     return localContainer;
/*      */   }
/*      */ 
/*      */   public static Container getAncestorNamed(String paramString, Component paramComponent)
/*      */   {
/*  243 */     if ((paramComponent == null) || (paramString == null)) {
/*  244 */       return null;
/*      */     }
/*  246 */     Container localContainer = paramComponent.getParent();
/*  247 */     while ((localContainer != null) && (!paramString.equals(localContainer.getName())))
/*  248 */       localContainer = localContainer.getParent();
/*  249 */     return localContainer;
/*      */   }
/*      */ 
/*      */   public static Component getDeepestComponentAt(Component paramComponent, int paramInt1, int paramInt2)
/*      */   {
/*  265 */     if (!paramComponent.contains(paramInt1, paramInt2)) {
/*  266 */       return null;
/*      */     }
/*  268 */     if ((paramComponent instanceof Container)) {
/*  269 */       Component[] arrayOfComponent1 = ((Container)paramComponent).getComponents();
/*  270 */       for (Component localComponent : arrayOfComponent1) {
/*  271 */         if ((localComponent != null) && (localComponent.isVisible())) {
/*  272 */           Point localPoint = localComponent.getLocation();
/*  273 */           if ((localComponent instanceof Container))
/*  274 */             localComponent = getDeepestComponentAt(localComponent, paramInt1 - localPoint.x, paramInt2 - localPoint.y);
/*      */           else {
/*  276 */             localComponent = localComponent.getComponentAt(paramInt1 - localPoint.x, paramInt2 - localPoint.y);
/*      */           }
/*  278 */           if ((localComponent != null) && (localComponent.isVisible())) {
/*  279 */             return localComponent;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*  284 */     return paramComponent;
/*      */   }
/*      */ 
/*      */   public static MouseEvent convertMouseEvent(Component paramComponent1, MouseEvent paramMouseEvent, Component paramComponent2)
/*      */   {
/*  304 */     Point localPoint = convertPoint(paramComponent1, new Point(paramMouseEvent.getX(), paramMouseEvent.getY()), paramComponent2);
/*      */     Component localComponent;
/*  309 */     if (paramComponent2 != null)
/*  310 */       localComponent = paramComponent2;
/*      */     else
/*  312 */       localComponent = paramComponent1;
/*      */     Object localObject2;
/*      */     Object localObject1;
/*  315 */     if ((paramMouseEvent instanceof MouseWheelEvent)) {
/*  316 */       localObject2 = (MouseWheelEvent)paramMouseEvent;
/*  317 */       localObject1 = new MouseWheelEvent(localComponent, ((MouseWheelEvent)localObject2).getID(), ((MouseWheelEvent)localObject2).getWhen(), ((MouseWheelEvent)localObject2).getModifiers() | ((MouseWheelEvent)localObject2).getModifiersEx(), localPoint.x, localPoint.y, ((MouseWheelEvent)localObject2).getXOnScreen(), ((MouseWheelEvent)localObject2).getYOnScreen(), ((MouseWheelEvent)localObject2).getClickCount(), ((MouseWheelEvent)localObject2).isPopupTrigger(), ((MouseWheelEvent)localObject2).getScrollType(), ((MouseWheelEvent)localObject2).getScrollAmount(), ((MouseWheelEvent)localObject2).getWheelRotation());
/*      */     }
/*  331 */     else if ((paramMouseEvent instanceof MenuDragMouseEvent)) {
/*  332 */       localObject2 = (MenuDragMouseEvent)paramMouseEvent;
/*  333 */       localObject1 = new MenuDragMouseEvent(localComponent, ((MenuDragMouseEvent)localObject2).getID(), ((MenuDragMouseEvent)localObject2).getWhen(), ((MenuDragMouseEvent)localObject2).getModifiers() | ((MenuDragMouseEvent)localObject2).getModifiersEx(), localPoint.x, localPoint.y, ((MenuDragMouseEvent)localObject2).getXOnScreen(), ((MenuDragMouseEvent)localObject2).getYOnScreen(), ((MenuDragMouseEvent)localObject2).getClickCount(), ((MenuDragMouseEvent)localObject2).isPopupTrigger(), ((MenuDragMouseEvent)localObject2).getPath(), ((MenuDragMouseEvent)localObject2).getMenuSelectionManager());
/*      */     }
/*      */     else
/*      */     {
/*  347 */       localObject1 = new MouseEvent(localComponent, paramMouseEvent.getID(), paramMouseEvent.getWhen(), paramMouseEvent.getModifiers() | paramMouseEvent.getModifiersEx(), localPoint.x, localPoint.y, paramMouseEvent.getXOnScreen(), paramMouseEvent.getYOnScreen(), paramMouseEvent.getClickCount(), paramMouseEvent.isPopupTrigger(), paramMouseEvent.getButton());
/*      */     }
/*      */ 
/*  359 */     return localObject1;
/*      */   }
/*      */ 
/*      */   public static void convertPointToScreen(Point paramPoint, Component paramComponent)
/*      */   {
/*      */     do
/*      */     {
/*      */       int i;
/*      */       int j;
/*  375 */       if ((paramComponent instanceof JComponent)) {
/*  376 */         i = paramComponent.getX();
/*  377 */         j = paramComponent.getY();
/*  378 */       } else if (((paramComponent instanceof Applet)) || ((paramComponent instanceof Window)))
/*      */       {
/*      */         try {
/*  381 */           Point localPoint = paramComponent.getLocationOnScreen();
/*  382 */           i = localPoint.x;
/*  383 */           j = localPoint.y;
/*      */         } catch (IllegalComponentStateException localIllegalComponentStateException) {
/*  385 */           i = paramComponent.getX();
/*  386 */           j = paramComponent.getY();
/*      */         }
/*      */       } else {
/*  389 */         i = paramComponent.getX();
/*  390 */         j = paramComponent.getY();
/*      */       }
/*      */ 
/*  393 */       paramPoint.x += i;
/*  394 */       paramPoint.y += j;
/*      */ 
/*  396 */       if (((paramComponent instanceof Window)) || ((paramComponent instanceof Applet)))
/*      */         break;
/*  398 */       paramComponent = paramComponent.getParent();
/*  399 */     }while (paramComponent != null);
/*      */   }
/*      */ 
/*      */   public static void convertPointFromScreen(Point paramPoint, Component paramComponent)
/*      */   {
/*      */     do
/*      */     {
/*      */       int i;
/*      */       int j;
/*  414 */       if ((paramComponent instanceof JComponent)) {
/*  415 */         i = paramComponent.getX();
/*  416 */         j = paramComponent.getY();
/*  417 */       } else if (((paramComponent instanceof Applet)) || ((paramComponent instanceof Window)))
/*      */       {
/*      */         try {
/*  420 */           Point localPoint = paramComponent.getLocationOnScreen();
/*  421 */           i = localPoint.x;
/*  422 */           j = localPoint.y;
/*      */         } catch (IllegalComponentStateException localIllegalComponentStateException) {
/*  424 */           i = paramComponent.getX();
/*  425 */           j = paramComponent.getY();
/*      */         }
/*      */       } else {
/*  428 */         i = paramComponent.getX();
/*  429 */         j = paramComponent.getY();
/*      */       }
/*      */ 
/*  432 */       paramPoint.x -= i;
/*  433 */       paramPoint.y -= j;
/*      */ 
/*  435 */       if (((paramComponent instanceof Window)) || ((paramComponent instanceof Applet)))
/*      */         break;
/*  437 */       paramComponent = paramComponent.getParent();
/*  438 */     }while (paramComponent != null);
/*      */   }
/*      */ 
/*      */   public static Window windowForComponent(Component paramComponent)
/*      */   {
/*  455 */     return getWindowAncestor(paramComponent);
/*      */   }
/*      */ 
/*      */   public static boolean isDescendingFrom(Component paramComponent1, Component paramComponent2)
/*      */   {
/*  462 */     if (paramComponent1 == paramComponent2)
/*  463 */       return true;
/*  464 */     for (Container localContainer = paramComponent1.getParent(); localContainer != null; localContainer = localContainer.getParent())
/*  465 */       if (localContainer == paramComponent2)
/*  466 */         return true;
/*  467 */     return false;
/*      */   }
/*      */ 
/*      */   public static Rectangle computeIntersection(int paramInt1, int paramInt2, int paramInt3, int paramInt4, Rectangle paramRectangle)
/*      */   {
/*  487 */     int i = paramInt1 > paramRectangle.x ? paramInt1 : paramRectangle.x;
/*  488 */     int j = paramInt1 + paramInt3 < paramRectangle.x + paramRectangle.width ? paramInt1 + paramInt3 : paramRectangle.x + paramRectangle.width;
/*  489 */     int k = paramInt2 > paramRectangle.y ? paramInt2 : paramRectangle.y;
/*  490 */     int m = paramInt2 + paramInt4 < paramRectangle.y + paramRectangle.height ? paramInt2 + paramInt4 : paramRectangle.y + paramRectangle.height;
/*      */ 
/*  492 */     paramRectangle.x = i;
/*  493 */     paramRectangle.y = k;
/*  494 */     paramRectangle.width = (j - i);
/*  495 */     paramRectangle.height = (m - k);
/*      */ 
/*  498 */     if ((paramRectangle.width < 0) || (paramRectangle.height < 0)) {
/*  499 */       paramRectangle.x = (paramRectangle.y = paramRectangle.width = paramRectangle.height = 0);
/*      */     }
/*      */ 
/*  502 */     return paramRectangle;
/*      */   }
/*      */ 
/*      */   public static Rectangle computeUnion(int paramInt1, int paramInt2, int paramInt3, int paramInt4, Rectangle paramRectangle)
/*      */   {
/*  518 */     int i = paramInt1 < paramRectangle.x ? paramInt1 : paramRectangle.x;
/*  519 */     int j = paramInt1 + paramInt3 > paramRectangle.x + paramRectangle.width ? paramInt1 + paramInt3 : paramRectangle.x + paramRectangle.width;
/*  520 */     int k = paramInt2 < paramRectangle.y ? paramInt2 : paramRectangle.y;
/*  521 */     int m = paramInt2 + paramInt4 > paramRectangle.y + paramRectangle.height ? paramInt2 + paramInt4 : paramRectangle.y + paramRectangle.height;
/*      */ 
/*  523 */     paramRectangle.x = i;
/*  524 */     paramRectangle.y = k;
/*  525 */     paramRectangle.width = (j - i);
/*  526 */     paramRectangle.height = (m - k);
/*  527 */     return paramRectangle;
/*      */   }
/*      */ 
/*      */   public static Rectangle[] computeDifference(Rectangle paramRectangle1, Rectangle paramRectangle2)
/*      */   {
/*  536 */     if ((paramRectangle2 == null) || (!paramRectangle1.intersects(paramRectangle2)) || (isRectangleContainingRectangle(paramRectangle2, paramRectangle1))) {
/*  537 */       return new Rectangle[0];
/*      */     }
/*      */ 
/*  540 */     Rectangle localRectangle1 = new Rectangle();
/*  541 */     Rectangle localRectangle2 = null; Rectangle localRectangle3 = null; Rectangle localRectangle4 = null; Rectangle localRectangle5 = null;
/*      */ 
/*  543 */     int i = 0;
/*      */ 
/*  546 */     if (isRectangleContainingRectangle(paramRectangle1, paramRectangle2)) {
/*  547 */       localRectangle1.x = paramRectangle1.x; localRectangle1.y = paramRectangle1.y; localRectangle1.width = (paramRectangle2.x - paramRectangle1.x); localRectangle1.height = paramRectangle1.height;
/*  548 */       if ((localRectangle1.width > 0) && (localRectangle1.height > 0)) {
/*  549 */         localRectangle2 = new Rectangle(localRectangle1);
/*  550 */         i++;
/*      */       }
/*      */ 
/*  553 */       localRectangle1.x = paramRectangle2.x; localRectangle1.y = paramRectangle1.y; localRectangle1.width = paramRectangle2.width; localRectangle1.height = (paramRectangle2.y - paramRectangle1.y);
/*  554 */       if ((localRectangle1.width > 0) && (localRectangle1.height > 0)) {
/*  555 */         localRectangle3 = new Rectangle(localRectangle1);
/*  556 */         i++;
/*      */       }
/*      */ 
/*  559 */       localRectangle1.x = paramRectangle2.x; paramRectangle2.y += paramRectangle2.height; localRectangle1.width = paramRectangle2.width;
/*  560 */       localRectangle1.height = (paramRectangle1.y + paramRectangle1.height - (paramRectangle2.y + paramRectangle2.height));
/*  561 */       if ((localRectangle1.width > 0) && (localRectangle1.height > 0)) {
/*  562 */         localRectangle4 = new Rectangle(localRectangle1);
/*  563 */         i++;
/*      */       }
/*      */ 
/*  566 */       paramRectangle2.x += paramRectangle2.width; localRectangle1.y = paramRectangle1.y; localRectangle1.width = (paramRectangle1.x + paramRectangle1.width - (paramRectangle2.x + paramRectangle2.width));
/*  567 */       localRectangle1.height = paramRectangle1.height;
/*  568 */       if ((localRectangle1.width > 0) && (localRectangle1.height > 0)) {
/*  569 */         localRectangle5 = new Rectangle(localRectangle1);
/*  570 */         i++;
/*      */       }
/*      */ 
/*      */     }
/*  574 */     else if ((paramRectangle2.x <= paramRectangle1.x) && (paramRectangle2.y <= paramRectangle1.y)) {
/*  575 */       if (paramRectangle2.x + paramRectangle2.width > paramRectangle1.x + paramRectangle1.width)
/*      */       {
/*  577 */         localRectangle1.x = paramRectangle1.x; paramRectangle2.y += paramRectangle2.height;
/*  578 */         localRectangle1.width = paramRectangle1.width; localRectangle1.height = (paramRectangle1.y + paramRectangle1.height - (paramRectangle2.y + paramRectangle2.height));
/*  579 */         if ((localRectangle1.width > 0) && (localRectangle1.height > 0)) {
/*  580 */           localRectangle2 = localRectangle1;
/*  581 */           i++;
/*      */         }
/*  583 */       } else if (paramRectangle2.y + paramRectangle2.height > paramRectangle1.y + paramRectangle1.height) {
/*  584 */         localRectangle1.setBounds(paramRectangle2.x + paramRectangle2.width, paramRectangle1.y, paramRectangle1.x + paramRectangle1.width - (paramRectangle2.x + paramRectangle2.width), paramRectangle1.height);
/*      */ 
/*  586 */         if ((localRectangle1.width > 0) && (localRectangle1.height > 0)) {
/*  587 */           localRectangle2 = localRectangle1;
/*  588 */           i++;
/*      */         }
/*      */       } else {
/*  591 */         localRectangle1.setBounds(paramRectangle2.x + paramRectangle2.width, paramRectangle1.y, paramRectangle1.x + paramRectangle1.width - (paramRectangle2.x + paramRectangle2.width), paramRectangle2.y + paramRectangle2.height - paramRectangle1.y);
/*      */ 
/*  594 */         if ((localRectangle1.width > 0) && (localRectangle1.height > 0)) {
/*  595 */           localRectangle2 = new Rectangle(localRectangle1);
/*  596 */           i++;
/*      */         }
/*      */ 
/*  599 */         localRectangle1.setBounds(paramRectangle1.x, paramRectangle2.y + paramRectangle2.height, paramRectangle1.width, paramRectangle1.y + paramRectangle1.height - (paramRectangle2.y + paramRectangle2.height));
/*      */ 
/*  601 */         if ((localRectangle1.width > 0) && (localRectangle1.height > 0)) {
/*  602 */           localRectangle3 = new Rectangle(localRectangle1);
/*  603 */           i++;
/*      */         }
/*      */       }
/*  606 */     } else if ((paramRectangle2.x <= paramRectangle1.x) && (paramRectangle2.y + paramRectangle2.height >= paramRectangle1.y + paramRectangle1.height)) {
/*  607 */       if (paramRectangle2.x + paramRectangle2.width > paramRectangle1.x + paramRectangle1.width) {
/*  608 */         localRectangle1.setBounds(paramRectangle1.x, paramRectangle1.y, paramRectangle1.width, paramRectangle2.y - paramRectangle1.y);
/*  609 */         if ((localRectangle1.width > 0) && (localRectangle1.height > 0)) {
/*  610 */           localRectangle2 = localRectangle1;
/*  611 */           i++;
/*      */         }
/*      */       } else {
/*  614 */         localRectangle1.setBounds(paramRectangle1.x, paramRectangle1.y, paramRectangle1.width, paramRectangle2.y - paramRectangle1.y);
/*  615 */         if ((localRectangle1.width > 0) && (localRectangle1.height > 0)) {
/*  616 */           localRectangle2 = new Rectangle(localRectangle1);
/*  617 */           i++;
/*      */         }
/*  619 */         localRectangle1.setBounds(paramRectangle2.x + paramRectangle2.width, paramRectangle2.y, paramRectangle1.x + paramRectangle1.width - (paramRectangle2.x + paramRectangle2.width), paramRectangle1.y + paramRectangle1.height - paramRectangle2.y);
/*      */ 
/*  622 */         if ((localRectangle1.width > 0) && (localRectangle1.height > 0)) {
/*  623 */           localRectangle3 = new Rectangle(localRectangle1);
/*  624 */           i++;
/*      */         }
/*      */       }
/*  627 */     } else if (paramRectangle2.x <= paramRectangle1.x) {
/*  628 */       if (paramRectangle2.x + paramRectangle2.width >= paramRectangle1.x + paramRectangle1.width) {
/*  629 */         localRectangle1.setBounds(paramRectangle1.x, paramRectangle1.y, paramRectangle1.width, paramRectangle2.y - paramRectangle1.y);
/*  630 */         if ((localRectangle1.width > 0) && (localRectangle1.height > 0)) {
/*  631 */           localRectangle2 = new Rectangle(localRectangle1);
/*  632 */           i++;
/*      */         }
/*      */ 
/*  635 */         localRectangle1.setBounds(paramRectangle1.x, paramRectangle2.y + paramRectangle2.height, paramRectangle1.width, paramRectangle1.y + paramRectangle1.height - (paramRectangle2.y + paramRectangle2.height));
/*      */ 
/*  637 */         if ((localRectangle1.width > 0) && (localRectangle1.height > 0)) {
/*  638 */           localRectangle3 = new Rectangle(localRectangle1);
/*  639 */           i++;
/*      */         }
/*      */       } else {
/*  642 */         localRectangle1.setBounds(paramRectangle1.x, paramRectangle1.y, paramRectangle1.width, paramRectangle2.y - paramRectangle1.y);
/*  643 */         if ((localRectangle1.width > 0) && (localRectangle1.height > 0)) {
/*  644 */           localRectangle2 = new Rectangle(localRectangle1);
/*  645 */           i++;
/*      */         }
/*      */ 
/*  648 */         localRectangle1.setBounds(paramRectangle2.x + paramRectangle2.width, paramRectangle2.y, paramRectangle1.x + paramRectangle1.width - (paramRectangle2.x + paramRectangle2.width), paramRectangle2.height);
/*      */ 
/*  651 */         if ((localRectangle1.width > 0) && (localRectangle1.height > 0)) {
/*  652 */           localRectangle3 = new Rectangle(localRectangle1);
/*  653 */           i++;
/*      */         }
/*      */ 
/*  656 */         localRectangle1.setBounds(paramRectangle1.x, paramRectangle2.y + paramRectangle2.height, paramRectangle1.width, paramRectangle1.y + paramRectangle1.height - (paramRectangle2.y + paramRectangle2.height));
/*      */ 
/*  658 */         if ((localRectangle1.width > 0) && (localRectangle1.height > 0)) {
/*  659 */           localRectangle4 = new Rectangle(localRectangle1);
/*  660 */           i++;
/*      */         }
/*      */       }
/*  663 */     } else if ((paramRectangle2.x <= paramRectangle1.x + paramRectangle1.width) && (paramRectangle2.x + paramRectangle2.width > paramRectangle1.x + paramRectangle1.width)) {
/*  664 */       if ((paramRectangle2.y <= paramRectangle1.y) && (paramRectangle2.y + paramRectangle2.height > paramRectangle1.y + paramRectangle1.height)) {
/*  665 */         localRectangle1.setBounds(paramRectangle1.x, paramRectangle1.y, paramRectangle2.x - paramRectangle1.x, paramRectangle1.height);
/*  666 */         if ((localRectangle1.width > 0) && (localRectangle1.height > 0)) {
/*  667 */           localRectangle2 = localRectangle1;
/*  668 */           i++;
/*      */         }
/*  670 */       } else if (paramRectangle2.y <= paramRectangle1.y) {
/*  671 */         localRectangle1.setBounds(paramRectangle1.x, paramRectangle1.y, paramRectangle2.x - paramRectangle1.x, paramRectangle2.y + paramRectangle2.height - paramRectangle1.y);
/*      */ 
/*  673 */         if ((localRectangle1.width > 0) && (localRectangle1.height > 0)) {
/*  674 */           localRectangle2 = new Rectangle(localRectangle1);
/*  675 */           i++;
/*      */         }
/*      */ 
/*  678 */         localRectangle1.setBounds(paramRectangle1.x, paramRectangle2.y + paramRectangle2.height, paramRectangle1.width, paramRectangle1.y + paramRectangle1.height - (paramRectangle2.y + paramRectangle2.height));
/*      */ 
/*  680 */         if ((localRectangle1.width > 0) && (localRectangle1.height > 0)) {
/*  681 */           localRectangle3 = new Rectangle(localRectangle1);
/*  682 */           i++;
/*      */         }
/*  684 */       } else if (paramRectangle2.y + paramRectangle2.height > paramRectangle1.y + paramRectangle1.height) {
/*  685 */         localRectangle1.setBounds(paramRectangle1.x, paramRectangle1.y, paramRectangle1.width, paramRectangle2.y - paramRectangle1.y);
/*  686 */         if ((localRectangle1.width > 0) && (localRectangle1.height > 0)) {
/*  687 */           localRectangle2 = new Rectangle(localRectangle1);
/*  688 */           i++;
/*      */         }
/*      */ 
/*  691 */         localRectangle1.setBounds(paramRectangle1.x, paramRectangle2.y, paramRectangle2.x - paramRectangle1.x, paramRectangle1.y + paramRectangle1.height - paramRectangle2.y);
/*      */ 
/*  693 */         if ((localRectangle1.width > 0) && (localRectangle1.height > 0)) {
/*  694 */           localRectangle3 = new Rectangle(localRectangle1);
/*  695 */           i++;
/*      */         }
/*      */       } else {
/*  698 */         localRectangle1.setBounds(paramRectangle1.x, paramRectangle1.y, paramRectangle1.width, paramRectangle2.y - paramRectangle1.y);
/*  699 */         if ((localRectangle1.width > 0) && (localRectangle1.height > 0)) {
/*  700 */           localRectangle2 = new Rectangle(localRectangle1);
/*  701 */           i++;
/*      */         }
/*      */ 
/*  704 */         localRectangle1.setBounds(paramRectangle1.x, paramRectangle2.y, paramRectangle2.x - paramRectangle1.x, paramRectangle2.height);
/*      */ 
/*  706 */         if ((localRectangle1.width > 0) && (localRectangle1.height > 0)) {
/*  707 */           localRectangle3 = new Rectangle(localRectangle1);
/*  708 */           i++;
/*      */         }
/*      */ 
/*  711 */         localRectangle1.setBounds(paramRectangle1.x, paramRectangle2.y + paramRectangle2.height, paramRectangle1.width, paramRectangle1.y + paramRectangle1.height - (paramRectangle2.y + paramRectangle2.height));
/*      */ 
/*  713 */         if ((localRectangle1.width > 0) && (localRectangle1.height > 0)) {
/*  714 */           localRectangle4 = new Rectangle(localRectangle1);
/*  715 */           i++;
/*      */         }
/*      */       }
/*  718 */     } else if ((paramRectangle2.x >= paramRectangle1.x) && (paramRectangle2.x + paramRectangle2.width <= paramRectangle1.x + paramRectangle1.width)) {
/*  719 */       if ((paramRectangle2.y <= paramRectangle1.y) && (paramRectangle2.y + paramRectangle2.height > paramRectangle1.y + paramRectangle1.height)) {
/*  720 */         localRectangle1.setBounds(paramRectangle1.x, paramRectangle1.y, paramRectangle2.x - paramRectangle1.x, paramRectangle1.height);
/*  721 */         if ((localRectangle1.width > 0) && (localRectangle1.height > 0)) {
/*  722 */           localRectangle2 = new Rectangle(localRectangle1);
/*  723 */           i++;
/*      */         }
/*  725 */         localRectangle1.setBounds(paramRectangle2.x + paramRectangle2.width, paramRectangle1.y, paramRectangle1.x + paramRectangle1.width - (paramRectangle2.x + paramRectangle2.width), paramRectangle1.height);
/*      */ 
/*  727 */         if ((localRectangle1.width > 0) && (localRectangle1.height > 0)) {
/*  728 */           localRectangle3 = new Rectangle(localRectangle1);
/*  729 */           i++;
/*      */         }
/*  731 */       } else if (paramRectangle2.y <= paramRectangle1.y) {
/*  732 */         localRectangle1.setBounds(paramRectangle1.x, paramRectangle1.y, paramRectangle2.x - paramRectangle1.x, paramRectangle1.height);
/*  733 */         if ((localRectangle1.width > 0) && (localRectangle1.height > 0)) {
/*  734 */           localRectangle2 = new Rectangle(localRectangle1);
/*  735 */           i++;
/*      */         }
/*      */ 
/*  738 */         localRectangle1.setBounds(paramRectangle2.x, paramRectangle2.y + paramRectangle2.height, paramRectangle2.width, paramRectangle1.y + paramRectangle1.height - (paramRectangle2.y + paramRectangle2.height));
/*      */ 
/*  741 */         if ((localRectangle1.width > 0) && (localRectangle1.height > 0)) {
/*  742 */           localRectangle3 = new Rectangle(localRectangle1);
/*  743 */           i++;
/*      */         }
/*      */ 
/*  746 */         localRectangle1.setBounds(paramRectangle2.x + paramRectangle2.width, paramRectangle1.y, paramRectangle1.x + paramRectangle1.width - (paramRectangle2.x + paramRectangle2.width), paramRectangle1.height);
/*      */ 
/*  748 */         if ((localRectangle1.width > 0) && (localRectangle1.height > 0)) {
/*  749 */           localRectangle4 = new Rectangle(localRectangle1);
/*  750 */           i++;
/*      */         }
/*      */       } else {
/*  753 */         localRectangle1.setBounds(paramRectangle1.x, paramRectangle1.y, paramRectangle2.x - paramRectangle1.x, paramRectangle1.height);
/*  754 */         if ((localRectangle1.width > 0) && (localRectangle1.height > 0)) {
/*  755 */           localRectangle2 = new Rectangle(localRectangle1);
/*  756 */           i++;
/*      */         }
/*      */ 
/*  759 */         localRectangle1.setBounds(paramRectangle2.x, paramRectangle1.y, paramRectangle2.width, paramRectangle2.y - paramRectangle1.y);
/*      */ 
/*  761 */         if ((localRectangle1.width > 0) && (localRectangle1.height > 0)) {
/*  762 */           localRectangle3 = new Rectangle(localRectangle1);
/*  763 */           i++;
/*      */         }
/*      */ 
/*  766 */         localRectangle1.setBounds(paramRectangle2.x + paramRectangle2.width, paramRectangle1.y, paramRectangle1.x + paramRectangle1.width - (paramRectangle2.x + paramRectangle2.width), paramRectangle1.height);
/*      */ 
/*  768 */         if ((localRectangle1.width > 0) && (localRectangle1.height > 0)) {
/*  769 */           localRectangle4 = new Rectangle(localRectangle1);
/*  770 */           i++;
/*      */         }
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  776 */     Rectangle[] arrayOfRectangle = new Rectangle[i];
/*  777 */     i = 0;
/*  778 */     if (localRectangle2 != null)
/*  779 */       arrayOfRectangle[(i++)] = localRectangle2;
/*  780 */     if (localRectangle3 != null)
/*  781 */       arrayOfRectangle[(i++)] = localRectangle3;
/*  782 */     if (localRectangle4 != null)
/*  783 */       arrayOfRectangle[(i++)] = localRectangle4;
/*  784 */     if (localRectangle5 != null)
/*  785 */       arrayOfRectangle[(i++)] = localRectangle5;
/*  786 */     return arrayOfRectangle;
/*      */   }
/*      */ 
/*      */   public static boolean isLeftMouseButton(MouseEvent paramMouseEvent)
/*      */   {
/*  796 */     return (paramMouseEvent.getModifiers() & 0x10) != 0;
/*      */   }
/*      */ 
/*      */   public static boolean isMiddleMouseButton(MouseEvent paramMouseEvent)
/*      */   {
/*  806 */     return (paramMouseEvent.getModifiers() & 0x8) == 8;
/*      */   }
/*      */ 
/*      */   public static boolean isRightMouseButton(MouseEvent paramMouseEvent)
/*      */   {
/*  816 */     return (paramMouseEvent.getModifiers() & 0x4) == 4;
/*      */   }
/*      */ 
/*      */   public static int computeStringWidth(FontMetrics paramFontMetrics, String paramString)
/*      */   {
/*  831 */     return SwingUtilities2.stringWidth(null, paramFontMetrics, paramString);
/*      */   }
/*      */ 
/*      */   public static String layoutCompoundLabel(JComponent paramJComponent, FontMetrics paramFontMetrics, String paramString, Icon paramIcon, int paramInt1, int paramInt2, int paramInt3, int paramInt4, Rectangle paramRectangle1, Rectangle paramRectangle2, Rectangle paramRectangle3, int paramInt5)
/*      */   {
/*  855 */     int i = 1;
/*  856 */     int j = paramInt2;
/*  857 */     int k = paramInt4;
/*      */ 
/*  859 */     if ((paramJComponent != null) && 
/*  860 */       (!paramJComponent.getComponentOrientation().isLeftToRight())) {
/*  861 */       i = 0;
/*      */     }
/*      */ 
/*  867 */     switch (paramInt2) {
/*      */     case 10:
/*  869 */       j = i != 0 ? 2 : 4;
/*  870 */       break;
/*      */     case 11:
/*  872 */       j = i != 0 ? 4 : 2;
/*      */     }
/*      */ 
/*  878 */     switch (paramInt4) {
/*      */     case 10:
/*  880 */       k = i != 0 ? 2 : 4;
/*  881 */       break;
/*      */     case 11:
/*  883 */       k = i != 0 ? 4 : 2;
/*      */     }
/*      */ 
/*  887 */     return layoutCompoundLabelImpl(paramJComponent, paramFontMetrics, paramString, paramIcon, paramInt1, j, paramInt3, k, paramRectangle1, paramRectangle2, paramRectangle3, paramInt5);
/*      */   }
/*      */ 
/*      */   public static String layoutCompoundLabel(FontMetrics paramFontMetrics, String paramString, Icon paramIcon, int paramInt1, int paramInt2, int paramInt3, int paramInt4, Rectangle paramRectangle1, Rectangle paramRectangle2, Rectangle paramRectangle3, int paramInt5)
/*      */   {
/*  924 */     return layoutCompoundLabelImpl(null, paramFontMetrics, paramString, paramIcon, paramInt1, paramInt2, paramInt3, paramInt4, paramRectangle1, paramRectangle2, paramRectangle3, paramInt5);
/*      */   }
/*      */ 
/*      */   private static String layoutCompoundLabelImpl(JComponent paramJComponent, FontMetrics paramFontMetrics, String paramString, Icon paramIcon, int paramInt1, int paramInt2, int paramInt3, int paramInt4, Rectangle paramRectangle1, Rectangle paramRectangle2, Rectangle paramRectangle3, int paramInt5)
/*      */   {
/*  959 */     if (paramIcon != null) {
/*  960 */       paramRectangle2.width = paramIcon.getIconWidth();
/*  961 */       paramRectangle2.height = paramIcon.getIconHeight();
/*      */     }
/*      */     else {
/*  964 */       paramRectangle2.width = (paramRectangle2.height = 0);
/*      */     }
/*      */ 
/*  972 */     int i = (paramString == null) || (paramString.equals("")) ? 1 : 0;
/*  973 */     int j = 0;
/*  974 */     int k = 0;
/*      */     int m;
/*  981 */     if (i != 0) {
/*  982 */       paramRectangle3.width = (paramRectangle3.height = 0);
/*  983 */       paramString = "";
/*  984 */       m = 0;
/*      */     }
/*      */     else
/*      */     {
/*  988 */       m = paramIcon == null ? 0 : paramInt5;
/*      */ 
/*  990 */       if (paramInt4 == 0) {
/*  991 */         n = paramRectangle1.width;
/*      */       }
/*      */       else {
/*  994 */         n = paramRectangle1.width - (paramRectangle2.width + m);
/*      */       }
/*  996 */       Object localObject = paramJComponent != null ? (View)paramJComponent.getClientProperty("html") : null;
/*  997 */       if (localObject != null) {
/*  998 */         paramRectangle3.width = Math.min(n, (int)localObject.getPreferredSpan(0));
/*      */ 
/* 1000 */         paramRectangle3.height = ((int)localObject.getPreferredSpan(1));
/*      */       } else {
/* 1002 */         paramRectangle3.width = SwingUtilities2.stringWidth(paramJComponent, paramFontMetrics, paramString);
/* 1003 */         j = SwingUtilities2.getLeftSideBearing(paramJComponent, paramFontMetrics, paramString);
/* 1004 */         if (j < 0)
/*      */         {
/* 1015 */           paramRectangle3.width -= j;
/*      */         }
/* 1017 */         if (paramRectangle3.width > n) {
/* 1018 */           paramString = SwingUtilities2.clipString(paramJComponent, paramFontMetrics, paramString, n);
/*      */ 
/* 1020 */           paramRectangle3.width = SwingUtilities2.stringWidth(paramJComponent, paramFontMetrics, paramString);
/*      */         }
/* 1022 */         paramRectangle3.height = paramFontMetrics.getHeight();
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1031 */     if (paramInt3 == 1) {
/* 1032 */       if (paramInt4 != 0) {
/* 1033 */         paramRectangle3.y = 0;
/*      */       }
/*      */       else {
/* 1036 */         paramRectangle3.y = (-(paramRectangle3.height + m));
/*      */       }
/*      */     }
/* 1039 */     else if (paramInt3 == 0) {
/* 1040 */       paramRectangle3.y = (paramRectangle2.height / 2 - paramRectangle3.height / 2);
/*      */     }
/* 1043 */     else if (paramInt4 != 0) {
/* 1044 */       paramRectangle3.y = (paramRectangle2.height - paramRectangle3.height);
/*      */     }
/*      */     else {
/* 1047 */       paramRectangle3.y = (paramRectangle2.height + m);
/*      */     }
/*      */ 
/* 1051 */     if (paramInt4 == 2) {
/* 1052 */       paramRectangle3.x = (-(paramRectangle3.width + m));
/*      */     }
/* 1054 */     else if (paramInt4 == 0) {
/* 1055 */       paramRectangle3.x = (paramRectangle2.width / 2 - paramRectangle3.width / 2);
/*      */     }
/*      */     else {
/* 1058 */       paramRectangle3.x = (paramRectangle2.width + m);
/*      */     }
/*      */ 
/* 1072 */     int n = Math.min(paramRectangle2.x, paramRectangle3.x);
/* 1073 */     int i1 = Math.max(paramRectangle2.x + paramRectangle2.width, paramRectangle3.x + paramRectangle3.width) - n;
/*      */ 
/* 1075 */     int i2 = Math.min(paramRectangle2.y, paramRectangle3.y);
/* 1076 */     int i3 = Math.max(paramRectangle2.y + paramRectangle2.height, paramRectangle3.y + paramRectangle3.height) - i2;
/*      */     int i5;
/* 1081 */     if (paramInt1 == 1) {
/* 1082 */       i5 = paramRectangle1.y - i2;
/*      */     }
/* 1084 */     else if (paramInt1 == 0) {
/* 1085 */       i5 = paramRectangle1.y + paramRectangle1.height / 2 - (i2 + i3 / 2);
/*      */     }
/*      */     else
/* 1088 */       i5 = paramRectangle1.y + paramRectangle1.height - (i2 + i3);
/*      */     int i4;
/* 1091 */     if (paramInt2 == 2) {
/* 1092 */       i4 = paramRectangle1.x - n;
/*      */     }
/* 1094 */     else if (paramInt2 == 4) {
/* 1095 */       i4 = paramRectangle1.x + paramRectangle1.width - (n + i1);
/*      */     }
/*      */     else {
/* 1098 */       i4 = paramRectangle1.x + paramRectangle1.width / 2 - (n + i1 / 2);
/*      */     }
/*      */ 
/* 1105 */     paramRectangle3.x += i4;
/* 1106 */     paramRectangle3.y += i5;
/*      */ 
/* 1108 */     paramRectangle2.x += i4;
/* 1109 */     paramRectangle2.y += i5;
/*      */ 
/* 1111 */     if (j < 0)
/*      */     {
/* 1114 */       paramRectangle3.x -= j;
/*      */ 
/* 1116 */       paramRectangle3.width += j;
/*      */     }
/* 1118 */     if (k > 0) {
/* 1119 */       paramRectangle3.width -= k;
/*      */     }
/*      */ 
/* 1122 */     return paramString;
/*      */   }
/*      */ 
/*      */   public static void paintComponent(Graphics paramGraphics, Component paramComponent, Container paramContainer, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/* 1174 */     getCellRendererPane(paramComponent, paramContainer).paintComponent(paramGraphics, paramComponent, paramContainer, paramInt1, paramInt2, paramInt3, paramInt4, false);
/*      */   }
/*      */ 
/*      */   public static void paintComponent(Graphics paramGraphics, Component paramComponent, Container paramContainer, Rectangle paramRectangle)
/*      */   {
/* 1192 */     paintComponent(paramGraphics, paramComponent, paramContainer, paramRectangle.x, paramRectangle.y, paramRectangle.width, paramRectangle.height);
/*      */   }
/*      */ 
/*      */   private static CellRendererPane getCellRendererPane(Component paramComponent, Container paramContainer)
/*      */   {
/* 1202 */     Object localObject = paramComponent.getParent();
/* 1203 */     if ((localObject instanceof CellRendererPane)) {
/* 1204 */       if (((Container)localObject).getParent() != paramContainer)
/* 1205 */         paramContainer.add((Component)localObject);
/*      */     }
/*      */     else {
/* 1208 */       localObject = new CellRendererPane();
/* 1209 */       ((Container)localObject).add(paramComponent);
/* 1210 */       paramContainer.add((Component)localObject);
/*      */     }
/* 1212 */     return (CellRendererPane)localObject;
/*      */   }
/*      */ 
/*      */   public static void updateComponentTreeUI(Component paramComponent)
/*      */   {
/* 1221 */     updateComponentTreeUI0(paramComponent);
/* 1222 */     paramComponent.invalidate();
/* 1223 */     paramComponent.validate();
/* 1224 */     paramComponent.repaint();
/*      */   }
/*      */ 
/*      */   private static void updateComponentTreeUI0(Component paramComponent)
/*      */   {
/*      */     Object localObject2;
/* 1228 */     if ((paramComponent instanceof JComponent)) {
/* 1229 */       localObject1 = (JComponent)paramComponent;
/* 1230 */       ((JComponent)localObject1).updateUI();
/* 1231 */       localObject2 = ((JComponent)localObject1).getComponentPopupMenu();
/* 1232 */       if (localObject2 != null) {
/* 1233 */         updateComponentTreeUI((Component)localObject2);
/*      */       }
/*      */     }
/* 1236 */     Object localObject1 = null;
/* 1237 */     if ((paramComponent instanceof JMenu)) {
/* 1238 */       localObject1 = ((JMenu)paramComponent).getMenuComponents();
/*      */     }
/* 1240 */     else if ((paramComponent instanceof Container)) {
/* 1241 */       localObject1 = ((Container)paramComponent).getComponents();
/*      */     }
/* 1243 */     if (localObject1 != null)
/* 1244 */       for (Component localComponent : localObject1)
/* 1245 */         updateComponentTreeUI0(localComponent);
/*      */   }
/*      */ 
/*      */   public static void invokeLater(Runnable paramRunnable)
/*      */   {
/* 1288 */     EventQueue.invokeLater(paramRunnable);
/*      */   }
/*      */ 
/*      */   public static void invokeAndWait(Runnable paramRunnable)
/*      */     throws InterruptedException, InvocationTargetException
/*      */   {
/* 1347 */     EventQueue.invokeAndWait(paramRunnable);
/*      */   }
/*      */ 
/*      */   public static boolean isEventDispatchThread()
/*      */   {
/* 1360 */     return EventQueue.isDispatchThread();
/*      */   }
/*      */ 
/*      */   public static int getAccessibleIndexInParent(Component paramComponent)
/*      */   {
/* 1380 */     return paramComponent.getAccessibleContext().getAccessibleIndexInParent();
/*      */   }
/*      */ 
/*      */   public static Accessible getAccessibleAt(Component paramComponent, Point paramPoint)
/*      */   {
/* 1392 */     if ((paramComponent instanceof Container))
/* 1393 */       return paramComponent.getAccessibleContext().getAccessibleComponent().getAccessibleAt(paramPoint);
/* 1394 */     if ((paramComponent instanceof Accessible)) {
/* 1395 */       Accessible localAccessible = (Accessible)paramComponent;
/* 1396 */       if (localAccessible != null) {
/* 1397 */         AccessibleContext localAccessibleContext = localAccessible.getAccessibleContext();
/* 1398 */         if (localAccessibleContext != null)
/*      */         {
/* 1401 */           int i = localAccessibleContext.getAccessibleChildrenCount();
/* 1402 */           for (int j = 0; j < i; j++) {
/* 1403 */             localAccessible = localAccessibleContext.getAccessibleChild(j);
/* 1404 */             if (localAccessible != null) {
/* 1405 */               localAccessibleContext = localAccessible.getAccessibleContext();
/* 1406 */               if (localAccessibleContext != null) {
/* 1407 */                 AccessibleComponent localAccessibleComponent = localAccessibleContext.getAccessibleComponent();
/* 1408 */                 if ((localAccessibleComponent != null) && (localAccessibleComponent.isShowing())) {
/* 1409 */                   Point localPoint1 = localAccessibleComponent.getLocation();
/* 1410 */                   Point localPoint2 = new Point(paramPoint.x - localPoint1.x, paramPoint.y - localPoint1.y);
/*      */ 
/* 1412 */                   if (localAccessibleComponent.contains(localPoint2)) {
/* 1413 */                     return localAccessible;
/*      */                   }
/*      */                 }
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/* 1421 */       return (Accessible)paramComponent;
/*      */     }
/* 1423 */     return null;
/*      */   }
/*      */ 
/*      */   public static AccessibleStateSet getAccessibleStateSet(Component paramComponent)
/*      */   {
/* 1438 */     return paramComponent.getAccessibleContext().getAccessibleStateSet();
/*      */   }
/*      */ 
/*      */   public static int getAccessibleChildrenCount(Component paramComponent)
/*      */   {
/* 1453 */     return paramComponent.getAccessibleContext().getAccessibleChildrenCount();
/*      */   }
/*      */ 
/*      */   public static Accessible getAccessibleChild(Component paramComponent, int paramInt)
/*      */   {
/* 1467 */     return paramComponent.getAccessibleContext().getAccessibleChild(paramInt);
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   public static Component findFocusOwner(Component paramComponent)
/*      */   {
/* 1486 */     Component localComponent = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
/*      */ 
/* 1490 */     for (Object localObject = localComponent; localObject != null; 
/* 1491 */       localObject = (localObject instanceof Window) ? null : ((Component)localObject).getParent())
/*      */     {
/* 1493 */       if (localObject == paramComponent) {
/* 1494 */         return localComponent;
/*      */       }
/*      */     }
/*      */ 
/* 1498 */     return null;
/*      */   }
/*      */ 
/*      */   public static JRootPane getRootPane(Component paramComponent)
/*      */   {
/* 1507 */     if ((paramComponent instanceof RootPaneContainer)) {
/* 1508 */       return ((RootPaneContainer)paramComponent).getRootPane();
/*      */     }
/* 1510 */     for (; paramComponent != null; paramComponent = paramComponent.getParent()) {
/* 1511 */       if ((paramComponent instanceof JRootPane)) {
/* 1512 */         return (JRootPane)paramComponent;
/*      */       }
/*      */     }
/* 1515 */     return null;
/*      */   }
/*      */ 
/*      */   public static Component getRoot(Component paramComponent)
/*      */   {
/* 1524 */     Object localObject1 = null;
/* 1525 */     for (Object localObject2 = paramComponent; localObject2 != null; localObject2 = ((Component)localObject2).getParent()) {
/* 1526 */       if ((localObject2 instanceof Window)) {
/* 1527 */         return localObject2;
/*      */       }
/* 1529 */       if ((localObject2 instanceof Applet)) {
/* 1530 */         localObject1 = localObject2;
/*      */       }
/*      */     }
/* 1533 */     return localObject1;
/*      */   }
/*      */ 
/*      */   static JComponent getPaintingOrigin(JComponent paramJComponent) {
/* 1537 */     Object localObject = paramJComponent;
/* 1538 */     while (((localObject = ((Container)localObject).getParent()) instanceof JComponent)) {
/* 1539 */       JComponent localJComponent = (JComponent)localObject;
/* 1540 */       if (localJComponent.isPaintingOrigin()) {
/* 1541 */         return localJComponent;
/*      */       }
/*      */     }
/* 1544 */     return null;
/*      */   }
/*      */ 
/*      */   public static boolean processKeyBindings(KeyEvent paramKeyEvent)
/*      */   {
/* 1564 */     if (paramKeyEvent != null) {
/* 1565 */       if (paramKeyEvent.isConsumed()) {
/* 1566 */         return false;
/*      */       }
/*      */ 
/* 1569 */       Object localObject = paramKeyEvent.getComponent();
/* 1570 */       boolean bool = paramKeyEvent.getID() == 401;
/*      */ 
/* 1572 */       if (!isValidKeyEventForKeyBindings(paramKeyEvent)) {
/* 1573 */         return false;
/*      */       }
/*      */ 
/* 1577 */       while (localObject != null) {
/* 1578 */         if ((localObject instanceof JComponent)) {
/* 1579 */           return ((JComponent)localObject).processKeyBindings(paramKeyEvent, bool);
/*      */         }
/*      */ 
/* 1582 */         if (((localObject instanceof Applet)) || ((localObject instanceof Window)))
/*      */         {
/* 1586 */           return JComponent.processKeyBindingsForAllComponents(paramKeyEvent, (Container)localObject, bool);
/*      */         }
/*      */ 
/* 1589 */         localObject = ((Component)localObject).getParent();
/*      */       }
/*      */     }
/* 1592 */     return false;
/*      */   }
/*      */ 
/*      */   static boolean isValidKeyEventForKeyBindings(KeyEvent paramKeyEvent)
/*      */   {
/* 1600 */     return true;
/*      */   }
/*      */ 
/*      */   public static boolean notifyAction(Action paramAction, KeyStroke paramKeyStroke, KeyEvent paramKeyEvent, Object paramObject, int paramInt)
/*      */   {
/* 1623 */     if (paramAction == null) {
/* 1624 */       return false;
/*      */     }
/* 1626 */     if ((paramAction instanceof UIAction)) {
/* 1627 */       if (!((UIAction)paramAction).isEnabled(paramObject)) {
/* 1628 */         return false;
/*      */       }
/*      */     }
/* 1631 */     else if (!paramAction.isEnabled()) {
/* 1632 */       return false;
/*      */     }
/*      */ 
/* 1638 */     Object localObject = paramAction.getValue("ActionCommandKey");
/*      */     int i;
/* 1639 */     if ((localObject == null) && ((paramAction instanceof JComponent.ActionStandin)))
/*      */     {
/* 1642 */       i = 1;
/*      */     }
/*      */     else
/* 1645 */       i = 0;
/*      */     String str;
/* 1651 */     if (localObject != null) {
/* 1652 */       str = localObject.toString();
/*      */     }
/* 1654 */     else if ((i == 0) && (paramKeyEvent.getKeyChar() != 65535)) {
/* 1655 */       str = String.valueOf(paramKeyEvent.getKeyChar());
/*      */     }
/*      */     else
/*      */     {
/* 1660 */       str = null;
/*      */     }
/* 1662 */     paramAction.actionPerformed(new ActionEvent(paramObject, 1001, str, paramKeyEvent.getWhen(), paramInt));
/*      */ 
/* 1665 */     return true;
/*      */   }
/*      */ 
/*      */   public static void replaceUIInputMap(JComponent paramJComponent, int paramInt, InputMap paramInputMap)
/*      */   {
/* 1678 */     Object localObject = paramJComponent.getInputMap(paramInt, paramInputMap != null);
/*      */ 
/* 1680 */     while (localObject != null) {
/* 1681 */       InputMap localInputMap = ((InputMap)localObject).getParent();
/* 1682 */       if ((localInputMap == null) || ((localInputMap instanceof UIResource))) {
/* 1683 */         ((InputMap)localObject).setParent(paramInputMap);
/* 1684 */         return;
/*      */       }
/* 1686 */       localObject = localInputMap;
/*      */     }
/*      */   }
/*      */ 
/*      */   public static void replaceUIActionMap(JComponent paramJComponent, ActionMap paramActionMap)
/*      */   {
/* 1700 */     Object localObject = paramJComponent.getActionMap(paramActionMap != null);
/*      */ 
/* 1702 */     while (localObject != null) {
/* 1703 */       ActionMap localActionMap = ((ActionMap)localObject).getParent();
/* 1704 */       if ((localActionMap == null) || ((localActionMap instanceof UIResource))) {
/* 1705 */         ((ActionMap)localObject).setParent(paramActionMap);
/* 1706 */         return;
/*      */       }
/* 1708 */       localObject = localActionMap;
/*      */     }
/*      */   }
/*      */ 
/*      */   public static InputMap getUIInputMap(JComponent paramJComponent, int paramInt)
/*      */   {
/* 1722 */     Object localObject = paramJComponent.getInputMap(paramInt, false);
/* 1723 */     while (localObject != null) {
/* 1724 */       InputMap localInputMap = ((InputMap)localObject).getParent();
/* 1725 */       if ((localInputMap instanceof UIResource)) {
/* 1726 */         return localInputMap;
/*      */       }
/* 1728 */       localObject = localInputMap;
/*      */     }
/* 1730 */     return null;
/*      */   }
/*      */ 
/*      */   public static ActionMap getUIActionMap(JComponent paramJComponent)
/*      */   {
/* 1741 */     Object localObject = paramJComponent.getActionMap(false);
/* 1742 */     while (localObject != null) {
/* 1743 */       ActionMap localActionMap = ((ActionMap)localObject).getParent();
/* 1744 */       if ((localActionMap instanceof UIResource)) {
/* 1745 */         return localActionMap;
/*      */       }
/* 1747 */       localObject = localActionMap;
/*      */     }
/* 1749 */     return null;
/*      */   }
/*      */ 
/*      */   static Frame getSharedOwnerFrame()
/*      */     throws HeadlessException
/*      */   {
/* 1829 */     Object localObject = (Frame)appContextGet(sharedOwnerFrameKey);
/*      */ 
/* 1831 */     if (localObject == null) {
/* 1832 */       localObject = new SharedOwnerFrame();
/* 1833 */       appContextPut(sharedOwnerFrameKey, localObject);
/*      */     }
/*      */ 
/* 1836 */     return localObject;
/*      */   }
/*      */ 
/*      */   static WindowListener getSharedOwnerFrameShutdownListener()
/*      */     throws HeadlessException
/*      */   {
/* 1847 */     Frame localFrame = getSharedOwnerFrame();
/* 1848 */     return (WindowListener)localFrame;
/*      */   }
/*      */ 
/*      */   static Object appContextGet(Object paramObject)
/*      */   {
/* 1859 */     return AppContext.getAppContext().get(paramObject);
/*      */   }
/*      */ 
/*      */   static void appContextPut(Object paramObject1, Object paramObject2) {
/* 1863 */     AppContext.getAppContext().put(paramObject1, paramObject2);
/*      */   }
/*      */ 
/*      */   static void appContextRemove(Object paramObject) {
/* 1867 */     AppContext.getAppContext().remove(paramObject);
/*      */   }
/*      */ 
/*      */   static Class<?> loadSystemClass(String paramString) throws ClassNotFoundException
/*      */   {
/* 1872 */     ReflectUtil.checkPackageAccess(paramString);
/* 1873 */     return Class.forName(paramString, true, Thread.currentThread().getContextClassLoader());
/*      */   }
/*      */ 
/*      */   static boolean isLeftToRight(Component paramComponent)
/*      */   {
/* 1883 */     return paramComponent.getComponentOrientation().isLeftToRight();
/*      */   }
/*      */   private SwingUtilities() {
/* 1886 */     throw new Error("SwingUtilities is just a container for static methods");
/*      */   }
/*      */ 
/*      */   static boolean doesIconReferenceImage(Icon paramIcon, Image paramImage)
/*      */   {
/* 1894 */     Object localObject = (paramIcon != null) && ((paramIcon instanceof ImageIcon)) ? ((ImageIcon)paramIcon).getImage() : null;
/*      */ 
/* 1896 */     return localObject == paramImage;
/*      */   }
/*      */ 
/*      */   static int findDisplayedMnemonicIndex(String paramString, int paramInt)
/*      */   {
/* 1909 */     if ((paramString == null) || (paramInt == 0)) {
/* 1910 */       return -1;
/*      */     }
/*      */ 
/* 1913 */     int i = Character.toUpperCase((char)paramInt);
/* 1914 */     int j = Character.toLowerCase((char)paramInt);
/*      */ 
/* 1916 */     int k = paramString.indexOf(i);
/* 1917 */     int m = paramString.indexOf(j);
/*      */ 
/* 1919 */     if (k == -1)
/* 1920 */       return m;
/* 1921 */     if (m == -1) {
/* 1922 */       return k;
/*      */     }
/* 1924 */     return m < k ? m : k;
/*      */   }
/*      */ 
/*      */   public static Rectangle calculateInnerArea(JComponent paramJComponent, Rectangle paramRectangle)
/*      */   {
/* 1947 */     if (paramJComponent == null) {
/* 1948 */       return null;
/*      */     }
/* 1950 */     Rectangle localRectangle = paramRectangle;
/* 1951 */     Insets localInsets = paramJComponent.getInsets();
/*      */ 
/* 1953 */     if (localRectangle == null) {
/* 1954 */       localRectangle = new Rectangle();
/*      */     }
/*      */ 
/* 1957 */     localRectangle.x = localInsets.left;
/* 1958 */     localRectangle.y = localInsets.top;
/* 1959 */     localRectangle.width = (paramJComponent.getWidth() - localInsets.left - localInsets.right);
/* 1960 */     localRectangle.height = (paramJComponent.getHeight() - localInsets.top - localInsets.bottom);
/*      */ 
/* 1962 */     return localRectangle;
/*      */   }
/*      */ 
/*      */   static void updateRendererOrEditorUI(Object paramObject) {
/* 1966 */     if (paramObject == null) {
/* 1967 */       return;
/*      */     }
/*      */ 
/* 1970 */     Component localComponent = null;
/*      */ 
/* 1972 */     if ((paramObject instanceof Component)) {
/* 1973 */       localComponent = (Component)paramObject;
/*      */     }
/* 1975 */     if ((paramObject instanceof DefaultCellEditor)) {
/* 1976 */       localComponent = ((DefaultCellEditor)paramObject).getComponent();
/*      */     }
/*      */ 
/* 1979 */     if (localComponent != null)
/* 1980 */       updateComponentTreeUI(localComponent);
/*      */   }
/*      */ 
/*      */   public static Container getUnwrappedParent(Component paramComponent)
/*      */   {
/* 2001 */     Container localContainer = paramComponent.getParent();
/* 2002 */     while ((localContainer instanceof JLayer)) {
/* 2003 */       localContainer = localContainer.getParent();
/*      */     }
/* 2005 */     return localContainer;
/*      */   }
/*      */ 
/*      */   public static Component getUnwrappedView(JViewport paramJViewport)
/*      */   {
/* 2032 */     Component localComponent = paramJViewport.getView();
/* 2033 */     while ((localComponent instanceof JLayer)) {
/* 2034 */       localComponent = ((JLayer)localComponent).getView();
/*      */     }
/* 2036 */     return localComponent;
/*      */   }
/*      */ 
/*      */   static Container getValidateRoot(Container paramContainer, boolean paramBoolean)
/*      */   {
/* 2060 */     Container localContainer = null;
/*      */ 
/* 2062 */     for (; paramContainer != null; paramContainer = paramContainer.getParent())
/*      */     {
/* 2064 */       if ((!paramContainer.isDisplayable()) || ((paramContainer instanceof CellRendererPane))) {
/* 2065 */         return null;
/*      */       }
/* 2067 */       if (paramContainer.isValidateRoot()) {
/* 2068 */         localContainer = paramContainer;
/* 2069 */         break;
/*      */       }
/*      */     }
/*      */ 
/* 2073 */     if (localContainer == null) {
/* 2074 */       return null;
/*      */     }
/*      */ 
/* 2077 */     for (; paramContainer != null; paramContainer = paramContainer.getParent()) {
/* 2078 */       if ((!paramContainer.isDisplayable()) || ((paramBoolean) && (!paramContainer.isVisible()))) {
/* 2079 */         return null;
/*      */       }
/* 2081 */       if (((paramContainer instanceof Window)) || ((paramContainer instanceof Applet))) {
/* 2082 */         return localContainer;
/*      */       }
/*      */     }
/*      */ 
/* 2086 */     return null;
/*      */   }
/*      */ 
/*      */   static class SharedOwnerFrame extends Frame
/*      */     implements WindowListener
/*      */   {
/*      */     public void addNotify()
/*      */     {
/* 1759 */       super.addNotify();
/* 1760 */       installListeners();
/*      */     }
/*      */ 
/*      */     void installListeners()
/*      */     {
/* 1767 */       Window[] arrayOfWindow1 = getOwnedWindows();
/* 1768 */       for (Window localWindow : arrayOfWindow1)
/* 1769 */         if (localWindow != null) {
/* 1770 */           localWindow.removeWindowListener(this);
/* 1771 */           localWindow.addWindowListener(this);
/*      */         }
/*      */     }
/*      */ 
/*      */     public void windowClosed(WindowEvent paramWindowEvent)
/*      */     {
/* 1781 */       synchronized (getTreeLock()) {
/* 1782 */         Window[] arrayOfWindow1 = getOwnedWindows();
/* 1783 */         for (Window localWindow : arrayOfWindow1) {
/* 1784 */           if (localWindow != null) {
/* 1785 */             if (localWindow.isDisplayable()) {
/* 1786 */               return;
/*      */             }
/* 1788 */             localWindow.removeWindowListener(this);
/*      */           }
/*      */         }
/* 1791 */         dispose();
/*      */       }
/*      */     }
/*      */     public void windowOpened(WindowEvent paramWindowEvent) {
/*      */     }
/*      */     public void windowClosing(WindowEvent paramWindowEvent) {
/*      */     }
/*      */     public void windowIconified(WindowEvent paramWindowEvent) {
/*      */     }
/*      */     public void windowDeiconified(WindowEvent paramWindowEvent) {
/*      */     }
/*      */     public void windowActivated(WindowEvent paramWindowEvent) {
/*      */     }
/*      */ 
/*      */     public void windowDeactivated(WindowEvent paramWindowEvent) {
/*      */     }
/*      */ 
/*      */     public void show() {
/*      */     }
/*      */ 
/*      */     public void dispose() {
/*      */       try { getToolkit().getSystemEventQueue();
/* 1813 */         super.dispose();
/*      */       }
/*      */       catch (Exception localException)
/*      */       {
/*      */       }
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.SwingUtilities
 * JD-Core Version:    0.6.2
 */