/*     */ package com.sun.media.sound;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.RandomAccessFile;
/*     */ import java.util.Collection;
/*     */ 
/*     */ public final class ModelByteBuffer
/*     */ {
/*  43 */   private ModelByteBuffer root = this;
/*     */   private File file;
/*     */   private long fileoffset;
/*     */   private byte[] buffer;
/*     */   private long offset;
/*     */   private final long len;
/*     */ 
/*     */   private ModelByteBuffer(ModelByteBuffer paramModelByteBuffer, long paramLong1, long paramLong2, boolean paramBoolean)
/*     */   {
/* 140 */     this.root = paramModelByteBuffer.root;
/* 141 */     this.offset = 0L;
/* 142 */     long l = paramModelByteBuffer.len;
/* 143 */     if (paramLong1 < 0L)
/* 144 */       paramLong1 = 0L;
/* 145 */     if (paramLong1 > l)
/* 146 */       paramLong1 = l;
/* 147 */     if (paramLong2 < 0L)
/* 148 */       paramLong2 = 0L;
/* 149 */     if (paramLong2 > l)
/* 150 */       paramLong2 = l;
/* 151 */     if (paramLong1 > paramLong2)
/* 152 */       paramLong1 = paramLong2;
/* 153 */     this.offset = paramLong1;
/* 154 */     this.len = (paramLong2 - paramLong1);
/* 155 */     if (paramBoolean) {
/* 156 */       this.buffer = this.root.buffer;
/* 157 */       if (this.root.file != null) {
/* 158 */         this.file = this.root.file;
/* 159 */         this.fileoffset = (this.root.fileoffset + arrayOffset());
/* 160 */         this.offset = 0L;
/*     */       } else {
/* 162 */         this.offset = arrayOffset();
/* 163 */       }this.root = this;
/*     */     }
/*     */   }
/*     */ 
/*     */   public ModelByteBuffer(byte[] paramArrayOfByte) {
/* 168 */     this.buffer = paramArrayOfByte;
/* 169 */     this.offset = 0L;
/* 170 */     this.len = paramArrayOfByte.length;
/*     */   }
/*     */ 
/*     */   public ModelByteBuffer(byte[] paramArrayOfByte, int paramInt1, int paramInt2) {
/* 174 */     this.buffer = paramArrayOfByte;
/* 175 */     this.offset = paramInt1;
/* 176 */     this.len = paramInt2;
/*     */   }
/*     */ 
/*     */   public ModelByteBuffer(File paramFile) {
/* 180 */     this.file = paramFile;
/* 181 */     this.fileoffset = 0L;
/* 182 */     this.len = paramFile.length();
/*     */   }
/*     */ 
/*     */   public ModelByteBuffer(File paramFile, long paramLong1, long paramLong2) {
/* 186 */     this.file = paramFile;
/* 187 */     this.fileoffset = paramLong1;
/* 188 */     this.len = paramLong2;
/*     */   }
/*     */ 
/*     */   public void writeTo(OutputStream paramOutputStream) throws IOException {
/* 192 */     if ((this.root.file != null) && (this.root.buffer == null)) {
/* 193 */       InputStream localInputStream = getInputStream();
/* 194 */       byte[] arrayOfByte = new byte[1024];
/*     */       int i;
/* 196 */       while ((i = localInputStream.read(arrayOfByte)) != -1)
/* 197 */         paramOutputStream.write(arrayOfByte, 0, i);
/*     */     } else {
/* 199 */       paramOutputStream.write(array(), (int)arrayOffset(), (int)capacity());
/*     */     }
/*     */   }
/*     */ 
/* 203 */   public InputStream getInputStream() { if ((this.root.file != null) && (this.root.buffer == null)) {
/*     */       try {
/* 205 */         return new RandomFileInputStream();
/*     */       }
/*     */       catch (IOException localIOException) {
/* 208 */         return null;
/*     */       }
/*     */     }
/* 211 */     return new ByteArrayInputStream(array(), (int)arrayOffset(), (int)capacity());
/*     */   }
/*     */ 
/*     */   public ModelByteBuffer subbuffer(long paramLong)
/*     */   {
/* 216 */     return subbuffer(paramLong, capacity());
/*     */   }
/*     */ 
/*     */   public ModelByteBuffer subbuffer(long paramLong1, long paramLong2) {
/* 220 */     return subbuffer(paramLong1, paramLong2, false);
/*     */   }
/*     */ 
/*     */   public ModelByteBuffer subbuffer(long paramLong1, long paramLong2, boolean paramBoolean)
/*     */   {
/* 225 */     return new ModelByteBuffer(this, paramLong1, paramLong2, paramBoolean);
/*     */   }
/*     */ 
/*     */   public byte[] array() {
/* 229 */     return this.root.buffer;
/*     */   }
/*     */ 
/*     */   public long arrayOffset() {
/* 233 */     if (this.root != this)
/* 234 */       return this.root.arrayOffset() + this.offset;
/* 235 */     return this.offset;
/*     */   }
/*     */ 
/*     */   public long capacity() {
/* 239 */     return this.len;
/*     */   }
/*     */ 
/*     */   public ModelByteBuffer getRoot() {
/* 243 */     return this.root;
/*     */   }
/*     */ 
/*     */   public File getFile() {
/* 247 */     return this.file;
/*     */   }
/*     */ 
/*     */   public long getFilePointer() {
/* 251 */     return this.fileoffset;
/*     */   }
/*     */ 
/*     */   public static void loadAll(Collection<ModelByteBuffer> paramCollection) throws IOException
/*     */   {
/* 256 */     File localFile = null;
/* 257 */     RandomAccessFile localRandomAccessFile = null;
/*     */     try {
/* 259 */       for (ModelByteBuffer localModelByteBuffer : paramCollection) {
/* 260 */         localModelByteBuffer = localModelByteBuffer.root;
/* 261 */         if ((localModelByteBuffer.file != null) && 
/* 263 */           (localModelByteBuffer.buffer == null))
/*     */         {
/* 265 */           if ((localFile == null) || (!localFile.equals(localModelByteBuffer.file))) {
/* 266 */             if (localRandomAccessFile != null) {
/* 267 */               localRandomAccessFile.close();
/* 268 */               localRandomAccessFile = null;
/*     */             }
/* 270 */             localFile = localModelByteBuffer.file;
/* 271 */             localRandomAccessFile = new RandomAccessFile(localModelByteBuffer.file, "r");
/*     */           }
/* 273 */           localRandomAccessFile.seek(localModelByteBuffer.fileoffset);
/* 274 */           byte[] arrayOfByte = new byte[(int)localModelByteBuffer.capacity()];
/*     */ 
/* 276 */           int i = 0;
/* 277 */           int j = arrayOfByte.length;
/* 278 */           while (i != j) {
/* 279 */             if (j - i > 65536) {
/* 280 */               localRandomAccessFile.readFully(arrayOfByte, i, 65536);
/* 281 */               i += 65536;
/*     */             } else {
/* 283 */               localRandomAccessFile.readFully(arrayOfByte, i, j - i);
/* 284 */               i = j;
/*     */             }
/*     */ 
/*     */           }
/*     */ 
/* 289 */           localModelByteBuffer.buffer = arrayOfByte;
/* 290 */           localModelByteBuffer.offset = 0L;
/*     */         }
/*     */       }
/*     */     } finally { if (localRandomAccessFile != null)
/* 294 */         localRandomAccessFile.close(); }
/*     */   }
/*     */ 
/*     */   public void load() throws IOException
/*     */   {
/* 299 */     if (this.root != this) {
/* 300 */       this.root.load();
/* 301 */       return;
/*     */     }
/* 303 */     if (this.buffer != null)
/* 304 */       return;
/* 305 */     if (this.file == null) {
/* 306 */       throw new IllegalStateException("No file associated with this ByteBuffer!");
/*     */     }
/*     */ 
/* 310 */     DataInputStream localDataInputStream = new DataInputStream(getInputStream());
/* 311 */     this.buffer = new byte[(int)capacity()];
/* 312 */     this.offset = 0L;
/* 313 */     localDataInputStream.readFully(this.buffer);
/* 314 */     localDataInputStream.close();
/*     */   }
/*     */ 
/*     */   public void unload()
/*     */   {
/* 319 */     if (this.root != this) {
/* 320 */       this.root.unload();
/* 321 */       return;
/*     */     }
/* 323 */     if (this.file == null) {
/* 324 */       throw new IllegalStateException("No file associated with this ByteBuffer!");
/*     */     }
/*     */ 
/* 327 */     this.root.buffer = null;
/*     */   }
/*     */ 
/*     */   private class RandomFileInputStream extends InputStream
/*     */   {
/*     */     private final RandomAccessFile raf;
/*     */     private long left;
/*  54 */     private long mark = 0L;
/*  55 */     private long markleft = 0L;
/*     */ 
/*     */     RandomFileInputStream() throws IOException {
/*  58 */       this.raf = new RandomAccessFile(ModelByteBuffer.access$000(ModelByteBuffer.this).file, "r");
/*  59 */       this.raf.seek(ModelByteBuffer.access$000(ModelByteBuffer.this).fileoffset + ModelByteBuffer.this.arrayOffset());
/*  60 */       this.left = ModelByteBuffer.this.capacity();
/*     */     }
/*     */ 
/*     */     public int available() throws IOException {
/*  64 */       if (this.left > 2147483647L)
/*  65 */         return 2147483647;
/*  66 */       return (int)this.left;
/*     */     }
/*     */ 
/*     */     public synchronized void mark(int paramInt) {
/*     */       try {
/*  71 */         this.mark = this.raf.getFilePointer();
/*  72 */         this.markleft = this.left;
/*     */       }
/*     */       catch (IOException localIOException) {
/*     */       }
/*     */     }
/*     */ 
/*     */     public boolean markSupported() {
/*  79 */       return true;
/*     */     }
/*     */ 
/*     */     public synchronized void reset() throws IOException {
/*  83 */       this.raf.seek(this.mark);
/*  84 */       this.left = this.markleft;
/*     */     }
/*     */ 
/*     */     public long skip(long paramLong) throws IOException {
/*  88 */       if (paramLong < 0L)
/*  89 */         return 0L;
/*  90 */       if (paramLong > this.left)
/*  91 */         paramLong = this.left;
/*  92 */       long l = this.raf.getFilePointer();
/*  93 */       this.raf.seek(l + paramLong);
/*  94 */       this.left -= paramLong;
/*  95 */       return paramLong;
/*     */     }
/*     */ 
/*     */     public int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2) throws IOException {
/*  99 */       if (paramInt2 > this.left)
/* 100 */         paramInt2 = (int)this.left;
/* 101 */       if (this.left == 0L)
/* 102 */         return -1;
/* 103 */       paramInt2 = this.raf.read(paramArrayOfByte, paramInt1, paramInt2);
/* 104 */       if (paramInt2 == -1)
/* 105 */         return -1;
/* 106 */       this.left -= paramInt2;
/* 107 */       return paramInt2;
/*     */     }
/*     */ 
/*     */     public int read(byte[] paramArrayOfByte) throws IOException {
/* 111 */       int i = paramArrayOfByte.length;
/* 112 */       if (i > this.left)
/* 113 */         i = (int)this.left;
/* 114 */       if (this.left == 0L)
/* 115 */         return -1;
/* 116 */       i = this.raf.read(paramArrayOfByte, 0, i);
/* 117 */       if (i == -1)
/* 118 */         return -1;
/* 119 */       this.left -= i;
/* 120 */       return i;
/*     */     }
/*     */ 
/*     */     public int read() throws IOException {
/* 124 */       if (this.left == 0L)
/* 125 */         return -1;
/* 126 */       int i = this.raf.read();
/* 127 */       if (i == -1)
/* 128 */         return -1;
/* 129 */       this.left -= 1L;
/* 130 */       return i;
/*     */     }
/*     */ 
/*     */     public void close() throws IOException {
/* 134 */       this.raf.close();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.media.sound.ModelByteBuffer
 * JD-Core Version:    0.6.2
 */