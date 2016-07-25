/*     */ package sun.awt.windows;
/*     */ 
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Image;
/*     */ import java.awt.color.ColorSpace;
/*     */ import java.awt.datatransfer.DataFlavor;
/*     */ import java.awt.datatransfer.FlavorTable;
/*     */ import java.awt.datatransfer.Transferable;
/*     */ import java.awt.datatransfer.UnsupportedFlavorException;
/*     */ import java.awt.geom.AffineTransform;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.awt.image.ComponentColorModel;
/*     */ import java.awt.image.DataBufferByte;
/*     */ import java.awt.image.DataBufferInt;
/*     */ import java.awt.image.DirectColorModel;
/*     */ import java.awt.image.Raster;
/*     */ import java.awt.image.WritableRaster;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.URL;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.SortedMap;
/*     */ import sun.awt.datatransfer.DataTransferer;
/*     */ import sun.awt.datatransfer.ToolkitThreadBlockedHandler;
/*     */ import sun.awt.image.ImageRepresentation;
/*     */ import sun.awt.image.ToolkitImage;
/*     */ 
/*     */ public class WDataTransferer extends DataTransferer
/*     */ {
/*  89 */   private static final String[] predefinedClipboardNames = { "", "TEXT", "BITMAP", "METAFILEPICT", "SYLK", "DIF", "TIFF", "OEM TEXT", "DIB", "PALETTE", "PENDATA", "RIFF", "WAVE", "UNICODE TEXT", "ENHMETAFILE", "HDROP", "LOCALE", "DIBV5" };
/*     */ 
/* 116 */   private static final Map predefinedClipboardNameMap = Collections.synchronizedMap(localHashMap);
/*     */   public static final int CF_TEXT = 1;
/*     */   public static final int CF_METAFILEPICT = 3;
/*     */   public static final int CF_DIB = 8;
/*     */   public static final int CF_ENHMETAFILE = 14;
/*     */   public static final int CF_HDROP = 15;
/*     */   public static final int CF_LOCALE = 16;
/* 129 */   public static final long CF_HTML = registerClipboardFormat("HTML Format");
/* 130 */   public static final long CFSTR_INETURL = registerClipboardFormat("UniformResourceLocator");
/* 131 */   public static final long CF_PNG = registerClipboardFormat("PNG");
/* 132 */   public static final long CF_JFIF = registerClipboardFormat("JFIF");
/*     */ 
/* 134 */   public static final long CF_FILEGROUPDESCRIPTORW = registerClipboardFormat("FileGroupDescriptorW");
/* 135 */   public static final long CF_FILEGROUPDESCRIPTORA = registerClipboardFormat("FileGroupDescriptor");
/*     */ 
/* 138 */   private static final Long L_CF_LOCALE = (Long)predefinedClipboardNameMap.get(predefinedClipboardNames[16]);
/*     */ 
/* 141 */   private static final DirectColorModel directColorModel = new DirectColorModel(24, 16711680, 65280, 255);
/*     */ 
/* 147 */   private static final int[] bandmasks = { directColorModel.getRedMask(), directColorModel.getGreenMask(), directColorModel.getBlueMask() };
/*     */   private static WDataTransferer transferer;
/* 282 */   private final ToolkitThreadBlockedHandler handler = new WToolkitThreadBlockedHandler();
/*     */ 
/* 378 */   private static final byte[] UNICODE_NULL_TERMINATOR = { 0, 0 };
/*     */ 
/*     */   public static WDataTransferer getInstanceImpl()
/*     */   {
/* 161 */     if (transferer == null) {
/* 162 */       synchronized (WDataTransferer.class) {
/* 163 */         if (transferer == null) {
/* 164 */           transferer = new WDataTransferer();
/*     */         }
/*     */       }
/*     */     }
/* 168 */     return transferer;
/*     */   }
/*     */ 
/*     */   public SortedMap getFormatsForFlavors(DataFlavor[] paramArrayOfDataFlavor, FlavorTable paramFlavorTable) {
/* 172 */     SortedMap localSortedMap = super.getFormatsForFlavors(paramArrayOfDataFlavor, paramFlavorTable);
/*     */ 
/* 176 */     localSortedMap.remove(L_CF_LOCALE);
/*     */ 
/* 178 */     return localSortedMap;
/*     */   }
/*     */ 
/*     */   public String getDefaultUnicodeEncoding() {
/* 182 */     return "utf-16le";
/*     */   }
/*     */ 
/*     */   public byte[] translateTransferable(Transferable paramTransferable, DataFlavor paramDataFlavor, long paramLong)
/*     */     throws IOException
/*     */   {
/* 189 */     byte[] arrayOfByte = super.translateTransferable(paramTransferable, paramDataFlavor, paramLong);
/*     */ 
/* 191 */     if (paramLong == CF_HTML) {
/* 192 */       arrayOfByte = HTMLCodec.convertToHTMLFormat(arrayOfByte);
/*     */     }
/* 194 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */   protected Object translateBytesOrStream(InputStream paramInputStream, byte[] paramArrayOfByte, DataFlavor paramDataFlavor, long paramLong, Transferable paramTransferable)
/*     */     throws IOException
/*     */   {
/* 202 */     if ((paramLong == CF_HTML) && (paramDataFlavor.isFlavorTextType())) {
/* 203 */       if (paramInputStream == null) {
/* 204 */         paramInputStream = new ByteArrayInputStream(paramArrayOfByte);
/* 205 */         paramArrayOfByte = null;
/*     */       }
/*     */ 
/* 208 */       paramInputStream = new HTMLCodec(paramInputStream, EHTMLReadMode.HTML_READ_ALL);
/*     */     }
/*     */     String str;
/* 211 */     if ((paramLong == CF_FILEGROUPDESCRIPTORA) || (paramLong == CF_FILEGROUPDESCRIPTORW)) {
/* 212 */       if (null != paramInputStream) {
/* 213 */         paramInputStream.close();
/*     */       }
/* 215 */       if ((paramArrayOfByte == null) || (!DataFlavor.javaFileListFlavor.equals(paramDataFlavor))) {
/* 216 */         throw new IOException("data translation failed");
/*     */       }
/* 218 */       str = new String(paramArrayOfByte, 0, paramArrayOfByte.length, "UTF-16LE");
/* 219 */       String[] arrayOfString = str.split("");
/* 220 */       if (0 == arrayOfString.length) {
/* 221 */         return null;
/*     */       }
/*     */ 
/* 225 */       File[] arrayOfFile = new File[arrayOfString.length];
/* 226 */       for (int i = 0; i < arrayOfString.length; i++) {
/* 227 */         arrayOfFile[i] = new File(arrayOfString[i]);
/*     */ 
/* 229 */         arrayOfFile[i].deleteOnExit();
/*     */       }
/*     */ 
/* 232 */       return Arrays.asList(arrayOfFile);
/*     */     }
/*     */ 
/* 235 */     if ((paramLong == CFSTR_INETURL) && (URL.class.equals(paramDataFlavor.getRepresentationClass())))
/*     */     {
/* 238 */       if (paramArrayOfByte == null) {
/* 239 */         paramArrayOfByte = inputStreamToByteArray(paramInputStream);
/* 240 */         paramInputStream = null;
/*     */       }
/* 242 */       str = getDefaultTextCharset();
/* 243 */       if ((paramTransferable != null) && (paramTransferable.isDataFlavorSupported(javaTextEncodingFlavor)))
/*     */       {
/*     */         try
/*     */         {
/* 247 */           str = new String((byte[])paramTransferable.getTransferData(javaTextEncodingFlavor), "UTF-8");
/*     */         }
/*     */         catch (UnsupportedFlavorException localUnsupportedFlavorException)
/*     */         {
/*     */         }
/*     */       }
/* 253 */       return new URL(new String(paramArrayOfByte, str));
/*     */     }
/*     */ 
/* 256 */     return super.translateBytesOrStream(paramInputStream, paramArrayOfByte, paramDataFlavor, paramLong, paramTransferable);
/*     */   }
/*     */ 
/*     */   public boolean isLocaleDependentTextFormat(long paramLong)
/*     */   {
/* 261 */     return (paramLong == 1L) || (paramLong == CFSTR_INETURL);
/*     */   }
/*     */ 
/*     */   public boolean isFileFormat(long paramLong) {
/* 265 */     return (paramLong == 15L) || (paramLong == CF_FILEGROUPDESCRIPTORA) || (paramLong == CF_FILEGROUPDESCRIPTORW);
/*     */   }
/*     */ 
/*     */   protected Long getFormatForNativeAsLong(String paramString) {
/* 269 */     Long localLong = (Long)predefinedClipboardNameMap.get(paramString);
/* 270 */     if (localLong == null) {
/* 271 */       localLong = Long.valueOf(registerClipboardFormat(paramString));
/*     */     }
/* 273 */     return localLong;
/*     */   }
/*     */ 
/*     */   protected String getNativeForFormat(long paramLong) {
/* 277 */     return paramLong < predefinedClipboardNames.length ? predefinedClipboardNames[((int)paramLong)] : getClipboardFormatName(paramLong);
/*     */   }
/*     */ 
/*     */   public ToolkitThreadBlockedHandler getToolkitThreadBlockedHandler()
/*     */   {
/* 286 */     return this.handler;
/*     */   }
/*     */ 
/*     */   private static native long registerClipboardFormat(String paramString);
/*     */ 
/*     */   private static native String getClipboardFormatName(long paramLong);
/*     */ 
/*     */   public boolean isImageFormat(long paramLong)
/*     */   {
/* 302 */     return (paramLong == 8L) || (paramLong == 14L) || (paramLong == 3L) || (paramLong == CF_PNG) || (paramLong == CF_JFIF);
/*     */   }
/*     */ 
/*     */   protected byte[] imageToPlatformBytes(Image paramImage, long paramLong)
/*     */     throws IOException
/*     */   {
/* 309 */     String str = null;
/* 310 */     if (paramLong == CF_PNG)
/* 311 */       str = "image/png";
/* 312 */     else if (paramLong == CF_JFIF) {
/* 313 */       str = "image/jpeg";
/*     */     }
/* 315 */     if (str != null) {
/* 316 */       return imageToStandardBytes(paramImage, str);
/*     */     }
/*     */ 
/* 319 */     int i = 0;
/* 320 */     int j = 0;
/*     */ 
/* 322 */     if ((paramImage instanceof ToolkitImage)) {
/* 323 */       ImageRepresentation localImageRepresentation = ((ToolkitImage)paramImage).getImageRep();
/* 324 */       localImageRepresentation.reconstruct(32);
/* 325 */       i = localImageRepresentation.getWidth();
/* 326 */       j = localImageRepresentation.getHeight();
/*     */     } else {
/* 328 */       i = paramImage.getWidth(null);
/* 329 */       j = paramImage.getHeight(null);
/*     */     }
/*     */ 
/* 343 */     int k = i * 3 % 4;
/* 344 */     int m = k > 0 ? 4 - k : 0;
/*     */ 
/* 346 */     ColorSpace localColorSpace = ColorSpace.getInstance(1000);
/* 347 */     int[] arrayOfInt1 = { 8, 8, 8 };
/* 348 */     int[] arrayOfInt2 = { 2, 1, 0 };
/* 349 */     ComponentColorModel localComponentColorModel = new ComponentColorModel(localColorSpace, arrayOfInt1, false, false, 1, 0);
/*     */ 
/* 352 */     WritableRaster localWritableRaster = Raster.createInterleavedRaster(0, i, j, i * 3 + m, 3, arrayOfInt2, null);
/*     */ 
/* 356 */     BufferedImage localBufferedImage = new BufferedImage(localComponentColorModel, localWritableRaster, false, null);
/*     */ 
/* 361 */     AffineTransform localAffineTransform = new AffineTransform(1.0F, 0.0F, 0.0F, -1.0F, 0.0F, j);
/*     */ 
/* 364 */     Graphics2D localGraphics2D = localBufferedImage.createGraphics();
/*     */     try
/*     */     {
/* 367 */       localGraphics2D.drawImage(paramImage, localAffineTransform, null);
/*     */     } finally {
/* 369 */       localGraphics2D.dispose();
/*     */     }
/*     */ 
/* 372 */     DataBufferByte localDataBufferByte = (DataBufferByte)localWritableRaster.getDataBuffer();
/*     */ 
/* 374 */     byte[] arrayOfByte = localDataBufferByte.getData();
/* 375 */     return imageDataToPlatformImageBytes(arrayOfByte, i, j, paramLong);
/*     */   }
/*     */ 
/*     */   protected ByteArrayOutputStream convertFileListToBytes(ArrayList<String> paramArrayList)
/*     */     throws IOException
/*     */   {
/* 383 */     ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
/*     */ 
/* 385 */     if (paramArrayList.isEmpty())
/*     */     {
/* 387 */       localByteArrayOutputStream.write(UNICODE_NULL_TERMINATOR);
/*     */     }
/* 389 */     else for (int i = 0; i < paramArrayList.size(); i++) {
/* 390 */         byte[] arrayOfByte = ((String)paramArrayList.get(i)).getBytes(getDefaultUnicodeEncoding());
/*     */ 
/* 392 */         localByteArrayOutputStream.write(arrayOfByte, 0, arrayOfByte.length);
/* 393 */         localByteArrayOutputStream.write(UNICODE_NULL_TERMINATOR);
/*     */       }
/*     */ 
/*     */ 
/* 401 */     localByteArrayOutputStream.write(UNICODE_NULL_TERMINATOR);
/* 402 */     return localByteArrayOutputStream;
/*     */   }
/*     */ 
/*     */   private native byte[] imageDataToPlatformImageBytes(byte[] paramArrayOfByte, int paramInt1, int paramInt2, long paramLong);
/*     */ 
/*     */   protected Image platformImageBytesOrStreamToImage(InputStream paramInputStream, byte[] paramArrayOfByte, long paramLong)
/*     */     throws IOException
/*     */   {
/* 421 */     String str = null;
/* 422 */     if (paramLong == CF_PNG)
/* 423 */       str = "image/png";
/* 424 */     else if (paramLong == CF_JFIF) {
/* 425 */       str = "image/jpeg";
/*     */     }
/* 427 */     if (str != null) {
/* 428 */       return standardImageBytesOrStreamToImage(paramInputStream, paramArrayOfByte, str);
/*     */     }
/*     */ 
/* 431 */     if (paramArrayOfByte == null) {
/* 432 */       paramArrayOfByte = inputStreamToByteArray(paramInputStream);
/*     */     }
/*     */ 
/* 435 */     int[] arrayOfInt = platformImageBytesToImageData(paramArrayOfByte, paramLong);
/* 436 */     if (arrayOfInt == null) {
/* 437 */       throw new IOException("data translation failed");
/*     */     }
/*     */ 
/* 440 */     int i = arrayOfInt.length - 2;
/* 441 */     int j = arrayOfInt[i];
/* 442 */     int k = arrayOfInt[(i + 1)];
/*     */ 
/* 444 */     DataBufferInt localDataBufferInt = new DataBufferInt(arrayOfInt, i);
/* 445 */     WritableRaster localWritableRaster = Raster.createPackedRaster(localDataBufferInt, j, k, j, bandmasks, null);
/*     */ 
/* 449 */     return new BufferedImage(directColorModel, localWritableRaster, false, null);
/*     */   }
/*     */ 
/*     */   private native int[] platformImageBytesToImageData(byte[] paramArrayOfByte, long paramLong)
/*     */     throws IOException;
/*     */ 
/*     */   protected native String[] dragQueryFile(byte[] paramArrayOfByte);
/*     */ 
/*     */   static
/*     */   {
/* 112 */     HashMap localHashMap = new HashMap(predefinedClipboardNames.length, 1.0F);
/* 113 */     for (int i = 1; i < predefinedClipboardNames.length; i++)
/* 114 */       localHashMap.put(predefinedClipboardNames[i], Long.valueOf(i));
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.windows.WDataTransferer
 * JD-Core Version:    0.6.2
 */