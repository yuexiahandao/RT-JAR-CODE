/*     */ package javax.swing.colorchooser;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Container;
/*     */ import java.awt.ContainerOrderFocusTraversalPolicy;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.GridBagConstraints;
/*     */ import java.awt.GridBagLayout;
/*     */ import java.awt.Insets;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import javax.accessibility.AccessibleContext;
/*     */ import javax.swing.AbstractButton;
/*     */ import javax.swing.ButtonGroup;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JFormattedTextField;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JRadioButton;
/*     */ import javax.swing.JSlider;
/*     */ import javax.swing.JSpinner;
/*     */ import javax.swing.JSpinner.DefaultEditor;
/*     */ import javax.swing.border.EmptyBorder;
/*     */ 
/*     */ final class ColorPanel extends JPanel
/*     */   implements ActionListener
/*     */ {
/*  44 */   private final SlidingSpinner[] spinners = new SlidingSpinner[5];
/*  45 */   private final float[] values = new float[this.spinners.length];
/*     */   private final ColorModel model;
/*     */   private Color color;
/*  49 */   private int x = 1;
/*  50 */   private int y = 2;
/*     */   private int z;
/*     */ 
/*     */   ColorPanel(ColorModel paramColorModel)
/*     */   {
/*  54 */     super(new GridBagLayout());
/*     */ 
/*  56 */     GridBagConstraints localGridBagConstraints = new GridBagConstraints();
/*  57 */     localGridBagConstraints.fill = 2;
/*     */ 
/*  59 */     localGridBagConstraints.gridx = 1;
/*  60 */     ButtonGroup localButtonGroup = new ButtonGroup();
/*  61 */     EmptyBorder localEmptyBorder = null;
/*  62 */     for (int i = 0; i < this.spinners.length; i++)
/*     */     {
/*     */       Object localObject;
/*  63 */       if (i < 3) {
/*  64 */         localObject = new JRadioButton();
/*  65 */         if (i == 0) {
/*  66 */           Insets localInsets = ((JRadioButton)localObject).getInsets();
/*  67 */           localInsets.left = ((JRadioButton)localObject).getPreferredSize().width;
/*  68 */           localEmptyBorder = new EmptyBorder(localInsets);
/*  69 */           ((JRadioButton)localObject).setSelected(true);
/*  70 */           localGridBagConstraints.insets.top = 5;
/*     */         }
/*  72 */         add((Component)localObject, localGridBagConstraints);
/*  73 */         localButtonGroup.add((AbstractButton)localObject);
/*  74 */         ((JRadioButton)localObject).setActionCommand(Integer.toString(i));
/*  75 */         ((JRadioButton)localObject).addActionListener(this);
/*  76 */         this.spinners[i] = new SlidingSpinner(this, (JComponent)localObject);
/*     */       }
/*     */       else {
/*  79 */         localObject = new JLabel();
/*  80 */         add((Component)localObject, localGridBagConstraints);
/*  81 */         ((JLabel)localObject).setBorder(localEmptyBorder);
/*  82 */         ((JLabel)localObject).setFocusable(false);
/*  83 */         this.spinners[i] = new SlidingSpinner(this, (JComponent)localObject);
/*     */       }
/*     */     }
/*  86 */     localGridBagConstraints.gridx = 2;
/*  87 */     localGridBagConstraints.weightx = 1.0D;
/*  88 */     localGridBagConstraints.insets.top = 0;
/*  89 */     localGridBagConstraints.insets.left = 5;
/*     */     SlidingSpinner localSlidingSpinner;
/*  90 */     for (localSlidingSpinner : this.spinners) {
/*  91 */       add(localSlidingSpinner.getSlider(), localGridBagConstraints);
/*  92 */       localGridBagConstraints.insets.top = 5;
/*     */     }
/*  94 */     localGridBagConstraints.gridx = 3;
/*  95 */     localGridBagConstraints.weightx = 0.0D;
/*  96 */     localGridBagConstraints.insets.top = 0;
/*  97 */     for (localSlidingSpinner : this.spinners) {
/*  98 */       add(localSlidingSpinner.getSpinner(), localGridBagConstraints);
/*  99 */       localGridBagConstraints.insets.top = 5;
/*     */     }
/* 101 */     setFocusTraversalPolicy(new ContainerOrderFocusTraversalPolicy());
/* 102 */     setFocusTraversalPolicyProvider(true);
/* 103 */     setFocusable(false);
/*     */ 
/* 105 */     this.model = paramColorModel;
/*     */   }
/*     */ 
/*     */   public void actionPerformed(ActionEvent paramActionEvent) {
/*     */     try {
/* 110 */       this.z = Integer.parseInt(paramActionEvent.getActionCommand());
/* 111 */       this.y = (this.z != 2 ? 2 : 1);
/* 112 */       this.x = (this.z != 0 ? 0 : 1);
/* 113 */       getParent().repaint();
/*     */     }
/*     */     catch (NumberFormatException localNumberFormatException) {
/*     */     }
/*     */   }
/*     */ 
/*     */   void buildPanel() {
/* 120 */     int i = this.model.getCount();
/* 121 */     this.spinners[4].setVisible(i > 4);
/* 122 */     for (int j = 0; j < i; j++) {
/* 123 */       String str = this.model.getLabel(this, j);
/* 124 */       JComponent localJComponent = this.spinners[j].getLabel();
/* 125 */       if ((localJComponent instanceof JRadioButton)) {
/* 126 */         localObject = (JRadioButton)localJComponent;
/* 127 */         ((JRadioButton)localObject).setText(str);
/* 128 */         ((JRadioButton)localObject).getAccessibleContext().setAccessibleDescription(str);
/*     */       }
/* 130 */       else if ((localJComponent instanceof JLabel)) {
/* 131 */         localObject = (JLabel)localJComponent;
/* 132 */         ((JLabel)localObject).setText(str);
/*     */       }
/* 134 */       this.spinners[j].setRange(this.model.getMinimum(j), this.model.getMaximum(j));
/* 135 */       this.spinners[j].setValue(this.values[j]);
/* 136 */       this.spinners[j].getSlider().getAccessibleContext().setAccessibleName(str);
/* 137 */       this.spinners[j].getSpinner().getAccessibleContext().setAccessibleName(str);
/* 138 */       Object localObject = (JSpinner.DefaultEditor)this.spinners[j].getSpinner().getEditor();
/* 139 */       ((JSpinner.DefaultEditor)localObject).getTextField().getAccessibleContext().setAccessibleName(str);
/* 140 */       this.spinners[j].getSlider().getAccessibleContext().setAccessibleDescription(str);
/* 141 */       this.spinners[j].getSpinner().getAccessibleContext().setAccessibleDescription(str);
/* 142 */       ((JSpinner.DefaultEditor)localObject).getTextField().getAccessibleContext().setAccessibleDescription(str);
/*     */     }
/*     */   }
/*     */ 
/*     */   void colorChanged() {
/* 147 */     this.color = new Color(getColor(0), true);
/* 148 */     Container localContainer = getParent();
/* 149 */     if ((localContainer instanceof ColorChooserPanel)) {
/* 150 */       ColorChooserPanel localColorChooserPanel = (ColorChooserPanel)localContainer;
/* 151 */       localColorChooserPanel.setSelectedColor(this.color);
/* 152 */       localColorChooserPanel.repaint();
/*     */     }
/*     */   }
/*     */ 
/*     */   float getValueX() {
/* 157 */     return this.spinners[this.x].getValue();
/*     */   }
/*     */ 
/*     */   float getValueY() {
/* 161 */     return 1.0F - this.spinners[this.y].getValue();
/*     */   }
/*     */ 
/*     */   float getValueZ() {
/* 165 */     return 1.0F - this.spinners[this.z].getValue();
/*     */   }
/*     */ 
/*     */   void setValue(float paramFloat) {
/* 169 */     this.spinners[this.z].setValue(1.0F - paramFloat);
/* 170 */     colorChanged();
/*     */   }
/*     */ 
/*     */   void setValue(float paramFloat1, float paramFloat2) {
/* 174 */     this.spinners[this.x].setValue(paramFloat1);
/* 175 */     this.spinners[this.y].setValue(1.0F - paramFloat2);
/* 176 */     colorChanged();
/*     */   }
/*     */ 
/*     */   int getColor(float paramFloat) {
/* 180 */     setDefaultValue(this.x);
/* 181 */     setDefaultValue(this.y);
/* 182 */     this.values[this.z] = (1.0F - paramFloat);
/* 183 */     return getColor(3);
/*     */   }
/*     */ 
/*     */   int getColor(float paramFloat1, float paramFloat2) {
/* 187 */     this.values[this.x] = paramFloat1;
/* 188 */     this.values[this.y] = (1.0F - paramFloat2);
/* 189 */     setValue(this.z);
/* 190 */     return getColor(3);
/*     */   }
/*     */ 
/*     */   void setColor(Color paramColor) {
/* 194 */     if (!paramColor.equals(this.color)) {
/* 195 */       this.color = paramColor;
/* 196 */       this.model.setColor(paramColor.getRGB(), this.values);
/* 197 */       for (int i = 0; i < this.model.getCount(); i++)
/* 198 */         this.spinners[i].setValue(this.values[i]);
/*     */     }
/*     */   }
/*     */ 
/*     */   private int getColor(int paramInt)
/*     */   {
/* 204 */     while (paramInt < this.model.getCount()) {
/* 205 */       setValue(paramInt++);
/*     */     }
/* 207 */     return this.model.getColor(this.values);
/*     */   }
/*     */ 
/*     */   private void setValue(int paramInt) {
/* 211 */     this.values[paramInt] = this.spinners[paramInt].getValue();
/*     */   }
/*     */ 
/*     */   private void setDefaultValue(int paramInt) {
/* 215 */     float f = this.model.getDefault(paramInt);
/* 216 */     this.values[paramInt] = (f < 0.0F ? this.spinners[paramInt].getValue() : f);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.colorchooser.ColorPanel
 * JD-Core Version:    0.6.2
 */