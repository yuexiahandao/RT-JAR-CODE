/*      */ package java.awt;
/*      */ 
/*      */ import java.awt.event.AWTEventListener;
/*      */ import java.awt.event.MouseEvent;
/*      */ import java.awt.event.MouseWheelEvent;
/*      */ import java.io.Serializable;
/*      */ import java.security.AccessController;
/*      */ import java.security.PrivilegedAction;
/*      */ import sun.awt.AppContext;
/*      */ import sun.awt.SunToolkit;
/*      */ import sun.awt.dnd.SunDropTargetEvent;
/*      */ import sun.util.logging.PlatformLogger;
/*      */ 
/*      */ class LightweightDispatcher
/*      */   implements Serializable, AWTEventListener
/*      */ {
/*      */   private static final long serialVersionUID = 5184291520170872969L;
/*      */   private static final int LWD_MOUSE_DRAGGED_OVER = 1500;
/* 4374 */   private static final PlatformLogger eventLog = PlatformLogger.getLogger("java.awt.event.LightweightDispatcher");
/*      */   private Container nativeContainer;
/*      */   private Component focus;
/*      */   private transient Component mouseEventTarget;
/*      */   private transient Component targetLastEntered;
/* 4873 */   private transient boolean isMouseInNativeContainer = false;
/*      */   private Cursor nativeCursor;
/*      */   private long eventMask;
/*      */   private static final long PROXY_EVENT_MASK = 131132L;
/*      */   private static final long MOUSE_MASK = 131120L;
/*      */ 
/*      */   LightweightDispatcher(Container paramContainer)
/*      */   {
/* 4377 */     this.nativeContainer = paramContainer;
/* 4378 */     this.mouseEventTarget = null;
/* 4379 */     this.eventMask = 0L;
/*      */   }
/*      */ 
/*      */   void dispose()
/*      */   {
/* 4388 */     stopListeningForOtherDrags();
/* 4389 */     this.mouseEventTarget = null;
/*      */   }
/*      */ 
/*      */   void enableEvents(long paramLong)
/*      */   {
/* 4396 */     this.eventMask |= paramLong;
/*      */   }
/*      */ 
/*      */   boolean dispatchEvent(AWTEvent paramAWTEvent)
/*      */   {
/* 4407 */     boolean bool = false;
/*      */     Object localObject;
/* 4414 */     if ((paramAWTEvent instanceof SunDropTargetEvent))
/*      */     {
/* 4416 */       localObject = (SunDropTargetEvent)paramAWTEvent;
/* 4417 */       bool = processDropTargetEvent((SunDropTargetEvent)localObject);
/*      */     }
/*      */     else {
/* 4420 */       if (((paramAWTEvent instanceof MouseEvent)) && ((this.eventMask & 0x20030) != 0L)) {
/* 4421 */         localObject = (MouseEvent)paramAWTEvent;
/* 4422 */         bool = processMouseEvent((MouseEvent)localObject);
/*      */       }
/*      */ 
/* 4425 */       if (paramAWTEvent.getID() == 503) {
/* 4426 */         this.nativeContainer.updateCursorImmediately();
/*      */       }
/*      */     }
/*      */ 
/* 4430 */     return bool;
/*      */   }
/*      */ 
/*      */   private boolean isMouseGrab(MouseEvent paramMouseEvent)
/*      */   {
/* 4438 */     int i = paramMouseEvent.getModifiersEx();
/*      */ 
/* 4440 */     if ((paramMouseEvent.getID() == 501) || (paramMouseEvent.getID() == 502))
/*      */     {
/* 4443 */       switch (paramMouseEvent.getButton()) {
/*      */       case 1:
/* 4445 */         i ^= 1024;
/* 4446 */         break;
/*      */       case 2:
/* 4448 */         i ^= 2048;
/* 4449 */         break;
/*      */       case 3:
/* 4451 */         i ^= 4096;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 4456 */     return (i & 0x1C00) != 0;
/*      */   }
/*      */ 
/*      */   private boolean processMouseEvent(MouseEvent paramMouseEvent)
/*      */   {
/* 4469 */     int i = paramMouseEvent.getID();
/* 4470 */     Component localComponent = this.nativeContainer.getMouseEventTarget(paramMouseEvent.getX(), paramMouseEvent.getY(), true);
/*      */ 
/* 4474 */     trackMouseEnterExit(localComponent, paramMouseEvent);
/*      */ 
/* 4479 */     if ((!isMouseGrab(paramMouseEvent)) && (i != 500)) {
/* 4480 */       this.mouseEventTarget = (localComponent != this.nativeContainer ? localComponent : null);
/*      */     }
/*      */ 
/* 4483 */     if (this.mouseEventTarget != null) {
/* 4484 */       switch (i) {
/*      */       case 504:
/*      */       case 505:
/* 4487 */         break;
/*      */       case 501:
/* 4489 */         retargetMouseEvent(this.mouseEventTarget, i, paramMouseEvent);
/* 4490 */         break;
/*      */       case 502:
/* 4492 */         retargetMouseEvent(this.mouseEventTarget, i, paramMouseEvent);
/* 4493 */         break;
/*      */       case 500:
/* 4500 */         if (localComponent == this.mouseEventTarget)
/* 4501 */           retargetMouseEvent(localComponent, i, paramMouseEvent); break;
/*      */       case 503:
/* 4505 */         retargetMouseEvent(this.mouseEventTarget, i, paramMouseEvent);
/* 4506 */         break;
/*      */       case 506:
/* 4508 */         if (isMouseGrab(paramMouseEvent))
/* 4509 */           retargetMouseEvent(this.mouseEventTarget, i, paramMouseEvent); break;
/*      */       case 507:
/* 4516 */         if ((eventLog.isLoggable(300)) && (localComponent != null)) {
/* 4517 */           eventLog.finest("retargeting mouse wheel to " + localComponent.getName() + ", " + localComponent.getClass());
/*      */         }
/*      */ 
/* 4521 */         retargetMouseEvent(localComponent, i, paramMouseEvent);
/*      */       }
/*      */ 
/* 4525 */       if (i != 507) {
/* 4526 */         paramMouseEvent.consume();
/*      */       }
/*      */     }
/* 4529 */     return paramMouseEvent.isConsumed();
/*      */   }
/*      */ 
/*      */   private boolean processDropTargetEvent(SunDropTargetEvent paramSunDropTargetEvent) {
/* 4533 */     int i = paramSunDropTargetEvent.getID();
/* 4534 */     int j = paramSunDropTargetEvent.getX();
/* 4535 */     int k = paramSunDropTargetEvent.getY();
/*      */ 
/* 4542 */     if (!this.nativeContainer.contains(j, k)) {
/* 4543 */       localObject = this.nativeContainer.getSize();
/* 4544 */       if (((Dimension)localObject).width <= j)
/* 4545 */         j = ((Dimension)localObject).width - 1;
/* 4546 */       else if (j < 0) {
/* 4547 */         j = 0;
/*      */       }
/* 4549 */       if (((Dimension)localObject).height <= k)
/* 4550 */         k = ((Dimension)localObject).height - 1;
/* 4551 */       else if (k < 0) {
/* 4552 */         k = 0;
/*      */       }
/*      */     }
/* 4555 */     Object localObject = this.nativeContainer.getDropTargetEventTarget(j, k, true);
/*      */ 
/* 4558 */     trackMouseEnterExit((Component)localObject, paramSunDropTargetEvent);
/*      */ 
/* 4560 */     if ((localObject != this.nativeContainer) && (localObject != null)) {
/* 4561 */       switch (i) {
/*      */       case 504:
/*      */       case 505:
/* 4564 */         break;
/*      */       default:
/* 4566 */         retargetMouseEvent((Component)localObject, i, paramSunDropTargetEvent);
/* 4567 */         paramSunDropTargetEvent.consume();
/*      */       }
/*      */     }
/*      */ 
/* 4571 */     return paramSunDropTargetEvent.isConsumed();
/*      */   }
/*      */ 
/*      */   private void trackMouseEnterExit(Component paramComponent, MouseEvent paramMouseEvent)
/*      */   {
/* 4580 */     Component localComponent = null;
/* 4581 */     int i = paramMouseEvent.getID();
/*      */ 
/* 4583 */     if (((paramMouseEvent instanceof SunDropTargetEvent)) && (i == 504) && (this.isMouseInNativeContainer == true))
/*      */     {
/* 4590 */       this.targetLastEntered = null;
/* 4591 */     } else if ((i != 505) && (i != 506) && (i != 1500) && (!this.isMouseInNativeContainer))
/*      */     {
/* 4596 */       this.isMouseInNativeContainer = true;
/* 4597 */       startListeningForOtherDrags();
/* 4598 */     } else if (i == 505) {
/* 4599 */       this.isMouseInNativeContainer = false;
/* 4600 */       stopListeningForOtherDrags();
/*      */     }
/*      */ 
/* 4603 */     if (this.isMouseInNativeContainer) {
/* 4604 */       localComponent = paramComponent;
/*      */     }
/*      */ 
/* 4607 */     if (this.targetLastEntered == localComponent) {
/* 4608 */       return;
/*      */     }
/*      */ 
/* 4611 */     if (this.targetLastEntered != null) {
/* 4612 */       retargetMouseEvent(this.targetLastEntered, 505, paramMouseEvent);
/*      */     }
/* 4614 */     if (i == 505)
/*      */     {
/* 4616 */       paramMouseEvent.consume();
/*      */     }
/*      */ 
/* 4619 */     if (localComponent != null) {
/* 4620 */       retargetMouseEvent(localComponent, 504, paramMouseEvent);
/*      */     }
/* 4622 */     if (i == 504)
/*      */     {
/* 4624 */       paramMouseEvent.consume();
/*      */     }
/*      */ 
/* 4627 */     this.targetLastEntered = localComponent;
/*      */   }
/*      */ 
/*      */   private void startListeningForOtherDrags()
/*      */   {
/* 4637 */     AccessController.doPrivileged(new PrivilegedAction()
/*      */     {
/*      */       public Object run() {
/* 4640 */         LightweightDispatcher.this.nativeContainer.getToolkit().addAWTEventListener(LightweightDispatcher.this, 48L);
/*      */ 
/* 4644 */         return null;
/*      */       }
/*      */     });
/*      */   }
/*      */ 
/*      */   private void stopListeningForOtherDrags()
/*      */   {
/* 4652 */     AccessController.doPrivileged(new PrivilegedAction()
/*      */     {
/*      */       public Object run() {
/* 4655 */         LightweightDispatcher.this.nativeContainer.getToolkit().removeAWTEventListener(LightweightDispatcher.this);
/* 4656 */         return null;
/*      */       }
/*      */     });
/*      */   }
/*      */ 
/*      */   public void eventDispatched(AWTEvent paramAWTEvent)
/*      */   {
/* 4668 */     int i = ((paramAWTEvent instanceof MouseEvent)) && (!(paramAWTEvent instanceof SunDropTargetEvent)) && (paramAWTEvent.id == 506) && (paramAWTEvent.getSource() != this.nativeContainer) ? 1 : 0;
/*      */ 
/* 4673 */     if (i == 0)
/*      */     {
/* 4675 */       return;
/*      */     }
/*      */ 
/* 4678 */     MouseEvent localMouseEvent1 = (MouseEvent)paramAWTEvent;
/*      */     MouseEvent localMouseEvent2;
/* 4681 */     synchronized (this.nativeContainer.getTreeLock()) {
/* 4682 */       Component localComponent = localMouseEvent1.getComponent();
/*      */ 
/* 4686 */       if (!localComponent.isShowing()) {
/* 4687 */         return;
/*      */       }
/*      */ 
/* 4692 */       Container localContainer = this.nativeContainer;
/* 4693 */       while ((localContainer != null) && (!(localContainer instanceof Window))) {
/* 4694 */         localContainer = localContainer.getParent_NoClientCode();
/*      */       }
/* 4696 */       if ((localContainer == null) || (((Window)localContainer).isModalBlocked())) {
/* 4697 */         return;
/*      */       }
/*      */ 
/* 4704 */       localMouseEvent2 = new MouseEvent(this.nativeContainer, 1500, localMouseEvent1.getWhen(), localMouseEvent1.getModifiersEx() | localMouseEvent1.getModifiers(), localMouseEvent1.getX(), localMouseEvent1.getY(), localMouseEvent1.getXOnScreen(), localMouseEvent1.getYOnScreen(), localMouseEvent1.getClickCount(), localMouseEvent1.isPopupTrigger(), localMouseEvent1.getButton());
/*      */ 
/* 4715 */       localMouseEvent1.copyPrivateDataInto(localMouseEvent2);
/*      */ 
/* 4717 */       final Point localPoint = localComponent.getLocationOnScreen();
/*      */ 
/* 4719 */       if (AppContext.getAppContext() != this.nativeContainer.appContext) {
/* 4720 */         localObject1 = localMouseEvent2;
/* 4721 */         Runnable local3 = new Runnable() {
/*      */           public void run() {
/* 4723 */             if (!LightweightDispatcher.this.nativeContainer.isShowing()) {
/* 4724 */               return;
/*      */             }
/*      */ 
/* 4727 */             Point localPoint = LightweightDispatcher.this.nativeContainer.getLocationOnScreen();
/* 4728 */             this.val$mouseEvent.translatePoint(localPoint.x - localPoint.x, localPoint.y - localPoint.y);
/*      */ 
/* 4730 */             Component localComponent = LightweightDispatcher.this.nativeContainer.getMouseEventTarget(this.val$mouseEvent.getX(), this.val$mouseEvent.getY(), true);
/*      */ 
/* 4734 */             LightweightDispatcher.this.trackMouseEnterExit(localComponent, this.val$mouseEvent);
/*      */           }
/*      */         };
/* 4737 */         SunToolkit.executeOnEventHandlerThread(this.nativeContainer, local3);
/* 4738 */         return;
/*      */       }
/* 4740 */       if (!this.nativeContainer.isShowing()) {
/* 4741 */         return;
/*      */       }
/*      */ 
/* 4744 */       Object localObject1 = this.nativeContainer.getLocationOnScreen();
/* 4745 */       localMouseEvent2.translatePoint(localPoint.x - ((Point)localObject1).x, localPoint.y - ((Point)localObject1).y);
/*      */     }
/*      */ 
/* 4751 */     ??? = this.nativeContainer.getMouseEventTarget(localMouseEvent2.getX(), localMouseEvent2.getY(), true);
/*      */ 
/* 4754 */     trackMouseEnterExit((Component)???, localMouseEvent2);
/*      */   }
/*      */ 
/*      */   void retargetMouseEvent(Component paramComponent, int paramInt, MouseEvent paramMouseEvent)
/*      */   {
/* 4766 */     if (paramComponent == null) {
/* 4767 */       return;
/*      */     }
/*      */ 
/* 4770 */     int i = paramMouseEvent.getX(); int j = paramMouseEvent.getY();
/*      */ 
/* 4773 */     for (Object localObject1 = paramComponent; 
/* 4774 */       (localObject1 != null) && (localObject1 != this.nativeContainer); 
/* 4775 */       localObject1 = ((Component)localObject1).getParent()) {
/* 4776 */       i -= ((Component)localObject1).x;
/* 4777 */       j -= ((Component)localObject1).y;
/*      */     }
/*      */ 
/* 4780 */     if (localObject1 != null)
/*      */     {
/*      */       Object localObject2;
/* 4781 */       if ((paramMouseEvent instanceof SunDropTargetEvent)) {
/* 4782 */         localObject2 = new SunDropTargetEvent(paramComponent, paramInt, i, j, ((SunDropTargetEvent)paramMouseEvent).getDispatcher());
/*      */       }
/* 4787 */       else if (paramInt == 507) {
/* 4788 */         localObject2 = new MouseWheelEvent(paramComponent, paramInt, paramMouseEvent.getWhen(), paramMouseEvent.getModifiersEx() | paramMouseEvent.getModifiers(), i, j, paramMouseEvent.getXOnScreen(), paramMouseEvent.getYOnScreen(), paramMouseEvent.getClickCount(), paramMouseEvent.isPopupTrigger(), ((MouseWheelEvent)paramMouseEvent).getScrollType(), ((MouseWheelEvent)paramMouseEvent).getScrollAmount(), ((MouseWheelEvent)paramMouseEvent).getWheelRotation(), ((MouseWheelEvent)paramMouseEvent).getPreciseWheelRotation());
/*      */       }
/*      */       else
/*      */       {
/* 4804 */         localObject2 = new MouseEvent(paramComponent, paramInt, paramMouseEvent.getWhen(), paramMouseEvent.getModifiersEx() | paramMouseEvent.getModifiers(), i, j, paramMouseEvent.getXOnScreen(), paramMouseEvent.getYOnScreen(), paramMouseEvent.getClickCount(), paramMouseEvent.isPopupTrigger(), paramMouseEvent.getButton());
/*      */       }
/*      */ 
/* 4817 */       paramMouseEvent.copyPrivateDataInto((AWTEvent)localObject2);
/*      */ 
/* 4819 */       if (paramComponent == this.nativeContainer)
/*      */       {
/* 4821 */         ((Container)paramComponent).dispatchEventToSelf((AWTEvent)localObject2);
/*      */       } else {
/* 4823 */         assert (AppContext.getAppContext() == paramComponent.appContext);
/*      */ 
/* 4825 */         if (this.nativeContainer.modalComp != null) {
/* 4826 */           if (((Container)this.nativeContainer.modalComp).isAncestorOf(paramComponent))
/* 4827 */             paramComponent.dispatchEvent((AWTEvent)localObject2);
/*      */           else
/* 4829 */             paramMouseEvent.consume();
/*      */         }
/*      */         else {
/* 4832 */           paramComponent.dispatchEvent((AWTEvent)localObject2);
/*      */         }
/*      */       }
/* 4835 */       if ((paramInt == 507) && (((MouseEvent)localObject2).isConsumed()))
/*      */       {
/* 4839 */         paramMouseEvent.consume();
/*      */       }
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.LightweightDispatcher
 * JD-Core Version:    0.6.2
 */