/*      */ package java.awt.datatransfer;
/*      */ 
/*      */ import java.awt.Toolkit;
/*      */ import java.io.BufferedReader;
/*      */ import java.io.File;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStreamReader;
/*      */ import java.io.PrintStream;
/*      */ import java.lang.ref.SoftReference;
/*      */ import java.net.MalformedURLException;
/*      */ import java.net.URI;
/*      */ import java.net.URL;
/*      */ import java.security.AccessController;
/*      */ import java.security.PrivilegedAction;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedList;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import sun.awt.AppContext;
/*      */ import sun.awt.datatransfer.DataTransferer;
/*      */ 
/*      */ public final class SystemFlavorMap
/*      */   implements FlavorMap, FlavorTable
/*      */ {
/*   73 */   private static String JavaMIME = "JAVA_DATAFLAVOR:";
/*      */ 
/*   75 */   private static final Object FLAVOR_MAP_KEY = new Object();
/*      */   private static final String keyValueSeparators = "=: \t\r\n\f";
/*      */   private static final String strictKeyValueSeparators = "=:";
/*      */   private static final String whiteSpaceChars = " \t\r\n\f";
/*   88 */   private static final String[] UNICODE_TEXT_CLASSES = { "java.io.Reader", "java.lang.String", "java.nio.CharBuffer", "\"[C\"" };
/*      */ 
/*   96 */   private static final String[] ENCODED_TEXT_CLASSES = { "java.io.InputStream", "java.nio.ByteBuffer", "\"[B\"" };
/*      */   private static final String TEXT_PLAIN_BASE_TYPE = "text/plain";
/*      */   private static final boolean SYNTHESIZE_IF_NOT_FOUND = true;
/*  118 */   private Map nativeToFlavor = new HashMap();
/*      */ 
/*  139 */   private Map flavorToNative = new HashMap();
/*      */ 
/*  158 */   private boolean isMapInitialized = false;
/*      */ 
/*  164 */   private Map getNativesForFlavorCache = new HashMap();
/*      */ 
/*  170 */   private Map getFlavorsForNativeCache = new HashMap();
/*      */ 
/*  178 */   private Set disabledMappingGenerationKeys = new HashSet();
/*      */ 
/*      */   private Map getNativeToFlavor()
/*      */   {
/*  128 */     if (!this.isMapInitialized) {
/*  129 */       initSystemFlavorMap();
/*      */     }
/*  131 */     return this.nativeToFlavor;
/*      */   }
/*      */ 
/*      */   private synchronized Map getFlavorToNative()
/*      */   {
/*  149 */     if (!this.isMapInitialized) {
/*  150 */       initSystemFlavorMap();
/*      */     }
/*  152 */     return this.flavorToNative;
/*      */   }
/*      */ 
/*      */   public static FlavorMap getDefaultFlavorMap()
/*      */   {
/*  184 */     AppContext localAppContext = AppContext.getAppContext();
/*  185 */     Object localObject = (FlavorTable)localAppContext.get(FLAVOR_MAP_KEY);
/*  186 */     if (localObject == null) {
/*  187 */       localObject = new SystemFlavorMap();
/*  188 */       localAppContext.put(FLAVOR_MAP_KEY, localObject);
/*      */     }
/*  190 */     return localObject;
/*      */   }
/*      */ 
/*      */   private void initSystemFlavorMap()
/*      */   {
/*  202 */     if (this.isMapInitialized) {
/*  203 */       return;
/*      */     }
/*      */ 
/*  206 */     this.isMapInitialized = true;
/*  207 */     BufferedReader localBufferedReader1 = (BufferedReader)AccessController.doPrivileged(new PrivilegedAction()
/*      */     {
/*      */       public BufferedReader run()
/*      */       {
/*  211 */         String str = System.getProperty("java.home") + File.separator + "lib" + File.separator + "flavormap.properties";
/*      */         try
/*      */         {
/*  218 */           return new BufferedReader(new InputStreamReader(new File(str).toURI().toURL().openStream(), "ISO-8859-1"));
/*      */         }
/*      */         catch (MalformedURLException localMalformedURLException)
/*      */         {
/*  222 */           System.err.println("MalformedURLException:" + localMalformedURLException + " while loading default flavormap.properties file:" + str);
/*      */         } catch (IOException localIOException) {
/*  224 */           System.err.println("IOException:" + localIOException + " while loading default flavormap.properties file:" + str);
/*      */         }
/*  226 */         return null;
/*      */       }
/*      */     });
/*  230 */     String str = (String)AccessController.doPrivileged(new PrivilegedAction()
/*      */     {
/*      */       public String run()
/*      */       {
/*  234 */         return Toolkit.getProperty("AWT.DnD.flavorMapFileURL", null);
/*      */       }
/*      */     });
/*  238 */     if (localBufferedReader1 != null) {
/*      */       try {
/*  240 */         parseAndStoreReader(localBufferedReader1);
/*      */       } catch (IOException localIOException1) {
/*  242 */         System.err.println("IOException:" + localIOException1 + " while parsing default flavormap.properties file");
/*      */       }
/*      */     }
/*      */ 
/*  246 */     BufferedReader localBufferedReader2 = null;
/*  247 */     if (str != null) {
/*      */       try {
/*  249 */         localBufferedReader2 = new BufferedReader(new InputStreamReader(new URL(str).openStream(), "ISO-8859-1"));
/*      */       } catch (MalformedURLException localMalformedURLException) {
/*  251 */         System.err.println("MalformedURLException:" + localMalformedURLException + " while reading AWT.DnD.flavorMapFileURL:" + str);
/*      */       } catch (IOException localIOException2) {
/*  253 */         System.err.println("IOException:" + localIOException2 + " while reading AWT.DnD.flavorMapFileURL:" + str);
/*      */       }
/*      */       catch (SecurityException localSecurityException)
/*      */       {
/*      */       }
/*      */     }
/*  259 */     if (localBufferedReader2 != null)
/*      */       try {
/*  261 */         parseAndStoreReader(localBufferedReader2);
/*      */       } catch (IOException localIOException3) {
/*  263 */         System.err.println("IOException:" + localIOException3 + " while parsing AWT.DnD.flavorMapFileURL");
/*      */       }
/*      */   }
/*      */ 
/*      */   private void parseAndStoreReader(BufferedReader paramBufferedReader)
/*      */     throws IOException
/*      */   {
/*      */     while (true)
/*      */     {
/*  274 */       String str1 = paramBufferedReader.readLine();
/*  275 */       if (str1 == null) {
/*  276 */         return;
/*      */       }
/*      */ 
/*  279 */       if (str1.length() > 0)
/*      */       {
/*  281 */         int i = str1.charAt(0);
/*  282 */         if ((i != 35) && (i != 33))
/*      */         {
/*      */           int m;
/*  283 */           while (continueLine(str1)) {
/*  284 */             String str2 = paramBufferedReader.readLine();
/*  285 */             if (str2 == null) {
/*  286 */               str2 = "";
/*      */             }
/*  288 */             String str3 = str1.substring(0, str1.length() - 1);
/*      */ 
/*  291 */             m = 0;
/*  292 */             while ((m < str2.length()) && 
/*  293 */               (" \t\r\n\f".indexOf(str2.charAt(m)) != -1)) {
/*  292 */               m++;
/*      */             }
/*      */ 
/*  299 */             str2 = str2.substring(m, str2.length());
/*      */ 
/*  301 */             str1 = str3 + str2;
/*      */           }
/*      */ 
/*  305 */           int j = str1.length();
/*  306 */           int k = 0;
/*  307 */           while ((k < j) && 
/*  308 */             (" \t\r\n\f".indexOf(str1.charAt(k)) != -1)) {
/*  307 */             k++;
/*      */           }
/*      */ 
/*  315 */           if (k != j)
/*      */           {
/*  320 */             for (m = k; 
/*  321 */               m < j; m++) {
/*  322 */               n = str1.charAt(m);
/*  323 */               if (n == 92)
/*  324 */                 m++;
/*  325 */               else if ("=: \t\r\n\f".indexOf(n) != -1)
/*      */                 {
/*      */                   break;
/*      */                 }
/*      */ 
/*      */             }
/*      */ 
/*  332 */             int n = m;
/*  333 */             while ((n < j) && 
/*  334 */               (" \t\r\n\f".indexOf(str1.charAt(n)) != -1)) {
/*  333 */               n++;
/*      */             }
/*      */ 
/*  341 */             if ((n < j) && 
/*  342 */               ("=:".indexOf(str1.charAt(n)) != -1))
/*      */             {
/*  344 */               n++;
/*      */             }
/*      */ 
/*  349 */             while ((n < j) && 
/*  350 */               (" \t\r\n\f".indexOf(str1.charAt(n)) != -1))
/*      */             {
/*  354 */               n++;
/*      */             }
/*      */ 
/*  357 */             String str4 = str1.substring(k, m);
/*  358 */             String str5 = m < j ? str1.substring(n, j) : "";
/*      */ 
/*  363 */             str4 = loadConvert(str4);
/*  364 */             str5 = loadConvert(str5);
/*      */             try
/*      */             {
/*  367 */               MimeType localMimeType = new MimeType(str5);
/*  368 */               if ("text".equals(localMimeType.getPrimaryType())) {
/*  369 */                 String str6 = localMimeType.getParameter("charset");
/*  370 */                 if (DataTransferer.doesSubtypeSupportCharset(localMimeType.getSubType(), str6))
/*      */                 {
/*  377 */                   DataTransferer localDataTransferer = DataTransferer.getInstance();
/*      */ 
/*  379 */                   if (localDataTransferer != null) {
/*  380 */                     localDataTransferer.registerTextFlavorProperties(str4, str6, localMimeType.getParameter("eoln"), localMimeType.getParameter("terminators"));
/*      */                   }
/*      */ 
/*      */                 }
/*      */ 
/*  391 */                 localMimeType.removeParameter("charset");
/*  392 */                 localMimeType.removeParameter("class");
/*  393 */                 localMimeType.removeParameter("eoln");
/*  394 */                 localMimeType.removeParameter("terminators");
/*  395 */                 str5 = localMimeType.toString();
/*      */               }
/*      */             } catch (MimeTypeParseException localMimeTypeParseException) {
/*  398 */               localMimeTypeParseException.printStackTrace();
/*  399 */             }continue;
/*      */             DataFlavor localDataFlavor;
/*      */             try
/*      */             {
/*  404 */               localDataFlavor = new DataFlavor(str5);
/*      */             } catch (Exception localException1) {
/*      */               try {
/*  407 */                 localDataFlavor = new DataFlavor(str5, (String)null);
/*      */               } catch (Exception localException2) {
/*  409 */                 localException2.printStackTrace(); } 
/*  410 */             }continue;
/*      */ 
/*  416 */             if ("text".equals(localDataFlavor.getPrimaryType())) {
/*  417 */               store(str5, str4, getFlavorToNative());
/*  418 */               store(str4, str5, getNativeToFlavor());
/*      */             } else {
/*  420 */               store(localDataFlavor, str4, getFlavorToNative());
/*  421 */               store(str4, localDataFlavor, getNativeToFlavor());
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private boolean continueLine(String paramString)
/*      */   {
/*  432 */     int i = 0;
/*  433 */     int j = paramString.length() - 1;
/*  434 */     while ((j >= 0) && (paramString.charAt(j--) == '\\')) {
/*  435 */       i++;
/*      */     }
/*  437 */     return i % 2 == 1;
/*      */   }
/*      */ 
/*      */   private String loadConvert(String paramString)
/*      */   {
/*  445 */     int j = paramString.length();
/*  446 */     StringBuilder localStringBuilder = new StringBuilder(j);
/*      */ 
/*  448 */     for (int k = 0; k < j; ) {
/*  449 */       int i = paramString.charAt(k++);
/*  450 */       if (i == 92) {
/*  451 */         i = paramString.charAt(k++);
/*  452 */         if (i == 117)
/*      */         {
/*  454 */           int m = 0;
/*  455 */           for (int n = 0; n < 4; n++) {
/*  456 */             i = paramString.charAt(k++);
/*  457 */             switch (i) { case 48:
/*      */             case 49:
/*      */             case 50:
/*      */             case 51:
/*      */             case 52:
/*      */             case 53:
/*      */             case 54:
/*      */             case 55:
/*      */             case 56:
/*      */             case 57:
/*  460 */               m = (m << 4) + i - 48;
/*  461 */               break;
/*      */             case 97:
/*      */             case 98:
/*      */             case 99:
/*      */             case 100:
/*      */             case 101:
/*      */             case 102:
/*  465 */               m = (m << 4) + 10 + i - 97;
/*  466 */               break;
/*      */             case 65:
/*      */             case 66:
/*      */             case 67:
/*      */             case 68:
/*      */             case 69:
/*      */             case 70:
/*  470 */               m = (m << 4) + 10 + i - 65;
/*  471 */               break;
/*      */             case 58:
/*      */             case 59:
/*      */             case 60:
/*      */             case 61:
/*      */             case 62:
/*      */             case 63:
/*      */             case 64:
/*      */             case 71:
/*      */             case 72:
/*      */             case 73:
/*      */             case 74:
/*      */             case 75:
/*      */             case 76:
/*      */             case 77:
/*      */             case 78:
/*      */             case 79:
/*      */             case 80:
/*      */             case 81:
/*      */             case 82:
/*      */             case 83:
/*      */             case 84:
/*      */             case 85:
/*      */             case 86:
/*      */             case 87:
/*      */             case 88:
/*      */             case 89:
/*      */             case 90:
/*      */             case 91:
/*      */             case 92:
/*      */             case 93:
/*      */             case 94:
/*      */             case 95:
/*      */             case 96:
/*      */             default:
/*  474 */               throw new IllegalArgumentException("Malformed \\uxxxx encoding.");
/*      */             }
/*      */ 
/*      */           }
/*      */ 
/*  479 */           localStringBuilder.append((char)m);
/*      */         } else {
/*  481 */           if (i == 116)
/*  482 */             i = 9;
/*  483 */           else if (i == 114)
/*  484 */             i = 13;
/*  485 */           else if (i == 110)
/*  486 */             i = 10;
/*  487 */           else if (i == 102) {
/*  488 */             i = 12;
/*      */           }
/*  490 */           localStringBuilder.append(i);
/*      */         }
/*      */       } else {
/*  493 */         localStringBuilder.append(i);
/*      */       }
/*      */     }
/*  496 */     return localStringBuilder.toString();
/*      */   }
/*      */ 
/*      */   private void store(Object paramObject1, Object paramObject2, Map paramMap)
/*      */   {
/*  506 */     Object localObject = (List)paramMap.get(paramObject1);
/*  507 */     if (localObject == null) {
/*  508 */       localObject = new ArrayList(1);
/*  509 */       paramMap.put(paramObject1, localObject);
/*      */     }
/*  511 */     if (!((List)localObject).contains(paramObject2))
/*  512 */       ((List)localObject).add(paramObject2);
/*      */   }
/*      */ 
/*      */   private List nativeToFlavorLookup(String paramString)
/*      */   {
/*  523 */     Object localObject1 = (List)getNativeToFlavor().get(paramString);
/*      */     Object localObject2;
/*      */     Object localObject3;
/*  525 */     if ((paramString != null) && (!this.disabledMappingGenerationKeys.contains(paramString))) {
/*  526 */       localObject2 = DataTransferer.getInstance();
/*  527 */       if (localObject2 != null) {
/*  528 */         localObject3 = ((DataTransferer)localObject2).getPlatformMappingsForNative(paramString);
/*      */ 
/*  530 */         if (!((List)localObject3).isEmpty()) {
/*  531 */           if (localObject1 != null) {
/*  532 */             ((List)localObject3).removeAll(new HashSet((Collection)localObject1));
/*      */ 
/*  537 */             ((List)localObject3).addAll((Collection)localObject1);
/*      */           }
/*  539 */           localObject1 = localObject3;
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  544 */     if ((localObject1 == null) && (isJavaMIMEType(paramString))) {
/*  545 */       localObject2 = decodeJavaMIMEType(paramString);
/*  546 */       localObject3 = null;
/*      */       try
/*      */       {
/*  549 */         localObject3 = new DataFlavor((String)localObject2);
/*      */       } catch (Exception localException) {
/*  551 */         System.err.println("Exception \"" + localException.getClass().getName() + ": " + localException.getMessage() + "\"while constructing DataFlavor for: " + (String)localObject2);
/*      */       }
/*      */ 
/*  557 */       if (localObject3 != null) {
/*  558 */         localObject1 = new ArrayList(1);
/*  559 */         getNativeToFlavor().put(paramString, localObject1);
/*  560 */         ((List)localObject1).add(localObject3);
/*  561 */         this.getFlavorsForNativeCache.remove(paramString);
/*  562 */         this.getFlavorsForNativeCache.remove(null);
/*      */ 
/*  564 */         Object localObject4 = (List)getFlavorToNative().get(localObject3);
/*  565 */         if (localObject4 == null) {
/*  566 */           localObject4 = new ArrayList(1);
/*  567 */           getFlavorToNative().put(localObject3, localObject4);
/*      */         }
/*  569 */         ((List)localObject4).add(paramString);
/*  570 */         this.getNativesForFlavorCache.remove(localObject3);
/*  571 */         this.getNativesForFlavorCache.remove(null);
/*      */       }
/*      */     }
/*      */ 
/*  575 */     return localObject1 != null ? localObject1 : new ArrayList(0);
/*      */   }
/*      */ 
/*      */   private List flavorToNativeLookup(DataFlavor paramDataFlavor, boolean paramBoolean)
/*      */   {
/*  588 */     Object localObject1 = (List)getFlavorToNative().get(paramDataFlavor);
/*      */     Object localObject2;
/*      */     Object localObject3;
/*  590 */     if ((paramDataFlavor != null) && (!this.disabledMappingGenerationKeys.contains(paramDataFlavor))) {
/*  591 */       localObject2 = DataTransferer.getInstance();
/*  592 */       if (localObject2 != null) {
/*  593 */         localObject3 = ((DataTransferer)localObject2).getPlatformMappingsForFlavor(paramDataFlavor);
/*      */ 
/*  595 */         if (!((List)localObject3).isEmpty()) {
/*  596 */           if (localObject1 != null) {
/*  597 */             ((List)localObject3).removeAll(new HashSet((Collection)localObject1));
/*      */ 
/*  602 */             ((List)localObject3).addAll((Collection)localObject1);
/*      */           }
/*  604 */           localObject1 = localObject3;
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  609 */     if (localObject1 == null) {
/*  610 */       if (paramBoolean) {
/*  611 */         localObject2 = encodeDataFlavor(paramDataFlavor);
/*  612 */         localObject1 = new ArrayList(1);
/*  613 */         getFlavorToNative().put(paramDataFlavor, localObject1);
/*  614 */         ((List)localObject1).add(localObject2);
/*  615 */         this.getNativesForFlavorCache.remove(paramDataFlavor);
/*  616 */         this.getNativesForFlavorCache.remove(null);
/*      */ 
/*  618 */         localObject3 = (List)getNativeToFlavor().get(localObject2);
/*  619 */         if (localObject3 == null) {
/*  620 */           localObject3 = new ArrayList(1);
/*  621 */           getNativeToFlavor().put(localObject2, localObject3);
/*      */         }
/*  623 */         ((List)localObject3).add(paramDataFlavor);
/*  624 */         this.getFlavorsForNativeCache.remove(localObject2);
/*  625 */         this.getFlavorsForNativeCache.remove(null);
/*      */       } else {
/*  627 */         localObject1 = new ArrayList(0);
/*      */       }
/*      */     }
/*      */ 
/*  631 */     return localObject1;
/*      */   }
/*      */ 
/*      */   public synchronized List<String> getNativesForFlavor(DataFlavor paramDataFlavor)
/*      */   {
/*  660 */     Object localObject1 = null;
/*      */ 
/*  663 */     SoftReference localSoftReference = (SoftReference)this.getNativesForFlavorCache.get(paramDataFlavor);
/*  664 */     if (localSoftReference != null) {
/*  665 */       localObject1 = (List)localSoftReference.get();
/*  666 */       if (localObject1 != null)
/*      */       {
/*  669 */         return new ArrayList((Collection)localObject1);
/*      */       }
/*      */     }
/*      */ 
/*  673 */     if (paramDataFlavor == null) {
/*  674 */       localObject1 = new ArrayList(getNativeToFlavor().keySet());
/*  675 */     } else if (this.disabledMappingGenerationKeys.contains(paramDataFlavor))
/*      */     {
/*  678 */       localObject1 = flavorToNativeLookup(paramDataFlavor, false);
/*      */     }
/*      */     else
/*      */     {
/*      */       Object localObject2;
/*  679 */       if (DataTransferer.isFlavorCharsetTextType(paramDataFlavor))
/*      */       {
/*  683 */         if ("text".equals(paramDataFlavor.getPrimaryType())) {
/*  684 */           localObject1 = (List)getFlavorToNative().get(paramDataFlavor.mimeType.getBaseType());
/*  685 */           if (localObject1 != null)
/*      */           {
/*  687 */             localObject1 = new ArrayList((Collection)localObject1);
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*  692 */         localObject2 = (List)getFlavorToNative().get("text/plain");
/*      */ 
/*  694 */         if ((localObject2 != null) && (!((List)localObject2).isEmpty()))
/*      */         {
/*  697 */           localObject2 = new ArrayList((Collection)localObject2);
/*  698 */           if ((localObject1 != null) && (!((List)localObject1).isEmpty()))
/*      */           {
/*  700 */             ((List)localObject2).removeAll(new HashSet((Collection)localObject1));
/*  701 */             ((List)localObject1).addAll((Collection)localObject2);
/*      */           } else {
/*  703 */             localObject1 = localObject2;
/*      */           }
/*      */         }
/*      */ 
/*  707 */         if ((localObject1 == null) || (((List)localObject1).isEmpty())) {
/*  708 */           localObject1 = flavorToNativeLookup(paramDataFlavor, true);
/*      */         }
/*      */         else
/*      */         {
/*  713 */           Object localObject3 = flavorToNativeLookup(paramDataFlavor, false);
/*      */ 
/*  718 */           if (!((List)localObject3).isEmpty())
/*      */           {
/*  721 */             localObject3 = new ArrayList((Collection)localObject3);
/*      */ 
/*  723 */             ((List)localObject3).removeAll(new HashSet((Collection)localObject1));
/*  724 */             ((List)localObject1).addAll((Collection)localObject3);
/*      */           }
/*      */         }
/*  727 */       } else if (DataTransferer.isFlavorNoncharsetTextType(paramDataFlavor)) {
/*  728 */         localObject1 = (List)getFlavorToNative().get(paramDataFlavor.mimeType.getBaseType());
/*      */ 
/*  730 */         if ((localObject1 == null) || (((List)localObject1).isEmpty())) {
/*  731 */           localObject1 = flavorToNativeLookup(paramDataFlavor, true);
/*      */         }
/*      */         else
/*      */         {
/*  736 */           localObject2 = flavorToNativeLookup(paramDataFlavor, false);
/*      */ 
/*  741 */           if (!((List)localObject2).isEmpty())
/*      */           {
/*  744 */             localObject1 = new ArrayList((Collection)localObject1);
/*  745 */             localObject2 = new ArrayList((Collection)localObject2);
/*      */ 
/*  747 */             ((List)localObject2).removeAll(new HashSet((Collection)localObject1));
/*  748 */             ((List)localObject1).addAll((Collection)localObject2);
/*      */           }
/*      */         }
/*      */       } else {
/*  752 */         localObject1 = flavorToNativeLookup(paramDataFlavor, true);
/*      */       }
/*      */     }
/*  755 */     this.getNativesForFlavorCache.put(paramDataFlavor, new SoftReference(localObject1));
/*      */ 
/*  757 */     return new ArrayList((Collection)localObject1);
/*      */   }
/*      */ 
/*      */   public synchronized List<DataFlavor> getFlavorsForNative(String paramString)
/*      */   {
/*  794 */     SoftReference localSoftReference = (SoftReference)this.getFlavorsForNativeCache.get(paramString);
/*  795 */     if (localSoftReference != null) {
/*  796 */       localObject1 = (ArrayList)localSoftReference.get();
/*  797 */       if (localObject1 != null) {
/*  798 */         return (List)((ArrayList)localObject1).clone();
/*      */       }
/*      */     }
/*      */ 
/*  802 */     Object localObject1 = new LinkedList();
/*      */     HashSet localHashSet;
/*      */     Object localObject3;
/*      */     Object localObject4;
/*      */     Object localObject5;
/*      */     Object localObject6;
/*  804 */     if (paramString == null) {
/*  805 */       localObject2 = getNativesForFlavor(null);
/*  806 */       localHashSet = new HashSet(((List)localObject2).size());
/*      */ 
/*  808 */       localObject3 = ((List)localObject2).iterator();
/*  809 */       while (((Iterator)localObject3).hasNext())
/*      */       {
/*  811 */         localObject4 = getFlavorsForNative((String)((Iterator)localObject3).next());
/*      */ 
/*  813 */         localObject5 = ((List)localObject4).iterator();
/*  814 */         while (((Iterator)localObject5).hasNext())
/*      */         {
/*  816 */           localObject6 = ((Iterator)localObject5).next();
/*  817 */           if (localHashSet.add(localObject6))
/*  818 */             ((LinkedList)localObject1).add(localObject6);
/*      */         }
/*      */       }
/*      */     }
/*      */     else {
/*  823 */       localObject2 = nativeToFlavorLookup(paramString);
/*      */ 
/*  825 */       if (this.disabledMappingGenerationKeys.contains(paramString)) {
/*  826 */         return localObject2;
/*      */       }
/*      */ 
/*  829 */       localHashSet = new HashSet(((List)localObject2).size());
/*      */ 
/*  831 */       localObject3 = nativeToFlavorLookup(paramString);
/*      */ 
/*  833 */       localObject4 = ((List)localObject3).iterator();
/*      */ 
/*  835 */       while (((Iterator)localObject4).hasNext())
/*      */       {
/*  837 */         localObject5 = ((Iterator)localObject4).next();
/*  838 */         if ((localObject5 instanceof String)) {
/*  839 */           localObject6 = (String)localObject5;
/*  840 */           String str = null;
/*      */           try {
/*  842 */             MimeType localMimeType = new MimeType((String)localObject6);
/*  843 */             str = localMimeType.getSubType();
/*      */           }
/*      */           catch (MimeTypeParseException localMimeTypeParseException)
/*      */           {
/*  847 */             if (!$assertionsDisabled) throw new AssertionError();
/*      */           }
/*      */           Object localObject7;
/*  849 */           if (DataTransferer.doesSubtypeSupportCharset(str, null))
/*      */           {
/*  851 */             if (("text/plain".equals(localObject6)) && (localHashSet.add(DataFlavor.stringFlavor)))
/*      */             {
/*  854 */               ((LinkedList)localObject1).add(DataFlavor.stringFlavor);
/*      */             }
/*      */ 
/*  857 */             for (int i = 0; i < UNICODE_TEXT_CLASSES.length; i++) {
/*  858 */               localObject7 = null;
/*      */               try {
/*  860 */                 localObject7 = new DataFlavor((String)localObject6 + ";charset=Unicode;class=" + UNICODE_TEXT_CLASSES[i]);
/*      */               }
/*      */               catch (ClassNotFoundException localClassNotFoundException1)
/*      */               {
/*      */               }
/*  865 */               if (localHashSet.add(localObject7)) {
/*  866 */                 ((LinkedList)localObject1).add(localObject7);
/*      */               }
/*      */             }
/*      */ 
/*  870 */             Iterator localIterator = DataTransferer.standardEncodings();
/*      */ 
/*  872 */             while (localIterator.hasNext())
/*      */             {
/*  874 */               localObject7 = (String)localIterator.next();
/*      */ 
/*  876 */               for (int k = 0; k < ENCODED_TEXT_CLASSES.length; 
/*  877 */                 k++)
/*      */               {
/*  879 */                 DataFlavor localDataFlavor = null;
/*      */                 try {
/*  881 */                   localDataFlavor = new DataFlavor((String)localObject6 + ";charset=" + (String)localObject7 + ";class=" + ENCODED_TEXT_CLASSES[k]);
/*      */                 }
/*      */                 catch (ClassNotFoundException localClassNotFoundException3)
/*      */                 {
/*      */                 }
/*      */ 
/*  892 */                 if (localDataFlavor.equals(DataFlavor.plainTextFlavor)) {
/*  893 */                   localDataFlavor = DataFlavor.plainTextFlavor;
/*      */                 }
/*      */ 
/*  896 */                 if (localHashSet.add(localDataFlavor)) {
/*  897 */                   ((LinkedList)localObject1).add(localDataFlavor);
/*      */                 }
/*      */               }
/*      */             }
/*      */ 
/*  902 */             if (("text/plain".equals(localObject6)) && (localHashSet.add(DataFlavor.plainTextFlavor)))
/*      */             {
/*  905 */               ((LinkedList)localObject1).add(DataFlavor.plainTextFlavor);
/*      */             }
/*      */ 
/*      */           }
/*      */           else
/*      */           {
/*  911 */             for (int j = 0; j < ENCODED_TEXT_CLASSES.length; j++) {
/*  912 */               localObject7 = null;
/*      */               try {
/*  914 */                 localObject7 = new DataFlavor((String)localObject6 + ";class=" + ENCODED_TEXT_CLASSES[j]);
/*      */               }
/*      */               catch (ClassNotFoundException localClassNotFoundException2)
/*      */               {
/*      */               }
/*  919 */               if (localHashSet.add(localObject7))
/*  920 */                 ((LinkedList)localObject1).add(localObject7);
/*      */             }
/*      */           }
/*      */         }
/*      */         else {
/*  925 */           localObject6 = (DataFlavor)localObject5;
/*  926 */           if (localHashSet.add(localObject6)) {
/*  927 */             ((LinkedList)localObject1).add(localObject6);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  933 */     Object localObject2 = new ArrayList((Collection)localObject1);
/*  934 */     this.getFlavorsForNativeCache.put(paramString, new SoftReference(localObject2));
/*  935 */     return (List)((ArrayList)localObject2).clone();
/*      */   }
/*      */ 
/*      */   public synchronized Map<DataFlavor, String> getNativesForFlavors(DataFlavor[] paramArrayOfDataFlavor)
/*      */   {
/*  966 */     if (paramArrayOfDataFlavor == null) {
/*  967 */       localObject = getFlavorsForNative(null);
/*  968 */       paramArrayOfDataFlavor = new DataFlavor[((List)localObject).size()];
/*  969 */       ((List)localObject).toArray(paramArrayOfDataFlavor);
/*      */     }
/*      */ 
/*  972 */     Object localObject = new HashMap(paramArrayOfDataFlavor.length, 1.0F);
/*  973 */     for (int i = 0; i < paramArrayOfDataFlavor.length; i++) {
/*  974 */       List localList = getNativesForFlavor(paramArrayOfDataFlavor[i]);
/*  975 */       String str = localList.isEmpty() ? null : (String)localList.get(0);
/*  976 */       ((HashMap)localObject).put(paramArrayOfDataFlavor[i], str);
/*      */     }
/*      */ 
/*  979 */     return localObject;
/*      */   }
/*      */ 
/*      */   public synchronized Map<String, DataFlavor> getFlavorsForNatives(String[] paramArrayOfString)
/*      */   {
/* 1011 */     if (paramArrayOfString == null) {
/* 1012 */       localObject = getNativesForFlavor(null);
/* 1013 */       paramArrayOfString = new String[((List)localObject).size()];
/* 1014 */       ((List)localObject).toArray(paramArrayOfString);
/*      */     }
/*      */ 
/* 1017 */     Object localObject = new HashMap(paramArrayOfString.length, 1.0F);
/* 1018 */     for (int i = 0; i < paramArrayOfString.length; i++) {
/* 1019 */       List localList = getFlavorsForNative(paramArrayOfString[i]);
/* 1020 */       DataFlavor localDataFlavor = localList.isEmpty() ? null : (DataFlavor)localList.get(0);
/*      */ 
/* 1022 */       ((HashMap)localObject).put(paramArrayOfString[i], localDataFlavor);
/*      */     }
/*      */ 
/* 1025 */     return localObject;
/*      */   }
/*      */ 
/*      */   public synchronized void addUnencodedNativeForFlavor(DataFlavor paramDataFlavor, String paramString)
/*      */   {
/* 1050 */     if ((paramDataFlavor == null) || (paramString == null)) {
/* 1051 */       throw new NullPointerException("null arguments not permitted");
/*      */     }
/*      */ 
/* 1054 */     Object localObject = (List)getFlavorToNative().get(paramDataFlavor);
/* 1055 */     if (localObject == null) {
/* 1056 */       localObject = new ArrayList(1);
/* 1057 */       getFlavorToNative().put(paramDataFlavor, localObject);
/* 1058 */     } else if (((List)localObject).contains(paramString)) {
/* 1059 */       return;
/*      */     }
/* 1061 */     ((List)localObject).add(paramString);
/* 1062 */     this.getNativesForFlavorCache.remove(paramDataFlavor);
/* 1063 */     this.getNativesForFlavorCache.remove(null);
/*      */   }
/*      */ 
/*      */   public synchronized void setNativesForFlavor(DataFlavor paramDataFlavor, String[] paramArrayOfString)
/*      */   {
/* 1096 */     if ((paramDataFlavor == null) || (paramArrayOfString == null)) {
/* 1097 */       throw new NullPointerException("null arguments not permitted");
/*      */     }
/*      */ 
/* 1100 */     getFlavorToNative().remove(paramDataFlavor);
/* 1101 */     for (int i = 0; i < paramArrayOfString.length; i++) {
/* 1102 */       addUnencodedNativeForFlavor(paramDataFlavor, paramArrayOfString[i]);
/*      */     }
/* 1104 */     this.disabledMappingGenerationKeys.add(paramDataFlavor);
/*      */ 
/* 1106 */     this.getNativesForFlavorCache.remove(paramDataFlavor);
/* 1107 */     this.getNativesForFlavorCache.remove(null);
/*      */   }
/*      */ 
/*      */   public synchronized void addFlavorForUnencodedNative(String paramString, DataFlavor paramDataFlavor)
/*      */   {
/* 1130 */     if ((paramString == null) || (paramDataFlavor == null)) {
/* 1131 */       throw new NullPointerException("null arguments not permitted");
/*      */     }
/*      */ 
/* 1134 */     Object localObject = (List)getNativeToFlavor().get(paramString);
/* 1135 */     if (localObject == null) {
/* 1136 */       localObject = new ArrayList(1);
/* 1137 */       getNativeToFlavor().put(paramString, localObject);
/* 1138 */     } else if (((List)localObject).contains(paramDataFlavor)) {
/* 1139 */       return;
/*      */     }
/* 1141 */     ((List)localObject).add(paramDataFlavor);
/* 1142 */     this.getFlavorsForNativeCache.remove(paramString);
/* 1143 */     this.getFlavorsForNativeCache.remove(null);
/*      */   }
/*      */ 
/*      */   public synchronized void setFlavorsForNative(String paramString, DataFlavor[] paramArrayOfDataFlavor)
/*      */   {
/* 1175 */     if ((paramString == null) || (paramArrayOfDataFlavor == null)) {
/* 1176 */       throw new NullPointerException("null arguments not permitted");
/*      */     }
/*      */ 
/* 1179 */     getNativeToFlavor().remove(paramString);
/* 1180 */     for (int i = 0; i < paramArrayOfDataFlavor.length; i++) {
/* 1181 */       addFlavorForUnencodedNative(paramString, paramArrayOfDataFlavor[i]);
/*      */     }
/* 1183 */     this.disabledMappingGenerationKeys.add(paramString);
/*      */ 
/* 1185 */     this.getFlavorsForNativeCache.remove(paramString);
/* 1186 */     this.getFlavorsForNativeCache.remove(null);
/*      */   }
/*      */ 
/*      */   public static String encodeJavaMIMEType(String paramString)
/*      */   {
/* 1209 */     return paramString != null ? JavaMIME + paramString : null;
/*      */   }
/*      */ 
/*      */   public static String encodeDataFlavor(DataFlavor paramDataFlavor)
/*      */   {
/* 1238 */     return paramDataFlavor != null ? encodeJavaMIMEType(paramDataFlavor.getMimeType()) : null;
/*      */   }
/*      */ 
/*      */   public static boolean isJavaMIMEType(String paramString)
/*      */   {
/* 1252 */     return (paramString != null) && (paramString.startsWith(JavaMIME, 0));
/*      */   }
/*      */ 
/*      */   public static String decodeJavaMIMEType(String paramString)
/*      */   {
/* 1263 */     return isJavaMIMEType(paramString) ? paramString.substring(JavaMIME.length(), paramString.length()).trim() : null;
/*      */   }
/*      */ 
/*      */   public static DataFlavor decodeDataFlavor(String paramString)
/*      */     throws ClassNotFoundException
/*      */   {
/* 1279 */     String str = decodeJavaMIMEType(paramString);
/* 1280 */     return str != null ? new DataFlavor(str) : null;
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.datatransfer.SystemFlavorMap
 * JD-Core Version:    0.6.2
 */