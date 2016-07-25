/*     */ package javax.swing.plaf.metal;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Insets;
/*     */ import javax.swing.ButtonModel;
/*     */ import javax.swing.CellRendererPane;
/*     */ import javax.swing.DefaultButtonModel;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JComboBox;
/*     */ import javax.swing.JList;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.ListCellRenderer;
/*     */ import javax.swing.UIManager;
/*     */ 
/*     */ public class MetalComboBoxButton extends JButton
/*     */ {
/*     */   protected JComboBox comboBox;
/*     */   protected JList listBox;
/*     */   protected CellRendererPane rendererPane;
/*     */   protected Icon comboIcon;
/*  56 */   protected boolean iconOnly = false;
/*     */ 
/*  58 */   public final JComboBox getComboBox() { return this.comboBox; } 
/*  59 */   public final void setComboBox(JComboBox paramJComboBox) { this.comboBox = paramJComboBox; } 
/*     */   public final Icon getComboIcon() {
/*  61 */     return this.comboIcon; } 
/*  62 */   public final void setComboIcon(Icon paramIcon) { this.comboIcon = paramIcon; } 
/*     */   public final boolean isIconOnly() {
/*  64 */     return this.iconOnly; } 
/*  65 */   public final void setIconOnly(boolean paramBoolean) { this.iconOnly = paramBoolean; }
/*     */ 
/*     */   MetalComboBoxButton() {
/*  68 */     super("");
/*  69 */     DefaultButtonModel local1 = new DefaultButtonModel() {
/*     */       public void setArmed(boolean paramAnonymousBoolean) {
/*  71 */         super.setArmed(isPressed() ? true : paramAnonymousBoolean);
/*     */       }
/*     */     };
/*  74 */     setModel(local1);
/*     */   }
/*     */ 
/*     */   public MetalComboBoxButton(JComboBox paramJComboBox, Icon paramIcon, CellRendererPane paramCellRendererPane, JList paramJList)
/*     */   {
/*  79 */     this();
/*  80 */     this.comboBox = paramJComboBox;
/*  81 */     this.comboIcon = paramIcon;
/*  82 */     this.rendererPane = paramCellRendererPane;
/*  83 */     this.listBox = paramJList;
/*  84 */     setEnabled(this.comboBox.isEnabled());
/*     */   }
/*     */ 
/*     */   public MetalComboBoxButton(JComboBox paramJComboBox, Icon paramIcon, boolean paramBoolean, CellRendererPane paramCellRendererPane, JList paramJList)
/*     */   {
/*  89 */     this(paramJComboBox, paramIcon, paramCellRendererPane, paramJList);
/*  90 */     this.iconOnly = paramBoolean;
/*     */   }
/*     */ 
/*     */   public boolean isFocusTraversable() {
/*  94 */     return false;
/*     */   }
/*     */ 
/*     */   public void setEnabled(boolean paramBoolean) {
/*  98 */     super.setEnabled(paramBoolean);
/*     */ 
/* 101 */     if (paramBoolean) {
/* 102 */       setBackground(this.comboBox.getBackground());
/* 103 */       setForeground(this.comboBox.getForeground());
/*     */     } else {
/* 105 */       setBackground(UIManager.getColor("ComboBox.disabledBackground"));
/* 106 */       setForeground(UIManager.getColor("ComboBox.disabledForeground"));
/*     */     }
/*     */   }
/*     */ 
/*     */   public void paintComponent(Graphics paramGraphics) {
/* 111 */     boolean bool1 = MetalUtils.isLeftToRight(this.comboBox);
/*     */ 
/* 114 */     super.paintComponent(paramGraphics);
/*     */ 
/* 116 */     Insets localInsets = getInsets();
/*     */ 
/* 118 */     int i = getWidth() - (localInsets.left + localInsets.right);
/* 119 */     int j = getHeight() - (localInsets.top + localInsets.bottom);
/*     */ 
/* 121 */     if ((j <= 0) || (i <= 0)) {
/* 122 */       return;
/*     */     }
/*     */ 
/* 125 */     int k = localInsets.left;
/* 126 */     int m = localInsets.top;
/* 127 */     int n = k + (i - 1);
/* 128 */     int i1 = m + (j - 1);
/*     */ 
/* 130 */     int i2 = 0;
/* 131 */     int i3 = bool1 ? n : k;
/*     */ 
/* 134 */     if (this.comboIcon != null) {
/* 135 */       i2 = this.comboIcon.getIconWidth();
/* 136 */       int i4 = this.comboIcon.getIconHeight();
/* 137 */       int i5 = 0;
/*     */ 
/* 139 */       if (this.iconOnly) {
/* 140 */         i3 = getWidth() / 2 - i2 / 2;
/* 141 */         i5 = getHeight() / 2 - i4 / 2;
/*     */       }
/*     */       else {
/* 144 */         if (bool1) {
/* 145 */           i3 = k + (i - 1) - i2;
/*     */         }
/*     */         else {
/* 148 */           i3 = k;
/*     */         }
/* 150 */         i5 = m + (i1 - m) / 2 - i4 / 2;
/*     */       }
/*     */ 
/* 153 */       this.comboIcon.paintIcon(this, paramGraphics, i3, i5);
/*     */ 
/* 156 */       if ((this.comboBox.hasFocus()) && ((!MetalLookAndFeel.usingOcean()) || (this.comboBox.isEditable())))
/*     */       {
/* 158 */         paramGraphics.setColor(MetalLookAndFeel.getFocusColor());
/* 159 */         paramGraphics.drawRect(k - 1, m - 1, i + 3, j + 1);
/*     */       }
/*     */     }
/*     */ 
/* 163 */     if (MetalLookAndFeel.usingOcean())
/*     */     {
/* 165 */       return;
/*     */     }
/*     */ 
/* 169 */     if ((!this.iconOnly) && (this.comboBox != null)) {
/* 170 */       ListCellRenderer localListCellRenderer = this.comboBox.getRenderer();
/*     */ 
/* 172 */       boolean bool2 = getModel().isPressed();
/* 173 */       Component localComponent = localListCellRenderer.getListCellRendererComponent(this.listBox, this.comboBox.getSelectedItem(), -1, bool2, false);
/*     */ 
/* 178 */       localComponent.setFont(this.rendererPane.getFont());
/*     */ 
/* 180 */       if ((this.model.isArmed()) && (this.model.isPressed())) {
/* 181 */         if (isOpaque()) {
/* 182 */           localComponent.setBackground(UIManager.getColor("Button.select"));
/*     */         }
/* 184 */         localComponent.setForeground(this.comboBox.getForeground());
/*     */       }
/* 186 */       else if (!this.comboBox.isEnabled()) {
/* 187 */         if (isOpaque()) {
/* 188 */           localComponent.setBackground(UIManager.getColor("ComboBox.disabledBackground"));
/*     */         }
/* 190 */         localComponent.setForeground(UIManager.getColor("ComboBox.disabledForeground"));
/*     */       }
/*     */       else {
/* 193 */         localComponent.setForeground(this.comboBox.getForeground());
/* 194 */         localComponent.setBackground(this.comboBox.getBackground());
/*     */       }
/*     */ 
/* 198 */       int i6 = i - (localInsets.right + i2);
/*     */ 
/* 201 */       boolean bool3 = false;
/* 202 */       if ((localComponent instanceof JPanel)) {
/* 203 */         bool3 = true;
/*     */       }
/*     */ 
/* 206 */       if (bool1) {
/* 207 */         this.rendererPane.paintComponent(paramGraphics, localComponent, this, k, m, i6, j, bool3);
/*     */       }
/*     */       else
/*     */       {
/* 211 */         this.rendererPane.paintComponent(paramGraphics, localComponent, this, k + i2, m, i6, j, bool3);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public Dimension getMinimumSize()
/*     */   {
/* 218 */     Dimension localDimension = new Dimension();
/* 219 */     Insets localInsets = getInsets();
/* 220 */     localDimension.width = (localInsets.left + getComboIcon().getIconWidth() + localInsets.right);
/* 221 */     localDimension.height = (localInsets.bottom + getComboIcon().getIconHeight() + localInsets.top);
/* 222 */     return localDimension;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.metal.MetalComboBoxButton
 * JD-Core Version:    0.6.2
 */