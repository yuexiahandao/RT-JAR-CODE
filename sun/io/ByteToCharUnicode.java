/*     */ package sun.io;
/*     */ 
/*     */ public class ByteToCharUnicode extends ByteToCharConverter
/*     */ {
/*     */   static final char BYTE_ORDER_MARK = '﻿';
/*     */   static final char REVERSED_MARK = '￾';
/*     */   static final int AUTO = 0;
/*     */   static final int BIG = 1;
/*     */   static final int LITTLE = 2;
/*     */   int originalByteOrder;
/*     */   int byteOrder;
/*     */   boolean usesMark;
/*  94 */   boolean started = false;
/*     */   int leftOverByte;
/*  96 */   boolean leftOver = false;
/*     */ 
/*     */   public ByteToCharUnicode()
/*     */   {
/*  70 */     this.originalByteOrder = (this.byteOrder = 0);
/*  71 */     this.usesMark = true;
/*     */   }
/*     */ 
/*     */   protected ByteToCharUnicode(int paramInt, boolean paramBoolean)
/*     */   {
/*  79 */     this.originalByteOrder = (this.byteOrder = paramInt);
/*  80 */     this.usesMark = paramBoolean;
/*     */   }
/*     */ 
/*     */   public String getCharacterEncoding() {
/*  84 */     switch (this.originalByteOrder) {
/*     */     case 1:
/*  86 */       return this.usesMark ? "UnicodeBig" : "UnicodeBigUnmarked";
/*     */     case 2:
/*  88 */       return this.usesMark ? "UnicodeLittle" : "UnicodeLittleUnmarked";
/*     */     }
/*  90 */     return "Unicode";
/*     */   }
/*     */ 
/*     */   public int convert(byte[] paramArrayOfByte, int paramInt1, int paramInt2, char[] paramArrayOfChar, int paramInt3, int paramInt4)
/*     */     throws ConversionBufferFullException, MalformedInputException
/*     */   {
/* 102 */     this.byteOff = paramInt1;
/* 103 */     this.charOff = paramInt3;
/*     */ 
/* 105 */     if (paramInt1 >= paramInt2) {
/* 106 */       return 0;
/*     */     }
/*     */ 
/* 109 */     int k = 0;
/* 110 */     int m = paramInt1; int n = paramInt3;
/*     */     int i;
/* 112 */     if (this.leftOver) {
/* 113 */       i = this.leftOverByte & 0xFF;
/* 114 */       this.leftOver = false;
/*     */     }
/*     */     else {
/* 117 */       i = paramArrayOfByte[(m++)] & 0xFF;
/*     */     }
/* 119 */     k = 1;
/*     */     int j;
/*     */     int i1;
/* 121 */     if ((this.usesMark) && (!this.started) && 
/* 122 */       (m < paramInt2)) {
/* 123 */       j = paramArrayOfByte[(m++)] & 0xFF;
/* 124 */       k = 2;
/*     */ 
/* 126 */       i1 = (char)(i << 8 | j);
/* 127 */       int i2 = 0;
/*     */ 
/* 129 */       if (i1 == 65279)
/* 130 */         i2 = 1;
/* 131 */       else if (i1 == 65534) {
/* 132 */         i2 = 2;
/*     */       }
/* 134 */       if (this.byteOrder == 0) {
/* 135 */         if (i2 == 0) {
/* 136 */           this.badInputLength = k;
/* 137 */           throw new MalformedInputException("Missing byte-order mark");
/*     */         }
/*     */ 
/* 140 */         this.byteOrder = i2;
/* 141 */         if (m < paramInt2) {
/* 142 */           i = paramArrayOfByte[(m++)] & 0xFF;
/* 143 */           k = 1;
/*     */         }
/*     */       }
/* 146 */       else if (i2 == 0) {
/* 147 */         m--;
/* 148 */         k = 1;
/*     */       }
/* 150 */       else if (this.byteOrder == i2) {
/* 151 */         if (m < paramInt2) {
/* 152 */           i = paramArrayOfByte[(m++)] & 0xFF;
/* 153 */           k = 1;
/*     */         }
/*     */       }
/*     */       else {
/* 157 */         this.badInputLength = k;
/* 158 */         throw new MalformedInputException("Incorrect byte-order mark");
/*     */       }
/*     */ 
/* 162 */       this.started = true;
/*     */     }
/*     */ 
/* 167 */     while (m < paramInt2) {
/* 168 */       j = paramArrayOfByte[(m++)] & 0xFF;
/* 169 */       k = 2;
/*     */ 
/* 172 */       if (this.byteOrder == 1)
/* 173 */         i1 = (char)(i << 8 | j);
/*     */       else {
/* 175 */         i1 = (char)(j << 8 | i);
/*     */       }
/* 177 */       if (i1 == 65534) {
/* 178 */         throw new MalformedInputException("Reversed byte-order mark");
/*     */       }
/*     */ 
/* 181 */       if (n >= paramInt4)
/* 182 */         throw new ConversionBufferFullException();
/* 183 */       paramArrayOfChar[(n++)] = i1;
/* 184 */       this.byteOff = m;
/* 185 */       this.charOff = n;
/*     */ 
/* 187 */       if (m < paramInt2) {
/* 188 */         i = paramArrayOfByte[(m++)] & 0xFF;
/* 189 */         k = 1;
/*     */       }
/*     */     }
/*     */ 
/* 193 */     if (k == 1) {
/* 194 */       this.leftOverByte = i;
/* 195 */       this.byteOff = m;
/* 196 */       this.leftOver = true;
/*     */     }
/*     */ 
/* 199 */     return n - paramInt3;
/*     */   }
/*     */ 
/*     */   public void reset() {
/* 203 */     this.leftOver = false;
/* 204 */     this.byteOff = (this.charOff = 0);
/* 205 */     this.started = false;
/* 206 */     this.byteOrder = this.originalByteOrder;
/*     */   }
/*     */ 
/*     */   public int flush(char[] paramArrayOfChar, int paramInt1, int paramInt2)
/*     */     throws MalformedInputException
/*     */   {
/* 212 */     if (this.leftOver) {
/* 213 */       reset();
/* 214 */       throw new MalformedInputException();
/*     */     }
/* 216 */     this.byteOff = (this.charOff = 0);
/* 217 */     return 0;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.io.ByteToCharUnicode
 * JD-Core Version:    0.6.2
 */