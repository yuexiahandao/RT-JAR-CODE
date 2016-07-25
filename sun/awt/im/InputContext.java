/*      */ package sun.awt.im;
/*      */ 
/*      */ import java.awt.AWTEvent;
/*      */ import java.awt.AWTKeyStroke;
/*      */ import java.awt.Component;
/*      */ import java.awt.EventQueue;
/*      */ import java.awt.Frame;
/*      */ import java.awt.Rectangle;
/*      */ import java.awt.Toolkit;
/*      */ import java.awt.Window;
/*      */ import java.awt.event.ComponentEvent;
/*      */ import java.awt.event.ComponentListener;
/*      */ import java.awt.event.FocusEvent;
/*      */ import java.awt.event.InputEvent;
/*      */ import java.awt.event.InputMethodEvent;
/*      */ import java.awt.event.KeyEvent;
/*      */ import java.awt.event.WindowEvent;
/*      */ import java.awt.event.WindowListener;
/*      */ import java.awt.im.InputMethodRequests;
/*      */ import java.awt.im.spi.InputMethod;
/*      */ import java.awt.im.spi.InputMethodDescriptor;
/*      */ import java.security.AccessController;
/*      */ import java.security.PrivilegedAction;
/*      */ import java.text.MessageFormat;
/*      */ import java.util.Collection;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.Locale;
/*      */ import java.util.prefs.BackingStoreException;
/*      */ import java.util.prefs.Preferences;
/*      */ import sun.awt.SunToolkit;
/*      */ import sun.util.logging.PlatformLogger;
/*      */ 
/*      */ public class InputContext extends java.awt.im.InputContext
/*      */   implements ComponentListener, WindowListener
/*      */ {
/*   70 */   private static final PlatformLogger log = PlatformLogger.getLogger("sun.awt.im.InputContext");
/*      */   private InputMethodLocator inputMethodLocator;
/*      */   private InputMethod inputMethod;
/*      */   private boolean inputMethodCreationFailed;
/*      */   private HashMap usedInputMethods;
/*      */   private Component currentClientComponent;
/*      */   private Component awtFocussedComponent;
/*      */   private boolean isInputMethodActive;
/*   90 */   private Character.Subset[] characterSubsets = null;
/*      */ 
/*   93 */   private boolean compositionAreaHidden = false;
/*      */   private static InputContext inputMethodWindowContext;
/*  100 */   private static InputMethod previousInputMethod = null;
/*      */ 
/*  103 */   private boolean clientWindowNotificationEnabled = false;
/*      */   private Window clientWindowListened;
/*  107 */   private Rectangle clientWindowLocation = null;
/*      */   private HashMap perInputMethodState;
/*      */   private static AWTKeyStroke inputMethodSelectionKey;
/*  113 */   private static boolean inputMethodSelectionKeyInitialized = false;
/*      */   private static final String inputMethodSelectionKeyPath = "/java/awt/im/selectionKey";
/*      */   private static final String inputMethodSelectionKeyCodeName = "keyCode";
/*      */   private static final String inputMethodSelectionKeyModifiersName = "modifiers";
/*      */ 
/*      */   protected InputContext()
/*      */   {
/*  122 */     InputMethodManager localInputMethodManager = InputMethodManager.getInstance();
/*  123 */     synchronized (InputContext.class) {
/*  124 */       if (!inputMethodSelectionKeyInitialized) {
/*  125 */         inputMethodSelectionKeyInitialized = true;
/*  126 */         if (localInputMethodManager.hasMultipleInputMethods()) {
/*  127 */           initializeInputMethodSelectionKey();
/*      */         }
/*      */       }
/*      */     }
/*  131 */     selectInputMethod(localInputMethodManager.getDefaultKeyboardLocale());
/*      */   }
/*      */ 
/*      */   public synchronized boolean selectInputMethod(Locale paramLocale)
/*      */   {
/*  139 */     if (paramLocale == null) {
/*  140 */       throw new NullPointerException();
/*      */     }
/*      */ 
/*  144 */     if (this.inputMethod != null) {
/*  145 */       if (this.inputMethod.setLocale(paramLocale))
/*  146 */         return true;
/*      */     }
/*  148 */     else if (this.inputMethodLocator != null)
/*      */     {
/*  153 */       if (this.inputMethodLocator.isLocaleAvailable(paramLocale)) {
/*  154 */         this.inputMethodLocator = this.inputMethodLocator.deriveLocator(paramLocale);
/*  155 */         return true;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  160 */     InputMethodLocator localInputMethodLocator = InputMethodManager.getInstance().findInputMethod(paramLocale);
/*  161 */     if (localInputMethodLocator != null) {
/*  162 */       changeInputMethod(localInputMethodLocator);
/*  163 */       return true;
/*      */     }
/*      */ 
/*  168 */     if ((this.inputMethod == null) && (this.inputMethodLocator != null)) {
/*  169 */       this.inputMethod = getInputMethod();
/*  170 */       if (this.inputMethod != null) {
/*  171 */         return this.inputMethod.setLocale(paramLocale);
/*      */       }
/*      */     }
/*  174 */     return false;
/*      */   }
/*      */ 
/*      */   public Locale getLocale()
/*      */   {
/*  181 */     if (this.inputMethod != null)
/*  182 */       return this.inputMethod.getLocale();
/*  183 */     if (this.inputMethodLocator != null) {
/*  184 */       return this.inputMethodLocator.getLocale();
/*      */     }
/*  186 */     return null;
/*      */   }
/*      */ 
/*      */   public void setCharacterSubsets(Character.Subset[] paramArrayOfSubset)
/*      */   {
/*  194 */     if (paramArrayOfSubset == null) {
/*  195 */       this.characterSubsets = null;
/*      */     } else {
/*  197 */       this.characterSubsets = new Character.Subset[paramArrayOfSubset.length];
/*  198 */       System.arraycopy(paramArrayOfSubset, 0, this.characterSubsets, 0, this.characterSubsets.length);
/*      */     }
/*      */ 
/*  201 */     if (this.inputMethod != null)
/*  202 */       this.inputMethod.setCharacterSubsets(paramArrayOfSubset);
/*      */   }
/*      */ 
/*      */   public synchronized void reconvert()
/*      */   {
/*  212 */     InputMethod localInputMethod = getInputMethod();
/*  213 */     if (localInputMethod == null) {
/*  214 */       throw new UnsupportedOperationException();
/*      */     }
/*  216 */     localInputMethod.reconvert();
/*      */   }
/*      */ 
/*      */   public void dispatchEvent(AWTEvent paramAWTEvent)
/*      */   {
/*  224 */     if ((paramAWTEvent instanceof InputMethodEvent)) {
/*  225 */       return;
/*      */     }
/*      */ 
/*  230 */     if ((paramAWTEvent instanceof FocusEvent)) {
/*  231 */       localObject = ((FocusEvent)paramAWTEvent).getOppositeComponent();
/*  232 */       if ((localObject != null) && ((getComponentWindow((Component)localObject) instanceof InputMethodWindow)) && (((Component)localObject).getInputContext() == this))
/*      */       {
/*  235 */         return;
/*      */       }
/*      */     }
/*      */ 
/*  239 */     Object localObject = getInputMethod();
/*  240 */     int i = paramAWTEvent.getID();
/*      */ 
/*  242 */     switch (i) {
/*      */     case 1004:
/*  244 */       focusGained((Component)paramAWTEvent.getSource());
/*  245 */       break;
/*      */     case 1005:
/*  248 */       focusLost((Component)paramAWTEvent.getSource(), ((FocusEvent)paramAWTEvent).isTemporary());
/*  249 */       break;
/*      */     case 401:
/*  252 */       if (checkInputMethodSelectionKey((KeyEvent)paramAWTEvent))
/*      */       {
/*  254 */         InputMethodManager.getInstance().notifyChangeRequestByHotKey((Component)paramAWTEvent.getSource());
/*  255 */       }break;
/*      */     }
/*      */ 
/*  261 */     if ((localObject != null) && ((paramAWTEvent instanceof InputEvent)))
/*  262 */       ((InputMethod)localObject).dispatchEvent(paramAWTEvent);
/*      */   }
/*      */ 
/*      */   private void focusGained(Component paramComponent)
/*      */   {
/*  298 */     synchronized (paramComponent.getTreeLock()) {
/*  299 */       synchronized (this) {
/*  300 */         if (!"sun.awt.im.CompositionArea".equals(paramComponent.getClass().getName()))
/*      */         {
/*  302 */           if (!(getComponentWindow(paramComponent) instanceof InputMethodWindow))
/*      */           {
/*  305 */             if (!paramComponent.isDisplayable())
/*      */             {
/*  307 */               return;
/*      */             }
/*      */ 
/*  315 */             if ((this.inputMethod != null) && 
/*  316 */               (this.currentClientComponent != null) && (this.currentClientComponent != paramComponent)) {
/*  317 */               if (!this.isInputMethodActive) {
/*  318 */                 activateInputMethod(false);
/*      */               }
/*  320 */               endComposition();
/*  321 */               deactivateInputMethod(false);
/*      */             }
/*      */ 
/*  325 */             this.currentClientComponent = paramComponent;
/*      */           }
/*      */         }
/*  328 */         this.awtFocussedComponent = paramComponent;
/*  329 */         if ((this.inputMethod instanceof InputMethodAdapter)) {
/*  330 */           ((InputMethodAdapter)this.inputMethod).setAWTFocussedComponent(paramComponent);
/*      */         }
/*      */ 
/*  336 */         if (!this.isInputMethodActive) {
/*  337 */           activateInputMethod(true);
/*      */         }
/*      */ 
/*  343 */         InputMethodContext localInputMethodContext = (InputMethodContext)this;
/*  344 */         if (!localInputMethodContext.isCompositionAreaVisible()) {
/*  345 */           InputMethodRequests localInputMethodRequests = paramComponent.getInputMethodRequests();
/*  346 */           if ((localInputMethodRequests != null) && (localInputMethodContext.useBelowTheSpotInput()))
/*  347 */             localInputMethodContext.setCompositionAreaUndecorated(true);
/*      */           else {
/*  349 */             localInputMethodContext.setCompositionAreaUndecorated(false);
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*  354 */         if (this.compositionAreaHidden == true) {
/*  355 */           ((InputMethodContext)this).setCompositionAreaVisible(true);
/*  356 */           this.compositionAreaHidden = false;
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private void activateInputMethod(boolean paramBoolean)
/*      */   {
/*  372 */     if ((inputMethodWindowContext != null) && (inputMethodWindowContext != this) && (inputMethodWindowContext.inputMethodLocator != null) && (!inputMethodWindowContext.inputMethodLocator.sameInputMethod(this.inputMethodLocator)) && (inputMethodWindowContext.inputMethod != null))
/*      */     {
/*  376 */       inputMethodWindowContext.inputMethod.hideWindows();
/*      */     }
/*  378 */     inputMethodWindowContext = this;
/*      */ 
/*  380 */     if (this.inputMethod != null) {
/*  381 */       if ((previousInputMethod != this.inputMethod) && ((previousInputMethod instanceof InputMethodAdapter)))
/*      */       {
/*  385 */         ((InputMethodAdapter)previousInputMethod).stopListening();
/*      */       }
/*  387 */       previousInputMethod = null;
/*      */ 
/*  389 */       if (log.isLoggable(500)) log.fine("Current client component " + this.currentClientComponent);
/*  390 */       if ((this.inputMethod instanceof InputMethodAdapter)) {
/*  391 */         ((InputMethodAdapter)this.inputMethod).setClientComponent(this.currentClientComponent);
/*      */       }
/*  393 */       this.inputMethod.activate();
/*  394 */       this.isInputMethodActive = true;
/*      */ 
/*  396 */       if (this.perInputMethodState != null) {
/*  397 */         Boolean localBoolean = (Boolean)this.perInputMethodState.remove(this.inputMethod);
/*  398 */         if (localBoolean != null) {
/*  399 */           this.clientWindowNotificationEnabled = localBoolean.booleanValue();
/*      */         }
/*      */       }
/*  402 */       if (this.clientWindowNotificationEnabled) {
/*  403 */         if (!addedClientWindowListeners()) {
/*  404 */           addClientWindowListeners();
/*      */         }
/*  406 */         synchronized (this) {
/*  407 */           if (this.clientWindowListened != null) {
/*  408 */             notifyClientWindowChange(this.clientWindowListened);
/*      */           }
/*      */         }
/*      */       }
/*  412 */       else if (addedClientWindowListeners()) {
/*  413 */         removeClientWindowListeners();
/*      */       }
/*      */     }
/*      */ 
/*  417 */     InputMethodManager.getInstance().setInputContext(this);
/*      */ 
/*  419 */     ((InputMethodContext)this).grabCompositionArea(paramBoolean);
/*      */   }
/*      */ 
/*      */   static Window getComponentWindow(Component paramComponent) {
/*      */     while (true) {
/*  424 */       if (paramComponent == null)
/*  425 */         return null;
/*  426 */       if ((paramComponent instanceof Window)) {
/*  427 */         return (Window)paramComponent;
/*      */       }
/*  429 */       paramComponent = paramComponent.getParent();
/*      */     }
/*      */   }
/*      */ 
/*      */   private void focusLost(Component paramComponent, boolean paramBoolean)
/*      */   {
/*  449 */     synchronized (paramComponent.getTreeLock()) {
/*  450 */       synchronized (this)
/*      */       {
/*  454 */         if (this.isInputMethodActive) {
/*  455 */           deactivateInputMethod(paramBoolean);
/*      */         }
/*      */ 
/*  458 */         this.awtFocussedComponent = null;
/*  459 */         if ((this.inputMethod instanceof InputMethodAdapter)) {
/*  460 */           ((InputMethodAdapter)this.inputMethod).setAWTFocussedComponent(null);
/*      */         }
/*      */ 
/*  464 */         InputMethodContext localInputMethodContext = (InputMethodContext)this;
/*  465 */         if (localInputMethodContext.isCompositionAreaVisible()) {
/*  466 */           localInputMethodContext.setCompositionAreaVisible(false);
/*  467 */           this.compositionAreaHidden = true;
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private boolean checkInputMethodSelectionKey(KeyEvent paramKeyEvent)
/*      */   {
/*  477 */     if (inputMethodSelectionKey != null) {
/*  478 */       AWTKeyStroke localAWTKeyStroke = AWTKeyStroke.getAWTKeyStrokeForEvent(paramKeyEvent);
/*  479 */       return inputMethodSelectionKey.equals(localAWTKeyStroke);
/*      */     }
/*  481 */     return false;
/*      */   }
/*      */ 
/*      */   private void deactivateInputMethod(boolean paramBoolean)
/*      */   {
/*  486 */     InputMethodManager.getInstance().setInputContext(null);
/*  487 */     if (this.inputMethod != null) {
/*  488 */       this.isInputMethodActive = false;
/*  489 */       this.inputMethod.deactivate(paramBoolean);
/*  490 */       previousInputMethod = this.inputMethod;
/*      */     }
/*      */   }
/*      */ 
/*      */   synchronized void changeInputMethod(InputMethodLocator paramInputMethodLocator)
/*      */   {
/*  508 */     if (this.inputMethodLocator == null) {
/*  509 */       this.inputMethodLocator = paramInputMethodLocator;
/*  510 */       this.inputMethodCreationFailed = false;
/*  511 */       return;
/*      */     }
/*      */ 
/*  516 */     if (this.inputMethodLocator.sameInputMethod(paramInputMethodLocator)) {
/*  517 */       localLocale = paramInputMethodLocator.getLocale();
/*  518 */       if ((localLocale != null) && (this.inputMethodLocator.getLocale() != localLocale)) {
/*  519 */         if (this.inputMethod != null) {
/*  520 */           this.inputMethod.setLocale(localLocale);
/*      */         }
/*  522 */         this.inputMethodLocator = paramInputMethodLocator;
/*      */       }
/*  524 */       return;
/*      */     }
/*      */ 
/*  528 */     Locale localLocale = this.inputMethodLocator.getLocale();
/*  529 */     boolean bool1 = this.isInputMethodActive;
/*  530 */     int i = 0;
/*  531 */     boolean bool2 = false;
/*  532 */     if (this.inputMethod != null) {
/*      */       try {
/*  534 */         bool2 = this.inputMethod.isCompositionEnabled();
/*  535 */         i = 1;
/*      */       } catch (UnsupportedOperationException localUnsupportedOperationException1) {
/*      */       }
/*  538 */       if (this.currentClientComponent != null) {
/*  539 */         if (!this.isInputMethodActive) {
/*  540 */           activateInputMethod(false);
/*      */         }
/*  542 */         endComposition();
/*  543 */         deactivateInputMethod(false);
/*  544 */         if ((this.inputMethod instanceof InputMethodAdapter)) {
/*  545 */           ((InputMethodAdapter)this.inputMethod).setClientComponent(null);
/*      */         }
/*      */       }
/*  548 */       localLocale = this.inputMethod.getLocale();
/*      */ 
/*  551 */       if (this.usedInputMethods == null) {
/*  552 */         this.usedInputMethods = new HashMap(5);
/*      */       }
/*  554 */       if (this.perInputMethodState == null) {
/*  555 */         this.perInputMethodState = new HashMap(5);
/*      */       }
/*  557 */       this.usedInputMethods.put(this.inputMethodLocator.deriveLocator(null), this.inputMethod);
/*  558 */       this.perInputMethodState.put(this.inputMethod, Boolean.valueOf(this.clientWindowNotificationEnabled));
/*      */ 
/*  560 */       enableClientWindowNotification(this.inputMethod, false);
/*  561 */       if (this == inputMethodWindowContext) {
/*  562 */         this.inputMethod.hideWindows();
/*  563 */         inputMethodWindowContext = null;
/*      */       }
/*  565 */       this.inputMethodLocator = null;
/*  566 */       this.inputMethod = null;
/*  567 */       this.inputMethodCreationFailed = false;
/*      */     }
/*      */ 
/*  571 */     if ((paramInputMethodLocator.getLocale() == null) && (localLocale != null) && (paramInputMethodLocator.isLocaleAvailable(localLocale)))
/*      */     {
/*  573 */       paramInputMethodLocator = paramInputMethodLocator.deriveLocator(localLocale);
/*      */     }
/*  575 */     this.inputMethodLocator = paramInputMethodLocator;
/*  576 */     this.inputMethodCreationFailed = false;
/*      */ 
/*  579 */     if (bool1) {
/*  580 */       this.inputMethod = getInputMethodInstance();
/*  581 */       if ((this.inputMethod instanceof InputMethodAdapter)) {
/*  582 */         ((InputMethodAdapter)this.inputMethod).setAWTFocussedComponent(this.awtFocussedComponent);
/*      */       }
/*  584 */       activateInputMethod(true);
/*      */     }
/*      */ 
/*  588 */     if (i != 0) {
/*  589 */       this.inputMethod = getInputMethod();
/*  590 */       if (this.inputMethod != null)
/*      */         try {
/*  592 */           this.inputMethod.setCompositionEnabled(bool2);
/*      */         }
/*      */         catch (UnsupportedOperationException localUnsupportedOperationException2)
/*      */         {
/*      */         }
/*      */     }
/*      */   }
/*      */ 
/*      */   Component getClientComponent()
/*      */   {
/*  602 */     return this.currentClientComponent;
/*      */   }
/*      */ 
/*      */   public synchronized void removeNotify(Component paramComponent)
/*      */   {
/*  610 */     if (paramComponent == null) {
/*  611 */       throw new NullPointerException();
/*      */     }
/*      */ 
/*  614 */     if (this.inputMethod == null) {
/*  615 */       if (paramComponent == this.currentClientComponent) {
/*  616 */         this.currentClientComponent = null;
/*      */       }
/*  618 */       return;
/*      */     }
/*      */ 
/*  623 */     if (paramComponent == this.awtFocussedComponent) {
/*  624 */       focusLost(paramComponent, false);
/*      */     }
/*      */ 
/*  627 */     if (paramComponent == this.currentClientComponent) {
/*  628 */       if (this.isInputMethodActive)
/*      */       {
/*  630 */         deactivateInputMethod(false);
/*      */       }
/*  632 */       this.inputMethod.removeNotify();
/*  633 */       if ((this.clientWindowNotificationEnabled) && (addedClientWindowListeners())) {
/*  634 */         removeClientWindowListeners();
/*      */       }
/*  636 */       this.currentClientComponent = null;
/*  637 */       if ((this.inputMethod instanceof InputMethodAdapter)) {
/*  638 */         ((InputMethodAdapter)this.inputMethod).setClientComponent(null);
/*      */       }
/*      */ 
/*  645 */       if (EventQueue.isDispatchThread())
/*  646 */         ((InputMethodContext)this).releaseCompositionArea();
/*      */       else
/*  648 */         EventQueue.invokeLater(new Runnable() {
/*      */           public void run() {
/*  650 */             ((InputMethodContext)InputContext.this).releaseCompositionArea();
/*      */           }
/*      */         });
/*      */     }
/*      */   }
/*      */ 
/*      */   public synchronized void dispose()
/*      */   {
/*  662 */     if (this.currentClientComponent != null) {
/*  663 */       throw new IllegalStateException("Can't dispose InputContext while it's active");
/*      */     }
/*  665 */     if (this.inputMethod != null) {
/*  666 */       if (this == inputMethodWindowContext) {
/*  667 */         this.inputMethod.hideWindows();
/*  668 */         inputMethodWindowContext = null;
/*      */       }
/*  670 */       if (this.inputMethod == previousInputMethod) {
/*  671 */         previousInputMethod = null;
/*      */       }
/*  673 */       if (this.clientWindowNotificationEnabled) {
/*  674 */         if (addedClientWindowListeners()) {
/*  675 */           removeClientWindowListeners();
/*      */         }
/*  677 */         this.clientWindowNotificationEnabled = false;
/*      */       }
/*  679 */       this.inputMethod.dispose();
/*      */ 
/*  684 */       if (this.clientWindowNotificationEnabled) {
/*  685 */         enableClientWindowNotification(this.inputMethod, false);
/*      */       }
/*      */ 
/*  688 */       this.inputMethod = null;
/*      */     }
/*  690 */     this.inputMethodLocator = null;
/*  691 */     if ((this.usedInputMethods != null) && (!this.usedInputMethods.isEmpty())) {
/*  692 */       Iterator localIterator = this.usedInputMethods.values().iterator();
/*  693 */       this.usedInputMethods = null;
/*  694 */       while (localIterator.hasNext()) {
/*  695 */         ((InputMethod)localIterator.next()).dispose();
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  700 */     this.clientWindowNotificationEnabled = false;
/*  701 */     this.clientWindowListened = null;
/*  702 */     this.perInputMethodState = null;
/*      */   }
/*      */ 
/*      */   public synchronized Object getInputMethodControlObject()
/*      */   {
/*  709 */     InputMethod localInputMethod = getInputMethod();
/*      */ 
/*  711 */     if (localInputMethod != null) {
/*  712 */       return localInputMethod.getControlObject();
/*      */     }
/*  714 */     return null;
/*      */   }
/*      */ 
/*      */   public void setCompositionEnabled(boolean paramBoolean)
/*      */   {
/*  723 */     InputMethod localInputMethod = getInputMethod();
/*      */ 
/*  725 */     if (localInputMethod == null) {
/*  726 */       throw new UnsupportedOperationException();
/*      */     }
/*  728 */     localInputMethod.setCompositionEnabled(paramBoolean);
/*      */   }
/*      */ 
/*      */   public boolean isCompositionEnabled()
/*      */   {
/*  736 */     InputMethod localInputMethod = getInputMethod();
/*      */ 
/*  738 */     if (localInputMethod == null) {
/*  739 */       throw new UnsupportedOperationException();
/*      */     }
/*  741 */     return localInputMethod.isCompositionEnabled();
/*      */   }
/*      */ 
/*      */   public String getInputMethodInfo()
/*      */   {
/*  749 */     InputMethod localInputMethod = getInputMethod();
/*      */ 
/*  751 */     if (localInputMethod == null) {
/*  752 */       throw new UnsupportedOperationException("Null input method");
/*      */     }
/*      */ 
/*  755 */     String str = null;
/*  756 */     if ((localInputMethod instanceof InputMethodAdapter))
/*      */     {
/*  758 */       str = ((InputMethodAdapter)localInputMethod).getNativeInputMethodInfo();
/*      */     }
/*      */ 
/*  764 */     if ((str == null) && (this.inputMethodLocator != null)) {
/*  765 */       str = this.inputMethodLocator.getDescriptor().getInputMethodDisplayName(getLocale(), SunToolkit.getStartupLocale());
/*      */     }
/*      */ 
/*  770 */     if ((str != null) && (!str.equals(""))) {
/*  771 */       return str;
/*      */     }
/*      */ 
/*  775 */     return localInputMethod.toString() + "-" + localInputMethod.getLocale().toString();
/*      */   }
/*      */ 
/*      */   public void disableNativeIM()
/*      */   {
/*  786 */     InputMethod localInputMethod = getInputMethod();
/*  787 */     if ((localInputMethod != null) && ((localInputMethod instanceof InputMethodAdapter)))
/*  788 */       ((InputMethodAdapter)localInputMethod).stopListening();
/*      */   }
/*      */ 
/*      */   private synchronized InputMethod getInputMethod()
/*      */   {
/*  794 */     if (this.inputMethod != null) {
/*  795 */       return this.inputMethod;
/*      */     }
/*      */ 
/*  798 */     if (this.inputMethodCreationFailed) {
/*  799 */       return null;
/*      */     }
/*      */ 
/*  802 */     this.inputMethod = getInputMethodInstance();
/*  803 */     return this.inputMethod;
/*      */   }
/*      */ 
/*      */   private InputMethod getInputMethodInstance()
/*      */   {
/*  822 */     InputMethodLocator localInputMethodLocator = this.inputMethodLocator;
/*  823 */     if (localInputMethodLocator == null) {
/*  824 */       this.inputMethodCreationFailed = true;
/*  825 */       return null;
/*      */     }
/*      */ 
/*  828 */     Locale localLocale = localInputMethodLocator.getLocale();
/*  829 */     InputMethod localInputMethod = null;
/*      */ 
/*  832 */     if (this.usedInputMethods != null) {
/*  833 */       localInputMethod = (InputMethod)this.usedInputMethods.remove(localInputMethodLocator.deriveLocator(null));
/*  834 */       if (localInputMethod != null) {
/*  835 */         if (localLocale != null) {
/*  836 */           localInputMethod.setLocale(localLocale);
/*      */         }
/*  838 */         localInputMethod.setCharacterSubsets(this.characterSubsets);
/*  839 */         Boolean localBoolean = (Boolean)this.perInputMethodState.remove(localInputMethod);
/*  840 */         if (localBoolean != null) {
/*  841 */           enableClientWindowNotification(localInputMethod, localBoolean.booleanValue());
/*      */         }
/*  843 */         ((InputMethodContext)this).setInputMethodSupportsBelowTheSpot((!(localInputMethod instanceof InputMethodAdapter)) || (((InputMethodAdapter)localInputMethod).supportsBelowTheSpot()));
/*      */ 
/*  846 */         return localInputMethod;
/*      */       }
/*      */     }
/*      */ 
/*      */     try
/*      */     {
/*  852 */       localInputMethod = localInputMethodLocator.getDescriptor().createInputMethod();
/*      */ 
/*  854 */       if (localLocale != null) {
/*  855 */         localInputMethod.setLocale(localLocale);
/*      */       }
/*  857 */       localInputMethod.setInputMethodContext((InputMethodContext)this);
/*  858 */       localInputMethod.setCharacterSubsets(this.characterSubsets);
/*      */     }
/*      */     catch (Exception localException) {
/*  861 */       logCreationFailed(localException);
/*      */ 
/*  866 */       this.inputMethodCreationFailed = true;
/*      */ 
/*  870 */       if (localInputMethod != null)
/*  871 */         localInputMethod = null;
/*      */     }
/*      */     catch (LinkageError localLinkageError) {
/*  874 */       logCreationFailed(localLinkageError);
/*      */ 
/*  877 */       this.inputMethodCreationFailed = true;
/*      */     }
/*  879 */     ((InputMethodContext)this).setInputMethodSupportsBelowTheSpot((!(localInputMethod instanceof InputMethodAdapter)) || (((InputMethodAdapter)localInputMethod).supportsBelowTheSpot()));
/*      */ 
/*  882 */     return localInputMethod;
/*      */   }
/*      */ 
/*      */   private void logCreationFailed(Throwable paramThrowable) {
/*  886 */     String str = Toolkit.getProperty("AWT.InputMethodCreationFailed", "Could not create {0}. Reason: {1}");
/*      */ 
/*  888 */     Object[] arrayOfObject = { this.inputMethodLocator.getDescriptor().getInputMethodDisplayName(null, Locale.getDefault()), paramThrowable.getLocalizedMessage() };
/*      */ 
/*  891 */     MessageFormat localMessageFormat = new MessageFormat(str);
/*  892 */     PlatformLogger localPlatformLogger = PlatformLogger.getLogger("sun.awt.im");
/*  893 */     localPlatformLogger.config(localMessageFormat.format(arrayOfObject));
/*      */   }
/*      */ 
/*      */   InputMethodLocator getInputMethodLocator() {
/*  897 */     if (this.inputMethod != null) {
/*  898 */       return this.inputMethodLocator.deriveLocator(this.inputMethod.getLocale());
/*      */     }
/*  900 */     return this.inputMethodLocator;
/*      */   }
/*      */ 
/*      */   public synchronized void endComposition()
/*      */   {
/*  907 */     if (this.inputMethod != null)
/*  908 */       this.inputMethod.endComposition();
/*      */   }
/*      */ 
/*      */   synchronized void enableClientWindowNotification(InputMethod paramInputMethod, boolean paramBoolean)
/*      */   {
/*  920 */     if (paramInputMethod != this.inputMethod) {
/*  921 */       if (this.perInputMethodState == null) {
/*  922 */         this.perInputMethodState = new HashMap(5);
/*      */       }
/*  924 */       this.perInputMethodState.put(paramInputMethod, Boolean.valueOf(paramBoolean));
/*  925 */       return;
/*      */     }
/*      */ 
/*  928 */     if (this.clientWindowNotificationEnabled != paramBoolean) {
/*  929 */       this.clientWindowLocation = null;
/*  930 */       this.clientWindowNotificationEnabled = paramBoolean;
/*      */     }
/*  932 */     if (this.clientWindowNotificationEnabled) {
/*  933 */       if (!addedClientWindowListeners()) {
/*  934 */         addClientWindowListeners();
/*      */       }
/*  936 */       if (this.clientWindowListened != null) {
/*  937 */         this.clientWindowLocation = null;
/*  938 */         notifyClientWindowChange(this.clientWindowListened);
/*      */       }
/*      */     }
/*  941 */     else if (addedClientWindowListeners()) {
/*  942 */       removeClientWindowListeners();
/*      */     }
/*      */   }
/*      */ 
/*      */   private synchronized void notifyClientWindowChange(Window paramWindow)
/*      */   {
/*  948 */     if (this.inputMethod == null) {
/*  949 */       return;
/*      */     }
/*      */ 
/*  953 */     if ((!paramWindow.isVisible()) || (((paramWindow instanceof Frame)) && (((Frame)paramWindow).getState() == 1)))
/*      */     {
/*  955 */       this.clientWindowLocation = null;
/*  956 */       this.inputMethod.notifyClientWindowChange(null);
/*  957 */       return;
/*      */     }
/*  959 */     Rectangle localRectangle = paramWindow.getBounds();
/*  960 */     if ((this.clientWindowLocation == null) || (!this.clientWindowLocation.equals(localRectangle))) {
/*  961 */       this.clientWindowLocation = localRectangle;
/*  962 */       this.inputMethod.notifyClientWindowChange(this.clientWindowLocation);
/*      */     }
/*      */   }
/*      */ 
/*      */   private synchronized void addClientWindowListeners() {
/*  967 */     Component localComponent = getClientComponent();
/*  968 */     if (localComponent == null) {
/*  969 */       return;
/*      */     }
/*  971 */     Window localWindow = getComponentWindow(localComponent);
/*  972 */     if (localWindow == null) {
/*  973 */       return;
/*      */     }
/*  975 */     localWindow.addComponentListener(this);
/*  976 */     localWindow.addWindowListener(this);
/*  977 */     this.clientWindowListened = localWindow;
/*      */   }
/*      */ 
/*      */   private synchronized void removeClientWindowListeners() {
/*  981 */     this.clientWindowListened.removeComponentListener(this);
/*  982 */     this.clientWindowListened.removeWindowListener(this);
/*  983 */     this.clientWindowListened = null;
/*      */   }
/*      */ 
/*      */   private boolean addedClientWindowListeners()
/*      */   {
/*  991 */     return this.clientWindowListened != null;
/*      */   }
/*      */ 
/*      */   public void componentResized(ComponentEvent paramComponentEvent)
/*      */   {
/*  998 */     notifyClientWindowChange((Window)paramComponentEvent.getComponent());
/*      */   }
/*      */ 
/*      */   public void componentMoved(ComponentEvent paramComponentEvent) {
/* 1002 */     notifyClientWindowChange((Window)paramComponentEvent.getComponent());
/*      */   }
/*      */ 
/*      */   public void componentShown(ComponentEvent paramComponentEvent) {
/* 1006 */     notifyClientWindowChange((Window)paramComponentEvent.getComponent());
/*      */   }
/*      */ 
/*      */   public void componentHidden(ComponentEvent paramComponentEvent) {
/* 1010 */     notifyClientWindowChange((Window)paramComponentEvent.getComponent());
/*      */   }
/*      */   public void windowOpened(WindowEvent paramWindowEvent) {
/*      */   }
/*      */   public void windowClosing(WindowEvent paramWindowEvent) {
/*      */   }
/*      */   public void windowClosed(WindowEvent paramWindowEvent) {
/*      */   }
/* 1018 */   public void windowIconified(WindowEvent paramWindowEvent) { notifyClientWindowChange(paramWindowEvent.getWindow()); }
/*      */ 
/*      */   public void windowDeiconified(WindowEvent paramWindowEvent)
/*      */   {
/* 1022 */     notifyClientWindowChange(paramWindowEvent.getWindow());
/*      */   }
/*      */ 
/*      */   public void windowActivated(WindowEvent paramWindowEvent) {
/*      */   }
/*      */ 
/*      */   public void windowDeactivated(WindowEvent paramWindowEvent) {
/*      */   }
/*      */ 
/*      */   private void initializeInputMethodSelectionKey() {
/* 1032 */     AccessController.doPrivileged(new PrivilegedAction()
/*      */     {
/*      */       public Object run() {
/* 1035 */         Preferences localPreferences = Preferences.userRoot();
/* 1036 */         InputContext.access$002(InputContext.this.getInputMethodSelectionKeyStroke(localPreferences));
/*      */ 
/* 1038 */         if (InputContext.inputMethodSelectionKey == null)
/*      */         {
/* 1040 */           localPreferences = Preferences.systemRoot();
/* 1041 */           InputContext.access$002(InputContext.this.getInputMethodSelectionKeyStroke(localPreferences));
/*      */         }
/* 1043 */         return null;
/*      */       }
/*      */     });
/*      */   }
/*      */ 
/*      */   private AWTKeyStroke getInputMethodSelectionKeyStroke(Preferences paramPreferences) {
/*      */     try {
/* 1050 */       if (paramPreferences.nodeExists("/java/awt/im/selectionKey")) {
/* 1051 */         Preferences localPreferences = paramPreferences.node("/java/awt/im/selectionKey");
/* 1052 */         int i = localPreferences.getInt("keyCode", 0);
/* 1053 */         if (i != 0) {
/* 1054 */           int j = localPreferences.getInt("modifiers", 0);
/* 1055 */           return AWTKeyStroke.getAWTKeyStroke(i, j);
/*      */         }
/*      */       }
/*      */     }
/*      */     catch (BackingStoreException localBackingStoreException) {
/*      */     }
/* 1061 */     return null;
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.im.InputContext
 * JD-Core Version:    0.6.2
 */