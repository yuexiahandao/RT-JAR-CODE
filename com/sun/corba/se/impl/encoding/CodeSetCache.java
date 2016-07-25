/*     */ package com.sun.corba.se.impl.encoding;
/*     */ 
/*     */ import java.nio.charset.CharsetDecoder;
/*     */ import java.nio.charset.CharsetEncoder;
/*     */ import java.util.Map;
/*     */ import java.util.WeakHashMap;
/*     */ 
/*     */ class CodeSetCache
/*     */ {
/*  47 */   private ThreadLocal converterCaches = new ThreadLocal() {
/*     */     public Object initialValue() {
/*  49 */       return new Map[] { new WeakHashMap(), new WeakHashMap() };
/*     */     }
/*  47 */   };
/*     */   private static final int BTC_CACHE_MAP = 0;
/*     */   private static final int CTB_CACHE_MAP = 1;
/*     */ 
/*     */   CharsetDecoder getByteToCharConverter(Object paramObject)
/*     */   {
/*  71 */     Map localMap = ((Map[])(Map[])this.converterCaches.get())[0];
/*     */ 
/*  73 */     return (CharsetDecoder)localMap.get(paramObject);
/*     */   }
/*     */ 
/*     */   CharsetEncoder getCharToByteConverter(Object paramObject)
/*     */   {
/*  80 */     Map localMap = ((Map[])(Map[])this.converterCaches.get())[1];
/*     */ 
/*  82 */     return (CharsetEncoder)localMap.get(paramObject);
/*     */   }
/*     */ 
/*     */   CharsetDecoder setConverter(Object paramObject, CharsetDecoder paramCharsetDecoder)
/*     */   {
/*  90 */     Map localMap = ((Map[])(Map[])this.converterCaches.get())[0];
/*     */ 
/*  92 */     localMap.put(paramObject, paramCharsetDecoder);
/*     */ 
/*  94 */     return paramCharsetDecoder;
/*     */   }
/*     */ 
/*     */   CharsetEncoder setConverter(Object paramObject, CharsetEncoder paramCharsetEncoder)
/*     */   {
/* 103 */     Map localMap = ((Map[])(Map[])this.converterCaches.get())[1];
/*     */ 
/* 105 */     localMap.put(paramObject, paramCharsetEncoder);
/*     */ 
/* 107 */     return paramCharsetEncoder;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.encoding.CodeSetCache
 * JD-Core Version:    0.6.2
 */