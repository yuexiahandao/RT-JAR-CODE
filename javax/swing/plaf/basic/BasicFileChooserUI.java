/*      */ package javax.swing.plaf.basic;
/*      */ 
/*      */ import java.awt.BorderLayout;
/*      */ import java.awt.Toolkit;
/*      */ import java.awt.datatransfer.DataFlavor;
/*      */ import java.awt.datatransfer.Transferable;
/*      */ import java.awt.event.ActionEvent;
/*      */ import java.awt.event.MouseAdapter;
/*      */ import java.awt.event.MouseEvent;
/*      */ import java.awt.event.MouseListener;
/*      */ import java.beans.PropertyChangeListener;
/*      */ import java.io.File;
/*      */ import java.io.FileNotFoundException;
/*      */ import java.io.IOException;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Hashtable;
/*      */ import java.util.List;
/*      */ import java.util.Locale;
/*      */ import java.util.regex.Matcher;
/*      */ import java.util.regex.Pattern;
/*      */ import java.util.regex.PatternSyntaxException;
/*      */ import javax.swing.AbstractAction;
/*      */ import javax.swing.Action;
/*      */ import javax.swing.ActionMap;
/*      */ import javax.swing.Icon;
/*      */ import javax.swing.InputMap;
/*      */ import javax.swing.JButton;
/*      */ import javax.swing.JComponent;
/*      */ import javax.swing.JFileChooser;
/*      */ import javax.swing.JList;
/*      */ import javax.swing.JOptionPane;
/*      */ import javax.swing.JPanel;
/*      */ import javax.swing.JTable;
/*      */ import javax.swing.ListModel;
/*      */ import javax.swing.LookAndFeel;
/*      */ import javax.swing.SwingUtilities;
/*      */ import javax.swing.TransferHandler;
/*      */ import javax.swing.UIManager;
/*      */ import javax.swing.event.ListSelectionEvent;
/*      */ import javax.swing.event.ListSelectionListener;
/*      */ import javax.swing.filechooser.FileFilter;
/*      */ import javax.swing.filechooser.FileSystemView;
/*      */ import javax.swing.filechooser.FileView;
/*      */ import javax.swing.plaf.ActionMapUIResource;
/*      */ import javax.swing.plaf.ComponentUI;
/*      */ import javax.swing.plaf.FileChooserUI;
/*      */ import javax.swing.plaf.UIResource;
/*      */ import sun.awt.shell.ShellFolder;
/*      */ import sun.swing.DefaultLookup;
/*      */ import sun.swing.FilePane;
/*      */ import sun.swing.SwingUtilities2;
/*      */ import sun.swing.UIAction;
/*      */ 
/*      */ public class BasicFileChooserUI extends FileChooserUI
/*      */ {
/*   53 */   protected Icon directoryIcon = null;
/*   54 */   protected Icon fileIcon = null;
/*   55 */   protected Icon computerIcon = null;
/*   56 */   protected Icon hardDriveIcon = null;
/*   57 */   protected Icon floppyDriveIcon = null;
/*      */ 
/*   59 */   protected Icon newFolderIcon = null;
/*   60 */   protected Icon upFolderIcon = null;
/*   61 */   protected Icon homeFolderIcon = null;
/*   62 */   protected Icon listViewIcon = null;
/*   63 */   protected Icon detailsViewIcon = null;
/*   64 */   protected Icon viewMenuIcon = null;
/*      */ 
/*   66 */   protected int saveButtonMnemonic = 0;
/*   67 */   protected int openButtonMnemonic = 0;
/*   68 */   protected int cancelButtonMnemonic = 0;
/*   69 */   protected int updateButtonMnemonic = 0;
/*   70 */   protected int helpButtonMnemonic = 0;
/*      */ 
/*   78 */   protected int directoryOpenButtonMnemonic = 0;
/*      */ 
/*   80 */   protected String saveButtonText = null;
/*   81 */   protected String openButtonText = null;
/*   82 */   protected String cancelButtonText = null;
/*   83 */   protected String updateButtonText = null;
/*   84 */   protected String helpButtonText = null;
/*      */ 
/*   92 */   protected String directoryOpenButtonText = null;
/*      */ 
/*   94 */   private String openDialogTitleText = null;
/*   95 */   private String saveDialogTitleText = null;
/*      */ 
/*   97 */   protected String saveButtonToolTipText = null;
/*   98 */   protected String openButtonToolTipText = null;
/*   99 */   protected String cancelButtonToolTipText = null;
/*  100 */   protected String updateButtonToolTipText = null;
/*  101 */   protected String helpButtonToolTipText = null;
/*      */ 
/*  109 */   protected String directoryOpenButtonToolTipText = null;
/*      */ 
/*  112 */   private Action approveSelectionAction = new ApproveSelectionAction();
/*  113 */   private Action cancelSelectionAction = new CancelSelectionAction();
/*  114 */   private Action updateAction = new UpdateAction();
/*      */   private Action newFolderAction;
/*  116 */   private Action goHomeAction = new GoHomeAction();
/*  117 */   private Action changeToParentDirectoryAction = new ChangeToParentDirectoryAction();
/*      */ 
/*  119 */   private String newFolderErrorSeparator = null;
/*  120 */   private String newFolderErrorText = null;
/*  121 */   private String newFolderParentDoesntExistTitleText = null;
/*  122 */   private String newFolderParentDoesntExistText = null;
/*  123 */   private String fileDescriptionText = null;
/*  124 */   private String directoryDescriptionText = null;
/*      */ 
/*  126 */   private JFileChooser filechooser = null;
/*      */ 
/*  128 */   private boolean directorySelected = false;
/*  129 */   private File directory = null;
/*      */ 
/*  131 */   private PropertyChangeListener propertyChangeListener = null;
/*  132 */   private AcceptAllFileFilter acceptAllFileFilter = new AcceptAllFileFilter();
/*  133 */   private FileFilter actualFileFilter = null;
/*  134 */   private GlobFilter globFilter = null;
/*  135 */   private BasicDirectoryModel model = null;
/*  136 */   private BasicFileView fileView = new BasicFileView();
/*      */   private boolean usesSingleFilePane;
/*      */   private boolean readOnly;
/*  141 */   private JPanel accessoryPanel = null;
/*      */   private Handler handler;
/* 1283 */   private static final TransferHandler defaultTransferHandler = new FileTransferHandler();
/*      */ 
/*      */   public static ComponentUI createUI(JComponent paramJComponent)
/*      */   {
/*  158 */     return new BasicFileChooserUI((JFileChooser)paramJComponent);
/*      */   }
/*      */ 
/*      */   public BasicFileChooserUI(JFileChooser paramJFileChooser) {
/*      */   }
/*      */ 
/*      */   public void installUI(JComponent paramJComponent) {
/*  165 */     this.accessoryPanel = new JPanel(new BorderLayout());
/*  166 */     this.filechooser = ((JFileChooser)paramJComponent);
/*      */ 
/*  168 */     createModel();
/*      */ 
/*  170 */     clearIconCache();
/*      */ 
/*  172 */     installDefaults(this.filechooser);
/*  173 */     installComponents(this.filechooser);
/*  174 */     installListeners(this.filechooser);
/*  175 */     this.filechooser.applyComponentOrientation(this.filechooser.getComponentOrientation());
/*      */   }
/*      */ 
/*      */   public void uninstallUI(JComponent paramJComponent) {
/*  179 */     uninstallListeners(this.filechooser);
/*  180 */     uninstallComponents(this.filechooser);
/*  181 */     uninstallDefaults(this.filechooser);
/*      */ 
/*  183 */     if (this.accessoryPanel != null) {
/*  184 */       this.accessoryPanel.removeAll();
/*      */     }
/*      */ 
/*  187 */     this.accessoryPanel = null;
/*  188 */     getFileChooser().removeAll();
/*      */ 
/*  190 */     this.handler = null;
/*      */   }
/*      */ 
/*      */   public void installComponents(JFileChooser paramJFileChooser) {
/*      */   }
/*      */ 
/*      */   public void uninstallComponents(JFileChooser paramJFileChooser) {
/*      */   }
/*      */ 
/*      */   protected void installListeners(JFileChooser paramJFileChooser) {
/*  200 */     this.propertyChangeListener = createPropertyChangeListener(paramJFileChooser);
/*  201 */     if (this.propertyChangeListener != null) {
/*  202 */       paramJFileChooser.addPropertyChangeListener(this.propertyChangeListener);
/*      */     }
/*  204 */     paramJFileChooser.addPropertyChangeListener(getModel());
/*      */ 
/*  206 */     InputMap localInputMap = getInputMap(1);
/*      */ 
/*  208 */     SwingUtilities.replaceUIInputMap(paramJFileChooser, 1, localInputMap);
/*      */ 
/*  210 */     ActionMap localActionMap = getActionMap();
/*  211 */     SwingUtilities.replaceUIActionMap(paramJFileChooser, localActionMap);
/*      */   }
/*      */ 
/*      */   InputMap getInputMap(int paramInt) {
/*  215 */     if (paramInt == 1) {
/*  216 */       return (InputMap)DefaultLookup.get(getFileChooser(), this, "FileChooser.ancestorInputMap");
/*      */     }
/*      */ 
/*  219 */     return null;
/*      */   }
/*      */ 
/*      */   ActionMap getActionMap() {
/*  223 */     return createActionMap();
/*      */   }
/*      */ 
/*      */   ActionMap createActionMap() {
/*  227 */     ActionMapUIResource localActionMapUIResource = new ActionMapUIResource();
/*      */ 
/*  229 */     UIAction local1 = new UIAction("refresh") {
/*      */       public void actionPerformed(ActionEvent paramAnonymousActionEvent) {
/*  231 */         BasicFileChooserUI.this.getFileChooser().rescanCurrentDirectory();
/*      */       }
/*      */     };
/*  235 */     localActionMapUIResource.put("approveSelection", getApproveSelectionAction());
/*  236 */     localActionMapUIResource.put("cancelSelection", getCancelSelectionAction());
/*  237 */     localActionMapUIResource.put("refresh", local1);
/*  238 */     localActionMapUIResource.put("Go Up", getChangeToParentDirectoryAction());
/*      */ 
/*  240 */     return localActionMapUIResource;
/*      */   }
/*      */ 
/*      */   protected void uninstallListeners(JFileChooser paramJFileChooser)
/*      */   {
/*  245 */     if (this.propertyChangeListener != null) {
/*  246 */       paramJFileChooser.removePropertyChangeListener(this.propertyChangeListener);
/*      */     }
/*  248 */     paramJFileChooser.removePropertyChangeListener(getModel());
/*  249 */     SwingUtilities.replaceUIInputMap(paramJFileChooser, 1, null);
/*      */ 
/*  251 */     SwingUtilities.replaceUIActionMap(paramJFileChooser, null);
/*      */   }
/*      */ 
/*      */   protected void installDefaults(JFileChooser paramJFileChooser)
/*      */   {
/*  256 */     installIcons(paramJFileChooser);
/*  257 */     installStrings(paramJFileChooser);
/*  258 */     this.usesSingleFilePane = UIManager.getBoolean("FileChooser.usesSingleFilePane");
/*  259 */     this.readOnly = UIManager.getBoolean("FileChooser.readOnly");
/*  260 */     TransferHandler localTransferHandler = paramJFileChooser.getTransferHandler();
/*  261 */     if ((localTransferHandler == null) || ((localTransferHandler instanceof UIResource))) {
/*  262 */       paramJFileChooser.setTransferHandler(defaultTransferHandler);
/*      */     }
/*  264 */     LookAndFeel.installProperty(paramJFileChooser, "opaque", Boolean.FALSE);
/*      */   }
/*      */ 
/*      */   protected void installIcons(JFileChooser paramJFileChooser) {
/*  268 */     this.directoryIcon = UIManager.getIcon("FileView.directoryIcon");
/*  269 */     this.fileIcon = UIManager.getIcon("FileView.fileIcon");
/*  270 */     this.computerIcon = UIManager.getIcon("FileView.computerIcon");
/*  271 */     this.hardDriveIcon = UIManager.getIcon("FileView.hardDriveIcon");
/*  272 */     this.floppyDriveIcon = UIManager.getIcon("FileView.floppyDriveIcon");
/*      */ 
/*  274 */     this.newFolderIcon = UIManager.getIcon("FileChooser.newFolderIcon");
/*  275 */     this.upFolderIcon = UIManager.getIcon("FileChooser.upFolderIcon");
/*  276 */     this.homeFolderIcon = UIManager.getIcon("FileChooser.homeFolderIcon");
/*  277 */     this.detailsViewIcon = UIManager.getIcon("FileChooser.detailsViewIcon");
/*  278 */     this.listViewIcon = UIManager.getIcon("FileChooser.listViewIcon");
/*  279 */     this.viewMenuIcon = UIManager.getIcon("FileChooser.viewMenuIcon");
/*      */   }
/*      */ 
/*      */   protected void installStrings(JFileChooser paramJFileChooser)
/*      */   {
/*  284 */     Locale localLocale = paramJFileChooser.getLocale();
/*  285 */     this.newFolderErrorText = UIManager.getString("FileChooser.newFolderErrorText", localLocale);
/*  286 */     this.newFolderErrorSeparator = UIManager.getString("FileChooser.newFolderErrorSeparator", localLocale);
/*      */ 
/*  288 */     this.newFolderParentDoesntExistTitleText = UIManager.getString("FileChooser.newFolderParentDoesntExistTitleText", localLocale);
/*  289 */     this.newFolderParentDoesntExistText = UIManager.getString("FileChooser.newFolderParentDoesntExistText", localLocale);
/*      */ 
/*  291 */     this.fileDescriptionText = UIManager.getString("FileChooser.fileDescriptionText", localLocale);
/*  292 */     this.directoryDescriptionText = UIManager.getString("FileChooser.directoryDescriptionText", localLocale);
/*      */ 
/*  294 */     this.saveButtonText = UIManager.getString("FileChooser.saveButtonText", localLocale);
/*  295 */     this.openButtonText = UIManager.getString("FileChooser.openButtonText", localLocale);
/*  296 */     this.saveDialogTitleText = UIManager.getString("FileChooser.saveDialogTitleText", localLocale);
/*  297 */     this.openDialogTitleText = UIManager.getString("FileChooser.openDialogTitleText", localLocale);
/*  298 */     this.cancelButtonText = UIManager.getString("FileChooser.cancelButtonText", localLocale);
/*  299 */     this.updateButtonText = UIManager.getString("FileChooser.updateButtonText", localLocale);
/*  300 */     this.helpButtonText = UIManager.getString("FileChooser.helpButtonText", localLocale);
/*  301 */     this.directoryOpenButtonText = UIManager.getString("FileChooser.directoryOpenButtonText", localLocale);
/*      */ 
/*  303 */     this.saveButtonMnemonic = getMnemonic("FileChooser.saveButtonMnemonic", localLocale);
/*  304 */     this.openButtonMnemonic = getMnemonic("FileChooser.openButtonMnemonic", localLocale);
/*  305 */     this.cancelButtonMnemonic = getMnemonic("FileChooser.cancelButtonMnemonic", localLocale);
/*  306 */     this.updateButtonMnemonic = getMnemonic("FileChooser.updateButtonMnemonic", localLocale);
/*  307 */     this.helpButtonMnemonic = getMnemonic("FileChooser.helpButtonMnemonic", localLocale);
/*  308 */     this.directoryOpenButtonMnemonic = getMnemonic("FileChooser.directoryOpenButtonMnemonic", localLocale);
/*      */ 
/*  310 */     this.saveButtonToolTipText = UIManager.getString("FileChooser.saveButtonToolTipText", localLocale);
/*  311 */     this.openButtonToolTipText = UIManager.getString("FileChooser.openButtonToolTipText", localLocale);
/*  312 */     this.cancelButtonToolTipText = UIManager.getString("FileChooser.cancelButtonToolTipText", localLocale);
/*  313 */     this.updateButtonToolTipText = UIManager.getString("FileChooser.updateButtonToolTipText", localLocale);
/*  314 */     this.helpButtonToolTipText = UIManager.getString("FileChooser.helpButtonToolTipText", localLocale);
/*  315 */     this.directoryOpenButtonToolTipText = UIManager.getString("FileChooser.directoryOpenButtonToolTipText", localLocale);
/*      */   }
/*      */ 
/*      */   protected void uninstallDefaults(JFileChooser paramJFileChooser) {
/*  319 */     uninstallIcons(paramJFileChooser);
/*  320 */     uninstallStrings(paramJFileChooser);
/*  321 */     if ((paramJFileChooser.getTransferHandler() instanceof UIResource))
/*  322 */       paramJFileChooser.setTransferHandler(null);
/*      */   }
/*      */ 
/*      */   protected void uninstallIcons(JFileChooser paramJFileChooser)
/*      */   {
/*  327 */     this.directoryIcon = null;
/*  328 */     this.fileIcon = null;
/*  329 */     this.computerIcon = null;
/*  330 */     this.hardDriveIcon = null;
/*  331 */     this.floppyDriveIcon = null;
/*      */ 
/*  333 */     this.newFolderIcon = null;
/*  334 */     this.upFolderIcon = null;
/*  335 */     this.homeFolderIcon = null;
/*  336 */     this.detailsViewIcon = null;
/*  337 */     this.listViewIcon = null;
/*  338 */     this.viewMenuIcon = null;
/*      */   }
/*      */ 
/*      */   protected void uninstallStrings(JFileChooser paramJFileChooser) {
/*  342 */     this.saveButtonText = null;
/*  343 */     this.openButtonText = null;
/*  344 */     this.cancelButtonText = null;
/*  345 */     this.updateButtonText = null;
/*  346 */     this.helpButtonText = null;
/*  347 */     this.directoryOpenButtonText = null;
/*      */ 
/*  349 */     this.saveButtonToolTipText = null;
/*  350 */     this.openButtonToolTipText = null;
/*  351 */     this.cancelButtonToolTipText = null;
/*  352 */     this.updateButtonToolTipText = null;
/*  353 */     this.helpButtonToolTipText = null;
/*  354 */     this.directoryOpenButtonToolTipText = null;
/*      */   }
/*      */ 
/*      */   protected void createModel() {
/*  358 */     if (this.model != null) {
/*  359 */       this.model.invalidateFileCache();
/*      */     }
/*  361 */     this.model = new BasicDirectoryModel(getFileChooser());
/*      */   }
/*      */ 
/*      */   public BasicDirectoryModel getModel() {
/*  365 */     return this.model;
/*      */   }
/*      */ 
/*      */   public PropertyChangeListener createPropertyChangeListener(JFileChooser paramJFileChooser) {
/*  369 */     return null;
/*      */   }
/*      */ 
/*      */   public String getFileName() {
/*  373 */     return null;
/*      */   }
/*      */ 
/*      */   public String getDirectoryName() {
/*  377 */     return null;
/*      */   }
/*      */ 
/*      */   public void setFileName(String paramString) {
/*      */   }
/*      */ 
/*      */   public void setDirectoryName(String paramString) {
/*      */   }
/*      */ 
/*      */   public void rescanCurrentDirectory(JFileChooser paramJFileChooser) {
/*      */   }
/*      */ 
/*      */   public void ensureFileIsVisible(JFileChooser paramJFileChooser, File paramFile) {
/*      */   }
/*      */ 
/*      */   public JFileChooser getFileChooser() {
/*  393 */     return this.filechooser;
/*      */   }
/*      */ 
/*      */   public JPanel getAccessoryPanel() {
/*  397 */     return this.accessoryPanel;
/*      */   }
/*      */ 
/*      */   protected JButton getApproveButton(JFileChooser paramJFileChooser) {
/*  401 */     return null;
/*      */   }
/*      */ 
/*      */   public JButton getDefaultButton(JFileChooser paramJFileChooser) {
/*  405 */     return getApproveButton(paramJFileChooser);
/*      */   }
/*      */ 
/*      */   public String getApproveButtonToolTipText(JFileChooser paramJFileChooser) {
/*  409 */     String str = paramJFileChooser.getApproveButtonToolTipText();
/*  410 */     if (str != null) {
/*  411 */       return str;
/*      */     }
/*      */ 
/*  414 */     if (paramJFileChooser.getDialogType() == 0)
/*  415 */       return this.openButtonToolTipText;
/*  416 */     if (paramJFileChooser.getDialogType() == 1) {
/*  417 */       return this.saveButtonToolTipText;
/*      */     }
/*  419 */     return null;
/*      */   }
/*      */ 
/*      */   public void clearIconCache() {
/*  423 */     this.fileView.clearIconCache();
/*      */   }
/*      */ 
/*      */   private Handler getHandler()
/*      */   {
/*  432 */     if (this.handler == null) {
/*  433 */       this.handler = new Handler();
/*      */     }
/*  435 */     return this.handler;
/*      */   }
/*      */ 
/*      */   protected MouseListener createDoubleClickListener(JFileChooser paramJFileChooser, JList paramJList)
/*      */   {
/*  440 */     return new Handler(paramJList);
/*      */   }
/*      */ 
/*      */   public ListSelectionListener createListSelectionListener(JFileChooser paramJFileChooser) {
/*  444 */     return getHandler();
/*      */   }
/*      */ 
/*      */   protected boolean isDirectorySelected()
/*      */   {
/*  610 */     return this.directorySelected;
/*      */   }
/*      */ 
/*      */   protected void setDirectorySelected(boolean paramBoolean)
/*      */   {
/*  621 */     this.directorySelected = paramBoolean;
/*      */   }
/*      */ 
/*      */   protected File getDirectory()
/*      */   {
/*  632 */     return this.directory;
/*      */   }
/*      */ 
/*      */   protected void setDirectory(File paramFile)
/*      */   {
/*  644 */     this.directory = paramFile;
/*      */   }
/*      */ 
/*      */   private int getMnemonic(String paramString, Locale paramLocale)
/*      */   {
/*  651 */     return SwingUtilities2.getUIDefaultsInt(paramString, paramLocale);
/*      */   }
/*      */ 
/*      */   public FileFilter getAcceptAllFileFilter(JFileChooser paramJFileChooser)
/*      */   {
/*  662 */     return this.acceptAllFileFilter;
/*      */   }
/*      */ 
/*      */   public FileView getFileView(JFileChooser paramJFileChooser)
/*      */   {
/*  667 */     return this.fileView;
/*      */   }
/*      */ 
/*      */   public String getDialogTitle(JFileChooser paramJFileChooser)
/*      */   {
/*  675 */     String str = paramJFileChooser.getDialogTitle();
/*  676 */     if (str != null)
/*  677 */       return str;
/*  678 */     if (paramJFileChooser.getDialogType() == 0)
/*  679 */       return this.openDialogTitleText;
/*  680 */     if (paramJFileChooser.getDialogType() == 1) {
/*  681 */       return this.saveDialogTitleText;
/*      */     }
/*  683 */     return getApproveButtonText(paramJFileChooser);
/*      */   }
/*      */ 
/*      */   public int getApproveButtonMnemonic(JFileChooser paramJFileChooser)
/*      */   {
/*  689 */     int i = paramJFileChooser.getApproveButtonMnemonic();
/*  690 */     if (i > 0)
/*  691 */       return i;
/*  692 */     if (paramJFileChooser.getDialogType() == 0)
/*  693 */       return this.openButtonMnemonic;
/*  694 */     if (paramJFileChooser.getDialogType() == 1) {
/*  695 */       return this.saveButtonMnemonic;
/*      */     }
/*  697 */     return i;
/*      */   }
/*      */ 
/*      */   public String getApproveButtonText(JFileChooser paramJFileChooser)
/*      */   {
/*  702 */     String str = paramJFileChooser.getApproveButtonText();
/*  703 */     if (str != null)
/*  704 */       return str;
/*  705 */     if (paramJFileChooser.getDialogType() == 0)
/*  706 */       return this.openButtonText;
/*  707 */     if (paramJFileChooser.getDialogType() == 1) {
/*  708 */       return this.saveButtonText;
/*      */     }
/*  710 */     return null;
/*      */   }
/*      */ 
/*      */   public Action getNewFolderAction()
/*      */   {
/*  720 */     if (this.newFolderAction == null) {
/*  721 */       this.newFolderAction = new NewFolderAction();
/*      */ 
/*  724 */       if (this.readOnly) {
/*  725 */         this.newFolderAction.setEnabled(false);
/*      */       }
/*      */     }
/*  728 */     return this.newFolderAction;
/*      */   }
/*      */ 
/*      */   public Action getGoHomeAction() {
/*  732 */     return this.goHomeAction;
/*      */   }
/*      */ 
/*      */   public Action getChangeToParentDirectoryAction() {
/*  736 */     return this.changeToParentDirectoryAction;
/*      */   }
/*      */ 
/*      */   public Action getApproveSelectionAction() {
/*  740 */     return this.approveSelectionAction;
/*      */   }
/*      */ 
/*      */   public Action getCancelSelectionAction() {
/*  744 */     return this.cancelSelectionAction;
/*      */   }
/*      */ 
/*      */   public Action getUpdateAction() {
/*  748 */     return this.updateAction;
/*      */   }
/*      */ 
/*      */   private void resetGlobFilter()
/*      */   {
/*  980 */     if (this.actualFileFilter != null) {
/*  981 */       JFileChooser localJFileChooser = getFileChooser();
/*  982 */       FileFilter localFileFilter = localJFileChooser.getFileFilter();
/*  983 */       if ((localFileFilter != null) && (localFileFilter.equals(this.globFilter))) {
/*  984 */         localJFileChooser.setFileFilter(this.actualFileFilter);
/*  985 */         localJFileChooser.removeChoosableFileFilter(this.globFilter);
/*      */       }
/*  987 */       this.actualFileFilter = null;
/*      */     }
/*      */   }
/*      */ 
/*      */   private static boolean isGlobPattern(String paramString) {
/*  992 */     return ((File.separatorChar == '\\') && ((paramString.indexOf('*') >= 0) || (paramString.indexOf('?') >= 0))) || ((File.separatorChar == '/') && ((paramString.indexOf('*') >= 0) || (paramString.indexOf('?') >= 0) || (paramString.indexOf('[') >= 0)));
/*      */   }
/*      */ 
/*      */   private void changeDirectory(File paramFile)
/*      */   {
/* 1147 */     JFileChooser localJFileChooser = getFileChooser();
/*      */ 
/* 1149 */     if ((paramFile != null) && (FilePane.usesShellFolder(localJFileChooser))) {
/*      */       try {
/* 1151 */         ShellFolder localShellFolder1 = ShellFolder.getShellFolder(paramFile);
/*      */ 
/* 1153 */         if (localShellFolder1.isLink()) {
/* 1154 */           ShellFolder localShellFolder2 = localShellFolder1.getLinkLocation();
/*      */ 
/* 1157 */           if (localShellFolder2 != null) {
/* 1158 */             if (localJFileChooser.isTraversable(localShellFolder2)) {
/* 1159 */               paramFile = localShellFolder2;
/*      */             }
/*      */           }
/*      */           else
/*      */           {
/* 1164 */             paramFile = localShellFolder1;
/*      */           }
/*      */         }
/*      */       } catch (FileNotFoundException localFileNotFoundException) {
/* 1168 */         return;
/*      */       }
/*      */     }
/* 1171 */     localJFileChooser.setCurrentDirectory(paramFile);
/* 1172 */     if ((localJFileChooser.getFileSelectionMode() == 2) && (localJFileChooser.getFileSystemView().isFileSystem(paramFile)))
/*      */     {
/* 1175 */       setFileName(paramFile.getAbsolutePath());
/*      */     }
/*      */   }
/*      */ 
/*      */   protected class AcceptAllFileFilter extends FileFilter
/*      */   {
/*      */     public AcceptAllFileFilter()
/*      */     {
/*      */     }
/*      */ 
/*      */     public boolean accept(File paramFile)
/*      */     {
/* 1189 */       return true;
/*      */     }
/*      */ 
/*      */     public String getDescription() {
/* 1193 */       return UIManager.getString("FileChooser.acceptAllFileFilterText");
/*      */     }
/*      */   }
/*      */ 
/*      */   protected class ApproveSelectionAction extends AbstractAction
/*      */   {
/*      */     protected ApproveSelectionAction()
/*      */     {
/*  822 */       super();
/*      */     }
/*      */     public void actionPerformed(ActionEvent paramActionEvent) {
/*  825 */       if (BasicFileChooserUI.this.isDirectorySelected()) {
/*  826 */         localObject1 = BasicFileChooserUI.this.getDirectory();
/*  827 */         if (localObject1 != null)
/*      */         {
/*      */           try {
/*  830 */             localObject1 = ShellFolder.getNormalizedFile((File)localObject1);
/*      */           }
/*      */           catch (IOException localIOException) {
/*      */           }
/*  834 */           BasicFileChooserUI.this.changeDirectory((File)localObject1);
/*  835 */           return;
/*      */         }
/*      */       }
/*      */ 
/*  839 */       Object localObject1 = BasicFileChooserUI.this.getFileChooser();
/*      */ 
/*  841 */       String str1 = BasicFileChooserUI.this.getFileName();
/*  842 */       FileSystemView localFileSystemView = ((JFileChooser)localObject1).getFileSystemView();
/*  843 */       File localFile1 = ((JFileChooser)localObject1).getCurrentDirectory();
/*      */ 
/*  845 */       if (str1 != null)
/*      */       {
/*  847 */         int i = str1.length() - 1;
/*      */ 
/*  849 */         while ((i >= 0) && (str1.charAt(i) <= ' ')) {
/*  850 */           i--;
/*      */         }
/*      */ 
/*  853 */         str1 = str1.substring(0, i + 1);
/*      */       }
/*      */ 
/*  856 */       if ((str1 == null) || (str1.length() == 0))
/*      */       {
/*  858 */         BasicFileChooserUI.this.resetGlobFilter();
/*  859 */         return;
/*      */       }
/*      */ 
/*  862 */       File localFile2 = null;
/*  863 */       File[] arrayOfFile1 = null;
/*      */ 
/*  866 */       if (File.separatorChar == '/')
/*  867 */         if (str1.startsWith("~/"))
/*  868 */           str1 = System.getProperty("user.home") + str1.substring(1);
/*  869 */         else if (str1.equals("~"))
/*  870 */           str1 = System.getProperty("user.home");
/*      */       Object localObject2;
/*  874 */       if ((((JFileChooser)localObject1).isMultiSelectionEnabled()) && (str1.length() > 1) && (str1.charAt(0) == '"') && (str1.charAt(str1.length() - 1) == '"'))
/*      */       {
/*  876 */         localObject2 = new ArrayList();
/*      */ 
/*  878 */         String[] arrayOfString1 = str1.substring(1, str1.length() - 1).split("\" \"");
/*      */ 
/*  880 */         Arrays.sort(arrayOfString1);
/*      */ 
/*  882 */         File[] arrayOfFile2 = null;
/*  883 */         int m = 0;
/*      */ 
/*  885 */         for (String str2 : arrayOfString1) {
/*  886 */           File localFile3 = localFileSystemView.createFileObject(str2);
/*  887 */           if (!localFile3.isAbsolute()) {
/*  888 */             if (arrayOfFile2 == null) {
/*  889 */               arrayOfFile2 = localFileSystemView.getFiles(localFile1, false);
/*  890 */               Arrays.sort(arrayOfFile2);
/*      */             }
/*  892 */             for (int i2 = 0; i2 < arrayOfFile2.length; i2++) {
/*  893 */               int i3 = (m + i2) % arrayOfFile2.length;
/*  894 */               if (arrayOfFile2[i3].getName().equals(str2)) {
/*  895 */                 localFile3 = arrayOfFile2[i3];
/*  896 */                 m = i3 + 1;
/*  897 */                 break;
/*      */               }
/*      */             }
/*      */           }
/*  901 */           ((List)localObject2).add(localFile3);
/*      */         }
/*      */ 
/*  904 */         if (!((List)localObject2).isEmpty()) {
/*  905 */           arrayOfFile1 = (File[])((List)localObject2).toArray(new File[((List)localObject2).size()]);
/*      */         }
/*  907 */         BasicFileChooserUI.this.resetGlobFilter();
/*      */       } else {
/*  909 */         localFile2 = localFileSystemView.createFileObject(str1);
/*  910 */         if (!localFile2.isAbsolute()) {
/*  911 */           localFile2 = localFileSystemView.getChild(localFile1, str1);
/*      */         }
/*      */ 
/*  914 */         localObject2 = ((JFileChooser)localObject1).getFileFilter();
/*  915 */         if ((!localFile2.exists()) && (BasicFileChooserUI.isGlobPattern(str1))) {
/*  916 */           BasicFileChooserUI.this.changeDirectory(localFile2.getParentFile());
/*  917 */           if (BasicFileChooserUI.this.globFilter == null)
/*  918 */             BasicFileChooserUI.this.globFilter = new BasicFileChooserUI.GlobFilter(BasicFileChooserUI.this);
/*      */           try
/*      */           {
/*  921 */             BasicFileChooserUI.this.globFilter.setPattern(localFile2.getName());
/*  922 */             if (!(localObject2 instanceof BasicFileChooserUI.GlobFilter)) {
/*  923 */               BasicFileChooserUI.this.actualFileFilter = ((FileFilter)localObject2);
/*      */             }
/*  925 */             ((JFileChooser)localObject1).setFileFilter(null);
/*  926 */             ((JFileChooser)localObject1).setFileFilter(BasicFileChooserUI.this.globFilter);
/*  927 */             return;
/*      */           }
/*      */           catch (PatternSyntaxException localPatternSyntaxException)
/*      */           {
/*      */           }
/*      */         }
/*  933 */         BasicFileChooserUI.this.resetGlobFilter();
/*      */ 
/*  936 */         int j = (localFile2 != null) && (localFile2.isDirectory()) ? 1 : 0;
/*  937 */         int k = (localFile2 != null) && (((JFileChooser)localObject1).isTraversable(localFile2)) ? 1 : 0;
/*  938 */         boolean bool1 = ((JFileChooser)localObject1).isDirectorySelectionEnabled();
/*  939 */         boolean bool2 = ((JFileChooser)localObject1).isFileSelectionEnabled();
/*  940 */         ??? = (paramActionEvent != null) && ((paramActionEvent.getModifiers() & Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()) != 0) ? 1 : 0;
/*      */ 
/*  943 */         if ((j != 0) && (k != 0) && ((??? != 0) || (!bool1))) {
/*  944 */           BasicFileChooserUI.this.changeDirectory(localFile2);
/*  945 */           return;
/*  946 */         }if (((j != 0) || (!bool2)) && ((j == 0) || (!bool1)) && ((!bool1) || (localFile2.exists())))
/*      */         {
/*  949 */           localFile2 = null;
/*      */         }
/*      */       }
/*      */ 
/*  953 */       if ((arrayOfFile1 != null) || (localFile2 != null)) {
/*  954 */         if ((arrayOfFile1 != null) || (((JFileChooser)localObject1).isMultiSelectionEnabled())) {
/*  955 */           if (arrayOfFile1 == null) {
/*  956 */             arrayOfFile1 = new File[] { localFile2 };
/*      */           }
/*  958 */           ((JFileChooser)localObject1).setSelectedFiles(arrayOfFile1);
/*      */ 
/*  962 */           ((JFileChooser)localObject1).setSelectedFiles(arrayOfFile1);
/*      */         } else {
/*  964 */           ((JFileChooser)localObject1).setSelectedFile(localFile2);
/*      */         }
/*  966 */         ((JFileChooser)localObject1).approveSelection();
/*      */       } else {
/*  968 */         if (((JFileChooser)localObject1).isMultiSelectionEnabled())
/*  969 */           ((JFileChooser)localObject1).setSelectedFiles(null);
/*      */         else {
/*  971 */           ((JFileChooser)localObject1).setSelectedFile(null);
/*      */         }
/*  973 */         ((JFileChooser)localObject1).cancelSelection();
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   protected class BasicFileView extends FileView
/*      */   {
/* 1204 */     protected Hashtable<File, Icon> iconCache = new Hashtable();
/*      */ 
/*      */     public BasicFileView() {
/*      */     }
/*      */ 
/*      */     public void clearIconCache() {
/* 1210 */       this.iconCache = new Hashtable();
/*      */     }
/*      */ 
/*      */     public String getName(File paramFile)
/*      */     {
/* 1215 */       String str = null;
/* 1216 */       if (paramFile != null) {
/* 1217 */         str = BasicFileChooserUI.this.getFileChooser().getFileSystemView().getSystemDisplayName(paramFile);
/*      */       }
/* 1219 */       return str;
/*      */     }
/*      */ 
/*      */     public String getDescription(File paramFile)
/*      */     {
/* 1224 */       return paramFile.getName();
/*      */     }
/*      */ 
/*      */     public String getTypeDescription(File paramFile) {
/* 1228 */       String str = BasicFileChooserUI.this.getFileChooser().getFileSystemView().getSystemTypeDescription(paramFile);
/* 1229 */       if (str == null) {
/* 1230 */         if (paramFile.isDirectory())
/* 1231 */           str = BasicFileChooserUI.this.directoryDescriptionText;
/*      */         else {
/* 1233 */           str = BasicFileChooserUI.this.fileDescriptionText;
/*      */         }
/*      */       }
/* 1236 */       return str;
/*      */     }
/*      */ 
/*      */     public Icon getCachedIcon(File paramFile) {
/* 1240 */       return (Icon)this.iconCache.get(paramFile);
/*      */     }
/*      */ 
/*      */     public void cacheIcon(File paramFile, Icon paramIcon) {
/* 1244 */       if ((paramFile == null) || (paramIcon == null)) {
/* 1245 */         return;
/*      */       }
/* 1247 */       this.iconCache.put(paramFile, paramIcon);
/*      */     }
/*      */ 
/*      */     public Icon getIcon(File paramFile) {
/* 1251 */       Icon localIcon = getCachedIcon(paramFile);
/* 1252 */       if (localIcon != null) {
/* 1253 */         return localIcon;
/*      */       }
/* 1255 */       localIcon = BasicFileChooserUI.this.fileIcon;
/* 1256 */       if (paramFile != null) {
/* 1257 */         FileSystemView localFileSystemView = BasicFileChooserUI.this.getFileChooser().getFileSystemView();
/*      */ 
/* 1259 */         if (localFileSystemView.isFloppyDrive(paramFile))
/* 1260 */           localIcon = BasicFileChooserUI.this.floppyDriveIcon;
/* 1261 */         else if (localFileSystemView.isDrive(paramFile))
/* 1262 */           localIcon = BasicFileChooserUI.this.hardDriveIcon;
/* 1263 */         else if (localFileSystemView.isComputerNode(paramFile))
/* 1264 */           localIcon = BasicFileChooserUI.this.computerIcon;
/* 1265 */         else if (paramFile.isDirectory()) {
/* 1266 */           localIcon = BasicFileChooserUI.this.directoryIcon;
/*      */         }
/*      */       }
/* 1269 */       cacheIcon(paramFile, localIcon);
/* 1270 */       return localIcon;
/*      */     }
/*      */ 
/*      */     public Boolean isHidden(File paramFile) {
/* 1274 */       String str = paramFile.getName();
/* 1275 */       if ((str != null) && (str.charAt(0) == '.')) {
/* 1276 */         return Boolean.TRUE;
/*      */       }
/* 1278 */       return Boolean.FALSE;
/*      */     }
/*      */   }
/*      */ 
/*      */   protected class CancelSelectionAction extends AbstractAction
/*      */   {
/*      */     protected CancelSelectionAction()
/*      */     {
/*      */     }
/*      */ 
/*      */     public void actionPerformed(ActionEvent paramActionEvent)
/*      */     {
/* 1130 */       BasicFileChooserUI.this.getFileChooser().cancelSelection();
/*      */     }
/*      */   }
/*      */ 
/*      */   protected class ChangeToParentDirectoryAction extends AbstractAction
/*      */   {
/*      */     protected ChangeToParentDirectoryAction()
/*      */     {
/*  809 */       super();
/*  810 */       putValue("ActionCommandKey", "Go Up");
/*      */     }
/*      */     public void actionPerformed(ActionEvent paramActionEvent) {
/*  813 */       BasicFileChooserUI.this.getFileChooser().changeToParentDirectory();
/*      */     }
/*      */   }
/*      */ 
/*      */   protected class DoubleClickListener extends MouseAdapter
/*      */   {
/*      */     BasicFileChooserUI.Handler handler;
/*      */ 
/*      */     public DoubleClickListener(JList arg2)
/*      */     {
/*      */       JList localJList;
/*  574 */       this.handler = new BasicFileChooserUI.Handler(BasicFileChooserUI.this, localJList);
/*      */     }
/*      */ 
/*      */     public void mouseEntered(MouseEvent paramMouseEvent)
/*      */     {
/*  585 */       this.handler.mouseEntered(paramMouseEvent);
/*      */     }
/*      */ 
/*      */     public void mouseClicked(MouseEvent paramMouseEvent) {
/*  589 */       this.handler.mouseClicked(paramMouseEvent);
/*      */     }
/*      */   }
/*      */ 
/*      */   static class FileTransferHandler extends TransferHandler
/*      */     implements UIResource
/*      */   {
/*      */     protected Transferable createTransferable(JComponent paramJComponent)
/*      */     {
/* 1301 */       Object[] arrayOfObject1 = null;
/* 1302 */       if ((paramJComponent instanceof JList)) {
/* 1303 */         arrayOfObject1 = ((JList)paramJComponent).getSelectedValues();
/* 1304 */       } else if ((paramJComponent instanceof JTable)) {
/* 1305 */         localObject1 = (JTable)paramJComponent;
/* 1306 */         localObject2 = ((JTable)localObject1).getSelectedRows();
/* 1307 */         if (localObject2 != null) {
/* 1308 */           arrayOfObject1 = new Object[localObject2.length];
/* 1309 */           for (int i = 0; i < localObject2.length; i++) {
/* 1310 */             arrayOfObject1[i] = ((JTable)localObject1).getValueAt(localObject2[i], 0);
/*      */           }
/*      */         }
/*      */       }
/* 1314 */       if ((arrayOfObject1 == null) || (arrayOfObject1.length == 0)) {
/* 1315 */         return null;
/*      */       }
/*      */ 
/* 1318 */       Object localObject1 = new StringBuffer();
/* 1319 */       Object localObject2 = new StringBuffer();
/*      */ 
/* 1321 */       ((StringBuffer)localObject2).append("<html>\n<body>\n<ul>\n");
/*      */ 
/* 1323 */       for (Object localObject3 : arrayOfObject1) {
/* 1324 */         String str = localObject3 == null ? "" : localObject3.toString();
/* 1325 */         ((StringBuffer)localObject1).append(str + "\n");
/* 1326 */         ((StringBuffer)localObject2).append("  <li>" + str + "\n");
/*      */       }
/*      */ 
/* 1330 */       ((StringBuffer)localObject1).deleteCharAt(((StringBuffer)localObject1).length() - 1);
/* 1331 */       ((StringBuffer)localObject2).append("</ul>\n</body>\n</html>");
/*      */ 
/* 1333 */       return new FileTransferable(((StringBuffer)localObject1).toString(), ((StringBuffer)localObject2).toString(), arrayOfObject1);
/*      */     }
/*      */ 
/*      */     public int getSourceActions(JComponent paramJComponent) {
/* 1337 */       return 1;
/*      */     }
/*      */ 
/*      */     static class FileTransferable extends BasicTransferable
/*      */     {
/*      */       Object[] fileData;
/*      */ 
/*      */       FileTransferable(String paramString1, String paramString2, Object[] paramArrayOfObject) {
/* 1345 */         super(paramString2);
/* 1346 */         this.fileData = paramArrayOfObject;
/*      */       }
/*      */ 
/*      */       protected DataFlavor[] getRicherFlavors()
/*      */       {
/* 1353 */         DataFlavor[] arrayOfDataFlavor = new DataFlavor[1];
/* 1354 */         arrayOfDataFlavor[0] = DataFlavor.javaFileListFlavor;
/* 1355 */         return arrayOfDataFlavor;
/*      */       }
/*      */ 
/*      */       protected Object getRicherData(DataFlavor paramDataFlavor)
/*      */       {
/* 1362 */         if (DataFlavor.javaFileListFlavor.equals(paramDataFlavor)) {
/* 1363 */           ArrayList localArrayList = new ArrayList();
/* 1364 */           for (Object localObject : this.fileData) {
/* 1365 */             localArrayList.add(localObject);
/*      */           }
/* 1367 */           return localArrayList;
/*      */         }
/* 1369 */         return null;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   class GlobFilter extends FileFilter
/*      */   {
/*      */     Pattern pattern;
/*      */     String globPattern;
/*      */ 
/*      */     GlobFilter()
/*      */     {
/*      */     }
/*      */ 
/*      */     public void setPattern(String paramString)
/*      */     {
/* 1008 */       char[] arrayOfChar1 = paramString.toCharArray();
/* 1009 */       char[] arrayOfChar2 = new char[arrayOfChar1.length * 2];
/* 1010 */       int i = File.separatorChar == '\\' ? 1 : 0;
/* 1011 */       int j = 0;
/* 1012 */       int k = 0;
/*      */ 
/* 1014 */       this.globPattern = paramString;
/*      */       int m;
/* 1016 */       if (i != 0)
/*      */       {
/* 1018 */         m = arrayOfChar1.length;
/* 1019 */         if (paramString.endsWith("*.*")) {
/* 1020 */           m -= 2;
/*      */         }
/* 1022 */         for (int n = 0; n < m; n++)
/* 1023 */           switch (arrayOfChar1[n]) {
/*      */           case '*':
/* 1025 */             arrayOfChar2[(k++)] = '.';
/* 1026 */             arrayOfChar2[(k++)] = '*';
/* 1027 */             break;
/*      */           case '?':
/* 1030 */             arrayOfChar2[(k++)] = '.';
/* 1031 */             break;
/*      */           case '\\':
/* 1034 */             arrayOfChar2[(k++)] = '\\';
/* 1035 */             arrayOfChar2[(k++)] = '\\';
/* 1036 */             break;
/*      */           default:
/* 1039 */             if ("+()^$.{}[]".indexOf(arrayOfChar1[n]) >= 0) {
/* 1040 */               arrayOfChar2[(k++)] = '\\';
/*      */             }
/* 1042 */             arrayOfChar2[(k++)] = arrayOfChar1[n];
/*      */           }
/*      */       }
/*      */       else
/*      */       {
/* 1047 */         for (m = 0; m < arrayOfChar1.length; m++) {
/* 1048 */           switch (arrayOfChar1[m]) {
/*      */           case '*':
/* 1050 */             if (j == 0) {
/* 1051 */               arrayOfChar2[(k++)] = '.';
/*      */             }
/* 1053 */             arrayOfChar2[(k++)] = '*';
/* 1054 */             break;
/*      */           case '?':
/* 1057 */             arrayOfChar2[(k++)] = (j != 0 ? 63 : '.');
/* 1058 */             break;
/*      */           case '[':
/* 1061 */             j = 1;
/* 1062 */             arrayOfChar2[(k++)] = arrayOfChar1[m];
/*      */ 
/* 1064 */             if (m < arrayOfChar1.length - 1)
/* 1065 */               switch (arrayOfChar1[(m + 1)]) {
/*      */               case '!':
/*      */               case '^':
/* 1068 */                 arrayOfChar2[(k++)] = '^';
/* 1069 */                 m++;
/* 1070 */                 break;
/*      */               case ']':
/* 1073 */                 arrayOfChar2[(k++)] = arrayOfChar1[(++m)];
/*      */               }
/* 1074 */             break;
/*      */           case ']':
/* 1080 */             arrayOfChar2[(k++)] = arrayOfChar1[m];
/* 1081 */             j = 0;
/* 1082 */             break;
/*      */           case '\\':
/* 1085 */             if ((m == 0) && (arrayOfChar1.length > 1) && (arrayOfChar1[1] == '~')) {
/* 1086 */               arrayOfChar2[(k++)] = arrayOfChar1[(++m)];
/*      */             } else {
/* 1088 */               arrayOfChar2[(k++)] = '\\';
/* 1089 */               if ((m < arrayOfChar1.length - 1) && ("*?[]".indexOf(arrayOfChar1[(m + 1)]) >= 0))
/* 1090 */                 arrayOfChar2[(k++)] = arrayOfChar1[(++m)];
/*      */               else {
/* 1092 */                 arrayOfChar2[(k++)] = '\\';
/*      */               }
/*      */             }
/* 1095 */             break;
/*      */           default:
/* 1099 */             if (!Character.isLetterOrDigit(arrayOfChar1[m])) {
/* 1100 */               arrayOfChar2[(k++)] = '\\';
/*      */             }
/* 1102 */             arrayOfChar2[(k++)] = arrayOfChar1[m];
/*      */           }
/*      */         }
/*      */       }
/*      */ 
/* 1107 */       this.pattern = Pattern.compile(new String(arrayOfChar2, 0, k), 2);
/*      */     }
/*      */ 
/*      */     public boolean accept(File paramFile) {
/* 1111 */       if (paramFile == null) {
/* 1112 */         return false;
/*      */       }
/* 1114 */       if (paramFile.isDirectory()) {
/* 1115 */         return true;
/*      */       }
/* 1117 */       return this.pattern.matcher(paramFile.getName()).matches();
/*      */     }
/*      */ 
/*      */     public String getDescription() {
/* 1121 */       return this.globPattern;
/*      */     }
/*      */   }
/*      */ 
/*      */   protected class GoHomeAction extends AbstractAction
/*      */   {
/*      */     protected GoHomeAction()
/*      */     {
/*  799 */       super();
/*      */     }
/*      */     public void actionPerformed(ActionEvent paramActionEvent) {
/*  802 */       JFileChooser localJFileChooser = BasicFileChooserUI.this.getFileChooser();
/*  803 */       BasicFileChooserUI.this.changeDirectory(localJFileChooser.getFileSystemView().getHomeDirectory());
/*      */     }
/*      */   }
/*      */ 
/*      */   private class Handler
/*      */     implements MouseListener, ListSelectionListener
/*      */   {
/*      */     JList list;
/*      */ 
/*      */     Handler()
/*      */     {
/*      */     }
/*      */ 
/*      */     Handler(JList arg2)
/*      */     {
/*      */       Object localObject;
/*  454 */       this.list = localObject;
/*      */     }
/*      */ 
/*      */     public void mouseClicked(MouseEvent paramMouseEvent)
/*      */     {
/*  460 */       if ((this.list != null) && (SwingUtilities.isLeftMouseButton(paramMouseEvent)) && (paramMouseEvent.getClickCount() % 2 == 0))
/*      */       {
/*  464 */         int i = SwingUtilities2.loc2IndexFileList(this.list, paramMouseEvent.getPoint());
/*  465 */         if (i >= 0) {
/*  466 */           File localFile = (File)this.list.getModel().getElementAt(i);
/*      */           try
/*      */           {
/*  469 */             localFile = ShellFolder.getNormalizedFile(localFile);
/*      */           }
/*      */           catch (IOException localIOException) {
/*      */           }
/*  473 */           if (BasicFileChooserUI.this.getFileChooser().isTraversable(localFile)) {
/*  474 */             this.list.clearSelection();
/*  475 */             BasicFileChooserUI.this.changeDirectory(localFile);
/*      */           } else {
/*  477 */             BasicFileChooserUI.this.getFileChooser().approveSelection();
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     public void mouseEntered(MouseEvent paramMouseEvent) {
/*  484 */       if (this.list != null) {
/*  485 */         TransferHandler localTransferHandler1 = BasicFileChooserUI.this.getFileChooser().getTransferHandler();
/*  486 */         TransferHandler localTransferHandler2 = this.list.getTransferHandler();
/*  487 */         if (localTransferHandler1 != localTransferHandler2) {
/*  488 */           this.list.setTransferHandler(localTransferHandler1);
/*      */         }
/*  490 */         if (BasicFileChooserUI.this.getFileChooser().getDragEnabled() != this.list.getDragEnabled())
/*  491 */           this.list.setDragEnabled(BasicFileChooserUI.this.getFileChooser().getDragEnabled());
/*      */       }
/*      */     }
/*      */ 
/*      */     public void mouseExited(MouseEvent paramMouseEvent)
/*      */     {
/*      */     }
/*      */ 
/*      */     public void mousePressed(MouseEvent paramMouseEvent) {
/*      */     }
/*      */ 
/*      */     public void mouseReleased(MouseEvent paramMouseEvent) {
/*      */     }
/*      */ 
/*      */     public void valueChanged(ListSelectionEvent paramListSelectionEvent) {
/*  506 */       if (!paramListSelectionEvent.getValueIsAdjusting()) {
/*  507 */         JFileChooser localJFileChooser = BasicFileChooserUI.this.getFileChooser();
/*  508 */         FileSystemView localFileSystemView = localJFileChooser.getFileSystemView();
/*  509 */         JList localJList = (JList)paramListSelectionEvent.getSource();
/*      */ 
/*  511 */         int i = localJFileChooser.getFileSelectionMode();
/*  512 */         int j = (BasicFileChooserUI.this.usesSingleFilePane) && (i == 0) ? 1 : 0;
/*      */         Object localObject1;
/*  515 */         if (localJFileChooser.isMultiSelectionEnabled()) {
/*  516 */           localObject1 = null;
/*  517 */           Object[] arrayOfObject1 = localJList.getSelectedValues();
/*  518 */           if (arrayOfObject1 != null) {
/*  519 */             if ((arrayOfObject1.length == 1) && (((File)arrayOfObject1[0]).isDirectory()) && (localJFileChooser.isTraversable((File)arrayOfObject1[0])) && ((j != 0) || (!localFileSystemView.isFileSystem((File)arrayOfObject1[0]))))
/*      */             {
/*  523 */               BasicFileChooserUI.this.setDirectorySelected(true);
/*  524 */               BasicFileChooserUI.this.setDirectory((File)arrayOfObject1[0]);
/*      */             } else {
/*  526 */               ArrayList localArrayList = new ArrayList(arrayOfObject1.length);
/*  527 */               for (Object localObject2 : arrayOfObject1) {
/*  528 */                 File localFile = (File)localObject2;
/*  529 */                 boolean bool = localFile.isDirectory();
/*  530 */                 if (((localJFileChooser.isFileSelectionEnabled()) && (!bool)) || ((localJFileChooser.isDirectorySelectionEnabled()) && (localFileSystemView.isFileSystem(localFile)) && (bool)))
/*      */                 {
/*  534 */                   localArrayList.add(localFile);
/*      */                 }
/*      */               }
/*  537 */               if (localArrayList.size() > 0) {
/*  538 */                 localObject1 = (File[])localArrayList.toArray(new File[localArrayList.size()]);
/*      */               }
/*  540 */               BasicFileChooserUI.this.setDirectorySelected(false);
/*      */             }
/*      */           }
/*  543 */           localJFileChooser.setSelectedFiles((File[])localObject1);
/*      */         } else {
/*  545 */           localObject1 = (File)localJList.getSelectedValue();
/*  546 */           if ((localObject1 != null) && (((File)localObject1).isDirectory()) && (localJFileChooser.isTraversable((File)localObject1)) && ((j != 0) || (!localFileSystemView.isFileSystem((File)localObject1))))
/*      */           {
/*  551 */             BasicFileChooserUI.this.setDirectorySelected(true);
/*  552 */             BasicFileChooserUI.this.setDirectory((File)localObject1);
/*  553 */             if (BasicFileChooserUI.this.usesSingleFilePane)
/*  554 */               localJFileChooser.setSelectedFile(null);
/*      */           }
/*      */           else {
/*  557 */             BasicFileChooserUI.this.setDirectorySelected(false);
/*  558 */             if (localObject1 != null)
/*  559 */               localJFileChooser.setSelectedFile((File)localObject1);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   protected class NewFolderAction extends AbstractAction
/*      */   {
/*      */     protected NewFolderAction()
/*      */     {
/*  757 */       super();
/*      */     }
/*      */     public void actionPerformed(ActionEvent paramActionEvent) {
/*  760 */       if (BasicFileChooserUI.this.readOnly) {
/*  761 */         return;
/*      */       }
/*  763 */       JFileChooser localJFileChooser = BasicFileChooserUI.this.getFileChooser();
/*  764 */       File localFile1 = localJFileChooser.getCurrentDirectory();
/*      */ 
/*  766 */       if (!localFile1.exists()) {
/*  767 */         JOptionPane.showMessageDialog(localJFileChooser, BasicFileChooserUI.this.newFolderParentDoesntExistText, BasicFileChooserUI.this.newFolderParentDoesntExistTitleText, 2);
/*      */ 
/*  771 */         return;
/*      */       }
/*      */ 
/*      */       try
/*      */       {
/*  776 */         File localFile2 = localJFileChooser.getFileSystemView().createNewFolder(localFile1);
/*  777 */         if (localJFileChooser.isMultiSelectionEnabled())
/*  778 */           localJFileChooser.setSelectedFiles(new File[] { localFile2 });
/*      */         else
/*  780 */           localJFileChooser.setSelectedFile(localFile2);
/*      */       }
/*      */       catch (IOException localIOException) {
/*  783 */         JOptionPane.showMessageDialog(localJFileChooser, BasicFileChooserUI.this.newFolderErrorText + BasicFileChooserUI.this.newFolderErrorSeparator + localIOException, BasicFileChooserUI.this.newFolderErrorText, 0);
/*      */ 
/*  787 */         return;
/*      */       }
/*      */ 
/*  790 */       localJFileChooser.rescanCurrentDirectory();
/*      */     }
/*      */   }
/*      */ 
/*      */   protected class SelectionListener
/*      */     implements ListSelectionListener
/*      */   {
/*      */     protected SelectionListener()
/*      */     {
/*      */     }
/*      */ 
/*      */     public void valueChanged(ListSelectionEvent paramListSelectionEvent)
/*      */     {
/*  599 */       BasicFileChooserUI.this.getHandler().valueChanged(paramListSelectionEvent);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected class UpdateAction extends AbstractAction
/*      */   {
/*      */     protected UpdateAction()
/*      */     {
/*      */     }
/*      */ 
/*      */     public void actionPerformed(ActionEvent paramActionEvent)
/*      */     {
/* 1139 */       JFileChooser localJFileChooser = BasicFileChooserUI.this.getFileChooser();
/* 1140 */       localJFileChooser.setCurrentDirectory(localJFileChooser.getFileSystemView().createFileObject(BasicFileChooserUI.this.getDirectoryName()));
/* 1141 */       localJFileChooser.rescanCurrentDirectory();
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.basic.BasicFileChooserUI
 * JD-Core Version:    0.6.2
 */