/*     */ package com.sun.java.swing.plaf.motif;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Container;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Insets;
/*     */ import java.awt.LayoutManager;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.event.KeyListener;
/*     */ import java.awt.event.MouseMotionAdapter;
/*     */ import java.awt.event.MouseMotionListener;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.io.Serializable;
/*     */ import javax.swing.CellRendererPane;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.JComboBox;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.ListCellRenderer;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.border.Border;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.basic.BasicComboBoxUI;
/*     */ import javax.swing.plaf.basic.BasicComboBoxUI.ComboBoxLayoutManager;
/*     */ import javax.swing.plaf.basic.BasicComboBoxUI.PropertyChangeHandler;
/*     */ import javax.swing.plaf.basic.BasicComboPopup;
/*     */ import javax.swing.plaf.basic.BasicComboPopup.InvocationKeyHandler;
/*     */ import javax.swing.plaf.basic.ComboPopup;
/*     */ 
/*     */ public class MotifComboBoxUI extends BasicComboBoxUI
/*     */   implements Serializable
/*     */ {
/*     */   Icon arrowIcon;
/*     */   static final int HORIZ_MARGIN = 3;
/*     */ 
/*     */   public static ComponentUI createUI(JComponent paramJComponent)
/*     */   {
/*  52 */     return new MotifComboBoxUI();
/*     */   }
/*     */ 
/*     */   public void installUI(JComponent paramJComponent) {
/*  56 */     super.installUI(paramJComponent);
/*  57 */     this.arrowIcon = new MotifComboBoxArrowIcon(UIManager.getColor("controlHighlight"), UIManager.getColor("controlShadow"), UIManager.getColor("control"));
/*     */ 
/*  61 */     Runnable local1 = new Runnable() {
/*     */       public void run() {
/*  63 */         if (MotifComboBoxUI.this.motifGetEditor() != null)
/*  64 */           MotifComboBoxUI.this.motifGetEditor().setBackground(UIManager.getColor("text"));
/*     */       }
/*     */     };
/*  69 */     SwingUtilities.invokeLater(local1);
/*     */   }
/*     */ 
/*     */   public Dimension getMinimumSize(JComponent paramJComponent) {
/*  73 */     if (!this.isMinimumSizeDirty) {
/*  74 */       return new Dimension(this.cachedMinimumSize);
/*     */     }
/*     */ 
/*  77 */     Insets localInsets = getInsets();
/*  78 */     Dimension localDimension = getDisplaySize();
/*  79 */     localDimension.height += localInsets.top + localInsets.bottom;
/*  80 */     int i = iconAreaWidth();
/*  81 */     localDimension.width += localInsets.left + localInsets.right + i;
/*     */ 
/*  83 */     this.cachedMinimumSize.setSize(localDimension.width, localDimension.height);
/*  84 */     this.isMinimumSizeDirty = false;
/*     */ 
/*  86 */     return localDimension;
/*     */   }
/*     */ 
/*     */   protected ComboPopup createPopup() {
/*  90 */     return new MotifComboPopup(this.comboBox);
/*     */   }
/*     */ 
/*     */   protected void installComponents()
/*     */   {
/* 121 */     if (this.comboBox.isEditable()) {
/* 122 */       addEditor();
/*     */     }
/*     */ 
/* 125 */     this.comboBox.add(this.currentValuePane);
/*     */   }
/*     */ 
/*     */   protected void uninstallComponents() {
/* 129 */     removeEditor();
/* 130 */     this.comboBox.removeAll();
/*     */   }
/*     */ 
/*     */   public void paint(Graphics paramGraphics, JComponent paramJComponent) {
/* 134 */     boolean bool = this.comboBox.hasFocus();
/*     */ 
/* 137 */     if (this.comboBox.isEnabled())
/* 138 */       paramGraphics.setColor(this.comboBox.getBackground());
/*     */     else {
/* 140 */       paramGraphics.setColor(UIManager.getColor("ComboBox.disabledBackground"));
/*     */     }
/* 142 */     paramGraphics.fillRect(0, 0, paramJComponent.getWidth(), paramJComponent.getHeight());
/*     */ 
/* 144 */     if (!this.comboBox.isEditable()) {
/* 145 */       localRectangle = rectangleForCurrentValue();
/* 146 */       paintCurrentValue(paramGraphics, localRectangle, bool);
/*     */     }
/* 148 */     Rectangle localRectangle = rectangleForArrowIcon();
/* 149 */     this.arrowIcon.paintIcon(paramJComponent, paramGraphics, localRectangle.x, localRectangle.y);
/* 150 */     if (!this.comboBox.isEditable()) {
/* 151 */       Border localBorder = this.comboBox.getBorder();
/*     */       Insets localInsets;
/* 153 */       if (localBorder != null) {
/* 154 */         localInsets = localBorder.getBorderInsets(this.comboBox);
/*     */       }
/*     */       else {
/* 157 */         localInsets = new Insets(0, 0, 0, 0);
/*     */       }
/*     */ 
/* 160 */       if (MotifGraphicsUtils.isLeftToRight(this.comboBox)) {
/* 161 */         localRectangle.x -= 5;
/*     */       }
/*     */       else {
/* 164 */         localRectangle.x += localRectangle.width + 3 + 1;
/*     */       }
/* 166 */       localRectangle.y = localInsets.top;
/* 167 */       localRectangle.width = 1;
/* 168 */       localRectangle.height = (this.comboBox.getBounds().height - localInsets.bottom - localInsets.top);
/* 169 */       paramGraphics.setColor(UIManager.getColor("controlShadow"));
/* 170 */       paramGraphics.fillRect(localRectangle.x, localRectangle.y, localRectangle.width, localRectangle.height);
/* 171 */       localRectangle.x += 1;
/* 172 */       paramGraphics.setColor(UIManager.getColor("controlHighlight"));
/* 173 */       paramGraphics.fillRect(localRectangle.x, localRectangle.y, localRectangle.width, localRectangle.height);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void paintCurrentValue(Graphics paramGraphics, Rectangle paramRectangle, boolean paramBoolean) {
/* 178 */     ListCellRenderer localListCellRenderer = this.comboBox.getRenderer();
/*     */ 
/* 181 */     Component localComponent = localListCellRenderer.getListCellRendererComponent(this.listBox, this.comboBox.getSelectedItem(), -1, false, false);
/* 182 */     localComponent.setFont(this.comboBox.getFont());
/* 183 */     if (this.comboBox.isEnabled()) {
/* 184 */       localComponent.setForeground(this.comboBox.getForeground());
/* 185 */       localComponent.setBackground(this.comboBox.getBackground());
/*     */     }
/*     */     else {
/* 188 */       localComponent.setForeground(UIManager.getColor("ComboBox.disabledForeground"));
/* 189 */       localComponent.setBackground(UIManager.getColor("ComboBox.disabledBackground"));
/*     */     }
/* 191 */     Dimension localDimension = localComponent.getPreferredSize();
/* 192 */     this.currentValuePane.paintComponent(paramGraphics, localComponent, this.comboBox, paramRectangle.x, paramRectangle.y, paramRectangle.width, localDimension.height);
/*     */   }
/*     */ 
/*     */   protected Rectangle rectangleForArrowIcon()
/*     */   {
/* 197 */     Rectangle localRectangle = this.comboBox.getBounds();
/* 198 */     Border localBorder = this.comboBox.getBorder();
/*     */     Insets localInsets;
/* 200 */     if (localBorder != null) {
/* 201 */       localInsets = localBorder.getBorderInsets(this.comboBox);
/*     */     }
/*     */     else {
/* 204 */       localInsets = new Insets(0, 0, 0, 0);
/*     */     }
/* 206 */     localRectangle.x = localInsets.left;
/* 207 */     localRectangle.y = localInsets.top;
/* 208 */     localRectangle.width -= localInsets.left + localInsets.right;
/* 209 */     localRectangle.height -= localInsets.top + localInsets.bottom;
/*     */ 
/* 211 */     if (MotifGraphicsUtils.isLeftToRight(this.comboBox)) {
/* 212 */       localRectangle.x = (localRectangle.x + localRectangle.width - 3 - this.arrowIcon.getIconWidth());
/*     */     }
/*     */     else {
/* 215 */       localRectangle.x += 3;
/*     */     }
/* 217 */     localRectangle.y += (localRectangle.height - this.arrowIcon.getIconHeight()) / 2;
/* 218 */     localRectangle.width = this.arrowIcon.getIconWidth();
/* 219 */     localRectangle.height = this.arrowIcon.getIconHeight();
/* 220 */     return localRectangle;
/*     */   }
/*     */ 
/*     */   protected Rectangle rectangleForCurrentValue() {
/* 224 */     int i = this.comboBox.getWidth();
/* 225 */     int j = this.comboBox.getHeight();
/* 226 */     Insets localInsets = getInsets();
/* 227 */     if (MotifGraphicsUtils.isLeftToRight(this.comboBox)) {
/* 228 */       return new Rectangle(localInsets.left, localInsets.top, i - (localInsets.left + localInsets.right) - iconAreaWidth(), j - (localInsets.top + localInsets.bottom));
/*     */     }
/*     */ 
/* 234 */     return new Rectangle(localInsets.left + iconAreaWidth(), localInsets.top, i - (localInsets.left + localInsets.right) - iconAreaWidth(), j - (localInsets.top + localInsets.bottom));
/*     */   }
/*     */ 
/*     */   public int iconAreaWidth()
/*     */   {
/* 242 */     if (this.comboBox.isEditable()) {
/* 243 */       return this.arrowIcon.getIconWidth() + 6;
/*     */     }
/* 245 */     return this.arrowIcon.getIconWidth() + 9 + 2;
/*     */   }
/*     */ 
/*     */   public void configureEditor() {
/* 249 */     super.configureEditor();
/* 250 */     this.editor.setBackground(UIManager.getColor("text"));
/*     */   }
/*     */ 
/*     */   protected LayoutManager createLayoutManager() {
/* 254 */     return new ComboBoxLayoutManager();
/*     */   }
/*     */ 
/*     */   private Component motifGetEditor() {
/* 258 */     return this.editor;
/*     */   }
/*     */ 
/*     */   protected PropertyChangeListener createPropertyChangeListener()
/*     */   {
/* 340 */     return new MotifPropertyChangeListener(null);
/*     */   }
/*     */ 
/*     */   public class ComboBoxLayoutManager extends BasicComboBoxUI.ComboBoxLayoutManager
/*     */   {
/*     */     public ComboBoxLayoutManager()
/*     */     {
/* 268 */       super();
/*     */     }
/*     */     public void layoutContainer(Container paramContainer) {
/* 271 */       if (MotifComboBoxUI.this.motifGetEditor() != null) {
/* 272 */         Rectangle localRectangle = MotifComboBoxUI.this.rectangleForCurrentValue();
/* 273 */         localRectangle.x += 1;
/* 274 */         localRectangle.y += 1;
/* 275 */         localRectangle.width -= 1;
/* 276 */         localRectangle.height -= 2;
/* 277 */         MotifComboBoxUI.this.motifGetEditor().setBounds(localRectangle);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   static class MotifComboBoxArrowIcon implements Icon, Serializable { private Color lightShadow;
/*     */     private Color darkShadow;
/*     */     private Color fill;
/*     */ 
/* 288 */     public MotifComboBoxArrowIcon(Color paramColor1, Color paramColor2, Color paramColor3) { this.lightShadow = paramColor1;
/* 289 */       this.darkShadow = paramColor2;
/* 290 */       this.fill = paramColor3;
/*     */     }
/*     */ 
/*     */     public void paintIcon(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2)
/*     */     {
/* 295 */       int i = getIconWidth();
/* 296 */       int j = getIconHeight();
/*     */ 
/* 298 */       paramGraphics.setColor(this.lightShadow);
/* 299 */       paramGraphics.drawLine(paramInt1, paramInt2, paramInt1 + i - 1, paramInt2);
/* 300 */       paramGraphics.drawLine(paramInt1, paramInt2 + 1, paramInt1 + i - 3, paramInt2 + 1);
/* 301 */       paramGraphics.setColor(this.darkShadow);
/* 302 */       paramGraphics.drawLine(paramInt1 + i - 2, paramInt2 + 1, paramInt1 + i - 1, paramInt2 + 1);
/*     */ 
/* 304 */       int k = paramInt1 + 1; int m = paramInt2 + 2; for (int n = i - 6; m + 1 < paramInt2 + j; m += 2) {
/* 305 */         paramGraphics.setColor(this.lightShadow);
/* 306 */         paramGraphics.drawLine(k, m, k + 1, m);
/* 307 */         paramGraphics.drawLine(k, m + 1, k + 1, m + 1);
/* 308 */         if (n > 0) {
/* 309 */           paramGraphics.setColor(this.fill);
/* 310 */           paramGraphics.drawLine(k + 2, m, k + 1 + n, m);
/* 311 */           paramGraphics.drawLine(k + 2, m + 1, k + 1 + n, m + 1);
/*     */         }
/* 313 */         paramGraphics.setColor(this.darkShadow);
/* 314 */         paramGraphics.drawLine(k + n + 2, m, k + n + 3, m);
/* 315 */         paramGraphics.drawLine(k + n + 2, m + 1, k + n + 3, m + 1);
/* 316 */         k++;
/* 317 */         n -= 2;
/*     */       }
/*     */ 
/* 320 */       paramGraphics.setColor(this.darkShadow);
/* 321 */       paramGraphics.drawLine(paramInt1 + i / 2, paramInt2 + j - 1, paramInt1 + i / 2, paramInt2 + j - 1);
/*     */     }
/*     */ 
/*     */     public int getIconWidth()
/*     */     {
/* 326 */       return 11;
/*     */     }
/*     */ 
/*     */     public int getIconHeight() {
/* 330 */       return 11;
/*     */     }
/*     */   }
/*     */ 
/*     */   protected class MotifComboPopup extends BasicComboPopup
/*     */   {
/*     */     public MotifComboPopup(JComboBox arg2)
/*     */     {
/*  99 */       super();
/*     */     }
/*     */ 
/*     */     public MouseMotionListener createListMouseMotionListener()
/*     */     {
/* 106 */       return new MouseMotionAdapter() { } ;
/*     */     }
/*     */ 
/*     */     public KeyListener createKeyListener() {
/* 110 */       return super.createKeyListener();
/*     */     }
/*     */ 
/*     */     protected class InvocationKeyHandler extends BasicComboPopup.InvocationKeyHandler {
/*     */       protected InvocationKeyHandler() {
/* 115 */         super();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private class MotifPropertyChangeListener extends BasicComboBoxUI.PropertyChangeHandler
/*     */   {
/*     */     private MotifPropertyChangeListener()
/*     */     {
/* 346 */       super();
/*     */     }
/*     */     public void propertyChange(PropertyChangeEvent paramPropertyChangeEvent) {
/* 349 */       super.propertyChange(paramPropertyChangeEvent);
/* 350 */       String str = paramPropertyChangeEvent.getPropertyName();
/* 351 */       if ((str == "enabled") && 
/* 352 */         (MotifComboBoxUI.this.comboBox.isEnabled())) {
/* 353 */         Component localComponent = MotifComboBoxUI.this.motifGetEditor();
/* 354 */         if (localComponent != null)
/* 355 */           localComponent.setBackground(UIManager.getColor("text"));
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.java.swing.plaf.motif.MotifComboBoxUI
 * JD-Core Version:    0.6.2
 */