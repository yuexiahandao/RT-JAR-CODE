/*      */ package javax.swing.plaf.metal;
/*      */ 
/*      */ import java.awt.Component;
/*      */ import java.awt.Container;
/*      */ import java.awt.Frame;
/*      */ import java.awt.Insets;
/*      */ import java.awt.Toolkit;
/*      */ import java.awt.Window;
/*      */ import java.beans.PropertyChangeEvent;
/*      */ import java.beans.PropertyChangeListener;
/*      */ import java.lang.ref.ReferenceQueue;
/*      */ import java.lang.ref.WeakReference;
/*      */ import java.lang.reflect.InvocationTargetException;
/*      */ import java.lang.reflect.Method;
/*      */ import java.security.AccessController;
/*      */ import java.security.PrivilegedAction;
/*      */ import javax.swing.ButtonModel;
/*      */ import javax.swing.DefaultButtonModel;
/*      */ import javax.swing.Icon;
/*      */ import javax.swing.ImageIcon;
/*      */ import javax.swing.JComponent;
/*      */ import javax.swing.JToggleButton;
/*      */ import javax.swing.LayoutStyle;
/*      */ import javax.swing.LayoutStyle.ComponentPlacement;
/*      */ import javax.swing.LookAndFeel;
/*      */ import javax.swing.SwingUtilities;
/*      */ import javax.swing.UIDefaults;
/*      */ import javax.swing.UIDefaults.ActiveValue;
/*      */ import javax.swing.UIDefaults.LazyInputMap;
/*      */ import javax.swing.UIDefaults.LazyValue;
/*      */ import javax.swing.UIManager;
/*      */ import javax.swing.plaf.ColorUIResource;
/*      */ import javax.swing.plaf.FontUIResource;
/*      */ import javax.swing.plaf.InsetsUIResource;
/*      */ import javax.swing.plaf.UIResource;
/*      */ import javax.swing.plaf.basic.BasicLookAndFeel;
/*      */ import sun.awt.AppContext;
/*      */ import sun.awt.OSInfo;
/*      */ import sun.awt.OSInfo.OSType;
/*      */ import sun.security.action.GetPropertyAction;
/*      */ import sun.swing.DefaultLayoutStyle;
/*      */ import sun.swing.SwingLazyValue;
/*      */ import sun.swing.SwingUtilities2;
/*      */ import sun.swing.SwingUtilities2.AATextInfo;
/*      */ 
/*      */ public class MetalLookAndFeel extends BasicLookAndFeel
/*      */ {
/*   88 */   private static boolean METAL_LOOK_AND_FEEL_INITED = false;
/*      */   private static boolean checkedWindows;
/*      */   private static boolean isWindows;
/*      */   private static boolean checkedSystemFontSettings;
/*      */   private static boolean useSystemFonts;
/* 2271 */   static ReferenceQueue<LookAndFeel> queue = new ReferenceQueue();
/*      */ 
/*      */   static boolean isWindows()
/*      */   {
/*  116 */     if (!checkedWindows) {
/*  117 */       OSInfo.OSType localOSType = (OSInfo.OSType)AccessController.doPrivileged(OSInfo.getOSTypeAction());
/*  118 */       if (localOSType == OSInfo.OSType.WINDOWS) {
/*  119 */         isWindows = true;
/*  120 */         String str = (String)AccessController.doPrivileged(new GetPropertyAction("swing.useSystemFontSettings"));
/*      */ 
/*  122 */         useSystemFonts = (str != null) && (Boolean.valueOf(str).booleanValue());
/*      */       }
/*      */ 
/*  125 */       checkedWindows = true;
/*      */     }
/*  127 */     return isWindows;
/*      */   }
/*      */ 
/*      */   static boolean useSystemFonts()
/*      */   {
/*  135 */     if ((isWindows()) && (useSystemFonts)) {
/*  136 */       if (METAL_LOOK_AND_FEEL_INITED) {
/*  137 */         Object localObject = UIManager.get("Application.useSystemFontSettings");
/*      */ 
/*  140 */         return (localObject == null) || (Boolean.TRUE.equals(localObject));
/*      */       }
/*      */ 
/*  147 */       return true;
/*      */     }
/*  149 */     return false;
/*      */   }
/*      */ 
/*      */   private static boolean useHighContrastTheme()
/*      */   {
/*  157 */     if ((isWindows()) && (useSystemFonts())) {
/*  158 */       Boolean localBoolean = (Boolean)Toolkit.getDefaultToolkit().getDesktopProperty("win.highContrast.on");
/*      */ 
/*  161 */       return localBoolean == null ? false : localBoolean.booleanValue();
/*      */     }
/*      */ 
/*  164 */     return false;
/*      */   }
/*      */ 
/*      */   static boolean usingOcean()
/*      */   {
/*  171 */     return getCurrentTheme() instanceof OceanTheme;
/*      */   }
/*      */ 
/*      */   public String getName()
/*      */   {
/*  181 */     return "Metal";
/*      */   }
/*      */ 
/*      */   public String getID()
/*      */   {
/*  191 */     return "Metal";
/*      */   }
/*      */ 
/*      */   public String getDescription()
/*      */   {
/*  201 */     return "The Java(tm) Look and Feel";
/*      */   }
/*      */ 
/*      */   public boolean isNativeLookAndFeel()
/*      */   {
/*  211 */     return false;
/*      */   }
/*      */ 
/*      */   public boolean isSupportedLookAndFeel()
/*      */   {
/*  221 */     return true;
/*      */   }
/*      */ 
/*      */   public boolean getSupportsWindowDecorations()
/*      */   {
/*  236 */     return true;
/*      */   }
/*      */ 
/*      */   protected void initClassDefaults(UIDefaults paramUIDefaults)
/*      */   {
/*  259 */     super.initClassDefaults(paramUIDefaults);
/*      */ 
/*  262 */     Object[] arrayOfObject = { "ButtonUI", "javax.swing.plaf.metal.MetalButtonUI", "CheckBoxUI", "javax.swing.plaf.metal.MetalCheckBoxUI", "ComboBoxUI", "javax.swing.plaf.metal.MetalComboBoxUI", "DesktopIconUI", "javax.swing.plaf.metal.MetalDesktopIconUI", "FileChooserUI", "javax.swing.plaf.metal.MetalFileChooserUI", "InternalFrameUI", "javax.swing.plaf.metal.MetalInternalFrameUI", "LabelUI", "javax.swing.plaf.metal.MetalLabelUI", "PopupMenuSeparatorUI", "javax.swing.plaf.metal.MetalPopupMenuSeparatorUI", "ProgressBarUI", "javax.swing.plaf.metal.MetalProgressBarUI", "RadioButtonUI", "javax.swing.plaf.metal.MetalRadioButtonUI", "ScrollBarUI", "javax.swing.plaf.metal.MetalScrollBarUI", "ScrollPaneUI", "javax.swing.plaf.metal.MetalScrollPaneUI", "SeparatorUI", "javax.swing.plaf.metal.MetalSeparatorUI", "SliderUI", "javax.swing.plaf.metal.MetalSliderUI", "SplitPaneUI", "javax.swing.plaf.metal.MetalSplitPaneUI", "TabbedPaneUI", "javax.swing.plaf.metal.MetalTabbedPaneUI", "TextFieldUI", "javax.swing.plaf.metal.MetalTextFieldUI", "ToggleButtonUI", "javax.swing.plaf.metal.MetalToggleButtonUI", "ToolBarUI", "javax.swing.plaf.metal.MetalToolBarUI", "ToolTipUI", "javax.swing.plaf.metal.MetalToolTipUI", "TreeUI", "javax.swing.plaf.metal.MetalTreeUI", "RootPaneUI", "javax.swing.plaf.metal.MetalRootPaneUI" };
/*      */ 
/*  287 */     paramUIDefaults.putDefaults(arrayOfObject);
/*      */   }
/*      */ 
/*      */   protected void initSystemColorDefaults(UIDefaults paramUIDefaults)
/*      */   {
/*  385 */     MetalTheme localMetalTheme = getCurrentTheme();
/*  386 */     ColorUIResource localColorUIResource = localMetalTheme.getControl();
/*  387 */     Object[] arrayOfObject = { "desktop", localMetalTheme.getDesktopColor(), "activeCaption", localMetalTheme.getWindowTitleBackground(), "activeCaptionText", localMetalTheme.getWindowTitleForeground(), "activeCaptionBorder", localMetalTheme.getPrimaryControlShadow(), "inactiveCaption", localMetalTheme.getWindowTitleInactiveBackground(), "inactiveCaptionText", localMetalTheme.getWindowTitleInactiveForeground(), "inactiveCaptionBorder", localMetalTheme.getControlShadow(), "window", localMetalTheme.getWindowBackground(), "windowBorder", localColorUIResource, "windowText", localMetalTheme.getUserTextColor(), "menu", localMetalTheme.getMenuBackground(), "menuText", localMetalTheme.getMenuForeground(), "text", localMetalTheme.getWindowBackground(), "textText", localMetalTheme.getUserTextColor(), "textHighlight", localMetalTheme.getTextHighlightColor(), "textHighlightText", localMetalTheme.getHighlightedTextColor(), "textInactiveText", localMetalTheme.getInactiveSystemTextColor(), "control", localColorUIResource, "controlText", localMetalTheme.getControlTextColor(), "controlHighlight", localMetalTheme.getControlHighlight(), "controlLtHighlight", localMetalTheme.getControlHighlight(), "controlShadow", localMetalTheme.getControlShadow(), "controlDkShadow", localMetalTheme.getControlDarkShadow(), "scrollbar", localColorUIResource, "info", localMetalTheme.getPrimaryControl(), "infoText", localMetalTheme.getPrimaryControlInfo() };
/*      */ 
/*  416 */     paramUIDefaults.putDefaults(arrayOfObject);
/*      */   }
/*      */ 
/*      */   private void initResourceBundle(UIDefaults paramUIDefaults)
/*      */   {
/*  424 */     paramUIDefaults.addResourceBundle("com.sun.swing.internal.plaf.metal.resources.metal");
/*      */   }
/*      */ 
/*      */   protected void initComponentDefaults(UIDefaults paramUIDefaults)
/*      */   {
/*  434 */     super.initComponentDefaults(paramUIDefaults);
/*      */ 
/*  436 */     initResourceBundle(paramUIDefaults);
/*      */ 
/*  438 */     ColorUIResource localColorUIResource1 = getAcceleratorForeground();
/*  439 */     ColorUIResource localColorUIResource2 = getAcceleratorSelectedForeground();
/*  440 */     ColorUIResource localColorUIResource3 = getControl();
/*  441 */     ColorUIResource localColorUIResource4 = getControlHighlight();
/*  442 */     ColorUIResource localColorUIResource5 = getControlShadow();
/*  443 */     ColorUIResource localColorUIResource6 = getControlDarkShadow();
/*  444 */     ColorUIResource localColorUIResource7 = getControlTextColor();
/*  445 */     ColorUIResource localColorUIResource8 = getFocusColor();
/*  446 */     ColorUIResource localColorUIResource9 = getInactiveControlTextColor();
/*  447 */     ColorUIResource localColorUIResource10 = getMenuBackground();
/*  448 */     ColorUIResource localColorUIResource11 = getMenuSelectedBackground();
/*  449 */     ColorUIResource localColorUIResource12 = getMenuDisabledForeground();
/*  450 */     ColorUIResource localColorUIResource13 = getMenuSelectedForeground();
/*  451 */     ColorUIResource localColorUIResource14 = getPrimaryControl();
/*  452 */     ColorUIResource localColorUIResource15 = getPrimaryControlDarkShadow();
/*  453 */     ColorUIResource localColorUIResource16 = getPrimaryControlShadow();
/*  454 */     ColorUIResource localColorUIResource17 = getSystemTextColor();
/*      */ 
/*  456 */     InsetsUIResource localInsetsUIResource1 = new InsetsUIResource(0, 0, 0, 0);
/*      */ 
/*  458 */     Integer localInteger = Integer.valueOf(0);
/*      */ 
/*  460 */     SwingLazyValue localSwingLazyValue1 = new SwingLazyValue("javax.swing.plaf.metal.MetalBorders", "getTextFieldBorder");
/*      */ 
/*  464 */     MetalLazyValue localMetalLazyValue1 = new MetalLazyValue("javax.swing.plaf.metal.MetalBorders$DialogBorder");
/*      */ 
/*  467 */     MetalLazyValue localMetalLazyValue2 = new MetalLazyValue("javax.swing.plaf.metal.MetalBorders$QuestionDialogBorder");
/*      */ 
/*  470 */     UIDefaults.LazyInputMap localLazyInputMap1 = new UIDefaults.LazyInputMap(new Object[] { "ctrl C", "copy-to-clipboard", "ctrl V", "paste-from-clipboard", "ctrl X", "cut-to-clipboard", "COPY", "copy-to-clipboard", "PASTE", "paste-from-clipboard", "CUT", "cut-to-clipboard", "control INSERT", "copy-to-clipboard", "shift INSERT", "paste-from-clipboard", "shift DELETE", "cut-to-clipboard", "shift LEFT", "selection-backward", "shift KP_LEFT", "selection-backward", "shift RIGHT", "selection-forward", "shift KP_RIGHT", "selection-forward", "ctrl LEFT", "caret-previous-word", "ctrl KP_LEFT", "caret-previous-word", "ctrl RIGHT", "caret-next-word", "ctrl KP_RIGHT", "caret-next-word", "ctrl shift LEFT", "selection-previous-word", "ctrl shift KP_LEFT", "selection-previous-word", "ctrl shift RIGHT", "selection-next-word", "ctrl shift KP_RIGHT", "selection-next-word", "ctrl A", "select-all", "HOME", "caret-begin-line", "END", "caret-end-line", "shift HOME", "selection-begin-line", "shift END", "selection-end-line", "BACK_SPACE", "delete-previous", "shift BACK_SPACE", "delete-previous", "ctrl H", "delete-previous", "DELETE", "delete-next", "ctrl DELETE", "delete-next-word", "ctrl BACK_SPACE", "delete-previous-word", "RIGHT", "caret-forward", "LEFT", "caret-backward", "KP_RIGHT", "caret-forward", "KP_LEFT", "caret-backward", "ENTER", "notify-field-accept", "ctrl BACK_SLASH", "unselect", "control shift O", "toggle-componentOrientation" });
/*      */ 
/*  512 */     UIDefaults.LazyInputMap localLazyInputMap2 = new UIDefaults.LazyInputMap(new Object[] { "ctrl C", "copy-to-clipboard", "ctrl V", "paste-from-clipboard", "ctrl X", "cut-to-clipboard", "COPY", "copy-to-clipboard", "PASTE", "paste-from-clipboard", "CUT", "cut-to-clipboard", "control INSERT", "copy-to-clipboard", "shift INSERT", "paste-from-clipboard", "shift DELETE", "cut-to-clipboard", "shift LEFT", "selection-backward", "shift KP_LEFT", "selection-backward", "shift RIGHT", "selection-forward", "shift KP_RIGHT", "selection-forward", "ctrl LEFT", "caret-begin-line", "ctrl KP_LEFT", "caret-begin-line", "ctrl RIGHT", "caret-end-line", "ctrl KP_RIGHT", "caret-end-line", "ctrl shift LEFT", "selection-begin-line", "ctrl shift KP_LEFT", "selection-begin-line", "ctrl shift RIGHT", "selection-end-line", "ctrl shift KP_RIGHT", "selection-end-line", "ctrl A", "select-all", "HOME", "caret-begin-line", "END", "caret-end-line", "shift HOME", "selection-begin-line", "shift END", "selection-end-line", "BACK_SPACE", "delete-previous", "shift BACK_SPACE", "delete-previous", "ctrl H", "delete-previous", "DELETE", "delete-next", "RIGHT", "caret-forward", "LEFT", "caret-backward", "KP_RIGHT", "caret-forward", "KP_LEFT", "caret-backward", "ENTER", "notify-field-accept", "ctrl BACK_SLASH", "unselect", "control shift O", "toggle-componentOrientation" });
/*      */ 
/*  552 */     UIDefaults.LazyInputMap localLazyInputMap3 = new UIDefaults.LazyInputMap(new Object[] { "ctrl C", "copy-to-clipboard", "ctrl V", "paste-from-clipboard", "ctrl X", "cut-to-clipboard", "COPY", "copy-to-clipboard", "PASTE", "paste-from-clipboard", "CUT", "cut-to-clipboard", "control INSERT", "copy-to-clipboard", "shift INSERT", "paste-from-clipboard", "shift DELETE", "cut-to-clipboard", "shift LEFT", "selection-backward", "shift KP_LEFT", "selection-backward", "shift RIGHT", "selection-forward", "shift KP_RIGHT", "selection-forward", "ctrl LEFT", "caret-previous-word", "ctrl KP_LEFT", "caret-previous-word", "ctrl RIGHT", "caret-next-word", "ctrl KP_RIGHT", "caret-next-word", "ctrl shift LEFT", "selection-previous-word", "ctrl shift KP_LEFT", "selection-previous-word", "ctrl shift RIGHT", "selection-next-word", "ctrl shift KP_RIGHT", "selection-next-word", "ctrl A", "select-all", "HOME", "caret-begin-line", "END", "caret-end-line", "shift HOME", "selection-begin-line", "shift END", "selection-end-line", "UP", "caret-up", "KP_UP", "caret-up", "DOWN", "caret-down", "KP_DOWN", "caret-down", "PAGE_UP", "page-up", "PAGE_DOWN", "page-down", "shift PAGE_UP", "selection-page-up", "shift PAGE_DOWN", "selection-page-down", "ctrl shift PAGE_UP", "selection-page-left", "ctrl shift PAGE_DOWN", "selection-page-right", "shift UP", "selection-up", "shift KP_UP", "selection-up", "shift DOWN", "selection-down", "shift KP_DOWN", "selection-down", "ENTER", "insert-break", "BACK_SPACE", "delete-previous", "shift BACK_SPACE", "delete-previous", "ctrl H", "delete-previous", "DELETE", "delete-next", "ctrl DELETE", "delete-next-word", "ctrl BACK_SPACE", "delete-previous-word", "RIGHT", "caret-forward", "LEFT", "caret-backward", "KP_RIGHT", "caret-forward", "KP_LEFT", "caret-backward", "TAB", "insert-tab", "ctrl BACK_SLASH", "unselect", "ctrl HOME", "caret-begin", "ctrl END", "caret-end", "ctrl shift HOME", "selection-begin", "ctrl shift END", "selection-end", "ctrl T", "next-link-action", "ctrl shift T", "previous-link-action", "ctrl SPACE", "activate-link-action", "control shift O", "toggle-componentOrientation" });
/*      */ 
/*  617 */     SwingLazyValue localSwingLazyValue2 = new SwingLazyValue("javax.swing.plaf.metal.MetalBorders$ScrollPaneBorder");
/*  618 */     SwingLazyValue localSwingLazyValue3 = new SwingLazyValue("javax.swing.plaf.metal.MetalBorders", "getButtonBorder");
/*      */ 
/*  622 */     SwingLazyValue localSwingLazyValue4 = new SwingLazyValue("javax.swing.plaf.metal.MetalBorders", "getToggleButtonBorder");
/*      */ 
/*  626 */     SwingLazyValue localSwingLazyValue5 = new SwingLazyValue("javax.swing.plaf.BorderUIResource$LineBorderUIResource", new Object[] { localColorUIResource5 });
/*      */ 
/*  631 */     SwingLazyValue localSwingLazyValue6 = new SwingLazyValue("javax.swing.plaf.metal.MetalBorders", "getDesktopIconBorder");
/*      */ 
/*  636 */     SwingLazyValue localSwingLazyValue7 = new SwingLazyValue("javax.swing.plaf.metal.MetalBorders$MenuBarBorder");
/*      */ 
/*  640 */     SwingLazyValue localSwingLazyValue8 = new SwingLazyValue("javax.swing.plaf.metal.MetalBorders$PopupMenuBorder");
/*      */ 
/*  643 */     SwingLazyValue localSwingLazyValue9 = new SwingLazyValue("javax.swing.plaf.metal.MetalBorders$MenuItemBorder");
/*      */ 
/*  647 */     String str = "-";
/*  648 */     SwingLazyValue localSwingLazyValue10 = new SwingLazyValue("javax.swing.plaf.metal.MetalBorders$ToolBarBorder");
/*      */ 
/*  650 */     SwingLazyValue localSwingLazyValue11 = new SwingLazyValue("javax.swing.plaf.BorderUIResource$LineBorderUIResource", new Object[] { localColorUIResource6, new Integer(1) });
/*      */ 
/*  654 */     SwingLazyValue localSwingLazyValue12 = new SwingLazyValue("javax.swing.plaf.BorderUIResource$LineBorderUIResource", new Object[] { localColorUIResource15 });
/*      */ 
/*  658 */     SwingLazyValue localSwingLazyValue13 = new SwingLazyValue("javax.swing.plaf.BorderUIResource$LineBorderUIResource", new Object[] { localColorUIResource6 });
/*      */ 
/*  662 */     SwingLazyValue localSwingLazyValue14 = new SwingLazyValue("javax.swing.plaf.BorderUIResource$LineBorderUIResource", new Object[] { localColorUIResource8 });
/*      */ 
/*  666 */     InsetsUIResource localInsetsUIResource2 = new InsetsUIResource(4, 2, 0, 6);
/*      */ 
/*  668 */     InsetsUIResource localInsetsUIResource3 = new InsetsUIResource(0, 9, 1, 9);
/*      */ 
/*  670 */     Object[] arrayOfObject1 = new Object[1];
/*  671 */     arrayOfObject1[0] = new Integer(16);
/*      */ 
/*  673 */     Object[] arrayOfObject2 = { "OptionPane.errorSound", "OptionPane.informationSound", "OptionPane.questionSound", "OptionPane.warningSound" };
/*      */ 
/*  679 */     MetalTheme localMetalTheme = getCurrentTheme();
/*  680 */     FontActiveValue localFontActiveValue1 = new FontActiveValue(localMetalTheme, 3);
/*      */ 
/*  682 */     FontActiveValue localFontActiveValue2 = new FontActiveValue(localMetalTheme, 0);
/*      */ 
/*  684 */     FontActiveValue localFontActiveValue3 = new FontActiveValue(localMetalTheme, 2);
/*      */ 
/*  686 */     FontActiveValue localFontActiveValue4 = new FontActiveValue(localMetalTheme, 4);
/*      */ 
/*  688 */     FontActiveValue localFontActiveValue5 = new FontActiveValue(localMetalTheme, 5);
/*      */ 
/*  690 */     FontActiveValue localFontActiveValue6 = new FontActiveValue(localMetalTheme, 1);
/*      */ 
/*  696 */     Object[] arrayOfObject3 = { "AuditoryCues.defaultCueList", arrayOfObject2, "AuditoryCues.playList", null, "TextField.border", localSwingLazyValue1, "TextField.font", localFontActiveValue3, "PasswordField.border", localSwingLazyValue1, "PasswordField.font", localFontActiveValue3, "PasswordField.echoChar", Character.valueOf('•'), "TextArea.font", localFontActiveValue3, "TextPane.background", paramUIDefaults.get("window"), "TextPane.font", localFontActiveValue3, "EditorPane.background", paramUIDefaults.get("window"), "EditorPane.font", localFontActiveValue3, "TextField.focusInputMap", localLazyInputMap1, "PasswordField.focusInputMap", localLazyInputMap2, "TextArea.focusInputMap", localLazyInputMap3, "TextPane.focusInputMap", localLazyInputMap3, "EditorPane.focusInputMap", localLazyInputMap3, "FormattedTextField.border", localSwingLazyValue1, "FormattedTextField.font", localFontActiveValue3, "FormattedTextField.focusInputMap", new UIDefaults.LazyInputMap(new Object[] { "ctrl C", "copy-to-clipboard", "ctrl V", "paste-from-clipboard", "ctrl X", "cut-to-clipboard", "COPY", "copy-to-clipboard", "PASTE", "paste-from-clipboard", "CUT", "cut-to-clipboard", "control INSERT", "copy-to-clipboard", "shift INSERT", "paste-from-clipboard", "shift DELETE", "cut-to-clipboard", "shift LEFT", "selection-backward", "shift KP_LEFT", "selection-backward", "shift RIGHT", "selection-forward", "shift KP_RIGHT", "selection-forward", "ctrl LEFT", "caret-previous-word", "ctrl KP_LEFT", "caret-previous-word", "ctrl RIGHT", "caret-next-word", "ctrl KP_RIGHT", "caret-next-word", "ctrl shift LEFT", "selection-previous-word", "ctrl shift KP_LEFT", "selection-previous-word", "ctrl shift RIGHT", "selection-next-word", "ctrl shift KP_RIGHT", "selection-next-word", "ctrl A", "select-all", "HOME", "caret-begin-line", "END", "caret-end-line", "shift HOME", "selection-begin-line", "shift END", "selection-end-line", "BACK_SPACE", "delete-previous", "shift BACK_SPACE", "delete-previous", "ctrl H", "delete-previous", "DELETE", "delete-next", "ctrl DELETE", "delete-next-word", "ctrl BACK_SPACE", "delete-previous-word", "RIGHT", "caret-forward", "LEFT", "caret-backward", "KP_RIGHT", "caret-forward", "KP_LEFT", "caret-backward", "ENTER", "notify-field-accept", "ctrl BACK_SLASH", "unselect", "control shift O", "toggle-componentOrientation", "ESCAPE", "reset-field-edit", "UP", "increment", "KP_UP", "increment", "DOWN", "decrement", "KP_DOWN", "decrement" }), "Button.defaultButtonFollowsFocus", Boolean.FALSE, "Button.disabledText", localColorUIResource9, "Button.select", localColorUIResource5, "Button.border", localSwingLazyValue3, "Button.font", localFontActiveValue2, "Button.focus", localColorUIResource8, "Button.focusInputMap", new UIDefaults.LazyInputMap(new Object[] { "SPACE", "pressed", "released SPACE", "released" }), "CheckBox.disabledText", localColorUIResource9, "Checkbox.select", localColorUIResource5, "CheckBox.font", localFontActiveValue2, "CheckBox.focus", localColorUIResource8, "CheckBox.icon", new SwingLazyValue("javax.swing.plaf.metal.MetalIconFactory", "getCheckBoxIcon"), "CheckBox.focusInputMap", new UIDefaults.LazyInputMap(new Object[] { "SPACE", "pressed", "released SPACE", "released" }), "CheckBox.totalInsets", new Insets(4, 4, 4, 4), "RadioButton.disabledText", localColorUIResource9, "RadioButton.select", localColorUIResource5, "RadioButton.icon", new SwingLazyValue("javax.swing.plaf.metal.MetalIconFactory", "getRadioButtonIcon"), "RadioButton.font", localFontActiveValue2, "RadioButton.focus", localColorUIResource8, "RadioButton.focusInputMap", new UIDefaults.LazyInputMap(new Object[] { "SPACE", "pressed", "released SPACE", "released" }), "RadioButton.totalInsets", new Insets(4, 4, 4, 4), "ToggleButton.select", localColorUIResource5, "ToggleButton.disabledText", localColorUIResource9, "ToggleButton.focus", localColorUIResource8, "ToggleButton.border", localSwingLazyValue4, "ToggleButton.font", localFontActiveValue2, "ToggleButton.focusInputMap", new UIDefaults.LazyInputMap(new Object[] { "SPACE", "pressed", "released SPACE", "released" }), "FileView.directoryIcon", new SwingLazyValue("javax.swing.plaf.metal.MetalIconFactory", "getTreeFolderIcon"), "FileView.fileIcon", new SwingLazyValue("javax.swing.plaf.metal.MetalIconFactory", "getTreeLeafIcon"), "FileView.computerIcon", new SwingLazyValue("javax.swing.plaf.metal.MetalIconFactory", "getTreeComputerIcon"), "FileView.hardDriveIcon", new SwingLazyValue("javax.swing.plaf.metal.MetalIconFactory", "getTreeHardDriveIcon"), "FileView.floppyDriveIcon", new SwingLazyValue("javax.swing.plaf.metal.MetalIconFactory", "getTreeFloppyDriveIcon"), "FileChooser.detailsViewIcon", new SwingLazyValue("javax.swing.plaf.metal.MetalIconFactory", "getFileChooserDetailViewIcon"), "FileChooser.homeFolderIcon", new SwingLazyValue("javax.swing.plaf.metal.MetalIconFactory", "getFileChooserHomeFolderIcon"), "FileChooser.listViewIcon", new SwingLazyValue("javax.swing.plaf.metal.MetalIconFactory", "getFileChooserListViewIcon"), "FileChooser.newFolderIcon", new SwingLazyValue("javax.swing.plaf.metal.MetalIconFactory", "getFileChooserNewFolderIcon"), "FileChooser.upFolderIcon", new SwingLazyValue("javax.swing.plaf.metal.MetalIconFactory", "getFileChooserUpFolderIcon"), "FileChooser.usesSingleFilePane", Boolean.TRUE, "FileChooser.ancestorInputMap", new UIDefaults.LazyInputMap(new Object[] { "ESCAPE", "cancelSelection", "F2", "editFileName", "F5", "refresh", "BACK_SPACE", "Go Up" }), "ToolTip.font", localFontActiveValue6, "ToolTip.border", localSwingLazyValue12, "ToolTip.borderInactive", localSwingLazyValue13, "ToolTip.backgroundInactive", localColorUIResource3, "ToolTip.foregroundInactive", localColorUIResource6, "ToolTip.hideAccelerator", Boolean.FALSE, "ToolTipManager.enableToolTipMode", "activeApplication", "Slider.font", localFontActiveValue2, "Slider.border", null, "Slider.foreground", localColorUIResource16, "Slider.focus", localColorUIResource8, "Slider.focusInsets", localInsetsUIResource1, "Slider.trackWidth", new Integer(7), "Slider.majorTickLength", new Integer(6), "Slider.horizontalThumbIcon", new SwingLazyValue("javax.swing.plaf.metal.MetalIconFactory", "getHorizontalSliderThumbIcon"), "Slider.verticalThumbIcon", new SwingLazyValue("javax.swing.plaf.metal.MetalIconFactory", "getVerticalSliderThumbIcon"), "Slider.focusInputMap", new UIDefaults.LazyInputMap(new Object[] { "RIGHT", "positiveUnitIncrement", "KP_RIGHT", "positiveUnitIncrement", "DOWN", "negativeUnitIncrement", "KP_DOWN", "negativeUnitIncrement", "PAGE_DOWN", "negativeBlockIncrement", "ctrl PAGE_DOWN", "negativeBlockIncrement", "LEFT", "negativeUnitIncrement", "KP_LEFT", "negativeUnitIncrement", "UP", "positiveUnitIncrement", "KP_UP", "positiveUnitIncrement", "PAGE_UP", "positiveBlockIncrement", "ctrl PAGE_UP", "positiveBlockIncrement", "HOME", "minScroll", "END", "maxScroll" }), "ProgressBar.font", localFontActiveValue2, "ProgressBar.foreground", localColorUIResource16, "ProgressBar.selectionBackground", localColorUIResource15, "ProgressBar.border", localSwingLazyValue11, "ProgressBar.cellSpacing", localInteger, "ProgressBar.cellLength", Integer.valueOf(1), "ComboBox.background", localColorUIResource3, "ComboBox.foreground", localColorUIResource7, "ComboBox.selectionBackground", localColorUIResource16, "ComboBox.selectionForeground", localColorUIResource7, "ComboBox.font", localFontActiveValue2, "ComboBox.ancestorInputMap", new UIDefaults.LazyInputMap(new Object[] { "ESCAPE", "hidePopup", "PAGE_UP", "pageUpPassThrough", "PAGE_DOWN", "pageDownPassThrough", "HOME", "homePassThrough", "END", "endPassThrough", "DOWN", "selectNext", "KP_DOWN", "selectNext", "alt DOWN", "togglePopup", "alt KP_DOWN", "togglePopup", "alt UP", "togglePopup", "alt KP_UP", "togglePopup", "SPACE", "spacePopup", "ENTER", "enterPressed", "UP", "selectPrevious", "KP_UP", "selectPrevious" }), "InternalFrame.icon", new SwingLazyValue("javax.swing.plaf.metal.MetalIconFactory", "getInternalFrameDefaultMenuIcon"), "InternalFrame.border", new SwingLazyValue("javax.swing.plaf.metal.MetalBorders$InternalFrameBorder"), "InternalFrame.optionDialogBorder", new SwingLazyValue("javax.swing.plaf.metal.MetalBorders$OptionDialogBorder"), "InternalFrame.paletteBorder", new SwingLazyValue("javax.swing.plaf.metal.MetalBorders$PaletteBorder"), "InternalFrame.paletteTitleHeight", new Integer(11), "InternalFrame.paletteCloseIcon", new SwingLazyValue("javax.swing.plaf.metal.MetalIconFactory$PaletteCloseIcon"), "InternalFrame.closeIcon", new SwingLazyValue("javax.swing.plaf.metal.MetalIconFactory", "getInternalFrameCloseIcon", arrayOfObject1), "InternalFrame.maximizeIcon", new SwingLazyValue("javax.swing.plaf.metal.MetalIconFactory", "getInternalFrameMaximizeIcon", arrayOfObject1), "InternalFrame.iconifyIcon", new SwingLazyValue("javax.swing.plaf.metal.MetalIconFactory", "getInternalFrameMinimizeIcon", arrayOfObject1), "InternalFrame.minimizeIcon", new SwingLazyValue("javax.swing.plaf.metal.MetalIconFactory", "getInternalFrameAltMaximizeIcon", arrayOfObject1), "InternalFrame.titleFont", localFontActiveValue4, "InternalFrame.windowBindings", null, "InternalFrame.closeSound", "sounds/FrameClose.wav", "InternalFrame.maximizeSound", "sounds/FrameMaximize.wav", "InternalFrame.minimizeSound", "sounds/FrameMinimize.wav", "InternalFrame.restoreDownSound", "sounds/FrameRestoreDown.wav", "InternalFrame.restoreUpSound", "sounds/FrameRestoreUp.wav", "DesktopIcon.border", localSwingLazyValue6, "DesktopIcon.font", localFontActiveValue2, "DesktopIcon.foreground", localColorUIResource7, "DesktopIcon.background", localColorUIResource3, "DesktopIcon.width", Integer.valueOf(160), "Desktop.ancestorInputMap", new UIDefaults.LazyInputMap(new Object[] { "ctrl F5", "restore", "ctrl F4", "close", "ctrl F7", "move", "ctrl F8", "resize", "RIGHT", "right", "KP_RIGHT", "right", "shift RIGHT", "shrinkRight", "shift KP_RIGHT", "shrinkRight", "LEFT", "left", "KP_LEFT", "left", "shift LEFT", "shrinkLeft", "shift KP_LEFT", "shrinkLeft", "UP", "up", "KP_UP", "up", "shift UP", "shrinkUp", "shift KP_UP", "shrinkUp", "DOWN", "down", "KP_DOWN", "down", "shift DOWN", "shrinkDown", "shift KP_DOWN", "shrinkDown", "ESCAPE", "escape", "ctrl F9", "minimize", "ctrl F10", "maximize", "ctrl F6", "selectNextFrame", "ctrl TAB", "selectNextFrame", "ctrl alt F6", "selectNextFrame", "shift ctrl alt F6", "selectPreviousFrame", "ctrl F12", "navigateNext", "shift ctrl F12", "navigatePrevious" }), "TitledBorder.font", localFontActiveValue2, "TitledBorder.titleColor", localColorUIResource17, "TitledBorder.border", localSwingLazyValue5, "Label.font", localFontActiveValue2, "Label.foreground", localColorUIResource17, "Label.disabledForeground", getInactiveSystemTextColor(), "List.font", localFontActiveValue2, "List.focusCellHighlightBorder", localSwingLazyValue14, "List.focusInputMap", new UIDefaults.LazyInputMap(new Object[] { "ctrl C", "copy", "ctrl V", "paste", "ctrl X", "cut", "COPY", "copy", "PASTE", "paste", "CUT", "cut", "control INSERT", "copy", "shift INSERT", "paste", "shift DELETE", "cut", "UP", "selectPreviousRow", "KP_UP", "selectPreviousRow", "shift UP", "selectPreviousRowExtendSelection", "shift KP_UP", "selectPreviousRowExtendSelection", "ctrl shift UP", "selectPreviousRowExtendSelection", "ctrl shift KP_UP", "selectPreviousRowExtendSelection", "ctrl UP", "selectPreviousRowChangeLead", "ctrl KP_UP", "selectPreviousRowChangeLead", "DOWN", "selectNextRow", "KP_DOWN", "selectNextRow", "shift DOWN", "selectNextRowExtendSelection", "shift KP_DOWN", "selectNextRowExtendSelection", "ctrl shift DOWN", "selectNextRowExtendSelection", "ctrl shift KP_DOWN", "selectNextRowExtendSelection", "ctrl DOWN", "selectNextRowChangeLead", "ctrl KP_DOWN", "selectNextRowChangeLead", "LEFT", "selectPreviousColumn", "KP_LEFT", "selectPreviousColumn", "shift LEFT", "selectPreviousColumnExtendSelection", "shift KP_LEFT", "selectPreviousColumnExtendSelection", "ctrl shift LEFT", "selectPreviousColumnExtendSelection", "ctrl shift KP_LEFT", "selectPreviousColumnExtendSelection", "ctrl LEFT", "selectPreviousColumnChangeLead", "ctrl KP_LEFT", "selectPreviousColumnChangeLead", "RIGHT", "selectNextColumn", "KP_RIGHT", "selectNextColumn", "shift RIGHT", "selectNextColumnExtendSelection", "shift KP_RIGHT", "selectNextColumnExtendSelection", "ctrl shift RIGHT", "selectNextColumnExtendSelection", "ctrl shift KP_RIGHT", "selectNextColumnExtendSelection", "ctrl RIGHT", "selectNextColumnChangeLead", "ctrl KP_RIGHT", "selectNextColumnChangeLead", "HOME", "selectFirstRow", "shift HOME", "selectFirstRowExtendSelection", "ctrl shift HOME", "selectFirstRowExtendSelection", "ctrl HOME", "selectFirstRowChangeLead", "END", "selectLastRow", "shift END", "selectLastRowExtendSelection", "ctrl shift END", "selectLastRowExtendSelection", "ctrl END", "selectLastRowChangeLead", "PAGE_UP", "scrollUp", "shift PAGE_UP", "scrollUpExtendSelection", "ctrl shift PAGE_UP", "scrollUpExtendSelection", "ctrl PAGE_UP", "scrollUpChangeLead", "PAGE_DOWN", "scrollDown", "shift PAGE_DOWN", "scrollDownExtendSelection", "ctrl shift PAGE_DOWN", "scrollDownExtendSelection", "ctrl PAGE_DOWN", "scrollDownChangeLead", "ctrl A", "selectAll", "ctrl SLASH", "selectAll", "ctrl BACK_SLASH", "clearSelection", "SPACE", "addToSelection", "ctrl SPACE", "toggleAndAnchor", "shift SPACE", "extendTo", "ctrl shift SPACE", "moveSelectionTo" }), "ScrollBar.background", localColorUIResource3, "ScrollBar.highlight", localColorUIResource4, "ScrollBar.shadow", localColorUIResource5, "ScrollBar.darkShadow", localColorUIResource6, "ScrollBar.thumb", localColorUIResource16, "ScrollBar.thumbShadow", localColorUIResource15, "ScrollBar.thumbHighlight", localColorUIResource14, "ScrollBar.width", new Integer(17), "ScrollBar.allowsAbsolutePositioning", Boolean.TRUE, "ScrollBar.ancestorInputMap", new UIDefaults.LazyInputMap(new Object[] { "RIGHT", "positiveUnitIncrement", "KP_RIGHT", "positiveUnitIncrement", "DOWN", "positiveUnitIncrement", "KP_DOWN", "positiveUnitIncrement", "PAGE_DOWN", "positiveBlockIncrement", "LEFT", "negativeUnitIncrement", "KP_LEFT", "negativeUnitIncrement", "UP", "negativeUnitIncrement", "KP_UP", "negativeUnitIncrement", "PAGE_UP", "negativeBlockIncrement", "HOME", "minScroll", "END", "maxScroll" }), "ScrollPane.border", localSwingLazyValue2, "ScrollPane.ancestorInputMap", new UIDefaults.LazyInputMap(new Object[] { "RIGHT", "unitScrollRight", "KP_RIGHT", "unitScrollRight", "DOWN", "unitScrollDown", "KP_DOWN", "unitScrollDown", "LEFT", "unitScrollLeft", "KP_LEFT", "unitScrollLeft", "UP", "unitScrollUp", "KP_UP", "unitScrollUp", "PAGE_UP", "scrollUp", "PAGE_DOWN", "scrollDown", "ctrl PAGE_UP", "scrollLeft", "ctrl PAGE_DOWN", "scrollRight", "ctrl HOME", "scrollHome", "ctrl END", "scrollEnd" }), "TabbedPane.font", localFontActiveValue2, "TabbedPane.tabAreaBackground", localColorUIResource3, "TabbedPane.background", localColorUIResource5, "TabbedPane.light", localColorUIResource3, "TabbedPane.focus", localColorUIResource15, "TabbedPane.selected", localColorUIResource3, "TabbedPane.selectHighlight", localColorUIResource4, "TabbedPane.tabAreaInsets", localInsetsUIResource2, "TabbedPane.tabInsets", localInsetsUIResource3, "TabbedPane.focusInputMap", new UIDefaults.LazyInputMap(new Object[] { "RIGHT", "navigateRight", "KP_RIGHT", "navigateRight", "LEFT", "navigateLeft", "KP_LEFT", "navigateLeft", "UP", "navigateUp", "KP_UP", "navigateUp", "DOWN", "navigateDown", "KP_DOWN", "navigateDown", "ctrl DOWN", "requestFocusForVisibleComponent", "ctrl KP_DOWN", "requestFocusForVisibleComponent" }), "TabbedPane.ancestorInputMap", new UIDefaults.LazyInputMap(new Object[] { "ctrl PAGE_DOWN", "navigatePageDown", "ctrl PAGE_UP", "navigatePageUp", "ctrl UP", "requestFocus", "ctrl KP_UP", "requestFocus" }), "Table.font", localFontActiveValue3, "Table.focusCellHighlightBorder", localSwingLazyValue14, "Table.scrollPaneBorder", localSwingLazyValue2, "Table.dropLineColor", localColorUIResource8, "Table.dropLineShortColor", localColorUIResource15, "Table.gridColor", localColorUIResource5, "Table.ancestorInputMap", new UIDefaults.LazyInputMap(new Object[] { "ctrl C", "copy", "ctrl V", "paste", "ctrl X", "cut", "COPY", "copy", "PASTE", "paste", "CUT", "cut", "control INSERT", "copy", "shift INSERT", "paste", "shift DELETE", "cut", "RIGHT", "selectNextColumn", "KP_RIGHT", "selectNextColumn", "shift RIGHT", "selectNextColumnExtendSelection", "shift KP_RIGHT", "selectNextColumnExtendSelection", "ctrl shift RIGHT", "selectNextColumnExtendSelection", "ctrl shift KP_RIGHT", "selectNextColumnExtendSelection", "ctrl RIGHT", "selectNextColumnChangeLead", "ctrl KP_RIGHT", "selectNextColumnChangeLead", "LEFT", "selectPreviousColumn", "KP_LEFT", "selectPreviousColumn", "shift LEFT", "selectPreviousColumnExtendSelection", "shift KP_LEFT", "selectPreviousColumnExtendSelection", "ctrl shift LEFT", "selectPreviousColumnExtendSelection", "ctrl shift KP_LEFT", "selectPreviousColumnExtendSelection", "ctrl LEFT", "selectPreviousColumnChangeLead", "ctrl KP_LEFT", "selectPreviousColumnChangeLead", "DOWN", "selectNextRow", "KP_DOWN", "selectNextRow", "shift DOWN", "selectNextRowExtendSelection", "shift KP_DOWN", "selectNextRowExtendSelection", "ctrl shift DOWN", "selectNextRowExtendSelection", "ctrl shift KP_DOWN", "selectNextRowExtendSelection", "ctrl DOWN", "selectNextRowChangeLead", "ctrl KP_DOWN", "selectNextRowChangeLead", "UP", "selectPreviousRow", "KP_UP", "selectPreviousRow", "shift UP", "selectPreviousRowExtendSelection", "shift KP_UP", "selectPreviousRowExtendSelection", "ctrl shift UP", "selectPreviousRowExtendSelection", "ctrl shift KP_UP", "selectPreviousRowExtendSelection", "ctrl UP", "selectPreviousRowChangeLead", "ctrl KP_UP", "selectPreviousRowChangeLead", "HOME", "selectFirstColumn", "shift HOME", "selectFirstColumnExtendSelection", "ctrl shift HOME", "selectFirstRowExtendSelection", "ctrl HOME", "selectFirstRow", "END", "selectLastColumn", "shift END", "selectLastColumnExtendSelection", "ctrl shift END", "selectLastRowExtendSelection", "ctrl END", "selectLastRow", "PAGE_UP", "scrollUpChangeSelection", "shift PAGE_UP", "scrollUpExtendSelection", "ctrl shift PAGE_UP", "scrollLeftExtendSelection", "ctrl PAGE_UP", "scrollLeftChangeSelection", "PAGE_DOWN", "scrollDownChangeSelection", "shift PAGE_DOWN", "scrollDownExtendSelection", "ctrl shift PAGE_DOWN", "scrollRightExtendSelection", "ctrl PAGE_DOWN", "scrollRightChangeSelection", "TAB", "selectNextColumnCell", "shift TAB", "selectPreviousColumnCell", "ENTER", "selectNextRowCell", "shift ENTER", "selectPreviousRowCell", "ctrl A", "selectAll", "ctrl SLASH", "selectAll", "ctrl BACK_SLASH", "clearSelection", "ESCAPE", "cancel", "F2", "startEditing", "SPACE", "addToSelection", "ctrl SPACE", "toggleAndAnchor", "shift SPACE", "extendTo", "ctrl shift SPACE", "moveSelectionTo", "F8", "focusHeader" }), "Table.ascendingSortIcon", SwingUtilities2.makeIcon(getClass(), MetalLookAndFeel.class, "icons/sortUp.png"), "Table.descendingSortIcon", SwingUtilities2.makeIcon(getClass(), MetalLookAndFeel.class, "icons/sortDown.png"), "TableHeader.font", localFontActiveValue3, "TableHeader.cellBorder", new SwingLazyValue("javax.swing.plaf.metal.MetalBorders$TableHeaderBorder"), "MenuBar.border", localSwingLazyValue7, "MenuBar.font", localFontActiveValue1, "MenuBar.windowBindings", { "F10", "takeFocus" }, "Menu.border", localSwingLazyValue9, "Menu.borderPainted", Boolean.TRUE, "Menu.menuPopupOffsetX", localInteger, "Menu.menuPopupOffsetY", localInteger, "Menu.submenuPopupOffsetX", new Integer(-4), "Menu.submenuPopupOffsetY", new Integer(-3), "Menu.font", localFontActiveValue1, "Menu.selectionForeground", localColorUIResource13, "Menu.selectionBackground", localColorUIResource11, "Menu.disabledForeground", localColorUIResource12, "Menu.acceleratorFont", localFontActiveValue5, "Menu.acceleratorForeground", localColorUIResource1, "Menu.acceleratorSelectionForeground", localColorUIResource2, "Menu.checkIcon", new SwingLazyValue("javax.swing.plaf.metal.MetalIconFactory", "getMenuItemCheckIcon"), "Menu.arrowIcon", new SwingLazyValue("javax.swing.plaf.metal.MetalIconFactory", "getMenuArrowIcon"), "MenuItem.border", localSwingLazyValue9, "MenuItem.borderPainted", Boolean.TRUE, "MenuItem.font", localFontActiveValue1, "MenuItem.selectionForeground", localColorUIResource13, "MenuItem.selectionBackground", localColorUIResource11, "MenuItem.disabledForeground", localColorUIResource12, "MenuItem.acceleratorFont", localFontActiveValue5, "MenuItem.acceleratorForeground", localColorUIResource1, "MenuItem.acceleratorSelectionForeground", localColorUIResource2, "MenuItem.acceleratorDelimiter", str, "MenuItem.checkIcon", new SwingLazyValue("javax.swing.plaf.metal.MetalIconFactory", "getMenuItemCheckIcon"), "MenuItem.arrowIcon", new SwingLazyValue("javax.swing.plaf.metal.MetalIconFactory", "getMenuItemArrowIcon"), "MenuItem.commandSound", "sounds/MenuItemCommand.wav", "OptionPane.windowBindings", { "ESCAPE", "close" }, "OptionPane.informationSound", "sounds/OptionPaneInformation.wav", "OptionPane.warningSound", "sounds/OptionPaneWarning.wav", "OptionPane.errorSound", "sounds/OptionPaneError.wav", "OptionPane.questionSound", "sounds/OptionPaneQuestion.wav", "OptionPane.errorDialog.border.background", new ColorUIResource(153, 51, 51), "OptionPane.errorDialog.titlePane.foreground", new ColorUIResource(51, 0, 0), "OptionPane.errorDialog.titlePane.background", new ColorUIResource(255, 153, 153), "OptionPane.errorDialog.titlePane.shadow", new ColorUIResource(204, 102, 102), "OptionPane.questionDialog.border.background", new ColorUIResource(51, 102, 51), "OptionPane.questionDialog.titlePane.foreground", new ColorUIResource(0, 51, 0), "OptionPane.questionDialog.titlePane.background", new ColorUIResource(153, 204, 153), "OptionPane.questionDialog.titlePane.shadow", new ColorUIResource(102, 153, 102), "OptionPane.warningDialog.border.background", new ColorUIResource(153, 102, 51), "OptionPane.warningDialog.titlePane.foreground", new ColorUIResource(102, 51, 0), "OptionPane.warningDialog.titlePane.background", new ColorUIResource(255, 204, 153), "OptionPane.warningDialog.titlePane.shadow", new ColorUIResource(204, 153, 102), "Separator.background", getSeparatorBackground(), "Separator.foreground", getSeparatorForeground(), "PopupMenu.border", localSwingLazyValue8, "PopupMenu.popupSound", "sounds/PopupMenuPopup.wav", "PopupMenu.font", localFontActiveValue1, "CheckBoxMenuItem.border", localSwingLazyValue9, "CheckBoxMenuItem.borderPainted", Boolean.TRUE, "CheckBoxMenuItem.font", localFontActiveValue1, "CheckBoxMenuItem.selectionForeground", localColorUIResource13, "CheckBoxMenuItem.selectionBackground", localColorUIResource11, "CheckBoxMenuItem.disabledForeground", localColorUIResource12, "CheckBoxMenuItem.acceleratorFont", localFontActiveValue5, "CheckBoxMenuItem.acceleratorForeground", localColorUIResource1, "CheckBoxMenuItem.acceleratorSelectionForeground", localColorUIResource2, "CheckBoxMenuItem.checkIcon", new SwingLazyValue("javax.swing.plaf.metal.MetalIconFactory", "getCheckBoxMenuItemIcon"), "CheckBoxMenuItem.arrowIcon", new SwingLazyValue("javax.swing.plaf.metal.MetalIconFactory", "getMenuItemArrowIcon"), "CheckBoxMenuItem.commandSound", "sounds/MenuItemCommand.wav", "RadioButtonMenuItem.border", localSwingLazyValue9, "RadioButtonMenuItem.borderPainted", Boolean.TRUE, "RadioButtonMenuItem.font", localFontActiveValue1, "RadioButtonMenuItem.selectionForeground", localColorUIResource13, "RadioButtonMenuItem.selectionBackground", localColorUIResource11, "RadioButtonMenuItem.disabledForeground", localColorUIResource12, "RadioButtonMenuItem.acceleratorFont", localFontActiveValue5, "RadioButtonMenuItem.acceleratorForeground", localColorUIResource1, "RadioButtonMenuItem.acceleratorSelectionForeground", localColorUIResource2, "RadioButtonMenuItem.checkIcon", new SwingLazyValue("javax.swing.plaf.metal.MetalIconFactory", "getRadioButtonMenuItemIcon"), "RadioButtonMenuItem.arrowIcon", new SwingLazyValue("javax.swing.plaf.metal.MetalIconFactory", "getMenuItemArrowIcon"), "RadioButtonMenuItem.commandSound", "sounds/MenuItemCommand.wav", "Spinner.ancestorInputMap", new UIDefaults.LazyInputMap(new Object[] { "UP", "increment", "KP_UP", "increment", "DOWN", "decrement", "KP_DOWN", "decrement" }), "Spinner.arrowButtonInsets", localInsetsUIResource1, "Spinner.border", localSwingLazyValue1, "Spinner.arrowButtonBorder", localSwingLazyValue3, "Spinner.font", localFontActiveValue2, "SplitPane.dividerSize", new Integer(10), "SplitPane.ancestorInputMap", new UIDefaults.LazyInputMap(new Object[] { "UP", "negativeIncrement", "DOWN", "positiveIncrement", "LEFT", "negativeIncrement", "RIGHT", "positiveIncrement", "KP_UP", "negativeIncrement", "KP_DOWN", "positiveIncrement", "KP_LEFT", "negativeIncrement", "KP_RIGHT", "positiveIncrement", "HOME", "selectMin", "END", "selectMax", "F8", "startResize", "F6", "toggleFocus", "ctrl TAB", "focusOutForward", "ctrl shift TAB", "focusOutBackward" }), "SplitPane.centerOneTouchButtons", Boolean.FALSE, "SplitPane.dividerFocusColor", localColorUIResource14, "Tree.font", localFontActiveValue3, "Tree.textBackground", getWindowBackground(), "Tree.selectionBorderColor", localColorUIResource8, "Tree.openIcon", new SwingLazyValue("javax.swing.plaf.metal.MetalIconFactory", "getTreeFolderIcon"), "Tree.closedIcon", new SwingLazyValue("javax.swing.plaf.metal.MetalIconFactory", "getTreeFolderIcon"), "Tree.leafIcon", new SwingLazyValue("javax.swing.plaf.metal.MetalIconFactory", "getTreeLeafIcon"), "Tree.expandedIcon", new SwingLazyValue("javax.swing.plaf.metal.MetalIconFactory", "getTreeControlIcon", new Object[] { Boolean.valueOf(false) }), "Tree.collapsedIcon", new SwingLazyValue("javax.swing.plaf.metal.MetalIconFactory", "getTreeControlIcon", new Object[] { Boolean.valueOf(true) }), "Tree.line", localColorUIResource14, "Tree.hash", localColorUIResource14, "Tree.rowHeight", localInteger, "Tree.focusInputMap", new UIDefaults.LazyInputMap(new Object[] { "ADD", "expand", "SUBTRACT", "collapse", "ctrl C", "copy", "ctrl V", "paste", "ctrl X", "cut", "COPY", "copy", "PASTE", "paste", "CUT", "cut", "control INSERT", "copy", "shift INSERT", "paste", "shift DELETE", "cut", "UP", "selectPrevious", "KP_UP", "selectPrevious", "shift UP", "selectPreviousExtendSelection", "shift KP_UP", "selectPreviousExtendSelection", "ctrl shift UP", "selectPreviousExtendSelection", "ctrl shift KP_UP", "selectPreviousExtendSelection", "ctrl UP", "selectPreviousChangeLead", "ctrl KP_UP", "selectPreviousChangeLead", "DOWN", "selectNext", "KP_DOWN", "selectNext", "shift DOWN", "selectNextExtendSelection", "shift KP_DOWN", "selectNextExtendSelection", "ctrl shift DOWN", "selectNextExtendSelection", "ctrl shift KP_DOWN", "selectNextExtendSelection", "ctrl DOWN", "selectNextChangeLead", "ctrl KP_DOWN", "selectNextChangeLead", "RIGHT", "selectChild", "KP_RIGHT", "selectChild", "LEFT", "selectParent", "KP_LEFT", "selectParent", "PAGE_UP", "scrollUpChangeSelection", "shift PAGE_UP", "scrollUpExtendSelection", "ctrl shift PAGE_UP", "scrollUpExtendSelection", "ctrl PAGE_UP", "scrollUpChangeLead", "PAGE_DOWN", "scrollDownChangeSelection", "shift PAGE_DOWN", "scrollDownExtendSelection", "ctrl shift PAGE_DOWN", "scrollDownExtendSelection", "ctrl PAGE_DOWN", "scrollDownChangeLead", "HOME", "selectFirst", "shift HOME", "selectFirstExtendSelection", "ctrl shift HOME", "selectFirstExtendSelection", "ctrl HOME", "selectFirstChangeLead", "END", "selectLast", "shift END", "selectLastExtendSelection", "ctrl shift END", "selectLastExtendSelection", "ctrl END", "selectLastChangeLead", "F2", "startEditing", "ctrl A", "selectAll", "ctrl SLASH", "selectAll", "ctrl BACK_SLASH", "clearSelection", "ctrl LEFT", "scrollLeft", "ctrl KP_LEFT", "scrollLeft", "ctrl RIGHT", "scrollRight", "ctrl KP_RIGHT", "scrollRight", "SPACE", "addToSelection", "ctrl SPACE", "toggleAndAnchor", "shift SPACE", "extendTo", "ctrl shift SPACE", "moveSelectionTo" }), "Tree.ancestorInputMap", new UIDefaults.LazyInputMap(new Object[] { "ESCAPE", "cancel" }), "ToolBar.border", localSwingLazyValue10, "ToolBar.background", localColorUIResource10, "ToolBar.foreground", getMenuForeground(), "ToolBar.font", localFontActiveValue1, "ToolBar.dockingBackground", localColorUIResource10, "ToolBar.floatingBackground", localColorUIResource10, "ToolBar.dockingForeground", localColorUIResource15, "ToolBar.floatingForeground", localColorUIResource14, "ToolBar.rolloverBorder", new MetalLazyValue("javax.swing.plaf.metal.MetalBorders", "getToolBarRolloverBorder"), "ToolBar.nonrolloverBorder", new MetalLazyValue("javax.swing.plaf.metal.MetalBorders", "getToolBarNonrolloverBorder"), "ToolBar.ancestorInputMap", new UIDefaults.LazyInputMap(new Object[] { "UP", "navigateUp", "KP_UP", "navigateUp", "DOWN", "navigateDown", "KP_DOWN", "navigateDown", "LEFT", "navigateLeft", "KP_LEFT", "navigateLeft", "RIGHT", "navigateRight", "KP_RIGHT", "navigateRight" }), "RootPane.frameBorder", new MetalLazyValue("javax.swing.plaf.metal.MetalBorders$FrameBorder"), "RootPane.plainDialogBorder", localMetalLazyValue1, "RootPane.informationDialogBorder", localMetalLazyValue1, "RootPane.errorDialogBorder", new MetalLazyValue("javax.swing.plaf.metal.MetalBorders$ErrorDialogBorder"), "RootPane.colorChooserDialogBorder", localMetalLazyValue2, "RootPane.fileChooserDialogBorder", localMetalLazyValue2, "RootPane.questionDialogBorder", localMetalLazyValue2, "RootPane.warningDialogBorder", new MetalLazyValue("javax.swing.plaf.metal.MetalBorders$WarningDialogBorder"), "RootPane.defaultButtonWindowKeyBindings", { "ENTER", "press", "released ENTER", "release", "ctrl ENTER", "press", "ctrl released ENTER", "release" } };
/*      */ 
/* 1538 */     paramUIDefaults.putDefaults(arrayOfObject3);
/*      */ 
/* 1540 */     if ((isWindows()) && (useSystemFonts()) && (localMetalTheme.isSystemTheme())) {
/* 1541 */       MetalFontDesktopProperty localMetalFontDesktopProperty = new MetalFontDesktopProperty("win.messagebox.font.height", 0);
/*      */ 
/* 1544 */       arrayOfObject3 = new Object[] { "OptionPane.messageFont", localMetalFontDesktopProperty, "OptionPane.buttonFont", localMetalFontDesktopProperty };
/*      */ 
/* 1548 */       paramUIDefaults.putDefaults(arrayOfObject3);
/*      */     }
/*      */ 
/* 1551 */     flushUnreferenced();
/*      */ 
/* 1553 */     boolean bool = SwingUtilities2.isLocalDisplay();
/* 1554 */     SwingUtilities2.AATextInfo localAATextInfo = SwingUtilities2.AATextInfo.getAATextInfo(bool);
/* 1555 */     paramUIDefaults.put(SwingUtilities2.AA_TEXT_PROPERTY_KEY, localAATextInfo);
/* 1556 */     new AATextListener(this);
/*      */   }
/*      */ 
/*      */   protected void createDefaultTheme()
/*      */   {
/* 1566 */     getCurrentTheme();
/*      */   }
/*      */ 
/*      */   public UIDefaults getDefaults()
/*      */   {
/* 1586 */     METAL_LOOK_AND_FEEL_INITED = true;
/*      */ 
/* 1588 */     createDefaultTheme();
/* 1589 */     UIDefaults localUIDefaults = super.getDefaults();
/* 1590 */     MetalTheme localMetalTheme = getCurrentTheme();
/* 1591 */     localMetalTheme.addCustomEntriesToTable(localUIDefaults);
/* 1592 */     localMetalTheme.install();
/* 1593 */     return localUIDefaults;
/*      */   }
/*      */ 
/*      */   public void provideErrorFeedback(Component paramComponent)
/*      */   {
/* 1602 */     super.provideErrorFeedback(paramComponent);
/*      */   }
/*      */ 
/*      */   public static void setCurrentTheme(MetalTheme paramMetalTheme)
/*      */   {
/* 1631 */     if (paramMetalTheme == null) {
/* 1632 */       throw new NullPointerException("Can't have null theme");
/*      */     }
/* 1634 */     AppContext.getAppContext().put("currentMetalTheme", paramMetalTheme);
/*      */   }
/*      */ 
/*      */   public static MetalTheme getCurrentTheme()
/*      */   {
/* 1647 */     AppContext localAppContext = AppContext.getAppContext();
/* 1648 */     Object localObject = (MetalTheme)localAppContext.get("currentMetalTheme");
/* 1649 */     if (localObject == null)
/*      */     {
/* 1657 */       if (useHighContrastTheme()) {
/* 1658 */         localObject = new MetalHighContrastTheme();
/*      */       }
/*      */       else
/*      */       {
/* 1663 */         String str = (String)AccessController.doPrivileged(new GetPropertyAction("swing.metalTheme"));
/*      */ 
/* 1665 */         if ("steel".equals(str)) {
/* 1666 */           localObject = new DefaultMetalTheme();
/*      */         }
/*      */         else {
/* 1669 */           localObject = new OceanTheme();
/*      */         }
/*      */       }
/* 1672 */       setCurrentTheme((MetalTheme)localObject);
/*      */     }
/* 1674 */     return localObject;
/*      */   }
/*      */ 
/*      */   public Icon getDisabledIcon(JComponent paramJComponent, Icon paramIcon)
/*      */   {
/* 1696 */     if (((paramIcon instanceof ImageIcon)) && (usingOcean())) {
/* 1697 */       return MetalUtils.getOceanDisabledButtonIcon(((ImageIcon)paramIcon).getImage());
/*      */     }
/*      */ 
/* 1700 */     return super.getDisabledIcon(paramJComponent, paramIcon);
/*      */   }
/*      */ 
/*      */   public Icon getDisabledSelectedIcon(JComponent paramJComponent, Icon paramIcon)
/*      */   {
/* 1724 */     if (((paramIcon instanceof ImageIcon)) && (usingOcean())) {
/* 1725 */       return MetalUtils.getOceanDisabledButtonIcon(((ImageIcon)paramIcon).getImage());
/*      */     }
/*      */ 
/* 1728 */     return super.getDisabledSelectedIcon(paramJComponent, paramIcon);
/*      */   }
/*      */ 
/*      */   public static FontUIResource getControlTextFont()
/*      */   {
/* 1739 */     return getCurrentTheme().getControlTextFont();
/*      */   }
/*      */ 
/*      */   public static FontUIResource getSystemTextFont()
/*      */   {
/* 1749 */     return getCurrentTheme().getSystemTextFont();
/*      */   }
/*      */ 
/*      */   public static FontUIResource getUserTextFont()
/*      */   {
/* 1759 */     return getCurrentTheme().getUserTextFont();
/*      */   }
/*      */ 
/*      */   public static FontUIResource getMenuTextFont()
/*      */   {
/* 1769 */     return getCurrentTheme().getMenuTextFont();
/*      */   }
/*      */ 
/*      */   public static FontUIResource getWindowTitleFont()
/*      */   {
/* 1779 */     return getCurrentTheme().getWindowTitleFont();
/*      */   }
/*      */ 
/*      */   public static FontUIResource getSubTextFont()
/*      */   {
/* 1789 */     return getCurrentTheme().getSubTextFont();
/*      */   }
/*      */ 
/*      */   public static ColorUIResource getDesktopColor()
/*      */   {
/* 1799 */     return getCurrentTheme().getDesktopColor();
/*      */   }
/*      */ 
/*      */   public static ColorUIResource getFocusColor()
/*      */   {
/* 1809 */     return getCurrentTheme().getFocusColor();
/*      */   }
/*      */ 
/*      */   public static ColorUIResource getWhite()
/*      */   {
/* 1819 */     return getCurrentTheme().getWhite();
/*      */   }
/*      */ 
/*      */   public static ColorUIResource getBlack()
/*      */   {
/* 1829 */     return getCurrentTheme().getBlack();
/*      */   }
/*      */ 
/*      */   public static ColorUIResource getControl()
/*      */   {
/* 1839 */     return getCurrentTheme().getControl();
/*      */   }
/*      */ 
/*      */   public static ColorUIResource getControlShadow()
/*      */   {
/* 1849 */     return getCurrentTheme().getControlShadow();
/*      */   }
/*      */ 
/*      */   public static ColorUIResource getControlDarkShadow()
/*      */   {
/* 1859 */     return getCurrentTheme().getControlDarkShadow();
/*      */   }
/*      */ 
/*      */   public static ColorUIResource getControlInfo()
/*      */   {
/* 1869 */     return getCurrentTheme().getControlInfo();
/*      */   }
/*      */ 
/*      */   public static ColorUIResource getControlHighlight()
/*      */   {
/* 1879 */     return getCurrentTheme().getControlHighlight();
/*      */   }
/*      */ 
/*      */   public static ColorUIResource getControlDisabled()
/*      */   {
/* 1889 */     return getCurrentTheme().getControlDisabled();
/*      */   }
/*      */ 
/*      */   public static ColorUIResource getPrimaryControl()
/*      */   {
/* 1899 */     return getCurrentTheme().getPrimaryControl();
/*      */   }
/*      */ 
/*      */   public static ColorUIResource getPrimaryControlShadow()
/*      */   {
/* 1909 */     return getCurrentTheme().getPrimaryControlShadow();
/*      */   }
/*      */ 
/*      */   public static ColorUIResource getPrimaryControlDarkShadow()
/*      */   {
/* 1920 */     return getCurrentTheme().getPrimaryControlDarkShadow();
/*      */   }
/*      */ 
/*      */   public static ColorUIResource getPrimaryControlInfo()
/*      */   {
/* 1930 */     return getCurrentTheme().getPrimaryControlInfo();
/*      */   }
/*      */ 
/*      */   public static ColorUIResource getPrimaryControlHighlight()
/*      */   {
/* 1941 */     return getCurrentTheme().getPrimaryControlHighlight();
/*      */   }
/*      */ 
/*      */   public static ColorUIResource getSystemTextColor()
/*      */   {
/* 1951 */     return getCurrentTheme().getSystemTextColor();
/*      */   }
/*      */ 
/*      */   public static ColorUIResource getControlTextColor()
/*      */   {
/* 1961 */     return getCurrentTheme().getControlTextColor();
/*      */   }
/*      */ 
/*      */   public static ColorUIResource getInactiveControlTextColor()
/*      */   {
/* 1972 */     return getCurrentTheme().getInactiveControlTextColor();
/*      */   }
/*      */ 
/*      */   public static ColorUIResource getInactiveSystemTextColor()
/*      */   {
/* 1983 */     return getCurrentTheme().getInactiveSystemTextColor();
/*      */   }
/*      */ 
/*      */   public static ColorUIResource getUserTextColor()
/*      */   {
/* 1993 */     return getCurrentTheme().getUserTextColor();
/*      */   }
/*      */ 
/*      */   public static ColorUIResource getTextHighlightColor()
/*      */   {
/* 2003 */     return getCurrentTheme().getTextHighlightColor();
/*      */   }
/*      */ 
/*      */   public static ColorUIResource getHighlightedTextColor()
/*      */   {
/* 2013 */     return getCurrentTheme().getHighlightedTextColor();
/*      */   }
/*      */ 
/*      */   public static ColorUIResource getWindowBackground()
/*      */   {
/* 2023 */     return getCurrentTheme().getWindowBackground();
/*      */   }
/*      */ 
/*      */   public static ColorUIResource getWindowTitleBackground()
/*      */   {
/* 2034 */     return getCurrentTheme().getWindowTitleBackground();
/*      */   }
/*      */ 
/*      */   public static ColorUIResource getWindowTitleForeground()
/*      */   {
/* 2045 */     return getCurrentTheme().getWindowTitleForeground();
/*      */   }
/*      */ 
/*      */   public static ColorUIResource getWindowTitleInactiveBackground()
/*      */   {
/* 2056 */     return getCurrentTheme().getWindowTitleInactiveBackground();
/*      */   }
/*      */ 
/*      */   public static ColorUIResource getWindowTitleInactiveForeground()
/*      */   {
/* 2067 */     return getCurrentTheme().getWindowTitleInactiveForeground();
/*      */   }
/*      */ 
/*      */   public static ColorUIResource getMenuBackground()
/*      */   {
/* 2077 */     return getCurrentTheme().getMenuBackground();
/*      */   }
/*      */ 
/*      */   public static ColorUIResource getMenuForeground()
/*      */   {
/* 2087 */     return getCurrentTheme().getMenuForeground();
/*      */   }
/*      */ 
/*      */   public static ColorUIResource getMenuSelectedBackground()
/*      */   {
/* 2098 */     return getCurrentTheme().getMenuSelectedBackground();
/*      */   }
/*      */ 
/*      */   public static ColorUIResource getMenuSelectedForeground()
/*      */   {
/* 2109 */     return getCurrentTheme().getMenuSelectedForeground();
/*      */   }
/*      */ 
/*      */   public static ColorUIResource getMenuDisabledForeground()
/*      */   {
/* 2120 */     return getCurrentTheme().getMenuDisabledForeground();
/*      */   }
/*      */ 
/*      */   public static ColorUIResource getSeparatorBackground()
/*      */   {
/* 2130 */     return getCurrentTheme().getSeparatorBackground();
/*      */   }
/*      */ 
/*      */   public static ColorUIResource getSeparatorForeground()
/*      */   {
/* 2140 */     return getCurrentTheme().getSeparatorForeground();
/*      */   }
/*      */ 
/*      */   public static ColorUIResource getAcceleratorForeground()
/*      */   {
/* 2150 */     return getCurrentTheme().getAcceleratorForeground();
/*      */   }
/*      */ 
/*      */   public static ColorUIResource getAcceleratorSelectedForeground()
/*      */   {
/* 2161 */     return getCurrentTheme().getAcceleratorSelectedForeground();
/*      */   }
/*      */ 
/*      */   public LayoutStyle getLayoutStyle()
/*      */   {
/* 2174 */     return MetalLayoutStyle.INSTANCE;
/*      */   }
/*      */ 
/*      */   static void flushUnreferenced()
/*      */   {
/*      */     AATextListener localAATextListener;
/* 2275 */     while ((localAATextListener = (AATextListener)queue.poll()) != null)
/* 2276 */       localAATextListener.dispose();
/*      */   }
/*      */ 
/*      */   static class AATextListener extends WeakReference<LookAndFeel> implements PropertyChangeListener
/*      */   {
/* 2283 */     private String key = "awt.font.desktophints";
/*      */     private static boolean updatePending;
/*      */ 
/*      */     AATextListener(LookAndFeel paramLookAndFeel) {
/* 2286 */       super(MetalLookAndFeel.queue);
/* 2287 */       Toolkit localToolkit = Toolkit.getDefaultToolkit();
/* 2288 */       localToolkit.addPropertyChangeListener(this.key, this);
/*      */     }
/*      */ 
/*      */     public void propertyChange(PropertyChangeEvent paramPropertyChangeEvent) {
/* 2292 */       LookAndFeel localLookAndFeel = (LookAndFeel)get();
/* 2293 */       if ((localLookAndFeel == null) || (localLookAndFeel != UIManager.getLookAndFeel())) {
/* 2294 */         dispose();
/* 2295 */         return;
/*      */       }
/* 2297 */       UIDefaults localUIDefaults = UIManager.getLookAndFeelDefaults();
/* 2298 */       boolean bool = SwingUtilities2.isLocalDisplay();
/* 2299 */       SwingUtilities2.AATextInfo localAATextInfo = SwingUtilities2.AATextInfo.getAATextInfo(bool);
/*      */ 
/* 2301 */       localUIDefaults.put(SwingUtilities2.AA_TEXT_PROPERTY_KEY, localAATextInfo);
/* 2302 */       updateUI();
/*      */     }
/*      */ 
/*      */     void dispose() {
/* 2306 */       Toolkit localToolkit = Toolkit.getDefaultToolkit();
/* 2307 */       localToolkit.removePropertyChangeListener(this.key, this);
/*      */     }
/*      */ 
/*      */     private static void updateWindowUI(Window paramWindow)
/*      */     {
/* 2314 */       SwingUtilities.updateComponentTreeUI(paramWindow);
/* 2315 */       Window[] arrayOfWindow1 = paramWindow.getOwnedWindows();
/* 2316 */       for (Window localWindow : arrayOfWindow1)
/* 2317 */         updateWindowUI(localWindow);
/*      */     }
/*      */ 
/*      */     private static void updateAllUIs()
/*      */     {
/* 2325 */       Frame[] arrayOfFrame1 = Frame.getFrames();
/* 2326 */       for (Frame localFrame : arrayOfFrame1)
/* 2327 */         updateWindowUI(localFrame);
/*      */     }
/*      */ 
/*      */     private static synchronized void setUpdatePending(boolean paramBoolean)
/*      */     {
/* 2340 */       updatePending = paramBoolean;
/*      */     }
/*      */ 
/*      */     private static synchronized boolean isUpdatePending()
/*      */     {
/* 2347 */       return updatePending;
/*      */     }
/*      */ 
/*      */     protected void updateUI() {
/* 2351 */       if (!isUpdatePending()) {
/* 2352 */         setUpdatePending(true);
/* 2353 */         Runnable local1 = new Runnable() {
/*      */           public void run() {
/* 2355 */             MetalLookAndFeel.AATextListener.access$200();
/* 2356 */             MetalLookAndFeel.AATextListener.setUpdatePending(false);
/*      */           }
/*      */         };
/* 2359 */         SwingUtilities.invokeLater(local1);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class FontActiveValue
/*      */     implements UIDefaults.ActiveValue
/*      */   {
/*      */     private int type;
/*      */     private MetalTheme theme;
/*      */ 
/*      */     FontActiveValue(MetalTheme paramMetalTheme, int paramInt)
/*      */     {
/* 2241 */       this.theme = paramMetalTheme;
/* 2242 */       this.type = paramInt;
/*      */     }
/*      */ 
/*      */     public Object createValue(UIDefaults paramUIDefaults) {
/* 2246 */       FontUIResource localFontUIResource = null;
/* 2247 */       switch (this.type) {
/*      */       case 0:
/* 2249 */         localFontUIResource = this.theme.getControlTextFont();
/* 2250 */         break;
/*      */       case 1:
/* 2252 */         localFontUIResource = this.theme.getSystemTextFont();
/* 2253 */         break;
/*      */       case 2:
/* 2255 */         localFontUIResource = this.theme.getUserTextFont();
/* 2256 */         break;
/*      */       case 3:
/* 2258 */         localFontUIResource = this.theme.getMenuTextFont();
/* 2259 */         break;
/*      */       case 4:
/* 2261 */         localFontUIResource = this.theme.getWindowTitleFont();
/* 2262 */         break;
/*      */       case 5:
/* 2264 */         localFontUIResource = this.theme.getSubTextFont();
/*      */       }
/*      */ 
/* 2267 */       return localFontUIResource;
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class MetalLayoutStyle extends DefaultLayoutStyle
/*      */   {
/* 2367 */     private static MetalLayoutStyle INSTANCE = new MetalLayoutStyle();
/*      */ 
/*      */     public int getPreferredGap(JComponent paramJComponent1, JComponent paramJComponent2, LayoutStyle.ComponentPlacement paramComponentPlacement, int paramInt, Container paramContainer)
/*      */     {
/* 2374 */       super.getPreferredGap(paramJComponent1, paramJComponent2, paramComponentPlacement, paramInt, paramContainer);
/*      */ 
/* 2377 */       int i = 0;
/*      */ 
/* 2379 */       switch (MetalLookAndFeel.1.$SwitchMap$javax$swing$LayoutStyle$ComponentPlacement[paramComponentPlacement.ordinal()])
/*      */       {
/*      */       case 1:
/* 2382 */         if ((paramInt == 3) || (paramInt == 7))
/*      */         {
/* 2384 */           int j = getIndent(paramJComponent1, paramInt);
/* 2385 */           if (j > 0) {
/* 2386 */             return j;
/*      */           }
/* 2388 */           return 12;
/*      */         }
/*      */ 
/*      */       case 2:
/* 2392 */         if ((paramJComponent1.getUIClassID() == "ToggleButtonUI") && (paramJComponent2.getUIClassID() == "ToggleButtonUI"))
/*      */         {
/* 2394 */           ButtonModel localButtonModel1 = ((JToggleButton)paramJComponent1).getModel();
/*      */ 
/* 2396 */           ButtonModel localButtonModel2 = ((JToggleButton)paramJComponent2).getModel();
/*      */ 
/* 2398 */           if (((localButtonModel1 instanceof DefaultButtonModel)) && ((localButtonModel2 instanceof DefaultButtonModel)) && (((DefaultButtonModel)localButtonModel1).getGroup() == ((DefaultButtonModel)localButtonModel2).getGroup()) && (((DefaultButtonModel)localButtonModel1).getGroup() != null))
/*      */           {
/* 2411 */             return 2;
/*      */           }
/*      */ 
/* 2416 */           if (MetalLookAndFeel.usingOcean()) {
/* 2417 */             return 6;
/*      */           }
/* 2419 */           return 5;
/*      */         }
/* 2421 */         i = 6;
/* 2422 */         break;
/*      */       case 3:
/* 2424 */         i = 12;
/*      */       }
/*      */ 
/* 2427 */       if (isLabelAndNonlabel(paramJComponent1, paramJComponent2, paramInt))
/*      */       {
/* 2435 */         return getButtonGap(paramJComponent1, paramJComponent2, paramInt, i + 6);
/*      */       }
/*      */ 
/* 2438 */       return getButtonGap(paramJComponent1, paramJComponent2, paramInt, i);
/*      */     }
/*      */ 
/*      */     public int getContainerGap(JComponent paramJComponent, int paramInt, Container paramContainer)
/*      */     {
/* 2444 */       super.getContainerGap(paramJComponent, paramInt, paramContainer);
/*      */ 
/* 2461 */       return getButtonGap(paramJComponent, paramInt, 12 - getButtonAdjustment(paramJComponent, paramInt));
/*      */     }
/*      */ 
/*      */     protected int getButtonGap(JComponent paramJComponent1, JComponent paramJComponent2, int paramInt1, int paramInt2)
/*      */     {
/* 2468 */       paramInt2 = super.getButtonGap(paramJComponent1, paramJComponent2, paramInt1, paramInt2);
/* 2469 */       if (paramInt2 > 0) {
/* 2470 */         int i = getButtonAdjustment(paramJComponent1, paramInt1);
/* 2471 */         if (i == 0) {
/* 2472 */           i = getButtonAdjustment(paramJComponent2, flipDirection(paramInt1));
/*      */         }
/*      */ 
/* 2475 */         paramInt2 -= i;
/*      */       }
/* 2477 */       if (paramInt2 < 0) {
/* 2478 */         return 0;
/*      */       }
/* 2480 */       return paramInt2;
/*      */     }
/*      */ 
/*      */     private int getButtonAdjustment(JComponent paramJComponent, int paramInt) {
/* 2484 */       String str = paramJComponent.getUIClassID();
/* 2485 */       if ((str == "ButtonUI") || (str == "ToggleButtonUI")) {
/* 2486 */         if ((!MetalLookAndFeel.usingOcean()) && ((paramInt == 3) || (paramInt == 5)))
/*      */         {
/* 2488 */           if ((paramJComponent.getBorder() instanceof UIResource)) {
/* 2489 */             return 1;
/*      */           }
/*      */         }
/*      */       }
/* 2493 */       else if ((paramInt == 5) && 
/* 2494 */         ((str == "RadioButtonUI") || (str == "CheckBoxUI")) && (!MetalLookAndFeel.usingOcean()))
/*      */       {
/* 2496 */         return 1;
/*      */       }
/*      */ 
/* 2499 */       return 0;
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class MetalLazyValue
/*      */     implements UIDefaults.LazyValue
/*      */   {
/*      */     private String className;
/*      */     private String methodName;
/*      */ 
/*      */     MetalLazyValue(String paramString)
/*      */     {
/* 2191 */       this.className = paramString;
/*      */     }
/*      */ 
/*      */     MetalLazyValue(String paramString1, String paramString2) {
/* 2195 */       this(paramString1);
/* 2196 */       this.methodName = paramString2;
/*      */     }
/*      */ 
/*      */     public Object createValue(UIDefaults paramUIDefaults) {
/*      */       try {
/* 2201 */         final Class localClass = Class.forName(this.className);
/*      */ 
/* 2203 */         if (this.methodName == null) {
/* 2204 */           return localClass.newInstance();
/*      */         }
/* 2206 */         Method localMethod = (Method)AccessController.doPrivileged(new PrivilegedAction()
/*      */         {
/*      */           public Method run() {
/* 2209 */             Method[] arrayOfMethod = localClass.getDeclaredMethods();
/* 2210 */             for (int i = arrayOfMethod.length - 1; i >= 0; 
/* 2211 */               i--) {
/* 2212 */               if (arrayOfMethod[i].getName().equals(MetalLookAndFeel.MetalLazyValue.this.methodName)) {
/* 2213 */                 arrayOfMethod[i].setAccessible(true);
/* 2214 */                 return arrayOfMethod[i];
/*      */               }
/*      */             }
/* 2217 */             return null;
/*      */           }
/*      */         });
/* 2220 */         if (localMethod != null)
/* 2221 */           return localMethod.invoke(null, (Object[])null);
/*      */       } catch (ClassNotFoundException localClassNotFoundException) {
/*      */       } catch (InstantiationException localInstantiationException) {
/*      */       } catch (IllegalAccessException localIllegalAccessException) {
/*      */       }
/*      */       catch (InvocationTargetException localInvocationTargetException) {
/*      */       }
/* 2228 */       return null;
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.metal.MetalLookAndFeel
 * JD-Core Version:    0.6.2
 */