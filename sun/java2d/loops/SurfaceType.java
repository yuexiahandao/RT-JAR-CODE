/*     */ package sun.java2d.loops;
/*     */ 
/*     */ import java.awt.image.ColorModel;
/*     */ import java.util.HashMap;
/*     */ import sun.awt.image.PixelConverter;
/*     */ import sun.awt.image.PixelConverter.Argb;
/*     */ import sun.awt.image.PixelConverter.ArgbBm;
/*     */ import sun.awt.image.PixelConverter.ArgbPre;
/*     */ import sun.awt.image.PixelConverter.Bgrx;
/*     */ import sun.awt.image.PixelConverter.ByteGray;
/*     */ import sun.awt.image.PixelConverter.Rgba;
/*     */ import sun.awt.image.PixelConverter.RgbaPre;
/*     */ import sun.awt.image.PixelConverter.Rgbx;
/*     */ import sun.awt.image.PixelConverter.Ushort4444Argb;
/*     */ import sun.awt.image.PixelConverter.Ushort555Rgb;
/*     */ import sun.awt.image.PixelConverter.Ushort555Rgbx;
/*     */ import sun.awt.image.PixelConverter.Ushort565Rgb;
/*     */ import sun.awt.image.PixelConverter.UshortGray;
/*     */ import sun.awt.image.PixelConverter.Xbgr;
/*     */ import sun.awt.image.PixelConverter.Xrgb;
/*     */ 
/*     */ public final class SurfaceType
/*     */ {
/*  58 */   private static int unusedUID = 1;
/*  59 */   private static HashMap surfaceUIDMap = new HashMap(100);
/*     */   public static final String DESC_ANY = "Any Surface";
/*     */   public static final String DESC_INT_RGB = "Integer RGB";
/*     */   public static final String DESC_INT_ARGB = "Integer ARGB";
/*     */   public static final String DESC_INT_ARGB_PRE = "Integer ARGB Premultiplied";
/*     */   public static final String DESC_INT_BGR = "Integer BGR";
/*     */   public static final String DESC_3BYTE_BGR = "3 Byte BGR";
/*     */   public static final String DESC_4BYTE_ABGR = "4 Byte ABGR";
/*     */   public static final String DESC_4BYTE_ABGR_PRE = "4 Byte ABGR Premultiplied";
/*     */   public static final String DESC_USHORT_565_RGB = "Short 565 RGB";
/*     */   public static final String DESC_USHORT_555_RGB = "Short 555 RGB";
/*     */   public static final String DESC_USHORT_555_RGBx = "Short 555 RGBx";
/*     */   public static final String DESC_USHORT_4444_ARGB = "Short 4444 ARGB";
/*     */   public static final String DESC_BYTE_GRAY = "8-bit Gray";
/*     */   public static final String DESC_USHORT_INDEXED = "16-bit Indexed";
/*     */   public static final String DESC_USHORT_GRAY = "16-bit Gray";
/*     */   public static final String DESC_BYTE_BINARY = "Packed Binary Bitmap";
/*     */   public static final String DESC_BYTE_INDEXED = "8-bit Indexed";
/*     */   public static final String DESC_ANY_INT = "Any Discrete Integer";
/*     */   public static final String DESC_ANY_SHORT = "Any Discrete Short";
/*     */   public static final String DESC_ANY_BYTE = "Any Discrete Byte";
/*     */   public static final String DESC_ANY_3BYTE = "Any 3 Byte Component";
/*     */   public static final String DESC_ANY_4BYTE = "Any 4 Byte Component";
/*     */   public static final String DESC_ANY_INT_DCM = "Any Integer DCM";
/*     */   public static final String DESC_INT_RGBx = "Integer RGBx";
/*     */   public static final String DESC_INT_BGRx = "Integer BGRx";
/*     */   public static final String DESC_3BYTE_RGB = "3 Byte RGB";
/*     */   public static final String DESC_INT_ARGB_BM = "Int ARGB (Bitmask)";
/*     */   public static final String DESC_BYTE_INDEXED_BM = "8-bit Indexed (Bitmask)";
/*     */   public static final String DESC_BYTE_INDEXED_OPAQUE = "8-bit Indexed (Opaque)";
/*     */   public static final String DESC_INDEX8_GRAY = "8-bit Palettized Gray";
/*     */   public static final String DESC_INDEX12_GRAY = "12-bit Palettized Gray";
/*     */   public static final String DESC_BYTE_BINARY_1BIT = "Packed Binary 1-bit Bitmap";
/*     */   public static final String DESC_BYTE_BINARY_2BIT = "Packed Binary 2-bit Bitmap";
/*     */   public static final String DESC_BYTE_BINARY_4BIT = "Packed Binary 4-bit Bitmap";
/*     */   public static final String DESC_ANY_PAINT = "Paint Object";
/*     */   public static final String DESC_ANY_COLOR = "Single Color";
/*     */   public static final String DESC_OPAQUE_COLOR = "Opaque Color";
/*     */   public static final String DESC_GRADIENT_PAINT = "Gradient Paint";
/*     */   public static final String DESC_OPAQUE_GRADIENT_PAINT = "Opaque Gradient Paint";
/*     */   public static final String DESC_TEXTURE_PAINT = "Texture Paint";
/*     */   public static final String DESC_OPAQUE_TEXTURE_PAINT = "Opaque Texture Paint";
/*     */   public static final String DESC_LINEAR_GRADIENT_PAINT = "Linear Gradient Paint";
/*     */   public static final String DESC_OPAQUE_LINEAR_GRADIENT_PAINT = "Opaque Linear Gradient Paint";
/*     */   public static final String DESC_RADIAL_GRADIENT_PAINT = "Radial Gradient Paint";
/*     */   public static final String DESC_OPAQUE_RADIAL_GRADIENT_PAINT = "Opaque Radial Gradient Paint";
/* 227 */   public static final SurfaceType Any = new SurfaceType(null, "Any Surface", PixelConverter.instance);
/*     */ 
/* 235 */   public static final SurfaceType AnyInt = Any.deriveSubType("Any Discrete Integer");
/*     */ 
/* 237 */   public static final SurfaceType AnyShort = Any.deriveSubType("Any Discrete Short");
/*     */ 
/* 239 */   public static final SurfaceType AnyByte = Any.deriveSubType("Any Discrete Byte");
/*     */ 
/* 241 */   public static final SurfaceType AnyByteBinary = Any.deriveSubType("Packed Binary Bitmap");
/*     */ 
/* 243 */   public static final SurfaceType Any3Byte = Any.deriveSubType("Any 3 Byte Component");
/*     */ 
/* 245 */   public static final SurfaceType Any4Byte = Any.deriveSubType("Any 4 Byte Component");
/*     */ 
/* 247 */   public static final SurfaceType AnyDcm = AnyInt.deriveSubType("Any Integer DCM");
/*     */ 
/* 250 */   public static final SurfaceType Custom = Any;
/* 251 */   public static final SurfaceType IntRgb = AnyDcm.deriveSubType("Integer RGB", PixelConverter.Xrgb.instance);
/*     */ 
/* 254 */   public static final SurfaceType IntArgb = AnyDcm.deriveSubType("Integer ARGB", PixelConverter.Argb.instance);
/*     */ 
/* 257 */   public static final SurfaceType IntArgbPre = AnyDcm.deriveSubType("Integer ARGB Premultiplied", PixelConverter.ArgbPre.instance);
/*     */ 
/* 261 */   public static final SurfaceType IntBgr = AnyDcm.deriveSubType("Integer BGR", PixelConverter.Xbgr.instance);
/*     */ 
/* 264 */   public static final SurfaceType ThreeByteBgr = Any3Byte.deriveSubType("3 Byte BGR", PixelConverter.Xrgb.instance);
/*     */ 
/* 267 */   public static final SurfaceType FourByteAbgr = Any4Byte.deriveSubType("4 Byte ABGR", PixelConverter.Rgba.instance);
/*     */ 
/* 270 */   public static final SurfaceType FourByteAbgrPre = Any4Byte.deriveSubType("4 Byte ABGR Premultiplied", PixelConverter.RgbaPre.instance);
/*     */ 
/* 274 */   public static final SurfaceType Ushort565Rgb = AnyShort.deriveSubType("Short 565 RGB", PixelConverter.Ushort565Rgb.instance);
/*     */ 
/* 278 */   public static final SurfaceType Ushort555Rgb = AnyShort.deriveSubType("Short 555 RGB", PixelConverter.Ushort555Rgb.instance);
/*     */ 
/* 282 */   public static final SurfaceType Ushort555Rgbx = AnyShort.deriveSubType("Short 555 RGBx", PixelConverter.Ushort555Rgbx.instance);
/*     */ 
/* 286 */   public static final SurfaceType Ushort4444Argb = AnyShort.deriveSubType("Short 4444 ARGB", PixelConverter.Ushort4444Argb.instance);
/*     */ 
/* 290 */   public static final SurfaceType UshortIndexed = AnyShort.deriveSubType("16-bit Indexed");
/*     */ 
/* 293 */   public static final SurfaceType ByteGray = AnyByte.deriveSubType("8-bit Gray", PixelConverter.ByteGray.instance);
/*     */ 
/* 297 */   public static final SurfaceType UshortGray = AnyShort.deriveSubType("16-bit Gray", PixelConverter.UshortGray.instance);
/*     */ 
/* 301 */   public static final SurfaceType ByteBinary1Bit = AnyByteBinary.deriveSubType("Packed Binary 1-bit Bitmap");
/*     */ 
/* 303 */   public static final SurfaceType ByteBinary2Bit = AnyByteBinary.deriveSubType("Packed Binary 2-bit Bitmap");
/*     */ 
/* 305 */   public static final SurfaceType ByteBinary4Bit = AnyByteBinary.deriveSubType("Packed Binary 4-bit Bitmap");
/*     */ 
/* 308 */   public static final SurfaceType ByteIndexed = AnyByte.deriveSubType("8-bit Indexed");
/*     */ 
/* 311 */   public static final SurfaceType IntRgbx = AnyDcm.deriveSubType("Integer RGBx", PixelConverter.Rgbx.instance);
/*     */ 
/* 314 */   public static final SurfaceType IntBgrx = AnyDcm.deriveSubType("Integer BGRx", PixelConverter.Bgrx.instance);
/*     */ 
/* 317 */   public static final SurfaceType ThreeByteRgb = Any3Byte.deriveSubType("3 Byte RGB", PixelConverter.Xbgr.instance);
/*     */ 
/* 320 */   public static final SurfaceType IntArgbBm = AnyDcm.deriveSubType("Int ARGB (Bitmask)", PixelConverter.ArgbBm.instance);
/*     */ 
/* 323 */   public static final SurfaceType ByteIndexedBm = ByteIndexed.deriveSubType("8-bit Indexed (Bitmask)");
/*     */ 
/* 326 */   public static final SurfaceType ByteIndexedOpaque = ByteIndexedBm.deriveSubType("8-bit Indexed (Opaque)");
/*     */ 
/* 329 */   public static final SurfaceType Index8Gray = ByteIndexedOpaque.deriveSubType("8-bit Palettized Gray");
/*     */ 
/* 332 */   public static final SurfaceType Index12Gray = Any.deriveSubType("12-bit Palettized Gray");
/*     */ 
/* 335 */   public static final SurfaceType AnyPaint = Any.deriveSubType("Paint Object");
/*     */ 
/* 338 */   public static final SurfaceType AnyColor = AnyPaint.deriveSubType("Single Color");
/*     */ 
/* 341 */   public static final SurfaceType OpaqueColor = AnyColor.deriveSubType("Opaque Color");
/*     */ 
/* 344 */   public static final SurfaceType GradientPaint = AnyPaint.deriveSubType("Gradient Paint");
/*     */ 
/* 346 */   public static final SurfaceType OpaqueGradientPaint = GradientPaint.deriveSubType("Opaque Gradient Paint");
/*     */ 
/* 349 */   public static final SurfaceType LinearGradientPaint = AnyPaint.deriveSubType("Linear Gradient Paint");
/*     */ 
/* 351 */   public static final SurfaceType OpaqueLinearGradientPaint = LinearGradientPaint.deriveSubType("Opaque Linear Gradient Paint");
/*     */ 
/* 354 */   public static final SurfaceType RadialGradientPaint = AnyPaint.deriveSubType("Radial Gradient Paint");
/*     */ 
/* 356 */   public static final SurfaceType OpaqueRadialGradientPaint = RadialGradientPaint.deriveSubType("Opaque Radial Gradient Paint");
/*     */ 
/* 359 */   public static final SurfaceType TexturePaint = AnyPaint.deriveSubType("Texture Paint");
/*     */ 
/* 361 */   public static final SurfaceType OpaqueTexturePaint = TexturePaint.deriveSubType("Opaque Texture Paint");
/*     */   private int uniqueID;
/*     */   private String desc;
/*     */   private SurfaceType next;
/*     */   protected PixelConverter pixelConverter;
/*     */ 
/*     */   public SurfaceType deriveSubType(String paramString)
/*     */   {
/* 376 */     return new SurfaceType(this, paramString);
/*     */   }
/*     */ 
/*     */   public SurfaceType deriveSubType(String paramString, PixelConverter paramPixelConverter)
/*     */   {
/* 381 */     return new SurfaceType(this, paramString, paramPixelConverter);
/*     */   }
/*     */ 
/*     */   private SurfaceType(SurfaceType paramSurfaceType, String paramString, PixelConverter paramPixelConverter)
/*     */   {
/* 391 */     this.next = paramSurfaceType;
/* 392 */     this.desc = paramString;
/* 393 */     this.uniqueID = makeUniqueID(paramString);
/* 394 */     this.pixelConverter = paramPixelConverter;
/*     */   }
/*     */ 
/*     */   private SurfaceType(SurfaceType paramSurfaceType, String paramString) {
/* 398 */     this.next = paramSurfaceType;
/* 399 */     this.desc = paramString;
/* 400 */     this.uniqueID = makeUniqueID(paramString);
/* 401 */     this.pixelConverter = paramSurfaceType.pixelConverter;
/*     */   }
/*     */ 
/*     */   public static final synchronized int makeUniqueID(String paramString) {
/* 405 */     Integer localInteger = (Integer)surfaceUIDMap.get(paramString);
/*     */ 
/* 407 */     if (localInteger == null) {
/* 408 */       if (unusedUID > 255) {
/* 409 */         throw new InternalError("surface type id overflow");
/*     */       }
/* 411 */       localInteger = Integer.valueOf(unusedUID++);
/* 412 */       surfaceUIDMap.put(paramString, localInteger);
/*     */     }
/* 414 */     return localInteger.intValue();
/*     */   }
/*     */ 
/*     */   public int getUniqueID() {
/* 418 */     return this.uniqueID;
/*     */   }
/*     */ 
/*     */   public String getDescriptor() {
/* 422 */     return this.desc;
/*     */   }
/*     */ 
/*     */   public SurfaceType getSuperType() {
/* 426 */     return this.next;
/*     */   }
/*     */ 
/*     */   public PixelConverter getPixelConverter() {
/* 430 */     return this.pixelConverter;
/*     */   }
/*     */ 
/*     */   public int pixelFor(int paramInt, ColorModel paramColorModel) {
/* 434 */     return this.pixelConverter.rgbToPixel(paramInt, paramColorModel);
/*     */   }
/*     */ 
/*     */   public int rgbFor(int paramInt, ColorModel paramColorModel) {
/* 438 */     return this.pixelConverter.pixelToRgb(paramInt, paramColorModel);
/*     */   }
/*     */ 
/*     */   public int getAlphaMask() {
/* 442 */     return this.pixelConverter.getAlphaMask();
/*     */   }
/*     */ 
/*     */   public int hashCode() {
/* 446 */     return this.desc.hashCode();
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject) {
/* 450 */     if ((paramObject instanceof SurfaceType)) {
/* 451 */       return ((SurfaceType)paramObject).uniqueID == this.uniqueID;
/*     */     }
/* 453 */     return false;
/*     */   }
/*     */ 
/*     */   public String toString() {
/* 457 */     return this.desc;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.java2d.loops.SurfaceType
 * JD-Core Version:    0.6.2
 */