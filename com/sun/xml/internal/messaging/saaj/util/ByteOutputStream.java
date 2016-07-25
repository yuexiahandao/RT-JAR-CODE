/*     */ package com.sun.xml.internal.messaging.saaj.util;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ 
/*     */ public final class ByteOutputStream extends OutputStream
/*     */ {
/*     */   protected byte[] buf;
/*  55 */   protected int count = 0;
/*     */ 
/*     */   public ByteOutputStream() {
/*  58 */     this(1024);
/*     */   }
/*     */ 
/*     */   public ByteOutputStream(int size) {
/*  62 */     this.buf = new byte[size];
/*     */   }
/*     */ 
/*     */   public void write(InputStream in)
/*     */     throws IOException
/*     */   {
/*  69 */     if ((in instanceof ByteArrayInputStream)) {
/*  70 */       int size = in.available();
/*  71 */       ensureCapacity(size);
/*  72 */       this.count += in.read(this.buf, this.count, size);
/*     */     }
/*     */     else {
/*     */       while (true) {
/*  76 */         int cap = this.buf.length - this.count;
/*  77 */         int sz = in.read(this.buf, this.count, cap);
/*  78 */         if (sz < 0) return;
/*     */ 
/*  80 */         this.count += sz;
/*  81 */         if (cap == sz)
/*     */         {
/*  83 */           ensureCapacity(this.count); } 
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*  88 */   public void write(int b) { ensureCapacity(1);
/*  89 */     this.buf[this.count] = ((byte)b);
/*  90 */     this.count += 1;
/*     */   }
/*     */ 
/*     */   private void ensureCapacity(int space)
/*     */   {
/*  97 */     int newcount = space + this.count;
/*  98 */     if (newcount > this.buf.length) {
/*  99 */       byte[] newbuf = new byte[Math.max(this.buf.length << 1, newcount)];
/* 100 */       System.arraycopy(this.buf, 0, newbuf, 0, this.count);
/* 101 */       this.buf = newbuf;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void write(byte[] b, int off, int len) {
/* 106 */     ensureCapacity(len);
/* 107 */     System.arraycopy(b, off, this.buf, this.count, len);
/* 108 */     this.count += len;
/*     */   }
/*     */ 
/*     */   public void write(byte[] b) {
/* 112 */     write(b, 0, b.length);
/*     */   }
/*     */ 
/*     */   public void writeAsAscii(String s)
/*     */   {
/* 119 */     int len = s.length();
/*     */ 
/* 121 */     ensureCapacity(len);
/*     */ 
/* 123 */     int ptr = this.count;
/* 124 */     for (int i = 0; i < len; i++)
/* 125 */       this.buf[(ptr++)] = ((byte)s.charAt(i));
/* 126 */     this.count = ptr;
/*     */   }
/*     */ 
/*     */   public void writeTo(OutputStream out) throws IOException {
/* 130 */     out.write(this.buf, 0, this.count);
/*     */   }
/*     */ 
/*     */   public void reset() {
/* 134 */     this.count = 0;
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public byte[] toByteArray()
/*     */   {
/* 145 */     byte[] newbuf = new byte[this.count];
/* 146 */     System.arraycopy(this.buf, 0, newbuf, 0, this.count);
/* 147 */     return newbuf;
/*     */   }
/*     */ 
/*     */   public int size() {
/* 151 */     return this.count;
/*     */   }
/*     */ 
/*     */   public ByteInputStream newInputStream() {
/* 155 */     return new ByteInputStream(this.buf, this.count);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 166 */     return new String(this.buf, 0, this.count);
/*     */   }
/*     */ 
/*     */   public void close() {
/*     */   }
/*     */ 
/*     */   public byte[] getBytes() {
/* 173 */     return this.buf;
/*     */   }
/*     */ 
/*     */   public int getCount()
/*     */   {
/* 178 */     return this.count;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.messaging.saaj.util.ByteOutputStream
 * JD-Core Version:    0.6.2
 */