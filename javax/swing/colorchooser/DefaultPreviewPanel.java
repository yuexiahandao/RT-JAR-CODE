/*     */ package javax.swing.colorchooser;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.ComponentOrientation;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Font;
/*     */ import java.awt.FontMetrics;
/*     */ import java.awt.Graphics;
/*     */ import javax.swing.JColorChooser;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.UIManager;
/*     */ import sun.swing.SwingUtilities2;
/*     */ 
/*     */ class DefaultPreviewPanel extends JPanel
/*     */ {
/*  58 */   private int squareSize = 25;
/*  59 */   private int squareGap = 5;
/*  60 */   private int innerGap = 5;
/*     */ 
/*  63 */   private int textGap = 5;
/*  64 */   private Font font = new Font("Dialog", 0, 12);
/*     */   private String sampleText;
/*  67 */   private int swatchWidth = 50;
/*     */ 
/*  69 */   private Color oldColor = null;
/*     */ 
/*     */   private JColorChooser getColorChooser() {
/*  72 */     return (JColorChooser)SwingUtilities.getAncestorOfClass(JColorChooser.class, this);
/*     */   }
/*     */ 
/*     */   public Dimension getPreferredSize()
/*     */   {
/*  77 */     Object localObject = getColorChooser();
/*  78 */     if (localObject == null) {
/*  79 */       localObject = this;
/*     */     }
/*  81 */     FontMetrics localFontMetrics = ((JComponent)localObject).getFontMetrics(getFont());
/*     */ 
/*  83 */     int i = localFontMetrics.getAscent();
/*  84 */     int j = localFontMetrics.getHeight();
/*  85 */     int k = SwingUtilities2.stringWidth((JComponent)localObject, localFontMetrics, getSampleText());
/*     */ 
/*  87 */     int m = j * 3 + this.textGap * 3;
/*  88 */     int n = this.squareSize * 3 + this.squareGap * 2 + this.swatchWidth + k + this.textGap * 3;
/*  89 */     return new Dimension(n, m);
/*     */   }
/*     */ 
/*     */   public void paintComponent(Graphics paramGraphics) {
/*  93 */     if (this.oldColor == null) {
/*  94 */       this.oldColor = getForeground();
/*     */     }
/*  96 */     paramGraphics.setColor(getBackground());
/*  97 */     paramGraphics.fillRect(0, 0, getWidth(), getHeight());
/*     */     int i;
/*     */     int j;
/*  99 */     if (getComponentOrientation().isLeftToRight()) {
/* 100 */       i = paintSquares(paramGraphics, 0);
/* 101 */       j = paintText(paramGraphics, i);
/* 102 */       paintSwatch(paramGraphics, i + j);
/*     */     } else {
/* 104 */       i = paintSwatch(paramGraphics, 0);
/* 105 */       j = paintText(paramGraphics, i);
/* 106 */       paintSquares(paramGraphics, i + j);
/*     */     }
/*     */   }
/*     */ 
/*     */   private int paintSwatch(Graphics paramGraphics, int paramInt)
/*     */   {
/* 112 */     int i = paramInt;
/* 113 */     paramGraphics.setColor(this.oldColor);
/* 114 */     paramGraphics.fillRect(i, 0, this.swatchWidth, this.squareSize + this.squareGap / 2);
/* 115 */     paramGraphics.setColor(getForeground());
/* 116 */     paramGraphics.fillRect(i, this.squareSize + this.squareGap / 2, this.swatchWidth, this.squareSize + this.squareGap / 2);
/* 117 */     return i + this.swatchWidth;
/*     */   }
/*     */ 
/*     */   private int paintText(Graphics paramGraphics, int paramInt) {
/* 121 */     paramGraphics.setFont(getFont());
/* 122 */     Object localObject = getColorChooser();
/* 123 */     if (localObject == null) {
/* 124 */       localObject = this;
/*     */     }
/* 126 */     FontMetrics localFontMetrics = SwingUtilities2.getFontMetrics((JComponent)localObject, paramGraphics);
/*     */ 
/* 128 */     int i = localFontMetrics.getAscent();
/* 129 */     int j = localFontMetrics.getHeight();
/* 130 */     int k = SwingUtilities2.stringWidth((JComponent)localObject, localFontMetrics, getSampleText());
/*     */ 
/* 132 */     int m = paramInt + this.textGap;
/*     */ 
/* 134 */     Color localColor = getForeground();
/*     */ 
/* 136 */     paramGraphics.setColor(localColor);
/*     */ 
/* 138 */     SwingUtilities2.drawString((JComponent)localObject, paramGraphics, getSampleText(), m + this.textGap / 2, i + 2);
/*     */ 
/* 141 */     paramGraphics.fillRect(m, j + this.textGap, k + this.textGap, j + 2);
/*     */ 
/* 146 */     paramGraphics.setColor(Color.black);
/* 147 */     SwingUtilities2.drawString((JComponent)localObject, paramGraphics, getSampleText(), m + this.textGap / 2, j + i + this.textGap + 2);
/*     */ 
/* 152 */     paramGraphics.setColor(Color.white);
/*     */ 
/* 154 */     paramGraphics.fillRect(m, (j + this.textGap) * 2, k + this.textGap, j + 2);
/*     */ 
/* 159 */     paramGraphics.setColor(localColor);
/* 160 */     SwingUtilities2.drawString((JComponent)localObject, paramGraphics, getSampleText(), m + this.textGap / 2, (j + this.textGap) * 2 + i + 2);
/*     */ 
/* 164 */     return k + this.textGap * 3;
/*     */   }
/*     */ 
/*     */   private int paintSquares(Graphics paramGraphics, int paramInt)
/*     */   {
/* 170 */     int i = paramInt;
/* 171 */     Color localColor = getForeground();
/*     */ 
/* 173 */     paramGraphics.setColor(Color.white);
/* 174 */     paramGraphics.fillRect(i, 0, this.squareSize, this.squareSize);
/* 175 */     paramGraphics.setColor(localColor);
/* 176 */     paramGraphics.fillRect(i + this.innerGap, this.innerGap, this.squareSize - this.innerGap * 2, this.squareSize - this.innerGap * 2);
/*     */ 
/* 180 */     paramGraphics.setColor(Color.white);
/* 181 */     paramGraphics.fillRect(i + this.innerGap * 2, this.innerGap * 2, this.squareSize - this.innerGap * 4, this.squareSize - this.innerGap * 4);
/*     */ 
/* 186 */     paramGraphics.setColor(localColor);
/* 187 */     paramGraphics.fillRect(i, this.squareSize + this.squareGap, this.squareSize, this.squareSize);
/*     */ 
/* 189 */     paramGraphics.translate(this.squareSize + this.squareGap, 0);
/* 190 */     paramGraphics.setColor(Color.black);
/* 191 */     paramGraphics.fillRect(i, 0, this.squareSize, this.squareSize);
/* 192 */     paramGraphics.setColor(localColor);
/* 193 */     paramGraphics.fillRect(i + this.innerGap, this.innerGap, this.squareSize - this.innerGap * 2, this.squareSize - this.innerGap * 2);
/*     */ 
/* 197 */     paramGraphics.setColor(Color.white);
/* 198 */     paramGraphics.fillRect(i + this.innerGap * 2, this.innerGap * 2, this.squareSize - this.innerGap * 4, this.squareSize - this.innerGap * 4);
/*     */ 
/* 202 */     paramGraphics.translate(-(this.squareSize + this.squareGap), 0);
/*     */ 
/* 204 */     paramGraphics.translate(this.squareSize + this.squareGap, this.squareSize + this.squareGap);
/* 205 */     paramGraphics.setColor(Color.white);
/* 206 */     paramGraphics.fillRect(i, 0, this.squareSize, this.squareSize);
/* 207 */     paramGraphics.setColor(localColor);
/* 208 */     paramGraphics.fillRect(i + this.innerGap, this.innerGap, this.squareSize - this.innerGap * 2, this.squareSize - this.innerGap * 2);
/*     */ 
/* 212 */     paramGraphics.translate(-(this.squareSize + this.squareGap), -(this.squareSize + this.squareGap));
/*     */ 
/* 216 */     paramGraphics.translate((this.squareSize + this.squareGap) * 2, 0);
/* 217 */     paramGraphics.setColor(Color.white);
/* 218 */     paramGraphics.fillRect(i, 0, this.squareSize, this.squareSize);
/* 219 */     paramGraphics.setColor(localColor);
/* 220 */     paramGraphics.fillRect(i + this.innerGap, this.innerGap, this.squareSize - this.innerGap * 2, this.squareSize - this.innerGap * 2);
/*     */ 
/* 224 */     paramGraphics.setColor(Color.black);
/* 225 */     paramGraphics.fillRect(i + this.innerGap * 2, this.innerGap * 2, this.squareSize - this.innerGap * 4, this.squareSize - this.innerGap * 4);
/*     */ 
/* 229 */     paramGraphics.translate(-((this.squareSize + this.squareGap) * 2), 0);
/*     */ 
/* 231 */     paramGraphics.translate((this.squareSize + this.squareGap) * 2, this.squareSize + this.squareGap);
/* 232 */     paramGraphics.setColor(Color.black);
/* 233 */     paramGraphics.fillRect(i, 0, this.squareSize, this.squareSize);
/* 234 */     paramGraphics.setColor(localColor);
/* 235 */     paramGraphics.fillRect(i + this.innerGap, this.innerGap, this.squareSize - this.innerGap * 2, this.squareSize - this.innerGap * 2);
/*     */ 
/* 239 */     paramGraphics.translate(-((this.squareSize + this.squareGap) * 2), -(this.squareSize + this.squareGap));
/*     */ 
/* 241 */     return this.squareSize * 3 + this.squareGap * 2;
/*     */   }
/*     */ 
/*     */   private String getSampleText()
/*     */   {
/* 246 */     if (this.sampleText == null) {
/* 247 */       this.sampleText = UIManager.getString("ColorChooser.sampleText", getLocale());
/*     */     }
/* 249 */     return this.sampleText;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.colorchooser.DefaultPreviewPanel
 * JD-Core Version:    0.6.2
 */