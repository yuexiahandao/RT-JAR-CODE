/*      */ package com.sun.java.swing.plaf.windows;
/*      */ 
/*      */ import java.awt.BorderLayout;
/*      */ import java.awt.Color;
/*      */ import java.awt.Component;
/*      */ import java.awt.ComponentOrientation;
/*      */ import java.awt.Container;
/*      */ import java.awt.Dimension;
/*      */ import java.awt.Graphics;
/*      */ import java.awt.Insets;
/*      */ import java.awt.LayoutManager;
/*      */ import java.awt.event.ActionEvent;
/*      */ import java.awt.event.ActionListener;
/*      */ import java.awt.event.FocusAdapter;
/*      */ import java.awt.event.FocusEvent;
/*      */ import java.awt.event.KeyAdapter;
/*      */ import java.awt.event.KeyEvent;
/*      */ import java.awt.event.MouseAdapter;
/*      */ import java.awt.event.MouseEvent;
/*      */ import java.awt.event.MouseListener;
/*      */ import java.awt.image.BufferedImage;
/*      */ import java.beans.PropertyChangeEvent;
/*      */ import java.beans.PropertyChangeListener;
/*      */ import java.io.File;
/*      */ import java.io.FileNotFoundException;
/*      */ import java.io.IOException;
/*      */ import java.util.Arrays;
/*      */ import java.util.Locale;
/*      */ import java.util.Vector;
/*      */ import javax.swing.AbstractListModel;
/*      */ import javax.swing.Action;
/*      */ import javax.swing.ActionMap;
/*      */ import javax.swing.Box;
/*      */ import javax.swing.BoxLayout;
/*      */ import javax.swing.ButtonGroup;
/*      */ import javax.swing.ButtonModel;
/*      */ import javax.swing.ComboBoxModel;
/*      */ import javax.swing.DefaultButtonModel;
/*      */ import javax.swing.DefaultListCellRenderer;
/*      */ import javax.swing.Icon;
/*      */ import javax.swing.ImageIcon;
/*      */ import javax.swing.JButton;
/*      */ import javax.swing.JComboBox;
/*      */ import javax.swing.JComponent;
/*      */ import javax.swing.JFileChooser;
/*      */ import javax.swing.JLabel;
/*      */ import javax.swing.JList;
/*      */ import javax.swing.JPanel;
/*      */ import javax.swing.JPopupMenu;
/*      */ import javax.swing.JRadioButtonMenuItem;
/*      */ import javax.swing.JTextField;
/*      */ import javax.swing.JToolBar;
/*      */ import javax.swing.SwingUtilities;
/*      */ import javax.swing.UIManager;
/*      */ import javax.swing.border.EmptyBorder;
/*      */ import javax.swing.event.ListSelectionEvent;
/*      */ import javax.swing.event.ListSelectionListener;
/*      */ import javax.swing.event.PopupMenuEvent;
/*      */ import javax.swing.event.PopupMenuListener;
/*      */ import javax.swing.filechooser.FileFilter;
/*      */ import javax.swing.filechooser.FileSystemView;
/*      */ import javax.swing.filechooser.FileView;
/*      */ import javax.swing.plaf.ActionMapUIResource;
/*      */ import javax.swing.plaf.ComponentUI;
/*      */ import javax.swing.plaf.InsetsUIResource;
/*      */ import javax.swing.plaf.basic.BasicDirectoryModel;
/*      */ import javax.swing.plaf.basic.BasicFileChooserUI;
/*      */ import javax.swing.plaf.basic.BasicFileChooserUI.BasicFileView;
/*      */ import javax.swing.plaf.basic.BasicFileChooserUI.NewFolderAction;
/*      */ import sun.awt.shell.ShellFolder;
/*      */ import sun.swing.FilePane;
/*      */ import sun.swing.FilePane.FileChooserUIAccessor;
/*      */ import sun.swing.SwingUtilities2;
/*      */ import sun.swing.WindowsPlacesBar;
/*      */ 
/*      */ public class WindowsFileChooserUI extends BasicFileChooserUI
/*      */ {
/*      */   private JPanel centerPanel;
/*      */   private JLabel lookInLabel;
/*      */   private JComboBox directoryComboBox;
/*      */   private DirectoryComboBoxModel directoryComboBoxModel;
/*   65 */   private ActionListener directoryComboBoxAction = new DirectoryComboBoxAction();
/*      */   private FilterComboBoxModel filterComboBoxModel;
/*      */   private JTextField filenameTextField;
/*      */   private FilePane filePane;
/*      */   private WindowsPlacesBar placesBar;
/*      */   private JButton approveButton;
/*      */   private JButton cancelButton;
/*      */   private JPanel buttonPanel;
/*      */   private JPanel bottomPanel;
/*      */   private JComboBox filterComboBox;
/*   81 */   private static final Dimension hstrut10 = new Dimension(10, 1);
/*      */ 
/*   83 */   private static final Dimension vstrut4 = new Dimension(1, 4);
/*   84 */   private static final Dimension vstrut6 = new Dimension(1, 6);
/*   85 */   private static final Dimension vstrut8 = new Dimension(1, 8);
/*      */ 
/*   87 */   private static final Insets shrinkwrap = new Insets(0, 0, 0, 0);
/*      */ 
/*   90 */   private static int PREF_WIDTH = 425;
/*   91 */   private static int PREF_HEIGHT = 245;
/*   92 */   private static Dimension PREF_SIZE = new Dimension(PREF_WIDTH, PREF_HEIGHT);
/*      */ 
/*   94 */   private static int MIN_WIDTH = 425;
/*   95 */   private static int MIN_HEIGHT = 245;
/*   96 */   private static Dimension MIN_SIZE = new Dimension(MIN_WIDTH, MIN_HEIGHT);
/*      */ 
/*   98 */   private static int LIST_PREF_WIDTH = 444;
/*   99 */   private static int LIST_PREF_HEIGHT = 138;
/*  100 */   private static Dimension LIST_PREF_SIZE = new Dimension(LIST_PREF_WIDTH, LIST_PREF_HEIGHT);
/*      */ 
/*  103 */   private int lookInLabelMnemonic = 0;
/*  104 */   private String lookInLabelText = null;
/*  105 */   private String saveInLabelText = null;
/*      */ 
/*  107 */   private int fileNameLabelMnemonic = 0;
/*  108 */   private String fileNameLabelText = null;
/*  109 */   private int folderNameLabelMnemonic = 0;
/*  110 */   private String folderNameLabelText = null;
/*      */ 
/*  112 */   private int filesOfTypeLabelMnemonic = 0;
/*  113 */   private String filesOfTypeLabelText = null;
/*      */ 
/*  115 */   private String upFolderToolTipText = null;
/*  116 */   private String upFolderAccessibleName = null;
/*      */ 
/*  118 */   private String newFolderToolTipText = null;
/*  119 */   private String newFolderAccessibleName = null;
/*      */ 
/*  121 */   private String viewMenuButtonToolTipText = null;
/*  122 */   private String viewMenuButtonAccessibleName = null;
/*      */ 
/*  124 */   private BasicFileChooserUI.BasicFileView fileView = new WindowsFileView();
/*      */   private JLabel fileNameLabel;
/*      */   static final int space = 10;
/*      */ 
/*      */   private void populateFileNameLabel()
/*      */   {
/*  129 */     if (getFileChooser().getFileSelectionMode() == 1) {
/*  130 */       this.fileNameLabel.setText(this.folderNameLabelText);
/*  131 */       this.fileNameLabel.setDisplayedMnemonic(this.folderNameLabelMnemonic);
/*      */     } else {
/*  133 */       this.fileNameLabel.setText(this.fileNameLabelText);
/*  134 */       this.fileNameLabel.setDisplayedMnemonic(this.fileNameLabelMnemonic);
/*      */     }
/*      */   }
/*      */ 
/*      */   public static ComponentUI createUI(JComponent paramJComponent)
/*      */   {
/*  142 */     return new WindowsFileChooserUI((JFileChooser)paramJComponent);
/*      */   }
/*      */ 
/*      */   public WindowsFileChooserUI(JFileChooser paramJFileChooser) {
/*  146 */     super(paramJFileChooser);
/*      */   }
/*      */ 
/*      */   public void installUI(JComponent paramJComponent) {
/*  150 */     super.installUI(paramJComponent);
/*      */   }
/*      */ 
/*      */   public void uninstallComponents(JFileChooser paramJFileChooser) {
/*  154 */     paramJFileChooser.removeAll();
/*      */   }
/*      */ 
/*      */   public void installComponents(JFileChooser paramJFileChooser)
/*      */   {
/*  205 */     this.filePane = new FilePane(new WindowsFileChooserUIAccessor(null));
/*  206 */     paramJFileChooser.addPropertyChangeListener(this.filePane);
/*      */ 
/*  208 */     FileSystemView localFileSystemView = paramJFileChooser.getFileSystemView();
/*      */ 
/*  210 */     paramJFileChooser.setBorder(new EmptyBorder(4, 10, 10, 10));
/*  211 */     paramJFileChooser.setLayout(new BorderLayout(8, 8));
/*      */ 
/*  213 */     updateUseShellFolder();
/*      */ 
/*  220 */     JToolBar localJToolBar = new JToolBar();
/*  221 */     localJToolBar.setFloatable(false);
/*  222 */     localJToolBar.putClientProperty("JToolBar.isRollover", Boolean.TRUE);
/*      */ 
/*  225 */     paramJFileChooser.add(localJToolBar, "North");
/*      */ 
/*  228 */     this.lookInLabel = new JLabel(this.lookInLabelText, 11) {
/*      */       public Dimension getPreferredSize() {
/*  230 */         return getMinimumSize();
/*      */       }
/*      */ 
/*      */       public Dimension getMinimumSize() {
/*  234 */         Dimension localDimension = super.getPreferredSize();
/*  235 */         if (WindowsFileChooserUI.this.placesBar != null) {
/*  236 */           localDimension.width = Math.max(localDimension.width, WindowsFileChooserUI.this.placesBar.getWidth());
/*      */         }
/*  238 */         return localDimension;
/*      */       }
/*      */     };
/*  241 */     this.lookInLabel.setDisplayedMnemonic(this.lookInLabelMnemonic);
/*  242 */     this.lookInLabel.setAlignmentX(0.0F);
/*  243 */     this.lookInLabel.setAlignmentY(0.5F);
/*  244 */     localJToolBar.add(this.lookInLabel);
/*  245 */     localJToolBar.add(Box.createRigidArea(new Dimension(8, 0)));
/*      */ 
/*  248 */     this.directoryComboBox = new JComboBox() {
/*      */       public Dimension getMinimumSize() {
/*  250 */         Dimension localDimension = super.getMinimumSize();
/*  251 */         localDimension.width = 60;
/*  252 */         return localDimension;
/*      */       }
/*      */ 
/*      */       public Dimension getPreferredSize() {
/*  256 */         Dimension localDimension = super.getPreferredSize();
/*      */ 
/*  258 */         localDimension.width = 150;
/*  259 */         return localDimension;
/*      */       }
/*      */     };
/*  262 */     this.directoryComboBox.putClientProperty("JComboBox.lightweightKeyboardNavigation", "Lightweight");
/*  263 */     this.lookInLabel.setLabelFor(this.directoryComboBox);
/*  264 */     this.directoryComboBoxModel = createDirectoryComboBoxModel(paramJFileChooser);
/*  265 */     this.directoryComboBox.setModel(this.directoryComboBoxModel);
/*  266 */     this.directoryComboBox.addActionListener(this.directoryComboBoxAction);
/*  267 */     this.directoryComboBox.setRenderer(createDirectoryComboBoxRenderer(paramJFileChooser));
/*  268 */     this.directoryComboBox.setAlignmentX(0.0F);
/*  269 */     this.directoryComboBox.setAlignmentY(0.5F);
/*  270 */     this.directoryComboBox.setMaximumRowCount(8);
/*      */ 
/*  272 */     localJToolBar.add(this.directoryComboBox);
/*  273 */     localJToolBar.add(Box.createRigidArea(hstrut10));
/*      */ 
/*  276 */     JButton localJButton1 = createToolButton(getChangeToParentDirectoryAction(), this.upFolderIcon, this.upFolderToolTipText, this.upFolderAccessibleName);
/*      */ 
/*  278 */     localJToolBar.add(localJButton1);
/*      */ 
/*  281 */     if (!UIManager.getBoolean("FileChooser.readOnly")) {
/*  282 */       localObject1 = createToolButton(this.filePane.getNewFolderAction(), this.newFolderIcon, this.newFolderToolTipText, this.newFolderAccessibleName);
/*      */ 
/*  284 */       localJToolBar.add((Component)localObject1);
/*      */     }
/*      */ 
/*  288 */     Object localObject1 = new ButtonGroup();
/*      */ 
/*  291 */     final JPopupMenu localJPopupMenu = new JPopupMenu();
/*      */ 
/*  293 */     final JRadioButtonMenuItem localJRadioButtonMenuItem1 = new JRadioButtonMenuItem(this.filePane.getViewTypeAction(0));
/*      */ 
/*  295 */     localJRadioButtonMenuItem1.setSelected(this.filePane.getViewType() == 0);
/*  296 */     localJPopupMenu.add(localJRadioButtonMenuItem1);
/*  297 */     ((ButtonGroup)localObject1).add(localJRadioButtonMenuItem1);
/*      */ 
/*  299 */     final JRadioButtonMenuItem localJRadioButtonMenuItem2 = new JRadioButtonMenuItem(this.filePane.getViewTypeAction(1));
/*      */ 
/*  301 */     localJRadioButtonMenuItem2.setSelected(this.filePane.getViewType() == 1);
/*  302 */     localJPopupMenu.add(localJRadioButtonMenuItem2);
/*  303 */     ((ButtonGroup)localObject1).add(localJRadioButtonMenuItem2);
/*      */ 
/*  306 */     BufferedImage localBufferedImage = new BufferedImage(this.viewMenuIcon.getIconWidth() + 7, this.viewMenuIcon.getIconHeight(), 2);
/*      */ 
/*  308 */     Graphics localGraphics = localBufferedImage.getGraphics();
/*  309 */     this.viewMenuIcon.paintIcon(this.filePane, localGraphics, 0, 0);
/*  310 */     int i = localBufferedImage.getWidth() - 5;
/*  311 */     int j = localBufferedImage.getHeight() / 2 - 1;
/*  312 */     localGraphics.setColor(Color.BLACK);
/*  313 */     localGraphics.fillPolygon(new int[] { i, i + 5, i + 2 }, new int[] { j, j, j + 3 }, 3);
/*      */ 
/*  316 */     final JButton localJButton2 = createToolButton(null, new ImageIcon(localBufferedImage), this.viewMenuButtonToolTipText, this.viewMenuButtonAccessibleName);
/*      */ 
/*  319 */     localJButton2.addMouseListener(new MouseAdapter() {
/*      */       public void mousePressed(MouseEvent paramAnonymousMouseEvent) {
/*  321 */         if ((SwingUtilities.isLeftMouseButton(paramAnonymousMouseEvent)) && (!localJButton2.isSelected())) {
/*  322 */           localJButton2.setSelected(true);
/*      */ 
/*  324 */           localJPopupMenu.show(localJButton2, 0, localJButton2.getHeight());
/*      */         }
/*      */       }
/*      */     });
/*  328 */     localJButton2.addKeyListener(new KeyAdapter()
/*      */     {
/*      */       public void keyPressed(KeyEvent paramAnonymousKeyEvent) {
/*  331 */         if ((paramAnonymousKeyEvent.getKeyCode() == 32) && (localJButton2.getModel().isRollover())) {
/*  332 */           localJButton2.setSelected(true);
/*      */ 
/*  334 */           localJPopupMenu.show(localJButton2, 0, localJButton2.getHeight());
/*      */         }
/*      */       }
/*      */     });
/*  338 */     localJPopupMenu.addPopupMenuListener(new PopupMenuListener() {
/*      */       public void popupMenuWillBecomeVisible(PopupMenuEvent paramAnonymousPopupMenuEvent) {
/*      */       }
/*      */ 
/*      */       public void popupMenuWillBecomeInvisible(PopupMenuEvent paramAnonymousPopupMenuEvent) {
/*  343 */         SwingUtilities.invokeLater(new Runnable() {
/*      */           public void run() {
/*  345 */             WindowsFileChooserUI.5.this.val$viewMenuButton.setSelected(false);
/*      */           }
/*      */         });
/*      */       }
/*      */ 
/*      */       public void popupMenuCanceled(PopupMenuEvent paramAnonymousPopupMenuEvent)
/*      */       {
/*      */       }
/*      */     });
/*  354 */     localJToolBar.add(localJButton2);
/*      */ 
/*  356 */     localJToolBar.add(Box.createRigidArea(new Dimension(80, 0)));
/*      */ 
/*  358 */     this.filePane.addPropertyChangeListener(new PropertyChangeListener() {
/*      */       public void propertyChange(PropertyChangeEvent paramAnonymousPropertyChangeEvent) {
/*  360 */         if ("viewType".equals(paramAnonymousPropertyChangeEvent.getPropertyName()))
/*  361 */           switch (WindowsFileChooserUI.this.filePane.getViewType()) {
/*      */           case 0:
/*  363 */             localJRadioButtonMenuItem1.setSelected(true);
/*  364 */             break;
/*      */           case 1:
/*  367 */             localJRadioButtonMenuItem2.setSelected(true);
/*      */           }
/*      */       }
/*      */     });
/*  377 */     this.centerPanel = new JPanel(new BorderLayout());
/*  378 */     this.centerPanel.add(getAccessoryPanel(), "After");
/*  379 */     JComponent localJComponent = paramJFileChooser.getAccessory();
/*  380 */     if (localJComponent != null) {
/*  381 */       getAccessoryPanel().add(localJComponent);
/*      */     }
/*  383 */     this.filePane.setPreferredSize(LIST_PREF_SIZE);
/*  384 */     this.centerPanel.add(this.filePane, "Center");
/*  385 */     paramJFileChooser.add(this.centerPanel, "Center");
/*      */ 
/*  390 */     getBottomPanel().setLayout(new BoxLayout(getBottomPanel(), 2));
/*      */ 
/*  393 */     this.centerPanel.add(getBottomPanel(), "South");
/*      */ 
/*  396 */     JPanel localJPanel1 = new JPanel();
/*  397 */     localJPanel1.setLayout(new BoxLayout(localJPanel1, 3));
/*  398 */     localJPanel1.add(Box.createRigidArea(vstrut4));
/*      */ 
/*  400 */     this.fileNameLabel = new JLabel();
/*  401 */     populateFileNameLabel();
/*  402 */     this.fileNameLabel.setAlignmentY(0.0F);
/*  403 */     localJPanel1.add(this.fileNameLabel);
/*      */ 
/*  405 */     localJPanel1.add(Box.createRigidArea(new Dimension(1, 12)));
/*      */ 
/*  407 */     JLabel localJLabel = new JLabel(this.filesOfTypeLabelText);
/*  408 */     localJLabel.setDisplayedMnemonic(this.filesOfTypeLabelMnemonic);
/*  409 */     localJPanel1.add(localJLabel);
/*      */ 
/*  411 */     getBottomPanel().add(localJPanel1);
/*  412 */     getBottomPanel().add(Box.createRigidArea(new Dimension(15, 0)));
/*      */ 
/*  415 */     JPanel localJPanel2 = new JPanel();
/*  416 */     localJPanel2.add(Box.createRigidArea(vstrut8));
/*  417 */     localJPanel2.setLayout(new BoxLayout(localJPanel2, 1));
/*      */ 
/*  420 */     this.filenameTextField = new JTextField(35) {
/*      */       public Dimension getMaximumSize() {
/*  422 */         return new Dimension(32767, super.getPreferredSize().height);
/*      */       }
/*      */     };
/*  426 */     this.fileNameLabel.setLabelFor(this.filenameTextField);
/*  427 */     this.filenameTextField.addFocusListener(new FocusAdapter()
/*      */     {
/*      */       public void focusGained(FocusEvent paramAnonymousFocusEvent) {
/*  430 */         if (!WindowsFileChooserUI.this.getFileChooser().isMultiSelectionEnabled())
/*  431 */           WindowsFileChooserUI.this.filePane.clearSelection();
/*      */       }
/*      */     });
/*  437 */     if (paramJFileChooser.isMultiSelectionEnabled())
/*  438 */       setFileName(fileNameString(paramJFileChooser.getSelectedFiles()));
/*      */     else {
/*  440 */       setFileName(fileNameString(paramJFileChooser.getSelectedFile()));
/*      */     }
/*      */ 
/*  443 */     localJPanel2.add(this.filenameTextField);
/*  444 */     localJPanel2.add(Box.createRigidArea(vstrut8));
/*      */ 
/*  446 */     this.filterComboBoxModel = createFilterComboBoxModel();
/*  447 */     paramJFileChooser.addPropertyChangeListener(this.filterComboBoxModel);
/*  448 */     this.filterComboBox = new JComboBox(this.filterComboBoxModel);
/*  449 */     localJLabel.setLabelFor(this.filterComboBox);
/*  450 */     this.filterComboBox.setRenderer(createFilterComboBoxRenderer());
/*  451 */     localJPanel2.add(this.filterComboBox);
/*      */ 
/*  453 */     getBottomPanel().add(localJPanel2);
/*  454 */     getBottomPanel().add(Box.createRigidArea(new Dimension(30, 0)));
/*      */ 
/*  457 */     getButtonPanel().setLayout(new BoxLayout(getButtonPanel(), 1));
/*      */ 
/*  459 */     this.approveButton = new JButton(getApproveButtonText(paramJFileChooser)) {
/*      */       public Dimension getMaximumSize() {
/*  461 */         return WindowsFileChooserUI.this.approveButton.getPreferredSize().width > WindowsFileChooserUI.this.cancelButton.getPreferredSize().width ? WindowsFileChooserUI.this.approveButton.getPreferredSize() : WindowsFileChooserUI.this.cancelButton.getPreferredSize();
/*      */       }
/*      */     };
/*  465 */     Object localObject2 = this.approveButton.getMargin();
/*  466 */     localObject2 = new InsetsUIResource(((Insets)localObject2).top, ((Insets)localObject2).left + 5, ((Insets)localObject2).bottom, ((Insets)localObject2).right + 5);
/*      */ 
/*  468 */     this.approveButton.setMargin((Insets)localObject2);
/*  469 */     this.approveButton.setMnemonic(getApproveButtonMnemonic(paramJFileChooser));
/*  470 */     this.approveButton.addActionListener(getApproveSelectionAction());
/*  471 */     this.approveButton.setToolTipText(getApproveButtonToolTipText(paramJFileChooser));
/*  472 */     getButtonPanel().add(Box.createRigidArea(vstrut6));
/*  473 */     getButtonPanel().add(this.approveButton);
/*  474 */     getButtonPanel().add(Box.createRigidArea(vstrut4));
/*      */ 
/*  476 */     this.cancelButton = new JButton(this.cancelButtonText) {
/*      */       public Dimension getMaximumSize() {
/*  478 */         return WindowsFileChooserUI.this.approveButton.getPreferredSize().width > WindowsFileChooserUI.this.cancelButton.getPreferredSize().width ? WindowsFileChooserUI.this.approveButton.getPreferredSize() : WindowsFileChooserUI.this.cancelButton.getPreferredSize();
/*      */       }
/*      */     };
/*  482 */     this.cancelButton.setMargin((Insets)localObject2);
/*  483 */     this.cancelButton.setToolTipText(this.cancelButtonToolTipText);
/*  484 */     this.cancelButton.addActionListener(getCancelSelectionAction());
/*  485 */     getButtonPanel().add(this.cancelButton);
/*      */ 
/*  487 */     if (paramJFileChooser.getControlButtonsAreShown())
/*  488 */       addControlButtons();
/*      */   }
/*      */ 
/*      */   private void updateUseShellFolder()
/*      */   {
/*  495 */     JFileChooser localJFileChooser = getFileChooser();
/*      */ 
/*  497 */     if (FilePane.usesShellFolder(localJFileChooser)) {
/*  498 */       if ((this.placesBar == null) && (!UIManager.getBoolean("FileChooser.noPlacesBar"))) {
/*  499 */         this.placesBar = new WindowsPlacesBar(localJFileChooser, XPStyle.getXP() != null);
/*  500 */         localJFileChooser.add(this.placesBar, "Before");
/*  501 */         localJFileChooser.addPropertyChangeListener(this.placesBar);
/*      */       }
/*      */     }
/*  504 */     else if (this.placesBar != null) {
/*  505 */       localJFileChooser.remove(this.placesBar);
/*  506 */       localJFileChooser.removePropertyChangeListener(this.placesBar);
/*  507 */       this.placesBar = null;
/*      */     }
/*      */   }
/*      */ 
/*      */   protected JPanel getButtonPanel()
/*      */   {
/*  513 */     if (this.buttonPanel == null) {
/*  514 */       this.buttonPanel = new JPanel();
/*      */     }
/*  516 */     return this.buttonPanel;
/*      */   }
/*      */ 
/*      */   protected JPanel getBottomPanel() {
/*  520 */     if (this.bottomPanel == null) {
/*  521 */       this.bottomPanel = new JPanel();
/*      */     }
/*  523 */     return this.bottomPanel;
/*      */   }
/*      */ 
/*      */   protected void installStrings(JFileChooser paramJFileChooser) {
/*  527 */     super.installStrings(paramJFileChooser);
/*      */ 
/*  529 */     Locale localLocale = paramJFileChooser.getLocale();
/*      */ 
/*  531 */     this.lookInLabelMnemonic = getMnemonic("FileChooser.lookInLabelMnemonic", localLocale).intValue();
/*  532 */     this.lookInLabelText = UIManager.getString("FileChooser.lookInLabelText", localLocale);
/*  533 */     this.saveInLabelText = UIManager.getString("FileChooser.saveInLabelText", localLocale);
/*      */ 
/*  535 */     this.fileNameLabelMnemonic = getMnemonic("FileChooser.fileNameLabelMnemonic", localLocale).intValue();
/*  536 */     this.fileNameLabelText = UIManager.getString("FileChooser.fileNameLabelText", localLocale);
/*  537 */     this.folderNameLabelMnemonic = getMnemonic("FileChooser.folderNameLabelMnemonic", localLocale).intValue();
/*  538 */     this.folderNameLabelText = UIManager.getString("FileChooser.folderNameLabelText", localLocale);
/*      */ 
/*  540 */     this.filesOfTypeLabelMnemonic = getMnemonic("FileChooser.filesOfTypeLabelMnemonic", localLocale).intValue();
/*  541 */     this.filesOfTypeLabelText = UIManager.getString("FileChooser.filesOfTypeLabelText", localLocale);
/*      */ 
/*  543 */     this.upFolderToolTipText = UIManager.getString("FileChooser.upFolderToolTipText", localLocale);
/*  544 */     this.upFolderAccessibleName = UIManager.getString("FileChooser.upFolderAccessibleName", localLocale);
/*      */ 
/*  546 */     this.newFolderToolTipText = UIManager.getString("FileChooser.newFolderToolTipText", localLocale);
/*  547 */     this.newFolderAccessibleName = UIManager.getString("FileChooser.newFolderAccessibleName", localLocale);
/*      */ 
/*  549 */     this.viewMenuButtonToolTipText = UIManager.getString("FileChooser.viewMenuButtonToolTipText", localLocale);
/*  550 */     this.viewMenuButtonAccessibleName = UIManager.getString("FileChooser.viewMenuButtonAccessibleName", localLocale);
/*      */   }
/*      */ 
/*      */   private Integer getMnemonic(String paramString, Locale paramLocale) {
/*  554 */     return Integer.valueOf(SwingUtilities2.getUIDefaultsInt(paramString, paramLocale));
/*      */   }
/*      */ 
/*      */   protected void installListeners(JFileChooser paramJFileChooser) {
/*  558 */     super.installListeners(paramJFileChooser);
/*  559 */     ActionMap localActionMap = getActionMap();
/*  560 */     SwingUtilities.replaceUIActionMap(paramJFileChooser, localActionMap);
/*      */   }
/*      */ 
/*      */   protected ActionMap getActionMap() {
/*  564 */     return createActionMap();
/*      */   }
/*      */ 
/*      */   protected ActionMap createActionMap() {
/*  568 */     ActionMapUIResource localActionMapUIResource = new ActionMapUIResource();
/*  569 */     FilePane.addActionsToMap(localActionMapUIResource, this.filePane.getActions());
/*  570 */     return localActionMapUIResource;
/*      */   }
/*      */ 
/*      */   protected JPanel createList(JFileChooser paramJFileChooser) {
/*  574 */     return this.filePane.createList();
/*      */   }
/*      */ 
/*      */   protected JPanel createDetailsView(JFileChooser paramJFileChooser) {
/*  578 */     return this.filePane.createDetailsView();
/*      */   }
/*      */ 
/*      */   public ListSelectionListener createListSelectionListener(JFileChooser paramJFileChooser)
/*      */   {
/*  588 */     return super.createListSelectionListener(paramJFileChooser);
/*      */   }
/*      */ 
/*      */   public void uninstallUI(JComponent paramJComponent)
/*      */   {
/*  605 */     paramJComponent.removePropertyChangeListener(this.filterComboBoxModel);
/*  606 */     paramJComponent.removePropertyChangeListener(this.filePane);
/*  607 */     if (this.placesBar != null) {
/*  608 */       paramJComponent.removePropertyChangeListener(this.placesBar);
/*      */     }
/*  610 */     this.cancelButton.removeActionListener(getCancelSelectionAction());
/*  611 */     this.approveButton.removeActionListener(getApproveSelectionAction());
/*  612 */     this.filenameTextField.removeActionListener(getApproveSelectionAction());
/*      */ 
/*  614 */     if (this.filePane != null) {
/*  615 */       this.filePane.uninstallUI();
/*  616 */       this.filePane = null;
/*      */     }
/*      */ 
/*  619 */     super.uninstallUI(paramJComponent);
/*      */   }
/*      */ 
/*      */   public Dimension getPreferredSize(JComponent paramJComponent)
/*      */   {
/*  635 */     int i = PREF_SIZE.width;
/*  636 */     Dimension localDimension = paramJComponent.getLayout().preferredLayoutSize(paramJComponent);
/*  637 */     if (localDimension != null) {
/*  638 */       return new Dimension(localDimension.width < i ? i : localDimension.width, localDimension.height < PREF_SIZE.height ? PREF_SIZE.height : localDimension.height);
/*      */     }
/*      */ 
/*  641 */     return new Dimension(i, PREF_SIZE.height);
/*      */   }
/*      */ 
/*      */   public Dimension getMinimumSize(JComponent paramJComponent)
/*      */   {
/*  653 */     return MIN_SIZE;
/*      */   }
/*      */ 
/*      */   public Dimension getMaximumSize(JComponent paramJComponent)
/*      */   {
/*  664 */     return new Dimension(2147483647, 2147483647);
/*      */   }
/*      */ 
/*      */   private String fileNameString(File paramFile) {
/*  668 */     if (paramFile == null) {
/*  669 */       return null;
/*      */     }
/*  671 */     JFileChooser localJFileChooser = getFileChooser();
/*  672 */     if (((localJFileChooser.isDirectorySelectionEnabled()) && (!localJFileChooser.isFileSelectionEnabled())) || ((localJFileChooser.isDirectorySelectionEnabled()) && (localJFileChooser.isFileSelectionEnabled()) && (localJFileChooser.getFileSystemView().isFileSystemRoot(paramFile))))
/*      */     {
/*  674 */       return paramFile.getPath();
/*      */     }
/*  676 */     return paramFile.getName();
/*      */   }
/*      */ 
/*      */   private String fileNameString(File[] paramArrayOfFile)
/*      */   {
/*  682 */     StringBuffer localStringBuffer = new StringBuffer();
/*  683 */     for (int i = 0; (paramArrayOfFile != null) && (i < paramArrayOfFile.length); i++) {
/*  684 */       if (i > 0) {
/*  685 */         localStringBuffer.append(" ");
/*      */       }
/*  687 */       if (paramArrayOfFile.length > 1) {
/*  688 */         localStringBuffer.append("\"");
/*      */       }
/*  690 */       localStringBuffer.append(fileNameString(paramArrayOfFile[i]));
/*  691 */       if (paramArrayOfFile.length > 1) {
/*  692 */         localStringBuffer.append("\"");
/*      */       }
/*      */     }
/*  695 */     return localStringBuffer.toString();
/*      */   }
/*      */ 
/*      */   private void doSelectedFileChanged(PropertyChangeEvent paramPropertyChangeEvent)
/*      */   {
/*  701 */     File localFile = (File)paramPropertyChangeEvent.getNewValue();
/*  702 */     JFileChooser localJFileChooser = getFileChooser();
/*  703 */     if ((localFile != null) && (((localJFileChooser.isFileSelectionEnabled()) && (!localFile.isDirectory())) || ((localFile.isDirectory()) && (localJFileChooser.isDirectorySelectionEnabled()))))
/*      */     {
/*  707 */       setFileName(fileNameString(localFile));
/*      */     }
/*      */   }
/*      */ 
/*      */   private void doSelectedFilesChanged(PropertyChangeEvent paramPropertyChangeEvent) {
/*  712 */     File[] arrayOfFile = (File[])paramPropertyChangeEvent.getNewValue();
/*  713 */     JFileChooser localJFileChooser = getFileChooser();
/*  714 */     if ((arrayOfFile != null) && (arrayOfFile.length > 0) && ((arrayOfFile.length > 1) || (localJFileChooser.isDirectorySelectionEnabled()) || (!arrayOfFile[0].isDirectory())))
/*      */     {
/*  717 */       setFileName(fileNameString(arrayOfFile));
/*      */     }
/*      */   }
/*      */ 
/*      */   private void doDirectoryChanged(PropertyChangeEvent paramPropertyChangeEvent) {
/*  722 */     JFileChooser localJFileChooser = getFileChooser();
/*  723 */     FileSystemView localFileSystemView = localJFileChooser.getFileSystemView();
/*      */ 
/*  725 */     clearIconCache();
/*  726 */     File localFile = localJFileChooser.getCurrentDirectory();
/*  727 */     if (localFile != null) {
/*  728 */       this.directoryComboBoxModel.addItem(localFile);
/*      */ 
/*  730 */       if ((localJFileChooser.isDirectorySelectionEnabled()) && (!localJFileChooser.isFileSelectionEnabled()))
/*  731 */         if (localFileSystemView.isFileSystem(localFile))
/*  732 */           setFileName(localFile.getPath());
/*      */         else
/*  734 */           setFileName(null);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void doFilterChanged(PropertyChangeEvent paramPropertyChangeEvent)
/*      */   {
/*  741 */     clearIconCache();
/*      */   }
/*      */ 
/*      */   private void doFileSelectionModeChanged(PropertyChangeEvent paramPropertyChangeEvent) {
/*  745 */     if (this.fileNameLabel != null) {
/*  746 */       populateFileNameLabel();
/*      */     }
/*  748 */     clearIconCache();
/*      */ 
/*  750 */     JFileChooser localJFileChooser = getFileChooser();
/*  751 */     File localFile = localJFileChooser.getCurrentDirectory();
/*  752 */     if ((localFile != null) && (localJFileChooser.isDirectorySelectionEnabled()) && (!localJFileChooser.isFileSelectionEnabled()) && (localJFileChooser.getFileSystemView().isFileSystem(localFile)))
/*      */     {
/*  757 */       setFileName(localFile.getPath());
/*      */     }
/*  759 */     else setFileName(null);
/*      */   }
/*      */ 
/*      */   private void doAccessoryChanged(PropertyChangeEvent paramPropertyChangeEvent)
/*      */   {
/*  764 */     if (getAccessoryPanel() != null) {
/*  765 */       if (paramPropertyChangeEvent.getOldValue() != null) {
/*  766 */         getAccessoryPanel().remove((JComponent)paramPropertyChangeEvent.getOldValue());
/*      */       }
/*  768 */       JComponent localJComponent = (JComponent)paramPropertyChangeEvent.getNewValue();
/*  769 */       if (localJComponent != null)
/*  770 */         getAccessoryPanel().add(localJComponent, "Center");
/*      */     }
/*      */   }
/*      */ 
/*      */   private void doApproveButtonTextChanged(PropertyChangeEvent paramPropertyChangeEvent)
/*      */   {
/*  776 */     JFileChooser localJFileChooser = getFileChooser();
/*  777 */     this.approveButton.setText(getApproveButtonText(localJFileChooser));
/*  778 */     this.approveButton.setToolTipText(getApproveButtonToolTipText(localJFileChooser));
/*  779 */     this.approveButton.setMnemonic(getApproveButtonMnemonic(localJFileChooser));
/*      */   }
/*      */ 
/*      */   private void doDialogTypeChanged(PropertyChangeEvent paramPropertyChangeEvent) {
/*  783 */     JFileChooser localJFileChooser = getFileChooser();
/*  784 */     this.approveButton.setText(getApproveButtonText(localJFileChooser));
/*  785 */     this.approveButton.setToolTipText(getApproveButtonToolTipText(localJFileChooser));
/*  786 */     this.approveButton.setMnemonic(getApproveButtonMnemonic(localJFileChooser));
/*  787 */     if (localJFileChooser.getDialogType() == 1)
/*  788 */       this.lookInLabel.setText(this.saveInLabelText);
/*      */     else
/*  790 */       this.lookInLabel.setText(this.lookInLabelText);
/*      */   }
/*      */ 
/*      */   private void doApproveButtonMnemonicChanged(PropertyChangeEvent paramPropertyChangeEvent)
/*      */   {
/*  795 */     this.approveButton.setMnemonic(getApproveButtonMnemonic(getFileChooser()));
/*      */   }
/*      */ 
/*      */   private void doControlButtonsChanged(PropertyChangeEvent paramPropertyChangeEvent) {
/*  799 */     if (getFileChooser().getControlButtonsAreShown())
/*  800 */       addControlButtons();
/*      */     else
/*  802 */       removeControlButtons();
/*      */   }
/*      */ 
/*      */   public PropertyChangeListener createPropertyChangeListener(JFileChooser paramJFileChooser)
/*      */   {
/*  811 */     return new PropertyChangeListener() {
/*      */       public void propertyChange(PropertyChangeEvent paramAnonymousPropertyChangeEvent) {
/*  813 */         String str = paramAnonymousPropertyChangeEvent.getPropertyName();
/*  814 */         if (str.equals("SelectedFileChangedProperty")) {
/*  815 */           WindowsFileChooserUI.this.doSelectedFileChanged(paramAnonymousPropertyChangeEvent);
/*  816 */         } else if (str.equals("SelectedFilesChangedProperty")) {
/*  817 */           WindowsFileChooserUI.this.doSelectedFilesChanged(paramAnonymousPropertyChangeEvent);
/*  818 */         } else if (str.equals("directoryChanged")) {
/*  819 */           WindowsFileChooserUI.this.doDirectoryChanged(paramAnonymousPropertyChangeEvent);
/*  820 */         } else if (str.equals("fileFilterChanged")) {
/*  821 */           WindowsFileChooserUI.this.doFilterChanged(paramAnonymousPropertyChangeEvent);
/*  822 */         } else if (str.equals("fileSelectionChanged")) {
/*  823 */           WindowsFileChooserUI.this.doFileSelectionModeChanged(paramAnonymousPropertyChangeEvent);
/*  824 */         } else if (str.equals("AccessoryChangedProperty")) {
/*  825 */           WindowsFileChooserUI.this.doAccessoryChanged(paramAnonymousPropertyChangeEvent);
/*  826 */         } else if ((str.equals("ApproveButtonTextChangedProperty")) || (str.equals("ApproveButtonToolTipTextChangedProperty")))
/*      */         {
/*  828 */           WindowsFileChooserUI.this.doApproveButtonTextChanged(paramAnonymousPropertyChangeEvent);
/*  829 */         } else if (str.equals("DialogTypeChangedProperty")) {
/*  830 */           WindowsFileChooserUI.this.doDialogTypeChanged(paramAnonymousPropertyChangeEvent);
/*  831 */         } else if (str.equals("ApproveButtonMnemonicChangedProperty")) {
/*  832 */           WindowsFileChooserUI.this.doApproveButtonMnemonicChanged(paramAnonymousPropertyChangeEvent);
/*  833 */         } else if (str.equals("ControlButtonsAreShownChangedProperty")) {
/*  834 */           WindowsFileChooserUI.this.doControlButtonsChanged(paramAnonymousPropertyChangeEvent);
/*  835 */         } else if (str == "FileChooser.useShellFolder") {
/*  836 */           WindowsFileChooserUI.this.updateUseShellFolder();
/*  837 */           WindowsFileChooserUI.this.doDirectoryChanged(paramAnonymousPropertyChangeEvent);
/*  838 */         } else if (str.equals("componentOrientation")) {
/*  839 */           ComponentOrientation localComponentOrientation = (ComponentOrientation)paramAnonymousPropertyChangeEvent.getNewValue();
/*  840 */           JFileChooser localJFileChooser = (JFileChooser)paramAnonymousPropertyChangeEvent.getSource();
/*  841 */           if (localComponentOrientation != paramAnonymousPropertyChangeEvent.getOldValue())
/*  842 */             localJFileChooser.applyComponentOrientation(localComponentOrientation);
/*      */         }
/*  844 */         else if ((str.equals("ancestor")) && 
/*  845 */           (paramAnonymousPropertyChangeEvent.getOldValue() == null) && (paramAnonymousPropertyChangeEvent.getNewValue() != null))
/*      */         {
/*  847 */           WindowsFileChooserUI.this.filenameTextField.selectAll();
/*  848 */           WindowsFileChooserUI.this.filenameTextField.requestFocus();
/*      */         }
/*      */       }
/*      */     };
/*      */   }
/*      */ 
/*      */   protected void removeControlButtons()
/*      */   {
/*  857 */     getBottomPanel().remove(getButtonPanel());
/*      */   }
/*      */ 
/*      */   protected void addControlButtons() {
/*  861 */     getBottomPanel().add(getButtonPanel());
/*      */   }
/*      */ 
/*      */   public void ensureFileIsVisible(JFileChooser paramJFileChooser, File paramFile) {
/*  865 */     this.filePane.ensureFileIsVisible(paramJFileChooser, paramFile);
/*      */   }
/*      */ 
/*      */   public void rescanCurrentDirectory(JFileChooser paramJFileChooser) {
/*  869 */     this.filePane.rescanCurrentDirectory();
/*      */   }
/*      */ 
/*      */   public String getFileName() {
/*  873 */     if (this.filenameTextField != null) {
/*  874 */       return this.filenameTextField.getText();
/*      */     }
/*  876 */     return null;
/*      */   }
/*      */ 
/*      */   public void setFileName(String paramString)
/*      */   {
/*  881 */     if (this.filenameTextField != null)
/*  882 */       this.filenameTextField.setText(paramString);
/*      */   }
/*      */ 
/*      */   protected void setDirectorySelected(boolean paramBoolean)
/*      */   {
/*  894 */     super.setDirectorySelected(paramBoolean);
/*  895 */     JFileChooser localJFileChooser = getFileChooser();
/*  896 */     if (paramBoolean) {
/*  897 */       this.approveButton.setText(this.directoryOpenButtonText);
/*  898 */       this.approveButton.setToolTipText(this.directoryOpenButtonToolTipText);
/*  899 */       this.approveButton.setMnemonic(this.directoryOpenButtonMnemonic);
/*      */     } else {
/*  901 */       this.approveButton.setText(getApproveButtonText(localJFileChooser));
/*  902 */       this.approveButton.setToolTipText(getApproveButtonToolTipText(localJFileChooser));
/*  903 */       this.approveButton.setMnemonic(getApproveButtonMnemonic(localJFileChooser));
/*      */     }
/*      */   }
/*      */ 
/*      */   public String getDirectoryName()
/*      */   {
/*  909 */     return null;
/*      */   }
/*      */ 
/*      */   public void setDirectoryName(String paramString)
/*      */   {
/*      */   }
/*      */ 
/*      */   protected DirectoryComboBoxRenderer createDirectoryComboBoxRenderer(JFileChooser paramJFileChooser) {
/*  917 */     return new DirectoryComboBoxRenderer();
/*      */   }
/*      */ 
/*      */   private static JButton createToolButton(Action paramAction, Icon paramIcon, String paramString1, String paramString2) {
/*  921 */     JButton localJButton = new JButton(paramAction);
/*      */ 
/*  923 */     localJButton.setText(null);
/*  924 */     localJButton.setIcon(paramIcon);
/*  925 */     localJButton.setToolTipText(paramString1);
/*  926 */     localJButton.setRequestFocusEnabled(false);
/*  927 */     localJButton.putClientProperty("AccessibleName", paramString2);
/*  928 */     localJButton.putClientProperty(WindowsLookAndFeel.HI_RES_DISABLED_ICON_CLIENT_KEY, Boolean.TRUE);
/*  929 */     localJButton.setAlignmentX(0.0F);
/*  930 */     localJButton.setAlignmentY(0.5F);
/*  931 */     localJButton.setMargin(shrinkwrap);
/*  932 */     localJButton.setFocusPainted(false);
/*      */ 
/*  934 */     localJButton.setModel(new DefaultButtonModel()
/*      */     {
/*      */       public void setPressed(boolean paramAnonymousBoolean) {
/*  937 */         if ((!paramAnonymousBoolean) || (isRollover()))
/*  938 */           super.setPressed(paramAnonymousBoolean);
/*      */       }
/*      */ 
/*      */       public void setRollover(boolean paramAnonymousBoolean)
/*      */       {
/*  943 */         if ((paramAnonymousBoolean) && (!isRollover()))
/*      */         {
/*  945 */           for (Component localComponent : this.val$result.getParent().getComponents()) {
/*  946 */             if (((localComponent instanceof JButton)) && (localComponent != this.val$result)) {
/*  947 */               ((JButton)localComponent).getModel().setRollover(false);
/*      */             }
/*      */           }
/*      */         }
/*      */ 
/*  952 */         super.setRollover(paramAnonymousBoolean);
/*      */       }
/*      */ 
/*      */       public void setSelected(boolean paramAnonymousBoolean) {
/*  956 */         super.setSelected(paramAnonymousBoolean);
/*      */ 
/*  958 */         if (paramAnonymousBoolean)
/*  959 */           this.stateMask |= 5;
/*      */         else
/*  961 */           this.stateMask &= -6;
/*      */       }
/*      */     });
/*  966 */     localJButton.addFocusListener(new FocusAdapter() {
/*      */       public void focusGained(FocusEvent paramAnonymousFocusEvent) {
/*  968 */         this.val$result.getModel().setRollover(true);
/*      */       }
/*      */ 
/*      */       public void focusLost(FocusEvent paramAnonymousFocusEvent) {
/*  972 */         this.val$result.getModel().setRollover(false);
/*      */       }
/*      */     });
/*  976 */     return localJButton;
/*      */   }
/*      */ 
/*      */   protected DirectoryComboBoxModel createDirectoryComboBoxModel(JFileChooser paramJFileChooser)
/*      */   {
/* 1033 */     return new DirectoryComboBoxModel();
/*      */   }
/*      */ 
/*      */   protected FilterComboBoxRenderer createFilterComboBoxRenderer()
/*      */   {
/* 1158 */     return new FilterComboBoxRenderer();
/*      */   }
/*      */ 
/*      */   protected FilterComboBoxModel createFilterComboBoxModel()
/*      */   {
/* 1183 */     return new FilterComboBoxModel();
/*      */   }
/*      */ 
/*      */   public void valueChanged(ListSelectionEvent paramListSelectionEvent)
/*      */   {
/* 1256 */     JFileChooser localJFileChooser = getFileChooser();
/* 1257 */     File localFile = localJFileChooser.getSelectedFile();
/* 1258 */     if ((!paramListSelectionEvent.getValueIsAdjusting()) && (localFile != null) && (!getFileChooser().isTraversable(localFile)))
/* 1259 */       setFileName(fileNameString(localFile));
/*      */   }
/*      */ 
/*      */   protected JButton getApproveButton(JFileChooser paramJFileChooser)
/*      */   {
/* 1278 */     return this.approveButton;
/*      */   }
/*      */ 
/*      */   public FileView getFileView(JFileChooser paramJFileChooser) {
/* 1282 */     return this.fileView;
/*      */   }
/*      */ 
/*      */   protected class DirectoryComboBoxAction
/*      */     implements ActionListener
/*      */   {
/*      */     protected DirectoryComboBoxAction()
/*      */     {
/*      */     }
/*      */ 
/*      */     public void actionPerformed(ActionEvent paramActionEvent)
/*      */     {
/* 1272 */       File localFile = (File)WindowsFileChooserUI.this.directoryComboBox.getSelectedItem();
/* 1273 */       WindowsFileChooserUI.this.getFileChooser().setCurrentDirectory(localFile);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected class DirectoryComboBoxModel extends AbstractListModel
/*      */     implements ComboBoxModel
/*      */   {
/* 1040 */     Vector<File> directories = new Vector();
/* 1041 */     int[] depths = null;
/* 1042 */     File selectedDirectory = null;
/* 1043 */     JFileChooser chooser = WindowsFileChooserUI.this.getFileChooser();
/* 1044 */     FileSystemView fsv = this.chooser.getFileSystemView();
/*      */ 
/*      */     public DirectoryComboBoxModel()
/*      */     {
/* 1049 */       File localFile = WindowsFileChooserUI.this.getFileChooser().getCurrentDirectory();
/* 1050 */       if (localFile != null)
/* 1051 */         addItem(localFile);
/*      */     }
/*      */ 
/*      */     private void addItem(File paramFile)
/*      */     {
/* 1062 */       if (paramFile == null) {
/* 1063 */         return;
/*      */       }
/*      */ 
/* 1066 */       boolean bool = FilePane.usesShellFolder(this.chooser);
/*      */ 
/* 1068 */       this.directories.clear();
/*      */ 
/* 1070 */       File[] arrayOfFile = bool ? (File[])ShellFolder.get("fileChooserComboBoxFolders") : this.fsv.getRoots();
/*      */ 
/* 1073 */       this.directories.addAll(Arrays.asList(arrayOfFile));
/*      */       File localFile1;
/*      */       try
/*      */       {
/* 1080 */         localFile1 = paramFile.getCanonicalFile();
/*      */       }
/*      */       catch (IOException localIOException) {
/* 1083 */         localFile1 = paramFile;
/*      */       }
/*      */ 
/*      */       try
/*      */       {
/* 1088 */         File localFile2 = bool ? ShellFolder.getShellFolder(localFile1) : localFile1;
/*      */ 
/* 1090 */         File localFile3 = localFile2;
/* 1091 */         Vector localVector = new Vector(10);
/*      */         do
/* 1093 */           localVector.addElement(localFile3);
/* 1094 */         while ((localFile3 = localFile3.getParentFile()) != null);
/*      */ 
/* 1096 */         int i = localVector.size();
/*      */ 
/* 1098 */         for (int j = 0; j < i; j++) {
/* 1099 */           localFile3 = (File)localVector.get(j);
/* 1100 */           if (this.directories.contains(localFile3)) {
/* 1101 */             int k = this.directories.indexOf(localFile3);
/* 1102 */             for (int m = j - 1; m >= 0; m--) {
/* 1103 */               this.directories.insertElementAt(localVector.get(m), k + j - m);
/*      */             }
/* 1105 */             break;
/*      */           }
/*      */         }
/* 1108 */         calculateDepths();
/* 1109 */         setSelectedItem(localFile2);
/*      */       } catch (FileNotFoundException localFileNotFoundException) {
/* 1111 */         calculateDepths();
/*      */       }
/*      */     }
/*      */ 
/*      */     private void calculateDepths() {
/* 1116 */       this.depths = new int[this.directories.size()];
/* 1117 */       for (int i = 0; i < this.depths.length; i++) {
/* 1118 */         File localFile1 = (File)this.directories.get(i);
/* 1119 */         File localFile2 = localFile1.getParentFile();
/* 1120 */         this.depths[i] = 0;
/* 1121 */         if (localFile2 != null)
/* 1122 */           for (int j = i - 1; j >= 0; j--)
/* 1123 */             if (localFile2.equals(this.directories.get(j))) {
/* 1124 */               this.depths[j] += 1;
/* 1125 */               break;
/*      */             }
/*      */       }
/*      */     }
/*      */ 
/*      */     public int getDepth(int paramInt)
/*      */     {
/* 1133 */       return (this.depths != null) && (paramInt >= 0) && (paramInt < this.depths.length) ? this.depths[paramInt] : 0;
/*      */     }
/*      */ 
/*      */     public void setSelectedItem(Object paramObject) {
/* 1137 */       this.selectedDirectory = ((File)paramObject);
/* 1138 */       fireContentsChanged(this, -1, -1);
/*      */     }
/*      */ 
/*      */     public Object getSelectedItem() {
/* 1142 */       return this.selectedDirectory;
/*      */     }
/*      */ 
/*      */     public int getSize() {
/* 1146 */       return this.directories.size();
/*      */     }
/*      */ 
/*      */     public Object getElementAt(int paramInt) {
/* 1150 */       return this.directories.elementAt(paramInt);
/*      */     }
/*      */   }
/*      */ 
/*      */   class DirectoryComboBoxRenderer extends DefaultListCellRenderer
/*      */   {
/*  983 */     WindowsFileChooserUI.IndentIcon ii = new WindowsFileChooserUI.IndentIcon(WindowsFileChooserUI.this);
/*      */ 
/*      */     DirectoryComboBoxRenderer() {
/*      */     }
/*      */     public Component getListCellRendererComponent(JList paramJList, Object paramObject, int paramInt, boolean paramBoolean1, boolean paramBoolean2) {
/*  988 */       super.getListCellRendererComponent(paramJList, paramObject, paramInt, paramBoolean1, paramBoolean2);
/*      */ 
/*  990 */       if (paramObject == null) {
/*  991 */         setText("");
/*  992 */         return this;
/*      */       }
/*  994 */       File localFile = (File)paramObject;
/*  995 */       setText(WindowsFileChooserUI.this.getFileChooser().getName(localFile));
/*  996 */       Icon localIcon = WindowsFileChooserUI.this.getFileChooser().getIcon(localFile);
/*  997 */       this.ii.icon = localIcon;
/*  998 */       this.ii.depth = WindowsFileChooserUI.this.directoryComboBoxModel.getDepth(paramInt);
/*  999 */       setIcon(this.ii);
/*      */ 
/* 1001 */       return this;
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
/*      */   protected class FilterComboBoxModel extends AbstractListModel
/*      */     implements ComboBoxModel, PropertyChangeListener
/*      */   {
/*      */     protected FileFilter[] filters;
/*      */ 
/*      */     protected FilterComboBoxModel()
/*      */     {
/* 1193 */       this.filters = WindowsFileChooserUI.this.getFileChooser().getChoosableFileFilters();
/*      */     }
/*      */ 
/*      */     public void propertyChange(PropertyChangeEvent paramPropertyChangeEvent) {
/* 1197 */       String str = paramPropertyChangeEvent.getPropertyName();
/* 1198 */       if (str == "ChoosableFileFilterChangedProperty") {
/* 1199 */         this.filters = ((FileFilter[])paramPropertyChangeEvent.getNewValue());
/* 1200 */         fireContentsChanged(this, -1, -1);
/* 1201 */       } else if (str == "fileFilterChanged") {
/* 1202 */         fireContentsChanged(this, -1, -1);
/*      */       }
/*      */     }
/*      */ 
/*      */     public void setSelectedItem(Object paramObject) {
/* 1207 */       if (paramObject != null) {
/* 1208 */         WindowsFileChooserUI.this.getFileChooser().setFileFilter((FileFilter)paramObject);
/* 1209 */         fireContentsChanged(this, -1, -1);
/*      */       }
/*      */     }
/*      */ 
/*      */     public Object getSelectedItem()
/*      */     {
/* 1219 */       FileFilter localFileFilter1 = WindowsFileChooserUI.this.getFileChooser().getFileFilter();
/* 1220 */       int i = 0;
/* 1221 */       if (localFileFilter1 != null) {
/* 1222 */         for (FileFilter localFileFilter2 : this.filters) {
/* 1223 */           if (localFileFilter2 == localFileFilter1) {
/* 1224 */             i = 1;
/*      */           }
/*      */         }
/* 1227 */         if (i == 0) {
/* 1228 */           WindowsFileChooserUI.this.getFileChooser().addChoosableFileFilter(localFileFilter1);
/*      */         }
/*      */       }
/* 1231 */       return WindowsFileChooserUI.this.getFileChooser().getFileFilter();
/*      */     }
/*      */ 
/*      */     public int getSize() {
/* 1235 */       if (this.filters != null) {
/* 1236 */         return this.filters.length;
/*      */       }
/* 1238 */       return 0;
/*      */     }
/*      */ 
/*      */     public Object getElementAt(int paramInt)
/*      */     {
/* 1243 */       if (paramInt > getSize() - 1)
/*      */       {
/* 1245 */         return WindowsFileChooserUI.this.getFileChooser().getFileFilter();
/*      */       }
/* 1247 */       if (this.filters != null) {
/* 1248 */         return this.filters[paramInt];
/*      */       }
/* 1250 */       return null;
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
/* 1169 */       super.getListCellRendererComponent(paramJList, paramObject, paramInt, paramBoolean1, paramBoolean2);
/*      */ 
/* 1171 */       if ((paramObject != null) && ((paramObject instanceof FileFilter))) {
/* 1172 */         setText(((FileFilter)paramObject).getDescription());
/*      */       }
/*      */ 
/* 1175 */       return this;
/*      */     }
/*      */   }
/*      */ 
/*      */   class IndentIcon
/*      */     implements Icon
/*      */   {
/* 1008 */     Icon icon = null;
/* 1009 */     int depth = 0;
/*      */ 
/*      */     IndentIcon() {  } 
/* 1012 */     public void paintIcon(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2) { if (paramComponent.getComponentOrientation().isLeftToRight())
/* 1013 */         this.icon.paintIcon(paramComponent, paramGraphics, paramInt1 + this.depth * 10, paramInt2);
/*      */       else
/* 1015 */         this.icon.paintIcon(paramComponent, paramGraphics, paramInt1, paramInt2);
/*      */     }
/*      */ 
/*      */     public int getIconWidth()
/*      */     {
/* 1020 */       return this.icon.getIconWidth() + this.depth * 10;
/*      */     }
/*      */ 
/*      */     public int getIconHeight() {
/* 1024 */       return this.icon.getIconHeight();
/*      */     }
/*      */   }
/*      */ 
/*      */   protected class SingleClickListener extends MouseAdapter
/*      */   {
/*      */     protected SingleClickListener()
/*      */     {
/*      */     }
/*      */   }
/*      */ 
/*      */   private class WindowsFileChooserUIAccessor
/*      */     implements FilePane.FileChooserUIAccessor
/*      */   {
/*      */     private WindowsFileChooserUIAccessor()
/*      */     {
/*      */     }
/*      */ 
/*      */     public JFileChooser getFileChooser()
/*      */     {
/*  159 */       return WindowsFileChooserUI.this.getFileChooser();
/*      */     }
/*      */ 
/*      */     public BasicDirectoryModel getModel() {
/*  163 */       return WindowsFileChooserUI.this.getModel();
/*      */     }
/*      */ 
/*      */     public JPanel createList() {
/*  167 */       return WindowsFileChooserUI.this.createList(getFileChooser());
/*      */     }
/*      */ 
/*      */     public JPanel createDetailsView() {
/*  171 */       return WindowsFileChooserUI.this.createDetailsView(getFileChooser());
/*      */     }
/*      */ 
/*      */     public boolean isDirectorySelected() {
/*  175 */       return WindowsFileChooserUI.this.isDirectorySelected();
/*      */     }
/*      */ 
/*      */     public File getDirectory() {
/*  179 */       return WindowsFileChooserUI.this.getDirectory();
/*      */     }
/*      */ 
/*      */     public Action getChangeToParentDirectoryAction() {
/*  183 */       return WindowsFileChooserUI.this.getChangeToParentDirectoryAction();
/*      */     }
/*      */ 
/*      */     public Action getApproveSelectionAction() {
/*  187 */       return WindowsFileChooserUI.this.getApproveSelectionAction();
/*      */     }
/*      */ 
/*      */     public Action getNewFolderAction() {
/*  191 */       return WindowsFileChooserUI.this.getNewFolderAction();
/*      */     }
/*      */ 
/*      */     public MouseListener createDoubleClickListener(JList paramJList) {
/*  195 */       return WindowsFileChooserUI.this.createDoubleClickListener(getFileChooser(), paramJList);
/*      */     }
/*      */ 
/*      */     public ListSelectionListener createListSelectionListener()
/*      */     {
/*  200 */       return WindowsFileChooserUI.this.createListSelectionListener(getFileChooser());
/*      */     }
/*      */   }
/*      */ 
/*      */   protected class WindowsFileView extends BasicFileChooserUI.BasicFileView
/*      */   {
/*      */     protected WindowsFileView()
/*      */     {
/* 1288 */       super();
/*      */     }
/*      */ 
/*      */     public Icon getIcon(File paramFile) {
/* 1292 */       Icon localIcon = getCachedIcon(paramFile);
/* 1293 */       if (localIcon != null) {
/* 1294 */         return localIcon;
/*      */       }
/* 1296 */       if (paramFile != null) {
/* 1297 */         localIcon = WindowsFileChooserUI.this.getFileChooser().getFileSystemView().getSystemIcon(paramFile);
/*      */       }
/* 1299 */       if (localIcon == null) {
/* 1300 */         localIcon = super.getIcon(paramFile);
/*      */       }
/* 1302 */       cacheIcon(paramFile, localIcon);
/* 1303 */       return localIcon;
/*      */     }
/*      */   }
/*      */ 
/*      */   protected class WindowsNewFolderAction extends BasicFileChooserUI.NewFolderAction
/*      */   {
/*      */     protected WindowsNewFolderAction()
/*      */     {
/*  592 */       super();
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.java.swing.plaf.windows.WindowsFileChooserUI
 * JD-Core Version:    0.6.2
 */