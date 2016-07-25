/*      */ package sun.print;
/*      */ 
/*      */ import java.awt.BorderLayout;
/*      */ import java.awt.Component;
/*      */ import java.awt.Container;
/*      */ import java.awt.Dialog;
/*      */ import java.awt.FlowLayout;
/*      */ import java.awt.Frame;
/*      */ import java.awt.GraphicsConfiguration;
/*      */ import java.awt.GridBagConstraints;
/*      */ import java.awt.GridBagLayout;
/*      */ import java.awt.Insets;
/*      */ import java.awt.event.ActionEvent;
/*      */ import java.awt.event.ActionListener;
/*      */ import java.awt.event.FocusEvent;
/*      */ import java.awt.event.FocusListener;
/*      */ import java.awt.event.ItemEvent;
/*      */ import java.awt.event.ItemListener;
/*      */ import java.awt.event.WindowAdapter;
/*      */ import java.awt.event.WindowEvent;
/*      */ import java.awt.print.PrinterJob;
/*      */ import java.io.File;
/*      */ import java.io.FilePermission;
/*      */ import java.io.IOException;
/*      */ import java.lang.reflect.Field;
/*      */ import java.net.URI;
/*      */ import java.net.URISyntaxException;
/*      */ import java.net.URL;
/*      */ import java.security.AccessController;
/*      */ import java.security.PrivilegedAction;
/*      */ import java.text.DecimalFormat;
/*      */ import java.util.Locale;
/*      */ import java.util.MissingResourceException;
/*      */ import java.util.ResourceBundle;
/*      */ import java.util.Vector;
/*      */ import javax.accessibility.AccessibleContext;
/*      */ import javax.print.DocFlavor;
/*      */ import javax.print.PrintService;
/*      */ import javax.print.ServiceUIFactory;
/*      */ import javax.print.attribute.Attribute;
/*      */ import javax.print.attribute.AttributeSet;
/*      */ import javax.print.attribute.HashPrintRequestAttributeSet;
/*      */ import javax.print.attribute.PrintRequestAttributeSet;
/*      */ import javax.print.attribute.PrintServiceAttribute;
/*      */ import javax.print.attribute.standard.Chromaticity;
/*      */ import javax.print.attribute.standard.Copies;
/*      */ import javax.print.attribute.standard.CopiesSupported;
/*      */ import javax.print.attribute.standard.Destination;
/*      */ import javax.print.attribute.standard.JobName;
/*      */ import javax.print.attribute.standard.JobPriority;
/*      */ import javax.print.attribute.standard.JobSheets;
/*      */ import javax.print.attribute.standard.Media;
/*      */ import javax.print.attribute.standard.MediaPrintableArea;
/*      */ import javax.print.attribute.standard.MediaSize;
/*      */ import javax.print.attribute.standard.MediaSizeName;
/*      */ import javax.print.attribute.standard.MediaTray;
/*      */ import javax.print.attribute.standard.OrientationRequested;
/*      */ import javax.print.attribute.standard.PageRanges;
/*      */ import javax.print.attribute.standard.PrintQuality;
/*      */ import javax.print.attribute.standard.PrinterInfo;
/*      */ import javax.print.attribute.standard.PrinterIsAcceptingJobs;
/*      */ import javax.print.attribute.standard.PrinterMakeAndModel;
/*      */ import javax.print.attribute.standard.RequestingUserName;
/*      */ import javax.print.attribute.standard.SheetCollate;
/*      */ import javax.print.attribute.standard.Sides;
/*      */ import javax.swing.AbstractAction;
/*      */ import javax.swing.AbstractButton;
/*      */ import javax.swing.ActionMap;
/*      */ import javax.swing.BorderFactory;
/*      */ import javax.swing.ButtonGroup;
/*      */ import javax.swing.Icon;
/*      */ import javax.swing.ImageIcon;
/*      */ import javax.swing.InputMap;
/*      */ import javax.swing.JButton;
/*      */ import javax.swing.JCheckBox;
/*      */ import javax.swing.JComboBox;
/*      */ import javax.swing.JDialog;
/*      */ import javax.swing.JFileChooser;
/*      */ import javax.swing.JFormattedTextField;
/*      */ import javax.swing.JLabel;
/*      */ import javax.swing.JOptionPane;
/*      */ import javax.swing.JPanel;
/*      */ import javax.swing.JRadioButton;
/*      */ import javax.swing.JRootPane;
/*      */ import javax.swing.JSpinner;
/*      */ import javax.swing.JSpinner.NumberEditor;
/*      */ import javax.swing.JTabbedPane;
/*      */ import javax.swing.JTextField;
/*      */ import javax.swing.KeyStroke;
/*      */ import javax.swing.SpinnerNumberModel;
/*      */ import javax.swing.border.EmptyBorder;
/*      */ import javax.swing.event.ChangeEvent;
/*      */ import javax.swing.event.ChangeListener;
/*      */ import javax.swing.event.PopupMenuEvent;
/*      */ import javax.swing.event.PopupMenuListener;
/*      */ import javax.swing.text.NumberFormatter;
/*      */ 
/*      */ public class ServiceDialog extends JDialog
/*      */   implements ActionListener
/*      */ {
/*      */   public static final int WAITING = 0;
/*      */   public static final int APPROVE = 1;
/*      */   public static final int CANCEL = 2;
/*      */   private static final String strBundle = "sun.print.resources.serviceui";
/*  102 */   private static final Insets panelInsets = new Insets(6, 6, 6, 6);
/*  103 */   private static final Insets compInsets = new Insets(3, 6, 3, 6);
/*      */   private static ResourceBundle messageRB;
/*      */   private JTabbedPane tpTabs;
/*      */   private JButton btnCancel;
/*      */   private JButton btnApprove;
/*      */   private PrintService[] services;
/*      */   private int defaultServiceIndex;
/*      */   private PrintRequestAttributeSet asOriginal;
/*      */   private HashPrintRequestAttributeSet asCurrent;
/*      */   private PrintService psCurrent;
/*      */   private DocFlavor docFlavor;
/*      */   private int status;
/*      */   private ValidatingFileChooser jfc;
/*      */   private GeneralPanel pnlGeneral;
/*      */   private PageSetupPanel pnlPageSetup;
/*      */   private AppearancePanel pnlAppearance;
/*  122 */   private boolean isAWT = false;
/*      */ 
/*  530 */   static Class _keyEventClazz = null;
/*      */ 
/*      */   public ServiceDialog(GraphicsConfiguration paramGraphicsConfiguration, int paramInt1, int paramInt2, PrintService[] paramArrayOfPrintService, int paramInt3, DocFlavor paramDocFlavor, PrintRequestAttributeSet paramPrintRequestAttributeSet, Dialog paramDialog)
/*      */   {
/*  140 */     super(paramDialog, getMsg("dialog.printtitle"), true, paramGraphicsConfiguration);
/*  141 */     initPrintDialog(paramInt1, paramInt2, paramArrayOfPrintService, paramInt3, paramDocFlavor, paramPrintRequestAttributeSet);
/*      */   }
/*      */ 
/*      */   public ServiceDialog(GraphicsConfiguration paramGraphicsConfiguration, int paramInt1, int paramInt2, PrintService[] paramArrayOfPrintService, int paramInt3, DocFlavor paramDocFlavor, PrintRequestAttributeSet paramPrintRequestAttributeSet, Frame paramFrame)
/*      */   {
/*  159 */     super(paramFrame, getMsg("dialog.printtitle"), true, paramGraphicsConfiguration);
/*  160 */     initPrintDialog(paramInt1, paramInt2, paramArrayOfPrintService, paramInt3, paramDocFlavor, paramPrintRequestAttributeSet);
/*      */   }
/*      */ 
/*      */   void initPrintDialog(int paramInt1, int paramInt2, PrintService[] paramArrayOfPrintService, int paramInt3, DocFlavor paramDocFlavor, PrintRequestAttributeSet paramPrintRequestAttributeSet)
/*      */   {
/*  174 */     this.services = paramArrayOfPrintService;
/*  175 */     this.defaultServiceIndex = paramInt3;
/*  176 */     this.asOriginal = paramPrintRequestAttributeSet;
/*  177 */     this.asCurrent = new HashPrintRequestAttributeSet(paramPrintRequestAttributeSet);
/*  178 */     this.psCurrent = paramArrayOfPrintService[paramInt3];
/*  179 */     this.docFlavor = paramDocFlavor;
/*  180 */     SunPageSelection localSunPageSelection = (SunPageSelection)paramPrintRequestAttributeSet.get(SunPageSelection.class);
/*      */ 
/*  182 */     if (localSunPageSelection != null) {
/*  183 */       this.isAWT = true;
/*      */     }
/*      */ 
/*  186 */     Container localContainer = getContentPane();
/*  187 */     localContainer.setLayout(new BorderLayout());
/*      */ 
/*  189 */     this.tpTabs = new JTabbedPane();
/*  190 */     this.tpTabs.setBorder(new EmptyBorder(5, 5, 5, 5));
/*      */ 
/*  192 */     String str1 = getMsg("tab.general");
/*  193 */     int i = getVKMnemonic("tab.general");
/*  194 */     this.pnlGeneral = new GeneralPanel();
/*  195 */     this.tpTabs.add(str1, this.pnlGeneral);
/*  196 */     this.tpTabs.setMnemonicAt(0, i);
/*      */ 
/*  198 */     String str2 = getMsg("tab.pagesetup");
/*  199 */     int j = getVKMnemonic("tab.pagesetup");
/*  200 */     this.pnlPageSetup = new PageSetupPanel();
/*  201 */     this.tpTabs.add(str2, this.pnlPageSetup);
/*  202 */     this.tpTabs.setMnemonicAt(1, j);
/*      */ 
/*  204 */     String str3 = getMsg("tab.appearance");
/*  205 */     int k = getVKMnemonic("tab.appearance");
/*  206 */     this.pnlAppearance = new AppearancePanel();
/*  207 */     this.tpTabs.add(str3, this.pnlAppearance);
/*  208 */     this.tpTabs.setMnemonicAt(2, k);
/*      */ 
/*  210 */     localContainer.add(this.tpTabs, "Center");
/*      */ 
/*  212 */     updatePanels();
/*      */ 
/*  214 */     JPanel localJPanel = new JPanel(new FlowLayout(4));
/*  215 */     this.btnApprove = createExitButton("button.print", this);
/*  216 */     localJPanel.add(this.btnApprove);
/*  217 */     getRootPane().setDefaultButton(this.btnApprove);
/*  218 */     this.btnCancel = createExitButton("button.cancel", this);
/*  219 */     handleEscKey(this.btnCancel);
/*  220 */     localJPanel.add(this.btnCancel);
/*  221 */     localContainer.add(localJPanel, "South");
/*      */ 
/*  223 */     addWindowListener(new WindowAdapter() {
/*      */       public void windowClosing(WindowEvent paramAnonymousWindowEvent) {
/*  225 */         ServiceDialog.this.dispose(2);
/*      */       }
/*      */     });
/*  229 */     getAccessibleContext().setAccessibleDescription(getMsg("dialog.printtitle"));
/*  230 */     setResizable(false);
/*  231 */     setLocation(paramInt1, paramInt2);
/*  232 */     pack();
/*      */   }
/*      */ 
/*      */   public ServiceDialog(GraphicsConfiguration paramGraphicsConfiguration, int paramInt1, int paramInt2, PrintService paramPrintService, DocFlavor paramDocFlavor, PrintRequestAttributeSet paramPrintRequestAttributeSet, Dialog paramDialog)
/*      */   {
/*  245 */     super(paramDialog, getMsg("dialog.pstitle"), true, paramGraphicsConfiguration);
/*  246 */     initPageDialog(paramInt1, paramInt2, paramPrintService, paramDocFlavor, paramPrintRequestAttributeSet);
/*      */   }
/*      */ 
/*      */   public ServiceDialog(GraphicsConfiguration paramGraphicsConfiguration, int paramInt1, int paramInt2, PrintService paramPrintService, DocFlavor paramDocFlavor, PrintRequestAttributeSet paramPrintRequestAttributeSet, Frame paramFrame)
/*      */   {
/*  259 */     super(paramFrame, getMsg("dialog.pstitle"), true, paramGraphicsConfiguration);
/*  260 */     initPageDialog(paramInt1, paramInt2, paramPrintService, paramDocFlavor, paramPrintRequestAttributeSet);
/*      */   }
/*      */ 
/*      */   void initPageDialog(int paramInt1, int paramInt2, PrintService paramPrintService, DocFlavor paramDocFlavor, PrintRequestAttributeSet paramPrintRequestAttributeSet)
/*      */   {
/*  272 */     this.psCurrent = paramPrintService;
/*  273 */     this.docFlavor = paramDocFlavor;
/*  274 */     this.asOriginal = paramPrintRequestAttributeSet;
/*  275 */     this.asCurrent = new HashPrintRequestAttributeSet(paramPrintRequestAttributeSet);
/*      */ 
/*  277 */     Container localContainer = getContentPane();
/*  278 */     localContainer.setLayout(new BorderLayout());
/*      */ 
/*  280 */     this.pnlPageSetup = new PageSetupPanel();
/*  281 */     localContainer.add(this.pnlPageSetup, "Center");
/*      */ 
/*  283 */     this.pnlPageSetup.updateInfo();
/*      */ 
/*  285 */     JPanel localJPanel = new JPanel(new FlowLayout(4));
/*  286 */     this.btnApprove = createExitButton("button.ok", this);
/*  287 */     localJPanel.add(this.btnApprove);
/*  288 */     getRootPane().setDefaultButton(this.btnApprove);
/*  289 */     this.btnCancel = createExitButton("button.cancel", this);
/*  290 */     handleEscKey(this.btnCancel);
/*  291 */     localJPanel.add(this.btnCancel);
/*  292 */     localContainer.add(localJPanel, "South");
/*      */ 
/*  294 */     addWindowListener(new WindowAdapter() {
/*      */       public void windowClosing(WindowEvent paramAnonymousWindowEvent) {
/*  296 */         ServiceDialog.this.dispose(2);
/*      */       }
/*      */     });
/*  300 */     getAccessibleContext().setAccessibleDescription(getMsg("dialog.pstitle"));
/*  301 */     setResizable(false);
/*  302 */     setLocation(paramInt1, paramInt2);
/*  303 */     pack();
/*      */   }
/*      */ 
/*      */   private void handleEscKey(JButton paramJButton)
/*      */   {
/*  310 */     AbstractAction local3 = new AbstractAction() {
/*      */       public void actionPerformed(ActionEvent paramAnonymousActionEvent) {
/*  312 */         ServiceDialog.this.dispose(2);
/*      */       }
/*      */     };
/*  315 */     KeyStroke localKeyStroke = KeyStroke.getKeyStroke(27, 0);
/*      */ 
/*  317 */     InputMap localInputMap = paramJButton.getInputMap(2);
/*      */ 
/*  319 */     ActionMap localActionMap = paramJButton.getActionMap();
/*      */ 
/*  321 */     if ((localInputMap != null) && (localActionMap != null)) {
/*  322 */       localInputMap.put(localKeyStroke, "cancel");
/*  323 */       localActionMap.put("cancel", local3);
/*      */     }
/*      */   }
/*      */ 
/*      */   public int getStatus()
/*      */   {
/*  333 */     return this.status;
/*      */   }
/*      */ 
/*      */   public PrintRequestAttributeSet getAttributes()
/*      */   {
/*  342 */     if (this.status == 1) {
/*  343 */       return this.asCurrent;
/*      */     }
/*  345 */     return this.asOriginal;
/*      */   }
/*      */ 
/*      */   public PrintService getPrintService()
/*      */   {
/*  355 */     if (this.status == 1) {
/*  356 */       return this.psCurrent;
/*      */     }
/*  358 */     return null;
/*      */   }
/*      */ 
/*      */   public void dispose(int paramInt)
/*      */   {
/*  367 */     this.status = paramInt;
/*      */ 
/*  369 */     super.dispose();
/*      */   }
/*      */ 
/*      */   public void actionPerformed(ActionEvent paramActionEvent) {
/*  373 */     Object localObject = paramActionEvent.getSource();
/*  374 */     boolean bool = false;
/*      */ 
/*  376 */     if (localObject == this.btnApprove) {
/*  377 */       bool = true;
/*      */ 
/*  379 */       if (this.pnlGeneral != null) {
/*  380 */         if (this.pnlGeneral.isPrintToFileRequested())
/*  381 */           bool = showFileChooser();
/*      */         else {
/*  383 */           this.asCurrent.remove(Destination.class);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  388 */     dispose(bool ? 1 : 2);
/*      */   }
/*      */ 
/*      */   private boolean showFileChooser()
/*      */   {
/*  396 */     Destination localDestination1 = Destination.class;
/*      */ 
/*  398 */     Destination localDestination2 = (Destination)this.asCurrent.get(localDestination1);
/*  399 */     if (localDestination2 == null) {
/*  400 */       localDestination2 = (Destination)this.asOriginal.get(localDestination1);
/*  401 */       if (localDestination2 == null) {
/*  402 */         localDestination2 = (Destination)this.psCurrent.getDefaultAttributeValue(localDestination1);
/*      */ 
/*  407 */         if (localDestination2 == null)
/*      */           try {
/*  409 */             localDestination2 = new Destination(new URI("file:out.prn"));
/*      */           }
/*      */           catch (URISyntaxException localURISyntaxException)
/*      */           {
/*      */           }
/*      */       }
/*      */     }
/*      */     File localFile;
/*  417 */     if (localDestination2 != null)
/*      */       try {
/*  419 */         localFile = new File(localDestination2.getURI());
/*      */       }
/*      */       catch (Exception localException1) {
/*  422 */         localFile = new File("out.prn");
/*      */       }
/*      */     else {
/*  425 */       localFile = new File("out.prn");
/*      */     }
/*      */ 
/*  428 */     ValidatingFileChooser localValidatingFileChooser = new ValidatingFileChooser(null);
/*  429 */     localValidatingFileChooser.setApproveButtonText(getMsg("button.ok"));
/*  430 */     localValidatingFileChooser.setDialogTitle(getMsg("dialog.printtofile"));
/*  431 */     localValidatingFileChooser.setDialogType(1);
/*  432 */     localValidatingFileChooser.setSelectedFile(localFile);
/*      */ 
/*  434 */     int i = localValidatingFileChooser.showDialog(this, null);
/*  435 */     if (i == 0) {
/*  436 */       localFile = localValidatingFileChooser.getSelectedFile();
/*      */       try
/*      */       {
/*  439 */         this.asCurrent.add(new Destination(localFile.toURI()));
/*      */       } catch (Exception localException2) {
/*  441 */         this.asCurrent.remove(localDestination1);
/*      */       }
/*      */     } else {
/*  444 */       this.asCurrent.remove(localDestination1);
/*      */     }
/*      */ 
/*  447 */     return i == 0;
/*      */   }
/*      */ 
/*      */   private void updatePanels()
/*      */   {
/*  454 */     this.pnlGeneral.updateInfo();
/*  455 */     this.pnlPageSetup.updateInfo();
/*  456 */     this.pnlAppearance.updateInfo();
/*      */   }
/*      */ 
/*      */   public static void initResource()
/*      */   {
/*  463 */     AccessController.doPrivileged(new PrivilegedAction()
/*      */     {
/*      */       public Object run() {
/*      */         try {
/*  467 */           ServiceDialog.access$102(ResourceBundle.getBundle("sun.print.resources.serviceui"));
/*  468 */           return null; } catch (MissingResourceException localMissingResourceException) {
/*      */         }
/*  470 */         throw new Error("Fatal: Resource for ServiceUI is missing");
/*      */       }
/*      */     });
/*      */   }
/*      */ 
/*      */   public static String getMsg(String paramString)
/*      */   {
/*      */     try
/*      */     {
/*  483 */       return removeMnemonics(messageRB.getString(paramString)); } catch (MissingResourceException localMissingResourceException) {
/*      */     }
/*  485 */     throw new Error("Fatal: Resource for ServiceUI is broken; there is no " + paramString + " key in resource");
/*      */   }
/*      */ 
/*      */   private static String removeMnemonics(String paramString)
/*      */   {
/*  491 */     int i = paramString.indexOf('&');
/*  492 */     int j = paramString.length();
/*  493 */     if ((i < 0) || (i == j - 1)) {
/*  494 */       return paramString;
/*      */     }
/*  496 */     int k = paramString.indexOf('&', i + 1);
/*  497 */     if (k == i + 1) {
/*  498 */       if (k + 1 == j) {
/*  499 */         return paramString.substring(0, i + 1);
/*      */       }
/*  501 */       return paramString.substring(0, i + 1) + removeMnemonics(paramString.substring(k + 1));
/*      */     }
/*      */ 
/*  505 */     if (i == 0) {
/*  506 */       return removeMnemonics(paramString.substring(1));
/*      */     }
/*  508 */     return paramString.substring(0, i) + removeMnemonics(paramString.substring(i + 1));
/*      */   }
/*      */ 
/*      */   private static char getMnemonic(String paramString)
/*      */   {
/*  517 */     String str = messageRB.getString(paramString).replace("&&", "");
/*  518 */     int i = str.indexOf('&');
/*  519 */     if ((0 <= i) && (i < str.length() - 1)) {
/*  520 */       char c = str.charAt(i + 1);
/*  521 */       return Character.toUpperCase(c);
/*      */     }
/*  523 */     return '\000';
/*      */   }
/*      */ 
/*      */   private static int getVKMnemonic(String paramString)
/*      */   {
/*  532 */     String str1 = String.valueOf(getMnemonic(paramString));
/*  533 */     if ((str1 == null) || (str1.length() != 1)) {
/*  534 */       return 0;
/*      */     }
/*  536 */     String str2 = "VK_" + str1.toUpperCase();
/*      */     try
/*      */     {
/*  539 */       if (_keyEventClazz == null) {
/*  540 */         _keyEventClazz = Class.forName("java.awt.event.KeyEvent", true, ServiceDialog.class.getClassLoader());
/*      */       }
/*      */ 
/*  543 */       Field localField = _keyEventClazz.getDeclaredField(str2);
/*  544 */       return localField.getInt(null);
/*      */     }
/*      */     catch (Exception localException) {
/*      */     }
/*  548 */     return 0;
/*      */   }
/*      */ 
/*      */   private static URL getImageResource(String paramString)
/*      */   {
/*  555 */     URL localURL = (URL)AccessController.doPrivileged(new PrivilegedAction()
/*      */     {
/*      */       public Object run() {
/*  558 */         URL localURL = ServiceDialog.class.getResource("resources/" + this.val$key);
/*      */ 
/*  560 */         return localURL;
/*      */       }
/*      */     });
/*  564 */     if (localURL == null) {
/*  565 */       throw new Error("Fatal: Resource for ServiceUI is broken; there is no " + paramString + " key in resource");
/*      */     }
/*      */ 
/*  569 */     return localURL;
/*      */   }
/*      */ 
/*      */   private static JButton createButton(String paramString, ActionListener paramActionListener)
/*      */   {
/*  576 */     JButton localJButton = new JButton(getMsg(paramString));
/*  577 */     localJButton.setMnemonic(getMnemonic(paramString));
/*  578 */     localJButton.addActionListener(paramActionListener);
/*      */ 
/*  580 */     return localJButton;
/*      */   }
/*      */ 
/*      */   private static JButton createExitButton(String paramString, ActionListener paramActionListener)
/*      */   {
/*  587 */     String str = getMsg(paramString);
/*  588 */     JButton localJButton = new JButton(str);
/*  589 */     localJButton.addActionListener(paramActionListener);
/*  590 */     localJButton.getAccessibleContext().setAccessibleDescription(str);
/*  591 */     return localJButton;
/*      */   }
/*      */ 
/*      */   private static JCheckBox createCheckBox(String paramString, ActionListener paramActionListener)
/*      */   {
/*  598 */     JCheckBox localJCheckBox = new JCheckBox(getMsg(paramString));
/*  599 */     localJCheckBox.setMnemonic(getMnemonic(paramString));
/*  600 */     localJCheckBox.addActionListener(paramActionListener);
/*      */ 
/*  602 */     return localJCheckBox;
/*      */   }
/*      */ 
/*      */   private static JRadioButton createRadioButton(String paramString, ActionListener paramActionListener)
/*      */   {
/*  612 */     JRadioButton localJRadioButton = new JRadioButton(getMsg(paramString));
/*  613 */     localJRadioButton.setMnemonic(getMnemonic(paramString));
/*  614 */     localJRadioButton.addActionListener(paramActionListener);
/*      */ 
/*  616 */     return localJRadioButton;
/*      */   }
/*      */ 
/*      */   public static void showNoPrintService(GraphicsConfiguration paramGraphicsConfiguration)
/*      */   {
/*  624 */     Frame localFrame = new Frame(paramGraphicsConfiguration);
/*  625 */     JOptionPane.showMessageDialog(localFrame, getMsg("dialog.noprintermsg"));
/*      */ 
/*  627 */     localFrame.dispose();
/*      */   }
/*      */ 
/*      */   private static void addToGB(Component paramComponent, Container paramContainer, GridBagLayout paramGridBagLayout, GridBagConstraints paramGridBagConstraints)
/*      */   {
/*  638 */     paramGridBagLayout.setConstraints(paramComponent, paramGridBagConstraints);
/*  639 */     paramContainer.add(paramComponent);
/*      */   }
/*      */ 
/*      */   private static void addToBG(AbstractButton paramAbstractButton, Container paramContainer, ButtonGroup paramButtonGroup)
/*      */   {
/*  648 */     paramButtonGroup.add(paramAbstractButton);
/*  649 */     paramContainer.add(paramAbstractButton);
/*      */   }
/*      */ 
/*      */   static
/*      */   {
/*  124 */     initResource();
/*      */   }
/*      */ 
/*      */   private class AppearancePanel extends JPanel
/*      */   {
/*      */     private ServiceDialog.ChromaticityPanel pnlChromaticity;
/*      */     private ServiceDialog.QualityPanel pnlQuality;
/*      */     private ServiceDialog.JobAttributesPanel pnlJobAttributes;
/*      */     private ServiceDialog.SidesPanel pnlSides;
/*      */ 
/*      */     public AppearancePanel()
/*      */     {
/* 2277 */       GridBagLayout localGridBagLayout = new GridBagLayout();
/* 2278 */       GridBagConstraints localGridBagConstraints = new GridBagConstraints();
/*      */ 
/* 2280 */       setLayout(localGridBagLayout);
/*      */ 
/* 2282 */       localGridBagConstraints.fill = 1;
/* 2283 */       localGridBagConstraints.insets = ServiceDialog.panelInsets;
/* 2284 */       localGridBagConstraints.weightx = 1.0D;
/* 2285 */       localGridBagConstraints.weighty = 1.0D;
/*      */ 
/* 2287 */       localGridBagConstraints.gridwidth = -1;
/* 2288 */       this.pnlChromaticity = new ServiceDialog.ChromaticityPanel(ServiceDialog.this);
/* 2289 */       ServiceDialog.addToGB(this.pnlChromaticity, this, localGridBagLayout, localGridBagConstraints);
/*      */ 
/* 2291 */       localGridBagConstraints.gridwidth = 0;
/* 2292 */       this.pnlQuality = new ServiceDialog.QualityPanel(ServiceDialog.this);
/* 2293 */       ServiceDialog.addToGB(this.pnlQuality, this, localGridBagLayout, localGridBagConstraints);
/*      */ 
/* 2295 */       localGridBagConstraints.gridwidth = 1;
/* 2296 */       this.pnlSides = new ServiceDialog.SidesPanel(ServiceDialog.this);
/* 2297 */       ServiceDialog.addToGB(this.pnlSides, this, localGridBagLayout, localGridBagConstraints);
/*      */ 
/* 2299 */       localGridBagConstraints.gridwidth = 0;
/* 2300 */       this.pnlJobAttributes = new ServiceDialog.JobAttributesPanel(ServiceDialog.this);
/* 2301 */       ServiceDialog.addToGB(this.pnlJobAttributes, this, localGridBagLayout, localGridBagConstraints);
/*      */     }
/*      */ 
/*      */     public void updateInfo()
/*      */     {
/* 2306 */       this.pnlChromaticity.updateInfo();
/* 2307 */       this.pnlQuality.updateInfo();
/* 2308 */       this.pnlSides.updateInfo();
/* 2309 */       this.pnlJobAttributes.updateInfo();
/*      */     }
/*      */   }
/*      */ 
/*      */   private class ChromaticityPanel extends JPanel implements ActionListener
/*      */   {
/* 2316 */     private final String strTitle = ServiceDialog.getMsg("border.chromaticity");
/*      */     private JRadioButton rbMonochrome;
/*      */     private JRadioButton rbColor;
/*      */ 
/*      */     public ChromaticityPanel() {
/* 2322 */       GridBagLayout localGridBagLayout = new GridBagLayout();
/* 2323 */       GridBagConstraints localGridBagConstraints = new GridBagConstraints();
/*      */ 
/* 2325 */       setLayout(localGridBagLayout);
/* 2326 */       setBorder(BorderFactory.createTitledBorder(this.strTitle));
/*      */ 
/* 2328 */       localGridBagConstraints.fill = 1;
/* 2329 */       localGridBagConstraints.gridwidth = 0;
/* 2330 */       localGridBagConstraints.weighty = 1.0D;
/*      */ 
/* 2332 */       ButtonGroup localButtonGroup = new ButtonGroup();
/* 2333 */       this.rbMonochrome = ServiceDialog.createRadioButton("radiobutton.monochrome", this);
/* 2334 */       this.rbMonochrome.setSelected(true);
/* 2335 */       localButtonGroup.add(this.rbMonochrome);
/* 2336 */       ServiceDialog.addToGB(this.rbMonochrome, this, localGridBagLayout, localGridBagConstraints);
/* 2337 */       this.rbColor = ServiceDialog.createRadioButton("radiobutton.color", this);
/* 2338 */       localButtonGroup.add(this.rbColor);
/* 2339 */       ServiceDialog.addToGB(this.rbColor, this, localGridBagLayout, localGridBagConstraints);
/*      */     }
/*      */ 
/*      */     public void actionPerformed(ActionEvent paramActionEvent) {
/* 2343 */       Object localObject = paramActionEvent.getSource();
/*      */ 
/* 2346 */       if (localObject == this.rbMonochrome)
/* 2347 */         ServiceDialog.this.asCurrent.add(Chromaticity.MONOCHROME);
/* 2348 */       else if (localObject == this.rbColor)
/* 2349 */         ServiceDialog.this.asCurrent.add(Chromaticity.COLOR);
/*      */     }
/*      */ 
/*      */     public void updateInfo()
/*      */     {
/* 2354 */       Chromaticity localChromaticity1 = Chromaticity.class;
/* 2355 */       boolean bool1 = false;
/* 2356 */       boolean bool2 = false;
/*      */ 
/* 2358 */       if (ServiceDialog.this.isAWT) {
/* 2359 */         bool1 = true;
/* 2360 */         bool2 = true;
/*      */       }
/* 2362 */       else if (ServiceDialog.this.psCurrent.isAttributeCategorySupported(localChromaticity1)) {
/* 2363 */         localObject = ServiceDialog.this.psCurrent.getSupportedAttributeValues(localChromaticity1, ServiceDialog.this.docFlavor, ServiceDialog.this.asCurrent);
/*      */ 
/* 2368 */         if ((localObject instanceof Chromaticity[])) {
/* 2369 */           Chromaticity[] arrayOfChromaticity = (Chromaticity[])localObject;
/*      */ 
/* 2371 */           for (int i = 0; i < arrayOfChromaticity.length; i++) {
/* 2372 */             Chromaticity localChromaticity2 = arrayOfChromaticity[i];
/*      */ 
/* 2374 */             if (localChromaticity2 == Chromaticity.MONOCHROME)
/* 2375 */               bool1 = true;
/* 2376 */             else if (localChromaticity2 == Chromaticity.COLOR) {
/* 2377 */               bool2 = true;
/*      */             }
/*      */           }
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 2384 */       this.rbMonochrome.setEnabled(bool1);
/* 2385 */       this.rbColor.setEnabled(bool2);
/*      */ 
/* 2387 */       Object localObject = (Chromaticity)ServiceDialog.this.asCurrent.get(localChromaticity1);
/* 2388 */       if (localObject == null) {
/* 2389 */         localObject = (Chromaticity)ServiceDialog.this.psCurrent.getDefaultAttributeValue(localChromaticity1);
/* 2390 */         if (localObject == null) {
/* 2391 */           localObject = Chromaticity.MONOCHROME;
/*      */         }
/*      */       }
/*      */ 
/* 2395 */       if (localObject == Chromaticity.MONOCHROME)
/* 2396 */         this.rbMonochrome.setSelected(true);
/*      */       else
/* 2398 */         this.rbColor.setSelected(true);
/*      */     }
/*      */   }
/*      */ 
/*      */   private class CopiesPanel extends JPanel
/*      */     implements ActionListener, ChangeListener
/*      */   {
/* 1174 */     private final String strTitle = ServiceDialog.getMsg("border.copies");
/*      */     private SpinnerNumberModel snModel;
/*      */     private JSpinner spinCopies;
/*      */     private JLabel lblCopies;
/*      */     private JCheckBox cbCollate;
/*      */     private boolean scSupported;
/*      */ 
/*      */     public CopiesPanel()
/*      */     {
/* 1184 */       GridBagLayout localGridBagLayout = new GridBagLayout();
/* 1185 */       GridBagConstraints localGridBagConstraints = new GridBagConstraints();
/*      */ 
/* 1187 */       setLayout(localGridBagLayout);
/* 1188 */       setBorder(BorderFactory.createTitledBorder(this.strTitle));
/*      */ 
/* 1190 */       localGridBagConstraints.fill = 2;
/* 1191 */       localGridBagConstraints.insets = ServiceDialog.compInsets;
/*      */ 
/* 1193 */       this.lblCopies = new JLabel(ServiceDialog.getMsg("label.numcopies"), 11);
/* 1194 */       this.lblCopies.setDisplayedMnemonic(ServiceDialog.getMnemonic("label.numcopies"));
/* 1195 */       this.lblCopies.getAccessibleContext().setAccessibleName(ServiceDialog.getMsg("label.numcopies"));
/*      */ 
/* 1197 */       ServiceDialog.addToGB(this.lblCopies, this, localGridBagLayout, localGridBagConstraints);
/*      */ 
/* 1199 */       this.snModel = new SpinnerNumberModel(1, 1, 999, 1);
/* 1200 */       this.spinCopies = new JSpinner(this.snModel);
/* 1201 */       this.lblCopies.setLabelFor(this.spinCopies);
/*      */ 
/* 1203 */       ((JSpinner.NumberEditor)this.spinCopies.getEditor()).getTextField().setColumns(3);
/* 1204 */       this.spinCopies.addChangeListener(this);
/* 1205 */       localGridBagConstraints.gridwidth = 0;
/* 1206 */       ServiceDialog.addToGB(this.spinCopies, this, localGridBagLayout, localGridBagConstraints);
/*      */ 
/* 1208 */       this.cbCollate = ServiceDialog.createCheckBox("checkbox.collate", this);
/* 1209 */       this.cbCollate.setEnabled(false);
/* 1210 */       ServiceDialog.addToGB(this.cbCollate, this, localGridBagLayout, localGridBagConstraints);
/*      */     }
/*      */ 
/*      */     public void actionPerformed(ActionEvent paramActionEvent) {
/* 1214 */       if (this.cbCollate.isSelected())
/* 1215 */         ServiceDialog.this.asCurrent.add(SheetCollate.COLLATED);
/*      */       else
/* 1217 */         ServiceDialog.this.asCurrent.add(SheetCollate.UNCOLLATED);
/*      */     }
/*      */ 
/*      */     public void stateChanged(ChangeEvent paramChangeEvent)
/*      */     {
/* 1222 */       updateCollateCB();
/*      */ 
/* 1224 */       ServiceDialog.this.asCurrent.add(new Copies(this.snModel.getNumber().intValue()));
/*      */     }
/*      */ 
/*      */     private void updateCollateCB() {
/* 1228 */       int i = this.snModel.getNumber().intValue();
/* 1229 */       if (ServiceDialog.this.isAWT)
/* 1230 */         this.cbCollate.setEnabled(true);
/*      */       else
/* 1232 */         this.cbCollate.setEnabled((i > 1) && (this.scSupported));
/*      */     }
/*      */ 
/*      */     public void updateInfo()
/*      */     {
/* 1237 */       Copies localCopies1 = Copies.class;
/* 1238 */       CopiesSupported localCopiesSupported1 = CopiesSupported.class;
/* 1239 */       SheetCollate localSheetCollate1 = SheetCollate.class;
/* 1240 */       boolean bool = false;
/* 1241 */       this.scSupported = false;
/*      */ 
/* 1244 */       if (ServiceDialog.this.psCurrent.isAttributeCategorySupported(localCopies1)) {
/* 1245 */         bool = true;
/*      */       }
/* 1247 */       CopiesSupported localCopiesSupported2 = (CopiesSupported)ServiceDialog.this.psCurrent.getSupportedAttributeValues(localCopies1, null, null);
/*      */ 
/* 1250 */       if (localCopiesSupported2 == null) {
/* 1251 */         localCopiesSupported2 = new CopiesSupported(1, 999);
/*      */       }
/* 1253 */       Copies localCopies2 = (Copies)ServiceDialog.this.asCurrent.get(localCopies1);
/* 1254 */       if (localCopies2 == null) {
/* 1255 */         localCopies2 = (Copies)ServiceDialog.this.psCurrent.getDefaultAttributeValue(localCopies1);
/* 1256 */         if (localCopies2 == null) {
/* 1257 */           localCopies2 = new Copies(1);
/*      */         }
/*      */       }
/* 1260 */       this.spinCopies.setEnabled(bool);
/* 1261 */       this.lblCopies.setEnabled(bool);
/*      */ 
/* 1263 */       int[][] arrayOfInt = localCopiesSupported2.getMembers();
/*      */       int i;
/*      */       int j;
/* 1265 */       if ((arrayOfInt.length > 0) && (arrayOfInt[0].length > 0)) {
/* 1266 */         i = arrayOfInt[0][0];
/* 1267 */         j = arrayOfInt[0][1];
/*      */       } else {
/* 1269 */         i = 1;
/* 1270 */         j = 2147483647;
/*      */       }
/* 1272 */       this.snModel.setMinimum(new Integer(i));
/* 1273 */       this.snModel.setMaximum(new Integer(j));
/*      */ 
/* 1275 */       int k = localCopies2.getValue();
/* 1276 */       if ((k < i) || (k > j)) {
/* 1277 */         k = i;
/*      */       }
/* 1279 */       this.snModel.setValue(new Integer(k));
/*      */ 
/* 1282 */       if (ServiceDialog.this.psCurrent.isAttributeCategorySupported(localSheetCollate1)) {
/* 1283 */         this.scSupported = true;
/*      */       }
/* 1285 */       SheetCollate localSheetCollate2 = (SheetCollate)ServiceDialog.this.asCurrent.get(localSheetCollate1);
/* 1286 */       if (localSheetCollate2 == null) {
/* 1287 */         localSheetCollate2 = (SheetCollate)ServiceDialog.this.psCurrent.getDefaultAttributeValue(localSheetCollate1);
/* 1288 */         if (localSheetCollate2 == null) {
/* 1289 */           localSheetCollate2 = SheetCollate.UNCOLLATED;
/*      */         }
/*      */       }
/* 1292 */       this.cbCollate.setSelected(localSheetCollate2 == SheetCollate.COLLATED);
/* 1293 */       updateCollateCB();
/*      */     }
/*      */   }
/*      */ 
/*      */   private class GeneralPanel extends JPanel
/*      */   {
/*      */     private ServiceDialog.PrintServicePanel pnlPrintService;
/*      */     private ServiceDialog.PrintRangePanel pnlPrintRange;
/*      */     private ServiceDialog.CopiesPanel pnlCopies;
/*      */ 
/*      */     public GeneralPanel()
/*      */     {
/*  668 */       GridBagLayout localGridBagLayout = new GridBagLayout();
/*  669 */       GridBagConstraints localGridBagConstraints = new GridBagConstraints();
/*      */ 
/*  671 */       setLayout(localGridBagLayout);
/*      */ 
/*  673 */       localGridBagConstraints.fill = 1;
/*  674 */       localGridBagConstraints.insets = ServiceDialog.panelInsets;
/*  675 */       localGridBagConstraints.weightx = 1.0D;
/*  676 */       localGridBagConstraints.weighty = 1.0D;
/*      */ 
/*  678 */       localGridBagConstraints.gridwidth = 0;
/*  679 */       this.pnlPrintService = new ServiceDialog.PrintServicePanel(ServiceDialog.this);
/*  680 */       ServiceDialog.addToGB(this.pnlPrintService, this, localGridBagLayout, localGridBagConstraints);
/*      */ 
/*  682 */       localGridBagConstraints.gridwidth = -1;
/*  683 */       this.pnlPrintRange = new ServiceDialog.PrintRangePanel(ServiceDialog.this);
/*  684 */       ServiceDialog.addToGB(this.pnlPrintRange, this, localGridBagLayout, localGridBagConstraints);
/*      */ 
/*  686 */       localGridBagConstraints.gridwidth = 0;
/*  687 */       this.pnlCopies = new ServiceDialog.CopiesPanel(ServiceDialog.this);
/*  688 */       ServiceDialog.addToGB(this.pnlCopies, this, localGridBagLayout, localGridBagConstraints);
/*      */     }
/*      */ 
/*      */     public boolean isPrintToFileRequested() {
/*  692 */       return this.pnlPrintService.isPrintToFileSelected();
/*      */     }
/*      */ 
/*      */     public void updateInfo() {
/*  696 */       this.pnlPrintService.updateInfo();
/*  697 */       this.pnlPrintRange.updateInfo();
/*  698 */       this.pnlCopies.updateInfo();
/*      */     }
/*      */   }
/*      */ 
/*      */   private class IconRadioButton extends JPanel
/*      */   {
/*      */     private JRadioButton rb;
/*      */     private JLabel lbl;
/*      */ 
/*      */     public IconRadioButton(String paramString1, String paramBoolean, boolean paramButtonGroup, ButtonGroup paramActionListener, ActionListener arg6)
/*      */     {
/* 2802 */       super();
/* 2803 */       final URL localURL = ServiceDialog.getImageResource(paramBoolean);
/* 2804 */       Icon localIcon = (Icon)AccessController.doPrivileged(new PrivilegedAction()
/*      */       {
/*      */         public Object run() {
/* 2807 */           ImageIcon localImageIcon = new ImageIcon(localURL);
/* 2808 */           return localImageIcon;
/*      */         }
/*      */       });
/* 2811 */       this.lbl = new JLabel(localIcon);
/* 2812 */       add(this.lbl);
/*      */       ActionListener localActionListener;
/* 2814 */       this.rb = ServiceDialog.createRadioButton(paramString1, localActionListener);
/* 2815 */       this.rb.setSelected(paramButtonGroup);
/* 2816 */       ServiceDialog.addToBG(this.rb, this, paramActionListener);
/*      */     }
/*      */ 
/*      */     public void addActionListener(ActionListener paramActionListener) {
/* 2820 */       this.rb.addActionListener(paramActionListener);
/*      */     }
/*      */ 
/*      */     public boolean isSameAs(Object paramObject) {
/* 2824 */       return this.rb == paramObject;
/*      */     }
/*      */ 
/*      */     public void setEnabled(boolean paramBoolean) {
/* 2828 */       this.rb.setEnabled(paramBoolean);
/* 2829 */       this.lbl.setEnabled(paramBoolean);
/*      */     }
/*      */ 
/*      */     public boolean isSelected() {
/* 2833 */       return this.rb.isSelected();
/*      */     }
/*      */ 
/*      */     public void setSelected(boolean paramBoolean) {
/* 2837 */       this.rb.setSelected(paramBoolean);
/*      */     }
/*      */   }
/*      */ 
/*      */   private class JobAttributesPanel extends JPanel
/*      */     implements ActionListener, ChangeListener, FocusListener
/*      */   {
/* 2610 */     private final String strTitle = ServiceDialog.getMsg("border.jobattributes");
/*      */     private JLabel lblPriority;
/*      */     private JLabel lblJobName;
/*      */     private JLabel lblUserName;
/*      */     private JSpinner spinPriority;
/*      */     private SpinnerNumberModel snModel;
/*      */     private JCheckBox cbJobSheets;
/*      */     private JTextField tfJobName;
/*      */     private JTextField tfUserName;
/*      */ 
/*      */     public JobAttributesPanel()
/*      */     {
/* 2620 */       GridBagLayout localGridBagLayout = new GridBagLayout();
/* 2621 */       GridBagConstraints localGridBagConstraints = new GridBagConstraints();
/*      */ 
/* 2623 */       setLayout(localGridBagLayout);
/* 2624 */       setBorder(BorderFactory.createTitledBorder(this.strTitle));
/*      */ 
/* 2626 */       localGridBagConstraints.fill = 0;
/* 2627 */       localGridBagConstraints.insets = ServiceDialog.compInsets;
/* 2628 */       localGridBagConstraints.weighty = 1.0D;
/*      */ 
/* 2630 */       this.cbJobSheets = ServiceDialog.createCheckBox("checkbox.jobsheets", this);
/* 2631 */       localGridBagConstraints.anchor = 21;
/* 2632 */       ServiceDialog.addToGB(this.cbJobSheets, this, localGridBagLayout, localGridBagConstraints);
/*      */ 
/* 2634 */       JPanel localJPanel = new JPanel();
/* 2635 */       this.lblPriority = new JLabel(ServiceDialog.getMsg("label.priority"), 11);
/* 2636 */       this.lblPriority.setDisplayedMnemonic(ServiceDialog.getMnemonic("label.priority"));
/*      */ 
/* 2638 */       localJPanel.add(this.lblPriority);
/* 2639 */       this.snModel = new SpinnerNumberModel(1, 1, 100, 1);
/* 2640 */       this.spinPriority = new JSpinner(this.snModel);
/* 2641 */       this.lblPriority.setLabelFor(this.spinPriority);
/*      */ 
/* 2643 */       ((JSpinner.NumberEditor)this.spinPriority.getEditor()).getTextField().setColumns(3);
/* 2644 */       this.spinPriority.addChangeListener(this);
/* 2645 */       localJPanel.add(this.spinPriority);
/* 2646 */       localGridBagConstraints.anchor = 22;
/* 2647 */       localGridBagConstraints.gridwidth = 0;
/* 2648 */       localJPanel.getAccessibleContext().setAccessibleName(ServiceDialog.getMsg("label.priority"));
/*      */ 
/* 2650 */       ServiceDialog.addToGB(localJPanel, this, localGridBagLayout, localGridBagConstraints);
/*      */ 
/* 2652 */       localGridBagConstraints.fill = 2;
/* 2653 */       localGridBagConstraints.anchor = 10;
/* 2654 */       localGridBagConstraints.weightx = 0.0D;
/* 2655 */       localGridBagConstraints.gridwidth = 1;
/* 2656 */       char c1 = ServiceDialog.getMnemonic("label.jobname");
/* 2657 */       this.lblJobName = new JLabel(ServiceDialog.getMsg("label.jobname"), 11);
/* 2658 */       this.lblJobName.setDisplayedMnemonic(c1);
/* 2659 */       ServiceDialog.addToGB(this.lblJobName, this, localGridBagLayout, localGridBagConstraints);
/* 2660 */       localGridBagConstraints.weightx = 1.0D;
/* 2661 */       localGridBagConstraints.gridwidth = 0;
/* 2662 */       this.tfJobName = new JTextField();
/* 2663 */       this.lblJobName.setLabelFor(this.tfJobName);
/* 2664 */       this.tfJobName.addFocusListener(this);
/* 2665 */       this.tfJobName.setFocusAccelerator(c1);
/* 2666 */       this.tfJobName.getAccessibleContext().setAccessibleName(ServiceDialog.getMsg("label.jobname"));
/*      */ 
/* 2668 */       ServiceDialog.addToGB(this.tfJobName, this, localGridBagLayout, localGridBagConstraints);
/*      */ 
/* 2670 */       localGridBagConstraints.weightx = 0.0D;
/* 2671 */       localGridBagConstraints.gridwidth = 1;
/* 2672 */       char c2 = ServiceDialog.getMnemonic("label.username");
/* 2673 */       this.lblUserName = new JLabel(ServiceDialog.getMsg("label.username"), 11);
/* 2674 */       this.lblUserName.setDisplayedMnemonic(c2);
/* 2675 */       ServiceDialog.addToGB(this.lblUserName, this, localGridBagLayout, localGridBagConstraints);
/* 2676 */       localGridBagConstraints.gridwidth = 0;
/* 2677 */       this.tfUserName = new JTextField();
/* 2678 */       this.lblUserName.setLabelFor(this.tfUserName);
/* 2679 */       this.tfUserName.addFocusListener(this);
/* 2680 */       this.tfUserName.setFocusAccelerator(c2);
/* 2681 */       this.tfUserName.getAccessibleContext().setAccessibleName(ServiceDialog.getMsg("label.username"));
/*      */ 
/* 2683 */       ServiceDialog.addToGB(this.tfUserName, this, localGridBagLayout, localGridBagConstraints);
/*      */     }
/*      */ 
/*      */     public void actionPerformed(ActionEvent paramActionEvent) {
/* 2687 */       if (this.cbJobSheets.isSelected())
/* 2688 */         ServiceDialog.this.asCurrent.add(JobSheets.STANDARD);
/*      */       else
/* 2690 */         ServiceDialog.this.asCurrent.add(JobSheets.NONE);
/*      */     }
/*      */ 
/*      */     public void stateChanged(ChangeEvent paramChangeEvent)
/*      */     {
/* 2695 */       ServiceDialog.this.asCurrent.add(new JobPriority(this.snModel.getNumber().intValue()));
/*      */     }
/*      */ 
/*      */     public void focusLost(FocusEvent paramFocusEvent) {
/* 2699 */       Object localObject = paramFocusEvent.getSource();
/*      */ 
/* 2701 */       if (localObject == this.tfJobName) {
/* 2702 */         ServiceDialog.this.asCurrent.add(new JobName(this.tfJobName.getText(), Locale.getDefault()));
/*      */       }
/* 2704 */       else if (localObject == this.tfUserName)
/* 2705 */         ServiceDialog.this.asCurrent.add(new RequestingUserName(this.tfUserName.getText(), Locale.getDefault()));
/*      */     }
/*      */ 
/*      */     public void focusGained(FocusEvent paramFocusEvent)
/*      */     {
/*      */     }
/*      */ 
/*      */     public void updateInfo() {
/* 2713 */       JobSheets localJobSheets1 = JobSheets.class;
/* 2714 */       JobPriority localJobPriority1 = JobPriority.class;
/* 2715 */       JobName localJobName1 = JobName.class;
/* 2716 */       RequestingUserName localRequestingUserName1 = RequestingUserName.class;
/* 2717 */       boolean bool1 = false;
/* 2718 */       boolean bool2 = false;
/* 2719 */       boolean bool3 = false;
/* 2720 */       boolean bool4 = false;
/*      */ 
/* 2723 */       if (ServiceDialog.this.psCurrent.isAttributeCategorySupported(localJobSheets1)) {
/* 2724 */         bool1 = true;
/*      */       }
/* 2726 */       JobSheets localJobSheets2 = (JobSheets)ServiceDialog.this.asCurrent.get(localJobSheets1);
/* 2727 */       if (localJobSheets2 == null) {
/* 2728 */         localJobSheets2 = (JobSheets)ServiceDialog.this.psCurrent.getDefaultAttributeValue(localJobSheets1);
/* 2729 */         if (localJobSheets2 == null) {
/* 2730 */           localJobSheets2 = JobSheets.NONE;
/*      */         }
/*      */       }
/* 2733 */       this.cbJobSheets.setSelected(localJobSheets2 != JobSheets.NONE);
/* 2734 */       this.cbJobSheets.setEnabled(bool1);
/*      */ 
/* 2737 */       if ((!ServiceDialog.this.isAWT) && (ServiceDialog.this.psCurrent.isAttributeCategorySupported(localJobPriority1))) {
/* 2738 */         bool2 = true;
/*      */       }
/* 2740 */       JobPriority localJobPriority2 = (JobPriority)ServiceDialog.this.asCurrent.get(localJobPriority1);
/* 2741 */       if (localJobPriority2 == null) {
/* 2742 */         localJobPriority2 = (JobPriority)ServiceDialog.this.psCurrent.getDefaultAttributeValue(localJobPriority1);
/* 2743 */         if (localJobPriority2 == null) {
/* 2744 */           localJobPriority2 = new JobPriority(1);
/*      */         }
/*      */       }
/* 2747 */       int i = localJobPriority2.getValue();
/* 2748 */       if ((i < 1) || (i > 100)) {
/* 2749 */         i = 1;
/*      */       }
/* 2751 */       this.snModel.setValue(new Integer(i));
/* 2752 */       this.lblPriority.setEnabled(bool2);
/* 2753 */       this.spinPriority.setEnabled(bool2);
/*      */ 
/* 2756 */       if (ServiceDialog.this.psCurrent.isAttributeCategorySupported(localJobName1)) {
/* 2757 */         bool3 = true;
/*      */       }
/* 2759 */       JobName localJobName2 = (JobName)ServiceDialog.this.asCurrent.get(localJobName1);
/* 2760 */       if (localJobName2 == null) {
/* 2761 */         localJobName2 = (JobName)ServiceDialog.this.psCurrent.getDefaultAttributeValue(localJobName1);
/* 2762 */         if (localJobName2 == null) {
/* 2763 */           localJobName2 = new JobName("", Locale.getDefault());
/*      */         }
/*      */       }
/* 2766 */       this.tfJobName.setText(localJobName2.getValue());
/* 2767 */       this.tfJobName.setEnabled(bool3);
/* 2768 */       this.lblJobName.setEnabled(bool3);
/*      */ 
/* 2771 */       if ((!ServiceDialog.this.isAWT) && (ServiceDialog.this.psCurrent.isAttributeCategorySupported(localRequestingUserName1))) {
/* 2772 */         bool4 = true;
/*      */       }
/* 2774 */       RequestingUserName localRequestingUserName2 = (RequestingUserName)ServiceDialog.this.asCurrent.get(localRequestingUserName1);
/* 2775 */       if (localRequestingUserName2 == null) {
/* 2776 */         localRequestingUserName2 = (RequestingUserName)ServiceDialog.this.psCurrent.getDefaultAttributeValue(localRequestingUserName1);
/* 2777 */         if (localRequestingUserName2 == null) {
/* 2778 */           localRequestingUserName2 = new RequestingUserName("", Locale.getDefault());
/*      */         }
/*      */       }
/* 2781 */       this.tfUserName.setText(localRequestingUserName2.getValue());
/* 2782 */       this.tfUserName.setEnabled(bool4);
/* 2783 */       this.lblUserName.setEnabled(bool4);
/*      */     }
/*      */   }
/*      */ 
/*      */   private class MarginsPanel extends JPanel
/*      */     implements ActionListener, FocusListener
/*      */   {
/* 1348 */     private final String strTitle = ServiceDialog.getMsg("border.margins");
/*      */     private JFormattedTextField leftMargin;
/*      */     private JFormattedTextField rightMargin;
/*      */     private JFormattedTextField topMargin;
/*      */     private JFormattedTextField bottomMargin;
/*      */     private JLabel lblLeft;
/*      */     private JLabel lblRight;
/*      */     private JLabel lblTop;
/*      */     private JLabel lblBottom;
/* 1352 */     private int units = 1000;
/*      */ 
/* 1354 */     private float lmVal = -1.0F; private float rmVal = -1.0F; private float tmVal = -1.0F; private float bmVal = -1.0F;
/*      */     private Float lmObj;
/*      */     private Float rmObj;
/*      */     private Float tmObj;
/*      */     private Float bmObj;
/*      */ 
/*      */     public MarginsPanel()
/*      */     {
/* 1361 */       GridBagLayout localGridBagLayout = new GridBagLayout();
/* 1362 */       GridBagConstraints localGridBagConstraints = new GridBagConstraints();
/* 1363 */       localGridBagConstraints.fill = 2;
/* 1364 */       localGridBagConstraints.weightx = 1.0D;
/* 1365 */       localGridBagConstraints.weighty = 0.0D;
/* 1366 */       localGridBagConstraints.insets = ServiceDialog.compInsets;
/*      */ 
/* 1368 */       setLayout(localGridBagLayout);
/* 1369 */       setBorder(BorderFactory.createTitledBorder(this.strTitle));
/*      */ 
/* 1371 */       String str1 = "label.millimetres";
/* 1372 */       String str2 = Locale.getDefault().getCountry();
/* 1373 */       if ((str2 != null) && ((str2.equals("")) || (str2.equals(Locale.US.getCountry())) || (str2.equals(Locale.CANADA.getCountry()))))
/*      */       {
/* 1377 */         str1 = "label.inches";
/* 1378 */         this.units = 25400;
/*      */       }
/* 1380 */       String str3 = ServiceDialog.getMsg(str1);
/*      */       DecimalFormat localDecimalFormat;
/* 1383 */       if (this.units == 1000) {
/* 1384 */         localDecimalFormat = new DecimalFormat("###.##");
/* 1385 */         localDecimalFormat.setMaximumIntegerDigits(3);
/*      */       } else {
/* 1387 */         localDecimalFormat = new DecimalFormat("##.##");
/* 1388 */         localDecimalFormat.setMaximumIntegerDigits(2);
/*      */       }
/*      */ 
/* 1391 */       localDecimalFormat.setMinimumFractionDigits(1);
/* 1392 */       localDecimalFormat.setMaximumFractionDigits(2);
/* 1393 */       localDecimalFormat.setMinimumIntegerDigits(1);
/* 1394 */       localDecimalFormat.setParseIntegerOnly(false);
/* 1395 */       localDecimalFormat.setDecimalSeparatorAlwaysShown(true);
/* 1396 */       NumberFormatter localNumberFormatter = new NumberFormatter(localDecimalFormat);
/* 1397 */       localNumberFormatter.setMinimum(new Float(0.0F));
/* 1398 */       localNumberFormatter.setMaximum(new Float(999.0F));
/* 1399 */       localNumberFormatter.setAllowsInvalid(true);
/* 1400 */       localNumberFormatter.setCommitsOnValidEdit(true);
/*      */ 
/* 1402 */       this.leftMargin = new JFormattedTextField(localNumberFormatter);
/* 1403 */       this.leftMargin.addFocusListener(this);
/* 1404 */       this.leftMargin.addActionListener(this);
/* 1405 */       this.leftMargin.getAccessibleContext().setAccessibleName(ServiceDialog.getMsg("label.leftmargin"));
/*      */ 
/* 1407 */       this.rightMargin = new JFormattedTextField(localNumberFormatter);
/* 1408 */       this.rightMargin.addFocusListener(this);
/* 1409 */       this.rightMargin.addActionListener(this);
/* 1410 */       this.rightMargin.getAccessibleContext().setAccessibleName(ServiceDialog.getMsg("label.rightmargin"));
/*      */ 
/* 1412 */       this.topMargin = new JFormattedTextField(localNumberFormatter);
/* 1413 */       this.topMargin.addFocusListener(this);
/* 1414 */       this.topMargin.addActionListener(this);
/* 1415 */       this.topMargin.getAccessibleContext().setAccessibleName(ServiceDialog.getMsg("label.topmargin"));
/*      */ 
/* 1417 */       this.topMargin = new JFormattedTextField(localNumberFormatter);
/* 1418 */       this.bottomMargin = new JFormattedTextField(localNumberFormatter);
/* 1419 */       this.bottomMargin.addFocusListener(this);
/* 1420 */       this.bottomMargin.addActionListener(this);
/* 1421 */       this.bottomMargin.getAccessibleContext().setAccessibleName(ServiceDialog.getMsg("label.bottommargin"));
/*      */ 
/* 1423 */       this.topMargin = new JFormattedTextField(localNumberFormatter);
/* 1424 */       localGridBagConstraints.gridwidth = -1;
/* 1425 */       this.lblLeft = new JLabel(ServiceDialog.getMsg("label.leftmargin") + " " + str3, 10);
/*      */ 
/* 1427 */       this.lblLeft.setDisplayedMnemonic(ServiceDialog.getMnemonic("label.leftmargin"));
/* 1428 */       this.lblLeft.setLabelFor(this.leftMargin);
/* 1429 */       ServiceDialog.addToGB(this.lblLeft, this, localGridBagLayout, localGridBagConstraints);
/*      */ 
/* 1431 */       localGridBagConstraints.gridwidth = 0;
/* 1432 */       this.lblRight = new JLabel(ServiceDialog.getMsg("label.rightmargin") + " " + str3, 10);
/*      */ 
/* 1434 */       this.lblRight.setDisplayedMnemonic(ServiceDialog.getMnemonic("label.rightmargin"));
/* 1435 */       this.lblRight.setLabelFor(this.rightMargin);
/* 1436 */       ServiceDialog.addToGB(this.lblRight, this, localGridBagLayout, localGridBagConstraints);
/*      */ 
/* 1438 */       localGridBagConstraints.gridwidth = -1;
/* 1439 */       ServiceDialog.addToGB(this.leftMargin, this, localGridBagLayout, localGridBagConstraints);
/*      */ 
/* 1441 */       localGridBagConstraints.gridwidth = 0;
/* 1442 */       ServiceDialog.addToGB(this.rightMargin, this, localGridBagLayout, localGridBagConstraints);
/*      */ 
/* 1445 */       ServiceDialog.addToGB(new JPanel(), this, localGridBagLayout, localGridBagConstraints);
/*      */ 
/* 1447 */       localGridBagConstraints.gridwidth = -1;
/* 1448 */       this.lblTop = new JLabel(ServiceDialog.getMsg("label.topmargin") + " " + str3, 10);
/*      */ 
/* 1450 */       this.lblTop.setDisplayedMnemonic(ServiceDialog.getMnemonic("label.topmargin"));
/* 1451 */       this.lblTop.setLabelFor(this.topMargin);
/* 1452 */       ServiceDialog.addToGB(this.lblTop, this, localGridBagLayout, localGridBagConstraints);
/*      */ 
/* 1454 */       localGridBagConstraints.gridwidth = 0;
/* 1455 */       this.lblBottom = new JLabel(ServiceDialog.getMsg("label.bottommargin") + " " + str3, 10);
/*      */ 
/* 1457 */       this.lblBottom.setDisplayedMnemonic(ServiceDialog.getMnemonic("label.bottommargin"));
/* 1458 */       this.lblBottom.setLabelFor(this.bottomMargin);
/* 1459 */       ServiceDialog.addToGB(this.lblBottom, this, localGridBagLayout, localGridBagConstraints);
/*      */ 
/* 1461 */       localGridBagConstraints.gridwidth = -1;
/* 1462 */       ServiceDialog.addToGB(this.topMargin, this, localGridBagLayout, localGridBagConstraints);
/*      */ 
/* 1464 */       localGridBagConstraints.gridwidth = 0;
/* 1465 */       ServiceDialog.addToGB(this.bottomMargin, this, localGridBagLayout, localGridBagConstraints);
/*      */     }
/*      */ 
/*      */     public void actionPerformed(ActionEvent paramActionEvent)
/*      */     {
/* 1470 */       Object localObject = paramActionEvent.getSource();
/* 1471 */       updateMargins(localObject);
/*      */     }
/*      */ 
/*      */     public void focusLost(FocusEvent paramFocusEvent) {
/* 1475 */       Object localObject = paramFocusEvent.getSource();
/* 1476 */       updateMargins(localObject);
/*      */     }
/*      */ 
/*      */     public void focusGained(FocusEvent paramFocusEvent)
/*      */     {
/*      */     }
/*      */ 
/*      */     public void updateMargins(Object paramObject)
/*      */     {
/* 1487 */       if (!(paramObject instanceof JFormattedTextField)) {
/* 1488 */         return;
/*      */       }
/* 1490 */       Object localObject = (JFormattedTextField)paramObject;
/* 1491 */       Float localFloat1 = (Float)((JFormattedTextField)localObject).getValue();
/* 1492 */       if (localFloat1 == null) {
/* 1493 */         return;
/*      */       }
/* 1495 */       if ((localObject == this.leftMargin) && (localFloat1.equals(this.lmObj))) {
/* 1496 */         return;
/*      */       }
/* 1498 */       if ((localObject == this.rightMargin) && (localFloat1.equals(this.rmObj))) {
/* 1499 */         return;
/*      */       }
/* 1501 */       if ((localObject == this.topMargin) && (localFloat1.equals(this.tmObj))) {
/* 1502 */         return;
/*      */       }
/* 1504 */       if ((localObject == this.bottomMargin) && (localFloat1.equals(this.bmObj))) {
/* 1505 */         return;
/*      */       }
/*      */ 
/* 1509 */       localObject = (Float)this.leftMargin.getValue();
/* 1510 */       localFloat1 = (Float)this.rightMargin.getValue();
/* 1511 */       Float localFloat2 = (Float)this.topMargin.getValue();
/* 1512 */       Float localFloat3 = (Float)this.bottomMargin.getValue();
/*      */ 
/* 1514 */       float f1 = ((Float)localObject).floatValue();
/* 1515 */       float f2 = localFloat1.floatValue();
/* 1516 */       float f3 = localFloat2.floatValue();
/* 1517 */       float f4 = localFloat3.floatValue();
/*      */ 
/* 1520 */       OrientationRequested localOrientationRequested1 = OrientationRequested.class;
/* 1521 */       OrientationRequested localOrientationRequested2 = (OrientationRequested)ServiceDialog.this.asCurrent.get(localOrientationRequested1);
/*      */ 
/* 1524 */       if (localOrientationRequested2 == null)
/* 1525 */         localOrientationRequested2 = (OrientationRequested)ServiceDialog.this.psCurrent.getDefaultAttributeValue(localOrientationRequested1);
/*      */       float f5;
/* 1530 */       if (localOrientationRequested2 == OrientationRequested.REVERSE_PORTRAIT) {
/* 1531 */         f5 = f1; f1 = f2; f2 = f5;
/* 1532 */         f5 = f3; f3 = f4; f4 = f5;
/* 1533 */       } else if (localOrientationRequested2 == OrientationRequested.LANDSCAPE) {
/* 1534 */         f5 = f1;
/* 1535 */         f1 = f3;
/* 1536 */         f3 = f2;
/* 1537 */         f2 = f4;
/* 1538 */         f4 = f5;
/* 1539 */       } else if (localOrientationRequested2 == OrientationRequested.REVERSE_LANDSCAPE) {
/* 1540 */         f5 = f1;
/* 1541 */         f1 = f4;
/* 1542 */         f4 = f2;
/* 1543 */         f2 = f3;
/* 1544 */         f3 = f5;
/*      */       }
/*      */       MediaPrintableArea localMediaPrintableArea;
/* 1547 */       if ((localMediaPrintableArea = validateMargins(f1, f2, f3, f4)) != null) {
/* 1548 */         ServiceDialog.this.asCurrent.add(localMediaPrintableArea);
/* 1549 */         this.lmVal = f1;
/* 1550 */         this.rmVal = f2;
/* 1551 */         this.tmVal = f3;
/* 1552 */         this.bmVal = f4;
/* 1553 */         this.lmObj = ((Float)localObject);
/* 1554 */         this.rmObj = localFloat1;
/* 1555 */         this.tmObj = localFloat2;
/* 1556 */         this.bmObj = localFloat3;
/*      */       } else {
/* 1558 */         if ((this.lmObj == null) || (this.rmObj == null) || (this.tmObj == null) || (this.rmObj == null))
/*      */         {
/* 1560 */           return;
/*      */         }
/* 1562 */         this.leftMargin.setValue(this.lmObj);
/* 1563 */         this.rightMargin.setValue(this.rmObj);
/* 1564 */         this.topMargin.setValue(this.tmObj);
/* 1565 */         this.bottomMargin.setValue(this.bmObj);
/*      */       }
/*      */     }
/*      */ 
/*      */     private MediaPrintableArea validateMargins(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
/*      */     {
/* 1584 */       MediaPrintableArea localMediaPrintableArea1 = MediaPrintableArea.class;
/*      */ 
/* 1586 */       MediaPrintableArea localMediaPrintableArea2 = null;
/* 1587 */       MediaSize localMediaSize = null;
/*      */ 
/* 1589 */       Media localMedia = (Media)ServiceDialog.this.asCurrent.get(Media.class);
/* 1590 */       if ((localMedia == null) || (!(localMedia instanceof MediaSizeName)))
/* 1591 */         localMedia = (Media)ServiceDialog.this.psCurrent.getDefaultAttributeValue(Media.class);
/*      */       Object localObject1;
/* 1593 */       if ((localMedia != null) && ((localMedia instanceof MediaSizeName))) {
/* 1594 */         localObject1 = (MediaSizeName)localMedia;
/* 1595 */         localMediaSize = MediaSize.getMediaSizeForName((MediaSizeName)localObject1);
/*      */       }
/* 1597 */       if (localMediaSize == null) {
/* 1598 */         localMediaSize = new MediaSize(8.5F, 11.0F, 25400);
/*      */       }
/*      */ 
/* 1601 */       if (localMedia != null) {
/* 1602 */         localObject1 = new HashPrintRequestAttributeSet(ServiceDialog.this.asCurrent);
/*      */ 
/* 1604 */         ((PrintRequestAttributeSet)localObject1).add(localMedia);
/*      */ 
/* 1606 */         Object localObject2 = ServiceDialog.this.psCurrent.getSupportedAttributeValues(localMediaPrintableArea1, ServiceDialog.this.docFlavor, (AttributeSet)localObject1);
/*      */ 
/* 1610 */         if (((localObject2 instanceof MediaPrintableArea[])) && (((MediaPrintableArea[])localObject2).length > 0))
/*      */         {
/* 1612 */           localMediaPrintableArea2 = ((MediaPrintableArea[])(MediaPrintableArea[])localObject2)[0];
/*      */         }
/*      */       }
/*      */ 
/* 1616 */       if (localMediaPrintableArea2 == null) {
/* 1617 */         localMediaPrintableArea2 = new MediaPrintableArea(0.0F, 0.0F, localMediaSize.getX(this.units), localMediaSize.getY(this.units), this.units);
/*      */       }
/*      */ 
/* 1623 */       float f1 = localMediaSize.getX(this.units);
/* 1624 */       float f2 = localMediaSize.getY(this.units);
/* 1625 */       float f3 = paramFloat1;
/* 1626 */       float f4 = paramFloat3;
/* 1627 */       float f5 = f1 - paramFloat1 - paramFloat2;
/* 1628 */       float f6 = f2 - paramFloat3 - paramFloat4;
/*      */ 
/* 1630 */       if ((f5 <= 0.0F) || (f6 <= 0.0F) || (f3 < 0.0F) || (f4 < 0.0F) || (f3 < localMediaPrintableArea2.getX(this.units)) || (f5 > localMediaPrintableArea2.getWidth(this.units)) || (f4 < localMediaPrintableArea2.getY(this.units)) || (f6 > localMediaPrintableArea2.getHeight(this.units)))
/*      */       {
/* 1633 */         return null;
/*      */       }
/* 1635 */       return new MediaPrintableArea(paramFloat1, paramFloat3, f5, f6, this.units);
/*      */     }
/*      */ 
/*      */     public void updateInfo()
/*      */     {
/* 1654 */       if (ServiceDialog.this.isAWT) {
/* 1655 */         this.leftMargin.setEnabled(false);
/* 1656 */         this.rightMargin.setEnabled(false);
/* 1657 */         this.topMargin.setEnabled(false);
/* 1658 */         this.bottomMargin.setEnabled(false);
/* 1659 */         this.lblLeft.setEnabled(false);
/* 1660 */         this.lblRight.setEnabled(false);
/* 1661 */         this.lblTop.setEnabled(false);
/* 1662 */         this.lblBottom.setEnabled(false);
/* 1663 */         return;
/*      */       }
/*      */ 
/* 1666 */       MediaPrintableArea localMediaPrintableArea1 = MediaPrintableArea.class;
/* 1667 */       MediaPrintableArea localMediaPrintableArea2 = (MediaPrintableArea)ServiceDialog.this.asCurrent.get(localMediaPrintableArea1);
/*      */ 
/* 1669 */       MediaPrintableArea localMediaPrintableArea3 = null;
/* 1670 */       MediaSize localMediaSize = null;
/*      */ 
/* 1672 */       Media localMedia = (Media)ServiceDialog.this.asCurrent.get(Media.class);
/* 1673 */       if ((localMedia == null) || (!(localMedia instanceof MediaSizeName)))
/* 1674 */         localMedia = (Media)ServiceDialog.this.psCurrent.getDefaultAttributeValue(Media.class);
/*      */       Object localObject1;
/* 1676 */       if ((localMedia != null) && ((localMedia instanceof MediaSizeName))) {
/* 1677 */         localObject1 = (MediaSizeName)localMedia;
/* 1678 */         localMediaSize = MediaSize.getMediaSizeForName((MediaSizeName)localObject1);
/*      */       }
/* 1680 */       if (localMediaSize == null) {
/* 1681 */         localMediaSize = new MediaSize(8.5F, 11.0F, 25400);
/*      */       }
/*      */ 
/* 1684 */       if (localMedia != null) {
/* 1685 */         localObject1 = new HashPrintRequestAttributeSet(ServiceDialog.this.asCurrent);
/*      */ 
/* 1687 */         ((PrintRequestAttributeSet)localObject1).add(localMedia);
/*      */ 
/* 1689 */         Object localObject2 = ServiceDialog.this.psCurrent.getSupportedAttributeValues(localMediaPrintableArea1, ServiceDialog.this.docFlavor, (AttributeSet)localObject1);
/*      */ 
/* 1693 */         if (((localObject2 instanceof MediaPrintableArea[])) && (((MediaPrintableArea[])localObject2).length > 0))
/*      */         {
/* 1695 */           localMediaPrintableArea3 = ((MediaPrintableArea[])(MediaPrintableArea[])localObject2)[0];
/*      */         }
/* 1697 */         else if ((localObject2 instanceof MediaPrintableArea)) {
/* 1698 */           localMediaPrintableArea3 = (MediaPrintableArea)localObject2;
/*      */         }
/*      */       }
/* 1701 */       if (localMediaPrintableArea3 == null) {
/* 1702 */         localMediaPrintableArea3 = new MediaPrintableArea(0.0F, 0.0F, localMediaSize.getX(this.units), localMediaSize.getY(this.units), this.units);
/*      */       }
/*      */ 
/* 1718 */       float f1 = localMediaSize.getX(25400);
/* 1719 */       float f2 = localMediaSize.getY(25400);
/* 1720 */       float f3 = 5.0F;
/*      */       float f4;
/* 1722 */       if (f1 > f3)
/* 1723 */         f4 = 1.0F;
/*      */       else
/* 1725 */         f4 = f1 / f3;
/*      */       float f5;
/* 1727 */       if (f2 > f3)
/* 1728 */         f5 = 1.0F;
/*      */       else {
/* 1730 */         f5 = f2 / f3;
/*      */       }
/*      */ 
/* 1733 */       if (localMediaPrintableArea2 == null) {
/* 1734 */         localMediaPrintableArea2 = new MediaPrintableArea(f4, f5, f1 - 2.0F * f4, f2 - 2.0F * f5, 25400);
/*      */ 
/* 1737 */         ServiceDialog.this.asCurrent.add(localMediaPrintableArea2);
/*      */       }
/* 1739 */       float f6 = localMediaPrintableArea2.getX(this.units);
/* 1740 */       float f7 = localMediaPrintableArea2.getY(this.units);
/* 1741 */       float f8 = localMediaPrintableArea2.getWidth(this.units);
/* 1742 */       float f9 = localMediaPrintableArea2.getHeight(this.units);
/* 1743 */       float f10 = localMediaPrintableArea3.getX(this.units);
/* 1744 */       float f11 = localMediaPrintableArea3.getY(this.units);
/* 1745 */       float f12 = localMediaPrintableArea3.getWidth(this.units);
/* 1746 */       float f13 = localMediaPrintableArea3.getHeight(this.units);
/*      */ 
/* 1749 */       int i = 0;
/*      */ 
/* 1764 */       f1 = localMediaSize.getX(this.units);
/* 1765 */       f2 = localMediaSize.getY(this.units);
/* 1766 */       if (this.lmVal >= 0.0F) {
/* 1767 */         i = 1;
/*      */ 
/* 1769 */         if (this.lmVal + this.rmVal > f1)
/*      */         {
/* 1771 */           if (f8 > f12) {
/* 1772 */             f8 = f12;
/*      */           }
/*      */ 
/* 1775 */           f6 = (f1 - f8) / 2.0F;
/*      */         } else {
/* 1777 */           f6 = this.lmVal >= f10 ? this.lmVal : f10;
/* 1778 */           f8 = f1 - f6 - this.rmVal;
/*      */         }
/* 1780 */         if (this.tmVal + this.bmVal > f2) {
/* 1781 */           if (f9 > f13) {
/* 1782 */             f9 = f13;
/*      */           }
/* 1784 */           f7 = (f2 - f9) / 2.0F;
/*      */         } else {
/* 1786 */           f7 = this.tmVal >= f11 ? this.tmVal : f11;
/* 1787 */           f9 = f2 - f7 - this.bmVal;
/*      */         }
/*      */       }
/* 1790 */       if (f6 < f10) {
/* 1791 */         i = 1;
/* 1792 */         f6 = f10;
/*      */       }
/* 1794 */       if (f7 < f11) {
/* 1795 */         i = 1;
/* 1796 */         f7 = f11;
/*      */       }
/* 1798 */       if (f8 > f12) {
/* 1799 */         i = 1;
/* 1800 */         f8 = f12;
/*      */       }
/* 1802 */       if (f9 > f13) {
/* 1803 */         i = 1;
/* 1804 */         f9 = f13;
/*      */       }
/*      */ 
/* 1807 */       if ((f6 + f8 > f10 + f12) || (f8 <= 0.0F)) {
/* 1808 */         i = 1;
/* 1809 */         f6 = f10;
/* 1810 */         f8 = f12;
/*      */       }
/* 1812 */       if ((f7 + f9 > f11 + f13) || (f9 <= 0.0F)) {
/* 1813 */         i = 1;
/* 1814 */         f7 = f11;
/* 1815 */         f9 = f13;
/*      */       }
/*      */ 
/* 1818 */       if (i != 0) {
/* 1819 */         localMediaPrintableArea2 = new MediaPrintableArea(f6, f7, f8, f9, this.units);
/* 1820 */         ServiceDialog.this.asCurrent.add(localMediaPrintableArea2);
/*      */       }
/*      */ 
/* 1826 */       this.lmVal = f6;
/* 1827 */       this.tmVal = f7;
/* 1828 */       this.rmVal = (localMediaSize.getX(this.units) - f6 - f8);
/* 1829 */       this.bmVal = (localMediaSize.getY(this.units) - f7 - f9);
/*      */ 
/* 1831 */       this.lmObj = new Float(this.lmVal);
/* 1832 */       this.rmObj = new Float(this.rmVal);
/* 1833 */       this.tmObj = new Float(this.tmVal);
/* 1834 */       this.bmObj = new Float(this.bmVal);
/*      */ 
/* 1840 */       OrientationRequested localOrientationRequested1 = OrientationRequested.class;
/* 1841 */       OrientationRequested localOrientationRequested2 = (OrientationRequested)ServiceDialog.this.asCurrent.get(localOrientationRequested1);
/*      */ 
/* 1844 */       if (localOrientationRequested2 == null)
/* 1845 */         localOrientationRequested2 = (OrientationRequested)ServiceDialog.this.psCurrent.getDefaultAttributeValue(localOrientationRequested1);
/*      */       Float localFloat;
/* 1851 */       if (localOrientationRequested2 == OrientationRequested.REVERSE_PORTRAIT) {
/* 1852 */         localFloat = this.lmObj; this.lmObj = this.rmObj; this.rmObj = localFloat;
/* 1853 */         localFloat = this.tmObj; this.tmObj = this.bmObj; this.bmObj = localFloat;
/* 1854 */       } else if (localOrientationRequested2 == OrientationRequested.LANDSCAPE) {
/* 1855 */         localFloat = this.lmObj;
/* 1856 */         this.lmObj = this.bmObj;
/* 1857 */         this.bmObj = this.rmObj;
/* 1858 */         this.rmObj = this.tmObj;
/* 1859 */         this.tmObj = localFloat;
/* 1860 */       } else if (localOrientationRequested2 == OrientationRequested.REVERSE_LANDSCAPE) {
/* 1861 */         localFloat = this.lmObj;
/* 1862 */         this.lmObj = this.tmObj;
/* 1863 */         this.tmObj = this.rmObj;
/* 1864 */         this.rmObj = this.bmObj;
/* 1865 */         this.bmObj = localFloat;
/*      */       }
/*      */ 
/* 1868 */       this.leftMargin.setValue(this.lmObj);
/* 1869 */       this.rightMargin.setValue(this.rmObj);
/* 1870 */       this.topMargin.setValue(this.tmObj);
/* 1871 */       this.bottomMargin.setValue(this.bmObj);
/*      */     }
/*      */   }
/*      */ 
/*      */   private class MediaPanel extends JPanel implements ItemListener
/*      */   {
/* 1877 */     private final String strTitle = ServiceDialog.getMsg("border.media");
/*      */     private JLabel lblSize;
/*      */     private JLabel lblSource;
/*      */     private JComboBox cbSize;
/*      */     private JComboBox cbSource;
/* 1880 */     private Vector sizes = new Vector();
/* 1881 */     private Vector sources = new Vector();
/* 1882 */     private ServiceDialog.MarginsPanel pnlMargins = null;
/*      */ 
/*      */     public MediaPanel()
/*      */     {
/* 1887 */       GridBagLayout localGridBagLayout = new GridBagLayout();
/* 1888 */       GridBagConstraints localGridBagConstraints = new GridBagConstraints();
/*      */ 
/* 1890 */       setLayout(localGridBagLayout);
/* 1891 */       setBorder(BorderFactory.createTitledBorder(this.strTitle));
/*      */ 
/* 1893 */       this.cbSize = new JComboBox();
/* 1894 */       this.cbSource = new JComboBox();
/*      */ 
/* 1896 */       localGridBagConstraints.fill = 1;
/* 1897 */       localGridBagConstraints.insets = ServiceDialog.compInsets;
/* 1898 */       localGridBagConstraints.weighty = 1.0D;
/*      */ 
/* 1900 */       localGridBagConstraints.weightx = 0.0D;
/* 1901 */       this.lblSize = new JLabel(ServiceDialog.getMsg("label.size"), 11);
/* 1902 */       this.lblSize.setDisplayedMnemonic(ServiceDialog.getMnemonic("label.size"));
/* 1903 */       this.lblSize.setLabelFor(this.cbSize);
/* 1904 */       ServiceDialog.addToGB(this.lblSize, this, localGridBagLayout, localGridBagConstraints);
/* 1905 */       localGridBagConstraints.weightx = 1.0D;
/* 1906 */       localGridBagConstraints.gridwidth = 0;
/* 1907 */       ServiceDialog.addToGB(this.cbSize, this, localGridBagLayout, localGridBagConstraints);
/*      */ 
/* 1909 */       localGridBagConstraints.weightx = 0.0D;
/* 1910 */       localGridBagConstraints.gridwidth = 1;
/* 1911 */       this.lblSource = new JLabel(ServiceDialog.getMsg("label.source"), 11);
/* 1912 */       this.lblSource.setDisplayedMnemonic(ServiceDialog.getMnemonic("label.source"));
/* 1913 */       this.lblSource.setLabelFor(this.cbSource);
/* 1914 */       ServiceDialog.addToGB(this.lblSource, this, localGridBagLayout, localGridBagConstraints);
/* 1915 */       localGridBagConstraints.gridwidth = 0;
/* 1916 */       ServiceDialog.addToGB(this.cbSource, this, localGridBagLayout, localGridBagConstraints);
/*      */     }
/*      */ 
/*      */     private String getMediaName(String paramString)
/*      */     {
/*      */       try
/*      */       {
/* 1923 */         String str = paramString.replace(' ', '-');
/* 1924 */         str = str.replace('#', 'n');
/*      */ 
/* 1926 */         return ServiceDialog.messageRB.getString(str); } catch (MissingResourceException localMissingResourceException) {
/*      */       }
/* 1928 */       return paramString;
/*      */     }
/*      */ 
/*      */     public void itemStateChanged(ItemEvent paramItemEvent)
/*      */     {
/* 1933 */       Object localObject1 = paramItemEvent.getSource();
/*      */ 
/* 1935 */       if (paramItemEvent.getStateChange() == 1)
/*      */       {
/*      */         int i;
/*      */         Object localObject2;
/* 1936 */         if (localObject1 == this.cbSize) {
/* 1937 */           i = this.cbSize.getSelectedIndex();
/*      */ 
/* 1939 */           if ((i >= 0) && (i < this.sizes.size())) {
/* 1940 */             if ((this.cbSource.getItemCount() > 1) && (this.cbSource.getSelectedIndex() >= 1))
/*      */             {
/* 1943 */               int j = this.cbSource.getSelectedIndex() - 1;
/* 1944 */               localObject2 = (MediaTray)this.sources.get(j);
/* 1945 */               ServiceDialog.this.asCurrent.add(new SunAlternateMedia((Media)localObject2));
/*      */             }
/* 1947 */             ServiceDialog.this.asCurrent.add((MediaSizeName)this.sizes.get(i));
/*      */           }
/* 1949 */         } else if (localObject1 == this.cbSource) {
/* 1950 */           i = this.cbSource.getSelectedIndex();
/*      */ 
/* 1952 */           if ((i >= 1) && (i < this.sources.size() + 1)) {
/* 1953 */             ServiceDialog.this.asCurrent.remove(SunAlternateMedia.class);
/* 1954 */             MediaTray localMediaTray = (MediaTray)this.sources.get(i - 1);
/* 1955 */             localObject2 = (Media)ServiceDialog.this.asCurrent.get(Media.class);
/* 1956 */             if ((localObject2 == null) || ((localObject2 instanceof MediaTray))) {
/* 1957 */               ServiceDialog.this.asCurrent.add(localMediaTray);
/* 1958 */             } else if ((localObject2 instanceof MediaSizeName)) {
/* 1959 */               MediaSizeName localMediaSizeName = (MediaSizeName)localObject2;
/* 1960 */               Media localMedia = (Media)ServiceDialog.this.psCurrent.getDefaultAttributeValue(Media.class);
/* 1961 */               if (((localMedia instanceof MediaSizeName)) && (localMedia.equals(localMediaSizeName))) {
/* 1962 */                 ServiceDialog.this.asCurrent.add(localMediaTray);
/*      */               }
/*      */               else
/*      */               {
/* 1967 */                 ServiceDialog.this.asCurrent.add(new SunAlternateMedia(localMediaTray));
/*      */               }
/*      */             }
/* 1970 */           } else if (i == 0) {
/* 1971 */             ServiceDialog.this.asCurrent.remove(SunAlternateMedia.class);
/* 1972 */             if (this.cbSize.getItemCount() > 0) {
/* 1973 */               int k = this.cbSize.getSelectedIndex();
/* 1974 */               ServiceDialog.this.asCurrent.add((MediaSizeName)this.sizes.get(k));
/*      */             }
/*      */           }
/*      */         }
/*      */ 
/* 1979 */         if (this.pnlMargins != null)
/* 1980 */           this.pnlMargins.updateInfo();
/*      */       }
/*      */     }
/*      */ 
/*      */     public void addMediaListener(ServiceDialog.MarginsPanel paramMarginsPanel)
/*      */     {
/* 1988 */       this.pnlMargins = paramMarginsPanel;
/*      */     }
/*      */     public void updateInfo() {
/* 1991 */       Media localMedia1 = Media.class;
/* 1992 */       SunAlternateMedia localSunAlternateMedia = SunAlternateMedia.class;
/* 1993 */       boolean bool1 = false;
/*      */ 
/* 1995 */       this.cbSize.removeItemListener(this);
/* 1996 */       this.cbSize.removeAllItems();
/* 1997 */       this.cbSource.removeItemListener(this);
/* 1998 */       this.cbSource.removeAllItems();
/* 1999 */       this.cbSource.addItem(getMediaName("auto-select"));
/*      */ 
/* 2001 */       this.sizes.clear();
/* 2002 */       this.sources.clear();
/*      */       Object localObject2;
/*      */       Object localObject3;
/* 2004 */       if (ServiceDialog.this.psCurrent.isAttributeCategorySupported(localMedia1)) {
/* 2005 */         bool1 = true;
/*      */ 
/* 2007 */         Object localObject1 = ServiceDialog.this.psCurrent.getSupportedAttributeValues(localMedia1, ServiceDialog.this.docFlavor, ServiceDialog.this.asCurrent);
/*      */ 
/* 2012 */         if ((localObject1 instanceof Media[])) {
/* 2013 */           localObject2 = (Media[])localObject1;
/*      */ 
/* 2015 */           for (int i = 0; i < localObject2.length; i++) {
/* 2016 */             localObject3 = localObject2[i];
/*      */ 
/* 2018 */             if ((localObject3 instanceof MediaSizeName)) {
/* 2019 */               this.sizes.add(localObject3);
/* 2020 */               this.cbSize.addItem(getMediaName(((Media)localObject3).toString()));
/* 2021 */             } else if ((localObject3 instanceof MediaTray)) {
/* 2022 */               this.sources.add(localObject3);
/* 2023 */               this.cbSource.addItem(getMediaName(((Media)localObject3).toString()));
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */ 
/* 2029 */       boolean bool2 = (bool1) && (this.sizes.size() > 0);
/* 2030 */       this.lblSize.setEnabled(bool2);
/* 2031 */       this.cbSize.setEnabled(bool2);
/*      */ 
/* 2033 */       if (ServiceDialog.this.isAWT) {
/* 2034 */         this.cbSource.setEnabled(false);
/* 2035 */         this.lblSource.setEnabled(false);
/*      */       } else {
/* 2037 */         this.cbSource.setEnabled(bool1);
/*      */       }
/*      */ 
/* 2040 */       if (bool1)
/*      */       {
/* 2042 */         localObject2 = (Media)ServiceDialog.this.asCurrent.get(localMedia1);
/*      */ 
/* 2045 */         Media localMedia2 = (Media)ServiceDialog.this.psCurrent.getDefaultAttributeValue(localMedia1);
/* 2046 */         if ((localMedia2 instanceof MediaSizeName)) {
/* 2047 */           this.cbSize.setSelectedIndex(this.sizes.size() > 0 ? this.sizes.indexOf(localMedia2) : -1);
/*      */         }
/*      */ 
/* 2050 */         if ((localObject2 == null) || (!ServiceDialog.this.psCurrent.isAttributeValueSupported((Attribute)localObject2, ServiceDialog.this.docFlavor, ServiceDialog.this.asCurrent)))
/*      */         {
/* 2054 */           localObject2 = localMedia2;
/*      */ 
/* 2056 */           if ((localObject2 == null) && 
/* 2057 */             (this.sizes.size() > 0)) {
/* 2058 */             localObject2 = (Media)this.sizes.get(0);
/*      */           }
/*      */ 
/* 2061 */           if (localObject2 != null) {
/* 2062 */             ServiceDialog.this.asCurrent.add((Attribute)localObject2);
/*      */           }
/*      */         }
/* 2065 */         if (localObject2 != null) {
/* 2066 */           if ((localObject2 instanceof MediaSizeName)) {
/* 2067 */             localObject3 = (MediaSizeName)localObject2;
/* 2068 */             this.cbSize.setSelectedIndex(this.sizes.indexOf(localObject3));
/* 2069 */           } else if ((localObject2 instanceof MediaTray)) {
/* 2070 */             localObject3 = (MediaTray)localObject2;
/* 2071 */             this.cbSource.setSelectedIndex(this.sources.indexOf(localObject3) + 1);
/*      */           }
/*      */         } else {
/* 2074 */           this.cbSize.setSelectedIndex(this.sizes.size() > 0 ? 0 : -1);
/* 2075 */           this.cbSource.setSelectedIndex(0);
/*      */         }
/*      */ 
/* 2078 */         localObject3 = (SunAlternateMedia)ServiceDialog.this.asCurrent.get(localSunAlternateMedia);
/*      */         MediaTray localMediaTray;
/* 2079 */         if (localObject3 != null) {
/* 2080 */           Media localMedia3 = ((SunAlternateMedia)localObject3).getMedia();
/* 2081 */           if ((localMedia3 instanceof MediaTray)) {
/* 2082 */             localMediaTray = (MediaTray)localMedia3;
/* 2083 */             this.cbSource.setSelectedIndex(this.sources.indexOf(localMediaTray) + 1);
/*      */           }
/*      */         }
/*      */ 
/* 2087 */         int j = this.cbSize.getSelectedIndex();
/* 2088 */         if ((j >= 0) && (j < this.sizes.size())) {
/* 2089 */           ServiceDialog.this.asCurrent.add((MediaSizeName)this.sizes.get(j));
/*      */         }
/*      */ 
/* 2092 */         j = this.cbSource.getSelectedIndex();
/* 2093 */         if ((j >= 1) && (j < this.sources.size() + 1)) {
/* 2094 */           localMediaTray = (MediaTray)this.sources.get(j - 1);
/* 2095 */           if ((localObject2 instanceof MediaTray))
/* 2096 */             ServiceDialog.this.asCurrent.add(localMediaTray);
/*      */           else {
/* 2098 */             ServiceDialog.this.asCurrent.add(new SunAlternateMedia(localMediaTray));
/*      */           }
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 2104 */       this.cbSize.addItemListener(this);
/* 2105 */       this.cbSource.addItemListener(this);
/*      */     }
/*      */   }
/*      */ 
/*      */   private class OrientationPanel extends JPanel
/*      */     implements ActionListener
/*      */   {
/* 2112 */     private final String strTitle = ServiceDialog.getMsg("border.orientation");
/*      */     private ServiceDialog.IconRadioButton rbPortrait;
/*      */     private ServiceDialog.IconRadioButton rbLandscape;
/*      */     private ServiceDialog.IconRadioButton rbRevPortrait;
/*      */     private ServiceDialog.IconRadioButton rbRevLandscape;
/* 2115 */     private ServiceDialog.MarginsPanel pnlMargins = null;
/*      */ 
/*      */     public OrientationPanel()
/*      */     {
/* 2120 */       GridBagLayout localGridBagLayout = new GridBagLayout();
/* 2121 */       GridBagConstraints localGridBagConstraints = new GridBagConstraints();
/*      */ 
/* 2123 */       setLayout(localGridBagLayout);
/* 2124 */       setBorder(BorderFactory.createTitledBorder(this.strTitle));
/*      */ 
/* 2126 */       localGridBagConstraints.fill = 1;
/* 2127 */       localGridBagConstraints.insets = ServiceDialog.compInsets;
/* 2128 */       localGridBagConstraints.weighty = 1.0D;
/* 2129 */       localGridBagConstraints.gridwidth = 0;
/*      */ 
/* 2131 */       ButtonGroup localButtonGroup = new ButtonGroup();
/* 2132 */       this.rbPortrait = new ServiceDialog.IconRadioButton(ServiceDialog.this, "radiobutton.portrait", "orientPortrait.png", true, localButtonGroup, this);
/*      */ 
/* 2135 */       this.rbPortrait.addActionListener(this);
/* 2136 */       ServiceDialog.addToGB(this.rbPortrait, this, localGridBagLayout, localGridBagConstraints);
/* 2137 */       this.rbLandscape = new ServiceDialog.IconRadioButton(ServiceDialog.this, "radiobutton.landscape", "orientLandscape.png", false, localButtonGroup, this);
/*      */ 
/* 2140 */       this.rbLandscape.addActionListener(this);
/* 2141 */       ServiceDialog.addToGB(this.rbLandscape, this, localGridBagLayout, localGridBagConstraints);
/* 2142 */       this.rbRevPortrait = new ServiceDialog.IconRadioButton(ServiceDialog.this, "radiobutton.revportrait", "orientRevPortrait.png", false, localButtonGroup, this);
/*      */ 
/* 2145 */       this.rbRevPortrait.addActionListener(this);
/* 2146 */       ServiceDialog.addToGB(this.rbRevPortrait, this, localGridBagLayout, localGridBagConstraints);
/* 2147 */       this.rbRevLandscape = new ServiceDialog.IconRadioButton(ServiceDialog.this, "radiobutton.revlandscape", "orientRevLandscape.png", false, localButtonGroup, this);
/*      */ 
/* 2150 */       this.rbRevLandscape.addActionListener(this);
/* 2151 */       ServiceDialog.addToGB(this.rbRevLandscape, this, localGridBagLayout, localGridBagConstraints);
/*      */     }
/*      */ 
/*      */     public void actionPerformed(ActionEvent paramActionEvent) {
/* 2155 */       Object localObject = paramActionEvent.getSource();
/*      */ 
/* 2157 */       if (this.rbPortrait.isSameAs(localObject))
/* 2158 */         ServiceDialog.this.asCurrent.add(OrientationRequested.PORTRAIT);
/* 2159 */       else if (this.rbLandscape.isSameAs(localObject))
/* 2160 */         ServiceDialog.this.asCurrent.add(OrientationRequested.LANDSCAPE);
/* 2161 */       else if (this.rbRevPortrait.isSameAs(localObject))
/* 2162 */         ServiceDialog.this.asCurrent.add(OrientationRequested.REVERSE_PORTRAIT);
/* 2163 */       else if (this.rbRevLandscape.isSameAs(localObject)) {
/* 2164 */         ServiceDialog.this.asCurrent.add(OrientationRequested.REVERSE_LANDSCAPE);
/*      */       }
/*      */ 
/* 2167 */       if (this.pnlMargins != null)
/* 2168 */         this.pnlMargins.updateInfo();
/*      */     }
/*      */ 
/*      */     void addOrientationListener(ServiceDialog.MarginsPanel paramMarginsPanel)
/*      */     {
/* 2174 */       this.pnlMargins = paramMarginsPanel;
/*      */     }
/*      */ 
/*      */     public void updateInfo() {
/* 2178 */       OrientationRequested localOrientationRequested = OrientationRequested.class;
/* 2179 */       boolean bool1 = false;
/* 2180 */       boolean bool2 = false;
/* 2181 */       boolean bool3 = false;
/* 2182 */       boolean bool4 = false;
/*      */       Object localObject2;
/* 2184 */       if (ServiceDialog.this.isAWT) {
/* 2185 */         bool1 = true;
/* 2186 */         bool2 = true;
/*      */       }
/* 2188 */       else if (ServiceDialog.this.psCurrent.isAttributeCategorySupported(localOrientationRequested)) {
/* 2189 */         localObject1 = ServiceDialog.this.psCurrent.getSupportedAttributeValues(localOrientationRequested, ServiceDialog.this.docFlavor, ServiceDialog.this.asCurrent);
/*      */ 
/* 2194 */         if ((localObject1 instanceof OrientationRequested[])) {
/* 2195 */           localObject2 = (OrientationRequested[])localObject1;
/*      */ 
/* 2198 */           for (int i = 0; i < localObject2.length; i++) {
/* 2199 */             Object localObject3 = localObject2[i];
/*      */ 
/* 2201 */             if (localObject3 == OrientationRequested.PORTRAIT)
/* 2202 */               bool1 = true;
/* 2203 */             else if (localObject3 == OrientationRequested.LANDSCAPE)
/* 2204 */               bool2 = true;
/* 2205 */             else if (localObject3 == OrientationRequested.REVERSE_PORTRAIT)
/* 2206 */               bool3 = true;
/* 2207 */             else if (localObject3 == OrientationRequested.REVERSE_LANDSCAPE) {
/* 2208 */               bool4 = true;
/*      */             }
/*      */           }
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 2215 */       this.rbPortrait.setEnabled(bool1);
/* 2216 */       this.rbLandscape.setEnabled(bool2);
/* 2217 */       this.rbRevPortrait.setEnabled(bool3);
/* 2218 */       this.rbRevLandscape.setEnabled(bool4);
/*      */ 
/* 2220 */       Object localObject1 = (OrientationRequested)ServiceDialog.this.asCurrent.get(localOrientationRequested);
/* 2221 */       if ((localObject1 == null) || (!ServiceDialog.this.psCurrent.isAttributeValueSupported((Attribute)localObject1, ServiceDialog.this.docFlavor, ServiceDialog.this.asCurrent)))
/*      */       {
/* 2224 */         localObject1 = (OrientationRequested)ServiceDialog.this.psCurrent.getDefaultAttributeValue(localOrientationRequested);
/*      */ 
/* 2226 */         if ((localObject1 != null) && (!ServiceDialog.this.psCurrent.isAttributeValueSupported((Attribute)localObject1, ServiceDialog.this.docFlavor, ServiceDialog.this.asCurrent)))
/*      */         {
/* 2228 */           localObject1 = null;
/* 2229 */           localObject2 = ServiceDialog.this.psCurrent.getSupportedAttributeValues(localOrientationRequested, ServiceDialog.this.docFlavor, ServiceDialog.this.asCurrent);
/*      */ 
/* 2233 */           if ((localObject2 instanceof OrientationRequested[])) {
/* 2234 */             OrientationRequested[] arrayOfOrientationRequested = (OrientationRequested[])localObject2;
/*      */ 
/* 2236 */             if (arrayOfOrientationRequested.length > 1)
/*      */             {
/* 2238 */               localObject1 = arrayOfOrientationRequested[0];
/*      */             }
/*      */           }
/*      */         }
/*      */ 
/* 2243 */         if (localObject1 == null) {
/* 2244 */           localObject1 = OrientationRequested.PORTRAIT;
/*      */         }
/* 2246 */         ServiceDialog.this.asCurrent.add((Attribute)localObject1);
/*      */       }
/*      */ 
/* 2249 */       if (localObject1 == OrientationRequested.PORTRAIT)
/* 2250 */         this.rbPortrait.setSelected(true);
/* 2251 */       else if (localObject1 == OrientationRequested.LANDSCAPE)
/* 2252 */         this.rbLandscape.setSelected(true);
/* 2253 */       else if (localObject1 == OrientationRequested.REVERSE_PORTRAIT)
/* 2254 */         this.rbRevPortrait.setSelected(true);
/*      */       else
/* 2256 */         this.rbRevLandscape.setSelected(true);
/*      */     }
/*      */   }
/*      */ 
/*      */   private class PageSetupPanel extends JPanel
/*      */   {
/*      */     private ServiceDialog.MediaPanel pnlMedia;
/*      */     private ServiceDialog.OrientationPanel pnlOrientation;
/*      */     private ServiceDialog.MarginsPanel pnlMargins;
/*      */ 
/*      */     public PageSetupPanel()
/*      */     {
/* 1313 */       GridBagLayout localGridBagLayout = new GridBagLayout();
/* 1314 */       GridBagConstraints localGridBagConstraints = new GridBagConstraints();
/*      */ 
/* 1316 */       setLayout(localGridBagLayout);
/*      */ 
/* 1318 */       localGridBagConstraints.fill = 1;
/* 1319 */       localGridBagConstraints.insets = ServiceDialog.panelInsets;
/* 1320 */       localGridBagConstraints.weightx = 1.0D;
/* 1321 */       localGridBagConstraints.weighty = 1.0D;
/*      */ 
/* 1323 */       localGridBagConstraints.gridwidth = 0;
/* 1324 */       this.pnlMedia = new ServiceDialog.MediaPanel(ServiceDialog.this);
/* 1325 */       ServiceDialog.addToGB(this.pnlMedia, this, localGridBagLayout, localGridBagConstraints);
/*      */ 
/* 1327 */       this.pnlOrientation = new ServiceDialog.OrientationPanel(ServiceDialog.this);
/* 1328 */       localGridBagConstraints.gridwidth = -1;
/* 1329 */       ServiceDialog.addToGB(this.pnlOrientation, this, localGridBagLayout, localGridBagConstraints);
/*      */ 
/* 1331 */       this.pnlMargins = new ServiceDialog.MarginsPanel(ServiceDialog.this);
/* 1332 */       this.pnlOrientation.addOrientationListener(this.pnlMargins);
/* 1333 */       this.pnlMedia.addMediaListener(this.pnlMargins);
/* 1334 */       localGridBagConstraints.gridwidth = 0;
/* 1335 */       ServiceDialog.addToGB(this.pnlMargins, this, localGridBagLayout, localGridBagConstraints);
/*      */     }
/*      */ 
/*      */     public void updateInfo() {
/* 1339 */       this.pnlMedia.updateInfo();
/* 1340 */       this.pnlOrientation.updateInfo();
/* 1341 */       this.pnlMargins.updateInfo();
/*      */     }
/*      */   }
/*      */ 
/*      */   private class PrintRangePanel extends JPanel
/*      */     implements ActionListener, FocusListener
/*      */   {
/*  962 */     private final String strTitle = ServiceDialog.getMsg("border.printrange");
/*  963 */     private final PageRanges prAll = new PageRanges(1, 2147483647);
/*      */     private JRadioButton rbAll;
/*      */     private JRadioButton rbPages;
/*      */     private JRadioButton rbSelect;
/*      */     private JFormattedTextField tfRangeFrom;
/*      */     private JFormattedTextField tfRangeTo;
/*      */     private JLabel lblRangeTo;
/*      */     private boolean prSupported;
/*      */ 
/*      */     public PrintRangePanel()
/*      */     {
/*  972 */       GridBagLayout localGridBagLayout = new GridBagLayout();
/*  973 */       GridBagConstraints localGridBagConstraints = new GridBagConstraints();
/*      */ 
/*  975 */       setLayout(localGridBagLayout);
/*  976 */       setBorder(BorderFactory.createTitledBorder(this.strTitle));
/*      */ 
/*  978 */       localGridBagConstraints.fill = 1;
/*  979 */       localGridBagConstraints.insets = ServiceDialog.compInsets;
/*  980 */       localGridBagConstraints.gridwidth = 0;
/*      */ 
/*  982 */       ButtonGroup localButtonGroup = new ButtonGroup();
/*  983 */       JPanel localJPanel1 = new JPanel(new FlowLayout(3));
/*  984 */       this.rbAll = ServiceDialog.createRadioButton("radiobutton.rangeall", this);
/*  985 */       this.rbAll.setSelected(true);
/*  986 */       localButtonGroup.add(this.rbAll);
/*  987 */       localJPanel1.add(this.rbAll);
/*  988 */       ServiceDialog.addToGB(localJPanel1, this, localGridBagLayout, localGridBagConstraints);
/*      */ 
/* 1003 */       JPanel localJPanel2 = new JPanel(new FlowLayout(3));
/* 1004 */       this.rbPages = ServiceDialog.createRadioButton("radiobutton.rangepages", this);
/* 1005 */       localButtonGroup.add(this.rbPages);
/* 1006 */       localJPanel2.add(this.rbPages);
/* 1007 */       DecimalFormat localDecimalFormat = new DecimalFormat("####0");
/* 1008 */       localDecimalFormat.setMinimumFractionDigits(0);
/* 1009 */       localDecimalFormat.setMaximumFractionDigits(0);
/* 1010 */       localDecimalFormat.setMinimumIntegerDigits(0);
/* 1011 */       localDecimalFormat.setMaximumIntegerDigits(5);
/* 1012 */       localDecimalFormat.setParseIntegerOnly(true);
/* 1013 */       localDecimalFormat.setDecimalSeparatorAlwaysShown(false);
/* 1014 */       NumberFormatter localNumberFormatter1 = new NumberFormatter(localDecimalFormat);
/* 1015 */       localNumberFormatter1.setMinimum(new Integer(1));
/* 1016 */       localNumberFormatter1.setMaximum(new Integer(2147483647));
/* 1017 */       localNumberFormatter1.setAllowsInvalid(true);
/* 1018 */       localNumberFormatter1.setCommitsOnValidEdit(true);
/* 1019 */       this.tfRangeFrom = new JFormattedTextField(localNumberFormatter1);
/* 1020 */       this.tfRangeFrom.setColumns(4);
/* 1021 */       this.tfRangeFrom.setEnabled(false);
/* 1022 */       this.tfRangeFrom.addActionListener(this);
/* 1023 */       this.tfRangeFrom.addFocusListener(this);
/* 1024 */       this.tfRangeFrom.setFocusLostBehavior(3);
/*      */ 
/* 1026 */       this.tfRangeFrom.getAccessibleContext().setAccessibleName(ServiceDialog.getMsg("radiobutton.rangepages"));
/*      */ 
/* 1028 */       localJPanel2.add(this.tfRangeFrom);
/* 1029 */       this.lblRangeTo = new JLabel(ServiceDialog.getMsg("label.rangeto"));
/* 1030 */       this.lblRangeTo.setEnabled(false);
/* 1031 */       localJPanel2.add(this.lblRangeTo);
/*      */       NumberFormatter localNumberFormatter2;
/*      */       try
/*      */       {
/* 1034 */         localNumberFormatter2 = (NumberFormatter)localNumberFormatter1.clone();
/*      */       } catch (CloneNotSupportedException localCloneNotSupportedException) {
/* 1036 */         localNumberFormatter2 = new NumberFormatter();
/*      */       }
/* 1038 */       this.tfRangeTo = new JFormattedTextField(localNumberFormatter2);
/* 1039 */       this.tfRangeTo.setColumns(4);
/* 1040 */       this.tfRangeTo.setEnabled(false);
/* 1041 */       this.tfRangeTo.addFocusListener(this);
/* 1042 */       this.tfRangeTo.getAccessibleContext().setAccessibleName(ServiceDialog.getMsg("label.rangeto"));
/*      */ 
/* 1044 */       localJPanel2.add(this.tfRangeTo);
/* 1045 */       ServiceDialog.addToGB(localJPanel2, this, localGridBagLayout, localGridBagConstraints);
/*      */     }
/*      */ 
/*      */     public void actionPerformed(ActionEvent paramActionEvent) {
/* 1049 */       Object localObject = paramActionEvent.getSource();
/* 1050 */       SunPageSelection localSunPageSelection = SunPageSelection.ALL;
/*      */ 
/* 1052 */       setupRangeWidgets();
/*      */ 
/* 1054 */       if (localObject == this.rbAll) {
/* 1055 */         ServiceDialog.this.asCurrent.add(this.prAll);
/* 1056 */       } else if (localObject == this.rbSelect) {
/* 1057 */         localSunPageSelection = SunPageSelection.SELECTION;
/* 1058 */       } else if ((localObject == this.rbPages) || (localObject == this.tfRangeFrom) || (localObject == this.tfRangeTo))
/*      */       {
/* 1061 */         updateRangeAttribute();
/* 1062 */         localSunPageSelection = SunPageSelection.RANGE;
/*      */       }
/*      */ 
/* 1065 */       if (ServiceDialog.this.isAWT)
/* 1066 */         ServiceDialog.this.asCurrent.add(localSunPageSelection);
/*      */     }
/*      */ 
/*      */     public void focusLost(FocusEvent paramFocusEvent)
/*      */     {
/* 1071 */       Object localObject = paramFocusEvent.getSource();
/*      */ 
/* 1073 */       if ((localObject == this.tfRangeFrom) || (localObject == this.tfRangeTo))
/* 1074 */         updateRangeAttribute();
/*      */     }
/*      */ 
/*      */     public void focusGained(FocusEvent paramFocusEvent) {
/*      */     }
/*      */ 
/*      */     private void setupRangeWidgets() {
/* 1081 */       boolean bool = (this.rbPages.isSelected()) && (this.prSupported);
/* 1082 */       this.tfRangeFrom.setEnabled(bool);
/* 1083 */       this.tfRangeTo.setEnabled(bool);
/* 1084 */       this.lblRangeTo.setEnabled(bool);
/*      */     }
/*      */ 
/*      */     private void updateRangeAttribute() {
/* 1088 */       String str1 = this.tfRangeFrom.getText();
/* 1089 */       String str2 = this.tfRangeTo.getText();
/*      */       int i;
/*      */       try
/*      */       {
/* 1095 */         i = Integer.parseInt(str1);
/*      */       } catch (NumberFormatException localNumberFormatException1) {
/* 1097 */         i = 1;
/*      */       }
/*      */       int j;
/*      */       try {
/* 1101 */         j = Integer.parseInt(str2);
/*      */       } catch (NumberFormatException localNumberFormatException2) {
/* 1103 */         j = i;
/*      */       }
/*      */ 
/* 1106 */       if (i < 1) {
/* 1107 */         i = 1;
/* 1108 */         this.tfRangeFrom.setValue(new Integer(1));
/*      */       }
/*      */ 
/* 1111 */       if (j < i) {
/* 1112 */         j = i;
/* 1113 */         this.tfRangeTo.setValue(new Integer(i));
/*      */       }
/*      */ 
/* 1116 */       PageRanges localPageRanges = new PageRanges(i, j);
/* 1117 */       ServiceDialog.this.asCurrent.add(localPageRanges);
/*      */     }
/*      */ 
/*      */     public void updateInfo() {
/* 1121 */       PageRanges localPageRanges1 = PageRanges.class;
/* 1122 */       this.prSupported = false;
/*      */ 
/* 1124 */       if ((ServiceDialog.this.psCurrent.isAttributeCategorySupported(localPageRanges1)) || (ServiceDialog.this.isAWT))
/*      */       {
/* 1126 */         this.prSupported = true;
/*      */       }
/*      */ 
/* 1129 */       SunPageSelection localSunPageSelection = SunPageSelection.ALL;
/* 1130 */       int i = 1;
/* 1131 */       int j = 1;
/*      */ 
/* 1133 */       PageRanges localPageRanges2 = (PageRanges)ServiceDialog.this.asCurrent.get(localPageRanges1);
/* 1134 */       if ((localPageRanges2 != null) && 
/* 1135 */         (!localPageRanges2.equals(this.prAll))) {
/* 1136 */         localSunPageSelection = SunPageSelection.RANGE;
/*      */ 
/* 1138 */         int[][] arrayOfInt = localPageRanges2.getMembers();
/* 1139 */         if ((arrayOfInt.length > 0) && (arrayOfInt[0].length > 1))
/*      */         {
/* 1141 */           i = arrayOfInt[0][0];
/* 1142 */           j = arrayOfInt[0][1];
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1147 */       if (ServiceDialog.this.isAWT) {
/* 1148 */         localSunPageSelection = (SunPageSelection)ServiceDialog.this.asCurrent.get(SunPageSelection.class);
/*      */       }
/*      */ 
/* 1152 */       if (localSunPageSelection == SunPageSelection.ALL)
/* 1153 */         this.rbAll.setSelected(true);
/* 1154 */       else if (localSunPageSelection != SunPageSelection.SELECTION)
/*      */       {
/* 1161 */         this.rbPages.setSelected(true);
/*      */       }
/* 1163 */       this.tfRangeFrom.setValue(new Integer(i));
/* 1164 */       this.tfRangeTo.setValue(new Integer(j));
/* 1165 */       this.rbAll.setEnabled(this.prSupported);
/* 1166 */       this.rbPages.setEnabled(this.prSupported);
/* 1167 */       setupRangeWidgets();
/*      */     }
/*      */   }
/*      */ 
/*      */   private class PrintServicePanel extends JPanel
/*      */     implements ActionListener, ItemListener, PopupMenuListener
/*      */   {
/*  705 */     private final String strTitle = ServiceDialog.getMsg("border.printservice");
/*      */     private FilePermission printToFilePermission;
/*      */     private JButton btnProperties;
/*      */     private JCheckBox cbPrintToFile;
/*      */     private JComboBox cbName;
/*      */     private JLabel lblType;
/*      */     private JLabel lblStatus;
/*      */     private JLabel lblInfo;
/*      */     private ServiceUIFactory uiFactory;
/*  712 */     private boolean changedService = false;
/*      */     private boolean filePermission;
/*      */ 
/*      */     public PrintServicePanel()
/*      */     {
/*  718 */       this.uiFactory = ServiceDialog.this.psCurrent.getServiceUIFactory();
/*      */ 
/*  720 */       GridBagLayout localGridBagLayout = new GridBagLayout();
/*  721 */       GridBagConstraints localGridBagConstraints = new GridBagConstraints();
/*      */ 
/*  723 */       setLayout(localGridBagLayout);
/*  724 */       setBorder(BorderFactory.createTitledBorder(this.strTitle));
/*      */ 
/*  726 */       String[] arrayOfString = new String[ServiceDialog.this.services.length];
/*  727 */       for (int i = 0; i < arrayOfString.length; i++) {
/*  728 */         arrayOfString[i] = ServiceDialog.this.services[i].getName();
/*      */       }
/*  730 */       this.cbName = new JComboBox(arrayOfString);
/*  731 */       this.cbName.setSelectedIndex(ServiceDialog.this.defaultServiceIndex);
/*  732 */       this.cbName.addItemListener(this);
/*  733 */       this.cbName.addPopupMenuListener(this);
/*      */ 
/*  735 */       localGridBagConstraints.fill = 1;
/*  736 */       localGridBagConstraints.insets = ServiceDialog.compInsets;
/*      */ 
/*  738 */       localGridBagConstraints.weightx = 0.0D;
/*  739 */       JLabel localJLabel = new JLabel(ServiceDialog.getMsg("label.psname"), 11);
/*  740 */       localJLabel.setDisplayedMnemonic(ServiceDialog.getMnemonic("label.psname"));
/*  741 */       localJLabel.setLabelFor(this.cbName);
/*  742 */       ServiceDialog.addToGB(localJLabel, this, localGridBagLayout, localGridBagConstraints);
/*  743 */       localGridBagConstraints.weightx = 1.0D;
/*  744 */       localGridBagConstraints.gridwidth = -1;
/*  745 */       ServiceDialog.addToGB(this.cbName, this, localGridBagLayout, localGridBagConstraints);
/*  746 */       localGridBagConstraints.weightx = 0.0D;
/*  747 */       localGridBagConstraints.gridwidth = 0;
/*  748 */       this.btnProperties = ServiceDialog.createButton("button.properties", this);
/*  749 */       ServiceDialog.addToGB(this.btnProperties, this, localGridBagLayout, localGridBagConstraints);
/*      */ 
/*  751 */       localGridBagConstraints.weighty = 1.0D;
/*  752 */       this.lblStatus = addLabel(ServiceDialog.getMsg("label.status"), localGridBagLayout, localGridBagConstraints);
/*  753 */       this.lblStatus.setLabelFor(null);
/*      */ 
/*  755 */       this.lblType = addLabel(ServiceDialog.getMsg("label.pstype"), localGridBagLayout, localGridBagConstraints);
/*  756 */       this.lblType.setLabelFor(null);
/*      */ 
/*  758 */       localGridBagConstraints.gridwidth = 1;
/*  759 */       ServiceDialog.addToGB(new JLabel(ServiceDialog.getMsg("label.info"), 11), this, localGridBagLayout, localGridBagConstraints);
/*      */ 
/*  761 */       localGridBagConstraints.gridwidth = -1;
/*  762 */       this.lblInfo = new JLabel();
/*  763 */       this.lblInfo.setLabelFor(null);
/*      */ 
/*  765 */       ServiceDialog.addToGB(this.lblInfo, this, localGridBagLayout, localGridBagConstraints);
/*      */ 
/*  767 */       localGridBagConstraints.gridwidth = 0;
/*  768 */       this.cbPrintToFile = ServiceDialog.createCheckBox("checkbox.printtofile", this);
/*  769 */       ServiceDialog.addToGB(this.cbPrintToFile, this, localGridBagLayout, localGridBagConstraints);
/*      */ 
/*  771 */       this.filePermission = allowedToPrintToFile();
/*      */     }
/*      */ 
/*      */     public boolean isPrintToFileSelected() {
/*  775 */       return this.cbPrintToFile.isSelected();
/*      */     }
/*      */ 
/*      */     private JLabel addLabel(String paramString, GridBagLayout paramGridBagLayout, GridBagConstraints paramGridBagConstraints)
/*      */     {
/*  781 */       paramGridBagConstraints.gridwidth = 1;
/*  782 */       ServiceDialog.addToGB(new JLabel(paramString, 11), this, paramGridBagLayout, paramGridBagConstraints);
/*      */ 
/*  784 */       paramGridBagConstraints.gridwidth = 0;
/*  785 */       JLabel localJLabel = new JLabel();
/*  786 */       ServiceDialog.addToGB(localJLabel, this, paramGridBagLayout, paramGridBagConstraints);
/*      */ 
/*  788 */       return localJLabel;
/*      */     }
/*      */ 
/*      */     public void actionPerformed(ActionEvent paramActionEvent) {
/*  792 */       Object localObject = paramActionEvent.getSource();
/*      */ 
/*  794 */       if ((localObject == this.btnProperties) && 
/*  795 */         (this.uiFactory != null)) {
/*  796 */         JDialog localJDialog = (JDialog)this.uiFactory.getUI(3, "javax.swing.JDialog");
/*      */ 
/*  800 */         if (localJDialog != null) {
/*  801 */           localJDialog.show();
/*      */         } else {
/*  803 */           DocumentPropertiesUI localDocumentPropertiesUI = null;
/*      */           try {
/*  805 */             localDocumentPropertiesUI = (DocumentPropertiesUI)this.uiFactory.getUI(199, DocumentPropertiesUI.DOCPROPERTIESCLASSNAME);
/*      */           }
/*      */           catch (Exception localException)
/*      */           {
/*      */           }
/*      */ 
/*  811 */           if (localDocumentPropertiesUI != null) {
/*  812 */             PrinterJobWrapper localPrinterJobWrapper = (PrinterJobWrapper)ServiceDialog.this.asCurrent.get(PrinterJobWrapper.class);
/*      */ 
/*  814 */             if (localPrinterJobWrapper == null) {
/*  815 */               return;
/*      */             }
/*  817 */             PrinterJob localPrinterJob = localPrinterJobWrapper.getPrinterJob();
/*  818 */             if (localPrinterJob == null) {
/*  819 */               return;
/*      */             }
/*  821 */             PrintRequestAttributeSet localPrintRequestAttributeSet = localDocumentPropertiesUI.showDocumentProperties(localPrinterJob, ServiceDialog.this, ServiceDialog.this.psCurrent, ServiceDialog.this.asCurrent);
/*      */ 
/*  824 */             if (localPrintRequestAttributeSet != null) {
/*  825 */               ServiceDialog.this.asCurrent.addAll(localPrintRequestAttributeSet);
/*  826 */               ServiceDialog.this.updatePanels();
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     public void itemStateChanged(ItemEvent paramItemEvent)
/*      */     {
/*  835 */       if (paramItemEvent.getStateChange() == 1) {
/*  836 */         int i = this.cbName.getSelectedIndex();
/*      */ 
/*  838 */         if ((i >= 0) && (i < ServiceDialog.this.services.length) && 
/*  839 */           (!ServiceDialog.this.services[i].equals(ServiceDialog.this.psCurrent))) {
/*  840 */           ServiceDialog.this.psCurrent = ServiceDialog.this.services[i];
/*  841 */           this.uiFactory = ServiceDialog.this.psCurrent.getServiceUIFactory();
/*  842 */           this.changedService = true;
/*      */ 
/*  844 */           Destination localDestination = (Destination)ServiceDialog.this.asOriginal.get(Destination.class);
/*      */ 
/*  847 */           if (((localDestination != null) || (isPrintToFileSelected())) && (ServiceDialog.this.psCurrent.isAttributeCategorySupported(Destination.class)))
/*      */           {
/*  851 */             if (localDestination != null) {
/*  852 */               ServiceDialog.this.asCurrent.add(localDestination);
/*      */             } else {
/*  854 */               localDestination = (Destination)ServiceDialog.this.psCurrent.getDefaultAttributeValue(Destination.class);
/*      */ 
/*  860 */               if (localDestination == null) {
/*      */                 try {
/*  862 */                   localDestination = new Destination(new URI("file:out.prn"));
/*      */                 }
/*      */                 catch (URISyntaxException localURISyntaxException)
/*      */                 {
/*      */                 }
/*      */               }
/*  868 */               if (localDestination != null)
/*  869 */                 ServiceDialog.this.asCurrent.add(localDestination);
/*      */             }
/*      */           }
/*      */           else
/*  873 */             ServiceDialog.this.asCurrent.remove(Destination.class);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     public void popupMenuWillBecomeVisible(PopupMenuEvent paramPopupMenuEvent)
/*      */     {
/*  881 */       this.changedService = false;
/*      */     }
/*      */ 
/*      */     public void popupMenuWillBecomeInvisible(PopupMenuEvent paramPopupMenuEvent) {
/*  885 */       if (this.changedService) {
/*  886 */         this.changedService = false;
/*  887 */         ServiceDialog.this.updatePanels();
/*      */       }
/*      */     }
/*      */ 
/*      */     public void popupMenuCanceled(PopupMenuEvent paramPopupMenuEvent)
/*      */     {
/*      */     }
/*      */ 
/*      */     private boolean allowedToPrintToFile()
/*      */     {
/*      */       try
/*      */       {
/*  899 */         throwPrintToFile();
/*  900 */         return true; } catch (SecurityException localSecurityException) {
/*      */       }
/*  902 */       return false;
/*      */     }
/*      */ 
/*      */     private void throwPrintToFile()
/*      */     {
/*  912 */       SecurityManager localSecurityManager = System.getSecurityManager();
/*  913 */       if (localSecurityManager != null) {
/*  914 */         if (this.printToFilePermission == null) {
/*  915 */           this.printToFilePermission = new FilePermission("<<ALL FILES>>", "read,write");
/*      */         }
/*      */ 
/*  918 */         localSecurityManager.checkPermission(this.printToFilePermission);
/*      */       }
/*      */     }
/*      */ 
/*      */     public void updateInfo() {
/*  923 */       Destination localDestination1 = Destination.class;
/*  924 */       int i = 0;
/*  925 */       int j = 0;
/*  926 */       int k = this.filePermission ? allowedToPrintToFile() : 0;
/*      */ 
/*  930 */       if (ServiceDialog.this.psCurrent.isAttributeCategorySupported(localDestination1)) {
/*  931 */         i = 1;
/*      */       }
/*  933 */       Destination localDestination2 = (Destination)ServiceDialog.this.asCurrent.get(localDestination1);
/*  934 */       if (localDestination2 != null) {
/*  935 */         j = 1;
/*      */       }
/*  937 */       this.cbPrintToFile.setEnabled((i != 0) && (k != 0));
/*  938 */       this.cbPrintToFile.setSelected((j != 0) && (k != 0) && (i != 0));
/*      */ 
/*  942 */       PrintServiceAttribute localPrintServiceAttribute1 = ServiceDialog.this.psCurrent.getAttribute(PrinterMakeAndModel.class);
/*  943 */       if (localPrintServiceAttribute1 != null) {
/*  944 */         this.lblType.setText(localPrintServiceAttribute1.toString());
/*      */       }
/*  946 */       PrintServiceAttribute localPrintServiceAttribute2 = ServiceDialog.this.psCurrent.getAttribute(PrinterIsAcceptingJobs.class);
/*      */ 
/*  948 */       if (localPrintServiceAttribute2 != null) {
/*  949 */         this.lblStatus.setText(ServiceDialog.getMsg(localPrintServiceAttribute2.toString()));
/*      */       }
/*  951 */       PrintServiceAttribute localPrintServiceAttribute3 = ServiceDialog.this.psCurrent.getAttribute(PrinterInfo.class);
/*  952 */       if (localPrintServiceAttribute3 != null) {
/*  953 */         this.lblInfo.setText(localPrintServiceAttribute3.toString());
/*      */       }
/*  955 */       this.btnProperties.setEnabled(this.uiFactory != null);
/*      */     }
/*      */   }
/*      */ 
/*      */   private class QualityPanel extends JPanel
/*      */     implements ActionListener
/*      */   {
/* 2406 */     private final String strTitle = ServiceDialog.getMsg("border.quality");
/*      */     private JRadioButton rbDraft;
/*      */     private JRadioButton rbNormal;
/*      */     private JRadioButton rbHigh;
/*      */ 
/*      */     public QualityPanel()
/*      */     {
/* 2412 */       GridBagLayout localGridBagLayout = new GridBagLayout();
/* 2413 */       GridBagConstraints localGridBagConstraints = new GridBagConstraints();
/*      */ 
/* 2415 */       setLayout(localGridBagLayout);
/* 2416 */       setBorder(BorderFactory.createTitledBorder(this.strTitle));
/*      */ 
/* 2418 */       localGridBagConstraints.fill = 1;
/* 2419 */       localGridBagConstraints.gridwidth = 0;
/* 2420 */       localGridBagConstraints.weighty = 1.0D;
/*      */ 
/* 2422 */       ButtonGroup localButtonGroup = new ButtonGroup();
/* 2423 */       this.rbDraft = ServiceDialog.createRadioButton("radiobutton.draftq", this);
/* 2424 */       localButtonGroup.add(this.rbDraft);
/* 2425 */       ServiceDialog.addToGB(this.rbDraft, this, localGridBagLayout, localGridBagConstraints);
/* 2426 */       this.rbNormal = ServiceDialog.createRadioButton("radiobutton.normalq", this);
/* 2427 */       this.rbNormal.setSelected(true);
/* 2428 */       localButtonGroup.add(this.rbNormal);
/* 2429 */       ServiceDialog.addToGB(this.rbNormal, this, localGridBagLayout, localGridBagConstraints);
/* 2430 */       this.rbHigh = ServiceDialog.createRadioButton("radiobutton.highq", this);
/* 2431 */       localButtonGroup.add(this.rbHigh);
/* 2432 */       ServiceDialog.addToGB(this.rbHigh, this, localGridBagLayout, localGridBagConstraints);
/*      */     }
/*      */ 
/*      */     public void actionPerformed(ActionEvent paramActionEvent) {
/* 2436 */       Object localObject = paramActionEvent.getSource();
/*      */ 
/* 2438 */       if (localObject == this.rbDraft)
/* 2439 */         ServiceDialog.this.asCurrent.add(PrintQuality.DRAFT);
/* 2440 */       else if (localObject == this.rbNormal)
/* 2441 */         ServiceDialog.this.asCurrent.add(PrintQuality.NORMAL);
/* 2442 */       else if (localObject == this.rbHigh)
/* 2443 */         ServiceDialog.this.asCurrent.add(PrintQuality.HIGH);
/*      */     }
/*      */ 
/*      */     public void updateInfo()
/*      */     {
/* 2448 */       PrintQuality localPrintQuality1 = PrintQuality.class;
/* 2449 */       boolean bool1 = false;
/* 2450 */       boolean bool2 = false;
/* 2451 */       boolean bool3 = false;
/*      */ 
/* 2453 */       if (ServiceDialog.this.isAWT) {
/* 2454 */         bool1 = true;
/* 2455 */         bool2 = true;
/* 2456 */         bool3 = true;
/*      */       }
/* 2458 */       else if (ServiceDialog.this.psCurrent.isAttributeCategorySupported(localPrintQuality1)) {
/* 2459 */         localObject = ServiceDialog.this.psCurrent.getSupportedAttributeValues(localPrintQuality1, ServiceDialog.this.docFlavor, ServiceDialog.this.asCurrent);
/*      */ 
/* 2464 */         if ((localObject instanceof PrintQuality[])) {
/* 2465 */           PrintQuality[] arrayOfPrintQuality = (PrintQuality[])localObject;
/*      */ 
/* 2467 */           for (int i = 0; i < arrayOfPrintQuality.length; i++) {
/* 2468 */             PrintQuality localPrintQuality2 = arrayOfPrintQuality[i];
/*      */ 
/* 2470 */             if (localPrintQuality2 == PrintQuality.DRAFT)
/* 2471 */               bool1 = true;
/* 2472 */             else if (localPrintQuality2 == PrintQuality.NORMAL)
/* 2473 */               bool2 = true;
/* 2474 */             else if (localPrintQuality2 == PrintQuality.HIGH) {
/* 2475 */               bool3 = true;
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */ 
/* 2481 */       this.rbDraft.setEnabled(bool1);
/* 2482 */       this.rbNormal.setEnabled(bool2);
/* 2483 */       this.rbHigh.setEnabled(bool3);
/*      */ 
/* 2485 */       Object localObject = (PrintQuality)ServiceDialog.this.asCurrent.get(localPrintQuality1);
/* 2486 */       if (localObject == null) {
/* 2487 */         localObject = (PrintQuality)ServiceDialog.this.psCurrent.getDefaultAttributeValue(localPrintQuality1);
/* 2488 */         if (localObject == null) {
/* 2489 */           localObject = PrintQuality.NORMAL;
/*      */         }
/*      */       }
/*      */ 
/* 2493 */       if (localObject == PrintQuality.DRAFT)
/* 2494 */         this.rbDraft.setSelected(true);
/* 2495 */       else if (localObject == PrintQuality.NORMAL)
/* 2496 */         this.rbNormal.setSelected(true);
/*      */       else
/* 2498 */         this.rbHigh.setSelected(true);
/*      */     }
/*      */   }
/*      */ 
/*      */   private class SidesPanel extends JPanel implements ActionListener
/*      */   {
/* 2507 */     private final String strTitle = ServiceDialog.getMsg("border.sides");
/*      */     private ServiceDialog.IconRadioButton rbOneSide;
/*      */     private ServiceDialog.IconRadioButton rbTumble;
/*      */     private ServiceDialog.IconRadioButton rbDuplex;
/*      */ 
/*      */     public SidesPanel() {
/* 2513 */       GridBagLayout localGridBagLayout = new GridBagLayout();
/* 2514 */       GridBagConstraints localGridBagConstraints = new GridBagConstraints();
/*      */ 
/* 2516 */       setLayout(localGridBagLayout);
/* 2517 */       setBorder(BorderFactory.createTitledBorder(this.strTitle));
/*      */ 
/* 2519 */       localGridBagConstraints.fill = 1;
/* 2520 */       localGridBagConstraints.insets = ServiceDialog.compInsets;
/* 2521 */       localGridBagConstraints.weighty = 1.0D;
/* 2522 */       localGridBagConstraints.gridwidth = 0;
/*      */ 
/* 2524 */       ButtonGroup localButtonGroup = new ButtonGroup();
/* 2525 */       this.rbOneSide = new ServiceDialog.IconRadioButton(ServiceDialog.this, "radiobutton.oneside", "oneside.png", true, localButtonGroup, this);
/*      */ 
/* 2528 */       this.rbOneSide.addActionListener(this);
/* 2529 */       ServiceDialog.addToGB(this.rbOneSide, this, localGridBagLayout, localGridBagConstraints);
/* 2530 */       this.rbTumble = new ServiceDialog.IconRadioButton(ServiceDialog.this, "radiobutton.tumble", "tumble.png", false, localButtonGroup, this);
/*      */ 
/* 2533 */       this.rbTumble.addActionListener(this);
/* 2534 */       ServiceDialog.addToGB(this.rbTumble, this, localGridBagLayout, localGridBagConstraints);
/* 2535 */       this.rbDuplex = new ServiceDialog.IconRadioButton(ServiceDialog.this, "radiobutton.duplex", "duplex.png", false, localButtonGroup, this);
/*      */ 
/* 2538 */       this.rbDuplex.addActionListener(this);
/* 2539 */       localGridBagConstraints.gridwidth = 0;
/* 2540 */       ServiceDialog.addToGB(this.rbDuplex, this, localGridBagLayout, localGridBagConstraints);
/*      */     }
/*      */ 
/*      */     public void actionPerformed(ActionEvent paramActionEvent) {
/* 2544 */       Object localObject = paramActionEvent.getSource();
/*      */ 
/* 2546 */       if (this.rbOneSide.isSameAs(localObject))
/* 2547 */         ServiceDialog.this.asCurrent.add(Sides.ONE_SIDED);
/* 2548 */       else if (this.rbTumble.isSameAs(localObject))
/* 2549 */         ServiceDialog.this.asCurrent.add(Sides.TUMBLE);
/* 2550 */       else if (this.rbDuplex.isSameAs(localObject))
/* 2551 */         ServiceDialog.this.asCurrent.add(Sides.DUPLEX);
/*      */     }
/*      */ 
/*      */     public void updateInfo()
/*      */     {
/* 2556 */       Sides localSides1 = Sides.class;
/* 2557 */       boolean bool1 = false;
/* 2558 */       boolean bool2 = false;
/* 2559 */       boolean bool3 = false;
/*      */ 
/* 2561 */       if (ServiceDialog.this.psCurrent.isAttributeCategorySupported(localSides1)) {
/* 2562 */         localObject = ServiceDialog.this.psCurrent.getSupportedAttributeValues(localSides1, ServiceDialog.this.docFlavor, ServiceDialog.this.asCurrent);
/*      */ 
/* 2567 */         if ((localObject instanceof Sides[])) {
/* 2568 */           Sides[] arrayOfSides = (Sides[])localObject;
/*      */ 
/* 2570 */           for (int i = 0; i < arrayOfSides.length; i++) {
/* 2571 */             Sides localSides2 = arrayOfSides[i];
/*      */ 
/* 2573 */             if (localSides2 == Sides.ONE_SIDED)
/* 2574 */               bool1 = true;
/* 2575 */             else if (localSides2 == Sides.TUMBLE)
/* 2576 */               bool2 = true;
/* 2577 */             else if (localSides2 == Sides.DUPLEX) {
/* 2578 */               bool3 = true;
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/* 2583 */       this.rbOneSide.setEnabled(bool1);
/* 2584 */       this.rbTumble.setEnabled(bool2);
/* 2585 */       this.rbDuplex.setEnabled(bool3);
/*      */ 
/* 2587 */       Object localObject = (Sides)ServiceDialog.this.asCurrent.get(localSides1);
/* 2588 */       if (localObject == null) {
/* 2589 */         localObject = (Sides)ServiceDialog.this.psCurrent.getDefaultAttributeValue(localSides1);
/* 2590 */         if (localObject == null) {
/* 2591 */           localObject = Sides.ONE_SIDED;
/*      */         }
/*      */       }
/*      */ 
/* 2595 */       if (localObject == Sides.ONE_SIDED)
/* 2596 */         this.rbOneSide.setSelected(true);
/* 2597 */       else if (localObject == Sides.TUMBLE)
/* 2598 */         this.rbTumble.setSelected(true);
/*      */       else
/* 2600 */         this.rbDuplex.setSelected(true);
/*      */     }
/*      */   }
/*      */ 
/*      */   private class ValidatingFileChooser extends JFileChooser
/*      */   {
/*      */     private ValidatingFileChooser()
/*      */     {
/*      */     }
/*      */ 
/*      */     public void approveSelection()
/*      */     {
/* 2848 */       File localFile1 = getSelectedFile();
/*      */       boolean bool;
/*      */       try
/*      */       {
/* 2852 */         bool = localFile1.exists();
/*      */       } catch (SecurityException localSecurityException1) {
/* 2854 */         bool = false;
/*      */       }
/*      */ 
/* 2857 */       if (bool)
/*      */       {
/* 2859 */         int i = JOptionPane.showConfirmDialog(this, ServiceDialog.getMsg("dialog.overwrite"), ServiceDialog.getMsg("dialog.owtitle"), 0);
/*      */ 
/* 2863 */         if (i != 0) {
/* 2864 */           return;
/*      */         }
/*      */       }
/*      */       try
/*      */       {
/* 2869 */         if (localFile1.createNewFile())
/* 2870 */           localFile1.delete();
/*      */       }
/*      */       catch (IOException localIOException) {
/* 2873 */         JOptionPane.showMessageDialog(this, ServiceDialog.getMsg("dialog.writeerror") + " " + localFile1, ServiceDialog.getMsg("dialog.owtitle"), 2);
/*      */ 
/* 2877 */         return;
/*      */       }
/*      */       catch (SecurityException localSecurityException2)
/*      */       {
/*      */       }
/*      */ 
/* 2884 */       File localFile2 = localFile1.getParentFile();
/* 2885 */       if (((localFile1.exists()) && ((!localFile1.isFile()) || (!localFile1.canWrite()))) || ((localFile2 != null) && ((!localFile2.exists()) || ((localFile2.exists()) && (!localFile2.canWrite())))))
/*      */       {
/* 2889 */         JOptionPane.showMessageDialog(this, ServiceDialog.getMsg("dialog.writeerror") + " " + localFile1, ServiceDialog.getMsg("dialog.owtitle"), 2);
/*      */ 
/* 2893 */         return;
/*      */       }
/*      */ 
/* 2896 */       super.approveSelection();
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.print.ServiceDialog
 * JD-Core Version:    0.6.2
 */