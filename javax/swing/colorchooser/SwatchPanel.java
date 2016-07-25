/*     */ package javax.swing.colorchooser;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.ComponentOrientation;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.event.FocusAdapter;
/*     */ import java.awt.event.FocusEvent;
/*     */ import java.awt.event.KeyAdapter;
/*     */ import java.awt.event.KeyEvent;
/*     */ import java.awt.event.MouseEvent;
/*     */ import javax.swing.JPanel;
/*     */ 
/*     */ class SwatchPanel extends JPanel
/*     */ {
/*     */   protected Color[] colors;
/*     */   protected Dimension swatchSize;
/*     */   protected Dimension numSwatches;
/*     */   protected Dimension gap;
/*     */   private int selRow;
/*     */   private int selCol;
/*     */ 
/*     */   public SwatchPanel()
/*     */   {
/* 281 */     initValues();
/* 282 */     initColors();
/* 283 */     setToolTipText("");
/* 284 */     setOpaque(true);
/* 285 */     setBackground(Color.white);
/* 286 */     setFocusable(true);
/* 287 */     setInheritsPopupMenu(true);
/*     */ 
/* 289 */     addFocusListener(new FocusAdapter() {
/*     */       public void focusGained(FocusEvent paramAnonymousFocusEvent) {
/* 291 */         SwatchPanel.this.repaint();
/*     */       }
/*     */ 
/*     */       public void focusLost(FocusEvent paramAnonymousFocusEvent) {
/* 295 */         SwatchPanel.this.repaint();
/*     */       }
/*     */     });
/* 299 */     addKeyListener(new KeyAdapter() {
/*     */       public void keyPressed(KeyEvent paramAnonymousKeyEvent) {
/* 301 */         int i = paramAnonymousKeyEvent.getKeyCode();
/* 302 */         switch (i) {
/*     */         case 38:
/* 304 */           if (SwatchPanel.this.selRow > 0) {
/* 305 */             SwatchPanel.access$010(SwatchPanel.this);
/* 306 */             SwatchPanel.this.repaint(); } break;
/*     */         case 40:
/* 310 */           if (SwatchPanel.this.selRow < SwatchPanel.this.numSwatches.height - 1) {
/* 311 */             SwatchPanel.access$008(SwatchPanel.this);
/* 312 */             SwatchPanel.this.repaint(); } break;
/*     */         case 37:
/* 316 */           if ((SwatchPanel.this.selCol > 0) && (SwatchPanel.this.getComponentOrientation().isLeftToRight())) {
/* 317 */             SwatchPanel.access$110(SwatchPanel.this);
/* 318 */             SwatchPanel.this.repaint();
/* 319 */           } else if ((SwatchPanel.this.selCol < SwatchPanel.this.numSwatches.width - 1) && (!SwatchPanel.this.getComponentOrientation().isLeftToRight()))
/*     */           {
/* 321 */             SwatchPanel.access$108(SwatchPanel.this);
/* 322 */             SwatchPanel.this.repaint(); } break;
/*     */         case 39:
/* 326 */           if ((SwatchPanel.this.selCol < SwatchPanel.this.numSwatches.width - 1) && (SwatchPanel.this.getComponentOrientation().isLeftToRight()))
/*     */           {
/* 328 */             SwatchPanel.access$108(SwatchPanel.this);
/* 329 */             SwatchPanel.this.repaint();
/* 330 */           } else if ((SwatchPanel.this.selCol > 0) && (!SwatchPanel.this.getComponentOrientation().isLeftToRight())) {
/* 331 */             SwatchPanel.access$110(SwatchPanel.this);
/* 332 */             SwatchPanel.this.repaint(); } break;
/*     */         case 36:
/* 336 */           SwatchPanel.this.selCol = 0;
/* 337 */           SwatchPanel.this.selRow = 0;
/* 338 */           SwatchPanel.this.repaint();
/* 339 */           break;
/*     */         case 35:
/* 341 */           SwatchPanel.this.selCol = (SwatchPanel.this.numSwatches.width - 1);
/* 342 */           SwatchPanel.this.selRow = (SwatchPanel.this.numSwatches.height - 1);
/* 343 */           SwatchPanel.this.repaint();
/*     */         }
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   public Color getSelectedColor()
/*     */   {
/* 351 */     return getColorForCell(this.selCol, this.selRow);
/*     */   }
/*     */ 
/*     */   protected void initValues()
/*     */   {
/*     */   }
/*     */ 
/*     */   public void paintComponent(Graphics paramGraphics) {
/* 359 */     paramGraphics.setColor(getBackground());
/* 360 */     paramGraphics.fillRect(0, 0, getWidth(), getHeight());
/* 361 */     for (int i = 0; i < this.numSwatches.height; i++) {
/* 362 */       int j = i * (this.swatchSize.height + this.gap.height);
/* 363 */       for (int k = 0; k < this.numSwatches.width; k++) {
/* 364 */         Color localColor1 = getColorForCell(k, i);
/* 365 */         paramGraphics.setColor(localColor1);
/*     */         int m;
/* 367 */         if (!getComponentOrientation().isLeftToRight())
/* 368 */           m = (this.numSwatches.width - k - 1) * (this.swatchSize.width + this.gap.width);
/*     */         else {
/* 370 */           m = k * (this.swatchSize.width + this.gap.width);
/*     */         }
/* 372 */         paramGraphics.fillRect(m, j, this.swatchSize.width, this.swatchSize.height);
/* 373 */         paramGraphics.setColor(Color.black);
/* 374 */         paramGraphics.drawLine(m + this.swatchSize.width - 1, j, m + this.swatchSize.width - 1, j + this.swatchSize.height - 1);
/* 375 */         paramGraphics.drawLine(m, j + this.swatchSize.height - 1, m + this.swatchSize.width - 1, j + this.swatchSize.height - 1);
/*     */ 
/* 377 */         if ((this.selRow == i) && (this.selCol == k) && (isFocusOwner())) {
/* 378 */           Color localColor2 = new Color(localColor1.getRed() < 125 ? 255 : 0, localColor1.getGreen() < 125 ? 255 : 0, localColor1.getBlue() < 125 ? 255 : 0);
/*     */ 
/* 381 */           paramGraphics.setColor(localColor2);
/*     */ 
/* 383 */           paramGraphics.drawLine(m, j, m + this.swatchSize.width - 1, j);
/* 384 */           paramGraphics.drawLine(m, j, m, j + this.swatchSize.height - 1);
/* 385 */           paramGraphics.drawLine(m + this.swatchSize.width - 1, j, m + this.swatchSize.width - 1, j + this.swatchSize.height - 1);
/* 386 */           paramGraphics.drawLine(m, j + this.swatchSize.height - 1, m + this.swatchSize.width - 1, j + this.swatchSize.height - 1);
/* 387 */           paramGraphics.drawLine(m, j, m + this.swatchSize.width - 1, j + this.swatchSize.height - 1);
/* 388 */           paramGraphics.drawLine(m, j + this.swatchSize.height - 1, m + this.swatchSize.width - 1, j);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public Dimension getPreferredSize() {
/* 395 */     int i = this.numSwatches.width * (this.swatchSize.width + this.gap.width) - 1;
/* 396 */     int j = this.numSwatches.height * (this.swatchSize.height + this.gap.height) - 1;
/* 397 */     return new Dimension(i, j);
/*     */   }
/*     */ 
/*     */   protected void initColors()
/*     */   {
/*     */   }
/*     */ 
/*     */   public String getToolTipText(MouseEvent paramMouseEvent)
/*     */   {
/* 406 */     Color localColor = getColorForLocation(paramMouseEvent.getX(), paramMouseEvent.getY());
/* 407 */     return localColor.getRed() + ", " + localColor.getGreen() + ", " + localColor.getBlue();
/*     */   }
/*     */ 
/*     */   public void setSelectedColorFromLocation(int paramInt1, int paramInt2) {
/* 411 */     if (!getComponentOrientation().isLeftToRight())
/* 412 */       this.selCol = (this.numSwatches.width - paramInt1 / (this.swatchSize.width + this.gap.width) - 1);
/*     */     else {
/* 414 */       this.selCol = (paramInt1 / (this.swatchSize.width + this.gap.width));
/*     */     }
/* 416 */     this.selRow = (paramInt2 / (this.swatchSize.height + this.gap.height));
/* 417 */     repaint();
/*     */   }
/*     */ 
/*     */   public Color getColorForLocation(int paramInt1, int paramInt2)
/*     */   {
/*     */     int i;
/* 422 */     if (!getComponentOrientation().isLeftToRight())
/* 423 */       i = this.numSwatches.width - paramInt1 / (this.swatchSize.width + this.gap.width) - 1;
/*     */     else {
/* 425 */       i = paramInt1 / (this.swatchSize.width + this.gap.width);
/*     */     }
/* 427 */     int j = paramInt2 / (this.swatchSize.height + this.gap.height);
/* 428 */     return getColorForCell(i, j);
/*     */   }
/*     */ 
/*     */   private Color getColorForCell(int paramInt1, int paramInt2) {
/* 432 */     return this.colors[(paramInt2 * this.numSwatches.width + paramInt1)];
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.colorchooser.SwatchPanel
 * JD-Core Version:    0.6.2
 */