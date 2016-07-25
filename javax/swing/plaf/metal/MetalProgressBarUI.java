/*     */ package javax.swing.plaf.metal;
/*     */ 
/*     */ import java.awt.BasicStroke;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Insets;
/*     */ import java.awt.Rectangle;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JProgressBar;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.basic.BasicProgressBarUI;
/*     */ 
/*     */ public class MetalProgressBarUI extends BasicProgressBarUI
/*     */ {
/*     */   private Rectangle innards;
/*     */   private Rectangle box;
/*     */ 
/*     */   public static ComponentUI createUI(JComponent paramJComponent)
/*     */   {
/*  53 */     return new MetalProgressBarUI();
/*     */   }
/*     */ 
/*     */   public void paintDeterminate(Graphics paramGraphics, JComponent paramJComponent)
/*     */   {
/*  63 */     super.paintDeterminate(paramGraphics, paramJComponent);
/*     */ 
/*  65 */     if (!(paramGraphics instanceof Graphics2D)) {
/*  66 */       return;
/*     */     }
/*     */ 
/*  69 */     if (this.progressBar.isBorderPainted()) {
/*  70 */       Insets localInsets = this.progressBar.getInsets();
/*  71 */       int i = this.progressBar.getWidth() - (localInsets.left + localInsets.right);
/*  72 */       int j = this.progressBar.getHeight() - (localInsets.top + localInsets.bottom);
/*  73 */       int k = getAmountFull(localInsets, i, j);
/*  74 */       boolean bool = MetalUtils.isLeftToRight(paramJComponent);
/*     */ 
/*  80 */       int m = localInsets.left;
/*  81 */       int n = localInsets.top;
/*  82 */       int i1 = localInsets.left + i - 1;
/*  83 */       int i2 = localInsets.top + j - 1;
/*     */ 
/*  85 */       Graphics2D localGraphics2D = (Graphics2D)paramGraphics;
/*  86 */       localGraphics2D.setStroke(new BasicStroke(1.0F));
/*     */ 
/*  88 */       if (this.progressBar.getOrientation() == 0)
/*     */       {
/*  90 */         localGraphics2D.setColor(MetalLookAndFeel.getControlShadow());
/*  91 */         localGraphics2D.drawLine(m, n, i1, n);
/*     */ 
/*  93 */         if (k > 0)
/*     */         {
/*  95 */           localGraphics2D.setColor(MetalLookAndFeel.getPrimaryControlDarkShadow());
/*     */ 
/*  97 */           if (bool) {
/*  98 */             localGraphics2D.drawLine(m, n, m + k - 1, n);
/*     */           }
/*     */           else {
/* 101 */             localGraphics2D.drawLine(i1, n, i1 - k + 1, n);
/*     */ 
/* 103 */             if (this.progressBar.getPercentComplete() != 1.0D) {
/* 104 */               localGraphics2D.setColor(MetalLookAndFeel.getControlShadow());
/*     */             }
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/* 110 */         localGraphics2D.drawLine(m, n, m, i2);
/*     */       }
/*     */       else
/*     */       {
/* 114 */         localGraphics2D.setColor(MetalLookAndFeel.getControlShadow());
/* 115 */         localGraphics2D.drawLine(m, n, m, i2);
/*     */ 
/* 117 */         if (k > 0)
/*     */         {
/* 119 */           localGraphics2D.setColor(MetalLookAndFeel.getPrimaryControlDarkShadow());
/* 120 */           localGraphics2D.drawLine(m, i2, m, i2 - k + 1);
/*     */         }
/*     */ 
/* 125 */         localGraphics2D.setColor(MetalLookAndFeel.getControlShadow());
/*     */ 
/* 127 */         if (this.progressBar.getPercentComplete() == 1.0D) {
/* 128 */           localGraphics2D.setColor(MetalLookAndFeel.getPrimaryControlDarkShadow());
/*     */         }
/* 130 */         localGraphics2D.drawLine(m, n, i1, n);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void paintIndeterminate(Graphics paramGraphics, JComponent paramJComponent)
/*     */   {
/* 143 */     super.paintIndeterminate(paramGraphics, paramJComponent);
/*     */ 
/* 145 */     if ((!this.progressBar.isBorderPainted()) || (!(paramGraphics instanceof Graphics2D))) {
/* 146 */       return;
/*     */     }
/*     */ 
/* 149 */     Insets localInsets = this.progressBar.getInsets();
/* 150 */     int i = this.progressBar.getWidth() - (localInsets.left + localInsets.right);
/* 151 */     int j = this.progressBar.getHeight() - (localInsets.top + localInsets.bottom);
/* 152 */     int k = getAmountFull(localInsets, i, j);
/* 153 */     boolean bool = MetalUtils.isLeftToRight(paramJComponent);
/*     */ 
/* 155 */     Rectangle localRectangle = null;
/* 156 */     localRectangle = getBox(localRectangle);
/*     */ 
/* 161 */     int m = localInsets.left;
/* 162 */     int n = localInsets.top;
/* 163 */     int i1 = localInsets.left + i - 1;
/* 164 */     int i2 = localInsets.top + j - 1;
/*     */ 
/* 166 */     Graphics2D localGraphics2D = (Graphics2D)paramGraphics;
/* 167 */     localGraphics2D.setStroke(new BasicStroke(1.0F));
/*     */ 
/* 169 */     if (this.progressBar.getOrientation() == 0)
/*     */     {
/* 171 */       localGraphics2D.setColor(MetalLookAndFeel.getControlShadow());
/* 172 */       localGraphics2D.drawLine(m, n, i1, n);
/* 173 */       localGraphics2D.drawLine(m, n, m, i2);
/*     */ 
/* 176 */       localGraphics2D.setColor(MetalLookAndFeel.getPrimaryControlDarkShadow());
/* 177 */       localGraphics2D.drawLine(localRectangle.x, n, localRectangle.x + localRectangle.width - 1, n);
/*     */     }
/*     */     else
/*     */     {
/* 181 */       localGraphics2D.setColor(MetalLookAndFeel.getControlShadow());
/* 182 */       localGraphics2D.drawLine(m, n, m, i2);
/* 183 */       localGraphics2D.drawLine(m, n, i1, n);
/*     */ 
/* 186 */       localGraphics2D.setColor(MetalLookAndFeel.getPrimaryControlDarkShadow());
/* 187 */       localGraphics2D.drawLine(m, localRectangle.y, m, localRectangle.y + localRectangle.height - 1);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.metal.MetalProgressBarUI
 * JD-Core Version:    0.6.2
 */