/*      */ package java.awt;
/*      */ 
/*      */ import java.awt.event.FocusEvent;
/*      */ import java.awt.event.KeyEvent;
/*      */ import java.awt.event.WindowEvent;
/*      */ import java.awt.peer.ComponentPeer;
/*      */ import java.awt.peer.LightweightPeer;
/*      */ import java.lang.ref.WeakReference;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedList;
/*      */ import java.util.List;
/*      */ import java.util.ListIterator;
/*      */ import java.util.Set;
/*      */ import sun.awt.AWTAccessor;
/*      */ import sun.awt.AWTAccessor.DefaultKeyboardFocusManagerAccessor;
/*      */ import sun.awt.AppContext;
/*      */ import sun.awt.CausedFocusEvent;
/*      */ import sun.awt.CausedFocusEvent.Cause;
/*      */ import sun.awt.SunToolkit;
/*      */ import sun.util.logging.PlatformLogger;
/*      */ 
/*      */ public class DefaultKeyboardFocusManager extends KeyboardFocusManager
/*      */ {
/*   65 */   private static final PlatformLogger focusLog = PlatformLogger.getLogger("java.awt.focus.DefaultKeyboardFocusManager");
/*      */ 
/*   68 */   private static final WeakReference<Window> NULL_WINDOW_WR = new WeakReference(null);
/*      */ 
/*   70 */   private static final WeakReference<Component> NULL_COMPONENT_WR = new WeakReference(null);
/*      */   private WeakReference<Window> realOppositeWindowWR;
/*      */   private WeakReference<Component> realOppositeComponentWR;
/*      */   private int inSendMessage;
/*      */   private LinkedList enqueuedKeyEvents;
/*      */   private LinkedList typeAheadMarkers;
/*      */   private boolean consumeNextKeyTyped;
/*      */ 
/*      */   public DefaultKeyboardFocusManager()
/*      */   {
/*   72 */     this.realOppositeWindowWR = NULL_WINDOW_WR;
/*   73 */     this.realOppositeComponentWR = NULL_COMPONENT_WR;
/*      */ 
/*   75 */     this.enqueuedKeyEvents = new LinkedList(); this.typeAheadMarkers = new LinkedList();
/*      */   }
/*      */ 
/*      */   private Window getOwningFrameDialog(Window paramWindow)
/*      */   {
/*  105 */     while ((paramWindow != null) && (!(paramWindow instanceof Frame)) && (!(paramWindow instanceof Dialog)))
/*      */     {
/*  107 */       paramWindow = (Window)paramWindow.getParent();
/*      */     }
/*  109 */     return paramWindow;
/*      */   }
/*      */ 
/*      */   private void restoreFocus(FocusEvent paramFocusEvent, Window paramWindow)
/*      */   {
/*  118 */     Component localComponent1 = (Component)this.realOppositeComponentWR.get();
/*  119 */     Component localComponent2 = paramFocusEvent.getComponent();
/*      */ 
/*  121 */     if ((paramWindow == null) || (!restoreFocus(paramWindow, localComponent2, false)))
/*      */     {
/*  124 */       if ((localComponent1 == null) || (!doRestoreFocus(localComponent1, localComponent2, false)))
/*      */       {
/*  126 */         if ((paramFocusEvent.getOppositeComponent() == null) || (!doRestoreFocus(paramFocusEvent.getOppositeComponent(), localComponent2, false)))
/*      */         {
/*  129 */           clearGlobalFocusOwner(); }  } 
/*      */     }
/*      */   }
/*      */ 
/*  133 */   private void restoreFocus(WindowEvent paramWindowEvent) { Window localWindow = (Window)this.realOppositeWindowWR.get();
/*  134 */     if ((localWindow == null) || (!restoreFocus(localWindow, null, false)))
/*      */     {
/*  138 */       if ((paramWindowEvent.getOppositeWindow() == null) || (!restoreFocus(paramWindowEvent.getOppositeWindow(), null, false)))
/*      */       {
/*  143 */         clearGlobalFocusOwner();
/*      */       }
/*      */     } }
/*      */ 
/*      */   private boolean restoreFocus(Window paramWindow, Component paramComponent, boolean paramBoolean) {
/*  148 */     Component localComponent = KeyboardFocusManager.getMostRecentFocusOwner(paramWindow);
/*      */ 
/*  151 */     if ((localComponent != null) && (localComponent != paramComponent) && (doRestoreFocus(localComponent, paramComponent, false)))
/*  152 */       return true;
/*  153 */     if (paramBoolean) {
/*  154 */       clearGlobalFocusOwner();
/*  155 */       return true;
/*      */     }
/*  157 */     return false;
/*      */   }
/*      */ 
/*      */   private boolean restoreFocus(Component paramComponent, boolean paramBoolean) {
/*  161 */     return doRestoreFocus(paramComponent, null, paramBoolean);
/*      */   }
/*      */ 
/*      */   private boolean doRestoreFocus(Component paramComponent1, Component paramComponent2, boolean paramBoolean)
/*      */   {
/*  166 */     if ((paramComponent1 != paramComponent2) && (paramComponent1.isShowing()) && (paramComponent1.canBeFocusOwner()) && (paramComponent1.requestFocus(false, CausedFocusEvent.Cause.ROLLBACK)))
/*      */     {
/*  169 */       return true;
/*      */     }
/*  171 */     Component localComponent = paramComponent1.getNextFocusCandidate();
/*  172 */     if ((localComponent != null) && (localComponent != paramComponent2) && (localComponent.requestFocusInWindow(CausedFocusEvent.Cause.ROLLBACK)))
/*      */     {
/*  175 */       return true;
/*  176 */     }if (paramBoolean) {
/*  177 */       clearGlobalFocusOwner();
/*  178 */       return true;
/*      */     }
/*  180 */     return false;
/*      */   }
/*      */ 
/*      */   static boolean sendMessage(Component paramComponent, AWTEvent paramAWTEvent)
/*      */   {
/*  236 */     paramAWTEvent.isPosted = true;
/*  237 */     AppContext localAppContext1 = AppContext.getAppContext();
/*  238 */     final AppContext localAppContext2 = paramComponent.appContext;
/*  239 */     DefaultKeyboardFocusManagerSentEvent localDefaultKeyboardFocusManagerSentEvent = new DefaultKeyboardFocusManagerSentEvent(paramAWTEvent, localAppContext1);
/*      */ 
/*  242 */     if (localAppContext1 == localAppContext2) {
/*  243 */       localDefaultKeyboardFocusManagerSentEvent.dispatch();
/*      */     } else {
/*  245 */       if (localAppContext2.isDisposed()) {
/*  246 */         return false;
/*      */       }
/*  248 */       SunToolkit.postEvent(localAppContext2, localDefaultKeyboardFocusManagerSentEvent);
/*  249 */       if (EventQueue.isDispatchThread()) {
/*  250 */         EventDispatchThread localEventDispatchThread = (EventDispatchThread)Thread.currentThread();
/*      */ 
/*  252 */         localEventDispatchThread.pumpEvents(1007, new Conditional() {
/*      */           public boolean evaluate() {
/*  254 */             return (!this.val$se.dispatched) && (!localAppContext2.isDisposed());
/*      */           } } );
/*      */       }
/*      */       else {
/*  258 */         synchronized (localDefaultKeyboardFocusManagerSentEvent) {
/*      */           while (true) if ((!localDefaultKeyboardFocusManagerSentEvent.dispatched) && (!localAppContext2.isDisposed()))
/*      */               try {
/*  261 */                 localDefaultKeyboardFocusManagerSentEvent.wait(1000L);
/*      */               }
/*      */               catch (InterruptedException localInterruptedException)
/*      */               {
/*      */               }
/*      */         }
/*      */       }
/*      */     }
/*  269 */     return localDefaultKeyboardFocusManagerSentEvent.dispatched;
/*      */   }
/*      */ 
/*      */   public boolean dispatchEvent(AWTEvent paramAWTEvent)
/*      */   {
/*  287 */     if ((focusLog.isLoggable(500)) && (((paramAWTEvent instanceof WindowEvent)) || ((paramAWTEvent instanceof FocusEvent)))) focusLog.fine("" + paramAWTEvent);
/*      */     Object localObject1;
/*      */     Object localObject2;
/*      */     Object localObject3;
/*      */     Object localObject5;
/*      */     Object localObject6;
/*      */     Object localObject4;
/*      */     Window localWindow3;
/*  288 */     switch (paramAWTEvent.getID()) {
/*      */     case 207:
/*  290 */       localObject1 = (WindowEvent)paramAWTEvent;
/*  291 */       localObject2 = getGlobalFocusedWindow();
/*  292 */       localObject3 = ((WindowEvent)localObject1).getWindow();
/*  293 */       if (localObject3 != localObject2)
/*      */       {
/*  297 */         if ((!((Window)localObject3).isFocusableWindow()) || (!((Window)localObject3).isVisible()) || (!((Window)localObject3).isDisplayable()))
/*      */         {
/*  302 */           restoreFocus((WindowEvent)localObject1);
/*      */         }
/*      */         else
/*      */         {
/*  307 */           if (localObject2 != null) {
/*  308 */             boolean bool1 = sendMessage((Component)localObject2, new WindowEvent((Window)localObject2, 208, (Window)localObject3));
/*      */ 
/*  314 */             if (!bool1) {
/*  315 */               setGlobalFocusOwner(null);
/*  316 */               setGlobalFocusedWindow(null);
/*      */             }
/*      */ 
/*      */           }
/*      */ 
/*  323 */           Window localWindow1 = getOwningFrameDialog((Window)localObject3);
/*      */ 
/*  325 */           Window localWindow2 = getGlobalActiveWindow();
/*  326 */           if (localWindow1 != localWindow2) {
/*  327 */             sendMessage(localWindow1, new WindowEvent(localWindow1, 205, localWindow2));
/*      */ 
/*  331 */             if (localWindow1 != getGlobalActiveWindow())
/*      */             {
/*  334 */               restoreFocus((WindowEvent)localObject1);
/*  335 */               break;
/*      */             }
/*      */           }
/*      */ 
/*  339 */           setGlobalFocusedWindow((Window)localObject3);
/*      */ 
/*  341 */           if (localObject3 != getGlobalFocusedWindow())
/*      */           {
/*  344 */             restoreFocus((WindowEvent)localObject1);
/*      */           }
/*      */           else
/*      */           {
/*  355 */             if (this.inSendMessage == 0)
/*      */             {
/*  375 */               localObject5 = KeyboardFocusManager.getMostRecentFocusOwner((Window)localObject3);
/*      */ 
/*  377 */               if ((localObject5 == null) && (((Window)localObject3).isFocusableWindow()))
/*      */               {
/*  380 */                 localObject5 = ((Window)localObject3).getFocusTraversalPolicy().getInitialComponent((Window)localObject3);
/*      */               }
/*      */ 
/*  383 */               localObject6 = null;
/*  384 */               synchronized (KeyboardFocusManager.class) {
/*  385 */                 localObject6 = ((Window)localObject3).setTemporaryLostComponent(null);
/*      */               }
/*      */ 
/*  390 */               if (focusLog.isLoggable(400)) {
/*  391 */                 focusLog.finer("tempLost {0}, toFocus {1}", new Object[] { localObject6, localObject5 });
/*      */               }
/*      */ 
/*  394 */               if (localObject6 != null) {
/*  395 */                 ((Component)localObject6).requestFocusInWindow(CausedFocusEvent.Cause.ACTIVATION);
/*      */               }
/*      */ 
/*  398 */               if ((localObject5 != null) && (localObject5 != localObject6))
/*      */               {
/*  401 */                 ((Component)localObject5).requestFocusInWindow(CausedFocusEvent.Cause.ACTIVATION);
/*      */               }
/*      */             }
/*      */ 
/*  405 */             localObject5 = (Window)this.realOppositeWindowWR.get();
/*  406 */             if (localObject5 != ((WindowEvent)localObject1).getOppositeWindow()) {
/*  407 */               localObject1 = new WindowEvent((Window)localObject3, 207, (Window)localObject5);
/*      */             }
/*      */ 
/*  411 */             return typeAheadAssertions((Component)localObject3, (AWTEvent)localObject1);
/*      */           }
/*      */         }
/*      */       }
/*      */       break;
/*      */     case 205:
/*  415 */       localObject1 = (WindowEvent)paramAWTEvent;
/*  416 */       localObject2 = getGlobalActiveWindow();
/*  417 */       localObject3 = ((WindowEvent)localObject1).getWindow();
/*  418 */       if (localObject2 != localObject3)
/*      */       {
/*  424 */         if (localObject2 != null) {
/*  425 */           boolean bool2 = sendMessage((Component)localObject2, new WindowEvent((Window)localObject2, 206, (Window)localObject3));
/*      */ 
/*  431 */           if (!bool2) {
/*  432 */             setGlobalActiveWindow(null);
/*      */           }
/*  434 */           if (getGlobalActiveWindow() != null)
/*      */           {
/*      */             break;
/*      */           }
/*      */         }
/*      */         else
/*      */         {
/*  441 */           setGlobalActiveWindow((Window)localObject3);
/*      */ 
/*  443 */           if (localObject3 == getGlobalActiveWindow())
/*      */           {
/*  449 */             return typeAheadAssertions((Component)localObject3, (AWTEvent)localObject1);
/*      */           }
/*      */         }
/*      */       }
/*      */       break;
/*      */     case 1004:
/*  453 */       localObject1 = (FocusEvent)paramAWTEvent;
/*  454 */       localObject2 = (localObject1 instanceof CausedFocusEvent) ? ((CausedFocusEvent)localObject1).getCause() : CausedFocusEvent.Cause.UNKNOWN;
/*      */ 
/*  456 */       localObject3 = getGlobalFocusOwner();
/*  457 */       localObject4 = ((FocusEvent)localObject1).getComponent();
/*  458 */       if (localObject3 == localObject4) {
/*  459 */         if (focusLog.isLoggable(500)) {
/*  460 */           focusLog.fine("Skipping {0} because focus owner is the same", new Object[] { paramAWTEvent });
/*      */         }
/*      */ 
/*  464 */         dequeueKeyEvents(-1L, (Component)localObject4);
/*      */       }
/*      */       else
/*      */       {
/*  470 */         if (localObject3 != null) {
/*  471 */           boolean bool3 = sendMessage((Component)localObject3, new CausedFocusEvent((Component)localObject3, 1005, ((FocusEvent)localObject1).isTemporary(), (Component)localObject4, (CausedFocusEvent.Cause)localObject2));
/*      */ 
/*  478 */           if (!bool3) {
/*  479 */             setGlobalFocusOwner(null);
/*  480 */             if (!((FocusEvent)localObject1).isTemporary()) {
/*  481 */               setGlobalPermanentFocusOwner(null);
/*      */             }
/*      */ 
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*  491 */         localWindow3 = SunToolkit.getContainingWindow((Component)localObject4);
/*  492 */         localObject5 = getGlobalFocusedWindow();
/*  493 */         if ((localWindow3 != null) && (localWindow3 != localObject5))
/*      */         {
/*  496 */           sendMessage(localWindow3, new WindowEvent(localWindow3, 207, (Window)localObject5));
/*      */ 
/*  500 */           if (localWindow3 != getGlobalFocusedWindow())
/*      */           {
/*  507 */             dequeueKeyEvents(-1L, (Component)localObject4);
/*  508 */             break;
/*      */           }
/*      */         }
/*      */ 
/*  512 */         if ((!((Component)localObject4).isFocusable()) || (!((Component)localObject4).isShowing()) || ((!((Component)localObject4).isEnabled()) && (!((CausedFocusEvent.Cause)localObject2).equals(CausedFocusEvent.Cause.UNKNOWN))))
/*      */         {
/*  519 */           dequeueKeyEvents(-1L, (Component)localObject4);
/*  520 */           if (KeyboardFocusManager.isAutoFocusTransferEnabled())
/*      */           {
/*  525 */             if (localWindow3 == null)
/*  526 */               restoreFocus((FocusEvent)localObject1, (Window)localObject5);
/*      */             else {
/*  528 */               restoreFocus((FocusEvent)localObject1, localWindow3);
/*      */             }
/*      */           }
/*      */         }
/*      */         else
/*      */         {
/*  534 */           setGlobalFocusOwner((Component)localObject4);
/*      */ 
/*  536 */           if (localObject4 != getGlobalFocusOwner())
/*      */           {
/*  539 */             dequeueKeyEvents(-1L, (Component)localObject4);
/*  540 */             if (KeyboardFocusManager.isAutoFocusTransferEnabled()) {
/*  541 */               restoreFocus((FocusEvent)localObject1, localWindow3);
/*      */             }
/*      */           }
/*      */           else
/*      */           {
/*  546 */             if (!((FocusEvent)localObject1).isTemporary()) {
/*  547 */               setGlobalPermanentFocusOwner((Component)localObject4);
/*      */ 
/*  549 */               if (localObject4 != getGlobalPermanentFocusOwner())
/*      */               {
/*  551 */                 dequeueKeyEvents(-1L, (Component)localObject4);
/*  552 */                 if (!KeyboardFocusManager.isAutoFocusTransferEnabled()) break;
/*  553 */                 restoreFocus((FocusEvent)localObject1, localWindow3); break;
/*      */               }
/*      */ 
/*      */             }
/*      */ 
/*  559 */             setNativeFocusOwner(getHeavyweight((Component)localObject4));
/*      */ 
/*  561 */             localObject6 = (Component)this.realOppositeComponentWR.get();
/*  562 */             if ((localObject6 != null) && (localObject6 != ((FocusEvent)localObject1).getOppositeComponent()))
/*      */             {
/*  564 */               localObject1 = new CausedFocusEvent((Component)localObject4, 1004, ((FocusEvent)localObject1).isTemporary(), (Component)localObject6, (CausedFocusEvent.Cause)localObject2);
/*      */ 
/*  568 */               ((AWTEvent)localObject1).isPosted = true;
/*      */             }
/*  570 */             return typeAheadAssertions((Component)localObject4, (AWTEvent)localObject1);
/*      */           }
/*      */         }
/*      */       }
/*      */       break;
/*      */     case 1005:
/*  574 */       localObject1 = (FocusEvent)paramAWTEvent;
/*  575 */       localObject2 = getGlobalFocusOwner();
/*  576 */       if (localObject2 == null) {
/*  577 */         if (focusLog.isLoggable(500)) {
/*  578 */           focusLog.fine("Skipping {0} because focus owner is null", new Object[] { paramAWTEvent });
/*      */         }
/*      */ 
/*      */       }
/*  584 */       else if (localObject2 == ((FocusEvent)localObject1).getOppositeComponent()) {
/*  585 */         if (focusLog.isLoggable(500))
/*  586 */           focusLog.fine("Skipping {0} because current focus owner is equal to opposite", new Object[] { paramAWTEvent });
/*      */       }
/*      */       else
/*      */       {
/*  590 */         setGlobalFocusOwner(null);
/*      */ 
/*  592 */         if (getGlobalFocusOwner() != null)
/*      */         {
/*  594 */           restoreFocus((Component)localObject2, true);
/*      */         }
/*      */         else
/*      */         {
/*  598 */           if (!((FocusEvent)localObject1).isTemporary()) {
/*  599 */             setGlobalPermanentFocusOwner(null);
/*      */ 
/*  601 */             if (getGlobalPermanentFocusOwner() != null)
/*      */             {
/*  603 */               restoreFocus((Component)localObject2, true);
/*  604 */               break;
/*      */             }
/*      */           } else {
/*  607 */             localObject3 = ((Component)localObject2).getContainingWindow();
/*  608 */             if (localObject3 != null) {
/*  609 */               ((Window)localObject3).setTemporaryLostComponent((Component)localObject2);
/*      */             }
/*      */           }
/*      */ 
/*  613 */           setNativeFocusOwner(null);
/*      */ 
/*  615 */           ((FocusEvent)localObject1).setSource(localObject2);
/*      */ 
/*  617 */           this.realOppositeComponentWR = (((FocusEvent)localObject1).getOppositeComponent() != null ? new WeakReference(localObject2) : NULL_COMPONENT_WR);
/*      */ 
/*  621 */           return typeAheadAssertions((Component)localObject2, (AWTEvent)localObject1);
/*      */         }
/*      */       }
/*      */       break;
/*      */     case 206:
/*  625 */       localObject1 = (WindowEvent)paramAWTEvent;
/*  626 */       localObject2 = getGlobalActiveWindow();
/*  627 */       if (localObject2 != null)
/*      */       {
/*  631 */         if (localObject2 == paramAWTEvent.getSource())
/*      */         {
/*  638 */           setGlobalActiveWindow(null);
/*  639 */           if (getGlobalActiveWindow() == null)
/*      */           {
/*  644 */             ((WindowEvent)localObject1).setSource(localObject2);
/*  645 */             return typeAheadAssertions((Component)localObject2, (AWTEvent)localObject1);
/*      */           }
/*      */         }
/*      */       }
/*      */       break;
/*      */     case 208:
/*  649 */       localObject1 = (WindowEvent)paramAWTEvent;
/*  650 */       localObject2 = getGlobalFocusedWindow();
/*  651 */       localObject3 = ((WindowEvent)localObject1).getWindow();
/*  652 */       localObject4 = getGlobalActiveWindow();
/*  653 */       localWindow3 = ((WindowEvent)localObject1).getOppositeWindow();
/*  654 */       if (focusLog.isLoggable(500)) {
/*  655 */         focusLog.fine("Active {0}, Current focused {1}, losing focus {2} opposite {3}", new Object[] { localObject4, localObject2, localObject3, localWindow3 });
/*      */       }
/*      */ 
/*  658 */       if (localObject2 != null)
/*      */       {
/*  667 */         if ((this.inSendMessage != 0) || (localObject3 != localObject4) || (localWindow3 != localObject2))
/*      */         {
/*  673 */           localObject5 = getGlobalFocusOwner();
/*  674 */           if (localObject5 != null)
/*      */           {
/*  677 */             localObject6 = null;
/*  678 */             if (localWindow3 != null) {
/*  679 */               localObject6 = localWindow3.getTemporaryLostComponent();
/*  680 */               if (localObject6 == null) {
/*  681 */                 localObject6 = localWindow3.getMostRecentFocusOwner();
/*      */               }
/*      */             }
/*  684 */             if (localObject6 == null) {
/*  685 */               localObject6 = localWindow3;
/*      */             }
/*  687 */             sendMessage((Component)localObject5, new CausedFocusEvent((Component)localObject5, 1005, true, (Component)localObject6, CausedFocusEvent.Cause.ACTIVATION));
/*      */           }
/*      */ 
/*  694 */           setGlobalFocusedWindow(null);
/*  695 */           if (getGlobalFocusedWindow() != null)
/*      */           {
/*  697 */             restoreFocus((Window)localObject2, null, true);
/*      */           }
/*      */           else
/*      */           {
/*  701 */             ((WindowEvent)localObject1).setSource(localObject2);
/*  702 */             this.realOppositeWindowWR = (localWindow3 != null ? new WeakReference(localObject2) : NULL_WINDOW_WR);
/*      */ 
/*  705 */             typeAheadAssertions((Component)localObject2, (AWTEvent)localObject1);
/*      */ 
/*  707 */             if (localWindow3 == null)
/*      */             {
/*  711 */               sendMessage((Component)localObject4, new WindowEvent((Window)localObject4, 206, null));
/*      */ 
/*  715 */               if (getGlobalActiveWindow() != null)
/*      */               {
/*  718 */                 restoreFocus((Window)localObject2, null, true); }  }  }  }  } break;
/*      */     case 400:
/*      */     case 401:
/*      */     case 402:
/*  727 */       return typeAheadAssertions(null, paramAWTEvent);
/*      */     default:
/*  730 */       return false;
/*      */     }
/*      */ 
/*  733 */     return true;
/*      */   }
/*      */ 
/*      */   public boolean dispatchKeyEvent(KeyEvent paramKeyEvent)
/*      */   {
/*  756 */     Component localComponent1 = paramKeyEvent.isPosted ? getFocusOwner() : paramKeyEvent.getComponent();
/*      */ 
/*  758 */     if ((localComponent1 != null) && (localComponent1.isShowing()) && (localComponent1.canBeFocusOwner()) && 
/*  759 */       (!paramKeyEvent.isConsumed())) {
/*  760 */       Component localComponent2 = paramKeyEvent.getComponent();
/*  761 */       if ((localComponent2 != null) && (localComponent2.isEnabled())) {
/*  762 */         redispatchEvent(localComponent2, paramKeyEvent);
/*      */       }
/*      */     }
/*      */ 
/*  766 */     boolean bool = false;
/*  767 */     List localList = getKeyEventPostProcessors();
/*  768 */     if (localList != null) {
/*  769 */       localObject = localList.iterator();
/*  770 */       while ((!bool) && (((Iterator)localObject).hasNext()))
/*      */       {
/*  772 */         bool = ((KeyEventPostProcessor)((Iterator)localObject).next()).postProcessKeyEvent(paramKeyEvent);
/*      */       }
/*      */     }
/*      */ 
/*  776 */     if (!bool) {
/*  777 */       postProcessKeyEvent(paramKeyEvent);
/*      */     }
/*      */ 
/*  781 */     Object localObject = paramKeyEvent.getComponent();
/*  782 */     ComponentPeer localComponentPeer = ((Component)localObject).getPeer();
/*      */ 
/*  784 */     if ((localComponentPeer == null) || ((localComponentPeer instanceof LightweightPeer)))
/*      */     {
/*  787 */       Container localContainer = ((Component)localObject).getNativeContainer();
/*  788 */       if (localContainer != null) {
/*  789 */         localComponentPeer = localContainer.getPeer();
/*      */       }
/*      */     }
/*  792 */     if (localComponentPeer != null) {
/*  793 */       localComponentPeer.handleEvent(paramKeyEvent);
/*      */     }
/*      */ 
/*  796 */     return true;
/*      */   }
/*      */ 
/*      */   public boolean postProcessKeyEvent(KeyEvent paramKeyEvent)
/*      */   {
/*  811 */     if (!paramKeyEvent.isConsumed()) {
/*  812 */       Component localComponent = paramKeyEvent.getComponent();
/*  813 */       Container localContainer = (Container)((localComponent instanceof Container) ? localComponent : localComponent.getParent());
/*      */ 
/*  815 */       if (localContainer != null) {
/*  816 */         localContainer.postProcessKeyEvent(paramKeyEvent);
/*      */       }
/*      */     }
/*  819 */     return true;
/*      */   }
/*      */ 
/*      */   private void pumpApprovedKeyEvents() {
/*      */     KeyEvent localKeyEvent;
/*      */     do {
/*  825 */       localKeyEvent = null;
/*  826 */       synchronized (this) {
/*  827 */         if (this.enqueuedKeyEvents.size() != 0) {
/*  828 */           localKeyEvent = (KeyEvent)this.enqueuedKeyEvents.getFirst();
/*  829 */           if (this.typeAheadMarkers.size() != 0) {
/*  830 */             TypeAheadMarker localTypeAheadMarker = (TypeAheadMarker)this.typeAheadMarkers.getFirst();
/*      */ 
/*  836 */             if (localKeyEvent.getWhen() > localTypeAheadMarker.after) {
/*  837 */               localKeyEvent = null;
/*      */             }
/*      */           }
/*  840 */           if (localKeyEvent != null) {
/*  841 */             focusLog.finer("Pumping approved event {0}", new Object[] { localKeyEvent });
/*  842 */             this.enqueuedKeyEvents.removeFirst();
/*      */           }
/*      */         }
/*      */       }
/*  846 */       if (localKeyEvent != null)
/*  847 */         preDispatchKeyEvent(localKeyEvent);
/*      */     }
/*  849 */     while (localKeyEvent != null);
/*      */   }
/*      */ 
/*      */   void dumpMarkers()
/*      */   {
/*  856 */     if (focusLog.isLoggable(300)) {
/*  857 */       focusLog.finest(">>> Markers dump, time: {0}", new Object[] { Long.valueOf(System.currentTimeMillis()) });
/*  858 */       synchronized (this) {
/*  859 */         if (this.typeAheadMarkers.size() != 0) {
/*  860 */           Iterator localIterator = this.typeAheadMarkers.iterator();
/*  861 */           while (localIterator.hasNext()) {
/*  862 */             TypeAheadMarker localTypeAheadMarker = (TypeAheadMarker)localIterator.next();
/*  863 */             focusLog.finest("    {0}", new Object[] { localTypeAheadMarker });
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private boolean typeAheadAssertions(Component paramComponent, AWTEvent paramAWTEvent)
/*      */   {
/*  875 */     pumpApprovedKeyEvents();
/*      */     Object localObject1;
/*  877 */     switch (paramAWTEvent.getID()) {
/*      */     case 400:
/*      */     case 401:
/*      */     case 402:
/*  881 */       KeyEvent localKeyEvent = (KeyEvent)paramAWTEvent;
/*  882 */       synchronized (this) {
/*  883 */         if ((paramAWTEvent.isPosted) && (this.typeAheadMarkers.size() != 0)) {
/*  884 */           localObject1 = (TypeAheadMarker)this.typeAheadMarkers.getFirst();
/*      */ 
/*  890 */           if (localKeyEvent.getWhen() > ((TypeAheadMarker)localObject1).after) {
/*  891 */             focusLog.finer("Storing event {0} because of marker {1}", new Object[] { localKeyEvent, localObject1 });
/*  892 */             this.enqueuedKeyEvents.addLast(localKeyEvent);
/*  893 */             return true;
/*      */           }
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  899 */       return preDispatchKeyEvent(localKeyEvent);
/*      */     case 1004:
/*  903 */       focusLog.finest("Markers before FOCUS_GAINED on {0}", new Object[] { paramComponent });
/*  904 */       dumpMarkers();
/*      */ 
/*  915 */       synchronized (this) {
/*  916 */         int i = 0;
/*  917 */         if (hasMarker(paramComponent)) {
/*  918 */           localObject1 = this.typeAheadMarkers.iterator();
/*  919 */           while (((Iterator)localObject1).hasNext())
/*      */           {
/*  921 */             if (((TypeAheadMarker)((Iterator)localObject1).next()).untilFocused == paramComponent)
/*      */             {
/*  924 */               i = 1;
/*      */             } else if (i != 0) {
/*      */                 break;
/*      */               }
/*  928 */             ((Iterator)localObject1).remove();
/*      */           }
/*      */         }
/*      */         else {
/*  932 */           focusLog.finer("Event without marker {0}", new Object[] { paramAWTEvent });
/*      */         }
/*      */       }
/*  935 */       focusLog.finest("Markers after FOCUS_GAINED");
/*  936 */       dumpMarkers();
/*      */ 
/*  938 */       redispatchEvent(paramComponent, paramAWTEvent);
/*      */ 
/*  943 */       pumpApprovedKeyEvents();
/*  944 */       return true;
/*      */     }
/*      */ 
/*  947 */     redispatchEvent(paramComponent, paramAWTEvent);
/*  948 */     return true;
/*      */   }
/*      */ 
/*      */   private boolean hasMarker(Component paramComponent)
/*      */   {
/*  958 */     for (Iterator localIterator = this.typeAheadMarkers.iterator(); localIterator.hasNext(); ) {
/*  959 */       if (((TypeAheadMarker)localIterator.next()).untilFocused == paramComponent) {
/*  960 */         return true;
/*      */       }
/*      */     }
/*  963 */     return false;
/*      */   }
/*      */ 
/*      */   void clearMarkers()
/*      */   {
/*  971 */     synchronized (this) {
/*  972 */       this.typeAheadMarkers.clear();
/*      */     }
/*      */   }
/*      */ 
/*      */   private boolean preDispatchKeyEvent(KeyEvent paramKeyEvent) {
/*  977 */     if (paramKeyEvent.isPosted) {
/*  978 */       localObject1 = getFocusOwner();
/*  979 */       paramKeyEvent.setSource(localObject1 != null ? localObject1 : getFocusedWindow());
/*      */     }
/*  981 */     if (paramKeyEvent.getSource() == null) {
/*  982 */       return true;
/*      */     }
/*      */ 
/*  990 */     EventQueue.setCurrentEventAndMostRecentTime(paramKeyEvent);
/*      */     Object localObject2;
/*  999 */     if (KeyboardFocusManager.isProxyActive(paramKeyEvent)) {
/* 1000 */       localObject1 = (Component)paramKeyEvent.getSource();
/* 1001 */       localObject2 = ((Component)localObject1).getNativeContainer();
/* 1002 */       if (localObject2 != null) {
/* 1003 */         ComponentPeer localComponentPeer = ((Container)localObject2).getPeer();
/* 1004 */         if (localComponentPeer != null) {
/* 1005 */           localComponentPeer.handleEvent(paramKeyEvent);
/*      */ 
/* 1009 */           paramKeyEvent.consume();
/*      */         }
/*      */       }
/* 1012 */       return true;
/*      */     }
/*      */ 
/* 1015 */     Object localObject1 = getKeyEventDispatchers();
/* 1016 */     if (localObject1 != null) {
/* 1017 */       localObject2 = ((List)localObject1).iterator();
/* 1018 */       while (((Iterator)localObject2).hasNext())
/*      */       {
/* 1020 */         if (((KeyEventDispatcher)((Iterator)localObject2).next()).dispatchKeyEvent(paramKeyEvent))
/*      */         {
/* 1023 */           return true;
/*      */         }
/*      */       }
/*      */     }
/* 1027 */     return dispatchKeyEvent(paramKeyEvent);
/*      */   }
/*      */ 
/*      */   private void consumeNextKeyTyped(KeyEvent paramKeyEvent)
/*      */   {
/* 1035 */     this.consumeNextKeyTyped = true;
/*      */   }
/*      */ 
/*      */   private void consumeTraversalKey(KeyEvent paramKeyEvent) {
/* 1039 */     paramKeyEvent.consume();
/* 1040 */     this.consumeNextKeyTyped = ((paramKeyEvent.getID() == 401) && (!paramKeyEvent.isActionKey()));
/*      */   }
/*      */ 
/*      */   private boolean consumeProcessedKeyEvent(KeyEvent paramKeyEvent)
/*      */   {
/* 1048 */     if ((paramKeyEvent.getID() == 400) && (this.consumeNextKeyTyped)) {
/* 1049 */       paramKeyEvent.consume();
/* 1050 */       this.consumeNextKeyTyped = false;
/* 1051 */       return true;
/*      */     }
/* 1053 */     return false;
/*      */   }
/*      */ 
/*      */   public void processKeyEvent(Component paramComponent, KeyEvent paramKeyEvent)
/*      */   {
/* 1071 */     if (consumeProcessedKeyEvent(paramKeyEvent)) {
/* 1072 */       return;
/*      */     }
/*      */ 
/* 1076 */     if (paramKeyEvent.getID() == 400) {
/* 1077 */       return;
/*      */     }
/*      */ 
/* 1080 */     if ((paramComponent.getFocusTraversalKeysEnabled()) && (!paramKeyEvent.isConsumed()))
/*      */     {
/* 1083 */       AWTKeyStroke localAWTKeyStroke1 = AWTKeyStroke.getAWTKeyStrokeForEvent(paramKeyEvent);
/* 1084 */       AWTKeyStroke localAWTKeyStroke2 = AWTKeyStroke.getAWTKeyStroke(localAWTKeyStroke1.getKeyCode(), localAWTKeyStroke1.getModifiers(), !localAWTKeyStroke1.isOnKeyRelease());
/*      */ 
/* 1090 */       Set localSet = paramComponent.getFocusTraversalKeys(0);
/*      */ 
/* 1092 */       boolean bool1 = localSet.contains(localAWTKeyStroke1);
/* 1093 */       boolean bool2 = localSet.contains(localAWTKeyStroke2);
/*      */ 
/* 1095 */       if ((bool1) || (bool2)) {
/* 1096 */         consumeTraversalKey(paramKeyEvent);
/* 1097 */         if (bool1) {
/* 1098 */           focusNextComponent(paramComponent);
/*      */         }
/* 1100 */         return;
/* 1101 */       }if (paramKeyEvent.getID() == 401)
/*      */       {
/* 1103 */         this.consumeNextKeyTyped = false;
/*      */       }
/*      */ 
/* 1106 */       localSet = paramComponent.getFocusTraversalKeys(1);
/*      */ 
/* 1108 */       bool1 = localSet.contains(localAWTKeyStroke1);
/* 1109 */       bool2 = localSet.contains(localAWTKeyStroke2);
/*      */ 
/* 1111 */       if ((bool1) || (bool2)) {
/* 1112 */         consumeTraversalKey(paramKeyEvent);
/* 1113 */         if (bool1) {
/* 1114 */           focusPreviousComponent(paramComponent);
/*      */         }
/* 1116 */         return;
/*      */       }
/*      */ 
/* 1119 */       localSet = paramComponent.getFocusTraversalKeys(2);
/*      */ 
/* 1121 */       bool1 = localSet.contains(localAWTKeyStroke1);
/* 1122 */       bool2 = localSet.contains(localAWTKeyStroke2);
/*      */ 
/* 1124 */       if ((bool1) || (bool2)) {
/* 1125 */         consumeTraversalKey(paramKeyEvent);
/* 1126 */         if (bool1) {
/* 1127 */           upFocusCycle(paramComponent);
/*      */         }
/* 1129 */         return;
/*      */       }
/*      */ 
/* 1132 */       if ((!(paramComponent instanceof Container)) || (!((Container)paramComponent).isFocusCycleRoot()))
/*      */       {
/* 1134 */         return;
/*      */       }
/*      */ 
/* 1137 */       localSet = paramComponent.getFocusTraversalKeys(3);
/*      */ 
/* 1139 */       bool1 = localSet.contains(localAWTKeyStroke1);
/* 1140 */       bool2 = localSet.contains(localAWTKeyStroke2);
/*      */ 
/* 1142 */       if ((bool1) || (bool2)) {
/* 1143 */         consumeTraversalKey(paramKeyEvent);
/* 1144 */         if (bool1)
/* 1145 */           downFocusCycle((Container)paramComponent);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   protected synchronized void enqueueKeyEvents(long paramLong, Component paramComponent)
/*      */   {
/* 1168 */     if (paramComponent == null) {
/* 1169 */       return;
/*      */     }
/*      */ 
/* 1172 */     focusLog.finer("Enqueue at {0} for {1}", new Object[] { Long.valueOf(paramLong), paramComponent });
/*      */ 
/* 1175 */     int i = 0;
/* 1176 */     int j = this.typeAheadMarkers.size();
/* 1177 */     ListIterator localListIterator = this.typeAheadMarkers.listIterator(j);
/*      */ 
/* 1179 */     for (; j > 0; j--) {
/* 1180 */       TypeAheadMarker localTypeAheadMarker = (TypeAheadMarker)localListIterator.previous();
/* 1181 */       if (localTypeAheadMarker.after <= paramLong) {
/* 1182 */         i = j;
/* 1183 */         break;
/*      */       }
/*      */     }
/*      */ 
/* 1187 */     this.typeAheadMarkers.add(i, new TypeAheadMarker(paramLong, paramComponent));
/*      */   }
/*      */ 
/*      */   protected synchronized void dequeueKeyEvents(long paramLong, Component paramComponent)
/*      */   {
/* 1208 */     if (paramComponent == null) {
/* 1209 */       return;
/*      */     }
/*      */ 
/* 1212 */     focusLog.finer("Dequeue at {0} for {1}", new Object[] { Long.valueOf(paramLong), paramComponent });
/*      */ 
/* 1216 */     ListIterator localListIterator = this.typeAheadMarkers.listIterator(paramLong >= 0L ? this.typeAheadMarkers.size() : 0);
/*      */     TypeAheadMarker localTypeAheadMarker;
/* 1219 */     if (paramLong < 0L) {
/*      */       do { if (!localListIterator.hasNext()) break;
/* 1221 */         localTypeAheadMarker = (TypeAheadMarker)localListIterator.next(); }
/* 1222 */       while (localTypeAheadMarker.untilFocused != paramComponent);
/*      */ 
/* 1224 */       localListIterator.remove();
/* 1225 */       return;
/*      */     }
/*      */ 
/* 1229 */     while (localListIterator.hasPrevious()) {
/* 1230 */       localTypeAheadMarker = (TypeAheadMarker)localListIterator.previous();
/* 1231 */       if ((localTypeAheadMarker.untilFocused == paramComponent) && (localTypeAheadMarker.after == paramLong))
/*      */       {
/* 1234 */         localListIterator.remove();
/* 1235 */         return;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   protected synchronized void discardKeyEvents(Component paramComponent)
/*      */   {
/* 1252 */     if (paramComponent == null) {
/* 1253 */       return;
/*      */     }
/*      */ 
/* 1256 */     long l = -1L;
/*      */ 
/* 1258 */     for (Iterator localIterator = this.typeAheadMarkers.iterator(); localIterator.hasNext(); ) {
/* 1259 */       TypeAheadMarker localTypeAheadMarker = (TypeAheadMarker)localIterator.next();
/* 1260 */       Object localObject = localTypeAheadMarker.untilFocused;
/* 1261 */       int i = localObject == paramComponent ? 1 : 0;
/* 1262 */       while ((i == 0) && (localObject != null) && (!(localObject instanceof Window))) {
/* 1263 */         localObject = ((Component)localObject).getParent();
/* 1264 */         i = localObject == paramComponent ? 1 : 0;
/*      */       }
/* 1266 */       if (i != 0) {
/* 1267 */         if (l < 0L) {
/* 1268 */           l = localTypeAheadMarker.after;
/*      */         }
/* 1270 */         localIterator.remove();
/* 1271 */       } else if (l >= 0L) {
/* 1272 */         purgeStampedEvents(l, localTypeAheadMarker.after);
/* 1273 */         l = -1L;
/*      */       }
/*      */     }
/*      */ 
/* 1277 */     purgeStampedEvents(l, -1L);
/*      */   }
/*      */ 
/*      */   private void purgeStampedEvents(long paramLong1, long paramLong2)
/*      */   {
/* 1286 */     if (paramLong1 < 0L) {
/* 1287 */       return;
/*      */     }
/*      */ 
/* 1290 */     for (Iterator localIterator = this.enqueuedKeyEvents.iterator(); localIterator.hasNext(); ) {
/* 1291 */       KeyEvent localKeyEvent = (KeyEvent)localIterator.next();
/* 1292 */       long l = localKeyEvent.getWhen();
/*      */ 
/* 1294 */       if ((paramLong1 < l) && ((paramLong2 < 0L) || (l <= paramLong2))) {
/* 1295 */         localIterator.remove();
/*      */       }
/*      */ 
/* 1298 */       if ((paramLong2 >= 0L) && (l > paramLong2))
/*      */         break;
/*      */     }
/*      */   }
/*      */ 
/*      */   public void focusPreviousComponent(Component paramComponent)
/*      */   {
/* 1314 */     if (paramComponent != null)
/* 1315 */       paramComponent.transferFocusBackward();
/*      */   }
/*      */ 
/*      */   public void focusNextComponent(Component paramComponent)
/*      */   {
/* 1329 */     if (paramComponent != null)
/* 1330 */       paramComponent.transferFocus();
/*      */   }
/*      */ 
/*      */   public void upFocusCycle(Component paramComponent)
/*      */   {
/* 1347 */     if (paramComponent != null)
/* 1348 */       paramComponent.transferFocusUpCycle();
/*      */   }
/*      */ 
/*      */   public void downFocusCycle(Container paramContainer)
/*      */   {
/* 1364 */     if ((paramContainer != null) && (paramContainer.isFocusCycleRoot()))
/* 1365 */       paramContainer.transferFocusDownCycle();
/*      */   }
/*      */ 
/*      */   static
/*      */   {
/*   80 */     AWTAccessor.setDefaultKeyboardFocusManagerAccessor(new AWTAccessor.DefaultKeyboardFocusManagerAccessor()
/*      */     {
/*      */       public void consumeNextKeyTyped(DefaultKeyboardFocusManager paramAnonymousDefaultKeyboardFocusManager, KeyEvent paramAnonymousKeyEvent) {
/*   83 */         paramAnonymousDefaultKeyboardFocusManager.consumeNextKeyTyped(paramAnonymousKeyEvent);
/*      */       }
/*      */     });
/*      */   }
/*      */ 
/*      */   private static class DefaultKeyboardFocusManagerSentEvent extends SentEvent
/*      */   {
/*      */     private static final long serialVersionUID = -2924743257508701758L;
/*      */ 
/*      */     public DefaultKeyboardFocusManagerSentEvent(AWTEvent paramAWTEvent, AppContext paramAppContext)
/*      */     {
/*  200 */       super(paramAppContext);
/*      */     }
/*      */     public final void dispatch() {
/*  203 */       KeyboardFocusManager localKeyboardFocusManager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
/*      */ 
/*  205 */       DefaultKeyboardFocusManager localDefaultKeyboardFocusManager = (localKeyboardFocusManager instanceof DefaultKeyboardFocusManager) ? (DefaultKeyboardFocusManager)localKeyboardFocusManager : null;
/*      */ 
/*  210 */       if (localDefaultKeyboardFocusManager != null) {
/*  211 */         synchronized (localDefaultKeyboardFocusManager) {
/*  212 */           DefaultKeyboardFocusManager.access$108(localDefaultKeyboardFocusManager);
/*      */         }
/*      */       }
/*      */ 
/*  216 */       super.dispatch();
/*      */ 
/*  218 */       if (localDefaultKeyboardFocusManager != null)
/*  219 */         synchronized (localDefaultKeyboardFocusManager) {
/*  220 */           DefaultKeyboardFocusManager.access$110(localDefaultKeyboardFocusManager);
/*      */         }
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class TypeAheadMarker
/*      */   {
/*      */     long after;
/*      */     Component untilFocused;
/*      */ 
/*      */     TypeAheadMarker(long paramLong, Component paramComponent)
/*      */     {
/*   93 */       this.after = paramLong;
/*   94 */       this.untilFocused = paramComponent;
/*      */     }
/*      */ 
/*      */     public String toString()
/*      */     {
/*  100 */       return ">>> Marker after " + this.after + " on " + this.untilFocused;
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.DefaultKeyboardFocusManager
 * JD-Core Version:    0.6.2
 */