/*     */ package com.sun.xml.internal.messaging.saaj.packaging.mime.util;
/*     */ 
/*     */ import java.io.FilterInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.PushbackInputStream;
/*     */ 
/*     */ public class QPDecoderStream extends FilterInputStream
/*     */ {
/*  46 */   protected byte[] ba = new byte[2];
/*  47 */   protected int spaces = 0;
/*     */ 
/*     */   public QPDecoderStream(InputStream in)
/*     */   {
/*  55 */     super(new PushbackInputStream(in, 2));
/*     */   }
/*     */ 
/*     */   public int read()
/*     */     throws IOException
/*     */   {
/*  71 */     if (this.spaces > 0)
/*     */     {
/*  73 */       this.spaces -= 1;
/*  74 */       return 32;
/*     */     }
/*     */ 
/*  77 */     int c = this.in.read();
/*     */ 
/*  79 */     if (c == 32)
/*     */     {
/*  81 */       while ((c = this.in.read()) == 32) {
/*  82 */         this.spaces += 1;
/*     */       }
/*  84 */       if ((c == 13) || (c == 10) || (c == -1))
/*     */       {
/*  87 */         this.spaces = 0;
/*     */       }
/*     */       else {
/*  90 */         ((PushbackInputStream)this.in).unread(c);
/*  91 */         c = 32;
/*     */       }
/*  93 */       return c;
/*     */     }
/*  95 */     if (c == 61)
/*     */     {
/*  97 */       int a = this.in.read();
/*     */ 
/*  99 */       if (a == 10)
/*     */       {
/* 105 */         return read();
/* 106 */       }if (a == 13)
/*     */       {
/* 108 */         int b = this.in.read();
/* 109 */         if (b != 10)
/*     */         {
/* 113 */           ((PushbackInputStream)this.in).unread(b);
/* 114 */         }return read();
/* 115 */       }if (a == -1)
/*     */       {
/* 117 */         return -1;
/*     */       }
/* 119 */       this.ba[0] = ((byte)a);
/* 120 */       this.ba[1] = ((byte)this.in.read());
/*     */       try {
/* 122 */         return ASCIIUtility.parseInt(this.ba, 0, 2, 16);
/*     */       }
/*     */       catch (NumberFormatException nex)
/*     */       {
/* 131 */         ((PushbackInputStream)this.in).unread(this.ba);
/* 132 */         return c;
/*     */       }
/*     */     }
/*     */ 
/* 136 */     return c;
/*     */   }
/*     */ 
/*     */   public int read(byte[] buf, int off, int len)
/*     */     throws IOException
/*     */   {
/* 155 */     for (int i = 0; i < len; i++)
/*     */     {
/*     */       int c;
/* 156 */       if ((c = read()) == -1) {
/* 157 */         if (i != 0) break;
/* 158 */         i = -1; break;
/*     */       }
/*     */ 
/* 161 */       buf[(off + i)] = ((byte)c);
/*     */     }
/* 163 */     return i;
/*     */   }
/*     */ 
/*     */   public boolean markSupported()
/*     */   {
/* 171 */     return false;
/*     */   }
/*     */ 
/*     */   public int available()
/*     */     throws IOException
/*     */   {
/* 184 */     return this.in.available();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.messaging.saaj.packaging.mime.util.QPDecoderStream
 * JD-Core Version:    0.6.2
 */