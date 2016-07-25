/*     */ package sun.awt;
/*     */ 
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Image;
/*     */ import java.awt.color.ColorSpace;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.awt.image.DataBufferInt;
/*     */ import java.awt.image.DirectColorModel;
/*     */ import java.awt.image.Raster;
/*     */ import java.awt.image.WritableRaster;
/*     */ import java.util.Arrays;
/*     */ import sun.awt.image.ImageRepresentation;
/*     */ import sun.awt.image.ToolkitImage;
/*     */ 
/*     */ public class IconInfo
/*     */ {
/*     */   private int[] intIconData;
/*     */   private long[] longIconData;
/*     */   private Image image;
/*     */   private final int width;
/*     */   private final int height;
/*     */   private int scaledWidth;
/*     */   private int scaledHeight;
/*     */   private int rawLength;
/*     */ 
/*     */   public IconInfo(int[] paramArrayOfInt)
/*     */   {
/*  72 */     this.intIconData = (null == paramArrayOfInt ? null : Arrays.copyOf(paramArrayOfInt, paramArrayOfInt.length));
/*     */ 
/*  74 */     this.width = paramArrayOfInt[0];
/*  75 */     this.height = paramArrayOfInt[1];
/*  76 */     this.scaledWidth = this.width;
/*  77 */     this.scaledHeight = this.height;
/*  78 */     this.rawLength = (this.width * this.height + 2);
/*     */   }
/*     */ 
/*     */   public IconInfo(long[] paramArrayOfLong) {
/*  82 */     this.longIconData = (null == paramArrayOfLong ? null : Arrays.copyOf(paramArrayOfLong, paramArrayOfLong.length));
/*     */ 
/*  84 */     this.width = ((int)paramArrayOfLong[0]);
/*  85 */     this.height = ((int)paramArrayOfLong[1]);
/*  86 */     this.scaledWidth = this.width;
/*  87 */     this.scaledHeight = this.height;
/*  88 */     this.rawLength = (this.width * this.height + 2);
/*     */   }
/*     */ 
/*     */   public IconInfo(Image paramImage) {
/*  92 */     this.image = paramImage;
/*  93 */     if ((paramImage instanceof ToolkitImage)) {
/*  94 */       ImageRepresentation localImageRepresentation = ((ToolkitImage)paramImage).getImageRep();
/*  95 */       localImageRepresentation.reconstruct(32);
/*  96 */       this.width = localImageRepresentation.getWidth();
/*  97 */       this.height = localImageRepresentation.getHeight();
/*     */     } else {
/*  99 */       this.width = paramImage.getWidth(null);
/* 100 */       this.height = paramImage.getHeight(null);
/*     */     }
/* 102 */     this.scaledWidth = this.width;
/* 103 */     this.scaledHeight = this.height;
/* 104 */     this.rawLength = (this.width * this.height + 2);
/*     */   }
/*     */ 
/*     */   public void setScaledSize(int paramInt1, int paramInt2)
/*     */   {
/* 111 */     this.scaledWidth = paramInt1;
/* 112 */     this.scaledHeight = paramInt2;
/* 113 */     this.rawLength = (paramInt1 * paramInt2 + 2);
/*     */   }
/*     */ 
/*     */   public boolean isValid() {
/* 117 */     return (this.width > 0) && (this.height > 0);
/*     */   }
/*     */ 
/*     */   public int getWidth() {
/* 121 */     return this.width;
/*     */   }
/*     */ 
/*     */   public int getHeight() {
/* 125 */     return this.height;
/*     */   }
/*     */ 
/*     */   public String toString() {
/* 129 */     return "IconInfo[w=" + this.width + ",h=" + this.height + ",sw=" + this.scaledWidth + ",sh=" + this.scaledHeight + "]";
/*     */   }
/*     */ 
/*     */   public int getRawLength() {
/* 133 */     return this.rawLength;
/*     */   }
/*     */ 
/*     */   public int[] getIntData() {
/* 137 */     if (this.intIconData == null) {
/* 138 */       if (this.longIconData != null)
/* 139 */         this.intIconData = longArrayToIntArray(this.longIconData);
/* 140 */       else if (this.image != null) {
/* 141 */         this.intIconData = imageToIntArray(this.image, this.scaledWidth, this.scaledHeight);
/*     */       }
/*     */     }
/* 144 */     return this.intIconData;
/*     */   }
/*     */ 
/*     */   public long[] getLongData() {
/* 148 */     if (this.longIconData == null) {
/* 149 */       if (this.intIconData != null) {
/* 150 */         this.longIconData = intArrayToLongArray(this.intIconData);
/* 151 */       } else if (this.image != null) {
/* 152 */         int[] arrayOfInt = imageToIntArray(this.image, this.scaledWidth, this.scaledHeight);
/* 153 */         this.longIconData = intArrayToLongArray(arrayOfInt);
/*     */       }
/*     */     }
/* 156 */     return this.longIconData;
/*     */   }
/*     */ 
/*     */   public Image getImage() {
/* 160 */     if (this.image == null) {
/* 161 */       if (this.intIconData != null) {
/* 162 */         this.image = intArrayToImage(this.intIconData);
/* 163 */       } else if (this.longIconData != null) {
/* 164 */         int[] arrayOfInt = longArrayToIntArray(this.longIconData);
/* 165 */         this.image = intArrayToImage(arrayOfInt);
/*     */       }
/*     */     }
/* 168 */     return this.image;
/*     */   }
/*     */ 
/*     */   private static int[] longArrayToIntArray(long[] paramArrayOfLong) {
/* 172 */     int[] arrayOfInt = new int[paramArrayOfLong.length];
/* 173 */     for (int i = 0; i < paramArrayOfLong.length; i++)
/*     */     {
/* 177 */       arrayOfInt[i] = ((int)paramArrayOfLong[i]);
/*     */     }
/* 179 */     return arrayOfInt;
/*     */   }
/*     */ 
/*     */   private static long[] intArrayToLongArray(int[] paramArrayOfInt) {
/* 183 */     long[] arrayOfLong = new long[paramArrayOfInt.length];
/* 184 */     for (int i = 0; i < paramArrayOfInt.length; i++) {
/* 185 */       arrayOfLong[i] = paramArrayOfInt[i];
/*     */     }
/* 187 */     return arrayOfLong;
/*     */   }
/*     */ 
/*     */   static Image intArrayToImage(int[] paramArrayOfInt) {
/* 191 */     DirectColorModel localDirectColorModel = new DirectColorModel(ColorSpace.getInstance(1000), 32, 16711680, 65280, 255, -16777216, false, 3);
/*     */ 
/* 195 */     DataBufferInt localDataBufferInt = new DataBufferInt(paramArrayOfInt, paramArrayOfInt.length - 2, 2);
/* 196 */     WritableRaster localWritableRaster = Raster.createPackedRaster(localDataBufferInt, paramArrayOfInt[0], paramArrayOfInt[1], paramArrayOfInt[0], new int[] { 16711680, 65280, 255, -16777216 }, null);
/*     */ 
/* 202 */     BufferedImage localBufferedImage = new BufferedImage(localDirectColorModel, localWritableRaster, false, null);
/* 203 */     return localBufferedImage;
/*     */   }
/*     */ 
/*     */   static int[] imageToIntArray(Image paramImage, int paramInt1, int paramInt2)
/*     */   {
/* 211 */     if ((paramInt1 <= 0) || (paramInt2 <= 0)) {
/* 212 */       return null;
/*     */     }
/* 214 */     DirectColorModel localDirectColorModel = new DirectColorModel(ColorSpace.getInstance(1000), 32, 16711680, 65280, 255, -16777216, false, 3);
/*     */ 
/* 218 */     DataBufferInt localDataBufferInt = new DataBufferInt(paramInt1 * paramInt2);
/* 219 */     WritableRaster localWritableRaster = Raster.createPackedRaster(localDataBufferInt, paramInt1, paramInt2, paramInt1, new int[] { 16711680, 65280, 255, -16777216 }, null);
/*     */ 
/* 225 */     BufferedImage localBufferedImage = new BufferedImage(localDirectColorModel, localWritableRaster, false, null);
/* 226 */     Graphics localGraphics = localBufferedImage.getGraphics();
/* 227 */     localGraphics.drawImage(paramImage, 0, 0, paramInt1, paramInt2, null);
/* 228 */     localGraphics.dispose();
/* 229 */     int[] arrayOfInt1 = localDataBufferInt.getData();
/* 230 */     int[] arrayOfInt2 = new int[paramInt1 * paramInt2 + 2];
/* 231 */     arrayOfInt2[0] = paramInt1;
/* 232 */     arrayOfInt2[1] = paramInt2;
/* 233 */     System.arraycopy(arrayOfInt1, 0, arrayOfInt2, 2, paramInt1 * paramInt2);
/* 234 */     return arrayOfInt2;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.IconInfo
 * JD-Core Version:    0.6.2
 */