/*     */ package com.sun.xml.internal.org.jvnet.mimepull;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.nio.ByteBuffer;
/*     */ 
/*     */ final class DataHead
/*     */ {
/*     */   volatile Chunk head;
/*     */   volatile Chunk tail;
/*     */   DataFile dataFile;
/*     */   private final MIMEPart part;
/*     */   boolean readOnce;
/*     */   volatile long inMemory;
/*     */   private Throwable consumedAt;
/*     */ 
/*     */   DataHead(MIMEPart part)
/*     */   {
/*  64 */     this.part = part;
/*     */   }
/*     */ 
/*     */   void addBody(ByteBuffer buf) {
/*  68 */     synchronized (this) {
/*  69 */       this.inMemory += buf.limit();
/*     */     }
/*  71 */     if (this.tail != null)
/*  72 */       this.tail = this.tail.createNext(this, buf);
/*     */     else
/*  74 */       this.head = (this.tail = new Chunk(new MemoryData(buf, this.part.msg.config)));
/*     */   }
/*     */ 
/*     */   void doneParsing()
/*     */   {
/*     */   }
/*     */ 
/*     */   void moveTo(File f) {
/*  82 */     if (this.dataFile != null)
/*  83 */       this.dataFile.renameTo(f);
/*     */     else
/*     */       try {
/*  86 */         OutputStream os = new FileOutputStream(f);
/*  87 */         InputStream in = readOnce();
/*  88 */         byte[] buf = new byte[8192];
/*     */         int len;
/*  90 */         while ((len = in.read(buf)) != -1) {
/*  91 */           os.write(buf, 0, len);
/*     */         }
/*  93 */         os.close();
/*     */       } catch (IOException ioe) {
/*  95 */         throw new MIMEParsingException(ioe);
/*     */       }
/*     */   }
/*     */ 
/*     */   void close()
/*     */   {
/* 101 */     if (this.dataFile != null) {
/* 102 */       this.head = (this.tail = null);
/* 103 */       this.dataFile.close();
/*     */     }
/*     */   }
/*     */ 
/*     */   public InputStream read()
/*     */   {
/* 118 */     if (this.readOnce) {
/* 119 */       throw new IllegalStateException("readOnce() is called before, read() cannot be called later.");
/*     */     }
/*     */ 
/* 123 */     while (this.tail == null) {
/* 124 */       if (!this.part.msg.makeProgress()) {
/* 125 */         throw new IllegalStateException("No such MIME Part: " + this.part);
/*     */       }
/*     */     }
/*     */ 
/* 129 */     if (this.head == null) {
/* 130 */       throw new IllegalStateException("Already read. Probably readOnce() is called before.");
/*     */     }
/* 132 */     return new ReadMultiStream();
/*     */   }
/*     */ 
/*     */   private boolean unconsumed()
/*     */   {
/* 145 */     if (this.consumedAt != null) {
/* 146 */       AssertionError error = new AssertionError("readOnce() is already called before. See the nested exception from where it's called.");
/* 147 */       error.initCause(this.consumedAt);
/* 148 */       throw error;
/*     */     }
/* 150 */     this.consumedAt = new Exception().fillInStackTrace();
/* 151 */     return true;
/*     */   }
/*     */ 
/*     */   public InputStream readOnce()
/*     */   {
/* 167 */     assert (unconsumed());
/* 168 */     if (this.readOnce) {
/* 169 */       throw new IllegalStateException("readOnce() is called before. It can only be called once.");
/*     */     }
/* 171 */     this.readOnce = true;
/*     */ 
/* 173 */     while (this.tail == null) {
/* 174 */       if ((!this.part.msg.makeProgress()) && (this.tail == null)) {
/* 175 */         throw new IllegalStateException("No such Part: " + this.part);
/*     */       }
/*     */     }
/* 178 */     InputStream in = new ReadOnceStream();
/* 179 */     this.head = null;
/* 180 */     return in;
/*     */   }
/*     */   class ReadMultiStream extends InputStream { Chunk current;
/*     */     int offset;
/*     */     int len;
/*     */     byte[] buf;
/*     */ 
/* 190 */     public ReadMultiStream() { this.current = DataHead.this.head;
/* 191 */       this.len = this.current.data.size();
/* 192 */       this.buf = this.current.data.read(); }
/*     */ 
/*     */     public int read(byte[] b, int off, int sz)
/*     */       throws IOException
/*     */     {
/* 197 */       if (!fetch()) return -1;
/*     */ 
/* 199 */       sz = Math.min(sz, this.len - this.offset);
/* 200 */       System.arraycopy(this.buf, this.offset, b, off, sz);
/* 201 */       this.offset += sz;
/* 202 */       return sz;
/*     */     }
/*     */ 
/*     */     public int read() throws IOException {
/* 206 */       if (!fetch()) {
/* 207 */         return -1;
/*     */       }
/* 209 */       return this.buf[(this.offset++)] & 0xFF;
/*     */     }
/*     */ 
/*     */     void adjustInMemoryUsage()
/*     */     {
/*     */     }
/*     */ 
/*     */     private boolean fetch()
/*     */     {
/* 221 */       if (this.current == null) {
/* 222 */         throw new IllegalStateException("Stream already closed");
/*     */       }
/* 224 */       while (this.offset == this.len) {
/* 225 */         while ((!DataHead.this.part.parsed) && (this.current.next == null)) {
/* 226 */           DataHead.this.part.msg.makeProgress();
/*     */         }
/* 228 */         this.current = this.current.next;
/*     */ 
/* 230 */         if (this.current == null) {
/* 231 */           return false;
/*     */         }
/* 233 */         adjustInMemoryUsage();
/* 234 */         this.offset = 0;
/* 235 */         this.buf = this.current.data.read();
/* 236 */         this.len = this.current.data.size();
/*     */       }
/* 238 */       return true;
/*     */     }
/*     */ 
/*     */     public void close() throws IOException {
/* 242 */       super.close();
/* 243 */       this.current = null;
/*     */     } } 
/*     */   final class ReadOnceStream extends DataHead.ReadMultiStream {
/*     */     ReadOnceStream() {
/* 247 */       super();
/*     */     }
/*     */ 
/*     */     void adjustInMemoryUsage() {
/* 251 */       synchronized (DataHead.this) {
/* 252 */         DataHead.this.inMemory -= this.current.data.size();
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.org.jvnet.mimepull.DataHead
 * JD-Core Version:    0.6.2
 */