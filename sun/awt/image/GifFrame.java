/*     */ package sun.awt.image;
/*     */ 
/*     */ import java.awt.image.ColorModel;
/*     */ import java.awt.image.IndexColorModel;
/*     */ 
/*     */ class GifFrame
/*     */ {
/*     */   private static final boolean verbose = false;
/*     */   private static IndexColorModel trans_model;
/*     */   static final int DISPOSAL_NONE = 0;
/*     */   static final int DISPOSAL_SAVE = 1;
/*     */   static final int DISPOSAL_BGCOLOR = 2;
/*     */   static final int DISPOSAL_PREVIOUS = 3;
/*     */   GifImageDecoder decoder;
/*     */   int disposal_method;
/*     */   int delay;
/*     */   IndexColorModel model;
/*     */   int x;
/*     */   int y;
/*     */   int width;
/*     */   int height;
/*     */   boolean initialframe;
/*     */ 
/*     */   public GifFrame(GifImageDecoder paramGifImageDecoder, int paramInt1, int paramInt2, boolean paramBoolean, IndexColorModel paramIndexColorModel, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
/*     */   {
/* 645 */     this.decoder = paramGifImageDecoder;
/* 646 */     this.disposal_method = paramInt1;
/* 647 */     this.delay = paramInt2;
/* 648 */     this.model = paramIndexColorModel;
/* 649 */     this.initialframe = paramBoolean;
/* 650 */     this.x = paramInt3;
/* 651 */     this.y = paramInt4;
/* 652 */     this.width = paramInt5;
/* 653 */     this.height = paramInt6;
/*     */   }
/*     */ 
/*     */   private void setPixels(int paramInt1, int paramInt2, int paramInt3, int paramInt4, ColorModel paramColorModel, byte[] paramArrayOfByte, int paramInt5, int paramInt6)
/*     */   {
/* 658 */     this.decoder.setPixels(paramInt1, paramInt2, paramInt3, paramInt4, paramColorModel, paramArrayOfByte, paramInt5, paramInt6);
/*     */   }
/*     */ 
/*     */   public boolean dispose() {
/* 662 */     if (this.decoder.imageComplete(2, false) == 0) {
/* 663 */       return false;
/*     */     }
/* 665 */     if (this.delay > 0)
/*     */     {
/*     */       try
/*     */       {
/* 670 */         Thread.sleep(this.delay);
/*     */       } catch (InterruptedException localInterruptedException) {
/* 672 */         return false;
/*     */       }
/*     */     }
/* 675 */     else Thread.yield();
/*     */ 
/* 682 */     int i = this.decoder.global_width;
/* 683 */     int j = this.decoder.global_height;
/*     */ 
/* 685 */     if (this.x < 0) {
/* 686 */       this.width += this.x;
/* 687 */       this.x = 0;
/*     */     }
/* 689 */     if (this.x + this.width > i) {
/* 690 */       this.width = (i - this.x);
/*     */     }
/* 692 */     if (this.width <= 0) {
/* 693 */       this.disposal_method = 0;
/*     */     } else {
/* 695 */       if (this.y < 0) {
/* 696 */         this.height += this.y;
/* 697 */         this.y = 0;
/*     */       }
/* 699 */       if (this.y + this.height > j) {
/* 700 */         this.height = (j - this.y);
/*     */       }
/* 702 */       if (this.height <= 0) {
/* 703 */         this.disposal_method = 0;
/*     */       }
/*     */     }
/*     */ 
/* 707 */     switch (this.disposal_method) {
/*     */     case 3:
/* 709 */       byte[] arrayOfByte1 = this.decoder.saved_image;
/* 710 */       IndexColorModel localIndexColorModel = this.decoder.saved_model;
/* 711 */       if (arrayOfByte1 != null)
/* 712 */         setPixels(this.x, this.y, this.width, this.height, localIndexColorModel, arrayOfByte1, this.y * i + this.x, i); break;
/*     */     case 2:
/*     */       int k;
/* 719 */       if (this.model.getTransparentPixel() < 0) {
/* 720 */         this.model = trans_model;
/* 721 */         if (this.model == null) {
/* 722 */           this.model = new IndexColorModel(8, 1, new byte[4], 0, true);
/*     */ 
/* 724 */           trans_model = this.model;
/*     */         }
/* 726 */         k = 0;
/*     */       } else {
/* 728 */         k = (byte)this.model.getTransparentPixel();
/*     */       }
/* 730 */       byte[] arrayOfByte2 = new byte[this.width];
/*     */       int m;
/* 731 */       if (k != 0) {
/* 732 */         for (m = 0; m < this.width; m++) {
/* 733 */           arrayOfByte2[m] = k;
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 739 */       if (this.decoder.saved_image != null) {
/* 740 */         for (m = 0; m < i * j; m++) {
/* 741 */           this.decoder.saved_image[m] = k;
/*     */         }
/*     */       }
/* 744 */       setPixels(this.x, this.y, this.width, this.height, this.model, arrayOfByte2, 0, 0);
/* 745 */       break;
/*     */     case 1:
/* 747 */       this.decoder.saved_model = this.model;
/*     */     }
/*     */ 
/* 751 */     return true;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.image.GifFrame
 * JD-Core Version:    0.6.2
 */