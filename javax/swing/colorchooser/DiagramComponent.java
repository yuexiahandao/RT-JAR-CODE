/*     */ package javax.swing.colorchooser;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Insets;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.awt.event.MouseListener;
/*     */ import java.awt.event.MouseMotionListener;
/*     */ import java.awt.image.BufferedImage;
/*     */ import javax.swing.JComponent;
/*     */ 
/*     */ final class DiagramComponent extends JComponent
/*     */   implements MouseListener, MouseMotionListener
/*     */ {
/*     */   private final ColorPanel panel;
/*     */   private final boolean diagram;
/*  42 */   private final Insets insets = new Insets(0, 0, 0, 0);
/*     */   private int width;
/*     */   private int height;
/*     */   private int[] array;
/*     */   private BufferedImage image;
/*     */ 
/*     */   DiagramComponent(ColorPanel paramColorPanel, boolean paramBoolean)
/*     */   {
/*  51 */     this.panel = paramColorPanel;
/*  52 */     this.diagram = paramBoolean;
/*  53 */     addMouseListener(this);
/*  54 */     addMouseMotionListener(this);
/*     */   }
/*     */ 
/*     */   protected void paintComponent(Graphics paramGraphics)
/*     */   {
/*  59 */     getInsets(this.insets);
/*  60 */     this.width = (getWidth() - this.insets.left - this.insets.right);
/*  61 */     this.height = (getHeight() - this.insets.top - this.insets.bottom);
/*     */ 
/*  63 */     int i = (this.image == null) || (this.width != this.image.getWidth()) || (this.height != this.image.getHeight()) ? 1 : 0;
/*     */ 
/*  66 */     if (i != 0) {
/*  67 */       int j = this.width * this.height;
/*  68 */       if ((this.array == null) || (this.array.length < j)) {
/*  69 */         this.array = new int[j];
/*     */       }
/*  71 */       this.image = new BufferedImage(this.width, this.height, 1);
/*     */     }
/*     */ 
/*  74 */     float f1 = 1.0F / (this.width - 1);
/*  75 */     float f2 = 1.0F / (this.height - 1);
/*     */ 
/*  77 */     int n = 0;
/*  78 */     float f3 = 0.0F;
/*  79 */     for (int i1 = 0; i1 < this.height; f3 += f2)
/*     */     {
/*     */       int i3;
/*  80 */       if (this.diagram) {
/*  81 */         float f4 = 0.0F;
/*  82 */         for (i3 = 0; i3 < this.width; n++) {
/*  83 */           this.array[n] = this.panel.getColor(f4, f3);
/*     */ 
/*  82 */           i3++; f4 += f1;
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/*  87 */         int i2 = this.panel.getColor(f3);
/*  88 */         for (i3 = 0; i3 < this.width; n++) {
/*  89 */           this.array[n] = i2;
/*     */ 
/*  88 */           i3++;
/*     */         }
/*     */       }
/*  79 */       i1++;
/*     */     }
/*     */ 
/*  94 */     this.image.setRGB(0, 0, this.width, this.height, this.array, 0, this.width);
/*  95 */     paramGraphics.drawImage(this.image, this.insets.left, this.insets.top, this.width, this.height, this);
/*  96 */     if (isEnabled()) {
/*  97 */       this.width -= 1;
/*  98 */       this.height -= 1;
/*  99 */       paramGraphics.setXORMode(Color.WHITE);
/* 100 */       paramGraphics.setColor(Color.BLACK);
/*     */       int k;
/* 101 */       if (this.diagram) {
/* 102 */         k = getValue(this.panel.getValueX(), this.insets.left, this.width);
/* 103 */         int m = getValue(this.panel.getValueY(), this.insets.top, this.height);
/* 104 */         paramGraphics.drawLine(k - 8, m, k + 8, m);
/* 105 */         paramGraphics.drawLine(k, m - 8, k, m + 8);
/*     */       }
/*     */       else {
/* 108 */         k = getValue(this.panel.getValueZ(), this.insets.top, this.height);
/* 109 */         paramGraphics.drawLine(this.insets.left, k, this.insets.left + this.width, k);
/*     */       }
/* 111 */       paramGraphics.setPaintMode();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void mousePressed(MouseEvent paramMouseEvent) {
/* 116 */     mouseDragged(paramMouseEvent);
/*     */   }
/*     */ 
/*     */   public void mouseReleased(MouseEvent paramMouseEvent) {
/*     */   }
/*     */ 
/*     */   public void mouseClicked(MouseEvent paramMouseEvent) {
/*     */   }
/*     */ 
/*     */   public void mouseEntered(MouseEvent paramMouseEvent) {
/*     */   }
/*     */ 
/*     */   public void mouseExited(MouseEvent paramMouseEvent) {
/*     */   }
/*     */ 
/*     */   public void mouseMoved(MouseEvent paramMouseEvent) {
/*     */   }
/*     */ 
/*     */   public void mouseDragged(MouseEvent paramMouseEvent) {
/* 135 */     if (isEnabled()) {
/* 136 */       float f1 = getValue(paramMouseEvent.getY(), this.insets.top, this.height);
/* 137 */       if (this.diagram) {
/* 138 */         float f2 = getValue(paramMouseEvent.getX(), this.insets.left, this.width);
/* 139 */         this.panel.setValue(f2, f1);
/*     */       }
/*     */       else {
/* 142 */         this.panel.setValue(f1);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private static int getValue(float paramFloat, int paramInt1, int paramInt2) {
/* 148 */     return paramInt1 + (int)(paramFloat * paramInt2);
/*     */   }
/*     */ 
/*     */   private static float getValue(int paramInt1, int paramInt2, int paramInt3) {
/* 152 */     if (paramInt2 < paramInt1) {
/* 153 */       paramInt1 -= paramInt2;
/* 154 */       return paramInt1 < paramInt3 ? paramInt1 / paramInt3 : 1.0F;
/*     */     }
/*     */ 
/* 158 */     return 0.0F;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.colorchooser.DiagramComponent
 * JD-Core Version:    0.6.2
 */