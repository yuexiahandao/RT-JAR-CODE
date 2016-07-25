/*      */ package javax.imageio;
/*      */ 
/*      */ import java.awt.Rectangle;
/*      */ import java.awt.image.BufferedImage;
/*      */ import java.awt.image.Raster;
/*      */ import java.awt.image.RenderedImage;
/*      */ import java.io.IOException;
/*      */ import java.security.AccessController;
/*      */ import java.security.PrivilegedAction;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Locale;
/*      */ import java.util.MissingResourceException;
/*      */ import java.util.ResourceBundle;
/*      */ import java.util.Set;
/*      */ import javax.imageio.event.IIOReadProgressListener;
/*      */ import javax.imageio.event.IIOReadUpdateListener;
/*      */ import javax.imageio.event.IIOReadWarningListener;
/*      */ import javax.imageio.metadata.IIOMetadata;
/*      */ import javax.imageio.spi.ImageReaderSpi;
/*      */ import javax.imageio.stream.ImageInputStream;
/*      */ 
/*      */ public abstract class ImageReader
/*      */ {
/*      */   protected ImageReaderSpi originatingProvider;
/*   88 */   protected Object input = null;
/*      */ 
/*   98 */   protected boolean seekForwardOnly = false;
/*      */ 
/*  107 */   protected boolean ignoreMetadata = false;
/*      */ 
/*  119 */   protected int minIndex = 0;
/*      */ 
/*  126 */   protected Locale[] availableLocales = null;
/*      */ 
/*  132 */   protected Locale locale = null;
/*      */ 
/*  140 */   protected List<IIOReadWarningListener> warningListeners = null;
/*      */ 
/*  148 */   protected List<Locale> warningLocales = null;
/*      */ 
/*  156 */   protected List<IIOReadProgressListener> progressListeners = null;
/*      */ 
/*  164 */   protected List<IIOReadUpdateListener> updateListeners = null;
/*      */ 
/*  170 */   private boolean abortFlag = false;
/*      */ 
/*      */   protected ImageReader(ImageReaderSpi paramImageReaderSpi)
/*      */   {
/*  186 */     this.originatingProvider = paramImageReaderSpi;
/*      */   }
/*      */ 
/*      */   public String getFormatName()
/*      */     throws IOException
/*      */   {
/*  205 */     return this.originatingProvider.getFormatNames()[0];
/*      */   }
/*      */ 
/*      */   public ImageReaderSpi getOriginatingProvider()
/*      */   {
/*  217 */     return this.originatingProvider;
/*      */   }
/*      */ 
/*      */   public void setInput(Object paramObject, boolean paramBoolean1, boolean paramBoolean2)
/*      */   {
/*  291 */     if (paramObject != null) {
/*  292 */       int i = 0;
/*  293 */       if (this.originatingProvider != null) {
/*  294 */         Class[] arrayOfClass = this.originatingProvider.getInputTypes();
/*  295 */         for (int j = 0; j < arrayOfClass.length; j++) {
/*  296 */           if (arrayOfClass[j].isInstance(paramObject)) {
/*  297 */             i = 1;
/*  298 */             break;
/*      */           }
/*      */         }
/*      */       }
/*  302 */       else if ((paramObject instanceof ImageInputStream)) {
/*  303 */         i = 1;
/*      */       }
/*      */ 
/*  306 */       if (i == 0) {
/*  307 */         throw new IllegalArgumentException("Incorrect input type!");
/*      */       }
/*      */ 
/*  310 */       this.seekForwardOnly = paramBoolean1;
/*  311 */       this.ignoreMetadata = paramBoolean2;
/*  312 */       this.minIndex = 0;
/*      */     }
/*      */ 
/*  315 */     this.input = paramObject;
/*      */   }
/*      */ 
/*      */   public void setInput(Object paramObject, boolean paramBoolean)
/*      */   {
/*  355 */     setInput(paramObject, paramBoolean, false);
/*      */   }
/*      */ 
/*      */   public void setInput(Object paramObject)
/*      */   {
/*  380 */     setInput(paramObject, false, false);
/*      */   }
/*      */ 
/*      */   public Object getInput()
/*      */   {
/*  395 */     return this.input;
/*      */   }
/*      */ 
/*      */   public boolean isSeekForwardOnly()
/*      */   {
/*  410 */     return this.seekForwardOnly;
/*      */   }
/*      */ 
/*      */   public boolean isIgnoringMetadata()
/*      */   {
/*  424 */     return this.ignoreMetadata;
/*      */   }
/*      */ 
/*      */   public int getMinIndex()
/*      */   {
/*  438 */     return this.minIndex;
/*      */   }
/*      */ 
/*      */   public Locale[] getAvailableLocales()
/*      */   {
/*  457 */     if (this.availableLocales == null) {
/*  458 */       return null;
/*      */     }
/*  460 */     return (Locale[])this.availableLocales.clone();
/*      */   }
/*      */ 
/*      */   public void setLocale(Locale paramLocale)
/*      */   {
/*  480 */     if (paramLocale != null) {
/*  481 */       Locale[] arrayOfLocale = getAvailableLocales();
/*  482 */       int i = 0;
/*  483 */       if (arrayOfLocale != null) {
/*  484 */         for (int j = 0; j < arrayOfLocale.length; j++) {
/*  485 */           if (paramLocale.equals(arrayOfLocale[j])) {
/*  486 */             i = 1;
/*  487 */             break;
/*      */           }
/*      */         }
/*      */       }
/*  491 */       if (i == 0) {
/*  492 */         throw new IllegalArgumentException("Invalid locale!");
/*      */       }
/*      */     }
/*  495 */     this.locale = paramLocale;
/*      */   }
/*      */ 
/*      */   public Locale getLocale()
/*      */   {
/*  507 */     return this.locale;
/*      */   }
/*      */ 
/*      */   public abstract int getNumImages(boolean paramBoolean)
/*      */     throws IOException;
/*      */ 
/*      */   public abstract int getWidth(int paramInt)
/*      */     throws IOException;
/*      */ 
/*      */   public abstract int getHeight(int paramInt)
/*      */     throws IOException;
/*      */ 
/*      */   public boolean isRandomAccessEasy(int paramInt)
/*      */     throws IOException
/*      */   {
/*  629 */     return false;
/*      */   }
/*      */ 
/*      */   public float getAspectRatio(int paramInt)
/*      */     throws IOException
/*      */   {
/*  654 */     return getWidth(paramInt) / getHeight(paramInt);
/*      */   }
/*      */ 
/*      */   public ImageTypeSpecifier getRawImageType(int paramInt)
/*      */     throws IOException
/*      */   {
/*  682 */     return (ImageTypeSpecifier)getImageTypes(paramInt).next();
/*      */   }
/*      */ 
/*      */   public abstract Iterator<ImageTypeSpecifier> getImageTypes(int paramInt)
/*      */     throws IOException;
/*      */ 
/*      */   public ImageReadParam getDefaultReadParam()
/*      */   {
/*  732 */     return new ImageReadParam();
/*      */   }
/*      */ 
/*      */   public abstract IIOMetadata getStreamMetadata()
/*      */     throws IOException;
/*      */ 
/*      */   public IIOMetadata getStreamMetadata(String paramString, Set<String> paramSet)
/*      */     throws IOException
/*      */   {
/*  793 */     return getMetadata(paramString, paramSet, true, 0);
/*      */   }
/*      */ 
/*      */   private IIOMetadata getMetadata(String paramString, Set paramSet, boolean paramBoolean, int paramInt)
/*      */     throws IOException
/*      */   {
/*  800 */     if (paramString == null) {
/*  801 */       throw new IllegalArgumentException("formatName == null!");
/*      */     }
/*  803 */     if (paramSet == null) {
/*  804 */       throw new IllegalArgumentException("nodeNames == null!");
/*      */     }
/*  806 */     IIOMetadata localIIOMetadata = paramBoolean ? getStreamMetadata() : getImageMetadata(paramInt);
/*      */ 
/*  810 */     if (localIIOMetadata != null) {
/*  811 */       if ((localIIOMetadata.isStandardMetadataFormatSupported()) && (paramString.equals("javax_imageio_1.0")))
/*      */       {
/*  814 */         return localIIOMetadata;
/*      */       }
/*  816 */       String str = localIIOMetadata.getNativeMetadataFormatName();
/*  817 */       if ((str != null) && (paramString.equals(str))) {
/*  818 */         return localIIOMetadata;
/*      */       }
/*  820 */       String[] arrayOfString = localIIOMetadata.getExtraMetadataFormatNames();
/*  821 */       if (arrayOfString != null) {
/*  822 */         for (int i = 0; i < arrayOfString.length; i++) {
/*  823 */           if (paramString.equals(arrayOfString[i])) {
/*  824 */             return localIIOMetadata;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*  829 */     return null;
/*      */   }
/*      */ 
/*      */   public abstract IIOMetadata getImageMetadata(int paramInt)
/*      */     throws IOException;
/*      */ 
/*      */   public IIOMetadata getImageMetadata(int paramInt, String paramString, Set<String> paramSet)
/*      */     throws IOException
/*      */   {
/*  904 */     return getMetadata(paramString, paramSet, false, paramInt);
/*      */   }
/*      */ 
/*      */   public BufferedImage read(int paramInt)
/*      */     throws IOException
/*      */   {
/*  940 */     return read(paramInt, null);
/*      */   }
/*      */ 
/*      */   public abstract BufferedImage read(int paramInt, ImageReadParam paramImageReadParam)
/*      */     throws IOException;
/*      */ 
/*      */   public IIOImage readAll(int paramInt, ImageReadParam paramImageReadParam)
/*      */     throws IOException
/*      */   {
/* 1063 */     if (paramInt < getMinIndex()) {
/* 1064 */       throw new IndexOutOfBoundsException("imageIndex < getMinIndex()!");
/*      */     }
/*      */ 
/* 1067 */     BufferedImage localBufferedImage = read(paramInt, paramImageReadParam);
/*      */ 
/* 1069 */     ArrayList localArrayList = null;
/* 1070 */     int i = getNumThumbnails(paramInt);
/* 1071 */     if (i > 0) {
/* 1072 */       localArrayList = new ArrayList();
/* 1073 */       for (int j = 0; j < i; j++) {
/* 1074 */         localArrayList.add(readThumbnail(paramInt, j));
/*      */       }
/*      */     }
/*      */ 
/* 1078 */     IIOMetadata localIIOMetadata = getImageMetadata(paramInt);
/* 1079 */     return new IIOImage(localBufferedImage, localArrayList, localIIOMetadata);
/*      */   }
/*      */ 
/*      */   public Iterator<IIOImage> readAll(Iterator<? extends ImageReadParam> paramIterator)
/*      */     throws IOException
/*      */   {
/* 1160 */     ArrayList localArrayList1 = new ArrayList();
/*      */ 
/* 1162 */     int i = getMinIndex();
/*      */ 
/* 1165 */     processSequenceStarted(i);
/*      */     while (true)
/*      */     {
/* 1171 */       ImageReadParam localImageReadParam = null;
/* 1172 */       if ((paramIterator != null) && (paramIterator.hasNext())) {
/* 1173 */         localObject = paramIterator.next();
/* 1174 */         if (localObject != null) {
/* 1175 */           if ((localObject instanceof ImageReadParam))
/* 1176 */             localImageReadParam = (ImageReadParam)localObject;
/*      */           else {
/* 1178 */             throw new IllegalArgumentException("Non-ImageReadParam supplied as part of params!");
/*      */           }
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1184 */       Object localObject = null;
/*      */       try {
/* 1186 */         localObject = read(i, localImageReadParam);
/*      */       } catch (IndexOutOfBoundsException localIndexOutOfBoundsException) {
/* 1188 */         break;
/*      */       }
/*      */ 
/* 1191 */       ArrayList localArrayList2 = null;
/* 1192 */       int j = getNumThumbnails(i);
/* 1193 */       if (j > 0) {
/* 1194 */         localArrayList2 = new ArrayList();
/* 1195 */         for (int k = 0; k < j; k++) {
/* 1196 */           localArrayList2.add(readThumbnail(i, k));
/*      */         }
/*      */       }
/*      */ 
/* 1200 */       IIOMetadata localIIOMetadata = getImageMetadata(i);
/* 1201 */       IIOImage localIIOImage = new IIOImage((RenderedImage)localObject, localArrayList2, localIIOMetadata);
/* 1202 */       localArrayList1.add(localIIOImage);
/*      */ 
/* 1204 */       i++;
/*      */     }
/*      */ 
/* 1208 */     processSequenceComplete();
/*      */ 
/* 1210 */     return localArrayList1.iterator();
/*      */   }
/*      */ 
/*      */   public boolean canReadRaster()
/*      */   {
/* 1230 */     return false;
/*      */   }
/*      */ 
/*      */   public Raster readRaster(int paramInt, ImageReadParam paramImageReadParam)
/*      */     throws IOException
/*      */   {
/* 1288 */     throw new UnsupportedOperationException("readRaster not supported!");
/*      */   }
/*      */ 
/*      */   public boolean isImageTiled(int paramInt)
/*      */     throws IOException
/*      */   {
/* 1325 */     return false;
/*      */   }
/*      */ 
/*      */   public int getTileWidth(int paramInt)
/*      */     throws IOException
/*      */   {
/* 1346 */     return getWidth(paramInt);
/*      */   }
/*      */ 
/*      */   public int getTileHeight(int paramInt)
/*      */     throws IOException
/*      */   {
/* 1367 */     return getHeight(paramInt);
/*      */   }
/*      */ 
/*      */   public int getTileGridXOffset(int paramInt)
/*      */     throws IOException
/*      */   {
/* 1397 */     return 0;
/*      */   }
/*      */ 
/*      */   public int getTileGridYOffset(int paramInt)
/*      */     throws IOException
/*      */   {
/* 1427 */     return 0;
/*      */   }
/*      */ 
/*      */   public BufferedImage readTile(int paramInt1, int paramInt2, int paramInt3)
/*      */     throws IOException
/*      */   {
/* 1472 */     if ((paramInt2 != 0) || (paramInt3 != 0)) {
/* 1473 */       throw new IllegalArgumentException("Invalid tile indices");
/*      */     }
/* 1475 */     return read(paramInt1);
/*      */   }
/*      */ 
/*      */   public Raster readTileRaster(int paramInt1, int paramInt2, int paramInt3)
/*      */     throws IOException
/*      */   {
/* 1518 */     if (!canReadRaster()) {
/* 1519 */       throw new UnsupportedOperationException("readTileRaster not supported!");
/*      */     }
/*      */ 
/* 1522 */     if ((paramInt2 != 0) || (paramInt3 != 0)) {
/* 1523 */       throw new IllegalArgumentException("Invalid tile indices");
/*      */     }
/* 1525 */     return readRaster(paramInt1, null);
/*      */   }
/*      */ 
/*      */   public RenderedImage readAsRenderedImage(int paramInt, ImageReadParam paramImageReadParam)
/*      */     throws IOException
/*      */   {
/* 1577 */     return read(paramInt, paramImageReadParam);
/*      */   }
/*      */ 
/*      */   public boolean readerSupportsThumbnails()
/*      */   {
/* 1600 */     return false;
/*      */   }
/*      */ 
/*      */   public boolean hasThumbnails(int paramInt)
/*      */     throws IOException
/*      */   {
/* 1625 */     return getNumThumbnails(paramInt) > 0;
/*      */   }
/*      */ 
/*      */   public int getNumThumbnails(int paramInt)
/*      */     throws IOException
/*      */   {
/* 1652 */     return 0;
/*      */   }
/*      */ 
/*      */   public int getThumbnailWidth(int paramInt1, int paramInt2)
/*      */     throws IOException
/*      */   {
/* 1685 */     return readThumbnail(paramInt1, paramInt2).getWidth();
/*      */   }
/*      */ 
/*      */   public int getThumbnailHeight(int paramInt1, int paramInt2)
/*      */     throws IOException
/*      */   {
/* 1718 */     return readThumbnail(paramInt1, paramInt2).getHeight();
/*      */   }
/*      */ 
/*      */   public BufferedImage readThumbnail(int paramInt1, int paramInt2)
/*      */     throws IOException
/*      */   {
/* 1755 */     throw new UnsupportedOperationException("Thumbnails not supported!");
/*      */   }
/*      */ 
/*      */   public synchronized void abort()
/*      */   {
/* 1769 */     this.abortFlag = true;
/*      */   }
/*      */ 
/*      */   protected synchronized boolean abortRequested()
/*      */   {
/* 1784 */     return this.abortFlag;
/*      */   }
/*      */ 
/*      */   protected synchronized void clearAbortRequest()
/*      */   {
/* 1796 */     this.abortFlag = false;
/*      */   }
/*      */ 
/*      */   static List addToList(List paramList, Object paramObject)
/*      */   {
/* 1804 */     if (paramList == null) {
/* 1805 */       paramList = new ArrayList();
/*      */     }
/* 1807 */     paramList.add(paramObject);
/* 1808 */     return paramList;
/*      */   }
/*      */ 
/*      */   static List removeFromList(List paramList, Object paramObject)
/*      */   {
/* 1815 */     if (paramList == null) {
/* 1816 */       return paramList;
/*      */     }
/* 1818 */     paramList.remove(paramObject);
/* 1819 */     if (paramList.size() == 0) {
/* 1820 */       paramList = null;
/*      */     }
/* 1822 */     return paramList;
/*      */   }
/*      */ 
/*      */   public void addIIOReadWarningListener(IIOReadWarningListener paramIIOReadWarningListener)
/*      */   {
/* 1839 */     if (paramIIOReadWarningListener == null) {
/* 1840 */       return;
/*      */     }
/* 1842 */     this.warningListeners = addToList(this.warningListeners, paramIIOReadWarningListener);
/* 1843 */     this.warningLocales = addToList(this.warningLocales, getLocale());
/*      */   }
/*      */ 
/*      */   public void removeIIOReadWarningListener(IIOReadWarningListener paramIIOReadWarningListener)
/*      */   {
/* 1857 */     if ((paramIIOReadWarningListener == null) || (this.warningListeners == null)) {
/* 1858 */       return;
/*      */     }
/* 1860 */     int i = this.warningListeners.indexOf(paramIIOReadWarningListener);
/* 1861 */     if (i != -1) {
/* 1862 */       this.warningListeners.remove(i);
/* 1863 */       this.warningLocales.remove(i);
/* 1864 */       if (this.warningListeners.size() == 0) {
/* 1865 */         this.warningListeners = null;
/* 1866 */         this.warningLocales = null;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public void removeAllIIOReadWarningListeners()
/*      */   {
/* 1880 */     this.warningListeners = null;
/* 1881 */     this.warningLocales = null;
/*      */   }
/*      */ 
/*      */   public void addIIOReadProgressListener(IIOReadProgressListener paramIIOReadProgressListener)
/*      */   {
/* 1895 */     if (paramIIOReadProgressListener == null) {
/* 1896 */       return;
/*      */     }
/* 1898 */     this.progressListeners = addToList(this.progressListeners, paramIIOReadProgressListener);
/*      */   }
/*      */ 
/*      */   public void removeIIOReadProgressListener(IIOReadProgressListener paramIIOReadProgressListener)
/*      */   {
/* 1914 */     if ((paramIIOReadProgressListener == null) || (this.progressListeners == null)) {
/* 1915 */       return;
/*      */     }
/* 1917 */     this.progressListeners = removeFromList(this.progressListeners, paramIIOReadProgressListener);
/*      */   }
/*      */ 
/*      */   public void removeAllIIOReadProgressListeners()
/*      */   {
/* 1929 */     this.progressListeners = null;
/*      */   }
/*      */ 
/*      */   public void addIIOReadUpdateListener(IIOReadUpdateListener paramIIOReadUpdateListener)
/*      */   {
/* 1967 */     if (paramIIOReadUpdateListener == null) {
/* 1968 */       return;
/*      */     }
/* 1970 */     this.updateListeners = addToList(this.updateListeners, paramIIOReadUpdateListener);
/*      */   }
/*      */ 
/*      */   public void removeIIOReadUpdateListener(IIOReadUpdateListener paramIIOReadUpdateListener)
/*      */   {
/* 1985 */     if ((paramIIOReadUpdateListener == null) || (this.updateListeners == null)) {
/* 1986 */       return;
/*      */     }
/* 1988 */     this.updateListeners = removeFromList(this.updateListeners, paramIIOReadUpdateListener);
/*      */   }
/*      */ 
/*      */   public void removeAllIIOReadUpdateListeners()
/*      */   {
/* 2000 */     this.updateListeners = null;
/*      */   }
/*      */ 
/*      */   protected void processSequenceStarted(int paramInt)
/*      */   {
/* 2012 */     if (this.progressListeners == null) {
/* 2013 */       return;
/*      */     }
/* 2015 */     int i = this.progressListeners.size();
/* 2016 */     for (int j = 0; j < i; j++) {
/* 2017 */       IIOReadProgressListener localIIOReadProgressListener = (IIOReadProgressListener)this.progressListeners.get(j);
/*      */ 
/* 2019 */       localIIOReadProgressListener.sequenceStarted(this, paramInt);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void processSequenceComplete()
/*      */   {
/* 2030 */     if (this.progressListeners == null) {
/* 2031 */       return;
/*      */     }
/* 2033 */     int i = this.progressListeners.size();
/* 2034 */     for (int j = 0; j < i; j++) {
/* 2035 */       IIOReadProgressListener localIIOReadProgressListener = (IIOReadProgressListener)this.progressListeners.get(j);
/*      */ 
/* 2037 */       localIIOReadProgressListener.sequenceComplete(this);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void processImageStarted(int paramInt)
/*      */   {
/* 2050 */     if (this.progressListeners == null) {
/* 2051 */       return;
/*      */     }
/* 2053 */     int i = this.progressListeners.size();
/* 2054 */     for (int j = 0; j < i; j++) {
/* 2055 */       IIOReadProgressListener localIIOReadProgressListener = (IIOReadProgressListener)this.progressListeners.get(j);
/*      */ 
/* 2057 */       localIIOReadProgressListener.imageStarted(this, paramInt);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void processImageProgress(float paramFloat)
/*      */   {
/* 2071 */     if (this.progressListeners == null) {
/* 2072 */       return;
/*      */     }
/* 2074 */     int i = this.progressListeners.size();
/* 2075 */     for (int j = 0; j < i; j++) {
/* 2076 */       IIOReadProgressListener localIIOReadProgressListener = (IIOReadProgressListener)this.progressListeners.get(j);
/*      */ 
/* 2078 */       localIIOReadProgressListener.imageProgress(this, paramFloat);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void processImageComplete()
/*      */   {
/* 2089 */     if (this.progressListeners == null) {
/* 2090 */       return;
/*      */     }
/* 2092 */     int i = this.progressListeners.size();
/* 2093 */     for (int j = 0; j < i; j++) {
/* 2094 */       IIOReadProgressListener localIIOReadProgressListener = (IIOReadProgressListener)this.progressListeners.get(j);
/*      */ 
/* 2096 */       localIIOReadProgressListener.imageComplete(this);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void processThumbnailStarted(int paramInt1, int paramInt2)
/*      */   {
/* 2112 */     if (this.progressListeners == null) {
/* 2113 */       return;
/*      */     }
/* 2115 */     int i = this.progressListeners.size();
/* 2116 */     for (int j = 0; j < i; j++) {
/* 2117 */       IIOReadProgressListener localIIOReadProgressListener = (IIOReadProgressListener)this.progressListeners.get(j);
/*      */ 
/* 2119 */       localIIOReadProgressListener.thumbnailStarted(this, paramInt1, paramInt2);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void processThumbnailProgress(float paramFloat)
/*      */   {
/* 2133 */     if (this.progressListeners == null) {
/* 2134 */       return;
/*      */     }
/* 2136 */     int i = this.progressListeners.size();
/* 2137 */     for (int j = 0; j < i; j++) {
/* 2138 */       IIOReadProgressListener localIIOReadProgressListener = (IIOReadProgressListener)this.progressListeners.get(j);
/*      */ 
/* 2140 */       localIIOReadProgressListener.thumbnailProgress(this, paramFloat);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void processThumbnailComplete()
/*      */   {
/* 2151 */     if (this.progressListeners == null) {
/* 2152 */       return;
/*      */     }
/* 2154 */     int i = this.progressListeners.size();
/* 2155 */     for (int j = 0; j < i; j++) {
/* 2156 */       IIOReadProgressListener localIIOReadProgressListener = (IIOReadProgressListener)this.progressListeners.get(j);
/*      */ 
/* 2158 */       localIIOReadProgressListener.thumbnailComplete(this);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void processReadAborted()
/*      */   {
/* 2169 */     if (this.progressListeners == null) {
/* 2170 */       return;
/*      */     }
/* 2172 */     int i = this.progressListeners.size();
/* 2173 */     for (int j = 0; j < i; j++) {
/* 2174 */       IIOReadProgressListener localIIOReadProgressListener = (IIOReadProgressListener)this.progressListeners.get(j);
/*      */ 
/* 2176 */       localIIOReadProgressListener.readAborted(this);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void processPassStarted(BufferedImage paramBufferedImage, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int[] paramArrayOfInt)
/*      */   {
/* 2205 */     if (this.updateListeners == null) {
/* 2206 */       return;
/*      */     }
/* 2208 */     int i = this.updateListeners.size();
/* 2209 */     for (int j = 0; j < i; j++) {
/* 2210 */       IIOReadUpdateListener localIIOReadUpdateListener = (IIOReadUpdateListener)this.updateListeners.get(j);
/*      */ 
/* 2212 */       localIIOReadUpdateListener.passStarted(this, paramBufferedImage, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramInt7, paramArrayOfInt);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void processImageUpdate(BufferedImage paramBufferedImage, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int[] paramArrayOfInt)
/*      */   {
/* 2246 */     if (this.updateListeners == null) {
/* 2247 */       return;
/*      */     }
/* 2249 */     int i = this.updateListeners.size();
/* 2250 */     for (int j = 0; j < i; j++) {
/* 2251 */       IIOReadUpdateListener localIIOReadUpdateListener = (IIOReadUpdateListener)this.updateListeners.get(j);
/*      */ 
/* 2253 */       localIIOReadUpdateListener.imageUpdate(this, paramBufferedImage, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramArrayOfInt);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void processPassComplete(BufferedImage paramBufferedImage)
/*      */   {
/* 2271 */     if (this.updateListeners == null) {
/* 2272 */       return;
/*      */     }
/* 2274 */     int i = this.updateListeners.size();
/* 2275 */     for (int j = 0; j < i; j++) {
/* 2276 */       IIOReadUpdateListener localIIOReadUpdateListener = (IIOReadUpdateListener)this.updateListeners.get(j);
/*      */ 
/* 2278 */       localIIOReadUpdateListener.passComplete(this, paramBufferedImage);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void processThumbnailPassStarted(BufferedImage paramBufferedImage, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int[] paramArrayOfInt)
/*      */   {
/* 2308 */     if (this.updateListeners == null) {
/* 2309 */       return;
/*      */     }
/* 2311 */     int i = this.updateListeners.size();
/* 2312 */     for (int j = 0; j < i; j++) {
/* 2313 */       IIOReadUpdateListener localIIOReadUpdateListener = (IIOReadUpdateListener)this.updateListeners.get(j);
/*      */ 
/* 2315 */       localIIOReadUpdateListener.thumbnailPassStarted(this, paramBufferedImage, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramInt7, paramArrayOfInt);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void processThumbnailUpdate(BufferedImage paramBufferedImage, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int[] paramArrayOfInt)
/*      */   {
/* 2350 */     if (this.updateListeners == null) {
/* 2351 */       return;
/*      */     }
/* 2353 */     int i = this.updateListeners.size();
/* 2354 */     for (int j = 0; j < i; j++) {
/* 2355 */       IIOReadUpdateListener localIIOReadUpdateListener = (IIOReadUpdateListener)this.updateListeners.get(j);
/*      */ 
/* 2357 */       localIIOReadUpdateListener.thumbnailUpdate(this, paramBufferedImage, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramArrayOfInt);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void processThumbnailPassComplete(BufferedImage paramBufferedImage)
/*      */   {
/* 2376 */     if (this.updateListeners == null) {
/* 2377 */       return;
/*      */     }
/* 2379 */     int i = this.updateListeners.size();
/* 2380 */     for (int j = 0; j < i; j++) {
/* 2381 */       IIOReadUpdateListener localIIOReadUpdateListener = (IIOReadUpdateListener)this.updateListeners.get(j);
/*      */ 
/* 2383 */       localIIOReadUpdateListener.thumbnailPassComplete(this, paramBufferedImage);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void processWarningOccurred(String paramString)
/*      */   {
/* 2399 */     if (this.warningListeners == null) {
/* 2400 */       return;
/*      */     }
/* 2402 */     if (paramString == null) {
/* 2403 */       throw new IllegalArgumentException("warning == null!");
/*      */     }
/* 2405 */     int i = this.warningListeners.size();
/* 2406 */     for (int j = 0; j < i; j++) {
/* 2407 */       IIOReadWarningListener localIIOReadWarningListener = (IIOReadWarningListener)this.warningListeners.get(j);
/*      */ 
/* 2410 */       localIIOReadWarningListener.warningOccurred(this, paramString);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void processWarningOccurred(String paramString1, String paramString2)
/*      */   {
/* 2441 */     if (this.warningListeners == null) {
/* 2442 */       return;
/*      */     }
/* 2444 */     if (paramString1 == null) {
/* 2445 */       throw new IllegalArgumentException("baseName == null!");
/*      */     }
/* 2447 */     if (paramString2 == null) {
/* 2448 */       throw new IllegalArgumentException("keyword == null!");
/*      */     }
/* 2450 */     int i = this.warningListeners.size();
/* 2451 */     for (int j = 0; j < i; j++) {
/* 2452 */       IIOReadWarningListener localIIOReadWarningListener = (IIOReadWarningListener)this.warningListeners.get(j);
/*      */ 
/* 2454 */       Locale localLocale = (Locale)this.warningLocales.get(j);
/* 2455 */       if (localLocale == null) {
/* 2456 */         localLocale = Locale.getDefault();
/*      */       }
/*      */ 
/* 2467 */       ClassLoader localClassLoader = (ClassLoader)AccessController.doPrivileged(new PrivilegedAction()
/*      */       {
/*      */         public Object run()
/*      */         {
/* 2471 */           return Thread.currentThread().getContextClassLoader();
/*      */         }
/*      */       });
/* 2475 */       ResourceBundle localResourceBundle = null;
/*      */       try {
/* 2477 */         localResourceBundle = ResourceBundle.getBundle(paramString1, localLocale, localClassLoader);
/*      */       } catch (MissingResourceException localMissingResourceException1) {
/*      */         try {
/* 2480 */           localResourceBundle = ResourceBundle.getBundle(paramString1, localLocale);
/*      */         } catch (MissingResourceException localMissingResourceException2) {
/* 2482 */           throw new IllegalArgumentException("Bundle not found!");
/*      */         }
/*      */       }
/*      */ 
/* 2486 */       String str = null;
/*      */       try {
/* 2488 */         str = localResourceBundle.getString(paramString2);
/*      */       } catch (ClassCastException localClassCastException) {
/* 2490 */         throw new IllegalArgumentException("Resource is not a String!");
/*      */       } catch (MissingResourceException localMissingResourceException3) {
/* 2492 */         throw new IllegalArgumentException("Resource is missing!");
/*      */       }
/*      */ 
/* 2495 */       localIIOReadWarningListener.warningOccurred(this, str);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void reset()
/*      */   {
/* 2512 */     setInput(null, false, false);
/* 2513 */     setLocale(null);
/* 2514 */     removeAllIIOReadUpdateListeners();
/* 2515 */     removeAllIIOReadProgressListeners();
/* 2516 */     removeAllIIOReadWarningListeners();
/* 2517 */     clearAbortRequest();
/*      */   }
/*      */ 
/*      */   public void dispose()
/*      */   {
/*      */   }
/*      */ 
/*      */   protected static Rectangle getSourceRegion(ImageReadParam paramImageReadParam, int paramInt1, int paramInt2)
/*      */   {
/* 2561 */     Rectangle localRectangle1 = new Rectangle(0, 0, paramInt1, paramInt2);
/* 2562 */     if (paramImageReadParam != null) {
/* 2563 */       Rectangle localRectangle2 = paramImageReadParam.getSourceRegion();
/* 2564 */       if (localRectangle2 != null) {
/* 2565 */         localRectangle1 = localRectangle1.intersection(localRectangle2);
/*      */       }
/*      */ 
/* 2568 */       int i = paramImageReadParam.getSubsamplingXOffset();
/* 2569 */       int j = paramImageReadParam.getSubsamplingYOffset();
/* 2570 */       localRectangle1.x += i;
/* 2571 */       localRectangle1.y += j;
/* 2572 */       localRectangle1.width -= i;
/* 2573 */       localRectangle1.height -= j;
/*      */     }
/*      */ 
/* 2576 */     return localRectangle1;
/*      */   }
/*      */ 
/*      */   protected static void computeRegions(ImageReadParam paramImageReadParam, int paramInt1, int paramInt2, BufferedImage paramBufferedImage, Rectangle paramRectangle1, Rectangle paramRectangle2)
/*      */   {
/* 2629 */     if (paramRectangle1 == null) {
/* 2630 */       throw new IllegalArgumentException("srcRegion == null!");
/*      */     }
/* 2632 */     if (paramRectangle2 == null) {
/* 2633 */       throw new IllegalArgumentException("destRegion == null!");
/*      */     }
/*      */ 
/* 2637 */     paramRectangle1.setBounds(0, 0, paramInt1, paramInt2);
/*      */ 
/* 2641 */     paramRectangle2.setBounds(0, 0, paramInt1, paramInt2);
/*      */ 
/* 2644 */     int i = 1;
/* 2645 */     int j = 1;
/* 2646 */     int k = 0;
/* 2647 */     int m = 0;
/* 2648 */     if (paramImageReadParam != null) {
/* 2649 */       Rectangle localRectangle1 = paramImageReadParam.getSourceRegion();
/* 2650 */       if (localRectangle1 != null) {
/* 2651 */         paramRectangle1.setBounds(paramRectangle1.intersection(localRectangle1));
/*      */       }
/* 2653 */       i = paramImageReadParam.getSourceXSubsampling();
/* 2654 */       j = paramImageReadParam.getSourceYSubsampling();
/* 2655 */       k = paramImageReadParam.getSubsamplingXOffset();
/* 2656 */       m = paramImageReadParam.getSubsamplingYOffset();
/* 2657 */       paramRectangle1.translate(k, m);
/* 2658 */       paramRectangle1.width -= k;
/* 2659 */       paramRectangle1.height -= m;
/* 2660 */       paramRectangle2.setLocation(paramImageReadParam.getDestinationOffset());
/*      */     }
/*      */ 
/* 2665 */     if (paramRectangle2.x < 0) {
/* 2666 */       n = -paramRectangle2.x * i;
/* 2667 */       paramRectangle1.x += n;
/* 2668 */       paramRectangle1.width -= n;
/* 2669 */       paramRectangle2.x = 0;
/*      */     }
/* 2671 */     if (paramRectangle2.y < 0) {
/* 2672 */       n = -paramRectangle2.y * j;
/* 2673 */       paramRectangle1.y += n;
/* 2674 */       paramRectangle1.height -= n;
/* 2675 */       paramRectangle2.y = 0;
/*      */     }
/*      */ 
/* 2679 */     int n = (paramRectangle1.width + i - 1) / i;
/* 2680 */     int i1 = (paramRectangle1.height + j - 1) / j;
/* 2681 */     paramRectangle2.width = n;
/* 2682 */     paramRectangle2.height = i1;
/*      */ 
/* 2686 */     if (paramBufferedImage != null) {
/* 2687 */       Rectangle localRectangle2 = new Rectangle(0, 0, paramBufferedImage.getWidth(), paramBufferedImage.getHeight());
/*      */ 
/* 2690 */       paramRectangle2.setBounds(paramRectangle2.intersection(localRectangle2));
/* 2691 */       if (paramRectangle2.isEmpty()) {
/* 2692 */         throw new IllegalArgumentException("Empty destination region!");
/*      */       }
/*      */ 
/* 2696 */       int i2 = paramRectangle2.x + n - paramBufferedImage.getWidth();
/* 2697 */       if (i2 > 0) {
/* 2698 */         paramRectangle1.width -= i2 * i;
/*      */       }
/* 2700 */       int i3 = paramRectangle2.y + i1 - paramBufferedImage.getHeight();
/* 2701 */       if (i3 > 0) {
/* 2702 */         paramRectangle1.height -= i3 * j;
/*      */       }
/*      */     }
/* 2705 */     if ((paramRectangle1.isEmpty()) || (paramRectangle2.isEmpty()))
/* 2706 */       throw new IllegalArgumentException("Empty region!");
/*      */   }
/*      */ 
/*      */   protected static void checkReadParamBandSettings(ImageReadParam paramImageReadParam, int paramInt1, int paramInt2)
/*      */   {
/* 2750 */     int[] arrayOfInt1 = null;
/* 2751 */     int[] arrayOfInt2 = null;
/* 2752 */     if (paramImageReadParam != null) {
/* 2753 */       arrayOfInt1 = paramImageReadParam.getSourceBands();
/* 2754 */       arrayOfInt2 = paramImageReadParam.getDestinationBands();
/*      */     }
/*      */ 
/* 2757 */     int i = arrayOfInt1 == null ? paramInt1 : arrayOfInt1.length;
/*      */ 
/* 2759 */     int j = arrayOfInt2 == null ? paramInt2 : arrayOfInt2.length;
/*      */ 
/* 2762 */     if (i != j)
/* 2763 */       throw new IllegalArgumentException("ImageReadParam num source & dest bands differ!");
/*      */     int k;
/* 2766 */     if (arrayOfInt1 != null) {
/* 2767 */       for (k = 0; k < arrayOfInt1.length; k++) {
/* 2768 */         if (arrayOfInt1[k] >= paramInt1) {
/* 2769 */           throw new IllegalArgumentException("ImageReadParam source bands contains a value >= the number of source bands!");
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 2774 */     if (arrayOfInt2 != null)
/* 2775 */       for (k = 0; k < arrayOfInt2.length; k++)
/* 2776 */         if (arrayOfInt2[k] >= paramInt2)
/* 2777 */           throw new IllegalArgumentException("ImageReadParam dest bands contains a value >= the number of dest bands!");
/*      */   }
/*      */ 
/*      */   protected static BufferedImage getDestination(ImageReadParam paramImageReadParam, Iterator<ImageTypeSpecifier> paramIterator, int paramInt1, int paramInt2)
/*      */     throws IIOException
/*      */   {
/* 2836 */     if ((paramIterator == null) || (!paramIterator.hasNext())) {
/* 2837 */       throw new IllegalArgumentException("imageTypes null or empty!");
/*      */     }
/* 2839 */     if (paramInt1 * paramInt2 > 2147483647L) {
/* 2840 */       throw new IllegalArgumentException("width*height > Integer.MAX_VALUE!");
/*      */     }
/*      */ 
/* 2844 */     BufferedImage localBufferedImage = null;
/* 2845 */     ImageTypeSpecifier localImageTypeSpecifier = null;
/*      */ 
/* 2848 */     if (paramImageReadParam != null)
/*      */     {
/* 2850 */       localBufferedImage = paramImageReadParam.getDestination();
/* 2851 */       if (localBufferedImage != null) {
/* 2852 */         return localBufferedImage;
/*      */       }
/*      */ 
/* 2856 */       localImageTypeSpecifier = paramImageReadParam.getDestinationType();
/*      */     }
/*      */ 
/* 2860 */     if (localImageTypeSpecifier == null) {
/* 2861 */       Object localObject1 = paramIterator.next();
/* 2862 */       if (!(localObject1 instanceof ImageTypeSpecifier)) {
/* 2863 */         throw new IllegalArgumentException("Non-ImageTypeSpecifier retrieved from imageTypes!");
/*      */       }
/*      */ 
/* 2866 */       localImageTypeSpecifier = (ImageTypeSpecifier)localObject1;
/*      */     } else {
/* 2868 */       int i = 0;
/* 2869 */       while (paramIterator.hasNext()) {
/* 2870 */         localObject2 = (ImageTypeSpecifier)paramIterator.next();
/*      */ 
/* 2872 */         if (((ImageTypeSpecifier)localObject2).equals(localImageTypeSpecifier)) {
/* 2873 */           i = 1;
/* 2874 */           break;
/*      */         }
/*      */       }
/*      */ 
/* 2878 */       if (i == 0) {
/* 2879 */         throw new IIOException("Destination type from ImageReadParam does not match!");
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2884 */     Rectangle localRectangle = new Rectangle(0, 0, 0, 0);
/* 2885 */     Object localObject2 = new Rectangle(0, 0, 0, 0);
/* 2886 */     computeRegions(paramImageReadParam, paramInt1, paramInt2, null, localRectangle, (Rectangle)localObject2);
/*      */ 
/* 2893 */     int j = ((Rectangle)localObject2).x + ((Rectangle)localObject2).width;
/* 2894 */     int k = ((Rectangle)localObject2).y + ((Rectangle)localObject2).height;
/*      */ 
/* 2896 */     return localImageTypeSpecifier.createBufferedImage(j, k);
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.imageio.ImageReader
 * JD-Core Version:    0.6.2
 */