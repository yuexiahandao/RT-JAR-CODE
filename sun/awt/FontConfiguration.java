/*      */ package sun.awt;
/*      */ 
/*      */ import java.io.DataInputStream;
/*      */ import java.io.DataOutputStream;
/*      */ import java.io.File;
/*      */ import java.io.FileInputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.OutputStream;
/*      */ import java.io.PrintStream;
/*      */ import java.nio.charset.Charset;
/*      */ import java.nio.charset.CharsetEncoder;
/*      */ import java.security.AccessController;
/*      */ import java.security.PrivilegedAction;
/*      */ import java.util.Arrays;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Hashtable;
/*      */ import java.util.Iterator;
/*      */ import java.util.Locale;
/*      */ import java.util.Map.Entry;
/*      */ import java.util.Properties;
/*      */ import java.util.Set;
/*      */ import java.util.Vector;
/*      */ import sun.font.CompositeFontDescriptor;
/*      */ import sun.font.FontUtilities;
/*      */ import sun.font.SunFontManager;
/*      */ import sun.util.logging.PlatformLogger;
/*      */ 
/*      */ public abstract class FontConfiguration
/*      */ {
/*      */   protected static String osVersion;
/*      */   protected static String osName;
/*      */   protected static String encoding;
/*   66 */   protected static Locale startupLocale = null;
/*   67 */   protected static Hashtable localeMap = null;
/*      */   private static FontConfiguration fontConfig;
/*      */   private static PlatformLogger logger;
/*   70 */   protected static boolean isProperties = true;
/*      */   protected SunFontManager fontManager;
/*      */   protected boolean preferLocaleFonts;
/*      */   protected boolean preferPropFonts;
/*      */   private File fontConfigFile;
/*      */   private boolean foundOsSpecificFile;
/*      */   private boolean inited;
/*      */   private String javaLib;
/*      */   private static short stringIDNum;
/*      */   private static short[] stringIDs;
/*      */   private static StringBuilder stringTable;
/*      */   public static boolean verbose;
/*  455 */   private short initELC = -1;
/*      */   private Locale initLocale;
/*      */   private String initEncoding;
/*      */   private String alphabeticSuffix;
/*  460 */   private short[][][] compFontNameIDs = new short[5][4];
/*  461 */   private int[][][] compExclusions = new int[5][][];
/*  462 */   private int[] compCoreNum = new int[5];
/*      */ 
/*  464 */   private Set<Short> coreFontNameIDs = new HashSet();
/*  465 */   private Set<Short> fallbackFontNameIDs = new HashSet();
/*      */   protected static final int NUM_FONTS = 5;
/*      */   protected static final int NUM_STYLES = 4;
/*  582 */   protected static final String[] fontNames = { "serif", "sansserif", "monospaced", "dialog", "dialoginput" };
/*      */ 
/*  584 */   protected static final String[] publicFontNames = { "Serif", "SansSerif", "Monospaced", "Dialog", "DialogInput" };
/*      */ 
/*  587 */   protected static final String[] styleNames = { "plain", "bold", "italic", "bolditalic" };
/*      */ 
/*  732 */   protected static String[] installedFallbackFontFiles = null;
/*      */ 
/*  749 */   protected HashMap reorderMap = null;
/*      */ 
/*  843 */   private Hashtable charsetRegistry = new Hashtable(5);
/*      */ 
/*  858 */   private FontDescriptor[][][] fontDescriptors = new FontDescriptor[5][4];
/*      */   HashMap<String, Boolean> existsMap;
/* 1176 */   private int numCoreFonts = -1;
/* 1177 */   private String[] componentFonts = null;
/* 1178 */   HashMap<String, String> filenamesMap = new HashMap();
/* 1179 */   HashSet<String> coreFontFileNames = new HashSet();
/*      */   private static final int HEAD_LENGTH = 20;
/*      */   private static final int INDEX_scriptIDs = 0;
/*      */   private static final int INDEX_scriptFonts = 1;
/*      */   private static final int INDEX_elcIDs = 2;
/*      */   private static final int INDEX_sequences = 3;
/*      */   private static final int INDEX_fontfileNameIDs = 4;
/*      */   private static final int INDEX_componentFontNameIDs = 5;
/*      */   private static final int INDEX_filenames = 6;
/*      */   private static final int INDEX_awtfontpaths = 7;
/*      */   private static final int INDEX_exclusions = 8;
/*      */   private static final int INDEX_proportionals = 9;
/*      */   private static final int INDEX_scriptFontsMotif = 10;
/*      */   private static final int INDEX_alphabeticSuffix = 11;
/*      */   private static final int INDEX_stringIDs = 12;
/*      */   private static final int INDEX_stringTable = 13;
/*      */   private static final int INDEX_TABLEEND = 14;
/*      */   private static final int INDEX_fallbackScripts = 15;
/*      */   private static final int INDEX_appendedfontpath = 16;
/*      */   private static final int INDEX_version = 17;
/*      */   private static short[] head;
/*      */   private static short[] table_scriptIDs;
/*      */   private static short[] table_scriptFonts;
/*      */   private static short[] table_elcIDs;
/*      */   private static short[] table_sequences;
/*      */   private static short[] table_fontfileNameIDs;
/*      */   private static short[] table_componentFontNameIDs;
/*      */   private static short[] table_filenames;
/*      */   protected static short[] table_awtfontpaths;
/*      */   private static short[] table_exclusions;
/*      */   private static short[] table_proportionals;
/*      */   private static short[] table_scriptFontsMotif;
/*      */   private static short[] table_alphabeticSuffix;
/*      */   private static short[] table_stringIDs;
/*      */   private static char[] table_stringTable;
/*      */   private HashMap<String, Short> reorderScripts;
/*      */   private static String[] stringCache;
/* 1774 */   private static final int[] EMPTY_INT_ARRAY = new int[0];
/* 1775 */   private static final String[] EMPTY_STRING_ARRAY = new String[0];
/* 1776 */   private static final short[] EMPTY_SHORT_ARRAY = new short[0];
/*      */   private static final String UNDEFINED_COMPONENT_FONT = "unknown";
/*      */ 
/*      */   public FontConfiguration(SunFontManager paramSunFontManager)
/*      */   {
/*   85 */     if (FontUtilities.debugFonts()) {
/*   86 */       FontUtilities.getLogger().info("Creating standard Font Configuration");
/*      */     }
/*      */ 
/*   89 */     if ((FontUtilities.debugFonts()) && (logger == null)) {
/*   90 */       logger = PlatformLogger.getLogger("sun.awt.FontConfiguration");
/*      */     }
/*   92 */     this.fontManager = paramSunFontManager;
/*   93 */     setOsNameAndVersion();
/*   94 */     setEncoding();
/*      */ 
/*   99 */     findFontConfigFile();
/*      */   }
/*      */ 
/*      */   public synchronized boolean init() {
/*  103 */     if (!this.inited) {
/*  104 */       this.preferLocaleFonts = false;
/*  105 */       this.preferPropFonts = false;
/*  106 */       setFontConfiguration();
/*  107 */       readFontConfigFile(this.fontConfigFile);
/*  108 */       initFontConfig();
/*  109 */       this.inited = true;
/*      */     }
/*  111 */     return true;
/*      */   }
/*      */ 
/*      */   public FontConfiguration(SunFontManager paramSunFontManager, boolean paramBoolean1, boolean paramBoolean2)
/*      */   {
/*  117 */     this.fontManager = paramSunFontManager;
/*  118 */     if (FontUtilities.debugFonts()) {
/*  119 */       FontUtilities.getLogger().info("Creating alternate Font Configuration");
/*      */     }
/*      */ 
/*  122 */     this.preferLocaleFonts = paramBoolean1;
/*  123 */     this.preferPropFonts = paramBoolean2;
/*      */ 
/*  128 */     initFontConfig();
/*      */   }
/*      */ 
/*      */   protected void setOsNameAndVersion()
/*      */   {
/*  137 */     osName = System.getProperty("os.name");
/*  138 */     osVersion = System.getProperty("os.version");
/*      */   }
/*      */ 
/*      */   private void setEncoding() {
/*  142 */     encoding = Charset.defaultCharset().name();
/*  143 */     startupLocale = SunToolkit.getStartupLocale();
/*      */   }
/*      */ 
/*      */   public boolean foundOsSpecificFile()
/*      */   {
/*  151 */     return this.foundOsSpecificFile;
/*      */   }
/*      */ 
/*      */   public boolean fontFilesArePresent()
/*      */   {
/*  158 */     init();
/*  159 */     short s1 = this.compFontNameIDs[0][0][0];
/*  160 */     short s2 = getComponentFileID(s1);
/*  161 */     final String str = mapFileName(getComponentFileName(s2));
/*  162 */     Boolean localBoolean = (Boolean)AccessController.doPrivileged(new PrivilegedAction()
/*      */     {
/*      */       public Object run() {
/*      */         try {
/*  166 */           File localFile = new File(str);
/*  167 */           return Boolean.valueOf(localFile.exists());
/*      */         } catch (Exception localException) {
/*      */         }
/*  170 */         return Boolean.valueOf(false);
/*      */       }
/*      */     });
/*  174 */     return localBoolean.booleanValue();
/*      */   }
/*      */ 
/*      */   private void findFontConfigFile()
/*      */   {
/*  179 */     this.foundOsSpecificFile = true;
/*  180 */     String str1 = System.getProperty("java.home");
/*  181 */     if (str1 == null) {
/*  182 */       throw new Error("java.home property not set");
/*      */     }
/*  184 */     this.javaLib = (str1 + File.separator + "lib");
/*  185 */     String str2 = System.getProperty("sun.awt.fontconfig");
/*  186 */     if (str2 != null)
/*  187 */       this.fontConfigFile = new File(str2);
/*      */     else
/*  189 */       this.fontConfigFile = findFontConfigFile(this.javaLib);
/*      */   }
/*      */ 
/*      */   private void readFontConfigFile(File paramFile)
/*      */   {
/*  199 */     getInstalledFallbackFonts(this.javaLib);
/*      */ 
/*  201 */     if (paramFile != null) {
/*      */       try {
/*  203 */         FileInputStream localFileInputStream = new FileInputStream(paramFile.getPath());
/*  204 */         if (isProperties)
/*  205 */           loadProperties(localFileInputStream);
/*      */         else {
/*  207 */           loadBinary(localFileInputStream);
/*      */         }
/*  209 */         localFileInputStream.close();
/*  210 */         if (FontUtilities.debugFonts())
/*  211 */           logger.config("Read logical font configuration from " + paramFile);
/*      */       }
/*      */       catch (IOException localIOException) {
/*  214 */         if (FontUtilities.debugFonts()) {
/*  215 */           logger.config("Failed to read logical font configuration from " + paramFile);
/*      */         }
/*      */       }
/*      */     }
/*  219 */     String str = getVersion();
/*  220 */     if ((!"1".equals(str)) && (FontUtilities.debugFonts()))
/*  221 */       logger.config("Unsupported fontconfig version: " + str);
/*      */   }
/*      */ 
/*      */   protected void getInstalledFallbackFonts(String paramString)
/*      */   {
/*  226 */     String str = paramString + File.separator + "fonts" + File.separator + "fallback";
/*      */ 
/*  229 */     File localFile = new File(str);
/*  230 */     if ((localFile.exists()) && (localFile.isDirectory())) {
/*  231 */       String[] arrayOfString1 = localFile.list(this.fontManager.getTrueTypeFilter());
/*  232 */       String[] arrayOfString2 = localFile.list(this.fontManager.getType1Filter());
/*  233 */       int i = arrayOfString1 == null ? 0 : arrayOfString1.length;
/*  234 */       int j = arrayOfString2 == null ? 0 : arrayOfString2.length;
/*  235 */       int k = i + j;
/*  236 */       if (i + j == 0) {
/*  237 */         return;
/*      */       }
/*  239 */       installedFallbackFontFiles = new String[k];
/*  240 */       for (int m = 0; m < i; m++) {
/*  241 */         installedFallbackFontFiles[m] = (localFile + File.separator + arrayOfString1[m]);
/*      */       }
/*      */ 
/*  244 */       for (m = 0; m < j; m++) {
/*  245 */         installedFallbackFontFiles[(m + i)] = (localFile + File.separator + arrayOfString2[m]);
/*      */       }
/*      */ 
/*  248 */       this.fontManager.registerFontsInDir(str);
/*      */     }
/*      */   }
/*      */ 
/*      */   private File findImpl(String paramString) {
/*  253 */     File localFile = new File(paramString + ".properties");
/*  254 */     if (localFile.canRead()) {
/*  255 */       isProperties = true;
/*  256 */       return localFile;
/*      */     }
/*  258 */     localFile = new File(paramString + ".bfc");
/*  259 */     if (localFile.canRead()) {
/*  260 */       isProperties = false;
/*  261 */       return localFile;
/*      */     }
/*  263 */     return null;
/*      */   }
/*      */ 
/*      */   private File findFontConfigFile(String paramString) {
/*  267 */     String str1 = paramString + File.separator + "fontconfig";
/*      */ 
/*  269 */     String str2 = null;
/*  270 */     if ((osVersion != null) && (osName != null)) {
/*  271 */       localFile = findImpl(str1 + "." + osName + "." + osVersion);
/*  272 */       if (localFile != null) {
/*  273 */         return localFile;
/*      */       }
/*  275 */       int i = osVersion.indexOf(".");
/*  276 */       if (i != -1) {
/*  277 */         str2 = osVersion.substring(0, osVersion.indexOf("."));
/*  278 */         localFile = findImpl(str1 + "." + osName + "." + str2);
/*  279 */         if (localFile != null) {
/*  280 */           return localFile;
/*      */         }
/*      */       }
/*      */     }
/*  284 */     if (osName != null) {
/*  285 */       localFile = findImpl(str1 + "." + osName);
/*  286 */       if (localFile != null) {
/*  287 */         return localFile;
/*      */       }
/*      */     }
/*  290 */     if (osVersion != null) {
/*  291 */       localFile = findImpl(str1 + "." + osVersion);
/*  292 */       if (localFile != null) {
/*  293 */         return localFile;
/*      */       }
/*  295 */       if (str2 != null) {
/*  296 */         localFile = findImpl(str1 + "." + str2);
/*  297 */         if (localFile != null) {
/*  298 */           return localFile;
/*      */         }
/*      */       }
/*      */     }
/*  302 */     this.foundOsSpecificFile = false;
/*      */ 
/*  304 */     File localFile = findImpl(str1);
/*  305 */     if (localFile != null) {
/*  306 */       return localFile;
/*      */     }
/*  308 */     return null;
/*      */   }
/*      */ 
/*      */   public static void loadBinary(InputStream paramInputStream)
/*      */     throws IOException
/*      */   {
/*  315 */     DataInputStream localDataInputStream = new DataInputStream(paramInputStream);
/*  316 */     head = readShortTable(localDataInputStream, 20);
/*  317 */     int[] arrayOfInt = new int[14];
/*  318 */     for (int i = 0; i < 14; i++) {
/*  319 */       arrayOfInt[i] = (head[(i + 1)] - head[i]);
/*      */     }
/*  321 */     table_scriptIDs = readShortTable(localDataInputStream, arrayOfInt[0]);
/*  322 */     table_scriptFonts = readShortTable(localDataInputStream, arrayOfInt[1]);
/*  323 */     table_elcIDs = readShortTable(localDataInputStream, arrayOfInt[2]);
/*  324 */     table_sequences = readShortTable(localDataInputStream, arrayOfInt[3]);
/*  325 */     table_fontfileNameIDs = readShortTable(localDataInputStream, arrayOfInt[4]);
/*  326 */     table_componentFontNameIDs = readShortTable(localDataInputStream, arrayOfInt[5]);
/*  327 */     table_filenames = readShortTable(localDataInputStream, arrayOfInt[6]);
/*  328 */     table_awtfontpaths = readShortTable(localDataInputStream, arrayOfInt[7]);
/*  329 */     table_exclusions = readShortTable(localDataInputStream, arrayOfInt[8]);
/*  330 */     table_proportionals = readShortTable(localDataInputStream, arrayOfInt[9]);
/*  331 */     table_scriptFontsMotif = readShortTable(localDataInputStream, arrayOfInt[10]);
/*  332 */     table_alphabeticSuffix = readShortTable(localDataInputStream, arrayOfInt[11]);
/*  333 */     table_stringIDs = readShortTable(localDataInputStream, arrayOfInt[12]);
/*      */ 
/*  336 */     stringCache = new String[table_stringIDs.length + 1];
/*      */ 
/*  338 */     i = arrayOfInt[13];
/*  339 */     byte[] arrayOfByte = new byte[i * 2];
/*  340 */     table_stringTable = new char[i];
/*  341 */     localDataInputStream.read(arrayOfByte);
/*  342 */     int j = 0; int k = 0;
/*  343 */     while (j < i) {
/*  344 */       table_stringTable[(j++)] = ((char)(arrayOfByte[(k++)] << 8 | arrayOfByte[(k++)] & 0xFF));
/*      */     }
/*  346 */     if (verbose)
/*  347 */       dump();
/*      */   }
/*      */ 
/*      */   public static void saveBinary(OutputStream paramOutputStream)
/*      */     throws IOException
/*      */   {
/*  355 */     sanityCheck();
/*      */ 
/*  357 */     DataOutputStream localDataOutputStream = new DataOutputStream(paramOutputStream);
/*  358 */     writeShortTable(localDataOutputStream, head);
/*  359 */     writeShortTable(localDataOutputStream, table_scriptIDs);
/*  360 */     writeShortTable(localDataOutputStream, table_scriptFonts);
/*  361 */     writeShortTable(localDataOutputStream, table_elcIDs);
/*  362 */     writeShortTable(localDataOutputStream, table_sequences);
/*  363 */     writeShortTable(localDataOutputStream, table_fontfileNameIDs);
/*  364 */     writeShortTable(localDataOutputStream, table_componentFontNameIDs);
/*  365 */     writeShortTable(localDataOutputStream, table_filenames);
/*  366 */     writeShortTable(localDataOutputStream, table_awtfontpaths);
/*  367 */     writeShortTable(localDataOutputStream, table_exclusions);
/*  368 */     writeShortTable(localDataOutputStream, table_proportionals);
/*  369 */     writeShortTable(localDataOutputStream, table_scriptFontsMotif);
/*  370 */     writeShortTable(localDataOutputStream, table_alphabeticSuffix);
/*  371 */     writeShortTable(localDataOutputStream, table_stringIDs);
/*      */ 
/*  373 */     localDataOutputStream.writeChars(new String(table_stringTable));
/*  374 */     paramOutputStream.close();
/*  375 */     if (verbose)
/*  376 */       dump();
/*      */   }
/*      */ 
/*      */   public static void loadProperties(InputStream paramInputStream)
/*      */     throws IOException
/*      */   {
/*  388 */     stringIDNum = 1;
/*  389 */     stringIDs = new short[1000];
/*  390 */     stringTable = new StringBuilder(4096);
/*      */ 
/*  392 */     if ((verbose) && (logger == null)) {
/*  393 */       logger = PlatformLogger.getLogger("sun.awt.FontConfiguration");
/*      */     }
/*  395 */     new PropertiesHandler().load(paramInputStream);
/*      */ 
/*  398 */     stringIDs = null;
/*  399 */     stringTable = null;
/*      */   }
/*      */ 
/*      */   private void initFontConfig()
/*      */   {
/*  412 */     this.initLocale = startupLocale;
/*  413 */     this.initEncoding = encoding;
/*  414 */     if ((this.preferLocaleFonts) && (!willReorderForStartupLocale())) {
/*  415 */       this.preferLocaleFonts = false;
/*      */     }
/*  417 */     this.initELC = getInitELC();
/*  418 */     initAllComponentFonts();
/*      */   }
/*      */ 
/*      */   private short getInitELC()
/*      */   {
/*  426 */     if (this.initELC != -1) {
/*  427 */       return this.initELC;
/*      */     }
/*  429 */     HashMap localHashMap = new HashMap();
/*  430 */     for (int i = 0; i < table_elcIDs.length; i++) {
/*  431 */       localHashMap.put(getString(table_elcIDs[i]), Integer.valueOf(i));
/*      */     }
/*  433 */     String str1 = this.initLocale.getLanguage();
/*  434 */     String str2 = this.initLocale.getCountry();
/*      */     String str3;
/*  436 */     if ((localHashMap.containsKey(str3 = this.initEncoding + "." + str1 + "." + str2)) || (localHashMap.containsKey(str3 = this.initEncoding + "." + str1)) || (localHashMap.containsKey(str3 = this.initEncoding)))
/*      */     {
/*  439 */       this.initELC = ((Integer)localHashMap.get(str3)).shortValue();
/*      */     }
/*  441 */     else this.initELC = ((Integer)localHashMap.get("NULL.NULL.NULL")).shortValue();
/*      */ 
/*  443 */     int j = 0;
/*  444 */     while (j < table_alphabeticSuffix.length) {
/*  445 */       if (this.initELC == table_alphabeticSuffix[j]) {
/*  446 */         this.alphabeticSuffix = getString(table_alphabeticSuffix[(j + 1)]);
/*  447 */         return this.initELC;
/*      */       }
/*  449 */       j += 2;
/*      */     }
/*  451 */     return this.initELC;
/*      */   }
/*      */ 
/*      */   private void initAllComponentFonts()
/*      */   {
/*  468 */     short[] arrayOfShort1 = getFallbackScripts();
/*  469 */     for (int i = 0; i < 5; i++) {
/*  470 */       short[] arrayOfShort2 = getCoreScripts(i);
/*  471 */       this.compCoreNum[i] = arrayOfShort2.length;
/*      */ 
/*  479 */       int[][] arrayOfInt = new int[arrayOfShort2.length][];
/*  480 */       for (int j = 0; j < arrayOfShort2.length; j++) {
/*  481 */         arrayOfInt[j] = getExclusionRanges(arrayOfShort2[j]);
/*      */       }
/*  483 */       this.compExclusions[i] = arrayOfInt;
/*      */ 
/*  485 */       for (j = 0; j < 4; j++)
/*      */       {
/*  487 */         Object localObject = new short[arrayOfShort2.length + arrayOfShort1.length];
/*      */ 
/*  489 */         for (int k = 0; k < arrayOfShort2.length; k++) {
/*  490 */           localObject[k] = getComponentFontID(arrayOfShort2[k], i, j);
/*      */ 
/*  492 */           if ((this.preferLocaleFonts) && (localeMap != null) && (this.fontManager.usingAlternateFontforJALocales()))
/*      */           {
/*  494 */             localObject[k] = remapLocaleMap(i, j, arrayOfShort2[k], localObject[k]);
/*      */           }
/*      */ 
/*  497 */           if (this.preferPropFonts) {
/*  498 */             localObject[k] = remapProportional(i, localObject[k]);
/*      */           }
/*      */ 
/*  501 */           this.coreFontNameIDs.add(Short.valueOf(localObject[k]));
/*      */         }
/*      */ 
/*  504 */         for (int m = 0; m < arrayOfShort1.length; m++) {
/*  505 */           short s = getComponentFontID(arrayOfShort1[m], i, j);
/*      */ 
/*  507 */           if ((this.preferLocaleFonts) && (localeMap != null) && (this.fontManager.usingAlternateFontforJALocales()))
/*      */           {
/*  509 */             s = remapLocaleMap(i, j, arrayOfShort1[m], s);
/*      */           }
/*  511 */           if (this.preferPropFonts) {
/*  512 */             s = remapProportional(i, s);
/*      */           }
/*  514 */           if (!contains((short[])localObject, s, k))
/*      */           {
/*  521 */             this.fallbackFontNameIDs.add(Short.valueOf(s));
/*  522 */             localObject[(k++)] = s;
/*      */           }
/*      */         }
/*  524 */         if (k < localObject.length) {
/*  525 */           short[] arrayOfShort3 = new short[k];
/*  526 */           System.arraycopy(localObject, 0, arrayOfShort3, 0, k);
/*  527 */           localObject = arrayOfShort3;
/*      */         }
/*  529 */         this.compFontNameIDs[i][j] = localObject;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private short remapLocaleMap(int paramInt1, int paramInt2, short paramShort1, short paramShort2) {
/*  535 */     String str1 = getString(table_scriptIDs[paramShort1]);
/*      */ 
/*  537 */     String str2 = (String)localeMap.get(str1);
/*      */     String str4;
/*  538 */     if (str2 == null) {
/*  539 */       String str3 = fontNames[paramInt1];
/*  540 */       str4 = styleNames[paramInt2];
/*  541 */       str2 = (String)localeMap.get(str3 + "." + str4 + "." + str1);
/*      */     }
/*  543 */     if (str2 == null) {
/*  544 */       return paramShort2;
/*      */     }
/*      */ 
/*  547 */     for (int i = 0; i < table_componentFontNameIDs.length; i++) {
/*  548 */       str4 = getString(table_componentFontNameIDs[i]);
/*  549 */       if (str2.equalsIgnoreCase(str4)) {
/*  550 */         paramShort2 = (short)i;
/*  551 */         break;
/*      */       }
/*      */     }
/*  554 */     return paramShort2;
/*      */   }
/*      */ 
/*      */   public static boolean hasMonoToPropMap() {
/*  558 */     return (table_proportionals != null) && (table_proportionals.length != 0);
/*      */   }
/*      */ 
/*      */   private short remapProportional(int paramInt, short paramShort) {
/*  562 */     if ((this.preferPropFonts) && (table_proportionals.length != 0) && (paramInt != 2) && (paramInt != 4))
/*      */     {
/*  566 */       int i = 0;
/*  567 */       while (i < table_proportionals.length) {
/*  568 */         if (table_proportionals[i] == paramShort) {
/*  569 */           return table_proportionals[(i + 1)];
/*      */         }
/*  571 */         i += 2;
/*      */       }
/*      */     }
/*  574 */     return paramShort;
/*      */   }
/*      */ 
/*      */   public static boolean isLogicalFontFamilyName(String paramString)
/*      */   {
/*  595 */     return isLogicalFontFamilyNameLC(paramString.toLowerCase(Locale.ENGLISH));
/*      */   }
/*      */ 
/*      */   public static boolean isLogicalFontFamilyNameLC(String paramString)
/*      */   {
/*  603 */     for (int i = 0; i < fontNames.length; i++) {
/*  604 */       if (paramString.equals(fontNames[i])) {
/*  605 */         return true;
/*      */       }
/*      */     }
/*  608 */     return false;
/*      */   }
/*      */ 
/*      */   private static boolean isLogicalFontStyleName(String paramString)
/*      */   {
/*  615 */     for (int i = 0; i < styleNames.length; i++) {
/*  616 */       if (paramString.equals(styleNames[i])) {
/*  617 */         return true;
/*      */       }
/*      */     }
/*  620 */     return false;
/*      */   }
/*      */ 
/*      */   public static boolean isLogicalFontFaceName(String paramString)
/*      */   {
/*  628 */     return isLogicalFontFaceNameLC(paramString.toLowerCase(Locale.ENGLISH));
/*      */   }
/*      */ 
/*      */   public static boolean isLogicalFontFaceNameLC(String paramString)
/*      */   {
/*  636 */     int i = paramString.indexOf('.');
/*  637 */     if (i >= 0) {
/*  638 */       String str1 = paramString.substring(0, i);
/*  639 */       String str2 = paramString.substring(i + 1);
/*  640 */       return (isLogicalFontFamilyName(str1)) && (isLogicalFontStyleName(str2));
/*      */     }
/*      */ 
/*  643 */     return isLogicalFontFamilyName(paramString);
/*      */   }
/*      */ 
/*      */   protected static int getFontIndex(String paramString)
/*      */   {
/*  648 */     return getArrayIndex(fontNames, paramString);
/*      */   }
/*      */ 
/*      */   protected static int getStyleIndex(String paramString) {
/*  652 */     return getArrayIndex(styleNames, paramString);
/*      */   }
/*      */ 
/*      */   private static int getArrayIndex(String[] paramArrayOfString, String paramString) {
/*  656 */     for (int i = 0; i < paramArrayOfString.length; i++) {
/*  657 */       if (paramString.equals(paramArrayOfString[i])) {
/*  658 */         return i;
/*      */       }
/*      */     }
/*  661 */     if (!$assertionsDisabled) throw new AssertionError();
/*  662 */     return 0;
/*      */   }
/*      */ 
/*      */   protected static int getStyleIndex(int paramInt) {
/*  666 */     switch (paramInt) {
/*      */     case 0:
/*  668 */       return 0;
/*      */     case 1:
/*  670 */       return 1;
/*      */     case 2:
/*  672 */       return 2;
/*      */     case 3:
/*  674 */       return 3;
/*      */     }
/*  676 */     return 0;
/*      */   }
/*      */ 
/*      */   protected static String getFontName(int paramInt)
/*      */   {
/*  681 */     return fontNames[paramInt];
/*      */   }
/*      */ 
/*      */   protected static String getStyleName(int paramInt) {
/*  685 */     return styleNames[paramInt];
/*      */   }
/*      */ 
/*      */   public static String getLogicalFontFaceName(String paramString, int paramInt)
/*      */   {
/*  694 */     assert (isLogicalFontFamilyName(paramString));
/*  695 */     return paramString.toLowerCase(Locale.ENGLISH) + "." + getStyleString(paramInt);
/*      */   }
/*      */ 
/*      */   public static String getStyleString(int paramInt)
/*      */   {
/*  704 */     return getStyleName(getStyleIndex(paramInt));
/*      */   }
/*      */ 
/*      */   public abstract String getFallbackFamilyName(String paramString1, String paramString2);
/*      */ 
/*      */   protected String getCompatibilityFamilyName(String paramString)
/*      */   {
/*  721 */     paramString = paramString.toLowerCase(Locale.ENGLISH);
/*  722 */     if (paramString.equals("timesroman"))
/*  723 */       return "serif";
/*  724 */     if (paramString.equals("helvetica"))
/*  725 */       return "sansserif";
/*  726 */     if (paramString.equals("courier")) {
/*  727 */       return "monospaced";
/*      */     }
/*  729 */     return null;
/*      */   }
/*      */ 
/*      */   protected String mapFileName(String paramString)
/*      */   {
/*  739 */     return paramString;
/*      */   }
/*      */ 
/*      */   protected abstract void initReorderMap();
/*      */ 
/*      */   private void shuffle(String[] paramArrayOfString, int paramInt1, int paramInt2)
/*      */   {
/*  758 */     if (paramInt2 >= paramInt1) {
/*  759 */       return;
/*      */     }
/*  761 */     String str = paramArrayOfString[paramInt1];
/*  762 */     for (int i = paramInt1; i > paramInt2; i--) {
/*  763 */       paramArrayOfString[i] = paramArrayOfString[(i - 1)];
/*      */     }
/*  765 */     paramArrayOfString[paramInt2] = str;
/*      */   }
/*      */ 
/*      */   public static boolean willReorderForStartupLocale()
/*      */   {
/*  773 */     return getReorderSequence() != null;
/*      */   }
/*      */ 
/*      */   private static Object getReorderSequence() {
/*  777 */     if (fontConfig.reorderMap == null) {
/*  778 */       fontConfig.initReorderMap();
/*      */     }
/*  780 */     HashMap localHashMap = fontConfig.reorderMap;
/*      */ 
/*  783 */     String str1 = startupLocale.getLanguage();
/*  784 */     String str2 = startupLocale.getCountry();
/*  785 */     Object localObject = localHashMap.get(encoding + "." + str1 + "." + str2);
/*  786 */     if (localObject == null) {
/*  787 */       localObject = localHashMap.get(encoding + "." + str1);
/*      */     }
/*  789 */     if (localObject == null) {
/*  790 */       localObject = localHashMap.get(encoding);
/*      */     }
/*  792 */     return localObject;
/*      */   }
/*      */ 
/*      */   private void reorderSequenceForLocale(String[] paramArrayOfString)
/*      */   {
/*  800 */     Object localObject = getReorderSequence();
/*  801 */     if ((localObject instanceof String)) {
/*  802 */       for (int i = 0; i < paramArrayOfString.length; i++)
/*  803 */         if (paramArrayOfString[i].equals(localObject)) {
/*  804 */           shuffle(paramArrayOfString, i, 0);
/*  805 */           return;
/*      */         }
/*      */     }
/*  808 */     else if ((localObject instanceof String[])) {
/*  809 */       String[] arrayOfString = (String[])localObject;
/*  810 */       for (int j = 0; j < arrayOfString.length; j++)
/*  811 */         for (int k = 0; k < paramArrayOfString.length; k++)
/*  812 */           if (paramArrayOfString[k].equals(arrayOfString[j]))
/*  813 */             shuffle(paramArrayOfString, k, j);
/*      */     }
/*      */   }
/*      */ 
/*      */   private static Vector splitSequence(String paramString)
/*      */   {
/*  822 */     Vector localVector = new Vector();
/*  823 */     int i = 0;
/*      */     int j;
/*  825 */     while ((j = paramString.indexOf(',', i)) >= 0) {
/*  826 */       localVector.add(paramString.substring(i, j));
/*  827 */       i = j + 1;
/*      */     }
/*  829 */     if (paramString.length() > i) {
/*  830 */       localVector.add(paramString.substring(i, paramString.length()));
/*      */     }
/*  832 */     return localVector;
/*      */   }
/*      */ 
/*      */   protected String[] split(String paramString) {
/*  836 */     Vector localVector = splitSequence(paramString);
/*  837 */     return (String[])localVector.toArray(new String[0]);
/*      */   }
/*      */ 
/*      */   public FontDescriptor[] getFontDescriptors(String paramString, int paramInt)
/*      */   {
/*  852 */     assert (isLogicalFontFamilyName(paramString));
/*  853 */     paramString = paramString.toLowerCase(Locale.ENGLISH);
/*  854 */     int i = getFontIndex(paramString);
/*  855 */     int j = getStyleIndex(paramInt);
/*  856 */     return getFontDescriptors(i, j);
/*      */   }
/*      */ 
/*      */   private FontDescriptor[] getFontDescriptors(int paramInt1, int paramInt2)
/*      */   {
/*  862 */     FontDescriptor[] arrayOfFontDescriptor = this.fontDescriptors[paramInt1][paramInt2];
/*  863 */     if (arrayOfFontDescriptor == null) {
/*  864 */       arrayOfFontDescriptor = buildFontDescriptors(paramInt1, paramInt2);
/*  865 */       this.fontDescriptors[paramInt1][paramInt2] = arrayOfFontDescriptor;
/*      */     }
/*  867 */     return arrayOfFontDescriptor;
/*      */   }
/*      */ 
/*      */   protected FontDescriptor[] buildFontDescriptors(int paramInt1, int paramInt2) {
/*  871 */     String str1 = fontNames[paramInt1];
/*  872 */     String str2 = styleNames[paramInt2];
/*      */ 
/*  874 */     short[] arrayOfShort1 = getCoreScripts(paramInt1);
/*  875 */     short[] arrayOfShort2 = this.compFontNameIDs[paramInt1][paramInt2];
/*  876 */     String[] arrayOfString1 = new String[arrayOfShort1.length];
/*  877 */     String[] arrayOfString2 = new String[arrayOfShort1.length];
/*  878 */     for (int i = 0; i < arrayOfString1.length; i++) {
/*  879 */       arrayOfString2[i] = getComponentFontName(arrayOfShort2[i]);
/*  880 */       arrayOfString1[i] = getScriptName(arrayOfShort1[i]);
/*  881 */       if ((this.alphabeticSuffix != null) && ("alphabetic".equals(arrayOfString1[i]))) {
/*  882 */         arrayOfString1[i] = (arrayOfString1[i] + "/" + this.alphabeticSuffix);
/*      */       }
/*      */     }
/*  885 */     int[][] arrayOfInt = this.compExclusions[paramInt1];
/*      */ 
/*  887 */     FontDescriptor[] arrayOfFontDescriptor = new FontDescriptor[arrayOfString2.length];
/*      */ 
/*  889 */     for (int j = 0; j < arrayOfString2.length; j++)
/*      */     {
/*  893 */       String str3 = makeAWTFontName(arrayOfString2[j], arrayOfString1[j]);
/*      */ 
/*  896 */       String str4 = getEncoding(arrayOfString2[j], arrayOfString1[j]);
/*  897 */       if (str4 == null) {
/*  898 */         str4 = "default";
/*      */       }
/*  900 */       CharsetEncoder localCharsetEncoder = getFontCharsetEncoder(str4.trim(), str3);
/*      */ 
/*  904 */       int[] arrayOfInt1 = arrayOfInt[j];
/*      */ 
/*  907 */       arrayOfFontDescriptor[j] = new FontDescriptor(str3, localCharsetEncoder, arrayOfInt1);
/*      */     }
/*  909 */     return arrayOfFontDescriptor;
/*      */   }
/*      */ 
/*      */   protected String makeAWTFontName(String paramString1, String paramString2)
/*      */   {
/*  918 */     return paramString1;
/*      */   }
/*      */ 
/*      */   protected abstract String getEncoding(String paramString1, String paramString2);
/*      */ 
/*      */   private CharsetEncoder getFontCharsetEncoder(final String paramString1, String paramString2)
/*      */   {
/*  933 */     Charset localCharset = null;
/*  934 */     if (paramString1.equals("default"))
/*  935 */       localCharset = (Charset)this.charsetRegistry.get(paramString2);
/*      */     else {
/*  937 */       localCharset = (Charset)this.charsetRegistry.get(paramString1);
/*      */     }
/*  939 */     if (localCharset != null) {
/*  940 */       return localCharset.newEncoder();
/*      */     }
/*      */ 
/*  943 */     if ((!paramString1.startsWith("sun.awt.")) && (!paramString1.equals("default"))) {
/*  944 */       localCharset = Charset.forName(paramString1);
/*      */     } else {
/*  946 */       Class localClass = (Class)AccessController.doPrivileged(new PrivilegedAction() {
/*      */         public Object run() {
/*      */           try {
/*  949 */             return Class.forName(paramString1, true, ClassLoader.getSystemClassLoader());
/*      */           }
/*      */           catch (ClassNotFoundException localClassNotFoundException) {
/*      */           }
/*  953 */           return null;
/*      */         }
/*      */       });
/*  957 */       if (localClass != null)
/*      */         try {
/*  959 */           localCharset = (Charset)localClass.newInstance();
/*      */         }
/*      */         catch (Exception localException) {
/*      */         }
/*      */     }
/*  964 */     if (localCharset == null) {
/*  965 */       localCharset = getDefaultFontCharset(paramString2);
/*      */     }
/*      */ 
/*  968 */     if (paramString1.equals("default"))
/*  969 */       this.charsetRegistry.put(paramString2, localCharset);
/*      */     else {
/*  971 */       this.charsetRegistry.put(paramString1, localCharset);
/*      */     }
/*  973 */     return localCharset.newEncoder();
/*      */   }
/*      */ 
/*      */   protected abstract Charset getDefaultFontCharset(String paramString);
/*      */ 
/*      */   public HashSet<String> getAWTFontPathSet()
/*      */   {
/*  985 */     return null;
/*      */   }
/*      */ 
/*      */   public CompositeFontDescriptor[] get2DCompositeFontInfo()
/*      */   {
/*  999 */     CompositeFontDescriptor[] arrayOfCompositeFontDescriptor = new CompositeFontDescriptor[20];
/*      */ 
/* 1001 */     String str1 = this.fontManager.getDefaultFontFile();
/* 1002 */     String str2 = this.fontManager.getDefaultFontFaceName();
/*      */ 
/* 1004 */     for (int i = 0; i < 5; i++) {
/* 1005 */       String str3 = publicFontNames[i];
/*      */ 
/* 1011 */       int[][] arrayOfInt = this.compExclusions[i];
/* 1012 */       int j = 0;
/* 1013 */       for (int k = 0; k < arrayOfInt.length; k++) {
/* 1014 */         j += arrayOfInt[k].length;
/*      */       }
/* 1016 */       int[] arrayOfInt1 = new int[j];
/* 1017 */       int[] arrayOfInt2 = new int[arrayOfInt.length];
/* 1018 */       int m = 0;
/* 1019 */       int n = 0;
/*      */       int i3;
/* 1020 */       for (int i1 = 0; i1 < arrayOfInt.length; i1++) {
/* 1021 */         int[] arrayOfInt3 = arrayOfInt[i1];
/* 1022 */         for (i3 = 0; i3 < arrayOfInt3.length; ) {
/* 1023 */           int i4 = arrayOfInt3[i3];
/* 1024 */           arrayOfInt1[(m++)] = arrayOfInt3[(i3++)];
/* 1025 */           arrayOfInt1[(m++)] = arrayOfInt3[(i3++)];
/*      */         }
/* 1027 */         arrayOfInt2[i1] = m;
/*      */       }
/*      */ 
/* 1030 */       for (i1 = 0; i1 < 4; i1++) {
/* 1031 */         int i2 = this.compFontNameIDs[i][i1].length;
/* 1032 */         i3 = 0;
/*      */ 
/* 1034 */         if (installedFallbackFontFiles != null) {
/* 1035 */           i2 += installedFallbackFontFiles.length;
/*      */         }
/* 1037 */         String str4 = str3 + "." + styleNames[i1];
/*      */ 
/* 1040 */         Object localObject1 = new String[i2];
/* 1041 */         Object localObject2 = new String[i2];
/*      */         short s1;
/* 1044 */         for (String[] arrayOfString1 = 0; arrayOfString1 < this.compFontNameIDs[i][i1].length; arrayOfString1++) {
/* 1045 */           s1 = this.compFontNameIDs[i][i1][arrayOfString1];
/* 1046 */           short s2 = getComponentFileID(s1);
/* 1047 */           localObject1[arrayOfString1] = getFaceNameFromComponentFontName(getComponentFontName(s1));
/* 1048 */           localObject2[arrayOfString1] = mapFileName(getComponentFileName(s2));
/* 1049 */           if ((localObject2[arrayOfString1] == null) || (needToSearchForFile(localObject2[arrayOfString1])))
/*      */           {
/* 1051 */             localObject2[arrayOfString1] = getFileNameFromComponentFontName(getComponentFontName(s1));
/*      */           }
/* 1053 */           if ((i3 == 0) && (str1.equals(localObject2[arrayOfString1])))
/*      */           {
/* 1055 */             i3 = 1;
/*      */           }
/*      */         }
/*      */         int i5;
/*      */         String[] arrayOfString2;
/*      */         String[] arrayOfString4;
/* 1064 */         if (i3 == 0) {
/* 1065 */           s1 = 0;
/* 1066 */           if (installedFallbackFontFiles != null) {
/* 1067 */             i5 = installedFallbackFontFiles.length;
/*      */           }
/* 1069 */           if (arrayOfString1 + i5 == i2) {
/* 1070 */             arrayOfString2 = new String[i2 + 1];
/* 1071 */             System.arraycopy(localObject1, 0, arrayOfString2, 0, arrayOfString1);
/* 1072 */             localObject1 = arrayOfString2;
/* 1073 */             arrayOfString4 = new String[i2 + 1];
/* 1074 */             System.arraycopy(localObject2, 0, arrayOfString4, 0, arrayOfString1);
/* 1075 */             localObject2 = arrayOfString4;
/*      */           }
/* 1077 */           localObject1[arrayOfString1] = str2;
/* 1078 */           localObject2[arrayOfString1] = str1;
/* 1079 */           arrayOfString1++;
/*      */         }
/*      */ 
/* 1082 */         if (installedFallbackFontFiles != null) {
/* 1083 */           for (i5 = 0; i5 < installedFallbackFontFiles.length; i5++) {
/* 1084 */             localObject1[arrayOfString1] = null;
/* 1085 */             localObject2[arrayOfString1] = installedFallbackFontFiles[i5];
/* 1086 */             arrayOfString1++;
/*      */           }
/*      */         }
/*      */ 
/* 1090 */         if (arrayOfString1 < i2) {
/* 1091 */           localObject3 = new String[arrayOfString1];
/* 1092 */           System.arraycopy(localObject1, 0, localObject3, 0, arrayOfString1);
/* 1093 */           localObject1 = localObject3;
/* 1094 */           arrayOfString2 = new String[arrayOfString1];
/* 1095 */           System.arraycopy(localObject2, 0, arrayOfString2, 0, arrayOfString1);
/* 1096 */           localObject2 = arrayOfString2;
/*      */         }
/*      */ 
/* 1101 */         Object localObject3 = arrayOfInt2;
/* 1102 */         if (arrayOfString1 != localObject3.length) {
/* 1103 */           String[] arrayOfString3 = arrayOfInt2.length;
/* 1104 */           localObject3 = new int[arrayOfString1];
/* 1105 */           System.arraycopy(arrayOfInt2, 0, localObject3, 0, arrayOfString3);
/*      */ 
/* 1107 */           for (arrayOfString4 = arrayOfString3; arrayOfString4 < arrayOfString1; arrayOfString4++) {
/* 1108 */             localObject3[arrayOfString4] = arrayOfInt1.length;
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/* 1118 */         arrayOfCompositeFontDescriptor[(i * 4 + i1)] = new CompositeFontDescriptor(str4, this.compCoreNum[i], (String[])localObject1, (String[])localObject2, arrayOfInt1, (int[])localObject3);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1128 */     return arrayOfCompositeFontDescriptor;
/*      */   }
/*      */ 
/*      */   protected abstract String getFaceNameFromComponentFontName(String paramString);
/*      */ 
/*      */   protected abstract String getFileNameFromComponentFontName(String paramString);
/*      */ 
/*      */   public boolean needToSearchForFile(String paramString)
/*      */   {
/* 1149 */     if (!FontUtilities.isLinux)
/* 1150 */       return false;
/* 1151 */     if (this.existsMap == null) {
/* 1152 */       this.existsMap = new HashMap();
/*      */     }
/* 1154 */     Boolean localBoolean = (Boolean)this.existsMap.get(paramString);
/* 1155 */     if (localBoolean == null)
/*      */     {
/* 1161 */       getNumberCoreFonts();
/* 1162 */       if (!this.coreFontFileNames.contains(paramString)) {
/* 1163 */         localBoolean = Boolean.TRUE;
/*      */       } else {
/* 1165 */         localBoolean = Boolean.valueOf(new File(paramString).exists());
/* 1166 */         this.existsMap.put(paramString, localBoolean);
/* 1167 */         if ((FontUtilities.debugFonts()) && (localBoolean == Boolean.FALSE))
/*      */         {
/* 1169 */           logger.warning("Couldn't locate font file " + paramString);
/*      */         }
/*      */       }
/*      */     }
/* 1173 */     return localBoolean == Boolean.FALSE;
/*      */   }
/*      */ 
/*      */   public int getNumberCoreFonts()
/*      */   {
/* 1186 */     if (this.numCoreFonts == -1) {
/* 1187 */       this.numCoreFonts = this.coreFontNameIDs.size();
/* 1188 */       Short[] arrayOfShort1 = new Short[0];
/* 1189 */       Short[] arrayOfShort2 = (Short[])this.coreFontNameIDs.toArray(arrayOfShort1);
/* 1190 */       Short[] arrayOfShort3 = (Short[])this.fallbackFontNameIDs.toArray(arrayOfShort1);
/*      */ 
/* 1192 */       int i = 0;
/*      */ 
/* 1194 */       for (int j = 0; j < arrayOfShort3.length; j++) {
/* 1195 */         if (this.coreFontNameIDs.contains(arrayOfShort3[j])) {
/* 1196 */           arrayOfShort3[j] = null;
/*      */         }
/*      */         else
/* 1199 */           i++;
/*      */       }
/* 1201 */       this.componentFonts = new String[this.numCoreFonts + i];
/* 1202 */       Object localObject = null;
/*      */       short s1;
/* 1203 */       for (j = 0; j < arrayOfShort2.length; j++) {
/* 1204 */         k = arrayOfShort2[j].shortValue();
/* 1205 */         s1 = getComponentFileID(k);
/* 1206 */         this.componentFonts[j] = getComponentFontName(k);
/* 1207 */         String str = getComponentFileName(s1);
/* 1208 */         if (str != null) {
/* 1209 */           this.coreFontFileNames.add(str);
/*      */         }
/* 1211 */         this.filenamesMap.put(this.componentFonts[j], mapFileName(str));
/*      */       }
/* 1213 */       for (int k = 0; k < arrayOfShort3.length; k++) {
/* 1214 */         if (arrayOfShort3[k] != null) {
/* 1215 */           s1 = arrayOfShort3[k].shortValue();
/* 1216 */           short s2 = getComponentFileID(s1);
/* 1217 */           this.componentFonts[j] = getComponentFontName(s1);
/* 1218 */           this.filenamesMap.put(this.componentFonts[j], mapFileName(getComponentFileName(s2)));
/*      */ 
/* 1220 */           j++;
/*      */         }
/*      */       }
/*      */     }
/* 1224 */     return this.numCoreFonts;
/*      */   }
/*      */ 
/*      */   public String[] getPlatformFontNames()
/*      */   {
/* 1232 */     if (this.numCoreFonts == -1) {
/* 1233 */       getNumberCoreFonts();
/*      */     }
/* 1235 */     return this.componentFonts;
/*      */   }
/*      */ 
/*      */   public String getFileNameFromPlatformName(String paramString)
/*      */   {
/* 1252 */     return (String)this.filenamesMap.get(paramString);
/*      */   }
/*      */ 
/*      */   public String getExtraFontPath()
/*      */   {
/* 1260 */     return getString(head[16]);
/*      */   }
/*      */ 
/*      */   public String getVersion() {
/* 1264 */     return getString(head[17]);
/*      */   }
/*      */ 
/*      */   protected static FontConfiguration getFontConfiguration()
/*      */   {
/* 1269 */     return fontConfig;
/*      */   }
/*      */ 
/*      */   protected void setFontConfiguration() {
/* 1273 */     fontConfig = this;
/*      */   }
/*      */ 
/*      */   private static void sanityCheck()
/*      */   {
/* 1376 */     int i = 0;
/*      */ 
/* 1380 */     String str1 = (String)AccessController.doPrivileged(new PrivilegedAction()
/*      */     {
/*      */       public Object run() {
/* 1383 */         return System.getProperty("os.name");
/*      */       }
/*      */     });
/* 1388 */     for (int j = 1; j < table_filenames.length; j++) {
/* 1389 */       if (table_filenames[j] == -1)
/*      */       {
/* 1393 */         if (str1.contains("Windows")) {
/* 1394 */           System.err.println("\n Error: <filename." + getString(table_componentFontNameIDs[j]) + "> entry is missing!!!");
/*      */ 
/* 1397 */           i++;
/*      */         }
/* 1399 */         else if ((verbose) && (!isEmpty(table_filenames))) {
/* 1400 */           System.err.println("\n Note: 'filename' entry is undefined for \"" + getString(table_componentFontNameIDs[j]) + "\"");
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1407 */     for (j = 0; j < table_scriptIDs.length; j++) {
/* 1408 */       int k = table_scriptFonts[j];
/* 1409 */       if (k == 0) {
/* 1410 */         System.out.println("\n Error: <allfonts." + getString(table_scriptIDs[j]) + "> entry is missing!!!");
/*      */ 
/* 1413 */         i++;
/*      */       }
/* 1415 */       else if (k < 0) {
/* 1416 */         k = (short)-k;
/* 1417 */         for (int m = 0; m < 5; m++) {
/* 1418 */           for (int n = 0; n < 4; n++) {
/* 1419 */             int i1 = m * 4 + n;
/* 1420 */             int i2 = table_scriptFonts[(k + i1)];
/* 1421 */             if (i2 == 0) {
/* 1422 */               System.err.println("\n Error: <" + getFontName(m) + "." + getStyleName(n) + "." + getString(table_scriptIDs[j]) + "> entry is missing!!!");
/*      */ 
/* 1427 */               i++;
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/* 1433 */     if ("SunOS".equals(str1)) {
/* 1434 */       for (j = 0; j < table_awtfontpaths.length; j++)
/* 1435 */         if (table_awtfontpaths[j] == 0) {
/* 1436 */           String str2 = getString(table_scriptIDs[j]);
/* 1437 */           if ((!str2.contains("lucida")) && (!str2.contains("dingbats")) && (!str2.contains("symbol")))
/*      */           {
/* 1442 */             System.err.println("\nError: <awtfontpath." + str2 + "> entry is missing!!!");
/*      */ 
/* 1446 */             i++;
/*      */           }
/*      */         }
/*      */     }
/* 1450 */     if (i != 0) {
/* 1451 */       System.err.println("!!THERE ARE " + i + " ERROR(S) IN " + "THE FONTCONFIG FILE, PLEASE CHECK ITS CONTENT!!\n");
/*      */ 
/* 1453 */       System.exit(1);
/*      */     }
/*      */   }
/*      */ 
/*      */   private static boolean isEmpty(short[] paramArrayOfShort) {
/* 1458 */     for (int k : paramArrayOfShort) {
/* 1459 */       if (k != -1) {
/* 1460 */         return false;
/*      */       }
/*      */     }
/* 1463 */     return true;
/*      */   }
/*      */ 
/*      */   private static void dump()
/*      */   {
/* 1468 */     System.out.println("\n----Head Table------------");
/* 1469 */     for (int i = 0; i < 20; i++) {
/* 1470 */       System.out.println("  " + i + " : " + head[i]);
/*      */     }
/* 1472 */     System.out.println("\n----scriptIDs-------------");
/* 1473 */     printTable(table_scriptIDs, 0);
/* 1474 */     System.out.println("\n----scriptFonts----------------");
/*      */     int j;
/* 1475 */     for (i = 0; i < table_scriptIDs.length; i++) {
/* 1476 */       j = table_scriptFonts[i];
/* 1477 */       if (j >= 0) {
/* 1478 */         System.out.println("  allfonts." + getString(table_scriptIDs[i]) + "=" + getString(table_componentFontNameIDs[j]));
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1484 */     for (i = 0; i < table_scriptIDs.length; i++) {
/* 1485 */       j = table_scriptFonts[i];
/* 1486 */       if (j < 0) {
/* 1487 */         j = (short)-j;
/* 1488 */         for (k = 0; k < 5; k++) {
/* 1489 */           for (int m = 0; m < 4; m++) {
/* 1490 */             int n = k * 4 + m;
/* 1491 */             int i1 = table_scriptFonts[(j + n)];
/* 1492 */             System.out.println("  " + getFontName(k) + "." + getStyleName(m) + "." + getString(table_scriptIDs[i]) + "=" + getString(table_componentFontNameIDs[i1]));
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1503 */     System.out.println("\n----elcIDs----------------");
/* 1504 */     printTable(table_elcIDs, 0);
/* 1505 */     System.out.println("\n----sequences-------------");
/* 1506 */     for (i = 0; i < table_elcIDs.length; i++) {
/* 1507 */       System.out.println("  " + i + "/" + getString(table_elcIDs[i]));
/* 1508 */       arrayOfShort = getShortArray(table_sequences[(i * 5 + 0)]);
/* 1509 */       for (k = 0; k < arrayOfShort.length; k++) {
/* 1510 */         System.out.println("     " + getString(table_scriptIDs[arrayOfShort[k]]));
/*      */       }
/*      */     }
/* 1513 */     System.out.println("\n----fontfileNameIDs-------");
/* 1514 */     printTable(table_fontfileNameIDs, 0);
/*      */ 
/* 1516 */     System.out.println("\n----componentFontNameIDs--");
/* 1517 */     printTable(table_componentFontNameIDs, 1);
/* 1518 */     System.out.println("\n----filenames-------------");
/* 1519 */     for (i = 0; i < table_filenames.length; i++) {
/* 1520 */       if (table_filenames[i] == -1)
/* 1521 */         System.out.println("  " + i + " : null");
/*      */       else {
/* 1523 */         System.out.println("  " + i + " : " + getString(table_fontfileNameIDs[table_filenames[i]]));
/*      */       }
/*      */     }
/*      */ 
/* 1527 */     System.out.println("\n----awtfontpaths---------");
/* 1528 */     for (i = 0; i < table_awtfontpaths.length; i++) {
/* 1529 */       System.out.println("  " + getString(table_scriptIDs[i]) + " : " + getString(table_awtfontpaths[i]));
/*      */     }
/*      */ 
/* 1533 */     System.out.println("\n----proportionals--------");
/* 1534 */     for (i = 0; i < table_proportionals.length; i++) {
/* 1535 */       System.out.println("  " + getString(table_componentFontNameIDs[table_proportionals[(i++)]]) + " -> " + getString(table_componentFontNameIDs[table_proportionals[i]]));
/*      */     }
/*      */ 
/* 1540 */     i = 0;
/* 1541 */     System.out.println("\n----alphabeticSuffix----");
/* 1542 */     while (i < table_alphabeticSuffix.length) {
/* 1543 */       System.out.println("    " + getString(table_elcIDs[table_alphabeticSuffix[(i++)]]) + " -> " + getString(table_alphabeticSuffix[(i++)]));
/*      */     }
/*      */ 
/* 1546 */     System.out.println("\n----String Table---------");
/* 1547 */     System.out.println("    stringID:    Num =" + table_stringIDs.length);
/* 1548 */     System.out.println("    stringTable: Size=" + table_stringTable.length * 2);
/*      */ 
/* 1550 */     System.out.println("\n----fallbackScriptIDs---");
/* 1551 */     short[] arrayOfShort = getShortArray(head[15]);
/* 1552 */     for (int k = 0; k < arrayOfShort.length; k++) {
/* 1553 */       System.out.println("  " + getString(table_scriptIDs[arrayOfShort[k]]));
/*      */     }
/* 1555 */     System.out.println("\n----appendedfontpath-----");
/* 1556 */     System.out.println("  " + getString(head[16]));
/* 1557 */     System.out.println("\n----Version--------------");
/* 1558 */     System.out.println("  " + getString(head[17]));
/*      */   }
/*      */ 
/*      */   protected static short getComponentFontID(short paramShort, int paramInt1, int paramInt2)
/*      */   {
/* 1571 */     short s = table_scriptFonts[paramShort];
/*      */ 
/* 1573 */     if (s >= 0)
/*      */     {
/* 1575 */       return s;
/*      */     }
/* 1577 */     return table_scriptFonts[(-s + paramInt1 * 4 + paramInt2)];
/*      */   }
/*      */ 
/*      */   protected static short getComponentFontIDMotif(short paramShort, int paramInt1, int paramInt2)
/*      */   {
/* 1585 */     if (table_scriptFontsMotif.length == 0) {
/* 1586 */       return 0;
/*      */     }
/* 1588 */     short s = table_scriptFontsMotif[paramShort];
/* 1589 */     if (s >= 0)
/*      */     {
/* 1591 */       return s;
/*      */     }
/* 1593 */     return table_scriptFontsMotif[(-s + paramInt1 * 4 + paramInt2)];
/*      */   }
/*      */ 
/*      */   private static int[] getExclusionRanges(short paramShort)
/*      */   {
/* 1598 */     short s = table_exclusions[paramShort];
/* 1599 */     if (s == 0) {
/* 1600 */       return EMPTY_INT_ARRAY;
/*      */     }
/* 1602 */     char[] arrayOfChar = getString(s).toCharArray();
/* 1603 */     int[] arrayOfInt = new int[arrayOfChar.length / 2];
/* 1604 */     int i = 0;
/* 1605 */     for (int j = 0; j < arrayOfInt.length; j++) {
/* 1606 */       arrayOfInt[j] = ((arrayOfChar[(i++)] << '\020') + (arrayOfChar[(i++)] & 0xFFFF));
/*      */     }
/* 1608 */     return arrayOfInt;
/*      */   }
/*      */ 
/*      */   private static boolean contains(short[] paramArrayOfShort, short paramShort, int paramInt)
/*      */   {
/* 1613 */     for (int i = 0; i < paramInt; i++) {
/* 1614 */       if (paramArrayOfShort[i] == paramShort) {
/* 1615 */         return true;
/*      */       }
/*      */     }
/* 1618 */     return false;
/*      */   }
/*      */ 
/*      */   protected static String getComponentFontName(short paramShort)
/*      */   {
/* 1623 */     if (paramShort < 0) {
/* 1624 */       return null;
/*      */     }
/* 1626 */     return getString(table_componentFontNameIDs[paramShort]);
/*      */   }
/*      */ 
/*      */   private static String getComponentFileName(short paramShort) {
/* 1630 */     if (paramShort < 0) {
/* 1631 */       return null;
/*      */     }
/* 1633 */     return getString(table_fontfileNameIDs[paramShort]);
/*      */   }
/*      */ 
/*      */   private static short getComponentFileID(short paramShort)
/*      */   {
/* 1638 */     return table_filenames[paramShort];
/*      */   }
/*      */ 
/*      */   private static String getScriptName(short paramShort) {
/* 1642 */     return getString(table_scriptIDs[paramShort]);
/*      */   }
/*      */ 
/*      */   protected short[] getCoreScripts(int paramInt)
/*      */   {
/* 1647 */     int i = getInitELC();
/*      */ 
/* 1655 */     short[] arrayOfShort = getShortArray(table_sequences[(i * 5 + paramInt)]);
/* 1656 */     if (this.preferLocaleFonts) {
/* 1657 */       if (this.reorderScripts == null) {
/* 1658 */         this.reorderScripts = new HashMap();
/*      */       }
/* 1660 */       String[] arrayOfString = new String[arrayOfShort.length];
/* 1661 */       for (int j = 0; j < arrayOfString.length; j++) {
/* 1662 */         arrayOfString[j] = getScriptName(arrayOfShort[j]);
/* 1663 */         this.reorderScripts.put(arrayOfString[j], Short.valueOf(arrayOfShort[j]));
/*      */       }
/* 1665 */       reorderSequenceForLocale(arrayOfString);
/* 1666 */       for (j = 0; j < arrayOfString.length; j++) {
/* 1667 */         arrayOfShort[j] = ((Short)this.reorderScripts.get(arrayOfString[j])).shortValue();
/*      */       }
/*      */     }
/* 1670 */     return arrayOfShort;
/*      */   }
/*      */ 
/*      */   private static short[] getFallbackScripts() {
/* 1674 */     return getShortArray(head[15]);
/*      */   }
/*      */ 
/*      */   private static void printTable(short[] paramArrayOfShort, int paramInt) {
/* 1678 */     for (int i = paramInt; i < paramArrayOfShort.length; i++)
/* 1679 */       System.out.println("  " + i + " : " + getString(paramArrayOfShort[i]));
/*      */   }
/*      */ 
/*      */   private static short[] readShortTable(DataInputStream paramDataInputStream, int paramInt)
/*      */     throws IOException
/*      */   {
/* 1685 */     if (paramInt == 0) {
/* 1686 */       return EMPTY_SHORT_ARRAY;
/*      */     }
/* 1688 */     short[] arrayOfShort = new short[paramInt];
/* 1689 */     byte[] arrayOfByte = new byte[paramInt * 2];
/* 1690 */     paramDataInputStream.read(arrayOfByte);
/* 1691 */     int i = 0; int j = 0;
/* 1692 */     while (i < paramInt) {
/* 1693 */       arrayOfShort[(i++)] = ((short)(arrayOfByte[(j++)] << 8 | arrayOfByte[(j++)] & 0xFF));
/*      */     }
/* 1695 */     return arrayOfShort;
/*      */   }
/*      */ 
/*      */   private static void writeShortTable(DataOutputStream paramDataOutputStream, short[] paramArrayOfShort) throws IOException
/*      */   {
/* 1700 */     for (int k : paramArrayOfShort)
/* 1701 */       paramDataOutputStream.writeShort(k);
/*      */   }
/*      */ 
/*      */   private static short[] toList(HashMap<String, Short> paramHashMap)
/*      */   {
/* 1706 */     short[] arrayOfShort = new short[paramHashMap.size()];
/* 1707 */     Arrays.fill(arrayOfShort, (short)-1);
/* 1708 */     for (Map.Entry localEntry : paramHashMap.entrySet()) {
/* 1709 */       arrayOfShort[((Short)localEntry.getValue()).shortValue()] = getStringID((String)localEntry.getKey());
/*      */     }
/* 1711 */     return arrayOfShort;
/*      */   }
/*      */ 
/*      */   protected static String getString(short paramShort)
/*      */   {
/* 1717 */     if (paramShort == 0) {
/* 1718 */       return null;
/*      */     }
/*      */ 
/* 1726 */     if (stringCache[paramShort] == null) {
/* 1727 */       stringCache[paramShort] = new String(table_stringTable, table_stringIDs[paramShort], table_stringIDs[(paramShort + 1)] - table_stringIDs[paramShort]);
/*      */     }
/*      */ 
/* 1732 */     return stringCache[paramShort];
/*      */   }
/*      */ 
/*      */   private static short[] getShortArray(short paramShort) {
/* 1736 */     String str = getString(paramShort);
/* 1737 */     char[] arrayOfChar = str.toCharArray();
/* 1738 */     short[] arrayOfShort = new short[arrayOfChar.length];
/* 1739 */     for (int i = 0; i < arrayOfChar.length; i++) {
/* 1740 */       arrayOfShort[i] = ((short)(arrayOfChar[i] & 0xFFFF));
/*      */     }
/* 1742 */     return arrayOfShort;
/*      */   }
/*      */ 
/*      */   private static short getStringID(String paramString) {
/* 1746 */     if (paramString == null) {
/* 1747 */       return 0;
/*      */     }
/* 1749 */     int i = (short)stringTable.length();
/* 1750 */     stringTable.append(paramString);
/* 1751 */     int j = (short)stringTable.length();
/*      */ 
/* 1753 */     stringIDs[stringIDNum] = i;
/* 1754 */     stringIDs[(stringIDNum + 1)] = j;
/* 1755 */     stringIDNum = (short)(stringIDNum + 1);
/* 1756 */     if (stringIDNum + 1 >= stringIDs.length) {
/* 1757 */       short[] arrayOfShort = new short[stringIDNum + 1000];
/* 1758 */       System.arraycopy(stringIDs, 0, arrayOfShort, 0, stringIDNum);
/* 1759 */       stringIDs = arrayOfShort;
/*      */     }
/* 1761 */     return (short)(stringIDNum - 1);
/*      */   }
/*      */ 
/*      */   private static short getShortArrayID(short[] paramArrayOfShort) {
/* 1765 */     char[] arrayOfChar = new char[paramArrayOfShort.length];
/* 1766 */     for (int i = 0; i < paramArrayOfShort.length; i++) {
/* 1767 */       arrayOfChar[i] = ((char)paramArrayOfShort[i]);
/*      */     }
/* 1769 */     String str = new String(arrayOfChar);
/* 1770 */     return getStringID(str); } 
/*      */   static class PropertiesHandler { private HashMap<String, Short> scriptIDs;
/*      */     private HashMap<String, Short> elcIDs;
/*      */     private HashMap<String, Short> componentFontNameIDs;
/*      */     private HashMap<String, Short> fontfileNameIDs;
/*      */     private HashMap<String, Integer> logicalFontIDs;
/*      */     private HashMap<String, Integer> fontStyleIDs;
/*      */     private HashMap<Short, Short> filenames;
/*      */     private HashMap<Short, short[]> sequences;
/*      */     private HashMap<Short, Short[]> scriptFonts;
/*      */     private HashMap<Short, Short> scriptAllfonts;
/*      */     private HashMap<Short, int[]> exclusions;
/*      */     private HashMap<Short, Short> awtfontpaths;
/*      */     private HashMap<Short, Short> proportionals;
/*      */     private HashMap<Short, Short> scriptAllfontsMotif;
/*      */     private HashMap<Short, Short[]> scriptFontsMotif;
/*      */     private HashMap<Short, Short> alphabeticSuffix;
/*      */     private short[] fallbackScriptIDs;
/*      */     private String version;
/*      */     private String appendedfontpath;
/*      */ 
/* 1784 */     public void load(InputStream paramInputStream) throws IOException { initLogicalNameStyle();
/* 1785 */       initHashMaps();
/* 1786 */       FontProperties localFontProperties = new FontProperties();
/* 1787 */       localFontProperties.load(paramInputStream);
/* 1788 */       initBinaryTable();
/*      */     }
/*      */ 
/*      */     private void initBinaryTable()
/*      */     {
/* 1793 */       FontConfiguration.access$002(new short[20]);
/* 1794 */       FontConfiguration.head[0] = 20;
/*      */ 
/* 1796 */       FontConfiguration.access$102(FontConfiguration.toList(this.scriptIDs));
/*      */ 
/* 1803 */       FontConfiguration.head[1] = ((short)(FontConfiguration.head[0] + FontConfiguration.table_scriptIDs.length));
/* 1804 */       int i = FontConfiguration.table_scriptIDs.length + this.scriptFonts.size() * 20;
/* 1805 */       FontConfiguration.access$302(new short[i]);
/*      */ 
/* 1807 */       for (Iterator localIterator = this.scriptAllfonts.entrySet().iterator(); localIterator.hasNext(); ) { localObject1 = (Map.Entry)localIterator.next();
/* 1808 */         FontConfiguration.table_scriptFonts[((Short)localObject1.getKey()).intValue()] = ((Short)((Map.Entry)localObject1).getValue()).shortValue();
/*      */       }
/* 1810 */       int j = FontConfiguration.table_scriptIDs.length;
/* 1811 */       for (Object localObject1 = this.scriptFonts.entrySet().iterator(); ((Iterator)localObject1).hasNext(); ) { localObject2 = (Map.Entry)((Iterator)localObject1).next();
/* 1812 */         FontConfiguration.table_scriptFonts[((Short)localObject2.getKey()).intValue()] = ((short)-j);
/* 1813 */         Short[] arrayOfShort = (Short[])((Map.Entry)localObject2).getValue();
/* 1814 */         for (int n = 0; n < 20; n++) {
/* 1815 */           if (arrayOfShort[n] != null)
/* 1816 */             FontConfiguration.table_scriptFonts[(j++)] = arrayOfShort[n].shortValue();
/*      */           else {
/* 1818 */             FontConfiguration.table_scriptFonts[(j++)] = 0;
/*      */           }
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1824 */       FontConfiguration.head[2] = ((short)(FontConfiguration.head[1] + FontConfiguration.table_scriptFonts.length));
/* 1825 */       FontConfiguration.access$402(FontConfiguration.toList(this.elcIDs));
/*      */ 
/* 1828 */       FontConfiguration.head[3] = ((short)(FontConfiguration.head[2] + FontConfiguration.table_elcIDs.length));
/* 1829 */       FontConfiguration.access$502(new short[this.elcIDs.size() * 5]);
/* 1830 */       for (localObject1 = this.sequences.entrySet().iterator(); ((Iterator)localObject1).hasNext(); ) { localObject2 = (Map.Entry)((Iterator)localObject1).next();
/*      */ 
/* 1832 */         int m = ((Short)((Map.Entry)localObject2).getKey()).intValue();
/* 1833 */         localObject4 = (short[])((Map.Entry)localObject2).getValue();
/*      */ 
/* 1841 */         if (localObject4.length == 1)
/*      */         {
/* 1843 */           for (i1 = 0; i1 < 5; i1++)
/* 1844 */             FontConfiguration.table_sequences[(m * 5 + i1)] = localObject4[0];
/*      */         }
/*      */         else
/* 1847 */           for (i1 = 0; i1 < 5; i1++)
/* 1848 */             FontConfiguration.table_sequences[(m * 5 + i1)] = localObject4[i1];
/*      */       }
/* 1865 */       Object localObject4;
/*      */       int i1;
/* 1853 */       FontConfiguration.head[4] = ((short)(FontConfiguration.head[3] + FontConfiguration.table_sequences.length));
/* 1854 */       FontConfiguration.access$602(FontConfiguration.toList(this.fontfileNameIDs));
/*      */ 
/* 1857 */       FontConfiguration.head[5] = ((short)(FontConfiguration.head[4] + FontConfiguration.table_fontfileNameIDs.length));
/* 1858 */       FontConfiguration.access$702(FontConfiguration.toList(this.componentFontNameIDs));
/*      */ 
/* 1861 */       FontConfiguration.head[6] = ((short)(FontConfiguration.head[5] + FontConfiguration.table_componentFontNameIDs.length));
/* 1862 */       FontConfiguration.access$802(new short[FontConfiguration.table_componentFontNameIDs.length]);
/* 1863 */       Arrays.fill(FontConfiguration.table_filenames, (short)-1);
/*      */ 
/* 1865 */       for (localObject1 = this.filenames.entrySet().iterator(); ((Iterator)localObject1).hasNext(); ) { localObject2 = (Map.Entry)((Iterator)localObject1).next();
/* 1866 */         FontConfiguration.table_filenames[((Short)localObject2.getKey()).shortValue()] = ((Short)((Map.Entry)localObject2).getValue()).shortValue();
/*      */       }
/*      */ 
/* 1871 */       FontConfiguration.head[7] = ((short)(FontConfiguration.head[6] + FontConfiguration.table_filenames.length));
/* 1872 */       FontConfiguration.table_awtfontpaths = new short[FontConfiguration.table_scriptIDs.length];
/* 1873 */       for (localObject1 = this.awtfontpaths.entrySet().iterator(); ((Iterator)localObject1).hasNext(); ) { localObject2 = (Map.Entry)((Iterator)localObject1).next();
/* 1874 */         FontConfiguration.table_awtfontpaths[((Short)localObject2.getKey()).shortValue()] = ((Short)((Map.Entry)localObject2).getValue()).shortValue();
/*      */       }
/*      */ 
/* 1878 */       FontConfiguration.head[8] = ((short)(FontConfiguration.head[7] + FontConfiguration.table_awtfontpaths.length));
/* 1879 */       FontConfiguration.access$902(new short[this.scriptIDs.size()]);
/* 1880 */       for (localObject1 = this.exclusions.entrySet().iterator(); ((Iterator)localObject1).hasNext(); ) { localObject2 = (Map.Entry)((Iterator)localObject1).next();
/* 1881 */         localObject3 = (int[])((Map.Entry)localObject2).getValue();
/* 1882 */         localObject4 = new char[localObject3.length * 2];
/* 1883 */         i1 = 0;
/* 1884 */         for (int i2 = 0; i2 < localObject3.length; i2++) {
/* 1885 */           localObject4[(i1++)] = ((char)(localObject3[i2] >> 16));
/* 1886 */           localObject4[(i1++)] = ((char)(localObject3[i2] & 0xFFFF));
/*      */         }
/* 1888 */         FontConfiguration.table_exclusions[((Short)localObject2.getKey()).shortValue()] = FontConfiguration.getStringID(new String((char[])localObject4));
/*      */       }
/* 1894 */       Object localObject3;
/* 1891 */       FontConfiguration.head[9] = ((short)(FontConfiguration.head[8] + FontConfiguration.table_exclusions.length));
/* 1892 */       FontConfiguration.access$1102(new short[this.proportionals.size() * 2]);
/* 1893 */       int k = 0;
/* 1894 */       for (Object localObject2 = this.proportionals.entrySet().iterator(); ((Iterator)localObject2).hasNext(); ) { localObject3 = (Map.Entry)((Iterator)localObject2).next();
/* 1895 */         FontConfiguration.table_proportionals[(k++)] = ((Short)((Map.Entry)localObject3).getKey()).shortValue();
/* 1896 */         FontConfiguration.table_proportionals[(k++)] = ((Short)((Map.Entry)localObject3).getValue()).shortValue();
/*      */       }
/*      */ 
/* 1900 */       FontConfiguration.head[10] = ((short)(FontConfiguration.head[9] + FontConfiguration.table_proportionals.length));
/* 1901 */       if ((this.scriptAllfontsMotif.size() != 0) || (this.scriptFontsMotif.size() != 0)) {
/* 1902 */         i = FontConfiguration.table_scriptIDs.length + this.scriptFontsMotif.size() * 20;
/* 1903 */         FontConfiguration.access$1202(new short[i]);
/*      */ 
/* 1905 */         for (localObject2 = this.scriptAllfontsMotif.entrySet().iterator(); ((Iterator)localObject2).hasNext(); ) { localObject3 = (Map.Entry)((Iterator)localObject2).next();
/* 1906 */           FontConfiguration.table_scriptFontsMotif[((Short)localObject3.getKey()).intValue()] = ((Short)((Map.Entry)localObject3).getValue()).shortValue();
/*      */         }
/*      */ 
/* 1909 */         j = FontConfiguration.table_scriptIDs.length;
/* 1910 */         for (localObject2 = this.scriptFontsMotif.entrySet().iterator(); ((Iterator)localObject2).hasNext(); ) { localObject3 = (Map.Entry)((Iterator)localObject2).next();
/* 1911 */           FontConfiguration.table_scriptFontsMotif[((Short)localObject3.getKey()).intValue()] = ((short)-j);
/* 1912 */           localObject4 = (Short[])((Map.Entry)localObject3).getValue();
/* 1913 */           i1 = 0;
/* 1914 */           while (i1 < 20) {
/* 1915 */             if (localObject4[i1] != null)
/* 1916 */               FontConfiguration.table_scriptFontsMotif[(j++)] = localObject4[i1].shortValue();
/*      */             else {
/* 1918 */               FontConfiguration.table_scriptFontsMotif[(j++)] = 0;
/*      */             }
/* 1920 */             i1++;
/*      */           } }
/*      */       }
/*      */       else {
/* 1924 */         FontConfiguration.access$1202(FontConfiguration.EMPTY_SHORT_ARRAY);
/*      */       }
/*      */ 
/* 1928 */       FontConfiguration.head[11] = ((short)(FontConfiguration.head[10] + FontConfiguration.table_scriptFontsMotif.length));
/* 1929 */       FontConfiguration.access$1402(new short[this.alphabeticSuffix.size() * 2]);
/* 1930 */       k = 0;
/* 1931 */       for (localObject2 = this.alphabeticSuffix.entrySet().iterator(); ((Iterator)localObject2).hasNext(); ) { localObject3 = (Map.Entry)((Iterator)localObject2).next();
/* 1932 */         FontConfiguration.table_alphabeticSuffix[(k++)] = ((Short)((Map.Entry)localObject3).getKey()).shortValue();
/* 1933 */         FontConfiguration.table_alphabeticSuffix[(k++)] = ((Short)((Map.Entry)localObject3).getValue()).shortValue();
/*      */       }
/*      */ 
/* 1937 */       FontConfiguration.head[15] = FontConfiguration.getShortArrayID(this.fallbackScriptIDs);
/*      */ 
/* 1940 */       FontConfiguration.head[16] = FontConfiguration.getStringID(this.appendedfontpath);
/*      */ 
/* 1943 */       FontConfiguration.head[17] = FontConfiguration.getStringID(this.version);
/*      */ 
/* 1946 */       FontConfiguration.head[12] = ((short)(FontConfiguration.head[11] + FontConfiguration.table_alphabeticSuffix.length));
/* 1947 */       FontConfiguration.access$1602(new short[FontConfiguration.stringIDNum + 1]);
/* 1948 */       System.arraycopy(FontConfiguration.stringIDs, 0, FontConfiguration.table_stringIDs, 0, FontConfiguration.stringIDNum + 1);
/*      */ 
/* 1951 */       FontConfiguration.head[13] = ((short)(FontConfiguration.head[12] + FontConfiguration.stringIDNum + 1));
/* 1952 */       FontConfiguration.access$1902(FontConfiguration.stringTable.toString().toCharArray());
/*      */ 
/* 1954 */       FontConfiguration.head[14] = ((short)(FontConfiguration.head[13] + FontConfiguration.stringTable.length()));
/*      */ 
/* 1957 */       FontConfiguration.access$2102(new String[FontConfiguration.table_stringIDs.length]);
/*      */     }
/*      */ 
/*      */     private void initLogicalNameStyle()
/*      */     {
/* 2010 */       this.logicalFontIDs = new HashMap();
/* 2011 */       this.fontStyleIDs = new HashMap();
/* 2012 */       this.logicalFontIDs.put("serif", Integer.valueOf(0));
/* 2013 */       this.logicalFontIDs.put("sansserif", Integer.valueOf(1));
/* 2014 */       this.logicalFontIDs.put("monospaced", Integer.valueOf(2));
/* 2015 */       this.logicalFontIDs.put("dialog", Integer.valueOf(3));
/* 2016 */       this.logicalFontIDs.put("dialoginput", Integer.valueOf(4));
/* 2017 */       this.fontStyleIDs.put("plain", Integer.valueOf(0));
/* 2018 */       this.fontStyleIDs.put("bold", Integer.valueOf(1));
/* 2019 */       this.fontStyleIDs.put("italic", Integer.valueOf(2));
/* 2020 */       this.fontStyleIDs.put("bolditalic", Integer.valueOf(3));
/*      */     }
/*      */ 
/*      */     private void initHashMaps() {
/* 2024 */       this.scriptIDs = new HashMap();
/* 2025 */       this.elcIDs = new HashMap();
/* 2026 */       this.componentFontNameIDs = new HashMap();
/*      */ 
/* 2030 */       this.componentFontNameIDs.put("", Short.valueOf((short)0));
/*      */ 
/* 2032 */       this.fontfileNameIDs = new HashMap();
/* 2033 */       this.filenames = new HashMap();
/* 2034 */       this.sequences = new HashMap();
/* 2035 */       this.scriptFonts = new HashMap();
/* 2036 */       this.scriptAllfonts = new HashMap();
/* 2037 */       this.exclusions = new HashMap();
/* 2038 */       this.awtfontpaths = new HashMap();
/* 2039 */       this.proportionals = new HashMap();
/* 2040 */       this.scriptFontsMotif = new HashMap();
/* 2041 */       this.scriptAllfontsMotif = new HashMap();
/* 2042 */       this.alphabeticSuffix = new HashMap();
/* 2043 */       this.fallbackScriptIDs = FontConfiguration.EMPTY_SHORT_ARRAY;
/*      */     }
/*      */ 
/*      */     private int[] parseExclusions(String paramString1, String paramString2)
/*      */     {
/* 2051 */       if (paramString2 == null) {
/* 2052 */         return FontConfiguration.EMPTY_INT_ARRAY;
/*      */       }
/*      */ 
/* 2055 */       int i = 1;
/* 2056 */       int j = 0;
/* 2057 */       while ((j = paramString2.indexOf(',', j)) != -1) {
/* 2058 */         i++;
/* 2059 */         j++;
/*      */       }
/* 2061 */       int[] arrayOfInt = new int[i * 2];
/* 2062 */       j = 0;
/* 2063 */       int k = 0;
/* 2064 */       for (int m = 0; m < i * 2; )
/*      */       {
/* 2066 */         int n = 0; int i1 = 0;
/*      */         try {
/* 2068 */           k = paramString2.indexOf('-', j);
/* 2069 */           String str1 = paramString2.substring(j, k);
/* 2070 */           j = k + 1;
/* 2071 */           k = paramString2.indexOf(',', j);
/* 2072 */           if (k == -1) {
/* 2073 */             k = paramString2.length();
/*      */           }
/* 2075 */           String str2 = paramString2.substring(j, k);
/* 2076 */           j = k + 1;
/* 2077 */           int i2 = str1.length();
/* 2078 */           int i3 = str2.length();
/* 2079 */           if (((i2 != 4) && (i2 != 6)) || ((i3 != 4) && (i3 != 6)))
/*      */           {
/* 2081 */             throw new Exception();
/*      */           }
/* 2083 */           n = Integer.parseInt(str1, 16);
/* 2084 */           i1 = Integer.parseInt(str2, 16);
/* 2085 */           if (n > i1)
/* 2086 */             throw new Exception();
/*      */         }
/*      */         catch (Exception localException) {
/* 2089 */           if ((FontUtilities.debugFonts()) && (FontConfiguration.logger != null))
/*      */           {
/* 2091 */             FontConfiguration.logger.config("Failed parsing " + paramString1 + " property of font configuration.");
/*      */           }
/*      */ 
/* 2095 */           return FontConfiguration.EMPTY_INT_ARRAY;
/*      */         }
/* 2097 */         arrayOfInt[(m++)] = n;
/* 2098 */         arrayOfInt[(m++)] = i1;
/*      */       }
/* 2100 */       return arrayOfInt;
/*      */     }
/*      */ 
/*      */     private Short getID(HashMap<String, Short> paramHashMap, String paramString) {
/* 2104 */       Short localShort = (Short)paramHashMap.get(paramString);
/* 2105 */       if (localShort == null) {
/* 2106 */         paramHashMap.put(paramString, Short.valueOf((short)paramHashMap.size()));
/* 2107 */         return (Short)paramHashMap.get(paramString);
/*      */       }
/* 2109 */       return localShort;
/*      */     }
/*      */ 
/*      */     private void parseProperty(String paramString1, String paramString2)
/*      */     {
/* 2120 */       if (paramString1.startsWith("filename."))
/*      */       {
/* 2123 */         paramString1 = paramString1.substring(9);
/* 2124 */         if (!"MingLiU_HKSCS".equals(paramString1)) {
/* 2125 */           paramString1 = paramString1.replace('_', ' ');
/*      */         }
/* 2127 */         Short localShort1 = getID(this.componentFontNameIDs, paramString1);
/* 2128 */         Short localShort2 = getID(this.fontfileNameIDs, paramString2);
/*      */ 
/* 2131 */         this.filenames.put(localShort1, localShort2);
/* 2132 */       } else if (paramString1.startsWith("exclusion.")) {
/* 2133 */         paramString1 = paramString1.substring(10);
/* 2134 */         this.exclusions.put(getID(this.scriptIDs, paramString1), parseExclusions(paramString1, paramString2));
/*      */       }
/*      */       else
/*      */       {
/*      */         int i;
/*      */         int j;
/*      */         Object localObject;
/*      */         Short localShort3;
/* 2135 */         if (paramString1.startsWith("sequence.")) {
/* 2136 */           paramString1 = paramString1.substring(9);
/* 2137 */           i = 0;
/* 2138 */           j = 0;
/*      */ 
/* 2141 */           String[] arrayOfString = (String[])FontConfiguration.splitSequence(paramString2).toArray(FontConfiguration.EMPTY_STRING_ARRAY);
/* 2142 */           localObject = new short[arrayOfString.length];
/* 2143 */           for (int m = 0; m < arrayOfString.length; m++) {
/* 2144 */             if ("alphabetic/default".equals(arrayOfString[m]))
/*      */             {
/* 2146 */               arrayOfString[m] = "alphabetic";
/* 2147 */               i = 1;
/* 2148 */             } else if ("alphabetic/1252".equals(arrayOfString[m]))
/*      */             {
/* 2150 */               arrayOfString[m] = "alphabetic";
/* 2151 */               j = 1;
/*      */             }
/* 2153 */             localObject[m] = getID(this.scriptIDs, arrayOfString[m]).shortValue();
/*      */           }
/*      */ 
/* 2157 */           m = FontConfiguration.getShortArrayID((short[])localObject);
/* 2158 */           localShort3 = null;
/* 2159 */           int n = paramString1.indexOf('.');
/* 2160 */           if (n == -1) {
/* 2161 */             if ("fallback".equals(paramString1)) {
/* 2162 */               this.fallbackScriptIDs = ((short[])localObject);
/* 2163 */               return;
/*      */             }
/* 2165 */             if ("allfonts".equals(paramString1)) {
/* 2166 */               localShort3 = getID(this.elcIDs, "NULL.NULL.NULL");
/*      */             }
/* 2168 */             else if (FontConfiguration.logger != null) {
/* 2169 */               FontConfiguration.logger.config("Error sequence def: <sequence." + paramString1 + ">");
/*      */             }
/*      */           }
/*      */           else
/*      */           {
/* 2174 */             localShort3 = getID(this.elcIDs, paramString1.substring(n + 1));
/*      */ 
/* 2176 */             paramString1 = paramString1.substring(0, n);
/*      */           }
/* 2178 */           short[] arrayOfShort1 = null;
/* 2179 */           if ("allfonts".equals(paramString1)) {
/* 2180 */             arrayOfShort1 = new short[1];
/* 2181 */             arrayOfShort1[0] = m;
/*      */           } else {
/* 2183 */             arrayOfShort1 = (short[])this.sequences.get(localShort3);
/* 2184 */             if (arrayOfShort1 == null) {
/* 2185 */               arrayOfShort1 = new short[5];
/*      */             }
/* 2187 */             Integer localInteger2 = (Integer)this.logicalFontIDs.get(paramString1);
/* 2188 */             if (localInteger2 == null) {
/* 2189 */               if (FontConfiguration.logger != null) {
/* 2190 */                 FontConfiguration.logger.config("Unrecognizable logicfont name " + paramString1);
/*      */               }
/* 2192 */               return;
/*      */             }
/*      */ 
/* 2195 */             arrayOfShort1[localInteger2.intValue()] = m;
/*      */           }
/* 2197 */           this.sequences.put(localShort3, arrayOfShort1);
/* 2198 */           if (i != 0) {
/* 2199 */             this.alphabeticSuffix.put(localShort3, Short.valueOf(FontConfiguration.getStringID("default")));
/*      */           }
/* 2201 */           else if (j != 0)
/* 2202 */             this.alphabeticSuffix.put(localShort3, Short.valueOf(FontConfiguration.getStringID("1252")));
/*      */         }
/* 2204 */         else if (paramString1.startsWith("allfonts.")) {
/* 2205 */           paramString1 = paramString1.substring(9);
/* 2206 */           if (paramString1.endsWith(".motif")) {
/* 2207 */             paramString1 = paramString1.substring(0, paramString1.length() - 6);
/*      */ 
/* 2209 */             this.scriptAllfontsMotif.put(getID(this.scriptIDs, paramString1), getID(this.componentFontNameIDs, paramString2));
/*      */           } else {
/* 2211 */             this.scriptAllfonts.put(getID(this.scriptIDs, paramString1), getID(this.componentFontNameIDs, paramString2));
/*      */           }
/* 2213 */         } else if (paramString1.startsWith("awtfontpath.")) {
/* 2214 */           paramString1 = paramString1.substring(12);
/*      */ 
/* 2216 */           this.awtfontpaths.put(getID(this.scriptIDs, paramString1), Short.valueOf(FontConfiguration.getStringID(paramString2)));
/* 2217 */         } else if ("version".equals(paramString1)) {
/* 2218 */           this.version = paramString2;
/* 2219 */         } else if ("appendedfontpath".equals(paramString1)) {
/* 2220 */           this.appendedfontpath = paramString2;
/* 2221 */         } else if (paramString1.startsWith("proportional.")) {
/* 2222 */           paramString1 = paramString1.substring(13).replace('_', ' ');
/*      */ 
/* 2224 */           this.proportionals.put(getID(this.componentFontNameIDs, paramString1), getID(this.componentFontNameIDs, paramString2));
/*      */         }
/*      */         else
/*      */         {
/* 2229 */           int k = 0;
/*      */ 
/* 2231 */           i = paramString1.indexOf('.');
/* 2232 */           if (i == -1) {
/* 2233 */             if (FontConfiguration.logger != null) {
/* 2234 */               FontConfiguration.logger.config("Failed parsing " + paramString1 + " property of font configuration.");
/*      */             }
/*      */ 
/* 2238 */             return;
/*      */           }
/* 2240 */           j = paramString1.indexOf('.', i + 1);
/* 2241 */           if (j == -1) {
/* 2242 */             if (FontConfiguration.logger != null) {
/* 2243 */               FontConfiguration.logger.config("Failed parsing " + paramString1 + " property of font configuration.");
/*      */             }
/*      */ 
/* 2247 */             return;
/*      */           }
/* 2249 */           if (paramString1.endsWith(".motif")) {
/* 2250 */             paramString1 = paramString1.substring(0, paramString1.length() - 6);
/* 2251 */             k = 1;
/*      */           }
/*      */ 
/* 2254 */           localObject = (Integer)this.logicalFontIDs.get(paramString1.substring(0, i));
/* 2255 */           Integer localInteger1 = (Integer)this.fontStyleIDs.get(paramString1.substring(i + 1, j));
/* 2256 */           localShort3 = getID(this.scriptIDs, paramString1.substring(j + 1));
/* 2257 */           if ((localObject == null) || (localInteger1 == null)) {
/* 2258 */             if (FontConfiguration.logger != null)
/* 2259 */               FontConfiguration.logger.config("unrecognizable logicfont name/style at " + paramString1);
/*      */             return;
/*      */           }
/*      */           Short[] arrayOfShort;
/* 2264 */           if (k != 0)
/* 2265 */             arrayOfShort = (Short[])this.scriptFontsMotif.get(localShort3);
/*      */           else {
/* 2267 */             arrayOfShort = (Short[])this.scriptFonts.get(localShort3);
/*      */           }
/* 2269 */           if (arrayOfShort == null) {
/* 2270 */             arrayOfShort = new Short[20];
/*      */           }
/* 2272 */           arrayOfShort[(localObject.intValue() * 4 + localInteger1.intValue())] = getID(this.componentFontNameIDs, paramString2);
/*      */ 
/* 2279 */           if (k != 0)
/* 2280 */             this.scriptFontsMotif.put(localShort3, arrayOfShort);
/*      */           else
/* 2282 */             this.scriptFonts.put(localShort3, arrayOfShort);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     class FontProperties extends Properties
/*      */     {
/*      */       FontProperties()
/*      */       {
/*      */       }
/*      */ 
/*      */       public synchronized Object put(Object paramObject1, Object paramObject2)
/*      */       {
/* 2114 */         FontConfiguration.PropertiesHandler.this.parseProperty((String)paramObject1, (String)paramObject2);
/* 2115 */         return null;
/*      */       }
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.FontConfiguration
 * JD-Core Version:    0.6.2
 */