/*     */ package sun.awt;
/*     */ 
/*     */ import java.awt.AWTException;
/*     */ import java.awt.Button;
/*     */ import java.awt.Canvas;
/*     */ import java.awt.Checkbox;
/*     */ import java.awt.CheckboxMenuItem;
/*     */ import java.awt.Choice;
/*     */ import java.awt.Component;
/*     */ import java.awt.Cursor;
/*     */ import java.awt.Desktop;
/*     */ import java.awt.Dialog;
/*     */ import java.awt.Dialog.ModalExclusionType;
/*     */ import java.awt.Dialog.ModalityType;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.EventQueue;
/*     */ import java.awt.FileDialog;
/*     */ import java.awt.Font;
/*     */ import java.awt.FontMetrics;
/*     */ import java.awt.Frame;
/*     */ import java.awt.GraphicsConfiguration;
/*     */ import java.awt.GraphicsDevice;
/*     */ import java.awt.HeadlessException;
/*     */ import java.awt.Image;
/*     */ import java.awt.Insets;
/*     */ import java.awt.JobAttributes;
/*     */ import java.awt.Label;
/*     */ import java.awt.List;
/*     */ import java.awt.Menu;
/*     */ import java.awt.MenuBar;
/*     */ import java.awt.MenuItem;
/*     */ import java.awt.PageAttributes;
/*     */ import java.awt.Panel;
/*     */ import java.awt.Point;
/*     */ import java.awt.PopupMenu;
/*     */ import java.awt.PrintJob;
/*     */ import java.awt.Robot;
/*     */ import java.awt.ScrollPane;
/*     */ import java.awt.Scrollbar;
/*     */ import java.awt.SystemTray;
/*     */ import java.awt.TextArea;
/*     */ import java.awt.TextField;
/*     */ import java.awt.Toolkit;
/*     */ import java.awt.TrayIcon;
/*     */ import java.awt.Window;
/*     */ import java.awt.datatransfer.Clipboard;
/*     */ import java.awt.dnd.DragGestureEvent;
/*     */ import java.awt.dnd.DragGestureListener;
/*     */ import java.awt.dnd.DragGestureRecognizer;
/*     */ import java.awt.dnd.DragSource;
/*     */ import java.awt.dnd.InvalidDnDOperationException;
/*     */ import java.awt.dnd.peer.DragSourceContextPeer;
/*     */ import java.awt.event.AWTEventListener;
/*     */ import java.awt.im.InputMethodHighlight;
/*     */ import java.awt.image.ColorModel;
/*     */ import java.awt.image.ImageObserver;
/*     */ import java.awt.image.ImageProducer;
/*     */ import java.awt.peer.ButtonPeer;
/*     */ import java.awt.peer.CanvasPeer;
/*     */ import java.awt.peer.CheckboxMenuItemPeer;
/*     */ import java.awt.peer.CheckboxPeer;
/*     */ import java.awt.peer.ChoicePeer;
/*     */ import java.awt.peer.DesktopPeer;
/*     */ import java.awt.peer.DialogPeer;
/*     */ import java.awt.peer.FileDialogPeer;
/*     */ import java.awt.peer.FontPeer;
/*     */ import java.awt.peer.FramePeer;
/*     */ import java.awt.peer.KeyboardFocusManagerPeer;
/*     */ import java.awt.peer.LabelPeer;
/*     */ import java.awt.peer.ListPeer;
/*     */ import java.awt.peer.MenuBarPeer;
/*     */ import java.awt.peer.MenuItemPeer;
/*     */ import java.awt.peer.MenuPeer;
/*     */ import java.awt.peer.PanelPeer;
/*     */ import java.awt.peer.PopupMenuPeer;
/*     */ import java.awt.peer.RobotPeer;
/*     */ import java.awt.peer.ScrollPanePeer;
/*     */ import java.awt.peer.ScrollbarPeer;
/*     */ import java.awt.peer.SystemTrayPeer;
/*     */ import java.awt.peer.TextAreaPeer;
/*     */ import java.awt.peer.TextFieldPeer;
/*     */ import java.awt.peer.TrayIconPeer;
/*     */ import java.awt.peer.WindowPeer;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.io.PrintStream;
/*     */ import java.net.URL;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ 
/*     */ public class HeadlessToolkit extends Toolkit
/*     */   implements ComponentFactory, KeyboardFocusManagerPeerProvider
/*     */ {
/*  44 */   private static final KeyboardFocusManagerPeer kfmPeer = new KeyboardFocusManagerPeer() {
/*     */     public void setCurrentFocusedWindow(Window paramAnonymousWindow) {  } 
/*  46 */     public Window getCurrentFocusedWindow() { return null; } 
/*     */     public void setCurrentFocusOwner(Component paramAnonymousComponent) {  } 
/*  48 */     public Component getCurrentFocusOwner() { return null; } 
/*     */     public void clearGlobalFocusOwner(Window paramAnonymousWindow) {
/*     */     } } ;
/*     */   private Toolkit tk;
/*     */   private ComponentFactory componentFactory;
/*     */ 
/*  56 */   public HeadlessToolkit(Toolkit paramToolkit) { this.tk = paramToolkit;
/*  57 */     if ((paramToolkit instanceof KeyboardFocusManagerPeerProvider))
/*  58 */       this.componentFactory = ((KeyboardFocusManagerPeerProvider)paramToolkit);
/*     */   }
/*     */ 
/*     */   public Toolkit getUnderlyingToolkit()
/*     */   {
/*  63 */     return this.tk;
/*     */   }
/*     */ 
/*     */   public CanvasPeer createCanvas(Canvas paramCanvas)
/*     */   {
/*  73 */     return (CanvasPeer)createComponent(paramCanvas);
/*     */   }
/*     */ 
/*     */   public PanelPeer createPanel(Panel paramPanel) {
/*  77 */     return (PanelPeer)createComponent(paramPanel);
/*     */   }
/*     */ 
/*     */   public WindowPeer createWindow(Window paramWindow)
/*     */     throws HeadlessException
/*     */   {
/*  86 */     throw new HeadlessException();
/*     */   }
/*     */ 
/*     */   public FramePeer createFrame(Frame paramFrame) throws HeadlessException
/*     */   {
/*  91 */     throw new HeadlessException();
/*     */   }
/*     */ 
/*     */   public DialogPeer createDialog(Dialog paramDialog) throws HeadlessException
/*     */   {
/*  96 */     throw new HeadlessException();
/*     */   }
/*     */ 
/*     */   public ButtonPeer createButton(Button paramButton) throws HeadlessException
/*     */   {
/* 101 */     throw new HeadlessException();
/*     */   }
/*     */ 
/*     */   public TextFieldPeer createTextField(TextField paramTextField) throws HeadlessException
/*     */   {
/* 106 */     throw new HeadlessException();
/*     */   }
/*     */ 
/*     */   public ChoicePeer createChoice(Choice paramChoice) throws HeadlessException
/*     */   {
/* 111 */     throw new HeadlessException();
/*     */   }
/*     */ 
/*     */   public LabelPeer createLabel(Label paramLabel) throws HeadlessException
/*     */   {
/* 116 */     throw new HeadlessException();
/*     */   }
/*     */ 
/*     */   public ListPeer createList(List paramList) throws HeadlessException
/*     */   {
/* 121 */     throw new HeadlessException();
/*     */   }
/*     */ 
/*     */   public CheckboxPeer createCheckbox(Checkbox paramCheckbox) throws HeadlessException
/*     */   {
/* 126 */     throw new HeadlessException();
/*     */   }
/*     */ 
/*     */   public ScrollbarPeer createScrollbar(Scrollbar paramScrollbar) throws HeadlessException
/*     */   {
/* 131 */     throw new HeadlessException();
/*     */   }
/*     */ 
/*     */   public ScrollPanePeer createScrollPane(ScrollPane paramScrollPane) throws HeadlessException
/*     */   {
/* 136 */     throw new HeadlessException();
/*     */   }
/*     */ 
/*     */   public TextAreaPeer createTextArea(TextArea paramTextArea) throws HeadlessException
/*     */   {
/* 141 */     throw new HeadlessException();
/*     */   }
/*     */ 
/*     */   public FileDialogPeer createFileDialog(FileDialog paramFileDialog) throws HeadlessException
/*     */   {
/* 146 */     throw new HeadlessException();
/*     */   }
/*     */ 
/*     */   public MenuBarPeer createMenuBar(MenuBar paramMenuBar) throws HeadlessException
/*     */   {
/* 151 */     throw new HeadlessException();
/*     */   }
/*     */ 
/*     */   public MenuPeer createMenu(Menu paramMenu) throws HeadlessException
/*     */   {
/* 156 */     throw new HeadlessException();
/*     */   }
/*     */ 
/*     */   public PopupMenuPeer createPopupMenu(PopupMenu paramPopupMenu) throws HeadlessException
/*     */   {
/* 161 */     throw new HeadlessException();
/*     */   }
/*     */ 
/*     */   public MenuItemPeer createMenuItem(MenuItem paramMenuItem) throws HeadlessException
/*     */   {
/* 166 */     throw new HeadlessException();
/*     */   }
/*     */ 
/*     */   public CheckboxMenuItemPeer createCheckboxMenuItem(CheckboxMenuItem paramCheckboxMenuItem) throws HeadlessException
/*     */   {
/* 171 */     throw new HeadlessException();
/*     */   }
/*     */ 
/*     */   public DragSourceContextPeer createDragSourceContextPeer(DragGestureEvent paramDragGestureEvent)
/*     */     throws InvalidDnDOperationException
/*     */   {
/* 177 */     throw new InvalidDnDOperationException("Headless environment");
/*     */   }
/*     */ 
/*     */   public RobotPeer createRobot(Robot paramRobot, GraphicsDevice paramGraphicsDevice) throws AWTException, HeadlessException
/*     */   {
/* 182 */     throw new HeadlessException();
/*     */   }
/*     */ 
/*     */   public KeyboardFocusManagerPeer getKeyboardFocusManagerPeer()
/*     */   {
/* 187 */     return kfmPeer;
/*     */   }
/*     */ 
/*     */   public TrayIconPeer createTrayIcon(TrayIcon paramTrayIcon) throws HeadlessException
/*     */   {
/* 192 */     throw new HeadlessException();
/*     */   }
/*     */ 
/*     */   public SystemTrayPeer createSystemTray(SystemTray paramSystemTray) throws HeadlessException
/*     */   {
/* 197 */     throw new HeadlessException();
/*     */   }
/*     */ 
/*     */   public boolean isTraySupported() {
/* 201 */     return false;
/*     */   }
/*     */ 
/*     */   public GlobalCursorManager getGlobalCursorManager() throws HeadlessException
/*     */   {
/* 206 */     throw new HeadlessException();
/*     */   }
/*     */ 
/*     */   protected void loadSystemColors(int[] paramArrayOfInt)
/*     */     throws HeadlessException
/*     */   {
/* 214 */     throw new HeadlessException();
/*     */   }
/*     */ 
/*     */   public ColorModel getColorModel() throws HeadlessException
/*     */   {
/* 219 */     throw new HeadlessException();
/*     */   }
/*     */ 
/*     */   public int getScreenResolution() throws HeadlessException
/*     */   {
/* 224 */     throw new HeadlessException();
/*     */   }
/*     */ 
/*     */   public Map mapInputMethodHighlight(InputMethodHighlight paramInputMethodHighlight) throws HeadlessException
/*     */   {
/* 229 */     throw new HeadlessException();
/*     */   }
/*     */ 
/*     */   public int getMenuShortcutKeyMask() throws HeadlessException
/*     */   {
/* 234 */     throw new HeadlessException();
/*     */   }
/*     */ 
/*     */   public boolean getLockingKeyState(int paramInt) throws UnsupportedOperationException
/*     */   {
/* 239 */     throw new HeadlessException();
/*     */   }
/*     */ 
/*     */   public void setLockingKeyState(int paramInt, boolean paramBoolean) throws UnsupportedOperationException
/*     */   {
/* 244 */     throw new HeadlessException();
/*     */   }
/*     */ 
/*     */   public Cursor createCustomCursor(Image paramImage, Point paramPoint, String paramString) throws IndexOutOfBoundsException, HeadlessException
/*     */   {
/* 249 */     throw new HeadlessException();
/*     */   }
/*     */ 
/*     */   public Dimension getBestCursorSize(int paramInt1, int paramInt2) throws HeadlessException
/*     */   {
/* 254 */     throw new HeadlessException();
/*     */   }
/*     */ 
/*     */   public int getMaximumCursorColors() throws HeadlessException
/*     */   {
/* 259 */     throw new HeadlessException();
/*     */   }
/*     */ 
/*     */   public <T extends DragGestureRecognizer> T createDragGestureRecognizer(Class<T> paramClass, DragSource paramDragSource, Component paramComponent, int paramInt, DragGestureListener paramDragGestureListener)
/*     */   {
/* 267 */     return null;
/*     */   }
/*     */ 
/*     */   public int getScreenHeight() throws HeadlessException
/*     */   {
/* 272 */     throw new HeadlessException();
/*     */   }
/*     */ 
/*     */   public int getScreenWidth() throws HeadlessException
/*     */   {
/* 277 */     throw new HeadlessException();
/*     */   }
/*     */ 
/*     */   public Dimension getScreenSize() throws HeadlessException
/*     */   {
/* 282 */     throw new HeadlessException();
/*     */   }
/*     */ 
/*     */   public Insets getScreenInsets(GraphicsConfiguration paramGraphicsConfiguration) throws HeadlessException
/*     */   {
/* 287 */     throw new HeadlessException();
/*     */   }
/*     */ 
/*     */   public void setDynamicLayout(boolean paramBoolean) throws HeadlessException
/*     */   {
/* 292 */     throw new HeadlessException();
/*     */   }
/*     */ 
/*     */   protected boolean isDynamicLayoutSet() throws HeadlessException
/*     */   {
/* 297 */     throw new HeadlessException();
/*     */   }
/*     */ 
/*     */   public boolean isDynamicLayoutActive() throws HeadlessException
/*     */   {
/* 302 */     throw new HeadlessException();
/*     */   }
/*     */ 
/*     */   public Clipboard getSystemClipboard() throws HeadlessException
/*     */   {
/* 307 */     throw new HeadlessException();
/*     */   }
/*     */ 
/*     */   public PrintJob getPrintJob(Frame paramFrame, String paramString, JobAttributes paramJobAttributes, PageAttributes paramPageAttributes)
/*     */   {
/* 316 */     if (paramFrame != null)
/*     */     {
/* 318 */       throw new HeadlessException();
/*     */     }
/* 320 */     throw new NullPointerException("frame must not be null");
/*     */   }
/*     */ 
/*     */   public PrintJob getPrintJob(Frame paramFrame, String paramString, Properties paramProperties)
/*     */   {
/* 325 */     if (paramFrame != null)
/*     */     {
/* 327 */       throw new HeadlessException();
/*     */     }
/* 329 */     throw new NullPointerException("frame must not be null");
/*     */   }
/*     */ 
/*     */   public void sync()
/*     */   {
/*     */   }
/*     */ 
/*     */   public void beep()
/*     */   {
/* 342 */     System.out.write(7);
/*     */   }
/*     */ 
/*     */   public EventQueue getSystemEventQueueImpl()
/*     */   {
/* 349 */     return SunToolkit.getSystemEventQueueImplPP();
/*     */   }
/*     */ 
/*     */   public int checkImage(Image paramImage, int paramInt1, int paramInt2, ImageObserver paramImageObserver)
/*     */   {
/* 356 */     return this.tk.checkImage(paramImage, paramInt1, paramInt2, paramImageObserver);
/*     */   }
/*     */ 
/*     */   public boolean prepareImage(Image paramImage, int paramInt1, int paramInt2, ImageObserver paramImageObserver)
/*     */   {
/* 361 */     return this.tk.prepareImage(paramImage, paramInt1, paramInt2, paramImageObserver);
/*     */   }
/*     */ 
/*     */   public Image getImage(String paramString) {
/* 365 */     return this.tk.getImage(paramString);
/*     */   }
/*     */ 
/*     */   public Image getImage(URL paramURL) {
/* 369 */     return this.tk.getImage(paramURL);
/*     */   }
/*     */ 
/*     */   public Image createImage(String paramString) {
/* 373 */     return this.tk.createImage(paramString);
/*     */   }
/*     */ 
/*     */   public Image createImage(URL paramURL) {
/* 377 */     return this.tk.createImage(paramURL);
/*     */   }
/*     */ 
/*     */   public Image createImage(byte[] paramArrayOfByte, int paramInt1, int paramInt2) {
/* 381 */     return this.tk.createImage(paramArrayOfByte, paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   public Image createImage(ImageProducer paramImageProducer) {
/* 385 */     return this.tk.createImage(paramImageProducer);
/*     */   }
/*     */ 
/*     */   public Image createImage(byte[] paramArrayOfByte) {
/* 389 */     return this.tk.createImage(paramArrayOfByte);
/*     */   }
/*     */ 
/*     */   public FontPeer getFontPeer(String paramString, int paramInt)
/*     */   {
/* 397 */     if (this.componentFactory != null) {
/* 398 */       return this.componentFactory.getFontPeer(paramString, paramInt);
/*     */     }
/* 400 */     return null;
/*     */   }
/*     */ 
/*     */   public FontMetrics getFontMetrics(Font paramFont) {
/* 404 */     return this.tk.getFontMetrics(paramFont);
/*     */   }
/*     */ 
/*     */   public String[] getFontList() {
/* 408 */     return this.tk.getFontList();
/*     */   }
/*     */ 
/*     */   public void addPropertyChangeListener(String paramString, PropertyChangeListener paramPropertyChangeListener)
/*     */   {
/* 417 */     this.tk.addPropertyChangeListener(paramString, paramPropertyChangeListener);
/*     */   }
/*     */ 
/*     */   public void removePropertyChangeListener(String paramString, PropertyChangeListener paramPropertyChangeListener)
/*     */   {
/* 422 */     this.tk.removePropertyChangeListener(paramString, paramPropertyChangeListener);
/*     */   }
/*     */ 
/*     */   public boolean isModalityTypeSupported(Dialog.ModalityType paramModalityType)
/*     */   {
/* 429 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean isModalExclusionTypeSupported(Dialog.ModalExclusionType paramModalExclusionType) {
/* 433 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean isAlwaysOnTopSupported()
/*     */   {
/* 440 */     return false;
/*     */   }
/*     */ 
/*     */   public void addAWTEventListener(AWTEventListener paramAWTEventListener, long paramLong)
/*     */   {
/* 449 */     this.tk.addAWTEventListener(paramAWTEventListener, paramLong);
/*     */   }
/*     */ 
/*     */   public void removeAWTEventListener(AWTEventListener paramAWTEventListener) {
/* 453 */     this.tk.removeAWTEventListener(paramAWTEventListener);
/*     */   }
/*     */ 
/*     */   public AWTEventListener[] getAWTEventListeners() {
/* 457 */     return this.tk.getAWTEventListeners();
/*     */   }
/*     */ 
/*     */   public AWTEventListener[] getAWTEventListeners(long paramLong) {
/* 461 */     return this.tk.getAWTEventListeners(paramLong);
/*     */   }
/*     */ 
/*     */   public boolean isDesktopSupported() {
/* 465 */     return false;
/*     */   }
/*     */ 
/*     */   public DesktopPeer createDesktopPeer(Desktop paramDesktop) throws HeadlessException
/*     */   {
/* 470 */     throw new HeadlessException();
/*     */   }
/*     */ 
/*     */   public boolean areExtraMouseButtonsEnabled() throws HeadlessException {
/* 474 */     throw new HeadlessException();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.HeadlessToolkit
 * JD-Core Version:    0.6.2
 */