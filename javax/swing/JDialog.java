/*      */ package javax.swing;
/*      */ 
/*      */ import java.awt.Component;
/*      */ import java.awt.Container;
/*      */ import java.awt.Dialog;
/*      */ import java.awt.Dialog.AccessibleAWTDialog;
/*      */ import java.awt.Dialog.ModalityType;
/*      */ import java.awt.Frame;
/*      */ import java.awt.Graphics;
/*      */ import java.awt.GraphicsConfiguration;
/*      */ import java.awt.LayoutManager;
/*      */ import java.awt.Window;
/*      */ import java.awt.event.WindowEvent;
/*      */ import java.awt.event.WindowListener;
/*      */ import javax.accessibility.Accessible;
/*      */ import javax.accessibility.AccessibleContext;
/*      */ import javax.accessibility.AccessibleState;
/*      */ import javax.accessibility.AccessibleStateSet;
/*      */ import sun.awt.SunToolkit;
/*      */ 
/*      */ public class JDialog extends Dialog
/*      */   implements WindowConstants, Accessible, RootPaneContainer, TransferHandler.HasGetTransferHandler
/*      */ {
/*  106 */   private static final Object defaultLookAndFeelDecoratedKey = new StringBuffer("JDialog.defaultLookAndFeelDecorated");
/*      */ 
/*  109 */   private int defaultCloseOperation = 1;
/*      */   protected JRootPane rootPane;
/*  126 */   protected boolean rootPaneCheckingEnabled = false;
/*      */   private TransferHandler transferHandler;
/* 1224 */   protected AccessibleContext accessibleContext = null;
/*      */ 
/*      */   public JDialog()
/*      */   {
/*  153 */     this((Frame)null, false);
/*      */   }
/*      */ 
/*      */   public JDialog(Frame paramFrame)
/*      */   {
/*  178 */     this(paramFrame, false);
/*      */   }
/*      */ 
/*      */   public JDialog(Frame paramFrame, boolean paramBoolean)
/*      */   {
/*  205 */     this(paramFrame, "", paramBoolean);
/*      */   }
/*      */ 
/*      */   public JDialog(Frame paramFrame, String paramString)
/*      */   {
/*  232 */     this(paramFrame, paramString, false);
/*      */   }
/*      */ 
/*      */   public JDialog(Frame paramFrame, String paramString, boolean paramBoolean)
/*      */   {
/*  271 */     super(paramFrame == null ? SwingUtilities.getSharedOwnerFrame() : paramFrame, paramString, paramBoolean);
/*      */ 
/*  273 */     if (paramFrame == null) {
/*  274 */       WindowListener localWindowListener = SwingUtilities.getSharedOwnerFrameShutdownListener();
/*      */ 
/*  276 */       addWindowListener(localWindowListener);
/*      */     }
/*  278 */     dialogInit();
/*      */   }
/*      */ 
/*      */   public JDialog(Frame paramFrame, String paramString, boolean paramBoolean, GraphicsConfiguration paramGraphicsConfiguration)
/*      */   {
/*  322 */     super(paramFrame == null ? SwingUtilities.getSharedOwnerFrame() : paramFrame, paramString, paramBoolean, paramGraphicsConfiguration);
/*      */ 
/*  324 */     if (paramFrame == null) {
/*  325 */       WindowListener localWindowListener = SwingUtilities.getSharedOwnerFrameShutdownListener();
/*      */ 
/*  327 */       addWindowListener(localWindowListener);
/*      */     }
/*  329 */     dialogInit();
/*      */   }
/*      */ 
/*      */   public JDialog(Dialog paramDialog)
/*      */   {
/*  347 */     this(paramDialog, false);
/*      */   }
/*      */ 
/*      */   public JDialog(Dialog paramDialog, boolean paramBoolean)
/*      */   {
/*  373 */     this(paramDialog, "", paramBoolean);
/*      */   }
/*      */ 
/*      */   public JDialog(Dialog paramDialog, String paramString)
/*      */   {
/*  393 */     this(paramDialog, paramString, false);
/*      */   }
/*      */ 
/*      */   public JDialog(Dialog paramDialog, String paramString, boolean paramBoolean)
/*      */   {
/*  421 */     super(paramDialog, paramString, paramBoolean);
/*  422 */     dialogInit();
/*      */   }
/*      */ 
/*      */   public JDialog(Dialog paramDialog, String paramString, boolean paramBoolean, GraphicsConfiguration paramGraphicsConfiguration)
/*      */   {
/*  460 */     super(paramDialog, paramString, paramBoolean, paramGraphicsConfiguration);
/*  461 */     dialogInit();
/*      */   }
/*      */ 
/*      */   public JDialog(Window paramWindow)
/*      */   {
/*  488 */     this(paramWindow, Dialog.ModalityType.MODELESS);
/*      */   }
/*      */ 
/*      */   public JDialog(Window paramWindow, Dialog.ModalityType paramModalityType)
/*      */   {
/*  524 */     this(paramWindow, "", paramModalityType);
/*      */   }
/*      */ 
/*      */   public JDialog(Window paramWindow, String paramString)
/*      */   {
/*  553 */     this(paramWindow, paramString, Dialog.ModalityType.MODELESS);
/*      */   }
/*      */ 
/*      */   public JDialog(Window paramWindow, String paramString, Dialog.ModalityType paramModalityType)
/*      */   {
/*  591 */     super(paramWindow, paramString, paramModalityType);
/*  592 */     dialogInit();
/*      */   }
/*      */ 
/*      */   public JDialog(Window paramWindow, String paramString, Dialog.ModalityType paramModalityType, GraphicsConfiguration paramGraphicsConfiguration)
/*      */   {
/*  637 */     super(paramWindow, paramString, paramModalityType, paramGraphicsConfiguration);
/*  638 */     dialogInit();
/*      */   }
/*      */ 
/*      */   protected void dialogInit()
/*      */   {
/*  645 */     enableEvents(72L);
/*  646 */     setLocale(JComponent.getDefaultLocale());
/*  647 */     setRootPane(createRootPane());
/*  648 */     setRootPaneCheckingEnabled(true);
/*  649 */     if (isDefaultLookAndFeelDecorated()) {
/*  650 */       boolean bool = UIManager.getLookAndFeel().getSupportsWindowDecorations();
/*      */ 
/*  652 */       if (bool) {
/*  653 */         setUndecorated(true);
/*  654 */         getRootPane().setWindowDecorationStyle(2);
/*      */       }
/*      */     }
/*  657 */     SunToolkit.checkAndSetPolicy(this);
/*      */   }
/*      */ 
/*      */   protected JRootPane createRootPane()
/*      */   {
/*  665 */     JRootPane localJRootPane = new JRootPane();
/*      */ 
/*  670 */     localJRootPane.setOpaque(true);
/*  671 */     return localJRootPane;
/*      */   }
/*      */ 
/*      */   protected void processWindowEvent(WindowEvent paramWindowEvent)
/*      */   {
/*  681 */     super.processWindowEvent(paramWindowEvent);
/*      */ 
/*  683 */     if (paramWindowEvent.getID() == 201)
/*  684 */       switch (this.defaultCloseOperation) {
/*      */       case 1:
/*  686 */         setVisible(false);
/*  687 */         break;
/*      */       case 2:
/*  689 */         dispose();
/*  690 */         break;
/*      */       case 0:
/*      */       }
/*      */   }
/*      */ 
/*      */   public void setDefaultCloseOperation(int paramInt)
/*      */   {
/*  750 */     if ((paramInt != 0) && (paramInt != 1) && (paramInt != 2))
/*      */     {
/*  753 */       throw new IllegalArgumentException("defaultCloseOperation must be one of: DO_NOTHING_ON_CLOSE, HIDE_ON_CLOSE, or DISPOSE_ON_CLOSE");
/*      */     }
/*      */ 
/*  756 */     int i = this.defaultCloseOperation;
/*  757 */     this.defaultCloseOperation = paramInt;
/*  758 */     firePropertyChange("defaultCloseOperation", i, paramInt);
/*      */   }
/*      */ 
/*      */   public int getDefaultCloseOperation()
/*      */   {
/*  769 */     return this.defaultCloseOperation;
/*      */   }
/*      */ 
/*      */   public void setTransferHandler(TransferHandler paramTransferHandler)
/*      */   {
/*  806 */     TransferHandler localTransferHandler = this.transferHandler;
/*  807 */     this.transferHandler = paramTransferHandler;
/*  808 */     SwingUtilities.installSwingDropTargetAsNecessary(this, this.transferHandler);
/*  809 */     firePropertyChange("transferHandler", localTransferHandler, paramTransferHandler);
/*      */   }
/*      */ 
/*      */   public TransferHandler getTransferHandler()
/*      */   {
/*  822 */     return this.transferHandler;
/*      */   }
/*      */ 
/*      */   public void update(Graphics paramGraphics)
/*      */   {
/*  832 */     paint(paramGraphics);
/*      */   }
/*      */ 
/*      */   public void setJMenuBar(JMenuBar paramJMenuBar)
/*      */   {
/*  847 */     getRootPane().setMenuBar(paramJMenuBar);
/*      */   }
/*      */ 
/*      */   public JMenuBar getJMenuBar()
/*      */   {
/*  856 */     return getRootPane().getMenuBar();
/*      */   }
/*      */ 
/*      */   protected boolean isRootPaneCheckingEnabled()
/*      */   {
/*  873 */     return this.rootPaneCheckingEnabled;
/*      */   }
/*      */ 
/*      */   protected void setRootPaneCheckingEnabled(boolean paramBoolean)
/*      */   {
/*  894 */     this.rootPaneCheckingEnabled = paramBoolean;
/*      */   }
/*      */ 
/*      */   protected void addImpl(Component paramComponent, Object paramObject, int paramInt)
/*      */   {
/*  918 */     if (isRootPaneCheckingEnabled()) {
/*  919 */       getContentPane().add(paramComponent, paramObject, paramInt);
/*      */     }
/*      */     else
/*  922 */       super.addImpl(paramComponent, paramObject, paramInt);
/*      */   }
/*      */ 
/*      */   public void remove(Component paramComponent)
/*      */   {
/*  939 */     if (paramComponent == this.rootPane)
/*  940 */       super.remove(paramComponent);
/*      */     else
/*  942 */       getContentPane().remove(paramComponent);
/*      */   }
/*      */ 
/*      */   public void setLayout(LayoutManager paramLayoutManager)
/*      */   {
/*  959 */     if (isRootPaneCheckingEnabled()) {
/*  960 */       getContentPane().setLayout(paramLayoutManager);
/*      */     }
/*      */     else
/*  963 */       super.setLayout(paramLayoutManager);
/*      */   }
/*      */ 
/*      */   public JRootPane getRootPane()
/*      */   {
/*  975 */     return this.rootPane;
/*      */   }
/*      */ 
/*      */   protected void setRootPane(JRootPane paramJRootPane)
/*      */   {
/*  992 */     if (this.rootPane != null) {
/*  993 */       remove(this.rootPane);
/*      */     }
/*  995 */     this.rootPane = paramJRootPane;
/*  996 */     if (this.rootPane != null) {
/*  997 */       boolean bool = isRootPaneCheckingEnabled();
/*      */       try {
/*  999 */         setRootPaneCheckingEnabled(false);
/* 1000 */         add(this.rootPane, "Center");
/*      */       }
/*      */       finally {
/* 1003 */         setRootPaneCheckingEnabled(bool);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public Container getContentPane()
/*      */   {
/* 1018 */     return getRootPane().getContentPane();
/*      */   }
/*      */ 
/*      */   public void setContentPane(Container paramContainer)
/*      */   {
/* 1045 */     getRootPane().setContentPane(paramContainer);
/*      */   }
/*      */ 
/*      */   public JLayeredPane getLayeredPane()
/*      */   {
/* 1057 */     return getRootPane().getLayeredPane();
/*      */   }
/*      */ 
/*      */   public void setLayeredPane(JLayeredPane paramJLayeredPane)
/*      */   {
/* 1076 */     getRootPane().setLayeredPane(paramJLayeredPane);
/*      */   }
/*      */ 
/*      */   public Component getGlassPane()
/*      */   {
/* 1088 */     return getRootPane().getGlassPane();
/*      */   }
/*      */ 
/*      */   public void setGlassPane(Component paramComponent)
/*      */   {
/* 1104 */     getRootPane().setGlassPane(paramComponent);
/*      */   }
/*      */ 
/*      */   public Graphics getGraphics()
/*      */   {
/* 1113 */     JComponent.getGraphicsInvoked(this);
/* 1114 */     return super.getGraphics();
/*      */   }
/*      */ 
/*      */   public void repaint(long paramLong, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/* 1131 */     if (RepaintManager.HANDLE_TOP_LEVEL_PAINT) {
/* 1132 */       RepaintManager.currentManager(this).addDirtyRegion(this, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */     else
/*      */     {
/* 1136 */       super.repaint(paramLong, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */   }
/*      */ 
/*      */   public static void setDefaultLookAndFeelDecorated(boolean paramBoolean)
/*      */   {
/* 1165 */     if (paramBoolean)
/* 1166 */       SwingUtilities.appContextPut(defaultLookAndFeelDecoratedKey, Boolean.TRUE);
/*      */     else
/* 1168 */       SwingUtilities.appContextPut(defaultLookAndFeelDecoratedKey, Boolean.FALSE);
/*      */   }
/*      */ 
/*      */   public static boolean isDefaultLookAndFeelDecorated()
/*      */   {
/* 1181 */     Boolean localBoolean = (Boolean)SwingUtilities.appContextGet(defaultLookAndFeelDecoratedKey);
/*      */ 
/* 1183 */     if (localBoolean == null) {
/* 1184 */       localBoolean = Boolean.FALSE;
/*      */     }
/* 1186 */     return localBoolean.booleanValue();
/*      */   }
/*      */ 
/*      */   protected String paramString()
/*      */   {
/*      */     String str1;
/* 1201 */     if (this.defaultCloseOperation == 1)
/* 1202 */       str1 = "HIDE_ON_CLOSE";
/* 1203 */     else if (this.defaultCloseOperation == 2)
/* 1204 */       str1 = "DISPOSE_ON_CLOSE";
/* 1205 */     else if (this.defaultCloseOperation == 0)
/* 1206 */       str1 = "DO_NOTHING_ON_CLOSE";
/* 1207 */     else str1 = "";
/* 1208 */     String str2 = this.rootPane != null ? this.rootPane.toString() : "";
/*      */ 
/* 1210 */     String str3 = this.rootPaneCheckingEnabled ? "true" : "false";
/*      */ 
/* 1213 */     return super.paramString() + ",defaultCloseOperation=" + str1 + ",rootPane=" + str2 + ",rootPaneCheckingEnabled=" + str3;
/*      */   }
/*      */ 
/*      */   public AccessibleContext getAccessibleContext()
/*      */   {
/* 1236 */     if (this.accessibleContext == null) {
/* 1237 */       this.accessibleContext = new AccessibleJDialog();
/*      */     }
/* 1239 */     return this.accessibleContext;
/*      */   }
/*      */ 
/*      */   protected class AccessibleJDialog extends Dialog.AccessibleAWTDialog
/*      */   {
/*      */     protected AccessibleJDialog()
/*      */     {
/* 1248 */       super();
/*      */     }
/*      */ 
/*      */     public String getAccessibleName()
/*      */     {
/* 1259 */       if (this.accessibleName != null) {
/* 1260 */         return this.accessibleName;
/*      */       }
/* 1262 */       if (JDialog.this.getTitle() == null) {
/* 1263 */         return super.getAccessibleName();
/*      */       }
/* 1265 */       return JDialog.this.getTitle();
/*      */     }
/*      */ 
/*      */     public AccessibleStateSet getAccessibleStateSet()
/*      */     {
/* 1278 */       AccessibleStateSet localAccessibleStateSet = super.getAccessibleStateSet();
/*      */ 
/* 1280 */       if (JDialog.this.isResizable()) {
/* 1281 */         localAccessibleStateSet.add(AccessibleState.RESIZABLE);
/*      */       }
/* 1283 */       if (JDialog.this.getFocusOwner() != null) {
/* 1284 */         localAccessibleStateSet.add(AccessibleState.ACTIVE);
/*      */       }
/* 1286 */       if (JDialog.this.isModal()) {
/* 1287 */         localAccessibleStateSet.add(AccessibleState.MODAL);
/*      */       }
/* 1289 */       return localAccessibleStateSet;
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.JDialog
 * JD-Core Version:    0.6.2
 */