/*     */ package javax.swing.colorchooser;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Container;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.GridBagConstraints;
/*     */ import java.awt.GridBagLayout;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import javax.accessibility.AccessibleContext;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JFormattedTextField;
/*     */ import javax.swing.JLabel;
/*     */ 
/*     */ final class ColorChooserPanel extends AbstractColorChooserPanel
/*     */   implements PropertyChangeListener
/*     */ {
/*     */   private static final int MASK = -16777216;
/*     */   private final ColorModel model;
/*     */   private final ColorPanel panel;
/*     */   private final DiagramComponent slider;
/*     */   private final DiagramComponent diagram;
/*     */   private final JFormattedTextField text;
/*     */   private final JLabel label;
/*     */ 
/*     */   ColorChooserPanel(ColorModel paramColorModel)
/*     */   {
/*  52 */     this.model = paramColorModel;
/*  53 */     this.panel = new ColorPanel(this.model);
/*  54 */     this.slider = new DiagramComponent(this.panel, false);
/*  55 */     this.diagram = new DiagramComponent(this.panel, true);
/*  56 */     this.text = new JFormattedTextField();
/*  57 */     this.label = new JLabel(null, null, 4);
/*  58 */     ValueFormatter.init(6, true, this.text);
/*     */   }
/*     */ 
/*     */   public void setEnabled(boolean paramBoolean)
/*     */   {
/*  63 */     super.setEnabled(paramBoolean);
/*  64 */     setEnabled(this, paramBoolean);
/*     */   }
/*     */ 
/*     */   private static void setEnabled(Container paramContainer, boolean paramBoolean) {
/*  68 */     for (Component localComponent : paramContainer.getComponents()) {
/*  69 */       localComponent.setEnabled(paramBoolean);
/*  70 */       if ((localComponent instanceof Container))
/*  71 */         setEnabled((Container)localComponent, paramBoolean);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void updateChooser()
/*     */   {
/*  78 */     Color localColor = getColorFromModel();
/*  79 */     if (localColor != null) {
/*  80 */       this.panel.setColor(localColor);
/*  81 */       this.text.setValue(Integer.valueOf(localColor.getRGB()));
/*  82 */       this.slider.repaint();
/*  83 */       this.diagram.repaint();
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void buildChooser()
/*     */   {
/*  89 */     if (0 == getComponentCount()) {
/*  90 */       setLayout(new GridBagLayout());
/*     */ 
/*  92 */       localObject = new GridBagConstraints();
/*     */ 
/*  94 */       ((GridBagConstraints)localObject).gridx = 3;
/*  95 */       ((GridBagConstraints)localObject).gridwidth = 2;
/*  96 */       ((GridBagConstraints)localObject).weighty = 1.0D;
/*  97 */       ((GridBagConstraints)localObject).anchor = 11;
/*  98 */       ((GridBagConstraints)localObject).fill = 2;
/*  99 */       ((GridBagConstraints)localObject).insets.top = 10;
/* 100 */       ((GridBagConstraints)localObject).insets.right = 10;
/* 101 */       add(this.panel, localObject);
/*     */ 
/* 103 */       ((GridBagConstraints)localObject).gridwidth = 1;
/* 104 */       ((GridBagConstraints)localObject).weightx = 1.0D;
/* 105 */       ((GridBagConstraints)localObject).weighty = 0.0D;
/* 106 */       ((GridBagConstraints)localObject).anchor = 10;
/* 107 */       ((GridBagConstraints)localObject).insets.right = 5;
/* 108 */       ((GridBagConstraints)localObject).insets.bottom = 10;
/* 109 */       add(this.label, localObject);
/*     */ 
/* 111 */       ((GridBagConstraints)localObject).gridx = 4;
/* 112 */       ((GridBagConstraints)localObject).weightx = 0.0D;
/* 113 */       ((GridBagConstraints)localObject).insets.right = 10;
/* 114 */       add(this.text, localObject);
/*     */ 
/* 116 */       ((GridBagConstraints)localObject).gridx = 2;
/* 117 */       ((GridBagConstraints)localObject).gridheight = 2;
/* 118 */       ((GridBagConstraints)localObject).anchor = 11;
/* 119 */       ((GridBagConstraints)localObject).ipadx = this.text.getPreferredSize().height;
/* 120 */       ((GridBagConstraints)localObject).ipady = getPreferredSize().height;
/* 121 */       add(this.slider, localObject);
/*     */ 
/* 123 */       ((GridBagConstraints)localObject).gridx = 1;
/* 124 */       ((GridBagConstraints)localObject).insets.left = 10;
/* 125 */       ((GridBagConstraints)localObject).ipadx = ((GridBagConstraints)localObject).ipady;
/* 126 */       add(this.diagram, localObject);
/*     */ 
/* 128 */       this.label.setLabelFor(this.text);
/* 129 */       this.text.addPropertyChangeListener("value", this);
/* 130 */       this.slider.setBorder(this.text.getBorder());
/* 131 */       this.diagram.setBorder(this.text.getBorder());
/*     */ 
/* 133 */       setInheritsPopupMenu(this, true);
/*     */     }
/* 135 */     Object localObject = this.model.getText(this, "HexCode");
/* 136 */     boolean bool = localObject != null;
/* 137 */     this.text.setVisible(bool);
/* 138 */     this.text.getAccessibleContext().setAccessibleDescription((String)localObject);
/* 139 */     this.label.setVisible(bool);
/* 140 */     if (bool) {
/* 141 */       this.label.setText((String)localObject);
/* 142 */       int i = this.model.getInteger(this, "HexCodeMnemonic");
/* 143 */       if (i > 0) {
/* 144 */         this.label.setDisplayedMnemonic(i);
/* 145 */         i = this.model.getInteger(this, "HexCodeMnemonicIndex");
/* 146 */         if (i >= 0) {
/* 147 */           this.label.setDisplayedMnemonicIndex(i);
/*     */         }
/*     */       }
/*     */     }
/* 151 */     this.panel.buildPanel();
/*     */   }
/*     */ 
/*     */   public String getDisplayName()
/*     */   {
/* 156 */     return this.model.getText(this, "Name");
/*     */   }
/*     */ 
/*     */   public int getMnemonic()
/*     */   {
/* 161 */     return this.model.getInteger(this, "Mnemonic");
/*     */   }
/*     */ 
/*     */   public int getDisplayedMnemonicIndex()
/*     */   {
/* 166 */     return this.model.getInteger(this, "DisplayedMnemonicIndex");
/*     */   }
/*     */ 
/*     */   public Icon getSmallDisplayIcon()
/*     */   {
/* 171 */     return null;
/*     */   }
/*     */ 
/*     */   public Icon getLargeDisplayIcon()
/*     */   {
/* 176 */     return null;
/*     */   }
/*     */ 
/*     */   public void propertyChange(PropertyChangeEvent paramPropertyChangeEvent) {
/* 180 */     ColorSelectionModel localColorSelectionModel = getColorSelectionModel();
/* 181 */     if (localColorSelectionModel != null) {
/* 182 */       Object localObject = paramPropertyChangeEvent.getNewValue();
/* 183 */       if ((localObject instanceof Integer)) {
/* 184 */         int i = 0xFF000000 & localColorSelectionModel.getSelectedColor().getRGB() | ((Integer)localObject).intValue();
/* 185 */         localColorSelectionModel.setSelectedColor(new Color(i, true));
/*     */       }
/*     */     }
/* 188 */     this.text.selectAll();
/*     */   }
/*     */ 
/*     */   private static void setInheritsPopupMenu(JComponent paramJComponent, boolean paramBoolean)
/*     */   {
/* 198 */     paramJComponent.setInheritsPopupMenu(paramBoolean);
/* 199 */     for (Component localComponent : paramJComponent.getComponents())
/* 200 */       if ((localComponent instanceof JComponent))
/* 201 */         setInheritsPopupMenu((JComponent)localComponent, paramBoolean);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.colorchooser.ColorChooserPanel
 * JD-Core Version:    0.6.2
 */