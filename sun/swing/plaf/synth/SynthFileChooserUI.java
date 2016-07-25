/*     */ package sun.swing.plaf.synth;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.ComponentOrientation;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Insets;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.io.File;
/*     */ import java.util.Vector;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import java.util.regex.PatternSyntaxException;
/*     */ import javax.swing.AbstractAction;
/*     */ import javax.swing.Action;
/*     */ import javax.swing.ActionMap;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JFileChooser;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.border.AbstractBorder;
/*     */ import javax.swing.border.Border;
/*     */ import javax.swing.event.ListDataEvent;
/*     */ import javax.swing.event.ListDataListener;
/*     */ import javax.swing.filechooser.FileFilter;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.UIResource;
/*     */ import javax.swing.plaf.basic.BasicDirectoryModel;
/*     */ import javax.swing.plaf.basic.BasicFileChooserUI;
/*     */ import javax.swing.plaf.synth.ColorType;
/*     */ import javax.swing.plaf.synth.Region;
/*     */ import javax.swing.plaf.synth.SynthContext;
/*     */ import javax.swing.plaf.synth.SynthLookAndFeel;
/*     */ import javax.swing.plaf.synth.SynthPainter;
/*     */ import javax.swing.plaf.synth.SynthStyle;
/*     */ import javax.swing.plaf.synth.SynthStyleFactory;
/*     */ import javax.swing.plaf.synth.SynthUI;
/*     */ 
/*     */ public abstract class SynthFileChooserUI extends BasicFileChooserUI
/*     */   implements SynthUI
/*     */ {
/*     */   private JButton approveButton;
/*     */   private JButton cancelButton;
/*     */   private SynthStyle style;
/*  65 */   private Action fileNameCompletionAction = new FileNameCompletionAction();
/*     */ 
/*  67 */   private FileFilter actualFileFilter = null;
/*  68 */   private GlobFilter globFilter = null;
/*     */   private String fileNameCompletionString;
/*     */ 
/*     */   public static ComponentUI createUI(JComponent paramJComponent)
/*     */   {
/*  71 */     return new SynthFileChooserUIImpl((JFileChooser)paramJComponent);
/*     */   }
/*     */ 
/*     */   public SynthFileChooserUI(JFileChooser paramJFileChooser) {
/*  75 */     super(paramJFileChooser);
/*     */   }
/*     */ 
/*     */   public SynthContext getContext(JComponent paramJComponent) {
/*  79 */     return new SynthContext(paramJComponent, Region.FILE_CHOOSER, this.style, getComponentState(paramJComponent));
/*     */   }
/*     */ 
/*     */   protected SynthContext getContext(JComponent paramJComponent, int paramInt)
/*     */   {
/*  84 */     Region localRegion = SynthLookAndFeel.getRegion(paramJComponent);
/*  85 */     return new SynthContext(paramJComponent, Region.FILE_CHOOSER, this.style, paramInt);
/*     */   }
/*     */ 
/*     */   private Region getRegion(JComponent paramJComponent) {
/*  89 */     return SynthLookAndFeel.getRegion(paramJComponent);
/*     */   }
/*     */ 
/*     */   private int getComponentState(JComponent paramJComponent) {
/*  93 */     if (paramJComponent.isEnabled()) {
/*  94 */       if (paramJComponent.isFocusOwner()) {
/*  95 */         return 257;
/*     */       }
/*  97 */       return 1;
/*     */     }
/*  99 */     return 8;
/*     */   }
/*     */ 
/*     */   private void updateStyle(JComponent paramJComponent) {
/* 103 */     SynthStyle localSynthStyle = SynthLookAndFeel.getStyleFactory().getStyle(paramJComponent, Region.FILE_CHOOSER);
/*     */ 
/* 105 */     if (localSynthStyle != this.style) {
/* 106 */       if (this.style != null) {
/* 107 */         this.style.uninstallDefaults(getContext(paramJComponent, 1));
/*     */       }
/* 109 */       this.style = localSynthStyle;
/* 110 */       SynthContext localSynthContext = getContext(paramJComponent, 1);
/* 111 */       this.style.installDefaults(localSynthContext);
/* 112 */       Border localBorder = paramJComponent.getBorder();
/* 113 */       if ((localBorder == null) || ((localBorder instanceof UIResource))) {
/* 114 */         paramJComponent.setBorder(new UIBorder(this.style.getInsets(localSynthContext, null)));
/*     */       }
/*     */ 
/* 117 */       this.directoryIcon = this.style.getIcon(localSynthContext, "FileView.directoryIcon");
/* 118 */       this.fileIcon = this.style.getIcon(localSynthContext, "FileView.fileIcon");
/* 119 */       this.computerIcon = this.style.getIcon(localSynthContext, "FileView.computerIcon");
/* 120 */       this.hardDriveIcon = this.style.getIcon(localSynthContext, "FileView.hardDriveIcon");
/* 121 */       this.floppyDriveIcon = this.style.getIcon(localSynthContext, "FileView.floppyDriveIcon");
/*     */ 
/* 123 */       this.newFolderIcon = this.style.getIcon(localSynthContext, "FileChooser.newFolderIcon");
/* 124 */       this.upFolderIcon = this.style.getIcon(localSynthContext, "FileChooser.upFolderIcon");
/* 125 */       this.homeFolderIcon = this.style.getIcon(localSynthContext, "FileChooser.homeFolderIcon");
/* 126 */       this.detailsViewIcon = this.style.getIcon(localSynthContext, "FileChooser.detailsViewIcon");
/* 127 */       this.listViewIcon = this.style.getIcon(localSynthContext, "FileChooser.listViewIcon");
/*     */     }
/*     */   }
/*     */ 
/*     */   public void installUI(JComponent paramJComponent) {
/* 132 */     super.installUI(paramJComponent);
/* 133 */     SwingUtilities.replaceUIActionMap(paramJComponent, createActionMap());
/*     */   }
/*     */ 
/*     */   public void installComponents(JFileChooser paramJFileChooser) {
/* 137 */     SynthContext localSynthContext = getContext(paramJFileChooser, 1);
/*     */ 
/* 139 */     this.cancelButton = new JButton(this.cancelButtonText);
/* 140 */     this.cancelButton.setName("SynthFileChooser.cancelButton");
/* 141 */     this.cancelButton.setIcon(localSynthContext.getStyle().getIcon(localSynthContext, "FileChooser.cancelIcon"));
/* 142 */     this.cancelButton.setMnemonic(this.cancelButtonMnemonic);
/* 143 */     this.cancelButton.setToolTipText(this.cancelButtonToolTipText);
/* 144 */     this.cancelButton.addActionListener(getCancelSelectionAction());
/*     */ 
/* 146 */     this.approveButton = new JButton(getApproveButtonText(paramJFileChooser));
/* 147 */     this.approveButton.setName("SynthFileChooser.approveButton");
/* 148 */     this.approveButton.setIcon(localSynthContext.getStyle().getIcon(localSynthContext, "FileChooser.okIcon"));
/* 149 */     this.approveButton.setMnemonic(getApproveButtonMnemonic(paramJFileChooser));
/* 150 */     this.approveButton.setToolTipText(getApproveButtonToolTipText(paramJFileChooser));
/* 151 */     this.approveButton.addActionListener(getApproveSelectionAction());
/*     */   }
/*     */ 
/*     */   public void uninstallComponents(JFileChooser paramJFileChooser)
/*     */   {
/* 156 */     paramJFileChooser.removeAll();
/*     */   }
/*     */ 
/*     */   protected void installListeners(JFileChooser paramJFileChooser) {
/* 160 */     super.installListeners(paramJFileChooser);
/*     */ 
/* 162 */     getModel().addListDataListener(new ListDataListener()
/*     */     {
/*     */       public void contentsChanged(ListDataEvent paramAnonymousListDataEvent) {
/* 165 */         new SynthFileChooserUI.DelayedSelectionUpdater(SynthFileChooserUI.this);
/*     */       }
/*     */       public void intervalAdded(ListDataEvent paramAnonymousListDataEvent) {
/* 168 */         new SynthFileChooserUI.DelayedSelectionUpdater(SynthFileChooserUI.this);
/*     */       }
/*     */ 
/*     */       public void intervalRemoved(ListDataEvent paramAnonymousListDataEvent)
/*     */       {
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   protected abstract ActionMap createActionMap();
/*     */ 
/*     */   protected void installDefaults(JFileChooser paramJFileChooser)
/*     */   {
/* 190 */     super.installDefaults(paramJFileChooser);
/* 191 */     updateStyle(paramJFileChooser);
/*     */   }
/*     */ 
/*     */   protected void uninstallDefaults(JFileChooser paramJFileChooser) {
/* 195 */     super.uninstallDefaults(paramJFileChooser);
/*     */ 
/* 197 */     SynthContext localSynthContext = getContext(getFileChooser(), 1);
/* 198 */     this.style.uninstallDefaults(localSynthContext);
/* 199 */     this.style = null;
/*     */   }
/*     */ 
/*     */   protected void installIcons(JFileChooser paramJFileChooser)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void update(Graphics paramGraphics, JComponent paramJComponent) {
/* 207 */     SynthContext localSynthContext = getContext(paramJComponent);
/*     */ 
/* 209 */     if (paramJComponent.isOpaque()) {
/* 210 */       paramGraphics.setColor(this.style.getColor(localSynthContext, ColorType.BACKGROUND));
/* 211 */       paramGraphics.fillRect(0, 0, paramJComponent.getWidth(), paramJComponent.getHeight());
/*     */     }
/*     */ 
/* 214 */     this.style.getPainter(localSynthContext).paintFileChooserBackground(localSynthContext, paramGraphics, 0, 0, paramJComponent.getWidth(), paramJComponent.getHeight());
/*     */ 
/* 216 */     paint(localSynthContext, paramGraphics);
/*     */   }
/*     */ 
/*     */   public void paintBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
/*     */   }
/*     */ 
/*     */   public void paint(Graphics paramGraphics, JComponent paramJComponent) {
/* 223 */     SynthContext localSynthContext = getContext(paramJComponent);
/*     */ 
/* 225 */     paint(localSynthContext, paramGraphics);
/*     */   }
/*     */   protected void paint(SynthContext paramSynthContext, Graphics paramGraphics) {
/*     */   }
/*     */ 
/*     */   public abstract void setFileName(String paramString);
/*     */ 
/*     */   public abstract String getFileName();
/*     */ 
/*     */   protected void doSelectedFileChanged(PropertyChangeEvent paramPropertyChangeEvent) {
/*     */   }
/*     */ 
/*     */   protected void doSelectedFilesChanged(PropertyChangeEvent paramPropertyChangeEvent) {
/*     */   }
/*     */ 
/*     */   protected void doDirectoryChanged(PropertyChangeEvent paramPropertyChangeEvent) {
/*     */   }
/*     */ 
/*     */   protected void doAccessoryChanged(PropertyChangeEvent paramPropertyChangeEvent) {
/*     */   }
/*     */ 
/*     */   protected void doFileSelectionModeChanged(PropertyChangeEvent paramPropertyChangeEvent) {
/*     */   }
/*     */ 
/*     */   protected void doMultiSelectionChanged(PropertyChangeEvent paramPropertyChangeEvent) {
/* 250 */     if (!getFileChooser().isMultiSelectionEnabled())
/* 251 */       getFileChooser().setSelectedFiles(null);
/*     */   }
/*     */ 
/*     */   protected void doControlButtonsChanged(PropertyChangeEvent paramPropertyChangeEvent)
/*     */   {
/* 256 */     if (getFileChooser().getControlButtonsAreShown()) {
/* 257 */       this.approveButton.setText(getApproveButtonText(getFileChooser()));
/* 258 */       this.approveButton.setToolTipText(getApproveButtonToolTipText(getFileChooser()));
/* 259 */       this.approveButton.setMnemonic(getApproveButtonMnemonic(getFileChooser()));
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void doAncestorChanged(PropertyChangeEvent paramPropertyChangeEvent) {
/*     */   }
/*     */ 
/*     */   public PropertyChangeListener createPropertyChangeListener(JFileChooser paramJFileChooser) {
/* 267 */     return new SynthFCPropertyChangeListener(null);
/*     */   }
/*     */ 
/*     */   private void updateFileNameCompletion()
/*     */   {
/* 349 */     if ((this.fileNameCompletionString != null) && 
/* 350 */       (this.fileNameCompletionString.equals(getFileName()))) {
/* 351 */       File[] arrayOfFile = (File[])getModel().getFiles().toArray(new File[0]);
/* 352 */       String str = getCommonStartString(arrayOfFile);
/* 353 */       if ((str != null) && (str.startsWith(this.fileNameCompletionString))) {
/* 354 */         setFileName(str);
/*     */       }
/* 356 */       this.fileNameCompletionString = null;
/*     */     }
/*     */   }
/*     */ 
/*     */   private String getCommonStartString(File[] paramArrayOfFile)
/*     */   {
/* 362 */     Object localObject = null;
/* 363 */     String str1 = null;
/* 364 */     int i = 0;
/* 365 */     if (paramArrayOfFile.length == 0)
/* 366 */       return null;
/*     */     while (true)
/*     */     {
/* 369 */       for (int j = 0; j < paramArrayOfFile.length; j++) {
/* 370 */         String str2 = paramArrayOfFile[j].getName();
/* 371 */         if (j == 0) {
/* 372 */           if (str2.length() == i) {
/* 373 */             return localObject;
/*     */           }
/* 375 */           str1 = str2.substring(0, i + 1);
/*     */         }
/* 377 */         if (!str2.startsWith(str1)) {
/* 378 */           return localObject;
/*     */         }
/*     */       }
/* 381 */       localObject = str1;
/* 382 */       i++;
/*     */     }
/*     */   }
/*     */ 
/*     */   private void resetGlobFilter() {
/* 387 */     if (this.actualFileFilter != null) {
/* 388 */       JFileChooser localJFileChooser = getFileChooser();
/* 389 */       FileFilter localFileFilter = localJFileChooser.getFileFilter();
/* 390 */       if ((localFileFilter != null) && (localFileFilter.equals(this.globFilter))) {
/* 391 */         localJFileChooser.setFileFilter(this.actualFileFilter);
/* 392 */         localJFileChooser.removeChoosableFileFilter(this.globFilter);
/*     */       }
/* 394 */       this.actualFileFilter = null;
/*     */     }
/*     */   }
/*     */ 
/*     */   private static boolean isGlobPattern(String paramString) {
/* 399 */     return ((File.separatorChar == '\\') && (paramString.indexOf('*') >= 0)) || ((File.separatorChar == '/') && ((paramString.indexOf('*') >= 0) || (paramString.indexOf('?') >= 0) || (paramString.indexOf('[') >= 0)));
/*     */   }
/*     */ 
/*     */   public Action getFileNameCompletionAction()
/*     */   {
/* 524 */     return this.fileNameCompletionAction;
/*     */   }
/*     */ 
/*     */   protected JButton getApproveButton(JFileChooser paramJFileChooser)
/*     */   {
/* 529 */     return this.approveButton;
/*     */   }
/*     */ 
/*     */   protected JButton getCancelButton(JFileChooser paramJFileChooser) {
/* 533 */     return this.cancelButton;
/*     */   }
/*     */ 
/*     */   public void clearIconCache()
/*     */   {
/*     */   }
/*     */ 
/*     */   private class DelayedSelectionUpdater
/*     */     implements Runnable
/*     */   {
/*     */     DelayedSelectionUpdater()
/*     */     {
/* 178 */       SwingUtilities.invokeLater(this);
/*     */     }
/*     */ 
/*     */     public void run() {
/* 182 */       SynthFileChooserUI.this.updateFileNameCompletion();
/*     */     }
/*     */   }
/*     */ 
/*     */   private class FileNameCompletionAction extends AbstractAction
/*     */   {
/*     */     protected FileNameCompletionAction()
/*     */     {
/* 308 */       super();
/*     */     }
/*     */ 
/*     */     public void actionPerformed(ActionEvent paramActionEvent) {
/* 312 */       JFileChooser localJFileChooser = SynthFileChooserUI.this.getFileChooser();
/*     */ 
/* 314 */       String str = SynthFileChooserUI.this.getFileName();
/*     */ 
/* 316 */       if (str != null)
/*     */       {
/* 318 */         str = str.trim();
/*     */       }
/*     */ 
/* 321 */       SynthFileChooserUI.this.resetGlobFilter();
/*     */ 
/* 323 */       if ((str == null) || (str.equals("")) || ((localJFileChooser.isMultiSelectionEnabled()) && (str.startsWith("\""))))
/*     */       {
/* 325 */         return;
/*     */       }
/*     */ 
/* 328 */       FileFilter localFileFilter = localJFileChooser.getFileFilter();
/* 329 */       if (SynthFileChooserUI.this.globFilter == null)
/* 330 */         SynthFileChooserUI.this.globFilter = new SynthFileChooserUI.GlobFilter(SynthFileChooserUI.this);
/*     */       try
/*     */       {
/* 333 */         SynthFileChooserUI.this.globFilter.setPattern(!SynthFileChooserUI.isGlobPattern(str) ? str + "*" : str);
/* 334 */         if (!(localFileFilter instanceof SynthFileChooserUI.GlobFilter)) {
/* 335 */           SynthFileChooserUI.this.actualFileFilter = localFileFilter;
/*     */         }
/* 337 */         localJFileChooser.setFileFilter(null);
/* 338 */         localJFileChooser.setFileFilter(SynthFileChooserUI.this.globFilter);
/* 339 */         SynthFileChooserUI.this.fileNameCompletionString = str;
/*     */       }
/*     */       catch (PatternSyntaxException localPatternSyntaxException)
/*     */       {
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   class GlobFilter extends FileFilter
/*     */   {
/*     */     Pattern pattern;
/*     */     String globPattern;
/*     */ 
/*     */     GlobFilter()
/*     */     {
/*     */     }
/*     */ 
/*     */     public void setPattern(String paramString)
/*     */     {
/* 414 */       char[] arrayOfChar1 = paramString.toCharArray();
/* 415 */       char[] arrayOfChar2 = new char[arrayOfChar1.length * 2];
/* 416 */       int i = File.separatorChar == '\\' ? 1 : 0;
/* 417 */       int j = 0;
/* 418 */       int k = 0;
/*     */ 
/* 420 */       this.globPattern = paramString;
/*     */       int m;
/* 422 */       if (i != 0)
/*     */       {
/* 424 */         m = arrayOfChar1.length;
/* 425 */         if (paramString.endsWith("*.*")) {
/* 426 */           m -= 2;
/*     */         }
/* 428 */         for (int n = 0; n < m; n++) {
/* 429 */           if (arrayOfChar1[n] == '*') {
/* 430 */             arrayOfChar2[(k++)] = '.';
/*     */           }
/* 432 */           arrayOfChar2[(k++)] = arrayOfChar1[n];
/*     */         }
/*     */       } else {
/* 435 */         for (m = 0; m < arrayOfChar1.length; m++) {
/* 436 */           switch (arrayOfChar1[m]) {
/*     */           case '*':
/* 438 */             if (j == 0) {
/* 439 */               arrayOfChar2[(k++)] = '.';
/*     */             }
/* 441 */             arrayOfChar2[(k++)] = '*';
/* 442 */             break;
/*     */           case '?':
/* 445 */             arrayOfChar2[(k++)] = (j != 0 ? 63 : '.');
/* 446 */             break;
/*     */           case '[':
/* 449 */             j = 1;
/* 450 */             arrayOfChar2[(k++)] = arrayOfChar1[m];
/*     */ 
/* 452 */             if (m < arrayOfChar1.length - 1)
/* 453 */               switch (arrayOfChar1[(m + 1)]) {
/*     */               case '!':
/*     */               case '^':
/* 456 */                 arrayOfChar2[(k++)] = '^';
/* 457 */                 m++;
/* 458 */                 break;
/*     */               case ']':
/* 461 */                 arrayOfChar2[(k++)] = arrayOfChar1[(++m)];
/*     */               }
/* 462 */             break;
/*     */           case ']':
/* 468 */             arrayOfChar2[(k++)] = arrayOfChar1[m];
/* 469 */             j = 0;
/* 470 */             break;
/*     */           case '\\':
/* 473 */             if ((m == 0) && (arrayOfChar1.length > 1) && (arrayOfChar1[1] == '~')) {
/* 474 */               arrayOfChar2[(k++)] = arrayOfChar1[(++m)];
/*     */             } else {
/* 476 */               arrayOfChar2[(k++)] = '\\';
/* 477 */               if ((m < arrayOfChar1.length - 1) && ("*?[]".indexOf(arrayOfChar1[(m + 1)]) >= 0))
/* 478 */                 arrayOfChar2[(k++)] = arrayOfChar1[(++m)];
/*     */               else {
/* 480 */                 arrayOfChar2[(k++)] = '\\';
/*     */               }
/*     */             }
/* 483 */             break;
/*     */           default:
/* 487 */             if (!Character.isLetterOrDigit(arrayOfChar1[m])) {
/* 488 */               arrayOfChar2[(k++)] = '\\';
/*     */             }
/* 490 */             arrayOfChar2[(k++)] = arrayOfChar1[m];
/*     */           }
/*     */         }
/*     */       }
/*     */ 
/* 495 */       this.pattern = Pattern.compile(new String(arrayOfChar2, 0, k), 2);
/*     */     }
/*     */ 
/*     */     public boolean accept(File paramFile) {
/* 499 */       if (paramFile == null) {
/* 500 */         return false;
/*     */       }
/* 502 */       if (paramFile.isDirectory()) {
/* 503 */         return true;
/*     */       }
/* 505 */       return this.pattern.matcher(paramFile.getName()).matches();
/*     */     }
/*     */ 
/*     */     public String getDescription() {
/* 509 */       return this.globPattern;
/*     */     }
/*     */   }
/*     */ 
/*     */   private class SynthFCPropertyChangeListener
/*     */     implements PropertyChangeListener
/*     */   {
/*     */     private SynthFCPropertyChangeListener()
/*     */     {
/*     */     }
/*     */ 
/*     */     public void propertyChange(PropertyChangeEvent paramPropertyChangeEvent)
/*     */     {
/* 272 */       String str = paramPropertyChangeEvent.getPropertyName();
/* 273 */       if (str.equals("fileSelectionChanged")) {
/* 274 */         SynthFileChooserUI.this.doFileSelectionModeChanged(paramPropertyChangeEvent);
/* 275 */       } else if (str.equals("SelectedFileChangedProperty")) {
/* 276 */         SynthFileChooserUI.this.doSelectedFileChanged(paramPropertyChangeEvent);
/* 277 */       } else if (str.equals("SelectedFilesChangedProperty")) {
/* 278 */         SynthFileChooserUI.this.doSelectedFilesChanged(paramPropertyChangeEvent);
/* 279 */       } else if (str.equals("directoryChanged")) {
/* 280 */         SynthFileChooserUI.this.doDirectoryChanged(paramPropertyChangeEvent);
/* 281 */       } else if (str == "MultiSelectionEnabledChangedProperty") {
/* 282 */         SynthFileChooserUI.this.doMultiSelectionChanged(paramPropertyChangeEvent);
/* 283 */       } else if (str == "AccessoryChangedProperty") {
/* 284 */         SynthFileChooserUI.this.doAccessoryChanged(paramPropertyChangeEvent);
/* 285 */       } else if ((str == "ApproveButtonTextChangedProperty") || (str == "ApproveButtonToolTipTextChangedProperty") || (str == "DialogTypeChangedProperty") || (str == "ControlButtonsAreShownChangedProperty"))
/*     */       {
/* 289 */         SynthFileChooserUI.this.doControlButtonsChanged(paramPropertyChangeEvent);
/* 290 */       } else if (str.equals("componentOrientation")) {
/* 291 */         ComponentOrientation localComponentOrientation = (ComponentOrientation)paramPropertyChangeEvent.getNewValue();
/* 292 */         JFileChooser localJFileChooser = (JFileChooser)paramPropertyChangeEvent.getSource();
/* 293 */         if (localComponentOrientation != (ComponentOrientation)paramPropertyChangeEvent.getOldValue())
/* 294 */           localJFileChooser.applyComponentOrientation(localComponentOrientation);
/*     */       }
/* 296 */       else if (str.equals("ancestor")) {
/* 297 */         SynthFileChooserUI.this.doAncestorChanged(paramPropertyChangeEvent);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private class UIBorder extends AbstractBorder
/*     */     implements UIResource
/*     */   {
/*     */     private Insets _insets;
/*     */ 
/*     */     UIBorder(Insets arg2)
/*     */     {
/*     */       Object localObject;
/* 544 */       if (localObject != null) {
/* 545 */         this._insets = new Insets(localObject.top, localObject.left, localObject.bottom, localObject.right);
/*     */       }
/*     */       else
/*     */       {
/* 549 */         this._insets = null;
/*     */       }
/*     */     }
/*     */ 
/*     */     public void paintBorder(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */     {
/* 555 */       if (!(paramComponent instanceof JComponent)) {
/* 556 */         return;
/*     */       }
/* 558 */       JComponent localJComponent = (JComponent)paramComponent;
/* 559 */       SynthContext localSynthContext = SynthFileChooserUI.this.getContext(localJComponent);
/* 560 */       SynthStyle localSynthStyle = localSynthContext.getStyle();
/* 561 */       if (localSynthStyle != null)
/* 562 */         localSynthStyle.getPainter(localSynthContext).paintFileChooserBorder(localSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*     */     }
/*     */ 
/*     */     public Insets getBorderInsets(Component paramComponent, Insets paramInsets)
/*     */     {
/* 568 */       if (paramInsets == null) {
/* 569 */         paramInsets = new Insets(0, 0, 0, 0);
/*     */       }
/* 571 */       if (this._insets != null) {
/* 572 */         paramInsets.top = this._insets.top;
/* 573 */         paramInsets.bottom = this._insets.bottom;
/* 574 */         paramInsets.left = this._insets.left;
/* 575 */         paramInsets.right = this._insets.right;
/*     */       }
/*     */       else {
/* 578 */         paramInsets.top = (paramInsets.bottom = paramInsets.right = paramInsets.left = 0);
/*     */       }
/* 580 */       return paramInsets;
/*     */     }
/*     */     public boolean isBorderOpaque() {
/* 583 */       return false;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.swing.plaf.synth.SynthFileChooserUI
 * JD-Core Version:    0.6.2
 */