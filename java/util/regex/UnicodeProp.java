/*     */ package java.util.regex;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Locale;
/*     */ 
/*     */  enum UnicodeProp
/*     */ {
/*  33 */   ALPHABETIC, 
/*     */ 
/*  39 */   LETTER, 
/*     */ 
/*  45 */   IDEOGRAPHIC, 
/*     */ 
/*  51 */   LOWERCASE, 
/*     */ 
/*  57 */   UPPERCASE, 
/*     */ 
/*  63 */   TITLECASE, 
/*     */ 
/*  69 */   WHITE_SPACE, 
/*     */ 
/*  79 */   CONTROL, 
/*     */ 
/*  86 */   PUNCTUATION, 
/*     */ 
/* 100 */   HEX_DIGIT, 
/*     */ 
/* 114 */   ASSIGNED, 
/*     */ 
/* 120 */   NONCHARACTER_CODE_POINT, 
/*     */ 
/* 127 */   DIGIT, 
/*     */ 
/* 134 */   ALNUM, 
/*     */ 
/* 142 */   BLANK, 
/*     */ 
/* 153 */   GRAPH, 
/*     */ 
/* 170 */   PRINT, 
/*     */ 
/* 179 */   WORD;
/*     */ 
/*     */   private static final HashMap<String, String> posix;
/*     */   private static final HashMap<String, String> aliases;
/*     */ 
/*     */   public static UnicodeProp forName(String paramString)
/*     */   {
/* 218 */     paramString = paramString.toUpperCase(Locale.ENGLISH);
/* 219 */     String str = (String)aliases.get(paramString);
/* 220 */     if (str != null)
/* 221 */       paramString = str;
/*     */     try {
/* 223 */       return valueOf(paramString); } catch (IllegalArgumentException localIllegalArgumentException) {
/*     */     }
/* 225 */     return null;
/*     */   }
/*     */ 
/*     */   public static UnicodeProp forPOSIXName(String paramString) {
/* 229 */     paramString = (String)posix.get(paramString.toUpperCase(Locale.ENGLISH));
/* 230 */     if (paramString == null)
/* 231 */       return null;
/* 232 */     return valueOf(paramString);
/*     */   }
/*     */ 
/*     */   public abstract boolean is(int paramInt);
/*     */ 
/*     */   static
/*     */   {
/* 196 */     posix = new HashMap();
/* 197 */     aliases = new HashMap();
/*     */ 
/* 199 */     posix.put("ALPHA", "ALPHABETIC");
/* 200 */     posix.put("LOWER", "LOWERCASE");
/* 201 */     posix.put("UPPER", "UPPERCASE");
/* 202 */     posix.put("SPACE", "WHITE_SPACE");
/* 203 */     posix.put("PUNCT", "PUNCTUATION");
/* 204 */     posix.put("XDIGIT", "HEX_DIGIT");
/* 205 */     posix.put("ALNUM", "ALNUM");
/* 206 */     posix.put("CNTRL", "CONTROL");
/* 207 */     posix.put("DIGIT", "DIGIT");
/* 208 */     posix.put("BLANK", "BLANK");
/* 209 */     posix.put("GRAPH", "GRAPH");
/* 210 */     posix.put("PRINT", "PRINT");
/*     */ 
/* 212 */     aliases.put("WHITESPACE", "WHITE_SPACE");
/* 213 */     aliases.put("HEXDIGIT", "HEX_DIGIT");
/* 214 */     aliases.put("NONCHARACTERCODEPOINT", "NONCHARACTER_CODE_POINT");
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.regex.UnicodeProp
 * JD-Core Version:    0.6.2
 */