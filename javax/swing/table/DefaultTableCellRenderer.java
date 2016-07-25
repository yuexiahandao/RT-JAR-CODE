/*     */ package javax.swing.table;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Container;
/*     */ import java.awt.Rectangle;
/*     */ import java.io.Serializable;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JTable;
/*     */ import javax.swing.JTable.DropLocation;
/*     */ import javax.swing.border.Border;
/*     */ import javax.swing.border.EmptyBorder;
/*     */ import javax.swing.plaf.UIResource;
/*     */ import sun.swing.DefaultLookup;
/*     */ 
/*     */ public class DefaultTableCellRenderer extends JLabel
/*     */   implements TableCellRenderer, Serializable
/*     */ {
/*  95 */   private static final Border SAFE_NO_FOCUS_BORDER = new EmptyBorder(1, 1, 1, 1);
/*  96 */   private static final Border DEFAULT_NO_FOCUS_BORDER = new EmptyBorder(1, 1, 1, 1);
/*  97 */   protected static Border noFocusBorder = DEFAULT_NO_FOCUS_BORDER;
/*     */   private Color unselectedForeground;
/*     */   private Color unselectedBackground;
/*     */ 
/*     */   public DefaultTableCellRenderer()
/*     */   {
/* 111 */     setOpaque(true);
/* 112 */     setBorder(getNoFocusBorder());
/* 113 */     setName("Table.cellRenderer");
/*     */   }
/*     */ 
/*     */   private Border getNoFocusBorder() {
/* 117 */     Border localBorder = DefaultLookup.getBorder(this, this.ui, "Table.cellNoFocusBorder");
/* 118 */     if (System.getSecurityManager() != null) {
/* 119 */       if (localBorder != null) return localBorder;
/* 120 */       return SAFE_NO_FOCUS_BORDER;
/* 121 */     }if ((localBorder != null) && (
/* 122 */       (noFocusBorder == null) || (noFocusBorder == DEFAULT_NO_FOCUS_BORDER))) {
/* 123 */       return localBorder;
/*     */     }
/*     */ 
/* 126 */     return noFocusBorder;
/*     */   }
/*     */ 
/*     */   public void setForeground(Color paramColor)
/*     */   {
/* 136 */     super.setForeground(paramColor);
/* 137 */     this.unselectedForeground = paramColor;
/*     */   }
/*     */ 
/*     */   public void setBackground(Color paramColor)
/*     */   {
/* 147 */     super.setBackground(paramColor);
/* 148 */     this.unselectedBackground = paramColor;
/*     */   }
/*     */ 
/*     */   public void updateUI()
/*     */   {
/* 160 */     super.updateUI();
/* 161 */     setForeground(null);
/* 162 */     setBackground(null);
/*     */   }
/*     */ 
/*     */   public Component getTableCellRendererComponent(JTable paramJTable, Object paramObject, boolean paramBoolean1, boolean paramBoolean2, int paramInt1, int paramInt2)
/*     */   {
/* 189 */     if (paramJTable == null) {
/* 190 */       return this;
/*     */     }
/*     */ 
/* 193 */     Color localColor1 = null;
/* 194 */     Color localColor2 = null;
/*     */ 
/* 196 */     JTable.DropLocation localDropLocation = paramJTable.getDropLocation();
/* 197 */     if ((localDropLocation != null) && (!localDropLocation.isInsertRow()) && (!localDropLocation.isInsertColumn()) && (localDropLocation.getRow() == paramInt1) && (localDropLocation.getColumn() == paramInt2))
/*     */     {
/* 203 */       localColor1 = DefaultLookup.getColor(this, this.ui, "Table.dropCellForeground");
/* 204 */       localColor2 = DefaultLookup.getColor(this, this.ui, "Table.dropCellBackground");
/*     */ 
/* 206 */       paramBoolean1 = true;
/*     */     }
/*     */     Object localObject;
/*     */     Color localColor3;
/* 209 */     if (paramBoolean1) {
/* 210 */       super.setForeground(localColor1 == null ? paramJTable.getSelectionForeground() : localColor1);
/*     */ 
/* 212 */       super.setBackground(localColor2 == null ? paramJTable.getSelectionBackground() : localColor2);
/*     */     }
/*     */     else {
/* 215 */       localObject = this.unselectedBackground != null ? this.unselectedBackground : paramJTable.getBackground();
/*     */ 
/* 218 */       if ((localObject == null) || ((localObject instanceof UIResource))) {
/* 219 */         localColor3 = DefaultLookup.getColor(this, this.ui, "Table.alternateRowColor");
/* 220 */         if ((localColor3 != null) && (paramInt1 % 2 != 0)) {
/* 221 */           localObject = localColor3;
/*     */         }
/*     */       }
/* 224 */       super.setForeground(this.unselectedForeground != null ? this.unselectedForeground : paramJTable.getForeground());
/*     */ 
/* 227 */       super.setBackground((Color)localObject);
/*     */     }
/*     */ 
/* 230 */     setFont(paramJTable.getFont());
/*     */ 
/* 232 */     if (paramBoolean2) {
/* 233 */       localObject = null;
/* 234 */       if (paramBoolean1) {
/* 235 */         localObject = DefaultLookup.getBorder(this, this.ui, "Table.focusSelectedCellHighlightBorder");
/*     */       }
/* 237 */       if (localObject == null) {
/* 238 */         localObject = DefaultLookup.getBorder(this, this.ui, "Table.focusCellHighlightBorder");
/*     */       }
/* 240 */       setBorder((Border)localObject);
/*     */ 
/* 242 */       if ((!paramBoolean1) && (paramJTable.isCellEditable(paramInt1, paramInt2)))
/*     */       {
/* 244 */         localColor3 = DefaultLookup.getColor(this, this.ui, "Table.focusCellForeground");
/* 245 */         if (localColor3 != null) {
/* 246 */           super.setForeground(localColor3);
/*     */         }
/* 248 */         localColor3 = DefaultLookup.getColor(this, this.ui, "Table.focusCellBackground");
/* 249 */         if (localColor3 != null)
/* 250 */           super.setBackground(localColor3);
/*     */       }
/*     */     }
/*     */     else {
/* 254 */       setBorder(getNoFocusBorder());
/*     */     }
/*     */ 
/* 257 */     setValue(paramObject);
/*     */ 
/* 259 */     return this;
/*     */   }
/*     */ 
/*     */   public boolean isOpaque()
/*     */   {
/* 276 */     Color localColor = getBackground();
/* 277 */     Container localContainer = getParent();
/* 278 */     if (localContainer != null) {
/* 279 */       localContainer = localContainer.getParent();
/*     */     }
/*     */ 
/* 283 */     int i = (localColor != null) && (localContainer != null) && (localColor.equals(localContainer.getBackground())) && (localContainer.isOpaque()) ? 1 : 0;
/*     */ 
/* 286 */     return (i == 0) && (super.isOpaque());
/*     */   }
/*     */ 
/*     */   public void invalidate()
/*     */   {
/*     */   }
/*     */ 
/*     */   public void validate()
/*     */   {
/*     */   }
/*     */ 
/*     */   public void revalidate()
/*     */   {
/*     */   }
/*     */ 
/*     */   public void repaint(long paramLong, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void repaint(Rectangle paramRectangle)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void repaint()
/*     */   {
/*     */   }
/*     */ 
/*     */   protected void firePropertyChange(String paramString, Object paramObject1, Object paramObject2)
/*     */   {
/* 343 */     if ((paramString == "text") || (paramString == "labelFor") || (paramString == "displayedMnemonic") || (((paramString == "font") || (paramString == "foreground")) && (paramObject1 != paramObject2) && (getClientProperty("html") != null)))
/*     */     {
/* 350 */       super.firePropertyChange(paramString, paramObject1, paramObject2);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void firePropertyChange(String paramString, boolean paramBoolean1, boolean paramBoolean2)
/*     */   {
/*     */   }
/*     */ 
/*     */   protected void setValue(Object paramObject)
/*     */   {
/* 372 */     setText(paramObject == null ? "" : paramObject.toString());
/*     */   }
/*     */ 
/*     */   public static class UIResource extends DefaultTableCellRenderer
/*     */     implements UIResource
/*     */   {
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.table.DefaultTableCellRenderer
 * JD-Core Version:    0.6.2
 */