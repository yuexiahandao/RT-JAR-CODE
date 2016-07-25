/*     */ package javax.lang.model;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ 
/*     */ public enum SourceVersion
/*     */ {
/*  64 */   RELEASE_0, 
/*     */ 
/*  72 */   RELEASE_1, 
/*     */ 
/*  83 */   RELEASE_2, 
/*     */ 
/*  91 */   RELEASE_3, 
/*     */ 
/*  99 */   RELEASE_4, 
/*     */ 
/* 111 */   RELEASE_5, 
/*     */ 
/* 119 */   RELEASE_6, 
/*     */ 
/* 127 */   RELEASE_7;
/*     */ 
/*     */   private static final SourceVersion latestSupported;
/* 244 */   private static final Set<String> keywords = Collections.unmodifiableSet(localHashSet);
/*     */ 
/*     */   public static SourceVersion latest()
/*     */   {
/* 138 */     return RELEASE_7;
/*     */   }
/*     */ 
/*     */   private static SourceVersion getLatestSupported()
/*     */   {
/*     */     try
/*     */     {
/* 145 */       String str = System.getProperty("java.specification.version");
/* 146 */       if ("1.7".equals(str))
/* 147 */         return RELEASE_7;
/* 148 */       if ("1.6".equals(str))
/* 149 */         return RELEASE_6;
/*     */     } catch (SecurityException localSecurityException) {
/*     */     }
/* 152 */     return RELEASE_5;
/*     */   }
/*     */ 
/*     */   public static SourceVersion latestSupported()
/*     */   {
/* 163 */     return latestSupported;
/*     */   }
/*     */ 
/*     */   public static boolean isIdentifier(CharSequence paramCharSequence)
/*     */   {
/* 184 */     String str = paramCharSequence.toString();
/*     */ 
/* 186 */     if (str.length() == 0) {
/* 187 */       return false;
/*     */     }
/* 189 */     int i = str.codePointAt(0);
/* 190 */     if (!Character.isJavaIdentifierStart(i)) {
/* 191 */       return false;
/*     */     }
/* 193 */     int j = Character.charCount(i);
/*     */ 
/* 195 */     for (; j < str.length(); 
/* 195 */       j += Character.charCount(i)) {
/* 196 */       i = str.codePointAt(j);
/* 197 */       if (!Character.isJavaIdentifierPart(i)) {
/* 198 */         return false;
/*     */       }
/*     */     }
/* 201 */     return true;
/*     */   }
/*     */ 
/*     */   public static boolean isName(CharSequence paramCharSequence)
/*     */   {
/* 216 */     String str1 = paramCharSequence.toString();
/*     */ 
/* 218 */     for (String str2 : str1.split("\\.", -1)) {
/* 219 */       if ((!isIdentifier(str2)) || (isKeyword(str2)))
/* 220 */         return false;
/*     */     }
/* 222 */     return true;
/*     */   }
/*     */ 
/*     */   public static boolean isKeyword(CharSequence paramCharSequence)
/*     */   {
/* 255 */     String str = paramCharSequence.toString();
/* 256 */     return keywords.contains(str);
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/* 141 */     latestSupported = getLatestSupported();
/*     */ 
/* 227 */     HashSet localHashSet = new HashSet();
/* 228 */     String[] arrayOfString1 = { "abstract", "continue", "for", "new", "switch", "assert", "default", "if", "package", "synchronized", "boolean", "do", "goto", "private", "this", "break", "double", "implements", "protected", "throw", "byte", "else", "import", "public", "throws", "case", "enum", "instanceof", "return", "transient", "catch", "extends", "int", "short", "try", "char", "final", "interface", "static", "void", "class", "finally", "long", "strictfp", "volatile", "const", "float", "native", "super", "while", "null", "true", "false" };
/*     */ 
/* 242 */     for (String str : arrayOfString1)
/* 243 */       localHashSet.add(str);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.lang.model.SourceVersion
 * JD-Core Version:    0.6.2
 */