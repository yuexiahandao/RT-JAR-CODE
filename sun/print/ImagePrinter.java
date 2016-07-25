/*    */ package sun.print;
/*    */ 
/*    */ import java.awt.Graphics;
/*    */ import java.awt.Graphics2D;
/*    */ import java.awt.image.BufferedImage;
/*    */ import java.awt.print.PageFormat;
/*    */ import java.awt.print.Printable;
/*    */ import java.io.InputStream;
/*    */ import java.net.URL;
/*    */ import javax.imageio.ImageIO;
/*    */ 
/*    */ class ImagePrinter
/*    */   implements Printable
/*    */ {
/*    */   BufferedImage image;
/*    */ 
/*    */   ImagePrinter(InputStream paramInputStream)
/*    */   {
/*    */     try
/*    */     {
/* 43 */       this.image = ImageIO.read(paramInputStream);
/*    */     } catch (Exception localException) {
/*    */     }
/*    */   }
/*    */ 
/*    */   ImagePrinter(URL paramURL) {
/*    */     try {
/* 50 */       this.image = ImageIO.read(paramURL);
/*    */     }
/*    */     catch (Exception localException) {
/*    */     }
/*    */   }
/*    */ 
/*    */   public int print(Graphics paramGraphics, PageFormat paramPageFormat, int paramInt) {
/* 57 */     if ((paramInt > 0) || (this.image == null)) {
/* 58 */       return 1;
/*    */     }
/*    */ 
/* 61 */     ((Graphics2D)paramGraphics).translate(paramPageFormat.getImageableX(), paramPageFormat.getImageableY());
/* 62 */     int i = this.image.getWidth(null);
/* 63 */     int j = this.image.getHeight(null);
/* 64 */     int k = (int)paramPageFormat.getImageableWidth();
/* 65 */     int m = (int)paramPageFormat.getImageableHeight();
/*    */ 
/* 68 */     int n = i;
/* 69 */     int i1 = j;
/* 70 */     if (n > k) {
/* 71 */       i1 = (int)(i1 * (k / n));
/* 72 */       n = k;
/*    */     }
/* 74 */     if (i1 > m) {
/* 75 */       n = (int)(n * (m / i1));
/* 76 */       i1 = m;
/*    */     }
/*    */ 
/* 79 */     int i2 = (k - n) / 2;
/* 80 */     int i3 = (m - i1) / 2;
/*    */ 
/* 82 */     paramGraphics.drawImage(this.image, i2, i3, i2 + n, i3 + i1, 0, 0, i, j, null);
/* 83 */     return 0;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.print.ImagePrinter
 * JD-Core Version:    0.6.2
 */