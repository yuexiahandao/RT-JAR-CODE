/*     */ package java.awt.image;
/*     */ 
/*     */ import java.awt.Image;
/*     */ import java.util.Hashtable;
/*     */ 
/*     */ public class PixelGrabber
/*     */   implements ImageConsumer
/*     */ {
/*     */   ImageProducer producer;
/*     */   int dstX;
/*     */   int dstY;
/*     */   int dstW;
/*     */   int dstH;
/*     */   ColorModel imageModel;
/*     */   byte[] bytePixels;
/*     */   int[] intPixels;
/*     */   int dstOff;
/*     */   int dstScan;
/*     */   private boolean grabbing;
/*     */   private int flags;
/*     */   private static final int GRABBEDBITS = 48;
/*     */   private static final int DONEBITS = 112;
/*     */ 
/*     */   public PixelGrabber(Image paramImage, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int[] paramArrayOfInt, int paramInt5, int paramInt6)
/*     */   {
/* 120 */     this(paramImage.getSource(), paramInt1, paramInt2, paramInt3, paramInt4, paramArrayOfInt, paramInt5, paramInt6);
/*     */   }
/*     */ 
/*     */   public PixelGrabber(ImageProducer paramImageProducer, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int[] paramArrayOfInt, int paramInt5, int paramInt6)
/*     */   {
/* 149 */     this.producer = paramImageProducer;
/* 150 */     this.dstX = paramInt1;
/* 151 */     this.dstY = paramInt2;
/* 152 */     this.dstW = paramInt3;
/* 153 */     this.dstH = paramInt4;
/* 154 */     this.dstOff = paramInt5;
/* 155 */     this.dstScan = paramInt6;
/* 156 */     this.intPixels = paramArrayOfInt;
/* 157 */     this.imageModel = ColorModel.getRGBdefault();
/*     */   }
/*     */ 
/*     */   public PixelGrabber(Image paramImage, int paramInt1, int paramInt2, int paramInt3, int paramInt4, boolean paramBoolean)
/*     */   {
/* 185 */     this.producer = paramImage.getSource();
/* 186 */     this.dstX = paramInt1;
/* 187 */     this.dstY = paramInt2;
/* 188 */     this.dstW = paramInt3;
/* 189 */     this.dstH = paramInt4;
/* 190 */     if (paramBoolean)
/* 191 */       this.imageModel = ColorModel.getRGBdefault();
/*     */   }
/*     */ 
/*     */   public synchronized void startGrabbing()
/*     */   {
/* 199 */     if ((this.flags & 0x70) != 0) {
/* 200 */       return;
/*     */     }
/* 202 */     if (!this.grabbing) {
/* 203 */       this.grabbing = true;
/* 204 */       this.flags &= -129;
/* 205 */       this.producer.startProduction(this);
/*     */     }
/*     */   }
/*     */ 
/*     */   public synchronized void abortGrabbing()
/*     */   {
/* 213 */     imageComplete(4);
/*     */   }
/*     */ 
/*     */   public boolean grabPixels()
/*     */     throws InterruptedException
/*     */   {
/* 226 */     return grabPixels(0L);
/*     */   }
/*     */ 
/*     */   public synchronized boolean grabPixels(long paramLong)
/*     */     throws InterruptedException
/*     */   {
/* 252 */     if ((this.flags & 0x70) != 0) {
/* 253 */       return (this.flags & 0x30) != 0;
/*     */     }
/* 255 */     long l1 = paramLong + System.currentTimeMillis();
/* 256 */     if (!this.grabbing) {
/* 257 */       this.grabbing = true;
/* 258 */       this.flags &= -129;
/* 259 */       this.producer.startProduction(this);
/*     */     }
/* 261 */     while (this.grabbing)
/*     */     {
/*     */       long l2;
/* 263 */       if (paramLong == 0L) {
/* 264 */         l2 = 0L;
/*     */       } else {
/* 266 */         l2 = l1 - System.currentTimeMillis();
/* 267 */         if (l2 <= 0L) {
/*     */           break;
/*     */         }
/*     */       }
/* 271 */       wait(l2);
/*     */     }
/* 273 */     return (this.flags & 0x30) != 0;
/*     */   }
/*     */ 
/*     */   public synchronized int getStatus()
/*     */   {
/* 283 */     return this.flags;
/*     */   }
/*     */ 
/*     */   public synchronized int getWidth()
/*     */   {
/* 296 */     return this.dstW < 0 ? -1 : this.dstW;
/*     */   }
/*     */ 
/*     */   public synchronized int getHeight()
/*     */   {
/* 309 */     return this.dstH < 0 ? -1 : this.dstH;
/*     */   }
/*     */ 
/*     */   public synchronized Object getPixels()
/*     */   {
/* 328 */     return this.bytePixels == null ? this.intPixels : this.bytePixels;
/*     */   }
/*     */ 
/*     */   public synchronized ColorModel getColorModel()
/*     */   {
/* 351 */     return this.imageModel;
/*     */   }
/*     */ 
/*     */   public void setDimensions(int paramInt1, int paramInt2)
/*     */   {
/* 367 */     if (this.dstW < 0) {
/* 368 */       this.dstW = (paramInt1 - this.dstX);
/*     */     }
/* 370 */     if (this.dstH < 0) {
/* 371 */       this.dstH = (paramInt2 - this.dstY);
/*     */     }
/* 373 */     if ((this.dstW <= 0) || (this.dstH <= 0)) {
/* 374 */       imageComplete(3);
/* 375 */     } else if ((this.intPixels == null) && (this.imageModel == ColorModel.getRGBdefault()))
/*     */     {
/* 377 */       this.intPixels = new int[this.dstW * this.dstH];
/* 378 */       this.dstScan = this.dstW;
/* 379 */       this.dstOff = 0;
/*     */     }
/* 381 */     this.flags |= 3;
/*     */   }
/*     */ 
/*     */   public void setHints(int paramInt)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void setProperties(Hashtable<?, ?> paramHashtable)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void setColorModel(ColorModel paramColorModel)
/*     */   {
/*     */   }
/*     */ 
/*     */   private void convertToRGB()
/*     */   {
/* 431 */     int i = this.dstW * this.dstH;
/* 432 */     int[] arrayOfInt = new int[i];
/*     */     int j;
/* 433 */     if (this.bytePixels != null) {
/* 434 */       for (j = 0; j < i; j++)
/* 435 */         arrayOfInt[j] = this.imageModel.getRGB(this.bytePixels[j] & 0xFF);
/*     */     }
/* 437 */     else if (this.intPixels != null) {
/* 438 */       for (j = 0; j < i; j++) {
/* 439 */         arrayOfInt[j] = this.imageModel.getRGB(this.intPixels[j]);
/*     */       }
/*     */     }
/* 442 */     this.bytePixels = null;
/* 443 */     this.intPixels = arrayOfInt;
/* 444 */     this.dstScan = this.dstW;
/* 445 */     this.dstOff = 0;
/* 446 */     this.imageModel = ColorModel.getRGBdefault();
/*     */   }
/*     */ 
/*     */   public void setPixels(int paramInt1, int paramInt2, int paramInt3, int paramInt4, ColorModel paramColorModel, byte[] paramArrayOfByte, int paramInt5, int paramInt6)
/*     */   {
/* 474 */     if (paramInt2 < this.dstY) {
/* 475 */       i = this.dstY - paramInt2;
/* 476 */       if (i >= paramInt4) {
/* 477 */         return;
/*     */       }
/* 479 */       paramInt5 += paramInt6 * i;
/* 480 */       paramInt2 += i;
/* 481 */       paramInt4 -= i;
/*     */     }
/* 483 */     if (paramInt2 + paramInt4 > this.dstY + this.dstH) {
/* 484 */       paramInt4 = this.dstY + this.dstH - paramInt2;
/* 485 */       if (paramInt4 <= 0) {
/* 486 */         return;
/*     */       }
/*     */     }
/* 489 */     if (paramInt1 < this.dstX) {
/* 490 */       i = this.dstX - paramInt1;
/* 491 */       if (i >= paramInt3) {
/* 492 */         return;
/*     */       }
/* 494 */       paramInt5 += i;
/* 495 */       paramInt1 += i;
/* 496 */       paramInt3 -= i;
/*     */     }
/* 498 */     if (paramInt1 + paramInt3 > this.dstX + this.dstW) {
/* 499 */       paramInt3 = this.dstX + this.dstW - paramInt1;
/* 500 */       if (paramInt3 <= 0) {
/* 501 */         return;
/*     */       }
/*     */     }
/* 504 */     int i = this.dstOff + (paramInt2 - this.dstY) * this.dstScan + (paramInt1 - this.dstX);
/*     */     int j;
/* 505 */     if (this.intPixels == null) {
/* 506 */       if (this.bytePixels == null) {
/* 507 */         this.bytePixels = new byte[this.dstW * this.dstH];
/* 508 */         this.dstScan = this.dstW;
/* 509 */         this.dstOff = 0;
/* 510 */         this.imageModel = paramColorModel;
/* 511 */       } else if (this.imageModel != paramColorModel) {
/* 512 */         convertToRGB();
/*     */       }
/* 514 */       if (this.bytePixels != null) {
/* 515 */         for (j = paramInt4; j > 0; j--) {
/* 516 */           System.arraycopy(paramArrayOfByte, paramInt5, this.bytePixels, i, paramInt3);
/* 517 */           paramInt5 += paramInt6;
/* 518 */           i += this.dstScan;
/*     */         }
/*     */       }
/*     */     }
/* 522 */     if (this.intPixels != null) {
/* 523 */       j = this.dstScan - paramInt3;
/* 524 */       int k = paramInt6 - paramInt3;
/* 525 */       for (int m = paramInt4; m > 0; m--) {
/* 526 */         for (int n = paramInt3; n > 0; n--) {
/* 527 */           this.intPixels[(i++)] = paramColorModel.getRGB(paramArrayOfByte[(paramInt5++)] & 0xFF);
/*     */         }
/* 529 */         paramInt5 += k;
/* 530 */         i += j;
/*     */       }
/*     */     }
/* 533 */     this.flags |= 8;
/*     */   }
/*     */ 
/*     */   public void setPixels(int paramInt1, int paramInt2, int paramInt3, int paramInt4, ColorModel paramColorModel, int[] paramArrayOfInt, int paramInt5, int paramInt6)
/*     */   {
/* 561 */     if (paramInt2 < this.dstY) {
/* 562 */       i = this.dstY - paramInt2;
/* 563 */       if (i >= paramInt4) {
/* 564 */         return;
/*     */       }
/* 566 */       paramInt5 += paramInt6 * i;
/* 567 */       paramInt2 += i;
/* 568 */       paramInt4 -= i;
/*     */     }
/* 570 */     if (paramInt2 + paramInt4 > this.dstY + this.dstH) {
/* 571 */       paramInt4 = this.dstY + this.dstH - paramInt2;
/* 572 */       if (paramInt4 <= 0) {
/* 573 */         return;
/*     */       }
/*     */     }
/* 576 */     if (paramInt1 < this.dstX) {
/* 577 */       i = this.dstX - paramInt1;
/* 578 */       if (i >= paramInt3) {
/* 579 */         return;
/*     */       }
/* 581 */       paramInt5 += i;
/* 582 */       paramInt1 += i;
/* 583 */       paramInt3 -= i;
/*     */     }
/* 585 */     if (paramInt1 + paramInt3 > this.dstX + this.dstW) {
/* 586 */       paramInt3 = this.dstX + this.dstW - paramInt1;
/* 587 */       if (paramInt3 <= 0) {
/* 588 */         return;
/*     */       }
/*     */     }
/* 591 */     if (this.intPixels == null) {
/* 592 */       if (this.bytePixels == null) {
/* 593 */         this.intPixels = new int[this.dstW * this.dstH];
/* 594 */         this.dstScan = this.dstW;
/* 595 */         this.dstOff = 0;
/* 596 */         this.imageModel = paramColorModel;
/*     */       } else {
/* 598 */         convertToRGB();
/*     */       }
/*     */     }
/* 601 */     int i = this.dstOff + (paramInt2 - this.dstY) * this.dstScan + (paramInt1 - this.dstX);
/*     */     int j;
/* 602 */     if (this.imageModel == paramColorModel) {
/* 603 */       for (j = paramInt4; j > 0; j--) {
/* 604 */         System.arraycopy(paramArrayOfInt, paramInt5, this.intPixels, i, paramInt3);
/* 605 */         paramInt5 += paramInt6;
/* 606 */         i += this.dstScan;
/*     */       }
/*     */     } else {
/* 609 */       if (this.imageModel != ColorModel.getRGBdefault()) {
/* 610 */         convertToRGB();
/*     */       }
/* 612 */       j = this.dstScan - paramInt3;
/* 613 */       int k = paramInt6 - paramInt3;
/* 614 */       for (int m = paramInt4; m > 0; m--) {
/* 615 */         for (int n = paramInt3; n > 0; n--) {
/* 616 */           this.intPixels[(i++)] = paramColorModel.getRGB(paramArrayOfInt[(paramInt5++)]);
/*     */         }
/* 618 */         paramInt5 += k;
/* 619 */         i += j;
/*     */       }
/*     */     }
/* 622 */     this.flags |= 8;
/*     */   }
/*     */ 
/*     */   public synchronized void imageComplete(int paramInt)
/*     */   {
/* 637 */     this.grabbing = false;
/* 638 */     switch (paramInt) {
/*     */     case 1:
/*     */     default:
/* 641 */       this.flags |= 192;
/* 642 */       break;
/*     */     case 4:
/* 644 */       this.flags |= 128;
/* 645 */       break;
/*     */     case 3:
/* 647 */       this.flags |= 32;
/* 648 */       break;
/*     */     case 2:
/* 650 */       this.flags |= 16;
/*     */     }
/*     */ 
/* 653 */     this.producer.removeConsumer(this);
/* 654 */     notifyAll();
/*     */   }
/*     */ 
/*     */   public synchronized int status()
/*     */   {
/* 670 */     return this.flags;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.image.PixelGrabber
 * JD-Core Version:    0.6.2
 */