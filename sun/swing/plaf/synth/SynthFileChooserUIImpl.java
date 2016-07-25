/*      */ package sun.swing.plaf.synth;
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
/*      */ import java.awt.event.MouseListener;
/*      */ import java.beans.PropertyChangeEvent;
/*      */ import java.beans.PropertyChangeListener;
/*      */ import java.io.File;
/*      */ import java.io.FileNotFoundException;
/*      */ import java.io.IOException;
/*      */ import java.util.Arrays;
/*      */ import java.util.Locale;
/*      */ import java.util.Vector;
/*      */ import javax.accessibility.AccessibleContext;
/*      */ import javax.swing.AbstractAction;
/*      */ import javax.swing.AbstractListModel;
/*      */ import javax.swing.Action;
/*      */ import javax.swing.ActionMap;
/*      */ import javax.swing.Box;
/*      */ import javax.swing.BoxLayout;
/*      */ import javax.swing.ButtonGroup;
/*      */ import javax.swing.ComboBoxModel;
/*      */ import javax.swing.Icon;
/*      */ import javax.swing.JButton;
/*      */ import javax.swing.JComboBox;
/*      */ import javax.swing.JComponent;
/*      */ import javax.swing.JFileChooser;
/*      */ import javax.swing.JLabel;
/*      */ import javax.swing.JList;
/*      */ import javax.swing.JPanel;
/*      */ import javax.swing.JPopupMenu;
/*      */ import javax.swing.JTextField;
/*      */ import javax.swing.JToggleButton;
/*      */ import javax.swing.ListCellRenderer;
/*      */ import javax.swing.UIManager;
/*      */ import javax.swing.event.ListSelectionListener;
/*      */ import javax.swing.filechooser.FileFilter;
/*      */ import javax.swing.filechooser.FileSystemView;
/*      */ import javax.swing.filechooser.FileView;
/*      */ import javax.swing.plaf.ActionMapUIResource;
/*      */ import javax.swing.plaf.basic.BasicDirectoryModel;
/*      */ import javax.swing.plaf.synth.SynthContext;
/*      */ import sun.awt.shell.ShellFolder;
/*      */ import sun.swing.FilePane;
/*      */ import sun.swing.FilePane.FileChooserUIAccessor;
/*      */ import sun.swing.SwingUtilities2;
/*      */ 
/*      */ public class SynthFileChooserUIImpl extends SynthFileChooserUI
/*      */ {
/*      */   private JLabel lookInLabel;
/*      */   private JComboBox directoryComboBox;
/*      */   private DirectoryComboBoxModel directoryComboBoxModel;
/*   65 */   private Action directoryComboBoxAction = new DirectoryComboBoxAction();
/*      */   private FilterComboBoxModel filterComboBoxModel;
/*      */   private JTextField fileNameTextField;
/*      */   private FilePane filePane;
/*      */   private JToggleButton listViewButton;
/*      */   private JToggleButton detailsViewButton;
/*      */   private boolean readOnly;
/*      */   private JPanel buttonPanel;
/*      */   private JPanel bottomPanel;
/*      */   private JComboBox filterComboBox;
/*   82 */   private static final Dimension hstrut5 = new Dimension(5, 1);
/*   83 */   private static final Dimension vstrut5 = new Dimension(1, 5);
/*      */ 
/*   85 */   private static final Insets shrinkwrap = new Insets(0, 0, 0, 0);
/*      */ 
/*   88 */   private static Dimension LIST_PREF_SIZE = new Dimension(405, 135);
/*      */ 
/*   91 */   private int lookInLabelMnemonic = 0;
/*   92 */   private String lookInLabelText = null;
/*   93 */   private String saveInLabelText = null;
/*      */ 
/*   95 */   private int fileNameLabelMnemonic = 0;
/*   96 */   private String fileNameLabelText = null;
/*   97 */   private int folderNameLabelMnemonic = 0;
/*   98 */   private String folderNameLabelText = null;
/*      */ 
/*  100 */   private int filesOfTypeLabelMnemonic = 0;
/*  101 */   private String filesOfTypeLabelText = null;
/*      */ 
/*  103 */   private String upFolderToolTipText = null;
/*  104 */   private String upFolderAccessibleName = null;
/*      */ 
/*  106 */   private String homeFolderToolTipText = null;
/*  107 */   private String homeFolderAccessibleName = null;
/*      */ 
/*  109 */   private String newFolderToolTipText = null;
/*  110 */   private String newFolderAccessibleName = null;
/*      */ 
/*  112 */   private String listViewButtonToolTipText = null;
/*  113 */   private String listViewButtonAccessibleName = null;
/*      */ 
/*  115 */   private String detailsViewButtonToolTipText = null;
/*  116 */   private String detailsViewButtonAccessibleName = null;
/*      */   private AlignedLabel fileNameLabel;
/*  119 */   private final PropertyChangeListener modeListener = new PropertyChangeListener() {
/*      */     public void propertyChange(PropertyChangeEvent paramAnonymousPropertyChangeEvent) {
/*  121 */       if (SynthFileChooserUIImpl.this.fileNameLabel != null)
/*  122 */         SynthFileChooserUIImpl.this.populateFileNameLabel();
/*      */     } } ;
/*      */   static final int space = 10;
/*      */ 
/*      */   private void populateFileNameLabel() {
/*  128 */     if (getFileChooser().getFileSelectionMode() == 1) {
/*  129 */       this.fileNameLabel.setText(this.folderNameLabelText);
/*  130 */       this.fileNameLabel.setDisplayedMnemonic(this.folderNameLabelMnemonic);
/*      */     } else {
/*  132 */       this.fileNameLabel.setText(this.fileNameLabelText);
/*  133 */       this.fileNameLabel.setDisplayedMnemonic(this.fileNameLabelMnemonic);
/*      */     }
/*      */   }
/*      */ 
/*      */   public SynthFileChooserUIImpl(JFileChooser paramJFileChooser) {
/*  138 */     super(paramJFileChooser);
/*      */   }
/*      */ 
/*      */   protected void installDefaults(JFileChooser paramJFileChooser)
/*      */   {
/*  190 */     super.installDefaults(paramJFileChooser);
/*  191 */     this.readOnly = UIManager.getBoolean("FileChooser.readOnly");
/*      */   }
/*      */ 
/*      */   public void installComponents(JFileChooser paramJFileChooser) {
/*  195 */     super.installComponents(paramJFileChooser);
/*      */ 
/*  197 */     SynthContext localSynthContext = getContext(paramJFileChooser, 1);
/*      */ 
/*  199 */     paramJFileChooser.setLayout(new BorderLayout(0, 11));
/*      */ 
/*  206 */     JPanel localJPanel1 = new JPanel(new BorderLayout(11, 0));
/*  207 */     JPanel localJPanel2 = new JPanel();
/*  208 */     localJPanel2.setLayout(new BoxLayout(localJPanel2, 2));
/*  209 */     localJPanel1.add(localJPanel2, "After");
/*      */ 
/*  212 */     paramJFileChooser.add(localJPanel1, "North");
/*      */ 
/*  215 */     this.lookInLabel = new JLabel(this.lookInLabelText);
/*  216 */     this.lookInLabel.setDisplayedMnemonic(this.lookInLabelMnemonic);
/*  217 */     localJPanel1.add(this.lookInLabel, "Before");
/*      */ 
/*  220 */     this.directoryComboBox = new JComboBox();
/*  221 */     this.directoryComboBox.getAccessibleContext().setAccessibleDescription(this.lookInLabelText);
/*  222 */     this.directoryComboBox.putClientProperty("JComboBox.isTableCellEditor", Boolean.TRUE);
/*  223 */     this.lookInLabel.setLabelFor(this.directoryComboBox);
/*  224 */     this.directoryComboBoxModel = createDirectoryComboBoxModel(paramJFileChooser);
/*  225 */     this.directoryComboBox.setModel(this.directoryComboBoxModel);
/*  226 */     this.directoryComboBox.addActionListener(this.directoryComboBoxAction);
/*  227 */     this.directoryComboBox.setRenderer(createDirectoryComboBoxRenderer(paramJFileChooser));
/*  228 */     this.directoryComboBox.setAlignmentX(0.0F);
/*  229 */     this.directoryComboBox.setAlignmentY(0.0F);
/*  230 */     this.directoryComboBox.setMaximumRowCount(8);
/*  231 */     localJPanel1.add(this.directoryComboBox, "Center");
/*      */ 
/*  233 */     this.filePane = new FilePane(new SynthFileChooserUIAccessor(null));
/*  234 */     paramJFileChooser.addPropertyChangeListener(this.filePane);
/*      */ 
/*  237 */     JPopupMenu localJPopupMenu = this.filePane.getComponentPopupMenu();
/*  238 */     if (localJPopupMenu != null) {
/*  239 */       localJPopupMenu.insert(getChangeToParentDirectoryAction(), 0);
/*  240 */       if (File.separatorChar == '/') {
/*  241 */         localJPopupMenu.insert(getGoHomeAction(), 1);
/*      */       }
/*      */     }
/*      */ 
/*  245 */     FileSystemView localFileSystemView = paramJFileChooser.getFileSystemView();
/*      */ 
/*  248 */     JButton localJButton1 = new JButton(getChangeToParentDirectoryAction());
/*  249 */     localJButton1.setText(null);
/*  250 */     localJButton1.setIcon(this.upFolderIcon);
/*  251 */     localJButton1.setToolTipText(this.upFolderToolTipText);
/*  252 */     localJButton1.getAccessibleContext().setAccessibleName(this.upFolderAccessibleName);
/*  253 */     localJButton1.setAlignmentX(0.0F);
/*  254 */     localJButton1.setAlignmentY(0.5F);
/*  255 */     localJButton1.setMargin(shrinkwrap);
/*      */ 
/*  257 */     localJPanel2.add(localJButton1);
/*  258 */     localJPanel2.add(Box.createRigidArea(hstrut5));
/*      */ 
/*  261 */     File localFile = localFileSystemView.getHomeDirectory();
/*  262 */     String str = this.homeFolderToolTipText;
/*  263 */     if (localFileSystemView.isRoot(localFile)) {
/*  264 */       str = getFileView(paramJFileChooser).getName(localFile);
/*      */     }
/*      */ 
/*  267 */     JButton localJButton2 = new JButton(this.homeFolderIcon);
/*  268 */     localJButton2.setToolTipText(str);
/*  269 */     localJButton2.getAccessibleContext().setAccessibleName(this.homeFolderAccessibleName);
/*  270 */     localJButton2.setAlignmentX(0.0F);
/*  271 */     localJButton2.setAlignmentY(0.5F);
/*  272 */     localJButton2.setMargin(shrinkwrap);
/*      */ 
/*  274 */     localJButton2.addActionListener(getGoHomeAction());
/*  275 */     localJPanel2.add(localJButton2);
/*  276 */     localJPanel2.add(Box.createRigidArea(hstrut5));
/*      */ 
/*  279 */     if (!this.readOnly) {
/*  280 */       localJButton2 = new JButton(this.filePane.getNewFolderAction());
/*  281 */       localJButton2.setText(null);
/*  282 */       localJButton2.setIcon(this.newFolderIcon);
/*  283 */       localJButton2.setToolTipText(this.newFolderToolTipText);
/*  284 */       localJButton2.getAccessibleContext().setAccessibleName(this.newFolderAccessibleName);
/*  285 */       localJButton2.setAlignmentX(0.0F);
/*  286 */       localJButton2.setAlignmentY(0.5F);
/*  287 */       localJButton2.setMargin(shrinkwrap);
/*  288 */       localJPanel2.add(localJButton2);
/*  289 */       localJPanel2.add(Box.createRigidArea(hstrut5));
/*      */     }
/*      */ 
/*  293 */     ButtonGroup localButtonGroup = new ButtonGroup();
/*      */ 
/*  296 */     this.listViewButton = new JToggleButton(this.listViewIcon);
/*  297 */     this.listViewButton.setToolTipText(this.listViewButtonToolTipText);
/*  298 */     this.listViewButton.getAccessibleContext().setAccessibleName(this.listViewButtonAccessibleName);
/*  299 */     this.listViewButton.setSelected(true);
/*  300 */     this.listViewButton.setAlignmentX(0.0F);
/*  301 */     this.listViewButton.setAlignmentY(0.5F);
/*  302 */     this.listViewButton.setMargin(shrinkwrap);
/*  303 */     this.listViewButton.addActionListener(this.filePane.getViewTypeAction(0));
/*  304 */     localJPanel2.add(this.listViewButton);
/*  305 */     localButtonGroup.add(this.listViewButton);
/*      */ 
/*  308 */     this.detailsViewButton = new JToggleButton(this.detailsViewIcon);
/*  309 */     this.detailsViewButton.setToolTipText(this.detailsViewButtonToolTipText);
/*  310 */     this.detailsViewButton.getAccessibleContext().setAccessibleName(this.detailsViewButtonAccessibleName);
/*  311 */     this.detailsViewButton.setAlignmentX(0.0F);
/*  312 */     this.detailsViewButton.setAlignmentY(0.5F);
/*  313 */     this.detailsViewButton.setMargin(shrinkwrap);
/*  314 */     this.detailsViewButton.addActionListener(this.filePane.getViewTypeAction(1));
/*  315 */     localJPanel2.add(this.detailsViewButton);
/*  316 */     localButtonGroup.add(this.detailsViewButton);
/*      */ 
/*  318 */     this.filePane.addPropertyChangeListener(new PropertyChangeListener() {
/*      */       public void propertyChange(PropertyChangeEvent paramAnonymousPropertyChangeEvent) {
/*  320 */         if ("viewType".equals(paramAnonymousPropertyChangeEvent.getPropertyName())) {
/*  321 */           int i = SynthFileChooserUIImpl.this.filePane.getViewType();
/*  322 */           switch (i) {
/*      */           case 0:
/*  324 */             SynthFileChooserUIImpl.this.listViewButton.setSelected(true);
/*  325 */             break;
/*      */           case 1:
/*  327 */             SynthFileChooserUIImpl.this.detailsViewButton.setSelected(true);
/*      */           }
/*      */         }
/*      */       }
/*      */     });
/*  337 */     paramJFileChooser.add(getAccessoryPanel(), "After");
/*  338 */     JComponent localJComponent = paramJFileChooser.getAccessory();
/*  339 */     if (localJComponent != null) {
/*  340 */       getAccessoryPanel().add(localJComponent);
/*      */     }
/*  342 */     this.filePane.setPreferredSize(LIST_PREF_SIZE);
/*  343 */     paramJFileChooser.add(this.filePane, "Center");
/*      */ 
/*  349 */     this.bottomPanel = new JPanel();
/*  350 */     this.bottomPanel.setLayout(new BoxLayout(this.bottomPanel, 1));
/*  351 */     paramJFileChooser.add(this.bottomPanel, "South");
/*      */ 
/*  354 */     JPanel localJPanel3 = new JPanel();
/*  355 */     localJPanel3.setLayout(new BoxLayout(localJPanel3, 2));
/*  356 */     this.bottomPanel.add(localJPanel3);
/*  357 */     this.bottomPanel.add(Box.createRigidArea(new Dimension(1, 5)));
/*      */ 
/*  359 */     this.fileNameLabel = new AlignedLabel();
/*  360 */     populateFileNameLabel();
/*  361 */     localJPanel3.add(this.fileNameLabel);
/*      */ 
/*  363 */     this.fileNameTextField = new JTextField(35) {
/*      */       public Dimension getMaximumSize() {
/*  365 */         return new Dimension(32767, super.getPreferredSize().height);
/*      */       }
/*      */     };
/*  368 */     localJPanel3.add(this.fileNameTextField);
/*  369 */     this.fileNameLabel.setLabelFor(this.fileNameTextField);
/*  370 */     this.fileNameTextField.addFocusListener(new FocusAdapter()
/*      */     {
/*      */       public void focusGained(FocusEvent paramAnonymousFocusEvent) {
/*  373 */         if (!SynthFileChooserUIImpl.this.getFileChooser().isMultiSelectionEnabled())
/*  374 */           SynthFileChooserUIImpl.this.filePane.clearSelection();
/*      */       }
/*      */     });
/*  379 */     if (paramJFileChooser.isMultiSelectionEnabled())
/*  380 */       setFileName(fileNameString(paramJFileChooser.getSelectedFiles()));
/*      */     else {
/*  382 */       setFileName(fileNameString(paramJFileChooser.getSelectedFile()));
/*      */     }
/*      */ 
/*  387 */     JPanel localJPanel4 = new JPanel();
/*  388 */     localJPanel4.setLayout(new BoxLayout(localJPanel4, 2));
/*  389 */     this.bottomPanel.add(localJPanel4);
/*      */ 
/*  391 */     AlignedLabel localAlignedLabel = new AlignedLabel(this.filesOfTypeLabelText);
/*  392 */     localAlignedLabel.setDisplayedMnemonic(this.filesOfTypeLabelMnemonic);
/*  393 */     localJPanel4.add(localAlignedLabel);
/*      */ 
/*  395 */     this.filterComboBoxModel = createFilterComboBoxModel();
/*  396 */     paramJFileChooser.addPropertyChangeListener(this.filterComboBoxModel);
/*  397 */     this.filterComboBox = new JComboBox(this.filterComboBoxModel);
/*  398 */     this.filterComboBox.getAccessibleContext().setAccessibleDescription(this.filesOfTypeLabelText);
/*  399 */     localAlignedLabel.setLabelFor(this.filterComboBox);
/*  400 */     this.filterComboBox.setRenderer(createFilterComboBoxRenderer());
/*  401 */     localJPanel4.add(this.filterComboBox);
/*      */ 
/*  405 */     this.buttonPanel = new JPanel();
/*  406 */     this.buttonPanel.setLayout(new ButtonAreaLayout(null));
/*      */ 
/*  408 */     this.buttonPanel.add(getApproveButton(paramJFileChooser));
/*  409 */     this.buttonPanel.add(getCancelButton(paramJFileChooser));
/*      */ 
/*  411 */     if (paramJFileChooser.getControlButtonsAreShown()) {
/*  412 */       addControlButtons();
/*      */     }
/*      */ 
/*  415 */     groupLabels(new AlignedLabel[] { this.fileNameLabel, localAlignedLabel });
/*      */   }
/*      */ 
/*      */   protected void installListeners(JFileChooser paramJFileChooser) {
/*  419 */     super.installListeners(paramJFileChooser);
/*  420 */     paramJFileChooser.addPropertyChangeListener("fileSelectionChanged", this.modeListener);
/*      */   }
/*      */ 
/*      */   protected void uninstallListeners(JFileChooser paramJFileChooser) {
/*  424 */     paramJFileChooser.removePropertyChangeListener("fileSelectionChanged", this.modeListener);
/*  425 */     super.uninstallListeners(paramJFileChooser);
/*      */   }
/*      */ 
/*      */   private String fileNameString(File paramFile) {
/*  429 */     if (paramFile == null) {
/*  430 */       return null;
/*      */     }
/*  432 */     JFileChooser localJFileChooser = getFileChooser();
/*  433 */     if ((localJFileChooser.isDirectorySelectionEnabled()) && (!localJFileChooser.isFileSelectionEnabled())) {
/*  434 */       return paramFile.getPath();
/*      */     }
/*  436 */     return paramFile.getName();
/*      */   }
/*      */ 
/*      */   private String fileNameString(File[] paramArrayOfFile)
/*      */   {
/*  442 */     StringBuffer localStringBuffer = new StringBuffer();
/*  443 */     for (int i = 0; (paramArrayOfFile != null) && (i < paramArrayOfFile.length); i++) {
/*  444 */       if (i > 0) {
/*  445 */         localStringBuffer.append(" ");
/*      */       }
/*  447 */       if (paramArrayOfFile.length > 1) {
/*  448 */         localStringBuffer.append("\"");
/*      */       }
/*  450 */       localStringBuffer.append(fileNameString(paramArrayOfFile[i]));
/*  451 */       if (paramArrayOfFile.length > 1) {
/*  452 */         localStringBuffer.append("\"");
/*      */       }
/*      */     }
/*  455 */     return localStringBuffer.toString();
/*      */   }
/*      */ 
/*      */   public void uninstallUI(JComponent paramJComponent)
/*      */   {
/*  460 */     paramJComponent.removePropertyChangeListener(this.filterComboBoxModel);
/*  461 */     paramJComponent.removePropertyChangeListener(this.filePane);
/*      */ 
/*  463 */     if (this.filePane != null) {
/*  464 */       this.filePane.uninstallUI();
/*  465 */       this.filePane = null;
/*      */     }
/*      */ 
/*  468 */     super.uninstallUI(paramJComponent);
/*      */   }
/*      */ 
/*      */   protected void installStrings(JFileChooser paramJFileChooser) {
/*  472 */     super.installStrings(paramJFileChooser);
/*      */ 
/*  474 */     Locale localLocale = paramJFileChooser.getLocale();
/*      */ 
/*  476 */     this.lookInLabelMnemonic = getMnemonic("FileChooser.lookInLabelMnemonic", localLocale);
/*  477 */     this.lookInLabelText = UIManager.getString("FileChooser.lookInLabelText", localLocale);
/*  478 */     this.saveInLabelText = UIManager.getString("FileChooser.saveInLabelText", localLocale);
/*      */ 
/*  480 */     this.fileNameLabelMnemonic = getMnemonic("FileChooser.fileNameLabelMnemonic", localLocale);
/*  481 */     this.fileNameLabelText = UIManager.getString("FileChooser.fileNameLabelText", localLocale);
/*  482 */     this.folderNameLabelMnemonic = getMnemonic("FileChooser.folderNameLabelMnemonic", localLocale);
/*  483 */     this.folderNameLabelText = UIManager.getString("FileChooser.folderNameLabelText", localLocale);
/*      */ 
/*  485 */     this.filesOfTypeLabelMnemonic = getMnemonic("FileChooser.filesOfTypeLabelMnemonic", localLocale);
/*  486 */     this.filesOfTypeLabelText = UIManager.getString("FileChooser.filesOfTypeLabelText", localLocale);
/*      */ 
/*  488 */     this.upFolderToolTipText = UIManager.getString("FileChooser.upFolderToolTipText", localLocale);
/*  489 */     this.upFolderAccessibleName = UIManager.getString("FileChooser.upFolderAccessibleName", localLocale);
/*      */ 
/*  491 */     this.homeFolderToolTipText = UIManager.getString("FileChooser.homeFolderToolTipText", localLocale);
/*  492 */     this.homeFolderAccessibleName = UIManager.getString("FileChooser.homeFolderAccessibleName", localLocale);
/*      */ 
/*  494 */     this.newFolderToolTipText = UIManager.getString("FileChooser.newFolderToolTipText", localLocale);
/*  495 */     this.newFolderAccessibleName = UIManager.getString("FileChooser.newFolderAccessibleName", localLocale);
/*      */ 
/*  497 */     this.listViewButtonToolTipText = UIManager.getString("FileChooser.listViewButtonToolTipText", localLocale);
/*  498 */     this.listViewButtonAccessibleName = UIManager.getString("FileChooser.listViewButtonAccessibleName", localLocale);
/*      */ 
/*  500 */     this.detailsViewButtonToolTipText = UIManager.getString("FileChooser.detailsViewButtonToolTipText", localLocale);
/*  501 */     this.detailsViewButtonAccessibleName = UIManager.getString("FileChooser.detailsViewButtonAccessibleName", localLocale);
/*      */   }
/*      */ 
/*      */   private int getMnemonic(String paramString, Locale paramLocale) {
/*  505 */     return SwingUtilities2.getUIDefaultsInt(paramString, paramLocale);
/*      */   }
/*      */ 
/*      */   public String getFileName()
/*      */   {
/*  510 */     if (this.fileNameTextField != null) {
/*  511 */       return this.fileNameTextField.getText();
/*      */     }
/*  513 */     return null;
/*      */   }
/*      */ 
/*      */   public void setFileName(String paramString)
/*      */   {
/*  518 */     if (this.fileNameTextField != null)
/*  519 */       this.fileNameTextField.setText(paramString);
/*      */   }
/*      */ 
/*      */   public void rescanCurrentDirectory(JFileChooser paramJFileChooser)
/*      */   {
/*  524 */     this.filePane.rescanCurrentDirectory();
/*      */   }
/*      */ 
/*      */   protected void doSelectedFileChanged(PropertyChangeEvent paramPropertyChangeEvent) {
/*  528 */     super.doSelectedFileChanged(paramPropertyChangeEvent);
/*      */ 
/*  530 */     File localFile = (File)paramPropertyChangeEvent.getNewValue();
/*  531 */     JFileChooser localJFileChooser = getFileChooser();
/*  532 */     if ((localFile != null) && (((localJFileChooser.isFileSelectionEnabled()) && (!localFile.isDirectory())) || ((localFile.isDirectory()) && (localJFileChooser.isDirectorySelectionEnabled()))))
/*      */     {
/*  536 */       setFileName(fileNameString(localFile));
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void doSelectedFilesChanged(PropertyChangeEvent paramPropertyChangeEvent) {
/*  541 */     super.doSelectedFilesChanged(paramPropertyChangeEvent);
/*      */ 
/*  543 */     File[] arrayOfFile = (File[])paramPropertyChangeEvent.getNewValue();
/*  544 */     JFileChooser localJFileChooser = getFileChooser();
/*  545 */     if ((arrayOfFile != null) && (arrayOfFile.length > 0) && ((arrayOfFile.length > 1) || (localJFileChooser.isDirectorySelectionEnabled()) || (!arrayOfFile[0].isDirectory())))
/*      */     {
/*  548 */       setFileName(fileNameString(arrayOfFile));
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void doDirectoryChanged(PropertyChangeEvent paramPropertyChangeEvent) {
/*  553 */     super.doDirectoryChanged(paramPropertyChangeEvent);
/*      */ 
/*  555 */     JFileChooser localJFileChooser = getFileChooser();
/*  556 */     FileSystemView localFileSystemView = localJFileChooser.getFileSystemView();
/*  557 */     File localFile = localJFileChooser.getCurrentDirectory();
/*      */ 
/*  559 */     if ((!this.readOnly) && (localFile != null)) {
/*  560 */       getNewFolderAction().setEnabled(this.filePane.canWrite(localFile));
/*      */     }
/*      */ 
/*  563 */     if (localFile != null) {
/*  564 */       JComponent localJComponent = getDirectoryComboBox();
/*  565 */       if ((localJComponent instanceof JComboBox)) {
/*  566 */         ComboBoxModel localComboBoxModel = ((JComboBox)localJComponent).getModel();
/*  567 */         if ((localComboBoxModel instanceof DirectoryComboBoxModel)) {
/*  568 */           ((DirectoryComboBoxModel)localComboBoxModel).addItem(localFile);
/*      */         }
/*      */       }
/*      */ 
/*  572 */       if ((localJFileChooser.isDirectorySelectionEnabled()) && (!localJFileChooser.isFileSelectionEnabled()))
/*  573 */         if (localFileSystemView.isFileSystem(localFile))
/*  574 */           setFileName(localFile.getPath());
/*      */         else
/*  576 */           setFileName(null);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void doFileSelectionModeChanged(PropertyChangeEvent paramPropertyChangeEvent)
/*      */   {
/*  584 */     super.doFileSelectionModeChanged(paramPropertyChangeEvent);
/*      */ 
/*  586 */     JFileChooser localJFileChooser = getFileChooser();
/*  587 */     File localFile = localJFileChooser.getCurrentDirectory();
/*  588 */     if ((localFile != null) && (localJFileChooser.isDirectorySelectionEnabled()) && (!localJFileChooser.isFileSelectionEnabled()) && (localJFileChooser.getFileSystemView().isFileSystem(localFile)))
/*      */     {
/*  593 */       setFileName(localFile.getPath());
/*      */     }
/*  595 */     else setFileName(null);
/*      */   }
/*      */ 
/*      */   protected void doAccessoryChanged(PropertyChangeEvent paramPropertyChangeEvent)
/*      */   {
/*  600 */     if (getAccessoryPanel() != null) {
/*  601 */       if (paramPropertyChangeEvent.getOldValue() != null) {
/*  602 */         getAccessoryPanel().remove((JComponent)paramPropertyChangeEvent.getOldValue());
/*      */       }
/*  604 */       JComponent localJComponent = (JComponent)paramPropertyChangeEvent.getNewValue();
/*  605 */       if (localJComponent != null)
/*  606 */         getAccessoryPanel().add(localJComponent, "Center");
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void doControlButtonsChanged(PropertyChangeEvent paramPropertyChangeEvent)
/*      */   {
/*  612 */     super.doControlButtonsChanged(paramPropertyChangeEvent);
/*      */ 
/*  614 */     if (getFileChooser().getControlButtonsAreShown())
/*  615 */       addControlButtons();
/*      */     else
/*  617 */       removeControlButtons();
/*      */   }
/*      */ 
/*      */   protected void addControlButtons()
/*      */   {
/*  622 */     if (this.bottomPanel != null)
/*  623 */       this.bottomPanel.add(this.buttonPanel);
/*      */   }
/*      */ 
/*      */   protected void removeControlButtons()
/*      */   {
/*  628 */     if (this.bottomPanel != null)
/*  629 */       this.bottomPanel.remove(this.buttonPanel);
/*      */   }
/*      */ 
/*      */   protected ActionMap createActionMap()
/*      */   {
/*  641 */     ActionMapUIResource localActionMapUIResource = new ActionMapUIResource();
/*      */ 
/*  643 */     FilePane.addActionsToMap(localActionMapUIResource, this.filePane.getActions());
/*      */ 
/*  645 */     localActionMapUIResource.put("fileNameCompletion", getFileNameCompletionAction());
/*  646 */     return localActionMapUIResource;
/*      */   }
/*      */ 
/*      */   protected JComponent getDirectoryComboBox()
/*      */   {
/*  654 */     return this.directoryComboBox;
/*      */   }
/*      */ 
/*      */   protected Action getDirectoryComboBoxAction() {
/*  658 */     return this.directoryComboBoxAction;
/*      */   }
/*      */ 
/*      */   protected DirectoryComboBoxRenderer createDirectoryComboBoxRenderer(JFileChooser paramJFileChooser) {
/*  662 */     return new DirectoryComboBoxRenderer(this.directoryComboBox.getRenderer(), null);
/*      */   }
/*      */ 
/*      */   protected DirectoryComboBoxModel createDirectoryComboBoxModel(JFileChooser paramJFileChooser)
/*      */   {
/*  733 */     return new DirectoryComboBoxModel();
/*      */   }
/*      */ 
/*      */   protected FilterComboBoxRenderer createFilterComboBoxRenderer()
/*      */   {
/*  880 */     return new FilterComboBoxRenderer(this.filterComboBox.getRenderer(), null);
/*      */   }
/*      */ 
/*      */   protected FilterComboBoxModel createFilterComboBoxModel()
/*      */   {
/*  914 */     return new FilterComboBoxModel();
/*      */   }
/*      */ 
/*      */   private static void groupLabels(AlignedLabel[] paramArrayOfAlignedLabel)
/*      */   {
/* 1064 */     for (int i = 0; i < paramArrayOfAlignedLabel.length; i++)
/* 1065 */       paramArrayOfAlignedLabel[i].group = paramArrayOfAlignedLabel;
/*      */   }
/*      */ 
/*      */   private class AlignedLabel extends JLabel
/*      */   {
/*      */     private AlignedLabel[] group;
/* 1071 */     private int maxWidth = 0;
/*      */ 
/*      */     AlignedLabel()
/*      */     {
/* 1075 */       setAlignmentX(0.0F);
/*      */     }
/*      */ 
/*      */     AlignedLabel(String arg2) {
/* 1079 */       super();
/* 1080 */       setAlignmentX(0.0F);
/*      */     }
/*      */ 
/*      */     public Dimension getPreferredSize() {
/* 1084 */       Dimension localDimension = super.getPreferredSize();
/*      */ 
/* 1086 */       return new Dimension(getMaxWidth() + 11, localDimension.height);
/*      */     }
/*      */ 
/*      */     private int getMaxWidth() {
/* 1090 */       if ((this.maxWidth == 0) && (this.group != null)) {
/* 1091 */         int i = 0;
/* 1092 */         for (int j = 0; j < this.group.length; j++) {
/* 1093 */           i = Math.max(this.group[j].getSuperPreferredWidth(), i);
/*      */         }
/* 1095 */         for (j = 0; j < this.group.length; j++) {
/* 1096 */           this.group[j].maxWidth = i;
/*      */         }
/*      */       }
/* 1099 */       return this.maxWidth;
/*      */     }
/*      */ 
/*      */     private int getSuperPreferredWidth() {
/* 1103 */       return super.getPreferredSize().width;
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class ButtonAreaLayout
/*      */     implements LayoutManager
/*      */   {
/*  995 */     private int hGap = 5;
/*  996 */     private int topMargin = 17;
/*      */ 
/*      */     public void addLayoutComponent(String paramString, Component paramComponent) {
/*      */     }
/*      */ 
/*      */     public void layoutContainer(Container paramContainer) {
/* 1002 */       Component[] arrayOfComponent = paramContainer.getComponents();
/*      */ 
/* 1004 */       if ((arrayOfComponent != null) && (arrayOfComponent.length > 0)) {
/* 1005 */         int i = arrayOfComponent.length;
/* 1006 */         Dimension[] arrayOfDimension = new Dimension[i];
/* 1007 */         Insets localInsets = paramContainer.getInsets();
/* 1008 */         int j = localInsets.top + this.topMargin;
/* 1009 */         int k = 0;
/*      */ 
/* 1011 */         for (int m = 0; m < i; m++) {
/* 1012 */           arrayOfDimension[m] = arrayOfComponent[m].getPreferredSize();
/* 1013 */           k = Math.max(k, arrayOfDimension[m].width);
/*      */         }
/*      */         int n;
/* 1016 */         if (paramContainer.getComponentOrientation().isLeftToRight()) {
/* 1017 */           m = paramContainer.getSize().width - localInsets.left - k;
/* 1018 */           n = this.hGap + k;
/*      */         } else {
/* 1020 */           m = localInsets.left;
/* 1021 */           n = -(this.hGap + k);
/*      */         }
/* 1023 */         for (int i1 = i - 1; i1 >= 0; i1--) {
/* 1024 */           arrayOfComponent[i1].setBounds(m, j, k, arrayOfDimension[i1].height);
/*      */ 
/* 1026 */           m -= n;
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     public Dimension minimumLayoutSize(Container paramContainer) {
/* 1032 */       if (paramContainer != null) {
/* 1033 */         Component[] arrayOfComponent = paramContainer.getComponents();
/*      */ 
/* 1035 */         if ((arrayOfComponent != null) && (arrayOfComponent.length > 0)) {
/* 1036 */           int i = arrayOfComponent.length;
/* 1037 */           int j = 0;
/* 1038 */           Insets localInsets = paramContainer.getInsets();
/* 1039 */           int k = this.topMargin + localInsets.top + localInsets.bottom;
/* 1040 */           int m = localInsets.left + localInsets.right;
/* 1041 */           int n = 0;
/*      */ 
/* 1043 */           for (int i1 = 0; i1 < i; i1++) {
/* 1044 */             Dimension localDimension = arrayOfComponent[i1].getPreferredSize();
/* 1045 */             j = Math.max(j, localDimension.height);
/* 1046 */             n = Math.max(n, localDimension.width);
/*      */           }
/* 1048 */           return new Dimension(m + i * n + (i - 1) * this.hGap, k + j);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1053 */       return new Dimension(0, 0);
/*      */     }
/*      */ 
/*      */     public Dimension preferredLayoutSize(Container paramContainer) {
/* 1057 */       return minimumLayoutSize(paramContainer);
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
/*  863 */       super();
/*      */     }
/*      */ 
/*      */     public void actionPerformed(ActionEvent paramActionEvent) {
/*  867 */       SynthFileChooserUIImpl.this.directoryComboBox.hidePopup();
/*  868 */       JComponent localJComponent = SynthFileChooserUIImpl.this.getDirectoryComboBox();
/*  869 */       if ((localJComponent instanceof JComboBox)) {
/*  870 */         File localFile = (File)((JComboBox)localJComponent).getSelectedItem();
/*  871 */         SynthFileChooserUIImpl.this.getFileChooser().setCurrentDirectory(localFile);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   protected class DirectoryComboBoxModel extends AbstractListModel
/*      */     implements ComboBoxModel
/*      */   {
/*  740 */     Vector<File> directories = new Vector();
/*  741 */     int[] depths = null;
/*  742 */     File selectedDirectory = null;
/*  743 */     JFileChooser chooser = SynthFileChooserUIImpl.this.getFileChooser();
/*  744 */     FileSystemView fsv = this.chooser.getFileSystemView();
/*      */ 
/*      */     public DirectoryComboBoxModel()
/*      */     {
/*  749 */       File localFile = SynthFileChooserUIImpl.this.getFileChooser().getCurrentDirectory();
/*  750 */       if (localFile != null)
/*  751 */         addItem(localFile);
/*      */     }
/*      */ 
/*      */     public void addItem(File paramFile)
/*      */     {
/*  762 */       if (paramFile == null) {
/*  763 */         return;
/*      */       }
/*      */ 
/*  766 */       boolean bool = FilePane.usesShellFolder(this.chooser);
/*      */ 
/*  768 */       int i = this.directories.size();
/*  769 */       this.directories.clear();
/*  770 */       if (i > 0) {
/*  771 */         fireIntervalRemoved(this, 0, i);
/*      */       }
/*      */ 
/*  774 */       File[] arrayOfFile = bool ? (File[])ShellFolder.get("fileChooserComboBoxFolders") : this.fsv.getRoots();
/*      */ 
/*  777 */       this.directories.addAll(Arrays.asList(arrayOfFile));
/*      */       File localFile1;
/*      */       try
/*      */       {
/*  784 */         localFile1 = ShellFolder.getNormalizedFile(paramFile);
/*      */       }
/*      */       catch (IOException localIOException) {
/*  787 */         localFile1 = paramFile;
/*      */       }
/*      */ 
/*      */       try
/*      */       {
/*  792 */         File localFile2 = bool ? ShellFolder.getShellFolder(localFile1) : localFile1;
/*      */ 
/*  794 */         File localFile3 = localFile2;
/*  795 */         Vector localVector = new Vector(10);
/*      */         do
/*  797 */           localVector.addElement(localFile3);
/*  798 */         while ((localFile3 = localFile3.getParentFile()) != null);
/*      */ 
/*  800 */         int j = localVector.size();
/*      */ 
/*  802 */         for (int k = 0; k < j; k++) {
/*  803 */           localFile3 = (File)localVector.get(k);
/*  804 */           if (this.directories.contains(localFile3)) {
/*  805 */             int m = this.directories.indexOf(localFile3);
/*  806 */             for (int n = k - 1; n >= 0; n--) {
/*  807 */               this.directories.insertElementAt(localVector.get(n), m + k - n);
/*      */             }
/*  809 */             break;
/*      */           }
/*      */         }
/*  812 */         calculateDepths();
/*  813 */         setSelectedItem(localFile2);
/*      */       } catch (FileNotFoundException localFileNotFoundException) {
/*  815 */         calculateDepths();
/*      */       }
/*      */     }
/*      */ 
/*      */     private void calculateDepths() {
/*  820 */       this.depths = new int[this.directories.size()];
/*  821 */       for (int i = 0; i < this.depths.length; i++) {
/*  822 */         File localFile1 = (File)this.directories.get(i);
/*  823 */         File localFile2 = localFile1.getParentFile();
/*  824 */         this.depths[i] = 0;
/*  825 */         if (localFile2 != null)
/*  826 */           for (int j = i - 1; j >= 0; j--)
/*  827 */             if (localFile2.equals(this.directories.get(j))) {
/*  828 */               this.depths[j] += 1;
/*  829 */               break;
/*      */             }
/*      */       }
/*      */     }
/*      */ 
/*      */     public int getDepth(int paramInt)
/*      */     {
/*  837 */       return (this.depths != null) && (paramInt >= 0) && (paramInt < this.depths.length) ? this.depths[paramInt] : 0;
/*      */     }
/*      */ 
/*      */     public void setSelectedItem(Object paramObject) {
/*  841 */       this.selectedDirectory = ((File)paramObject);
/*  842 */       fireContentsChanged(this, -1, -1);
/*      */     }
/*      */ 
/*      */     public Object getSelectedItem() {
/*  846 */       return this.selectedDirectory;
/*      */     }
/*      */ 
/*      */     public int getSize() {
/*  850 */       return this.directories.size();
/*      */     }
/*      */ 
/*      */     public Object getElementAt(int paramInt) {
/*  854 */       return this.directories.elementAt(paramInt);
/*      */     }
/*      */   }
/*      */ 
/*      */   private class DirectoryComboBoxRenderer
/*      */     implements ListCellRenderer
/*      */   {
/*      */     private ListCellRenderer delegate;
/*  676 */     SynthFileChooserUIImpl.IndentIcon ii = new SynthFileChooserUIImpl.IndentIcon(SynthFileChooserUIImpl.this);
/*      */ 
/*      */     private DirectoryComboBoxRenderer(ListCellRenderer arg2)
/*      */     {
/*      */       Object localObject;
/*  679 */       this.delegate = localObject;
/*      */     }
/*      */ 
/*      */     public Component getListCellRendererComponent(JList paramJList, Object paramObject, int paramInt, boolean paramBoolean1, boolean paramBoolean2)
/*      */     {
/*  684 */       Component localComponent = this.delegate.getListCellRendererComponent(paramJList, paramObject, paramInt, paramBoolean1, paramBoolean2);
/*      */ 
/*  686 */       assert ((localComponent instanceof JLabel));
/*  687 */       JLabel localJLabel = (JLabel)localComponent;
/*  688 */       if (paramObject == null) {
/*  689 */         localJLabel.setText("");
/*  690 */         return localJLabel;
/*      */       }
/*  692 */       File localFile = (File)paramObject;
/*  693 */       localJLabel.setText(SynthFileChooserUIImpl.this.getFileChooser().getName(localFile));
/*  694 */       Icon localIcon = SynthFileChooserUIImpl.this.getFileChooser().getIcon(localFile);
/*  695 */       this.ii.icon = localIcon;
/*  696 */       this.ii.depth = SynthFileChooserUIImpl.this.directoryComboBoxModel.getDepth(paramInt);
/*  697 */       localJLabel.setIcon(this.ii);
/*      */ 
/*  699 */       return localJLabel;
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
/*  924 */       this.filters = SynthFileChooserUIImpl.this.getFileChooser().getChoosableFileFilters();
/*      */     }
/*      */ 
/*      */     public void propertyChange(PropertyChangeEvent paramPropertyChangeEvent) {
/*  928 */       String str = paramPropertyChangeEvent.getPropertyName();
/*  929 */       if (str == "ChoosableFileFilterChangedProperty") {
/*  930 */         this.filters = ((FileFilter[])paramPropertyChangeEvent.getNewValue());
/*  931 */         fireContentsChanged(this, -1, -1);
/*  932 */       } else if (str == "fileFilterChanged") {
/*  933 */         fireContentsChanged(this, -1, -1);
/*      */       }
/*      */     }
/*      */ 
/*      */     public void setSelectedItem(Object paramObject) {
/*  938 */       if (paramObject != null) {
/*  939 */         SynthFileChooserUIImpl.this.getFileChooser().setFileFilter((FileFilter)paramObject);
/*  940 */         fireContentsChanged(this, -1, -1);
/*      */       }
/*      */     }
/*      */ 
/*      */     public Object getSelectedItem()
/*      */     {
/*  950 */       FileFilter localFileFilter1 = SynthFileChooserUIImpl.this.getFileChooser().getFileFilter();
/*  951 */       int i = 0;
/*  952 */       if (localFileFilter1 != null) {
/*  953 */         for (FileFilter localFileFilter2 : this.filters) {
/*  954 */           if (localFileFilter2 == localFileFilter1) {
/*  955 */             i = 1;
/*      */           }
/*      */         }
/*  958 */         if (i == 0) {
/*  959 */           SynthFileChooserUIImpl.this.getFileChooser().addChoosableFileFilter(localFileFilter1);
/*      */         }
/*      */       }
/*  962 */       return SynthFileChooserUIImpl.this.getFileChooser().getFileFilter();
/*      */     }
/*      */ 
/*      */     public int getSize() {
/*  966 */       if (this.filters != null) {
/*  967 */         return this.filters.length;
/*      */       }
/*  969 */       return 0;
/*      */     }
/*      */ 
/*      */     public Object getElementAt(int paramInt)
/*      */     {
/*  974 */       if (paramInt > getSize() - 1)
/*      */       {
/*  976 */         return SynthFileChooserUIImpl.this.getFileChooser().getFileFilter();
/*      */       }
/*  978 */       if (this.filters != null) {
/*  979 */         return this.filters[paramInt];
/*      */       }
/*  981 */       return null;
/*      */     }
/*      */   }
/*      */ 
/*      */   public class FilterComboBoxRenderer
/*      */     implements ListCellRenderer
/*      */   {
/*      */     private ListCellRenderer delegate;
/*      */ 
/*      */     private FilterComboBoxRenderer(ListCellRenderer arg2)
/*      */     {
/*      */       Object localObject;
/*  889 */       this.delegate = localObject;
/*      */     }
/*      */ 
/*      */     public Component getListCellRendererComponent(JList paramJList, Object paramObject, int paramInt, boolean paramBoolean1, boolean paramBoolean2) {
/*  893 */       Component localComponent = this.delegate.getListCellRendererComponent(paramJList, paramObject, paramInt, paramBoolean1, paramBoolean2);
/*      */ 
/*  895 */       String str = null;
/*  896 */       if ((paramObject != null) && ((paramObject instanceof FileFilter))) {
/*  897 */         str = ((FileFilter)paramObject).getDescription();
/*      */       }
/*      */ 
/*  902 */       assert ((localComponent instanceof JLabel));
/*  903 */       if (str != null) {
/*  904 */         ((JLabel)localComponent).setText(str);
/*      */       }
/*  906 */       return localComponent;
/*      */     }
/*      */   }
/*      */ 
/*      */   class IndentIcon
/*      */     implements Icon
/*      */   {
/*  706 */     Icon icon = null;
/*  707 */     int depth = 0;
/*      */ 
/*      */     IndentIcon() {  } 
/*  710 */     public void paintIcon(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2) { if (this.icon != null)
/*  711 */         if (paramComponent.getComponentOrientation().isLeftToRight())
/*  712 */           this.icon.paintIcon(paramComponent, paramGraphics, paramInt1 + this.depth * 10, paramInt2);
/*      */         else
/*  714 */           this.icon.paintIcon(paramComponent, paramGraphics, paramInt1, paramInt2);
/*      */     }
/*      */ 
/*      */     public int getIconWidth()
/*      */     {
/*  720 */       return (this.icon != null ? this.icon.getIconWidth() : 0) + this.depth * 10;
/*      */     }
/*      */ 
/*      */     public int getIconHeight() {
/*  724 */       return this.icon != null ? this.icon.getIconHeight() : 0;
/*      */     }
/*      */   }
/*      */ 
/*      */   private class SynthFileChooserUIAccessor
/*      */     implements FilePane.FileChooserUIAccessor
/*      */   {
/*      */     private SynthFileChooserUIAccessor()
/*      */     {
/*      */     }
/*      */ 
/*      */     public JFileChooser getFileChooser()
/*      */     {
/*  144 */       return SynthFileChooserUIImpl.this.getFileChooser();
/*      */     }
/*      */ 
/*      */     public BasicDirectoryModel getModel() {
/*  148 */       return SynthFileChooserUIImpl.this.getModel();
/*      */     }
/*      */ 
/*      */     public JPanel createList() {
/*  152 */       return null;
/*      */     }
/*      */ 
/*      */     public JPanel createDetailsView() {
/*  156 */       return null;
/*      */     }
/*      */ 
/*      */     public boolean isDirectorySelected() {
/*  160 */       return SynthFileChooserUIImpl.this.isDirectorySelected();
/*      */     }
/*      */ 
/*      */     public File getDirectory() {
/*  164 */       return SynthFileChooserUIImpl.this.getDirectory();
/*      */     }
/*      */ 
/*      */     public Action getChangeToParentDirectoryAction() {
/*  168 */       return SynthFileChooserUIImpl.this.getChangeToParentDirectoryAction();
/*      */     }
/*      */ 
/*      */     public Action getApproveSelectionAction() {
/*  172 */       return SynthFileChooserUIImpl.this.getApproveSelectionAction();
/*      */     }
/*      */ 
/*      */     public Action getNewFolderAction() {
/*  176 */       return SynthFileChooserUIImpl.this.getNewFolderAction();
/*      */     }
/*      */ 
/*      */     public MouseListener createDoubleClickListener(JList paramJList) {
/*  180 */       return SynthFileChooserUIImpl.this.createDoubleClickListener(getFileChooser(), paramJList);
/*      */     }
/*      */ 
/*      */     public ListSelectionListener createListSelectionListener()
/*      */     {
/*  185 */       return SynthFileChooserUIImpl.this.createListSelectionListener(getFileChooser());
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.swing.plaf.synth.SynthFileChooserUIImpl
 * JD-Core Version:    0.6.2
 */