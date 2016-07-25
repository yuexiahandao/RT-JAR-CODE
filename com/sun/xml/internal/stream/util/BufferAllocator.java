/*     */ package com.sun.xml.internal.stream.util;
/*     */ 
/*     */ public class BufferAllocator
/*     */ {
/*  38 */   public static int SMALL_SIZE_LIMIT = 128;
/*  39 */   public static int MEDIUM_SIZE_LIMIT = 2048;
/*  40 */   public static int LARGE_SIZE_LIMIT = 8192;
/*     */   char[] smallCharBuffer;
/*     */   char[] mediumCharBuffer;
/*     */   char[] largeCharBuffer;
/*     */   byte[] smallByteBuffer;
/*     */   byte[] mediumByteBuffer;
/*     */   byte[] largeByteBuffer;
/*     */ 
/*     */   public char[] getCharBuffer(int size)
/*     */   {
/*  54 */     if (size <= SMALL_SIZE_LIMIT) {
/*  55 */       char[] buffer = this.smallCharBuffer;
/*  56 */       this.smallCharBuffer = null;
/*  57 */       return buffer;
/*     */     }
/*  59 */     if (size <= MEDIUM_SIZE_LIMIT) {
/*  60 */       char[] buffer = this.mediumCharBuffer;
/*  61 */       this.mediumCharBuffer = null;
/*  62 */       return buffer;
/*     */     }
/*  64 */     if (size <= LARGE_SIZE_LIMIT) {
/*  65 */       char[] buffer = this.largeCharBuffer;
/*  66 */       this.largeCharBuffer = null;
/*  67 */       return buffer;
/*     */     }
/*  69 */     return null;
/*     */   }
/*     */ 
/*     */   public void returnCharBuffer(char[] c) {
/*  73 */     if (c == null) {
/*  74 */       return;
/*     */     }
/*  76 */     if (c.length <= SMALL_SIZE_LIMIT) {
/*  77 */       this.smallCharBuffer = c;
/*     */     }
/*  79 */     else if (c.length <= MEDIUM_SIZE_LIMIT) {
/*  80 */       this.mediumCharBuffer = c;
/*     */     }
/*  82 */     else if (c.length <= LARGE_SIZE_LIMIT)
/*  83 */       this.largeCharBuffer = c;
/*     */   }
/*     */ 
/*     */   public byte[] getByteBuffer(int size)
/*     */   {
/*  88 */     if (size <= SMALL_SIZE_LIMIT) {
/*  89 */       byte[] buffer = this.smallByteBuffer;
/*  90 */       this.smallByteBuffer = null;
/*  91 */       return buffer;
/*     */     }
/*  93 */     if (size <= MEDIUM_SIZE_LIMIT) {
/*  94 */       byte[] buffer = this.mediumByteBuffer;
/*  95 */       this.mediumByteBuffer = null;
/*  96 */       return buffer;
/*     */     }
/*  98 */     if (size <= LARGE_SIZE_LIMIT) {
/*  99 */       byte[] buffer = this.largeByteBuffer;
/* 100 */       this.largeByteBuffer = null;
/* 101 */       return buffer;
/*     */     }
/* 103 */     return null;
/*     */   }
/*     */ 
/*     */   public void returnByteBuffer(byte[] b) {
/* 107 */     if (b == null) {
/* 108 */       return;
/*     */     }
/* 110 */     if (b.length <= SMALL_SIZE_LIMIT) {
/* 111 */       this.smallByteBuffer = b;
/*     */     }
/* 113 */     else if (b.length <= MEDIUM_SIZE_LIMIT) {
/* 114 */       this.mediumByteBuffer = b;
/*     */     }
/* 116 */     else if (b.length <= LARGE_SIZE_LIMIT)
/* 117 */       this.largeByteBuffer = b;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.stream.util.BufferAllocator
 * JD-Core Version:    0.6.2
 */