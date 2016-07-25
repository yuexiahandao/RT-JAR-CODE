/*     */ package com.sun.org.apache.xml.internal.serializer;
/*     */ 
/*     */ public final class EncodingInfo
/*     */ {
/*     */   final String name;
/*     */   final String javaName;
/*     */   private InEncoding m_encoding;
/*     */ 
/*     */   public boolean isInEncoding(char ch)
/*     */   {
/*  90 */     if (this.m_encoding == null) {
/*  91 */       this.m_encoding = new EncodingImpl(null);
/*     */     }
/*     */ 
/*  98 */     return this.m_encoding.isInEncoding(ch);
/*     */   }
/*     */ 
/*     */   public boolean isInEncoding(char high, char low)
/*     */   {
/* 109 */     if (this.m_encoding == null) {
/* 110 */       this.m_encoding = new EncodingImpl(null);
/*     */     }
/*     */ 
/* 117 */     return this.m_encoding.isInEncoding(high, low);
/*     */   }
/*     */ 
/*     */   public EncodingInfo(String name, String javaName)
/*     */   {
/* 132 */     this.name = name;
/* 133 */     this.javaName = javaName;
/*     */   }
/*     */ 
/*     */   private static boolean inEncoding(char ch, String encoding)
/*     */   {
/*     */     boolean isInEncoding;
/*     */     try
/*     */     {
/* 409 */       char[] cArray = new char[1];
/* 410 */       cArray[0] = ch;
/*     */ 
/* 412 */       String s = new String(cArray);
/*     */ 
/* 415 */       byte[] bArray = s.getBytes(encoding);
/* 416 */       isInEncoding = inEncoding(ch, bArray);
/*     */     }
/*     */     catch (Exception e) {
/* 419 */       isInEncoding = false;
/*     */ 
/* 424 */       if (encoding == null)
/* 425 */         isInEncoding = true;
/*     */     }
/* 427 */     return isInEncoding;
/*     */   }
/*     */ 
/*     */   private static boolean inEncoding(char high, char low, String encoding)
/*     */   {
/*     */     boolean isInEncoding;
/*     */     try
/*     */     {
/* 448 */       char[] cArray = new char[2];
/* 449 */       cArray[0] = high;
/* 450 */       cArray[1] = low;
/*     */ 
/* 452 */       String s = new String(cArray);
/*     */ 
/* 455 */       byte[] bArray = s.getBytes(encoding);
/* 456 */       isInEncoding = inEncoding(high, bArray);
/*     */     } catch (Exception e) {
/* 458 */       isInEncoding = false;
/*     */     }
/*     */ 
/* 461 */     return isInEncoding;
/*     */   }
/*     */ 
/*     */   private static boolean inEncoding(char ch, byte[] data)
/*     */   {
/*     */     boolean isInEncoding;
/*     */     boolean isInEncoding;
/* 481 */     if ((data == null) || (data.length == 0)) {
/* 482 */       isInEncoding = false;
/*     */     }
/*     */     else
/*     */     {
/*     */       boolean isInEncoding;
/* 485 */       if (data[0] == 0) {
/* 486 */         isInEncoding = false;
/*     */       }
/*     */       else
/*     */       {
/*     */         boolean isInEncoding;
/* 487 */         if ((data[0] == 63) && (ch != '?')) {
/* 488 */           isInEncoding = false;
/*     */         }
/*     */         else
/*     */         {
/* 508 */           isInEncoding = true;
/*     */         }
/*     */       }
/*     */     }
/* 511 */     return isInEncoding;
/*     */   }
/*     */ 
/*     */   private class EncodingImpl
/*     */     implements EncodingInfo.InEncoding
/*     */   {
/*     */     private final String m_encoding;
/*     */     private final int m_first;
/*     */     private final int m_explFirst;
/*     */     private final int m_explLast;
/*     */     private final int m_last;
/*     */     private EncodingInfo.InEncoding m_before;
/*     */     private EncodingInfo.InEncoding m_after;
/*     */     private static final int RANGE = 128;
/* 309 */     private final boolean[] m_alreadyKnown = new boolean[''];
/*     */ 
/* 314 */     private final boolean[] m_isInEncoding = new boolean[''];
/*     */ 
/*     */     public boolean isInEncoding(char ch1)
/*     */     {
/* 168 */       int codePoint = Encodings.toCodePoint(ch1);
/*     */       boolean ret;
/*     */       boolean ret;
/* 169 */       if (codePoint < this.m_explFirst)
/*     */       {
/* 174 */         if (this.m_before == null) {
/* 175 */           this.m_before = new EncodingImpl(EncodingInfo.this, this.m_encoding, this.m_first, this.m_explFirst - 1, codePoint);
/*     */         }
/*     */ 
/* 181 */         ret = this.m_before.isInEncoding(ch1);
/*     */       }
/*     */       else
/*     */       {
/*     */         boolean ret;
/* 182 */         if (this.m_explLast < codePoint)
/*     */         {
/* 187 */           if (this.m_after == null) {
/* 188 */             this.m_after = new EncodingImpl(EncodingInfo.this, this.m_encoding, this.m_explLast + 1, this.m_last, codePoint);
/*     */           }
/*     */ 
/* 194 */           ret = this.m_after.isInEncoding(ch1);
/*     */         }
/*     */         else {
/* 197 */           int idx = codePoint - this.m_explFirst;
/*     */           boolean ret;
/* 200 */           if (this.m_alreadyKnown[idx] != 0) {
/* 201 */             ret = this.m_isInEncoding[idx];
/*     */           }
/*     */           else
/*     */           {
/* 205 */             ret = EncodingInfo.inEncoding(ch1, this.m_encoding);
/* 206 */             this.m_alreadyKnown[idx] = true;
/* 207 */             this.m_isInEncoding[idx] = ret;
/*     */           }
/*     */         }
/*     */       }
/* 210 */       return ret;
/*     */     }
/*     */ 
/*     */     public boolean isInEncoding(char high, char low)
/*     */     {
/* 215 */       int codePoint = Encodings.toCodePoint(high, low);
/*     */       boolean ret;
/*     */       boolean ret;
/* 216 */       if (codePoint < this.m_explFirst)
/*     */       {
/* 221 */         if (this.m_before == null) {
/* 222 */           this.m_before = new EncodingImpl(EncodingInfo.this, this.m_encoding, this.m_first, this.m_explFirst - 1, codePoint);
/*     */         }
/*     */ 
/* 228 */         ret = this.m_before.isInEncoding(high, low);
/*     */       }
/*     */       else
/*     */       {
/*     */         boolean ret;
/* 229 */         if (this.m_explLast < codePoint)
/*     */         {
/* 234 */           if (this.m_after == null) {
/* 235 */             this.m_after = new EncodingImpl(EncodingInfo.this, this.m_encoding, this.m_explLast + 1, this.m_last, codePoint);
/*     */           }
/*     */ 
/* 241 */           ret = this.m_after.isInEncoding(high, low);
/*     */         }
/*     */         else {
/* 244 */           int idx = codePoint - this.m_explFirst;
/*     */           boolean ret;
/* 247 */           if (this.m_alreadyKnown[idx] != 0) {
/* 248 */             ret = this.m_isInEncoding[idx];
/*     */           }
/*     */           else
/*     */           {
/* 252 */             ret = EncodingInfo.inEncoding(high, low, this.m_encoding);
/* 253 */             this.m_alreadyKnown[idx] = true;
/* 254 */             this.m_isInEncoding[idx] = ret;
/*     */           }
/*     */         }
/*     */       }
/* 257 */       return ret;
/*     */     }
/*     */ 
/*     */     private EncodingImpl()
/*     */     {
/* 319 */       this(EncodingInfo.this.javaName, 0, 2147483647, 0);
/*     */     }
/*     */ 
/*     */     private EncodingImpl(String encoding, int first, int last, int codePoint)
/*     */     {
/* 325 */       this.m_first = first;
/* 326 */       this.m_last = last;
/*     */ 
/* 332 */       this.m_explFirst = (codePoint / 128 * 128);
/* 333 */       this.m_explLast = (this.m_explFirst + 127);
/*     */ 
/* 335 */       this.m_encoding = encoding;
/*     */ 
/* 337 */       if (EncodingInfo.this.javaName != null)
/*     */       {
/*     */         int unicode;
/* 340 */         if ((0 <= this.m_explFirst) && (this.m_explFirst <= 127))
/*     */         {
/* 343 */           if (("UTF8".equals(EncodingInfo.this.javaName)) || ("UTF-16".equals(EncodingInfo.this.javaName)) || ("ASCII".equals(EncodingInfo.this.javaName)) || ("US-ASCII".equals(EncodingInfo.this.javaName)) || ("Unicode".equals(EncodingInfo.this.javaName)) || ("UNICODE".equals(EncodingInfo.this.javaName)) || (EncodingInfo.this.javaName.startsWith("ISO8859")))
/*     */           {
/* 362 */             for (unicode = 1; unicode < 127; unicode++) {
/* 363 */               int idx = unicode - this.m_explFirst;
/* 364 */               if ((0 <= idx) && (idx < 128)) {
/* 365 */                 this.m_alreadyKnown[idx] = true;
/* 366 */                 this.m_isInEncoding[idx] = true;
/*     */               }
/*     */ 
/*     */             }
/*     */ 
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/* 382 */         if (EncodingInfo.this.javaName == null)
/* 383 */           for (int idx = 0; idx < this.m_alreadyKnown.length; idx++) {
/* 384 */             this.m_alreadyKnown[idx] = true;
/* 385 */             this.m_isInEncoding[idx] = true;
/*     */           }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private static abstract interface InEncoding
/*     */   {
/*     */     public abstract boolean isInEncoding(char paramChar);
/*     */ 
/*     */     public abstract boolean isInEncoding(char paramChar1, char paramChar2);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.serializer.EncodingInfo
 * JD-Core Version:    0.6.2
 */