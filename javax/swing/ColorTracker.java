/*     */ package javax.swing;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.io.Serializable;
/*     */ 
/*     */ class ColorTracker
/*     */   implements ActionListener, Serializable
/*     */ {
/*     */   JColorChooser chooser;
/*     */   Color color;
/*     */ 
/*     */   public ColorTracker(JColorChooser paramJColorChooser)
/*     */   {
/* 753 */     this.chooser = paramJColorChooser;
/*     */   }
/*     */ 
/*     */   public void actionPerformed(ActionEvent paramActionEvent) {
/* 757 */     this.color = this.chooser.getColor();
/*     */   }
/*     */ 
/*     */   public Color getColor() {
/* 761 */     return this.color;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.ColorTracker
 * JD-Core Version:    0.6.2
 */