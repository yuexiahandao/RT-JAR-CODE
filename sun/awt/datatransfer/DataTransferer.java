/*      */ package sun.awt.datatransfer;
/*      */ 
/*      */ import java.awt.AWTError;
/*      */ import java.awt.EventQueue;
/*      */ import java.awt.Graphics;
/*      */ import java.awt.Image;
/*      */ import java.awt.datatransfer.DataFlavor;
/*      */ import java.awt.datatransfer.FlavorMap;
/*      */ import java.awt.datatransfer.FlavorTable;
/*      */ import java.awt.datatransfer.Transferable;
/*      */ import java.awt.datatransfer.UnsupportedFlavorException;
/*      */ import java.awt.image.BufferedImage;
/*      */ import java.awt.image.ColorModel;
/*      */ import java.awt.image.RenderedImage;
/*      */ import java.awt.image.WritableRaster;
/*      */ import java.io.BufferedReader;
/*      */ import java.io.ByteArrayInputStream;
/*      */ import java.io.ByteArrayOutputStream;
/*      */ import java.io.File;
/*      */ import java.io.FilePermission;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.InputStreamReader;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.ObjectOutputStream;
/*      */ import java.io.OutputStream;
/*      */ import java.io.Reader;
/*      */ import java.io.SequenceInputStream;
/*      */ import java.io.Serializable;
/*      */ import java.io.StringReader;
/*      */ import java.lang.reflect.Constructor;
/*      */ import java.lang.reflect.InvocationTargetException;
/*      */ import java.lang.reflect.Method;
/*      */ import java.lang.reflect.Modifier;
/*      */ import java.net.URI;
/*      */ import java.net.URISyntaxException;
/*      */ import java.nio.Buffer;
/*      */ import java.nio.ByteBuffer;
/*      */ import java.nio.CharBuffer;
/*      */ import java.nio.charset.Charset;
/*      */ import java.nio.charset.CharsetEncoder;
/*      */ import java.nio.charset.IllegalCharsetNameException;
/*      */ import java.nio.charset.UnsupportedCharsetException;
/*      */ import java.security.AccessController;
/*      */ import java.security.PrivilegedAction;
/*      */ import java.security.PrivilegedActionException;
/*      */ import java.security.PrivilegedExceptionAction;
/*      */ import java.security.ProtectionDomain;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collections;
/*      */ import java.util.Comparator;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import java.util.SortedMap;
/*      */ import java.util.SortedSet;
/*      */ import java.util.Stack;
/*      */ import java.util.TreeMap;
/*      */ import java.util.TreeSet;
/*      */ import javax.imageio.ImageIO;
/*      */ import javax.imageio.ImageReadParam;
/*      */ import javax.imageio.ImageReader;
/*      */ import javax.imageio.ImageTypeSpecifier;
/*      */ import javax.imageio.ImageWriter;
/*      */ import javax.imageio.spi.ImageWriterSpi;
/*      */ import javax.imageio.stream.ImageInputStream;
/*      */ import javax.imageio.stream.ImageOutputStream;
/*      */ import sun.awt.AppContext;
/*      */ import sun.awt.SunToolkit;
/*      */ import sun.awt.image.ImageRepresentation;
/*      */ import sun.awt.image.ToolkitImage;
/*      */ import sun.util.logging.PlatformLogger;
/*      */ 
/*      */ public abstract class DataTransferer
/*      */ {
/*      */   public static final Class charArrayClass;
/*      */   public static final Class byteArrayClass;
/*      */   public static final DataFlavor plainTextStringFlavor;
/*      */   public static final DataFlavor javaTextEncodingFlavor;
/*  285 */   private static final Map textMIMESubtypeCharsetSupport = Collections.synchronizedMap(localHashMap);
/*      */   private static String defaultEncoding;
/*  209 */   private static final Set textNatives = Collections.synchronizedSet(new HashSet());
/*      */ 
/*  215 */   private static final Map nativeCharsets = Collections.synchronizedMap(new HashMap());
/*      */ 
/*  221 */   private static final Map nativeEOLNs = Collections.synchronizedMap(new HashMap());
/*      */ 
/*  227 */   private static final Map nativeTerminators = Collections.synchronizedMap(new HashMap());
/*      */   private static final String DATA_CONVERTER_KEY = "DATA_CONVERTER_KEY";
/*      */   private static DataTransferer transferer;
/*  241 */   private static final PlatformLogger dtLog = PlatformLogger.getLogger("sun.awt.datatransfer.DataTransfer");
/*      */ 
/* 1521 */   private static final String[] DEPLOYMENT_CACHE_PROPERTIES = { "deployment.system.cachedir", "deployment.user.cachedir", "deployment.javaws.cachedir", "deployment.javapi.cachedir" };
/*      */ 
/* 1528 */   private static final ArrayList<File> deploymentCacheDirectoryList = new ArrayList();
/*      */ 
/*      */   public static DataTransferer getInstance()
/*      */   {
/*  294 */     synchronized (DataTransferer.class) {
/*  295 */       if (transferer == null) {
/*  296 */         String str = SunToolkit.getDataTransfererClassName();
/*  297 */         if (str != null) {
/*  298 */           PrivilegedAction local1 = new PrivilegedAction()
/*      */           {
/*      */             public DataTransferer run() {
/*  301 */               Class localClass = null;
/*  302 */               Method localMethod = null;
/*  303 */               DataTransferer localDataTransferer = null;
/*      */               try
/*      */               {
/*  306 */                 localClass = Class.forName(this.val$name);
/*      */               } catch (ClassNotFoundException localClassNotFoundException1) {
/*  308 */                 ClassLoader localClassLoader = ClassLoader.getSystemClassLoader();
/*      */ 
/*  310 */                 if (localClassLoader != null) {
/*      */                   try {
/*  312 */                     localClass = localClassLoader.loadClass(this.val$name);
/*      */                   } catch (ClassNotFoundException localClassNotFoundException2) {
/*  314 */                     localClassNotFoundException2.printStackTrace();
/*  315 */                     throw new AWTError("DataTransferer not found: " + this.val$name);
/*      */                   }
/*      */                 }
/*      */               }
/*  319 */               if (localClass != null) {
/*      */                 try {
/*  321 */                   localMethod = localClass.getDeclaredMethod("getInstanceImpl", new Class[0]);
/*  322 */                   localMethod.setAccessible(true);
/*      */                 } catch (NoSuchMethodException localNoSuchMethodException) {
/*  324 */                   localNoSuchMethodException.printStackTrace();
/*  325 */                   throw new AWTError("Cannot instantiate DataTransferer: " + this.val$name);
/*      */                 } catch (SecurityException localSecurityException) {
/*  327 */                   localSecurityException.printStackTrace();
/*  328 */                   throw new AWTError("Access is denied for DataTransferer: " + this.val$name);
/*      */                 }
/*      */               }
/*  331 */               if (localMethod != null) {
/*      */                 try {
/*  333 */                   localDataTransferer = (DataTransferer)localMethod.invoke(null, new Object[0]);
/*      */                 } catch (InvocationTargetException localInvocationTargetException) {
/*  335 */                   localInvocationTargetException.printStackTrace();
/*  336 */                   throw new AWTError("Cannot instantiate DataTransferer: " + this.val$name);
/*      */                 } catch (IllegalAccessException localIllegalAccessException) {
/*  338 */                   localIllegalAccessException.printStackTrace();
/*  339 */                   throw new AWTError("Cannot access DataTransferer: " + this.val$name);
/*      */                 }
/*      */               }
/*  342 */               return localDataTransferer;
/*      */             }
/*      */           };
/*  345 */           transferer = (DataTransferer)AccessController.doPrivileged(local1);
/*      */         }
/*      */       }
/*      */     }
/*  349 */     return transferer;
/*      */   }
/*      */ 
/*      */   public static String canonicalName(String paramString)
/*      */   {
/*  356 */     if (paramString == null)
/*  357 */       return null;
/*      */     try
/*      */     {
/*  360 */       return Charset.forName(paramString).name();
/*      */     } catch (IllegalCharsetNameException localIllegalCharsetNameException) {
/*  362 */       return paramString; } catch (UnsupportedCharsetException localUnsupportedCharsetException) {
/*      */     }
/*  364 */     return paramString;
/*      */   }
/*      */ 
/*      */   public static String getTextCharset(DataFlavor paramDataFlavor)
/*      */   {
/*  376 */     if (!isFlavorCharsetTextType(paramDataFlavor)) {
/*  377 */       return null;
/*      */     }
/*      */ 
/*  380 */     String str = paramDataFlavor.getParameter("charset");
/*      */ 
/*  382 */     return str != null ? str : getDefaultTextCharset();
/*      */   }
/*      */ 
/*      */   public static String getDefaultTextCharset()
/*      */   {
/*  389 */     if (defaultEncoding != null) {
/*  390 */       return defaultEncoding;
/*      */     }
/*  392 */     return DataTransferer.defaultEncoding = Charset.defaultCharset().name();
/*      */   }
/*      */ 
/*      */   public static boolean doesSubtypeSupportCharset(DataFlavor paramDataFlavor)
/*      */   {
/*  401 */     if ((dtLog.isLoggable(500)) && 
/*  402 */       (!"text".equals(paramDataFlavor.getPrimaryType()))) {
/*  403 */       dtLog.fine("Assertion (\"text\".equals(flavor.getPrimaryType())) failed");
/*      */     }
/*      */ 
/*  407 */     String str = paramDataFlavor.getSubType();
/*  408 */     if (str == null) {
/*  409 */       return false;
/*      */     }
/*      */ 
/*  412 */     Object localObject = textMIMESubtypeCharsetSupport.get(str);
/*      */ 
/*  414 */     if (localObject != null) {
/*  415 */       return localObject == Boolean.TRUE;
/*      */     }
/*      */ 
/*  418 */     boolean bool = paramDataFlavor.getParameter("charset") != null;
/*  419 */     textMIMESubtypeCharsetSupport.put(str, bool ? Boolean.TRUE : Boolean.FALSE);
/*      */ 
/*  421 */     return bool;
/*      */   }
/*      */ 
/*      */   public static boolean doesSubtypeSupportCharset(String paramString1, String paramString2)
/*      */   {
/*  426 */     Object localObject = textMIMESubtypeCharsetSupport.get(paramString1);
/*      */ 
/*  428 */     if (localObject != null) {
/*  429 */       return localObject == Boolean.TRUE;
/*      */     }
/*      */ 
/*  432 */     boolean bool = paramString2 != null;
/*  433 */     textMIMESubtypeCharsetSupport.put(paramString1, bool ? Boolean.TRUE : Boolean.FALSE);
/*      */ 
/*  435 */     return bool;
/*      */   }
/*      */ 
/*      */   public static boolean isFlavorCharsetTextType(DataFlavor paramDataFlavor)
/*      */   {
/*  447 */     if (DataFlavor.stringFlavor.equals(paramDataFlavor)) {
/*  448 */       return true;
/*      */     }
/*      */ 
/*  451 */     if ((!"text".equals(paramDataFlavor.getPrimaryType())) || (!doesSubtypeSupportCharset(paramDataFlavor)))
/*      */     {
/*  454 */       return false;
/*      */     }
/*      */ 
/*  457 */     Class localClass = paramDataFlavor.getRepresentationClass();
/*      */ 
/*  459 */     if ((paramDataFlavor.isRepresentationClassReader()) || (String.class.equals(localClass)) || (paramDataFlavor.isRepresentationClassCharBuffer()) || (charArrayClass.equals(localClass)))
/*      */     {
/*  464 */       return true;
/*      */     }
/*      */ 
/*  467 */     if ((!paramDataFlavor.isRepresentationClassInputStream()) && (!paramDataFlavor.isRepresentationClassByteBuffer()) && (!byteArrayClass.equals(localClass)))
/*      */     {
/*  470 */       return false;
/*      */     }
/*      */ 
/*  473 */     String str = paramDataFlavor.getParameter("charset");
/*      */ 
/*  475 */     return str != null ? isEncodingSupported(str) : true;
/*      */   }
/*      */ 
/*      */   public static boolean isFlavorNoncharsetTextType(DataFlavor paramDataFlavor)
/*      */   {
/*  485 */     if ((!"text".equals(paramDataFlavor.getPrimaryType())) || (doesSubtypeSupportCharset(paramDataFlavor)))
/*      */     {
/*  488 */       return false;
/*      */     }
/*      */ 
/*  491 */     return (paramDataFlavor.isRepresentationClassInputStream()) || (paramDataFlavor.isRepresentationClassByteBuffer()) || (byteArrayClass.equals(paramDataFlavor.getRepresentationClass()));
/*      */   }
/*      */ 
/*      */   public static boolean isEncodingSupported(String paramString)
/*      */   {
/*  502 */     if (paramString == null)
/*  503 */       return false;
/*      */     try
/*      */     {
/*  506 */       return Charset.isSupported(paramString); } catch (IllegalCharsetNameException localIllegalCharsetNameException) {
/*      */     }
/*  508 */     return false;
/*      */   }
/*      */ 
/*      */   public static boolean isRemote(Class<?> paramClass)
/*      */   {
/*  516 */     return RMI.isRemote(paramClass);
/*      */   }
/*      */ 
/*      */   public static Iterator standardEncodings()
/*      */   {
/*  527 */     return StandardEncodingsHolder.standardEncodings.iterator();
/*      */   }
/*      */ 
/*      */   public static FlavorTable adaptFlavorMap(FlavorMap paramFlavorMap)
/*      */   {
/*  534 */     if ((paramFlavorMap instanceof FlavorTable)) {
/*  535 */       return (FlavorTable)paramFlavorMap;
/*      */     }
/*      */ 
/*  538 */     return new FlavorTable() {
/*      */       public Map getNativesForFlavors(DataFlavor[] paramAnonymousArrayOfDataFlavor) {
/*  540 */         return this.val$map.getNativesForFlavors(paramAnonymousArrayOfDataFlavor);
/*      */       }
/*      */       public Map getFlavorsForNatives(String[] paramAnonymousArrayOfString) {
/*  543 */         return this.val$map.getFlavorsForNatives(paramAnonymousArrayOfString);
/*      */       }
/*      */       public List getNativesForFlavor(DataFlavor paramAnonymousDataFlavor) {
/*  546 */         Map localMap = getNativesForFlavors(new DataFlavor[] { paramAnonymousDataFlavor });
/*      */ 
/*  548 */         String str = (String)localMap.get(paramAnonymousDataFlavor);
/*  549 */         if (str != null) {
/*  550 */           ArrayList localArrayList = new ArrayList(1);
/*  551 */           localArrayList.add(str);
/*  552 */           return localArrayList;
/*      */         }
/*  554 */         return Collections.EMPTY_LIST;
/*      */       }
/*      */ 
/*      */       public List getFlavorsForNative(String paramAnonymousString) {
/*  558 */         Map localMap = getFlavorsForNatives(new String[] { paramAnonymousString });
/*      */ 
/*  560 */         DataFlavor localDataFlavor = (DataFlavor)localMap.get(paramAnonymousString);
/*  561 */         if (localDataFlavor != null) {
/*  562 */           ArrayList localArrayList = new ArrayList(1);
/*  563 */           localArrayList.add(localDataFlavor);
/*  564 */           return localArrayList;
/*      */         }
/*  566 */         return Collections.EMPTY_LIST;
/*      */       }
/*      */     };
/*      */   }
/*      */ 
/*      */   public abstract String getDefaultUnicodeEncoding();
/*      */ 
/*      */   public void registerTextFlavorProperties(String paramString1, String paramString2, String paramString3, String paramString4)
/*      */   {
/*  587 */     Long localLong = getFormatForNativeAsLong(paramString1);
/*      */ 
/*  589 */     textNatives.add(localLong);
/*  590 */     nativeCharsets.put(localLong, (paramString2 != null) && (paramString2.length() != 0) ? paramString2 : getDefaultTextCharset());
/*      */ 
/*  592 */     if ((paramString3 != null) && (paramString3.length() != 0) && (!paramString3.equals("\n"))) {
/*  593 */       nativeEOLNs.put(localLong, paramString3);
/*      */     }
/*  595 */     if ((paramString4 != null) && (paramString4.length() != 0)) {
/*  596 */       Integer localInteger = Integer.valueOf(paramString4);
/*  597 */       if (localInteger.intValue() > 0)
/*  598 */         nativeTerminators.put(localLong, localInteger);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected boolean isTextFormat(long paramLong)
/*      */   {
/*  608 */     return textNatives.contains(Long.valueOf(paramLong));
/*      */   }
/*      */ 
/*      */   protected String getCharsetForTextFormat(Long paramLong) {
/*  612 */     return (String)nativeCharsets.get(paramLong);
/*      */   }
/*      */ 
/*      */   public abstract boolean isLocaleDependentTextFormat(long paramLong);
/*      */ 
/*      */   public abstract boolean isFileFormat(long paramLong);
/*      */ 
/*      */   public abstract boolean isImageFormat(long paramLong);
/*      */ 
/*      */   protected boolean isURIListFormat(long paramLong)
/*      */   {
/*  640 */     return false;
/*      */   }
/*      */ 
/*      */   public SortedMap getFormatsForTransferable(Transferable paramTransferable, FlavorTable paramFlavorTable)
/*      */   {
/*  654 */     DataFlavor[] arrayOfDataFlavor = paramTransferable.getTransferDataFlavors();
/*  655 */     if (arrayOfDataFlavor == null) {
/*  656 */       return new TreeMap();
/*      */     }
/*  658 */     return getFormatsForFlavors(arrayOfDataFlavor, paramFlavorTable);
/*      */   }
/*      */ 
/*      */   public SortedMap getFormatsForFlavor(DataFlavor paramDataFlavor, FlavorTable paramFlavorTable)
/*      */   {
/*  671 */     return getFormatsForFlavors(new DataFlavor[] { paramDataFlavor }, paramFlavorTable);
/*      */   }
/*      */ 
/*      */   public SortedMap getFormatsForFlavors(DataFlavor[] paramArrayOfDataFlavor, FlavorTable paramFlavorTable)
/*      */   {
/*  690 */     HashMap localHashMap1 = new HashMap(paramArrayOfDataFlavor.length);
/*  691 */     HashMap localHashMap2 = new HashMap(paramArrayOfDataFlavor.length);
/*      */ 
/*  695 */     HashMap localHashMap3 = new HashMap(paramArrayOfDataFlavor.length);
/*  696 */     HashMap localHashMap4 = new HashMap(paramArrayOfDataFlavor.length);
/*      */ 
/*  698 */     int i = 0;
/*      */ 
/*  703 */     for (int j = paramArrayOfDataFlavor.length - 1; j >= 0; j--) {
/*  704 */       localObject = paramArrayOfDataFlavor[j];
/*  705 */       if (localObject != null)
/*      */       {
/*  709 */         if ((((DataFlavor)localObject).isFlavorTextType()) || (((DataFlavor)localObject).isFlavorJavaFileListType()) || (DataFlavor.imageFlavor.equals((DataFlavor)localObject)) || (((DataFlavor)localObject).isRepresentationClassSerializable()) || (((DataFlavor)localObject).isRepresentationClassInputStream()) || (((DataFlavor)localObject).isRepresentationClassRemote()))
/*      */         {
/*  716 */           List localList = paramFlavorTable.getNativesForFlavor((DataFlavor)localObject);
/*      */ 
/*  718 */           i += localList.size();
/*      */ 
/*  720 */           for (Iterator localIterator = localList.iterator(); localIterator.hasNext(); ) {
/*  721 */             Long localLong = getFormatForNativeAsLong((String)localIterator.next());
/*      */ 
/*  723 */             Integer localInteger = Integer.valueOf(i--);
/*      */ 
/*  725 */             localHashMap1.put(localLong, localObject);
/*  726 */             localHashMap3.put(localLong, localInteger);
/*      */ 
/*  732 */             if ((("text".equals(((DataFlavor)localObject).getPrimaryType())) && ("plain".equals(((DataFlavor)localObject).getSubType()))) || (((DataFlavor)localObject).equals(DataFlavor.stringFlavor)))
/*      */             {
/*  736 */               localHashMap2.put(localLong, localObject);
/*  737 */               localHashMap4.put(localLong, localInteger);
/*      */             }
/*      */           }
/*      */ 
/*  741 */           i += localList.size();
/*      */         }
/*      */       }
/*      */     }
/*  745 */     localHashMap1.putAll(localHashMap2);
/*  746 */     localHashMap3.putAll(localHashMap4);
/*      */ 
/*  749 */     IndexOrderComparator localIndexOrderComparator = new IndexOrderComparator(localHashMap3, false);
/*      */ 
/*  751 */     Object localObject = new TreeMap(localIndexOrderComparator);
/*  752 */     ((SortedMap)localObject).putAll(localHashMap1);
/*      */ 
/*  754 */     return localObject;
/*      */   }
/*      */ 
/*      */   public long[] getFormatsForTransferableAsArray(Transferable paramTransferable, FlavorTable paramFlavorTable)
/*      */   {
/*  763 */     return keysToLongArray(getFormatsForTransferable(paramTransferable, paramFlavorTable));
/*      */   }
/*      */ 
/*      */   public long[] getFormatsForFlavorAsArray(DataFlavor paramDataFlavor, FlavorTable paramFlavorTable) {
/*  767 */     return keysToLongArray(getFormatsForFlavor(paramDataFlavor, paramFlavorTable));
/*      */   }
/*      */ 
/*      */   public long[] getFormatsForFlavorsAsArray(DataFlavor[] paramArrayOfDataFlavor, FlavorTable paramFlavorTable) {
/*  771 */     return keysToLongArray(getFormatsForFlavors(paramArrayOfDataFlavor, paramFlavorTable));
/*      */   }
/*      */ 
/*      */   public Map getFlavorsForFormat(long paramLong, FlavorTable paramFlavorTable)
/*      */   {
/*  781 */     return getFlavorsForFormats(new long[] { paramLong }, paramFlavorTable);
/*      */   }
/*      */ 
/*      */   public Map getFlavorsForFormats(long[] paramArrayOfLong, FlavorTable paramFlavorTable)
/*      */   {
/*  791 */     HashMap localHashMap = new HashMap(paramArrayOfLong.length);
/*  792 */     HashSet localHashSet1 = new HashSet(paramArrayOfLong.length);
/*  793 */     HashSet localHashSet2 = new HashSet(paramArrayOfLong.length);
/*      */     long l;
/*      */     Object localObject1;
/*      */     Object localObject2;
/*      */     Object localObject3;
/*  802 */     for (int i = 0; i < paramArrayOfLong.length; i++) {
/*  803 */       l = paramArrayOfLong[i];
/*  804 */       localObject1 = getNativeForFormat(l);
/*  805 */       localObject2 = paramFlavorTable.getFlavorsForNative((String)localObject1);
/*      */ 
/*  807 */       for (localObject3 = ((List)localObject2).iterator(); ((Iterator)localObject3).hasNext(); ) {
/*  808 */         DataFlavor localDataFlavor2 = (DataFlavor)((Iterator)localObject3).next();
/*      */ 
/*  812 */         if ((localDataFlavor2.isFlavorTextType()) || (localDataFlavor2.isFlavorJavaFileListType()) || (DataFlavor.imageFlavor.equals(localDataFlavor2)) || (localDataFlavor2.isRepresentationClassSerializable()) || (localDataFlavor2.isRepresentationClassInputStream()) || (localDataFlavor2.isRepresentationClassRemote()))
/*      */         {
/*  819 */           Long localLong = Long.valueOf(l);
/*  820 */           Object localObject4 = createMapping(localLong, localDataFlavor2);
/*      */ 
/*  822 */           localHashMap.put(localDataFlavor2, localLong);
/*  823 */           localHashSet1.add(localObject4);
/*  824 */           localHashSet2.add(localDataFlavor2);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  842 */     Iterator localIterator = localHashSet2.iterator();
/*  843 */     while (localIterator.hasNext()) {
/*  844 */       DataFlavor localDataFlavor1 = (DataFlavor)localIterator.next();
/*      */ 
/*  846 */       List localList = paramFlavorTable.getNativesForFlavor(localDataFlavor1);
/*      */ 
/*  848 */       localObject1 = localList.iterator();
/*  849 */       while (((Iterator)localObject1).hasNext()) {
/*  850 */         localObject2 = getFormatForNativeAsLong((String)((Iterator)localObject1).next());
/*      */ 
/*  852 */         localObject3 = createMapping(localObject2, localDataFlavor1);
/*      */ 
/*  854 */         if (localHashSet1.contains(localObject3)) {
/*  855 */           localHashMap.put(localDataFlavor1, localObject2);
/*  856 */           break;
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  861 */     return localHashMap;
/*      */   }
/*      */ 
/*      */   public Set getFlavorsForFormatsAsSet(long[] paramArrayOfLong, FlavorTable paramFlavorTable)
/*      */   {
/*  877 */     HashSet localHashSet = new HashSet(paramArrayOfLong.length);
/*      */     Iterator localIterator;
/*  879 */     for (int i = 0; i < paramArrayOfLong.length; i++) {
/*  880 */       String str = getNativeForFormat(paramArrayOfLong[i]);
/*  881 */       List localList = paramFlavorTable.getFlavorsForNative(str);
/*      */ 
/*  883 */       for (localIterator = localList.iterator(); localIterator.hasNext(); ) {
/*  884 */         DataFlavor localDataFlavor = (DataFlavor)localIterator.next();
/*      */ 
/*  888 */         if ((localDataFlavor.isFlavorTextType()) || (localDataFlavor.isFlavorJavaFileListType()) || (DataFlavor.imageFlavor.equals(localDataFlavor)) || (localDataFlavor.isRepresentationClassSerializable()) || (localDataFlavor.isRepresentationClassInputStream()) || (localDataFlavor.isRepresentationClassRemote()))
/*      */         {
/*  895 */           localHashSet.add(localDataFlavor);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  900 */     return localHashSet;
/*      */   }
/*      */ 
/*      */   public DataFlavor[] getFlavorsForFormatAsArray(long paramLong, FlavorTable paramFlavorTable)
/*      */   {
/*  919 */     return getFlavorsForFormatsAsArray(new long[] { paramLong }, paramFlavorTable);
/*      */   }
/*      */ 
/*      */   public DataFlavor[] getFlavorsForFormatsAsArray(long[] paramArrayOfLong, FlavorTable paramFlavorTable)
/*      */   {
/*  941 */     return setToSortedDataFlavorArray(getFlavorsForFormatsAsSet(paramArrayOfLong, paramFlavorTable));
/*      */   }
/*      */ 
/*      */   private static Object createMapping(Object paramObject1, Object paramObject2)
/*      */   {
/*  957 */     return Arrays.asList(new Object[] { paramObject1, paramObject2 });
/*      */   }
/*      */ 
/*      */   protected abstract Long getFormatForNativeAsLong(String paramString);
/*      */ 
/*      */   protected abstract String getNativeForFormat(long paramLong);
/*      */ 
/*      */   private String getBestCharsetForTextFormat(Long paramLong, Transferable paramTransferable)
/*      */     throws IOException
/*      */   {
/*  979 */     String str = null;
/*  980 */     if ((paramTransferable != null) && (isLocaleDependentTextFormat(paramLong.longValue())) && (paramTransferable.isDataFlavorSupported(javaTextEncodingFlavor)))
/*      */     {
/*      */       try
/*      */       {
/*  985 */         str = new String((byte[])paramTransferable.getTransferData(javaTextEncodingFlavor), "UTF-8");
/*      */       }
/*      */       catch (UnsupportedFlavorException localUnsupportedFlavorException)
/*      */       {
/*      */       }
/*      */     }
/*      */     else {
/*  992 */       str = getCharsetForTextFormat(paramLong);
/*      */     }
/*  994 */     if (str == null)
/*      */     {
/*  996 */       str = getDefaultTextCharset();
/*      */     }
/*  998 */     return str;
/*      */   }
/*      */ 
/*      */   private byte[] translateTransferableString(String paramString, long paramLong)
/*      */     throws IOException
/*      */   {
/* 1011 */     Long localLong = Long.valueOf(paramLong);
/* 1012 */     String str1 = getBestCharsetForTextFormat(localLong, null);
/*      */ 
/* 1017 */     String str2 = (String)nativeEOLNs.get(localLong);
/*      */     int j;
/* 1018 */     if (str2 != null) {
/* 1019 */       int i = paramString.length();
/* 1020 */       localObject2 = new StringBuffer(i * 2);
/*      */ 
/* 1022 */       for (j = 0; j < i; j++)
/*      */       {
/* 1024 */         if (paramString.startsWith(str2, j)) {
/* 1025 */           ((StringBuffer)localObject2).append(str2);
/* 1026 */           j += str2.length() - 1;
/*      */         }
/*      */         else {
/* 1029 */           char c = paramString.charAt(j);
/* 1030 */           if (c == '\n')
/* 1031 */             ((StringBuffer)localObject2).append(str2);
/*      */           else
/* 1033 */             ((StringBuffer)localObject2).append(c);
/*      */         }
/*      */       }
/* 1036 */       paramString = ((StringBuffer)localObject2).toString();
/*      */     }
/*      */ 
/* 1040 */     Object localObject1 = paramString.getBytes(str1);
/*      */ 
/* 1046 */     Object localObject2 = (Integer)nativeTerminators.get(localLong);
/* 1047 */     if (localObject2 != null) {
/* 1048 */       j = ((Integer)localObject2).intValue();
/* 1049 */       byte[] arrayOfByte = new byte[localObject1.length + j];
/*      */ 
/* 1051 */       System.arraycopy(localObject1, 0, arrayOfByte, 0, localObject1.length);
/* 1052 */       for (int k = localObject1.length; k < arrayOfByte.length; k++) {
/* 1053 */         arrayOfByte[k] = 0;
/*      */       }
/* 1055 */       localObject1 = arrayOfByte;
/*      */     }
/* 1057 */     return localObject1;
/*      */   }
/*      */ 
/*      */   private String translateBytesOrStreamToString(InputStream paramInputStream, byte[] paramArrayOfByte, long paramLong, Transferable paramTransferable)
/*      */     throws IOException
/*      */   {
/* 1073 */     if (paramArrayOfByte == null) {
/* 1074 */       paramArrayOfByte = inputStreamToByteArray(paramInputStream);
/*      */     }
/* 1076 */     paramInputStream.close();
/*      */ 
/* 1078 */     Long localLong = Long.valueOf(paramLong);
/* 1079 */     String str1 = getBestCharsetForTextFormat(localLong, paramTransferable);
/*      */ 
/* 1091 */     String str2 = (String)nativeEOLNs.get(localLong);
/* 1092 */     Integer localInteger = (Integer)nativeTerminators.get(localLong);
/*      */     int i;
/* 1094 */     if (localInteger != null) {
/* 1095 */       int j = localInteger.intValue();
/*      */ 
/* 1097 */       label119: for (i = 0; i < paramArrayOfByte.length - j + 1; i += j) {
/* 1098 */         for (int k = i; k < i + j; k++) {
/* 1099 */           if (paramArrayOfByte[k] != 0)
/*      */           {
/*      */             break label119;
/*      */           }
/*      */         }
/* 1104 */         break;
/*      */       }
/*      */     } else {
/* 1107 */       i = paramArrayOfByte.length;
/*      */     }
/*      */ 
/* 1111 */     String str3 = new String(paramArrayOfByte, 0, i, str1);
/*      */ 
/* 1120 */     if (str2 != null)
/*      */     {
/* 1126 */       char[] arrayOfChar1 = str3.toCharArray();
/* 1127 */       char[] arrayOfChar2 = str2.toCharArray();
/* 1128 */       str3 = null;
/* 1129 */       int m = 0;
/*      */ 
/* 1132 */       for (int i1 = 0; i1 < arrayOfChar1.length; )
/*      */       {
/* 1134 */         if (i1 + arrayOfChar2.length > arrayOfChar1.length) {
/* 1135 */           arrayOfChar1[(m++)] = arrayOfChar1[(i1++)];
/*      */         }
/*      */         else
/*      */         {
/* 1139 */           int n = 1;
/* 1140 */           int i2 = 0; for (int i3 = i1; i2 < arrayOfChar2.length; i3++) {
/* 1141 */             if (arrayOfChar2[i2] != arrayOfChar1[i3]) {
/* 1142 */               n = 0;
/* 1143 */               break;
/*      */             }
/* 1140 */             i2++;
/*      */           }
/*      */ 
/* 1146 */           if (n != 0) {
/* 1147 */             arrayOfChar1[(m++)] = '\n';
/* 1148 */             i1 += arrayOfChar2.length;
/*      */           } else {
/* 1150 */             arrayOfChar1[(m++)] = arrayOfChar1[(i1++)];
/*      */           }
/*      */         }
/*      */       }
/* 1153 */       str3 = new String(arrayOfChar1, 0, m);
/*      */     }
/*      */ 
/* 1156 */     return str3;
/*      */   }
/*      */ 
/*      */   public byte[] translateTransferable(Transferable paramTransferable, DataFlavor paramDataFlavor, long paramLong)
/*      */     throws IOException
/*      */   {
/*      */     Object localObject1;
/*      */     int i;
/*      */     try
/*      */     {
/* 1180 */       localObject1 = paramTransferable.getTransferData(paramDataFlavor);
/* 1181 */       if (localObject1 == null) {
/* 1182 */         return null;
/*      */       }
/* 1184 */       if ((paramDataFlavor.equals(DataFlavor.plainTextFlavor)) && (!(localObject1 instanceof InputStream)))
/*      */       {
/* 1187 */         localObject1 = paramTransferable.getTransferData(DataFlavor.stringFlavor);
/* 1188 */         if (localObject1 == null) {
/* 1189 */           return null;
/*      */         }
/* 1191 */         i = 1;
/*      */       } else {
/* 1193 */         i = 0;
/*      */       }
/*      */     } catch (UnsupportedFlavorException localUnsupportedFlavorException) {
/* 1196 */       throw new IOException(localUnsupportedFlavorException.getMessage());
/*      */     }
/*      */ 
/* 1201 */     if ((i != 0) || ((String.class.equals(paramDataFlavor.getRepresentationClass())) && (isFlavorCharsetTextType(paramDataFlavor)) && (isTextFormat(paramLong))))
/*      */     {
/* 1205 */       localObject2 = removeSuspectedData(paramDataFlavor, paramTransferable, (String)localObject1);
/*      */ 
/* 1207 */       return translateTransferableString((String)localObject2, paramLong);
/*      */     }
/*      */ 
/* 1213 */     if (paramDataFlavor.isRepresentationClassReader()) {
/* 1214 */       if ((!isFlavorCharsetTextType(paramDataFlavor)) || (!isTextFormat(paramLong))) {
/* 1215 */         throw new IOException("cannot transfer non-text data as Reader");
/*      */       }
/*      */ 
/* 1219 */       localObject2 = (Reader)localObject1;
/* 1220 */       StringBuffer localStringBuffer = new StringBuffer();
/*      */       int k;
/* 1222 */       while ((k = ((Reader)localObject2).read()) != -1) {
/* 1223 */         localStringBuffer.append((char)k);
/*      */       }
/* 1225 */       ((Reader)localObject2).close();
/*      */ 
/* 1227 */       return translateTransferableString(localStringBuffer.toString(), paramLong);
/*      */     }
/*      */     int j;
/*      */     Object localObject4;
/* 1232 */     if (paramDataFlavor.isRepresentationClassCharBuffer()) {
/* 1233 */       if ((!isFlavorCharsetTextType(paramDataFlavor)) || (!isTextFormat(paramLong))) {
/* 1234 */         throw new IOException("cannot transfer non-text data as CharBuffer");
/*      */       }
/*      */ 
/* 1238 */       localObject2 = (CharBuffer)localObject1;
/* 1239 */       j = ((CharBuffer)localObject2).remaining();
/* 1240 */       localObject4 = new char[j];
/* 1241 */       ((CharBuffer)localObject2).get((char[])localObject4, 0, j);
/*      */ 
/* 1243 */       return translateTransferableString(new String((char[])localObject4), paramLong);
/*      */     }
/*      */ 
/* 1248 */     if (charArrayClass.equals(paramDataFlavor.getRepresentationClass())) {
/* 1249 */       if ((!isFlavorCharsetTextType(paramDataFlavor)) || (!isTextFormat(paramLong))) {
/* 1250 */         throw new IOException("cannot transfer non-text data as char array");
/*      */       }
/*      */ 
/* 1254 */       return translateTransferableString(new String((char[])localObject1), paramLong);
/*      */     }
/*      */     Object localObject5;
/* 1261 */     if (paramDataFlavor.isRepresentationClassByteBuffer()) {
/* 1262 */       localObject2 = (ByteBuffer)localObject1;
/* 1263 */       j = ((ByteBuffer)localObject2).remaining();
/* 1264 */       localObject4 = new byte[j];
/* 1265 */       ((ByteBuffer)localObject2).get((byte[])localObject4, 0, j);
/*      */ 
/* 1267 */       if ((isFlavorCharsetTextType(paramDataFlavor)) && (isTextFormat(paramLong))) {
/* 1268 */         localObject5 = getTextCharset(paramDataFlavor);
/* 1269 */         return translateTransferableString(new String((byte[])localObject4, (String)localObject5), paramLong);
/*      */       }
/*      */ 
/* 1273 */       return localObject4;
/*      */     }
/*      */ 
/* 1279 */     if (byteArrayClass.equals(paramDataFlavor.getRepresentationClass())) {
/* 1280 */       localObject2 = (byte[])localObject1;
/*      */ 
/* 1282 */       if ((isFlavorCharsetTextType(paramDataFlavor)) && (isTextFormat(paramLong))) {
/* 1283 */         localObject3 = getTextCharset(paramDataFlavor);
/* 1284 */         return translateTransferableString(new String((byte[])localObject2, (String)localObject3), paramLong);
/*      */       }
/*      */ 
/* 1288 */       return localObject2;
/*      */     }
/*      */ 
/* 1291 */     if (DataFlavor.imageFlavor.equals(paramDataFlavor)) {
/* 1292 */       if (!isImageFormat(paramLong)) {
/* 1293 */         throw new IOException("Data translation failed: not an image format");
/*      */       }
/*      */ 
/* 1297 */       localObject2 = (Image)localObject1;
/* 1298 */       localObject3 = imageToPlatformBytes((Image)localObject2, paramLong);
/*      */ 
/* 1300 */       if (localObject3 == null) {
/* 1301 */         throw new IOException("Data translation failed: cannot convert java image to native format");
/*      */       }
/*      */ 
/* 1304 */       return localObject3;
/*      */     }
/*      */ 
/* 1307 */     Object localObject2 = new ByteArrayOutputStream();
/*      */ 
/* 1311 */     if (isFileFormat(paramLong)) {
/* 1312 */       if (!DataFlavor.javaFileListFlavor.equals(paramDataFlavor)) {
/* 1313 */         throw new IOException("data translation failed");
/*      */       }
/*      */ 
/* 1316 */       localObject3 = (List)localObject1;
/*      */ 
/* 1318 */       localObject4 = getUserProtectionDomain(paramTransferable);
/*      */ 
/* 1320 */       localObject5 = castToFiles((List)localObject3, (ProtectionDomain)localObject4);
/*      */ 
/* 1322 */       localObject2 = convertFileListToBytes((ArrayList)localObject5);
/*      */     }
/*      */     else
/*      */     {
/*      */       Object localObject6;
/*      */       Object localObject7;
/* 1327 */       if (isURIListFormat(paramLong)) {
/* 1328 */         if (!DataFlavor.javaFileListFlavor.equals(paramDataFlavor)) {
/* 1329 */           throw new IOException("data translation failed");
/*      */         }
/* 1331 */         localObject3 = getNativeForFormat(paramLong);
/* 1332 */         localObject4 = null;
/* 1333 */         if (localObject3 != null) {
/*      */           try {
/* 1335 */             localObject4 = new DataFlavor((String)localObject3).getParameter("charset");
/*      */           } catch (ClassNotFoundException localClassNotFoundException) {
/* 1337 */             throw new IOException(localClassNotFoundException);
/*      */           }
/*      */         }
/* 1340 */         if (localObject4 == null) {
/* 1341 */           localObject4 = "UTF-8";
/*      */         }
/* 1343 */         List localList = (List)localObject1;
/* 1344 */         localObject6 = getUserProtectionDomain(paramTransferable);
/* 1345 */         ArrayList localArrayList = castToFiles(localList, (ProtectionDomain)localObject6);
/* 1346 */         localObject7 = new ArrayList(localArrayList.size());
/* 1347 */         for (Object localObject8 = localArrayList.iterator(); ((Iterator)localObject8).hasNext(); ) { String str = (String)((Iterator)localObject8).next();
/* 1348 */           localObject9 = new File(str).toURI();
/*      */           try
/*      */           {
/* 1351 */             ((ArrayList)localObject7).add(new URI(((URI)localObject9).getScheme(), "", ((URI)localObject9).getPath(), ((URI)localObject9).getFragment()).toString());
/*      */           } catch (URISyntaxException localURISyntaxException) {
/* 1353 */             throw new IOException(localURISyntaxException);
/*      */           }
/*      */         }
/*      */         Object localObject9;
/* 1357 */         localObject8 = "\r\n".getBytes((String)localObject4);
/* 1358 */         for (int i2 = 0; i2 < ((ArrayList)localObject7).size(); i2++) {
/* 1359 */           localObject9 = ((String)((ArrayList)localObject7).get(i2)).getBytes((String)localObject4);
/* 1360 */           ((ByteArrayOutputStream)localObject2).write((byte[])localObject9, 0, localObject9.length);
/* 1361 */           ((ByteArrayOutputStream)localObject2).write((byte[])localObject8, 0, localObject8.length);
/*      */         }
/*      */ 
/*      */       }
/* 1367 */       else if (paramDataFlavor.isRepresentationClassInputStream()) {
/* 1368 */         localObject3 = (InputStream)localObject1;
/* 1369 */         int m = 0;
/* 1370 */         int n = ((InputStream)localObject3).available();
/* 1371 */         localObject6 = new byte[n > 8192 ? n : 8192];
/*      */         do
/*      */         {
/*      */           int i1;
/* 1374 */           if ((m = (i1 = ((InputStream)localObject3).read((byte[])localObject6, 0, localObject6.length)) == -1 ? 1 : 0) == 0)
/* 1375 */             ((ByteArrayOutputStream)localObject2).write((byte[])localObject6, 0, i1);
/*      */         }
/* 1377 */         while (m == 0);
/* 1378 */         ((InputStream)localObject3).close();
/*      */ 
/* 1380 */         if ((isFlavorCharsetTextType(paramDataFlavor)) && (isTextFormat(paramLong))) {
/* 1381 */           byte[] arrayOfByte = ((ByteArrayOutputStream)localObject2).toByteArray();
/* 1382 */           ((ByteArrayOutputStream)localObject2).close();
/* 1383 */           localObject7 = getTextCharset(paramDataFlavor);
/* 1384 */           return translateTransferableString(new String(arrayOfByte, (String)localObject7), paramLong);
/*      */         }
/*      */ 
/*      */       }
/* 1390 */       else if (paramDataFlavor.isRepresentationClassRemote()) {
/* 1391 */         localObject3 = RMI.newMarshalledObject(localObject1);
/* 1392 */         ObjectOutputStream localObjectOutputStream = new ObjectOutputStream((OutputStream)localObject2);
/* 1393 */         localObjectOutputStream.writeObject(localObject3);
/* 1394 */         localObjectOutputStream.close();
/*      */       }
/* 1397 */       else if (paramDataFlavor.isRepresentationClassSerializable()) {
/* 1398 */         localObject3 = new ObjectOutputStream((OutputStream)localObject2);
/* 1399 */         ((ObjectOutputStream)localObject3).writeObject(localObject1);
/* 1400 */         ((ObjectOutputStream)localObject3).close();
/*      */       }
/*      */       else {
/* 1403 */         throw new IOException("data translation failed");
/*      */       }
/*      */     }
/* 1406 */     Object localObject3 = ((ByteArrayOutputStream)localObject2).toByteArray();
/* 1407 */     ((ByteArrayOutputStream)localObject2).close();
/* 1408 */     return localObject3;
/*      */   }
/*      */ 
/*      */   protected abstract ByteArrayOutputStream convertFileListToBytes(ArrayList<String> paramArrayList)
/*      */     throws IOException;
/*      */ 
/*      */   private String removeSuspectedData(DataFlavor paramDataFlavor, Transferable paramTransferable, final String paramString) throws IOException
/*      */   {
/* 1416 */     if ((null == System.getSecurityManager()) || (!paramDataFlavor.isMimeTypeEqual("text/uri-list")))
/*      */     {
/* 1419 */       return paramString;
/*      */     }
/*      */ 
/* 1423 */     String str = "";
/* 1424 */     final ProtectionDomain localProtectionDomain = getUserProtectionDomain(paramTransferable);
/*      */     try
/*      */     {
/* 1427 */       str = (String)AccessController.doPrivileged(new PrivilegedExceptionAction()
/*      */       {
/*      */         public Object run() {
/* 1430 */           StringBuffer localStringBuffer = new StringBuffer(paramString.length());
/* 1431 */           String[] arrayOfString1 = paramString.split("(\\s)+");
/*      */ 
/* 1433 */           for (String str : arrayOfString1)
/*      */           {
/* 1435 */             File localFile = new File(str);
/* 1436 */             if ((localFile.exists()) && (!DataTransferer.isFileInWebstartedCache(localFile)) && (!DataTransferer.this.isForbiddenToRead(localFile, localProtectionDomain)))
/*      */             {
/* 1441 */               if (0 != localStringBuffer.length())
/*      */               {
/* 1443 */                 localStringBuffer.append("\\r\\n");
/*      */               }
/*      */ 
/* 1446 */               localStringBuffer.append(str);
/*      */             }
/*      */           }
/*      */ 
/* 1450 */           return localStringBuffer.toString();
/*      */         } } );
/*      */     }
/*      */     catch (PrivilegedActionException localPrivilegedActionException) {
/* 1454 */       throw new IOException(localPrivilegedActionException.getMessage(), localPrivilegedActionException);
/*      */     }
/*      */ 
/* 1457 */     return str;
/*      */   }
/*      */ 
/*      */   private static ProtectionDomain getUserProtectionDomain(Transferable paramTransferable) {
/* 1461 */     return paramTransferable.getClass().getProtectionDomain();
/*      */   }
/*      */ 
/*      */   private boolean isForbiddenToRead(File paramFile, ProtectionDomain paramProtectionDomain)
/*      */   {
/* 1466 */     if (null == paramProtectionDomain)
/* 1467 */       return false;
/*      */     try
/*      */     {
/* 1470 */       FilePermission localFilePermission = new FilePermission(paramFile.getCanonicalPath(), "read, delete");
/*      */ 
/* 1472 */       if (paramProtectionDomain.implies(localFilePermission))
/* 1473 */         return false;
/*      */     }
/*      */     catch (IOException localIOException) {
/*      */     }
/* 1477 */     return true;
/*      */   }
/*      */ 
/*      */   private ArrayList<String> castToFiles(final List paramList, final ProtectionDomain paramProtectionDomain)
/*      */     throws IOException
/*      */   {
/* 1483 */     final ArrayList localArrayList = new ArrayList();
/*      */     try {
/* 1485 */       AccessController.doPrivileged(new PrivilegedExceptionAction() {
/*      */         public Object run() throws IOException {
/* 1487 */           for (Iterator localIterator = paramList.iterator(); localIterator.hasNext(); ) { Object localObject = localIterator.next();
/*      */ 
/* 1489 */             File localFile = DataTransferer.this.castToFile(localObject);
/* 1490 */             if ((localFile != null) && ((null == System.getSecurityManager()) || ((!DataTransferer.isFileInWebstartedCache(localFile)) && (!DataTransferer.this.isForbiddenToRead(localFile, paramProtectionDomain)))))
/*      */             {
/* 1495 */               localArrayList.add(localFile.getCanonicalPath());
/*      */             }
/*      */           }
/* 1498 */           return null;
/*      */         } } );
/*      */     }
/*      */     catch (PrivilegedActionException localPrivilegedActionException) {
/* 1502 */       throw new IOException(localPrivilegedActionException.getMessage());
/*      */     }
/* 1504 */     return localArrayList;
/*      */   }
/*      */ 
/*      */   private File castToFile(Object paramObject)
/*      */     throws IOException
/*      */   {
/* 1510 */     String str = null;
/* 1511 */     if ((paramObject instanceof File))
/* 1512 */       str = ((File)paramObject).getCanonicalPath();
/* 1513 */     else if ((paramObject instanceof String))
/* 1514 */       str = (String)paramObject;
/*      */     else {
/* 1516 */       return null;
/*      */     }
/* 1518 */     return new File(str);
/*      */   }
/*      */ 
/*      */   private static boolean isFileInWebstartedCache(File paramFile)
/*      */   {
/* 1533 */     if (deploymentCacheDirectoryList.isEmpty()) {
/* 1534 */       for (String str1 : DEPLOYMENT_CACHE_PROPERTIES) {
/* 1535 */         String str2 = System.getProperty(str1);
/* 1536 */         if (str2 != null)
/*      */           try {
/* 1538 */             File localFile3 = new File(str2).getCanonicalFile();
/* 1539 */             if (localFile3 != null)
/* 1540 */               deploymentCacheDirectoryList.add(localFile3);
/*      */           }
/*      */           catch (IOException localIOException)
/*      */           {
/*      */           }
/*      */       }
/*      */     }
/* 1547 */     for (??? = deploymentCacheDirectoryList.iterator(); ((Iterator)???).hasNext(); ) { File localFile1 = (File)((Iterator)???).next();
/* 1548 */       for (File localFile2 = paramFile; localFile2 != null; localFile2 = localFile2.getParentFile()) {
/* 1549 */         if (localFile2.equals(localFile1)) {
/* 1550 */           return true;
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 1555 */     return false;
/*      */   }
/*      */ 
/*      */   public Object translateBytes(byte[] paramArrayOfByte, DataFlavor paramDataFlavor, long paramLong, Transferable paramTransferable)
/*      */     throws IOException
/*      */   {
/* 1563 */     return translateBytesOrStream(null, paramArrayOfByte, paramDataFlavor, paramLong, paramTransferable);
/*      */   }
/*      */ 
/*      */   public Object translateStream(InputStream paramInputStream, DataFlavor paramDataFlavor, long paramLong, Transferable paramTransferable)
/*      */     throws IOException
/*      */   {
/* 1571 */     return translateBytesOrStream(paramInputStream, null, paramDataFlavor, paramLong, paramTransferable);
/*      */   }
/*      */ 
/*      */   protected Object translateBytesOrStream(InputStream paramInputStream, byte[] paramArrayOfByte, DataFlavor paramDataFlavor, long paramLong, Transferable paramTransferable)
/*      */     throws IOException
/*      */   {
/* 1592 */     if (paramInputStream == null)
/* 1593 */       paramInputStream = new ByteArrayInputStream(paramArrayOfByte);
/*      */     Object localObject1;
/*      */     Object localObject2;
/* 1599 */     if (isFileFormat(paramLong)) {
/* 1600 */       if (!DataFlavor.javaFileListFlavor.equals(paramDataFlavor)) {
/* 1601 */         throw new IOException("data translation failed");
/*      */       }
/* 1603 */       if (paramArrayOfByte == null) {
/* 1604 */         paramArrayOfByte = inputStreamToByteArray(paramInputStream);
/*      */       }
/* 1606 */       localObject1 = dragQueryFile(paramArrayOfByte);
/* 1607 */       if (localObject1 == null) {
/* 1608 */         paramInputStream.close();
/* 1609 */         return null;
/*      */       }
/*      */ 
/* 1613 */       localObject2 = new File[localObject1.length];
/* 1614 */       for (int i = 0; i < localObject1.length; i++) {
/* 1615 */         localObject2[i] = new File(localObject1[i]);
/*      */       }
/* 1617 */       paramInputStream.close();
/*      */ 
/* 1620 */       return Arrays.asList((Object[])localObject2);
/*      */     }
/*      */ 
/* 1624 */     if ((isURIListFormat(paramLong)) && (DataFlavor.javaFileListFlavor.equals(paramDataFlavor))) {
/*      */       try {
/* 1626 */         localObject1 = dragQueryURIs(paramInputStream, paramArrayOfByte, paramLong, paramTransferable);
/* 1627 */         if (localObject1 == null) {
/* 1628 */           return null;
/*      */         }
/* 1630 */         localObject2 = new ArrayList();
/* 1631 */         for (URI localURI : localObject1) {
/*      */           try {
/* 1633 */             ((ArrayList)localObject2).add(new File(localURI));
/*      */           }
/*      */           catch (IllegalArgumentException localIllegalArgumentException)
/*      */           {
/*      */           }
/*      */         }
/*      */ 
/* 1640 */         return localObject2;
/*      */       } finally {
/* 1642 */         paramInputStream.close();
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1647 */     if ((String.class.equals(paramDataFlavor.getRepresentationClass())) && (isFlavorCharsetTextType(paramDataFlavor)) && (isTextFormat(paramLong)))
/*      */     {
/* 1650 */       return translateBytesOrStreamToString(paramInputStream, paramArrayOfByte, paramLong, paramTransferable);
/*      */     }
/*      */ 
/* 1657 */     if (DataFlavor.plainTextFlavor.equals(paramDataFlavor)) {
/* 1658 */       return new StringReader(translateBytesOrStreamToString(paramInputStream, paramArrayOfByte, paramLong, paramTransferable));
/*      */     }
/*      */ 
/* 1666 */     if (paramDataFlavor.isRepresentationClassInputStream()) {
/* 1667 */       return translateBytesOrStreamToInputStream(paramInputStream, paramDataFlavor, paramLong, paramTransferable);
/*      */     }
/*      */ 
/* 1673 */     if (paramDataFlavor.isRepresentationClassReader()) {
/* 1674 */       if ((!isFlavorCharsetTextType(paramDataFlavor)) || (!isTextFormat(paramLong))) {
/* 1675 */         throw new IOException("cannot transfer non-text data as Reader");
/*      */       }
/*      */ 
/* 1679 */       localObject1 = (InputStream)translateBytesOrStreamToInputStream(paramInputStream, DataFlavor.plainTextFlavor, paramLong, paramTransferable);
/*      */ 
/* 1683 */       localObject2 = getTextCharset(DataFlavor.plainTextFlavor);
/*      */ 
/* 1685 */       ??? = new InputStreamReader((InputStream)localObject1, (String)localObject2);
/*      */ 
/* 1687 */       return constructFlavoredObject(???, paramDataFlavor, Reader.class);
/*      */     }
/*      */ 
/* 1690 */     if (paramDataFlavor.isRepresentationClassCharBuffer()) {
/* 1691 */       if ((!isFlavorCharsetTextType(paramDataFlavor)) || (!isTextFormat(paramLong))) {
/* 1692 */         throw new IOException("cannot transfer non-text data as CharBuffer");
/*      */       }
/*      */ 
/* 1696 */       localObject1 = CharBuffer.wrap(translateBytesOrStreamToString(paramInputStream, paramArrayOfByte, paramLong, paramTransferable));
/*      */ 
/* 1700 */       return constructFlavoredObject(localObject1, paramDataFlavor, CharBuffer.class);
/*      */     }
/*      */ 
/* 1704 */     if (charArrayClass.equals(paramDataFlavor.getRepresentationClass())) {
/* 1705 */       if ((!isFlavorCharsetTextType(paramDataFlavor)) || (!isTextFormat(paramLong))) {
/* 1706 */         throw new IOException("cannot transfer non-text data as char array");
/*      */       }
/*      */ 
/* 1710 */       return translateBytesOrStreamToString(paramInputStream, paramArrayOfByte, paramLong, paramTransferable).toCharArray();
/*      */     }
/*      */ 
/* 1718 */     if (paramDataFlavor.isRepresentationClassByteBuffer()) {
/* 1719 */       if ((isFlavorCharsetTextType(paramDataFlavor)) && (isTextFormat(paramLong))) {
/* 1720 */         paramArrayOfByte = translateBytesOrStreamToString(paramInputStream, paramArrayOfByte, paramLong, paramTransferable).getBytes(getTextCharset(paramDataFlavor));
/*      */       }
/* 1727 */       else if (paramArrayOfByte == null) {
/* 1728 */         paramArrayOfByte = inputStreamToByteArray(paramInputStream);
/*      */       }
/*      */ 
/* 1732 */       localObject1 = ByteBuffer.wrap(paramArrayOfByte);
/* 1733 */       return constructFlavoredObject(localObject1, paramDataFlavor, ByteBuffer.class);
/*      */     }
/*      */ 
/* 1739 */     if (byteArrayClass.equals(paramDataFlavor.getRepresentationClass())) {
/* 1740 */       if ((isFlavorCharsetTextType(paramDataFlavor)) && (isTextFormat(paramLong))) {
/* 1741 */         return translateBytesOrStreamToString(paramInputStream, paramArrayOfByte, paramLong, paramTransferable).getBytes(getTextCharset(paramDataFlavor));
/*      */       }
/*      */ 
/* 1748 */       return paramArrayOfByte != null ? paramArrayOfByte : inputStreamToByteArray(paramInputStream);
/*      */     }
/*      */ 
/* 1752 */     if (paramDataFlavor.isRepresentationClassRemote()) {
/*      */       try {
/* 1754 */         localObject1 = inputStreamToByteArray(paramInputStream);
/* 1755 */         localObject2 = new ObjectInputStream(new ByteArrayInputStream((byte[])localObject1));
/* 1756 */         ??? = RMI.getMarshalledObject(((ObjectInputStream)localObject2).readObject());
/* 1757 */         ((ObjectInputStream)localObject2).close();
/* 1758 */         paramInputStream.close();
/* 1759 */         return ???;
/*      */       } catch (Exception localException1) {
/* 1761 */         throw new IOException(localException1.getMessage());
/*      */       }
/*      */     }
/*      */ 
/* 1765 */     if (paramDataFlavor.isRepresentationClassSerializable()) {
/*      */       try {
/* 1767 */         byte[] arrayOfByte = inputStreamToByteArray(paramInputStream);
/* 1768 */         localObject2 = new ObjectInputStream(new ByteArrayInputStream(arrayOfByte));
/* 1769 */         ??? = ((ObjectInputStream)localObject2).readObject();
/* 1770 */         ((ObjectInputStream)localObject2).close();
/* 1771 */         paramInputStream.close();
/* 1772 */         return ???;
/*      */       } catch (Exception localException2) {
/* 1774 */         throw new IOException(localException2.getMessage());
/*      */       }
/*      */     }
/*      */ 
/* 1778 */     if (DataFlavor.imageFlavor.equals(paramDataFlavor)) {
/* 1779 */       if (!isImageFormat(paramLong)) {
/* 1780 */         throw new IOException("data translation failed");
/*      */       }
/*      */ 
/* 1783 */       Image localImage = platformImageBytesOrStreamToImage(paramInputStream, paramArrayOfByte, paramLong);
/* 1784 */       paramInputStream.close();
/* 1785 */       return localImage;
/*      */     }
/*      */ 
/* 1788 */     throw new IOException("data translation failed");
/*      */   }
/*      */ 
/*      */   private Object translateBytesOrStreamToInputStream(InputStream paramInputStream, DataFlavor paramDataFlavor, long paramLong, Transferable paramTransferable)
/*      */     throws IOException
/*      */   {
/* 1800 */     if ((isFlavorCharsetTextType(paramDataFlavor)) && (isTextFormat(paramLong))) {
/* 1801 */       paramInputStream = new ReencodingInputStream(paramInputStream, paramLong, getTextCharset(paramDataFlavor), paramTransferable);
/*      */     }
/*      */ 
/* 1806 */     return constructFlavoredObject(paramInputStream, paramDataFlavor, InputStream.class);
/*      */   }
/*      */ 
/*      */   private Object constructFlavoredObject(Object paramObject, DataFlavor paramDataFlavor, Class paramClass)
/*      */     throws IOException
/*      */   {
/* 1818 */     final Class localClass = paramDataFlavor.getRepresentationClass();
/*      */ 
/* 1820 */     if (paramClass.equals(localClass)) {
/* 1821 */       return paramObject;
/*      */     }
/* 1823 */     Constructor[] arrayOfConstructor = null;
/*      */     try
/*      */     {
/* 1826 */       arrayOfConstructor = (Constructor[])AccessController.doPrivileged(new PrivilegedAction()
/*      */       {
/*      */         public Object run() {
/* 1829 */           return localClass.getConstructors();
/*      */         } } );
/*      */     }
/*      */     catch (SecurityException localSecurityException) {
/* 1833 */       throw new IOException(localSecurityException.getMessage());
/*      */     }
/*      */ 
/* 1836 */     Constructor localConstructor = null;
/*      */ 
/* 1838 */     for (int i = 0; i < arrayOfConstructor.length; i++) {
/* 1839 */       if (Modifier.isPublic(arrayOfConstructor[i].getModifiers()))
/*      */       {
/* 1843 */         Class[] arrayOfClass = arrayOfConstructor[i].getParameterTypes();
/*      */ 
/* 1845 */         if ((arrayOfClass != null) && (arrayOfClass.length == 1) && (paramClass.equals(arrayOfClass[0])))
/*      */         {
/* 1847 */           localConstructor = arrayOfConstructor[i];
/* 1848 */           break;
/*      */         }
/*      */       }
/*      */     }
/* 1852 */     if (localConstructor == null) {
/* 1853 */       throw new IOException("can't find <init>(L" + paramClass + ";)V for class: " + localClass.getName());
/*      */     }
/*      */ 
/*      */     try
/*      */     {
/* 1858 */       return localConstructor.newInstance(new Object[] { paramObject });
/*      */     } catch (Exception localException) {
/* 1860 */       throw new IOException(localException.getMessage());
/*      */     }
/*      */   }
/*      */ 
/*      */   protected abstract String[] dragQueryFile(byte[] paramArrayOfByte);
/*      */ 
/*      */   protected URI[] dragQueryURIs(InputStream paramInputStream, byte[] paramArrayOfByte, long paramLong, Transferable paramTransferable)
/*      */     throws IOException
/*      */   {
/* 2057 */     throw new IOException(new UnsupportedOperationException("not implemented on this platform"));
/*      */   }
/*      */ 
/*      */   protected abstract Image platformImageBytesOrStreamToImage(InputStream paramInputStream, byte[] paramArrayOfByte, long paramLong)
/*      */     throws IOException;
/*      */ 
/*      */   protected Image standardImageBytesOrStreamToImage(InputStream paramInputStream, byte[] paramArrayOfByte, String paramString)
/*      */     throws IOException
/*      */   {
/* 2080 */     if (paramInputStream == null) {
/* 2081 */       paramInputStream = new ByteArrayInputStream(paramArrayOfByte);
/*      */     }
/*      */ 
/* 2084 */     Iterator localIterator = ImageIO.getImageReadersByMIMEType(paramString);
/*      */ 
/* 2086 */     if (!localIterator.hasNext()) {
/* 2087 */       throw new IOException("No registered service provider can decode  an image from " + paramString);
/*      */     }
/*      */ 
/* 2091 */     Object localObject1 = null;
/*      */ 
/* 2093 */     while (localIterator.hasNext()) {
/* 2094 */       ImageReader localImageReader = (ImageReader)localIterator.next();
/*      */       try {
/* 2096 */         ImageInputStream localImageInputStream = ImageIO.createImageInputStream(paramInputStream);
/*      */         try
/*      */         {
/* 2100 */           ImageReadParam localImageReadParam = localImageReader.getDefaultReadParam();
/* 2101 */           localImageReader.setInput(localImageInputStream, true, true);
/* 2102 */           BufferedImage localBufferedImage1 = localImageReader.read(localImageReader.getMinIndex(), localImageReadParam);
/*      */ 
/* 2104 */           if (localBufferedImage1 != null)
/* 2105 */             return localBufferedImage1;
/*      */         }
/*      */         finally {
/* 2108 */           localImageInputStream.close();
/* 2109 */           localImageReader.dispose();
/*      */         }
/*      */       } catch (IOException localIOException) {
/* 2112 */         localObject1 = localIOException;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2117 */     if (localObject1 == null) {
/* 2118 */       localObject1 = new IOException("Registered service providers failed to decode an image from " + paramString);
/*      */     }
/*      */ 
/* 2122 */     throw ((Throwable)localObject1);
/*      */   }
/*      */ 
/*      */   protected abstract byte[] imageToPlatformBytes(Image paramImage, long paramLong)
/*      */     throws IOException;
/*      */ 
/*      */   protected byte[] imageToStandardBytes(Image paramImage, String paramString)
/*      */     throws IOException
/*      */   {
/* 2140 */     Object localObject1 = null;
/*      */ 
/* 2142 */     Iterator localIterator = ImageIO.getImageWritersByMIMEType(paramString);
/*      */ 
/* 2144 */     if (!localIterator.hasNext()) {
/* 2145 */       throw new IOException("No registered service provider can encode  an image to " + paramString);
/*      */     }
/*      */ 
/* 2149 */     if ((paramImage instanceof RenderedImage)) {
/*      */       try
/*      */       {
/* 2152 */         return imageToStandardBytesImpl((RenderedImage)paramImage, paramString);
/*      */       } catch (IOException localIOException1) {
/* 2154 */         localObject1 = localIOException1;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2159 */     int i = 0;
/* 2160 */     int j = 0;
/* 2161 */     if ((paramImage instanceof ToolkitImage)) {
/* 2162 */       localObject2 = ((ToolkitImage)paramImage).getImageRep();
/* 2163 */       ((ImageRepresentation)localObject2).reconstruct(32);
/* 2164 */       i = ((ImageRepresentation)localObject2).getWidth();
/* 2165 */       j = ((ImageRepresentation)localObject2).getHeight();
/*      */     } else {
/* 2167 */       i = paramImage.getWidth(null);
/* 2168 */       j = paramImage.getHeight(null);
/*      */     }
/*      */ 
/* 2171 */     Object localObject2 = ColorModel.getRGBdefault();
/* 2172 */     WritableRaster localWritableRaster = ((ColorModel)localObject2).createCompatibleWritableRaster(i, j);
/*      */ 
/* 2175 */     BufferedImage localBufferedImage = new BufferedImage((ColorModel)localObject2, localWritableRaster, ((ColorModel)localObject2).isAlphaPremultiplied(), null);
/*      */ 
/* 2179 */     Graphics localGraphics = localBufferedImage.getGraphics();
/*      */     try {
/* 2181 */       localGraphics.drawImage(paramImage, 0, 0, i, j, null);
/*      */     } finally {
/* 2183 */       localGraphics.dispose();
/*      */     }
/*      */     try
/*      */     {
/* 2187 */       return imageToStandardBytesImpl(localBufferedImage, paramString);
/*      */     } catch (IOException localIOException2) {
/* 2189 */       if (localObject1 != null) {
/* 2190 */         throw localObject1;
/*      */       }
/* 2192 */       throw localIOException2;
/*      */     }
/*      */   }
/*      */ 
/*      */   protected byte[] imageToStandardBytesImpl(RenderedImage paramRenderedImage, String paramString)
/*      */     throws IOException
/*      */   {
/* 2201 */     Iterator localIterator = ImageIO.getImageWritersByMIMEType(paramString);
/*      */ 
/* 2203 */     ImageTypeSpecifier localImageTypeSpecifier = new ImageTypeSpecifier(paramRenderedImage);
/*      */ 
/* 2206 */     ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
/* 2207 */     Object localObject1 = null;
/*      */ 
/* 2209 */     while (localIterator.hasNext()) {
/* 2210 */       ImageWriter localImageWriter = (ImageWriter)localIterator.next();
/* 2211 */       ImageWriterSpi localImageWriterSpi = localImageWriter.getOriginatingProvider();
/*      */ 
/* 2213 */       if (localImageWriterSpi.canEncodeImage(localImageTypeSpecifier))
/*      */       {
/*      */         try
/*      */         {
/* 2218 */           ImageOutputStream localImageOutputStream = ImageIO.createImageOutputStream(localByteArrayOutputStream);
/*      */           try
/*      */           {
/* 2221 */             localImageWriter.setOutput(localImageOutputStream);
/* 2222 */             localImageWriter.write(paramRenderedImage);
/* 2223 */             localImageOutputStream.flush();
/*      */           } finally {
/* 2225 */             localImageOutputStream.close();
/*      */           }
/*      */         } catch (IOException localIOException) {
/* 2228 */           localImageWriter.dispose();
/* 2229 */           localByteArrayOutputStream.reset();
/* 2230 */           localObject1 = localIOException;
/* 2231 */         }continue;
/*      */ 
/* 2234 */         localImageWriter.dispose();
/* 2235 */         localByteArrayOutputStream.close();
/* 2236 */         return localByteArrayOutputStream.toByteArray();
/*      */       }
/*      */     }
/* 2239 */     localByteArrayOutputStream.close();
/*      */ 
/* 2241 */     if (localObject1 == null) {
/* 2242 */       localObject1 = new IOException("Registered service providers failed to encode " + paramRenderedImage + " to " + paramString);
/*      */     }
/*      */ 
/* 2246 */     throw ((Throwable)localObject1);
/*      */   }
/*      */ 
/*      */   private Object concatData(Object paramObject1, Object paramObject2)
/*      */   {
/* 2268 */     Object localObject1 = null;
/* 2269 */     Object localObject2 = null;
/*      */ 
/* 2271 */     if ((paramObject1 instanceof byte[])) {
/* 2272 */       byte[] arrayOfByte1 = (byte[])paramObject1;
/* 2273 */       if ((paramObject2 instanceof byte[])) {
/* 2274 */         byte[] arrayOfByte2 = (byte[])paramObject2;
/* 2275 */         byte[] arrayOfByte3 = new byte[arrayOfByte1.length + arrayOfByte2.length];
/* 2276 */         System.arraycopy(arrayOfByte1, 0, arrayOfByte3, 0, arrayOfByte1.length);
/* 2277 */         System.arraycopy(arrayOfByte2, 0, arrayOfByte3, arrayOfByte1.length, arrayOfByte2.length);
/* 2278 */         return arrayOfByte3;
/*      */       }
/* 2280 */       localObject1 = new ByteArrayInputStream(arrayOfByte1);
/* 2281 */       localObject2 = (InputStream)paramObject2;
/*      */     }
/*      */     else {
/* 2284 */       localObject1 = (InputStream)paramObject1;
/* 2285 */       if ((paramObject2 instanceof byte[]))
/* 2286 */         localObject2 = new ByteArrayInputStream((byte[])paramObject2);
/*      */       else {
/* 2288 */         localObject2 = (InputStream)paramObject2;
/*      */       }
/*      */     }
/*      */ 
/* 2292 */     return new SequenceInputStream((InputStream)localObject1, (InputStream)localObject2);
/*      */   }
/*      */ 
/*      */   public byte[] convertData(Object paramObject, Transferable paramTransferable, final long paramLong, final Map paramMap, boolean paramBoolean)
/*      */     throws IOException
/*      */   {
/* 2302 */     byte[] arrayOfByte = null;
/*      */     Object localObject1;
/* 2310 */     if (paramBoolean) {
/*      */       try { localObject1 = new Stack();
/* 2312 */         Runnable local6 = new Runnable()
/*      */         {
/* 2314 */           private boolean done = false;
/*      */ 
/* 2316 */           public void run() { if (this.done) {
/* 2317 */               return;
/*      */             }
/* 2319 */             byte[] arrayOfByte = null;
/*      */             try {
/* 2321 */               DataFlavor localDataFlavor = (DataFlavor)paramMap.get(Long.valueOf(paramLong));
/* 2322 */               if (localDataFlavor != null)
/* 2323 */                 arrayOfByte = DataTransferer.this.translateTransferable(this.val$contents, localDataFlavor, paramLong);
/*      */             }
/*      */             catch (Exception localException) {
/* 2326 */               localException.printStackTrace();
/* 2327 */               arrayOfByte = null;
/*      */             }
/*      */             try {
/* 2330 */               DataTransferer.this.getToolkitThreadBlockedHandler().lock();
/* 2331 */               this.val$stack.push(arrayOfByte);
/* 2332 */               DataTransferer.this.getToolkitThreadBlockedHandler().exit();
/*      */             } finally {
/* 2334 */               DataTransferer.this.getToolkitThreadBlockedHandler().unlock();
/* 2335 */               this.done = true;
/*      */             }
/*      */           }
/*      */         };
/* 2340 */         AppContext localAppContext = SunToolkit.targetToAppContext(paramObject);
/*      */ 
/* 2342 */         getToolkitThreadBlockedHandler().lock();
/*      */ 
/* 2344 */         if (localAppContext != null) {
/* 2345 */           localAppContext.put("DATA_CONVERTER_KEY", local6);
/*      */         }
/*      */ 
/* 2348 */         SunToolkit.executeOnEventHandlerThread(paramObject, local6);
/*      */ 
/* 2350 */         while (((Stack)localObject1).empty()) {
/* 2351 */           getToolkitThreadBlockedHandler().enter();
/*      */         }
/*      */ 
/* 2354 */         if (localAppContext != null) {
/* 2355 */           localAppContext.remove("DATA_CONVERTER_KEY");
/*      */         }
/*      */ 
/* 2358 */         arrayOfByte = (byte[])((Stack)localObject1).pop();
/*      */       } finally {
/* 2360 */         getToolkitThreadBlockedHandler().unlock();
/*      */       }
/*      */     } else { localObject1 = (DataFlavor)paramMap.get(Long.valueOf(paramLong));
/*      */ 
/* 2364 */       if (localObject1 != null) {
/* 2365 */         arrayOfByte = translateTransferable(paramTransferable, (DataFlavor)localObject1, paramLong);
/*      */       }
/*      */     }
/*      */ 
/* 2369 */     return arrayOfByte;
/*      */   }
/*      */ 
/*      */   public void processDataConversionRequests() {
/* 2373 */     if (EventQueue.isDispatchThread()) {
/* 2374 */       AppContext localAppContext = AppContext.getAppContext();
/* 2375 */       getToolkitThreadBlockedHandler().lock();
/*      */       try {
/* 2377 */         Runnable localRunnable = (Runnable)localAppContext.get("DATA_CONVERTER_KEY");
/*      */ 
/* 2379 */         if (localRunnable != null) {
/* 2380 */           localRunnable.run();
/* 2381 */           localAppContext.remove("DATA_CONVERTER_KEY");
/*      */         }
/*      */       } finally {
/* 2384 */         getToolkitThreadBlockedHandler().unlock();
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public abstract ToolkitThreadBlockedHandler getToolkitThreadBlockedHandler();
/*      */ 
/*      */   public static long[] keysToLongArray(SortedMap paramSortedMap)
/*      */   {
/* 2399 */     Set localSet = paramSortedMap.keySet();
/* 2400 */     long[] arrayOfLong = new long[localSet.size()];
/* 2401 */     int i = 0;
/* 2402 */     for (Iterator localIterator = localSet.iterator(); localIterator.hasNext(); i++) {
/* 2403 */       arrayOfLong[i] = ((Long)localIterator.next()).longValue();
/*      */     }
/* 2405 */     return arrayOfLong;
/*      */   }
/*      */ 
/*      */   public static DataFlavor[] setToSortedDataFlavorArray(Set paramSet)
/*      */   {
/* 2413 */     DataFlavor[] arrayOfDataFlavor = new DataFlavor[paramSet.size()];
/* 2414 */     paramSet.toArray(arrayOfDataFlavor);
/* 2415 */     DataFlavorComparator localDataFlavorComparator = new DataFlavorComparator(false);
/*      */ 
/* 2417 */     Arrays.sort(arrayOfDataFlavor, localDataFlavorComparator);
/* 2418 */     return arrayOfDataFlavor;
/*      */   }
/*      */ 
/*      */   protected static byte[] inputStreamToByteArray(InputStream paramInputStream)
/*      */     throws IOException
/*      */   {
/* 2427 */     ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
/* 2428 */     int i = 0;
/* 2429 */     byte[] arrayOfByte = new byte[8192];
/*      */ 
/* 2431 */     while ((i = paramInputStream.read(arrayOfByte)) != -1) {
/* 2432 */       localByteArrayOutputStream.write(arrayOfByte, 0, i);
/*      */     }
/*      */ 
/* 2435 */     return localByteArrayOutputStream.toByteArray();
/*      */   }
/*      */ 
/*      */   public List getPlatformMappingsForNative(String paramString)
/*      */   {
/* 2444 */     return new ArrayList();
/*      */   }
/*      */ 
/*      */   public List getPlatformMappingsForFlavor(DataFlavor paramDataFlavor)
/*      */   {
/* 2453 */     return new ArrayList();
/*      */   }
/*      */ 
/*      */   static
/*      */   {
/*  244 */     Class localClass1 = null; Class localClass2 = null;
/*      */     try {
/*  246 */       localClass1 = Class.forName("[C");
/*  247 */       localClass2 = Class.forName("[B");
/*      */     } catch (ClassNotFoundException localClassNotFoundException1) {
/*      */     }
/*  250 */     charArrayClass = localClass1;
/*  251 */     byteArrayClass = localClass2;
/*      */ 
/*  253 */     DataFlavor localDataFlavor1 = null;
/*      */     try {
/*  255 */       localDataFlavor1 = new DataFlavor("text/plain;charset=Unicode;class=java.lang.String");
/*      */     }
/*      */     catch (ClassNotFoundException localClassNotFoundException2) {
/*      */     }
/*  259 */     plainTextStringFlavor = localDataFlavor1;
/*      */ 
/*  261 */     DataFlavor localDataFlavor2 = null;
/*      */     try {
/*  263 */       localDataFlavor2 = new DataFlavor("application/x-java-text-encoding;class=\"[B\"");
/*      */     }
/*      */     catch (ClassNotFoundException localClassNotFoundException3) {
/*      */     }
/*  267 */     javaTextEncodingFlavor = localDataFlavor2;
/*      */ 
/*  269 */     HashMap localHashMap = new HashMap(17);
/*  270 */     localHashMap.put("sgml", Boolean.TRUE);
/*  271 */     localHashMap.put("xml", Boolean.TRUE);
/*  272 */     localHashMap.put("html", Boolean.TRUE);
/*  273 */     localHashMap.put("enriched", Boolean.TRUE);
/*  274 */     localHashMap.put("richtext", Boolean.TRUE);
/*  275 */     localHashMap.put("uri-list", Boolean.TRUE);
/*  276 */     localHashMap.put("directory", Boolean.TRUE);
/*  277 */     localHashMap.put("css", Boolean.TRUE);
/*  278 */     localHashMap.put("calendar", Boolean.TRUE);
/*  279 */     localHashMap.put("plain", Boolean.TRUE);
/*  280 */     localHashMap.put("rtf", Boolean.FALSE);
/*  281 */     localHashMap.put("tab-separated-values", Boolean.FALSE);
/*  282 */     localHashMap.put("t140", Boolean.FALSE);
/*  283 */     localHashMap.put("rfc822-headers", Boolean.FALSE);
/*  284 */     localHashMap.put("parityfec", Boolean.FALSE);
/*      */   }
/*      */ 
/*      */   public static class CharsetComparator extends DataTransferer.IndexedComparator
/*      */   {
/* 2580 */     private static final Map charsets = Collections.unmodifiableMap(localHashMap);
/*      */     private static String defaultEncoding;
/* 2552 */     private static final Integer DEFAULT_CHARSET_INDEX = Integer.valueOf(2);
/* 2553 */     private static final Integer OTHER_CHARSET_INDEX = Integer.valueOf(1);
/* 2554 */     private static final Integer WORST_CHARSET_INDEX = Integer.valueOf(0);
/* 2555 */     private static final Integer UNSUPPORTED_CHARSET_INDEX = Integer.valueOf(-2147483648);
/*      */     private static final String UNSUPPORTED_CHARSET = "UNSUPPORTED";
/*      */ 
/*      */     public CharsetComparator()
/*      */     {
/* 2584 */       this(true);
/*      */     }
/*      */ 
/*      */     public CharsetComparator(boolean paramBoolean) {
/* 2588 */       super();
/*      */     }
/*      */ 
/*      */     public int compare(Object paramObject1, Object paramObject2)
/*      */     {
/* 2607 */       String str1 = null;
/* 2608 */       String str2 = null;
/* 2609 */       if (this.order == true) {
/* 2610 */         str1 = (String)paramObject1;
/* 2611 */         str2 = (String)paramObject2;
/*      */       } else {
/* 2613 */         str1 = (String)paramObject2;
/* 2614 */         str2 = (String)paramObject1;
/*      */       }
/*      */ 
/* 2617 */       return compareCharsets(str1, str2);
/*      */     }
/*      */ 
/*      */     protected int compareCharsets(String paramString1, String paramString2)
/*      */     {
/* 2644 */       paramString1 = getEncoding(paramString1);
/* 2645 */       paramString2 = getEncoding(paramString2);
/*      */ 
/* 2647 */       int i = compareIndices(charsets, paramString1, paramString2, OTHER_CHARSET_INDEX);
/*      */ 
/* 2650 */       if (i == 0) {
/* 2651 */         return paramString2.compareTo(paramString1);
/*      */       }
/*      */ 
/* 2654 */       return i;
/*      */     }
/*      */ 
/*      */     protected static String getEncoding(String paramString)
/*      */     {
/* 2674 */       if (paramString == null)
/* 2675 */         return null;
/* 2676 */       if (!DataTransferer.isEncodingSupported(paramString)) {
/* 2677 */         return "UNSUPPORTED";
/*      */       }
/*      */ 
/* 2683 */       String str = DataTransferer.canonicalName(paramString);
/* 2684 */       return charsets.containsKey(str) ? str : paramString;
/*      */     }
/*      */ 
/*      */     static
/*      */     {
/* 2561 */       HashMap localHashMap = new HashMap(8, 1.0F);
/*      */ 
/* 2564 */       localHashMap.put(DataTransferer.canonicalName("UTF-16LE"), Integer.valueOf(4));
/* 2565 */       localHashMap.put(DataTransferer.canonicalName("UTF-16BE"), Integer.valueOf(5));
/* 2566 */       localHashMap.put(DataTransferer.canonicalName("UTF-8"), Integer.valueOf(6));
/* 2567 */       localHashMap.put(DataTransferer.canonicalName("UTF-16"), Integer.valueOf(7));
/*      */ 
/* 2570 */       localHashMap.put(DataTransferer.canonicalName("US-ASCII"), WORST_CHARSET_INDEX);
/*      */ 
/* 2572 */       String str = DataTransferer.canonicalName(DataTransferer.getDefaultTextCharset());
/*      */ 
/* 2575 */       if (localHashMap.get(defaultEncoding) == null) {
/* 2576 */         localHashMap.put(defaultEncoding, DEFAULT_CHARSET_INDEX);
/*      */       }
/* 2578 */       localHashMap.put("UNSUPPORTED", UNSUPPORTED_CHARSET_INDEX);
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class DataFlavorComparator extends DataTransferer.IndexedComparator
/*      */   {
/*      */     private final DataTransferer.CharsetComparator charsetComparator;
/*      */     private static final Map exactTypes;
/*      */     private static final Map primaryTypes;
/*      */     private static final Map nonTextRepresentations;
/*      */     private static final Map textTypes;
/*      */     private static final Map decodedTextRepresentations;
/* 2824 */     private static final Map encodedTextRepresentations = Collections.unmodifiableMap(localHashMap);
/*      */ 
/* 2712 */     private static final Integer UNKNOWN_OBJECT_LOSES = Integer.valueOf(-2147483648);
/*      */ 
/* 2714 */     private static final Integer UNKNOWN_OBJECT_WINS = Integer.valueOf(2147483647);
/*      */ 
/* 2717 */     private static final Long UNKNOWN_OBJECT_LOSES_L = Long.valueOf(-9223372036854775808L);
/*      */ 
/* 2719 */     private static final Long UNKNOWN_OBJECT_WINS_L = Long.valueOf(9223372036854775807L);
/*      */ 
/*      */     public DataFlavorComparator()
/*      */     {
/* 2830 */       this(true);
/*      */     }
/*      */ 
/*      */     public DataFlavorComparator(boolean paramBoolean) {
/* 2834 */       super();
/*      */ 
/* 2836 */       this.charsetComparator = new DataTransferer.CharsetComparator(paramBoolean);
/*      */     }
/*      */ 
/*      */     public int compare(Object paramObject1, Object paramObject2) {
/* 2840 */       DataFlavor localDataFlavor1 = null;
/* 2841 */       DataFlavor localDataFlavor2 = null;
/* 2842 */       if (this.order == true) {
/* 2843 */         localDataFlavor1 = (DataFlavor)paramObject1;
/* 2844 */         localDataFlavor2 = (DataFlavor)paramObject2;
/*      */       } else {
/* 2846 */         localDataFlavor1 = (DataFlavor)paramObject2;
/* 2847 */         localDataFlavor2 = (DataFlavor)paramObject1;
/*      */       }
/*      */ 
/* 2850 */       if (localDataFlavor1.equals(localDataFlavor2)) {
/* 2851 */         return 0;
/*      */       }
/*      */ 
/* 2854 */       int i = 0;
/*      */ 
/* 2856 */       String str1 = localDataFlavor1.getPrimaryType();
/* 2857 */       String str2 = localDataFlavor1.getSubType();
/* 2858 */       String str3 = str1 + "/" + str2;
/* 2859 */       Class localClass1 = localDataFlavor1.getRepresentationClass();
/*      */ 
/* 2861 */       String str4 = localDataFlavor2.getPrimaryType();
/* 2862 */       String str5 = localDataFlavor2.getSubType();
/* 2863 */       String str6 = str4 + "/" + str5;
/* 2864 */       Class localClass2 = localDataFlavor2.getRepresentationClass();
/*      */ 
/* 2866 */       if ((localDataFlavor1.isFlavorTextType()) && (localDataFlavor2.isFlavorTextType()))
/*      */       {
/* 2868 */         i = compareIndices(textTypes, str3, str6, UNKNOWN_OBJECT_LOSES);
/*      */ 
/* 2870 */         if (i != 0) {
/* 2871 */           return i;
/*      */         }
/*      */ 
/* 2881 */         if (DataTransferer.doesSubtypeSupportCharset(localDataFlavor1))
/*      */         {
/* 2884 */           i = compareIndices(decodedTextRepresentations, localClass1, localClass2, UNKNOWN_OBJECT_LOSES);
/*      */ 
/* 2886 */           if (i != 0) {
/* 2887 */             return i;
/*      */           }
/*      */ 
/* 2891 */           i = this.charsetComparator.compareCharsets(DataTransferer.getTextCharset(localDataFlavor1), DataTransferer.getTextCharset(localDataFlavor2));
/*      */ 
/* 2894 */           if (i != 0) {
/* 2895 */             return i;
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/* 2901 */         i = compareIndices(encodedTextRepresentations, localClass1, localClass2, UNKNOWN_OBJECT_LOSES);
/*      */ 
/* 2903 */         if (i != 0)
/* 2904 */           return i;
/*      */       }
/*      */       else
/*      */       {
/* 2908 */         i = compareIndices(primaryTypes, str1, str4, UNKNOWN_OBJECT_LOSES);
/*      */ 
/* 2910 */         if (i != 0) {
/* 2911 */           return i;
/*      */         }
/*      */ 
/* 2917 */         i = compareIndices(exactTypes, str3, str6, UNKNOWN_OBJECT_WINS);
/*      */ 
/* 2919 */         if (i != 0) {
/* 2920 */           return i;
/*      */         }
/*      */ 
/* 2925 */         i = compareIndices(nonTextRepresentations, localClass1, localClass2, UNKNOWN_OBJECT_LOSES);
/*      */ 
/* 2927 */         if (i != 0) {
/* 2928 */           return i;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 2934 */       return localDataFlavor1.getMimeType().compareTo(localDataFlavor2.getMimeType());
/*      */     }
/*      */ 
/*      */     static
/*      */     {
/* 2724 */       HashMap localHashMap = new HashMap(4, 1.0F);
/*      */ 
/* 2727 */       localHashMap.put("application/x-java-file-list", Integer.valueOf(0));
/*      */ 
/* 2729 */       localHashMap.put("application/x-java-serialized-object", Integer.valueOf(1));
/*      */ 
/* 2731 */       localHashMap.put("application/x-java-jvm-local-objectref", Integer.valueOf(2));
/*      */ 
/* 2733 */       localHashMap.put("application/x-java-remote-object", Integer.valueOf(3));
/*      */ 
/* 2736 */       exactTypes = Collections.unmodifiableMap(localHashMap);
/*      */ 
/* 2740 */       localHashMap = new HashMap(1, 1.0F);
/*      */ 
/* 2742 */       localHashMap.put("application", Integer.valueOf(0));
/*      */ 
/* 2744 */       primaryTypes = Collections.unmodifiableMap(localHashMap);
/*      */ 
/* 2748 */       localHashMap = new HashMap(3, 1.0F);
/*      */ 
/* 2750 */       localHashMap.put(InputStream.class, Integer.valueOf(0));
/*      */ 
/* 2752 */       localHashMap.put(Serializable.class, Integer.valueOf(1));
/*      */ 
/* 2755 */       Class localClass = DataTransferer.RMI.remoteClass();
/* 2756 */       if (localClass != null) {
/* 2757 */         localHashMap.put(localClass, Integer.valueOf(2));
/*      */       }
/*      */ 
/* 2761 */       nonTextRepresentations = Collections.unmodifiableMap(localHashMap);
/*      */ 
/* 2766 */       localHashMap = new HashMap(16, 1.0F);
/*      */ 
/* 2769 */       localHashMap.put("text/plain", Integer.valueOf(0));
/*      */ 
/* 2772 */       localHashMap.put("application/x-java-serialized-object", Integer.valueOf(1));
/*      */ 
/* 2776 */       localHashMap.put("text/calendar", Integer.valueOf(2));
/* 2777 */       localHashMap.put("text/css", Integer.valueOf(3));
/* 2778 */       localHashMap.put("text/directory", Integer.valueOf(4));
/* 2779 */       localHashMap.put("text/parityfec", Integer.valueOf(5));
/* 2780 */       localHashMap.put("text/rfc822-headers", Integer.valueOf(6));
/* 2781 */       localHashMap.put("text/t140", Integer.valueOf(7));
/* 2782 */       localHashMap.put("text/tab-separated-values", Integer.valueOf(8));
/* 2783 */       localHashMap.put("text/uri-list", Integer.valueOf(9));
/*      */ 
/* 2786 */       localHashMap.put("text/richtext", Integer.valueOf(10));
/* 2787 */       localHashMap.put("text/enriched", Integer.valueOf(11));
/* 2788 */       localHashMap.put("text/rtf", Integer.valueOf(12));
/*      */ 
/* 2791 */       localHashMap.put("text/html", Integer.valueOf(13));
/* 2792 */       localHashMap.put("text/xml", Integer.valueOf(14));
/* 2793 */       localHashMap.put("text/sgml", Integer.valueOf(15));
/*      */ 
/* 2795 */       textTypes = Collections.unmodifiableMap(localHashMap);
/*      */ 
/* 2799 */       localHashMap = new HashMap(4, 1.0F);
/*      */ 
/* 2801 */       localHashMap.put(DataTransferer.charArrayClass, Integer.valueOf(0));
/*      */ 
/* 2803 */       localHashMap.put(CharBuffer.class, Integer.valueOf(1));
/*      */ 
/* 2805 */       localHashMap.put(String.class, Integer.valueOf(2));
/*      */ 
/* 2807 */       localHashMap.put(Reader.class, Integer.valueOf(3));
/*      */ 
/* 2810 */       decodedTextRepresentations = Collections.unmodifiableMap(localHashMap);
/*      */ 
/* 2815 */       localHashMap = new HashMap(3, 1.0F);
/*      */ 
/* 2817 */       localHashMap.put(DataTransferer.byteArrayClass, Integer.valueOf(0));
/*      */ 
/* 2819 */       localHashMap.put(ByteBuffer.class, Integer.valueOf(1));
/*      */ 
/* 2821 */       localHashMap.put(InputStream.class, Integer.valueOf(2));
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class IndexOrderComparator extends DataTransferer.IndexedComparator
/*      */   {
/*      */     private final Map indexMap;
/* 2953 */     private static final Integer FALLBACK_INDEX = Integer.valueOf(-2147483648);
/*      */ 
/*      */     public IndexOrderComparator(Map paramMap)
/*      */     {
/* 2957 */       super();
/* 2958 */       this.indexMap = paramMap;
/*      */     }
/*      */ 
/*      */     public IndexOrderComparator(Map paramMap, boolean paramBoolean) {
/* 2962 */       super();
/* 2963 */       this.indexMap = paramMap;
/*      */     }
/*      */ 
/*      */     public int compare(Object paramObject1, Object paramObject2) {
/* 2967 */       if (!this.order) {
/* 2968 */         return -compareIndices(this.indexMap, paramObject1, paramObject2, FALLBACK_INDEX);
/*      */       }
/* 2970 */       return compareIndices(this.indexMap, paramObject1, paramObject2, FALLBACK_INDEX);
/*      */     }
/*      */   }
/*      */ 
/*      */   public static abstract class IndexedComparator
/*      */     implements Comparator
/*      */   {
/*      */     public static final boolean SELECT_BEST = true;
/*      */     public static final boolean SELECT_WORST = false;
/*      */     protected final boolean order;
/*      */ 
/*      */     public IndexedComparator()
/*      */     {
/* 2475 */       this(true);
/*      */     }
/*      */ 
/*      */     public IndexedComparator(boolean paramBoolean) {
/* 2479 */       this.order = paramBoolean;
/*      */     }
/*      */ 
/*      */     protected static int compareIndices(Map paramMap, Object paramObject1, Object paramObject2, Integer paramInteger)
/*      */     {
/* 2498 */       Integer localInteger1 = (Integer)paramMap.get(paramObject1);
/* 2499 */       Integer localInteger2 = (Integer)paramMap.get(paramObject2);
/*      */ 
/* 2501 */       if (localInteger1 == null) {
/* 2502 */         localInteger1 = paramInteger;
/*      */       }
/* 2504 */       if (localInteger2 == null) {
/* 2505 */         localInteger2 = paramInteger;
/*      */       }
/*      */ 
/* 2508 */       return localInteger1.compareTo(localInteger2);
/*      */     }
/*      */ 
/*      */     protected static int compareLongs(Map paramMap, Object paramObject1, Object paramObject2, Long paramLong)
/*      */     {
/* 2527 */       Long localLong1 = (Long)paramMap.get(paramObject1);
/* 2528 */       Long localLong2 = (Long)paramMap.get(paramObject2);
/*      */ 
/* 2530 */       if (localLong1 == null) {
/* 2531 */         localLong1 = paramLong;
/*      */       }
/* 2533 */       if (localLong2 == null) {
/* 2534 */         localLong2 = paramLong;
/*      */       }
/*      */ 
/* 2537 */       return localLong1.compareTo(localLong2);
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class RMI
/*      */   {
/* 2980 */     private static final Class<?> remoteClass = getClass("java.rmi.Remote");
/* 2981 */     private static final Class<?> marshallObjectClass = getClass("java.rmi.MarshalledObject");
/*      */ 
/* 2983 */     private static final Constructor<?> marshallCtor = getConstructor(marshallObjectClass, new Class[] { Object.class });
/*      */ 
/* 2985 */     private static final Method marshallGet = getMethod(marshallObjectClass, "get", new Class[0]);
/*      */ 
/*      */     private static Class<?> getClass(String paramString)
/*      */     {
/*      */       try {
/* 2990 */         return Class.forName(paramString, true, null); } catch (ClassNotFoundException localClassNotFoundException) {
/*      */       }
/* 2992 */       return null;
/*      */     }
/*      */ 
/*      */     private static Constructor<?> getConstructor(Class<?> paramClass, Class<?>[] paramArrayOfClass)
/*      */     {
/*      */       try {
/* 2998 */         return paramClass == null ? null : paramClass.getDeclaredConstructor(paramArrayOfClass);
/*      */       } catch (NoSuchMethodException localNoSuchMethodException) {
/* 3000 */         throw new AssertionError(localNoSuchMethodException);
/*      */       }
/*      */     }
/*      */ 
/*      */     private static Method getMethod(Class<?> paramClass, String paramString, Class<?>[] paramArrayOfClass) {
/*      */       try {
/* 3006 */         return paramClass == null ? null : paramClass.getMethod(paramString, paramArrayOfClass);
/*      */       } catch (NoSuchMethodException localNoSuchMethodException) {
/* 3008 */         throw new AssertionError(localNoSuchMethodException);
/*      */       }
/*      */     }
/*      */ 
/*      */     static boolean isRemote(Class<?> paramClass)
/*      */     {
/* 3016 */       return (remoteClass == null ? null : Boolean.valueOf(remoteClass.isAssignableFrom(paramClass))).booleanValue();
/*      */     }
/*      */ 
/*      */     static Class<?> remoteClass()
/*      */     {
/* 3023 */       return remoteClass;
/*      */     }
/*      */ 
/*      */     static Object newMarshalledObject(Object paramObject)
/*      */       throws IOException
/*      */     {
/*      */       try
/*      */       {
/* 3032 */         return marshallCtor.newInstance(new Object[] { paramObject });
/*      */       } catch (InstantiationException localInstantiationException) {
/* 3034 */         throw new AssertionError(localInstantiationException);
/*      */       } catch (IllegalAccessException localIllegalAccessException) {
/* 3036 */         throw new AssertionError(localIllegalAccessException);
/*      */       } catch (InvocationTargetException localInvocationTargetException) {
/* 3038 */         Throwable localThrowable = localInvocationTargetException.getCause();
/* 3039 */         if ((localThrowable instanceof IOException))
/* 3040 */           throw ((IOException)localThrowable);
/* 3041 */         throw new AssertionError(localInvocationTargetException);
/*      */       }
/*      */     }
/*      */ 
/*      */     static Object getMarshalledObject(Object paramObject)
/*      */       throws IOException, ClassNotFoundException
/*      */     {
/*      */       try
/*      */       {
/* 3052 */         return marshallGet.invoke(paramObject, new Object[0]);
/*      */       } catch (IllegalAccessException localIllegalAccessException) {
/* 3054 */         throw new AssertionError(localIllegalAccessException);
/*      */       } catch (InvocationTargetException localInvocationTargetException) {
/* 3056 */         Throwable localThrowable = localInvocationTargetException.getCause();
/* 3057 */         if ((localThrowable instanceof IOException))
/* 3058 */           throw ((IOException)localThrowable);
/* 3059 */         if ((localThrowable instanceof ClassNotFoundException))
/* 3060 */           throw ((ClassNotFoundException)localThrowable);
/* 3061 */         throw new AssertionError(localInvocationTargetException);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public class ReencodingInputStream extends InputStream
/*      */   {
/*      */     protected BufferedReader wrapped;
/* 1871 */     protected final char[] in = new char[2];
/*      */     protected byte[] out;
/*      */     protected CharsetEncoder encoder;
/*      */     protected CharBuffer inBuf;
/*      */     protected ByteBuffer outBuf;
/*      */     protected char[] eoln;
/*      */     protected int numTerminators;
/*      */     protected boolean eos;
/*      */     protected int index;
/*      */     protected int limit;
/*      */ 
/*      */     public ReencodingInputStream(InputStream paramLong, long arg3, String paramTransferable, Transferable arg6)
/*      */       throws IOException
/*      */     {
/* 1889 */       Long localLong = Long.valueOf(???);
/*      */ 
/* 1891 */       String str1 = null;
/*      */       Object localObject;
/* 1892 */       if ((DataTransferer.this.isLocaleDependentTextFormat(???)) && (localObject != null) && (localObject.isDataFlavorSupported(DataTransferer.javaTextEncodingFlavor)))
/*      */       {
/*      */         try
/*      */         {
/* 1898 */           str1 = new String((byte[])localObject.getTransferData(DataTransferer.javaTextEncodingFlavor), "UTF-8");
/*      */         }
/*      */         catch (UnsupportedFlavorException localUnsupportedFlavorException) {
/*      */         }
/*      */       }
/*      */       else {
/* 1904 */         str1 = DataTransferer.this.getCharsetForTextFormat(localLong);
/*      */       }
/*      */ 
/* 1907 */       if (str1 == null)
/*      */       {
/* 1909 */         str1 = DataTransferer.getDefaultTextCharset();
/*      */       }
/* 1911 */       this.wrapped = new BufferedReader(new InputStreamReader(paramLong, str1));
/*      */ 
/* 1914 */       if (paramTransferable == null)
/*      */       {
/* 1919 */         throw new NullPointerException("null target encoding");
/*      */       }
/*      */       try
/*      */       {
/* 1923 */         this.encoder = Charset.forName(paramTransferable).newEncoder();
/* 1924 */         this.out = new byte[(int)(this.encoder.maxBytesPerChar() * this.in.length + 0.5D)];
/* 1925 */         this.inBuf = CharBuffer.wrap(this.in);
/* 1926 */         this.outBuf = ByteBuffer.wrap(this.out);
/*      */       } catch (IllegalCharsetNameException localIllegalCharsetNameException) {
/* 1928 */         throw new IOException(localIllegalCharsetNameException.toString());
/*      */       } catch (UnsupportedCharsetException localUnsupportedCharsetException) {
/* 1930 */         throw new IOException(localUnsupportedCharsetException.toString());
/*      */       } catch (UnsupportedOperationException localUnsupportedOperationException) {
/* 1932 */         throw new IOException(localUnsupportedOperationException.toString());
/*      */       }
/*      */ 
/* 1935 */       String str2 = (String)DataTransferer.nativeEOLNs.get(localLong);
/* 1936 */       if (str2 != null) {
/* 1937 */         this.eoln = str2.toCharArray();
/*      */       }
/*      */ 
/* 1942 */       Integer localInteger = (Integer)DataTransferer.nativeTerminators.get(localLong);
/* 1943 */       if (localInteger != null)
/* 1944 */         this.numTerminators = localInteger.intValue();
/*      */     }
/*      */ 
/*      */     private int readChar() throws IOException
/*      */     {
/* 1949 */       int i = this.wrapped.read();
/*      */ 
/* 1951 */       if (i == -1) {
/* 1952 */         this.eos = true;
/* 1953 */         return -1;
/*      */       }
/*      */ 
/* 1957 */       if ((this.numTerminators > 0) && (i == 0)) {
/* 1958 */         this.eos = true;
/* 1959 */         return -1;
/* 1960 */       }if ((this.eoln != null) && (matchCharArray(this.eoln, i))) {
/* 1961 */         i = 10;
/*      */       }
/*      */ 
/* 1964 */       return i;
/*      */     }
/*      */ 
/*      */     public int read() throws IOException {
/* 1968 */       if (this.eos) {
/* 1969 */         return -1;
/*      */       }
/*      */ 
/* 1972 */       if (this.index >= this.limit)
/*      */       {
/* 1974 */         int i = readChar();
/* 1975 */         if (i == -1) {
/* 1976 */           return -1;
/*      */         }
/*      */ 
/* 1979 */         this.in[0] = ((char)i);
/* 1980 */         this.in[1] = '\000';
/* 1981 */         this.inBuf.limit(1);
/* 1982 */         if (Character.isHighSurrogate((char)i)) {
/* 1983 */           i = readChar();
/* 1984 */           if (i != -1) {
/* 1985 */             this.in[1] = ((char)i);
/* 1986 */             this.inBuf.limit(2);
/*      */           }
/*      */         }
/*      */ 
/* 1990 */         this.inBuf.rewind();
/* 1991 */         this.outBuf.limit(this.out.length).rewind();
/* 1992 */         this.encoder.encode(this.inBuf, this.outBuf, false);
/* 1993 */         this.outBuf.flip();
/* 1994 */         this.limit = this.outBuf.limit();
/*      */ 
/* 1996 */         this.index = 0;
/*      */ 
/* 1998 */         return read();
/*      */       }
/* 2000 */       return this.out[(this.index++)] & 0xFF;
/*      */     }
/*      */ 
/*      */     public int available() throws IOException
/*      */     {
/* 2005 */       return this.eos ? 0 : this.limit - this.index;
/*      */     }
/*      */ 
/*      */     public void close() throws IOException {
/* 2009 */       this.wrapped.close();
/*      */     }
/*      */ 
/*      */     private boolean matchCharArray(char[] paramArrayOfChar, int paramInt)
/*      */       throws IOException
/*      */     {
/* 2022 */       this.wrapped.mark(paramArrayOfChar.length);
/*      */ 
/* 2024 */       int i = 0;
/* 2025 */       if ((char)paramInt == paramArrayOfChar[0]) {
/* 2026 */         for (i = 1; i < paramArrayOfChar.length; i++) {
/* 2027 */           paramInt = this.wrapped.read();
/* 2028 */           if ((paramInt == -1) || ((char)paramInt != paramArrayOfChar[i]))
/*      */           {
/*      */             break;
/*      */           }
/*      */         }
/*      */       }
/* 2034 */       if (i == paramArrayOfChar.length) {
/* 2035 */         return true;
/*      */       }
/* 2037 */       this.wrapped.reset();
/* 2038 */       return false;
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class StandardEncodingsHolder
/*      */   {
/*  174 */     private static final SortedSet standardEncodings = load();
/*      */ 
/*      */     private static SortedSet load() {
/*  177 */       DataTransferer.CharsetComparator localCharsetComparator = new DataTransferer.CharsetComparator(false);
/*      */ 
/*  179 */       TreeSet localTreeSet = new TreeSet(localCharsetComparator);
/*  180 */       localTreeSet.add("US-ASCII");
/*  181 */       localTreeSet.add("ISO-8859-1");
/*  182 */       localTreeSet.add("UTF-8");
/*  183 */       localTreeSet.add("UTF-16BE");
/*  184 */       localTreeSet.add("UTF-16LE");
/*  185 */       localTreeSet.add("UTF-16");
/*  186 */       localTreeSet.add(DataTransferer.getDefaultTextCharset());
/*  187 */       return Collections.unmodifiableSortedSet(localTreeSet);
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.datatransfer.DataTransferer
 * JD-Core Version:    0.6.2
 */