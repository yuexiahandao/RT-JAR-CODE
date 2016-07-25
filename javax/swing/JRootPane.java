/*      */ package javax.swing;
/*      */ 
/*      */ import java.awt.BorderLayout;
/*      */ import java.awt.Component;
/*      */ import java.awt.Container;
/*      */ import java.awt.Dimension;
/*      */ import java.awt.IllegalComponentStateException;
/*      */ import java.awt.Insets;
/*      */ import java.awt.LayoutManager;
/*      */ import java.awt.LayoutManager2;
/*      */ import java.awt.Rectangle;
/*      */ import java.awt.event.ActionEvent;
/*      */ import java.io.PrintStream;
/*      */ import java.io.Serializable;
/*      */ import java.security.AccessController;
/*      */ import javax.accessibility.Accessible;
/*      */ import javax.accessibility.AccessibleContext;
/*      */ import javax.accessibility.AccessibleRole;
/*      */ import javax.swing.plaf.RootPaneUI;
/*      */ import sun.awt.AWTAccessor;
/*      */ import sun.awt.AWTAccessor.ComponentAccessor;
/*      */ import sun.security.action.GetBooleanAction;
/*      */ 
/*      */ public class JRootPane extends JComponent
/*      */   implements Accessible
/*      */ {
/*      */   private static final String uiClassID = "RootPaneUI";
/*  351 */   private static final boolean LOG_DISABLE_TRUE_DOUBLE_BUFFERING = ((Boolean)AccessController.doPrivileged(new GetBooleanAction("swing.logDoubleBufferingDisable"))).booleanValue();
/*      */ 
/*  354 */   private static final boolean IGNORE_DISABLE_TRUE_DOUBLE_BUFFERING = ((Boolean)AccessController.doPrivileged(new GetBooleanAction("swing.ignoreDoubleBufferingDisable"))).booleanValue();
/*      */   public static final int NONE = 0;
/*      */   public static final int FRAME = 1;
/*      */   public static final int PLAIN_DIALOG = 2;
/*      */   public static final int INFORMATION_DIALOG = 3;
/*      */   public static final int ERROR_DIALOG = 4;
/*      */   public static final int COLOR_CHOOSER_DIALOG = 5;
/*      */   public static final int FILE_CHOOSER_DIALOG = 6;
/*      */   public static final int QUESTION_DIALOG = 7;
/*      */   public static final int WARNING_DIALOG = 8;
/*      */   private int windowDecorationStyle;
/*      */   protected JMenuBar menuBar;
/*      */   protected Container contentPane;
/*      */   protected JLayeredPane layeredPane;
/*      */   protected Component glassPane;
/*      */   protected JButton defaultButton;
/*      */ 
/*      */   @Deprecated
/*      */   protected DefaultAction defaultPressAction;
/*      */ 
/*      */   @Deprecated
/*      */   protected DefaultAction defaultReleaseAction;
/*  348 */   boolean useTrueDoubleBuffering = true;
/*      */ 
/*      */   public JRootPane()
/*      */   {
/*  365 */     setGlassPane(createGlassPane());
/*  366 */     setLayeredPane(createLayeredPane());
/*  367 */     setContentPane(createContentPane());
/*  368 */     setLayout(createRootLayout());
/*  369 */     setDoubleBuffered(true);
/*  370 */     updateUI();
/*      */   }
/*      */ 
/*      */   public void setDoubleBuffered(boolean paramBoolean)
/*      */   {
/*  378 */     if (isDoubleBuffered() != paramBoolean) {
/*  379 */       super.setDoubleBuffered(paramBoolean);
/*  380 */       RepaintManager.currentManager(this).doubleBufferingChanged(this);
/*      */     }
/*      */   }
/*      */ 
/*      */   public int getWindowDecorationStyle()
/*      */   {
/*  397 */     return this.windowDecorationStyle;
/*      */   }
/*      */ 
/*      */   public void setWindowDecorationStyle(int paramInt)
/*      */   {
/*  438 */     if ((paramInt < 0) || (paramInt > 8))
/*      */     {
/*  440 */       throw new IllegalArgumentException("Invalid decoration style");
/*      */     }
/*  442 */     int i = getWindowDecorationStyle();
/*  443 */     this.windowDecorationStyle = paramInt;
/*  444 */     firePropertyChange("windowDecorationStyle", i, paramInt);
/*      */   }
/*      */ 
/*      */   public RootPaneUI getUI()
/*      */   {
/*  456 */     return (RootPaneUI)this.ui;
/*      */   }
/*      */ 
/*      */   public void setUI(RootPaneUI paramRootPaneUI)
/*      */   {
/*  473 */     super.setUI(paramRootPaneUI);
/*      */   }
/*      */ 
/*      */   public void updateUI()
/*      */   {
/*  483 */     setUI((RootPaneUI)UIManager.getUI(this));
/*      */   }
/*      */ 
/*      */   public String getUIClassID()
/*      */   {
/*  497 */     return "RootPaneUI";
/*      */   }
/*      */ 
/*      */   protected JLayeredPane createLayeredPane()
/*      */   {
/*  507 */     JLayeredPane localJLayeredPane = new JLayeredPane();
/*  508 */     localJLayeredPane.setName(getName() + ".layeredPane");
/*  509 */     return localJLayeredPane;
/*      */   }
/*      */ 
/*      */   protected Container createContentPane()
/*      */   {
/*  520 */     JPanel localJPanel = new JPanel();
/*  521 */     localJPanel.setName(getName() + ".contentPane");
/*  522 */     localJPanel.setLayout(new BorderLayout()
/*      */     {
/*      */       public void addLayoutComponent(Component paramAnonymousComponent, Object paramAnonymousObject)
/*      */       {
/*  528 */         if (paramAnonymousObject == null) {
/*  529 */           paramAnonymousObject = "Center";
/*      */         }
/*  531 */         super.addLayoutComponent(paramAnonymousComponent, paramAnonymousObject);
/*      */       }
/*      */     });
/*  534 */     return localJPanel;
/*      */   }
/*      */ 
/*      */   protected Component createGlassPane()
/*      */   {
/*  545 */     JPanel localJPanel = new JPanel();
/*  546 */     localJPanel.setName(getName() + ".glassPane");
/*  547 */     localJPanel.setVisible(false);
/*  548 */     ((JPanel)localJPanel).setOpaque(false);
/*  549 */     return localJPanel;
/*      */   }
/*      */ 
/*      */   protected LayoutManager createRootLayout()
/*      */   {
/*  558 */     return new RootLayout();
/*      */   }
/*      */ 
/*      */   public void setJMenuBar(JMenuBar paramJMenuBar)
/*      */   {
/*  566 */     if ((this.menuBar != null) && (this.menuBar.getParent() == this.layeredPane))
/*  567 */       this.layeredPane.remove(this.menuBar);
/*  568 */     this.menuBar = paramJMenuBar;
/*      */ 
/*  570 */     if (this.menuBar != null)
/*  571 */       this.layeredPane.add(this.menuBar, JLayeredPane.FRAME_CONTENT_LAYER);
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   public void setMenuBar(JMenuBar paramJMenuBar)
/*      */   {
/*  582 */     if ((this.menuBar != null) && (this.menuBar.getParent() == this.layeredPane))
/*  583 */       this.layeredPane.remove(this.menuBar);
/*  584 */     this.menuBar = paramJMenuBar;
/*      */ 
/*  586 */     if (this.menuBar != null)
/*  587 */       this.layeredPane.add(this.menuBar, JLayeredPane.FRAME_CONTENT_LAYER);
/*      */   }
/*      */ 
/*      */   public JMenuBar getJMenuBar()
/*      */   {
/*  594 */     return this.menuBar;
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   public JMenuBar getMenuBar()
/*      */   {
/*  603 */     return this.menuBar;
/*      */   }
/*      */ 
/*      */   public void setContentPane(Container paramContainer)
/*      */   {
/*  619 */     if (paramContainer == null)
/*  620 */       throw new IllegalComponentStateException("contentPane cannot be set to null.");
/*  621 */     if ((this.contentPane != null) && (this.contentPane.getParent() == this.layeredPane))
/*  622 */       this.layeredPane.remove(this.contentPane);
/*  623 */     this.contentPane = paramContainer;
/*      */ 
/*  625 */     this.layeredPane.add(this.contentPane, JLayeredPane.FRAME_CONTENT_LAYER);
/*      */   }
/*      */ 
/*      */   public Container getContentPane()
/*      */   {
/*  634 */     return this.contentPane;
/*      */   }
/*      */ 
/*      */   public void setLayeredPane(JLayeredPane paramJLayeredPane)
/*      */   {
/*  646 */     if (paramJLayeredPane == null)
/*  647 */       throw new IllegalComponentStateException("layeredPane cannot be set to null.");
/*  648 */     if ((this.layeredPane != null) && (this.layeredPane.getParent() == this))
/*  649 */       remove(this.layeredPane);
/*  650 */     this.layeredPane = paramJLayeredPane;
/*      */ 
/*  652 */     add(this.layeredPane, -1);
/*      */   }
/*      */ 
/*      */   public JLayeredPane getLayeredPane()
/*      */   {
/*  660 */     return this.layeredPane;
/*      */   }
/*      */ 
/*      */   public void setGlassPane(Component paramComponent)
/*      */   {
/*  688 */     if (paramComponent == null) {
/*  689 */       throw new NullPointerException("glassPane cannot be set to null.");
/*      */     }
/*      */ 
/*  692 */     AWTAccessor.getComponentAccessor().setMixingCutoutShape(paramComponent, new Rectangle());
/*      */ 
/*  695 */     boolean bool = false;
/*  696 */     if ((this.glassPane != null) && (this.glassPane.getParent() == this)) {
/*  697 */       remove(this.glassPane);
/*  698 */       bool = this.glassPane.isVisible();
/*      */     }
/*      */ 
/*  701 */     paramComponent.setVisible(bool);
/*  702 */     this.glassPane = paramComponent;
/*  703 */     add(this.glassPane, 0);
/*  704 */     if (bool)
/*  705 */       repaint();
/*      */   }
/*      */ 
/*      */   public Component getGlassPane()
/*      */   {
/*  715 */     return this.glassPane;
/*      */   }
/*      */ 
/*      */   public boolean isValidateRoot()
/*      */   {
/*  733 */     return true;
/*      */   }
/*      */ 
/*      */   public boolean isOptimizedDrawingEnabled()
/*      */   {
/*  749 */     return !this.glassPane.isVisible();
/*      */   }
/*      */ 
/*      */   public void addNotify()
/*      */   {
/*  756 */     super.addNotify();
/*  757 */     enableEvents(8L);
/*      */   }
/*      */ 
/*      */   public void removeNotify()
/*      */   {
/*  764 */     super.removeNotify();
/*      */   }
/*      */ 
/*      */   public void setDefaultButton(JButton paramJButton)
/*      */   {
/*  789 */     JButton localJButton = this.defaultButton;
/*      */ 
/*  791 */     if (localJButton != paramJButton) {
/*  792 */       this.defaultButton = paramJButton;
/*      */ 
/*  794 */       if (localJButton != null) {
/*  795 */         localJButton.repaint();
/*      */       }
/*  797 */       if (paramJButton != null) {
/*  798 */         paramJButton.repaint();
/*      */       }
/*      */     }
/*      */ 
/*  802 */     firePropertyChange("defaultButton", localJButton, paramJButton);
/*      */   }
/*      */ 
/*      */   public JButton getDefaultButton()
/*      */   {
/*  811 */     return this.defaultButton;
/*      */   }
/*      */ 
/*      */   final void setUseTrueDoubleBuffering(boolean paramBoolean) {
/*  815 */     this.useTrueDoubleBuffering = paramBoolean;
/*      */   }
/*      */ 
/*      */   final boolean getUseTrueDoubleBuffering() {
/*  819 */     return this.useTrueDoubleBuffering;
/*      */   }
/*      */ 
/*      */   final void disableTrueDoubleBuffering() {
/*  823 */     if ((this.useTrueDoubleBuffering) && 
/*  824 */       (!IGNORE_DISABLE_TRUE_DOUBLE_BUFFERING)) {
/*  825 */       if (LOG_DISABLE_TRUE_DOUBLE_BUFFERING) {
/*  826 */         System.out.println("Disabling true double buffering for " + this);
/*      */ 
/*  828 */         Thread.dumpStack();
/*      */       }
/*  830 */       this.useTrueDoubleBuffering = false;
/*  831 */       RepaintManager.currentManager(this).doubleBufferingChanged(this);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void addImpl(Component paramComponent, Object paramObject, int paramInt)
/*      */   {
/*  874 */     super.addImpl(paramComponent, paramObject, paramInt);
/*      */ 
/*  877 */     if ((this.glassPane != null) && (this.glassPane.getParent() == this) && (getComponent(0) != this.glassPane))
/*      */     {
/*  880 */       add(this.glassPane, 0);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected String paramString()
/*      */   {
/* 1030 */     return super.paramString();
/*      */   }
/*      */ 
/*      */   public AccessibleContext getAccessibleContext()
/*      */   {
/* 1048 */     if (this.accessibleContext == null) {
/* 1049 */       this.accessibleContext = new AccessibleJRootPane();
/*      */     }
/* 1051 */     return this.accessibleContext;
/*      */   }
/*      */ 
/*      */   protected class AccessibleJRootPane extends JComponent.AccessibleJComponent
/*      */   {
/*      */     protected AccessibleJRootPane()
/*      */     {
/* 1068 */       super();
/*      */     }
/*      */ 
/*      */     public AccessibleRole getAccessibleRole()
/*      */     {
/* 1076 */       return AccessibleRole.ROOT_PANE;
/*      */     }
/*      */ 
/*      */     public int getAccessibleChildrenCount()
/*      */     {
/* 1085 */       return super.getAccessibleChildrenCount();
/*      */     }
/*      */ 
/*      */     public Accessible getAccessibleChild(int paramInt)
/*      */     {
/* 1099 */       return super.getAccessibleChild(paramInt);
/*      */     }
/*      */   }
/*      */ 
/*      */   static class DefaultAction extends AbstractAction
/*      */   {
/*      */     JButton owner;
/*      */     JRootPane root;
/*      */     boolean press;
/*      */ 
/*      */     DefaultAction(JRootPane paramJRootPane, boolean paramBoolean)
/*      */     {
/*  842 */       this.root = paramJRootPane;
/*  843 */       this.press = paramBoolean;
/*      */     }
/*      */     public void setOwner(JButton paramJButton) {
/*  846 */       this.owner = paramJButton;
/*      */     }
/*      */     public void actionPerformed(ActionEvent paramActionEvent) {
/*  849 */       if ((this.owner != null) && (SwingUtilities.getRootPane(this.owner) == this.root)) {
/*  850 */         ButtonModel localButtonModel = this.owner.getModel();
/*  851 */         if (this.press) {
/*  852 */           localButtonModel.setArmed(true);
/*  853 */           localButtonModel.setPressed(true);
/*      */         } else {
/*  855 */           localButtonModel.setPressed(false);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  860 */     public boolean isEnabled() { return this.owner.getModel().isEnabled(); }
/*      */ 
/*      */   }
/*      */ 
/*      */   protected class RootLayout
/*      */     implements LayoutManager2, Serializable
/*      */   {
/*      */     protected RootLayout()
/*      */     {
/*      */     }
/*      */ 
/*      */     public Dimension preferredLayoutSize(Container paramContainer)
/*      */     {
/*  914 */       Insets localInsets = JRootPane.this.getInsets();
/*      */       Dimension localDimension1;
/*  916 */       if (JRootPane.this.contentPane != null)
/*  917 */         localDimension1 = JRootPane.this.contentPane.getPreferredSize();
/*      */       else
/*  919 */         localDimension1 = paramContainer.getSize();
/*      */       Dimension localDimension2;
/*  921 */       if ((JRootPane.this.menuBar != null) && (JRootPane.this.menuBar.isVisible()))
/*  922 */         localDimension2 = JRootPane.this.menuBar.getPreferredSize();
/*      */       else {
/*  924 */         localDimension2 = new Dimension(0, 0);
/*      */       }
/*  926 */       return new Dimension(Math.max(localDimension1.width, localDimension2.width) + localInsets.left + localInsets.right, localDimension1.height + localDimension2.height + localInsets.top + localInsets.bottom);
/*      */     }
/*      */ 
/*      */     public Dimension minimumLayoutSize(Container paramContainer)
/*      */     {
/*  939 */       Insets localInsets = JRootPane.this.getInsets();
/*      */       Dimension localDimension1;
/*  940 */       if (JRootPane.this.contentPane != null)
/*  941 */         localDimension1 = JRootPane.this.contentPane.getMinimumSize();
/*      */       else
/*  943 */         localDimension1 = paramContainer.getSize();
/*      */       Dimension localDimension2;
/*  945 */       if ((JRootPane.this.menuBar != null) && (JRootPane.this.menuBar.isVisible()))
/*  946 */         localDimension2 = JRootPane.this.menuBar.getMinimumSize();
/*      */       else {
/*  948 */         localDimension2 = new Dimension(0, 0);
/*      */       }
/*  950 */       return new Dimension(Math.max(localDimension1.width, localDimension2.width) + localInsets.left + localInsets.right, localDimension1.height + localDimension2.height + localInsets.top + localInsets.bottom);
/*      */     }
/*      */ 
/*      */     public Dimension maximumLayoutSize(Container paramContainer)
/*      */     {
/*  963 */       Insets localInsets = JRootPane.this.getInsets();
/*      */       Dimension localDimension2;
/*  964 */       if ((JRootPane.this.menuBar != null) && (JRootPane.this.menuBar.isVisible()))
/*  965 */         localDimension2 = JRootPane.this.menuBar.getMaximumSize();
/*      */       else
/*  967 */         localDimension2 = new Dimension(0, 0);
/*      */       Dimension localDimension1;
/*  969 */       if (JRootPane.this.contentPane != null) {
/*  970 */         localDimension1 = JRootPane.this.contentPane.getMaximumSize();
/*      */       }
/*      */       else {
/*  973 */         localDimension1 = new Dimension(2147483647, 2147483647 - localInsets.top - localInsets.bottom - localDimension2.height - 1);
/*      */       }
/*      */ 
/*  976 */       return new Dimension(Math.min(localDimension1.width, localDimension2.width) + localInsets.left + localInsets.right, localDimension1.height + localDimension2.height + localInsets.top + localInsets.bottom);
/*      */     }
/*      */ 
/*      */     public void layoutContainer(Container paramContainer)
/*      */     {
/*  988 */       Rectangle localRectangle = paramContainer.getBounds();
/*  989 */       Insets localInsets = JRootPane.this.getInsets();
/*  990 */       int i = 0;
/*  991 */       int j = localRectangle.width - localInsets.right - localInsets.left;
/*  992 */       int k = localRectangle.height - localInsets.top - localInsets.bottom;
/*      */ 
/*  994 */       if (JRootPane.this.layeredPane != null) {
/*  995 */         JRootPane.this.layeredPane.setBounds(localInsets.left, localInsets.top, j, k);
/*      */       }
/*  997 */       if (JRootPane.this.glassPane != null) {
/*  998 */         JRootPane.this.glassPane.setBounds(localInsets.left, localInsets.top, j, k);
/*      */       }
/*      */ 
/* 1002 */       if ((JRootPane.this.menuBar != null) && (JRootPane.this.menuBar.isVisible())) {
/* 1003 */         Dimension localDimension = JRootPane.this.menuBar.getPreferredSize();
/* 1004 */         JRootPane.this.menuBar.setBounds(0, 0, j, localDimension.height);
/* 1005 */         i += localDimension.height;
/*      */       }
/* 1007 */       if (JRootPane.this.contentPane != null)
/* 1008 */         JRootPane.this.contentPane.setBounds(0, i, j, k - i); 
/*      */     }
/*      */     public void addLayoutComponent(String paramString, Component paramComponent) {
/*      */     }
/*      */     public void removeLayoutComponent(Component paramComponent) {
/*      */     }
/*      */     public void addLayoutComponent(Component paramComponent, Object paramObject) {  }
/*      */ 
/* 1015 */     public float getLayoutAlignmentX(Container paramContainer) { return 0.0F; } 
/* 1016 */     public float getLayoutAlignmentY(Container paramContainer) { return 0.0F; }
/*      */ 
/*      */ 
/*      */     public void invalidateLayout(Container paramContainer)
/*      */     {
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.JRootPane
 * JD-Core Version:    0.6.2
 */