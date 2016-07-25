/*    */ package javax.swing.colorchooser;
/*    */ 
/*    */ import javax.swing.JComponent;
/*    */ import javax.swing.JSlider;
/*    */ import javax.swing.JSpinner;
/*    */ import javax.swing.JSpinner.DefaultEditor;
/*    */ import javax.swing.SpinnerNumberModel;
/*    */ import javax.swing.event.ChangeEvent;
/*    */ import javax.swing.event.ChangeListener;
/*    */ 
/*    */ final class SlidingSpinner
/*    */   implements ChangeListener
/*    */ {
/*    */   private final ColorPanel panel;
/*    */   private final JComponent label;
/* 40 */   private final SpinnerNumberModel model = new SpinnerNumberModel();
/* 41 */   private final JSlider slider = new JSlider();
/* 42 */   private final JSpinner spinner = new JSpinner(this.model);
/*    */   private float value;
/*    */   private boolean internal;
/*    */ 
/*    */   SlidingSpinner(ColorPanel paramColorPanel, JComponent paramJComponent)
/*    */   {
/* 47 */     this.panel = paramColorPanel;
/* 48 */     this.label = paramJComponent;
/* 49 */     this.slider.addChangeListener(this);
/* 50 */     this.spinner.addChangeListener(this);
/* 51 */     JSpinner.DefaultEditor localDefaultEditor = (JSpinner.DefaultEditor)this.spinner.getEditor();
/* 52 */     ValueFormatter.init(3, false, localDefaultEditor.getTextField());
/* 53 */     localDefaultEditor.setFocusable(false);
/* 54 */     this.spinner.setFocusable(false);
/*    */   }
/*    */ 
/*    */   JComponent getLabel() {
/* 58 */     return this.label;
/*    */   }
/*    */ 
/*    */   JSlider getSlider() {
/* 62 */     return this.slider;
/*    */   }
/*    */ 
/*    */   JSpinner getSpinner() {
/* 66 */     return this.spinner;
/*    */   }
/*    */ 
/*    */   float getValue() {
/* 70 */     return this.value;
/*    */   }
/*    */ 
/*    */   void setValue(float paramFloat) {
/* 74 */     int i = this.slider.getMinimum();
/* 75 */     int j = this.slider.getMaximum();
/* 76 */     this.internal = true;
/* 77 */     this.slider.setValue(i + (int)(paramFloat * (j - i)));
/* 78 */     this.spinner.setValue(Integer.valueOf(this.slider.getValue()));
/* 79 */     this.internal = false;
/* 80 */     this.value = paramFloat;
/*    */   }
/*    */ 
/*    */   void setRange(int paramInt1, int paramInt2) {
/* 84 */     this.internal = true;
/* 85 */     this.slider.setMinimum(paramInt1);
/* 86 */     this.slider.setMaximum(paramInt2);
/* 87 */     this.model.setMinimum(Integer.valueOf(paramInt1));
/* 88 */     this.model.setMaximum(Integer.valueOf(paramInt2));
/* 89 */     this.internal = false;
/*    */   }
/*    */ 
/*    */   void setVisible(boolean paramBoolean) {
/* 93 */     this.label.setVisible(paramBoolean);
/* 94 */     this.slider.setVisible(paramBoolean);
/* 95 */     this.spinner.setVisible(paramBoolean);
/*    */   }
/*    */ 
/*    */   public void stateChanged(ChangeEvent paramChangeEvent) {
/* 99 */     if (!this.internal) {
/* 100 */       if (this.spinner == paramChangeEvent.getSource()) {
/* 101 */         Object localObject = this.spinner.getValue();
/* 102 */         if ((localObject instanceof Integer)) {
/* 103 */           this.internal = true;
/* 104 */           this.slider.setValue(((Integer)localObject).intValue());
/* 105 */           this.internal = false;
/*    */         }
/*    */       }
/* 108 */       int i = this.slider.getValue();
/* 109 */       this.internal = true;
/* 110 */       this.spinner.setValue(Integer.valueOf(i));
/* 111 */       this.internal = false;
/* 112 */       int j = this.slider.getMinimum();
/* 113 */       int k = this.slider.getMaximum();
/* 114 */       this.value = ((i - j) / (k - j));
/* 115 */       this.panel.colorChanged();
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.colorchooser.SlidingSpinner
 * JD-Core Version:    0.6.2
 */