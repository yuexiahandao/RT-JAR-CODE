/*      */ package sun.awt;
/*      */ 
/*      */ import java.awt.AWTEvent;
/*      */ import java.awt.AWTException;
/*      */ import java.awt.CheckboxMenuItem;
/*      */ import java.awt.Color;
/*      */ import java.awt.Component;
/*      */ import java.awt.Container;
/*      */ import java.awt.Cursor;
/*      */ import java.awt.DefaultKeyboardFocusManager;
/*      */ import java.awt.Dimension;
/*      */ import java.awt.EventQueue;
/*      */ import java.awt.FileDialog;
/*      */ import java.awt.Font;
/*      */ import java.awt.Frame;
/*      */ import java.awt.GraphicsConfiguration;
/*      */ import java.awt.KeyboardFocusManager;
/*      */ import java.awt.Menu;
/*      */ import java.awt.MenuBar;
/*      */ import java.awt.MenuComponent;
/*      */ import java.awt.MenuContainer;
/*      */ import java.awt.MenuItem;
/*      */ import java.awt.MenuShortcut;
/*      */ import java.awt.Point;
/*      */ import java.awt.PopupMenu;
/*      */ import java.awt.Rectangle;
/*      */ import java.awt.ScrollPaneAdjustable;
/*      */ import java.awt.Shape;
/*      */ import java.awt.SystemTray;
/*      */ import java.awt.Toolkit;
/*      */ import java.awt.TrayIcon;
/*      */ import java.awt.Window;
/*      */ import java.awt.event.InputEvent;
/*      */ import java.awt.event.InvocationEvent;
/*      */ import java.awt.event.KeyEvent;
/*      */ import java.awt.geom.Point2D;
/*      */ import java.awt.peer.ComponentPeer;
/*      */ import java.io.File;
/*      */ import java.lang.reflect.InvocationTargetException;
/*      */ import java.security.AccessControlContext;
/*      */ import java.util.ResourceBundle;
/*      */ import java.util.Vector;
/*      */ import javax.accessibility.AccessibleContext;
/*      */ import sun.misc.Unsafe;
/*      */ 
/*      */ public final class AWTAccessor
/*      */ {
/*   56 */   private static final Unsafe unsafe = Unsafe.getUnsafe();
/*      */   private static ComponentAccessor componentAccessor;
/*      */   private static ContainerAccessor containerAccessor;
/*      */   private static WindowAccessor windowAccessor;
/*      */   private static AWTEventAccessor awtEventAccessor;
/*      */   private static InputEventAccessor inputEventAccessor;
/*      */   private static FrameAccessor frameAccessor;
/*      */   private static KeyboardFocusManagerAccessor kfmAccessor;
/*      */   private static MenuComponentAccessor menuComponentAccessor;
/*      */   private static EventQueueAccessor eventQueueAccessor;
/*      */   private static PopupMenuAccessor popupMenuAccessor;
/*      */   private static FileDialogAccessor fileDialogAccessor;
/*      */   private static ScrollPaneAdjustableAccessor scrollPaneAdjustableAccessor;
/*      */   private static CheckboxMenuItemAccessor checkboxMenuItemAccessor;
/*      */   private static CursorAccessor cursorAccessor;
/*      */   private static MenuBarAccessor menuBarAccessor;
/*      */   private static MenuItemAccessor menuItemAccessor;
/*      */   private static MenuAccessor menuAccessor;
/*      */   private static KeyEventAccessor keyEventAccessor;
/*      */   private static ClientPropertyKeyAccessor clientPropertyKeyAccessor;
/*      */   private static SystemTrayAccessor systemTrayAccessor;
/*      */   private static TrayIconAccessor trayIconAccessor;
/*      */   private static DefaultKeyboardFocusManagerAccessor defaultKeyboardFocusManagerAccessor;
/*      */   private static SequencedEventAccessor sequencedEventAccessor;
/*      */   private static InvocationEventAccessor invocationEventAccessor;
/*      */   private static ToolkitAccessor toolkitAccessor;
/*      */   private static AccessibleContextAccessor accessibleContextAccessor;
/*      */ 
/*      */   public static void setComponentAccessor(ComponentAccessor paramComponentAccessor)
/*      */   {
/*  774 */     componentAccessor = paramComponentAccessor;
/*      */   }
/*      */ 
/*      */   public static ComponentAccessor getComponentAccessor()
/*      */   {
/*  781 */     if (componentAccessor == null) {
/*  782 */       unsafe.ensureClassInitialized(Component.class);
/*      */     }
/*      */ 
/*  785 */     return componentAccessor;
/*      */   }
/*      */ 
/*      */   public static void setContainerAccessor(ContainerAccessor paramContainerAccessor)
/*      */   {
/*  792 */     containerAccessor = paramContainerAccessor;
/*      */   }
/*      */ 
/*      */   public static ContainerAccessor getContainerAccessor()
/*      */   {
/*  799 */     if (containerAccessor == null) {
/*  800 */       unsafe.ensureClassInitialized(Container.class);
/*      */     }
/*      */ 
/*  803 */     return containerAccessor;
/*      */   }
/*      */ 
/*      */   public static void setWindowAccessor(WindowAccessor paramWindowAccessor)
/*      */   {
/*  810 */     windowAccessor = paramWindowAccessor;
/*      */   }
/*      */ 
/*      */   public static WindowAccessor getWindowAccessor()
/*      */   {
/*  817 */     if (windowAccessor == null) {
/*  818 */       unsafe.ensureClassInitialized(Window.class);
/*      */     }
/*  820 */     return windowAccessor;
/*      */   }
/*      */ 
/*      */   public static void setAWTEventAccessor(AWTEventAccessor paramAWTEventAccessor)
/*      */   {
/*  827 */     awtEventAccessor = paramAWTEventAccessor;
/*      */   }
/*      */ 
/*      */   public static AWTEventAccessor getAWTEventAccessor()
/*      */   {
/*  834 */     if (awtEventAccessor == null) {
/*  835 */       unsafe.ensureClassInitialized(AWTEvent.class);
/*      */     }
/*  837 */     return awtEventAccessor;
/*      */   }
/*      */ 
/*      */   public static void setInputEventAccessor(InputEventAccessor paramInputEventAccessor)
/*      */   {
/*  844 */     inputEventAccessor = paramInputEventAccessor;
/*      */   }
/*      */ 
/*      */   public static InputEventAccessor getInputEventAccessor()
/*      */   {
/*  851 */     if (inputEventAccessor == null) {
/*  852 */       unsafe.ensureClassInitialized(InputEvent.class);
/*      */     }
/*  854 */     return inputEventAccessor;
/*      */   }
/*      */ 
/*      */   public static void setFrameAccessor(FrameAccessor paramFrameAccessor)
/*      */   {
/*  861 */     frameAccessor = paramFrameAccessor;
/*      */   }
/*      */ 
/*      */   public static FrameAccessor getFrameAccessor()
/*      */   {
/*  868 */     if (frameAccessor == null) {
/*  869 */       unsafe.ensureClassInitialized(Frame.class);
/*      */     }
/*  871 */     return frameAccessor;
/*      */   }
/*      */ 
/*      */   public static void setKeyboardFocusManagerAccessor(KeyboardFocusManagerAccessor paramKeyboardFocusManagerAccessor)
/*      */   {
/*  878 */     kfmAccessor = paramKeyboardFocusManagerAccessor;
/*      */   }
/*      */ 
/*      */   public static KeyboardFocusManagerAccessor getKeyboardFocusManagerAccessor()
/*      */   {
/*  885 */     if (kfmAccessor == null) {
/*  886 */       unsafe.ensureClassInitialized(KeyboardFocusManager.class);
/*      */     }
/*  888 */     return kfmAccessor;
/*      */   }
/*      */ 
/*      */   public static void setMenuComponentAccessor(MenuComponentAccessor paramMenuComponentAccessor)
/*      */   {
/*  895 */     menuComponentAccessor = paramMenuComponentAccessor;
/*      */   }
/*      */ 
/*      */   public static MenuComponentAccessor getMenuComponentAccessor()
/*      */   {
/*  902 */     if (menuComponentAccessor == null) {
/*  903 */       unsafe.ensureClassInitialized(MenuComponent.class);
/*      */     }
/*  905 */     return menuComponentAccessor;
/*      */   }
/*      */ 
/*      */   public static void setEventQueueAccessor(EventQueueAccessor paramEventQueueAccessor)
/*      */   {
/*  912 */     eventQueueAccessor = paramEventQueueAccessor;
/*      */   }
/*      */ 
/*      */   public static EventQueueAccessor getEventQueueAccessor()
/*      */   {
/*  919 */     if (eventQueueAccessor == null) {
/*  920 */       unsafe.ensureClassInitialized(EventQueue.class);
/*      */     }
/*  922 */     return eventQueueAccessor;
/*      */   }
/*      */ 
/*      */   public static void setPopupMenuAccessor(PopupMenuAccessor paramPopupMenuAccessor)
/*      */   {
/*  929 */     popupMenuAccessor = paramPopupMenuAccessor;
/*      */   }
/*      */ 
/*      */   public static PopupMenuAccessor getPopupMenuAccessor()
/*      */   {
/*  936 */     if (popupMenuAccessor == null) {
/*  937 */       unsafe.ensureClassInitialized(PopupMenu.class);
/*      */     }
/*  939 */     return popupMenuAccessor;
/*      */   }
/*      */ 
/*      */   public static void setFileDialogAccessor(FileDialogAccessor paramFileDialogAccessor)
/*      */   {
/*  946 */     fileDialogAccessor = paramFileDialogAccessor;
/*      */   }
/*      */ 
/*      */   public static FileDialogAccessor getFileDialogAccessor()
/*      */   {
/*  953 */     if (fileDialogAccessor == null) {
/*  954 */       unsafe.ensureClassInitialized(FileDialog.class);
/*      */     }
/*  956 */     return fileDialogAccessor;
/*      */   }
/*      */ 
/*      */   public static void setScrollPaneAdjustableAccessor(ScrollPaneAdjustableAccessor paramScrollPaneAdjustableAccessor)
/*      */   {
/*  963 */     scrollPaneAdjustableAccessor = paramScrollPaneAdjustableAccessor;
/*      */   }
/*      */ 
/*      */   public static ScrollPaneAdjustableAccessor getScrollPaneAdjustableAccessor()
/*      */   {
/*  971 */     if (scrollPaneAdjustableAccessor == null) {
/*  972 */       unsafe.ensureClassInitialized(ScrollPaneAdjustable.class);
/*      */     }
/*  974 */     return scrollPaneAdjustableAccessor;
/*      */   }
/*      */ 
/*      */   public static void setCheckboxMenuItemAccessor(CheckboxMenuItemAccessor paramCheckboxMenuItemAccessor)
/*      */   {
/*  981 */     checkboxMenuItemAccessor = paramCheckboxMenuItemAccessor;
/*      */   }
/*      */ 
/*      */   public static CheckboxMenuItemAccessor getCheckboxMenuItemAccessor()
/*      */   {
/*  988 */     if (checkboxMenuItemAccessor == null) {
/*  989 */       unsafe.ensureClassInitialized(CheckboxMenuItemAccessor.class);
/*      */     }
/*  991 */     return checkboxMenuItemAccessor;
/*      */   }
/*      */ 
/*      */   public static void setCursorAccessor(CursorAccessor paramCursorAccessor)
/*      */   {
/*  998 */     cursorAccessor = paramCursorAccessor;
/*      */   }
/*      */ 
/*      */   public static CursorAccessor getCursorAccessor()
/*      */   {
/* 1005 */     if (cursorAccessor == null) {
/* 1006 */       unsafe.ensureClassInitialized(CursorAccessor.class);
/*      */     }
/* 1008 */     return cursorAccessor;
/*      */   }
/*      */ 
/*      */   public static void setMenuBarAccessor(MenuBarAccessor paramMenuBarAccessor)
/*      */   {
/* 1015 */     menuBarAccessor = paramMenuBarAccessor;
/*      */   }
/*      */ 
/*      */   public static MenuBarAccessor getMenuBarAccessor()
/*      */   {
/* 1022 */     if (menuBarAccessor == null) {
/* 1023 */       unsafe.ensureClassInitialized(MenuBarAccessor.class);
/*      */     }
/* 1025 */     return menuBarAccessor;
/*      */   }
/*      */ 
/*      */   public static void setMenuItemAccessor(MenuItemAccessor paramMenuItemAccessor)
/*      */   {
/* 1032 */     menuItemAccessor = paramMenuItemAccessor;
/*      */   }
/*      */ 
/*      */   public static MenuItemAccessor getMenuItemAccessor()
/*      */   {
/* 1039 */     if (menuItemAccessor == null) {
/* 1040 */       unsafe.ensureClassInitialized(MenuItemAccessor.class);
/*      */     }
/* 1042 */     return menuItemAccessor;
/*      */   }
/*      */ 
/*      */   public static void setMenuAccessor(MenuAccessor paramMenuAccessor)
/*      */   {
/* 1049 */     menuAccessor = paramMenuAccessor;
/*      */   }
/*      */ 
/*      */   public static MenuAccessor getMenuAccessor()
/*      */   {
/* 1056 */     if (menuAccessor == null) {
/* 1057 */       unsafe.ensureClassInitialized(MenuAccessor.class);
/*      */     }
/* 1059 */     return menuAccessor;
/*      */   }
/*      */ 
/*      */   public static void setKeyEventAccessor(KeyEventAccessor paramKeyEventAccessor)
/*      */   {
/* 1066 */     keyEventAccessor = paramKeyEventAccessor;
/*      */   }
/*      */ 
/*      */   public static KeyEventAccessor getKeyEventAccessor()
/*      */   {
/* 1073 */     if (keyEventAccessor == null) {
/* 1074 */       unsafe.ensureClassInitialized(KeyEventAccessor.class);
/*      */     }
/* 1076 */     return keyEventAccessor;
/*      */   }
/*      */ 
/*      */   public static void setClientPropertyKeyAccessor(ClientPropertyKeyAccessor paramClientPropertyKeyAccessor)
/*      */   {
/* 1083 */     clientPropertyKeyAccessor = paramClientPropertyKeyAccessor;
/*      */   }
/*      */ 
/*      */   public static ClientPropertyKeyAccessor getClientPropertyKeyAccessor()
/*      */   {
/* 1090 */     if (clientPropertyKeyAccessor == null) {
/* 1091 */       unsafe.ensureClassInitialized(ClientPropertyKeyAccessor.class);
/*      */     }
/* 1093 */     return clientPropertyKeyAccessor;
/*      */   }
/*      */ 
/*      */   public static void setSystemTrayAccessor(SystemTrayAccessor paramSystemTrayAccessor)
/*      */   {
/* 1100 */     systemTrayAccessor = paramSystemTrayAccessor;
/*      */   }
/*      */ 
/*      */   public static SystemTrayAccessor getSystemTrayAccessor()
/*      */   {
/* 1107 */     if (systemTrayAccessor == null) {
/* 1108 */       unsafe.ensureClassInitialized(SystemTrayAccessor.class);
/*      */     }
/* 1110 */     return systemTrayAccessor;
/*      */   }
/*      */ 
/*      */   public static void setTrayIconAccessor(TrayIconAccessor paramTrayIconAccessor)
/*      */   {
/* 1117 */     trayIconAccessor = paramTrayIconAccessor;
/*      */   }
/*      */ 
/*      */   public static TrayIconAccessor getTrayIconAccessor()
/*      */   {
/* 1124 */     if (trayIconAccessor == null) {
/* 1125 */       unsafe.ensureClassInitialized(TrayIconAccessor.class);
/*      */     }
/* 1127 */     return trayIconAccessor;
/*      */   }
/*      */ 
/*      */   public static void setDefaultKeyboardFocusManagerAccessor(DefaultKeyboardFocusManagerAccessor paramDefaultKeyboardFocusManagerAccessor)
/*      */   {
/* 1134 */     defaultKeyboardFocusManagerAccessor = paramDefaultKeyboardFocusManagerAccessor;
/*      */   }
/*      */ 
/*      */   public static DefaultKeyboardFocusManagerAccessor getDefaultKeyboardFocusManagerAccessor()
/*      */   {
/* 1141 */     if (defaultKeyboardFocusManagerAccessor == null) {
/* 1142 */       unsafe.ensureClassInitialized(DefaultKeyboardFocusManagerAccessor.class);
/*      */     }
/* 1144 */     return defaultKeyboardFocusManagerAccessor;
/*      */   }
/*      */ 
/*      */   public static void setSequencedEventAccessor(SequencedEventAccessor paramSequencedEventAccessor)
/*      */   {
/* 1151 */     sequencedEventAccessor = paramSequencedEventAccessor;
/*      */   }
/*      */ 
/*      */   public static SequencedEventAccessor getSequencedEventAccessor()
/*      */   {
/* 1161 */     return sequencedEventAccessor;
/*      */   }
/*      */ 
/*      */   public static void setToolkitAccessor(ToolkitAccessor paramToolkitAccessor)
/*      */   {
/* 1168 */     toolkitAccessor = paramToolkitAccessor;
/*      */   }
/*      */ 
/*      */   public static ToolkitAccessor getToolkitAccessor()
/*      */   {
/* 1175 */     if (toolkitAccessor == null) {
/* 1176 */       unsafe.ensureClassInitialized(Toolkit.class);
/*      */     }
/*      */ 
/* 1179 */     return toolkitAccessor;
/*      */   }
/*      */ 
/*      */   public static void setInvocationEventAccessor(InvocationEventAccessor paramInvocationEventAccessor)
/*      */   {
/* 1186 */     invocationEventAccessor = paramInvocationEventAccessor;
/*      */   }
/*      */ 
/*      */   public static InvocationEventAccessor getInvocationEventAccessor()
/*      */   {
/* 1193 */     return invocationEventAccessor;
/*      */   }
/*      */ 
/*      */   public static AccessibleContextAccessor getAccessibleContextAccessor()
/*      */   {
/* 1201 */     if (accessibleContextAccessor == null) {
/* 1202 */       unsafe.ensureClassInitialized(AccessibleContext.class);
/*      */     }
/* 1204 */     return accessibleContextAccessor;
/*      */   }
/*      */ 
/*      */   public static void setAccessibleContextAccessor(AccessibleContextAccessor paramAccessibleContextAccessor)
/*      */   {
/* 1211 */     accessibleContextAccessor = paramAccessibleContextAccessor;
/*      */   }
/*      */ 
/*      */   public static abstract interface AWTEventAccessor
/*      */   {
/*      */     public abstract void setPosted(AWTEvent paramAWTEvent);
/*      */ 
/*      */     public abstract void setSystemGenerated(AWTEvent paramAWTEvent);
/*      */ 
/*      */     public abstract boolean isSystemGenerated(AWTEvent paramAWTEvent);
/*      */ 
/*      */     public abstract AccessControlContext getAccessControlContext(AWTEvent paramAWTEvent);
/*      */ 
/*      */     public abstract byte[] getBData(AWTEvent paramAWTEvent);
/*      */ 
/*      */     public abstract void setBData(AWTEvent paramAWTEvent, byte[] paramArrayOfByte);
/*      */   }
/*      */ 
/*      */   public static abstract interface AccessibleContextAccessor
/*      */   {
/*      */     public abstract void setAppContext(AccessibleContext paramAccessibleContext, AppContext paramAppContext);
/*      */ 
/*      */     public abstract AppContext getAppContext(AccessibleContext paramAccessibleContext);
/*      */   }
/*      */ 
/*      */   public static abstract interface CheckboxMenuItemAccessor
/*      */   {
/*      */     public abstract boolean getState(CheckboxMenuItem paramCheckboxMenuItem);
/*      */   }
/*      */ 
/*      */   public static abstract interface ClientPropertyKeyAccessor
/*      */   {
/*      */     public abstract Object getJComponent_TRANSFER_HANDLER();
/*      */   }
/*      */ 
/*      */   public static abstract interface ComponentAccessor
/*      */   {
/*      */     public abstract void setBackgroundEraseDisabled(Component paramComponent, boolean paramBoolean);
/*      */ 
/*      */     public abstract boolean getBackgroundEraseDisabled(Component paramComponent);
/*      */ 
/*      */     public abstract Rectangle getBounds(Component paramComponent);
/*      */ 
/*      */     public abstract void setMixingCutoutShape(Component paramComponent, Shape paramShape);
/*      */ 
/*      */     public abstract void setGraphicsConfiguration(Component paramComponent, GraphicsConfiguration paramGraphicsConfiguration);
/*      */ 
/*      */     public abstract boolean requestFocus(Component paramComponent, CausedFocusEvent.Cause paramCause);
/*      */ 
/*      */     public abstract boolean canBeFocusOwner(Component paramComponent);
/*      */ 
/*      */     public abstract boolean isVisible(Component paramComponent);
/*      */ 
/*      */     public abstract void setRequestFocusController(RequestFocusController paramRequestFocusController);
/*      */ 
/*      */     public abstract AppContext getAppContext(Component paramComponent);
/*      */ 
/*      */     public abstract void setAppContext(Component paramComponent, AppContext paramAppContext);
/*      */ 
/*      */     public abstract Container getParent(Component paramComponent);
/*      */ 
/*      */     public abstract void setParent(Component paramComponent, Container paramContainer);
/*      */ 
/*      */     public abstract void setSize(Component paramComponent, int paramInt1, int paramInt2);
/*      */ 
/*      */     public abstract Point getLocation(Component paramComponent);
/*      */ 
/*      */     public abstract void setLocation(Component paramComponent, int paramInt1, int paramInt2);
/*      */ 
/*      */     public abstract boolean isEnabled(Component paramComponent);
/*      */ 
/*      */     public abstract boolean isDisplayable(Component paramComponent);
/*      */ 
/*      */     public abstract Cursor getCursor(Component paramComponent);
/*      */ 
/*      */     public abstract ComponentPeer getPeer(Component paramComponent);
/*      */ 
/*      */     public abstract void setPeer(Component paramComponent, ComponentPeer paramComponentPeer);
/*      */ 
/*      */     public abstract boolean isLightweight(Component paramComponent);
/*      */ 
/*      */     public abstract boolean getIgnoreRepaint(Component paramComponent);
/*      */ 
/*      */     public abstract int getWidth(Component paramComponent);
/*      */ 
/*      */     public abstract int getHeight(Component paramComponent);
/*      */ 
/*      */     public abstract int getX(Component paramComponent);
/*      */ 
/*      */     public abstract int getY(Component paramComponent);
/*      */ 
/*      */     public abstract Color getForeground(Component paramComponent);
/*      */ 
/*      */     public abstract Color getBackground(Component paramComponent);
/*      */ 
/*      */     public abstract void setBackground(Component paramComponent, Color paramColor);
/*      */ 
/*      */     public abstract Font getFont(Component paramComponent);
/*      */ 
/*      */     public abstract void processEvent(Component paramComponent, AWTEvent paramAWTEvent);
/*      */ 
/*      */     public abstract AccessControlContext getAccessControlContext(Component paramComponent);
/*      */ 
/*      */     public abstract void revalidateSynchronously(Component paramComponent);
/*      */   }
/*      */ 
/*      */   public static abstract interface ContainerAccessor
/*      */   {
/*      */     public abstract void validateUnconditionally(Container paramContainer);
/*      */   }
/*      */ 
/*      */   public static abstract interface CursorAccessor
/*      */   {
/*      */     public abstract long getPData(Cursor paramCursor);
/*      */ 
/*      */     public abstract void setPData(Cursor paramCursor, long paramLong);
/*      */ 
/*      */     public abstract int getType(Cursor paramCursor);
/*      */   }
/*      */ 
/*      */   public static abstract interface DefaultKeyboardFocusManagerAccessor
/*      */   {
/*      */     public abstract void consumeNextKeyTyped(DefaultKeyboardFocusManager paramDefaultKeyboardFocusManager, KeyEvent paramKeyEvent);
/*      */   }
/*      */ 
/*      */   public static abstract interface EventQueueAccessor
/*      */   {
/*      */     public abstract Thread getDispatchThread(EventQueue paramEventQueue);
/*      */ 
/*      */     public abstract boolean isDispatchThreadImpl(EventQueue paramEventQueue);
/*      */ 
/*      */     public abstract void removeSourceEvents(EventQueue paramEventQueue, Object paramObject, boolean paramBoolean);
/*      */ 
/*      */     public abstract boolean noEvents(EventQueue paramEventQueue);
/*      */ 
/*      */     public abstract void wakeup(EventQueue paramEventQueue, boolean paramBoolean);
/*      */ 
/*      */     public abstract void invokeAndWait(Object paramObject, Runnable paramRunnable)
/*      */       throws InterruptedException, InvocationTargetException;
/*      */ 
/*      */     public abstract long getMostRecentEventTime(EventQueue paramEventQueue);
/*      */   }
/*      */ 
/*      */   public static abstract interface FileDialogAccessor
/*      */   {
/*      */     public abstract void setFiles(FileDialog paramFileDialog, File[] paramArrayOfFile);
/*      */ 
/*      */     public abstract void setFile(FileDialog paramFileDialog, String paramString);
/*      */ 
/*      */     public abstract void setDirectory(FileDialog paramFileDialog, String paramString);
/*      */ 
/*      */     public abstract boolean isMultipleMode(FileDialog paramFileDialog);
/*      */   }
/*      */ 
/*      */   public static abstract interface FrameAccessor
/*      */   {
/*      */     public abstract void setExtendedState(Frame paramFrame, int paramInt);
/*      */ 
/*      */     public abstract int getExtendedState(Frame paramFrame);
/*      */ 
/*      */     public abstract Rectangle getMaximizedBounds(Frame paramFrame);
/*      */   }
/*      */ 
/*      */   public static abstract interface InputEventAccessor
/*      */   {
/*      */     public abstract int[] getButtonDownMasks();
/*      */   }
/*      */ 
/*      */   public static abstract interface InvocationEventAccessor
/*      */   {
/*      */     public abstract void dispose(InvocationEvent paramInvocationEvent);
/*      */ 
/*      */     public abstract InvocationEvent createEvent(Object paramObject, Runnable paramRunnable1, Runnable paramRunnable2, boolean paramBoolean);
/*      */   }
/*      */ 
/*      */   public static abstract interface KeyEventAccessor
/*      */   {
/*      */     public abstract void setRawCode(KeyEvent paramKeyEvent, long paramLong);
/*      */ 
/*      */     public abstract void setPrimaryLevelUnicode(KeyEvent paramKeyEvent, long paramLong);
/*      */ 
/*      */     public abstract void setExtendedKeyCode(KeyEvent paramKeyEvent, long paramLong);
/*      */   }
/*      */ 
/*      */   public static abstract interface KeyboardFocusManagerAccessor
/*      */   {
/*      */     public abstract int shouldNativelyFocusHeavyweight(Component paramComponent1, Component paramComponent2, boolean paramBoolean1, boolean paramBoolean2, long paramLong, CausedFocusEvent.Cause paramCause);
/*      */ 
/*      */     public abstract boolean processSynchronousLightweightTransfer(Component paramComponent1, Component paramComponent2, boolean paramBoolean1, boolean paramBoolean2, long paramLong);
/*      */ 
/*      */     public abstract void removeLastFocusRequest(Component paramComponent);
/*      */ 
/*      */     public abstract void setMostRecentFocusOwner(Window paramWindow, Component paramComponent);
/*      */ 
/*      */     public abstract KeyboardFocusManager getCurrentKeyboardFocusManager(AppContext paramAppContext);
/*      */ 
/*      */     public abstract Container getCurrentFocusCycleRoot();
/*      */   }
/*      */ 
/*      */   public static abstract interface MenuAccessor
/*      */   {
/*      */     public abstract Vector getItems(Menu paramMenu);
/*      */   }
/*      */ 
/*      */   public static abstract interface MenuBarAccessor
/*      */   {
/*      */     public abstract Menu getHelpMenu(MenuBar paramMenuBar);
/*      */ 
/*      */     public abstract Vector getMenus(MenuBar paramMenuBar);
/*      */   }
/*      */ 
/*      */   public static abstract interface MenuComponentAccessor
/*      */   {
/*      */     public abstract AppContext getAppContext(MenuComponent paramMenuComponent);
/*      */ 
/*      */     public abstract void setAppContext(MenuComponent paramMenuComponent, AppContext paramAppContext);
/*      */ 
/*      */     public abstract MenuContainer getParent(MenuComponent paramMenuComponent);
/*      */ 
/*      */     public abstract Font getFont_NoClientCode(MenuComponent paramMenuComponent);
/*      */   }
/*      */ 
/*      */   public static abstract interface MenuItemAccessor
/*      */   {
/*      */     public abstract boolean isEnabled(MenuItem paramMenuItem);
/*      */ 
/*      */     public abstract String getActionCommandImpl(MenuItem paramMenuItem);
/*      */ 
/*      */     public abstract boolean isItemEnabled(MenuItem paramMenuItem);
/*      */ 
/*      */     public abstract String getLabel(MenuItem paramMenuItem);
/*      */ 
/*      */     public abstract MenuShortcut getShortcut(MenuItem paramMenuItem);
/*      */   }
/*      */ 
/*      */   public static abstract interface PopupMenuAccessor
/*      */   {
/*      */     public abstract boolean isTrayIconPopup(PopupMenu paramPopupMenu);
/*      */   }
/*      */ 
/*      */   public static abstract interface ScrollPaneAdjustableAccessor
/*      */   {
/*      */     public abstract void setTypedValue(ScrollPaneAdjustable paramScrollPaneAdjustable, int paramInt1, int paramInt2);
/*      */   }
/*      */ 
/*      */   public static abstract interface SequencedEventAccessor
/*      */   {
/*      */     public abstract AWTEvent getNested(AWTEvent paramAWTEvent);
/*      */ 
/*      */     public abstract boolean isSequencedEvent(AWTEvent paramAWTEvent);
/*      */   }
/*      */ 
/*      */   public static abstract interface SystemTrayAccessor
/*      */   {
/*      */     public abstract void firePropertyChange(SystemTray paramSystemTray, String paramString, Object paramObject1, Object paramObject2);
/*      */   }
/*      */ 
/*      */   public static abstract interface ToolkitAccessor
/*      */   {
/*      */     public abstract void setPlatformResources(ResourceBundle paramResourceBundle);
/*      */   }
/*      */ 
/*      */   public static abstract interface TrayIconAccessor
/*      */   {
/*      */     public abstract void addNotify(TrayIcon paramTrayIcon)
/*      */       throws AWTException;
/*      */ 
/*      */     public abstract void removeNotify(TrayIcon paramTrayIcon);
/*      */   }
/*      */ 
/*      */   public static abstract interface WindowAccessor
/*      */   {
/*      */     public abstract float getOpacity(Window paramWindow);
/*      */ 
/*      */     public abstract void setOpacity(Window paramWindow, float paramFloat);
/*      */ 
/*      */     public abstract Shape getShape(Window paramWindow);
/*      */ 
/*      */     public abstract void setShape(Window paramWindow, Shape paramShape);
/*      */ 
/*      */     public abstract void setOpaque(Window paramWindow, boolean paramBoolean);
/*      */ 
/*      */     public abstract void updateWindow(Window paramWindow);
/*      */ 
/*      */     public abstract Dimension getSecurityWarningSize(Window paramWindow);
/*      */ 
/*      */     public abstract void setSecurityWarningSize(Window paramWindow, int paramInt1, int paramInt2);
/*      */ 
/*      */     public abstract void setSecurityWarningPosition(Window paramWindow, Point2D paramPoint2D, float paramFloat1, float paramFloat2);
/*      */ 
/*      */     public abstract Point2D calculateSecurityWarningPosition(Window paramWindow, double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4);
/*      */ 
/*      */     public abstract void setLWRequestStatus(Window paramWindow, boolean paramBoolean);
/*      */ 
/*      */     public abstract boolean isAutoRequestFocus(Window paramWindow);
/*      */ 
/*      */     public abstract boolean isTrayIconWindow(Window paramWindow);
/*      */ 
/*      */     public abstract void setTrayIconWindow(Window paramWindow, boolean paramBoolean);
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.AWTAccessor
 * JD-Core Version:    0.6.2
 */