/*      */ package javax.swing.plaf.basic;
/*      */ 
/*      */ import java.awt.AWTEvent;
/*      */ import java.awt.Color;
/*      */ import java.awt.Component;
/*      */ import java.awt.Dimension;
/*      */ import java.awt.Point;
/*      */ import java.awt.SystemColor;
/*      */ import java.awt.Toolkit;
/*      */ import java.awt.Window;
/*      */ import java.awt.event.AWTEventListener;
/*      */ import java.awt.event.ActionEvent;
/*      */ import java.awt.event.MouseEvent;
/*      */ import java.beans.PropertyChangeEvent;
/*      */ import java.beans.PropertyChangeListener;
/*      */ import java.beans.PropertyVetoException;
/*      */ import java.io.BufferedInputStream;
/*      */ import java.io.ByteArrayInputStream;
/*      */ import java.io.ByteArrayOutputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.PrintStream;
/*      */ import java.io.Serializable;
/*      */ import java.lang.reflect.Field;
/*      */ import java.security.AccessController;
/*      */ import java.security.PrivilegedAction;
/*      */ import java.util.HashSet;
/*      */ import java.util.Locale;
/*      */ import javax.sound.sampled.AudioInputStream;
/*      */ import javax.sound.sampled.AudioSystem;
/*      */ import javax.sound.sampled.Clip;
/*      */ import javax.sound.sampled.DataLine.Info;
/*      */ import javax.sound.sampled.LineEvent;
/*      */ import javax.sound.sampled.LineEvent.Type;
/*      */ import javax.sound.sampled.LineListener;
/*      */ import javax.swing.AbstractAction;
/*      */ import javax.swing.Action;
/*      */ import javax.swing.ActionMap;
/*      */ import javax.swing.DefaultListCellRenderer.UIResource;
/*      */ import javax.swing.JComponent;
/*      */ import javax.swing.JInternalFrame;
/*      */ import javax.swing.JPopupMenu;
/*      */ import javax.swing.LookAndFeel;
/*      */ import javax.swing.MenuSelectionManager;
/*      */ import javax.swing.SwingUtilities;
/*      */ import javax.swing.UIDefaults;
/*      */ import javax.swing.UIDefaults.ActiveValue;
/*      */ import javax.swing.UIDefaults.LazyInputMap;
/*      */ import javax.swing.UIManager;
/*      */ import javax.swing.plaf.ActionMapUIResource;
/*      */ import javax.swing.plaf.BorderUIResource.EmptyBorderUIResource;
/*      */ import javax.swing.plaf.ColorUIResource;
/*      */ import javax.swing.plaf.ComponentUI;
/*      */ import javax.swing.plaf.DimensionUIResource;
/*      */ import javax.swing.plaf.InsetsUIResource;
/*      */ import sun.awt.AppContext;
/*      */ import sun.awt.SunToolkit;
/*      */ import sun.swing.SwingLazyValue;
/*      */ import sun.swing.SwingUtilities2;
/*      */ 
/*      */ public abstract class BasicLookAndFeel extends LookAndFeel
/*      */   implements Serializable
/*      */ {
/*      */   static boolean needsEventHelper;
/*      */   private transient Object audioLock;
/*      */   private Clip clipPlaying;
/*      */   AWTEventHelper invocator;
/*      */   private PropertyChangeListener disposer;
/*      */ 
/*      */   public BasicLookAndFeel()
/*      */   {
/*  115 */     this.audioLock = new Object();
/*      */ 
/*  121 */     this.invocator = null;
/*      */ 
/*  126 */     this.disposer = null;
/*      */   }
/*      */ 
/*      */   public UIDefaults getDefaults()
/*      */   {
/*  144 */     UIDefaults localUIDefaults = new UIDefaults(610, 0.75F);
/*      */ 
/*  146 */     initClassDefaults(localUIDefaults);
/*  147 */     initSystemColorDefaults(localUIDefaults);
/*  148 */     initComponentDefaults(localUIDefaults);
/*      */ 
/*  150 */     return localUIDefaults;
/*      */   }
/*      */ 
/*      */   public void initialize()
/*      */   {
/*  157 */     if (needsEventHelper)
/*  158 */       installAWTEventListener();
/*      */   }
/*      */ 
/*      */   void installAWTEventListener()
/*      */   {
/*  163 */     if (this.invocator == null) {
/*  164 */       this.invocator = new AWTEventHelper();
/*  165 */       needsEventHelper = true;
/*      */ 
/*  170 */       this.disposer = new PropertyChangeListener() {
/*      */         public void propertyChange(PropertyChangeEvent paramAnonymousPropertyChangeEvent) {
/*  172 */           BasicLookAndFeel.this.uninitialize();
/*      */         }
/*      */       };
/*  175 */       AppContext.getAppContext().addPropertyChangeListener("guidisposed", this.disposer);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void uninitialize()
/*      */   {
/*  185 */     AppContext localAppContext = AppContext.getAppContext();
/*      */     Object localObject1;
/*  186 */     synchronized (BasicPopupMenuUI.MOUSE_GRABBER_KEY) {
/*  187 */       localObject1 = localAppContext.get(BasicPopupMenuUI.MOUSE_GRABBER_KEY);
/*  188 */       if (localObject1 != null) {
/*  189 */         ((BasicPopupMenuUI.MouseGrabber)localObject1).uninstall();
/*      */       }
/*      */     }
/*  192 */     synchronized (BasicPopupMenuUI.MENU_KEYBOARD_HELPER_KEY) {
/*  193 */       localObject1 = localAppContext.get(BasicPopupMenuUI.MENU_KEYBOARD_HELPER_KEY);
/*      */ 
/*  195 */       if (localObject1 != null) {
/*  196 */         ((BasicPopupMenuUI.MenuKeyboardHelper)localObject1).uninstall();
/*      */       }
/*      */     }
/*      */ 
/*  200 */     if (this.invocator != null) {
/*  201 */       AccessController.doPrivileged(this.invocator);
/*  202 */       this.invocator = null;
/*      */     }
/*      */ 
/*  205 */     if (this.disposer != null)
/*      */     {
/*  210 */       localAppContext.removePropertyChangeListener("guidisposed", this.disposer);
/*      */ 
/*  212 */       this.disposer = null;
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void initClassDefaults(UIDefaults paramUIDefaults)
/*      */   {
/*  234 */     Object[] arrayOfObject = { "ButtonUI", "javax.swing.plaf.basic.BasicButtonUI", "CheckBoxUI", "javax.swing.plaf.basic.BasicCheckBoxUI", "ColorChooserUI", "javax.swing.plaf.basic.BasicColorChooserUI", "FormattedTextFieldUI", "javax.swing.plaf.basic.BasicFormattedTextFieldUI", "MenuBarUI", "javax.swing.plaf.basic.BasicMenuBarUI", "MenuUI", "javax.swing.plaf.basic.BasicMenuUI", "MenuItemUI", "javax.swing.plaf.basic.BasicMenuItemUI", "CheckBoxMenuItemUI", "javax.swing.plaf.basic.BasicCheckBoxMenuItemUI", "RadioButtonMenuItemUI", "javax.swing.plaf.basic.BasicRadioButtonMenuItemUI", "RadioButtonUI", "javax.swing.plaf.basic.BasicRadioButtonUI", "ToggleButtonUI", "javax.swing.plaf.basic.BasicToggleButtonUI", "PopupMenuUI", "javax.swing.plaf.basic.BasicPopupMenuUI", "ProgressBarUI", "javax.swing.plaf.basic.BasicProgressBarUI", "ScrollBarUI", "javax.swing.plaf.basic.BasicScrollBarUI", "ScrollPaneUI", "javax.swing.plaf.basic.BasicScrollPaneUI", "SplitPaneUI", "javax.swing.plaf.basic.BasicSplitPaneUI", "SliderUI", "javax.swing.plaf.basic.BasicSliderUI", "SeparatorUI", "javax.swing.plaf.basic.BasicSeparatorUI", "SpinnerUI", "javax.swing.plaf.basic.BasicSpinnerUI", "ToolBarSeparatorUI", "javax.swing.plaf.basic.BasicToolBarSeparatorUI", "PopupMenuSeparatorUI", "javax.swing.plaf.basic.BasicPopupMenuSeparatorUI", "TabbedPaneUI", "javax.swing.plaf.basic.BasicTabbedPaneUI", "TextAreaUI", "javax.swing.plaf.basic.BasicTextAreaUI", "TextFieldUI", "javax.swing.plaf.basic.BasicTextFieldUI", "PasswordFieldUI", "javax.swing.plaf.basic.BasicPasswordFieldUI", "TextPaneUI", "javax.swing.plaf.basic.BasicTextPaneUI", "EditorPaneUI", "javax.swing.plaf.basic.BasicEditorPaneUI", "TreeUI", "javax.swing.plaf.basic.BasicTreeUI", "LabelUI", "javax.swing.plaf.basic.BasicLabelUI", "ListUI", "javax.swing.plaf.basic.BasicListUI", "ToolBarUI", "javax.swing.plaf.basic.BasicToolBarUI", "ToolTipUI", "javax.swing.plaf.basic.BasicToolTipUI", "ComboBoxUI", "javax.swing.plaf.basic.BasicComboBoxUI", "TableUI", "javax.swing.plaf.basic.BasicTableUI", "TableHeaderUI", "javax.swing.plaf.basic.BasicTableHeaderUI", "InternalFrameUI", "javax.swing.plaf.basic.BasicInternalFrameUI", "DesktopPaneUI", "javax.swing.plaf.basic.BasicDesktopPaneUI", "DesktopIconUI", "javax.swing.plaf.basic.BasicDesktopIconUI", "FileChooserUI", "javax.swing.plaf.basic.BasicFileChooserUI", "OptionPaneUI", "javax.swing.plaf.basic.BasicOptionPaneUI", "PanelUI", "javax.swing.plaf.basic.BasicPanelUI", "ViewportUI", "javax.swing.plaf.basic.BasicViewportUI", "RootPaneUI", "javax.swing.plaf.basic.BasicRootPaneUI" };
/*      */ 
/*  280 */     paramUIDefaults.putDefaults(arrayOfObject);
/*      */   }
/*      */ 
/*      */   protected void initSystemColorDefaults(UIDefaults paramUIDefaults)
/*      */   {
/*  324 */     String[] arrayOfString = { "desktop", "#005C5C", "activeCaption", "#000080", "activeCaptionText", "#FFFFFF", "activeCaptionBorder", "#C0C0C0", "inactiveCaption", "#808080", "inactiveCaptionText", "#C0C0C0", "inactiveCaptionBorder", "#C0C0C0", "window", "#FFFFFF", "windowBorder", "#000000", "windowText", "#000000", "menu", "#C0C0C0", "menuText", "#000000", "text", "#C0C0C0", "textText", "#000000", "textHighlight", "#000080", "textHighlightText", "#FFFFFF", "textInactiveText", "#808080", "control", "#C0C0C0", "controlText", "#000000", "controlHighlight", "#C0C0C0", "controlLtHighlight", "#FFFFFF", "controlShadow", "#808080", "controlDkShadow", "#000000", "scrollbar", "#E0E0E0", "info", "#FFFFE1", "infoText", "#000000" };
/*      */ 
/*  353 */     loadSystemColors(paramUIDefaults, arrayOfString, isNativeLookAndFeel());
/*      */   }
/*      */ 
/*      */   protected void loadSystemColors(UIDefaults paramUIDefaults, String[] paramArrayOfString, boolean paramBoolean)
/*      */   {
/*      */     int i;
/*      */     Color localColor;
/*  408 */     if (paramBoolean)
/*  409 */       for (i = 0; i < paramArrayOfString.length; i += 2) {
/*  410 */         localColor = Color.black;
/*      */         try {
/*  412 */           String str = paramArrayOfString[i];
/*  413 */           localColor = (Color)SystemColor.class.getField(str).get(null);
/*      */         } catch (Exception localException) {
/*      */         }
/*  416 */         paramUIDefaults.put(paramArrayOfString[i], new ColorUIResource(localColor));
/*      */       }
/*      */     else
/*  419 */       for (i = 0; i < paramArrayOfString.length; i += 2) {
/*  420 */         localColor = Color.black;
/*      */         try {
/*  422 */           localColor = Color.decode(paramArrayOfString[(i + 1)]);
/*      */         }
/*      */         catch (NumberFormatException localNumberFormatException) {
/*  425 */           localNumberFormatException.printStackTrace();
/*      */         }
/*  427 */         paramUIDefaults.put(paramArrayOfString[i], new ColorUIResource(localColor));
/*      */       }
/*      */   }
/*      */ 
/*      */   private void initResourceBundle(UIDefaults paramUIDefaults)
/*      */   {
/*  439 */     paramUIDefaults.setDefaultLocale(Locale.getDefault());
/*  440 */     paramUIDefaults.addResourceBundle("com.sun.swing.internal.plaf.basic.resources.basic");
/*      */   }
/*      */ 
/*      */   protected void initComponentDefaults(UIDefaults paramUIDefaults)
/*      */   {
/*  453 */     initResourceBundle(paramUIDefaults);
/*      */ 
/*  456 */     Integer localInteger1 = new Integer(500);
/*      */ 
/*  459 */     Long localLong = new Long(1000L);
/*      */ 
/*  462 */     Integer localInteger2 = new Integer(12);
/*  463 */     Integer localInteger3 = new Integer(0);
/*  464 */     Integer localInteger4 = new Integer(1);
/*  465 */     SwingLazyValue localSwingLazyValue1 = new SwingLazyValue("javax.swing.plaf.FontUIResource", null, new Object[] { "Dialog", localInteger3, localInteger2 });
/*      */ 
/*  469 */     SwingLazyValue localSwingLazyValue2 = new SwingLazyValue("javax.swing.plaf.FontUIResource", null, new Object[] { "Serif", localInteger3, localInteger2 });
/*      */ 
/*  473 */     SwingLazyValue localSwingLazyValue3 = new SwingLazyValue("javax.swing.plaf.FontUIResource", null, new Object[] { "SansSerif", localInteger3, localInteger2 });
/*      */ 
/*  477 */     SwingLazyValue localSwingLazyValue4 = new SwingLazyValue("javax.swing.plaf.FontUIResource", null, new Object[] { "Monospaced", localInteger3, localInteger2 });
/*      */ 
/*  481 */     SwingLazyValue localSwingLazyValue5 = new SwingLazyValue("javax.swing.plaf.FontUIResource", null, new Object[] { "Dialog", localInteger4, localInteger2 });
/*      */ 
/*  488 */     ColorUIResource localColorUIResource1 = new ColorUIResource(Color.red);
/*  489 */     ColorUIResource localColorUIResource2 = new ColorUIResource(Color.black);
/*  490 */     ColorUIResource localColorUIResource3 = new ColorUIResource(Color.white);
/*  491 */     ColorUIResource localColorUIResource4 = new ColorUIResource(Color.yellow);
/*  492 */     ColorUIResource localColorUIResource5 = new ColorUIResource(Color.gray);
/*  493 */     ColorUIResource localColorUIResource6 = new ColorUIResource(Color.lightGray);
/*  494 */     ColorUIResource localColorUIResource7 = new ColorUIResource(Color.darkGray);
/*  495 */     ColorUIResource localColorUIResource8 = new ColorUIResource(224, 224, 224);
/*      */ 
/*  497 */     Color localColor1 = paramUIDefaults.getColor("control");
/*  498 */     Color localColor2 = paramUIDefaults.getColor("controlDkShadow");
/*  499 */     Color localColor3 = paramUIDefaults.getColor("controlHighlight");
/*  500 */     Color localColor4 = paramUIDefaults.getColor("controlLtHighlight");
/*  501 */     Color localColor5 = paramUIDefaults.getColor("controlShadow");
/*  502 */     Color localColor6 = paramUIDefaults.getColor("controlText");
/*  503 */     Color localColor7 = paramUIDefaults.getColor("menu");
/*  504 */     Color localColor8 = paramUIDefaults.getColor("menuText");
/*  505 */     Color localColor9 = paramUIDefaults.getColor("textHighlight");
/*  506 */     Color localColor10 = paramUIDefaults.getColor("textHighlightText");
/*  507 */     Color localColor11 = paramUIDefaults.getColor("textInactiveText");
/*  508 */     Color localColor12 = paramUIDefaults.getColor("textText");
/*  509 */     Color localColor13 = paramUIDefaults.getColor("window");
/*      */ 
/*  512 */     InsetsUIResource localInsetsUIResource1 = new InsetsUIResource(0, 0, 0, 0);
/*  513 */     InsetsUIResource localInsetsUIResource2 = new InsetsUIResource(2, 2, 2, 2);
/*  514 */     InsetsUIResource localInsetsUIResource3 = new InsetsUIResource(3, 3, 3, 3);
/*      */ 
/*  517 */     SwingLazyValue localSwingLazyValue6 = new SwingLazyValue("javax.swing.plaf.basic.BasicBorders$MarginBorder");
/*      */ 
/*  519 */     SwingLazyValue localSwingLazyValue7 = new SwingLazyValue("javax.swing.plaf.BorderUIResource", "getEtchedBorderUIResource");
/*      */ 
/*  522 */     SwingLazyValue localSwingLazyValue8 = new SwingLazyValue("javax.swing.plaf.BorderUIResource", "getLoweredBevelBorderUIResource");
/*      */ 
/*  526 */     SwingLazyValue localSwingLazyValue9 = new SwingLazyValue("javax.swing.plaf.basic.BasicBorders", "getInternalFrameBorder");
/*      */ 
/*  530 */     SwingLazyValue localSwingLazyValue10 = new SwingLazyValue("javax.swing.plaf.BorderUIResource", "getBlackLineBorderUIResource");
/*      */ 
/*  533 */     SwingLazyValue localSwingLazyValue11 = new SwingLazyValue("javax.swing.plaf.BorderUIResource$LineBorderUIResource", null, new Object[] { localColorUIResource4 });
/*      */ 
/*  538 */     BorderUIResource.EmptyBorderUIResource localEmptyBorderUIResource = new BorderUIResource.EmptyBorderUIResource(1, 1, 1, 1);
/*      */ 
/*  540 */     SwingLazyValue localSwingLazyValue12 = new SwingLazyValue("javax.swing.plaf.BorderUIResource$BevelBorderUIResource", null, new Object[] { new Integer(0), localColor4, localColor1, localColor2, localColor5 });
/*      */ 
/*  552 */     SwingLazyValue localSwingLazyValue13 = new SwingLazyValue("javax.swing.plaf.basic.BasicBorders", "getButtonBorder");
/*      */ 
/*  557 */     SwingLazyValue localSwingLazyValue14 = new SwingLazyValue("javax.swing.plaf.basic.BasicBorders", "getToggleButtonBorder");
/*      */ 
/*  562 */     SwingLazyValue localSwingLazyValue15 = new SwingLazyValue("javax.swing.plaf.basic.BasicBorders", "getRadioButtonBorder");
/*      */ 
/*  569 */     Object localObject1 = SwingUtilities2.makeIcon(getClass(), BasicLookAndFeel.class, "icons/NewFolder.gif");
/*      */ 
/*  572 */     Object localObject2 = SwingUtilities2.makeIcon(getClass(), BasicLookAndFeel.class, "icons/UpFolder.gif");
/*      */ 
/*  575 */     Object localObject3 = SwingUtilities2.makeIcon(getClass(), BasicLookAndFeel.class, "icons/HomeFolder.gif");
/*      */ 
/*  578 */     Object localObject4 = SwingUtilities2.makeIcon(getClass(), BasicLookAndFeel.class, "icons/DetailsView.gif");
/*      */ 
/*  581 */     Object localObject5 = SwingUtilities2.makeIcon(getClass(), BasicLookAndFeel.class, "icons/ListView.gif");
/*      */ 
/*  584 */     Object localObject6 = SwingUtilities2.makeIcon(getClass(), BasicLookAndFeel.class, "icons/Directory.gif");
/*      */ 
/*  587 */     Object localObject7 = SwingUtilities2.makeIcon(getClass(), BasicLookAndFeel.class, "icons/File.gif");
/*      */ 
/*  590 */     Object localObject8 = SwingUtilities2.makeIcon(getClass(), BasicLookAndFeel.class, "icons/Computer.gif");
/*      */ 
/*  593 */     Object localObject9 = SwingUtilities2.makeIcon(getClass(), BasicLookAndFeel.class, "icons/HardDrive.gif");
/*      */ 
/*  596 */     Object localObject10 = SwingUtilities2.makeIcon(getClass(), BasicLookAndFeel.class, "icons/FloppyDrive.gif");
/*      */ 
/*  603 */     SwingLazyValue localSwingLazyValue16 = new SwingLazyValue("javax.swing.plaf.basic.BasicBorders", "getInternalFrameBorder");
/*      */ 
/*  609 */     UIDefaults.ActiveValue local2 = new UIDefaults.ActiveValue() {
/*      */       public Object createValue(UIDefaults paramAnonymousUIDefaults) {
/*  611 */         return new DefaultListCellRenderer.UIResource();
/*      */       }
/*      */     };
/*  618 */     SwingLazyValue localSwingLazyValue17 = new SwingLazyValue("javax.swing.plaf.basic.BasicBorders", "getMenuBarBorder");
/*      */ 
/*  623 */     SwingLazyValue localSwingLazyValue18 = new SwingLazyValue("javax.swing.plaf.basic.BasicIconFactory", "getMenuItemCheckIcon");
/*      */ 
/*  628 */     SwingLazyValue localSwingLazyValue19 = new SwingLazyValue("javax.swing.plaf.basic.BasicIconFactory", "getMenuItemArrowIcon");
/*      */ 
/*  634 */     SwingLazyValue localSwingLazyValue20 = new SwingLazyValue("javax.swing.plaf.basic.BasicIconFactory", "getMenuArrowIcon");
/*      */ 
/*  639 */     SwingLazyValue localSwingLazyValue21 = new SwingLazyValue("javax.swing.plaf.basic.BasicIconFactory", "getCheckBoxIcon");
/*      */ 
/*  644 */     SwingLazyValue localSwingLazyValue22 = new SwingLazyValue("javax.swing.plaf.basic.BasicIconFactory", "getRadioButtonIcon");
/*      */ 
/*  649 */     SwingLazyValue localSwingLazyValue23 = new SwingLazyValue("javax.swing.plaf.basic.BasicIconFactory", "getCheckBoxMenuItemIcon");
/*      */ 
/*  654 */     SwingLazyValue localSwingLazyValue24 = new SwingLazyValue("javax.swing.plaf.basic.BasicIconFactory", "getRadioButtonMenuItemIcon");
/*      */ 
/*  659 */     String str = "+";
/*      */ 
/*  663 */     DimensionUIResource localDimensionUIResource1 = new DimensionUIResource(262, 90);
/*      */ 
/*  665 */     Integer localInteger5 = new Integer(0);
/*  666 */     SwingLazyValue localSwingLazyValue25 = new SwingLazyValue("javax.swing.plaf.BorderUIResource$EmptyBorderUIResource", new Object[] { localInteger5, localInteger5, localInteger5, localInteger5 });
/*      */ 
/*  670 */     Integer localInteger6 = new Integer(10);
/*  671 */     SwingLazyValue localSwingLazyValue26 = new SwingLazyValue("javax.swing.plaf.BorderUIResource$EmptyBorderUIResource", new Object[] { localInteger6, localInteger6, localInteger2, localInteger6 });
/*      */ 
/*  675 */     SwingLazyValue localSwingLazyValue27 = new SwingLazyValue("javax.swing.plaf.BorderUIResource$EmptyBorderUIResource", new Object[] { new Integer(6), localInteger5, localInteger5, localInteger5 });
/*      */ 
/*  682 */     SwingLazyValue localSwingLazyValue28 = new SwingLazyValue("javax.swing.plaf.basic.BasicBorders", "getProgressBarBorder");
/*      */ 
/*  689 */     DimensionUIResource localDimensionUIResource2 = new DimensionUIResource(8, 8);
/*  690 */     DimensionUIResource localDimensionUIResource3 = new DimensionUIResource(4096, 4096);
/*      */ 
/*  694 */     InsetsUIResource localInsetsUIResource4 = localInsetsUIResource2;
/*      */ 
/*  696 */     DimensionUIResource localDimensionUIResource4 = new DimensionUIResource(10, 10);
/*      */ 
/*  701 */     SwingLazyValue localSwingLazyValue29 = new SwingLazyValue("javax.swing.plaf.basic.BasicBorders", "getSplitPaneBorder");
/*      */ 
/*  705 */     SwingLazyValue localSwingLazyValue30 = new SwingLazyValue("javax.swing.plaf.basic.BasicBorders", "getSplitPaneDividerBorder");
/*      */ 
/*  712 */     InsetsUIResource localInsetsUIResource5 = new InsetsUIResource(0, 4, 1, 4);
/*      */ 
/*  714 */     InsetsUIResource localInsetsUIResource6 = new InsetsUIResource(2, 2, 2, 1);
/*      */ 
/*  716 */     InsetsUIResource localInsetsUIResource7 = new InsetsUIResource(3, 2, 0, 2);
/*      */ 
/*  718 */     InsetsUIResource localInsetsUIResource8 = new InsetsUIResource(2, 2, 3, 3);
/*      */ 
/*  723 */     SwingLazyValue localSwingLazyValue31 = new SwingLazyValue("javax.swing.plaf.basic.BasicBorders", "getTextFieldBorder");
/*      */ 
/*  728 */     InsetsUIResource localInsetsUIResource9 = localInsetsUIResource3;
/*      */ 
/*  730 */     Integer localInteger7 = localInteger1;
/*  731 */     Integer localInteger8 = new Integer(4);
/*      */ 
/*  733 */     Object[] arrayOfObject1 = { "CheckBoxMenuItem.commandSound", "InternalFrame.closeSound", "InternalFrame.maximizeSound", "InternalFrame.minimizeSound", "InternalFrame.restoreDownSound", "InternalFrame.restoreUpSound", "MenuItem.commandSound", "OptionPane.errorSound", "OptionPane.informationSound", "OptionPane.questionSound", "OptionPane.warningSound", "PopupMenu.popupSound", "RadioButtonMenuItem.commandSound" };
/*      */ 
/*  748 */     Object[] arrayOfObject2 = { "mute" };
/*      */ 
/*  752 */     Object[] arrayOfObject3 = { "AuditoryCues.cueList", arrayOfObject1, "AuditoryCues.allAuditoryCues", arrayOfObject1, "AuditoryCues.noAuditoryCues", arrayOfObject2, "AuditoryCues.playList", null, "Button.defaultButtonFollowsFocus", Boolean.TRUE, "Button.font", localSwingLazyValue1, "Button.background", localColor1, "Button.foreground", localColor6, "Button.shadow", localColor5, "Button.darkShadow", localColor2, "Button.light", localColor3, "Button.highlight", localColor4, "Button.border", localSwingLazyValue13, "Button.margin", new InsetsUIResource(2, 14, 2, 14), "Button.textIconGap", localInteger8, "Button.textShiftOffset", localInteger5, "Button.focusInputMap", new UIDefaults.LazyInputMap(new Object[] { "SPACE", "pressed", "released SPACE", "released", "ENTER", "pressed", "released ENTER", "released" }), "ToggleButton.font", localSwingLazyValue1, "ToggleButton.background", localColor1, "ToggleButton.foreground", localColor6, "ToggleButton.shadow", localColor5, "ToggleButton.darkShadow", localColor2, "ToggleButton.light", localColor3, "ToggleButton.highlight", localColor4, "ToggleButton.border", localSwingLazyValue14, "ToggleButton.margin", new InsetsUIResource(2, 14, 2, 14), "ToggleButton.textIconGap", localInteger8, "ToggleButton.textShiftOffset", localInteger5, "ToggleButton.focusInputMap", new UIDefaults.LazyInputMap(new Object[] { "SPACE", "pressed", "released SPACE", "released" }), "RadioButton.font", localSwingLazyValue1, "RadioButton.background", localColor1, "RadioButton.foreground", localColor6, "RadioButton.shadow", localColor5, "RadioButton.darkShadow", localColor2, "RadioButton.light", localColor3, "RadioButton.highlight", localColor4, "RadioButton.border", localSwingLazyValue15, "RadioButton.margin", localInsetsUIResource2, "RadioButton.textIconGap", localInteger8, "RadioButton.textShiftOffset", localInteger5, "RadioButton.icon", localSwingLazyValue22, "RadioButton.focusInputMap", new UIDefaults.LazyInputMap(new Object[] { "SPACE", "pressed", "released SPACE", "released", "RETURN", "pressed" }), "CheckBox.font", localSwingLazyValue1, "CheckBox.background", localColor1, "CheckBox.foreground", localColor6, "CheckBox.border", localSwingLazyValue15, "CheckBox.margin", localInsetsUIResource2, "CheckBox.textIconGap", localInteger8, "CheckBox.textShiftOffset", localInteger5, "CheckBox.icon", localSwingLazyValue21, "CheckBox.focusInputMap", new UIDefaults.LazyInputMap(new Object[] { "SPACE", "pressed", "released SPACE", "released" }), "FileChooser.useSystemExtensionHiding", Boolean.FALSE, "ColorChooser.font", localSwingLazyValue1, "ColorChooser.background", localColor1, "ColorChooser.foreground", localColor6, "ColorChooser.swatchesSwatchSize", new Dimension(10, 10), "ColorChooser.swatchesRecentSwatchSize", new Dimension(10, 10), "ColorChooser.swatchesDefaultRecentColor", localColor1, "ComboBox.font", localSwingLazyValue3, "ComboBox.background", localColor13, "ComboBox.foreground", localColor12, "ComboBox.buttonBackground", localColor1, "ComboBox.buttonShadow", localColor5, "ComboBox.buttonDarkShadow", localColor2, "ComboBox.buttonHighlight", localColor4, "ComboBox.selectionBackground", localColor9, "ComboBox.selectionForeground", localColor10, "ComboBox.disabledBackground", localColor1, "ComboBox.disabledForeground", localColor11, "ComboBox.timeFactor", localLong, "ComboBox.isEnterSelectablePopup", Boolean.FALSE, "ComboBox.ancestorInputMap", new UIDefaults.LazyInputMap(new Object[] { "ESCAPE", "hidePopup", "PAGE_UP", "pageUpPassThrough", "PAGE_DOWN", "pageDownPassThrough", "HOME", "homePassThrough", "END", "endPassThrough", "ENTER", "enterPressed" }), "ComboBox.noActionOnKeyNavigation", Boolean.FALSE, "FileChooser.newFolderIcon", localObject1, "FileChooser.upFolderIcon", localObject2, "FileChooser.homeFolderIcon", localObject3, "FileChooser.detailsViewIcon", localObject4, "FileChooser.listViewIcon", localObject5, "FileChooser.readOnly", Boolean.FALSE, "FileChooser.usesSingleFilePane", Boolean.FALSE, "FileChooser.ancestorInputMap", new UIDefaults.LazyInputMap(new Object[] { "ESCAPE", "cancelSelection", "F5", "refresh" }), "FileView.directoryIcon", localObject6, "FileView.fileIcon", localObject7, "FileView.computerIcon", localObject8, "FileView.hardDriveIcon", localObject9, "FileView.floppyDriveIcon", localObject10, "InternalFrame.titleFont", localSwingLazyValue5, "InternalFrame.borderColor", localColor1, "InternalFrame.borderShadow", localColor5, "InternalFrame.borderDarkShadow", localColor2, "InternalFrame.borderHighlight", localColor4, "InternalFrame.borderLight", localColor3, "InternalFrame.border", localSwingLazyValue16, "InternalFrame.icon", SwingUtilities2.makeIcon(getClass(), BasicLookAndFeel.class, "icons/JavaCup16.png"), "InternalFrame.maximizeIcon", new SwingLazyValue("javax.swing.plaf.basic.BasicIconFactory", "createEmptyFrameIcon"), "InternalFrame.minimizeIcon", new SwingLazyValue("javax.swing.plaf.basic.BasicIconFactory", "createEmptyFrameIcon"), "InternalFrame.iconifyIcon", new SwingLazyValue("javax.swing.plaf.basic.BasicIconFactory", "createEmptyFrameIcon"), "InternalFrame.closeIcon", new SwingLazyValue("javax.swing.plaf.basic.BasicIconFactory", "createEmptyFrameIcon"), "InternalFrame.closeSound", null, "InternalFrame.maximizeSound", null, "InternalFrame.minimizeSound", null, "InternalFrame.restoreDownSound", null, "InternalFrame.restoreUpSound", null, "InternalFrame.activeTitleBackground", paramUIDefaults.get("activeCaption"), "InternalFrame.activeTitleForeground", paramUIDefaults.get("activeCaptionText"), "InternalFrame.inactiveTitleBackground", paramUIDefaults.get("inactiveCaption"), "InternalFrame.inactiveTitleForeground", paramUIDefaults.get("inactiveCaptionText"), "InternalFrame.windowBindings", { "shift ESCAPE", "showSystemMenu", "ctrl SPACE", "showSystemMenu", "ESCAPE", "hideSystemMenu" }, "InternalFrameTitlePane.iconifyButtonOpacity", Boolean.TRUE, "InternalFrameTitlePane.maximizeButtonOpacity", Boolean.TRUE, "InternalFrameTitlePane.closeButtonOpacity", Boolean.TRUE, "DesktopIcon.border", localSwingLazyValue16, "Desktop.minOnScreenInsets", localInsetsUIResource3, "Desktop.background", paramUIDefaults.get("desktop"), "Desktop.ancestorInputMap", new UIDefaults.LazyInputMap(new Object[] { "ctrl F5", "restore", "ctrl F4", "close", "ctrl F7", "move", "ctrl F8", "resize", "RIGHT", "right", "KP_RIGHT", "right", "shift RIGHT", "shrinkRight", "shift KP_RIGHT", "shrinkRight", "LEFT", "left", "KP_LEFT", "left", "shift LEFT", "shrinkLeft", "shift KP_LEFT", "shrinkLeft", "UP", "up", "KP_UP", "up", "shift UP", "shrinkUp", "shift KP_UP", "shrinkUp", "DOWN", "down", "KP_DOWN", "down", "shift DOWN", "shrinkDown", "shift KP_DOWN", "shrinkDown", "ESCAPE", "escape", "ctrl F9", "minimize", "ctrl F10", "maximize", "ctrl F6", "selectNextFrame", "ctrl TAB", "selectNextFrame", "ctrl alt F6", "selectNextFrame", "shift ctrl alt F6", "selectPreviousFrame", "ctrl F12", "navigateNext", "shift ctrl F12", "navigatePrevious" }), "Label.font", localSwingLazyValue1, "Label.background", localColor1, "Label.foreground", localColor6, "Label.disabledForeground", localColorUIResource3, "Label.disabledShadow", localColor5, "Label.border", null, "List.font", localSwingLazyValue1, "List.background", localColor13, "List.foreground", localColor12, "List.selectionBackground", localColor9, "List.selectionForeground", localColor10, "List.noFocusBorder", localEmptyBorderUIResource, "List.focusCellHighlightBorder", localSwingLazyValue11, "List.dropLineColor", localColor5, "List.border", null, "List.cellRenderer", local2, "List.timeFactor", localLong, "List.focusInputMap", new UIDefaults.LazyInputMap(new Object[] { "ctrl C", "copy", "ctrl V", "paste", "ctrl X", "cut", "COPY", "copy", "PASTE", "paste", "CUT", "cut", "control INSERT", "copy", "shift INSERT", "paste", "shift DELETE", "cut", "UP", "selectPreviousRow", "KP_UP", "selectPreviousRow", "shift UP", "selectPreviousRowExtendSelection", "shift KP_UP", "selectPreviousRowExtendSelection", "ctrl shift UP", "selectPreviousRowExtendSelection", "ctrl shift KP_UP", "selectPreviousRowExtendSelection", "ctrl UP", "selectPreviousRowChangeLead", "ctrl KP_UP", "selectPreviousRowChangeLead", "DOWN", "selectNextRow", "KP_DOWN", "selectNextRow", "shift DOWN", "selectNextRowExtendSelection", "shift KP_DOWN", "selectNextRowExtendSelection", "ctrl shift DOWN", "selectNextRowExtendSelection", "ctrl shift KP_DOWN", "selectNextRowExtendSelection", "ctrl DOWN", "selectNextRowChangeLead", "ctrl KP_DOWN", "selectNextRowChangeLead", "LEFT", "selectPreviousColumn", "KP_LEFT", "selectPreviousColumn", "shift LEFT", "selectPreviousColumnExtendSelection", "shift KP_LEFT", "selectPreviousColumnExtendSelection", "ctrl shift LEFT", "selectPreviousColumnExtendSelection", "ctrl shift KP_LEFT", "selectPreviousColumnExtendSelection", "ctrl LEFT", "selectPreviousColumnChangeLead", "ctrl KP_LEFT", "selectPreviousColumnChangeLead", "RIGHT", "selectNextColumn", "KP_RIGHT", "selectNextColumn", "shift RIGHT", "selectNextColumnExtendSelection", "shift KP_RIGHT", "selectNextColumnExtendSelection", "ctrl shift RIGHT", "selectNextColumnExtendSelection", "ctrl shift KP_RIGHT", "selectNextColumnExtendSelection", "ctrl RIGHT", "selectNextColumnChangeLead", "ctrl KP_RIGHT", "selectNextColumnChangeLead", "HOME", "selectFirstRow", "shift HOME", "selectFirstRowExtendSelection", "ctrl shift HOME", "selectFirstRowExtendSelection", "ctrl HOME", "selectFirstRowChangeLead", "END", "selectLastRow", "shift END", "selectLastRowExtendSelection", "ctrl shift END", "selectLastRowExtendSelection", "ctrl END", "selectLastRowChangeLead", "PAGE_UP", "scrollUp", "shift PAGE_UP", "scrollUpExtendSelection", "ctrl shift PAGE_UP", "scrollUpExtendSelection", "ctrl PAGE_UP", "scrollUpChangeLead", "PAGE_DOWN", "scrollDown", "shift PAGE_DOWN", "scrollDownExtendSelection", "ctrl shift PAGE_DOWN", "scrollDownExtendSelection", "ctrl PAGE_DOWN", "scrollDownChangeLead", "ctrl A", "selectAll", "ctrl SLASH", "selectAll", "ctrl BACK_SLASH", "clearSelection", "SPACE", "addToSelection", "ctrl SPACE", "toggleAndAnchor", "shift SPACE", "extendTo", "ctrl shift SPACE", "moveSelectionTo" }), "List.focusInputMap.RightToLeft", new UIDefaults.LazyInputMap(new Object[] { "LEFT", "selectNextColumn", "KP_LEFT", "selectNextColumn", "shift LEFT", "selectNextColumnExtendSelection", "shift KP_LEFT", "selectNextColumnExtendSelection", "ctrl shift LEFT", "selectNextColumnExtendSelection", "ctrl shift KP_LEFT", "selectNextColumnExtendSelection", "ctrl LEFT", "selectNextColumnChangeLead", "ctrl KP_LEFT", "selectNextColumnChangeLead", "RIGHT", "selectPreviousColumn", "KP_RIGHT", "selectPreviousColumn", "shift RIGHT", "selectPreviousColumnExtendSelection", "shift KP_RIGHT", "selectPreviousColumnExtendSelection", "ctrl shift RIGHT", "selectPreviousColumnExtendSelection", "ctrl shift KP_RIGHT", "selectPreviousColumnExtendSelection", "ctrl RIGHT", "selectPreviousColumnChangeLead", "ctrl KP_RIGHT", "selectPreviousColumnChangeLead" }), "MenuBar.font", localSwingLazyValue1, "MenuBar.background", localColor7, "MenuBar.foreground", localColor8, "MenuBar.shadow", localColor5, "MenuBar.highlight", localColor4, "MenuBar.border", localSwingLazyValue17, "MenuBar.windowBindings", { "F10", "takeFocus" }, "MenuItem.font", localSwingLazyValue1, "MenuItem.acceleratorFont", localSwingLazyValue1, "MenuItem.background", localColor7, "MenuItem.foreground", localColor8, "MenuItem.selectionForeground", localColor10, "MenuItem.selectionBackground", localColor9, "MenuItem.disabledForeground", null, "MenuItem.acceleratorForeground", localColor8, "MenuItem.acceleratorSelectionForeground", localColor10, "MenuItem.acceleratorDelimiter", str, "MenuItem.border", localSwingLazyValue6, "MenuItem.borderPainted", Boolean.FALSE, "MenuItem.margin", localInsetsUIResource2, "MenuItem.checkIcon", localSwingLazyValue18, "MenuItem.arrowIcon", localSwingLazyValue19, "MenuItem.commandSound", null, "RadioButtonMenuItem.font", localSwingLazyValue1, "RadioButtonMenuItem.acceleratorFont", localSwingLazyValue1, "RadioButtonMenuItem.background", localColor7, "RadioButtonMenuItem.foreground", localColor8, "RadioButtonMenuItem.selectionForeground", localColor10, "RadioButtonMenuItem.selectionBackground", localColor9, "RadioButtonMenuItem.disabledForeground", null, "RadioButtonMenuItem.acceleratorForeground", localColor8, "RadioButtonMenuItem.acceleratorSelectionForeground", localColor10, "RadioButtonMenuItem.border", localSwingLazyValue6, "RadioButtonMenuItem.borderPainted", Boolean.FALSE, "RadioButtonMenuItem.margin", localInsetsUIResource2, "RadioButtonMenuItem.checkIcon", localSwingLazyValue24, "RadioButtonMenuItem.arrowIcon", localSwingLazyValue19, "RadioButtonMenuItem.commandSound", null, "CheckBoxMenuItem.font", localSwingLazyValue1, "CheckBoxMenuItem.acceleratorFont", localSwingLazyValue1, "CheckBoxMenuItem.background", localColor7, "CheckBoxMenuItem.foreground", localColor8, "CheckBoxMenuItem.selectionForeground", localColor10, "CheckBoxMenuItem.selectionBackground", localColor9, "CheckBoxMenuItem.disabledForeground", null, "CheckBoxMenuItem.acceleratorForeground", localColor8, "CheckBoxMenuItem.acceleratorSelectionForeground", localColor10, "CheckBoxMenuItem.border", localSwingLazyValue6, "CheckBoxMenuItem.borderPainted", Boolean.FALSE, "CheckBoxMenuItem.margin", localInsetsUIResource2, "CheckBoxMenuItem.checkIcon", localSwingLazyValue23, "CheckBoxMenuItem.arrowIcon", localSwingLazyValue19, "CheckBoxMenuItem.commandSound", null, "Menu.font", localSwingLazyValue1, "Menu.acceleratorFont", localSwingLazyValue1, "Menu.background", localColor7, "Menu.foreground", localColor8, "Menu.selectionForeground", localColor10, "Menu.selectionBackground", localColor9, "Menu.disabledForeground", null, "Menu.acceleratorForeground", localColor8, "Menu.acceleratorSelectionForeground", localColor10, "Menu.border", localSwingLazyValue6, "Menu.borderPainted", Boolean.FALSE, "Menu.margin", localInsetsUIResource2, "Menu.checkIcon", localSwingLazyValue18, "Menu.arrowIcon", localSwingLazyValue20, "Menu.menuPopupOffsetX", new Integer(0), "Menu.menuPopupOffsetY", new Integer(0), "Menu.submenuPopupOffsetX", new Integer(0), "Menu.submenuPopupOffsetY", new Integer(0), "Menu.shortcutKeys", { SwingUtilities2.getSystemMnemonicKeyMask() }, "Menu.crossMenuMnemonic", Boolean.TRUE, "Menu.cancelMode", "hideLastSubmenu", "Menu.preserveTopLevelSelection", Boolean.FALSE, "PopupMenu.font", localSwingLazyValue1, "PopupMenu.background", localColor7, "PopupMenu.foreground", localColor8, "PopupMenu.border", localSwingLazyValue9, "PopupMenu.popupSound", null, "PopupMenu.selectedWindowInputMapBindings", { "ESCAPE", "cancel", "DOWN", "selectNext", "KP_DOWN", "selectNext", "UP", "selectPrevious", "KP_UP", "selectPrevious", "LEFT", "selectParent", "KP_LEFT", "selectParent", "RIGHT", "selectChild", "KP_RIGHT", "selectChild", "ENTER", "return", "ctrl ENTER", "return", "SPACE", "return" }, "PopupMenu.selectedWindowInputMapBindings.RightToLeft", { "LEFT", "selectChild", "KP_LEFT", "selectChild", "RIGHT", "selectParent", "KP_RIGHT", "selectParent" }, "PopupMenu.consumeEventOnClose", Boolean.FALSE, "OptionPane.font", localSwingLazyValue1, "OptionPane.background", localColor1, "OptionPane.foreground", localColor6, "OptionPane.messageForeground", localColor6, "OptionPane.border", localSwingLazyValue26, "OptionPane.messageAreaBorder", localSwingLazyValue25, "OptionPane.buttonAreaBorder", localSwingLazyValue27, "OptionPane.minimumSize", localDimensionUIResource1, "OptionPane.errorIcon", SwingUtilities2.makeIcon(getClass(), BasicLookAndFeel.class, "icons/Error.gif"), "OptionPane.informationIcon", SwingUtilities2.makeIcon(getClass(), BasicLookAndFeel.class, "icons/Inform.gif"), "OptionPane.warningIcon", SwingUtilities2.makeIcon(getClass(), BasicLookAndFeel.class, "icons/Warn.gif"), "OptionPane.questionIcon", SwingUtilities2.makeIcon(getClass(), BasicLookAndFeel.class, "icons/Question.gif"), "OptionPane.windowBindings", { "ESCAPE", "close" }, "OptionPane.errorSound", null, "OptionPane.informationSound", null, "OptionPane.questionSound", null, "OptionPane.warningSound", null, "OptionPane.buttonClickThreshhold", localInteger1, "Panel.font", localSwingLazyValue1, "Panel.background", localColor1, "Panel.foreground", localColor12, "ProgressBar.font", localSwingLazyValue1, "ProgressBar.foreground", localColor9, "ProgressBar.background", localColor1, "ProgressBar.selectionForeground", localColor1, "ProgressBar.selectionBackground", localColor9, "ProgressBar.border", localSwingLazyValue28, "ProgressBar.cellLength", new Integer(1), "ProgressBar.cellSpacing", localInteger5, "ProgressBar.repaintInterval", new Integer(50), "ProgressBar.cycleTime", new Integer(3000), "ProgressBar.horizontalSize", new DimensionUIResource(146, 12), "ProgressBar.verticalSize", new DimensionUIResource(12, 146), "Separator.shadow", localColor5, "Separator.highlight", localColor4, "Separator.background", localColor4, "Separator.foreground", localColor5, "ScrollBar.background", localColorUIResource8, "ScrollBar.foreground", localColor1, "ScrollBar.track", paramUIDefaults.get("scrollbar"), "ScrollBar.trackHighlight", localColor2, "ScrollBar.thumb", localColor1, "ScrollBar.thumbHighlight", localColor4, "ScrollBar.thumbDarkShadow", localColor2, "ScrollBar.thumbShadow", localColor5, "ScrollBar.border", null, "ScrollBar.minimumThumbSize", localDimensionUIResource2, "ScrollBar.maximumThumbSize", localDimensionUIResource3, "ScrollBar.ancestorInputMap", new UIDefaults.LazyInputMap(new Object[] { "RIGHT", "positiveUnitIncrement", "KP_RIGHT", "positiveUnitIncrement", "DOWN", "positiveUnitIncrement", "KP_DOWN", "positiveUnitIncrement", "PAGE_DOWN", "positiveBlockIncrement", "LEFT", "negativeUnitIncrement", "KP_LEFT", "negativeUnitIncrement", "UP", "negativeUnitIncrement", "KP_UP", "negativeUnitIncrement", "PAGE_UP", "negativeBlockIncrement", "HOME", "minScroll", "END", "maxScroll" }), "ScrollBar.ancestorInputMap.RightToLeft", new UIDefaults.LazyInputMap(new Object[] { "RIGHT", "negativeUnitIncrement", "KP_RIGHT", "negativeUnitIncrement", "LEFT", "positiveUnitIncrement", "KP_LEFT", "positiveUnitIncrement" }), "ScrollBar.width", new Integer(16), "ScrollPane.font", localSwingLazyValue1, "ScrollPane.background", localColor1, "ScrollPane.foreground", localColor6, "ScrollPane.border", localSwingLazyValue31, "ScrollPane.viewportBorder", null, "ScrollPane.ancestorInputMap", new UIDefaults.LazyInputMap(new Object[] { "RIGHT", "unitScrollRight", "KP_RIGHT", "unitScrollRight", "DOWN", "unitScrollDown", "KP_DOWN", "unitScrollDown", "LEFT", "unitScrollLeft", "KP_LEFT", "unitScrollLeft", "UP", "unitScrollUp", "KP_UP", "unitScrollUp", "PAGE_UP", "scrollUp", "PAGE_DOWN", "scrollDown", "ctrl PAGE_UP", "scrollLeft", "ctrl PAGE_DOWN", "scrollRight", "ctrl HOME", "scrollHome", "ctrl END", "scrollEnd" }), "ScrollPane.ancestorInputMap.RightToLeft", new UIDefaults.LazyInputMap(new Object[] { "ctrl PAGE_UP", "scrollRight", "ctrl PAGE_DOWN", "scrollLeft" }), "Viewport.font", localSwingLazyValue1, "Viewport.background", localColor1, "Viewport.foreground", localColor12, "Slider.font", localSwingLazyValue1, "Slider.foreground", localColor1, "Slider.background", localColor1, "Slider.highlight", localColor4, "Slider.tickColor", Color.black, "Slider.shadow", localColor5, "Slider.focus", localColor2, "Slider.border", null, "Slider.horizontalSize", new Dimension(200, 21), "Slider.verticalSize", new Dimension(21, 200), "Slider.minimumHorizontalSize", new Dimension(36, 21), "Slider.minimumVerticalSize", new Dimension(21, 36), "Slider.focusInsets", localInsetsUIResource4, "Slider.focusInputMap", new UIDefaults.LazyInputMap(new Object[] { "RIGHT", "positiveUnitIncrement", "KP_RIGHT", "positiveUnitIncrement", "DOWN", "negativeUnitIncrement", "KP_DOWN", "negativeUnitIncrement", "PAGE_DOWN", "negativeBlockIncrement", "LEFT", "negativeUnitIncrement", "KP_LEFT", "negativeUnitIncrement", "UP", "positiveUnitIncrement", "KP_UP", "positiveUnitIncrement", "PAGE_UP", "positiveBlockIncrement", "HOME", "minScroll", "END", "maxScroll" }), "Slider.focusInputMap.RightToLeft", new UIDefaults.LazyInputMap(new Object[] { "RIGHT", "negativeUnitIncrement", "KP_RIGHT", "negativeUnitIncrement", "LEFT", "positiveUnitIncrement", "KP_LEFT", "positiveUnitIncrement" }), "Slider.onlyLeftMouseButtonDrag", Boolean.TRUE, "Spinner.font", localSwingLazyValue4, "Spinner.background", localColor1, "Spinner.foreground", localColor1, "Spinner.border", localSwingLazyValue31, "Spinner.arrowButtonBorder", null, "Spinner.arrowButtonInsets", null, "Spinner.arrowButtonSize", new Dimension(16, 5), "Spinner.ancestorInputMap", new UIDefaults.LazyInputMap(new Object[] { "UP", "increment", "KP_UP", "increment", "DOWN", "decrement", "KP_DOWN", "decrement" }), "Spinner.editorBorderPainted", Boolean.FALSE, "Spinner.editorAlignment", Integer.valueOf(11), "SplitPane.background", localColor1, "SplitPane.highlight", localColor4, "SplitPane.shadow", localColor5, "SplitPane.darkShadow", localColor2, "SplitPane.border", localSwingLazyValue29, "SplitPane.dividerSize", new Integer(7), "SplitPaneDivider.border", localSwingLazyValue30, "SplitPaneDivider.draggingColor", localColorUIResource7, "SplitPane.ancestorInputMap", new UIDefaults.LazyInputMap(new Object[] { "UP", "negativeIncrement", "DOWN", "positiveIncrement", "LEFT", "negativeIncrement", "RIGHT", "positiveIncrement", "KP_UP", "negativeIncrement", "KP_DOWN", "positiveIncrement", "KP_LEFT", "negativeIncrement", "KP_RIGHT", "positiveIncrement", "HOME", "selectMin", "END", "selectMax", "F8", "startResize", "F6", "toggleFocus", "ctrl TAB", "focusOutForward", "ctrl shift TAB", "focusOutBackward" }), "TabbedPane.font", localSwingLazyValue1, "TabbedPane.background", localColor1, "TabbedPane.foreground", localColor6, "TabbedPane.highlight", localColor4, "TabbedPane.light", localColor3, "TabbedPane.shadow", localColor5, "TabbedPane.darkShadow", localColor2, "TabbedPane.selected", null, "TabbedPane.focus", localColor6, "TabbedPane.textIconGap", localInteger8, "TabbedPane.tabsOverlapBorder", Boolean.FALSE, "TabbedPane.selectionFollowsFocus", Boolean.TRUE, "TabbedPane.labelShift", Integer.valueOf(1), "TabbedPane.selectedLabelShift", Integer.valueOf(-1), "TabbedPane.tabInsets", localInsetsUIResource5, "TabbedPane.selectedTabPadInsets", localInsetsUIResource6, "TabbedPane.tabAreaInsets", localInsetsUIResource7, "TabbedPane.contentBorderInsets", localInsetsUIResource8, "TabbedPane.tabRunOverlay", new Integer(2), "TabbedPane.tabsOpaque", Boolean.TRUE, "TabbedPane.contentOpaque", Boolean.TRUE, "TabbedPane.focusInputMap", new UIDefaults.LazyInputMap(new Object[] { "RIGHT", "navigateRight", "KP_RIGHT", "navigateRight", "LEFT", "navigateLeft", "KP_LEFT", "navigateLeft", "UP", "navigateUp", "KP_UP", "navigateUp", "DOWN", "navigateDown", "KP_DOWN", "navigateDown", "ctrl DOWN", "requestFocusForVisibleComponent", "ctrl KP_DOWN", "requestFocusForVisibleComponent" }), "TabbedPane.ancestorInputMap", new UIDefaults.LazyInputMap(new Object[] { "ctrl PAGE_DOWN", "navigatePageDown", "ctrl PAGE_UP", "navigatePageUp", "ctrl UP", "requestFocus", "ctrl KP_UP", "requestFocus" }), "Table.font", localSwingLazyValue1, "Table.foreground", localColor6, "Table.background", localColor13, "Table.selectionForeground", localColor10, "Table.selectionBackground", localColor9, "Table.dropLineColor", localColor5, "Table.dropLineShortColor", localColorUIResource2, "Table.gridColor", localColorUIResource5, "Table.focusCellBackground", localColor13, "Table.focusCellForeground", localColor6, "Table.focusCellHighlightBorder", localSwingLazyValue11, "Table.scrollPaneBorder", localSwingLazyValue8, "Table.ancestorInputMap", new UIDefaults.LazyInputMap(new Object[] { "ctrl C", "copy", "ctrl V", "paste", "ctrl X", "cut", "COPY", "copy", "PASTE", "paste", "CUT", "cut", "control INSERT", "copy", "shift INSERT", "paste", "shift DELETE", "cut", "RIGHT", "selectNextColumn", "KP_RIGHT", "selectNextColumn", "shift RIGHT", "selectNextColumnExtendSelection", "shift KP_RIGHT", "selectNextColumnExtendSelection", "ctrl shift RIGHT", "selectNextColumnExtendSelection", "ctrl shift KP_RIGHT", "selectNextColumnExtendSelection", "ctrl RIGHT", "selectNextColumnChangeLead", "ctrl KP_RIGHT", "selectNextColumnChangeLead", "LEFT", "selectPreviousColumn", "KP_LEFT", "selectPreviousColumn", "shift LEFT", "selectPreviousColumnExtendSelection", "shift KP_LEFT", "selectPreviousColumnExtendSelection", "ctrl shift LEFT", "selectPreviousColumnExtendSelection", "ctrl shift KP_LEFT", "selectPreviousColumnExtendSelection", "ctrl LEFT", "selectPreviousColumnChangeLead", "ctrl KP_LEFT", "selectPreviousColumnChangeLead", "DOWN", "selectNextRow", "KP_DOWN", "selectNextRow", "shift DOWN", "selectNextRowExtendSelection", "shift KP_DOWN", "selectNextRowExtendSelection", "ctrl shift DOWN", "selectNextRowExtendSelection", "ctrl shift KP_DOWN", "selectNextRowExtendSelection", "ctrl DOWN", "selectNextRowChangeLead", "ctrl KP_DOWN", "selectNextRowChangeLead", "UP", "selectPreviousRow", "KP_UP", "selectPreviousRow", "shift UP", "selectPreviousRowExtendSelection", "shift KP_UP", "selectPreviousRowExtendSelection", "ctrl shift UP", "selectPreviousRowExtendSelection", "ctrl shift KP_UP", "selectPreviousRowExtendSelection", "ctrl UP", "selectPreviousRowChangeLead", "ctrl KP_UP", "selectPreviousRowChangeLead", "HOME", "selectFirstColumn", "shift HOME", "selectFirstColumnExtendSelection", "ctrl shift HOME", "selectFirstRowExtendSelection", "ctrl HOME", "selectFirstRow", "END", "selectLastColumn", "shift END", "selectLastColumnExtendSelection", "ctrl shift END", "selectLastRowExtendSelection", "ctrl END", "selectLastRow", "PAGE_UP", "scrollUpChangeSelection", "shift PAGE_UP", "scrollUpExtendSelection", "ctrl shift PAGE_UP", "scrollLeftExtendSelection", "ctrl PAGE_UP", "scrollLeftChangeSelection", "PAGE_DOWN", "scrollDownChangeSelection", "shift PAGE_DOWN", "scrollDownExtendSelection", "ctrl shift PAGE_DOWN", "scrollRightExtendSelection", "ctrl PAGE_DOWN", "scrollRightChangeSelection", "TAB", "selectNextColumnCell", "shift TAB", "selectPreviousColumnCell", "ENTER", "selectNextRowCell", "shift ENTER", "selectPreviousRowCell", "ctrl A", "selectAll", "ctrl SLASH", "selectAll", "ctrl BACK_SLASH", "clearSelection", "ESCAPE", "cancel", "F2", "startEditing", "SPACE", "addToSelection", "ctrl SPACE", "toggleAndAnchor", "shift SPACE", "extendTo", "ctrl shift SPACE", "moveSelectionTo", "F8", "focusHeader" }), "Table.ancestorInputMap.RightToLeft", new UIDefaults.LazyInputMap(new Object[] { "RIGHT", "selectPreviousColumn", "KP_RIGHT", "selectPreviousColumn", "shift RIGHT", "selectPreviousColumnExtendSelection", "shift KP_RIGHT", "selectPreviousColumnExtendSelection", "ctrl shift RIGHT", "selectPreviousColumnExtendSelection", "ctrl shift KP_RIGHT", "selectPreviousColumnExtendSelection", "ctrl RIGHT", "selectPreviousColumnChangeLead", "ctrl KP_RIGHT", "selectPreviousColumnChangeLead", "LEFT", "selectNextColumn", "KP_LEFT", "selectNextColumn", "shift LEFT", "selectNextColumnExtendSelection", "shift KP_LEFT", "selectNextColumnExtendSelection", "ctrl shift LEFT", "selectNextColumnExtendSelection", "ctrl shift KP_LEFT", "selectNextColumnExtendSelection", "ctrl LEFT", "selectNextColumnChangeLead", "ctrl KP_LEFT", "selectNextColumnChangeLead", "ctrl PAGE_UP", "scrollRightChangeSelection", "ctrl PAGE_DOWN", "scrollLeftChangeSelection", "ctrl shift PAGE_UP", "scrollRightExtendSelection", "ctrl shift PAGE_DOWN", "scrollLeftExtendSelection" }), "Table.ascendingSortIcon", new SwingLazyValue("sun.swing.icon.SortArrowIcon", null, new Object[] { Boolean.TRUE, "Table.sortIconColor" }), "Table.descendingSortIcon", new SwingLazyValue("sun.swing.icon.SortArrowIcon", null, new Object[] { Boolean.FALSE, "Table.sortIconColor" }), "Table.sortIconColor", localColor5, "TableHeader.font", localSwingLazyValue1, "TableHeader.foreground", localColor6, "TableHeader.background", localColor1, "TableHeader.cellBorder", localSwingLazyValue12, "TableHeader.focusCellBackground", paramUIDefaults.getColor("text"), "TableHeader.focusCellForeground", null, "TableHeader.focusCellBorder", null, "TableHeader.ancestorInputMap", new UIDefaults.LazyInputMap(new Object[] { "SPACE", "toggleSortOrder", "LEFT", "selectColumnToLeft", "KP_LEFT", "selectColumnToLeft", "RIGHT", "selectColumnToRight", "KP_RIGHT", "selectColumnToRight", "alt LEFT", "moveColumnLeft", "alt KP_LEFT", "moveColumnLeft", "alt RIGHT", "moveColumnRight", "alt KP_RIGHT", "moveColumnRight", "alt shift LEFT", "resizeLeft", "alt shift KP_LEFT", "resizeLeft", "alt shift RIGHT", "resizeRight", "alt shift KP_RIGHT", "resizeRight", "ESCAPE", "focusTable" }), "TextField.font", localSwingLazyValue3, "TextField.background", localColor13, "TextField.foreground", localColor12, "TextField.shadow", localColor5, "TextField.darkShadow", localColor2, "TextField.light", localColor3, "TextField.highlight", localColor4, "TextField.inactiveForeground", localColor11, "TextField.inactiveBackground", localColor1, "TextField.selectionBackground", localColor9, "TextField.selectionForeground", localColor10, "TextField.caretForeground", localColor12, "TextField.caretBlinkRate", localInteger7, "TextField.border", localSwingLazyValue31, "TextField.margin", localInsetsUIResource1, "FormattedTextField.font", localSwingLazyValue3, "FormattedTextField.background", localColor13, "FormattedTextField.foreground", localColor12, "FormattedTextField.inactiveForeground", localColor11, "FormattedTextField.inactiveBackground", localColor1, "FormattedTextField.selectionBackground", localColor9, "FormattedTextField.selectionForeground", localColor10, "FormattedTextField.caretForeground", localColor12, "FormattedTextField.caretBlinkRate", localInteger7, "FormattedTextField.border", localSwingLazyValue31, "FormattedTextField.margin", localInsetsUIResource1, "FormattedTextField.focusInputMap", new UIDefaults.LazyInputMap(new Object[] { "ctrl C", "copy-to-clipboard", "ctrl V", "paste-from-clipboard", "ctrl X", "cut-to-clipboard", "COPY", "copy-to-clipboard", "PASTE", "paste-from-clipboard", "CUT", "cut-to-clipboard", "control INSERT", "copy-to-clipboard", "shift INSERT", "paste-from-clipboard", "shift DELETE", "cut-to-clipboard", "shift LEFT", "selection-backward", "shift KP_LEFT", "selection-backward", "shift RIGHT", "selection-forward", "shift KP_RIGHT", "selection-forward", "ctrl LEFT", "caret-previous-word", "ctrl KP_LEFT", "caret-previous-word", "ctrl RIGHT", "caret-next-word", "ctrl KP_RIGHT", "caret-next-word", "ctrl shift LEFT", "selection-previous-word", "ctrl shift KP_LEFT", "selection-previous-word", "ctrl shift RIGHT", "selection-next-word", "ctrl shift KP_RIGHT", "selection-next-word", "ctrl A", "select-all", "HOME", "caret-begin-line", "END", "caret-end-line", "shift HOME", "selection-begin-line", "shift END", "selection-end-line", "BACK_SPACE", "delete-previous", "shift BACK_SPACE", "delete-previous", "ctrl H", "delete-previous", "DELETE", "delete-next", "ctrl DELETE", "delete-next-word", "ctrl BACK_SPACE", "delete-previous-word", "RIGHT", "caret-forward", "LEFT", "caret-backward", "KP_RIGHT", "caret-forward", "KP_LEFT", "caret-backward", "ENTER", "notify-field-accept", "ctrl BACK_SLASH", "unselect", "control shift O", "toggle-componentOrientation", "ESCAPE", "reset-field-edit", "UP", "increment", "KP_UP", "increment", "DOWN", "decrement", "KP_DOWN", "decrement" }), "PasswordField.font", localSwingLazyValue4, "PasswordField.background", localColor13, "PasswordField.foreground", localColor12, "PasswordField.inactiveForeground", localColor11, "PasswordField.inactiveBackground", localColor1, "PasswordField.selectionBackground", localColor9, "PasswordField.selectionForeground", localColor10, "PasswordField.caretForeground", localColor12, "PasswordField.caretBlinkRate", localInteger7, "PasswordField.border", localSwingLazyValue31, "PasswordField.margin", localInsetsUIResource1, "PasswordField.echoChar", Character.valueOf('*'), "TextArea.font", localSwingLazyValue4, "TextArea.background", localColor13, "TextArea.foreground", localColor12, "TextArea.inactiveForeground", localColor11, "TextArea.selectionBackground", localColor9, "TextArea.selectionForeground", localColor10, "TextArea.caretForeground", localColor12, "TextArea.caretBlinkRate", localInteger7, "TextArea.border", localSwingLazyValue6, "TextArea.margin", localInsetsUIResource1, "TextPane.font", localSwingLazyValue2, "TextPane.background", localColorUIResource3, "TextPane.foreground", localColor12, "TextPane.selectionBackground", localColor9, "TextPane.selectionForeground", localColor10, "TextPane.caretForeground", localColor12, "TextPane.caretBlinkRate", localInteger7, "TextPane.inactiveForeground", localColor11, "TextPane.border", localSwingLazyValue6, "TextPane.margin", localInsetsUIResource9, "EditorPane.font", localSwingLazyValue2, "EditorPane.background", localColorUIResource3, "EditorPane.foreground", localColor12, "EditorPane.selectionBackground", localColor9, "EditorPane.selectionForeground", localColor10, "EditorPane.caretForeground", localColor12, "EditorPane.caretBlinkRate", localInteger7, "EditorPane.inactiveForeground", localColor11, "EditorPane.border", localSwingLazyValue6, "EditorPane.margin", localInsetsUIResource9, "html.pendingImage", SwingUtilities2.makeIcon(getClass(), BasicLookAndFeel.class, "icons/image-delayed.png"), "html.missingImage", SwingUtilities2.makeIcon(getClass(), BasicLookAndFeel.class, "icons/image-failed.png"), "TitledBorder.font", localSwingLazyValue1, "TitledBorder.titleColor", localColor6, "TitledBorder.border", localSwingLazyValue7, "ToolBar.font", localSwingLazyValue1, "ToolBar.background", localColor1, "ToolBar.foreground", localColor6, "ToolBar.shadow", localColor5, "ToolBar.darkShadow", localColor2, "ToolBar.light", localColor3, "ToolBar.highlight", localColor4, "ToolBar.dockingBackground", localColor1, "ToolBar.dockingForeground", localColorUIResource1, "ToolBar.floatingBackground", localColor1, "ToolBar.floatingForeground", localColorUIResource7, "ToolBar.border", localSwingLazyValue7, "ToolBar.separatorSize", localDimensionUIResource4, "ToolBar.ancestorInputMap", new UIDefaults.LazyInputMap(new Object[] { "UP", "navigateUp", "KP_UP", "navigateUp", "DOWN", "navigateDown", "KP_DOWN", "navigateDown", "LEFT", "navigateLeft", "KP_LEFT", "navigateLeft", "RIGHT", "navigateRight", "KP_RIGHT", "navigateRight" }), "ToolTip.font", localSwingLazyValue3, "ToolTip.background", paramUIDefaults.get("info"), "ToolTip.foreground", paramUIDefaults.get("infoText"), "ToolTip.border", localSwingLazyValue10, "ToolTipManager.enableToolTipMode", "allWindows", "Tree.paintLines", Boolean.TRUE, "Tree.lineTypeDashed", Boolean.FALSE, "Tree.font", localSwingLazyValue1, "Tree.background", localColor13, "Tree.foreground", localColor12, "Tree.hash", localColorUIResource5, "Tree.textForeground", localColor12, "Tree.textBackground", paramUIDefaults.get("text"), "Tree.selectionForeground", localColor10, "Tree.selectionBackground", localColor9, "Tree.selectionBorderColor", localColorUIResource2, "Tree.dropLineColor", localColor5, "Tree.editorBorder", localSwingLazyValue10, "Tree.leftChildIndent", new Integer(7), "Tree.rightChildIndent", new Integer(13), "Tree.rowHeight", new Integer(16), "Tree.scrollsOnExpand", Boolean.TRUE, "Tree.openIcon", SwingUtilities2.makeIcon(getClass(), BasicLookAndFeel.class, "icons/TreeOpen.gif"), "Tree.closedIcon", SwingUtilities2.makeIcon(getClass(), BasicLookAndFeel.class, "icons/TreeClosed.gif"), "Tree.leafIcon", SwingUtilities2.makeIcon(getClass(), BasicLookAndFeel.class, "icons/TreeLeaf.gif"), "Tree.expandedIcon", null, "Tree.collapsedIcon", null, "Tree.changeSelectionWithFocus", Boolean.TRUE, "Tree.drawsFocusBorderAroundIcon", Boolean.FALSE, "Tree.timeFactor", localLong, "Tree.focusInputMap", new UIDefaults.LazyInputMap(new Object[] { "ctrl C", "copy", "ctrl V", "paste", "ctrl X", "cut", "COPY", "copy", "PASTE", "paste", "CUT", "cut", "control INSERT", "copy", "shift INSERT", "paste", "shift DELETE", "cut", "UP", "selectPrevious", "KP_UP", "selectPrevious", "shift UP", "selectPreviousExtendSelection", "shift KP_UP", "selectPreviousExtendSelection", "ctrl shift UP", "selectPreviousExtendSelection", "ctrl shift KP_UP", "selectPreviousExtendSelection", "ctrl UP", "selectPreviousChangeLead", "ctrl KP_UP", "selectPreviousChangeLead", "DOWN", "selectNext", "KP_DOWN", "selectNext", "shift DOWN", "selectNextExtendSelection", "shift KP_DOWN", "selectNextExtendSelection", "ctrl shift DOWN", "selectNextExtendSelection", "ctrl shift KP_DOWN", "selectNextExtendSelection", "ctrl DOWN", "selectNextChangeLead", "ctrl KP_DOWN", "selectNextChangeLead", "RIGHT", "selectChild", "KP_RIGHT", "selectChild", "LEFT", "selectParent", "KP_LEFT", "selectParent", "PAGE_UP", "scrollUpChangeSelection", "shift PAGE_UP", "scrollUpExtendSelection", "ctrl shift PAGE_UP", "scrollUpExtendSelection", "ctrl PAGE_UP", "scrollUpChangeLead", "PAGE_DOWN", "scrollDownChangeSelection", "shift PAGE_DOWN", "scrollDownExtendSelection", "ctrl shift PAGE_DOWN", "scrollDownExtendSelection", "ctrl PAGE_DOWN", "scrollDownChangeLead", "HOME", "selectFirst", "shift HOME", "selectFirstExtendSelection", "ctrl shift HOME", "selectFirstExtendSelection", "ctrl HOME", "selectFirstChangeLead", "END", "selectLast", "shift END", "selectLastExtendSelection", "ctrl shift END", "selectLastExtendSelection", "ctrl END", "selectLastChangeLead", "F2", "startEditing", "ctrl A", "selectAll", "ctrl SLASH", "selectAll", "ctrl BACK_SLASH", "clearSelection", "ctrl LEFT", "scrollLeft", "ctrl KP_LEFT", "scrollLeft", "ctrl RIGHT", "scrollRight", "ctrl KP_RIGHT", "scrollRight", "SPACE", "addToSelection", "ctrl SPACE", "toggleAndAnchor", "shift SPACE", "extendTo", "ctrl shift SPACE", "moveSelectionTo" }), "Tree.focusInputMap.RightToLeft", new UIDefaults.LazyInputMap(new Object[] { "RIGHT", "selectParent", "KP_RIGHT", "selectParent", "LEFT", "selectChild", "KP_LEFT", "selectChild" }), "Tree.ancestorInputMap", new UIDefaults.LazyInputMap(new Object[] { "ESCAPE", "cancel" }), "RootPane.ancestorInputMap", new UIDefaults.LazyInputMap(new Object[] { "shift F10", "postPopup", "CONTEXT_MENU", "postPopup" }), "RootPane.defaultButtonWindowKeyBindings", { "ENTER", "press", "released ENTER", "release", "ctrl ENTER", "press", "ctrl released ENTER", "release" } };
/*      */ 
/* 1914 */     paramUIDefaults.putDefaults(arrayOfObject3);
/*      */   }
/*      */ 
/*      */   static int getFocusAcceleratorKeyMask() {
/* 1918 */     Toolkit localToolkit = Toolkit.getDefaultToolkit();
/* 1919 */     if ((localToolkit instanceof SunToolkit)) {
/* 1920 */       return ((SunToolkit)localToolkit).getFocusAcceleratorKeyMask();
/*      */     }
/* 1922 */     return 8;
/*      */   }
/*      */ 
/*      */   static Object getUIOfType(ComponentUI paramComponentUI, Class paramClass)
/*      */   {
/* 1932 */     if (paramClass.isInstance(paramComponentUI)) {
/* 1933 */       return paramComponentUI;
/*      */     }
/* 1935 */     return null;
/*      */   }
/*      */ 
/*      */   protected ActionMap getAudioActionMap()
/*      */   {
/* 1990 */     Object localObject = (ActionMap)UIManager.get("AuditoryCues.actionMap");
/*      */ 
/* 1992 */     if (localObject == null) {
/* 1993 */       Object[] arrayOfObject = (Object[])UIManager.get("AuditoryCues.cueList");
/* 1994 */       if (arrayOfObject != null) {
/* 1995 */         localObject = new ActionMapUIResource();
/* 1996 */         for (int i = arrayOfObject.length - 1; i >= 0; i--) {
/* 1997 */           ((ActionMap)localObject).put(arrayOfObject[i], createAudioAction(arrayOfObject[i]));
/*      */         }
/*      */       }
/*      */ 
/* 2001 */       UIManager.getLookAndFeelDefaults().put("AuditoryCues.actionMap", localObject);
/*      */     }
/*      */ 
/* 2004 */     return localObject;
/*      */   }
/*      */ 
/*      */   protected Action createAudioAction(Object paramObject)
/*      */   {
/* 2024 */     if (paramObject != null) {
/* 2025 */       String str1 = (String)paramObject;
/* 2026 */       String str2 = (String)UIManager.get(paramObject);
/* 2027 */       return new AudioAction(str1, str2);
/*      */     }
/* 2029 */     return null;
/*      */   }
/*      */ 
/*      */   private byte[] loadAudioData(final String paramString)
/*      */   {
/* 2125 */     if (paramString == null) {
/* 2126 */       return null;
/*      */     }
/*      */ 
/* 2135 */     byte[] arrayOfByte = (byte[])AccessController.doPrivileged(new PrivilegedAction()
/*      */     {
/*      */       public byte[] run() {
/*      */         try {
/* 2139 */           InputStream localInputStream = BasicLookAndFeel.this.getClass().getResourceAsStream(paramString);
/*      */ 
/* 2141 */           if (localInputStream == null) {
/* 2142 */             return null;
/*      */           }
/* 2144 */           BufferedInputStream localBufferedInputStream = new BufferedInputStream(localInputStream);
/*      */ 
/* 2146 */           ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream(1024);
/*      */ 
/* 2148 */           byte[] arrayOfByte = new byte[1024];
/*      */           int i;
/* 2150 */           while ((i = localBufferedInputStream.read(arrayOfByte)) > 0) {
/* 2151 */             localByteArrayOutputStream.write(arrayOfByte, 0, i);
/*      */           }
/* 2153 */           localBufferedInputStream.close();
/* 2154 */           localByteArrayOutputStream.flush();
/* 2155 */           return localByteArrayOutputStream.toByteArray();
/*      */         }
/*      */         catch (IOException localIOException) {
/* 2158 */           System.err.println(localIOException.toString());
/* 2159 */         }return null;
/*      */       }
/*      */     });
/* 2163 */     if (arrayOfByte == null) {
/* 2164 */       System.err.println(getClass().getName() + "/" + paramString + " not found.");
/*      */ 
/* 2166 */       return null;
/*      */     }
/* 2168 */     if (arrayOfByte.length == 0) {
/* 2169 */       System.err.println("warning: " + paramString + " is zero-length");
/*      */ 
/* 2171 */       return null;
/*      */     }
/* 2173 */     return arrayOfByte;
/*      */   }
/*      */ 
/*      */   protected void playSound(Action paramAction)
/*      */   {
/* 2194 */     if (paramAction != null) {
/* 2195 */       Object[] arrayOfObject = (Object[])UIManager.get("AuditoryCues.playList");
/*      */ 
/* 2197 */       if (arrayOfObject != null)
/*      */       {
/* 2199 */         HashSet localHashSet = new HashSet();
/* 2200 */         for (Object localObject2 : arrayOfObject) {
/* 2201 */           localHashSet.add(localObject2);
/*      */         }
/*      */ 
/* 2204 */         ??? = (String)paramAction.getValue("Name");
/*      */ 
/* 2206 */         if (localHashSet.contains(???))
/* 2207 */           paramAction.actionPerformed(new ActionEvent(this, 1001, (String)???));
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   static void installAudioActionMap(ActionMap paramActionMap)
/*      */   {
/* 2221 */     LookAndFeel localLookAndFeel = UIManager.getLookAndFeel();
/* 2222 */     if ((localLookAndFeel instanceof BasicLookAndFeel))
/* 2223 */       paramActionMap.setParent(((BasicLookAndFeel)localLookAndFeel).getAudioActionMap());
/*      */   }
/*      */ 
/*      */   static void playSound(JComponent paramJComponent, Object paramObject)
/*      */   {
/* 2235 */     LookAndFeel localLookAndFeel = UIManager.getLookAndFeel();
/* 2236 */     if ((localLookAndFeel instanceof BasicLookAndFeel)) {
/* 2237 */       ActionMap localActionMap = paramJComponent.getActionMap();
/* 2238 */       if (localActionMap != null) {
/* 2239 */         Action localAction = localActionMap.get(paramObject);
/* 2240 */         if (localAction != null)
/*      */         {
/* 2242 */           ((BasicLookAndFeel)localLookAndFeel).playSound(localAction);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   class AWTEventHelper
/*      */     implements AWTEventListener, PrivilegedAction<Object>
/*      */   {
/*      */     AWTEventHelper()
/*      */     {
/* 2255 */       AccessController.doPrivileged(this);
/*      */     }
/*      */ 
/*      */     public Object run() {
/* 2259 */       Toolkit localToolkit = Toolkit.getDefaultToolkit();
/* 2260 */       if (BasicLookAndFeel.this.invocator == null)
/* 2261 */         localToolkit.addAWTEventListener(this, 16L);
/*      */       else {
/* 2263 */         localToolkit.removeAWTEventListener(BasicLookAndFeel.this.invocator);
/*      */       }
/*      */ 
/* 2266 */       return null;
/*      */     }
/*      */ 
/*      */     public void eventDispatched(AWTEvent paramAWTEvent) {
/* 2270 */       int i = paramAWTEvent.getID();
/*      */       Object localObject1;
/*      */       Object localObject2;
/*      */       Object localObject3;
/* 2271 */       if ((i & 0x10) != 0L) {
/* 2272 */         localObject1 = (MouseEvent)paramAWTEvent;
/* 2273 */         if (((MouseEvent)localObject1).isPopupTrigger()) {
/* 2274 */           localObject2 = MenuSelectionManager.defaultManager().getSelectedPath();
/*      */ 
/* 2277 */           if ((localObject2 != null) && (localObject2.length != 0)) {
/* 2278 */             return;
/*      */           }
/*      */ 
/* 2281 */           localObject3 = ((MouseEvent)localObject1).getSource();
/* 2282 */           JComponent localJComponent = null;
/* 2283 */           if ((localObject3 instanceof JComponent))
/* 2284 */             localJComponent = (JComponent)localObject3;
/* 2285 */           else if ((localObject3 instanceof BasicSplitPaneDivider))
/*      */           {
/* 2288 */             localJComponent = (JComponent)((BasicSplitPaneDivider)localObject3).getParent();
/*      */           }
/*      */ 
/* 2291 */           if ((localJComponent != null) && 
/* 2292 */             (localJComponent.getComponentPopupMenu() != null)) {
/* 2293 */             Point localPoint = localJComponent.getPopupLocation((MouseEvent)localObject1);
/* 2294 */             if (localPoint == null) {
/* 2295 */               localPoint = ((MouseEvent)localObject1).getPoint();
/* 2296 */               localPoint = SwingUtilities.convertPoint((Component)localObject3, localPoint, localJComponent);
/*      */             }
/*      */ 
/* 2299 */             localJComponent.getComponentPopupMenu().show(localJComponent, localPoint.x, localPoint.y);
/* 2300 */             ((MouseEvent)localObject1).consume();
/*      */           }
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 2306 */       if (i == 501) {
/* 2307 */         localObject1 = paramAWTEvent.getSource();
/* 2308 */         if (!(localObject1 instanceof Component)) {
/* 2309 */           return;
/*      */         }
/* 2311 */         localObject2 = (Component)localObject1;
/* 2312 */         if (localObject2 != null) {
/* 2313 */           localObject3 = localObject2;
/* 2314 */           while ((localObject3 != null) && (!(localObject3 instanceof Window))) {
/* 2315 */             if ((localObject3 instanceof JInternalFrame))
/*      */               try {
/* 2317 */                 ((JInternalFrame)localObject3).setSelected(true);
/*      */               } catch (PropertyVetoException localPropertyVetoException) {
/*      */               }
/* 2320 */             localObject3 = ((Component)localObject3).getParent();
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private class AudioAction extends AbstractAction
/*      */     implements LineListener
/*      */   {
/*      */     private String audioResource;
/*      */     private byte[] audioBuffer;
/*      */ 
/*      */     public AudioAction(String paramString1, String arg3)
/*      */     {
/* 2056 */       super();
/*      */       Object localObject;
/* 2057 */       this.audioResource = localObject;
/*      */     }
/*      */ 
/*      */     public void actionPerformed(ActionEvent paramActionEvent) {
/* 2061 */       if (this.audioBuffer == null) {
/* 2062 */         this.audioBuffer = BasicLookAndFeel.this.loadAudioData(this.audioResource);
/*      */       }
/* 2064 */       if (this.audioBuffer != null) {
/* 2065 */         cancelCurrentSound(null);
/*      */         try {
/* 2067 */           AudioInputStream localAudioInputStream = AudioSystem.getAudioInputStream(new ByteArrayInputStream(this.audioBuffer));
/*      */ 
/* 2070 */           DataLine.Info localInfo = new DataLine.Info(Clip.class, localAudioInputStream.getFormat());
/*      */ 
/* 2072 */           Clip localClip = (Clip)AudioSystem.getLine(localInfo);
/* 2073 */           localClip.open(localAudioInputStream);
/* 2074 */           localClip.addLineListener(this);
/*      */ 
/* 2076 */           synchronized (BasicLookAndFeel.this.audioLock) {
/* 2077 */             BasicLookAndFeel.this.clipPlaying = localClip;
/*      */           }
/*      */ 
/* 2080 */           localClip.start(); } catch (Exception localException) {
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     public void update(LineEvent paramLineEvent) {
/* 2086 */       if (paramLineEvent.getType() == LineEvent.Type.STOP)
/* 2087 */         cancelCurrentSound((Clip)paramLineEvent.getLine());
/*      */     }
/*      */ 
/*      */     private void cancelCurrentSound(Clip paramClip)
/*      */     {
/* 2096 */       Clip localClip = null;
/*      */ 
/* 2098 */       synchronized (BasicLookAndFeel.this.audioLock) {
/* 2099 */         if ((paramClip == null) || (paramClip == BasicLookAndFeel.this.clipPlaying)) {
/* 2100 */           localClip = BasicLookAndFeel.this.clipPlaying;
/* 2101 */           BasicLookAndFeel.this.clipPlaying = null;
/*      */         }
/*      */       }
/*      */ 
/* 2105 */       if (localClip != null) {
/* 2106 */         localClip.removeLineListener(this);
/* 2107 */         localClip.close();
/*      */       }
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.basic.BasicLookAndFeel
 * JD-Core Version:    0.6.2
 */