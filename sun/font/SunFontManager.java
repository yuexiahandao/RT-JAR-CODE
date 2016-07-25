/*      */ package sun.font;
/*      */ 
/*      */ import java.awt.Font;
/*      */ import java.awt.FontFormatException;
/*      */ import java.io.BufferedReader;
/*      */ import java.io.File;
/*      */ import java.io.FileInputStream;
/*      */ import java.io.FilenameFilter;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.InputStreamReader;
/*      */ import java.io.PrintStream;
/*      */ import java.security.AccessController;
/*      */ import java.security.PrivilegedAction;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Hashtable;
/*      */ import java.util.Iterator;
/*      */ import java.util.Locale;
/*      */ import java.util.Map.Entry;
/*      */ import java.util.NoSuchElementException;
/*      */ import java.util.Set;
/*      */ import java.util.StringTokenizer;
/*      */ import java.util.TreeMap;
/*      */ import java.util.Vector;
/*      */ import java.util.concurrent.ConcurrentHashMap;
/*      */ import javax.swing.plaf.FontUIResource;
/*      */ import sun.applet.AppletSecurity;
/*      */ import sun.awt.AppContext;
/*      */ import sun.awt.FontConfiguration;
/*      */ import sun.awt.SunToolkit;
/*      */ import sun.java2d.FontSupport;
/*      */ import sun.misc.ThreadGroupUtils;
/*      */ import sun.util.logging.PlatformLogger;
/*      */ 
/*      */ public abstract class SunFontManager
/*      */   implements FontSupport, FontManagerForSGE
/*      */ {
/*      */   public static final int FONTFORMAT_NONE = -1;
/*      */   public static final int FONTFORMAT_TRUETYPE = 0;
/*      */   public static final int FONTFORMAT_TYPE1 = 1;
/*      */   public static final int FONTFORMAT_T2K = 2;
/*      */   public static final int FONTFORMAT_TTC = 3;
/*      */   public static final int FONTFORMAT_COMPOSITE = 4;
/*      */   public static final int FONTFORMAT_NATIVE = 5;
/*      */   protected static final int CHANNELPOOLSIZE = 20;
/*  148 */   protected FileFont[] fontFileCache = new FileFont[20];
/*      */ 
/*  150 */   private int lastPoolIndex = 0;
/*      */ 
/*  161 */   private int maxCompFont = 0;
/*  162 */   private CompositeFont[] compFonts = new CompositeFont[20];
/*  163 */   private ConcurrentHashMap<String, CompositeFont> compositeFonts = new ConcurrentHashMap();
/*      */ 
/*  165 */   private ConcurrentHashMap<String, PhysicalFont> physicalFonts = new ConcurrentHashMap();
/*      */ 
/*  167 */   private ConcurrentHashMap<String, PhysicalFont> registeredFonts = new ConcurrentHashMap();
/*      */ 
/*  175 */   protected ConcurrentHashMap<String, Font2D> fullNameToFont = new ConcurrentHashMap();
/*      */   private HashMap<String, TrueTypeFont> localeFullNamesToFont;
/*      */   private PhysicalFont defaultPhysicalFont;
/*      */   static boolean longAddresses;
/*  187 */   private boolean loaded1dot0Fonts = false;
/*  188 */   boolean loadedAllFonts = false;
/*  189 */   boolean loadedAllFontFiles = false;
/*      */   HashMap<String, String> jreFontMap;
/*      */   HashSet<String> jreLucidaFontFiles;
/*      */   String[] jreOtherFontFiles;
/*  193 */   boolean noOtherJREFontFiles = false;
/*      */   public static final String lucidaFontName = "Lucida Sans Regular";
/*      */   public static String jreLibDirName;
/*      */   public static String jreFontDirName;
/*  198 */   private static HashSet<String> missingFontFiles = null;
/*      */   private String defaultFontName;
/*      */   private String defaultFontFileName;
/*  201 */   protected HashSet registeredFontFiles = new HashSet();
/*      */   private ArrayList badFonts;
/*      */   protected String fontPath;
/*      */   private FontConfiguration fontConfig;
/*  218 */   private boolean discoveredAllFonts = false;
/*      */ 
/*  223 */   private static final FilenameFilter ttFilter = new TTFilter(null);
/*  224 */   private static final FilenameFilter t1Filter = new T1Filter(null);
/*      */   private Font[] allFonts;
/*      */   private String[] allFamilies;
/*      */   private Locale lastDefaultLocale;
/*      */   public static boolean noType1Font;
/*  233 */   private static String[] STR_ARRAY = new String[0];
/*      */ 
/*  239 */   private boolean usePlatformFontMetrics = false;
/*      */ 
/*  913 */   private final ConcurrentHashMap<String, FontRegistrationInfo> deferredFontFiles = new ConcurrentHashMap();
/*      */ 
/*  916 */   private final ConcurrentHashMap<String, Font2DHandle> initialisedFonts = new ConcurrentHashMap();
/*      */ 
/* 1281 */   private HashMap<String, String> fontToFileMap = null;
/*      */ 
/* 1287 */   private HashMap<String, String> fontToFamilyNameMap = null;
/*      */ 
/* 1294 */   private HashMap<String, ArrayList<String>> familyToFontListMap = null;
/*      */ 
/* 1297 */   private String[] pathDirs = null;
/*      */   private boolean haveCheckedUnreferencedFontFiles;
/*      */   static HashMap<String, FamilyDescription> platformFontMap;
/* 2063 */   private ConcurrentHashMap<String, Font2D> fontNameCache = new ConcurrentHashMap();
/*      */ 
/* 2446 */   protected Thread fileCloser = null;
/*      */ 
/* 2448 */   Vector<File> tmpFontFiles = null;
/*      */ 
/* 2828 */   private static final Object altJAFontKey = new Object();
/* 2829 */   private static final Object localeFontKey = new Object();
/* 2830 */   private static final Object proportionalFontKey = new Object();
/* 2831 */   private boolean _usingPerAppContextComposites = false;
/* 2832 */   private boolean _usingAlternateComposites = false;
/*      */ 
/* 2837 */   private static boolean gAltJAFont = false;
/* 2838 */   private boolean gLocalePref = false;
/* 2839 */   private boolean gPropPref = false;
/*      */ 
/* 2976 */   private static HashSet<String> installedNames = null;
/*      */ 
/* 2997 */   private static final Object regFamilyKey = new Object();
/* 2998 */   private static final Object regFullNameKey = new Object();
/*      */   private Hashtable<String, FontFamily> createdByFamilyName;
/*      */   private Hashtable<String, Font2D> createdByFullName;
/* 3001 */   private boolean fontsAreRegistered = false;
/* 3002 */   private boolean fontsAreRegisteredPerAppContext = false;
/*      */ 
/* 3838 */   private static Locale systemLocale = null;
/*      */ 
/*      */   public static SunFontManager getInstance()
/*      */   {
/*  250 */     FontManager localFontManager = FontManagerFactory.getInstance();
/*  251 */     return (SunFontManager)localFontManager;
/*      */   }
/*      */ 
/*      */   public FilenameFilter getTrueTypeFilter() {
/*  255 */     return ttFilter;
/*      */   }
/*      */ 
/*      */   public FilenameFilter getType1Filter() {
/*  259 */     return t1Filter;
/*      */   }
/*      */ 
/*      */   public boolean usingPerAppContextComposites()
/*      */   {
/*  264 */     return this._usingPerAppContextComposites;
/*      */   }
/*      */ 
/*      */   private void initJREFontMap()
/*      */   {
/*  280 */     this.jreFontMap = new HashMap();
/*  281 */     this.jreLucidaFontFiles = new HashSet();
/*  282 */     if (isOpenJDK()) {
/*  283 */       return;
/*      */     }
/*      */ 
/*  286 */     this.jreFontMap.put("lucida sans0", "LucidaSansRegular.ttf");
/*  287 */     this.jreFontMap.put("lucida sans1", "LucidaSansDemiBold.ttf");
/*      */ 
/*  289 */     this.jreFontMap.put("lucida sans regular0", "LucidaSansRegular.ttf");
/*  290 */     this.jreFontMap.put("lucida sans regular1", "LucidaSansDemiBold.ttf");
/*  291 */     this.jreFontMap.put("lucida sans bold1", "LucidaSansDemiBold.ttf");
/*  292 */     this.jreFontMap.put("lucida sans demibold1", "LucidaSansDemiBold.ttf");
/*      */ 
/*  295 */     this.jreFontMap.put("lucida sans typewriter0", "LucidaTypewriterRegular.ttf");
/*      */ 
/*  297 */     this.jreFontMap.put("lucida sans typewriter1", "LucidaTypewriterBold.ttf");
/*      */ 
/*  299 */     this.jreFontMap.put("lucida sans typewriter regular0", "LucidaTypewriter.ttf");
/*      */ 
/*  301 */     this.jreFontMap.put("lucida sans typewriter regular1", "LucidaTypewriterBold.ttf");
/*      */ 
/*  303 */     this.jreFontMap.put("lucida sans typewriter bold1", "LucidaTypewriterBold.ttf");
/*      */ 
/*  305 */     this.jreFontMap.put("lucida sans typewriter demibold1", "LucidaTypewriterBold.ttf");
/*      */ 
/*  309 */     this.jreFontMap.put("lucida bright0", "LucidaBrightRegular.ttf");
/*  310 */     this.jreFontMap.put("lucida bright1", "LucidaBrightDemiBold.ttf");
/*  311 */     this.jreFontMap.put("lucida bright2", "LucidaBrightItalic.ttf");
/*  312 */     this.jreFontMap.put("lucida bright3", "LucidaBrightDemiItalic.ttf");
/*      */ 
/*  314 */     this.jreFontMap.put("lucida bright regular0", "LucidaBrightRegular.ttf");
/*  315 */     this.jreFontMap.put("lucida bright regular1", "LucidaBrightDemiBold.ttf");
/*  316 */     this.jreFontMap.put("lucida bright regular2", "LucidaBrightItalic.ttf");
/*  317 */     this.jreFontMap.put("lucida bright regular3", "LucidaBrightDemiItalic.ttf");
/*  318 */     this.jreFontMap.put("lucida bright bold1", "LucidaBrightDemiBold.ttf");
/*  319 */     this.jreFontMap.put("lucida bright bold3", "LucidaBrightDemiItalic.ttf");
/*  320 */     this.jreFontMap.put("lucida bright demibold1", "LucidaBrightDemiBold.ttf");
/*  321 */     this.jreFontMap.put("lucida bright demibold3", "LucidaBrightDemiItalic.ttf");
/*  322 */     this.jreFontMap.put("lucida bright italic2", "LucidaBrightItalic.ttf");
/*  323 */     this.jreFontMap.put("lucida bright italic3", "LucidaBrightDemiItalic.ttf");
/*  324 */     this.jreFontMap.put("lucida bright bold italic3", "LucidaBrightDemiItalic.ttf");
/*      */ 
/*  326 */     this.jreFontMap.put("lucida bright demibold italic3", "LucidaBrightDemiItalic.ttf");
/*      */ 
/*  328 */     for (String str : this.jreFontMap.values())
/*  329 */       this.jreLucidaFontFiles.add(str);
/*      */   }
/*      */ 
/*      */   public TrueTypeFont getEUDCFont()
/*      */   {
/*  366 */     return null;
/*      */   }
/*      */ 
/*      */   private static native void initIDs();
/*      */ 
/*      */   protected SunFontManager()
/*      */   {
/*  375 */     initJREFontMap();
/*  376 */     AccessController.doPrivileged(new PrivilegedAction()
/*      */     {
/*      */       public Object run() {
/*  379 */         File localFile = new File(SunFontManager.jreFontDirName + File.separator + "badfonts.txt");
/*      */ 
/*  382 */         if (localFile.exists()) {
/*  383 */           localObject = null;
/*      */           try {
/*  385 */             SunFontManager.this.badFonts = new ArrayList();
/*  386 */             localObject = new FileInputStream(localFile);
/*  387 */             InputStreamReader localInputStreamReader = new InputStreamReader((InputStream)localObject);
/*  388 */             BufferedReader localBufferedReader = new BufferedReader(localInputStreamReader);
/*      */             while (true) {
/*  390 */               str = localBufferedReader.readLine();
/*  391 */               if (str == null) {
/*      */                 break;
/*      */               }
/*  394 */               if (FontUtilities.debugFonts()) {
/*  395 */                 FontUtilities.getLogger().warning("read bad font: " + str);
/*      */               }
/*      */ 
/*  398 */               SunFontManager.this.badFonts.add(str);
/*      */             }
/*      */           }
/*      */           catch (IOException localIOException1) {
/*      */             try {
/*  403 */               if (localObject != null) {
/*  404 */                 ((FileInputStream)localObject).close();
/*      */               }
/*      */ 
/*      */             }
/*      */             catch (IOException localIOException2)
/*      */             {
/*      */             }
/*      */ 
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*  421 */         if (FontUtilities.isLinux)
/*      */         {
/*  423 */           SunFontManager.this.registerFontDir(SunFontManager.jreFontDirName);
/*      */         }
/*  425 */         SunFontManager.this.registerFontsInDir(SunFontManager.jreFontDirName, true, 2, true, false);
/*      */ 
/*  431 */         SunFontManager.this.fontConfig = SunFontManager.this.createFontConfiguration();
/*  432 */         if (SunFontManager.isOpenJDK()) {
/*  433 */           localObject = SunFontManager.this.getDefaultPlatformFont();
/*  434 */           SunFontManager.this.defaultFontName = localObject[0];
/*  435 */           SunFontManager.this.defaultFontFileName = localObject[1];
/*      */         }
/*      */ 
/*  438 */         Object localObject = SunFontManager.this.fontConfig.getExtraFontPath();
/*      */ 
/*  466 */         int i = 0;
/*  467 */         int j = 0;
/*  468 */         String str = System.getProperty("sun.java2d.fontpath");
/*      */ 
/*  471 */         if (str != null) {
/*  472 */           if (str.startsWith("prepend:")) {
/*  473 */             i = 1;
/*  474 */             str = str.substring("prepend:".length());
/*      */           }
/*  476 */           else if (str.startsWith("append:")) {
/*  477 */             j = 1;
/*  478 */             str = str.substring("append:".length());
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*  483 */         if (FontUtilities.debugFonts()) {
/*  484 */           PlatformLogger localPlatformLogger = FontUtilities.getLogger();
/*  485 */           localPlatformLogger.info("JRE font directory: " + SunFontManager.jreFontDirName);
/*  486 */           localPlatformLogger.info("Extra font path: " + (String)localObject);
/*  487 */           localPlatformLogger.info("Debug font path: " + str);
/*      */         }
/*      */ 
/*  490 */         if (str != null)
/*      */         {
/*  494 */           SunFontManager.this.fontPath = SunFontManager.this.getPlatformFontPath(SunFontManager.noType1Font);
/*      */ 
/*  496 */           if (localObject != null) {
/*  497 */             SunFontManager.this.fontPath = ((String)localObject + File.pathSeparator + SunFontManager.this.fontPath);
/*      */           }
/*      */ 
/*  500 */           if (j != 0) {
/*  501 */             SunFontManager.this.fontPath = (SunFontManager.this.fontPath + File.pathSeparator + str);
/*      */           }
/*  503 */           else if (i != 0) {
/*  504 */             SunFontManager.this.fontPath = (str + File.pathSeparator + SunFontManager.this.fontPath);
/*      */           }
/*      */           else {
/*  507 */             SunFontManager.this.fontPath = str;
/*      */           }
/*  509 */           SunFontManager.this.registerFontDirs(SunFontManager.this.fontPath);
/*  510 */         } else if (localObject != null)
/*      */         {
/*  524 */           SunFontManager.this.registerFontDirs((String)localObject);
/*      */         }
/*      */ 
/*  541 */         if ((FontUtilities.isSolaris) && (Locale.JAPAN.equals(Locale.getDefault()))) {
/*  542 */           SunFontManager.this.registerFontDir("/usr/openwin/lib/locale/ja/X11/fonts/TT");
/*      */         }
/*      */ 
/*  545 */         SunFontManager.this.initCompositeFonts(SunFontManager.this.fontConfig, null);
/*      */ 
/*  547 */         return null;
/*      */       }
/*      */     });
/*  551 */     boolean bool = ((Boolean)AccessController.doPrivileged(new PrivilegedAction()
/*      */     {
/*      */       public Boolean run() {
/*  554 */         String str1 = System.getProperty("java2d.font.usePlatformFont");
/*      */ 
/*  556 */         String str2 = System.getenv("JAVA2D_USEPLATFORMFONT");
/*  557 */         return Boolean.valueOf(("true".equals(str1)) || (str2 != null));
/*      */       }
/*      */     })).booleanValue();
/*      */ 
/*  561 */     if (bool) {
/*  562 */       this.usePlatformFontMetrics = true;
/*  563 */       System.out.println("Enabling platform font metrics for win32. This is an unsupported option.");
/*  564 */       System.out.println("This yields incorrect composite font metrics as reported by 1.1.x releases.");
/*  565 */       System.out.println("It is appropriate only for use by applications which do not use any Java 2");
/*  566 */       System.out.println("functionality. This property will be removed in a later release.");
/*      */     }
/*      */   }
/*      */ 
/*      */   public Font2DHandle getNewComposite(String paramString, int paramInt, Font2DHandle paramFont2DHandle)
/*      */   {
/*  603 */     if (!(paramFont2DHandle.font2D instanceof CompositeFont)) {
/*  604 */       return paramFont2DHandle;
/*      */     }
/*      */ 
/*  607 */     CompositeFont localCompositeFont1 = (CompositeFont)paramFont2DHandle.font2D;
/*  608 */     PhysicalFont localPhysicalFont1 = localCompositeFont1.getSlotFont(0);
/*      */ 
/*  610 */     if (paramString == null) {
/*  611 */       paramString = localPhysicalFont1.getFamilyName(null);
/*      */     }
/*  613 */     if (paramInt == -1) {
/*  614 */       paramInt = localCompositeFont1.getStyle();
/*      */     }
/*      */ 
/*  617 */     Object localObject = findFont2D(paramString, paramInt, 0);
/*  618 */     if (!(localObject instanceof PhysicalFont)) {
/*  619 */       localObject = localPhysicalFont1;
/*      */     }
/*  621 */     PhysicalFont localPhysicalFont2 = (PhysicalFont)localObject;
/*  622 */     CompositeFont localCompositeFont2 = (CompositeFont)findFont2D("dialog", paramInt, 0);
/*      */ 
/*  624 */     if (localCompositeFont2 == null) {
/*  625 */       return paramFont2DHandle;
/*      */     }
/*  627 */     CompositeFont localCompositeFont3 = new CompositeFont(localPhysicalFont2, localCompositeFont2);
/*  628 */     Font2DHandle localFont2DHandle = new Font2DHandle(localCompositeFont3);
/*  629 */     return localFont2DHandle;
/*      */   }
/*      */ 
/*      */   protected void registerCompositeFont(String paramString, String[] paramArrayOfString1, String[] paramArrayOfString2, int paramInt, int[] paramArrayOfInt1, int[] paramArrayOfInt2, boolean paramBoolean)
/*      */   {
/*  640 */     CompositeFont localCompositeFont = new CompositeFont(paramString, paramArrayOfString1, paramArrayOfString2, paramInt, paramArrayOfInt1, paramArrayOfInt2, paramBoolean, this);
/*      */ 
/*  646 */     addCompositeToFontList(localCompositeFont, 2);
/*  647 */     synchronized (this.compFonts) {
/*  648 */       this.compFonts[(this.maxCompFont++)] = localCompositeFont;
/*      */     }
/*      */   }
/*      */ 
/*      */   protected static void registerCompositeFont(String paramString, String[] paramArrayOfString1, String[] paramArrayOfString2, int paramInt, int[] paramArrayOfInt1, int[] paramArrayOfInt2, boolean paramBoolean, ConcurrentHashMap<String, Font2D> paramConcurrentHashMap)
/*      */   {
/*  666 */     CompositeFont localCompositeFont = new CompositeFont(paramString, paramArrayOfString1, paramArrayOfString2, paramInt, paramArrayOfInt1, paramArrayOfInt2, paramBoolean, getInstance());
/*      */ 
/*  687 */     Font2D localFont2D = (Font2D)paramConcurrentHashMap.get(paramString.toLowerCase(Locale.ENGLISH));
/*      */ 
/*  689 */     if ((localFont2D instanceof CompositeFont)) {
/*  690 */       localFont2D.handle.font2D = localCompositeFont;
/*      */     }
/*  692 */     paramConcurrentHashMap.put(paramString.toLowerCase(Locale.ENGLISH), localCompositeFont);
/*      */   }
/*      */ 
/*      */   private void addCompositeToFontList(CompositeFont paramCompositeFont, int paramInt)
/*      */   {
/*  697 */     if (FontUtilities.isLogging()) {
/*  698 */       FontUtilities.getLogger().info("Add to Family " + paramCompositeFont.familyName + ", Font " + paramCompositeFont.fullName + " rank=" + paramInt);
/*      */     }
/*      */ 
/*  701 */     paramCompositeFont.setRank(paramInt);
/*  702 */     this.compositeFonts.put(paramCompositeFont.fullName, paramCompositeFont);
/*  703 */     this.fullNameToFont.put(paramCompositeFont.fullName.toLowerCase(Locale.ENGLISH), paramCompositeFont);
/*      */ 
/*  705 */     FontFamily localFontFamily = FontFamily.getFamily(paramCompositeFont.familyName);
/*  706 */     if (localFontFamily == null) {
/*  707 */       localFontFamily = new FontFamily(paramCompositeFont.familyName, true, paramInt);
/*      */     }
/*  709 */     localFontFamily.setFont(paramCompositeFont, paramCompositeFont.style);
/*      */   }
/*      */ 
/*      */   protected PhysicalFont addToFontList(PhysicalFont paramPhysicalFont, int paramInt)
/*      */   {
/*  747 */     String str1 = paramPhysicalFont.fullName;
/*  748 */     String str2 = paramPhysicalFont.familyName;
/*  749 */     if ((str1 == null) || ("".equals(str1))) {
/*  750 */       return null;
/*      */     }
/*  752 */     if (this.compositeFonts.containsKey(str1))
/*      */     {
/*  754 */       return null;
/*      */     }
/*  756 */     paramPhysicalFont.setRank(paramInt);
/*  757 */     if (!this.physicalFonts.containsKey(str1)) {
/*  758 */       if (FontUtilities.isLogging()) {
/*  759 */         FontUtilities.getLogger().info("Add to Family " + str2 + ", Font " + str1 + " rank=" + paramInt);
/*      */       }
/*      */ 
/*  762 */       this.physicalFonts.put(str1, paramPhysicalFont);
/*  763 */       localObject1 = FontFamily.getFamily(str2);
/*  764 */       if (localObject1 == null) {
/*  765 */         localObject1 = new FontFamily(str2, false, paramInt);
/*  766 */         ((FontFamily)localObject1).setFont(paramPhysicalFont, paramPhysicalFont.style);
/*  767 */       } else if (((FontFamily)localObject1).getRank() >= paramInt) {
/*  768 */         ((FontFamily)localObject1).setFont(paramPhysicalFont, paramPhysicalFont.style);
/*      */       }
/*  770 */       this.fullNameToFont.put(str1.toLowerCase(Locale.ENGLISH), paramPhysicalFont);
/*  771 */       return paramPhysicalFont;
/*      */     }
/*  773 */     Object localObject1 = paramPhysicalFont;
/*  774 */     PhysicalFont localPhysicalFont = (PhysicalFont)this.physicalFonts.get(str1);
/*  775 */     if (localPhysicalFont == null) {
/*  776 */       return null;
/*      */     }
/*      */ 
/*  781 */     if (localPhysicalFont.getRank() >= paramInt)
/*      */     {
/*  801 */       if ((localPhysicalFont.mapper != null) && (paramInt > 2)) {
/*  802 */         return localPhysicalFont;
/*      */       }
/*      */ 
/*  810 */       if (localPhysicalFont.getRank() == paramInt) {
/*  811 */         if (((localPhysicalFont instanceof TrueTypeFont)) && ((localObject1 instanceof TrueTypeFont)))
/*      */         {
/*  813 */           localObject2 = (TrueTypeFont)localPhysicalFont;
/*  814 */           TrueTypeFont localTrueTypeFont = (TrueTypeFont)localObject1;
/*  815 */           if (((TrueTypeFont)localObject2).fileSize >= localTrueTypeFont.fileSize)
/*  816 */             return localPhysicalFont;
/*      */         }
/*      */         else {
/*  819 */           return localPhysicalFont;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  831 */       if (localPhysicalFont.platName.startsWith(jreFontDirName)) {
/*  832 */         if (FontUtilities.isLogging()) {
/*  833 */           FontUtilities.getLogger().warning("Unexpected attempt to replace a JRE  font " + str1 + " from " + localPhysicalFont.platName + " with " + ((PhysicalFont)localObject1).platName);
/*      */         }
/*      */ 
/*  839 */         return localPhysicalFont;
/*      */       }
/*      */ 
/*  842 */       if (FontUtilities.isLogging()) {
/*  843 */         FontUtilities.getLogger().info("Replace in Family " + str2 + ",Font " + str1 + " new rank=" + paramInt + " from " + localPhysicalFont.platName + " with " + ((PhysicalFont)localObject1).platName);
/*      */       }
/*      */ 
/*  849 */       replaceFont(localPhysicalFont, (PhysicalFont)localObject1);
/*  850 */       this.physicalFonts.put(str1, localObject1);
/*  851 */       this.fullNameToFont.put(str1.toLowerCase(Locale.ENGLISH), localObject1);
/*      */ 
/*  854 */       Object localObject2 = FontFamily.getFamily(str2);
/*  855 */       if (localObject2 == null) {
/*  856 */         localObject2 = new FontFamily(str2, false, paramInt);
/*  857 */         ((FontFamily)localObject2).setFont((Font2D)localObject1, ((PhysicalFont)localObject1).style);
/*  858 */       } else if (((FontFamily)localObject2).getRank() >= paramInt) {
/*  859 */         ((FontFamily)localObject2).setFont((Font2D)localObject1, ((PhysicalFont)localObject1).style);
/*      */       }
/*  861 */       return localObject1;
/*      */     }
/*  863 */     return localPhysicalFont;
/*      */   }
/*      */ 
/*      */   public Font2D[] getRegisteredFonts()
/*      */   {
/*  869 */     PhysicalFont[] arrayOfPhysicalFont = getPhysicalFonts();
/*  870 */     int i = this.maxCompFont;
/*  871 */     Font2D[] arrayOfFont2D = new Font2D[arrayOfPhysicalFont.length + i];
/*  872 */     System.arraycopy(this.compFonts, 0, arrayOfFont2D, 0, i);
/*  873 */     System.arraycopy(arrayOfPhysicalFont, 0, arrayOfFont2D, i, arrayOfPhysicalFont.length);
/*  874 */     return arrayOfFont2D;
/*      */   }
/*      */ 
/*      */   protected PhysicalFont[] getPhysicalFonts() {
/*  878 */     return (PhysicalFont[])this.physicalFonts.values().toArray(new PhysicalFont[0]);
/*      */   }
/*      */ 
/*      */   protected synchronized void initialiseDeferredFonts()
/*      */   {
/*  926 */     for (String str : this.deferredFontFiles.keySet())
/*  927 */       initialiseDeferredFont(str);
/*      */   }
/*      */ 
/*      */   protected synchronized void registerDeferredJREFonts(String paramString)
/*      */   {
/*  932 */     for (FontRegistrationInfo localFontRegistrationInfo : this.deferredFontFiles.values())
/*  933 */       if ((localFontRegistrationInfo.fontFilePath != null) && (localFontRegistrationInfo.fontFilePath.startsWith(paramString)))
/*      */       {
/*  935 */         initialiseDeferredFont(localFontRegistrationInfo.fontFilePath);
/*      */       }
/*      */   }
/*      */ 
/*      */   public boolean isDeferredFont(String paramString)
/*      */   {
/*  941 */     return this.deferredFontFiles.containsKey(paramString);
/*      */   }
/*      */ 
/*      */   public PhysicalFont findJREDeferredFont(String paramString, int paramInt)
/*      */   {
/*  957 */     String str1 = paramString.toLowerCase(Locale.ENGLISH) + paramInt;
/*  958 */     String str2 = (String)this.jreFontMap.get(str1);
/*      */     PhysicalFont localPhysicalFont;
/*  959 */     if (str2 != null) {
/*  960 */       str2 = jreFontDirName + File.separator + str2;
/*  961 */       if (this.deferredFontFiles.get(str2) != null) {
/*  962 */         localPhysicalFont = initialiseDeferredFont(str2);
/*  963 */         if ((localPhysicalFont != null) && ((localPhysicalFont.getFontName(null).equalsIgnoreCase(paramString)) || (localPhysicalFont.getFamilyName(null).equalsIgnoreCase(paramString))) && (localPhysicalFont.style == paramInt))
/*      */         {
/*  967 */           return localPhysicalFont;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  981 */     if (this.noOtherJREFontFiles) {
/*  982 */       return null;
/*      */     }
/*  984 */     synchronized (this.jreLucidaFontFiles) {
/*  985 */       if (this.jreOtherFontFiles == null) {
/*  986 */         HashSet localHashSet = new HashSet();
/*  987 */         for (String str3 : this.deferredFontFiles.keySet()) {
/*  988 */           File localFile = new File(str3);
/*  989 */           String str4 = localFile.getParent();
/*  990 */           String str5 = localFile.getName();
/*      */ 
/*  994 */           if ((str4 != null) && (str4.equals(jreFontDirName)) && (!this.jreLucidaFontFiles.contains(str5)))
/*      */           {
/*  999 */             localHashSet.add(str3);
/*      */           }
/*      */         }
/* 1001 */         this.jreOtherFontFiles = ((String[])localHashSet.toArray(STR_ARRAY));
/* 1002 */         if (this.jreOtherFontFiles.length == 0) {
/* 1003 */           this.noOtherJREFontFiles = true;
/*      */         }
/*      */       }
/*      */ 
/* 1007 */       for (int i = 0; i < this.jreOtherFontFiles.length; i++) {
/* 1008 */         str2 = this.jreOtherFontFiles[i];
/* 1009 */         if (str2 != null)
/*      */         {
/* 1012 */           this.jreOtherFontFiles[i] = null;
/* 1013 */           localPhysicalFont = initialiseDeferredFont(str2);
/* 1014 */           if ((localPhysicalFont != null) && ((localPhysicalFont.getFontName(null).equalsIgnoreCase(paramString)) || (localPhysicalFont.getFamilyName(null).equalsIgnoreCase(paramString))) && (localPhysicalFont.style == paramInt))
/*      */           {
/* 1018 */             return localPhysicalFont;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/* 1023 */     return null;
/*      */   }
/*      */ 
/*      */   private PhysicalFont findOtherDeferredFont(String paramString, int paramInt)
/*      */   {
/* 1028 */     for (String str1 : this.deferredFontFiles.keySet()) {
/* 1029 */       File localFile = new File(str1);
/* 1030 */       String str2 = localFile.getParent();
/* 1031 */       String str3 = localFile.getName();
/* 1032 */       if ((str2 == null) || (!str2.equals(jreFontDirName)) || (!this.jreLucidaFontFiles.contains(str3)))
/*      */       {
/* 1037 */         PhysicalFont localPhysicalFont = initialiseDeferredFont(str1);
/* 1038 */         if ((localPhysicalFont != null) && ((localPhysicalFont.getFontName(null).equalsIgnoreCase(paramString)) || (localPhysicalFont.getFamilyName(null).equalsIgnoreCase(paramString))) && (localPhysicalFont.style == paramInt))
/*      */         {
/* 1042 */           return localPhysicalFont;
/*      */         }
/*      */       }
/*      */     }
/* 1045 */     return null;
/*      */   }
/*      */ 
/*      */   private PhysicalFont findDeferredFont(String paramString, int paramInt)
/*      */   {
/* 1050 */     PhysicalFont localPhysicalFont = findJREDeferredFont(paramString, paramInt);
/* 1051 */     if (localPhysicalFont != null) {
/* 1052 */       return localPhysicalFont;
/*      */     }
/* 1054 */     return findOtherDeferredFont(paramString, paramInt);
/*      */   }
/*      */ 
/*      */   public void registerDeferredFont(String paramString1, String paramString2, String[] paramArrayOfString, int paramInt1, boolean paramBoolean, int paramInt2)
/*      */   {
/* 1064 */     FontRegistrationInfo localFontRegistrationInfo = new FontRegistrationInfo(paramString2, paramArrayOfString, paramInt1, paramBoolean, paramInt2);
/*      */ 
/* 1067 */     this.deferredFontFiles.put(paramString1, localFontRegistrationInfo);
/*      */   }
/*      */ 
/*      */   public synchronized PhysicalFont initialiseDeferredFont(String paramString)
/*      */   {
/* 1074 */     if (paramString == null) {
/* 1075 */       return null;
/*      */     }
/* 1077 */     if (FontUtilities.isLogging()) {
/* 1078 */       FontUtilities.getLogger().info("Opening deferred font file " + paramString);
/*      */     }
/*      */ 
/* 1083 */     FontRegistrationInfo localFontRegistrationInfo = (FontRegistrationInfo)this.deferredFontFiles.get(paramString);
/*      */     PhysicalFont localPhysicalFont;
/* 1084 */     if (localFontRegistrationInfo != null) {
/* 1085 */       this.deferredFontFiles.remove(paramString);
/* 1086 */       localPhysicalFont = registerFontFile(localFontRegistrationInfo.fontFilePath, localFontRegistrationInfo.nativeNames, localFontRegistrationInfo.fontFormat, localFontRegistrationInfo.javaRasterizer, localFontRegistrationInfo.fontRank);
/*      */ 
/* 1093 */       if (localPhysicalFont != null)
/*      */       {
/* 1097 */         this.initialisedFonts.put(paramString, localPhysicalFont.handle);
/*      */       }
/* 1099 */       else this.initialisedFonts.put(paramString, getDefaultPhysicalFont().handle);
/*      */     }
/*      */     else
/*      */     {
/* 1103 */       Font2DHandle localFont2DHandle = (Font2DHandle)this.initialisedFonts.get(paramString);
/* 1104 */       if (localFont2DHandle == null)
/*      */       {
/* 1106 */         localPhysicalFont = getDefaultPhysicalFont();
/*      */       }
/* 1108 */       else localPhysicalFont = (PhysicalFont)localFont2DHandle.font2D;
/*      */     }
/*      */ 
/* 1111 */     return localPhysicalFont;
/*      */   }
/*      */ 
/*      */   public boolean isRegisteredFontFile(String paramString) {
/* 1115 */     return this.registeredFonts.containsKey(paramString);
/*      */   }
/*      */ 
/*      */   public PhysicalFont getRegisteredFontFile(String paramString) {
/* 1119 */     return (PhysicalFont)this.registeredFonts.get(paramString);
/*      */   }
/*      */ 
/*      */   public PhysicalFont registerFontFile(String paramString, String[] paramArrayOfString, int paramInt1, boolean paramBoolean, int paramInt2)
/*      */   {
/* 1132 */     PhysicalFont localPhysicalFont = (PhysicalFont)this.registeredFonts.get(paramString);
/* 1133 */     if (localPhysicalFont != null) {
/* 1134 */       return localPhysicalFont;
/*      */     }
/*      */ 
/* 1137 */     Object localObject1 = null;
/*      */     try
/*      */     {
/*      */       Object localObject2;
/* 1141 */       switch (paramInt1) {
/*      */       case 0:
/* 1144 */         int i = 0;
/*      */         TrueTypeFont localTrueTypeFont;
/*      */         do {
/* 1147 */           localTrueTypeFont = new TrueTypeFont(paramString, paramArrayOfString, i++, paramBoolean);
/*      */ 
/* 1149 */           localObject2 = addToFontList(localTrueTypeFont, paramInt2);
/* 1150 */           if (localObject1 == null) {
/* 1151 */             localObject1 = localObject2;
/*      */           }
/*      */         }
/* 1154 */         while (i < localTrueTypeFont.getFontCount());
/* 1155 */         break;
/*      */       case 1:
/* 1158 */         localObject2 = new Type1Font(paramString, paramArrayOfString);
/* 1159 */         localObject1 = addToFontList((PhysicalFont)localObject2, paramInt2);
/* 1160 */         break;
/*      */       case 5:
/* 1163 */         NativeFont localNativeFont = new NativeFont(paramString, false);
/* 1164 */         localObject1 = addToFontList(localNativeFont, paramInt2);
/*      */       }
/*      */ 
/* 1168 */       if (FontUtilities.isLogging()) {
/* 1169 */         FontUtilities.getLogger().info("Registered file " + paramString + " as font " + localObject1 + " rank=" + paramInt2);
/*      */       }
/*      */     }
/*      */     catch (FontFormatException localFontFormatException)
/*      */     {
/* 1174 */       if (FontUtilities.isLogging()) {
/* 1175 */         FontUtilities.getLogger().warning("Unusable font: " + paramString + " " + localFontFormatException.toString());
/*      */       }
/*      */     }
/*      */ 
/* 1179 */     if ((localObject1 != null) && (paramInt1 != 5))
/*      */     {
/* 1181 */       this.registeredFonts.put(paramString, localObject1);
/*      */     }
/* 1183 */     return localObject1;
/*      */   }
/*      */ 
/*      */   public void registerFonts(String[] paramArrayOfString, String[][] paramArrayOfString1, int paramInt1, int paramInt2, boolean paramBoolean1, int paramInt3, boolean paramBoolean2)
/*      */   {
/* 1193 */     for (int i = 0; i < paramInt1; i++)
/* 1194 */       if (paramBoolean2) {
/* 1195 */         registerDeferredFont(paramArrayOfString[i], paramArrayOfString[i], paramArrayOfString1[i], paramInt2, paramBoolean1, paramInt3);
/*      */       }
/*      */       else
/* 1198 */         registerFontFile(paramArrayOfString[i], paramArrayOfString1[i], paramInt2, paramBoolean1, paramInt3);
/*      */   }
/*      */ 
/*      */   public PhysicalFont getDefaultPhysicalFont()
/*      */   {
/* 1211 */     if (this.defaultPhysicalFont == null)
/*      */     {
/* 1219 */       this.defaultPhysicalFont = ((PhysicalFont)findFont2D("Lucida Sans Regular", 0, 0));
/*      */ 
/* 1221 */       if (this.defaultPhysicalFont == null) {
/* 1222 */         this.defaultPhysicalFont = ((PhysicalFont)findFont2D("Arial", 0, 0));
/*      */       }
/*      */ 
/* 1225 */       if (this.defaultPhysicalFont == null)
/*      */       {
/* 1232 */         Iterator localIterator = this.physicalFonts.values().iterator();
/* 1233 */         if (localIterator.hasNext())
/* 1234 */           this.defaultPhysicalFont = ((PhysicalFont)localIterator.next());
/*      */         else {
/* 1236 */           throw new Error("Probable fatal error:No fonts found.");
/*      */         }
/*      */       }
/*      */     }
/* 1240 */     return this.defaultPhysicalFont;
/*      */   }
/*      */ 
/*      */   public Font2D getDefaultLogicalFont(int paramInt) {
/* 1244 */     return findFont2D("dialog", paramInt, 0);
/*      */   }
/*      */ 
/*      */   private static String dotStyleStr(int paramInt)
/*      */   {
/* 1252 */     switch (paramInt) {
/*      */     case 1:
/* 1254 */       return ".bold";
/*      */     case 2:
/* 1256 */       return ".italic";
/*      */     case 3:
/* 1258 */       return ".bolditalic";
/*      */     }
/* 1260 */     return ".plain";
/*      */   }
/*      */ 
/*      */   protected void populateFontFileNameMap(HashMap<String, String> paramHashMap1, HashMap<String, String> paramHashMap2, HashMap<String, ArrayList<String>> paramHashMap, Locale paramLocale)
/*      */   {
/*      */   }
/*      */ 
/*      */   private String[] getFontFilesFromPath(boolean paramBoolean)
/*      */   {
/*      */     Object localObject;
/* 1303 */     if (paramBoolean)
/* 1304 */       localObject = ttFilter;
/*      */     else {
/* 1306 */       localObject = new TTorT1Filter(null);
/*      */     }
/* 1308 */     return (String[])AccessController.doPrivileged(new PrivilegedAction() {
/*      */       public Object run() {
/* 1310 */         if (SunFontManager.this.pathDirs.length == 1) {
/* 1311 */           localObject = new File(SunFontManager.this.pathDirs[0]);
/* 1312 */           String[] arrayOfString1 = ((File)localObject).list(this.val$filter);
/* 1313 */           if (arrayOfString1 == null) {
/* 1314 */             return new String[0];
/*      */           }
/* 1316 */           for (int j = 0; j < arrayOfString1.length; j++) {
/* 1317 */             arrayOfString1[j] = arrayOfString1[j].toLowerCase();
/*      */           }
/* 1319 */           return arrayOfString1;
/*      */         }
/* 1321 */         Object localObject = new ArrayList();
/* 1322 */         for (int i = 0; i < SunFontManager.this.pathDirs.length; i++) {
/* 1323 */           File localFile = new File(SunFontManager.this.pathDirs[i]);
/* 1324 */           String[] arrayOfString2 = localFile.list(this.val$filter);
/* 1325 */           if (arrayOfString2 != null)
/*      */           {
/* 1328 */             for (int k = 0; k < arrayOfString2.length; k++)
/* 1329 */               ((ArrayList)localObject).add(arrayOfString2[k].toLowerCase());
/*      */           }
/*      */         }
/* 1332 */         return ((ArrayList)localObject).toArray(SunFontManager.STR_ARRAY);
/*      */       }
/*      */     });
/*      */   }
/*      */ 
/*      */   private void resolveWindowsFonts()
/*      */   {
/* 1360 */     ArrayList localArrayList = null;
/* 1361 */     for (Object localObject1 = this.fontToFamilyNameMap.keySet().iterator(); ((Iterator)localObject1).hasNext(); ) { localObject2 = (String)((Iterator)localObject1).next();
/* 1362 */       localObject3 = (String)this.fontToFileMap.get(localObject2);
/* 1363 */       if (localObject3 == null)
/* 1364 */         if (((String)localObject2).indexOf("  ") > 0) {
/* 1365 */           localObject4 = ((String)localObject2).replaceFirst("  ", " ");
/* 1366 */           localObject3 = (String)this.fontToFileMap.get(localObject4);
/*      */ 
/* 1370 */           if ((localObject3 != null) && (!this.fontToFamilyNameMap.containsKey(localObject4)))
/*      */           {
/* 1372 */             this.fontToFileMap.remove(localObject4);
/* 1373 */             this.fontToFileMap.put(localObject2, localObject3);
/*      */           }
/* 1375 */         } else if (((String)localObject2).equals("marlett")) {
/* 1376 */           this.fontToFileMap.put(localObject2, "marlett.ttf");
/* 1377 */         } else if (((String)localObject2).equals("david")) {
/* 1378 */           localObject3 = (String)this.fontToFileMap.get("david regular");
/* 1379 */           if (localObject3 != null) {
/* 1380 */             this.fontToFileMap.remove("david regular");
/* 1381 */             this.fontToFileMap.put("david", localObject3);
/*      */           }
/*      */         } else {
/* 1384 */           if (localArrayList == null) {
/* 1385 */             localArrayList = new ArrayList();
/*      */           }
/* 1387 */           localArrayList.add(localObject2);
/*      */         }
/*      */     }
/*      */     Object localObject2;
/*      */     Object localObject3;
/*      */     Object localObject4;
/* 1392 */     if (localArrayList != null) {
/* 1393 */       localObject1 = new HashSet();
/*      */ 
/* 1424 */       localObject2 = (HashMap)this.fontToFileMap.clone();
/*      */ 
/* 1426 */       for (localObject3 = this.fontToFamilyNameMap.keySet().iterator(); ((Iterator)localObject3).hasNext(); ) { localObject4 = (String)((Iterator)localObject3).next();
/* 1427 */         ((HashMap)localObject2).remove(localObject4);
/*      */       }
/* 1429 */       for (localObject3 = ((HashMap)localObject2).keySet().iterator(); ((Iterator)localObject3).hasNext(); ) { localObject4 = (String)((Iterator)localObject3).next();
/* 1430 */         ((HashSet)localObject1).add(((HashMap)localObject2).get(localObject4));
/* 1431 */         this.fontToFileMap.remove(localObject4);
/*      */       }
/*      */ 
/* 1434 */       resolveFontFiles((HashSet)localObject1, localArrayList);
/*      */       Object localObject5;
/* 1441 */       if (localArrayList.size() > 0)
/*      */       {
/* 1447 */         localObject3 = new ArrayList();
/*      */ 
/* 1449 */         for (localObject4 = this.fontToFileMap.values().iterator(); ((Iterator)localObject4).hasNext(); ) { String str1 = (String)((Iterator)localObject4).next();
/* 1450 */           ((ArrayList)localObject3).add(str1.toLowerCase());
/*      */         }
/*      */ 
/* 1457 */         for (localObject5 : getFontFilesFromPath(true)) {
/* 1458 */           if (!((ArrayList)localObject3).contains(localObject5)) {
/* 1459 */             ((HashSet)localObject1).add(localObject5);
/*      */           }
/*      */         }
/*      */ 
/* 1463 */         resolveFontFiles((HashSet)localObject1, localArrayList);
/*      */       }
/*      */ 
/* 1469 */       if (localArrayList.size() > 0) {
/* 1470 */         int i = localArrayList.size();
/* 1471 */         for (int j = 0; j < i; j++) {
/* 1472 */           String str2 = (String)localArrayList.get(j);
/* 1473 */           String str3 = (String)this.fontToFamilyNameMap.get(str2);
/* 1474 */           if (str3 != null) {
/* 1475 */             localObject5 = (ArrayList)this.familyToFontListMap.get(str3);
/* 1476 */             if ((localObject5 != null) && 
/* 1477 */               (((ArrayList)localObject5).size() <= 1)) {
/* 1478 */               this.familyToFontListMap.remove(str3);
/*      */             }
/*      */           }
/*      */ 
/* 1482 */           this.fontToFamilyNameMap.remove(str2);
/* 1483 */           if (FontUtilities.isLogging())
/* 1484 */             FontUtilities.getLogger().info("No file for font:" + str2);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private synchronized void checkForUnreferencedFontFiles()
/*      */   {
/* 1505 */     if (this.haveCheckedUnreferencedFontFiles) {
/* 1506 */       return;
/*      */     }
/* 1508 */     this.haveCheckedUnreferencedFontFiles = true;
/* 1509 */     if (!FontUtilities.isWindows) {
/* 1510 */       return;
/*      */     }
/*      */ 
/* 1516 */     ArrayList localArrayList1 = new ArrayList();
/* 1517 */     for (Object localObject1 = this.fontToFileMap.values().iterator(); ((Iterator)localObject1).hasNext(); ) { localObject2 = (String)((Iterator)localObject1).next();
/* 1518 */       localArrayList1.add(((String)localObject2).toLowerCase());
/*      */     }
/*      */ 
/* 1528 */     localObject1 = null;
/* 1529 */     Object localObject2 = null;
/* 1530 */     HashMap localHashMap = null;
/*      */ 
/* 1532 */     for (String str1 : getFontFilesFromPath(false))
/* 1533 */       if (!localArrayList1.contains(str1)) {
/* 1534 */         if (FontUtilities.isLogging()) {
/* 1535 */           FontUtilities.getLogger().info("Found non-registry file : " + str1);
/*      */         }
/*      */ 
/* 1538 */         PhysicalFont localPhysicalFont = registerFontFile(getPathName(str1));
/* 1539 */         if (localPhysicalFont != null)
/*      */         {
/* 1542 */           if (localObject1 == null) {
/* 1543 */             localObject1 = new HashMap(this.fontToFileMap);
/* 1544 */             localObject2 = new HashMap(this.fontToFamilyNameMap);
/*      */ 
/* 1546 */             localHashMap = new HashMap(this.familyToFontListMap);
/*      */           }
/*      */ 
/* 1549 */           String str2 = localPhysicalFont.getFontName(null);
/* 1550 */           String str3 = localPhysicalFont.getFamilyName(null);
/* 1551 */           String str4 = str3.toLowerCase();
/* 1552 */           ((HashMap)localObject2).put(str2, str3);
/* 1553 */           ((HashMap)localObject1).put(str2, str1);
/* 1554 */           ArrayList localArrayList2 = (ArrayList)localHashMap.get(str4);
/* 1555 */           if (localArrayList2 == null)
/* 1556 */             localArrayList2 = new ArrayList();
/*      */           else {
/* 1558 */             localArrayList2 = new ArrayList(localArrayList2);
/*      */           }
/* 1560 */           localArrayList2.add(str2);
/* 1561 */           localHashMap.put(str4, localArrayList2);
/*      */         }
/*      */       }
/* 1564 */     if (localObject1 != null) {
/* 1565 */       this.fontToFileMap = ((HashMap)localObject1);
/* 1566 */       this.familyToFontListMap = localHashMap;
/* 1567 */       this.fontToFamilyNameMap = ((HashMap)localObject2);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void resolveFontFiles(HashSet<String> paramHashSet, ArrayList<String> paramArrayList)
/*      */   {
/* 1574 */     Locale localLocale = SunToolkit.getStartupLocale();
/*      */ 
/* 1576 */     for (String str1 : paramHashSet)
/*      */       try {
/* 1578 */         int i = 0;
/*      */ 
/* 1580 */         String str2 = getPathName(str1);
/* 1581 */         if (FontUtilities.isLogging())
/* 1582 */           FontUtilities.getLogger().info("Trying to resolve file " + str2);
/*      */         TrueTypeFont localTrueTypeFont;
/*      */         do
/*      */         {
/* 1586 */           localTrueTypeFont = new TrueTypeFont(str2, null, i++, false);
/*      */ 
/* 1588 */           String str3 = localTrueTypeFont.getFontName(localLocale).toLowerCase();
/* 1589 */           if (paramArrayList.contains(str3)) {
/* 1590 */             this.fontToFileMap.put(str3, str1);
/* 1591 */             paramArrayList.remove(str3);
/* 1592 */             if (FontUtilities.isLogging()) {
/* 1593 */               FontUtilities.getLogger().info("Resolved absent registry entry for " + str3 + " located in " + str2);
/*      */             }
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/* 1599 */         while (i < localTrueTypeFont.getFontCount());
/*      */       }
/*      */       catch (Exception localException)
/*      */       {
/*      */       }
/*      */   }
/*      */ 
/*      */   public HashMap<String, FamilyDescription> populateHardcodedFileNameMap()
/*      */   {
/* 1636 */     return new HashMap(0);
/*      */   }
/*      */ 
/*      */   Font2D findFontFromPlatformMap(String paramString, int paramInt) {
/* 1640 */     if (platformFontMap == null) {
/* 1641 */       platformFontMap = populateHardcodedFileNameMap();
/*      */     }
/*      */ 
/* 1644 */     if ((platformFontMap == null) || (platformFontMap.size() == 0)) {
/* 1645 */       return null;
/*      */     }
/*      */ 
/* 1648 */     int i = paramString.indexOf(' ');
/* 1649 */     String str1 = paramString;
/* 1650 */     if (i > 0) {
/* 1651 */       str1 = paramString.substring(0, i);
/*      */     }
/*      */ 
/* 1654 */     FamilyDescription localFamilyDescription = (FamilyDescription)platformFontMap.get(str1);
/* 1655 */     if (localFamilyDescription == null) {
/* 1656 */       return null;
/*      */     }
/*      */ 
/* 1664 */     int j = -1;
/* 1665 */     if (paramString.equalsIgnoreCase(localFamilyDescription.plainFullName))
/* 1666 */       j = 0;
/* 1667 */     else if (paramString.equalsIgnoreCase(localFamilyDescription.boldFullName))
/* 1668 */       j = 1;
/* 1669 */     else if (paramString.equalsIgnoreCase(localFamilyDescription.italicFullName))
/* 1670 */       j = 2;
/* 1671 */     else if (paramString.equalsIgnoreCase(localFamilyDescription.boldItalicFullName)) {
/* 1672 */       j = 3;
/*      */     }
/* 1674 */     if ((j == -1) && (!paramString.equalsIgnoreCase(localFamilyDescription.familyName))) {
/* 1675 */       return null;
/*      */     }
/*      */ 
/* 1678 */     String str2 = null; String str3 = null;
/* 1679 */     String str4 = null; String str5 = null;
/*      */ 
/* 1681 */     boolean bool = false;
/*      */ 
/* 1688 */     getPlatformFontDirs(noType1Font);
/*      */ 
/* 1690 */     if (localFamilyDescription.plainFileName != null) {
/* 1691 */       str2 = getPathName(localFamilyDescription.plainFileName);
/* 1692 */       if (str2 == null) {
/* 1693 */         bool = true;
/*      */       }
/*      */     }
/*      */ 
/* 1697 */     if (localFamilyDescription.boldFileName != null) {
/* 1698 */       str3 = getPathName(localFamilyDescription.boldFileName);
/* 1699 */       if (str3 == null) {
/* 1700 */         bool = true;
/*      */       }
/*      */     }
/*      */ 
/* 1704 */     if (localFamilyDescription.italicFileName != null) {
/* 1705 */       str4 = getPathName(localFamilyDescription.italicFileName);
/* 1706 */       if (str4 == null) {
/* 1707 */         bool = true;
/*      */       }
/*      */     }
/*      */ 
/* 1711 */     if (localFamilyDescription.boldItalicFileName != null) {
/* 1712 */       str5 = getPathName(localFamilyDescription.boldItalicFileName);
/* 1713 */       if (str5 == null) {
/* 1714 */         bool = true;
/*      */       }
/*      */     }
/*      */ 
/* 1718 */     if (bool) {
/* 1719 */       if (FontUtilities.isLogging()) {
/* 1720 */         FontUtilities.getLogger().info("Hardcoded file missing looking for " + paramString);
/*      */       }
/*      */ 
/* 1723 */       platformFontMap.remove(str1);
/* 1724 */       return null;
/*      */     }
/*      */ 
/* 1728 */     final String[] arrayOfString = { str2, str3, str4, str5 };
/*      */ 
/* 1731 */     bool = ((Boolean)AccessController.doPrivileged(new PrivilegedAction()
/*      */     {
/*      */       public Boolean run() {
/* 1734 */         for (int i = 0; i < arrayOfString.length; i++) {
/* 1735 */           if (arrayOfString[i] != null)
/*      */           {
/* 1738 */             File localFile = new File(arrayOfString[i]);
/* 1739 */             if (!localFile.exists())
/* 1740 */               return Boolean.TRUE;
/*      */           }
/*      */         }
/* 1743 */         return Boolean.FALSE;
/*      */       }
/*      */     })).booleanValue();
/*      */ 
/* 1747 */     if (bool) {
/* 1748 */       if (FontUtilities.isLogging()) {
/* 1749 */         FontUtilities.getLogger().info("Hardcoded file missing looking for " + paramString);
/*      */       }
/*      */ 
/* 1752 */       platformFontMap.remove(str1);
/* 1753 */       return null;
/*      */     }
/*      */ 
/* 1762 */     Object localObject = null;
/* 1763 */     for (int k = 0; k < arrayOfString.length; k++) {
/* 1764 */       if (arrayOfString[k] != null)
/*      */       {
/* 1767 */         PhysicalFont localPhysicalFont = registerFontFile(arrayOfString[k], null, 0, false, 3);
/*      */ 
/* 1770 */         if (k == j) {
/* 1771 */           localObject = localPhysicalFont;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1788 */     FontFamily localFontFamily = FontFamily.getFamily(localFamilyDescription.familyName);
/* 1789 */     if (localFontFamily != null) {
/* 1790 */       if (localObject == null) {
/* 1791 */         localObject = localFontFamily.getFont(paramInt);
/* 1792 */         if (localObject == null)
/* 1793 */           localObject = localFontFamily.getClosestStyle(paramInt);
/*      */       }
/* 1795 */       else if ((paramInt > 0) && (paramInt != ((Font2D)localObject).style)) {
/* 1796 */         paramInt |= ((Font2D)localObject).style;
/* 1797 */         localObject = localFontFamily.getFont(paramInt);
/* 1798 */         if (localObject == null) {
/* 1799 */           localObject = localFontFamily.getClosestStyle(paramInt);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 1804 */     return localObject;
/*      */   }
/*      */   private synchronized HashMap<String, String> getFullNameToFileMap() {
/* 1807 */     if (this.fontToFileMap == null)
/*      */     {
/* 1809 */       this.pathDirs = getPlatformFontDirs(noType1Font);
/*      */ 
/* 1811 */       this.fontToFileMap = new HashMap(100);
/* 1812 */       this.fontToFamilyNameMap = new HashMap(100);
/* 1813 */       this.familyToFontListMap = new HashMap(50);
/* 1814 */       populateFontFileNameMap(this.fontToFileMap, this.fontToFamilyNameMap, this.familyToFontListMap, Locale.ENGLISH);
/*      */ 
/* 1818 */       if (FontUtilities.isWindows) {
/* 1819 */         resolveWindowsFonts();
/*      */       }
/* 1821 */       if (FontUtilities.isLogging()) {
/* 1822 */         logPlatformFontInfo();
/*      */       }
/*      */     }
/* 1825 */     return this.fontToFileMap;
/*      */   }
/*      */ 
/*      */   private void logPlatformFontInfo() {
/* 1829 */     PlatformLogger localPlatformLogger = FontUtilities.getLogger();
/* 1830 */     for (int i = 0; i < this.pathDirs.length; i++) {
/* 1831 */       localPlatformLogger.info("fontdir=" + this.pathDirs[i]);
/*      */     }
/* 1833 */     for (Iterator localIterator = this.fontToFileMap.keySet().iterator(); localIterator.hasNext(); ) { str = (String)localIterator.next();
/* 1834 */       localPlatformLogger.info("font=" + str + " file=" + (String)this.fontToFileMap.get(str));
/*      */     }
/* 1836 */     String str;
/* 1836 */     for (localIterator = this.fontToFamilyNameMap.keySet().iterator(); localIterator.hasNext(); ) { str = (String)localIterator.next();
/* 1837 */       localPlatformLogger.info("font=" + str + " family=" + (String)this.fontToFamilyNameMap.get(str));
/*      */     }
/*      */ 
/* 1840 */     for (localIterator = this.familyToFontListMap.keySet().iterator(); localIterator.hasNext(); ) { str = (String)localIterator.next();
/* 1841 */       localPlatformLogger.info("family=" + str + " fonts=" + this.familyToFontListMap.get(str));
/*      */     }
/*      */   }
/*      */ 
/*      */   protected String[] getFontNamesFromPlatform()
/*      */   {
/* 1848 */     if (getFullNameToFileMap().size() == 0) {
/* 1849 */       return null;
/*      */     }
/* 1851 */     checkForUnreferencedFontFiles();
/*      */ 
/* 1854 */     ArrayList localArrayList1 = new ArrayList();
/* 1855 */     for (ArrayList localArrayList2 : this.familyToFontListMap.values()) {
/* 1856 */       for (String str : localArrayList2) {
/* 1857 */         localArrayList1.add(str);
/*      */       }
/*      */     }
/* 1860 */     return (String[])localArrayList1.toArray(STR_ARRAY);
/*      */   }
/*      */ 
/*      */   public boolean gotFontsFromPlatform() {
/* 1864 */     return getFullNameToFileMap().size() != 0;
/*      */   }
/*      */ 
/*      */   public String getFileNameForFontName(String paramString) {
/* 1868 */     String str = paramString.toLowerCase(Locale.ENGLISH);
/* 1869 */     return (String)this.fontToFileMap.get(str);
/*      */   }
/*      */ 
/*      */   private PhysicalFont registerFontFile(String paramString) {
/* 1873 */     if ((new File(paramString).isAbsolute()) && (!this.registeredFonts.contains(paramString)))
/*      */     {
/* 1875 */       int i = -1;
/* 1876 */       int j = 6;
/* 1877 */       if (ttFilter.accept(null, paramString)) {
/* 1878 */         i = 0;
/* 1879 */         j = 3;
/* 1880 */       } else if (t1Filter.accept(null, paramString))
/*      */       {
/* 1882 */         i = 1;
/* 1883 */         j = 4;
/*      */       }
/* 1885 */       if (i == -1) {
/* 1886 */         return null;
/*      */       }
/* 1888 */       return registerFontFile(paramString, null, i, false, j);
/*      */     }
/* 1890 */     return null;
/*      */   }
/*      */ 
/*      */   protected void registerOtherFontFiles(HashSet paramHashSet)
/*      */   {
/* 1902 */     if (getFullNameToFileMap().size() == 0) {
/* 1903 */       return;
/*      */     }
/* 1905 */     for (String str : this.fontToFileMap.values())
/* 1906 */       registerFontFile(str);
/*      */   }
/*      */ 
/*      */   public boolean getFamilyNamesFromPlatform(TreeMap<String, String> paramTreeMap, Locale paramLocale)
/*      */   {
/* 1913 */     if (getFullNameToFileMap().size() == 0) {
/* 1914 */       return false;
/*      */     }
/* 1916 */     checkForUnreferencedFontFiles();
/* 1917 */     for (String str : this.fontToFamilyNameMap.values()) {
/* 1918 */       paramTreeMap.put(str.toLowerCase(paramLocale), str);
/*      */     }
/* 1920 */     return true;
/*      */   }
/*      */ 
/*      */   private String getPathName(final String paramString)
/*      */   {
/* 1927 */     File localFile = new File(paramString);
/* 1928 */     if (localFile.isAbsolute())
/* 1929 */       return paramString;
/* 1930 */     if (this.pathDirs.length == 1) {
/* 1931 */       return this.pathDirs[0] + File.separator + paramString;
/*      */     }
/* 1933 */     String str = (String)AccessController.doPrivileged(new PrivilegedAction()
/*      */     {
/*      */       public String run() {
/* 1936 */         for (int i = 0; i < SunFontManager.this.pathDirs.length; i++) {
/* 1937 */           File localFile = new File(SunFontManager.this.pathDirs[i] + File.separator + paramString);
/* 1938 */           if (localFile.exists()) {
/* 1939 */             return localFile.getAbsolutePath();
/*      */           }
/*      */         }
/* 1942 */         return null;
/*      */       }
/*      */     });
/* 1945 */     if (str != null) {
/* 1946 */       return str;
/*      */     }
/*      */ 
/* 1949 */     return paramString;
/*      */   }
/*      */ 
/*      */   private Font2D findFontFromPlatform(String paramString, int paramInt)
/*      */   {
/* 1973 */     if (getFullNameToFileMap().size() == 0) {
/* 1974 */       return null;
/*      */     }
/*      */ 
/* 1977 */     ArrayList localArrayList = null;
/* 1978 */     String str1 = null;
/* 1979 */     String str2 = (String)this.fontToFamilyNameMap.get(paramString);
/* 1980 */     if (str2 != null) {
/* 1981 */       str1 = (String)this.fontToFileMap.get(paramString);
/* 1982 */       localArrayList = (ArrayList)this.familyToFontListMap.get(str2.toLowerCase(Locale.ENGLISH));
/*      */     }
/*      */     else {
/* 1985 */       localArrayList = (ArrayList)this.familyToFontListMap.get(paramString);
/* 1986 */       if ((localArrayList != null) && (localArrayList.size() > 0)) {
/* 1987 */         localObject1 = ((String)localArrayList.get(0)).toLowerCase(Locale.ENGLISH);
/* 1988 */         if (localObject1 != null) {
/* 1989 */           str2 = (String)this.fontToFamilyNameMap.get(localObject1);
/*      */         }
/*      */       }
/*      */     }
/* 1993 */     if ((localArrayList == null) || (str2 == null)) {
/* 1994 */       return null;
/*      */     }
/* 1996 */     Object localObject1 = (String[])localArrayList.toArray(STR_ARRAY);
/* 1997 */     if (localObject1.length == 0) {
/* 1998 */       return null;
/*      */     }
/*      */ 
/* 2012 */     for (int i = 0; i < localObject1.length; i++) {
/* 2013 */       String str3 = localObject1[i].toLowerCase(Locale.ENGLISH);
/* 2014 */       localObject2 = (String)this.fontToFileMap.get(str3);
/* 2015 */       if (localObject2 == null) {
/* 2016 */         if (FontUtilities.isLogging()) {
/* 2017 */           FontUtilities.getLogger().info("Platform lookup : No file for font " + localObject1[i] + " in family " + str2);
/*      */         }
/*      */ 
/* 2021 */         return null;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2028 */     PhysicalFont localPhysicalFont = null;
/* 2029 */     if (str1 != null) {
/* 2030 */       localPhysicalFont = registerFontFile(getPathName(str1), null, 0, false, 3);
/*      */     }
/*      */ 
/* 2035 */     for (int j = 0; j < localObject1.length; j++) {
/* 2036 */       localObject2 = localObject1[j].toLowerCase(Locale.ENGLISH);
/* 2037 */       String str4 = (String)this.fontToFileMap.get(localObject2);
/* 2038 */       if ((str1 == null) || (!str1.equals(str4)))
/*      */       {
/* 2044 */         registerFontFile(getPathName(str4), null, 0, false, 3);
/*      */       }
/*      */     }
/*      */ 
/* 2048 */     Font2D localFont2D = null;
/* 2049 */     Object localObject2 = FontFamily.getFamily(str2);
/*      */ 
/* 2051 */     if (localPhysicalFont != null) {
/* 2052 */       paramInt |= localPhysicalFont.style;
/*      */     }
/* 2054 */     if (localObject2 != null) {
/* 2055 */       localFont2D = ((FontFamily)localObject2).getFont(paramInt);
/* 2056 */       if (localFont2D == null) {
/* 2057 */         localFont2D = ((FontFamily)localObject2).getClosestStyle(paramInt);
/*      */       }
/*      */     }
/* 2060 */     return localFont2D;
/*      */   }
/*      */ 
/*      */   public Font2D findFont2D(String paramString, int paramInt1, int paramInt2)
/*      */   {
/* 2074 */     String str1 = paramString.toLowerCase(Locale.ENGLISH);
/* 2075 */     String str2 = str1 + dotStyleStr(paramInt1);
/*      */ 
/* 2084 */     if (this._usingPerAppContextComposites) {
/* 2085 */       localObject2 = (ConcurrentHashMap)AppContext.getAppContext().get(CompositeFont.class);
/*      */ 
/* 2088 */       if (localObject2 != null)
/* 2089 */         localObject1 = (Font2D)((ConcurrentHashMap)localObject2).get(str2);
/*      */       else
/* 2091 */         localObject1 = null;
/*      */     }
/*      */     else {
/* 2094 */       localObject1 = (Font2D)this.fontNameCache.get(str2);
/*      */     }
/* 2096 */     if (localObject1 != null) {
/* 2097 */       return localObject1;
/*      */     }
/*      */ 
/* 2100 */     if (FontUtilities.isLogging()) {
/* 2101 */       FontUtilities.getLogger().info("Search for font: " + paramString);
/*      */     }
/*      */ 
/* 2110 */     if (FontUtilities.isWindows) {
/* 2111 */       if (str1.equals("ms sans serif"))
/* 2112 */         paramString = "sansserif";
/* 2113 */       else if (str1.equals("ms serif")) {
/* 2114 */         paramString = "serif";
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2126 */     if (str1.equals("default")) {
/* 2127 */       paramString = "dialog";
/*      */     }
/*      */ 
/* 2131 */     Object localObject2 = FontFamily.getFamily(paramString);
/* 2132 */     if (localObject2 != null) {
/* 2133 */       localObject1 = ((FontFamily)localObject2).getFontWithExactStyleMatch(paramInt1);
/* 2134 */       if (localObject1 == null) {
/* 2135 */         localObject1 = findDeferredFont(paramString, paramInt1);
/*      */       }
/* 2137 */       if (localObject1 == null) {
/* 2138 */         localObject1 = ((FontFamily)localObject2).getFont(paramInt1);
/*      */       }
/* 2140 */       if (localObject1 == null) {
/* 2141 */         localObject1 = ((FontFamily)localObject2).getClosestStyle(paramInt1);
/*      */       }
/* 2143 */       if (localObject1 != null) {
/* 2144 */         this.fontNameCache.put(str2, localObject1);
/* 2145 */         return localObject1;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2152 */     Object localObject1 = (Font2D)this.fullNameToFont.get(str1);
/*      */     Object localObject3;
/* 2153 */     if (localObject1 != null)
/*      */     {
/* 2166 */       if ((((Font2D)localObject1).style == paramInt1) || (paramInt1 == 0)) {
/* 2167 */         this.fontNameCache.put(str2, localObject1);
/* 2168 */         return localObject1;
/*      */       }
/*      */ 
/* 2176 */       localObject2 = FontFamily.getFamily(((Font2D)localObject1).getFamilyName(null));
/* 2177 */       if (localObject2 != null) {
/* 2178 */         localObject3 = ((FontFamily)localObject2).getFont(paramInt1 | ((Font2D)localObject1).style);
/*      */ 
/* 2180 */         if (localObject3 != null) {
/* 2181 */           this.fontNameCache.put(str2, localObject3);
/* 2182 */           return localObject3;
/*      */         }
/*      */ 
/* 2189 */         localObject3 = ((FontFamily)localObject2).getClosestStyle(paramInt1 | ((Font2D)localObject1).style);
/* 2190 */         if (localObject3 != null)
/*      */         {
/* 2198 */           if (((Font2D)localObject3).canDoStyle(paramInt1 | ((Font2D)localObject1).style)) {
/* 2199 */             this.fontNameCache.put(str2, localObject3);
/* 2200 */             return localObject3;
/*      */           }
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2208 */     if (FontUtilities.isWindows)
/*      */     {
/* 2210 */       localObject1 = findFontFromPlatformMap(str1, paramInt1);
/* 2211 */       if (FontUtilities.isLogging()) {
/* 2212 */         FontUtilities.getLogger().info("findFontFromPlatformMap returned " + localObject1);
/*      */       }
/*      */ 
/* 2215 */       if (localObject1 != null) {
/* 2216 */         this.fontNameCache.put(str2, localObject1);
/* 2217 */         return localObject1;
/*      */       }
/*      */ 
/* 2223 */       if (this.deferredFontFiles.size() > 0) {
/* 2224 */         localObject1 = findJREDeferredFont(str1, paramInt1);
/* 2225 */         if (localObject1 != null) {
/* 2226 */           this.fontNameCache.put(str2, localObject1);
/* 2227 */           return localObject1;
/*      */         }
/*      */       }
/* 2230 */       localObject1 = findFontFromPlatform(str1, paramInt1);
/* 2231 */       if (localObject1 != null) {
/* 2232 */         if (FontUtilities.isLogging()) {
/* 2233 */           FontUtilities.getLogger().info("Found font via platform API for request:\"" + paramString + "\":, style=" + paramInt1 + " found font: " + localObject1);
/*      */         }
/*      */ 
/* 2238 */         this.fontNameCache.put(str2, localObject1);
/* 2239 */         return localObject1;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2255 */     if (this.deferredFontFiles.size() > 0) {
/* 2256 */       localObject1 = findDeferredFont(paramString, paramInt1);
/* 2257 */       if (localObject1 != null) {
/* 2258 */         this.fontNameCache.put(str2, localObject1);
/* 2259 */         return localObject1;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2276 */     if ((FontUtilities.isSolaris) && (!this.loaded1dot0Fonts))
/*      */     {
/* 2280 */       if (str1.equals("timesroman")) {
/* 2281 */         localObject1 = findFont2D("serif", paramInt1, paramInt2);
/* 2282 */         this.fontNameCache.put(str2, localObject1);
/*      */       }
/* 2284 */       register1dot0Fonts();
/* 2285 */       this.loaded1dot0Fonts = true;
/* 2286 */       localObject3 = findFont2D(paramString, paramInt1, paramInt2);
/* 2287 */       return localObject3;
/*      */     }
/*      */ 
/* 2300 */     if ((this.fontsAreRegistered) || (this.fontsAreRegisteredPerAppContext)) {
/* 2301 */       localObject3 = null;
/*      */       Hashtable localHashtable;
/* 2304 */       if (this.fontsAreRegistered) {
/* 2305 */         localObject3 = this.createdByFamilyName;
/* 2306 */         localHashtable = this.createdByFullName;
/*      */       } else {
/* 2308 */         AppContext localAppContext = AppContext.getAppContext();
/* 2309 */         localObject3 = (Hashtable)localAppContext.get(regFamilyKey);
/*      */ 
/* 2311 */         localHashtable = (Hashtable)localAppContext.get(regFullNameKey);
/*      */       }
/*      */ 
/* 2315 */       localObject2 = (FontFamily)((Hashtable)localObject3).get(str1);
/* 2316 */       if (localObject2 != null) {
/* 2317 */         localObject1 = ((FontFamily)localObject2).getFontWithExactStyleMatch(paramInt1);
/* 2318 */         if (localObject1 == null) {
/* 2319 */           localObject1 = ((FontFamily)localObject2).getFont(paramInt1);
/*      */         }
/* 2321 */         if (localObject1 == null) {
/* 2322 */           localObject1 = ((FontFamily)localObject2).getClosestStyle(paramInt1);
/*      */         }
/* 2324 */         if (localObject1 != null) {
/* 2325 */           if (this.fontsAreRegistered) {
/* 2326 */             this.fontNameCache.put(str2, localObject1);
/*      */           }
/* 2328 */           return localObject1;
/*      */         }
/*      */       }
/* 2331 */       localObject1 = (Font2D)localHashtable.get(str1);
/* 2332 */       if (localObject1 != null) {
/* 2333 */         if (this.fontsAreRegistered) {
/* 2334 */           this.fontNameCache.put(str2, localObject1);
/*      */         }
/* 2336 */         return localObject1;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2343 */     if (!this.loadedAllFonts) {
/* 2344 */       if (FontUtilities.isLogging()) {
/* 2345 */         FontUtilities.getLogger().info("Load fonts looking for:" + paramString);
/*      */       }
/*      */ 
/* 2348 */       loadFonts();
/* 2349 */       this.loadedAllFonts = true;
/* 2350 */       return findFont2D(paramString, paramInt1, paramInt2);
/*      */     }
/*      */ 
/* 2353 */     if (!this.loadedAllFontFiles) {
/* 2354 */       if (FontUtilities.isLogging()) {
/* 2355 */         FontUtilities.getLogger().info("Load font files looking for:" + paramString);
/*      */       }
/*      */ 
/* 2358 */       loadFontFiles();
/* 2359 */       this.loadedAllFontFiles = true;
/* 2360 */       return findFont2D(paramString, paramInt1, paramInt2);
/*      */     }
/*      */ 
/* 2378 */     if ((localObject1 = findFont2DAllLocales(paramString, paramInt1)) != null) {
/* 2379 */       this.fontNameCache.put(str2, localObject1);
/* 2380 */       return localObject1;
/*      */     }
/*      */ 
/* 2393 */     if (FontUtilities.isWindows) {
/* 2394 */       localObject3 = getFontConfiguration().getFallbackFamilyName(paramString, null);
/*      */ 
/* 2396 */       if (localObject3 != null) {
/* 2397 */         localObject1 = findFont2D((String)localObject3, paramInt1, paramInt2);
/* 2398 */         this.fontNameCache.put(str2, localObject1);
/* 2399 */         return localObject1;
/*      */       }
/*      */     } else { if (str1.equals("timesroman")) {
/* 2402 */         localObject1 = findFont2D("serif", paramInt1, paramInt2);
/* 2403 */         this.fontNameCache.put(str2, localObject1);
/* 2404 */         return localObject1;
/* 2405 */       }if (str1.equals("helvetica")) {
/* 2406 */         localObject1 = findFont2D("sansserif", paramInt1, paramInt2);
/* 2407 */         this.fontNameCache.put(str2, localObject1);
/* 2408 */         return localObject1;
/* 2409 */       }if (str1.equals("courier")) {
/* 2410 */         localObject1 = findFont2D("monospaced", paramInt1, paramInt2);
/* 2411 */         this.fontNameCache.put(str2, localObject1);
/* 2412 */         return localObject1;
/*      */       }
/*      */     }
/* 2415 */     if (FontUtilities.isLogging()) {
/* 2416 */       FontUtilities.getLogger().info("No font found for:" + paramString);
/*      */     }
/*      */ 
/* 2419 */     switch (paramInt2) { case 1:
/* 2420 */       return getDefaultPhysicalFont();
/*      */     case 2:
/* 2421 */       return getDefaultLogicalFont(paramInt1); }
/* 2422 */     return null;
/*      */   }
/*      */ 
/*      */   public boolean usePlatformFontMetrics()
/*      */   {
/* 2432 */     return this.usePlatformFontMetrics;
/*      */   }
/*      */ 
/*      */   public int getNumFonts() {
/* 2436 */     return this.physicalFonts.size() + this.maxCompFont;
/*      */   }
/*      */ 
/*      */   private static boolean fontSupportsEncoding(Font paramFont, String paramString) {
/* 2440 */     return FontUtilities.getFont2D(paramFont).supportsEncoding(paramString);
/*      */   }
/*      */ 
/*      */   protected abstract String getFontPath(boolean paramBoolean);
/*      */ 
/*      */   public Font2D createFont2D(File paramFile, int paramInt, boolean paramBoolean, CreatedFontTracker paramCreatedFontTracker)
/*      */     throws FontFormatException
/*      */   {
/* 2454 */     String str = paramFile.getPath();
/* 2455 */     Object localObject1 = null;
/* 2456 */     final File localFile = paramFile;
/* 2457 */     final CreatedFontTracker localCreatedFontTracker = paramCreatedFontTracker;
/*      */     try {
/* 2459 */       switch (paramInt) {
/*      */       case 0:
/* 2461 */         localObject1 = new TrueTypeFont(str, null, 0, true);
/* 2462 */         break;
/*      */       case 1:
/* 2464 */         localObject1 = new Type1Font(str, null, paramBoolean);
/* 2465 */         break;
/*      */       default:
/* 2467 */         throw new FontFormatException("Unrecognised Font Format");
/*      */       }
/*      */     } catch (FontFormatException localFontFormatException) {
/* 2470 */       if (paramBoolean) {
/* 2471 */         AccessController.doPrivileged(new PrivilegedAction()
/*      */         {
/*      */           public Object run() {
/* 2474 */             if (localCreatedFontTracker != null) {
/* 2475 */               localCreatedFontTracker.subBytes((int)localFile.length());
/*      */             }
/* 2477 */             localFile.delete();
/* 2478 */             return null;
/*      */           }
/*      */         });
/*      */       }
/* 2482 */       throw localFontFormatException;
/*      */     }
/* 2484 */     if (paramBoolean) {
/* 2485 */       ((FileFont)localObject1).setFileToRemove(paramFile, paramCreatedFontTracker);
/* 2486 */       synchronized (FontManager.class)
/*      */       {
/* 2488 */         if (this.tmpFontFiles == null) {
/* 2489 */           this.tmpFontFiles = new Vector();
/*      */         }
/* 2491 */         this.tmpFontFiles.add(paramFile);
/*      */ 
/* 2493 */         if (this.fileCloser == null) {
/* 2494 */           final Runnable local8 = new Runnable() {
/*      */             public void run() {
/* 2496 */               AccessController.doPrivileged(new PrivilegedAction()
/*      */               {
/*      */                 public Object run()
/*      */                 {
/* 2500 */                   for (int i = 0; i < 20; i++)
/* 2501 */                     if (SunFontManager.this.fontFileCache[i] != null)
/*      */                       try {
/* 2503 */                         SunFontManager.this.fontFileCache[i].close();
/*      */                       }
/*      */                       catch (Exception localException1)
/*      */                       {
/*      */                       }
/* 2508 */                   if (SunFontManager.this.tmpFontFiles != null) {
/* 2509 */                     File[] arrayOfFile = new File[SunFontManager.this.tmpFontFiles.size()];
/* 2510 */                     arrayOfFile = (File[])SunFontManager.this.tmpFontFiles.toArray(arrayOfFile);
/* 2511 */                     for (int j = 0; j < arrayOfFile.length; j++)
/*      */                       try {
/* 2513 */                         arrayOfFile[j].delete();
/*      */                       }
/*      */                       catch (Exception localException2)
/*      */                       {
/*      */                       }
/*      */                   }
/* 2519 */                   return null;
/*      */                 }
/*      */               });
/*      */             }
/*      */           };
/* 2525 */           AccessController.doPrivileged(new PrivilegedAction()
/*      */           {
/*      */             public Void run()
/*      */             {
/* 2532 */               ThreadGroup localThreadGroup = ThreadGroupUtils.getRootThreadGroup();
/* 2533 */               SunFontManager.this.fileCloser = new Thread(localThreadGroup, local8);
/* 2534 */               SunFontManager.this.fileCloser.setContextClassLoader(null);
/* 2535 */               Runtime.getRuntime().addShutdownHook(SunFontManager.this.fileCloser);
/* 2536 */               return null;
/*      */             }
/*      */           });
/*      */         }
/*      */       }
/*      */     }
/* 2542 */     return localObject1;
/*      */   }
/*      */ 
/*      */   public synchronized String getFullNameByFileName(String paramString)
/*      */   {
/* 2549 */     PhysicalFont[] arrayOfPhysicalFont = getPhysicalFonts();
/* 2550 */     for (int i = 0; i < arrayOfPhysicalFont.length; i++) {
/* 2551 */       if (arrayOfPhysicalFont[i].platName.equals(paramString)) {
/* 2552 */         return arrayOfPhysicalFont[i].getFontName(null);
/*      */       }
/*      */     }
/* 2555 */     return null;
/*      */   }
/*      */ 
/*      */   public synchronized void deRegisterBadFont(Font2D paramFont2D)
/*      */   {
/* 2571 */     if (!(paramFont2D instanceof PhysicalFont))
/*      */     {
/* 2573 */       return;
/*      */     }
/* 2575 */     if (FontUtilities.isLogging()) {
/* 2576 */       FontUtilities.getLogger().severe("Deregister bad font: " + paramFont2D);
/*      */     }
/*      */ 
/* 2579 */     replaceFont((PhysicalFont)paramFont2D, getDefaultPhysicalFont());
/*      */   }
/*      */ 
/*      */   public synchronized void replaceFont(PhysicalFont paramPhysicalFont1, PhysicalFont paramPhysicalFont2)
/*      */   {
/* 2590 */     if (paramPhysicalFont1.handle.font2D != paramPhysicalFont1)
/*      */       return;
/*      */     Object localObject;
/*      */     int j;
/* 2598 */     if (paramPhysicalFont1 == paramPhysicalFont2) {
/* 2599 */       if (FontUtilities.isLogging()) {
/* 2600 */         FontUtilities.getLogger().severe("Can't replace bad font with itself " + paramPhysicalFont1);
/*      */       }
/*      */ 
/* 2603 */       localObject = getPhysicalFonts();
/* 2604 */       for (j = 0; j < localObject.length; j++) {
/* 2605 */         if (localObject[j] != paramPhysicalFont2) {
/* 2606 */           paramPhysicalFont2 = localObject[j];
/* 2607 */           break;
/*      */         }
/*      */       }
/* 2610 */       if (paramPhysicalFont1 == paramPhysicalFont2) {
/* 2611 */         if (FontUtilities.isLogging()) {
/* 2612 */           FontUtilities.getLogger().severe("This is bad. No good physicalFonts found.");
/*      */         }
/*      */ 
/* 2615 */         return;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2623 */     paramPhysicalFont1.handle.font2D = paramPhysicalFont2;
/* 2624 */     this.physicalFonts.remove(paramPhysicalFont1.fullName);
/* 2625 */     this.fullNameToFont.remove(paramPhysicalFont1.fullName.toLowerCase(Locale.ENGLISH));
/* 2626 */     FontFamily.remove(paramPhysicalFont1);
/*      */ 
/* 2628 */     if (this.localeFullNamesToFont != null) {
/* 2629 */       localObject = (Map.Entry[])this.localeFullNamesToFont.entrySet().toArray(new Map.Entry[0]);
/*      */ 
/* 2635 */       for (j = 0; j < localObject.length; j++) {
/* 2636 */         if (localObject[j].getValue() == paramPhysicalFont1) {
/*      */           try {
/* 2638 */             localObject[j].setValue(paramPhysicalFont2);
/*      */           }
/*      */           catch (Exception localException)
/*      */           {
/* 2643 */             this.localeFullNamesToFont.remove(localObject[j].getKey());
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 2649 */     for (int i = 0; i < this.maxCompFont; i++)
/*      */     {
/* 2679 */       if (paramPhysicalFont2.getRank() > 2)
/* 2680 */         this.compFonts[i].replaceComponentFont(paramPhysicalFont1, paramPhysicalFont2);
/*      */     }
/*      */   }
/*      */ 
/*      */   private synchronized void loadLocaleNames()
/*      */   {
/* 2686 */     if (this.localeFullNamesToFont != null) {
/* 2687 */       return;
/*      */     }
/* 2689 */     this.localeFullNamesToFont = new HashMap();
/* 2690 */     Font2D[] arrayOfFont2D = getRegisteredFonts();
/* 2691 */     for (int i = 0; i < arrayOfFont2D.length; i++)
/* 2692 */       if ((arrayOfFont2D[i] instanceof TrueTypeFont)) {
/* 2693 */         TrueTypeFont localTrueTypeFont = (TrueTypeFont)arrayOfFont2D[i];
/* 2694 */         String[] arrayOfString = localTrueTypeFont.getAllFullNames();
/* 2695 */         for (int j = 0; j < arrayOfString.length; j++) {
/* 2696 */           this.localeFullNamesToFont.put(arrayOfString[j], localTrueTypeFont);
/*      */         }
/* 2698 */         FontFamily localFontFamily = FontFamily.getFamily(localTrueTypeFont.familyName);
/* 2699 */         if (localFontFamily != null)
/* 2700 */           FontFamily.addLocaleNames(localFontFamily, localTrueTypeFont.getAllFamilyNames());
/*      */       }
/*      */   }
/*      */ 
/*      */   private Font2D findFont2DAllLocales(String paramString, int paramInt)
/*      */   {
/* 2715 */     if (FontUtilities.isLogging()) {
/* 2716 */       FontUtilities.getLogger().info("Searching localised font names for:" + paramString);
/*      */     }
/*      */ 
/* 2724 */     if (this.localeFullNamesToFont == null) {
/* 2725 */       loadLocaleNames();
/*      */     }
/* 2727 */     String str = paramString.toLowerCase();
/* 2728 */     Font2D localFont2D = null;
/*      */ 
/* 2731 */     FontFamily localFontFamily = FontFamily.getLocaleFamily(str);
/* 2732 */     if (localFontFamily != null) {
/* 2733 */       localFont2D = localFontFamily.getFont(paramInt);
/* 2734 */       if (localFont2D == null) {
/* 2735 */         localFont2D = localFontFamily.getClosestStyle(paramInt);
/*      */       }
/* 2737 */       if (localFont2D != null) {
/* 2738 */         return localFont2D;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2743 */     synchronized (this) {
/* 2744 */       localFont2D = (Font2D)this.localeFullNamesToFont.get(paramString);
/*      */     }
/* 2746 */     if (localFont2D != null) {
/* 2747 */       if ((localFont2D.style == paramInt) || (paramInt == 0)) {
/* 2748 */         return localFont2D;
/*      */       }
/* 2750 */       localFontFamily = FontFamily.getFamily(localFont2D.getFamilyName(null));
/* 2751 */       if (localFontFamily != null) {
/* 2752 */         ??? = localFontFamily.getFont(paramInt);
/*      */ 
/* 2754 */         if (??? != null) {
/* 2755 */           return ???;
/*      */         }
/* 2757 */         ??? = localFontFamily.getClosestStyle(paramInt);
/* 2758 */         if (??? != null)
/*      */         {
/* 2767 */           if (!((Font2D)???).canDoStyle(paramInt)) {
/* 2768 */             ??? = null;
/*      */           }
/* 2770 */           return ???;
/*      */         }
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2776 */     return localFont2D;
/*      */   }
/*      */ 
/*      */   public boolean maybeUsingAlternateCompositeFonts()
/*      */   {
/* 2855 */     return (this._usingAlternateComposites) || (this._usingPerAppContextComposites);
/*      */   }
/*      */ 
/*      */   public boolean usingAlternateCompositeFonts() {
/* 2859 */     return (this._usingAlternateComposites) || ((this._usingPerAppContextComposites) && (AppContext.getAppContext().get(CompositeFont.class) != null));
/*      */   }
/*      */ 
/*      */   private static boolean maybeMultiAppContext()
/*      */   {
/* 2865 */     Boolean localBoolean = (Boolean)AccessController.doPrivileged(new PrivilegedAction()
/*      */     {
/*      */       public Object run()
/*      */       {
/* 2869 */         SecurityManager localSecurityManager = System.getSecurityManager();
/* 2870 */         return new Boolean(localSecurityManager instanceof AppletSecurity);
/*      */       }
/*      */     });
/* 2874 */     return localBoolean.booleanValue();
/*      */   }
/*      */ 
/*      */   public synchronized void useAlternateFontforJALocales()
/*      */   {
/* 2882 */     if (FontUtilities.isLogging()) {
/* 2883 */       FontUtilities.getLogger().info("Entered useAlternateFontforJALocales().");
/*      */     }
/*      */ 
/* 2886 */     if (!FontUtilities.isWindows) {
/* 2887 */       return;
/*      */     }
/*      */ 
/* 2890 */     if (!maybeMultiAppContext()) {
/* 2891 */       gAltJAFont = true;
/*      */     } else {
/* 2893 */       AppContext localAppContext = AppContext.getAppContext();
/* 2894 */       localAppContext.put(altJAFontKey, altJAFontKey);
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean usingAlternateFontforJALocales() {
/* 2899 */     if (!maybeMultiAppContext()) {
/* 2900 */       return gAltJAFont;
/*      */     }
/* 2902 */     AppContext localAppContext = AppContext.getAppContext();
/* 2903 */     return localAppContext.get(altJAFontKey) == altJAFontKey;
/*      */   }
/*      */ 
/*      */   public synchronized void preferLocaleFonts()
/*      */   {
/* 2908 */     if (FontUtilities.isLogging()) {
/* 2909 */       FontUtilities.getLogger().info("Entered preferLocaleFonts().");
/*      */     }
/*      */ 
/* 2912 */     if (!FontConfiguration.willReorderForStartupLocale()) {
/* 2913 */       return;
/*      */     }
/*      */ 
/* 2916 */     if (!maybeMultiAppContext()) {
/* 2917 */       if (this.gLocalePref == true) {
/* 2918 */         return;
/*      */       }
/* 2920 */       this.gLocalePref = true;
/* 2921 */       createCompositeFonts(this.fontNameCache, this.gLocalePref, this.gPropPref);
/* 2922 */       this._usingAlternateComposites = true;
/*      */     } else {
/* 2924 */       AppContext localAppContext = AppContext.getAppContext();
/* 2925 */       if (localAppContext.get(localeFontKey) == localeFontKey) {
/* 2926 */         return;
/*      */       }
/* 2928 */       localAppContext.put(localeFontKey, localeFontKey);
/* 2929 */       boolean bool = localAppContext.get(proportionalFontKey) == proportionalFontKey;
/*      */ 
/* 2932 */       ConcurrentHashMap localConcurrentHashMap = new ConcurrentHashMap();
/*      */ 
/* 2934 */       localAppContext.put(CompositeFont.class, localConcurrentHashMap);
/* 2935 */       this._usingPerAppContextComposites = true;
/* 2936 */       createCompositeFonts(localConcurrentHashMap, true, bool);
/*      */     }
/*      */   }
/*      */ 
/*      */   public synchronized void preferProportionalFonts() {
/* 2941 */     if (FontUtilities.isLogging()) {
/* 2942 */       FontUtilities.getLogger().info("Entered preferProportionalFonts().");
/*      */     }
/*      */ 
/* 2948 */     if (!FontConfiguration.hasMonoToPropMap()) {
/* 2949 */       return;
/*      */     }
/*      */ 
/* 2952 */     if (!maybeMultiAppContext()) {
/* 2953 */       if (this.gPropPref == true) {
/* 2954 */         return;
/*      */       }
/* 2956 */       this.gPropPref = true;
/* 2957 */       createCompositeFonts(this.fontNameCache, this.gLocalePref, this.gPropPref);
/* 2958 */       this._usingAlternateComposites = true;
/*      */     } else {
/* 2960 */       AppContext localAppContext = AppContext.getAppContext();
/* 2961 */       if (localAppContext.get(proportionalFontKey) == proportionalFontKey) {
/* 2962 */         return;
/*      */       }
/* 2964 */       localAppContext.put(proportionalFontKey, proportionalFontKey);
/* 2965 */       boolean bool = localAppContext.get(localeFontKey) == localeFontKey;
/*      */ 
/* 2968 */       ConcurrentHashMap localConcurrentHashMap = new ConcurrentHashMap();
/*      */ 
/* 2970 */       localAppContext.put(CompositeFont.class, localConcurrentHashMap);
/* 2971 */       this._usingPerAppContextComposites = true;
/* 2972 */       createCompositeFonts(localConcurrentHashMap, bool, true);
/*      */     }
/*      */   }
/*      */ 
/*      */   private static HashSet<String> getInstalledNames()
/*      */   {
/* 2978 */     if (installedNames == null) {
/* 2979 */       Locale localLocale = getSystemStartupLocale();
/* 2980 */       SunFontManager localSunFontManager = getInstance();
/* 2981 */       String[] arrayOfString = localSunFontManager.getInstalledFontFamilyNames(localLocale);
/*      */ 
/* 2983 */       Font[] arrayOfFont = localSunFontManager.getAllInstalledFonts();
/* 2984 */       HashSet localHashSet = new HashSet();
/* 2985 */       for (int i = 0; i < arrayOfString.length; i++) {
/* 2986 */         localHashSet.add(arrayOfString[i].toLowerCase(localLocale));
/*      */       }
/* 2988 */       for (i = 0; i < arrayOfFont.length; i++) {
/* 2989 */         localHashSet.add(arrayOfFont[i].getFontName(localLocale).toLowerCase(localLocale));
/*      */       }
/* 2991 */       installedNames = localHashSet;
/*      */     }
/* 2993 */     return installedNames;
/*      */   }
/*      */ 
/*      */   public boolean registerFont(Font paramFont)
/*      */   {
/* 3008 */     if (paramFont == null) {
/* 3009 */       return false;
/*      */     }
/*      */ 
/* 3013 */     synchronized (regFamilyKey) {
/* 3014 */       if (this.createdByFamilyName == null) {
/* 3015 */         this.createdByFamilyName = new Hashtable();
/* 3016 */         this.createdByFullName = new Hashtable();
/*      */       }
/*      */     }
/*      */ 
/* 3020 */     if (!FontAccess.getFontAccess().isCreatedFont(paramFont)) {
/* 3021 */       return false;
/*      */     }
/*      */ 
/* 3041 */     ??? = getInstalledNames();
/* 3042 */     Locale localLocale = getSystemStartupLocale();
/* 3043 */     String str1 = paramFont.getFamily(localLocale).toLowerCase();
/* 3044 */     String str2 = paramFont.getFontName(localLocale).toLowerCase();
/* 3045 */     if ((((HashSet)???).contains(str1)) || (((HashSet)???).contains(str2)))
/* 3046 */       return false;
/*      */     Hashtable localHashtable1;
/*      */     Hashtable localHashtable2;
/* 3052 */     if (!maybeMultiAppContext()) {
/* 3053 */       localHashtable1 = this.createdByFamilyName;
/* 3054 */       localHashtable2 = this.createdByFullName;
/* 3055 */       this.fontsAreRegistered = true;
/*      */     } else {
/* 3057 */       localObject2 = AppContext.getAppContext();
/* 3058 */       localHashtable1 = (Hashtable)((AppContext)localObject2).get(regFamilyKey);
/*      */ 
/* 3060 */       localHashtable2 = (Hashtable)((AppContext)localObject2).get(regFullNameKey);
/*      */ 
/* 3062 */       if (localHashtable1 == null) {
/* 3063 */         localHashtable1 = new Hashtable();
/* 3064 */         localHashtable2 = new Hashtable();
/* 3065 */         ((AppContext)localObject2).put(regFamilyKey, localHashtable1);
/* 3066 */         ((AppContext)localObject2).put(regFullNameKey, localHashtable2);
/*      */       }
/* 3068 */       this.fontsAreRegisteredPerAppContext = true;
/*      */     }
/*      */ 
/* 3071 */     Object localObject2 = FontUtilities.getFont2D(paramFont);
/* 3072 */     int i = ((Font2D)localObject2).getStyle();
/* 3073 */     FontFamily localFontFamily = (FontFamily)localHashtable1.get(str1);
/* 3074 */     if (localFontFamily == null) {
/* 3075 */       localFontFamily = new FontFamily(paramFont.getFamily(localLocale));
/* 3076 */       localHashtable1.put(str1, localFontFamily);
/*      */     }
/*      */ 
/* 3084 */     if (this.fontsAreRegistered) {
/* 3085 */       removeFromCache(localFontFamily.getFont(0));
/* 3086 */       removeFromCache(localFontFamily.getFont(1));
/* 3087 */       removeFromCache(localFontFamily.getFont(2));
/* 3088 */       removeFromCache(localFontFamily.getFont(3));
/* 3089 */       removeFromCache((Font2D)localHashtable2.get(str2));
/*      */     }
/* 3091 */     localFontFamily.setFont((Font2D)localObject2, i);
/* 3092 */     localHashtable2.put(str2, localObject2);
/* 3093 */     return true;
/*      */   }
/*      */ 
/*      */   private void removeFromCache(Font2D paramFont2D)
/*      */   {
/* 3098 */     if (paramFont2D == null) {
/* 3099 */       return;
/*      */     }
/* 3101 */     String[] arrayOfString = (String[])this.fontNameCache.keySet().toArray(STR_ARRAY);
/* 3102 */     for (int i = 0; i < arrayOfString.length; i++)
/* 3103 */       if (this.fontNameCache.get(arrayOfString[i]) == paramFont2D)
/* 3104 */         this.fontNameCache.remove(arrayOfString[i]);
/*      */   }
/*      */ 
/*      */   public TreeMap<String, String> getCreatedFontFamilyNames()
/*      */   {
/*      */     Hashtable localHashtable;
/* 3113 */     if (this.fontsAreRegistered) {
/* 3114 */       localHashtable = this.createdByFamilyName;
/* 3115 */     } else if (this.fontsAreRegisteredPerAppContext) {
/* 3116 */       localObject1 = AppContext.getAppContext();
/* 3117 */       localHashtable = (Hashtable)((AppContext)localObject1).get(regFamilyKey);
/*      */     }
/*      */     else {
/* 3120 */       return null;
/*      */     }
/*      */ 
/* 3123 */     Object localObject1 = getSystemStartupLocale();
/* 3124 */     synchronized (localHashtable) {
/* 3125 */       TreeMap localTreeMap = new TreeMap();
/* 3126 */       for (FontFamily localFontFamily : localHashtable.values()) {
/* 3127 */         Font2D localFont2D = localFontFamily.getFont(0);
/* 3128 */         if (localFont2D == null) {
/* 3129 */           localFont2D = localFontFamily.getClosestStyle(0);
/*      */         }
/* 3131 */         String str = localFont2D.getFamilyName((Locale)localObject1);
/* 3132 */         localTreeMap.put(str.toLowerCase((Locale)localObject1), str);
/*      */       }
/* 3134 */       return localTreeMap;
/*      */     }
/*      */   }
/*      */ 
/*      */   public Font[] getCreatedFonts()
/*      */   {
/*      */     Hashtable localHashtable;
/* 3141 */     if (this.fontsAreRegistered) {
/* 3142 */       localHashtable = this.createdByFullName;
/* 3143 */     } else if (this.fontsAreRegisteredPerAppContext) {
/* 3144 */       localObject1 = AppContext.getAppContext();
/* 3145 */       localHashtable = (Hashtable)((AppContext)localObject1).get(regFullNameKey);
/*      */     }
/*      */     else {
/* 3148 */       return null;
/*      */     }
/*      */ 
/* 3151 */     Object localObject1 = getSystemStartupLocale();
/* 3152 */     synchronized (localHashtable) {
/* 3153 */       Font[] arrayOfFont = new Font[localHashtable.size()];
/* 3154 */       int i = 0;
/* 3155 */       for (Font2D localFont2D : localHashtable.values()) {
/* 3156 */         arrayOfFont[(i++)] = new Font(localFont2D.getFontName((Locale)localObject1), 0, 1);
/*      */       }
/* 3158 */       return arrayOfFont;
/*      */     }
/*      */   }
/*      */ 
/*      */   protected String[] getPlatformFontDirs(boolean paramBoolean)
/*      */   {
/* 3166 */     if (this.pathDirs != null) {
/* 3167 */       return this.pathDirs;
/*      */     }
/*      */ 
/* 3170 */     String str = getPlatformFontPath(paramBoolean);
/* 3171 */     StringTokenizer localStringTokenizer = new StringTokenizer(str, File.pathSeparator);
/*      */ 
/* 3173 */     ArrayList localArrayList = new ArrayList();
/*      */     try {
/* 3175 */       while (localStringTokenizer.hasMoreTokens())
/* 3176 */         localArrayList.add(localStringTokenizer.nextToken());
/*      */     }
/*      */     catch (NoSuchElementException localNoSuchElementException) {
/*      */     }
/* 3180 */     this.pathDirs = ((String[])localArrayList.toArray(new String[0]));
/* 3181 */     return this.pathDirs;
/*      */   }
/*      */ 
/*      */   public abstract String[] getDefaultPlatformFont();
/*      */ 
/*      */   private void addDirFonts(String paramString, File paramFile, FilenameFilter paramFilenameFilter, int paramInt1, boolean paramBoolean1, int paramInt2, boolean paramBoolean2, boolean paramBoolean3)
/*      */   {
/* 3200 */     String[] arrayOfString1 = paramFile.list(paramFilenameFilter);
/* 3201 */     if ((arrayOfString1 == null) || (arrayOfString1.length == 0)) {
/* 3202 */       return;
/*      */     }
/* 3204 */     String[] arrayOfString2 = new String[arrayOfString1.length];
/* 3205 */     String[][] arrayOfString; = new String[arrayOfString1.length][];
/* 3206 */     int i = 0;
/*      */ 
/* 3208 */     for (int j = 0; j < arrayOfString1.length; j++) {
/* 3209 */       File localFile = new File(paramFile, arrayOfString1[j]);
/* 3210 */       String str1 = null;
/* 3211 */       if (paramBoolean3)
/*      */         try {
/* 3213 */           str1 = localFile.getCanonicalPath();
/*      */         }
/*      */         catch (IOException localIOException) {
/*      */         }
/* 3217 */       if (str1 == null) {
/* 3218 */         str1 = paramString + File.separator + arrayOfString1[j];
/*      */       }
/*      */ 
/* 3222 */       if (!this.registeredFontFiles.contains(str1))
/*      */       {
/* 3226 */         if ((this.badFonts != null) && (this.badFonts.contains(str1))) {
/* 3227 */           if (FontUtilities.debugFonts()) {
/* 3228 */             FontUtilities.getLogger().warning("skip bad font " + str1);
/*      */           }
/*      */ 
/*      */         }
/*      */         else
/*      */         {
/* 3234 */           this.registeredFontFiles.add(str1);
/*      */ 
/* 3236 */           if ((FontUtilities.debugFonts()) && (FontUtilities.getLogger().isLoggable(800)))
/*      */           {
/* 3238 */             String str2 = "Registering font " + str1;
/* 3239 */             String[] arrayOfString3 = getNativeNames(str1, null);
/* 3240 */             if (arrayOfString3 == null) {
/* 3241 */               str2 = str2 + " with no native name";
/*      */             } else {
/* 3243 */               str2 = str2 + " with native name(s) " + arrayOfString3[0];
/* 3244 */               for (int k = 1; k < arrayOfString3.length; k++) {
/* 3245 */                 str2 = str2 + ", " + arrayOfString3[k];
/*      */               }
/*      */             }
/* 3248 */             FontUtilities.getLogger().info(str2);
/*      */           }
/* 3250 */           arrayOfString2[i] = str1;
/* 3251 */           arrayOfString;[(i++)] = getNativeNames(str1, null);
/*      */         }
/*      */       }
/*      */     }
/* 3253 */     registerFonts(arrayOfString2, arrayOfString;, i, paramInt1, paramBoolean1, paramInt2, paramBoolean2);
/*      */   }
/*      */ 
/*      */   protected String[] getNativeNames(String paramString1, String paramString2)
/*      */   {
/* 3260 */     return null;
/*      */   }
/*      */ 
/*      */   protected String getFileNameFromPlatformName(String paramString)
/*      */   {
/* 3270 */     return this.fontConfig.getFileNameFromPlatformName(paramString);
/*      */   }
/*      */ 
/*      */   public FontConfiguration getFontConfiguration()
/*      */   {
/* 3277 */     return this.fontConfig;
/*      */   }
/*      */ 
/*      */   public String getPlatformFontPath(boolean paramBoolean)
/*      */   {
/* 3284 */     if (this.fontPath == null) {
/* 3285 */       this.fontPath = getFontPath(paramBoolean);
/*      */     }
/* 3287 */     return this.fontPath;
/*      */   }
/*      */ 
/*      */   public static boolean isOpenJDK() {
/* 3291 */     return FontUtilities.isOpenJDK;
/*      */   }
/*      */ 
/*      */   protected void loadFonts() {
/* 3295 */     if (this.discoveredAllFonts) {
/* 3296 */       return;
/*      */     }
/*      */ 
/* 3299 */     synchronized (this) {
/* 3300 */       if (FontUtilities.debugFonts()) {
/* 3301 */         Thread.dumpStack();
/* 3302 */         FontUtilities.getLogger().info("SunGraphicsEnvironment.loadFonts() called");
/*      */       }
/*      */ 
/* 3305 */       initialiseDeferredFonts();
/*      */ 
/* 3307 */       AccessController.doPrivileged(new PrivilegedAction()
/*      */       {
/*      */         public Object run() {
/* 3310 */           if (SunFontManager.this.fontPath == null) {
/* 3311 */             SunFontManager.this.fontPath = SunFontManager.this.getPlatformFontPath(SunFontManager.noType1Font);
/* 3312 */             SunFontManager.this.registerFontDirs(SunFontManager.this.fontPath);
/*      */           }
/* 3314 */           if (SunFontManager.this.fontPath != null)
/*      */           {
/* 3318 */             if (!SunFontManager.this.gotFontsFromPlatform()) {
/* 3319 */               SunFontManager.this.registerFontsOnPath(SunFontManager.this.fontPath, false, 6, false, true);
/*      */ 
/* 3322 */               SunFontManager.this.loadedAllFontFiles = true;
/*      */             }
/*      */           }
/* 3325 */           SunFontManager.this.registerOtherFontFiles(SunFontManager.this.registeredFontFiles);
/* 3326 */           SunFontManager.this.discoveredAllFonts = true;
/* 3327 */           return null;
/*      */         }
/*      */       });
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void registerFontDirs(String paramString)
/*      */   {
/*      */   }
/*      */ 
/*      */   private void registerFontsOnPath(String paramString, boolean paramBoolean1, int paramInt, boolean paramBoolean2, boolean paramBoolean3)
/*      */   {
/* 3341 */     StringTokenizer localStringTokenizer = new StringTokenizer(paramString, File.pathSeparator);
/*      */     try
/*      */     {
/* 3344 */       while (localStringTokenizer.hasMoreTokens())
/* 3345 */         registerFontsInDir(localStringTokenizer.nextToken(), paramBoolean1, paramInt, paramBoolean2, paramBoolean3);
/*      */     }
/*      */     catch (NoSuchElementException localNoSuchElementException)
/*      */     {
/*      */     }
/*      */   }
/*      */ 
/*      */   public void registerFontsInDir(String paramString)
/*      */   {
/* 3355 */     registerFontsInDir(paramString, true, 2, true, false);
/*      */   }
/*      */ 
/*      */   protected void registerFontsInDir(String paramString, boolean paramBoolean1, int paramInt, boolean paramBoolean2, boolean paramBoolean3)
/*      */   {
/* 3363 */     File localFile = new File(paramString);
/* 3364 */     addDirFonts(paramString, localFile, ttFilter, 0, paramBoolean1, paramInt == 6 ? 3 : paramInt, paramBoolean2, paramBoolean3);
/*      */ 
/* 3369 */     addDirFonts(paramString, localFile, t1Filter, 1, paramBoolean1, paramInt == 6 ? 4 : paramInt, paramBoolean2, paramBoolean3);
/*      */   }
/*      */ 
/*      */   protected void registerFontDir(String paramString)
/*      */   {
/*      */   }
/*      */ 
/*      */   public synchronized String getDefaultFontFile()
/*      */   {
/* 3384 */     if (this.defaultFontFileName == null) {
/* 3385 */       initDefaultFonts();
/*      */     }
/* 3387 */     return this.defaultFontFileName;
/*      */   }
/*      */ 
/*      */   private void initDefaultFonts() {
/* 3391 */     if (!isOpenJDK()) {
/* 3392 */       this.defaultFontName = "Lucida Sans Regular";
/* 3393 */       if (useAbsoluteFontFileNames()) {
/* 3394 */         this.defaultFontFileName = (jreFontDirName + File.separator + "LucidaSansRegular.ttf");
/*      */       }
/*      */       else
/* 3397 */         this.defaultFontFileName = "LucidaSansRegular.ttf";
/*      */     }
/*      */   }
/*      */ 
/*      */   protected boolean useAbsoluteFontFileNames()
/*      */   {
/* 3407 */     return true;
/*      */   }
/*      */ 
/*      */   protected abstract FontConfiguration createFontConfiguration();
/*      */ 
/*      */   public abstract FontConfiguration createFontConfiguration(boolean paramBoolean1, boolean paramBoolean2);
/*      */ 
/*      */   public synchronized String getDefaultFontFaceName()
/*      */   {
/* 3425 */     if (this.defaultFontName == null) {
/* 3426 */       initDefaultFonts();
/*      */     }
/* 3428 */     return this.defaultFontName;
/*      */   }
/*      */ 
/*      */   public void loadFontFiles() {
/* 3432 */     loadFonts();
/* 3433 */     if (this.loadedAllFontFiles) {
/* 3434 */       return;
/*      */     }
/*      */ 
/* 3437 */     synchronized (this) {
/* 3438 */       if (FontUtilities.debugFonts()) {
/* 3439 */         Thread.dumpStack();
/* 3440 */         FontUtilities.getLogger().info("loadAllFontFiles() called");
/*      */       }
/* 3442 */       AccessController.doPrivileged(new PrivilegedAction()
/*      */       {
/*      */         public Object run() {
/* 3445 */           if (SunFontManager.this.fontPath == null) {
/* 3446 */             SunFontManager.this.fontPath = SunFontManager.this.getPlatformFontPath(SunFontManager.noType1Font);
/*      */           }
/* 3448 */           if (SunFontManager.this.fontPath != null)
/*      */           {
/* 3452 */             SunFontManager.this.registerFontsOnPath(SunFontManager.this.fontPath, false, 6, false, true);
/*      */           }
/*      */ 
/* 3456 */           SunFontManager.this.loadedAllFontFiles = true;
/* 3457 */           return null;
/*      */         }
/*      */       });
/*      */     }
/*      */   }
/*      */ 
/*      */   private void initCompositeFonts(FontConfiguration paramFontConfiguration, ConcurrentHashMap<String, Font2D> paramConcurrentHashMap)
/*      */   {
/* 3474 */     if (FontUtilities.isLogging()) {
/* 3475 */       FontUtilities.getLogger().info("Initialising composite fonts");
/*      */     }
/*      */ 
/* 3479 */     int i = paramFontConfiguration.getNumberCoreFonts();
/* 3480 */     String[] arrayOfString1 = paramFontConfiguration.getPlatformFontNames();
/*      */     Object localObject;
/*      */     String[] arrayOfString2;
/* 3481 */     for (int j = 0; j < arrayOfString1.length; j++) {
/* 3482 */       String str = arrayOfString1[j];
/* 3483 */       localObject = getFileNameFromPlatformName(str);
/*      */ 
/* 3485 */       arrayOfString2 = null;
/* 3486 */       if ((localObject == null) || (((String)localObject).equals(str)))
/*      */       {
/* 3491 */         localObject = str;
/*      */       } else {
/* 3493 */         if (j < i)
/*      */         {
/* 3508 */           addFontToPlatformFontPath(str);
/*      */         }
/* 3510 */         arrayOfString2 = getNativeNames((String)localObject, str);
/*      */       }
/*      */ 
/* 3519 */       registerFontFile((String)localObject, arrayOfString2, 2, true);
/*      */     }
/*      */ 
/* 3531 */     registerPlatformFontsUsedByFontConfiguration();
/*      */ 
/* 3533 */     CompositeFontDescriptor[] arrayOfCompositeFontDescriptor = paramFontConfiguration.get2DCompositeFontInfo();
/*      */ 
/* 3535 */     for (int k = 0; k < arrayOfCompositeFontDescriptor.length; k++) {
/* 3536 */       localObject = arrayOfCompositeFontDescriptor[k];
/* 3537 */       arrayOfString2 = ((CompositeFontDescriptor)localObject).getComponentFileNames();
/* 3538 */       String[] arrayOfString3 = ((CompositeFontDescriptor)localObject).getComponentFaceNames();
/*      */ 
/* 3543 */       if (missingFontFiles != null) {
/* 3544 */         for (int m = 0; m < arrayOfString2.length; m++) {
/* 3545 */           if (missingFontFiles.contains(arrayOfString2[m])) {
/* 3546 */             arrayOfString2[m] = getDefaultFontFile();
/* 3547 */             arrayOfString3[m] = getDefaultFontFaceName();
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 3558 */       if (paramConcurrentHashMap != null) {
/* 3559 */         registerCompositeFont(((CompositeFontDescriptor)localObject).getFaceName(), arrayOfString2, arrayOfString3, ((CompositeFontDescriptor)localObject).getCoreComponentCount(), ((CompositeFontDescriptor)localObject).getExclusionRanges(), ((CompositeFontDescriptor)localObject).getExclusionRangeLimits(), true, paramConcurrentHashMap);
/*      */       }
/*      */       else
/*      */       {
/* 3568 */         registerCompositeFont(((CompositeFontDescriptor)localObject).getFaceName(), arrayOfString2, arrayOfString3, ((CompositeFontDescriptor)localObject).getCoreComponentCount(), ((CompositeFontDescriptor)localObject).getExclusionRanges(), ((CompositeFontDescriptor)localObject).getExclusionRangeLimits(), true);
/*      */       }
/*      */ 
/* 3575 */       if (FontUtilities.debugFonts())
/* 3576 */         FontUtilities.getLogger().info("registered " + ((CompositeFontDescriptor)localObject).getFaceName());
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void addFontToPlatformFontPath(String paramString)
/*      */   {
/*      */   }
/*      */ 
/*      */   protected void registerFontFile(String paramString, String[] paramArrayOfString, int paramInt, boolean paramBoolean)
/*      */   {
/* 3593 */     if (this.registeredFontFiles.contains(paramString))
/*      */       return;
/*      */     int i;
/* 3597 */     if (ttFilter.accept(null, paramString))
/* 3598 */       i = 0;
/* 3599 */     else if (t1Filter.accept(null, paramString))
/* 3600 */       i = 1;
/*      */     else {
/* 3602 */       i = 5;
/*      */     }
/* 3604 */     this.registeredFontFiles.add(paramString);
/* 3605 */     if (paramBoolean) {
/* 3606 */       registerDeferredFont(paramString, paramString, paramArrayOfString, i, false, paramInt);
/*      */     }
/*      */     else
/* 3609 */       registerFontFile(paramString, paramArrayOfString, i, false, paramInt);
/*      */   }
/*      */ 
/*      */   protected void registerPlatformFontsUsedByFontConfiguration()
/*      */   {
/*      */   }
/*      */ 
/*      */   protected void addToMissingFontFileList(String paramString)
/*      */   {
/* 3632 */     if (missingFontFiles == null) {
/* 3633 */       missingFontFiles = new HashSet();
/*      */     }
/* 3635 */     missingFontFiles.add(paramString);
/*      */   }
/*      */ 
/*      */   private boolean isNameForRegisteredFile(String paramString)
/*      */   {
/* 3657 */     String str = getFileNameForFontName(paramString);
/* 3658 */     if (str == null) {
/* 3659 */       return false;
/*      */     }
/* 3661 */     return this.registeredFontFiles.contains(str);
/*      */   }
/*      */ 
/*      */   public void createCompositeFonts(ConcurrentHashMap<String, Font2D> paramConcurrentHashMap, boolean paramBoolean1, boolean paramBoolean2)
/*      */   {
/* 3674 */     FontConfiguration localFontConfiguration = createFontConfiguration(paramBoolean1, paramBoolean2);
/*      */ 
/* 3676 */     initCompositeFonts(localFontConfiguration, paramConcurrentHashMap);
/*      */   }
/*      */ 
/*      */   public Font[] getAllInstalledFonts()
/*      */   {
/* 3683 */     if (this.allFonts == null) {
/* 3684 */       loadFonts();
/* 3685 */       localObject1 = new TreeMap();
/*      */ 
/* 3690 */       Font2D[] arrayOfFont2D = getRegisteredFonts();
/* 3691 */       for (int i = 0; i < arrayOfFont2D.length; i++) {
/* 3692 */         if (!(arrayOfFont2D[i] instanceof NativeFont)) {
/* 3693 */           ((TreeMap)localObject1).put(arrayOfFont2D[i].getFontName(null), arrayOfFont2D[i]);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 3698 */       String[] arrayOfString1 = getFontNamesFromPlatform();
/* 3699 */       if (arrayOfString1 != null) {
/* 3700 */         for (int j = 0; j < arrayOfString1.length; j++) {
/* 3701 */           if (!isNameForRegisteredFile(arrayOfString1[j])) {
/* 3702 */             ((TreeMap)localObject1).put(arrayOfString1[j], null);
/*      */           }
/*      */         }
/*      */       }
/*      */ 
/* 3707 */       String[] arrayOfString2 = null;
/* 3708 */       if (((TreeMap)localObject1).size() > 0) {
/* 3709 */         arrayOfString2 = new String[((TreeMap)localObject1).size()];
/* 3710 */         localObject2 = ((TreeMap)localObject1).keySet().toArray();
/* 3711 */         for (k = 0; k < localObject2.length; k++) {
/* 3712 */           arrayOfString2[k] = ((String)localObject2[k]);
/*      */         }
/*      */       }
/* 3715 */       Object localObject2 = new Font[arrayOfString2.length];
/* 3716 */       for (int k = 0; k < arrayOfString2.length; k++) {
/* 3717 */         localObject2[k] = new Font(arrayOfString2[k], 0, 1);
/* 3718 */         Font2D localFont2D = (Font2D)((TreeMap)localObject1).get(arrayOfString2[k]);
/* 3719 */         if (localFont2D != null) {
/* 3720 */           FontAccess.getFontAccess().setFont2D(localObject2[k], localFont2D.handle);
/*      */         }
/*      */       }
/* 3723 */       this.allFonts = ((Font[])localObject2);
/*      */     }
/*      */ 
/* 3726 */     Object localObject1 = new Font[this.allFonts.length];
/* 3727 */     System.arraycopy(this.allFonts, 0, localObject1, 0, this.allFonts.length);
/* 3728 */     return localObject1;
/*      */   }
/*      */ 
/*      */   public String[] getInstalledFontFamilyNames(Locale paramLocale)
/*      */   {
/* 3740 */     if (paramLocale == null) {
/* 3741 */       paramLocale = Locale.getDefault();
/*      */     }
/* 3743 */     if ((this.allFamilies != null) && (this.lastDefaultLocale != null) && (paramLocale.equals(this.lastDefaultLocale)))
/*      */     {
/* 3745 */       localObject1 = new String[this.allFamilies.length];
/* 3746 */       System.arraycopy(this.allFamilies, 0, localObject1, 0, this.allFamilies.length);
/*      */ 
/* 3748 */       return localObject1;
/*      */     }
/*      */ 
/* 3751 */     Object localObject1 = new TreeMap();
/*      */ 
/* 3754 */     String str1 = "Serif"; ((TreeMap)localObject1).put(str1.toLowerCase(), str1);
/* 3755 */     str1 = "SansSerif"; ((TreeMap)localObject1).put(str1.toLowerCase(), str1);
/* 3756 */     str1 = "Monospaced"; ((TreeMap)localObject1).put(str1.toLowerCase(), str1);
/* 3757 */     str1 = "Dialog"; ((TreeMap)localObject1).put(str1.toLowerCase(), str1);
/* 3758 */     str1 = "DialogInput"; ((TreeMap)localObject1).put(str1.toLowerCase(), str1);
/*      */ 
/* 3764 */     if ((paramLocale.equals(getSystemStartupLocale())) && (getFamilyNamesFromPlatform((TreeMap)localObject1, paramLocale)))
/*      */     {
/* 3767 */       getJREFontFamilyNames((TreeMap)localObject1, paramLocale);
/*      */     } else {
/* 3769 */       loadFontFiles();
/* 3770 */       localObject2 = getPhysicalFonts();
/* 3771 */       for (int i = 0; i < localObject2.length; i++) {
/* 3772 */         if (!(localObject2[i] instanceof NativeFont)) {
/* 3773 */           String str2 = localObject2[i].getFamilyName(paramLocale);
/*      */ 
/* 3775 */           ((TreeMap)localObject1).put(str2.toLowerCase(paramLocale), str2);
/*      */         }
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 3781 */     addNativeFontFamilyNames((TreeMap)localObject1, paramLocale);
/*      */ 
/* 3783 */     Object localObject2 = new String[((TreeMap)localObject1).size()];
/* 3784 */     Object[] arrayOfObject = ((TreeMap)localObject1).keySet().toArray();
/* 3785 */     for (int j = 0; j < arrayOfObject.length; j++) {
/* 3786 */       localObject2[j] = ((String)((TreeMap)localObject1).get(arrayOfObject[j]));
/*      */     }
/* 3788 */     if (paramLocale.equals(Locale.getDefault())) {
/* 3789 */       this.lastDefaultLocale = paramLocale;
/* 3790 */       this.allFamilies = new String[localObject2.length];
/* 3791 */       System.arraycopy(localObject2, 0, this.allFamilies, 0, this.allFamilies.length);
/*      */     }
/* 3793 */     return localObject2;
/*      */   }
/*      */ 
/*      */   protected void addNativeFontFamilyNames(TreeMap<String, String> paramTreeMap, Locale paramLocale) {
/*      */   }
/*      */ 
/*      */   public void register1dot0Fonts() {
/* 3800 */     AccessController.doPrivileged(new PrivilegedAction()
/*      */     {
/*      */       public Object run() {
/* 3803 */         String str = "/usr/openwin/lib/X11/fonts/Type1";
/* 3804 */         SunFontManager.this.registerFontsInDir(str, true, 4, false, false);
/*      */ 
/* 3806 */         return null;
/*      */       }
/*      */     });
/*      */   }
/*      */ 
/*      */   protected void getJREFontFamilyNames(TreeMap<String, String> paramTreeMap, Locale paramLocale)
/*      */   {
/* 3817 */     registerDeferredJREFonts(jreFontDirName);
/* 3818 */     PhysicalFont[] arrayOfPhysicalFont = getPhysicalFonts();
/* 3819 */     for (int i = 0; i < arrayOfPhysicalFont.length; i++)
/* 3820 */       if (!(arrayOfPhysicalFont[i] instanceof NativeFont)) {
/* 3821 */         String str = arrayOfPhysicalFont[i].getFamilyName(paramLocale);
/*      */ 
/* 3823 */         paramTreeMap.put(str.toLowerCase(paramLocale), str);
/*      */       }
/*      */   }
/*      */ 
/*      */   private static Locale getSystemStartupLocale()
/*      */   {
/* 3840 */     if (systemLocale == null) {
/* 3841 */       systemLocale = (Locale)AccessController.doPrivileged(new PrivilegedAction()
/*      */       {
/*      */         public Object run()
/*      */         {
/* 3855 */           String str1 = System.getProperty("file.encoding", "");
/* 3856 */           String str2 = System.getProperty("sun.jnu.encoding");
/* 3857 */           if ((str2 != null) && (!str2.equals(str1))) {
/* 3858 */             return Locale.ROOT;
/*      */           }
/*      */ 
/* 3861 */           String str3 = System.getProperty("user.language", "en");
/* 3862 */           String str4 = System.getProperty("user.country", "");
/* 3863 */           String str5 = System.getProperty("user.variant", "");
/* 3864 */           return new Locale(str3, str4, str5);
/*      */         }
/*      */       });
/*      */     }
/* 3868 */     return systemLocale;
/*      */   }
/*      */ 
/*      */   void addToPool(FileFont paramFileFont)
/*      */   {
/* 3873 */     FileFont localFileFont = null;
/* 3874 */     int i = -1;
/*      */ 
/* 3876 */     synchronized (this.fontFileCache)
/*      */     {
/* 3884 */       for (int j = 0; j < 20; j++) {
/* 3885 */         if (this.fontFileCache[j] == paramFileFont) {
/* 3886 */           return;
/*      */         }
/* 3888 */         if ((this.fontFileCache[j] == null) && (i < 0)) {
/* 3889 */           i = j;
/*      */         }
/*      */       }
/* 3892 */       if (i >= 0) {
/* 3893 */         this.fontFileCache[i] = paramFileFont;
/* 3894 */         return;
/*      */       }
/*      */ 
/* 3897 */       localFileFont = this.fontFileCache[this.lastPoolIndex];
/* 3898 */       this.fontFileCache[this.lastPoolIndex] = paramFileFont;
/*      */ 
/* 3902 */       this.lastPoolIndex = ((this.lastPoolIndex + 1) % 20);
/*      */     }
/*      */ 
/* 3915 */     if (localFileFont != null)
/* 3916 */       localFileFont.close();
/*      */   }
/*      */ 
/*      */   protected FontUIResource getFontConfigFUIR(String paramString, int paramInt1, int paramInt2)
/*      */   {
/* 3923 */     return new FontUIResource(paramString, paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   static
/*      */   {
/*  335 */     AccessController.doPrivileged(new PrivilegedAction()
/*      */     {
/*      */       public Object run()
/*      */       {
/*  339 */         FontManagerNativeLibrary.load();
/*      */ 
/*  343 */         SunFontManager.access$200();
/*      */ 
/*  345 */         switch (StrikeCache.nativeAddressSize) { case 8:
/*  346 */           SunFontManager.longAddresses = true; break;
/*      */         case 4:
/*  347 */           SunFontManager.longAddresses = false; break;
/*      */         default:
/*  348 */           throw new RuntimeException("Unexpected address size");
/*      */         }
/*      */ 
/*  351 */         SunFontManager.noType1Font = "true".equals(System.getProperty("sun.java2d.noType1Font"));
/*      */ 
/*  353 */         SunFontManager.jreLibDirName = System.getProperty("java.home", "") + File.separator + "lib";
/*      */ 
/*  355 */         SunFontManager.jreFontDirName = SunFontManager.jreLibDirName + File.separator + "fonts";
/*  356 */         File localFile = new File(SunFontManager.jreFontDirName + File.separator + "LucidaSansRegular.ttf");
/*      */ 
/*  359 */         return null;
/*      */       }
/*      */     });
/*      */   }
/*      */ 
/*      */   public static class FamilyDescription
/*      */   {
/*      */     public String familyName;
/*      */     public String plainFullName;
/*      */     public String boldFullName;
/*      */     public String italicFullName;
/*      */     public String boldItalicFullName;
/*      */     public String plainFileName;
/*      */     public String boldFileName;
/*      */     public String italicFileName;
/*      */     public String boldItalicFileName;
/*      */   }
/*      */ 
/*      */   private static final class FontRegistrationInfo
/*      */   {
/*      */     String fontFilePath;
/*      */     String[] nativeNames;
/*      */     int fontFormat;
/*      */     boolean javaRasterizer;
/*      */     int fontRank;
/*      */ 
/*      */     FontRegistrationInfo(String paramString, String[] paramArrayOfString, int paramInt1, boolean paramBoolean, int paramInt2)
/*      */     {
/*  905 */       this.fontFilePath = paramString;
/*  906 */       this.nativeNames = paramArrayOfString;
/*  907 */       this.fontFormat = paramInt1;
/*  908 */       this.javaRasterizer = paramBoolean;
/*  909 */       this.fontRank = paramInt2;
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class T1Filter
/*      */     implements FilenameFilter
/*      */   {
/*      */     public boolean accept(File paramFile, String paramString)
/*      */     {
/*   86 */       if (SunFontManager.noType1Font) {
/*   87 */         return false;
/*      */       }
/*      */ 
/*   90 */       int i = paramString.length() - 4;
/*   91 */       if (i <= 0) {
/*   92 */         return false;
/*      */       }
/*   94 */       return (paramString.startsWith(".pfa", i)) || (paramString.startsWith(".pfb", i)) || (paramString.startsWith(".PFA", i)) || (paramString.startsWith(".PFB", i));
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class TTFilter
/*      */     implements FilenameFilter
/*      */   {
/*      */     public boolean accept(File paramFile, String paramString)
/*      */     {
/*   70 */       int i = paramString.length() - 4;
/*   71 */       if (i <= 0) {
/*   72 */         return false;
/*      */       }
/*   74 */       return (paramString.startsWith(".ttf", i)) || (paramString.startsWith(".TTF", i)) || (paramString.startsWith(".ttc", i)) || (paramString.startsWith(".TTC", i)) || (paramString.startsWith(".otf", i)) || (paramString.startsWith(".OTF", i));
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class TTorT1Filter
/*      */     implements FilenameFilter
/*      */   {
/*      */     public boolean accept(File paramFile, String paramString)
/*      */     {
/*  106 */       int i = paramString.length() - 4;
/*  107 */       if (i <= 0) {
/*  108 */         return false;
/*      */       }
/*  110 */       int j = (paramString.startsWith(".ttf", i)) || (paramString.startsWith(".TTF", i)) || (paramString.startsWith(".ttc", i)) || (paramString.startsWith(".TTC", i)) || (paramString.startsWith(".otf", i)) || (paramString.startsWith(".OTF", i)) ? 1 : 0;
/*      */ 
/*  117 */       if (j != 0)
/*  118 */         return true;
/*  119 */       if (SunFontManager.noType1Font) {
/*  120 */         return false;
/*      */       }
/*  122 */       return (paramString.startsWith(".pfa", i)) || (paramString.startsWith(".pfb", i)) || (paramString.startsWith(".PFA", i)) || (paramString.startsWith(".PFB", i));
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.font.SunFontManager
 * JD-Core Version:    0.6.2
 */