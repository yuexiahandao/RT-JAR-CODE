/*      */ package javax.swing.plaf.basic;
/*      */ 
/*      */ import java.awt.BorderLayout;
/*      */ import java.awt.Color;
/*      */ import java.awt.Component;
/*      */ import java.awt.Container;
/*      */ import java.awt.Dialog;
/*      */ import java.awt.Dimension;
/*      */ import java.awt.Frame;
/*      */ import java.awt.Graphics;
/*      */ import java.awt.GraphicsConfiguration;
/*      */ import java.awt.IllegalComponentStateException;
/*      */ import java.awt.Insets;
/*      */ import java.awt.LayoutManager;
/*      */ import java.awt.Point;
/*      */ import java.awt.Window;
/*      */ import java.awt.event.ActionEvent;
/*      */ import java.awt.event.ContainerEvent;
/*      */ import java.awt.event.ContainerListener;
/*      */ import java.awt.event.FocusEvent;
/*      */ import java.awt.event.FocusListener;
/*      */ import java.awt.event.MouseEvent;
/*      */ import java.awt.event.WindowAdapter;
/*      */ import java.awt.event.WindowEvent;
/*      */ import java.awt.event.WindowListener;
/*      */ import java.beans.PropertyChangeEvent;
/*      */ import java.beans.PropertyChangeListener;
/*      */ import java.util.HashMap;
/*      */ import java.util.Hashtable;
/*      */ import javax.swing.AbstractButton;
/*      */ import javax.swing.InputMap;
/*      */ import javax.swing.JComponent;
/*      */ import javax.swing.JDialog;
/*      */ import javax.swing.JFrame;
/*      */ import javax.swing.JRootPane;
/*      */ import javax.swing.JToggleButton;
/*      */ import javax.swing.JToolBar;
/*      */ import javax.swing.JToolBar.Separator;
/*      */ import javax.swing.KeyStroke;
/*      */ import javax.swing.LookAndFeel;
/*      */ import javax.swing.RootPaneContainer;
/*      */ import javax.swing.SwingConstants;
/*      */ import javax.swing.SwingUtilities;
/*      */ import javax.swing.UIDefaults;
/*      */ import javax.swing.UIManager;
/*      */ import javax.swing.border.Border;
/*      */ import javax.swing.border.CompoundBorder;
/*      */ import javax.swing.event.MouseInputListener;
/*      */ import javax.swing.plaf.ComponentUI;
/*      */ import javax.swing.plaf.ToolBarUI;
/*      */ import javax.swing.plaf.UIResource;
/*      */ import sun.swing.DefaultLookup;
/*      */ import sun.swing.UIAction;
/*      */ 
/*      */ public class BasicToolBarUI extends ToolBarUI
/*      */   implements SwingConstants
/*      */ {
/*      */   protected JToolBar toolBar;
/*      */   private boolean floating;
/*      */   private int floatingX;
/*      */   private int floatingY;
/*      */   private JFrame floatingFrame;
/*      */   private RootPaneContainer floatingToolBar;
/*      */   protected DragWindow dragWindow;
/*      */   private Container dockingSource;
/*      */   private int dockingSensitivity;
/*      */   protected int focusedCompIndex;
/*      */   protected Color dockingColor;
/*      */   protected Color floatingColor;
/*      */   protected Color dockingBorderColor;
/*      */   protected Color floatingBorderColor;
/*      */   protected MouseInputListener dockingListener;
/*      */   protected PropertyChangeListener propertyListener;
/*      */   protected ContainerListener toolBarContListener;
/*      */   protected FocusListener toolBarFocusListener;
/*      */   private Handler handler;
/*      */   protected String constraintBeforeFloating;
/*   80 */   private static String IS_ROLLOVER = "JToolBar.isRollover";
/*      */   private static Border rolloverBorder;
/*      */   private static Border nonRolloverBorder;
/*      */   private static Border nonRolloverToggleBorder;
/*      */   private boolean rolloverBorders;
/*      */   private HashMap<AbstractButton, Border> borderTable;
/*      */   private Hashtable<AbstractButton, Boolean> rolloverTable;
/*      */ 
/*      */   @Deprecated
/*      */   protected KeyStroke upKey;
/*      */ 
/*      */   @Deprecated
/*      */   protected KeyStroke downKey;
/*      */ 
/*      */   @Deprecated
/*      */   protected KeyStroke leftKey;
/*      */ 
/*      */   @Deprecated
/*      */   protected KeyStroke rightKey;
/*  132 */   private static String FOCUSED_COMP_INDEX = "JToolBar.focusedCompIndex";
/*      */ 
/*      */   public BasicToolBarUI()
/*      */   {
/*   62 */     this.dockingSensitivity = 0;
/*   63 */     this.focusedCompIndex = -1;
/*      */ 
/*   65 */     this.dockingColor = null;
/*   66 */     this.floatingColor = null;
/*   67 */     this.dockingBorderColor = null;
/*   68 */     this.floatingBorderColor = null;
/*      */ 
/*   77 */     this.constraintBeforeFloating = "North";
/*      */ 
/*   84 */     this.rolloverBorders = false;
/*      */ 
/*   86 */     this.borderTable = new HashMap();
/*   87 */     this.rolloverTable = new Hashtable();
/*      */   }
/*      */ 
/*      */   public static ComponentUI createUI(JComponent paramJComponent)
/*      */   {
/*  136 */     return new BasicToolBarUI();
/*      */   }
/*      */ 
/*      */   public void installUI(JComponent paramJComponent)
/*      */   {
/*  141 */     this.toolBar = ((JToolBar)paramJComponent);
/*      */ 
/*  144 */     installDefaults();
/*  145 */     installComponents();
/*  146 */     installListeners();
/*  147 */     installKeyboardActions();
/*      */ 
/*  150 */     this.dockingSensitivity = 0;
/*  151 */     this.floating = false;
/*  152 */     this.floatingX = (this.floatingY = 0);
/*  153 */     this.floatingToolBar = null;
/*      */ 
/*  155 */     setOrientation(this.toolBar.getOrientation());
/*  156 */     LookAndFeel.installProperty(paramJComponent, "opaque", Boolean.TRUE);
/*      */ 
/*  158 */     if (paramJComponent.getClientProperty(FOCUSED_COMP_INDEX) != null)
/*      */     {
/*  160 */       this.focusedCompIndex = ((Integer)paramJComponent.getClientProperty(FOCUSED_COMP_INDEX)).intValue();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void uninstallUI(JComponent paramJComponent)
/*      */   {
/*  168 */     uninstallDefaults();
/*  169 */     uninstallComponents();
/*  170 */     uninstallListeners();
/*  171 */     uninstallKeyboardActions();
/*      */ 
/*  174 */     if (isFloating()) {
/*  175 */       setFloating(false, null);
/*      */     }
/*  177 */     this.floatingToolBar = null;
/*  178 */     this.dragWindow = null;
/*  179 */     this.dockingSource = null;
/*      */ 
/*  181 */     paramJComponent.putClientProperty(FOCUSED_COMP_INDEX, Integer.valueOf(this.focusedCompIndex));
/*      */   }
/*      */ 
/*      */   protected void installDefaults()
/*      */   {
/*  186 */     LookAndFeel.installBorder(this.toolBar, "ToolBar.border");
/*  187 */     LookAndFeel.installColorsAndFont(this.toolBar, "ToolBar.background", "ToolBar.foreground", "ToolBar.font");
/*      */ 
/*  192 */     if ((this.dockingColor == null) || ((this.dockingColor instanceof UIResource)))
/*  193 */       this.dockingColor = UIManager.getColor("ToolBar.dockingBackground");
/*  194 */     if ((this.floatingColor == null) || ((this.floatingColor instanceof UIResource)))
/*  195 */       this.floatingColor = UIManager.getColor("ToolBar.floatingBackground");
/*  196 */     if ((this.dockingBorderColor == null) || ((this.dockingBorderColor instanceof UIResource)))
/*      */     {
/*  198 */       this.dockingBorderColor = UIManager.getColor("ToolBar.dockingForeground");
/*  199 */     }if ((this.floatingBorderColor == null) || ((this.floatingBorderColor instanceof UIResource)))
/*      */     {
/*  201 */       this.floatingBorderColor = UIManager.getColor("ToolBar.floatingForeground");
/*      */     }
/*      */ 
/*  204 */     Object localObject = this.toolBar.getClientProperty(IS_ROLLOVER);
/*  205 */     if (localObject == null) {
/*  206 */       localObject = UIManager.get("ToolBar.isRollover");
/*      */     }
/*  208 */     if (localObject != null) {
/*  209 */       this.rolloverBorders = ((Boolean)localObject).booleanValue();
/*      */     }
/*      */ 
/*  212 */     if (rolloverBorder == null) {
/*  213 */       rolloverBorder = createRolloverBorder();
/*      */     }
/*  215 */     if (nonRolloverBorder == null) {
/*  216 */       nonRolloverBorder = createNonRolloverBorder();
/*      */     }
/*  218 */     if (nonRolloverToggleBorder == null) {
/*  219 */       nonRolloverToggleBorder = createNonRolloverToggleBorder();
/*      */     }
/*      */ 
/*  223 */     setRolloverBorders(isRolloverBorders());
/*      */   }
/*      */ 
/*      */   protected void uninstallDefaults()
/*      */   {
/*  228 */     LookAndFeel.uninstallBorder(this.toolBar);
/*  229 */     this.dockingColor = null;
/*  230 */     this.floatingColor = null;
/*  231 */     this.dockingBorderColor = null;
/*  232 */     this.floatingBorderColor = null;
/*      */ 
/*  234 */     installNormalBorders(this.toolBar);
/*      */ 
/*  236 */     rolloverBorder = null;
/*  237 */     nonRolloverBorder = null;
/*  238 */     nonRolloverToggleBorder = null;
/*      */   }
/*      */ 
/*      */   protected void installComponents()
/*      */   {
/*      */   }
/*      */ 
/*      */   protected void uninstallComponents()
/*      */   {
/*      */   }
/*      */ 
/*      */   protected void installListeners()
/*      */   {
/*  251 */     this.dockingListener = createDockingListener();
/*      */ 
/*  253 */     if (this.dockingListener != null)
/*      */     {
/*  255 */       this.toolBar.addMouseMotionListener(this.dockingListener);
/*  256 */       this.toolBar.addMouseListener(this.dockingListener);
/*      */     }
/*      */ 
/*  259 */     this.propertyListener = createPropertyListener();
/*  260 */     if (this.propertyListener != null) {
/*  261 */       this.toolBar.addPropertyChangeListener(this.propertyListener);
/*      */     }
/*      */ 
/*  264 */     this.toolBarContListener = createToolBarContListener();
/*  265 */     if (this.toolBarContListener != null) {
/*  266 */       this.toolBar.addContainerListener(this.toolBarContListener);
/*      */     }
/*      */ 
/*  269 */     this.toolBarFocusListener = createToolBarFocusListener();
/*      */ 
/*  271 */     if (this.toolBarFocusListener != null)
/*      */     {
/*  274 */       Component[] arrayOfComponent1 = this.toolBar.getComponents();
/*      */ 
/*  276 */       for (Component localComponent : arrayOfComponent1)
/*  277 */         localComponent.addFocusListener(this.toolBarFocusListener);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void uninstallListeners()
/*      */   {
/*  284 */     if (this.dockingListener != null)
/*      */     {
/*  286 */       this.toolBar.removeMouseMotionListener(this.dockingListener);
/*  287 */       this.toolBar.removeMouseListener(this.dockingListener);
/*      */ 
/*  289 */       this.dockingListener = null;
/*      */     }
/*      */ 
/*  292 */     if (this.propertyListener != null)
/*      */     {
/*  294 */       this.toolBar.removePropertyChangeListener(this.propertyListener);
/*  295 */       this.propertyListener = null;
/*      */     }
/*      */ 
/*  298 */     if (this.toolBarContListener != null)
/*      */     {
/*  300 */       this.toolBar.removeContainerListener(this.toolBarContListener);
/*  301 */       this.toolBarContListener = null;
/*      */     }
/*      */ 
/*  304 */     if (this.toolBarFocusListener != null)
/*      */     {
/*  307 */       Component[] arrayOfComponent1 = this.toolBar.getComponents();
/*      */ 
/*  309 */       for (Component localComponent : arrayOfComponent1) {
/*  310 */         localComponent.removeFocusListener(this.toolBarFocusListener);
/*      */       }
/*      */ 
/*  313 */       this.toolBarFocusListener = null;
/*      */     }
/*  315 */     this.handler = null;
/*      */   }
/*      */ 
/*      */   protected void installKeyboardActions()
/*      */   {
/*  320 */     InputMap localInputMap = getInputMap(1);
/*      */ 
/*  323 */     SwingUtilities.replaceUIInputMap(this.toolBar, 1, localInputMap);
/*      */ 
/*  327 */     LazyActionMap.installLazyActionMap(this.toolBar, BasicToolBarUI.class, "ToolBar.actionMap");
/*      */   }
/*      */ 
/*      */   InputMap getInputMap(int paramInt)
/*      */   {
/*  332 */     if (paramInt == 1) {
/*  333 */       return (InputMap)DefaultLookup.get(this.toolBar, this, "ToolBar.ancestorInputMap");
/*      */     }
/*      */ 
/*  336 */     return null;
/*      */   }
/*      */ 
/*      */   static void loadActionMap(LazyActionMap paramLazyActionMap) {
/*  340 */     paramLazyActionMap.put(new Actions("navigateRight"));
/*  341 */     paramLazyActionMap.put(new Actions("navigateLeft"));
/*  342 */     paramLazyActionMap.put(new Actions("navigateUp"));
/*  343 */     paramLazyActionMap.put(new Actions("navigateDown"));
/*      */   }
/*      */ 
/*      */   protected void uninstallKeyboardActions()
/*      */   {
/*  348 */     SwingUtilities.replaceUIActionMap(this.toolBar, null);
/*  349 */     SwingUtilities.replaceUIInputMap(this.toolBar, 1, null);
/*      */   }
/*      */ 
/*      */   protected void navigateFocusedComp(int paramInt)
/*      */   {
/*  356 */     int i = this.toolBar.getComponentCount();
/*      */     int j;
/*  359 */     switch (paramInt)
/*      */     {
/*      */     case 3:
/*      */     case 5:
/*  364 */       if ((this.focusedCompIndex >= 0) && (this.focusedCompIndex < i))
/*      */       {
/*  366 */         j = this.focusedCompIndex + 1; } break;
/*      */     case 1:
/*      */     case 7:
/*  368 */       while (j != this.focusedCompIndex)
/*      */       {
/*  370 */         if (j >= i) j = 0;
/*  371 */         Component localComponent = this.toolBar.getComponentAtIndex(j++);
/*      */ 
/*  373 */         if ((localComponent != null) && (localComponent.isFocusTraversable()) && (localComponent.isEnabled()))
/*      */         {
/*  375 */           localComponent.requestFocus();
/*      */         }
/*      */         else {
/*  378 */           continue;
/*      */ 
/*  385 */           if ((this.focusedCompIndex >= 0) && (this.focusedCompIndex < i))
/*      */           {
/*  387 */             j = this.focusedCompIndex - 1;
/*      */ 
/*  389 */             while (j != this.focusedCompIndex)
/*      */             {
/*  391 */               if (j < 0) j = i - 1;
/*  392 */               localComponent = this.toolBar.getComponentAtIndex(j--);
/*      */ 
/*  394 */               if ((localComponent != null) && (localComponent.isFocusTraversable()) && (localComponent.isEnabled()))
/*      */               {
/*  396 */                 localComponent.requestFocus();
/*  397 */                 break;
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     case 2:
/*      */     case 4:
/*      */     case 6:
/*      */     }
/*      */   }
/*      */ 
/*      */   protected Border createRolloverBorder()
/*      */   {
/*  418 */     Object localObject = UIManager.get("ToolBar.rolloverBorder");
/*  419 */     if (localObject != null) {
/*  420 */       return (Border)localObject;
/*      */     }
/*  422 */     UIDefaults localUIDefaults = UIManager.getLookAndFeelDefaults();
/*  423 */     return new CompoundBorder(new BasicBorders.RolloverButtonBorder(localUIDefaults.getColor("controlShadow"), localUIDefaults.getColor("controlDkShadow"), localUIDefaults.getColor("controlHighlight"), localUIDefaults.getColor("controlLtHighlight")), new BasicBorders.RolloverMarginBorder());
/*      */   }
/*      */ 
/*      */   protected Border createNonRolloverBorder()
/*      */   {
/*  441 */     Object localObject = UIManager.get("ToolBar.nonrolloverBorder");
/*  442 */     if (localObject != null) {
/*  443 */       return (Border)localObject;
/*      */     }
/*  445 */     UIDefaults localUIDefaults = UIManager.getLookAndFeelDefaults();
/*  446 */     return new CompoundBorder(new BasicBorders.ButtonBorder(localUIDefaults.getColor("Button.shadow"), localUIDefaults.getColor("Button.darkShadow"), localUIDefaults.getColor("Button.light"), localUIDefaults.getColor("Button.highlight")), new BasicBorders.RolloverMarginBorder());
/*      */   }
/*      */ 
/*      */   private Border createNonRolloverToggleBorder()
/*      */   {
/*  458 */     UIDefaults localUIDefaults = UIManager.getLookAndFeelDefaults();
/*  459 */     return new CompoundBorder(new BasicBorders.RadioButtonBorder(localUIDefaults.getColor("ToggleButton.shadow"), localUIDefaults.getColor("ToggleButton.darkShadow"), localUIDefaults.getColor("ToggleButton.light"), localUIDefaults.getColor("ToggleButton.highlight")), new BasicBorders.RolloverMarginBorder());
/*      */   }
/*      */ 
/*      */   protected JFrame createFloatingFrame(JToolBar paramJToolBar)
/*      */   {
/*  472 */     Window localWindow = SwingUtilities.getWindowAncestor(paramJToolBar);
/*  473 */     JFrame local1 = new JFrame(paramJToolBar.getName(), localWindow != null ? localWindow.getGraphicsConfiguration() : null)
/*      */     {
/*      */       protected JRootPane createRootPane()
/*      */       {
/*  478 */         JRootPane local1 = new JRootPane() {
/*  479 */           private boolean packing = false;
/*      */ 
/*      */           public void validate() {
/*  482 */             super.validate();
/*  483 */             if (!this.packing) {
/*  484 */               this.packing = true;
/*  485 */               BasicToolBarUI.1.this.pack();
/*  486 */               this.packing = false;
/*      */             }
/*      */           }
/*      */         };
/*  490 */         local1.setOpaque(true);
/*  491 */         return local1;
/*      */       }
/*      */     };
/*  494 */     local1.getRootPane().setName("ToolBar.FloatingFrame");
/*  495 */     local1.setResizable(false);
/*  496 */     WindowListener localWindowListener = createFrameListener();
/*  497 */     local1.addWindowListener(localWindowListener);
/*  498 */     return local1;
/*      */   }
/*      */ 
/*      */   protected RootPaneContainer createFloatingWindow(JToolBar paramJToolBar)
/*      */   {
/*  538 */     Window localWindow = SwingUtilities.getWindowAncestor(paramJToolBar);
/*      */     JDialog local1ToolBarDialog;
/*  539 */     if ((localWindow instanceof Frame)) {
/*  540 */       local1ToolBarDialog = new JDialog((Frame)localWindow, paramJToolBar.getName(), false)
/*      */       {
/*      */         protected JRootPane createRootPane()
/*      */         {
/*  520 */           JRootPane local1 = new JRootPane() {
/*  521 */             private boolean packing = false;
/*      */ 
/*      */             public void validate() {
/*  524 */               super.validate();
/*  525 */               if (!this.packing) {
/*  526 */                 this.packing = true;
/*  527 */                 BasicToolBarUI.1ToolBarDialog.this.pack();
/*  528 */                 this.packing = false;
/*      */               }
/*      */             }
/*      */           };
/*  532 */           local1.setOpaque(true);
/*  533 */           return local1;
/*      */         }
/*      */ 
/*      */       };
/*      */     }
/*  541 */     else if ((localWindow instanceof Dialog)) {
/*  542 */       local1ToolBarDialog = new JDialog((Dialog)localWindow, paramJToolBar.getName(), false)
/*      */       {
/*      */         protected JRootPane createRootPane()
/*      */         {
/*  520 */           JRootPane local1 = new JRootPane() {
/*  521 */             private boolean packing = false;
/*      */ 
/*      */             public void validate() {
/*  524 */               super.validate();
/*  525 */               if (!this.packing) {
/*  526 */                 this.packing = true;
/*  527 */                 BasicToolBarUI.1ToolBarDialog.this.pack();
/*  528 */                 this.packing = false;
/*      */               }
/*      */             }
/*      */           };
/*  532 */           local1.setOpaque(true);
/*  533 */           return local1;
/*      */         }
/*      */ 
/*      */       };
/*      */     }
/*      */     else
/*      */     {
/*  544 */       local1ToolBarDialog = new JDialog((Frame)null, paramJToolBar.getName(), false)
/*      */       {
/*      */         protected JRootPane createRootPane()
/*      */         {
/*  520 */           JRootPane local1 = new JRootPane() {
/*  521 */             private boolean packing = false;
/*      */ 
/*      */             public void validate() {
/*  524 */               super.validate();
/*  525 */               if (!this.packing) {
/*  526 */                 this.packing = true;
/*  527 */                 BasicToolBarUI.1ToolBarDialog.this.pack();
/*  528 */                 this.packing = false;
/*      */               }
/*      */             }
/*      */           };
/*  532 */           local1.setOpaque(true);
/*  533 */           return local1;
/*      */         }
/*      */ 
/*      */       };
/*      */     }
/*      */ 
/*  547 */     local1ToolBarDialog.getRootPane().setName("ToolBar.FloatingWindow");
/*  548 */     local1ToolBarDialog.setTitle(paramJToolBar.getName());
/*  549 */     local1ToolBarDialog.setResizable(false);
/*  550 */     WindowListener localWindowListener = createFrameListener();
/*  551 */     local1ToolBarDialog.addWindowListener(localWindowListener);
/*  552 */     return local1ToolBarDialog;
/*      */   }
/*      */ 
/*      */   protected DragWindow createDragWindow(JToolBar paramJToolBar) {
/*  556 */     Window localWindow = null;
/*  557 */     if (this.toolBar != null)
/*      */     {
/*  559 */       for (localObject = this.toolBar.getParent(); (localObject != null) && (!(localObject instanceof Window)); )
/*  560 */         localObject = ((Container)localObject).getParent();
/*  561 */       if ((localObject != null) && ((localObject instanceof Window)))
/*  562 */         localWindow = (Window)localObject;
/*      */     }
/*  564 */     if (this.floatingToolBar == null) {
/*  565 */       this.floatingToolBar = createFloatingWindow(this.toolBar);
/*      */     }
/*  567 */     if ((this.floatingToolBar instanceof Window)) localWindow = (Window)this.floatingToolBar;
/*  568 */     Object localObject = new DragWindow(localWindow);
/*  569 */     return localObject;
/*      */   }
/*      */ 
/*      */   public boolean isRolloverBorders()
/*      */   {
/*  581 */     return this.rolloverBorders;
/*      */   }
/*      */ 
/*      */   public void setRolloverBorders(boolean paramBoolean)
/*      */   {
/*  594 */     this.rolloverBorders = paramBoolean;
/*      */ 
/*  596 */     if (this.rolloverBorders)
/*  597 */       installRolloverBorders(this.toolBar);
/*      */     else
/*  599 */       installNonRolloverBorders(this.toolBar);
/*      */   }
/*      */ 
/*      */   protected void installRolloverBorders(JComponent paramJComponent)
/*      */   {
/*  615 */     Component[] arrayOfComponent1 = paramJComponent.getComponents();
/*      */ 
/*  617 */     for (Component localComponent : arrayOfComponent1)
/*  618 */       if ((localComponent instanceof JComponent)) {
/*  619 */         ((JComponent)localComponent).updateUI();
/*  620 */         setBorderToRollover(localComponent);
/*      */       }
/*      */   }
/*      */ 
/*      */   protected void installNonRolloverBorders(JComponent paramJComponent)
/*      */   {
/*  639 */     Component[] arrayOfComponent1 = paramJComponent.getComponents();
/*      */ 
/*  641 */     for (Component localComponent : arrayOfComponent1)
/*  642 */       if ((localComponent instanceof JComponent)) {
/*  643 */         ((JComponent)localComponent).updateUI();
/*  644 */         setBorderToNonRollover(localComponent);
/*      */       }
/*      */   }
/*      */ 
/*      */   protected void installNormalBorders(JComponent paramJComponent)
/*      */   {
/*  663 */     Component[] arrayOfComponent1 = paramJComponent.getComponents();
/*      */ 
/*  665 */     for (Component localComponent : arrayOfComponent1)
/*  666 */       setBorderToNormal(localComponent);
/*      */   }
/*      */ 
/*      */   protected void setBorderToRollover(Component paramComponent)
/*      */   {
/*  679 */     if ((paramComponent instanceof AbstractButton)) {
/*  680 */       AbstractButton localAbstractButton = (AbstractButton)paramComponent;
/*      */ 
/*  682 */       Border localBorder = (Border)this.borderTable.get(localAbstractButton);
/*  683 */       if ((localBorder == null) || ((localBorder instanceof UIResource))) {
/*  684 */         this.borderTable.put(localAbstractButton, localAbstractButton.getBorder());
/*      */       }
/*      */ 
/*  688 */       if ((localAbstractButton.getBorder() instanceof UIResource)) {
/*  689 */         localAbstractButton.setBorder(getRolloverBorder(localAbstractButton));
/*      */       }
/*      */ 
/*  692 */       this.rolloverTable.put(localAbstractButton, localAbstractButton.isRolloverEnabled() ? Boolean.TRUE : Boolean.FALSE);
/*      */ 
/*  694 */       localAbstractButton.setRolloverEnabled(true);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected Border getRolloverBorder(AbstractButton paramAbstractButton)
/*      */   {
/*  707 */     return rolloverBorder;
/*      */   }
/*      */ 
/*      */   protected void setBorderToNonRollover(Component paramComponent)
/*      */   {
/*  719 */     if ((paramComponent instanceof AbstractButton)) {
/*  720 */       AbstractButton localAbstractButton = (AbstractButton)paramComponent;
/*      */ 
/*  722 */       Border localBorder = (Border)this.borderTable.get(localAbstractButton);
/*  723 */       if ((localBorder == null) || ((localBorder instanceof UIResource))) {
/*  724 */         this.borderTable.put(localAbstractButton, localAbstractButton.getBorder());
/*      */       }
/*      */ 
/*  728 */       if ((localAbstractButton.getBorder() instanceof UIResource)) {
/*  729 */         localAbstractButton.setBorder(getNonRolloverBorder(localAbstractButton));
/*      */       }
/*  731 */       this.rolloverTable.put(localAbstractButton, localAbstractButton.isRolloverEnabled() ? Boolean.TRUE : Boolean.FALSE);
/*      */ 
/*  733 */       localAbstractButton.setRolloverEnabled(false);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected Border getNonRolloverBorder(AbstractButton paramAbstractButton)
/*      */   {
/*  746 */     if ((paramAbstractButton instanceof JToggleButton)) {
/*  747 */       return nonRolloverToggleBorder;
/*      */     }
/*  749 */     return nonRolloverBorder;
/*      */   }
/*      */ 
/*      */   protected void setBorderToNormal(Component paramComponent)
/*      */   {
/*  763 */     if ((paramComponent instanceof AbstractButton)) {
/*  764 */       AbstractButton localAbstractButton = (AbstractButton)paramComponent;
/*      */ 
/*  766 */       Border localBorder = (Border)this.borderTable.remove(localAbstractButton);
/*  767 */       localAbstractButton.setBorder(localBorder);
/*      */ 
/*  769 */       Boolean localBoolean = (Boolean)this.rolloverTable.remove(localAbstractButton);
/*  770 */       if (localBoolean != null)
/*  771 */         localAbstractButton.setRolloverEnabled(localBoolean.booleanValue());
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setFloatingLocation(int paramInt1, int paramInt2)
/*      */   {
/*  777 */     this.floatingX = paramInt1;
/*  778 */     this.floatingY = paramInt2;
/*      */   }
/*      */ 
/*      */   public boolean isFloating() {
/*  782 */     return this.floating;
/*      */   }
/*      */ 
/*      */   public void setFloating(boolean paramBoolean, Point paramPoint) {
/*  786 */     if (this.toolBar.isFloatable()) {
/*  787 */       boolean bool = false;
/*  788 */       Window localWindow = SwingUtilities.getWindowAncestor(this.toolBar);
/*  789 */       if (localWindow != null) {
/*  790 */         bool = localWindow.isVisible();
/*      */       }
/*  792 */       if (this.dragWindow != null)
/*  793 */         this.dragWindow.setVisible(false);
/*  794 */       this.floating = paramBoolean;
/*  795 */       if (this.floatingToolBar == null) {
/*  796 */         this.floatingToolBar = createFloatingWindow(this.toolBar);
/*      */       }
/*  798 */       if (paramBoolean == true)
/*      */       {
/*  800 */         if (this.dockingSource == null)
/*      */         {
/*  802 */           this.dockingSource = this.toolBar.getParent();
/*  803 */           this.dockingSource.remove(this.toolBar);
/*      */         }
/*  805 */         this.constraintBeforeFloating = calculateConstraint();
/*  806 */         if (this.propertyListener != null)
/*  807 */           UIManager.addPropertyChangeListener(this.propertyListener);
/*  808 */         this.floatingToolBar.getContentPane().add(this.toolBar, "Center");
/*  809 */         if ((this.floatingToolBar instanceof Window)) {
/*  810 */           ((Window)this.floatingToolBar).pack();
/*  811 */           ((Window)this.floatingToolBar).setLocation(this.floatingX, this.floatingY);
/*  812 */           if (bool)
/*  813 */             ((Window)this.floatingToolBar).show();
/*      */           else
/*  815 */             localWindow.addWindowListener(new WindowAdapter() {
/*      */               public void windowOpened(WindowEvent paramAnonymousWindowEvent) {
/*  817 */                 ((Window)BasicToolBarUI.this.floatingToolBar).show();
/*      */               }
/*      */             });
/*      */         }
/*      */       }
/*      */       else {
/*  823 */         if (this.floatingToolBar == null)
/*  824 */           this.floatingToolBar = createFloatingWindow(this.toolBar);
/*  825 */         if ((this.floatingToolBar instanceof Window)) ((Window)this.floatingToolBar).setVisible(false);
/*  826 */         this.floatingToolBar.getContentPane().remove(this.toolBar);
/*  827 */         localObject = getDockingConstraint(this.dockingSource, paramPoint);
/*      */ 
/*  829 */         if (localObject == null) {
/*  830 */           localObject = "North";
/*      */         }
/*  832 */         int i = mapConstraintToOrientation((String)localObject);
/*  833 */         setOrientation(i);
/*  834 */         if (this.dockingSource == null)
/*  835 */           this.dockingSource = this.toolBar.getParent();
/*  836 */         if (this.propertyListener != null)
/*  837 */           UIManager.removePropertyChangeListener(this.propertyListener);
/*  838 */         this.dockingSource.add((String)localObject, this.toolBar);
/*      */       }
/*  840 */       this.dockingSource.invalidate();
/*  841 */       Object localObject = this.dockingSource.getParent();
/*  842 */       if (localObject != null)
/*  843 */         ((Container)localObject).validate();
/*  844 */       this.dockingSource.repaint();
/*      */     }
/*      */   }
/*      */ 
/*      */   private int mapConstraintToOrientation(String paramString)
/*      */   {
/*  850 */     int i = this.toolBar.getOrientation();
/*      */ 
/*  852 */     if (paramString != null)
/*      */     {
/*  854 */       if ((paramString.equals("East")) || (paramString.equals("West")))
/*  855 */         i = 1;
/*  856 */       else if ((paramString.equals("North")) || (paramString.equals("South"))) {
/*  857 */         i = 0;
/*      */       }
/*      */     }
/*  860 */     return i;
/*      */   }
/*      */ 
/*      */   public void setOrientation(int paramInt)
/*      */   {
/*  865 */     this.toolBar.setOrientation(paramInt);
/*      */ 
/*  867 */     if (this.dragWindow != null)
/*  868 */       this.dragWindow.setOrientation(paramInt);
/*      */   }
/*      */ 
/*      */   public Color getDockingColor()
/*      */   {
/*  875 */     return this.dockingColor;
/*      */   }
/*      */ 
/*      */   public void setDockingColor(Color paramColor)
/*      */   {
/*  882 */     this.dockingColor = paramColor;
/*      */   }
/*      */ 
/*      */   public Color getFloatingColor()
/*      */   {
/*  889 */     return this.floatingColor;
/*      */   }
/*      */ 
/*      */   public void setFloatingColor(Color paramColor)
/*      */   {
/*  896 */     this.floatingColor = paramColor;
/*      */   }
/*      */ 
/*      */   private boolean isBlocked(Component paramComponent, Object paramObject) {
/*  900 */     if ((paramComponent instanceof Container)) {
/*  901 */       Container localContainer = (Container)paramComponent;
/*  902 */       LayoutManager localLayoutManager = localContainer.getLayout();
/*  903 */       if ((localLayoutManager instanceof BorderLayout)) {
/*  904 */         BorderLayout localBorderLayout = (BorderLayout)localLayoutManager;
/*  905 */         Component localComponent = localBorderLayout.getLayoutComponent(localContainer, paramObject);
/*  906 */         return (localComponent != null) && (localComponent != this.toolBar);
/*      */       }
/*      */     }
/*  909 */     return false;
/*      */   }
/*      */ 
/*      */   public boolean canDock(Component paramComponent, Point paramPoint) {
/*  913 */     return (paramPoint != null) && (getDockingConstraint(paramComponent, paramPoint) != null);
/*      */   }
/*      */ 
/*      */   private String calculateConstraint() {
/*  917 */     String str = null;
/*  918 */     LayoutManager localLayoutManager = this.dockingSource.getLayout();
/*  919 */     if ((localLayoutManager instanceof BorderLayout)) {
/*  920 */       str = (String)((BorderLayout)localLayoutManager).getConstraints(this.toolBar);
/*      */     }
/*  922 */     return str != null ? str : this.constraintBeforeFloating;
/*      */   }
/*      */ 
/*      */   private String getDockingConstraint(Component paramComponent, Point paramPoint)
/*      */   {
/*  928 */     if (paramPoint == null) return this.constraintBeforeFloating;
/*  929 */     if (paramComponent.contains(paramPoint)) {
/*  930 */       this.dockingSensitivity = (this.toolBar.getOrientation() == 0 ? this.toolBar.getSize().height : this.toolBar.getSize().width);
/*      */ 
/*  934 */       if ((paramPoint.y < this.dockingSensitivity) && (!isBlocked(paramComponent, "North"))) {
/*  935 */         return "North";
/*      */       }
/*      */ 
/*  938 */       if ((paramPoint.x >= paramComponent.getWidth() - this.dockingSensitivity) && (!isBlocked(paramComponent, "East"))) {
/*  939 */         return "East";
/*      */       }
/*      */ 
/*  942 */       if ((paramPoint.x < this.dockingSensitivity) && (!isBlocked(paramComponent, "West"))) {
/*  943 */         return "West";
/*      */       }
/*  945 */       if ((paramPoint.y >= paramComponent.getHeight() - this.dockingSensitivity) && (!isBlocked(paramComponent, "South"))) {
/*  946 */         return "South";
/*      */       }
/*      */     }
/*  949 */     return null;
/*      */   }
/*      */ 
/*      */   protected void dragTo(Point paramPoint1, Point paramPoint2)
/*      */   {
/*  954 */     if (this.toolBar.isFloatable())
/*      */     {
/*      */       try
/*      */       {
/*  958 */         if (this.dragWindow == null)
/*  959 */           this.dragWindow = createDragWindow(this.toolBar);
/*  960 */         Point localPoint1 = this.dragWindow.getOffset();
/*  961 */         if (localPoint1 == null) {
/*  962 */           localObject1 = this.toolBar.getPreferredSize();
/*  963 */           localPoint1 = new Point(((Dimension)localObject1).width / 2, ((Dimension)localObject1).height / 2);
/*  964 */           this.dragWindow.setOffset(localPoint1);
/*      */         }
/*  966 */         Object localObject1 = new Point(paramPoint2.x + paramPoint1.x, paramPoint2.y + paramPoint1.y);
/*      */ 
/*  968 */         Point localPoint2 = new Point(((Point)localObject1).x - localPoint1.x, ((Point)localObject1).y - localPoint1.y);
/*      */ 
/*  970 */         if (this.dockingSource == null)
/*  971 */           this.dockingSource = this.toolBar.getParent();
/*  972 */         this.constraintBeforeFloating = calculateConstraint();
/*  973 */         Point localPoint3 = this.dockingSource.getLocationOnScreen();
/*  974 */         Point localPoint4 = new Point(((Point)localObject1).x - localPoint3.x, ((Point)localObject1).y - localPoint3.y);
/*      */         Object localObject2;
/*  976 */         if (canDock(this.dockingSource, localPoint4)) {
/*  977 */           this.dragWindow.setBackground(getDockingColor());
/*  978 */           localObject2 = getDockingConstraint(this.dockingSource, localPoint4);
/*      */ 
/*  980 */           int i = mapConstraintToOrientation((String)localObject2);
/*  981 */           this.dragWindow.setOrientation(i);
/*  982 */           this.dragWindow.setBorderColor(this.dockingBorderColor);
/*      */         } else {
/*  984 */           this.dragWindow.setBackground(getFloatingColor());
/*  985 */           this.dragWindow.setBorderColor(this.floatingBorderColor);
/*  986 */           this.dragWindow.setOrientation(this.toolBar.getOrientation());
/*      */         }
/*      */ 
/*  989 */         this.dragWindow.setLocation(localPoint2.x, localPoint2.y);
/*  990 */         if (!this.dragWindow.isVisible()) {
/*  991 */           localObject2 = this.toolBar.getPreferredSize();
/*  992 */           this.dragWindow.setSize(((Dimension)localObject2).width, ((Dimension)localObject2).height);
/*  993 */           this.dragWindow.show();
/*      */         }
/*      */       }
/*      */       catch (IllegalComponentStateException localIllegalComponentStateException)
/*      */       {
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void floatAt(Point paramPoint1, Point paramPoint2)
/*      */   {
/* 1004 */     if (this.toolBar.isFloatable())
/*      */     {
/*      */       try
/*      */       {
/* 1008 */         Point localPoint1 = this.dragWindow.getOffset();
/* 1009 */         if (localPoint1 == null) {
/* 1010 */           localPoint1 = paramPoint1;
/* 1011 */           this.dragWindow.setOffset(localPoint1);
/*      */         }
/* 1013 */         Point localPoint2 = new Point(paramPoint2.x + paramPoint1.x, paramPoint2.y + paramPoint1.y);
/*      */ 
/* 1015 */         setFloatingLocation(localPoint2.x - localPoint1.x, localPoint2.y - localPoint1.y);
/*      */ 
/* 1017 */         if (this.dockingSource != null) {
/* 1018 */           Point localPoint3 = this.dockingSource.getLocationOnScreen();
/* 1019 */           Point localPoint4 = new Point(localPoint2.x - localPoint3.x, localPoint2.y - localPoint3.y);
/*      */ 
/* 1021 */           if (canDock(this.dockingSource, localPoint4))
/* 1022 */             setFloating(false, localPoint4);
/*      */           else
/* 1024 */             setFloating(true, null);
/*      */         }
/*      */         else {
/* 1027 */           setFloating(true, null);
/*      */         }
/* 1029 */         this.dragWindow.setOffset(null);
/*      */       }
/*      */       catch (IllegalComponentStateException localIllegalComponentStateException)
/*      */       {
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private Handler getHandler() {
/* 1038 */     if (this.handler == null) {
/* 1039 */       this.handler = new Handler(null);
/*      */     }
/* 1041 */     return this.handler;
/*      */   }
/*      */ 
/*      */   protected ContainerListener createToolBarContListener()
/*      */   {
/* 1046 */     return getHandler();
/*      */   }
/*      */ 
/*      */   protected FocusListener createToolBarFocusListener()
/*      */   {
/* 1051 */     return getHandler();
/*      */   }
/*      */ 
/*      */   protected PropertyChangeListener createPropertyListener()
/*      */   {
/* 1056 */     return getHandler();
/*      */   }
/*      */ 
/*      */   protected MouseInputListener createDockingListener() {
/* 1060 */     getHandler().tb = this.toolBar;
/* 1061 */     return getHandler();
/*      */   }
/*      */ 
/*      */   protected WindowListener createFrameListener() {
/* 1065 */     return new FrameListener();
/*      */   }
/*      */ 
/*      */   protected void paintDragWindow(Graphics paramGraphics)
/*      */   {
/* 1076 */     paramGraphics.setColor(this.dragWindow.getBackground());
/* 1077 */     int i = this.dragWindow.getWidth();
/* 1078 */     int j = this.dragWindow.getHeight();
/* 1079 */     paramGraphics.fillRect(0, 0, i, j);
/* 1080 */     paramGraphics.setColor(this.dragWindow.getBorderColor());
/* 1081 */     paramGraphics.drawRect(0, 0, i - 1, j - 1);
/*      */   }
/*      */   private static class Actions extends UIAction {
/*      */     private static final String NAVIGATE_RIGHT = "navigateRight";
/*      */     private static final String NAVIGATE_LEFT = "navigateLeft";
/*      */     private static final String NAVIGATE_UP = "navigateUp";
/*      */     private static final String NAVIGATE_DOWN = "navigateDown";
/*      */ 
/* 1092 */     public Actions(String paramString) { super(); }
/*      */ 
/*      */     public void actionPerformed(ActionEvent paramActionEvent)
/*      */     {
/* 1096 */       String str = getName();
/* 1097 */       JToolBar localJToolBar = (JToolBar)paramActionEvent.getSource();
/* 1098 */       BasicToolBarUI localBasicToolBarUI = (BasicToolBarUI)BasicLookAndFeel.getUIOfType(localJToolBar.getUI(), BasicToolBarUI.class);
/*      */ 
/* 1101 */       if ("navigateRight" == str)
/* 1102 */         localBasicToolBarUI.navigateFocusedComp(3);
/* 1103 */       else if ("navigateLeft" == str)
/* 1104 */         localBasicToolBarUI.navigateFocusedComp(7);
/* 1105 */       else if ("navigateUp" == str)
/* 1106 */         localBasicToolBarUI.navigateFocusedComp(1);
/* 1107 */       else if ("navigateDown" == str)
/* 1108 */         localBasicToolBarUI.navigateFocusedComp(5);
/*      */     }
/*      */   }
/*      */ 
/*      */   public class DockingListener
/*      */     implements MouseInputListener
/*      */   {
/*      */     protected JToolBar toolBar;
/* 1325 */     protected boolean isDragging = false;
/* 1326 */     protected Point origin = null;
/*      */ 
/*      */     public DockingListener(JToolBar arg2)
/*      */     {
/*      */       Object localObject;
/* 1329 */       this.toolBar = localObject;
/* 1330 */       BasicToolBarUI.this.getHandler().tb = localObject;
/*      */     }
/*      */ 
/*      */     public void mouseClicked(MouseEvent paramMouseEvent) {
/* 1334 */       BasicToolBarUI.this.getHandler().mouseClicked(paramMouseEvent);
/*      */     }
/*      */ 
/*      */     public void mousePressed(MouseEvent paramMouseEvent) {
/* 1338 */       BasicToolBarUI.this.getHandler().tb = this.toolBar;
/* 1339 */       BasicToolBarUI.this.getHandler().mousePressed(paramMouseEvent);
/* 1340 */       this.isDragging = BasicToolBarUI.this.getHandler().isDragging;
/*      */     }
/*      */ 
/*      */     public void mouseReleased(MouseEvent paramMouseEvent) {
/* 1344 */       BasicToolBarUI.this.getHandler().tb = this.toolBar;
/* 1345 */       BasicToolBarUI.this.getHandler().isDragging = this.isDragging;
/* 1346 */       BasicToolBarUI.this.getHandler().origin = this.origin;
/* 1347 */       BasicToolBarUI.this.getHandler().mouseReleased(paramMouseEvent);
/* 1348 */       this.isDragging = BasicToolBarUI.this.getHandler().isDragging;
/* 1349 */       this.origin = BasicToolBarUI.this.getHandler().origin;
/*      */     }
/*      */ 
/*      */     public void mouseEntered(MouseEvent paramMouseEvent) {
/* 1353 */       BasicToolBarUI.this.getHandler().mouseEntered(paramMouseEvent);
/*      */     }
/*      */ 
/*      */     public void mouseExited(MouseEvent paramMouseEvent) {
/* 1357 */       BasicToolBarUI.this.getHandler().mouseExited(paramMouseEvent);
/*      */     }
/*      */ 
/*      */     public void mouseDragged(MouseEvent paramMouseEvent) {
/* 1361 */       BasicToolBarUI.this.getHandler().tb = this.toolBar;
/* 1362 */       BasicToolBarUI.this.getHandler().origin = this.origin;
/* 1363 */       BasicToolBarUI.this.getHandler().mouseDragged(paramMouseEvent);
/* 1364 */       this.isDragging = BasicToolBarUI.this.getHandler().isDragging;
/* 1365 */       this.origin = BasicToolBarUI.this.getHandler().origin;
/*      */     }
/*      */ 
/*      */     public void mouseMoved(MouseEvent paramMouseEvent) {
/* 1369 */       BasicToolBarUI.this.getHandler().mouseMoved(paramMouseEvent);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected class DragWindow extends Window {
/* 1375 */     Color borderColor = Color.gray;
/* 1376 */     int orientation = BasicToolBarUI.this.toolBar.getOrientation();
/*      */     Point offset;
/*      */ 
/* 1380 */     DragWindow(Window arg2) { super(); }
/*      */ 
/*      */ 
/*      */     public int getOrientation()
/*      */     {
/* 1392 */       return this.orientation;
/*      */     }
/*      */ 
/*      */     public void setOrientation(int paramInt) {
/* 1396 */       if (isShowing()) {
/* 1397 */         if (paramInt == this.orientation)
/* 1398 */           return;
/* 1399 */         this.orientation = paramInt;
/* 1400 */         Dimension localDimension = getSize();
/* 1401 */         setSize(new Dimension(localDimension.height, localDimension.width));
/* 1402 */         if (this.offset != null) {
/* 1403 */           if (BasicGraphicsUtils.isLeftToRight(BasicToolBarUI.this.toolBar))
/* 1404 */             setOffset(new Point(this.offset.y, this.offset.x));
/* 1405 */           else if (paramInt == 0)
/* 1406 */             setOffset(new Point(localDimension.height - this.offset.y, this.offset.x));
/*      */           else {
/* 1408 */             setOffset(new Point(this.offset.y, localDimension.width - this.offset.x));
/*      */           }
/*      */         }
/* 1411 */         repaint();
/*      */       }
/*      */     }
/*      */ 
/*      */     public Point getOffset() {
/* 1416 */       return this.offset;
/*      */     }
/*      */ 
/*      */     public void setOffset(Point paramPoint) {
/* 1420 */       this.offset = paramPoint;
/*      */     }
/*      */ 
/*      */     public void setBorderColor(Color paramColor) {
/* 1424 */       if (this.borderColor == paramColor)
/* 1425 */         return;
/* 1426 */       this.borderColor = paramColor;
/* 1427 */       repaint();
/*      */     }
/*      */ 
/*      */     public Color getBorderColor() {
/* 1431 */       return this.borderColor;
/*      */     }
/*      */ 
/*      */     public void paint(Graphics paramGraphics) {
/* 1435 */       BasicToolBarUI.this.paintDragWindow(paramGraphics);
/*      */ 
/* 1437 */       super.paint(paramGraphics);
/*      */     }
/*      */     public Insets getInsets() {
/* 1440 */       return new Insets(1, 1, 1, 1);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected class FrameListener extends WindowAdapter
/*      */   {
/*      */     protected FrameListener()
/*      */     {
/*      */     }
/*      */ 
/*      */     public void windowClosing(WindowEvent paramWindowEvent)
/*      */     {
/* 1243 */       if (BasicToolBarUI.this.toolBar.isFloatable()) {
/* 1244 */         if (BasicToolBarUI.this.dragWindow != null)
/* 1245 */           BasicToolBarUI.this.dragWindow.setVisible(false);
/* 1246 */         BasicToolBarUI.this.floating = false;
/* 1247 */         if (BasicToolBarUI.this.floatingToolBar == null)
/* 1248 */           BasicToolBarUI.this.floatingToolBar = BasicToolBarUI.this.createFloatingWindow(BasicToolBarUI.this.toolBar);
/* 1249 */         if ((BasicToolBarUI.this.floatingToolBar instanceof Window)) ((Window)BasicToolBarUI.this.floatingToolBar).setVisible(false);
/* 1250 */         BasicToolBarUI.this.floatingToolBar.getContentPane().remove(BasicToolBarUI.this.toolBar);
/* 1251 */         String str = BasicToolBarUI.this.constraintBeforeFloating;
/* 1252 */         if (BasicToolBarUI.this.toolBar.getOrientation() == 0) {
/* 1253 */           if ((str == "West") || (str == "East")) {
/* 1254 */             str = "North";
/*      */           }
/*      */         }
/* 1257 */         else if ((str == "North") || (str == "South")) {
/* 1258 */           str = "West";
/*      */         }
/*      */ 
/* 1261 */         if (BasicToolBarUI.this.dockingSource == null)
/* 1262 */           BasicToolBarUI.this.dockingSource = BasicToolBarUI.this.toolBar.getParent();
/* 1263 */         if (BasicToolBarUI.this.propertyListener != null)
/* 1264 */           UIManager.removePropertyChangeListener(BasicToolBarUI.this.propertyListener);
/* 1265 */         BasicToolBarUI.this.dockingSource.add(BasicToolBarUI.this.toolBar, str);
/* 1266 */         BasicToolBarUI.this.dockingSource.invalidate();
/* 1267 */         Container localContainer = BasicToolBarUI.this.dockingSource.getParent();
/* 1268 */         if (localContainer != null)
/* 1269 */           localContainer.validate();
/* 1270 */         BasicToolBarUI.this.dockingSource.repaint();
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private class Handler
/*      */     implements ContainerListener, FocusListener, MouseInputListener, PropertyChangeListener
/*      */   {
/*      */     JToolBar tb;
/* 1161 */     boolean isDragging = false;
/* 1162 */     Point origin = null;
/*      */ 
/*      */     private Handler()
/*      */     {
/*      */     }
/*      */ 
/*      */     public void componentAdded(ContainerEvent paramContainerEvent)
/*      */     {
/* 1121 */       Component localComponent = paramContainerEvent.getChild();
/*      */ 
/* 1123 */       if (BasicToolBarUI.this.toolBarFocusListener != null) {
/* 1124 */         localComponent.addFocusListener(BasicToolBarUI.this.toolBarFocusListener);
/*      */       }
/*      */ 
/* 1127 */       if (BasicToolBarUI.this.isRolloverBorders())
/* 1128 */         BasicToolBarUI.this.setBorderToRollover(localComponent);
/*      */       else
/* 1130 */         BasicToolBarUI.this.setBorderToNonRollover(localComponent);
/*      */     }
/*      */ 
/*      */     public void componentRemoved(ContainerEvent paramContainerEvent)
/*      */     {
/* 1135 */       Component localComponent = paramContainerEvent.getChild();
/*      */ 
/* 1137 */       if (BasicToolBarUI.this.toolBarFocusListener != null) {
/* 1138 */         localComponent.removeFocusListener(BasicToolBarUI.this.toolBarFocusListener);
/*      */       }
/*      */ 
/* 1142 */       BasicToolBarUI.this.setBorderToNormal(localComponent);
/*      */     }
/*      */ 
/*      */     public void focusGained(FocusEvent paramFocusEvent)
/*      */     {
/* 1150 */       Component localComponent = paramFocusEvent.getComponent();
/* 1151 */       BasicToolBarUI.this.focusedCompIndex = BasicToolBarUI.this.toolBar.getComponentIndex(localComponent);
/*      */     }
/*      */ 
/*      */     public void focusLost(FocusEvent paramFocusEvent)
/*      */     {
/*      */     }
/*      */ 
/*      */     public void mousePressed(MouseEvent paramMouseEvent)
/*      */     {
/* 1165 */       if (!this.tb.isEnabled()) {
/* 1166 */         return;
/*      */       }
/* 1168 */       this.isDragging = false;
/*      */     }
/*      */ 
/*      */     public void mouseReleased(MouseEvent paramMouseEvent) {
/* 1172 */       if (!this.tb.isEnabled()) {
/* 1173 */         return;
/*      */       }
/* 1175 */       if (this.isDragging) {
/* 1176 */         Point localPoint = paramMouseEvent.getPoint();
/* 1177 */         if (this.origin == null)
/* 1178 */           this.origin = paramMouseEvent.getComponent().getLocationOnScreen();
/* 1179 */         BasicToolBarUI.this.floatAt(localPoint, this.origin);
/*      */       }
/* 1181 */       this.origin = null;
/* 1182 */       this.isDragging = false;
/*      */     }
/*      */ 
/*      */     public void mouseDragged(MouseEvent paramMouseEvent) {
/* 1186 */       if (!this.tb.isEnabled()) {
/* 1187 */         return;
/*      */       }
/* 1189 */       this.isDragging = true;
/* 1190 */       Point localPoint = paramMouseEvent.getPoint();
/* 1191 */       if (this.origin == null) {
/* 1192 */         this.origin = paramMouseEvent.getComponent().getLocationOnScreen();
/*      */       }
/* 1194 */       BasicToolBarUI.this.dragTo(localPoint, this.origin);
/*      */     }
/*      */     public void mouseClicked(MouseEvent paramMouseEvent) {
/*      */     }
/*      */     public void mouseEntered(MouseEvent paramMouseEvent) {
/*      */     }
/*      */     public void mouseExited(MouseEvent paramMouseEvent) {
/*      */     }
/*      */ 
/*      */     public void mouseMoved(MouseEvent paramMouseEvent) {
/*      */     }
/*      */ 
/*      */     public void propertyChange(PropertyChangeEvent paramPropertyChangeEvent) {
/* 1207 */       String str = paramPropertyChangeEvent.getPropertyName();
/* 1208 */       if (str == "lookAndFeel") {
/* 1209 */         BasicToolBarUI.this.toolBar.updateUI();
/* 1210 */       } else if (str == "orientation")
/*      */       {
/* 1213 */         Component[] arrayOfComponent = BasicToolBarUI.this.toolBar.getComponents();
/* 1214 */         int i = ((Integer)paramPropertyChangeEvent.getNewValue()).intValue();
/*      */ 
/* 1217 */         for (int j = 0; j < arrayOfComponent.length; j++)
/* 1218 */           if ((arrayOfComponent[j] instanceof JToolBar.Separator)) {
/* 1219 */             JToolBar.Separator localSeparator = (JToolBar.Separator)arrayOfComponent[j];
/* 1220 */             if (i == 0)
/* 1221 */               localSeparator.setOrientation(1);
/*      */             else {
/* 1223 */               localSeparator.setOrientation(0);
/*      */             }
/* 1225 */             Dimension localDimension1 = localSeparator.getSeparatorSize();
/* 1226 */             if ((localDimension1 != null) && (localDimension1.width != localDimension1.height))
/*      */             {
/* 1228 */               Dimension localDimension2 = new Dimension(localDimension1.height, localDimension1.width);
/*      */ 
/* 1230 */               localSeparator.setSeparatorSize(localDimension2);
/*      */             }
/*      */           }
/*      */       }
/* 1234 */       else if (str == BasicToolBarUI.IS_ROLLOVER) {
/* 1235 */         BasicToolBarUI.this.installNormalBorders(BasicToolBarUI.this.toolBar);
/* 1236 */         BasicToolBarUI.this.setRolloverBorders(((Boolean)paramPropertyChangeEvent.getNewValue()).booleanValue());
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   protected class PropertyListener
/*      */     implements PropertyChangeListener
/*      */   {
/*      */     protected PropertyListener()
/*      */     {
/*      */     }
/*      */ 
/*      */     public void propertyChange(PropertyChangeEvent paramPropertyChangeEvent)
/*      */     {
/* 1311 */       BasicToolBarUI.this.getHandler().propertyChange(paramPropertyChangeEvent);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected class ToolBarContListener
/*      */     implements ContainerListener
/*      */   {
/*      */     protected ToolBarContListener()
/*      */     {
/*      */     }
/*      */ 
/*      */     public void componentAdded(ContainerEvent paramContainerEvent)
/*      */     {
/* 1282 */       BasicToolBarUI.this.getHandler().componentAdded(paramContainerEvent);
/*      */     }
/*      */ 
/*      */     public void componentRemoved(ContainerEvent paramContainerEvent) {
/* 1286 */       BasicToolBarUI.this.getHandler().componentRemoved(paramContainerEvent);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected class ToolBarFocusListener implements FocusListener
/*      */   {
/*      */     protected ToolBarFocusListener()
/*      */     {
/*      */     }
/*      */ 
/*      */     public void focusGained(FocusEvent paramFocusEvent) {
/* 1297 */       BasicToolBarUI.this.getHandler().focusGained(paramFocusEvent);
/*      */     }
/*      */ 
/*      */     public void focusLost(FocusEvent paramFocusEvent) {
/* 1301 */       BasicToolBarUI.this.getHandler().focusLost(paramFocusEvent);
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.basic.BasicToolBarUI
 * JD-Core Version:    0.6.2
 */