/*     */ package com.sun.xml.internal.messaging.saaj.packaging.mime.util;
/*     */ 
/*     */ import java.io.FilterOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ 
/*     */ public class BASE64EncoderStream extends FilterOutputStream
/*     */ {
/*     */   private byte[] buffer;
/*  48 */   private int bufsize = 0;
/*  49 */   private int count = 0;
/*     */   private int bytesPerLine;
/* 133 */   private static final char[] pem_array = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/' };
/*     */ 
/*     */   public BASE64EncoderStream(OutputStream out, int bytesPerLine)
/*     */   {
/*  59 */     super(out);
/*  60 */     this.buffer = new byte[3];
/*  61 */     this.bytesPerLine = bytesPerLine;
/*     */   }
/*     */ 
/*     */   public BASE64EncoderStream(OutputStream out)
/*     */   {
/*  70 */     this(out, 76);
/*     */   }
/*     */ 
/*     */   public void write(byte[] b, int off, int len)
/*     */     throws IOException
/*     */   {
/*  84 */     for (int i = 0; i < len; i++)
/*  85 */       write(b[(off + i)]);
/*     */   }
/*     */ 
/*     */   public void write(byte[] b)
/*     */     throws IOException
/*     */   {
/*  94 */     write(b, 0, b.length);
/*     */   }
/*     */ 
/*     */   public void write(int c)
/*     */     throws IOException
/*     */   {
/* 103 */     this.buffer[(this.bufsize++)] = ((byte)c);
/* 104 */     if (this.bufsize == 3) {
/* 105 */       encode();
/* 106 */       this.bufsize = 0;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void flush()
/*     */     throws IOException
/*     */   {
/* 116 */     if (this.bufsize > 0) {
/* 117 */       encode();
/* 118 */       this.bufsize = 0;
/*     */     }
/* 120 */     this.out.flush();
/*     */   }
/*     */ 
/*     */   public void close()
/*     */     throws IOException
/*     */   {
/* 128 */     flush();
/* 129 */     this.out.close();
/*     */   }
/*     */ 
/*     */   private void encode()
/*     */     throws IOException
/*     */   {
/* 147 */     if (this.count + 4 > this.bytesPerLine) {
/* 148 */       this.out.write(13);
/* 149 */       this.out.write(10);
/* 150 */       this.count = 0;
/*     */     }
/*     */ 
/* 154 */     if (this.bufsize == 1) {
/* 155 */       byte a = this.buffer[0];
/* 156 */       byte b = 0;
/* 157 */       byte c = 0;
/* 158 */       this.out.write(pem_array[(a >>> 2 & 0x3F)]);
/* 159 */       this.out.write(pem_array[((a << 4 & 0x30) + (b >>> 4 & 0xF))]);
/* 160 */       this.out.write(61);
/* 161 */       this.out.write(61);
/* 162 */     } else if (this.bufsize == 2) {
/* 163 */       byte a = this.buffer[0];
/* 164 */       byte b = this.buffer[1];
/* 165 */       byte c = 0;
/* 166 */       this.out.write(pem_array[(a >>> 2 & 0x3F)]);
/* 167 */       this.out.write(pem_array[((a << 4 & 0x30) + (b >>> 4 & 0xF))]);
/* 168 */       this.out.write(pem_array[((b << 2 & 0x3C) + (c >>> 6 & 0x3))]);
/* 169 */       this.out.write(61);
/*     */     } else {
/* 171 */       byte a = this.buffer[0];
/* 172 */       byte b = this.buffer[1];
/* 173 */       byte c = this.buffer[2];
/* 174 */       this.out.write(pem_array[(a >>> 2 & 0x3F)]);
/* 175 */       this.out.write(pem_array[((a << 4 & 0x30) + (b >>> 4 & 0xF))]);
/* 176 */       this.out.write(pem_array[((b << 2 & 0x3C) + (c >>> 6 & 0x3))]);
/* 177 */       this.out.write(pem_array[(c & 0x3F)]);
/*     */     }
/*     */ 
/* 181 */     this.count += 4;
/*     */   }
/*     */ 
/*     */   public static byte[] encode(byte[] inbuf)
/*     */   {
/* 191 */     if (inbuf.length == 0)
/* 192 */       return inbuf;
/* 193 */     byte[] outbuf = new byte[(inbuf.length + 2) / 3 * 4];
/* 194 */     int inpos = 0; int outpos = 0;
/* 195 */     int size = inbuf.length;
/* 196 */     while (size > 0)
/*     */     {
/* 198 */       if (size == 1) {
/* 199 */         byte a = inbuf[(inpos++)];
/* 200 */         byte b = 0;
/* 201 */         byte c = 0;
/* 202 */         outbuf[(outpos++)] = ((byte)pem_array[(a >>> 2 & 0x3F)]);
/* 203 */         outbuf[(outpos++)] = ((byte)pem_array[((a << 4 & 0x30) + (b >>> 4 & 0xF))]);
/*     */ 
/* 205 */         outbuf[(outpos++)] = 61;
/* 206 */         outbuf[(outpos++)] = 61;
/* 207 */       } else if (size == 2) {
/* 208 */         byte a = inbuf[(inpos++)];
/* 209 */         byte b = inbuf[(inpos++)];
/* 210 */         byte c = 0;
/* 211 */         outbuf[(outpos++)] = ((byte)pem_array[(a >>> 2 & 0x3F)]);
/* 212 */         outbuf[(outpos++)] = ((byte)pem_array[((a << 4 & 0x30) + (b >>> 4 & 0xF))]);
/*     */ 
/* 214 */         outbuf[(outpos++)] = ((byte)pem_array[((b << 2 & 0x3C) + (c >>> 6 & 0x3))]);
/*     */ 
/* 216 */         outbuf[(outpos++)] = 61;
/*     */       } else {
/* 218 */         byte a = inbuf[(inpos++)];
/* 219 */         byte b = inbuf[(inpos++)];
/* 220 */         byte c = inbuf[(inpos++)];
/* 221 */         outbuf[(outpos++)] = ((byte)pem_array[(a >>> 2 & 0x3F)]);
/* 222 */         outbuf[(outpos++)] = ((byte)pem_array[((a << 4 & 0x30) + (b >>> 4 & 0xF))]);
/*     */ 
/* 224 */         outbuf[(outpos++)] = ((byte)pem_array[((b << 2 & 0x3C) + (c >>> 6 & 0x3))]);
/*     */ 
/* 226 */         outbuf[(outpos++)] = ((byte)pem_array[(c & 0x3F)]);
/*     */       }
/* 228 */       size -= 3;
/*     */     }
/* 230 */     return outbuf;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.messaging.saaj.packaging.mime.util.BASE64EncoderStream
 * JD-Core Version:    0.6.2
 */