/*     */ package sun.swing.table;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.FontMetrics;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Insets;
/*     */ import java.awt.Point;
/*     */ import java.awt.Rectangle;
/*     */ import java.io.Serializable;
/*     */ import java.util.List;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.JTable;
/*     */ import javax.swing.RowSorter;
/*     */ import javax.swing.RowSorter.SortKey;
/*     */ import javax.swing.SortOrder;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.border.Border;
/*     */ import javax.swing.plaf.UIResource;
/*     */ import javax.swing.table.DefaultTableCellRenderer;
/*     */ import javax.swing.table.JTableHeader;
/*     */ import sun.swing.DefaultLookup;
/*     */ 
/*     */ public class DefaultTableCellHeaderRenderer extends DefaultTableCellRenderer
/*     */   implements UIResource
/*     */ {
/*     */   private boolean horizontalTextPositionSet;
/*     */   private Icon sortArrow;
/*  46 */   private EmptyIcon emptyIcon = new EmptyIcon(null);
/*     */ 
/*     */   public DefaultTableCellHeaderRenderer() {
/*  49 */     setHorizontalAlignment(0);
/*     */   }
/*     */ 
/*     */   public void setHorizontalTextPosition(int paramInt) {
/*  53 */     this.horizontalTextPositionSet = true;
/*  54 */     super.setHorizontalTextPosition(paramInt);
/*     */   }
/*     */ 
/*     */   public Component getTableCellRendererComponent(JTable paramJTable, Object paramObject, boolean paramBoolean1, boolean paramBoolean2, int paramInt1, int paramInt2)
/*     */   {
/*  59 */     Icon localIcon = null;
/*     */ 
/*  61 */     boolean bool = false;
/*     */ 
/*  63 */     if (paramJTable != null) {
/*  64 */       localObject1 = paramJTable.getTableHeader();
/*     */       Object localObject2;
/*  65 */       if (localObject1 != null) {
/*  66 */         localObject2 = null;
/*  67 */         Color localColor = null;
/*  68 */         if (paramBoolean2) {
/*  69 */           localObject2 = DefaultLookup.getColor(this, this.ui, "TableHeader.focusCellForeground");
/*  70 */           localColor = DefaultLookup.getColor(this, this.ui, "TableHeader.focusCellBackground");
/*     */         }
/*  72 */         if (localObject2 == null) {
/*  73 */           localObject2 = ((JTableHeader)localObject1).getForeground();
/*     */         }
/*  75 */         if (localColor == null) {
/*  76 */           localColor = ((JTableHeader)localObject1).getBackground();
/*     */         }
/*  78 */         setForeground((Color)localObject2);
/*  79 */         setBackground(localColor);
/*     */ 
/*  81 */         setFont(((JTableHeader)localObject1).getFont());
/*     */ 
/*  83 */         bool = ((JTableHeader)localObject1).isPaintingForPrint();
/*     */       }
/*     */ 
/*  86 */       if ((!bool) && (paramJTable.getRowSorter() != null)) {
/*  87 */         if (!this.horizontalTextPositionSet)
/*     */         {
/*  90 */           setHorizontalTextPosition(10);
/*     */         }
/*  92 */         localObject2 = getColumnSortOrder(paramJTable, paramInt2);
/*  93 */         if (localObject2 != null) {
/*  94 */           switch (1.$SwitchMap$javax$swing$SortOrder[localObject2.ordinal()]) {
/*     */           case 1:
/*  96 */             localIcon = DefaultLookup.getIcon(this, this.ui, "Table.ascendingSortIcon");
/*     */ 
/*  98 */             break;
/*     */           case 2:
/* 100 */             localIcon = DefaultLookup.getIcon(this, this.ui, "Table.descendingSortIcon");
/*     */ 
/* 102 */             break;
/*     */           case 3:
/* 104 */             localIcon = DefaultLookup.getIcon(this, this.ui, "Table.naturalSortIcon");
/*     */           }
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 112 */     setText(paramObject == null ? "" : paramObject.toString());
/* 113 */     setIcon(localIcon);
/* 114 */     this.sortArrow = localIcon;
/*     */ 
/* 116 */     Object localObject1 = null;
/* 117 */     if (paramBoolean2) {
/* 118 */       localObject1 = DefaultLookup.getBorder(this, this.ui, "TableHeader.focusCellBorder");
/*     */     }
/* 120 */     if (localObject1 == null) {
/* 121 */       localObject1 = DefaultLookup.getBorder(this, this.ui, "TableHeader.cellBorder");
/*     */     }
/* 123 */     setBorder((Border)localObject1);
/*     */ 
/* 125 */     return this;
/*     */   }
/*     */ 
/*     */   public static SortOrder getColumnSortOrder(JTable paramJTable, int paramInt) {
/* 129 */     SortOrder localSortOrder = null;
/* 130 */     if ((paramJTable == null) || (paramJTable.getRowSorter() == null)) {
/* 131 */       return localSortOrder;
/*     */     }
/* 133 */     List localList = paramJTable.getRowSorter().getSortKeys();
/*     */ 
/* 135 */     if ((localList.size() > 0) && (((RowSorter.SortKey)localList.get(0)).getColumn() == paramJTable.convertColumnIndexToModel(paramInt)))
/*     */     {
/* 137 */       localSortOrder = ((RowSorter.SortKey)localList.get(0)).getSortOrder();
/*     */     }
/* 139 */     return localSortOrder;
/*     */   }
/*     */ 
/*     */   public void paintComponent(Graphics paramGraphics)
/*     */   {
/* 144 */     boolean bool = DefaultLookup.getBoolean(this, this.ui, "TableHeader.rightAlignSortArrow", false);
/*     */ 
/* 146 */     if ((bool) && (this.sortArrow != null))
/*     */     {
/* 151 */       this.emptyIcon.width = this.sortArrow.getIconWidth();
/* 152 */       this.emptyIcon.height = this.sortArrow.getIconHeight();
/* 153 */       setIcon(this.emptyIcon);
/* 154 */       super.paintComponent(paramGraphics);
/* 155 */       Point localPoint = computeIconPosition(paramGraphics);
/* 156 */       this.sortArrow.paintIcon(this, paramGraphics, localPoint.x, localPoint.y);
/*     */     } else {
/* 158 */       super.paintComponent(paramGraphics);
/*     */     }
/*     */   }
/*     */ 
/*     */   private Point computeIconPosition(Graphics paramGraphics) {
/* 163 */     FontMetrics localFontMetrics = paramGraphics.getFontMetrics();
/* 164 */     Rectangle localRectangle1 = new Rectangle();
/* 165 */     Rectangle localRectangle2 = new Rectangle();
/* 166 */     Rectangle localRectangle3 = new Rectangle();
/* 167 */     Insets localInsets = getInsets();
/* 168 */     localRectangle1.x = localInsets.left;
/* 169 */     localRectangle1.y = localInsets.top;
/* 170 */     localRectangle1.width = (getWidth() - (localInsets.left + localInsets.right));
/* 171 */     localRectangle1.height = (getHeight() - (localInsets.top + localInsets.bottom));
/* 172 */     SwingUtilities.layoutCompoundLabel(this, localFontMetrics, getText(), this.sortArrow, getVerticalAlignment(), getHorizontalAlignment(), getVerticalTextPosition(), getHorizontalTextPosition(), localRectangle1, localRectangle3, localRectangle2, getIconTextGap());
/*     */ 
/* 185 */     int i = getWidth() - localInsets.right - this.sortArrow.getIconWidth();
/* 186 */     int j = localRectangle3.y;
/* 187 */     return new Point(i, j);
/*     */   }
/*     */ 
/*     */   private class EmptyIcon implements Icon, Serializable {
/* 191 */     int width = 0;
/* 192 */     int height = 0;
/*     */ 
/*     */     private EmptyIcon() {  } 
/*     */     public void paintIcon(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2) {  } 
/* 194 */     public int getIconWidth() { return this.width; } 
/* 195 */     public int getIconHeight() { return this.height; }
/*     */ 
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.swing.table.DefaultTableCellHeaderRenderer
 * JD-Core Version:    0.6.2
 */