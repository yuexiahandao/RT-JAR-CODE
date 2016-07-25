/*     */ package java.lang;
/*     */ 
/*     */ import java.text.BreakIterator;
/*     */ import java.util.HashSet;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Iterator;
/*     */ import java.util.Locale;
/*     */ import sun.text.Normalizer;
/*     */ 
/*     */ final class ConditionalSpecialCasing
/*     */ {
/*     */   static final int FINAL_CASED = 1;
/*     */   static final int AFTER_SOFT_DOTTED = 2;
/*     */   static final int MORE_ABOVE = 3;
/*     */   static final int AFTER_I = 4;
/*     */   static final int NOT_BEFORE_DOT = 5;
/*     */   static final int COMBINING_CLASS_ABOVE = 230;
/*  60 */   static Entry[] entry = { new Entry(931, new char[] { 'ς' }, new char[] { 'Σ' }, null, 1), new Entry(304, new char[] { 'i', '̇' }, new char[] { 'İ' }, null, 0), new Entry(775, new char[] { '̇' }, new char[0], "lt", 2), new Entry(73, new char[] { 'i', '̇' }, new char[] { 'I' }, "lt", 3), new Entry(74, new char[] { 'j', '̇' }, new char[] { 'J' }, "lt", 3), new Entry(302, new char[] { 'į', '̇' }, new char[] { 'Į' }, "lt", 3), new Entry(204, new char[] { 'i', '̇', '̀' }, new char[] { 'Ì' }, "lt", 0), new Entry(205, new char[] { 'i', '̇', '́' }, new char[] { 'Í' }, "lt", 0), new Entry(296, new char[] { 'i', '̇', '̃' }, new char[] { 'Ĩ' }, "lt", 0), new Entry(304, new char[] { 'i' }, new char[] { 'İ' }, "tr", 0), new Entry(304, new char[] { 'i' }, new char[] { 'İ' }, "az", 0), new Entry(775, new char[0], new char[] { '̇' }, "tr", 4), new Entry(775, new char[0], new char[] { '̇' }, "az", 4), new Entry(73, new char[] { 'ı' }, new char[] { 'I' }, "tr", 5), new Entry(73, new char[] { 'ı' }, new char[] { 'I' }, "az", 5), new Entry(105, new char[] { 'i' }, new char[] { 'İ' }, "tr", 0), new Entry(105, new char[] { 'i' }, new char[] { 'İ' }, "az", 0) };
/*     */ 
/*  92 */   static Hashtable entryTable = new Hashtable();
/*     */ 
/*     */   static int toLowerCaseEx(String paramString, int paramInt, Locale paramLocale)
/*     */   {
/* 108 */     char[] arrayOfChar = lookUpTable(paramString, paramInt, paramLocale, true);
/*     */ 
/* 110 */     if (arrayOfChar != null) {
/* 111 */       if (arrayOfChar.length == 1) {
/* 112 */         return arrayOfChar[0];
/*     */       }
/* 114 */       return -1;
/*     */     }
/*     */ 
/* 118 */     return Character.toLowerCase(paramString.codePointAt(paramInt));
/*     */   }
/*     */ 
/*     */   static int toUpperCaseEx(String paramString, int paramInt, Locale paramLocale)
/*     */   {
/* 123 */     char[] arrayOfChar = lookUpTable(paramString, paramInt, paramLocale, false);
/*     */ 
/* 125 */     if (arrayOfChar != null) {
/* 126 */       if (arrayOfChar.length == 1) {
/* 127 */         return arrayOfChar[0];
/*     */       }
/* 129 */       return -1;
/*     */     }
/*     */ 
/* 133 */     return Character.toUpperCaseEx(paramString.codePointAt(paramInt));
/*     */   }
/*     */ 
/*     */   static char[] toLowerCaseCharArray(String paramString, int paramInt, Locale paramLocale)
/*     */   {
/* 138 */     return lookUpTable(paramString, paramInt, paramLocale, true);
/*     */   }
/*     */ 
/*     */   static char[] toUpperCaseCharArray(String paramString, int paramInt, Locale paramLocale) {
/* 142 */     char[] arrayOfChar = lookUpTable(paramString, paramInt, paramLocale, false);
/* 143 */     if (arrayOfChar != null) {
/* 144 */       return arrayOfChar;
/*     */     }
/* 146 */     return Character.toUpperCaseCharArray(paramString.codePointAt(paramInt));
/*     */   }
/*     */ 
/*     */   private static char[] lookUpTable(String paramString, int paramInt, Locale paramLocale, boolean paramBoolean)
/*     */   {
/* 151 */     HashSet localHashSet = (HashSet)entryTable.get(new Integer(paramString.codePointAt(paramInt)));
/* 152 */     char[] arrayOfChar = null;
/*     */ 
/* 154 */     if (localHashSet != null) {
/* 155 */       Iterator localIterator = localHashSet.iterator();
/* 156 */       String str1 = paramLocale.getLanguage();
/* 157 */       while (localIterator.hasNext()) {
/* 158 */         Entry localEntry = (Entry)localIterator.next();
/* 159 */         String str2 = localEntry.getLanguage();
/* 160 */         if (((str2 == null) || (str2.equals(str1))) && (isConditionMet(paramString, paramInt, paramLocale, localEntry.getCondition())))
/*     */         {
/* 162 */           arrayOfChar = paramBoolean ? localEntry.getLowerCase() : localEntry.getUpperCase();
/* 163 */           if (str2 != null)
/*     */           {
/*     */             break;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 170 */     return arrayOfChar;
/*     */   }
/*     */ 
/*     */   private static boolean isConditionMet(String paramString, int paramInt1, Locale paramLocale, int paramInt2) {
/* 174 */     switch (paramInt2) {
/*     */     case 1:
/* 176 */       return isFinalCased(paramString, paramInt1, paramLocale);
/*     */     case 2:
/* 179 */       return isAfterSoftDotted(paramString, paramInt1);
/*     */     case 3:
/* 182 */       return isMoreAbove(paramString, paramInt1);
/*     */     case 4:
/* 185 */       return isAfterI(paramString, paramInt1);
/*     */     case 5:
/* 188 */       return !isBeforeDot(paramString, paramInt1);
/*     */     }
/*     */ 
/* 191 */     return true;
/*     */   }
/*     */ 
/*     */   private static boolean isFinalCased(String paramString, int paramInt, Locale paramLocale)
/*     */   {
/* 206 */     BreakIterator localBreakIterator = BreakIterator.getWordInstance(paramLocale);
/* 207 */     localBreakIterator.setText(paramString);
/*     */     int i;
/* 211 */     for (int j = paramInt; (j >= 0) && (!localBreakIterator.isBoundary(j)); 
/* 212 */       j -= Character.charCount(i))
/*     */     {
/* 214 */       i = paramString.codePointBefore(j);
/* 215 */       if (isCased(i))
/*     */       {
/* 217 */         int k = paramString.length();
/*     */ 
/* 219 */         j = paramInt + Character.charCount(paramString.codePointAt(paramInt));
/*     */ 
/* 221 */         for (; (j < k) && (!localBreakIterator.isBoundary(j)); 
/* 221 */           j += Character.charCount(i))
/*     */         {
/* 223 */           i = paramString.codePointAt(j);
/* 224 */           if (isCased(i)) {
/* 225 */             return false;
/*     */           }
/*     */         }
/*     */ 
/* 229 */         return true;
/*     */       }
/*     */     }
/*     */ 
/* 233 */     return false;
/*     */   }
/*     */ 
/*     */   private static boolean isAfterI(String paramString, int paramInt)
/*     */   {
/*     */     int i;
/* 250 */     for (int k = paramInt; k > 0; k -= Character.charCount(i))
/*     */     {
/* 252 */       i = paramString.codePointBefore(k);
/*     */ 
/* 254 */       if (i == 73) {
/* 255 */         return true;
/*     */       }
/* 257 */       int j = Normalizer.getCombiningClass(i);
/* 258 */       if ((j == 0) || (j == 230)) {
/* 259 */         return false;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 264 */     return false;
/*     */   }
/*     */ 
/*     */   private static boolean isAfterSoftDotted(String paramString, int paramInt)
/*     */   {
/*     */     int i;
/* 282 */     for (int k = paramInt; k > 0; k -= Character.charCount(i))
/*     */     {
/* 284 */       i = paramString.codePointBefore(k);
/*     */ 
/* 286 */       if (isSoftDotted(i)) {
/* 287 */         return true;
/*     */       }
/* 289 */       int j = Normalizer.getCombiningClass(i);
/* 290 */       if ((j == 0) || (j == 230)) {
/* 291 */         return false;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 296 */     return false;
/*     */   }
/*     */ 
/*     */   private static boolean isMoreAbove(String paramString, int paramInt)
/*     */   {
/* 311 */     int k = paramString.length();
/*     */ 
/* 314 */     int m = paramInt + Character.charCount(paramString.codePointAt(paramInt));
/*     */     int i;
/* 315 */     for (; m < k; m += Character.charCount(i))
/*     */     {
/* 317 */       i = paramString.codePointAt(m);
/* 318 */       int j = Normalizer.getCombiningClass(i);
/*     */ 
/* 320 */       if (j == 230)
/* 321 */         return true;
/* 322 */       if (j == 0) {
/* 323 */         return false;
/*     */       }
/*     */     }
/*     */ 
/* 327 */     return false;
/*     */   }
/*     */ 
/*     */   private static boolean isBeforeDot(String paramString, int paramInt)
/*     */   {
/* 344 */     int k = paramString.length();
/*     */ 
/* 347 */     int m = paramInt + Character.charCount(paramString.codePointAt(paramInt));
/*     */     int i;
/* 348 */     for (; m < k; m += Character.charCount(i))
/*     */     {
/* 350 */       i = paramString.codePointAt(m);
/*     */ 
/* 352 */       if (i == 775) {
/* 353 */         return true;
/*     */       }
/* 355 */       int j = Normalizer.getCombiningClass(i);
/* 356 */       if ((j == 0) || (j == 230)) {
/* 357 */         return false;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 362 */     return false;
/*     */   }
/*     */ 
/*     */   private static boolean isCased(int paramInt)
/*     */   {
/* 376 */     int i = Character.getType(paramInt);
/* 377 */     if ((i == 2) || (i == 1) || (i == 3))
/*     */     {
/* 380 */       return true;
/*     */     }
/*     */ 
/* 384 */     if ((paramInt >= 688) && (paramInt <= 696))
/*     */     {
/* 386 */       return true;
/* 387 */     }if ((paramInt >= 704) && (paramInt <= 705))
/*     */     {
/* 389 */       return true;
/* 390 */     }if ((paramInt >= 736) && (paramInt <= 740))
/*     */     {
/* 392 */       return true;
/* 393 */     }if (paramInt == 837)
/*     */     {
/* 395 */       return true;
/* 396 */     }if (paramInt == 890)
/*     */     {
/* 398 */       return true;
/* 399 */     }if ((paramInt >= 7468) && (paramInt <= 7521))
/*     */     {
/* 401 */       return true;
/* 402 */     }if ((paramInt >= 8544) && (paramInt <= 8575))
/*     */     {
/* 405 */       return true;
/* 406 */     }if ((paramInt >= 9398) && (paramInt <= 9449))
/*     */     {
/* 409 */       return true;
/*     */     }
/* 411 */     return false;
/*     */   }
/*     */ 
/*     */   private static boolean isSoftDotted(int paramInt)
/*     */   {
/* 417 */     switch (paramInt) {
/*     */     case 105:
/*     */     case 106:
/*     */     case 303:
/*     */     case 616:
/*     */     case 1110:
/*     */     case 1112:
/*     */     case 7522:
/*     */     case 7725:
/*     */     case 7883:
/*     */     case 8305:
/* 428 */       return true;
/*     */     }
/* 430 */     return false;
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  95 */     for (int i = 0; i < entry.length; i++) {
/*  96 */       Entry localEntry = entry[i];
/*  97 */       Integer localInteger = new Integer(localEntry.getCodePoint());
/*  98 */       HashSet localHashSet = (HashSet)entryTable.get(localInteger);
/*  99 */       if (localHashSet == null) {
/* 100 */         localHashSet = new HashSet();
/*     */       }
/* 102 */       localHashSet.add(localEntry);
/* 103 */       entryTable.put(localInteger, localHashSet);
/*     */     }
/*     */   }
/*     */ 
/*     */   static class Entry
/*     */   {
/*     */     int ch;
/*     */     char[] lower;
/*     */     char[] upper;
/*     */     String lang;
/*     */     int condition;
/*     */ 
/*     */     Entry(int paramInt1, char[] paramArrayOfChar1, char[] paramArrayOfChar2, String paramString, int paramInt2)
/*     */     {
/* 445 */       this.ch = paramInt1;
/* 446 */       this.lower = paramArrayOfChar1;
/* 447 */       this.upper = paramArrayOfChar2;
/* 448 */       this.lang = paramString;
/* 449 */       this.condition = paramInt2;
/*     */     }
/*     */ 
/*     */     int getCodePoint() {
/* 453 */       return this.ch;
/*     */     }
/*     */ 
/*     */     char[] getLowerCase() {
/* 457 */       return this.lower;
/*     */     }
/*     */ 
/*     */     char[] getUpperCase() {
/* 461 */       return this.upper;
/*     */     }
/*     */ 
/*     */     String getLanguage() {
/* 465 */       return this.lang;
/*     */     }
/*     */ 
/*     */     int getCondition() {
/* 469 */       return this.condition;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.lang.ConditionalSpecialCasing
 * JD-Core Version:    0.6.2
 */