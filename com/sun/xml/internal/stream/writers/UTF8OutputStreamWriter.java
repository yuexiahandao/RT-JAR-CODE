/*     */ package com.sun.xml.internal.stream.writers;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.util.XMLChar;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.Writer;
/*     */ 
/*     */ public final class UTF8OutputStreamWriter extends Writer
/*     */ {
/*     */   OutputStream out;
/*  60 */   int lastUTF16CodePoint = 0;
/*     */ 
/*     */   public UTF8OutputStreamWriter(OutputStream out) {
/*  63 */     this.out = out;
/*     */   }
/*     */ 
/*     */   public String getEncoding() {
/*  67 */     return "UTF-8";
/*     */   }
/*     */ 
/*     */   public void write(int c) throws IOException
/*     */   {
/*  72 */     if (this.lastUTF16CodePoint != 0) {
/*  73 */       int uc = ((this.lastUTF16CodePoint & 0x3FF) << 10 | c & 0x3FF) + 65536;
/*     */ 
/*  76 */       if ((uc < 0) || (uc >= 2097152)) {
/*  77 */         throw new IOException("Atttempting to write invalid Unicode code point '" + uc + "'");
/*     */       }
/*     */ 
/*  80 */       this.out.write(0xF0 | uc >> 18);
/*  81 */       this.out.write(0x80 | uc >> 12 & 0x3F);
/*  82 */       this.out.write(0x80 | uc >> 6 & 0x3F);
/*  83 */       this.out.write(0x80 | uc & 0x3F);
/*     */ 
/*  85 */       this.lastUTF16CodePoint = 0;
/*  86 */       return;
/*     */     }
/*     */ 
/*  90 */     if (c < 128)
/*     */     {
/*  92 */       this.out.write(c);
/*     */     }
/*  94 */     else if (c < 2048)
/*     */     {
/*  96 */       this.out.write(0xC0 | c >> 6);
/*  97 */       this.out.write(0x80 | c & 0x3F);
/*     */     }
/*  99 */     else if (c <= 65535) {
/* 100 */       if ((!XMLChar.isHighSurrogate(c)) && (!XMLChar.isLowSurrogate(c)))
/*     */       {
/* 102 */         this.out.write(0xE0 | c >> 12);
/* 103 */         this.out.write(0x80 | c >> 6 & 0x3F);
/* 104 */         this.out.write(0x80 | c & 0x3F);
/*     */       }
/*     */       else {
/* 107 */         this.lastUTF16CodePoint = c;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void write(char[] cbuf) throws IOException {
/* 113 */     for (int i = 0; i < cbuf.length; i++)
/* 114 */       write(cbuf[i]);
/*     */   }
/*     */ 
/*     */   public void write(char[] cbuf, int off, int len) throws IOException
/*     */   {
/* 119 */     for (int i = 0; i < len; i++)
/* 120 */       write(cbuf[(off + i)]);
/*     */   }
/*     */ 
/*     */   public void write(String str) throws IOException
/*     */   {
/* 125 */     int len = str.length();
/* 126 */     for (int i = 0; i < len; i++)
/* 127 */       write(str.charAt(i));
/*     */   }
/*     */ 
/*     */   public void write(String str, int off, int len) throws IOException
/*     */   {
/* 132 */     for (int i = 0; i < len; i++)
/* 133 */       write(str.charAt(off + i));
/*     */   }
/*     */ 
/*     */   public void flush() throws IOException
/*     */   {
/* 138 */     this.out.flush();
/*     */   }
/*     */ 
/*     */   public void close() throws IOException {
/* 142 */     if (this.lastUTF16CodePoint != 0) {
/* 143 */       throw new IllegalStateException("Attempting to close a UTF8OutputStreamWriter while awaiting for a UTF-16 code unit");
/*     */     }
/*     */ 
/* 146 */     this.out.close();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.stream.writers.UTF8OutputStreamWriter
 * JD-Core Version:    0.6.2
 */