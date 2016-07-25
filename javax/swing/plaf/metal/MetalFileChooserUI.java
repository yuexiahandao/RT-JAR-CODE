/*      */ package javax.swing.plaf.metal;
/*      */ 
/*      */ import java.awt.BorderLayout;
/*      */ import java.awt.Component;
/*      */ import java.awt.ComponentOrientation;
/*      */ import java.awt.Container;
/*      */ import java.awt.Dimension;
/*      */ import java.awt.Graphics;
/*      */ import java.awt.Insets;
/*      */ import java.awt.LayoutManager;
/*      */ import java.awt.event.ActionEvent;
/*      */ import java.awt.event.FocusAdapter;
/*      */ import java.awt.event.FocusEvent;
/*      */ import java.awt.event.MouseAdapter;
/*      */ import java.awt.event.MouseListener;
/*      */ import java.beans.PropertyChangeEvent;
/*      */ import java.beans.PropertyChangeListener;
/*      */ import java.io.File;
/*      */ import java.io.FileNotFoundException;
/*      */ import java.io.IOException;
/*      */ import java.util.Arrays;
/*      */ import java.util.Locale;
/*      */ import java.util.Vector;
/*      */ import javax.swing.AbstractAction;
/*      */ import javax.swing.AbstractListModel;
/*      */ import javax.swing.Action;
/*      */ import javax.swing.ActionMap;
/*      */ import javax.swing.Box;
/*      */ import javax.swing.BoxLayout;
/*      */ import javax.swing.ButtonGroup;
/*      */ import javax.swing.ComboBoxModel;
/*      */ import javax.swing.DefaultListCellRenderer;
/*      */ import javax.swing.Icon;
/*      */ import javax.swing.JButton;
/*      */ import javax.swing.JComboBox;
/*      */ import javax.swing.JComponent;
/*      */ import javax.swing.JFileChooser;
/*      */ import javax.swing.JLabel;
/*      */ import javax.swing.JList;
/*      */ import javax.swing.JPanel;
/*      */ import javax.swing.JTextField;
/*      */ import javax.swing.JToggleButton;
/*      */ import javax.swing.SwingUtilities;
/*      */ import javax.swing.UIManager;
/*      */ import javax.swing.border.EmptyBorder;
/*      */ import javax.swing.event.ListSelectionEvent;
/*      */ import javax.swing.event.ListSelectionListener;
/*      */ import javax.swing.filechooser.FileFilter;
/*      */ import javax.swing.filechooser.FileSystemView;
/*      */ import javax.swing.filechooser.FileView;
/*      */ import javax.swing.plaf.ActionMapUIResource;
/*      */ import javax.swing.plaf.ComponentUI;
/*      */ import javax.swing.plaf.basic.BasicDirectoryModel;
/*      */ import javax.swing.plaf.basic.BasicFileChooserUI;
/*      */ import sun.awt.shell.ShellFolder;
/*      */ import sun.swing.FilePane;
/*      */ import sun.swing.FilePane.FileChooserUIAccessor;
/*      */ import sun.swing.SwingUtilities2;
/*      */ 
/*      */ public class MetalFileChooserUI extends BasicFileChooserUI
/*      */ {
/*      */   private JLabel lookInLabel;
/*      */   private JComboBox directoryComboBox;
/*      */   private DirectoryComboBoxModel directoryComboBoxModel;
/*   63 */   private Action directoryComboBoxAction = new DirectoryComboBoxAction();
/*      */   private FilterComboBoxModel filterComboBoxModel;
/*      */   private JTextField fileNameTextField;
/*      */   private FilePane filePane;
/*      */   private JToggleButton listViewButton;
/*      */   private JToggleButton detailsViewButton;
/*      */   private JButton approveButton;
/*      */   private JButton cancelButton;
/*      */   private JPanel buttonPanel;
/*      */   private JPanel bottomPanel;
/*      */   private JComboBox filterComboBox;
/*   81 */   private static final Dimension hstrut5 = new Dimension(5, 1);
/*   82 */   private static final Dimension hstrut11 = new Dimension(11, 1);
/*      */ 
/*   84 */   private static final Dimension vstrut5 = new Dimension(1, 5);
/*      */ 
/*   86 */   private static final Insets shrinkwrap = new Insets(0, 0, 0, 0);
/*      */ 
/*   89 */   private static int PREF_WIDTH = 500;
/*   90 */   private static int PREF_HEIGHT = 326;
/*   91 */   private static Dimension PREF_SIZE = new Dimension(PREF_WIDTH, PREF_HEIGHT);
/*      */ 
/*   93 */   private static int MIN_WIDTH = 500;
/*   94 */   private static int MIN_HEIGHT = 326;
/*   95 */   private static Dimension MIN_SIZE = new Dimension(MIN_WIDTH, MIN_HEIGHT);
/*      */ 
/*   97 */   private static int LIST_PREF_WIDTH = 405;
/*   98 */   private static int LIST_PREF_HEIGHT = 135;
/*   99 */   private static Dimension LIST_PREF_SIZE = new Dimension(LIST_PREF_WIDTH, LIST_PREF_HEIGHT);
/*      */ 
/*  102 */   private int lookInLabelMnemonic = 0;
/*  103 */   private String lookInLabelText = null;
/*  104 */   private String saveInLabelText = null;
/*      */ 
/*  106 */   private int fileNameLabelMnemonic = 0;
/*  107 */   private String fileNameLabelText = null;
/*  108 */   private int folderNameLabelMnemonic = 0;
/*  109 */   private String folderNameLabelText = null;
/*      */ 
/*  111 */   private int filesOfTypeLabelMnemonic = 0;
/*  112 */   private String filesOfTypeLabelText = null;
/*      */ 
/*  114 */   private String upFolderToolTipText = null;
/*  115 */   private String upFolderAccessibleName = null;
/*      */ 
/*  117 */   private String homeFolderToolTipText = null;
/*  118 */   private String homeFolderAccessibleName = null;
/*      */ 
/*  120 */   private String newFolderToolTipText = null;
/*  121 */   private String newFolderAccessibleName = null;
/*      */ 
/*  123 */   private String listViewButtonToolTipText = null;
/*  124 */   private String listViewButtonAccessibleName = null;
/*      */ 
/*  126 */   private String detailsViewButtonToolTipText = null;
/*  127 */   private String detailsViewButtonAccessibleName = null;
/*      */   private AlignedLabel fileNameLabel;
/*      */   static final int space = 10;
/*      */ 
/*      */   private void populateFileNameLabel()
/*      */   {
/*  132 */     if (getFileChooser().getFileSelectionMode() == 1) {
/*  133 */       this.fileNameLabel.setText(this.folderNameLabelText);
/*  134 */       this.fileNameLabel.setDisplayedMnemonic(this.folderNameLabelMnemonic);
/*      */     } else {
/*  136 */       this.fileNameLabel.setText(this.fileNameLabelText);
/*  137 */       this.fileNameLabel.setDisplayedMnemonic(this.fileNameLabelMnemonic);
/*      */     }
/*      */   }
/*      */ 
/*      */   public static ComponentUI createUI(JComponent paramJComponent)
/*      */   {
/*  145 */     return new MetalFileChooserUI((JFileChooser)paramJComponent);
/*      */   }
/*      */ 
/*      */   public MetalFileChooserUI(JFileChooser paramJFileChooser) {
/*  149 */     super(paramJFileChooser);
/*      */   }
/*      */ 
/*      */   public void installUI(JComponent paramJComponent) {
/*  153 */     super.installUI(paramJComponent);
/*      */   }
/*      */ 
/*      */   public void uninstallComponents(JFileChooser paramJFileChooser) {
/*  157 */     paramJFileChooser.removeAll();
/*  158 */     this.bottomPanel = null;
/*  159 */     this.buttonPanel = null;
/*      */   }
/*      */ 
/*      */   public void installComponents(JFileChooser paramJFileChooser)
/*      */   {
/*  210 */     FileSystemView localFileSystemView = paramJFileChooser.getFileSystemView();
/*      */ 
/*  212 */     paramJFileChooser.setBorder(new EmptyBorder(12, 12, 11, 11));
/*  213 */     paramJFileChooser.setLayout(new BorderLayout(0, 11));
/*      */ 
/*  215 */     this.filePane = new FilePane(new MetalFileChooserUIAccessor(null));
/*  216 */     paramJFileChooser.addPropertyChangeListener(this.filePane);
/*      */ 
/*  223 */     JPanel localJPanel1 = new JPanel(new BorderLayout(11, 0));
/*  224 */     JPanel localJPanel2 = new JPanel();
/*  225 */     localJPanel2.setLayout(new BoxLayout(localJPanel2, 2));
/*  226 */     localJPanel1.add(localJPanel2, "After");
/*      */ 
/*  229 */     paramJFileChooser.add(localJPanel1, "North");
/*      */ 
/*  232 */     this.lookInLabel = new JLabel(this.lookInLabelText);
/*  233 */     this.lookInLabel.setDisplayedMnemonic(this.lookInLabelMnemonic);
/*  234 */     localJPanel1.add(this.lookInLabel, "Before");
/*      */ 
/*  237 */     this.directoryComboBox = new JComboBox() {
/*      */       public Dimension getPreferredSize() {
/*  239 */         Dimension localDimension = super.getPreferredSize();
/*      */ 
/*  241 */         localDimension.width = 150;
/*  242 */         return localDimension;
/*      */       }
/*      */     };
/*  245 */     this.directoryComboBox.putClientProperty("AccessibleDescription", this.lookInLabelText);
/*      */ 
/*  247 */     this.directoryComboBox.putClientProperty("JComboBox.isTableCellEditor", Boolean.TRUE);
/*  248 */     this.lookInLabel.setLabelFor(this.directoryComboBox);
/*  249 */     this.directoryComboBoxModel = createDirectoryComboBoxModel(paramJFileChooser);
/*  250 */     this.directoryComboBox.setModel(this.directoryComboBoxModel);
/*  251 */     this.directoryComboBox.addActionListener(this.directoryComboBoxAction);
/*  252 */     this.directoryComboBox.setRenderer(createDirectoryComboBoxRenderer(paramJFileChooser));
/*  253 */     this.directoryComboBox.setAlignmentX(0.0F);
/*  254 */     this.directoryComboBox.setAlignmentY(0.0F);
/*  255 */     this.directoryComboBox.setMaximumRowCount(8);
/*      */ 
/*  257 */     localJPanel1.add(this.directoryComboBox, "Center");
/*      */ 
/*  260 */     JButton localJButton1 = new JButton(getChangeToParentDirectoryAction());
/*  261 */     localJButton1.setText(null);
/*  262 */     localJButton1.setIcon(this.upFolderIcon);
/*  263 */     localJButton1.setToolTipText(this.upFolderToolTipText);
/*  264 */     localJButton1.putClientProperty("AccessibleName", this.upFolderAccessibleName);
/*      */ 
/*  266 */     localJButton1.setAlignmentX(0.0F);
/*  267 */     localJButton1.setAlignmentY(0.5F);
/*  268 */     localJButton1.setMargin(shrinkwrap);
/*      */ 
/*  270 */     localJPanel2.add(localJButton1);
/*  271 */     localJPanel2.add(Box.createRigidArea(hstrut5));
/*      */ 
/*  274 */     File localFile = localFileSystemView.getHomeDirectory();
/*  275 */     String str = this.homeFolderToolTipText;
/*  276 */     if (localFileSystemView.isRoot(localFile)) {
/*  277 */       str = getFileView(paramJFileChooser).getName(localFile);
/*      */     }
/*      */ 
/*  283 */     JButton localJButton2 = new JButton(this.homeFolderIcon);
/*  284 */     localJButton2.setToolTipText(str);
/*  285 */     localJButton2.putClientProperty("AccessibleName", this.homeFolderAccessibleName);
/*      */ 
/*  287 */     localJButton2.setAlignmentX(0.0F);
/*  288 */     localJButton2.setAlignmentY(0.5F);
/*  289 */     localJButton2.setMargin(shrinkwrap);
/*      */ 
/*  291 */     localJButton2.addActionListener(getGoHomeAction());
/*  292 */     localJPanel2.add(localJButton2);
/*  293 */     localJPanel2.add(Box.createRigidArea(hstrut5));
/*      */ 
/*  296 */     if (!UIManager.getBoolean("FileChooser.readOnly")) {
/*  297 */       localJButton2 = new JButton(this.filePane.getNewFolderAction());
/*  298 */       localJButton2.setText(null);
/*  299 */       localJButton2.setIcon(this.newFolderIcon);
/*  300 */       localJButton2.setToolTipText(this.newFolderToolTipText);
/*  301 */       localJButton2.putClientProperty("AccessibleName", this.newFolderAccessibleName);
/*      */ 
/*  303 */       localJButton2.setAlignmentX(0.0F);
/*  304 */       localJButton2.setAlignmentY(0.5F);
/*  305 */       localJButton2.setMargin(shrinkwrap);
/*      */     }
/*  307 */     localJPanel2.add(localJButton2);
/*  308 */     localJPanel2.add(Box.createRigidArea(hstrut5));
/*      */ 
/*  311 */     ButtonGroup localButtonGroup = new ButtonGroup();
/*      */ 
/*  314 */     this.listViewButton = new JToggleButton(this.listViewIcon);
/*  315 */     this.listViewButton.setToolTipText(this.listViewButtonToolTipText);
/*  316 */     this.listViewButton.putClientProperty("AccessibleName", this.listViewButtonAccessibleName);
/*      */ 
/*  318 */     this.listViewButton.setSelected(true);
/*  319 */     this.listViewButton.setAlignmentX(0.0F);
/*  320 */     this.listViewButton.setAlignmentY(0.5F);
/*  321 */     this.listViewButton.setMargin(shrinkwrap);
/*  322 */     this.listViewButton.addActionListener(this.filePane.getViewTypeAction(0));
/*  323 */     localJPanel2.add(this.listViewButton);
/*  324 */     localButtonGroup.add(this.listViewButton);
/*      */ 
/*  327 */     this.detailsViewButton = new JToggleButton(this.detailsViewIcon);
/*  328 */     this.detailsViewButton.setToolTipText(this.detailsViewButtonToolTipText);
/*  329 */     this.detailsViewButton.putClientProperty("AccessibleName", this.detailsViewButtonAccessibleName);
/*      */ 
/*  331 */     this.detailsViewButton.setAlignmentX(0.0F);
/*  332 */     this.detailsViewButton.setAlignmentY(0.5F);
/*  333 */     this.detailsViewButton.setMargin(shrinkwrap);
/*  334 */     this.detailsViewButton.addActionListener(this.filePane.getViewTypeAction(1));
/*  335 */     localJPanel2.add(this.detailsViewButton);
/*  336 */     localButtonGroup.add(this.detailsViewButton);
/*      */ 
/*  338 */     this.filePane.addPropertyChangeListener(new PropertyChangeListener() {
/*      */       public void propertyChange(PropertyChangeEvent paramAnonymousPropertyChangeEvent) {
/*  340 */         if ("viewType".equals(paramAnonymousPropertyChangeEvent.getPropertyName())) {
/*  341 */           int i = MetalFileChooserUI.this.filePane.getViewType();
/*  342 */           switch (i) {
/*      */           case 0:
/*  344 */             MetalFileChooserUI.this.listViewButton.setSelected(true);
/*  345 */             break;
/*      */           case 1:
/*  348 */             MetalFileChooserUI.this.detailsViewButton.setSelected(true);
/*      */           }
/*      */         }
/*      */       }
/*      */     });
/*  358 */     paramJFileChooser.add(getAccessoryPanel(), "After");
/*  359 */     JComponent localJComponent = paramJFileChooser.getAccessory();
/*  360 */     if (localJComponent != null) {
/*  361 */       getAccessoryPanel().add(localJComponent);
/*      */     }
/*  363 */     this.filePane.setPreferredSize(LIST_PREF_SIZE);
/*  364 */     paramJFileChooser.add(this.filePane, "Center");
/*      */ 
/*  369 */     JPanel localJPanel3 = getBottomPanel();
/*  370 */     localJPanel3.setLayout(new BoxLayout(localJPanel3, 1));
/*  371 */     paramJFileChooser.add(localJPanel3, "South");
/*      */ 
/*  374 */     JPanel localJPanel4 = new JPanel();
/*  375 */     localJPanel4.setLayout(new BoxLayout(localJPanel4, 2));
/*  376 */     localJPanel3.add(localJPanel4);
/*  377 */     localJPanel3.add(Box.createRigidArea(vstrut5));
/*      */ 
/*  379 */     this.fileNameLabel = new AlignedLabel();
/*  380 */     populateFileNameLabel();
/*  381 */     localJPanel4.add(this.fileNameLabel);
/*      */ 
/*  383 */     this.fileNameTextField = new JTextField(35) {
/*      */       public Dimension getMaximumSize() {
/*  385 */         return new Dimension(32767, super.getPreferredSize().height);
/*      */       }
/*      */     };
/*  388 */     localJPanel4.add(this.fileNameTextField);
/*  389 */     this.fileNameLabel.setLabelFor(this.fileNameTextField);
/*  390 */     this.fileNameTextField.addFocusListener(new FocusAdapter()
/*      */     {
/*      */       public void focusGained(FocusEvent paramAnonymousFocusEvent) {
/*  393 */         if (!MetalFileChooserUI.this.getFileChooser().isMultiSelectionEnabled())
/*  394 */           MetalFileChooserUI.this.filePane.clearSelection();
/*      */       }
/*      */     });
/*  399 */     if (paramJFileChooser.isMultiSelectionEnabled())
/*  400 */       setFileName(fileNameString(paramJFileChooser.getSelectedFiles()));
/*      */     else {
/*  402 */       setFileName(fileNameString(paramJFileChooser.getSelectedFile()));
/*      */     }
/*      */ 
/*  407 */     JPanel localJPanel5 = new JPanel();
/*  408 */     localJPanel5.setLayout(new BoxLayout(localJPanel5, 2));
/*  409 */     localJPanel3.add(localJPanel5);
/*      */ 
/*  411 */     AlignedLabel localAlignedLabel = new AlignedLabel(this.filesOfTypeLabelText);
/*  412 */     localAlignedLabel.setDisplayedMnemonic(this.filesOfTypeLabelMnemonic);
/*  413 */     localJPanel5.add(localAlignedLabel);
/*      */ 
/*  415 */     this.filterComboBoxModel = createFilterComboBoxModel();
/*  416 */     paramJFileChooser.addPropertyChangeListener(this.filterComboBoxModel);
/*  417 */     this.filterComboBox = new JComboBox(this.filterComboBoxModel);
/*  418 */     this.filterComboBox.putClientProperty("AccessibleDescription", this.filesOfTypeLabelText);
/*      */ 
/*  420 */     localAlignedLabel.setLabelFor(this.filterComboBox);
/*  421 */     this.filterComboBox.setRenderer(createFilterComboBoxRenderer());
/*  422 */     localJPanel5.add(this.filterComboBox);
/*      */ 
/*  425 */     getButtonPanel().setLayout(new ButtonAreaLayout(null));
/*      */ 
/*  427 */     this.approveButton = new JButton(getApproveButtonText(paramJFileChooser));
/*      */ 
/*  429 */     this.approveButton.addActionListener(getApproveSelectionAction());
/*  430 */     this.approveButton.setToolTipText(getApproveButtonToolTipText(paramJFileChooser));
/*  431 */     getButtonPanel().add(this.approveButton);
/*      */ 
/*  433 */     this.cancelButton = new JButton(this.cancelButtonText);
/*  434 */     this.cancelButton.setToolTipText(this.cancelButtonToolTipText);
/*  435 */     this.cancelButton.addActionListener(getCancelSelectionAction());
/*  436 */     getButtonPanel().add(this.cancelButton);
/*      */ 
/*  438 */     if (paramJFileChooser.getControlButtonsAreShown()) {
/*  439 */       addControlButtons();
/*      */     }
/*      */ 
/*  442 */     groupLabels(new AlignedLabel[] { this.fileNameLabel, localAlignedLabel });
/*      */   }
/*      */ 
/*      */   protected JPanel getButtonPanel() {
/*  446 */     if (this.buttonPanel == null) {
/*  447 */       this.buttonPanel = new JPanel();
/*      */     }
/*  449 */     return this.buttonPanel;
/*      */   }
/*      */ 
/*      */   protected JPanel getBottomPanel() {
/*  453 */     if (this.bottomPanel == null) {
/*  454 */       this.bottomPanel = new JPanel();
/*      */     }
/*  456 */     return this.bottomPanel;
/*      */   }
/*      */ 
/*      */   protected void installStrings(JFileChooser paramJFileChooser) {
/*  460 */     super.installStrings(paramJFileChooser);
/*      */ 
/*  462 */     Locale localLocale = paramJFileChooser.getLocale();
/*      */ 
/*  464 */     this.lookInLabelMnemonic = getMnemonic("FileChooser.lookInLabelMnemonic", localLocale).intValue();
/*  465 */     this.lookInLabelText = UIManager.getString("FileChooser.lookInLabelText", localLocale);
/*  466 */     this.saveInLabelText = UIManager.getString("FileChooser.saveInLabelText", localLocale);
/*      */ 
/*  468 */     this.fileNameLabelMnemonic = getMnemonic("FileChooser.fileNameLabelMnemonic", localLocale).intValue();
/*  469 */     this.fileNameLabelText = UIManager.getString("FileChooser.fileNameLabelText", localLocale);
/*  470 */     this.folderNameLabelMnemonic = getMnemonic("FileChooser.folderNameLabelMnemonic", localLocale).intValue();
/*  471 */     this.folderNameLabelText = UIManager.getString("FileChooser.folderNameLabelText", localLocale);
/*      */ 
/*  473 */     this.filesOfTypeLabelMnemonic = getMnemonic("FileChooser.filesOfTypeLabelMnemonic", localLocale).intValue();
/*  474 */     this.filesOfTypeLabelText = UIManager.getString("FileChooser.filesOfTypeLabelText", localLocale);
/*      */ 
/*  476 */     this.upFolderToolTipText = UIManager.getString("FileChooser.upFolderToolTipText", localLocale);
/*  477 */     this.upFolderAccessibleName = UIManager.getString("FileChooser.upFolderAccessibleName", localLocale);
/*      */ 
/*  479 */     this.homeFolderToolTipText = UIManager.getString("FileChooser.homeFolderToolTipText", localLocale);
/*  480 */     this.homeFolderAccessibleName = UIManager.getString("FileChooser.homeFolderAccessibleName", localLocale);
/*      */ 
/*  482 */     this.newFolderToolTipText = UIManager.getString("FileChooser.newFolderToolTipText", localLocale);
/*  483 */     this.newFolderAccessibleName = UIManager.getString("FileChooser.newFolderAccessibleName", localLocale);
/*      */ 
/*  485 */     this.listViewButtonToolTipText = UIManager.getString("FileChooser.listViewButtonToolTipText", localLocale);
/*  486 */     this.listViewButtonAccessibleName = UIManager.getString("FileChooser.listViewButtonAccessibleName", localLocale);
/*      */ 
/*  488 */     this.detailsViewButtonToolTipText = UIManager.getString("FileChooser.detailsViewButtonToolTipText", localLocale);
/*  489 */     this.detailsViewButtonAccessibleName = UIManager.getString("FileChooser.detailsViewButtonAccessibleName", localLocale);
/*      */   }
/*      */ 
/*      */   private Integer getMnemonic(String paramString, Locale paramLocale) {
/*  493 */     return Integer.valueOf(SwingUtilities2.getUIDefaultsInt(paramString, paramLocale));
/*      */   }
/*      */ 
/*      */   protected void installListeners(JFileChooser paramJFileChooser) {
/*  497 */     super.installListeners(paramJFileChooser);
/*  498 */     ActionMap localActionMap = getActionMap();
/*  499 */     SwingUtilities.replaceUIActionMap(paramJFileChooser, localActionMap);
/*      */   }
/*      */ 
/*      */   protected ActionMap getActionMap() {
/*  503 */     return createActionMap();
/*      */   }
/*      */ 
/*      */   protected ActionMap createActionMap() {
/*  507 */     ActionMapUIResource localActionMapUIResource = new ActionMapUIResource();
/*  508 */     FilePane.addActionsToMap(localActionMapUIResource, this.filePane.getActions());
/*  509 */     return localActionMapUIResource;
/*      */   }
/*      */ 
/*      */   protected JPanel createList(JFileChooser paramJFileChooser) {
/*  513 */     return this.filePane.createList();
/*      */   }
/*      */ 
/*      */   protected JPanel createDetailsView(JFileChooser paramJFileChooser) {
/*  517 */     return this.filePane.createDetailsView();
/*      */   }
/*      */ 
/*      */   public ListSelectionListener createListSelectionListener(JFileChooser paramJFileChooser)
/*      */   {
/*  527 */     return super.createListSelectionListener(paramJFileChooser);
/*      */   }
/*      */ 
/*      */   public void uninstallUI(JComponent paramJComponent)
/*      */   {
/*  542 */     paramJComponent.removePropertyChangeListener(this.filterComboBoxModel);
/*  543 */     paramJComponent.removePropertyChangeListener(this.filePane);
/*  544 */     this.cancelButton.removeActionListener(getCancelSelectionAction());
/*  545 */     this.approveButton.removeActionListener(getApproveSelectionAction());
/*  546 */     this.fileNameTextField.removeActionListener(getApproveSelectionAction());
/*      */ 
/*  548 */     if (this.filePane != null) {
/*  549 */       this.filePane.uninstallUI();
/*  550 */       this.filePane = null;
/*      */     }
/*      */ 
/*  553 */     super.uninstallUI(paramJComponent);
/*      */   }
/*      */ 
/*      */   public Dimension getPreferredSize(JComponent paramJComponent)
/*      */   {
/*  569 */     int i = PREF_SIZE.width;
/*  570 */     Dimension localDimension = paramJComponent.getLayout().preferredLayoutSize(paramJComponent);
/*  571 */     if (localDimension != null) {
/*  572 */       return new Dimension(localDimension.width < i ? i : localDimension.width, localDimension.height < PREF_SIZE.height ? PREF_SIZE.height : localDimension.height);
/*      */     }
/*      */ 
/*  575 */     return new Dimension(i, PREF_SIZE.height);
/*      */   }
/*      */ 
/*      */   public Dimension getMinimumSize(JComponent paramJComponent)
/*      */   {
/*  587 */     return MIN_SIZE;
/*      */   }
/*      */ 
/*      */   public Dimension getMaximumSize(JComponent paramJComponent)
/*      */   {
/*  598 */     return new Dimension(2147483647, 2147483647);
/*      */   }
/*      */ 
/*      */   private String fileNameString(File paramFile) {
/*  602 */     if (paramFile == null) {
/*  603 */       return null;
/*      */     }
/*  605 */     JFileChooser localJFileChooser = getFileChooser();
/*  606 */     if (((localJFileChooser.isDirectorySelectionEnabled()) && (!localJFileChooser.isFileSelectionEnabled())) || ((localJFileChooser.isDirectorySelectionEnabled()) && (localJFileChooser.isFileSelectionEnabled()) && (localJFileChooser.getFileSystemView().isFileSystemRoot(paramFile))))
/*      */     {
/*  608 */       return paramFile.getPath();
/*      */     }
/*  610 */     return paramFile.getName();
/*      */   }
/*      */ 
/*      */   private String fileNameString(File[] paramArrayOfFile)
/*      */   {
/*  616 */     StringBuffer localStringBuffer = new StringBuffer();
/*  617 */     for (int i = 0; (paramArrayOfFile != null) && (i < paramArrayOfFile.length); i++) {
/*  618 */       if (i > 0) {
/*  619 */         localStringBuffer.append(" ");
/*      */       }
/*  621 */       if (paramArrayOfFile.length > 1) {
/*  622 */         localStringBuffer.append("\"");
/*      */       }
/*  624 */       localStringBuffer.append(fileNameString(paramArrayOfFile[i]));
/*  625 */       if (paramArrayOfFile.length > 1) {
/*  626 */         localStringBuffer.append("\"");
/*      */       }
/*      */     }
/*  629 */     return localStringBuffer.toString();
/*      */   }
/*      */ 
/*      */   private void doSelectedFileChanged(PropertyChangeEvent paramPropertyChangeEvent)
/*      */   {
/*  635 */     File localFile = (File)paramPropertyChangeEvent.getNewValue();
/*  636 */     JFileChooser localJFileChooser = getFileChooser();
/*  637 */     if ((localFile != null) && (((localJFileChooser.isFileSelectionEnabled()) && (!localFile.isDirectory())) || ((localFile.isDirectory()) && (localJFileChooser.isDirectorySelectionEnabled()))))
/*      */     {
/*  641 */       setFileName(fileNameString(localFile));
/*      */     }
/*      */   }
/*      */ 
/*      */   private void doSelectedFilesChanged(PropertyChangeEvent paramPropertyChangeEvent) {
/*  646 */     File[] arrayOfFile = (File[])paramPropertyChangeEvent.getNewValue();
/*  647 */     JFileChooser localJFileChooser = getFileChooser();
/*  648 */     if ((arrayOfFile != null) && (arrayOfFile.length > 0) && ((arrayOfFile.length > 1) || (localJFileChooser.isDirectorySelectionEnabled()) || (!arrayOfFile[0].isDirectory())))
/*      */     {
/*  651 */       setFileName(fileNameString(arrayOfFile));
/*      */     }
/*      */   }
/*      */ 
/*      */   private void doDirectoryChanged(PropertyChangeEvent paramPropertyChangeEvent) {
/*  656 */     JFileChooser localJFileChooser = getFileChooser();
/*  657 */     FileSystemView localFileSystemView = localJFileChooser.getFileSystemView();
/*      */ 
/*  659 */     clearIconCache();
/*  660 */     File localFile = localJFileChooser.getCurrentDirectory();
/*  661 */     if (localFile != null) {
/*  662 */       this.directoryComboBoxModel.addItem(localFile);
/*      */ 
/*  664 */       if ((localJFileChooser.isDirectorySelectionEnabled()) && (!localJFileChooser.isFileSelectionEnabled()))
/*  665 */         if (localFileSystemView.isFileSystem(localFile))
/*  666 */           setFileName(localFile.getPath());
/*      */         else
/*  668 */           setFileName(null);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void doFilterChanged(PropertyChangeEvent paramPropertyChangeEvent)
/*      */   {
/*  675 */     clearIconCache();
/*      */   }
/*      */ 
/*      */   private void doFileSelectionModeChanged(PropertyChangeEvent paramPropertyChangeEvent) {
/*  679 */     if (this.fileNameLabel != null) {
/*  680 */       populateFileNameLabel();
/*      */     }
/*  682 */     clearIconCache();
/*      */ 
/*  684 */     JFileChooser localJFileChooser = getFileChooser();
/*  685 */     File localFile = localJFileChooser.getCurrentDirectory();
/*  686 */     if ((localFile != null) && (localJFileChooser.isDirectorySelectionEnabled()) && (!localJFileChooser.isFileSelectionEnabled()) && (localJFileChooser.getFileSystemView().isFileSystem(localFile)))
/*      */     {
/*  691 */       setFileName(localFile.getPath());
/*      */     }
/*  693 */     else setFileName(null);
/*      */   }
/*      */ 
/*      */   private void doAccessoryChanged(PropertyChangeEvent paramPropertyChangeEvent)
/*      */   {
/*  698 */     if (getAccessoryPanel() != null) {
/*  699 */       if (paramPropertyChangeEvent.getOldValue() != null) {
/*  700 */         getAccessoryPanel().remove((JComponent)paramPropertyChangeEvent.getOldValue());
/*      */       }
/*  702 */       JComponent localJComponent = (JComponent)paramPropertyChangeEvent.getNewValue();
/*  703 */       if (localJComponent != null)
/*  704 */         getAccessoryPanel().add(localJComponent, "Center");
/*      */     }
/*      */   }
/*      */ 
/*      */   private void doApproveButtonTextChanged(PropertyChangeEvent paramPropertyChangeEvent)
/*      */   {
/*  710 */     JFileChooser localJFileChooser = getFileChooser();
/*  711 */     this.approveButton.setText(getApproveButtonText(localJFileChooser));
/*  712 */     this.approveButton.setToolTipText(getApproveButtonToolTipText(localJFileChooser));
/*      */   }
/*      */ 
/*      */   private void doDialogTypeChanged(PropertyChangeEvent paramPropertyChangeEvent) {
/*  716 */     JFileChooser localJFileChooser = getFileChooser();
/*  717 */     this.approveButton.setText(getApproveButtonText(localJFileChooser));
/*  718 */     this.approveButton.setToolTipText(getApproveButtonToolTipText(localJFileChooser));
/*  719 */     if (localJFileChooser.getDialogType() == 1)
/*  720 */       this.lookInLabel.setText(this.saveInLabelText);
/*      */     else
/*  722 */       this.lookInLabel.setText(this.lookInLabelText);
/*      */   }
/*      */ 
/*      */   private void doApproveButtonMnemonicChanged(PropertyChangeEvent paramPropertyChangeEvent)
/*      */   {
/*      */   }
/*      */ 
/*      */   private void doControlButtonsChanged(PropertyChangeEvent paramPropertyChangeEvent)
/*      */   {
/*  731 */     if (getFileChooser().getControlButtonsAreShown())
/*  732 */       addControlButtons();
/*      */     else
/*  734 */       removeControlButtons();
/*      */   }
/*      */ 
/*      */   public PropertyChangeListener createPropertyChangeListener(JFileChooser paramJFileChooser)
/*      */   {
/*  743 */     return new PropertyChangeListener() {
/*      */       public void propertyChange(PropertyChangeEvent paramAnonymousPropertyChangeEvent) {
/*  745 */         String str = paramAnonymousPropertyChangeEvent.getPropertyName();
/*  746 */         if (str.equals("SelectedFileChangedProperty")) {
/*  747 */           MetalFileChooserUI.this.doSelectedFileChanged(paramAnonymousPropertyChangeEvent);
/*  748 */         } else if (str.equals("SelectedFilesChangedProperty")) {
/*  749 */           MetalFileChooserUI.this.doSelectedFilesChanged(paramAnonymousPropertyChangeEvent);
/*  750 */         } else if (str.equals("directoryChanged")) {
/*  751 */           MetalFileChooserUI.this.doDirectoryChanged(paramAnonymousPropertyChangeEvent);
/*  752 */         } else if (str.equals("fileFilterChanged")) {
/*  753 */           MetalFileChooserUI.this.doFilterChanged(paramAnonymousPropertyChangeEvent);
/*  754 */         } else if (str.equals("fileSelectionChanged")) {
/*  755 */           MetalFileChooserUI.this.doFileSelectionModeChanged(paramAnonymousPropertyChangeEvent);
/*  756 */         } else if (str.equals("AccessoryChangedProperty")) {
/*  757 */           MetalFileChooserUI.this.doAccessoryChanged(paramAnonymousPropertyChangeEvent);
/*  758 */         } else if ((str.equals("ApproveButtonTextChangedProperty")) || (str.equals("ApproveButtonToolTipTextChangedProperty")))
/*      */         {
/*  760 */           MetalFileChooserUI.this.doApproveButtonTextChanged(paramAnonymousPropertyChangeEvent);
/*  761 */         } else if (str.equals("DialogTypeChangedProperty")) {
/*  762 */           MetalFileChooserUI.this.doDialogTypeChanged(paramAnonymousPropertyChangeEvent);
/*  763 */         } else if (str.equals("ApproveButtonMnemonicChangedProperty")) {
/*  764 */           MetalFileChooserUI.this.doApproveButtonMnemonicChanged(paramAnonymousPropertyChangeEvent);
/*  765 */         } else if (str.equals("ControlButtonsAreShownChangedProperty")) {
/*  766 */           MetalFileChooserUI.this.doControlButtonsChanged(paramAnonymousPropertyChangeEvent);
/*  767 */         } else if (str.equals("componentOrientation")) {
/*  768 */           ComponentOrientation localComponentOrientation = (ComponentOrientation)paramAnonymousPropertyChangeEvent.getNewValue();
/*  769 */           JFileChooser localJFileChooser = (JFileChooser)paramAnonymousPropertyChangeEvent.getSource();
/*  770 */           if (localComponentOrientation != paramAnonymousPropertyChangeEvent.getOldValue())
/*  771 */             localJFileChooser.applyComponentOrientation(localComponentOrientation);
/*      */         }
/*  773 */         else if (str == "FileChooser.useShellFolder") {
/*  774 */           MetalFileChooserUI.this.doDirectoryChanged(paramAnonymousPropertyChangeEvent);
/*  775 */         } else if ((str.equals("ancestor")) && 
/*  776 */           (paramAnonymousPropertyChangeEvent.getOldValue() == null) && (paramAnonymousPropertyChangeEvent.getNewValue() != null))
/*      */         {
/*  778 */           MetalFileChooserUI.this.fileNameTextField.selectAll();
/*  779 */           MetalFileChooserUI.this.fileNameTextField.requestFocus();
/*      */         }
/*      */       }
/*      */     };
/*      */   }
/*      */ 
/*      */   protected void removeControlButtons()
/*      */   {
/*  788 */     getBottomPanel().remove(getButtonPanel());
/*      */   }
/*      */ 
/*      */   protected void addControlButtons() {
/*  792 */     getBottomPanel().add(getButtonPanel());
/*      */   }
/*      */ 
/*      */   public void ensureFileIsVisible(JFileChooser paramJFileChooser, File paramFile) {
/*  796 */     this.filePane.ensureFileIsVisible(paramJFileChooser, paramFile);
/*      */   }
/*      */ 
/*      */   public void rescanCurrentDirectory(JFileChooser paramJFileChooser) {
/*  800 */     this.filePane.rescanCurrentDirectory();
/*      */   }
/*      */ 
/*      */   public String getFileName() {
/*  804 */     if (this.fileNameTextField != null) {
/*  805 */       return this.fileNameTextField.getText();
/*      */     }
/*  807 */     return null;
/*      */   }
/*      */ 
/*      */   public void setFileName(String paramString)
/*      */   {
/*  812 */     if (this.fileNameTextField != null)
/*  813 */       this.fileNameTextField.setText(paramString);
/*      */   }
/*      */ 
/*      */   protected void setDirectorySelected(boolean paramBoolean)
/*      */   {
/*  825 */     super.setDirectorySelected(paramBoolean);
/*  826 */     JFileChooser localJFileChooser = getFileChooser();
/*  827 */     if (paramBoolean) {
/*  828 */       if (this.approveButton != null) {
/*  829 */         this.approveButton.setText(this.directoryOpenButtonText);
/*  830 */         this.approveButton.setToolTipText(this.directoryOpenButtonToolTipText);
/*      */       }
/*      */     }
/*  833 */     else if (this.approveButton != null) {
/*  834 */       this.approveButton.setText(getApproveButtonText(localJFileChooser));
/*  835 */       this.approveButton.setToolTipText(getApproveButtonToolTipText(localJFileChooser));
/*      */     }
/*      */   }
/*      */ 
/*      */   public String getDirectoryName()
/*      */   {
/*  842 */     return null;
/*      */   }
/*      */ 
/*      */   public void setDirectoryName(String paramString)
/*      */   {
/*      */   }
/*      */ 
/*      */   protected DirectoryComboBoxRenderer createDirectoryComboBoxRenderer(JFileChooser paramJFileChooser) {
/*  850 */     return new DirectoryComboBoxRenderer();
/*      */   }
/*      */ 
/*      */   protected DirectoryComboBoxModel createDirectoryComboBoxModel(JFileChooser paramJFileChooser)
/*      */   {
/*  907 */     return new DirectoryComboBoxModel();
/*      */   }
/*      */ 
/*      */   protected FilterComboBoxRenderer createFilterComboBoxRenderer()
/*      */   {
/* 1032 */     return new FilterComboBoxRenderer();
/*      */   }
/*      */ 
/*      */   protected FilterComboBoxModel createFilterComboBoxModel()
/*      */   {
/* 1057 */     return new FilterComboBoxModel();
/*      */   }
/*      */ 
/*      */   public void valueChanged(ListSelectionEvent paramListSelectionEvent)
/*      */   {
/* 1130 */     JFileChooser localJFileChooser = getFileChooser();
/* 1131 */     File localFile = localJFileChooser.getSelectedFile();
/* 1132 */     if ((!paramListSelectionEvent.getValueIsAdjusting()) && (localFile != null) && (!getFileChooser().isTraversable(localFile)))
/* 1133 */       setFileName(fileNameString(localFile));
/*      */   }
/*      */ 
/*      */   protected JButton getApproveButton(JFileChooser paramJFileChooser)
/*      */   {
/* 1155 */     return this.approveButton;
/*      */   }
/*      */ 
/*      */   private static void groupLabels(AlignedLabel[] paramArrayOfAlignedLabel)
/*      */   {
/* 1235 */     for (int i = 0; i < paramArrayOfAlignedLabel.length; i++)
/* 1236 */       paramArrayOfAlignedLabel[i].group = paramArrayOfAlignedLabel;
/*      */   }
/*      */ 
/*      */   private class AlignedLabel extends JLabel
/*      */   {
/*      */     private AlignedLabel[] group;
/* 1242 */     private int maxWidth = 0;
/*      */ 
/*      */     AlignedLabel()
/*      */     {
/* 1246 */       setAlignmentX(0.0F);
/*      */     }
/*      */ 
/*      */     AlignedLabel(String arg2)
/*      */     {
/* 1251 */       super();
/* 1252 */       setAlignmentX(0.0F);
/*      */     }
/*      */ 
/*      */     public Dimension getPreferredSize() {
/* 1256 */       Dimension localDimension = super.getPreferredSize();
/*      */ 
/* 1258 */       return new Dimension(getMaxWidth() + 11, localDimension.height);
/*      */     }
/*      */ 
/*      */     private int getMaxWidth() {
/* 1262 */       if ((this.maxWidth == 0) && (this.group != null)) {
/* 1263 */         int i = 0;
/* 1264 */         for (int j = 0; j < this.group.length; j++) {
/* 1265 */           i = Math.max(this.group[j].getSuperPreferredWidth(), i);
/*      */         }
/* 1267 */         for (j = 0; j < this.group.length; j++) {
/* 1268 */           this.group[j].maxWidth = i;
/*      */         }
/*      */       }
/* 1271 */       return this.maxWidth;
/*      */     }
/*      */ 
/*      */     private int getSuperPreferredWidth() {
/* 1275 */       return super.getPreferredSize().width;
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class ButtonAreaLayout
/*      */     implements LayoutManager
/*      */   {
/* 1166 */     private int hGap = 5;
/* 1167 */     private int topMargin = 17;
/*      */ 
/*      */     public void addLayoutComponent(String paramString, Component paramComponent) {
/*      */     }
/*      */ 
/*      */     public void layoutContainer(Container paramContainer) {
/* 1173 */       Component[] arrayOfComponent = paramContainer.getComponents();
/*      */ 
/* 1175 */       if ((arrayOfComponent != null) && (arrayOfComponent.length > 0)) {
/* 1176 */         int i = arrayOfComponent.length;
/* 1177 */         Dimension[] arrayOfDimension = new Dimension[i];
/* 1178 */         Insets localInsets = paramContainer.getInsets();
/* 1179 */         int j = localInsets.top + this.topMargin;
/* 1180 */         int k = 0;
/*      */ 
/* 1182 */         for (int m = 0; m < i; m++) {
/* 1183 */           arrayOfDimension[m] = arrayOfComponent[m].getPreferredSize();
/* 1184 */           k = Math.max(k, arrayOfDimension[m].width);
/*      */         }
/*      */         int n;
/* 1187 */         if (paramContainer.getComponentOrientation().isLeftToRight()) {
/* 1188 */           m = paramContainer.getSize().width - localInsets.left - k;
/* 1189 */           n = this.hGap + k;
/*      */         } else {
/* 1191 */           m = localInsets.left;
/* 1192 */           n = -(this.hGap + k);
/*      */         }
/* 1194 */         for (int i1 = i - 1; i1 >= 0; i1--) {
/* 1195 */           arrayOfComponent[i1].setBounds(m, j, k, arrayOfDimension[i1].height);
/*      */ 
/* 1197 */           m -= n;
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     public Dimension minimumLayoutSize(Container paramContainer) {
/* 1203 */       if (paramContainer != null) {
/* 1204 */         Component[] arrayOfComponent = paramContainer.getComponents();
/*      */ 
/* 1206 */         if ((arrayOfComponent != null) && (arrayOfComponent.length > 0)) {
/* 1207 */           int i = arrayOfComponent.length;
/* 1208 */           int j = 0;
/* 1209 */           Insets localInsets = paramContainer.getInsets();
/* 1210 */           int k = this.topMargin + localInsets.top + localInsets.bottom;
/* 1211 */           int m = localInsets.left + localInsets.right;
/* 1212 */           int n = 0;
/*      */ 
/* 1214 */           for (int i1 = 0; i1 < i; i1++) {
/* 1215 */             Dimension localDimension = arrayOfComponent[i1].getPreferredSize();
/* 1216 */             j = Math.max(j, localDimension.height);
/* 1217 */             n = Math.max(n, localDimension.width);
/*      */           }
/* 1219 */           return new Dimension(m + i * n + (i - 1) * this.hGap, k + j);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1224 */       return new Dimension(0, 0);
/*      */     }
/*      */ 
/*      */     public Dimension preferredLayoutSize(Container paramContainer) {
/* 1228 */       return minimumLayoutSize(paramContainer);
/*      */     }
/*      */ 
/*      */     public void removeLayoutComponent(Component paramComponent)
/*      */     {
/*      */     }
/*      */   }
/*      */ 
/*      */   protected class DirectoryComboBoxAction extends AbstractAction
/*      */   {
/*      */     protected DirectoryComboBoxAction()
/*      */     {
/* 1142 */       super();
/*      */     }
/*      */ 
/*      */     public void actionPerformed(ActionEvent paramActionEvent) {
/* 1146 */       MetalFileChooserUI.this.directoryComboBox.hidePopup();
/* 1147 */       File localFile = (File)MetalFileChooserUI.this.directoryComboBox.getSelectedItem();
/* 1148 */       if (!MetalFileChooserUI.this.getFileChooser().getCurrentDirectory().equals(localFile))
/* 1149 */         MetalFileChooserUI.this.getFileChooser().setCurrentDirectory(localFile);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected class DirectoryComboBoxModel extends AbstractListModel<Object>
/*      */     implements ComboBoxModel<Object>
/*      */   {
/*  914 */     Vector<File> directories = new Vector();
/*  915 */     int[] depths = null;
/*  916 */     File selectedDirectory = null;
/*  917 */     JFileChooser chooser = MetalFileChooserUI.this.getFileChooser();
/*  918 */     FileSystemView fsv = this.chooser.getFileSystemView();
/*      */ 
/*      */     public DirectoryComboBoxModel()
/*      */     {
/*  923 */       File localFile = MetalFileChooserUI.this.getFileChooser().getCurrentDirectory();
/*  924 */       if (localFile != null)
/*  925 */         addItem(localFile);
/*      */     }
/*      */ 
/*      */     private void addItem(File paramFile)
/*      */     {
/*  936 */       if (paramFile == null) {
/*  937 */         return;
/*      */       }
/*      */ 
/*  940 */       boolean bool = FilePane.usesShellFolder(this.chooser);
/*      */ 
/*  942 */       this.directories.clear();
/*      */ 
/*  944 */       File[] arrayOfFile = bool ? (File[])ShellFolder.get("fileChooserComboBoxFolders") : this.fsv.getRoots();
/*      */ 
/*  947 */       this.directories.addAll(Arrays.asList(arrayOfFile));
/*      */       File localFile1;
/*      */       try
/*      */       {
/*  954 */         localFile1 = ShellFolder.getNormalizedFile(paramFile);
/*      */       }
/*      */       catch (IOException localIOException) {
/*  957 */         localFile1 = paramFile;
/*      */       }
/*      */ 
/*      */       try
/*      */       {
/*  962 */         File localFile2 = bool ? ShellFolder.getShellFolder(localFile1) : localFile1;
/*      */ 
/*  964 */         File localFile3 = localFile2;
/*  965 */         Vector localVector = new Vector(10);
/*      */         do
/*  967 */           localVector.addElement(localFile3);
/*  968 */         while ((localFile3 = localFile3.getParentFile()) != null);
/*      */ 
/*  970 */         int i = localVector.size();
/*      */ 
/*  972 */         for (int j = 0; j < i; j++) {
/*  973 */           localFile3 = (File)localVector.get(j);
/*  974 */           if (this.directories.contains(localFile3)) {
/*  975 */             int k = this.directories.indexOf(localFile3);
/*  976 */             for (int m = j - 1; m >= 0; m--) {
/*  977 */               this.directories.insertElementAt(localVector.get(m), k + j - m);
/*      */             }
/*  979 */             break;
/*      */           }
/*      */         }
/*  982 */         calculateDepths();
/*  983 */         setSelectedItem(localFile2);
/*      */       } catch (FileNotFoundException localFileNotFoundException) {
/*  985 */         calculateDepths();
/*      */       }
/*      */     }
/*      */ 
/*      */     private void calculateDepths() {
/*  990 */       this.depths = new int[this.directories.size()];
/*  991 */       for (int i = 0; i < this.depths.length; i++) {
/*  992 */         File localFile1 = (File)this.directories.get(i);
/*  993 */         File localFile2 = localFile1.getParentFile();
/*  994 */         this.depths[i] = 0;
/*  995 */         if (localFile2 != null)
/*  996 */           for (int j = i - 1; j >= 0; j--)
/*  997 */             if (localFile2.equals(this.directories.get(j))) {
/*  998 */               this.depths[j] += 1;
/*  999 */               break;
/*      */             }
/*      */       }
/*      */     }
/*      */ 
/*      */     public int getDepth(int paramInt)
/*      */     {
/* 1007 */       return (this.depths != null) && (paramInt >= 0) && (paramInt < this.depths.length) ? this.depths[paramInt] : 0;
/*      */     }
/*      */ 
/*      */     public void setSelectedItem(Object paramObject) {
/* 1011 */       this.selectedDirectory = ((File)paramObject);
/* 1012 */       fireContentsChanged(this, -1, -1);
/*      */     }
/*      */ 
/*      */     public Object getSelectedItem() {
/* 1016 */       return this.selectedDirectory;
/*      */     }
/*      */ 
/*      */     public int getSize() {
/* 1020 */       return this.directories.size();
/*      */     }
/*      */ 
/*      */     public Object getElementAt(int paramInt) {
/* 1024 */       return this.directories.elementAt(paramInt);
/*      */     }
/*      */   }
/*      */ 
/*      */   class DirectoryComboBoxRenderer extends DefaultListCellRenderer
/*      */   {
/*  857 */     MetalFileChooserUI.IndentIcon ii = new MetalFileChooserUI.IndentIcon(MetalFileChooserUI.this);
/*      */ 
/*      */     DirectoryComboBoxRenderer() {
/*      */     }
/*      */     public Component getListCellRendererComponent(JList paramJList, Object paramObject, int paramInt, boolean paramBoolean1, boolean paramBoolean2) {
/*  862 */       super.getListCellRendererComponent(paramJList, paramObject, paramInt, paramBoolean1, paramBoolean2);
/*      */ 
/*  864 */       if (paramObject == null) {
/*  865 */         setText("");
/*  866 */         return this;
/*      */       }
/*  868 */       File localFile = (File)paramObject;
/*  869 */       setText(MetalFileChooserUI.this.getFileChooser().getName(localFile));
/*  870 */       Icon localIcon = MetalFileChooserUI.this.getFileChooser().getIcon(localFile);
/*  871 */       this.ii.icon = localIcon;
/*  872 */       this.ii.depth = MetalFileChooserUI.this.directoryComboBoxModel.getDepth(paramInt);
/*  873 */       setIcon(this.ii);
/*      */ 
/*  875 */       return this;
/*      */     }
/*      */   }
/*      */ 
/*      */   protected class FileRenderer extends DefaultListCellRenderer
/*      */   {
/*      */     protected FileRenderer()
/*      */     {
/*      */     }
/*      */   }
/*      */ 
/*      */   protected class FilterComboBoxModel extends AbstractListModel<Object>
/*      */     implements ComboBoxModel<Object>, PropertyChangeListener
/*      */   {
/*      */     protected FileFilter[] filters;
/*      */ 
/*      */     protected FilterComboBoxModel()
/*      */     {
/* 1067 */       this.filters = MetalFileChooserUI.this.getFileChooser().getChoosableFileFilters();
/*      */     }
/*      */ 
/*      */     public void propertyChange(PropertyChangeEvent paramPropertyChangeEvent) {
/* 1071 */       String str = paramPropertyChangeEvent.getPropertyName();
/* 1072 */       if (str == "ChoosableFileFilterChangedProperty") {
/* 1073 */         this.filters = ((FileFilter[])paramPropertyChangeEvent.getNewValue());
/* 1074 */         fireContentsChanged(this, -1, -1);
/* 1075 */       } else if (str == "fileFilterChanged") {
/* 1076 */         fireContentsChanged(this, -1, -1);
/*      */       }
/*      */     }
/*      */ 
/*      */     public void setSelectedItem(Object paramObject) {
/* 1081 */       if (paramObject != null) {
/* 1082 */         MetalFileChooserUI.this.getFileChooser().setFileFilter((FileFilter)paramObject);
/* 1083 */         fireContentsChanged(this, -1, -1);
/*      */       }
/*      */     }
/*      */ 
/*      */     public Object getSelectedItem()
/*      */     {
/* 1093 */       FileFilter localFileFilter1 = MetalFileChooserUI.this.getFileChooser().getFileFilter();
/* 1094 */       int i = 0;
/* 1095 */       if (localFileFilter1 != null) {
/* 1096 */         for (FileFilter localFileFilter2 : this.filters) {
/* 1097 */           if (localFileFilter2 == localFileFilter1) {
/* 1098 */             i = 1;
/*      */           }
/*      */         }
/* 1101 */         if (i == 0) {
/* 1102 */           MetalFileChooserUI.this.getFileChooser().addChoosableFileFilter(localFileFilter1);
/*      */         }
/*      */       }
/* 1105 */       return MetalFileChooserUI.this.getFileChooser().getFileFilter();
/*      */     }
/*      */ 
/*      */     public int getSize() {
/* 1109 */       if (this.filters != null) {
/* 1110 */         return this.filters.length;
/*      */       }
/* 1112 */       return 0;
/*      */     }
/*      */ 
/*      */     public Object getElementAt(int paramInt)
/*      */     {
/* 1117 */       if (paramInt > getSize() - 1)
/*      */       {
/* 1119 */         return MetalFileChooserUI.this.getFileChooser().getFileFilter();
/*      */       }
/* 1121 */       if (this.filters != null) {
/* 1122 */         return this.filters[paramInt];
/*      */       }
/* 1124 */       return null;
/*      */     }
/*      */   }
/*      */ 
/*      */   public class FilterComboBoxRenderer extends DefaultListCellRenderer
/*      */   {
/*      */     public FilterComboBoxRenderer()
/*      */     {
/*      */     }
/*      */ 
/*      */     public Component getListCellRendererComponent(JList paramJList, Object paramObject, int paramInt, boolean paramBoolean1, boolean paramBoolean2)
/*      */     {
/* 1043 */       super.getListCellRendererComponent(paramJList, paramObject, paramInt, paramBoolean1, paramBoolean2);
/*      */ 
/* 1045 */       if ((paramObject != null) && ((paramObject instanceof FileFilter))) {
/* 1046 */         setText(((FileFilter)paramObject).getDescription());
/*      */       }
/*      */ 
/* 1049 */       return this;
/*      */     }
/*      */   }
/*      */ 
/*      */   class IndentIcon
/*      */     implements Icon
/*      */   {
/*  882 */     Icon icon = null;
/*  883 */     int depth = 0;
/*      */ 
/*      */     IndentIcon() {  } 
/*  886 */     public void paintIcon(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2) { if (paramComponent.getComponentOrientation().isLeftToRight())
/*  887 */         this.icon.paintIcon(paramComponent, paramGraphics, paramInt1 + this.depth * 10, paramInt2);
/*      */       else
/*  889 */         this.icon.paintIcon(paramComponent, paramGraphics, paramInt1, paramInt2);
/*      */     }
/*      */ 
/*      */     public int getIconWidth()
/*      */     {
/*  894 */       return this.icon.getIconWidth() + this.depth * 10;
/*      */     }
/*      */ 
/*      */     public int getIconHeight() {
/*  898 */       return this.icon.getIconHeight();
/*      */     }
/*      */   }
/*      */ 
/*      */   private class MetalFileChooserUIAccessor
/*      */     implements FilePane.FileChooserUIAccessor
/*      */   {
/*      */     private MetalFileChooserUIAccessor()
/*      */     {
/*      */     }
/*      */ 
/*      */     public JFileChooser getFileChooser()
/*      */     {
/*  164 */       return MetalFileChooserUI.this.getFileChooser();
/*      */     }
/*      */ 
/*      */     public BasicDirectoryModel getModel() {
/*  168 */       return MetalFileChooserUI.this.getModel();
/*      */     }
/*      */ 
/*      */     public JPanel createList() {
/*  172 */       return MetalFileChooserUI.this.createList(getFileChooser());
/*      */     }
/*      */ 
/*      */     public JPanel createDetailsView() {
/*  176 */       return MetalFileChooserUI.this.createDetailsView(getFileChooser());
/*      */     }
/*      */ 
/*      */     public boolean isDirectorySelected() {
/*  180 */       return MetalFileChooserUI.this.isDirectorySelected();
/*      */     }
/*      */ 
/*      */     public File getDirectory() {
/*  184 */       return MetalFileChooserUI.this.getDirectory();
/*      */     }
/*      */ 
/*      */     public Action getChangeToParentDirectoryAction() {
/*  188 */       return MetalFileChooserUI.this.getChangeToParentDirectoryAction();
/*      */     }
/*      */ 
/*      */     public Action getApproveSelectionAction() {
/*  192 */       return MetalFileChooserUI.this.getApproveSelectionAction();
/*      */     }
/*      */ 
/*      */     public Action getNewFolderAction() {
/*  196 */       return MetalFileChooserUI.this.getNewFolderAction();
/*      */     }
/*      */ 
/*      */     public MouseListener createDoubleClickListener(JList paramJList) {
/*  200 */       return MetalFileChooserUI.this.createDoubleClickListener(getFileChooser(), paramJList);
/*      */     }
/*      */ 
/*      */     public ListSelectionListener createListSelectionListener()
/*      */     {
/*  205 */       return MetalFileChooserUI.this.createListSelectionListener(getFileChooser());
/*      */     }
/*      */   }
/*      */ 
/*      */   protected class SingleClickListener extends MouseAdapter
/*      */   {
/*      */     public SingleClickListener(JList arg2)
/*      */     {
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.metal.MetalFileChooserUI
 * JD-Core Version:    0.6.2
 */