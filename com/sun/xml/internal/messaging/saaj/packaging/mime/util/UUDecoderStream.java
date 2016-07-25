/*     */ package com.sun.xml.internal.messaging.saaj.packaging.mime.util;
/*     */ 
/*     */ import java.io.FilterInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ 
/*     */ public class UUDecoderStream extends FilterInputStream
/*     */ {
/*     */   private String name;
/*     */   private int mode;
/*     */   private byte[] buffer;
/*  51 */   private int bufsize = 0;
/*  52 */   private int index = 0;
/*  53 */   private boolean gotPrefix = false;
/*  54 */   private boolean gotEnd = false;
/*     */   private LineInputStream lin;
/*     */ 
/*     */   public UUDecoderStream(InputStream in)
/*     */   {
/*  62 */     super(in);
/*  63 */     this.lin = new LineInputStream(in);
/*  64 */     this.buffer = new byte[45];
/*     */   }
/*     */ 
/*     */   public int read()
/*     */     throws IOException
/*     */   {
/*  82 */     if (this.index >= this.bufsize) {
/*  83 */       readPrefix();
/*  84 */       if (!decode())
/*  85 */         return -1;
/*  86 */       this.index = 0;
/*     */     }
/*  88 */     return this.buffer[(this.index++)] & 0xFF;
/*     */   }
/*     */ 
/*     */   public int read(byte[] buf, int off, int len) throws IOException
/*     */   {
/*  93 */     for (int i = 0; i < len; i++)
/*     */     {
/*     */       int c;
/*  94 */       if ((c = read()) == -1) {
/*  95 */         if (i != 0) break;
/*  96 */         i = -1; break;
/*     */       }
/*     */ 
/*  99 */       buf[(off + i)] = ((byte)c);
/*     */     }
/* 101 */     return i;
/*     */   }
/*     */ 
/*     */   public boolean markSupported() {
/* 105 */     return false;
/*     */   }
/*     */ 
/*     */   public int available()
/*     */     throws IOException
/*     */   {
/* 111 */     return this.in.available() * 3 / 4 + (this.bufsize - this.index);
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */     throws IOException
/*     */   {
/* 122 */     readPrefix();
/* 123 */     return this.name;
/*     */   }
/*     */ 
/*     */   public int getMode()
/*     */     throws IOException
/*     */   {
/* 134 */     readPrefix();
/* 135 */     return this.mode;
/*     */   }
/*     */ 
/*     */   private void readPrefix()
/*     */     throws IOException
/*     */   {
/* 144 */     if (this.gotPrefix) {
/*     */       return;
/*     */     }
/*     */     String s;
/*     */     do
/*     */     {
/* 150 */       s = this.lin.readLine();
/* 151 */       if (s == null)
/* 152 */         throw new IOException("UUDecoder error: No Begin"); 
/*     */     }
/* 153 */     while (!s.regionMatches(true, 0, "begin", 0, 5));
/*     */     try {
/* 155 */       this.mode = Integer.parseInt(s.substring(6, 9));
/*     */     } catch (NumberFormatException ex) {
/* 157 */       throw new IOException("UUDecoder error: " + ex.toString());
/*     */     }
/* 159 */     this.name = s.substring(10);
/* 160 */     this.gotPrefix = true;
/*     */   }
/*     */ 
/*     */   private boolean decode()
/*     */     throws IOException
/*     */   {
/* 168 */     if (this.gotEnd)
/* 169 */       return false; this.bufsize = 0;
/*     */     String line;
/*     */     do {
/* 173 */       line = this.lin.readLine();
/*     */ 
/* 180 */       if (line == null)
/* 181 */         throw new IOException("Missing End");
/* 182 */       if (line.regionMatches(true, 0, "end", 0, 3)) {
/* 183 */         this.gotEnd = true;
/* 184 */         return false;
/*     */       }
/*     */     }
/* 186 */     while (line.length() == 0);
/* 187 */     int count = line.charAt(0);
/* 188 */     if (count < 32) {
/* 189 */       throw new IOException("Buffer format error");
/*     */     }
/*     */ 
/* 197 */     count = count - 32 & 0x3F;
/*     */ 
/* 199 */     if (count == 0) {
/* 200 */       line = this.lin.readLine();
/* 201 */       if ((line == null) || (!line.regionMatches(true, 0, "end", 0, 3)))
/* 202 */         throw new IOException("Missing End");
/* 203 */       this.gotEnd = true;
/* 204 */       return false;
/*     */     }
/*     */ 
/* 207 */     int need = (count * 8 + 5) / 6;
/*     */ 
/* 209 */     if (line.length() < need + 1) {
/* 210 */       throw new IOException("Short buffer error");
/*     */     }
/* 212 */     int i = 1;
/*     */ 
/* 220 */     while (this.bufsize < count)
/*     */     {
/* 222 */       byte a = (byte)(line.charAt(i++) - ' ' & 0x3F);
/* 223 */       byte b = (byte)(line.charAt(i++) - ' ' & 0x3F);
/* 224 */       this.buffer[(this.bufsize++)] = ((byte)(a << 2 & 0xFC | b >>> 4 & 0x3));
/*     */ 
/* 226 */       if (this.bufsize < count) {
/* 227 */         a = b;
/* 228 */         b = (byte)(line.charAt(i++) - ' ' & 0x3F);
/* 229 */         this.buffer[(this.bufsize++)] = ((byte)(a << 4 & 0xF0 | b >>> 2 & 0xF));
/*     */       }
/*     */ 
/* 233 */       if (this.bufsize < count) {
/* 234 */         a = b;
/* 235 */         b = (byte)(line.charAt(i++) - ' ' & 0x3F);
/* 236 */         this.buffer[(this.bufsize++)] = ((byte)(a << 6 & 0xC0 | b & 0x3F));
/*     */       }
/*     */     }
/* 239 */     return true;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.messaging.saaj.packaging.mime.util.UUDecoderStream
 * JD-Core Version:    0.6.2
 */