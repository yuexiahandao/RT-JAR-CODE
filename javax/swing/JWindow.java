/*     */ package javax.swing;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.Container;
/*     */ import java.awt.Frame;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.GraphicsConfiguration;
/*     */ import java.awt.LayoutManager;
/*     */ import java.awt.Window;
/*     */ import java.awt.Window.AccessibleAWTWindow;
/*     */ import java.awt.event.WindowListener;
/*     */ import javax.accessibility.Accessible;
/*     */ import javax.accessibility.AccessibleContext;
/*     */ import sun.awt.SunToolkit;
/*     */ 
/*     */ public class JWindow extends Window
/*     */   implements Accessible, RootPaneContainer, TransferHandler.HasGetTransferHandler
/*     */ {
/*     */   protected JRootPane rootPane;
/* 116 */   protected boolean rootPaneCheckingEnabled = false;
/*     */   private TransferHandler transferHandler;
/* 641 */   protected AccessibleContext accessibleContext = null;
/*     */ 
/*     */   public JWindow()
/*     */   {
/* 137 */     this((Frame)null);
/*     */   }
/*     */ 
/*     */   public JWindow(GraphicsConfiguration paramGraphicsConfiguration)
/*     */   {
/* 163 */     this(null, paramGraphicsConfiguration);
/* 164 */     super.setFocusableWindowState(false);
/*     */   }
/*     */ 
/*     */   public JWindow(Frame paramFrame)
/*     */   {
/* 185 */     super(paramFrame == null ? SwingUtilities.getSharedOwnerFrame() : paramFrame);
/* 186 */     if (paramFrame == null) {
/* 187 */       WindowListener localWindowListener = SwingUtilities.getSharedOwnerFrameShutdownListener();
/*     */ 
/* 189 */       addWindowListener(localWindowListener);
/*     */     }
/* 191 */     windowInit();
/*     */   }
/*     */ 
/*     */   public JWindow(Window paramWindow)
/*     */   {
/* 211 */     super(paramWindow == null ? SwingUtilities.getSharedOwnerFrame() : paramWindow);
/*     */ 
/* 213 */     if (paramWindow == null) {
/* 214 */       WindowListener localWindowListener = SwingUtilities.getSharedOwnerFrameShutdownListener();
/*     */ 
/* 216 */       addWindowListener(localWindowListener);
/*     */     }
/* 218 */     windowInit();
/*     */   }
/*     */ 
/*     */   public JWindow(Window paramWindow, GraphicsConfiguration paramGraphicsConfiguration)
/*     */   {
/* 249 */     super(paramWindow == null ? SwingUtilities.getSharedOwnerFrame() : paramWindow, paramGraphicsConfiguration);
/*     */ 
/* 251 */     if (paramWindow == null) {
/* 252 */       WindowListener localWindowListener = SwingUtilities.getSharedOwnerFrameShutdownListener();
/*     */ 
/* 254 */       addWindowListener(localWindowListener);
/*     */     }
/* 256 */     windowInit();
/*     */   }
/*     */ 
/*     */   protected void windowInit()
/*     */   {
/* 263 */     setLocale(JComponent.getDefaultLocale());
/* 264 */     setRootPane(createRootPane());
/* 265 */     setRootPaneCheckingEnabled(true);
/* 266 */     SunToolkit.checkAndSetPolicy(this);
/*     */   }
/*     */ 
/*     */   protected JRootPane createRootPane()
/*     */   {
/* 274 */     JRootPane localJRootPane = new JRootPane();
/*     */ 
/* 279 */     localJRootPane.setOpaque(true);
/* 280 */     return localJRootPane;
/*     */   }
/*     */ 
/*     */   protected boolean isRootPaneCheckingEnabled()
/*     */   {
/* 296 */     return this.rootPaneCheckingEnabled;
/*     */   }
/*     */ 
/*     */   public void setTransferHandler(TransferHandler paramTransferHandler)
/*     */   {
/* 333 */     TransferHandler localTransferHandler = this.transferHandler;
/* 334 */     this.transferHandler = paramTransferHandler;
/* 335 */     SwingUtilities.installSwingDropTargetAsNecessary(this, this.transferHandler);
/* 336 */     firePropertyChange("transferHandler", localTransferHandler, paramTransferHandler);
/*     */   }
/*     */ 
/*     */   public TransferHandler getTransferHandler()
/*     */   {
/* 349 */     return this.transferHandler;
/*     */   }
/*     */ 
/*     */   public void update(Graphics paramGraphics)
/*     */   {
/* 359 */     paint(paramGraphics);
/*     */   }
/*     */ 
/*     */   protected void setRootPaneCheckingEnabled(boolean paramBoolean)
/*     */   {
/* 379 */     this.rootPaneCheckingEnabled = paramBoolean;
/*     */   }
/*     */ 
/*     */   protected void addImpl(Component paramComponent, Object paramObject, int paramInt)
/*     */   {
/* 404 */     if (isRootPaneCheckingEnabled()) {
/* 405 */       getContentPane().add(paramComponent, paramObject, paramInt);
/*     */     }
/*     */     else
/* 408 */       super.addImpl(paramComponent, paramObject, paramInt);
/*     */   }
/*     */ 
/*     */   public void remove(Component paramComponent)
/*     */   {
/* 425 */     if (paramComponent == this.rootPane)
/* 426 */       super.remove(paramComponent);
/*     */     else
/* 428 */       getContentPane().remove(paramComponent);
/*     */   }
/*     */ 
/*     */   public void setLayout(LayoutManager paramLayoutManager)
/*     */   {
/* 445 */     if (isRootPaneCheckingEnabled()) {
/* 446 */       getContentPane().setLayout(paramLayoutManager);
/*     */     }
/*     */     else
/* 449 */       super.setLayout(paramLayoutManager);
/*     */   }
/*     */ 
/*     */   public JRootPane getRootPane()
/*     */   {
/* 462 */     return this.rootPane;
/*     */   }
/*     */ 
/*     */   protected void setRootPane(JRootPane paramJRootPane)
/*     */   {
/* 478 */     if (this.rootPane != null) {
/* 479 */       remove(this.rootPane);
/*     */     }
/* 481 */     this.rootPane = paramJRootPane;
/* 482 */     if (this.rootPane != null) {
/* 483 */       boolean bool = isRootPaneCheckingEnabled();
/*     */       try {
/* 485 */         setRootPaneCheckingEnabled(false);
/* 486 */         add(this.rootPane, "Center");
/*     */       }
/*     */       finally {
/* 489 */         setRootPaneCheckingEnabled(bool);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public Container getContentPane()
/*     */   {
/* 504 */     return getRootPane().getContentPane();
/*     */   }
/*     */ 
/*     */   public void setContentPane(Container paramContainer)
/*     */   {
/* 524 */     getRootPane().setContentPane(paramContainer);
/*     */   }
/*     */ 
/*     */   public JLayeredPane getLayeredPane()
/*     */   {
/* 535 */     return getRootPane().getLayeredPane();
/*     */   }
/*     */ 
/*     */   public void setLayeredPane(JLayeredPane paramJLayeredPane)
/*     */   {
/* 554 */     getRootPane().setLayeredPane(paramJLayeredPane);
/*     */   }
/*     */ 
/*     */   public Component getGlassPane()
/*     */   {
/* 565 */     return getRootPane().getGlassPane();
/*     */   }
/*     */ 
/*     */   public void setGlassPane(Component paramComponent)
/*     */   {
/* 581 */     getRootPane().setGlassPane(paramComponent);
/*     */   }
/*     */ 
/*     */   public Graphics getGraphics()
/*     */   {
/* 590 */     JComponent.getGraphicsInvoked(this);
/* 591 */     return super.getGraphics();
/*     */   }
/*     */ 
/*     */   public void repaint(long paramLong, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/* 608 */     if (RepaintManager.HANDLE_TOP_LEVEL_PAINT) {
/* 609 */       RepaintManager.currentManager(this).addDirtyRegion(this, paramInt1, paramInt2, paramInt3, paramInt4);
/*     */     }
/*     */     else
/*     */     {
/* 613 */       super.repaint(paramLong, paramInt1, paramInt2, paramInt3, paramInt4);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected String paramString()
/*     */   {
/* 628 */     String str = this.rootPaneCheckingEnabled ? "true" : "false";
/*     */ 
/* 631 */     return super.paramString() + ",rootPaneCheckingEnabled=" + str;
/*     */   }
/*     */ 
/*     */   public AccessibleContext getAccessibleContext()
/*     */   {
/* 653 */     if (this.accessibleContext == null) {
/* 654 */       this.accessibleContext = new AccessibleJWindow();
/*     */     }
/* 656 */     return this.accessibleContext;
/*     */   }
/*     */ 
/*     */   protected class AccessibleJWindow extends Window.AccessibleAWTWindow
/*     */   {
/*     */     protected AccessibleJWindow()
/*     */     {
/* 666 */       super();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.JWindow
 * JD-Core Version:    0.6.2
 */