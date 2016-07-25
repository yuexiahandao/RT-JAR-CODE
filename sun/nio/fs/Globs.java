/*     */ package sun.nio.fs;
/*     */ 
/*     */ import java.util.regex.PatternSyntaxException;
/*     */ 
/*     */ public class Globs
/*     */ {
/*     */   private static final String regexMetaChars = ".^$+{[]|()";
/*     */   private static final String globMetaChars = "\\*?[{";
/*  43 */   private static char EOL = '\000';
/*     */ 
/*     */   private static boolean isRegexMeta(char paramChar)
/*     */   {
/*  37 */     return ".^$+{[]|()".indexOf(paramChar) != -1;
/*     */   }
/*     */ 
/*     */   private static boolean isGlobMeta(char paramChar) {
/*  41 */     return "\\*?[{".indexOf(paramChar) != -1;
/*     */   }
/*     */ 
/*     */   private static char next(String paramString, int paramInt)
/*     */   {
/*  46 */     if (paramInt < paramString.length()) {
/*  47 */       return paramString.charAt(paramInt);
/*     */     }
/*  49 */     return EOL;
/*     */   }
/*     */ 
/*     */   private static String toRegexPattern(String paramString, boolean paramBoolean)
/*     */   {
/*  58 */     int i = 0;
/*  59 */     StringBuilder localStringBuilder = new StringBuilder("^");
/*     */ 
/*  61 */     int j = 0;
/*  62 */     while (j < paramString.length()) {
/*  63 */       char c1 = paramString.charAt(j++);
/*  64 */       switch (c1)
/*     */       {
/*     */       case '\\':
/*  67 */         if (j == paramString.length()) {
/*  68 */           throw new PatternSyntaxException("No character to escape", paramString, j - 1);
/*     */         }
/*     */ 
/*  71 */         char c2 = paramString.charAt(j++);
/*  72 */         if ((isGlobMeta(c2)) || (isRegexMeta(c2))) {
/*  73 */           localStringBuilder.append('\\');
/*     */         }
/*  75 */         localStringBuilder.append(c2);
/*  76 */         break;
/*     */       case '/':
/*  78 */         if (paramBoolean)
/*  79 */           localStringBuilder.append("\\\\");
/*     */         else {
/*  81 */           localStringBuilder.append(c1);
/*     */         }
/*  83 */         break;
/*     */       case '[':
/*  86 */         if (paramBoolean)
/*  87 */           localStringBuilder.append("[[^\\\\]&&[");
/*     */         else {
/*  89 */           localStringBuilder.append("[[^/]&&[");
/*     */         }
/*  91 */         if (next(paramString, j) == '^')
/*     */         {
/*  93 */           localStringBuilder.append("\\^");
/*  94 */           j++;
/*     */         }
/*     */         else {
/*  97 */           if (next(paramString, j) == '!') {
/*  98 */             localStringBuilder.append('^');
/*  99 */             j++;
/*     */           }
/*     */ 
/* 102 */           if (next(paramString, j) == '-') {
/* 103 */             localStringBuilder.append('-');
/* 104 */             j++;
/*     */           }
/*     */         }
/* 107 */         int k = 0;
/* 108 */         char c3 = '\000';
/* 109 */         while (j < paramString.length()) {
/* 110 */           c1 = paramString.charAt(j++);
/* 111 */           if (c1 == ']') {
/*     */             break;
/*     */           }
/* 114 */           if ((c1 == '/') || ((paramBoolean) && (c1 == '\\'))) {
/* 115 */             throw new PatternSyntaxException("Explicit 'name separator' in class", paramString, j - 1);
/*     */           }
/*     */ 
/* 119 */           if ((c1 == '\\') || (c1 == '[') || ((c1 == '&') && (next(paramString, j) == '&')))
/*     */           {
/* 122 */             localStringBuilder.append('\\');
/*     */           }
/* 124 */           localStringBuilder.append(c1);
/*     */ 
/* 126 */           if (c1 == '-') {
/* 127 */             if (k == 0) {
/* 128 */               throw new PatternSyntaxException("Invalid range", paramString, j - 1);
/*     */             }
/*     */ 
/* 131 */             if (((c1 = next(paramString, j++)) == EOL) || (c1 == ']')) {
/*     */               break;
/*     */             }
/* 134 */             if (c1 < c3) {
/* 135 */               throw new PatternSyntaxException("Invalid range", paramString, j - 3);
/*     */             }
/*     */ 
/* 138 */             localStringBuilder.append(c1);
/* 139 */             k = 0;
/*     */           } else {
/* 141 */             k = 1;
/* 142 */             c3 = c1;
/*     */           }
/*     */         }
/* 145 */         if (c1 != ']') {
/* 146 */           throw new PatternSyntaxException("Missing ']", paramString, j - 1);
/*     */         }
/* 148 */         localStringBuilder.append("]]");
/* 149 */         break;
/*     */       case '{':
/* 151 */         if (i != 0) {
/* 152 */           throw new PatternSyntaxException("Cannot nest groups", paramString, j - 1);
/*     */         }
/*     */ 
/* 155 */         localStringBuilder.append("(?:(?:");
/* 156 */         i = 1;
/* 157 */         break;
/*     */       case '}':
/* 159 */         if (i != 0) {
/* 160 */           localStringBuilder.append("))");
/* 161 */           i = 0;
/*     */         } else {
/* 163 */           localStringBuilder.append('}');
/*     */         }
/* 165 */         break;
/*     */       case ',':
/* 167 */         if (i != 0)
/* 168 */           localStringBuilder.append(")|(?:");
/*     */         else {
/* 170 */           localStringBuilder.append(',');
/*     */         }
/* 172 */         break;
/*     */       case '*':
/* 174 */         if (next(paramString, j) == '*')
/*     */         {
/* 176 */           localStringBuilder.append(".*");
/* 177 */           j++;
/*     */         }
/* 180 */         else if (paramBoolean) {
/* 181 */           localStringBuilder.append("[^\\\\]*");
/*     */         } else {
/* 183 */           localStringBuilder.append("[^/]*");
/*     */         }
/*     */ 
/* 186 */         break;
/*     */       case '?':
/* 188 */         if (paramBoolean)
/* 189 */           localStringBuilder.append("[^\\\\]");
/*     */         else {
/* 191 */           localStringBuilder.append("[^/]");
/*     */         }
/* 193 */         break;
/*     */       default:
/* 196 */         if (isRegexMeta(c1)) {
/* 197 */           localStringBuilder.append('\\');
/*     */         }
/* 199 */         localStringBuilder.append(c1);
/*     */       }
/*     */     }
/*     */ 
/* 203 */     if (i != 0) {
/* 204 */       throw new PatternSyntaxException("Missing '}", paramString, j - 1);
/*     */     }
/*     */ 
/* 207 */     return '$';
/*     */   }
/*     */ 
/*     */   static String toUnixRegexPattern(String paramString) {
/* 211 */     return toRegexPattern(paramString, false);
/*     */   }
/*     */ 
/*     */   static String toWindowsRegexPattern(String paramString) {
/* 215 */     return toRegexPattern(paramString, true);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.nio.fs.Globs
 * JD-Core Version:    0.6.2
 */