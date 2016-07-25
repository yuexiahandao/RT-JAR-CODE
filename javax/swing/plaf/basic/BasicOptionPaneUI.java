/*      */ package javax.swing.plaf.basic;
/*      */ 
/*      */ import java.awt.BorderLayout;
/*      */ import java.awt.Color;
/*      */ import java.awt.Component;
/*      */ import java.awt.ComponentOrientation;
/*      */ import java.awt.Container;
/*      */ import java.awt.Dimension;
/*      */ import java.awt.Font;
/*      */ import java.awt.GridBagConstraints;
/*      */ import java.awt.GridBagLayout;
/*      */ import java.awt.Insets;
/*      */ import java.awt.LayoutManager;
/*      */ import java.awt.event.ActionEvent;
/*      */ import java.awt.event.ActionListener;
/*      */ import java.awt.event.HierarchyEvent;
/*      */ import java.awt.event.HierarchyListener;
/*      */ import java.awt.event.KeyEvent;
/*      */ import java.awt.event.MouseEvent;
/*      */ import java.awt.event.MouseListener;
/*      */ import java.beans.PropertyChangeEvent;
/*      */ import java.beans.PropertyChangeListener;
/*      */ import java.io.PrintStream;
/*      */ import java.security.AccessController;
/*      */ import java.util.Locale;
/*      */ import javax.swing.Box;
/*      */ import javax.swing.BoxLayout;
/*      */ import javax.swing.Icon;
/*      */ import javax.swing.InputMap;
/*      */ import javax.swing.JButton;
/*      */ import javax.swing.JComboBox;
/*      */ import javax.swing.JComponent;
/*      */ import javax.swing.JLabel;
/*      */ import javax.swing.JList;
/*      */ import javax.swing.JOptionPane;
/*      */ import javax.swing.JPanel;
/*      */ import javax.swing.JRootPane;
/*      */ import javax.swing.JScrollPane;
/*      */ import javax.swing.JTextField;
/*      */ import javax.swing.KeyStroke;
/*      */ import javax.swing.ListModel;
/*      */ import javax.swing.LookAndFeel;
/*      */ import javax.swing.SwingUtilities;
/*      */ import javax.swing.UIManager;
/*      */ import javax.swing.border.Border;
/*      */ import javax.swing.plaf.ComponentUI;
/*      */ import javax.swing.plaf.OptionPaneUI;
/*      */ import sun.security.action.GetPropertyAction;
/*      */ import sun.swing.DefaultLookup;
/*      */ import sun.swing.UIAction;
/*      */ 
/*      */ public class BasicOptionPaneUI extends OptionPaneUI
/*      */ {
/*      */   public static final int MinimumWidth = 262;
/*      */   public static final int MinimumHeight = 90;
/*  112 */   private static String newline = (String)AccessController.doPrivileged(new GetPropertyAction("line.separator"));
/*      */   protected JOptionPane optionPane;
/*      */   protected Dimension minimumSize;
/*      */   protected JComponent inputComponent;
/*      */   protected Component initialFocusComponent;
/*      */   protected boolean hasCustomComponents;
/*      */   protected PropertyChangeListener propertyChangeListener;
/*      */   private Handler handler;
/*      */ 
/*      */   static void loadActionMap(LazyActionMap paramLazyActionMap)
/*      */   {
/*  120 */     paramLazyActionMap.put(new Actions("close"));
/*  121 */     BasicLookAndFeel.installAudioActionMap(paramLazyActionMap);
/*      */   }
/*      */ 
/*      */   public static ComponentUI createUI(JComponent paramJComponent)
/*      */   {
/*  130 */     return new BasicOptionPaneUI();
/*      */   }
/*      */ 
/*      */   public void installUI(JComponent paramJComponent)
/*      */   {
/*  138 */     this.optionPane = ((JOptionPane)paramJComponent);
/*  139 */     installDefaults();
/*  140 */     this.optionPane.setLayout(createLayoutManager());
/*  141 */     installComponents();
/*  142 */     installListeners();
/*  143 */     installKeyboardActions();
/*      */   }
/*      */ 
/*      */   public void uninstallUI(JComponent paramJComponent)
/*      */   {
/*  151 */     uninstallComponents();
/*  152 */     this.optionPane.setLayout(null);
/*  153 */     uninstallKeyboardActions();
/*  154 */     uninstallListeners();
/*  155 */     uninstallDefaults();
/*  156 */     this.optionPane = null;
/*      */   }
/*      */ 
/*      */   protected void installDefaults() {
/*  160 */     LookAndFeel.installColorsAndFont(this.optionPane, "OptionPane.background", "OptionPane.foreground", "OptionPane.font");
/*      */ 
/*  162 */     LookAndFeel.installBorder(this.optionPane, "OptionPane.border");
/*  163 */     this.minimumSize = UIManager.getDimension("OptionPane.minimumSize");
/*  164 */     LookAndFeel.installProperty(this.optionPane, "opaque", Boolean.TRUE);
/*      */   }
/*      */ 
/*      */   protected void uninstallDefaults() {
/*  168 */     LookAndFeel.uninstallBorder(this.optionPane);
/*      */   }
/*      */ 
/*      */   protected void installComponents() {
/*  172 */     this.optionPane.add(createMessageArea());
/*      */ 
/*  174 */     Container localContainer = createSeparator();
/*  175 */     if (localContainer != null) {
/*  176 */       this.optionPane.add(localContainer);
/*      */     }
/*  178 */     this.optionPane.add(createButtonArea());
/*  179 */     this.optionPane.applyComponentOrientation(this.optionPane.getComponentOrientation());
/*      */   }
/*      */ 
/*      */   protected void uninstallComponents() {
/*  183 */     this.hasCustomComponents = false;
/*  184 */     this.inputComponent = null;
/*  185 */     this.initialFocusComponent = null;
/*  186 */     this.optionPane.removeAll();
/*      */   }
/*      */ 
/*      */   protected LayoutManager createLayoutManager() {
/*  190 */     return new BoxLayout(this.optionPane, 1);
/*      */   }
/*      */ 
/*      */   protected void installListeners() {
/*  194 */     if ((this.propertyChangeListener = createPropertyChangeListener()) != null)
/*  195 */       this.optionPane.addPropertyChangeListener(this.propertyChangeListener);
/*      */   }
/*      */ 
/*      */   protected void uninstallListeners()
/*      */   {
/*  200 */     if (this.propertyChangeListener != null) {
/*  201 */       this.optionPane.removePropertyChangeListener(this.propertyChangeListener);
/*  202 */       this.propertyChangeListener = null;
/*      */     }
/*  204 */     this.handler = null;
/*      */   }
/*      */ 
/*      */   protected PropertyChangeListener createPropertyChangeListener() {
/*  208 */     return getHandler();
/*      */   }
/*      */ 
/*      */   private Handler getHandler() {
/*  212 */     if (this.handler == null) {
/*  213 */       this.handler = new Handler(null);
/*      */     }
/*  215 */     return this.handler;
/*      */   }
/*      */ 
/*      */   protected void installKeyboardActions() {
/*  219 */     InputMap localInputMap = getInputMap(2);
/*      */ 
/*  221 */     SwingUtilities.replaceUIInputMap(this.optionPane, 2, localInputMap);
/*      */ 
/*  224 */     LazyActionMap.installLazyActionMap(this.optionPane, BasicOptionPaneUI.class, "OptionPane.actionMap");
/*      */   }
/*      */ 
/*      */   protected void uninstallKeyboardActions()
/*      */   {
/*  229 */     SwingUtilities.replaceUIInputMap(this.optionPane, 2, null);
/*      */ 
/*  231 */     SwingUtilities.replaceUIActionMap(this.optionPane, null);
/*      */   }
/*      */ 
/*      */   InputMap getInputMap(int paramInt) {
/*  235 */     if (paramInt == 2) {
/*  236 */       Object[] arrayOfObject = (Object[])DefaultLookup.get(this.optionPane, this, "OptionPane.windowBindings");
/*      */ 
/*  238 */       if (arrayOfObject != null) {
/*  239 */         return LookAndFeel.makeComponentInputMap(this.optionPane, arrayOfObject);
/*      */       }
/*      */     }
/*  242 */     return null;
/*      */   }
/*      */ 
/*      */   public Dimension getMinimumOptionPaneSize()
/*      */   {
/*  250 */     if (this.minimumSize == null) {
/*  251 */       return new Dimension(262, 90);
/*      */     }
/*  253 */     return new Dimension(this.minimumSize.width, this.minimumSize.height);
/*      */   }
/*      */ 
/*      */   public Dimension getPreferredSize(JComponent paramJComponent)
/*      */   {
/*  265 */     if (paramJComponent == this.optionPane) {
/*  266 */       Dimension localDimension1 = getMinimumOptionPaneSize();
/*  267 */       LayoutManager localLayoutManager = paramJComponent.getLayout();
/*      */ 
/*  269 */       if (localLayoutManager != null) {
/*  270 */         Dimension localDimension2 = localLayoutManager.preferredLayoutSize(paramJComponent);
/*      */ 
/*  272 */         if (localDimension1 != null) {
/*  273 */           return new Dimension(Math.max(localDimension2.width, localDimension1.width), Math.max(localDimension2.height, localDimension1.height));
/*      */         }
/*      */ 
/*  276 */         return localDimension2;
/*      */       }
/*  278 */       return localDimension1;
/*      */     }
/*  280 */     return null;
/*      */   }
/*      */ 
/*      */   protected Container createMessageArea()
/*      */   {
/*  289 */     JPanel localJPanel1 = new JPanel();
/*  290 */     Border localBorder = (Border)DefaultLookup.get(this.optionPane, this, "OptionPane.messageAreaBorder");
/*      */ 
/*  292 */     if (localBorder != null) {
/*  293 */       localJPanel1.setBorder(localBorder);
/*      */     }
/*  295 */     localJPanel1.setLayout(new BorderLayout());
/*      */ 
/*  298 */     JPanel localJPanel2 = new JPanel(new GridBagLayout());
/*  299 */     JPanel localJPanel3 = new JPanel(new BorderLayout());
/*      */ 
/*  301 */     localJPanel2.setName("OptionPane.body");
/*  302 */     localJPanel3.setName("OptionPane.realBody");
/*      */ 
/*  304 */     if (getIcon() != null) {
/*  305 */       localObject = new JPanel();
/*  306 */       ((JPanel)localObject).setName("OptionPane.separator");
/*  307 */       ((JPanel)localObject).setPreferredSize(new Dimension(15, 1));
/*  308 */       localJPanel3.add((Component)localObject, "Before");
/*      */     }
/*  310 */     localJPanel3.add(localJPanel2, "Center");
/*      */ 
/*  312 */     Object localObject = new GridBagConstraints();
/*  313 */     ((GridBagConstraints)localObject).gridx = (((GridBagConstraints)localObject).gridy = 0);
/*  314 */     ((GridBagConstraints)localObject).gridwidth = 0;
/*  315 */     ((GridBagConstraints)localObject).gridheight = 1;
/*  316 */     ((GridBagConstraints)localObject).anchor = DefaultLookup.getInt(this.optionPane, this, "OptionPane.messageAnchor", 10);
/*      */ 
/*  318 */     ((GridBagConstraints)localObject).insets = new Insets(0, 0, 3, 0);
/*      */ 
/*  320 */     addMessageComponents(localJPanel2, (GridBagConstraints)localObject, getMessage(), getMaxCharactersPerLineCount(), false);
/*      */ 
/*  322 */     localJPanel1.add(localJPanel3, "Center");
/*      */ 
/*  324 */     addIcon(localJPanel1);
/*  325 */     return localJPanel1;
/*      */   }
/*      */ 
/*      */   protected void addMessageComponents(Container paramContainer, GridBagConstraints paramGridBagConstraints, Object paramObject, int paramInt, boolean paramBoolean)
/*      */   {
/*  343 */     if (paramObject == null) {
/*  344 */       return;
/*      */     }
/*  346 */     if ((paramObject instanceof Component))
/*      */     {
/*  350 */       if (((paramObject instanceof JScrollPane)) || ((paramObject instanceof JPanel))) {
/*  351 */         paramGridBagConstraints.fill = 1;
/*  352 */         paramGridBagConstraints.weighty = 1.0D;
/*      */       } else {
/*  354 */         paramGridBagConstraints.fill = 2;
/*      */       }
/*  356 */       paramGridBagConstraints.weightx = 1.0D;
/*      */ 
/*  358 */       paramContainer.add((Component)paramObject, paramGridBagConstraints);
/*  359 */       paramGridBagConstraints.weightx = 0.0D;
/*  360 */       paramGridBagConstraints.weighty = 0.0D;
/*  361 */       paramGridBagConstraints.fill = 0;
/*  362 */       paramGridBagConstraints.gridy += 1;
/*  363 */       if (!paramBoolean)
/*  364 */         this.hasCustomComponents = true;
/*      */     }
/*      */     else
/*      */     {
/*      */       Object localObject1;
/*      */       Object localObject3;
/*  367 */       if ((paramObject instanceof Object[])) {
/*  368 */         localObject1 = (Object[])paramObject;
/*  369 */         for (localObject3 : localObject1) {
/*  370 */           addMessageComponents(paramContainer, paramGridBagConstraints, localObject3, paramInt, false);
/*      */         }
/*      */       }
/*  373 */       else if ((paramObject instanceof Icon)) {
/*  374 */         localObject1 = new JLabel((Icon)paramObject, 0);
/*  375 */         configureMessageLabel((JLabel)localObject1);
/*  376 */         addMessageComponents(paramContainer, paramGridBagConstraints, localObject1, paramInt, true);
/*      */       }
/*      */       else {
/*  379 */         localObject1 = paramObject.toString();
/*  380 */         int i = ((String)localObject1).length();
/*  381 */         if (i <= 0) {
/*  382 */           return;
/*      */         }
/*      */ 
/*  385 */         ??? = 0;
/*      */ 
/*  387 */         if (( = ((String)localObject1).indexOf(newline)) >= 0)
/*  388 */           ??? = newline.length();
/*  389 */         else if (( = ((String)localObject1).indexOf("\r\n")) >= 0)
/*  390 */           ??? = 2;
/*  391 */         else if (( = ((String)localObject1).indexOf('\n')) >= 0) {
/*  392 */           ??? = 1;
/*      */         }
/*  394 */         if (??? >= 0)
/*      */         {
/*  396 */           if (??? == 0) {
/*  397 */             localObject3 = new JPanel() {
/*      */               public Dimension getPreferredSize() {
/*  399 */                 Font localFont = getFont();
/*      */ 
/*  401 */                 if (localFont != null) {
/*  402 */                   return new Dimension(1, localFont.getSize() + 2);
/*      */                 }
/*  404 */                 return new Dimension(0, 0);
/*      */               }
/*      */             };
/*  407 */             ((JPanel)localObject3).setName("OptionPane.break");
/*  408 */             addMessageComponents(paramContainer, paramGridBagConstraints, localObject3, paramInt, true);
/*      */           }
/*      */           else {
/*  411 */             addMessageComponents(paramContainer, paramGridBagConstraints, ((String)localObject1).substring(0, ???), paramInt, false);
/*      */           }
/*      */ 
/*  414 */           addMessageComponents(paramContainer, paramGridBagConstraints, ((String)localObject1).substring(??? + ???), paramInt, false);
/*      */         }
/*  417 */         else if (i > paramInt) {
/*  418 */           localObject3 = Box.createVerticalBox();
/*  419 */           ((Container)localObject3).setName("OptionPane.verticalBox");
/*  420 */           burstStringInto((Container)localObject3, (String)localObject1, paramInt);
/*  421 */           addMessageComponents(paramContainer, paramGridBagConstraints, localObject3, paramInt, true);
/*      */         }
/*      */         else
/*      */         {
/*  425 */           localObject3 = new JLabel((String)localObject1, 10);
/*  426 */           ((JLabel)localObject3).setName("OptionPane.label");
/*  427 */           configureMessageLabel((JLabel)localObject3);
/*  428 */           addMessageComponents(paramContainer, paramGridBagConstraints, localObject3, paramInt, true);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   protected Object getMessage()
/*      */   {
/*  438 */     this.inputComponent = null;
/*  439 */     if (this.optionPane != null) {
/*  440 */       if (this.optionPane.getWantsInput())
/*      */       {
/*  445 */         Object localObject1 = this.optionPane.getMessage();
/*  446 */         Object[] arrayOfObject = this.optionPane.getSelectionValues();
/*  447 */         Object localObject2 = this.optionPane.getInitialSelectionValue();
/*      */         Object localObject4;
/*      */         Object localObject3;
/*      */         Object localObject5;
/*  451 */         if (arrayOfObject != null) {
/*  452 */           if (arrayOfObject.length < 20) {
/*  453 */             localObject4 = new JComboBox();
/*      */ 
/*  455 */             ((JComboBox)localObject4).setName("OptionPane.comboBox");
/*  456 */             int i = 0; int j = arrayOfObject.length;
/*  457 */             for (; i < j; i++) {
/*  458 */               ((JComboBox)localObject4).addItem(arrayOfObject[i]);
/*      */             }
/*  460 */             if (localObject2 != null) {
/*  461 */               ((JComboBox)localObject4).setSelectedItem(localObject2);
/*      */             }
/*  463 */             this.inputComponent = ((JComponent)localObject4);
/*  464 */             localObject3 = localObject4;
/*      */           }
/*      */           else {
/*  467 */             localObject4 = new JList(arrayOfObject);
/*  468 */             localObject5 = new JScrollPane((Component)localObject4);
/*      */ 
/*  470 */             ((JScrollPane)localObject5).setName("OptionPane.scrollPane");
/*  471 */             ((JList)localObject4).setName("OptionPane.list");
/*  472 */             ((JList)localObject4).setVisibleRowCount(10);
/*  473 */             ((JList)localObject4).setSelectionMode(0);
/*  474 */             if (localObject2 != null)
/*  475 */               ((JList)localObject4).setSelectedValue(localObject2, true);
/*  476 */             ((JList)localObject4).addMouseListener(getHandler());
/*  477 */             localObject3 = localObject5;
/*  478 */             this.inputComponent = ((JComponent)localObject4);
/*      */           }
/*      */         }
/*      */         else {
/*  482 */           localObject4 = new MultiplexingTextField(20);
/*      */ 
/*  484 */           ((MultiplexingTextField)localObject4).setName("OptionPane.textField");
/*  485 */           ((MultiplexingTextField)localObject4).setKeyStrokes(new KeyStroke[] { KeyStroke.getKeyStroke("ENTER") });
/*      */ 
/*  487 */           if (localObject2 != null) {
/*  488 */             localObject5 = localObject2.toString();
/*  489 */             ((MultiplexingTextField)localObject4).setText((String)localObject5);
/*  490 */             ((MultiplexingTextField)localObject4).setSelectionStart(0);
/*  491 */             ((MultiplexingTextField)localObject4).setSelectionEnd(((String)localObject5).length());
/*      */           }
/*  493 */           ((MultiplexingTextField)localObject4).addActionListener(getHandler());
/*  494 */           localObject3 = this.inputComponent = localObject4;
/*      */         }
/*      */ 
/*  499 */         if (localObject1 == null) {
/*  500 */           localObject4 = new Object[1];
/*  501 */           localObject4[0] = localObject3;
/*      */         }
/*      */         else {
/*  504 */           localObject4 = new Object[2];
/*  505 */           localObject4[0] = localObject1;
/*  506 */           localObject4[1] = localObject3;
/*      */         }
/*  508 */         return localObject4;
/*      */       }
/*  510 */       return this.optionPane.getMessage();
/*      */     }
/*  512 */     return null;
/*      */   }
/*      */ 
/*      */   protected void addIcon(Container paramContainer)
/*      */   {
/*  522 */     Icon localIcon = getIcon();
/*      */ 
/*  524 */     if (localIcon != null) {
/*  525 */       JLabel localJLabel = new JLabel(localIcon);
/*      */ 
/*  527 */       localJLabel.setName("OptionPane.iconLabel");
/*  528 */       localJLabel.setVerticalAlignment(1);
/*  529 */       paramContainer.add(localJLabel, "Before");
/*      */     }
/*      */   }
/*      */ 
/*      */   protected Icon getIcon()
/*      */   {
/*  539 */     Icon localIcon = this.optionPane == null ? null : this.optionPane.getIcon();
/*      */ 
/*  541 */     if ((localIcon == null) && (this.optionPane != null))
/*  542 */       localIcon = getIconForType(this.optionPane.getMessageType());
/*  543 */     return localIcon;
/*      */   }
/*      */ 
/*      */   protected Icon getIconForType(int paramInt)
/*      */   {
/*  550 */     if ((paramInt < 0) || (paramInt > 3))
/*  551 */       return null;
/*  552 */     String str = null;
/*  553 */     switch (paramInt) {
/*      */     case 0:
/*  555 */       str = "OptionPane.errorIcon";
/*  556 */       break;
/*      */     case 1:
/*  558 */       str = "OptionPane.informationIcon";
/*  559 */       break;
/*      */     case 2:
/*  561 */       str = "OptionPane.warningIcon";
/*  562 */       break;
/*      */     case 3:
/*  564 */       str = "OptionPane.questionIcon";
/*      */     }
/*      */ 
/*  567 */     if (str != null) {
/*  568 */       return (Icon)DefaultLookup.get(this.optionPane, this, str);
/*      */     }
/*  570 */     return null;
/*      */   }
/*      */ 
/*      */   protected int getMaxCharactersPerLineCount()
/*      */   {
/*  577 */     return this.optionPane.getMaxCharactersPerLineCount();
/*      */   }
/*      */ 
/*      */   protected void burstStringInto(Container paramContainer, String paramString, int paramInt)
/*      */   {
/*  586 */     int i = paramString.length();
/*  587 */     if (i <= 0)
/*  588 */       return;
/*  589 */     if (i > paramInt) {
/*  590 */       int j = paramString.lastIndexOf(' ', paramInt);
/*  591 */       if (j <= 0)
/*  592 */         j = paramString.indexOf(' ', paramInt);
/*  593 */       if ((j > 0) && (j < i)) {
/*  594 */         burstStringInto(paramContainer, paramString.substring(0, j), paramInt);
/*  595 */         burstStringInto(paramContainer, paramString.substring(j + 1), paramInt);
/*  596 */         return;
/*      */       }
/*      */     }
/*  599 */     JLabel localJLabel = new JLabel(paramString, 2);
/*  600 */     localJLabel.setName("OptionPane.label");
/*  601 */     configureMessageLabel(localJLabel);
/*  602 */     paramContainer.add(localJLabel);
/*      */   }
/*      */ 
/*      */   protected Container createSeparator() {
/*  606 */     return null;
/*      */   }
/*      */ 
/*      */   protected Container createButtonArea()
/*      */   {
/*  614 */     JPanel localJPanel = new JPanel();
/*  615 */     Border localBorder = (Border)DefaultLookup.get(this.optionPane, this, "OptionPane.buttonAreaBorder");
/*      */ 
/*  617 */     localJPanel.setName("OptionPane.buttonArea");
/*  618 */     if (localBorder != null) {
/*  619 */       localJPanel.setBorder(localBorder);
/*      */     }
/*  621 */     localJPanel.setLayout(new ButtonAreaLayout(DefaultLookup.getBoolean(this.optionPane, this, "OptionPane.sameSizeButtons", true), DefaultLookup.getInt(this.optionPane, this, "OptionPane.buttonPadding", 6), DefaultLookup.getInt(this.optionPane, this, "OptionPane.buttonOrientation", 0), DefaultLookup.getBoolean(this.optionPane, this, "OptionPane.isYesLast", false)));
/*      */ 
/*  630 */     addButtonComponents(localJPanel, getButtons(), getInitialValueIndex());
/*  631 */     return localJPanel;
/*      */   }
/*      */ 
/*      */   protected void addButtonComponents(Container paramContainer, Object[] paramArrayOfObject, int paramInt)
/*      */   {
/*  643 */     if ((paramArrayOfObject != null) && (paramArrayOfObject.length > 0)) {
/*  644 */       boolean bool = getSizeButtonsToSameWidth();
/*  645 */       int i = 1;
/*  646 */       int j = paramArrayOfObject.length;
/*  647 */       JButton[] arrayOfJButton = null;
/*  648 */       int k = 0;
/*      */ 
/*  650 */       if (bool) {
/*  651 */         arrayOfJButton = new JButton[j];
/*      */       }
/*      */ 
/*  654 */       for (int m = 0; m < j; m++) {
/*  655 */         Object localObject1 = paramArrayOfObject[m];
/*      */         Object localObject2;
/*      */         JButton localJButton2;
/*  658 */         if ((localObject1 instanceof Component)) {
/*  659 */           i = 0;
/*  660 */           localObject2 = (Component)localObject1;
/*  661 */           paramContainer.add((Component)localObject2);
/*  662 */           this.hasCustomComponents = true;
/*      */         }
/*      */         else
/*      */         {
/*  667 */           if ((localObject1 instanceof ButtonFactory)) {
/*  668 */             localJButton2 = ((ButtonFactory)localObject1).createButton();
/*      */           }
/*  670 */           else if ((localObject1 instanceof Icon))
/*  671 */             localJButton2 = new JButton((Icon)localObject1);
/*      */           else {
/*  673 */             localJButton2 = new JButton(localObject1.toString());
/*      */           }
/*  675 */           localJButton2.setName("OptionPane.button");
/*  676 */           localJButton2.setMultiClickThreshhold(DefaultLookup.getInt(this.optionPane, this, "OptionPane.buttonClickThreshhold", 0));
/*      */ 
/*  679 */           configureButton(localJButton2);
/*      */ 
/*  681 */           paramContainer.add(localJButton2);
/*      */ 
/*  683 */           ActionListener localActionListener = createButtonActionListener(m);
/*  684 */           if (localActionListener != null) {
/*  685 */             localJButton2.addActionListener(localActionListener);
/*      */           }
/*  687 */           localObject2 = localJButton2;
/*      */         }
/*  689 */         if ((bool) && (i != 0) && ((localObject2 instanceof JButton)))
/*      */         {
/*  691 */           arrayOfJButton[m] = ((JButton)localObject2);
/*  692 */           k = Math.max(k, ((Component)localObject2).getMinimumSize().width);
/*      */         }
/*      */ 
/*  695 */         if (m == paramInt) {
/*  696 */           this.initialFocusComponent = ((Component)localObject2);
/*  697 */           if ((this.initialFocusComponent instanceof JButton)) {
/*  698 */             localJButton2 = (JButton)this.initialFocusComponent;
/*  699 */             localJButton2.addHierarchyListener(new HierarchyListener() {
/*      */               public void hierarchyChanged(HierarchyEvent paramAnonymousHierarchyEvent) {
/*  701 */                 if ((paramAnonymousHierarchyEvent.getChangeFlags() & 1L) != 0L)
/*      */                 {
/*  703 */                   JButton localJButton = (JButton)paramAnonymousHierarchyEvent.getComponent();
/*  704 */                   JRootPane localJRootPane = SwingUtilities.getRootPane(localJButton);
/*      */ 
/*  706 */                   if (localJRootPane != null) {
/*  707 */                     localJRootPane.setDefaultButton(localJButton);
/*      */                   }
/*      */                 }
/*      */               }
/*      */             });
/*      */           }
/*      */         }
/*      */       }
/*  715 */       ((ButtonAreaLayout)paramContainer.getLayout()).setSyncAllWidths((bool) && (i != 0));
/*      */ 
/*  720 */       if ((DefaultLookup.getBoolean(this.optionPane, this, "OptionPane.setButtonMargin", true)) && (bool) && (i != 0))
/*      */       {
/*  726 */         int n = j <= 2 ? 8 : 4;
/*      */ 
/*  728 */         for (int i1 = 0; i1 < j; i1++) {
/*  729 */           JButton localJButton1 = arrayOfJButton[i1];
/*  730 */           localJButton1.setMargin(new Insets(2, n, 2, n));
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   protected ActionListener createButtonActionListener(int paramInt) {
/*  737 */     return new ButtonActionListener(paramInt);
/*      */   }
/*      */ 
/*      */   protected Object[] getButtons()
/*      */   {
/*  749 */     if (this.optionPane != null) {
/*  750 */       Object[] arrayOfObject = this.optionPane.getOptions();
/*      */ 
/*  752 */       if (arrayOfObject == null)
/*      */       {
/*  754 */         int i = this.optionPane.getOptionType();
/*  755 */         Locale localLocale = this.optionPane.getLocale();
/*  756 */         int j = DefaultLookup.getInt(this.optionPane, this, "OptionPane.buttonMinimumWidth", -1);
/*      */         ButtonFactory[] arrayOfButtonFactory;
/*  759 */         if (i == 0) {
/*  760 */           arrayOfButtonFactory = new ButtonFactory[2];
/*  761 */           arrayOfButtonFactory[0] = new ButtonFactory(UIManager.getString("OptionPane.yesButtonText", localLocale), getMnemonic("OptionPane.yesButtonMnemonic", localLocale), (Icon)DefaultLookup.get(this.optionPane, this, "OptionPane.yesIcon"), j);
/*      */ 
/*  766 */           arrayOfButtonFactory[1] = new ButtonFactory(UIManager.getString("OptionPane.noButtonText", localLocale), getMnemonic("OptionPane.noButtonMnemonic", localLocale), (Icon)DefaultLookup.get(this.optionPane, this, "OptionPane.noIcon"), j);
/*      */         }
/*  771 */         else if (i == 1) {
/*  772 */           arrayOfButtonFactory = new ButtonFactory[3];
/*  773 */           arrayOfButtonFactory[0] = new ButtonFactory(UIManager.getString("OptionPane.yesButtonText", localLocale), getMnemonic("OptionPane.yesButtonMnemonic", localLocale), (Icon)DefaultLookup.get(this.optionPane, this, "OptionPane.yesIcon"), j);
/*      */ 
/*  778 */           arrayOfButtonFactory[1] = new ButtonFactory(UIManager.getString("OptionPane.noButtonText", localLocale), getMnemonic("OptionPane.noButtonMnemonic", localLocale), (Icon)DefaultLookup.get(this.optionPane, this, "OptionPane.noIcon"), j);
/*      */ 
/*  783 */           arrayOfButtonFactory[2] = new ButtonFactory(UIManager.getString("OptionPane.cancelButtonText", localLocale), getMnemonic("OptionPane.cancelButtonMnemonic", localLocale), (Icon)DefaultLookup.get(this.optionPane, this, "OptionPane.cancelIcon"), j);
/*      */         }
/*  788 */         else if (i == 2) {
/*  789 */           arrayOfButtonFactory = new ButtonFactory[2];
/*  790 */           arrayOfButtonFactory[0] = new ButtonFactory(UIManager.getString("OptionPane.okButtonText", localLocale), getMnemonic("OptionPane.okButtonMnemonic", localLocale), (Icon)DefaultLookup.get(this.optionPane, this, "OptionPane.okIcon"), j);
/*      */ 
/*  795 */           arrayOfButtonFactory[1] = new ButtonFactory(UIManager.getString("OptionPane.cancelButtonText", localLocale), getMnemonic("OptionPane.cancelButtonMnemonic", localLocale), (Icon)DefaultLookup.get(this.optionPane, this, "OptionPane.cancelIcon"), j);
/*      */         }
/*      */         else
/*      */         {
/*  801 */           arrayOfButtonFactory = new ButtonFactory[1];
/*  802 */           arrayOfButtonFactory[0] = new ButtonFactory(UIManager.getString("OptionPane.okButtonText", localLocale), getMnemonic("OptionPane.okButtonMnemonic", localLocale), (Icon)DefaultLookup.get(this.optionPane, this, "OptionPane.okIcon"), j);
/*      */         }
/*      */ 
/*  808 */         return arrayOfButtonFactory;
/*      */       }
/*      */ 
/*  811 */       return arrayOfObject;
/*      */     }
/*  813 */     return null;
/*      */   }
/*      */ 
/*      */   private int getMnemonic(String paramString, Locale paramLocale) {
/*  817 */     String str = (String)UIManager.get(paramString, paramLocale);
/*      */ 
/*  819 */     if (str == null)
/*  820 */       return 0;
/*      */     try
/*      */     {
/*  823 */       return Integer.parseInt(str);
/*      */     } catch (NumberFormatException localNumberFormatException) {
/*      */     }
/*  826 */     return 0;
/*      */   }
/*      */ 
/*      */   protected boolean getSizeButtonsToSameWidth()
/*      */   {
/*  834 */     return true;
/*      */   }
/*      */ 
/*      */   protected int getInitialValueIndex()
/*      */   {
/*  843 */     if (this.optionPane != null) {
/*  844 */       Object localObject = this.optionPane.getInitialValue();
/*  845 */       Object[] arrayOfObject = this.optionPane.getOptions();
/*      */ 
/*  847 */       if (arrayOfObject == null) {
/*  848 */         return 0;
/*      */       }
/*  850 */       if (localObject != null) {
/*  851 */         for (int i = arrayOfObject.length - 1; i >= 0; i--) {
/*  852 */           if (arrayOfObject[i].equals(localObject))
/*  853 */             return i;
/*      */         }
/*      */       }
/*      */     }
/*  857 */     return -1;
/*      */   }
/*      */ 
/*      */   protected void resetInputValue()
/*      */   {
/*  865 */     if ((this.inputComponent != null) && ((this.inputComponent instanceof JTextField))) {
/*  866 */       this.optionPane.setInputValue(((JTextField)this.inputComponent).getText());
/*      */     }
/*  868 */     else if ((this.inputComponent != null) && ((this.inputComponent instanceof JComboBox)))
/*      */     {
/*  870 */       this.optionPane.setInputValue(((JComboBox)this.inputComponent).getSelectedItem());
/*      */     }
/*  872 */     else if (this.inputComponent != null)
/*  873 */       this.optionPane.setInputValue(((JList)this.inputComponent).getSelectedValue());
/*      */   }
/*      */ 
/*      */   public void selectInitialValue(JOptionPane paramJOptionPane)
/*      */   {
/*  884 */     if (this.inputComponent != null) {
/*  885 */       this.inputComponent.requestFocus();
/*      */     } else {
/*  887 */       if (this.initialFocusComponent != null) {
/*  888 */         this.initialFocusComponent.requestFocus();
/*      */       }
/*  890 */       if ((this.initialFocusComponent instanceof JButton)) {
/*  891 */         JRootPane localJRootPane = SwingUtilities.getRootPane(this.initialFocusComponent);
/*  892 */         if (localJRootPane != null)
/*  893 */           localJRootPane.setDefaultButton((JButton)this.initialFocusComponent);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean containsCustomComponents(JOptionPane paramJOptionPane)
/*      */   {
/*  904 */     return this.hasCustomComponents;
/*      */   }
/*      */ 
/*      */   private void configureMessageLabel(JLabel paramJLabel)
/*      */   {
/* 1137 */     Color localColor = (Color)DefaultLookup.get(this.optionPane, this, "OptionPane.messageForeground");
/*      */ 
/* 1139 */     if (localColor != null) {
/* 1140 */       paramJLabel.setForeground(localColor);
/*      */     }
/* 1142 */     Font localFont = (Font)DefaultLookup.get(this.optionPane, this, "OptionPane.messageFont");
/*      */ 
/* 1144 */     if (localFont != null)
/* 1145 */       paramJLabel.setFont(localFont);
/*      */   }
/*      */ 
/*      */   private void configureButton(JButton paramJButton)
/*      */   {
/* 1154 */     Font localFont = (Font)DefaultLookup.get(this.optionPane, this, "OptionPane.buttonFont");
/*      */ 
/* 1156 */     if (localFont != null)
/* 1157 */       paramJButton.setFont(localFont);
/*      */   }
/*      */ 
/*      */   static
/*      */   {
/*  114 */     if (newline == null)
/*  115 */       newline = "\n";
/*      */   }
/*      */ 
/*      */   private static class Actions extends UIAction
/*      */   {
/*      */     private static final String CLOSE = "close";
/*      */ 
/*      */     Actions(String paramString)
/*      */     {
/* 1387 */       super();
/*      */     }
/*      */ 
/*      */     public void actionPerformed(ActionEvent paramActionEvent) {
/* 1391 */       if (getName() == "close") {
/* 1392 */         JOptionPane localJOptionPane = (JOptionPane)paramActionEvent.getSource();
/*      */ 
/* 1394 */         localJOptionPane.setValue(Integer.valueOf(-1));
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public class ButtonActionListener
/*      */     implements ActionListener
/*      */   {
/*      */     protected int buttonIndex;
/*      */ 
/*      */     public ButtonActionListener(int arg2)
/*      */     {
/*      */       int i;
/* 1169 */       this.buttonIndex = i;
/*      */     }
/*      */ 
/*      */     public void actionPerformed(ActionEvent paramActionEvent) {
/* 1173 */       if (BasicOptionPaneUI.this.optionPane != null) {
/* 1174 */         int i = BasicOptionPaneUI.this.optionPane.getOptionType();
/* 1175 */         Object[] arrayOfObject = BasicOptionPaneUI.this.optionPane.getOptions();
/*      */ 
/* 1182 */         if ((BasicOptionPaneUI.this.inputComponent != null) && (
/* 1183 */           (arrayOfObject != null) || (i == -1) || (((i == 0) || (i == 1) || (i == 2)) && (this.buttonIndex == 0))))
/*      */         {
/* 1189 */           BasicOptionPaneUI.this.resetInputValue();
/*      */         }
/*      */ 
/* 1192 */         if (arrayOfObject == null) {
/* 1193 */           if ((i == 2) && (this.buttonIndex == 1))
/*      */           {
/* 1195 */             BasicOptionPaneUI.this.optionPane.setValue(Integer.valueOf(2));
/*      */           }
/*      */           else
/* 1198 */             BasicOptionPaneUI.this.optionPane.setValue(Integer.valueOf(this.buttonIndex));
/*      */         }
/*      */         else
/* 1201 */           BasicOptionPaneUI.this.optionPane.setValue(arrayOfObject[this.buttonIndex]);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class ButtonAreaLayout
/*      */     implements LayoutManager
/*      */   {
/*      */     protected boolean syncAllWidths;
/*      */     protected int padding;
/*      */     protected boolean centersChildren;
/*      */     private int orientation;
/*      */     private boolean reverseButtons;
/*      */     private boolean useOrientation;
/*      */ 
/*      */     public ButtonAreaLayout(boolean paramBoolean, int paramInt)
/*      */     {
/*  932 */       this.syncAllWidths = paramBoolean;
/*  933 */       this.padding = paramInt;
/*  934 */       this.centersChildren = true;
/*  935 */       this.useOrientation = false;
/*      */     }
/*      */ 
/*      */     ButtonAreaLayout(boolean paramBoolean1, int paramInt1, int paramInt2, boolean paramBoolean2)
/*      */     {
/*  940 */       this(paramBoolean1, paramInt1);
/*  941 */       this.useOrientation = true;
/*  942 */       this.orientation = paramInt2;
/*  943 */       this.reverseButtons = paramBoolean2;
/*      */     }
/*      */ 
/*      */     public void setSyncAllWidths(boolean paramBoolean) {
/*  947 */       this.syncAllWidths = paramBoolean;
/*      */     }
/*      */ 
/*      */     public boolean getSyncAllWidths() {
/*  951 */       return this.syncAllWidths;
/*      */     }
/*      */ 
/*      */     public void setPadding(int paramInt) {
/*  955 */       this.padding = paramInt;
/*      */     }
/*      */ 
/*      */     public int getPadding() {
/*  959 */       return this.padding;
/*      */     }
/*      */ 
/*      */     public void setCentersChildren(boolean paramBoolean) {
/*  963 */       this.centersChildren = paramBoolean;
/*  964 */       this.useOrientation = false;
/*      */     }
/*      */ 
/*      */     public boolean getCentersChildren() {
/*  968 */       return this.centersChildren;
/*      */     }
/*      */ 
/*      */     private int getOrientation(Container paramContainer) {
/*  972 */       if (!this.useOrientation) {
/*  973 */         return 0;
/*      */       }
/*  975 */       if (paramContainer.getComponentOrientation().isLeftToRight()) {
/*  976 */         return this.orientation;
/*      */       }
/*  978 */       switch (this.orientation) {
/*      */       case 2:
/*  980 */         return 4;
/*      */       case 4:
/*  982 */         return 2;
/*      */       case 0:
/*  984 */         return 0;
/*      */       case 1:
/*  986 */       case 3: } return 2;
/*      */     }
/*      */ 
/*      */     public void addLayoutComponent(String paramString, Component paramComponent) {
/*      */     }
/*      */ 
/*      */     public void layoutContainer(Container paramContainer) {
/*  993 */       Component[] arrayOfComponent = paramContainer.getComponents();
/*      */ 
/*  995 */       if ((arrayOfComponent != null) && (arrayOfComponent.length > 0)) {
/*  996 */         int i = arrayOfComponent.length;
/*  997 */         Insets localInsets = paramContainer.getInsets();
/*  998 */         int j = 0;
/*  999 */         int k = 0;
/* 1000 */         int m = 0;
/* 1001 */         int n = 0;
/* 1002 */         int i1 = 0;
/* 1003 */         boolean bool = paramContainer.getComponentOrientation().isLeftToRight();
/*      */ 
/* 1005 */         int i2 = !this.reverseButtons ? 1 : bool ? this.reverseButtons : 0;
/*      */         Dimension localDimension2;
/* 1007 */         for (Dimension localDimension1 = 0; localDimension1 < i; localDimension1++) {
/* 1008 */           localDimension2 = arrayOfComponent[localDimension1].getPreferredSize();
/* 1009 */           j = Math.max(j, localDimension2.width);
/* 1010 */           k = Math.max(k, localDimension2.height);
/* 1011 */           m += localDimension2.width;
/*      */         }
/* 1013 */         if (getSyncAllWidths()) {
/* 1014 */           m = j * i;
/*      */         }
/* 1016 */         m += (i - 1) * this.padding;
/*      */ 
/* 1018 */         switch (getOrientation(paramContainer)) {
/*      */         case 2:
/* 1020 */           n = localInsets.left;
/* 1021 */           break;
/*      */         case 4:
/* 1023 */           n = paramContainer.getWidth() - localInsets.right - m;
/* 1024 */           break;
/*      */         case 0:
/* 1026 */           if ((getCentersChildren()) || (i < 2)) {
/* 1027 */             n = (paramContainer.getWidth() - m) / 2;
/*      */           }
/*      */           else {
/* 1030 */             n = localInsets.left;
/* 1031 */             if (getSyncAllWidths()) {
/* 1032 */               i1 = (paramContainer.getWidth() - localInsets.left - localInsets.right - m) / (i - 1) + j;
/*      */             }
/*      */             else
/*      */             {
/* 1037 */               i1 = (paramContainer.getWidth() - localInsets.left - localInsets.right - m) / (i - 1);
/*      */             }
/*      */           }
/*      */           break;
/*      */         case 1:
/*      */         case 3:
/*      */         }
/*      */ 
/* 1045 */         for (localDimension1 = 0; localDimension1 < i; localDimension1++) {
/* 1046 */           localDimension2 = i2 != 0 ? i - localDimension1 - 1 : localDimension1;
/*      */ 
/* 1048 */           Dimension localDimension3 = arrayOfComponent[localDimension2].getPreferredSize();
/*      */ 
/* 1050 */           if (getSyncAllWidths()) {
/* 1051 */             arrayOfComponent[localDimension2].setBounds(n, localInsets.top, j, k);
/*      */           }
/*      */           else
/*      */           {
/* 1055 */             arrayOfComponent[localDimension2].setBounds(n, localInsets.top, localDimension3.width, localDimension3.height);
/*      */           }
/*      */ 
/* 1058 */           if (i1 != 0) {
/* 1059 */             n += i1;
/*      */           }
/*      */           else
/* 1062 */             n += arrayOfComponent[localDimension2].getWidth() + this.padding;
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     public Dimension minimumLayoutSize(Container paramContainer)
/*      */     {
/* 1069 */       if (paramContainer != null) {
/* 1070 */         Component[] arrayOfComponent = paramContainer.getComponents();
/*      */ 
/* 1072 */         if ((arrayOfComponent != null) && (arrayOfComponent.length > 0))
/*      */         {
/* 1074 */           int i = arrayOfComponent.length;
/* 1075 */           int j = 0;
/* 1076 */           Insets localInsets = paramContainer.getInsets();
/* 1077 */           int k = localInsets.top + localInsets.bottom;
/* 1078 */           int m = localInsets.left + localInsets.right;
/*      */           Dimension localDimension;
/* 1080 */           if (this.syncAllWidths) {
/* 1081 */             n = 0;
/*      */ 
/* 1083 */             for (i1 = 0; i1 < i; i1++) {
/* 1084 */               localDimension = arrayOfComponent[i1].getPreferredSize();
/* 1085 */               j = Math.max(j, localDimension.height);
/* 1086 */               n = Math.max(n, localDimension.width);
/*      */             }
/* 1088 */             return new Dimension(m + n * i + (i - 1) * this.padding, k + j);
/*      */           }
/*      */ 
/* 1093 */           int n = 0;
/*      */ 
/* 1095 */           for (int i1 = 0; i1 < i; i1++) {
/* 1096 */             localDimension = arrayOfComponent[i1].getPreferredSize();
/* 1097 */             j = Math.max(j, localDimension.height);
/* 1098 */             n += localDimension.width;
/*      */           }
/* 1100 */           n += (i - 1) * this.padding;
/* 1101 */           return new Dimension(m + n, k + j);
/*      */         }
/*      */       }
/*      */ 
/* 1105 */       return new Dimension(0, 0);
/*      */     }
/*      */ 
/*      */     public Dimension preferredLayoutSize(Container paramContainer) {
/* 1109 */       return minimumLayoutSize(paramContainer);
/*      */     }
/*      */ 
/*      */     public void removeLayoutComponent(Component paramComponent)
/*      */     {
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class ButtonFactory
/*      */   {
/*      */     private String text;
/*      */     private int mnemonic;
/*      */     private Icon icon;
/* 1409 */     private int minimumWidth = -1;
/*      */ 
/*      */     ButtonFactory(String paramString, int paramInt1, Icon paramIcon, int paramInt2) {
/* 1412 */       this.text = paramString;
/* 1413 */       this.mnemonic = paramInt1;
/* 1414 */       this.icon = paramIcon;
/* 1415 */       this.minimumWidth = paramInt2;
/*      */     }
/*      */ 
/*      */     JButton createButton()
/*      */     {
/*      */       Object localObject;
/* 1421 */       if (this.minimumWidth > 0)
/* 1422 */         localObject = new ConstrainedButton(this.text, this.minimumWidth);
/*      */       else {
/* 1424 */         localObject = new JButton(this.text);
/*      */       }
/* 1426 */       if (this.icon != null) {
/* 1427 */         ((JButton)localObject).setIcon(this.icon);
/*      */       }
/* 1429 */       if (this.mnemonic != 0) {
/* 1430 */         ((JButton)localObject).setMnemonic(this.mnemonic);
/*      */       }
/* 1432 */       return localObject;
/*      */     }
/*      */ 
/*      */     private static class ConstrainedButton extends JButton {
/*      */       int minimumWidth;
/*      */ 
/*      */       ConstrainedButton(String paramString, int paramInt) {
/* 1439 */         super();
/* 1440 */         this.minimumWidth = paramInt;
/*      */       }
/*      */ 
/*      */       public Dimension getMinimumSize() {
/* 1444 */         Dimension localDimension = super.getMinimumSize();
/* 1445 */         localDimension.width = Math.max(localDimension.width, this.minimumWidth);
/* 1446 */         return localDimension;
/*      */       }
/*      */ 
/*      */       public Dimension getPreferredSize() {
/* 1450 */         Dimension localDimension = super.getPreferredSize();
/* 1451 */         localDimension.width = Math.max(localDimension.width, this.minimumWidth);
/* 1452 */         return localDimension;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private class Handler
/*      */     implements ActionListener, MouseListener, PropertyChangeListener
/*      */   {
/*      */     private Handler()
/*      */     {
/*      */     }
/*      */ 
/*      */     public void actionPerformed(ActionEvent paramActionEvent)
/*      */     {
/* 1214 */       BasicOptionPaneUI.this.optionPane.setInputValue(((JTextField)paramActionEvent.getSource()).getText());
/*      */     }
/*      */ 
/*      */     public void mouseClicked(MouseEvent paramMouseEvent)
/*      */     {
/*      */     }
/*      */ 
/*      */     public void mouseReleased(MouseEvent paramMouseEvent)
/*      */     {
/*      */     }
/*      */ 
/*      */     public void mouseEntered(MouseEvent paramMouseEvent)
/*      */     {
/*      */     }
/*      */ 
/*      */     public void mouseExited(MouseEvent paramMouseEvent)
/*      */     {
/*      */     }
/*      */ 
/*      */     public void mousePressed(MouseEvent paramMouseEvent) {
/* 1234 */       if (paramMouseEvent.getClickCount() == 2) {
/* 1235 */         JList localJList = (JList)paramMouseEvent.getSource();
/* 1236 */         int i = localJList.locationToIndex(paramMouseEvent.getPoint());
/*      */ 
/* 1238 */         BasicOptionPaneUI.this.optionPane.setInputValue(localJList.getModel().getElementAt(i));
/* 1239 */         BasicOptionPaneUI.this.optionPane.setValue(Integer.valueOf(0));
/*      */       }
/*      */     }
/*      */ 
/*      */     public void propertyChange(PropertyChangeEvent paramPropertyChangeEvent)
/*      */     {
/* 1247 */       if (paramPropertyChangeEvent.getSource() == BasicOptionPaneUI.this.optionPane)
/*      */       {
/* 1252 */         if ("ancestor" == paramPropertyChangeEvent.getPropertyName()) {
/* 1253 */           localObject = (JOptionPane)paramPropertyChangeEvent.getSource();
/*      */           int i;
/* 1258 */           if (paramPropertyChangeEvent.getOldValue() == null)
/* 1259 */             i = 1;
/*      */           else {
/* 1261 */             i = 0;
/*      */           }
/*      */ 
/* 1265 */           switch (((JOptionPane)localObject).getMessageType()) {
/*      */           case -1:
/* 1267 */             if (i != 0)
/* 1268 */               BasicLookAndFeel.playSound(BasicOptionPaneUI.this.optionPane, "OptionPane.informationSound"); break;
/*      */           case 3:
/* 1273 */             if (i != 0)
/* 1274 */               BasicLookAndFeel.playSound(BasicOptionPaneUI.this.optionPane, "OptionPane.questionSound"); break;
/*      */           case 1:
/* 1279 */             if (i != 0)
/* 1280 */               BasicLookAndFeel.playSound(BasicOptionPaneUI.this.optionPane, "OptionPane.informationSound"); break;
/*      */           case 2:
/* 1285 */             if (i != 0)
/* 1286 */               BasicLookAndFeel.playSound(BasicOptionPaneUI.this.optionPane, "OptionPane.warningSound"); break;
/*      */           case 0:
/* 1291 */             if (i != 0)
/* 1292 */               BasicLookAndFeel.playSound(BasicOptionPaneUI.this.optionPane, "OptionPane.errorSound"); break;
/*      */           default:
/* 1297 */             System.err.println("Undefined JOptionPane type: " + ((JOptionPane)localObject).getMessageType());
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/* 1303 */         Object localObject = paramPropertyChangeEvent.getPropertyName();
/*      */ 
/* 1305 */         if ((localObject == "options") || (localObject == "initialValue") || (localObject == "icon") || (localObject == "messageType") || (localObject == "optionType") || (localObject == "message") || (localObject == "selectionValues") || (localObject == "initialSelectionValue") || (localObject == "wantsInput"))
/*      */         {
/* 1314 */           BasicOptionPaneUI.this.uninstallComponents();
/* 1315 */           BasicOptionPaneUI.this.installComponents();
/* 1316 */           BasicOptionPaneUI.this.optionPane.validate();
/*      */         }
/* 1318 */         else if (localObject == "componentOrientation") {
/* 1319 */           ComponentOrientation localComponentOrientation = (ComponentOrientation)paramPropertyChangeEvent.getNewValue();
/* 1320 */           JOptionPane localJOptionPane = (JOptionPane)paramPropertyChangeEvent.getSource();
/* 1321 */           if (localComponentOrientation != paramPropertyChangeEvent.getOldValue())
/* 1322 */             localJOptionPane.applyComponentOrientation(localComponentOrientation);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class MultiplexingTextField extends JTextField
/*      */   {
/*      */     private KeyStroke[] strokes;
/*      */ 
/*      */     MultiplexingTextField(int paramInt)
/*      */     {
/* 1346 */       super();
/*      */     }
/*      */ 
/*      */     void setKeyStrokes(KeyStroke[] paramArrayOfKeyStroke)
/*      */     {
/* 1354 */       this.strokes = paramArrayOfKeyStroke;
/*      */     }
/*      */ 
/*      */     protected boolean processKeyBinding(KeyStroke paramKeyStroke, KeyEvent paramKeyEvent, int paramInt, boolean paramBoolean)
/*      */     {
/* 1359 */       boolean bool = super.processKeyBinding(paramKeyStroke, paramKeyEvent, paramInt, paramBoolean);
/*      */ 
/* 1362 */       if ((bool) && (paramInt != 2)) {
/* 1363 */         for (int i = this.strokes.length - 1; i >= 0; 
/* 1364 */           i--) {
/* 1365 */           if (this.strokes[i].equals(paramKeyStroke))
/*      */           {
/* 1369 */             return false;
/*      */           }
/*      */         }
/*      */       }
/* 1373 */       return bool;
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
/* 1128 */       BasicOptionPaneUI.this.getHandler().propertyChange(paramPropertyChangeEvent);
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.basic.BasicOptionPaneUI
 * JD-Core Version:    0.6.2
 */