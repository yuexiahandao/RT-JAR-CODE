/*      */ package sun.applet;
/*      */ 
/*      */ import java.applet.Applet;
/*      */ import java.applet.AppletContext;
/*      */ import java.applet.AppletStub;
/*      */ import java.awt.BorderLayout;
/*      */ import java.awt.Component;
/*      */ import java.awt.Container;
/*      */ import java.awt.Dimension;
/*      */ import java.awt.EventQueue;
/*      */ import java.awt.FocusTraversalPolicy;
/*      */ import java.awt.Font;
/*      */ import java.awt.Frame;
/*      */ import java.awt.KeyboardFocusManager;
/*      */ import java.awt.Panel;
/*      */ import java.awt.Toolkit;
/*      */ import java.awt.Window;
/*      */ import java.awt.event.InvocationEvent;
/*      */ import java.io.File;
/*      */ import java.io.FilePermission;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.PrintStream;
/*      */ import java.lang.ref.WeakReference;
/*      */ import java.lang.reflect.InvocationTargetException;
/*      */ import java.lang.reflect.Method;
/*      */ import java.net.JarURLConnection;
/*      */ import java.net.SocketPermission;
/*      */ import java.net.URL;
/*      */ import java.net.URLConnection;
/*      */ import java.security.AccessControlContext;
/*      */ import java.security.AccessControlException;
/*      */ import java.security.AccessController;
/*      */ import java.security.CodeSource;
/*      */ import java.security.Permission;
/*      */ import java.security.PermissionCollection;
/*      */ import java.security.Permissions;
/*      */ import java.security.Policy;
/*      */ import java.security.PrivilegedAction;
/*      */ import java.security.ProtectionDomain;
/*      */ import java.security.cert.Certificate;
/*      */ import java.util.HashMap;
/*      */ import java.util.Locale;
/*      */ import java.util.StringTokenizer;
/*      */ import java.util.Vector;
/*      */ import sun.awt.AWTAccessor;
/*      */ import sun.awt.AWTAccessor.EventQueueAccessor;
/*      */ import sun.awt.AppContext;
/*      */ import sun.awt.EmbeddedFrame;
/*      */ import sun.awt.SunToolkit;
/*      */ import sun.misc.MessageUtils;
/*      */ import sun.misc.PerformanceLogger;
/*      */ import sun.misc.Queue;
/*      */ import sun.security.util.SecurityConstants;
/*      */ 
/*      */ public abstract class AppletPanel extends Panel
/*      */   implements AppletStub, Runnable
/*      */ {
/*      */   Applet applet;
/*   78 */   protected boolean doInit = true;
/*      */   protected AppletClassLoader loader;
/*      */   public static final int APPLET_DISPOSE = 0;
/*      */   public static final int APPLET_LOAD = 1;
/*      */   public static final int APPLET_INIT = 2;
/*      */   public static final int APPLET_START = 3;
/*      */   public static final int APPLET_STOP = 4;
/*      */   public static final int APPLET_DESTROY = 5;
/*      */   public static final int APPLET_QUIT = 6;
/*      */   public static final int APPLET_ERROR = 7;
/*      */   public static final int APPLET_RESIZE = 51234;
/*      */   public static final int APPLET_LOADING = 51235;
/*      */   public static final int APPLET_LOADING_COMPLETED = 51236;
/*      */   protected int status;
/*      */   protected Thread handler;
/*  126 */   Dimension defaultAppletSize = new Dimension(10, 10);
/*      */ 
/*  131 */   Dimension currentAppletSize = new Dimension(10, 10);
/*      */ 
/*  133 */   MessageUtils mu = new MessageUtils();
/*      */ 
/*  139 */   Thread loaderThread = null;
/*      */ 
/*  144 */   boolean loadAbortRequest = false;
/*      */ 
/*  155 */   private static int threadGroupNumber = 0;
/*      */   private AppletListener listeners;
/*  257 */   private Queue queue = null;
/*      */ 
/*  893 */   private EventQueue appEvtQ = null;
/*      */ 
/*  984 */   private static HashMap classloaders = new HashMap();
/*      */ 
/* 1201 */   private boolean jdk11Applet = false;
/*      */ 
/* 1204 */   private boolean jdk12Applet = false;
/*      */ 
/* 1315 */   private static AppletMessageHandler amh = new AppletMessageHandler("appletpanel");
/*      */ 
/*      */   protected abstract String getCode();
/*      */ 
/*      */   protected abstract String getJarFiles();
/*      */ 
/*      */   protected abstract String getSerializedObject();
/*      */ 
/*      */   public abstract int getWidth();
/*      */ 
/*      */   public abstract int getHeight();
/*      */ 
/*      */   public abstract boolean hasInitialFocus();
/*      */ 
/*      */   protected void setupAppletAppContext()
/*      */   {
/*      */   }
/*      */ 
/*      */   synchronized void createAppletThread()
/*      */   {
/*  168 */     String str1 = "applet-" + getCode();
/*  169 */     this.loader = getClassLoader(getCodeBase(), getClassLoaderCacheKey());
/*  170 */     this.loader.grab();
/*      */ 
/*  174 */     String str2 = getParameter("codebase_lookup");
/*      */ 
/*  176 */     if ((str2 != null) && (str2.equals("false")))
/*  177 */       this.loader.setCodebaseLookup(false);
/*      */     else {
/*  179 */       this.loader.setCodebaseLookup(true);
/*      */     }
/*      */ 
/*  182 */     ThreadGroup localThreadGroup = this.loader.getThreadGroup();
/*      */ 
/*  184 */     this.handler = new Thread(localThreadGroup, this, "thread " + str1);
/*      */ 
/*  186 */     AccessController.doPrivileged(new PrivilegedAction() {
/*      */       public Object run() {
/*  188 */         AppletPanel.this.handler.setContextClassLoader(AppletPanel.this.loader);
/*  189 */         return null;
/*      */       }
/*      */     });
/*  192 */     this.handler.start();
/*      */   }
/*      */ 
/*      */   void joinAppletThread() throws InterruptedException {
/*  196 */     if (this.handler != null) {
/*  197 */       this.handler.join();
/*  198 */       this.handler = null;
/*      */     }
/*      */   }
/*      */ 
/*      */   void release() {
/*  203 */     if (this.loader != null) {
/*  204 */       this.loader.release();
/*  205 */       this.loader = null;
/*      */     }
/*      */   }
/*      */ 
/*      */   public void init()
/*      */   {
/*      */     try
/*      */     {
/*  215 */       this.defaultAppletSize.width = getWidth();
/*  216 */       this.currentAppletSize.width = this.defaultAppletSize.width;
/*      */ 
/*  219 */       this.defaultAppletSize.height = getHeight();
/*  220 */       this.currentAppletSize.height = this.defaultAppletSize.height;
/*      */     }
/*      */     catch (NumberFormatException localNumberFormatException)
/*      */     {
/*  225 */       this.status = 7;
/*  226 */       showAppletStatus("badattribute.exception");
/*  227 */       showAppletLog("badattribute.exception");
/*  228 */       showAppletException(localNumberFormatException);
/*      */     }
/*      */ 
/*  231 */     setLayout(new BorderLayout());
/*      */ 
/*  233 */     createAppletThread();
/*      */   }
/*      */ 
/*      */   public Dimension minimumSize()
/*      */   {
/*  240 */     return new Dimension(this.defaultAppletSize.width, this.defaultAppletSize.height);
/*      */   }
/*      */ 
/*      */   public Dimension preferredSize()
/*      */   {
/*  248 */     return new Dimension(this.currentAppletSize.width, this.currentAppletSize.height);
/*      */   }
/*      */ 
/*      */   public synchronized void addAppletListener(AppletListener paramAppletListener)
/*      */   {
/*  261 */     this.listeners = AppletEventMulticaster.add(this.listeners, paramAppletListener);
/*      */   }
/*      */ 
/*      */   public synchronized void removeAppletListener(AppletListener paramAppletListener) {
/*  265 */     this.listeners = AppletEventMulticaster.remove(this.listeners, paramAppletListener);
/*      */   }
/*      */ 
/*      */   public void dispatchAppletEvent(int paramInt, Object paramObject)
/*      */   {
/*  273 */     if (this.listeners != null) {
/*  274 */       AppletEvent localAppletEvent = new AppletEvent(this, paramInt, paramObject);
/*  275 */       this.listeners.appletStateChanged(localAppletEvent);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void sendEvent(int paramInt)
/*      */   {
/*  283 */     synchronized (this) {
/*  284 */       if (this.queue == null)
/*      */       {
/*  286 */         this.queue = new Queue();
/*      */       }
/*  288 */       Integer localInteger = Integer.valueOf(paramInt);
/*  289 */       this.queue.enqueue(localInteger);
/*  290 */       notifyAll();
/*      */     }
/*  292 */     if (paramInt == 6) {
/*      */       try {
/*  294 */         joinAppletThread();
/*      */       }
/*      */       catch (InterruptedException localInterruptedException)
/*      */       {
/*      */       }
/*      */ 
/*  300 */       if (this.loader == null)
/*  301 */         this.loader = getClassLoader(getCodeBase(), getClassLoaderCacheKey());
/*  302 */       release();
/*      */     }
/*      */   }
/*      */ 
/*      */   synchronized AppletEvent getNextEvent()
/*      */     throws InterruptedException
/*      */   {
/*  310 */     while ((this.queue == null) || (this.queue.isEmpty())) {
/*  311 */       wait();
/*      */     }
/*  313 */     Integer localInteger = (Integer)this.queue.dequeue();
/*  314 */     return new AppletEvent(this, localInteger.intValue(), null);
/*      */   }
/*      */ 
/*      */   boolean emptyEventQueue() {
/*  318 */     if ((this.queue == null) || (this.queue.isEmpty())) {
/*  319 */       return true;
/*      */     }
/*  321 */     return false;
/*      */   }
/*      */ 
/*      */   private void setExceptionStatus(AccessControlException paramAccessControlException)
/*      */   {
/*  332 */     Permission localPermission = paramAccessControlException.getPermission();
/*  333 */     if (((localPermission instanceof RuntimePermission)) && 
/*  334 */       (localPermission.getName().startsWith("modifyThread"))) {
/*  335 */       if (this.loader == null)
/*  336 */         this.loader = getClassLoader(getCodeBase(), getClassLoaderCacheKey());
/*  337 */       this.loader.setExceptionStatus();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void run()
/*      */   {
/*  373 */     Thread localThread = Thread.currentThread();
/*  374 */     if (localThread == this.loaderThread)
/*      */     {
/*  379 */       runLoader();
/*  380 */       return;
/*      */     }
/*      */ 
/*  383 */     int i = 0;
/*  384 */     while ((i == 0) && (!localThread.isInterrupted())) {
/*      */       AppletEvent localAppletEvent;
/*      */       try {
/*  387 */         localAppletEvent = getNextEvent();
/*      */       } catch (InterruptedException localInterruptedException1) {
/*  389 */         showAppletStatus("bail");
/*  390 */         return;
/*      */       }
/*      */       try
/*      */       {
/*      */         Object localObject;
/*  395 */         switch (localAppletEvent.getID()) {
/*      */         case 1:
/*  397 */           if (okToLoad())
/*      */           {
/*  408 */             if (this.loaderThread == null)
/*      */             {
/*  411 */               setLoaderThread(new Thread(this));
/*  412 */               this.loaderThread.start();
/*      */ 
/*  414 */               this.loaderThread.join();
/*  415 */               setLoaderThread(null); }  } break;
/*      */         case 2:
/*  425 */           if ((this.status != 1) && (this.status != 5)) {
/*  426 */             showAppletStatus("notloaded");
/*      */           }
/*      */           else {
/*  429 */             this.applet.resize(this.defaultAppletSize);
/*  430 */             if (this.doInit) {
/*  431 */               if (PerformanceLogger.loggingEnabled()) {
/*  432 */                 PerformanceLogger.setTime("Applet Init");
/*  433 */                 PerformanceLogger.outputLog();
/*      */               }
/*  435 */               this.applet.init();
/*      */             }
/*      */ 
/*  439 */             Font localFont = getFont();
/*  440 */             if ((localFont == null) || (("dialog".equals(localFont.getFamily().toLowerCase(Locale.ENGLISH))) && (localFont.getSize() == 12) && (localFont.getStyle() == 0)))
/*      */             {
/*  443 */               setFont(new Font("Dialog", 0, 12));
/*      */             }
/*      */ 
/*  446 */             this.doInit = true;
/*      */             try
/*      */             {
/*  451 */               final AppletPanel localAppletPanel1 = this;
/*  452 */               localObject = new Runnable() {
/*      */                 public void run() {
/*  454 */                   localAppletPanel1.validate();
/*      */                 }
/*      */               };
/*  457 */               AWTAccessor.getEventQueueAccessor().invokeAndWait(this.applet, (Runnable)localObject);
/*      */             }
/*      */             catch (InterruptedException localInterruptedException2)
/*      */             {
/*      */             }
/*      */             catch (InvocationTargetException localInvocationTargetException1) {
/*      */             }
/*  464 */             this.status = 2;
/*  465 */             showAppletStatus("inited");
/*  466 */           }break;
/*      */         case 3:
/*  470 */           if ((this.status != 2) && (this.status != 4)) {
/*  471 */             showAppletStatus("notinited");
/*      */           }
/*      */           else {
/*  474 */             this.applet.resize(this.currentAppletSize);
/*  475 */             this.applet.start();
/*      */             try
/*      */             {
/*  480 */               final AppletPanel localAppletPanel2 = this;
/*  481 */               localObject = this.applet;
/*  482 */               Runnable local3 = new Runnable() {
/*      */                 public void run() {
/*  484 */                   localAppletPanel2.validate();
/*  485 */                   this.val$a.setVisible(true);
/*      */ 
/*  489 */                   if (AppletPanel.this.hasInitialFocus())
/*  490 */                     AppletPanel.this.setDefaultFocus();
/*      */                 }
/*      */               };
/*  494 */               AWTAccessor.getEventQueueAccessor().invokeAndWait(this.applet, local3);
/*      */             }
/*      */             catch (InterruptedException localInterruptedException3)
/*      */             {
/*      */             }
/*      */             catch (InvocationTargetException localInvocationTargetException2) {
/*      */             }
/*  501 */             this.status = 3;
/*  502 */             showAppletStatus("started");
/*  503 */           }break;
/*      */         case 4:
/*  507 */           if (this.status != 3) {
/*  508 */             showAppletStatus("notstarted");
/*      */           }
/*      */           else {
/*  511 */             this.status = 4;
/*      */             try
/*      */             {
/*  516 */               final Applet localApplet1 = this.applet;
/*  517 */               localObject = new Runnable() {
/*      */                 public void run() {
/*  519 */                   localApplet1.setVisible(false);
/*      */                 }
/*      */               };
/*  522 */               AWTAccessor.getEventQueueAccessor().invokeAndWait(this.applet, (Runnable)localObject);
/*      */             }
/*      */             catch (InterruptedException localInterruptedException4)
/*      */             {
/*      */             }
/*      */             catch (InvocationTargetException localInvocationTargetException3)
/*      */             {
/*      */             }
/*      */ 
/*      */             try
/*      */             {
/*  536 */               this.applet.stop();
/*      */             } catch (AccessControlException localAccessControlException1) {
/*  538 */               setExceptionStatus(localAccessControlException1);
/*      */ 
/*  540 */               throw localAccessControlException1;
/*      */             }
/*  542 */             showAppletStatus("stopped");
/*  543 */           }break;
/*      */         case 5:
/*  546 */           if ((this.status != 4) && (this.status != 2)) {
/*  547 */             showAppletStatus("notstopped");
/*      */           }
/*      */           else {
/*  550 */             this.status = 5;
/*      */             try
/*      */             {
/*  558 */               this.applet.destroy();
/*      */             } catch (AccessControlException localAccessControlException2) {
/*  560 */               setExceptionStatus(localAccessControlException2);
/*      */ 
/*  562 */               throw localAccessControlException2;
/*      */             }
/*  564 */             showAppletStatus("destroyed");
/*  565 */           }break;
/*      */         case 0:
/*  568 */           if ((this.status != 5) && (this.status != 1)) {
/*  569 */             showAppletStatus("notdestroyed");
/*      */           }
/*      */           else {
/*  572 */             this.status = 0;
/*      */             try
/*      */             {
/*  575 */               final Applet localApplet2 = this.applet;
/*  576 */               localObject = new Runnable() {
/*      */                 public void run() {
/*  578 */                   AppletPanel.this.remove(localApplet2);
/*      */                 }
/*      */               };
/*  581 */               AWTAccessor.getEventQueueAccessor().invokeAndWait(this.applet, (Runnable)localObject);
/*      */             }
/*      */             catch (InterruptedException localInterruptedException5)
/*      */             {
/*      */             }
/*      */             catch (InvocationTargetException localInvocationTargetException4)
/*      */             {
/*      */             }
/*  589 */             this.applet = null;
/*  590 */             showAppletStatus("disposed");
/*  591 */             i = 1;
/*  592 */           }break;
/*      */         case 6:
/*  595 */           return;
/*      */         }
/*      */       } catch (Exception localException) {
/*  598 */         this.status = 7;
/*  599 */         if (localException.getMessage() != null) {
/*  600 */           showAppletStatus("exception2", localException.getClass().getName(), localException.getMessage());
/*      */         }
/*      */         else {
/*  603 */           showAppletStatus("exception", localException.getClass().getName());
/*      */         }
/*  605 */         showAppletException(localException);
/*      */       } catch (ThreadDeath localThreadDeath) {
/*  607 */         showAppletStatus("death");
/*  608 */         return;
/*      */       } catch (Error localError) {
/*  610 */         this.status = 7;
/*  611 */         if (localError.getMessage() != null) {
/*  612 */           showAppletStatus("error2", localError.getClass().getName(), localError.getMessage());
/*      */         }
/*      */         else {
/*  615 */           showAppletStatus("error", localError.getClass().getName());
/*      */         }
/*  617 */         showAppletException(localError);
/*      */       }
/*  619 */       clearLoadAbortRequest();
/*      */     }
/*      */   }
/*      */ 
/*      */   private Component getMostRecentFocusOwnerForWindow(Window paramWindow)
/*      */   {
/*  630 */     Method localMethod = (Method)AccessController.doPrivileged(new PrivilegedAction() {
/*      */       public Object run() {
/*  632 */         Method localMethod = null;
/*      */         try {
/*  634 */           localMethod = KeyboardFocusManager.class.getDeclaredMethod("getMostRecentFocusOwner", new Class[] { Window.class });
/*  635 */           localMethod.setAccessible(true);
/*      */         }
/*      */         catch (Exception localException) {
/*  638 */           localException.printStackTrace();
/*      */         }
/*  640 */         return localMethod;
/*      */       }
/*      */     });
/*  643 */     if (localMethod != null) {
/*      */       try
/*      */       {
/*  646 */         return (Component)localMethod.invoke(null, new Object[] { paramWindow });
/*      */       }
/*      */       catch (Exception localException) {
/*  649 */         localException.printStackTrace();
/*      */       }
/*      */     }
/*      */ 
/*  653 */     return paramWindow.getMostRecentFocusOwner();
/*      */   }
/*      */ 
/*      */   private void setDefaultFocus()
/*      */   {
/*  661 */     Component localComponent = null;
/*  662 */     Container localContainer = getParent();
/*      */ 
/*  664 */     if (localContainer != null) {
/*  665 */       if ((localContainer instanceof Window)) {
/*  666 */         localComponent = getMostRecentFocusOwnerForWindow((Window)localContainer);
/*  667 */         if ((localComponent == localContainer) || (localComponent == null)) {
/*  668 */           localComponent = localContainer.getFocusTraversalPolicy().getInitialComponent((Window)localContainer);
/*      */         }
/*      */       }
/*  671 */       else if (localContainer.isFocusCycleRoot()) {
/*  672 */         localComponent = localContainer.getFocusTraversalPolicy().getDefaultComponent(localContainer);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  677 */     if (localComponent != null) {
/*  678 */       if ((localContainer instanceof EmbeddedFrame)) {
/*  679 */         ((EmbeddedFrame)localContainer).synthesizeWindowActivation(true);
/*      */       }
/*      */ 
/*  685 */       localComponent.requestFocusInWindow();
/*      */     }
/*      */   }
/*      */ 
/*      */   private void runLoader()
/*      */   {
/*  696 */     if (this.status != 0) {
/*  697 */       showAppletStatus("notdisposed");
/*  698 */       return;
/*      */     }
/*      */ 
/*  701 */     dispatchAppletEvent(51235, null);
/*      */ 
/*  705 */     this.status = 1;
/*      */ 
/*  708 */     this.loader = getClassLoader(getCodeBase(), getClassLoaderCacheKey());
/*      */ 
/*  714 */     String str = getCode();
/*      */ 
/*  718 */     setupAppletAppContext();
/*      */     try
/*      */     {
/*  721 */       loadJarFiles(this.loader);
/*  722 */       this.applet = createApplet(this.loader); } catch (ClassNotFoundException localClassNotFoundException) { this.status = 7;
/*  725 */       showAppletStatus("notfound", str);
/*  726 */       showAppletLog("notfound", str);
/*  727 */       showAppletException(localClassNotFoundException);
/*      */       return; } catch (InstantiationException localInstantiationException) { this.status = 7;
/*  731 */       showAppletStatus("nocreate", str);
/*  732 */       showAppletLog("nocreate", str);
/*  733 */       showAppletException(localInstantiationException);
/*      */       return; } catch (IllegalAccessException localIllegalAccessException) { this.status = 7;
/*  737 */       showAppletStatus("noconstruct", str);
/*  738 */       showAppletLog("noconstruct", str);
/*  739 */       showAppletException(localIllegalAccessException);
/*      */       return; } catch (Exception localException) { this.status = 7;
/*  744 */       showAppletStatus("exception", localException.getMessage());
/*  745 */       showAppletException(localException);
/*      */       return; } catch (ThreadDeath localThreadDeath) { this.status = 7;
/*  749 */       showAppletStatus("death");
/*      */       return; } catch (Error localError) { this.status = 7;
/*  753 */       showAppletStatus("error", localError.getMessage());
/*  754 */       showAppletException(localError);
/*      */       return; } finally { dispatchAppletEvent(51236, null); }
/*      */ 
/*      */ 
/*  764 */     if (this.applet != null)
/*      */     {
/*  767 */       this.applet.setStub(this);
/*  768 */       this.applet.hide();
/*  769 */       add("Center", this.applet);
/*  770 */       showAppletStatus("loaded");
/*  771 */       validate();
/*      */     }
/*      */   }
/*      */ 
/*      */   protected Applet createApplet(final AppletClassLoader paramAppletClassLoader) throws ClassNotFoundException, IllegalAccessException, IOException, InstantiationException, InterruptedException
/*      */   {
/*  777 */     final String str1 = getSerializedObject();
/*  778 */     String str2 = getCode();
/*      */ 
/*  780 */     if ((str2 != null) && (str1 != null)) {
/*  781 */       System.err.println(amh.getMessage("runloader.err"));
/*      */ 
/*  783 */       throw new InstantiationException("Either \"code\" or \"object\" should be specified, but not both.");
/*      */     }
/*      */     Object localObject1;
/*  785 */     if ((str2 == null) && (str1 == null)) {
/*  786 */       localObject1 = "nocode";
/*  787 */       this.status = 7;
/*  788 */       showAppletStatus((String)localObject1);
/*  789 */       showAppletLog((String)localObject1);
/*  790 */       repaint();
/*      */     }
/*  792 */     if (str2 != null) {
/*  793 */       this.applet = ((Applet)paramAppletClassLoader.loadCode(str2).newInstance());
/*  794 */       this.doInit = true;
/*      */     }
/*      */     else {
/*  797 */       localObject1 = (InputStream)AccessController.doPrivileged(new PrivilegedAction()
/*      */       {
/*      */         public Object run()
/*      */         {
/*  801 */           return paramAppletClassLoader.getResourceAsStream(str1);
/*      */         }
/*      */       });
/*  804 */       AppletObjectInputStream localAppletObjectInputStream = new AppletObjectInputStream((InputStream)localObject1, paramAppletClassLoader);
/*      */ 
/*  806 */       Object localObject2 = localAppletObjectInputStream.readObject();
/*  807 */       this.applet = ((Applet)localObject2);
/*  808 */       this.doInit = false;
/*      */     }
/*      */ 
/*  815 */     findAppletJDKLevel(this.applet);
/*      */ 
/*  817 */     if (Thread.interrupted()) {
/*      */       try {
/*  819 */         this.status = 0;
/*  820 */         this.applet = null;
/*      */ 
/*  824 */         showAppletStatus("death");
/*      */       } finally {
/*  826 */         Thread.currentThread().interrupt();
/*      */       }
/*  828 */       return null;
/*      */     }
/*  830 */     return this.applet;
/*      */   }
/*      */ 
/*      */   protected void loadJarFiles(AppletClassLoader paramAppletClassLoader)
/*      */     throws IOException, InterruptedException
/*      */   {
/*  838 */     String str1 = getJarFiles();
/*      */ 
/*  840 */     if (str1 != null) {
/*  841 */       StringTokenizer localStringTokenizer = new StringTokenizer(str1, ",", false);
/*  842 */       while (localStringTokenizer.hasMoreTokens()) {
/*  843 */         String str2 = localStringTokenizer.nextToken().trim();
/*      */         try {
/*  845 */           paramAppletClassLoader.addJar(str2);
/*      */         }
/*      */         catch (IllegalArgumentException localIllegalArgumentException)
/*      */         {
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   protected synchronized void stopLoading()
/*      */   {
/*  859 */     if (this.loaderThread != null)
/*      */     {
/*  861 */       this.loaderThread.interrupt();
/*      */     }
/*  863 */     else setLoadAbortRequest();
/*      */   }
/*      */ 
/*      */   protected synchronized boolean okToLoad()
/*      */   {
/*  869 */     return !this.loadAbortRequest;
/*      */   }
/*      */ 
/*      */   protected synchronized void clearLoadAbortRequest() {
/*  873 */     this.loadAbortRequest = false;
/*      */   }
/*      */ 
/*      */   protected synchronized void setLoadAbortRequest() {
/*  877 */     this.loadAbortRequest = true;
/*      */   }
/*      */ 
/*      */   private synchronized void setLoaderThread(Thread paramThread)
/*      */   {
/*  882 */     this.loaderThread = paramThread;
/*      */   }
/*      */ 
/*      */   public boolean isActive()
/*      */   {
/*  889 */     return this.status == 3;
/*      */   }
/*      */ 
/*      */   public void appletResize(int paramInt1, int paramInt2)
/*      */   {
/*  898 */     this.currentAppletSize.width = paramInt1;
/*  899 */     this.currentAppletSize.height = paramInt2;
/*  900 */     final Dimension localDimension = new Dimension(this.currentAppletSize.width, this.currentAppletSize.height);
/*      */ 
/*  903 */     if (this.loader != null) {
/*  904 */       localObject = this.loader.getAppContext();
/*  905 */       if (localObject != null) {
/*  906 */         this.appEvtQ = ((EventQueue)((AppContext)localObject).get(AppContext.EVENT_QUEUE_KEY));
/*      */       }
/*      */     }
/*  909 */     Object localObject = this;
/*  910 */     if (this.appEvtQ != null)
/*  911 */       this.appEvtQ.postEvent(new InvocationEvent(Toolkit.getDefaultToolkit(), new Runnable()
/*      */       {
/*      */         public void run() {
/*  914 */           if (this.val$ap != null)
/*      */           {
/*  916 */             this.val$ap.dispatchAppletEvent(51234, localDimension);
/*      */           }
/*      */         }
/*      */       }));
/*      */   }
/*      */ 
/*      */   public void setBounds(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/*  924 */     super.setBounds(paramInt1, paramInt2, paramInt3, paramInt4);
/*  925 */     this.currentAppletSize.width = paramInt3;
/*  926 */     this.currentAppletSize.height = paramInt4;
/*      */   }
/*      */ 
/*      */   public Applet getApplet() {
/*  930 */     return this.applet;
/*      */   }
/*      */ 
/*      */   protected void showAppletStatus(String paramString)
/*      */   {
/*  938 */     getAppletContext().showStatus(amh.getMessage(paramString));
/*      */   }
/*      */ 
/*      */   protected void showAppletStatus(String paramString, Object paramObject) {
/*  942 */     getAppletContext().showStatus(amh.getMessage(paramString, paramObject));
/*      */   }
/*      */   protected void showAppletStatus(String paramString, Object paramObject1, Object paramObject2) {
/*  945 */     getAppletContext().showStatus(amh.getMessage(paramString, paramObject1, paramObject2));
/*      */   }
/*      */ 
/*      */   protected void showAppletLog(String paramString)
/*      */   {
/*  952 */     System.out.println(amh.getMessage(paramString));
/*      */   }
/*      */ 
/*      */   protected void showAppletLog(String paramString, Object paramObject) {
/*  956 */     System.out.println(amh.getMessage(paramString, paramObject));
/*      */   }
/*      */ 
/*      */   protected void showAppletException(Throwable paramThrowable)
/*      */   {
/*  964 */     paramThrowable.printStackTrace();
/*  965 */     repaint();
/*      */   }
/*      */ 
/*      */   public String getClassLoaderCacheKey()
/*      */   {
/*  978 */     return getCodeBase().toString();
/*      */   }
/*      */ 
/*      */   public static synchronized void flushClassLoader(String paramString)
/*      */   {
/*  990 */     classloaders.remove(paramString);
/*      */   }
/*      */ 
/*      */   public static synchronized void flushClassLoaders()
/*      */   {
/*  997 */     classloaders = new HashMap();
/*      */   }
/*      */ 
/*      */   protected AppletClassLoader createClassLoader(URL paramURL)
/*      */   {
/* 1007 */     return new AppletClassLoader(paramURL);
/*      */   }
/*      */ 
/*      */   synchronized AppletClassLoader getClassLoader(final URL paramURL, final String paramString)
/*      */   {
/* 1014 */     AppletClassLoader localAppletClassLoader = (AppletClassLoader)classloaders.get(paramString);
/* 1015 */     if (localAppletClassLoader == null) {
/* 1016 */       AccessControlContext localAccessControlContext = getAccessControlContext(paramURL);
/*      */ 
/* 1018 */       localAppletClassLoader = (AppletClassLoader)AccessController.doPrivileged(new PrivilegedAction()
/*      */       {
/*      */         public Object run() {
/* 1021 */           AppletClassLoader localAppletClassLoader1 = AppletPanel.this.createClassLoader(paramURL);
/*      */ 
/* 1037 */           synchronized (getClass()) {
/* 1038 */             AppletClassLoader localAppletClassLoader2 = (AppletClassLoader)AppletPanel.classloaders.get(paramString);
/*      */ 
/* 1040 */             if (localAppletClassLoader2 == null) {
/* 1041 */               AppletPanel.classloaders.put(paramString, localAppletClassLoader1);
/* 1042 */               return localAppletClassLoader1;
/*      */             }
/* 1044 */             return localAppletClassLoader2;
/*      */           }
/*      */         }
/*      */       }
/*      */       , localAccessControlContext);
/*      */     }
/*      */ 
/* 1050 */     return localAppletClassLoader;
/*      */   }
/*      */ 
/*      */   private AccessControlContext getAccessControlContext(URL paramURL)
/*      */   {
/* 1061 */     Object localObject1 = (PermissionCollection)AccessController.doPrivileged(new PrivilegedAction()
/*      */     {
/*      */       public Object run() {
/* 1064 */         Policy localPolicy = Policy.getPolicy();
/* 1065 */         if (localPolicy != null) {
/* 1066 */           return localPolicy.getPermissions(new CodeSource(null, (Certificate[])null));
/*      */         }
/*      */ 
/* 1069 */         return null;
/*      */       }
/*      */     });
/* 1074 */     if (localObject1 == null) {
/* 1075 */       localObject1 = new Permissions();
/* 1079 */     }
/*      */ ((PermissionCollection)localObject1).add(SecurityConstants.CREATE_CLASSLOADER_PERMISSION);
/*      */ 
/* 1082 */     URLConnection localURLConnection = null;
/*      */     Permission localPermission;
/*      */     try {
/* 1084 */       localURLConnection = paramURL.openConnection();
/* 1085 */       localPermission = localURLConnection.getPermission();
/*      */     } catch (IOException localIOException) {
/* 1087 */       localPermission = null;
/*      */     }
/*      */ 
/* 1090 */     if (localPermission != null) {
/* 1091 */       ((PermissionCollection)localObject1).add(localPermission);
/*      */     }
/* 1093 */     if ((localPermission instanceof FilePermission))
/*      */     {
/* 1095 */       localObject2 = localPermission.getName();
/*      */ 
/* 1097 */       int i = ((String)localObject2).lastIndexOf(File.separatorChar);
/*      */ 
/* 1099 */       if (i != -1) {
/* 1100 */         localObject2 = ((String)localObject2).substring(0, i + 1);
/*      */ 
/* 1102 */         if (((String)localObject2).endsWith(File.separator)) {
/* 1103 */           localObject2 = (String)localObject2 + "-";
/*      */         }
/* 1105 */         ((PermissionCollection)localObject1).add(new FilePermission((String)localObject2, "read"));
/*      */       }
/*      */     }
/*      */     else {
/* 1109 */       localObject2 = paramURL;
/* 1110 */       if ((localURLConnection instanceof JarURLConnection)) {
/* 1111 */         localObject2 = ((JarURLConnection)localURLConnection).getJarFileURL();
/*      */       }
/* 1113 */       localObject3 = ((URL)localObject2).getHost();
/* 1114 */       if ((localObject3 != null) && (((String)localObject3).length() > 0)) {
/* 1115 */         ((PermissionCollection)localObject1).add(new SocketPermission((String)localObject3, "connect,accept"));
/*      */       }
/*      */     }
/*      */ 
/* 1119 */     Object localObject2 = new ProtectionDomain(new CodeSource(paramURL, (Certificate[])null), (PermissionCollection)localObject1);
/*      */ 
/* 1122 */     Object localObject3 = new AccessControlContext(new ProtectionDomain[] { localObject2 });
/*      */ 
/* 1125 */     return localObject3;
/*      */   }
/*      */ 
/*      */   public Thread getAppletHandlerThread() {
/* 1129 */     return this.handler;
/*      */   }
/*      */ 
/*      */   public int getAppletWidth() {
/* 1133 */     return this.currentAppletSize.width;
/*      */   }
/*      */ 
/*      */   public int getAppletHeight() {
/* 1137 */     return this.currentAppletSize.height;
/*      */   }
/*      */ 
/*      */   public static void changeFrameAppContext(Frame paramFrame, AppContext paramAppContext)
/*      */   {
/* 1157 */     AppContext localAppContext = SunToolkit.targetToAppContext(paramFrame);
/*      */ 
/* 1159 */     if (localAppContext == paramAppContext) {
/* 1160 */       return;
/*      */     }
/*      */ 
/* 1164 */     synchronized (Window.class)
/*      */     {
/* 1166 */       Object localObject1 = null;
/*      */ 
/* 1170 */       Vector localVector = (Vector)localAppContext.get(Window.class);
/* 1171 */       if (localVector != null) {
/* 1172 */         for (WeakReference localWeakReference : localVector) {
/* 1173 */           if (localWeakReference.get() == paramFrame) {
/* 1174 */             localObject1 = localWeakReference;
/* 1175 */             break;
/*      */           }
/*      */         }
/*      */ 
/* 1179 */         if (localObject1 != null) {
/* 1180 */           localVector.remove(localObject1);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1185 */       SunToolkit.insertTargetMapping(paramFrame, paramAppContext);
/*      */ 
/* 1189 */       localVector = (Vector)paramAppContext.get(Window.class);
/* 1190 */       if (localVector == null) {
/* 1191 */         localVector = new Vector();
/* 1192 */         paramAppContext.put(Window.class, localVector);
/*      */       }
/*      */ 
/* 1195 */       localVector.add(localObject1);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void findAppletJDKLevel(Applet paramApplet)
/*      */   {
/* 1218 */     Class localClass = paramApplet.getClass();
/*      */ 
/* 1220 */     synchronized (localClass)
/*      */     {
/* 1223 */       Boolean localBoolean1 = this.loader.isJDK11Target(localClass);
/* 1224 */       Boolean localBoolean2 = this.loader.isJDK12Target(localClass);
/*      */ 
/* 1228 */       if ((localBoolean1 != null) || (localBoolean2 != null)) {
/* 1229 */         this.jdk11Applet = (localBoolean1 == null ? false : localBoolean1.booleanValue());
/* 1230 */         this.jdk12Applet = (localBoolean2 == null ? false : localBoolean2.booleanValue());
/* 1231 */         return;
/*      */       }
/*      */ 
/* 1234 */       String str1 = localClass.getName();
/*      */ 
/* 1237 */       str1 = str1.replace('.', '/');
/*      */ 
/* 1240 */       final String str2 = str1 + ".class";
/*      */ 
/* 1242 */       InputStream localInputStream = null;
/* 1243 */       byte[] arrayOfByte = new byte[8];
/*      */       try
/*      */       {
/* 1246 */         localInputStream = (InputStream)AccessController.doPrivileged(new PrivilegedAction()
/*      */         {
/*      */           public Object run() {
/* 1249 */             return AppletPanel.this.loader.getResourceAsStream(str2);
/*      */           }
/*      */         });
/* 1254 */         int i = localInputStream.read(arrayOfByte, 0, 8);
/* 1255 */         localInputStream.close();
/*      */ 
/* 1259 */         if (i != 8)
/* 1260 */           return;
/*      */       }
/*      */       catch (IOException localIOException) {
/* 1263 */         return;
/*      */       }
/*      */ 
/* 1267 */       int j = readShort(arrayOfByte, 6);
/*      */ 
/* 1275 */       if (j < 46)
/* 1276 */         this.jdk11Applet = true;
/* 1277 */       else if (j == 46) {
/* 1278 */         this.jdk12Applet = true;
/*      */       }
/*      */ 
/* 1282 */       this.loader.setJDK11Target(localClass, this.jdk11Applet);
/* 1283 */       this.loader.setJDK12Target(localClass, this.jdk12Applet);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected boolean isJDK11Applet()
/*      */   {
/* 1291 */     return this.jdk11Applet;
/*      */   }
/*      */ 
/*      */   protected boolean isJDK12Applet()
/*      */   {
/* 1298 */     return this.jdk12Applet;
/*      */   }
/*      */ 
/*      */   private int readShort(byte[] paramArrayOfByte, int paramInt)
/*      */   {
/* 1305 */     int i = readByte(paramArrayOfByte[paramInt]);
/* 1306 */     int j = readByte(paramArrayOfByte[(paramInt + 1)]);
/* 1307 */     return i << 8 | j;
/*      */   }
/*      */ 
/*      */   private int readByte(byte paramByte) {
/* 1311 */     return paramByte & 0xFF;
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.applet.AppletPanel
 * JD-Core Version:    0.6.2
 */