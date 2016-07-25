/*     */ package sun.awt;
/*     */ 
/*     */ import java.awt.AWTException;
/*     */ import java.awt.Button;
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
/*     */ import java.awt.FileDialog;
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
/*     */ import java.awt.Point;
/*     */ import java.awt.PopupMenu;
/*     */ import java.awt.PrintJob;
/*     */ import java.awt.Robot;
/*     */ import java.awt.ScrollPane;
/*     */ import java.awt.Scrollbar;
/*     */ import java.awt.SystemTray;
/*     */ import java.awt.TextArea;
/*     */ import java.awt.TextField;
/*     */ import java.awt.TrayIcon;
/*     */ import java.awt.Window;
/*     */ import java.awt.datatransfer.Clipboard;
/*     */ import java.awt.dnd.DragGestureEvent;
/*     */ import java.awt.dnd.DragGestureListener;
/*     */ import java.awt.dnd.DragGestureRecognizer;
/*     */ import java.awt.dnd.DragSource;
/*     */ import java.awt.dnd.InvalidDnDOperationException;
/*     */ import java.awt.dnd.peer.DragSourceContextPeer;
/*     */ import java.awt.im.InputMethodHighlight;
/*     */ import java.awt.im.spi.InputMethodDescriptor;
/*     */ import java.awt.image.ColorModel;
/*     */ import java.awt.peer.ButtonPeer;
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
/*     */ import java.awt.peer.PopupMenuPeer;
/*     */ import java.awt.peer.RobotPeer;
/*     */ import java.awt.peer.ScrollPanePeer;
/*     */ import java.awt.peer.ScrollbarPeer;
/*     */ import java.awt.peer.SystemTrayPeer;
/*     */ import java.awt.peer.TextAreaPeer;
/*     */ import java.awt.peer.TextFieldPeer;
/*     */ import java.awt.peer.TrayIconPeer;
/*     */ import java.awt.peer.WindowPeer;
/*     */ import java.io.PrintStream;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ 
/*     */ public class HToolkit extends SunToolkit
/*     */   implements ComponentFactory
/*     */ {
/*  47 */   private static final KeyboardFocusManagerPeer kfmPeer = new KeyboardFocusManagerPeer() {
/*     */     public void setCurrentFocusedWindow(Window paramAnonymousWindow) {  } 
/*  49 */     public Window getCurrentFocusedWindow() { return null; } 
/*     */     public void setCurrentFocusOwner(Component paramAnonymousComponent) {  } 
/*  51 */     public Component getCurrentFocusOwner() { return null; }
/*     */ 
/*     */ 
/*     */     public void clearGlobalFocusOwner(Window paramAnonymousWindow)
/*     */     {
/*     */     }
/*  47 */   };
/*     */ 
/*     */   public WindowPeer createWindow(Window paramWindow)
/*     */     throws HeadlessException
/*     */   {
/*  64 */     throw new HeadlessException();
/*     */   }
/*     */ 
/*     */   public FramePeer createFrame(Frame paramFrame) throws HeadlessException
/*     */   {
/*  69 */     throw new HeadlessException();
/*     */   }
/*     */ 
/*     */   public DialogPeer createDialog(Dialog paramDialog) throws HeadlessException
/*     */   {
/*  74 */     throw new HeadlessException();
/*     */   }
/*     */ 
/*     */   public ButtonPeer createButton(Button paramButton) throws HeadlessException
/*     */   {
/*  79 */     throw new HeadlessException();
/*     */   }
/*     */ 
/*     */   public TextFieldPeer createTextField(TextField paramTextField) throws HeadlessException
/*     */   {
/*  84 */     throw new HeadlessException();
/*     */   }
/*     */ 
/*     */   public ChoicePeer createChoice(Choice paramChoice) throws HeadlessException
/*     */   {
/*  89 */     throw new HeadlessException();
/*     */   }
/*     */ 
/*     */   public LabelPeer createLabel(Label paramLabel) throws HeadlessException
/*     */   {
/*  94 */     throw new HeadlessException();
/*     */   }
/*     */ 
/*     */   public ListPeer createList(List paramList) throws HeadlessException
/*     */   {
/*  99 */     throw new HeadlessException();
/*     */   }
/*     */ 
/*     */   public CheckboxPeer createCheckbox(Checkbox paramCheckbox) throws HeadlessException
/*     */   {
/* 104 */     throw new HeadlessException();
/*     */   }
/*     */ 
/*     */   public ScrollbarPeer createScrollbar(Scrollbar paramScrollbar) throws HeadlessException
/*     */   {
/* 109 */     throw new HeadlessException();
/*     */   }
/*     */ 
/*     */   public ScrollPanePeer createScrollPane(ScrollPane paramScrollPane) throws HeadlessException
/*     */   {
/* 114 */     throw new HeadlessException();
/*     */   }
/*     */ 
/*     */   public TextAreaPeer createTextArea(TextArea paramTextArea) throws HeadlessException
/*     */   {
/* 119 */     throw new HeadlessException();
/*     */   }
/*     */ 
/*     */   public FileDialogPeer createFileDialog(FileDialog paramFileDialog) throws HeadlessException
/*     */   {
/* 124 */     throw new HeadlessException();
/*     */   }
/*     */ 
/*     */   public MenuBarPeer createMenuBar(MenuBar paramMenuBar) throws HeadlessException
/*     */   {
/* 129 */     throw new HeadlessException();
/*     */   }
/*     */ 
/*     */   public MenuPeer createMenu(Menu paramMenu) throws HeadlessException
/*     */   {
/* 134 */     throw new HeadlessException();
/*     */   }
/*     */ 
/*     */   public PopupMenuPeer createPopupMenu(PopupMenu paramPopupMenu) throws HeadlessException
/*     */   {
/* 139 */     throw new HeadlessException();
/*     */   }
/*     */ 
/*     */   public MenuItemPeer createMenuItem(MenuItem paramMenuItem) throws HeadlessException
/*     */   {
/* 144 */     throw new HeadlessException();
/*     */   }
/*     */ 
/*     */   public CheckboxMenuItemPeer createCheckboxMenuItem(CheckboxMenuItem paramCheckboxMenuItem) throws HeadlessException
/*     */   {
/* 149 */     throw new HeadlessException();
/*     */   }
/*     */ 
/*     */   public DragSourceContextPeer createDragSourceContextPeer(DragGestureEvent paramDragGestureEvent)
/*     */     throws InvalidDnDOperationException
/*     */   {
/* 155 */     throw new InvalidDnDOperationException("Headless environment");
/*     */   }
/*     */ 
/*     */   public RobotPeer createRobot(Robot paramRobot, GraphicsDevice paramGraphicsDevice) throws AWTException, HeadlessException
/*     */   {
/* 160 */     throw new HeadlessException();
/*     */   }
/*     */ 
/*     */   public KeyboardFocusManagerPeer getKeyboardFocusManagerPeer()
/*     */   {
/* 165 */     return kfmPeer;
/*     */   }
/*     */ 
/*     */   public TrayIconPeer createTrayIcon(TrayIcon paramTrayIcon) throws HeadlessException
/*     */   {
/* 170 */     throw new HeadlessException();
/*     */   }
/*     */ 
/*     */   public SystemTrayPeer createSystemTray(SystemTray paramSystemTray) throws HeadlessException
/*     */   {
/* 175 */     throw new HeadlessException();
/*     */   }
/*     */ 
/*     */   public boolean isTraySupported() {
/* 179 */     return false;
/*     */   }
/*     */ 
/*     */   public GlobalCursorManager getGlobalCursorManager() throws HeadlessException
/*     */   {
/* 184 */     throw new HeadlessException();
/*     */   }
/*     */ 
/*     */   protected void loadSystemColors(int[] paramArrayOfInt)
/*     */     throws HeadlessException
/*     */   {
/* 192 */     throw new HeadlessException();
/*     */   }
/*     */ 
/*     */   public ColorModel getColorModel() throws HeadlessException
/*     */   {
/* 197 */     throw new HeadlessException();
/*     */   }
/*     */ 
/*     */   public int getScreenResolution() throws HeadlessException
/*     */   {
/* 202 */     throw new HeadlessException();
/*     */   }
/*     */ 
/*     */   public Map mapInputMethodHighlight(InputMethodHighlight paramInputMethodHighlight) throws HeadlessException
/*     */   {
/* 207 */     throw new HeadlessException();
/*     */   }
/*     */ 
/*     */   public int getMenuShortcutKeyMask() throws HeadlessException
/*     */   {
/* 212 */     throw new HeadlessException();
/*     */   }
/*     */ 
/*     */   public boolean getLockingKeyState(int paramInt) throws UnsupportedOperationException
/*     */   {
/* 217 */     throw new HeadlessException();
/*     */   }
/*     */ 
/*     */   public void setLockingKeyState(int paramInt, boolean paramBoolean) throws UnsupportedOperationException
/*     */   {
/* 222 */     throw new HeadlessException();
/*     */   }
/*     */ 
/*     */   public Cursor createCustomCursor(Image paramImage, Point paramPoint, String paramString) throws IndexOutOfBoundsException, HeadlessException
/*     */   {
/* 227 */     throw new HeadlessException();
/*     */   }
/*     */ 
/*     */   public Dimension getBestCursorSize(int paramInt1, int paramInt2) throws HeadlessException
/*     */   {
/* 232 */     throw new HeadlessException();
/*     */   }
/*     */ 
/*     */   public int getMaximumCursorColors() throws HeadlessException
/*     */   {
/* 237 */     throw new HeadlessException();
/*     */   }
/*     */ 
/*     */   public <T extends DragGestureRecognizer> T createDragGestureRecognizer(Class<T> paramClass, DragSource paramDragSource, Component paramComponent, int paramInt, DragGestureListener paramDragGestureListener)
/*     */   {
/* 245 */     return null;
/*     */   }
/*     */ 
/*     */   public int getScreenHeight() throws HeadlessException
/*     */   {
/* 250 */     throw new HeadlessException();
/*     */   }
/*     */ 
/*     */   public int getScreenWidth() throws HeadlessException
/*     */   {
/* 255 */     throw new HeadlessException();
/*     */   }
/*     */ 
/*     */   public Dimension getScreenSize() throws HeadlessException
/*     */   {
/* 260 */     throw new HeadlessException();
/*     */   }
/*     */ 
/*     */   public Insets getScreenInsets(GraphicsConfiguration paramGraphicsConfiguration) throws HeadlessException
/*     */   {
/* 265 */     throw new HeadlessException();
/*     */   }
/*     */ 
/*     */   public void setDynamicLayout(boolean paramBoolean) throws HeadlessException
/*     */   {
/* 270 */     throw new HeadlessException();
/*     */   }
/*     */ 
/*     */   protected boolean isDynamicLayoutSet() throws HeadlessException
/*     */   {
/* 275 */     throw new HeadlessException();
/*     */   }
/*     */ 
/*     */   public boolean isDynamicLayoutActive() throws HeadlessException
/*     */   {
/* 280 */     throw new HeadlessException();
/*     */   }
/*     */ 
/*     */   public Clipboard getSystemClipboard() throws HeadlessException
/*     */   {
/* 285 */     throw new HeadlessException();
/*     */   }
/*     */ 
/*     */   public PrintJob getPrintJob(Frame paramFrame, String paramString, JobAttributes paramJobAttributes, PageAttributes paramPageAttributes)
/*     */   {
/* 294 */     if (paramFrame != null)
/*     */     {
/* 296 */       throw new HeadlessException();
/*     */     }
/* 298 */     throw new IllegalArgumentException("PrintJob not supported in a headless environment");
/*     */   }
/*     */ 
/*     */   public PrintJob getPrintJob(Frame paramFrame, String paramString, Properties paramProperties)
/*     */   {
/* 304 */     if (paramFrame != null)
/*     */     {
/* 306 */       throw new HeadlessException();
/*     */     }
/* 308 */     throw new IllegalArgumentException("PrintJob not supported in a headless environment");
/*     */   }
/*     */ 
/*     */   public void sync()
/*     */   {
/*     */   }
/*     */ 
/*     */   protected boolean syncNativeQueue(long paramLong)
/*     */   {
/* 321 */     return false;
/*     */   }
/*     */ 
/*     */   public void beep()
/*     */   {
/* 326 */     System.out.write(7);
/*     */   }
/*     */ 
/*     */   public FontPeer getFontPeer(String paramString, int paramInt)
/*     */   {
/* 334 */     return (FontPeer)null;
/*     */   }
/*     */ 
/*     */   public boolean isModalityTypeSupported(Dialog.ModalityType paramModalityType)
/*     */   {
/* 341 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean isModalExclusionTypeSupported(Dialog.ModalExclusionType paramModalExclusionType) {
/* 345 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean isDesktopSupported() {
/* 349 */     return false;
/*     */   }
/*     */ 
/*     */   public DesktopPeer createDesktopPeer(Desktop paramDesktop) throws HeadlessException
/*     */   {
/* 354 */     throw new HeadlessException();
/*     */   }
/*     */ 
/*     */   public boolean isWindowOpacityControlSupported() {
/* 358 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean isWindowShapingSupported() {
/* 362 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean isWindowTranslucencySupported() {
/* 366 */     return false;
/*     */   }
/*     */   public void grab(Window paramWindow) {
/*     */   }
/*     */   public void ungrab(Window paramWindow) {
/*     */   }
/*     */   protected boolean syncNativeQueue() {
/* 373 */     return false;
/*     */   }
/*     */ 
/*     */   public InputMethodDescriptor getInputMethodAdapterDescriptor() throws AWTException
/*     */   {
/* 378 */     return (InputMethodDescriptor)null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.HToolkit
 * JD-Core Version:    0.6.2
 */