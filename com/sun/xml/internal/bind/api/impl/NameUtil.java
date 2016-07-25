/*     */ package com.sun.xml.internal.bind.api.impl;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ 
/*     */ class NameUtil
/*     */ {
/*     */   protected static final int UPPER_LETTER = 0;
/*     */   protected static final int LOWER_LETTER = 1;
/*     */   protected static final int OTHER_LETTER = 2;
/*     */   protected static final int DIGIT = 3;
/*     */   protected static final int OTHER = 4;
/* 137 */   private static final byte[] actionTable = new byte[25];
/*     */   private static final byte ACTION_CHECK_PUNCT = 0;
/*     */   private static final byte ACTION_CHECK_C2 = 1;
/*     */   private static final byte ACTION_BREAK = 2;
/*     */   private static final byte ACTION_NOBREAK = 3;
/*     */   private static HashSet<String> reservedKeywords;
/*     */ 
/*     */   protected boolean isPunct(char c)
/*     */   {
/*  45 */     return (c == '-') || (c == '.') || (c == ':') || (c == '_') || (c == '·') || (c == '·') || (c == '۝') || (c == '۞');
/*     */   }
/*     */ 
/*     */   protected static boolean isDigit(char c) {
/*  49 */     return ((c >= '0') && (c <= '9')) || (Character.isDigit(c));
/*     */   }
/*     */ 
/*     */   protected static boolean isUpper(char c) {
/*  53 */     return ((c >= 'A') && (c <= 'Z')) || (Character.isUpperCase(c));
/*     */   }
/*     */ 
/*     */   protected static boolean isLower(char c) {
/*  57 */     return ((c >= 'a') && (c <= 'z')) || (Character.isLowerCase(c));
/*     */   }
/*     */ 
/*     */   protected boolean isLetter(char c) {
/*  61 */     return ((c >= 'A') && (c <= 'Z')) || ((c >= 'a') && (c <= 'z')) || (Character.isLetter(c));
/*     */   }
/*     */ 
/*     */   private String toLowerCase(String s)
/*     */   {
/*  66 */     return s.toLowerCase(Locale.ENGLISH);
/*     */   }
/*     */ 
/*     */   private String toUpperCase(char c)
/*     */   {
/*  71 */     return String.valueOf(c).toUpperCase(Locale.ENGLISH);
/*     */   }
/*     */ 
/*     */   private String toUpperCase(String s)
/*     */   {
/*  76 */     return s.toUpperCase(Locale.ENGLISH);
/*     */   }
/*     */ 
/*     */   public String capitalize(String s)
/*     */   {
/*  84 */     if (!isLower(s.charAt(0)))
/*  85 */       return s;
/*  86 */     StringBuilder sb = new StringBuilder(s.length());
/*  87 */     sb.append(toUpperCase(s.charAt(0)));
/*  88 */     sb.append(toLowerCase(s.substring(1)));
/*  89 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   private int nextBreak(String s, int start)
/*     */   {
/*  94 */     int n = s.length();
/*     */ 
/*  96 */     char c1 = s.charAt(start);
/*  97 */     int t1 = classify(c1);
/*     */ 
/*  99 */     for (int i = start + 1; i < n; i++)
/*     */     {
/* 102 */       int t0 = t1;
/*     */ 
/* 104 */       c1 = s.charAt(i);
/* 105 */       t1 = classify(c1);
/*     */ 
/* 107 */       switch (actionTable[(t0 * 5 + t1)]) {
/*     */       case 0:
/* 109 */         if (isPunct(c1)) return i;
/*     */         break;
/*     */       case 1:
/* 112 */         if (i < n - 1) {
/* 113 */           char c2 = s.charAt(i + 1);
/* 114 */           if (isLower(c2))
/* 115 */             return i; 
/*     */         }
/* 116 */         break;
/*     */       case 2:
/* 119 */         return i;
/*     */       }
/*     */     }
/* 122 */     return -1;
/*     */   }
/*     */ 
/*     */   private static byte decideAction(int t0, int t1)
/*     */   {
/* 151 */     if ((t0 == 4) && (t1 == 4)) return 0;
/* 152 */     if (!xor(t0 == 3, t1 == 3)) return 2;
/* 153 */     if ((t0 == 1) && (t1 != 1)) return 2;
/* 154 */     if (!xor(t0 <= 2, t1 <= 2)) return 2;
/* 155 */     if (!xor(t0 == 2, t1 == 2)) return 2;
/*     */ 
/* 157 */     if ((t0 == 0) && (t1 == 0)) return 1;
/*     */ 
/* 159 */     return 3;
/*     */   }
/*     */ 
/*     */   private static boolean xor(boolean x, boolean y) {
/* 163 */     return ((x) && (y)) || ((!x) && (!y));
/*     */   }
/*     */ 
/*     */   protected int classify(char c0)
/*     */   {
/* 177 */     switch (Character.getType(c0)) { case 1:
/* 178 */       return 0;
/*     */     case 2:
/* 179 */       return 1;
/*     */     case 3:
/*     */     case 4:
/*     */     case 5:
/* 182 */       return 2;
/*     */     case 9:
/* 183 */       return 3;
/*     */     case 6:
/*     */     case 7:
/* 184 */     case 8: } return 4;
/*     */   }
/*     */ 
/*     */   public List<String> toWordList(String s)
/*     */   {
/* 199 */     ArrayList ss = new ArrayList();
/* 200 */     int n = s.length();
/* 201 */     for (int i = 0; i < n; )
/*     */     {
/* 204 */       while ((i < n) && 
/* 205 */         (isPunct(s.charAt(i))))
/*     */       {
/* 207 */         i++;
/*     */       }
/* 209 */       if (i >= n) {
/*     */         break;
/*     */       }
/* 212 */       int b = nextBreak(s, i);
/* 213 */       String w = b == -1 ? s.substring(i) : s.substring(i, b);
/* 214 */       ss.add(escape(capitalize(w)));
/* 215 */       if (b == -1) break;
/* 216 */       i = b;
/*     */     }
/*     */ 
/* 223 */     return ss;
/*     */   }
/*     */ 
/*     */   protected String toMixedCaseName(List<String> ss, boolean startUpper) {
/* 227 */     StringBuilder sb = new StringBuilder();
/* 228 */     if (!ss.isEmpty()) {
/* 229 */       sb.append(startUpper ? (String)ss.get(0) : toLowerCase((String)ss.get(0)));
/* 230 */       for (int i = 1; i < ss.size(); i++)
/* 231 */         sb.append((String)ss.get(i));
/*     */     }
/* 233 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   protected String toMixedCaseVariableName(String[] ss, boolean startUpper, boolean cdrUpper)
/*     */   {
/* 239 */     if (cdrUpper)
/* 240 */       for (int i = 1; i < ss.length; i++)
/* 241 */         ss[i] = capitalize(ss[i]);
/* 242 */     StringBuilder sb = new StringBuilder();
/* 243 */     if (ss.length > 0) {
/* 244 */       sb.append(startUpper ? ss[0] : toLowerCase(ss[0]));
/* 245 */       for (int i = 1; i < ss.length; i++)
/* 246 */         sb.append(ss[i]);
/*     */     }
/* 248 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   public String toConstantName(String s)
/*     */   {
/* 260 */     return toConstantName(toWordList(s));
/*     */   }
/*     */ 
/*     */   public String toConstantName(List<String> ss)
/*     */   {
/* 271 */     StringBuilder sb = new StringBuilder();
/* 272 */     if (!ss.isEmpty()) {
/* 273 */       sb.append(toUpperCase((String)ss.get(0)));
/* 274 */       for (int i = 1; i < ss.size(); i++) {
/* 275 */         sb.append('_');
/* 276 */         sb.append(toUpperCase((String)ss.get(i)));
/*     */       }
/*     */     }
/* 279 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   public static void escape(StringBuilder sb, String s, int start)
/*     */   {
/* 298 */     int n = s.length();
/* 299 */     for (int i = start; i < n; i++) {
/* 300 */       char c = s.charAt(i);
/* 301 */       if (Character.isJavaIdentifierPart(c)) {
/* 302 */         sb.append(c);
/*     */       } else {
/* 304 */         sb.append('_');
/* 305 */         if (c <= '\017') sb.append("000");
/* 306 */         else if (c <= 'ÿ') sb.append("00");
/* 307 */         else if (c <= '࿿') sb.append('0');
/* 308 */         sb.append(Integer.toString(c, 16));
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private static String escape(String s)
/*     */   {
/* 318 */     int n = s.length();
/* 319 */     for (int i = 0; i < n; i++)
/* 320 */       if (!Character.isJavaIdentifierPart(s.charAt(i))) {
/* 321 */         StringBuilder sb = new StringBuilder(s.substring(0, i));
/* 322 */         escape(sb, s, i);
/* 323 */         return sb.toString();
/*     */       }
/* 325 */     return s;
/*     */   }
/*     */ 
/*     */   public static boolean isJavaIdentifier(String s)
/*     */   {
/* 333 */     if (s.length() == 0) return false;
/* 334 */     if (reservedKeywords.contains(s)) return false;
/*     */ 
/* 336 */     if (!Character.isJavaIdentifierStart(s.charAt(0))) return false;
/*     */ 
/* 338 */     for (int i = 1; i < s.length(); i++) {
/* 339 */       if (!Character.isJavaIdentifierPart(s.charAt(i)))
/* 340 */         return false;
/*     */     }
/* 342 */     return true;
/*     */   }
/*     */ 
/*     */   public static boolean isJavaPackageName(String s)
/*     */   {
/* 349 */     while (s.length() != 0) {
/* 350 */       int idx = s.indexOf('.');
/* 351 */       if (idx == -1) idx = s.length();
/* 352 */       if (!isJavaIdentifier(s.substring(0, idx))) {
/* 353 */         return false;
/*     */       }
/* 355 */       s = s.substring(idx);
/* 356 */       if (s.length() != 0) s = s.substring(1);
/*     */     }
/* 358 */     return true;
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/* 168 */     for (int t0 = 0; t0 < 5; t0++) {
/* 169 */       for (int t1 = 0; t1 < 5; t1++) {
/* 170 */         actionTable[(t0 * 5 + t1)] = decideAction(t0, t1);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 363 */     reservedKeywords = new HashSet();
/*     */ 
/* 367 */     String[] words = { "abstract", "boolean", "break", "byte", "case", "catch", "char", "class", "const", "continue", "default", "do", "double", "else", "extends", "final", "finally", "float", "for", "goto", "if", "implements", "import", "instanceof", "int", "interface", "long", "native", "new", "package", "private", "protected", "public", "return", "short", "static", "strictfp", "super", "switch", "synchronized", "this", "throw", "throws", "transient", "try", "void", "volatile", "while", "true", "false", "null", "assert", "enum" };
/*     */ 
/* 428 */     for (String word : words)
/* 429 */       reservedKeywords.add(word);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.api.impl.NameUtil
 * JD-Core Version:    0.6.2
 */