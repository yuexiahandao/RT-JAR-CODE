/*     */ package com.sun.xml.internal.messaging.saaj.packaging.mime.util;
/*     */ 
/*     */ import java.io.FilterInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ 
/*     */ public class BASE64DecoderStream extends FilterInputStream
/*     */ {
/*     */   private byte[] buffer;
/*  48 */   private int bufsize = 0;
/*  49 */   private int index = 0;
/*     */ 
/* 135 */   private static final char[] pem_array = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/' };
/*     */ 
/* 146 */   private static final byte[] pem_convert_array = new byte[256];
/*     */ 
/* 156 */   private byte[] decode_buffer = new byte[4];
/*     */ 
/*     */   public BASE64DecoderStream(InputStream in)
/*     */   {
/*  56 */     super(in);
/*  57 */     this.buffer = new byte[3];
/*     */   }
/*     */ 
/*     */   public int read()
/*     */     throws IOException
/*     */   {
/*  74 */     if (this.index >= this.bufsize) {
/*  75 */       decode();
/*  76 */       if (this.bufsize == 0)
/*  77 */         return -1;
/*  78 */       this.index = 0;
/*     */     }
/*  80 */     return this.buffer[(this.index++)] & 0xFF;
/*     */   }
/*     */ 
/*     */   public int read(byte[] buf, int off, int len)
/*     */     throws IOException
/*     */   {
/*  99 */     for (int i = 0; i < len; i++)
/*     */     {
/*     */       int c;
/* 100 */       if ((c = read()) == -1) {
/* 101 */         if (i != 0) break;
/* 102 */         i = -1; break;
/*     */       }
/*     */ 
/* 105 */       buf[(off + i)] = ((byte)c);
/*     */     }
/*     */ 
/* 108 */     return i;
/*     */   }
/*     */ 
/*     */   public boolean markSupported()
/*     */   {
/* 116 */     return false;
/*     */   }
/*     */ 
/*     */   public int available()
/*     */     throws IOException
/*     */   {
/* 128 */     return this.in.available() * 3 / 4 + (this.bufsize - this.index);
/*     */   }
/*     */ 
/*     */   private void decode()
/*     */     throws IOException
/*     */   {
/* 158 */     this.bufsize = 0;
/*     */ 
/* 164 */     int got = 0;
/* 165 */     while (got < 4) {
/* 166 */       int i = this.in.read();
/* 167 */       if (i == -1) {
/* 168 */         if (got == 0)
/* 169 */           return;
/* 170 */         throw new IOException("Error in encoded stream, got " + got);
/*     */       }
/* 172 */       if (((i >= 0) && (i < 256) && (i == 61)) || (pem_convert_array[i] != -1)) {
/* 173 */         this.decode_buffer[(got++)] = ((byte)i);
/*     */       }
/*     */     }
/*     */ 
/* 177 */     byte a = pem_convert_array[(this.decode_buffer[0] & 0xFF)];
/* 178 */     byte b = pem_convert_array[(this.decode_buffer[1] & 0xFF)];
/*     */ 
/* 180 */     this.buffer[(this.bufsize++)] = ((byte)(a << 2 & 0xFC | b >>> 4 & 0x3));
/*     */ 
/* 182 */     if (this.decode_buffer[2] == 61)
/* 183 */       return;
/* 184 */     a = b;
/* 185 */     b = pem_convert_array[(this.decode_buffer[2] & 0xFF)];
/*     */ 
/* 187 */     this.buffer[(this.bufsize++)] = ((byte)(a << 4 & 0xF0 | b >>> 2 & 0xF));
/*     */ 
/* 189 */     if (this.decode_buffer[3] == 61)
/* 190 */       return;
/* 191 */     a = b;
/* 192 */     b = pem_convert_array[(this.decode_buffer[3] & 0xFF)];
/*     */ 
/* 194 */     this.buffer[(this.bufsize++)] = ((byte)(a << 6 & 0xC0 | b & 0x3F));
/*     */   }
/*     */ 
/*     */   public static byte[] decode(byte[] inbuf)
/*     */   {
/* 207 */     int size = inbuf.length / 4 * 3;
/* 208 */     if (size == 0) {
/* 209 */       return inbuf;
/*     */     }
/* 211 */     if (inbuf[(inbuf.length - 1)] == 61) {
/* 212 */       size--;
/* 213 */       if (inbuf[(inbuf.length - 2)] == 61)
/* 214 */         size--;
/*     */     }
/* 216 */     byte[] outbuf = new byte[size];
/*     */ 
/* 218 */     int inpos = 0; int outpos = 0;
/* 219 */     size = inbuf.length;
/* 220 */     while (size > 0)
/*     */     {
/* 222 */       byte a = pem_convert_array[(inbuf[(inpos++)] & 0xFF)];
/* 223 */       byte b = pem_convert_array[(inbuf[(inpos++)] & 0xFF)];
/*     */ 
/* 225 */       outbuf[(outpos++)] = ((byte)(a << 2 & 0xFC | b >>> 4 & 0x3));
/*     */ 
/* 227 */       if (inbuf[inpos] == 61)
/* 228 */         return outbuf;
/* 229 */       a = b;
/* 230 */       b = pem_convert_array[(inbuf[(inpos++)] & 0xFF)];
/*     */ 
/* 232 */       outbuf[(outpos++)] = ((byte)(a << 4 & 0xF0 | b >>> 2 & 0xF));
/*     */ 
/* 234 */       if (inbuf[inpos] == 61)
/* 235 */         return outbuf;
/* 236 */       a = b;
/* 237 */       b = pem_convert_array[(inbuf[(inpos++)] & 0xFF)];
/*     */ 
/* 239 */       outbuf[(outpos++)] = ((byte)(a << 6 & 0xC0 | b & 0x3F));
/* 240 */       size -= 4;
/*     */     }
/* 242 */     return outbuf;
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/* 149 */     for (int i = 0; i < 255; i++)
/* 150 */       pem_convert_array[i] = -1;
/* 151 */     for (int i = 0; i < pem_array.length; i++)
/* 152 */       pem_convert_array[pem_array[i]] = ((byte)i);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.messaging.saaj.packaging.mime.util.BASE64DecoderStream
 * JD-Core Version:    0.6.2
 */