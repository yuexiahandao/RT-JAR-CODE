/*     */ package javax.swing.plaf.basic;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Polygon;
/*     */ import java.io.Serializable;
/*     */ import javax.swing.AbstractButton;
/*     */ import javax.swing.ButtonModel;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.plaf.UIResource;
/*     */ 
/*     */ public class BasicIconFactory
/*     */   implements Serializable
/*     */ {
/*     */   private static Icon frame_icon;
/*     */   private static Icon checkBoxIcon;
/*     */   private static Icon radioButtonIcon;
/*     */   private static Icon checkBoxMenuItemIcon;
/*     */   private static Icon radioButtonMenuItemIcon;
/*     */   private static Icon menuItemCheckIcon;
/*     */   private static Icon menuItemArrowIcon;
/*     */   private static Icon menuArrowIcon;
/*     */ 
/*     */   public static Icon getMenuItemCheckIcon()
/*     */   {
/*  64 */     if (menuItemCheckIcon == null) {
/*  65 */       menuItemCheckIcon = new MenuItemCheckIcon(null);
/*     */     }
/*  67 */     return menuItemCheckIcon;
/*     */   }
/*     */ 
/*     */   public static Icon getMenuItemArrowIcon() {
/*  71 */     if (menuItemArrowIcon == null) {
/*  72 */       menuItemArrowIcon = new MenuItemArrowIcon(null);
/*     */     }
/*  74 */     return menuItemArrowIcon;
/*     */   }
/*     */ 
/*     */   public static Icon getMenuArrowIcon() {
/*  78 */     if (menuArrowIcon == null) {
/*  79 */       menuArrowIcon = new MenuArrowIcon(null);
/*     */     }
/*  81 */     return menuArrowIcon;
/*     */   }
/*     */ 
/*     */   public static Icon getCheckBoxIcon() {
/*  85 */     if (checkBoxIcon == null) {
/*  86 */       checkBoxIcon = new CheckBoxIcon(null);
/*     */     }
/*  88 */     return checkBoxIcon;
/*     */   }
/*     */ 
/*     */   public static Icon getRadioButtonIcon() {
/*  92 */     if (radioButtonIcon == null) {
/*  93 */       radioButtonIcon = new RadioButtonIcon(null);
/*     */     }
/*  95 */     return radioButtonIcon;
/*     */   }
/*     */ 
/*     */   public static Icon getCheckBoxMenuItemIcon() {
/*  99 */     if (checkBoxMenuItemIcon == null) {
/* 100 */       checkBoxMenuItemIcon = new CheckBoxMenuItemIcon(null);
/*     */     }
/* 102 */     return checkBoxMenuItemIcon;
/*     */   }
/*     */ 
/*     */   public static Icon getRadioButtonMenuItemIcon() {
/* 106 */     if (radioButtonMenuItemIcon == null) {
/* 107 */       radioButtonMenuItemIcon = new RadioButtonMenuItemIcon(null);
/*     */     }
/* 109 */     return radioButtonMenuItemIcon;
/*     */   }
/*     */ 
/*     */   public static Icon createEmptyFrameIcon() {
/* 113 */     if (frame_icon == null)
/* 114 */       frame_icon = new EmptyFrameIcon(null);
/* 115 */     return frame_icon;
/*     */   }
/*     */ 
/*     */   private static class CheckBoxIcon
/*     */     implements Icon, Serializable
/*     */   {
/*     */     static final int csize = 13;
/*     */ 
/*     */     public void paintIcon(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2)
/*     */     {
/*     */     }
/*     */ 
/*     */     public int getIconWidth()
/*     */     {
/* 134 */       return 13;
/*     */     }
/*     */ 
/*     */     public int getIconHeight() {
/* 138 */       return 13;
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class CheckBoxMenuItemIcon
/*     */     implements Icon, UIResource, Serializable
/*     */   {
/*     */     public void paintIcon(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2)
/*     */     {
/* 160 */       AbstractButton localAbstractButton = (AbstractButton)paramComponent;
/* 161 */       ButtonModel localButtonModel = localAbstractButton.getModel();
/* 162 */       boolean bool = localButtonModel.isSelected();
/* 163 */       if (bool) {
/* 164 */         paramGraphics.drawLine(paramInt1 + 7, paramInt2 + 1, paramInt1 + 7, paramInt2 + 3);
/* 165 */         paramGraphics.drawLine(paramInt1 + 6, paramInt2 + 2, paramInt1 + 6, paramInt2 + 4);
/* 166 */         paramGraphics.drawLine(paramInt1 + 5, paramInt2 + 3, paramInt1 + 5, paramInt2 + 5);
/* 167 */         paramGraphics.drawLine(paramInt1 + 4, paramInt2 + 4, paramInt1 + 4, paramInt2 + 6);
/* 168 */         paramGraphics.drawLine(paramInt1 + 3, paramInt2 + 5, paramInt1 + 3, paramInt2 + 7);
/* 169 */         paramGraphics.drawLine(paramInt1 + 2, paramInt2 + 4, paramInt1 + 2, paramInt2 + 6);
/* 170 */         paramGraphics.drawLine(paramInt1 + 1, paramInt2 + 3, paramInt1 + 1, paramInt2 + 5);
/*     */       }
/*     */     }
/* 173 */     public int getIconWidth() { return 9; } 
/* 174 */     public int getIconHeight() { return 9; }
/*     */ 
/*     */   }
/*     */ 
/*     */   private static class EmptyFrameIcon
/*     */     implements Icon, Serializable
/*     */   {
/* 119 */     int height = 16;
/* 120 */     int width = 14;
/*     */ 
/*     */     public void paintIcon(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2) {  } 
/* 123 */     public int getIconWidth() { return this.width; } 
/* 124 */     public int getIconHeight() { return this.height; }
/*     */ 
/*     */   }
/*     */ 
/*     */   private static class MenuArrowIcon
/*     */     implements Icon, UIResource, Serializable
/*     */   {
/*     */     public void paintIcon(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2)
/*     */     {
/* 212 */       Polygon localPolygon = new Polygon();
/* 213 */       localPolygon.addPoint(paramInt1, paramInt2);
/* 214 */       localPolygon.addPoint(paramInt1 + getIconWidth(), paramInt2 + getIconHeight() / 2);
/* 215 */       localPolygon.addPoint(paramInt1, paramInt2 + getIconHeight());
/* 216 */       paramGraphics.fillPolygon(localPolygon);
/*     */     }
/*     */     public int getIconWidth() {
/* 219 */       return 4; } 
/* 220 */     public int getIconHeight() { return 8; }
/*     */ 
/*     */   }
/*     */ 
/*     */   private static class MenuItemArrowIcon
/*     */     implements Icon, UIResource, Serializable
/*     */   {
/*     */     public void paintIcon(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2)
/*     */     {
/*     */     }
/*     */ 
/*     */     public int getIconWidth()
/*     */     {
/* 205 */       return 4; } 
/* 206 */     public int getIconHeight() { return 8; }
/*     */ 
/*     */   }
/*     */ 
/*     */   private static class MenuItemCheckIcon
/*     */     implements Icon, UIResource, Serializable
/*     */   {
/*     */     public void paintIcon(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2)
/*     */     {
/*     */     }
/*     */ 
/*     */     public int getIconWidth()
/*     */     {
/* 197 */       return 9; } 
/* 198 */     public int getIconHeight() { return 9; }
/*     */ 
/*     */   }
/*     */ 
/*     */   private static class RadioButtonIcon
/*     */     implements Icon, UIResource, Serializable
/*     */   {
/*     */     public void paintIcon(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2)
/*     */     {
/*     */     }
/*     */ 
/*     */     public int getIconWidth()
/*     */     {
/* 148 */       return 13;
/*     */     }
/*     */ 
/*     */     public int getIconHeight() {
/* 152 */       return 13;
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class RadioButtonMenuItemIcon
/*     */     implements Icon, UIResource, Serializable
/*     */   {
/*     */     public void paintIcon(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2)
/*     */     {
/* 182 */       AbstractButton localAbstractButton = (AbstractButton)paramComponent;
/* 183 */       ButtonModel localButtonModel = localAbstractButton.getModel();
/* 184 */       if (localAbstractButton.isSelected() == true)
/* 185 */         paramGraphics.fillOval(paramInt1 + 1, paramInt2 + 1, getIconWidth(), getIconHeight()); 
/*     */     }
/*     */ 
/* 188 */     public int getIconWidth() { return 6; } 
/* 189 */     public int getIconHeight() { return 6; }
/*     */ 
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.basic.BasicIconFactory
 * JD-Core Version:    0.6.2
 */