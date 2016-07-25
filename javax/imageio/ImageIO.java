/*      */ package javax.imageio;
/*      */ 
/*      */ import java.awt.image.BufferedImage;
/*      */ import java.awt.image.RenderedImage;
/*      */ import java.io.File;
/*      */ import java.io.FilePermission;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.OutputStream;
/*      */ import java.lang.reflect.Method;
/*      */ import java.net.URL;
/*      */ import java.security.AccessController;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collections;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import javax.imageio.spi.IIORegistry;
/*      */ import javax.imageio.spi.ImageInputStreamSpi;
/*      */ import javax.imageio.spi.ImageOutputStreamSpi;
/*      */ import javax.imageio.spi.ImageReaderSpi;
/*      */ import javax.imageio.spi.ImageReaderWriterSpi;
/*      */ import javax.imageio.spi.ImageTranscoderSpi;
/*      */ import javax.imageio.spi.ImageWriterSpi;
/*      */ import javax.imageio.spi.ServiceRegistry.Filter;
/*      */ import javax.imageio.stream.ImageInputStream;
/*      */ import javax.imageio.stream.ImageOutputStream;
/*      */ import sun.awt.AppContext;
/*      */ import sun.security.action.GetPropertyAction;
/*      */ 
/*      */ public final class ImageIO
/*      */ {
/*   65 */   private static final IIORegistry theRegistry = IIORegistry.getDefaultInstance();
/*      */   private static Method readerFormatNamesMethod;
/*      */   private static Method readerFileSuffixesMethod;
/*      */   private static Method readerMIMETypesMethod;
/*      */   private static Method writerFormatNamesMethod;
/*      */   private static Method writerFileSuffixesMethod;
/*      */   private static Method writerMIMETypesMethod;
/*      */ 
/*      */   public static void scanForPlugins()
/*      */   {
/*  110 */     theRegistry.registerApplicationClasspathSpis();
/*      */   }
/*      */ 
/*      */   private static synchronized CacheInfo getCacheInfo()
/*      */   {
/*  157 */     AppContext localAppContext = AppContext.getAppContext();
/*  158 */     CacheInfo localCacheInfo = (CacheInfo)localAppContext.get(CacheInfo.class);
/*  159 */     if (localCacheInfo == null) {
/*  160 */       localCacheInfo = new CacheInfo();
/*  161 */       localAppContext.put(CacheInfo.class, localCacheInfo);
/*      */     }
/*  163 */     return localCacheInfo;
/*      */   }
/*      */ 
/*      */   private static String getTempDir()
/*      */   {
/*  171 */     GetPropertyAction localGetPropertyAction = new GetPropertyAction("java.io.tmpdir");
/*  172 */     return (String)AccessController.doPrivileged(localGetPropertyAction);
/*      */   }
/*      */ 
/*      */   private static boolean hasCachePermission()
/*      */   {
/*  183 */     Boolean localBoolean = getCacheInfo().getHasPermission();
/*      */ 
/*  185 */     if (localBoolean != null)
/*  186 */       return localBoolean.booleanValue();
/*      */     try
/*      */     {
/*  189 */       SecurityManager localSecurityManager = System.getSecurityManager();
/*  190 */       if (localSecurityManager != null) {
/*  191 */         File localFile = getCacheDirectory();
/*      */         String str1;
/*  194 */         if (localFile != null) {
/*  195 */           str1 = localFile.getPath();
/*      */         } else {
/*  197 */           str1 = getTempDir();
/*      */ 
/*  199 */           if ((str1 == null) || (str1.isEmpty())) {
/*  200 */             getCacheInfo().setHasPermission(Boolean.FALSE);
/*  201 */             return false;
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*  208 */         String str2 = str1;
/*  209 */         if (!str2.endsWith(File.separator)) {
/*  210 */           str2 = str2 + File.separator;
/*      */         }
/*  212 */         str2 = str2 + "*";
/*      */ 
/*  214 */         localSecurityManager.checkPermission(new FilePermission(str2, "read, write, delete"));
/*      */       }
/*      */     } catch (SecurityException localSecurityException) {
/*  217 */       getCacheInfo().setHasPermission(Boolean.FALSE);
/*  218 */       return false;
/*      */     }
/*      */ 
/*  221 */     getCacheInfo().setHasPermission(Boolean.TRUE);
/*  222 */     return true;
/*      */   }
/*      */ 
/*      */   public static void setUseCache(boolean paramBoolean)
/*      */   {
/*  253 */     getCacheInfo().setUseCache(paramBoolean);
/*      */   }
/*      */ 
/*      */   public static boolean getUseCache()
/*      */   {
/*  267 */     return getCacheInfo().getUseCache();
/*      */   }
/*      */ 
/*      */   public static void setCacheDirectory(File paramFile)
/*      */   {
/*  288 */     if ((paramFile != null) && (!paramFile.isDirectory())) {
/*  289 */       throw new IllegalArgumentException("Not a directory!");
/*      */     }
/*  291 */     getCacheInfo().setCacheDirectory(paramFile);
/*  292 */     getCacheInfo().setHasPermission(null);
/*      */   }
/*      */ 
/*      */   public static File getCacheDirectory()
/*      */   {
/*  307 */     return getCacheInfo().getCacheDirectory();
/*      */   }
/*      */ 
/*      */   public static ImageInputStream createImageInputStream(Object paramObject)
/*      */     throws IOException
/*      */   {
/*  338 */     if (paramObject == null) {
/*  339 */       throw new IllegalArgumentException("input == null!");
/*      */     }
/*      */ 
/*      */     Iterator localIterator;
/*      */     try
/*      */     {
/*  345 */       localIterator = theRegistry.getServiceProviders(ImageInputStreamSpi.class, true);
/*      */     }
/*      */     catch (IllegalArgumentException localIllegalArgumentException) {
/*  348 */       return null;
/*      */     }
/*      */ 
/*  351 */     boolean bool = (getUseCache()) && (hasCachePermission());
/*      */ 
/*  353 */     while (localIterator.hasNext()) {
/*  354 */       ImageInputStreamSpi localImageInputStreamSpi = (ImageInputStreamSpi)localIterator.next();
/*  355 */       if (localImageInputStreamSpi.getInputClass().isInstance(paramObject)) {
/*      */         try {
/*  357 */           return localImageInputStreamSpi.createInputStreamInstance(paramObject, bool, getCacheDirectory());
/*      */         }
/*      */         catch (IOException localIOException)
/*      */         {
/*  361 */           throw new IIOException("Can't create cache file!", localIOException);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  366 */     return null;
/*      */   }
/*      */ 
/*      */   public static ImageOutputStream createImageOutputStream(Object paramObject)
/*      */     throws IOException
/*      */   {
/*  400 */     if (paramObject == null) {
/*  401 */       throw new IllegalArgumentException("output == null!");
/*      */     }
/*      */ 
/*      */     Iterator localIterator;
/*      */     try
/*      */     {
/*  407 */       localIterator = theRegistry.getServiceProviders(ImageOutputStreamSpi.class, true);
/*      */     }
/*      */     catch (IllegalArgumentException localIllegalArgumentException) {
/*  410 */       return null;
/*      */     }
/*      */ 
/*  413 */     boolean bool = (getUseCache()) && (hasCachePermission());
/*      */ 
/*  415 */     while (localIterator.hasNext()) {
/*  416 */       ImageOutputStreamSpi localImageOutputStreamSpi = (ImageOutputStreamSpi)localIterator.next();
/*  417 */       if (localImageOutputStreamSpi.getOutputClass().isInstance(paramObject)) {
/*      */         try {
/*  419 */           return localImageOutputStreamSpi.createOutputStreamInstance(paramObject, bool, getCacheDirectory());
/*      */         }
/*      */         catch (IOException localIOException)
/*      */         {
/*  423 */           throw new IIOException("Can't create cache file!", localIOException);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  428 */     return null;
/*      */   }
/*      */ 
/*      */   private static <S extends ImageReaderWriterSpi> String[] getReaderWriterInfo(Class<S> paramClass, SpiInfo paramSpiInfo)
/*      */   {
/*      */     Iterator localIterator;
/*      */     try
/*      */     {
/*  460 */       localIterator = theRegistry.getServiceProviders(paramClass, true);
/*      */     } catch (IllegalArgumentException localIllegalArgumentException) {
/*  462 */       return new String[0];
/*      */     }
/*      */ 
/*  465 */     HashSet localHashSet = new HashSet();
/*  466 */     while (localIterator.hasNext()) {
/*  467 */       ImageReaderWriterSpi localImageReaderWriterSpi = (ImageReaderWriterSpi)localIterator.next();
/*  468 */       Collections.addAll(localHashSet, paramSpiInfo.info(localImageReaderWriterSpi));
/*      */     }
/*      */ 
/*  471 */     return (String[])localHashSet.toArray(new String[localHashSet.size()]);
/*      */   }
/*      */ 
/*      */   public static String[] getReaderFormatNames()
/*      */   {
/*  484 */     return getReaderWriterInfo(ImageReaderSpi.class, SpiInfo.FORMAT_NAMES);
/*      */   }
/*      */ 
/*      */   public static String[] getReaderMIMETypes()
/*      */   {
/*  496 */     return getReaderWriterInfo(ImageReaderSpi.class, SpiInfo.MIME_TYPES);
/*      */   }
/*      */ 
/*      */   public static String[] getReaderFileSuffixes()
/*      */   {
/*  509 */     return getReaderWriterInfo(ImageReaderSpi.class, SpiInfo.FILE_SUFFIXES);
/*      */   }
/*      */ 
/*      */   public static Iterator<ImageReader> getImageReaders(Object paramObject)
/*      */   {
/*  640 */     if (paramObject == null) {
/*  641 */       throw new IllegalArgumentException("input == null!");
/*      */     }
/*      */     Iterator localIterator;
/*      */     try
/*      */     {
/*  646 */       localIterator = theRegistry.getServiceProviders(ImageReaderSpi.class, new CanDecodeInputFilter(paramObject), true);
/*      */     }
/*      */     catch (IllegalArgumentException localIllegalArgumentException)
/*      */     {
/*  650 */       return Collections.emptyIterator();
/*      */     }
/*      */ 
/*  653 */     return new ImageReaderIterator(localIterator);
/*      */   }
/*      */ 
/*      */   public static Iterator<ImageReader> getImageReadersByFormatName(String paramString)
/*      */   {
/*  702 */     if (paramString == null) {
/*  703 */       throw new IllegalArgumentException("formatName == null!");
/*      */     }
/*      */     Iterator localIterator;
/*      */     try
/*      */     {
/*  708 */       localIterator = theRegistry.getServiceProviders(ImageReaderSpi.class, new ContainsFilter(readerFormatNamesMethod, paramString), true);
/*      */     }
/*      */     catch (IllegalArgumentException localIllegalArgumentException)
/*      */     {
/*  713 */       return Collections.emptyIterator();
/*      */     }
/*  715 */     return new ImageReaderIterator(localIterator);
/*      */   }
/*      */ 
/*      */   public static Iterator<ImageReader> getImageReadersBySuffix(String paramString)
/*      */   {
/*  737 */     if (paramString == null) {
/*  738 */       throw new IllegalArgumentException("fileSuffix == null!");
/*      */     }
/*      */     Iterator localIterator;
/*      */     try
/*      */     {
/*  743 */       localIterator = theRegistry.getServiceProviders(ImageReaderSpi.class, new ContainsFilter(readerFileSuffixesMethod, paramString), true);
/*      */     }
/*      */     catch (IllegalArgumentException localIllegalArgumentException)
/*      */     {
/*  748 */       return Collections.emptyIterator();
/*      */     }
/*  750 */     return new ImageReaderIterator(localIterator);
/*      */   }
/*      */ 
/*      */   public static Iterator<ImageReader> getImageReadersByMIMEType(String paramString)
/*      */   {
/*  772 */     if (paramString == null) {
/*  773 */       throw new IllegalArgumentException("MIMEType == null!");
/*      */     }
/*      */     Iterator localIterator;
/*      */     try
/*      */     {
/*  778 */       localIterator = theRegistry.getServiceProviders(ImageReaderSpi.class, new ContainsFilter(readerMIMETypesMethod, paramString), true);
/*      */     }
/*      */     catch (IllegalArgumentException localIllegalArgumentException)
/*      */     {
/*  783 */       return Collections.emptyIterator();
/*      */     }
/*  785 */     return new ImageReaderIterator(localIterator);
/*      */   }
/*      */ 
/*      */   public static String[] getWriterFormatNames()
/*      */   {
/*  798 */     return getReaderWriterInfo(ImageWriterSpi.class, SpiInfo.FORMAT_NAMES);
/*      */   }
/*      */ 
/*      */   public static String[] getWriterMIMETypes()
/*      */   {
/*  810 */     return getReaderWriterInfo(ImageWriterSpi.class, SpiInfo.MIME_TYPES);
/*      */   }
/*      */ 
/*      */   public static String[] getWriterFileSuffixes()
/*      */   {
/*  823 */     return getReaderWriterInfo(ImageWriterSpi.class, SpiInfo.FILE_SUFFIXES);
/*      */   }
/*      */ 
/*      */   private static boolean contains(String[] paramArrayOfString, String paramString)
/*      */   {
/*  857 */     for (int i = 0; i < paramArrayOfString.length; i++) {
/*  858 */       if (paramString.equalsIgnoreCase(paramArrayOfString[i])) {
/*  859 */         return true;
/*      */       }
/*      */     }
/*      */ 
/*  863 */     return false;
/*      */   }
/*      */ 
/*      */   public static Iterator<ImageWriter> getImageWritersByFormatName(String paramString)
/*      */   {
/*  885 */     if (paramString == null) {
/*  886 */       throw new IllegalArgumentException("formatName == null!");
/*      */     }
/*      */     Iterator localIterator;
/*      */     try
/*      */     {
/*  891 */       localIterator = theRegistry.getServiceProviders(ImageWriterSpi.class, new ContainsFilter(writerFormatNamesMethod, paramString), true);
/*      */     }
/*      */     catch (IllegalArgumentException localIllegalArgumentException)
/*      */     {
/*  896 */       return Collections.emptyIterator();
/*      */     }
/*  898 */     return new ImageWriterIterator(localIterator);
/*      */   }
/*      */ 
/*      */   public static Iterator<ImageWriter> getImageWritersBySuffix(String paramString)
/*      */   {
/*  919 */     if (paramString == null) {
/*  920 */       throw new IllegalArgumentException("fileSuffix == null!");
/*      */     }
/*      */     Iterator localIterator;
/*      */     try
/*      */     {
/*  925 */       localIterator = theRegistry.getServiceProviders(ImageWriterSpi.class, new ContainsFilter(writerFileSuffixesMethod, paramString), true);
/*      */     }
/*      */     catch (IllegalArgumentException localIllegalArgumentException)
/*      */     {
/*  930 */       return Collections.emptyIterator();
/*      */     }
/*  932 */     return new ImageWriterIterator(localIterator);
/*      */   }
/*      */ 
/*      */   public static Iterator<ImageWriter> getImageWritersByMIMEType(String paramString)
/*      */   {
/*  953 */     if (paramString == null) {
/*  954 */       throw new IllegalArgumentException("MIMEType == null!");
/*      */     }
/*      */     Iterator localIterator;
/*      */     try
/*      */     {
/*  959 */       localIterator = theRegistry.getServiceProviders(ImageWriterSpi.class, new ContainsFilter(writerMIMETypesMethod, paramString), true);
/*      */     }
/*      */     catch (IllegalArgumentException localIllegalArgumentException)
/*      */     {
/*  964 */       return Collections.emptyIterator();
/*      */     }
/*  966 */     return new ImageWriterIterator(localIterator);
/*      */   }
/*      */ 
/*      */   public static ImageWriter getImageWriter(ImageReader paramImageReader)
/*      */   {
/*  999 */     if (paramImageReader == null) {
/* 1000 */       throw new IllegalArgumentException("reader == null!");
/*      */     }
/*      */ 
/* 1003 */     Object localObject1 = paramImageReader.getOriginatingProvider();
/* 1004 */     if (localObject1 == null)
/*      */     {
/*      */       try
/*      */       {
/* 1008 */         localObject2 = theRegistry.getServiceProviders(ImageReaderSpi.class, false);
/*      */       }
/*      */       catch (IllegalArgumentException localIllegalArgumentException)
/*      */       {
/* 1012 */         return null;
/*      */       }
/*      */ 
/* 1015 */       while (((Iterator)localObject2).hasNext()) {
/* 1016 */         localObject3 = (ImageReaderSpi)((Iterator)localObject2).next();
/* 1017 */         if (((ImageReaderSpi)localObject3).isOwnReader(paramImageReader)) {
/* 1018 */           localObject1 = localObject3;
/* 1019 */           break;
/*      */         }
/*      */       }
/* 1022 */       if (localObject1 == null) {
/* 1023 */         return null;
/*      */       }
/*      */     }
/*      */ 
/* 1027 */     Object localObject2 = ((ImageReaderSpi)localObject1).getImageWriterSpiNames();
/* 1028 */     if (localObject2 == null) {
/* 1029 */       return null;
/*      */     }
/*      */ 
/* 1032 */     Object localObject3 = null;
/*      */     try {
/* 1034 */       localObject3 = Class.forName(localObject2[0], true, ClassLoader.getSystemClassLoader());
/*      */     }
/*      */     catch (ClassNotFoundException localClassNotFoundException) {
/* 1037 */       return null;
/*      */     }
/*      */ 
/* 1040 */     ImageWriterSpi localImageWriterSpi = (ImageWriterSpi)theRegistry.getServiceProviderByClass((Class)localObject3);
/*      */ 
/* 1042 */     if (localImageWriterSpi == null) {
/* 1043 */       return null;
/*      */     }
/*      */     try
/*      */     {
/* 1047 */       return localImageWriterSpi.createWriterInstance();
/*      */     }
/*      */     catch (IOException localIOException) {
/* 1050 */       theRegistry.deregisterServiceProvider(localImageWriterSpi, ImageWriterSpi.class);
/*      */     }
/* 1052 */     return null;
/*      */   }
/*      */ 
/*      */   public static ImageReader getImageReader(ImageWriter paramImageWriter)
/*      */   {
/* 1079 */     if (paramImageWriter == null) {
/* 1080 */       throw new IllegalArgumentException("writer == null!");
/*      */     }
/*      */ 
/* 1083 */     Object localObject1 = paramImageWriter.getOriginatingProvider();
/* 1084 */     if (localObject1 == null)
/*      */     {
/*      */       try
/*      */       {
/* 1088 */         localObject2 = theRegistry.getServiceProviders(ImageWriterSpi.class, false);
/*      */       }
/*      */       catch (IllegalArgumentException localIllegalArgumentException)
/*      */       {
/* 1092 */         return null;
/*      */       }
/*      */ 
/* 1095 */       while (((Iterator)localObject2).hasNext()) {
/* 1096 */         localObject3 = (ImageWriterSpi)((Iterator)localObject2).next();
/* 1097 */         if (((ImageWriterSpi)localObject3).isOwnWriter(paramImageWriter)) {
/* 1098 */           localObject1 = localObject3;
/* 1099 */           break;
/*      */         }
/*      */       }
/* 1102 */       if (localObject1 == null) {
/* 1103 */         return null;
/*      */       }
/*      */     }
/*      */ 
/* 1107 */     Object localObject2 = ((ImageWriterSpi)localObject1).getImageReaderSpiNames();
/* 1108 */     if (localObject2 == null) {
/* 1109 */       return null;
/*      */     }
/*      */ 
/* 1112 */     Object localObject3 = null;
/*      */     try {
/* 1114 */       localObject3 = Class.forName(localObject2[0], true, ClassLoader.getSystemClassLoader());
/*      */     }
/*      */     catch (ClassNotFoundException localClassNotFoundException) {
/* 1117 */       return null;
/*      */     }
/*      */ 
/* 1120 */     ImageReaderSpi localImageReaderSpi = (ImageReaderSpi)theRegistry.getServiceProviderByClass((Class)localObject3);
/*      */ 
/* 1122 */     if (localImageReaderSpi == null) {
/* 1123 */       return null;
/*      */     }
/*      */     try
/*      */     {
/* 1127 */       return localImageReaderSpi.createReaderInstance();
/*      */     }
/*      */     catch (IOException localIOException) {
/* 1130 */       theRegistry.deregisterServiceProvider(localImageReaderSpi, ImageReaderSpi.class);
/*      */     }
/* 1132 */     return null;
/*      */   }
/*      */ 
/*      */   public static Iterator<ImageWriter> getImageWriters(ImageTypeSpecifier paramImageTypeSpecifier, String paramString)
/*      */   {
/* 1156 */     if (paramImageTypeSpecifier == null) {
/* 1157 */       throw new IllegalArgumentException("type == null!");
/*      */     }
/* 1159 */     if (paramString == null) {
/* 1160 */       throw new IllegalArgumentException("formatName == null!");
/*      */     }
/*      */ 
/*      */     Iterator localIterator;
/*      */     try
/*      */     {
/* 1166 */       localIterator = theRegistry.getServiceProviders(ImageWriterSpi.class, new CanEncodeImageAndFormatFilter(paramImageTypeSpecifier, paramString), true);
/*      */     }
/*      */     catch (IllegalArgumentException localIllegalArgumentException)
/*      */     {
/* 1171 */       return Collections.emptyIterator();
/*      */     }
/*      */ 
/* 1174 */     return new ImageWriterIterator(localIterator);
/*      */   }
/*      */ 
/*      */   public static Iterator<ImageTranscoder> getImageTranscoders(ImageReader paramImageReader, ImageWriter paramImageWriter)
/*      */   {
/* 1241 */     if (paramImageReader == null) {
/* 1242 */       throw new IllegalArgumentException("reader == null!");
/*      */     }
/* 1244 */     if (paramImageWriter == null) {
/* 1245 */       throw new IllegalArgumentException("writer == null!");
/*      */     }
/* 1247 */     ImageReaderSpi localImageReaderSpi = paramImageReader.getOriginatingProvider();
/* 1248 */     ImageWriterSpi localImageWriterSpi = paramImageWriter.getOriginatingProvider();
/* 1249 */     TranscoderFilter localTranscoderFilter = new TranscoderFilter(localImageReaderSpi, localImageWriterSpi);
/*      */     Iterator localIterator;
/*      */     try {
/* 1255 */       localIterator = theRegistry.getServiceProviders(ImageTranscoderSpi.class, localTranscoderFilter, true);
/*      */     }
/*      */     catch (IllegalArgumentException localIllegalArgumentException) {
/* 1258 */       return Collections.emptyIterator();
/*      */     }
/* 1260 */     return new ImageTranscoderIterator(localIterator);
/*      */   }
/*      */ 
/*      */   public static BufferedImage read(File paramFile)
/*      */     throws IOException
/*      */   {
/* 1297 */     if (paramFile == null) {
/* 1298 */       throw new IllegalArgumentException("input == null!");
/*      */     }
/* 1300 */     if (!paramFile.canRead()) {
/* 1301 */       throw new IIOException("Can't read input file!");
/*      */     }
/*      */ 
/* 1304 */     ImageInputStream localImageInputStream = createImageInputStream(paramFile);
/* 1305 */     if (localImageInputStream == null) {
/* 1306 */       throw new IIOException("Can't create an ImageInputStream!");
/*      */     }
/* 1308 */     BufferedImage localBufferedImage = read(localImageInputStream);
/* 1309 */     if (localBufferedImage == null) {
/* 1310 */       localImageInputStream.close();
/*      */     }
/* 1312 */     return localBufferedImage;
/*      */   }
/*      */ 
/*      */   public static BufferedImage read(InputStream paramInputStream)
/*      */     throws IOException
/*      */   {
/* 1347 */     if (paramInputStream == null) {
/* 1348 */       throw new IllegalArgumentException("input == null!");
/*      */     }
/*      */ 
/* 1351 */     ImageInputStream localImageInputStream = createImageInputStream(paramInputStream);
/* 1352 */     BufferedImage localBufferedImage = read(localImageInputStream);
/* 1353 */     if (localBufferedImage == null) {
/* 1354 */       localImageInputStream.close();
/*      */     }
/* 1356 */     return localBufferedImage;
/*      */   }
/*      */ 
/*      */   public static BufferedImage read(URL paramURL)
/*      */     throws IOException
/*      */   {
/* 1387 */     if (paramURL == null) {
/* 1388 */       throw new IllegalArgumentException("input == null!");
/*      */     }
/*      */ 
/* 1391 */     InputStream localInputStream = null;
/*      */     try {
/* 1393 */       localInputStream = paramURL.openStream();
/*      */     } catch (IOException localIOException) {
/* 1395 */       throw new IIOException("Can't get input stream from URL!", localIOException);
/* 1397 */     }ImageInputStream localImageInputStream = createImageInputStream(localInputStream);
/*      */     BufferedImage localBufferedImage;
/*      */     try {
/* 1400 */       localBufferedImage = read(localImageInputStream);
/* 1401 */       if (localBufferedImage == null)
/* 1402 */         localImageInputStream.close();
/*      */     }
/*      */     finally {
/* 1405 */       localInputStream.close();
/*      */     }
/* 1407 */     return localBufferedImage;
/*      */   }
/*      */ 
/*      */   public static BufferedImage read(ImageInputStream paramImageInputStream)
/*      */     throws IOException
/*      */   {
/* 1434 */     if (paramImageInputStream == null) {
/* 1435 */       throw new IllegalArgumentException("stream == null!");
/*      */     }
/*      */ 
/* 1438 */     Iterator localIterator = getImageReaders(paramImageInputStream);
/* 1439 */     if (!localIterator.hasNext()) {
/* 1440 */       return null; } 
/*      */ ImageReader localImageReader = (ImageReader)localIterator.next();
/* 1444 */     ImageReadParam localImageReadParam = localImageReader.getDefaultReadParam();
/* 1445 */     localImageReader.setInput(paramImageInputStream, true, true);
/*      */     BufferedImage localBufferedImage;
/*      */     try { localBufferedImage = localImageReader.read(0, localImageReadParam);
/*      */     } finally {
/* 1450 */       localImageReader.dispose();
/* 1451 */       paramImageInputStream.close();
/*      */     }
/* 1453 */     return localBufferedImage;
/*      */   }
/*      */ 
/*      */   public static boolean write(RenderedImage paramRenderedImage, String paramString, ImageOutputStream paramImageOutputStream)
/*      */     throws IOException
/*      */   {
/* 1482 */     if (paramRenderedImage == null) {
/* 1483 */       throw new IllegalArgumentException("im == null!");
/*      */     }
/* 1485 */     if (paramString == null) {
/* 1486 */       throw new IllegalArgumentException("formatName == null!");
/*      */     }
/* 1488 */     if (paramImageOutputStream == null) {
/* 1489 */       throw new IllegalArgumentException("output == null!");
/*      */     }
/*      */ 
/* 1492 */     return doWrite(paramRenderedImage, getWriter(paramRenderedImage, paramString), paramImageOutputStream);
/*      */   }
/*      */ 
/*      */   public static boolean write(RenderedImage paramRenderedImage, String paramString, File paramFile)
/*      */     throws IOException
/*      */   {
/* 1515 */     if (paramFile == null) {
/* 1516 */       throw new IllegalArgumentException("output == null!");
/*      */     }
/* 1518 */     ImageOutputStream localImageOutputStream = null;
/*      */ 
/* 1520 */     ImageWriter localImageWriter = getWriter(paramRenderedImage, paramString);
/* 1521 */     if (localImageWriter == null)
/*      */     {
/* 1525 */       return false;
/*      */     }
/*      */     try
/*      */     {
/* 1529 */       paramFile.delete();
/* 1530 */       localImageOutputStream = createImageOutputStream(paramFile);
/*      */     } catch (IOException localIOException) {
/* 1532 */       throw new IIOException("Can't create output stream!", localIOException);
/*      */     }
/*      */     try
/*      */     {
/* 1536 */       return doWrite(paramRenderedImage, localImageWriter, localImageOutputStream);
/*      */     } finally {
/* 1538 */       localImageOutputStream.close();
/*      */     }
/*      */   }
/*      */ 
/*      */   public static boolean write(RenderedImage paramRenderedImage, String paramString, OutputStream paramOutputStream)
/*      */     throws IOException
/*      */   {
/* 1567 */     if (paramOutputStream == null) {
/* 1568 */       throw new IllegalArgumentException("output == null!");
/*      */     }
/* 1570 */     ImageOutputStream localImageOutputStream = null;
/*      */     try {
/* 1572 */       localImageOutputStream = createImageOutputStream(paramOutputStream);
/*      */     } catch (IOException localIOException) {
/* 1574 */       throw new IIOException("Can't create output stream!", localIOException);
/*      */     }
/*      */     try
/*      */     {
/* 1578 */       return doWrite(paramRenderedImage, getWriter(paramRenderedImage, paramString), localImageOutputStream);
/*      */     } finally {
/* 1580 */       localImageOutputStream.close();
/*      */     }
/*      */   }
/*      */ 
/*      */   private static ImageWriter getWriter(RenderedImage paramRenderedImage, String paramString)
/*      */   {
/* 1591 */     ImageTypeSpecifier localImageTypeSpecifier = ImageTypeSpecifier.createFromRenderedImage(paramRenderedImage);
/*      */ 
/* 1593 */     Iterator localIterator = getImageWriters(localImageTypeSpecifier, paramString);
/*      */ 
/* 1595 */     if (localIterator.hasNext()) {
/* 1596 */       return (ImageWriter)localIterator.next();
/*      */     }
/* 1598 */     return null;
/*      */   }
/*      */ 
/*      */   private static boolean doWrite(RenderedImage paramRenderedImage, ImageWriter paramImageWriter, ImageOutputStream paramImageOutputStream)
/*      */     throws IOException
/*      */   {
/* 1607 */     if (paramImageWriter == null) {
/* 1608 */       return false;
/*      */     }
/* 1610 */     paramImageWriter.setOutput(paramImageOutputStream);
/*      */     try {
/* 1612 */       paramImageWriter.write(paramRenderedImage);
/*      */     } finally {
/* 1614 */       paramImageWriter.dispose();
/* 1615 */       paramImageOutputStream.flush();
/*      */     }
/* 1617 */     return true;
/*      */   }
/*      */ 
/*      */   static
/*      */   {
/*      */     try
/*      */     {
/*  665 */       readerFormatNamesMethod = ImageReaderSpi.class.getMethod("getFormatNames", new Class[0]);
/*      */ 
/*  667 */       readerFileSuffixesMethod = ImageReaderSpi.class.getMethod("getFileSuffixes", new Class[0]);
/*      */ 
/*  669 */       readerMIMETypesMethod = ImageReaderSpi.class.getMethod("getMIMETypes", new Class[0]);
/*      */ 
/*  672 */       writerFormatNamesMethod = ImageWriterSpi.class.getMethod("getFormatNames", new Class[0]);
/*      */ 
/*  674 */       writerFileSuffixesMethod = ImageWriterSpi.class.getMethod("getFileSuffixes", new Class[0]);
/*      */ 
/*  676 */       writerMIMETypesMethod = ImageWriterSpi.class.getMethod("getMIMETypes", new Class[0]);
/*      */     }
/*      */     catch (NoSuchMethodException localNoSuchMethodException) {
/*  679 */       localNoSuchMethodException.printStackTrace();
/*      */     }
/*      */   }
/*      */ 
/*      */   static class CacheInfo
/*      */   {
/*  121 */     boolean useCache = true;
/*  122 */     File cacheDirectory = null;
/*  123 */     Boolean hasPermission = null;
/*      */ 
/*      */     public boolean getUseCache()
/*      */     {
/*  128 */       return this.useCache;
/*      */     }
/*      */ 
/*      */     public void setUseCache(boolean paramBoolean) {
/*  132 */       this.useCache = paramBoolean;
/*      */     }
/*      */ 
/*      */     public File getCacheDirectory() {
/*  136 */       return this.cacheDirectory;
/*      */     }
/*      */ 
/*      */     public void setCacheDirectory(File paramFile) {
/*  140 */       this.cacheDirectory = paramFile;
/*      */     }
/*      */ 
/*      */     public Boolean getHasPermission() {
/*  144 */       return this.hasPermission;
/*      */     }
/*      */ 
/*      */     public void setHasPermission(Boolean paramBoolean) {
/*  148 */       this.hasPermission = paramBoolean;
/*      */     }
/*      */   }
/*      */ 
/*      */   static class CanDecodeInputFilter
/*      */     implements ServiceRegistry.Filter
/*      */   {
/*      */     Object input;
/*      */ 
/*      */     public CanDecodeInputFilter(Object paramObject)
/*      */     {
/*  549 */       this.input = paramObject;
/*      */     }
/*      */ 
/*      */     public boolean filter(Object paramObject) {
/*      */       try {
/*  554 */         ImageReaderSpi localImageReaderSpi = (ImageReaderSpi)paramObject;
/*  555 */         ImageInputStream localImageInputStream = null;
/*  556 */         if ((this.input instanceof ImageInputStream)) {
/*  557 */           localImageInputStream = (ImageInputStream)this.input;
/*      */         }
/*      */ 
/*  563 */         boolean bool = false;
/*  564 */         if (localImageInputStream != null) {
/*  565 */           localImageInputStream.mark();
/*      */         }
/*  567 */         bool = localImageReaderSpi.canDecodeInput(this.input);
/*  568 */         if (localImageInputStream != null) {
/*  569 */           localImageInputStream.reset();
/*      */         }
/*      */ 
/*  572 */         return bool; } catch (IOException localIOException) {
/*      */       }
/*  574 */       return false;
/*      */     }
/*      */   }
/*      */ 
/*      */   static class CanEncodeImageAndFormatFilter
/*      */     implements ServiceRegistry.Filter
/*      */   {
/*      */     ImageTypeSpecifier type;
/*      */     String formatName;
/*      */ 
/*      */     public CanEncodeImageAndFormatFilter(ImageTypeSpecifier paramImageTypeSpecifier, String paramString)
/*      */     {
/*  587 */       this.type = paramImageTypeSpecifier;
/*  588 */       this.formatName = paramString;
/*      */     }
/*      */ 
/*      */     public boolean filter(Object paramObject) {
/*  592 */       ImageWriterSpi localImageWriterSpi = (ImageWriterSpi)paramObject;
/*  593 */       return (Arrays.asList(localImageWriterSpi.getFormatNames()).contains(this.formatName)) && (localImageWriterSpi.canEncodeImage(this.type));
/*      */     }
/*      */   }
/*      */ 
/*      */   static class ContainsFilter
/*      */     implements ServiceRegistry.Filter
/*      */   {
/*      */     Method method;
/*      */     String name;
/*      */ 
/*      */     public ContainsFilter(Method paramMethod, String paramString)
/*      */     {
/*  607 */       this.method = paramMethod;
/*  608 */       this.name = paramString;
/*      */     }
/*      */ 
/*      */     public boolean filter(Object paramObject) {
/*      */       try {
/*  613 */         return ImageIO.contains((String[])this.method.invoke(paramObject, new Object[0]), this.name); } catch (Exception localException) {
/*      */       }
/*  615 */       return false;
/*      */     }
/*      */   }
/*      */ 
/*      */   static class ImageReaderIterator
/*      */     implements Iterator<ImageReader>
/*      */   {
/*      */     public Iterator iter;
/*      */ 
/*      */     public ImageReaderIterator(Iterator paramIterator)
/*      */     {
/*  518 */       this.iter = paramIterator;
/*      */     }
/*      */ 
/*      */     public boolean hasNext() {
/*  522 */       return this.iter.hasNext();
/*      */     }
/*      */ 
/*      */     public ImageReader next() {
/*  526 */       ImageReaderSpi localImageReaderSpi = null;
/*      */       try {
/*  528 */         localImageReaderSpi = (ImageReaderSpi)this.iter.next();
/*  529 */         return localImageReaderSpi.createReaderInstance();
/*      */       }
/*      */       catch (IOException localIOException)
/*      */       {
/*  533 */         ImageIO.theRegistry.deregisterServiceProvider(localImageReaderSpi, ImageReaderSpi.class);
/*      */       }
/*  535 */       return null;
/*      */     }
/*      */ 
/*      */     public void remove() {
/*  539 */       throw new UnsupportedOperationException();
/*      */     }
/*      */   }
/*      */ 
/*      */   static class ImageTranscoderIterator
/*      */     implements Iterator<ImageTranscoder>
/*      */   {
/*      */     public Iterator iter;
/*      */ 
/*      */     public ImageTranscoderIterator(Iterator paramIterator)
/*      */     {
/* 1184 */       this.iter = paramIterator;
/*      */     }
/*      */ 
/*      */     public boolean hasNext() {
/* 1188 */       return this.iter.hasNext();
/*      */     }
/*      */ 
/*      */     public ImageTranscoder next() {
/* 1192 */       ImageTranscoderSpi localImageTranscoderSpi = null;
/* 1193 */       localImageTranscoderSpi = (ImageTranscoderSpi)this.iter.next();
/* 1194 */       return localImageTranscoderSpi.createTranscoderInstance();
/*      */     }
/*      */ 
/*      */     public void remove() {
/* 1198 */       throw new UnsupportedOperationException();
/*      */     }
/*      */   }
/*      */ 
/*      */   static class ImageWriterIterator
/*      */     implements Iterator<ImageWriter>
/*      */   {
/*      */     public Iterator iter;
/*      */ 
/*      */     public ImageWriterIterator(Iterator paramIterator)
/*      */     {
/*  832 */       this.iter = paramIterator;
/*      */     }
/*      */ 
/*      */     public boolean hasNext() {
/*  836 */       return this.iter.hasNext();
/*      */     }
/*      */ 
/*      */     public ImageWriter next() {
/*  840 */       ImageWriterSpi localImageWriterSpi = null;
/*      */       try {
/*  842 */         localImageWriterSpi = (ImageWriterSpi)this.iter.next();
/*  843 */         return localImageWriterSpi.createWriterInstance();
/*      */       }
/*      */       catch (IOException localIOException) {
/*  846 */         ImageIO.theRegistry.deregisterServiceProvider(localImageWriterSpi, ImageWriterSpi.class);
/*      */       }
/*  848 */       return null;
/*      */     }
/*      */ 
/*      */     public void remove() {
/*  852 */       throw new UnsupportedOperationException();
/*      */     }
/*      */   }
/*      */ 
/*      */   private static abstract enum SpiInfo
/*      */   {
/*  432 */     FORMAT_NAMES, 
/*      */ 
/*  438 */     MIME_TYPES, 
/*      */ 
/*  444 */     FILE_SUFFIXES;
/*      */ 
/*      */     abstract String[] info(ImageReaderWriterSpi paramImageReaderWriterSpi);
/*      */   }
/*      */ 
/*      */   static class TranscoderFilter
/*      */     implements ServiceRegistry.Filter
/*      */   {
/*      */     String readerSpiName;
/*      */     String writerSpiName;
/*      */ 
/*      */     public TranscoderFilter(ImageReaderSpi paramImageReaderSpi, ImageWriterSpi paramImageWriterSpi)
/*      */     {
/* 1210 */       this.readerSpiName = paramImageReaderSpi.getClass().getName();
/* 1211 */       this.writerSpiName = paramImageWriterSpi.getClass().getName();
/*      */     }
/*      */ 
/*      */     public boolean filter(Object paramObject) {
/* 1215 */       ImageTranscoderSpi localImageTranscoderSpi = (ImageTranscoderSpi)paramObject;
/* 1216 */       String str1 = localImageTranscoderSpi.getReaderServiceProviderName();
/* 1217 */       String str2 = localImageTranscoderSpi.getWriterServiceProviderName();
/* 1218 */       return (str1.equals(this.readerSpiName)) && (str2.equals(this.writerSpiName));
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.imageio.ImageIO
 * JD-Core Version:    0.6.2
 */