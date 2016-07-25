/*     */ package sun.awt.im;
/*     */ 
/*     */ import java.awt.AWTException;
/*     */ import java.awt.Component;
/*     */ import java.awt.Dialog;
/*     */ import java.awt.EventQueue;
/*     */ import java.awt.Frame;
/*     */ import java.awt.Toolkit;
/*     */ import java.awt.event.InvocationEvent;
/*     */ import java.awt.im.spi.InputMethodDescriptor;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.security.PrivilegedActionException;
/*     */ import java.security.PrivilegedExceptionAction;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Locale;
/*     */ import java.util.ServiceLoader;
/*     */ import java.util.Vector;
/*     */ import java.util.prefs.BackingStoreException;
/*     */ import java.util.prefs.Preferences;
/*     */ import sun.awt.AppContext;
/*     */ import sun.awt.InputMethodSupport;
/*     */ import sun.awt.SunToolkit;
/*     */ 
/*     */ class ExecutableInputMethodManager extends InputMethodManager
/*     */   implements Runnable
/*     */ {
/*     */   private InputContext currentInputContext;
/*     */   private String triggerMenuString;
/*     */   private InputMethodPopupMenu selectionMenu;
/*     */   private static String selectInputMethodMenuTitle;
/*     */   private InputMethodLocator hostAdapterLocator;
/*     */   private int javaInputMethodCount;
/*     */   private Vector<InputMethodLocator> javaInputMethodLocatorList;
/*     */   private Component requestComponent;
/*     */   private InputContext requestInputContext;
/*     */   private static final String preferredIMNode = "/sun/awt/im/preferredInputMethod";
/*     */   private static final String descriptorKey = "descriptor";
/* 273 */   private Hashtable preferredLocatorCache = new Hashtable();
/*     */   private Preferences userRoot;
/*     */ 
/*     */   ExecutableInputMethodManager()
/*     */   {
/* 279 */     Toolkit localToolkit = Toolkit.getDefaultToolkit();
/*     */     try {
/* 281 */       if ((localToolkit instanceof InputMethodSupport)) {
/* 282 */         InputMethodDescriptor localInputMethodDescriptor = ((InputMethodSupport)localToolkit).getInputMethodAdapterDescriptor();
/*     */ 
/* 285 */         if (localInputMethodDescriptor != null) {
/* 286 */           this.hostAdapterLocator = new InputMethodLocator(localInputMethodDescriptor, null, null);
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (AWTException localAWTException)
/*     */     {
/*     */     }
/* 293 */     this.javaInputMethodLocatorList = new Vector();
/* 294 */     initializeInputMethodLocatorList();
/*     */   }
/*     */ 
/*     */   synchronized void initialize() {
/* 298 */     selectInputMethodMenuTitle = Toolkit.getProperty("AWT.InputMethodSelectionMenu", "Select Input Method");
/*     */ 
/* 300 */     this.triggerMenuString = selectInputMethodMenuTitle;
/*     */   }
/*     */ 
/*     */   public void run()
/*     */   {
/* 305 */     while (!hasMultipleInputMethods())
/*     */       try {
/* 307 */         synchronized (this) {
/* 308 */           wait();
/*     */         }
/*     */       }
/*     */       catch (InterruptedException localInterruptedException1)
/*     */       {
/*     */       }
/*     */     while (true)
/*     */     {
/* 316 */       waitForChangeRequest();
/* 317 */       initializeInputMethodLocatorList();
/*     */       try {
/* 319 */         if (this.requestComponent != null) {
/* 320 */           showInputMethodMenuOnRequesterEDT(this.requestComponent);
/*     */         }
/*     */         else
/* 323 */           EventQueue.invokeAndWait(new Runnable() {
/*     */             public void run() {
/* 325 */               ExecutableInputMethodManager.this.showInputMethodMenu();
/*     */             }
/*     */           });
/*     */       }
/*     */       catch (InterruptedException localInterruptedException2)
/*     */       {
/*     */       }
/*     */       catch (InvocationTargetException localInvocationTargetException)
/*     */       {
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private void showInputMethodMenuOnRequesterEDT(Component paramComponent)
/*     */     throws InterruptedException, InvocationTargetException
/*     */   {
/* 341 */     if (paramComponent == null) {
/* 342 */       return;
/*     */     }
/*     */ 
/* 346 */     Object local1AWTInvocationLock = new Object()
/*     */     {
/*     */     };
/* 348 */     InvocationEvent localInvocationEvent = new InvocationEvent(paramComponent, new Runnable()
/*     */     {
/*     */       public void run()
/*     */       {
/* 352 */         ExecutableInputMethodManager.this.showInputMethodMenu();
/*     */       }
/*     */     }
/*     */     , local1AWTInvocationLock, true);
/*     */ 
/* 358 */     AppContext localAppContext = SunToolkit.targetToAppContext(paramComponent);
/* 359 */     synchronized (local1AWTInvocationLock) {
/* 360 */       SunToolkit.postEvent(localAppContext, localInvocationEvent);
/* 361 */       while (!localInvocationEvent.isDispatched()) {
/* 362 */         local1AWTInvocationLock.wait();
/*     */       }
/*     */     }
/*     */ 
/* 366 */     ??? = localInvocationEvent.getThrowable();
/* 367 */     if (??? != null)
/* 368 */       throw new InvocationTargetException((Throwable)???);
/*     */   }
/*     */ 
/*     */   void setInputContext(InputContext paramInputContext)
/*     */   {
/* 373 */     if ((this.currentInputContext != null) && (paramInputContext != null));
/* 377 */     this.currentInputContext = paramInputContext;
/*     */   }
/*     */ 
/*     */   public synchronized void notifyChangeRequest(Component paramComponent) {
/* 381 */     if ((!(paramComponent instanceof Frame)) && (!(paramComponent instanceof Dialog))) {
/* 382 */       return;
/*     */     }
/*     */ 
/* 385 */     if (this.requestComponent != null) {
/* 386 */       return;
/*     */     }
/* 388 */     this.requestComponent = paramComponent;
/* 389 */     notify();
/*     */   }
/*     */ 
/*     */   public synchronized void notifyChangeRequestByHotKey(Component paramComponent) {
/* 393 */     while ((!(paramComponent instanceof Frame)) && (!(paramComponent instanceof Dialog))) {
/* 394 */       if (paramComponent == null)
/*     */       {
/* 396 */         return;
/*     */       }
/* 398 */       paramComponent = paramComponent.getParent();
/*     */     }
/*     */ 
/* 401 */     notifyChangeRequest(paramComponent);
/*     */   }
/*     */ 
/*     */   public String getTriggerMenuString() {
/* 405 */     return this.triggerMenuString;
/*     */   }
/*     */ 
/*     */   boolean hasMultipleInputMethods()
/*     */   {
/* 412 */     return ((this.hostAdapterLocator != null) && (this.javaInputMethodCount > 0)) || (this.javaInputMethodCount > 1);
/*     */   }
/*     */ 
/*     */   private synchronized void waitForChangeRequest()
/*     */   {
/*     */     try {
/* 418 */       while (this.requestComponent == null)
/* 419 */         wait();
/*     */     }
/*     */     catch (InterruptedException localInterruptedException)
/*     */     {
/*     */     }
/*     */   }
/*     */ 
/*     */   private void initializeInputMethodLocatorList()
/*     */   {
/* 430 */     synchronized (this.javaInputMethodLocatorList) {
/* 431 */       this.javaInputMethodLocatorList.clear();
/*     */       try {
/* 433 */         AccessController.doPrivileged(new PrivilegedExceptionAction()
/*     */         {
/*     */           public Object run() {
/* 436 */             for (InputMethodDescriptor localInputMethodDescriptor : ServiceLoader.loadInstalled(InputMethodDescriptor.class)) {
/* 437 */               ClassLoader localClassLoader = localInputMethodDescriptor.getClass().getClassLoader();
/* 438 */               ExecutableInputMethodManager.this.javaInputMethodLocatorList.add(new InputMethodLocator(localInputMethodDescriptor, localClassLoader, null));
/*     */             }
/* 440 */             return null;
/*     */           } } );
/*     */       }
/*     */       catch (PrivilegedActionException localPrivilegedActionException) {
/* 444 */         localPrivilegedActionException.printStackTrace();
/*     */       }
/* 446 */       this.javaInputMethodCount = this.javaInputMethodLocatorList.size();
/*     */     }
/*     */ 
/* 449 */     if (hasMultipleInputMethods())
/*     */     {
/* 451 */       if (this.userRoot == null) {
/* 452 */         this.userRoot = getUserRoot();
/*     */       }
/*     */     }
/*     */     else
/* 456 */       this.triggerMenuString = null;
/*     */   }
/*     */ 
/*     */   private void showInputMethodMenu()
/*     */   {
/* 462 */     if (!hasMultipleInputMethods()) {
/* 463 */       this.requestComponent = null;
/* 464 */       return;
/*     */     }
/*     */ 
/* 468 */     this.selectionMenu = InputMethodPopupMenu.getInstance(this.requestComponent, selectInputMethodMenuTitle);
/*     */ 
/* 473 */     this.selectionMenu.removeAll();
/*     */ 
/* 478 */     String str = getCurrentSelection();
/*     */ 
/* 481 */     if (this.hostAdapterLocator != null) {
/* 482 */       this.selectionMenu.addOneInputMethodToMenu(this.hostAdapterLocator, str);
/* 483 */       this.selectionMenu.addSeparator();
/*     */     }
/*     */ 
/* 487 */     for (int i = 0; i < this.javaInputMethodLocatorList.size(); i++) {
/* 488 */       InputMethodLocator localInputMethodLocator = (InputMethodLocator)this.javaInputMethodLocatorList.get(i);
/* 489 */       this.selectionMenu.addOneInputMethodToMenu(localInputMethodLocator, str);
/*     */     }
/*     */ 
/* 492 */     synchronized (this) {
/* 493 */       this.selectionMenu.addToComponent(this.requestComponent);
/* 494 */       this.requestInputContext = this.currentInputContext;
/* 495 */       this.selectionMenu.show(this.requestComponent, 60, 80);
/* 496 */       this.requestComponent = null;
/*     */     }
/*     */   }
/*     */ 
/*     */   private String getCurrentSelection() {
/* 501 */     InputContext localInputContext = this.currentInputContext;
/* 502 */     if (localInputContext != null) {
/* 503 */       InputMethodLocator localInputMethodLocator = localInputContext.getInputMethodLocator();
/* 504 */       if (localInputMethodLocator != null) {
/* 505 */         return localInputMethodLocator.getActionCommandString();
/*     */       }
/*     */     }
/* 508 */     return null;
/*     */   }
/*     */ 
/*     */   synchronized void changeInputMethod(String paramString) {
/* 512 */     Object localObject1 = null;
/*     */ 
/* 514 */     String str1 = paramString;
/* 515 */     String str2 = null;
/* 516 */     int i = paramString.indexOf('\n');
/* 517 */     if (i != -1) {
/* 518 */       str2 = paramString.substring(i + 1);
/* 519 */       str1 = paramString.substring(0, i);
/*     */     }
/*     */     Object localObject2;
/*     */     String str4;
/* 521 */     if (this.hostAdapterLocator.getActionCommandString().equals(str1))
/* 522 */       localObject1 = this.hostAdapterLocator;
/*     */     else {
/* 524 */       for (int j = 0; j < this.javaInputMethodLocatorList.size(); j++) {
/* 525 */         localObject2 = (InputMethodLocator)this.javaInputMethodLocatorList.get(j);
/* 526 */         str4 = ((InputMethodLocator)localObject2).getActionCommandString();
/* 527 */         if (str4.equals(str1)) {
/* 528 */           localObject1 = localObject2;
/* 529 */           break;
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 534 */     if ((localObject1 != null) && (str2 != null)) {
/* 535 */       String str3 = ""; localObject2 = ""; str4 = "";
/* 536 */       int k = str2.indexOf('_');
/* 537 */       if (k == -1) {
/* 538 */         str3 = str2;
/*     */       } else {
/* 540 */         str3 = str2.substring(0, k);
/* 541 */         int m = k + 1;
/* 542 */         k = str2.indexOf('_', m);
/* 543 */         if (k == -1) {
/* 544 */           localObject2 = str2.substring(m);
/*     */         } else {
/* 546 */           localObject2 = str2.substring(m, k);
/* 547 */           str4 = str2.substring(k + 1);
/*     */         }
/*     */       }
/* 550 */       Locale localLocale = new Locale(str3, (String)localObject2, str4);
/* 551 */       localObject1 = ((InputMethodLocator)localObject1).deriveLocator(localLocale);
/*     */     }
/*     */ 
/* 554 */     if (localObject1 == null) {
/* 555 */       return;
/*     */     }
/*     */ 
/* 558 */     if (this.requestInputContext != null) {
/* 559 */       this.requestInputContext.changeInputMethod((InputMethodLocator)localObject1);
/* 560 */       this.requestInputContext = null;
/*     */ 
/* 563 */       putPreferredInputMethod((InputMethodLocator)localObject1);
/*     */     }
/*     */   }
/*     */ 
/*     */   InputMethodLocator findInputMethod(Locale paramLocale)
/*     */   {
/* 569 */     InputMethodLocator localInputMethodLocator1 = getPreferredInputMethod(paramLocale);
/* 570 */     if (localInputMethodLocator1 != null) {
/* 571 */       return localInputMethodLocator1;
/*     */     }
/*     */ 
/* 574 */     if ((this.hostAdapterLocator != null) && (this.hostAdapterLocator.isLocaleAvailable(paramLocale))) {
/* 575 */       return this.hostAdapterLocator.deriveLocator(paramLocale);
/*     */     }
/*     */ 
/* 579 */     initializeInputMethodLocatorList();
/*     */ 
/* 581 */     for (int i = 0; i < this.javaInputMethodLocatorList.size(); i++) {
/* 582 */       InputMethodLocator localInputMethodLocator2 = (InputMethodLocator)this.javaInputMethodLocatorList.get(i);
/* 583 */       if (localInputMethodLocator2.isLocaleAvailable(paramLocale)) {
/* 584 */         return localInputMethodLocator2.deriveLocator(paramLocale);
/*     */       }
/*     */     }
/* 587 */     return null;
/*     */   }
/*     */ 
/*     */   Locale getDefaultKeyboardLocale() {
/* 591 */     Toolkit localToolkit = Toolkit.getDefaultToolkit();
/* 592 */     if ((localToolkit instanceof InputMethodSupport)) {
/* 593 */       return ((InputMethodSupport)localToolkit).getDefaultKeyboardLocale();
/*     */     }
/* 595 */     return Locale.getDefault();
/*     */   }
/*     */ 
/*     */   private synchronized InputMethodLocator getPreferredInputMethod(Locale paramLocale)
/*     */   {
/* 606 */     InputMethodLocator localInputMethodLocator1 = null;
/*     */ 
/* 608 */     if (!hasMultipleInputMethods())
/*     */     {
/* 610 */       return null;
/*     */     }
/*     */ 
/* 614 */     localInputMethodLocator1 = (InputMethodLocator)this.preferredLocatorCache.get(paramLocale.toString().intern());
/* 615 */     if (localInputMethodLocator1 != null) {
/* 616 */       return localInputMethodLocator1;
/*     */     }
/*     */ 
/* 620 */     String str1 = findPreferredInputMethodNode(paramLocale);
/* 621 */     String str2 = readPreferredInputMethod(str1);
/*     */ 
/* 625 */     if (str2 != null)
/*     */     {
/*     */       Locale localLocale;
/* 627 */       if ((this.hostAdapterLocator != null) && (this.hostAdapterLocator.getDescriptor().getClass().getName().equals(str2)))
/*     */       {
/* 629 */         localLocale = getAdvertisedLocale(this.hostAdapterLocator, paramLocale);
/* 630 */         if (localLocale != null) {
/* 631 */           localInputMethodLocator1 = this.hostAdapterLocator.deriveLocator(localLocale);
/* 632 */           this.preferredLocatorCache.put(paramLocale.toString().intern(), localInputMethodLocator1);
/*     */         }
/* 634 */         return localInputMethodLocator1;
/*     */       }
/*     */ 
/* 637 */       for (int i = 0; i < this.javaInputMethodLocatorList.size(); i++) {
/* 638 */         InputMethodLocator localInputMethodLocator2 = (InputMethodLocator)this.javaInputMethodLocatorList.get(i);
/* 639 */         InputMethodDescriptor localInputMethodDescriptor = localInputMethodLocator2.getDescriptor();
/* 640 */         if (localInputMethodDescriptor.getClass().getName().equals(str2)) {
/* 641 */           localLocale = getAdvertisedLocale(localInputMethodLocator2, paramLocale);
/* 642 */           if (localLocale != null) {
/* 643 */             localInputMethodLocator1 = localInputMethodLocator2.deriveLocator(localLocale);
/* 644 */             this.preferredLocatorCache.put(paramLocale.toString().intern(), localInputMethodLocator1);
/*     */           }
/* 646 */           return localInputMethodLocator1;
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 651 */       writePreferredInputMethod(str1, null);
/*     */     }
/*     */ 
/* 654 */     return null;
/*     */   }
/*     */ 
/*     */   private String findPreferredInputMethodNode(Locale paramLocale) {
/* 658 */     if (this.userRoot == null) {
/* 659 */       return null;
/*     */     }
/*     */ 
/* 663 */     String str = "/sun/awt/im/preferredInputMethod/" + createLocalePath(paramLocale);
/*     */ 
/* 666 */     while (!str.equals("/sun/awt/im/preferredInputMethod")) {
/*     */       try {
/* 668 */         if ((this.userRoot.nodeExists(str)) && 
/* 669 */           (readPreferredInputMethod(str) != null)) {
/* 670 */           return str;
/*     */         }
/*     */       }
/*     */       catch (BackingStoreException localBackingStoreException)
/*     */       {
/*     */       }
/*     */ 
/* 677 */       str = str.substring(0, str.lastIndexOf('/'));
/*     */     }
/*     */ 
/* 680 */     return null;
/*     */   }
/*     */ 
/*     */   private String readPreferredInputMethod(String paramString) {
/* 684 */     if ((this.userRoot == null) || (paramString == null)) {
/* 685 */       return null;
/*     */     }
/*     */ 
/* 688 */     return this.userRoot.node(paramString).get("descriptor", null);
/*     */   }
/*     */ 
/*     */   private synchronized void putPreferredInputMethod(InputMethodLocator paramInputMethodLocator)
/*     */   {
/* 698 */     InputMethodDescriptor localInputMethodDescriptor = paramInputMethodLocator.getDescriptor();
/* 699 */     Locale localLocale = paramInputMethodLocator.getLocale();
/*     */ 
/* 701 */     if (localLocale == null) {
/*     */       try
/*     */       {
/* 704 */         Locale[] arrayOfLocale = localInputMethodDescriptor.getAvailableLocales();
/* 705 */         if (arrayOfLocale.length == 1) {
/* 706 */           localLocale = arrayOfLocale[0];
/*     */         }
/*     */         else
/* 709 */           return;
/*     */       }
/*     */       catch (AWTException localAWTException)
/*     */       {
/* 713 */         return;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 719 */     if (localLocale.equals(Locale.JAPAN)) {
/* 720 */       localLocale = Locale.JAPANESE;
/*     */     }
/* 722 */     if (localLocale.equals(Locale.KOREA)) {
/* 723 */       localLocale = Locale.KOREAN;
/*     */     }
/* 725 */     if (localLocale.equals(new Locale("th", "TH"))) {
/* 726 */       localLocale = new Locale("th");
/*     */     }
/*     */ 
/* 730 */     String str = "/sun/awt/im/preferredInputMethod/" + createLocalePath(localLocale);
/*     */ 
/* 733 */     writePreferredInputMethod(str, localInputMethodDescriptor.getClass().getName());
/* 734 */     this.preferredLocatorCache.put(localLocale.toString().intern(), paramInputMethodLocator.deriveLocator(localLocale));
/*     */   }
/*     */ 
/*     */   private String createLocalePath(Locale paramLocale)
/*     */   {
/* 741 */     String str1 = paramLocale.getLanguage();
/* 742 */     String str2 = paramLocale.getCountry();
/* 743 */     String str3 = paramLocale.getVariant();
/* 744 */     String str4 = null;
/* 745 */     if (!str3.equals(""))
/* 746 */       str4 = "_" + str1 + "/_" + str2 + "/_" + str3;
/* 747 */     else if (!str2.equals(""))
/* 748 */       str4 = "_" + str1 + "/_" + str2;
/*     */     else {
/* 750 */       str4 = "_" + str1;
/*     */     }
/*     */ 
/* 753 */     return str4;
/*     */   }
/*     */ 
/*     */   private void writePreferredInputMethod(String paramString1, String paramString2) {
/* 757 */     if (this.userRoot != null) {
/* 758 */       Preferences localPreferences = this.userRoot.node(paramString1);
/*     */ 
/* 761 */       if (paramString2 != null)
/* 762 */         localPreferences.put("descriptor", paramString2);
/*     */       else
/* 764 */         localPreferences.remove("descriptor");
/*     */     }
/*     */   }
/*     */ 
/*     */   private Preferences getUserRoot()
/*     */   {
/* 770 */     return (Preferences)AccessController.doPrivileged(new PrivilegedAction() {
/*     */       public Object run() {
/* 772 */         return Preferences.userRoot();
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   private Locale getAdvertisedLocale(InputMethodLocator paramInputMethodLocator, Locale paramLocale) {
/* 778 */     Locale localLocale = null;
/*     */ 
/* 780 */     if (paramInputMethodLocator.isLocaleAvailable(paramLocale))
/* 781 */       localLocale = paramLocale;
/* 782 */     else if (paramLocale.getLanguage().equals("ja"))
/*     */     {
/* 785 */       if (paramInputMethodLocator.isLocaleAvailable(Locale.JAPAN))
/* 786 */         localLocale = Locale.JAPAN;
/* 787 */       else if (paramInputMethodLocator.isLocaleAvailable(Locale.JAPANESE))
/* 788 */         localLocale = Locale.JAPANESE;
/*     */     }
/* 790 */     else if (paramLocale.getLanguage().equals("ko")) {
/* 791 */       if (paramInputMethodLocator.isLocaleAvailable(Locale.KOREA))
/* 792 */         localLocale = Locale.KOREA;
/* 793 */       else if (paramInputMethodLocator.isLocaleAvailable(Locale.KOREAN))
/* 794 */         localLocale = Locale.KOREAN;
/*     */     }
/* 796 */     else if (paramLocale.getLanguage().equals("th")) {
/* 797 */       if (paramInputMethodLocator.isLocaleAvailable(new Locale("th", "TH")))
/* 798 */         localLocale = new Locale("th", "TH");
/* 799 */       else if (paramInputMethodLocator.isLocaleAvailable(new Locale("th"))) {
/* 800 */         localLocale = new Locale("th");
/*     */       }
/*     */     }
/*     */ 
/* 804 */     return localLocale;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.im.ExecutableInputMethodManager
 * JD-Core Version:    0.6.2
 */