/*      */ package javax.swing;
/*      */ 
/*      */ import java.awt.AWTEvent;
/*      */ import java.awt.BorderLayout;
/*      */ import java.awt.Component;
/*      */ import java.awt.Container;
/*      */ import java.awt.Dialog;
/*      */ import java.awt.EventQueue;
/*      */ import java.awt.Frame;
/*      */ import java.awt.GraphicsEnvironment;
/*      */ import java.awt.HeadlessException;
/*      */ import java.awt.Toolkit;
/*      */ import java.awt.Window;
/*      */ import java.awt.event.ActionEvent;
/*      */ import java.awt.event.ActionListener;
/*      */ import java.awt.event.InputEvent;
/*      */ import java.awt.event.WindowAdapter;
/*      */ import java.awt.event.WindowEvent;
/*      */ import java.beans.PropertyChangeEvent;
/*      */ import java.beans.PropertyChangeListener;
/*      */ import java.io.File;
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.ObjectOutputStream;
/*      */ import java.lang.ref.WeakReference;
/*      */ import java.util.Vector;
/*      */ import javax.accessibility.Accessible;
/*      */ import javax.accessibility.AccessibleContext;
/*      */ import javax.accessibility.AccessibleRole;
/*      */ import javax.swing.event.EventListenerList;
/*      */ import javax.swing.filechooser.FileFilter;
/*      */ import javax.swing.filechooser.FileSystemView;
/*      */ import javax.swing.filechooser.FileView;
/*      */ import javax.swing.plaf.ComponentUI;
/*      */ import javax.swing.plaf.FileChooserUI;
/*      */ 
/*      */ public class JFileChooser extends JComponent
/*      */   implements Accessible
/*      */ {
/*      */   private static final String uiClassID = "FileChooserUI";
/*      */   public static final int OPEN_DIALOG = 0;
/*      */   public static final int SAVE_DIALOG = 1;
/*      */   public static final int CUSTOM_DIALOG = 2;
/*      */   public static final int CANCEL_OPTION = 1;
/*      */   public static final int APPROVE_OPTION = 0;
/*      */   public static final int ERROR_OPTION = -1;
/*      */   public static final int FILES_ONLY = 0;
/*      */   public static final int DIRECTORIES_ONLY = 1;
/*      */   public static final int FILES_AND_DIRECTORIES = 2;
/*      */   public static final String CANCEL_SELECTION = "CancelSelection";
/*      */   public static final String APPROVE_SELECTION = "ApproveSelection";
/*      */   public static final String APPROVE_BUTTON_TEXT_CHANGED_PROPERTY = "ApproveButtonTextChangedProperty";
/*      */   public static final String APPROVE_BUTTON_TOOL_TIP_TEXT_CHANGED_PROPERTY = "ApproveButtonToolTipTextChangedProperty";
/*      */   public static final String APPROVE_BUTTON_MNEMONIC_CHANGED_PROPERTY = "ApproveButtonMnemonicChangedProperty";
/*      */   public static final String CONTROL_BUTTONS_ARE_SHOWN_CHANGED_PROPERTY = "ControlButtonsAreShownChangedProperty";
/*      */   public static final String DIRECTORY_CHANGED_PROPERTY = "directoryChanged";
/*      */   public static final String SELECTED_FILE_CHANGED_PROPERTY = "SelectedFileChangedProperty";
/*      */   public static final String SELECTED_FILES_CHANGED_PROPERTY = "SelectedFilesChangedProperty";
/*      */   public static final String MULTI_SELECTION_ENABLED_CHANGED_PROPERTY = "MultiSelectionEnabledChangedProperty";
/*      */   public static final String FILE_SYSTEM_VIEW_CHANGED_PROPERTY = "FileSystemViewChanged";
/*      */   public static final String FILE_VIEW_CHANGED_PROPERTY = "fileViewChanged";
/*      */   public static final String FILE_HIDING_CHANGED_PROPERTY = "FileHidingChanged";
/*      */   public static final String FILE_FILTER_CHANGED_PROPERTY = "fileFilterChanged";
/*      */   public static final String FILE_SELECTION_MODE_CHANGED_PROPERTY = "fileSelectionChanged";
/*      */   public static final String ACCESSORY_CHANGED_PROPERTY = "AccessoryChangedProperty";
/*      */   public static final String ACCEPT_ALL_FILE_FILTER_USED_CHANGED_PROPERTY = "acceptAllFileFilterUsedChanged";
/*      */   public static final String DIALOG_TITLE_CHANGED_PROPERTY = "DialogTitleChangedProperty";
/*      */   public static final String DIALOG_TYPE_CHANGED_PROPERTY = "DialogTypeChangedProperty";
/*      */   public static final String CHOOSABLE_FILE_FILTER_CHANGED_PROPERTY = "ChoosableFileFilterChangedProperty";
/*  246 */   private String dialogTitle = null;
/*  247 */   private String approveButtonText = null;
/*  248 */   private String approveButtonToolTipText = null;
/*  249 */   private int approveButtonMnemonic = 0;
/*      */ 
/*  251 */   private Vector<FileFilter> filters = new Vector(5);
/*  252 */   private JDialog dialog = null;
/*  253 */   private int dialogType = 0;
/*  254 */   private int returnValue = -1;
/*  255 */   private JComponent accessory = null;
/*      */ 
/*  257 */   private FileView fileView = null;
/*      */ 
/*  259 */   private boolean controlsShown = true;
/*      */ 
/*  261 */   private boolean useFileHiding = true;
/*      */   private static final String SHOW_HIDDEN_PROP = "awt.file.showHiddenFiles";
/*  267 */   private transient PropertyChangeListener showFilesListener = null;
/*      */ 
/*  269 */   private int fileSelectionMode = 0;
/*      */ 
/*  271 */   private boolean multiSelectionEnabled = false;
/*      */ 
/*  273 */   private boolean useAcceptAllFileFilter = true;
/*      */ 
/*  275 */   private boolean dragEnabled = false;
/*      */ 
/*  277 */   private FileFilter fileFilter = null;
/*      */ 
/*  279 */   private FileSystemView fileSystemView = null;
/*      */ 
/*  281 */   private File currentDirectory = null;
/*  282 */   private File selectedFile = null;
/*      */   private File[] selectedFiles;
/* 1942 */   protected AccessibleContext accessibleContext = null;
/*      */ 
/*      */   public JFileChooser()
/*      */   {
/*  296 */     this((File)null, (FileSystemView)null);
/*      */   }
/*      */ 
/*      */   public JFileChooser(String paramString)
/*      */   {
/*  311 */     this(paramString, (FileSystemView)null);
/*      */   }
/*      */ 
/*      */   public JFileChooser(File paramFile)
/*      */   {
/*  326 */     this(paramFile, (FileSystemView)null);
/*      */   }
/*      */ 
/*      */   public JFileChooser(FileSystemView paramFileSystemView)
/*      */   {
/*  334 */     this((File)null, paramFileSystemView);
/*      */   }
/*      */ 
/*      */   public JFileChooser(File paramFile, FileSystemView paramFileSystemView)
/*      */   {
/*  343 */     setup(paramFileSystemView);
/*  344 */     setCurrentDirectory(paramFile);
/*      */   }
/*      */ 
/*      */   public JFileChooser(String paramString, FileSystemView paramFileSystemView)
/*      */   {
/*  352 */     setup(paramFileSystemView);
/*  353 */     if (paramString == null)
/*  354 */       setCurrentDirectory(null);
/*      */     else
/*  356 */       setCurrentDirectory(this.fileSystemView.createFileObject(paramString));
/*      */   }
/*      */ 
/*      */   protected void setup(FileSystemView paramFileSystemView)
/*      */   {
/*  364 */     installShowFilesListener();
/*      */ 
/*  366 */     if (paramFileSystemView == null) {
/*  367 */       paramFileSystemView = FileSystemView.getFileSystemView();
/*      */     }
/*  369 */     setFileSystemView(paramFileSystemView);
/*  370 */     updateUI();
/*  371 */     if (isAcceptAllFileFilterUsed()) {
/*  372 */       setFileFilter(getAcceptAllFileFilter());
/*      */     }
/*  374 */     enableEvents(16L);
/*      */   }
/*      */ 
/*      */   private void installShowFilesListener()
/*      */   {
/*  379 */     Toolkit localToolkit = Toolkit.getDefaultToolkit();
/*  380 */     Object localObject = localToolkit.getDesktopProperty("awt.file.showHiddenFiles");
/*  381 */     if ((localObject instanceof Boolean)) {
/*  382 */       this.useFileHiding = (!((Boolean)localObject).booleanValue());
/*  383 */       this.showFilesListener = new WeakPCL(this);
/*  384 */       localToolkit.addPropertyChangeListener("awt.file.showHiddenFiles", this.showFilesListener);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setDragEnabled(boolean paramBoolean)
/*      */   {
/*  433 */     if ((paramBoolean) && (GraphicsEnvironment.isHeadless())) {
/*  434 */       throw new HeadlessException();
/*      */     }
/*  436 */     this.dragEnabled = paramBoolean;
/*      */   }
/*      */ 
/*      */   public boolean getDragEnabled()
/*      */   {
/*  447 */     return this.dragEnabled;
/*      */   }
/*      */ 
/*      */   public File getSelectedFile()
/*      */   {
/*  464 */     return this.selectedFile;
/*      */   }
/*      */ 
/*      */   public void setSelectedFile(File paramFile)
/*      */   {
/*  481 */     File localFile = this.selectedFile;
/*  482 */     this.selectedFile = paramFile;
/*  483 */     if (this.selectedFile != null) {
/*  484 */       if ((paramFile.isAbsolute()) && (!getFileSystemView().isParent(getCurrentDirectory(), this.selectedFile))) {
/*  485 */         setCurrentDirectory(this.selectedFile.getParentFile());
/*      */       }
/*  487 */       if ((!isMultiSelectionEnabled()) || (this.selectedFiles == null) || (this.selectedFiles.length == 1)) {
/*  488 */         ensureFileIsVisible(this.selectedFile);
/*      */       }
/*      */     }
/*  491 */     firePropertyChange("SelectedFileChangedProperty", localFile, this.selectedFile);
/*      */   }
/*      */ 
/*      */   public File[] getSelectedFiles()
/*      */   {
/*  499 */     if (this.selectedFiles == null) {
/*  500 */       return new File[0];
/*      */     }
/*  502 */     return (File[])this.selectedFiles.clone();
/*      */   }
/*      */ 
/*      */   public void setSelectedFiles(File[] paramArrayOfFile)
/*      */   {
/*  515 */     File[] arrayOfFile = this.selectedFiles;
/*  516 */     if ((paramArrayOfFile == null) || (paramArrayOfFile.length == 0)) {
/*  517 */       paramArrayOfFile = null;
/*  518 */       this.selectedFiles = null;
/*  519 */       setSelectedFile(null);
/*      */     } else {
/*  521 */       this.selectedFiles = ((File[])paramArrayOfFile.clone());
/*  522 */       setSelectedFile(this.selectedFiles[0]);
/*      */     }
/*  524 */     firePropertyChange("SelectedFilesChangedProperty", arrayOfFile, paramArrayOfFile);
/*      */   }
/*      */ 
/*      */   public File getCurrentDirectory()
/*      */   {
/*  534 */     return this.currentDirectory;
/*      */   }
/*      */ 
/*      */   public void setCurrentDirectory(File paramFile)
/*      */   {
/*  559 */     File localFile1 = this.currentDirectory;
/*      */ 
/*  561 */     if ((paramFile != null) && (!paramFile.exists())) {
/*  562 */       paramFile = this.currentDirectory;
/*      */     }
/*  564 */     if (paramFile == null) {
/*  565 */       paramFile = getFileSystemView().getDefaultDirectory();
/*      */     }
/*  567 */     if (this.currentDirectory != null)
/*      */     {
/*  569 */       if (this.currentDirectory.equals(paramFile)) {
/*  570 */         return;
/*      */       }
/*      */     }
/*      */ 
/*  574 */     File localFile2 = null;
/*  575 */     while ((!isTraversable(paramFile)) && (localFile2 != paramFile)) {
/*  576 */       localFile2 = paramFile;
/*  577 */       paramFile = getFileSystemView().getParentDirectory(paramFile);
/*      */     }
/*  579 */     this.currentDirectory = paramFile;
/*      */ 
/*  581 */     firePropertyChange("directoryChanged", localFile1, this.currentDirectory);
/*      */   }
/*      */ 
/*      */   public void changeToParentDirectory()
/*      */   {
/*  591 */     this.selectedFile = null;
/*  592 */     File localFile = getCurrentDirectory();
/*  593 */     setCurrentDirectory(getFileSystemView().getParentDirectory(localFile));
/*      */   }
/*      */ 
/*      */   public void rescanCurrentDirectory()
/*      */   {
/*  600 */     getUI().rescanCurrentDirectory(this);
/*      */   }
/*      */ 
/*      */   public void ensureFileIsVisible(File paramFile)
/*      */   {
/*  610 */     getUI().ensureFileIsVisible(this, paramFile);
/*      */   }
/*      */ 
/*      */   public int showOpenDialog(Component paramComponent)
/*      */     throws HeadlessException
/*      */   {
/*  638 */     setDialogType(0);
/*  639 */     return showDialog(paramComponent, null);
/*      */   }
/*      */ 
/*      */   public int showSaveDialog(Component paramComponent)
/*      */     throws HeadlessException
/*      */   {
/*  663 */     setDialogType(1);
/*  664 */     return showDialog(paramComponent, null);
/*      */   }
/*      */ 
/*      */   public int showDialog(Component paramComponent, String paramString)
/*      */     throws HeadlessException
/*      */   {
/*  723 */     if (this.dialog != null)
/*      */     {
/*  725 */       return -1;
/*      */     }
/*      */ 
/*  728 */     if (paramString != null) {
/*  729 */       setApproveButtonText(paramString);
/*  730 */       setDialogType(2);
/*      */     }
/*  732 */     this.dialog = createDialog(paramComponent);
/*  733 */     this.dialog.addWindowListener(new WindowAdapter() {
/*      */       public void windowClosing(WindowEvent paramAnonymousWindowEvent) {
/*  735 */         JFileChooser.this.returnValue = 1;
/*      */       }
/*      */     });
/*  738 */     this.returnValue = -1;
/*  739 */     rescanCurrentDirectory();
/*      */ 
/*  741 */     this.dialog.show();
/*  742 */     firePropertyChange("JFileChooserDialogIsClosingProperty", this.dialog, null);
/*      */ 
/*  747 */     this.dialog.getContentPane().removeAll();
/*  748 */     this.dialog.dispose();
/*  749 */     this.dialog = null;
/*  750 */     return this.returnValue;
/*      */   }
/*      */ 
/*      */   protected JDialog createDialog(Component paramComponent)
/*      */     throws HeadlessException
/*      */   {
/*  779 */     FileChooserUI localFileChooserUI = getUI();
/*  780 */     String str = localFileChooserUI.getDialogTitle(this);
/*  781 */     putClientProperty("AccessibleDescription", str);
/*      */ 
/*  785 */     Window localWindow = JOptionPane.getWindowForComponent(paramComponent);
/*      */     JDialog localJDialog;
/*  786 */     if ((localWindow instanceof Frame))
/*  787 */       localJDialog = new JDialog((Frame)localWindow, str, true);
/*      */     else {
/*  789 */       localJDialog = new JDialog((Dialog)localWindow, str, true);
/*      */     }
/*  791 */     localJDialog.setComponentOrientation(getComponentOrientation());
/*      */ 
/*  793 */     Container localContainer = localJDialog.getContentPane();
/*  794 */     localContainer.setLayout(new BorderLayout());
/*  795 */     localContainer.add(this, "Center");
/*      */ 
/*  797 */     if (JDialog.isDefaultLookAndFeelDecorated()) {
/*  798 */       boolean bool = UIManager.getLookAndFeel().getSupportsWindowDecorations();
/*      */ 
/*  800 */       if (bool) {
/*  801 */         localJDialog.getRootPane().setWindowDecorationStyle(6);
/*      */       }
/*      */     }
/*  804 */     localJDialog.getRootPane().setDefaultButton(localFileChooserUI.getDefaultButton(this));
/*  805 */     localJDialog.pack();
/*  806 */     localJDialog.setLocationRelativeTo(paramComponent);
/*      */ 
/*  808 */     return localJDialog;
/*      */   }
/*      */ 
/*      */   public boolean getControlButtonsAreShown()
/*      */   {
/*  826 */     return this.controlsShown;
/*      */   }
/*      */ 
/*      */   public void setControlButtonsAreShown(boolean paramBoolean)
/*      */   {
/*  855 */     if (this.controlsShown == paramBoolean) {
/*  856 */       return;
/*      */     }
/*  858 */     boolean bool = this.controlsShown;
/*  859 */     this.controlsShown = paramBoolean;
/*  860 */     firePropertyChange("ControlButtonsAreShownChangedProperty", bool, this.controlsShown);
/*      */   }
/*      */ 
/*      */   public int getDialogType()
/*      */   {
/*  877 */     return this.dialogType;
/*      */   }
/*      */ 
/*      */   public void setDialogType(int paramInt)
/*      */   {
/*  917 */     if (this.dialogType == paramInt) {
/*  918 */       return;
/*      */     }
/*  920 */     if ((paramInt != 0) && (paramInt != 1) && (paramInt != 2)) {
/*  921 */       throw new IllegalArgumentException("Incorrect Dialog Type: " + paramInt);
/*      */     }
/*  923 */     int i = this.dialogType;
/*  924 */     this.dialogType = paramInt;
/*  925 */     if ((paramInt == 0) || (paramInt == 1)) {
/*  926 */       setApproveButtonText(null);
/*      */     }
/*  928 */     firePropertyChange("DialogTypeChangedProperty", i, paramInt);
/*      */   }
/*      */ 
/*      */   public void setDialogTitle(String paramString)
/*      */   {
/*  946 */     String str = this.dialogTitle;
/*  947 */     this.dialogTitle = paramString;
/*  948 */     if (this.dialog != null) {
/*  949 */       this.dialog.setTitle(paramString);
/*      */     }
/*  951 */     firePropertyChange("DialogTitleChangedProperty", str, paramString);
/*      */   }
/*      */ 
/*      */   public String getDialogTitle()
/*      */   {
/*  960 */     return this.dialogTitle;
/*      */   }
/*      */ 
/*      */   public void setApproveButtonToolTipText(String paramString)
/*      */   {
/*  984 */     if (this.approveButtonToolTipText == paramString) {
/*  985 */       return;
/*      */     }
/*  987 */     String str = this.approveButtonToolTipText;
/*  988 */     this.approveButtonToolTipText = paramString;
/*  989 */     firePropertyChange("ApproveButtonToolTipTextChangedProperty", str, this.approveButtonToolTipText);
/*      */   }
/*      */ 
/*      */   public String getApproveButtonToolTipText()
/*      */   {
/* 1004 */     return this.approveButtonToolTipText;
/*      */   }
/*      */ 
/*      */   public int getApproveButtonMnemonic()
/*      */   {
/* 1014 */     return this.approveButtonMnemonic;
/*      */   }
/*      */ 
/*      */   public void setApproveButtonMnemonic(int paramInt)
/*      */   {
/* 1030 */     if (this.approveButtonMnemonic == paramInt) {
/* 1031 */       return;
/*      */     }
/* 1033 */     int i = this.approveButtonMnemonic;
/* 1034 */     this.approveButtonMnemonic = paramInt;
/* 1035 */     firePropertyChange("ApproveButtonMnemonicChangedProperty", i, this.approveButtonMnemonic);
/*      */   }
/*      */ 
/*      */   public void setApproveButtonMnemonic(char paramChar)
/*      */   {
/* 1045 */     int i = paramChar;
/* 1046 */     if ((i >= 97) && (i <= 122)) {
/* 1047 */       i -= 32;
/*      */     }
/* 1049 */     setApproveButtonMnemonic(i);
/*      */   }
/*      */ 
/*      */   public void setApproveButtonText(String paramString)
/*      */   {
/* 1070 */     if (this.approveButtonText == paramString) {
/* 1071 */       return;
/*      */     }
/* 1073 */     String str = this.approveButtonText;
/* 1074 */     this.approveButtonText = paramString;
/* 1075 */     firePropertyChange("ApproveButtonTextChangedProperty", str, paramString);
/*      */   }
/*      */ 
/*      */   public String getApproveButtonText()
/*      */   {
/* 1092 */     return this.approveButtonText;
/*      */   }
/*      */ 
/*      */   public FileFilter[] getChoosableFileFilters()
/*      */   {
/* 1106 */     FileFilter[] arrayOfFileFilter = new FileFilter[this.filters.size()];
/* 1107 */     this.filters.copyInto(arrayOfFileFilter);
/* 1108 */     return arrayOfFileFilter;
/*      */   }
/*      */ 
/*      */   public void addChoosableFileFilter(FileFilter paramFileFilter)
/*      */   {
/* 1130 */     if ((paramFileFilter != null) && (!this.filters.contains(paramFileFilter))) {
/* 1131 */       FileFilter[] arrayOfFileFilter = getChoosableFileFilters();
/* 1132 */       this.filters.addElement(paramFileFilter);
/* 1133 */       firePropertyChange("ChoosableFileFilterChangedProperty", arrayOfFileFilter, getChoosableFileFilters());
/* 1134 */       if ((this.fileFilter == null) && (this.filters.size() == 1))
/* 1135 */         setFileFilter(paramFileFilter);
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean removeChoosableFileFilter(FileFilter paramFileFilter)
/*      */   {
/* 1149 */     if (this.filters.contains(paramFileFilter)) {
/* 1150 */       if (getFileFilter() == paramFileFilter) {
/* 1151 */         setFileFilter(null);
/*      */       }
/* 1153 */       FileFilter[] arrayOfFileFilter = getChoosableFileFilters();
/* 1154 */       this.filters.removeElement(paramFileFilter);
/* 1155 */       firePropertyChange("ChoosableFileFilterChangedProperty", arrayOfFileFilter, getChoosableFileFilters());
/* 1156 */       return true;
/*      */     }
/* 1158 */     return false;
/*      */   }
/*      */ 
/*      */   public void resetChoosableFileFilters()
/*      */   {
/* 1172 */     FileFilter[] arrayOfFileFilter = getChoosableFileFilters();
/* 1173 */     setFileFilter(null);
/* 1174 */     this.filters.removeAllElements();
/* 1175 */     if (isAcceptAllFileFilterUsed()) {
/* 1176 */       addChoosableFileFilter(getAcceptAllFileFilter());
/*      */     }
/* 1178 */     firePropertyChange("ChoosableFileFilterChangedProperty", arrayOfFileFilter, getChoosableFileFilters());
/*      */   }
/*      */ 
/*      */   public FileFilter getAcceptAllFileFilter()
/*      */   {
/* 1186 */     FileFilter localFileFilter = null;
/* 1187 */     if (getUI() != null) {
/* 1188 */       localFileFilter = getUI().getAcceptAllFileFilter(this);
/*      */     }
/* 1190 */     return localFileFilter;
/*      */   }
/*      */ 
/*      */   public boolean isAcceptAllFileFilterUsed()
/*      */   {
/* 1200 */     return this.useAcceptAllFileFilter;
/*      */   }
/*      */ 
/*      */   public void setAcceptAllFileFilterUsed(boolean paramBoolean)
/*      */   {
/* 1222 */     boolean bool = this.useAcceptAllFileFilter;
/* 1223 */     this.useAcceptAllFileFilter = paramBoolean;
/* 1224 */     if (!paramBoolean) {
/* 1225 */       removeChoosableFileFilter(getAcceptAllFileFilter());
/*      */     } else {
/* 1227 */       removeChoosableFileFilter(getAcceptAllFileFilter());
/* 1228 */       addChoosableFileFilter(getAcceptAllFileFilter());
/*      */     }
/* 1230 */     firePropertyChange("acceptAllFileFilterUsedChanged", bool, this.useAcceptAllFileFilter);
/*      */   }
/*      */ 
/*      */   public JComponent getAccessory()
/*      */   {
/* 1240 */     return this.accessory;
/*      */   }
/*      */ 
/*      */   public void setAccessory(JComponent paramJComponent)
/*      */   {
/* 1259 */     JComponent localJComponent = this.accessory;
/* 1260 */     this.accessory = paramJComponent;
/* 1261 */     firePropertyChange("AccessoryChangedProperty", localJComponent, this.accessory);
/*      */   }
/*      */ 
/*      */   public void setFileSelectionMode(int paramInt)
/*      */   {
/* 1291 */     if (this.fileSelectionMode == paramInt) {
/* 1292 */       return;
/*      */     }
/*      */ 
/* 1295 */     if ((paramInt == 0) || (paramInt == 1) || (paramInt == 2)) {
/* 1296 */       int i = this.fileSelectionMode;
/* 1297 */       this.fileSelectionMode = paramInt;
/* 1298 */       firePropertyChange("fileSelectionChanged", i, this.fileSelectionMode);
/*      */     } else {
/* 1300 */       throw new IllegalArgumentException("Incorrect Mode for file selection: " + paramInt);
/*      */     }
/*      */   }
/*      */ 
/*      */   public int getFileSelectionMode()
/*      */   {
/* 1317 */     return this.fileSelectionMode;
/*      */   }
/*      */ 
/*      */   public boolean isFileSelectionEnabled()
/*      */   {
/* 1328 */     return (this.fileSelectionMode == 0) || (this.fileSelectionMode == 2);
/*      */   }
/*      */ 
/*      */   public boolean isDirectorySelectionEnabled()
/*      */   {
/* 1339 */     return (this.fileSelectionMode == 1) || (this.fileSelectionMode == 2);
/*      */   }
/*      */ 
/*      */   public void setMultiSelectionEnabled(boolean paramBoolean)
/*      */   {
/* 1353 */     if (this.multiSelectionEnabled == paramBoolean) {
/* 1354 */       return;
/*      */     }
/* 1356 */     boolean bool = this.multiSelectionEnabled;
/* 1357 */     this.multiSelectionEnabled = paramBoolean;
/* 1358 */     firePropertyChange("MultiSelectionEnabledChangedProperty", bool, this.multiSelectionEnabled);
/*      */   }
/*      */ 
/*      */   public boolean isMultiSelectionEnabled()
/*      */   {
/* 1367 */     return this.multiSelectionEnabled;
/*      */   }
/*      */ 
/*      */   public boolean isFileHidingEnabled()
/*      */   {
/* 1379 */     return this.useFileHiding;
/*      */   }
/*      */ 
/*      */   public void setFileHidingEnabled(boolean paramBoolean)
/*      */   {
/* 1398 */     if (this.showFilesListener != null) {
/* 1399 */       Toolkit.getDefaultToolkit().removePropertyChangeListener("awt.file.showHiddenFiles", this.showFilesListener);
/* 1400 */       this.showFilesListener = null;
/*      */     }
/* 1402 */     boolean bool = this.useFileHiding;
/* 1403 */     this.useFileHiding = paramBoolean;
/* 1404 */     firePropertyChange("FileHidingChanged", bool, this.useFileHiding);
/*      */   }
/*      */ 
/*      */   public void setFileFilter(FileFilter paramFileFilter)
/*      */   {
/* 1420 */     FileFilter localFileFilter = this.fileFilter;
/* 1421 */     this.fileFilter = paramFileFilter;
/* 1422 */     if (paramFileFilter != null) {
/* 1423 */       if ((isMultiSelectionEnabled()) && (this.selectedFiles != null) && (this.selectedFiles.length > 0)) {
/* 1424 */         Vector localVector = new Vector();
/* 1425 */         int i = 0;
/* 1426 */         for (File localFile : this.selectedFiles) {
/* 1427 */           if (paramFileFilter.accept(localFile))
/* 1428 */             localVector.add(localFile);
/*      */           else {
/* 1430 */             i = 1;
/*      */           }
/*      */         }
/* 1433 */         if (i != 0)
/* 1434 */           setSelectedFiles(localVector.size() == 0 ? null : (File[])localVector.toArray(new File[localVector.size()]));
/*      */       }
/* 1436 */       else if ((this.selectedFile != null) && (!paramFileFilter.accept(this.selectedFile))) {
/* 1437 */         setSelectedFile(null);
/*      */       }
/*      */     }
/* 1440 */     firePropertyChange("fileFilterChanged", localFileFilter, this.fileFilter);
/*      */   }
/*      */ 
/*      */   public FileFilter getFileFilter()
/*      */   {
/* 1452 */     return this.fileFilter;
/*      */   }
/*      */ 
/*      */   public void setFileView(FileView paramFileView)
/*      */   {
/* 1467 */     FileView localFileView = this.fileView;
/* 1468 */     this.fileView = paramFileView;
/* 1469 */     firePropertyChange("fileViewChanged", localFileView, paramFileView);
/*      */   }
/*      */ 
/*      */   public FileView getFileView()
/*      */   {
/* 1478 */     return this.fileView;
/*      */   }
/*      */ 
/*      */   public String getName(File paramFile)
/*      */   {
/* 1498 */     String str = null;
/* 1499 */     if (paramFile != null) {
/* 1500 */       if (getFileView() != null) {
/* 1501 */         str = getFileView().getName(paramFile);
/*      */       }
/*      */ 
/* 1504 */       FileView localFileView = getUI().getFileView(this);
/*      */ 
/* 1506 */       if ((str == null) && (localFileView != null)) {
/* 1507 */         str = localFileView.getName(paramFile);
/*      */       }
/*      */     }
/* 1510 */     return str;
/*      */   }
/*      */ 
/*      */   public String getDescription(File paramFile)
/*      */   {
/* 1521 */     String str = null;
/* 1522 */     if (paramFile != null) {
/* 1523 */       if (getFileView() != null) {
/* 1524 */         str = getFileView().getDescription(paramFile);
/*      */       }
/*      */ 
/* 1527 */       FileView localFileView = getUI().getFileView(this);
/*      */ 
/* 1529 */       if ((str == null) && (localFileView != null)) {
/* 1530 */         str = localFileView.getDescription(paramFile);
/*      */       }
/*      */     }
/* 1533 */     return str;
/*      */   }
/*      */ 
/*      */   public String getTypeDescription(File paramFile)
/*      */   {
/* 1544 */     String str = null;
/* 1545 */     if (paramFile != null) {
/* 1546 */       if (getFileView() != null) {
/* 1547 */         str = getFileView().getTypeDescription(paramFile);
/*      */       }
/*      */ 
/* 1550 */       FileView localFileView = getUI().getFileView(this);
/*      */ 
/* 1552 */       if ((str == null) && (localFileView != null)) {
/* 1553 */         str = localFileView.getTypeDescription(paramFile);
/*      */       }
/*      */     }
/* 1556 */     return str;
/*      */   }
/*      */ 
/*      */   public Icon getIcon(File paramFile)
/*      */   {
/* 1567 */     Icon localIcon = null;
/* 1568 */     if (paramFile != null) {
/* 1569 */       if (getFileView() != null) {
/* 1570 */         localIcon = getFileView().getIcon(paramFile);
/*      */       }
/*      */ 
/* 1573 */       FileView localFileView = getUI().getFileView(this);
/*      */ 
/* 1575 */       if ((localIcon == null) && (localFileView != null)) {
/* 1576 */         localIcon = localFileView.getIcon(paramFile);
/*      */       }
/*      */     }
/* 1579 */     return localIcon;
/*      */   }
/*      */ 
/*      */   public boolean isTraversable(File paramFile)
/*      */   {
/* 1590 */     Boolean localBoolean = null;
/* 1591 */     if (paramFile != null) {
/* 1592 */       if (getFileView() != null) {
/* 1593 */         localBoolean = getFileView().isTraversable(paramFile);
/*      */       }
/*      */ 
/* 1596 */       FileView localFileView = getUI().getFileView(this);
/*      */ 
/* 1598 */       if ((localBoolean == null) && (localFileView != null)) {
/* 1599 */         localBoolean = localFileView.isTraversable(paramFile);
/*      */       }
/* 1601 */       if (localBoolean == null) {
/* 1602 */         localBoolean = getFileSystemView().isTraversable(paramFile);
/*      */       }
/*      */     }
/* 1605 */     return (localBoolean != null) && (localBoolean.booleanValue());
/*      */   }
/*      */ 
/*      */   public boolean accept(File paramFile)
/*      */   {
/* 1615 */     boolean bool = true;
/* 1616 */     if ((paramFile != null) && (this.fileFilter != null)) {
/* 1617 */       bool = this.fileFilter.accept(paramFile);
/*      */     }
/* 1619 */     return bool;
/*      */   }
/*      */ 
/*      */   public void setFileSystemView(FileSystemView paramFileSystemView)
/*      */   {
/* 1636 */     FileSystemView localFileSystemView = this.fileSystemView;
/* 1637 */     this.fileSystemView = paramFileSystemView;
/* 1638 */     firePropertyChange("FileSystemViewChanged", localFileSystemView, this.fileSystemView);
/*      */   }
/*      */ 
/*      */   public FileSystemView getFileSystemView()
/*      */   {
/* 1647 */     return this.fileSystemView;
/*      */   }
/*      */ 
/*      */   public void approveSelection()
/*      */   {
/* 1665 */     this.returnValue = 0;
/* 1666 */     if (this.dialog != null) {
/* 1667 */       this.dialog.setVisible(false);
/*      */     }
/* 1669 */     fireActionPerformed("ApproveSelection");
/*      */   }
/*      */ 
/*      */   public void cancelSelection()
/*      */   {
/* 1682 */     this.returnValue = 1;
/* 1683 */     if (this.dialog != null) {
/* 1684 */       this.dialog.setVisible(false);
/*      */     }
/* 1686 */     fireActionPerformed("CancelSelection");
/*      */   }
/*      */ 
/*      */   public void addActionListener(ActionListener paramActionListener)
/*      */   {
/* 1698 */     this.listenerList.add(ActionListener.class, paramActionListener);
/*      */   }
/*      */ 
/*      */   public void removeActionListener(ActionListener paramActionListener)
/*      */   {
/* 1709 */     this.listenerList.remove(ActionListener.class, paramActionListener);
/*      */   }
/*      */ 
/*      */   public ActionListener[] getActionListeners()
/*      */   {
/* 1726 */     return (ActionListener[])this.listenerList.getListeners(ActionListener.class);
/*      */   }
/*      */ 
/*      */   protected void fireActionPerformed(String paramString)
/*      */   {
/* 1738 */     Object[] arrayOfObject = this.listenerList.getListenerList();
/* 1739 */     long l = EventQueue.getMostRecentEventTime();
/* 1740 */     int i = 0;
/* 1741 */     AWTEvent localAWTEvent = EventQueue.getCurrentEvent();
/* 1742 */     if ((localAWTEvent instanceof InputEvent))
/* 1743 */       i = ((InputEvent)localAWTEvent).getModifiers();
/* 1744 */     else if ((localAWTEvent instanceof ActionEvent)) {
/* 1745 */       i = ((ActionEvent)localAWTEvent).getModifiers();
/*      */     }
/* 1747 */     ActionEvent localActionEvent = null;
/*      */ 
/* 1750 */     for (int j = arrayOfObject.length - 2; j >= 0; j -= 2)
/* 1751 */       if (arrayOfObject[j] == ActionListener.class)
/*      */       {
/* 1753 */         if (localActionEvent == null) {
/* 1754 */           localActionEvent = new ActionEvent(this, 1001, paramString, l, i);
/*      */         }
/*      */ 
/* 1758 */         ((ActionListener)arrayOfObject[(j + 1)]).actionPerformed(localActionEvent);
/*      */       }
/*      */   }
/*      */ 
/*      */   public void updateUI()
/*      */   {
/* 1795 */     if (isAcceptAllFileFilterUsed()) {
/* 1796 */       removeChoosableFileFilter(getAcceptAllFileFilter());
/*      */     }
/* 1798 */     FileChooserUI localFileChooserUI = (FileChooserUI)UIManager.getUI(this);
/* 1799 */     if (this.fileSystemView == null)
/*      */     {
/* 1801 */       setFileSystemView(FileSystemView.getFileSystemView());
/*      */     }
/* 1803 */     setUI(localFileChooserUI);
/*      */ 
/* 1805 */     if (isAcceptAllFileFilterUsed())
/* 1806 */       addChoosableFileFilter(getAcceptAllFileFilter());
/*      */   }
/*      */ 
/*      */   public String getUIClassID()
/*      */   {
/* 1822 */     return "FileChooserUI";
/*      */   }
/*      */ 
/*      */   public FileChooserUI getUI()
/*      */   {
/* 1831 */     return (FileChooserUI)this.ui;
/*      */   }
/*      */ 
/*      */   private void readObject(ObjectInputStream paramObjectInputStream)
/*      */     throws IOException, ClassNotFoundException
/*      */   {
/* 1841 */     paramObjectInputStream.defaultReadObject();
/* 1842 */     installShowFilesListener();
/*      */   }
/*      */ 
/*      */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*      */     throws IOException
/*      */   {
/* 1851 */     FileSystemView localFileSystemView = null;
/*      */ 
/* 1853 */     if (isAcceptAllFileFilterUsed())
/*      */     {
/* 1856 */       removeChoosableFileFilter(getAcceptAllFileFilter());
/*      */     }
/* 1858 */     if (this.fileSystemView.equals(FileSystemView.getFileSystemView()))
/*      */     {
/* 1861 */       localFileSystemView = this.fileSystemView;
/* 1862 */       this.fileSystemView = null;
/*      */     }
/* 1864 */     paramObjectOutputStream.defaultWriteObject();
/* 1865 */     if (localFileSystemView != null) {
/* 1866 */       this.fileSystemView = localFileSystemView;
/*      */     }
/* 1868 */     if (isAcceptAllFileFilterUsed()) {
/* 1869 */       addChoosableFileFilter(getAcceptAllFileFilter());
/*      */     }
/* 1871 */     if (getUIClassID().equals("FileChooserUI")) {
/* 1872 */       byte b = JComponent.getWriteObjCounter(this);
/* 1873 */       b = (byte)(b - 1); JComponent.setWriteObjCounter(this, b);
/* 1874 */       if ((b == 0) && (this.ui != null))
/* 1875 */         this.ui.installUI(this);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected String paramString()
/*      */   {
/* 1892 */     String str1 = this.approveButtonText != null ? this.approveButtonText : "";
/*      */ 
/* 1894 */     String str2 = this.dialogTitle != null ? this.dialogTitle : "";
/*      */     String str3;
/* 1897 */     if (this.dialogType == 0)
/* 1898 */       str3 = "OPEN_DIALOG";
/* 1899 */     else if (this.dialogType == 1)
/* 1900 */       str3 = "SAVE_DIALOG";
/* 1901 */     else if (this.dialogType == 2)
/* 1902 */       str3 = "CUSTOM_DIALOG";
/* 1903 */     else str3 = "";
/*      */     String str4;
/* 1905 */     if (this.returnValue == 1)
/* 1906 */       str4 = "CANCEL_OPTION";
/* 1907 */     else if (this.returnValue == 0)
/* 1908 */       str4 = "APPROVE_OPTION";
/* 1909 */     else if (this.returnValue == -1)
/* 1910 */       str4 = "ERROR_OPTION";
/* 1911 */     else str4 = "";
/* 1912 */     String str5 = this.useFileHiding ? "true" : "false";
/*      */     String str6;
/* 1915 */     if (this.fileSelectionMode == 0)
/* 1916 */       str6 = "FILES_ONLY";
/* 1917 */     else if (this.fileSelectionMode == 1)
/* 1918 */       str6 = "DIRECTORIES_ONLY";
/* 1919 */     else if (this.fileSelectionMode == 2)
/* 1920 */       str6 = "FILES_AND_DIRECTORIES";
/* 1921 */     else str6 = "";
/* 1922 */     String str7 = this.currentDirectory != null ? this.currentDirectory.toString() : "";
/*      */ 
/* 1924 */     String str8 = this.selectedFile != null ? this.selectedFile.toString() : "";
/*      */ 
/* 1927 */     return super.paramString() + ",approveButtonText=" + str1 + ",currentDirectory=" + str7 + ",dialogTitle=" + str2 + ",dialogType=" + str3 + ",fileSelectionMode=" + str6 + ",returnValue=" + str4 + ",selectedFile=" + str8 + ",useFileHiding=" + str5;
/*      */   }
/*      */ 
/*      */   public AccessibleContext getAccessibleContext()
/*      */   {
/* 1954 */     if (this.accessibleContext == null) {
/* 1955 */       this.accessibleContext = new AccessibleJFileChooser();
/*      */     }
/* 1957 */     return this.accessibleContext;
/*      */   }
/*      */ 
/*      */   protected class AccessibleJFileChooser extends JComponent.AccessibleJComponent
/*      */   {
/*      */     protected AccessibleJFileChooser()
/*      */     {
/* 1966 */       super();
/*      */     }
/*      */ 
/*      */     public AccessibleRole getAccessibleRole()
/*      */     {
/* 1976 */       return AccessibleRole.FILE_CHOOSER;
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class WeakPCL
/*      */     implements PropertyChangeListener
/*      */   {
/*      */     WeakReference<JFileChooser> jfcRef;
/*      */ 
/*      */     public WeakPCL(JFileChooser paramJFileChooser)
/*      */     {
/* 1767 */       this.jfcRef = new WeakReference(paramJFileChooser);
/*      */     }
/*      */     public void propertyChange(PropertyChangeEvent paramPropertyChangeEvent) {
/* 1770 */       assert (paramPropertyChangeEvent.getPropertyName().equals("awt.file.showHiddenFiles"));
/* 1771 */       JFileChooser localJFileChooser = (JFileChooser)this.jfcRef.get();
/* 1772 */       if (localJFileChooser == null)
/*      */       {
/* 1775 */         Toolkit.getDefaultToolkit().removePropertyChangeListener("awt.file.showHiddenFiles", this);
/*      */       }
/*      */       else {
/* 1778 */         boolean bool = localJFileChooser.useFileHiding;
/* 1779 */         localJFileChooser.useFileHiding = (!((Boolean)paramPropertyChangeEvent.getNewValue()).booleanValue());
/* 1780 */         localJFileChooser.firePropertyChange("FileHidingChanged", bool, localJFileChooser.useFileHiding);
/*      */       }
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.JFileChooser
 * JD-Core Version:    0.6.2
 */