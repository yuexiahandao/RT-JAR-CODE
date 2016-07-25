/*     */ package com.sun.org.apache.xml.internal.serialize;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.util.EncodingMap;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Locale;
/*     */ 
/*     */ public class Encodings
/*     */ {
/*     */   static final int DEFAULT_LAST_PRINTABLE = 127;
/*     */   static final int LAST_PRINTABLE_UNICODE = 65535;
/*  52 */   static final String[] UNICODE_ENCODINGS = { "Unicode", "UnicodeBig", "UnicodeLittle", "GB2312", "UTF8", "UTF-16" };
/*     */   static final String DEFAULT_ENCODING = "UTF8";
/*  61 */   static Hashtable _encodings = new Hashtable();
/*     */   static final String JIS_DANGER_CHARS = "\\~¢£¥¬—―‖…‾‾∥∯〜＼～￠￡￢￣";
/*     */ 
/*     */   static EncodingInfo getEncodingInfo(String encoding, boolean allowJavaNames)
/*     */     throws UnsupportedEncodingException
/*     */   {
/*  67 */     EncodingInfo eInfo = null;
/*  68 */     if (encoding == null) {
/*  69 */       if ((eInfo = (EncodingInfo)_encodings.get("UTF8")) != null)
/*  70 */         return eInfo;
/*  71 */       eInfo = new EncodingInfo(EncodingMap.getJava2IANAMapping("UTF8"), "UTF8", 65535);
/*  72 */       _encodings.put("UTF8", eInfo);
/*  73 */       return eInfo;
/*     */     }
/*     */ 
/*  76 */     encoding = encoding.toUpperCase(Locale.ENGLISH);
/*  77 */     String jName = EncodingMap.getIANA2JavaMapping(encoding);
/*  78 */     if (jName == null)
/*     */     {
/*  80 */       if (allowJavaNames) {
/*  81 */         EncodingInfo.testJavaEncodingName(encoding);
/*  82 */         if ((eInfo = (EncodingInfo)_encodings.get(encoding)) != null) {
/*  83 */           return eInfo;
/*     */         }
/*  85 */         for (int i = 0; 
/*  86 */           i < UNICODE_ENCODINGS.length; i++) {
/*  87 */           if (UNICODE_ENCODINGS[i].equalsIgnoreCase(encoding)) {
/*  88 */             eInfo = new EncodingInfo(EncodingMap.getJava2IANAMapping(encoding), encoding, 65535);
/*  89 */             break;
/*     */           }
/*     */         }
/*  92 */         if (i == UNICODE_ENCODINGS.length) {
/*  93 */           eInfo = new EncodingInfo(EncodingMap.getJava2IANAMapping(encoding), encoding, 127);
/*     */         }
/*  95 */         _encodings.put(encoding, eInfo);
/*  96 */         return eInfo;
/*     */       }
/*  98 */       throw new UnsupportedEncodingException(encoding);
/*     */     }
/*     */ 
/* 101 */     if ((eInfo = (EncodingInfo)_encodings.get(jName)) != null) {
/* 102 */       return eInfo;
/*     */     }
/*     */ 
/* 105 */     for (int i = 0; 
/* 106 */       i < UNICODE_ENCODINGS.length; i++) {
/* 107 */       if (UNICODE_ENCODINGS[i].equalsIgnoreCase(jName)) {
/* 108 */         eInfo = new EncodingInfo(encoding, jName, 65535);
/* 109 */         break;
/*     */       }
/*     */     }
/* 112 */     if (i == UNICODE_ENCODINGS.length) {
/* 113 */       eInfo = new EncodingInfo(encoding, jName, 127);
/*     */     }
/* 115 */     _encodings.put(jName, eInfo);
/* 116 */     return eInfo;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.serialize.Encodings
 * JD-Core Version:    0.6.2
 */