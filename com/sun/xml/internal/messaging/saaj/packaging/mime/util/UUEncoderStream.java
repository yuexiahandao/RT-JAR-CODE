/*     */ package com.sun.xml.internal.messaging.saaj.packaging.mime.util;
/*     */ 
/*     */ import java.io.FilterOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.PrintStream;
/*     */ 
/*     */ public class UUEncoderStream extends FilterOutputStream
/*     */ {
/*     */   private byte[] buffer;
/*  47 */   private int bufsize = 0;
/*  48 */   private boolean wrotePrefix = false;
/*     */   protected String name;
/*     */   protected int mode;
/*     */ 
/*     */   public UUEncoderStream(OutputStream out)
/*     */   {
/*  58 */     this(out, "encoder.buf", 644);
/*     */   }
/*     */ 
/*     */   public UUEncoderStream(OutputStream out, String name)
/*     */   {
/*  67 */     this(out, name, 644);
/*     */   }
/*     */ 
/*     */   public UUEncoderStream(OutputStream out, String name, int mode)
/*     */   {
/*  77 */     super(out);
/*  78 */     this.name = name;
/*  79 */     this.mode = mode;
/*  80 */     this.buffer = new byte[45];
/*     */   }
/*     */ 
/*     */   public void setNameMode(String name, int mode)
/*     */   {
/*  89 */     this.name = name;
/*  90 */     this.mode = mode;
/*     */   }
/*     */ 
/*     */   public void write(byte[] b, int off, int len) throws IOException {
/*  94 */     for (int i = 0; i < len; i++)
/*  95 */       write(b[(off + i)]);
/*     */   }
/*     */ 
/*     */   public void write(byte[] data) throws IOException {
/*  99 */     write(data, 0, data.length);
/*     */   }
/*     */ 
/*     */   public void write(int c)
/*     */     throws IOException
/*     */   {
/* 107 */     this.buffer[(this.bufsize++)] = ((byte)c);
/* 108 */     if (this.bufsize == 45) {
/* 109 */       writePrefix();
/* 110 */       encode();
/* 111 */       this.bufsize = 0;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void flush() throws IOException {
/* 116 */     if (this.bufsize > 0) {
/* 117 */       writePrefix();
/* 118 */       encode();
/*     */     }
/* 120 */     writeSuffix();
/* 121 */     this.out.flush();
/*     */   }
/*     */ 
/*     */   public void close() throws IOException {
/* 125 */     flush();
/* 126 */     this.out.close();
/*     */   }
/*     */ 
/*     */   private void writePrefix()
/*     */     throws IOException
/*     */   {
/* 133 */     if (!this.wrotePrefix) {
/* 134 */       PrintStream ps = new PrintStream(this.out);
/* 135 */       ps.println("begin " + this.mode + " " + this.name);
/* 136 */       ps.flush();
/* 137 */       this.wrotePrefix = true;
/*     */     }
/*     */   }
/*     */ 
/*     */   private void writeSuffix()
/*     */     throws IOException
/*     */   {
/* 146 */     PrintStream ps = new PrintStream(this.out);
/* 147 */     ps.println(" \nend");
/* 148 */     ps.flush();
/*     */   }
/*     */ 
/*     */   private void encode()
/*     */     throws IOException
/*     */   {
/* 163 */     int i = 0;
/*     */ 
/* 166 */     this.out.write((this.bufsize & 0x3F) + 32);
/*     */ 
/* 168 */     while (i < this.bufsize) {
/* 169 */       byte a = this.buffer[(i++)];
/*     */       byte c;
/*     */       byte b;
/*     */       byte c;
/* 170 */       if (i < this.bufsize) {
/* 171 */         byte b = this.buffer[(i++)];
/*     */         byte c;
/* 172 */         if (i < this.bufsize)
/* 173 */           c = this.buffer[(i++)];
/*     */         else
/* 175 */           c = 1;
/*     */       }
/*     */       else {
/* 178 */         b = 1;
/* 179 */         c = 1;
/*     */       }
/*     */ 
/* 182 */       int c1 = a >>> 2 & 0x3F;
/* 183 */       int c2 = a << 4 & 0x30 | b >>> 4 & 0xF;
/* 184 */       int c3 = b << 2 & 0x3C | c >>> 6 & 0x3;
/* 185 */       int c4 = c & 0x3F;
/* 186 */       this.out.write(c1 + 32);
/* 187 */       this.out.write(c2 + 32);
/* 188 */       this.out.write(c3 + 32);
/* 189 */       this.out.write(c4 + 32);
/*     */     }
/*     */ 
/* 192 */     this.out.write(10);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.messaging.saaj.packaging.mime.util.UUEncoderStream
 * JD-Core Version:    0.6.2
 */