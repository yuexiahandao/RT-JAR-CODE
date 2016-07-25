/*     */ package sun.util.locale;
/*     */ 
/*     */ import java.lang.ref.SoftReference;
/*     */ 
/*     */ public final class BaseLocale
/*     */ {
/*     */   public static final String SEP = "_";
/*  41 */   private static final Cache CACHE = new Cache();
/*     */   private final String language;
/*     */   private final String script;
/*     */   private final String region;
/*     */   private final String variant;
/*  48 */   private volatile int hash = 0;
/*     */ 
/*     */   private BaseLocale(String paramString1, String paramString2)
/*     */   {
/*  52 */     this.language = paramString1;
/*  53 */     this.script = "";
/*  54 */     this.region = paramString2;
/*  55 */     this.variant = "";
/*     */   }
/*     */ 
/*     */   private BaseLocale(String paramString1, String paramString2, String paramString3, String paramString4) {
/*  59 */     this.language = (paramString1 != null ? LocaleUtils.toLowerString(paramString1).intern() : "");
/*  60 */     this.script = (paramString2 != null ? LocaleUtils.toTitleString(paramString2).intern() : "");
/*  61 */     this.region = (paramString3 != null ? LocaleUtils.toUpperString(paramString3).intern() : "");
/*  62 */     this.variant = (paramString4 != null ? paramString4.intern() : "");
/*     */   }
/*     */ 
/*     */   public static BaseLocale createInstance(String paramString1, String paramString2)
/*     */   {
/*  68 */     BaseLocale localBaseLocale = new BaseLocale(paramString1, paramString2);
/*  69 */     CACHE.put(new Key(paramString1, paramString2, null), localBaseLocale);
/*  70 */     return localBaseLocale;
/*     */   }
/*     */ 
/*     */   public static BaseLocale getInstance(String paramString1, String paramString2, String paramString3, String paramString4)
/*     */   {
/*  76 */     if (paramString1 != null) {
/*  77 */       if (LocaleUtils.caseIgnoreMatch(paramString1, "he"))
/*  78 */         paramString1 = "iw";
/*  79 */       else if (LocaleUtils.caseIgnoreMatch(paramString1, "yi"))
/*  80 */         paramString1 = "ji";
/*  81 */       else if (LocaleUtils.caseIgnoreMatch(paramString1, "id")) {
/*  82 */         paramString1 = "in";
/*     */       }
/*     */     }
/*     */ 
/*  86 */     Key localKey = new Key(paramString1, paramString2, paramString3, paramString4);
/*  87 */     BaseLocale localBaseLocale = (BaseLocale)CACHE.get(localKey);
/*  88 */     return localBaseLocale;
/*     */   }
/*     */ 
/*     */   public String getLanguage() {
/*  92 */     return this.language;
/*     */   }
/*     */ 
/*     */   public String getScript() {
/*  96 */     return this.script;
/*     */   }
/*     */ 
/*     */   public String getRegion() {
/* 100 */     return this.region;
/*     */   }
/*     */ 
/*     */   public String getVariant() {
/* 104 */     return this.variant;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 109 */     if (this == paramObject) {
/* 110 */       return true;
/*     */     }
/* 112 */     if (!(paramObject instanceof BaseLocale)) {
/* 113 */       return false;
/*     */     }
/* 115 */     BaseLocale localBaseLocale = (BaseLocale)paramObject;
/* 116 */     return (this.language == localBaseLocale.language) && (this.script == localBaseLocale.script) && (this.region == localBaseLocale.region) && (this.variant == localBaseLocale.variant);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 124 */     StringBuilder localStringBuilder = new StringBuilder();
/* 125 */     if (this.language.length() > 0) {
/* 126 */       localStringBuilder.append("language=");
/* 127 */       localStringBuilder.append(this.language);
/*     */     }
/* 129 */     if (this.script.length() > 0) {
/* 130 */       if (localStringBuilder.length() > 0) {
/* 131 */         localStringBuilder.append(", ");
/*     */       }
/* 133 */       localStringBuilder.append("script=");
/* 134 */       localStringBuilder.append(this.script);
/*     */     }
/* 136 */     if (this.region.length() > 0) {
/* 137 */       if (localStringBuilder.length() > 0) {
/* 138 */         localStringBuilder.append(", ");
/*     */       }
/* 140 */       localStringBuilder.append("region=");
/* 141 */       localStringBuilder.append(this.region);
/*     */     }
/* 143 */     if (this.variant.length() > 0) {
/* 144 */       if (localStringBuilder.length() > 0) {
/* 145 */         localStringBuilder.append(", ");
/*     */       }
/* 147 */       localStringBuilder.append("variant=");
/* 148 */       localStringBuilder.append(this.variant);
/*     */     }
/* 150 */     return localStringBuilder.toString();
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 155 */     int i = this.hash;
/* 156 */     if (i == 0)
/*     */     {
/* 158 */       i = this.language.hashCode();
/* 159 */       i = 31 * i + this.script.hashCode();
/* 160 */       i = 31 * i + this.region.hashCode();
/* 161 */       i = 31 * i + this.variant.hashCode();
/* 162 */       this.hash = i;
/*     */     }
/* 164 */     return i;
/*     */   }
/*     */ 
/*     */   private static class Cache extends LocaleObjectCache<BaseLocale.Key, BaseLocale>
/*     */   {
/*     */     protected BaseLocale.Key normalizeKey(BaseLocale.Key paramKey)
/*     */     {
/* 301 */       assert ((BaseLocale.Key.access$100(paramKey).get() != null) && (BaseLocale.Key.access$200(paramKey).get() != null) && (BaseLocale.Key.access$300(paramKey).get() != null) && (BaseLocale.Key.access$400(paramKey).get() != null));
/*     */ 
/* 306 */       return BaseLocale.Key.normalize(paramKey);
/*     */     }
/*     */ 
/*     */     protected BaseLocale createObject(BaseLocale.Key paramKey)
/*     */     {
/* 311 */       return new BaseLocale((String)BaseLocale.Key.access$100(paramKey).get(), (String)BaseLocale.Key.access$200(paramKey).get(), (String)BaseLocale.Key.access$300(paramKey).get(), (String)BaseLocale.Key.access$400(paramKey).get(), null);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static final class Key
/*     */   {
/*     */     private final SoftReference<String> lang;
/*     */     private final SoftReference<String> scrt;
/*     */     private final SoftReference<String> regn;
/*     */     private final SoftReference<String> vart;
/*     */     private final boolean normalized;
/*     */     private final int hash;
/*     */ 
/*     */     private Key(String paramString1, String paramString2)
/*     */     {
/* 180 */       assert ((paramString1.intern() == paramString1) && (paramString2.intern() == paramString2));
/*     */ 
/* 183 */       this.lang = new SoftReference(paramString1);
/* 184 */       this.scrt = new SoftReference("");
/* 185 */       this.regn = new SoftReference(paramString2);
/* 186 */       this.vart = new SoftReference("");
/* 187 */       this.normalized = true;
/*     */ 
/* 189 */       int i = paramString1.hashCode();
/* 190 */       if (paramString2 != "") {
/* 191 */         int j = paramString2.length();
/* 192 */         for (int k = 0; k < j; k++) {
/* 193 */           i = 31 * i + LocaleUtils.toLower(paramString2.charAt(k));
/*     */         }
/*     */       }
/* 196 */       this.hash = i;
/*     */     }
/*     */ 
/*     */     public Key(String paramString1, String paramString2, String paramString3, String paramString4) {
/* 200 */       this(paramString1, paramString2, paramString3, paramString4, false);
/*     */     }
/*     */ 
/*     */     private Key(String paramString1, String paramString2, String paramString3, String paramString4, boolean paramBoolean)
/*     */     {
/* 205 */       int i = 0;
/*     */       int j;
/*     */       int k;
/* 206 */       if (paramString1 != null) {
/* 207 */         this.lang = new SoftReference(paramString1);
/* 208 */         j = paramString1.length();
/* 209 */         for (k = 0; k < j; k++)
/* 210 */           i = 31 * i + LocaleUtils.toLower(paramString1.charAt(k));
/*     */       }
/*     */       else {
/* 213 */         this.lang = new SoftReference("");
/*     */       }
/* 215 */       if (paramString2 != null) {
/* 216 */         this.scrt = new SoftReference(paramString2);
/* 217 */         j = paramString2.length();
/* 218 */         for (k = 0; k < j; k++)
/* 219 */           i = 31 * i + LocaleUtils.toLower(paramString2.charAt(k));
/*     */       }
/*     */       else {
/* 222 */         this.scrt = new SoftReference("");
/*     */       }
/* 224 */       if (paramString3 != null) {
/* 225 */         this.regn = new SoftReference(paramString3);
/* 226 */         j = paramString3.length();
/* 227 */         for (k = 0; k < j; k++)
/* 228 */           i = 31 * i + LocaleUtils.toLower(paramString3.charAt(k));
/*     */       }
/*     */       else {
/* 231 */         this.regn = new SoftReference("");
/*     */       }
/* 233 */       if (paramString4 != null) {
/* 234 */         this.vart = new SoftReference(paramString4);
/* 235 */         j = paramString4.length();
/* 236 */         for (k = 0; k < j; k++)
/* 237 */           i = 31 * i + paramString4.charAt(k);
/*     */       }
/*     */       else {
/* 240 */         this.vart = new SoftReference("");
/*     */       }
/* 242 */       this.hash = i;
/* 243 */       this.normalized = paramBoolean;
/*     */     }
/*     */ 
/*     */     public boolean equals(Object paramObject)
/*     */     {
/* 248 */       if (this == paramObject) {
/* 249 */         return true;
/*     */       }
/*     */ 
/* 252 */       if (((paramObject instanceof Key)) && (this.hash == ((Key)paramObject).hash)) {
/* 253 */         String str1 = (String)this.lang.get();
/* 254 */         String str2 = (String)((Key)paramObject).lang.get();
/* 255 */         if ((str1 != null) && (str2 != null) && (LocaleUtils.caseIgnoreMatch(str2, str1)))
/*     */         {
/* 257 */           String str3 = (String)this.scrt.get();
/* 258 */           String str4 = (String)((Key)paramObject).scrt.get();
/* 259 */           if ((str3 != null) && (str4 != null) && (LocaleUtils.caseIgnoreMatch(str4, str3)))
/*     */           {
/* 261 */             String str5 = (String)this.regn.get();
/* 262 */             String str6 = (String)((Key)paramObject).regn.get();
/* 263 */             if ((str5 != null) && (str6 != null) && (LocaleUtils.caseIgnoreMatch(str6, str5)))
/*     */             {
/* 265 */               String str7 = (String)this.vart.get();
/* 266 */               String str8 = (String)((Key)paramObject).vart.get();
/* 267 */               return (str8 != null) && (str8.equals(str7));
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/* 272 */       return false;
/*     */     }
/*     */ 
/*     */     public int hashCode()
/*     */     {
/* 277 */       return this.hash;
/*     */     }
/*     */ 
/*     */     public static Key normalize(Key paramKey) {
/* 281 */       if (paramKey.normalized) {
/* 282 */         return paramKey;
/*     */       }
/*     */ 
/* 285 */       String str1 = LocaleUtils.toLowerString((String)paramKey.lang.get()).intern();
/* 286 */       String str2 = LocaleUtils.toTitleString((String)paramKey.scrt.get()).intern();
/* 287 */       String str3 = LocaleUtils.toUpperString((String)paramKey.regn.get()).intern();
/* 288 */       String str4 = ((String)paramKey.vart.get()).intern();
/*     */ 
/* 290 */       return new Key(str1, str2, str3, str4, true);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.util.locale.BaseLocale
 * JD-Core Version:    0.6.2
 */