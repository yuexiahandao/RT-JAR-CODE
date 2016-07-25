/*     */ package com.sun.org.apache.xml.internal.utils;
/*     */ 
/*     */ public class XMLCharacterRecognizer
/*     */ {
/*     */   public static boolean isWhiteSpace(char ch)
/*     */   {
/*  42 */     return (ch == ' ') || (ch == '\t') || (ch == '\r') || (ch == '\n');
/*     */   }
/*     */ 
/*     */   public static boolean isWhiteSpace(char[] ch, int start, int length)
/*     */   {
/*  57 */     int end = start + length;
/*     */ 
/*  59 */     for (int s = start; s < end; s++)
/*     */     {
/*  61 */       if (!isWhiteSpace(ch[s])) {
/*  62 */         return false;
/*     */       }
/*     */     }
/*  65 */     return true;
/*     */   }
/*     */ 
/*     */   public static boolean isWhiteSpace(StringBuffer buf)
/*     */   {
/*  77 */     int n = buf.length();
/*     */ 
/*  79 */     for (int i = 0; i < n; i++)
/*     */     {
/*  81 */       if (!isWhiteSpace(buf.charAt(i))) {
/*  82 */         return false;
/*     */       }
/*     */     }
/*  85 */     return true;
/*     */   }
/*     */ 
/*     */   public static boolean isWhiteSpace(String s)
/*     */   {
/*  97 */     if (null != s)
/*     */     {
/*  99 */       int n = s.length();
/*     */ 
/* 101 */       for (int i = 0; i < n; i++)
/*     */       {
/* 103 */         if (!isWhiteSpace(s.charAt(i))) {
/* 104 */           return false;
/*     */         }
/*     */       }
/*     */     }
/* 108 */     return true;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.utils.XMLCharacterRecognizer
 * JD-Core Version:    0.6.2
 */