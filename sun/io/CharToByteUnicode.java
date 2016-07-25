/*     */ package sun.io;
/*     */ 
/*     */ import java.security.AccessController;
/*     */ import sun.security.action.GetPropertyAction;
/*     */ 
/*     */ public class CharToByteUnicode extends CharToByteConverter
/*     */ {
/*     */   static final char BYTE_ORDER_MARK = '﻿';
/*  40 */   protected boolean usesMark = true;
/*  41 */   private boolean markWritten = false;
/*     */   static final int UNKNOWN = 0;
/*     */   static final int BIG = 1;
/*     */   static final int LITTLE = 2;
/*  46 */   protected int byteOrder = 0;
/*     */ 
/*     */   public CharToByteUnicode() {
/*  49 */     String str = (String)AccessController.doPrivileged(new GetPropertyAction("sun.io.unicode.encoding", "UnicodeBig"));
/*     */ 
/*  52 */     if (str.equals("UnicodeBig"))
/*  53 */       this.byteOrder = 1;
/*  54 */     else if (str.equals("UnicodeLittle"))
/*  55 */       this.byteOrder = 2;
/*     */     else
/*  57 */       this.byteOrder = 1;
/*     */   }
/*     */ 
/*     */   public CharToByteUnicode(int paramInt, boolean paramBoolean) {
/*  61 */     this.byteOrder = paramInt;
/*  62 */     this.usesMark = paramBoolean;
/*     */   }
/*     */ 
/*     */   public CharToByteUnicode(boolean paramBoolean) {
/*  66 */     this();
/*  67 */     this.usesMark = paramBoolean;
/*     */   }
/*     */ 
/*     */   public String getCharacterEncoding() {
/*  71 */     switch (this.byteOrder) {
/*     */     case 1:
/*  73 */       return this.usesMark ? "UnicodeBig" : "UnicodeBigUnmarked";
/*     */     case 2:
/*  75 */       return this.usesMark ? "UnicodeLittle" : "UnicodeLittleUnmarked";
/*     */     }
/*  77 */     return "UnicodeUnknown";
/*     */   }
/*     */ 
/*     */   public int convert(char[] paramArrayOfChar, int paramInt1, int paramInt2, byte[] paramArrayOfByte, int paramInt3, int paramInt4)
/*     */     throws ConversionBufferFullException, MalformedInputException
/*     */   {
/*  85 */     this.charOff = paramInt1;
/*  86 */     this.byteOff = paramInt3;
/*     */ 
/*  88 */     if (paramInt1 >= paramInt2) {
/*  89 */       return 0;
/*     */     }
/*  91 */     int i = paramInt1;
/*  92 */     int j = paramInt3;
/*  93 */     int k = paramInt4 - 2;
/*     */ 
/*  95 */     if ((this.usesMark) && (!this.markWritten)) {
/*  96 */       if (j > k)
/*  97 */         throw new ConversionBufferFullException();
/*  98 */       if (this.byteOrder == 1) {
/*  99 */         paramArrayOfByte[(j++)] = -2;
/* 100 */         paramArrayOfByte[(j++)] = -1;
/*     */       }
/*     */       else {
/* 103 */         paramArrayOfByte[(j++)] = -1;
/* 104 */         paramArrayOfByte[(j++)] = -2;
/*     */       }
/* 106 */       this.markWritten = true;
/*     */     }
/*     */     int m;
/* 109 */     if (this.byteOrder == 1) {
/* 110 */       while (i < paramInt2) {
/* 111 */         if (j > k) {
/* 112 */           this.charOff = i;
/* 113 */           this.byteOff = j;
/* 114 */           throw new ConversionBufferFullException();
/*     */         }
/* 116 */         m = paramArrayOfChar[(i++)];
/* 117 */         paramArrayOfByte[(j++)] = ((byte)(m >> 8));
/* 118 */         paramArrayOfByte[(j++)] = ((byte)(m & 0xFF));
/*     */       }
/*     */     }
/*     */ 
/* 122 */     while (i < paramInt2) {
/* 123 */       if (j > k) {
/* 124 */         this.charOff = i;
/* 125 */         this.byteOff = j;
/* 126 */         throw new ConversionBufferFullException();
/*     */       }
/* 128 */       m = paramArrayOfChar[(i++)];
/* 129 */       paramArrayOfByte[(j++)] = ((byte)(m & 0xFF));
/* 130 */       paramArrayOfByte[(j++)] = ((byte)(m >> 8));
/*     */     }
/*     */ 
/* 134 */     this.charOff = i;
/* 135 */     this.byteOff = j;
/* 136 */     return j - paramInt3;
/*     */   }
/*     */ 
/*     */   public int flush(byte[] paramArrayOfByte, int paramInt1, int paramInt2) {
/* 140 */     this.byteOff = (this.charOff = 0);
/* 141 */     return 0;
/*     */   }
/*     */ 
/*     */   public void reset() {
/* 145 */     this.byteOff = (this.charOff = 0);
/* 146 */     this.markWritten = false;
/*     */   }
/*     */ 
/*     */   public int getMaxBytesPerChar() {
/* 150 */     return 4;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.io.CharToByteUnicode
 * JD-Core Version:    0.6.2
 */