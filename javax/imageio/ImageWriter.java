/*      */ package javax.imageio;
/*      */ 
/*      */ import java.awt.Dimension;
/*      */ import java.awt.Rectangle;
/*      */ import java.awt.image.BufferedImage;
/*      */ import java.awt.image.Raster;
/*      */ import java.awt.image.RenderedImage;
/*      */ import java.io.IOException;
/*      */ import java.security.AccessController;
/*      */ import java.security.PrivilegedAction;
/*      */ import java.util.List;
/*      */ import java.util.Locale;
/*      */ import java.util.MissingResourceException;
/*      */ import java.util.ResourceBundle;
/*      */ import javax.imageio.event.IIOWriteProgressListener;
/*      */ import javax.imageio.event.IIOWriteWarningListener;
/*      */ import javax.imageio.metadata.IIOMetadata;
/*      */ import javax.imageio.spi.ImageWriterSpi;
/*      */ 
/*      */ public abstract class ImageWriter
/*      */   implements ImageTranscoder
/*      */ {
/*   71 */   protected ImageWriterSpi originatingProvider = null;
/*      */ 
/*   79 */   protected Object output = null;
/*      */ 
/*   87 */   protected Locale[] availableLocales = null;
/*      */ 
/*   94 */   protected Locale locale = null;
/*      */ 
/*  102 */   protected List<IIOWriteWarningListener> warningListeners = null;
/*      */ 
/*  110 */   protected List<Locale> warningLocales = null;
/*      */ 
/*  118 */   protected List<IIOWriteProgressListener> progressListeners = null;
/*      */ 
/*  124 */   private boolean abortFlag = false;
/*      */ 
/*      */   protected ImageWriter(ImageWriterSpi paramImageWriterSpi)
/*      */   {
/*  141 */     this.originatingProvider = paramImageWriterSpi;
/*      */   }
/*      */ 
/*      */   public ImageWriterSpi getOriginatingProvider()
/*      */   {
/*  157 */     return this.originatingProvider;
/*      */   }
/*      */ 
/*      */   public void setOutput(Object paramObject)
/*      */   {
/*  207 */     if (paramObject != null) {
/*  208 */       ImageWriterSpi localImageWriterSpi = getOriginatingProvider();
/*  209 */       if (localImageWriterSpi != null) {
/*  210 */         Class[] arrayOfClass = localImageWriterSpi.getOutputTypes();
/*  211 */         int i = 0;
/*  212 */         for (int j = 0; j < arrayOfClass.length; j++) {
/*  213 */           if (arrayOfClass[j].isInstance(paramObject)) {
/*  214 */             i = 1;
/*  215 */             break;
/*      */           }
/*      */         }
/*  218 */         if (i == 0) {
/*  219 */           throw new IllegalArgumentException("Illegal output type!");
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  224 */     this.output = paramObject;
/*      */   }
/*      */ 
/*      */   public Object getOutput()
/*      */   {
/*  242 */     return this.output;
/*      */   }
/*      */ 
/*      */   public Locale[] getAvailableLocales()
/*      */   {
/*  261 */     return this.availableLocales == null ? null : (Locale[])this.availableLocales.clone();
/*      */   }
/*      */ 
/*      */   public void setLocale(Locale paramLocale)
/*      */   {
/*  288 */     if (paramLocale != null) {
/*  289 */       Locale[] arrayOfLocale = getAvailableLocales();
/*  290 */       int i = 0;
/*  291 */       if (arrayOfLocale != null) {
/*  292 */         for (int j = 0; j < arrayOfLocale.length; j++) {
/*  293 */           if (paramLocale.equals(arrayOfLocale[j])) {
/*  294 */             i = 1;
/*  295 */             break;
/*      */           }
/*      */         }
/*      */       }
/*  299 */       if (i == 0) {
/*  300 */         throw new IllegalArgumentException("Invalid locale!");
/*      */       }
/*      */     }
/*  303 */     this.locale = paramLocale;
/*      */   }
/*      */ 
/*      */   public Locale getLocale()
/*      */   {
/*  318 */     return this.locale;
/*      */   }
/*      */ 
/*      */   public ImageWriteParam getDefaultWriteParam()
/*      */   {
/*  347 */     return new ImageWriteParam(getLocale());
/*      */   }
/*      */ 
/*      */   public abstract IIOMetadata getDefaultStreamMetadata(ImageWriteParam paramImageWriteParam);
/*      */ 
/*      */   public abstract IIOMetadata getDefaultImageMetadata(ImageTypeSpecifier paramImageTypeSpecifier, ImageWriteParam paramImageWriteParam);
/*      */ 
/*      */   public abstract IIOMetadata convertStreamMetadata(IIOMetadata paramIIOMetadata, ImageWriteParam paramImageWriteParam);
/*      */ 
/*      */   public abstract IIOMetadata convertImageMetadata(IIOMetadata paramIIOMetadata, ImageTypeSpecifier paramImageTypeSpecifier, ImageWriteParam paramImageWriteParam);
/*      */ 
/*      */   public int getNumThumbnailsSupported(ImageTypeSpecifier paramImageTypeSpecifier, ImageWriteParam paramImageWriteParam, IIOMetadata paramIIOMetadata1, IIOMetadata paramIIOMetadata2)
/*      */   {
/*  459 */     return 0;
/*      */   }
/*      */ 
/*      */   public Dimension[] getPreferredThumbnailSizes(ImageTypeSpecifier paramImageTypeSpecifier, ImageWriteParam paramImageWriteParam, IIOMetadata paramIIOMetadata1, IIOMetadata paramIIOMetadata2)
/*      */   {
/*  503 */     return null;
/*      */   }
/*      */ 
/*      */   public boolean canWriteRasters()
/*      */   {
/*  521 */     return false;
/*      */   }
/*      */ 
/*      */   public abstract void write(IIOMetadata paramIIOMetadata, IIOImage paramIIOImage, ImageWriteParam paramImageWriteParam)
/*      */     throws IOException;
/*      */ 
/*      */   public void write(IIOImage paramIIOImage)
/*      */     throws IOException
/*      */   {
/*  597 */     write(null, paramIIOImage, null);
/*      */   }
/*      */ 
/*      */   public void write(RenderedImage paramRenderedImage)
/*      */     throws IOException
/*      */   {
/*  615 */     write(null, new IIOImage(paramRenderedImage, null, null), null);
/*      */   }
/*      */ 
/*      */   private void unsupported()
/*      */   {
/*  621 */     if (getOutput() == null) {
/*  622 */       throw new IllegalStateException("getOutput() == null!");
/*      */     }
/*  624 */     throw new UnsupportedOperationException("Unsupported write variant!");
/*      */   }
/*      */ 
/*      */   public boolean canWriteSequence()
/*      */   {
/*  643 */     return false;
/*      */   }
/*      */ 
/*      */   public void prepareWriteSequence(IIOMetadata paramIIOMetadata)
/*      */     throws IOException
/*      */   {
/*  685 */     unsupported();
/*      */   }
/*      */ 
/*      */   public void writeToSequence(IIOImage paramIIOImage, ImageWriteParam paramImageWriteParam)
/*      */     throws IOException
/*      */   {
/*  750 */     unsupported();
/*      */   }
/*      */ 
/*      */   public void endWriteSequence()
/*      */     throws IOException
/*      */   {
/*  779 */     unsupported();
/*      */   }
/*      */ 
/*      */   public boolean canReplaceStreamMetadata()
/*      */     throws IOException
/*      */   {
/*  800 */     if (getOutput() == null) {
/*  801 */       throw new IllegalStateException("getOutput() == null!");
/*      */     }
/*  803 */     return false;
/*      */   }
/*      */ 
/*      */   public void replaceStreamMetadata(IIOMetadata paramIIOMetadata)
/*      */     throws IOException
/*      */   {
/*  835 */     unsupported();
/*      */   }
/*      */ 
/*      */   public boolean canReplaceImageMetadata(int paramInt)
/*      */     throws IOException
/*      */   {
/*  871 */     if (getOutput() == null) {
/*  872 */       throw new IllegalStateException("getOutput() == null!");
/*      */     }
/*  874 */     return false;
/*      */   }
/*      */ 
/*      */   public void replaceImageMetadata(int paramInt, IIOMetadata paramIIOMetadata)
/*      */     throws IOException
/*      */   {
/*  906 */     unsupported();
/*      */   }
/*      */ 
/*      */   public boolean canInsertImage(int paramInt)
/*      */     throws IOException
/*      */   {
/*  942 */     if (getOutput() == null) {
/*  943 */       throw new IllegalStateException("getOutput() == null!");
/*      */     }
/*  945 */     return false;
/*      */   }
/*      */ 
/*      */   public void writeInsert(int paramInt, IIOImage paramIIOImage, ImageWriteParam paramImageWriteParam)
/*      */     throws IOException
/*      */   {
/*  999 */     unsupported();
/*      */   }
/*      */ 
/*      */   public boolean canRemoveImage(int paramInt)
/*      */     throws IOException
/*      */   {
/* 1033 */     if (getOutput() == null) {
/* 1034 */       throw new IllegalStateException("getOutput() == null!");
/*      */     }
/* 1036 */     return false;
/*      */   }
/*      */ 
/*      */   public void removeImage(int paramInt)
/*      */     throws IOException
/*      */   {
/* 1065 */     unsupported();
/*      */   }
/*      */ 
/*      */   public boolean canWriteEmpty()
/*      */     throws IOException
/*      */   {
/* 1093 */     if (getOutput() == null) {
/* 1094 */       throw new IllegalStateException("getOutput() == null!");
/*      */     }
/* 1096 */     return false;
/*      */   }
/*      */ 
/*      */   public void prepareWriteEmpty(IIOMetadata paramIIOMetadata1, ImageTypeSpecifier paramImageTypeSpecifier, int paramInt1, int paramInt2, IIOMetadata paramIIOMetadata2, List<? extends BufferedImage> paramList, ImageWriteParam paramImageWriteParam)
/*      */     throws IOException
/*      */   {
/* 1173 */     unsupported();
/*      */   }
/*      */ 
/*      */   public void endWriteEmpty()
/*      */     throws IOException
/*      */   {
/* 1205 */     if (getOutput() == null) {
/* 1206 */       throw new IllegalStateException("getOutput() == null!");
/*      */     }
/* 1208 */     throw new IllegalStateException("No call to prepareWriteEmpty!");
/*      */   }
/*      */ 
/*      */   public boolean canInsertEmpty(int paramInt)
/*      */     throws IOException
/*      */   {
/* 1245 */     if (getOutput() == null) {
/* 1246 */       throw new IllegalStateException("getOutput() == null!");
/*      */     }
/* 1248 */     return false;
/*      */   }
/*      */ 
/*      */   public void prepareInsertEmpty(int paramInt1, ImageTypeSpecifier paramImageTypeSpecifier, int paramInt2, int paramInt3, IIOMetadata paramIIOMetadata, List<? extends BufferedImage> paramList, ImageWriteParam paramImageWriteParam)
/*      */     throws IOException
/*      */   {
/* 1333 */     unsupported();
/*      */   }
/*      */ 
/*      */   public void endInsertEmpty()
/*      */     throws IOException
/*      */   {
/* 1362 */     unsupported();
/*      */   }
/*      */ 
/*      */   public boolean canReplacePixels(int paramInt)
/*      */     throws IOException
/*      */   {
/* 1395 */     if (getOutput() == null) {
/* 1396 */       throw new IllegalStateException("getOutput() == null!");
/*      */     }
/* 1398 */     return false;
/*      */   }
/*      */ 
/*      */   public void prepareReplacePixels(int paramInt, Rectangle paramRectangle)
/*      */     throws IOException
/*      */   {
/* 1438 */     unsupported();
/*      */   }
/*      */ 
/*      */   public void replacePixels(RenderedImage paramRenderedImage, ImageWriteParam paramImageWriteParam)
/*      */     throws IOException
/*      */   {
/* 1496 */     unsupported();
/*      */   }
/*      */ 
/*      */   public void replacePixels(Raster paramRaster, ImageWriteParam paramImageWriteParam)
/*      */     throws IOException
/*      */   {
/* 1557 */     unsupported();
/*      */   }
/*      */ 
/*      */   public void endReplacePixels()
/*      */     throws IOException
/*      */   {
/* 1583 */     unsupported();
/*      */   }
/*      */ 
/*      */   public synchronized void abort()
/*      */   {
/* 1597 */     this.abortFlag = true;
/*      */   }
/*      */ 
/*      */   protected synchronized boolean abortRequested()
/*      */   {
/* 1612 */     return this.abortFlag;
/*      */   }
/*      */ 
/*      */   protected synchronized void clearAbortRequest()
/*      */   {
/* 1624 */     this.abortFlag = false;
/*      */   }
/*      */ 
/*      */   public void addIIOWriteWarningListener(IIOWriteWarningListener paramIIOWriteWarningListener)
/*      */   {
/* 1644 */     if (paramIIOWriteWarningListener == null) {
/* 1645 */       return;
/*      */     }
/* 1647 */     this.warningListeners = ImageReader.addToList(this.warningListeners, paramIIOWriteWarningListener);
/* 1648 */     this.warningLocales = ImageReader.addToList(this.warningLocales, getLocale());
/*      */   }
/*      */ 
/*      */   public void removeIIOWriteWarningListener(IIOWriteWarningListener paramIIOWriteWarningListener)
/*      */   {
/* 1665 */     if ((paramIIOWriteWarningListener == null) || (this.warningListeners == null)) {
/* 1666 */       return;
/*      */     }
/* 1668 */     int i = this.warningListeners.indexOf(paramIIOWriteWarningListener);
/* 1669 */     if (i != -1) {
/* 1670 */       this.warningListeners.remove(i);
/* 1671 */       this.warningLocales.remove(i);
/* 1672 */       if (this.warningListeners.size() == 0) {
/* 1673 */         this.warningListeners = null;
/* 1674 */         this.warningLocales = null;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public void removeAllIIOWriteWarningListeners()
/*      */   {
/* 1688 */     this.warningListeners = null;
/* 1689 */     this.warningLocales = null;
/*      */   }
/*      */ 
/*      */   public void addIIOWriteProgressListener(IIOWriteProgressListener paramIIOWriteProgressListener)
/*      */   {
/* 1705 */     if (paramIIOWriteProgressListener == null) {
/* 1706 */       return;
/*      */     }
/* 1708 */     this.progressListeners = ImageReader.addToList(this.progressListeners, paramIIOWriteProgressListener);
/*      */   }
/*      */ 
/*      */   public void removeIIOWriteProgressListener(IIOWriteProgressListener paramIIOWriteProgressListener)
/*      */   {
/* 1725 */     if ((paramIIOWriteProgressListener == null) || (this.progressListeners == null)) {
/* 1726 */       return;
/*      */     }
/* 1728 */     this.progressListeners = ImageReader.removeFromList(this.progressListeners, paramIIOWriteProgressListener);
/*      */   }
/*      */ 
/*      */   public void removeAllIIOWriteProgressListeners()
/*      */   {
/* 1741 */     this.progressListeners = null;
/*      */   }
/*      */ 
/*      */   protected void processImageStarted(int paramInt)
/*      */   {
/* 1753 */     if (this.progressListeners == null) {
/* 1754 */       return;
/*      */     }
/* 1756 */     int i = this.progressListeners.size();
/* 1757 */     for (int j = 0; j < i; j++) {
/* 1758 */       IIOWriteProgressListener localIIOWriteProgressListener = (IIOWriteProgressListener)this.progressListeners.get(j);
/*      */ 
/* 1760 */       localIIOWriteProgressListener.imageStarted(this, paramInt);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void processImageProgress(float paramFloat)
/*      */   {
/* 1774 */     if (this.progressListeners == null) {
/* 1775 */       return;
/*      */     }
/* 1777 */     int i = this.progressListeners.size();
/* 1778 */     for (int j = 0; j < i; j++) {
/* 1779 */       IIOWriteProgressListener localIIOWriteProgressListener = (IIOWriteProgressListener)this.progressListeners.get(j);
/*      */ 
/* 1781 */       localIIOWriteProgressListener.imageProgress(this, paramFloat);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void processImageComplete()
/*      */   {
/* 1792 */     if (this.progressListeners == null) {
/* 1793 */       return;
/*      */     }
/* 1795 */     int i = this.progressListeners.size();
/* 1796 */     for (int j = 0; j < i; j++) {
/* 1797 */       IIOWriteProgressListener localIIOWriteProgressListener = (IIOWriteProgressListener)this.progressListeners.get(j);
/*      */ 
/* 1799 */       localIIOWriteProgressListener.imageComplete(this);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void processThumbnailStarted(int paramInt1, int paramInt2)
/*      */   {
/* 1815 */     if (this.progressListeners == null) {
/* 1816 */       return;
/*      */     }
/* 1818 */     int i = this.progressListeners.size();
/* 1819 */     for (int j = 0; j < i; j++) {
/* 1820 */       IIOWriteProgressListener localIIOWriteProgressListener = (IIOWriteProgressListener)this.progressListeners.get(j);
/*      */ 
/* 1822 */       localIIOWriteProgressListener.thumbnailStarted(this, paramInt1, paramInt2);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void processThumbnailProgress(float paramFloat)
/*      */   {
/* 1836 */     if (this.progressListeners == null) {
/* 1837 */       return;
/*      */     }
/* 1839 */     int i = this.progressListeners.size();
/* 1840 */     for (int j = 0; j < i; j++) {
/* 1841 */       IIOWriteProgressListener localIIOWriteProgressListener = (IIOWriteProgressListener)this.progressListeners.get(j);
/*      */ 
/* 1843 */       localIIOWriteProgressListener.thumbnailProgress(this, paramFloat);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void processThumbnailComplete()
/*      */   {
/* 1854 */     if (this.progressListeners == null) {
/* 1855 */       return;
/*      */     }
/* 1857 */     int i = this.progressListeners.size();
/* 1858 */     for (int j = 0; j < i; j++) {
/* 1859 */       IIOWriteProgressListener localIIOWriteProgressListener = (IIOWriteProgressListener)this.progressListeners.get(j);
/*      */ 
/* 1861 */       localIIOWriteProgressListener.thumbnailComplete(this);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void processWriteAborted()
/*      */   {
/* 1872 */     if (this.progressListeners == null) {
/* 1873 */       return;
/*      */     }
/* 1875 */     int i = this.progressListeners.size();
/* 1876 */     for (int j = 0; j < i; j++) {
/* 1877 */       IIOWriteProgressListener localIIOWriteProgressListener = (IIOWriteProgressListener)this.progressListeners.get(j);
/*      */ 
/* 1879 */       localIIOWriteProgressListener.writeAborted(this);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void processWarningOccurred(int paramInt, String paramString)
/*      */   {
/* 1898 */     if (this.warningListeners == null) {
/* 1899 */       return;
/*      */     }
/* 1901 */     if (paramString == null) {
/* 1902 */       throw new IllegalArgumentException("warning == null!");
/*      */     }
/* 1904 */     int i = this.warningListeners.size();
/* 1905 */     for (int j = 0; j < i; j++) {
/* 1906 */       IIOWriteWarningListener localIIOWriteWarningListener = (IIOWriteWarningListener)this.warningListeners.get(j);
/*      */ 
/* 1909 */       localIIOWriteWarningListener.warningOccurred(this, paramInt, paramString);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void processWarningOccurred(int paramInt, String paramString1, String paramString2)
/*      */   {
/* 1943 */     if (this.warningListeners == null) {
/* 1944 */       return;
/*      */     }
/* 1946 */     if (paramString1 == null) {
/* 1947 */       throw new IllegalArgumentException("baseName == null!");
/*      */     }
/* 1949 */     if (paramString2 == null) {
/* 1950 */       throw new IllegalArgumentException("keyword == null!");
/*      */     }
/* 1952 */     int i = this.warningListeners.size();
/* 1953 */     for (int j = 0; j < i; j++) {
/* 1954 */       IIOWriteWarningListener localIIOWriteWarningListener = (IIOWriteWarningListener)this.warningListeners.get(j);
/*      */ 
/* 1956 */       Locale localLocale = (Locale)this.warningLocales.get(j);
/* 1957 */       if (localLocale == null) {
/* 1958 */         localLocale = Locale.getDefault();
/*      */       }
/*      */ 
/* 1969 */       ClassLoader localClassLoader = (ClassLoader)AccessController.doPrivileged(new PrivilegedAction()
/*      */       {
/*      */         public Object run()
/*      */         {
/* 1973 */           return Thread.currentThread().getContextClassLoader();
/*      */         }
/*      */       });
/* 1977 */       ResourceBundle localResourceBundle = null;
/*      */       try {
/* 1979 */         localResourceBundle = ResourceBundle.getBundle(paramString1, localLocale, localClassLoader);
/*      */       } catch (MissingResourceException localMissingResourceException1) {
/*      */         try {
/* 1982 */           localResourceBundle = ResourceBundle.getBundle(paramString1, localLocale);
/*      */         } catch (MissingResourceException localMissingResourceException2) {
/* 1984 */           throw new IllegalArgumentException("Bundle not found!");
/*      */         }
/*      */       }
/*      */ 
/* 1988 */       String str = null;
/*      */       try {
/* 1990 */         str = localResourceBundle.getString(paramString2);
/*      */       } catch (ClassCastException localClassCastException) {
/* 1992 */         throw new IllegalArgumentException("Resource is not a String!");
/*      */       } catch (MissingResourceException localMissingResourceException3) {
/* 1994 */         throw new IllegalArgumentException("Resource is missing!");
/*      */       }
/*      */ 
/* 1997 */       localIIOWriteWarningListener.warningOccurred(this, paramInt, str);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void reset()
/*      */   {
/* 2013 */     setOutput(null);
/* 2014 */     setLocale(null);
/* 2015 */     removeAllIIOWriteWarningListeners();
/* 2016 */     removeAllIIOWriteProgressListeners();
/* 2017 */     clearAbortRequest();
/*      */   }
/*      */ 
/*      */   public void dispose()
/*      */   {
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.imageio.ImageWriter
 * JD-Core Version:    0.6.2
 */