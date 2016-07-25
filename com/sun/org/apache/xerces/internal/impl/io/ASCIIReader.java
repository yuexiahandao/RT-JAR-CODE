/*     */ package com.sun.org.apache.xerces.internal.impl.io;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.util.MessageFormatter;
/*     */ import com.sun.xml.internal.stream.util.BufferAllocator;
/*     */ import com.sun.xml.internal.stream.util.ThreadLocalBufferAllocator;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.Reader;
/*     */ import java.util.Locale;
/*     */ 
/*     */ public class ASCIIReader extends Reader
/*     */ {
/*     */   public static final int DEFAULT_BUFFER_SIZE = 2048;
/*     */   protected InputStream fInputStream;
/*     */   protected byte[] fBuffer;
/*  64 */   private MessageFormatter fFormatter = null;
/*     */ 
/*  67 */   private Locale fLocale = null;
/*     */ 
/*     */   public ASCIIReader(InputStream inputStream, MessageFormatter messageFormatter, Locale locale)
/*     */   {
/*  83 */     this(inputStream, 2048, messageFormatter, locale);
/*     */   }
/*     */ 
/*     */   public ASCIIReader(InputStream inputStream, int size, MessageFormatter messageFormatter, Locale locale)
/*     */   {
/*  97 */     this.fInputStream = inputStream;
/*  98 */     BufferAllocator ba = ThreadLocalBufferAllocator.getBufferAllocator();
/*  99 */     this.fBuffer = ba.getByteBuffer(size);
/* 100 */     if (this.fBuffer == null) {
/* 101 */       this.fBuffer = new byte[size];
/*     */     }
/* 103 */     this.fFormatter = messageFormatter;
/* 104 */     this.fLocale = locale;
/*     */   }
/*     */ 
/*     */   public int read()
/*     */     throws IOException
/*     */   {
/* 125 */     int b0 = this.fInputStream.read();
/* 126 */     if (b0 >= 128) {
/* 127 */       throw new MalformedByteSequenceException(this.fFormatter, this.fLocale, "http://www.w3.org/TR/1998/REC-xml-19980210", "InvalidASCII", new Object[] { Integer.toString(b0) });
/*     */     }
/*     */ 
/* 131 */     return b0;
/*     */   }
/*     */ 
/*     */   public int read(char[] ch, int offset, int length)
/*     */     throws IOException
/*     */   {
/* 149 */     if (length > this.fBuffer.length) {
/* 150 */       length = this.fBuffer.length;
/*     */     }
/* 152 */     int count = this.fInputStream.read(this.fBuffer, 0, length);
/* 153 */     for (int i = 0; i < count; i++) {
/* 154 */       int b0 = this.fBuffer[i];
/* 155 */       if (b0 < 0) {
/* 156 */         throw new MalformedByteSequenceException(this.fFormatter, this.fLocale, "http://www.w3.org/TR/1998/REC-xml-19980210", "InvalidASCII", new Object[] { Integer.toString(b0 & 0xFF) });
/*     */       }
/*     */ 
/* 160 */       ch[(offset + i)] = ((char)b0);
/*     */     }
/* 162 */     return count;
/*     */   }
/*     */ 
/*     */   public long skip(long n)
/*     */     throws IOException
/*     */   {
/* 176 */     return this.fInputStream.skip(n);
/*     */   }
/*     */ 
/*     */   public boolean ready()
/*     */     throws IOException
/*     */   {
/* 189 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean markSupported()
/*     */   {
/* 196 */     return this.fInputStream.markSupported();
/*     */   }
/*     */ 
/*     */   public void mark(int readAheadLimit)
/*     */     throws IOException
/*     */   {
/* 213 */     this.fInputStream.mark(readAheadLimit);
/*     */   }
/*     */ 
/*     */   public void reset()
/*     */     throws IOException
/*     */   {
/* 230 */     this.fInputStream.reset();
/*     */   }
/*     */ 
/*     */   public void close()
/*     */     throws IOException
/*     */   {
/* 241 */     BufferAllocator ba = ThreadLocalBufferAllocator.getBufferAllocator();
/* 242 */     ba.returnByteBuffer(this.fBuffer);
/* 243 */     this.fBuffer = null;
/* 244 */     this.fInputStream.close();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.io.ASCIIReader
 * JD-Core Version:    0.6.2
 */