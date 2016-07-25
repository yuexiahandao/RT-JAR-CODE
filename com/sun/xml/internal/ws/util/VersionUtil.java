/*     */ package com.sun.xml.internal.ws.util;
/*     */ 
/*     */ import java.util.StringTokenizer;
/*     */ 
/*     */ public final class VersionUtil
/*     */ {
/*     */   public static final String JAXWS_VERSION_20 = "2.0";
/*     */   public static final String JAXWS_VERSION_DEFAULT = "2.0";
/*     */ 
/*     */   public static boolean isVersion20(String version)
/*     */   {
/*  40 */     return "2.0".equals(version);
/*     */   }
/*     */ 
/*     */   public static boolean isValidVersion(String version)
/*     */   {
/*  48 */     return isVersion20(version);
/*     */   }
/*     */ 
/*     */   public static String getValidVersionString() {
/*  52 */     return "2.0";
/*     */   }
/*     */ 
/*     */   public static int[] getCanonicalVersion(String version)
/*     */   {
/*  68 */     int[] canonicalVersion = new int[4];
/*     */ 
/*  71 */     canonicalVersion[0] = 1;
/*  72 */     canonicalVersion[1] = 1;
/*  73 */     canonicalVersion[2] = 0;
/*  74 */     canonicalVersion[3] = 0;
/*     */ 
/*  76 */     String DASH_DELIM = "_";
/*  77 */     String DOT_DELIM = ".";
/*     */ 
/*  79 */     StringTokenizer tokenizer = new StringTokenizer(version, ".");
/*     */ 
/*  81 */     String token = tokenizer.nextToken();
/*     */ 
/*  84 */     canonicalVersion[0] = Integer.parseInt(token);
/*     */ 
/*  87 */     token = tokenizer.nextToken();
/*  88 */     if (token.indexOf("_") == -1)
/*     */     {
/*  90 */       canonicalVersion[1] = Integer.parseInt(token);
/*     */     }
/*     */     else {
/*  93 */       StringTokenizer subTokenizer = new StringTokenizer(token, "_");
/*     */ 
/*  95 */       canonicalVersion[1] = Integer.parseInt(subTokenizer.nextToken());
/*     */ 
/*  98 */       canonicalVersion[3] = Integer.parseInt(subTokenizer.nextToken());
/*     */     }
/*     */ 
/* 102 */     if (tokenizer.hasMoreTokens()) {
/* 103 */       token = tokenizer.nextToken();
/* 104 */       if (token.indexOf("_") == -1)
/*     */       {
/* 106 */         canonicalVersion[2] = Integer.parseInt(token);
/*     */ 
/* 109 */         if (tokenizer.hasMoreTokens())
/* 110 */           canonicalVersion[3] = Integer.parseInt(tokenizer.nextToken());
/*     */       }
/*     */       else {
/* 113 */         StringTokenizer subTokenizer = new StringTokenizer(token, "_");
/*     */ 
/* 116 */         canonicalVersion[2] = Integer.parseInt(subTokenizer.nextToken());
/*     */ 
/* 119 */         canonicalVersion[3] = Integer.parseInt(subTokenizer.nextToken());
/*     */       }
/*     */     }
/*     */ 
/* 123 */     return canonicalVersion;
/*     */   }
/*     */ 
/*     */   public static int compare(String version1, String version2)
/*     */   {
/* 136 */     int[] canonicalVersion1 = getCanonicalVersion(version1);
/* 137 */     int[] canonicalVersion2 = getCanonicalVersion(version2);
/*     */ 
/* 139 */     if (canonicalVersion1[0] < canonicalVersion2[0])
/* 140 */       return -1;
/* 141 */     if (canonicalVersion1[0] > canonicalVersion2[0]) {
/* 142 */       return 1;
/*     */     }
/* 144 */     if (canonicalVersion1[1] < canonicalVersion2[1])
/* 145 */       return -1;
/* 146 */     if (canonicalVersion1[1] > canonicalVersion2[1]) {
/* 147 */       return 1;
/*     */     }
/* 149 */     if (canonicalVersion1[2] < canonicalVersion2[2])
/* 150 */       return -1;
/* 151 */     if (canonicalVersion1[2] > canonicalVersion2[2]) {
/* 152 */       return 1;
/*     */     }
/* 154 */     if (canonicalVersion1[3] < canonicalVersion2[3])
/* 155 */       return -1;
/* 156 */     if (canonicalVersion1[3] > canonicalVersion2[3]) {
/* 157 */       return 1;
/*     */     }
/* 159 */     return 0;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.util.VersionUtil
 * JD-Core Version:    0.6.2
 */