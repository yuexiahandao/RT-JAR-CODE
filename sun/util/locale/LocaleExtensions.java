/*     */ package sun.util.locale;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import java.util.SortedMap;
/*     */ import java.util.SortedSet;
/*     */ import java.util.TreeMap;
/*     */ import java.util.TreeSet;
/*     */ 
/*     */ public class LocaleExtensions
/*     */ {
/*     */   private final Map<Character, Extension> extensionMap;
/*     */   private final String id;
/*  52 */   public static final LocaleExtensions CALENDAR_JAPANESE = new LocaleExtensions("u-ca-japanese", Character.valueOf('u'), UnicodeLocaleExtension.CA_JAPANESE);
/*     */ 
/*  57 */   public static final LocaleExtensions NUMBER_THAI = new LocaleExtensions("u-nu-thai", Character.valueOf('u'), UnicodeLocaleExtension.NU_THAI);
/*     */ 
/*     */   private LocaleExtensions(String paramString, Character paramCharacter, Extension paramExtension)
/*     */   {
/*  63 */     this.id = paramString;
/*  64 */     this.extensionMap = Collections.singletonMap(paramCharacter, paramExtension);
/*     */   }
/*     */ 
/*     */   LocaleExtensions(Map<InternalLocaleBuilder.CaseInsensitiveChar, String> paramMap, Set<InternalLocaleBuilder.CaseInsensitiveString> paramSet, Map<InternalLocaleBuilder.CaseInsensitiveString, String> paramMap1)
/*     */   {
/*  73 */     int i = !LocaleUtils.isEmpty(paramMap) ? 1 : 0;
/*  74 */     int j = !LocaleUtils.isEmpty(paramSet) ? 1 : 0;
/*  75 */     int k = !LocaleUtils.isEmpty(paramMap1) ? 1 : 0;
/*     */ 
/*  77 */     if ((i == 0) && (j == 0) && (k == 0)) {
/*  78 */       this.id = "";
/*  79 */       this.extensionMap = Collections.emptyMap();
/*  80 */       return;
/*     */     }
/*     */ 
/*  84 */     TreeMap localTreeMap = new TreeMap();
/*     */     Object localObject1;
/*  85 */     if (i != 0)
/*  86 */       for (localObject1 = paramMap.entrySet().iterator(); ((Iterator)localObject1).hasNext(); ) { localObject2 = (Map.Entry)((Iterator)localObject1).next();
/*  87 */         char c = LocaleUtils.toLower(((InternalLocaleBuilder.CaseInsensitiveChar)((Map.Entry)localObject2).getKey()).value());
/*  88 */         localObject4 = (String)((Map.Entry)localObject2).getValue();
/*     */ 
/*  90 */         if (LanguageTag.isPrivateusePrefixChar(c))
/*     */         {
/*  92 */           localObject4 = InternalLocaleBuilder.removePrivateuseVariant((String)localObject4);
/*  93 */           if (localObject4 == null);
/*     */         }
/*     */         else
/*     */         {
/*  98 */           localTreeMap.put(Character.valueOf(c), new Extension(c, LocaleUtils.toLowerString((String)localObject4)));
/*     */         }
/*     */       }
/*     */     Object localObject2;
/*     */     Object localObject4;
/* 102 */     if ((j != 0) || (k != 0)) {
/* 103 */       localObject1 = null;
/* 104 */       localObject2 = null;
/*     */ 
/* 106 */       if (j != 0) {
/* 107 */         localObject1 = new TreeSet();
/* 108 */         for (localObject3 = paramSet.iterator(); ((Iterator)localObject3).hasNext(); ) { localObject4 = (InternalLocaleBuilder.CaseInsensitiveString)((Iterator)localObject3).next();
/* 109 */           ((SortedSet)localObject1).add(LocaleUtils.toLowerString(((InternalLocaleBuilder.CaseInsensitiveString)localObject4).value()));
/*     */         }
/*     */       }
/*     */ 
/* 113 */       if (k != 0) {
/* 114 */         localObject2 = new TreeMap();
/* 115 */         for (localObject3 = paramMap1.entrySet().iterator(); ((Iterator)localObject3).hasNext(); ) { localObject4 = (Map.Entry)((Iterator)localObject3).next();
/* 116 */           String str1 = LocaleUtils.toLowerString(((InternalLocaleBuilder.CaseInsensitiveString)((Map.Entry)localObject4).getKey()).value());
/* 117 */           String str2 = LocaleUtils.toLowerString((String)((Map.Entry)localObject4).getValue());
/* 118 */           ((SortedMap)localObject2).put(str1, str2);
/*     */         }
/*     */       }
/*     */ 
/* 122 */       Object localObject3 = new UnicodeLocaleExtension((SortedSet)localObject1, (SortedMap)localObject2);
/* 123 */       localTreeMap.put(Character.valueOf('u'), localObject3);
/*     */     }
/*     */ 
/* 126 */     if (localTreeMap.isEmpty())
/*     */     {
/* 128 */       this.id = "";
/* 129 */       this.extensionMap = Collections.emptyMap();
/*     */     } else {
/* 131 */       this.id = toID(localTreeMap);
/* 132 */       this.extensionMap = localTreeMap;
/*     */     }
/*     */   }
/*     */ 
/*     */   public Set<Character> getKeys() {
/* 137 */     if (this.extensionMap.isEmpty()) {
/* 138 */       return Collections.emptySet();
/*     */     }
/* 140 */     return Collections.unmodifiableSet(this.extensionMap.keySet());
/*     */   }
/*     */ 
/*     */   public Extension getExtension(Character paramCharacter) {
/* 144 */     return (Extension)this.extensionMap.get(Character.valueOf(LocaleUtils.toLower(paramCharacter.charValue())));
/*     */   }
/*     */ 
/*     */   public String getExtensionValue(Character paramCharacter) {
/* 148 */     Extension localExtension = (Extension)this.extensionMap.get(Character.valueOf(LocaleUtils.toLower(paramCharacter.charValue())));
/* 149 */     if (localExtension == null) {
/* 150 */       return null;
/*     */     }
/* 152 */     return localExtension.getValue();
/*     */   }
/*     */ 
/*     */   public Set<String> getUnicodeLocaleAttributes() {
/* 156 */     Extension localExtension = (Extension)this.extensionMap.get(Character.valueOf('u'));
/* 157 */     if (localExtension == null) {
/* 158 */       return Collections.emptySet();
/*     */     }
/* 160 */     assert ((localExtension instanceof UnicodeLocaleExtension));
/* 161 */     return ((UnicodeLocaleExtension)localExtension).getUnicodeLocaleAttributes();
/*     */   }
/*     */ 
/*     */   public Set<String> getUnicodeLocaleKeys() {
/* 165 */     Extension localExtension = (Extension)this.extensionMap.get(Character.valueOf('u'));
/* 166 */     if (localExtension == null) {
/* 167 */       return Collections.emptySet();
/*     */     }
/* 169 */     assert ((localExtension instanceof UnicodeLocaleExtension));
/* 170 */     return ((UnicodeLocaleExtension)localExtension).getUnicodeLocaleKeys();
/*     */   }
/*     */ 
/*     */   public String getUnicodeLocaleType(String paramString) {
/* 174 */     Extension localExtension = (Extension)this.extensionMap.get(Character.valueOf('u'));
/* 175 */     if (localExtension == null) {
/* 176 */       return null;
/*     */     }
/* 178 */     assert ((localExtension instanceof UnicodeLocaleExtension));
/* 179 */     return ((UnicodeLocaleExtension)localExtension).getUnicodeLocaleType(LocaleUtils.toLowerString(paramString));
/*     */   }
/*     */ 
/*     */   public boolean isEmpty() {
/* 183 */     return this.extensionMap.isEmpty();
/*     */   }
/*     */ 
/*     */   public static boolean isValidKey(char paramChar) {
/* 187 */     return (LanguageTag.isExtensionSingletonChar(paramChar)) || (LanguageTag.isPrivateusePrefixChar(paramChar));
/*     */   }
/*     */ 
/*     */   public static boolean isValidUnicodeLocaleKey(String paramString) {
/* 191 */     return UnicodeLocaleExtension.isKey(paramString);
/*     */   }
/*     */ 
/*     */   private static String toID(SortedMap<Character, Extension> paramSortedMap) {
/* 195 */     StringBuilder localStringBuilder = new StringBuilder();
/* 196 */     Object localObject = null;
/* 197 */     for (Map.Entry localEntry : paramSortedMap.entrySet()) {
/* 198 */       char c = ((Character)localEntry.getKey()).charValue();
/* 199 */       Extension localExtension = (Extension)localEntry.getValue();
/* 200 */       if (LanguageTag.isPrivateusePrefixChar(c)) {
/* 201 */         localObject = localExtension;
/*     */       } else {
/* 203 */         if (localStringBuilder.length() > 0) {
/* 204 */           localStringBuilder.append("-");
/*     */         }
/* 206 */         localStringBuilder.append(localExtension);
/*     */       }
/*     */     }
/* 209 */     if (localObject != null) {
/* 210 */       if (localStringBuilder.length() > 0) {
/* 211 */         localStringBuilder.append("-");
/*     */       }
/* 213 */       localStringBuilder.append(localObject);
/*     */     }
/* 215 */     return localStringBuilder.toString();
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 220 */     return this.id;
/*     */   }
/*     */ 
/*     */   public String getID() {
/* 224 */     return this.id;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 229 */     return this.id.hashCode();
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 234 */     if (this == paramObject) {
/* 235 */       return true;
/*     */     }
/* 237 */     if (!(paramObject instanceof LocaleExtensions)) {
/* 238 */       return false;
/*     */     }
/* 240 */     return this.id.equals(((LocaleExtensions)paramObject).id);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.util.locale.LocaleExtensions
 * JD-Core Version:    0.6.2
 */