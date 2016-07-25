/*     */ package com.sun.xml.internal.org.jvnet.staxex;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import javax.activation.DataHandler;
/*     */ import javax.activation.DataSource;
/*     */ 
/*     */ public class Base64Data
/*     */   implements CharSequence, Cloneable
/*     */ {
/*     */   private DataHandler dataHandler;
/*     */   private byte[] data;
/*     */   private int dataLen;
/*     */   private boolean dataCloneByRef;
/*     */   private String mimeType;
/*     */ 
/*     */   public Base64Data()
/*     */   {
/*     */   }
/*     */ 
/*     */   public Base64Data(Base64Data that)
/*     */   {
/*  83 */     that.get();
/*  84 */     if (that.dataCloneByRef) {
/*  85 */       this.data = that.data;
/*     */     } else {
/*  87 */       this.data = new byte[that.dataLen];
/*  88 */       System.arraycopy(that.data, 0, this.data, 0, that.dataLen);
/*     */     }
/*     */ 
/*  91 */     this.dataCloneByRef = true;
/*  92 */     this.dataLen = that.dataLen;
/*  93 */     this.dataHandler = null;
/*  94 */     this.mimeType = that.mimeType;
/*     */   }
/*     */ 
/*     */   public void set(byte[] data, int len, String mimeType, boolean cloneByRef)
/*     */   {
/* 108 */     this.data = data;
/* 109 */     this.dataLen = len;
/* 110 */     this.dataCloneByRef = cloneByRef;
/* 111 */     this.dataHandler = null;
/* 112 */     this.mimeType = mimeType;
/*     */   }
/*     */ 
/*     */   public void set(byte[] data, int len, String mimeType)
/*     */   {
/* 124 */     set(data, len, mimeType, false);
/*     */   }
/*     */ 
/*     */   public void set(byte[] data, String mimeType)
/*     */   {
/* 135 */     set(data, data.length, mimeType, false);
/*     */   }
/*     */ 
/*     */   public void set(DataHandler data)
/*     */   {
/* 144 */     assert (data != null);
/* 145 */     this.dataHandler = data;
/* 146 */     this.data = null;
/*     */   }
/*     */ 
/*     */   public DataHandler getDataHandler()
/*     */   {
/* 157 */     if (this.dataHandler == null)
/* 158 */       this.dataHandler = new Base64StreamingDataHandler(new Base64DataSource(null));
/* 159 */     else if (!(this.dataHandler instanceof StreamingDataHandler)) {
/* 160 */       this.dataHandler = new FilterDataHandler(this.dataHandler);
/*     */     }
/* 162 */     return this.dataHandler;
/*     */   }
/*     */ 
/*     */   public byte[] getExact()
/*     */   {
/* 261 */     get();
/* 262 */     if (this.dataLen != this.data.length) {
/* 263 */       byte[] buf = new byte[this.dataLen];
/* 264 */       System.arraycopy(this.data, 0, buf, 0, this.dataLen);
/* 265 */       this.data = buf;
/*     */     }
/* 267 */     return this.data;
/*     */   }
/*     */ 
/*     */   public InputStream getInputStream()
/*     */     throws IOException
/*     */   {
/* 277 */     if (this.dataHandler != null) {
/* 278 */       return this.dataHandler.getInputStream();
/*     */     }
/* 280 */     return new ByteArrayInputStream(this.data, 0, this.dataLen);
/*     */   }
/*     */ 
/*     */   public boolean hasData()
/*     */   {
/* 290 */     return this.data != null;
/*     */   }
/*     */ 
/*     */   public byte[] get()
/*     */   {
/* 299 */     if (this.data == null) {
/*     */       try {
/* 301 */         ByteArrayOutputStreamEx baos = new ByteArrayOutputStreamEx(1024);
/* 302 */         InputStream is = this.dataHandler.getDataSource().getInputStream();
/* 303 */         baos.readFrom(is);
/* 304 */         is.close();
/* 305 */         this.data = baos.getBuffer();
/* 306 */         this.dataLen = baos.size();
/* 307 */         this.dataCloneByRef = true;
/*     */       }
/*     */       catch (IOException e) {
/* 310 */         this.dataLen = 0;
/*     */       }
/*     */     }
/* 313 */     return this.data;
/*     */   }
/*     */ 
/*     */   public int getDataLen()
/*     */   {
/* 327 */     get();
/* 328 */     return this.dataLen;
/*     */   }
/*     */ 
/*     */   public String getMimeType() {
/* 332 */     if (this.mimeType == null)
/* 333 */       return "application/octet-stream";
/* 334 */     return this.mimeType;
/*     */   }
/*     */ 
/*     */   public int length()
/*     */   {
/* 344 */     get();
/* 345 */     return (this.dataLen + 2) / 3 * 4;
/*     */   }
/*     */ 
/*     */   public char charAt(int index)
/*     */   {
/* 357 */     int offset = index % 4;
/* 358 */     int base = index / 4 * 3;
/*     */     byte b1;
/* 362 */     switch (offset) {
/*     */     case 0:
/* 364 */       return Base64Encoder.encode(this.data[base] >> 2);
/*     */     case 1:
/*     */       byte b1;
/* 366 */       if (base + 1 < this.dataLen)
/* 367 */         b1 = this.data[(base + 1)];
/*     */       else
/* 369 */         b1 = 0;
/* 370 */       return Base64Encoder.encode((this.data[base] & 0x3) << 4 | b1 >> 4 & 0xF);
/*     */     case 2:
/* 374 */       if (base + 1 < this.dataLen) {
/* 375 */         b1 = this.data[(base + 1)];
/*     */         byte b2;
/*     */         byte b2;
/* 376 */         if (base + 2 < this.dataLen)
/* 377 */           b2 = this.data[(base + 2)];
/*     */         else {
/* 379 */           b2 = 0;
/*     */         }
/* 381 */         return Base64Encoder.encode((b1 & 0xF) << 2 | b2 >> 6 & 0x3);
/*     */       }
/*     */ 
/* 385 */       return '=';
/*     */     case 3:
/* 387 */       if (base + 2 < this.dataLen) {
/* 388 */         return Base64Encoder.encode(this.data[(base + 2)] & 0x3F);
/*     */       }
/* 390 */       return '=';
/*     */     }
/*     */ 
/* 393 */     throw new IllegalStateException();
/*     */   }
/*     */ 
/*     */   public CharSequence subSequence(int start, int end)
/*     */   {
/* 402 */     StringBuilder buf = new StringBuilder();
/* 403 */     get();
/* 404 */     for (int i = start; i < end; i++)
/* 405 */       buf.append(charAt(i));
/* 406 */     return buf;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 413 */     get();
/* 414 */     return Base64Encoder.print(this.data, 0, this.dataLen);
/*     */   }
/*     */ 
/*     */   public void writeTo(char[] buf, int start) {
/* 418 */     get();
/* 419 */     Base64Encoder.print(this.data, 0, this.dataLen, buf, start);
/*     */   }
/*     */ 
/*     */   public Base64Data clone() {
/* 423 */     return new Base64Data(this);
/*     */   }
/*     */ 
/*     */   private final class Base64DataSource
/*     */     implements DataSource
/*     */   {
/*     */     private Base64DataSource()
/*     */     {
/*     */     }
/*     */ 
/*     */     public String getContentType()
/*     */     {
/* 167 */       return Base64Data.this.getMimeType();
/*     */     }
/*     */ 
/*     */     public InputStream getInputStream() {
/* 171 */       return new ByteArrayInputStream(Base64Data.this.data, 0, Base64Data.this.dataLen);
/*     */     }
/*     */ 
/*     */     public String getName() {
/* 175 */       return null;
/*     */     }
/*     */ 
/*     */     public OutputStream getOutputStream() {
/* 179 */       throw new UnsupportedOperationException();
/*     */     }
/*     */   }
/*     */ 
/*     */   private final class Base64StreamingDataHandler extends StreamingDataHandler
/*     */   {
/*     */     Base64StreamingDataHandler(DataSource source)
/*     */     {
/* 187 */       super();
/*     */     }
/*     */ 
/*     */     public InputStream readOnce() throws IOException {
/* 191 */       return getDataSource().getInputStream();
/*     */     }
/*     */ 
/*     */     public void moveTo(File dst) throws IOException {
/* 195 */       FileOutputStream fout = new FileOutputStream(dst);
/*     */       try {
/* 197 */         fout.write(Base64Data.this.data, 0, Base64Data.this.dataLen);
/*     */       } finally {
/* 199 */         fout.close();
/*     */       }
/*     */     }
/*     */ 
/*     */     public void close() throws IOException
/*     */     {
/*     */     }
/*     */   }
/*     */ 
/*     */   private static final class FilterDataHandler extends StreamingDataHandler
/*     */   {
/*     */     FilterDataHandler(DataHandler dh) {
/* 211 */       super();
/*     */     }
/*     */ 
/*     */     public InputStream readOnce() throws IOException {
/* 215 */       return getDataSource().getInputStream();
/*     */     }
/*     */ 
/*     */     public void moveTo(File dst) throws IOException {
/* 219 */       byte[] buf = new byte[8192];
/* 220 */       InputStream in = null;
/* 221 */       OutputStream out = null;
/*     */       try {
/* 223 */         in = getDataSource().getInputStream();
/* 224 */         out = new FileOutputStream(dst);
/*     */         while (true) {
/* 226 */           int amountRead = in.read(buf);
/* 227 */           if (amountRead == -1) {
/*     */             break;
/*     */           }
/* 230 */           out.write(buf, 0, amountRead);
/*     */         }
/*     */       } finally {
/* 233 */         if (in != null)
/*     */           try {
/* 235 */             in.close();
/*     */           }
/*     */           catch (IOException ioe)
/*     */           {
/*     */           }
/* 240 */         if (out != null)
/*     */           try {
/* 242 */             out.close();
/*     */           }
/*     */           catch (IOException ioe)
/*     */           {
/*     */           }
/*     */       }
/*     */     }
/*     */ 
/*     */     public void close()
/*     */       throws IOException
/*     */     {
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.org.jvnet.staxex.Base64Data
 * JD-Core Version:    0.6.2
 */