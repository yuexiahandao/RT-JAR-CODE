/*     */ package javax.swing.plaf.basic;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.Container;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Insets;
/*     */ import java.awt.KeyboardFocusManager;
/*     */ import java.awt.Point;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.beans.PropertyVetoException;
/*     */ import javax.swing.AbstractAction;
/*     */ import javax.swing.DefaultDesktopManager;
/*     */ import javax.swing.DesktopManager;
/*     */ import javax.swing.InputMap;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JDesktopPane;
/*     */ import javax.swing.JInternalFrame;
/*     */ import javax.swing.KeyStroke;
/*     */ import javax.swing.LookAndFeel;
/*     */ import javax.swing.SortingFocusTraversalPolicy;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.DesktopPaneUI;
/*     */ import javax.swing.plaf.UIResource;
/*     */ import sun.swing.DefaultLookup;
/*     */ import sun.swing.UIAction;
/*     */ 
/*     */ public class BasicDesktopPaneUI extends DesktopPaneUI
/*     */ {
/*  51 */   private static final Actions SHARED_ACTION = new Actions();
/*  52 */   private static Dimension minSize = new Dimension(0, 0);
/*  53 */   private static Dimension maxSize = new Dimension(2147483647, 2147483647);
/*     */   private Handler handler;
/*     */   private PropertyChangeListener pcl;
/*     */   protected JDesktopPane desktop;
/*     */   protected DesktopManager desktopManager;
/*     */ 
/*     */   @Deprecated
/*     */   protected KeyStroke minimizeKey;
/*     */ 
/*     */   @Deprecated
/*     */   protected KeyStroke maximizeKey;
/*     */ 
/*     */   @Deprecated
/*     */   protected KeyStroke closeKey;
/*     */ 
/*     */   @Deprecated
/*     */   protected KeyStroke navigateKey;
/*     */ 
/*     */   @Deprecated
/*     */   protected KeyStroke navigateKey2;
/*     */ 
/*     */   public static ComponentUI createUI(JComponent paramJComponent)
/*     */   {
/* 113 */     return new BasicDesktopPaneUI();
/*     */   }
/*     */ 
/*     */   public void installUI(JComponent paramJComponent)
/*     */   {
/* 120 */     this.desktop = ((JDesktopPane)paramJComponent);
/* 121 */     installDefaults();
/* 122 */     installDesktopManager();
/* 123 */     installListeners();
/* 124 */     installKeyboardActions();
/*     */   }
/*     */ 
/*     */   public void uninstallUI(JComponent paramJComponent) {
/* 128 */     uninstallKeyboardActions();
/* 129 */     uninstallListeners();
/* 130 */     uninstallDesktopManager();
/* 131 */     uninstallDefaults();
/* 132 */     this.desktop = null;
/* 133 */     this.handler = null;
/*     */   }
/*     */ 
/*     */   protected void installDefaults() {
/* 137 */     if ((this.desktop.getBackground() == null) || ((this.desktop.getBackground() instanceof UIResource)))
/*     */     {
/* 139 */       this.desktop.setBackground(UIManager.getColor("Desktop.background"));
/*     */     }
/* 141 */     LookAndFeel.installProperty(this.desktop, "opaque", Boolean.TRUE);
/*     */   }
/*     */ 
/*     */   protected void uninstallDefaults()
/*     */   {
/*     */   }
/*     */ 
/*     */   protected void installListeners()
/*     */   {
/* 155 */     this.pcl = createPropertyChangeListener();
/* 156 */     this.desktop.addPropertyChangeListener(this.pcl);
/*     */   }
/*     */ 
/*     */   protected void uninstallListeners()
/*     */   {
/* 168 */     this.desktop.removePropertyChangeListener(this.pcl);
/* 169 */     this.pcl = null;
/*     */   }
/*     */ 
/*     */   protected void installDesktopManager() {
/* 173 */     this.desktopManager = this.desktop.getDesktopManager();
/* 174 */     if (this.desktopManager == null) {
/* 175 */       this.desktopManager = new BasicDesktopManager(null);
/* 176 */       this.desktop.setDesktopManager(this.desktopManager);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void uninstallDesktopManager() {
/* 181 */     if ((this.desktop.getDesktopManager() instanceof UIResource)) {
/* 182 */       this.desktop.setDesktopManager(null);
/*     */     }
/* 184 */     this.desktopManager = null;
/*     */   }
/*     */ 
/*     */   protected void installKeyboardActions() {
/* 188 */     InputMap localInputMap = getInputMap(2);
/* 189 */     if (localInputMap != null) {
/* 190 */       SwingUtilities.replaceUIInputMap(this.desktop, 2, localInputMap);
/*     */     }
/*     */ 
/* 193 */     localInputMap = getInputMap(1);
/* 194 */     if (localInputMap != null) {
/* 195 */       SwingUtilities.replaceUIInputMap(this.desktop, 1, localInputMap);
/*     */     }
/*     */ 
/* 200 */     LazyActionMap.installLazyActionMap(this.desktop, BasicDesktopPaneUI.class, "DesktopPane.actionMap");
/*     */ 
/* 202 */     registerKeyboardActions();
/*     */   }
/*     */ 
/*     */   protected void registerKeyboardActions() {
/*     */   }
/*     */ 
/*     */   protected void unregisterKeyboardActions() {
/*     */   }
/*     */ 
/*     */   InputMap getInputMap(int paramInt) {
/* 212 */     if (paramInt == 2) {
/* 213 */       return createInputMap(paramInt);
/*     */     }
/* 215 */     if (paramInt == 1) {
/* 216 */       return (InputMap)DefaultLookup.get(this.desktop, this, "Desktop.ancestorInputMap");
/*     */     }
/*     */ 
/* 219 */     return null;
/*     */   }
/*     */ 
/*     */   InputMap createInputMap(int paramInt) {
/* 223 */     if (paramInt == 2) {
/* 224 */       Object[] arrayOfObject = (Object[])DefaultLookup.get(this.desktop, this, "Desktop.windowBindings");
/*     */ 
/* 227 */       if (arrayOfObject != null) {
/* 228 */         return LookAndFeel.makeComponentInputMap(this.desktop, arrayOfObject);
/*     */       }
/*     */     }
/* 231 */     return null;
/*     */   }
/*     */ 
/*     */   static void loadActionMap(LazyActionMap paramLazyActionMap) {
/* 235 */     paramLazyActionMap.put(new Actions(Actions.RESTORE));
/* 236 */     paramLazyActionMap.put(new Actions(Actions.CLOSE));
/* 237 */     paramLazyActionMap.put(new Actions(Actions.MOVE));
/* 238 */     paramLazyActionMap.put(new Actions(Actions.RESIZE));
/* 239 */     paramLazyActionMap.put(new Actions(Actions.LEFT));
/* 240 */     paramLazyActionMap.put(new Actions(Actions.SHRINK_LEFT));
/* 241 */     paramLazyActionMap.put(new Actions(Actions.RIGHT));
/* 242 */     paramLazyActionMap.put(new Actions(Actions.SHRINK_RIGHT));
/* 243 */     paramLazyActionMap.put(new Actions(Actions.UP));
/* 244 */     paramLazyActionMap.put(new Actions(Actions.SHRINK_UP));
/* 245 */     paramLazyActionMap.put(new Actions(Actions.DOWN));
/* 246 */     paramLazyActionMap.put(new Actions(Actions.SHRINK_DOWN));
/* 247 */     paramLazyActionMap.put(new Actions(Actions.ESCAPE));
/* 248 */     paramLazyActionMap.put(new Actions(Actions.MINIMIZE));
/* 249 */     paramLazyActionMap.put(new Actions(Actions.MAXIMIZE));
/* 250 */     paramLazyActionMap.put(new Actions(Actions.NEXT_FRAME));
/* 251 */     paramLazyActionMap.put(new Actions(Actions.PREVIOUS_FRAME));
/* 252 */     paramLazyActionMap.put(new Actions(Actions.NAVIGATE_NEXT));
/* 253 */     paramLazyActionMap.put(new Actions(Actions.NAVIGATE_PREVIOUS));
/*     */   }
/*     */ 
/*     */   protected void uninstallKeyboardActions() {
/* 257 */     unregisterKeyboardActions();
/* 258 */     SwingUtilities.replaceUIInputMap(this.desktop, 2, null);
/*     */ 
/* 260 */     SwingUtilities.replaceUIInputMap(this.desktop, 1, null);
/*     */ 
/* 262 */     SwingUtilities.replaceUIActionMap(this.desktop, null);
/*     */   }
/*     */   public void paint(Graphics paramGraphics, JComponent paramJComponent) {
/*     */   }
/*     */   public Dimension getPreferredSize(JComponent paramJComponent) {
/* 267 */     return null;
/*     */   }
/*     */   public Dimension getMinimumSize(JComponent paramJComponent) {
/* 270 */     return minSize;
/*     */   }
/*     */   public Dimension getMaximumSize(JComponent paramJComponent) {
/* 273 */     return maxSize;
/*     */   }
/*     */ 
/*     */   protected PropertyChangeListener createPropertyChangeListener()
/*     */   {
/* 285 */     return getHandler();
/*     */   }
/*     */ 
/*     */   private Handler getHandler() {
/* 289 */     if (this.handler == null) {
/* 290 */       this.handler = new Handler(null);
/*     */     }
/* 292 */     return this.handler;
/*     */   }
/*     */ 
/*     */   private static class Actions extends UIAction
/*     */   {
/* 312 */     private static String CLOSE = "close";
/* 313 */     private static String ESCAPE = "escape";
/* 314 */     private static String MAXIMIZE = "maximize";
/* 315 */     private static String MINIMIZE = "minimize";
/* 316 */     private static String MOVE = "move";
/* 317 */     private static String RESIZE = "resize";
/* 318 */     private static String RESTORE = "restore";
/* 319 */     private static String LEFT = "left";
/* 320 */     private static String RIGHT = "right";
/* 321 */     private static String UP = "up";
/* 322 */     private static String DOWN = "down";
/* 323 */     private static String SHRINK_LEFT = "shrinkLeft";
/* 324 */     private static String SHRINK_RIGHT = "shrinkRight";
/* 325 */     private static String SHRINK_UP = "shrinkUp";
/* 326 */     private static String SHRINK_DOWN = "shrinkDown";
/* 327 */     private static String NEXT_FRAME = "selectNextFrame";
/* 328 */     private static String PREVIOUS_FRAME = "selectPreviousFrame";
/* 329 */     private static String NAVIGATE_NEXT = "navigateNext";
/* 330 */     private static String NAVIGATE_PREVIOUS = "navigatePrevious";
/* 331 */     private final int MOVE_RESIZE_INCREMENT = 10;
/* 332 */     private static boolean moving = false;
/* 333 */     private static boolean resizing = false;
/* 334 */     private static JInternalFrame sourceFrame = null;
/* 335 */     private static Component focusOwner = null;
/*     */ 
/*     */     Actions() {
/* 338 */       super();
/*     */     }
/*     */ 
/*     */     Actions(String paramString) {
/* 342 */       super();
/*     */     }
/*     */ 
/*     */     public void actionPerformed(ActionEvent paramActionEvent) {
/* 346 */       JDesktopPane localJDesktopPane = (JDesktopPane)paramActionEvent.getSource();
/* 347 */       String str = getName();
/*     */ 
/* 349 */       if ((CLOSE == str) || (MAXIMIZE == str) || (MINIMIZE == str) || (RESTORE == str))
/*     */       {
/* 351 */         setState(localJDesktopPane, str);
/*     */       }
/* 353 */       else if (ESCAPE == str) {
/* 354 */         if ((sourceFrame == localJDesktopPane.getSelectedFrame()) && (focusOwner != null))
/*     */         {
/* 356 */           focusOwner.requestFocus();
/*     */         }
/* 358 */         moving = false;
/* 359 */         resizing = false;
/* 360 */         sourceFrame = null;
/* 361 */         focusOwner = null;
/*     */       }
/* 363 */       else if ((MOVE == str) || (RESIZE == str)) {
/* 364 */         sourceFrame = localJDesktopPane.getSelectedFrame();
/* 365 */         if (sourceFrame == null) {
/* 366 */           return;
/*     */         }
/* 368 */         moving = str == MOVE;
/* 369 */         resizing = str == RESIZE;
/*     */ 
/* 371 */         focusOwner = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
/*     */ 
/* 373 */         if (!SwingUtilities.isDescendingFrom(focusOwner, sourceFrame)) {
/* 374 */           focusOwner = null;
/*     */         }
/* 376 */         sourceFrame.requestFocus();
/*     */       }
/*     */       else
/*     */       {
/*     */         Object localObject1;
/*     */         Object localObject2;
/*     */         Object localObject3;
/* 378 */         if ((LEFT == str) || (RIGHT == str) || (UP == str) || (DOWN == str) || (SHRINK_RIGHT == str) || (SHRINK_LEFT == str) || (SHRINK_UP == str) || (SHRINK_DOWN == str))
/*     */         {
/* 386 */           JInternalFrame localJInternalFrame = localJDesktopPane.getSelectedFrame();
/* 387 */           if ((sourceFrame == null) || (localJInternalFrame != sourceFrame) || (KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner() != sourceFrame))
/*     */           {
/* 391 */             return;
/*     */           }
/* 393 */           localObject1 = UIManager.getInsets("Desktop.minOnScreenInsets");
/*     */ 
/* 395 */           localObject2 = localJInternalFrame.getSize();
/* 396 */           localObject3 = localJInternalFrame.getMinimumSize();
/* 397 */           int j = localJDesktopPane.getWidth();
/* 398 */           int k = localJDesktopPane.getHeight();
/*     */ 
/* 400 */           Point localPoint = localJInternalFrame.getLocation();
/* 401 */           if (LEFT == str) {
/* 402 */             if (moving) {
/* 403 */               localJInternalFrame.setLocation(localPoint.x + ((Dimension)localObject2).width - 10 < ((Insets)localObject1).right ? -((Dimension)localObject2).width + ((Insets)localObject1).right : localPoint.x - 10, localPoint.y);
/*     */             }
/* 409 */             else if (resizing) {
/* 410 */               localJInternalFrame.setLocation(localPoint.x - 10, localPoint.y);
/* 411 */               localJInternalFrame.setSize(((Dimension)localObject2).width + 10, ((Dimension)localObject2).height);
/*     */             }
/*     */           }
/* 414 */           else if (RIGHT == str) {
/* 415 */             if (moving) {
/* 416 */               localJInternalFrame.setLocation(localPoint.x + 10 > j - ((Insets)localObject1).left ? j - ((Insets)localObject1).left : localPoint.x + 10, localPoint.y);
/*     */             }
/* 422 */             else if (resizing) {
/* 423 */               localJInternalFrame.setSize(((Dimension)localObject2).width + 10, ((Dimension)localObject2).height);
/*     */             }
/*     */           }
/* 426 */           else if (UP == str) {
/* 427 */             if (moving) {
/* 428 */               localJInternalFrame.setLocation(localPoint.x, localPoint.y + ((Dimension)localObject2).height - 10 < ((Insets)localObject1).bottom ? -((Dimension)localObject2).height + ((Insets)localObject1).bottom : localPoint.y - 10);
/*     */             }
/* 434 */             else if (resizing) {
/* 435 */               localJInternalFrame.setLocation(localPoint.x, localPoint.y - 10);
/* 436 */               localJInternalFrame.setSize(((Dimension)localObject2).width, ((Dimension)localObject2).height + 10);
/*     */             }
/*     */           }
/* 439 */           else if (DOWN == str) {
/* 440 */             if (moving) {
/* 441 */               localJInternalFrame.setLocation(localPoint.x, localPoint.y + 10 > k - ((Insets)localObject1).top ? k - ((Insets)localObject1).top : localPoint.y + 10);
/*     */             }
/* 446 */             else if (resizing)
/* 447 */               localJInternalFrame.setSize(((Dimension)localObject2).width, ((Dimension)localObject2).height + 10);
/*     */           }
/*     */           else
/*     */           {
/*     */             int m;
/* 450 */             if ((SHRINK_LEFT == str) && (resizing))
/*     */             {
/* 452 */               if (((Dimension)localObject3).width < ((Dimension)localObject2).width - 10)
/* 453 */                 m = 10;
/*     */               else {
/* 455 */                 m = ((Dimension)localObject2).width - ((Dimension)localObject3).width;
/*     */               }
/*     */ 
/* 459 */               if (localPoint.x + ((Dimension)localObject2).width - m < ((Insets)localObject1).left) {
/* 460 */                 m = localPoint.x + ((Dimension)localObject2).width - ((Insets)localObject1).left;
/*     */               }
/* 462 */               localJInternalFrame.setSize(((Dimension)localObject2).width - m, ((Dimension)localObject2).height);
/* 463 */             } else if ((SHRINK_RIGHT == str) && (resizing))
/*     */             {
/* 465 */               if (((Dimension)localObject3).width < ((Dimension)localObject2).width - 10)
/* 466 */                 m = 10;
/*     */               else {
/* 468 */                 m = ((Dimension)localObject2).width - ((Dimension)localObject3).width;
/*     */               }
/*     */ 
/* 472 */               if (localPoint.x + m > j - ((Insets)localObject1).right) {
/* 473 */                 m = j - ((Insets)localObject1).right - localPoint.x;
/*     */               }
/*     */ 
/* 476 */               localJInternalFrame.setLocation(localPoint.x + m, localPoint.y);
/* 477 */               localJInternalFrame.setSize(((Dimension)localObject2).width - m, ((Dimension)localObject2).height);
/* 478 */             } else if ((SHRINK_UP == str) && (resizing))
/*     */             {
/* 480 */               if (((Dimension)localObject3).height < ((Dimension)localObject2).height - 10)
/*     */               {
/* 482 */                 m = 10;
/*     */               }
/* 484 */               else m = ((Dimension)localObject2).height - ((Dimension)localObject3).height;
/*     */ 
/* 488 */               if (localPoint.y + ((Dimension)localObject2).height - m < ((Insets)localObject1).bottom)
/*     */               {
/* 490 */                 m = localPoint.y + ((Dimension)localObject2).height - ((Insets)localObject1).bottom;
/*     */               }
/*     */ 
/* 493 */               localJInternalFrame.setSize(((Dimension)localObject2).width, ((Dimension)localObject2).height - m);
/* 494 */             } else if ((SHRINK_DOWN == str) && (resizing))
/*     */             {
/* 496 */               if (((Dimension)localObject3).height < ((Dimension)localObject2).height - 10)
/*     */               {
/* 498 */                 m = 10;
/*     */               }
/* 500 */               else m = ((Dimension)localObject2).height - ((Dimension)localObject3).height;
/*     */ 
/* 504 */               if (localPoint.y + m > k - ((Insets)localObject1).top) {
/* 505 */                 m = k - ((Insets)localObject1).top - localPoint.y;
/*     */               }
/*     */ 
/* 508 */               localJInternalFrame.setLocation(localPoint.x, localPoint.y + m);
/* 509 */               localJInternalFrame.setSize(((Dimension)localObject2).width, ((Dimension)localObject2).height - m);
/*     */             }
/*     */           }
/* 512 */         } else if ((NEXT_FRAME == str) || (PREVIOUS_FRAME == str)) {
/* 513 */           localJDesktopPane.selectFrame(str == NEXT_FRAME);
/*     */         }
/* 515 */         else if ((NAVIGATE_NEXT == str) || (NAVIGATE_PREVIOUS == str))
/*     */         {
/* 517 */           int i = 1;
/* 518 */           if (NAVIGATE_PREVIOUS == str) {
/* 519 */             i = 0;
/*     */           }
/* 521 */           localObject1 = localJDesktopPane.getFocusCycleRootAncestor();
/*     */ 
/* 523 */           if (localObject1 != null) {
/* 524 */             localObject2 = ((Container)localObject1).getFocusTraversalPolicy();
/*     */ 
/* 526 */             if ((localObject2 != null) && ((localObject2 instanceof SortingFocusTraversalPolicy)))
/*     */             {
/* 528 */               localObject3 = (SortingFocusTraversalPolicy)localObject2;
/*     */ 
/* 530 */               boolean bool = ((SortingFocusTraversalPolicy)localObject3).getImplicitDownCycleTraversal();
/*     */               try {
/* 532 */                 ((SortingFocusTraversalPolicy)localObject3).setImplicitDownCycleTraversal(false);
/* 533 */                 if (i != 0) {
/* 534 */                   KeyboardFocusManager.getCurrentKeyboardFocusManager().focusNextComponent(localJDesktopPane);
/*     */                 }
/*     */                 else
/*     */                 {
/* 538 */                   KeyboardFocusManager.getCurrentKeyboardFocusManager().focusPreviousComponent(localJDesktopPane);
/*     */                 }
/*     */               }
/*     */               finally
/*     */               {
/* 543 */                 ((SortingFocusTraversalPolicy)localObject3).setImplicitDownCycleTraversal(bool);
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/*     */     private void setState(JDesktopPane paramJDesktopPane, String paramString)
/*     */     {
/*     */       JInternalFrame localJInternalFrame;
/* 551 */       if (paramString == CLOSE) {
/* 552 */         localJInternalFrame = paramJDesktopPane.getSelectedFrame();
/* 553 */         if (localJInternalFrame == null) {
/* 554 */           return;
/*     */         }
/* 556 */         localJInternalFrame.doDefaultCloseAction();
/* 557 */       } else if (paramString == MAXIMIZE)
/*     */       {
/* 559 */         localJInternalFrame = paramJDesktopPane.getSelectedFrame();
/* 560 */         if (localJInternalFrame == null) {
/* 561 */           return;
/*     */         }
/* 563 */         if (!localJInternalFrame.isMaximum())
/* 564 */           if (localJInternalFrame.isIcon())
/*     */             try {
/* 566 */               localJInternalFrame.setIcon(false);
/* 567 */               localJInternalFrame.setMaximum(true);
/*     */             } catch (PropertyVetoException localPropertyVetoException1) {
/*     */             }
/*     */           else try {
/* 571 */               localJInternalFrame.setMaximum(true);
/*     */             }
/*     */             catch (PropertyVetoException localPropertyVetoException2)
/*     */             {
/*     */             } 
/*     */       }
/* 576 */       else if (paramString == MINIMIZE)
/*     */       {
/* 578 */         localJInternalFrame = paramJDesktopPane.getSelectedFrame();
/* 579 */         if (localJInternalFrame == null) {
/* 580 */           return;
/*     */         }
/* 582 */         if (!localJInternalFrame.isIcon())
/*     */           try {
/* 584 */             localJInternalFrame.setIcon(true);
/*     */           } catch (PropertyVetoException localPropertyVetoException3) {
/*     */           }
/*     */       }
/* 588 */       else if (paramString == RESTORE)
/*     */       {
/* 590 */         localJInternalFrame = paramJDesktopPane.getSelectedFrame();
/* 591 */         if (localJInternalFrame == null)
/* 592 */           return;
/*     */         try
/*     */         {
/* 595 */           if (localJInternalFrame.isIcon())
/* 596 */             localJInternalFrame.setIcon(false);
/* 597 */           else if (localJInternalFrame.isMaximum()) {
/* 598 */             localJInternalFrame.setMaximum(false);
/*     */           }
/* 600 */           localJInternalFrame.setSelected(true);
/*     */         } catch (PropertyVetoException localPropertyVetoException4) {
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/*     */     public boolean isEnabled(Object paramObject) {
/* 607 */       if ((paramObject instanceof JDesktopPane)) {
/* 608 */         JDesktopPane localJDesktopPane = (JDesktopPane)paramObject;
/* 609 */         String str = getName();
/* 610 */         if ((str == NEXT_FRAME) || (str == PREVIOUS_FRAME))
/*     */         {
/* 612 */           return true;
/*     */         }
/* 614 */         JInternalFrame localJInternalFrame = localJDesktopPane.getSelectedFrame();
/* 615 */         if (localJInternalFrame == null)
/* 616 */           return false;
/* 617 */         if (str == CLOSE)
/* 618 */           return localJInternalFrame.isClosable();
/* 619 */         if (str == MINIMIZE)
/* 620 */           return localJInternalFrame.isIconifiable();
/* 621 */         if (str == MAXIMIZE) {
/* 622 */           return localJInternalFrame.isMaximizable();
/*     */         }
/* 624 */         return true;
/*     */       }
/* 626 */       return false;
/*     */     }
/*     */   }
/*     */ 
/*     */   private class BasicDesktopManager extends DefaultDesktopManager
/*     */     implements UIResource
/*     */   {
/*     */     private BasicDesktopManager()
/*     */     {
/*     */     }
/*     */   }
/*     */ 
/*     */   protected class CloseAction extends AbstractAction
/*     */   {
/*     */     protected CloseAction()
/*     */     {
/*     */     }
/*     */ 
/*     */     public void actionPerformed(ActionEvent paramActionEvent)
/*     */     {
/* 651 */       JDesktopPane localJDesktopPane = (JDesktopPane)paramActionEvent.getSource();
/* 652 */       BasicDesktopPaneUI.SHARED_ACTION.setState(localJDesktopPane, BasicDesktopPaneUI.Actions.CLOSE);
/*     */     }
/*     */ 
/*     */     public boolean isEnabled() {
/* 656 */       JInternalFrame localJInternalFrame = BasicDesktopPaneUI.this.desktop.getSelectedFrame();
/* 657 */       if (localJInternalFrame != null) {
/* 658 */         return localJInternalFrame.isClosable();
/*     */       }
/* 660 */       return false;
/*     */     }
/*     */   }
/*     */ 
/*     */   private class Handler
/*     */     implements PropertyChangeListener
/*     */   {
/*     */     private Handler()
/*     */     {
/*     */     }
/*     */ 
/*     */     public void propertyChange(PropertyChangeEvent paramPropertyChangeEvent)
/*     */     {
/* 297 */       String str = paramPropertyChangeEvent.getPropertyName();
/* 298 */       if ("desktopManager" == str)
/* 299 */         BasicDesktopPaneUI.this.installDesktopManager();
/*     */     }
/*     */   }
/*     */ 
/*     */   protected class MaximizeAction extends AbstractAction
/*     */   {
/*     */     protected MaximizeAction()
/*     */     {
/*     */     }
/*     */ 
/*     */     public void actionPerformed(ActionEvent paramActionEvent)
/*     */     {
/* 687 */       JDesktopPane localJDesktopPane = (JDesktopPane)paramActionEvent.getSource();
/* 688 */       BasicDesktopPaneUI.SHARED_ACTION.setState(localJDesktopPane, BasicDesktopPaneUI.Actions.MAXIMIZE);
/*     */     }
/*     */ 
/*     */     public boolean isEnabled() {
/* 692 */       JInternalFrame localJInternalFrame = BasicDesktopPaneUI.this.desktop.getSelectedFrame();
/* 693 */       if (localJInternalFrame != null) {
/* 694 */         return localJInternalFrame.isMaximizable();
/*     */       }
/* 696 */       return false;
/*     */     }
/*     */   }
/*     */ 
/*     */   protected class MinimizeAction extends AbstractAction
/*     */   {
/*     */     protected MinimizeAction()
/*     */     {
/*     */     }
/*     */ 
/*     */     public void actionPerformed(ActionEvent paramActionEvent)
/*     */     {
/* 669 */       JDesktopPane localJDesktopPane = (JDesktopPane)paramActionEvent.getSource();
/* 670 */       BasicDesktopPaneUI.SHARED_ACTION.setState(localJDesktopPane, BasicDesktopPaneUI.Actions.MINIMIZE);
/*     */     }
/*     */ 
/*     */     public boolean isEnabled() {
/* 674 */       JInternalFrame localJInternalFrame = BasicDesktopPaneUI.this.desktop.getSelectedFrame();
/* 675 */       if (localJInternalFrame != null) {
/* 676 */         return localJInternalFrame.isIconifiable();
/*     */       }
/* 678 */       return false;
/*     */     }
/*     */   }
/*     */ 
/*     */   protected class NavigateAction extends AbstractAction
/*     */   {
/*     */     protected NavigateAction()
/*     */     {
/*     */     }
/*     */ 
/*     */     public void actionPerformed(ActionEvent paramActionEvent)
/*     */     {
/* 705 */       JDesktopPane localJDesktopPane = (JDesktopPane)paramActionEvent.getSource();
/* 706 */       localJDesktopPane.selectFrame(true);
/*     */     }
/*     */ 
/*     */     public boolean isEnabled() {
/* 710 */       return true;
/*     */     }
/*     */   }
/*     */ 
/*     */   protected class OpenAction extends AbstractAction
/*     */   {
/*     */     protected OpenAction()
/*     */     {
/*     */     }
/*     */ 
/*     */     public void actionPerformed(ActionEvent paramActionEvent)
/*     */     {
/* 637 */       JDesktopPane localJDesktopPane = (JDesktopPane)paramActionEvent.getSource();
/* 638 */       BasicDesktopPaneUI.SHARED_ACTION.setState(localJDesktopPane, BasicDesktopPaneUI.Actions.RESTORE);
/*     */     }
/*     */ 
/*     */     public boolean isEnabled() {
/* 642 */       return true;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.basic.BasicDesktopPaneUI
 * JD-Core Version:    0.6.2
 */