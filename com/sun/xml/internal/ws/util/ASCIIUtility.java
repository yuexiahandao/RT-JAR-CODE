/*     */ package com.sun.xml.internal.ws.util;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ 
/*     */ public class ASCIIUtility
/*     */ {
/*     */   public static int parseInt(byte[] b, int start, int end, int radix)
/*     */     throws NumberFormatException
/*     */   {
/*  51 */     if (b == null) {
/*  52 */       throw new NumberFormatException("null");
/*     */     }
/*  54 */     int result = 0;
/*  55 */     boolean negative = false;
/*  56 */     int i = start;
/*     */ 
/*  61 */     if (end > start)
/*     */     {
/*     */       int limit;
/*  62 */       if (b[i] == 45) {
/*  63 */         negative = true;
/*  64 */         int limit = -2147483648;
/*  65 */         i++;
/*     */       } else {
/*  67 */         limit = -2147483647;
/*     */       }
/*  69 */       int multmin = limit / radix;
/*  70 */       if (i < end) {
/*  71 */         int digit = Character.digit((char)b[(i++)], radix);
/*  72 */         if (digit < 0) {
/*  73 */           throw new NumberFormatException("illegal number: " + toString(b, start, end));
/*     */         }
/*     */ 
/*  77 */         result = -digit;
/*     */       }
/*     */ 
/*  80 */       while (i < end)
/*     */       {
/*  82 */         int digit = Character.digit((char)b[(i++)], radix);
/*  83 */         if (digit < 0) {
/*  84 */           throw new NumberFormatException("illegal number");
/*     */         }
/*  86 */         if (result < multmin) {
/*  87 */           throw new NumberFormatException("illegal number");
/*     */         }
/*  89 */         result *= radix;
/*  90 */         if (result < limit + digit) {
/*  91 */           throw new NumberFormatException("illegal number");
/*     */         }
/*  93 */         result -= digit;
/*     */       }
/*     */     }
/*  96 */     throw new NumberFormatException("illegal number");
/*     */     int multmin;
/*     */     int limit;
/*  98 */     if (negative) {
/*  99 */       if (i > start + 1) {
/* 100 */         return result;
/*     */       }
/* 102 */       throw new NumberFormatException("illegal number");
/*     */     }
/*     */ 
/* 105 */     return -result;
/*     */   }
/*     */ 
/*     */   public static String toString(byte[] b, int start, int end)
/*     */   {
/* 115 */     int size = end - start;
/* 116 */     char[] theChars = new char[size];
/*     */ 
/* 118 */     int i = 0; for (int j = start; i < size; ) {
/* 119 */       theChars[(i++)] = ((char)(b[(j++)] & 0xFF));
/*     */     }
/* 121 */     return new String(theChars);
/*     */   }
/*     */ 
/*     */   public static byte[] getBytes(String s) {
/* 125 */     char[] chars = s.toCharArray();
/* 126 */     int size = chars.length;
/* 127 */     byte[] bytes = new byte[size];
/*     */ 
/* 129 */     for (int i = 0; i < size; )
/* 130 */       bytes[i] = ((byte)chars[(i++)]);
/* 131 */     return bytes;
/*     */   }
/*     */ 
/*     */   public static byte[] getBytes(InputStream is) throws IOException {
/* 135 */     ByteArrayBuffer bab = new ByteArrayBuffer();
/* 136 */     bab.write(is);
/* 137 */     return bab.toByteArray();
/*     */   }
/*     */ 
/*     */   public static void copyStream(InputStream is, OutputStream out) throws IOException {
/* 141 */     int size = 1024;
/* 142 */     byte[] buf = new byte[size];
/*     */     int len;
/* 145 */     while ((len = is.read(buf, 0, size)) != -1)
/* 146 */       out.write(buf, 0, len);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.util.ASCIIUtility
 * JD-Core Version:    0.6.2
 */