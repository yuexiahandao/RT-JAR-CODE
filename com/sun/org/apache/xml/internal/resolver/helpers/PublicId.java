/*     */ package com.sun.org.apache.xml.internal.resolver.helpers;
/*     */ 
/*     */ public abstract class PublicId
/*     */ {
/*     */   public static String normalize(String publicId)
/*     */   {
/*  61 */     String normal = publicId.replace('\t', ' ');
/*  62 */     normal = normal.replace('\r', ' ');
/*  63 */     normal = normal.replace('\n', ' ');
/*  64 */     normal = normal.trim();
/*     */     int pos;
/*  68 */     while ((pos = normal.indexOf("  ")) >= 0) {
/*  69 */       normal = normal.substring(0, pos) + normal.substring(pos + 1);
/*     */     }
/*     */ 
/*  72 */     return normal;
/*     */   }
/*     */ 
/*     */   public static String encodeURN(String publicId)
/*     */   {
/*  86 */     String urn = normalize(publicId);
/*     */ 
/*  88 */     urn = stringReplace(urn, "%", "%25");
/*  89 */     urn = stringReplace(urn, ";", "%3B");
/*  90 */     urn = stringReplace(urn, "'", "%27");
/*  91 */     urn = stringReplace(urn, "?", "%3F");
/*  92 */     urn = stringReplace(urn, "#", "%23");
/*  93 */     urn = stringReplace(urn, "+", "%2B");
/*  94 */     urn = stringReplace(urn, " ", "+");
/*  95 */     urn = stringReplace(urn, "::", ";");
/*  96 */     urn = stringReplace(urn, ":", "%3A");
/*  97 */     urn = stringReplace(urn, "//", ":");
/*  98 */     urn = stringReplace(urn, "/", "%2F");
/*     */ 
/* 100 */     return "urn:publicid:" + urn;
/*     */   }
/*     */ 
/*     */   public static String decodeURN(String urn)
/*     */   {
/* 114 */     String publicId = "";
/*     */ 
/* 116 */     if (urn.startsWith("urn:publicid:"))
/* 117 */       publicId = urn.substring(13);
/*     */     else {
/* 119 */       return urn;
/*     */     }
/*     */ 
/* 122 */     publicId = stringReplace(publicId, "%2F", "/");
/* 123 */     publicId = stringReplace(publicId, ":", "//");
/* 124 */     publicId = stringReplace(publicId, "%3A", ":");
/* 125 */     publicId = stringReplace(publicId, ";", "::");
/* 126 */     publicId = stringReplace(publicId, "+", " ");
/* 127 */     publicId = stringReplace(publicId, "%2B", "+");
/* 128 */     publicId = stringReplace(publicId, "%23", "#");
/* 129 */     publicId = stringReplace(publicId, "%3F", "?");
/* 130 */     publicId = stringReplace(publicId, "%27", "'");
/* 131 */     publicId = stringReplace(publicId, "%3B", ";");
/* 132 */     publicId = stringReplace(publicId, "%25", "%");
/*     */ 
/* 134 */     return publicId;
/*     */   }
/*     */ 
/*     */   private static String stringReplace(String str, String oldStr, String newStr)
/*     */   {
/* 145 */     String result = "";
/* 146 */     int pos = str.indexOf(oldStr);
/*     */ 
/* 150 */     while (pos >= 0)
/*     */     {
/* 152 */       result = result + str.substring(0, pos);
/* 153 */       result = result + newStr;
/* 154 */       str = str.substring(pos + 1);
/*     */ 
/* 156 */       pos = str.indexOf(oldStr);
/*     */     }
/*     */ 
/* 159 */     return result + str;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.resolver.helpers.PublicId
 * JD-Core Version:    0.6.2
 */