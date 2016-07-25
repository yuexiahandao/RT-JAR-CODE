/*      */ package com.sun.java.swing.plaf.windows;
/*      */ 
/*      */ import java.awt.Color;
/*      */ import java.awt.Component;
/*      */ import java.awt.Container;
/*      */ import java.awt.Dimension;
/*      */ import java.awt.Font;
/*      */ import java.awt.FontMetrics;
/*      */ import java.awt.Graphics;
/*      */ import java.awt.HeadlessException;
/*      */ import java.awt.Image;
/*      */ import java.awt.Insets;
/*      */ import java.awt.KeyboardFocusManager;
/*      */ import java.awt.Toolkit;
/*      */ import java.awt.event.ActionEvent;
/*      */ import java.awt.image.BufferedImage;
/*      */ import java.awt.image.FilteredImageSource;
/*      */ import java.awt.image.RGBImageFilter;
/*      */ import java.security.AccessController;
/*      */ import javax.swing.AbstractAction;
/*      */ import javax.swing.Action;
/*      */ import javax.swing.BorderFactory;
/*      */ import javax.swing.Icon;
/*      */ import javax.swing.ImageIcon;
/*      */ import javax.swing.JComponent;
/*      */ import javax.swing.JRootPane;
/*      */ import javax.swing.LayoutStyle;
/*      */ import javax.swing.LayoutStyle.ComponentPlacement;
/*      */ import javax.swing.LookAndFeel;
/*      */ import javax.swing.MenuSelectionManager;
/*      */ import javax.swing.UIDefaults;
/*      */ import javax.swing.UIDefaults.ActiveValue;
/*      */ import javax.swing.UIDefaults.LazyInputMap;
/*      */ import javax.swing.UIDefaults.LazyValue;
/*      */ import javax.swing.UIManager;
/*      */ import javax.swing.border.Border;
/*      */ import javax.swing.border.EmptyBorder;
/*      */ import javax.swing.plaf.BorderUIResource.CompoundBorderUIResource;
/*      */ import javax.swing.plaf.ColorUIResource;
/*      */ import javax.swing.plaf.FontUIResource;
/*      */ import javax.swing.plaf.InsetsUIResource;
/*      */ import javax.swing.plaf.UIResource;
/*      */ import javax.swing.plaf.basic.BasicLookAndFeel;
/*      */ import sun.awt.OSInfo;
/*      */ import sun.awt.OSInfo.OSType;
/*      */ import sun.awt.OSInfo.WindowsVersion;
/*      */ import sun.awt.shell.ShellFolder;
/*      */ import sun.font.FontUtilities;
/*      */ import sun.security.action.GetPropertyAction;
/*      */ import sun.swing.DefaultLayoutStyle;
/*      */ import sun.swing.ImageIconUIResource;
/*      */ import sun.swing.StringUIClientPropertyKey;
/*      */ import sun.swing.SwingLazyValue;
/*      */ import sun.swing.SwingUtilities2;
/*      */ import sun.swing.SwingUtilities2.AATextInfo;
/*      */ 
/*      */ public class WindowsLookAndFeel extends BasicLookAndFeel
/*      */ {
/*  103 */   static final Object HI_RES_DISABLED_ICON_CLIENT_KEY = new StringUIClientPropertyKey("WindowsLookAndFeel.generateHiResDisabledIcon");
/*      */   private boolean updatePending;
/*      */   private boolean useSystemFontSettings;
/*      */   private boolean useSystemFontSizeSettings;
/*      */   private DesktopProperty themeActive;
/*      */   private DesktopProperty dllName;
/*      */   private DesktopProperty colorName;
/*      */   private DesktopProperty sizeName;
/*      */   private DesktopProperty aaSettings;
/*      */   private transient LayoutStyle style;
/*      */   private int baseUnitX;
/*      */   private int baseUnitY;
/* 1948 */   private static boolean isMnemonicHidden = true;
/*      */ 
/* 1952 */   private static boolean isClassicWindows = false;
/*      */ 
/*      */   public WindowsLookAndFeel()
/*      */   {
/*  107 */     this.updatePending = false;
/*      */ 
/*  109 */     this.useSystemFontSettings = true;
/*      */   }
/*      */ 
/*      */   public String getName()
/*      */   {
/*  130 */     return "Windows";
/*      */   }
/*      */ 
/*      */   public String getDescription() {
/*  134 */     return "The Microsoft Windows Look and Feel";
/*      */   }
/*      */ 
/*      */   public String getID() {
/*  138 */     return "Windows";
/*      */   }
/*      */ 
/*      */   public boolean isNativeLookAndFeel() {
/*  142 */     return OSInfo.getOSType() == OSInfo.OSType.WINDOWS;
/*      */   }
/*      */ 
/*      */   public boolean isSupportedLookAndFeel() {
/*  146 */     return isNativeLookAndFeel();
/*      */   }
/*      */ 
/*      */   public void initialize() {
/*  150 */     super.initialize();
/*      */ 
/*  155 */     if (OSInfo.getWindowsVersion().compareTo(OSInfo.WINDOWS_95) <= 0) {
/*  156 */       isClassicWindows = true;
/*      */     } else {
/*  158 */       isClassicWindows = false;
/*  159 */       XPStyle.invalidateStyle();
/*      */     }
/*      */ 
/*  166 */     String str = (String)AccessController.doPrivileged(new GetPropertyAction("swing.useSystemFontSettings"));
/*      */ 
/*  168 */     this.useSystemFontSettings = ((str == null) || (Boolean.valueOf(str).booleanValue()));
/*      */ 
/*  171 */     if (this.useSystemFontSettings) {
/*  172 */       Object localObject = UIManager.get("Application.useSystemFontSettings");
/*      */ 
/*  174 */       this.useSystemFontSettings = ((localObject == null) || (Boolean.TRUE.equals(localObject)));
/*      */     }
/*      */ 
/*  177 */     KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventPostProcessor(WindowsRootPaneUI.altProcessor);
/*      */   }
/*      */ 
/*      */   protected void initClassDefaults(UIDefaults paramUIDefaults)
/*      */   {
/*  193 */     super.initClassDefaults(paramUIDefaults);
/*      */ 
/*  197 */     Object[] arrayOfObject = { "ButtonUI", "com.sun.java.swing.plaf.windows.WindowsButtonUI", "CheckBoxUI", "com.sun.java.swing.plaf.windows.WindowsCheckBoxUI", "CheckBoxMenuItemUI", "com.sun.java.swing.plaf.windows.WindowsCheckBoxMenuItemUI", "LabelUI", "com.sun.java.swing.plaf.windows.WindowsLabelUI", "RadioButtonUI", "com.sun.java.swing.plaf.windows.WindowsRadioButtonUI", "RadioButtonMenuItemUI", "com.sun.java.swing.plaf.windows.WindowsRadioButtonMenuItemUI", "ToggleButtonUI", "com.sun.java.swing.plaf.windows.WindowsToggleButtonUI", "ProgressBarUI", "com.sun.java.swing.plaf.windows.WindowsProgressBarUI", "SliderUI", "com.sun.java.swing.plaf.windows.WindowsSliderUI", "SeparatorUI", "com.sun.java.swing.plaf.windows.WindowsSeparatorUI", "SplitPaneUI", "com.sun.java.swing.plaf.windows.WindowsSplitPaneUI", "SpinnerUI", "com.sun.java.swing.plaf.windows.WindowsSpinnerUI", "TabbedPaneUI", "com.sun.java.swing.plaf.windows.WindowsTabbedPaneUI", "TextAreaUI", "com.sun.java.swing.plaf.windows.WindowsTextAreaUI", "TextFieldUI", "com.sun.java.swing.plaf.windows.WindowsTextFieldUI", "PasswordFieldUI", "com.sun.java.swing.plaf.windows.WindowsPasswordFieldUI", "TextPaneUI", "com.sun.java.swing.plaf.windows.WindowsTextPaneUI", "EditorPaneUI", "com.sun.java.swing.plaf.windows.WindowsEditorPaneUI", "TreeUI", "com.sun.java.swing.plaf.windows.WindowsTreeUI", "ToolBarUI", "com.sun.java.swing.plaf.windows.WindowsToolBarUI", "ToolBarSeparatorUI", "com.sun.java.swing.plaf.windows.WindowsToolBarSeparatorUI", "ComboBoxUI", "com.sun.java.swing.plaf.windows.WindowsComboBoxUI", "TableHeaderUI", "com.sun.java.swing.plaf.windows.WindowsTableHeaderUI", "InternalFrameUI", "com.sun.java.swing.plaf.windows.WindowsInternalFrameUI", "DesktopPaneUI", "com.sun.java.swing.plaf.windows.WindowsDesktopPaneUI", "DesktopIconUI", "com.sun.java.swing.plaf.windows.WindowsDesktopIconUI", "FileChooserUI", "com.sun.java.swing.plaf.windows.WindowsFileChooserUI", "MenuUI", "com.sun.java.swing.plaf.windows.WindowsMenuUI", "MenuItemUI", "com.sun.java.swing.plaf.windows.WindowsMenuItemUI", "MenuBarUI", "com.sun.java.swing.plaf.windows.WindowsMenuBarUI", "PopupMenuUI", "com.sun.java.swing.plaf.windows.WindowsPopupMenuUI", "PopupMenuSeparatorUI", "com.sun.java.swing.plaf.windows.WindowsPopupMenuSeparatorUI", "ScrollBarUI", "com.sun.java.swing.plaf.windows.WindowsScrollBarUI", "RootPaneUI", "com.sun.java.swing.plaf.windows.WindowsRootPaneUI" };
/*      */ 
/*  234 */     paramUIDefaults.putDefaults(arrayOfObject);
/*      */   }
/*      */ 
/*      */   protected void initSystemColorDefaults(UIDefaults paramUIDefaults)
/*      */   {
/*  247 */     String[] arrayOfString = { "desktop", "#005C5C", "activeCaption", "#000080", "activeCaptionText", "#FFFFFF", "activeCaptionBorder", "#C0C0C0", "inactiveCaption", "#808080", "inactiveCaptionText", "#C0C0C0", "inactiveCaptionBorder", "#C0C0C0", "window", "#FFFFFF", "windowBorder", "#000000", "windowText", "#000000", "menu", "#C0C0C0", "menuPressedItemB", "#000080", "menuPressedItemF", "#FFFFFF", "menuText", "#000000", "text", "#C0C0C0", "textText", "#000000", "textHighlight", "#000080", "textHighlightText", "#FFFFFF", "textInactiveText", "#808080", "control", "#C0C0C0", "controlText", "#000000", "controlHighlight", "#C0C0C0", "controlLtHighlight", "#FFFFFF", "controlShadow", "#808080", "controlDkShadow", "#000000", "scrollbar", "#E0E0E0", "info", "#FFFFE1", "infoText", "#000000" };
/*      */ 
/*  280 */     loadSystemColors(paramUIDefaults, arrayOfString, isNativeLookAndFeel());
/*      */   }
/*      */ 
/*      */   private void initResourceBundle(UIDefaults paramUIDefaults)
/*      */   {
/*  288 */     paramUIDefaults.addResourceBundle("com.sun.java.swing.plaf.windows.resources.windows");
/*      */   }
/*      */ 
/*      */   protected void initComponentDefaults(UIDefaults paramUIDefaults)
/*      */   {
/*  295 */     super.initComponentDefaults(paramUIDefaults);
/*      */ 
/*  297 */     initResourceBundle(paramUIDefaults);
/*      */ 
/*  300 */     Integer localInteger1 = Integer.valueOf(12);
/*  301 */     Integer localInteger2 = Integer.valueOf(0);
/*  302 */     Integer localInteger3 = Integer.valueOf(1);
/*      */ 
/*  304 */     SwingLazyValue localSwingLazyValue1 = new SwingLazyValue("javax.swing.plaf.FontUIResource", null, new Object[] { "Dialog", localInteger2, localInteger1 });
/*      */ 
/*  309 */     SwingLazyValue localSwingLazyValue2 = new SwingLazyValue("javax.swing.plaf.FontUIResource", null, new Object[] { "SansSerif", localInteger2, localInteger1 });
/*      */ 
/*  313 */     SwingLazyValue localSwingLazyValue3 = new SwingLazyValue("javax.swing.plaf.FontUIResource", null, new Object[] { "Monospaced", localInteger2, localInteger1 });
/*      */ 
/*  317 */     SwingLazyValue localSwingLazyValue4 = new SwingLazyValue("javax.swing.plaf.FontUIResource", null, new Object[] { "Dialog", localInteger3, localInteger1 });
/*      */ 
/*  324 */     ColorUIResource localColorUIResource1 = new ColorUIResource(Color.red);
/*  325 */     ColorUIResource localColorUIResource2 = new ColorUIResource(Color.black);
/*  326 */     ColorUIResource localColorUIResource3 = new ColorUIResource(Color.white);
/*  327 */     ColorUIResource localColorUIResource4 = new ColorUIResource(Color.gray);
/*  328 */     ColorUIResource localColorUIResource5 = new ColorUIResource(Color.darkGray);
/*  329 */     ColorUIResource localColorUIResource6 = localColorUIResource5;
/*      */ 
/*  334 */     isClassicWindows = OSInfo.getWindowsVersion().compareTo(OSInfo.WINDOWS_95) <= 0;
/*      */ 
/*  337 */     Icon localIcon1 = WindowsTreeUI.ExpandedIcon.createExpandedIcon();
/*      */ 
/*  339 */     Icon localIcon2 = WindowsTreeUI.CollapsedIcon.createCollapsedIcon();
/*      */ 
/*  343 */     UIDefaults.LazyInputMap localLazyInputMap1 = new UIDefaults.LazyInputMap(new Object[] { "control C", "copy-to-clipboard", "control V", "paste-from-clipboard", "control X", "cut-to-clipboard", "COPY", "copy-to-clipboard", "PASTE", "paste-from-clipboard", "CUT", "cut-to-clipboard", "control INSERT", "copy-to-clipboard", "shift INSERT", "paste-from-clipboard", "shift DELETE", "cut-to-clipboard", "control A", "select-all", "control BACK_SLASH", "unselect", "shift LEFT", "selection-backward", "shift RIGHT", "selection-forward", "control LEFT", "caret-previous-word", "control RIGHT", "caret-next-word", "control shift LEFT", "selection-previous-word", "control shift RIGHT", "selection-next-word", "HOME", "caret-begin-line", "END", "caret-end-line", "shift HOME", "selection-begin-line", "shift END", "selection-end-line", "BACK_SPACE", "delete-previous", "shift BACK_SPACE", "delete-previous", "ctrl H", "delete-previous", "DELETE", "delete-next", "ctrl DELETE", "delete-next-word", "ctrl BACK_SPACE", "delete-previous-word", "RIGHT", "caret-forward", "LEFT", "caret-backward", "KP_RIGHT", "caret-forward", "KP_LEFT", "caret-backward", "ENTER", "notify-field-accept", "control shift O", "toggle-componentOrientation" });
/*      */ 
/*  379 */     UIDefaults.LazyInputMap localLazyInputMap2 = new UIDefaults.LazyInputMap(new Object[] { "control C", "copy-to-clipboard", "control V", "paste-from-clipboard", "control X", "cut-to-clipboard", "COPY", "copy-to-clipboard", "PASTE", "paste-from-clipboard", "CUT", "cut-to-clipboard", "control INSERT", "copy-to-clipboard", "shift INSERT", "paste-from-clipboard", "shift DELETE", "cut-to-clipboard", "control A", "select-all", "control BACK_SLASH", "unselect", "shift LEFT", "selection-backward", "shift RIGHT", "selection-forward", "control LEFT", "caret-begin-line", "control RIGHT", "caret-end-line", "control shift LEFT", "selection-begin-line", "control shift RIGHT", "selection-end-line", "HOME", "caret-begin-line", "END", "caret-end-line", "shift HOME", "selection-begin-line", "shift END", "selection-end-line", "BACK_SPACE", "delete-previous", "shift BACK_SPACE", "delete-previous", "ctrl H", "delete-previous", "DELETE", "delete-next", "RIGHT", "caret-forward", "LEFT", "caret-backward", "KP_RIGHT", "caret-forward", "KP_LEFT", "caret-backward", "ENTER", "notify-field-accept", "control shift O", "toggle-componentOrientation" });
/*      */ 
/*  413 */     UIDefaults.LazyInputMap localLazyInputMap3 = new UIDefaults.LazyInputMap(new Object[] { "control C", "copy-to-clipboard", "control V", "paste-from-clipboard", "control X", "cut-to-clipboard", "COPY", "copy-to-clipboard", "PASTE", "paste-from-clipboard", "CUT", "cut-to-clipboard", "control INSERT", "copy-to-clipboard", "shift INSERT", "paste-from-clipboard", "shift DELETE", "cut-to-clipboard", "shift LEFT", "selection-backward", "shift RIGHT", "selection-forward", "control LEFT", "caret-previous-word", "control RIGHT", "caret-next-word", "control shift LEFT", "selection-previous-word", "control shift RIGHT", "selection-next-word", "control A", "select-all", "control BACK_SLASH", "unselect", "HOME", "caret-begin-line", "END", "caret-end-line", "shift HOME", "selection-begin-line", "shift END", "selection-end-line", "control HOME", "caret-begin", "control END", "caret-end", "control shift HOME", "selection-begin", "control shift END", "selection-end", "UP", "caret-up", "DOWN", "caret-down", "BACK_SPACE", "delete-previous", "shift BACK_SPACE", "delete-previous", "ctrl H", "delete-previous", "DELETE", "delete-next", "ctrl DELETE", "delete-next-word", "ctrl BACK_SPACE", "delete-previous-word", "RIGHT", "caret-forward", "LEFT", "caret-backward", "KP_RIGHT", "caret-forward", "KP_LEFT", "caret-backward", "PAGE_UP", "page-up", "PAGE_DOWN", "page-down", "shift PAGE_UP", "selection-page-up", "shift PAGE_DOWN", "selection-page-down", "ctrl shift PAGE_UP", "selection-page-left", "ctrl shift PAGE_DOWN", "selection-page-right", "shift UP", "selection-up", "shift DOWN", "selection-down", "ENTER", "insert-break", "TAB", "insert-tab", "control T", "next-link-action", "control shift T", "previous-link-action", "control SPACE", "activate-link-action", "control shift O", "toggle-componentOrientation" });
/*      */ 
/*  467 */     String str = "+";
/*      */ 
/*  469 */     DesktopProperty localDesktopProperty1 = new DesktopProperty("win.3d.backgroundColor", paramUIDefaults.get("control"));
/*      */ 
/*  472 */     DesktopProperty localDesktopProperty2 = new DesktopProperty("win.3d.lightColor", paramUIDefaults.get("controlHighlight"));
/*      */ 
/*  475 */     DesktopProperty localDesktopProperty3 = new DesktopProperty("win.3d.highlightColor", paramUIDefaults.get("controlLtHighlight"));
/*      */ 
/*  478 */     DesktopProperty localDesktopProperty4 = new DesktopProperty("win.3d.shadowColor", paramUIDefaults.get("controlShadow"));
/*      */ 
/*  481 */     DesktopProperty localDesktopProperty5 = new DesktopProperty("win.3d.darkShadowColor", paramUIDefaults.get("controlDkShadow"));
/*      */ 
/*  484 */     DesktopProperty localDesktopProperty6 = new DesktopProperty("win.button.textColor", paramUIDefaults.get("controlText"));
/*      */ 
/*  487 */     DesktopProperty localDesktopProperty7 = new DesktopProperty("win.menu.backgroundColor", paramUIDefaults.get("menu"));
/*      */ 
/*  490 */     DesktopProperty localDesktopProperty8 = new DesktopProperty("win.menubar.backgroundColor", paramUIDefaults.get("menu"));
/*      */ 
/*  493 */     DesktopProperty localDesktopProperty9 = new DesktopProperty("win.menu.textColor", paramUIDefaults.get("menuText"));
/*      */ 
/*  496 */     DesktopProperty localDesktopProperty10 = new DesktopProperty("win.item.highlightColor", paramUIDefaults.get("textHighlight"));
/*      */ 
/*  499 */     DesktopProperty localDesktopProperty11 = new DesktopProperty("win.item.highlightTextColor", paramUIDefaults.get("textHighlightText"));
/*      */ 
/*  502 */     DesktopProperty localDesktopProperty12 = new DesktopProperty("win.frame.backgroundColor", paramUIDefaults.get("window"));
/*      */ 
/*  505 */     DesktopProperty localDesktopProperty13 = new DesktopProperty("win.frame.textColor", paramUIDefaults.get("windowText"));
/*      */ 
/*  508 */     DesktopProperty localDesktopProperty14 = new DesktopProperty("win.frame.sizingBorderWidth", Integer.valueOf(1));
/*      */ 
/*  511 */     DesktopProperty localDesktopProperty15 = new DesktopProperty("win.frame.captionHeight", Integer.valueOf(18));
/*      */ 
/*  514 */     DesktopProperty localDesktopProperty16 = new DesktopProperty("win.frame.captionButtonWidth", Integer.valueOf(16));
/*      */ 
/*  517 */     DesktopProperty localDesktopProperty17 = new DesktopProperty("win.frame.captionButtonHeight", Integer.valueOf(16));
/*      */ 
/*  520 */     DesktopProperty localDesktopProperty18 = new DesktopProperty("win.text.grayedTextColor", paramUIDefaults.get("textInactiveText"));
/*      */ 
/*  523 */     DesktopProperty localDesktopProperty19 = new DesktopProperty("win.scrollbar.backgroundColor", paramUIDefaults.get("scrollbar"));
/*      */ 
/*  526 */     FocusColorProperty localFocusColorProperty = new FocusColorProperty();
/*      */ 
/*  528 */     XPColorValue localXPColorValue = new XPColorValue(TMSchema.Part.EP_EDIT, null, TMSchema.Prop.FILLCOLOR, localDesktopProperty12);
/*      */ 
/*  538 */     DesktopProperty localDesktopProperty20 = localDesktopProperty1;
/*  539 */     DesktopProperty localDesktopProperty21 = localDesktopProperty1;
/*      */ 
/*  541 */     Object localObject1 = localSwingLazyValue1;
/*  542 */     Object localObject2 = localSwingLazyValue3;
/*  543 */     Object localObject3 = localSwingLazyValue1;
/*  544 */     Object localObject4 = localSwingLazyValue1;
/*  545 */     Object localObject5 = localSwingLazyValue4;
/*  546 */     Object localObject6 = localSwingLazyValue2;
/*  547 */     Object localObject7 = localObject3;
/*      */ 
/*  549 */     DesktopProperty localDesktopProperty22 = new DesktopProperty("win.scrollbar.width", Integer.valueOf(16));
/*      */ 
/*  551 */     DesktopProperty localDesktopProperty23 = new DesktopProperty("win.menu.height", null);
/*      */ 
/*  553 */     DesktopProperty localDesktopProperty24 = new DesktopProperty("win.item.hotTrackingOn", Boolean.valueOf(true));
/*      */ 
/*  555 */     DesktopProperty localDesktopProperty25 = new DesktopProperty("win.menu.keyboardCuesOn", Boolean.TRUE);
/*      */ 
/*  557 */     if (this.useSystemFontSettings) {
/*  558 */       localObject1 = getDesktopFontValue("win.menu.font", localObject1);
/*  559 */       localObject2 = getDesktopFontValue("win.ansiFixed.font", localObject2);
/*  560 */       localObject3 = getDesktopFontValue("win.defaultGUI.font", localObject3);
/*  561 */       localObject4 = getDesktopFontValue("win.messagebox.font", localObject4);
/*  562 */       localObject5 = getDesktopFontValue("win.frame.captionFont", localObject5);
/*  563 */       localObject7 = getDesktopFontValue("win.icon.font", localObject7);
/*  564 */       localObject6 = getDesktopFontValue("win.tooltip.font", localObject6);
/*      */ 
/*  572 */       localObject8 = SwingUtilities2.AATextInfo.getAATextInfo(true);
/*  573 */       paramUIDefaults.put(SwingUtilities2.AA_TEXT_PROPERTY_KEY, localObject8);
/*  574 */       this.aaSettings = new FontDesktopProperty("awt.font.desktophints");
/*      */     }
/*      */ 
/*  577 */     if (this.useSystemFontSizeSettings) {
/*  578 */       localObject1 = new WindowsFontSizeProperty("win.menu.font.height", "Dialog", 0, 12);
/*  579 */       localObject2 = new WindowsFontSizeProperty("win.ansiFixed.font.height", "Monospaced", 0, 12);
/*      */ 
/*  581 */       localObject3 = new WindowsFontSizeProperty("win.defaultGUI.font.height", "Dialog", 0, 12);
/*  582 */       localObject4 = new WindowsFontSizeProperty("win.messagebox.font.height", "Dialog", 0, 12);
/*  583 */       localObject5 = new WindowsFontSizeProperty("win.frame.captionFont.height", "Dialog", 1, 12);
/*  584 */       localObject6 = new WindowsFontSizeProperty("win.tooltip.font.height", "SansSerif", 0, 12);
/*  585 */       localObject7 = new WindowsFontSizeProperty("win.icon.font.height", "Dialog", 0, 12);
/*      */     }
/*      */ 
/*  589 */     if ((!(this instanceof WindowsClassicLookAndFeel)) && (OSInfo.getOSType() == OSInfo.OSType.WINDOWS) && (OSInfo.getWindowsVersion().compareTo(OSInfo.WINDOWS_XP) >= 0) && (AccessController.doPrivileged(new GetPropertyAction("swing.noxp")) == null))
/*      */     {
/*  596 */       this.themeActive = new TriggerDesktopProperty("win.xpstyle.themeActive");
/*  597 */       this.dllName = new TriggerDesktopProperty("win.xpstyle.dllName");
/*  598 */       this.colorName = new TriggerDesktopProperty("win.xpstyle.colorName");
/*  599 */       this.sizeName = new TriggerDesktopProperty("win.xpstyle.sizeName");
/*      */     }
/*      */ 
/*  603 */     Object localObject8 = { "AuditoryCues.playList", null, "Application.useSystemFontSettings", Boolean.valueOf(this.useSystemFontSettings), "TextField.focusInputMap", localLazyInputMap1, "PasswordField.focusInputMap", localLazyInputMap2, "TextArea.focusInputMap", localLazyInputMap3, "TextPane.focusInputMap", localLazyInputMap3, "EditorPane.focusInputMap", localLazyInputMap3, "Button.font", localObject3, "Button.background", localDesktopProperty1, "Button.foreground", localDesktopProperty6, "Button.shadow", localDesktopProperty4, "Button.darkShadow", localDesktopProperty5, "Button.light", localDesktopProperty2, "Button.highlight", localDesktopProperty3, "Button.disabledForeground", localDesktopProperty18, "Button.disabledShadow", localDesktopProperty3, "Button.focus", localFocusColorProperty, "Button.dashedRectGapX", new XPValue(Integer.valueOf(3), Integer.valueOf(5)), "Button.dashedRectGapY", new XPValue(Integer.valueOf(3), Integer.valueOf(4)), "Button.dashedRectGapWidth", new XPValue(Integer.valueOf(6), Integer.valueOf(10)), "Button.dashedRectGapHeight", new XPValue(Integer.valueOf(6), Integer.valueOf(8)), "Button.textShiftOffset", new XPValue(Integer.valueOf(0), Integer.valueOf(1)), "Button.showMnemonics", localDesktopProperty25, "Button.focusInputMap", new UIDefaults.LazyInputMap(new Object[] { "SPACE", "pressed", "released SPACE", "released" }), "Caret.width", new DesktopProperty("win.caret.width", null), "CheckBox.font", localObject3, "CheckBox.interiorBackground", localDesktopProperty12, "CheckBox.background", localDesktopProperty1, "CheckBox.foreground", localDesktopProperty13, "CheckBox.shadow", localDesktopProperty4, "CheckBox.darkShadow", localDesktopProperty5, "CheckBox.light", localDesktopProperty2, "CheckBox.highlight", localDesktopProperty3, "CheckBox.focus", localFocusColorProperty, "CheckBox.focusInputMap", new UIDefaults.LazyInputMap(new Object[] { "SPACE", "pressed", "released SPACE", "released" }), "CheckBox.totalInsets", new Insets(4, 4, 4, 4), "CheckBoxMenuItem.font", localObject1, "CheckBoxMenuItem.background", localDesktopProperty7, "CheckBoxMenuItem.foreground", localDesktopProperty9, "CheckBoxMenuItem.selectionForeground", localDesktopProperty11, "CheckBoxMenuItem.selectionBackground", localDesktopProperty10, "CheckBoxMenuItem.acceleratorForeground", localDesktopProperty9, "CheckBoxMenuItem.acceleratorSelectionForeground", localDesktopProperty11, "CheckBoxMenuItem.commandSound", "win.sound.menuCommand", "ComboBox.font", localObject3, "ComboBox.background", localDesktopProperty12, "ComboBox.foreground", localDesktopProperty13, "ComboBox.buttonBackground", localDesktopProperty1, "ComboBox.buttonShadow", localDesktopProperty4, "ComboBox.buttonDarkShadow", localDesktopProperty5, "ComboBox.buttonHighlight", localDesktopProperty3, "ComboBox.selectionBackground", localDesktopProperty10, "ComboBox.selectionForeground", localDesktopProperty11, "ComboBox.editorBorder", new XPValue(new EmptyBorder(1, 2, 1, 1), new EmptyBorder(1, 4, 1, 4)), "ComboBox.disabledBackground", new XPColorValue(TMSchema.Part.CP_COMBOBOX, TMSchema.State.DISABLED, TMSchema.Prop.FILLCOLOR, localDesktopProperty21), "ComboBox.disabledForeground", new XPColorValue(TMSchema.Part.CP_COMBOBOX, TMSchema.State.DISABLED, TMSchema.Prop.TEXTCOLOR, localDesktopProperty18), "ComboBox.ancestorInputMap", new UIDefaults.LazyInputMap(new Object[] { "ESCAPE", "hidePopup", "PAGE_UP", "pageUpPassThrough", "PAGE_DOWN", "pageDownPassThrough", "HOME", "homePassThrough", "END", "endPassThrough", "DOWN", "selectNext2", "KP_DOWN", "selectNext2", "UP", "selectPrevious2", "KP_UP", "selectPrevious2", "ENTER", "enterPressed", "F4", "togglePopup", "alt DOWN", "togglePopup", "alt KP_DOWN", "togglePopup", "alt UP", "togglePopup", "alt KP_UP", "togglePopup" }), "Desktop.background", new DesktopProperty("win.desktop.backgroundColor", paramUIDefaults.get("desktop")), "Desktop.ancestorInputMap", new UIDefaults.LazyInputMap(new Object[] { "ctrl F5", "restore", "ctrl F4", "close", "ctrl F7", "move", "ctrl F8", "resize", "RIGHT", "right", "KP_RIGHT", "right", "LEFT", "left", "KP_LEFT", "left", "UP", "up", "KP_UP", "up", "DOWN", "down", "KP_DOWN", "down", "ESCAPE", "escape", "ctrl F9", "minimize", "ctrl F10", "maximize", "ctrl F6", "selectNextFrame", "ctrl TAB", "selectNextFrame", "ctrl alt F6", "selectNextFrame", "shift ctrl alt F6", "selectPreviousFrame", "ctrl F12", "navigateNext", "shift ctrl F12", "navigatePrevious" }), "DesktopIcon.width", Integer.valueOf(160), "EditorPane.font", localObject3, "EditorPane.background", localDesktopProperty12, "EditorPane.foreground", localDesktopProperty13, "EditorPane.selectionBackground", localDesktopProperty10, "EditorPane.selectionForeground", localDesktopProperty11, "EditorPane.caretForeground", localDesktopProperty13, "EditorPane.inactiveForeground", localDesktopProperty18, "EditorPane.inactiveBackground", localDesktopProperty12, "EditorPane.disabledBackground", localDesktopProperty21, "FileChooser.homeFolderIcon", new LazyWindowsIcon(null, "icons/HomeFolder.gif"), "FileChooser.listFont", localObject7, "FileChooser.listViewBackground", new XPColorValue(TMSchema.Part.LVP_LISTVIEW, null, TMSchema.Prop.FILLCOLOR, localDesktopProperty12), "FileChooser.listViewBorder", new XPBorderValue(TMSchema.Part.LVP_LISTVIEW, new SwingLazyValue("javax.swing.plaf.BorderUIResource", "getLoweredBevelBorderUIResource")), "FileChooser.listViewIcon", new LazyWindowsIcon("fileChooserIcon ListView", "icons/ListView.gif"), "FileChooser.listViewWindowsStyle", Boolean.TRUE, "FileChooser.detailsViewIcon", new LazyWindowsIcon("fileChooserIcon DetailsView", "icons/DetailsView.gif"), "FileChooser.viewMenuIcon", new LazyWindowsIcon("fileChooserIcon ViewMenu", "icons/ListView.gif"), "FileChooser.upFolderIcon", new LazyWindowsIcon("fileChooserIcon UpFolder", "icons/UpFolder.gif"), "FileChooser.newFolderIcon", new LazyWindowsIcon("fileChooserIcon NewFolder", "icons/NewFolder.gif"), "FileChooser.useSystemExtensionHiding", Boolean.TRUE, "FileChooser.usesSingleFilePane", Boolean.TRUE, "FileChooser.noPlacesBar", new DesktopProperty("win.comdlg.noPlacesBar", Boolean.FALSE), "FileChooser.ancestorInputMap", new UIDefaults.LazyInputMap(new Object[] { "ESCAPE", "cancelSelection", "F2", "editFileName", "F5", "refresh", "BACK_SPACE", "Go Up" }), "FileView.directoryIcon", SwingUtilities2.makeIcon(getClass(), WindowsLookAndFeel.class, "icons/Directory.gif"), "FileView.fileIcon", SwingUtilities2.makeIcon(getClass(), WindowsLookAndFeel.class, "icons/File.gif"), "FileView.computerIcon", SwingUtilities2.makeIcon(getClass(), WindowsLookAndFeel.class, "icons/Computer.gif"), "FileView.hardDriveIcon", SwingUtilities2.makeIcon(getClass(), WindowsLookAndFeel.class, "icons/HardDrive.gif"), "FileView.floppyDriveIcon", SwingUtilities2.makeIcon(getClass(), WindowsLookAndFeel.class, "icons/FloppyDrive.gif"), "FormattedTextField.font", localObject3, "InternalFrame.titleFont", localObject5, "InternalFrame.titlePaneHeight", localDesktopProperty15, "InternalFrame.titleButtonWidth", localDesktopProperty16, "InternalFrame.titleButtonHeight", localDesktopProperty17, "InternalFrame.titleButtonToolTipsOn", localDesktopProperty24, "InternalFrame.borderColor", localDesktopProperty1, "InternalFrame.borderShadow", localDesktopProperty4, "InternalFrame.borderDarkShadow", localDesktopProperty5, "InternalFrame.borderHighlight", localDesktopProperty3, "InternalFrame.borderLight", localDesktopProperty2, "InternalFrame.borderWidth", localDesktopProperty14, "InternalFrame.minimizeIconBackground", localDesktopProperty1, "InternalFrame.resizeIconHighlight", localDesktopProperty2, "InternalFrame.resizeIconShadow", localDesktopProperty4, "InternalFrame.activeBorderColor", new DesktopProperty("win.frame.activeBorderColor", paramUIDefaults.get("windowBorder")), "InternalFrame.inactiveBorderColor", new DesktopProperty("win.frame.inactiveBorderColor", paramUIDefaults.get("windowBorder")), "InternalFrame.activeTitleBackground", new DesktopProperty("win.frame.activeCaptionColor", paramUIDefaults.get("activeCaption")), "InternalFrame.activeTitleGradient", new DesktopProperty("win.frame.activeCaptionGradientColor", paramUIDefaults.get("activeCaption")), "InternalFrame.activeTitleForeground", new DesktopProperty("win.frame.captionTextColor", paramUIDefaults.get("activeCaptionText")), "InternalFrame.inactiveTitleBackground", new DesktopProperty("win.frame.inactiveCaptionColor", paramUIDefaults.get("inactiveCaption")), "InternalFrame.inactiveTitleGradient", new DesktopProperty("win.frame.inactiveCaptionGradientColor", paramUIDefaults.get("inactiveCaption")), "InternalFrame.inactiveTitleForeground", new DesktopProperty("win.frame.inactiveCaptionTextColor", paramUIDefaults.get("inactiveCaptionText")), "InternalFrame.maximizeIcon", WindowsIconFactory.createFrameMaximizeIcon(), "InternalFrame.minimizeIcon", WindowsIconFactory.createFrameMinimizeIcon(), "InternalFrame.iconifyIcon", WindowsIconFactory.createFrameIconifyIcon(), "InternalFrame.closeIcon", WindowsIconFactory.createFrameCloseIcon(), "InternalFrame.icon", new SwingLazyValue("com.sun.java.swing.plaf.windows.WindowsInternalFrameTitlePane$ScalableIconUIResource", new Object[][] { { SwingUtilities2.makeIcon(getClass(), BasicLookAndFeel.class, "icons/JavaCup16.png"), SwingUtilities2.makeIcon(getClass(), WindowsLookAndFeel.class, "icons/JavaCup32.png") } }), "InternalFrame.closeSound", "win.sound.close", "InternalFrame.maximizeSound", "win.sound.maximize", "InternalFrame.minimizeSound", "win.sound.minimize", "InternalFrame.restoreDownSound", "win.sound.restoreDown", "InternalFrame.restoreUpSound", "win.sound.restoreUp", "InternalFrame.windowBindings", { "shift ESCAPE", "showSystemMenu", "ctrl SPACE", "showSystemMenu", "ESCAPE", "hideSystemMenu" }, "Label.font", localObject3, "Label.background", localDesktopProperty1, "Label.foreground", localDesktopProperty13, "Label.disabledForeground", localDesktopProperty18, "Label.disabledShadow", localDesktopProperty3, "List.font", localObject3, "List.background", localDesktopProperty12, "List.foreground", localDesktopProperty13, "List.selectionBackground", localDesktopProperty10, "List.selectionForeground", localDesktopProperty11, "List.lockToPositionOnScroll", Boolean.TRUE, "List.focusInputMap", new UIDefaults.LazyInputMap(new Object[] { "ctrl C", "copy", "ctrl V", "paste", "ctrl X", "cut", "COPY", "copy", "PASTE", "paste", "CUT", "cut", "control INSERT", "copy", "shift INSERT", "paste", "shift DELETE", "cut", "UP", "selectPreviousRow", "KP_UP", "selectPreviousRow", "shift UP", "selectPreviousRowExtendSelection", "shift KP_UP", "selectPreviousRowExtendSelection", "ctrl shift UP", "selectPreviousRowExtendSelection", "ctrl shift KP_UP", "selectPreviousRowExtendSelection", "ctrl UP", "selectPreviousRowChangeLead", "ctrl KP_UP", "selectPreviousRowChangeLead", "DOWN", "selectNextRow", "KP_DOWN", "selectNextRow", "shift DOWN", "selectNextRowExtendSelection", "shift KP_DOWN", "selectNextRowExtendSelection", "ctrl shift DOWN", "selectNextRowExtendSelection", "ctrl shift KP_DOWN", "selectNextRowExtendSelection", "ctrl DOWN", "selectNextRowChangeLead", "ctrl KP_DOWN", "selectNextRowChangeLead", "LEFT", "selectPreviousColumn", "KP_LEFT", "selectPreviousColumn", "shift LEFT", "selectPreviousColumnExtendSelection", "shift KP_LEFT", "selectPreviousColumnExtendSelection", "ctrl shift LEFT", "selectPreviousColumnExtendSelection", "ctrl shift KP_LEFT", "selectPreviousColumnExtendSelection", "ctrl LEFT", "selectPreviousColumnChangeLead", "ctrl KP_LEFT", "selectPreviousColumnChangeLead", "RIGHT", "selectNextColumn", "KP_RIGHT", "selectNextColumn", "shift RIGHT", "selectNextColumnExtendSelection", "shift KP_RIGHT", "selectNextColumnExtendSelection", "ctrl shift RIGHT", "selectNextColumnExtendSelection", "ctrl shift KP_RIGHT", "selectNextColumnExtendSelection", "ctrl RIGHT", "selectNextColumnChangeLead", "ctrl KP_RIGHT", "selectNextColumnChangeLead", "HOME", "selectFirstRow", "shift HOME", "selectFirstRowExtendSelection", "ctrl shift HOME", "selectFirstRowExtendSelection", "ctrl HOME", "selectFirstRowChangeLead", "END", "selectLastRow", "shift END", "selectLastRowExtendSelection", "ctrl shift END", "selectLastRowExtendSelection", "ctrl END", "selectLastRowChangeLead", "PAGE_UP", "scrollUp", "shift PAGE_UP", "scrollUpExtendSelection", "ctrl shift PAGE_UP", "scrollUpExtendSelection", "ctrl PAGE_UP", "scrollUpChangeLead", "PAGE_DOWN", "scrollDown", "shift PAGE_DOWN", "scrollDownExtendSelection", "ctrl shift PAGE_DOWN", "scrollDownExtendSelection", "ctrl PAGE_DOWN", "scrollDownChangeLead", "ctrl A", "selectAll", "ctrl SLASH", "selectAll", "ctrl BACK_SLASH", "clearSelection", "SPACE", "addToSelection", "ctrl SPACE", "toggleAndAnchor", "shift SPACE", "extendTo", "ctrl shift SPACE", "moveSelectionTo" }), "PopupMenu.font", localObject1, "PopupMenu.background", localDesktopProperty7, "PopupMenu.foreground", localDesktopProperty9, "PopupMenu.popupSound", "win.sound.menuPopup", "PopupMenu.consumeEventOnClose", Boolean.TRUE, "Menu.font", localObject1, "Menu.foreground", localDesktopProperty9, "Menu.background", localDesktopProperty7, "Menu.useMenuBarBackgroundForTopLevel", Boolean.TRUE, "Menu.selectionForeground", localDesktopProperty11, "Menu.selectionBackground", localDesktopProperty10, "Menu.acceleratorForeground", localDesktopProperty9, "Menu.acceleratorSelectionForeground", localDesktopProperty11, "Menu.menuPopupOffsetX", Integer.valueOf(0), "Menu.menuPopupOffsetY", Integer.valueOf(0), "Menu.submenuPopupOffsetX", Integer.valueOf(-4), "Menu.submenuPopupOffsetY", Integer.valueOf(-3), "Menu.crossMenuMnemonic", Boolean.FALSE, "Menu.preserveTopLevelSelection", Boolean.TRUE, "MenuBar.font", localObject1, "MenuBar.background", new XPValue(localDesktopProperty8, localDesktopProperty7), "MenuBar.foreground", localDesktopProperty9, "MenuBar.shadow", localDesktopProperty4, "MenuBar.highlight", localDesktopProperty3, "MenuBar.height", localDesktopProperty23, "MenuBar.rolloverEnabled", localDesktopProperty24, "MenuBar.windowBindings", { "F10", "takeFocus" }, "MenuItem.font", localObject1, "MenuItem.acceleratorFont", localObject1, "MenuItem.foreground", localDesktopProperty9, "MenuItem.background", localDesktopProperty7, "MenuItem.selectionForeground", localDesktopProperty11, "MenuItem.selectionBackground", localDesktopProperty10, "MenuItem.disabledForeground", localDesktopProperty18, "MenuItem.acceleratorForeground", localDesktopProperty9, "MenuItem.acceleratorSelectionForeground", localDesktopProperty11, "MenuItem.acceleratorDelimiter", str, "MenuItem.commandSound", "win.sound.menuCommand", "MenuItem.disabledAreNavigable", Boolean.TRUE, "RadioButton.font", localObject3, "RadioButton.interiorBackground", localDesktopProperty12, "RadioButton.background", localDesktopProperty1, "RadioButton.foreground", localDesktopProperty13, "RadioButton.shadow", localDesktopProperty4, "RadioButton.darkShadow", localDesktopProperty5, "RadioButton.light", localDesktopProperty2, "RadioButton.highlight", localDesktopProperty3, "RadioButton.focus", localFocusColorProperty, "RadioButton.focusInputMap", new UIDefaults.LazyInputMap(new Object[] { "SPACE", "pressed", "released SPACE", "released" }), "RadioButton.totalInsets", new Insets(4, 4, 4, 4), "RadioButtonMenuItem.font", localObject1, "RadioButtonMenuItem.foreground", localDesktopProperty9, "RadioButtonMenuItem.background", localDesktopProperty7, "RadioButtonMenuItem.selectionForeground", localDesktopProperty11, "RadioButtonMenuItem.selectionBackground", localDesktopProperty10, "RadioButtonMenuItem.disabledForeground", localDesktopProperty18, "RadioButtonMenuItem.acceleratorForeground", localDesktopProperty9, "RadioButtonMenuItem.acceleratorSelectionForeground", localDesktopProperty11, "RadioButtonMenuItem.commandSound", "win.sound.menuCommand", "OptionPane.font", localObject4, "OptionPane.messageFont", localObject4, "OptionPane.buttonFont", localObject4, "OptionPane.background", localDesktopProperty1, "OptionPane.foreground", localDesktopProperty13, "OptionPane.buttonMinimumWidth", new XPDLUValue(50, 50, 3), "OptionPane.messageForeground", localDesktopProperty6, "OptionPane.errorIcon", new LazyWindowsIcon("optionPaneIcon Error", "icons/Error.gif"), "OptionPane.informationIcon", new LazyWindowsIcon("optionPaneIcon Information", "icons/Inform.gif"), "OptionPane.questionIcon", new LazyWindowsIcon("optionPaneIcon Question", "icons/Question.gif"), "OptionPane.warningIcon", new LazyWindowsIcon("optionPaneIcon Warning", "icons/Warn.gif"), "OptionPane.windowBindings", { "ESCAPE", "close" }, "OptionPane.errorSound", "win.sound.hand", "OptionPane.informationSound", "win.sound.asterisk", "OptionPane.questionSound", "win.sound.question", "OptionPane.warningSound", "win.sound.exclamation", "FormattedTextField.focusInputMap", new UIDefaults.LazyInputMap(new Object[] { "ctrl C", "copy-to-clipboard", "ctrl V", "paste-from-clipboard", "ctrl X", "cut-to-clipboard", "COPY", "copy-to-clipboard", "PASTE", "paste-from-clipboard", "CUT", "cut-to-clipboard", "control INSERT", "copy-to-clipboard", "shift INSERT", "paste-from-clipboard", "shift DELETE", "cut-to-clipboard", "shift LEFT", "selection-backward", "shift KP_LEFT", "selection-backward", "shift RIGHT", "selection-forward", "shift KP_RIGHT", "selection-forward", "ctrl LEFT", "caret-previous-word", "ctrl KP_LEFT", "caret-previous-word", "ctrl RIGHT", "caret-next-word", "ctrl KP_RIGHT", "caret-next-word", "ctrl shift LEFT", "selection-previous-word", "ctrl shift KP_LEFT", "selection-previous-word", "ctrl shift RIGHT", "selection-next-word", "ctrl shift KP_RIGHT", "selection-next-word", "ctrl A", "select-all", "HOME", "caret-begin-line", "END", "caret-end-line", "shift HOME", "selection-begin-line", "shift END", "selection-end-line", "BACK_SPACE", "delete-previous", "shift BACK_SPACE", "delete-previous", "ctrl H", "delete-previous", "DELETE", "delete-next", "ctrl DELETE", "delete-next-word", "ctrl BACK_SPACE", "delete-previous-word", "RIGHT", "caret-forward", "LEFT", "caret-backward", "KP_RIGHT", "caret-forward", "KP_LEFT", "caret-backward", "ENTER", "notify-field-accept", "ctrl BACK_SLASH", "unselect", "control shift O", "toggle-componentOrientation", "ESCAPE", "reset-field-edit", "UP", "increment", "KP_UP", "increment", "DOWN", "decrement", "KP_DOWN", "decrement" }), "FormattedTextField.inactiveBackground", localDesktopProperty20, "FormattedTextField.disabledBackground", localDesktopProperty21, "Panel.font", localObject3, "Panel.background", localDesktopProperty1, "Panel.foreground", localDesktopProperty13, "PasswordField.font", localObject3, "PasswordField.background", localXPColorValue, "PasswordField.foreground", localDesktopProperty13, "PasswordField.inactiveForeground", localDesktopProperty18, "PasswordField.inactiveBackground", localDesktopProperty20, "PasswordField.disabledBackground", localDesktopProperty21, "PasswordField.selectionBackground", localDesktopProperty10, "PasswordField.selectionForeground", localDesktopProperty11, "PasswordField.caretForeground", localDesktopProperty13, "PasswordField.echoChar", new XPValue(new Character('●'), new Character('*')), "ProgressBar.font", localObject3, "ProgressBar.foreground", localDesktopProperty10, "ProgressBar.background", localDesktopProperty1, "ProgressBar.shadow", localDesktopProperty4, "ProgressBar.highlight", localDesktopProperty3, "ProgressBar.selectionForeground", localDesktopProperty1, "ProgressBar.selectionBackground", localDesktopProperty10, "ProgressBar.cellLength", Integer.valueOf(7), "ProgressBar.cellSpacing", Integer.valueOf(2), "ProgressBar.indeterminateInsets", new Insets(3, 3, 3, 3), "RootPane.defaultButtonWindowKeyBindings", { "ENTER", "press", "released ENTER", "release", "ctrl ENTER", "press", "ctrl released ENTER", "release" }, "ScrollBar.background", localDesktopProperty19, "ScrollBar.foreground", localDesktopProperty1, "ScrollBar.track", localColorUIResource3, "ScrollBar.trackForeground", localDesktopProperty19, "ScrollBar.trackHighlight", localColorUIResource2, "ScrollBar.trackHighlightForeground", localColorUIResource6, "ScrollBar.thumb", localDesktopProperty1, "ScrollBar.thumbHighlight", localDesktopProperty3, "ScrollBar.thumbDarkShadow", localDesktopProperty5, "ScrollBar.thumbShadow", localDesktopProperty4, "ScrollBar.width", localDesktopProperty22, "ScrollBar.ancestorInputMap", new UIDefaults.LazyInputMap(new Object[] { "RIGHT", "positiveUnitIncrement", "KP_RIGHT", "positiveUnitIncrement", "DOWN", "positiveUnitIncrement", "KP_DOWN", "positiveUnitIncrement", "PAGE_DOWN", "positiveBlockIncrement", "ctrl PAGE_DOWN", "positiveBlockIncrement", "LEFT", "negativeUnitIncrement", "KP_LEFT", "negativeUnitIncrement", "UP", "negativeUnitIncrement", "KP_UP", "negativeUnitIncrement", "PAGE_UP", "negativeBlockIncrement", "ctrl PAGE_UP", "negativeBlockIncrement", "HOME", "minScroll", "END", "maxScroll" }), "ScrollPane.font", localObject3, "ScrollPane.background", localDesktopProperty1, "ScrollPane.foreground", localDesktopProperty6, "ScrollPane.ancestorInputMap", new UIDefaults.LazyInputMap(new Object[] { "RIGHT", "unitScrollRight", "KP_RIGHT", "unitScrollRight", "DOWN", "unitScrollDown", "KP_DOWN", "unitScrollDown", "LEFT", "unitScrollLeft", "KP_LEFT", "unitScrollLeft", "UP", "unitScrollUp", "KP_UP", "unitScrollUp", "PAGE_UP", "scrollUp", "PAGE_DOWN", "scrollDown", "ctrl PAGE_UP", "scrollLeft", "ctrl PAGE_DOWN", "scrollRight", "ctrl HOME", "scrollHome", "ctrl END", "scrollEnd" }), "Separator.background", localDesktopProperty3, "Separator.foreground", localDesktopProperty4, "Slider.font", localObject3, "Slider.foreground", localDesktopProperty1, "Slider.background", localDesktopProperty1, "Slider.highlight", localDesktopProperty3, "Slider.shadow", localDesktopProperty4, "Slider.focus", localDesktopProperty5, "Slider.focusInputMap", new UIDefaults.LazyInputMap(new Object[] { "RIGHT", "positiveUnitIncrement", "KP_RIGHT", "positiveUnitIncrement", "DOWN", "negativeUnitIncrement", "KP_DOWN", "negativeUnitIncrement", "PAGE_DOWN", "negativeBlockIncrement", "LEFT", "negativeUnitIncrement", "KP_LEFT", "negativeUnitIncrement", "UP", "positiveUnitIncrement", "KP_UP", "positiveUnitIncrement", "PAGE_UP", "positiveBlockIncrement", "HOME", "minScroll", "END", "maxScroll" }), "Spinner.font", localObject3, "Spinner.ancestorInputMap", new UIDefaults.LazyInputMap(new Object[] { "UP", "increment", "KP_UP", "increment", "DOWN", "decrement", "KP_DOWN", "decrement" }), "SplitPane.background", localDesktopProperty1, "SplitPane.highlight", localDesktopProperty3, "SplitPane.shadow", localDesktopProperty4, "SplitPane.darkShadow", localDesktopProperty5, "SplitPane.dividerSize", Integer.valueOf(5), "SplitPane.ancestorInputMap", new UIDefaults.LazyInputMap(new Object[] { "UP", "negativeIncrement", "DOWN", "positiveIncrement", "LEFT", "negativeIncrement", "RIGHT", "positiveIncrement", "KP_UP", "negativeIncrement", "KP_DOWN", "positiveIncrement", "KP_LEFT", "negativeIncrement", "KP_RIGHT", "positiveIncrement", "HOME", "selectMin", "END", "selectMax", "F8", "startResize", "F6", "toggleFocus", "ctrl TAB", "focusOutForward", "ctrl shift TAB", "focusOutBackward" }), "TabbedPane.tabsOverlapBorder", new XPValue(Boolean.TRUE, Boolean.FALSE), "TabbedPane.tabInsets", new XPValue(new InsetsUIResource(1, 4, 1, 4), new InsetsUIResource(0, 4, 1, 4)), "TabbedPane.tabAreaInsets", new XPValue(new InsetsUIResource(3, 2, 2, 2), new InsetsUIResource(3, 2, 0, 2)), "TabbedPane.font", localObject3, "TabbedPane.background", localDesktopProperty1, "TabbedPane.foreground", localDesktopProperty6, "TabbedPane.highlight", localDesktopProperty3, "TabbedPane.light", localDesktopProperty2, "TabbedPane.shadow", localDesktopProperty4, "TabbedPane.darkShadow", localDesktopProperty5, "TabbedPane.focus", localDesktopProperty6, "TabbedPane.focusInputMap", new UIDefaults.LazyInputMap(new Object[] { "RIGHT", "navigateRight", "KP_RIGHT", "navigateRight", "LEFT", "navigateLeft", "KP_LEFT", "navigateLeft", "UP", "navigateUp", "KP_UP", "navigateUp", "DOWN", "navigateDown", "KP_DOWN", "navigateDown", "ctrl DOWN", "requestFocusForVisibleComponent", "ctrl KP_DOWN", "requestFocusForVisibleComponent" }), "TabbedPane.ancestorInputMap", new UIDefaults.LazyInputMap(new Object[] { "ctrl TAB", "navigateNext", "ctrl shift TAB", "navigatePrevious", "ctrl PAGE_DOWN", "navigatePageDown", "ctrl PAGE_UP", "navigatePageUp", "ctrl UP", "requestFocus", "ctrl KP_UP", "requestFocus" }), "Table.font", localObject3, "Table.foreground", localDesktopProperty6, "Table.background", localDesktopProperty12, "Table.highlight", localDesktopProperty3, "Table.light", localDesktopProperty2, "Table.shadow", localDesktopProperty4, "Table.darkShadow", localDesktopProperty5, "Table.selectionForeground", localDesktopProperty11, "Table.selectionBackground", localDesktopProperty10, "Table.gridColor", localColorUIResource4, "Table.focusCellBackground", localDesktopProperty12, "Table.focusCellForeground", localDesktopProperty6, "Table.ancestorInputMap", new UIDefaults.LazyInputMap(new Object[] { "ctrl C", "copy", "ctrl V", "paste", "ctrl X", "cut", "COPY", "copy", "PASTE", "paste", "CUT", "cut", "control INSERT", "copy", "shift INSERT", "paste", "shift DELETE", "cut", "RIGHT", "selectNextColumn", "KP_RIGHT", "selectNextColumn", "shift RIGHT", "selectNextColumnExtendSelection", "shift KP_RIGHT", "selectNextColumnExtendSelection", "ctrl shift RIGHT", "selectNextColumnExtendSelection", "ctrl shift KP_RIGHT", "selectNextColumnExtendSelection", "ctrl RIGHT", "selectNextColumnChangeLead", "ctrl KP_RIGHT", "selectNextColumnChangeLead", "LEFT", "selectPreviousColumn", "KP_LEFT", "selectPreviousColumn", "shift LEFT", "selectPreviousColumnExtendSelection", "shift KP_LEFT", "selectPreviousColumnExtendSelection", "ctrl shift LEFT", "selectPreviousColumnExtendSelection", "ctrl shift KP_LEFT", "selectPreviousColumnExtendSelection", "ctrl LEFT", "selectPreviousColumnChangeLead", "ctrl KP_LEFT", "selectPreviousColumnChangeLead", "DOWN", "selectNextRow", "KP_DOWN", "selectNextRow", "shift DOWN", "selectNextRowExtendSelection", "shift KP_DOWN", "selectNextRowExtendSelection", "ctrl shift DOWN", "selectNextRowExtendSelection", "ctrl shift KP_DOWN", "selectNextRowExtendSelection", "ctrl DOWN", "selectNextRowChangeLead", "ctrl KP_DOWN", "selectNextRowChangeLead", "UP", "selectPreviousRow", "KP_UP", "selectPreviousRow", "shift UP", "selectPreviousRowExtendSelection", "shift KP_UP", "selectPreviousRowExtendSelection", "ctrl shift UP", "selectPreviousRowExtendSelection", "ctrl shift KP_UP", "selectPreviousRowExtendSelection", "ctrl UP", "selectPreviousRowChangeLead", "ctrl KP_UP", "selectPreviousRowChangeLead", "HOME", "selectFirstColumn", "shift HOME", "selectFirstColumnExtendSelection", "ctrl shift HOME", "selectFirstRowExtendSelection", "ctrl HOME", "selectFirstRow", "END", "selectLastColumn", "shift END", "selectLastColumnExtendSelection", "ctrl shift END", "selectLastRowExtendSelection", "ctrl END", "selectLastRow", "PAGE_UP", "scrollUpChangeSelection", "shift PAGE_UP", "scrollUpExtendSelection", "ctrl shift PAGE_UP", "scrollLeftExtendSelection", "ctrl PAGE_UP", "scrollLeftChangeSelection", "PAGE_DOWN", "scrollDownChangeSelection", "shift PAGE_DOWN", "scrollDownExtendSelection", "ctrl shift PAGE_DOWN", "scrollRightExtendSelection", "ctrl PAGE_DOWN", "scrollRightChangeSelection", "TAB", "selectNextColumnCell", "shift TAB", "selectPreviousColumnCell", "ENTER", "selectNextRowCell", "shift ENTER", "selectPreviousRowCell", "ctrl A", "selectAll", "ctrl SLASH", "selectAll", "ctrl BACK_SLASH", "clearSelection", "ESCAPE", "cancel", "F2", "startEditing", "SPACE", "addToSelection", "ctrl SPACE", "toggleAndAnchor", "shift SPACE", "extendTo", "ctrl shift SPACE", "moveSelectionTo", "F8", "focusHeader" }), "Table.sortIconHighlight", localDesktopProperty4, "Table.sortIconLight", localColorUIResource3, "TableHeader.font", localObject3, "TableHeader.foreground", localDesktopProperty6, "TableHeader.background", localDesktopProperty1, "TableHeader.focusCellBackground", new XPValue(XPValue.NULL_VALUE, localDesktopProperty12), "TextArea.font", localObject2, "TextArea.background", localDesktopProperty12, "TextArea.foreground", localDesktopProperty13, "TextArea.inactiveForeground", localDesktopProperty18, "TextArea.inactiveBackground", localDesktopProperty12, "TextArea.disabledBackground", localDesktopProperty21, "TextArea.selectionBackground", localDesktopProperty10, "TextArea.selectionForeground", localDesktopProperty11, "TextArea.caretForeground", localDesktopProperty13, "TextField.font", localObject3, "TextField.background", localXPColorValue, "TextField.foreground", localDesktopProperty13, "TextField.shadow", localDesktopProperty4, "TextField.darkShadow", localDesktopProperty5, "TextField.light", localDesktopProperty2, "TextField.highlight", localDesktopProperty3, "TextField.inactiveForeground", localDesktopProperty18, "TextField.inactiveBackground", localDesktopProperty20, "TextField.disabledBackground", localDesktopProperty21, "TextField.selectionBackground", localDesktopProperty10, "TextField.selectionForeground", localDesktopProperty11, "TextField.caretForeground", localDesktopProperty13, "TextPane.font", localObject3, "TextPane.background", localDesktopProperty12, "TextPane.foreground", localDesktopProperty13, "TextPane.selectionBackground", localDesktopProperty10, "TextPane.selectionForeground", localDesktopProperty11, "TextPane.inactiveBackground", localDesktopProperty12, "TextPane.disabledBackground", localDesktopProperty21, "TextPane.caretForeground", localDesktopProperty13, "TitledBorder.font", localObject3, "TitledBorder.titleColor", new XPColorValue(TMSchema.Part.BP_GROUPBOX, null, TMSchema.Prop.TEXTCOLOR, localDesktopProperty13), "ToggleButton.font", localObject3, "ToggleButton.background", localDesktopProperty1, "ToggleButton.foreground", localDesktopProperty6, "ToggleButton.shadow", localDesktopProperty4, "ToggleButton.darkShadow", localDesktopProperty5, "ToggleButton.light", localDesktopProperty2, "ToggleButton.highlight", localDesktopProperty3, "ToggleButton.focus", localDesktopProperty6, "ToggleButton.textShiftOffset", Integer.valueOf(1), "ToggleButton.focusInputMap", new UIDefaults.LazyInputMap(new Object[] { "SPACE", "pressed", "released SPACE", "released" }), "ToolBar.font", localObject1, "ToolBar.background", localDesktopProperty1, "ToolBar.foreground", localDesktopProperty6, "ToolBar.shadow", localDesktopProperty4, "ToolBar.darkShadow", localDesktopProperty5, "ToolBar.light", localDesktopProperty2, "ToolBar.highlight", localDesktopProperty3, "ToolBar.dockingBackground", localDesktopProperty1, "ToolBar.dockingForeground", localColorUIResource1, "ToolBar.floatingBackground", localDesktopProperty1, "ToolBar.floatingForeground", localColorUIResource5, "ToolBar.ancestorInputMap", new UIDefaults.LazyInputMap(new Object[] { "UP", "navigateUp", "KP_UP", "navigateUp", "DOWN", "navigateDown", "KP_DOWN", "navigateDown", "LEFT", "navigateLeft", "KP_LEFT", "navigateLeft", "RIGHT", "navigateRight", "KP_RIGHT", "navigateRight" }), "ToolBar.separatorSize", null, "ToolTip.font", localObject6, "ToolTip.background", new DesktopProperty("win.tooltip.backgroundColor", paramUIDefaults.get("info")), "ToolTip.foreground", new DesktopProperty("win.tooltip.textColor", paramUIDefaults.get("infoText")), "ToolTipManager.enableToolTipMode", "activeApplication", "Tree.selectionBorderColor", localColorUIResource2, "Tree.drawDashedFocusIndicator", Boolean.TRUE, "Tree.lineTypeDashed", Boolean.TRUE, "Tree.font", localObject3, "Tree.background", localDesktopProperty12, "Tree.foreground", localDesktopProperty13, "Tree.hash", localColorUIResource4, "Tree.leftChildIndent", Integer.valueOf(8), "Tree.rightChildIndent", Integer.valueOf(11), "Tree.textForeground", localDesktopProperty13, "Tree.textBackground", localDesktopProperty12, "Tree.selectionForeground", localDesktopProperty11, "Tree.selectionBackground", localDesktopProperty10, "Tree.expandedIcon", localIcon1, "Tree.collapsedIcon", localIcon2, "Tree.openIcon", new ActiveWindowsIcon("win.icon.shellIconBPP", "shell32Icon 5", "icons/TreeOpen.gif"), "Tree.closedIcon", new ActiveWindowsIcon("win.icon.shellIconBPP", "shell32Icon 4", "icons/TreeClosed.gif"), "Tree.focusInputMap", new UIDefaults.LazyInputMap(new Object[] { "ADD", "expand", "SUBTRACT", "collapse", "ctrl C", "copy", "ctrl V", "paste", "ctrl X", "cut", "COPY", "copy", "PASTE", "paste", "CUT", "cut", "control INSERT", "copy", "shift INSERT", "paste", "shift DELETE", "cut", "UP", "selectPrevious", "KP_UP", "selectPrevious", "shift UP", "selectPreviousExtendSelection", "shift KP_UP", "selectPreviousExtendSelection", "ctrl shift UP", "selectPreviousExtendSelection", "ctrl shift KP_UP", "selectPreviousExtendSelection", "ctrl UP", "selectPreviousChangeLead", "ctrl KP_UP", "selectPreviousChangeLead", "DOWN", "selectNext", "KP_DOWN", "selectNext", "shift DOWN", "selectNextExtendSelection", "shift KP_DOWN", "selectNextExtendSelection", "ctrl shift DOWN", "selectNextExtendSelection", "ctrl shift KP_DOWN", "selectNextExtendSelection", "ctrl DOWN", "selectNextChangeLead", "ctrl KP_DOWN", "selectNextChangeLead", "RIGHT", "selectChild", "KP_RIGHT", "selectChild", "LEFT", "selectParent", "KP_LEFT", "selectParent", "PAGE_UP", "scrollUpChangeSelection", "shift PAGE_UP", "scrollUpExtendSelection", "ctrl shift PAGE_UP", "scrollUpExtendSelection", "ctrl PAGE_UP", "scrollUpChangeLead", "PAGE_DOWN", "scrollDownChangeSelection", "shift PAGE_DOWN", "scrollDownExtendSelection", "ctrl shift PAGE_DOWN", "scrollDownExtendSelection", "ctrl PAGE_DOWN", "scrollDownChangeLead", "HOME", "selectFirst", "shift HOME", "selectFirstExtendSelection", "ctrl shift HOME", "selectFirstExtendSelection", "ctrl HOME", "selectFirstChangeLead", "END", "selectLast", "shift END", "selectLastExtendSelection", "ctrl shift END", "selectLastExtendSelection", "ctrl END", "selectLastChangeLead", "F2", "startEditing", "ctrl A", "selectAll", "ctrl SLASH", "selectAll", "ctrl BACK_SLASH", "clearSelection", "ctrl LEFT", "scrollLeft", "ctrl KP_LEFT", "scrollLeft", "ctrl RIGHT", "scrollRight", "ctrl KP_RIGHT", "scrollRight", "SPACE", "addToSelection", "ctrl SPACE", "toggleAndAnchor", "shift SPACE", "extendTo", "ctrl shift SPACE", "moveSelectionTo" }), "Tree.ancestorInputMap", new UIDefaults.LazyInputMap(new Object[] { "ESCAPE", "cancel" }), "Viewport.font", localObject3, "Viewport.background", localDesktopProperty1, "Viewport.foreground", localDesktopProperty13 };
/*      */ 
/* 1582 */     paramUIDefaults.putDefaults((Object[])localObject8);
/* 1583 */     paramUIDefaults.putDefaults(getLazyValueDefaults());
/* 1584 */     initVistaComponentDefaults(paramUIDefaults);
/*      */   }
/*      */ 
/*      */   static boolean isOnVista() {
/* 1588 */     return (OSInfo.getOSType() == OSInfo.OSType.WINDOWS) && (OSInfo.getWindowsVersion().compareTo(OSInfo.WINDOWS_VISTA) >= 0);
/*      */   }
/*      */ 
/*      */   private void initVistaComponentDefaults(UIDefaults paramUIDefaults)
/*      */   {
/* 1593 */     if (!isOnVista()) {
/* 1594 */       return;
/*      */     }
/*      */ 
/* 1597 */     String[] arrayOfString = { "MenuItem", "Menu", "CheckBoxMenuItem", "RadioButtonMenuItem" };
/*      */ 
/* 1601 */     Object[] arrayOfObject = new Object[arrayOfString.length * 2];
/*      */ 
/* 1604 */     int i = 0;
/*      */     String str1;
/*      */     Object localObject1;
/* 1604 */     for (int j = 0; i < arrayOfString.length; i++) {
/* 1605 */       str1 = arrayOfString[i] + ".opaque";
/* 1606 */       localObject1 = paramUIDefaults.get(str1);
/* 1607 */       arrayOfObject[(j++)] = str1;
/* 1608 */       arrayOfObject[(j++)] = new XPValue(Boolean.FALSE, localObject1);
/*      */     }
/*      */ 
/* 1611 */     paramUIDefaults.putDefaults(arrayOfObject);
/*      */ 
/* 1617 */     i = 0; for (j = 0; i < arrayOfString.length; i++) {
/* 1618 */       str1 = arrayOfString[i] + ".acceleratorSelectionForeground";
/* 1619 */       localObject1 = paramUIDefaults.get(str1);
/* 1620 */       arrayOfObject[(j++)] = str1;
/* 1621 */       arrayOfObject[(j++)] = new XPValue(paramUIDefaults.getColor(arrayOfString[i] + ".acceleratorForeground"), localObject1);
/*      */     }
/*      */ 
/* 1627 */     paramUIDefaults.putDefaults(arrayOfObject);
/*      */ 
/* 1630 */     WindowsIconFactory.VistaMenuItemCheckIconFactory localVistaMenuItemCheckIconFactory = WindowsIconFactory.getMenuItemCheckIconFactory();
/*      */ 
/* 1632 */     j = 0;
/*      */     Object localObject2;
/* 1632 */     for (int k = 0; j < arrayOfString.length; j++) {
/* 1633 */       localObject1 = arrayOfString[j] + ".checkIconFactory";
/* 1634 */       localObject2 = paramUIDefaults.get(localObject1);
/* 1635 */       arrayOfObject[(k++)] = localObject1;
/* 1636 */       arrayOfObject[(k++)] = new XPValue(localVistaMenuItemCheckIconFactory, localObject2);
/*      */     }
/*      */ 
/* 1639 */     paramUIDefaults.putDefaults(arrayOfObject);
/*      */ 
/* 1641 */     j = 0; for (k = 0; j < arrayOfString.length; j++) {
/* 1642 */       localObject1 = arrayOfString[j] + ".checkIcon";
/* 1643 */       localObject2 = paramUIDefaults.get(localObject1);
/* 1644 */       arrayOfObject[(k++)] = localObject1;
/* 1645 */       arrayOfObject[(k++)] = new XPValue(localVistaMenuItemCheckIconFactory.getIcon(arrayOfString[j]), localObject2);
/*      */     }
/*      */ 
/* 1649 */     paramUIDefaults.putDefaults(arrayOfObject);
/*      */ 
/* 1653 */     j = 0; for (k = 0; j < arrayOfString.length; j++) {
/* 1654 */       localObject1 = arrayOfString[j] + ".evenHeight";
/* 1655 */       localObject2 = paramUIDefaults.get(localObject1);
/* 1656 */       arrayOfObject[(k++)] = localObject1;
/* 1657 */       arrayOfObject[(k++)] = new XPValue(Boolean.TRUE, localObject2);
/*      */     }
/* 1659 */     paramUIDefaults.putDefaults(arrayOfObject);
/*      */ 
/* 1662 */     InsetsUIResource localInsetsUIResource = new InsetsUIResource(0, 0, 0, 0);
/* 1663 */     k = 0;
/*      */     Object localObject3;
/* 1663 */     for (int m = 0; k < arrayOfString.length; k++) {
/* 1664 */       localObject2 = arrayOfString[k] + ".margin";
/* 1665 */       localObject3 = paramUIDefaults.get(localObject2);
/* 1666 */       arrayOfObject[(m++)] = localObject2;
/* 1667 */       arrayOfObject[(m++)] = new XPValue(localInsetsUIResource, localObject3);
/*      */     }
/* 1669 */     paramUIDefaults.putDefaults(arrayOfObject);
/*      */ 
/* 1672 */     Integer localInteger1 = Integer.valueOf(0);
/*      */ 
/* 1674 */     m = 0;
/*      */     Object localObject4;
/* 1674 */     for (int n = 0; m < arrayOfString.length; m++) {
/* 1675 */       localObject3 = arrayOfString[m] + ".checkIconOffset";
/* 1676 */       localObject4 = paramUIDefaults.get(localObject3);
/* 1677 */       arrayOfObject[(n++)] = localObject3;
/* 1678 */       arrayOfObject[(n++)] = new XPValue(localInteger1, localObject4);
/*      */     }
/*      */ 
/* 1681 */     paramUIDefaults.putDefaults(arrayOfObject);
/*      */ 
/* 1684 */     Integer localInteger2 = Integer.valueOf(WindowsPopupMenuUI.getSpanBeforeGutter() + WindowsPopupMenuUI.getGutterWidth() + WindowsPopupMenuUI.getSpanAfterGutter());
/*      */ 
/* 1687 */     n = 0;
/*      */     Object localObject5;
/* 1687 */     for (int i1 = 0; n < arrayOfString.length; n++) {
/* 1688 */       localObject4 = arrayOfString[n] + ".afterCheckIconGap";
/* 1689 */       localObject5 = paramUIDefaults.get(localObject4);
/* 1690 */       arrayOfObject[(i1++)] = localObject4;
/* 1691 */       arrayOfObject[(i1++)] = new XPValue(localInteger2, localObject5);
/*      */     }
/*      */ 
/* 1694 */     paramUIDefaults.putDefaults(arrayOfObject);
/*      */ 
/* 1697 */     UIDefaults.ActiveValue local1 = new UIDefaults.ActiveValue() {
/*      */       public Object createValue(UIDefaults paramAnonymousUIDefaults) {
/* 1699 */         return Integer.valueOf(WindowsIconFactory.VistaMenuItemCheckIconFactory.getIconWidth() + WindowsPopupMenuUI.getSpanBeforeGutter() + WindowsPopupMenuUI.getGutterWidth() + WindowsPopupMenuUI.getSpanAfterGutter());
/*      */       }
/*      */     };
/* 1705 */     i1 = 0; for (int i2 = 0; i1 < arrayOfString.length; i1++) {
/* 1706 */       localObject5 = arrayOfString[i1] + ".minimumTextOffset";
/* 1707 */       Object localObject6 = paramUIDefaults.get(localObject5);
/* 1708 */       arrayOfObject[(i2++)] = localObject5;
/* 1709 */       arrayOfObject[(i2++)] = new XPValue(local1, localObject6);
/*      */     }
/* 1711 */     paramUIDefaults.putDefaults(arrayOfObject);
/*      */ 
/* 1716 */     String str2 = "PopupMenu.border";
/*      */ 
/* 1718 */     XPBorderValue localXPBorderValue = new XPBorderValue(TMSchema.Part.MENU, new SwingLazyValue("javax.swing.plaf.basic.BasicBorders", "getInternalFrameBorder"), BorderFactory.createEmptyBorder(2, 2, 2, 2));
/*      */ 
/* 1723 */     paramUIDefaults.put(str2, localXPBorderValue);
/*      */ 
/* 1727 */     paramUIDefaults.put("Table.ascendingSortIcon", new XPValue(new SkinIcon(TMSchema.Part.HP_HEADERSORTARROW, TMSchema.State.SORTEDDOWN), new SwingLazyValue("sun.swing.plaf.windows.ClassicSortArrowIcon", null, new Object[] { Boolean.TRUE })));
/*      */ 
/* 1732 */     paramUIDefaults.put("Table.descendingSortIcon", new XPValue(new SkinIcon(TMSchema.Part.HP_HEADERSORTARROW, TMSchema.State.SORTEDUP), new SwingLazyValue("sun.swing.plaf.windows.ClassicSortArrowIcon", null, new Object[] { Boolean.FALSE })));
/*      */   }
/*      */ 
/*      */   private Object getDesktopFontValue(String paramString, Object paramObject)
/*      */   {
/* 1747 */     if (this.useSystemFontSettings) {
/* 1748 */       return new WindowsFontProperty(paramString, paramObject);
/*      */     }
/* 1750 */     return null;
/*      */   }
/*      */ 
/*      */   private Object[] getLazyValueDefaults()
/*      */   {
/* 1759 */     XPBorderValue localXPBorderValue1 = new XPBorderValue(TMSchema.Part.BP_PUSHBUTTON, new SwingLazyValue("javax.swing.plaf.basic.BasicBorders", "getButtonBorder"));
/*      */ 
/* 1765 */     XPBorderValue localXPBorderValue2 = new XPBorderValue(TMSchema.Part.EP_EDIT, new SwingLazyValue("javax.swing.plaf.basic.BasicBorders", "getTextFieldBorder"));
/*      */ 
/* 1771 */     XPValue localXPValue1 = new XPValue(new InsetsUIResource(2, 2, 2, 2), new InsetsUIResource(1, 1, 1, 1));
/*      */ 
/* 1775 */     XPBorderValue localXPBorderValue3 = new XPBorderValue(TMSchema.Part.EP_EDIT, localXPBorderValue2, new EmptyBorder(2, 2, 2, 2));
/*      */ 
/* 1779 */     XPValue localXPValue2 = new XPValue(new InsetsUIResource(1, 1, 1, 1), null);
/*      */ 
/* 1783 */     XPBorderValue localXPBorderValue4 = new XPBorderValue(TMSchema.Part.CP_COMBOBOX, localXPBorderValue2);
/*      */ 
/* 1786 */     SwingLazyValue localSwingLazyValue1 = new SwingLazyValue("com.sun.java.swing.plaf.windows.WindowsBorders", "getFocusCellHighlightBorder");
/*      */ 
/* 1790 */     SwingLazyValue localSwingLazyValue2 = new SwingLazyValue("javax.swing.plaf.BorderUIResource", "getEtchedBorderUIResource");
/*      */ 
/* 1794 */     SwingLazyValue localSwingLazyValue3 = new SwingLazyValue("com.sun.java.swing.plaf.windows.WindowsBorders", "getInternalFrameBorder");
/*      */ 
/* 1798 */     SwingLazyValue localSwingLazyValue4 = new SwingLazyValue("javax.swing.plaf.BorderUIResource", "getLoweredBevelBorderUIResource");
/*      */ 
/* 1803 */     SwingLazyValue localSwingLazyValue5 = new SwingLazyValue("javax.swing.plaf.basic.BasicBorders$MarginBorder");
/*      */ 
/* 1806 */     SwingLazyValue localSwingLazyValue6 = new SwingLazyValue("javax.swing.plaf.basic.BasicBorders", "getMenuBarBorder");
/*      */ 
/* 1811 */     XPBorderValue localXPBorderValue5 = new XPBorderValue(TMSchema.Part.MENU, new SwingLazyValue("javax.swing.plaf.basic.BasicBorders", "getInternalFrameBorder"));
/*      */ 
/* 1817 */     SwingLazyValue localSwingLazyValue7 = new SwingLazyValue("com.sun.java.swing.plaf.windows.WindowsBorders", "getProgressBarBorder");
/*      */ 
/* 1821 */     SwingLazyValue localSwingLazyValue8 = new SwingLazyValue("javax.swing.plaf.basic.BasicBorders", "getRadioButtonBorder");
/*      */ 
/* 1825 */     XPBorderValue localXPBorderValue6 = new XPBorderValue(TMSchema.Part.LBP_LISTBOX, localXPBorderValue2);
/*      */ 
/* 1828 */     XPBorderValue localXPBorderValue7 = new XPBorderValue(TMSchema.Part.LBP_LISTBOX, localSwingLazyValue4);
/*      */ 
/* 1831 */     SwingLazyValue localSwingLazyValue9 = new SwingLazyValue("com.sun.java.swing.plaf.windows.WindowsBorders", "getTableHeaderBorder");
/*      */ 
/* 1836 */     SwingLazyValue localSwingLazyValue10 = new SwingLazyValue("com.sun.java.swing.plaf.windows.WindowsBorders", "getToolBarBorder");
/*      */ 
/* 1841 */     SwingLazyValue localSwingLazyValue11 = new SwingLazyValue("javax.swing.plaf.BorderUIResource", "getBlackLineBorderUIResource");
/*      */ 
/* 1847 */     SwingLazyValue localSwingLazyValue12 = new SwingLazyValue("com.sun.java.swing.plaf.windows.WindowsIconFactory", "getCheckBoxIcon");
/*      */ 
/* 1851 */     SwingLazyValue localSwingLazyValue13 = new SwingLazyValue("com.sun.java.swing.plaf.windows.WindowsIconFactory", "getRadioButtonIcon");
/*      */ 
/* 1855 */     SwingLazyValue localSwingLazyValue14 = new SwingLazyValue("com.sun.java.swing.plaf.windows.WindowsIconFactory", "getRadioButtonMenuItemIcon");
/*      */ 
/* 1859 */     SwingLazyValue localSwingLazyValue15 = new SwingLazyValue("com.sun.java.swing.plaf.windows.WindowsIconFactory", "getMenuItemCheckIcon");
/*      */ 
/* 1863 */     SwingLazyValue localSwingLazyValue16 = new SwingLazyValue("com.sun.java.swing.plaf.windows.WindowsIconFactory", "getMenuItemArrowIcon");
/*      */ 
/* 1867 */     SwingLazyValue localSwingLazyValue17 = new SwingLazyValue("com.sun.java.swing.plaf.windows.WindowsIconFactory", "getMenuArrowIcon");
/*      */ 
/* 1872 */     Object[] arrayOfObject = { "Button.border", localXPBorderValue1, "CheckBox.border", localSwingLazyValue8, "ComboBox.border", localXPBorderValue4, "DesktopIcon.border", localSwingLazyValue3, "FormattedTextField.border", localXPBorderValue2, "FormattedTextField.margin", localXPValue1, "InternalFrame.border", localSwingLazyValue3, "List.focusCellHighlightBorder", localSwingLazyValue1, "Table.focusCellHighlightBorder", localSwingLazyValue1, "Menu.border", localSwingLazyValue5, "MenuBar.border", localSwingLazyValue6, "MenuItem.border", localSwingLazyValue5, "PasswordField.border", localXPBorderValue2, "PasswordField.margin", localXPValue1, "PopupMenu.border", localXPBorderValue5, "ProgressBar.border", localSwingLazyValue7, "RadioButton.border", localSwingLazyValue8, "ScrollPane.border", localXPBorderValue6, "Spinner.border", localXPBorderValue3, "Spinner.arrowButtonInsets", localXPValue2, "Spinner.arrowButtonSize", new Dimension(17, 9), "Table.scrollPaneBorder", localXPBorderValue7, "TableHeader.cellBorder", localSwingLazyValue9, "TextArea.margin", localXPValue1, "TextField.border", localXPBorderValue2, "TextField.margin", localXPValue1, "TitledBorder.border", new XPBorderValue(TMSchema.Part.BP_GROUPBOX, localSwingLazyValue2), "ToggleButton.border", localSwingLazyValue8, "ToolBar.border", localSwingLazyValue10, "ToolTip.border", localSwingLazyValue11, "CheckBox.icon", localSwingLazyValue12, "Menu.arrowIcon", localSwingLazyValue17, "MenuItem.checkIcon", localSwingLazyValue15, "MenuItem.arrowIcon", localSwingLazyValue16, "RadioButton.icon", localSwingLazyValue13, "RadioButtonMenuItem.checkIcon", localSwingLazyValue14, "InternalFrame.layoutTitlePaneAtOrigin", new XPValue(Boolean.TRUE, Boolean.FALSE), "Table.ascendingSortIcon", new XPValue(new SwingLazyValue("sun.swing.icon.SortArrowIcon", null, new Object[] { Boolean.TRUE, "Table.sortIconColor" }), new SwingLazyValue("sun.swing.plaf.windows.ClassicSortArrowIcon", null, new Object[] { Boolean.TRUE })), "Table.descendingSortIcon", new XPValue(new SwingLazyValue("sun.swing.icon.SortArrowIcon", null, new Object[] { Boolean.FALSE, "Table.sortIconColor" }), new SwingLazyValue("sun.swing.plaf.windows.ClassicSortArrowIcon", null, new Object[] { Boolean.FALSE })) };
/*      */ 
/* 1931 */     return arrayOfObject;
/*      */   }
/*      */ 
/*      */   public void uninitialize() {
/* 1935 */     super.uninitialize();
/*      */ 
/* 1937 */     if (WindowsPopupMenuUI.mnemonicListener != null) {
/* 1938 */       MenuSelectionManager.defaultManager().removeChangeListener(WindowsPopupMenuUI.mnemonicListener);
/*      */     }
/*      */ 
/* 1941 */     KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventPostProcessor(WindowsRootPaneUI.altProcessor);
/*      */ 
/* 1943 */     DesktopProperty.flushUnreferencedProperties();
/*      */   }
/*      */ 
/*      */   public static void setMnemonicHidden(boolean paramBoolean)
/*      */   {
/* 1964 */     if (UIManager.getBoolean("Button.showMnemonics") == true)
/*      */     {
/* 1966 */       isMnemonicHidden = false;
/*      */     }
/* 1968 */     else isMnemonicHidden = paramBoolean;
/*      */   }
/*      */ 
/*      */   public static boolean isMnemonicHidden()
/*      */   {
/* 1981 */     if (UIManager.getBoolean("Button.showMnemonics") == true)
/*      */     {
/* 1983 */       isMnemonicHidden = false;
/*      */     }
/* 1985 */     return isMnemonicHidden;
/*      */   }
/*      */ 
/*      */   public static boolean isClassicWindows()
/*      */   {
/* 1999 */     return isClassicWindows;
/*      */   }
/*      */ 
/*      */   public void provideErrorFeedback(Component paramComponent)
/*      */   {
/* 2024 */     super.provideErrorFeedback(paramComponent);
/*      */   }
/*      */ 
/*      */   public LayoutStyle getLayoutStyle()
/*      */   {
/* 2031 */     Object localObject = this.style;
/* 2032 */     if (localObject == null) {
/* 2033 */       localObject = new WindowsLayoutStyle(null);
/* 2034 */       this.style = ((LayoutStyle)localObject);
/*      */     }
/* 2036 */     return localObject;
/*      */   }
/*      */ 
/*      */   protected Action createAudioAction(Object paramObject)
/*      */   {
/* 2061 */     if (paramObject != null) {
/* 2062 */       String str1 = (String)paramObject;
/* 2063 */       String str2 = (String)UIManager.get(paramObject);
/* 2064 */       return new AudioAction(str1, str2);
/*      */     }
/* 2066 */     return null;
/*      */   }
/*      */ 
/*      */   static void repaintRootPane(Component paramComponent)
/*      */   {
/* 2071 */     JRootPane localJRootPane = null;
/* 2072 */     for (; paramComponent != null; paramComponent = paramComponent.getParent()) {
/* 2073 */       if ((paramComponent instanceof JRootPane)) {
/* 2074 */         localJRootPane = (JRootPane)paramComponent;
/*      */       }
/*      */     }
/*      */ 
/* 2078 */     if (localJRootPane != null)
/* 2079 */       localJRootPane.repaint();
/*      */     else
/* 2081 */       paramComponent.repaint();
/*      */   }
/*      */ 
/*      */   private int dluToPixels(int paramInt1, int paramInt2)
/*      */   {
/* 2548 */     if (this.baseUnitX == 0) {
/* 2549 */       calculateBaseUnits();
/*      */     }
/* 2551 */     if ((paramInt2 == 3) || (paramInt2 == 7))
/*      */     {
/* 2553 */       return paramInt1 * this.baseUnitX / 4;
/*      */     }
/* 2555 */     assert ((paramInt2 == 1) || (paramInt2 == 5));
/*      */ 
/* 2557 */     return paramInt1 * this.baseUnitY / 8;
/*      */   }
/*      */ 
/*      */   private void calculateBaseUnits()
/*      */   {
/* 2566 */     FontMetrics localFontMetrics = Toolkit.getDefaultToolkit().getFontMetrics(UIManager.getFont("Button.font"));
/*      */ 
/* 2568 */     this.baseUnitX = localFontMetrics.stringWidth("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz");
/*      */ 
/* 2570 */     this.baseUnitX = ((this.baseUnitX / 26 + 1) / 2);
/*      */ 
/* 2572 */     this.baseUnitY = (localFontMetrics.getAscent() + localFontMetrics.getDescent() - 1);
/*      */   }
/*      */ 
/*      */   public Icon getDisabledIcon(JComponent paramJComponent, Icon paramIcon)
/*      */   {
/* 2585 */     if ((paramIcon != null) && (paramJComponent != null) && (Boolean.TRUE.equals(paramJComponent.getClientProperty(HI_RES_DISABLED_ICON_CLIENT_KEY))) && (paramIcon.getIconWidth() > 0) && (paramIcon.getIconHeight() > 0))
/*      */     {
/* 2590 */       BufferedImage localBufferedImage = new BufferedImage(paramIcon.getIconWidth(), paramIcon.getIconWidth(), 2);
/*      */ 
/* 2592 */       paramIcon.paintIcon(paramJComponent, localBufferedImage.getGraphics(), 0, 0);
/* 2593 */       RGBGrayFilter localRGBGrayFilter = new RGBGrayFilter();
/* 2594 */       FilteredImageSource localFilteredImageSource = new FilteredImageSource(localBufferedImage.getSource(), localRGBGrayFilter);
/* 2595 */       Image localImage = paramJComponent.createImage(localFilteredImageSource);
/* 2596 */       return new ImageIconUIResource(localImage);
/*      */     }
/* 2598 */     return super.getDisabledIcon(paramJComponent, paramIcon);
/*      */   }
/*      */ 
/*      */   private class ActiveWindowsIcon
/*      */     implements UIDefaults.ActiveValue
/*      */   {
/*      */     private Icon icon;
/*      */     private String nativeImageName;
/*      */     private String fallbackName;
/*      */     private DesktopProperty desktopProperty;
/*      */ 
/*      */     ActiveWindowsIcon(String paramString1, String paramString2, String arg4)
/*      */     {
/* 2158 */       this.nativeImageName = paramString2;
/*      */       Object localObject;
/* 2159 */       this.fallbackName = localObject;
/*      */ 
/* 2161 */       if ((OSInfo.getOSType() == OSInfo.OSType.WINDOWS) && (OSInfo.getWindowsVersion().compareTo(OSInfo.WINDOWS_XP) < 0))
/*      */       {
/* 2165 */         this.desktopProperty = new WindowsLookAndFeel.TriggerDesktopProperty(paramString1, WindowsLookAndFeel.this) {
/*      */           protected void updateUI() {
/* 2167 */             WindowsLookAndFeel.ActiveWindowsIcon.this.icon = null;
/* 2168 */             super.updateUI();
/*      */           }
/*      */         };
/*      */       }
/*      */     }
/*      */ 
/*      */     public Object createValue(UIDefaults paramUIDefaults)
/*      */     {
/*      */       Object localObject;
/* 2176 */       if (this.icon == null) {
/* 2177 */         localObject = (Image)ShellFolder.get(this.nativeImageName);
/* 2178 */         if (localObject != null) {
/* 2179 */           this.icon = new ImageIconUIResource((Image)localObject);
/*      */         }
/*      */       }
/* 2182 */       if ((this.icon == null) && (this.fallbackName != null)) {
/* 2183 */         localObject = (UIDefaults.LazyValue)SwingUtilities2.makeIcon(WindowsLookAndFeel.class, BasicLookAndFeel.class, this.fallbackName);
/*      */ 
/* 2186 */         this.icon = ((Icon)((UIDefaults.LazyValue)localObject).createValue(paramUIDefaults));
/*      */       }
/* 2188 */       return this.icon;
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class AudioAction extends AbstractAction
/*      */   {
/*      */     private Runnable audioRunnable;
/*      */     private String audioResource;
/*      */ 
/*      */     public AudioAction(String paramString1, String paramString2)
/*      */     {
/* 2104 */       super();
/* 2105 */       this.audioResource = paramString2;
/*      */     }
/*      */     public void actionPerformed(ActionEvent paramActionEvent) {
/* 2108 */       if (this.audioRunnable == null) {
/* 2109 */         this.audioRunnable = ((Runnable)Toolkit.getDefaultToolkit().getDesktopProperty(this.audioResource));
/*      */       }
/* 2111 */       if (this.audioRunnable != null)
/*      */       {
/* 2114 */         new Thread(this.audioRunnable).start();
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class FocusColorProperty extends DesktopProperty
/*      */   {
/*      */     public FocusColorProperty()
/*      */     {
/* 2626 */       super(Color.BLACK);
/*      */     }
/*      */ 
/*      */     protected Object configureValue(Object paramObject)
/*      */     {
/* 2631 */       if (!((Boolean)Toolkit.getDefaultToolkit().getDesktopProperty("win.highContrast.on")).booleanValue()) {
/* 2632 */         return Color.BLACK;
/*      */       }
/* 2634 */       return Color.BLACK.equals(paramObject) ? Color.WHITE : Color.BLACK;
/*      */     }
/*      */   }
/*      */ 
/*      */   private class FontDesktopProperty extends WindowsLookAndFeel.TriggerDesktopProperty
/*      */   {
/*      */     FontDesktopProperty(String arg2)
/*      */     {
/* 2474 */       super(str);
/*      */     }
/*      */ 
/*      */     protected void updateUI() {
/* 2478 */       SwingUtilities2.AATextInfo localAATextInfo = SwingUtilities2.AATextInfo.getAATextInfo(true);
/* 2479 */       UIDefaults localUIDefaults = UIManager.getLookAndFeelDefaults();
/* 2480 */       localUIDefaults.put(SwingUtilities2.AA_TEXT_PROPERTY_KEY, localAATextInfo);
/* 2481 */       super.updateUI();
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class LazyWindowsIcon
/*      */     implements UIDefaults.LazyValue
/*      */   {
/*      */     private String nativeImage;
/*      */     private String resource;
/*      */ 
/*      */     LazyWindowsIcon(String paramString1, String paramString2)
/*      */     {
/* 2128 */       this.nativeImage = paramString1;
/* 2129 */       this.resource = paramString2;
/*      */     }
/*      */ 
/*      */     public Object createValue(UIDefaults paramUIDefaults) {
/* 2133 */       if (this.nativeImage != null) {
/* 2134 */         Image localImage = (Image)ShellFolder.get(this.nativeImage);
/* 2135 */         if (localImage != null) {
/* 2136 */           return new ImageIcon(localImage);
/*      */         }
/*      */       }
/* 2139 */       return SwingUtilities2.makeIcon(getClass(), WindowsLookAndFeel.class, this.resource);
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class RGBGrayFilter extends RGBImageFilter
/*      */   {
/*      */     public RGBGrayFilter()
/*      */     {
/* 2603 */       this.canFilterIndexColorModel = true;
/*      */     }
/*      */ 
/*      */     public int filterRGB(int paramInt1, int paramInt2, int paramInt3) {
/* 2607 */       float f1 = ((paramInt3 >> 16 & 0xFF) / 255.0F + (paramInt3 >> 8 & 0xFF) / 255.0F + (paramInt3 & 0xFF) / 255.0F) / 3.0F;
/*      */ 
/* 2611 */       float f2 = (paramInt3 >> 24 & 0xFF) / 255.0F;
/*      */ 
/* 2613 */       f1 = Math.min(1.0F, (1.0F - f1) / 2.857143F + f1);
/*      */ 
/* 2615 */       int i = (int)(f2 * 255.0F) << 24 | (int)(f1 * 255.0F) << 16 | (int)(f1 * 255.0F) << 8 | (int)(f1 * 255.0F);
/*      */ 
/* 2619 */       return i;
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class SkinIcon
/*      */     implements Icon, UIResource
/*      */   {
/*      */     private final TMSchema.Part part;
/*      */     private final TMSchema.State state;
/*      */ 
/*      */     SkinIcon(TMSchema.Part paramPart, TMSchema.State paramState)
/*      */     {
/* 2199 */       this.part = paramPart;
/* 2200 */       this.state = paramState;
/*      */     }
/*      */ 
/*      */     public void paintIcon(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2)
/*      */     {
/* 2209 */       XPStyle localXPStyle = XPStyle.getXP();
/* 2210 */       assert (localXPStyle != null);
/* 2211 */       if (localXPStyle != null) {
/* 2212 */         XPStyle.Skin localSkin = localXPStyle.getSkin(null, this.part);
/* 2213 */         localSkin.paintSkin(paramGraphics, paramInt1, paramInt2, this.state);
/*      */       }
/*      */     }
/*      */ 
/*      */     public int getIconWidth()
/*      */     {
/* 2223 */       int i = 0;
/* 2224 */       XPStyle localXPStyle = XPStyle.getXP();
/* 2225 */       assert (localXPStyle != null);
/* 2226 */       if (localXPStyle != null) {
/* 2227 */         XPStyle.Skin localSkin = localXPStyle.getSkin(null, this.part);
/* 2228 */         i = localSkin.getWidth();
/*      */       }
/* 2230 */       return i;
/*      */     }
/*      */ 
/*      */     public int getIconHeight()
/*      */     {
/* 2239 */       int i = 0;
/* 2240 */       XPStyle localXPStyle = XPStyle.getXP();
/* 2241 */       if (localXPStyle != null) {
/* 2242 */         XPStyle.Skin localSkin = localXPStyle.getSkin(null, this.part);
/* 2243 */         i = localSkin.getHeight();
/*      */       }
/* 2245 */       return i;
/*      */     }
/*      */   }
/*      */ 
/*      */   private class TriggerDesktopProperty extends DesktopProperty
/*      */   {
/*      */     TriggerDesktopProperty(String arg2)
/*      */     {
/* 2457 */       super(null);
/*      */ 
/* 2461 */       getValueFromDesktop();
/*      */     }
/*      */ 
/*      */     protected void updateUI() {
/* 2465 */       super.updateUI();
/*      */ 
/* 2468 */       getValueFromDesktop();
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class WindowsFontProperty extends DesktopProperty
/*      */   {
/*      */     WindowsFontProperty(String paramString, Object paramObject)
/*      */     {
/* 2256 */       super(paramObject);
/*      */     }
/*      */ 
/*      */     public void invalidate(LookAndFeel paramLookAndFeel) {
/* 2260 */       if ("win.defaultGUI.font.height".equals(getKey())) {
/* 2261 */         ((WindowsLookAndFeel)paramLookAndFeel).style = null;
/*      */       }
/* 2263 */       super.invalidate(paramLookAndFeel);
/*      */     }
/*      */ 
/*      */     protected Object configureValue(Object paramObject) {
/* 2267 */       if ((paramObject instanceof Font)) {
/* 2268 */         Object localObject = (Font)paramObject;
/* 2269 */         if ("MS Sans Serif".equals(((Font)localObject).getName()))
/*      */         {
/* 2270 */           int i = ((Font)localObject).getSize();
/*      */           int j;
/*      */           try
/*      */           {
/* 2276 */             j = Toolkit.getDefaultToolkit().getScreenResolution();
/*      */           } catch (HeadlessException localHeadlessException) {
/* 2278 */             j = 96;
/*      */           }
/* 2280 */           if (Math.round(i * 72.0F / j) < 8) {
/* 2281 */             i = Math.round(8 * j / 72.0F);
/*      */           }
/* 2283 */           FontUIResource localFontUIResource = new FontUIResource("Microsoft Sans Serif", ((Font)localObject).getStyle(), i);
/*      */ 
/* 2285 */           if ((localFontUIResource.getName() != null) && (localFontUIResource.getName().equals(localFontUIResource.getFamily())))
/*      */           {
/* 2287 */             localObject = localFontUIResource;
/* 2288 */           } else if (i != ((Font)localObject).getSize()) {
/* 2289 */             localObject = new FontUIResource("MS Sans Serif", ((Font)localObject).getStyle(), i);
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/* 2294 */         if (FontUtilities.fontSupportsDefaultEncoding((Font)localObject)) {
/* 2295 */           if (!(localObject instanceof UIResource)) {
/* 2296 */             localObject = new FontUIResource((Font)localObject);
/*      */           }
/*      */         }
/*      */         else {
/* 2300 */           localObject = FontUtilities.getCompositeFontUIResource((Font)localObject);
/*      */         }
/* 2302 */         return localObject;
/*      */       }
/*      */ 
/* 2305 */       return super.configureValue(paramObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class WindowsFontSizeProperty extends DesktopProperty
/*      */   {
/*      */     private String fontName;
/*      */     private int fontSize;
/*      */     private int fontStyle;
/*      */ 
/*      */     WindowsFontSizeProperty(String paramString1, String paramString2, int paramInt1, int paramInt2)
/*      */     {
/* 2321 */       super(null);
/* 2322 */       this.fontName = paramString2;
/* 2323 */       this.fontSize = paramInt2;
/* 2324 */       this.fontStyle = paramInt1;
/*      */     }
/*      */ 
/*      */     protected Object configureValue(Object paramObject) {
/* 2328 */       if (paramObject == null) {
/* 2329 */         paramObject = new FontUIResource(this.fontName, this.fontStyle, this.fontSize);
/*      */       }
/* 2331 */       else if ((paramObject instanceof Integer)) {
/* 2332 */         paramObject = new FontUIResource(this.fontName, this.fontStyle, ((Integer)paramObject).intValue());
/*      */       }
/*      */ 
/* 2335 */       return paramObject;
/*      */     }
/*      */   }
/*      */ 
/*      */   private class WindowsLayoutStyle extends DefaultLayoutStyle
/*      */   {
/*      */     private WindowsLayoutStyle()
/*      */     {
/*      */     }
/*      */ 
/*      */     public int getPreferredGap(JComponent paramJComponent1, JComponent paramJComponent2, LayoutStyle.ComponentPlacement paramComponentPlacement, int paramInt, Container paramContainer)
/*      */     {
/* 2493 */       super.getPreferredGap(paramJComponent1, paramJComponent2, paramComponentPlacement, paramInt, paramContainer);
/*      */ 
/* 2496 */       switch (WindowsLookAndFeel.2.$SwitchMap$javax$swing$LayoutStyle$ComponentPlacement[paramComponentPlacement.ordinal()])
/*      */       {
/*      */       case 1:
/* 2499 */         if ((paramInt == 3) || (paramInt == 7))
/*      */         {
/* 2501 */           int i = getIndent(paramJComponent1, paramInt);
/* 2502 */           if (i > 0) {
/* 2503 */             return i;
/*      */           }
/* 2505 */           return 10;
/*      */         }
/*      */ 
/*      */       case 2:
/* 2509 */         if (isLabelAndNonlabel(paramJComponent1, paramJComponent2, paramInt))
/*      */         {
/* 2519 */           return getButtonGap(paramJComponent1, paramJComponent2, paramInt, WindowsLookAndFeel.this.dluToPixels(3, paramInt));
/*      */         }
/*      */ 
/* 2523 */         return getButtonGap(paramJComponent1, paramJComponent2, paramInt, WindowsLookAndFeel.this.dluToPixels(4, paramInt));
/*      */       case 3:
/* 2527 */         return getButtonGap(paramJComponent1, paramJComponent2, paramInt, WindowsLookAndFeel.this.dluToPixels(7, paramInt));
/*      */       }
/*      */ 
/* 2530 */       return 0;
/*      */     }
/*      */ 
/*      */     public int getContainerGap(JComponent paramJComponent, int paramInt, Container paramContainer)
/*      */     {
/* 2537 */       super.getContainerGap(paramJComponent, paramInt, paramContainer);
/* 2538 */       return getButtonGap(paramJComponent, paramInt, WindowsLookAndFeel.this.dluToPixels(7, paramInt));
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class XPBorderValue extends WindowsLookAndFeel.XPValue
/*      */   {
/*      */     private final Border extraMargin;
/*      */ 
/*      */     XPBorderValue(TMSchema.Part paramPart, Object paramObject)
/*      */     {
/* 2394 */       this(paramPart, paramObject, null);
/*      */     }
/*      */ 
/*      */     XPBorderValue(TMSchema.Part paramPart, Object paramObject, Border paramBorder) {
/* 2398 */       super(paramObject);
/* 2399 */       this.extraMargin = paramBorder;
/*      */     }
/*      */ 
/*      */     public Object getXPValue(UIDefaults paramUIDefaults) {
/* 2403 */       XPStyle localXPStyle = XPStyle.getXP();
/* 2404 */       Border localBorder = localXPStyle != null ? localXPStyle.getBorder(null, (TMSchema.Part)this.xpValue) : null;
/* 2405 */       if ((localBorder != null) && (this.extraMargin != null)) {
/* 2406 */         return new BorderUIResource.CompoundBorderUIResource(localBorder, this.extraMargin);
/*      */       }
/*      */ 
/* 2409 */       return localBorder;
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class XPColorValue extends WindowsLookAndFeel.XPValue
/*      */   {
/*      */     XPColorValue(TMSchema.Part paramPart, TMSchema.State paramState, TMSchema.Prop paramProp, Object paramObject) {
/* 2416 */       super(paramObject);
/*      */     }
/*      */ 
/*      */     public Object getXPValue(UIDefaults paramUIDefaults) {
/* 2420 */       XPColorValueKey localXPColorValueKey = (XPColorValueKey)this.xpValue;
/* 2421 */       XPStyle localXPStyle = XPStyle.getXP();
/* 2422 */       return localXPStyle != null ? localXPStyle.getColor(localXPColorValueKey.skin, localXPColorValueKey.prop, null) : null;
/*      */     }
/*      */     private static class XPColorValueKey {
/*      */       XPStyle.Skin skin;
/*      */       TMSchema.Prop prop;
/*      */ 
/*      */       XPColorValueKey(TMSchema.Part paramPart, TMSchema.State paramState, TMSchema.Prop paramProp) {
/* 2430 */         this.skin = new XPStyle.Skin(paramPart, paramState);
/* 2431 */         this.prop = paramProp;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private class XPDLUValue extends WindowsLookAndFeel.XPValue {
/*      */     private int direction;
/*      */ 
/*      */     XPDLUValue(int paramInt1, int paramInt2, int arg4) {
/* 2440 */       super(Integer.valueOf(paramInt2));
/*      */       int i;
/* 2441 */       this.direction = i;
/*      */     }
/*      */ 
/*      */     public Object getXPValue(UIDefaults paramUIDefaults) {
/* 2445 */       int i = WindowsLookAndFeel.this.dluToPixels(((Integer)this.xpValue).intValue(), this.direction);
/* 2446 */       return Integer.valueOf(i);
/*      */     }
/*      */ 
/*      */     public Object getClassicValue(UIDefaults paramUIDefaults) {
/* 2450 */       int i = WindowsLookAndFeel.this.dluToPixels(((Integer)this.classicValue).intValue(), this.direction);
/* 2451 */       return Integer.valueOf(i);
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class XPValue
/*      */     implements UIDefaults.ActiveValue
/*      */   {
/*      */     protected Object classicValue;
/*      */     protected Object xpValue;
/* 2348 */     private static final Object NULL_VALUE = new Object();
/*      */ 
/*      */     XPValue(Object paramObject1, Object paramObject2) {
/* 2351 */       this.xpValue = paramObject1;
/* 2352 */       this.classicValue = paramObject2;
/*      */     }
/*      */ 
/*      */     public Object createValue(UIDefaults paramUIDefaults) {
/* 2356 */       Object localObject = null;
/* 2357 */       if (XPStyle.getXP() != null) {
/* 2358 */         localObject = getXPValue(paramUIDefaults);
/*      */       }
/*      */ 
/* 2361 */       if (localObject == null)
/* 2362 */         localObject = getClassicValue(paramUIDefaults);
/* 2363 */       else if (localObject == NULL_VALUE) {
/* 2364 */         localObject = null;
/*      */       }
/*      */ 
/* 2367 */       return localObject;
/*      */     }
/*      */ 
/*      */     protected Object getXPValue(UIDefaults paramUIDefaults) {
/* 2371 */       return recursiveCreateValue(this.xpValue, paramUIDefaults);
/*      */     }
/*      */ 
/*      */     protected Object getClassicValue(UIDefaults paramUIDefaults) {
/* 2375 */       return recursiveCreateValue(this.classicValue, paramUIDefaults);
/*      */     }
/*      */ 
/*      */     private Object recursiveCreateValue(Object paramObject, UIDefaults paramUIDefaults) {
/* 2379 */       if ((paramObject instanceof UIDefaults.LazyValue)) {
/* 2380 */         paramObject = ((UIDefaults.LazyValue)paramObject).createValue(paramUIDefaults);
/*      */       }
/* 2382 */       if ((paramObject instanceof UIDefaults.ActiveValue)) {
/* 2383 */         return ((UIDefaults.ActiveValue)paramObject).createValue(paramUIDefaults);
/*      */       }
/* 2385 */       return paramObject;
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.java.swing.plaf.windows.WindowsLookAndFeel
 * JD-Core Version:    0.6.2
 */