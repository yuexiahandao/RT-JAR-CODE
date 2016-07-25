/*     */ package com.sun.xml.internal.fastinfoset.sax;
/*     */ 
/*     */ import java.io.File;
/*     */ 
/*     */ public class SystemIdResolver
/*     */ {
/*     */   public static String getAbsoluteURIFromRelative(String localPath)
/*     */   {
/*  38 */     if ((localPath == null) || (localPath.length() == 0)) {
/*  39 */       return "";
/*     */     }
/*     */ 
/*  42 */     String absolutePath = localPath;
/*  43 */     if (!isAbsolutePath(localPath))
/*     */       try {
/*  45 */         absolutePath = getAbsolutePathFromRelativePath(localPath);
/*     */       }
/*     */       catch (SecurityException se) {
/*  48 */         return "file:" + localPath;
/*     */       }
/*     */     String urlString;
/*     */     String urlString;
/*  53 */     if (null != absolutePath) {
/*  54 */       urlString = "file:///" + absolutePath;
/*     */     }
/*     */     else
/*     */     {
/*  59 */       urlString = "file:" + localPath;
/*     */     }
/*     */ 
/*  62 */     return replaceChars(urlString);
/*     */   }
/*     */ 
/*     */   private static String getAbsolutePathFromRelativePath(String relativePath) {
/*  66 */     return new File(relativePath).getAbsolutePath();
/*     */   }
/*     */ 
/*     */   public static boolean isAbsoluteURI(String systemId) {
/*  70 */     if (systemId == null) {
/*  71 */       return false;
/*     */     }
/*     */ 
/*  74 */     if (isWindowsAbsolutePath(systemId)) {
/*  75 */       return false;
/*     */     }
/*     */ 
/*  78 */     int fragmentIndex = systemId.indexOf('#');
/*  79 */     int queryIndex = systemId.indexOf('?');
/*  80 */     int slashIndex = systemId.indexOf('/');
/*  81 */     int colonIndex = systemId.indexOf(':');
/*     */ 
/*  83 */     int index = systemId.length() - 1;
/*  84 */     if (fragmentIndex > 0) {
/*  85 */       index = fragmentIndex;
/*     */     }
/*  87 */     if ((queryIndex > 0) && (queryIndex < index)) {
/*  88 */       index = queryIndex;
/*     */     }
/*  90 */     if ((slashIndex > 0) && (slashIndex < index)) {
/*  91 */       index = slashIndex;
/*     */     }
/*  93 */     return (colonIndex > 0) && (colonIndex < index);
/*     */   }
/*     */ 
/*     */   public static boolean isAbsolutePath(String systemId) {
/*  97 */     if (systemId == null)
/*  98 */       return false;
/*  99 */     File file = new File(systemId);
/* 100 */     return file.isAbsolute();
/*     */   }
/*     */ 
/*     */   private static boolean isWindowsAbsolutePath(String systemId)
/*     */   {
/* 105 */     if (!isAbsolutePath(systemId))
/* 106 */       return false;
/* 107 */     if ((systemId.length() > 2) && (systemId.charAt(1) == ':') && (Character.isLetter(systemId.charAt(0))) && ((systemId.charAt(2) == '\\') || (systemId.charAt(2) == '/')))
/*     */     {
/* 111 */       return true;
/*     */     }
/* 113 */     return false;
/*     */   }
/*     */ 
/*     */   private static String replaceChars(String str) {
/* 117 */     StringBuffer buf = new StringBuffer(str);
/* 118 */     int length = buf.length();
/* 119 */     for (int i = 0; i < length; i++) {
/* 120 */       char currentChar = buf.charAt(i);
/*     */ 
/* 122 */       if (currentChar == ' ') {
/* 123 */         buf.setCharAt(i, '%');
/* 124 */         buf.insert(i + 1, "20");
/* 125 */         length += 2;
/* 126 */         i += 2;
/*     */       }
/* 129 */       else if (currentChar == '\\') {
/* 130 */         buf.setCharAt(i, '/');
/*     */       }
/*     */     }
/*     */ 
/* 134 */     return buf.toString();
/*     */   }
/*     */ 
/*     */   public static String getAbsoluteURI(String systemId) {
/* 138 */     String absoluteURI = systemId;
/* 139 */     if (isAbsoluteURI(systemId)) {
/* 140 */       if (systemId.startsWith("file:")) {
/* 141 */         String str = systemId.substring(5);
/*     */ 
/* 143 */         if ((str != null) && (str.startsWith("/"))) {
/* 144 */           if ((str.startsWith("///")) || (!str.startsWith("//"))) {
/* 145 */             int secondColonIndex = systemId.indexOf(':', 5);
/* 146 */             if (secondColonIndex > 0) {
/* 147 */               String localPath = systemId.substring(secondColonIndex - 1);
/*     */               try {
/* 149 */                 if (!isAbsolutePath(localPath))
/* 150 */                   absoluteURI = systemId.substring(0, secondColonIndex - 1) + getAbsolutePathFromRelativePath(localPath);
/*     */               }
/*     */               catch (SecurityException se)
/*     */               {
/* 154 */                 return systemId;
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */         else {
/* 160 */           return getAbsoluteURIFromRelative(systemId.substring(5));
/*     */         }
/*     */ 
/* 163 */         return replaceChars(absoluteURI);
/*     */       }
/*     */ 
/* 166 */       return systemId;
/*     */     }
/*     */ 
/* 170 */     return getAbsoluteURIFromRelative(systemId);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.fastinfoset.sax.SystemIdResolver
 * JD-Core Version:    0.6.2
 */