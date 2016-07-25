/*     */ package sun.awt.windows;
/*     */ 
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.HashMap;
/*     */ import java.util.Hashtable;
/*     */ import sun.awt.FontConfiguration;
/*     */ import sun.awt.FontDescriptor;
/*     */ import sun.font.SunFontManager;
/*     */ 
/*     */ public class WFontConfiguration extends FontConfiguration
/*     */ {
/*     */   private boolean useCompatibilityFallbacks;
/* 171 */   private static HashMap subsetCharsetMap = new HashMap();
/* 172 */   private static HashMap subsetEncodingMap = new HashMap();
/*     */   private static String textInputCharset;
/*     */ 
/*     */   public WFontConfiguration(SunFontManager paramSunFontManager)
/*     */   {
/*  43 */     super(paramSunFontManager);
/*  44 */     this.useCompatibilityFallbacks = "windows-1252".equals(encoding);
/*  45 */     initTables(encoding);
/*     */   }
/*     */ 
/*     */   public WFontConfiguration(SunFontManager paramSunFontManager, boolean paramBoolean1, boolean paramBoolean2)
/*     */   {
/*  51 */     super(paramSunFontManager, paramBoolean1, paramBoolean2);
/*  52 */     this.useCompatibilityFallbacks = "windows-1252".equals(encoding);
/*     */   }
/*     */ 
/*     */   protected void initReorderMap() {
/*  56 */     if (encoding.equalsIgnoreCase("windows-31j")) {
/*  57 */       localeMap = new Hashtable();
/*     */ 
/*  66 */       localeMap.put("dialoginput.plain.japanese", "MS Mincho");
/*  67 */       localeMap.put("dialoginput.bold.japanese", "MS Mincho");
/*  68 */       localeMap.put("dialoginput.italic.japanese", "MS Mincho");
/*  69 */       localeMap.put("dialoginput.bolditalic.japanese", "MS Mincho");
/*     */     }
/*  71 */     this.reorderMap = new HashMap();
/*  72 */     this.reorderMap.put("UTF-8.hi", "devanagari");
/*  73 */     this.reorderMap.put("windows-1255", "hebrew");
/*  74 */     this.reorderMap.put("x-windows-874", "thai");
/*  75 */     this.reorderMap.put("windows-31j", "japanese");
/*  76 */     this.reorderMap.put("x-windows-949", "korean");
/*  77 */     this.reorderMap.put("GBK", "chinese-ms936");
/*  78 */     this.reorderMap.put("GB18030", "chinese-gb18030");
/*  79 */     this.reorderMap.put("x-windows-950", "chinese-ms950");
/*  80 */     this.reorderMap.put("x-MS950-HKSCS", split("chinese-ms950,chinese-hkscs"));
/*     */   }
/*     */ 
/*     */   protected void setOsNameAndVersion()
/*     */   {
/*  85 */     super.setOsNameAndVersion();
/*  86 */     if (osName.startsWith("Windows"))
/*     */     {
/*  88 */       int i = osName.indexOf(' ');
/*  89 */       if (i == -1) {
/*  90 */         osName = null;
/*     */       }
/*     */       else {
/*  93 */         int j = osName.indexOf(' ', i + 1);
/*  94 */         if (j == -1) {
/*  95 */           osName = osName.substring(i + 1);
/*     */         }
/*     */         else {
/*  98 */           osName = osName.substring(i + 1, j);
/*     */         }
/*     */       }
/* 101 */       osVersion = null;
/*     */     }
/*     */   }
/*     */ 
/*     */   public String getFallbackFamilyName(String paramString1, String paramString2)
/*     */   {
/* 109 */     if (this.useCompatibilityFallbacks) {
/* 110 */       String str = getCompatibilityFamilyName(paramString1);
/* 111 */       if (str != null) {
/* 112 */         return str;
/*     */       }
/*     */     }
/* 115 */     return paramString2;
/*     */   }
/*     */ 
/*     */   protected String makeAWTFontName(String paramString1, String paramString2) {
/* 119 */     String str = (String)subsetCharsetMap.get(paramString2);
/* 120 */     if (str == null) {
/* 121 */       str = "DEFAULT_CHARSET";
/*     */     }
/* 123 */     return paramString1 + "," + str;
/*     */   }
/*     */ 
/*     */   protected String getEncoding(String paramString1, String paramString2) {
/* 127 */     String str = (String)subsetEncodingMap.get(paramString2);
/* 128 */     if (str == null) {
/* 129 */       str = "default";
/*     */     }
/* 131 */     return str;
/*     */   }
/*     */ 
/*     */   protected Charset getDefaultFontCharset(String paramString) {
/* 135 */     return new WDefaultFontCharset(paramString);
/*     */   }
/*     */ 
/*     */   public String getFaceNameFromComponentFontName(String paramString)
/*     */   {
/* 140 */     return paramString;
/*     */   }
/*     */ 
/*     */   protected String getFileNameFromComponentFontName(String paramString) {
/* 144 */     return getFileNameFromPlatformName(paramString);
/*     */   }
/*     */ 
/*     */   public String getTextComponentFontName(String paramString, int paramInt)
/*     */   {
/* 152 */     FontDescriptor[] arrayOfFontDescriptor = getFontDescriptors(paramString, paramInt);
/* 153 */     String str = findFontWithCharset(arrayOfFontDescriptor, textInputCharset);
/* 154 */     if (str == null) {
/* 155 */       str = findFontWithCharset(arrayOfFontDescriptor, "DEFAULT_CHARSET");
/*     */     }
/* 157 */     return str;
/*     */   }
/*     */ 
/*     */   private String findFontWithCharset(FontDescriptor[] paramArrayOfFontDescriptor, String paramString) {
/* 161 */     Object localObject = null;
/* 162 */     for (int i = 0; i < paramArrayOfFontDescriptor.length; i++) {
/* 163 */       String str = paramArrayOfFontDescriptor[i].getNativeName();
/* 164 */       if (str.endsWith(paramString)) {
/* 165 */         localObject = str;
/*     */       }
/*     */     }
/* 168 */     return localObject;
/*     */   }
/*     */ 
/*     */   private void initTables(String paramString)
/*     */   {
/* 176 */     subsetCharsetMap.put("alphabetic", "ANSI_CHARSET");
/* 177 */     subsetCharsetMap.put("alphabetic/1252", "ANSI_CHARSET");
/* 178 */     subsetCharsetMap.put("alphabetic/default", "DEFAULT_CHARSET");
/* 179 */     subsetCharsetMap.put("arabic", "ARABIC_CHARSET");
/* 180 */     subsetCharsetMap.put("chinese-ms936", "GB2312_CHARSET");
/* 181 */     subsetCharsetMap.put("chinese-gb18030", "GB2312_CHARSET");
/* 182 */     subsetCharsetMap.put("chinese-ms950", "CHINESEBIG5_CHARSET");
/* 183 */     subsetCharsetMap.put("chinese-hkscs", "CHINESEBIG5_CHARSET");
/* 184 */     subsetCharsetMap.put("cyrillic", "RUSSIAN_CHARSET");
/* 185 */     subsetCharsetMap.put("devanagari", "DEFAULT_CHARSET");
/* 186 */     subsetCharsetMap.put("dingbats", "SYMBOL_CHARSET");
/* 187 */     subsetCharsetMap.put("greek", "GREEK_CHARSET");
/* 188 */     subsetCharsetMap.put("hebrew", "HEBREW_CHARSET");
/* 189 */     subsetCharsetMap.put("japanese", "SHIFTJIS_CHARSET");
/* 190 */     subsetCharsetMap.put("korean", "HANGEUL_CHARSET");
/* 191 */     subsetCharsetMap.put("latin", "ANSI_CHARSET");
/* 192 */     subsetCharsetMap.put("symbol", "SYMBOL_CHARSET");
/* 193 */     subsetCharsetMap.put("thai", "THAI_CHARSET");
/*     */ 
/* 195 */     subsetEncodingMap.put("alphabetic", "default");
/* 196 */     subsetEncodingMap.put("alphabetic/1252", "windows-1252");
/* 197 */     subsetEncodingMap.put("alphabetic/default", paramString);
/* 198 */     subsetEncodingMap.put("arabic", "windows-1256");
/* 199 */     subsetEncodingMap.put("chinese-ms936", "GBK");
/* 200 */     subsetEncodingMap.put("chinese-gb18030", "GB18030");
/* 201 */     if ("x-MS950-HKSCS".equals(paramString))
/* 202 */       subsetEncodingMap.put("chinese-ms950", "x-MS950-HKSCS");
/*     */     else {
/* 204 */       subsetEncodingMap.put("chinese-ms950", "x-windows-950");
/*     */     }
/* 206 */     subsetEncodingMap.put("chinese-hkscs", "sun.awt.HKSCS");
/* 207 */     subsetEncodingMap.put("cyrillic", "windows-1251");
/* 208 */     subsetEncodingMap.put("devanagari", "UTF-16LE");
/* 209 */     subsetEncodingMap.put("dingbats", "sun.awt.windows.WingDings");
/* 210 */     subsetEncodingMap.put("greek", "windows-1253");
/* 211 */     subsetEncodingMap.put("hebrew", "windows-1255");
/* 212 */     subsetEncodingMap.put("japanese", "windows-31j");
/* 213 */     subsetEncodingMap.put("korean", "x-windows-949");
/* 214 */     subsetEncodingMap.put("latin", "windows-1252");
/* 215 */     subsetEncodingMap.put("symbol", "sun.awt.Symbol");
/* 216 */     subsetEncodingMap.put("thai", "x-windows-874");
/*     */ 
/* 218 */     if ("windows-1256".equals(paramString))
/* 219 */       textInputCharset = "ARABIC_CHARSET";
/* 220 */     else if ("GBK".equals(paramString))
/* 221 */       textInputCharset = "GB2312_CHARSET";
/* 222 */     else if ("GB18030".equals(paramString))
/* 223 */       textInputCharset = "GB2312_CHARSET";
/* 224 */     else if ("x-windows-950".equals(paramString))
/* 225 */       textInputCharset = "CHINESEBIG5_CHARSET";
/* 226 */     else if ("x-MS950-HKSCS".equals(paramString))
/* 227 */       textInputCharset = "CHINESEBIG5_CHARSET";
/* 228 */     else if ("windows-1251".equals(paramString))
/* 229 */       textInputCharset = "RUSSIAN_CHARSET";
/* 230 */     else if ("UTF-8".equals(paramString))
/* 231 */       textInputCharset = "DEFAULT_CHARSET";
/* 232 */     else if ("windows-1253".equals(paramString))
/* 233 */       textInputCharset = "GREEK_CHARSET";
/* 234 */     else if ("windows-1255".equals(paramString))
/* 235 */       textInputCharset = "HEBREW_CHARSET";
/* 236 */     else if ("windows-31j".equals(paramString))
/* 237 */       textInputCharset = "SHIFTJIS_CHARSET";
/* 238 */     else if ("x-windows-949".equals(paramString))
/* 239 */       textInputCharset = "HANGEUL_CHARSET";
/* 240 */     else if ("x-windows-874".equals(paramString))
/* 241 */       textInputCharset = "THAI_CHARSET";
/*     */     else
/* 243 */       textInputCharset = "DEFAULT_CHARSET";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.windows.WFontConfiguration
 * JD-Core Version:    0.6.2
 */