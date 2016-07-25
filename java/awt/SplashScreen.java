/*     */ package java.awt;
/*     */ 
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.awt.image.DataBuffer;
/*     */ import java.awt.image.DataBufferInt;
/*     */ import java.awt.image.SinglePixelPackedSampleModel;
/*     */ import java.awt.image.WritableRaster;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.net.URLConnection;
/*     */ import java.security.AccessController;
/*     */ import sun.awt.image.SunWritableRaster;
/*     */ import sun.security.action.LoadLibraryAction;
/*     */ import sun.util.logging.PlatformLogger;
/*     */ 
/*     */ public final class SplashScreen
/*     */ {
/*     */   private BufferedImage image;
/*     */   private final long splashPtr;
/* 376 */   private static boolean wasClosed = false;
/*     */   private URL imageURL;
/* 387 */   private static SplashScreen theInstance = null;
/*     */ 
/* 389 */   private static final PlatformLogger log = PlatformLogger.getLogger("java.awt.SplashScreen");
/*     */ 
/*     */   SplashScreen(long paramLong)
/*     */   {
/* 100 */     this.splashPtr = paramLong;
/*     */   }
/*     */ 
/*     */   public static SplashScreen getSplashScreen()
/*     */   {
/* 115 */     synchronized (SplashScreen.class) {
/* 116 */       if (GraphicsEnvironment.isHeadless()) {
/* 117 */         throw new HeadlessException();
/*     */       }
/*     */ 
/* 120 */       if ((!wasClosed) && (theInstance == null)) {
/* 121 */         AccessController.doPrivileged(new LoadLibraryAction("splashscreen"));
/*     */ 
/* 123 */         long l = _getInstance();
/* 124 */         if ((l != 0L) && (_isVisible(l))) {
/* 125 */           theInstance = new SplashScreen(l);
/*     */         }
/*     */       }
/* 128 */       return theInstance;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setImageURL(URL paramURL)
/*     */     throws NullPointerException, IOException, IllegalStateException
/*     */   {
/* 148 */     checkVisible();
/* 149 */     URLConnection localURLConnection = paramURL.openConnection();
/* 150 */     localURLConnection.connect();
/* 151 */     int i = localURLConnection.getContentLength();
/* 152 */     InputStream localInputStream = localURLConnection.getInputStream();
/* 153 */     byte[] arrayOfByte1 = new byte[i];
/* 154 */     int j = 0;
/*     */     while (true)
/*     */     {
/* 157 */       int k = localInputStream.available();
/* 158 */       if (k <= 0)
/*     */       {
/* 161 */         k = 1;
/*     */       }
/*     */ 
/* 165 */       if (j + k > i) {
/* 166 */         i = j * 2;
/* 167 */         if (j + k > i) {
/* 168 */           i = k + j;
/*     */         }
/* 170 */         byte[] arrayOfByte2 = arrayOfByte1;
/* 171 */         arrayOfByte1 = new byte[i];
/* 172 */         System.arraycopy(arrayOfByte2, 0, arrayOfByte1, 0, j);
/*     */       }
/*     */ 
/* 175 */       int m = localInputStream.read(arrayOfByte1, j, k);
/* 176 */       if (m < 0) {
/*     */         break;
/*     */       }
/* 179 */       j += m;
/*     */     }
/* 181 */     synchronized (SplashScreen.class) {
/* 182 */       checkVisible();
/* 183 */       if (!_setImageData(this.splashPtr, arrayOfByte1)) {
/* 184 */         throw new IOException("Bad image format or i/o error when loading image");
/*     */       }
/* 186 */       this.imageURL = paramURL;
/*     */     }
/*     */   }
/*     */ 
/*     */   private void checkVisible() {
/* 191 */     if (!isVisible())
/* 192 */       throw new IllegalStateException("no splash screen available");
/*     */   }
/*     */ 
/*     */   public URL getImageURL()
/*     */     throws IllegalStateException
/*     */   {
/* 202 */     synchronized (SplashScreen.class) {
/* 203 */       checkVisible();
/* 204 */       if (this.imageURL == null) {
/*     */         try {
/* 206 */           String str1 = _getImageFileName(this.splashPtr);
/* 207 */           String str2 = _getImageJarName(this.splashPtr);
/* 208 */           if (str1 != null) {
/* 209 */             if (str2 != null)
/* 210 */               this.imageURL = new URL("jar:" + new File(str2).toURL().toString() + "!/" + str1);
/*     */             else
/* 212 */               this.imageURL = new File(str1).toURL();
/*     */           }
/*     */         }
/*     */         catch (MalformedURLException localMalformedURLException)
/*     */         {
/* 217 */           if (log.isLoggable(500)) {
/* 218 */             log.fine("MalformedURLException caught in the getImageURL() method", localMalformedURLException);
/*     */           }
/*     */         }
/*     */       }
/* 222 */       return this.imageURL;
/*     */     }
/*     */   }
/*     */ 
/*     */   public Rectangle getBounds()
/*     */     throws IllegalStateException
/*     */   {
/* 241 */     synchronized (SplashScreen.class) {
/* 242 */       checkVisible();
/* 243 */       return _getBounds(this.splashPtr);
/*     */     }
/*     */   }
/*     */ 
/*     */   public Dimension getSize()
/*     */     throws IllegalStateException
/*     */   {
/* 262 */     return getBounds().getSize();
/*     */   }
/*     */ 
/*     */   public Graphics2D createGraphics()
/*     */     throws IllegalStateException
/*     */   {
/* 283 */     synchronized (SplashScreen.class) {
/* 284 */       if (this.image == null) {
/* 285 */         Dimension localDimension = getSize();
/* 286 */         this.image = new BufferedImage(localDimension.width, localDimension.height, 2);
/*     */       }
/* 288 */       return this.image.createGraphics();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void update()
/*     */     throws IllegalStateException
/*     */   {
/*     */     BufferedImage localBufferedImage;
/* 301 */     synchronized (SplashScreen.class) {
/* 302 */       checkVisible();
/* 303 */       localBufferedImage = this.image;
/*     */     }
/* 305 */     if (localBufferedImage == null) {
/* 306 */       throw new IllegalStateException("no overlay image available");
/*     */     }
/* 308 */     ??? = localBufferedImage.getRaster().getDataBuffer();
/* 309 */     if (!(??? instanceof DataBufferInt)) {
/* 310 */       throw new AssertionError("Overlay image DataBuffer is of invalid type == " + ???.getClass().getName());
/*     */     }
/* 312 */     int i = ((DataBuffer)???).getNumBanks();
/* 313 */     if (i != 1) {
/* 314 */       throw new AssertionError("Invalid number of banks ==" + i + " in overlay image DataBuffer");
/*     */     }
/* 316 */     if (!(localBufferedImage.getSampleModel() instanceof SinglePixelPackedSampleModel)) {
/* 317 */       throw new AssertionError("Overlay image has invalid sample model == " + localBufferedImage.getSampleModel().getClass().getName());
/*     */     }
/* 319 */     SinglePixelPackedSampleModel localSinglePixelPackedSampleModel = (SinglePixelPackedSampleModel)localBufferedImage.getSampleModel();
/* 320 */     int j = localSinglePixelPackedSampleModel.getScanlineStride();
/* 321 */     Rectangle localRectangle = localBufferedImage.getRaster().getBounds();
/*     */ 
/* 324 */     int[] arrayOfInt = SunWritableRaster.stealData((DataBufferInt)???, 0);
/* 325 */     synchronized (SplashScreen.class) {
/* 326 */       checkVisible();
/* 327 */       _update(this.splashPtr, arrayOfInt, localRectangle.x, localRectangle.y, localRectangle.width, localRectangle.height, j);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void close()
/*     */     throws IllegalStateException
/*     */   {
/* 338 */     synchronized (SplashScreen.class) {
/* 339 */       checkVisible();
/* 340 */       _close(this.splashPtr);
/* 341 */       this.image = null;
/* 342 */       markClosed();
/*     */     }
/*     */   }
/*     */ 
/*     */   static void markClosed() {
/* 347 */     synchronized (SplashScreen.class) {
/* 348 */       wasClosed = true;
/* 349 */       theInstance = null;
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean isVisible()
/*     */   {
/* 368 */     synchronized (SplashScreen.class) {
/* 369 */       return (!wasClosed) && (_isVisible(this.splashPtr));
/*     */     }
/*     */   }
/*     */ 
/*     */   private static native void _update(long paramLong, int[] paramArrayOfInt, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5);
/*     */ 
/*     */   private static native boolean _isVisible(long paramLong);
/*     */ 
/*     */   private static native Rectangle _getBounds(long paramLong);
/*     */ 
/*     */   private static native long _getInstance();
/*     */ 
/*     */   private static native void _close(long paramLong);
/*     */ 
/*     */   private static native String _getImageFileName(long paramLong);
/*     */ 
/*     */   private static native String _getImageJarName(long paramLong);
/*     */ 
/*     */   private static native boolean _setImageData(long paramLong, byte[] paramArrayOfByte);
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.SplashScreen
 * JD-Core Version:    0.6.2
 */