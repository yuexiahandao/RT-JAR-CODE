/*      */ package com.sun.imageio.plugins.jpeg;
/*      */ 
/*      */ import javax.imageio.ImageTypeSpecifier;
/*      */ 
/*      */ class ImageTypeProducer
/*      */ {
/* 1744 */   private ImageTypeSpecifier type = null;
/* 1745 */   boolean failed = false;
/*      */   private int csCode;
/* 1767 */   private static final ImageTypeProducer[] defaultTypes = new ImageTypeProducer[12];
/*      */ 
/*      */   public ImageTypeProducer(int paramInt)
/*      */   {
/* 1749 */     this.csCode = paramInt;
/*      */   }
/*      */ 
/*      */   public ImageTypeProducer() {
/* 1753 */     this.csCode = -1;
/*      */   }
/*      */ 
/*      */   public synchronized ImageTypeSpecifier getType() {
/* 1757 */     if ((!this.failed) && (this.type == null)) {
/*      */       try {
/* 1759 */         this.type = produce();
/*      */       } catch (Throwable localThrowable) {
/* 1761 */         this.failed = true;
/*      */       }
/*      */     }
/* 1764 */     return this.type;
/*      */   }
/*      */ 
/*      */   public static synchronized ImageTypeProducer getTypeProducer(int paramInt)
/*      */   {
/* 1771 */     if ((paramInt < 0) || (paramInt >= 12)) {
/* 1772 */       return null;
/*      */     }
/* 1774 */     if (defaultTypes[paramInt] == null) {
/* 1775 */       defaultTypes[paramInt] = new ImageTypeProducer(paramInt);
/*      */     }
/* 1777 */     return defaultTypes[paramInt];
/*      */   }
/*      */ 
/*      */   protected ImageTypeSpecifier produce() {
/* 1781 */     switch (this.csCode) {
/*      */     case 1:
/* 1783 */       return ImageTypeSpecifier.createFromBufferedImageType(10);
/*      */     case 2:
/* 1786 */       return ImageTypeSpecifier.createInterleaved(JPEG.JCS.sRGB, JPEG.bOffsRGB, 0, false, false);
/*      */     case 6:
/* 1792 */       return ImageTypeSpecifier.createPacked(JPEG.JCS.sRGB, -16777216, 16711680, 65280, 255, 3, false);
/*      */     case 5:
/* 1800 */       if (JPEG.JCS.getYCC() != null) {
/* 1801 */         return ImageTypeSpecifier.createInterleaved(JPEG.JCS.getYCC(), JPEG.bandOffsets[2], 0, false, false);
/*      */       }
/*      */ 
/* 1808 */       return null;
/*      */     case 10:
/* 1811 */       if (JPEG.JCS.getYCC() != null) {
/* 1812 */         return ImageTypeSpecifier.createInterleaved(JPEG.JCS.getYCC(), JPEG.bandOffsets[3], 0, true, false);
/*      */       }
/*      */ 
/* 1819 */       return null;
/*      */     case 3:
/*      */     case 4:
/*      */     case 7:
/*      */     case 8:
/* 1822 */     case 9: } return null;
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.imageio.plugins.jpeg.ImageTypeProducer
 * JD-Core Version:    0.6.2
 */