/*     */ package com.sun.jndi.toolkit.url;
/*     */ 
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URLDecoder;
/*     */ 
/*     */ public final class UrlUtil
/*     */ {
/*     */   public static final String decode(String paramString)
/*     */     throws MalformedURLException
/*     */   {
/*     */     try
/*     */     {
/*  48 */       return decode(paramString, "8859_1");
/*     */     } catch (UnsupportedEncodingException localUnsupportedEncodingException) {
/*     */     }
/*  51 */     throw new MalformedURLException("ISO-Latin-1 decoder unavailable");
/*     */   }
/*     */ 
/*     */   public static final String decode(String paramString1, String paramString2)
/*     */     throws MalformedURLException, UnsupportedEncodingException
/*     */   {
/*     */     try
/*     */     {
/*  67 */       return URLDecoder.decode(paramString1, paramString2);
/*     */     } catch (IllegalArgumentException localIllegalArgumentException) {
/*  69 */       MalformedURLException localMalformedURLException = new MalformedURLException("Invalid URI encoding: " + paramString1);
/*  70 */       localMalformedURLException.initCause(localIllegalArgumentException);
/*  71 */       throw localMalformedURLException;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static final String encode(String paramString1, String paramString2)
/*     */     throws UnsupportedEncodingException
/*     */   {
/*  90 */     byte[] arrayOfByte = paramString1.getBytes(paramString2);
/*  91 */     int i = arrayOfByte.length;
/*     */ 
/* 100 */     char[] arrayOfChar = new char[3 * i];
/* 101 */     int j = 0;
/*     */ 
/* 103 */     for (int k = 0; k < i; k++) {
/* 104 */       if (((arrayOfByte[k] >= 97) && (arrayOfByte[k] <= 122)) || ((arrayOfByte[k] >= 65) && (arrayOfByte[k] <= 90)) || ((arrayOfByte[k] >= 48) && (arrayOfByte[k] <= 57)) || ("=,+;.'-@&/$_()!~*:".indexOf(arrayOfByte[k]) >= 0))
/*     */       {
/* 108 */         arrayOfChar[(j++)] = ((char)arrayOfByte[k]);
/*     */       } else {
/* 110 */         arrayOfChar[(j++)] = '%';
/* 111 */         arrayOfChar[(j++)] = Character.forDigit(0xF & arrayOfByte[k] >>> 4, 16);
/* 112 */         arrayOfChar[(j++)] = Character.forDigit(0xF & arrayOfByte[k], 16);
/*     */       }
/*     */     }
/* 115 */     return new String(arrayOfChar, 0, j);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jndi.toolkit.url.UrlUtil
 * JD-Core Version:    0.6.2
 */