/*     */ package javax.swing;
/*     */ 
/*     */ import java.applet.Applet;
/*     */ import java.applet.Applet.AccessibleApplet;
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Container;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.HeadlessException;
/*     */ import java.awt.LayoutManager;
/*     */ import javax.accessibility.Accessible;
/*     */ import javax.accessibility.AccessibleContext;
/*     */ import sun.awt.SunToolkit;
/*     */ 
/*     */ public class JApplet extends Applet
/*     */   implements Accessible, RootPaneContainer, TransferHandler.HasGetTransferHandler
/*     */ {
/*     */   protected JRootPane rootPane;
/* 111 */   protected boolean rootPaneCheckingEnabled = false;
/*     */   private TransferHandler transferHandler;
/* 542 */   protected AccessibleContext accessibleContext = null;
/*     */ 
/*     */   public JApplet()
/*     */     throws HeadlessException
/*     */   {
/* 132 */     TimerQueue localTimerQueue = TimerQueue.sharedInstance();
/* 133 */     if (localTimerQueue != null) {
/* 134 */       localTimerQueue.startIfNeeded();
/*     */     }
/*     */ 
/* 143 */     setForeground(Color.black);
/* 144 */     setBackground(Color.white);
/*     */ 
/* 146 */     setLocale(JComponent.getDefaultLocale());
/* 147 */     setLayout(new BorderLayout());
/* 148 */     setRootPane(createRootPane());
/* 149 */     setRootPaneCheckingEnabled(true);
/*     */ 
/* 151 */     setFocusTraversalPolicyProvider(true);
/* 152 */     SunToolkit.checkAndSetPolicy(this);
/*     */ 
/* 154 */     enableEvents(8L);
/*     */   }
/*     */ 
/*     */   protected JRootPane createRootPane()
/*     */   {
/* 160 */     JRootPane localJRootPane = new JRootPane();
/*     */ 
/* 165 */     localJRootPane.setOpaque(true);
/* 166 */     return localJRootPane;
/*     */   }
/*     */ 
/*     */   public void setTransferHandler(TransferHandler paramTransferHandler)
/*     */   {
/* 203 */     TransferHandler localTransferHandler = this.transferHandler;
/* 204 */     this.transferHandler = paramTransferHandler;
/* 205 */     SwingUtilities.installSwingDropTargetAsNecessary(this, this.transferHandler);
/* 206 */     firePropertyChange("transferHandler", localTransferHandler, paramTransferHandler);
/*     */   }
/*     */ 
/*     */   public TransferHandler getTransferHandler()
/*     */   {
/* 219 */     return this.transferHandler;
/*     */   }
/*     */ 
/*     */   public void update(Graphics paramGraphics)
/*     */   {
/* 227 */     paint(paramGraphics);
/*     */   }
/*     */ 
/*     */   public void setJMenuBar(JMenuBar paramJMenuBar)
/*     */   {
/* 241 */     getRootPane().setMenuBar(paramJMenuBar);
/*     */   }
/*     */ 
/*     */   public JMenuBar getJMenuBar()
/*     */   {
/* 250 */     return getRootPane().getMenuBar();
/*     */   }
/*     */ 
/*     */   protected boolean isRootPaneCheckingEnabled()
/*     */   {
/* 267 */     return this.rootPaneCheckingEnabled;
/*     */   }
/*     */ 
/*     */   protected void setRootPaneCheckingEnabled(boolean paramBoolean)
/*     */   {
/* 288 */     this.rootPaneCheckingEnabled = paramBoolean;
/*     */   }
/*     */ 
/*     */   protected void addImpl(Component paramComponent, Object paramObject, int paramInt)
/*     */   {
/* 313 */     if (isRootPaneCheckingEnabled()) {
/* 314 */       getContentPane().add(paramComponent, paramObject, paramInt);
/*     */     }
/*     */     else
/* 317 */       super.addImpl(paramComponent, paramObject, paramInt);
/*     */   }
/*     */ 
/*     */   public void remove(Component paramComponent)
/*     */   {
/* 334 */     if (paramComponent == this.rootPane)
/* 335 */       super.remove(paramComponent);
/*     */     else
/* 337 */       getContentPane().remove(paramComponent);
/*     */   }
/*     */ 
/*     */   public void setLayout(LayoutManager paramLayoutManager)
/*     */   {
/* 354 */     if (isRootPaneCheckingEnabled()) {
/* 355 */       getContentPane().setLayout(paramLayoutManager);
/*     */     }
/*     */     else
/* 358 */       super.setLayout(paramLayoutManager);
/*     */   }
/*     */ 
/*     */   public JRootPane getRootPane()
/*     */   {
/* 370 */     return this.rootPane;
/*     */   }
/*     */ 
/*     */   protected void setRootPane(JRootPane paramJRootPane)
/*     */   {
/* 385 */     if (this.rootPane != null) {
/* 386 */       remove(this.rootPane);
/*     */     }
/* 388 */     this.rootPane = paramJRootPane;
/* 389 */     if (this.rootPane != null) {
/* 390 */       boolean bool = isRootPaneCheckingEnabled();
/*     */       try {
/* 392 */         setRootPaneCheckingEnabled(false);
/* 393 */         add(this.rootPane, "Center");
/*     */       }
/*     */       finally {
/* 396 */         setRootPaneCheckingEnabled(bool);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public Container getContentPane()
/*     */   {
/* 409 */     return getRootPane().getContentPane();
/*     */   }
/*     */ 
/*     */   public void setContentPane(Container paramContainer)
/*     */   {
/* 427 */     getRootPane().setContentPane(paramContainer);
/*     */   }
/*     */ 
/*     */   public JLayeredPane getLayeredPane()
/*     */   {
/* 439 */     return getRootPane().getLayeredPane();
/*     */   }
/*     */ 
/*     */   public void setLayeredPane(JLayeredPane paramJLayeredPane)
/*     */   {
/* 454 */     getRootPane().setLayeredPane(paramJLayeredPane);
/*     */   }
/*     */ 
/*     */   public Component getGlassPane()
/*     */   {
/* 464 */     return getRootPane().getGlassPane();
/*     */   }
/*     */ 
/*     */   public void setGlassPane(Component paramComponent)
/*     */   {
/* 480 */     getRootPane().setGlassPane(paramComponent);
/*     */   }
/*     */ 
/*     */   public Graphics getGraphics()
/*     */   {
/* 489 */     JComponent.getGraphicsInvoked(this);
/* 490 */     return super.getGraphics();
/*     */   }
/*     */ 
/*     */   public void repaint(long paramLong, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/* 507 */     if (RepaintManager.HANDLE_TOP_LEVEL_PAINT) {
/* 508 */       RepaintManager.currentManager(this).addDirtyRegion(this, paramInt1, paramInt2, paramInt3, paramInt4);
/*     */     }
/*     */     else
/*     */     {
/* 512 */       super.repaint(paramLong, paramInt1, paramInt2, paramInt3, paramInt4);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected String paramString()
/*     */   {
/* 526 */     String str1 = this.rootPane != null ? this.rootPane.toString() : "";
/*     */ 
/* 528 */     String str2 = this.rootPaneCheckingEnabled ? "true" : "false";
/*     */ 
/* 531 */     return super.paramString() + ",rootPane=" + str1 + ",rootPaneCheckingEnabled=" + str2;
/*     */   }
/*     */ 
/*     */   public AccessibleContext getAccessibleContext()
/*     */   {
/* 554 */     if (this.accessibleContext == null) {
/* 555 */       this.accessibleContext = new AccessibleJApplet();
/*     */     }
/* 557 */     return this.accessibleContext;
/*     */   }
/*     */ 
/*     */   protected class AccessibleJApplet extends Applet.AccessibleApplet
/*     */   {
/*     */     protected AccessibleJApplet()
/*     */     {
/* 564 */       super();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.JApplet
 * JD-Core Version:    0.6.2
 */