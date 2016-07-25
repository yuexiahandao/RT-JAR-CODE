/*    */ package javax.swing;
/*    */ 
/*    */ import java.awt.Image;
/*    */ import java.awt.Toolkit;
/*    */ import java.awt.image.FilteredImageSource;
/*    */ import java.awt.image.RGBImageFilter;
/*    */ 
/*    */ public class GrayFilter extends RGBImageFilter
/*    */ {
/*    */   private boolean brighter;
/*    */   private int percent;
/*    */ 
/*    */   public static Image createDisabledImage(Image paramImage)
/*    */   {
/* 48 */     GrayFilter localGrayFilter = new GrayFilter(true, 50);
/* 49 */     FilteredImageSource localFilteredImageSource = new FilteredImageSource(paramImage.getSource(), localGrayFilter);
/* 50 */     Image localImage = Toolkit.getDefaultToolkit().createImage(localFilteredImageSource);
/* 51 */     return localImage;
/*    */   }
/*    */ 
/*    */   public GrayFilter(boolean paramBoolean, int paramInt)
/*    */   {
/* 64 */     this.brighter = paramBoolean;
/* 65 */     this.percent = paramInt;
/*    */ 
/* 71 */     this.canFilterIndexColorModel = true;
/*    */   }
/*    */ 
/*    */   public int filterRGB(int paramInt1, int paramInt2, int paramInt3)
/*    */   {
/* 79 */     int i = (int)((0.3D * (paramInt3 >> 16 & 0xFF) + 0.59D * (paramInt3 >> 8 & 0xFF) + 0.11D * (paramInt3 & 0xFF)) / 3.0D);
/*    */ 
/* 83 */     if (this.brighter)
/* 84 */       i = 255 - (255 - i) * (100 - this.percent) / 100;
/*    */     else {
/* 86 */       i = i * (100 - this.percent) / 100;
/*    */     }
/*    */ 
/* 89 */     if (i < 0) i = 0;
/* 90 */     if (i > 255) i = 255;
/* 91 */     return paramInt3 & 0xFF000000 | i << 16 | i << 8 | i << 0;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.GrayFilter
 * JD-Core Version:    0.6.2
 */