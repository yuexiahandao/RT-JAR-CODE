/*     */ package sun.awt.image;
/*     */ 
/*     */ import java.awt.image.ColorModel;
/*     */ import java.awt.image.DirectColorModel;
/*     */ import java.awt.image.IndexColorModel;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.security.AccessController;
/*     */ import java.util.Hashtable;
/*     */ import sun.security.action.LoadLibraryAction;
/*     */ 
/*     */ public class JPEGImageDecoder extends ImageDecoder
/*     */ {
/*     */   private static ColorModel RGBcolormodel;
/*     */   private static ColorModel ARGBcolormodel;
/*  65 */   private static ColorModel Graycolormodel = new IndexColorModel(8, 256, arrayOfByte, arrayOfByte, arrayOfByte);
/*     */ 
/*  50 */   private static final Class InputStreamClass = InputStream.class;
/*     */   private ColorModel colormodel;
/*  71 */   Hashtable props = new Hashtable();
/*     */   private static final int hintflags = 22;
/*     */ 
/*     */   private static native void initIDs(Class paramClass);
/*     */ 
/*     */   private native void readImage(InputStream paramInputStream, byte[] paramArrayOfByte)
/*     */     throws ImageFormatException, IOException;
/*     */ 
/*     */   public JPEGImageDecoder(InputStreamImageSource paramInputStreamImageSource, InputStream paramInputStream)
/*     */   {
/*  74 */     super(paramInputStreamImageSource, paramInputStream);
/*     */   }
/*     */ 
/*     */   private static void error(String paramString)
/*     */     throws ImageFormatException
/*     */   {
/*  81 */     throw new ImageFormatException(paramString);
/*     */   }
/*     */ 
/*     */   public boolean sendHeaderInfo(int paramInt1, int paramInt2, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3)
/*     */   {
/*  88 */     setDimensions(paramInt1, paramInt2);
/*     */ 
/*  90 */     setProperties(this.props);
/*  91 */     if (paramBoolean1) {
/*  92 */       this.colormodel = Graycolormodel;
/*     */     }
/*  94 */     else if (paramBoolean2)
/*  95 */       this.colormodel = ARGBcolormodel;
/*     */     else {
/*  97 */       this.colormodel = RGBcolormodel;
/*     */     }
/*     */ 
/* 101 */     setColorModel(this.colormodel);
/*     */ 
/* 103 */     int i = 22;
/* 104 */     if (!paramBoolean3) {
/* 105 */       i |= 8;
/*     */     }
/* 107 */     setHints(22);
/* 108 */     headerComplete();
/*     */ 
/* 110 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean sendPixels(int[] paramArrayOfInt, int paramInt) {
/* 114 */     int i = setPixels(0, paramInt, paramArrayOfInt.length, 1, this.colormodel, paramArrayOfInt, 0, paramArrayOfInt.length);
/*     */ 
/* 116 */     if (i <= 0) {
/* 117 */       this.aborted = true;
/*     */     }
/* 119 */     return !this.aborted;
/*     */   }
/*     */ 
/*     */   public boolean sendPixels(byte[] paramArrayOfByte, int paramInt) {
/* 123 */     int i = setPixels(0, paramInt, paramArrayOfByte.length, 1, this.colormodel, paramArrayOfByte, 0, paramArrayOfByte.length);
/*     */ 
/* 125 */     if (i <= 0) {
/* 126 */       this.aborted = true;
/*     */     }
/* 128 */     return !this.aborted;
/*     */   }
/*     */ 
/*     */   public void produceImage()
/*     */     throws IOException, ImageFormatException
/*     */   {
/*     */     try
/*     */     {
/* 136 */       readImage(this.input, new byte[1024]);
/* 137 */       if (!this.aborted)
/* 138 */         imageComplete(3, true);
/*     */     }
/*     */     catch (IOException localIOException) {
/* 141 */       if (!this.aborted)
/* 142 */         throw localIOException;
/*     */     }
/*     */     finally {
/* 145 */       close();
/*     */     }
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  56 */     AccessController.doPrivileged(new LoadLibraryAction("jpeg"));
/*     */ 
/*  58 */     initIDs(InputStreamClass);
/*  59 */     RGBcolormodel = new DirectColorModel(24, 16711680, 65280, 255);
/*  60 */     ARGBcolormodel = ColorModel.getRGBdefault();
/*  61 */     byte[] arrayOfByte = new byte[256];
/*  62 */     for (int i = 0; i < 256; i++)
/*  63 */       arrayOfByte[i] = ((byte)i);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.image.JPEGImageDecoder
 * JD-Core Version:    0.6.2
 */