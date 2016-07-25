/*    */ package javax.swing.plaf.synth;
/*    */ 
/*    */ import java.awt.Insets;
/*    */ import java.awt.Rectangle;
/*    */ import javax.swing.JComboBox;
/*    */ import javax.swing.JList;
/*    */ import javax.swing.plaf.ComboBoxUI;
/*    */ import javax.swing.plaf.basic.BasicComboPopup;
/*    */ 
/*    */ class SynthComboPopup extends BasicComboPopup
/*    */ {
/*    */   public SynthComboPopup(JComboBox paramJComboBox)
/*    */   {
/* 41 */     super(paramJComboBox);
/*    */   }
/*    */ 
/*    */   protected void configureList()
/*    */   {
/* 53 */     this.list.setFont(this.comboBox.getFont());
/* 54 */     this.list.setCellRenderer(this.comboBox.getRenderer());
/* 55 */     this.list.setFocusable(false);
/* 56 */     this.list.setSelectionMode(0);
/* 57 */     int i = this.comboBox.getSelectedIndex();
/* 58 */     if (i == -1) {
/* 59 */       this.list.clearSelection();
/*    */     }
/*    */     else {
/* 62 */       this.list.setSelectedIndex(i);
/* 63 */       this.list.ensureIndexIsVisible(i);
/*    */     }
/* 65 */     installListListeners();
/*    */   }
/*    */ 
/*    */   protected Rectangle computePopupBounds(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*    */   {
/* 76 */     ComboBoxUI localComboBoxUI = this.comboBox.getUI();
/* 77 */     if ((localComboBoxUI instanceof SynthComboBoxUI)) {
/* 78 */       SynthComboBoxUI localSynthComboBoxUI = (SynthComboBoxUI)localComboBoxUI;
/* 79 */       if (localSynthComboBoxUI.popupInsets != null) {
/* 80 */         Insets localInsets = localSynthComboBoxUI.popupInsets;
/* 81 */         return super.computePopupBounds(paramInt1 + localInsets.left, paramInt2 + localInsets.top, paramInt3 - localInsets.left - localInsets.right, paramInt4 - localInsets.top - localInsets.bottom);
/*    */       }
/*    */ 
/*    */     }
/*    */ 
/* 88 */     return super.computePopupBounds(paramInt1, paramInt2, paramInt3, paramInt4);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.synth.SynthComboPopup
 * JD-Core Version:    0.6.2
 */