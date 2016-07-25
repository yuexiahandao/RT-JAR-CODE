/*     */ package sun.font;
/*     */ 
/*     */ import java.awt.Font;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.lang.ref.SoftReference;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import javax.swing.plaf.FontUIResource;
/*     */ import sun.util.logging.PlatformLogger;
/*     */ 
/*     */ public final class FontUtilities
/*     */ {
/*     */   public static boolean isSolaris;
/*     */   public static boolean isLinux;
/*     */   public static boolean isMacOSX;
/*     */   public static boolean isSolaris8;
/*     */   public static boolean isSolaris9;
/*     */   public static boolean isOpenSolaris;
/*     */   public static boolean useT2K;
/*     */   public static boolean isWindows;
/*     */   public static boolean isOpenJDK;
/*     */   static final String LUCIDA_FILE_NAME = "LucidaSansRegular.ttf";
/*  67 */   private static boolean debugFonts = false;
/*  68 */   private static PlatformLogger logger = null;
/*     */   private static boolean logging;
/*     */   public static final int MIN_LAYOUT_CHARCODE = 768;
/*     */   public static final int MAX_LAYOUT_CHARCODE = 8303;
/* 394 */   private static volatile SoftReference<ConcurrentHashMap<PhysicalFont, CompositeFont>> compMapRef = new SoftReference(null);
/*     */ 
/* 442 */   private static final String[][] nameMap = { { "sans", "sansserif" }, { "sans-serif", "sansserif" }, { "serif", "serif" }, { "monospace", "monospaced" } };
/*     */ 
/*     */   public static Font2D getFont2D(Font paramFont)
/*     */   {
/* 180 */     return FontAccess.getFontAccess().getFont2D(paramFont);
/*     */   }
/*     */ 
/*     */   public static boolean isComplexText(char[] paramArrayOfChar, int paramInt1, int paramInt2)
/*     */   {
/* 202 */     for (int i = paramInt1; i < paramInt2; i++) {
/* 203 */       if (paramArrayOfChar[i] >= '̀')
/*     */       {
/* 206 */         if (isNonSimpleChar(paramArrayOfChar[i]))
/* 207 */           return true;
/*     */       }
/*     */     }
/* 210 */     return false;
/*     */   }
/*     */ 
/*     */   public static boolean isNonSimpleChar(char paramChar)
/*     */   {
/* 225 */     return (isComplexCharCode(paramChar)) || ((paramChar >= 55296) && (paramChar <= 57343));
/*     */   }
/*     */ 
/*     */   public static boolean isComplexCharCode(int paramInt)
/*     */   {
/* 251 */     if ((paramInt < 768) || (paramInt > 8303)) {
/* 252 */       return false;
/*     */     }
/* 254 */     if (paramInt <= 879)
/*     */     {
/* 256 */       return true;
/*     */     }
/* 258 */     if (paramInt < 1424)
/*     */     {
/* 260 */       return false;
/*     */     }
/* 262 */     if (paramInt <= 1791)
/*     */     {
/* 265 */       return true;
/*     */     }
/* 267 */     if (paramInt < 2304) {
/* 268 */       return false;
/*     */     }
/* 270 */     if (paramInt <= 3711)
/*     */     {
/* 283 */       return true;
/*     */     }
/* 285 */     if (paramInt < 3840) {
/* 286 */       return false;
/*     */     }
/* 288 */     if (paramInt <= 4095) {
/* 289 */       return true;
/*     */     }
/* 291 */     if (paramInt < 4352) {
/* 292 */       return false;
/*     */     }
/* 294 */     if (paramInt < 4607) {
/* 295 */       return true;
/*     */     }
/* 297 */     if (paramInt < 6016) {
/* 298 */       return false;
/*     */     }
/* 300 */     if (paramInt <= 6143) {
/* 301 */       return true;
/*     */     }
/* 303 */     if (paramInt < 8204) {
/* 304 */       return false;
/*     */     }
/* 306 */     if (paramInt <= 8205) {
/* 307 */       return true;
/*     */     }
/* 309 */     if ((paramInt >= 8234) && (paramInt <= 8238)) {
/* 310 */       return true;
/*     */     }
/* 312 */     if ((paramInt >= 8298) && (paramInt <= 8303)) {
/* 313 */       return true;
/*     */     }
/* 315 */     return false;
/*     */   }
/*     */ 
/*     */   public static PlatformLogger getLogger() {
/* 319 */     return logger;
/*     */   }
/*     */ 
/*     */   public static boolean isLogging() {
/* 323 */     return logging;
/*     */   }
/*     */ 
/*     */   public static boolean debugFonts() {
/* 327 */     return debugFonts;
/*     */   }
/*     */ 
/*     */   public static boolean fontSupportsDefaultEncoding(Font paramFont)
/*     */   {
/* 338 */     return getFont2D(paramFont) instanceof CompositeFont;
/*     */   }
/*     */ 
/*     */   public static FontUIResource getCompositeFontUIResource(Font paramFont)
/*     */   {
/* 398 */     FontUIResource localFontUIResource = new FontUIResource(paramFont);
/* 399 */     Font2D localFont2D1 = getFont2D(paramFont);
/*     */ 
/* 401 */     if (!(localFont2D1 instanceof PhysicalFont))
/*     */     {
/* 410 */       return localFontUIResource;
/*     */     }
/*     */ 
/* 413 */     FontManager localFontManager = FontManagerFactory.getInstance();
/* 414 */     Font2D localFont2D2 = localFontManager.findFont2D("dialog", paramFont.getStyle(), 0);
/*     */ 
/* 416 */     if ((localFont2D2 == null) || (!(localFont2D2 instanceof CompositeFont))) {
/* 417 */       return localFontUIResource;
/*     */     }
/* 419 */     CompositeFont localCompositeFont1 = (CompositeFont)localFont2D2;
/* 420 */     PhysicalFont localPhysicalFont = (PhysicalFont)localFont2D1;
/* 421 */     ConcurrentHashMap localConcurrentHashMap = (ConcurrentHashMap)compMapRef.get();
/* 422 */     if (localConcurrentHashMap == null) {
/* 423 */       localConcurrentHashMap = new ConcurrentHashMap();
/* 424 */       compMapRef = new SoftReference(localConcurrentHashMap);
/*     */     }
/* 426 */     CompositeFont localCompositeFont2 = (CompositeFont)localConcurrentHashMap.get(localPhysicalFont);
/* 427 */     if (localCompositeFont2 == null) {
/* 428 */       localCompositeFont2 = new CompositeFont(localPhysicalFont, localCompositeFont1);
/* 429 */       localConcurrentHashMap.put(localPhysicalFont, localCompositeFont2);
/*     */     }
/* 431 */     FontAccess.getFontAccess().setFont2D(localFontUIResource, localCompositeFont2.handle);
/*     */ 
/* 435 */     FontAccess.getFontAccess().setCreatedFont(localFontUIResource);
/* 436 */     return localFontUIResource;
/*     */   }
/*     */ 
/*     */   public static String mapFcName(String paramString)
/*     */   {
/* 450 */     for (int i = 0; i < nameMap.length; i++) {
/* 451 */       if (paramString.equals(nameMap[i][0])) {
/* 452 */         return nameMap[i][1];
/*     */       }
/*     */     }
/* 455 */     return null;
/*     */   }
/*     */ 
/*     */   public static FontUIResource getFontConfigFUIR(String paramString, int paramInt1, int paramInt2)
/*     */   {
/* 479 */     String str = mapFcName(paramString);
/* 480 */     if (str == null) {
/* 481 */       str = "sansserif";
/*     */     }
/*     */ 
/* 485 */     FontManager localFontManager = FontManagerFactory.getInstance();
/*     */     FontUIResource localFontUIResource;
/* 486 */     if ((localFontManager instanceof SunFontManager)) {
/* 487 */       SunFontManager localSunFontManager = (SunFontManager)localFontManager;
/* 488 */       localFontUIResource = localSunFontManager.getFontConfigFUIR(str, paramInt1, paramInt2);
/*     */     } else {
/* 490 */       localFontUIResource = new FontUIResource(str, paramInt1, paramInt2);
/*     */     }
/* 492 */     return localFontUIResource;
/*     */   }
/*     */ 
/*     */   public static boolean textLayoutIsCompatible(Font paramFont)
/*     */   {
/* 505 */     Font2D localFont2D = getFont2D(paramFont);
/* 506 */     if ((localFont2D instanceof TrueTypeFont)) {
/* 507 */       TrueTypeFont localTrueTypeFont = (TrueTypeFont)localFont2D;
/* 508 */       return (localTrueTypeFont.getDirectoryEntry(1196643650) == null) || (localTrueTypeFont.getDirectoryEntry(1196445523) != null);
/*     */     }
/*     */ 
/* 512 */     return false;
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  74 */     AccessController.doPrivileged(new PrivilegedAction() {
/*     */       public Object run() {
/*  76 */         String str1 = System.getProperty("os.name", "unknownOS");
/*  77 */         FontUtilities.isSolaris = str1.startsWith("SunOS");
/*     */ 
/*  79 */         FontUtilities.isLinux = str1.startsWith("Linux");
/*     */ 
/*  81 */         FontUtilities.isMacOSX = str1.contains("OS X");
/*     */ 
/*  83 */         String str2 = System.getProperty("sun.java2d.font.scaler");
/*  84 */         if (str2 != null)
/*  85 */           FontUtilities.useT2K = "t2k".equals(str2);
/*     */         else {
/*  87 */           FontUtilities.useT2K = false;
/*     */         }
/*  89 */         if (FontUtilities.isSolaris) {
/*  90 */           str3 = System.getProperty("os.version", "0.0");
/*  91 */           FontUtilities.isSolaris8 = str3.startsWith("5.8");
/*  92 */           FontUtilities.isSolaris9 = str3.startsWith("5.9");
/*  93 */           float f = Float.parseFloat(str3);
/*  94 */           if (f > 5.1F) {
/*  95 */             localFile = new File("/etc/release");
/*  96 */             str5 = null;
/*     */             try {
/*  98 */               FileInputStream localFileInputStream = new FileInputStream(localFile);
/*  99 */               InputStreamReader localInputStreamReader = new InputStreamReader(localFileInputStream, "ISO-8859-1");
/*     */ 
/* 101 */               BufferedReader localBufferedReader = new BufferedReader(localInputStreamReader);
/* 102 */               str5 = localBufferedReader.readLine();
/* 103 */               localFileInputStream.close();
/*     */             }
/*     */             catch (Exception localException) {
/*     */             }
/* 107 */             if ((str5 != null) && (str5.indexOf("OpenSolaris") >= 0))
/* 108 */               FontUtilities.isOpenSolaris = true;
/*     */             else
/* 110 */               FontUtilities.isOpenSolaris = false;
/*     */           }
/*     */           else {
/* 113 */             FontUtilities.isOpenSolaris = false;
/*     */           }
/*     */         } else {
/* 116 */           FontUtilities.isSolaris8 = false;
/* 117 */           FontUtilities.isSolaris9 = false;
/* 118 */           FontUtilities.isOpenSolaris = false;
/*     */         }
/* 120 */         FontUtilities.isWindows = str1.startsWith("Windows");
/* 121 */         String str3 = System.getProperty("java.home", "") + File.separator + "lib";
/*     */ 
/* 123 */         String str4 = str3 + File.separator + "fonts";
/*     */ 
/* 125 */         File localFile = new File(str4 + File.separator + "LucidaSansRegular.ttf");
/*     */ 
/* 127 */         FontUtilities.isOpenJDK = !localFile.exists();
/*     */ 
/* 129 */         String str5 = System.getProperty("sun.java2d.debugfonts");
/*     */ 
/* 132 */         if ((str5 != null) && (!str5.equals("false"))) {
/* 133 */           FontUtilities.access$002(true);
/* 134 */           FontUtilities.access$102(PlatformLogger.getLogger("sun.java2d"));
/* 135 */           if (str5.equals("warning"))
/* 136 */             FontUtilities.logger.setLevel(900);
/* 137 */           else if (str5.equals("severe")) {
/* 138 */             FontUtilities.logger.setLevel(1000);
/*     */           }
/*     */         }
/*     */ 
/* 142 */         if (FontUtilities.debugFonts) {
/* 143 */           FontUtilities.access$102(PlatformLogger.getLogger("sun.java2d"));
/* 144 */           FontUtilities.access$202(FontUtilities.logger.isEnabled());
/*     */         }
/*     */ 
/* 147 */         return null;
/*     */       }
/*     */     });
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.font.FontUtilities
 * JD-Core Version:    0.6.2
 */