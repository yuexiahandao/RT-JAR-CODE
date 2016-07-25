/*     */ package com.sun.org.apache.xml.internal.security.c14n.implementations;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.util.Map;
/*     */ 
/*     */ public class UtfHelpper
/*     */ {
/*     */   static final void writeByte(String paramString, OutputStream paramOutputStream, Map paramMap)
/*     */     throws IOException
/*     */   {
/*  10 */     byte[] arrayOfByte = (byte[])paramMap.get(paramString);
/*  11 */     if (arrayOfByte == null) {
/*  12 */       arrayOfByte = getStringInUtf8(paramString);
/*  13 */       paramMap.put(paramString, arrayOfByte);
/*     */     }
/*     */ 
/*  16 */     paramOutputStream.write(arrayOfByte);
/*     */   }
/*     */ 
/*     */   static final void writeCharToUtf8(char paramChar, OutputStream paramOutputStream) throws IOException
/*     */   {
/*  21 */     if (paramChar < '') {
/*  22 */       paramOutputStream.write(paramChar);
/*  23 */       return;
/*     */     }
/*  25 */     if (((paramChar >= 55296) && (paramChar <= 56319)) || ((paramChar >= 56320) && (paramChar <= 57343)))
/*     */     {
/*  27 */       paramOutputStream.write(63);
/*     */       return;
/*     */     }
/*     */     int j;
/*     */     int i;
/*  33 */     if (paramChar > '߿') {
/*  34 */       k = (char)(paramChar >>> '\f');
/*  35 */       j = 224;
/*  36 */       if (k > 0) {
/*  37 */         j |= k & 0xF;
/*     */       }
/*  39 */       paramOutputStream.write(j);
/*  40 */       j = 128;
/*  41 */       i = 63;
/*     */     } else {
/*  43 */       j = 192;
/*  44 */       i = 31;
/*     */     }
/*  46 */     int k = (char)(paramChar >>> '\006');
/*  47 */     if (k > 0) {
/*  48 */       j |= k & i;
/*     */     }
/*  50 */     paramOutputStream.write(j);
/*  51 */     paramOutputStream.write(0x80 | paramChar & 0x3F);
/*     */   }
/*     */ 
/*     */   static final void writeStringToUtf8(String paramString, OutputStream paramOutputStream) throws IOException
/*     */   {
/*  56 */     int i = paramString.length();
/*  57 */     int j = 0;
/*     */ 
/*  59 */     while (j < i) {
/*  60 */       int k = paramString.charAt(j++);
/*  61 */       if (k < 128) {
/*  62 */         paramOutputStream.write(k);
/*     */       }
/*  65 */       else if (((k >= 55296) && (k <= 56319)) || ((k >= 56320) && (k <= 57343)))
/*     */       {
/*  67 */         paramOutputStream.write(63);
/*     */       }
/*     */       else
/*     */       {
/*     */         int i1;
/*     */         int n;
/*  73 */         if (k > 2047) {
/*  74 */           m = (char)(k >>> 12);
/*  75 */           i1 = 224;
/*  76 */           if (m > 0) {
/*  77 */             i1 |= m & 0xF;
/*     */           }
/*  79 */           paramOutputStream.write(i1);
/*  80 */           i1 = 128;
/*  81 */           n = 63;
/*     */         } else {
/*  83 */           i1 = 192;
/*  84 */           n = 31;
/*     */         }
/*  86 */         int m = (char)(k >>> 6);
/*  87 */         if (m > 0) {
/*  88 */           i1 |= m & n;
/*     */         }
/*  90 */         paramOutputStream.write(i1);
/*  91 */         paramOutputStream.write(0x80 | k & 0x3F);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public static final byte[] getStringInUtf8(String paramString) {
/*  97 */     int i = paramString.length();
/*  98 */     int j = 0;
/*  99 */     Object localObject = new byte[i];
/* 100 */     int k = 0;
/* 101 */     int m = 0;
/*     */ 
/* 103 */     while (k < i) {
/* 104 */       int n = paramString.charAt(k++);
/* 105 */       if (n < 128) {
/* 106 */         localObject[(m++)] = ((byte)n);
/*     */       }
/* 109 */       else if (((n >= 55296) && (n <= 56319)) || ((n >= 56320) && (n <= 57343)))
/*     */       {
/* 111 */         localObject[(m++)] = 63;
/*     */       }
/*     */       else
/*     */       {
/* 115 */         if (j == 0) {
/* 116 */           byte[] arrayOfByte1 = new byte[3 * i];
/* 117 */           System.arraycopy(localObject, 0, arrayOfByte1, 0, m);
/* 118 */           localObject = arrayOfByte1;
/* 119 */           j = 1;
/*     */         }
/*     */         int i3;
/*     */         int i2;
/* 124 */         if (n > 2047) {
/* 125 */           i1 = (char)(n >>> 12);
/* 126 */           i3 = -32;
/* 127 */           if (i1 > 0) {
/* 128 */             i3 = (byte)(i3 | i1 & 0xF);
/*     */           }
/* 130 */           localObject[(m++)] = i3;
/* 131 */           i3 = -128;
/* 132 */           i2 = 63;
/*     */         } else {
/* 134 */           i3 = -64;
/* 135 */           i2 = 31;
/*     */         }
/* 137 */         int i1 = (char)(n >>> 6);
/* 138 */         if (i1 > 0) {
/* 139 */           i3 = (byte)(i3 | i1 & i2);
/*     */         }
/* 141 */         localObject[(m++)] = i3;
/* 142 */         localObject[(m++)] = ((byte)(0x80 | n & 0x3F));
/*     */       }
/*     */     }
/* 145 */     if (j != 0) {
/* 146 */       byte[] arrayOfByte2 = new byte[m];
/* 147 */       System.arraycopy(localObject, 0, arrayOfByte2, 0, m);
/* 148 */       localObject = arrayOfByte2;
/*     */     }
/* 150 */     return localObject;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.security.c14n.implementations.UtfHelpper
 * JD-Core Version:    0.6.2
 */