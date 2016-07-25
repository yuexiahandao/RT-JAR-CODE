/*     */ package sun.java2d.cmm.lcms;
/*     */ 
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.awt.image.SampleModel;
/*     */ import sun.awt.image.ByteComponentRaster;
/*     */ import sun.awt.image.IntegerComponentRaster;
/*     */ import sun.awt.image.ShortComponentRaster;
/*     */ 
/*     */ class LCMSImageLayout
/*     */ {
/*     */   public static final int SWAPFIRST = 16384;
/*     */   public static final int DOSWAP = 1024;
/*  63 */   public static final int PT_RGB_8 = CHANNELS_SH(3) | BYTES_SH(1);
/*     */ 
/*  66 */   public static final int PT_GRAY_8 = CHANNELS_SH(1) | BYTES_SH(1);
/*     */ 
/*  69 */   public static final int PT_GRAY_16 = CHANNELS_SH(1) | BYTES_SH(2);
/*     */ 
/*  72 */   public static final int PT_RGBA_8 = EXTRA_SH(1) | CHANNELS_SH(3) | BYTES_SH(1);
/*     */ 
/*  75 */   public static final int PT_ARGB_8 = EXTRA_SH(1) | CHANNELS_SH(3) | BYTES_SH(1) | 0x4000;
/*     */ 
/*  78 */   public static final int PT_BGR_8 = 0x400 | CHANNELS_SH(3) | BYTES_SH(1);
/*     */ 
/*  81 */   public static final int PT_ABGR_8 = 0x400 | EXTRA_SH(1) | CHANNELS_SH(3) | BYTES_SH(1);
/*     */ 
/*  84 */   public static final int PT_BGRA_8 = EXTRA_SH(1) | CHANNELS_SH(3) | BYTES_SH(1) | 0x400 | 0x4000;
/*     */   public static final int DT_BYTE = 0;
/*     */   public static final int DT_SHORT = 1;
/*     */   public static final int DT_INT = 2;
/*     */   public static final int DT_DOUBLE = 3;
/*  93 */   boolean isIntPacked = false;
/*     */   int pixelType;
/*     */   int dataType;
/*     */   int width;
/*     */   int height;
/*     */   int nextRowOffset;
/*     */   private int nextPixelOffset;
/*     */   int offset;
/*     */   Object dataArray;
/*     */   private int dataArrayLength;
/*     */ 
/*     */   public static int BYTES_SH(int paramInt)
/*     */   {
/*  48 */     return paramInt;
/*     */   }
/*     */ 
/*     */   public static int EXTRA_SH(int paramInt) {
/*  52 */     return paramInt << 7;
/*     */   }
/*     */ 
/*     */   public static int CHANNELS_SH(int paramInt) {
/*  56 */     return paramInt << 3;
/*     */   }
/*     */ 
/*     */   private LCMSImageLayout(int paramInt1, int paramInt2, int paramInt3)
/*     */     throws LCMSImageLayout.ImageLayoutException
/*     */   {
/* 108 */     this.pixelType = paramInt2;
/* 109 */     this.width = paramInt1;
/* 110 */     this.height = 1;
/* 111 */     this.nextPixelOffset = paramInt3;
/* 112 */     this.nextRowOffset = safeMult(paramInt3, paramInt1);
/* 113 */     this.offset = 0;
/*     */   }
/*     */ 
/*     */   private LCMSImageLayout(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */     throws LCMSImageLayout.ImageLayoutException
/*     */   {
/* 120 */     this.pixelType = paramInt3;
/* 121 */     this.width = paramInt1;
/* 122 */     this.height = paramInt2;
/* 123 */     this.nextPixelOffset = paramInt4;
/* 124 */     this.nextRowOffset = safeMult(paramInt4, paramInt1);
/* 125 */     this.offset = 0;
/*     */   }
/*     */ 
/*     */   public LCMSImageLayout(byte[] paramArrayOfByte, int paramInt1, int paramInt2, int paramInt3)
/*     */     throws LCMSImageLayout.ImageLayoutException
/*     */   {
/* 132 */     this(paramInt1, paramInt2, paramInt3);
/* 133 */     this.dataType = 0;
/* 134 */     this.dataArray = paramArrayOfByte;
/* 135 */     this.dataArrayLength = paramArrayOfByte.length;
/*     */ 
/* 137 */     verify();
/*     */   }
/*     */ 
/*     */   public LCMSImageLayout(short[] paramArrayOfShort, int paramInt1, int paramInt2, int paramInt3)
/*     */     throws LCMSImageLayout.ImageLayoutException
/*     */   {
/* 143 */     this(paramInt1, paramInt2, paramInt3);
/* 144 */     this.dataType = 1;
/* 145 */     this.dataArray = paramArrayOfShort;
/* 146 */     this.dataArrayLength = (2 * paramArrayOfShort.length);
/*     */ 
/* 148 */     verify();
/*     */   }
/*     */ 
/*     */   public LCMSImageLayout(int[] paramArrayOfInt, int paramInt1, int paramInt2, int paramInt3)
/*     */     throws LCMSImageLayout.ImageLayoutException
/*     */   {
/* 154 */     this(paramInt1, paramInt2, paramInt3);
/* 155 */     this.dataType = 2;
/* 156 */     this.dataArray = paramArrayOfInt;
/* 157 */     this.dataArrayLength = (4 * paramArrayOfInt.length);
/*     */ 
/* 159 */     verify();
/*     */   }
/*     */ 
/*     */   public LCMSImageLayout(double[] paramArrayOfDouble, int paramInt1, int paramInt2, int paramInt3)
/*     */     throws LCMSImageLayout.ImageLayoutException
/*     */   {
/* 165 */     this(paramInt1, paramInt2, paramInt3);
/* 166 */     this.dataType = 3;
/* 167 */     this.dataArray = paramArrayOfDouble;
/* 168 */     this.dataArrayLength = (8 * paramArrayOfDouble.length);
/*     */ 
/* 170 */     verify();
/*     */   }
/*     */ 
/*     */   public LCMSImageLayout(BufferedImage paramBufferedImage)
/*     */     throws LCMSImageLayout.ImageLayoutException
/*     */   {
/* 177 */     switch (paramBufferedImage.getType()) {
/*     */     case 1:
/* 179 */       this.pixelType = PT_ARGB_8;
/* 180 */       this.isIntPacked = true;
/* 181 */       break;
/*     */     case 2:
/* 183 */       this.pixelType = PT_ARGB_8;
/* 184 */       this.isIntPacked = true;
/* 185 */       break;
/*     */     case 4:
/* 187 */       this.pixelType = PT_ABGR_8;
/* 188 */       this.isIntPacked = true;
/* 189 */       break;
/*     */     case 5:
/* 191 */       this.pixelType = PT_BGR_8;
/* 192 */       break;
/*     */     case 6:
/* 194 */       this.pixelType = PT_ABGR_8;
/* 195 */       break;
/*     */     case 10:
/* 197 */       this.pixelType = PT_GRAY_8;
/* 198 */       break;
/*     */     case 11:
/* 200 */       this.pixelType = PT_GRAY_16;
/* 201 */       break;
/*     */     case 3:
/*     */     case 7:
/*     */     case 8:
/*     */     case 9:
/*     */     default:
/* 205 */       throw new IllegalArgumentException("CMMImageLayout - bad image type passed to constructor");
/*     */     }
/*     */ 
/* 209 */     this.width = paramBufferedImage.getWidth();
/* 210 */     this.height = paramBufferedImage.getHeight();
/*     */     ByteComponentRaster localByteComponentRaster;
/* 212 */     switch (paramBufferedImage.getType()) {
/*     */     case 1:
/*     */     case 2:
/*     */     case 4:
/* 216 */       IntegerComponentRaster localIntegerComponentRaster = (IntegerComponentRaster)paramBufferedImage.getRaster();
/*     */ 
/* 218 */       this.nextRowOffset = safeMult(4, localIntegerComponentRaster.getScanlineStride());
/* 219 */       this.nextPixelOffset = safeMult(4, localIntegerComponentRaster.getPixelStride());
/*     */ 
/* 221 */       this.offset = safeMult(4, localIntegerComponentRaster.getDataOffset(0));
/*     */ 
/* 223 */       this.dataArray = localIntegerComponentRaster.getDataStorage();
/* 224 */       this.dataArrayLength = (4 * localIntegerComponentRaster.getDataStorage().length);
/* 225 */       this.dataType = 2;
/* 226 */       break;
/*     */     case 5:
/*     */     case 6:
/* 230 */       localByteComponentRaster = (ByteComponentRaster)paramBufferedImage.getRaster();
/* 231 */       this.nextRowOffset = localByteComponentRaster.getScanlineStride();
/* 232 */       this.nextPixelOffset = localByteComponentRaster.getPixelStride();
/*     */ 
/* 234 */       int i = paramBufferedImage.getSampleModel().getNumBands() - 1;
/* 235 */       this.offset = localByteComponentRaster.getDataOffset(i);
/* 236 */       this.dataArray = localByteComponentRaster.getDataStorage();
/* 237 */       this.dataArrayLength = localByteComponentRaster.getDataStorage().length;
/* 238 */       this.dataType = 0;
/* 239 */       break;
/*     */     case 10:
/* 242 */       localByteComponentRaster = (ByteComponentRaster)paramBufferedImage.getRaster();
/* 243 */       this.nextRowOffset = localByteComponentRaster.getScanlineStride();
/* 244 */       this.nextPixelOffset = localByteComponentRaster.getPixelStride();
/*     */ 
/* 246 */       this.offset = localByteComponentRaster.getDataOffset(0);
/* 247 */       this.dataArray = localByteComponentRaster.getDataStorage();
/* 248 */       this.dataArrayLength = localByteComponentRaster.getDataStorage().length;
/* 249 */       this.dataType = 0;
/* 250 */       break;
/*     */     case 11:
/* 253 */       ShortComponentRaster localShortComponentRaster = (ShortComponentRaster)paramBufferedImage.getRaster();
/* 254 */       this.nextRowOffset = safeMult(2, localShortComponentRaster.getScanlineStride());
/* 255 */       this.nextPixelOffset = safeMult(2, localShortComponentRaster.getPixelStride());
/*     */ 
/* 257 */       this.offset = safeMult(2, localShortComponentRaster.getDataOffset(0));
/* 258 */       this.dataArray = localShortComponentRaster.getDataStorage();
/* 259 */       this.dataArrayLength = (2 * localShortComponentRaster.getDataStorage().length);
/* 260 */       this.dataType = 1;
/*     */     case 3:
/*     */     case 7:
/*     */     case 8:
/* 263 */     case 9: } verify();
/*     */   }
/*     */ 
/*     */   public static boolean isSupported(BufferedImage paramBufferedImage) {
/* 267 */     switch (paramBufferedImage.getType()) {
/*     */     case 1:
/*     */     case 2:
/*     */     case 4:
/*     */     case 5:
/*     */     case 6:
/*     */     case 10:
/*     */     case 11:
/* 275 */       return true;
/*     */     case 3:
/*     */     case 7:
/*     */     case 8:
/* 277 */     case 9: } return false;
/*     */   }
/*     */ 
/*     */   private void verify() throws LCMSImageLayout.ImageLayoutException
/*     */   {
/* 282 */     if ((this.offset < 0) || (this.offset >= this.dataArrayLength)) {
/* 283 */       throw new ImageLayoutException("Invalid image layout");
/*     */     }
/*     */ 
/* 286 */     if (this.nextPixelOffset != getBytesPerPixel(this.pixelType)) {
/* 287 */       throw new ImageLayoutException("Invalid image layout");
/*     */     }
/*     */ 
/* 290 */     int i = safeMult(this.nextRowOffset, this.height - 1);
/*     */ 
/* 292 */     int j = safeMult(this.nextPixelOffset, this.width - 1);
/*     */ 
/* 294 */     j = safeAdd(j, i);
/*     */ 
/* 296 */     int k = safeAdd(this.offset, j);
/*     */ 
/* 298 */     if ((k < 0) || (k >= this.dataArrayLength))
/* 299 */       throw new ImageLayoutException("Invalid image layout");
/*     */   }
/*     */ 
/*     */   static int safeAdd(int paramInt1, int paramInt2) throws LCMSImageLayout.ImageLayoutException
/*     */   {
/* 304 */     long l = paramInt1;
/* 305 */     l += paramInt2;
/* 306 */     if ((l < -2147483648L) || (l > 2147483647L)) {
/* 307 */       throw new ImageLayoutException("Invalid image layout");
/*     */     }
/* 309 */     return (int)l;
/*     */   }
/*     */ 
/*     */   static int safeMult(int paramInt1, int paramInt2) throws LCMSImageLayout.ImageLayoutException {
/* 313 */     long l = paramInt1;
/* 314 */     l *= paramInt2;
/* 315 */     if ((l < -2147483648L) || (l > 2147483647L)) {
/* 316 */       throw new ImageLayoutException("Invalid image layout");
/*     */     }
/* 318 */     return (int)l;
/*     */   }
/*     */ 
/*     */   private static int getBytesPerPixel(int paramInt)
/*     */   {
/* 341 */     int i = 0x7 & paramInt;
/* 342 */     int j = 0xF & paramInt >> 3;
/* 343 */     int k = 0x7 & paramInt >> 7;
/*     */ 
/* 345 */     return i * (j + k);
/*     */   }
/*     */ 
/*     */   public static class ImageLayoutException extends Exception
/*     */   {
/*     */     public ImageLayoutException(String paramString)
/*     */     {
/* 323 */       super();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.java2d.cmm.lcms.LCMSImageLayout
 * JD-Core Version:    0.6.2
 */