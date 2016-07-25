/*     */ package javax.swing.plaf.synth;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.ComponentOrientation;
/*     */ import java.awt.Container;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Frame;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Insets;
/*     */ import java.awt.KeyboardFocusManager;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.Toolkit;
/*     */ import java.awt.Window;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.NotSerializableException;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.lang.ref.ReferenceQueue;
/*     */ import java.lang.ref.WeakReference;
/*     */ import java.net.URL;
/*     */ import java.security.AccessController;
/*     */ import java.text.ParseException;
/*     */ import java.util.HashMap;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JMenu;
/*     */ import javax.swing.LookAndFeel;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.UIDefaults;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.InsetsUIResource;
/*     */ import javax.swing.plaf.basic.BasicLookAndFeel;
/*     */ import sun.awt.AppContext;
/*     */ import sun.security.action.GetPropertyAction;
/*     */ import sun.swing.DefaultLookup;
/*     */ import sun.swing.SwingUtilities2;
/*     */ import sun.swing.SwingUtilities2.AATextInfo;
/*     */ import sun.swing.plaf.synth.SynthFileChooserUI;
/*     */ 
/*     */ public class SynthLookAndFeel extends BasicLookAndFeel
/*     */ {
/*  70 */   static final Insets EMPTY_UIRESOURCE_INSETS = new InsetsUIResource(0, 0, 0, 0);
/*     */ 
/*  76 */   private static final Object STYLE_FACTORY_KEY = new StringBuffer("com.sun.java.swing.plaf.gtk.StyleCache");
/*     */ 
/*  82 */   private static final Object SELECTED_UI_KEY = new StringBuilder("selectedUI");
/*     */ 
/*  87 */   private static final Object SELECTED_UI_STATE_KEY = new StringBuilder("selectedUIState");
/*     */   private static SynthStyleFactory lastFactory;
/*     */   private static AppContext lastContext;
/*     */   private SynthStyleFactory factory;
/*     */   private Map<String, Object> defaultsMap;
/*     */   private Handler _handler;
/* 813 */   private static ReferenceQueue<LookAndFeel> queue = new ReferenceQueue();
/*     */ 
/*     */   static ComponentUI getSelectedUI()
/*     */   {
/* 113 */     return (ComponentUI)AppContext.getAppContext().get(SELECTED_UI_KEY);
/*     */   }
/*     */ 
/*     */   static void setSelectedUI(ComponentUI paramComponentUI, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4)
/*     */   {
/* 127 */     int i = 0;
/*     */ 
/* 129 */     if (paramBoolean1) {
/* 130 */       i = 512;
/* 131 */       if (paramBoolean2) {
/* 132 */         i |= 256;
/*     */       }
/*     */     }
/* 135 */     else if ((paramBoolean4) && (paramBoolean3)) {
/* 136 */       i |= 3;
/*     */ 
/* 138 */       if (paramBoolean2) {
/* 139 */         i |= 256;
/*     */       }
/*     */ 
/*     */     }
/* 143 */     else if (paramBoolean3) {
/* 144 */       i |= 1;
/* 145 */       if (paramBoolean2)
/* 146 */         i |= 256;
/*     */     }
/*     */     else
/*     */     {
/* 150 */       i |= 8;
/*     */     }
/*     */ 
/* 154 */     AppContext localAppContext = AppContext.getAppContext();
/*     */ 
/* 156 */     localAppContext.put(SELECTED_UI_KEY, paramComponentUI);
/* 157 */     localAppContext.put(SELECTED_UI_STATE_KEY, Integer.valueOf(i));
/*     */   }
/*     */ 
/*     */   static int getSelectedUIState() {
/* 161 */     Integer localInteger = (Integer)AppContext.getAppContext().get(SELECTED_UI_STATE_KEY);
/*     */ 
/* 163 */     return localInteger == null ? 0 : localInteger.intValue();
/*     */   }
/*     */ 
/*     */   static void resetSelectedUI()
/*     */   {
/* 170 */     AppContext.getAppContext().remove(SELECTED_UI_KEY);
/*     */   }
/*     */ 
/*     */   public static void setStyleFactory(SynthStyleFactory paramSynthStyleFactory)
/*     */   {
/* 183 */     synchronized (SynthLookAndFeel.class) {
/* 184 */       AppContext localAppContext = AppContext.getAppContext();
/* 185 */       lastFactory = paramSynthStyleFactory;
/* 186 */       lastContext = localAppContext;
/* 187 */       localAppContext.put(STYLE_FACTORY_KEY, paramSynthStyleFactory);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static SynthStyleFactory getStyleFactory()
/*     */   {
/* 197 */     synchronized (SynthLookAndFeel.class) {
/* 198 */       AppContext localAppContext = AppContext.getAppContext();
/*     */ 
/* 200 */       if (lastContext == localAppContext) {
/* 201 */         return lastFactory;
/*     */       }
/* 203 */       lastContext = localAppContext;
/* 204 */       lastFactory = (SynthStyleFactory)localAppContext.get(STYLE_FACTORY_KEY);
/* 205 */       return lastFactory;
/*     */     }
/*     */   }
/*     */ 
/*     */   static int getComponentState(Component paramComponent)
/*     */   {
/* 216 */     if (paramComponent.isEnabled()) {
/* 217 */       if (paramComponent.isFocusOwner()) {
/* 218 */         return 257;
/*     */       }
/* 220 */       return 1;
/*     */     }
/* 222 */     return 8;
/*     */   }
/*     */ 
/*     */   public static SynthStyle getStyle(JComponent paramJComponent, Region paramRegion)
/*     */   {
/* 235 */     return getStyleFactory().getStyle(paramJComponent, paramRegion);
/*     */   }
/*     */ 
/*     */   static boolean shouldUpdateStyle(PropertyChangeEvent paramPropertyChangeEvent)
/*     */   {
/* 244 */     LookAndFeel localLookAndFeel = UIManager.getLookAndFeel();
/* 245 */     return ((localLookAndFeel instanceof SynthLookAndFeel)) && (((SynthLookAndFeel)localLookAndFeel).shouldUpdateStyleOnEvent(paramPropertyChangeEvent));
/*     */   }
/*     */ 
/*     */   static SynthStyle updateStyle(SynthContext paramSynthContext, SynthUI paramSynthUI)
/*     */   {
/* 256 */     SynthStyle localSynthStyle1 = getStyle(paramSynthContext.getComponent(), paramSynthContext.getRegion());
/*     */ 
/* 258 */     SynthStyle localSynthStyle2 = paramSynthContext.getStyle();
/*     */ 
/* 260 */     if (localSynthStyle1 != localSynthStyle2) {
/* 261 */       if (localSynthStyle2 != null) {
/* 262 */         localSynthStyle2.uninstallDefaults(paramSynthContext);
/*     */       }
/* 264 */       paramSynthContext.setStyle(localSynthStyle1);
/* 265 */       localSynthStyle1.installDefaults(paramSynthContext, paramSynthUI);
/*     */     }
/* 267 */     return localSynthStyle1;
/*     */   }
/*     */ 
/*     */   public static void updateStyles(Component paramComponent)
/*     */   {
/* 278 */     if ((paramComponent instanceof JComponent))
/*     */     {
/* 282 */       localObject1 = paramComponent.getName();
/* 283 */       paramComponent.setName(null);
/* 284 */       if (localObject1 != null) {
/* 285 */         paramComponent.setName((String)localObject1);
/*     */       }
/* 287 */       ((JComponent)paramComponent).revalidate();
/*     */     }
/* 289 */     Object localObject1 = null;
/* 290 */     if ((paramComponent instanceof JMenu)) {
/* 291 */       localObject1 = ((JMenu)paramComponent).getMenuComponents();
/*     */     }
/* 293 */     else if ((paramComponent instanceof Container)) {
/* 294 */       localObject1 = ((Container)paramComponent).getComponents();
/*     */     }
/* 296 */     if (localObject1 != null) {
/* 297 */       for (Component localComponent : localObject1) {
/* 298 */         updateStyles(localComponent);
/*     */       }
/*     */     }
/* 301 */     paramComponent.repaint();
/*     */   }
/*     */ 
/*     */   public static Region getRegion(JComponent paramJComponent)
/*     */   {
/* 311 */     return Region.getRegion(paramJComponent);
/*     */   }
/*     */ 
/*     */   static Insets getPaintingInsets(SynthContext paramSynthContext, Insets paramInsets)
/*     */   {
/* 320 */     if (paramSynthContext.isSubregion()) {
/* 321 */       paramInsets = paramSynthContext.getStyle().getInsets(paramSynthContext, paramInsets);
/*     */     }
/*     */     else {
/* 324 */       paramInsets = paramSynthContext.getComponent().getInsets(paramInsets);
/*     */     }
/* 326 */     return paramInsets;
/*     */   }
/*     */ 
/*     */   static void update(SynthContext paramSynthContext, Graphics paramGraphics)
/*     */   {
/* 335 */     paintRegion(paramSynthContext, paramGraphics, null);
/*     */   }
/*     */ 
/*     */   static void updateSubregion(SynthContext paramSynthContext, Graphics paramGraphics, Rectangle paramRectangle)
/*     */   {
/* 345 */     paintRegion(paramSynthContext, paramGraphics, paramRectangle);
/*     */   }
/*     */ 
/*     */   private static void paintRegion(SynthContext paramSynthContext, Graphics paramGraphics, Rectangle paramRectangle)
/*     */   {
/* 350 */     JComponent localJComponent = paramSynthContext.getComponent();
/* 351 */     SynthStyle localSynthStyle = paramSynthContext.getStyle();
/*     */     int i;
/*     */     int j;
/*     */     int k;
/*     */     int m;
/* 354 */     if (paramRectangle == null) {
/* 355 */       i = 0;
/* 356 */       j = 0;
/* 357 */       k = localJComponent.getWidth();
/* 358 */       m = localJComponent.getHeight();
/*     */     }
/*     */     else {
/* 361 */       i = paramRectangle.x;
/* 362 */       j = paramRectangle.y;
/* 363 */       k = paramRectangle.width;
/* 364 */       m = paramRectangle.height;
/*     */     }
/*     */ 
/* 368 */     boolean bool = paramSynthContext.isSubregion();
/* 369 */     if (((bool) && (localSynthStyle.isOpaque(paramSynthContext))) || ((!bool) && (localJComponent.isOpaque())))
/*     */     {
/* 371 */       paramGraphics.setColor(localSynthStyle.getColor(paramSynthContext, ColorType.BACKGROUND));
/* 372 */       paramGraphics.fillRect(i, j, k, m);
/*     */     }
/*     */   }
/*     */ 
/*     */   static boolean isLeftToRight(Component paramComponent) {
/* 377 */     return paramComponent.getComponentOrientation().isLeftToRight();
/*     */   }
/*     */ 
/*     */   static Object getUIOfType(ComponentUI paramComponentUI, Class paramClass)
/*     */   {
/* 385 */     if (paramClass.isInstance(paramComponentUI)) {
/* 386 */       return paramComponentUI;
/*     */     }
/* 388 */     return null;
/*     */   }
/*     */ 
/*     */   public static ComponentUI createUI(JComponent paramJComponent)
/*     */   {
/* 399 */     String str = paramJComponent.getUIClassID().intern();
/*     */ 
/* 401 */     if (str == "ButtonUI") {
/* 402 */       return SynthButtonUI.createUI(paramJComponent);
/*     */     }
/* 404 */     if (str == "CheckBoxUI") {
/* 405 */       return SynthCheckBoxUI.createUI(paramJComponent);
/*     */     }
/* 407 */     if (str == "CheckBoxMenuItemUI") {
/* 408 */       return SynthCheckBoxMenuItemUI.createUI(paramJComponent);
/*     */     }
/* 410 */     if (str == "ColorChooserUI") {
/* 411 */       return SynthColorChooserUI.createUI(paramJComponent);
/*     */     }
/* 413 */     if (str == "ComboBoxUI") {
/* 414 */       return SynthComboBoxUI.createUI(paramJComponent);
/*     */     }
/* 416 */     if (str == "DesktopPaneUI") {
/* 417 */       return SynthDesktopPaneUI.createUI(paramJComponent);
/*     */     }
/* 419 */     if (str == "DesktopIconUI") {
/* 420 */       return SynthDesktopIconUI.createUI(paramJComponent);
/*     */     }
/* 422 */     if (str == "EditorPaneUI") {
/* 423 */       return SynthEditorPaneUI.createUI(paramJComponent);
/*     */     }
/* 425 */     if (str == "FileChooserUI") {
/* 426 */       return SynthFileChooserUI.createUI(paramJComponent);
/*     */     }
/* 428 */     if (str == "FormattedTextFieldUI") {
/* 429 */       return SynthFormattedTextFieldUI.createUI(paramJComponent);
/*     */     }
/* 431 */     if (str == "InternalFrameUI") {
/* 432 */       return SynthInternalFrameUI.createUI(paramJComponent);
/*     */     }
/* 434 */     if (str == "LabelUI") {
/* 435 */       return SynthLabelUI.createUI(paramJComponent);
/*     */     }
/* 437 */     if (str == "ListUI") {
/* 438 */       return SynthListUI.createUI(paramJComponent);
/*     */     }
/* 440 */     if (str == "MenuBarUI") {
/* 441 */       return SynthMenuBarUI.createUI(paramJComponent);
/*     */     }
/* 443 */     if (str == "MenuUI") {
/* 444 */       return SynthMenuUI.createUI(paramJComponent);
/*     */     }
/* 446 */     if (str == "MenuItemUI") {
/* 447 */       return SynthMenuItemUI.createUI(paramJComponent);
/*     */     }
/* 449 */     if (str == "OptionPaneUI") {
/* 450 */       return SynthOptionPaneUI.createUI(paramJComponent);
/*     */     }
/* 452 */     if (str == "PanelUI") {
/* 453 */       return SynthPanelUI.createUI(paramJComponent);
/*     */     }
/* 455 */     if (str == "PasswordFieldUI") {
/* 456 */       return SynthPasswordFieldUI.createUI(paramJComponent);
/*     */     }
/* 458 */     if (str == "PopupMenuSeparatorUI") {
/* 459 */       return SynthSeparatorUI.createUI(paramJComponent);
/*     */     }
/* 461 */     if (str == "PopupMenuUI") {
/* 462 */       return SynthPopupMenuUI.createUI(paramJComponent);
/*     */     }
/* 464 */     if (str == "ProgressBarUI") {
/* 465 */       return SynthProgressBarUI.createUI(paramJComponent);
/*     */     }
/* 467 */     if (str == "RadioButtonUI") {
/* 468 */       return SynthRadioButtonUI.createUI(paramJComponent);
/*     */     }
/* 470 */     if (str == "RadioButtonMenuItemUI") {
/* 471 */       return SynthRadioButtonMenuItemUI.createUI(paramJComponent);
/*     */     }
/* 473 */     if (str == "RootPaneUI") {
/* 474 */       return SynthRootPaneUI.createUI(paramJComponent);
/*     */     }
/* 476 */     if (str == "ScrollBarUI") {
/* 477 */       return SynthScrollBarUI.createUI(paramJComponent);
/*     */     }
/* 479 */     if (str == "ScrollPaneUI") {
/* 480 */       return SynthScrollPaneUI.createUI(paramJComponent);
/*     */     }
/* 482 */     if (str == "SeparatorUI") {
/* 483 */       return SynthSeparatorUI.createUI(paramJComponent);
/*     */     }
/* 485 */     if (str == "SliderUI") {
/* 486 */       return SynthSliderUI.createUI(paramJComponent);
/*     */     }
/* 488 */     if (str == "SpinnerUI") {
/* 489 */       return SynthSpinnerUI.createUI(paramJComponent);
/*     */     }
/* 491 */     if (str == "SplitPaneUI") {
/* 492 */       return SynthSplitPaneUI.createUI(paramJComponent);
/*     */     }
/* 494 */     if (str == "TabbedPaneUI") {
/* 495 */       return SynthTabbedPaneUI.createUI(paramJComponent);
/*     */     }
/* 497 */     if (str == "TableUI") {
/* 498 */       return SynthTableUI.createUI(paramJComponent);
/*     */     }
/* 500 */     if (str == "TableHeaderUI") {
/* 501 */       return SynthTableHeaderUI.createUI(paramJComponent);
/*     */     }
/* 503 */     if (str == "TextAreaUI") {
/* 504 */       return SynthTextAreaUI.createUI(paramJComponent);
/*     */     }
/* 506 */     if (str == "TextFieldUI") {
/* 507 */       return SynthTextFieldUI.createUI(paramJComponent);
/*     */     }
/* 509 */     if (str == "TextPaneUI") {
/* 510 */       return SynthTextPaneUI.createUI(paramJComponent);
/*     */     }
/* 512 */     if (str == "ToggleButtonUI") {
/* 513 */       return SynthToggleButtonUI.createUI(paramJComponent);
/*     */     }
/* 515 */     if (str == "ToolBarSeparatorUI") {
/* 516 */       return SynthSeparatorUI.createUI(paramJComponent);
/*     */     }
/* 518 */     if (str == "ToolBarUI") {
/* 519 */       return SynthToolBarUI.createUI(paramJComponent);
/*     */     }
/* 521 */     if (str == "ToolTipUI") {
/* 522 */       return SynthToolTipUI.createUI(paramJComponent);
/*     */     }
/* 524 */     if (str == "TreeUI") {
/* 525 */       return SynthTreeUI.createUI(paramJComponent);
/*     */     }
/* 527 */     if (str == "ViewportUI") {
/* 528 */       return SynthViewportUI.createUI(paramJComponent);
/*     */     }
/* 530 */     return null;
/*     */   }
/*     */ 
/*     */   public SynthLookAndFeel()
/*     */   {
/* 545 */     this.factory = new DefaultSynthStyleFactory();
/* 546 */     this._handler = new Handler(null);
/*     */   }
/*     */ 
/*     */   public void load(InputStream paramInputStream, Class<?> paramClass)
/*     */     throws ParseException
/*     */   {
/* 565 */     if (paramClass == null) {
/* 566 */       throw new IllegalArgumentException("You must supply a valid resource base Class");
/*     */     }
/*     */ 
/* 570 */     if (this.defaultsMap == null) {
/* 571 */       this.defaultsMap = new HashMap();
/*     */     }
/*     */ 
/* 574 */     new SynthParser().parse(paramInputStream, (DefaultSynthStyleFactory)this.factory, null, paramClass, this.defaultsMap);
/*     */   }
/*     */ 
/*     */   public void load(URL paramURL)
/*     */     throws ParseException, IOException
/*     */   {
/* 595 */     if (paramURL == null) {
/* 596 */       throw new IllegalArgumentException("You must supply a valid Synth set URL");
/*     */     }
/*     */ 
/* 600 */     if (this.defaultsMap == null) {
/* 601 */       this.defaultsMap = new HashMap();
/*     */     }
/*     */ 
/* 604 */     InputStream localInputStream = paramURL.openStream();
/* 605 */     new SynthParser().parse(localInputStream, (DefaultSynthStyleFactory)this.factory, paramURL, null, this.defaultsMap);
/*     */   }
/*     */ 
/*     */   public void initialize()
/*     */   {
/* 614 */     super.initialize();
/* 615 */     DefaultLookup.setDefaultLookup(new SynthDefaultLookup());
/* 616 */     setStyleFactory(this.factory);
/* 617 */     KeyboardFocusManager.getCurrentKeyboardFocusManager().addPropertyChangeListener(this._handler);
/*     */   }
/*     */ 
/*     */   public void uninitialize()
/*     */   {
/* 626 */     KeyboardFocusManager.getCurrentKeyboardFocusManager().removePropertyChangeListener(this._handler);
/*     */ 
/* 631 */     super.uninitialize();
/*     */   }
/*     */ 
/*     */   public UIDefaults getDefaults()
/*     */   {
/* 641 */     UIDefaults localUIDefaults = new UIDefaults(60, 0.75F);
/*     */ 
/* 643 */     Region.registerUIs(localUIDefaults);
/* 644 */     localUIDefaults.setDefaultLocale(Locale.getDefault());
/* 645 */     localUIDefaults.addResourceBundle("com.sun.swing.internal.plaf.basic.resources.basic");
/*     */ 
/* 647 */     localUIDefaults.addResourceBundle("com.sun.swing.internal.plaf.synth.resources.synth");
/*     */ 
/* 650 */     localUIDefaults.put("TabbedPane.isTabRollover", Boolean.TRUE);
/*     */ 
/* 653 */     localUIDefaults.put("ColorChooser.swatchesRecentSwatchSize", new Dimension(10, 10));
/*     */ 
/* 655 */     localUIDefaults.put("ColorChooser.swatchesDefaultRecentColor", Color.RED);
/* 656 */     localUIDefaults.put("ColorChooser.swatchesSwatchSize", new Dimension(10, 10));
/*     */ 
/* 659 */     localUIDefaults.put("html.pendingImage", SwingUtilities2.makeIcon(getClass(), BasicLookAndFeel.class, "icons/image-delayed.png"));
/*     */ 
/* 662 */     localUIDefaults.put("html.missingImage", SwingUtilities2.makeIcon(getClass(), BasicLookAndFeel.class, "icons/image-failed.png"));
/*     */ 
/* 667 */     localUIDefaults.put("PopupMenu.selectedWindowInputMapBindings", new Object[] { "ESCAPE", "cancel", "DOWN", "selectNext", "KP_DOWN", "selectNext", "UP", "selectPrevious", "KP_UP", "selectPrevious", "LEFT", "selectParent", "KP_LEFT", "selectParent", "RIGHT", "selectChild", "KP_RIGHT", "selectChild", "ENTER", "return", "SPACE", "return" });
/*     */ 
/* 680 */     localUIDefaults.put("PopupMenu.selectedWindowInputMapBindings.RightToLeft", new Object[] { "LEFT", "selectChild", "KP_LEFT", "selectChild", "RIGHT", "selectParent", "KP_RIGHT", "selectParent" });
/*     */ 
/* 689 */     flushUnreferenced();
/* 690 */     Object localObject = getAATextInfo();
/* 691 */     localUIDefaults.put(SwingUtilities2.AA_TEXT_PROPERTY_KEY, localObject);
/* 692 */     new AATextListener(this);
/*     */ 
/* 694 */     if (this.defaultsMap != null) {
/* 695 */       localUIDefaults.putAll(this.defaultsMap);
/*     */     }
/* 697 */     return localUIDefaults;
/*     */   }
/*     */ 
/*     */   public boolean isSupportedLookAndFeel()
/*     */   {
/* 707 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean isNativeLookAndFeel()
/*     */   {
/* 717 */     return false;
/*     */   }
/*     */ 
/*     */   public String getDescription()
/*     */   {
/* 727 */     return "Synth look and feel";
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/* 737 */     return "Synth look and feel";
/*     */   }
/*     */ 
/*     */   public String getID()
/*     */   {
/* 747 */     return "Synth";
/*     */   }
/*     */ 
/*     */   public boolean shouldUpdateStyleOnAncestorChanged()
/*     */   {
/* 763 */     return false;
/*     */   }
/*     */ 
/*     */   protected boolean shouldUpdateStyleOnEvent(PropertyChangeEvent paramPropertyChangeEvent)
/*     */   {
/* 775 */     String str = paramPropertyChangeEvent.getPropertyName();
/* 776 */     if (("name" == str) || ("componentOrientation" == str)) {
/* 777 */       return true;
/*     */     }
/* 779 */     if (("ancestor" == str) && (paramPropertyChangeEvent.getNewValue() != null))
/*     */     {
/* 782 */       return shouldUpdateStyleOnAncestorChanged();
/*     */     }
/* 784 */     return false;
/*     */   }
/*     */ 
/*     */   private static Object getAATextInfo()
/*     */   {
/* 797 */     String str1 = Locale.getDefault().getLanguage();
/* 798 */     String str2 = (String)AccessController.doPrivileged(new GetPropertyAction("sun.desktop"));
/*     */ 
/* 801 */     int i = (Locale.CHINESE.getLanguage().equals(str1)) || (Locale.JAPANESE.getLanguage().equals(str1)) || (Locale.KOREAN.getLanguage().equals(str1)) ? 1 : 0;
/*     */ 
/* 804 */     boolean bool1 = "gnome".equals(str2);
/* 805 */     boolean bool2 = SwingUtilities2.isLocalDisplay();
/*     */ 
/* 807 */     boolean bool3 = (bool2) && ((!bool1) || (i == 0));
/*     */ 
/* 809 */     SwingUtilities2.AATextInfo localAATextInfo = SwingUtilities2.AATextInfo.getAATextInfo(bool3);
/* 810 */     return localAATextInfo;
/*     */   }
/*     */ 
/*     */   private static void flushUnreferenced()
/*     */   {
/*     */     AATextListener localAATextListener;
/* 817 */     while ((localAATextListener = (AATextListener)queue.poll()) != null)
/* 818 */       localAATextListener.dispose();
/*     */   }
/*     */ 
/*     */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*     */     throws IOException
/*     */   {
/* 914 */     throw new NotSerializableException(getClass().getName());
/*     */   }
/*     */ 
/*     */   private static class AATextListener extends WeakReference<LookAndFeel>
/*     */     implements PropertyChangeListener
/*     */   {
/* 824 */     private String key = "awt.font.desktophints";
/*     */     private static boolean updatePending;
/*     */ 
/*     */     AATextListener(LookAndFeel paramLookAndFeel)
/*     */     {
/* 827 */       super(SynthLookAndFeel.queue);
/* 828 */       Toolkit localToolkit = Toolkit.getDefaultToolkit();
/* 829 */       localToolkit.addPropertyChangeListener(this.key, this);
/*     */     }
/*     */ 
/*     */     public void propertyChange(PropertyChangeEvent paramPropertyChangeEvent)
/*     */     {
/* 834 */       UIDefaults localUIDefaults = UIManager.getLookAndFeelDefaults();
/* 835 */       if (localUIDefaults.getBoolean("Synth.doNotSetTextAA")) {
/* 836 */         dispose();
/* 837 */         return;
/*     */       }
/*     */ 
/* 840 */       LookAndFeel localLookAndFeel = (LookAndFeel)get();
/* 841 */       if ((localLookAndFeel == null) || (localLookAndFeel != UIManager.getLookAndFeel())) {
/* 842 */         dispose();
/* 843 */         return;
/*     */       }
/*     */ 
/* 846 */       Object localObject = SynthLookAndFeel.access$200();
/* 847 */       localUIDefaults.put(SwingUtilities2.AA_TEXT_PROPERTY_KEY, localObject);
/*     */ 
/* 849 */       updateUI();
/*     */     }
/*     */ 
/*     */     void dispose() {
/* 853 */       Toolkit localToolkit = Toolkit.getDefaultToolkit();
/* 854 */       localToolkit.removePropertyChangeListener(this.key, this);
/*     */     }
/*     */ 
/*     */     private static void updateWindowUI(Window paramWindow)
/*     */     {
/* 861 */       SynthLookAndFeel.updateStyles(paramWindow);
/* 862 */       Window[] arrayOfWindow1 = paramWindow.getOwnedWindows();
/* 863 */       for (Window localWindow : arrayOfWindow1)
/* 864 */         updateWindowUI(localWindow);
/*     */     }
/*     */ 
/*     */     private static void updateAllUIs()
/*     */     {
/* 872 */       Frame[] arrayOfFrame1 = Frame.getFrames();
/* 873 */       for (Frame localFrame : arrayOfFrame1)
/* 874 */         updateWindowUI(localFrame);
/*     */     }
/*     */ 
/*     */     private static synchronized void setUpdatePending(boolean paramBoolean)
/*     */     {
/* 887 */       updatePending = paramBoolean;
/*     */     }
/*     */ 
/*     */     private static synchronized boolean isUpdatePending()
/*     */     {
/* 894 */       return updatePending;
/*     */     }
/*     */ 
/*     */     protected void updateUI() {
/* 898 */       if (!isUpdatePending()) {
/* 899 */         setUpdatePending(true);
/* 900 */         Runnable local1 = new Runnable()
/*     */         {
/*     */           public void run() {
/* 903 */             SynthLookAndFeel.AATextListener.access$300();
/* 904 */             SynthLookAndFeel.AATextListener.setUpdatePending(false);
/*     */           }
/*     */         };
/* 907 */         SwingUtilities.invokeLater(local1);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private class Handler implements PropertyChangeListener
/*     */   {
/*     */     private Handler()
/*     */     {
/*     */     }
/*     */ 
/*     */     public void propertyChange(PropertyChangeEvent paramPropertyChangeEvent)
/*     */     {
/* 920 */       String str = paramPropertyChangeEvent.getPropertyName();
/* 921 */       Object localObject1 = paramPropertyChangeEvent.getNewValue();
/* 922 */       Object localObject2 = paramPropertyChangeEvent.getOldValue();
/*     */ 
/* 924 */       if ("focusOwner" == str) {
/* 925 */         if ((localObject2 instanceof JComponent)) {
/* 926 */           repaintIfBackgroundsDiffer((JComponent)localObject2);
/*     */         }
/*     */ 
/* 930 */         if ((localObject1 instanceof JComponent)) {
/* 931 */           repaintIfBackgroundsDiffer((JComponent)localObject1);
/*     */         }
/*     */       }
/* 934 */       else if ("managingFocus" == str)
/*     */       {
/* 937 */         KeyboardFocusManager localKeyboardFocusManager = (KeyboardFocusManager)paramPropertyChangeEvent.getSource();
/*     */ 
/* 939 */         if (localObject1.equals(Boolean.FALSE)) {
/* 940 */           localKeyboardFocusManager.removePropertyChangeListener(SynthLookAndFeel.this._handler);
/*     */         }
/*     */         else
/* 943 */           localKeyboardFocusManager.addPropertyChangeListener(SynthLookAndFeel.this._handler);
/*     */       }
/*     */     }
/*     */ 
/*     */     private void repaintIfBackgroundsDiffer(JComponent paramJComponent)
/*     */     {
/* 956 */       ComponentUI localComponentUI = (ComponentUI)paramJComponent.getClientProperty(SwingUtilities2.COMPONENT_UI_PROPERTY_KEY);
/*     */ 
/* 958 */       if ((localComponentUI instanceof SynthUI)) {
/* 959 */         SynthUI localSynthUI = (SynthUI)localComponentUI;
/* 960 */         SynthContext localSynthContext = localSynthUI.getContext(paramJComponent);
/* 961 */         SynthStyle localSynthStyle = localSynthContext.getStyle();
/* 962 */         int i = localSynthContext.getComponentState();
/*     */ 
/* 965 */         Color localColor1 = localSynthStyle.getColor(localSynthContext, ColorType.BACKGROUND);
/*     */ 
/* 968 */         i ^= 256;
/* 969 */         localSynthContext.setComponentState(i);
/* 970 */         Color localColor2 = localSynthStyle.getColor(localSynthContext, ColorType.BACKGROUND);
/*     */ 
/* 973 */         i ^= 256;
/* 974 */         localSynthContext.setComponentState(i);
/*     */ 
/* 977 */         if ((localColor1 != null) && (!localColor1.equals(localColor2))) {
/* 978 */           paramJComponent.repaint();
/*     */         }
/* 980 */         localSynthContext.dispose();
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.synth.SynthLookAndFeel
 * JD-Core Version:    0.6.2
 */