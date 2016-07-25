/*     */ package com.sun.xml.internal.bind.api.impl;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.StringTokenizer;
/*     */ 
/*     */ public abstract interface NameConverter
/*     */ {
/*  93 */   public static final NameConverter standard = new Standard();
/*     */ 
/* 249 */   public static final NameConverter jaxrpcCompatible = new Standard() {
/*     */     protected boolean isPunct(char c) {
/* 251 */       return (c == '.') || (c == '-') || (c == ';') || (c == '·') || (c == '·') || (c == '۝') || (c == '۞');
/*     */     }
/*     */ 
/*     */     protected boolean isLetter(char c) {
/* 255 */       return (super.isLetter(c)) || (c == '_');
/*     */     }
/*     */ 
/*     */     protected int classify(char c0) {
/* 259 */       if (c0 == '_') return 2;
/* 260 */       return super.classify(c0);
/*     */     }
/* 249 */   };
/*     */ 
/* 267 */   public static final NameConverter smart = new Standard() {
/*     */     public String toConstantName(String token) {
/* 269 */       String name = super.toConstantName(token);
/* 270 */       if (NameUtil.isJavaIdentifier(name)) {
/* 271 */         return name;
/*     */       }
/* 273 */       return '_' + name;
/*     */     }
/* 267 */   };
/*     */ 
/*     */   public abstract String toClassName(String paramString);
/*     */ 
/*     */   public abstract String toInterfaceName(String paramString);
/*     */ 
/*     */   public abstract String toPropertyName(String paramString);
/*     */ 
/*     */   public abstract String toConstantName(String paramString);
/*     */ 
/*     */   public abstract String toVariableName(String paramString);
/*     */ 
/*     */   public abstract String toPackageName(String paramString);
/*     */ 
/*     */   public static class Standard extends NameUtil
/*     */     implements NameConverter
/*     */   {
/*     */     public String toClassName(String s)
/*     */     {
/*  97 */       return toMixedCaseName(toWordList(s), true);
/*     */     }
/*     */     public String toVariableName(String s) {
/* 100 */       return toMixedCaseName(toWordList(s), false);
/*     */     }
/*     */     public String toInterfaceName(String token) {
/* 103 */       return toClassName(token);
/*     */     }
/*     */     public String toPropertyName(String s) {
/* 106 */       String prop = toClassName(s);
/*     */ 
/* 109 */       if (prop.equals("Class"))
/* 110 */         prop = "Clazz";
/* 111 */       return prop;
/*     */     }
/*     */     public String toConstantName(String token) {
/* 114 */       return super.toConstantName(token);
/*     */     }
/*     */ 
/*     */     public String toPackageName(String nsUri)
/*     */     {
/* 126 */       int idx = nsUri.indexOf(':');
/* 127 */       String scheme = "";
/* 128 */       if (idx >= 0) {
/* 129 */         scheme = nsUri.substring(0, idx);
/* 130 */         if ((scheme.equalsIgnoreCase("http")) || (scheme.equalsIgnoreCase("urn"))) {
/* 131 */           nsUri = nsUri.substring(idx + 1);
/*     */         }
/*     */       }
/*     */ 
/* 135 */       idx = nsUri.indexOf("#");
/* 136 */       if (idx >= 0) {
/* 137 */         nsUri = nsUri.substring(0, idx);
/*     */       }
/*     */ 
/* 140 */       ArrayList tokens = tokenize(nsUri, "/: ");
/* 141 */       if (tokens.size() == 0) {
/* 142 */         return null;
/*     */       }
/*     */ 
/* 146 */       if (tokens.size() > 1)
/*     */       {
/* 150 */         String lastToken = (String)tokens.get(tokens.size() - 1);
/* 151 */         idx = lastToken.lastIndexOf('.');
/* 152 */         if (idx > 0) {
/* 153 */           lastToken = lastToken.substring(0, idx);
/* 154 */           tokens.set(tokens.size() - 1, lastToken);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 159 */       String domain = (String)tokens.get(0);
/* 160 */       idx = domain.indexOf(':');
/* 161 */       if (idx >= 0) domain = domain.substring(0, idx);
/* 162 */       ArrayList r = reverse(tokenize(domain, scheme.equals("urn") ? ".-" : "."));
/* 163 */       if (((String)r.get(r.size() - 1)).equalsIgnoreCase("www"))
/*     */       {
/* 165 */         r.remove(r.size() - 1);
/*     */       }
/*     */ 
/* 169 */       tokens.addAll(1, r);
/* 170 */       tokens.remove(0);
/*     */ 
/* 173 */       for (int i = 0; i < tokens.size(); i++)
/*     */       {
/* 176 */         String token = (String)tokens.get(i);
/* 177 */         token = removeIllegalIdentifierChars(token);
/*     */ 
/* 180 */         if (!NameUtil.isJavaIdentifier(token.toLowerCase())) {
/* 181 */           token = '_' + token;
/*     */         }
/*     */ 
/* 184 */         tokens.set(i, token.toLowerCase());
/*     */       }
/*     */ 
/* 188 */       return combine(tokens, '.');
/*     */     }
/*     */ 
/*     */     private static String removeIllegalIdentifierChars(String token)
/*     */     {
/* 193 */       StringBuffer newToken = new StringBuffer();
/* 194 */       for (int i = 0; i < token.length(); i++) {
/* 195 */         char c = token.charAt(i);
/*     */ 
/* 197 */         if ((i == 0) && (!Character.isJavaIdentifierStart(c)))
/*     */         {
/* 199 */           newToken.append('_').append(c);
/* 200 */         } else if (!Character.isJavaIdentifierPart(c))
/*     */         {
/* 202 */           newToken.append('_');
/*     */         }
/*     */         else {
/* 205 */           newToken.append(c);
/*     */         }
/*     */       }
/* 208 */       return newToken.toString();
/*     */     }
/*     */ 
/*     */     private static ArrayList<String> tokenize(String str, String sep)
/*     */     {
/* 213 */       StringTokenizer tokens = new StringTokenizer(str, sep);
/* 214 */       ArrayList r = new ArrayList();
/*     */ 
/* 216 */       while (tokens.hasMoreTokens()) {
/* 217 */         r.add(tokens.nextToken());
/*     */       }
/* 219 */       return r;
/*     */     }
/*     */ 
/*     */     private static <T> ArrayList<T> reverse(List<T> a) {
/* 223 */       ArrayList r = new ArrayList();
/*     */ 
/* 225 */       for (int i = a.size() - 1; i >= 0; i--) {
/* 226 */         r.add(a.get(i));
/*     */       }
/* 228 */       return r;
/*     */     }
/*     */ 
/*     */     private static String combine(List r, char sep) {
/* 232 */       StringBuilder buf = new StringBuilder(r.get(0).toString());
/*     */ 
/* 234 */       for (int i = 1; i < r.size(); i++) {
/* 235 */         buf.append(sep);
/* 236 */         buf.append(r.get(i));
/*     */       }
/*     */ 
/* 239 */       return buf.toString();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.api.impl.NameConverter
 * JD-Core Version:    0.6.2
 */