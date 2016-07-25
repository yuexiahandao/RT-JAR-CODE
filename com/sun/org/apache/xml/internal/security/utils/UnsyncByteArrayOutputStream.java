/*     */ package com.sun.org.apache.xml.internal.security.utils;
/*     */ 
/*     */ import java.io.OutputStream;
/*     */ 
/*     */ public class UnsyncByteArrayOutputStream extends OutputStream
/*     */ {
/*     */   private static final int INITIAL_SIZE = 8192;
/*  32 */   private static ThreadLocal bufCache = new ThreadLocal() {
/*     */     protected synchronized Object initialValue() {
/*  34 */       return new byte[8192];
/*     */     }
/*  32 */   };
/*     */   private byte[] buf;
/*  39 */   private int size = 8192;
/*  40 */   private int pos = 0;
/*     */ 
/*     */   public UnsyncByteArrayOutputStream() {
/*  43 */     this.buf = ((byte[])bufCache.get());
/*     */   }
/*     */ 
/*     */   public void write(byte[] paramArrayOfByte) {
/*  47 */     if (2147483647 - this.pos < paramArrayOfByte.length) {
/*  48 */       throw new OutOfMemoryError();
/*     */     }
/*  50 */     int i = this.pos + paramArrayOfByte.length;
/*  51 */     if (i > this.size) {
/*  52 */       expandSize(i);
/*     */     }
/*  54 */     System.arraycopy(paramArrayOfByte, 0, this.buf, this.pos, paramArrayOfByte.length);
/*  55 */     this.pos = i;
/*     */   }
/*     */ 
/*     */   public void write(byte[] paramArrayOfByte, int paramInt1, int paramInt2) {
/*  59 */     if (2147483647 - this.pos < paramInt2) {
/*  60 */       throw new OutOfMemoryError();
/*     */     }
/*  62 */     int i = this.pos + paramInt2;
/*  63 */     if (i > this.size) {
/*  64 */       expandSize(i);
/*     */     }
/*  66 */     System.arraycopy(paramArrayOfByte, paramInt1, this.buf, this.pos, paramInt2);
/*  67 */     this.pos = i;
/*     */   }
/*     */ 
/*     */   public void write(int paramInt) {
/*  71 */     if (2147483647 - this.pos == 0) {
/*  72 */       throw new OutOfMemoryError();
/*     */     }
/*  74 */     int i = this.pos + 1;
/*  75 */     if (i > this.size) {
/*  76 */       expandSize(i);
/*     */     }
/*  78 */     this.buf[(this.pos++)] = ((byte)paramInt);
/*     */   }
/*     */ 
/*     */   public byte[] toByteArray() {
/*  82 */     byte[] arrayOfByte = new byte[this.pos];
/*  83 */     System.arraycopy(this.buf, 0, arrayOfByte, 0, this.pos);
/*  84 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */   public void reset() {
/*  88 */     this.pos = 0;
/*     */   }
/*     */ 
/*     */   private void expandSize(int paramInt) {
/*  92 */     int i = this.size;
/*  93 */     while (paramInt > i) {
/*  94 */       i <<= 1;
/*     */ 
/*  96 */       if (i < 0) {
/*  97 */         i = 2147483647;
/*     */       }
/*     */     }
/* 100 */     byte[] arrayOfByte = new byte[i];
/* 101 */     System.arraycopy(this.buf, 0, arrayOfByte, 0, this.pos);
/* 102 */     this.buf = arrayOfByte;
/* 103 */     this.size = i;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.security.utils.UnsyncByteArrayOutputStream
 * JD-Core Version:    0.6.2
 */