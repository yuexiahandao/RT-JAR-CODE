/*     */ package com.sun.xml.internal.bind.v2.schemagen;
/*     */ 
/*     */ public final class Util
/*     */ {
/*     */   public static String escapeURI(String s)
/*     */   {
/*  47 */     StringBuilder sb = new StringBuilder();
/*  48 */     for (int i = 0; i < s.length(); i++) {
/*  49 */       char c = s.charAt(i);
/*  50 */       if (Character.isSpaceChar(c))
/*  51 */         sb.append("%20");
/*     */       else {
/*  53 */         sb.append(c);
/*     */       }
/*     */     }
/*  56 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   public static String getParentUriPath(String uriPath)
/*     */   {
/*  66 */     int idx = uriPath.lastIndexOf('/');
/*     */ 
/*  68 */     if (uriPath.endsWith("/")) {
/*  69 */       uriPath = uriPath.substring(0, idx);
/*  70 */       idx = uriPath.lastIndexOf('/');
/*     */     }
/*     */ 
/*  73 */     return uriPath.substring(0, idx) + "/";
/*     */   }
/*     */ 
/*     */   public static String normalizeUriPath(String uriPath)
/*     */   {
/*  89 */     if (uriPath.endsWith("/")) {
/*  90 */       return uriPath;
/*     */     }
/*     */ 
/*  94 */     int idx = uriPath.lastIndexOf('/');
/*  95 */     return uriPath.substring(0, idx + 1);
/*     */   }
/*     */ 
/*     */   public static boolean equalsIgnoreCase(String s, String t)
/*     */   {
/* 107 */     if (s == t) return true;
/* 108 */     if ((s != null) && (t != null)) {
/* 109 */       return s.equalsIgnoreCase(t);
/*     */     }
/* 111 */     return false;
/*     */   }
/*     */ 
/*     */   public static boolean equal(String s, String t)
/*     */   {
/* 123 */     if (s == t) return true;
/* 124 */     if ((s != null) && (t != null)) {
/* 125 */       return s.equals(t);
/*     */     }
/* 127 */     return false;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.schemagen.Util
 * JD-Core Version:    0.6.2
 */