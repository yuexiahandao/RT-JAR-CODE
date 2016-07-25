/*     */ package java.awt;
/*     */ 
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.awt.event.MouseListener;
/*     */ import java.awt.event.MouseMotionListener;
/*     */ import java.awt.peer.TrayIconPeer;
/*     */ import java.security.AccessControlContext;
/*     */ import java.security.AccessController;
/*     */ import sun.awt.AWTAccessor;
/*     */ import sun.awt.AWTAccessor.TrayIconAccessor;
/*     */ import sun.awt.AppContext;
/*     */ import sun.awt.HeadlessToolkit;
/*     */ import sun.awt.SunToolkit;
/*     */ 
/*     */ public class TrayIcon
/*     */ {
/*     */   private Image image;
/*     */   private String tooltip;
/*     */   private PopupMenu popup;
/*     */   private boolean autosize;
/*     */   private int id;
/*     */   private String actionCommand;
/*     */   private transient TrayIconPeer peer;
/*     */   transient MouseListener mouseListener;
/*     */   transient MouseMotionListener mouseMotionListener;
/*     */   transient ActionListener actionListener;
/* 107 */   private final AccessControlContext acc = AccessController.getContext();
/*     */ 
/*     */   final AccessControlContext getAccessControlContext()
/*     */   {
/* 113 */     if (this.acc == null) {
/* 114 */       throw new SecurityException("TrayIcon is missing AccessControlContext");
/*     */     }
/* 116 */     return this.acc;
/*     */   }
/*     */ 
/*     */   private TrayIcon()
/*     */     throws UnsupportedOperationException, HeadlessException, SecurityException
/*     */   {
/* 139 */     SystemTray.checkSystemTrayAllowed();
/* 140 */     if (GraphicsEnvironment.isHeadless()) {
/* 141 */       throw new HeadlessException();
/*     */     }
/* 143 */     if (!SystemTray.isSupported()) {
/* 144 */       throw new UnsupportedOperationException();
/*     */     }
/* 146 */     SunToolkit.insertTargetMapping(this, AppContext.getAppContext());
/*     */   }
/*     */ 
/*     */   public TrayIcon(Image paramImage)
/*     */   {
/* 168 */     this();
/* 169 */     if (paramImage == null) {
/* 170 */       throw new IllegalArgumentException("creating TrayIcon with null Image");
/*     */     }
/* 172 */     setImage(paramImage);
/*     */   }
/*     */ 
/*     */   public TrayIcon(Image paramImage, String paramString)
/*     */   {
/* 197 */     this(paramImage);
/* 198 */     setToolTip(paramString);
/*     */   }
/*     */ 
/*     */   public TrayIcon(Image paramImage, String paramString, PopupMenu paramPopupMenu)
/*     */   {
/* 227 */     this(paramImage, paramString);
/* 228 */     setPopupMenu(paramPopupMenu);
/*     */   }
/*     */ 
/*     */   public void setImage(Image paramImage)
/*     */   {
/* 254 */     if (paramImage == null) {
/* 255 */       throw new NullPointerException("setting null Image");
/*     */     }
/* 257 */     this.image = paramImage;
/*     */ 
/* 259 */     TrayIconPeer localTrayIconPeer = this.peer;
/* 260 */     if (localTrayIconPeer != null)
/* 261 */       localTrayIconPeer.updateImage();
/*     */   }
/*     */ 
/*     */   public Image getImage()
/*     */   {
/* 273 */     return this.image;
/*     */   }
/*     */ 
/*     */   public void setPopupMenu(PopupMenu paramPopupMenu)
/*     */   {
/* 303 */     if (paramPopupMenu == this.popup) {
/* 304 */       return;
/*     */     }
/* 306 */     synchronized (TrayIcon.class) {
/* 307 */       if (paramPopupMenu != null) {
/* 308 */         if (paramPopupMenu.isTrayIconPopup) {
/* 309 */           throw new IllegalArgumentException("the PopupMenu is already set for another TrayIcon");
/*     */         }
/* 311 */         paramPopupMenu.isTrayIconPopup = true;
/*     */       }
/* 313 */       if (this.popup != null) {
/* 314 */         this.popup.isTrayIconPopup = false;
/*     */       }
/* 316 */       this.popup = paramPopupMenu;
/*     */     }
/*     */   }
/*     */ 
/*     */   public PopupMenu getPopupMenu()
/*     */   {
/* 327 */     return this.popup;
/*     */   }
/*     */ 
/*     */   public void setToolTip(String paramString)
/*     */   {
/* 344 */     this.tooltip = paramString;
/*     */ 
/* 346 */     TrayIconPeer localTrayIconPeer = this.peer;
/* 347 */     if (localTrayIconPeer != null)
/* 348 */       localTrayIconPeer.setToolTip(paramString);
/*     */   }
/*     */ 
/*     */   public String getToolTip()
/*     */   {
/* 360 */     return this.tooltip;
/*     */   }
/*     */ 
/*     */   public void setImageAutoSize(boolean paramBoolean)
/*     */   {
/* 382 */     this.autosize = paramBoolean;
/*     */ 
/* 384 */     TrayIconPeer localTrayIconPeer = this.peer;
/* 385 */     if (localTrayIconPeer != null)
/* 386 */       localTrayIconPeer.updateImage();
/*     */   }
/*     */ 
/*     */   public boolean isImageAutoSize()
/*     */   {
/* 398 */     return this.autosize;
/*     */   }
/*     */ 
/*     */   public synchronized void addMouseListener(MouseListener paramMouseListener)
/*     */   {
/* 422 */     if (paramMouseListener == null) {
/* 423 */       return;
/*     */     }
/* 425 */     this.mouseListener = AWTEventMulticaster.add(this.mouseListener, paramMouseListener);
/*     */   }
/*     */ 
/*     */   public synchronized void removeMouseListener(MouseListener paramMouseListener)
/*     */   {
/* 441 */     if (paramMouseListener == null) {
/* 442 */       return;
/*     */     }
/* 444 */     this.mouseListener = AWTEventMulticaster.remove(this.mouseListener, paramMouseListener);
/*     */   }
/*     */ 
/*     */   public synchronized MouseListener[] getMouseListeners()
/*     */   {
/* 460 */     return (MouseListener[])AWTEventMulticaster.getListeners(this.mouseListener, MouseListener.class);
/*     */   }
/*     */ 
/*     */   public synchronized void addMouseMotionListener(MouseMotionListener paramMouseMotionListener)
/*     */   {
/* 483 */     if (paramMouseMotionListener == null) {
/* 484 */       return;
/*     */     }
/* 486 */     this.mouseMotionListener = AWTEventMulticaster.add(this.mouseMotionListener, paramMouseMotionListener);
/*     */   }
/*     */ 
/*     */   public synchronized void removeMouseMotionListener(MouseMotionListener paramMouseMotionListener)
/*     */   {
/* 502 */     if (paramMouseMotionListener == null) {
/* 503 */       return;
/*     */     }
/* 505 */     this.mouseMotionListener = AWTEventMulticaster.remove(this.mouseMotionListener, paramMouseMotionListener);
/*     */   }
/*     */ 
/*     */   public synchronized MouseMotionListener[] getMouseMotionListeners()
/*     */   {
/* 521 */     return (MouseMotionListener[])AWTEventMulticaster.getListeners(this.mouseMotionListener, MouseMotionListener.class);
/*     */   }
/*     */ 
/*     */   public String getActionCommand()
/*     */   {
/* 532 */     return this.actionCommand;
/*     */   }
/*     */ 
/*     */   public void setActionCommand(String paramString)
/*     */   {
/* 547 */     this.actionCommand = paramString;
/*     */   }
/*     */ 
/*     */   public synchronized void addActionListener(ActionListener paramActionListener)
/*     */   {
/* 569 */     if (paramActionListener == null) {
/* 570 */       return;
/*     */     }
/* 572 */     this.actionListener = AWTEventMulticaster.add(this.actionListener, paramActionListener);
/*     */   }
/*     */ 
/*     */   public synchronized void removeActionListener(ActionListener paramActionListener)
/*     */   {
/* 589 */     if (paramActionListener == null) {
/* 590 */       return;
/*     */     }
/* 592 */     this.actionListener = AWTEventMulticaster.remove(this.actionListener, paramActionListener);
/*     */   }
/*     */ 
/*     */   public synchronized ActionListener[] getActionListeners()
/*     */   {
/* 608 */     return (ActionListener[])AWTEventMulticaster.getListeners(this.actionListener, ActionListener.class);
/*     */   }
/*     */ 
/*     */   public void displayMessage(String paramString1, String paramString2, MessageType paramMessageType)
/*     */   {
/* 656 */     if ((paramString1 == null) && (paramString2 == null)) {
/* 657 */       throw new NullPointerException("displaying the message with both caption and text being null");
/*     */     }
/*     */ 
/* 660 */     TrayIconPeer localTrayIconPeer = this.peer;
/* 661 */     if (localTrayIconPeer != null)
/* 662 */       localTrayIconPeer.displayMessage(paramString1, paramString2, paramMessageType.name());
/*     */   }
/*     */ 
/*     */   public Dimension getSize()
/*     */   {
/* 678 */     return SystemTray.getSystemTray().getTrayIconSize();
/*     */   }
/*     */ 
/*     */   void addNotify()
/*     */     throws AWTException
/*     */   {
/* 687 */     synchronized (this) {
/* 688 */       if (this.peer == null) {
/* 689 */         Toolkit localToolkit = Toolkit.getDefaultToolkit();
/* 690 */         if ((localToolkit instanceof SunToolkit))
/* 691 */           this.peer = ((SunToolkit)Toolkit.getDefaultToolkit()).createTrayIcon(this);
/* 692 */         else if ((localToolkit instanceof HeadlessToolkit)) {
/* 693 */           this.peer = ((HeadlessToolkit)Toolkit.getDefaultToolkit()).createTrayIcon(this);
/*     */         }
/*     */       }
/*     */     }
/* 697 */     this.peer.setToolTip(this.tooltip);
/*     */   }
/*     */ 
/*     */   void removeNotify() {
/* 701 */     TrayIconPeer localTrayIconPeer = null;
/* 702 */     synchronized (this) {
/* 703 */       localTrayIconPeer = this.peer;
/* 704 */       this.peer = null;
/*     */     }
/* 706 */     if (localTrayIconPeer != null)
/* 707 */       localTrayIconPeer.dispose();
/*     */   }
/*     */ 
/*     */   void setID(int paramInt)
/*     */   {
/* 712 */     this.id = paramInt;
/*     */   }
/*     */ 
/*     */   int getID() {
/* 716 */     return this.id;
/*     */   }
/*     */ 
/*     */   void dispatchEvent(AWTEvent paramAWTEvent) {
/* 720 */     EventQueue.setCurrentEventAndMostRecentTime(paramAWTEvent);
/* 721 */     Toolkit.getDefaultToolkit().notifyAWTEventListeners(paramAWTEvent);
/* 722 */     processEvent(paramAWTEvent);
/*     */   }
/*     */ 
/*     */   void processEvent(AWTEvent paramAWTEvent) {
/* 726 */     if ((paramAWTEvent instanceof MouseEvent))
/* 727 */       switch (paramAWTEvent.getID()) {
/*     */       case 500:
/*     */       case 501:
/*     */       case 502:
/* 731 */         processMouseEvent((MouseEvent)paramAWTEvent);
/* 732 */         break;
/*     */       case 503:
/* 734 */         processMouseMotionEvent((MouseEvent)paramAWTEvent);
/* 735 */         break;
/*     */       default:
/* 737 */         return;
/*     */       }
/* 739 */     else if ((paramAWTEvent instanceof ActionEvent))
/* 740 */       processActionEvent((ActionEvent)paramAWTEvent);
/*     */   }
/*     */ 
/*     */   void processMouseEvent(MouseEvent paramMouseEvent)
/*     */   {
/* 745 */     MouseListener localMouseListener = this.mouseListener;
/*     */ 
/* 747 */     if (localMouseListener != null) {
/* 748 */       int i = paramMouseEvent.getID();
/* 749 */       switch (i) {
/*     */       case 501:
/* 751 */         localMouseListener.mousePressed(paramMouseEvent);
/* 752 */         break;
/*     */       case 502:
/* 754 */         localMouseListener.mouseReleased(paramMouseEvent);
/* 755 */         break;
/*     */       case 500:
/* 757 */         localMouseListener.mouseClicked(paramMouseEvent);
/* 758 */         break;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   void processMouseMotionEvent(MouseEvent paramMouseEvent)
/*     */   {
/* 766 */     MouseMotionListener localMouseMotionListener = this.mouseMotionListener;
/* 767 */     if ((localMouseMotionListener != null) && (paramMouseEvent.getID() == 503))
/*     */     {
/* 770 */       localMouseMotionListener.mouseMoved(paramMouseEvent);
/*     */     }
/*     */   }
/*     */ 
/*     */   void processActionEvent(ActionEvent paramActionEvent) {
/* 775 */     ActionListener localActionListener = this.actionListener;
/* 776 */     if (localActionListener != null)
/* 777 */       localActionListener.actionPerformed(paramActionEvent);
/*     */   }
/*     */ 
/*     */   private static native void initIDs();
/*     */ 
/*     */   static
/*     */   {
/* 120 */     Toolkit.loadLibraries();
/* 121 */     if (!GraphicsEnvironment.isHeadless()) {
/* 122 */       initIDs();
/*     */     }
/*     */ 
/* 125 */     AWTAccessor.setTrayIconAccessor(new AWTAccessor.TrayIconAccessor()
/*     */     {
/*     */       public void addNotify(TrayIcon paramAnonymousTrayIcon) throws AWTException {
/* 128 */         paramAnonymousTrayIcon.addNotify();
/*     */       }
/*     */       public void removeNotify(TrayIcon paramAnonymousTrayIcon) {
/* 131 */         paramAnonymousTrayIcon.removeNotify();
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   public static enum MessageType
/*     */   {
/* 622 */     ERROR, 
/*     */ 
/* 624 */     WARNING, 
/*     */ 
/* 626 */     INFO, 
/*     */ 
/* 628 */     NONE;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.TrayIcon
 * JD-Core Version:    0.6.2
 */