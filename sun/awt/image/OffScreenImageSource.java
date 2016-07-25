/*     */ package sun.awt.image;
/*     */ 
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.awt.image.ColorModel;
/*     */ import java.awt.image.DataBuffer;
/*     */ import java.awt.image.DirectColorModel;
/*     */ import java.awt.image.ImageConsumer;
/*     */ import java.awt.image.ImageProducer;
/*     */ import java.awt.image.IndexColorModel;
/*     */ import java.awt.image.WritableRaster;
/*     */ import java.util.Hashtable;
/*     */ 
/*     */ public class OffScreenImageSource
/*     */   implements ImageProducer
/*     */ {
/*     */   BufferedImage image;
/*     */   int width;
/*     */   int height;
/*     */   Hashtable properties;
/*     */   private ImageConsumer theConsumer;
/*     */ 
/*     */   public OffScreenImageSource(BufferedImage paramBufferedImage, Hashtable paramHashtable)
/*     */   {
/*  47 */     this.image = paramBufferedImage;
/*  48 */     if (paramHashtable != null)
/*  49 */       this.properties = paramHashtable;
/*     */     else {
/*  51 */       this.properties = new Hashtable();
/*     */     }
/*  53 */     this.width = paramBufferedImage.getWidth();
/*  54 */     this.height = paramBufferedImage.getHeight();
/*     */   }
/*     */ 
/*     */   public OffScreenImageSource(BufferedImage paramBufferedImage) {
/*  58 */     this(paramBufferedImage, null);
/*     */   }
/*     */ 
/*     */   public synchronized void addConsumer(ImageConsumer paramImageConsumer)
/*     */   {
/*  65 */     this.theConsumer = paramImageConsumer;
/*  66 */     produce();
/*     */   }
/*     */ 
/*     */   public synchronized boolean isConsumer(ImageConsumer paramImageConsumer) {
/*  70 */     return paramImageConsumer == this.theConsumer;
/*     */   }
/*     */ 
/*     */   public synchronized void removeConsumer(ImageConsumer paramImageConsumer) {
/*  74 */     if (this.theConsumer == paramImageConsumer)
/*  75 */       this.theConsumer = null;
/*     */   }
/*     */ 
/*     */   public void startProduction(ImageConsumer paramImageConsumer)
/*     */   {
/*  80 */     addConsumer(paramImageConsumer);
/*     */   }
/*     */ 
/*     */   public void requestTopDownLeftRightResend(ImageConsumer paramImageConsumer) {
/*     */   }
/*     */ 
/*     */   private void sendPixels() {
/*  87 */     ColorModel localColorModel = this.image.getColorModel();
/*  88 */     WritableRaster localWritableRaster = this.image.getRaster();
/*  89 */     int i = localWritableRaster.getNumDataElements();
/*  90 */     int j = localWritableRaster.getDataBuffer().getDataType();
/*  91 */     int[] arrayOfInt = new int[this.width * i];
/*  92 */     int k = 1;
/*     */     int n;
/*     */     int i2;
/*     */     Object localObject;
/*  94 */     if ((localColorModel instanceof IndexColorModel)) {
/*  95 */       byte[] arrayOfByte = new byte[this.width];
/*  96 */       this.theConsumer.setColorModel(localColorModel);
/*     */ 
/*  98 */       if ((localWritableRaster instanceof ByteComponentRaster)) {
/*  99 */         k = 0;
/* 100 */         for (n = 0; n < this.height; n++) {
/* 101 */           localWritableRaster.getDataElements(0, n, this.width, 1, arrayOfByte);
/* 102 */           this.theConsumer.setPixels(0, n, this.width, 1, localColorModel, arrayOfByte, 0, this.width);
/*     */         }
/*     */ 
/*     */       }
/* 106 */       else if ((localWritableRaster instanceof BytePackedRaster)) {
/* 107 */         k = 0;
/*     */ 
/* 109 */         for (n = 0; n < this.height; n++) {
/* 110 */           localWritableRaster.getPixels(0, n, this.width, 1, arrayOfInt);
/* 111 */           for (i2 = 0; i2 < this.width; i2++) {
/* 112 */             arrayOfByte[i2] = ((byte)arrayOfInt[i2]);
/*     */           }
/* 114 */           this.theConsumer.setPixels(0, n, this.width, 1, localColorModel, arrayOfByte, 0, this.width);
/*     */         }
/*     */ 
/*     */       }
/* 118 */       else if ((j == 2) || (j == 3))
/*     */       {
/* 122 */         k = 0;
/* 123 */         for (n = 0; n < this.height; n++) {
/* 124 */           localWritableRaster.getPixels(0, n, this.width, 1, arrayOfInt);
/* 125 */           this.theConsumer.setPixels(0, n, this.width, 1, localColorModel, arrayOfInt, 0, this.width);
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/* 130 */     else if ((localColorModel instanceof DirectColorModel)) {
/* 131 */       this.theConsumer.setColorModel(localColorModel);
/* 132 */       k = 0;
/* 133 */       switch (j) {
/*     */       case 3:
/* 135 */         for (int m = 0; m < this.height; m++) {
/* 136 */           localWritableRaster.getDataElements(0, m, this.width, 1, arrayOfInt);
/* 137 */           this.theConsumer.setPixels(0, m, this.width, 1, localColorModel, arrayOfInt, 0, this.width);
/*     */         }
/*     */ 
/* 140 */         break;
/*     */       case 0:
/* 142 */         localObject = new byte[this.width];
/* 143 */         for (n = 0; n < this.height; n++) {
/* 144 */           localWritableRaster.getDataElements(0, n, this.width, 1, localObject);
/* 145 */           for (i2 = 0; i2 < this.width; i2++) {
/* 146 */             localObject[i2] &= 255;
/*     */           }
/* 148 */           this.theConsumer.setPixels(0, n, this.width, 1, localColorModel, arrayOfInt, 0, this.width);
/*     */         }
/*     */ 
/* 151 */         break;
/*     */       case 1:
/* 153 */         short[] arrayOfShort = new short[this.width];
/* 154 */         for (i2 = 0; i2 < this.height; i2++) {
/* 155 */           localWritableRaster.getDataElements(0, i2, this.width, 1, arrayOfShort);
/* 156 */           for (int i3 = 0; i3 < this.width; i3++) {
/* 157 */             arrayOfShort[i3] &= 65535;
/*     */           }
/* 159 */           this.theConsumer.setPixels(0, i2, this.width, 1, localColorModel, arrayOfInt, 0, this.width);
/*     */         }
/*     */ 
/* 162 */         break;
/*     */       case 2:
/*     */       default:
/* 164 */         k = 1;
/*     */       }
/*     */     }
/*     */ 
/* 168 */     if (k != 0)
/*     */     {
/* 170 */       localObject = ColorModel.getRGBdefault();
/* 171 */       this.theConsumer.setColorModel((ColorModel)localObject);
/*     */ 
/* 173 */       for (int i1 = 0; i1 < this.height; i1++) {
/* 174 */         for (i2 = 0; i2 < this.width; i2++) {
/* 175 */           arrayOfInt[i2] = this.image.getRGB(i2, i1);
/*     */         }
/* 177 */         this.theConsumer.setPixels(0, i1, this.width, 1, (ColorModel)localObject, arrayOfInt, 0, this.width);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private void produce()
/*     */   {
/*     */     try {
/* 185 */       this.theConsumer.setDimensions(this.image.getWidth(), this.image.getHeight());
/* 186 */       this.theConsumer.setProperties(this.properties);
/* 187 */       sendPixels();
/* 188 */       this.theConsumer.imageComplete(2);
/*     */     } catch (NullPointerException localNullPointerException) {
/* 190 */       if (this.theConsumer != null)
/* 191 */         this.theConsumer.imageComplete(1);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.image.OffScreenImageSource
 * JD-Core Version:    0.6.2
 */