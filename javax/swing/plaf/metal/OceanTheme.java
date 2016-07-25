/*     */ package javax.swing.plaf.metal;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Insets;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import javax.swing.AbstractButton;
/*     */ import javax.swing.ButtonModel;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.UIDefaults;
/*     */ import javax.swing.UIDefaults.LazyValue;
/*     */ import javax.swing.plaf.ColorUIResource;
/*     */ import javax.swing.plaf.IconUIResource;
/*     */ import sun.swing.PrintColorUIResource;
/*     */ import sun.swing.SwingLazyValue;
/*     */ import sun.swing.SwingUtilities2;
/*     */ 
/*     */ public class OceanTheme extends DefaultMetalTheme
/*     */ {
/*  55 */   private static final ColorUIResource PRIMARY1 = new ColorUIResource(6521535);
/*     */ 
/*  57 */   private static final ColorUIResource PRIMARY2 = new ColorUIResource(10729676);
/*     */ 
/*  59 */   private static final ColorUIResource PRIMARY3 = new ColorUIResource(12111845);
/*     */ 
/*  61 */   private static final ColorUIResource SECONDARY1 = new ColorUIResource(8030873);
/*     */ 
/*  63 */   private static final ColorUIResource SECONDARY2 = new ColorUIResource(12111845);
/*     */ 
/*  65 */   private static final ColorUIResource SECONDARY3 = new ColorUIResource(15658734);
/*     */ 
/*  68 */   private static final ColorUIResource CONTROL_TEXT_COLOR = new PrintColorUIResource(3355443, Color.BLACK);
/*     */ 
/*  70 */   private static final ColorUIResource INACTIVE_CONTROL_TEXT_COLOR = new ColorUIResource(10066329);
/*     */ 
/*  72 */   private static final ColorUIResource MENU_DISABLED_FOREGROUND = new ColorUIResource(10066329);
/*     */ 
/*  74 */   private static final ColorUIResource OCEAN_BLACK = new PrintColorUIResource(3355443, Color.BLACK);
/*     */ 
/*  77 */   private static final ColorUIResource OCEAN_DROP = new ColorUIResource(13822463);
/*     */ 
/*     */   public void addCustomEntriesToTable(UIDefaults paramUIDefaults)
/*     */   {
/* 132 */     SwingLazyValue localSwingLazyValue = new SwingLazyValue("javax.swing.plaf.BorderUIResource$LineBorderUIResource", new Object[] { getPrimary1() });
/*     */ 
/* 136 */     List localList1 = Arrays.asList(new Object[] { new Float(0.3F), new Float(0.0F), new ColorUIResource(14543091), getWhite(), getSecondary2() });
/*     */ 
/* 147 */     ColorUIResource localColorUIResource1 = new ColorUIResource(13421772);
/* 148 */     ColorUIResource localColorUIResource2 = new ColorUIResource(14342874);
/* 149 */     ColorUIResource localColorUIResource3 = new ColorUIResource(13164018);
/* 150 */     Object localObject1 = getIconResource("icons/ocean/directory.gif");
/* 151 */     Object localObject2 = getIconResource("icons/ocean/file.gif");
/* 152 */     List localList2 = Arrays.asList(new Object[] { new Float(0.3F), new Float(0.2F), localColorUIResource3, getWhite(), new ColorUIResource(SECONDARY2) });
/*     */ 
/* 156 */     Object[] arrayOfObject = { "Button.gradient", localList1, "Button.rollover", Boolean.TRUE, "Button.toolBarBorderBackground", INACTIVE_CONTROL_TEXT_COLOR, "Button.disabledToolBarBorderBackground", localColorUIResource1, "Button.rolloverIconType", "ocean", "CheckBox.rollover", Boolean.TRUE, "CheckBox.gradient", localList1, "CheckBoxMenuItem.gradient", localList1, "FileChooser.homeFolderIcon", getIconResource("icons/ocean/homeFolder.gif"), "FileChooser.newFolderIcon", getIconResource("icons/ocean/newFolder.gif"), "FileChooser.upFolderIcon", getIconResource("icons/ocean/upFolder.gif"), "FileView.computerIcon", getIconResource("icons/ocean/computer.gif"), "FileView.directoryIcon", localObject1, "FileView.hardDriveIcon", getIconResource("icons/ocean/hardDrive.gif"), "FileView.fileIcon", localObject2, "FileView.floppyDriveIcon", getIconResource("icons/ocean/floppy.gif"), "Label.disabledForeground", getInactiveControlTextColor(), "Menu.opaque", Boolean.FALSE, "MenuBar.gradient", Arrays.asList(new Object[] { new Float(1.0F), new Float(0.0F), getWhite(), localColorUIResource2, new ColorUIResource(localColorUIResource2) }), "MenuBar.borderColor", localColorUIResource1, "InternalFrame.activeTitleGradient", localList1, "InternalFrame.closeIcon", new UIDefaults.LazyValue()
/*     */     {
/*     */       public Object createValue(UIDefaults paramAnonymousUIDefaults)
/*     */       {
/* 205 */         return new OceanTheme.IFIcon(OceanTheme.this.getHastenedIcon("icons/ocean/close.gif", paramAnonymousUIDefaults), OceanTheme.this.getHastenedIcon("icons/ocean/close-pressed.gif", paramAnonymousUIDefaults));
/*     */       }
/*     */     }
/*     */     , "InternalFrame.iconifyIcon", new UIDefaults.LazyValue()
/*     */     {
/*     */       public Object createValue(UIDefaults paramAnonymousUIDefaults)
/*     */       {
/* 213 */         return new OceanTheme.IFIcon(OceanTheme.this.getHastenedIcon("icons/ocean/iconify.gif", paramAnonymousUIDefaults), OceanTheme.this.getHastenedIcon("icons/ocean/iconify-pressed.gif", paramAnonymousUIDefaults));
/*     */       }
/*     */     }
/*     */     , "InternalFrame.minimizeIcon", new UIDefaults.LazyValue()
/*     */     {
/*     */       public Object createValue(UIDefaults paramAnonymousUIDefaults)
/*     */       {
/* 221 */         return new OceanTheme.IFIcon(OceanTheme.this.getHastenedIcon("icons/ocean/minimize.gif", paramAnonymousUIDefaults), OceanTheme.this.getHastenedIcon("icons/ocean/minimize-pressed.gif", paramAnonymousUIDefaults));
/*     */       }
/*     */     }
/*     */     , "InternalFrame.icon", getIconResource("icons/ocean/menu.gif"), "InternalFrame.maximizeIcon", new UIDefaults.LazyValue()
/*     */     {
/*     */       public Object createValue(UIDefaults paramAnonymousUIDefaults)
/*     */       {
/* 232 */         return new OceanTheme.IFIcon(OceanTheme.this.getHastenedIcon("icons/ocean/maximize.gif", paramAnonymousUIDefaults), OceanTheme.this.getHastenedIcon("icons/ocean/maximize-pressed.gif", paramAnonymousUIDefaults));
/*     */       }
/*     */     }
/*     */     , "InternalFrame.paletteCloseIcon", new UIDefaults.LazyValue()
/*     */     {
/*     */       public Object createValue(UIDefaults paramAnonymousUIDefaults)
/*     */       {
/* 240 */         return new OceanTheme.IFIcon(OceanTheme.this.getHastenedIcon("icons/ocean/paletteClose.gif", paramAnonymousUIDefaults), OceanTheme.this.getHastenedIcon("icons/ocean/paletteClose-pressed.gif", paramAnonymousUIDefaults));
/*     */       }
/*     */     }
/*     */     , "List.focusCellHighlightBorder", localSwingLazyValue, "MenuBarUI", "javax.swing.plaf.metal.MetalMenuBarUI", "OptionPane.errorIcon", getIconResource("icons/ocean/error.png"), "OptionPane.informationIcon", getIconResource("icons/ocean/info.png"), "OptionPane.questionIcon", getIconResource("icons/ocean/question.png"), "OptionPane.warningIcon", getIconResource("icons/ocean/warning.png"), "RadioButton.gradient", localList1, "RadioButton.rollover", Boolean.TRUE, "RadioButtonMenuItem.gradient", localList1, "ScrollBar.gradient", localList1, "Slider.altTrackColor", new ColorUIResource(13820655), "Slider.gradient", localList2, "Slider.focusGradient", localList2, "SplitPane.oneTouchButtonsOpaque", Boolean.FALSE, "SplitPane.dividerFocusColor", localColorUIResource3, "TabbedPane.borderHightlightColor", getPrimary1(), "TabbedPane.contentAreaColor", localColorUIResource3, "TabbedPane.contentBorderInsets", new Insets(4, 2, 3, 3), "TabbedPane.selected", localColorUIResource3, "TabbedPane.tabAreaBackground", localColorUIResource2, "TabbedPane.tabAreaInsets", new Insets(2, 2, 0, 6), "TabbedPane.unselectedBackground", SECONDARY3, "Table.focusCellHighlightBorder", localSwingLazyValue, "Table.gridColor", SECONDARY1, "TableHeader.focusCellBackground", localColorUIResource3, "ToggleButton.gradient", localList1, "ToolBar.borderColor", localColorUIResource1, "ToolBar.isRollover", Boolean.TRUE, "Tree.closedIcon", localObject1, "Tree.collapsedIcon", new UIDefaults.LazyValue()
/*     */     {
/*     */       public Object createValue(UIDefaults paramAnonymousUIDefaults)
/*     */       {
/* 294 */         return new OceanTheme.COIcon(OceanTheme.this.getHastenedIcon("icons/ocean/collapsed.gif", paramAnonymousUIDefaults), OceanTheme.this.getHastenedIcon("icons/ocean/collapsed-rtl.gif", paramAnonymousUIDefaults));
/*     */       }
/*     */     }
/*     */     , "Tree.expandedIcon", getIconResource("icons/ocean/expanded.gif"), "Tree.leafIcon", localObject2, "Tree.openIcon", localObject1, "Tree.selectionBorderColor", getPrimary1(), "Tree.dropLineColor", getPrimary1(), "Table.dropLineColor", getPrimary1(), "Table.dropLineShortColor", OCEAN_BLACK, "Table.dropCellBackground", OCEAN_DROP, "Tree.dropCellBackground", OCEAN_DROP, "List.dropCellBackground", OCEAN_DROP, "List.dropLineColor", getPrimary1() };
/*     */ 
/* 313 */     paramUIDefaults.putDefaults(arrayOfObject);
/*     */   }
/*     */ 
/*     */   boolean isSystemTheme()
/*     */   {
/* 320 */     return true;
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/* 329 */     return "Ocean";
/*     */   }
/*     */ 
/*     */   protected ColorUIResource getPrimary1()
/*     */   {
/* 340 */     return PRIMARY1;
/*     */   }
/*     */ 
/*     */   protected ColorUIResource getPrimary2()
/*     */   {
/* 351 */     return PRIMARY2;
/*     */   }
/*     */ 
/*     */   protected ColorUIResource getPrimary3()
/*     */   {
/* 362 */     return PRIMARY3;
/*     */   }
/*     */ 
/*     */   protected ColorUIResource getSecondary1()
/*     */   {
/* 373 */     return SECONDARY1;
/*     */   }
/*     */ 
/*     */   protected ColorUIResource getSecondary2()
/*     */   {
/* 384 */     return SECONDARY2;
/*     */   }
/*     */ 
/*     */   protected ColorUIResource getSecondary3()
/*     */   {
/* 395 */     return SECONDARY3;
/*     */   }
/*     */ 
/*     */   protected ColorUIResource getBlack()
/*     */   {
/* 406 */     return OCEAN_BLACK;
/*     */   }
/*     */ 
/*     */   public ColorUIResource getDesktopColor()
/*     */   {
/* 417 */     return MetalTheme.white;
/*     */   }
/*     */ 
/*     */   public ColorUIResource getInactiveControlTextColor()
/*     */   {
/* 427 */     return INACTIVE_CONTROL_TEXT_COLOR;
/*     */   }
/*     */ 
/*     */   public ColorUIResource getControlTextColor()
/*     */   {
/* 437 */     return CONTROL_TEXT_COLOR;
/*     */   }
/*     */ 
/*     */   public ColorUIResource getMenuDisabledForeground()
/*     */   {
/* 447 */     return MENU_DISABLED_FOREGROUND;
/*     */   }
/*     */ 
/*     */   private Object getIconResource(String paramString) {
/* 451 */     return SwingUtilities2.makeIcon(getClass(), OceanTheme.class, paramString);
/*     */   }
/*     */ 
/*     */   private Icon getHastenedIcon(String paramString, UIDefaults paramUIDefaults)
/*     */   {
/* 457 */     Object localObject = getIconResource(paramString);
/* 458 */     return (Icon)((UIDefaults.LazyValue)localObject).createValue(paramUIDefaults);
/*     */   }
/*     */ 
/*     */   private static class COIcon extends IconUIResource
/*     */   {
/*     */     private Icon rtl;
/*     */ 
/*     */     public COIcon(Icon paramIcon1, Icon paramIcon2)
/*     */     {
/*  86 */       super();
/*  87 */       this.rtl = paramIcon2;
/*     */     }
/*     */ 
/*     */     public void paintIcon(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2) {
/*  91 */       if (MetalUtils.isLeftToRight(paramComponent))
/*  92 */         super.paintIcon(paramComponent, paramGraphics, paramInt1, paramInt2);
/*     */       else
/*  94 */         this.rtl.paintIcon(paramComponent, paramGraphics, paramInt1, paramInt2);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class IFIcon extends IconUIResource
/*     */   {
/*     */     private Icon pressed;
/*     */ 
/*     */     public IFIcon(Icon paramIcon1, Icon paramIcon2)
/*     */     {
/* 105 */       super();
/* 106 */       this.pressed = paramIcon2;
/*     */     }
/*     */ 
/*     */     public void paintIcon(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2) {
/* 110 */       ButtonModel localButtonModel = ((AbstractButton)paramComponent).getModel();
/* 111 */       if ((localButtonModel.isPressed()) && (localButtonModel.isArmed()))
/* 112 */         this.pressed.paintIcon(paramComponent, paramGraphics, paramInt1, paramInt2);
/*     */       else
/* 114 */         super.paintIcon(paramComponent, paramGraphics, paramInt1, paramInt2);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.metal.OceanTheme
 * JD-Core Version:    0.6.2
 */