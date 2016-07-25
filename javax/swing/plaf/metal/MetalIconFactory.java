/*      */ package javax.swing.plaf.metal;
/*      */ 
/*      */ import java.awt.Color;
/*      */ import java.awt.Component;
/*      */ import java.awt.Dimension;
/*      */ import java.awt.Graphics;
/*      */ import java.awt.Graphics2D;
/*      */ import java.awt.GraphicsConfiguration;
/*      */ import java.awt.Image;
/*      */ import java.awt.Polygon;
/*      */ import java.awt.Rectangle;
/*      */ import java.awt.image.BufferedImage;
/*      */ import java.io.Serializable;
/*      */ import java.util.Vector;
/*      */ import javax.swing.ButtonModel;
/*      */ import javax.swing.Icon;
/*      */ import javax.swing.JButton;
/*      */ import javax.swing.JCheckBox;
/*      */ import javax.swing.JMenu;
/*      */ import javax.swing.JMenuItem;
/*      */ import javax.swing.JRadioButton;
/*      */ import javax.swing.plaf.ColorUIResource;
/*      */ import javax.swing.plaf.UIResource;
/*      */ import sun.swing.CachedPainter;
/*      */ 
/*      */ public class MetalIconFactory
/*      */   implements Serializable
/*      */ {
/*      */   private static Icon fileChooserDetailViewIcon;
/*      */   private static Icon fileChooserHomeFolderIcon;
/*      */   private static Icon fileChooserListViewIcon;
/*      */   private static Icon fileChooserNewFolderIcon;
/*      */   private static Icon fileChooserUpFolderIcon;
/*      */   private static Icon internalFrameAltMaximizeIcon;
/*      */   private static Icon internalFrameCloseIcon;
/*      */   private static Icon internalFrameDefaultMenuIcon;
/*      */   private static Icon internalFrameMaximizeIcon;
/*      */   private static Icon internalFrameMinimizeIcon;
/*      */   private static Icon radioButtonIcon;
/*      */   private static Icon treeComputerIcon;
/*      */   private static Icon treeFloppyDriveIcon;
/*      */   private static Icon treeHardDriveIcon;
/*      */   private static Icon menuArrowIcon;
/*      */   private static Icon menuItemArrowIcon;
/*      */   private static Icon checkBoxMenuItemIcon;
/*      */   private static Icon radioButtonMenuItemIcon;
/*      */   private static Icon checkBoxIcon;
/*      */   private static Icon oceanHorizontalSliderThumb;
/*      */   private static Icon oceanVerticalSliderThumb;
/*      */   public static final boolean DARK = false;
/*      */   public static final boolean LIGHT = true;
/* 1488 */   private static final Dimension folderIcon16Size = new Dimension(16, 16);
/*      */ 
/* 1645 */   private static final Dimension fileIcon16Size = new Dimension(16, 16);
/*      */ 
/* 1730 */   private static final Dimension treeControlSize = new Dimension(18, 18);
/*      */ 
/* 1878 */   private static final Dimension menuArrowIconSize = new Dimension(4, 8);
/* 1879 */   private static final Dimension menuCheckIconSize = new Dimension(10, 10);
/*      */   private static final int xOff = 4;
/*      */ 
/*      */   public static Icon getFileChooserDetailViewIcon()
/*      */   {
/*   97 */     if (fileChooserDetailViewIcon == null) {
/*   98 */       fileChooserDetailViewIcon = new FileChooserDetailViewIcon(null);
/*      */     }
/*  100 */     return fileChooserDetailViewIcon;
/*      */   }
/*      */ 
/*      */   public static Icon getFileChooserHomeFolderIcon() {
/*  104 */     if (fileChooserHomeFolderIcon == null) {
/*  105 */       fileChooserHomeFolderIcon = new FileChooserHomeFolderIcon(null);
/*      */     }
/*  107 */     return fileChooserHomeFolderIcon;
/*      */   }
/*      */ 
/*      */   public static Icon getFileChooserListViewIcon() {
/*  111 */     if (fileChooserListViewIcon == null) {
/*  112 */       fileChooserListViewIcon = new FileChooserListViewIcon(null);
/*      */     }
/*  114 */     return fileChooserListViewIcon;
/*      */   }
/*      */ 
/*      */   public static Icon getFileChooserNewFolderIcon() {
/*  118 */     if (fileChooserNewFolderIcon == null) {
/*  119 */       fileChooserNewFolderIcon = new FileChooserNewFolderIcon(null);
/*      */     }
/*  121 */     return fileChooserNewFolderIcon;
/*      */   }
/*      */ 
/*      */   public static Icon getFileChooserUpFolderIcon() {
/*  125 */     if (fileChooserUpFolderIcon == null) {
/*  126 */       fileChooserUpFolderIcon = new FileChooserUpFolderIcon(null);
/*      */     }
/*  128 */     return fileChooserUpFolderIcon;
/*      */   }
/*      */ 
/*      */   public static Icon getInternalFrameAltMaximizeIcon(int paramInt) {
/*  132 */     return new InternalFrameAltMaximizeIcon(paramInt);
/*      */   }
/*      */ 
/*      */   public static Icon getInternalFrameCloseIcon(int paramInt) {
/*  136 */     return new InternalFrameCloseIcon(paramInt);
/*      */   }
/*      */ 
/*      */   public static Icon getInternalFrameDefaultMenuIcon() {
/*  140 */     if (internalFrameDefaultMenuIcon == null) {
/*  141 */       internalFrameDefaultMenuIcon = new InternalFrameDefaultMenuIcon(null);
/*      */     }
/*  143 */     return internalFrameDefaultMenuIcon;
/*      */   }
/*      */ 
/*      */   public static Icon getInternalFrameMaximizeIcon(int paramInt) {
/*  147 */     return new InternalFrameMaximizeIcon(paramInt);
/*      */   }
/*      */ 
/*      */   public static Icon getInternalFrameMinimizeIcon(int paramInt) {
/*  151 */     return new InternalFrameMinimizeIcon(paramInt);
/*      */   }
/*      */ 
/*      */   public static Icon getRadioButtonIcon() {
/*  155 */     if (radioButtonIcon == null) {
/*  156 */       radioButtonIcon = new RadioButtonIcon(null);
/*      */     }
/*  158 */     return radioButtonIcon;
/*      */   }
/*      */ 
/*      */   public static Icon getCheckBoxIcon()
/*      */   {
/*  166 */     if (checkBoxIcon == null) {
/*  167 */       checkBoxIcon = new CheckBoxIcon(null);
/*      */     }
/*  169 */     return checkBoxIcon;
/*      */   }
/*      */ 
/*      */   public static Icon getTreeComputerIcon() {
/*  173 */     if (treeComputerIcon == null) {
/*  174 */       treeComputerIcon = new TreeComputerIcon(null);
/*      */     }
/*  176 */     return treeComputerIcon;
/*      */   }
/*      */ 
/*      */   public static Icon getTreeFloppyDriveIcon() {
/*  180 */     if (treeFloppyDriveIcon == null) {
/*  181 */       treeFloppyDriveIcon = new TreeFloppyDriveIcon(null);
/*      */     }
/*  183 */     return treeFloppyDriveIcon;
/*      */   }
/*      */ 
/*      */   public static Icon getTreeFolderIcon() {
/*  187 */     return new TreeFolderIcon();
/*      */   }
/*      */ 
/*      */   public static Icon getTreeHardDriveIcon() {
/*  191 */     if (treeHardDriveIcon == null) {
/*  192 */       treeHardDriveIcon = new TreeHardDriveIcon(null);
/*      */     }
/*  194 */     return treeHardDriveIcon;
/*      */   }
/*      */ 
/*      */   public static Icon getTreeLeafIcon() {
/*  198 */     return new TreeLeafIcon();
/*      */   }
/*      */ 
/*      */   public static Icon getTreeControlIcon(boolean paramBoolean) {
/*  202 */     return new TreeControlIcon(paramBoolean);
/*      */   }
/*      */ 
/*      */   public static Icon getMenuArrowIcon() {
/*  206 */     if (menuArrowIcon == null) {
/*  207 */       menuArrowIcon = new MenuArrowIcon(null);
/*      */     }
/*  209 */     return menuArrowIcon;
/*      */   }
/*      */ 
/*      */   public static Icon getMenuItemCheckIcon()
/*      */   {
/*  219 */     return null;
/*      */   }
/*      */ 
/*      */   public static Icon getMenuItemArrowIcon() {
/*  223 */     if (menuItemArrowIcon == null) {
/*  224 */       menuItemArrowIcon = new MenuItemArrowIcon(null);
/*      */     }
/*  226 */     return menuItemArrowIcon;
/*      */   }
/*      */ 
/*      */   public static Icon getCheckBoxMenuItemIcon() {
/*  230 */     if (checkBoxMenuItemIcon == null) {
/*  231 */       checkBoxMenuItemIcon = new CheckBoxMenuItemIcon(null);
/*      */     }
/*  233 */     return checkBoxMenuItemIcon;
/*      */   }
/*      */ 
/*      */   public static Icon getRadioButtonMenuItemIcon() {
/*  237 */     if (radioButtonMenuItemIcon == null) {
/*  238 */       radioButtonMenuItemIcon = new RadioButtonMenuItemIcon(null);
/*      */     }
/*  240 */     return radioButtonMenuItemIcon;
/*      */   }
/*      */ 
/*      */   public static Icon getHorizontalSliderThumbIcon() {
/*  244 */     if (MetalLookAndFeel.usingOcean()) {
/*  245 */       if (oceanHorizontalSliderThumb == null) {
/*  246 */         oceanHorizontalSliderThumb = new OceanHorizontalSliderThumbIcon();
/*      */       }
/*      */ 
/*  249 */       return oceanHorizontalSliderThumb;
/*      */     }
/*      */ 
/*  252 */     return new HorizontalSliderThumbIcon();
/*      */   }
/*      */ 
/*      */   public static Icon getVerticalSliderThumbIcon() {
/*  256 */     if (MetalLookAndFeel.usingOcean()) {
/*  257 */       if (oceanVerticalSliderThumb == null) {
/*  258 */         oceanVerticalSliderThumb = new OceanVerticalSliderThumbIcon();
/*      */       }
/*  260 */       return oceanVerticalSliderThumb;
/*      */     }
/*      */ 
/*  263 */     return new VerticalSliderThumbIcon();
/*      */   }
/*      */ 
/*      */   private static class CheckBoxIcon
/*      */     implements Icon, UIResource, Serializable
/*      */   {
/*      */     protected int getControlSize()
/*      */     {
/* 1047 */       return 13;
/*      */     }
/*      */     private void paintOceanIcon(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2) {
/* 1050 */       ButtonModel localButtonModel = ((JCheckBox)paramComponent).getModel();
/*      */ 
/* 1052 */       paramGraphics.translate(paramInt1, paramInt2);
/* 1053 */       int i = getIconWidth();
/* 1054 */       int j = getIconHeight();
/* 1055 */       if (localButtonModel.isEnabled()) {
/* 1056 */         if ((localButtonModel.isPressed()) && (localButtonModel.isArmed())) {
/* 1057 */           paramGraphics.setColor(MetalLookAndFeel.getControlShadow());
/* 1058 */           paramGraphics.fillRect(0, 0, i, j);
/* 1059 */           paramGraphics.setColor(MetalLookAndFeel.getControlDarkShadow());
/* 1060 */           paramGraphics.fillRect(0, 0, i, 2);
/* 1061 */           paramGraphics.fillRect(0, 2, 2, j - 2);
/* 1062 */           paramGraphics.fillRect(i - 1, 1, 1, j - 1);
/* 1063 */           paramGraphics.fillRect(1, j - 1, i - 2, 1);
/* 1064 */         } else if (localButtonModel.isRollover()) {
/* 1065 */           MetalUtils.drawGradient(paramComponent, paramGraphics, "CheckBox.gradient", 0, 0, i, j, true);
/*      */ 
/* 1067 */           paramGraphics.setColor(MetalLookAndFeel.getControlDarkShadow());
/* 1068 */           paramGraphics.drawRect(0, 0, i - 1, j - 1);
/* 1069 */           paramGraphics.setColor(MetalLookAndFeel.getPrimaryControl());
/* 1070 */           paramGraphics.drawRect(1, 1, i - 3, j - 3);
/* 1071 */           paramGraphics.drawRect(2, 2, i - 5, j - 5);
/*      */         }
/*      */         else {
/* 1074 */           MetalUtils.drawGradient(paramComponent, paramGraphics, "CheckBox.gradient", 0, 0, i, j, true);
/*      */ 
/* 1076 */           paramGraphics.setColor(MetalLookAndFeel.getControlDarkShadow());
/* 1077 */           paramGraphics.drawRect(0, 0, i - 1, j - 1);
/*      */         }
/* 1079 */         paramGraphics.setColor(MetalLookAndFeel.getControlInfo());
/*      */       } else {
/* 1081 */         paramGraphics.setColor(MetalLookAndFeel.getControlDarkShadow());
/* 1082 */         paramGraphics.drawRect(0, 0, i - 1, j - 1);
/*      */       }
/* 1084 */       paramGraphics.translate(-paramInt1, -paramInt2);
/* 1085 */       if (localButtonModel.isSelected())
/* 1086 */         drawCheck(paramComponent, paramGraphics, paramInt1, paramInt2);
/*      */     }
/*      */ 
/*      */     public void paintIcon(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2)
/*      */     {
/* 1091 */       if (MetalLookAndFeel.usingOcean()) {
/* 1092 */         paintOceanIcon(paramComponent, paramGraphics, paramInt1, paramInt2);
/* 1093 */         return;
/*      */       }
/* 1095 */       ButtonModel localButtonModel = ((JCheckBox)paramComponent).getModel();
/* 1096 */       int i = getControlSize();
/*      */ 
/* 1098 */       if (localButtonModel.isEnabled()) {
/* 1099 */         if ((localButtonModel.isPressed()) && (localButtonModel.isArmed())) {
/* 1100 */           paramGraphics.setColor(MetalLookAndFeel.getControlShadow());
/* 1101 */           paramGraphics.fillRect(paramInt1, paramInt2, i - 1, i - 1);
/* 1102 */           MetalUtils.drawPressed3DBorder(paramGraphics, paramInt1, paramInt2, i, i);
/*      */         } else {
/* 1104 */           MetalUtils.drawFlush3DBorder(paramGraphics, paramInt1, paramInt2, i, i);
/*      */         }
/* 1106 */         paramGraphics.setColor(paramComponent.getForeground());
/*      */       } else {
/* 1108 */         paramGraphics.setColor(MetalLookAndFeel.getControlShadow());
/* 1109 */         paramGraphics.drawRect(paramInt1, paramInt2, i - 2, i - 2);
/*      */       }
/*      */ 
/* 1112 */       if (localButtonModel.isSelected())
/* 1113 */         drawCheck(paramComponent, paramGraphics, paramInt1, paramInt2);
/*      */     }
/*      */ 
/*      */     protected void drawCheck(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2)
/*      */     {
/* 1119 */       int i = getControlSize();
/* 1120 */       paramGraphics.fillRect(paramInt1 + 3, paramInt2 + 5, 2, i - 8);
/* 1121 */       paramGraphics.drawLine(paramInt1 + (i - 4), paramInt2 + 3, paramInt1 + 5, paramInt2 + (i - 6));
/* 1122 */       paramGraphics.drawLine(paramInt1 + (i - 4), paramInt2 + 4, paramInt1 + 5, paramInt2 + (i - 5));
/*      */     }
/*      */ 
/*      */     public int getIconWidth() {
/* 1126 */       return getControlSize();
/*      */     }
/*      */ 
/*      */     public int getIconHeight() {
/* 1130 */       return getControlSize();
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class CheckBoxMenuItemIcon
/*      */     implements Icon, UIResource, Serializable
/*      */   {
/*      */     public void paintOceanIcon(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2)
/*      */     {
/* 1942 */       ButtonModel localButtonModel = ((JMenuItem)paramComponent).getModel();
/* 1943 */       boolean bool1 = localButtonModel.isSelected();
/* 1944 */       boolean bool2 = localButtonModel.isEnabled();
/* 1945 */       boolean bool3 = localButtonModel.isPressed();
/* 1946 */       boolean bool4 = localButtonModel.isArmed();
/*      */ 
/* 1948 */       paramGraphics.translate(paramInt1, paramInt2);
/* 1949 */       if (bool2) {
/* 1950 */         MetalUtils.drawGradient(paramComponent, paramGraphics, "CheckBoxMenuItem.gradient", 1, 1, 7, 7, true);
/*      */ 
/* 1952 */         if ((bool3) || (bool4)) {
/* 1953 */           paramGraphics.setColor(MetalLookAndFeel.getControlInfo());
/* 1954 */           paramGraphics.drawLine(0, 0, 8, 0);
/* 1955 */           paramGraphics.drawLine(0, 0, 0, 8);
/* 1956 */           paramGraphics.drawLine(8, 2, 8, 8);
/* 1957 */           paramGraphics.drawLine(2, 8, 8, 8);
/*      */ 
/* 1959 */           paramGraphics.setColor(MetalLookAndFeel.getPrimaryControl());
/* 1960 */           paramGraphics.drawLine(9, 1, 9, 9);
/* 1961 */           paramGraphics.drawLine(1, 9, 9, 9);
/*      */         }
/*      */         else {
/* 1964 */           paramGraphics.setColor(MetalLookAndFeel.getControlDarkShadow());
/* 1965 */           paramGraphics.drawLine(0, 0, 8, 0);
/* 1966 */           paramGraphics.drawLine(0, 0, 0, 8);
/* 1967 */           paramGraphics.drawLine(8, 2, 8, 8);
/* 1968 */           paramGraphics.drawLine(2, 8, 8, 8);
/*      */ 
/* 1970 */           paramGraphics.setColor(MetalLookAndFeel.getControlHighlight());
/* 1971 */           paramGraphics.drawLine(9, 1, 9, 9);
/* 1972 */           paramGraphics.drawLine(1, 9, 9, 9);
/*      */         }
/*      */       }
/*      */       else {
/* 1976 */         paramGraphics.setColor(MetalLookAndFeel.getMenuDisabledForeground());
/* 1977 */         paramGraphics.drawRect(0, 0, 8, 8);
/*      */       }
/* 1979 */       if (bool1) {
/* 1980 */         if (bool2) {
/* 1981 */           if ((bool4) || (((paramComponent instanceof JMenu)) && (bool1))) {
/* 1982 */             paramGraphics.setColor(MetalLookAndFeel.getMenuSelectedForeground());
/*      */           }
/*      */           else
/*      */           {
/* 1986 */             paramGraphics.setColor(MetalLookAndFeel.getControlInfo());
/*      */           }
/*      */         }
/*      */         else {
/* 1990 */           paramGraphics.setColor(MetalLookAndFeel.getMenuDisabledForeground());
/*      */         }
/*      */ 
/* 1993 */         paramGraphics.drawLine(2, 2, 2, 6);
/* 1994 */         paramGraphics.drawLine(3, 2, 3, 6);
/* 1995 */         paramGraphics.drawLine(4, 4, 8, 0);
/* 1996 */         paramGraphics.drawLine(4, 5, 9, 0);
/*      */       }
/* 1998 */       paramGraphics.translate(-paramInt1, -paramInt2);
/*      */     }
/*      */ 
/*      */     public void paintIcon(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2)
/*      */     {
/* 2003 */       if (MetalLookAndFeel.usingOcean()) {
/* 2004 */         paintOceanIcon(paramComponent, paramGraphics, paramInt1, paramInt2);
/* 2005 */         return;
/*      */       }
/* 2007 */       JMenuItem localJMenuItem = (JMenuItem)paramComponent;
/* 2008 */       ButtonModel localButtonModel = localJMenuItem.getModel();
/*      */ 
/* 2010 */       boolean bool1 = localButtonModel.isSelected();
/* 2011 */       boolean bool2 = localButtonModel.isEnabled();
/* 2012 */       boolean bool3 = localButtonModel.isPressed();
/* 2013 */       boolean bool4 = localButtonModel.isArmed();
/*      */ 
/* 2015 */       paramGraphics.translate(paramInt1, paramInt2);
/*      */ 
/* 2017 */       if (bool2)
/*      */       {
/* 2019 */         if ((bool3) || (bool4))
/*      */         {
/* 2021 */           paramGraphics.setColor(MetalLookAndFeel.getControlInfo());
/* 2022 */           paramGraphics.drawLine(0, 0, 8, 0);
/* 2023 */           paramGraphics.drawLine(0, 0, 0, 8);
/* 2024 */           paramGraphics.drawLine(8, 2, 8, 8);
/* 2025 */           paramGraphics.drawLine(2, 8, 8, 8);
/*      */ 
/* 2027 */           paramGraphics.setColor(MetalLookAndFeel.getPrimaryControl());
/* 2028 */           paramGraphics.drawLine(1, 1, 7, 1);
/* 2029 */           paramGraphics.drawLine(1, 1, 1, 7);
/* 2030 */           paramGraphics.drawLine(9, 1, 9, 9);
/* 2031 */           paramGraphics.drawLine(1, 9, 9, 9);
/*      */         }
/*      */         else
/*      */         {
/* 2035 */           paramGraphics.setColor(MetalLookAndFeel.getControlDarkShadow());
/* 2036 */           paramGraphics.drawLine(0, 0, 8, 0);
/* 2037 */           paramGraphics.drawLine(0, 0, 0, 8);
/* 2038 */           paramGraphics.drawLine(8, 2, 8, 8);
/* 2039 */           paramGraphics.drawLine(2, 8, 8, 8);
/*      */ 
/* 2041 */           paramGraphics.setColor(MetalLookAndFeel.getControlHighlight());
/* 2042 */           paramGraphics.drawLine(1, 1, 7, 1);
/* 2043 */           paramGraphics.drawLine(1, 1, 1, 7);
/* 2044 */           paramGraphics.drawLine(9, 1, 9, 9);
/* 2045 */           paramGraphics.drawLine(1, 9, 9, 9);
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/* 2050 */         paramGraphics.setColor(MetalLookAndFeel.getMenuDisabledForeground());
/* 2051 */         paramGraphics.drawRect(0, 0, 8, 8);
/*      */       }
/*      */ 
/* 2054 */       if (bool1)
/*      */       {
/* 2056 */         if (bool2)
/*      */         {
/* 2058 */           if ((localButtonModel.isArmed()) || (((paramComponent instanceof JMenu)) && (localButtonModel.isSelected())))
/*      */           {
/* 2060 */             paramGraphics.setColor(MetalLookAndFeel.getMenuSelectedForeground());
/*      */           }
/*      */           else
/*      */           {
/* 2064 */             paramGraphics.setColor(localJMenuItem.getForeground());
/*      */           }
/*      */         }
/*      */         else
/*      */         {
/* 2069 */           paramGraphics.setColor(MetalLookAndFeel.getMenuDisabledForeground());
/*      */         }
/*      */ 
/* 2072 */         paramGraphics.drawLine(2, 2, 2, 6);
/* 2073 */         paramGraphics.drawLine(3, 2, 3, 6);
/* 2074 */         paramGraphics.drawLine(4, 4, 8, 0);
/* 2075 */         paramGraphics.drawLine(4, 5, 9, 0);
/*      */       }
/*      */ 
/* 2078 */       paramGraphics.translate(-paramInt1, -paramInt2);
/*      */     }
/*      */     public int getIconWidth() {
/* 2081 */       return MetalIconFactory.menuCheckIconSize.width;
/*      */     }
/* 2083 */     public int getIconHeight() { return MetalIconFactory.menuCheckIconSize.height; }
/*      */ 
/*      */   }
/*      */ 
/*      */   private static class FileChooserDetailViewIcon
/*      */     implements Icon, UIResource, Serializable
/*      */   {
/*      */     public void paintIcon(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2)
/*      */     {
/*  269 */       paramGraphics.translate(paramInt1, paramInt2);
/*      */ 
/*  272 */       paramGraphics.setColor(MetalLookAndFeel.getPrimaryControlInfo());
/*      */ 
/*  274 */       paramGraphics.drawLine(2, 2, 5, 2);
/*  275 */       paramGraphics.drawLine(2, 3, 2, 7);
/*  276 */       paramGraphics.drawLine(3, 7, 6, 7);
/*  277 */       paramGraphics.drawLine(6, 6, 6, 3);
/*      */ 
/*  279 */       paramGraphics.drawLine(2, 10, 5, 10);
/*  280 */       paramGraphics.drawLine(2, 11, 2, 15);
/*  281 */       paramGraphics.drawLine(3, 15, 6, 15);
/*  282 */       paramGraphics.drawLine(6, 14, 6, 11);
/*      */ 
/*  286 */       paramGraphics.drawLine(8, 5, 15, 5);
/*  287 */       paramGraphics.drawLine(8, 13, 15, 13);
/*      */ 
/*  290 */       paramGraphics.setColor(MetalLookAndFeel.getPrimaryControl());
/*  291 */       paramGraphics.drawRect(3, 3, 2, 3);
/*  292 */       paramGraphics.drawRect(3, 11, 2, 3);
/*      */ 
/*  295 */       paramGraphics.setColor(MetalLookAndFeel.getPrimaryControlHighlight());
/*  296 */       paramGraphics.drawLine(4, 4, 4, 5);
/*  297 */       paramGraphics.drawLine(4, 12, 4, 13);
/*      */ 
/*  299 */       paramGraphics.translate(-paramInt1, -paramInt2);
/*      */     }
/*      */ 
/*      */     public int getIconWidth() {
/*  303 */       return 18;
/*      */     }
/*      */ 
/*      */     public int getIconHeight() {
/*  307 */       return 18;
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class FileChooserHomeFolderIcon implements Icon, UIResource, Serializable
/*      */   {
/*      */     public void paintIcon(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2) {
/*  314 */       paramGraphics.translate(paramInt1, paramInt2);
/*      */ 
/*  317 */       paramGraphics.setColor(MetalLookAndFeel.getPrimaryControlInfo());
/*  318 */       paramGraphics.drawLine(8, 1, 1, 8);
/*  319 */       paramGraphics.drawLine(8, 1, 15, 8);
/*  320 */       paramGraphics.drawLine(11, 2, 11, 3);
/*  321 */       paramGraphics.drawLine(12, 2, 12, 4);
/*  322 */       paramGraphics.drawLine(3, 7, 3, 15);
/*  323 */       paramGraphics.drawLine(13, 7, 13, 15);
/*  324 */       paramGraphics.drawLine(4, 15, 12, 15);
/*      */ 
/*  327 */       paramGraphics.drawLine(6, 9, 6, 14);
/*  328 */       paramGraphics.drawLine(10, 9, 10, 14);
/*  329 */       paramGraphics.drawLine(7, 9, 9, 9);
/*      */ 
/*  332 */       paramGraphics.setColor(MetalLookAndFeel.getControlDarkShadow());
/*  333 */       paramGraphics.fillRect(8, 2, 1, 1);
/*  334 */       paramGraphics.fillRect(7, 3, 3, 1);
/*  335 */       paramGraphics.fillRect(6, 4, 5, 1);
/*  336 */       paramGraphics.fillRect(5, 5, 7, 1);
/*  337 */       paramGraphics.fillRect(4, 6, 9, 2);
/*      */ 
/*  340 */       paramGraphics.drawLine(9, 12, 9, 12);
/*      */ 
/*  343 */       paramGraphics.setColor(MetalLookAndFeel.getPrimaryControl());
/*  344 */       paramGraphics.drawLine(4, 8, 12, 8);
/*  345 */       paramGraphics.fillRect(4, 9, 2, 6);
/*  346 */       paramGraphics.fillRect(11, 9, 2, 6);
/*      */ 
/*  348 */       paramGraphics.translate(-paramInt1, -paramInt2);
/*      */     }
/*      */ 
/*      */     public int getIconWidth() {
/*  352 */       return 18;
/*      */     }
/*      */ 
/*      */     public int getIconHeight() {
/*  356 */       return 18;
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class FileChooserListViewIcon implements Icon, UIResource, Serializable
/*      */   {
/*      */     public void paintIcon(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2) {
/*  363 */       paramGraphics.translate(paramInt1, paramInt2);
/*      */ 
/*  366 */       paramGraphics.setColor(MetalLookAndFeel.getPrimaryControlInfo());
/*      */ 
/*  368 */       paramGraphics.drawLine(2, 2, 5, 2);
/*  369 */       paramGraphics.drawLine(2, 3, 2, 7);
/*  370 */       paramGraphics.drawLine(3, 7, 6, 7);
/*  371 */       paramGraphics.drawLine(6, 6, 6, 3);
/*      */ 
/*  373 */       paramGraphics.drawLine(10, 2, 13, 2);
/*  374 */       paramGraphics.drawLine(10, 3, 10, 7);
/*  375 */       paramGraphics.drawLine(11, 7, 14, 7);
/*  376 */       paramGraphics.drawLine(14, 6, 14, 3);
/*      */ 
/*  378 */       paramGraphics.drawLine(2, 10, 5, 10);
/*  379 */       paramGraphics.drawLine(2, 11, 2, 15);
/*  380 */       paramGraphics.drawLine(3, 15, 6, 15);
/*  381 */       paramGraphics.drawLine(6, 14, 6, 11);
/*      */ 
/*  383 */       paramGraphics.drawLine(10, 10, 13, 10);
/*  384 */       paramGraphics.drawLine(10, 11, 10, 15);
/*  385 */       paramGraphics.drawLine(11, 15, 14, 15);
/*  386 */       paramGraphics.drawLine(14, 14, 14, 11);
/*      */ 
/*  390 */       paramGraphics.drawLine(8, 5, 8, 5);
/*  391 */       paramGraphics.drawLine(16, 5, 16, 5);
/*  392 */       paramGraphics.drawLine(8, 13, 8, 13);
/*  393 */       paramGraphics.drawLine(16, 13, 16, 13);
/*      */ 
/*  396 */       paramGraphics.setColor(MetalLookAndFeel.getPrimaryControl());
/*  397 */       paramGraphics.drawRect(3, 3, 2, 3);
/*  398 */       paramGraphics.drawRect(11, 3, 2, 3);
/*  399 */       paramGraphics.drawRect(3, 11, 2, 3);
/*  400 */       paramGraphics.drawRect(11, 11, 2, 3);
/*      */ 
/*  403 */       paramGraphics.setColor(MetalLookAndFeel.getPrimaryControlHighlight());
/*  404 */       paramGraphics.drawLine(4, 4, 4, 5);
/*  405 */       paramGraphics.drawLine(12, 4, 12, 5);
/*  406 */       paramGraphics.drawLine(4, 12, 4, 13);
/*  407 */       paramGraphics.drawLine(12, 12, 12, 13);
/*      */ 
/*  409 */       paramGraphics.translate(-paramInt1, -paramInt2);
/*      */     }
/*      */ 
/*      */     public int getIconWidth() {
/*  413 */       return 18;
/*      */     }
/*      */ 
/*      */     public int getIconHeight() {
/*  417 */       return 18;
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class FileChooserNewFolderIcon implements Icon, UIResource, Serializable
/*      */   {
/*      */     public void paintIcon(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2) {
/*  424 */       paramGraphics.translate(paramInt1, paramInt2);
/*      */ 
/*  427 */       paramGraphics.setColor(MetalLookAndFeel.getPrimaryControl());
/*  428 */       paramGraphics.fillRect(3, 5, 12, 9);
/*      */ 
/*  431 */       paramGraphics.setColor(MetalLookAndFeel.getPrimaryControlInfo());
/*  432 */       paramGraphics.drawLine(1, 6, 1, 14);
/*  433 */       paramGraphics.drawLine(2, 14, 15, 14);
/*  434 */       paramGraphics.drawLine(15, 13, 15, 5);
/*  435 */       paramGraphics.drawLine(2, 5, 9, 5);
/*  436 */       paramGraphics.drawLine(10, 6, 14, 6);
/*      */ 
/*  439 */       paramGraphics.setColor(MetalLookAndFeel.getPrimaryControlHighlight());
/*  440 */       paramGraphics.drawLine(2, 6, 2, 13);
/*  441 */       paramGraphics.drawLine(3, 6, 9, 6);
/*  442 */       paramGraphics.drawLine(10, 7, 14, 7);
/*      */ 
/*  445 */       paramGraphics.setColor(MetalLookAndFeel.getPrimaryControlDarkShadow());
/*  446 */       paramGraphics.drawLine(11, 3, 15, 3);
/*  447 */       paramGraphics.drawLine(10, 4, 15, 4);
/*      */ 
/*  449 */       paramGraphics.translate(-paramInt1, -paramInt2);
/*      */     }
/*      */ 
/*      */     public int getIconWidth() {
/*  453 */       return 18;
/*      */     }
/*      */ 
/*      */     public int getIconHeight() {
/*  457 */       return 18;
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class FileChooserUpFolderIcon implements Icon, UIResource, Serializable
/*      */   {
/*      */     public void paintIcon(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2) {
/*  464 */       paramGraphics.translate(paramInt1, paramInt2);
/*      */ 
/*  467 */       paramGraphics.setColor(MetalLookAndFeel.getPrimaryControl());
/*  468 */       paramGraphics.fillRect(3, 5, 12, 9);
/*      */ 
/*  471 */       paramGraphics.setColor(MetalLookAndFeel.getPrimaryControlInfo());
/*  472 */       paramGraphics.drawLine(1, 6, 1, 14);
/*  473 */       paramGraphics.drawLine(2, 14, 15, 14);
/*  474 */       paramGraphics.drawLine(15, 13, 15, 5);
/*  475 */       paramGraphics.drawLine(2, 5, 9, 5);
/*  476 */       paramGraphics.drawLine(10, 6, 14, 6);
/*      */ 
/*  479 */       paramGraphics.drawLine(8, 13, 8, 16);
/*  480 */       paramGraphics.drawLine(8, 9, 8, 9);
/*  481 */       paramGraphics.drawLine(7, 10, 9, 10);
/*  482 */       paramGraphics.drawLine(6, 11, 10, 11);
/*  483 */       paramGraphics.drawLine(5, 12, 11, 12);
/*      */ 
/*  486 */       paramGraphics.setColor(MetalLookAndFeel.getPrimaryControlHighlight());
/*  487 */       paramGraphics.drawLine(2, 6, 2, 13);
/*  488 */       paramGraphics.drawLine(3, 6, 9, 6);
/*  489 */       paramGraphics.drawLine(10, 7, 14, 7);
/*      */ 
/*  492 */       paramGraphics.setColor(MetalLookAndFeel.getPrimaryControlDarkShadow());
/*  493 */       paramGraphics.drawLine(11, 3, 15, 3);
/*  494 */       paramGraphics.drawLine(10, 4, 15, 4);
/*      */ 
/*  496 */       paramGraphics.translate(-paramInt1, -paramInt2);
/*      */     }
/*      */ 
/*      */     public int getIconWidth() {
/*  500 */       return 18;
/*      */     }
/*      */ 
/*      */     public int getIconHeight() {
/*  504 */       return 18;
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class FileIcon16
/*      */     implements Icon, Serializable
/*      */   {
/*      */     MetalIconFactory.ImageCacher imageCacher;
/*      */ 
/*      */     public void paintIcon(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2)
/*      */     {
/* 1663 */       GraphicsConfiguration localGraphicsConfiguration = paramComponent.getGraphicsConfiguration();
/* 1664 */       if (this.imageCacher == null) {
/* 1665 */         this.imageCacher = new MetalIconFactory.ImageCacher();
/*      */       }
/* 1667 */       Object localObject = this.imageCacher.getImage(localGraphicsConfiguration);
/* 1668 */       if (localObject == null) {
/* 1669 */         if (localGraphicsConfiguration != null) {
/* 1670 */           localObject = localGraphicsConfiguration.createCompatibleImage(getIconWidth(), getIconHeight(), 2);
/*      */         }
/*      */         else
/*      */         {
/* 1674 */           localObject = new BufferedImage(getIconWidth(), getIconHeight(), 2);
/*      */         }
/*      */ 
/* 1678 */         Graphics localGraphics = ((Image)localObject).getGraphics();
/* 1679 */         paintMe(paramComponent, localGraphics);
/* 1680 */         localGraphics.dispose();
/* 1681 */         this.imageCacher.cacheImage((Image)localObject, localGraphicsConfiguration);
/*      */       }
/* 1683 */       paramGraphics.drawImage((Image)localObject, paramInt1, paramInt2 + getShift(), null);
/*      */     }
/*      */ 
/*      */     private void paintMe(Component paramComponent, Graphics paramGraphics)
/*      */     {
/* 1688 */       int i = MetalIconFactory.fileIcon16Size.width - 1;
/* 1689 */       int j = MetalIconFactory.fileIcon16Size.height - 1;
/*      */ 
/* 1692 */       paramGraphics.setColor(MetalLookAndFeel.getWindowBackground());
/* 1693 */       paramGraphics.fillRect(4, 2, 9, 12);
/*      */ 
/* 1696 */       paramGraphics.setColor(MetalLookAndFeel.getPrimaryControlInfo());
/* 1697 */       paramGraphics.drawLine(2, 0, 2, j);
/* 1698 */       paramGraphics.drawLine(2, 0, i - 4, 0);
/* 1699 */       paramGraphics.drawLine(2, j, i - 1, j);
/* 1700 */       paramGraphics.drawLine(i - 1, 6, i - 1, j);
/* 1701 */       paramGraphics.drawLine(i - 6, 2, i - 2, 6);
/* 1702 */       paramGraphics.drawLine(i - 5, 1, i - 4, 1);
/* 1703 */       paramGraphics.drawLine(i - 3, 2, i - 3, 3);
/* 1704 */       paramGraphics.drawLine(i - 2, 4, i - 2, 5);
/*      */ 
/* 1707 */       paramGraphics.setColor(MetalLookAndFeel.getPrimaryControl());
/* 1708 */       paramGraphics.drawLine(3, 1, 3, j - 1);
/* 1709 */       paramGraphics.drawLine(3, 1, i - 6, 1);
/* 1710 */       paramGraphics.drawLine(i - 2, 7, i - 2, j - 1);
/* 1711 */       paramGraphics.drawLine(i - 5, 2, i - 3, 4);
/* 1712 */       paramGraphics.drawLine(3, j - 1, i - 2, j - 1);
/*      */     }
/*      */ 
/*      */     public int getShift() {
/* 1716 */       return 0; } 
/* 1717 */     public int getAdditionalHeight() { return 0; } 
/*      */     public int getIconWidth() {
/* 1719 */       return MetalIconFactory.fileIcon16Size.width; } 
/* 1720 */     public int getIconHeight() { return MetalIconFactory.fileIcon16Size.height + getAdditionalHeight(); }
/*      */ 
/*      */   }
/*      */ 
/*      */   public static class FolderIcon16
/*      */     implements Icon, Serializable
/*      */   {
/*      */     MetalIconFactory.ImageCacher imageCacher;
/*      */ 
/*      */     public void paintIcon(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2)
/*      */     {
/* 1562 */       GraphicsConfiguration localGraphicsConfiguration = paramComponent.getGraphicsConfiguration();
/* 1563 */       if (this.imageCacher == null) {
/* 1564 */         this.imageCacher = new MetalIconFactory.ImageCacher();
/*      */       }
/* 1566 */       Object localObject = this.imageCacher.getImage(localGraphicsConfiguration);
/* 1567 */       if (localObject == null) {
/* 1568 */         if (localGraphicsConfiguration != null) {
/* 1569 */           localObject = localGraphicsConfiguration.createCompatibleImage(getIconWidth(), getIconHeight(), 2);
/*      */         }
/*      */         else
/*      */         {
/* 1573 */           localObject = new BufferedImage(getIconWidth(), getIconHeight(), 2);
/*      */         }
/*      */ 
/* 1577 */         Graphics localGraphics = ((Image)localObject).getGraphics();
/* 1578 */         paintMe(paramComponent, localGraphics);
/* 1579 */         localGraphics.dispose();
/* 1580 */         this.imageCacher.cacheImage((Image)localObject, localGraphicsConfiguration);
/*      */       }
/* 1582 */       paramGraphics.drawImage((Image)localObject, paramInt1, paramInt2 + getShift(), null);
/*      */     }
/*      */ 
/*      */     private void paintMe(Component paramComponent, Graphics paramGraphics)
/*      */     {
/* 1588 */       int i = MetalIconFactory.folderIcon16Size.width - 1;
/* 1589 */       int j = MetalIconFactory.folderIcon16Size.height - 1;
/*      */ 
/* 1592 */       paramGraphics.setColor(MetalLookAndFeel.getPrimaryControlDarkShadow());
/* 1593 */       paramGraphics.drawLine(i - 5, 3, i, 3);
/* 1594 */       paramGraphics.drawLine(i - 6, 4, i, 4);
/*      */ 
/* 1597 */       paramGraphics.setColor(MetalLookAndFeel.getPrimaryControl());
/* 1598 */       paramGraphics.fillRect(2, 7, 13, 8);
/*      */ 
/* 1601 */       paramGraphics.setColor(MetalLookAndFeel.getPrimaryControlShadow());
/* 1602 */       paramGraphics.drawLine(i - 6, 5, i - 1, 5);
/*      */ 
/* 1605 */       paramGraphics.setColor(MetalLookAndFeel.getPrimaryControlInfo());
/* 1606 */       paramGraphics.drawLine(0, 6, 0, j);
/* 1607 */       paramGraphics.drawLine(1, 5, i - 7, 5);
/* 1608 */       paramGraphics.drawLine(i - 6, 6, i - 1, 6);
/* 1609 */       paramGraphics.drawLine(i, 5, i, j);
/* 1610 */       paramGraphics.drawLine(0, j, i, j);
/*      */ 
/* 1613 */       paramGraphics.setColor(MetalLookAndFeel.getPrimaryControlHighlight());
/* 1614 */       paramGraphics.drawLine(1, 6, 1, j - 1);
/* 1615 */       paramGraphics.drawLine(1, 6, i - 7, 6);
/* 1616 */       paramGraphics.drawLine(i - 6, 7, i - 1, 7);
/*      */     }
/*      */ 
/*      */     public int getShift() {
/* 1620 */       return 0; } 
/* 1621 */     public int getAdditionalHeight() { return 0; } 
/*      */     public int getIconWidth() {
/* 1623 */       return MetalIconFactory.folderIcon16Size.width; } 
/* 1624 */     public int getIconHeight() { return MetalIconFactory.folderIcon16Size.height + getAdditionalHeight(); }
/*      */ 
/*      */   }
/*      */ 
/*      */   private static class HorizontalSliderThumbIcon
/*      */     implements Icon, Serializable, UIResource
/*      */   {
/*      */     protected static MetalBumps controlBumps;
/*      */     protected static MetalBumps primaryBumps;
/*      */ 
/*      */     public HorizontalSliderThumbIcon()
/*      */     {
/* 2373 */       controlBumps = new MetalBumps(10, 6, MetalLookAndFeel.getControlHighlight(), MetalLookAndFeel.getControlInfo(), MetalLookAndFeel.getControl());
/*      */ 
/* 2377 */       primaryBumps = new MetalBumps(10, 6, MetalLookAndFeel.getPrimaryControl(), MetalLookAndFeel.getPrimaryControlDarkShadow(), MetalLookAndFeel.getPrimaryControlShadow());
/*      */     }
/*      */ 
/*      */     public void paintIcon(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2)
/*      */     {
/* 2384 */       paramGraphics.translate(paramInt1, paramInt2);
/*      */ 
/* 2387 */       if (paramComponent.hasFocus()) {
/* 2388 */         paramGraphics.setColor(MetalLookAndFeel.getPrimaryControlInfo());
/*      */       }
/*      */       else {
/* 2391 */         paramGraphics.setColor(paramComponent.isEnabled() ? MetalLookAndFeel.getPrimaryControlInfo() : MetalLookAndFeel.getControlDarkShadow());
/*      */       }
/*      */ 
/* 2395 */       paramGraphics.drawLine(1, 0, 13, 0);
/* 2396 */       paramGraphics.drawLine(0, 1, 0, 8);
/* 2397 */       paramGraphics.drawLine(14, 1, 14, 8);
/* 2398 */       paramGraphics.drawLine(1, 9, 7, 15);
/* 2399 */       paramGraphics.drawLine(7, 15, 14, 8);
/*      */ 
/* 2402 */       if (paramComponent.hasFocus()) {
/* 2403 */         paramGraphics.setColor(paramComponent.getForeground());
/*      */       }
/*      */       else {
/* 2406 */         paramGraphics.setColor(MetalLookAndFeel.getControl());
/*      */       }
/* 2408 */       paramGraphics.fillRect(1, 1, 13, 8);
/*      */ 
/* 2410 */       paramGraphics.drawLine(2, 9, 12, 9);
/* 2411 */       paramGraphics.drawLine(3, 10, 11, 10);
/* 2412 */       paramGraphics.drawLine(4, 11, 10, 11);
/* 2413 */       paramGraphics.drawLine(5, 12, 9, 12);
/* 2414 */       paramGraphics.drawLine(6, 13, 8, 13);
/* 2415 */       paramGraphics.drawLine(7, 14, 7, 14);
/*      */ 
/* 2418 */       if (paramComponent.isEnabled()) {
/* 2419 */         if (paramComponent.hasFocus()) {
/* 2420 */           primaryBumps.paintIcon(paramComponent, paramGraphics, 2, 2);
/*      */         }
/*      */         else {
/* 2423 */           controlBumps.paintIcon(paramComponent, paramGraphics, 2, 2);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 2428 */       if (paramComponent.isEnabled()) {
/* 2429 */         paramGraphics.setColor(paramComponent.hasFocus() ? MetalLookAndFeel.getPrimaryControl() : MetalLookAndFeel.getControlHighlight());
/*      */ 
/* 2431 */         paramGraphics.drawLine(1, 1, 13, 1);
/* 2432 */         paramGraphics.drawLine(1, 1, 1, 8);
/*      */       }
/*      */ 
/* 2435 */       paramGraphics.translate(-paramInt1, -paramInt2);
/*      */     }
/*      */ 
/*      */     public int getIconWidth() {
/* 2439 */       return 15;
/*      */     }
/*      */ 
/*      */     public int getIconHeight() {
/* 2443 */       return 16;
/*      */     }
/*      */   }
/*      */ 
/*      */   static class ImageCacher
/*      */   {
/*      */     Vector<ImageGcPair> images;
/*      */     ImageGcPair currentImageGcPair;
/*      */ 
/*      */     ImageCacher()
/*      */     {
/* 1505 */       this.images = new Vector(1, 1);
/*      */     }
/*      */ 
/*      */     Image getImage(GraphicsConfiguration paramGraphicsConfiguration)
/*      */     {
/* 1524 */       if ((this.currentImageGcPair == null) || (!this.currentImageGcPair.hasSameConfiguration(paramGraphicsConfiguration)))
/*      */       {
/* 1527 */         for (ImageGcPair localImageGcPair : this.images) {
/* 1528 */           if (localImageGcPair.hasSameConfiguration(paramGraphicsConfiguration)) {
/* 1529 */             this.currentImageGcPair = localImageGcPair;
/* 1530 */             return localImageGcPair.image;
/*      */           }
/*      */         }
/* 1533 */         return null;
/*      */       }
/* 1535 */       return this.currentImageGcPair.image;
/*      */     }
/*      */ 
/*      */     void cacheImage(Image paramImage, GraphicsConfiguration paramGraphicsConfiguration) {
/* 1539 */       ImageGcPair localImageGcPair = new ImageGcPair(paramImage, paramGraphicsConfiguration);
/* 1540 */       this.images.addElement(localImageGcPair);
/* 1541 */       this.currentImageGcPair = localImageGcPair;
/*      */     }
/*      */ 
/*      */     class ImageGcPair
/*      */     {
/*      */       Image image;
/*      */       GraphicsConfiguration gc;
/*      */ 
/*      */       ImageGcPair(Image paramGraphicsConfiguration, GraphicsConfiguration arg3)
/*      */       {
/* 1512 */         this.image = paramGraphicsConfiguration;
/*      */         Object localObject;
/* 1513 */         this.gc = localObject;
/*      */       }
/*      */ 
/*      */       boolean hasSameConfiguration(GraphicsConfiguration paramGraphicsConfiguration) {
/* 1517 */         return ((paramGraphicsConfiguration != null) && (paramGraphicsConfiguration.equals(this.gc))) || ((paramGraphicsConfiguration == null) && (this.gc == null));
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class InternalFrameAltMaximizeIcon
/*      */     implements Icon, UIResource, Serializable
/*      */   {
/*  665 */     int iconSize = 16;
/*      */ 
/*      */     public InternalFrameAltMaximizeIcon(int paramInt) {
/*  668 */       this.iconSize = paramInt;
/*      */     }
/*      */ 
/*      */     public void paintIcon(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2) {
/*  672 */       JButton localJButton = (JButton)paramComponent;
/*  673 */       ButtonModel localButtonModel = localJButton.getModel();
/*      */ 
/*  675 */       ColorUIResource localColorUIResource1 = MetalLookAndFeel.getPrimaryControl();
/*  676 */       ColorUIResource localColorUIResource2 = MetalLookAndFeel.getPrimaryControl();
/*      */ 
/*  678 */       Object localObject = MetalLookAndFeel.getPrimaryControlDarkShadow();
/*      */ 
/*  680 */       ColorUIResource localColorUIResource3 = MetalLookAndFeel.getBlack();
/*      */ 
/*  682 */       ColorUIResource localColorUIResource4 = MetalLookAndFeel.getWhite();
/*  683 */       ColorUIResource localColorUIResource5 = MetalLookAndFeel.getWhite();
/*      */ 
/*  686 */       if (localJButton.getClientProperty("paintActive") != Boolean.TRUE)
/*      */       {
/*  688 */         localColorUIResource1 = MetalLookAndFeel.getControl();
/*  689 */         localColorUIResource2 = localColorUIResource1;
/*  690 */         localObject = MetalLookAndFeel.getControlDarkShadow();
/*      */ 
/*  692 */         if ((localButtonModel.isPressed()) && (localButtonModel.isArmed())) {
/*  693 */           localColorUIResource2 = MetalLookAndFeel.getControlShadow();
/*      */ 
/*  695 */           localColorUIResource4 = localColorUIResource2;
/*  696 */           localObject = localColorUIResource3;
/*      */         }
/*      */ 
/*      */       }
/*  700 */       else if ((localButtonModel.isPressed()) && (localButtonModel.isArmed())) {
/*  701 */         localColorUIResource2 = MetalLookAndFeel.getPrimaryControlShadow();
/*      */ 
/*  703 */         localColorUIResource4 = localColorUIResource2;
/*  704 */         localObject = localColorUIResource3;
/*      */       }
/*      */ 
/*  708 */       paramGraphics.translate(paramInt1, paramInt2);
/*      */ 
/*  711 */       paramGraphics.setColor(localColorUIResource1);
/*  712 */       paramGraphics.fillRect(0, 0, this.iconSize, this.iconSize);
/*      */ 
/*  716 */       paramGraphics.setColor(localColorUIResource2);
/*  717 */       paramGraphics.fillRect(3, 6, this.iconSize - 9, this.iconSize - 9);
/*      */ 
/*  720 */       paramGraphics.setColor(localColorUIResource3);
/*  721 */       paramGraphics.drawRect(1, 5, this.iconSize - 8, this.iconSize - 8);
/*  722 */       paramGraphics.drawLine(1, this.iconSize - 2, 1, this.iconSize - 2);
/*      */ 
/*  725 */       paramGraphics.setColor(localColorUIResource5);
/*  726 */       paramGraphics.drawRect(2, 6, this.iconSize - 7, this.iconSize - 7);
/*      */ 
/*  728 */       paramGraphics.setColor(localColorUIResource4);
/*  729 */       paramGraphics.drawRect(3, 7, this.iconSize - 9, this.iconSize - 9);
/*      */ 
/*  732 */       paramGraphics.setColor((Color)localObject);
/*  733 */       paramGraphics.drawRect(2, 6, this.iconSize - 8, this.iconSize - 8);
/*      */ 
/*  736 */       paramGraphics.setColor(localColorUIResource4);
/*  737 */       paramGraphics.drawLine(this.iconSize - 6, 8, this.iconSize - 6, 8);
/*  738 */       paramGraphics.drawLine(this.iconSize - 9, 6, this.iconSize - 7, 8);
/*  739 */       paramGraphics.setColor((Color)localObject);
/*  740 */       paramGraphics.drawLine(3, this.iconSize - 3, 3, this.iconSize - 3);
/*  741 */       paramGraphics.setColor(localColorUIResource3);
/*  742 */       paramGraphics.drawLine(this.iconSize - 6, 9, this.iconSize - 6, 9);
/*  743 */       paramGraphics.setColor(localColorUIResource1);
/*  744 */       paramGraphics.drawLine(this.iconSize - 9, 5, this.iconSize - 9, 5);
/*      */ 
/*  748 */       paramGraphics.setColor((Color)localObject);
/*  749 */       paramGraphics.fillRect(this.iconSize - 7, 3, 3, 5);
/*  750 */       paramGraphics.drawLine(this.iconSize - 6, 5, this.iconSize - 3, 2);
/*  751 */       paramGraphics.drawLine(this.iconSize - 6, 6, this.iconSize - 2, 2);
/*  752 */       paramGraphics.drawLine(this.iconSize - 6, 7, this.iconSize - 3, 7);
/*      */ 
/*  755 */       paramGraphics.setColor(localColorUIResource3);
/*  756 */       paramGraphics.drawLine(this.iconSize - 8, 2, this.iconSize - 7, 2);
/*  757 */       paramGraphics.drawLine(this.iconSize - 8, 3, this.iconSize - 8, 7);
/*  758 */       paramGraphics.drawLine(this.iconSize - 6, 4, this.iconSize - 3, 1);
/*  759 */       paramGraphics.drawLine(this.iconSize - 4, 6, this.iconSize - 3, 6);
/*      */ 
/*  762 */       paramGraphics.setColor(localColorUIResource5);
/*  763 */       paramGraphics.drawLine(this.iconSize - 6, 3, this.iconSize - 6, 3);
/*  764 */       paramGraphics.drawLine(this.iconSize - 4, 5, this.iconSize - 2, 3);
/*  765 */       paramGraphics.drawLine(this.iconSize - 4, 8, this.iconSize - 3, 8);
/*  766 */       paramGraphics.drawLine(this.iconSize - 2, 8, this.iconSize - 2, 7);
/*      */ 
/*  768 */       paramGraphics.translate(-paramInt1, -paramInt2);
/*      */     }
/*      */ 
/*      */     public int getIconWidth() {
/*  772 */       return this.iconSize;
/*      */     }
/*      */ 
/*      */     public int getIconHeight() {
/*  776 */       return this.iconSize;
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class InternalFrameCloseIcon
/*      */     implements Icon, UIResource, Serializable
/*      */   {
/*  558 */     int iconSize = 16;
/*      */ 
/*      */     public InternalFrameCloseIcon(int paramInt) {
/*  561 */       this.iconSize = paramInt;
/*      */     }
/*      */ 
/*      */     public void paintIcon(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2) {
/*  565 */       JButton localJButton = (JButton)paramComponent;
/*  566 */       ButtonModel localButtonModel = localJButton.getModel();
/*      */ 
/*  568 */       ColorUIResource localColorUIResource1 = MetalLookAndFeel.getPrimaryControl();
/*  569 */       ColorUIResource localColorUIResource2 = MetalLookAndFeel.getPrimaryControl();
/*      */ 
/*  571 */       Object localObject = MetalLookAndFeel.getPrimaryControlDarkShadow();
/*      */ 
/*  573 */       ColorUIResource localColorUIResource3 = MetalLookAndFeel.getBlack();
/*  574 */       ColorUIResource localColorUIResource4 = MetalLookAndFeel.getWhite();
/*  575 */       ColorUIResource localColorUIResource5 = MetalLookAndFeel.getWhite();
/*      */ 
/*  578 */       if (localJButton.getClientProperty("paintActive") != Boolean.TRUE)
/*      */       {
/*  580 */         localColorUIResource1 = MetalLookAndFeel.getControl();
/*  581 */         localColorUIResource2 = localColorUIResource1;
/*  582 */         localObject = MetalLookAndFeel.getControlDarkShadow();
/*      */ 
/*  584 */         if ((localButtonModel.isPressed()) && (localButtonModel.isArmed())) {
/*  585 */           localColorUIResource2 = MetalLookAndFeel.getControlShadow();
/*      */ 
/*  587 */           localColorUIResource4 = localColorUIResource2;
/*  588 */           localObject = localColorUIResource3;
/*      */         }
/*      */ 
/*      */       }
/*  592 */       else if ((localButtonModel.isPressed()) && (localButtonModel.isArmed())) {
/*  593 */         localColorUIResource2 = MetalLookAndFeel.getPrimaryControlShadow();
/*      */ 
/*  595 */         localColorUIResource4 = localColorUIResource2;
/*  596 */         localObject = localColorUIResource3;
/*      */       }
/*      */ 
/*  601 */       int i = this.iconSize / 2;
/*      */ 
/*  603 */       paramGraphics.translate(paramInt1, paramInt2);
/*      */ 
/*  606 */       paramGraphics.setColor(localColorUIResource1);
/*  607 */       paramGraphics.fillRect(0, 0, this.iconSize, this.iconSize);
/*      */ 
/*  610 */       paramGraphics.setColor(localColorUIResource2);
/*  611 */       paramGraphics.fillRect(3, 3, this.iconSize - 6, this.iconSize - 6);
/*      */ 
/*  615 */       paramGraphics.setColor(localColorUIResource3);
/*  616 */       paramGraphics.drawRect(1, 1, this.iconSize - 3, this.iconSize - 3);
/*      */ 
/*  618 */       paramGraphics.drawRect(2, 2, this.iconSize - 5, this.iconSize - 5);
/*      */ 
/*  620 */       paramGraphics.setColor(localColorUIResource5);
/*  621 */       paramGraphics.drawRect(2, 2, this.iconSize - 3, this.iconSize - 3);
/*      */ 
/*  623 */       paramGraphics.setColor((Color)localObject);
/*  624 */       paramGraphics.drawRect(2, 2, this.iconSize - 4, this.iconSize - 4);
/*  625 */       paramGraphics.drawLine(3, this.iconSize - 3, 3, this.iconSize - 3);
/*  626 */       paramGraphics.drawLine(this.iconSize - 3, 3, this.iconSize - 3, 3);
/*      */ 
/*  630 */       paramGraphics.setColor(localColorUIResource3);
/*  631 */       paramGraphics.drawLine(4, 5, 5, 4);
/*  632 */       paramGraphics.drawLine(4, this.iconSize - 6, this.iconSize - 6, 4);
/*      */ 
/*  634 */       paramGraphics.setColor(localColorUIResource4);
/*  635 */       paramGraphics.drawLine(6, this.iconSize - 5, this.iconSize - 5, 6);
/*      */ 
/*  637 */       paramGraphics.drawLine(i, i + 2, i + 2, i);
/*      */ 
/*  639 */       paramGraphics.drawLine(this.iconSize - 5, this.iconSize - 5, this.iconSize - 4, this.iconSize - 5);
/*  640 */       paramGraphics.drawLine(this.iconSize - 5, this.iconSize - 4, this.iconSize - 5, this.iconSize - 4);
/*      */ 
/*  642 */       paramGraphics.setColor((Color)localObject);
/*      */ 
/*  644 */       paramGraphics.drawLine(5, 5, this.iconSize - 6, this.iconSize - 6);
/*  645 */       paramGraphics.drawLine(6, 5, this.iconSize - 5, this.iconSize - 6);
/*  646 */       paramGraphics.drawLine(5, 6, this.iconSize - 6, this.iconSize - 5);
/*      */ 
/*  648 */       paramGraphics.drawLine(5, this.iconSize - 5, this.iconSize - 5, 5);
/*  649 */       paramGraphics.drawLine(5, this.iconSize - 6, this.iconSize - 6, 5);
/*      */ 
/*  651 */       paramGraphics.translate(-paramInt1, -paramInt2);
/*      */     }
/*      */ 
/*      */     public int getIconWidth() {
/*  655 */       return this.iconSize;
/*      */     }
/*      */ 
/*      */     public int getIconHeight() {
/*  659 */       return this.iconSize;
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class InternalFrameDefaultMenuIcon
/*      */     implements Icon, UIResource, Serializable
/*      */   {
/*      */     public void paintIcon(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2)
/*      */     {
/*  784 */       ColorUIResource localColorUIResource1 = MetalLookAndFeel.getWindowBackground();
/*  785 */       ColorUIResource localColorUIResource2 = MetalLookAndFeel.getPrimaryControl();
/*  786 */       ColorUIResource localColorUIResource3 = MetalLookAndFeel.getPrimaryControlDarkShadow();
/*      */ 
/*  788 */       paramGraphics.translate(paramInt1, paramInt2);
/*      */ 
/*  792 */       paramGraphics.setColor(localColorUIResource2);
/*  793 */       paramGraphics.fillRect(0, 0, 16, 16);
/*      */ 
/*  796 */       paramGraphics.setColor(localColorUIResource1);
/*  797 */       paramGraphics.fillRect(2, 6, 13, 9);
/*      */ 
/*  799 */       paramGraphics.drawLine(2, 2, 2, 2);
/*  800 */       paramGraphics.drawLine(5, 2, 5, 2);
/*  801 */       paramGraphics.drawLine(8, 2, 8, 2);
/*  802 */       paramGraphics.drawLine(11, 2, 11, 2);
/*      */ 
/*  805 */       paramGraphics.setColor(localColorUIResource3);
/*  806 */       paramGraphics.drawRect(1, 1, 13, 13);
/*  807 */       paramGraphics.drawLine(1, 0, 14, 0);
/*  808 */       paramGraphics.drawLine(15, 1, 15, 14);
/*  809 */       paramGraphics.drawLine(1, 15, 14, 15);
/*  810 */       paramGraphics.drawLine(0, 1, 0, 14);
/*  811 */       paramGraphics.drawLine(2, 5, 13, 5);
/*      */ 
/*  813 */       paramGraphics.drawLine(3, 3, 3, 3);
/*  814 */       paramGraphics.drawLine(6, 3, 6, 3);
/*  815 */       paramGraphics.drawLine(9, 3, 9, 3);
/*  816 */       paramGraphics.drawLine(12, 3, 12, 3);
/*      */ 
/*  818 */       paramGraphics.translate(-paramInt1, -paramInt2);
/*      */     }
/*      */ 
/*      */     public int getIconWidth() {
/*  822 */       return 16;
/*      */     }
/*      */ 
/*      */     public int getIconHeight() {
/*  826 */       return 16;
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class InternalFrameMaximizeIcon implements Icon, UIResource, Serializable
/*      */   {
/*  832 */     protected int iconSize = 16;
/*      */ 
/*      */     public InternalFrameMaximizeIcon(int paramInt) {
/*  835 */       this.iconSize = paramInt;
/*      */     }
/*      */ 
/*      */     public void paintIcon(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2) {
/*  839 */       JButton localJButton = (JButton)paramComponent;
/*  840 */       ButtonModel localButtonModel = localJButton.getModel();
/*      */ 
/*  842 */       ColorUIResource localColorUIResource1 = MetalLookAndFeel.getPrimaryControl();
/*  843 */       ColorUIResource localColorUIResource2 = MetalLookAndFeel.getPrimaryControl();
/*      */ 
/*  845 */       Object localObject = MetalLookAndFeel.getPrimaryControlDarkShadow();
/*      */ 
/*  847 */       ColorUIResource localColorUIResource3 = MetalLookAndFeel.getBlack();
/*      */ 
/*  849 */       ColorUIResource localColorUIResource4 = MetalLookAndFeel.getWhite();
/*  850 */       ColorUIResource localColorUIResource5 = MetalLookAndFeel.getWhite();
/*      */ 
/*  853 */       if (localJButton.getClientProperty("paintActive") != Boolean.TRUE)
/*      */       {
/*  855 */         localColorUIResource1 = MetalLookAndFeel.getControl();
/*  856 */         localColorUIResource2 = localColorUIResource1;
/*  857 */         localObject = MetalLookAndFeel.getControlDarkShadow();
/*      */ 
/*  859 */         if ((localButtonModel.isPressed()) && (localButtonModel.isArmed())) {
/*  860 */           localColorUIResource2 = MetalLookAndFeel.getControlShadow();
/*      */ 
/*  862 */           localColorUIResource4 = localColorUIResource2;
/*  863 */           localObject = localColorUIResource3;
/*      */         }
/*      */ 
/*      */       }
/*  867 */       else if ((localButtonModel.isPressed()) && (localButtonModel.isArmed())) {
/*  868 */         localColorUIResource2 = MetalLookAndFeel.getPrimaryControlShadow();
/*      */ 
/*  870 */         localColorUIResource4 = localColorUIResource2;
/*  871 */         localObject = localColorUIResource3;
/*      */       }
/*      */ 
/*  875 */       paramGraphics.translate(paramInt1, paramInt2);
/*      */ 
/*  878 */       paramGraphics.setColor(localColorUIResource1);
/*  879 */       paramGraphics.fillRect(0, 0, this.iconSize, this.iconSize);
/*      */ 
/*  883 */       paramGraphics.setColor(localColorUIResource2);
/*  884 */       paramGraphics.fillRect(3, 7, this.iconSize - 10, this.iconSize - 10);
/*      */ 
/*  887 */       paramGraphics.setColor(localColorUIResource4);
/*  888 */       paramGraphics.drawRect(3, 7, this.iconSize - 10, this.iconSize - 10);
/*  889 */       paramGraphics.setColor(localColorUIResource5);
/*  890 */       paramGraphics.drawRect(2, 6, this.iconSize - 7, this.iconSize - 7);
/*      */ 
/*  892 */       paramGraphics.setColor(localColorUIResource3);
/*  893 */       paramGraphics.drawRect(1, 5, this.iconSize - 7, this.iconSize - 7);
/*  894 */       paramGraphics.drawRect(2, 6, this.iconSize - 9, this.iconSize - 9);
/*      */ 
/*  896 */       paramGraphics.setColor((Color)localObject);
/*  897 */       paramGraphics.drawRect(2, 6, this.iconSize - 8, this.iconSize - 8);
/*      */ 
/*  901 */       paramGraphics.setColor(localColorUIResource3);
/*      */ 
/*  903 */       paramGraphics.drawLine(3, this.iconSize - 5, this.iconSize - 9, 7);
/*      */ 
/*  905 */       paramGraphics.drawLine(this.iconSize - 6, 4, this.iconSize - 5, 3);
/*      */ 
/*  907 */       paramGraphics.drawLine(this.iconSize - 7, 1, this.iconSize - 7, 2);
/*      */ 
/*  909 */       paramGraphics.drawLine(this.iconSize - 6, 1, this.iconSize - 2, 1);
/*      */ 
/*  911 */       paramGraphics.setColor(localColorUIResource4);
/*      */ 
/*  913 */       paramGraphics.drawLine(5, this.iconSize - 4, this.iconSize - 8, 9);
/*  914 */       paramGraphics.setColor(localColorUIResource5);
/*  915 */       paramGraphics.drawLine(this.iconSize - 6, 3, this.iconSize - 4, 5);
/*  916 */       paramGraphics.drawLine(this.iconSize - 4, 5, this.iconSize - 4, 6);
/*  917 */       paramGraphics.drawLine(this.iconSize - 2, 7, this.iconSize - 1, 7);
/*  918 */       paramGraphics.drawLine(this.iconSize - 1, 2, this.iconSize - 1, 6);
/*      */ 
/*  920 */       paramGraphics.setColor((Color)localObject);
/*  921 */       paramGraphics.drawLine(3, this.iconSize - 4, this.iconSize - 3, 2);
/*  922 */       paramGraphics.drawLine(3, this.iconSize - 3, this.iconSize - 2, 2);
/*  923 */       paramGraphics.drawLine(4, this.iconSize - 3, 5, this.iconSize - 3);
/*  924 */       paramGraphics.drawLine(this.iconSize - 7, 8, this.iconSize - 7, 9);
/*  925 */       paramGraphics.drawLine(this.iconSize - 6, 2, this.iconSize - 4, 2);
/*  926 */       paramGraphics.drawRect(this.iconSize - 3, 3, 1, 3);
/*      */ 
/*  928 */       paramGraphics.translate(-paramInt1, -paramInt2);
/*      */     }
/*      */ 
/*      */     public int getIconWidth() {
/*  932 */       return this.iconSize;
/*      */     }
/*      */ 
/*      */     public int getIconHeight() {
/*  936 */       return this.iconSize;
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class InternalFrameMinimizeIcon implements Icon, UIResource, Serializable
/*      */   {
/*  942 */     int iconSize = 16;
/*      */ 
/*      */     public InternalFrameMinimizeIcon(int paramInt) {
/*  945 */       this.iconSize = paramInt;
/*      */     }
/*      */ 
/*      */     public void paintIcon(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2) {
/*  949 */       JButton localJButton = (JButton)paramComponent;
/*  950 */       ButtonModel localButtonModel = localJButton.getModel();
/*      */ 
/*  953 */       ColorUIResource localColorUIResource1 = MetalLookAndFeel.getPrimaryControl();
/*  954 */       ColorUIResource localColorUIResource2 = MetalLookAndFeel.getPrimaryControl();
/*      */ 
/*  956 */       Object localObject = MetalLookAndFeel.getPrimaryControlDarkShadow();
/*      */ 
/*  958 */       ColorUIResource localColorUIResource3 = MetalLookAndFeel.getBlack();
/*      */ 
/*  960 */       ColorUIResource localColorUIResource4 = MetalLookAndFeel.getWhite();
/*  961 */       ColorUIResource localColorUIResource5 = MetalLookAndFeel.getWhite();
/*      */ 
/*  964 */       if (localJButton.getClientProperty("paintActive") != Boolean.TRUE)
/*      */       {
/*  966 */         localColorUIResource1 = MetalLookAndFeel.getControl();
/*  967 */         localColorUIResource2 = localColorUIResource1;
/*  968 */         localObject = MetalLookAndFeel.getControlDarkShadow();
/*      */ 
/*  970 */         if ((localButtonModel.isPressed()) && (localButtonModel.isArmed())) {
/*  971 */           localColorUIResource2 = MetalLookAndFeel.getControlShadow();
/*      */ 
/*  973 */           localColorUIResource4 = localColorUIResource2;
/*  974 */           localObject = localColorUIResource3;
/*      */         }
/*      */ 
/*      */       }
/*  978 */       else if ((localButtonModel.isPressed()) && (localButtonModel.isArmed())) {
/*  979 */         localColorUIResource2 = MetalLookAndFeel.getPrimaryControlShadow();
/*      */ 
/*  981 */         localColorUIResource4 = localColorUIResource2;
/*  982 */         localObject = localColorUIResource3;
/*      */       }
/*      */ 
/*  986 */       paramGraphics.translate(paramInt1, paramInt2);
/*      */ 
/*  989 */       paramGraphics.setColor(localColorUIResource1);
/*  990 */       paramGraphics.fillRect(0, 0, this.iconSize, this.iconSize);
/*      */ 
/*  994 */       paramGraphics.setColor(localColorUIResource2);
/*  995 */       paramGraphics.fillRect(4, 11, this.iconSize - 13, this.iconSize - 13);
/*      */ 
/*  997 */       paramGraphics.setColor(localColorUIResource5);
/*  998 */       paramGraphics.drawRect(2, 10, this.iconSize - 10, this.iconSize - 11);
/*  999 */       paramGraphics.setColor(localColorUIResource4);
/* 1000 */       paramGraphics.drawRect(3, 10, this.iconSize - 12, this.iconSize - 12);
/*      */ 
/* 1002 */       paramGraphics.setColor(localColorUIResource3);
/* 1003 */       paramGraphics.drawRect(1, 8, this.iconSize - 10, this.iconSize - 10);
/* 1004 */       paramGraphics.drawRect(2, 9, this.iconSize - 12, this.iconSize - 12);
/*      */ 
/* 1006 */       paramGraphics.setColor((Color)localObject);
/* 1007 */       paramGraphics.drawRect(2, 9, this.iconSize - 11, this.iconSize - 11);
/* 1008 */       paramGraphics.drawLine(this.iconSize - 10, 10, this.iconSize - 10, 10);
/* 1009 */       paramGraphics.drawLine(3, this.iconSize - 3, 3, this.iconSize - 3);
/*      */ 
/* 1013 */       paramGraphics.setColor((Color)localObject);
/* 1014 */       paramGraphics.fillRect(this.iconSize - 7, 3, 3, 5);
/* 1015 */       paramGraphics.drawLine(this.iconSize - 6, 5, this.iconSize - 3, 2);
/* 1016 */       paramGraphics.drawLine(this.iconSize - 6, 6, this.iconSize - 2, 2);
/* 1017 */       paramGraphics.drawLine(this.iconSize - 6, 7, this.iconSize - 3, 7);
/*      */ 
/* 1020 */       paramGraphics.setColor(localColorUIResource3);
/* 1021 */       paramGraphics.drawLine(this.iconSize - 8, 2, this.iconSize - 7, 2);
/* 1022 */       paramGraphics.drawLine(this.iconSize - 8, 3, this.iconSize - 8, 7);
/* 1023 */       paramGraphics.drawLine(this.iconSize - 6, 4, this.iconSize - 3, 1);
/* 1024 */       paramGraphics.drawLine(this.iconSize - 4, 6, this.iconSize - 3, 6);
/*      */ 
/* 1027 */       paramGraphics.setColor(localColorUIResource5);
/* 1028 */       paramGraphics.drawLine(this.iconSize - 6, 3, this.iconSize - 6, 3);
/* 1029 */       paramGraphics.drawLine(this.iconSize - 4, 5, this.iconSize - 2, 3);
/* 1030 */       paramGraphics.drawLine(this.iconSize - 7, 8, this.iconSize - 3, 8);
/* 1031 */       paramGraphics.drawLine(this.iconSize - 2, 8, this.iconSize - 2, 7);
/*      */ 
/* 1033 */       paramGraphics.translate(-paramInt1, -paramInt2);
/*      */     }
/*      */ 
/*      */     public int getIconWidth() {
/* 1037 */       return this.iconSize;
/*      */     }
/*      */ 
/*      */     public int getIconHeight() {
/* 1041 */       return this.iconSize;
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class MenuArrowIcon
/*      */     implements Icon, UIResource, Serializable
/*      */   {
/*      */     public void paintIcon(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2)
/*      */     {
/* 1886 */       JMenuItem localJMenuItem = (JMenuItem)paramComponent;
/* 1887 */       ButtonModel localButtonModel = localJMenuItem.getModel();
/*      */ 
/* 1889 */       paramGraphics.translate(paramInt1, paramInt2);
/*      */ 
/* 1891 */       if (!localButtonModel.isEnabled())
/*      */       {
/* 1893 */         paramGraphics.setColor(MetalLookAndFeel.getMenuDisabledForeground());
/*      */       }
/* 1897 */       else if ((localButtonModel.isArmed()) || (((paramComponent instanceof JMenu)) && (localButtonModel.isSelected())))
/*      */       {
/* 1899 */         paramGraphics.setColor(MetalLookAndFeel.getMenuSelectedForeground());
/*      */       }
/*      */       else
/*      */       {
/* 1903 */         paramGraphics.setColor(localJMenuItem.getForeground());
/*      */       }
/*      */ 
/* 1906 */       if (MetalUtils.isLeftToRight(localJMenuItem)) {
/* 1907 */         paramGraphics.drawLine(0, 0, 0, 7);
/* 1908 */         paramGraphics.drawLine(1, 1, 1, 6);
/* 1909 */         paramGraphics.drawLine(2, 2, 2, 5);
/* 1910 */         paramGraphics.drawLine(3, 3, 3, 4);
/*      */       } else {
/* 1912 */         paramGraphics.drawLine(4, 0, 4, 7);
/* 1913 */         paramGraphics.drawLine(3, 1, 3, 6);
/* 1914 */         paramGraphics.drawLine(2, 2, 2, 5);
/* 1915 */         paramGraphics.drawLine(1, 3, 1, 4);
/*      */       }
/*      */ 
/* 1918 */       paramGraphics.translate(-paramInt1, -paramInt2);
/*      */     }
/*      */     public int getIconWidth() {
/* 1921 */       return MetalIconFactory.menuArrowIconSize.width;
/*      */     }
/* 1923 */     public int getIconHeight() { return MetalIconFactory.menuArrowIconSize.height; }
/*      */   }
/*      */ 
/*      */   private static class MenuItemArrowIcon implements Icon, UIResource, Serializable
/*      */   {
/*      */     public void paintIcon(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2)
/*      */     {
/*      */     }
/*      */ 
/*      */     public int getIconWidth() {
/* 1933 */       return MetalIconFactory.menuArrowIconSize.width;
/*      */     }
/* 1935 */     public int getIconHeight() { return MetalIconFactory.menuArrowIconSize.height; }
/*      */ 
/*      */   }
/*      */ 
/*      */   private static class OceanHorizontalSliderThumbIcon extends CachedPainter
/*      */     implements Icon, Serializable, UIResource
/*      */   {
/* 2572 */     private static Polygon THUMB_SHAPE = new Polygon(new int[] { 0, 14, 14, 7, 0 }, new int[] { 0, 0, 8, 15, 8 }, 5);
/*      */ 
/*      */     OceanHorizontalSliderThumbIcon()
/*      */     {
/* 2577 */       super();
/*      */     }
/*      */ 
/*      */     public void paintIcon(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2) {
/* 2581 */       if (!(paramGraphics instanceof Graphics2D)) {
/* 2582 */         return;
/*      */       }
/* 2584 */       paint(paramComponent, paramGraphics, paramInt1, paramInt2, getIconWidth(), getIconHeight(), new Object[] { Boolean.valueOf(paramComponent.hasFocus()), Boolean.valueOf(paramComponent.isEnabled()), MetalLookAndFeel.getCurrentTheme() });
/*      */     }
/*      */ 
/*      */     protected Image createImage(Component paramComponent, int paramInt1, int paramInt2, GraphicsConfiguration paramGraphicsConfiguration, Object[] paramArrayOfObject)
/*      */     {
/* 2593 */       if (paramGraphicsConfiguration == null) {
/* 2594 */         return new BufferedImage(paramInt1, paramInt2, 2);
/*      */       }
/* 2596 */       return paramGraphicsConfiguration.createCompatibleImage(paramInt1, paramInt2, 2);
/*      */     }
/*      */ 
/*      */     protected void paintToImage(Component paramComponent, Image paramImage, Graphics paramGraphics, int paramInt1, int paramInt2, Object[] paramArrayOfObject)
/*      */     {
/* 2602 */       Graphics2D localGraphics2D = (Graphics2D)paramGraphics;
/* 2603 */       boolean bool1 = ((Boolean)paramArrayOfObject[0]).booleanValue();
/* 2604 */       boolean bool2 = ((Boolean)paramArrayOfObject[1]).booleanValue();
/*      */ 
/* 2607 */       Rectangle localRectangle = localGraphics2D.getClipBounds();
/* 2608 */       localGraphics2D.clip(THUMB_SHAPE);
/* 2609 */       if (!bool2) {
/* 2610 */         localGraphics2D.setColor(MetalLookAndFeel.getControl());
/* 2611 */         localGraphics2D.fillRect(1, 1, 13, 14);
/*      */       }
/* 2613 */       else if (bool1) {
/* 2614 */         MetalUtils.drawGradient(paramComponent, localGraphics2D, "Slider.focusGradient", 1, 1, 13, 14, true);
/*      */       }
/*      */       else
/*      */       {
/* 2618 */         MetalUtils.drawGradient(paramComponent, localGraphics2D, "Slider.gradient", 1, 1, 13, 14, true);
/*      */       }
/*      */ 
/* 2621 */       localGraphics2D.setClip(localRectangle);
/*      */ 
/* 2624 */       if (bool1) {
/* 2625 */         localGraphics2D.setColor(MetalLookAndFeel.getPrimaryControlDarkShadow());
/*      */       }
/*      */       else {
/* 2628 */         localGraphics2D.setColor(bool2 ? MetalLookAndFeel.getPrimaryControlInfo() : MetalLookAndFeel.getControlDarkShadow());
/*      */       }
/*      */ 
/* 2632 */       localGraphics2D.drawLine(1, 0, 13, 0);
/* 2633 */       localGraphics2D.drawLine(0, 1, 0, 8);
/* 2634 */       localGraphics2D.drawLine(14, 1, 14, 8);
/* 2635 */       localGraphics2D.drawLine(1, 9, 7, 15);
/* 2636 */       localGraphics2D.drawLine(7, 15, 14, 8);
/*      */ 
/* 2638 */       if ((bool1) && (bool2))
/*      */       {
/* 2640 */         localGraphics2D.setColor(MetalLookAndFeel.getPrimaryControl());
/* 2641 */         localGraphics2D.fillRect(1, 1, 13, 1);
/* 2642 */         localGraphics2D.fillRect(1, 2, 1, 7);
/* 2643 */         localGraphics2D.fillRect(13, 2, 1, 7);
/* 2644 */         localGraphics2D.drawLine(2, 9, 7, 14);
/* 2645 */         localGraphics2D.drawLine(8, 13, 12, 9);
/*      */       }
/*      */     }
/*      */ 
/*      */     public int getIconWidth() {
/* 2650 */       return 15;
/*      */     }
/*      */ 
/*      */     public int getIconHeight() {
/* 2654 */       return 16;
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class OceanVerticalSliderThumbIcon extends CachedPainter
/*      */     implements Icon, Serializable, UIResource
/*      */   {
/* 2455 */     private static Polygon LTR_THUMB_SHAPE = new Polygon(new int[] { 0, 8, 15, 8, 0 }, new int[] { 0, 0, 7, 14, 14 }, 5);
/*      */ 
/* 2457 */     private static Polygon RTL_THUMB_SHAPE = new Polygon(new int[] { 15, 15, 7, 0, 7 }, new int[] { 0, 14, 14, 7, 0 }, 5);
/*      */ 
/*      */     OceanVerticalSliderThumbIcon()
/*      */     {
/* 2462 */       super();
/*      */     }
/*      */ 
/*      */     public void paintIcon(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2) {
/* 2466 */       if (!(paramGraphics instanceof Graphics2D)) {
/* 2467 */         return;
/*      */       }
/* 2469 */       paint(paramComponent, paramGraphics, paramInt1, paramInt2, getIconWidth(), getIconHeight(), new Object[] { Boolean.valueOf(MetalUtils.isLeftToRight(paramComponent)), Boolean.valueOf(paramComponent.hasFocus()), Boolean.valueOf(paramComponent.isEnabled()), MetalLookAndFeel.getCurrentTheme() });
/*      */     }
/*      */ 
/*      */     protected void paintToImage(Component paramComponent, Image paramImage, Graphics paramGraphics, int paramInt1, int paramInt2, Object[] paramArrayOfObject)
/*      */     {
/* 2476 */       Graphics2D localGraphics2D = (Graphics2D)paramGraphics;
/* 2477 */       boolean bool1 = ((Boolean)paramArrayOfObject[0]).booleanValue();
/* 2478 */       boolean bool2 = ((Boolean)paramArrayOfObject[1]).booleanValue();
/* 2479 */       boolean bool3 = ((Boolean)paramArrayOfObject[2]).booleanValue();
/*      */ 
/* 2481 */       Rectangle localRectangle = localGraphics2D.getClipBounds();
/* 2482 */       if (bool1) {
/* 2483 */         localGraphics2D.clip(LTR_THUMB_SHAPE);
/*      */       }
/*      */       else {
/* 2486 */         localGraphics2D.clip(RTL_THUMB_SHAPE);
/*      */       }
/* 2488 */       if (!bool3) {
/* 2489 */         localGraphics2D.setColor(MetalLookAndFeel.getControl());
/* 2490 */         localGraphics2D.fillRect(1, 1, 14, 14);
/*      */       }
/* 2492 */       else if (bool2) {
/* 2493 */         MetalUtils.drawGradient(paramComponent, localGraphics2D, "Slider.focusGradient", 1, 1, 14, 14, false);
/*      */       }
/*      */       else
/*      */       {
/* 2497 */         MetalUtils.drawGradient(paramComponent, localGraphics2D, "Slider.gradient", 1, 1, 14, 14, false);
/*      */       }
/*      */ 
/* 2500 */       localGraphics2D.setClip(localRectangle);
/*      */ 
/* 2503 */       if (bool2) {
/* 2504 */         localGraphics2D.setColor(MetalLookAndFeel.getPrimaryControlDarkShadow());
/*      */       }
/*      */       else {
/* 2507 */         localGraphics2D.setColor(bool3 ? MetalLookAndFeel.getPrimaryControlInfo() : MetalLookAndFeel.getControlDarkShadow());
/*      */       }
/*      */ 
/* 2511 */       if (bool1) {
/* 2512 */         localGraphics2D.drawLine(1, 0, 8, 0);
/* 2513 */         localGraphics2D.drawLine(0, 1, 0, 13);
/* 2514 */         localGraphics2D.drawLine(1, 14, 8, 14);
/* 2515 */         localGraphics2D.drawLine(9, 1, 15, 7);
/* 2516 */         localGraphics2D.drawLine(9, 13, 15, 7);
/*      */       }
/*      */       else {
/* 2519 */         localGraphics2D.drawLine(7, 0, 14, 0);
/* 2520 */         localGraphics2D.drawLine(15, 1, 15, 13);
/* 2521 */         localGraphics2D.drawLine(7, 14, 14, 14);
/* 2522 */         localGraphics2D.drawLine(0, 7, 6, 1);
/* 2523 */         localGraphics2D.drawLine(0, 7, 6, 13);
/*      */       }
/*      */ 
/* 2526 */       if ((bool2) && (bool3))
/*      */       {
/* 2528 */         localGraphics2D.setColor(MetalLookAndFeel.getPrimaryControl());
/* 2529 */         if (bool1) {
/* 2530 */           localGraphics2D.drawLine(1, 1, 8, 1);
/* 2531 */           localGraphics2D.drawLine(1, 1, 1, 13);
/* 2532 */           localGraphics2D.drawLine(1, 13, 8, 13);
/* 2533 */           localGraphics2D.drawLine(9, 2, 14, 7);
/* 2534 */           localGraphics2D.drawLine(9, 12, 14, 7);
/*      */         }
/*      */         else {
/* 2537 */           localGraphics2D.drawLine(7, 1, 14, 1);
/* 2538 */           localGraphics2D.drawLine(14, 1, 14, 13);
/* 2539 */           localGraphics2D.drawLine(7, 13, 14, 13);
/* 2540 */           localGraphics2D.drawLine(1, 7, 7, 1);
/* 2541 */           localGraphics2D.drawLine(1, 7, 7, 13);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     public int getIconWidth() {
/* 2547 */       return 16;
/*      */     }
/*      */ 
/*      */     public int getIconHeight() {
/* 2551 */       return 15;
/*      */     }
/*      */ 
/*      */     protected Image createImage(Component paramComponent, int paramInt1, int paramInt2, GraphicsConfiguration paramGraphicsConfiguration, Object[] paramArrayOfObject)
/*      */     {
/* 2557 */       if (paramGraphicsConfiguration == null) {
/* 2558 */         return new BufferedImage(paramInt1, paramInt2, 2);
/*      */       }
/* 2560 */       return paramGraphicsConfiguration.createCompatibleImage(paramInt1, paramInt2, 2);
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class PaletteCloseIcon
/*      */     implements Icon, UIResource, Serializable
/*      */   {
/*  514 */     int iconSize = 7;
/*      */ 
/*      */     public void paintIcon(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2) {
/*  517 */       JButton localJButton = (JButton)paramComponent;
/*  518 */       ButtonModel localButtonModel = localJButton.getModel();
/*      */ 
/*  521 */       ColorUIResource localColorUIResource2 = MetalLookAndFeel.getPrimaryControlHighlight();
/*  522 */       ColorUIResource localColorUIResource3 = MetalLookAndFeel.getPrimaryControlInfo();
/*      */       ColorUIResource localColorUIResource1;
/*  523 */       if ((localButtonModel.isPressed()) && (localButtonModel.isArmed()))
/*  524 */         localColorUIResource1 = localColorUIResource3;
/*      */       else {
/*  526 */         localColorUIResource1 = MetalLookAndFeel.getPrimaryControlDarkShadow();
/*      */       }
/*      */ 
/*  529 */       paramGraphics.translate(paramInt1, paramInt2);
/*  530 */       paramGraphics.setColor(localColorUIResource1);
/*  531 */       paramGraphics.drawLine(0, 1, 5, 6);
/*  532 */       paramGraphics.drawLine(1, 0, 6, 5);
/*  533 */       paramGraphics.drawLine(1, 1, 6, 6);
/*  534 */       paramGraphics.drawLine(6, 1, 1, 6);
/*  535 */       paramGraphics.drawLine(5, 0, 0, 5);
/*  536 */       paramGraphics.drawLine(5, 1, 1, 5);
/*      */ 
/*  538 */       paramGraphics.setColor(localColorUIResource2);
/*  539 */       paramGraphics.drawLine(6, 2, 5, 3);
/*  540 */       paramGraphics.drawLine(2, 6, 3, 5);
/*  541 */       paramGraphics.drawLine(6, 6, 6, 6);
/*      */ 
/*  544 */       paramGraphics.translate(-paramInt1, -paramInt2);
/*      */     }
/*      */ 
/*      */     public int getIconWidth() {
/*  548 */       return this.iconSize;
/*      */     }
/*      */ 
/*      */     public int getIconHeight() {
/*  552 */       return this.iconSize;
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class RadioButtonIcon
/*      */     implements Icon, UIResource, Serializable
/*      */   {
/*      */     public void paintOceanIcon(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2)
/*      */     {
/* 1137 */       ButtonModel localButtonModel = ((JRadioButton)paramComponent).getModel();
/* 1138 */       boolean bool = localButtonModel.isEnabled();
/* 1139 */       int i = (bool) && (localButtonModel.isPressed()) && (localButtonModel.isArmed()) ? 1 : 0;
/*      */ 
/* 1141 */       int j = (bool) && (localButtonModel.isRollover()) ? 1 : 0;
/*      */ 
/* 1143 */       paramGraphics.translate(paramInt1, paramInt2);
/* 1144 */       if ((bool) && (i == 0))
/*      */       {
/* 1147 */         MetalUtils.drawGradient(paramComponent, paramGraphics, "RadioButton.gradient", 1, 1, 10, 10, true);
/*      */ 
/* 1149 */         paramGraphics.setColor(paramComponent.getBackground());
/* 1150 */         paramGraphics.fillRect(1, 1, 1, 1);
/* 1151 */         paramGraphics.fillRect(10, 1, 1, 1);
/* 1152 */         paramGraphics.fillRect(1, 10, 1, 1);
/* 1153 */         paramGraphics.fillRect(10, 10, 1, 1);
/*      */       }
/* 1155 */       else if ((i != 0) || (!bool)) {
/* 1156 */         if (i != 0) {
/* 1157 */           paramGraphics.setColor(MetalLookAndFeel.getPrimaryControl());
/*      */         }
/*      */         else {
/* 1160 */           paramGraphics.setColor(MetalLookAndFeel.getControl());
/*      */         }
/* 1162 */         paramGraphics.fillRect(2, 2, 8, 8);
/* 1163 */         paramGraphics.fillRect(4, 1, 4, 1);
/* 1164 */         paramGraphics.fillRect(4, 10, 4, 1);
/* 1165 */         paramGraphics.fillRect(1, 4, 1, 4);
/* 1166 */         paramGraphics.fillRect(10, 4, 1, 4);
/*      */       }
/*      */ 
/* 1170 */       if (!bool) {
/* 1171 */         paramGraphics.setColor(MetalLookAndFeel.getInactiveControlTextColor());
/*      */       }
/*      */       else {
/* 1174 */         paramGraphics.setColor(MetalLookAndFeel.getControlDarkShadow());
/*      */       }
/* 1176 */       paramGraphics.drawLine(4, 0, 7, 0);
/* 1177 */       paramGraphics.drawLine(8, 1, 9, 1);
/* 1178 */       paramGraphics.drawLine(10, 2, 10, 3);
/* 1179 */       paramGraphics.drawLine(11, 4, 11, 7);
/* 1180 */       paramGraphics.drawLine(10, 8, 10, 9);
/* 1181 */       paramGraphics.drawLine(9, 10, 8, 10);
/* 1182 */       paramGraphics.drawLine(7, 11, 4, 11);
/* 1183 */       paramGraphics.drawLine(3, 10, 2, 10);
/* 1184 */       paramGraphics.drawLine(1, 9, 1, 8);
/* 1185 */       paramGraphics.drawLine(0, 7, 0, 4);
/* 1186 */       paramGraphics.drawLine(1, 3, 1, 2);
/* 1187 */       paramGraphics.drawLine(2, 1, 3, 1);
/*      */ 
/* 1189 */       if (i != 0) {
/* 1190 */         paramGraphics.fillRect(1, 4, 1, 4);
/* 1191 */         paramGraphics.fillRect(2, 2, 1, 2);
/* 1192 */         paramGraphics.fillRect(3, 2, 1, 1);
/* 1193 */         paramGraphics.fillRect(4, 1, 4, 1);
/*      */       }
/* 1195 */       else if (j != 0) {
/* 1196 */         paramGraphics.setColor(MetalLookAndFeel.getPrimaryControl());
/* 1197 */         paramGraphics.fillRect(4, 1, 4, 2);
/* 1198 */         paramGraphics.fillRect(8, 2, 2, 2);
/* 1199 */         paramGraphics.fillRect(9, 4, 2, 4);
/* 1200 */         paramGraphics.fillRect(8, 8, 2, 2);
/* 1201 */         paramGraphics.fillRect(4, 9, 4, 2);
/* 1202 */         paramGraphics.fillRect(2, 8, 2, 2);
/* 1203 */         paramGraphics.fillRect(1, 4, 2, 4);
/* 1204 */         paramGraphics.fillRect(2, 2, 2, 2);
/*      */       }
/*      */ 
/* 1208 */       if (localButtonModel.isSelected()) {
/* 1209 */         if (bool)
/* 1210 */           paramGraphics.setColor(MetalLookAndFeel.getControlInfo());
/*      */         else {
/* 1212 */           paramGraphics.setColor(MetalLookAndFeel.getControlDarkShadow());
/*      */         }
/* 1214 */         paramGraphics.fillRect(4, 4, 4, 4);
/* 1215 */         paramGraphics.drawLine(4, 3, 7, 3);
/* 1216 */         paramGraphics.drawLine(8, 4, 8, 7);
/* 1217 */         paramGraphics.drawLine(7, 8, 4, 8);
/* 1218 */         paramGraphics.drawLine(3, 7, 3, 4);
/*      */       }
/*      */ 
/* 1221 */       paramGraphics.translate(-paramInt1, -paramInt2);
/*      */     }
/*      */ 
/*      */     public void paintIcon(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2) {
/* 1225 */       if (MetalLookAndFeel.usingOcean()) {
/* 1226 */         paintOceanIcon(paramComponent, paramGraphics, paramInt1, paramInt2);
/* 1227 */         return;
/*      */       }
/* 1229 */       JRadioButton localJRadioButton = (JRadioButton)paramComponent;
/* 1230 */       ButtonModel localButtonModel = localJRadioButton.getModel();
/* 1231 */       boolean bool = localButtonModel.isSelected();
/*      */ 
/* 1233 */       Color localColor = paramComponent.getBackground();
/* 1234 */       Object localObject1 = paramComponent.getForeground();
/* 1235 */       ColorUIResource localColorUIResource1 = MetalLookAndFeel.getControlShadow();
/* 1236 */       ColorUIResource localColorUIResource2 = MetalLookAndFeel.getControlDarkShadow();
/* 1237 */       Object localObject2 = MetalLookAndFeel.getControlHighlight();
/* 1238 */       Object localObject3 = MetalLookAndFeel.getControlHighlight();
/* 1239 */       Object localObject4 = localColor;
/*      */ 
/* 1242 */       if (!localButtonModel.isEnabled()) {
/* 1243 */         localObject2 = localObject3 = localColor;
/* 1244 */         localColorUIResource2 = localObject1 = localColorUIResource1;
/*      */       }
/* 1246 */       else if ((localButtonModel.isPressed()) && (localButtonModel.isArmed())) {
/* 1247 */         localObject2 = localObject4 = localColorUIResource1;
/*      */       }
/*      */ 
/* 1250 */       paramGraphics.translate(paramInt1, paramInt2);
/*      */ 
/* 1253 */       paramGraphics.setColor((Color)localObject4);
/* 1254 */       paramGraphics.fillRect(2, 2, 9, 9);
/*      */ 
/* 1257 */       paramGraphics.setColor(localColorUIResource2);
/* 1258 */       paramGraphics.drawLine(4, 0, 7, 0);
/* 1259 */       paramGraphics.drawLine(8, 1, 9, 1);
/* 1260 */       paramGraphics.drawLine(10, 2, 10, 3);
/* 1261 */       paramGraphics.drawLine(11, 4, 11, 7);
/* 1262 */       paramGraphics.drawLine(10, 8, 10, 9);
/* 1263 */       paramGraphics.drawLine(9, 10, 8, 10);
/* 1264 */       paramGraphics.drawLine(7, 11, 4, 11);
/* 1265 */       paramGraphics.drawLine(3, 10, 2, 10);
/* 1266 */       paramGraphics.drawLine(1, 9, 1, 8);
/* 1267 */       paramGraphics.drawLine(0, 7, 0, 4);
/* 1268 */       paramGraphics.drawLine(1, 3, 1, 2);
/* 1269 */       paramGraphics.drawLine(2, 1, 3, 1);
/*      */ 
/* 1273 */       paramGraphics.setColor((Color)localObject2);
/* 1274 */       paramGraphics.drawLine(2, 9, 2, 8);
/* 1275 */       paramGraphics.drawLine(1, 7, 1, 4);
/* 1276 */       paramGraphics.drawLine(2, 2, 2, 3);
/* 1277 */       paramGraphics.drawLine(2, 2, 3, 2);
/* 1278 */       paramGraphics.drawLine(4, 1, 7, 1);
/* 1279 */       paramGraphics.drawLine(8, 2, 9, 2);
/*      */ 
/* 1282 */       paramGraphics.setColor((Color)localObject3);
/* 1283 */       paramGraphics.drawLine(10, 1, 10, 1);
/* 1284 */       paramGraphics.drawLine(11, 2, 11, 3);
/* 1285 */       paramGraphics.drawLine(12, 4, 12, 7);
/* 1286 */       paramGraphics.drawLine(11, 8, 11, 9);
/* 1287 */       paramGraphics.drawLine(10, 10, 10, 10);
/* 1288 */       paramGraphics.drawLine(9, 11, 8, 11);
/* 1289 */       paramGraphics.drawLine(7, 12, 4, 12);
/* 1290 */       paramGraphics.drawLine(3, 11, 2, 11);
/*      */ 
/* 1293 */       if (bool) {
/* 1294 */         paramGraphics.setColor((Color)localObject1);
/* 1295 */         paramGraphics.fillRect(4, 4, 4, 4);
/* 1296 */         paramGraphics.drawLine(4, 3, 7, 3);
/* 1297 */         paramGraphics.drawLine(8, 4, 8, 7);
/* 1298 */         paramGraphics.drawLine(7, 8, 4, 8);
/* 1299 */         paramGraphics.drawLine(3, 7, 3, 4);
/*      */       }
/*      */ 
/* 1302 */       paramGraphics.translate(-paramInt1, -paramInt2);
/*      */     }
/*      */ 
/*      */     public int getIconWidth() {
/* 1306 */       return 13;
/*      */     }
/*      */ 
/*      */     public int getIconHeight() {
/* 1310 */       return 13;
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class RadioButtonMenuItemIcon
/*      */     implements Icon, UIResource, Serializable
/*      */   {
/*      */     public void paintOceanIcon(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2)
/*      */     {
/* 2090 */       ButtonModel localButtonModel = ((JMenuItem)paramComponent).getModel();
/* 2091 */       boolean bool1 = localButtonModel.isSelected();
/* 2092 */       boolean bool2 = localButtonModel.isEnabled();
/* 2093 */       boolean bool3 = localButtonModel.isPressed();
/* 2094 */       boolean bool4 = localButtonModel.isArmed();
/*      */ 
/* 2096 */       paramGraphics.translate(paramInt1, paramInt2);
/*      */ 
/* 2098 */       if (bool2) {
/* 2099 */         MetalUtils.drawGradient(paramComponent, paramGraphics, "RadioButtonMenuItem.gradient", 1, 1, 7, 7, true);
/*      */ 
/* 2101 */         if ((bool3) || (bool4)) {
/* 2102 */           paramGraphics.setColor(MetalLookAndFeel.getPrimaryControl());
/*      */         }
/*      */         else {
/* 2105 */           paramGraphics.setColor(MetalLookAndFeel.getControlHighlight());
/*      */         }
/* 2107 */         paramGraphics.drawLine(2, 9, 7, 9);
/* 2108 */         paramGraphics.drawLine(9, 2, 9, 7);
/* 2109 */         paramGraphics.drawLine(8, 8, 8, 8);
/*      */ 
/* 2111 */         if ((bool3) || (bool4)) {
/* 2112 */           paramGraphics.setColor(MetalLookAndFeel.getControlInfo());
/*      */         }
/*      */         else
/* 2115 */           paramGraphics.setColor(MetalLookAndFeel.getControlDarkShadow());
/*      */       }
/*      */       else
/*      */       {
/* 2119 */         paramGraphics.setColor(MetalLookAndFeel.getMenuDisabledForeground());
/*      */       }
/* 2121 */       paramGraphics.drawLine(2, 0, 6, 0);
/* 2122 */       paramGraphics.drawLine(2, 8, 6, 8);
/* 2123 */       paramGraphics.drawLine(0, 2, 0, 6);
/* 2124 */       paramGraphics.drawLine(8, 2, 8, 6);
/* 2125 */       paramGraphics.drawLine(1, 1, 1, 1);
/* 2126 */       paramGraphics.drawLine(7, 1, 7, 1);
/* 2127 */       paramGraphics.drawLine(1, 7, 1, 7);
/* 2128 */       paramGraphics.drawLine(7, 7, 7, 7);
/*      */ 
/* 2130 */       if (bool1) {
/* 2131 */         if (bool2) {
/* 2132 */           if ((bool4) || (((paramComponent instanceof JMenu)) && (localButtonModel.isSelected()))) {
/* 2133 */             paramGraphics.setColor(MetalLookAndFeel.getMenuSelectedForeground());
/*      */           }
/*      */           else
/*      */           {
/* 2137 */             paramGraphics.setColor(MetalLookAndFeel.getControlInfo());
/*      */           }
/*      */         }
/*      */         else {
/* 2141 */           paramGraphics.setColor(MetalLookAndFeel.getMenuDisabledForeground());
/*      */         }
/* 2143 */         paramGraphics.drawLine(3, 2, 5, 2);
/* 2144 */         paramGraphics.drawLine(2, 3, 6, 3);
/* 2145 */         paramGraphics.drawLine(2, 4, 6, 4);
/* 2146 */         paramGraphics.drawLine(2, 5, 6, 5);
/* 2147 */         paramGraphics.drawLine(3, 6, 5, 6);
/*      */       }
/*      */ 
/* 2150 */       paramGraphics.translate(-paramInt1, -paramInt2);
/*      */     }
/*      */ 
/*      */     public void paintIcon(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2)
/*      */     {
/* 2155 */       if (MetalLookAndFeel.usingOcean()) {
/* 2156 */         paintOceanIcon(paramComponent, paramGraphics, paramInt1, paramInt2);
/* 2157 */         return;
/*      */       }
/* 2159 */       JMenuItem localJMenuItem = (JMenuItem)paramComponent;
/* 2160 */       ButtonModel localButtonModel = localJMenuItem.getModel();
/*      */ 
/* 2162 */       boolean bool1 = localButtonModel.isSelected();
/* 2163 */       boolean bool2 = localButtonModel.isEnabled();
/* 2164 */       boolean bool3 = localButtonModel.isPressed();
/* 2165 */       boolean bool4 = localButtonModel.isArmed();
/*      */ 
/* 2167 */       paramGraphics.translate(paramInt1, paramInt2);
/*      */ 
/* 2169 */       if (bool2)
/*      */       {
/* 2171 */         if ((bool3) || (bool4))
/*      */         {
/* 2173 */           paramGraphics.setColor(MetalLookAndFeel.getPrimaryControl());
/* 2174 */           paramGraphics.drawLine(3, 1, 8, 1);
/* 2175 */           paramGraphics.drawLine(2, 9, 7, 9);
/* 2176 */           paramGraphics.drawLine(1, 3, 1, 8);
/* 2177 */           paramGraphics.drawLine(9, 2, 9, 7);
/* 2178 */           paramGraphics.drawLine(2, 2, 2, 2);
/* 2179 */           paramGraphics.drawLine(8, 8, 8, 8);
/*      */ 
/* 2181 */           paramGraphics.setColor(MetalLookAndFeel.getControlInfo());
/* 2182 */           paramGraphics.drawLine(2, 0, 6, 0);
/* 2183 */           paramGraphics.drawLine(2, 8, 6, 8);
/* 2184 */           paramGraphics.drawLine(0, 2, 0, 6);
/* 2185 */           paramGraphics.drawLine(8, 2, 8, 6);
/* 2186 */           paramGraphics.drawLine(1, 1, 1, 1);
/* 2187 */           paramGraphics.drawLine(7, 1, 7, 1);
/* 2188 */           paramGraphics.drawLine(1, 7, 1, 7);
/* 2189 */           paramGraphics.drawLine(7, 7, 7, 7);
/*      */         }
/*      */         else
/*      */         {
/* 2193 */           paramGraphics.setColor(MetalLookAndFeel.getControlHighlight());
/* 2194 */           paramGraphics.drawLine(3, 1, 8, 1);
/* 2195 */           paramGraphics.drawLine(2, 9, 7, 9);
/* 2196 */           paramGraphics.drawLine(1, 3, 1, 8);
/* 2197 */           paramGraphics.drawLine(9, 2, 9, 7);
/* 2198 */           paramGraphics.drawLine(2, 2, 2, 2);
/* 2199 */           paramGraphics.drawLine(8, 8, 8, 8);
/*      */ 
/* 2201 */           paramGraphics.setColor(MetalLookAndFeel.getControlDarkShadow());
/* 2202 */           paramGraphics.drawLine(2, 0, 6, 0);
/* 2203 */           paramGraphics.drawLine(2, 8, 6, 8);
/* 2204 */           paramGraphics.drawLine(0, 2, 0, 6);
/* 2205 */           paramGraphics.drawLine(8, 2, 8, 6);
/* 2206 */           paramGraphics.drawLine(1, 1, 1, 1);
/* 2207 */           paramGraphics.drawLine(7, 1, 7, 1);
/* 2208 */           paramGraphics.drawLine(1, 7, 1, 7);
/* 2209 */           paramGraphics.drawLine(7, 7, 7, 7);
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/* 2214 */         paramGraphics.setColor(MetalLookAndFeel.getMenuDisabledForeground());
/* 2215 */         paramGraphics.drawLine(2, 0, 6, 0);
/* 2216 */         paramGraphics.drawLine(2, 8, 6, 8);
/* 2217 */         paramGraphics.drawLine(0, 2, 0, 6);
/* 2218 */         paramGraphics.drawLine(8, 2, 8, 6);
/* 2219 */         paramGraphics.drawLine(1, 1, 1, 1);
/* 2220 */         paramGraphics.drawLine(7, 1, 7, 1);
/* 2221 */         paramGraphics.drawLine(1, 7, 1, 7);
/* 2222 */         paramGraphics.drawLine(7, 7, 7, 7);
/*      */       }
/*      */ 
/* 2225 */       if (bool1)
/*      */       {
/* 2227 */         if (bool2)
/*      */         {
/* 2229 */           if ((localButtonModel.isArmed()) || (((paramComponent instanceof JMenu)) && (localButtonModel.isSelected())))
/*      */           {
/* 2231 */             paramGraphics.setColor(MetalLookAndFeel.getMenuSelectedForeground());
/*      */           }
/*      */           else
/*      */           {
/* 2235 */             paramGraphics.setColor(localJMenuItem.getForeground());
/*      */           }
/*      */         }
/*      */         else
/*      */         {
/* 2240 */           paramGraphics.setColor(MetalLookAndFeel.getMenuDisabledForeground());
/*      */         }
/*      */ 
/* 2243 */         paramGraphics.drawLine(3, 2, 5, 2);
/* 2244 */         paramGraphics.drawLine(2, 3, 6, 3);
/* 2245 */         paramGraphics.drawLine(2, 4, 6, 4);
/* 2246 */         paramGraphics.drawLine(2, 5, 6, 5);
/* 2247 */         paramGraphics.drawLine(3, 6, 5, 6);
/*      */       }
/*      */ 
/* 2250 */       paramGraphics.translate(-paramInt1, -paramInt2);
/*      */     }
/*      */     public int getIconWidth() {
/* 2253 */       return MetalIconFactory.menuCheckIconSize.width;
/*      */     }
/* 2255 */     public int getIconHeight() { return MetalIconFactory.menuCheckIconSize.height; }
/*      */ 
/*      */   }
/*      */ 
/*      */   private static class TreeComputerIcon
/*      */     implements Icon, UIResource, Serializable
/*      */   {
/*      */     public void paintIcon(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2)
/*      */     {
/* 1317 */       paramGraphics.translate(paramInt1, paramInt2);
/*      */ 
/* 1320 */       paramGraphics.setColor(MetalLookAndFeel.getPrimaryControl());
/* 1321 */       paramGraphics.fillRect(5, 4, 6, 4);
/*      */ 
/* 1324 */       paramGraphics.setColor(MetalLookAndFeel.getPrimaryControlInfo());
/* 1325 */       paramGraphics.drawLine(2, 2, 2, 8);
/* 1326 */       paramGraphics.drawLine(13, 2, 13, 8);
/* 1327 */       paramGraphics.drawLine(3, 1, 12, 1);
/* 1328 */       paramGraphics.drawLine(12, 9, 12, 9);
/* 1329 */       paramGraphics.drawLine(3, 9, 3, 9);
/*      */ 
/* 1331 */       paramGraphics.drawLine(4, 4, 4, 7);
/* 1332 */       paramGraphics.drawLine(5, 3, 10, 3);
/* 1333 */       paramGraphics.drawLine(11, 4, 11, 7);
/* 1334 */       paramGraphics.drawLine(5, 8, 10, 8);
/*      */ 
/* 1336 */       paramGraphics.drawLine(1, 10, 14, 10);
/* 1337 */       paramGraphics.drawLine(14, 10, 14, 14);
/* 1338 */       paramGraphics.drawLine(1, 14, 14, 14);
/* 1339 */       paramGraphics.drawLine(1, 10, 1, 14);
/*      */ 
/* 1342 */       paramGraphics.setColor(MetalLookAndFeel.getControlDarkShadow());
/* 1343 */       paramGraphics.drawLine(6, 12, 8, 12);
/* 1344 */       paramGraphics.drawLine(10, 12, 12, 12);
/*      */ 
/* 1346 */       paramGraphics.translate(-paramInt1, -paramInt2);
/*      */     }
/*      */ 
/*      */     public int getIconWidth() {
/* 1350 */       return 16;
/*      */     }
/*      */ 
/*      */     public int getIconHeight() {
/* 1354 */       return 16;
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class TreeControlIcon
/*      */     implements Icon, Serializable
/*      */   {
/*      */     protected boolean isLight;
/*      */     MetalIconFactory.ImageCacher imageCacher;
/* 1756 */     transient boolean cachedOrientation = true;
/*      */ 
/*      */     public TreeControlIcon(boolean paramBoolean)
/*      */     {
/* 1751 */       this.isLight = paramBoolean;
/*      */     }
/*      */ 
/*      */     public void paintIcon(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2)
/*      */     {
/* 1760 */       GraphicsConfiguration localGraphicsConfiguration = paramComponent.getGraphicsConfiguration();
/*      */ 
/* 1762 */       if (this.imageCacher == null) {
/* 1763 */         this.imageCacher = new MetalIconFactory.ImageCacher();
/*      */       }
/* 1765 */       Object localObject = this.imageCacher.getImage(localGraphicsConfiguration);
/*      */ 
/* 1767 */       if ((localObject == null) || (this.cachedOrientation != MetalUtils.isLeftToRight(paramComponent))) {
/* 1768 */         this.cachedOrientation = MetalUtils.isLeftToRight(paramComponent);
/* 1769 */         if (localGraphicsConfiguration != null) {
/* 1770 */           localObject = localGraphicsConfiguration.createCompatibleImage(getIconWidth(), getIconHeight(), 2);
/*      */         }
/*      */         else
/*      */         {
/* 1774 */           localObject = new BufferedImage(getIconWidth(), getIconHeight(), 2);
/*      */         }
/*      */ 
/* 1778 */         Graphics localGraphics = ((Image)localObject).getGraphics();
/* 1779 */         paintMe(paramComponent, localGraphics, paramInt1, paramInt2);
/* 1780 */         localGraphics.dispose();
/* 1781 */         this.imageCacher.cacheImage((Image)localObject, localGraphicsConfiguration);
/*      */       }
/*      */ 
/* 1785 */       if (MetalUtils.isLeftToRight(paramComponent)) {
/* 1786 */         if (this.isLight) {
/* 1787 */           paramGraphics.drawImage((Image)localObject, paramInt1 + 5, paramInt2 + 3, paramInt1 + 18, paramInt2 + 13, 4, 3, 17, 13, null);
/*      */         }
/*      */         else
/*      */         {
/* 1791 */           paramGraphics.drawImage((Image)localObject, paramInt1 + 5, paramInt2 + 3, paramInt1 + 18, paramInt2 + 17, 4, 3, 17, 17, null);
/*      */         }
/*      */ 
/*      */       }
/* 1796 */       else if (this.isLight) {
/* 1797 */         paramGraphics.drawImage((Image)localObject, paramInt1 + 3, paramInt2 + 3, paramInt1 + 16, paramInt2 + 13, 4, 3, 17, 13, null);
/*      */       }
/*      */       else
/*      */       {
/* 1801 */         paramGraphics.drawImage((Image)localObject, paramInt1 + 3, paramInt2 + 3, paramInt1 + 16, paramInt2 + 17, 4, 3, 17, 17, null);
/*      */       }
/*      */     }
/*      */ 
/*      */     public void paintMe(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2)
/*      */     {
/* 1809 */       paramGraphics.setColor(MetalLookAndFeel.getPrimaryControlInfo());
/*      */ 
/* 1811 */       int i = MetalUtils.isLeftToRight(paramComponent) ? 0 : 4;
/*      */ 
/* 1814 */       paramGraphics.drawLine(i + 4, 6, i + 4, 9);
/* 1815 */       paramGraphics.drawLine(i + 5, 5, i + 5, 5);
/* 1816 */       paramGraphics.drawLine(i + 6, 4, i + 9, 4);
/* 1817 */       paramGraphics.drawLine(i + 10, 5, i + 10, 5);
/* 1818 */       paramGraphics.drawLine(i + 11, 6, i + 11, 9);
/* 1819 */       paramGraphics.drawLine(i + 10, 10, i + 10, 10);
/* 1820 */       paramGraphics.drawLine(i + 6, 11, i + 9, 11);
/* 1821 */       paramGraphics.drawLine(i + 5, 10, i + 5, 10);
/*      */ 
/* 1824 */       paramGraphics.drawLine(i + 7, 7, i + 8, 7);
/* 1825 */       paramGraphics.drawLine(i + 7, 8, i + 8, 8);
/*      */ 
/* 1828 */       if (this.isLight) {
/* 1829 */         if (MetalUtils.isLeftToRight(paramComponent)) {
/* 1830 */           paramGraphics.drawLine(12, 7, 15, 7);
/* 1831 */           paramGraphics.drawLine(12, 8, 15, 8);
/*      */         }
/*      */         else
/*      */         {
/* 1836 */           paramGraphics.drawLine(4, 7, 7, 7);
/* 1837 */           paramGraphics.drawLine(4, 8, 7, 8);
/*      */         }
/*      */       }
/*      */       else {
/* 1841 */         paramGraphics.drawLine(i + 7, 12, i + 7, 15);
/* 1842 */         paramGraphics.drawLine(i + 8, 12, i + 8, 15);
/*      */       }
/*      */ 
/* 1848 */       paramGraphics.setColor(MetalLookAndFeel.getPrimaryControlDarkShadow());
/* 1849 */       paramGraphics.drawLine(i + 5, 6, i + 5, 9);
/* 1850 */       paramGraphics.drawLine(i + 6, 5, i + 9, 5);
/*      */ 
/* 1852 */       paramGraphics.setColor(MetalLookAndFeel.getPrimaryControlShadow());
/* 1853 */       paramGraphics.drawLine(i + 6, 6, i + 6, 6);
/* 1854 */       paramGraphics.drawLine(i + 9, 6, i + 9, 6);
/* 1855 */       paramGraphics.drawLine(i + 6, 9, i + 6, 9);
/* 1856 */       paramGraphics.drawLine(i + 10, 6, i + 10, 9);
/* 1857 */       paramGraphics.drawLine(i + 6, 10, i + 9, 10);
/*      */ 
/* 1859 */       paramGraphics.setColor(MetalLookAndFeel.getPrimaryControl());
/* 1860 */       paramGraphics.drawLine(i + 6, 7, i + 6, 8);
/* 1861 */       paramGraphics.drawLine(i + 7, 6, i + 8, 6);
/* 1862 */       paramGraphics.drawLine(i + 9, 7, i + 9, 7);
/* 1863 */       paramGraphics.drawLine(i + 7, 9, i + 7, 9);
/*      */ 
/* 1865 */       paramGraphics.setColor(MetalLookAndFeel.getPrimaryControlHighlight());
/* 1866 */       paramGraphics.drawLine(i + 8, 9, i + 9, 9);
/* 1867 */       paramGraphics.drawLine(i + 9, 8, i + 9, 8);
/*      */     }
/*      */     public int getIconWidth() {
/* 1870 */       return MetalIconFactory.treeControlSize.width; } 
/* 1871 */     public int getIconHeight() { return MetalIconFactory.treeControlSize.height; }
/*      */ 
/*      */   }
/*      */ 
/*      */   private static class TreeFloppyDriveIcon
/*      */     implements Icon, UIResource, Serializable
/*      */   {
/*      */     public void paintIcon(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2)
/*      */     {
/* 1445 */       paramGraphics.translate(paramInt1, paramInt2);
/*      */ 
/* 1448 */       paramGraphics.setColor(MetalLookAndFeel.getPrimaryControl());
/* 1449 */       paramGraphics.fillRect(2, 2, 12, 12);
/*      */ 
/* 1452 */       paramGraphics.setColor(MetalLookAndFeel.getPrimaryControlInfo());
/* 1453 */       paramGraphics.drawLine(1, 1, 13, 1);
/* 1454 */       paramGraphics.drawLine(14, 2, 14, 14);
/* 1455 */       paramGraphics.drawLine(1, 14, 14, 14);
/* 1456 */       paramGraphics.drawLine(1, 1, 1, 14);
/*      */ 
/* 1459 */       paramGraphics.setColor(MetalLookAndFeel.getControlDarkShadow());
/* 1460 */       paramGraphics.fillRect(5, 2, 6, 5);
/* 1461 */       paramGraphics.drawLine(4, 8, 11, 8);
/* 1462 */       paramGraphics.drawLine(3, 9, 3, 13);
/* 1463 */       paramGraphics.drawLine(12, 9, 12, 13);
/*      */ 
/* 1466 */       paramGraphics.setColor(MetalLookAndFeel.getPrimaryControlHighlight());
/* 1467 */       paramGraphics.fillRect(8, 3, 2, 3);
/* 1468 */       paramGraphics.fillRect(4, 9, 8, 5);
/*      */ 
/* 1471 */       paramGraphics.setColor(MetalLookAndFeel.getPrimaryControlShadow());
/* 1472 */       paramGraphics.drawLine(5, 10, 9, 10);
/* 1473 */       paramGraphics.drawLine(5, 12, 8, 12);
/*      */ 
/* 1475 */       paramGraphics.translate(-paramInt1, -paramInt2);
/*      */     }
/*      */ 
/*      */     public int getIconWidth() {
/* 1479 */       return 16;
/*      */     }
/*      */ 
/*      */     public int getIconHeight() {
/* 1483 */       return 16;
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class TreeFolderIcon extends MetalIconFactory.FolderIcon16
/*      */   {
/*      */     public int getShift()
/*      */     {
/* 1640 */       return -1; } 
/* 1641 */     public int getAdditionalHeight() { return 2; }
/*      */ 
/*      */   }
/*      */ 
/*      */   private static class TreeHardDriveIcon
/*      */     implements Icon, UIResource, Serializable
/*      */   {
/*      */     public void paintIcon(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2)
/*      */     {
/* 1361 */       paramGraphics.translate(paramInt1, paramInt2);
/*      */ 
/* 1364 */       paramGraphics.setColor(MetalLookAndFeel.getPrimaryControlInfo());
/*      */ 
/* 1366 */       paramGraphics.drawLine(1, 4, 1, 5);
/* 1367 */       paramGraphics.drawLine(2, 3, 3, 3);
/* 1368 */       paramGraphics.drawLine(4, 2, 11, 2);
/* 1369 */       paramGraphics.drawLine(12, 3, 13, 3);
/* 1370 */       paramGraphics.drawLine(14, 4, 14, 5);
/* 1371 */       paramGraphics.drawLine(12, 6, 13, 6);
/* 1372 */       paramGraphics.drawLine(4, 7, 11, 7);
/* 1373 */       paramGraphics.drawLine(2, 6, 3, 6);
/*      */ 
/* 1375 */       paramGraphics.drawLine(1, 7, 1, 8);
/* 1376 */       paramGraphics.drawLine(2, 9, 3, 9);
/* 1377 */       paramGraphics.drawLine(4, 10, 11, 10);
/* 1378 */       paramGraphics.drawLine(12, 9, 13, 9);
/* 1379 */       paramGraphics.drawLine(14, 7, 14, 8);
/*      */ 
/* 1381 */       paramGraphics.drawLine(1, 10, 1, 11);
/* 1382 */       paramGraphics.drawLine(2, 12, 3, 12);
/* 1383 */       paramGraphics.drawLine(4, 13, 11, 13);
/* 1384 */       paramGraphics.drawLine(12, 12, 13, 12);
/* 1385 */       paramGraphics.drawLine(14, 10, 14, 11);
/*      */ 
/* 1388 */       paramGraphics.setColor(MetalLookAndFeel.getControlShadow());
/*      */ 
/* 1390 */       paramGraphics.drawLine(7, 6, 7, 6);
/* 1391 */       paramGraphics.drawLine(9, 6, 9, 6);
/* 1392 */       paramGraphics.drawLine(10, 5, 10, 5);
/* 1393 */       paramGraphics.drawLine(11, 6, 11, 6);
/* 1394 */       paramGraphics.drawLine(12, 5, 13, 5);
/* 1395 */       paramGraphics.drawLine(13, 4, 13, 4);
/*      */ 
/* 1397 */       paramGraphics.drawLine(7, 9, 7, 9);
/* 1398 */       paramGraphics.drawLine(9, 9, 9, 9);
/* 1399 */       paramGraphics.drawLine(10, 8, 10, 8);
/* 1400 */       paramGraphics.drawLine(11, 9, 11, 9);
/* 1401 */       paramGraphics.drawLine(12, 8, 13, 8);
/* 1402 */       paramGraphics.drawLine(13, 7, 13, 7);
/*      */ 
/* 1404 */       paramGraphics.drawLine(7, 12, 7, 12);
/* 1405 */       paramGraphics.drawLine(9, 12, 9, 12);
/* 1406 */       paramGraphics.drawLine(10, 11, 10, 11);
/* 1407 */       paramGraphics.drawLine(11, 12, 11, 12);
/* 1408 */       paramGraphics.drawLine(12, 11, 13, 11);
/* 1409 */       paramGraphics.drawLine(13, 10, 13, 10);
/*      */ 
/* 1412 */       paramGraphics.setColor(MetalLookAndFeel.getControlHighlight());
/*      */ 
/* 1414 */       paramGraphics.drawLine(4, 3, 5, 3);
/* 1415 */       paramGraphics.drawLine(7, 3, 9, 3);
/* 1416 */       paramGraphics.drawLine(11, 3, 11, 3);
/* 1417 */       paramGraphics.drawLine(2, 4, 6, 4);
/* 1418 */       paramGraphics.drawLine(8, 4, 8, 4);
/* 1419 */       paramGraphics.drawLine(2, 5, 3, 5);
/* 1420 */       paramGraphics.drawLine(4, 6, 4, 6);
/*      */ 
/* 1422 */       paramGraphics.drawLine(2, 7, 3, 7);
/* 1423 */       paramGraphics.drawLine(2, 8, 3, 8);
/* 1424 */       paramGraphics.drawLine(4, 9, 4, 9);
/*      */ 
/* 1426 */       paramGraphics.drawLine(2, 10, 3, 10);
/* 1427 */       paramGraphics.drawLine(2, 11, 3, 11);
/* 1428 */       paramGraphics.drawLine(4, 12, 4, 12);
/*      */ 
/* 1430 */       paramGraphics.translate(-paramInt1, -paramInt2);
/*      */     }
/*      */ 
/*      */     public int getIconWidth() {
/* 1434 */       return 16;
/*      */     }
/*      */ 
/*      */     public int getIconHeight() {
/* 1438 */       return 16;
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class TreeLeafIcon extends MetalIconFactory.FileIcon16
/*      */   {
/*      */     public int getShift()
/*      */     {
/* 1725 */       return 2; } 
/* 1726 */     public int getAdditionalHeight() { return 4; }
/*      */ 
/*      */   }
/*      */ 
/*      */   private static class VerticalSliderThumbIcon
/*      */     implements Icon, Serializable, UIResource
/*      */   {
/*      */     protected static MetalBumps controlBumps;
/*      */     protected static MetalBumps primaryBumps;
/*      */ 
/*      */     public VerticalSliderThumbIcon()
/*      */     {
/* 2264 */       controlBumps = new MetalBumps(6, 10, MetalLookAndFeel.getControlHighlight(), MetalLookAndFeel.getControlInfo(), MetalLookAndFeel.getControl());
/*      */ 
/* 2268 */       primaryBumps = new MetalBumps(6, 10, MetalLookAndFeel.getPrimaryControl(), MetalLookAndFeel.getPrimaryControlDarkShadow(), MetalLookAndFeel.getPrimaryControlShadow());
/*      */     }
/*      */ 
/*      */     public void paintIcon(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2)
/*      */     {
/* 2275 */       boolean bool = MetalUtils.isLeftToRight(paramComponent);
/*      */ 
/* 2277 */       paramGraphics.translate(paramInt1, paramInt2);
/*      */ 
/* 2280 */       if (paramComponent.hasFocus()) {
/* 2281 */         paramGraphics.setColor(MetalLookAndFeel.getPrimaryControlInfo());
/*      */       }
/*      */       else {
/* 2284 */         paramGraphics.setColor(paramComponent.isEnabled() ? MetalLookAndFeel.getPrimaryControlInfo() : MetalLookAndFeel.getControlDarkShadow());
/*      */       }
/*      */ 
/* 2288 */       if (bool) {
/* 2289 */         paramGraphics.drawLine(1, 0, 8, 0);
/* 2290 */         paramGraphics.drawLine(0, 1, 0, 13);
/* 2291 */         paramGraphics.drawLine(1, 14, 8, 14);
/* 2292 */         paramGraphics.drawLine(9, 1, 15, 7);
/* 2293 */         paramGraphics.drawLine(9, 13, 15, 7);
/*      */       }
/*      */       else {
/* 2296 */         paramGraphics.drawLine(7, 0, 14, 0);
/* 2297 */         paramGraphics.drawLine(15, 1, 15, 13);
/* 2298 */         paramGraphics.drawLine(7, 14, 14, 14);
/* 2299 */         paramGraphics.drawLine(0, 7, 6, 1);
/* 2300 */         paramGraphics.drawLine(0, 7, 6, 13);
/*      */       }
/*      */ 
/* 2304 */       if (paramComponent.hasFocus()) {
/* 2305 */         paramGraphics.setColor(paramComponent.getForeground());
/*      */       }
/*      */       else {
/* 2308 */         paramGraphics.setColor(MetalLookAndFeel.getControl());
/*      */       }
/*      */ 
/* 2311 */       if (bool) {
/* 2312 */         paramGraphics.fillRect(1, 1, 8, 13);
/*      */ 
/* 2314 */         paramGraphics.drawLine(9, 2, 9, 12);
/* 2315 */         paramGraphics.drawLine(10, 3, 10, 11);
/* 2316 */         paramGraphics.drawLine(11, 4, 11, 10);
/* 2317 */         paramGraphics.drawLine(12, 5, 12, 9);
/* 2318 */         paramGraphics.drawLine(13, 6, 13, 8);
/* 2319 */         paramGraphics.drawLine(14, 7, 14, 7);
/*      */       }
/*      */       else {
/* 2322 */         paramGraphics.fillRect(7, 1, 8, 13);
/*      */ 
/* 2324 */         paramGraphics.drawLine(6, 3, 6, 12);
/* 2325 */         paramGraphics.drawLine(5, 4, 5, 11);
/* 2326 */         paramGraphics.drawLine(4, 5, 4, 10);
/* 2327 */         paramGraphics.drawLine(3, 6, 3, 9);
/* 2328 */         paramGraphics.drawLine(2, 7, 2, 8);
/*      */       }
/*      */ 
/* 2332 */       int i = bool ? 2 : 8;
/* 2333 */       if (paramComponent.isEnabled()) {
/* 2334 */         if (paramComponent.hasFocus()) {
/* 2335 */           primaryBumps.paintIcon(paramComponent, paramGraphics, i, 2);
/*      */         }
/*      */         else {
/* 2338 */           controlBumps.paintIcon(paramComponent, paramGraphics, i, 2);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 2343 */       if (paramComponent.isEnabled()) {
/* 2344 */         paramGraphics.setColor(paramComponent.hasFocus() ? MetalLookAndFeel.getPrimaryControl() : MetalLookAndFeel.getControlHighlight());
/*      */ 
/* 2346 */         if (bool) {
/* 2347 */           paramGraphics.drawLine(1, 1, 8, 1);
/* 2348 */           paramGraphics.drawLine(1, 1, 1, 13);
/*      */         }
/*      */         else {
/* 2351 */           paramGraphics.drawLine(8, 1, 14, 1);
/* 2352 */           paramGraphics.drawLine(1, 7, 7, 1);
/*      */         }
/*      */       }
/*      */ 
/* 2356 */       paramGraphics.translate(-paramInt1, -paramInt2);
/*      */     }
/*      */ 
/*      */     public int getIconWidth() {
/* 2360 */       return 16;
/*      */     }
/*      */ 
/*      */     public int getIconHeight() {
/* 2364 */       return 15;
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.metal.MetalIconFactory
 * JD-Core Version:    0.6.2
 */