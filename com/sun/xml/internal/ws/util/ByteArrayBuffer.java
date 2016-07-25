/*     */ package com.sun.xml.internal.ws.util;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ 
/*     */ public class ByteArrayBuffer extends OutputStream
/*     */ {
/*     */   protected byte[] buf;
/*     */   private int count;
/*     */   private static final int CHUNK_SIZE = 4096;
/*     */ 
/*     */   public ByteArrayBuffer()
/*     */   {
/*  70 */     this(32);
/*     */   }
/*     */ 
/*     */   public ByteArrayBuffer(int size)
/*     */   {
/*  81 */     if (size <= 0)
/*  82 */       throw new IllegalArgumentException();
/*  83 */     this.buf = new byte[size];
/*     */   }
/*     */ 
/*     */   public ByteArrayBuffer(byte[] data) {
/*  87 */     this(data, data.length);
/*     */   }
/*     */ 
/*     */   public ByteArrayBuffer(byte[] data, int length) {
/*  91 */     this.buf = data;
/*  92 */     this.count = length;
/*     */   }
/*     */ 
/*     */   public final void write(InputStream in)
/*     */     throws IOException
/*     */   {
/*     */     while (true)
/*     */     {
/* 104 */       int cap = this.buf.length - this.count;
/* 105 */       int sz = in.read(this.buf, this.count, cap);
/* 106 */       if (sz < 0) return;
/* 107 */       this.count += sz;
/*     */ 
/* 110 */       if (cap == sz)
/* 111 */         ensureCapacity(this.buf.length * 2);
/*     */     }
/*     */   }
/*     */ 
/*     */   public final void write(int b) {
/* 116 */     int newcount = this.count + 1;
/* 117 */     ensureCapacity(newcount);
/* 118 */     this.buf[this.count] = ((byte)b);
/* 119 */     this.count = newcount;
/*     */   }
/*     */ 
/*     */   public final void write(byte[] b, int off, int len) {
/* 123 */     int newcount = this.count + len;
/* 124 */     ensureCapacity(newcount);
/* 125 */     System.arraycopy(b, off, this.buf, this.count, len);
/* 126 */     this.count = newcount;
/*     */   }
/*     */ 
/*     */   private void ensureCapacity(int newcount) {
/* 130 */     if (newcount > this.buf.length) {
/* 131 */       byte[] newbuf = new byte[Math.max(this.buf.length << 1, newcount)];
/* 132 */       System.arraycopy(this.buf, 0, newbuf, 0, this.count);
/* 133 */       this.buf = newbuf;
/*     */     }
/*     */   }
/*     */ 
/*     */   public final void writeTo(OutputStream out)
/*     */     throws IOException
/*     */   {
/* 142 */     int remaining = this.count;
/* 143 */     int off = 0;
/* 144 */     while (remaining > 0) {
/* 145 */       int chunk = remaining > 4096 ? 4096 : remaining;
/* 146 */       out.write(this.buf, off, chunk);
/* 147 */       remaining -= chunk;
/* 148 */       off += chunk;
/*     */     }
/*     */   }
/*     */ 
/*     */   public final void reset() {
/* 153 */     this.count = 0;
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public final byte[] toByteArray()
/*     */   {
/* 167 */     byte[] newbuf = new byte[this.count];
/* 168 */     System.arraycopy(this.buf, 0, newbuf, 0, this.count);
/* 169 */     return newbuf;
/*     */   }
/*     */ 
/*     */   public final int size() {
/* 173 */     return this.count;
/*     */   }
/*     */ 
/*     */   public final byte[] getRawData()
/*     */   {
/* 183 */     return this.buf;
/*     */   }
/*     */ 
/*     */   public void close()
/*     */     throws IOException
/*     */   {
/*     */   }
/*     */ 
/*     */   public final InputStream newInputStream()
/*     */   {
/* 193 */     return new ByteArrayInputStream(this.buf, 0, this.count);
/*     */   }
/*     */ 
/*     */   public final InputStream newInputStream(int start, int length)
/*     */   {
/* 200 */     return new ByteArrayInputStream(this.buf, start, length);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 211 */     return new String(this.buf, 0, this.count);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.util.ByteArrayBuffer
 * JD-Core Version:    0.6.2
 */