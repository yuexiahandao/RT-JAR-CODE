/*     */ package sun.font;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Locale;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import sun.util.logging.PlatformLogger;
/*     */ 
/*     */ public class FontFamily
/*     */ {
/*  36 */   private static ConcurrentHashMap<String, FontFamily> familyNameMap = new ConcurrentHashMap();
/*     */   private static HashMap<String, FontFamily> allLocaleNames;
/*     */   protected String familyName;
/*     */   protected Font2D plain;
/*     */   protected Font2D bold;
/*     */   protected Font2D italic;
/*     */   protected Font2D bolditalic;
/*  44 */   protected boolean logicalFont = false;
/*     */   protected int familyRank;
/*     */ 
/*     */   public static FontFamily getFamily(String paramString)
/*     */   {
/*  48 */     return (FontFamily)familyNameMap.get(paramString.toLowerCase(Locale.ENGLISH));
/*     */   }
/*     */ 
/*     */   public static String[] getAllFamilyNames() {
/*  52 */     return null;
/*     */   }
/*     */ 
/*     */   static void remove(Font2D paramFont2D)
/*     */   {
/*  61 */     String str = paramFont2D.getFamilyName(Locale.ENGLISH);
/*  62 */     FontFamily localFontFamily = getFamily(str);
/*  63 */     if (localFontFamily == null) {
/*  64 */       return;
/*     */     }
/*  66 */     if (localFontFamily.plain == paramFont2D) {
/*  67 */       localFontFamily.plain = null;
/*     */     }
/*  69 */     if (localFontFamily.bold == paramFont2D) {
/*  70 */       localFontFamily.bold = null;
/*     */     }
/*  72 */     if (localFontFamily.italic == paramFont2D) {
/*  73 */       localFontFamily.italic = null;
/*     */     }
/*  75 */     if (localFontFamily.bolditalic == paramFont2D) {
/*  76 */       localFontFamily.bolditalic = null;
/*     */     }
/*  78 */     if ((localFontFamily.plain == null) && (localFontFamily.bold == null) && (localFontFamily.plain == null) && (localFontFamily.bold == null))
/*     */     {
/*  80 */       familyNameMap.remove(str);
/*     */     }
/*     */   }
/*     */ 
/*     */   public FontFamily(String paramString, boolean paramBoolean, int paramInt) {
/*  85 */     this.logicalFont = paramBoolean;
/*  86 */     this.familyName = paramString;
/*  87 */     this.familyRank = paramInt;
/*  88 */     familyNameMap.put(paramString.toLowerCase(Locale.ENGLISH), this);
/*     */   }
/*     */ 
/*     */   FontFamily(String paramString)
/*     */   {
/*  95 */     this.logicalFont = false;
/*  96 */     this.familyName = paramString;
/*  97 */     this.familyRank = 4;
/*     */   }
/*     */ 
/*     */   public String getFamilyName() {
/* 101 */     return this.familyName;
/*     */   }
/*     */ 
/*     */   public int getRank() {
/* 105 */     return this.familyRank;
/*     */   }
/*     */ 
/*     */   public void setFont(Font2D paramFont2D, int paramInt) {
/* 109 */     if (paramFont2D.getRank() > this.familyRank) {
/* 110 */       if (FontUtilities.isLogging()) {
/* 111 */         FontUtilities.getLogger().warning("Rejecting adding " + paramFont2D + " of lower rank " + paramFont2D.getRank() + " to family " + this + " of rank " + this.familyRank);
/*     */       }
/*     */ 
/* 117 */       return;
/*     */     }
/*     */ 
/* 120 */     switch (paramInt)
/*     */     {
/*     */     case 0:
/* 123 */       this.plain = paramFont2D;
/* 124 */       break;
/*     */     case 1:
/* 127 */       this.bold = paramFont2D;
/* 128 */       break;
/*     */     case 2:
/* 131 */       this.italic = paramFont2D;
/* 132 */       break;
/*     */     case 3:
/* 135 */       this.bolditalic = paramFont2D;
/* 136 */       break;
/*     */     }
/*     */   }
/*     */ 
/*     */   public Font2D getFontWithExactStyleMatch(int paramInt)
/*     */   {
/* 145 */     switch (paramInt)
/*     */     {
/*     */     case 0:
/* 148 */       return this.plain;
/*     */     case 1:
/* 151 */       return this.bold;
/*     */     case 2:
/* 154 */       return this.italic;
/*     */     case 3:
/* 157 */       return this.bolditalic;
/*     */     }
/*     */ 
/* 160 */     return null;
/*     */   }
/*     */ 
/*     */   public Font2D getFont(int paramInt)
/*     */   {
/* 174 */     switch (paramInt)
/*     */     {
/*     */     case 0:
/* 177 */       return this.plain;
/*     */     case 1:
/* 180 */       if (this.bold != null)
/* 181 */         return this.bold;
/* 182 */       if ((this.plain != null) && (this.plain.canDoStyle(paramInt))) {
/* 183 */         return this.plain;
/*     */       }
/* 185 */       return null;
/*     */     case 2:
/* 189 */       if (this.italic != null)
/* 190 */         return this.italic;
/* 191 */       if ((this.plain != null) && (this.plain.canDoStyle(paramInt))) {
/* 192 */         return this.plain;
/*     */       }
/* 194 */       return null;
/*     */     case 3:
/* 198 */       if (this.bolditalic != null)
/* 199 */         return this.bolditalic;
/* 200 */       if ((this.italic != null) && (this.italic.canDoStyle(paramInt)))
/* 201 */         return this.italic;
/* 202 */       if ((this.bold != null) && (this.bold.canDoStyle(paramInt)))
/* 203 */         return this.italic;
/* 204 */       if ((this.plain != null) && (this.plain.canDoStyle(paramInt))) {
/* 205 */         return this.plain;
/*     */       }
/* 207 */       return null;
/*     */     }
/*     */ 
/* 210 */     return null;
/*     */   }
/*     */ 
/*     */   Font2D getClosestStyle(int paramInt)
/*     */   {
/* 222 */     switch (paramInt)
/*     */     {
/*     */     case 0:
/* 226 */       if (this.bold != null)
/* 227 */         return this.bold;
/* 228 */       if (this.italic != null) {
/* 229 */         return this.italic;
/*     */       }
/* 231 */       return this.bolditalic;
/*     */     case 1:
/* 237 */       if (this.plain != null)
/* 238 */         return this.plain;
/* 239 */       if (this.bolditalic != null) {
/* 240 */         return this.bolditalic;
/*     */       }
/* 242 */       return this.italic;
/*     */     case 2:
/* 248 */       if (this.bolditalic != null)
/* 249 */         return this.bolditalic;
/* 250 */       if (this.plain != null) {
/* 251 */         return this.plain;
/*     */       }
/* 253 */       return this.bold;
/*     */     case 3:
/* 257 */       if (this.italic != null)
/* 258 */         return this.italic;
/* 259 */       if (this.bold != null) {
/* 260 */         return this.bold;
/*     */       }
/* 262 */       return this.plain;
/*     */     }
/*     */ 
/* 265 */     return null;
/*     */   }
/*     */ 
/*     */   static synchronized void addLocaleNames(FontFamily paramFontFamily, String[] paramArrayOfString)
/*     */   {
/* 272 */     if (allLocaleNames == null) {
/* 273 */       allLocaleNames = new HashMap();
/*     */     }
/* 275 */     for (int i = 0; i < paramArrayOfString.length; i++)
/* 276 */       allLocaleNames.put(paramArrayOfString[i].toLowerCase(), paramFontFamily);
/*     */   }
/*     */ 
/*     */   public static synchronized FontFamily getLocaleFamily(String paramString)
/*     */   {
/* 281 */     if (allLocaleNames == null) {
/* 282 */       return null;
/*     */     }
/* 284 */     return (FontFamily)allLocaleNames.get(paramString.toLowerCase());
/*     */   }
/*     */ 
/*     */   public String toString() {
/* 288 */     return "Font family: " + this.familyName + " plain=" + this.plain + " bold=" + this.bold + " italic=" + this.italic + " bolditalic=" + this.bolditalic;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.font.FontFamily
 * JD-Core Version:    0.6.2
 */