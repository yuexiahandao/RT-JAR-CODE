/*     */ package com.sun.xml.internal.messaging.saaj.packaging.mime.util;
/*     */ 
/*     */ import java.io.FilterOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ 
/*     */ public class QPEncoderStream extends FilterOutputStream
/*     */ {
/*  46 */   private int count = 0;
/*     */   private int bytesPerLine;
/*  48 */   private boolean gotSpace = false;
/*  49 */   private boolean gotCR = false;
/*     */ 
/* 161 */   private static final char[] hex = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
/*     */ 
/*     */   public QPEncoderStream(OutputStream out, int bytesPerLine)
/*     */   {
/*  59 */     super(out);
/*     */ 
/*  62 */     this.bytesPerLine = (bytesPerLine - 1);
/*     */   }
/*     */ 
/*     */   public QPEncoderStream(OutputStream out)
/*     */   {
/*  71 */     this(out, 76);
/*     */   }
/*     */ 
/*     */   public void write(byte[] b, int off, int len)
/*     */     throws IOException
/*     */   {
/*  85 */     for (int i = 0; i < len; i++)
/*  86 */       write(b[(off + i)]);
/*     */   }
/*     */ 
/*     */   public void write(byte[] b)
/*     */     throws IOException
/*     */   {
/*  95 */     write(b, 0, b.length);
/*     */   }
/*     */ 
/*     */   public void write(int c)
/*     */     throws IOException
/*     */   {
/* 104 */     c &= 255;
/* 105 */     if (this.gotSpace) {
/* 106 */       if ((c == 13) || (c == 10))
/*     */       {
/* 108 */         output(32, true);
/*     */       }
/* 110 */       else output(32, false);
/* 111 */       this.gotSpace = false;
/*     */     }
/*     */ 
/* 114 */     if (c == 13) {
/* 115 */       this.gotCR = true;
/* 116 */       outputCRLF();
/*     */     } else {
/* 118 */       if (c == 10) {
/* 119 */         if (!this.gotCR)
/*     */         {
/* 124 */           outputCRLF();
/*     */         } } else if (c == 32)
/* 126 */         this.gotSpace = true;
/* 127 */       else if ((c < 32) || (c >= 127) || (c == 61))
/*     */       {
/* 129 */         output(c, true);
/*     */       }
/* 131 */       else output(c, false);
/*     */ 
/* 133 */       this.gotCR = false;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void flush()
/*     */     throws IOException
/*     */   {
/* 143 */     this.out.flush();
/*     */   }
/*     */ 
/*     */   public void close()
/*     */     throws IOException
/*     */   {
/* 151 */     this.out.close();
/*     */   }
/*     */ 
/*     */   private void outputCRLF() throws IOException {
/* 155 */     this.out.write(13);
/* 156 */     this.out.write(10);
/* 157 */     this.count = 0;
/*     */   }
/*     */ 
/*     */   protected void output(int c, boolean encode)
/*     */     throws IOException
/*     */   {
/* 167 */     if (encode) {
/* 168 */       if (this.count += 3 > this.bytesPerLine) {
/* 169 */         this.out.write(61);
/* 170 */         this.out.write(13);
/* 171 */         this.out.write(10);
/* 172 */         this.count = 3;
/*     */       }
/* 174 */       this.out.write(61);
/* 175 */       this.out.write(hex[(c >> 4)]);
/* 176 */       this.out.write(hex[(c & 0xF)]);
/*     */     } else {
/* 178 */       if (++this.count > this.bytesPerLine) {
/* 179 */         this.out.write(61);
/* 180 */         this.out.write(13);
/* 181 */         this.out.write(10);
/* 182 */         this.count = 1;
/*     */       }
/* 184 */       this.out.write(c);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.messaging.saaj.packaging.mime.util.QPEncoderStream
 * JD-Core Version:    0.6.2
 */