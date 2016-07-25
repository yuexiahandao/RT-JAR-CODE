/*     */ package javax.swing.plaf.nimbus;
/*     */ 
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.Color;
/*     */ import java.awt.Container;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.LayoutManager;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.security.AccessController;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import javax.swing.GrayFilter;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JToolBar;
/*     */ import javax.swing.UIDefaults;
/*     */ import javax.swing.UIDefaults.ActiveValue;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.plaf.BorderUIResource;
/*     */ import javax.swing.plaf.ColorUIResource;
/*     */ import javax.swing.plaf.UIResource;
/*     */ import javax.swing.plaf.synth.Region;
/*     */ import javax.swing.plaf.synth.SynthLookAndFeel;
/*     */ import javax.swing.plaf.synth.SynthStyle;
/*     */ import javax.swing.plaf.synth.SynthStyleFactory;
/*     */ import sun.security.action.GetPropertyAction;
/*     */ import sun.swing.ImageIconUIResource;
/*     */ import sun.swing.plaf.GTKKeybindings;
/*     */ import sun.swing.plaf.WindowsKeybindings;
/*     */ import sun.swing.plaf.synth.SynthIcon;
/*     */ 
/*     */ public class NimbusLookAndFeel extends SynthLookAndFeel
/*     */ {
/*  67 */   private static final String[] COMPONENT_KEYS = { "ArrowButton", "Button", "CheckBox", "CheckBoxMenuItem", "ColorChooser", "ComboBox", "DesktopPane", "DesktopIcon", "EditorPane", "FileChooser", "FormattedTextField", "InternalFrame", "InternalFrameTitlePane", "Label", "List", "Menu", "MenuBar", "MenuItem", "OptionPane", "Panel", "PasswordField", "PopupMenu", "PopupMenuSeparator", "ProgressBar", "RadioButton", "RadioButtonMenuItem", "RootPane", "ScrollBar", "ScrollBarTrack", "ScrollBarThumb", "ScrollPane", "Separator", "Slider", "SliderTrack", "SliderThumb", "Spinner", "SplitPane", "TabbedPane", "Table", "TableHeader", "TextArea", "TextField", "TextPane", "ToggleButton", "ToolBar", "ToolTip", "Tree", "Viewport" };
/*     */   private NimbusDefaults defaults;
/*     */   private UIDefaults uiDefaults;
/*  93 */   private DefaultsListener defaultsListener = new DefaultsListener(null);
/*     */ 
/* 560 */   private Map<String, Map<String, Object>> compiledDefaults = null;
/* 561 */   private boolean defaultListenerAdded = false;
/*     */ 
/*     */   public NimbusLookAndFeel()
/*     */   {
/* 100 */     this.defaults = new NimbusDefaults();
/*     */   }
/*     */ 
/*     */   public void initialize()
/*     */   {
/* 105 */     super.initialize();
/* 106 */     this.defaults.initialize();
/*     */ 
/* 108 */     setStyleFactory(new SynthStyleFactory()
/*     */     {
/*     */       public SynthStyle getStyle(JComponent paramAnonymousJComponent, Region paramAnonymousRegion) {
/* 111 */         return NimbusLookAndFeel.this.defaults.getStyle(paramAnonymousJComponent, paramAnonymousRegion);
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   public void uninitialize()
/*     */   {
/* 119 */     super.uninitialize();
/* 120 */     this.defaults.uninitialize();
/*     */ 
/* 122 */     ImageCache.getInstance().flush();
/* 123 */     UIManager.getDefaults().removePropertyChangeListener(this.defaultsListener);
/*     */   }
/*     */ 
/*     */   public UIDefaults getDefaults()
/*     */   {
/* 130 */     if (this.uiDefaults == null)
/*     */     {
/* 132 */       String str1 = getSystemProperty("os.name");
/* 133 */       int i = (str1 != null) && (str1.contains("Windows")) ? 1 : 0;
/*     */ 
/* 136 */       this.uiDefaults = super.getDefaults();
/* 137 */       this.defaults.initializeDefaults(this.uiDefaults);
/*     */ 
/* 140 */       if (i != 0)
/* 141 */         WindowsKeybindings.installKeybindings(this.uiDefaults);
/*     */       else {
/* 143 */         GTKKeybindings.installKeybindings(this.uiDefaults);
/*     */       }
/*     */ 
/* 147 */       this.uiDefaults.put("TitledBorder.titlePosition", Integer.valueOf(1));
/*     */ 
/* 149 */       this.uiDefaults.put("TitledBorder.border", new BorderUIResource(new LoweredBorder()));
/*     */ 
/* 151 */       this.uiDefaults.put("TitledBorder.titleColor", getDerivedColor("text", 0.0F, 0.0F, 0.23F, 0, true));
/*     */ 
/* 153 */       this.uiDefaults.put("TitledBorder.font", new NimbusDefaults.DerivedFont("defaultFont", 1.0F, Boolean.valueOf(true), null));
/*     */ 
/* 158 */       this.uiDefaults.put("OptionPane.isYesLast", Boolean.valueOf(i == 0));
/*     */ 
/* 161 */       this.uiDefaults.put("Table.scrollPaneCornerComponent", new UIDefaults.ActiveValue()
/*     */       {
/*     */         public Object createValue(UIDefaults paramAnonymousUIDefaults)
/*     */         {
/* 165 */           return new TableScrollPaneCorner();
/*     */         }
/*     */       });
/* 171 */       this.uiDefaults.put("ToolBarSeparator[Enabled].backgroundPainter", new ToolBarSeparatorPainter());
/*     */ 
/* 175 */       for (String str2 : COMPONENT_KEYS) {
/* 176 */         String str3 = str2 + ".foreground";
/* 177 */         if (!this.uiDefaults.containsKey(str3)) {
/* 178 */           this.uiDefaults.put(str3, new NimbusProperty(str2, "textForeground", null));
/*     */         }
/*     */ 
/* 181 */         str3 = str2 + ".background";
/* 182 */         if (!this.uiDefaults.containsKey(str3)) {
/* 183 */           this.uiDefaults.put(str3, new NimbusProperty(str2, "background", null));
/*     */         }
/*     */ 
/* 186 */         str3 = str2 + ".font";
/* 187 */         if (!this.uiDefaults.containsKey(str3)) {
/* 188 */           this.uiDefaults.put(str3, new NimbusProperty(str2, "font", null));
/*     */         }
/*     */ 
/* 191 */         str3 = str2 + ".disabledText";
/* 192 */         if (!this.uiDefaults.containsKey(str3)) {
/* 193 */           this.uiDefaults.put(str3, new NimbusProperty(str2, "Disabled", "textForeground", null));
/*     */         }
/*     */ 
/* 197 */         str3 = str2 + ".disabled";
/* 198 */         if (!this.uiDefaults.containsKey(str3)) {
/* 199 */           this.uiDefaults.put(str3, new NimbusProperty(str2, "Disabled", "background", null));
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 207 */       this.uiDefaults.put("FileView.computerIcon", new LinkProperty("FileChooser.homeFolderIcon", null));
/*     */ 
/* 209 */       this.uiDefaults.put("FileView.directoryIcon", new LinkProperty("FileChooser.directoryIcon", null));
/*     */ 
/* 211 */       this.uiDefaults.put("FileView.fileIcon", new LinkProperty("FileChooser.fileIcon", null));
/*     */ 
/* 213 */       this.uiDefaults.put("FileView.floppyDriveIcon", new LinkProperty("FileChooser.floppyDriveIcon", null));
/*     */ 
/* 215 */       this.uiDefaults.put("FileView.hardDriveIcon", new LinkProperty("FileChooser.hardDriveIcon", null));
/*     */     }
/*     */ 
/* 218 */     return this.uiDefaults;
/*     */   }
/*     */ 
/*     */   public static NimbusStyle getStyle(JComponent paramJComponent, Region paramRegion)
/*     */   {
/* 231 */     return (NimbusStyle)SynthLookAndFeel.getStyle(paramJComponent, paramRegion);
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/* 241 */     return "Nimbus";
/*     */   }
/*     */ 
/*     */   public String getID()
/*     */   {
/* 251 */     return "Nimbus";
/*     */   }
/*     */ 
/*     */   public String getDescription()
/*     */   {
/* 260 */     return "Nimbus Look and Feel";
/*     */   }
/*     */ 
/*     */   public boolean shouldUpdateStyleOnAncestorChanged()
/*     */   {
/* 268 */     return true;
/*     */   }
/*     */ 
/*     */   protected boolean shouldUpdateStyleOnEvent(PropertyChangeEvent paramPropertyChangeEvent)
/*     */   {
/* 286 */     String str = paramPropertyChangeEvent.getPropertyName();
/*     */ 
/* 289 */     if (("name" == str) || ("ancestor" == str) || ("Nimbus.Overrides" == str) || ("Nimbus.Overrides.InheritDefaults" == str) || ("JComponent.sizeVariant" == str))
/*     */     {
/* 295 */       JComponent localJComponent = (JComponent)paramPropertyChangeEvent.getSource();
/* 296 */       this.defaults.clearOverridesCache(localJComponent);
/* 297 */       return true;
/*     */     }
/*     */ 
/* 300 */     return super.shouldUpdateStyleOnEvent(paramPropertyChangeEvent);
/*     */   }
/*     */ 
/*     */   public void register(Region paramRegion, String paramString)
/*     */   {
/* 348 */     this.defaults.register(paramRegion, paramString);
/*     */   }
/*     */ 
/*     */   private String getSystemProperty(String paramString)
/*     */   {
/* 355 */     return (String)AccessController.doPrivileged(new GetPropertyAction(paramString));
/*     */   }
/*     */ 
/*     */   public Icon getDisabledIcon(JComponent paramJComponent, Icon paramIcon)
/*     */   {
/* 360 */     if ((paramIcon instanceof SynthIcon)) {
/* 361 */       SynthIcon localSynthIcon = (SynthIcon)paramIcon;
/* 362 */       BufferedImage localBufferedImage = EffectUtils.createCompatibleTranslucentImage(localSynthIcon.getIconWidth(), localSynthIcon.getIconHeight());
/*     */ 
/* 364 */       Graphics2D localGraphics2D = localBufferedImage.createGraphics();
/* 365 */       localSynthIcon.paintIcon(paramJComponent, localGraphics2D, 0, 0);
/* 366 */       localGraphics2D.dispose();
/* 367 */       return new ImageIconUIResource(GrayFilter.createDisabledImage(localBufferedImage));
/*     */     }
/* 369 */     return super.getDisabledIcon(paramJComponent, paramIcon);
/*     */   }
/*     */ 
/*     */   public Color getDerivedColor(String paramString, float paramFloat1, float paramFloat2, float paramFloat3, int paramInt, boolean paramBoolean)
/*     */   {
/* 390 */     return this.defaults.getDerivedColor(paramString, paramFloat1, paramFloat2, paramFloat3, paramInt, paramBoolean);
/*     */   }
/*     */ 
/*     */   protected final Color getDerivedColor(Color paramColor1, Color paramColor2, float paramFloat, boolean paramBoolean)
/*     */   {
/* 407 */     int i = deriveARGB(paramColor1, paramColor2, paramFloat);
/* 408 */     if (paramBoolean) {
/* 409 */       return new ColorUIResource(i);
/*     */     }
/* 411 */     return new Color(i);
/*     */   }
/*     */ 
/*     */   protected final Color getDerivedColor(Color paramColor1, Color paramColor2, float paramFloat)
/*     */   {
/* 427 */     return getDerivedColor(paramColor1, paramColor2, paramFloat, true);
/*     */   }
/*     */ 
/*     */   static Object resolveToolbarConstraint(JToolBar paramJToolBar)
/*     */   {
/* 451 */     if (paramJToolBar != null) {
/* 452 */       Container localContainer = paramJToolBar.getParent();
/* 453 */       if (localContainer != null) {
/* 454 */         LayoutManager localLayoutManager = localContainer.getLayout();
/* 455 */         if ((localLayoutManager instanceof BorderLayout)) {
/* 456 */           BorderLayout localBorderLayout = (BorderLayout)localLayoutManager;
/* 457 */           Object localObject = localBorderLayout.getConstraints(paramJToolBar);
/* 458 */           if ((localObject == "South") || (localObject == "East") || (localObject == "West")) {
/* 459 */             return localObject;
/*     */           }
/* 461 */           return "North";
/*     */         }
/*     */       }
/*     */     }
/* 465 */     return "North";
/*     */   }
/*     */ 
/*     */   static int deriveARGB(Color paramColor1, Color paramColor2, float paramFloat)
/*     */   {
/* 479 */     int i = paramColor1.getRed() + Math.round((paramColor2.getRed() - paramColor1.getRed()) * paramFloat);
/*     */ 
/* 481 */     int j = paramColor1.getGreen() + Math.round((paramColor2.getGreen() - paramColor1.getGreen()) * paramFloat);
/*     */ 
/* 483 */     int k = paramColor1.getBlue() + Math.round((paramColor2.getBlue() - paramColor1.getBlue()) * paramFloat);
/*     */ 
/* 485 */     int m = paramColor1.getAlpha() + Math.round((paramColor2.getAlpha() - paramColor1.getAlpha()) * paramFloat);
/*     */ 
/* 487 */     return (m & 0xFF) << 24 | (i & 0xFF) << 16 | (j & 0xFF) << 8 | k & 0xFF;
/*     */   }
/*     */ 
/*     */   static String parsePrefix(String paramString)
/*     */   {
/* 564 */     if (paramString == null) {
/* 565 */       return null;
/*     */     }
/* 567 */     int i = 0;
/* 568 */     for (int j = 0; j < paramString.length(); j++) {
/* 569 */       int k = paramString.charAt(j);
/* 570 */       if (k == 34)
/* 571 */         i = i == 0 ? 1 : 0;
/* 572 */       else if (((k == 91) || (k == 46)) && (i == 0)) {
/* 573 */         return paramString.substring(0, j);
/*     */       }
/*     */     }
/* 576 */     return null;
/*     */   }
/*     */ 
/*     */   Map<String, Object> getDefaultsForPrefix(String paramString) {
/* 580 */     if (this.compiledDefaults == null) {
/* 581 */       this.compiledDefaults = new HashMap();
/* 582 */       for (Map.Entry localEntry : UIManager.getDefaults().entrySet()) {
/* 583 */         if ((localEntry.getKey() instanceof String)) {
/* 584 */           addDefault((String)localEntry.getKey(), localEntry.getValue());
/*     */         }
/*     */       }
/* 587 */       if (!this.defaultListenerAdded) {
/* 588 */         UIManager.getDefaults().addPropertyChangeListener(this.defaultsListener);
/* 589 */         this.defaultListenerAdded = true;
/*     */       }
/*     */     }
/* 592 */     return (Map)this.compiledDefaults.get(paramString);
/*     */   }
/*     */ 
/*     */   private void addDefault(String paramString, Object paramObject) {
/* 596 */     if (this.compiledDefaults == null) {
/* 597 */       return;
/*     */     }
/*     */ 
/* 600 */     String str = parsePrefix(paramString);
/* 601 */     if (str != null) {
/* 602 */       Object localObject = (Map)this.compiledDefaults.get(str);
/* 603 */       if (localObject == null) {
/* 604 */         localObject = new HashMap();
/* 605 */         this.compiledDefaults.put(str, localObject);
/*     */       }
/* 607 */       ((Map)localObject).put(paramString, paramObject);
/*     */     }
/*     */   }
/*     */   private class DefaultsListener implements PropertyChangeListener {
/*     */     private DefaultsListener() {
/*     */     }
/* 613 */     public void propertyChange(PropertyChangeEvent paramPropertyChangeEvent) { String str = paramPropertyChangeEvent.getPropertyName();
/* 614 */       if ("UIDefaults".equals(str))
/* 615 */         NimbusLookAndFeel.this.compiledDefaults = null;
/*     */       else
/* 617 */         NimbusLookAndFeel.this.addDefault(str, paramPropertyChangeEvent.getNewValue());
/*     */     }
/*     */   }
/*     */ 
/*     */   private class LinkProperty
/*     */     implements UIDefaults.ActiveValue, UIResource
/*     */   {
/*     */     private String dstPropName;
/*     */ 
/*     */     private LinkProperty(String arg2)
/*     */     {
/*     */       Object localObject;
/* 500 */       this.dstPropName = localObject;
/*     */     }
/*     */ 
/*     */     public Object createValue(UIDefaults paramUIDefaults)
/*     */     {
/* 505 */       return UIManager.get(this.dstPropName);
/*     */     }
/*     */   }
/*     */ 
/*     */   private class NimbusProperty implements UIDefaults.ActiveValue, UIResource
/*     */   {
/*     */     private String prefix;
/* 515 */     private String state = null;
/*     */     private String suffix;
/*     */     private boolean isFont;
/*     */ 
/*     */     private NimbusProperty(String paramString1, String arg3) {
/* 520 */       this.prefix = paramString1;
/*     */       Object localObject;
/* 521 */       this.suffix = localObject;
/* 522 */       this.isFont = "font".equals(localObject);
/*     */     }
/*     */ 
/*     */     private NimbusProperty(String paramString1, String paramString2, String arg4) {
/* 526 */       this(paramString1, str);
/* 527 */       this.state = paramString2;
/*     */     }
/*     */ 
/*     */     public Object createValue(UIDefaults paramUIDefaults)
/*     */     {
/* 539 */       Object localObject = null;
/*     */ 
/* 541 */       if (this.state != null) {
/* 542 */         localObject = NimbusLookAndFeel.this.uiDefaults.get(this.prefix + "[" + this.state + "]." + this.suffix);
/*     */       }
/*     */ 
/* 545 */       if (localObject == null) {
/* 546 */         localObject = NimbusLookAndFeel.this.uiDefaults.get(this.prefix + "[Enabled]." + this.suffix);
/*     */       }
/*     */ 
/* 549 */       if (localObject == null) {
/* 550 */         if (this.isFont)
/* 551 */           localObject = NimbusLookAndFeel.this.uiDefaults.get("defaultFont");
/*     */         else {
/* 553 */           localObject = NimbusLookAndFeel.this.uiDefaults.get(this.suffix);
/*     */         }
/*     */       }
/* 556 */       return localObject;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.nimbus.NimbusLookAndFeel
 * JD-Core Version:    0.6.2
 */