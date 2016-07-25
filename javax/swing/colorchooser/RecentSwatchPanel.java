/*     */ package javax.swing.colorchooser;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Dimension;
/*     */ import javax.swing.UIManager;
/*     */ 
/*     */ class RecentSwatchPanel extends SwatchPanel
/*     */ {
/*     */   protected void initValues()
/*     */   {
/* 442 */     this.swatchSize = UIManager.getDimension("ColorChooser.swatchesRecentSwatchSize", getLocale());
/* 443 */     this.numSwatches = new Dimension(5, 7);
/* 444 */     this.gap = new Dimension(1, 1);
/*     */   }
/*     */ 
/*     */   protected void initColors()
/*     */   {
/* 449 */     Color localColor = UIManager.getColor("ColorChooser.swatchesDefaultRecentColor", getLocale());
/* 450 */     int i = this.numSwatches.width * this.numSwatches.height;
/*     */ 
/* 452 */     this.colors = new Color[i];
/* 453 */     for (int j = 0; j < i; j++)
/* 454 */       this.colors[j] = localColor;
/*     */   }
/*     */ 
/*     */   public void setMostRecentColor(Color paramColor)
/*     */   {
/* 460 */     System.arraycopy(this.colors, 0, this.colors, 1, this.colors.length - 1);
/* 461 */     this.colors[0] = paramColor;
/* 462 */     repaint();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.colorchooser.RecentSwatchPanel
 * JD-Core Version:    0.6.2
 */