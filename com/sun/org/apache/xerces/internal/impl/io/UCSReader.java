/*     */ package com.sun.org.apache.xerces.internal.impl.io;
/*     */ 
/*     */ import com.sun.xml.internal.stream.util.BufferAllocator;
/*     */ import com.sun.xml.internal.stream.util.ThreadLocalBufferAllocator;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.io.Reader;
/*     */ 
/*     */ public class UCSReader extends Reader
/*     */ {
/*     */   public static final int DEFAULT_BUFFER_SIZE = 8192;
/*     */   public static final short UCS2LE = 1;
/*     */   public static final short UCS2BE = 2;
/*     */   public static final short UCS4LE = 4;
/*     */   public static final short UCS4BE = 8;
/*     */   protected InputStream fInputStream;
/*     */   protected byte[] fBuffer;
/*     */   protected short fEncoding;
/*     */ 
/*     */   public UCSReader(InputStream inputStream, short encoding)
/*     */   {
/*  82 */     this(inputStream, 8192, encoding);
/*     */   }
/*     */ 
/*     */   public UCSReader(InputStream inputStream, int size, short encoding)
/*     */   {
/*  95 */     this.fInputStream = inputStream;
/*  96 */     BufferAllocator ba = ThreadLocalBufferAllocator.getBufferAllocator();
/*  97 */     this.fBuffer = ba.getByteBuffer(size);
/*  98 */     if (this.fBuffer == null) {
/*  99 */       this.fBuffer = new byte[size];
/*     */     }
/* 101 */     this.fEncoding = encoding;
/*     */   }
/*     */ 
/*     */   public int read()
/*     */     throws IOException
/*     */   {
/* 122 */     int b0 = this.fInputStream.read() & 0xFF;
/* 123 */     if (b0 == 255)
/* 124 */       return -1;
/* 125 */     int b1 = this.fInputStream.read() & 0xFF;
/* 126 */     if (b1 == 255)
/* 127 */       return -1;
/* 128 */     if (this.fEncoding >= 4) {
/* 129 */       int b2 = this.fInputStream.read() & 0xFF;
/* 130 */       if (b2 == 255)
/* 131 */         return -1;
/* 132 */       int b3 = this.fInputStream.read() & 0xFF;
/* 133 */       if (b3 == 255)
/* 134 */         return -1;
/* 135 */       System.err.println("b0 is " + (b0 & 0xFF) + " b1 " + (b1 & 0xFF) + " b2 " + (b2 & 0xFF) + " b3 " + (b3 & 0xFF));
/* 136 */       if (this.fEncoding == 8) {
/* 137 */         return (b0 << 24) + (b1 << 16) + (b2 << 8) + b3;
/*     */       }
/* 139 */       return (b3 << 24) + (b2 << 16) + (b1 << 8) + b0;
/*     */     }
/* 141 */     if (this.fEncoding == 2) {
/* 142 */       return (b0 << 8) + b1;
/*     */     }
/* 144 */     return (b1 << 8) + b0;
/*     */   }
/*     */ 
/*     */   public int read(char[] ch, int offset, int length)
/*     */     throws IOException
/*     */   {
/* 163 */     int byteLength = length << (this.fEncoding >= 4 ? 2 : 1);
/* 164 */     if (byteLength > this.fBuffer.length) {
/* 165 */       byteLength = this.fBuffer.length;
/*     */     }
/* 167 */     int count = this.fInputStream.read(this.fBuffer, 0, byteLength);
/* 168 */     if (count == -1) return -1;
/*     */ 
/* 170 */     if (this.fEncoding >= 4)
/*     */     {
/* 172 */       int numToRead = 4 - (count & 0x3) & 0x3;
/* 173 */       for (int i = 0; i < numToRead; i++) {
/* 174 */         int charRead = this.fInputStream.read();
/* 175 */         if (charRead == -1) {
/* 176 */           for (int j = i; j < numToRead; j++)
/* 177 */             this.fBuffer[(count + j)] = 0;
/* 178 */           break;
/*     */         }
/* 180 */         this.fBuffer[(count + i)] = ((byte)charRead);
/*     */       }
/*     */ 
/* 183 */       count += numToRead;
/*     */     } else {
/* 185 */       int numToRead = count & 0x1;
/* 186 */       if (numToRead != 0) {
/* 187 */         count++;
/* 188 */         int charRead = this.fInputStream.read();
/* 189 */         if (charRead == -1)
/* 190 */           this.fBuffer[count] = 0;
/*     */         else {
/* 192 */           this.fBuffer[count] = ((byte)charRead);
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 198 */     int numChars = count >> (this.fEncoding >= 4 ? 2 : 1);
/* 199 */     int curPos = 0;
/* 200 */     for (int i = 0; i < numChars; i++) {
/* 201 */       int b0 = this.fBuffer[(curPos++)] & 0xFF;
/* 202 */       int b1 = this.fBuffer[(curPos++)] & 0xFF;
/* 203 */       if (this.fEncoding >= 4) {
/* 204 */         int b2 = this.fBuffer[(curPos++)] & 0xFF;
/* 205 */         int b3 = this.fBuffer[(curPos++)] & 0xFF;
/* 206 */         if (this.fEncoding == 8)
/* 207 */           ch[(offset + i)] = ((char)((b0 << 24) + (b1 << 16) + (b2 << 8) + b3));
/*     */         else
/* 209 */           ch[(offset + i)] = ((char)((b3 << 24) + (b2 << 16) + (b1 << 8) + b0));
/*     */       }
/* 211 */       else if (this.fEncoding == 2) {
/* 212 */         ch[(offset + i)] = ((char)((b0 << 8) + b1));
/*     */       } else {
/* 214 */         ch[(offset + i)] = ((char)((b1 << 8) + b0));
/*     */       }
/*     */     }
/* 217 */     return numChars;
/*     */   }
/*     */ 
/*     */   public long skip(long n)
/*     */     throws IOException
/*     */   {
/* 237 */     int charWidth = this.fEncoding >= 4 ? 2 : 1;
/* 238 */     long bytesSkipped = this.fInputStream.skip(n << charWidth);
/* 239 */     if ((bytesSkipped & (charWidth | 0x1)) == 0L) return bytesSkipped >> charWidth;
/* 240 */     return (bytesSkipped >> charWidth) + 1L;
/*     */   }
/*     */ 
/*     */   public boolean ready()
/*     */     throws IOException
/*     */   {
/* 253 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean markSupported()
/*     */   {
/* 260 */     return this.fInputStream.markSupported();
/*     */   }
/*     */ 
/*     */   public void mark(int readAheadLimit)
/*     */     throws IOException
/*     */   {
/* 277 */     this.fInputStream.mark(readAheadLimit);
/*     */   }
/*     */ 
/*     */   public void reset()
/*     */     throws IOException
/*     */   {
/* 294 */     this.fInputStream.reset();
/*     */   }
/*     */ 
/*     */   public void close()
/*     */     throws IOException
/*     */   {
/* 305 */     BufferAllocator ba = ThreadLocalBufferAllocator.getBufferAllocator();
/* 306 */     ba.returnByteBuffer(this.fBuffer);
/* 307 */     this.fBuffer = null;
/* 308 */     this.fInputStream.close();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.io.UCSReader
 * JD-Core Version:    0.6.2
 */