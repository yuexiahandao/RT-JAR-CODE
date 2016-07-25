/*     */ package sun.util.locale;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ 
/*     */ public final class InternalLocaleBuilder
/*     */ {
/*  43 */   private static final CaseInsensitiveChar PRIVATEUSE_KEY = new CaseInsensitiveChar("x", null);
/*     */ 
/*  46 */   private String language = "";
/*  47 */   private String script = "";
/*  48 */   private String region = "";
/*  49 */   private String variant = "";
/*     */   private Map<CaseInsensitiveChar, String> extensions;
/*     */   private Set<CaseInsensitiveString> uattributes;
/*     */   private Map<CaseInsensitiveString, String> ukeywords;
/*     */ 
/*     */   public InternalLocaleBuilder setLanguage(String paramString)
/*     */     throws LocaleSyntaxException
/*     */   {
/*  60 */     if (LocaleUtils.isEmpty(paramString)) {
/*  61 */       this.language = "";
/*     */     } else {
/*  63 */       if (!LanguageTag.isLanguage(paramString)) {
/*  64 */         throw new LocaleSyntaxException("Ill-formed language: " + paramString, 0);
/*     */       }
/*  66 */       this.language = paramString;
/*     */     }
/*  68 */     return this;
/*     */   }
/*     */ 
/*     */   public InternalLocaleBuilder setScript(String paramString) throws LocaleSyntaxException {
/*  72 */     if (LocaleUtils.isEmpty(paramString)) {
/*  73 */       this.script = "";
/*     */     } else {
/*  75 */       if (!LanguageTag.isScript(paramString)) {
/*  76 */         throw new LocaleSyntaxException("Ill-formed script: " + paramString, 0);
/*     */       }
/*  78 */       this.script = paramString;
/*     */     }
/*  80 */     return this;
/*     */   }
/*     */ 
/*     */   public InternalLocaleBuilder setRegion(String paramString) throws LocaleSyntaxException {
/*  84 */     if (LocaleUtils.isEmpty(paramString)) {
/*  85 */       this.region = "";
/*     */     } else {
/*  87 */       if (!LanguageTag.isRegion(paramString)) {
/*  88 */         throw new LocaleSyntaxException("Ill-formed region: " + paramString, 0);
/*     */       }
/*  90 */       this.region = paramString;
/*     */     }
/*  92 */     return this;
/*     */   }
/*     */ 
/*     */   public InternalLocaleBuilder setVariant(String paramString) throws LocaleSyntaxException {
/*  96 */     if (LocaleUtils.isEmpty(paramString)) {
/*  97 */       this.variant = "";
/*     */     }
/*     */     else {
/* 100 */       String str = paramString.replaceAll("-", "_");
/* 101 */       int i = checkVariants(str, "_");
/* 102 */       if (i != -1) {
/* 103 */         throw new LocaleSyntaxException("Ill-formed variant: " + paramString, i);
/*     */       }
/* 105 */       this.variant = str;
/*     */     }
/* 107 */     return this;
/*     */   }
/*     */ 
/*     */   public InternalLocaleBuilder addUnicodeLocaleAttribute(String paramString) throws LocaleSyntaxException {
/* 111 */     if (!UnicodeLocaleExtension.isAttribute(paramString)) {
/* 112 */       throw new LocaleSyntaxException("Ill-formed Unicode locale attribute: " + paramString);
/*     */     }
/*     */ 
/* 115 */     if (this.uattributes == null) {
/* 116 */       this.uattributes = new HashSet(4);
/*     */     }
/* 118 */     this.uattributes.add(new CaseInsensitiveString(paramString));
/* 119 */     return this;
/*     */   }
/*     */ 
/*     */   public InternalLocaleBuilder removeUnicodeLocaleAttribute(String paramString) throws LocaleSyntaxException {
/* 123 */     if ((paramString == null) || (!UnicodeLocaleExtension.isAttribute(paramString))) {
/* 124 */       throw new LocaleSyntaxException("Ill-formed Unicode locale attribute: " + paramString);
/*     */     }
/* 126 */     if (this.uattributes != null) {
/* 127 */       this.uattributes.remove(new CaseInsensitiveString(paramString));
/*     */     }
/* 129 */     return this;
/*     */   }
/*     */ 
/*     */   public InternalLocaleBuilder setUnicodeLocaleKeyword(String paramString1, String paramString2) throws LocaleSyntaxException {
/* 133 */     if (!UnicodeLocaleExtension.isKey(paramString1)) {
/* 134 */       throw new LocaleSyntaxException("Ill-formed Unicode locale keyword key: " + paramString1);
/*     */     }
/*     */ 
/* 137 */     CaseInsensitiveString localCaseInsensitiveString = new CaseInsensitiveString(paramString1);
/* 138 */     if (paramString2 == null) {
/* 139 */       if (this.ukeywords != null)
/*     */       {
/* 141 */         this.ukeywords.remove(localCaseInsensitiveString);
/*     */       }
/*     */     } else {
/* 144 */       if (paramString2.length() != 0)
/*     */       {
/* 146 */         String str1 = paramString2.replaceAll("_", "-");
/*     */ 
/* 148 */         StringTokenIterator localStringTokenIterator = new StringTokenIterator(str1, "-");
/* 149 */         while (!localStringTokenIterator.isDone()) {
/* 150 */           String str2 = localStringTokenIterator.current();
/* 151 */           if (!UnicodeLocaleExtension.isTypeSubtag(str2)) {
/* 152 */             throw new LocaleSyntaxException("Ill-formed Unicode locale keyword type: " + paramString2, localStringTokenIterator.currentStart());
/*     */           }
/*     */ 
/* 156 */           localStringTokenIterator.next();
/*     */         }
/*     */       }
/* 159 */       if (this.ukeywords == null) {
/* 160 */         this.ukeywords = new HashMap(4);
/*     */       }
/* 162 */       this.ukeywords.put(localCaseInsensitiveString, paramString2);
/*     */     }
/* 164 */     return this;
/*     */   }
/*     */ 
/*     */   public InternalLocaleBuilder setExtension(char paramChar, String paramString) throws LocaleSyntaxException
/*     */   {
/* 169 */     boolean bool1 = LanguageTag.isPrivateusePrefixChar(paramChar);
/* 170 */     if ((!bool1) && (!LanguageTag.isExtensionSingletonChar(paramChar))) {
/* 171 */       throw new LocaleSyntaxException("Ill-formed extension key: " + paramChar);
/*     */     }
/*     */ 
/* 174 */     boolean bool2 = LocaleUtils.isEmpty(paramString);
/* 175 */     CaseInsensitiveChar localCaseInsensitiveChar = new CaseInsensitiveChar(paramChar);
/*     */ 
/* 177 */     if (bool2) {
/* 178 */       if (UnicodeLocaleExtension.isSingletonChar(localCaseInsensitiveChar.value()))
/*     */       {
/* 180 */         if (this.uattributes != null) {
/* 181 */           this.uattributes.clear();
/*     */         }
/* 183 */         if (this.ukeywords != null) {
/* 184 */           this.ukeywords.clear();
/*     */         }
/*     */       }
/* 187 */       else if ((this.extensions != null) && (this.extensions.containsKey(localCaseInsensitiveChar))) {
/* 188 */         this.extensions.remove(localCaseInsensitiveChar);
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 193 */       String str1 = paramString.replaceAll("_", "-");
/* 194 */       StringTokenIterator localStringTokenIterator = new StringTokenIterator(str1, "-");
/* 195 */       while (!localStringTokenIterator.isDone()) {
/* 196 */         String str2 = localStringTokenIterator.current();
/*     */         boolean bool3;
/* 198 */         if (bool1)
/* 199 */           bool3 = LanguageTag.isPrivateuseSubtag(str2);
/*     */         else {
/* 201 */           bool3 = LanguageTag.isExtensionSubtag(str2);
/*     */         }
/* 203 */         if (!bool3) {
/* 204 */           throw new LocaleSyntaxException("Ill-formed extension value: " + str2, localStringTokenIterator.currentStart());
/*     */         }
/*     */ 
/* 207 */         localStringTokenIterator.next();
/*     */       }
/*     */ 
/* 210 */       if (UnicodeLocaleExtension.isSingletonChar(localCaseInsensitiveChar.value())) {
/* 211 */         setUnicodeLocaleExtension(str1);
/*     */       } else {
/* 213 */         if (this.extensions == null) {
/* 214 */           this.extensions = new HashMap(4);
/*     */         }
/* 216 */         this.extensions.put(localCaseInsensitiveChar, str1);
/*     */       }
/*     */     }
/* 219 */     return this;
/*     */   }
/*     */ 
/*     */   public InternalLocaleBuilder setExtensions(String paramString)
/*     */     throws LocaleSyntaxException
/*     */   {
/* 226 */     if (LocaleUtils.isEmpty(paramString)) {
/* 227 */       clearExtensions();
/* 228 */       return this;
/*     */     }
/* 230 */     paramString = paramString.replaceAll("_", "-");
/* 231 */     StringTokenIterator localStringTokenIterator = new StringTokenIterator(paramString, "-");
/*     */ 
/* 233 */     ArrayList localArrayList = null;
/* 234 */     String str1 = null;
/*     */ 
/* 236 */     int i = 0;
/*     */     String str2;
/*     */     int j;
/*     */     Object localObject;
/* 240 */     while (!localStringTokenIterator.isDone()) {
/* 241 */       str2 = localStringTokenIterator.current();
/* 242 */       if (!LanguageTag.isExtensionSingleton(str2)) break;
/* 243 */       j = localStringTokenIterator.currentStart();
/* 244 */       localObject = str2;
/* 245 */       StringBuilder localStringBuilder = new StringBuilder((String)localObject);
/*     */ 
/* 247 */       localStringTokenIterator.next();
/* 248 */       while (!localStringTokenIterator.isDone()) {
/* 249 */         str2 = localStringTokenIterator.current();
/* 250 */         if (!LanguageTag.isExtensionSubtag(str2)) break;
/* 251 */         localStringBuilder.append("-").append(str2);
/* 252 */         i = localStringTokenIterator.currentEnd();
/*     */ 
/* 256 */         localStringTokenIterator.next();
/*     */       }
/*     */ 
/* 259 */       if (i < j) {
/* 260 */         throw new LocaleSyntaxException("Incomplete extension '" + (String)localObject + "'", j);
/*     */       }
/*     */ 
/* 264 */       if (localArrayList == null) {
/* 265 */         localArrayList = new ArrayList(4);
/*     */       }
/* 267 */       localArrayList.add(localStringBuilder.toString());
/*     */     }
/*     */ 
/* 272 */     if (!localStringTokenIterator.isDone()) {
/* 273 */       str2 = localStringTokenIterator.current();
/* 274 */       if (LanguageTag.isPrivateusePrefix(str2)) {
/* 275 */         j = localStringTokenIterator.currentStart();
/* 276 */         localObject = new StringBuilder(str2);
/*     */ 
/* 278 */         localStringTokenIterator.next();
/* 279 */         while (!localStringTokenIterator.isDone()) {
/* 280 */           str2 = localStringTokenIterator.current();
/* 281 */           if (!LanguageTag.isPrivateuseSubtag(str2)) {
/*     */             break;
/*     */           }
/* 284 */           ((StringBuilder)localObject).append("-").append(str2);
/* 285 */           i = localStringTokenIterator.currentEnd();
/*     */ 
/* 287 */           localStringTokenIterator.next();
/*     */         }
/* 289 */         if (i <= j) {
/* 290 */           throw new LocaleSyntaxException("Incomplete privateuse:" + paramString.substring(j), j);
/*     */         }
/*     */ 
/* 294 */         str1 = ((StringBuilder)localObject).toString();
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 299 */     if (!localStringTokenIterator.isDone()) {
/* 300 */       throw new LocaleSyntaxException("Ill-formed extension subtags:" + paramString.substring(localStringTokenIterator.currentStart()), localStringTokenIterator.currentStart());
/*     */     }
/*     */ 
/* 305 */     return setExtensions(localArrayList, str1);
/*     */   }
/*     */ 
/*     */   private InternalLocaleBuilder setExtensions(List<String> paramList, String paramString)
/*     */   {
/* 313 */     clearExtensions();
/*     */     HashSet localHashSet;
/* 315 */     if (!LocaleUtils.isEmpty(paramList)) {
/* 316 */       localHashSet = new HashSet(paramList.size());
/* 317 */       for (String str : paramList) {
/* 318 */         CaseInsensitiveChar localCaseInsensitiveChar = new CaseInsensitiveChar(str, null);
/*     */ 
/* 320 */         if (!localHashSet.contains(localCaseInsensitiveChar))
/*     */         {
/* 322 */           if (UnicodeLocaleExtension.isSingletonChar(localCaseInsensitiveChar.value())) {
/* 323 */             setUnicodeLocaleExtension(str.substring(2));
/*     */           } else {
/* 325 */             if (this.extensions == null) {
/* 326 */               this.extensions = new HashMap(4);
/*     */             }
/* 328 */             this.extensions.put(localCaseInsensitiveChar, str.substring(2));
/*     */           }
/*     */         }
/* 331 */         localHashSet.add(localCaseInsensitiveChar);
/*     */       }
/*     */     }
/* 334 */     if ((paramString != null) && (paramString.length() > 0))
/*     */     {
/* 336 */       if (this.extensions == null) {
/* 337 */         this.extensions = new HashMap(1);
/*     */       }
/* 339 */       this.extensions.put(new CaseInsensitiveChar(paramString, null), paramString.substring(2));
/*     */     }
/*     */ 
/* 342 */     return this;
/*     */   }
/*     */ 
/*     */   public InternalLocaleBuilder setLanguageTag(LanguageTag paramLanguageTag)
/*     */   {
/* 349 */     clear();
/* 350 */     if (!paramLanguageTag.getExtlangs().isEmpty()) {
/* 351 */       this.language = ((String)paramLanguageTag.getExtlangs().get(0));
/*     */     } else {
/* 353 */       localObject = paramLanguageTag.getLanguage();
/* 354 */       if (!((String)localObject).equals("und")) {
/* 355 */         this.language = ((String)localObject);
/*     */       }
/*     */     }
/* 358 */     this.script = paramLanguageTag.getScript();
/* 359 */     this.region = paramLanguageTag.getRegion();
/*     */ 
/* 361 */     Object localObject = paramLanguageTag.getVariants();
/* 362 */     if (!((List)localObject).isEmpty()) {
/* 363 */       StringBuilder localStringBuilder = new StringBuilder((String)((List)localObject).get(0));
/* 364 */       int i = ((List)localObject).size();
/* 365 */       for (int j = 1; j < i; j++) {
/* 366 */         localStringBuilder.append("_").append((String)((List)localObject).get(j));
/*     */       }
/* 368 */       this.variant = localStringBuilder.toString();
/*     */     }
/*     */ 
/* 371 */     setExtensions(paramLanguageTag.getExtensions(), paramLanguageTag.getPrivateuse());
/*     */ 
/* 373 */     return this;
/*     */   }
/*     */ 
/*     */   public InternalLocaleBuilder setLocale(BaseLocale paramBaseLocale, LocaleExtensions paramLocaleExtensions) throws LocaleSyntaxException {
/* 377 */     String str1 = paramBaseLocale.getLanguage();
/* 378 */     String str2 = paramBaseLocale.getScript();
/* 379 */     String str3 = paramBaseLocale.getRegion();
/* 380 */     String str4 = paramBaseLocale.getVariant();
/*     */ 
/* 385 */     if ((str1.equals("ja")) && (str3.equals("JP")) && (str4.equals("JP")))
/*     */     {
/* 388 */       assert ("japanese".equals(paramLocaleExtensions.getUnicodeLocaleType("ca")));
/* 389 */       str4 = "";
/*     */     }
/* 392 */     else if ((str1.equals("th")) && (str3.equals("TH")) && (str4.equals("TH")))
/*     */     {
/* 395 */       assert ("thai".equals(paramLocaleExtensions.getUnicodeLocaleType("nu")));
/* 396 */       str4 = "";
/*     */     }
/* 399 */     else if ((str1.equals("no")) && (str3.equals("NO")) && (str4.equals("NY")))
/*     */     {
/* 402 */       str1 = "nn";
/* 403 */       str4 = "";
/*     */     }
/*     */ 
/* 409 */     if ((str1.length() > 0) && (!LanguageTag.isLanguage(str1))) {
/* 410 */       throw new LocaleSyntaxException("Ill-formed language: " + str1);
/*     */     }
/*     */ 
/* 413 */     if ((str2.length() > 0) && (!LanguageTag.isScript(str2))) {
/* 414 */       throw new LocaleSyntaxException("Ill-formed script: " + str2);
/*     */     }
/*     */ 
/* 417 */     if ((str3.length() > 0) && (!LanguageTag.isRegion(str3))) {
/* 418 */       throw new LocaleSyntaxException("Ill-formed region: " + str3);
/*     */     }
/*     */ 
/* 421 */     if (str4.length() > 0) {
/* 422 */       int i = checkVariants(str4, "_");
/* 423 */       if (i != -1) {
/* 424 */         throw new LocaleSyntaxException("Ill-formed variant: " + str4, i);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 430 */     this.language = str1;
/* 431 */     this.script = str2;
/* 432 */     this.region = str3;
/* 433 */     this.variant = str4;
/* 434 */     clearExtensions();
/*     */ 
/* 436 */     Set localSet = paramLocaleExtensions == null ? null : paramLocaleExtensions.getKeys();
/* 437 */     if (localSet != null)
/*     */     {
/* 439 */       for (Character localCharacter : localSet) {
/* 440 */         Extension localExtension = paramLocaleExtensions.getExtension(localCharacter);
/*     */         UnicodeLocaleExtension localUnicodeLocaleExtension;
/*     */         Iterator localIterator2;
/*     */         String str5;
/* 441 */         if ((localExtension instanceof UnicodeLocaleExtension)) {
/* 442 */           localUnicodeLocaleExtension = (UnicodeLocaleExtension)localExtension;
/* 443 */           for (localIterator2 = localUnicodeLocaleExtension.getUnicodeLocaleAttributes().iterator(); localIterator2.hasNext(); ) { str5 = (String)localIterator2.next();
/* 444 */             if (this.uattributes == null) {
/* 445 */               this.uattributes = new HashSet(4);
/*     */             }
/* 447 */             this.uattributes.add(new CaseInsensitiveString(str5));
/*     */           }
/* 449 */           for (localIterator2 = localUnicodeLocaleExtension.getUnicodeLocaleKeys().iterator(); localIterator2.hasNext(); ) { str5 = (String)localIterator2.next();
/* 450 */             if (this.ukeywords == null) {
/* 451 */               this.ukeywords = new HashMap(4);
/*     */             }
/* 453 */             this.ukeywords.put(new CaseInsensitiveString(str5), localUnicodeLocaleExtension.getUnicodeLocaleType(str5)); }
/*     */         }
/*     */         else {
/* 456 */           if (this.extensions == null) {
/* 457 */             this.extensions = new HashMap(4);
/*     */           }
/* 459 */           this.extensions.put(new CaseInsensitiveChar(localCharacter.charValue()), localExtension.getValue());
/*     */         }
/*     */       }
/*     */     }
/* 463 */     return this;
/*     */   }
/*     */ 
/*     */   public InternalLocaleBuilder clear() {
/* 467 */     this.language = "";
/* 468 */     this.script = "";
/* 469 */     this.region = "";
/* 470 */     this.variant = "";
/* 471 */     clearExtensions();
/* 472 */     return this;
/*     */   }
/*     */ 
/*     */   public InternalLocaleBuilder clearExtensions() {
/* 476 */     if (this.extensions != null) {
/* 477 */       this.extensions.clear();
/*     */     }
/* 479 */     if (this.uattributes != null) {
/* 480 */       this.uattributes.clear();
/*     */     }
/* 482 */     if (this.ukeywords != null) {
/* 483 */       this.ukeywords.clear();
/*     */     }
/* 485 */     return this;
/*     */   }
/*     */ 
/*     */   public BaseLocale getBaseLocale() {
/* 489 */     String str1 = this.language;
/* 490 */     String str2 = this.script;
/* 491 */     String str3 = this.region;
/* 492 */     String str4 = this.variant;
/*     */ 
/* 496 */     if (this.extensions != null) {
/* 497 */       String str5 = (String)this.extensions.get(PRIVATEUSE_KEY);
/* 498 */       if (str5 != null) {
/* 499 */         StringTokenIterator localStringTokenIterator = new StringTokenIterator(str5, "-");
/* 500 */         int i = 0;
/* 501 */         int j = -1;
/* 502 */         while (!localStringTokenIterator.isDone()) {
/* 503 */           if (i != 0) {
/* 504 */             j = localStringTokenIterator.currentStart();
/* 505 */             break;
/*     */           }
/* 507 */           if (LocaleUtils.caseIgnoreMatch(localStringTokenIterator.current(), "lvariant")) {
/* 508 */             i = 1;
/*     */           }
/* 510 */           localStringTokenIterator.next();
/*     */         }
/* 512 */         if (j != -1) {
/* 513 */           StringBuilder localStringBuilder = new StringBuilder(str4);
/* 514 */           if (localStringBuilder.length() != 0) {
/* 515 */             localStringBuilder.append("_");
/*     */           }
/* 517 */           localStringBuilder.append(str5.substring(j).replaceAll("-", "_"));
/*     */ 
/* 519 */           str4 = localStringBuilder.toString();
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 524 */     return BaseLocale.getInstance(str1, str2, str3, str4);
/*     */   }
/*     */ 
/*     */   public LocaleExtensions getLocaleExtensions() {
/* 528 */     if ((LocaleUtils.isEmpty(this.extensions)) && (LocaleUtils.isEmpty(this.uattributes)) && (LocaleUtils.isEmpty(this.ukeywords)))
/*     */     {
/* 530 */       return null;
/*     */     }
/*     */ 
/* 533 */     LocaleExtensions localLocaleExtensions = new LocaleExtensions(this.extensions, this.uattributes, this.ukeywords);
/* 534 */     return localLocaleExtensions.isEmpty() ? null : localLocaleExtensions;
/*     */   }
/*     */ 
/*     */   static String removePrivateuseVariant(String paramString)
/*     */   {
/* 542 */     StringTokenIterator localStringTokenIterator = new StringTokenIterator(paramString, "-");
/*     */ 
/* 547 */     int i = -1;
/* 548 */     int j = 0;
/* 549 */     while (!localStringTokenIterator.isDone()) {
/* 550 */       if (i != -1)
/*     */       {
/* 553 */         j = 1;
/* 554 */         break;
/*     */       }
/* 556 */       if (LocaleUtils.caseIgnoreMatch(localStringTokenIterator.current(), "lvariant")) {
/* 557 */         i = localStringTokenIterator.currentStart();
/*     */       }
/* 559 */       localStringTokenIterator.next();
/*     */     }
/* 561 */     if (j == 0) {
/* 562 */       return paramString;
/*     */     }
/*     */ 
/* 565 */     assert ((i == 0) || (i > 1));
/* 566 */     return i == 0 ? null : paramString.substring(0, i - 1);
/*     */   }
/*     */ 
/*     */   private int checkVariants(String paramString1, String paramString2)
/*     */   {
/* 574 */     StringTokenIterator localStringTokenIterator = new StringTokenIterator(paramString1, paramString2);
/* 575 */     while (!localStringTokenIterator.isDone()) {
/* 576 */       String str = localStringTokenIterator.current();
/* 577 */       if (!LanguageTag.isVariant(str)) {
/* 578 */         return localStringTokenIterator.currentStart();
/*     */       }
/* 580 */       localStringTokenIterator.next();
/*     */     }
/* 582 */     return -1;
/*     */   }
/*     */ 
/*     */   private void setUnicodeLocaleExtension(String paramString)
/*     */   {
/* 592 */     if (this.uattributes != null) {
/* 593 */       this.uattributes.clear();
/*     */     }
/* 595 */     if (this.ukeywords != null) {
/* 596 */       this.ukeywords.clear();
/*     */     }
/*     */ 
/* 599 */     StringTokenIterator localStringTokenIterator = new StringTokenIterator(paramString, "-");
/*     */ 
/* 602 */     while ((!localStringTokenIterator.isDone()) && 
/* 603 */       (UnicodeLocaleExtension.isAttribute(localStringTokenIterator.current())))
/*     */     {
/* 606 */       if (this.uattributes == null) {
/* 607 */         this.uattributes = new HashSet(4);
/*     */       }
/* 609 */       this.uattributes.add(new CaseInsensitiveString(localStringTokenIterator.current()));
/* 610 */       localStringTokenIterator.next();
/*     */     }
/*     */ 
/* 614 */     Object localObject = null;
/*     */ 
/* 616 */     int i = -1;
/* 617 */     int j = -1;
/* 618 */     while (!localStringTokenIterator.isDone())
/*     */     {
/*     */       String str;
/* 619 */       if (localObject != null) {
/* 620 */         if (UnicodeLocaleExtension.isKey(localStringTokenIterator.current()))
/*     */         {
/* 622 */           assert ((i == -1) || (j != -1));
/* 623 */           str = i == -1 ? "" : paramString.substring(i, j);
/* 624 */           if (this.ukeywords == null) {
/* 625 */             this.ukeywords = new HashMap(4);
/*     */           }
/* 627 */           this.ukeywords.put(localObject, str);
/*     */ 
/* 630 */           CaseInsensitiveString localCaseInsensitiveString = new CaseInsensitiveString(localStringTokenIterator.current());
/* 631 */           localObject = this.ukeywords.containsKey(localCaseInsensitiveString) ? null : localCaseInsensitiveString;
/* 632 */           i = j = -1;
/*     */         } else {
/* 634 */           if (i == -1) {
/* 635 */             i = localStringTokenIterator.currentStart();
/*     */           }
/* 637 */           j = localStringTokenIterator.currentEnd();
/*     */         }
/* 639 */       } else if (UnicodeLocaleExtension.isKey(localStringTokenIterator.current()))
/*     */       {
/* 642 */         localObject = new CaseInsensitiveString(localStringTokenIterator.current());
/* 643 */         if ((this.ukeywords != null) && (this.ukeywords.containsKey(localObject)))
/*     */         {
/* 645 */           localObject = null;
/*     */         }
/*     */       }
/*     */ 
/* 649 */       if (!localStringTokenIterator.hasNext()) {
/* 650 */         if (localObject == null)
/*     */           break;
/* 652 */         assert ((i == -1) || (j != -1));
/* 653 */         str = i == -1 ? "" : paramString.substring(i, j);
/* 654 */         if (this.ukeywords == null) {
/* 655 */           this.ukeywords = new HashMap(4);
/*     */         }
/* 657 */         this.ukeywords.put(localObject, str); break;
/*     */       }
/*     */ 
/* 662 */       localStringTokenIterator.next();
/*     */     }
/*     */   }
/*     */ 
/*     */   static final class CaseInsensitiveChar
/*     */   {
/*     */     private final char ch;
/*     */     private final char lowerCh;
/*     */ 
/*     */     private CaseInsensitiveChar(String paramString)
/*     */     {
/* 703 */       this(paramString.charAt(0));
/*     */     }
/*     */ 
/*     */     CaseInsensitiveChar(char paramChar) {
/* 707 */       this.ch = paramChar;
/* 708 */       this.lowerCh = LocaleUtils.toLower(this.ch);
/*     */     }
/*     */ 
/*     */     public char value() {
/* 712 */       return this.ch;
/*     */     }
/*     */ 
/*     */     public int hashCode()
/*     */     {
/* 717 */       return this.lowerCh;
/*     */     }
/*     */ 
/*     */     public boolean equals(Object paramObject)
/*     */     {
/* 722 */       if (this == paramObject) {
/* 723 */         return true;
/*     */       }
/* 725 */       if (!(paramObject instanceof CaseInsensitiveChar)) {
/* 726 */         return false;
/*     */       }
/* 728 */       return this.lowerCh == ((CaseInsensitiveChar)paramObject).lowerCh;
/*     */     }
/*     */   }
/*     */ 
/*     */   static final class CaseInsensitiveString
/*     */   {
/*     */     private final String str;
/*     */     private final String lowerStr;
/*     */ 
/*     */     CaseInsensitiveString(String paramString)
/*     */     {
/* 670 */       this.str = paramString;
/* 671 */       this.lowerStr = LocaleUtils.toLowerString(paramString);
/*     */     }
/*     */ 
/*     */     public String value() {
/* 675 */       return this.str;
/*     */     }
/*     */ 
/*     */     public int hashCode()
/*     */     {
/* 680 */       return this.lowerStr.hashCode();
/*     */     }
/*     */ 
/*     */     public boolean equals(Object paramObject)
/*     */     {
/* 685 */       if (this == paramObject) {
/* 686 */         return true;
/*     */       }
/* 688 */       if (!(paramObject instanceof CaseInsensitiveString)) {
/* 689 */         return false;
/*     */       }
/* 691 */       return this.lowerStr.equals(((CaseInsensitiveString)paramObject).lowerStr);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.util.locale.InternalLocaleBuilder
 * JD-Core Version:    0.6.2
 */