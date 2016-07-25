/*     */ package javax.swing.plaf.metal;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.GraphicsConfiguration;
/*     */ import java.awt.Image;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.awt.image.IndexColorModel;
/*     */ 
/*     */ class BumpBuffer
/*     */ {
/*     */   static final int IMAGE_SIZE = 64;
/*     */   transient Image image;
/*     */   Color topColor;
/*     */   Color shadowColor;
/*     */   Color backColor;
/*     */   private GraphicsConfiguration gc;
/*     */ 
/*     */   public BumpBuffer(GraphicsConfiguration paramGraphicsConfiguration, Color paramColor1, Color paramColor2, Color paramColor3)
/*     */   {
/* 156 */     this.gc = paramGraphicsConfiguration;
/* 157 */     this.topColor = paramColor1;
/* 158 */     this.shadowColor = paramColor2;
/* 159 */     this.backColor = paramColor3;
/* 160 */     createImage();
/* 161 */     fillBumpBuffer();
/*     */   }
/*     */ 
/*     */   public boolean hasSameConfiguration(GraphicsConfiguration paramGraphicsConfiguration, Color paramColor1, Color paramColor2, Color paramColor3)
/*     */   {
/* 167 */     if (this.gc != null) {
/* 168 */       if (!this.gc.equals(paramGraphicsConfiguration)) {
/* 169 */         return false;
/*     */       }
/*     */     }
/* 172 */     else if (paramGraphicsConfiguration != null) {
/* 173 */       return false;
/*     */     }
/* 175 */     return (this.topColor.equals(paramColor1)) && (this.shadowColor.equals(paramColor2)) && (this.backColor.equals(paramColor3));
/*     */   }
/*     */ 
/*     */   public Image getImage()
/*     */   {
/* 185 */     return this.image;
/*     */   }
/*     */ 
/*     */   private void fillBumpBuffer()
/*     */   {
/* 192 */     Graphics localGraphics = this.image.getGraphics();
/*     */ 
/* 194 */     localGraphics.setColor(this.backColor);
/* 195 */     localGraphics.fillRect(0, 0, 64, 64);
/*     */ 
/* 197 */     localGraphics.setColor(this.topColor);
/*     */     int j;
/* 198 */     for (int i = 0; i < 64; i += 4) {
/* 199 */       for (j = 0; j < 64; j += 4) {
/* 200 */         localGraphics.drawLine(i, j, i, j);
/* 201 */         localGraphics.drawLine(i + 2, j + 2, i + 2, j + 2);
/*     */       }
/*     */     }
/*     */ 
/* 205 */     localGraphics.setColor(this.shadowColor);
/* 206 */     for (i = 0; i < 64; i += 4) {
/* 207 */       for (j = 0; j < 64; j += 4) {
/* 208 */         localGraphics.drawLine(i + 1, j + 1, i + 1, j + 1);
/* 209 */         localGraphics.drawLine(i + 3, j + 3, i + 3, j + 3);
/*     */       }
/*     */     }
/* 212 */     localGraphics.dispose();
/*     */   }
/*     */ 
/*     */   private void createImage()
/*     */   {
/* 220 */     if (this.gc != null) {
/* 221 */       this.image = this.gc.createCompatibleImage(64, 64, this.backColor != MetalBumps.ALPHA ? 1 : 2);
/*     */     }
/*     */     else
/*     */     {
/* 226 */       int[] arrayOfInt = { this.backColor.getRGB(), this.topColor.getRGB(), this.shadowColor.getRGB() };
/*     */ 
/* 228 */       IndexColorModel localIndexColorModel = new IndexColorModel(8, 3, arrayOfInt, 0, false, this.backColor == MetalBumps.ALPHA ? 0 : -1, 0);
/*     */ 
/* 231 */       this.image = new BufferedImage(64, 64, 13, localIndexColorModel);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.metal.BumpBuffer
 * JD-Core Version:    0.6.2
 */