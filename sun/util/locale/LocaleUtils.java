/*     */ package sun.util.locale;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ 
/*     */ public final class LocaleUtils
/*     */ {
/*     */   public static boolean caseIgnoreMatch(String paramString1, String paramString2)
/*     */   {
/*  51 */     if (paramString1 == paramString2) {
/*  52 */       return true;
/*     */     }
/*     */ 
/*  55 */     int i = paramString1.length();
/*  56 */     if (i != paramString2.length()) {
/*  57 */       return false;
/*     */     }
/*     */ 
/*  60 */     for (int j = 0; j < i; j++) {
/*  61 */       char c1 = paramString1.charAt(j);
/*  62 */       char c2 = paramString2.charAt(j);
/*  63 */       if ((c1 != c2) && (toLower(c1) != toLower(c2))) {
/*  64 */         return false;
/*     */       }
/*     */     }
/*  67 */     return true;
/*     */   }
/*     */ 
/*     */   static int caseIgnoreCompare(String paramString1, String paramString2) {
/*  71 */     if (paramString1 == paramString2) {
/*  72 */       return 0;
/*     */     }
/*  74 */     return toLowerString(paramString1).compareTo(toLowerString(paramString2));
/*     */   }
/*     */ 
/*     */   static char toUpper(char paramChar) {
/*  78 */     return isLower(paramChar) ? (char)(paramChar - ' ') : paramChar;
/*     */   }
/*     */ 
/*     */   static char toLower(char paramChar) {
/*  82 */     return isUpper(paramChar) ? (char)(paramChar + ' ') : paramChar;
/*     */   }
/*     */ 
/*     */   public static String toLowerString(String paramString)
/*     */   {
/*  89 */     int i = paramString.length();
/*  90 */     int j = 0;
/*  91 */     while ((j < i) && 
/*  92 */       (!isUpper(paramString.charAt(j)))) {
/*  91 */       j++;
/*     */     }
/*     */ 
/*  96 */     if (j == i) {
/*  97 */       return paramString;
/*     */     }
/*     */ 
/* 100 */     char[] arrayOfChar = new char[i];
/* 101 */     for (int k = 0; k < i; k++) {
/* 102 */       char c = paramString.charAt(k);
/* 103 */       arrayOfChar[k] = (k < j ? c : toLower(c));
/*     */     }
/* 105 */     return new String(arrayOfChar);
/*     */   }
/*     */ 
/*     */   static String toUpperString(String paramString) {
/* 109 */     int i = paramString.length();
/* 110 */     int j = 0;
/* 111 */     while ((j < i) && 
/* 112 */       (!isLower(paramString.charAt(j)))) {
/* 111 */       j++;
/*     */     }
/*     */ 
/* 116 */     if (j == i) {
/* 117 */       return paramString;
/*     */     }
/*     */ 
/* 120 */     char[] arrayOfChar = new char[i];
/* 121 */     for (int k = 0; k < i; k++) {
/* 122 */       char c = paramString.charAt(k);
/* 123 */       arrayOfChar[k] = (k < j ? c : toUpper(c));
/*     */     }
/* 125 */     return new String(arrayOfChar);
/*     */   }
/*     */ 
/*     */   static String toTitleString(String paramString)
/*     */   {
/*     */     int i;
/* 130 */     if ((i = paramString.length()) == 0) {
/* 131 */       return paramString;
/*     */     }
/* 133 */     int j = 0;
/* 134 */     if (!isLower(paramString.charAt(j)))
/*     */     {
/* 135 */       for (j = 1; (j < i) && 
/* 136 */         (!isUpper(paramString.charAt(j))); j++);
/*     */     }
/*     */ 
/* 141 */     if (j == i) {
/* 142 */       return paramString;
/*     */     }
/*     */ 
/* 145 */     char[] arrayOfChar = new char[i];
/* 146 */     for (int k = 0; k < i; k++) {
/* 147 */       char c = paramString.charAt(k);
/* 148 */       if ((k == 0) && (j == 0))
/* 149 */         arrayOfChar[k] = toUpper(c);
/* 150 */       else if (k < j)
/* 151 */         arrayOfChar[k] = c;
/*     */       else {
/* 153 */         arrayOfChar[k] = toLower(c);
/*     */       }
/*     */     }
/* 156 */     return new String(arrayOfChar);
/*     */   }
/*     */ 
/*     */   private static boolean isUpper(char paramChar) {
/* 160 */     return (paramChar >= 'A') && (paramChar <= 'Z');
/*     */   }
/*     */ 
/*     */   private static boolean isLower(char paramChar) {
/* 164 */     return (paramChar >= 'a') && (paramChar <= 'z');
/*     */   }
/*     */ 
/*     */   static boolean isAlpha(char paramChar) {
/* 168 */     return ((paramChar >= 'A') && (paramChar <= 'Z')) || ((paramChar >= 'a') && (paramChar <= 'z'));
/*     */   }
/*     */ 
/*     */   static boolean isAlphaString(String paramString) {
/* 172 */     int i = paramString.length();
/* 173 */     for (int j = 0; j < i; j++) {
/* 174 */       if (!isAlpha(paramString.charAt(j))) {
/* 175 */         return false;
/*     */       }
/*     */     }
/* 178 */     return true;
/*     */   }
/*     */ 
/*     */   static boolean isNumeric(char paramChar) {
/* 182 */     return (paramChar >= '0') && (paramChar <= '9');
/*     */   }
/*     */ 
/*     */   static boolean isNumericString(String paramString) {
/* 186 */     int i = paramString.length();
/* 187 */     for (int j = 0; j < i; j++) {
/* 188 */       if (!isNumeric(paramString.charAt(j))) {
/* 189 */         return false;
/*     */       }
/*     */     }
/* 192 */     return true;
/*     */   }
/*     */ 
/*     */   static boolean isAlphaNumeric(char paramChar) {
/* 196 */     return (isAlpha(paramChar)) || (isNumeric(paramChar));
/*     */   }
/*     */ 
/*     */   static boolean isAlphaNumericString(String paramString) {
/* 200 */     int i = paramString.length();
/* 201 */     for (int j = 0; j < i; j++) {
/* 202 */       if (!isAlphaNumeric(paramString.charAt(j))) {
/* 203 */         return false;
/*     */       }
/*     */     }
/* 206 */     return true;
/*     */   }
/*     */ 
/*     */   static boolean isEmpty(String paramString) {
/* 210 */     return (paramString == null) || (paramString.length() == 0);
/*     */   }
/*     */ 
/*     */   static boolean isEmpty(Set<?> paramSet) {
/* 214 */     return (paramSet == null) || (paramSet.isEmpty());
/*     */   }
/*     */ 
/*     */   static boolean isEmpty(Map<?, ?> paramMap) {
/* 218 */     return (paramMap == null) || (paramMap.isEmpty());
/*     */   }
/*     */ 
/*     */   static boolean isEmpty(List<?> paramList) {
/* 222 */     return (paramList == null) || (paramList.isEmpty());
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.util.locale.LocaleUtils
 * JD-Core Version:    0.6.2
 */