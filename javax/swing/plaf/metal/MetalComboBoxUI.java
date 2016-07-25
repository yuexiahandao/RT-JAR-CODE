/*     */ package javax.swing.plaf.metal;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Container;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Insets;
/*     */ import java.awt.LayoutManager;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import javax.swing.ComboBoxEditor;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JComboBox;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JList;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.basic.BasicComboBoxUI;
/*     */ import javax.swing.plaf.basic.BasicComboBoxUI.ComboBoxLayoutManager;
/*     */ import javax.swing.plaf.basic.BasicComboBoxUI.PropertyChangeHandler;
/*     */ import javax.swing.plaf.basic.BasicComboPopup;
/*     */ import javax.swing.plaf.basic.ComboPopup;
/*     */ 
/*     */ public class MetalComboBoxUI extends BasicComboBoxUI
/*     */ {
/*     */   public static ComponentUI createUI(JComponent paramJComponent)
/*     */   {
/*  57 */     return new MetalComboBoxUI();
/*     */   }
/*     */ 
/*     */   public void paint(Graphics paramGraphics, JComponent paramJComponent) {
/*  61 */     if (MetalLookAndFeel.usingOcean())
/*  62 */       super.paint(paramGraphics, paramJComponent);
/*     */   }
/*     */ 
/*     */   public void paintCurrentValue(Graphics paramGraphics, Rectangle paramRectangle, boolean paramBoolean)
/*     */   {
/*  78 */     if (MetalLookAndFeel.usingOcean()) {
/*  79 */       paramRectangle.x += 2;
/*  80 */       paramRectangle.width -= 3;
/*  81 */       if (this.arrowButton != null) {
/*  82 */         Insets localInsets = this.arrowButton.getInsets();
/*  83 */         paramRectangle.y += localInsets.top;
/*  84 */         paramRectangle.height -= localInsets.top + localInsets.bottom;
/*     */       }
/*     */       else {
/*  87 */         paramRectangle.y += 2;
/*  88 */         paramRectangle.height -= 4;
/*     */       }
/*  90 */       super.paintCurrentValue(paramGraphics, paramRectangle, paramBoolean);
/*     */     }
/*  92 */     else if ((paramGraphics == null) || (paramRectangle == null)) {
/*  93 */       throw new NullPointerException("Must supply a non-null Graphics and Rectangle");
/*     */     }
/*     */   }
/*     */ 
/*     */   public void paintCurrentValueBackground(Graphics paramGraphics, Rectangle paramRectangle, boolean paramBoolean)
/*     */   {
/* 110 */     if (MetalLookAndFeel.usingOcean()) {
/* 111 */       paramGraphics.setColor(MetalLookAndFeel.getControlDarkShadow());
/* 112 */       paramGraphics.drawRect(paramRectangle.x, paramRectangle.y, paramRectangle.width, paramRectangle.height - 1);
/* 113 */       paramGraphics.setColor(MetalLookAndFeel.getControlShadow());
/* 114 */       paramGraphics.drawRect(paramRectangle.x + 1, paramRectangle.y + 1, paramRectangle.width - 2, paramRectangle.height - 3);
/*     */ 
/* 116 */       if ((paramBoolean) && (!isPopupVisible(this.comboBox)) && (this.arrowButton != null))
/*     */       {
/* 118 */         paramGraphics.setColor(this.listBox.getSelectionBackground());
/* 119 */         Insets localInsets = this.arrowButton.getInsets();
/* 120 */         if (localInsets.top > 2) {
/* 121 */           paramGraphics.fillRect(paramRectangle.x + 2, paramRectangle.y + 2, paramRectangle.width - 3, localInsets.top - 2);
/*     */         }
/*     */ 
/* 124 */         if (localInsets.bottom > 2) {
/* 125 */           paramGraphics.fillRect(paramRectangle.x + 2, paramRectangle.y + paramRectangle.height - localInsets.bottom, paramRectangle.width - 3, localInsets.bottom - 2);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/* 131 */     else if ((paramGraphics == null) || (paramRectangle == null)) {
/* 132 */       throw new NullPointerException("Must supply a non-null Graphics and Rectangle");
/*     */     }
/*     */   }
/*     */ 
/*     */   public int getBaseline(JComponent paramJComponent, int paramInt1, int paramInt2)
/*     */   {
/*     */     int i;
/* 147 */     if ((MetalLookAndFeel.usingOcean()) && (paramInt2 >= 4)) {
/* 148 */       paramInt2 -= 4;
/* 149 */       i = super.getBaseline(paramJComponent, paramInt1, paramInt2);
/* 150 */       if (i >= 0)
/* 151 */         i += 2;
/*     */     }
/*     */     else
/*     */     {
/* 155 */       i = super.getBaseline(paramJComponent, paramInt1, paramInt2);
/*     */     }
/* 157 */     return i;
/*     */   }
/*     */ 
/*     */   protected ComboBoxEditor createEditor() {
/* 161 */     return new MetalComboBoxEditor.UIResource();
/*     */   }
/*     */ 
/*     */   protected ComboPopup createPopup() {
/* 165 */     return super.createPopup();
/*     */   }
/*     */ 
/*     */   protected JButton createArrowButton() {
/* 169 */     boolean bool = (this.comboBox.isEditable()) || (MetalLookAndFeel.usingOcean());
/*     */ 
/* 171 */     MetalComboBoxButton localMetalComboBoxButton = new MetalComboBoxButton(this.comboBox, new MetalComboBoxIcon(), bool, this.currentValuePane, this.listBox);
/*     */ 
/* 176 */     localMetalComboBoxButton.setMargin(new Insets(0, 1, 1, 3));
/* 177 */     if (MetalLookAndFeel.usingOcean())
/*     */     {
/* 179 */       localMetalComboBoxButton.putClientProperty(MetalBorders.NO_BUTTON_ROLLOVER, Boolean.TRUE);
/*     */     }
/*     */ 
/* 182 */     updateButtonForOcean(localMetalComboBoxButton);
/* 183 */     return localMetalComboBoxButton;
/*     */   }
/*     */ 
/*     */   private void updateButtonForOcean(JButton paramJButton)
/*     */   {
/* 190 */     if (MetalLookAndFeel.usingOcean())
/*     */     {
/* 193 */       paramJButton.setFocusPainted(this.comboBox.isEditable());
/*     */     }
/*     */   }
/*     */ 
/*     */   public PropertyChangeListener createPropertyChangeListener() {
/* 198 */     return new MetalPropertyChangeListener();
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   protected void editablePropertyChanged(PropertyChangeEvent paramPropertyChangeEvent)
/*     */   {
/*     */   }
/*     */ 
/*     */   protected LayoutManager createLayoutManager()
/*     */   {
/* 242 */     return new MetalComboBoxLayoutManager();
/*     */   }
/*     */ 
/*     */   public void layoutComboBox(Container paramContainer, MetalComboBoxLayoutManager paramMetalComboBoxLayoutManager)
/*     */   {
/* 262 */     if ((this.comboBox.isEditable()) && (!MetalLookAndFeel.usingOcean())) {
/* 263 */       paramMetalComboBoxLayoutManager.superLayout(paramContainer);
/*     */       return;
/*     */     }
/*     */     Object localObject;
/* 267 */     if (this.arrowButton != null)
/*     */     {
/*     */       int i;
/* 268 */       if (MetalLookAndFeel.usingOcean()) {
/* 269 */         localObject = this.comboBox.getInsets();
/* 270 */         i = this.arrowButton.getMinimumSize().width;
/* 271 */         this.arrowButton.setBounds(MetalUtils.isLeftToRight(this.comboBox) ? this.comboBox.getWidth() - ((Insets)localObject).right - i : ((Insets)localObject).left, ((Insets)localObject).top, i, this.comboBox.getHeight() - ((Insets)localObject).top - ((Insets)localObject).bottom);
/*     */       }
/*     */       else
/*     */       {
/* 278 */         localObject = this.comboBox.getInsets();
/* 279 */         i = this.comboBox.getWidth();
/* 280 */         int j = this.comboBox.getHeight();
/* 281 */         this.arrowButton.setBounds(((Insets)localObject).left, ((Insets)localObject).top, i - (((Insets)localObject).left + ((Insets)localObject).right), j - (((Insets)localObject).top + ((Insets)localObject).bottom));
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 287 */     if ((this.editor != null) && (MetalLookAndFeel.usingOcean())) {
/* 288 */       localObject = rectangleForCurrentValue();
/* 289 */       this.editor.setBounds((Rectangle)localObject);
/*     */     }
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   protected void removeListeners()
/*     */   {
/* 301 */     if (this.propertyChangeListener != null)
/* 302 */       this.comboBox.removePropertyChangeListener(this.propertyChangeListener);
/*     */   }
/*     */ 
/*     */   public void configureEditor()
/*     */   {
/* 312 */     super.configureEditor();
/*     */   }
/*     */ 
/*     */   public void unconfigureEditor() {
/* 316 */     super.unconfigureEditor();
/*     */   }
/*     */ 
/*     */   public Dimension getMinimumSize(JComponent paramJComponent) {
/* 320 */     if (!this.isMinimumSizeDirty) {
/* 321 */       return new Dimension(this.cachedMinimumSize);
/*     */     }
/*     */ 
/* 324 */     Dimension localDimension = null;
/*     */     Insets localInsets1;
/* 326 */     if ((!this.comboBox.isEditable()) && (this.arrowButton != null))
/*     */     {
/* 328 */       localInsets1 = this.arrowButton.getInsets();
/* 329 */       Insets localInsets2 = this.comboBox.getInsets();
/*     */ 
/* 331 */       localDimension = getDisplaySize();
/* 332 */       localDimension.width += localInsets2.left + localInsets2.right;
/* 333 */       localDimension.width += localInsets1.right;
/* 334 */       localDimension.width += this.arrowButton.getMinimumSize().width;
/* 335 */       localDimension.height += localInsets2.top + localInsets2.bottom;
/* 336 */       localDimension.height += localInsets1.top + localInsets1.bottom;
/*     */     }
/* 338 */     else if ((this.comboBox.isEditable()) && (this.arrowButton != null) && (this.editor != null))
/*     */     {
/* 341 */       localDimension = super.getMinimumSize(paramJComponent);
/* 342 */       localInsets1 = this.arrowButton.getMargin();
/* 343 */       localDimension.height += localInsets1.top + localInsets1.bottom;
/* 344 */       localDimension.width += localInsets1.left + localInsets1.right;
/*     */     }
/*     */     else {
/* 347 */       localDimension = super.getMinimumSize(paramJComponent);
/*     */     }
/*     */ 
/* 350 */     this.cachedMinimumSize.setSize(localDimension.width, localDimension.height);
/* 351 */     this.isMinimumSizeDirty = false;
/*     */ 
/* 353 */     return new Dimension(this.cachedMinimumSize);
/*     */   }
/*     */ 
/*     */   public class MetalComboBoxLayoutManager extends BasicComboBoxUI.ComboBoxLayoutManager
/*     */   {
/*     */     public MetalComboBoxLayoutManager()
/*     */     {
/* 249 */       super();
/*     */     }
/* 251 */     public void layoutContainer(Container paramContainer) { MetalComboBoxUI.this.layoutComboBox(paramContainer, this); }
/*     */ 
/*     */     public void superLayout(Container paramContainer) {
/* 254 */       super.layoutContainer(paramContainer);
/*     */     }
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public class MetalComboPopup extends BasicComboPopup
/*     */   {
/*     */     public MetalComboPopup(JComboBox arg2)
/*     */     {
/* 370 */       super();
/*     */     }
/*     */ 
/*     */     public void delegateFocus(MouseEvent paramMouseEvent)
/*     */     {
/* 379 */       super.delegateFocus(paramMouseEvent);
/*     */     }
/*     */   }
/*     */ 
/*     */   public class MetalPropertyChangeListener extends BasicComboBoxUI.PropertyChangeHandler
/*     */   {
/*     */     public MetalPropertyChangeListener()
/*     */     {
/* 205 */       super();
/*     */     }
/* 207 */     public void propertyChange(PropertyChangeEvent paramPropertyChangeEvent) { super.propertyChange(paramPropertyChangeEvent);
/* 208 */       String str = paramPropertyChangeEvent.getPropertyName();
/*     */       Object localObject;
/* 210 */       if (str == "editable") {
/* 211 */         if ((MetalComboBoxUI.this.arrowButton instanceof MetalComboBoxButton)) {
/* 212 */           localObject = (MetalComboBoxButton)MetalComboBoxUI.this.arrowButton;
/* 213 */           ((MetalComboBoxButton)localObject).setIconOnly((MetalComboBoxUI.this.comboBox.isEditable()) || (MetalLookAndFeel.usingOcean()));
/*     */         }
/*     */ 
/* 216 */         MetalComboBoxUI.this.comboBox.repaint();
/* 217 */         MetalComboBoxUI.this.updateButtonForOcean(MetalComboBoxUI.this.arrowButton);
/* 218 */       } else if (str == "background") {
/* 219 */         localObject = (Color)paramPropertyChangeEvent.getNewValue();
/* 220 */         MetalComboBoxUI.this.arrowButton.setBackground((Color)localObject);
/* 221 */         MetalComboBoxUI.this.listBox.setBackground((Color)localObject);
/*     */       }
/* 223 */       else if (str == "foreground") {
/* 224 */         localObject = (Color)paramPropertyChangeEvent.getNewValue();
/* 225 */         MetalComboBoxUI.this.arrowButton.setForeground((Color)localObject);
/* 226 */         MetalComboBoxUI.this.listBox.setForeground((Color)localObject);
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.metal.MetalComboBoxUI
 * JD-Core Version:    0.6.2
 */