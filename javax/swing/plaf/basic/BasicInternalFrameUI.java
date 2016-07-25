/*      */ package javax.swing.plaf.basic;
/*      */ 
/*      */ import java.awt.Color;
/*      */ import java.awt.Component;
/*      */ import java.awt.Container;
/*      */ import java.awt.Cursor;
/*      */ import java.awt.Dimension;
/*      */ import java.awt.IllegalComponentStateException;
/*      */ import java.awt.Insets;
/*      */ import java.awt.LayoutManager;
/*      */ import java.awt.Point;
/*      */ import java.awt.Rectangle;
/*      */ import java.awt.Window;
/*      */ import java.awt.event.ActionEvent;
/*      */ import java.awt.event.ComponentEvent;
/*      */ import java.awt.event.ComponentListener;
/*      */ import java.awt.event.MouseEvent;
/*      */ import java.awt.event.WindowEvent;
/*      */ import java.awt.event.WindowFocusListener;
/*      */ import java.beans.PropertyChangeEvent;
/*      */ import java.beans.PropertyChangeListener;
/*      */ import java.beans.PropertyVetoException;
/*      */ import javax.swing.DefaultDesktopManager;
/*      */ import javax.swing.DesktopManager;
/*      */ import javax.swing.Icon;
/*      */ import javax.swing.InputMap;
/*      */ import javax.swing.JComponent;
/*      */ import javax.swing.JDesktopPane;
/*      */ import javax.swing.JInternalFrame;
/*      */ import javax.swing.JInternalFrame.JDesktopIcon;
/*      */ import javax.swing.JRootPane;
/*      */ import javax.swing.KeyStroke;
/*      */ import javax.swing.LookAndFeel;
/*      */ import javax.swing.RootPaneContainer;
/*      */ import javax.swing.SwingConstants;
/*      */ import javax.swing.SwingUtilities;
/*      */ import javax.swing.UIDefaults;
/*      */ import javax.swing.UIManager;
/*      */ import javax.swing.event.InternalFrameEvent;
/*      */ import javax.swing.event.InternalFrameListener;
/*      */ import javax.swing.event.MouseInputAdapter;
/*      */ import javax.swing.event.MouseInputListener;
/*      */ import javax.swing.plaf.ComponentUI;
/*      */ import javax.swing.plaf.InternalFrameUI;
/*      */ import javax.swing.plaf.UIResource;
/*      */ import sun.swing.DefaultLookup;
/*      */ import sun.swing.UIAction;
/*      */ 
/*      */ public class BasicInternalFrameUI extends InternalFrameUI
/*      */ {
/*      */   protected JInternalFrame frame;
/*      */   private Handler handler;
/*      */   protected MouseInputAdapter borderListener;
/*      */   protected PropertyChangeListener propertyChangeListener;
/*      */   protected LayoutManager internalFrameLayout;
/*      */   protected ComponentListener componentListener;
/*      */   protected MouseInputListener glassPaneDispatcher;
/*      */   private InternalFrameListener internalFrameListener;
/*      */   protected JComponent northPane;
/*      */   protected JComponent southPane;
/*      */   protected JComponent westPane;
/*      */   protected JComponent eastPane;
/*      */   protected BasicInternalFrameTitlePane titlePane;
/*      */   private static DesktopManager sharedDesktopManager;
/*   64 */   private boolean componentListenerAdded = false;
/*      */   private Rectangle parentBounds;
/*   68 */   private boolean dragging = false;
/*   69 */   private boolean resizing = false;
/*      */ 
/*      */   @Deprecated
/*      */   protected KeyStroke openMenuKey;
/*   82 */   private boolean keyBindingRegistered = false;
/*   83 */   private boolean keyBindingActive = false;
/*      */ 
/*      */   public static ComponentUI createUI(JComponent paramJComponent)
/*      */   {
/*   89 */     return new BasicInternalFrameUI((JInternalFrame)paramJComponent);
/*      */   }
/*      */ 
/*      */   public BasicInternalFrameUI(JInternalFrame paramJInternalFrame) {
/*   93 */     LookAndFeel localLookAndFeel = UIManager.getLookAndFeel();
/*   94 */     if ((localLookAndFeel instanceof BasicLookAndFeel))
/*   95 */       ((BasicLookAndFeel)localLookAndFeel).installAWTEventListener();
/*      */   }
/*      */ 
/*      */   public void installUI(JComponent paramJComponent)
/*      */   {
/*  101 */     this.frame = ((JInternalFrame)paramJComponent);
/*      */ 
/*  103 */     installDefaults();
/*  104 */     installListeners();
/*  105 */     installComponents();
/*  106 */     installKeyboardActions();
/*      */ 
/*  108 */     LookAndFeel.installProperty(this.frame, "opaque", Boolean.TRUE);
/*      */   }
/*      */ 
/*      */   public void uninstallUI(JComponent paramJComponent) {
/*  112 */     if (paramJComponent != this.frame) {
/*  113 */       throw new IllegalComponentStateException(this + " was asked to deinstall() " + paramJComponent + " when it only knows about " + this.frame + ".");
/*      */     }
/*      */ 
/*  118 */     uninstallKeyboardActions();
/*  119 */     uninstallComponents();
/*  120 */     uninstallListeners();
/*  121 */     uninstallDefaults();
/*  122 */     updateFrameCursor();
/*  123 */     this.handler = null;
/*  124 */     this.frame = null;
/*      */   }
/*      */ 
/*      */   protected void installDefaults() {
/*  128 */     Icon localIcon = this.frame.getFrameIcon();
/*  129 */     if ((localIcon == null) || ((localIcon instanceof UIResource))) {
/*  130 */       this.frame.setFrameIcon(UIManager.getIcon("InternalFrame.icon"));
/*      */     }
/*      */ 
/*  135 */     Container localContainer = this.frame.getContentPane();
/*  136 */     if (localContainer != null) {
/*  137 */       Color localColor = localContainer.getBackground();
/*  138 */       if ((localColor instanceof UIResource))
/*  139 */         localContainer.setBackground(null);
/*      */     }
/*  141 */     this.frame.setLayout(this.internalFrameLayout = createLayoutManager());
/*  142 */     this.frame.setBackground(UIManager.getLookAndFeelDefaults().getColor("control"));
/*      */ 
/*  144 */     LookAndFeel.installBorder(this.frame, "InternalFrame.border");
/*      */   }
/*      */ 
/*      */   protected void installKeyboardActions() {
/*  148 */     createInternalFrameListener();
/*  149 */     if (this.internalFrameListener != null) {
/*  150 */       this.frame.addInternalFrameListener(this.internalFrameListener);
/*      */     }
/*      */ 
/*  153 */     LazyActionMap.installLazyActionMap(this.frame, BasicInternalFrameUI.class, "InternalFrame.actionMap");
/*      */   }
/*      */ 
/*      */   static void loadActionMap(LazyActionMap paramLazyActionMap)
/*      */   {
/*  158 */     paramLazyActionMap.put(new UIAction("showSystemMenu") {
/*      */       public void actionPerformed(ActionEvent paramAnonymousActionEvent) {
/*  160 */         JInternalFrame localJInternalFrame = (JInternalFrame)paramAnonymousActionEvent.getSource();
/*  161 */         if ((localJInternalFrame.getUI() instanceof BasicInternalFrameUI)) {
/*  162 */           JComponent localJComponent = ((BasicInternalFrameUI)localJInternalFrame.getUI()).getNorthPane();
/*      */ 
/*  164 */           if ((localJComponent instanceof BasicInternalFrameTitlePane))
/*  165 */             ((BasicInternalFrameTitlePane)localJComponent).showSystemMenu();
/*      */         }
/*      */       }
/*      */ 
/*      */       public boolean isEnabled(Object paramAnonymousObject)
/*      */       {
/*  172 */         if ((paramAnonymousObject instanceof JInternalFrame)) {
/*  173 */           JInternalFrame localJInternalFrame = (JInternalFrame)paramAnonymousObject;
/*  174 */           if ((localJInternalFrame.getUI() instanceof BasicInternalFrameUI)) {
/*  175 */             return ((BasicInternalFrameUI)localJInternalFrame.getUI()).isKeyBindingActive();
/*      */           }
/*      */         }
/*      */ 
/*  179 */         return false;
/*      */       }
/*      */     });
/*  184 */     BasicLookAndFeel.installAudioActionMap(paramLazyActionMap);
/*      */   }
/*      */ 
/*      */   protected void installComponents() {
/*  188 */     setNorthPane(createNorthPane(this.frame));
/*  189 */     setSouthPane(createSouthPane(this.frame));
/*  190 */     setEastPane(createEastPane(this.frame));
/*  191 */     setWestPane(createWestPane(this.frame));
/*      */   }
/*      */ 
/*      */   protected void installListeners()
/*      */   {
/*  198 */     this.borderListener = createBorderListener(this.frame);
/*  199 */     this.propertyChangeListener = createPropertyChangeListener();
/*  200 */     this.frame.addPropertyChangeListener(this.propertyChangeListener);
/*  201 */     installMouseHandlers(this.frame);
/*  202 */     this.glassPaneDispatcher = createGlassPaneDispatcher();
/*  203 */     if (this.glassPaneDispatcher != null) {
/*  204 */       this.frame.getGlassPane().addMouseListener(this.glassPaneDispatcher);
/*  205 */       this.frame.getGlassPane().addMouseMotionListener(this.glassPaneDispatcher);
/*      */     }
/*  207 */     this.componentListener = createComponentListener();
/*  208 */     if (this.frame.getParent() != null) {
/*  209 */       this.parentBounds = this.frame.getParent().getBounds();
/*      */     }
/*  211 */     if ((this.frame.getParent() != null) && (!this.componentListenerAdded)) {
/*  212 */       this.frame.getParent().addComponentListener(this.componentListener);
/*  213 */       this.componentListenerAdded = true;
/*      */     }
/*      */   }
/*      */ 
/*      */   private WindowFocusListener getWindowFocusListener()
/*      */   {
/*  222 */     return getHandler();
/*      */   }
/*      */ 
/*      */   private void cancelResize()
/*      */   {
/*  227 */     if ((this.resizing) && 
/*  228 */       ((this.borderListener instanceof BorderListener)))
/*  229 */       ((BorderListener)this.borderListener).finishMouseReleased();
/*      */   }
/*      */ 
/*      */   private Handler getHandler()
/*      */   {
/*  235 */     if (this.handler == null) {
/*  236 */       this.handler = new Handler(null);
/*      */     }
/*  238 */     return this.handler;
/*      */   }
/*      */ 
/*      */   InputMap getInputMap(int paramInt) {
/*  242 */     if (paramInt == 2) {
/*  243 */       return createInputMap(paramInt);
/*      */     }
/*  245 */     return null;
/*      */   }
/*      */ 
/*      */   InputMap createInputMap(int paramInt) {
/*  249 */     if (paramInt == 2) {
/*  250 */       Object[] arrayOfObject = (Object[])DefaultLookup.get(this.frame, this, "InternalFrame.windowBindings");
/*      */ 
/*  253 */       if (arrayOfObject != null) {
/*  254 */         return LookAndFeel.makeComponentInputMap(this.frame, arrayOfObject);
/*      */       }
/*      */     }
/*  257 */     return null;
/*      */   }
/*      */ 
/*      */   protected void uninstallDefaults() {
/*  261 */     Icon localIcon = this.frame.getFrameIcon();
/*  262 */     if ((localIcon instanceof UIResource)) {
/*  263 */       this.frame.setFrameIcon(null);
/*      */     }
/*  265 */     this.internalFrameLayout = null;
/*  266 */     this.frame.setLayout(null);
/*  267 */     LookAndFeel.uninstallBorder(this.frame);
/*      */   }
/*      */ 
/*      */   protected void uninstallComponents() {
/*  271 */     setNorthPane(null);
/*  272 */     setSouthPane(null);
/*  273 */     setEastPane(null);
/*  274 */     setWestPane(null);
/*  275 */     if (this.titlePane != null) {
/*  276 */       this.titlePane.uninstallDefaults();
/*      */     }
/*  278 */     this.titlePane = null;
/*      */   }
/*      */ 
/*      */   protected void uninstallListeners()
/*      */   {
/*  285 */     if ((this.frame.getParent() != null) && (this.componentListenerAdded)) {
/*  286 */       this.frame.getParent().removeComponentListener(this.componentListener);
/*  287 */       this.componentListenerAdded = false;
/*      */     }
/*  289 */     this.componentListener = null;
/*  290 */     if (this.glassPaneDispatcher != null) {
/*  291 */       this.frame.getGlassPane().removeMouseListener(this.glassPaneDispatcher);
/*  292 */       this.frame.getGlassPane().removeMouseMotionListener(this.glassPaneDispatcher);
/*  293 */       this.glassPaneDispatcher = null;
/*      */     }
/*  295 */     deinstallMouseHandlers(this.frame);
/*  296 */     this.frame.removePropertyChangeListener(this.propertyChangeListener);
/*  297 */     this.propertyChangeListener = null;
/*  298 */     this.borderListener = null;
/*      */   }
/*      */ 
/*      */   protected void uninstallKeyboardActions() {
/*  302 */     if (this.internalFrameListener != null) {
/*  303 */       this.frame.removeInternalFrameListener(this.internalFrameListener);
/*      */     }
/*  305 */     this.internalFrameListener = null;
/*      */ 
/*  307 */     SwingUtilities.replaceUIInputMap(this.frame, 2, null);
/*      */ 
/*  309 */     SwingUtilities.replaceUIActionMap(this.frame, null);
/*      */   }
/*      */ 
/*      */   void updateFrameCursor()
/*      */   {
/*  314 */     if (this.resizing) {
/*  315 */       return;
/*      */     }
/*  317 */     Cursor localCursor = this.frame.getLastCursor();
/*  318 */     if (localCursor == null) {
/*  319 */       localCursor = Cursor.getPredefinedCursor(0);
/*      */     }
/*  321 */     this.frame.setCursor(localCursor);
/*      */   }
/*      */ 
/*      */   protected LayoutManager createLayoutManager() {
/*  325 */     return getHandler();
/*      */   }
/*      */ 
/*      */   protected PropertyChangeListener createPropertyChangeListener() {
/*  329 */     return getHandler();
/*      */   }
/*      */ 
/*      */   public Dimension getPreferredSize(JComponent paramJComponent)
/*      */   {
/*  335 */     if (this.frame == paramJComponent)
/*  336 */       return this.frame.getLayout().preferredLayoutSize(paramJComponent);
/*  337 */     return new Dimension(100, 100);
/*      */   }
/*      */ 
/*      */   public Dimension getMinimumSize(JComponent paramJComponent) {
/*  341 */     if (this.frame == paramJComponent) {
/*  342 */       return this.frame.getLayout().minimumLayoutSize(paramJComponent);
/*      */     }
/*  344 */     return new Dimension(0, 0);
/*      */   }
/*      */ 
/*      */   public Dimension getMaximumSize(JComponent paramJComponent) {
/*  348 */     return new Dimension(2147483647, 2147483647);
/*      */   }
/*      */ 
/*      */   protected void replacePane(JComponent paramJComponent1, JComponent paramJComponent2)
/*      */   {
/*  359 */     if (paramJComponent1 != null) {
/*  360 */       deinstallMouseHandlers(paramJComponent1);
/*  361 */       this.frame.remove(paramJComponent1);
/*      */     }
/*  363 */     if (paramJComponent2 != null) {
/*  364 */       this.frame.add(paramJComponent2);
/*  365 */       installMouseHandlers(paramJComponent2);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void deinstallMouseHandlers(JComponent paramJComponent) {
/*  370 */     paramJComponent.removeMouseListener(this.borderListener);
/*  371 */     paramJComponent.removeMouseMotionListener(this.borderListener);
/*      */   }
/*      */ 
/*      */   protected void installMouseHandlers(JComponent paramJComponent) {
/*  375 */     paramJComponent.addMouseListener(this.borderListener);
/*  376 */     paramJComponent.addMouseMotionListener(this.borderListener);
/*      */   }
/*      */ 
/*      */   protected JComponent createNorthPane(JInternalFrame paramJInternalFrame) {
/*  380 */     this.titlePane = new BasicInternalFrameTitlePane(paramJInternalFrame);
/*  381 */     return this.titlePane;
/*      */   }
/*      */ 
/*      */   protected JComponent createSouthPane(JInternalFrame paramJInternalFrame)
/*      */   {
/*  386 */     return null;
/*      */   }
/*      */ 
/*      */   protected JComponent createWestPane(JInternalFrame paramJInternalFrame) {
/*  390 */     return null;
/*      */   }
/*      */ 
/*      */   protected JComponent createEastPane(JInternalFrame paramJInternalFrame) {
/*  394 */     return null;
/*      */   }
/*      */ 
/*      */   protected MouseInputAdapter createBorderListener(JInternalFrame paramJInternalFrame)
/*      */   {
/*  399 */     return new BorderListener();
/*      */   }
/*      */ 
/*      */   protected void createInternalFrameListener() {
/*  403 */     this.internalFrameListener = getHandler();
/*      */   }
/*      */ 
/*      */   protected final boolean isKeyBindingRegistered() {
/*  407 */     return this.keyBindingRegistered;
/*      */   }
/*      */ 
/*      */   protected final void setKeyBindingRegistered(boolean paramBoolean) {
/*  411 */     this.keyBindingRegistered = paramBoolean;
/*      */   }
/*      */ 
/*      */   public final boolean isKeyBindingActive() {
/*  415 */     return this.keyBindingActive;
/*      */   }
/*      */ 
/*      */   protected final void setKeyBindingActive(boolean paramBoolean) {
/*  419 */     this.keyBindingActive = paramBoolean;
/*      */   }
/*      */ 
/*      */   protected void setupMenuOpenKey()
/*      */   {
/*  428 */     InputMap localInputMap = getInputMap(2);
/*  429 */     SwingUtilities.replaceUIInputMap(this.frame, 2, localInputMap);
/*      */   }
/*      */ 
/*      */   protected void setupMenuCloseKey()
/*      */   {
/*      */   }
/*      */ 
/*      */   public JComponent getNorthPane()
/*      */   {
/*  439 */     return this.northPane;
/*      */   }
/*      */ 
/*      */   public void setNorthPane(JComponent paramJComponent) {
/*  443 */     if ((this.northPane != null) && ((this.northPane instanceof BasicInternalFrameTitlePane)))
/*      */     {
/*  445 */       ((BasicInternalFrameTitlePane)this.northPane).uninstallListeners();
/*      */     }
/*  447 */     replacePane(this.northPane, paramJComponent);
/*  448 */     this.northPane = paramJComponent;
/*  449 */     if ((paramJComponent instanceof BasicInternalFrameTitlePane))
/*  450 */       this.titlePane = ((BasicInternalFrameTitlePane)paramJComponent);
/*      */   }
/*      */ 
/*      */   public JComponent getSouthPane()
/*      */   {
/*  455 */     return this.southPane;
/*      */   }
/*      */ 
/*      */   public void setSouthPane(JComponent paramJComponent) {
/*  459 */     this.southPane = paramJComponent;
/*      */   }
/*      */ 
/*      */   public JComponent getWestPane() {
/*  463 */     return this.westPane;
/*      */   }
/*      */ 
/*      */   public void setWestPane(JComponent paramJComponent) {
/*  467 */     this.westPane = paramJComponent;
/*      */   }
/*      */ 
/*      */   public JComponent getEastPane() {
/*  471 */     return this.eastPane;
/*      */   }
/*      */ 
/*      */   public void setEastPane(JComponent paramJComponent) {
/*  475 */     this.eastPane = paramJComponent;
/*      */   }
/*      */ 
/*      */   protected DesktopManager getDesktopManager()
/*      */   {
/*  526 */     if ((this.frame.getDesktopPane() != null) && (this.frame.getDesktopPane().getDesktopManager() != null))
/*      */     {
/*  528 */       return this.frame.getDesktopPane().getDesktopManager();
/*  529 */     }if (sharedDesktopManager == null)
/*  530 */       sharedDesktopManager = createDesktopManager();
/*  531 */     return sharedDesktopManager;
/*      */   }
/*      */ 
/*      */   protected DesktopManager createDesktopManager() {
/*  535 */     return new DefaultDesktopManager();
/*      */   }
/*      */ 
/*      */   protected void closeFrame(JInternalFrame paramJInternalFrame)
/*      */   {
/*  545 */     BasicLookAndFeel.playSound(this.frame, "InternalFrame.closeSound");
/*      */ 
/*  547 */     getDesktopManager().closeFrame(paramJInternalFrame);
/*      */   }
/*      */ 
/*      */   protected void maximizeFrame(JInternalFrame paramJInternalFrame)
/*      */   {
/*  557 */     BasicLookAndFeel.playSound(this.frame, "InternalFrame.maximizeSound");
/*      */ 
/*  559 */     getDesktopManager().maximizeFrame(paramJInternalFrame);
/*      */   }
/*      */ 
/*      */   protected void minimizeFrame(JInternalFrame paramJInternalFrame)
/*      */   {
/*  569 */     if (!paramJInternalFrame.isIcon())
/*      */     {
/*  572 */       BasicLookAndFeel.playSound(this.frame, "InternalFrame.restoreDownSound");
/*      */     }
/*      */ 
/*  575 */     getDesktopManager().minimizeFrame(paramJInternalFrame);
/*      */   }
/*      */ 
/*      */   protected void iconifyFrame(JInternalFrame paramJInternalFrame)
/*      */   {
/*  585 */     BasicLookAndFeel.playSound(this.frame, "InternalFrame.minimizeSound");
/*      */ 
/*  587 */     getDesktopManager().iconifyFrame(paramJInternalFrame);
/*      */   }
/*      */ 
/*      */   protected void deiconifyFrame(JInternalFrame paramJInternalFrame)
/*      */   {
/*  597 */     if (!paramJInternalFrame.isMaximum())
/*      */     {
/*  600 */       BasicLookAndFeel.playSound(this.frame, "InternalFrame.restoreUpSound");
/*      */     }
/*      */ 
/*  603 */     getDesktopManager().deiconifyFrame(paramJInternalFrame);
/*      */   }
/*      */ 
/*      */   protected void activateFrame(JInternalFrame paramJInternalFrame)
/*      */   {
/*  610 */     getDesktopManager().activateFrame(paramJInternalFrame);
/*      */   }
/*      */ 
/*      */   protected void deactivateFrame(JInternalFrame paramJInternalFrame)
/*      */   {
/*  616 */     getDesktopManager().deactivateFrame(paramJInternalFrame);
/*      */   }
/*      */ 
/*      */   protected ComponentListener createComponentListener()
/*      */   {
/* 1117 */     return getHandler();
/*      */   }
/*      */ 
/*      */   protected MouseInputListener createGlassPaneDispatcher()
/*      */   {
/* 1156 */     return null;
/*      */   }
/*      */ 
/*      */   protected class BasicInternalFrameListener implements InternalFrameListener
/*      */   {
/*      */     protected BasicInternalFrameListener()
/*      */     {
/*      */     }
/*      */ 
/*      */     public void internalFrameClosing(InternalFrameEvent paramInternalFrameEvent)
/*      */     {
/* 1167 */       BasicInternalFrameUI.this.getHandler().internalFrameClosing(paramInternalFrameEvent);
/*      */     }
/*      */ 
/*      */     public void internalFrameClosed(InternalFrameEvent paramInternalFrameEvent) {
/* 1171 */       BasicInternalFrameUI.this.getHandler().internalFrameClosed(paramInternalFrameEvent);
/*      */     }
/*      */ 
/*      */     public void internalFrameOpened(InternalFrameEvent paramInternalFrameEvent) {
/* 1175 */       BasicInternalFrameUI.this.getHandler().internalFrameOpened(paramInternalFrameEvent);
/*      */     }
/*      */ 
/*      */     public void internalFrameIconified(InternalFrameEvent paramInternalFrameEvent) {
/* 1179 */       BasicInternalFrameUI.this.getHandler().internalFrameIconified(paramInternalFrameEvent);
/*      */     }
/*      */ 
/*      */     public void internalFrameDeiconified(InternalFrameEvent paramInternalFrameEvent) {
/* 1183 */       BasicInternalFrameUI.this.getHandler().internalFrameDeiconified(paramInternalFrameEvent);
/*      */     }
/*      */ 
/*      */     public void internalFrameActivated(InternalFrameEvent paramInternalFrameEvent) {
/* 1187 */       BasicInternalFrameUI.this.getHandler().internalFrameActivated(paramInternalFrameEvent);
/*      */     }
/*      */ 
/*      */     public void internalFrameDeactivated(InternalFrameEvent paramInternalFrameEvent)
/*      */     {
/* 1192 */       BasicInternalFrameUI.this.getHandler().internalFrameDeactivated(paramInternalFrameEvent);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected class BorderListener extends MouseInputAdapter
/*      */     implements SwingConstants
/*      */   {
/*      */     int _x;
/*      */     int _y;
/*      */     int __x;
/*      */     int __y;
/*      */     Rectangle startingBounds;
/*      */     int resizeDir;
/*  635 */     protected final int RESIZE_NONE = 0;
/*  636 */     private boolean discardRelease = false;
/*      */ 
/*  638 */     int resizeCornerSize = 16;
/*      */ 
/*      */     protected BorderListener() {  } 
/*  641 */     public void mouseClicked(MouseEvent paramMouseEvent) { if ((paramMouseEvent.getClickCount() > 1) && (paramMouseEvent.getSource() == BasicInternalFrameUI.this.getNorthPane()))
/*  642 */         if ((BasicInternalFrameUI.this.frame.isIconifiable()) && (BasicInternalFrameUI.this.frame.isIcon())) try {
/*  643 */             BasicInternalFrameUI.this.frame.setIcon(false); } catch (PropertyVetoException localPropertyVetoException1) {
/*      */           } else if (BasicInternalFrameUI.this.frame.isMaximizable())
/*  645 */           if (!BasicInternalFrameUI.this.frame.isMaximum()) try {
/*  646 */               BasicInternalFrameUI.this.frame.setMaximum(true); } catch (PropertyVetoException localPropertyVetoException2) {
/*      */             } else try {
/*  648 */               BasicInternalFrameUI.this.frame.setMaximum(false);
/*      */             }
/*      */             catch (PropertyVetoException localPropertyVetoException3)
/*      */             {
/*      */             }
/*      */     }
/*      */ 
/*      */     void finishMouseReleased()
/*      */     {
/*  657 */       if (this.discardRelease) {
/*  658 */         this.discardRelease = false;
/*  659 */         return;
/*      */       }
/*  661 */       if (this.resizeDir == 0) {
/*  662 */         BasicInternalFrameUI.this.getDesktopManager().endDraggingFrame(BasicInternalFrameUI.this.frame);
/*  663 */         BasicInternalFrameUI.this.dragging = false;
/*      */       }
/*      */       else
/*      */       {
/*  667 */         Window localWindow = SwingUtilities.getWindowAncestor(BasicInternalFrameUI.this.frame);
/*      */ 
/*  669 */         if (localWindow != null) {
/*  670 */           localWindow.removeWindowFocusListener(BasicInternalFrameUI.this.getWindowFocusListener());
/*      */         }
/*      */ 
/*  673 */         Container localContainer = BasicInternalFrameUI.this.frame.getTopLevelAncestor();
/*  674 */         if ((localContainer instanceof RootPaneContainer)) {
/*  675 */           Component localComponent = ((RootPaneContainer)localContainer).getGlassPane();
/*  676 */           localComponent.setCursor(Cursor.getPredefinedCursor(0));
/*      */ 
/*  678 */           localComponent.setVisible(false);
/*      */         }
/*  680 */         BasicInternalFrameUI.this.getDesktopManager().endResizingFrame(BasicInternalFrameUI.this.frame);
/*  681 */         BasicInternalFrameUI.this.resizing = false;
/*  682 */         BasicInternalFrameUI.this.updateFrameCursor();
/*      */       }
/*  684 */       this._x = 0;
/*  685 */       this._y = 0;
/*  686 */       this.__x = 0;
/*  687 */       this.__y = 0;
/*  688 */       this.startingBounds = null;
/*  689 */       this.resizeDir = 0;
/*      */ 
/*  693 */       this.discardRelease = true;
/*      */     }
/*      */ 
/*      */     public void mouseReleased(MouseEvent paramMouseEvent) {
/*  697 */       finishMouseReleased();
/*      */     }
/*      */ 
/*      */     public void mousePressed(MouseEvent paramMouseEvent) {
/*  701 */       Point localPoint1 = SwingUtilities.convertPoint((Component)paramMouseEvent.getSource(), paramMouseEvent.getX(), paramMouseEvent.getY(), null);
/*      */ 
/*  703 */       this.__x = paramMouseEvent.getX();
/*  704 */       this.__y = paramMouseEvent.getY();
/*  705 */       this._x = localPoint1.x;
/*  706 */       this._y = localPoint1.y;
/*  707 */       this.startingBounds = BasicInternalFrameUI.this.frame.getBounds();
/*  708 */       this.resizeDir = 0;
/*  709 */       this.discardRelease = false;
/*      */       try {
/*  711 */         BasicInternalFrameUI.this.frame.setSelected(true);
/*      */       } catch (PropertyVetoException localPropertyVetoException) {
/*      */       }
/*  714 */       Insets localInsets = BasicInternalFrameUI.this.frame.getInsets();
/*      */ 
/*  716 */       Point localPoint2 = new Point(this.__x, this.__y);
/*      */       Object localObject1;
/*  717 */       if (paramMouseEvent.getSource() == BasicInternalFrameUI.this.getNorthPane()) {
/*  718 */         localObject1 = BasicInternalFrameUI.this.getNorthPane().getLocation();
/*  719 */         localPoint2.x += ((Point)localObject1).x;
/*  720 */         localPoint2.y += ((Point)localObject1).y;
/*      */       }
/*      */ 
/*  723 */       if ((paramMouseEvent.getSource() == BasicInternalFrameUI.this.getNorthPane()) && 
/*  724 */         (localPoint2.x > localInsets.left) && (localPoint2.y > localInsets.top) && (localPoint2.x < BasicInternalFrameUI.this.frame.getWidth() - localInsets.right)) {
/*  725 */         BasicInternalFrameUI.this.getDesktopManager().beginDraggingFrame(BasicInternalFrameUI.this.frame);
/*  726 */         BasicInternalFrameUI.this.dragging = true;
/*  727 */         return;
/*      */       }
/*      */ 
/*  730 */       if (!BasicInternalFrameUI.this.frame.isResizable()) {
/*  731 */         return;
/*      */       }
/*      */ 
/*  734 */       if ((paramMouseEvent.getSource() == BasicInternalFrameUI.this.frame) || (paramMouseEvent.getSource() == BasicInternalFrameUI.this.getNorthPane())) {
/*  735 */         if (localPoint2.x <= localInsets.left) {
/*  736 */           if (localPoint2.y < this.resizeCornerSize + localInsets.top)
/*  737 */             this.resizeDir = 8;
/*  738 */           else if (localPoint2.y > BasicInternalFrameUI.this.frame.getHeight() - this.resizeCornerSize - localInsets.bottom)
/*      */           {
/*  740 */             this.resizeDir = 6;
/*      */           }
/*  742 */           else this.resizeDir = 7;
/*      */         }
/*  744 */         else if (localPoint2.x >= BasicInternalFrameUI.this.frame.getWidth() - localInsets.right) {
/*  745 */           if (localPoint2.y < this.resizeCornerSize + localInsets.top)
/*  746 */             this.resizeDir = 2;
/*  747 */           else if (localPoint2.y > BasicInternalFrameUI.this.frame.getHeight() - this.resizeCornerSize - localInsets.bottom)
/*      */           {
/*  749 */             this.resizeDir = 4;
/*      */           }
/*  751 */           else this.resizeDir = 3;
/*      */         }
/*  753 */         else if (localPoint2.y <= localInsets.top) {
/*  754 */           if (localPoint2.x < this.resizeCornerSize + localInsets.left)
/*  755 */             this.resizeDir = 8;
/*  756 */           else if (localPoint2.x > BasicInternalFrameUI.this.frame.getWidth() - this.resizeCornerSize - localInsets.right)
/*      */           {
/*  758 */             this.resizeDir = 2;
/*      */           }
/*  760 */           else this.resizeDir = 1;
/*      */         }
/*  762 */         else if (localPoint2.y >= BasicInternalFrameUI.this.frame.getHeight() - localInsets.bottom) {
/*  763 */           if (localPoint2.x < this.resizeCornerSize + localInsets.left)
/*  764 */             this.resizeDir = 6;
/*  765 */           else if (localPoint2.x > BasicInternalFrameUI.this.frame.getWidth() - this.resizeCornerSize - localInsets.right)
/*      */           {
/*  767 */             this.resizeDir = 4;
/*      */           }
/*  769 */           else this.resizeDir = 5;
/*      */ 
/*      */         }
/*      */         else
/*      */         {
/*  774 */           this.discardRelease = true;
/*  775 */           return;
/*      */         }
/*  777 */         localObject1 = Cursor.getPredefinedCursor(0);
/*  778 */         switch (this.resizeDir) {
/*      */         case 5:
/*  780 */           localObject1 = Cursor.getPredefinedCursor(9);
/*  781 */           break;
/*      */         case 1:
/*  783 */           localObject1 = Cursor.getPredefinedCursor(8);
/*  784 */           break;
/*      */         case 7:
/*  786 */           localObject1 = Cursor.getPredefinedCursor(10);
/*  787 */           break;
/*      */         case 3:
/*  789 */           localObject1 = Cursor.getPredefinedCursor(11);
/*  790 */           break;
/*      */         case 4:
/*  792 */           localObject1 = Cursor.getPredefinedCursor(5);
/*  793 */           break;
/*      */         case 6:
/*  795 */           localObject1 = Cursor.getPredefinedCursor(4);
/*  796 */           break;
/*      */         case 8:
/*  798 */           localObject1 = Cursor.getPredefinedCursor(6);
/*  799 */           break;
/*      */         case 2:
/*  801 */           localObject1 = Cursor.getPredefinedCursor(7);
/*      */         }
/*      */ 
/*  804 */         Container localContainer = BasicInternalFrameUI.this.frame.getTopLevelAncestor();
/*  805 */         if ((localContainer instanceof RootPaneContainer)) {
/*  806 */           localObject2 = ((RootPaneContainer)localContainer).getGlassPane();
/*  807 */           ((Component)localObject2).setVisible(true);
/*  808 */           ((Component)localObject2).setCursor((Cursor)localObject1);
/*      */         }
/*  810 */         BasicInternalFrameUI.this.getDesktopManager().beginResizingFrame(BasicInternalFrameUI.this.frame, this.resizeDir);
/*  811 */         BasicInternalFrameUI.this.resizing = true;
/*      */ 
/*  814 */         Object localObject2 = SwingUtilities.getWindowAncestor(BasicInternalFrameUI.this.frame);
/*  815 */         if (localObject2 != null) {
/*  816 */           ((Window)localObject2).addWindowFocusListener(BasicInternalFrameUI.this.getWindowFocusListener());
/*      */         }
/*      */ 
/*  819 */         return;
/*      */       }
/*      */     }
/*      */ 
/*      */     public void mouseDragged(MouseEvent paramMouseEvent)
/*      */     {
/*  825 */       if (this.startingBounds == null)
/*      */       {
/*  827 */         return;
/*      */       }
/*      */ 
/*  830 */       Point localPoint = SwingUtilities.convertPoint((Component)paramMouseEvent.getSource(), paramMouseEvent.getX(), paramMouseEvent.getY(), null);
/*      */ 
/*  832 */       int i = this._x - localPoint.x;
/*  833 */       int j = this._y - localPoint.y;
/*  834 */       Dimension localDimension1 = BasicInternalFrameUI.this.frame.getMinimumSize();
/*  835 */       Dimension localDimension2 = BasicInternalFrameUI.this.frame.getMaximumSize();
/*      */ 
/*  837 */       Insets localInsets = BasicInternalFrameUI.this.frame.getInsets();
/*      */ 
/*  840 */       if (BasicInternalFrameUI.this.dragging) {
/*  841 */         if ((BasicInternalFrameUI.this.frame.isMaximum()) || ((paramMouseEvent.getModifiers() & 0x10) != 16))
/*      */         {
/*  846 */           return;
/*      */         }
/*      */ 
/*  849 */         Dimension localDimension3 = BasicInternalFrameUI.this.frame.getParent().getSize();
/*  850 */         int i2 = localDimension3.width;
/*  851 */         int i3 = localDimension3.height;
/*      */ 
/*  854 */         k = this.startingBounds.x - i;
/*  855 */         m = this.startingBounds.y - j;
/*      */ 
/*  858 */         if (k + localInsets.left <= -this.__x)
/*  859 */           k = -this.__x - localInsets.left + 1;
/*  860 */         if (m + localInsets.top <= -this.__y)
/*  861 */           m = -this.__y - localInsets.top + 1;
/*  862 */         if (k + this.__x + localInsets.right >= i2)
/*  863 */           k = i2 - this.__x - localInsets.right - 1;
/*  864 */         if (m + this.__y + localInsets.bottom >= i3) {
/*  865 */           m = i3 - this.__y - localInsets.bottom - 1;
/*      */         }
/*  867 */         BasicInternalFrameUI.this.getDesktopManager().dragFrame(BasicInternalFrameUI.this.frame, k, m);
/*  868 */         return;
/*      */       }
/*      */ 
/*  871 */       if (!BasicInternalFrameUI.this.frame.isResizable()) {
/*  872 */         return;
/*      */       }
/*      */ 
/*  875 */       int k = BasicInternalFrameUI.this.frame.getX();
/*  876 */       int m = BasicInternalFrameUI.this.frame.getY();
/*  877 */       int n = BasicInternalFrameUI.this.frame.getWidth();
/*  878 */       int i1 = BasicInternalFrameUI.this.frame.getHeight();
/*      */ 
/*  880 */       BasicInternalFrameUI.this.parentBounds = BasicInternalFrameUI.this.frame.getParent().getBounds();
/*      */ 
/*  882 */       switch (this.resizeDir) {
/*      */       case 0:
/*  884 */         return;
/*      */       case 1:
/*  886 */         if (this.startingBounds.height + j < localDimension1.height)
/*  887 */           j = -(this.startingBounds.height - localDimension1.height);
/*  888 */         else if (this.startingBounds.height + j > localDimension2.height)
/*  889 */           j = localDimension2.height - this.startingBounds.height;
/*  890 */         if (this.startingBounds.y - j < 0) j = this.startingBounds.y;
/*      */ 
/*  892 */         k = this.startingBounds.x;
/*  893 */         m = this.startingBounds.y - j;
/*  894 */         n = this.startingBounds.width;
/*  895 */         i1 = this.startingBounds.height + j;
/*  896 */         break;
/*      */       case 2:
/*  898 */         if (this.startingBounds.height + j < localDimension1.height)
/*  899 */           j = -(this.startingBounds.height - localDimension1.height);
/*  900 */         else if (this.startingBounds.height + j > localDimension2.height)
/*  901 */           j = localDimension2.height - this.startingBounds.height;
/*  902 */         if (this.startingBounds.y - j < 0) j = this.startingBounds.y;
/*      */ 
/*  904 */         if (this.startingBounds.width - i < localDimension1.width)
/*  905 */           i = this.startingBounds.width - localDimension1.width;
/*  906 */         else if (this.startingBounds.width - i > localDimension2.width)
/*  907 */           i = -(localDimension2.width - this.startingBounds.width);
/*  908 */         if (this.startingBounds.x + this.startingBounds.width - i > BasicInternalFrameUI.this.parentBounds.width)
/*      */         {
/*  910 */           i = this.startingBounds.x + this.startingBounds.width - BasicInternalFrameUI.this.parentBounds.width;
/*      */         }
/*      */ 
/*  914 */         k = this.startingBounds.x;
/*  915 */         m = this.startingBounds.y - j;
/*  916 */         n = this.startingBounds.width - i;
/*  917 */         i1 = this.startingBounds.height + j;
/*  918 */         break;
/*      */       case 3:
/*  920 */         if (this.startingBounds.width - i < localDimension1.width)
/*  921 */           i = this.startingBounds.width - localDimension1.width;
/*  922 */         else if (this.startingBounds.width - i > localDimension2.width)
/*  923 */           i = -(localDimension2.width - this.startingBounds.width);
/*  924 */         if (this.startingBounds.x + this.startingBounds.width - i > BasicInternalFrameUI.this.parentBounds.width)
/*      */         {
/*  926 */           i = this.startingBounds.x + this.startingBounds.width - BasicInternalFrameUI.this.parentBounds.width;
/*      */         }
/*      */ 
/*  930 */         n = this.startingBounds.width - i;
/*  931 */         i1 = this.startingBounds.height;
/*  932 */         break;
/*      */       case 4:
/*  934 */         if (this.startingBounds.width - i < localDimension1.width)
/*  935 */           i = this.startingBounds.width - localDimension1.width;
/*  936 */         else if (this.startingBounds.width - i > localDimension2.width)
/*  937 */           i = -(localDimension2.width - this.startingBounds.width);
/*  938 */         if (this.startingBounds.x + this.startingBounds.width - i > BasicInternalFrameUI.this.parentBounds.width)
/*      */         {
/*  940 */           i = this.startingBounds.x + this.startingBounds.width - BasicInternalFrameUI.this.parentBounds.width;
/*      */         }
/*      */ 
/*  944 */         if (this.startingBounds.height - j < localDimension1.height)
/*  945 */           j = this.startingBounds.height - localDimension1.height;
/*  946 */         else if (this.startingBounds.height - j > localDimension2.height)
/*  947 */           j = -(localDimension2.height - this.startingBounds.height);
/*  948 */         if (this.startingBounds.y + this.startingBounds.height - j > BasicInternalFrameUI.this.parentBounds.height)
/*      */         {
/*  950 */           j = this.startingBounds.y + this.startingBounds.height - BasicInternalFrameUI.this.parentBounds.height;
/*      */         }
/*      */ 
/*  954 */         n = this.startingBounds.width - i;
/*  955 */         i1 = this.startingBounds.height - j;
/*  956 */         break;
/*      */       case 5:
/*  958 */         if (this.startingBounds.height - j < localDimension1.height)
/*  959 */           j = this.startingBounds.height - localDimension1.height;
/*  960 */         else if (this.startingBounds.height - j > localDimension2.height)
/*  961 */           j = -(localDimension2.height - this.startingBounds.height);
/*  962 */         if (this.startingBounds.y + this.startingBounds.height - j > BasicInternalFrameUI.this.parentBounds.height)
/*      */         {
/*  964 */           j = this.startingBounds.y + this.startingBounds.height - BasicInternalFrameUI.this.parentBounds.height;
/*      */         }
/*      */ 
/*  968 */         n = this.startingBounds.width;
/*  969 */         i1 = this.startingBounds.height - j;
/*  970 */         break;
/*      */       case 6:
/*  972 */         if (this.startingBounds.height - j < localDimension1.height)
/*  973 */           j = this.startingBounds.height - localDimension1.height;
/*  974 */         else if (this.startingBounds.height - j > localDimension2.height)
/*  975 */           j = -(localDimension2.height - this.startingBounds.height);
/*  976 */         if (this.startingBounds.y + this.startingBounds.height - j > BasicInternalFrameUI.this.parentBounds.height)
/*      */         {
/*  978 */           j = this.startingBounds.y + this.startingBounds.height - BasicInternalFrameUI.this.parentBounds.height;
/*      */         }
/*      */ 
/*  982 */         if (this.startingBounds.width + i < localDimension1.width)
/*  983 */           i = -(this.startingBounds.width - localDimension1.width);
/*  984 */         else if (this.startingBounds.width + i > localDimension2.width)
/*  985 */           i = localDimension2.width - this.startingBounds.width;
/*  986 */         if (this.startingBounds.x - i < 0) {
/*  987 */           i = this.startingBounds.x;
/*      */         }
/*      */ 
/*  990 */         k = this.startingBounds.x - i;
/*  991 */         m = this.startingBounds.y;
/*  992 */         n = this.startingBounds.width + i;
/*  993 */         i1 = this.startingBounds.height - j;
/*  994 */         break;
/*      */       case 7:
/*  996 */         if (this.startingBounds.width + i < localDimension1.width)
/*  997 */           i = -(this.startingBounds.width - localDimension1.width);
/*  998 */         else if (this.startingBounds.width + i > localDimension2.width)
/*  999 */           i = localDimension2.width - this.startingBounds.width;
/* 1000 */         if (this.startingBounds.x - i < 0) {
/* 1001 */           i = this.startingBounds.x;
/*      */         }
/*      */ 
/* 1004 */         k = this.startingBounds.x - i;
/* 1005 */         m = this.startingBounds.y;
/* 1006 */         n = this.startingBounds.width + i;
/* 1007 */         i1 = this.startingBounds.height;
/* 1008 */         break;
/*      */       case 8:
/* 1010 */         if (this.startingBounds.width + i < localDimension1.width)
/* 1011 */           i = -(this.startingBounds.width - localDimension1.width);
/* 1012 */         else if (this.startingBounds.width + i > localDimension2.width)
/* 1013 */           i = localDimension2.width - this.startingBounds.width;
/* 1014 */         if (this.startingBounds.x - i < 0) {
/* 1015 */           i = this.startingBounds.x;
/*      */         }
/*      */ 
/* 1018 */         if (this.startingBounds.height + j < localDimension1.height)
/* 1019 */           j = -(this.startingBounds.height - localDimension1.height);
/* 1020 */         else if (this.startingBounds.height + j > localDimension2.height)
/* 1021 */           j = localDimension2.height - this.startingBounds.height;
/* 1022 */         if (this.startingBounds.y - j < 0) j = this.startingBounds.y;
/*      */ 
/* 1024 */         k = this.startingBounds.x - i;
/* 1025 */         m = this.startingBounds.y - j;
/* 1026 */         n = this.startingBounds.width + i;
/* 1027 */         i1 = this.startingBounds.height + j;
/* 1028 */         break;
/*      */       default:
/* 1030 */         return;
/*      */       }
/* 1032 */       BasicInternalFrameUI.this.getDesktopManager().resizeFrame(BasicInternalFrameUI.this.frame, k, m, n, i1);
/*      */     }
/*      */ 
/*      */     public void mouseMoved(MouseEvent paramMouseEvent)
/*      */     {
/* 1037 */       if (!BasicInternalFrameUI.this.frame.isResizable()) {
/* 1038 */         return;
/*      */       }
/* 1040 */       if ((paramMouseEvent.getSource() == BasicInternalFrameUI.this.frame) || (paramMouseEvent.getSource() == BasicInternalFrameUI.this.getNorthPane())) {
/* 1041 */         Insets localInsets = BasicInternalFrameUI.this.frame.getInsets();
/* 1042 */         Point localPoint1 = new Point(paramMouseEvent.getX(), paramMouseEvent.getY());
/* 1043 */         if (paramMouseEvent.getSource() == BasicInternalFrameUI.this.getNorthPane()) {
/* 1044 */           Point localPoint2 = BasicInternalFrameUI.this.getNorthPane().getLocation();
/* 1045 */           localPoint1.x += localPoint2.x;
/* 1046 */           localPoint1.y += localPoint2.y;
/*      */         }
/* 1048 */         if (localPoint1.x <= localInsets.left) {
/* 1049 */           if (localPoint1.y < this.resizeCornerSize + localInsets.top)
/* 1050 */             BasicInternalFrameUI.this.frame.setCursor(Cursor.getPredefinedCursor(6));
/* 1051 */           else if (localPoint1.y > BasicInternalFrameUI.this.frame.getHeight() - this.resizeCornerSize - localInsets.bottom)
/* 1052 */             BasicInternalFrameUI.this.frame.setCursor(Cursor.getPredefinedCursor(4));
/*      */           else
/* 1054 */             BasicInternalFrameUI.this.frame.setCursor(Cursor.getPredefinedCursor(10));
/* 1055 */         } else if (localPoint1.x >= BasicInternalFrameUI.this.frame.getWidth() - localInsets.right) {
/* 1056 */           if (paramMouseEvent.getY() < this.resizeCornerSize + localInsets.top)
/* 1057 */             BasicInternalFrameUI.this.frame.setCursor(Cursor.getPredefinedCursor(7));
/* 1058 */           else if (localPoint1.y > BasicInternalFrameUI.this.frame.getHeight() - this.resizeCornerSize - localInsets.bottom)
/* 1059 */             BasicInternalFrameUI.this.frame.setCursor(Cursor.getPredefinedCursor(5));
/*      */           else
/* 1061 */             BasicInternalFrameUI.this.frame.setCursor(Cursor.getPredefinedCursor(11));
/* 1062 */         } else if (localPoint1.y <= localInsets.top) {
/* 1063 */           if (localPoint1.x < this.resizeCornerSize + localInsets.left)
/* 1064 */             BasicInternalFrameUI.this.frame.setCursor(Cursor.getPredefinedCursor(6));
/* 1065 */           else if (localPoint1.x > BasicInternalFrameUI.this.frame.getWidth() - this.resizeCornerSize - localInsets.right)
/* 1066 */             BasicInternalFrameUI.this.frame.setCursor(Cursor.getPredefinedCursor(7));
/*      */           else
/* 1068 */             BasicInternalFrameUI.this.frame.setCursor(Cursor.getPredefinedCursor(8));
/* 1069 */         } else if (localPoint1.y >= BasicInternalFrameUI.this.frame.getHeight() - localInsets.bottom) {
/* 1070 */           if (localPoint1.x < this.resizeCornerSize + localInsets.left)
/* 1071 */             BasicInternalFrameUI.this.frame.setCursor(Cursor.getPredefinedCursor(4));
/* 1072 */           else if (localPoint1.x > BasicInternalFrameUI.this.frame.getWidth() - this.resizeCornerSize - localInsets.right)
/* 1073 */             BasicInternalFrameUI.this.frame.setCursor(Cursor.getPredefinedCursor(5));
/*      */           else
/* 1075 */             BasicInternalFrameUI.this.frame.setCursor(Cursor.getPredefinedCursor(9));
/*      */         }
/*      */         else
/* 1078 */           BasicInternalFrameUI.this.updateFrameCursor();
/* 1079 */         return;
/*      */       }
/*      */ 
/* 1082 */       BasicInternalFrameUI.this.updateFrameCursor();
/*      */     }
/*      */ 
/*      */     public void mouseEntered(MouseEvent paramMouseEvent) {
/* 1086 */       BasicInternalFrameUI.this.updateFrameCursor();
/*      */     }
/*      */ 
/*      */     public void mouseExited(MouseEvent paramMouseEvent) {
/* 1090 */       BasicInternalFrameUI.this.updateFrameCursor();
/*      */     }
/*      */   }
/*      */ 
/*      */   protected class ComponentHandler implements ComponentListener
/*      */   {
/*      */     protected ComponentHandler()
/*      */     {
/*      */     }
/*      */ 
/*      */     public void componentResized(ComponentEvent paramComponentEvent)
/*      */     {
/* 1102 */       BasicInternalFrameUI.this.getHandler().componentResized(paramComponentEvent);
/*      */     }
/*      */ 
/*      */     public void componentMoved(ComponentEvent paramComponentEvent) {
/* 1106 */       BasicInternalFrameUI.this.getHandler().componentMoved(paramComponentEvent);
/*      */     }
/*      */     public void componentShown(ComponentEvent paramComponentEvent) {
/* 1109 */       BasicInternalFrameUI.this.getHandler().componentShown(paramComponentEvent);
/*      */     }
/*      */     public void componentHidden(ComponentEvent paramComponentEvent) {
/* 1112 */       BasicInternalFrameUI.this.getHandler().componentHidden(paramComponentEvent);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected class GlassPaneDispatcher
/*      */     implements MouseInputListener
/*      */   {
/*      */     protected GlassPaneDispatcher()
/*      */     {
/*      */     }
/*      */ 
/*      */     public void mousePressed(MouseEvent paramMouseEvent)
/*      */     {
/* 1127 */       BasicInternalFrameUI.this.getHandler().mousePressed(paramMouseEvent);
/*      */     }
/*      */ 
/*      */     public void mouseEntered(MouseEvent paramMouseEvent) {
/* 1131 */       BasicInternalFrameUI.this.getHandler().mouseEntered(paramMouseEvent);
/*      */     }
/*      */ 
/*      */     public void mouseMoved(MouseEvent paramMouseEvent) {
/* 1135 */       BasicInternalFrameUI.this.getHandler().mouseMoved(paramMouseEvent);
/*      */     }
/*      */ 
/*      */     public void mouseExited(MouseEvent paramMouseEvent) {
/* 1139 */       BasicInternalFrameUI.this.getHandler().mouseExited(paramMouseEvent);
/*      */     }
/*      */ 
/*      */     public void mouseClicked(MouseEvent paramMouseEvent) {
/* 1143 */       BasicInternalFrameUI.this.getHandler().mouseClicked(paramMouseEvent);
/*      */     }
/*      */ 
/*      */     public void mouseReleased(MouseEvent paramMouseEvent) {
/* 1147 */       BasicInternalFrameUI.this.getHandler().mouseReleased(paramMouseEvent);
/*      */     }
/*      */ 
/*      */     public void mouseDragged(MouseEvent paramMouseEvent) {
/* 1151 */       BasicInternalFrameUI.this.getHandler().mouseDragged(paramMouseEvent);
/*      */     }
/*      */   }
/*      */ 
/*      */   private class Handler
/*      */     implements ComponentListener, InternalFrameListener, LayoutManager, MouseInputListener, PropertyChangeListener, WindowFocusListener, SwingConstants
/*      */   {
/*      */     private Handler()
/*      */     {
/*      */     }
/*      */ 
/*      */     public void windowGainedFocus(WindowEvent paramWindowEvent)
/*      */     {
/*      */     }
/*      */ 
/*      */     public void windowLostFocus(WindowEvent paramWindowEvent)
/*      */     {
/* 1207 */       BasicInternalFrameUI.this.cancelResize();
/*      */     }
/*      */ 
/*      */     public void componentResized(ComponentEvent paramComponentEvent)
/*      */     {
/* 1214 */       Rectangle localRectangle1 = ((Component)paramComponentEvent.getSource()).getBounds();
/* 1215 */       JInternalFrame.JDesktopIcon localJDesktopIcon = null;
/*      */ 
/* 1217 */       if (BasicInternalFrameUI.this.frame != null) {
/* 1218 */         localJDesktopIcon = BasicInternalFrameUI.this.frame.getDesktopIcon();
/*      */ 
/* 1221 */         if (BasicInternalFrameUI.this.frame.isMaximum()) {
/* 1222 */           BasicInternalFrameUI.this.frame.setBounds(0, 0, localRectangle1.width, localRectangle1.height);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1228 */       if (localJDesktopIcon != null) {
/* 1229 */         Rectangle localRectangle2 = localJDesktopIcon.getBounds();
/* 1230 */         int i = localRectangle2.y + (localRectangle1.height - BasicInternalFrameUI.this.parentBounds.height);
/*      */ 
/* 1232 */         localJDesktopIcon.setBounds(localRectangle2.x, i, localRectangle2.width, localRectangle2.height);
/*      */       }
/*      */ 
/* 1237 */       if (!BasicInternalFrameUI.this.parentBounds.equals(localRectangle1)) {
/* 1238 */         BasicInternalFrameUI.this.parentBounds = localRectangle1;
/*      */       }
/*      */ 
/* 1242 */       if (BasicInternalFrameUI.this.frame != null) BasicInternalFrameUI.this.frame.validate(); 
/*      */     }
/*      */     public void componentMoved(ComponentEvent paramComponentEvent) {
/*      */     }
/*      */     public void componentShown(ComponentEvent paramComponentEvent) {
/*      */     }
/*      */ 
/*      */     public void componentHidden(ComponentEvent paramComponentEvent) {
/*      */     }
/*      */ 
/* 1252 */     public void internalFrameClosed(InternalFrameEvent paramInternalFrameEvent) { BasicInternalFrameUI.this.frame.removeInternalFrameListener(BasicInternalFrameUI.this.getHandler()); }
/*      */ 
/*      */     public void internalFrameActivated(InternalFrameEvent paramInternalFrameEvent)
/*      */     {
/* 1256 */       if (!BasicInternalFrameUI.this.isKeyBindingRegistered()) {
/* 1257 */         BasicInternalFrameUI.this.setKeyBindingRegistered(true);
/* 1258 */         BasicInternalFrameUI.this.setupMenuOpenKey();
/* 1259 */         BasicInternalFrameUI.this.setupMenuCloseKey();
/*      */       }
/* 1261 */       if (BasicInternalFrameUI.this.isKeyBindingRegistered())
/* 1262 */         BasicInternalFrameUI.this.setKeyBindingActive(true);
/*      */     }
/*      */ 
/*      */     public void internalFrameDeactivated(InternalFrameEvent paramInternalFrameEvent) {
/* 1266 */       BasicInternalFrameUI.this.setKeyBindingActive(false);
/*      */     }
/*      */     public void internalFrameClosing(InternalFrameEvent paramInternalFrameEvent) {
/*      */     }
/*      */     public void internalFrameOpened(InternalFrameEvent paramInternalFrameEvent) {
/*      */     }
/*      */     public void internalFrameIconified(InternalFrameEvent paramInternalFrameEvent) {
/*      */     }
/*      */     public void internalFrameDeiconified(InternalFrameEvent paramInternalFrameEvent) {
/*      */     }
/*      */     public void addLayoutComponent(String paramString, Component paramComponent) {
/*      */     }
/*      */     public void removeLayoutComponent(Component paramComponent) {
/*      */     }
/* 1280 */     public Dimension preferredLayoutSize(Container paramContainer) { Insets localInsets = BasicInternalFrameUI.this.frame.getInsets();
/*      */ 
/* 1282 */       Dimension localDimension1 = new Dimension(BasicInternalFrameUI.this.frame.getRootPane().getPreferredSize());
/* 1283 */       localDimension1.width += localInsets.left + localInsets.right;
/* 1284 */       localDimension1.height += localInsets.top + localInsets.bottom;
/*      */       Dimension localDimension2;
/* 1286 */       if (BasicInternalFrameUI.this.getNorthPane() != null) {
/* 1287 */         localDimension2 = BasicInternalFrameUI.this.getNorthPane().getPreferredSize();
/* 1288 */         localDimension1.width = Math.max(localDimension2.width, localDimension1.width);
/* 1289 */         localDimension1.height += localDimension2.height;
/*      */       }
/*      */ 
/* 1292 */       if (BasicInternalFrameUI.this.getSouthPane() != null) {
/* 1293 */         localDimension2 = BasicInternalFrameUI.this.getSouthPane().getPreferredSize();
/* 1294 */         localDimension1.width = Math.max(localDimension2.width, localDimension1.width);
/* 1295 */         localDimension1.height += localDimension2.height;
/*      */       }
/*      */ 
/* 1298 */       if (BasicInternalFrameUI.this.getEastPane() != null) {
/* 1299 */         localDimension2 = BasicInternalFrameUI.this.getEastPane().getPreferredSize();
/* 1300 */         localDimension1.width += localDimension2.width;
/* 1301 */         localDimension1.height = Math.max(localDimension2.height, localDimension1.height);
/*      */       }
/*      */ 
/* 1304 */       if (BasicInternalFrameUI.this.getWestPane() != null) {
/* 1305 */         localDimension2 = BasicInternalFrameUI.this.getWestPane().getPreferredSize();
/* 1306 */         localDimension1.width += localDimension2.width;
/* 1307 */         localDimension1.height = Math.max(localDimension2.height, localDimension1.height);
/*      */       }
/* 1309 */       return localDimension1;
/*      */     }
/*      */ 
/*      */     public Dimension minimumLayoutSize(Container paramContainer)
/*      */     {
/* 1316 */       Dimension localDimension = new Dimension();
/* 1317 */       if ((BasicInternalFrameUI.this.getNorthPane() != null) && ((BasicInternalFrameUI.this.getNorthPane() instanceof BasicInternalFrameTitlePane)))
/*      */       {
/* 1319 */         localDimension = new Dimension(BasicInternalFrameUI.this.getNorthPane().getMinimumSize());
/*      */       }
/* 1321 */       Insets localInsets = BasicInternalFrameUI.this.frame.getInsets();
/* 1322 */       localDimension.width += localInsets.left + localInsets.right;
/* 1323 */       localDimension.height += localInsets.top + localInsets.bottom;
/*      */ 
/* 1325 */       return localDimension;
/*      */     }
/*      */ 
/*      */     public void layoutContainer(Container paramContainer) {
/* 1329 */       Insets localInsets = BasicInternalFrameUI.this.frame.getInsets();
/*      */ 
/* 1332 */       int i = localInsets.left;
/* 1333 */       int j = localInsets.top;
/* 1334 */       int k = BasicInternalFrameUI.this.frame.getWidth() - localInsets.left - localInsets.right;
/* 1335 */       int m = BasicInternalFrameUI.this.frame.getHeight() - localInsets.top - localInsets.bottom;
/*      */       Dimension localDimension;
/* 1337 */       if (BasicInternalFrameUI.this.getNorthPane() != null) {
/* 1338 */         localDimension = BasicInternalFrameUI.this.getNorthPane().getPreferredSize();
/* 1339 */         if (DefaultLookup.getBoolean(BasicInternalFrameUI.this.frame, BasicInternalFrameUI.this, "InternalFrame.layoutTitlePaneAtOrigin", false))
/*      */         {
/* 1341 */           j = 0;
/* 1342 */           m += localInsets.top;
/* 1343 */           BasicInternalFrameUI.this.getNorthPane().setBounds(0, 0, BasicInternalFrameUI.this.frame.getWidth(), localDimension.height);
/*      */         }
/*      */         else
/*      */         {
/* 1347 */           BasicInternalFrameUI.this.getNorthPane().setBounds(i, j, k, localDimension.height);
/*      */         }
/* 1349 */         j += localDimension.height;
/* 1350 */         m -= localDimension.height;
/*      */       }
/*      */ 
/* 1353 */       if (BasicInternalFrameUI.this.getSouthPane() != null) {
/* 1354 */         localDimension = BasicInternalFrameUI.this.getSouthPane().getPreferredSize();
/* 1355 */         BasicInternalFrameUI.this.getSouthPane().setBounds(i, BasicInternalFrameUI.this.frame.getHeight() - localInsets.bottom - localDimension.height, k, localDimension.height);
/*      */ 
/* 1358 */         m -= localDimension.height;
/*      */       }
/*      */ 
/* 1361 */       if (BasicInternalFrameUI.this.getWestPane() != null) {
/* 1362 */         localDimension = BasicInternalFrameUI.this.getWestPane().getPreferredSize();
/* 1363 */         BasicInternalFrameUI.this.getWestPane().setBounds(i, j, localDimension.width, m);
/* 1364 */         k -= localDimension.width;
/* 1365 */         i += localDimension.width;
/*      */       }
/*      */ 
/* 1368 */       if (BasicInternalFrameUI.this.getEastPane() != null) {
/* 1369 */         localDimension = BasicInternalFrameUI.this.getEastPane().getPreferredSize();
/* 1370 */         BasicInternalFrameUI.this.getEastPane().setBounds(k - localDimension.width, j, localDimension.width, m);
/* 1371 */         k -= localDimension.width;
/*      */       }
/*      */ 
/* 1374 */       if (BasicInternalFrameUI.this.frame.getRootPane() != null)
/* 1375 */         BasicInternalFrameUI.this.frame.getRootPane().setBounds(i, j, k, m); 
/*      */     }
/*      */     public void mousePressed(MouseEvent paramMouseEvent) {
/*      */     }
/*      */     public void mouseEntered(MouseEvent paramMouseEvent) {
/*      */     }
/*      */ 
/*      */     public void mouseMoved(MouseEvent paramMouseEvent) {
/*      */     }
/*      */ 
/*      */     public void mouseExited(MouseEvent paramMouseEvent) {
/*      */     }
/*      */ 
/*      */     public void mouseClicked(MouseEvent paramMouseEvent) {
/*      */     }
/*      */ 
/*      */     public void mouseReleased(MouseEvent paramMouseEvent) {
/*      */     }
/*      */ 
/*      */     public void mouseDragged(MouseEvent paramMouseEvent) {
/*      */     }
/*      */ 
/* 1397 */     public void propertyChange(PropertyChangeEvent paramPropertyChangeEvent) { String str = paramPropertyChangeEvent.getPropertyName();
/* 1398 */       JInternalFrame localJInternalFrame = (JInternalFrame)paramPropertyChangeEvent.getSource();
/* 1399 */       Object localObject1 = paramPropertyChangeEvent.getNewValue();
/* 1400 */       Object localObject2 = paramPropertyChangeEvent.getOldValue();
/*      */ 
/* 1402 */       if ("closed" == str) {
/* 1403 */         if (localObject1 == Boolean.TRUE)
/*      */         {
/* 1406 */           BasicInternalFrameUI.this.cancelResize();
/* 1407 */           if ((BasicInternalFrameUI.this.frame.getParent() != null) && (BasicInternalFrameUI.this.componentListenerAdded)) {
/* 1408 */             BasicInternalFrameUI.this.frame.getParent().removeComponentListener(BasicInternalFrameUI.this.componentListener);
/*      */           }
/* 1410 */           BasicInternalFrameUI.this.closeFrame(localJInternalFrame);
/*      */         }
/* 1412 */       } else if ("maximum" == str) {
/* 1413 */         if (localObject1 == Boolean.TRUE)
/* 1414 */           BasicInternalFrameUI.this.maximizeFrame(localJInternalFrame);
/*      */         else
/* 1416 */           BasicInternalFrameUI.this.minimizeFrame(localJInternalFrame);
/*      */       }
/* 1418 */       else if ("icon" == str) {
/* 1419 */         if (localObject1 == Boolean.TRUE)
/* 1420 */           BasicInternalFrameUI.this.iconifyFrame(localJInternalFrame);
/*      */         else
/* 1422 */           BasicInternalFrameUI.this.deiconifyFrame(localJInternalFrame);
/*      */       }
/* 1424 */       else if ("selected" == str) {
/* 1425 */         if ((localObject1 == Boolean.TRUE) && (localObject2 == Boolean.FALSE))
/* 1426 */           BasicInternalFrameUI.this.activateFrame(localJInternalFrame);
/* 1427 */         else if ((localObject1 == Boolean.FALSE) && (localObject2 == Boolean.TRUE))
/*      */         {
/* 1429 */           BasicInternalFrameUI.this.deactivateFrame(localJInternalFrame);
/*      */         }
/* 1431 */       } else if (str == "ancestor") {
/* 1432 */         if (localObject1 == null)
/*      */         {
/* 1435 */           BasicInternalFrameUI.this.cancelResize();
/*      */         }
/* 1437 */         if (BasicInternalFrameUI.this.frame.getParent() != null)
/* 1438 */           BasicInternalFrameUI.this.parentBounds = localJInternalFrame.getParent().getBounds();
/*      */         else {
/* 1440 */           BasicInternalFrameUI.this.parentBounds = null;
/*      */         }
/* 1442 */         if ((BasicInternalFrameUI.this.frame.getParent() != null) && (!BasicInternalFrameUI.this.componentListenerAdded)) {
/* 1443 */           localJInternalFrame.getParent().addComponentListener(BasicInternalFrameUI.this.componentListener);
/* 1444 */           BasicInternalFrameUI.this.componentListenerAdded = true;
/*      */         }
/* 1446 */       } else if (("title" == str) || (str == "closable") || (str == "iconable") || (str == "maximizable"))
/*      */       {
/* 1449 */         Dimension localDimension1 = BasicInternalFrameUI.this.frame.getMinimumSize();
/* 1450 */         Dimension localDimension2 = BasicInternalFrameUI.this.frame.getSize();
/* 1451 */         if (localDimension1.width > localDimension2.width)
/* 1452 */           BasicInternalFrameUI.this.frame.setSize(localDimension1.width, localDimension2.height);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public class InternalFrameLayout
/*      */     implements LayoutManager
/*      */   {
/*      */     public InternalFrameLayout()
/*      */     {
/*      */     }
/*      */ 
/*      */     public void addLayoutComponent(String paramString, Component paramComponent)
/*      */     {
/*  499 */       BasicInternalFrameUI.this.getHandler().addLayoutComponent(paramString, paramComponent);
/*      */     }
/*      */ 
/*      */     public void removeLayoutComponent(Component paramComponent) {
/*  503 */       BasicInternalFrameUI.this.getHandler().removeLayoutComponent(paramComponent);
/*      */     }
/*      */ 
/*      */     public Dimension preferredLayoutSize(Container paramContainer) {
/*  507 */       return BasicInternalFrameUI.this.getHandler().preferredLayoutSize(paramContainer);
/*      */     }
/*      */ 
/*      */     public Dimension minimumLayoutSize(Container paramContainer) {
/*  511 */       return BasicInternalFrameUI.this.getHandler().minimumLayoutSize(paramContainer);
/*      */     }
/*      */ 
/*      */     public void layoutContainer(Container paramContainer) {
/*  515 */       BasicInternalFrameUI.this.getHandler().layoutContainer(paramContainer);
/*      */     }
/*      */   }
/*      */ 
/*      */   public class InternalFramePropertyChangeListener
/*      */     implements PropertyChangeListener
/*      */   {
/*      */     public InternalFramePropertyChangeListener()
/*      */     {
/*      */     }
/*      */ 
/*      */     public void propertyChange(PropertyChangeEvent paramPropertyChangeEvent)
/*      */     {
/*  489 */       BasicInternalFrameUI.this.getHandler().propertyChange(paramPropertyChangeEvent);
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.basic.BasicInternalFrameUI
 * JD-Core Version:    0.6.2
 */