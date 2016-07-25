/*      */ package java.awt;
/*      */ 
/*      */ import java.awt.datatransfer.Clipboard;
/*      */ import java.awt.dnd.DragGestureEvent;
/*      */ import java.awt.dnd.DragGestureListener;
/*      */ import java.awt.dnd.DragGestureRecognizer;
/*      */ import java.awt.dnd.DragSource;
/*      */ import java.awt.dnd.InvalidDnDOperationException;
/*      */ import java.awt.dnd.peer.DragSourceContextPeer;
/*      */ import java.awt.event.AWTEventListener;
/*      */ import java.awt.event.AWTEventListenerProxy;
/*      */ import java.awt.font.TextAttribute;
/*      */ import java.awt.im.InputMethodHighlight;
/*      */ import java.awt.image.ColorModel;
/*      */ import java.awt.image.ImageObserver;
/*      */ import java.awt.image.ImageProducer;
/*      */ import java.awt.peer.ButtonPeer;
/*      */ import java.awt.peer.CanvasPeer;
/*      */ import java.awt.peer.CheckboxMenuItemPeer;
/*      */ import java.awt.peer.CheckboxPeer;
/*      */ import java.awt.peer.ChoicePeer;
/*      */ import java.awt.peer.DesktopPeer;
/*      */ import java.awt.peer.DialogPeer;
/*      */ import java.awt.peer.FileDialogPeer;
/*      */ import java.awt.peer.FontPeer;
/*      */ import java.awt.peer.FramePeer;
/*      */ import java.awt.peer.LabelPeer;
/*      */ import java.awt.peer.LightweightPeer;
/*      */ import java.awt.peer.ListPeer;
/*      */ import java.awt.peer.MenuBarPeer;
/*      */ import java.awt.peer.MenuItemPeer;
/*      */ import java.awt.peer.MenuPeer;
/*      */ import java.awt.peer.MouseInfoPeer;
/*      */ import java.awt.peer.PanelPeer;
/*      */ import java.awt.peer.PopupMenuPeer;
/*      */ import java.awt.peer.ScrollPanePeer;
/*      */ import java.awt.peer.ScrollbarPeer;
/*      */ import java.awt.peer.TextAreaPeer;
/*      */ import java.awt.peer.TextFieldPeer;
/*      */ import java.awt.peer.WindowPeer;
/*      */ import java.beans.PropertyChangeEvent;
/*      */ import java.beans.PropertyChangeListener;
/*      */ import java.beans.PropertyChangeSupport;
/*      */ import java.io.File;
/*      */ import java.io.FileInputStream;
/*      */ import java.io.InputStream;
/*      */ import java.net.URL;
/*      */ import java.security.AccessController;
/*      */ import java.security.PrivilegedAction;
/*      */ import java.util.ArrayList;
/*      */ import java.util.EventListener;
/*      */ import java.util.HashMap;
/*      */ import java.util.Map;
/*      */ import java.util.MissingResourceException;
/*      */ import java.util.Properties;
/*      */ import java.util.ResourceBundle;
/*      */ import java.util.StringTokenizer;
/*      */ import java.util.WeakHashMap;
/*      */ import sun.awt.AWTAccessor;
/*      */ import sun.awt.AWTAccessor.ToolkitAccessor;
/*      */ import sun.awt.AppContext;
/*      */ import sun.awt.HeadlessToolkit;
/*      */ import sun.awt.NullComponentPeer;
/*      */ import sun.awt.PeerEvent;
/*      */ import sun.awt.SunToolkit;
/*      */ import sun.awt.UngrabEvent;
/*      */ import sun.security.action.LoadLibraryAction;
/*      */ import sun.security.util.SecurityConstants.AWT;
/*      */ import sun.util.CoreResourceBundleControl;
/*      */ 
/*      */ public abstract class Toolkit
/*      */ {
/*      */   private static LightweightPeer lightweightMarker;
/*      */   private static Toolkit toolkit;
/*      */   private static String atNames;
/*      */   private static ResourceBundle resources;
/*      */   private static ResourceBundle platformResources;
/*      */   private static boolean loaded;
/*      */   protected final Map<String, Object> desktopProperties;
/*      */   protected final PropertyChangeSupport desktopPropsSupport;
/*      */   private static final int LONG_BITS = 64;
/*      */   private int[] calls;
/*      */   private static volatile long enabledOnToolkitMask;
/*      */   private AWTEventListener eventListener;
/*      */   private WeakHashMap listener2SelectiveListener;
/*      */ 
/*      */   public Toolkit()
/*      */   {
/* 1946 */     this.desktopProperties = new HashMap();
/*      */ 
/* 1948 */     this.desktopPropsSupport = createPropertyChangeSupport(this);
/*      */ 
/* 2004 */     this.calls = new int[64];
/*      */ 
/* 2006 */     this.eventListener = null;
/* 2007 */     this.listener2SelectiveListener = new WeakHashMap();
/*      */   }
/*      */ 
/*      */   protected abstract DesktopPeer createDesktopPeer(Desktop paramDesktop)
/*      */     throws HeadlessException;
/*      */ 
/*      */   protected abstract ButtonPeer createButton(Button paramButton)
/*      */     throws HeadlessException;
/*      */ 
/*      */   protected abstract TextFieldPeer createTextField(TextField paramTextField)
/*      */     throws HeadlessException;
/*      */ 
/*      */   protected abstract LabelPeer createLabel(Label paramLabel)
/*      */     throws HeadlessException;
/*      */ 
/*      */   protected abstract ListPeer createList(List paramList)
/*      */     throws HeadlessException;
/*      */ 
/*      */   protected abstract CheckboxPeer createCheckbox(Checkbox paramCheckbox)
/*      */     throws HeadlessException;
/*      */ 
/*      */   protected abstract ScrollbarPeer createScrollbar(Scrollbar paramScrollbar)
/*      */     throws HeadlessException;
/*      */ 
/*      */   protected abstract ScrollPanePeer createScrollPane(ScrollPane paramScrollPane)
/*      */     throws HeadlessException;
/*      */ 
/*      */   protected abstract TextAreaPeer createTextArea(TextArea paramTextArea)
/*      */     throws HeadlessException;
/*      */ 
/*      */   protected abstract ChoicePeer createChoice(Choice paramChoice)
/*      */     throws HeadlessException;
/*      */ 
/*      */   protected abstract FramePeer createFrame(Frame paramFrame)
/*      */     throws HeadlessException;
/*      */ 
/*      */   protected abstract CanvasPeer createCanvas(Canvas paramCanvas);
/*      */ 
/*      */   protected abstract PanelPeer createPanel(Panel paramPanel);
/*      */ 
/*      */   protected abstract WindowPeer createWindow(Window paramWindow)
/*      */     throws HeadlessException;
/*      */ 
/*      */   protected abstract DialogPeer createDialog(Dialog paramDialog)
/*      */     throws HeadlessException;
/*      */ 
/*      */   protected abstract MenuBarPeer createMenuBar(MenuBar paramMenuBar)
/*      */     throws HeadlessException;
/*      */ 
/*      */   protected abstract MenuPeer createMenu(Menu paramMenu)
/*      */     throws HeadlessException;
/*      */ 
/*      */   protected abstract PopupMenuPeer createPopupMenu(PopupMenu paramPopupMenu)
/*      */     throws HeadlessException;
/*      */ 
/*      */   protected abstract MenuItemPeer createMenuItem(MenuItem paramMenuItem)
/*      */     throws HeadlessException;
/*      */ 
/*      */   protected abstract FileDialogPeer createFileDialog(FileDialog paramFileDialog)
/*      */     throws HeadlessException;
/*      */ 
/*      */   protected abstract CheckboxMenuItemPeer createCheckboxMenuItem(CheckboxMenuItem paramCheckboxMenuItem)
/*      */     throws HeadlessException;
/*      */ 
/*      */   protected MouseInfoPeer getMouseInfoPeer()
/*      */   {
/*  419 */     throw new UnsupportedOperationException("Not implemented");
/*      */   }
/*      */ 
/*      */   protected LightweightPeer createComponent(Component paramComponent)
/*      */   {
/*  432 */     if (lightweightMarker == null) {
/*  433 */       lightweightMarker = new NullComponentPeer();
/*      */     }
/*  435 */     return lightweightMarker;
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   protected abstract FontPeer getFontPeer(String paramString, int paramInt);
/*      */ 
/*      */   protected void loadSystemColors(int[] paramArrayOfInt)
/*      */     throws HeadlessException
/*      */   {
/*  468 */     GraphicsEnvironment.checkHeadless();
/*      */   }
/*      */ 
/*      */   public void setDynamicLayout(boolean paramBoolean)
/*      */     throws HeadlessException
/*      */   {
/*  503 */     GraphicsEnvironment.checkHeadless();
/*      */   }
/*      */ 
/*      */   protected boolean isDynamicLayoutSet()
/*      */     throws HeadlessException
/*      */   {
/*  527 */     GraphicsEnvironment.checkHeadless();
/*      */ 
/*  529 */     if (this != getDefaultToolkit()) {
/*  530 */       return getDefaultToolkit().isDynamicLayoutSet();
/*      */     }
/*  532 */     return false;
/*      */   }
/*      */ 
/*      */   public boolean isDynamicLayoutActive()
/*      */     throws HeadlessException
/*      */   {
/*  564 */     GraphicsEnvironment.checkHeadless();
/*      */ 
/*  566 */     if (this != getDefaultToolkit()) {
/*  567 */       return getDefaultToolkit().isDynamicLayoutActive();
/*      */     }
/*  569 */     return false;
/*      */   }
/*      */ 
/*      */   public abstract Dimension getScreenSize()
/*      */     throws HeadlessException;
/*      */ 
/*      */   public abstract int getScreenResolution()
/*      */     throws HeadlessException;
/*      */ 
/*      */   public Insets getScreenInsets(GraphicsConfiguration paramGraphicsConfiguration)
/*      */     throws HeadlessException
/*      */   {
/*  609 */     GraphicsEnvironment.checkHeadless();
/*  610 */     if (this != getDefaultToolkit()) {
/*  611 */       return getDefaultToolkit().getScreenInsets(paramGraphicsConfiguration);
/*      */     }
/*  613 */     return new Insets(0, 0, 0, 0);
/*      */   }
/*      */ 
/*      */   public abstract ColorModel getColorModel()
/*      */     throws HeadlessException;
/*      */ 
/*      */   @Deprecated
/*      */   public abstract String[] getFontList();
/*      */ 
/*      */   @Deprecated
/*      */   public abstract FontMetrics getFontMetrics(Font paramFont);
/*      */ 
/*      */   public abstract void sync();
/*      */ 
/*      */   private static void initAssistiveTechnologies()
/*      */   {
/*  704 */     String str = File.separator;
/*  705 */     final Properties localProperties = new Properties();
/*      */ 
/*  708 */     atNames = (String)AccessController.doPrivileged(new PrivilegedAction()
/*      */     {
/*      */       public Object run()
/*      */       {
/*      */         try
/*      */         {
/*  714 */           File localFile1 = new File(System.getProperty("user.home") + this.val$sep + ".accessibility.properties");
/*      */ 
/*  717 */           localObject = new FileInputStream(localFile1);
/*      */ 
/*  721 */           localProperties.load((InputStream)localObject);
/*  722 */           ((FileInputStream)localObject).close();
/*      */         }
/*      */         catch (Exception localException1)
/*      */         {
/*      */         }
/*      */ 
/*  730 */         if (localProperties.size() == 0) {
/*      */           try {
/*  732 */             File localFile2 = new File(System.getProperty("java.home") + this.val$sep + "lib" + this.val$sep + "accessibility.properties");
/*      */ 
/*  735 */             localObject = new FileInputStream(localFile2);
/*      */ 
/*  739 */             localProperties.load((InputStream)localObject);
/*  740 */             ((FileInputStream)localObject).close();
/*      */           }
/*      */           catch (Exception localException2)
/*      */           {
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*  749 */         String str = System.getProperty("javax.accessibility.screen_magnifier_present");
/*  750 */         if (str == null) {
/*  751 */           str = localProperties.getProperty("screen_magnifier_present", null);
/*  752 */           if (str != null) {
/*  753 */             System.setProperty("javax.accessibility.screen_magnifier_present", str);
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*  760 */         Object localObject = System.getProperty("javax.accessibility.assistive_technologies");
/*  761 */         if (localObject == null) {
/*  762 */           localObject = localProperties.getProperty("assistive_technologies", null);
/*  763 */           if (localObject != null) {
/*  764 */             System.setProperty("javax.accessibility.assistive_technologies", (String)localObject);
/*      */           }
/*      */         }
/*  767 */         return localObject;
/*      */       }
/*      */     });
/*      */   }
/*      */ 
/*      */   private static void loadAssistiveTechnologies()
/*      */   {
/*  793 */     if (atNames != null) {
/*  794 */       ClassLoader localClassLoader = ClassLoader.getSystemClassLoader();
/*  795 */       StringTokenizer localStringTokenizer = new StringTokenizer(atNames, " ,");
/*      */ 
/*  797 */       while (localStringTokenizer.hasMoreTokens()) {
/*  798 */         String str = localStringTokenizer.nextToken();
/*      */         try
/*      */         {
/*      */           Class localClass;
/*  801 */           if (localClassLoader != null)
/*  802 */             localClass = localClassLoader.loadClass(str);
/*      */           else {
/*  804 */             localClass = Class.forName(str);
/*      */           }
/*  806 */           localClass.newInstance();
/*      */         } catch (ClassNotFoundException localClassNotFoundException) {
/*  808 */           throw new AWTError("Assistive Technology not found: " + str);
/*      */         }
/*      */         catch (InstantiationException localInstantiationException) {
/*  811 */           throw new AWTError("Could not instantiate Assistive Technology: " + str);
/*      */         }
/*      */         catch (IllegalAccessException localIllegalAccessException) {
/*  814 */           throw new AWTError("Could not access Assistive Technology: " + str);
/*      */         }
/*      */         catch (Exception localException) {
/*  817 */           throw new AWTError("Error trying to install Assistive Technology: " + str + " " + localException);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public static synchronized Toolkit getDefaultToolkit()
/*      */   {
/*  854 */     if (toolkit == null)
/*      */     {
/*      */       try
/*      */       {
/*  859 */         Compiler.disable();
/*      */ 
/*  861 */         AccessController.doPrivileged(new PrivilegedAction()
/*      */         {
/*      */           public Object run() {
/*  864 */             String str = null;
/*  865 */             Class localClass = null;
/*      */             try {
/*  867 */               str = System.getProperty("awt.toolkit");
/*      */               try {
/*  869 */                 localClass = Class.forName(str);
/*      */               } catch (ClassNotFoundException localClassNotFoundException1) {
/*  871 */                 ClassLoader localClassLoader = ClassLoader.getSystemClassLoader();
/*  872 */                 if (localClassLoader != null) {
/*      */                   try {
/*  874 */                     localClass = localClassLoader.loadClass(str);
/*      */                   } catch (ClassNotFoundException localClassNotFoundException2) {
/*  876 */                     throw new AWTError("Toolkit not found: " + str);
/*      */                   }
/*      */                 }
/*      */               }
/*  880 */               if (localClass != null) {
/*  881 */                 Toolkit.access$002((Toolkit)localClass.newInstance());
/*  882 */                 if (GraphicsEnvironment.isHeadless())
/*  883 */                   Toolkit.access$002(new HeadlessToolkit(Toolkit.toolkit));
/*      */               }
/*      */             }
/*      */             catch (InstantiationException localInstantiationException) {
/*  887 */               throw new AWTError("Could not instantiate Toolkit: " + str);
/*      */             } catch (IllegalAccessException localIllegalAccessException) {
/*  889 */               throw new AWTError("Could not access Toolkit: " + str);
/*      */             }
/*  891 */             return null;
/*      */           }
/*      */         });
/*  894 */         loadAssistiveTechnologies();
/*      */       }
/*      */       finally {
/*  897 */         Compiler.enable();
/*      */       }
/*      */     }
/*  900 */     return toolkit;
/*      */   }
/*      */ 
/*      */   public abstract Image getImage(String paramString);
/*      */ 
/*      */   public abstract Image getImage(URL paramURL);
/*      */ 
/*      */   public abstract Image createImage(String paramString);
/*      */ 
/*      */   public abstract Image createImage(URL paramURL);
/*      */ 
/*      */   public abstract boolean prepareImage(Image paramImage, int paramInt1, int paramInt2, ImageObserver paramImageObserver);
/*      */ 
/*      */   public abstract int checkImage(Image paramImage, int paramInt1, int paramInt2, ImageObserver paramImageObserver);
/*      */ 
/*      */   public abstract Image createImage(ImageProducer paramImageProducer);
/*      */ 
/*      */   public Image createImage(byte[] paramArrayOfByte)
/*      */   {
/* 1123 */     return createImage(paramArrayOfByte, 0, paramArrayOfByte.length);
/*      */   }
/*      */ 
/*      */   public abstract Image createImage(byte[] paramArrayOfByte, int paramInt1, int paramInt2);
/*      */ 
/*      */   public abstract PrintJob getPrintJob(Frame paramFrame, String paramString, Properties paramProperties);
/*      */ 
/*      */   public PrintJob getPrintJob(Frame paramFrame, String paramString, JobAttributes paramJobAttributes, PageAttributes paramPageAttributes)
/*      */   {
/* 1233 */     if (this != getDefaultToolkit()) {
/* 1234 */       return getDefaultToolkit().getPrintJob(paramFrame, paramString, paramJobAttributes, paramPageAttributes);
/*      */     }
/*      */ 
/* 1238 */     return getPrintJob(paramFrame, paramString, null);
/*      */   }
/*      */ 
/*      */   public abstract void beep();
/*      */ 
/*      */   public abstract Clipboard getSystemClipboard()
/*      */     throws HeadlessException;
/*      */ 
/*      */   public Clipboard getSystemSelection()
/*      */     throws HeadlessException
/*      */   {
/* 1348 */     GraphicsEnvironment.checkHeadless();
/*      */ 
/* 1350 */     if (this != getDefaultToolkit()) {
/* 1351 */       return getDefaultToolkit().getSystemSelection();
/*      */     }
/* 1353 */     GraphicsEnvironment.checkHeadless();
/* 1354 */     return null;
/*      */   }
/*      */ 
/*      */   public int getMenuShortcutKeyMask()
/*      */     throws HeadlessException
/*      */   {
/* 1379 */     GraphicsEnvironment.checkHeadless();
/*      */ 
/* 1381 */     return 2;
/*      */   }
/*      */ 
/*      */   public boolean getLockingKeyState(int paramInt)
/*      */     throws UnsupportedOperationException
/*      */   {
/* 1406 */     GraphicsEnvironment.checkHeadless();
/*      */ 
/* 1408 */     if ((paramInt != 20) && (paramInt != 144) && (paramInt != 145) && (paramInt != 262))
/*      */     {
/* 1410 */       throw new IllegalArgumentException("invalid key for Toolkit.getLockingKeyState");
/*      */     }
/* 1412 */     throw new UnsupportedOperationException("Toolkit.getLockingKeyState");
/*      */   }
/*      */ 
/*      */   public void setLockingKeyState(int paramInt, boolean paramBoolean)
/*      */     throws UnsupportedOperationException
/*      */   {
/* 1440 */     GraphicsEnvironment.checkHeadless();
/*      */ 
/* 1442 */     if ((paramInt != 20) && (paramInt != 144) && (paramInt != 145) && (paramInt != 262))
/*      */     {
/* 1444 */       throw new IllegalArgumentException("invalid key for Toolkit.setLockingKeyState");
/*      */     }
/* 1446 */     throw new UnsupportedOperationException("Toolkit.setLockingKeyState");
/*      */   }
/*      */ 
/*      */   protected static Container getNativeContainer(Component paramComponent)
/*      */   {
/* 1454 */     return paramComponent.getNativeContainer();
/*      */   }
/*      */ 
/*      */   public Cursor createCustomCursor(Image paramImage, Point paramPoint, String paramString)
/*      */     throws IndexOutOfBoundsException, HeadlessException
/*      */   {
/* 1481 */     if (this != getDefaultToolkit()) {
/* 1482 */       return getDefaultToolkit().createCustomCursor(paramImage, paramPoint, paramString);
/*      */     }
/*      */ 
/* 1485 */     return new Cursor(0);
/*      */   }
/*      */ 
/*      */   public Dimension getBestCursorSize(int paramInt1, int paramInt2)
/*      */     throws HeadlessException
/*      */   {
/* 1515 */     GraphicsEnvironment.checkHeadless();
/*      */ 
/* 1518 */     if (this != getDefaultToolkit()) {
/* 1519 */       return getDefaultToolkit().getBestCursorSize(paramInt1, paramInt2);
/*      */     }
/*      */ 
/* 1522 */     return new Dimension(0, 0);
/*      */   }
/*      */ 
/*      */   public int getMaximumCursorColors()
/*      */     throws HeadlessException
/*      */   {
/* 1544 */     GraphicsEnvironment.checkHeadless();
/*      */ 
/* 1547 */     if (this != getDefaultToolkit()) {
/* 1548 */       return getDefaultToolkit().getMaximumCursorColors();
/*      */     }
/* 1550 */     return 0;
/*      */   }
/*      */ 
/*      */   public boolean isFrameStateSupported(int paramInt)
/*      */     throws HeadlessException
/*      */   {
/* 1595 */     GraphicsEnvironment.checkHeadless();
/*      */ 
/* 1597 */     if (this != getDefaultToolkit()) {
/* 1598 */       return getDefaultToolkit().isFrameStateSupported(paramInt);
/*      */     }
/*      */ 
/* 1601 */     return paramInt == 0;
/*      */   }
/*      */ 
/*      */   private static void setPlatformResources(ResourceBundle paramResourceBundle)
/*      */   {
/* 1615 */     platformResources = paramResourceBundle;
/*      */   }
/*      */ 
/*      */   private static native void initIDs();
/*      */ 
/*      */   static void loadLibraries()
/*      */   {
/* 1652 */     if (!loaded) {
/* 1653 */       AccessController.doPrivileged(new LoadLibraryAction("awt"));
/*      */ 
/* 1655 */       loaded = true;
/*      */     }
/*      */   }
/*      */ 
/*      */   public static String getProperty(String paramString1, String paramString2)
/*      */   {
/* 1695 */     if (platformResources != null)
/*      */       try {
/* 1697 */         return platformResources.getString(paramString1);
/*      */       }
/*      */       catch (MissingResourceException localMissingResourceException1)
/*      */       {
/*      */       }
/* 1702 */     if (resources != null)
/*      */       try {
/* 1704 */         return resources.getString(paramString1);
/*      */       }
/*      */       catch (MissingResourceException localMissingResourceException2)
/*      */       {
/*      */       }
/* 1709 */     return paramString2;
/*      */   }
/*      */ 
/*      */   public final EventQueue getSystemEventQueue()
/*      */   {
/* 1735 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 1736 */     if (localSecurityManager != null) {
/* 1737 */       localSecurityManager.checkAwtEventQueueAccess();
/*      */     }
/* 1739 */     return getSystemEventQueueImpl();
/*      */   }
/*      */ 
/*      */   protected abstract EventQueue getSystemEventQueueImpl();
/*      */ 
/*      */   static EventQueue getEventQueue()
/*      */   {
/* 1752 */     return getDefaultToolkit().getSystemEventQueueImpl();
/*      */   }
/*      */ 
/*      */   public abstract DragSourceContextPeer createDragSourceContextPeer(DragGestureEvent paramDragGestureEvent)
/*      */     throws InvalidDnDOperationException;
/*      */ 
/*      */   public <T extends DragGestureRecognizer> T createDragGestureRecognizer(Class<T> paramClass, DragSource paramDragSource, Component paramComponent, int paramInt, DragGestureListener paramDragGestureListener)
/*      */   {
/* 1785 */     return null;
/*      */   }
/*      */ 
/*      */   public final synchronized Object getDesktopProperty(String paramString)
/*      */   {
/* 1802 */     if ((this instanceof HeadlessToolkit)) {
/* 1803 */       return ((HeadlessToolkit)this).getUnderlyingToolkit().getDesktopProperty(paramString);
/*      */     }
/*      */ 
/* 1807 */     if (this.desktopProperties.isEmpty()) {
/* 1808 */       initializeDesktopProperties();
/*      */     }
/*      */ 
/* 1814 */     if (paramString.equals("awt.dynamicLayoutSupported")) {
/* 1815 */       localObject = lazilyLoadDesktopProperty(paramString);
/* 1816 */       return localObject;
/*      */     }
/*      */ 
/* 1819 */     Object localObject = this.desktopProperties.get(paramString);
/*      */ 
/* 1821 */     if (localObject == null) {
/* 1822 */       localObject = lazilyLoadDesktopProperty(paramString);
/*      */ 
/* 1824 */       if (localObject != null) {
/* 1825 */         setDesktopProperty(paramString, localObject);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1830 */     if ((localObject instanceof RenderingHints)) {
/* 1831 */       localObject = ((RenderingHints)localObject).clone();
/*      */     }
/*      */ 
/* 1834 */     return localObject;
/*      */   }
/*      */ 
/*      */   protected final void setDesktopProperty(String paramString, Object paramObject)
/*      */   {
/* 1846 */     if ((this instanceof HeadlessToolkit)) {
/* 1847 */       ((HeadlessToolkit)this).getUnderlyingToolkit().setDesktopProperty(paramString, paramObject);
/*      */       return;
/*      */     }
/*      */     Object localObject1;
/* 1853 */     synchronized (this) {
/* 1854 */       localObject1 = this.desktopProperties.get(paramString);
/* 1855 */       this.desktopProperties.put(paramString, paramObject);
/*      */     }
/*      */ 
/* 1860 */     if ((localObject1 != null) || (paramObject != null))
/* 1861 */       this.desktopPropsSupport.firePropertyChange(paramString, localObject1, paramObject);
/*      */   }
/*      */ 
/*      */   protected Object lazilyLoadDesktopProperty(String paramString)
/*      */   {
/* 1869 */     return null;
/*      */   }
/*      */ 
/*      */   protected void initializeDesktopProperties()
/*      */   {
/*      */   }
/*      */ 
/*      */   public void addPropertyChangeListener(String paramString, PropertyChangeListener paramPropertyChangeListener)
/*      */   {
/* 1892 */     this.desktopPropsSupport.addPropertyChangeListener(paramString, paramPropertyChangeListener);
/*      */   }
/*      */ 
/*      */   public void removePropertyChangeListener(String paramString, PropertyChangeListener paramPropertyChangeListener)
/*      */   {
/* 1910 */     this.desktopPropsSupport.removePropertyChangeListener(paramString, paramPropertyChangeListener);
/*      */   }
/*      */ 
/*      */   public PropertyChangeListener[] getPropertyChangeListeners()
/*      */   {
/* 1927 */     return this.desktopPropsSupport.getPropertyChangeListeners();
/*      */   }
/*      */ 
/*      */   public PropertyChangeListener[] getPropertyChangeListeners(String paramString)
/*      */   {
/* 1943 */     return this.desktopPropsSupport.getPropertyChangeListeners(paramString);
/*      */   }
/*      */ 
/*      */   public boolean isAlwaysOnTopSupported()
/*      */   {
/* 1962 */     return true;
/*      */   }
/*      */ 
/*      */   public abstract boolean isModalityTypeSupported(Dialog.ModalityType paramModalityType);
/*      */ 
/*      */   public abstract boolean isModalExclusionTypeSupported(Dialog.ModalExclusionType paramModalExclusionType);
/*      */ 
/*      */   private static AWTEventListener deProxyAWTEventListener(AWTEventListener paramAWTEventListener)
/*      */   {
/* 2015 */     AWTEventListener localAWTEventListener = paramAWTEventListener;
/*      */ 
/* 2017 */     if (localAWTEventListener == null) {
/* 2018 */       return null;
/*      */     }
/*      */ 
/* 2022 */     if ((paramAWTEventListener instanceof AWTEventListenerProxy)) {
/* 2023 */       localAWTEventListener = (AWTEventListener)((AWTEventListenerProxy)paramAWTEventListener).getListener();
/*      */     }
/* 2025 */     return localAWTEventListener;
/*      */   }
/*      */ 
/*      */   public void addAWTEventListener(AWTEventListener paramAWTEventListener, long paramLong)
/*      */   {
/* 2063 */     AWTEventListener localAWTEventListener = deProxyAWTEventListener(paramAWTEventListener);
/*      */ 
/* 2065 */     if (localAWTEventListener == null) {
/* 2066 */       return;
/*      */     }
/* 2068 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 2069 */     if (localSecurityManager != null) {
/* 2070 */       localSecurityManager.checkPermission(SecurityConstants.AWT.ALL_AWT_EVENTS_PERMISSION);
/*      */     }
/* 2072 */     synchronized (this) {
/* 2073 */       SelectiveAWTEventListener localSelectiveAWTEventListener = (SelectiveAWTEventListener)this.listener2SelectiveListener.get(localAWTEventListener);
/*      */ 
/* 2076 */       if (localSelectiveAWTEventListener == null)
/*      */       {
/* 2078 */         localSelectiveAWTEventListener = new SelectiveAWTEventListener(localAWTEventListener, paramLong);
/*      */ 
/* 2080 */         this.listener2SelectiveListener.put(localAWTEventListener, localSelectiveAWTEventListener);
/* 2081 */         this.eventListener = ToolkitEventMulticaster.add(this.eventListener, localSelectiveAWTEventListener);
/*      */       }
/*      */ 
/* 2085 */       localSelectiveAWTEventListener.orEventMasks(paramLong);
/*      */ 
/* 2087 */       enabledOnToolkitMask |= paramLong;
/*      */ 
/* 2089 */       long l = paramLong;
/* 2090 */       for (int i = 0; i < 64; i++)
/*      */       {
/* 2092 */         if (l == 0L) {
/*      */           break;
/*      */         }
/* 2095 */         if ((l & 1L) != 0L) {
/* 2096 */           this.calls[i] += 1;
/*      */         }
/* 2098 */         l >>>= 1;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public void removeAWTEventListener(AWTEventListener paramAWTEventListener)
/*      */   {
/* 2132 */     AWTEventListener localAWTEventListener = deProxyAWTEventListener(paramAWTEventListener);
/*      */ 
/* 2134 */     if (paramAWTEventListener == null) {
/* 2135 */       return;
/*      */     }
/* 2137 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 2138 */     if (localSecurityManager != null) {
/* 2139 */       localSecurityManager.checkPermission(SecurityConstants.AWT.ALL_AWT_EVENTS_PERMISSION);
/*      */     }
/*      */ 
/* 2142 */     synchronized (this) {
/* 2143 */       SelectiveAWTEventListener localSelectiveAWTEventListener = (SelectiveAWTEventListener)this.listener2SelectiveListener.get(localAWTEventListener);
/*      */ 
/* 2146 */       if (localSelectiveAWTEventListener != null) {
/* 2147 */         this.listener2SelectiveListener.remove(localAWTEventListener);
/* 2148 */         int[] arrayOfInt = localSelectiveAWTEventListener.getCalls();
/* 2149 */         for (int i = 0; i < 64; i++) {
/* 2150 */           this.calls[i] -= arrayOfInt[i];
/* 2151 */           assert (this.calls[i] >= 0) : "Negative Listeners count";
/*      */ 
/* 2153 */           if (this.calls[i] == 0) {
/* 2154 */             enabledOnToolkitMask &= (1L << i ^ 0xFFFFFFFF);
/*      */           }
/*      */         }
/*      */       }
/* 2158 */       this.eventListener = ToolkitEventMulticaster.remove(this.eventListener, localSelectiveAWTEventListener == null ? localAWTEventListener : localSelectiveAWTEventListener);
/*      */     }
/*      */   }
/*      */ 
/*      */   static boolean enabledOnToolkit(long paramLong)
/*      */   {
/* 2164 */     return (enabledOnToolkitMask & paramLong) != 0L;
/*      */   }
/*      */ 
/*      */   synchronized int countAWTEventListeners(long paramLong) {
/* 2168 */     for (int i = 0; 
/* 2169 */       paramLong != 0L; i++) paramLong >>>= 1;
/*      */ 
/* 2171 */     i--;
/* 2172 */     return this.calls[i];
/*      */   }
/*      */ 
/*      */   public AWTEventListener[] getAWTEventListeners()
/*      */   {
/* 2202 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 2203 */     if (localSecurityManager != null) {
/* 2204 */       localSecurityManager.checkPermission(SecurityConstants.AWT.ALL_AWT_EVENTS_PERMISSION);
/*      */     }
/* 2206 */     synchronized (this) {
/* 2207 */       EventListener[] arrayOfEventListener = ToolkitEventMulticaster.getListeners(this.eventListener, AWTEventListener.class);
/*      */ 
/* 2209 */       AWTEventListener[] arrayOfAWTEventListener = new AWTEventListener[arrayOfEventListener.length];
/* 2210 */       for (int i = 0; i < arrayOfEventListener.length; i++) {
/* 2211 */         SelectiveAWTEventListener localSelectiveAWTEventListener = (SelectiveAWTEventListener)arrayOfEventListener[i];
/* 2212 */         AWTEventListener localAWTEventListener = localSelectiveAWTEventListener.getListener();
/*      */ 
/* 2216 */         arrayOfAWTEventListener[i] = new AWTEventListenerProxy(localSelectiveAWTEventListener.getEventMask(), localAWTEventListener);
/*      */       }
/* 2218 */       return arrayOfAWTEventListener;
/*      */     }
/*      */   }
/*      */ 
/*      */   public AWTEventListener[] getAWTEventListeners(long paramLong)
/*      */   {
/* 2254 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 2255 */     if (localSecurityManager != null) {
/* 2256 */       localSecurityManager.checkPermission(SecurityConstants.AWT.ALL_AWT_EVENTS_PERMISSION);
/*      */     }
/* 2258 */     synchronized (this) {
/* 2259 */       EventListener[] arrayOfEventListener = ToolkitEventMulticaster.getListeners(this.eventListener, AWTEventListener.class);
/*      */ 
/* 2261 */       ArrayList localArrayList = new ArrayList(arrayOfEventListener.length);
/*      */ 
/* 2263 */       for (int i = 0; i < arrayOfEventListener.length; i++) {
/* 2264 */         SelectiveAWTEventListener localSelectiveAWTEventListener = (SelectiveAWTEventListener)arrayOfEventListener[i];
/* 2265 */         if ((localSelectiveAWTEventListener.getEventMask() & paramLong) == paramLong)
/*      */         {
/* 2267 */           localArrayList.add(new AWTEventListenerProxy(localSelectiveAWTEventListener.getEventMask(), localSelectiveAWTEventListener.getListener()));
/*      */         }
/*      */       }
/*      */ 
/* 2271 */       return (AWTEventListener[])localArrayList.toArray(new AWTEventListener[0]);
/*      */     }
/*      */   }
/*      */ 
/*      */   void notifyAWTEventListeners(AWTEvent paramAWTEvent)
/*      */   {
/* 2286 */     if ((this instanceof HeadlessToolkit)) {
/* 2287 */       ((HeadlessToolkit)this).getUnderlyingToolkit().notifyAWTEventListeners(paramAWTEvent);
/*      */ 
/* 2289 */       return;
/*      */     }
/*      */ 
/* 2292 */     AWTEventListener localAWTEventListener = this.eventListener;
/* 2293 */     if (localAWTEventListener != null)
/* 2294 */       localAWTEventListener.eventDispatched(paramAWTEvent);
/*      */   }
/*      */ 
/*      */   public abstract Map<TextAttribute, ?> mapInputMethodHighlight(InputMethodHighlight paramInputMethodHighlight)
/*      */     throws HeadlessException;
/*      */ 
/*      */   private static PropertyChangeSupport createPropertyChangeSupport(Toolkit paramToolkit)
/*      */   {
/* 2467 */     if (((paramToolkit instanceof SunToolkit)) || ((paramToolkit instanceof HeadlessToolkit))) {
/* 2468 */       return new DesktopPropertyChangeSupport(paramToolkit);
/*      */     }
/* 2470 */     return new PropertyChangeSupport(paramToolkit);
/*      */   }
/*      */ 
/*      */   public boolean areExtraMouseButtonsEnabled()
/*      */     throws HeadlessException
/*      */   {
/* 2618 */     GraphicsEnvironment.checkHeadless();
/*      */ 
/* 2620 */     return getDefaultToolkit().areExtraMouseButtonsEnabled();
/*      */   }
/*      */ 
/*      */   static
/*      */   {
/* 1650 */     loaded = false;
/*      */ 
/* 1660 */     AWTAccessor.setToolkitAccessor(new AWTAccessor.ToolkitAccessor()
/*      */     {
/*      */       public void setPlatformResources(ResourceBundle paramAnonymousResourceBundle)
/*      */       {
/* 1664 */         Toolkit.setPlatformResources(paramAnonymousResourceBundle);
/*      */       }
/*      */     });
/* 1667 */     AccessController.doPrivileged(new PrivilegedAction()
/*      */     {
/*      */       public Object run() {
/*      */         try {
/* 1671 */           Toolkit.access$202(ResourceBundle.getBundle("sun.awt.resources.awt", CoreResourceBundleControl.getRBControlInstance()));
/*      */         }
/*      */         catch (MissingResourceException localMissingResourceException)
/*      */         {
/*      */         }
/*      */ 
/* 1677 */         return null;
/*      */       }
/*      */     });
/* 1682 */     loadLibraries();
/* 1683 */     initAssistiveTechnologies();
/* 1684 */     if (!GraphicsEnvironment.isHeadless())
/* 1685 */       initIDs();
/*      */   }
/*      */ 
/*      */   private static class DesktopPropertyChangeSupport extends PropertyChangeSupport
/*      */   {
/* 2475 */     private static final StringBuilder PROP_CHANGE_SUPPORT_KEY = new StringBuilder("desktop property change support key");
/*      */     private final Object source;
/*      */ 
/*      */     public DesktopPropertyChangeSupport(Object paramObject)
/*      */     {
/* 2480 */       super();
/* 2481 */       this.source = paramObject;
/*      */     }
/*      */ 
/*      */     public synchronized void addPropertyChangeListener(String paramString, PropertyChangeListener paramPropertyChangeListener)
/*      */     {
/* 2489 */       PropertyChangeSupport localPropertyChangeSupport = (PropertyChangeSupport)AppContext.getAppContext().get(PROP_CHANGE_SUPPORT_KEY);
/*      */ 
/* 2491 */       if (null == localPropertyChangeSupport) {
/* 2492 */         localPropertyChangeSupport = new PropertyChangeSupport(this.source);
/* 2493 */         AppContext.getAppContext().put(PROP_CHANGE_SUPPORT_KEY, localPropertyChangeSupport);
/*      */       }
/* 2495 */       localPropertyChangeSupport.addPropertyChangeListener(paramString, paramPropertyChangeListener);
/*      */     }
/*      */ 
/*      */     public synchronized void removePropertyChangeListener(String paramString, PropertyChangeListener paramPropertyChangeListener)
/*      */     {
/* 2503 */       PropertyChangeSupport localPropertyChangeSupport = (PropertyChangeSupport)AppContext.getAppContext().get(PROP_CHANGE_SUPPORT_KEY);
/*      */ 
/* 2505 */       if (null != localPropertyChangeSupport)
/* 2506 */         localPropertyChangeSupport.removePropertyChangeListener(paramString, paramPropertyChangeListener);
/*      */     }
/*      */ 
/*      */     public synchronized PropertyChangeListener[] getPropertyChangeListeners()
/*      */     {
/* 2513 */       PropertyChangeSupport localPropertyChangeSupport = (PropertyChangeSupport)AppContext.getAppContext().get(PROP_CHANGE_SUPPORT_KEY);
/*      */ 
/* 2515 */       if (null != localPropertyChangeSupport) {
/* 2516 */         return localPropertyChangeSupport.getPropertyChangeListeners();
/*      */       }
/* 2518 */       return new PropertyChangeListener[0];
/*      */     }
/*      */ 
/*      */     public synchronized PropertyChangeListener[] getPropertyChangeListeners(String paramString)
/*      */     {
/* 2525 */       PropertyChangeSupport localPropertyChangeSupport = (PropertyChangeSupport)AppContext.getAppContext().get(PROP_CHANGE_SUPPORT_KEY);
/*      */ 
/* 2527 */       if (null != localPropertyChangeSupport) {
/* 2528 */         return localPropertyChangeSupport.getPropertyChangeListeners(paramString);
/*      */       }
/* 2530 */       return new PropertyChangeListener[0];
/*      */     }
/*      */ 
/*      */     public synchronized void addPropertyChangeListener(PropertyChangeListener paramPropertyChangeListener)
/*      */     {
/* 2536 */       PropertyChangeSupport localPropertyChangeSupport = (PropertyChangeSupport)AppContext.getAppContext().get(PROP_CHANGE_SUPPORT_KEY);
/*      */ 
/* 2538 */       if (null == localPropertyChangeSupport) {
/* 2539 */         localPropertyChangeSupport = new PropertyChangeSupport(this.source);
/* 2540 */         AppContext.getAppContext().put(PROP_CHANGE_SUPPORT_KEY, localPropertyChangeSupport);
/*      */       }
/* 2542 */       localPropertyChangeSupport.addPropertyChangeListener(paramPropertyChangeListener);
/*      */     }
/*      */ 
/*      */     public synchronized void removePropertyChangeListener(PropertyChangeListener paramPropertyChangeListener)
/*      */     {
/* 2547 */       PropertyChangeSupport localPropertyChangeSupport = (PropertyChangeSupport)AppContext.getAppContext().get(PROP_CHANGE_SUPPORT_KEY);
/*      */ 
/* 2549 */       if (null != localPropertyChangeSupport)
/* 2550 */         localPropertyChangeSupport.removePropertyChangeListener(paramPropertyChangeListener);
/*      */     }
/*      */ 
/*      */     public void firePropertyChange(final PropertyChangeEvent paramPropertyChangeEvent)
/*      */     {
/* 2560 */       Object localObject1 = paramPropertyChangeEvent.getOldValue();
/* 2561 */       Object localObject2 = paramPropertyChangeEvent.getNewValue();
/* 2562 */       String str = paramPropertyChangeEvent.getPropertyName();
/* 2563 */       if ((localObject1 != null) && (localObject2 != null) && (localObject1.equals(localObject2))) {
/* 2564 */         return;
/*      */       }
/* 2566 */       Runnable local1 = new Runnable() {
/*      */         public void run() {
/* 2568 */           PropertyChangeSupport localPropertyChangeSupport = (PropertyChangeSupport)AppContext.getAppContext().get(Toolkit.DesktopPropertyChangeSupport.PROP_CHANGE_SUPPORT_KEY);
/*      */ 
/* 2570 */           if (null != localPropertyChangeSupport)
/* 2571 */             localPropertyChangeSupport.firePropertyChange(paramPropertyChangeEvent);
/*      */         }
/*      */       };
/* 2575 */       AppContext localAppContext1 = AppContext.getAppContext();
/* 2576 */       for (AppContext localAppContext2 : AppContext.getAppContexts())
/* 2577 */         if ((null != localAppContext2) && (!localAppContext2.isDisposed()))
/*      */         {
/* 2580 */           if (localAppContext1 == localAppContext2) {
/* 2581 */             local1.run();
/*      */           } else {
/* 2583 */             PeerEvent localPeerEvent = new PeerEvent(this.source, local1, 2L);
/* 2584 */             SunToolkit.postEvent(localAppContext2, localPeerEvent);
/*      */           }
/*      */         }
/*      */     }
/*      */   }
/*      */ 
/*      */   private class SelectiveAWTEventListener
/*      */     implements AWTEventListener
/*      */   {
/*      */     AWTEventListener listener;
/*      */     private long eventMask;
/* 2345 */     int[] calls = new int[64];
/*      */ 
/* 2347 */     public AWTEventListener getListener() { return this.listener; } 
/* 2348 */     public long getEventMask() { return this.eventMask; } 
/* 2349 */     public int[] getCalls() { return this.calls; }
/*      */ 
/*      */     public void orEventMasks(long paramLong) {
/* 2352 */       this.eventMask |= paramLong;
/*      */ 
/* 2354 */       for (int i = 0; i < 64; i++)
/*      */       {
/* 2356 */         if (paramLong == 0L) {
/*      */           break;
/*      */         }
/* 2359 */         if ((paramLong & 1L) != 0L) {
/* 2360 */           this.calls[i] += 1;
/*      */         }
/* 2362 */         paramLong >>>= 1;
/*      */       }
/*      */     }
/*      */ 
/*      */     SelectiveAWTEventListener(AWTEventListener paramLong, long arg3) {
/* 2367 */       this.listener = paramLong;
/*      */       Object localObject;
/* 2368 */       this.eventMask = localObject;
/*      */     }
/*      */ 
/*      */     public void eventDispatched(AWTEvent paramAWTEvent) {
/* 2372 */       long l1 = 0L;
/* 2373 */       if ((((l1 = this.eventMask & 1L) != 0L) && (paramAWTEvent.id >= 100) && (paramAWTEvent.id <= 103)) || (((l1 = this.eventMask & 0x2) != 0L) && (paramAWTEvent.id >= 300) && (paramAWTEvent.id <= 301)) || (((l1 = this.eventMask & 0x4) != 0L) && (paramAWTEvent.id >= 1004) && (paramAWTEvent.id <= 1005)) || (((l1 = this.eventMask & 0x8) != 0L) && (paramAWTEvent.id >= 400) && (paramAWTEvent.id <= 402)) || (((l1 = this.eventMask & 0x20000) != 0L) && (paramAWTEvent.id == 507)) || (((l1 = this.eventMask & 0x20) != 0L) && ((paramAWTEvent.id == 503) || (paramAWTEvent.id == 506))) || (((l1 = this.eventMask & 0x10) != 0L) && (paramAWTEvent.id != 503) && (paramAWTEvent.id != 506) && (paramAWTEvent.id != 507) && (paramAWTEvent.id >= 500) && (paramAWTEvent.id <= 507)) || (((l1 = this.eventMask & 0x40) != 0L) && (paramAWTEvent.id >= 200) && (paramAWTEvent.id <= 209)) || (((l1 = this.eventMask & 0x80) != 0L) && (paramAWTEvent.id >= 1001) && (paramAWTEvent.id <= 1001)) || (((l1 = this.eventMask & 0x100) != 0L) && (paramAWTEvent.id >= 601) && (paramAWTEvent.id <= 601)) || (((l1 = this.eventMask & 0x200) != 0L) && (paramAWTEvent.id >= 701) && (paramAWTEvent.id <= 701)) || (((l1 = this.eventMask & 0x400) != 0L) && (paramAWTEvent.id >= 900) && (paramAWTEvent.id <= 900)) || (((l1 = this.eventMask & 0x800) != 0L) && (paramAWTEvent.id >= 1100) && (paramAWTEvent.id <= 1101)) || (((l1 = this.eventMask & 0x2000) != 0L) && (paramAWTEvent.id >= 800) && (paramAWTEvent.id <= 801)) || (((l1 = this.eventMask & 0x4000) != 0L) && (paramAWTEvent.id >= 1200) && (paramAWTEvent.id <= 1200)) || (((l1 = this.eventMask & 0x8000) != 0L) && (paramAWTEvent.id == 1400)) || (((l1 = this.eventMask & 0x10000) != 0L) && ((paramAWTEvent.id == 1401) || (paramAWTEvent.id == 1402))) || (((l1 = this.eventMask & 0x40000) != 0L) && (paramAWTEvent.id == 209)) || (((l1 = this.eventMask & 0x80000) != 0L) && ((paramAWTEvent.id == 207) || (paramAWTEvent.id == 208))) || (((l1 = this.eventMask & 0x80000000) != 0L) && ((paramAWTEvent instanceof UngrabEvent))))
/*      */       {
/* 2437 */         int i = 0;
/* 2438 */         for (long l2 = l1; l2 != 0L; i++) l2 >>>= 1;
/*      */ 
/* 2440 */         i--;
/*      */ 
/* 2443 */         for (int j = 0; j < this.calls[i]; j++)
/* 2444 */           this.listener.eventDispatched(paramAWTEvent);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class ToolkitEventMulticaster extends AWTEventMulticaster
/*      */     implements AWTEventListener
/*      */   {
/*      */     ToolkitEventMulticaster(AWTEventListener paramAWTEventListener1, AWTEventListener paramAWTEventListener2)
/*      */     {
/* 2303 */       super(paramAWTEventListener2);
/*      */     }
/*      */ 
/*      */     static AWTEventListener add(AWTEventListener paramAWTEventListener1, AWTEventListener paramAWTEventListener2)
/*      */     {
/* 2308 */       if (paramAWTEventListener1 == null) return paramAWTEventListener2;
/* 2309 */       if (paramAWTEventListener2 == null) return paramAWTEventListener1;
/* 2310 */       return new ToolkitEventMulticaster(paramAWTEventListener1, paramAWTEventListener2);
/*      */     }
/*      */ 
/*      */     static AWTEventListener remove(AWTEventListener paramAWTEventListener1, AWTEventListener paramAWTEventListener2)
/*      */     {
/* 2315 */       return (AWTEventListener)removeInternal(paramAWTEventListener1, paramAWTEventListener2);
/*      */     }
/*      */ 
/*      */     protected EventListener remove(EventListener paramEventListener)
/*      */     {
/* 2324 */       if (paramEventListener == this.a) return this.b;
/* 2325 */       if (paramEventListener == this.b) return this.a;
/* 2326 */       AWTEventListener localAWTEventListener1 = (AWTEventListener)removeInternal(this.a, paramEventListener);
/* 2327 */       AWTEventListener localAWTEventListener2 = (AWTEventListener)removeInternal(this.b, paramEventListener);
/* 2328 */       if ((localAWTEventListener1 == this.a) && (localAWTEventListener2 == this.b)) {
/* 2329 */         return this;
/*      */       }
/* 2331 */       return add(localAWTEventListener1, localAWTEventListener2);
/*      */     }
/*      */ 
/*      */     public void eventDispatched(AWTEvent paramAWTEvent) {
/* 2335 */       ((AWTEventListener)this.a).eventDispatched(paramAWTEvent);
/* 2336 */       ((AWTEventListener)this.b).eventDispatched(paramAWTEvent);
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.Toolkit
 * JD-Core Version:    0.6.2
 */