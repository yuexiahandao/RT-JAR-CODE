/*     */ package sun.util.locale;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import java.util.SortedMap;
/*     */ import java.util.SortedSet;
/*     */ 
/*     */ public class UnicodeLocaleExtension extends Extension
/*     */ {
/*     */   public static final char SINGLETON = 'u';
/*     */   private final Set<String> attributes;
/*     */   private final Map<String, String> keywords;
/*  48 */   public static final UnicodeLocaleExtension CA_JAPANESE = new UnicodeLocaleExtension("ca", "japanese");
/*     */ 
/*  50 */   public static final UnicodeLocaleExtension NU_THAI = new UnicodeLocaleExtension("nu", "thai");
/*     */ 
/*     */   private UnicodeLocaleExtension(String paramString1, String paramString2)
/*     */   {
/*  54 */     super('u', paramString1 + "-" + paramString2);
/*  55 */     this.attributes = Collections.emptySet();
/*  56 */     this.keywords = Collections.singletonMap(paramString1, paramString2);
/*     */   }
/*     */ 
/*     */   UnicodeLocaleExtension(SortedSet<String> paramSortedSet, SortedMap<String, String> paramSortedMap) {
/*  60 */     super('u');
/*  61 */     if (paramSortedSet != null)
/*  62 */       this.attributes = paramSortedSet;
/*     */     else {
/*  64 */       this.attributes = Collections.emptySet();
/*     */     }
/*  66 */     if (paramSortedMap != null)
/*  67 */       this.keywords = paramSortedMap;
/*     */     else {
/*  69 */       this.keywords = Collections.emptyMap();
/*     */     }
/*     */ 
/*  72 */     if ((!this.attributes.isEmpty()) || (!this.keywords.isEmpty())) {
/*  73 */       StringBuilder localStringBuilder = new StringBuilder();
/*  74 */       for (Iterator localIterator = this.attributes.iterator(); localIterator.hasNext(); ) { localObject = (String)localIterator.next();
/*  75 */         localStringBuilder.append("-").append((String)localObject);
/*     */       }
/*  77 */       Object localObject;
/*  77 */       for (localIterator = this.keywords.entrySet().iterator(); localIterator.hasNext(); ) { localObject = (Map.Entry)localIterator.next();
/*  78 */         String str1 = (String)((Map.Entry)localObject).getKey();
/*  79 */         String str2 = (String)((Map.Entry)localObject).getValue();
/*     */ 
/*  81 */         localStringBuilder.append("-").append(str1);
/*  82 */         if (str2.length() > 0) {
/*  83 */           localStringBuilder.append("-").append(str2);
/*     */         }
/*     */       }
/*  86 */       setValue(localStringBuilder.substring(1));
/*     */     }
/*     */   }
/*     */ 
/*     */   public Set<String> getUnicodeLocaleAttributes() {
/*  91 */     if (this.attributes == Collections.EMPTY_SET) {
/*  92 */       return this.attributes;
/*     */     }
/*  94 */     return Collections.unmodifiableSet(this.attributes);
/*     */   }
/*     */ 
/*     */   public Set<String> getUnicodeLocaleKeys() {
/*  98 */     if (this.keywords == Collections.EMPTY_MAP) {
/*  99 */       return Collections.emptySet();
/*     */     }
/* 101 */     return Collections.unmodifiableSet(this.keywords.keySet());
/*     */   }
/*     */ 
/*     */   public String getUnicodeLocaleType(String paramString) {
/* 105 */     return (String)this.keywords.get(paramString);
/*     */   }
/*     */ 
/*     */   public static boolean isSingletonChar(char paramChar) {
/* 109 */     return 'u' == LocaleUtils.toLower(paramChar);
/*     */   }
/*     */ 
/*     */   public static boolean isAttribute(String paramString)
/*     */   {
/* 114 */     int i = paramString.length();
/* 115 */     return (i >= 3) && (i <= 8) && (LocaleUtils.isAlphaNumericString(paramString));
/*     */   }
/*     */ 
/*     */   public static boolean isKey(String paramString)
/*     */   {
/* 120 */     return (paramString.length() == 2) && (LocaleUtils.isAlphaNumericString(paramString));
/*     */   }
/*     */ 
/*     */   public static boolean isTypeSubtag(String paramString)
/*     */   {
/* 125 */     int i = paramString.length();
/* 126 */     return (i >= 3) && (i <= 8) && (LocaleUtils.isAlphaNumericString(paramString));
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.util.locale.UnicodeLocaleExtension
 * JD-Core Version:    0.6.2
 */