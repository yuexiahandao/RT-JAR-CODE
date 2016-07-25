/*      */ package sun.swing;
/*      */ 
/*      */ import java.awt.BorderLayout;
/*      */ import java.awt.Color;
/*      */ import java.awt.Component;
/*      */ import java.awt.ComponentOrientation;
/*      */ import java.awt.Container;
/*      */ import java.awt.Cursor;
/*      */ import java.awt.DefaultKeyboardFocusManager;
/*      */ import java.awt.Dimension;
/*      */ import java.awt.Font;
/*      */ import java.awt.Insets;
/*      */ import java.awt.KeyboardFocusManager;
/*      */ import java.awt.Point;
/*      */ import java.awt.Rectangle;
/*      */ import java.awt.event.ActionEvent;
/*      */ import java.awt.event.ActionListener;
/*      */ import java.awt.event.ComponentAdapter;
/*      */ import java.awt.event.ComponentEvent;
/*      */ import java.awt.event.FocusAdapter;
/*      */ import java.awt.event.FocusEvent;
/*      */ import java.awt.event.FocusListener;
/*      */ import java.awt.event.KeyAdapter;
/*      */ import java.awt.event.KeyEvent;
/*      */ import java.awt.event.KeyListener;
/*      */ import java.awt.event.MouseAdapter;
/*      */ import java.awt.event.MouseEvent;
/*      */ import java.awt.event.MouseListener;
/*      */ import java.beans.PropertyChangeEvent;
/*      */ import java.beans.PropertyChangeListener;
/*      */ import java.io.File;
/*      */ import java.io.FileNotFoundException;
/*      */ import java.text.DateFormat;
/*      */ import java.text.MessageFormat;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Comparator;
/*      */ import java.util.Date;
/*      */ import java.util.List;
/*      */ import java.util.Locale;
/*      */ import java.util.concurrent.Callable;
/*      */ import javax.swing.AbstractAction;
/*      */ import javax.swing.AbstractListModel;
/*      */ import javax.swing.Action;
/*      */ import javax.swing.ActionMap;
/*      */ import javax.swing.ButtonGroup;
/*      */ import javax.swing.DefaultCellEditor;
/*      */ import javax.swing.DefaultListCellRenderer;
/*      */ import javax.swing.DefaultListSelectionModel;
/*      */ import javax.swing.DefaultRowSorter.ModelWrapper;
/*      */ import javax.swing.Icon;
/*      */ import javax.swing.InputMap;
/*      */ import javax.swing.JComponent;
/*      */ import javax.swing.JFileChooser;
/*      */ import javax.swing.JLabel;
/*      */ import javax.swing.JList;
/*      */ import javax.swing.JMenu;
/*      */ import javax.swing.JOptionPane;
/*      */ import javax.swing.JPanel;
/*      */ import javax.swing.JPopupMenu;
/*      */ import javax.swing.JRadioButtonMenuItem;
/*      */ import javax.swing.JScrollPane;
/*      */ import javax.swing.JTable;
/*      */ import javax.swing.JTextField;
/*      */ import javax.swing.JViewport;
/*      */ import javax.swing.KeyStroke;
/*      */ import javax.swing.ListModel;
/*      */ import javax.swing.ListSelectionModel;
/*      */ import javax.swing.LookAndFeel;
/*      */ import javax.swing.RowSorter.SortKey;
/*      */ import javax.swing.SwingUtilities;
/*      */ import javax.swing.TransferHandler;
/*      */ import javax.swing.UIManager;
/*      */ import javax.swing.border.Border;
/*      */ import javax.swing.event.ListDataEvent;
/*      */ import javax.swing.event.ListDataListener;
/*      */ import javax.swing.event.ListSelectionListener;
/*      */ import javax.swing.event.RowSorterEvent;
/*      */ import javax.swing.event.RowSorterListener;
/*      */ import javax.swing.event.TableModelEvent;
/*      */ import javax.swing.event.TableModelListener;
/*      */ import javax.swing.filechooser.FileSystemView;
/*      */ import javax.swing.plaf.basic.BasicDirectoryModel;
/*      */ import javax.swing.table.AbstractTableModel;
/*      */ import javax.swing.table.DefaultTableCellRenderer;
/*      */ import javax.swing.table.DefaultTableColumnModel;
/*      */ import javax.swing.table.JTableHeader;
/*      */ import javax.swing.table.TableCellEditor;
/*      */ import javax.swing.table.TableCellRenderer;
/*      */ import javax.swing.table.TableColumn;
/*      */ import javax.swing.table.TableColumnModel;
/*      */ import javax.swing.table.TableModel;
/*      */ import javax.swing.table.TableRowSorter;
/*      */ import javax.swing.text.Position.Bias;
/*      */ import sun.awt.shell.ShellFolder;
/*      */ import sun.awt.shell.ShellFolderColumnInfo;
/*      */ 
/*      */ public class FilePane extends JPanel
/*      */   implements PropertyChangeListener
/*      */ {
/*      */   public static final String ACTION_APPROVE_SELECTION = "approveSelection";
/*      */   public static final String ACTION_CANCEL = "cancelSelection";
/*      */   public static final String ACTION_EDIT_FILE_NAME = "editFileName";
/*      */   public static final String ACTION_REFRESH = "refresh";
/*      */   public static final String ACTION_CHANGE_TO_PARENT_DIRECTORY = "Go Up";
/*      */   public static final String ACTION_NEW_FOLDER = "New Folder";
/*      */   public static final String ACTION_VIEW_LIST = "viewTypeList";
/*      */   public static final String ACTION_VIEW_DETAILS = "viewTypeDetails";
/*      */   private Action[] actions;
/*      */   public static final int VIEWTYPE_LIST = 0;
/*      */   public static final int VIEWTYPE_DETAILS = 1;
/*      */   private static final int VIEWTYPE_COUNT = 2;
/*   81 */   private int viewType = -1;
/*   82 */   private JPanel[] viewPanels = new JPanel[2];
/*      */   private JPanel currentViewPanel;
/*      */   private String[] viewTypeActionNames;
/*   86 */   private String filesListAccessibleName = null;
/*   87 */   private String filesDetailsAccessibleName = null;
/*      */   private JPopupMenu contextMenu;
/*      */   private JMenu viewMenu;
/*      */   private String viewMenuLabelText;
/*      */   private String refreshActionLabelText;
/*      */   private String newFolderActionLabelText;
/*      */   private String kiloByteString;
/*      */   private String megaByteString;
/*      */   private String gigaByteString;
/*      */   private String renameErrorTitleText;
/*      */   private String renameErrorText;
/*      */   private String renameErrorFileExistsText;
/*  104 */   private static final Cursor waitCursor = Cursor.getPredefinedCursor(3);
/*      */ 
/*  107 */   private final KeyListener detailsKeyListener = new KeyAdapter()
/*      */   {
/*      */     private final long timeFactor;
/*      */     private final StringBuilder typedString;
/*      */     private long lastTime;
/*      */ 
/*      */     public void keyTyped(KeyEvent paramAnonymousKeyEvent)
/*      */     {
/*  129 */       BasicDirectoryModel localBasicDirectoryModel = FilePane.this.getModel();
/*  130 */       int i = localBasicDirectoryModel.getSize();
/*      */ 
/*  132 */       if ((FilePane.this.detailsTable == null) || (i == 0) || (paramAnonymousKeyEvent.isAltDown()) || (paramAnonymousKeyEvent.isControlDown()) || (paramAnonymousKeyEvent.isMetaDown()))
/*      */       {
/*  134 */         return;
/*      */       }
/*      */ 
/*  137 */       InputMap localInputMap = FilePane.this.detailsTable.getInputMap(1);
/*  138 */       KeyStroke localKeyStroke = KeyStroke.getKeyStrokeForEvent(paramAnonymousKeyEvent);
/*      */ 
/*  140 */       if ((localInputMap != null) && (localInputMap.get(localKeyStroke) != null)) {
/*  141 */         return;
/*      */       }
/*      */ 
/*  144 */       int j = FilePane.this.detailsTable.getSelectionModel().getLeadSelectionIndex();
/*      */ 
/*  146 */       if (j < 0) {
/*  147 */         j = 0;
/*      */       }
/*      */ 
/*  150 */       if (j >= i) {
/*  151 */         j = i - 1;
/*      */       }
/*      */ 
/*  154 */       char c = paramAnonymousKeyEvent.getKeyChar();
/*      */ 
/*  156 */       long l = paramAnonymousKeyEvent.getWhen();
/*      */ 
/*  158 */       if (l - this.lastTime < this.timeFactor) {
/*  159 */         if ((this.typedString.length() == 1) && (this.typedString.charAt(0) == c))
/*      */         {
/*  162 */           j++;
/*      */         }
/*  164 */         else this.typedString.append(c); 
/*      */       }
/*      */       else
/*      */       {
/*  167 */         j++;
/*      */ 
/*  169 */         this.typedString.setLength(0);
/*  170 */         this.typedString.append(c);
/*      */       }
/*      */ 
/*  173 */       this.lastTime = l;
/*      */ 
/*  175 */       if (j >= i) {
/*  176 */         j = 0;
/*      */       }
/*      */ 
/*  180 */       int k = getNextMatch(j, i - 1);
/*      */ 
/*  182 */       if ((k < 0) && (j > 0)) {
/*  183 */         k = getNextMatch(0, j - 1);
/*      */       }
/*      */ 
/*  186 */       if (k >= 0) {
/*  187 */         FilePane.this.detailsTable.getSelectionModel().setSelectionInterval(k, k);
/*      */ 
/*  189 */         Rectangle localRectangle = FilePane.this.detailsTable.getCellRect(k, FilePane.this.detailsTable.convertColumnIndexToView(0), false);
/*      */ 
/*  191 */         FilePane.this.detailsTable.scrollRectToVisible(localRectangle);
/*      */       }
/*      */     }
/*      */ 
/*      */     private int getNextMatch(int paramAnonymousInt1, int paramAnonymousInt2) {
/*  196 */       BasicDirectoryModel localBasicDirectoryModel = FilePane.this.getModel();
/*  197 */       JFileChooser localJFileChooser = FilePane.this.getFileChooser();
/*  198 */       FilePane.DetailsTableRowSorter localDetailsTableRowSorter = FilePane.this.getRowSorter();
/*      */ 
/*  200 */       String str1 = this.typedString.toString().toLowerCase();
/*      */ 
/*  203 */       for (int i = paramAnonymousInt1; i <= paramAnonymousInt2; i++) {
/*  204 */         File localFile = (File)localBasicDirectoryModel.getElementAt(localDetailsTableRowSorter.convertRowIndexToModel(i));
/*      */ 
/*  206 */         String str2 = localJFileChooser.getName(localFile).toLowerCase();
/*      */ 
/*  208 */         if (str2.startsWith(str1)) {
/*  209 */           return i;
/*      */         }
/*      */       }
/*      */ 
/*  213 */       return -1;
/*      */     }
/*  107 */   };
/*      */ 
/*  217 */   private FocusListener editorFocusListener = new FocusAdapter() {
/*      */     public void focusLost(FocusEvent paramAnonymousFocusEvent) {
/*  219 */       if (!paramAnonymousFocusEvent.isTemporary())
/*  220 */         FilePane.this.applyEdit();
/*      */     }
/*  217 */   };
/*      */ 
/*  225 */   private static FocusListener repaintListener = new FocusListener() {
/*      */     public void focusGained(FocusEvent paramAnonymousFocusEvent) {
/*  227 */       repaintSelection(paramAnonymousFocusEvent.getSource());
/*      */     }
/*      */ 
/*      */     public void focusLost(FocusEvent paramAnonymousFocusEvent) {
/*  231 */       repaintSelection(paramAnonymousFocusEvent.getSource());
/*      */     }
/*      */ 
/*      */     private void repaintSelection(Object paramAnonymousObject) {
/*  235 */       if ((paramAnonymousObject instanceof JList))
/*  236 */         repaintListSelection((JList)paramAnonymousObject);
/*  237 */       else if ((paramAnonymousObject instanceof JTable))
/*  238 */         repaintTableSelection((JTable)paramAnonymousObject);
/*      */     }
/*      */ 
/*      */     private void repaintListSelection(JList paramAnonymousJList)
/*      */     {
/*  243 */       int[] arrayOfInt1 = paramAnonymousJList.getSelectedIndices();
/*  244 */       for (int k : arrayOfInt1) {
/*  245 */         Rectangle localRectangle = paramAnonymousJList.getCellBounds(k, k);
/*  246 */         paramAnonymousJList.repaint(localRectangle);
/*      */       }
/*      */     }
/*      */ 
/*      */     private void repaintTableSelection(JTable paramAnonymousJTable) {
/*  251 */       int i = paramAnonymousJTable.getSelectionModel().getMinSelectionIndex();
/*  252 */       int j = paramAnonymousJTable.getSelectionModel().getMaxSelectionIndex();
/*  253 */       if ((i == -1) || (j == -1)) {
/*  254 */         return;
/*      */       }
/*      */ 
/*  257 */       int k = paramAnonymousJTable.convertColumnIndexToView(0);
/*      */ 
/*  259 */       Rectangle localRectangle1 = paramAnonymousJTable.getCellRect(i, k, false);
/*  260 */       Rectangle localRectangle2 = paramAnonymousJTable.getCellRect(j, k, false);
/*  261 */       Rectangle localRectangle3 = localRectangle1.union(localRectangle2);
/*  262 */       paramAnonymousJTable.repaint(localRectangle3); }  } ;
/*      */ 
/*  266 */   private boolean smallIconsView = false;
/*      */   private Border listViewBorder;
/*      */   private Color listViewBackground;
/*      */   private boolean listViewWindowsStyle;
/*      */   private boolean readOnly;
/*  271 */   private boolean fullRowSelection = false;
/*      */   private ListSelectionModel listSelectionModel;
/*      */   private JList list;
/*      */   private JTable detailsTable;
/*      */   private static final int COLUMN_FILENAME = 0;
/*      */   private File newFolderFile;
/*      */   private FileChooserUIAccessor fileChooserUIAccessor;
/*      */   private DetailsTableModel detailsTableModel;
/*      */   private DetailsTableRowSorter rowSorter;
/*      */   private DetailsTableCellEditor tableCellEditor;
/* 1318 */   int lastIndex = -1;
/* 1319 */   File editFile = null;
/*      */ 
/* 1343 */   JTextField editCell = null;
/*      */   protected Action newFolderAction;
/*      */   private Handler handler;
/*      */ 
/*  289 */   public FilePane(FileChooserUIAccessor paramFileChooserUIAccessor) { super(new BorderLayout());
/*      */ 
/*  291 */     this.fileChooserUIAccessor = paramFileChooserUIAccessor;
/*      */ 
/*  293 */     installDefaults();
/*  294 */     createActionMap(); }
/*      */ 
/*      */   public void uninstallUI()
/*      */   {
/*  298 */     if (getModel() != null)
/*  299 */       getModel().removePropertyChangeListener(this);
/*      */   }
/*      */ 
/*      */   protected JFileChooser getFileChooser()
/*      */   {
/*  304 */     return this.fileChooserUIAccessor.getFileChooser();
/*      */   }
/*      */ 
/*      */   protected BasicDirectoryModel getModel() {
/*  308 */     return this.fileChooserUIAccessor.getModel();
/*      */   }
/*      */ 
/*      */   public int getViewType() {
/*  312 */     return this.viewType;
/*      */   }
/*      */ 
/*      */   public void setViewType(int paramInt) {
/*  316 */     if (paramInt == this.viewType) {
/*  317 */       return;
/*      */     }
/*      */ 
/*  320 */     int i = this.viewType;
/*  321 */     this.viewType = paramInt;
/*      */ 
/*  323 */     JPanel localJPanel = null;
/*  324 */     Object localObject = null;
/*      */ 
/*  326 */     switch (paramInt) {
/*      */     case 0:
/*  328 */       if (this.viewPanels[paramInt] == null) {
/*  329 */         localJPanel = this.fileChooserUIAccessor.createList();
/*  330 */         if (localJPanel == null) {
/*  331 */           localJPanel = createList();
/*      */         }
/*      */ 
/*  334 */         this.list = ((JList)findChildComponent(localJPanel, JList.class));
/*  335 */         if (this.listSelectionModel == null) {
/*  336 */           this.listSelectionModel = this.list.getSelectionModel();
/*  337 */           if (this.detailsTable != null)
/*  338 */             this.detailsTable.setSelectionModel(this.listSelectionModel);
/*      */         }
/*      */         else {
/*  341 */           this.list.setSelectionModel(this.listSelectionModel);
/*      */         }
/*      */       }
/*  344 */       this.list.setLayoutOrientation(1);
/*  345 */       localObject = this.list;
/*  346 */       break;
/*      */     case 1:
/*  349 */       if (this.viewPanels[paramInt] == null) {
/*  350 */         localJPanel = this.fileChooserUIAccessor.createDetailsView();
/*  351 */         if (localJPanel == null) {
/*  352 */           localJPanel = createDetailsView();
/*      */         }
/*      */ 
/*  355 */         this.detailsTable = ((JTable)findChildComponent(localJPanel, JTable.class));
/*  356 */         this.detailsTable.setRowHeight(Math.max(this.detailsTable.getFont().getSize() + 4, 17));
/*  357 */         if (this.listSelectionModel != null) {
/*  358 */           this.detailsTable.setSelectionModel(this.listSelectionModel);
/*      */         }
/*      */       }
/*  361 */       localObject = this.detailsTable;
/*      */     }
/*      */ 
/*  365 */     if (localJPanel != null) {
/*  366 */       this.viewPanels[paramInt] = localJPanel;
/*  367 */       recursivelySetInheritsPopupMenu(localJPanel, true);
/*      */     }
/*      */ 
/*  370 */     int j = 0;
/*      */ 
/*  372 */     if (this.currentViewPanel != null) {
/*  373 */       Component localComponent = DefaultKeyboardFocusManager.getCurrentKeyboardFocusManager().getPermanentFocusOwner();
/*      */ 
/*  376 */       j = (localComponent == this.detailsTable) || (localComponent == this.list) ? 1 : 0;
/*      */ 
/*  378 */       remove(this.currentViewPanel);
/*      */     }
/*      */ 
/*  381 */     this.currentViewPanel = this.viewPanels[paramInt];
/*  382 */     add(this.currentViewPanel, "Center");
/*      */ 
/*  384 */     if ((j != 0) && (localObject != null)) {
/*  385 */       ((Component)localObject).requestFocusInWindow();
/*      */     }
/*      */ 
/*  388 */     revalidate();
/*  389 */     repaint();
/*  390 */     updateViewMenu();
/*  391 */     firePropertyChange("viewType", i, paramInt);
/*      */   }
/*      */ 
/*      */   public Action getViewTypeAction(int paramInt)
/*      */   {
/*  416 */     return new ViewTypeAction(paramInt);
/*      */   }
/*      */ 
/*      */   private static void recursivelySetInheritsPopupMenu(Container paramContainer, boolean paramBoolean) {
/*  420 */     if ((paramContainer instanceof JComponent)) {
/*  421 */       ((JComponent)paramContainer).setInheritsPopupMenu(paramBoolean);
/*      */     }
/*  423 */     int i = paramContainer.getComponentCount();
/*  424 */     for (int j = 0; j < i; j++)
/*  425 */       recursivelySetInheritsPopupMenu((Container)paramContainer.getComponent(j), paramBoolean);
/*      */   }
/*      */ 
/*      */   protected void installDefaults()
/*      */   {
/*  430 */     Locale localLocale = getFileChooser().getLocale();
/*      */ 
/*  432 */     this.listViewBorder = UIManager.getBorder("FileChooser.listViewBorder");
/*  433 */     this.listViewBackground = UIManager.getColor("FileChooser.listViewBackground");
/*  434 */     this.listViewWindowsStyle = UIManager.getBoolean("FileChooser.listViewWindowsStyle");
/*  435 */     this.readOnly = UIManager.getBoolean("FileChooser.readOnly");
/*      */ 
/*  439 */     this.viewMenuLabelText = UIManager.getString("FileChooser.viewMenuLabelText", localLocale);
/*      */ 
/*  441 */     this.refreshActionLabelText = UIManager.getString("FileChooser.refreshActionLabelText", localLocale);
/*      */ 
/*  443 */     this.newFolderActionLabelText = UIManager.getString("FileChooser.newFolderActionLabelText", localLocale);
/*      */ 
/*  446 */     this.viewTypeActionNames = new String[2];
/*  447 */     this.viewTypeActionNames[0] = UIManager.getString("FileChooser.listViewActionLabelText", localLocale);
/*      */ 
/*  449 */     this.viewTypeActionNames[1] = UIManager.getString("FileChooser.detailsViewActionLabelText", localLocale);
/*      */ 
/*  452 */     this.kiloByteString = UIManager.getString("FileChooser.fileSizeKiloBytes", localLocale);
/*  453 */     this.megaByteString = UIManager.getString("FileChooser.fileSizeMegaBytes", localLocale);
/*  454 */     this.gigaByteString = UIManager.getString("FileChooser.fileSizeGigaBytes", localLocale);
/*  455 */     this.fullRowSelection = UIManager.getBoolean("FileView.fullRowSelection");
/*      */ 
/*  457 */     this.filesListAccessibleName = UIManager.getString("FileChooser.filesListAccessibleName", localLocale);
/*  458 */     this.filesDetailsAccessibleName = UIManager.getString("FileChooser.filesDetailsAccessibleName", localLocale);
/*      */ 
/*  460 */     this.renameErrorTitleText = UIManager.getString("FileChooser.renameErrorTitleText", localLocale);
/*  461 */     this.renameErrorText = UIManager.getString("FileChooser.renameErrorText", localLocale);
/*  462 */     this.renameErrorFileExistsText = UIManager.getString("FileChooser.renameErrorFileExistsText", localLocale);
/*      */   }
/*      */ 
/*      */   public Action[] getActions()
/*      */   {
/*  472 */     if (this.actions == null)
/*      */     {
/*  518 */       ArrayList localArrayList = new ArrayList(8);
/*      */ 
/*  521 */       localArrayList.add(new AbstractAction()
/*      */       {
/*      */         public void actionPerformed(ActionEvent paramAnonymousActionEvent)
/*      */         {
/*  484 */           String str = (String)getValue("ActionCommandKey");
/*      */ 
/*  486 */           if (str == "cancelSelection") {
/*  487 */             if (FilePane.this.editFile != null)
/*  488 */               FilePane.this.cancelEdit();
/*      */             else
/*  490 */               FilePane.this.getFileChooser().cancelSelection();
/*      */           }
/*  492 */           else if (str == "editFileName") {
/*  493 */             JFileChooser localJFileChooser = FilePane.this.getFileChooser();
/*  494 */             int i = FilePane.this.listSelectionModel.getMinSelectionIndex();
/*  495 */             if ((i >= 0) && (FilePane.this.editFile == null) && ((!localJFileChooser.isMultiSelectionEnabled()) || (localJFileChooser.getSelectedFiles().length <= 1)))
/*      */             {
/*  499 */               FilePane.this.editFileName(i);
/*      */             }
/*  501 */           } else if (str == "refresh") {
/*  502 */             FilePane.this.getFileChooser().rescanCurrentDirectory();
/*      */           }
/*      */         }
/*      */ 
/*      */         public boolean isEnabled() {
/*  507 */           String str = (String)getValue("ActionCommandKey");
/*  508 */           if (str == "cancelSelection")
/*  509 */             return FilePane.this.getFileChooser().isEnabled();
/*  510 */           if (str == "editFileName") {
/*  511 */             return (!FilePane.this.readOnly) && (FilePane.this.getFileChooser().isEnabled());
/*      */           }
/*  513 */           return true;
/*      */         }
/*      */       });
/*  522 */       localArrayList.add(new AbstractAction()
/*      */       {
/*      */         public void actionPerformed(ActionEvent paramAnonymousActionEvent)
/*      */         {
/*  484 */           String str = (String)getValue("ActionCommandKey");
/*      */ 
/*  486 */           if (str == "cancelSelection") {
/*  487 */             if (FilePane.this.editFile != null)
/*  488 */               FilePane.this.cancelEdit();
/*      */             else
/*  490 */               FilePane.this.getFileChooser().cancelSelection();
/*      */           }
/*  492 */           else if (str == "editFileName") {
/*  493 */             JFileChooser localJFileChooser = FilePane.this.getFileChooser();
/*  494 */             int i = FilePane.this.listSelectionModel.getMinSelectionIndex();
/*  495 */             if ((i >= 0) && (FilePane.this.editFile == null) && ((!localJFileChooser.isMultiSelectionEnabled()) || (localJFileChooser.getSelectedFiles().length <= 1)))
/*      */             {
/*  499 */               FilePane.this.editFileName(i);
/*      */             }
/*  501 */           } else if (str == "refresh") {
/*  502 */             FilePane.this.getFileChooser().rescanCurrentDirectory();
/*      */           }
/*      */         }
/*      */ 
/*      */         public boolean isEnabled() {
/*  507 */           String str = (String)getValue("ActionCommandKey");
/*  508 */           if (str == "cancelSelection")
/*  509 */             return FilePane.this.getFileChooser().isEnabled();
/*  510 */           if (str == "editFileName") {
/*  511 */             return (!FilePane.this.readOnly) && (FilePane.this.getFileChooser().isEnabled());
/*      */           }
/*  513 */           return true;
/*      */         }
/*      */       });
/*  523 */       localArrayList.add(new AbstractAction(this.refreshActionLabelText)
/*      */       {
/*      */         public void actionPerformed(ActionEvent paramAnonymousActionEvent)
/*      */         {
/*  484 */           String str = (String)getValue("ActionCommandKey");
/*      */ 
/*  486 */           if (str == "cancelSelection") {
/*  487 */             if (FilePane.this.editFile != null)
/*  488 */               FilePane.this.cancelEdit();
/*      */             else
/*  490 */               FilePane.this.getFileChooser().cancelSelection();
/*      */           }
/*  492 */           else if (str == "editFileName") {
/*  493 */             JFileChooser localJFileChooser = FilePane.this.getFileChooser();
/*  494 */             int i = FilePane.this.listSelectionModel.getMinSelectionIndex();
/*  495 */             if ((i >= 0) && (FilePane.this.editFile == null) && ((!localJFileChooser.isMultiSelectionEnabled()) || (localJFileChooser.getSelectedFiles().length <= 1)))
/*      */             {
/*  499 */               FilePane.this.editFileName(i);
/*      */             }
/*  501 */           } else if (str == "refresh") {
/*  502 */             FilePane.this.getFileChooser().rescanCurrentDirectory();
/*      */           }
/*      */         }
/*      */ 
/*      */         public boolean isEnabled() {
/*  507 */           String str = (String)getValue("ActionCommandKey");
/*  508 */           if (str == "cancelSelection")
/*  509 */             return FilePane.this.getFileChooser().isEnabled();
/*  510 */           if (str == "editFileName") {
/*  511 */             return (!FilePane.this.readOnly) && (FilePane.this.getFileChooser().isEnabled());
/*      */           }
/*  513 */           return true;
/*      */         }
/*      */       });
/*  525 */       Action localAction = this.fileChooserUIAccessor.getApproveSelectionAction();
/*  526 */       if (localAction != null) {
/*  527 */         localArrayList.add(localAction);
/*      */       }
/*  529 */       localAction = this.fileChooserUIAccessor.getChangeToParentDirectoryAction();
/*  530 */       if (localAction != null) {
/*  531 */         localArrayList.add(localAction);
/*      */       }
/*  533 */       localAction = getNewFolderAction();
/*  534 */       if (localAction != null) {
/*  535 */         localArrayList.add(localAction);
/*      */       }
/*  537 */       localAction = getViewTypeAction(0);
/*  538 */       if (localAction != null) {
/*  539 */         localArrayList.add(localAction);
/*      */       }
/*  541 */       localAction = getViewTypeAction(1);
/*  542 */       if (localAction != null) {
/*  543 */         localArrayList.add(localAction);
/*      */       }
/*  545 */       this.actions = ((Action[])localArrayList.toArray(new Action[localArrayList.size()]));
/*      */     }
/*      */ 
/*  548 */     return this.actions;
/*      */   }
/*      */ 
/*      */   protected void createActionMap() {
/*  552 */     addActionsToMap(super.getActionMap(), getActions());
/*      */   }
/*      */ 
/*      */   public static void addActionsToMap(ActionMap paramActionMap, Action[] paramArrayOfAction)
/*      */   {
/*  557 */     if ((paramActionMap != null) && (paramArrayOfAction != null))
/*  558 */       for (Action localAction : paramArrayOfAction) {
/*  559 */         String str = (String)localAction.getValue("ActionCommandKey");
/*  560 */         if (str == null) {
/*  561 */           str = (String)localAction.getValue("Name");
/*      */         }
/*  563 */         paramActionMap.put(str, localAction);
/*      */       }
/*      */   }
/*      */ 
/*      */   private void updateListRowCount(JList paramJList)
/*      */   {
/*  570 */     if (this.smallIconsView)
/*  571 */       paramJList.setVisibleRowCount(getModel().getSize() / 3);
/*      */     else
/*  573 */       paramJList.setVisibleRowCount(-1);
/*      */   }
/*      */ 
/*      */   public JPanel createList()
/*      */   {
/*  578 */     JPanel localJPanel = new JPanel(new BorderLayout());
/*  579 */     final JFileChooser localJFileChooser = getFileChooser();
/*  580 */     final JList local4 = new JList() {
/*      */       public int getNextMatch(String paramAnonymousString, int paramAnonymousInt, Position.Bias paramAnonymousBias) {
/*  582 */         ListModel localListModel = getModel();
/*  583 */         int i = localListModel.getSize();
/*  584 */         if ((paramAnonymousString == null) || (paramAnonymousInt < 0) || (paramAnonymousInt >= i)) {
/*  585 */           throw new IllegalArgumentException();
/*      */         }
/*      */ 
/*  588 */         int j = paramAnonymousBias == Position.Bias.Backward ? 1 : 0;
/*  589 */         for (int k = paramAnonymousInt; j != 0 ? k >= 0 : k < i; k += (j != 0 ? -1 : 1)) {
/*  590 */           String str = localJFileChooser.getName((File)localListModel.getElementAt(k));
/*  591 */           if (str.regionMatches(true, 0, paramAnonymousString, 0, paramAnonymousString.length())) {
/*  592 */             return k;
/*      */           }
/*      */         }
/*  595 */         return -1;
/*      */       }
/*      */     };
/*  598 */     local4.setCellRenderer(new FileRenderer());
/*  599 */     local4.setLayoutOrientation(1);
/*      */ 
/*  602 */     local4.putClientProperty("List.isFileList", Boolean.TRUE);
/*      */ 
/*  604 */     if (this.listViewWindowsStyle) {
/*  605 */       local4.addFocusListener(repaintListener);
/*      */     }
/*      */ 
/*  608 */     updateListRowCount(local4);
/*      */ 
/*  610 */     getModel().addListDataListener(new ListDataListener() {
/*      */       public void intervalAdded(ListDataEvent paramAnonymousListDataEvent) {
/*  612 */         FilePane.this.updateListRowCount(local4);
/*      */       }
/*      */       public void intervalRemoved(ListDataEvent paramAnonymousListDataEvent) {
/*  615 */         FilePane.this.updateListRowCount(local4);
/*      */       }
/*      */       public void contentsChanged(ListDataEvent paramAnonymousListDataEvent) {
/*  618 */         if (FilePane.this.isShowing()) {
/*  619 */           FilePane.this.clearSelection();
/*      */         }
/*  621 */         FilePane.this.updateListRowCount(local4);
/*      */       }
/*      */     });
/*  625 */     getModel().addPropertyChangeListener(this);
/*      */ 
/*  627 */     if (localJFileChooser.isMultiSelectionEnabled())
/*  628 */       local4.setSelectionMode(2);
/*      */     else {
/*  630 */       local4.setSelectionMode(0);
/*      */     }
/*  632 */     local4.setModel(new SortableListModel());
/*      */ 
/*  634 */     local4.addListSelectionListener(createListSelectionListener());
/*  635 */     local4.addMouseListener(getMouseHandler());
/*      */ 
/*  637 */     JScrollPane localJScrollPane = new JScrollPane(local4);
/*  638 */     if (this.listViewBackground != null) {
/*  639 */       local4.setBackground(this.listViewBackground);
/*      */     }
/*  641 */     if (this.listViewBorder != null) {
/*  642 */       localJScrollPane.setBorder(this.listViewBorder);
/*      */     }
/*      */ 
/*  645 */     local4.putClientProperty("AccessibleName", this.filesListAccessibleName);
/*      */ 
/*  647 */     localJPanel.add(localJScrollPane, "Center");
/*  648 */     return localJPanel;
/*      */   }
/*      */ 
/*      */   private DetailsTableModel getDetailsTableModel()
/*      */   {
/*  681 */     if (this.detailsTableModel == null) {
/*  682 */       this.detailsTableModel = new DetailsTableModel(getFileChooser());
/*      */     }
/*  684 */     return this.detailsTableModel;
/*      */   }
/*      */ 
/*      */   private void updateDetailsColumnModel(JTable paramJTable)
/*      */   {
/*  863 */     if (paramJTable != null) {
/*  864 */       ShellFolderColumnInfo[] arrayOfShellFolderColumnInfo = this.detailsTableModel.getColumns();
/*      */ 
/*  866 */       DefaultTableColumnModel localDefaultTableColumnModel = new DefaultTableColumnModel();
/*  867 */       for (int i = 0; i < arrayOfShellFolderColumnInfo.length; i++) {
/*  868 */         ShellFolderColumnInfo localShellFolderColumnInfo = arrayOfShellFolderColumnInfo[i];
/*  869 */         TableColumn localTableColumn = new TableColumn(i);
/*      */ 
/*  871 */         Object localObject1 = localShellFolderColumnInfo.getTitle();
/*  872 */         if ((localObject1 != null) && (((String)localObject1).startsWith("FileChooser.")) && (((String)localObject1).endsWith("HeaderText")))
/*      */         {
/*  874 */           localObject2 = UIManager.getString(localObject1, paramJTable.getLocale());
/*  875 */           if (localObject2 != null) {
/*  876 */             localObject1 = localObject2;
/*      */           }
/*      */         }
/*  879 */         localTableColumn.setHeaderValue(localObject1);
/*      */ 
/*  881 */         Object localObject2 = localShellFolderColumnInfo.getWidth();
/*  882 */         if (localObject2 != null) {
/*  883 */           localTableColumn.setPreferredWidth(((Integer)localObject2).intValue());
/*      */         }
/*      */ 
/*  887 */         localDefaultTableColumnModel.addColumn(localTableColumn);
/*      */       }
/*      */ 
/*  891 */       if ((!this.readOnly) && (localDefaultTableColumnModel.getColumnCount() > 0)) {
/*  892 */         localDefaultTableColumnModel.getColumn(0).setCellEditor(getDetailsTableCellEditor());
/*      */       }
/*      */ 
/*  896 */       paramJTable.setColumnModel(localDefaultTableColumnModel);
/*      */     }
/*      */   }
/*      */ 
/*      */   private DetailsTableRowSorter getRowSorter() {
/*  901 */     if (this.rowSorter == null) {
/*  902 */       this.rowSorter = new DetailsTableRowSorter();
/*      */     }
/*  904 */     return this.rowSorter;
/*      */   }
/*      */ 
/*      */   private DetailsTableCellEditor getDetailsTableCellEditor()
/*      */   {
/* 1000 */     if (this.tableCellEditor == null) {
/* 1001 */       this.tableCellEditor = new DetailsTableCellEditor(new JTextField());
/*      */     }
/* 1003 */     return this.tableCellEditor;
/*      */   }
/*      */ 
/*      */   public JPanel createDetailsView()
/*      */   {
/* 1128 */     final JFileChooser localJFileChooser = getFileChooser();
/*      */ 
/* 1130 */     JPanel localJPanel = new JPanel(new BorderLayout());
/*      */ 
/* 1132 */     JTable local6 = new JTable(getDetailsTableModel())
/*      */     {
/*      */       protected boolean processKeyBinding(KeyStroke paramAnonymousKeyStroke, KeyEvent paramAnonymousKeyEvent, int paramAnonymousInt, boolean paramAnonymousBoolean) {
/* 1135 */         if ((paramAnonymousKeyEvent.getKeyCode() == 27) && (getCellEditor() == null))
/*      */         {
/* 1137 */           localJFileChooser.dispatchEvent(paramAnonymousKeyEvent);
/* 1138 */           return true;
/*      */         }
/* 1140 */         return super.processKeyBinding(paramAnonymousKeyStroke, paramAnonymousKeyEvent, paramAnonymousInt, paramAnonymousBoolean);
/*      */       }
/*      */ 
/*      */       public void tableChanged(TableModelEvent paramAnonymousTableModelEvent) {
/* 1144 */         super.tableChanged(paramAnonymousTableModelEvent);
/*      */ 
/* 1146 */         if (paramAnonymousTableModelEvent.getFirstRow() == -1)
/*      */         {
/* 1148 */           FilePane.this.updateDetailsColumnModel(this);
/*      */         }
/*      */       }
/*      */     };
/* 1153 */     local6.setRowSorter(getRowSorter());
/* 1154 */     local6.setAutoCreateColumnsFromModel(false);
/* 1155 */     local6.setComponentOrientation(localJFileChooser.getComponentOrientation());
/* 1156 */     local6.setAutoResizeMode(0);
/* 1157 */     local6.setShowGrid(false);
/* 1158 */     local6.putClientProperty("JTable.autoStartsEdit", Boolean.FALSE);
/* 1159 */     local6.addKeyListener(this.detailsKeyListener);
/*      */ 
/* 1161 */     Font localFont = this.list.getFont();
/* 1162 */     local6.setFont(localFont);
/* 1163 */     local6.setIntercellSpacing(new Dimension(0, 0));
/*      */ 
/* 1165 */     AlignableTableHeaderRenderer localAlignableTableHeaderRenderer = new AlignableTableHeaderRenderer(local6.getTableHeader().getDefaultRenderer());
/*      */ 
/* 1167 */     local6.getTableHeader().setDefaultRenderer(localAlignableTableHeaderRenderer);
/* 1168 */     DetailsTableCellRenderer localDetailsTableCellRenderer = new DetailsTableCellRenderer(localJFileChooser);
/* 1169 */     local6.setDefaultRenderer(Object.class, localDetailsTableCellRenderer);
/*      */ 
/* 1172 */     local6.getColumnModel().getSelectionModel().setSelectionMode(0);
/*      */ 
/* 1175 */     local6.addMouseListener(getMouseHandler());
/*      */ 
/* 1180 */     local6.putClientProperty("Table.isFileList", Boolean.TRUE);
/*      */ 
/* 1182 */     if (this.listViewWindowsStyle) {
/* 1183 */       local6.addFocusListener(repaintListener);
/*      */     }
/*      */ 
/* 1188 */     ActionMap localActionMap = SwingUtilities.getUIActionMap(local6);
/* 1189 */     localActionMap.remove("selectNextRowCell");
/* 1190 */     localActionMap.remove("selectPreviousRowCell");
/* 1191 */     localActionMap.remove("selectNextColumnCell");
/* 1192 */     localActionMap.remove("selectPreviousColumnCell");
/* 1193 */     local6.setFocusTraversalKeys(0, null);
/*      */ 
/* 1195 */     local6.setFocusTraversalKeys(1, null);
/*      */ 
/* 1198 */     JScrollPane localJScrollPane = new JScrollPane(local6);
/* 1199 */     localJScrollPane.setComponentOrientation(localJFileChooser.getComponentOrientation());
/* 1200 */     LookAndFeel.installColors(localJScrollPane.getViewport(), "Table.background", "Table.foreground");
/*      */ 
/* 1204 */     localJScrollPane.addComponentListener(new ComponentAdapter() {
/*      */       public void componentResized(ComponentEvent paramAnonymousComponentEvent) {
/* 1206 */         JScrollPane localJScrollPane = (JScrollPane)paramAnonymousComponentEvent.getComponent();
/* 1207 */         FilePane.this.fixNameColumnWidth(localJScrollPane.getViewport().getSize().width);
/* 1208 */         localJScrollPane.removeComponentListener(this);
/*      */       }
/*      */     });
/* 1216 */     localJScrollPane.addMouseListener(new MouseAdapter() {
/*      */       public void mousePressed(MouseEvent paramAnonymousMouseEvent) {
/* 1218 */         JScrollPane localJScrollPane = (JScrollPane)paramAnonymousMouseEvent.getComponent();
/* 1219 */         JTable localJTable = (JTable)localJScrollPane.getViewport().getView();
/*      */ 
/* 1221 */         if ((!paramAnonymousMouseEvent.isShiftDown()) || (localJTable.getSelectionModel().getSelectionMode() == 0)) {
/* 1222 */           FilePane.this.clearSelection();
/* 1223 */           TableCellEditor localTableCellEditor = localJTable.getCellEditor();
/* 1224 */           if (localTableCellEditor != null)
/* 1225 */             localTableCellEditor.stopCellEditing();
/*      */         }
/*      */       }
/*      */     });
/* 1231 */     local6.setForeground(this.list.getForeground());
/* 1232 */     local6.setBackground(this.list.getBackground());
/*      */ 
/* 1234 */     if (this.listViewBorder != null) {
/* 1235 */       localJScrollPane.setBorder(this.listViewBorder);
/*      */     }
/* 1237 */     localJPanel.add(localJScrollPane, "Center");
/*      */ 
/* 1239 */     this.detailsTableModel.fireTableStructureChanged();
/*      */ 
/* 1241 */     local6.putClientProperty("AccessibleName", this.filesDetailsAccessibleName);
/*      */ 
/* 1243 */     return localJPanel;
/*      */   }
/*      */ 
/*      */   private void fixNameColumnWidth(int paramInt)
/*      */   {
/* 1276 */     TableColumn localTableColumn = this.detailsTable.getColumnModel().getColumn(0);
/* 1277 */     int i = this.detailsTable.getPreferredSize().width;
/*      */ 
/* 1279 */     if (i < paramInt)
/* 1280 */       localTableColumn.setPreferredWidth(localTableColumn.getPreferredWidth() + paramInt - i);
/*      */   }
/*      */ 
/*      */   public ListSelectionListener createListSelectionListener()
/*      */   {
/* 1315 */     return this.fileChooserUIAccessor.createListSelectionListener();
/*      */   }
/*      */ 
/*      */   private int getEditIndex()
/*      */   {
/* 1322 */     return this.lastIndex;
/*      */   }
/*      */ 
/*      */   private void setEditIndex(int paramInt) {
/* 1326 */     this.lastIndex = paramInt;
/*      */   }
/*      */ 
/*      */   private void resetEditIndex() {
/* 1330 */     this.lastIndex = -1;
/*      */   }
/*      */ 
/*      */   private void cancelEdit() {
/* 1334 */     if (this.editFile != null) {
/* 1335 */       this.editFile = null;
/* 1336 */       this.list.remove(this.editCell);
/* 1337 */       repaint();
/* 1338 */     } else if ((this.detailsTable != null) && (this.detailsTable.isEditing())) {
/* 1339 */       this.detailsTable.getCellEditor().cancelCellEditing();
/*      */     }
/*      */   }
/*      */ 
/*      */   private void editFileName(int paramInt)
/*      */   {
/* 1349 */     JFileChooser localJFileChooser = getFileChooser();
/* 1350 */     File localFile = localJFileChooser.getCurrentDirectory();
/*      */ 
/* 1352 */     if ((this.readOnly) || (!canWrite(localFile))) {
/* 1353 */       return;
/*      */     }
/*      */ 
/* 1356 */     ensureIndexIsVisible(paramInt);
/* 1357 */     switch (this.viewType) {
/*      */     case 0:
/* 1359 */       this.editFile = ((File)getModel().getElementAt(getRowSorter().convertRowIndexToModel(paramInt)));
/* 1360 */       Rectangle localRectangle = this.list.getCellBounds(paramInt, paramInt);
/* 1361 */       if (this.editCell == null) {
/* 1362 */         this.editCell = new JTextField();
/* 1363 */         this.editCell.setName("Tree.cellEditor");
/* 1364 */         this.editCell.addActionListener(new EditActionListener());
/* 1365 */         this.editCell.addFocusListener(this.editorFocusListener);
/* 1366 */         this.editCell.setNextFocusableComponent(this.list);
/*      */       }
/* 1368 */       this.list.add(this.editCell);
/* 1369 */       this.editCell.setText(localJFileChooser.getName(this.editFile));
/* 1370 */       ComponentOrientation localComponentOrientation = this.list.getComponentOrientation();
/* 1371 */       this.editCell.setComponentOrientation(localComponentOrientation);
/*      */ 
/* 1373 */       Icon localIcon = localJFileChooser.getIcon(this.editFile);
/*      */ 
/* 1376 */       int i = localIcon == null ? 20 : localIcon.getIconWidth() + 4;
/*      */ 
/* 1378 */       if (localComponentOrientation.isLeftToRight())
/* 1379 */         this.editCell.setBounds(i + localRectangle.x, localRectangle.y, localRectangle.width - i, localRectangle.height);
/*      */       else {
/* 1381 */         this.editCell.setBounds(localRectangle.x, localRectangle.y, localRectangle.width - i, localRectangle.height);
/*      */       }
/* 1383 */       this.editCell.requestFocus();
/* 1384 */       this.editCell.selectAll();
/* 1385 */       break;
/*      */     case 1:
/* 1388 */       this.detailsTable.editCellAt(paramInt, 0);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void applyEdit()
/*      */   {
/* 1401 */     if ((this.editFile != null) && (this.editFile.exists())) {
/* 1402 */       JFileChooser localJFileChooser = getFileChooser();
/* 1403 */       String str1 = localJFileChooser.getName(this.editFile);
/* 1404 */       String str2 = this.editFile.getName();
/* 1405 */       String str3 = this.editCell.getText().trim();
/*      */ 
/* 1408 */       if (!str3.equals(str1)) {
/* 1409 */         String str4 = str3;
/*      */ 
/* 1411 */         int i = str2.length();
/* 1412 */         int j = str1.length();
/* 1413 */         if ((i > j) && (str2.charAt(j) == '.')) {
/* 1414 */           str4 = str3 + str2.substring(j);
/*      */         }
/*      */ 
/* 1418 */         FileSystemView localFileSystemView = localJFileChooser.getFileSystemView();
/* 1419 */         File localFile = localFileSystemView.createFileObject(this.editFile.getParentFile(), str4);
/* 1420 */         if (localFile.exists()) {
/* 1421 */           JOptionPane.showMessageDialog(localJFileChooser, MessageFormat.format(this.renameErrorFileExistsText, new Object[] { str2 }), this.renameErrorTitleText, 0);
/*      */         }
/* 1424 */         else if (getModel().renameFile(this.editFile, localFile)) {
/* 1425 */           if (localFileSystemView.isParent(localJFileChooser.getCurrentDirectory(), localFile)) {
/* 1426 */             if (localJFileChooser.isMultiSelectionEnabled())
/* 1427 */               localJFileChooser.setSelectedFiles(new File[] { localFile });
/*      */             else {
/* 1429 */               localJFileChooser.setSelectedFile(localFile);
/*      */             }
/*      */           }
/*      */ 
/*      */         }
/*      */         else
/*      */         {
/* 1436 */           JOptionPane.showMessageDialog(localJFileChooser, MessageFormat.format(this.renameErrorText, new Object[] { str2 }), this.renameErrorTitleText, 0);
/*      */         }
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1442 */     if ((this.detailsTable != null) && (this.detailsTable.isEditing())) {
/* 1443 */       this.detailsTable.getCellEditor().stopCellEditing();
/*      */     }
/* 1445 */     cancelEdit();
/*      */   }
/*      */ 
/*      */   public Action getNewFolderAction()
/*      */   {
/* 1451 */     if ((!this.readOnly) && (this.newFolderAction == null)) {
/* 1452 */       this.newFolderAction = new AbstractAction(this.newFolderActionLabelText)
/*      */       {
/*      */         private Action basicNewFolderAction;
/*      */ 
/*      */         public void actionPerformed(ActionEvent paramAnonymousActionEvent)
/*      */         {
/* 1466 */           if (this.basicNewFolderAction == null) {
/* 1467 */             this.basicNewFolderAction = FilePane.this.fileChooserUIAccessor.getNewFolderAction();
/*      */           }
/* 1469 */           JFileChooser localJFileChooser = FilePane.this.getFileChooser();
/* 1470 */           File localFile1 = localJFileChooser.getSelectedFile();
/* 1471 */           this.basicNewFolderAction.actionPerformed(paramAnonymousActionEvent);
/* 1472 */           File localFile2 = localJFileChooser.getSelectedFile();
/* 1473 */           if ((localFile2 != null) && (!localFile2.equals(localFile1)) && (localFile2.isDirectory())) {
/* 1474 */             FilePane.this.newFolderFile = localFile2;
/*      */           }
/*      */         }
/*      */       };
/*      */     }
/* 1479 */     return this.newFolderAction;
/*      */   }
/*      */ 
/*      */   void setFileSelected()
/*      */   {
/*      */     Object localObject1;
/*      */     Object localObject2;
/*      */     int i;
/*      */     int j;
/* 1513 */     if ((getFileChooser().isMultiSelectionEnabled()) && (!isDirectorySelected())) {
/* 1514 */       localObject1 = getFileChooser().getSelectedFiles();
/* 1515 */       localObject2 = this.list.getSelectedValues();
/*      */ 
/* 1517 */       this.listSelectionModel.setValueIsAdjusting(true);
/*      */       try {
/* 1519 */         i = this.listSelectionModel.getLeadSelectionIndex();
/* 1520 */         j = this.listSelectionModel.getAnchorSelectionIndex();
/*      */ 
/* 1522 */         Arrays.sort((Object[])localObject1);
/* 1523 */         Arrays.sort((Object[])localObject2);
/*      */ 
/* 1525 */         int k = 0;
/* 1526 */         int m = 0;
/*      */ 
/* 1530 */         while ((k < localObject1.length) && (m < localObject2.length))
/*      */         {
/* 1532 */           int n = localObject1[k].compareTo((File)localObject2[m]);
/* 1533 */           if (n < 0) {
/* 1534 */             doSelectFile(localObject1[(k++)]);
/* 1535 */           } else if (n > 0) {
/* 1536 */             doDeselectFile(localObject2[(m++)]);
/*      */           }
/*      */           else {
/* 1539 */             k++;
/* 1540 */             m++;
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/* 1545 */         while (k < localObject1.length) {
/* 1546 */           doSelectFile(localObject1[(k++)]);
/*      */         }
/*      */ 
/* 1549 */         while (m < localObject2.length) {
/* 1550 */           doDeselectFile(localObject2[(m++)]);
/*      */         }
/*      */ 
/* 1554 */         if ((this.listSelectionModel instanceof DefaultListSelectionModel)) {
/* 1555 */           ((DefaultListSelectionModel)this.listSelectionModel).moveLeadSelectionIndex(i);
/*      */ 
/* 1557 */           this.listSelectionModel.setAnchorSelectionIndex(j);
/*      */         }
/*      */       } finally {
/* 1560 */         this.listSelectionModel.setValueIsAdjusting(false);
/*      */       }
/*      */     } else {
/* 1563 */       localObject1 = getFileChooser();
/*      */ 
/* 1565 */       if (isDirectorySelected())
/* 1566 */         localObject2 = getDirectory();
/*      */       else {
/* 1568 */         localObject2 = ((JFileChooser)localObject1).getSelectedFile();
/*      */       }
/*      */ 
/* 1571 */       if ((localObject2 != null) && ((i = getModel().indexOf(localObject2)) >= 0)) {
/* 1572 */         j = getRowSorter().convertRowIndexToView(i);
/* 1573 */         this.listSelectionModel.setSelectionInterval(j, j);
/* 1574 */         ensureIndexIsVisible(j);
/*      */       } else {
/* 1576 */         clearSelection();
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private void doSelectFile(File paramFile) {
/* 1582 */     int i = getModel().indexOf(paramFile);
/*      */ 
/* 1584 */     if (i >= 0) {
/* 1585 */       i = getRowSorter().convertRowIndexToView(i);
/* 1586 */       this.listSelectionModel.addSelectionInterval(i, i);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void doDeselectFile(Object paramObject) {
/* 1591 */     int i = getRowSorter().convertRowIndexToView(getModel().indexOf(paramObject));
/*      */ 
/* 1593 */     this.listSelectionModel.removeSelectionInterval(i, i);
/*      */   }
/*      */ 
/*      */   private void doSelectedFileChanged(PropertyChangeEvent paramPropertyChangeEvent)
/*      */   {
/* 1599 */     applyEdit();
/* 1600 */     File localFile = (File)paramPropertyChangeEvent.getNewValue();
/* 1601 */     JFileChooser localJFileChooser = getFileChooser();
/* 1602 */     if ((localFile != null) && (((localJFileChooser.isFileSelectionEnabled()) && (!localFile.isDirectory())) || ((localFile.isDirectory()) && (localJFileChooser.isDirectorySelectionEnabled()))))
/*      */     {
/* 1606 */       setFileSelected();
/*      */     }
/*      */   }
/*      */ 
/*      */   private void doSelectedFilesChanged(PropertyChangeEvent paramPropertyChangeEvent) {
/* 1611 */     applyEdit();
/* 1612 */     File[] arrayOfFile = (File[])paramPropertyChangeEvent.getNewValue();
/* 1613 */     JFileChooser localJFileChooser = getFileChooser();
/* 1614 */     if ((arrayOfFile != null) && (arrayOfFile.length > 0) && ((arrayOfFile.length > 1) || (localJFileChooser.isDirectorySelectionEnabled()) || (!arrayOfFile[0].isDirectory())))
/*      */     {
/* 1617 */       setFileSelected();
/*      */     }
/*      */   }
/*      */ 
/*      */   private void doDirectoryChanged(PropertyChangeEvent paramPropertyChangeEvent) {
/* 1622 */     getDetailsTableModel().updateColumnInfo();
/*      */ 
/* 1624 */     JFileChooser localJFileChooser = getFileChooser();
/* 1625 */     FileSystemView localFileSystemView = localJFileChooser.getFileSystemView();
/*      */ 
/* 1627 */     applyEdit();
/* 1628 */     resetEditIndex();
/* 1629 */     ensureIndexIsVisible(0);
/* 1630 */     File localFile = localJFileChooser.getCurrentDirectory();
/* 1631 */     if (localFile != null) {
/* 1632 */       if (!this.readOnly) {
/* 1633 */         getNewFolderAction().setEnabled(canWrite(localFile));
/*      */       }
/* 1635 */       this.fileChooserUIAccessor.getChangeToParentDirectoryAction().setEnabled(!localFileSystemView.isRoot(localFile));
/*      */     }
/* 1637 */     if (this.list != null)
/* 1638 */       this.list.clearSelection();
/*      */   }
/*      */ 
/*      */   private void doFilterChanged(PropertyChangeEvent paramPropertyChangeEvent)
/*      */   {
/* 1643 */     applyEdit();
/* 1644 */     resetEditIndex();
/* 1645 */     clearSelection();
/*      */   }
/*      */ 
/*      */   private void doFileSelectionModeChanged(PropertyChangeEvent paramPropertyChangeEvent) {
/* 1649 */     applyEdit();
/* 1650 */     resetEditIndex();
/* 1651 */     clearSelection();
/*      */   }
/*      */ 
/*      */   private void doMultiSelectionChanged(PropertyChangeEvent paramPropertyChangeEvent) {
/* 1655 */     if (getFileChooser().isMultiSelectionEnabled()) {
/* 1656 */       this.listSelectionModel.setSelectionMode(2);
/*      */     } else {
/* 1658 */       this.listSelectionModel.setSelectionMode(0);
/* 1659 */       clearSelection();
/* 1660 */       getFileChooser().setSelectedFiles(null);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void propertyChange(PropertyChangeEvent paramPropertyChangeEvent)
/*      */   {
/* 1669 */     if (this.viewType == -1) {
/* 1670 */       setViewType(0);
/*      */     }
/*      */ 
/* 1673 */     String str = paramPropertyChangeEvent.getPropertyName();
/* 1674 */     if (str.equals("SelectedFileChangedProperty")) {
/* 1675 */       doSelectedFileChanged(paramPropertyChangeEvent);
/* 1676 */     } else if (str.equals("SelectedFilesChangedProperty")) {
/* 1677 */       doSelectedFilesChanged(paramPropertyChangeEvent);
/* 1678 */     } else if (str.equals("directoryChanged")) {
/* 1679 */       doDirectoryChanged(paramPropertyChangeEvent);
/* 1680 */     } else if (str.equals("fileFilterChanged")) {
/* 1681 */       doFilterChanged(paramPropertyChangeEvent);
/* 1682 */     } else if (str.equals("fileSelectionChanged")) {
/* 1683 */       doFileSelectionModeChanged(paramPropertyChangeEvent);
/* 1684 */     } else if (str.equals("MultiSelectionEnabledChangedProperty")) {
/* 1685 */       doMultiSelectionChanged(paramPropertyChangeEvent);
/* 1686 */     } else if (str.equals("CancelSelection")) {
/* 1687 */       applyEdit();
/* 1688 */     } else if (str.equals("busy")) {
/* 1689 */       setCursor(((Boolean)paramPropertyChangeEvent.getNewValue()).booleanValue() ? waitCursor : null);
/* 1690 */     } else if (str.equals("componentOrientation")) {
/* 1691 */       ComponentOrientation localComponentOrientation = (ComponentOrientation)paramPropertyChangeEvent.getNewValue();
/* 1692 */       JFileChooser localJFileChooser = (JFileChooser)paramPropertyChangeEvent.getSource();
/* 1693 */       if (localComponentOrientation != paramPropertyChangeEvent.getOldValue()) {
/* 1694 */         localJFileChooser.applyComponentOrientation(localComponentOrientation);
/*      */       }
/* 1696 */       if (this.detailsTable != null) {
/* 1697 */         this.detailsTable.setComponentOrientation(localComponentOrientation);
/* 1698 */         this.detailsTable.getParent().getParent().setComponentOrientation(localComponentOrientation);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private void ensureIndexIsVisible(int paramInt) {
/* 1704 */     if (paramInt >= 0) {
/* 1705 */       if (this.list != null) {
/* 1706 */         this.list.ensureIndexIsVisible(paramInt);
/*      */       }
/* 1708 */       if (this.detailsTable != null)
/* 1709 */         this.detailsTable.scrollRectToVisible(this.detailsTable.getCellRect(paramInt, 0, true));
/*      */     }
/*      */   }
/*      */ 
/*      */   public void ensureFileIsVisible(JFileChooser paramJFileChooser, File paramFile)
/*      */   {
/* 1715 */     int i = getModel().indexOf(paramFile);
/* 1716 */     if (i >= 0)
/* 1717 */       ensureIndexIsVisible(getRowSorter().convertRowIndexToView(i));
/*      */   }
/*      */ 
/*      */   public void rescanCurrentDirectory()
/*      */   {
/* 1722 */     getModel().validateFileCache();
/*      */   }
/*      */ 
/*      */   public void clearSelection() {
/* 1726 */     if (this.listSelectionModel != null) {
/* 1727 */       this.listSelectionModel.clearSelection();
/* 1728 */       if ((this.listSelectionModel instanceof DefaultListSelectionModel)) {
/* 1729 */         ((DefaultListSelectionModel)this.listSelectionModel).moveLeadSelectionIndex(0);
/* 1730 */         this.listSelectionModel.setAnchorSelectionIndex(0);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public JMenu getViewMenu() {
/* 1736 */     if (this.viewMenu == null) {
/* 1737 */       this.viewMenu = new JMenu(this.viewMenuLabelText);
/* 1738 */       ButtonGroup localButtonGroup = new ButtonGroup();
/*      */ 
/* 1740 */       for (int i = 0; i < 2; i++) {
/* 1741 */         JRadioButtonMenuItem localJRadioButtonMenuItem = new JRadioButtonMenuItem(new ViewTypeAction(i));
/*      */ 
/* 1743 */         localButtonGroup.add(localJRadioButtonMenuItem);
/* 1744 */         this.viewMenu.add(localJRadioButtonMenuItem);
/*      */       }
/* 1746 */       updateViewMenu();
/*      */     }
/* 1748 */     return this.viewMenu;
/*      */   }
/*      */ 
/*      */   private void updateViewMenu() {
/* 1752 */     if (this.viewMenu != null) {
/* 1753 */       Component[] arrayOfComponent1 = this.viewMenu.getMenuComponents();
/* 1754 */       for (Component localComponent : arrayOfComponent1)
/* 1755 */         if ((localComponent instanceof JRadioButtonMenuItem)) {
/* 1756 */           JRadioButtonMenuItem localJRadioButtonMenuItem = (JRadioButtonMenuItem)localComponent;
/* 1757 */           if (((ViewTypeAction)localJRadioButtonMenuItem.getAction()).viewType == this.viewType)
/* 1758 */             localJRadioButtonMenuItem.setSelected(true);
/*      */         }
/*      */     }
/*      */   }
/*      */ 
/*      */   public JPopupMenu getComponentPopupMenu()
/*      */   {
/* 1766 */     JPopupMenu localJPopupMenu = getFileChooser().getComponentPopupMenu();
/* 1767 */     if (localJPopupMenu != null) {
/* 1768 */       return localJPopupMenu;
/*      */     }
/*      */ 
/* 1771 */     JMenu localJMenu = getViewMenu();
/* 1772 */     if (this.contextMenu == null) {
/* 1773 */       this.contextMenu = new JPopupMenu();
/* 1774 */       if (localJMenu != null) {
/* 1775 */         this.contextMenu.add(localJMenu);
/* 1776 */         if (this.listViewWindowsStyle) {
/* 1777 */           this.contextMenu.addSeparator();
/*      */         }
/*      */       }
/* 1780 */       ActionMap localActionMap = getActionMap();
/* 1781 */       Action localAction1 = localActionMap.get("refresh");
/* 1782 */       Action localAction2 = localActionMap.get("New Folder");
/* 1783 */       if (localAction1 != null) {
/* 1784 */         this.contextMenu.add(localAction1);
/* 1785 */         if ((this.listViewWindowsStyle) && (localAction2 != null)) {
/* 1786 */           this.contextMenu.addSeparator();
/*      */         }
/*      */       }
/* 1789 */       if (localAction2 != null) {
/* 1790 */         this.contextMenu.add(localAction2);
/*      */       }
/*      */     }
/* 1793 */     if (localJMenu != null) {
/* 1794 */       localJMenu.getPopupMenu().setInvoker(localJMenu);
/*      */     }
/* 1796 */     return this.contextMenu;
/*      */   }
/*      */ 
/*      */   protected Handler getMouseHandler()
/*      */   {
/* 1803 */     if (this.handler == null) {
/* 1804 */       this.handler = new Handler(null);
/*      */     }
/* 1806 */     return this.handler;
/*      */   }
/*      */ 
/*      */   protected boolean isDirectorySelected()
/*      */   {
/* 1946 */     return this.fileChooserUIAccessor.isDirectorySelected();
/*      */   }
/*      */ 
/*      */   protected File getDirectory()
/*      */   {
/* 1957 */     return this.fileChooserUIAccessor.getDirectory();
/*      */   }
/*      */ 
/*      */   private Component findChildComponent(Container paramContainer, Class paramClass) {
/* 1961 */     int i = paramContainer.getComponentCount();
/* 1962 */     for (int j = 0; j < i; j++) {
/* 1963 */       Component localComponent1 = paramContainer.getComponent(j);
/* 1964 */       if (paramClass.isInstance(localComponent1))
/* 1965 */         return localComponent1;
/* 1966 */       if ((localComponent1 instanceof Container)) {
/* 1967 */         Component localComponent2 = findChildComponent((Container)localComponent1, paramClass);
/* 1968 */         if (localComponent2 != null) {
/* 1969 */           return localComponent2;
/*      */         }
/*      */       }
/*      */     }
/* 1973 */     return null;
/*      */   }
/*      */ 
/*      */   public boolean canWrite(File paramFile)
/*      */   {
/* 1978 */     if (!paramFile.exists()) {
/* 1979 */       return false;
/*      */     }
/*      */     try
/*      */     {
/* 1983 */       if ((paramFile instanceof ShellFolder)) {
/* 1984 */         return ((ShellFolder)paramFile).isFileSystem();
/*      */       }
/* 1986 */       if (usesShellFolder(getFileChooser())) {
/*      */         try {
/* 1988 */           return ShellFolder.getShellFolder(paramFile).isFileSystem();
/*      */         }
/*      */         catch (FileNotFoundException localFileNotFoundException) {
/* 1991 */           return false;
/*      */         }
/*      */       }
/*      */ 
/* 1995 */       return true;
/*      */     }
/*      */     catch (SecurityException localSecurityException) {
/*      */     }
/* 1999 */     return false;
/*      */   }
/*      */ 
/*      */   public static boolean usesShellFolder(JFileChooser paramJFileChooser)
/*      */   {
/* 2007 */     Boolean localBoolean = (Boolean)paramJFileChooser.getClientProperty("FileChooser.useShellFolder");
/*      */ 
/* 2009 */     return localBoolean == null ? paramJFileChooser.getFileSystemView().equals(FileSystemView.getFileSystemView()) : localBoolean.booleanValue();
/*      */   }
/*      */ 
/*      */   private class AlignableTableHeaderRenderer
/*      */     implements TableCellRenderer
/*      */   {
/*      */     TableCellRenderer wrappedRenderer;
/*      */ 
/*      */     public AlignableTableHeaderRenderer(TableCellRenderer arg2)
/*      */     {
/*      */       Object localObject;
/* 1250 */       this.wrappedRenderer = localObject;
/*      */     }
/*      */ 
/*      */     public Component getTableCellRendererComponent(JTable paramJTable, Object paramObject, boolean paramBoolean1, boolean paramBoolean2, int paramInt1, int paramInt2)
/*      */     {
/* 1257 */       Component localComponent = this.wrappedRenderer.getTableCellRendererComponent(paramJTable, paramObject, paramBoolean1, paramBoolean2, paramInt1, paramInt2);
/*      */ 
/* 1260 */       int i = paramJTable.convertColumnIndexToModel(paramInt2);
/* 1261 */       ShellFolderColumnInfo localShellFolderColumnInfo = FilePane.this.detailsTableModel.getColumns()[i];
/*      */ 
/* 1263 */       Integer localInteger = localShellFolderColumnInfo.getAlignment();
/* 1264 */       if (localInteger == null) {
/* 1265 */         localInteger = Integer.valueOf(0);
/*      */       }
/* 1267 */       if ((localComponent instanceof JLabel)) {
/* 1268 */         ((JLabel)localComponent).setHorizontalAlignment(localInteger.intValue());
/*      */       }
/*      */ 
/* 1271 */       return localComponent;
/*      */     }
/*      */   }
/*      */ 
/*      */   private class DelayedSelectionUpdater
/*      */     implements Runnable
/*      */   {
/*      */     File editFile;
/*      */ 
/*      */     DelayedSelectionUpdater()
/*      */     {
/* 1288 */       this(null);
/*      */     }
/*      */ 
/*      */     DelayedSelectionUpdater(File arg2)
/*      */     {
/*      */       Object localObject;
/* 1292 */       this.editFile = localObject;
/* 1293 */       if (FilePane.this.isShowing())
/* 1294 */         SwingUtilities.invokeLater(this);
/*      */     }
/*      */ 
/*      */     public void run()
/*      */     {
/* 1299 */       FilePane.this.setFileSelected();
/* 1300 */       if (this.editFile != null) {
/* 1301 */         FilePane.this.editFileName(FilePane.access$100(FilePane.this).convertRowIndexToView(FilePane.this.getModel().indexOf(this.editFile)));
/*      */ 
/* 1303 */         this.editFile = null;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private class DetailsTableCellEditor extends DefaultCellEditor
/*      */   {
/*      */     private final JTextField tf;
/*      */ 
/*      */     public DetailsTableCellEditor(JTextField arg2)
/*      */     {
/* 1010 */       super();
/* 1011 */       this.tf = localJTextField;
/* 1012 */       localJTextField.setName("Table.editor");
/* 1013 */       localJTextField.addFocusListener(FilePane.this.editorFocusListener);
/*      */     }
/*      */ 
/*      */     public Component getTableCellEditorComponent(JTable paramJTable, Object paramObject, boolean paramBoolean, int paramInt1, int paramInt2)
/*      */     {
/* 1018 */       Component localComponent = super.getTableCellEditorComponent(paramJTable, paramObject, paramBoolean, paramInt1, paramInt2);
/*      */ 
/* 1020 */       if ((paramObject instanceof File)) {
/* 1021 */         this.tf.setText(FilePane.this.getFileChooser().getName((File)paramObject));
/* 1022 */         this.tf.selectAll();
/*      */       }
/* 1024 */       return localComponent;
/*      */     }
/*      */   }
/*      */ 
/*      */   class DetailsTableCellRenderer extends DefaultTableCellRenderer
/*      */   {
/*      */     JFileChooser chooser;
/*      */     DateFormat df;
/*      */ 
/*      */     DetailsTableCellRenderer(JFileChooser arg2)
/*      */     {
/*      */       Object localObject;
/* 1034 */       this.chooser = localObject;
/* 1035 */       this.df = DateFormat.getDateTimeInstance(3, 3, localObject.getLocale());
/*      */     }
/*      */ 
/*      */     public void setBounds(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/* 1040 */       if ((getHorizontalAlignment() == 10) && (!FilePane.this.fullRowSelection))
/*      */       {
/* 1043 */         paramInt3 = Math.min(paramInt3, getPreferredSize().width + 4);
/*      */       }
/* 1045 */       else paramInt1 -= 4;
/*      */ 
/* 1047 */       super.setBounds(paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public Insets getInsets(Insets paramInsets)
/*      */     {
/* 1053 */       paramInsets = super.getInsets(paramInsets);
/* 1054 */       paramInsets.left += 4;
/* 1055 */       paramInsets.right += 4;
/* 1056 */       return paramInsets;
/*      */     }
/*      */ 
/*      */     public Component getTableCellRendererComponent(JTable paramJTable, Object paramObject, boolean paramBoolean1, boolean paramBoolean2, int paramInt1, int paramInt2)
/*      */     {
/* 1062 */       if (((paramJTable.convertColumnIndexToModel(paramInt2) != 0) || ((FilePane.this.listViewWindowsStyle) && (!paramJTable.isFocusOwner()))) && (!FilePane.this.fullRowSelection))
/*      */       {
/* 1065 */         paramBoolean1 = false;
/*      */       }
/*      */ 
/* 1068 */       super.getTableCellRendererComponent(paramJTable, paramObject, paramBoolean1, paramBoolean2, paramInt1, paramInt2);
/*      */ 
/* 1071 */       setIcon(null);
/*      */ 
/* 1073 */       int i = paramJTable.convertColumnIndexToModel(paramInt2);
/* 1074 */       ShellFolderColumnInfo localShellFolderColumnInfo = FilePane.this.detailsTableModel.getColumns()[i];
/*      */ 
/* 1076 */       Integer localInteger = localShellFolderColumnInfo.getAlignment();
/* 1077 */       if (localInteger == null) {
/* 1078 */         localInteger = Integer.valueOf((paramObject instanceof Number) ? 4 : 10);
/*      */       }
/*      */ 
/* 1083 */       setHorizontalAlignment(localInteger.intValue());
/*      */       String str;
/* 1089 */       if (paramObject == null) {
/* 1090 */         str = "";
/*      */       }
/* 1092 */       else if ((paramObject instanceof File)) {
/* 1093 */         File localFile = (File)paramObject;
/* 1094 */         str = this.chooser.getName(localFile);
/* 1095 */         Icon localIcon = this.chooser.getIcon(localFile);
/* 1096 */         setIcon(localIcon);
/*      */       }
/* 1098 */       else if ((paramObject instanceof Long)) {
/* 1099 */         long l = ((Long)paramObject).longValue() / 1024L;
/* 1100 */         if (FilePane.this.listViewWindowsStyle) {
/* 1101 */           str = MessageFormat.format(FilePane.this.kiloByteString, new Object[] { Long.valueOf(l + 1L) });
/* 1102 */         } else if (l < 1024L) {
/* 1103 */           str = MessageFormat.format(FilePane.this.kiloByteString, new Object[] { Long.valueOf(l == 0L ? 1L : l) });
/*      */         } else {
/* 1105 */           l /= 1024L;
/* 1106 */           if (l < 1024L) {
/* 1107 */             str = MessageFormat.format(FilePane.this.megaByteString, new Object[] { Long.valueOf(l) });
/*      */           } else {
/* 1109 */             l /= 1024L;
/* 1110 */             str = MessageFormat.format(FilePane.this.gigaByteString, new Object[] { Long.valueOf(l) });
/*      */           }
/*      */         }
/*      */       }
/* 1114 */       else if ((paramObject instanceof Date)) {
/* 1115 */         str = this.df.format((Date)paramObject);
/*      */       }
/*      */       else {
/* 1118 */         str = paramObject.toString();
/*      */       }
/*      */ 
/* 1121 */       setText(str);
/*      */ 
/* 1123 */       return this;
/*      */     }
/*      */   }
/*      */ 
/*      */   class DetailsTableModel extends AbstractTableModel
/*      */     implements ListDataListener
/*      */   {
/*      */     JFileChooser chooser;
/*      */     BasicDirectoryModel directoryModel;
/*      */     ShellFolderColumnInfo[] columns;
/*      */     int[] columnMap;
/*      */ 
/*      */     DetailsTableModel(JFileChooser arg2)
/*      */     {
/*      */       Object localObject;
/*  695 */       this.chooser = localObject;
/*  696 */       this.directoryModel = FilePane.this.getModel();
/*  697 */       this.directoryModel.addListDataListener(this);
/*      */ 
/*  699 */       updateColumnInfo();
/*      */     }
/*      */ 
/*      */     void updateColumnInfo() {
/*  703 */       Object localObject = this.chooser.getCurrentDirectory();
/*  704 */       if ((localObject != null) && (FilePane.usesShellFolder(this.chooser))) {
/*      */         try {
/*  706 */           localObject = ShellFolder.getShellFolder((File)localObject);
/*      */         }
/*      */         catch (FileNotFoundException localFileNotFoundException)
/*      */         {
/*      */         }
/*      */       }
/*  712 */       ShellFolderColumnInfo[] arrayOfShellFolderColumnInfo = ShellFolder.getFolderColumns((File)localObject);
/*      */ 
/*  714 */       ArrayList localArrayList = new ArrayList();
/*      */ 
/*  716 */       this.columnMap = new int[arrayOfShellFolderColumnInfo.length];
/*  717 */       for (int i = 0; i < arrayOfShellFolderColumnInfo.length; i++) {
/*  718 */         ShellFolderColumnInfo localShellFolderColumnInfo = arrayOfShellFolderColumnInfo[i];
/*  719 */         if (localShellFolderColumnInfo.isVisible()) {
/*  720 */           this.columnMap[localArrayList.size()] = i;
/*  721 */           localArrayList.add(localShellFolderColumnInfo);
/*      */         }
/*      */       }
/*      */ 
/*  725 */       this.columns = new ShellFolderColumnInfo[localArrayList.size()];
/*  726 */       localArrayList.toArray(this.columns);
/*  727 */       this.columnMap = Arrays.copyOf(this.columnMap, this.columns.length);
/*      */ 
/*  729 */       List localList = FilePane.this.rowSorter == null ? null : FilePane.this.rowSorter.getSortKeys();
/*      */ 
/*  731 */       fireTableStructureChanged();
/*  732 */       restoreSortKeys(localList);
/*      */     }
/*      */ 
/*      */     private void restoreSortKeys(List<? extends RowSorter.SortKey> paramList) {
/*  736 */       if (paramList != null)
/*      */       {
/*  738 */         for (int i = 0; i < paramList.size(); i++) {
/*  739 */           RowSorter.SortKey localSortKey = (RowSorter.SortKey)paramList.get(i);
/*  740 */           if (localSortKey.getColumn() >= this.columns.length) {
/*  741 */             paramList = null;
/*  742 */             break;
/*      */           }
/*      */         }
/*  745 */         if (paramList != null)
/*  746 */           FilePane.this.rowSorter.setSortKeys(paramList);
/*      */       }
/*      */     }
/*      */ 
/*      */     public int getRowCount()
/*      */     {
/*  752 */       return this.directoryModel.getSize();
/*      */     }
/*      */ 
/*      */     public int getColumnCount() {
/*  756 */       return this.columns.length;
/*      */     }
/*      */ 
/*      */     public Object getValueAt(int paramInt1, int paramInt2)
/*      */     {
/*  765 */       return getFileColumnValue((File)this.directoryModel.getElementAt(paramInt1), paramInt2);
/*      */     }
/*      */ 
/*      */     private Object getFileColumnValue(File paramFile, int paramInt) {
/*  769 */       return paramInt == 0 ? paramFile : ShellFolder.getFolderColumnValue(paramFile, this.columnMap[paramInt]);
/*      */     }
/*      */ 
/*      */     public void setValueAt(Object paramObject, int paramInt1, int paramInt2)
/*      */     {
/*  775 */       if (paramInt2 == 0) {
/*  776 */         final JFileChooser localJFileChooser = FilePane.this.getFileChooser();
/*  777 */         File localFile1 = (File)getValueAt(paramInt1, paramInt2);
/*  778 */         if (localFile1 != null) {
/*  779 */           String str1 = localJFileChooser.getName(localFile1);
/*  780 */           String str2 = localFile1.getName();
/*  781 */           String str3 = ((String)paramObject).trim();
/*      */ 
/*  784 */           if (!str3.equals(str1)) {
/*  785 */             String str4 = str3;
/*      */ 
/*  787 */             int i = str2.length();
/*  788 */             int j = str1.length();
/*  789 */             if ((i > j) && (str2.charAt(j) == '.')) {
/*  790 */               str4 = str3 + str2.substring(j);
/*      */             }
/*      */ 
/*  794 */             FileSystemView localFileSystemView = localJFileChooser.getFileSystemView();
/*  795 */             final File localFile2 = localFileSystemView.createFileObject(localFile1.getParentFile(), str4);
/*  796 */             if (localFile2.exists()) {
/*  797 */               JOptionPane.showMessageDialog(localJFileChooser, MessageFormat.format(FilePane.this.renameErrorFileExistsText, new Object[] { str2 }), FilePane.this.renameErrorTitleText, 0);
/*      */             }
/*  800 */             else if (FilePane.this.getModel().renameFile(localFile1, localFile2)) {
/*  801 */               if (localFileSystemView.isParent(localJFileChooser.getCurrentDirectory(), localFile2))
/*      */               {
/*  805 */                 SwingUtilities.invokeLater(new Runnable() {
/*      */                   public void run() {
/*  807 */                     if (localJFileChooser.isMultiSelectionEnabled())
/*  808 */                       localJFileChooser.setSelectedFiles(new File[] { localFile2 });
/*      */                     else {
/*  810 */                       localJFileChooser.setSelectedFile(localFile2);
/*      */                     }
/*      */                   }
/*      */                 });
/*      */               }
/*      */ 
/*      */             }
/*      */             else
/*      */             {
/*  819 */               JOptionPane.showMessageDialog(localJFileChooser, MessageFormat.format(FilePane.this.renameErrorText, new Object[] { str2 }), FilePane.this.renameErrorTitleText, 0);
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     public boolean isCellEditable(int paramInt1, int paramInt2)
/*      */     {
/*  829 */       File localFile = FilePane.this.getFileChooser().getCurrentDirectory();
/*  830 */       return (!FilePane.this.readOnly) && (paramInt2 == 0) && (FilePane.this.canWrite(localFile));
/*      */     }
/*      */ 
/*      */     public void contentsChanged(ListDataEvent paramListDataEvent)
/*      */     {
/*  835 */       new FilePane.DelayedSelectionUpdater(FilePane.this);
/*  836 */       fireTableDataChanged();
/*      */     }
/*      */ 
/*      */     public void intervalAdded(ListDataEvent paramListDataEvent) {
/*  840 */       int i = paramListDataEvent.getIndex0();
/*  841 */       int j = paramListDataEvent.getIndex1();
/*  842 */       if (i == j) {
/*  843 */         File localFile = (File)FilePane.this.getModel().getElementAt(i);
/*  844 */         if (localFile.equals(FilePane.this.newFolderFile)) {
/*  845 */           new FilePane.DelayedSelectionUpdater(FilePane.this, localFile);
/*  846 */           FilePane.this.newFolderFile = null;
/*      */         }
/*      */       }
/*      */ 
/*  850 */       fireTableRowsInserted(paramListDataEvent.getIndex0(), paramListDataEvent.getIndex1());
/*      */     }
/*      */     public void intervalRemoved(ListDataEvent paramListDataEvent) {
/*  853 */       fireTableRowsDeleted(paramListDataEvent.getIndex0(), paramListDataEvent.getIndex1());
/*      */     }
/*      */ 
/*      */     public ShellFolderColumnInfo[] getColumns() {
/*  857 */       return this.columns;
/*      */     }
/*      */   }
/*      */ 
/*      */   private class DetailsTableRowSorter extends TableRowSorter<TableModel>
/*      */   {
/*      */     public DetailsTableRowSorter()
/*      */     {
/*  909 */       setModelWrapper(new SorterModelWrapper(null));
/*      */     }
/*      */ 
/*      */     public void updateComparators(ShellFolderColumnInfo[] paramArrayOfShellFolderColumnInfo) {
/*  913 */       for (int i = 0; i < paramArrayOfShellFolderColumnInfo.length; i++) {
/*  914 */         Object localObject = paramArrayOfShellFolderColumnInfo[i].getComparator();
/*  915 */         if (localObject != null) {
/*  916 */           localObject = new FilePane.DirectoriesFirstComparatorWrapper(FilePane.this, i, (Comparator)localObject);
/*      */         }
/*  918 */         setComparator(i, (Comparator)localObject);
/*      */       }
/*      */     }
/*      */ 
/*      */     public void sort()
/*      */     {
/*  924 */       ShellFolder.invoke(new Callable() {
/*      */         public Void call() {
/*  926 */           FilePane.DetailsTableRowSorter.this.sort();
/*  927 */           return null;
/*      */         }
/*      */       });
/*      */     }
/*      */ 
/*      */     public void modelStructureChanged() {
/*  933 */       super.modelStructureChanged();
/*  934 */       updateComparators(FilePane.this.detailsTableModel.getColumns());
/*      */     }
/*      */     private class SorterModelWrapper extends DefaultRowSorter.ModelWrapper<TableModel, Integer> {
/*      */       private SorterModelWrapper() {
/*      */       }
/*  939 */       public TableModel getModel() { return FilePane.this.getDetailsTableModel(); }
/*      */ 
/*      */       public int getColumnCount()
/*      */       {
/*  943 */         return FilePane.this.getDetailsTableModel().getColumnCount();
/*      */       }
/*      */ 
/*      */       public int getRowCount() {
/*  947 */         return FilePane.this.getDetailsTableModel().getRowCount();
/*      */       }
/*      */ 
/*      */       public Object getValueAt(int paramInt1, int paramInt2) {
/*  951 */         return FilePane.this.getModel().getElementAt(paramInt1);
/*      */       }
/*      */ 
/*      */       public Integer getIdentifier(int paramInt) {
/*  955 */         return Integer.valueOf(paramInt);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private class DirectoriesFirstComparatorWrapper
/*      */     implements Comparator<File>
/*      */   {
/*      */     private Comparator comparator;
/*      */     private int column;
/*      */ 
/*      */     public DirectoriesFirstComparatorWrapper(int paramComparator, Comparator arg3)
/*      */     {
/*  969 */       this.column = paramComparator;
/*      */       Object localObject;
/*  970 */       this.comparator = localObject;
/*      */     }
/*      */ 
/*      */     public int compare(File paramFile1, File paramFile2) {
/*  974 */       if ((paramFile1 != null) && (paramFile2 != null)) {
/*  975 */         boolean bool1 = FilePane.this.getFileChooser().isTraversable(paramFile1);
/*  976 */         boolean bool2 = FilePane.this.getFileChooser().isTraversable(paramFile2);
/*      */ 
/*  978 */         if ((bool1) && (!bool2)) {
/*  979 */           return -1;
/*      */         }
/*  981 */         if ((!bool1) && (bool2)) {
/*  982 */           return 1;
/*      */         }
/*      */       }
/*  985 */       if (FilePane.this.detailsTableModel.getColumns()[this.column].isCompareByColumn()) {
/*  986 */         return this.comparator.compare(FilePane.access$900(FilePane.this).getFileColumnValue(paramFile1, this.column), FilePane.access$900(FilePane.this).getFileColumnValue(paramFile2, this.column));
/*      */       }
/*      */ 
/*  993 */       return this.comparator.compare(paramFile1, paramFile2);
/*      */     }
/*      */   }
/*      */ 
/*      */   class EditActionListener
/*      */     implements ActionListener
/*      */   {
/*      */     EditActionListener()
/*      */     {
/*      */     }
/*      */ 
/*      */     public void actionPerformed(ActionEvent paramActionEvent)
/*      */     {
/* 1396 */       FilePane.this.applyEdit();
/*      */     }
/*      */   }
/*      */ 
/*      */   public static abstract interface FileChooserUIAccessor
/*      */   {
/*      */     public abstract JFileChooser getFileChooser();
/*      */ 
/*      */     public abstract BasicDirectoryModel getModel();
/*      */ 
/*      */     public abstract JPanel createList();
/*      */ 
/*      */     public abstract JPanel createDetailsView();
/*      */ 
/*      */     public abstract boolean isDirectorySelected();
/*      */ 
/*      */     public abstract File getDirectory();
/*      */ 
/*      */     public abstract Action getApproveSelectionAction();
/*      */ 
/*      */     public abstract Action getChangeToParentDirectoryAction();
/*      */ 
/*      */     public abstract Action getNewFolderAction();
/*      */ 
/*      */     public abstract MouseListener createDoubleClickListener(JList paramJList);
/*      */ 
/*      */     public abstract ListSelectionListener createListSelectionListener();
/*      */   }
/*      */ 
/*      */   protected class FileRenderer extends DefaultListCellRenderer
/*      */   {
/*      */     protected FileRenderer()
/*      */     {
/*      */     }
/*      */ 
/*      */     public Component getListCellRendererComponent(JList paramJList, Object paramObject, int paramInt, boolean paramBoolean1, boolean paramBoolean2)
/*      */     {
/* 1488 */       if ((FilePane.this.listViewWindowsStyle) && (!paramJList.isFocusOwner())) {
/* 1489 */         paramBoolean1 = false;
/*      */       }
/*      */ 
/* 1492 */       super.getListCellRendererComponent(paramJList, paramObject, paramInt, paramBoolean1, paramBoolean2);
/* 1493 */       File localFile = (File)paramObject;
/* 1494 */       String str = FilePane.this.getFileChooser().getName(localFile);
/* 1495 */       setText(str);
/* 1496 */       setFont(paramJList.getFont());
/*      */ 
/* 1498 */       Icon localIcon = FilePane.this.getFileChooser().getIcon(localFile);
/* 1499 */       if (localIcon != null) {
/* 1500 */         setIcon(localIcon);
/*      */       }
/* 1502 */       else if (FilePane.this.getFileChooser().getFileSystemView().isTraversable(localFile).booleanValue()) {
/* 1503 */         setText(str + File.separator);
/*      */       }
/*      */ 
/* 1507 */       return this;
/*      */     }
/*      */   }
/*      */ 
/*      */   private class Handler
/*      */     implements MouseListener
/*      */   {
/*      */     private MouseListener doubleClickListener;
/*      */ 
/*      */     private Handler()
/*      */     {
/*      */     }
/*      */ 
/*      */     public void mouseClicked(MouseEvent paramMouseEvent)
/*      */     {
/* 1813 */       JComponent localJComponent = (JComponent)paramMouseEvent.getSource();
/*      */       int i;
/*      */       Object localObject;
/* 1816 */       if ((localJComponent instanceof JList)) {
/* 1817 */         i = SwingUtilities2.loc2IndexFileList(FilePane.this.list, paramMouseEvent.getPoint());
/* 1818 */       } else if ((localJComponent instanceof JTable)) {
/* 1819 */         localObject = (JTable)localJComponent;
/* 1820 */         Point localPoint = paramMouseEvent.getPoint();
/* 1821 */         i = ((JTable)localObject).rowAtPoint(localPoint);
/*      */ 
/* 1823 */         boolean bool = SwingUtilities2.pointOutsidePrefSize((JTable)localObject, i, ((JTable)localObject).columnAtPoint(localPoint), localPoint);
/*      */ 
/* 1827 */         if ((bool) && (!FilePane.this.fullRowSelection)) {
/* 1828 */           return;
/*      */         }
/*      */ 
/* 1832 */         if ((i >= 0) && (FilePane.this.list != null) && (FilePane.this.listSelectionModel.isSelectedIndex(i)))
/*      */         {
/* 1837 */           Rectangle localRectangle = FilePane.this.list.getCellBounds(i, i);
/* 1838 */           paramMouseEvent = new MouseEvent(FilePane.this.list, paramMouseEvent.getID(), paramMouseEvent.getWhen(), paramMouseEvent.getModifiers(), localRectangle.x + 1, localRectangle.y + localRectangle.height / 2, paramMouseEvent.getXOnScreen(), paramMouseEvent.getYOnScreen(), paramMouseEvent.getClickCount(), paramMouseEvent.isPopupTrigger(), paramMouseEvent.getButton());
/*      */         }
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/* 1847 */         return;
/*      */       }
/*      */ 
/* 1850 */       if ((i >= 0) && (SwingUtilities.isLeftMouseButton(paramMouseEvent))) {
/* 1851 */         localObject = FilePane.this.getFileChooser();
/*      */ 
/* 1854 */         if ((paramMouseEvent.getClickCount() == 1) && ((localJComponent instanceof JList))) {
/* 1855 */           if (((!((JFileChooser)localObject).isMultiSelectionEnabled()) || (((JFileChooser)localObject).getSelectedFiles().length <= 1)) && (i >= 0) && (FilePane.this.listSelectionModel.isSelectedIndex(i)) && (FilePane.this.getEditIndex() == i) && (FilePane.this.editFile == null))
/*      */           {
/* 1859 */             FilePane.this.editFileName(i);
/*      */           }
/* 1861 */           else if (i >= 0)
/* 1862 */             FilePane.this.setEditIndex(i);
/*      */           else {
/* 1864 */             FilePane.this.resetEditIndex();
/*      */           }
/*      */         }
/* 1867 */         else if (paramMouseEvent.getClickCount() == 2)
/*      */         {
/* 1870 */           FilePane.this.resetEditIndex();
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1875 */       if (getDoubleClickListener() != null)
/* 1876 */         getDoubleClickListener().mouseClicked(paramMouseEvent);
/*      */     }
/*      */ 
/*      */     public void mouseEntered(MouseEvent paramMouseEvent)
/*      */     {
/* 1881 */       JComponent localJComponent = (JComponent)paramMouseEvent.getSource();
/* 1882 */       if ((localJComponent instanceof JTable)) {
/* 1883 */         JTable localJTable = (JTable)paramMouseEvent.getSource();
/*      */ 
/* 1885 */         TransferHandler localTransferHandler1 = FilePane.this.getFileChooser().getTransferHandler();
/* 1886 */         TransferHandler localTransferHandler2 = localJTable.getTransferHandler();
/* 1887 */         if (localTransferHandler1 != localTransferHandler2) {
/* 1888 */           localJTable.setTransferHandler(localTransferHandler1);
/*      */         }
/*      */ 
/* 1891 */         boolean bool = FilePane.this.getFileChooser().getDragEnabled();
/* 1892 */         if (bool != localJTable.getDragEnabled())
/* 1893 */           localJTable.setDragEnabled(bool);
/*      */       }
/* 1895 */       else if ((localJComponent instanceof JList))
/*      */       {
/* 1897 */         if (getDoubleClickListener() != null)
/* 1898 */           getDoubleClickListener().mouseEntered(paramMouseEvent);
/*      */       }
/*      */     }
/*      */ 
/*      */     public void mouseExited(MouseEvent paramMouseEvent)
/*      */     {
/* 1904 */       if ((paramMouseEvent.getSource() instanceof JList))
/*      */       {
/* 1906 */         if (getDoubleClickListener() != null)
/* 1907 */           getDoubleClickListener().mouseExited(paramMouseEvent);
/*      */       }
/*      */     }
/*      */ 
/*      */     public void mousePressed(MouseEvent paramMouseEvent)
/*      */     {
/* 1913 */       if ((paramMouseEvent.getSource() instanceof JList))
/*      */       {
/* 1915 */         if (getDoubleClickListener() != null)
/* 1916 */           getDoubleClickListener().mousePressed(paramMouseEvent);
/*      */       }
/*      */     }
/*      */ 
/*      */     public void mouseReleased(MouseEvent paramMouseEvent)
/*      */     {
/* 1922 */       if ((paramMouseEvent.getSource() instanceof JList))
/*      */       {
/* 1924 */         if (getDoubleClickListener() != null)
/* 1925 */           getDoubleClickListener().mouseReleased(paramMouseEvent);
/*      */       }
/*      */     }
/*      */ 
/*      */     private MouseListener getDoubleClickListener()
/*      */     {
/* 1932 */       if ((this.doubleClickListener == null) && (FilePane.this.list != null)) {
/* 1933 */         this.doubleClickListener = FilePane.this.fileChooserUIAccessor.createDoubleClickListener(FilePane.this.list);
/*      */       }
/*      */ 
/* 1936 */       return this.doubleClickListener;
/*      */     }
/*      */   }
/*      */ 
/*      */   private class SortableListModel extends AbstractListModel
/*      */     implements TableModelListener, RowSorterListener
/*      */   {
/*      */     public SortableListModel()
/*      */     {
/*  658 */       FilePane.this.getDetailsTableModel().addTableModelListener(this);
/*  659 */       FilePane.this.getRowSorter().addRowSorterListener(this);
/*      */     }
/*      */ 
/*      */     public int getSize() {
/*  663 */       return FilePane.this.getModel().getSize();
/*      */     }
/*      */ 
/*      */     public Object getElementAt(int paramInt)
/*      */     {
/*  668 */       return FilePane.this.getModel().getElementAt(FilePane.this.getRowSorter().convertRowIndexToModel(paramInt));
/*      */     }
/*      */ 
/*      */     public void tableChanged(TableModelEvent paramTableModelEvent) {
/*  672 */       fireContentsChanged(this, 0, getSize());
/*      */     }
/*      */ 
/*      */     public void sorterChanged(RowSorterEvent paramRowSorterEvent) {
/*  676 */       fireContentsChanged(this, 0, getSize());
/*      */     }
/*      */   }
/*      */ 
/*      */   class ViewTypeAction extends AbstractAction
/*      */   {
/*      */     private int viewType;
/*      */ 
/*      */     ViewTypeAction(int arg2)
/*      */     {
/*  398 */       super();
/*  399 */       this.viewType = i;
/*      */       String str;
/*  402 */       switch (i) { case 0:
/*  403 */         str = "viewTypeList"; break;
/*      */       case 1:
/*  404 */         str = "viewTypeDetails"; break;
/*      */       default:
/*  405 */         str = (String)getValue("Name");
/*      */       }
/*  407 */       putValue("ActionCommandKey", str);
/*      */     }
/*      */ 
/*      */     public void actionPerformed(ActionEvent paramActionEvent) {
/*  411 */       FilePane.this.setViewType(this.viewType);
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.swing.FilePane
 * JD-Core Version:    0.6.2
 */