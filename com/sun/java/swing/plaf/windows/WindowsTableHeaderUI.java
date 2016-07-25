/*     */ package com.sun.java.swing.plaf.windows;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Insets;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JTable;
/*     */ import javax.swing.SortOrder;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.border.Border;
/*     */ import javax.swing.border.EmptyBorder;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.UIResource;
/*     */ import javax.swing.plaf.basic.BasicTableHeaderUI;
/*     */ import javax.swing.table.JTableHeader;
/*     */ import javax.swing.table.TableCellRenderer;
/*     */ import javax.swing.table.TableColumn;
/*     */ import sun.swing.SwingUtilities2;
/*     */ import sun.swing.table.DefaultTableCellHeaderRenderer;
/*     */ 
/*     */ public class WindowsTableHeaderUI extends BasicTableHeaderUI
/*     */ {
/*     */   private TableCellRenderer originalHeaderRenderer;
/*     */ 
/*     */   public static ComponentUI createUI(JComponent paramJComponent)
/*     */   {
/*  45 */     return new WindowsTableHeaderUI();
/*     */   }
/*     */ 
/*     */   public void installUI(JComponent paramJComponent) {
/*  49 */     super.installUI(paramJComponent);
/*     */ 
/*  51 */     if (XPStyle.getXP() != null) {
/*  52 */       this.originalHeaderRenderer = this.header.getDefaultRenderer();
/*  53 */       if ((this.originalHeaderRenderer instanceof UIResource))
/*  54 */         this.header.setDefaultRenderer(new XPDefaultRenderer());
/*     */     }
/*     */   }
/*     */ 
/*     */   public void uninstallUI(JComponent paramJComponent)
/*     */   {
/*  60 */     if ((this.header.getDefaultRenderer() instanceof XPDefaultRenderer)) {
/*  61 */       this.header.setDefaultRenderer(this.originalHeaderRenderer);
/*     */     }
/*  63 */     super.uninstallUI(paramJComponent);
/*     */   }
/*     */ 
/*     */   protected void rolloverColumnUpdated(int paramInt1, int paramInt2)
/*     */   {
/*  68 */     if (XPStyle.getXP() != null) {
/*  69 */       this.header.repaint(this.header.getHeaderRect(paramInt1));
/*  70 */       this.header.repaint(this.header.getHeaderRect(paramInt2));
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class IconBorder
/*     */     implements Border, UIResource
/*     */   {
/*     */     private final Icon icon;
/*     */     private final int top;
/*     */     private final int left;
/*     */     private final int bottom;
/*     */     private final int right;
/*     */ 
/*     */     public IconBorder(Icon paramIcon, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */     {
/* 227 */       this.icon = paramIcon;
/* 228 */       this.top = paramInt1;
/* 229 */       this.left = paramInt2;
/* 230 */       this.bottom = paramInt3;
/* 231 */       this.right = paramInt4;
/*     */     }
/*     */     public Insets getBorderInsets(Component paramComponent) {
/* 234 */       return new Insets(this.icon.getIconHeight() + this.top, this.left, this.bottom, this.right);
/*     */     }
/*     */     public boolean isBorderOpaque() {
/* 237 */       return false;
/*     */     }
/*     */ 
/*     */     public void paintBorder(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
/* 241 */       this.icon.paintIcon(paramComponent, paramGraphics, paramInt1 + this.left + (paramInt3 - this.left - this.right - this.icon.getIconWidth()) / 2, paramInt2 + this.top);
/*     */     }
/*     */   }
/*     */ 
/*     */   private class XPDefaultRenderer extends DefaultTableCellHeaderRenderer
/*     */   {
/*     */     XPStyle.Skin skin;
/*     */     boolean isSelected;
/*     */     boolean hasFocus;
/*     */     boolean hasRollover;
/*     */     int column;
/*     */ 
/*     */     XPDefaultRenderer()
/*     */     {
/*  80 */       setHorizontalAlignment(10);
/*     */     }
/*     */ 
/*     */     public Component getTableCellRendererComponent(JTable paramJTable, Object paramObject, boolean paramBoolean1, boolean paramBoolean2, int paramInt1, int paramInt2)
/*     */     {
/*  86 */       super.getTableCellRendererComponent(paramJTable, paramObject, paramBoolean1, paramBoolean2, paramInt1, paramInt2);
/*     */ 
/*  88 */       this.isSelected = paramBoolean1;
/*  89 */       this.hasFocus = paramBoolean2;
/*  90 */       this.column = paramInt2;
/*  91 */       this.hasRollover = (paramInt2 == WindowsTableHeaderUI.this.getRolloverColumn());
/*  92 */       if (this.skin == null) {
/*  93 */         localXPStyle = XPStyle.getXP();
/*  94 */         this.skin = (localXPStyle != null ? localXPStyle.getSkin(WindowsTableHeaderUI.this.header, TMSchema.Part.HP_HEADERITEM) : null);
/*     */       }
/*  96 */       XPStyle localXPStyle = this.skin != null ? this.skin.getContentMargin() : null;
/*  97 */       Object localObject = null;
/*  98 */       int i = 0;
/*  99 */       int j = 0;
/* 100 */       int k = 0;
/* 101 */       int m = 0;
/* 102 */       if (localXPStyle != null) {
/* 103 */         i = localXPStyle.top;
/* 104 */         j = localXPStyle.left;
/* 105 */         k = localXPStyle.bottom;
/* 106 */         m = localXPStyle.right;
/*     */       }
/*     */ 
/* 114 */       j += 5;
/* 115 */       k += 4;
/* 116 */       m += 5;
/*     */       Icon localIcon;
/* 122 */       if ((WindowsLookAndFeel.isOnVista()) && ((((localIcon = getIcon()) instanceof UIResource)) || (localIcon == null)))
/*     */       {
/* 125 */         i++;
/* 126 */         setIcon(null);
/* 127 */         localIcon = null;
/* 128 */         SortOrder localSortOrder = getColumnSortOrder(paramJTable, paramInt2);
/*     */ 
/* 130 */         if (localSortOrder != null) {
/* 131 */           switch (WindowsTableHeaderUI.1.$SwitchMap$javax$swing$SortOrder[localSortOrder.ordinal()]) {
/*     */           case 1:
/* 133 */             localIcon = UIManager.getIcon("Table.ascendingSortIcon");
/*     */ 
/* 135 */             break;
/*     */           case 2:
/* 137 */             localIcon = UIManager.getIcon("Table.descendingSortIcon");
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/* 142 */         if (localIcon != null) {
/* 143 */           k = localIcon.getIconHeight();
/* 144 */           localObject = new WindowsTableHeaderUI.IconBorder(localIcon, i, j, k, m);
/*     */         }
/*     */         else {
/* 147 */           localIcon = UIManager.getIcon("Table.ascendingSortIcon");
/*     */ 
/* 149 */           int n = localIcon != null ? localIcon.getIconHeight() : 0;
/*     */ 
/* 151 */           if (n != 0) {
/* 152 */             k = n;
/*     */           }
/* 154 */           localObject = new EmptyBorder(n + i, j, k, m);
/*     */         }
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/* 160 */         i += 3;
/* 161 */         localObject = new EmptyBorder(i, j, k, m);
/*     */       }
/*     */ 
/* 164 */       setBorder((Border)localObject);
/* 165 */       return this;
/*     */     }
/*     */ 
/*     */     public void paint(Graphics paramGraphics) {
/* 169 */       Dimension localDimension = getSize();
/* 170 */       TMSchema.State localState = TMSchema.State.NORMAL;
/* 171 */       TableColumn localTableColumn = WindowsTableHeaderUI.this.header.getDraggedColumn();
/* 172 */       if ((localTableColumn != null) && (this.column == SwingUtilities2.convertColumnIndexToView(WindowsTableHeaderUI.this.header.getColumnModel(), localTableColumn.getModelIndex())))
/*     */       {
/* 175 */         localState = TMSchema.State.PRESSED;
/* 176 */       } else if ((this.isSelected) || (this.hasFocus) || (this.hasRollover)) {
/* 177 */         localState = TMSchema.State.HOT;
/*     */       }
/*     */ 
/* 180 */       if (WindowsLookAndFeel.isOnVista()) {
/* 181 */         SortOrder localSortOrder = getColumnSortOrder(WindowsTableHeaderUI.this.header.getTable(), this.column);
/* 182 */         if (localSortOrder != null) {
/* 183 */           switch (WindowsTableHeaderUI.1.$SwitchMap$javax$swing$SortOrder[localSortOrder.ordinal()])
/*     */           {
/*     */           case 1:
/*     */           case 2:
/* 187 */             switch (WindowsTableHeaderUI.1.$SwitchMap$com$sun$java$swing$plaf$windows$TMSchema$State[localState.ordinal()]) {
/*     */             case 1:
/* 189 */               localState = TMSchema.State.SORTEDNORMAL;
/* 190 */               break;
/*     */             case 2:
/* 192 */               localState = TMSchema.State.SORTEDPRESSED;
/* 193 */               break;
/*     */             case 3:
/* 195 */               localState = TMSchema.State.SORTEDHOT;
/* 196 */             }break;
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 205 */       this.skin.paintSkin(paramGraphics, 0, 0, localDimension.width - 1, localDimension.height - 1, localState);
/* 206 */       super.paint(paramGraphics);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.java.swing.plaf.windows.WindowsTableHeaderUI
 * JD-Core Version:    0.6.2
 */