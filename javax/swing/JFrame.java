/*     */ package javax.swing;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.Container;
/*     */ import java.awt.Frame;
/*     */ import java.awt.Frame.AccessibleAWTFrame;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.GraphicsConfiguration;
/*     */ import java.awt.HeadlessException;
/*     */ import java.awt.Image;
/*     */ import java.awt.LayoutManager;
/*     */ import java.awt.event.WindowEvent;
/*     */ import javax.accessibility.Accessible;
/*     */ import javax.accessibility.AccessibleContext;
/*     */ import javax.accessibility.AccessibleState;
/*     */ import javax.accessibility.AccessibleStateSet;
/*     */ import sun.awt.SunToolkit;
/*     */ 
/*     */ public class JFrame extends Frame
/*     */   implements WindowConstants, Accessible, RootPaneContainer, TransferHandler.HasGetTransferHandler
/*     */ {
/*     */   public static final int EXIT_ON_CLOSE = 3;
/* 133 */   private static final Object defaultLookAndFeelDecoratedKey = new StringBuffer("JFrame.defaultLookAndFeelDecorated");
/*     */ 
/* 136 */   private int defaultCloseOperation = 1;
/*     */   private TransferHandler transferHandler;
/*     */   protected JRootPane rootPane;
/* 163 */   protected boolean rootPaneCheckingEnabled = false;
/*     */ 
/* 874 */   protected AccessibleContext accessibleContext = null;
/*     */ 
/*     */   public JFrame()
/*     */     throws HeadlessException
/*     */   {
/* 181 */     frameInit();
/*     */   }
/*     */ 
/*     */   public JFrame(GraphicsConfiguration paramGraphicsConfiguration)
/*     */   {
/* 204 */     super(paramGraphicsConfiguration);
/* 205 */     frameInit();
/*     */   }
/*     */ 
/*     */   public JFrame(String paramString)
/*     */     throws HeadlessException
/*     */   {
/* 224 */     super(paramString);
/* 225 */     frameInit();
/*     */   }
/*     */ 
/*     */   public JFrame(String paramString, GraphicsConfiguration paramGraphicsConfiguration)
/*     */   {
/* 250 */     super(paramString, paramGraphicsConfiguration);
/* 251 */     frameInit();
/*     */   }
/*     */ 
/*     */   protected void frameInit()
/*     */   {
/* 256 */     enableEvents(72L);
/* 257 */     setLocale(JComponent.getDefaultLocale());
/* 258 */     setRootPane(createRootPane());
/* 259 */     setBackground(UIManager.getColor("control"));
/* 260 */     setRootPaneCheckingEnabled(true);
/* 261 */     if (isDefaultLookAndFeelDecorated()) {
/* 262 */       boolean bool = UIManager.getLookAndFeel().getSupportsWindowDecorations();
/*     */ 
/* 264 */       if (bool) {
/* 265 */         setUndecorated(true);
/* 266 */         getRootPane().setWindowDecorationStyle(1);
/*     */       }
/*     */     }
/* 269 */     SunToolkit.checkAndSetPolicy(this);
/*     */   }
/*     */ 
/*     */   protected JRootPane createRootPane()
/*     */   {
/* 277 */     JRootPane localJRootPane = new JRootPane();
/*     */ 
/* 282 */     localJRootPane.setOpaque(true);
/* 283 */     return localJRootPane;
/*     */   }
/*     */ 
/*     */   protected void processWindowEvent(WindowEvent paramWindowEvent)
/*     */   {
/* 296 */     super.processWindowEvent(paramWindowEvent);
/*     */ 
/* 298 */     if (paramWindowEvent.getID() == 201)
/* 299 */       switch (this.defaultCloseOperation) {
/*     */       case 1:
/* 301 */         setVisible(false);
/* 302 */         break;
/*     */       case 2:
/* 304 */         dispose();
/* 305 */         break;
/*     */       case 0:
/*     */       default:
/* 308 */         break;
/*     */       case 3:
/* 312 */         System.exit(0);
/*     */       }
/*     */   }
/*     */ 
/*     */   public void setDefaultCloseOperation(int paramInt)
/*     */   {
/* 384 */     if ((paramInt != 0) && (paramInt != 1) && (paramInt != 2) && (paramInt != 3))
/*     */     {
/* 388 */       throw new IllegalArgumentException("defaultCloseOperation must be one of: DO_NOTHING_ON_CLOSE, HIDE_ON_CLOSE, DISPOSE_ON_CLOSE, or EXIT_ON_CLOSE");
/*     */     }
/* 390 */     if (this.defaultCloseOperation != paramInt) {
/* 391 */       if (paramInt == 3) {
/* 392 */         SecurityManager localSecurityManager = System.getSecurityManager();
/* 393 */         if (localSecurityManager != null) {
/* 394 */           localSecurityManager.checkExit(0);
/*     */         }
/*     */       }
/* 397 */       int i = this.defaultCloseOperation;
/* 398 */       this.defaultCloseOperation = paramInt;
/* 399 */       firePropertyChange("defaultCloseOperation", i, paramInt);
/*     */     }
/*     */   }
/*     */ 
/*     */   public int getDefaultCloseOperation()
/*     */   {
/* 412 */     return this.defaultCloseOperation;
/*     */   }
/*     */ 
/*     */   public void setTransferHandler(TransferHandler paramTransferHandler)
/*     */   {
/* 449 */     TransferHandler localTransferHandler = this.transferHandler;
/* 450 */     this.transferHandler = paramTransferHandler;
/* 451 */     SwingUtilities.installSwingDropTargetAsNecessary(this, this.transferHandler);
/* 452 */     firePropertyChange("transferHandler", localTransferHandler, paramTransferHandler);
/*     */   }
/*     */ 
/*     */   public TransferHandler getTransferHandler()
/*     */   {
/* 465 */     return this.transferHandler;
/*     */   }
/*     */ 
/*     */   public void update(Graphics paramGraphics)
/*     */   {
/* 475 */     paint(paramGraphics);
/*     */   }
/*     */ 
/*     */   public void setJMenuBar(JMenuBar paramJMenuBar)
/*     */   {
/* 489 */     getRootPane().setMenuBar(paramJMenuBar);
/*     */   }
/*     */ 
/*     */   public JMenuBar getJMenuBar()
/*     */   {
/* 499 */     return getRootPane().getMenuBar();
/*     */   }
/*     */ 
/*     */   protected boolean isRootPaneCheckingEnabled()
/*     */   {
/* 515 */     return this.rootPaneCheckingEnabled;
/*     */   }
/*     */ 
/*     */   protected void setRootPaneCheckingEnabled(boolean paramBoolean)
/*     */   {
/* 536 */     this.rootPaneCheckingEnabled = paramBoolean;
/*     */   }
/*     */ 
/*     */   protected void addImpl(Component paramComponent, Object paramObject, int paramInt)
/*     */   {
/* 561 */     if (isRootPaneCheckingEnabled()) {
/* 562 */       getContentPane().add(paramComponent, paramObject, paramInt);
/*     */     }
/*     */     else
/* 565 */       super.addImpl(paramComponent, paramObject, paramInt);
/*     */   }
/*     */ 
/*     */   public void remove(Component paramComponent)
/*     */   {
/* 582 */     if (paramComponent == this.rootPane)
/* 583 */       super.remove(paramComponent);
/*     */     else
/* 585 */       getContentPane().remove(paramComponent);
/*     */   }
/*     */ 
/*     */   public void setLayout(LayoutManager paramLayoutManager)
/*     */   {
/* 602 */     if (isRootPaneCheckingEnabled()) {
/* 603 */       getContentPane().setLayout(paramLayoutManager);
/*     */     }
/*     */     else
/* 606 */       super.setLayout(paramLayoutManager);
/*     */   }
/*     */ 
/*     */   public JRootPane getRootPane()
/*     */   {
/* 619 */     return this.rootPane;
/*     */   }
/*     */ 
/*     */   protected void setRootPane(JRootPane paramJRootPane)
/*     */   {
/* 636 */     if (this.rootPane != null) {
/* 637 */       remove(this.rootPane);
/*     */     }
/* 639 */     this.rootPane = paramJRootPane;
/* 640 */     if (this.rootPane != null) {
/* 641 */       boolean bool = isRootPaneCheckingEnabled();
/*     */       try {
/* 643 */         setRootPaneCheckingEnabled(false);
/* 644 */         add(this.rootPane, "Center");
/*     */       }
/*     */       finally {
/* 647 */         setRootPaneCheckingEnabled(bool);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setIconImage(Image paramImage)
/*     */   {
/* 656 */     super.setIconImage(paramImage);
/*     */   }
/*     */ 
/*     */   public Container getContentPane()
/*     */   {
/* 667 */     return getRootPane().getContentPane();
/*     */   }
/*     */ 
/*     */   public void setContentPane(Container paramContainer)
/*     */   {
/* 693 */     getRootPane().setContentPane(paramContainer);
/*     */   }
/*     */ 
/*     */   public JLayeredPane getLayeredPane()
/*     */   {
/* 704 */     return getRootPane().getLayeredPane();
/*     */   }
/*     */ 
/*     */   public void setLayeredPane(JLayeredPane paramJLayeredPane)
/*     */   {
/* 722 */     getRootPane().setLayeredPane(paramJLayeredPane);
/*     */   }
/*     */ 
/*     */   public Component getGlassPane()
/*     */   {
/* 733 */     return getRootPane().getGlassPane();
/*     */   }
/*     */ 
/*     */   public void setGlassPane(Component paramComponent)
/*     */   {
/* 749 */     getRootPane().setGlassPane(paramComponent);
/*     */   }
/*     */ 
/*     */   public Graphics getGraphics()
/*     */   {
/* 758 */     JComponent.getGraphicsInvoked(this);
/* 759 */     return super.getGraphics();
/*     */   }
/*     */ 
/*     */   public void repaint(long paramLong, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/* 776 */     if (RepaintManager.HANDLE_TOP_LEVEL_PAINT) {
/* 777 */       RepaintManager.currentManager(this).addDirtyRegion(this, paramInt1, paramInt2, paramInt3, paramInt4);
/*     */     }
/*     */     else
/*     */     {
/* 781 */       super.repaint(paramLong, paramInt1, paramInt2, paramInt3, paramInt4);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static void setDefaultLookAndFeelDecorated(boolean paramBoolean)
/*     */   {
/* 810 */     if (paramBoolean)
/* 811 */       SwingUtilities.appContextPut(defaultLookAndFeelDecoratedKey, Boolean.TRUE);
/*     */     else
/* 813 */       SwingUtilities.appContextPut(defaultLookAndFeelDecoratedKey, Boolean.FALSE);
/*     */   }
/*     */ 
/*     */   public static boolean isDefaultLookAndFeelDecorated()
/*     */   {
/* 827 */     Boolean localBoolean = (Boolean)SwingUtilities.appContextGet(defaultLookAndFeelDecoratedKey);
/*     */ 
/* 829 */     if (localBoolean == null) {
/* 830 */       localBoolean = Boolean.FALSE;
/*     */     }
/* 832 */     return localBoolean.booleanValue();
/*     */   }
/*     */ 
/*     */   protected String paramString()
/*     */   {
/*     */     String str1;
/* 847 */     if (this.defaultCloseOperation == 1)
/* 848 */       str1 = "HIDE_ON_CLOSE";
/* 849 */     else if (this.defaultCloseOperation == 2)
/* 850 */       str1 = "DISPOSE_ON_CLOSE";
/* 851 */     else if (this.defaultCloseOperation == 0)
/* 852 */       str1 = "DO_NOTHING_ON_CLOSE";
/* 853 */     else if (this.defaultCloseOperation == 3)
/* 854 */       str1 = "EXIT_ON_CLOSE";
/* 855 */     else str1 = "";
/* 856 */     String str2 = this.rootPane != null ? this.rootPane.toString() : "";
/*     */ 
/* 858 */     String str3 = this.rootPaneCheckingEnabled ? "true" : "false";
/*     */ 
/* 861 */     return super.paramString() + ",defaultCloseOperation=" + str1 + ",rootPane=" + str2 + ",rootPaneCheckingEnabled=" + str3;
/*     */   }
/*     */ 
/*     */   public AccessibleContext getAccessibleContext()
/*     */   {
/* 886 */     if (this.accessibleContext == null) {
/* 887 */       this.accessibleContext = new AccessibleJFrame();
/*     */     }
/* 889 */     return this.accessibleContext;
/*     */   }
/*     */ 
/*     */   protected class AccessibleJFrame extends Frame.AccessibleAWTFrame
/*     */   {
/*     */     protected AccessibleJFrame()
/*     */     {
/* 898 */       super();
/*     */     }
/*     */ 
/*     */     public String getAccessibleName()
/*     */     {
/* 908 */       if (this.accessibleName != null) {
/* 909 */         return this.accessibleName;
/*     */       }
/* 911 */       if (JFrame.this.getTitle() == null) {
/* 912 */         return super.getAccessibleName();
/*     */       }
/* 914 */       return JFrame.this.getTitle();
/*     */     }
/*     */ 
/*     */     public AccessibleStateSet getAccessibleStateSet()
/*     */     {
/* 927 */       AccessibleStateSet localAccessibleStateSet = super.getAccessibleStateSet();
/*     */ 
/* 929 */       if (JFrame.this.isResizable()) {
/* 930 */         localAccessibleStateSet.add(AccessibleState.RESIZABLE);
/*     */       }
/* 932 */       if (JFrame.this.getFocusOwner() != null) {
/* 933 */         localAccessibleStateSet.add(AccessibleState.ACTIVE);
/*     */       }
/*     */ 
/* 937 */       return localAccessibleStateSet;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.JFrame
 * JD-Core Version:    0.6.2
 */