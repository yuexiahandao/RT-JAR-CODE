/*     */ package sun.awt;
/*     */ 
/*     */ import java.awt.FontFormatException;
/*     */ import java.awt.GraphicsEnvironment;
/*     */ import java.io.File;
/*     */ import java.io.FilenameFilter;
/*     */ import java.io.PrintStream;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Locale;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.StringTokenizer;
/*     */ import sun.awt.windows.WFontConfiguration;
/*     */ import sun.font.SunFontManager;
/*     */ import sun.font.SunFontManager.FamilyDescription;
/*     */ import sun.font.TrueTypeFont;
/*     */ 
/*     */ public class Win32FontManager extends SunFontManager
/*     */ {
/*  53 */   private static String[] defaultPlatformFont = null;
/*     */   private static TrueTypeFont eudcFont;
/* 259 */   static String fontsForPrinting = null;
/*     */ 
/*     */   private static native String getEUDCFontFile();
/*     */ 
/*     */   public TrueTypeFont getEUDCFont()
/*     */   {
/*  88 */     return eudcFont;
/*     */   }
/*     */ 
/*     */   public Win32FontManager()
/*     */   {
/*  93 */     AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public Object run()
/*     */       {
/* 100 */         Win32FontManager.this.registerJREFontsWithPlatform(SunFontManager.jreFontDirName);
/* 101 */         return null;
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   protected boolean useAbsoluteFontFileNames()
/*     */   {
/* 111 */     return false;
/*     */   }
/*     */ 
/*     */   protected void registerFontFile(String paramString, String[] paramArrayOfString, int paramInt, boolean paramBoolean)
/*     */   {
/* 124 */     if (this.registeredFontFiles.contains(paramString)) {
/* 125 */       return;
/*     */     }
/* 127 */     this.registeredFontFiles.add(paramString);
/*     */     int i;
/* 130 */     if (getTrueTypeFilter().accept(null, paramString))
/* 131 */       i = 0;
/* 132 */     else if (getType1Filter().accept(null, paramString)) {
/* 133 */       i = 1;
/*     */     }
/*     */     else {
/* 136 */       return;
/*     */     }
/*     */ 
/* 139 */     if (this.fontPath == null) {
/* 140 */       this.fontPath = getPlatformFontPath(noType1Font);
/*     */     }
/*     */ 
/* 147 */     String str1 = jreFontDirName + File.pathSeparator + this.fontPath;
/* 148 */     StringTokenizer localStringTokenizer = new StringTokenizer(str1, File.pathSeparator);
/*     */ 
/* 151 */     int j = 0;
/*     */     try {
/* 153 */       while ((j == 0) && (localStringTokenizer.hasMoreTokens())) {
/* 154 */         String str2 = localStringTokenizer.nextToken();
/* 155 */         boolean bool = str2.equals(jreFontDirName);
/* 156 */         File localFile = new File(str2, paramString);
/* 157 */         if (localFile.canRead()) {
/* 158 */           j = 1;
/* 159 */           String str3 = localFile.getAbsolutePath();
/* 160 */           if (paramBoolean) {
/* 161 */             registerDeferredFont(paramString, str3, paramArrayOfString, i, bool, paramInt); break;
/*     */           }
/*     */ 
/* 166 */           registerFontFile(str3, paramArrayOfString, i, bool, paramInt);
/*     */ 
/* 170 */           break;
/*     */         }
/*     */       }
/*     */     } catch (NoSuchElementException localNoSuchElementException) {
/* 174 */       System.err.println(localNoSuchElementException);
/*     */     }
/* 176 */     if (j == 0)
/* 177 */       addToMissingFontFileList(paramString);
/*     */   }
/*     */ 
/*     */   protected FontConfiguration createFontConfiguration()
/*     */   {
/* 184 */     WFontConfiguration localWFontConfiguration = new WFontConfiguration(this);
/* 185 */     localWFontConfiguration.init();
/* 186 */     return localWFontConfiguration;
/*     */   }
/*     */ 
/*     */   public FontConfiguration createFontConfiguration(boolean paramBoolean1, boolean paramBoolean2)
/*     */   {
/* 193 */     return new WFontConfiguration(this, paramBoolean1, paramBoolean2);
/*     */   }
/*     */ 
/*     */   protected void populateFontFileNameMap(HashMap<String, String> paramHashMap1, HashMap<String, String> paramHashMap2, HashMap<String, ArrayList<String>> paramHashMap, Locale paramLocale)
/*     */   {
/* 204 */     populateFontFileNameMap0(paramHashMap1, paramHashMap2, paramHashMap, paramLocale);
/*     */   }
/*     */ 
/*     */   private static native void populateFontFileNameMap0(HashMap<String, String> paramHashMap1, HashMap<String, String> paramHashMap2, HashMap<String, ArrayList<String>> paramHashMap, Locale paramLocale);
/*     */ 
/*     */   protected synchronized native String getFontPath(boolean paramBoolean);
/*     */ 
/*     */   public String[] getDefaultPlatformFont()
/*     */   {
/* 220 */     if (defaultPlatformFont != null) {
/* 221 */       return defaultPlatformFont;
/*     */     }
/*     */ 
/* 224 */     String[] arrayOfString1 = new String[2];
/* 225 */     arrayOfString1[0] = "Arial";
/* 226 */     arrayOfString1[1] = "c:\\windows\\fonts";
/* 227 */     final String[] arrayOfString2 = getPlatformFontDirs(true);
/* 228 */     if (arrayOfString2.length > 1) {
/* 229 */       String str = (String)AccessController.doPrivileged(new PrivilegedAction()
/*     */       {
/*     */         public Object run() {
/* 232 */           for (int i = 0; i < arrayOfString2.length; i++) {
/* 233 */             String str = arrayOfString2[i] + File.separator + "arial.ttf";
/*     */ 
/* 235 */             File localFile = new File(str);
/* 236 */             if (localFile.exists()) {
/* 237 */               return arrayOfString2[i];
/*     */             }
/*     */           }
/* 240 */           return null;
/*     */         }
/*     */       });
/* 243 */       if (str != null)
/* 244 */         arrayOfString1[1] = str;
/*     */     }
/*     */     else {
/* 247 */       arrayOfString1[1] = arrayOfString2[0];
/*     */     }
/* 249 */     arrayOfString1[1] = (arrayOfString1[1] + File.separator + "arial.ttf");
/* 250 */     defaultPlatformFont = arrayOfString1;
/* 251 */     return defaultPlatformFont;
/*     */   }
/*     */ 
/*     */   protected void registerJREFontsWithPlatform(String paramString)
/*     */   {
/* 261 */     fontsForPrinting = paramString;
/*     */   }
/*     */ 
/*     */   public static void registerJREFontsForPrinting()
/*     */   {
/*     */     String str;
/* 266 */     synchronized (Win32GraphicsEnvironment.class) {
/* 267 */       GraphicsEnvironment.getLocalGraphicsEnvironment();
/* 268 */       if (fontsForPrinting == null) {
/* 269 */         return;
/*     */       }
/* 271 */       str = fontsForPrinting;
/* 272 */       fontsForPrinting = null;
/*     */     }
/* 274 */     AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public Object run() {
/* 277 */         File localFile1 = new File(this.val$pathName);
/* 278 */         String[] arrayOfString = localFile1.list(SunFontManager.getInstance().getTrueTypeFilter());
/*     */ 
/* 280 */         if (arrayOfString == null) {
/* 281 */           return null;
/*     */         }
/* 283 */         for (int i = 0; i < arrayOfString.length; i++) {
/* 284 */           File localFile2 = new File(localFile1, arrayOfString[i]);
/* 285 */           Win32FontManager.registerFontWithPlatform(localFile2.getAbsolutePath());
/*     */         }
/* 287 */         return null;
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   protected static native void registerFontWithPlatform(String paramString);
/*     */ 
/*     */   protected static native void deRegisterFontWithPlatform(String paramString);
/*     */ 
/*     */   public HashMap<String, SunFontManager.FamilyDescription> populateHardcodedFileNameMap()
/*     */   {
/* 301 */     HashMap localHashMap = new HashMap();
/*     */ 
/* 310 */     SunFontManager.FamilyDescription localFamilyDescription = new SunFontManager.FamilyDescription();
/* 311 */     localFamilyDescription.familyName = "Segoe UI";
/* 312 */     localFamilyDescription.plainFullName = "Segoe UI";
/* 313 */     localFamilyDescription.plainFileName = "segoeui.ttf";
/* 314 */     localFamilyDescription.boldFullName = "Segoe UI Bold";
/* 315 */     localFamilyDescription.boldFileName = "segoeuib.ttf";
/* 316 */     localFamilyDescription.italicFullName = "Segoe UI Italic";
/* 317 */     localFamilyDescription.italicFileName = "segoeuii.ttf";
/* 318 */     localFamilyDescription.boldItalicFullName = "Segoe UI Bold Italic";
/* 319 */     localFamilyDescription.boldItalicFileName = "segoeuiz.ttf";
/* 320 */     localHashMap.put("segoe", localFamilyDescription);
/*     */ 
/* 322 */     localFamilyDescription = new SunFontManager.FamilyDescription();
/* 323 */     localFamilyDescription.familyName = "Tahoma";
/* 324 */     localFamilyDescription.plainFullName = "Tahoma";
/* 325 */     localFamilyDescription.plainFileName = "tahoma.ttf";
/* 326 */     localFamilyDescription.boldFullName = "Tahoma Bold";
/* 327 */     localFamilyDescription.boldFileName = "tahomabd.ttf";
/* 328 */     localHashMap.put("tahoma", localFamilyDescription);
/*     */ 
/* 330 */     localFamilyDescription = new SunFontManager.FamilyDescription();
/* 331 */     localFamilyDescription.familyName = "Verdana";
/* 332 */     localFamilyDescription.plainFullName = "Verdana";
/* 333 */     localFamilyDescription.plainFileName = "verdana.TTF";
/* 334 */     localFamilyDescription.boldFullName = "Verdana Bold";
/* 335 */     localFamilyDescription.boldFileName = "verdanab.TTF";
/* 336 */     localFamilyDescription.italicFullName = "Verdana Italic";
/* 337 */     localFamilyDescription.italicFileName = "verdanai.TTF";
/* 338 */     localFamilyDescription.boldItalicFullName = "Verdana Bold Italic";
/* 339 */     localFamilyDescription.boldItalicFileName = "verdanaz.TTF";
/* 340 */     localHashMap.put("verdana", localFamilyDescription);
/*     */ 
/* 345 */     localFamilyDescription = new SunFontManager.FamilyDescription();
/* 346 */     localFamilyDescription.familyName = "Arial";
/* 347 */     localFamilyDescription.plainFullName = "Arial";
/* 348 */     localFamilyDescription.plainFileName = "ARIAL.TTF";
/* 349 */     localFamilyDescription.boldFullName = "Arial Bold";
/* 350 */     localFamilyDescription.boldFileName = "ARIALBD.TTF";
/* 351 */     localFamilyDescription.italicFullName = "Arial Italic";
/* 352 */     localFamilyDescription.italicFileName = "ARIALI.TTF";
/* 353 */     localFamilyDescription.boldItalicFullName = "Arial Bold Italic";
/* 354 */     localFamilyDescription.boldItalicFileName = "ARIALBI.TTF";
/* 355 */     localHashMap.put("arial", localFamilyDescription);
/*     */ 
/* 357 */     localFamilyDescription = new SunFontManager.FamilyDescription();
/* 358 */     localFamilyDescription.familyName = "Symbol";
/* 359 */     localFamilyDescription.plainFullName = "Symbol";
/* 360 */     localFamilyDescription.plainFileName = "Symbol.TTF";
/* 361 */     localHashMap.put("symbol", localFamilyDescription);
/*     */ 
/* 363 */     localFamilyDescription = new SunFontManager.FamilyDescription();
/* 364 */     localFamilyDescription.familyName = "WingDings";
/* 365 */     localFamilyDescription.plainFullName = "WingDings";
/* 366 */     localFamilyDescription.plainFileName = "WINGDING.TTF";
/* 367 */     localHashMap.put("wingdings", localFamilyDescription);
/*     */ 
/* 369 */     return localHashMap;
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  59 */     AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public Object run() {
/*  62 */         String str = Win32FontManager.access$000();
/*  63 */         if (str != null)
/*     */         {
/*     */           try
/*     */           {
/*  68 */             Win32FontManager.access$102(new TrueTypeFont(str, null, 0, true));
/*     */           }
/*     */           catch (FontFormatException localFontFormatException) {
/*     */           }
/*     */         }
/*  73 */         return null;
/*     */       }
/*     */     });
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.Win32FontManager
 * JD-Core Version:    0.6.2
 */