/*      */ package javax.swing.plaf.nimbus;
/*      */ 
/*      */ import java.awt.Color;
/*      */ import java.awt.Component;
/*      */ import java.awt.Dimension;
/*      */ import java.awt.Font;
/*      */ import java.awt.Graphics;
/*      */ import java.awt.Graphics2D;
/*      */ import java.awt.Insets;
/*      */ import java.awt.image.BufferedImage;
/*      */ import java.beans.PropertyChangeEvent;
/*      */ import java.beans.PropertyChangeListener;
/*      */ import java.lang.reflect.Constructor;
/*      */ import java.util.ArrayList;
/*      */ import java.util.HashMap;
/*      */ import java.util.LinkedList;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.WeakHashMap;
/*      */ import javax.swing.BorderFactory;
/*      */ import javax.swing.JComponent;
/*      */ import javax.swing.JInternalFrame.JDesktopIcon;
/*      */ import javax.swing.Painter;
/*      */ import javax.swing.UIDefaults;
/*      */ import javax.swing.UIDefaults.ActiveValue;
/*      */ import javax.swing.UIDefaults.LazyValue;
/*      */ import javax.swing.UIManager;
/*      */ import javax.swing.border.Border;
/*      */ import javax.swing.plaf.BorderUIResource;
/*      */ import javax.swing.plaf.ColorUIResource;
/*      */ import javax.swing.plaf.DimensionUIResource;
/*      */ import javax.swing.plaf.FontUIResource;
/*      */ import javax.swing.plaf.InsetsUIResource;
/*      */ import javax.swing.plaf.UIResource;
/*      */ import javax.swing.plaf.synth.Region;
/*      */ import javax.swing.plaf.synth.SynthStyle;
/*      */ import sun.font.FontUtilities;
/*      */ import sun.swing.plaf.synth.DefaultSynthStyle;
/*      */ 
/*      */ final class NimbusDefaults
/*      */ {
/*      */   private Map<Region, List<LazyStyle>> m;
/*   91 */   private Map<String, Region> registeredRegions = new HashMap();
/*      */ 
/*   94 */   private Map<JComponent, Map<Region, SynthStyle>> overridesCache = new WeakHashMap();
/*      */   private DefaultSynthStyle defaultStyle;
/*      */   private FontUIResource defaultFont;
/*  108 */   private ColorTree colorTree = new ColorTree(null);
/*      */ 
/*  111 */   private DefaultsListener defaultsListener = new DefaultsListener(null);
/*      */ 
/* 1641 */   private Map<DerivedColor, DerivedColor> derivedColors = new HashMap();
/*      */ 
/*      */   void initialize()
/*      */   {
/*  116 */     UIManager.addPropertyChangeListener(this.defaultsListener);
/*  117 */     UIManager.getDefaults().addPropertyChangeListener(this.colorTree);
/*      */   }
/*      */ 
/*      */   void uninitialize()
/*      */   {
/*  123 */     UIManager.removePropertyChangeListener(this.defaultsListener);
/*  124 */     UIManager.getDefaults().removePropertyChangeListener(this.colorTree);
/*      */   }
/*      */ 
/*      */   NimbusDefaults()
/*      */   {
/*  132 */     this.m = new HashMap();
/*      */ 
/*  138 */     this.defaultFont = FontUtilities.getFontConfigFUIR("sans", 0, 12);
/*  139 */     this.defaultStyle = new DefaultSynthStyle();
/*  140 */     this.defaultStyle.setFont(this.defaultFont);
/*      */ 
/*  143 */     register(Region.ARROW_BUTTON, "ArrowButton");
/*  144 */     register(Region.BUTTON, "Button");
/*  145 */     register(Region.TOGGLE_BUTTON, "ToggleButton");
/*  146 */     register(Region.RADIO_BUTTON, "RadioButton");
/*  147 */     register(Region.CHECK_BOX, "CheckBox");
/*  148 */     register(Region.COLOR_CHOOSER, "ColorChooser");
/*  149 */     register(Region.PANEL, "ColorChooser:\"ColorChooser.previewPanelHolder\"");
/*  150 */     register(Region.LABEL, "ColorChooser:\"ColorChooser.previewPanelHolder\":\"OptionPane.label\"");
/*  151 */     register(Region.COMBO_BOX, "ComboBox");
/*  152 */     register(Region.TEXT_FIELD, "ComboBox:\"ComboBox.textField\"");
/*  153 */     register(Region.ARROW_BUTTON, "ComboBox:\"ComboBox.arrowButton\"");
/*  154 */     register(Region.LABEL, "ComboBox:\"ComboBox.listRenderer\"");
/*  155 */     register(Region.LABEL, "ComboBox:\"ComboBox.renderer\"");
/*  156 */     register(Region.SCROLL_PANE, "\"ComboBox.scrollPane\"");
/*  157 */     register(Region.FILE_CHOOSER, "FileChooser");
/*  158 */     register(Region.INTERNAL_FRAME_TITLE_PANE, "InternalFrameTitlePane");
/*  159 */     register(Region.INTERNAL_FRAME, "InternalFrame");
/*  160 */     register(Region.INTERNAL_FRAME_TITLE_PANE, "InternalFrame:InternalFrameTitlePane");
/*  161 */     register(Region.BUTTON, "InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.menuButton\"");
/*  162 */     register(Region.BUTTON, "InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.iconifyButton\"");
/*  163 */     register(Region.BUTTON, "InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.maximizeButton\"");
/*  164 */     register(Region.BUTTON, "InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.closeButton\"");
/*  165 */     register(Region.DESKTOP_ICON, "DesktopIcon");
/*  166 */     register(Region.DESKTOP_PANE, "DesktopPane");
/*  167 */     register(Region.LABEL, "Label");
/*  168 */     register(Region.LIST, "List");
/*  169 */     register(Region.LABEL, "List:\"List.cellRenderer\"");
/*  170 */     register(Region.MENU_BAR, "MenuBar");
/*  171 */     register(Region.MENU, "MenuBar:Menu");
/*  172 */     register(Region.MENU_ITEM_ACCELERATOR, "MenuBar:Menu:MenuItemAccelerator");
/*  173 */     register(Region.MENU_ITEM, "MenuItem");
/*  174 */     register(Region.MENU_ITEM_ACCELERATOR, "MenuItem:MenuItemAccelerator");
/*  175 */     register(Region.RADIO_BUTTON_MENU_ITEM, "RadioButtonMenuItem");
/*  176 */     register(Region.MENU_ITEM_ACCELERATOR, "RadioButtonMenuItem:MenuItemAccelerator");
/*  177 */     register(Region.CHECK_BOX_MENU_ITEM, "CheckBoxMenuItem");
/*  178 */     register(Region.MENU_ITEM_ACCELERATOR, "CheckBoxMenuItem:MenuItemAccelerator");
/*  179 */     register(Region.MENU, "Menu");
/*  180 */     register(Region.MENU_ITEM_ACCELERATOR, "Menu:MenuItemAccelerator");
/*  181 */     register(Region.POPUP_MENU, "PopupMenu");
/*  182 */     register(Region.POPUP_MENU_SEPARATOR, "PopupMenuSeparator");
/*  183 */     register(Region.OPTION_PANE, "OptionPane");
/*  184 */     register(Region.SEPARATOR, "OptionPane:\"OptionPane.separator\"");
/*  185 */     register(Region.PANEL, "OptionPane:\"OptionPane.messageArea\"");
/*  186 */     register(Region.LABEL, "OptionPane:\"OptionPane.messageArea\":\"OptionPane.label\"");
/*  187 */     register(Region.PANEL, "Panel");
/*  188 */     register(Region.PROGRESS_BAR, "ProgressBar");
/*  189 */     register(Region.SEPARATOR, "Separator");
/*  190 */     register(Region.SCROLL_BAR, "ScrollBar");
/*  191 */     register(Region.ARROW_BUTTON, "ScrollBar:\"ScrollBar.button\"");
/*  192 */     register(Region.SCROLL_BAR_THUMB, "ScrollBar:ScrollBarThumb");
/*  193 */     register(Region.SCROLL_BAR_TRACK, "ScrollBar:ScrollBarTrack");
/*  194 */     register(Region.SCROLL_PANE, "ScrollPane");
/*  195 */     register(Region.VIEWPORT, "Viewport");
/*  196 */     register(Region.SLIDER, "Slider");
/*  197 */     register(Region.SLIDER_THUMB, "Slider:SliderThumb");
/*  198 */     register(Region.SLIDER_TRACK, "Slider:SliderTrack");
/*  199 */     register(Region.SPINNER, "Spinner");
/*  200 */     register(Region.PANEL, "Spinner:\"Spinner.editor\"");
/*  201 */     register(Region.FORMATTED_TEXT_FIELD, "Spinner:Panel:\"Spinner.formattedTextField\"");
/*  202 */     register(Region.ARROW_BUTTON, "Spinner:\"Spinner.previousButton\"");
/*  203 */     register(Region.ARROW_BUTTON, "Spinner:\"Spinner.nextButton\"");
/*  204 */     register(Region.SPLIT_PANE, "SplitPane");
/*  205 */     register(Region.SPLIT_PANE_DIVIDER, "SplitPane:SplitPaneDivider");
/*  206 */     register(Region.TABBED_PANE, "TabbedPane");
/*  207 */     register(Region.TABBED_PANE_TAB, "TabbedPane:TabbedPaneTab");
/*  208 */     register(Region.TABBED_PANE_TAB_AREA, "TabbedPane:TabbedPaneTabArea");
/*  209 */     register(Region.TABBED_PANE_CONTENT, "TabbedPane:TabbedPaneContent");
/*  210 */     register(Region.TABLE, "Table");
/*  211 */     register(Region.LABEL, "Table:\"Table.cellRenderer\"");
/*  212 */     register(Region.TABLE_HEADER, "TableHeader");
/*  213 */     register(Region.LABEL, "TableHeader:\"TableHeader.renderer\"");
/*  214 */     register(Region.TEXT_FIELD, "\"Table.editor\"");
/*  215 */     register(Region.TEXT_FIELD, "\"Tree.cellEditor\"");
/*  216 */     register(Region.TEXT_FIELD, "TextField");
/*  217 */     register(Region.FORMATTED_TEXT_FIELD, "FormattedTextField");
/*  218 */     register(Region.PASSWORD_FIELD, "PasswordField");
/*  219 */     register(Region.TEXT_AREA, "TextArea");
/*  220 */     register(Region.TEXT_PANE, "TextPane");
/*  221 */     register(Region.EDITOR_PANE, "EditorPane");
/*  222 */     register(Region.TOOL_BAR, "ToolBar");
/*  223 */     register(Region.BUTTON, "ToolBar:Button");
/*  224 */     register(Region.TOGGLE_BUTTON, "ToolBar:ToggleButton");
/*  225 */     register(Region.TOOL_BAR_SEPARATOR, "ToolBarSeparator");
/*  226 */     register(Region.TOOL_TIP, "ToolTip");
/*  227 */     register(Region.TREE, "Tree");
/*  228 */     register(Region.TREE_CELL, "Tree:TreeCell");
/*  229 */     register(Region.LABEL, "Tree:\"Tree.cellRenderer\"");
/*  230 */     register(Region.ROOT_PANE, "RootPane");
/*      */   }
/*      */ 
/*      */   void initializeDefaults(UIDefaults paramUIDefaults)
/*      */   {
/*  246 */     addColor(paramUIDefaults, "text", 0, 0, 0, 255);
/*  247 */     addColor(paramUIDefaults, "control", 214, 217, 223, 255);
/*  248 */     addColor(paramUIDefaults, "nimbusBase", 51, 98, 140, 255);
/*  249 */     addColor(paramUIDefaults, "nimbusBlueGrey", "nimbusBase", 0.03245944F, -0.525188F, 0.1960784F, 0);
/*  250 */     addColor(paramUIDefaults, "nimbusOrange", 191, 98, 4, 255);
/*  251 */     addColor(paramUIDefaults, "nimbusGreen", 176, 179, 50, 255);
/*  252 */     addColor(paramUIDefaults, "nimbusRed", 169, 46, 34, 255);
/*  253 */     addColor(paramUIDefaults, "nimbusBorder", "nimbusBlueGrey", 0.0F, -0.01735862F, -0.1137255F, 0);
/*  254 */     addColor(paramUIDefaults, "nimbusSelection", "nimbusBase", -0.01075047F, -0.04875779F, -0.007843137F, 0);
/*  255 */     addColor(paramUIDefaults, "nimbusInfoBlue", 47, 92, 180, 255);
/*  256 */     addColor(paramUIDefaults, "nimbusAlertYellow", 255, 220, 35, 255);
/*  257 */     addColor(paramUIDefaults, "nimbusFocus", 115, 164, 209, 255);
/*  258 */     addColor(paramUIDefaults, "nimbusSelectedText", 255, 255, 255, 255);
/*  259 */     addColor(paramUIDefaults, "nimbusSelectionBackground", 57, 105, 138, 255);
/*  260 */     addColor(paramUIDefaults, "nimbusDisabledText", 142, 143, 145, 255);
/*  261 */     addColor(paramUIDefaults, "nimbusLightBackground", 255, 255, 255, 255);
/*  262 */     addColor(paramUIDefaults, "infoText", "text", 0.0F, 0.0F, 0.0F, 0);
/*  263 */     addColor(paramUIDefaults, "info", 242, 242, 189, 255);
/*  264 */     addColor(paramUIDefaults, "menuText", "text", 0.0F, 0.0F, 0.0F, 0);
/*  265 */     addColor(paramUIDefaults, "menu", "nimbusBase", 0.0213483F, -0.6150531F, 0.4F, 0);
/*  266 */     addColor(paramUIDefaults, "scrollbar", "nimbusBlueGrey", -0.00694442F, -0.07296763F, 0.09019607F, 0);
/*  267 */     addColor(paramUIDefaults, "controlText", "text", 0.0F, 0.0F, 0.0F, 0);
/*  268 */     addColor(paramUIDefaults, "controlHighlight", "nimbusBlueGrey", 0.0F, -0.07333623F, 0.2039216F, 0);
/*  269 */     addColor(paramUIDefaults, "controlLHighlight", "nimbusBlueGrey", 0.0F, -0.09852631F, 0.235294F, 0);
/*  270 */     addColor(paramUIDefaults, "controlShadow", "nimbusBlueGrey", -0.002777755F, -0.0212406F, 0.1333333F, 0);
/*  271 */     addColor(paramUIDefaults, "controlDkShadow", "nimbusBlueGrey", -0.002777755F, -0.001830667F, -0.0235294F, 0);
/*  272 */     addColor(paramUIDefaults, "textHighlight", "nimbusSelectionBackground", 0.0F, 0.0F, 0.0F, 0);
/*  273 */     addColor(paramUIDefaults, "textHighlightText", "nimbusSelectedText", 0.0F, 0.0F, 0.0F, 0);
/*  274 */     addColor(paramUIDefaults, "textInactiveText", "nimbusDisabledText", 0.0F, 0.0F, 0.0F, 0);
/*  275 */     addColor(paramUIDefaults, "desktop", "nimbusBase", -0.009207249F, -0.1398465F, -0.07450983F, 0);
/*  276 */     addColor(paramUIDefaults, "activeCaption", "nimbusBlueGrey", 0.0F, -0.04992025F, 0.03137255F, 0);
/*  277 */     addColor(paramUIDefaults, "inactiveCaption", "nimbusBlueGrey", -0.0050505F, -0.05552632F, 0.03921568F, 0);
/*      */ 
/*  280 */     paramUIDefaults.put("defaultFont", new FontUIResource(this.defaultFont));
/*  281 */     paramUIDefaults.put("InternalFrame.titleFont", new DerivedFont("defaultFont", 1.0F, Boolean.valueOf(true), null));
/*      */ 
/*  286 */     addColor(paramUIDefaults, "textForeground", "text", 0.0F, 0.0F, 0.0F, 0);
/*  287 */     addColor(paramUIDefaults, "textBackground", "nimbusSelectionBackground", 0.0F, 0.0F, 0.0F, 0);
/*  288 */     addColor(paramUIDefaults, "background", "control", 0.0F, 0.0F, 0.0F, 0);
/*  289 */     paramUIDefaults.put("TitledBorder.position", "ABOVE_TOP");
/*  290 */     paramUIDefaults.put("FileView.fullRowSelection", Boolean.TRUE);
/*      */ 
/*  293 */     paramUIDefaults.put("ArrowButton.contentMargins", new InsetsUIResource(0, 0, 0, 0));
/*  294 */     paramUIDefaults.put("ArrowButton.size", new Integer(16));
/*  295 */     paramUIDefaults.put("ArrowButton[Disabled].foregroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ArrowButtonPainter", 2, new Insets(0, 0, 0, 0), new Dimension(10, 10), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0D, 1.0D));
/*  296 */     paramUIDefaults.put("ArrowButton[Enabled].foregroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ArrowButtonPainter", 3, new Insets(0, 0, 0, 0), new Dimension(10, 10), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0D, 1.0D));
/*      */ 
/*  299 */     paramUIDefaults.put("Button.contentMargins", new InsetsUIResource(6, 14, 6, 14));
/*  300 */     paramUIDefaults.put("Button.defaultButtonFollowsFocus", Boolean.FALSE);
/*  301 */     paramUIDefaults.put("Button[Default].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ButtonPainter", 1, new Insets(7, 7, 7, 7), new Dimension(104, 33), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, (1.0D / 0.0D), 2.0D));
/*  302 */     paramUIDefaults.put("Button[Default+Focused].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ButtonPainter", 2, new Insets(7, 7, 7, 7), new Dimension(104, 33), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, (1.0D / 0.0D), 2.0D));
/*  303 */     paramUIDefaults.put("Button[Default+MouseOver].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ButtonPainter", 3, new Insets(7, 7, 7, 7), new Dimension(104, 33), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, (1.0D / 0.0D), 2.0D));
/*  304 */     paramUIDefaults.put("Button[Default+Focused+MouseOver].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ButtonPainter", 4, new Insets(7, 7, 7, 7), new Dimension(104, 33), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, (1.0D / 0.0D), 2.0D));
/*  305 */     addColor(paramUIDefaults, "Button[Default+Pressed].textForeground", "nimbusSelectedText", 0.0F, 0.0F, 0.0F, 0);
/*  306 */     paramUIDefaults.put("Button[Default+Pressed].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ButtonPainter", 5, new Insets(7, 7, 7, 7), new Dimension(104, 33), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, (1.0D / 0.0D), 2.0D));
/*  307 */     paramUIDefaults.put("Button[Default+Focused+Pressed].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ButtonPainter", 6, new Insets(7, 7, 7, 7), new Dimension(104, 33), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, (1.0D / 0.0D), 2.0D));
/*  308 */     addColor(paramUIDefaults, "Button[Disabled].textForeground", "nimbusDisabledText", 0.0F, 0.0F, 0.0F, 0);
/*  309 */     paramUIDefaults.put("Button[Disabled].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ButtonPainter", 7, new Insets(7, 7, 7, 7), new Dimension(104, 33), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, (1.0D / 0.0D), 2.0D));
/*  310 */     paramUIDefaults.put("Button[Enabled].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ButtonPainter", 8, new Insets(7, 7, 7, 7), new Dimension(104, 33), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, (1.0D / 0.0D), 2.0D));
/*  311 */     paramUIDefaults.put("Button[Focused].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ButtonPainter", 9, new Insets(7, 7, 7, 7), new Dimension(104, 33), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, (1.0D / 0.0D), 2.0D));
/*  312 */     paramUIDefaults.put("Button[MouseOver].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ButtonPainter", 10, new Insets(7, 7, 7, 7), new Dimension(104, 33), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, (1.0D / 0.0D), 2.0D));
/*  313 */     paramUIDefaults.put("Button[Focused+MouseOver].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ButtonPainter", 11, new Insets(7, 7, 7, 7), new Dimension(104, 33), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, (1.0D / 0.0D), 2.0D));
/*  314 */     paramUIDefaults.put("Button[Pressed].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ButtonPainter", 12, new Insets(7, 7, 7, 7), new Dimension(104, 33), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, (1.0D / 0.0D), 2.0D));
/*  315 */     paramUIDefaults.put("Button[Focused+Pressed].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ButtonPainter", 13, new Insets(7, 7, 7, 7), new Dimension(104, 33), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, (1.0D / 0.0D), 2.0D));
/*      */ 
/*  318 */     paramUIDefaults.put("ToggleButton.contentMargins", new InsetsUIResource(6, 14, 6, 14));
/*  319 */     addColor(paramUIDefaults, "ToggleButton[Disabled].textForeground", "nimbusDisabledText", 0.0F, 0.0F, 0.0F, 0);
/*  320 */     paramUIDefaults.put("ToggleButton[Disabled].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ToggleButtonPainter", 1, new Insets(7, 7, 7, 7), new Dimension(104, 33), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, (1.0D / 0.0D), 2.0D));
/*  321 */     paramUIDefaults.put("ToggleButton[Enabled].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ToggleButtonPainter", 2, new Insets(7, 7, 7, 7), new Dimension(104, 33), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, (1.0D / 0.0D), 2.0D));
/*  322 */     paramUIDefaults.put("ToggleButton[Focused].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ToggleButtonPainter", 3, new Insets(7, 7, 7, 7), new Dimension(104, 33), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, (1.0D / 0.0D), 2.0D));
/*  323 */     paramUIDefaults.put("ToggleButton[MouseOver].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ToggleButtonPainter", 4, new Insets(7, 7, 7, 7), new Dimension(104, 33), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, (1.0D / 0.0D), 2.0D));
/*  324 */     paramUIDefaults.put("ToggleButton[Focused+MouseOver].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ToggleButtonPainter", 5, new Insets(7, 7, 7, 7), new Dimension(104, 33), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, (1.0D / 0.0D), 2.0D));
/*  325 */     paramUIDefaults.put("ToggleButton[Pressed].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ToggleButtonPainter", 6, new Insets(7, 7, 7, 7), new Dimension(104, 33), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, (1.0D / 0.0D), 2.0D));
/*  326 */     paramUIDefaults.put("ToggleButton[Focused+Pressed].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ToggleButtonPainter", 7, new Insets(7, 7, 7, 7), new Dimension(104, 33), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, (1.0D / 0.0D), 2.0D));
/*  327 */     paramUIDefaults.put("ToggleButton[Selected].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ToggleButtonPainter", 8, new Insets(7, 7, 7, 7), new Dimension(72, 24), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, (1.0D / 0.0D), 2.0D));
/*  328 */     paramUIDefaults.put("ToggleButton[Focused+Selected].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ToggleButtonPainter", 9, new Insets(7, 7, 7, 7), new Dimension(72, 24), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, (1.0D / 0.0D), 2.0D));
/*  329 */     paramUIDefaults.put("ToggleButton[Pressed+Selected].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ToggleButtonPainter", 10, new Insets(7, 7, 7, 7), new Dimension(72, 24), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, (1.0D / 0.0D), 2.0D));
/*  330 */     paramUIDefaults.put("ToggleButton[Focused+Pressed+Selected].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ToggleButtonPainter", 11, new Insets(7, 7, 7, 7), new Dimension(72, 24), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, (1.0D / 0.0D), 2.0D));
/*  331 */     paramUIDefaults.put("ToggleButton[MouseOver+Selected].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ToggleButtonPainter", 12, new Insets(7, 7, 7, 7), new Dimension(72, 24), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, (1.0D / 0.0D), 2.0D));
/*  332 */     paramUIDefaults.put("ToggleButton[Focused+MouseOver+Selected].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ToggleButtonPainter", 13, new Insets(7, 7, 7, 7), new Dimension(72, 24), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, (1.0D / 0.0D), 2.0D));
/*  333 */     addColor(paramUIDefaults, "ToggleButton[Disabled+Selected].textForeground", "nimbusDisabledText", 0.0F, 0.0F, 0.0F, 0);
/*  334 */     paramUIDefaults.put("ToggleButton[Disabled+Selected].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ToggleButtonPainter", 14, new Insets(7, 7, 7, 7), new Dimension(72, 24), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, (1.0D / 0.0D), 2.0D));
/*      */ 
/*  337 */     paramUIDefaults.put("RadioButton.contentMargins", new InsetsUIResource(0, 0, 0, 0));
/*  338 */     addColor(paramUIDefaults, "RadioButton[Disabled].textForeground", "nimbusDisabledText", 0.0F, 0.0F, 0.0F, 0);
/*  339 */     paramUIDefaults.put("RadioButton[Disabled].iconPainter", new LazyPainter("javax.swing.plaf.nimbus.RadioButtonPainter", 3, new Insets(5, 5, 5, 5), new Dimension(18, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0D, 1.0D));
/*  340 */     paramUIDefaults.put("RadioButton[Enabled].iconPainter", new LazyPainter("javax.swing.plaf.nimbus.RadioButtonPainter", 4, new Insets(5, 5, 5, 5), new Dimension(18, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0D, 1.0D));
/*  341 */     paramUIDefaults.put("RadioButton[Focused].iconPainter", new LazyPainter("javax.swing.plaf.nimbus.RadioButtonPainter", 5, new Insets(5, 5, 5, 5), new Dimension(18, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0D, 1.0D));
/*  342 */     paramUIDefaults.put("RadioButton[MouseOver].iconPainter", new LazyPainter("javax.swing.plaf.nimbus.RadioButtonPainter", 6, new Insets(5, 5, 5, 5), new Dimension(18, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0D, 1.0D));
/*  343 */     paramUIDefaults.put("RadioButton[Focused+MouseOver].iconPainter", new LazyPainter("javax.swing.plaf.nimbus.RadioButtonPainter", 7, new Insets(5, 5, 5, 5), new Dimension(18, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0D, 1.0D));
/*  344 */     paramUIDefaults.put("RadioButton[Pressed].iconPainter", new LazyPainter("javax.swing.plaf.nimbus.RadioButtonPainter", 8, new Insets(5, 5, 5, 5), new Dimension(18, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0D, 1.0D));
/*  345 */     paramUIDefaults.put("RadioButton[Focused+Pressed].iconPainter", new LazyPainter("javax.swing.plaf.nimbus.RadioButtonPainter", 9, new Insets(5, 5, 5, 5), new Dimension(18, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0D, 1.0D));
/*  346 */     paramUIDefaults.put("RadioButton[Selected].iconPainter", new LazyPainter("javax.swing.plaf.nimbus.RadioButtonPainter", 10, new Insets(5, 5, 5, 5), new Dimension(18, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0D, 1.0D));
/*  347 */     paramUIDefaults.put("RadioButton[Focused+Selected].iconPainter", new LazyPainter("javax.swing.plaf.nimbus.RadioButtonPainter", 11, new Insets(5, 5, 5, 5), new Dimension(18, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0D, 1.0D));
/*  348 */     paramUIDefaults.put("RadioButton[Pressed+Selected].iconPainter", new LazyPainter("javax.swing.plaf.nimbus.RadioButtonPainter", 12, new Insets(5, 5, 5, 5), new Dimension(18, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0D, 1.0D));
/*  349 */     paramUIDefaults.put("RadioButton[Focused+Pressed+Selected].iconPainter", new LazyPainter("javax.swing.plaf.nimbus.RadioButtonPainter", 13, new Insets(5, 5, 5, 5), new Dimension(18, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0D, 1.0D));
/*  350 */     paramUIDefaults.put("RadioButton[MouseOver+Selected].iconPainter", new LazyPainter("javax.swing.plaf.nimbus.RadioButtonPainter", 14, new Insets(5, 5, 5, 5), new Dimension(18, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0D, 1.0D));
/*  351 */     paramUIDefaults.put("RadioButton[Focused+MouseOver+Selected].iconPainter", new LazyPainter("javax.swing.plaf.nimbus.RadioButtonPainter", 15, new Insets(5, 5, 5, 5), new Dimension(18, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0D, 1.0D));
/*  352 */     paramUIDefaults.put("RadioButton[Disabled+Selected].iconPainter", new LazyPainter("javax.swing.plaf.nimbus.RadioButtonPainter", 16, new Insets(5, 5, 5, 5), new Dimension(18, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0D, 1.0D));
/*  353 */     paramUIDefaults.put("RadioButton.icon", new NimbusIcon("RadioButton", "iconPainter", 18, 18));
/*      */ 
/*  356 */     paramUIDefaults.put("CheckBox.contentMargins", new InsetsUIResource(0, 0, 0, 0));
/*  357 */     addColor(paramUIDefaults, "CheckBox[Disabled].textForeground", "nimbusDisabledText", 0.0F, 0.0F, 0.0F, 0);
/*  358 */     paramUIDefaults.put("CheckBox[Disabled].iconPainter", new LazyPainter("javax.swing.plaf.nimbus.CheckBoxPainter", 3, new Insets(5, 5, 5, 5), new Dimension(18, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0D, 1.0D));
/*  359 */     paramUIDefaults.put("CheckBox[Enabled].iconPainter", new LazyPainter("javax.swing.plaf.nimbus.CheckBoxPainter", 4, new Insets(5, 5, 5, 5), new Dimension(18, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0D, 1.0D));
/*  360 */     paramUIDefaults.put("CheckBox[Focused].iconPainter", new LazyPainter("javax.swing.plaf.nimbus.CheckBoxPainter", 5, new Insets(5, 5, 5, 5), new Dimension(18, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0D, 1.0D));
/*  361 */     paramUIDefaults.put("CheckBox[MouseOver].iconPainter", new LazyPainter("javax.swing.plaf.nimbus.CheckBoxPainter", 6, new Insets(5, 5, 5, 5), new Dimension(18, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0D, 1.0D));
/*  362 */     paramUIDefaults.put("CheckBox[Focused+MouseOver].iconPainter", new LazyPainter("javax.swing.plaf.nimbus.CheckBoxPainter", 7, new Insets(5, 5, 5, 5), new Dimension(18, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0D, 1.0D));
/*  363 */     paramUIDefaults.put("CheckBox[Pressed].iconPainter", new LazyPainter("javax.swing.plaf.nimbus.CheckBoxPainter", 8, new Insets(5, 5, 5, 5), new Dimension(18, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0D, 1.0D));
/*  364 */     paramUIDefaults.put("CheckBox[Focused+Pressed].iconPainter", new LazyPainter("javax.swing.plaf.nimbus.CheckBoxPainter", 9, new Insets(5, 5, 5, 5), new Dimension(18, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0D, 1.0D));
/*  365 */     paramUIDefaults.put("CheckBox[Selected].iconPainter", new LazyPainter("javax.swing.plaf.nimbus.CheckBoxPainter", 10, new Insets(5, 5, 5, 5), new Dimension(18, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0D, 1.0D));
/*  366 */     paramUIDefaults.put("CheckBox[Focused+Selected].iconPainter", new LazyPainter("javax.swing.plaf.nimbus.CheckBoxPainter", 11, new Insets(5, 5, 5, 5), new Dimension(18, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0D, 1.0D));
/*  367 */     paramUIDefaults.put("CheckBox[Pressed+Selected].iconPainter", new LazyPainter("javax.swing.plaf.nimbus.CheckBoxPainter", 12, new Insets(5, 5, 5, 5), new Dimension(18, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0D, 1.0D));
/*  368 */     paramUIDefaults.put("CheckBox[Focused+Pressed+Selected].iconPainter", new LazyPainter("javax.swing.plaf.nimbus.CheckBoxPainter", 13, new Insets(5, 5, 5, 5), new Dimension(18, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0D, 1.0D));
/*  369 */     paramUIDefaults.put("CheckBox[MouseOver+Selected].iconPainter", new LazyPainter("javax.swing.plaf.nimbus.CheckBoxPainter", 14, new Insets(5, 5, 5, 5), new Dimension(18, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0D, 1.0D));
/*  370 */     paramUIDefaults.put("CheckBox[Focused+MouseOver+Selected].iconPainter", new LazyPainter("javax.swing.plaf.nimbus.CheckBoxPainter", 15, new Insets(5, 5, 5, 5), new Dimension(18, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0D, 1.0D));
/*  371 */     paramUIDefaults.put("CheckBox[Disabled+Selected].iconPainter", new LazyPainter("javax.swing.plaf.nimbus.CheckBoxPainter", 16, new Insets(5, 5, 5, 5), new Dimension(18, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0D, 1.0D));
/*  372 */     paramUIDefaults.put("CheckBox.icon", new NimbusIcon("CheckBox", "iconPainter", 18, 18));
/*      */ 
/*  375 */     paramUIDefaults.put("ColorChooser.contentMargins", new InsetsUIResource(5, 0, 0, 0));
/*  376 */     addColor(paramUIDefaults, "ColorChooser.swatchesDefaultRecentColor", 255, 255, 255, 255);
/*  377 */     paramUIDefaults.put("ColorChooser:\"ColorChooser.previewPanelHolder\".contentMargins", new InsetsUIResource(0, 5, 10, 5));
/*  378 */     paramUIDefaults.put("ColorChooser:\"ColorChooser.previewPanelHolder\":\"OptionPane.label\".contentMargins", new InsetsUIResource(0, 10, 10, 10));
/*      */ 
/*  381 */     paramUIDefaults.put("ComboBox.contentMargins", new InsetsUIResource(0, 0, 0, 0));
/*  382 */     paramUIDefaults.put("ComboBox.States", "Enabled,MouseOver,Pressed,Selected,Disabled,Focused,Editable");
/*  383 */     paramUIDefaults.put("ComboBox.Editable", new ComboBoxEditableState());
/*  384 */     paramUIDefaults.put("ComboBox.forceOpaque", Boolean.TRUE);
/*  385 */     paramUIDefaults.put("ComboBox.buttonWhenNotEditable", Boolean.TRUE);
/*  386 */     paramUIDefaults.put("ComboBox.rendererUseListColors", Boolean.FALSE);
/*  387 */     paramUIDefaults.put("ComboBox.pressedWhenPopupVisible", Boolean.TRUE);
/*  388 */     paramUIDefaults.put("ComboBox.squareButton", Boolean.FALSE);
/*  389 */     paramUIDefaults.put("ComboBox.popupInsets", new InsetsUIResource(-2, 2, 0, 2));
/*  390 */     paramUIDefaults.put("ComboBox.padding", new InsetsUIResource(3, 3, 3, 3));
/*  391 */     paramUIDefaults.put("ComboBox[Disabled].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ComboBoxPainter", 1, new Insets(8, 9, 8, 19), new Dimension(83, 24), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, (1.0D / 0.0D), 2.0D));
/*  392 */     paramUIDefaults.put("ComboBox[Disabled+Pressed].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ComboBoxPainter", 2, new Insets(8, 9, 8, 19), new Dimension(83, 24), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, (1.0D / 0.0D), 2.0D));
/*  393 */     paramUIDefaults.put("ComboBox[Enabled].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ComboBoxPainter", 3, new Insets(8, 9, 8, 19), new Dimension(83, 24), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, (1.0D / 0.0D), 2.0D));
/*  394 */     paramUIDefaults.put("ComboBox[Focused].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ComboBoxPainter", 4, new Insets(8, 9, 8, 19), new Dimension(83, 24), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, (1.0D / 0.0D), 2.0D));
/*  395 */     paramUIDefaults.put("ComboBox[Focused+MouseOver].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ComboBoxPainter", 5, new Insets(8, 9, 8, 19), new Dimension(83, 24), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, (1.0D / 0.0D), 2.0D));
/*  396 */     paramUIDefaults.put("ComboBox[MouseOver].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ComboBoxPainter", 6, new Insets(8, 9, 8, 19), new Dimension(83, 24), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, (1.0D / 0.0D), 2.0D));
/*  397 */     paramUIDefaults.put("ComboBox[Focused+Pressed].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ComboBoxPainter", 7, new Insets(8, 9, 8, 19), new Dimension(83, 24), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, (1.0D / 0.0D), 2.0D));
/*  398 */     paramUIDefaults.put("ComboBox[Pressed].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ComboBoxPainter", 8, new Insets(8, 9, 8, 19), new Dimension(83, 24), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, (1.0D / 0.0D), 2.0D));
/*  399 */     paramUIDefaults.put("ComboBox[Enabled+Selected].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ComboBoxPainter", 9, new Insets(8, 9, 8, 19), new Dimension(83, 24), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, (1.0D / 0.0D), 2.0D));
/*  400 */     paramUIDefaults.put("ComboBox[Disabled+Editable].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ComboBoxPainter", 10, new Insets(6, 5, 6, 17), new Dimension(79, 21), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, (1.0D / 0.0D), 2.0D));
/*  401 */     paramUIDefaults.put("ComboBox[Editable+Enabled].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ComboBoxPainter", 11, new Insets(6, 5, 6, 17), new Dimension(79, 21), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, (1.0D / 0.0D), 2.0D));
/*  402 */     paramUIDefaults.put("ComboBox[Editable+Focused].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ComboBoxPainter", 12, new Insets(5, 5, 5, 5), new Dimension(142, 27), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, (1.0D / 0.0D), 2.0D));
/*  403 */     paramUIDefaults.put("ComboBox[Editable+MouseOver].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ComboBoxPainter", 13, new Insets(4, 5, 5, 17), new Dimension(79, 21), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, (1.0D / 0.0D), 2.0D));
/*  404 */     paramUIDefaults.put("ComboBox[Editable+Pressed].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ComboBoxPainter", 14, new Insets(4, 5, 5, 17), new Dimension(79, 21), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, (1.0D / 0.0D), 2.0D));
/*  405 */     paramUIDefaults.put("ComboBox:\"ComboBox.textField\".contentMargins", new InsetsUIResource(0, 6, 0, 3));
/*  406 */     addColor(paramUIDefaults, "ComboBox:\"ComboBox.textField\"[Disabled].textForeground", "nimbusDisabledText", 0.0F, 0.0F, 0.0F, 0);
/*  407 */     paramUIDefaults.put("ComboBox:\"ComboBox.textField\"[Disabled].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ComboBoxTextFieldPainter", 1, new Insets(5, 3, 3, 1), new Dimension(64, 24), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, (1.0D / 0.0D), 2.0D));
/*  408 */     paramUIDefaults.put("ComboBox:\"ComboBox.textField\"[Enabled].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ComboBoxTextFieldPainter", 2, new Insets(5, 3, 3, 1), new Dimension(64, 24), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, (1.0D / 0.0D), 2.0D));
/*  409 */     addColor(paramUIDefaults, "ComboBox:\"ComboBox.textField\"[Selected].textForeground", "nimbusSelectedText", 0.0F, 0.0F, 0.0F, 0);
/*  410 */     paramUIDefaults.put("ComboBox:\"ComboBox.textField\"[Selected].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ComboBoxTextFieldPainter", 3, new Insets(5, 3, 3, 1), new Dimension(64, 24), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, (1.0D / 0.0D), 2.0D));
/*  411 */     paramUIDefaults.put("ComboBox:\"ComboBox.arrowButton\".contentMargins", new InsetsUIResource(0, 0, 0, 0));
/*  412 */     paramUIDefaults.put("ComboBox:\"ComboBox.arrowButton\".States", "Enabled,MouseOver,Pressed,Disabled,Editable");
/*  413 */     paramUIDefaults.put("ComboBox:\"ComboBox.arrowButton\".Editable", new ComboBoxArrowButtonEditableState());
/*  414 */     paramUIDefaults.put("ComboBox:\"ComboBox.arrowButton\".size", new Integer(19));
/*  415 */     paramUIDefaults.put("ComboBox:\"ComboBox.arrowButton\"[Disabled+Editable].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ComboBoxArrowButtonPainter", 5, new Insets(8, 1, 8, 8), new Dimension(20, 24), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, (1.0D / 0.0D), 2.0D));
/*  416 */     paramUIDefaults.put("ComboBox:\"ComboBox.arrowButton\"[Editable+Enabled].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ComboBoxArrowButtonPainter", 6, new Insets(8, 1, 8, 8), new Dimension(20, 24), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, (1.0D / 0.0D), 2.0D));
/*  417 */     paramUIDefaults.put("ComboBox:\"ComboBox.arrowButton\"[Editable+MouseOver].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ComboBoxArrowButtonPainter", 7, new Insets(8, 1, 8, 8), new Dimension(20, 24), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, (1.0D / 0.0D), 2.0D));
/*  418 */     paramUIDefaults.put("ComboBox:\"ComboBox.arrowButton\"[Editable+Pressed].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ComboBoxArrowButtonPainter", 8, new Insets(8, 1, 8, 8), new Dimension(20, 24), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, (1.0D / 0.0D), 2.0D));
/*  419 */     paramUIDefaults.put("ComboBox:\"ComboBox.arrowButton\"[Editable+Selected].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ComboBoxArrowButtonPainter", 9, new Insets(8, 1, 8, 8), new Dimension(20, 24), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, (1.0D / 0.0D), 2.0D));
/*  420 */     paramUIDefaults.put("ComboBox:\"ComboBox.arrowButton\"[Enabled].foregroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ComboBoxArrowButtonPainter", 10, new Insets(6, 9, 6, 10), new Dimension(24, 19), true, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, (1.0D / 0.0D), 2.0D));
/*  421 */     paramUIDefaults.put("ComboBox:\"ComboBox.arrowButton\"[MouseOver].foregroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ComboBoxArrowButtonPainter", 11, new Insets(6, 9, 6, 10), new Dimension(24, 19), true, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, (1.0D / 0.0D), 2.0D));
/*  422 */     paramUIDefaults.put("ComboBox:\"ComboBox.arrowButton\"[Disabled].foregroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ComboBoxArrowButtonPainter", 12, new Insets(6, 9, 6, 10), new Dimension(24, 19), true, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, (1.0D / 0.0D), 2.0D));
/*  423 */     paramUIDefaults.put("ComboBox:\"ComboBox.arrowButton\"[Pressed].foregroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ComboBoxArrowButtonPainter", 13, new Insets(6, 9, 6, 10), new Dimension(24, 19), true, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, (1.0D / 0.0D), 2.0D));
/*  424 */     paramUIDefaults.put("ComboBox:\"ComboBox.arrowButton\"[Selected].foregroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ComboBoxArrowButtonPainter", 14, new Insets(6, 9, 6, 10), new Dimension(24, 19), true, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, (1.0D / 0.0D), 2.0D));
/*  425 */     paramUIDefaults.put("ComboBox:\"ComboBox.listRenderer\".contentMargins", new InsetsUIResource(2, 4, 2, 4));
/*  426 */     paramUIDefaults.put("ComboBox:\"ComboBox.listRenderer\".opaque", Boolean.TRUE);
/*  427 */     addColor(paramUIDefaults, "ComboBox:\"ComboBox.listRenderer\".background", "nimbusLightBackground", 0.0F, 0.0F, 0.0F, 0);
/*  428 */     addColor(paramUIDefaults, "ComboBox:\"ComboBox.listRenderer\"[Disabled].textForeground", "nimbusDisabledText", 0.0F, 0.0F, 0.0F, 0);
/*  429 */     addColor(paramUIDefaults, "ComboBox:\"ComboBox.listRenderer\"[Selected].textForeground", "nimbusSelectedText", 0.0F, 0.0F, 0.0F, 0);
/*  430 */     addColor(paramUIDefaults, "ComboBox:\"ComboBox.listRenderer\"[Selected].background", "nimbusSelectionBackground", 0.0F, 0.0F, 0.0F, 0);
/*  431 */     paramUIDefaults.put("ComboBox:\"ComboBox.renderer\".contentMargins", new InsetsUIResource(2, 4, 2, 4));
/*  432 */     addColor(paramUIDefaults, "ComboBox:\"ComboBox.renderer\"[Disabled].textForeground", "nimbusDisabledText", 0.0F, 0.0F, 0.0F, 0);
/*  433 */     addColor(paramUIDefaults, "ComboBox:\"ComboBox.renderer\"[Selected].textForeground", "nimbusSelectedText", 0.0F, 0.0F, 0.0F, 0);
/*  434 */     addColor(paramUIDefaults, "ComboBox:\"ComboBox.renderer\"[Selected].background", "nimbusSelectionBackground", 0.0F, 0.0F, 0.0F, 0);
/*      */ 
/*  437 */     paramUIDefaults.put("\"ComboBox.scrollPane\".contentMargins", new InsetsUIResource(0, 0, 0, 0));
/*      */ 
/*  440 */     paramUIDefaults.put("FileChooser.contentMargins", new InsetsUIResource(10, 10, 10, 10));
/*  441 */     paramUIDefaults.put("FileChooser.opaque", Boolean.TRUE);
/*  442 */     paramUIDefaults.put("FileChooser.usesSingleFilePane", Boolean.TRUE);
/*  443 */     paramUIDefaults.put("FileChooser[Enabled].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.FileChooserPainter", 1, new Insets(0, 0, 0, 0), new Dimension(100, 30), false, AbstractRegionPainter.PaintContext.CacheMode.NO_CACHING, 1.0D, 1.0D));
/*  444 */     paramUIDefaults.put("FileChooser[Enabled].fileIconPainter", new LazyPainter("javax.swing.plaf.nimbus.FileChooserPainter", 2, new Insets(5, 5, 5, 5), new Dimension(16, 16), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0D, 1.0D));
/*  445 */     paramUIDefaults.put("FileChooser.fileIcon", new NimbusIcon("FileChooser", "fileIconPainter", 16, 16));
/*  446 */     paramUIDefaults.put("FileChooser[Enabled].directoryIconPainter", new LazyPainter("javax.swing.plaf.nimbus.FileChooserPainter", 3, new Insets(5, 5, 5, 5), new Dimension(16, 16), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0D, 1.0D));
/*  447 */     paramUIDefaults.put("FileChooser.directoryIcon", new NimbusIcon("FileChooser", "directoryIconPainter", 16, 16));
/*  448 */     paramUIDefaults.put("FileChooser[Enabled].upFolderIconPainter", new LazyPainter("javax.swing.plaf.nimbus.FileChooserPainter", 4, new Insets(5, 5, 5, 5), new Dimension(16, 16), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0D, 1.0D));
/*  449 */     paramUIDefaults.put("FileChooser.upFolderIcon", new NimbusIcon("FileChooser", "upFolderIconPainter", 16, 16));
/*  450 */     paramUIDefaults.put("FileChooser[Enabled].newFolderIconPainter", new LazyPainter("javax.swing.plaf.nimbus.FileChooserPainter", 5, new Insets(5, 5, 5, 5), new Dimension(16, 16), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0D, 1.0D));
/*  451 */     paramUIDefaults.put("FileChooser.newFolderIcon", new NimbusIcon("FileChooser", "newFolderIconPainter", 16, 16));
/*  452 */     paramUIDefaults.put("FileChooser[Enabled].hardDriveIconPainter", new LazyPainter("javax.swing.plaf.nimbus.FileChooserPainter", 7, new Insets(5, 5, 5, 5), new Dimension(16, 16), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0D, 1.0D));
/*  453 */     paramUIDefaults.put("FileChooser.hardDriveIcon", new NimbusIcon("FileChooser", "hardDriveIconPainter", 16, 16));
/*  454 */     paramUIDefaults.put("FileChooser[Enabled].floppyDriveIconPainter", new LazyPainter("javax.swing.plaf.nimbus.FileChooserPainter", 8, new Insets(5, 5, 5, 5), new Dimension(16, 16), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0D, 1.0D));
/*  455 */     paramUIDefaults.put("FileChooser.floppyDriveIcon", new NimbusIcon("FileChooser", "floppyDriveIconPainter", 16, 16));
/*  456 */     paramUIDefaults.put("FileChooser[Enabled].homeFolderIconPainter", new LazyPainter("javax.swing.plaf.nimbus.FileChooserPainter", 9, new Insets(5, 5, 5, 5), new Dimension(16, 16), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0D, 1.0D));
/*  457 */     paramUIDefaults.put("FileChooser.homeFolderIcon", new NimbusIcon("FileChooser", "homeFolderIconPainter", 16, 16));
/*  458 */     paramUIDefaults.put("FileChooser[Enabled].detailsViewIconPainter", new LazyPainter("javax.swing.plaf.nimbus.FileChooserPainter", 10, new Insets(5, 5, 5, 5), new Dimension(16, 16), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0D, 1.0D));
/*  459 */     paramUIDefaults.put("FileChooser.detailsViewIcon", new NimbusIcon("FileChooser", "detailsViewIconPainter", 16, 16));
/*  460 */     paramUIDefaults.put("FileChooser[Enabled].listViewIconPainter", new LazyPainter("javax.swing.plaf.nimbus.FileChooserPainter", 11, new Insets(5, 5, 5, 5), new Dimension(16, 16), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0D, 1.0D));
/*  461 */     paramUIDefaults.put("FileChooser.listViewIcon", new NimbusIcon("FileChooser", "listViewIconPainter", 16, 16));
/*      */ 
/*  464 */     paramUIDefaults.put("InternalFrameTitlePane.contentMargins", new InsetsUIResource(0, 0, 0, 0));
/*  465 */     paramUIDefaults.put("InternalFrameTitlePane.maxFrameIconSize", new DimensionUIResource(18, 18));
/*      */ 
/*  468 */     paramUIDefaults.put("InternalFrame.contentMargins", new InsetsUIResource(1, 6, 6, 6));
/*  469 */     paramUIDefaults.put("InternalFrame.States", "Enabled,WindowFocused");
/*  470 */     paramUIDefaults.put("InternalFrame.WindowFocused", new InternalFrameWindowFocusedState());
/*  471 */     paramUIDefaults.put("InternalFrame[Enabled].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.InternalFramePainter", 1, new Insets(25, 6, 6, 6), new Dimension(25, 36), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, (1.0D / 0.0D), (1.0D / 0.0D)));
/*  472 */     paramUIDefaults.put("InternalFrame[Enabled+WindowFocused].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.InternalFramePainter", 2, new Insets(25, 6, 6, 6), new Dimension(25, 36), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, (1.0D / 0.0D), (1.0D / 0.0D)));
/*  473 */     paramUIDefaults.put("InternalFrame:InternalFrameTitlePane.contentMargins", new InsetsUIResource(3, 0, 3, 0));
/*  474 */     paramUIDefaults.put("InternalFrame:InternalFrameTitlePane.States", "Enabled,WindowFocused");
/*  475 */     paramUIDefaults.put("InternalFrame:InternalFrameTitlePane.WindowFocused", new InternalFrameTitlePaneWindowFocusedState());
/*  476 */     paramUIDefaults.put("InternalFrame:InternalFrameTitlePane.titleAlignment", "CENTER");
/*  477 */     addColor(paramUIDefaults, "InternalFrame:InternalFrameTitlePane[Enabled].textForeground", "nimbusDisabledText", 0.0F, 0.0F, 0.0F, 0);
/*  478 */     paramUIDefaults.put("InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.menuButton\".contentMargins", new InsetsUIResource(0, 0, 0, 0));
/*  479 */     paramUIDefaults.put("InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.menuButton\".States", "Enabled,MouseOver,Pressed,Disabled,Focused,Selected,WindowNotFocused");
/*  480 */     paramUIDefaults.put("InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.menuButton\".WindowNotFocused", new InternalFrameTitlePaneMenuButtonWindowNotFocusedState());
/*  481 */     paramUIDefaults.put("InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.menuButton\".test", "am InternalFrameTitlePane.menuButton");
/*  482 */     paramUIDefaults.put("InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.menuButton\"[Enabled].iconPainter", new LazyPainter("javax.swing.plaf.nimbus.InternalFrameTitlePaneMenuButtonPainter", 1, new Insets(0, 0, 0, 0), new Dimension(19, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, (1.0D / 0.0D), (1.0D / 0.0D)));
/*  483 */     paramUIDefaults.put("InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.menuButton\"[Disabled].iconPainter", new LazyPainter("javax.swing.plaf.nimbus.InternalFrameTitlePaneMenuButtonPainter", 2, new Insets(0, 0, 0, 0), new Dimension(19, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, (1.0D / 0.0D), (1.0D / 0.0D)));
/*  484 */     paramUIDefaults.put("InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.menuButton\"[MouseOver].iconPainter", new LazyPainter("javax.swing.plaf.nimbus.InternalFrameTitlePaneMenuButtonPainter", 3, new Insets(0, 0, 0, 0), new Dimension(19, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, (1.0D / 0.0D), (1.0D / 0.0D)));
/*  485 */     paramUIDefaults.put("InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.menuButton\"[Pressed].iconPainter", new LazyPainter("javax.swing.plaf.nimbus.InternalFrameTitlePaneMenuButtonPainter", 4, new Insets(0, 0, 0, 0), new Dimension(19, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, (1.0D / 0.0D), (1.0D / 0.0D)));
/*  486 */     paramUIDefaults.put("InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.menuButton\"[Enabled+WindowNotFocused].iconPainter", new LazyPainter("javax.swing.plaf.nimbus.InternalFrameTitlePaneMenuButtonPainter", 5, new Insets(0, 0, 0, 0), new Dimension(19, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, (1.0D / 0.0D), (1.0D / 0.0D)));
/*  487 */     paramUIDefaults.put("InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.menuButton\"[MouseOver+WindowNotFocused].iconPainter", new LazyPainter("javax.swing.plaf.nimbus.InternalFrameTitlePaneMenuButtonPainter", 6, new Insets(0, 0, 0, 0), new Dimension(19, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, (1.0D / 0.0D), (1.0D / 0.0D)));
/*  488 */     paramUIDefaults.put("InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.menuButton\"[Pressed+WindowNotFocused].iconPainter", new LazyPainter("javax.swing.plaf.nimbus.InternalFrameTitlePaneMenuButtonPainter", 7, new Insets(0, 0, 0, 0), new Dimension(19, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, (1.0D / 0.0D), (1.0D / 0.0D)));
/*  489 */     paramUIDefaults.put("InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.menuButton\".icon", new NimbusIcon("InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.menuButton\"", "iconPainter", 19, 18));
/*  490 */     paramUIDefaults.put("InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.iconifyButton\".contentMargins", new InsetsUIResource(9, 9, 9, 9));
/*  491 */     paramUIDefaults.put("InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.iconifyButton\".States", "Enabled,MouseOver,Pressed,Disabled,Focused,Selected,WindowNotFocused");
/*  492 */     paramUIDefaults.put("InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.iconifyButton\".WindowNotFocused", new InternalFrameTitlePaneIconifyButtonWindowNotFocusedState());
/*  493 */     paramUIDefaults.put("InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.iconifyButton\"[Enabled].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.InternalFrameTitlePaneIconifyButtonPainter", 1, new Insets(0, 0, 0, 0), new Dimension(19, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, (1.0D / 0.0D), (1.0D / 0.0D)));
/*  494 */     paramUIDefaults.put("InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.iconifyButton\"[Disabled].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.InternalFrameTitlePaneIconifyButtonPainter", 2, new Insets(0, 0, 0, 0), new Dimension(19, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, (1.0D / 0.0D), (1.0D / 0.0D)));
/*  495 */     paramUIDefaults.put("InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.iconifyButton\"[MouseOver].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.InternalFrameTitlePaneIconifyButtonPainter", 3, new Insets(0, 0, 0, 0), new Dimension(19, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, (1.0D / 0.0D), (1.0D / 0.0D)));
/*  496 */     paramUIDefaults.put("InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.iconifyButton\"[Pressed].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.InternalFrameTitlePaneIconifyButtonPainter", 4, new Insets(0, 0, 0, 0), new Dimension(19, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, (1.0D / 0.0D), (1.0D / 0.0D)));
/*  497 */     paramUIDefaults.put("InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.iconifyButton\"[Enabled+WindowNotFocused].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.InternalFrameTitlePaneIconifyButtonPainter", 5, new Insets(0, 0, 0, 0), new Dimension(19, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, (1.0D / 0.0D), (1.0D / 0.0D)));
/*  498 */     paramUIDefaults.put("InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.iconifyButton\"[MouseOver+WindowNotFocused].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.InternalFrameTitlePaneIconifyButtonPainter", 6, new Insets(0, 0, 0, 0), new Dimension(19, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, (1.0D / 0.0D), (1.0D / 0.0D)));
/*  499 */     paramUIDefaults.put("InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.iconifyButton\"[Pressed+WindowNotFocused].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.InternalFrameTitlePaneIconifyButtonPainter", 7, new Insets(0, 0, 0, 0), new Dimension(19, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, (1.0D / 0.0D), (1.0D / 0.0D)));
/*  500 */     paramUIDefaults.put("InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.maximizeButton\".contentMargins", new InsetsUIResource(9, 9, 9, 9));
/*  501 */     paramUIDefaults.put("InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.maximizeButton\".States", "Enabled,MouseOver,Pressed,Disabled,Focused,Selected,WindowNotFocused,WindowMaximized");
/*  502 */     paramUIDefaults.put("InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.maximizeButton\".WindowNotFocused", new InternalFrameTitlePaneMaximizeButtonWindowNotFocusedState());
/*  503 */     paramUIDefaults.put("InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.maximizeButton\".WindowMaximized", new InternalFrameTitlePaneMaximizeButtonWindowMaximizedState());
/*  504 */     paramUIDefaults.put("InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.maximizeButton\"[Disabled+WindowMaximized].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.InternalFrameTitlePaneMaximizeButtonPainter", 1, new Insets(0, 0, 0, 0), new Dimension(19, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, (1.0D / 0.0D), (1.0D / 0.0D)));
/*  505 */     paramUIDefaults.put("InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.maximizeButton\"[Enabled+WindowMaximized].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.InternalFrameTitlePaneMaximizeButtonPainter", 2, new Insets(0, 0, 0, 0), new Dimension(19, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, (1.0D / 0.0D), (1.0D / 0.0D)));
/*  506 */     paramUIDefaults.put("InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.maximizeButton\"[MouseOver+WindowMaximized].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.InternalFrameTitlePaneMaximizeButtonPainter", 3, new Insets(0, 0, 0, 0), new Dimension(19, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, (1.0D / 0.0D), (1.0D / 0.0D)));
/*  507 */     paramUIDefaults.put("InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.maximizeButton\"[Pressed+WindowMaximized].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.InternalFrameTitlePaneMaximizeButtonPainter", 4, new Insets(0, 0, 0, 0), new Dimension(19, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, (1.0D / 0.0D), (1.0D / 0.0D)));
/*  508 */     paramUIDefaults.put("InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.maximizeButton\"[Enabled+WindowMaximized+WindowNotFocused].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.InternalFrameTitlePaneMaximizeButtonPainter", 5, new Insets(0, 0, 0, 0), new Dimension(19, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, (1.0D / 0.0D), (1.0D / 0.0D)));
/*  509 */     paramUIDefaults.put("InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.maximizeButton\"[MouseOver+WindowMaximized+WindowNotFocused].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.InternalFrameTitlePaneMaximizeButtonPainter", 6, new Insets(0, 0, 0, 0), new Dimension(19, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, (1.0D / 0.0D), (1.0D / 0.0D)));
/*  510 */     paramUIDefaults.put("InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.maximizeButton\"[Pressed+WindowMaximized+WindowNotFocused].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.InternalFrameTitlePaneMaximizeButtonPainter", 7, new Insets(0, 0, 0, 0), new Dimension(19, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, (1.0D / 0.0D), (1.0D / 0.0D)));
/*  511 */     paramUIDefaults.put("InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.maximizeButton\"[Disabled].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.InternalFrameTitlePaneMaximizeButtonPainter", 8, new Insets(0, 0, 0, 0), new Dimension(19, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, (1.0D / 0.0D), (1.0D / 0.0D)));
/*  512 */     paramUIDefaults.put("InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.maximizeButton\"[Enabled].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.InternalFrameTitlePaneMaximizeButtonPainter", 9, new Insets(0, 0, 0, 0), new Dimension(19, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, (1.0D / 0.0D), (1.0D / 0.0D)));
/*  513 */     paramUIDefaults.put("InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.maximizeButton\"[MouseOver].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.InternalFrameTitlePaneMaximizeButtonPainter", 10, new Insets(0, 0, 0, 0), new Dimension(19, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, (1.0D / 0.0D), (1.0D / 0.0D)));
/*  514 */     paramUIDefaults.put("InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.maximizeButton\"[Pressed].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.InternalFrameTitlePaneMaximizeButtonPainter", 11, new Insets(0, 0, 0, 0), new Dimension(19, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, (1.0D / 0.0D), (1.0D / 0.0D)));
/*  515 */     paramUIDefaults.put("InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.maximizeButton\"[Enabled+WindowNotFocused].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.InternalFrameTitlePaneMaximizeButtonPainter", 12, new Insets(0, 0, 0, 0), new Dimension(19, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, (1.0D / 0.0D), (1.0D / 0.0D)));
/*  516 */     paramUIDefaults.put("InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.maximizeButton\"[MouseOver+WindowNotFocused].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.InternalFrameTitlePaneMaximizeButtonPainter", 13, new Insets(0, 0, 0, 0), new Dimension(19, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, (1.0D / 0.0D), (1.0D / 0.0D)));
/*  517 */     paramUIDefaults.put("InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.maximizeButton\"[Pressed+WindowNotFocused].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.InternalFrameTitlePaneMaximizeButtonPainter", 14, new Insets(0, 0, 0, 0), new Dimension(19, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, (1.0D / 0.0D), (1.0D / 0.0D)));
/*  518 */     paramUIDefaults.put("InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.closeButton\".contentMargins", new InsetsUIResource(9, 9, 9, 9));
/*  519 */     paramUIDefaults.put("InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.closeButton\".States", "Enabled,MouseOver,Pressed,Disabled,Focused,Selected,WindowNotFocused");
/*  520 */     paramUIDefaults.put("InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.closeButton\".WindowNotFocused", new InternalFrameTitlePaneCloseButtonWindowNotFocusedState());
/*  521 */     paramUIDefaults.put("InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.closeButton\"[Disabled].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.InternalFrameTitlePaneCloseButtonPainter", 1, new Insets(0, 0, 0, 0), new Dimension(19, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, (1.0D / 0.0D), (1.0D / 0.0D)));
/*  522 */     paramUIDefaults.put("InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.closeButton\"[Enabled].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.InternalFrameTitlePaneCloseButtonPainter", 2, new Insets(0, 0, 0, 0), new Dimension(19, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, (1.0D / 0.0D), (1.0D / 0.0D)));
/*  523 */     paramUIDefaults.put("InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.closeButton\"[MouseOver].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.InternalFrameTitlePaneCloseButtonPainter", 3, new Insets(0, 0, 0, 0), new Dimension(19, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, (1.0D / 0.0D), (1.0D / 0.0D)));
/*  524 */     paramUIDefaults.put("InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.closeButton\"[Pressed].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.InternalFrameTitlePaneCloseButtonPainter", 4, new Insets(0, 0, 0, 0), new Dimension(19, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, (1.0D / 0.0D), (1.0D / 0.0D)));
/*  525 */     paramUIDefaults.put("InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.closeButton\"[Enabled+WindowNotFocused].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.InternalFrameTitlePaneCloseButtonPainter", 5, new Insets(0, 0, 0, 0), new Dimension(19, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, (1.0D / 0.0D), (1.0D / 0.0D)));
/*  526 */     paramUIDefaults.put("InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.closeButton\"[MouseOver+WindowNotFocused].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.InternalFrameTitlePaneCloseButtonPainter", 6, new Insets(0, 0, 0, 0), new Dimension(19, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, (1.0D / 0.0D), (1.0D / 0.0D)));
/*  527 */     paramUIDefaults.put("InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.closeButton\"[Pressed+WindowNotFocused].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.InternalFrameTitlePaneCloseButtonPainter", 7, new Insets(0, 0, 0, 0), new Dimension(19, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, (1.0D / 0.0D), (1.0D / 0.0D)));
/*      */ 
/*  530 */     paramUIDefaults.put("DesktopIcon.contentMargins", new InsetsUIResource(4, 6, 5, 4));
/*  531 */     paramUIDefaults.put("DesktopIcon[Enabled].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.DesktopIconPainter", 1, new Insets(5, 5, 5, 5), new Dimension(28, 26), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, (1.0D / 0.0D), (1.0D / 0.0D)));
/*      */ 
/*  534 */     paramUIDefaults.put("DesktopPane.contentMargins", new InsetsUIResource(0, 0, 0, 0));
/*  535 */     paramUIDefaults.put("DesktopPane.opaque", Boolean.TRUE);
/*  536 */     paramUIDefaults.put("DesktopPane[Enabled].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.DesktopPanePainter", 1, new Insets(0, 0, 0, 0), new Dimension(300, 232), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, (1.0D / 0.0D), (1.0D / 0.0D)));
/*      */ 
/*  539 */     paramUIDefaults.put("Label.contentMargins", new InsetsUIResource(0, 0, 0, 0));
/*  540 */     addColor(paramUIDefaults, "Label[Disabled].textForeground", "nimbusDisabledText", 0.0F, 0.0F, 0.0F, 0);
/*      */ 
/*  543 */     paramUIDefaults.put("List.contentMargins", new InsetsUIResource(0, 0, 0, 0));
/*  544 */     paramUIDefaults.put("List.opaque", Boolean.TRUE);
/*  545 */     addColor(paramUIDefaults, "List.background", "nimbusLightBackground", 0.0F, 0.0F, 0.0F, 0);
/*  546 */     paramUIDefaults.put("List.rendererUseListColors", Boolean.TRUE);
/*  547 */     paramUIDefaults.put("List.rendererUseUIBorder", Boolean.TRUE);
/*  548 */     paramUIDefaults.put("List.cellNoFocusBorder", new BorderUIResource(BorderFactory.createEmptyBorder(2, 5, 2, 5)));
/*  549 */     paramUIDefaults.put("List.focusCellHighlightBorder", new BorderUIResource(new PainterBorder("Tree:TreeCell[Enabled+Focused].backgroundPainter", new Insets(2, 5, 2, 5))));
/*  550 */     addColor(paramUIDefaults, "List.dropLineColor", "nimbusFocus", 0.0F, 0.0F, 0.0F, 0);
/*  551 */     addColor(paramUIDefaults, "List[Selected].textForeground", "nimbusLightBackground", 0.0F, 0.0F, 0.0F, 0);
/*  552 */     addColor(paramUIDefaults, "List[Selected].textBackground", "nimbusSelectionBackground", 0.0F, 0.0F, 0.0F, 0);
/*  553 */     addColor(paramUIDefaults, "List[Disabled+Selected].textBackground", "nimbusSelectionBackground", 0.0F, 0.0F, 0.0F, 0);
/*  554 */     addColor(paramUIDefaults, "List[Disabled].textForeground", "nimbusDisabledText", 0.0F, 0.0F, 0.0F, 0);
/*  555 */     paramUIDefaults.put("List:\"List.cellRenderer\".contentMargins", new InsetsUIResource(0, 0, 0, 0));
/*  556 */     paramUIDefaults.put("List:\"List.cellRenderer\".opaque", Boolean.TRUE);
/*  557 */     addColor(paramUIDefaults, "List:\"List.cellRenderer\"[Disabled].textForeground", "nimbusDisabledText", 0.0F, 0.0F, 0.0F, 0);
/*  558 */     addColor(paramUIDefaults, "List:\"List.cellRenderer\"[Disabled].background", "nimbusSelectionBackground", 0.0F, 0.0F, 0.0F, 0);
/*      */ 
/*  561 */     paramUIDefaults.put("MenuBar.contentMargins", new InsetsUIResource(2, 6, 2, 6));
/*  562 */     paramUIDefaults.put("MenuBar[Enabled].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.MenuBarPainter", 1, new Insets(1, 0, 0, 0), new Dimension(18, 22), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, (1.0D / 0.0D), 2.0D));
/*  563 */     paramUIDefaults.put("MenuBar[Enabled].borderPainter", new LazyPainter("javax.swing.plaf.nimbus.MenuBarPainter", 2, new Insets(0, 0, 1, 0), new Dimension(30, 30), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, (1.0D / 0.0D), 2.0D));
/*  564 */     paramUIDefaults.put("MenuBar:Menu.contentMargins", new InsetsUIResource(1, 4, 2, 4));
/*  565 */     addColor(paramUIDefaults, "MenuBar:Menu[Disabled].textForeground", "nimbusDisabledText", 0.0F, 0.0F, 0.0F, 0);
/*  566 */     addColor(paramUIDefaults, "MenuBar:Menu[Enabled].textForeground", 35, 35, 36, 255);
/*  567 */     addColor(paramUIDefaults, "MenuBar:Menu[Selected].textForeground", 255, 255, 255, 255);
/*  568 */     paramUIDefaults.put("MenuBar:Menu[Selected].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.MenuBarMenuPainter", 3, new Insets(0, 0, 0, 0), new Dimension(100, 30), false, AbstractRegionPainter.PaintContext.CacheMode.NO_CACHING, 1.0D, 1.0D));
/*  569 */     paramUIDefaults.put("MenuBar:Menu:MenuItemAccelerator.contentMargins", new InsetsUIResource(0, 0, 0, 0));
/*      */ 
/*  572 */     paramUIDefaults.put("MenuItem.contentMargins", new InsetsUIResource(1, 12, 2, 13));
/*  573 */     paramUIDefaults.put("MenuItem.textIconGap", new Integer(5));
/*  574 */     addColor(paramUIDefaults, "MenuItem[Disabled].textForeground", "nimbusDisabledText", 0.0F, 0.0F, 0.0F, 0);
/*  575 */     addColor(paramUIDefaults, "MenuItem[Enabled].textForeground", 35, 35, 36, 255);
/*  576 */     addColor(paramUIDefaults, "MenuItem[MouseOver].textForeground", 255, 255, 255, 255);
/*  577 */     paramUIDefaults.put("MenuItem[MouseOver].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.MenuItemPainter", 3, new Insets(0, 0, 0, 0), new Dimension(100, 3), false, AbstractRegionPainter.PaintContext.CacheMode.NO_CACHING, 1.0D, 1.0D));
/*  578 */     paramUIDefaults.put("MenuItem:MenuItemAccelerator.contentMargins", new InsetsUIResource(0, 0, 0, 0));
/*  579 */     addColor(paramUIDefaults, "MenuItem:MenuItemAccelerator[Disabled].textForeground", "nimbusDisabledText", 0.0F, 0.0F, 0.0F, 0);
/*  580 */     addColor(paramUIDefaults, "MenuItem:MenuItemAccelerator[MouseOver].textForeground", 255, 255, 255, 255);
/*      */ 
/*  583 */     paramUIDefaults.put("RadioButtonMenuItem.contentMargins", new InsetsUIResource(1, 12, 2, 13));
/*  584 */     paramUIDefaults.put("RadioButtonMenuItem.textIconGap", new Integer(5));
/*  585 */     addColor(paramUIDefaults, "RadioButtonMenuItem[Disabled].textForeground", "nimbusDisabledText", 0.0F, 0.0F, 0.0F, 0);
/*  586 */     addColor(paramUIDefaults, "RadioButtonMenuItem[Enabled].textForeground", 35, 35, 36, 255);
/*  587 */     addColor(paramUIDefaults, "RadioButtonMenuItem[MouseOver].textForeground", 255, 255, 255, 255);
/*  588 */     paramUIDefaults.put("RadioButtonMenuItem[MouseOver].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.RadioButtonMenuItemPainter", 3, new Insets(0, 0, 0, 0), new Dimension(100, 3), false, AbstractRegionPainter.PaintContext.CacheMode.NO_CACHING, 1.0D, 1.0D));
/*  589 */     addColor(paramUIDefaults, "RadioButtonMenuItem[MouseOver+Selected].textForeground", 255, 255, 255, 255);
/*  590 */     paramUIDefaults.put("RadioButtonMenuItem[MouseOver+Selected].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.RadioButtonMenuItemPainter", 4, new Insets(0, 0, 0, 0), new Dimension(100, 3), false, AbstractRegionPainter.PaintContext.CacheMode.NO_CACHING, 1.0D, 1.0D));
/*  591 */     paramUIDefaults.put("RadioButtonMenuItem[Disabled+Selected].checkIconPainter", new LazyPainter("javax.swing.plaf.nimbus.RadioButtonMenuItemPainter", 5, new Insets(5, 5, 5, 5), new Dimension(9, 10), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0D, 1.0D));
/*  592 */     paramUIDefaults.put("RadioButtonMenuItem[Enabled+Selected].checkIconPainter", new LazyPainter("javax.swing.plaf.nimbus.RadioButtonMenuItemPainter", 6, new Insets(5, 5, 5, 5), new Dimension(9, 10), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0D, 1.0D));
/*  593 */     paramUIDefaults.put("RadioButtonMenuItem[MouseOver+Selected].checkIconPainter", new LazyPainter("javax.swing.plaf.nimbus.RadioButtonMenuItemPainter", 7, new Insets(5, 5, 5, 5), new Dimension(9, 10), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0D, 1.0D));
/*  594 */     paramUIDefaults.put("RadioButtonMenuItem.checkIcon", new NimbusIcon("RadioButtonMenuItem", "checkIconPainter", 9, 10));
/*  595 */     paramUIDefaults.put("RadioButtonMenuItem:MenuItemAccelerator.contentMargins", new InsetsUIResource(0, 0, 0, 0));
/*  596 */     addColor(paramUIDefaults, "RadioButtonMenuItem:MenuItemAccelerator[MouseOver].textForeground", 255, 255, 255, 255);
/*      */ 
/*  599 */     paramUIDefaults.put("CheckBoxMenuItem.contentMargins", new InsetsUIResource(1, 12, 2, 13));
/*  600 */     paramUIDefaults.put("CheckBoxMenuItem.textIconGap", new Integer(5));
/*  601 */     addColor(paramUIDefaults, "CheckBoxMenuItem[Disabled].textForeground", "nimbusDisabledText", 0.0F, 0.0F, 0.0F, 0);
/*  602 */     addColor(paramUIDefaults, "CheckBoxMenuItem[Enabled].textForeground", 35, 35, 36, 255);
/*  603 */     addColor(paramUIDefaults, "CheckBoxMenuItem[MouseOver].textForeground", 255, 255, 255, 255);
/*  604 */     paramUIDefaults.put("CheckBoxMenuItem[MouseOver].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.CheckBoxMenuItemPainter", 3, new Insets(0, 0, 0, 0), new Dimension(100, 3), false, AbstractRegionPainter.PaintContext.CacheMode.NO_CACHING, 1.0D, 1.0D));
/*  605 */     addColor(paramUIDefaults, "CheckBoxMenuItem[MouseOver+Selected].textForeground", 255, 255, 255, 255);
/*  606 */     paramUIDefaults.put("CheckBoxMenuItem[MouseOver+Selected].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.CheckBoxMenuItemPainter", 4, new Insets(0, 0, 0, 0), new Dimension(100, 3), false, AbstractRegionPainter.PaintContext.CacheMode.NO_CACHING, 1.0D, 1.0D));
/*  607 */     paramUIDefaults.put("CheckBoxMenuItem[Disabled+Selected].checkIconPainter", new LazyPainter("javax.swing.plaf.nimbus.CheckBoxMenuItemPainter", 5, new Insets(5, 5, 5, 5), new Dimension(9, 10), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0D, 1.0D));
/*  608 */     paramUIDefaults.put("CheckBoxMenuItem[Enabled+Selected].checkIconPainter", new LazyPainter("javax.swing.plaf.nimbus.CheckBoxMenuItemPainter", 6, new Insets(5, 5, 5, 5), new Dimension(9, 10), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0D, 1.0D));
/*  609 */     paramUIDefaults.put("CheckBoxMenuItem[MouseOver+Selected].checkIconPainter", new LazyPainter("javax.swing.plaf.nimbus.CheckBoxMenuItemPainter", 7, new Insets(5, 5, 5, 5), new Dimension(9, 10), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0D, 1.0D));
/*  610 */     paramUIDefaults.put("CheckBoxMenuItem.checkIcon", new NimbusIcon("CheckBoxMenuItem", "checkIconPainter", 9, 10));
/*  611 */     paramUIDefaults.put("CheckBoxMenuItem:MenuItemAccelerator.contentMargins", new InsetsUIResource(0, 0, 0, 0));
/*  612 */     addColor(paramUIDefaults, "CheckBoxMenuItem:MenuItemAccelerator[MouseOver].textForeground", 255, 255, 255, 255);
/*      */ 
/*  615 */     paramUIDefaults.put("Menu.contentMargins", new InsetsUIResource(1, 12, 2, 5));
/*  616 */     paramUIDefaults.put("Menu.textIconGap", new Integer(5));
/*  617 */     addColor(paramUIDefaults, "Menu[Disabled].textForeground", "nimbusDisabledText", 0.0F, 0.0F, 0.0F, 0);
/*  618 */     addColor(paramUIDefaults, "Menu[Enabled].textForeground", 35, 35, 36, 255);
/*  619 */     addColor(paramUIDefaults, "Menu[Enabled+Selected].textForeground", 255, 255, 255, 255);
/*  620 */     paramUIDefaults.put("Menu[Enabled+Selected].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.MenuPainter", 3, new Insets(0, 0, 0, 0), new Dimension(100, 30), false, AbstractRegionPainter.PaintContext.CacheMode.NO_CACHING, 1.0D, 1.0D));
/*  621 */     paramUIDefaults.put("Menu[Disabled].arrowIconPainter", new LazyPainter("javax.swing.plaf.nimbus.MenuPainter", 4, new Insets(5, 5, 5, 5), new Dimension(9, 10), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0D, 1.0D));
/*  622 */     paramUIDefaults.put("Menu[Enabled].arrowIconPainter", new LazyPainter("javax.swing.plaf.nimbus.MenuPainter", 5, new Insets(5, 5, 5, 5), new Dimension(9, 10), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0D, 1.0D));
/*  623 */     paramUIDefaults.put("Menu[Enabled+Selected].arrowIconPainter", new LazyPainter("javax.swing.plaf.nimbus.MenuPainter", 6, new Insets(1, 1, 1, 1), new Dimension(9, 10), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0D, 1.0D));
/*  624 */     paramUIDefaults.put("Menu.arrowIcon", new NimbusIcon("Menu", "arrowIconPainter", 9, 10));
/*  625 */     paramUIDefaults.put("Menu:MenuItemAccelerator.contentMargins", new InsetsUIResource(0, 0, 0, 0));
/*  626 */     addColor(paramUIDefaults, "Menu:MenuItemAccelerator[MouseOver].textForeground", 255, 255, 255, 255);
/*      */ 
/*  629 */     paramUIDefaults.put("PopupMenu.contentMargins", new InsetsUIResource(6, 1, 6, 1));
/*  630 */     paramUIDefaults.put("PopupMenu.opaque", Boolean.TRUE);
/*  631 */     paramUIDefaults.put("PopupMenu.consumeEventOnClose", Boolean.TRUE);
/*  632 */     paramUIDefaults.put("PopupMenu[Disabled].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.PopupMenuPainter", 1, new Insets(9, 0, 11, 0), new Dimension(220, 313), false, AbstractRegionPainter.PaintContext.CacheMode.NO_CACHING, 1.0D, 1.0D));
/*  633 */     paramUIDefaults.put("PopupMenu[Enabled].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.PopupMenuPainter", 2, new Insets(11, 2, 11, 2), new Dimension(220, 313), false, AbstractRegionPainter.PaintContext.CacheMode.NO_CACHING, 1.0D, 1.0D));
/*      */ 
/*  636 */     paramUIDefaults.put("PopupMenuSeparator.contentMargins", new InsetsUIResource(1, 0, 2, 0));
/*  637 */     paramUIDefaults.put("PopupMenuSeparator[Enabled].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.PopupMenuSeparatorPainter", 1, new Insets(1, 1, 1, 1), new Dimension(3, 3), true, AbstractRegionPainter.PaintContext.CacheMode.NO_CACHING, 1.0D, 1.0D));
/*      */ 
/*  640 */     paramUIDefaults.put("OptionPane.contentMargins", new InsetsUIResource(15, 15, 15, 15));
/*  641 */     paramUIDefaults.put("OptionPane.opaque", Boolean.TRUE);
/*  642 */     paramUIDefaults.put("OptionPane.buttonOrientation", new Integer(4));
/*  643 */     paramUIDefaults.put("OptionPane.messageAnchor", new Integer(17));
/*  644 */     paramUIDefaults.put("OptionPane.separatorPadding", new Integer(0));
/*  645 */     paramUIDefaults.put("OptionPane.sameSizeButtons", Boolean.FALSE);
/*  646 */     paramUIDefaults.put("OptionPane:\"OptionPane.separator\".contentMargins", new InsetsUIResource(1, 0, 0, 0));
/*  647 */     paramUIDefaults.put("OptionPane:\"OptionPane.messageArea\".contentMargins", new InsetsUIResource(0, 0, 10, 0));
/*  648 */     paramUIDefaults.put("OptionPane:\"OptionPane.messageArea\":\"OptionPane.label\".contentMargins", new InsetsUIResource(0, 10, 10, 10));
/*  649 */     paramUIDefaults.put("OptionPane:\"OptionPane.messageArea\":\"OptionPane.label\"[Enabled].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.OptionPaneMessageAreaOptionPaneLabelPainter", 1, new Insets(5, 5, 5, 5), new Dimension(100, 30), false, AbstractRegionPainter.PaintContext.CacheMode.NO_CACHING, 1.0D, 1.0D));
/*  650 */     paramUIDefaults.put("OptionPane[Enabled].errorIconPainter", new LazyPainter("javax.swing.plaf.nimbus.OptionPanePainter", 2, new Insets(0, 0, 0, 0), new Dimension(48, 48), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0D, 1.0D));
/*  651 */     paramUIDefaults.put("OptionPane.errorIcon", new NimbusIcon("OptionPane", "errorIconPainter", 48, 48));
/*  652 */     paramUIDefaults.put("OptionPane[Enabled].informationIconPainter", new LazyPainter("javax.swing.plaf.nimbus.OptionPanePainter", 3, new Insets(0, 0, 0, 0), new Dimension(48, 48), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0D, 1.0D));
/*  653 */     paramUIDefaults.put("OptionPane.informationIcon", new NimbusIcon("OptionPane", "informationIconPainter", 48, 48));
/*  654 */     paramUIDefaults.put("OptionPane[Enabled].questionIconPainter", new LazyPainter("javax.swing.plaf.nimbus.OptionPanePainter", 4, new Insets(0, 0, 0, 0), new Dimension(48, 48), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0D, 1.0D));
/*  655 */     paramUIDefaults.put("OptionPane.questionIcon", new NimbusIcon("OptionPane", "questionIconPainter", 48, 48));
/*  656 */     paramUIDefaults.put("OptionPane[Enabled].warningIconPainter", new LazyPainter("javax.swing.plaf.nimbus.OptionPanePainter", 5, new Insets(0, 0, 0, 0), new Dimension(48, 48), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0D, 1.0D));
/*  657 */     paramUIDefaults.put("OptionPane.warningIcon", new NimbusIcon("OptionPane", "warningIconPainter", 48, 48));
/*      */ 
/*  660 */     paramUIDefaults.put("Panel.contentMargins", new InsetsUIResource(0, 0, 0, 0));
/*  661 */     paramUIDefaults.put("Panel.opaque", Boolean.TRUE);
/*      */ 
/*  664 */     paramUIDefaults.put("ProgressBar.contentMargins", new InsetsUIResource(0, 0, 0, 0));
/*  665 */     paramUIDefaults.put("ProgressBar.States", "Enabled,Disabled,Indeterminate,Finished");
/*  666 */     paramUIDefaults.put("ProgressBar.Indeterminate", new ProgressBarIndeterminateState());
/*  667 */     paramUIDefaults.put("ProgressBar.Finished", new ProgressBarFinishedState());
/*  668 */     paramUIDefaults.put("ProgressBar.tileWhenIndeterminate", Boolean.TRUE);
/*  669 */     paramUIDefaults.put("ProgressBar.tileWidth", new Integer(27));
/*  670 */     paramUIDefaults.put("ProgressBar.paintOutsideClip", Boolean.TRUE);
/*  671 */     paramUIDefaults.put("ProgressBar.rotateText", Boolean.TRUE);
/*  672 */     paramUIDefaults.put("ProgressBar.vertictalSize", new DimensionUIResource(19, 150));
/*  673 */     paramUIDefaults.put("ProgressBar.horizontalSize", new DimensionUIResource(150, 19));
/*  674 */     paramUIDefaults.put("ProgressBar.cycleTime", new Integer(250));
/*  675 */     paramUIDefaults.put("ProgressBar[Enabled].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ProgressBarPainter", 1, new Insets(5, 5, 5, 5), new Dimension(29, 19), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0D, 1.0D));
/*  676 */     addColor(paramUIDefaults, "ProgressBar[Disabled].textForeground", "nimbusDisabledText", 0.0F, 0.0F, 0.0F, 0);
/*  677 */     paramUIDefaults.put("ProgressBar[Disabled].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ProgressBarPainter", 2, new Insets(5, 5, 5, 5), new Dimension(29, 19), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0D, 1.0D));
/*  678 */     paramUIDefaults.put("ProgressBar[Enabled].foregroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ProgressBarPainter", 3, new Insets(5, 5, 5, 5), new Dimension(27, 19), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, (1.0D / 0.0D), 2.0D));
/*  679 */     paramUIDefaults.put("ProgressBar[Enabled+Finished].foregroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ProgressBarPainter", 4, new Insets(5, 5, 5, 5), new Dimension(27, 19), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, (1.0D / 0.0D), 2.0D));
/*  680 */     paramUIDefaults.put("ProgressBar[Enabled+Indeterminate].progressPadding", new Integer(3));
/*  681 */     paramUIDefaults.put("ProgressBar[Enabled+Indeterminate].foregroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ProgressBarPainter", 5, new Insets(5, 5, 5, 5), new Dimension(30, 13), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, (1.0D / 0.0D), 2.0D));
/*  682 */     paramUIDefaults.put("ProgressBar[Disabled].foregroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ProgressBarPainter", 6, new Insets(5, 5, 5, 5), new Dimension(27, 19), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, (1.0D / 0.0D), 2.0D));
/*  683 */     paramUIDefaults.put("ProgressBar[Disabled+Finished].foregroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ProgressBarPainter", 7, new Insets(5, 5, 5, 5), new Dimension(27, 19), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, (1.0D / 0.0D), 2.0D));
/*  684 */     paramUIDefaults.put("ProgressBar[Disabled+Indeterminate].progressPadding", new Integer(3));
/*  685 */     paramUIDefaults.put("ProgressBar[Disabled+Indeterminate].foregroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ProgressBarPainter", 8, new Insets(5, 5, 5, 5), new Dimension(30, 13), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, (1.0D / 0.0D), 2.0D));
/*      */ 
/*  688 */     paramUIDefaults.put("Separator.contentMargins", new InsetsUIResource(0, 0, 0, 0));
/*  689 */     paramUIDefaults.put("Separator[Enabled].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.SeparatorPainter", 1, new Insets(0, 40, 0, 40), new Dimension(100, 3), true, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0D, 1.0D));
/*      */ 
/*  692 */     paramUIDefaults.put("ScrollBar.contentMargins", new InsetsUIResource(0, 0, 0, 0));
/*  693 */     paramUIDefaults.put("ScrollBar.opaque", Boolean.TRUE);
/*  694 */     paramUIDefaults.put("ScrollBar.incrementButtonGap", new Integer(-8));
/*  695 */     paramUIDefaults.put("ScrollBar.decrementButtonGap", new Integer(-8));
/*  696 */     paramUIDefaults.put("ScrollBar.thumbHeight", new Integer(15));
/*  697 */     paramUIDefaults.put("ScrollBar.minimumThumbSize", new DimensionUIResource(29, 29));
/*  698 */     paramUIDefaults.put("ScrollBar.maximumThumbSize", new DimensionUIResource(1000, 1000));
/*  699 */     paramUIDefaults.put("ScrollBar:\"ScrollBar.button\".contentMargins", new InsetsUIResource(0, 0, 0, 0));
/*  700 */     paramUIDefaults.put("ScrollBar:\"ScrollBar.button\".size", new Integer(25));
/*  701 */     paramUIDefaults.put("ScrollBar:\"ScrollBar.button\"[Enabled].foregroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ScrollBarButtonPainter", 1, new Insets(1, 1, 1, 1), new Dimension(25, 15), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0D, 1.0D));
/*  702 */     paramUIDefaults.put("ScrollBar:\"ScrollBar.button\"[Disabled].foregroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ScrollBarButtonPainter", 2, new Insets(1, 1, 1, 1), new Dimension(25, 15), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0D, 1.0D));
/*  703 */     paramUIDefaults.put("ScrollBar:\"ScrollBar.button\"[MouseOver].foregroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ScrollBarButtonPainter", 3, new Insets(1, 1, 1, 1), new Dimension(25, 15), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0D, 1.0D));
/*  704 */     paramUIDefaults.put("ScrollBar:\"ScrollBar.button\"[Pressed].foregroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ScrollBarButtonPainter", 4, new Insets(1, 1, 1, 1), new Dimension(25, 15), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0D, 1.0D));
/*  705 */     paramUIDefaults.put("ScrollBar:ScrollBarThumb.contentMargins", new InsetsUIResource(0, 0, 0, 0));
/*  706 */     paramUIDefaults.put("ScrollBar:ScrollBarThumb[Enabled].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ScrollBarThumbPainter", 2, new Insets(0, 15, 0, 15), new Dimension(38, 15), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, (1.0D / 0.0D), 2.0D));
/*  707 */     paramUIDefaults.put("ScrollBar:ScrollBarThumb[MouseOver].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ScrollBarThumbPainter", 4, new Insets(0, 15, 0, 15), new Dimension(38, 15), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, (1.0D / 0.0D), 2.0D));
/*  708 */     paramUIDefaults.put("ScrollBar:ScrollBarThumb[Pressed].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ScrollBarThumbPainter", 5, new Insets(0, 15, 0, 15), new Dimension(38, 15), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, (1.0D / 0.0D), 2.0D));
/*  709 */     paramUIDefaults.put("ScrollBar:ScrollBarTrack.contentMargins", new InsetsUIResource(0, 0, 0, 0));
/*  710 */     paramUIDefaults.put("ScrollBar:ScrollBarTrack[Disabled].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ScrollBarTrackPainter", 1, new Insets(5, 5, 5, 5), new Dimension(18, 15), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, (1.0D / 0.0D), 2.0D));
/*  711 */     paramUIDefaults.put("ScrollBar:ScrollBarTrack[Enabled].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ScrollBarTrackPainter", 2, new Insets(5, 10, 5, 9), new Dimension(34, 15), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0D, 1.0D));
/*      */ 
/*  714 */     paramUIDefaults.put("ScrollPane.contentMargins", new InsetsUIResource(3, 3, 3, 3));
/*  715 */     paramUIDefaults.put("ScrollPane.useChildTextComponentFocus", Boolean.TRUE);
/*  716 */     paramUIDefaults.put("ScrollPane[Enabled+Focused].borderPainter", new LazyPainter("javax.swing.plaf.nimbus.ScrollPanePainter", 2, new Insets(5, 5, 5, 5), new Dimension(122, 24), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, (1.0D / 0.0D), (1.0D / 0.0D)));
/*  717 */     paramUIDefaults.put("ScrollPane[Enabled].borderPainter", new LazyPainter("javax.swing.plaf.nimbus.ScrollPanePainter", 3, new Insets(5, 5, 5, 5), new Dimension(122, 24), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, (1.0D / 0.0D), (1.0D / 0.0D)));
/*      */ 
/*  720 */     paramUIDefaults.put("Viewport.contentMargins", new InsetsUIResource(0, 0, 0, 0));
/*  721 */     paramUIDefaults.put("Viewport.opaque", Boolean.TRUE);
/*      */ 
/*  724 */     paramUIDefaults.put("Slider.contentMargins", new InsetsUIResource(0, 0, 0, 0));
/*  725 */     paramUIDefaults.put("Slider.States", "Enabled,MouseOver,Pressed,Disabled,Focused,Selected,ArrowShape");
/*  726 */     paramUIDefaults.put("Slider.ArrowShape", new SliderArrowShapeState());
/*  727 */     paramUIDefaults.put("Slider.thumbWidth", new Integer(17));
/*  728 */     paramUIDefaults.put("Slider.thumbHeight", new Integer(17));
/*  729 */     paramUIDefaults.put("Slider.trackBorder", new Integer(0));
/*  730 */     paramUIDefaults.put("Slider.paintValue", Boolean.FALSE);
/*  731 */     addColor(paramUIDefaults, "Slider.tickColor", 35, 40, 48, 255);
/*  732 */     paramUIDefaults.put("Slider:SliderThumb.contentMargins", new InsetsUIResource(0, 0, 0, 0));
/*  733 */     paramUIDefaults.put("Slider:SliderThumb.States", "Enabled,MouseOver,Pressed,Disabled,Focused,Selected,ArrowShape");
/*  734 */     paramUIDefaults.put("Slider:SliderThumb.ArrowShape", new SliderThumbArrowShapeState());
/*  735 */     paramUIDefaults.put("Slider:SliderThumb[Disabled].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.SliderThumbPainter", 1, new Insets(5, 5, 5, 5), new Dimension(17, 17), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0D, 1.0D));
/*  736 */     paramUIDefaults.put("Slider:SliderThumb[Enabled].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.SliderThumbPainter", 2, new Insets(5, 5, 5, 5), new Dimension(17, 17), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0D, 1.0D));
/*  737 */     paramUIDefaults.put("Slider:SliderThumb[Focused].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.SliderThumbPainter", 3, new Insets(5, 5, 5, 5), new Dimension(17, 17), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0D, 1.0D));
/*  738 */     paramUIDefaults.put("Slider:SliderThumb[Focused+MouseOver].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.SliderThumbPainter", 4, new Insets(5, 5, 5, 5), new Dimension(17, 17), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0D, 1.0D));
/*  739 */     paramUIDefaults.put("Slider:SliderThumb[Focused+Pressed].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.SliderThumbPainter", 5, new Insets(5, 5, 5, 5), new Dimension(17, 17), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0D, 1.0D));
/*  740 */     paramUIDefaults.put("Slider:SliderThumb[MouseOver].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.SliderThumbPainter", 6, new Insets(5, 5, 5, 5), new Dimension(17, 17), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0D, 1.0D));
/*  741 */     paramUIDefaults.put("Slider:SliderThumb[Pressed].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.SliderThumbPainter", 7, new Insets(5, 5, 5, 5), new Dimension(17, 17), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0D, 1.0D));
/*  742 */     paramUIDefaults.put("Slider:SliderThumb[ArrowShape+Enabled].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.SliderThumbPainter", 8, new Insets(5, 5, 5, 5), new Dimension(17, 17), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0D, 1.0D));
/*  743 */     paramUIDefaults.put("Slider:SliderThumb[ArrowShape+Disabled].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.SliderThumbPainter", 9, new Insets(5, 5, 5, 5), new Dimension(17, 17), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0D, 1.0D));
/*  744 */     paramUIDefaults.put("Slider:SliderThumb[ArrowShape+MouseOver].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.SliderThumbPainter", 10, new Insets(5, 5, 5, 5), new Dimension(17, 17), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0D, 1.0D));
/*  745 */     paramUIDefaults.put("Slider:SliderThumb[ArrowShape+Pressed].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.SliderThumbPainter", 11, new Insets(5, 5, 5, 5), new Dimension(17, 17), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0D, 1.0D));
/*  746 */     paramUIDefaults.put("Slider:SliderThumb[ArrowShape+Focused].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.SliderThumbPainter", 12, new Insets(5, 5, 5, 5), new Dimension(17, 17), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0D, 1.0D));
/*  747 */     paramUIDefaults.put("Slider:SliderThumb[ArrowShape+Focused+MouseOver].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.SliderThumbPainter", 13, new Insets(5, 5, 5, 5), new Dimension(17, 17), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0D, 1.0D));
/*  748 */     paramUIDefaults.put("Slider:SliderThumb[ArrowShape+Focused+Pressed].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.SliderThumbPainter", 14, new Insets(5, 5, 5, 5), new Dimension(17, 17), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0D, 1.0D));
/*  749 */     paramUIDefaults.put("Slider:SliderTrack.contentMargins", new InsetsUIResource(0, 0, 0, 0));
/*  750 */     paramUIDefaults.put("Slider:SliderTrack.States", "Enabled,MouseOver,Pressed,Disabled,Focused,Selected,ArrowShape");
/*  751 */     paramUIDefaults.put("Slider:SliderTrack.ArrowShape", new SliderTrackArrowShapeState());
/*  752 */     paramUIDefaults.put("Slider:SliderTrack[Disabled].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.SliderTrackPainter", 1, new Insets(6, 5, 6, 5), new Dimension(23, 17), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, (1.0D / 0.0D), 2.0D));
/*  753 */     paramUIDefaults.put("Slider:SliderTrack[Enabled].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.SliderTrackPainter", 2, new Insets(6, 5, 6, 5), new Dimension(23, 17), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0D, 1.0D));
/*      */ 
/*  756 */     paramUIDefaults.put("Spinner.contentMargins", new InsetsUIResource(0, 0, 0, 0));
/*  757 */     paramUIDefaults.put("Spinner:\"Spinner.editor\".contentMargins", new InsetsUIResource(0, 0, 0, 0));
/*  758 */     paramUIDefaults.put("Spinner:Panel:\"Spinner.formattedTextField\".contentMargins", new InsetsUIResource(6, 6, 5, 6));
/*  759 */     addColor(paramUIDefaults, "Spinner:Panel:\"Spinner.formattedTextField\"[Disabled].textForeground", "nimbusDisabledText", 0.0F, 0.0F, 0.0F, 0);
/*  760 */     paramUIDefaults.put("Spinner:Panel:\"Spinner.formattedTextField\"[Disabled].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.SpinnerPanelSpinnerFormattedTextFieldPainter", 1, new Insets(5, 3, 3, 1), new Dimension(64, 24), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, (1.0D / 0.0D), (1.0D / 0.0D)));
/*  761 */     paramUIDefaults.put("Spinner:Panel:\"Spinner.formattedTextField\"[Enabled].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.SpinnerPanelSpinnerFormattedTextFieldPainter", 2, new Insets(5, 3, 3, 1), new Dimension(64, 24), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, (1.0D / 0.0D), (1.0D / 0.0D)));
/*  762 */     paramUIDefaults.put("Spinner:Panel:\"Spinner.formattedTextField\"[Focused].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.SpinnerPanelSpinnerFormattedTextFieldPainter", 3, new Insets(5, 3, 3, 1), new Dimension(64, 24), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, (1.0D / 0.0D), (1.0D / 0.0D)));
/*  763 */     addColor(paramUIDefaults, "Spinner:Panel:\"Spinner.formattedTextField\"[Selected].textForeground", "nimbusSelectedText", 0.0F, 0.0F, 0.0F, 0);
/*  764 */     paramUIDefaults.put("Spinner:Panel:\"Spinner.formattedTextField\"[Selected].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.SpinnerPanelSpinnerFormattedTextFieldPainter", 4, new Insets(5, 3, 3, 1), new Dimension(64, 24), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, (1.0D / 0.0D), (1.0D / 0.0D)));
/*  765 */     addColor(paramUIDefaults, "Spinner:Panel:\"Spinner.formattedTextField\"[Focused+Selected].textForeground", "nimbusSelectedText", 0.0F, 0.0F, 0.0F, 0);
/*  766 */     paramUIDefaults.put("Spinner:Panel:\"Spinner.formattedTextField\"[Focused+Selected].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.SpinnerPanelSpinnerFormattedTextFieldPainter", 5, new Insets(5, 3, 3, 1), new Dimension(64, 24), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, (1.0D / 0.0D), (1.0D / 0.0D)));
/*  767 */     paramUIDefaults.put("Spinner:\"Spinner.previousButton\".contentMargins", new InsetsUIResource(0, 0, 0, 0));
/*  768 */     paramUIDefaults.put("Spinner:\"Spinner.previousButton\".size", new Integer(20));
/*  769 */     paramUIDefaults.put("Spinner:\"Spinner.previousButton\"[Disabled].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.SpinnerPreviousButtonPainter", 1, new Insets(0, 1, 6, 7), new Dimension(20, 12), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0D, 1.0D));
/*  770 */     paramUIDefaults.put("Spinner:\"Spinner.previousButton\"[Enabled].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.SpinnerPreviousButtonPainter", 2, new Insets(0, 1, 6, 7), new Dimension(20, 12), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0D, 1.0D));
/*  771 */     paramUIDefaults.put("Spinner:\"Spinner.previousButton\"[Focused].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.SpinnerPreviousButtonPainter", 3, new Insets(0, 1, 6, 7), new Dimension(20, 12), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0D, 1.0D));
/*  772 */     paramUIDefaults.put("Spinner:\"Spinner.previousButton\"[Focused+MouseOver].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.SpinnerPreviousButtonPainter", 4, new Insets(3, 1, 6, 7), new Dimension(20, 12), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0D, 1.0D));
/*  773 */     paramUIDefaults.put("Spinner:\"Spinner.previousButton\"[Focused+Pressed].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.SpinnerPreviousButtonPainter", 5, new Insets(0, 1, 6, 7), new Dimension(20, 12), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0D, 1.0D));
/*  774 */     paramUIDefaults.put("Spinner:\"Spinner.previousButton\"[MouseOver].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.SpinnerPreviousButtonPainter", 6, new Insets(0, 1, 6, 7), new Dimension(20, 12), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0D, 1.0D));
/*  775 */     paramUIDefaults.put("Spinner:\"Spinner.previousButton\"[Pressed].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.SpinnerPreviousButtonPainter", 7, new Insets(0, 1, 6, 7), new Dimension(20, 12), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0D, 1.0D));
/*  776 */     paramUIDefaults.put("Spinner:\"Spinner.previousButton\"[Disabled].foregroundPainter", new LazyPainter("javax.swing.plaf.nimbus.SpinnerPreviousButtonPainter", 8, new Insets(3, 6, 5, 9), new Dimension(20, 12), true, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0D, 1.0D));
/*  777 */     paramUIDefaults.put("Spinner:\"Spinner.previousButton\"[Enabled].foregroundPainter", new LazyPainter("javax.swing.plaf.nimbus.SpinnerPreviousButtonPainter", 9, new Insets(3, 6, 5, 9), new Dimension(20, 12), true, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0D, 1.0D));
/*  778 */     paramUIDefaults.put("Spinner:\"Spinner.previousButton\"[Focused].foregroundPainter", new LazyPainter("javax.swing.plaf.nimbus.SpinnerPreviousButtonPainter", 10, new Insets(3, 6, 5, 9), new Dimension(20, 12), true, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0D, 1.0D));
/*  779 */     paramUIDefaults.put("Spinner:\"Spinner.previousButton\"[Focused+MouseOver].foregroundPainter", new LazyPainter("javax.swing.plaf.nimbus.SpinnerPreviousButtonPainter", 11, new Insets(3, 6, 5, 9), new Dimension(20, 12), true, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0D, 1.0D));
/*  780 */     paramUIDefaults.put("Spinner:\"Spinner.previousButton\"[Focused+Pressed].foregroundPainter", new LazyPainter("javax.swing.plaf.nimbus.SpinnerPreviousButtonPainter", 12, new Insets(3, 6, 5, 9), new Dimension(20, 12), true, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0D, 1.0D));
/*  781 */     paramUIDefaults.put("Spinner:\"Spinner.previousButton\"[MouseOver].foregroundPainter", new LazyPainter("javax.swing.plaf.nimbus.SpinnerPreviousButtonPainter", 13, new Insets(3, 6, 5, 9), new Dimension(20, 12), true, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0D, 1.0D));
/*  782 */     paramUIDefaults.put("Spinner:\"Spinner.previousButton\"[Pressed].foregroundPainter", new LazyPainter("javax.swing.plaf.nimbus.SpinnerPreviousButtonPainter", 14, new Insets(3, 6, 5, 9), new Dimension(20, 12), true, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0D, 1.0D));
/*  783 */     paramUIDefaults.put("Spinner:\"Spinner.nextButton\".contentMargins", new InsetsUIResource(0, 0, 0, 0));
/*  784 */     paramUIDefaults.put("Spinner:\"Spinner.nextButton\".size", new Integer(20));
/*  785 */     paramUIDefaults.put("Spinner:\"Spinner.nextButton\"[Disabled].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.SpinnerNextButtonPainter", 1, new Insets(7, 1, 1, 7), new Dimension(20, 12), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0D, 1.0D));
/*  786 */     paramUIDefaults.put("Spinner:\"Spinner.nextButton\"[Enabled].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.SpinnerNextButtonPainter", 2, new Insets(7, 1, 1, 7), new Dimension(20, 12), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0D, 1.0D));
/*  787 */     paramUIDefaults.put("Spinner:\"Spinner.nextButton\"[Focused].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.SpinnerNextButtonPainter", 3, new Insets(7, 1, 1, 7), new Dimension(20, 12), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0D, 1.0D));
/*  788 */     paramUIDefaults.put("Spinner:\"Spinner.nextButton\"[Focused+MouseOver].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.SpinnerNextButtonPainter", 4, new Insets(7, 1, 1, 7), new Dimension(20, 12), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0D, 1.0D));
/*  789 */     paramUIDefaults.put("Spinner:\"Spinner.nextButton\"[Focused+Pressed].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.SpinnerNextButtonPainter", 5, new Insets(7, 1, 1, 7), new Dimension(20, 12), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0D, 1.0D));
/*  790 */     paramUIDefaults.put("Spinner:\"Spinner.nextButton\"[MouseOver].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.SpinnerNextButtonPainter", 6, new Insets(7, 1, 1, 7), new Dimension(20, 12), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0D, 1.0D));
/*  791 */     paramUIDefaults.put("Spinner:\"Spinner.nextButton\"[Pressed].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.SpinnerNextButtonPainter", 7, new Insets(7, 1, 1, 7), new Dimension(20, 12), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0D, 1.0D));
/*  792 */     paramUIDefaults.put("Spinner:\"Spinner.nextButton\"[Disabled].foregroundPainter", new LazyPainter("javax.swing.plaf.nimbus.SpinnerNextButtonPainter", 8, new Insets(5, 6, 3, 9), new Dimension(20, 12), true, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0D, 1.0D));
/*  793 */     paramUIDefaults.put("Spinner:\"Spinner.nextButton\"[Enabled].foregroundPainter", new LazyPainter("javax.swing.plaf.nimbus.SpinnerNextButtonPainter", 9, new Insets(5, 6, 3, 9), new Dimension(20, 12), true, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0D, 1.0D));
/*  794 */     paramUIDefaults.put("Spinner:\"Spinner.nextButton\"[Focused].foregroundPainter", new LazyPainter("javax.swing.plaf.nimbus.SpinnerNextButtonPainter", 10, new Insets(3, 6, 3, 9), new Dimension(20, 12), true, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0D, 1.0D));
/*  795 */     paramUIDefaults.put("Spinner:\"Spinner.nextButton\"[Focused+MouseOver].foregroundPainter", new LazyPainter("javax.swing.plaf.nimbus.SpinnerNextButtonPainter", 11, new Insets(3, 6, 3, 9), new Dimension(20, 12), true, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0D, 1.0D));
/*  796 */     paramUIDefaults.put("Spinner:\"Spinner.nextButton\"[Focused+Pressed].foregroundPainter", new LazyPainter("javax.swing.plaf.nimbus.SpinnerNextButtonPainter", 12, new Insets(5, 6, 3, 9), new Dimension(20, 12), true, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0D, 1.0D));
/*  797 */     paramUIDefaults.put("Spinner:\"Spinner.nextButton\"[MouseOver].foregroundPainter", new LazyPainter("javax.swing.plaf.nimbus.SpinnerNextButtonPainter", 13, new Insets(5, 6, 3, 9), new Dimension(20, 12), true, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0D, 1.0D));
/*  798 */     paramUIDefaults.put("Spinner:\"Spinner.nextButton\"[Pressed].foregroundPainter", new LazyPainter("javax.swing.plaf.nimbus.SpinnerNextButtonPainter", 14, new Insets(5, 6, 3, 9), new Dimension(20, 12), true, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0D, 1.0D));
/*      */ 
/*  801 */     paramUIDefaults.put("SplitPane.contentMargins", new InsetsUIResource(1, 1, 1, 1));
/*  802 */     paramUIDefaults.put("SplitPane.States", "Enabled,MouseOver,Pressed,Disabled,Focused,Selected,Vertical");
/*  803 */     paramUIDefaults.put("SplitPane.Vertical", new SplitPaneVerticalState());
/*  804 */     paramUIDefaults.put("SplitPane.size", new Integer(10));
/*  805 */     paramUIDefaults.put("SplitPane.dividerSize", new Integer(10));
/*  806 */     paramUIDefaults.put("SplitPane.centerOneTouchButtons", Boolean.TRUE);
/*  807 */     paramUIDefaults.put("SplitPane.oneTouchButtonOffset", new Integer(30));
/*  808 */     paramUIDefaults.put("SplitPane.oneTouchExpandable", Boolean.FALSE);
/*  809 */     paramUIDefaults.put("SplitPane.continuousLayout", Boolean.TRUE);
/*  810 */     paramUIDefaults.put("SplitPane:SplitPaneDivider.contentMargins", new InsetsUIResource(0, 0, 0, 0));
/*  811 */     paramUIDefaults.put("SplitPane:SplitPaneDivider.States", "Enabled,MouseOver,Pressed,Disabled,Focused,Selected,Vertical");
/*  812 */     paramUIDefaults.put("SplitPane:SplitPaneDivider.Vertical", new SplitPaneDividerVerticalState());
/*  813 */     paramUIDefaults.put("SplitPane:SplitPaneDivider[Enabled].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.SplitPaneDividerPainter", 1, new Insets(3, 0, 3, 0), new Dimension(68, 10), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, (1.0D / 0.0D), (1.0D / 0.0D)));
/*  814 */     paramUIDefaults.put("SplitPane:SplitPaneDivider[Focused].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.SplitPaneDividerPainter", 2, new Insets(3, 0, 3, 0), new Dimension(68, 10), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, (1.0D / 0.0D), (1.0D / 0.0D)));
/*  815 */     paramUIDefaults.put("SplitPane:SplitPaneDivider[Enabled].foregroundPainter", new LazyPainter("javax.swing.plaf.nimbus.SplitPaneDividerPainter", 3, new Insets(0, 24, 0, 24), new Dimension(68, 10), true, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, (1.0D / 0.0D), (1.0D / 0.0D)));
/*  816 */     paramUIDefaults.put("SplitPane:SplitPaneDivider[Enabled+Vertical].foregroundPainter", new LazyPainter("javax.swing.plaf.nimbus.SplitPaneDividerPainter", 4, new Insets(5, 0, 5, 0), new Dimension(10, 38), true, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, (1.0D / 0.0D), (1.0D / 0.0D)));
/*      */ 
/*  819 */     paramUIDefaults.put("TabbedPane.contentMargins", new InsetsUIResource(0, 0, 0, 0));
/*  820 */     paramUIDefaults.put("TabbedPane.tabAreaStatesMatchSelectedTab", Boolean.TRUE);
/*  821 */     paramUIDefaults.put("TabbedPane.nudgeSelectedLabel", Boolean.FALSE);
/*  822 */     paramUIDefaults.put("TabbedPane.tabRunOverlay", new Integer(2));
/*  823 */     paramUIDefaults.put("TabbedPane.tabOverlap", new Integer(-1));
/*  824 */     paramUIDefaults.put("TabbedPane.extendTabsToBase", Boolean.TRUE);
/*  825 */     paramUIDefaults.put("TabbedPane.useBasicArrows", Boolean.TRUE);
/*  826 */     addColor(paramUIDefaults, "TabbedPane.shadow", "nimbusDisabledText", 0.0F, 0.0F, 0.0F, 0);
/*  827 */     addColor(paramUIDefaults, "TabbedPane.darkShadow", "text", 0.0F, 0.0F, 0.0F, 0);
/*  828 */     addColor(paramUIDefaults, "TabbedPane.highlight", "nimbusLightBackground", 0.0F, 0.0F, 0.0F, 0);
/*  829 */     paramUIDefaults.put("TabbedPane:TabbedPaneTab.contentMargins", new InsetsUIResource(2, 8, 3, 8));
/*  830 */     paramUIDefaults.put("TabbedPane:TabbedPaneTab[Enabled].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.TabbedPaneTabPainter", 1, new Insets(7, 7, 1, 7), new Dimension(44, 20), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, (1.0D / 0.0D), (1.0D / 0.0D)));
/*  831 */     paramUIDefaults.put("TabbedPane:TabbedPaneTab[Enabled+MouseOver].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.TabbedPaneTabPainter", 2, new Insets(7, 7, 1, 7), new Dimension(44, 20), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, (1.0D / 0.0D), (1.0D / 0.0D)));
/*  832 */     paramUIDefaults.put("TabbedPane:TabbedPaneTab[Enabled+Pressed].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.TabbedPaneTabPainter", 3, new Insets(7, 6, 1, 7), new Dimension(44, 20), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, (1.0D / 0.0D), (1.0D / 0.0D)));
/*  833 */     addColor(paramUIDefaults, "TabbedPane:TabbedPaneTab[Disabled].textForeground", "nimbusDisabledText", 0.0F, 0.0F, 0.0F, 0);
/*  834 */     paramUIDefaults.put("TabbedPane:TabbedPaneTab[Disabled].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.TabbedPaneTabPainter", 4, new Insets(6, 7, 1, 7), new Dimension(44, 20), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, (1.0D / 0.0D), (1.0D / 0.0D)));
/*  835 */     paramUIDefaults.put("TabbedPane:TabbedPaneTab[Disabled+Selected].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.TabbedPaneTabPainter", 5, new Insets(7, 7, 0, 7), new Dimension(44, 20), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, (1.0D / 0.0D), (1.0D / 0.0D)));
/*  836 */     paramUIDefaults.put("TabbedPane:TabbedPaneTab[Selected].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.TabbedPaneTabPainter", 6, new Insets(7, 7, 0, 7), new Dimension(44, 20), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, (1.0D / 0.0D), (1.0D / 0.0D)));
/*  837 */     paramUIDefaults.put("TabbedPane:TabbedPaneTab[MouseOver+Selected].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.TabbedPaneTabPainter", 7, new Insets(7, 9, 0, 9), new Dimension(44, 20), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, (1.0D / 0.0D), (1.0D / 0.0D)));
/*  838 */     addColor(paramUIDefaults, "TabbedPane:TabbedPaneTab[Pressed+Selected].textForeground", 255, 255, 255, 255);
/*  839 */     paramUIDefaults.put("TabbedPane:TabbedPaneTab[Pressed+Selected].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.TabbedPaneTabPainter", 8, new Insets(7, 9, 0, 9), new Dimension(44, 20), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, (1.0D / 0.0D), (1.0D / 0.0D)));
/*  840 */     paramUIDefaults.put("TabbedPane:TabbedPaneTab[Focused+Selected].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.TabbedPaneTabPainter", 9, new Insets(7, 7, 3, 7), new Dimension(44, 20), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, (1.0D / 0.0D), (1.0D / 0.0D)));
/*  841 */     paramUIDefaults.put("TabbedPane:TabbedPaneTab[Focused+MouseOver+Selected].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.TabbedPaneTabPainter", 10, new Insets(7, 9, 3, 9), new Dimension(44, 20), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, (1.0D / 0.0D), (1.0D / 0.0D)));
/*  842 */     addColor(paramUIDefaults, "TabbedPane:TabbedPaneTab[Focused+Pressed+Selected].textForeground", 255, 255, 255, 255);
/*  843 */     paramUIDefaults.put("TabbedPane:TabbedPaneTab[Focused+Pressed+Selected].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.TabbedPaneTabPainter", 11, new Insets(7, 9, 3, 9), new Dimension(44, 20), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, (1.0D / 0.0D), (1.0D / 0.0D)));
/*  844 */     paramUIDefaults.put("TabbedPane:TabbedPaneTabArea.contentMargins", new InsetsUIResource(3, 10, 4, 10));
/*  845 */     paramUIDefaults.put("TabbedPane:TabbedPaneTabArea[Enabled].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.TabbedPaneTabAreaPainter", 1, new Insets(0, 5, 6, 5), new Dimension(5, 24), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, (1.0D / 0.0D), (1.0D / 0.0D)));
/*  846 */     paramUIDefaults.put("TabbedPane:TabbedPaneTabArea[Disabled].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.TabbedPaneTabAreaPainter", 2, new Insets(0, 5, 6, 5), new Dimension(5, 24), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, (1.0D / 0.0D), (1.0D / 0.0D)));
/*  847 */     paramUIDefaults.put("TabbedPane:TabbedPaneTabArea[Enabled+MouseOver].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.TabbedPaneTabAreaPainter", 3, new Insets(0, 5, 6, 5), new Dimension(5, 24), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, (1.0D / 0.0D), (1.0D / 0.0D)));
/*  848 */     paramUIDefaults.put("TabbedPane:TabbedPaneTabArea[Enabled+Pressed].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.TabbedPaneTabAreaPainter", 4, new Insets(0, 5, 6, 5), new Dimension(5, 24), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, (1.0D / 0.0D), (1.0D / 0.0D)));
/*  849 */     paramUIDefaults.put("TabbedPane:TabbedPaneContent.contentMargins", new InsetsUIResource(0, 0, 0, 0));
/*      */ 
/*  852 */     paramUIDefaults.put("Table.contentMargins", new InsetsUIResource(0, 0, 0, 0));
/*  853 */     paramUIDefaults.put("Table.opaque", Boolean.TRUE);
/*  854 */     addColor(paramUIDefaults, "Table.textForeground", 35, 35, 36, 255);
/*  855 */     addColor(paramUIDefaults, "Table.background", "nimbusLightBackground", 0.0F, 0.0F, 0.0F, 0);
/*  856 */     paramUIDefaults.put("Table.showGrid", Boolean.FALSE);
/*  857 */     paramUIDefaults.put("Table.intercellSpacing", new DimensionUIResource(0, 0));
/*  858 */     addColor(paramUIDefaults, "Table.alternateRowColor", "nimbusLightBackground", 0.0F, 0.0F, -0.05098039F, 0, false);
/*  859 */     paramUIDefaults.put("Table.rendererUseTableColors", Boolean.TRUE);
/*  860 */     paramUIDefaults.put("Table.rendererUseUIBorder", Boolean.TRUE);
/*  861 */     paramUIDefaults.put("Table.cellNoFocusBorder", new BorderUIResource(BorderFactory.createEmptyBorder(2, 5, 2, 5)));
/*  862 */     paramUIDefaults.put("Table.focusCellHighlightBorder", new BorderUIResource(new PainterBorder("Tree:TreeCell[Enabled+Focused].backgroundPainter", new Insets(2, 5, 2, 5))));
/*  863 */     addColor(paramUIDefaults, "Table.dropLineColor", "nimbusFocus", 0.0F, 0.0F, 0.0F, 0);
/*  864 */     addColor(paramUIDefaults, "Table.dropLineShortColor", "nimbusOrange", 0.0F, 0.0F, 0.0F, 0);
/*  865 */     addColor(paramUIDefaults, "Table[Enabled+Selected].textForeground", "nimbusLightBackground", 0.0F, 0.0F, 0.0F, 0, false);
/*  866 */     addColor(paramUIDefaults, "Table[Enabled+Selected].textBackground", "nimbusSelectionBackground", 0.0F, 0.0F, 0.0F, 0, false);
/*  867 */     addColor(paramUIDefaults, "Table[Disabled+Selected].textBackground", "nimbusSelectionBackground", 0.0F, 0.0F, 0.0F, 0, false);
/*  868 */     paramUIDefaults.put("Table:\"Table.cellRenderer\".contentMargins", new InsetsUIResource(0, 0, 0, 0));
/*  869 */     paramUIDefaults.put("Table:\"Table.cellRenderer\".opaque", Boolean.TRUE);
/*  870 */     addColor(paramUIDefaults, "Table:\"Table.cellRenderer\".background", "nimbusLightBackground", 0.0F, 0.0F, 0.0F, 0, false);
/*      */ 
/*  873 */     paramUIDefaults.put("TableHeader.contentMargins", new InsetsUIResource(0, 0, 0, 0));
/*  874 */     paramUIDefaults.put("TableHeader.opaque", Boolean.TRUE);
/*  875 */     paramUIDefaults.put("TableHeader.rightAlignSortArrow", Boolean.TRUE);
/*  876 */     paramUIDefaults.put("TableHeader[Enabled].ascendingSortIconPainter", new LazyPainter("javax.swing.plaf.nimbus.TableHeaderPainter", 1, new Insets(0, 0, 0, 2), new Dimension(7, 7), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0D, 1.0D));
/*  877 */     paramUIDefaults.put("Table.ascendingSortIcon", new NimbusIcon("TableHeader", "ascendingSortIconPainter", 7, 7));
/*  878 */     paramUIDefaults.put("TableHeader[Enabled].descendingSortIconPainter", new LazyPainter("javax.swing.plaf.nimbus.TableHeaderPainter", 2, new Insets(0, 0, 0, 0), new Dimension(7, 7), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0D, 1.0D));
/*  879 */     paramUIDefaults.put("Table.descendingSortIcon", new NimbusIcon("TableHeader", "descendingSortIconPainter", 7, 7));
/*  880 */     paramUIDefaults.put("TableHeader:\"TableHeader.renderer\".contentMargins", new InsetsUIResource(2, 5, 4, 5));
/*  881 */     paramUIDefaults.put("TableHeader:\"TableHeader.renderer\".opaque", Boolean.TRUE);
/*  882 */     paramUIDefaults.put("TableHeader:\"TableHeader.renderer\".States", "Enabled,MouseOver,Pressed,Disabled,Focused,Selected,Sorted");
/*  883 */     paramUIDefaults.put("TableHeader:\"TableHeader.renderer\".Sorted", new TableHeaderRendererSortedState());
/*  884 */     paramUIDefaults.put("TableHeader:\"TableHeader.renderer\"[Disabled].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.TableHeaderRendererPainter", 1, new Insets(5, 5, 5, 5), new Dimension(22, 20), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, (1.0D / 0.0D), (1.0D / 0.0D)));
/*  885 */     paramUIDefaults.put("TableHeader:\"TableHeader.renderer\"[Enabled].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.TableHeaderRendererPainter", 2, new Insets(5, 5, 5, 5), new Dimension(22, 20), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, (1.0D / 0.0D), (1.0D / 0.0D)));
/*  886 */     paramUIDefaults.put("TableHeader:\"TableHeader.renderer\"[Enabled+Focused].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.TableHeaderRendererPainter", 3, new Insets(5, 5, 5, 5), new Dimension(22, 20), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, (1.0D / 0.0D), (1.0D / 0.0D)));
/*  887 */     paramUIDefaults.put("TableHeader:\"TableHeader.renderer\"[MouseOver].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.TableHeaderRendererPainter", 4, new Insets(5, 5, 5, 5), new Dimension(22, 20), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, (1.0D / 0.0D), (1.0D / 0.0D)));
/*  888 */     paramUIDefaults.put("TableHeader:\"TableHeader.renderer\"[Pressed].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.TableHeaderRendererPainter", 5, new Insets(5, 5, 5, 5), new Dimension(22, 20), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, (1.0D / 0.0D), (1.0D / 0.0D)));
/*  889 */     paramUIDefaults.put("TableHeader:\"TableHeader.renderer\"[Enabled+Sorted].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.TableHeaderRendererPainter", 6, new Insets(5, 5, 5, 5), new Dimension(22, 20), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, (1.0D / 0.0D), (1.0D / 0.0D)));
/*  890 */     paramUIDefaults.put("TableHeader:\"TableHeader.renderer\"[Enabled+Focused+Sorted].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.TableHeaderRendererPainter", 7, new Insets(5, 5, 5, 5), new Dimension(22, 20), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, (1.0D / 0.0D), (1.0D / 0.0D)));
/*  891 */     paramUIDefaults.put("TableHeader:\"TableHeader.renderer\"[Disabled+Sorted].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.TableHeaderRendererPainter", 8, new Insets(5, 5, 5, 5), new Dimension(22, 20), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, (1.0D / 0.0D), (1.0D / 0.0D)));
/*      */ 
/*  894 */     paramUIDefaults.put("\"Table.editor\".contentMargins", new InsetsUIResource(3, 5, 3, 5));
/*  895 */     paramUIDefaults.put("\"Table.editor\".opaque", Boolean.TRUE);
/*  896 */     addColor(paramUIDefaults, "\"Table.editor\".background", "nimbusLightBackground", 0.0F, 0.0F, 0.0F, 0);
/*  897 */     addColor(paramUIDefaults, "\"Table.editor\"[Disabled].textForeground", "nimbusDisabledText", 0.0F, 0.0F, 0.0F, 0);
/*  898 */     paramUIDefaults.put("\"Table.editor\"[Enabled].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.TableEditorPainter", 2, new Insets(5, 5, 5, 5), new Dimension(100, 30), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, (1.0D / 0.0D), (1.0D / 0.0D)));
/*  899 */     paramUIDefaults.put("\"Table.editor\"[Enabled+Focused].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.TableEditorPainter", 3, new Insets(5, 5, 5, 5), new Dimension(100, 30), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, (1.0D / 0.0D), (1.0D / 0.0D)));
/*  900 */     addColor(paramUIDefaults, "\"Table.editor\"[Selected].textForeground", "nimbusSelectedText", 0.0F, 0.0F, 0.0F, 0);
/*      */ 
/*  903 */     paramUIDefaults.put("\"Tree.cellEditor\".contentMargins", new InsetsUIResource(2, 5, 2, 5));
/*  904 */     paramUIDefaults.put("\"Tree.cellEditor\".opaque", Boolean.TRUE);
/*  905 */     addColor(paramUIDefaults, "\"Tree.cellEditor\".background", "nimbusLightBackground", 0.0F, 0.0F, 0.0F, 0);
/*  906 */     addColor(paramUIDefaults, "\"Tree.cellEditor\"[Disabled].textForeground", "nimbusDisabledText", 0.0F, 0.0F, 0.0F, 0);
/*  907 */     paramUIDefaults.put("\"Tree.cellEditor\"[Enabled].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.TreeCellEditorPainter", 2, new Insets(5, 5, 5, 5), new Dimension(100, 30), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, (1.0D / 0.0D), (1.0D / 0.0D)));
/*  908 */     paramUIDefaults.put("\"Tree.cellEditor\"[Enabled+Focused].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.TreeCellEditorPainter", 3, new Insets(5, 5, 5, 5), new Dimension(100, 30), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, (1.0D / 0.0D), (1.0D / 0.0D)));
/*  909 */     addColor(paramUIDefaults, "\"Tree.cellEditor\"[Selected].textForeground", "nimbusSelectedText", 0.0F, 0.0F, 0.0F, 0);
/*      */ 
/*  912 */     paramUIDefaults.put("TextField.contentMargins", new InsetsUIResource(6, 6, 6, 6));
/*  913 */     addColor(paramUIDefaults, "TextField.background", "nimbusLightBackground", 0.0F, 0.0F, 0.0F, 0);
/*  914 */     addColor(paramUIDefaults, "TextField[Disabled].textForeground", "nimbusDisabledText", 0.0F, 0.0F, 0.0F, 0);
/*  915 */     paramUIDefaults.put("TextField[Disabled].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.TextFieldPainter", 1, new Insets(5, 5, 5, 5), new Dimension(122, 24), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, (1.0D / 0.0D), (1.0D / 0.0D)));
/*  916 */     paramUIDefaults.put("TextField[Enabled].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.TextFieldPainter", 2, new Insets(5, 5, 5, 5), new Dimension(122, 24), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, (1.0D / 0.0D), (1.0D / 0.0D)));
/*  917 */     addColor(paramUIDefaults, "TextField[Selected].textForeground", "nimbusSelectedText", 0.0F, 0.0F, 0.0F, 0);
/*  918 */     paramUIDefaults.put("TextField[Selected].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.TextFieldPainter", 3, new Insets(5, 5, 5, 5), new Dimension(122, 24), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, (1.0D / 0.0D), (1.0D / 0.0D)));
/*  919 */     addColor(paramUIDefaults, "TextField[Disabled].textForeground", "nimbusDisabledText", 0.0F, 0.0F, 0.0F, 0);
/*  920 */     paramUIDefaults.put("TextField[Disabled].borderPainter", new LazyPainter("javax.swing.plaf.nimbus.TextFieldPainter", 4, new Insets(5, 3, 3, 3), new Dimension(122, 24), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, (1.0D / 0.0D), (1.0D / 0.0D)));
/*  921 */     paramUIDefaults.put("TextField[Focused].borderPainter", new LazyPainter("javax.swing.plaf.nimbus.TextFieldPainter", 5, new Insets(5, 5, 5, 5), new Dimension(122, 24), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, (1.0D / 0.0D), (1.0D / 0.0D)));
/*  922 */     paramUIDefaults.put("TextField[Enabled].borderPainter", new LazyPainter("javax.swing.plaf.nimbus.TextFieldPainter", 6, new Insets(5, 5, 5, 5), new Dimension(122, 24), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, (1.0D / 0.0D), (1.0D / 0.0D)));
/*      */ 
/*  925 */     paramUIDefaults.put("FormattedTextField.contentMargins", new InsetsUIResource(6, 6, 6, 6));
/*  926 */     addColor(paramUIDefaults, "FormattedTextField[Disabled].textForeground", "nimbusDisabledText", 0.0F, 0.0F, 0.0F, 0);
/*  927 */     paramUIDefaults.put("FormattedTextField[Disabled].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.FormattedTextFieldPainter", 1, new Insets(5, 5, 5, 5), new Dimension(122, 24), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, (1.0D / 0.0D), (1.0D / 0.0D)));
/*  928 */     paramUIDefaults.put("FormattedTextField[Enabled].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.FormattedTextFieldPainter", 2, new Insets(5, 5, 5, 5), new Dimension(122, 24), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, (1.0D / 0.0D), (1.0D / 0.0D)));
/*  929 */     addColor(paramUIDefaults, "FormattedTextField[Selected].textForeground", "nimbusSelectedText", 0.0F, 0.0F, 0.0F, 0);
/*  930 */     paramUIDefaults.put("FormattedTextField[Selected].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.FormattedTextFieldPainter", 3, new Insets(5, 5, 5, 5), new Dimension(122, 24), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, (1.0D / 0.0D), (1.0D / 0.0D)));
/*  931 */     addColor(paramUIDefaults, "FormattedTextField[Disabled].textForeground", "nimbusDisabledText", 0.0F, 0.0F, 0.0F, 0);
/*  932 */     paramUIDefaults.put("FormattedTextField[Disabled].borderPainter", new LazyPainter("javax.swing.plaf.nimbus.FormattedTextFieldPainter", 4, new Insets(5, 3, 3, 3), new Dimension(122, 24), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, (1.0D / 0.0D), (1.0D / 0.0D)));
/*  933 */     paramUIDefaults.put("FormattedTextField[Focused].borderPainter", new LazyPainter("javax.swing.plaf.nimbus.FormattedTextFieldPainter", 5, new Insets(5, 5, 5, 5), new Dimension(122, 24), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, (1.0D / 0.0D), (1.0D / 0.0D)));
/*  934 */     paramUIDefaults.put("FormattedTextField[Enabled].borderPainter", new LazyPainter("javax.swing.plaf.nimbus.FormattedTextFieldPainter", 6, new Insets(5, 5, 5, 5), new Dimension(122, 24), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, (1.0D / 0.0D), (1.0D / 0.0D)));
/*      */ 
/*  937 */     paramUIDefaults.put("PasswordField.contentMargins", new InsetsUIResource(6, 6, 6, 6));
/*  938 */     addColor(paramUIDefaults, "PasswordField[Disabled].textForeground", "nimbusDisabledText", 0.0F, 0.0F, 0.0F, 0);
/*  939 */     paramUIDefaults.put("PasswordField[Disabled].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.PasswordFieldPainter", 1, new Insets(5, 5, 5, 5), new Dimension(122, 24), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, (1.0D / 0.0D), (1.0D / 0.0D)));
/*  940 */     paramUIDefaults.put("PasswordField[Enabled].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.PasswordFieldPainter", 2, new Insets(5, 5, 5, 5), new Dimension(122, 24), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, (1.0D / 0.0D), (1.0D / 0.0D)));
/*  941 */     addColor(paramUIDefaults, "PasswordField[Selected].textForeground", "nimbusSelectedText", 0.0F, 0.0F, 0.0F, 0);
/*  942 */     paramUIDefaults.put("PasswordField[Selected].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.PasswordFieldPainter", 3, new Insets(5, 5, 5, 5), new Dimension(122, 24), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, (1.0D / 0.0D), (1.0D / 0.0D)));
/*  943 */     addColor(paramUIDefaults, "PasswordField[Disabled].textForeground", "nimbusDisabledText", 0.0F, 0.0F, 0.0F, 0);
/*  944 */     paramUIDefaults.put("PasswordField[Disabled].borderPainter", new LazyPainter("javax.swing.plaf.nimbus.PasswordFieldPainter", 4, new Insets(5, 3, 3, 3), new Dimension(122, 24), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, (1.0D / 0.0D), (1.0D / 0.0D)));
/*  945 */     paramUIDefaults.put("PasswordField[Focused].borderPainter", new LazyPainter("javax.swing.plaf.nimbus.PasswordFieldPainter", 5, new Insets(5, 5, 5, 5), new Dimension(122, 24), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, (1.0D / 0.0D), (1.0D / 0.0D)));
/*  946 */     paramUIDefaults.put("PasswordField[Enabled].borderPainter", new LazyPainter("javax.swing.plaf.nimbus.PasswordFieldPainter", 6, new Insets(5, 5, 5, 5), new Dimension(122, 24), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, (1.0D / 0.0D), (1.0D / 0.0D)));
/*      */ 
/*  949 */     paramUIDefaults.put("TextArea.contentMargins", new InsetsUIResource(6, 6, 6, 6));
/*  950 */     paramUIDefaults.put("TextArea.States", "Enabled,MouseOver,Pressed,Selected,Disabled,Focused,NotInScrollPane");
/*  951 */     paramUIDefaults.put("TextArea.NotInScrollPane", new TextAreaNotInScrollPaneState());
/*  952 */     addColor(paramUIDefaults, "TextArea[Disabled].textForeground", "nimbusDisabledText", 0.0F, 0.0F, 0.0F, 0);
/*  953 */     paramUIDefaults.put("TextArea[Disabled].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.TextAreaPainter", 1, new Insets(5, 5, 5, 5), new Dimension(122, 24), false, AbstractRegionPainter.PaintContext.CacheMode.NO_CACHING, 1.0D, 1.0D));
/*  954 */     paramUIDefaults.put("TextArea[Enabled].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.TextAreaPainter", 2, new Insets(5, 5, 5, 5), new Dimension(122, 24), false, AbstractRegionPainter.PaintContext.CacheMode.NO_CACHING, 1.0D, 1.0D));
/*  955 */     addColor(paramUIDefaults, "TextArea[Disabled+NotInScrollPane].textForeground", "nimbusDisabledText", 0.0F, 0.0F, 0.0F, 0);
/*  956 */     paramUIDefaults.put("TextArea[Disabled+NotInScrollPane].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.TextAreaPainter", 3, new Insets(5, 5, 5, 5), new Dimension(122, 24), false, AbstractRegionPainter.PaintContext.CacheMode.NO_CACHING, 1.0D, 1.0D));
/*  957 */     paramUIDefaults.put("TextArea[Enabled+NotInScrollPane].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.TextAreaPainter", 4, new Insets(5, 5, 5, 5), new Dimension(122, 24), false, AbstractRegionPainter.PaintContext.CacheMode.NO_CACHING, 1.0D, 1.0D));
/*  958 */     addColor(paramUIDefaults, "TextArea[Selected].textForeground", "nimbusSelectedText", 0.0F, 0.0F, 0.0F, 0);
/*  959 */     paramUIDefaults.put("TextArea[Selected].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.TextAreaPainter", 5, new Insets(5, 5, 5, 5), new Dimension(122, 24), false, AbstractRegionPainter.PaintContext.CacheMode.NO_CACHING, 1.0D, 1.0D));
/*  960 */     addColor(paramUIDefaults, "TextArea[Disabled+NotInScrollPane].textForeground", "nimbusDisabledText", 0.0F, 0.0F, 0.0F, 0);
/*  961 */     paramUIDefaults.put("TextArea[Disabled+NotInScrollPane].borderPainter", new LazyPainter("javax.swing.plaf.nimbus.TextAreaPainter", 6, new Insets(5, 3, 3, 3), new Dimension(122, 24), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, (1.0D / 0.0D), (1.0D / 0.0D)));
/*  962 */     paramUIDefaults.put("TextArea[Focused+NotInScrollPane].borderPainter", new LazyPainter("javax.swing.plaf.nimbus.TextAreaPainter", 7, new Insets(5, 5, 5, 5), new Dimension(122, 24), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, (1.0D / 0.0D), (1.0D / 0.0D)));
/*  963 */     paramUIDefaults.put("TextArea[Enabled+NotInScrollPane].borderPainter", new LazyPainter("javax.swing.plaf.nimbus.TextAreaPainter", 8, new Insets(5, 5, 5, 5), new Dimension(122, 24), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, (1.0D / 0.0D), (1.0D / 0.0D)));
/*      */ 
/*  966 */     paramUIDefaults.put("TextPane.contentMargins", new InsetsUIResource(4, 6, 4, 6));
/*  967 */     paramUIDefaults.put("TextPane.opaque", Boolean.TRUE);
/*  968 */     addColor(paramUIDefaults, "TextPane[Disabled].textForeground", "nimbusDisabledText", 0.0F, 0.0F, 0.0F, 0);
/*  969 */     paramUIDefaults.put("TextPane[Disabled].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.TextPanePainter", 1, new Insets(5, 5, 5, 5), new Dimension(100, 30), false, AbstractRegionPainter.PaintContext.CacheMode.NO_CACHING, 1.0D, 1.0D));
/*  970 */     paramUIDefaults.put("TextPane[Enabled].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.TextPanePainter", 2, new Insets(5, 5, 5, 5), new Dimension(100, 30), false, AbstractRegionPainter.PaintContext.CacheMode.NO_CACHING, 1.0D, 1.0D));
/*  971 */     addColor(paramUIDefaults, "TextPane[Selected].textForeground", "nimbusSelectedText", 0.0F, 0.0F, 0.0F, 0);
/*  972 */     paramUIDefaults.put("TextPane[Selected].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.TextPanePainter", 3, new Insets(5, 5, 5, 5), new Dimension(100, 30), false, AbstractRegionPainter.PaintContext.CacheMode.NO_CACHING, 1.0D, 1.0D));
/*      */ 
/*  975 */     paramUIDefaults.put("EditorPane.contentMargins", new InsetsUIResource(4, 6, 4, 6));
/*  976 */     paramUIDefaults.put("EditorPane.opaque", Boolean.TRUE);
/*  977 */     addColor(paramUIDefaults, "EditorPane[Disabled].textForeground", "nimbusDisabledText", 0.0F, 0.0F, 0.0F, 0);
/*  978 */     paramUIDefaults.put("EditorPane[Disabled].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.EditorPanePainter", 1, new Insets(5, 5, 5, 5), new Dimension(100, 30), false, AbstractRegionPainter.PaintContext.CacheMode.NO_CACHING, 1.0D, 1.0D));
/*  979 */     paramUIDefaults.put("EditorPane[Enabled].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.EditorPanePainter", 2, new Insets(5, 5, 5, 5), new Dimension(100, 30), false, AbstractRegionPainter.PaintContext.CacheMode.NO_CACHING, 1.0D, 1.0D));
/*  980 */     addColor(paramUIDefaults, "EditorPane[Selected].textForeground", "nimbusSelectedText", 0.0F, 0.0F, 0.0F, 0);
/*  981 */     paramUIDefaults.put("EditorPane[Selected].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.EditorPanePainter", 3, new Insets(5, 5, 5, 5), new Dimension(100, 30), false, AbstractRegionPainter.PaintContext.CacheMode.NO_CACHING, 1.0D, 1.0D));
/*      */ 
/*  984 */     paramUIDefaults.put("ToolBar.contentMargins", new InsetsUIResource(2, 2, 2, 2));
/*  985 */     paramUIDefaults.put("ToolBar.opaque", Boolean.TRUE);
/*  986 */     paramUIDefaults.put("ToolBar.States", "North,East,West,South");
/*  987 */     paramUIDefaults.put("ToolBar.North", new ToolBarNorthState());
/*  988 */     paramUIDefaults.put("ToolBar.East", new ToolBarEastState());
/*  989 */     paramUIDefaults.put("ToolBar.West", new ToolBarWestState());
/*  990 */     paramUIDefaults.put("ToolBar.South", new ToolBarSouthState());
/*  991 */     paramUIDefaults.put("ToolBar[North].borderPainter", new LazyPainter("javax.swing.plaf.nimbus.ToolBarPainter", 1, new Insets(0, 0, 1, 0), new Dimension(30, 30), false, AbstractRegionPainter.PaintContext.CacheMode.NO_CACHING, 1.0D, 1.0D));
/*  992 */     paramUIDefaults.put("ToolBar[South].borderPainter", new LazyPainter("javax.swing.plaf.nimbus.ToolBarPainter", 2, new Insets(1, 0, 0, 0), new Dimension(30, 30), false, AbstractRegionPainter.PaintContext.CacheMode.NO_CACHING, 1.0D, 1.0D));
/*  993 */     paramUIDefaults.put("ToolBar[East].borderPainter", new LazyPainter("javax.swing.plaf.nimbus.ToolBarPainter", 3, new Insets(1, 0, 0, 0), new Dimension(30, 30), false, AbstractRegionPainter.PaintContext.CacheMode.NO_CACHING, 1.0D, 1.0D));
/*  994 */     paramUIDefaults.put("ToolBar[West].borderPainter", new LazyPainter("javax.swing.plaf.nimbus.ToolBarPainter", 4, new Insets(0, 0, 1, 0), new Dimension(30, 30), false, AbstractRegionPainter.PaintContext.CacheMode.NO_CACHING, 1.0D, 1.0D));
/*  995 */     paramUIDefaults.put("ToolBar[Enabled].handleIconPainter", new LazyPainter("javax.swing.plaf.nimbus.ToolBarPainter", 5, new Insets(5, 5, 5, 5), new Dimension(11, 38), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, 2.0D, (1.0D / 0.0D)));
/*  996 */     paramUIDefaults.put("ToolBar.handleIcon", new NimbusIcon("ToolBar", "handleIconPainter", 11, 38));
/*  997 */     paramUIDefaults.put("ToolBar:Button.contentMargins", new InsetsUIResource(4, 4, 4, 4));
/*  998 */     paramUIDefaults.put("ToolBar:Button[Focused].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ToolBarButtonPainter", 2, new Insets(5, 5, 5, 5), new Dimension(104, 33), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, 2.0D, (1.0D / 0.0D)));
/*  999 */     paramUIDefaults.put("ToolBar:Button[MouseOver].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ToolBarButtonPainter", 3, new Insets(5, 5, 5, 5), new Dimension(104, 33), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, 2.0D, (1.0D / 0.0D)));
/* 1000 */     paramUIDefaults.put("ToolBar:Button[Focused+MouseOver].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ToolBarButtonPainter", 4, new Insets(5, 5, 5, 5), new Dimension(104, 33), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, 2.0D, (1.0D / 0.0D)));
/* 1001 */     paramUIDefaults.put("ToolBar:Button[Pressed].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ToolBarButtonPainter", 5, new Insets(5, 5, 5, 5), new Dimension(104, 33), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, 2.0D, (1.0D / 0.0D)));
/* 1002 */     paramUIDefaults.put("ToolBar:Button[Focused+Pressed].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ToolBarButtonPainter", 6, new Insets(5, 5, 5, 5), new Dimension(104, 33), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, 2.0D, (1.0D / 0.0D)));
/* 1003 */     paramUIDefaults.put("ToolBar:ToggleButton.contentMargins", new InsetsUIResource(4, 4, 4, 4));
/* 1004 */     paramUIDefaults.put("ToolBar:ToggleButton[Focused].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ToolBarToggleButtonPainter", 2, new Insets(5, 5, 5, 5), new Dimension(104, 34), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, 2.0D, (1.0D / 0.0D)));
/* 1005 */     paramUIDefaults.put("ToolBar:ToggleButton[MouseOver].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ToolBarToggleButtonPainter", 3, new Insets(5, 5, 5, 5), new Dimension(104, 34), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, 2.0D, (1.0D / 0.0D)));
/* 1006 */     paramUIDefaults.put("ToolBar:ToggleButton[Focused+MouseOver].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ToolBarToggleButtonPainter", 4, new Insets(5, 5, 5, 5), new Dimension(104, 34), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, 2.0D, (1.0D / 0.0D)));
/* 1007 */     paramUIDefaults.put("ToolBar:ToggleButton[Pressed].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ToolBarToggleButtonPainter", 5, new Insets(5, 5, 5, 5), new Dimension(72, 25), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, 2.0D, (1.0D / 0.0D)));
/* 1008 */     paramUIDefaults.put("ToolBar:ToggleButton[Focused+Pressed].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ToolBarToggleButtonPainter", 6, new Insets(5, 5, 5, 5), new Dimension(72, 25), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, 2.0D, (1.0D / 0.0D)));
/* 1009 */     paramUIDefaults.put("ToolBar:ToggleButton[Selected].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ToolBarToggleButtonPainter", 7, new Insets(5, 5, 5, 5), new Dimension(72, 25), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, 2.0D, (1.0D / 0.0D)));
/* 1010 */     paramUIDefaults.put("ToolBar:ToggleButton[Focused+Selected].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ToolBarToggleButtonPainter", 8, new Insets(5, 5, 5, 5), new Dimension(72, 25), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, 2.0D, (1.0D / 0.0D)));
/* 1011 */     paramUIDefaults.put("ToolBar:ToggleButton[Pressed+Selected].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ToolBarToggleButtonPainter", 9, new Insets(5, 5, 5, 5), new Dimension(72, 25), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, 2.0D, (1.0D / 0.0D)));
/* 1012 */     paramUIDefaults.put("ToolBar:ToggleButton[Focused+Pressed+Selected].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ToolBarToggleButtonPainter", 10, new Insets(5, 5, 5, 5), new Dimension(72, 25), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, 2.0D, (1.0D / 0.0D)));
/* 1013 */     paramUIDefaults.put("ToolBar:ToggleButton[MouseOver+Selected].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ToolBarToggleButtonPainter", 11, new Insets(5, 5, 5, 5), new Dimension(72, 25), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, 2.0D, (1.0D / 0.0D)));
/* 1014 */     paramUIDefaults.put("ToolBar:ToggleButton[Focused+MouseOver+Selected].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ToolBarToggleButtonPainter", 12, new Insets(5, 5, 5, 5), new Dimension(72, 25), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, 2.0D, (1.0D / 0.0D)));
/* 1015 */     addColor(paramUIDefaults, "ToolBar:ToggleButton[Disabled+Selected].textForeground", "nimbusDisabledText", 0.0F, 0.0F, 0.0F, 0);
/* 1016 */     paramUIDefaults.put("ToolBar:ToggleButton[Disabled+Selected].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ToolBarToggleButtonPainter", 13, new Insets(5, 5, 5, 5), new Dimension(72, 25), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, 2.0D, (1.0D / 0.0D)));
/*      */ 
/* 1019 */     paramUIDefaults.put("ToolBarSeparator.contentMargins", new InsetsUIResource(2, 0, 3, 0));
/* 1020 */     addColor(paramUIDefaults, "ToolBarSeparator.textForeground", "nimbusBorder", 0.0F, 0.0F, 0.0F, 0);
/*      */ 
/* 1023 */     paramUIDefaults.put("ToolTip.contentMargins", new InsetsUIResource(4, 4, 4, 4));
/* 1024 */     paramUIDefaults.put("ToolTip[Enabled].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ToolTipPainter", 1, new Insets(1, 1, 1, 1), new Dimension(10, 10), false, AbstractRegionPainter.PaintContext.CacheMode.NO_CACHING, 1.0D, 1.0D));
/*      */ 
/* 1027 */     paramUIDefaults.put("Tree.contentMargins", new InsetsUIResource(0, 0, 0, 0));
/* 1028 */     paramUIDefaults.put("Tree.opaque", Boolean.TRUE);
/* 1029 */     addColor(paramUIDefaults, "Tree.textForeground", "text", 0.0F, 0.0F, 0.0F, 0, false);
/* 1030 */     addColor(paramUIDefaults, "Tree.textBackground", "nimbusLightBackground", 0.0F, 0.0F, 0.0F, 0, false);
/* 1031 */     addColor(paramUIDefaults, "Tree.background", "nimbusLightBackground", 0.0F, 0.0F, 0.0F, 0);
/* 1032 */     paramUIDefaults.put("Tree.rendererFillBackground", Boolean.FALSE);
/* 1033 */     paramUIDefaults.put("Tree.leftChildIndent", new Integer(12));
/* 1034 */     paramUIDefaults.put("Tree.rightChildIndent", new Integer(4));
/* 1035 */     paramUIDefaults.put("Tree.drawHorizontalLines", Boolean.FALSE);
/* 1036 */     paramUIDefaults.put("Tree.drawVerticalLines", Boolean.FALSE);
/* 1037 */     paramUIDefaults.put("Tree.showRootHandles", Boolean.FALSE);
/* 1038 */     paramUIDefaults.put("Tree.rendererUseTreeColors", Boolean.TRUE);
/* 1039 */     paramUIDefaults.put("Tree.repaintWholeRow", Boolean.TRUE);
/* 1040 */     paramUIDefaults.put("Tree.rowHeight", new Integer(0));
/* 1041 */     paramUIDefaults.put("Tree.rendererMargins", new InsetsUIResource(2, 0, 1, 5));
/* 1042 */     addColor(paramUIDefaults, "Tree.selectionForeground", "nimbusSelectedText", 0.0F, 0.0F, 0.0F, 0, false);
/* 1043 */     addColor(paramUIDefaults, "Tree.selectionBackground", "nimbusSelectionBackground", 0.0F, 0.0F, 0.0F, 0, false);
/* 1044 */     addColor(paramUIDefaults, "Tree.dropLineColor", "nimbusFocus", 0.0F, 0.0F, 0.0F, 0);
/* 1045 */     paramUIDefaults.put("Tree:TreeCell.contentMargins", new InsetsUIResource(0, 0, 0, 0));
/* 1046 */     addColor(paramUIDefaults, "Tree:TreeCell[Enabled].background", "nimbusLightBackground", 0.0F, 0.0F, 0.0F, 0);
/* 1047 */     addColor(paramUIDefaults, "Tree:TreeCell[Enabled+Focused].background", "nimbusLightBackground", 0.0F, 0.0F, 0.0F, 0);
/* 1048 */     paramUIDefaults.put("Tree:TreeCell[Enabled+Focused].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.TreeCellPainter", 2, new Insets(5, 5, 5, 5), new Dimension(100, 30), false, AbstractRegionPainter.PaintContext.CacheMode.NO_CACHING, 1.0D, 1.0D));
/* 1049 */     addColor(paramUIDefaults, "Tree:TreeCell[Enabled+Selected].textForeground", 255, 255, 255, 255);
/* 1050 */     paramUIDefaults.put("Tree:TreeCell[Enabled+Selected].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.TreeCellPainter", 3, new Insets(5, 5, 5, 5), new Dimension(100, 30), false, AbstractRegionPainter.PaintContext.CacheMode.NO_CACHING, 1.0D, 1.0D));
/* 1051 */     addColor(paramUIDefaults, "Tree:TreeCell[Focused+Selected].textForeground", 255, 255, 255, 255);
/* 1052 */     paramUIDefaults.put("Tree:TreeCell[Focused+Selected].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.TreeCellPainter", 4, new Insets(5, 5, 5, 5), new Dimension(100, 30), false, AbstractRegionPainter.PaintContext.CacheMode.NO_CACHING, 1.0D, 1.0D));
/* 1053 */     paramUIDefaults.put("Tree:\"Tree.cellRenderer\".contentMargins", new InsetsUIResource(0, 0, 0, 0));
/* 1054 */     addColor(paramUIDefaults, "Tree:\"Tree.cellRenderer\"[Disabled].textForeground", "nimbusDisabledText", 0.0F, 0.0F, 0.0F, 0);
/* 1055 */     paramUIDefaults.put("Tree[Enabled].leafIconPainter", new LazyPainter("javax.swing.plaf.nimbus.TreePainter", 4, new Insets(5, 5, 5, 5), new Dimension(16, 16), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0D, 1.0D));
/* 1056 */     paramUIDefaults.put("Tree.leafIcon", new NimbusIcon("Tree", "leafIconPainter", 16, 16));
/* 1057 */     paramUIDefaults.put("Tree[Enabled].closedIconPainter", new LazyPainter("javax.swing.plaf.nimbus.TreePainter", 5, new Insets(5, 5, 5, 5), new Dimension(16, 16), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0D, 1.0D));
/* 1058 */     paramUIDefaults.put("Tree.closedIcon", new NimbusIcon("Tree", "closedIconPainter", 16, 16));
/* 1059 */     paramUIDefaults.put("Tree[Enabled].openIconPainter", new LazyPainter("javax.swing.plaf.nimbus.TreePainter", 6, new Insets(5, 5, 5, 5), new Dimension(16, 16), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0D, 1.0D));
/* 1060 */     paramUIDefaults.put("Tree.openIcon", new NimbusIcon("Tree", "openIconPainter", 16, 16));
/* 1061 */     paramUIDefaults.put("Tree[Enabled].collapsedIconPainter", new LazyPainter("javax.swing.plaf.nimbus.TreePainter", 7, new Insets(5, 5, 5, 5), new Dimension(18, 7), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0D, 1.0D));
/* 1062 */     paramUIDefaults.put("Tree[Enabled+Selected].collapsedIconPainter", new LazyPainter("javax.swing.plaf.nimbus.TreePainter", 8, new Insets(5, 5, 5, 5), new Dimension(18, 7), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0D, 1.0D));
/* 1063 */     paramUIDefaults.put("Tree.collapsedIcon", new NimbusIcon("Tree", "collapsedIconPainter", 18, 7));
/* 1064 */     paramUIDefaults.put("Tree[Enabled].expandedIconPainter", new LazyPainter("javax.swing.plaf.nimbus.TreePainter", 9, new Insets(5, 5, 5, 5), new Dimension(18, 7), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0D, 1.0D));
/* 1065 */     paramUIDefaults.put("Tree[Enabled+Selected].expandedIconPainter", new LazyPainter("javax.swing.plaf.nimbus.TreePainter", 10, new Insets(5, 5, 5, 5), new Dimension(18, 7), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0D, 1.0D));
/* 1066 */     paramUIDefaults.put("Tree.expandedIcon", new NimbusIcon("Tree", "expandedIconPainter", 18, 7));
/*      */ 
/* 1069 */     paramUIDefaults.put("RootPane.contentMargins", new InsetsUIResource(0, 0, 0, 0));
/* 1070 */     paramUIDefaults.put("RootPane.opaque", Boolean.TRUE);
/* 1071 */     addColor(paramUIDefaults, "RootPane.background", "control", 0.0F, 0.0F, 0.0F, 0);
/*      */   }
/*      */ 
/*      */   void register(Region paramRegion, String paramString)
/*      */   {
/* 1093 */     if ((paramRegion == null) || (paramString == null)) {
/* 1094 */       throw new IllegalArgumentException("Neither Region nor Prefix may be null");
/*      */     }
/*      */ 
/* 1099 */     Object localObject = (List)this.m.get(paramRegion);
/* 1100 */     if (localObject == null) {
/* 1101 */       localObject = new LinkedList();
/* 1102 */       ((List)localObject).add(new LazyStyle(paramString, null));
/* 1103 */       this.m.put(paramRegion, localObject);
/*      */     }
/*      */     else
/*      */     {
/* 1107 */       for (LazyStyle localLazyStyle : (List)localObject) {
/* 1108 */         if (paramString.equals(localLazyStyle.prefix)) {
/* 1109 */           return;
/*      */         }
/*      */       }
/* 1112 */       ((List)localObject).add(new LazyStyle(paramString, null));
/*      */     }
/*      */ 
/* 1116 */     this.registeredRegions.put(paramRegion.getName(), paramRegion);
/*      */   }
/*      */ 
/*      */   SynthStyle getStyle(JComponent paramJComponent, Region paramRegion)
/*      */   {
/* 1141 */     if ((paramJComponent == null) || (paramRegion == null)) {
/* 1142 */       throw new IllegalArgumentException("Neither comp nor r may be null");
/*      */     }
/*      */ 
/* 1148 */     List localList = (List)this.m.get(paramRegion);
/* 1149 */     if ((localList == null) || (localList.size() == 0)) {
/* 1150 */       return this.defaultStyle;
/*      */     }
/*      */ 
/* 1154 */     Object localObject = null;
/* 1155 */     for (LazyStyle localLazyStyle : localList) {
/* 1156 */       if (localLazyStyle.matches(paramJComponent))
/*      */       {
/* 1164 */         if ((localObject == null) || (((LazyStyle)localObject).parts.length < localLazyStyle.parts.length) || ((((LazyStyle)localObject).parts.length == localLazyStyle.parts.length) && (((LazyStyle)localObject).simple) && (!localLazyStyle.simple)))
/*      */         {
/* 1168 */           localObject = localLazyStyle;
/*      */         }
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1174 */     return localObject == null ? this.defaultStyle : ((LazyStyle)localObject).getStyle(paramJComponent, paramRegion);
/*      */   }
/*      */ 
/*      */   public void clearOverridesCache(JComponent paramJComponent) {
/* 1178 */     this.overridesCache.remove(paramJComponent);
/*      */   }
/*      */ 
/*      */   private void addColor(UIDefaults paramUIDefaults, String paramString, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/* 1579 */     ColorUIResource localColorUIResource = new ColorUIResource(new Color(paramInt1, paramInt2, paramInt3, paramInt4));
/* 1580 */     this.colorTree.addColor(paramString, localColorUIResource);
/* 1581 */     paramUIDefaults.put(paramString, localColorUIResource);
/*      */   }
/*      */ 
/*      */   private void addColor(UIDefaults paramUIDefaults, String paramString1, String paramString2, float paramFloat1, float paramFloat2, float paramFloat3, int paramInt)
/*      */   {
/* 1586 */     addColor(paramUIDefaults, paramString1, paramString2, paramFloat1, paramFloat2, paramFloat3, paramInt, true);
/*      */   }
/*      */ 
/*      */   private void addColor(UIDefaults paramUIDefaults, String paramString1, String paramString2, float paramFloat1, float paramFloat2, float paramFloat3, int paramInt, boolean paramBoolean)
/*      */   {
/* 1592 */     DerivedColor localDerivedColor = getDerivedColor(paramString1, paramString2, paramFloat1, paramFloat2, paramFloat3, paramInt, paramBoolean);
/*      */ 
/* 1594 */     paramUIDefaults.put(paramString1, localDerivedColor);
/*      */   }
/*      */ 
/*      */   public DerivedColor getDerivedColor(String paramString, float paramFloat1, float paramFloat2, float paramFloat3, int paramInt, boolean paramBoolean)
/*      */   {
/* 1614 */     return getDerivedColor(null, paramString, paramFloat1, paramFloat2, paramFloat3, paramInt, paramBoolean);
/*      */   }
/*      */ 
/*      */   private DerivedColor getDerivedColor(String paramString1, String paramString2, float paramFloat1, float paramFloat2, float paramFloat3, int paramInt, boolean paramBoolean)
/*      */   {
/*      */     Object localObject;
/* 1623 */     if (paramBoolean) {
/* 1624 */       localObject = new DerivedColor.UIResource(paramString2, paramFloat1, paramFloat2, paramFloat3, paramInt);
/*      */     }
/*      */     else {
/* 1627 */       localObject = new DerivedColor(paramString2, paramFloat1, paramFloat2, paramFloat3, paramInt);
/*      */     }
/*      */ 
/* 1631 */     if (this.derivedColors.containsKey(localObject)) {
/* 1632 */       return (DerivedColor)this.derivedColors.get(localObject);
/*      */     }
/* 1634 */     this.derivedColors.put(localObject, localObject);
/* 1635 */     ((DerivedColor)localObject).rederiveColor();
/* 1636 */     this.colorTree.addColor(paramString1, (Color)localObject);
/* 1637 */     return localObject;
/*      */   }
/*      */   private class ColorTree implements PropertyChangeListener {
/*      */     private Node root;
/*      */     private Map<String, Node> nodes;
/*      */ 
/*      */     private ColorTree() {
/* 1645 */       this.root = new Node(null, null);
/* 1646 */       this.nodes = new HashMap();
/*      */     }
/*      */     public Color getColor(String paramString) {
/* 1649 */       return ((Node)this.nodes.get(paramString)).color;
/*      */     }
/*      */ 
/*      */     public void addColor(String paramString, Color paramColor) {
/* 1653 */       Node localNode1 = getParentNode(paramColor);
/* 1654 */       Node localNode2 = new Node(paramColor, localNode1);
/* 1655 */       localNode1.children.add(localNode2);
/* 1656 */       if (paramString != null)
/* 1657 */         this.nodes.put(paramString, localNode2);
/*      */     }
/*      */ 
/*      */     private Node getParentNode(Color paramColor)
/*      */     {
/* 1662 */       Object localObject = this.root;
/* 1663 */       if ((paramColor instanceof DerivedColor)) {
/* 1664 */         String str = ((DerivedColor)paramColor).getUiDefaultParentName();
/* 1665 */         Node localNode = (Node)this.nodes.get(str);
/* 1666 */         if (localNode != null) {
/* 1667 */           localObject = localNode;
/*      */         }
/*      */       }
/* 1670 */       return localObject;
/*      */     }
/*      */ 
/*      */     public void update() {
/* 1674 */       this.root.update();
/*      */     }
/*      */ 
/*      */     public void propertyChange(PropertyChangeEvent paramPropertyChangeEvent)
/*      */     {
/* 1679 */       String str = paramPropertyChangeEvent.getPropertyName();
/* 1680 */       Node localNode1 = (Node)this.nodes.get(str);
/* 1681 */       if (localNode1 != null)
/*      */       {
/* 1683 */         localNode1.parent.children.remove(localNode1);
/* 1684 */         Color localColor = (Color)paramPropertyChangeEvent.getNewValue();
/* 1685 */         Node localNode2 = getParentNode(localColor);
/* 1686 */         localNode1.set(localColor, localNode2);
/* 1687 */         localNode2.children.add(localNode1);
/* 1688 */         localNode1.update();
/*      */       }
/*      */     }
/*      */ 
/*      */     class Node
/*      */     {
/*      */       Color color;
/*      */       Node parent;
/* 1695 */       List<Node> children = new LinkedList();
/*      */ 
/*      */       Node(Color paramNode, Node arg3)
/*      */       {
/*      */         Node localNode;
/* 1698 */         set(paramNode, localNode);
/*      */       }
/*      */ 
/*      */       public void set(Color paramColor, Node paramNode) {
/* 1702 */         this.color = paramColor;
/* 1703 */         this.parent = paramNode;
/*      */       }
/*      */ 
/*      */       public void update() {
/* 1707 */         if ((this.color instanceof DerivedColor)) {
/* 1708 */           ((DerivedColor)this.color).rederiveColor();
/*      */         }
/* 1710 */         for (Node localNode : this.children)
/* 1711 */           localNode.update();
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private class DefaultsListener implements PropertyChangeListener
/*      */   {
/*      */     private DefaultsListener()
/*      */     {
/*      */     }
/*      */ 
/*      */     public void propertyChange(PropertyChangeEvent paramPropertyChangeEvent) {
/* 1723 */       if ("lookAndFeel".equals(paramPropertyChangeEvent.getPropertyName()))
/*      */       {
/* 1728 */         NimbusDefaults.this.colorTree.update();
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   static final class DerivedFont
/*      */     implements UIDefaults.ActiveValue
/*      */   {
/*      */     private float sizeOffset;
/*      */     private Boolean bold;
/*      */     private Boolean italic;
/*      */     private String parentKey;
/*      */ 
/*      */     public DerivedFont(String paramString, float paramFloat, Boolean paramBoolean1, Boolean paramBoolean2)
/*      */     {
/* 1228 */       if (paramString == null) {
/* 1229 */         throw new IllegalArgumentException("You must specify a key");
/*      */       }
/*      */ 
/* 1233 */       this.parentKey = paramString;
/* 1234 */       this.sizeOffset = paramFloat;
/* 1235 */       this.bold = paramBoolean1;
/* 1236 */       this.italic = paramBoolean2;
/*      */     }
/*      */ 
/*      */     public Object createValue(UIDefaults paramUIDefaults)
/*      */     {
/* 1244 */       Font localFont = paramUIDefaults.getFont(this.parentKey);
/* 1245 */       if (localFont != null)
/*      */       {
/* 1248 */         float f = Math.round(localFont.getSize2D() * this.sizeOffset);
/* 1249 */         int i = localFont.getStyle();
/* 1250 */         if (this.bold != null) {
/* 1251 */           if (this.bold.booleanValue())
/* 1252 */             i |= 1;
/*      */           else {
/* 1254 */             i &= -2;
/*      */           }
/*      */         }
/* 1257 */         if (this.italic != null) {
/* 1258 */           if (this.italic.booleanValue())
/* 1259 */             i |= 2;
/*      */           else {
/* 1261 */             i &= -3;
/*      */           }
/*      */         }
/* 1264 */         return localFont.deriveFont(i, f);
/*      */       }
/* 1266 */       return null;
/*      */     }
/*      */   }
/*      */ 
/*      */   private static final class LazyPainter
/*      */     implements UIDefaults.LazyValue
/*      */   {
/*      */     private int which;
/*      */     private AbstractRegionPainter.PaintContext ctx;
/*      */     private String className;
/*      */ 
/*      */     LazyPainter(String paramString, int paramInt, Insets paramInsets, Dimension paramDimension, boolean paramBoolean)
/*      */     {
/* 1288 */       if (paramString == null) {
/* 1289 */         throw new IllegalArgumentException("The className must be specified");
/*      */       }
/*      */ 
/* 1293 */       this.className = paramString;
/* 1294 */       this.which = paramInt;
/* 1295 */       this.ctx = new AbstractRegionPainter.PaintContext(paramInsets, paramDimension, paramBoolean);
/*      */     }
/*      */ 
/*      */     LazyPainter(String paramString, int paramInt, Insets paramInsets, Dimension paramDimension, boolean paramBoolean, AbstractRegionPainter.PaintContext.CacheMode paramCacheMode, double paramDouble1, double paramDouble2)
/*      */     {
/* 1303 */       if (paramString == null) {
/* 1304 */         throw new IllegalArgumentException("The className must be specified");
/*      */       }
/*      */ 
/* 1308 */       this.className = paramString;
/* 1309 */       this.which = paramInt;
/* 1310 */       this.ctx = new AbstractRegionPainter.PaintContext(paramInsets, paramDimension, paramBoolean, paramCacheMode, paramDouble1, paramDouble2);
/*      */     }
/*      */ 
/*      */     public Object createValue(UIDefaults paramUIDefaults)
/*      */     {
/*      */       try
/*      */       {
/*      */         Object localObject;
/* 1320 */         if ((paramUIDefaults == null) || (!((localObject = paramUIDefaults.get("ClassLoader")) instanceof ClassLoader)))
/*      */         {
/* 1322 */           localObject = Thread.currentThread().getContextClassLoader();
/*      */ 
/* 1324 */           if (localObject == null)
/*      */           {
/* 1326 */             localObject = ClassLoader.getSystemClassLoader();
/*      */           }
/*      */         }
/*      */ 
/* 1330 */         Class localClass = Class.forName(this.className, true, (ClassLoader)localObject);
/* 1331 */         Constructor localConstructor = localClass.getConstructor(new Class[] { AbstractRegionPainter.PaintContext.class, Integer.TYPE });
/*      */ 
/* 1333 */         if (localConstructor == null) {
/* 1334 */           throw new NullPointerException("Failed to find the constructor for the class: " + this.className);
/*      */         }
/*      */ 
/* 1338 */         return localConstructor.newInstance(new Object[] { this.ctx, Integer.valueOf(this.which) });
/*      */       } catch (Exception localException) {
/* 1340 */         localException.printStackTrace();
/* 1341 */       }return null;
/*      */     }
/*      */   }
/*      */ 
/*      */   private final class LazyStyle
/*      */   {
/*      */     private String prefix;
/* 1370 */     private boolean simple = true;
/*      */     private Part[] parts;
/*      */     private NimbusStyle style;
/*      */ 
/*      */     private LazyStyle(String arg2)
/*      */     {
/*      */       Object localObject1;
/* 1391 */       if (localObject1 == null) {
/* 1392 */         throw new IllegalArgumentException("The prefix must not be null");
/*      */       }
/*      */ 
/* 1396 */       this.prefix = localObject1;
/*      */ 
/* 1409 */       Object localObject2 = localObject1;
/* 1410 */       if ((((String)localObject2).endsWith("cellRenderer\"")) || (((String)localObject2).endsWith("renderer\"")) || (((String)localObject2).endsWith("listRenderer\"")))
/*      */       {
/* 1413 */         localObject2 = ((String)localObject2).substring(((String)localObject2).lastIndexOf(":\"") + 1);
/*      */       }
/*      */ 
/* 1417 */       List localList = split((String)localObject2);
/* 1418 */       this.parts = new Part[localList.size()];
/* 1419 */       for (int i = 0; i < this.parts.length; i++) {
/* 1420 */         this.parts[i] = new Part((String)localList.get(i));
/* 1421 */         if (this.parts[i].named)
/* 1422 */           this.simple = false;
/*      */       }
/*      */     }
/*      */ 
/*      */     SynthStyle getStyle(JComponent paramJComponent, Region paramRegion)
/*      */     {
/* 1434 */       if (paramJComponent.getClientProperty("Nimbus.Overrides") != null) {
/* 1435 */         Object localObject1 = (Map)NimbusDefaults.this.overridesCache.get(paramJComponent);
/* 1436 */         Object localObject2 = null;
/* 1437 */         if (localObject1 == null) {
/* 1438 */           localObject1 = new HashMap();
/* 1439 */           NimbusDefaults.this.overridesCache.put(paramJComponent, localObject1);
/*      */         } else {
/* 1441 */           localObject2 = (SynthStyle)((Map)localObject1).get(paramRegion);
/*      */         }
/* 1443 */         if (localObject2 == null) {
/* 1444 */           localObject2 = new NimbusStyle(this.prefix, paramJComponent);
/* 1445 */           ((Map)localObject1).put(paramRegion, localObject2);
/*      */         }
/* 1447 */         return localObject2;
/*      */       }
/*      */ 
/* 1451 */       if (this.style == null) {
/* 1452 */         this.style = new NimbusStyle(this.prefix, null);
/*      */       }
/*      */ 
/* 1455 */       return this.style;
/*      */     }
/*      */ 
/*      */     boolean matches(JComponent paramJComponent)
/*      */     {
/* 1467 */       return matches(paramJComponent, this.parts.length - 1);
/*      */     }
/*      */ 
/*      */     private boolean matches(Component paramComponent, int paramInt) {
/* 1471 */       if (paramInt < 0) return true;
/* 1472 */       if (paramComponent == null) return false;
/*      */ 
/* 1475 */       String str = paramComponent.getName();
/* 1476 */       if ((this.parts[paramInt].named) && (this.parts[paramInt].s.equals(str)))
/*      */       {
/* 1478 */         return matches(paramComponent.getParent(), paramInt - 1);
/* 1479 */       }if (!this.parts[paramInt].named)
/*      */       {
/* 1483 */         Class localClass = this.parts[paramInt].c;
/* 1484 */         if ((localClass != null) && (localClass.isAssignableFrom(paramComponent.getClass())))
/*      */         {
/* 1486 */           return matches(paramComponent.getParent(), paramInt - 1);
/* 1487 */         }if ((localClass == null) && (NimbusDefaults.this.registeredRegions.containsKey(this.parts[paramInt].s)))
/*      */         {
/* 1489 */           Region localRegion = (Region)NimbusDefaults.this.registeredRegions.get(this.parts[paramInt].s);
/* 1490 */           Object localObject = localRegion.isSubregion() ? paramComponent : paramComponent.getParent();
/*      */ 
/* 1493 */           if ((localRegion == Region.INTERNAL_FRAME_TITLE_PANE) && (localObject != null) && ((localObject instanceof JInternalFrame.JDesktopIcon)))
/*      */           {
/* 1495 */             JInternalFrame.JDesktopIcon localJDesktopIcon = (JInternalFrame.JDesktopIcon)localObject;
/*      */ 
/* 1497 */             localObject = localJDesktopIcon.getInternalFrame();
/*      */           }
/*      */ 
/* 1500 */           return matches((Component)localObject, paramInt - 1);
/*      */         }
/*      */       }
/*      */ 
/* 1504 */       return false;
/*      */     }
/*      */ 
/*      */     private List<String> split(String paramString)
/*      */     {
/* 1515 */       ArrayList localArrayList = new ArrayList();
/* 1516 */       int i = 0;
/* 1517 */       int j = 0;
/* 1518 */       int k = 0;
/* 1519 */       for (int m = 0; m < paramString.length(); m++) {
/* 1520 */         int n = paramString.charAt(m);
/*      */ 
/* 1522 */         if (n == 91) {
/* 1523 */           i++;
/*      */         }
/* 1525 */         else if (n == 34) {
/* 1526 */           j = j == 0 ? 1 : 0;
/*      */         }
/* 1528 */         else if (n == 93) {
/* 1529 */           i--;
/* 1530 */           if (i < 0) {
/* 1531 */             throw new RuntimeException("Malformed prefix: " + paramString);
/*      */           }
/*      */ 
/*      */         }
/* 1537 */         else if ((n == 58) && (j == 0) && (i == 0))
/*      */         {
/* 1539 */           localArrayList.add(paramString.substring(k, m));
/* 1540 */           k = m + 1;
/*      */         }
/*      */       }
/* 1543 */       if ((k < paramString.length() - 1) && (j == 0) && (i == 0))
/*      */       {
/* 1545 */         localArrayList.add(paramString.substring(k));
/*      */       }
/* 1547 */       return localArrayList;
/*      */     }
/*      */ 
/*      */     private final class Part
/*      */     {
/*      */       private String s;
/*      */       private boolean named;
/*      */       private Class c;
/*      */ 
/*      */       Part(String arg2)
/*      */       {
/*      */         String str;
/* 1558 */         this.named = ((str.charAt(0) == '"') && (str.charAt(str.length() - 1) == '"'));
/* 1559 */         if (this.named) {
/* 1560 */           this.s = str.substring(1, str.length() - 1);
/*      */         } else {
/* 1562 */           this.s = str;
/*      */           try
/*      */           {
/* 1566 */             this.c = Class.forName("javax.swing.J" + str);
/*      */           } catch (Exception localException1) {
/*      */           }
/*      */           try {
/* 1570 */             this.c = Class.forName(str.replace("_", "."));
/*      */           }
/*      */           catch (Exception localException2)
/*      */           {
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private static final class PainterBorder
/*      */     implements Border, UIResource
/*      */   {
/*      */     private Insets insets;
/*      */     private Painter painter;
/*      */     private String painterKey;
/*      */ 
/*      */     PainterBorder(String paramString, Insets paramInsets)
/*      */     {
/* 1739 */       this.insets = paramInsets;
/* 1740 */       this.painterKey = paramString;
/*      */     }
/*      */ 
/*      */     public void paintBorder(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/* 1745 */       if (this.painter == null) {
/* 1746 */         this.painter = ((Painter)UIManager.get(this.painterKey));
/* 1747 */         if (this.painter == null) return;
/*      */       }
/*      */ 
/* 1750 */       paramGraphics.translate(paramInt1, paramInt2);
/* 1751 */       if ((paramGraphics instanceof Graphics2D)) {
/* 1752 */         this.painter.paint((Graphics2D)paramGraphics, paramComponent, paramInt3, paramInt4);
/*      */       } else {
/* 1754 */         BufferedImage localBufferedImage = new BufferedImage(paramInt3, paramInt4, 2);
/* 1755 */         Graphics2D localGraphics2D = localBufferedImage.createGraphics();
/* 1756 */         this.painter.paint(localGraphics2D, paramComponent, paramInt3, paramInt4);
/* 1757 */         localGraphics2D.dispose();
/* 1758 */         paramGraphics.drawImage(localBufferedImage, paramInt1, paramInt2, null);
/* 1759 */         localBufferedImage = null;
/*      */       }
/* 1761 */       paramGraphics.translate(-paramInt1, -paramInt2);
/*      */     }
/*      */ 
/*      */     public Insets getBorderInsets(Component paramComponent)
/*      */     {
/* 1766 */       return (Insets)this.insets.clone();
/*      */     }
/*      */ 
/*      */     public boolean isBorderOpaque()
/*      */     {
/* 1771 */       return false;
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.nimbus.NimbusDefaults
 * JD-Core Version:    0.6.2
 */