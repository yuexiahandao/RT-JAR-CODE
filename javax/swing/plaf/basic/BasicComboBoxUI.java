/*      */ package javax.swing.plaf.basic;
/*      */ 
/*      */ import java.awt.Color;
/*      */ import java.awt.Component;
/*      */ import java.awt.Component.BaselineResizeBehavior;
/*      */ import java.awt.Container;
/*      */ import java.awt.Dimension;
/*      */ import java.awt.EventQueue;
/*      */ import java.awt.Graphics;
/*      */ import java.awt.Insets;
/*      */ import java.awt.LayoutManager;
/*      */ import java.awt.Rectangle;
/*      */ import java.awt.event.ActionEvent;
/*      */ import java.awt.event.ActionListener;
/*      */ import java.awt.event.FocusEvent;
/*      */ import java.awt.event.FocusListener;
/*      */ import java.awt.event.ItemEvent;
/*      */ import java.awt.event.ItemListener;
/*      */ import java.awt.event.KeyAdapter;
/*      */ import java.awt.event.KeyEvent;
/*      */ import java.awt.event.KeyListener;
/*      */ import java.awt.event.MouseListener;
/*      */ import java.awt.event.MouseMotionListener;
/*      */ import java.beans.PropertyChangeEvent;
/*      */ import java.beans.PropertyChangeListener;
/*      */ import javax.accessibility.Accessible;
/*      */ import javax.accessibility.AccessibleContext;
/*      */ import javax.swing.Action;
/*      */ import javax.swing.ActionMap;
/*      */ import javax.swing.CellRendererPane;
/*      */ import javax.swing.ComboBoxEditor;
/*      */ import javax.swing.ComboBoxModel;
/*      */ import javax.swing.DefaultListCellRenderer;
/*      */ import javax.swing.InputMap;
/*      */ import javax.swing.JButton;
/*      */ import javax.swing.JComboBox;
/*      */ import javax.swing.JComboBox.KeySelectionManager;
/*      */ import javax.swing.JComponent;
/*      */ import javax.swing.JList;
/*      */ import javax.swing.JPanel;
/*      */ import javax.swing.JRootPane;
/*      */ import javax.swing.KeyStroke;
/*      */ import javax.swing.ListCellRenderer;
/*      */ import javax.swing.LookAndFeel;
/*      */ import javax.swing.SwingUtilities;
/*      */ import javax.swing.UIManager;
/*      */ import javax.swing.event.ListDataEvent;
/*      */ import javax.swing.event.ListDataListener;
/*      */ import javax.swing.plaf.ComboBoxUI;
/*      */ import javax.swing.plaf.ComponentUI;
/*      */ import javax.swing.plaf.UIResource;
/*      */ import javax.swing.text.Position.Bias;
/*      */ import sun.awt.AppContext;
/*      */ import sun.swing.DefaultLookup;
/*      */ import sun.swing.UIAction;
/*      */ 
/*      */ public class BasicComboBoxUI extends ComboBoxUI
/*      */ {
/*      */   protected JComboBox comboBox;
/*      */   protected boolean hasFocus;
/*      */   private boolean isTableCellEditor;
/*      */   private static final String IS_TABLE_CELL_EDITOR = "JComboBox.isTableCellEditor";
/*      */   protected JList listBox;
/*      */   protected CellRendererPane currentValuePane;
/*      */   protected ComboPopup popup;
/*      */   protected Component editor;
/*      */   protected JButton arrowButton;
/*      */   protected KeyListener keyListener;
/*      */   protected FocusListener focusListener;
/*      */   protected PropertyChangeListener propertyChangeListener;
/*      */   protected ItemListener itemListener;
/*      */   protected MouseListener popupMouseListener;
/*      */   protected MouseMotionListener popupMouseMotionListener;
/*      */   protected KeyListener popupKeyListener;
/*      */   protected ListDataListener listDataListener;
/*      */   private Handler handler;
/*      */   private long timeFactor;
/*      */   private long lastTime;
/*      */   private long time;
/*      */   JComboBox.KeySelectionManager keySelectionManager;
/*      */   protected boolean isMinimumSizeDirty;
/*      */   protected Dimension cachedMinimumSize;
/*      */   private boolean isDisplaySizeDirty;
/*      */   private Dimension cachedDisplaySize;
/*  177 */   private static final Object COMBO_UI_LIST_CELL_RENDERER_KEY = new StringBuffer("DefaultListCellRendererKey");
/*      */ 
/*  180 */   static final StringBuffer HIDE_POPUP_KEY = new StringBuffer("HidePopupKey");
/*      */   private boolean sameBaseline;
/*      */   protected boolean squareButton;
/*      */   protected Insets padding;
/*      */ 
/*      */   public BasicComboBoxUI()
/*      */   {
/*   70 */     this.hasFocus = false;
/*      */ 
/*   74 */     this.isTableCellEditor = false;
/*      */ 
/*   82 */     this.currentValuePane = new CellRendererPane();
/*      */ 
/*  150 */     this.timeFactor = 1000L;
/*      */ 
/*  156 */     this.lastTime = 0L;
/*  157 */     this.time = 0L;
/*      */ 
/*  165 */     this.isMinimumSizeDirty = true;
/*      */ 
/*  168 */     this.cachedMinimumSize = new Dimension(0, 0);
/*      */ 
/*  171 */     this.isDisplaySizeDirty = true;
/*      */ 
/*  174 */     this.cachedDisplaySize = new Dimension(0, 0);
/*      */ 
/*  195 */     this.squareButton = true;
/*      */   }
/*      */ 
/*      */   private static ListCellRenderer getDefaultListCellRenderer()
/*      */   {
/*  208 */     Object localObject = (ListCellRenderer)AppContext.getAppContext().get(COMBO_UI_LIST_CELL_RENDERER_KEY);
/*      */ 
/*  211 */     if (localObject == null) {
/*  212 */       localObject = new DefaultListCellRenderer();
/*  213 */       AppContext.getAppContext().put(COMBO_UI_LIST_CELL_RENDERER_KEY, new DefaultListCellRenderer());
/*      */     }
/*      */ 
/*  216 */     return localObject;
/*      */   }
/*      */ 
/*      */   static void loadActionMap(LazyActionMap paramLazyActionMap)
/*      */   {
/*  223 */     paramLazyActionMap.put(new Actions("hidePopup"));
/*  224 */     paramLazyActionMap.put(new Actions("pageDownPassThrough"));
/*  225 */     paramLazyActionMap.put(new Actions("pageUpPassThrough"));
/*  226 */     paramLazyActionMap.put(new Actions("homePassThrough"));
/*  227 */     paramLazyActionMap.put(new Actions("endPassThrough"));
/*  228 */     paramLazyActionMap.put(new Actions("selectNext"));
/*  229 */     paramLazyActionMap.put(new Actions("selectNext2"));
/*  230 */     paramLazyActionMap.put(new Actions("togglePopup"));
/*  231 */     paramLazyActionMap.put(new Actions("spacePopup"));
/*  232 */     paramLazyActionMap.put(new Actions("selectPrevious"));
/*  233 */     paramLazyActionMap.put(new Actions("selectPrevious2"));
/*  234 */     paramLazyActionMap.put(new Actions("enterPressed"));
/*      */   }
/*      */ 
/*      */   public static ComponentUI createUI(JComponent paramJComponent)
/*      */   {
/*  242 */     return new BasicComboBoxUI();
/*      */   }
/*      */ 
/*      */   public void installUI(JComponent paramJComponent)
/*      */   {
/*  247 */     this.isMinimumSizeDirty = true;
/*      */ 
/*  249 */     this.comboBox = ((JComboBox)paramJComponent);
/*  250 */     installDefaults();
/*  251 */     this.popup = createPopup();
/*  252 */     this.listBox = this.popup.getList();
/*      */ 
/*  255 */     Boolean localBoolean = (Boolean)paramJComponent.getClientProperty("JComboBox.isTableCellEditor");
/*  256 */     if (localBoolean != null) {
/*  257 */       this.isTableCellEditor = (localBoolean.equals(Boolean.TRUE));
/*      */     }
/*      */ 
/*  260 */     if ((this.comboBox.getRenderer() == null) || ((this.comboBox.getRenderer() instanceof UIResource))) {
/*  261 */       this.comboBox.setRenderer(createRenderer());
/*      */     }
/*      */ 
/*  264 */     if ((this.comboBox.getEditor() == null) || ((this.comboBox.getEditor() instanceof UIResource))) {
/*  265 */       this.comboBox.setEditor(createEditor());
/*      */     }
/*      */ 
/*  268 */     installListeners();
/*  269 */     installComponents();
/*      */ 
/*  271 */     this.comboBox.setLayout(createLayoutManager());
/*      */ 
/*  273 */     this.comboBox.setRequestFocusEnabled(true);
/*      */ 
/*  275 */     installKeyboardActions();
/*      */ 
/*  277 */     this.comboBox.putClientProperty("doNotCancelPopup", HIDE_POPUP_KEY);
/*      */ 
/*  279 */     if ((this.keySelectionManager == null) || ((this.keySelectionManager instanceof UIResource))) {
/*  280 */       this.keySelectionManager = new DefaultKeySelectionManager();
/*      */     }
/*  282 */     this.comboBox.setKeySelectionManager(this.keySelectionManager);
/*      */   }
/*      */ 
/*      */   public void uninstallUI(JComponent paramJComponent)
/*      */   {
/*  287 */     setPopupVisible(this.comboBox, false);
/*  288 */     this.popup.uninstallingUI();
/*      */ 
/*  290 */     uninstallKeyboardActions();
/*      */ 
/*  292 */     this.comboBox.setLayout(null);
/*      */ 
/*  294 */     uninstallComponents();
/*  295 */     uninstallListeners();
/*  296 */     uninstallDefaults();
/*      */ 
/*  298 */     if ((this.comboBox.getRenderer() == null) || ((this.comboBox.getRenderer() instanceof UIResource))) {
/*  299 */       this.comboBox.setRenderer(null);
/*      */     }
/*      */ 
/*  302 */     ComboBoxEditor localComboBoxEditor = this.comboBox.getEditor();
/*  303 */     if ((localComboBoxEditor instanceof UIResource)) {
/*  304 */       if (localComboBoxEditor.getEditorComponent().hasFocus())
/*      */       {
/*  306 */         this.comboBox.requestFocusInWindow();
/*      */       }
/*  308 */       this.comboBox.setEditor(null);
/*      */     }
/*      */ 
/*  311 */     if ((this.keySelectionManager instanceof UIResource)) {
/*  312 */       this.comboBox.setKeySelectionManager(null);
/*      */     }
/*      */ 
/*  315 */     this.handler = null;
/*  316 */     this.keyListener = null;
/*  317 */     this.focusListener = null;
/*  318 */     this.listDataListener = null;
/*  319 */     this.propertyChangeListener = null;
/*  320 */     this.popup = null;
/*  321 */     this.listBox = null;
/*  322 */     this.comboBox = null;
/*      */   }
/*      */ 
/*      */   protected void installDefaults()
/*      */   {
/*  330 */     LookAndFeel.installColorsAndFont(this.comboBox, "ComboBox.background", "ComboBox.foreground", "ComboBox.font");
/*      */ 
/*  334 */     LookAndFeel.installBorder(this.comboBox, "ComboBox.border");
/*  335 */     LookAndFeel.installProperty(this.comboBox, "opaque", Boolean.TRUE);
/*      */ 
/*  337 */     Long localLong = (Long)UIManager.get("ComboBox.timeFactor");
/*  338 */     this.timeFactor = (localLong == null ? 1000L : localLong.longValue());
/*      */ 
/*  341 */     Boolean localBoolean = (Boolean)UIManager.get("ComboBox.squareButton");
/*  342 */     this.squareButton = (localBoolean == null ? true : localBoolean.booleanValue());
/*      */ 
/*  344 */     this.padding = UIManager.getInsets("ComboBox.padding");
/*      */   }
/*      */ 
/*      */   protected void installListeners()
/*      */   {
/*  352 */     if ((this.itemListener = createItemListener()) != null) {
/*  353 */       this.comboBox.addItemListener(this.itemListener);
/*      */     }
/*  355 */     if ((this.propertyChangeListener = createPropertyChangeListener()) != null) {
/*  356 */       this.comboBox.addPropertyChangeListener(this.propertyChangeListener);
/*      */     }
/*  358 */     if ((this.keyListener = createKeyListener()) != null) {
/*  359 */       this.comboBox.addKeyListener(this.keyListener);
/*      */     }
/*  361 */     if ((this.focusListener = createFocusListener()) != null) {
/*  362 */       this.comboBox.addFocusListener(this.focusListener);
/*      */     }
/*  364 */     if ((this.popupMouseListener = this.popup.getMouseListener()) != null) {
/*  365 */       this.comboBox.addMouseListener(this.popupMouseListener);
/*      */     }
/*  367 */     if ((this.popupMouseMotionListener = this.popup.getMouseMotionListener()) != null) {
/*  368 */       this.comboBox.addMouseMotionListener(this.popupMouseMotionListener);
/*      */     }
/*  370 */     if ((this.popupKeyListener = this.popup.getKeyListener()) != null) {
/*  371 */       this.comboBox.addKeyListener(this.popupKeyListener);
/*      */     }
/*      */ 
/*  374 */     if ((this.comboBox.getModel() != null) && 
/*  375 */       ((this.listDataListener = createListDataListener()) != null))
/*  376 */       this.comboBox.getModel().addListDataListener(this.listDataListener);
/*      */   }
/*      */ 
/*      */   protected void uninstallDefaults()
/*      */   {
/*  386 */     LookAndFeel.installColorsAndFont(this.comboBox, "ComboBox.background", "ComboBox.foreground", "ComboBox.font");
/*      */ 
/*  390 */     LookAndFeel.uninstallBorder(this.comboBox);
/*      */   }
/*      */ 
/*      */   protected void uninstallListeners()
/*      */   {
/*  399 */     if (this.keyListener != null) {
/*  400 */       this.comboBox.removeKeyListener(this.keyListener);
/*      */     }
/*  402 */     if (this.itemListener != null) {
/*  403 */       this.comboBox.removeItemListener(this.itemListener);
/*      */     }
/*  405 */     if (this.propertyChangeListener != null) {
/*  406 */       this.comboBox.removePropertyChangeListener(this.propertyChangeListener);
/*      */     }
/*  408 */     if (this.focusListener != null) {
/*  409 */       this.comboBox.removeFocusListener(this.focusListener);
/*      */     }
/*  411 */     if (this.popupMouseListener != null) {
/*  412 */       this.comboBox.removeMouseListener(this.popupMouseListener);
/*      */     }
/*  414 */     if (this.popupMouseMotionListener != null) {
/*  415 */       this.comboBox.removeMouseMotionListener(this.popupMouseMotionListener);
/*      */     }
/*  417 */     if (this.popupKeyListener != null) {
/*  418 */       this.comboBox.removeKeyListener(this.popupKeyListener);
/*      */     }
/*  420 */     if ((this.comboBox.getModel() != null) && 
/*  421 */       (this.listDataListener != null))
/*  422 */       this.comboBox.getModel().removeListDataListener(this.listDataListener);
/*      */   }
/*      */ 
/*      */   protected ComboPopup createPopup()
/*      */   {
/*  434 */     return new BasicComboPopup(this.comboBox);
/*      */   }
/*      */ 
/*      */   protected KeyListener createKeyListener()
/*      */   {
/*  445 */     return getHandler();
/*      */   }
/*      */ 
/*      */   protected FocusListener createFocusListener()
/*      */   {
/*  455 */     return getHandler();
/*      */   }
/*      */ 
/*      */   protected ListDataListener createListDataListener()
/*      */   {
/*  466 */     return getHandler();
/*      */   }
/*      */ 
/*      */   protected ItemListener createItemListener()
/*      */   {
/*  480 */     return null;
/*      */   }
/*      */ 
/*      */   protected PropertyChangeListener createPropertyChangeListener()
/*      */   {
/*  491 */     return getHandler();
/*      */   }
/*      */ 
/*      */   protected LayoutManager createLayoutManager()
/*      */   {
/*  501 */     return getHandler();
/*      */   }
/*      */ 
/*      */   protected ListCellRenderer createRenderer()
/*      */   {
/*  513 */     return new BasicComboBoxRenderer.UIResource();
/*      */   }
/*      */ 
/*      */   protected ComboBoxEditor createEditor()
/*      */   {
/*  525 */     return new BasicComboBoxEditor.UIResource();
/*      */   }
/*      */ 
/*      */   private Handler getHandler()
/*      */   {
/*  532 */     if (this.handler == null) {
/*  533 */       this.handler = new Handler(null);
/*      */     }
/*  535 */     return this.handler;
/*      */   }
/*      */ 
/*      */   private void updateToolTipTextForChildren()
/*      */   {
/*  644 */     Component[] arrayOfComponent = this.comboBox.getComponents();
/*  645 */     for (int i = 0; i < arrayOfComponent.length; i++)
/*  646 */       if ((arrayOfComponent[i] instanceof JComponent))
/*  647 */         ((JComponent)arrayOfComponent[i]).setToolTipText(this.comboBox.getToolTipText());
/*      */   }
/*      */ 
/*      */   protected void installComponents()
/*      */   {
/*  694 */     this.arrowButton = createArrowButton();
/*      */ 
/*  696 */     if (this.arrowButton != null) {
/*  697 */       this.comboBox.add(this.arrowButton);
/*  698 */       configureArrowButton();
/*      */     }
/*      */ 
/*  701 */     if (this.comboBox.isEditable()) {
/*  702 */       addEditor();
/*      */     }
/*      */ 
/*  705 */     this.comboBox.add(this.currentValuePane);
/*      */   }
/*      */ 
/*      */   protected void uninstallComponents()
/*      */   {
/*  714 */     if (this.arrowButton != null) {
/*  715 */       unconfigureArrowButton();
/*      */     }
/*  717 */     if (this.editor != null) {
/*  718 */       unconfigureEditor();
/*      */     }
/*  720 */     this.comboBox.removeAll();
/*  721 */     this.arrowButton = null;
/*      */   }
/*      */ 
/*      */   public void addEditor()
/*      */   {
/*  734 */     removeEditor();
/*  735 */     this.editor = this.comboBox.getEditor().getEditorComponent();
/*  736 */     if (this.editor != null) {
/*  737 */       configureEditor();
/*  738 */       this.comboBox.add(this.editor);
/*  739 */       if (this.comboBox.isFocusOwner())
/*      */       {
/*  741 */         this.editor.requestFocusInWindow();
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public void removeEditor()
/*      */   {
/*  753 */     if (this.editor != null) {
/*  754 */       unconfigureEditor();
/*  755 */       this.comboBox.remove(this.editor);
/*  756 */       this.editor = null;
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void configureEditor()
/*      */   {
/*  768 */     this.editor.setEnabled(this.comboBox.isEnabled());
/*      */ 
/*  770 */     this.editor.setFocusable(this.comboBox.isFocusable());
/*      */ 
/*  772 */     this.editor.setFont(this.comboBox.getFont());
/*      */ 
/*  774 */     if (this.focusListener != null) {
/*  775 */       this.editor.addFocusListener(this.focusListener);
/*      */     }
/*      */ 
/*  778 */     this.editor.addFocusListener(getHandler());
/*      */ 
/*  780 */     this.comboBox.getEditor().addActionListener(getHandler());
/*      */ 
/*  782 */     if ((this.editor instanceof JComponent)) {
/*  783 */       ((JComponent)this.editor).putClientProperty("doNotCancelPopup", HIDE_POPUP_KEY);
/*      */ 
/*  785 */       ((JComponent)this.editor).setInheritsPopupMenu(true);
/*      */     }
/*      */ 
/*  788 */     this.comboBox.configureEditor(this.comboBox.getEditor(), this.comboBox.getSelectedItem());
/*      */ 
/*  790 */     this.editor.addPropertyChangeListener(this.propertyChangeListener);
/*      */   }
/*      */ 
/*      */   protected void unconfigureEditor()
/*      */   {
/*  800 */     if (this.focusListener != null) {
/*  801 */       this.editor.removeFocusListener(this.focusListener);
/*      */     }
/*      */ 
/*  804 */     this.editor.removePropertyChangeListener(this.propertyChangeListener);
/*  805 */     this.editor.removeFocusListener(getHandler());
/*  806 */     this.comboBox.getEditor().removeActionListener(getHandler());
/*      */   }
/*      */ 
/*      */   public void configureArrowButton()
/*      */   {
/*  816 */     if (this.arrowButton != null) {
/*  817 */       this.arrowButton.setEnabled(this.comboBox.isEnabled());
/*  818 */       this.arrowButton.setFocusable(this.comboBox.isFocusable());
/*  819 */       this.arrowButton.setRequestFocusEnabled(false);
/*  820 */       this.arrowButton.addMouseListener(this.popup.getMouseListener());
/*  821 */       this.arrowButton.addMouseMotionListener(this.popup.getMouseMotionListener());
/*  822 */       this.arrowButton.resetKeyboardActions();
/*  823 */       this.arrowButton.putClientProperty("doNotCancelPopup", HIDE_POPUP_KEY);
/*  824 */       this.arrowButton.setInheritsPopupMenu(true);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void unconfigureArrowButton()
/*      */   {
/*  835 */     if (this.arrowButton != null) {
/*  836 */       this.arrowButton.removeMouseListener(this.popup.getMouseListener());
/*  837 */       this.arrowButton.removeMouseMotionListener(this.popup.getMouseMotionListener());
/*      */     }
/*      */   }
/*      */ 
/*      */   protected JButton createArrowButton()
/*      */   {
/*  848 */     BasicArrowButton localBasicArrowButton = new BasicArrowButton(5, UIManager.getColor("ComboBox.buttonBackground"), UIManager.getColor("ComboBox.buttonShadow"), UIManager.getColor("ComboBox.buttonDarkShadow"), UIManager.getColor("ComboBox.buttonHighlight"));
/*      */ 
/*  853 */     localBasicArrowButton.setName("ComboBox.arrowButton");
/*  854 */     return localBasicArrowButton;
/*      */   }
/*      */ 
/*      */   public boolean isPopupVisible(JComboBox paramJComboBox)
/*      */   {
/*  870 */     return this.popup.isVisible();
/*      */   }
/*      */ 
/*      */   public void setPopupVisible(JComboBox paramJComboBox, boolean paramBoolean)
/*      */   {
/*  877 */     if (paramBoolean)
/*  878 */       this.popup.show();
/*      */     else
/*  880 */       this.popup.hide();
/*      */   }
/*      */ 
/*      */   public boolean isFocusTraversable(JComboBox paramJComboBox)
/*      */   {
/*  889 */     return !this.comboBox.isEditable();
/*      */   }
/*      */ 
/*      */   public void paint(Graphics paramGraphics, JComponent paramJComponent)
/*      */   {
/*  901 */     this.hasFocus = this.comboBox.hasFocus();
/*  902 */     if (!this.comboBox.isEditable()) {
/*  903 */       Rectangle localRectangle = rectangleForCurrentValue();
/*  904 */       paintCurrentValueBackground(paramGraphics, localRectangle, this.hasFocus);
/*  905 */       paintCurrentValue(paramGraphics, localRectangle, this.hasFocus);
/*      */     }
/*      */   }
/*      */ 
/*      */   public Dimension getPreferredSize(JComponent paramJComponent)
/*      */   {
/*  911 */     return getMinimumSize(paramJComponent);
/*      */   }
/*      */ 
/*      */   public Dimension getMinimumSize(JComponent paramJComponent)
/*      */   {
/*  919 */     if (!this.isMinimumSizeDirty) {
/*  920 */       return new Dimension(this.cachedMinimumSize);
/*      */     }
/*  922 */     Dimension localDimension = getDisplaySize();
/*  923 */     Insets localInsets = getInsets();
/*      */ 
/*  925 */     int i = localDimension.height;
/*  926 */     int j = this.squareButton ? i : this.arrowButton.getPreferredSize().width;
/*      */ 
/*  928 */     localDimension.height += localInsets.top + localInsets.bottom;
/*  929 */     localDimension.width += localInsets.left + localInsets.right + j;
/*      */ 
/*  931 */     this.cachedMinimumSize.setSize(localDimension.width, localDimension.height);
/*  932 */     this.isMinimumSizeDirty = false;
/*      */ 
/*  934 */     return new Dimension(localDimension);
/*      */   }
/*      */ 
/*      */   public Dimension getMaximumSize(JComponent paramJComponent)
/*      */   {
/*  939 */     return new Dimension(32767, 32767);
/*      */   }
/*      */ 
/*      */   public int getBaseline(JComponent paramJComponent, int paramInt1, int paramInt2)
/*      */   {
/*  952 */     super.getBaseline(paramJComponent, paramInt1, paramInt2);
/*  953 */     int i = -1;
/*      */ 
/*  955 */     getDisplaySize();
/*  956 */     if (this.sameBaseline) {
/*  957 */       Insets localInsets = paramJComponent.getInsets();
/*  958 */       paramInt2 = paramInt2 - localInsets.top - localInsets.bottom;
/*  959 */       if (!this.comboBox.isEditable()) {
/*  960 */         Object localObject1 = this.comboBox.getRenderer();
/*  961 */         if (localObject1 == null) {
/*  962 */           localObject1 = new DefaultListCellRenderer();
/*      */         }
/*  964 */         Object localObject2 = null;
/*  965 */         Object localObject3 = this.comboBox.getPrototypeDisplayValue();
/*  966 */         if (localObject3 != null) {
/*  967 */           localObject2 = localObject3;
/*      */         }
/*  969 */         else if (this.comboBox.getModel().getSize() > 0)
/*      */         {
/*  972 */           localObject2 = this.comboBox.getModel().getElementAt(0);
/*      */         }
/*  974 */         if (localObject2 == null)
/*  975 */           localObject2 = " ";
/*  976 */         else if (((localObject2 instanceof String)) && ("".equals(localObject2))) {
/*  977 */           localObject2 = " ";
/*      */         }
/*  979 */         Component localComponent = ((ListCellRenderer)localObject1).getListCellRendererComponent(this.listBox, localObject2, -1, false, false);
/*      */ 
/*  982 */         if ((localComponent instanceof JComponent)) {
/*  983 */           localComponent.setFont(this.comboBox.getFont());
/*      */         }
/*  985 */         i = localComponent.getBaseline(paramInt1, paramInt2);
/*      */       }
/*      */       else {
/*  988 */         i = this.editor.getBaseline(paramInt1, paramInt2);
/*      */       }
/*  990 */       if (i > 0) {
/*  991 */         i += localInsets.top;
/*      */       }
/*      */     }
/*  994 */     return i;
/*      */   }
/*      */ 
/*      */   public Component.BaselineResizeBehavior getBaselineResizeBehavior(JComponent paramJComponent)
/*      */   {
/* 1008 */     super.getBaselineResizeBehavior(paramJComponent);
/*      */ 
/* 1010 */     getDisplaySize();
/* 1011 */     if (this.comboBox.isEditable()) {
/* 1012 */       return this.editor.getBaselineResizeBehavior();
/*      */     }
/* 1014 */     if (this.sameBaseline) {
/* 1015 */       Object localObject1 = this.comboBox.getRenderer();
/* 1016 */       if (localObject1 == null) {
/* 1017 */         localObject1 = new DefaultListCellRenderer();
/*      */       }
/* 1019 */       Object localObject2 = null;
/* 1020 */       Object localObject3 = this.comboBox.getPrototypeDisplayValue();
/* 1021 */       if (localObject3 != null) {
/* 1022 */         localObject2 = localObject3;
/*      */       }
/* 1024 */       else if (this.comboBox.getModel().getSize() > 0)
/*      */       {
/* 1027 */         localObject2 = this.comboBox.getModel().getElementAt(0);
/*      */       }
/* 1029 */       if (localObject2 != null) {
/* 1030 */         Component localComponent = ((ListCellRenderer)localObject1).getListCellRendererComponent(this.listBox, localObject2, -1, false, false);
/*      */ 
/* 1033 */         return localComponent.getBaselineResizeBehavior();
/*      */       }
/*      */     }
/* 1036 */     return Component.BaselineResizeBehavior.OTHER;
/*      */   }
/*      */ 
/*      */   public int getAccessibleChildrenCount(JComponent paramJComponent)
/*      */   {
/* 1042 */     if (this.comboBox.isEditable()) {
/* 1043 */       return 2;
/*      */     }
/*      */ 
/* 1046 */     return 1;
/*      */   }
/*      */ 
/*      */   public Accessible getAccessibleChild(JComponent paramJComponent, int paramInt)
/*      */   {
/*      */     AccessibleContext localAccessibleContext;
/* 1055 */     switch (paramInt) {
/*      */     case 0:
/* 1057 */       if ((this.popup instanceof Accessible)) {
/* 1058 */         localAccessibleContext = ((Accessible)this.popup).getAccessibleContext();
/* 1059 */         localAccessibleContext.setAccessibleParent(this.comboBox);
/* 1060 */         return (Accessible)this.popup;
/*      */       }
/*      */       break;
/*      */     case 1:
/* 1064 */       if ((this.comboBox.isEditable()) && ((this.editor instanceof Accessible)))
/*      */       {
/* 1066 */         localAccessibleContext = ((Accessible)this.editor).getAccessibleContext();
/* 1067 */         localAccessibleContext.setAccessibleParent(this.comboBox);
/* 1068 */         return (Accessible)this.editor;
/*      */       }
/*      */       break;
/*      */     }
/* 1072 */     return null;
/*      */   }
/*      */ 
/*      */   protected boolean isNavigationKey(int paramInt)
/*      */   {
/* 1091 */     return (paramInt == 38) || (paramInt == 40) || (paramInt == 224) || (paramInt == 225);
/*      */   }
/*      */ 
/*      */   private boolean isNavigationKey(int paramInt1, int paramInt2)
/*      */   {
/* 1096 */     InputMap localInputMap = this.comboBox.getInputMap(1);
/* 1097 */     KeyStroke localKeyStroke = KeyStroke.getKeyStroke(paramInt1, paramInt2);
/*      */ 
/* 1099 */     if ((localInputMap != null) && (localInputMap.get(localKeyStroke) != null)) {
/* 1100 */       return true;
/*      */     }
/* 1102 */     return false;
/*      */   }
/*      */ 
/*      */   protected void selectNextPossibleValue()
/*      */   {
/*      */     int i;
/* 1112 */     if (this.comboBox.isPopupVisible()) {
/* 1113 */       i = this.listBox.getSelectedIndex();
/*      */     }
/*      */     else {
/* 1116 */       i = this.comboBox.getSelectedIndex();
/*      */     }
/*      */ 
/* 1119 */     if (i < this.comboBox.getModel().getSize() - 1) {
/* 1120 */       this.listBox.setSelectedIndex(i + 1);
/* 1121 */       this.listBox.ensureIndexIsVisible(i + 1);
/* 1122 */       if ((!this.isTableCellEditor) && (
/* 1123 */         (!UIManager.getBoolean("ComboBox.noActionOnKeyNavigation")) || (!this.comboBox.isPopupVisible()))) {
/* 1124 */         this.comboBox.setSelectedIndex(i + 1);
/*      */       }
/*      */ 
/* 1127 */       this.comboBox.repaint();
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void selectPreviousPossibleValue()
/*      */   {
/*      */     int i;
/* 1138 */     if (this.comboBox.isPopupVisible()) {
/* 1139 */       i = this.listBox.getSelectedIndex();
/*      */     }
/*      */     else {
/* 1142 */       i = this.comboBox.getSelectedIndex();
/*      */     }
/*      */ 
/* 1145 */     if (i > 0) {
/* 1146 */       this.listBox.setSelectedIndex(i - 1);
/* 1147 */       this.listBox.ensureIndexIsVisible(i - 1);
/* 1148 */       if ((!this.isTableCellEditor) && (
/* 1149 */         (!UIManager.getBoolean("ComboBox.noActionOnKeyNavigation")) || (!this.comboBox.isPopupVisible()))) {
/* 1150 */         this.comboBox.setSelectedIndex(i - 1);
/*      */       }
/*      */ 
/* 1153 */       this.comboBox.repaint();
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void toggleOpenClose()
/*      */   {
/* 1161 */     setPopupVisible(this.comboBox, !isPopupVisible(this.comboBox));
/*      */   }
/*      */ 
/*      */   protected Rectangle rectangleForCurrentValue()
/*      */   {
/* 1168 */     int i = this.comboBox.getWidth();
/* 1169 */     int j = this.comboBox.getHeight();
/* 1170 */     Insets localInsets = getInsets();
/* 1171 */     int k = j - (localInsets.top + localInsets.bottom);
/* 1172 */     if (this.arrowButton != null) {
/* 1173 */       k = this.arrowButton.getWidth();
/*      */     }
/* 1175 */     if (BasicGraphicsUtils.isLeftToRight(this.comboBox)) {
/* 1176 */       return new Rectangle(localInsets.left, localInsets.top, i - (localInsets.left + localInsets.right + k), j - (localInsets.top + localInsets.bottom));
/*      */     }
/*      */ 
/* 1181 */     return new Rectangle(localInsets.left + k, localInsets.top, i - (localInsets.left + localInsets.right + k), j - (localInsets.top + localInsets.bottom));
/*      */   }
/*      */ 
/*      */   protected Insets getInsets()
/*      */   {
/* 1191 */     return this.comboBox.getInsets();
/*      */   }
/*      */ 
/*      */   public void paintCurrentValue(Graphics paramGraphics, Rectangle paramRectangle, boolean paramBoolean)
/*      */   {
/* 1207 */     ListCellRenderer localListCellRenderer = this.comboBox.getRenderer();
/*      */     Component localComponent;
/* 1210 */     if ((paramBoolean) && (!isPopupVisible(this.comboBox))) {
/* 1211 */       localComponent = localListCellRenderer.getListCellRendererComponent(this.listBox, this.comboBox.getSelectedItem(), -1, true, false);
/*      */     }
/*      */     else
/*      */     {
/* 1218 */       localComponent = localListCellRenderer.getListCellRendererComponent(this.listBox, this.comboBox.getSelectedItem(), -1, false, false);
/*      */ 
/* 1223 */       localComponent.setBackground(UIManager.getColor("ComboBox.background"));
/*      */     }
/* 1225 */     localComponent.setFont(this.comboBox.getFont());
/* 1226 */     if ((paramBoolean) && (!isPopupVisible(this.comboBox))) {
/* 1227 */       localComponent.setForeground(this.listBox.getSelectionForeground());
/* 1228 */       localComponent.setBackground(this.listBox.getSelectionBackground());
/*      */     }
/* 1231 */     else if (this.comboBox.isEnabled()) {
/* 1232 */       localComponent.setForeground(this.comboBox.getForeground());
/* 1233 */       localComponent.setBackground(this.comboBox.getBackground());
/*      */     }
/*      */     else {
/* 1236 */       localComponent.setForeground(DefaultLookup.getColor(this.comboBox, this, "ComboBox.disabledForeground", null));
/*      */ 
/* 1238 */       localComponent.setBackground(DefaultLookup.getColor(this.comboBox, this, "ComboBox.disabledBackground", null));
/*      */     }
/*      */ 
/* 1244 */     boolean bool = false;
/* 1245 */     if ((localComponent instanceof JPanel)) {
/* 1246 */       bool = true;
/*      */     }
/*      */ 
/* 1249 */     int i = paramRectangle.x; int j = paramRectangle.y; int k = paramRectangle.width; int m = paramRectangle.height;
/* 1250 */     if (this.padding != null) {
/* 1251 */       i = paramRectangle.x + this.padding.left;
/* 1252 */       j = paramRectangle.y + this.padding.top;
/* 1253 */       k = paramRectangle.width - (this.padding.left + this.padding.right);
/* 1254 */       m = paramRectangle.height - (this.padding.top + this.padding.bottom);
/*      */     }
/*      */ 
/* 1257 */     this.currentValuePane.paintComponent(paramGraphics, localComponent, this.comboBox, i, j, k, m, bool);
/*      */   }
/*      */ 
/*      */   public void paintCurrentValueBackground(Graphics paramGraphics, Rectangle paramRectangle, boolean paramBoolean)
/*      */   {
/* 1264 */     Color localColor = paramGraphics.getColor();
/* 1265 */     if (this.comboBox.isEnabled()) {
/* 1266 */       paramGraphics.setColor(DefaultLookup.getColor(this.comboBox, this, "ComboBox.background", null));
/*      */     }
/*      */     else {
/* 1269 */       paramGraphics.setColor(DefaultLookup.getColor(this.comboBox, this, "ComboBox.disabledBackground", null));
/*      */     }
/* 1271 */     paramGraphics.fillRect(paramRectangle.x, paramRectangle.y, paramRectangle.width, paramRectangle.height);
/* 1272 */     paramGraphics.setColor(localColor);
/*      */   }
/*      */ 
/*      */   void repaintCurrentValue()
/*      */   {
/* 1279 */     Rectangle localRectangle = rectangleForCurrentValue();
/* 1280 */     this.comboBox.repaint(localRectangle.x, localRectangle.y, localRectangle.width, localRectangle.height);
/*      */   }
/*      */ 
/*      */   protected Dimension getDefaultSize()
/*      */   {
/* 1301 */     Dimension localDimension = getSizeForComponent(getDefaultListCellRenderer().getListCellRendererComponent(this.listBox, " ", -1, false, false));
/*      */ 
/* 1303 */     return new Dimension(localDimension.width, localDimension.height);
/*      */   }
/*      */ 
/*      */   protected Dimension getDisplaySize()
/*      */   {
/* 1319 */     if (!this.isDisplaySizeDirty) {
/* 1320 */       return new Dimension(this.cachedDisplaySize);
/*      */     }
/* 1322 */     Dimension localDimension1 = new Dimension();
/*      */ 
/* 1324 */     Object localObject1 = this.comboBox.getRenderer();
/* 1325 */     if (localObject1 == null) {
/* 1326 */       localObject1 = new DefaultListCellRenderer();
/*      */     }
/*      */ 
/* 1329 */     this.sameBaseline = true;
/*      */ 
/* 1331 */     Object localObject2 = this.comboBox.getPrototypeDisplayValue();
/*      */     Object localObject3;
/* 1332 */     if (localObject2 != null)
/*      */     {
/* 1334 */       localDimension1 = getSizeForComponent(((ListCellRenderer)localObject1).getListCellRendererComponent(this.listBox, localObject2, -1, false, false));
/*      */     }
/*      */     else
/*      */     {
/* 1340 */       localObject3 = this.comboBox.getModel();
/* 1341 */       int i = ((ComboBoxModel)localObject3).getSize();
/* 1342 */       int j = -1;
/*      */ 
/* 1347 */       if (i > 0) {
/* 1348 */         for (int k = 0; k < i; k++)
/*      */         {
/* 1351 */           Object localObject4 = ((ComboBoxModel)localObject3).getElementAt(k);
/* 1352 */           Component localComponent = ((ListCellRenderer)localObject1).getListCellRendererComponent(this.listBox, localObject4, -1, false, false);
/*      */ 
/* 1354 */           Dimension localDimension2 = getSizeForComponent(localComponent);
/* 1355 */           if ((this.sameBaseline) && (localObject4 != null) && ((!(localObject4 instanceof String)) || (!"".equals(localObject4))))
/*      */           {
/* 1357 */             int m = localComponent.getBaseline(localDimension2.width, localDimension2.height);
/* 1358 */             if (m == -1) {
/* 1359 */               this.sameBaseline = false;
/*      */             }
/* 1361 */             else if (j == -1) {
/* 1362 */               j = m;
/*      */             }
/* 1364 */             else if (j != m) {
/* 1365 */               this.sameBaseline = false;
/*      */             }
/*      */           }
/* 1368 */           localDimension1.width = Math.max(localDimension1.width, localDimension2.width);
/* 1369 */           localDimension1.height = Math.max(localDimension1.height, localDimension2.height);
/*      */         }
/*      */       } else {
/* 1372 */         localDimension1 = getDefaultSize();
/* 1373 */         if (this.comboBox.isEditable()) {
/* 1374 */           localDimension1.width = 100;
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 1379 */     if (this.comboBox.isEditable()) {
/* 1380 */       localObject3 = this.editor.getPreferredSize();
/* 1381 */       localDimension1.width = Math.max(localDimension1.width, ((Dimension)localObject3).width);
/* 1382 */       localDimension1.height = Math.max(localDimension1.height, ((Dimension)localObject3).height);
/*      */     }
/*      */ 
/* 1386 */     if (this.padding != null) {
/* 1387 */       localDimension1.width += this.padding.left + this.padding.right;
/* 1388 */       localDimension1.height += this.padding.top + this.padding.bottom;
/*      */     }
/*      */ 
/* 1392 */     this.cachedDisplaySize.setSize(localDimension1.width, localDimension1.height);
/* 1393 */     this.isDisplaySizeDirty = false;
/*      */ 
/* 1395 */     return localDimension1;
/*      */   }
/*      */ 
/*      */   protected Dimension getSizeForComponent(Component paramComponent)
/*      */   {
/* 1410 */     this.currentValuePane.add(paramComponent);
/* 1411 */     paramComponent.setFont(this.comboBox.getFont());
/* 1412 */     Dimension localDimension = paramComponent.getPreferredSize();
/* 1413 */     this.currentValuePane.remove(paramComponent);
/* 1414 */     return localDimension;
/*      */   }
/*      */ 
/*      */   protected void installKeyboardActions()
/*      */   {
/* 1432 */     InputMap localInputMap = getInputMap(1);
/* 1433 */     SwingUtilities.replaceUIInputMap(this.comboBox, 1, localInputMap);
/*      */ 
/* 1437 */     LazyActionMap.installLazyActionMap(this.comboBox, BasicComboBoxUI.class, "ComboBox.actionMap");
/*      */   }
/*      */ 
/*      */   InputMap getInputMap(int paramInt)
/*      */   {
/* 1442 */     if (paramInt == 1) {
/* 1443 */       return (InputMap)DefaultLookup.get(this.comboBox, this, "ComboBox.ancestorInputMap");
/*      */     }
/*      */ 
/* 1446 */     return null;
/*      */   }
/*      */ 
/*      */   boolean isTableCellEditor() {
/* 1450 */     return this.isTableCellEditor;
/*      */   }
/*      */ 
/*      */   protected void uninstallKeyboardActions()
/*      */   {
/* 1457 */     SwingUtilities.replaceUIInputMap(this.comboBox, 1, null);
/*      */ 
/* 1459 */     SwingUtilities.replaceUIActionMap(this.comboBox, null); } 
/*      */   private static class Actions extends UIAction { private static final String HIDE = "hidePopup";
/*      */     private static final String DOWN = "selectNext";
/*      */     private static final String DOWN_2 = "selectNext2";
/*      */     private static final String TOGGLE = "togglePopup";
/*      */     private static final String TOGGLE_2 = "spacePopup";
/*      */     private static final String UP = "selectPrevious";
/*      */     private static final String UP_2 = "selectPrevious2";
/*      */     private static final String ENTER = "enterPressed";
/*      */     private static final String PAGE_DOWN = "pageDownPassThrough";
/*      */     private static final String PAGE_UP = "pageUpPassThrough";
/*      */     private static final String HOME = "homePassThrough";
/*      */     private static final String END = "endPassThrough";
/*      */ 
/* 1481 */     Actions(String paramString) { super(); }
/*      */ 
/*      */     public void actionPerformed(ActionEvent paramActionEvent)
/*      */     {
/* 1485 */       String str = getName();
/* 1486 */       JComboBox localJComboBox = (JComboBox)paramActionEvent.getSource();
/* 1487 */       BasicComboBoxUI localBasicComboBoxUI = (BasicComboBoxUI)BasicLookAndFeel.getUIOfType(localJComboBox.getUI(), BasicComboBoxUI.class);
/*      */ 
/* 1489 */       if (str == "hidePopup") {
/* 1490 */         localJComboBox.firePopupMenuCanceled();
/* 1491 */         localJComboBox.setPopupVisible(false);
/*      */       }
/* 1493 */       else if ((str == "pageDownPassThrough") || (str == "pageUpPassThrough") || (str == "homePassThrough") || (str == "endPassThrough"))
/*      */       {
/* 1495 */         int i = getNextIndex(localJComboBox, str);
/* 1496 */         if ((i >= 0) && (i < localJComboBox.getItemCount())) {
/* 1497 */           if ((UIManager.getBoolean("ComboBox.noActionOnKeyNavigation")) && (localJComboBox.isPopupVisible())) {
/* 1498 */             localBasicComboBoxUI.listBox.setSelectedIndex(i);
/* 1499 */             localBasicComboBoxUI.listBox.ensureIndexIsVisible(i);
/* 1500 */             localJComboBox.repaint();
/*      */           } else {
/* 1502 */             localJComboBox.setSelectedIndex(i);
/*      */           }
/*      */         }
/*      */       }
/* 1506 */       else if (str == "selectNext") {
/* 1507 */         if (localJComboBox.isShowing()) {
/* 1508 */           if (localJComboBox.isPopupVisible()) {
/* 1509 */             if (localBasicComboBoxUI != null)
/* 1510 */               localBasicComboBoxUI.selectNextPossibleValue();
/*      */           }
/*      */           else {
/* 1513 */             localJComboBox.setPopupVisible(true);
/*      */           }
/*      */         }
/*      */       }
/* 1517 */       else if (str == "selectNext2")
/*      */       {
/* 1521 */         if (localJComboBox.isShowing()) {
/* 1522 */           if (((localJComboBox.isEditable()) || ((localBasicComboBoxUI != null) && (localBasicComboBoxUI.isTableCellEditor()))) && (!localJComboBox.isPopupVisible()))
/*      */           {
/* 1525 */             localJComboBox.setPopupVisible(true);
/*      */           }
/* 1527 */           else if (localBasicComboBoxUI != null) {
/* 1528 */             localBasicComboBoxUI.selectNextPossibleValue();
/*      */           }
/*      */         }
/*      */ 
/*      */       }
/* 1533 */       else if ((str == "togglePopup") || (str == "spacePopup")) {
/* 1534 */         if ((localBasicComboBoxUI != null) && ((str == "togglePopup") || (!localJComboBox.isEditable()))) {
/* 1535 */           if (localBasicComboBoxUI.isTableCellEditor())
/*      */           {
/* 1538 */             localJComboBox.setSelectedIndex(localBasicComboBoxUI.popup.getList().getSelectedIndex());
/*      */           }
/*      */           else
/*      */           {
/* 1542 */             localJComboBox.setPopupVisible(!localJComboBox.isPopupVisible());
/*      */           }
/*      */         }
/*      */       }
/* 1546 */       else if (str == "selectPrevious") {
/* 1547 */         if (localBasicComboBoxUI != null) {
/* 1548 */           if (localBasicComboBoxUI.isPopupVisible(localJComboBox)) {
/* 1549 */             localBasicComboBoxUI.selectPreviousPossibleValue();
/*      */           }
/* 1551 */           else if (DefaultLookup.getBoolean(localJComboBox, localBasicComboBoxUI, "ComboBox.showPopupOnNavigation", false))
/*      */           {
/* 1553 */             localBasicComboBoxUI.setPopupVisible(localJComboBox, true);
/*      */           }
/*      */         }
/*      */       }
/* 1557 */       else if (str == "selectPrevious2")
/*      */       {
/* 1560 */         if ((localJComboBox.isShowing()) && (localBasicComboBoxUI != null)) {
/* 1561 */           if ((localJComboBox.isEditable()) && (!localJComboBox.isPopupVisible()))
/* 1562 */             localJComboBox.setPopupVisible(true);
/*      */           else {
/* 1564 */             localBasicComboBoxUI.selectPreviousPossibleValue();
/*      */           }
/*      */         }
/*      */ 
/*      */       }
/* 1569 */       else if (str == "enterPressed")
/*      */       {
/*      */         Object localObject2;
/* 1570 */         if (localJComboBox.isPopupVisible())
/*      */         {
/* 1573 */           if (UIManager.getBoolean("ComboBox.noActionOnKeyNavigation")) {
/* 1574 */             Object localObject1 = localBasicComboBoxUI.popup.getList().getSelectedValue();
/* 1575 */             if (localObject1 != null) {
/* 1576 */               localJComboBox.getEditor().setItem(localObject1);
/* 1577 */               localJComboBox.setSelectedItem(localObject1);
/*      */             }
/* 1579 */             localJComboBox.setPopupVisible(false);
/*      */           }
/*      */           else {
/* 1582 */             boolean bool = UIManager.getBoolean("ComboBox.isEnterSelectablePopup");
/*      */ 
/* 1584 */             if ((!localJComboBox.isEditable()) || (bool) || (localBasicComboBoxUI.isTableCellEditor))
/*      */             {
/* 1586 */               localObject2 = localBasicComboBoxUI.popup.getList().getSelectedValue();
/* 1587 */               if (localObject2 != null)
/*      */               {
/* 1592 */                 localJComboBox.getEditor().setItem(localObject2);
/* 1593 */                 localJComboBox.setSelectedItem(localObject2);
/*      */               }
/*      */             }
/* 1596 */             localJComboBox.setPopupVisible(false);
/*      */           }
/*      */         }
/*      */         else
/*      */         {
/* 1601 */           if ((localBasicComboBoxUI.isTableCellEditor) && (!localJComboBox.isEditable())) {
/* 1602 */             localJComboBox.setSelectedItem(localJComboBox.getSelectedItem());
/*      */           }
/*      */ 
/* 1607 */           JRootPane localJRootPane = SwingUtilities.getRootPane(localJComboBox);
/* 1608 */           if (localJRootPane != null) {
/* 1609 */             localObject2 = localJRootPane.getInputMap(2);
/* 1610 */             ActionMap localActionMap = localJRootPane.getActionMap();
/* 1611 */             if ((localObject2 != null) && (localActionMap != null)) {
/* 1612 */               Object localObject3 = ((InputMap)localObject2).get(KeyStroke.getKeyStroke(10, 0));
/* 1613 */               if (localObject3 != null) {
/* 1614 */                 Action localAction = localActionMap.get(localObject3);
/* 1615 */                 if (localAction != null)
/* 1616 */                   localAction.actionPerformed(new ActionEvent(localJRootPane, paramActionEvent.getID(), paramActionEvent.getActionCommand(), paramActionEvent.getWhen(), paramActionEvent.getModifiers()));
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     private int getNextIndex(JComboBox paramJComboBox, String paramString)
/*      */     {
/* 1628 */       int i = paramJComboBox.getMaximumRowCount();
/*      */ 
/* 1630 */       int j = paramJComboBox.getSelectedIndex();
/* 1631 */       if ((UIManager.getBoolean("ComboBox.noActionOnKeyNavigation")) && ((paramJComboBox.getUI() instanceof BasicComboBoxUI)))
/*      */       {
/* 1633 */         j = ((BasicComboBoxUI)paramJComboBox.getUI()).listBox.getSelectedIndex();
/*      */       }
/*      */       int k;
/* 1636 */       if (paramString == "pageUpPassThrough") {
/* 1637 */         k = j - i;
/* 1638 */         return k < 0 ? 0 : k;
/*      */       }
/* 1640 */       if (paramString == "pageDownPassThrough") {
/* 1641 */         k = j + i;
/* 1642 */         int m = paramJComboBox.getItemCount();
/* 1643 */         return k < m ? k : m - 1;
/*      */       }
/* 1645 */       if (paramString == "homePassThrough") {
/* 1646 */         return 0;
/*      */       }
/* 1648 */       if (paramString == "endPassThrough") {
/* 1649 */         return paramJComboBox.getItemCount() - 1;
/*      */       }
/* 1651 */       return paramJComboBox.getSelectedIndex();
/*      */     }
/*      */ 
/*      */     public boolean isEnabled(Object paramObject) {
/* 1655 */       if (getName() == "hidePopup") {
/* 1656 */         return (paramObject != null) && (((JComboBox)paramObject).isPopupVisible());
/*      */       }
/* 1658 */       return true;
/*      */     }
/*      */   }
/*      */ 
/*      */   public class ComboBoxLayoutManager
/*      */     implements LayoutManager
/*      */   {
/*      */     public ComboBoxLayoutManager()
/*      */     {
/*      */     }
/*      */ 
/*      */     public void addLayoutComponent(String paramString, Component paramComponent)
/*      */     {
/*      */     }
/*      */ 
/*      */     public void removeLayoutComponent(Component paramComponent)
/*      */     {
/*      */     }
/*      */ 
/*      */     public Dimension preferredLayoutSize(Container paramContainer)
/*      */     {
/*  667 */       return BasicComboBoxUI.this.getHandler().preferredLayoutSize(paramContainer);
/*      */     }
/*      */ 
/*      */     public Dimension minimumLayoutSize(Container paramContainer) {
/*  671 */       return BasicComboBoxUI.this.getHandler().minimumLayoutSize(paramContainer);
/*      */     }
/*      */ 
/*      */     public void layoutContainer(Container paramContainer) {
/*  675 */       BasicComboBoxUI.this.getHandler().layoutContainer(paramContainer);
/*      */     }
/*      */   }
/*      */ 
/*      */   class DefaultKeySelectionManager
/*      */     implements JComboBox.KeySelectionManager, UIResource
/*      */   {
/* 1958 */     private String prefix = "";
/* 1959 */     private String typedString = "";
/*      */ 
/*      */     DefaultKeySelectionManager() {  } 
/* 1962 */     public int selectionForKey(char paramChar, ComboBoxModel paramComboBoxModel) { if (BasicComboBoxUI.this.lastTime == 0L) {
/* 1963 */         this.prefix = "";
/* 1964 */         this.typedString = "";
/*      */       }
/* 1966 */       int i = 1;
/*      */ 
/* 1968 */       int j = BasicComboBoxUI.this.comboBox.getSelectedIndex();
/* 1969 */       if (BasicComboBoxUI.this.time - BasicComboBoxUI.this.lastTime < BasicComboBoxUI.this.timeFactor) {
/* 1970 */         this.typedString += paramChar;
/* 1971 */         if ((this.prefix.length() == 1) && (paramChar == this.prefix.charAt(0)))
/*      */         {
/* 1974 */           j++;
/*      */         }
/* 1976 */         else this.prefix = this.typedString; 
/*      */       }
/*      */       else
/*      */       {
/* 1979 */         j++;
/* 1980 */         this.typedString = ("" + paramChar);
/* 1981 */         this.prefix = this.typedString;
/*      */       }
/* 1983 */       BasicComboBoxUI.this.lastTime = BasicComboBoxUI.this.time;
/*      */ 
/* 1985 */       if ((j < 0) || (j >= paramComboBoxModel.getSize())) {
/* 1986 */         i = 0;
/* 1987 */         j = 0;
/*      */       }
/* 1989 */       int k = BasicComboBoxUI.this.listBox.getNextMatch(this.prefix, j, Position.Bias.Forward);
/*      */ 
/* 1991 */       if ((k < 0) && (i != 0)) {
/* 1992 */         k = BasicComboBoxUI.this.listBox.getNextMatch(this.prefix, 0, Position.Bias.Forward);
/*      */       }
/*      */ 
/* 1995 */       return k;
/*      */     }
/*      */   }
/*      */ 
/*      */   public class FocusHandler
/*      */     implements FocusListener
/*      */   {
/*      */     public FocusHandler()
/*      */     {
/*      */     }
/*      */ 
/*      */     public void focusGained(FocusEvent paramFocusEvent)
/*      */     {
/*  573 */       BasicComboBoxUI.this.getHandler().focusGained(paramFocusEvent);
/*      */     }
/*      */ 
/*      */     public void focusLost(FocusEvent paramFocusEvent) {
/*  577 */       BasicComboBoxUI.this.getHandler().focusLost(paramFocusEvent);
/*      */     }
/*      */   }
/*      */ 
/*      */   private class Handler
/*      */     implements ActionListener, FocusListener, KeyListener, LayoutManager, ListDataListener, PropertyChangeListener
/*      */   {
/*      */     private Handler()
/*      */     {
/*      */     }
/*      */ 
/*      */     public void propertyChange(PropertyChangeEvent paramPropertyChangeEvent)
/*      */     {
/* 1676 */       String str = paramPropertyChangeEvent.getPropertyName();
/* 1677 */       if (paramPropertyChangeEvent.getSource() == BasicComboBoxUI.this.editor)
/*      */       {
/* 1681 */         if ("border".equals(str)) {
/* 1682 */           BasicComboBoxUI.this.isMinimumSizeDirty = true;
/* 1683 */           BasicComboBoxUI.this.isDisplaySizeDirty = true;
/* 1684 */           BasicComboBoxUI.this.comboBox.revalidate();
/*      */         }
/*      */       } else {
/* 1687 */         JComboBox localJComboBox = (JComboBox)paramPropertyChangeEvent.getSource();
/* 1688 */         if (str == "model") {
/* 1689 */           ComboBoxModel localComboBoxModel1 = (ComboBoxModel)paramPropertyChangeEvent.getNewValue();
/* 1690 */           ComboBoxModel localComboBoxModel2 = (ComboBoxModel)paramPropertyChangeEvent.getOldValue();
/*      */ 
/* 1692 */           if ((localComboBoxModel2 != null) && (BasicComboBoxUI.this.listDataListener != null)) {
/* 1693 */             localComboBoxModel2.removeListDataListener(BasicComboBoxUI.this.listDataListener);
/*      */           }
/*      */ 
/* 1696 */           if ((localComboBoxModel1 != null) && (BasicComboBoxUI.this.listDataListener != null)) {
/* 1697 */             localComboBoxModel1.addListDataListener(BasicComboBoxUI.this.listDataListener);
/*      */           }
/*      */ 
/* 1700 */           if (BasicComboBoxUI.this.editor != null) {
/* 1701 */             localJComboBox.configureEditor(localJComboBox.getEditor(), localJComboBox.getSelectedItem());
/*      */           }
/* 1703 */           BasicComboBoxUI.this.isMinimumSizeDirty = true;
/* 1704 */           BasicComboBoxUI.this.isDisplaySizeDirty = true;
/* 1705 */           localJComboBox.revalidate();
/* 1706 */           localJComboBox.repaint();
/*      */         }
/* 1708 */         else if ((str == "editor") && (localJComboBox.isEditable())) {
/* 1709 */           BasicComboBoxUI.this.addEditor();
/* 1710 */           localJComboBox.revalidate();
/*      */         }
/* 1712 */         else if (str == "editable") {
/* 1713 */           if (localJComboBox.isEditable()) {
/* 1714 */             localJComboBox.setRequestFocusEnabled(false);
/* 1715 */             BasicComboBoxUI.this.addEditor();
/*      */           } else {
/* 1717 */             localJComboBox.setRequestFocusEnabled(true);
/* 1718 */             BasicComboBoxUI.this.removeEditor();
/*      */           }
/* 1720 */           BasicComboBoxUI.this.updateToolTipTextForChildren();
/* 1721 */           localJComboBox.revalidate();
/*      */         }
/*      */         else
/*      */         {
/*      */           boolean bool;
/* 1723 */           if (str == "enabled") {
/* 1724 */             bool = localJComboBox.isEnabled();
/* 1725 */             if (BasicComboBoxUI.this.editor != null)
/* 1726 */               BasicComboBoxUI.this.editor.setEnabled(bool);
/* 1727 */             if (BasicComboBoxUI.this.arrowButton != null)
/* 1728 */               BasicComboBoxUI.this.arrowButton.setEnabled(bool);
/* 1729 */             localJComboBox.repaint();
/*      */           }
/* 1731 */           else if (str == "focusable") {
/* 1732 */             bool = localJComboBox.isFocusable();
/* 1733 */             if (BasicComboBoxUI.this.editor != null)
/* 1734 */               BasicComboBoxUI.this.editor.setFocusable(bool);
/* 1735 */             if (BasicComboBoxUI.this.arrowButton != null)
/* 1736 */               BasicComboBoxUI.this.arrowButton.setFocusable(bool);
/* 1737 */             localJComboBox.repaint();
/*      */           }
/* 1739 */           else if (str == "maximumRowCount") {
/* 1740 */             if (BasicComboBoxUI.this.isPopupVisible(localJComboBox)) {
/* 1741 */               BasicComboBoxUI.this.setPopupVisible(localJComboBox, false);
/* 1742 */               BasicComboBoxUI.this.setPopupVisible(localJComboBox, true);
/*      */             }
/*      */           }
/* 1745 */           else if (str == "font") {
/* 1746 */             BasicComboBoxUI.this.listBox.setFont(localJComboBox.getFont());
/* 1747 */             if (BasicComboBoxUI.this.editor != null) {
/* 1748 */               BasicComboBoxUI.this.editor.setFont(localJComboBox.getFont());
/*      */             }
/* 1750 */             BasicComboBoxUI.this.isMinimumSizeDirty = true;
/* 1751 */             BasicComboBoxUI.this.isDisplaySizeDirty = true;
/* 1752 */             localJComboBox.validate();
/*      */           }
/* 1754 */           else if (str == "ToolTipText") {
/* 1755 */             BasicComboBoxUI.this.updateToolTipTextForChildren();
/*      */           }
/* 1757 */           else if (str == "JComboBox.isTableCellEditor") {
/* 1758 */             Boolean localBoolean = (Boolean)paramPropertyChangeEvent.getNewValue();
/* 1759 */             BasicComboBoxUI.this.isTableCellEditor = (localBoolean.equals(Boolean.TRUE));
/*      */           }
/* 1761 */           else if (str == "prototypeDisplayValue") {
/* 1762 */             BasicComboBoxUI.this.isMinimumSizeDirty = true;
/* 1763 */             BasicComboBoxUI.this.isDisplaySizeDirty = true;
/* 1764 */             localJComboBox.revalidate();
/*      */           }
/* 1766 */           else if (str == "renderer") {
/* 1767 */             BasicComboBoxUI.this.isMinimumSizeDirty = true;
/* 1768 */             BasicComboBoxUI.this.isDisplaySizeDirty = true;
/* 1769 */             localJComboBox.revalidate();
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     public void keyPressed(KeyEvent paramKeyEvent)
/*      */     {
/* 1784 */       if (BasicComboBoxUI.this.isNavigationKey(paramKeyEvent.getKeyCode(), paramKeyEvent.getModifiers())) {
/* 1785 */         BasicComboBoxUI.this.lastTime = 0L;
/* 1786 */       } else if ((BasicComboBoxUI.this.comboBox.isEnabled()) && (BasicComboBoxUI.this.comboBox.getModel().getSize() != 0) && (isTypeAheadKey(paramKeyEvent)) && (paramKeyEvent.getKeyChar() != 65535))
/*      */       {
/* 1788 */         BasicComboBoxUI.this.time = paramKeyEvent.getWhen();
/* 1789 */         if (BasicComboBoxUI.this.comboBox.selectWithKeyChar(paramKeyEvent.getKeyChar()))
/* 1790 */           paramKeyEvent.consume();
/*      */       }
/*      */     }
/*      */ 
/*      */     public void keyTyped(KeyEvent paramKeyEvent)
/*      */     {
/*      */     }
/*      */ 
/*      */     public void keyReleased(KeyEvent paramKeyEvent) {
/*      */     }
/*      */ 
/*      */     private boolean isTypeAheadKey(KeyEvent paramKeyEvent) {
/* 1802 */       return (!paramKeyEvent.isAltDown()) && (!BasicGraphicsUtils.isMenuShortcutKeyDown(paramKeyEvent));
/*      */     }
/*      */ 
/*      */     public void focusGained(FocusEvent paramFocusEvent)
/*      */     {
/* 1813 */       ComboBoxEditor localComboBoxEditor = BasicComboBoxUI.this.comboBox.getEditor();
/*      */ 
/* 1815 */       if ((localComboBoxEditor != null) && (paramFocusEvent.getSource() == localComboBoxEditor.getEditorComponent()))
/*      */       {
/* 1817 */         return;
/*      */       }
/* 1819 */       BasicComboBoxUI.this.hasFocus = true;
/* 1820 */       BasicComboBoxUI.this.comboBox.repaint();
/*      */ 
/* 1822 */       if ((BasicComboBoxUI.this.comboBox.isEditable()) && (BasicComboBoxUI.this.editor != null))
/* 1823 */         BasicComboBoxUI.this.editor.requestFocus();
/*      */     }
/*      */ 
/*      */     public void focusLost(FocusEvent paramFocusEvent)
/*      */     {
/* 1828 */       ComboBoxEditor localComboBoxEditor = BasicComboBoxUI.this.comboBox.getEditor();
/* 1829 */       if ((localComboBoxEditor != null) && (paramFocusEvent.getSource() == localComboBoxEditor.getEditorComponent()))
/*      */       {
/* 1831 */         Object localObject1 = localComboBoxEditor.getItem();
/*      */ 
/* 1833 */         Object localObject2 = BasicComboBoxUI.this.comboBox.getSelectedItem();
/* 1834 */         if ((!paramFocusEvent.isTemporary()) && (localObject1 != null)) if (!localObject1.equals(localObject2 == null ? "" : localObject2))
/*      */           {
/* 1836 */             BasicComboBoxUI.this.comboBox.actionPerformed(new ActionEvent(localComboBoxEditor, 0, "", EventQueue.getMostRecentEventTime(), 0));
/*      */           }
/*      */ 
/*      */ 
/*      */       }
/*      */ 
/* 1842 */       BasicComboBoxUI.this.hasFocus = false;
/* 1843 */       if (!paramFocusEvent.isTemporary()) {
/* 1844 */         BasicComboBoxUI.this.setPopupVisible(BasicComboBoxUI.this.comboBox, false);
/*      */       }
/* 1846 */       BasicComboBoxUI.this.comboBox.repaint();
/*      */     }
/*      */ 
/*      */     public void contentsChanged(ListDataEvent paramListDataEvent)
/*      */     {
/* 1855 */       if ((paramListDataEvent.getIndex0() != -1) || (paramListDataEvent.getIndex1() != -1)) {
/* 1856 */         BasicComboBoxUI.this.isMinimumSizeDirty = true;
/* 1857 */         BasicComboBoxUI.this.comboBox.revalidate();
/*      */       }
/*      */ 
/* 1862 */       if ((BasicComboBoxUI.this.comboBox.isEditable()) && (BasicComboBoxUI.this.editor != null)) {
/* 1863 */         BasicComboBoxUI.this.comboBox.configureEditor(BasicComboBoxUI.this.comboBox.getEditor(), BasicComboBoxUI.this.comboBox.getSelectedItem());
/*      */       }
/*      */ 
/* 1867 */       BasicComboBoxUI.this.isDisplaySizeDirty = true;
/* 1868 */       BasicComboBoxUI.this.comboBox.repaint();
/*      */     }
/*      */ 
/*      */     public void intervalAdded(ListDataEvent paramListDataEvent) {
/* 1872 */       contentsChanged(paramListDataEvent);
/*      */     }
/*      */ 
/*      */     public void intervalRemoved(ListDataEvent paramListDataEvent) {
/* 1876 */       contentsChanged(paramListDataEvent);
/*      */     }
/*      */ 
/*      */     public void addLayoutComponent(String paramString, Component paramComponent)
/*      */     {
/*      */     }
/*      */ 
/*      */     public void removeLayoutComponent(Component paramComponent)
/*      */     {
/*      */     }
/*      */ 
/*      */     public Dimension preferredLayoutSize(Container paramContainer)
/*      */     {
/* 1891 */       return paramContainer.getPreferredSize();
/*      */     }
/*      */ 
/*      */     public Dimension minimumLayoutSize(Container paramContainer) {
/* 1895 */       return paramContainer.getMinimumSize();
/*      */     }
/*      */ 
/*      */     public void layoutContainer(Container paramContainer) {
/* 1899 */       JComboBox localJComboBox = (JComboBox)paramContainer;
/* 1900 */       int i = localJComboBox.getWidth();
/* 1901 */       int j = localJComboBox.getHeight();
/*      */ 
/* 1903 */       Insets localInsets = BasicComboBoxUI.this.getInsets();
/* 1904 */       int k = j - (localInsets.top + localInsets.bottom);
/* 1905 */       int m = k;
/*      */       Object localObject;
/* 1906 */       if (BasicComboBoxUI.this.arrowButton != null) {
/* 1907 */         localObject = BasicComboBoxUI.this.arrowButton.getInsets();
/* 1908 */         m = BasicComboBoxUI.this.squareButton ? k : BasicComboBoxUI.this.arrowButton.getPreferredSize().width + ((Insets)localObject).left + ((Insets)localObject).right;
/*      */       }
/*      */ 
/* 1914 */       if (BasicComboBoxUI.this.arrowButton != null) {
/* 1915 */         if (BasicGraphicsUtils.isLeftToRight(localJComboBox)) {
/* 1916 */           BasicComboBoxUI.this.arrowButton.setBounds(i - (localInsets.right + m), localInsets.top, m, k);
/*      */         }
/*      */         else {
/* 1919 */           BasicComboBoxUI.this.arrowButton.setBounds(localInsets.left, localInsets.top, m, k);
/*      */         }
/*      */       }
/*      */ 
/* 1923 */       if (BasicComboBoxUI.this.editor != null) {
/* 1924 */         localObject = BasicComboBoxUI.this.rectangleForCurrentValue();
/* 1925 */         BasicComboBoxUI.this.editor.setBounds((Rectangle)localObject);
/*      */       }
/*      */     }
/*      */ 
/*      */     public void actionPerformed(ActionEvent paramActionEvent)
/*      */     {
/* 1939 */       Object localObject = BasicComboBoxUI.this.comboBox.getEditor().getItem();
/* 1940 */       if (localObject != null) {
/* 1941 */         if ((!BasicComboBoxUI.this.comboBox.isPopupVisible()) && (!localObject.equals(BasicComboBoxUI.this.comboBox.getSelectedItem()))) {
/* 1942 */           BasicComboBoxUI.this.comboBox.setSelectedItem(BasicComboBoxUI.this.comboBox.getEditor().getItem());
/*      */         }
/* 1944 */         ActionMap localActionMap = BasicComboBoxUI.this.comboBox.getActionMap();
/* 1945 */         if (localActionMap != null) {
/* 1946 */           Action localAction = localActionMap.get("enterPressed");
/* 1947 */           if (localAction != null)
/* 1948 */             localAction.actionPerformed(new ActionEvent(BasicComboBoxUI.this.comboBox, paramActionEvent.getID(), paramActionEvent.getActionCommand(), paramActionEvent.getModifiers()));
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public class ItemHandler
/*      */     implements ItemListener
/*      */   {
/*      */     public ItemHandler()
/*      */     {
/*      */     }
/*      */ 
/*      */     public void itemStateChanged(ItemEvent paramItemEvent)
/*      */     {
/*      */     }
/*      */   }
/*      */ 
/*      */   public class KeyHandler extends KeyAdapter
/*      */   {
/*      */     public KeyHandler()
/*      */     {
/*      */     }
/*      */ 
/*      */     public void keyPressed(KeyEvent paramKeyEvent)
/*      */     {
/*  559 */       BasicComboBoxUI.this.getHandler().keyPressed(paramKeyEvent);
/*      */     }
/*      */   }
/*      */ 
/*      */   public class ListDataHandler
/*      */     implements ListDataListener
/*      */   {
/*      */     public ListDataHandler()
/*      */     {
/*      */     }
/*      */ 
/*      */     public void contentsChanged(ListDataEvent paramListDataEvent)
/*      */     {
/*  593 */       BasicComboBoxUI.this.getHandler().contentsChanged(paramListDataEvent);
/*      */     }
/*      */ 
/*      */     public void intervalAdded(ListDataEvent paramListDataEvent) {
/*  597 */       BasicComboBoxUI.this.getHandler().intervalAdded(paramListDataEvent);
/*      */     }
/*      */ 
/*      */     public void intervalRemoved(ListDataEvent paramListDataEvent) {
/*  601 */       BasicComboBoxUI.this.getHandler().intervalRemoved(paramListDataEvent);
/*      */     }
/*      */   }
/*      */ 
/*      */   public class PropertyChangeHandler
/*      */     implements PropertyChangeListener
/*      */   {
/*      */     public PropertyChangeHandler()
/*      */     {
/*      */     }
/*      */ 
/*      */     public void propertyChange(PropertyChangeEvent paramPropertyChangeEvent)
/*      */     {
/*  636 */       BasicComboBoxUI.this.getHandler().propertyChange(paramPropertyChangeEvent);
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.basic.BasicComboBoxUI
 * JD-Core Version:    0.6.2
 */