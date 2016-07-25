/*     */ package com.sun.media.sound;
/*     */ 
/*     */ import java.io.EOFException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import javax.sound.sampled.AudioFormat;
/*     */ import javax.sound.sampled.AudioInputStream;
/*     */ 
/*     */ public final class SoftJitterCorrector extends AudioInputStream
/*     */ {
/*     */   public SoftJitterCorrector(AudioInputStream paramAudioInputStream, int paramInt1, int paramInt2)
/*     */   {
/* 274 */     super(new JitterStream(paramAudioInputStream, paramInt1, paramInt2), paramAudioInputStream.getFormat(), paramAudioInputStream.getFrameLength());
/*     */   }
/*     */ 
/*     */   private static class JitterStream extends InputStream
/*     */   {
/*  43 */     static int MAX_BUFFER_SIZE = 1048576;
/*  44 */     boolean active = true;
/*     */     Thread thread;
/*     */     AudioInputStream stream;
/*  48 */     int writepos = 0;
/*  49 */     int readpos = 0;
/*     */     byte[][] buffers;
/*  51 */     private final Object buffers_mutex = new Object();
/*     */ 
/*  54 */     int w_count = 1000;
/*  55 */     int w_min_tol = 2;
/*  56 */     int w_max_tol = 10;
/*  57 */     int w = 0;
/*  58 */     int w_min = -1;
/*     */ 
/*  60 */     int bbuffer_pos = 0;
/*  61 */     int bbuffer_max = 0;
/*  62 */     byte[] bbuffer = null;
/*     */ 
/*     */     public byte[] nextReadBuffer()
/*     */     {
/*     */       int i;
/*  65 */       synchronized (this.buffers_mutex) {
/*  66 */         if (this.writepos > this.readpos) {
/*  67 */           i = this.writepos - this.readpos;
/*  68 */           if (i < this.w_min) {
/*  69 */             this.w_min = i;
/*     */           }
/*  71 */           int j = this.readpos;
/*  72 */           this.readpos += 1;
/*  73 */           return this.buffers[(j % this.buffers.length)];
/*     */         }
/*  75 */         this.w_min = -1;
/*  76 */         this.w = (this.w_count - 1);
/*     */       }
/*     */       while (true) {
/*     */         try {
/*  80 */           Thread.sleep(1L);
/*     */         }
/*     */         catch (InterruptedException ) {
/*  83 */           return null;
/*     */         }
/*  85 */         synchronized (this.buffers_mutex) {
/*  86 */           if (this.writepos > this.readpos) {
/*  87 */             this.w = 0;
/*  88 */             this.w_min = -1;
/*  89 */             this.w = (this.w_count - 1);
/*  90 */             i = this.readpos;
/*  91 */             this.readpos += 1;
/*  92 */             return this.buffers[(i % this.buffers.length)];
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/*     */     public byte[] nextWriteBuffer() {
/*  99 */       synchronized (this.buffers_mutex) {
/* 100 */         return this.buffers[(this.writepos % this.buffers.length)];
/*     */       }
/*     */     }
/*     */ 
/*     */     public void commit() {
/* 105 */       synchronized (this.buffers_mutex) {
/* 106 */         this.writepos += 1;
/* 107 */         if (this.writepos - this.readpos > this.buffers.length) {
/* 108 */           int i = this.writepos - this.readpos + 10;
/* 109 */           i = Math.max(this.buffers.length * 2, i);
/* 110 */           this.buffers = new byte[i][this.buffers[0].length];
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/*     */     JitterStream(AudioInputStream paramAudioInputStream, int paramInt1, int paramInt2)
/*     */     {
/* 117 */       this.w_count = (10 * (paramInt1 / paramInt2));
/* 118 */       if (this.w_count < 100)
/* 119 */         this.w_count = 100;
/* 120 */       this.buffers = new byte[paramInt1 / paramInt2 + 10][paramInt2];
/*     */ 
/* 122 */       this.bbuffer_max = (MAX_BUFFER_SIZE / paramInt2);
/* 123 */       this.stream = paramAudioInputStream;
/*     */ 
/* 126 */       Runnable local1 = new Runnable()
/*     */       {
/*     */         public void run() {
/* 129 */           AudioFormat localAudioFormat = SoftJitterCorrector.JitterStream.this.stream.getFormat();
/* 130 */           int i = SoftJitterCorrector.JitterStream.this.buffers[0].length;
/* 131 */           int j = i / localAudioFormat.getFrameSize();
/* 132 */           long l1 = ()(j * 1000000000.0D / localAudioFormat.getSampleRate());
/*     */ 
/* 134 */           long l2 = System.nanoTime();
/* 135 */           long l3 = l2 + l1;
/* 136 */           int k = 0;
/*     */           while (true) {
/* 138 */             synchronized (SoftJitterCorrector.JitterStream.this) {
/* 139 */               if (!SoftJitterCorrector.JitterStream.this.active)
/* 140 */                 break;
/*     */             }
/*     */             int m;
/* 143 */             synchronized (SoftJitterCorrector.JitterStream.this.buffers) {
/* 144 */               m = SoftJitterCorrector.JitterStream.this.writepos - SoftJitterCorrector.JitterStream.this.readpos;
/* 145 */               if (k == 0) {
/* 146 */                 SoftJitterCorrector.JitterStream.this.w += 1;
/* 147 */                 if ((SoftJitterCorrector.JitterStream.this.w_min != 2147483647) && 
/* 148 */                   (SoftJitterCorrector.JitterStream.this.w == SoftJitterCorrector.JitterStream.this.w_count)) {
/* 149 */                   k = 0;
/* 150 */                   if (SoftJitterCorrector.JitterStream.this.w_min < SoftJitterCorrector.JitterStream.this.w_min_tol) {
/* 151 */                     k = (SoftJitterCorrector.JitterStream.this.w_min_tol + SoftJitterCorrector.JitterStream.this.w_max_tol) / 2 - SoftJitterCorrector.JitterStream.this.w_min;
/*     */                   }
/*     */ 
/* 154 */                   if (SoftJitterCorrector.JitterStream.this.w_min > SoftJitterCorrector.JitterStream.this.w_max_tol) {
/* 155 */                     k = (SoftJitterCorrector.JitterStream.this.w_min_tol + SoftJitterCorrector.JitterStream.this.w_max_tol) / 2 - SoftJitterCorrector.JitterStream.this.w_min;
/*     */                   }
/*     */ 
/* 158 */                   SoftJitterCorrector.JitterStream.this.w = 0;
/* 159 */                   SoftJitterCorrector.JitterStream.this.w_min = 2147483647;
/*     */                 }
/*     */               }
/*     */             }
/*     */ 
/* 164 */             while (m > SoftJitterCorrector.JitterStream.this.bbuffer_max) {
/* 165 */               synchronized (SoftJitterCorrector.JitterStream.this.buffers) {
/* 166 */                 m = SoftJitterCorrector.JitterStream.this.writepos - SoftJitterCorrector.JitterStream.this.readpos;
/*     */               }
/* 168 */               synchronized (SoftJitterCorrector.JitterStream.this) {
/* 169 */                 if (!SoftJitterCorrector.JitterStream.this.active)
/* 170 */                   break;
/*     */               }
/*     */               try {
/* 173 */                 Thread.sleep(1L);
/*     */               }
/*     */               catch (InterruptedException localInterruptedException1)
/*     */               {
/*     */               }
/*     */             }
/* 179 */             if (k < 0) {
/* 180 */               k++;
/*     */             } else {
/* 182 */               byte[] arrayOfByte = SoftJitterCorrector.JitterStream.this.nextWriteBuffer();
/*     */               try {
/* 184 */                 int n = 0;
/* 185 */                 while (n != arrayOfByte.length) {
/* 186 */                   int i1 = SoftJitterCorrector.JitterStream.this.stream.read(arrayOfByte, n, arrayOfByte.length - n);
/*     */ 
/* 188 */                   if (i1 < 0)
/* 189 */                     throw new EOFException();
/* 190 */                   if (i1 == 0)
/* 191 */                     Thread.yield();
/* 192 */                   n += i1;
/*     */                 }
/*     */               }
/*     */               catch (IOException localIOException) {
/*     */               }
/* 197 */               SoftJitterCorrector.JitterStream.this.commit();
/*     */             }
/*     */ 
/* 200 */             if (k > 0) {
/* 201 */               k--;
/* 202 */               l3 = System.nanoTime() + l1;
/*     */             }
/*     */             else {
/* 205 */               long l4 = l3 - System.nanoTime();
/* 206 */               if (l4 > 0L)
/*     */                 try {
/* 208 */                   Thread.sleep(l4 / 1000000L);
/*     */                 }
/*     */                 catch (InterruptedException localInterruptedException2)
/*     */                 {
/*     */                 }
/* 213 */               l3 += l1;
/*     */             }
/*     */           }
/*     */         }
/*     */       };
/* 218 */       this.thread = new Thread(local1);
/* 219 */       this.thread.setDaemon(true);
/* 220 */       this.thread.setPriority(10);
/* 221 */       this.thread.start();
/*     */     }
/*     */ 
/*     */     public void close() throws IOException {
/* 225 */       synchronized (this) {
/* 226 */         this.active = false;
/*     */       }
/*     */       try {
/* 229 */         this.thread.join();
/*     */       }
/*     */       catch (InterruptedException localInterruptedException) {
/*     */       }
/* 233 */       this.stream.close();
/*     */     }
/*     */ 
/*     */     public int read() throws IOException {
/* 237 */       byte[] arrayOfByte = new byte[1];
/* 238 */       if (read(arrayOfByte) == -1)
/* 239 */         return -1;
/* 240 */       return arrayOfByte[0] & 0xFF;
/*     */     }
/*     */ 
/*     */     public void fillBuffer() {
/* 244 */       this.bbuffer = nextReadBuffer();
/* 245 */       this.bbuffer_pos = 0;
/*     */     }
/*     */ 
/*     */     public int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2) {
/* 249 */       if (this.bbuffer == null)
/* 250 */         fillBuffer();
/* 251 */       int i = this.bbuffer.length;
/* 252 */       int j = paramInt1 + paramInt2;
/* 253 */       while (paramInt1 < j) {
/* 254 */         if (available() == 0) {
/* 255 */           fillBuffer();
/*     */         } else {
/* 257 */           byte[] arrayOfByte = this.bbuffer;
/* 258 */           int k = this.bbuffer_pos;
/* 259 */           while ((paramInt1 < j) && (k < i))
/* 260 */             paramArrayOfByte[(paramInt1++)] = arrayOfByte[(k++)];
/* 261 */           this.bbuffer_pos = k;
/*     */         }
/*     */       }
/* 264 */       return paramInt2;
/*     */     }
/*     */ 
/*     */     public int available() {
/* 268 */       return this.bbuffer.length - this.bbuffer_pos;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.media.sound.SoftJitterCorrector
 * JD-Core Version:    0.6.2
 */